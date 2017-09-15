package fr.fusoft.fchatmobile.socketclient.model;

import android.graphics.Color;

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

    String name;
    String gender;
    Status status;

    static String avatarUrl = "https://static.f-list.net/images/avatar/%s.png";

    public FCharacter(NLN token){
        this.name = token.getIdentity();
        this.gender = token.getGender();
        setStatus(token.getStatus());
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

        if(this.getStatusVal() < other.getStatusVal()){
            return -1;
        }
        else if(this.getStatusVal() > other.getStatusVal()){
            return 1;
        }else{
            return this.getName().compareTo(other.getName());
        }
    }
}
