package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.socketclient.model.FChannel;

/**
 * Created by Florent on 09/09/2017.
 */

public class ICH extends FCommand {

    String channel = "";
    String mode = "";

    List<String> users = new ArrayList<>();

    public ICH(FCommand source){
        super(source);

        try{
            this.channel = this.data.getString("channel");
            this.mode = this.data.getString("mode");

            JSONArray a = this.data.getJSONArray("users");

            for(int i=0;i<a.length();i++){
                users.add(a.getJSONObject(i).getString("identity"));
            }

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading ICH " + e.getMessage());
        }
    }

    public String getChannel(){
        return this.channel;
    }
    public String getMode(){
        return this.mode;
    }
    public List<String> getUsers(){
        return this.users;
    }
}