package fr.fusoft.fchatmobile.socketclient.model.messages;

import fr.fusoft.fchatmobile.socketclient.model.commands.FLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.JCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.LCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;

/**
 * Created by Florent on 06/09/2017.
 */

public class FConnectionMessage extends FChatEntry  {

    public FConnectionMessage(NLN token){
        this.type = Type.NLN;
        this.header = token.getIdentity();
        this.content = token.getIdentity() + " is now online";
    }

    public FConnectionMessage(FLN token){
        this.type = Type.FLN;
        this.header = token.getCharacter();
        this.content = token.getCharacter() + " is now offline";
    }

    public FConnectionMessage(JCH token){
        this.type = Type.JCH;
        this.header = token.getCharacter();
        this.content = token.getCharacter() + " joined " + token.getChannel();
    }

    public FConnectionMessage(LCH token){
        this.type = Type.LCH;
        this.header = token.getCharacter();
        this.content = token.getCharacter() + " left " + token.getChannel();
    }

    public String getHeader(){
        return String.format("[%s] - %s", format.format(this.timestamp), this.header);
    }

    public String getContent(){
        return this.content;
    }
}
