package fr.fusoft.fchatmobile.login.controller;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import fr.fusoft.fchatmobile.login.model.LoginParams;
import fr.fusoft.fchatmobile.login.model.LoginTicket;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Florent on 05/09/2017.
 */

public class LoginClient {
    public static String LOG_TAG = "LoginClient";

    public static String LOGIN_URL = "https://www.f-list.net/json/getApiTicket.json";

    LoginParams params;

    public LoginClient(String User, String Pass)
    {
        this.params = new LoginParams(User,Pass);
    }

    public LoginClient(LoginParams p)
    {
        this.params = p;
    }

    public LoginTicket Login(){
        return Login(this.params);
    }

    public LoginTicket Login(LoginParams p){

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("secure", "yes")
                .add("account", p.User)
                .add("password", p.Pass)
                .build();

        Request r = new Request.Builder()
                .url(LOGIN_URL)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(r).execute();

            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            JSONObject o = new JSONObject(response.body().string());
            return new LoginTicket(p.User, o);

        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }
}
