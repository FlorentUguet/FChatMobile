package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONObject;

import fr.fusoft.fchatmobile.socketclient.model.FChannel;

/**
 * Created by Florent on 06/09/2017.
 */

public class JCH extends FCommand {

    String character;
    String title;
    String channel;

    public JCH(FCommand source){
        super(source);

        try{
            this.character = this.data.getString("character");
            this.channel = this.data.getString("channel");
            this.title = this.data.getString("title");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading IDN " + e.getMessage());
        }
    }
    public JCH(String channel){
        this.ID = "JCH";

        try{
            this.data = new JSONObject();
            this.data.put("channel", channel);
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while creating JCH Request " + e.getMessage());
        }
    }
    public JCH(FChannel channel){
        this.ID = "JCH";

        try{
            this.data = new JSONObject();
            this.data.put("channel", channel.getName());
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while creating JCH Request " + e.getMessage());
        }
    }

    public String getCharacter(){
        return this.character;
    }
    public String getTitle(){
        return this.title;
    }
    public String getChannel(){
        return this.channel;
    }
}