package abit.bean;

import abit.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import abit.util.StringUtil;


public class GroupBean {

    public static String PRIMARY_KEY = "gid";
    public static String TABLE       = "group_tbl";
    public String allHostView;
    public String groupName;
    protected String     name         = null;

    public String getName() {
        return name;
    }
    public void setName(String value) {
        name = value;
    }

    public String getKeyName() {
       return "gid";
    }

    protected String type=null;

    public String getAllHostView() {
        return allHostView;
    }

    public void setAllHostView(String value) {
        allHostView = value;
    }

    public void setGroupName (String value) {
        groupName = value;
    }

    public String getGroupName () {
        return groupName;
    }
    public void setType (String value) {
        type = value;
    }

    public String getType () {
        return type;
    }
    public int getTypeId() {
        if (type!=null)
                 if (type.equals("A")) return 0;
                 if (type.equals("O")) return 1;
                 if (type.equals("D")) return 2;
            else if (type.equals("I")) return 3;
        return 2;
    }
}