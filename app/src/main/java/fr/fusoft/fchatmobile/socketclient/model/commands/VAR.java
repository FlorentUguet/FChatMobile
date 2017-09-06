package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

/**
 * Created by Florent on 06/09/2017.
 */

public class VAR extends FCommand {

    String variable = "";
    Object value = "";

    public VAR(FCommand source){
        super(source);

        try{
            this.variable = this.data.getString("variable");
            this.value = this.data.get("value");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading VAR " + e.getMessage());
        }
    }

    public String getVariable(){
        return this.variable;
    }
    public Object getValue() {
        return this.value;
    }
}