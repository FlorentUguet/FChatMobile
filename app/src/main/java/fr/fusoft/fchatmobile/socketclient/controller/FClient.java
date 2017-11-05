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
import fr.fusoft.fchatmobile.socketclient.model.commands.LRP;
import fr.fusoft.fchatmobile.socketclient.model.commands.MSG;
import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.PRD;
import fr.fusoft.fchatmobile.socketclient.model.commands.PRI;
import fr.fusoft.fchatmobile.socketclient.model.commands.PRO;
import fr.fusoft.fchatmobile.socketclient.model.commands.STA;
import fr.fusoft.fchatmobile.socketclient.model.commands.TPN;
import fr.fusoft.fchatmobile.socketclient.model.commands.VAR;
import fr.fusoft.fchatmobile.socketclient.model.messages.FAdEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FDebugMessage;
import fr.fusoft.fchatmobile.socketclient.model.messages.FTextMessage;

/**
 * Created by Florent on 06/09/2017.
 */

public class FClient {
    public interface FClientListener{
        void onConnected();
        void onDisconnected();
        void onChannelJoined(String channel);
        void onChannelLeft(String channel);
        void onChannelListReceived(List<FChannel> channels);
        void onShowProfile(String character);
        void onPrivateMessageReceived(FCharacter character);
    }

    public interface FClientDebugListener{
        void onTextSent(String message);
        void onTextReceived(String message);
    }

    private FClientListener mListener;
    private FClientDebugListener mDebugListener;

    private FSocketManager socket;
    private FTokenHandler parser;
    private FListApi api;

    private FServer server;
    private WebSocket webSocket;

    private Context context;

    private LoginTicket ticket;

    private Map<String, FCharacter> characters = new HashMap<>();
    private Map<String, FChannel> joinedChannels = new HashMap<>();

    private List<FChatEntry> debugMessages = new ArrayList<>();

    private List<String> friends = new ArrayList<>();
    private List<String> bookmarks = new ArrayList<>();

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

    public void setDebugListener(FClientDebugListener listener){ this.mDebugListener = listener; }

    public void start(LoginTicket ticket){
        this.ticket = ticket;
        this.mainUser = ticket.defaultCharacter;
        this.socket.connect();

        setFriends(ticket.getFriends(ticket.defaultCharacter));
        setBookmarks(ticket.getBookmarks());

        this.api = new FListApi(ticket);
    }

    public void setBookmarks(List<String> bookmarks){
        this.bookmarks = bookmarks;
    }

    public void setFriends(List<String> friends){
        this.friends = friends;
    }

    public void bookmark(String name){
        this.api.bookmark(name);
    }

    public void sendAd(String message, String channel) {
        LRP command = new LRP(channel, message);
        this.socket.sendCommand(command);

        FAdEntry m = new FAdEntry(getCharacter(mainUser), command.getMessage());
        getOpenChannel(command.getChannel()).addAd(m);
    }

    public void sendMessage(String message, String channel){

        if(message.startsWith("/")){
            FCommand c = FCommandParser.parse(message, channel);

            if(c == null){

            }else{
                this.socket.sendCommand(c);

                switch(c.getToken()){
                    case "MSG":
                        getOpenChannel(channel).addMSG((MSG)c);
                        break;
                    case "LRP":
                        getOpenChannel(channel).addLRP((LRP)c);
                        break;
                }
            }

        }else{
            MSG command = new MSG(channel, message);
            this.socket.sendCommand(command);
            getOpenChannel(channel).addMSG(command);
        }


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

        channel.userJoined(command.getCharacter());
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
        }
    }

    private void channelData(ICH command){
        FChannel c = getOpenChannel(command.getChannel());
        List<String> users = command.getUsers();
        c.setUsers(users);
        Log.i(LOG_TAG, "Channel " + c.getName() + " has " + c.getUsers().size() + " users");
    }

    private void channelDescription(CDS command){
        FChannel c = getOpenChannel(command.getChannel());
        c.setDescription(command.getDescription());

    }

    private void messageReceived(MSG command){
        FCharacter sender = getCharacter(command.getCharacter());
        FTextMessage m = new FTextMessage(sender, command.getMessage());

        FChannel c = getOpenChannel(command.getChannel());

        if(c != null){
            c.addMessage(m);
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
        c.setClient(this);

        if(!this.characters.containsKey(c.getName()))
            this.characters.put(c.getName(), c);
    }

    private void characterOffline(FLN command){
        this.characters.remove(command.getCharacter());

        for(FChannel c : this.joinedChannels.values()){
            c.userLeft(command.getCharacter());
        }
    }

    private void characterListReceived(LIS command){
        for(FCharacter c : command.getCharacters()){
            c.setClient(this);
            this.characters.put(c.getName(), c);
        }

        //Log.d(LOG_TAG, this.characters.size() + " users online");
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

    private void characterStatusUpdated(STA command){
        FCharacter c = getCharacter(command.getCharacter());
        c.setStatus(command.getStatus(), command.getStatusMsg());

        for(FChannel channel : this.joinedChannels.values()){
            if(channel.hasCharacter(command.getCharacter()))
                channel.userListUpdated();
        }
    }

    private void typingStatusChanged(TPN command){
        FCharacter c = getCharacter(command.getCharacter());
        c.setTypingStatus(command.getStatus());
    }

    public void setStatus(String status, String message){
        this.socket.sendCommand(new STA(status, message));
    }

    public void sendPrivateMessage(String recipient, String message){
        this.socket.sendCommand(new PRI(recipient, message));

        if(this.mListener != null)
            this.mListener.onPrivateMessageReceived(getCharacter(recipient));
    }

    public void sendTypingStatus(FCharacter.Typing status){
        this.socket.sendCommand(new TPN(this.mainUser, status));
    }

    private void privateMessageReceived(PRI command){
        FCharacter c = getCharacter(command.getCharacter());
        c.messageReceived(new FTextMessage(c, command.getMessage()));

        if(this.mListener != null)
            this.mListener.onPrivateMessageReceived(c);
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

    public List<FCharacter> getCharacters(){return new ArrayList<>(this.characters.values());}

    public List<FCharacter> getOnlineFriends(){
        List<FCharacter> onlineFriends = new ArrayList<>();

        for(String s : this.friends){
            if(this.characters.containsKey(s)){
                onlineFriends.add(this.characters.get(s));
            }
        }

        Collections.sort(onlineFriends);

        return onlineFriends;
    }

    public List<FCharacter> getOnlineBookmarks(){
        List<FCharacter> onlineBookmarks = new ArrayList<>();

        for(String s : this.bookmarks){
            if(this.characters.containsKey(s)){
                onlineBookmarks.add(this.characters.get(s));
            }
        }

        Collections.sort(onlineBookmarks);

        return onlineBookmarks;
    }

    private void setupParser(){
        this.parser = new FTokenHandler(new FTokenHandler.FTokenHandlerListener() {
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

            @Override
            public void onCharacterStatus(STA command){ characterStatusUpdated(command); }

            @Override
            public void onPrivateMessage(PRI command){ privateMessageReceived(command);}

            @Override
            public void onTyping(TPN command) { typingStatusChanged(command);}
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
                if(mDebugListener != null) mDebugListener.onTextReceived(message);
                debugMessages.add(new FDebugMessage(true, message));
            }

            @Override
            public void onTextSent(String message) {
                if(mDebugListener != null) mDebugListener.onTextSent(message);
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
