package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.widget.ListView;

import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FChatEntryAdapter;

/**
 * Created by Florent on 05/09/2017.
 */

public class ChannelFragment extends Fragment{
    private final static String LOG_TAG = "ChannelFragment";

    private enum ChannelType{
        DEBUG(0),
        CONSOLE(1),
        PUBLIC(2),
        PRIVATE(3);

        private int value;

        ChannelType(int i){
            this.value = i;
        }

        public int getValue(){
            return this.value;
        }
    }

    protected String channelName = "";
    protected String iconFile = "";
    protected ChannelType type = ChannelType.DEBUG;
    protected FChatEntryAdapter messageAdapter;
    protected ListView lvMessages;

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

    public void addMessage(final FChatEntry message){
        if(this.messageAdapter != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);
                }
            });
        }
    }
}
