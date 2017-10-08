package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.controller.FClient;
import fr.fusoft.fchatmobile.socketclient.model.FChannel;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.ProfileData;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FProfileDataAdapter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist.FCharacterListAdapter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist.FCharacterListCompactAdapter;
import fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist.FCharacterListLargeAdapter;

/**
 * Created by Florent on 08/10/2017.
 */

public class FriendsListDialogFragment extends DialogFragment {

    private ListView lvFriends;
    private ListView lvBookmarks;
    private TabHost tabHost;

    private FCharacterListAdapter adapterFriends;
    private FCharacterListAdapter adapterBookmarks;

    private View root;

    private FClient client;

    public static FriendsListDialogFragment newInstance() {
        FriendsListDialogFragment f = new FriendsListDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_friends_list, container, false);
        this.lvFriends = (ListView) root.findViewById(R.id.lvFriends);
        this.lvBookmarks = (ListView) root.findViewById(R.id.lvBookmarks);
        this.tabHost = (TabHost) this.root.findViewById(R.id.tabHost);

        //Tab Host
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Friends");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Bookmarks");

        tab1.setIndicator("Friends");
        tab1.setContent(this.lvFriends.getId());

        tab2.setIndicator("Bookmarks");
        tab2.setContent(this.lvBookmarks.getId());

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        //Logic
        List<FCharacter> friends = client.getOnlineFriends();
        List<FCharacter> bookmarks = client.getOnlineBookmarks();

        adapterFriends = new FCharacterListLargeAdapter(friends, getActivity());
        adapterBookmarks = new FCharacterListLargeAdapter(bookmarks, getActivity());

        this.lvFriends.setAdapter(adapterFriends);
        this.lvBookmarks.setAdapter(adapterBookmarks);


        this.lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        this.lvBookmarks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        this.lvFriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        this.lvBookmarks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        return this.root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = ((FChatMobileApplication)getActivity().getApplication()).getClient();
    }

    private void showBookmarkOptions(FCharacter c){

    }

    private void showFriendOptions(FCharacter c){

    }
}
