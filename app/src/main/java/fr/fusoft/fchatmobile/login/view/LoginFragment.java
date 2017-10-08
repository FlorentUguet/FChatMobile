package fr.fusoft.fchatmobile.login.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.login.model.LoginParams;
import fr.fusoft.fchatmobile.login.controller.LoginTask;
import fr.fusoft.fchatmobile.login.model.LoginTicket;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Florent on 05/09/2017.
 */

public class LoginFragment extends Fragment {

    View root;
    LoginTicket loginTicket;
    Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) this.root.findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        return this.root;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    public void Login()
    {
        EditText user = (EditText) this.root.findViewById(R.id.username);
        EditText pass = (EditText) this.root.findViewById(R.id.password);

        final TextView labelError = (TextView) this.root.findViewById(R.id.error);

        LoginParams p = new LoginParams(
                user.getText().toString().trim(),
                pass.getText().toString().trim());

        LoginTask.LoginTaskListener listener = new LoginTask.LoginTaskListener() {
            @Override
            public void onLoggedIn(LoginTicket ticket) {
                loginTicket = ticket;
                SelectCharacter(ticket);
            }

            @Override
            public void onError(String error) {
                //labelError.setVisibility(View.VISIBLE);
                //labelError.setText(error);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

                loginButton.setText(R.string.sign_in);
                loginButton.setEnabled(true);
            }
        };

        loginButton.setEnabled(false);
        loginButton.setText(R.string.signing_in);

        new LoginTask(listener).execute(p);
    }

    public void SelectCharacter(LoginTicket ticket){
        final List<String> characters = ticket.characters;
        int selected = ticket.defaultCharacterIndex;

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_select_character)
                .setSingleChoiceItems(characters.toArray(new CharSequence[characters.size()]), selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        loginTicket.selectedIndex = i;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onCharacterSelected(dialog, loginTicket.selectedIndex);
                    }
                });
        builder.show();
    }


    public void onCharacterSelected(DialogInterface dialog, int id){
        this.loginTicket.selectedCharacter = this.loginTicket.characters.get(id);
        dialog.dismiss();
        loginFinished();
    }

    public void loginFinished(){
        Intent intent = new Intent();
        intent.putExtra("ticket", loginTicket);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
}
