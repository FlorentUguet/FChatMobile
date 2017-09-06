package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florent on 06/09/2017.
 */

public class ADL extends FCommand {
    List<String> ops = new ArrayList<>();

    public ADL(FCommand source){
        super(source);

        try{
            JSONArray a = source.data.getJSONArray("ops");

            for(int i=0;i<a.length();i++){
                ops.add(a.getString(i));
            }

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading ADL " + e.getMessage());
        }
    }

    public List<String> geOps(){
        return this.ops;
    }
}
