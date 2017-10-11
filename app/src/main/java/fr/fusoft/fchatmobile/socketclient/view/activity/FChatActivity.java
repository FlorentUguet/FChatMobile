package fr.fusoft.fchatmobile.socketclient.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.login.model.LoginTicket;
import fr.fusoft.fchatmobile.login.view.LoginActivity;
import fr.fusoft.fchatmobile.socketclient.controller.FClient;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FChannelFragmentAdapter;
import fr.fusoft.fchatmobile.socketclient.view.fragment.channels.ChannelFragment;
import fr.fusoft.fchatmobile.socketclient.view.fragment.channels.DebugFragment;
import fr.fusoft.fchatmobile.socketclient.view.fragment.dialogs.FriendsListDialogFragment;
import fr.fusoft.fchatmobile.socketclient.view.fragment.channels.PrivateMessageFragment;
import fr.fusoft.fchatmobile.socketclient.view.fragment.channels.PublicChannelFragment;
import fr.fusoft.fchatmobile.socketclient.view.fragment.dialogs.UserDialogFragment;
import fr.fusoft.fchatmobile.utils.Preferences;

/**
 * Created by Florent on 05/09/2017.
 */

public class FChatActivity extends AppCompatActivity {
    private static final int LOGIN_CODE = 256;
    private static final String LOG_TAG = "FChatActivity";

    List<ChannelFragment> fragments = new ArrayList<>();
    DebugFragment dbgFragment;
    FChatMobileApplication app;

    private int currentFragment = -1;

    FChannelFragmentAdapter adapter;

    FClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbgFragment = new DebugFragment();
        addFragment(dbgFragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_channels);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(newState == DrawerLayout.STATE_DRAGGING)
                    updateChannelDrawer();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_open_channel_list:
                openChannelList();
                return true;
            case R.id.action_test:
                testFunction();
                return true;
            case R.id.action_friends:
                showFriendsList();
                return true;
            case R.id.action_status:
                showStatusDialog();
                return true;
            case R.id.action_chanel_info:
                showChannelInfoDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void testFunction(){
        this.client.requestProfile(this.client.getMainUser().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.app = (FChatMobileApplication) getApplication();
        this.app.setCurrentActivity(this);
        this.app.setListener(new FChatMobileApplication.ServiceConnectedListener() {
            @Override
            public void onServiceConnected() {
                client = app.getClient();
                initClientListener();
                showLoginActivity();
            }

            @Override
            public void onServiceDisconnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FChatActivity.this, "Service unbound", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if(app.isServiceBound()){
            this.client = this.app.getClient();
            initClientListener();
        }

        updateChannelDrawer();
    }

    protected void openChannelList(){
        this.client.requestChannelList();
    }

    protected void showChannelList(final List<FChannel> channels){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] labels = new String[channels.size()];
        boolean[] joined = new boolean[channels.size()];

        for(int i=0;i<channels.size();i++){
            labels[i] = channels.get(i).getName();
            joined[i] = client.getOpenChannel(labels[i]) != null;
        }

        builder.setTitle(R.string.dialog_select_channels)
                .setMultiChoiceItems(labels,joined,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    final FChannel selected = channels.get(which);
                                    joinChannel(selected);
                                } else {
                                    final FChannel unselected = channels.get(which);
                                    leaveChannel(unselected);
                                }
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });
    }

    protected void showFriendsList(){
        FriendsListDialogFragment f = FriendsListDialogFragment.newInstance();

        f.setListener(new FriendsListDialogFragment.FriendsListListener() {
            @Override
            public void onCharacterSelected(FCharacter character) {
                openPrivateMessaging(character);
            }
        });

        f.show(getSupportFragmentManager(), "dialog");
    }

    protected void showLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent,LOGIN_CODE);
    }

    private void initClientListener()    {
        this.client.setListener(new FClient.FClientListener() {
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
            public void onChannelJoined(String channel) {
                channelJoined(channel);
            }

            @Override
            public void onChannelListReceived(List<FChannel> channels) {
                showChannelList(channels);
            }

            @Override
            public void onChannelLeft(String channel) {
                channelLeft(channel);
            }

            @Override
            public void onShowProfile(String character){ showProfile(character); }

            @Override
            public void onPrivateMessageReceived(FCharacter character){ privateMessageReceived(character); }
        });
    }

    protected void loadClient(final LoginTicket ticket){
        this.client = this.app.getClient();

        if(app.isSocketConnected()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FChatActivity.this, "Client already connected", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            initClientListener();
            client.start(ticket);
        }
    }

    private void showProfile(String character){
        UserDialogFragment f = UserDialogFragment.newInstance(character);
        f.show(getSupportFragmentManager(), "dialog");
    }

    protected void showChannelInfoDialog(){
        Fragment f = getFragmentManager().findFragmentById(R.id.content);

        if(f != null){
            if(ChannelFragment.class.isInstance(f)){
                ChannelFragment fragment = (ChannelFragment)f;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(fragment.getChannelName())
                        .setMessage(Html.fromHtml(fragment.getChannelInfo()))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }else{
                Log.e(LOG_TAG, "Fragment retreived for info is not a ChannelFragment (" + f.getClass().getName()+ ")");
            }
        }else{
            Log.e(LOG_TAG, "Fragment retreived for info is null");
        }
    }

    private void showStatusDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View root = View.inflate(this, R.layout.dialog_status, null);

        //Dialog's widgets
        final EditText input = (EditText) root.findViewById(R.id.editTextStatus);
        final Spinner spinner = (Spinner) root.findViewById(R.id.spinnerStatus);
        final CheckBox checkBox = (CheckBox) root.findViewById(R.id.checkBoxStatus);
        final Preferences p = new Preferences(FChatActivity.this);

        //Action on spinner's item changed
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                input.setText(p.getDefaultStatusMessage(FCharacter.Status.getIdentifiers().get(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(FCharacter.Status.getLabels());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Generating the dialog
        builder.setView(root)
                .setMessage("Update your status")
                .setTitle("Status")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setStatus(FCharacter.Status.getIdentifiers().get(spinner.getSelectedItemPosition()),input.getText().toString(), checkBox.isChecked());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog d = builder.create();
        d.show();
    }

    private void setStatus(String status, String message, boolean save){
        this.client.getMainUser().requestStatus(status, message);

        if(save){
            Preferences p = new Preferences(this);
            p.setDefaultStatusMessage(status, message);
        }
    }

    private void privateMessageReceived(FCharacter character){
        openPrivateMessaging(character);
    }

    private void openPrivateMessaging(FCharacter character){
        if(!isChannelOpen(character.getName())){
            PrivateMessageFragment f = new PrivateMessageFragment();
            f.setChannelName(character.getName());
            f.setIconFile(character.getAvatarUrl());
            addFragment(f);
        }
    }

    private boolean isChannelOpen(String name){
        for(ChannelFragment c : this.fragments){
            if(c.getChannelName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private void joinChannel(FChannel c){
        client.joinChannel(c.getName());
    }

    private void leaveChannel(FChannel c){
        client.leaveChannel(c.getName());
    }

    public void channelJoined(final String channel){
        Log.i(LOG_TAG, "Joined channel " + channel);
        PublicChannelFragment f = new PublicChannelFragment();
        f.setChannelName(channel);
        addFragment(f);
    }

    public void channelLeft(String channel){
        removeFragment(channel);
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

    private ChannelFragment getFragment(String label){
        for(ChannelFragment f : fragments){
            if(f.getChannelName().equals(label)){
                return f;
            }
        }

        return null;
    }

    private void removeFragment(String label){
        ChannelFragment f = getFragment(label);
        if(f != null){
            fragments.remove(f);
            updateChannelDrawer();
        }
    }

    private void removeFragment(int index){
        this.fragments.remove(index);
        updateChannelDrawer();
    }

    private void addFragment(ChannelFragment f){
        this.fragments.add(f);
        updateChannelDrawer();
    }

    public void updateChannelDrawer(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(adapter == null){
                    adapter = new FChannelFragmentAdapter(new ArrayList<ChannelFragment>(), FChatActivity.this);
                    ListView lv = (ListView) findViewById(R.id.lvDrawerChannels);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            showFragment(fragments.get(i));
                        }
                    });
                }

                adapter.clear();
                adapter.addAll(FChatActivity.this.fragments);
                adapter.notifyDataSetChanged();
            }
        });

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
