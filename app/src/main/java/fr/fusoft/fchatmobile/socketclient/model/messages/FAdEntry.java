package fr.fusoft.fchatmobile.socketclient.model.messages;

import java.util.Date;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

/**
 * Created by Florent on 06/09/2017.
 */

public class FAdEntry extends FChatEntry {
    FCharacter sender;
    String message;

    public FAdEntry(FCharacter sender, String message){
        this.message = message;
        this.sender = sender;
        this.timestamp = new Date();
    }

    public String getHeader(){
        return String.format("[%s] - %s (Ad)", format.format(this.timestamp), sender.getName());
    }

    public String getContent(){
        return this.message;
    }
}