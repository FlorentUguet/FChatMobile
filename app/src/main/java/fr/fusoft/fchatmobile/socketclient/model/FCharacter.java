package fr.fusoft.fchatmobile.socketclient.model;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;

/**
 * Created by Florent on 05/09/2017.
 */

public class FCharacter implements Comparable {
    public enum Status{
        LOOKING(0),
        ONLINE(1),
        AWAY(2),
        BUSY(3),
        DND(3),
        UNDOCUMENTED(4);

        private int value;

        Status(int i){
            this.value = i;
        }

        public int getValue(){
            return this.value;
        }
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

    String name;
    Gender gender;
    String genderString;
    Status status;

    List<ProfileData> profile = new ArrayList<>();

    static String avatarUrl = "https://static.f-list.net/images/avatar/%s.png";

    public FCharacter(NLN token){
        this.name = token.getIdentity();
        this.genderString = token.getGender();
        this.gender = Gender.fromString(genderString);
        setStatus(token.getStatus());
    }

    public List<ProfileData> getProfile(){
        return this.profile;
    }

    public void addProfileData(ProfileData data){
        this.profile.add(data);
        Collections.sort(this.profile, new Comparator<ProfileData>() {
            @Override
            public int compare(ProfileData profileData, ProfileData t1) {
                return profileData.getDataKey().compareTo(t1.getDataKey());
            }
        });
    }

    public void clearProfileData(){
        this.profile.clear();
    }

    private void setStatus(String status){
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
