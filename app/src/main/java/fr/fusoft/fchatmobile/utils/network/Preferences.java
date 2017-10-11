package fr.fusoft.fchatmobile.utils.network;

import android.content.Context;
import android.content.SharedPreferences;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Florent on 11/10/2017.
 */

public class Preferences {
    private Context mContext;
    private SharedPreferences mPrefs;

    public Preferences(Context c){
        this.mPrefs = c.getSharedPreferences(c.getString(R.string.shared_prefs), MODE_PRIVATE);
        this.mContext = c;
    }

    public String getDefaultStatusMessage(String status){
        String key = "status-" + status;
        return this.mPrefs.getString(key, "");
    }

    public void setDefaultStatusMessage(String status, String message){
        String key = "status-" + status;
        this.mPrefs.edit().putString(key, message).apply();
    }
}
