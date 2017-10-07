package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import fr.fusoft.fchatmobile.socketclient.model.ProfileData;

/**
 * Created by Florent on 07/10/2017.
 */

public class PRD extends FCommand {
    ProfileData profile;

    public PRD(FCommand source){
        super(source);

        try{
            String key = "";
            String value = "";

            if(this.data.has("key"))
                key = this.data.getString("key");

            if(this.data.has("value"))
                value = this.data.getString("value");

            this.profile = new ProfileData(this.data.getString("type"), key, value, this.data.getString("character"));

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading PRD " + e.getMessage());
        }
    }

    public ProfileData getProfile(){
        return this.profile;
    }
}
