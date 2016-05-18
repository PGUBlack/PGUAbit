package abit.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import abit.util.StringUtil;

public class LichAdjustBean {

    public String getKeyName() {
       return "uid";
    }

    protected String     pass = null;
    protected String     change_Pass = null;
    protected String     resolution = null;
    protected String     descr = null;
    protected String     action = null;
    protected String     folder = null;
    protected String     nazv = null;
    protected Integer    uid = null;
    protected Integer    idTema = null;
    protected GroupBean  group = null;

    public void setAction (String value) {
        action = value;
    }
    public String getAction () {
        return action;
    }

    public void setPass (String value) {
        pass = value;
    }
    public String getPass () {
        return pass;
    }

    public void setFolder (String value) {
        folder = value;
    }
    public String getFolder () {
        return folder;
    }

    public void setNazv (String value) {
        nazv = value;
    }
    public String getNazv () {
        return nazv;
    }

    public void setDescr (String value) {
        descr = value;
    }
    public String getDescr () {
        return descr;
    }

    public void setChange_Pass (String value) {
        change_Pass = value;
    }
    public String getChange_Pass () {
        return change_Pass;
    }

    public void setResolution (String value){
        resolution = value;
    } 
    public String getResolution () {
        return resolution;
    }
    public void setUid (Integer value){
        uid = value;
    } 
    public Integer getUid () {
        return uid;
    }
    public void setIdTema (Integer value){
        idTema = value;
    } 
    public Integer getIdTema () {
        return idTema;
    }
    public GroupBean getGroup() {
        return group;
    }
    public void setGroup(GroupBean value) {
        group = value;
    }
}