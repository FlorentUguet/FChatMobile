package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 06/09/2017.
 */

public class IDN extends FCommand {

    String character = "";

    public IDN(FCommand source){
        super(source);

        try{
            this.character = this.data.getString("character");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading IDN " + e.getMessage());
        }
    }

    public String getCharacter(){
        return this.character;
    }
}