package io.dworkin.security.impl;

import java.time.Instant;

/**
 * Created by yakov on 22.03.2017.
 */
public class TokenEntity {
    private String username;
    private String token;
    private Instant validTo;

    public TokenEntity(String username, String token, Instant validTo) {
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

    public Instant getValidTo() {
        return validTo;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }
}
