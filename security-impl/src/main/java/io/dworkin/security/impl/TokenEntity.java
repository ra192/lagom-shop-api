package io.dworkin.security.impl;

import java.util.Date;

/**
 * Created by yakov on 22.03.2017.
 */
public class TokenEntity {
    private String username;
    private String token;
    private Date validTo;

    public TokenEntity(String username, String token, Date validTo) {
        this.username = username;
        this.token = token;
        this.validTo = validTo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
}
