package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florent on 15/09/2017.
 */

public class CDS extends FCommand {
    private String description;
    private String channel;

    public CDS(FCommand source){
        super(source);

        try{
            this.channel = source.data.getString("channel");
            this.description = source.data.getString("description");

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading ADL " + e.getMessage());
        }
    }

    public String getChannel(){
        return this.channel;
    }
    public String getDescription(){
        return this.description;
    }
}