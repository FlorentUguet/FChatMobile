package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FChatEntryAdapter;

/**
 * Created by Florent on 05/09/2017.
 */

public class ChannelFragment extends Fragment{
    private final static String LOG_TAG = "ChannelFragment";
    View root;
    ListView lv;
    EditText messageInput;
    Button buttonSend;
    FChatEntryAdapter adapter;

    FChannel channel;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        this.adapter = new FChatEntryAdapter(new ArrayList<FChatEntry>(),getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_channel, container, false);
        this.messageInput = (EditText) this.root.findViewById(R.id.messageInput);
        this.buttonSend = (Button) this.root.findViewById(R.id.buttonSend);

        this.lv = (ListView) this.root.findViewById(R.id.listViewMessages);
        this.lv.setAdapter(this.adapter);
        Log.d(LOG_TAG, "onCreateView");
        return this.root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    public void setChannel(FChannel channel){
        this.channel = channel;
    }

    public void updateList(){
        this.adapter.clear();
        this.adapter.addAll(this.channel.getEntries());
        this.adapter.notifyDataSetChanged();
    }

}
