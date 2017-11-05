package fr.fusoft.fchatmobile.socketclient.view.fragment.dialogs;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.ProfileData;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FProfileDataAdapter;

/**
 * Created by Florent on 07/10/2017.
 */

public class UserDialogFragment extends DialogFragment {

    private ListView lvProfileData;
    private FProfileDataAdapter adapter;
    private String characterName;
    private FCharacter character;
    private View root;
    private ImageView avatar;
    private TextView username;
    private Button buttonDM;
    private TextView description;
    private TextView status;
    private TabHost tabHost;
    private Button buttonFriend;
    private Button buttonBookmark;

    public static UserDialogFragment newInstance(String character) {
        UserDialogFragment f = new UserDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("character", character);
        f.setArguments(args);

        return f;
    }

    public interface UserProfileDialogListener{
        void onBookmark(String character);
        void onFriend(String character);
        void onDM(String character);
    }

    private UserProfileDialogListener mListener = null;

    public void setListener(UserProfileDialogListener l){
        this.mListener = l;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        this.lvProfileData = (ListView) this.root.findViewById(R.id.lvProfileData);
        this.username = (TextView) this.root.findViewById(R.id.username);
        this.avatar = (ImageView) this.root.findViewById(R.id.avatar);
        this.buttonDM = (Button) this.root.findViewById(R.id.buttonDM);
        this.buttonFriend = (Button) this.root.findViewById(R.id.buttonFriend);
        this.buttonBookmark = (Button) this.root.findViewById(R.id.buttonBookmark);
        this.description = (TextView) this.root.findViewById(R.id.description);
        this.status = (TextView) this.root.findViewById(R.id.textViewStatus);
        this.tabHost = (TabHost) this.root.findViewById(R.id.tabHost);

        //Tab Host
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Profile");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Status");

        tab1.setIndicator("Profile");
        tab1.setContent(this.lvProfileData.getId());

        tab2.setIndicator("Status");
        tab2.setContent(this.status.getId());

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        //Avatar
        Picasso.with(getActivity()).load(this.character.getAvatarUrl()).into(this.avatar);

        //Username
        this.username.setText(this.character.getName());
        this.username.setTextColor(ContextCompat.getColor(getActivity(), this.character.getGender().getColor()));

        //Description
        this.description.setText(String.format("%s %s (%s)", this.character.getProfileData("Species"), this.character.getProfileData("Gender"), this.character.getStatus().getLabel()));

        //Profile Data
        this.adapter = new FProfileDataAdapter(new ArrayList<ProfileData>(), getActivity());
        this.lvProfileData.setAdapter(adapter);

        //Status
        this.status.setText(this.character.getStatusMessage());


        buttonDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onDM(characterName);
            }
        });

        buttonBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onBookmark(characterName);
            }
        });

        buttonFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onFriend(characterName);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(character.getProfile());
            }
        });

        return this.root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.characterName = getArguments().getString("character");
        this.character = ((FChatMobileApplication)getActivity().getApplication()).getClient().getCharacter(this.characterName);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            this.characterName = savedInstanceState.getString("character");
        }
    }

    public void addProfileData(final ProfileData data){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(data);
            }
        });

    }
}
