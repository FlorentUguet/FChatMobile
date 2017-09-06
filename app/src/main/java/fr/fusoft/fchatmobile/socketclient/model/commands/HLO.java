package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 06/09/2017.
 */

public class HLO extends FCommand {

    String message = "";

    public HLO(FCommand source){
        super(source);

        try{
            this.message = this.data.getString("message");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading HLO " + e.getMessage());
        }
    }

    public String getMessage(){
        return this.message;
    }
}
