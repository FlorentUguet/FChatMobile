package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 07/10/2017.
 */

public class PRO extends FCommand {

    String character;

    public PRO(String character){

        try{
            this.ID = "PRO";
            this.data.put("character",character);
            this.character = character;

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while creating PRO " + e.getMessage());
        }
    }
    public String getCharacter(){
        return this.character;
    }
}