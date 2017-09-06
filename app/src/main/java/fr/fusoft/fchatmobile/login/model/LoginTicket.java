package fr.fusoft.fchatmobile.login.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florent on 05/09/2017.
 */

public class LoginTicket implements Serializable {
    public static String LOG_TAG = "LoginTicket";

    public List<String> characters = new ArrayList<>();
    public String defaultCharacter;
    public int defaultCharacterIndex;

    private String account;
    private String ticket;
    private List< List<String> > friends = new ArrayList<>();
    private List<String> bookmarks = new ArrayList<>();

    public String selectedCharacter;
    public int selectedIndex;

    private String error = "";
    private boolean loaded = false;

    public LoginTicket(String account, JSONObject obj){
        this.account = account;
        try{

            if(obj.has("error") && !obj.getString("error").equals("")){
                this.error = obj.getString("error");
                this.loaded = false;
            }else{
                JSONArray aChar = obj.getJSONArray("characters");

                for(int i=0; i<aChar.length(); i++){
                    String character = aChar.getString(i);
                    this.characters.add(character);

                    this.friends.add((new ArrayList<String>()));

                    JSONArray aFriends = obj.getJSONArray("friends");
                    for(int j=0; j<aFriends.length(); j++)
                        if(aFriends.getJSONObject(j).getString("dest_name").equals(character))
                            this.friends.get(i).add(aFriends.getJSONObject(j).getString("source_name"));
                }

                this.defaultCharacter = obj.getString("default_character");

                this.defaultCharacterIndex = 0;

                for(int i=0; i<this.characters.size(); i++){
                    if(this.characters.get(i).equals(this.defaultCharacter)){
                        this.defaultCharacterIndex = i;
                        break;
                    }
                }

                this.ticket = obj.getString("ticket");

                for (int i=0; i<obj.getJSONArray("bookmarks").length(); i++)
                    this.bookmarks.add(obj.getJSONArray("bookmarks").getString(i));
                this.loaded = true;
            }
        }catch(Exception e){
            Log.e(LOG_TAG, "Error while loading ticket : " + e.getMessage());
        }

    }


    public String getTicket(){return this.ticket;}
    public boolean isLoaded() {return this.loaded;}
    public String getError() {return this.error;}

    public String getCharacter(){return this.selectedCharacter;}
    public String getAccount() {return this.account;};
}
