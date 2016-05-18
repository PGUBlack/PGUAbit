package abit.bean;

import java.util.ArrayList;

public class HistogrammBean {

    protected String     ñol     = null;
    protected String     row     = null;
    protected ArrayList  cells   = null;
    protected String     color   = null;
    protected String     height  = null;
    protected String     nazv    = null;
    protected Integer    uid     = null;

    public void setCol (String value) {
        ñol = value;
    }
    public String getCol () {
        return ñol;
    }
    public void setRow (String value) {
        row = value;
    }
    public String getRow () {
        return row;
    }
    public void setCells (ArrayList value){
        cells = value;
    } 
    public ArrayList getCells () {
        return cells;
    }
    public void setColor (String value) {
        color = value;
    }
    public String getColor () {
        return color;
    }

    public void setNazv (String value) {
        nazv = value;
    }
    public String getNazv () {
        return nazv;
    }

    public void setHeight (String value) {
        height = value;
    }
    public String getHeight () {
        return height;
    }

    public void setUid (Integer value){
        uid = value;
    } 
    public Integer getUid () {
        return uid;
    }

}