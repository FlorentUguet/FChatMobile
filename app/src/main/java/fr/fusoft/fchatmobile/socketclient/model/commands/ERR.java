package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 06/09/2017.
 */

public class ERR extends FCommand {

    int number;
    String message = "";

    public ERR(FCommand source){
        super(source);

        try{
            this.message = this.data.getString("message");
            this.number = this.data.getInt("number");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading HLO " + e.getMessage());
        }
    }

    public String getMessage(){
        return this.message;
    }
    public int getNumber(){
        return this.number;
    }
}
