package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Florent on 05/09/2017.
 */

public class FCommand {

    public enum Tokens{
        IDN,
        PIN,
        CHA,
        FRL,
        HLO,
        NLN
    }

    protected static final String LOG_TAG = "FCommand";

    public JSONObject data;
    public String ID;

    public FCommand(){
        this.data = new JSONObject();
    }

    public FCommand(FCommand source){
        this.ID = source.ID;
        this.data = source.data;
    }

    public FCommand(Tokens token, JSONObject data){
        this.ID = token.toString();
        this.data = data;
    }

    public FCommand(Tokens token){
        this.ID = token.toString();
    }

    public FCommand(String text){
        String[] parts = text.split(" ", 2);

        this.ID = parts[0];

        if(parts.length > 1){
            try{
                this.data = new JSONObject(parts[1]);
            }catch(Exception e){
                Log.e(LOG_TAG, "Error while parsing command " + text + " : " + e.getMessage());
            }

        }
    }

    public String getString(){
        if(data == null)
            return ID;
        else
            return ID + " " + data.toString();
    }

    public String getToken(){
        return this.ID;
    }

    public JSONObject getData(){
        return this.data;
    }
}
