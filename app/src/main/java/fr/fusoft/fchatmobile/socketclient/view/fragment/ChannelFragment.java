package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.os.Bundle;
import android.app.Fragment;

/**
 * Created by Florent on 05/09/2017.
 */

public class ChannelFragment extends Fragment{
    private final static String LOG_TAG = "ChannelFragment";

    private String channelName = "";
    private String iconFile = "";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void setIconFile(String url){
        this.iconFile = url;
    }

    public void setChannelName(String name){
        this.channelName = name;
    }

    public String getIcon(){
        return this.iconFile;
    }

    public String getChannelName(){
        return this.channelName;
    }

}
