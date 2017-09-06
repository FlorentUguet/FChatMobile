package fr.fusoft.fchatmobile.socketclient.view.fragment;

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

import fr.fusoft.fchatmobile.R;
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
        this.adapter = new FChatEntryAdapter(new ArrayList<FChatEntry>(),getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_debug_channel, container, false);

        this.lv = (ListView) this.root.findViewById(R.id.listViewMessages);
        this.lv.setAdapter(this.adapter);

        return this.root;
    }

    public void insertCommand(boolean received, String message){
        FDebugMessage m = new FDebugMessage(received, message);

        Log.e(LOG_TAG, "Inserting Command");

        if(this.adapter != null){
            Log.e(LOG_TAG, "Inserting Command in Adapter");
            this.adapter.add(m);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }
}
