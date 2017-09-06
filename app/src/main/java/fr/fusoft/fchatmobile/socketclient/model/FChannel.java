package fr.fusoft.fchatmobile.socketclient.model;

import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import fr.fusoft.fchatmobile.socketclient.model.commands.CHA;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;

/**
 * Created by Florent on 06/09/2017.
 */

public class FChannel {
    private static final String LOG_TAG = "FChannel";

    private String name;
    private String mode;
    private int characters;

    List<FChatEntry> entries;

    public FChannel(JSONObject obj){
        try{
            this.name = obj.getString("name");
            this.mode = obj.getString("mode");
            this.characters = obj.getInt("characters");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading " + e.getMessage());
        }
    }

    public FChannel(String name, String mode, int characters){
        this.name = name;
        this.mode = mode;
        this.characters = characters;
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
}
