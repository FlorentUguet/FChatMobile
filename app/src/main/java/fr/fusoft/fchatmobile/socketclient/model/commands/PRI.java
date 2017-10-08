package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 08/10/2017.
 */

public class PRI extends FCommand {

    String character;
    String message;

    public PRI(String recipient, String message){

        try{
            this.ID = "PRI";
            this.data.put("recipient",recipient);
            this.data.put("message",message);

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while creating PRI " + e.getMessage());
        }
    }

    public PRI(FCommand source){
        super(source);

        try{
            this.character = this.data.getString("character");
            this.message = this.data.getString("message");

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading PRD " + e.getMessage());
        }
    }

    public String getCharacter(){
        return this.character;
    }
    public String getMessage(){
        return this.message;
    }
}