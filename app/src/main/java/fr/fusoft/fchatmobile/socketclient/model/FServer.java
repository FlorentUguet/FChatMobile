package fr.fusoft.fchatmobile.socketclient.model;

import android.util.Log;

import org.json.JSONObject;

import fr.fusoft.fchatmobile.socketclient.model.commands.VAR;

/**
 * Created by Florent on 06/09/2017.
 */

public class FServer {
    private static final String LOG_TAG = "FServer";

    public JSONObject variables = new JSONObject();

    public FServer(){

    }

    public void setVariable(VAR variable){
        try{
            this.variables.put(variable.getVariable(), variable.getValue());
        }catch(Exception e){
            Log.e(LOG_TAG, "Error addin variable " + variable.getVariable() + " : " + e.getMessage());
        }

    }
}
