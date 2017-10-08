package fr.fusoft.fchatmobile.socketclient.model;

import android.graphics.Color;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.controller.FClient;
import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.model.messages.FTextMessage;

/**
 * Created by Florent on 05/09/2017.
 */

public class FCharacter implements Comparable {
    public enum Status{
        LOOKING(0, "Looking"),
        ONLINE(1, "Online"),
        AWAY(2, "Away"),
        BUSY(3, "Busy"),
        DND(4, "Do Not Disturb"),
        UNDOCUMENTED(4, "X");

        private String label;
        private int value;

        Status(int i, String label){
            this.value = i;
            this.label = label;
        }

        public int getValue(){
            return this.value;
        }
        public String getLabel(){return this.label;}
        public String getLetter(){return getLabel().substring(0,1);}
    }

    public enum Gender{

        MALE("Male", R.color.colorMale),
        FEMALE("Female", R.color.colorFemale),
        TRANSGENDER("Transgender", R.color.colorTransgender),
        HERM("Herm", R.color.colorHerm),
        SHEMALE("Shemale", R.color.colorShemale),
        MALEHERM("Male-Herm", R.color.colorMaleHerm),
        CUNTBOY("Cunt-boy", R.color.colorCuntBoy),
        NONE("None", R.color.colorNone),
        ERROR("Error", R.color.colorInvalidGender);

        String name;
        int color;

        Gender(String name, int color){
            this.name = name;
            this.color = color;
        }

        public String getName(){
            return this.name;
        }

        public int getColor(){
            return this.color;
        }

        public static Gender fromString(String name){
            for(Gender g : values()){
                if(g.getName().equals(name)){
                    return g;
                }
            }

            return null;
        }
    }

    private String name;
    private Gender gender;
    private String genderString;
    private Status status;
    private String statusMessage = "";

    private FClient client;

    private List<FChatEntry> privateMessages = new ArrayList<>();

    public interface FCharacterListener{
        void onPrivateMessage(FChatEntry message);
        void onPrivateMessageListUpdated(List<FChatEntry> messages);
    }

    private FCharacterListener mListener;

    Map<String, ProfileData> profile = new HashMap<String, ProfileData>();

    static String avatarUrl = "https://static.f-list.net/images/avatar/%s.png";

    public FCharacter(NLN token){
        this.name = token.getIdentity();
        this.genderString = token.getGender();
        this.gender = Gender.fromString(genderString);
        setStatus(token.getStatus());
    }

    public FCharacter(String name, String gender, String status, String message){
        this.name = name;
        this.genderString = gender;
        this.gender = Gender.fromString(gender);
        this.statusMessage = message;
        setStatus(status);
    }

    public void setClient(FClient client){
        this.client = client;
    }

    public void setListener(FCharacterListener listener){
        this.mListener = listener;
    }

    public void sendMessage(String message){
        this.client.sendPrivateMessage(this.name, message);
        addMessage(new FTextMessage(this.client.getMainUser(), message));
    }

    public void messageReceived(FChatEntry message){
        addMessage(message);
    }

    private void addMessage(FChatEntry message){
        this.privateMessages.add(message);
        if(this.mListener != null){
            this.mListener.onPrivateMessage(message);
            this.mListener.onPrivateMessageListUpdated(this.privateMessages);
        }
    }

    public List<ProfileData> getProfile(){
        List<ProfileData> p = new ArrayList<>(profile.values());
        Collections.sort(p, new Comparator<ProfileData>() {
            @Override
            public int compare(ProfileData profileData, ProfileData t1) {
                return profileData.getDataKey().compareTo(t1.getDataKey());
            }
        });

        return p;
    }

    public void addProfileData(ProfileData data){
        this.profile.put(data.getDataKey(), data);

    }

    public String getProfileData(String key){
        if(profile.containsKey(key))
            return profile.get(key).getValue();
        else
            return "";
    }

    public void clearProfileData(){
        this.profile.clear();
    }

    public void setStatus(String status){
        switch(status){
            case "online":
                this.status = Status.ONLINE;
                break;
            case "away":
                this.status = Status.AWAY;
                break;
            case "busy":
                this.status = Status.BUSY;
                break;
            case "looking":
                this.status = Status.LOOKING;
                break;
            case "dnd":
                this.status = Status.DND;
                break;
            default:
                this.status = Status.UNDOCUMENTED;
                break;
        }
    }

    public void setStatusMessage(String message){
        this.statusMessage = message;
    }

    public String getStatusMessage(){
        return this.statusMessage;
    }

    public String getName(){
        return this.name;
    }

    public Gender getGender(){return this.gender;}

    public String getFormattedName(){
        return this.name;
    }

    public Status getStatus(){
        return this.status;
    }

    public int getStatusVal(){
        return this.status.getValue();
    }

    public String getAvatarUrl(){
        return String.format(avatarUrl, getName().toLowerCase());
    }

    public int compareTo(Object o){
        FCharacter other = (FCharacter)o;

        int cName = this.getName().compareTo(other.getName());
        int cGender = this.getGender().getName().compareTo(other.getGender().getName());

        if(this.getStatusVal() < other.getStatusVal()){
            return -1;
        }

        if(cGender != 0){
            return cGender;
        }else{
            return cName;
        }
    }
}
