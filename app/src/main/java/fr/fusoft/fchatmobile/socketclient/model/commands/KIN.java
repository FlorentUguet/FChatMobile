package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONObject;

import fr.fusoft.fchatmobile.socketclient.model.FChannel;

/**
 * Created by Florent on 07/10/2017.
 */

public class KIN extends FCommand {

    String character;

    public KIN(String character){

        try{
            this.ID = "KIN";
            this.data.put("character",character);
            this.character = character;

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while creating KIN " + e.getMessage());
        }
    }
    public String getCharacter(){
        return this.character;
    }
}