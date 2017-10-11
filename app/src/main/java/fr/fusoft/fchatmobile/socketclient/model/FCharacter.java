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
        LOOKING("Looking", "looking"),
        ONLINE("Online", "online"),
        AWAY("Away", "away"),
        BUSY("Busy", "busy"),
        DND("Do Not Disturb", "dnd"),
        IDLE("Idle", "idle"),
        CROWN("Crown", "crown");

        private String label;
        private String id;

        Status(String label, String id){
            this.id = id;
            this.label = label;
        }

        public static Status fromlabel(String label){
            for(Status s : values()){
                if(s.getLabel().equals(label)){
                    return s;
                }
            }
            return null;
        }

        public static Status fromID(String id){
            for(Status s : values()){
                if(s.getIdentifier().equals(id)){
                    return s;
                }
            }
            return null;
        }

        public static List<String> getLabels(){
            List<String> labels = new ArrayList<>();

            for(Status s : values()){
                labels.add(s.getLabel());
            }

            return labels;
        }

        public static List<String> getIdentifiers(){
            List<String> labels = new ArrayList<>();

            for(Status s : values()){
                labels.add(s.getIdentifier());
            }

            return labels;
        }

        public String getIdentifier(){
            return this.id;
        }
        public int getValue(){
            int i = 0;

            for(Status s : values()){
                if(s.equals(this))
                    return i;
                i++;
            }
            return values().length;
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

    public enum Typing{
        CLEAR("clear"),
        PAUSED("paused"),
        TYPING("typing");

        private String label;

        Typing(String label){
            this.label = label;
        }

        public String getLabel(){
            return this.label;
        }

        public static Typing fromlabel(String label){
            for(Typing t : values()){
                if(t.getLabel().equals(label)){
                    return t;
                }
            }

            return CLEAR;
        }
    }

    private String name;
    private String genderString;
    private String statusMessage = "";

    private Gender gender = Gender.ERROR;
    private Status status = Status.ONLINE;
    private Typing typing = Typing.CLEAR;

    private FClient client;

    private List<FChatEntry> privateMessages = new ArrayList<>();

    public interface FCharacterListener{
        void onPrivateMessage(FChatEntry message);
        void onPrivateMessageListUpdated(List<FChatEntry> messages);
        void onTypingStatusChanged(Typing status);
    }

    private FCharacterListener mListener;

    Map<String, ProfileData> profile = new HashMap<String, ProfileData>();

    static String avatarUrl = "https://static.f-list.net/images/avatar/%s.png";

    public FCharacter(NLN token){
        this.name = token.getIdentity();
        this.genderString = token.getGender();
        this.gender = Gender.fromString(genderString);
        this.status = Status.fromID(token.getStatus());
    }

    public FCharacter(String name, String gender, String status, String message){
        this.name = name;
        this.genderString = gender;
        this.gender = Gender.fromString(gender);
        this.statusMessage = message;
        this.status = Status.fromID(status);
    }

    public void setClient(FClient client){
        this.client = client;
    }

    public void setListener(FCharacterListener listener){
        this.mListener = listener;
    }

    public void setTypingStatus(String label){
        setTypingStatus(Typing.fromlabel(label));
    }

    public void setTypingStatus(Typing t){
        this.typing = t;

        if(this.mListener != null)
            this.mListener.onTypingStatusChanged(t);
    }

    public void sendMessage(String message){
        this.client.sendPrivateMessage(this.name, message);
        addMessage(new FTextMessage(this.client.getMainUser(), message));
    }

    public void messageReceived(FChatEntry message){
        setTypingStatus(Typing.CLEAR);
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

    public void setStatus(String status, String message){
        this.setStatus(status, message, true);
    }

    public void requestStatus(String status, String message){
        this.setStatus(status, message, false);
    }

    private void setStatus(String status, String message, boolean received){
        this.status = Status.fromID(status);
        this.setStatusMessage(message);

        if(!received)
            this.client.setStatus(status, message);
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
