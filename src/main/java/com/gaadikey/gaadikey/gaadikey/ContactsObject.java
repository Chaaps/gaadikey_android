package com.gaadikey.gaadikey.gaadikey;

/**
 * Created by madratgames on 09/09/14.
 */
public class ContactsObject {

    public String  Desc;
    public String  Name;
    public String  ImgUrl;
    public String  gkey;
    public String  phonenumber1;
    public String  phonenumber2;
    public String  phonenumber3;
    public String  phonenumber4;

    public void setDesc(String desc)
    {
         this.Desc = desc;
    }

    public void setImgUrl(String imgurl)
    {
        this.ImgUrl = imgurl;
    }

    public void setName(String name)
    {
        this.Name  = name;
    }

    public void setGkey(String gkey)
    {
        this.gkey = gkey;
    }

    public void setPhonenumber1(String p1)
    {
        this.phonenumber1 = p1;
    }

    public void setPhonenumber2(String p2)
    {
        this.phonenumber2 = p2;
    }

    public void setPhonenumber3(String p3)
    {
        this.phonenumber3 = p3;
    }

    public void setPhonenumber4(String p4)
    {
        this.phonenumber4 = p4;
    }


    public String getName()
    {
        return Name;
    }

    public String getImgUrl()
    {
        return ImgUrl;
    }

    public String getDesc()
    {
        return Desc;
    }

    public String getGkey()
    {
        return gkey;
    }

    public String getPhonenumber1()
    {
        return phonenumber1;
    }

    public String getPhonenumber2()
    {
        return phonenumber2;
    }

    public String getPhonenumber3()
    {
        return phonenumber3;
    }

    public String getPhonenumber4()
    {
        return phonenumber4;
    }
}
