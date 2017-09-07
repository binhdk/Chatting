package com.vanbinh.chatting.apiconnect.jsoninput;

import java.io.Serializable;

/**
 * Created by vanbinh on 8/18/2017.
 *
 */

public class DeviceTokenInput implements Serializable {
    private String token;


    public DeviceTokenInput(String token) {
        this.token = token;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
