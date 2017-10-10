package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FTextMessage;
import fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist.FCharacterListAdapter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist.FCharacterListCompactAdapter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist.FCharacterListLargeAdapter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FChatEntryAdapter;

/**
 * Created by Florent on 07/09/2017.
 */

public class PublicChannelFragment extends ChannelFragment {
    private final static String LOG_TAG = "ChannelFragment";
    ListView lvUsers;
    EditText messageInput;
    EditText usernameInput;
    Button buttonSend;

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
        this.lvMessages = (ListView) this.root.findViewById(R.id.listViewMessages);
        this.lvUsers = (ListView) this.root.findViewById(R.id.lvDrawerUsers);
        this.usernameInput = (EditText) this.root.findViewById(R.id.editTextFilter);

        this.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(messageInput.getText().toString());
                messageInput.setText("");
            }
        });

        this.lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FCharacter c = channel.getUsers().get(i);
                openUserProfile(c);
            }
        });

        return this.root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.loadChannel(this.channelName);
        this.type = ChannelType.PUBLIC;
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

        if(this.channel != null){
            channel.sendMessage(message);
        }else{
            Log.e(LOG_TAG, "Trying to send a n unloaded channel");
        }

    }

    public void openUserProfile(FCharacter user){
        channel.getClient().requestProfile(user.getName());
    }

    public void loadChannel(String channel){
        FChatMobileApplication app = (FChatMobileApplication)getActivity().getApplication();
        final FChannel c = app.getClient().getOpenChannel(channel);

        if(c != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {loadChannel(c);
                }
            });

        }else{
            Log.w(LOG_TAG, "Channel " + channel + " was not found in the FClient");
        }
    }

    public void loadChannel(FChannel channel){
        this.channel = channel;

        if(this.messageAdapter == null){
            this.createMessageAdapter();
        }

        if(this.userAdapter == null){
            this.initUserAdapter();
        }

        this.initListView(channel.getEntries());
        this.initUserListView(channel.getUsers());

        this.channel.setListener(new FChannel.FChannelListener() {
            @Override
            public void onEntryListUpdated(List<FChatEntry> entries) {

            }

            @Override
            public void onMessageAdded(FTextMessage message) {

            }

            @Override
            public void onEntryAdded(FChatEntry message) {
                addEntry(message);
            }

            @Override
            public void onUserListUpdated(List<FCharacter> characters){updateUsers(characters);}

        });

        Log.d(LOG_TAG, "Channel " + channel + " has " + this.channel.getUsers().size() + " users and " +  this.channel.getEntries().size() + " messages");
    }

    public void addEntry(final FChatEntry entry){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(messageAdapter == null){updateMessages();}
                else{
                    addMessage(entry);
                }
            }
        });
    }

    public void updateMessages(){
        updateMessages(channel.getEntries());
    }

    public void updateMessages(final List<FChatEntry> entries){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setMessages(entries);
            }
        });
    }

    public void updateUsers() {
        updateUsers(channel.getUsers());
    }

    public void updateUsers(final List<FCharacter> users){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userAdapter.clear();
                userAdapter.addAll(users);
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    public void initUserAdapter(){
        initUserAdapter(true);
    }

    protected void initUserAdapter(boolean compact){
        if(compact){
            userAdapter = new FCharacterListCompactAdapter(new ArrayList<FCharacter>(),  getActivity());
        }else{
            userAdapter = new FCharacterListLargeAdapter(new ArrayList<FCharacter>(),  getActivity());
        }
    }

    protected void initUserListView(final List<FCharacter> users){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lvUsers.setAdapter(userAdapter);
                updateUsers(users);
            }
        });
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
