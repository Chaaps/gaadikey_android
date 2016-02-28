package com.gaadikey.gaadikey.gaadikey.serializables;

import java.io.Serializable;

/**
 * Created by madratgames on 08/09/14.
 */
public class AccessTokenObject implements Serializable{

    private static final long serialVersionUID = 3188019306210610041L;
    private String access_token;
    private String token_type;
    private String expires_in;

    public String get_access_token() {
        return access_token;
    }

    public void set_access_token(String access_token) {
        this.access_token = access_token;
    }

    public String get_token_type() { return token_type; }

    public void set_token_type(String token_type)  { this.token_type = token_type; }

    public String get_expires_in() { return expires_in; }

    public void set_expires_in(String expires_in)  { this.expires_in = expires_in; }

    //AccessTokenObject
    // Get and Set done for AccessToken Object
}
