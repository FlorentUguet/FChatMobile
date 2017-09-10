package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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
    View root;
    ListView lv;
    FChatEntryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_debug_channel, container, false);
        this.lv = (ListView) this.root.findViewById(R.id.listViewMessages);

        return this.root;
    }

    @Override
    public void onStart(){
        super.onStart();
        load();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void load(){
        Log.d(LOG_TAG, "Loading Client");
        if(this.adapter == null){
            this.adapter = new FChatEntryAdapter(new ArrayList<FChatEntry>(),getActivity());
            this.lv.setAdapter(this.adapter);
        }


        Activity act = getActivity();
        FChatMobileApplication app = (FChatMobileApplication)act.getApplication();
        FClient client = app.getClient();

        if(client != null){
            this.adapter.clear();
            this.adapter.addAll(client.getDebugMessages());
            this.adapter.notifyDataSetChanged();
        }

    }

    public void insertCommand(boolean received, String message){
        final FDebugMessage m = new FDebugMessage(received, message);

        if(this.adapter != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(m);
                }
            });
        }

    }

    public String getIcon(){
        return "";
    }

    public String getChannelName(){
        return "Debug";
    }
}
