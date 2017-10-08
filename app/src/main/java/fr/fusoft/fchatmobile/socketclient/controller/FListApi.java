package fr.fusoft.fchatmobile.socketclient.controller;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.fusoft.fchatmobile.login.model.LoginTicket;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Florent on 07/10/2017.
 */

public class FListApi {

    LoginTicket ticket;

    public static class KVP{
        private String key = "";
        private String value = "";

        public KVP(String key, String value){
            this.key = key;
            this.value = value;
        }

        public String getKey(){
            return this.key;
        }

        public String getValue(){
            return this.value;
        }

    }

    private final static String LOG_TAG = "FListApi";

    private final static String URL_BOOKMARK_ADD = "https://www.f-list.net/json/api/bookmark-add.php";
    private final static String URL_BOOKMARK_LIST = "https://www.f-list.net/json/api/bookmark-list.php";
    private final static String URL_BOOKMARK_REMOVE = "https://www.f-list.net/json/api/bookmark-remove.php";

    private final static String URL_CHARACTER_DATA = "https://www.f-list.net/json/api/character-data.php";
    private final static String URL_CHARACTER_LIST = "https://www.f-list.net/json/api/character-list.php";

    private final static String URL_FRIEND_PENDING = "https://www.f-list.net/json/api/request-pending.php";
    private final static String URL_FRIEND_SEND = "https://www.f-list.net/json/api/request-send.php";
    private final static String URL_FRIEND_REMOVE = "https://www.f-list.net/json/api/friend-remove.php";
    private final static String URL_FRIEND_REQUEST_LIST = "https://www.f-list.net/json/api/request-list.php";
    private final static String URL_FRIEND_LIST = "https://www.f-list.net/json/api/friend-list.php";
    private final static String URL_FRIEND_ACCEPT = "https://www.f-list.net/json/api/request-accept.php";
    private final static String URL_FRIEND_CANCEL = "https://www.f-list.net/json/api/request-cancel.php";
    private final static String URL_FRIEND_DENY = "https://www.f-list.net/json/api/request-deny.php";

    private final static String URL_LOGIN = "https://www.f-list.net/json/getApiTicket.php";

    public FListApi(){

    }

    public FListApi(LoginTicket ticket){
        setTicket(ticket);
    }

    public static JSONObject login(String user, String pass){
        List<KVP> data = new ArrayList<>();
        data.add(new KVP("secure","yes"));
        data.add(new KVP("account",user));
        data.add(new KVP("password",pass));

        return request(URL_LOGIN, data);
    }

    public void setTicket(LoginTicket ticket){
        this.ticket = ticket;
    }

    public void bookmark(String name){
        bookmark(name, true);
    }

    public void bookmark(String name, boolean add){
        List<KVP> data = new ArrayList<>();
        data.add(new KVP("name",name));

        if(add){
            request(URL_BOOKMARK_ADD, data);
        }else{
            request(URL_BOOKMARK_REMOVE, data);
        }

    }

    public JSONObject getFriendRequests(){
        return request(URL_FRIEND_LIST);
    }

    public JSONObject getFriendList(String source){
        List<KVP> data = new ArrayList<>();
        data.add(new KVP("source_name",source));

        return request(URL_FRIEND_LIST, data);
    }

    public void getPendingFriendRequests(){

    }

    public void sendFriendRequest(String source, String dest){
        List<KVP> data = new ArrayList<>();
        data.add(new KVP("source_name",source));
        data.add(new KVP("dest_name",dest));

        JSONObject o = request(URL_FRIEND_SEND, data);
    }

    @Nullable
    private static JSONObject request(String url){
        return request(url, new ArrayList<KVP>());
    }

    @Nullable
    private static JSONObject request(String url, List<KVP> parameters){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder b =  new FormBody.Builder();

        for(KVP k : parameters){
            b.add(k.getKey(), k.getValue());
        }

        RequestBody formBody = b.build();
        Request r = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(r).execute();

            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            return new JSONObject(response.body().string());

        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }
}
