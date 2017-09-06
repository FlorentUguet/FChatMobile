package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 06/09/2017.
 */

public class MSG extends FCommand {

    String character = "";
    String message;
    String channel;

    public MSG(FCommand source){
        super(source);

        try{
            this.character = this.data.getString("character");
            this.channel = this.data.getString("channel");
            this.message = this.data.getString("message");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading IDN " + e.getMessage());
        }
    }

    public String getCharacter(){
        return this.character;
    }

    public String getMessage(){
        return this.message;
    }
    public String getChannel(){
        return this.channel;
    }
}
