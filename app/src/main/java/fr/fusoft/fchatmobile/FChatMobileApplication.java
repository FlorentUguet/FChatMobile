package fr.fusoft.fchatmobile;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.telecom.ConnectionService;
import android.util.Log;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;

import fr.fusoft.fchatmobile.socketclient.controller.FClient;
import fr.fusoft.fchatmobile.socketclient.controller.FClientService;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.view.activity.FChatActivity;

/**
 * Created by Florent on 06/09/2017.
 */

public class FChatMobileApplication extends Application {

    private static final String LOG_TAG = "FChatMobileApplication";

    private FClientService clientService;
    private boolean serviceBound = false;
    private ServiceConnectedListener mListener;
    private FChatActivity currentActivity;

    public interface ServiceConnectedListener{
        void onServiceConnected();
        void onServiceDisconnected();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Closing service", Toast.LENGTH_SHORT).show();
                }
            });
            stopService(new Intent(FChatMobileApplication.this,FClientService.class));
            currentActivity.finishAffinity();

            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        }

    };

    private final ServiceConnection clientServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FClientService.FClientBinder localBinder = (FClientService.FClientBinder)iBinder;
            clientService = localBinder.getService();
            Log.d(LOG_TAG, "Service connected");
            serviceBound = true;

            if(mListener != null)
                mListener.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            clientService = null;
            Log.d(LOG_TAG, "Service disconnected");
            serviceBound = false;

            if(mListener != null)
                mListener.onServiceDisconnected();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Intent serviceIntent = new Intent(this, FClientService.class);
        bindService(serviceIntent, clientServiceConnection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("android.intent.CLOSE_APPLICATION");
        registerReceiver(mReceiver, filter);

        Log.d(LOG_TAG, "Starting Application");
    }

    public void setCurrentActivity(FChatActivity activity){
        this.currentActivity = activity;
    }

    public void setListener(ServiceConnectedListener listener){
        mListener = listener;
    }

    public WebSocket getSocket(){
        return clientService.getSocket();
    }

    public boolean isServiceBound(){
        return this.serviceBound;
    }

    public FClient getClient(){
        return clientService.getClient();
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
