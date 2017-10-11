package fr.fusoft.fchatmobile.socketclient.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.fusoft.fchatmobile.socketclient.controller.FClient;
import fr.fusoft.fchatmobile.socketclient.model.commands.CHA;
import fr.fusoft.fchatmobile.socketclient.model.commands.ICH;
import fr.fusoft.fchatmobile.socketclient.model.messages.FAdEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FTextMessage;

/**
 * Created by Florent on 06/09/2017.
 */

public class FChannel implements Comparable {
    private static final String LOG_TAG = "FChannel";

    private String name;
    private String mode;
    private int count;
    private String description;

    List<FChatEntry> entries = new ArrayList<>();
    List<String> characters = new ArrayList<>();

    FClient client;

    public interface FChannelListener{
        void onEntryListUpdated(List<FChatEntry> entries);
        void onMessageAdded(FTextMessage message);
        void onEntryAdded(FChatEntry message);
        void onUserListUpdated(List<FCharacter> users);
    }

    private FChannelListener mListener;

    public FChannel(JSONObject obj){
        try{
            this.name = obj.getString("name");
            this.mode = obj.getString("mode");
            this.count = obj.getInt("characters");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading " + e.getMessage());
        }
    }

    public FChannel(String name, String mode, int count){
        this.name = name;
        this.mode = mode;
        this.count = count;
    }

    public void setListener(FChannelListener listener){
        this.mListener = listener;
    }

    public void setClient(FClient c){
        this.client = c;
    }

    public FClient getClient(){return this.client;}

    public void sendAd(String message){
        this.client.sendAd(message, this.getName());
    }

    public void sendMessage(String message){
        this.client.sendMessage(message, this.getName());
    }

    public static List<FChannel> fromCHA(CHA command){
        List<FChannel> channels = new ArrayList<>();

        try{
            JSONArray a = command.getData().getJSONArray("channels");

            for(int i=0;i<a.length();i++){
                JSONObject root = a.getJSONObject(i);
                channels.add(new FChannel(root.getString("name"), root.getString("mode"), root.getInt("characters")));
            }

        }catch(Exception e){
            Log.e(LOG_TAG, "Error loading from CHA : " + e.getMessage());
        }

        return channels;
    }

    public void setDescription(String description){this.description = description; }

    public String getDescription(){ return this.description; }

    public FCharacter getCharacter(String name){
        if(this.characters.contains(name))
            return this.client.getCharacter(name);
        else
            return null;
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUsers(List<String> characters){
        this.characters = characters;
        userListUpdated();
    }

    public void userLeft(String name){
        this.characters.remove(name);
        userListUpdated();
    }

    public void userJoined(String c){
        this.characters.add(c);
        userListUpdated();
    }

    public void userListUpdated(){
        if(this.mListener != null)
            this.mListener.onUserListUpdated(getUsers());
    }

    public String toString(){
        return this.name;
    }

    public boolean hasCharacter(String name){
        return this.characters.contains(name);
    }

    public void addAd(FAdEntry ad){
        addEntry(ad);
    }

    public void addMessage(FTextMessage message){
        addEntry(message);

        if(this.mListener != null)
            this.mListener.onMessageAdded(message);
    }

    public void addEntry(FChatEntry entry){
        this.entries.add(entry);

        if(this.mListener != null){
            this.mListener.onEntryAdded(entry);
            this.mListener.onEntryListUpdated(this.entries);
        }
    }

    public String getName(){
        return this.name;
    }

    public List<FChatEntry> getEntries(){
        return this.entries;
    }

    public List<FCharacter> getUsers(){
        List<FCharacter> filtered = new ArrayList<>();

        for(String c : this.characters){
            if(this.client.getCharacter(c) != null)
                filtered.add(this.client.getCharacter(c));
        }

        Collections.sort(filtered);
        return filtered;
    }

    public int compareTo(Object other){
        FChannel c = (FChannel)other;

        return this.getName().compareTo(c.getName());
    }

}
