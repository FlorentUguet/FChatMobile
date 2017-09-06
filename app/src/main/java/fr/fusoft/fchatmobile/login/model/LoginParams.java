package fr.fusoft.fchatmobile.login.model;

/**
 * Created by Florent on 05/09/2017.
 */

public class LoginParams {
    public String User;
    public String Pass;

    public LoginParams(String User, String Pass)
    {
        this.User = User.toLowerCase();
        this.Pass = Pass;
    }
}
