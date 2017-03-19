package io.dworkin.model;

/**
 * Created by yakov on 19.03.2017.
 */
public class UserEntity {
    private String username;
    private String password;

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
