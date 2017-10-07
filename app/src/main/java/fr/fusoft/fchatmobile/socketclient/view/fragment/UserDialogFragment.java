package fr.fusoft.fchatmobile.socketclient.view.fragment;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.FChatMobileApplication;
import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.model.ProfileData;
import fr.fusoft.fchatmobile.socketclient.view.adapter.FProfileDataAdapter;
import fr.fusoft.fchatmobile.utils.network.DownloadImageTask;

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
    private Button buttonClose;
    private Button buttonDM;

    public static UserDialogFragment newInstance(String character) {
        UserDialogFragment f = new UserDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("character", character);
        f.setArguments(args);

        return f;
    }

    public interface UserProfileDialogListener{
        void onClose();
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
        this.buttonClose = (Button) this.root.findViewById(R.id.buttonClose);
        this.buttonDM = (Button) this.root.findViewById(R.id.buttonDM);

        Picasso.with(getActivity()).load(this.character.getAvatarUrl()).into(this.avatar);

        this.username.setText(this.character.getName());
        this.username.setTextColor(ContextCompat.getColor(getActivity(), this.character.getGender().getColor()));

        this.adapter = new FProfileDataAdapter(new ArrayList<ProfileData>(), getActivity());
        this.lvProfileData.setAdapter(adapter);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onClose();
            }
        });

        buttonDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onDM(characterName);
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
