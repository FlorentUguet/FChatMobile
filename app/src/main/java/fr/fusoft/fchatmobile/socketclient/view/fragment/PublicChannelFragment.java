package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FCharacterListAdapter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FChatEntryAdapter;

/**
 * Created by Florent on 07/09/2017.
 */

public class PublicChannelFragment extends ChannelFragment {
    private final static String LOG_TAG = "ChannelFragment";
    View root;
    ListView lv;
    ListView lvUsers;
    EditText messageInput;
    Button buttonSend;

    FChatEntryAdapter messageAdapter;
    FCharacterListAdapter userAdapter;

    FChannel channel;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_channel, container, false);
        this.messageInput = (EditText) this.root.findViewById(R.id.messageInput);
        this.buttonSend = (Button) this.root.findViewById(R.id.buttonSend);
        this.lv = (ListView) this.root.findViewById(R.id.listViewMessages);
        this.lvUsers = (ListView) this.root.findViewById(R.id.drawerUsers);

        Log.d(LOG_TAG, "onCreateView");
        return this.root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            String channel = savedInstanceState.getString("channel");

            if(channel != null)
                this.setChannel(channel);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here

        if(this.channel != null)
            outState.putString("channel", this.channel.getName());

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

    public void setChannel(String channel){
        FChatMobileApplication app = (FChatMobileApplication)getActivity().getApplication();
        FChannel c = app.getClient().getOpenChannel(channel);

        if(c != null) {
            setChannel(c);
        }else{
            Log.w(LOG_TAG, "Channel " + channel + " was not found in the FClient");
        }
    }

    public void setChannel(FChannel channel){
        this.channel = channel;
        this.messageAdapter = new FChatEntryAdapter(new ArrayList<FChatEntry>(),getActivity());
        this.lv.setAdapter(messageAdapter);

        this.messageAdapter.clear();
        this.messageAdapter.addAll(this.channel.getEntries());
        this.messageAdapter.notifyDataSetChanged();

        this.userAdapter = new FCharacterListAdapter(new ArrayList<FCharacter>(), getActivity());
        this.lvUsers.setAdapter(messageAdapter);

        this.userAdapter.clear();
        this.userAdapter.addAll(this.channel.getUsers());
        this.userAdapter.notifyDataSetChanged();
    }

    public String getIcon(){
        return "";
    }

    public String getChannelName(){
        if(this.channel != null)
            return this.channel.getName();
        else
            return "";
    }
}
