package fr.fusoft.fchatmobile.login.controller;

import android.os.AsyncTask;
import android.util.Log;

import fr.fusoft.fchatmobile.login.model.LoginParams;
import fr.fusoft.fchatmobile.login.model.LoginTicket;

/**
 * Created by Florent on 05/09/2017.
 */

public class LoginTask extends AsyncTask<LoginParams, Void, LoginTicket> {

    private static String LOG_TAG = "LoginTask";

    public interface LoginTaskListener{
        public void onLoggedIn(LoginTicket ticket);
        public void onError(String error);
    }

    // This is the reference to the associated listener
    private final LoginTaskListener taskListener;

    public LoginTask(LoginTaskListener listener) {
        // The listener reference is passed in through the constructor
        this.taskListener = listener;
    }

    @Override
    protected LoginTicket doInBackground(LoginParams... args){
        LoginClient c = new LoginClient(args[0]);

        return c.Login();
    }

    @Override
    protected void onPostExecute(LoginTicket result) {

        if(this.taskListener != null) {

            if(result.isLoaded()){
                this.taskListener.onLoggedIn(result);
            }else{
                this.taskListener.onError(result.getError());
            }
        }else{
            if(result.isLoaded()){
                Log.i(LOG_TAG, "Logged in with token " + result.getTicket());
            }else{
                Log.e(LOG_TAG, "Error while login : " + result.getError());
            }
        }



    }
}
