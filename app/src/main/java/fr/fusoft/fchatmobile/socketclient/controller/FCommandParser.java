package fr.fusoft.fchatmobile.socketclient.controller;

import java.util.Arrays;

import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.commands.FCommand;
import fr.fusoft.fchatmobile.socketclient.model.commands.LRP;
import fr.fusoft.fchatmobile.socketclient.model.commands.MSG;
import fr.fusoft.fchatmobile.socketclient.model.commands.PRO;
import fr.fusoft.fchatmobile.socketclient.model.commands.STA;

/**
 * Created by fuguet on 05/11/17.
 */

public class FCommandParser {

    enum Command{

        AD("ad"),
        PROFILE("profile"),
        LOOKING("looking"),
        AWAY("away"),
        BUSY("busy"),
        DND("dnd"),
        ONLINE("online"),
        STATUS("status"),
        MESSAGE("msg");

        String command;

        Command(String command){
            this.command = command;
        }

        public String getCommand(){
            return this.command;
        }

        static public Command fromString(String command){
            for(Command c : values()){
                if(c.getCommand().equals(command))
                    return c;
            }

            return null;
        }

    }

    public FCommandParser(){

    }

    public static FCommand parse(String text, String channel){
        String[] split = text.substring(1).split(" ");

        String cmd = split[0];
        String[] arg = Arrays.copyOfRange(split, 1,split.length-1);

        Command c = Command.fromString(cmd);

        if(c == null){
            return null;
        }

        switch (c){
            case AD:
                return new LRP(channel, arg[0]);
            case PROFILE:
                return new PRO(arg[0]);
            case STATUS:
                return new STA(arg[0], arg[1]);
            case DND:
                return new STA(FCharacter.Status.DND.getIdentifier(), arg[0]);
            case LOOKING:
                return new STA(FCharacter.Status.LOOKING.getIdentifier(), arg[0]);
            case AWAY:
                return new STA(FCharacter.Status.AWAY.getIdentifier(), arg[0]);
            case ONLINE:
                return new STA(FCharacter.Status.ONLINE.getIdentifier(), arg[0]);
            case BUSY:
                return new STA(FCharacter.Status.BUSY.getIdentifier(), arg[0]);
            case MESSAGE:
                return new MSG(channel, arg[0]);
            default:
                return null;
        }
    }
}
