package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

/**
 * Created by Florent on 08/10/2017.
 */

public class TPN extends FCommand {

    private String character;
    private String status;

    public TPN(String character, FCharacter.Typing status){
        this.ID = "TPN";

        try{
            this.data.put("character", character);
            this.data.put("status", status.getLabel());
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading TPN " + e.getMessage());
        }
    }

    public TPN(FCommand source){
        super(source);

        try{
            this.status = this.data.getString("status");
            this.character = this.data.getString("character");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading TPN " + e.getMessage());
        }
    }

    public String getStatus(){
        return this.status;
    }
    public String getCharacter() {
        return this.character;
    }
}
