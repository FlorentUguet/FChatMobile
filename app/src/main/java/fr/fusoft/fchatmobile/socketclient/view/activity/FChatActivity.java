package fr.fusoft.fchatmobile.socketclient.view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.login.model.LoginTicket;
import fr.fusoft.fchatmobile.login.view.LoginActivity;
import fr.fusoft.fchatmobile.socketclient.controller.FClient;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.view.fragment.ChannelFragment;
import fr.fusoft.fchatmobile.socketclient.view.fragment.DebugFragment;

/**
 * Created by Florent on 05/09/2017.
 */

public class FChatActivity extends Activity {
    private static final int LOGIN_CODE = 256;
    private static final String LOG_TAG = "FChatActivity";

    List<Fragment> fragments = new ArrayList<>();
    DebugFragment dbgFragment;
    FChatMobileApplication app;

    FClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.app = (FChatMobileApplication) getApplication();
        this.client = this.app.getClient();

        if(this.client == null){
            if(!app.isSocketConnected())
                showLoginActivity();
            else
                initClient();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbgFragment = new DebugFragment();
        this.fragments.add(dbgFragment);
    }

    protected void showLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent,LOGIN_CODE);
    }

    private void initClient(){
        this.client = new FClient(this, app.getSocket(), new FClient.FClientListener() {
            @Override
            public void onConnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        showFragment(dbgFragment);
                    }
                });
            }

            @Override
            public void onDisconnected() {
                Log.d(LOG_TAG, "Client disconnected");
            }

            @Override
            public void onTextSent(String message) {
                dbgFragment.insertCommand(false,message);
                Log.d(LOG_TAG, ">> " + message);
            }

            @Override
            public void onTextReceived(String message) {
                dbgFragment.insertCommand(true,message);
                Log.d(LOG_TAG, "<< " + message);
            }

            @Override
            public void onChannelUpdated(FChannel channel) {

            }

            @Override
            public void onChannelJoined(FChannel channel) {

            }

            @Override
            public void onChannelLeft(FChannel channel) {

            }
        });
        Log.i(LOG_TAG, "Client initialized");
    }


    protected void loadClient(final LoginTicket ticket){

        if(this.client != null) {
            Log.w(LOG_TAG, "Trying to initialize FClient while it already exists");
        }
        else{
            initClient();
            client.start(ticket);
        }
    }

    public void channelJoined(FChannel channel){
        ChannelFragment f = new ChannelFragment();
        f.setChannel(channel);
        this.fragments.add(f);
    }

    public void showFragment(int i){
        showFragment(this.fragments.get(i));
    }

    public void showFragment(Fragment f){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LOGIN_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                loadClient((LoginTicket)data.getSerializableExtra("ticket"));
            }
        }
    }
}
