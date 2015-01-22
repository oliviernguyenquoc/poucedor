package fr.poucedor.poucedor.provider;

/**
 * Created by loic on 22/01/15.
 */
public class LoginRequest {
    public String username;
    public String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
