package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 07/10/2017.
 */

public class STA extends FCommand {

    private String status = "";
    private String character = "";
    private String statusMsg = "";

    public STA(FCommand source){
        super(source);

        try{
            this.status = this.data.getString("status");
            this.character = this.data.getString("character");
            this.statusMsg = this.data.getString("statusmsg");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading STA " + e.getMessage());
        }
    }

    public STA(String status, String message){
        this.ID = "STA";
        this.status = status;
        this.statusMsg = message;

        try{
            this.data.put("status", status);
            this.data.put("statusmsg", message);
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading STA " + e.getMessage());
        }
    }

    public String getStatus(){
        return this.status;
    }
    public String getStatusMsg() {
        return this.statusMsg;
    }
    public String getCharacter() {
        return this.character;
    }
}