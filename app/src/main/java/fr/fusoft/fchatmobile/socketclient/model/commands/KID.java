package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONObject;

import fr.fusoft.fchatmobile.socketclient.model.FChannel;

/**
 * Created by Florent on 07/10/2017.
 */

public class KID extends FCommand {

    public enum Type{
        START("start"),
        END("end"),
        CUSTOM("custom");

        String type;

        Type(String type){
            this.type = type;
        }

        public String getName(){
            return this.type;
        }

        public static Type fromString(String s){
            for(Type t : values()){
                if(t.getName().equals(s))
                    return t;
            }

            return null;
        }
    }

    Type type;
    String message;
    String key;
    String value;
    String character;

    public KID(FCommand source){
        super(source);

        try{
            this.type = Type.fromString(this.data.getString("type"));
            this.key = this.data.getString("key");
            this.value = this.data.getString("value");
            this.message = this.data.getString("message");
            this.character = this.data.getString("character");

        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading KID " + e.getMessage());
        }
    }

    public String getMessage(){
        return this.message;
    }

    public boolean isStart(){
        return this.type == Type.START;
    }

    public boolean isEnd(){
        return this.type == Type.END;
    }

    public boolean isCustom(){
        return this.type == Type.CUSTOM;
    }
}
