package fr.fusoft.fchatmobile.socketclient.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.fusoft.fchatmobile.socketclient.model.commands.CHA;
import fr.fusoft.fchatmobile.socketclient.model.commands.ICH;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;

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
    Map<String, FCharacter> characters = new HashMap<>();


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

    public FCharacter getCharacter(String name){
        return this.characters.get(name);
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUsers(Map<String, FCharacter> characters){
        this.characters = characters;
    }

    public void userLeft(String name){
        this.characters.remove(name);
    }

    public void userJoined(FCharacter c){
        this.characters.put(c.getName(), c);
    }

    public void userUpdated(FCharacter c){
        this.characters.put(c.getName(), c);
    }

    public String toString(){
        return this.name;
    }

    boolean hasCharacter(String name){
        return (getCharacter(name) != null);
    }

    public void addEntry(FChatEntry entry){
        this.entries.add(entry);
    }

    public String getName(){
        return this.name;
    }

    public List<FChatEntry> getEntries(){
        return this.entries;
    }

    public List<FCharacter> getUsers(){
        List<FCharacter> c = new ArrayList<FCharacter>(this.characters.values());
        Collections.sort(c);
        return c;
    }

    public int compareTo(Object other){
        FChannel c = (FChannel)other;

        return this.getName().compareTo(c.getName());
    }
}
