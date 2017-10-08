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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.controller.FClient;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FChatEntryAdapter;

/**
 * Created by Florent on 08/10/2017.
 */

public class PrivateMessageFragment extends ChannelFragment {

    private static final String LOG_TAG = "PrivateMessageFragment";

    private FCharacter character;
    private FClient client;

    private EditText messageInput;
    private Button buttonSend;
    private TextView textViewTyping;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_private_messaging, container, false);

        this.messageInput = (EditText) this.root.findViewById(R.id.messageInput);
        this.buttonSend = (Button) this.root.findViewById(R.id.buttonSend);
        this.lvMessages = (ListView) this.root.findViewById(R.id.listViewMessages);
        this.textViewTyping = (TextView) this.root.findViewById(R.id.textViewTyping);

        this.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivateMessageFragment.this.character.sendMessage(messageInput.getText().toString());
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
            Log.i(LOG_TAG, "onActivityCreated : Restoring character " + this.channelName);
        }

        initMessageAdapter();
        loadUser(this.channelName);

        this.type = ChannelType.PRIVATE;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
    }

    private void initMessageAdapter(){
        if(this.messageAdapter == null){
            this.messageAdapter = new FChatEntryAdapter(new ArrayList<FChatEntry>(), getActivity());
            this.lvMessages.setAdapter(messageAdapter);
        }
    }

    private void setTyping(FCharacter.Typing status){
        switch(status){
            case CLEAR:
                this.textViewTyping.setVisibility(View.GONE);
                break;
            case TYPING:
                this.textViewTyping.setVisibility(View.VISIBLE);
                this.textViewTyping.setText(this.character.getName() + " is typing");
                break;
            case PAUSED:
                this.textViewTyping.setVisibility(View.VISIBLE);
                this.textViewTyping.setText(this.character.getName() + " has paused");
                break;
        }
    }

    private void loadUser(String name){
        FChatMobileApplication app = (FChatMobileApplication)getActivity().getApplication();
        this.client = app.getClient();
        this.character = this.client.getCharacter(name);
        this.iconFile = this.character.getAvatarUrl();

        this.character.setListener(new FCharacter.FCharacterListener() {
            @Override
            public void onPrivateMessage(FChatEntry message) {

            }

            @Override
            public void onPrivateMessageListUpdated(List<FChatEntry> messages) {
                PrivateMessageFragment.this.setMessages(messages);
            }

            @Override
            public void onTypingStatusChanged(FCharacter.Typing status){
                setTyping(status);
            }
        });
    }
}
