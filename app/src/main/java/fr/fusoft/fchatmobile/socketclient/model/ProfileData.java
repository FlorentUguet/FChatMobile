package fr.fusoft.fchatmobile.socketclient.model;

import fr.fusoft.fchatmobile.socketclient.model.commands.PRD;

/**
 * Created by Florent on 07/10/2017.
 */

public class ProfileData {
    public enum Type{
            START("start"),
            END("end"),
            INFO("info"),
            SELECT("select");

            String type;

            Type(String type){
                this.type = type;
            }

            public String getName(){
                return this.type;
            }

            public static Type fromString(String s){
                for(Type t : values()){
                    if(t.getName().equals(s))
                        return t;
                }

                return null;
            }
        }

    private String character = "";
    private String key = "";
    private String value = "";
    private Type type;

    public ProfileData(String type, String key, String value, String character){
        this.type = Type.fromString(type);
        this.key = key != null ? key : "";
        this.value = value;
        this.character = character;
    }

    public String getDataKey(){
        return this.key;
    }

    public String getCharacter(){
        return this.character;
    }

    public String getValue(){
        return this.value;
    }

    public Type getType(){
        return this.type;
    }

    public boolean isStart(){
        return this.type.equals(Type.START);
    }

    public boolean isEnd(){
        return this.type.equals(Type.END);
    }
}
