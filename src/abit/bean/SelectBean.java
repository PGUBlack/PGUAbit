package abit.bean;

import java.util.ArrayList;

public class SelectBean {

    protected String   kodFak = null;
    protected String   abbrFak = null;
    protected String   dataJekz = null;
    protected String   kodPredm = null;
    protected String   descr = null;
    protected String   nazv = null;
    protected Integer  uid = null;

    public void setKodFak (String value) {
        kodFak = value;
    }
    public String getKodFak () {
        return kodFak;
    }
    public void setAbbrFak (String value) {
        abbrFak = value;
    }
    public String getAbbrFak () {
        return abbrFak;
    }
    public void setDataJekz (String value){
        dataJekz = value;
    } 
    public String getDataJekz () {
        return dataJekz;
    }
    public void setKodPredm (String value) {
        kodPredm = value;
    }
    public String getKodPredm () {
        return kodPredm;
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

    public void setUid (Integer value){
        uid = value;
    } 
    public Integer getUid () {
        return uid;
    }

}