package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

/**
 * Created by Florent on 06/09/2017.
 */

public class NLN extends FCommand {
    String identity;
    String gender;
    String status;

    public NLN(FCommand source){
        super(source);

        try{
            this.identity = this.data.getString("identity");
            this.gender = this.data.getString("gender");
            this.status = this.data.getString("status");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading IDN " + e.getMessage());
        }
    }

    public String getIdentity(){
        return this.identity;
    }

    public String getGender(){
        return  this.gender;
    }

    public String getStatus(){
        return this.status;
    }
}