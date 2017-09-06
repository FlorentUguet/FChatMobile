package fr.fusoft.fchatmobile.socketclient.controller;

import fr.fusoft.fchatmobile.socketclient.model.commands.FCommand;
import fr.fusoft.fchatmobile.socketclient.model.commands.ADL;
import fr.fusoft.fchatmobile.socketclient.model.commands.CHA;
import fr.fusoft.fchatmobile.socketclient.model.commands.ERR;
import fr.fusoft.fchatmobile.socketclient.model.commands.FLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.FRL;
import fr.fusoft.fchatmobile.socketclient.model.commands.HLO;
import fr.fusoft.fchatmobile.socketclient.model.commands.IDN;
import fr.fusoft.fchatmobile.socketclient.model.commands.JCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.LCH;
import fr.fusoft.fchatmobile.socketclient.model.commands.MSG;
import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;
import fr.fusoft.fchatmobile.socketclient.model.commands.VAR;

/**
 * Created by Florent on 06/09/2017.
 */

public class FCommandParser {

    public interface  FCommandParserListener{
        void onPing();
        void onIdentification(IDN command);
        void onHello(HLO command);
        void onFriendList(FRL command);
        void onChannelListReceived(CHA command);
        void onServerVariable(VAR command);
        void onAdminList(ADL command);
        void onError(ERR command);
        void onMessage(MSG command);
        void onCharacterOnline(NLN command);
        void onCharacterOffline(FLN command);
        void onChannelJoined(JCH command);
        void onChannelLeft(LCH command);
        void onUnhandledCommand(FCommand command);
    }

    FCommandParserListener listener;

    public FCommandParser(FCommandParserListener listener){
        this.listener = listener;
    }

    public void onCommandReceived(String command){
        onCommandReceived(new FCommand(command));
    }

    public void onCommandReceived(FCommand command){
        switch (command.getToken()){
            case "PIN":
                listener.onPing();
                break;
            case "IDN":
                listener.onIdentification(new IDN(command));
                break;
            case "HLO":
                listener.onHello(new HLO(command));
                break;
            case "CHA":
                listener.onChannelListReceived(new CHA(command));
                break;
            case "ADL":
                listener.onAdminList(new ADL(command));
                break;
            case "VAR":
                listener.onServerVariable(new VAR(command));
                break;
            case "FRL":
                listener.onFriendList(new FRL(command));
                break;
            case "ERR":
                listener.onError(new ERR(command));
                break;
            case "MSG":
                listener.onMessage(new MSG(command));
                break;
            case "NLN":
                listener.onCharacterOnline(new NLN(command));
                break;
            case "FLN":
                listener.onCharacterOffline(new FLN(command));
                break;
            case "JCH":
                listener.onChannelJoined(new JCH(command));
                break;
            case "LCH":
                listener.onChannelLeft(new LCH(command));
                break;
            default:
                listener.onUnhandledCommand(command);
                break;
        }
    }
}
