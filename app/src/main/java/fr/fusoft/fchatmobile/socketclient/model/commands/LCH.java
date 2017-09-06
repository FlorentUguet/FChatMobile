package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONObject;

import fr.fusoft.fchatmobile.socketclient.model.FChannel;

/**
 * Created by Florent on 06/09/2017.
 */

public class LCH extends FCommand {

    String character;
    String channel;

    public LCH(FCommand source){
        super(source);

        try{
            this.character = this.data.getString("character");
            this.channel = this.data.getString("channel");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading IDN " + e.getMessage());
        }
    }

    public String getCharacter(){
        return this.character;
    }
    public String getChannel(){
        return this.channel;
    }
}