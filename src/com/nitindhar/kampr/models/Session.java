package com.nitindhar.kampr.models;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

public class Session {

    private final String username;
    private final String password;
    private final String token;
    private final Integer userId;

    public Session(String username, String password, String token,
            Integer userId) {
        Preconditions.checkArgument(!StringUtils.isBlank(username));
        Preconditions.checkArgument(!StringUtils.isBlank(password));
        Preconditions.checkArgument(!StringUtils.isBlank(token));

        this.username = username;
        this.password = password;
        this.token = token;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

}