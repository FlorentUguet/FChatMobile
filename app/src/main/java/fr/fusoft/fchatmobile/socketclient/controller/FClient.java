package fr.fusoft.fchatmobile.socketclient.controller;

import android.content.Context;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.login.model.LoginTicket;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.FServer;
import fr.fusoft.fchatmobile.socketclient.model.commands.ADL;
import fr.fusoft.fchatmobile.socketclient.model.commands.CHA;
import fr.fusoft.fchatmobile.socketclient.model.commands.ERR;
import fr.fusoft.fchatmobile.socketclient.model.commands.FCommand;
import fr.fusoft.fchatmobile.socketclient.model.commands.FLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.FRL;
import fr.fusoft.fchatmobile.socketclient.model.commands.HLO;
import fr.fusoft.fchatmobile.socketclient.model.commands.IDN;
import fr.fusoft.fchatmobile.socketclient.model.commands.JCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.LCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.MSG;
import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.VAR;
import fr.fusoft.fchatmobile.socketclient.model.messages.FConnectionMessage;
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
        void onChannelUpdated(FChannel channel);
        void onChannelJoined(FChannel channel);
        void onChannelLeft(FChannel channel);
    }

    FClientListener mListener;
    FSocketManager socket;
    FCommandParser parser;

    Context context;

    FServer server;

    LoginTicket ticket;

    List<FChannel> joinedChannels = new ArrayList<>();
    List<String> publicChannels = new ArrayList<>();
    List<FCharacter> characters = new ArrayList<>();

    String mainUser;

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
        this.socket.connect();
    }

    public void joinChannel(String channel){
        JCH c = new JCH(channel);
        this.socket.sendCommand(c);
    }

    private void channelJoined(JCH command){
        String channel = command.getChannel();

        if(command.getCharacter().equals(mainUser)){
            //If it's the main user
            FChannel fc = new FChannel(command.getData());
            this.joinedChannels.add(fc);
            this.mListener.onChannelJoined(fc);
        }else{
            //If it's another user
            FChannel c = getOpenChannel(command.getChannel());
            c.addEntry(new FConnectionMessage(command));
        }
    }

    private void channelLeft(LCH command){
        String channel = command.getChannel();

        if(command.getCharacter().equals(mainUser)){
            //If it's the main user
            FChannel fc = new FChannel(command.getData());
            this.joinedChannels.remove(fc);
            this.mListener.onChannelLeft(fc);
        }else{
            //If it's another user
            FChannel c = getOpenChannel(command.getChannel());
            c.addEntry(new FConnectionMessage(command));
        }
    }

    private void messageReceived(MSG command){
        FCharacter sender = getCharacter(command.getCharacter());
        FTextMessage m = new FTextMessage(sender, command);
        getOpenChannel(command.getChannel()).addEntry(m);
    }

    public FChannel getOpenChannel(String name){
        for(FChannel c : joinedChannels){
            if(c.getName().equals(name))
                return c;
        }

        return null;
    }

    public FCharacter getCharacter(String name){
        for(FCharacter c : characters){
            if(c.getName().equals(name))
                return c;
        }

        return null;
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
            public void onHello(HLO command) {

            }

            @Override
            public void onFriendList(FRL command) {

            }

            @Override
            public void onChannelListReceived(CHA command) {

            }

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

            }

            @Override
            public void onCharacterOffline(FLN command) {

            }

            @Override
            public void onMessage(MSG command) {
                messageReceived(command);
            }
        });

    }

    private void setupSocket(WebSocket webSocket){
        this.socket = new FSocketManager(this.context, webSocket, this.parser, new FSocketManager.FSocketListener() {
            @Override
            public void onConnected() {
                mListener.onConnected();
                socket.identify(ticket);
            }

            public void onDisconnected() {
                mListener.onDisconnected();
            }

            @Override
            public void onTextReceived(String message) {
                mListener.onTextReceived(message);
            }

            @Override
            public void onTextSent(String message) {
                mListener.onTextSent(message);
            }
        });
    }
}
