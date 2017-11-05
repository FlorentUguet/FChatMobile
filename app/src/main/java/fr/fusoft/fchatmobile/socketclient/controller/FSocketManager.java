package fr.fusoft.fchatmobile.socketclient.controller;
import android.content.Context;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.login.model.LoginTicket;
import fr.fusoft.fchatmobile.socketclient.model.commands.FCommand;

/**
 * Created by Florent on 05/09/2017.
 */

public class FSocketManager {
    private static final String LOG_TAG = "FSocketManager";
    private WebSocket socket;
    private WebSocketListener socketListener;

    public interface FSocketListener{
        void onConnected();
        void onDisconnected();
        void onTextReceived(String message);
        void onTextSent(String message);
    }

    private FSocketListener listener;
    private FTokenHandler parser;
    private Context context;

    public FSocketManager(Context context, WebSocket socket, FTokenHandler parser, FSocketListener listener)
    {
        this.context = context;
        this.socket = socket;
        this.listener = listener;
        setCommandParser(parser);

        setupSocketListener();
    }

    public void setCommandParser(FTokenHandler parser){
        this.parser = parser;
    }

    public void identify(LoginTicket ticket){
        try{
            JSONObject data = new JSONObject();
            data.put("cname", context.getText(R.string.app_name));
            data.put("cversion", context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
            data.put("character", ticket.getCharacter());
            data.put("ticket", ticket.getTicket());
            data.put("account", ticket.getAccount());
            data.put("method", "ticket");

            FCommand command = new FCommand(FCommand.Tokens.IDN, data);
            sendCommand(command);
        }catch(Exception e){
            Log.e(LOG_TAG, "Exception while sending IDN : " + e.toString());
        }
    }

    public void connect(){
        try{
            this.socket.connectAsynchronously();
        }catch(Exception e){
            Log.e(LOG_TAG, "Exception while opening socket " + e.toString());
        }
    }

    public boolean isConnected(){
        return socket.isOpen();
    }

    public void sendCommand(FCommand command){
        sendMessage(command.getString());
    }

    public void sendMessage(String message){
        this.socket.sendText(message);
        listener.onTextSent(message);
    }

    public void setupSocketListener(){

        if(this.socketListener == null){
            this.socketListener = new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    listener.onConnected();
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    listener.onDisconnected();
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    listener.onTextReceived(text);
                    parser.onCommandReceived(text);
                }
            };
        }

        this.socket.addListener(socketListener);
    }

    public void sendPing(){
        sendCommand(new FCommand(FCommand.Tokens.PIN));
    }

    public void disconnectListener(){
        this.socket.removeListener(socketListener);
    }
}
