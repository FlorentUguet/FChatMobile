package fr.fusoft.fchatmobile.socketclient.model.messages;

import java.util.Date;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.commands.MSG;

/**
 * Created by Florent on 06/09/2017.
 */

public class FTextMessage extends FChatEntry {

    FCharacter sender;
    String message;

    public FTextMessage(FCharacter sender, String message){
        this.message = message;
        this.sender = sender;
        this.timestamp = new Date();
    }

    public String getHeader(){
        return String.format("[%s] - %s", format.format(this.timestamp), sender.getName());
    }

    public String getContent(){
        return this.message;
    }
}
