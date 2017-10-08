package fr.fusoft.fchatmobile.login.controller;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import fr.fusoft.fchatmobile.login.model.LoginParams;
import fr.fusoft.fchatmobile.login.model.LoginTicket;
import fr.fusoft.fchatmobile.socketclient.controller.FListApi;
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

        JSONObject o = FListApi.login(p.User, p.Pass);

        if(o != null)
            return new LoginTicket(p.User, o);
        else
            return null;
    }
}
