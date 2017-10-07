package fr.fusoft.fchatmobile.socketclient.model.commands;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

/**
 * Created by Florent on 07/10/2017.
 */

public class LIS extends FCommand {

    List<FCharacter> characters = new ArrayList<>();

    public LIS(FCommand source) {
        super(source);

        try {
            JSONArray arr = this.data.getJSONArray("characters");
            for (int i = 0; i < arr.length(); i++) {
                JSONArray obj = arr.getJSONArray(i);
                this.characters.add(new FCharacter(
                        obj.getString(0),
                        obj.getString(1),
                        obj.getString(2),
                        obj.getString(3)
                ));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while loading LIS " + e.getMessage());
        }
    }

    public List<FCharacter> getCharacters() {
        return this.characters;
    }
}