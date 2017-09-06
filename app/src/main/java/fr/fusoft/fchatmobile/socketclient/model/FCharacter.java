package fr.fusoft.fchatmobile.socketclient.model;

import fr.fusoft.fchatmobile.socketclient.model.commands.NLN;

/**
 * Created by Florent on 05/09/2017.
 */

public class FCharacter {

    String name;
    String gender;
    String status;

    public FCharacter(NLN token){
        this.name = token.getIdentity();
        this.gender = token.getStatus();
        this.status = token.getGender();
    }

    public String getName(){
        return this.name;
    }

    public String getFormattedName(){
        return this.name;
    }
}
