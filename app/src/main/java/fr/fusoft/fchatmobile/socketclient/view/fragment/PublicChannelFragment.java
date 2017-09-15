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
    ListView lvUsers;
    EditText messageInput;
    Button buttonSend;

    FCharacterListAdapter userAdapter;

    FChannel channel;
    String channelName = "";

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
        this.lvMessages = (ListView) this.root.findViewById(R.id.listViewMessages);
        this.lvUsers = (ListView) this.root.findViewById(R.id.drawerUsers);

        this.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(messageInput.getText().toString());
                messageInput.setText("");
            }
        });

        return this.root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            this.channelName = savedInstanceState.getString("channel");
        }

        if(!this.channelName.equals(""))
            this.loadChannel(this.channelName);
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
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void sendMessage(String message){
        FChatMobileApplication app = (FChatMobileApplication)getActivity().getApplication();
        app.getClient().sendMessage(message, this.channelName);
    }

    public void loadChannel(String channel){
        FChatMobileApplication app = (FChatMobileApplication)getActivity().getApplication();
        FChannel c = app.getClient().getOpenChannel(channel);

        if(c != null) {
            loadChannel(c);
        }else{
            Log.w(LOG_TAG, "Channel " + channel + " was not found in the FClient");
        }
    }

    public void loadChannel(FChannel channel){
        this.channel = channel;
        this.messageAdapter = new FChatEntryAdapter(new ArrayList<FChatEntry>(),getActivity());
        this.lvMessages.setAdapter(messageAdapter);

        this.messageAdapter.clear();
        this.messageAdapter.addAll(this.channel.getEntries());
        this.messageAdapter.notifyDataSetChanged();

        this.userAdapter = new FCharacterListAdapter(new ArrayList<FCharacter>(), getActivity());
        this.lvUsers.setAdapter(messageAdapter);

        this.userAdapter.clear();
        this.userAdapter.addAll(this.channel.getUsers());
        this.userAdapter.notifyDataSetChanged();

        Log.d(LOG_TAG, "Channel " + channel + " was set in fragment " + this.getTag());
    }

    public String getIcon(){
        return "";
    }

    public String getChannelName(){
        if(this.channel != null)
            return this.channel.getName();
        else
            return this.channelName;
    }
}
