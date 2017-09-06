package fr.fusoft.fchatmobile;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.telecom.ConnectionService;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;

import fr.fusoft.fchatmobile.socketclient.controller.FClient;
import fr.fusoft.fchatmobile.socketclient.controller.FClientService;

/**
 * Created by Florent on 06/09/2017.
 */

public class FChatMobileApplication extends Application {

    private static final String LOG_TAG = "FChatMobileApplication";

    private FClientService clientService;
    private FClient client;

    private final ServiceConnection clientServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FClientService.FClientBinder localBinder = (FClientService.FClientBinder)iBinder;
            clientService = localBinder.getService();
            Log.d(LOG_TAG, "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            clientService = null;
            Log.d(LOG_TAG, "Service disconnected");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Intent serviceIntent = new Intent(this, FClientService.class);
        bindService(serviceIntent, clientServiceConnection, Context.BIND_AUTO_CREATE);

        Log.d(LOG_TAG, "Starting Application");
    }

    public WebSocket getSocket(){
        return clientService.getSocket();
    }

    public FClient getClient(){
        return this.client;
    }

    public boolean isSocketConnected(){
        if(clientService != null)
            if(getSocket() != null)
                return getSocket().isOpen();

        return false;
    }

    public boolean isBound() {
        return clientService != null;
    }

    public void quitApplication() {
        if (isBound()) {
            unbindService(clientServiceConnection);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
