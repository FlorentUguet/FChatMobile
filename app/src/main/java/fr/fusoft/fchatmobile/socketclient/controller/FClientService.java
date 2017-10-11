package fr.fusoft.fchatmobile.socketclient.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.util.List;
import java.util.Map;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.view.activity.FChatActivity;

/**
 * Created by Florent on 05/09/2017.
 */

public class FClientService extends Service {
    private NotificationManager mNM;
    private Notification.Builder mBuilder;

    private static final String LOG_TAG = "FClientService";
    private static final String SERVER_URL = "ws://chat.f-list.net:9722";
    private static final String SERVER_URL_TLS = "ws://chat.f-list.net:9799";
    private static final String SERVER_URL_DEBUG = "ws://chat.f-list.net:8722";
    private static final String SERVER_URL_TLS_DEBUG = "ws://chat.f-list.net:8799";

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.fclient_started;

    private WebSocket socket;
    private FClient client;

    private static final int notificationId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG,"Service Created");
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        createSocket(SERVER_URL);
        createClient();

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification(R.string.fclient_service_label, R.string.fclient_started);
    }

    public WebSocket getSocket()
    {
        return this.socket;
    }
    public FClient getClient() { return this.client; }

    public void createSocket(String url){
        try{
            WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
            this.socket = factory.createSocket(url);
            this.socket.addListener(setupListener());

        }catch(Exception e){
            Log.e(LOG_TAG,"Error starting socket " + e.toString());
        }
    }

    public WebSocketListener setupListener(){
        return new WebSocketListener() {
            @Override
            public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

            }

            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                showNotification(R.string.fclient_service_label, R.string.fclient_connected);
            }

            @Override
            public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
                stopService();
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                showNotification(R.string.fclient_service_label, R.string.fclient_disconnected);
            }

            @Override
            public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {

            }

            @Override
            public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

            }

            @Override
            public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

            }

            @Override
            public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

            }

            @Override
            public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

            }

            @Override
            public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

            }

            @Override
            public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {

            }

            @Override
            public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {

            }

            @Override
            public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {

            }

            @Override
            public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

            }

            @Override
            public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {

            }

            @Override
            public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {

            }

            @Override
            public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

            }
        };
    }

    private void createClient(){
        this.client = new FClient(this, this.getSocket(), null);
    }

    public void stopService(){
        Log.i(LOG_TAG,"Request for closing the service");
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.fclient_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new FClientBinder();

    public class FClientBinder extends Binder {
        public FClientService getService(){
            return FClientService.this;
        }
    }

    private void showNotification(int label, int content) {
        showNotification(getText(label), getText(content));
    }

    private void showNotification(CharSequence label, CharSequence content) {

        if(this.mBuilder == null){
            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, FChatActivity.class), 0);
            PendingIntent exitIntent = PendingIntent.getBroadcast(this, 0, new Intent("android.intent.CLOSE_APPLICATION"), 0);

            // Set the info for the views that show in the notification panel.
            this.mBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.icon_service)
                    .setTicker(label)  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle(label)  // the label of the entry
                    .setContentText(content)  // the contents of the entry
                    .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                    .addAction(R.drawable.ic_notif_exit, getText(R.string.exit), exitIntent);

            // Send the notification.

            startForeground(notificationId, this.mBuilder.build());
        }else{
            this.mBuilder
                    .setContentTitle(label)  // the label of the entry
                    .setContentText(content);

            mNM.notify(notificationId, this.mBuilder.build());
        }

    }
}
