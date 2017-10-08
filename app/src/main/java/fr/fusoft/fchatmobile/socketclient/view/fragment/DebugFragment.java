package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.controller.FClient;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FDebugMessage;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FChatEntryAdapter;

/**
 * Created by Florent on 06/09/2017.
 */

public class DebugFragment extends ChannelFragment {
    private static final String LOG_TAG = "DebugFragment";

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        this.iconFile = "";
        this.channelName = "Debug";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_debug_channel, container, false);
        this.lvMessages = (ListView) this.root.findViewById(R.id.listViewMessages);

        return this.root;
    }

    @Override
    public void onStart(){
        super.onStart();
        load();
    }

    public void load(){
        Log.d(LOG_TAG, "Loading Client");

        if(this.messageAdapter == null){
            Log.d(LOG_TAG, "Initializing Message Adapter");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DebugFragment.this.messageAdapter = new FChatEntryAdapter(new ArrayList<FChatEntry>(),getActivity());
                    DebugFragment.this.lvMessages.setAdapter(DebugFragment.this.messageAdapter);
                }
            });
        }

        FChatMobileApplication app = (FChatMobileApplication)getActivity().getApplication();
        final FClient client = app.getClient();

        if(client != null){
            Log.d(LOG_TAG, "Setting up client");

            client.setDebugListener(new FClient.FClientDebugListener() {
                @Override
                public void onTextSent(String message) {
                    insertCommand(false, ">> " + message);
                    Log.d(LOG_TAG, ">> " + message);
                }

                @Override
                public void onTextReceived(String message) {
                    insertCommand(true, "<< " + message);
                    Log.d(LOG_TAG, "<< " + message);
                }
            });

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setMessages(client.getDebugMessages());
                }
            });
        }else{
            Log.e(LOG_TAG, "Client could not be loaded");
        }
    }

    public void insertCommand(boolean received, String message){
        final FDebugMessage m = new FDebugMessage(received, message);
        addMessage(m);
    }
}
