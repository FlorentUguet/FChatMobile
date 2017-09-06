package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.socketclient.model.FChannel;

/**
 * Created by Florent on 06/09/2017.
 */

public class CHA extends FCommand {
    List<FChannel> channels = new ArrayList<>();

    public CHA(FCommand source){
        super(source);

        try{
            JSONArray a = source.data.getJSONArray("channels");

            for(int i=0;i<a.length();i++){
                channels.add(new FChannel(a.getJSONObject(i)));
            }

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading CHA " + e.getMessage());
        }
    }

    public List<FChannel> getChannels(){
        return this.channels;
    }
}
