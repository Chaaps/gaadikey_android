package com.gaadikey.gaadikey.gaadikey.serializables;

import java.io.Serializable;

/**
 * Created by madratgames on 22/08/14.
 */
public class PhoneObject implements Serializable {


    private static final long serialVersionUID = -3748828716166427093L;
    private String deviceid;
    private String phonenumber;
    private String email;

    public String get_deviceid() {
        return deviceid;
    }

    public void set_deviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String get_phonenumber() {
        return phonenumber;
    }

    public void set_phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String get_email() {
        return email;
    }

    public void set_email(String email) {
        this.email = email;
    }

}
