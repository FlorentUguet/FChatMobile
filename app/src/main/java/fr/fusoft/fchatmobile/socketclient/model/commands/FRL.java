package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florent on 06/09/2017.
 */

public class FRL extends FCommand {

    List<String> friends = new ArrayList<>();

    public FRL(FCommand source){
        super(source);

        try{
            JSONArray a = source.data.getJSONArray("characters");

            for(int i=0;i<a.length();i++){
                friends.add(a.getString(i));
            }

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading FRL " + e.getMessage());
        }
    }

    public List<String> getFriends(){
        return this.friends;
    }
}
