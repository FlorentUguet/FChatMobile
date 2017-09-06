package fr.fusoft.fchatmobile.socketclient.model.messages;

import java.util.Date;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.commands.MSG;

/**
 * Created by Florent on 06/09/2017.
 */

public class FDebugMessage extends FChatEntry {

    String type;
    String text;

    public FDebugMessage(boolean received, String text){
        this.text = text;
        this.timestamp = new Date();

        this.type = received ? "Received" : "Sent";
    }

    public String getHeader(){
        return String.format("[%s] - %s", format.format(this.timestamp), type);
    }

    public String getContent(){
        return this.text;
    }
}
