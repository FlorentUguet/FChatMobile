package fr.fusoft.fchatmobile.socketclient.controller;

import android.content.Context;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.fusoft.fchatmobile.login.model.LoginTicket;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.FServer;
import fr.fusoft.fchatmobile.socketclient.model.ProfileData;
import fr.fusoft.fchatmobile.socketclient.model.commands.ADL;
import fr.fusoft.fchatmobile.socketclient.model.commands.CDS;
import fr.fusoft.fchatmobile.socketclient.model.commands.CHA;
import fr.fusoft.fchatmobile.socketclient.model.commands.ERR;
import fr.fusoft.fchatmobile.socketclient.model.commands.FCommand;
import fr.fusoft.fchatmobile.socketclient.model.commands.FLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.FRL;
import fr.fusoft.fchatmobile.socketclient.model.commands.HLO;
import fr.fusoft.fchatmobile.socketclient.model.commands.ICH;
import fr.fusoft.fchatmobile.socketclient.model.commands.IDN;
import fr.fusoft.fchatmobile.socketclient.model.commands.JCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.KID;
import fr.fusoft.fchatmobile.socketclient.model.commands.KIN;
import fr.fusoft.fchatmobile.socketclient.model.commands.LCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.LIS;
import fr.fusoft.fchatmobile.socketclient.model.commands.MSG;
import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.PRD;
import fr.fusoft.fchatmobile.socketclient.model.commands.PRO;
import fr.fusoft.fchatmobile.socketclient.model.commands.VAR;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FConnectionMessage;
import fr.fusoft.fchatmobile.socketclient.model.messages.FDebugMessage;
import fr.fusoft.fchatmobile.socketclient.model.messages.FTextMessage;

/**
 * Created by Florent on 06/09/2017.
 */

public class FClient {
    public interface FClientListener{
        void onConnected();
        void onDisconnected();
        void onTextSent(String message);
        void onTextReceived(String message);
        void onChannelUpdated(String channel);
        void onChannelJoined(String channel);
        void onChannelLeft(String channel);
        void onChannelListReceived(List<FChannel> channels);

        void onMessageReceived(String channel, FTextMessage message);
        void onMessageSent(String channel, FTextMessage message);

        void onShowProfile(String character);
    }

    private FClientListener mListener;
    private FSocketManager socket;
    private FCommandParser parser;
    private FServer server;
    private WebSocket webSocket;

    private Context context;

    private LoginTicket ticket;

    private Map<String, FCharacter> characters = new HashMap<>();
    private Map<String, FChannel> joinedChannels = new HashMap<>();

    private List<FChatEntry> debugMessages = new ArrayList<>();

    private String mainUser;

    private final String LOG_TAG = "FClient";

    public FClient(Context context, WebSocket socket, FClientListener listener){
        this.context = context;
        this.mListener = listener;

        setupParser();
        setupSocket(socket);
    }

    public void setListener(FClientListener listener){
        this.mListener = listener;
    }

    public void start(LoginTicket ticket){
        this.ticket = ticket;
        this.mainUser = ticket.defaultCharacter;
        this.socket.connect();
    }

    public void sendMessage(String message, String channel){
        MSG command = new MSG(channel, message);
        this.socket.sendCommand(command);

        FTextMessage m = new FTextMessage(getCharacter(mainUser), command);
        getOpenChannel(command.getChannel()).addMessage(m);

        if(this.mListener != null)
            this.mListener.onMessageSent(channel, m);
    }

    public void requestKinks(String character){
        socket.sendCommand(new KIN(character));
    }

    public void requestProfile(String character){
        socket.sendCommand(new PRO(character));
    }

    public void joinChannel(String channel){
        JCH c = new JCH(channel);
        this.socket.sendCommand(c);
    }

    public void leaveChannel(String channel){
        LCH c = new LCH(channel);
        this.socket.sendCommand(c);
    }

    private void channelJoined(JCH command){
        FChannel channel;

        if(command.getCharacter().equals(mainUser)){
            //If it's the main user
            channel = new FChannel(command.getChannel(), "", 0);
            channel.setClient(this);
            joinedChannels.put(command.getChannel(), channel);

            if (this.mListener != null)
                this.mListener.onChannelJoined(command.getChannel());
        }else{
            //If it's another user
            channel = getOpenChannel(command.getChannel());
        }

        channel.userJoined(getCharacter(command.getCharacter()));
    }

    private void channelLeft(LCH command){
        String c = command.getChannel();

        if(command.getCharacter().equals(mainUser)){
            //If it's the main user
            this.joinedChannels.remove(c);

            if (this.mListener != null)
                this.mListener.onChannelLeft(c);
        }else{
            //If it's another user
            FChannel channel = getOpenChannel(c);
            channel.userLeft(command.getCharacter());

            if (this.mListener != null)
                this.mListener.onChannelUpdated(command.getChannel());
        }
    }

    private void channelData(ICH command){
        FChannel c = getOpenChannel(command.getChannel());

        List<String> users = command.getUsers();
        Map<String, FCharacter> cUsers = new HashMap<>();

        for(String user : users){
            cUsers.put(user,this.getCharacter(user));
        }

        c.setUsers(cUsers);

        if (this.mListener != null)
            this.mListener.onChannelUpdated(command.getChannel());
    }

    private void channelDescription(CDS command){
        FChannel c = getOpenChannel(command.getChannel());
        c.setDescription(command.getDescription());

    }

    private void messageReceived(MSG command){
        FCharacter sender = getCharacter(command.getCharacter());
        FTextMessage m = new FTextMessage(sender, command);

        FChannel c = getOpenChannel(command.getChannel());

        if(c != null){
            c.addMessage(m);
            if(this.mListener != null)
                this.mListener.onMessageReceived(command.getChannel(), m);
        }else{
            Log.w(LOG_TAG, "Received message for channel " + command.getChannel() + " but channel is not opened");
        }
    }

    private void channelListReceived(CHA command){
        List<FChannel> channels = FChannel.fromCHA(command);
        Collections.sort(channels);
        this.mListener.onChannelListReceived(channels);
    }

    private void characterOnline(NLN command){
        FCharacter c = new FCharacter(command);

        if(!this.characters.containsKey(c.getName()))
            this.characters.put(c.getName(), c);
    }

    private void characterOffline(FLN command){
        this.characters.remove(command.getCharacter());
    }

    private void characterListReceived(LIS command){
        for(FCharacter c : command.getCharacters()){
            this.characters.put(c.getName(), c);
        }

        Log.d(LOG_TAG, this.characters.size() + " users online");
    }

    private void profileDataReceived(PRD command){
        ProfileData d = command.getProfile();
        FCharacter c = getCharacter(d.getCharacter());

        switch(d.getType()){
            case INFO:
                c.addProfileData(d);
                break;
            case START:
                c.clearProfileData();
                break;
            case END:
                if(this.mListener != null)
                    this.mListener.onShowProfile(d.getCharacter());
                break;
        }
    }

    public FCharacter getMainUser(){
        return this.getCharacter(mainUser);
    }

    public FChannel getOpenChannel(String name){
        return this.joinedChannels.get(name);
    }

    public FCharacter getCharacter(String name){
        return this.characters.get(name);
    }

    private void setupParser(){
        this.parser = new FCommandParser(new FCommandParser.FCommandParserListener() {
            @Override
            public void onPing() {
                socket.sendPing();
            }

            @Override
            public void onChannelLeft(LCH command){
                channelLeft(command);
            }

            @Override
            public void onChannelJoined(JCH command){
                channelJoined(command);
            }

            @Override
            public void onIdentification(IDN command) {

            }

            @Override
            public void onChannelDescription(CDS command){channelDescription(command); }

            @Override
            public void onHello(HLO command) {

            }

            @Override
            public void onFriendList(FRL command) {

            }

            @Override
            public void onChannelData(ICH command){
                channelData(command);
            }

            @Override
            public void onChannelListReceived(CHA command) {channelListReceived(command); }

            @Override
            public void onServerVariable(VAR command) {

            }

            @Override
            public void onAdminList(ADL command) {

            }

            @Override
            public void onError(ERR command) {

            }

            @Override
            public void onUnhandledCommand(FCommand command) {

            }

            @Override
            public void onCharacterOnline(NLN command) {
                characterOnline(command);
            }

            @Override
            public void onCharacterOffline(FLN command) {
                characterOffline(command);
            }

            @Override
            public void onMessage(MSG command) {
                messageReceived(command);
            }

            @Override
            public void onKinkData(KID command){

            }

            @Override
            public void onCharacterList(LIS command){
                characterListReceived(command);
            }

            @Override
            public void onProfileData(PRD command){
                profileDataReceived(command);
            }
        });

    }

    private void setupSocket(WebSocket webSocket){
        this.webSocket = webSocket;
        this.socket = new FSocketManager(this.context, this.webSocket, this.parser, new FSocketManager.FSocketListener() {
            @Override
            public void onConnected() {
                if(mListener != null) mListener.onConnected();
                socket.identify(ticket);
            }

            public void onDisconnected() {
                if(mListener != null) mListener.onDisconnected();
            }

            @Override
            public void onTextReceived(String message) {
                if(mListener != null) mListener.onTextReceived(message);
                debugMessages.add(new FDebugMessage(true, message));
            }

            @Override
            public void onTextSent(String message) {
                if(mListener != null) mListener.onTextSent(message);
                debugMessages.add(new FDebugMessage(false, message));
            }
        });
    }

    public List<FChatEntry> getDebugMessages(){
        return this.debugMessages;
    }

    public void requestChannelList(){
        this.socket.sendCommand(new FCommand("CHA"));
    }
}
