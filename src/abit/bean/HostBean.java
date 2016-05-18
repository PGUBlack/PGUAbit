package abit.bean;

public class HostBean {

    String addr;

    protected String     name         = null;

    //------------------------------------------
    //   PUBLIC methods:
    //------------------------------------------


    public String getName() {
        return name;
    }
    public void setName(String value) {
        name = value;
    }

    public HostBean() {
        addr = null;
    }

    public void setAddr (String value) {
        addr = value;
    }

    public String getAddr () {
        return addr;
    }

    public String getKeyName() {
       return "hid";
    }

}