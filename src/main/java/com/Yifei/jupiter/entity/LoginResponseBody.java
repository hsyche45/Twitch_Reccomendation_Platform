package com.Yifei.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseBody {
    // 双向的 可以Json转换java 也可以java转换Json
    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("name")
    private final String name;

    public LoginResponseBody(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
