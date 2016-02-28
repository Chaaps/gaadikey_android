package com.gaadikey.gaadikey.gaadikey.serializables;

import java.io.Serializable;

/**
 * Created by madratgames on 27/08/14.
 */
public class ViewNotifyObject implements Serializable {

    private static final long serialVersionUID = -7755655238356066843L;
    public String phonenumber;
    public String gkey;
    public String name;
    public String vehicle;
    public String sendto;

    public String get_phonenumber() {
        return phonenumber;
    }

    public void set_phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String get_gkey() {
        return gkey;
    }

    public void set_gkey(String gkey) {
        this.gkey = gkey;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_vehicle() { return vehicle; }

    public void set_vehicle(String vehicle) { this.vehicle = vehicle; }

    public String get_sendto()  { return sendto; }

    public void set_sendto(String sendto) { this.sendto = sendto; }

}
