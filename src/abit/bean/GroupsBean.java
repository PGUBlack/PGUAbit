package abit.bean;

import java.util.ArrayList;

public class GroupsBean {

    protected Integer  kodGruppy = null;
    protected int      amount = 0;
    protected String   gruppa = null;
    protected String   shifrGruppy = null;
    protected Integer  kodFakulteta = null;
    protected Integer  nomerPotoka = null;
    protected Integer  kodSpetsialnosti = null;

    public void setGruppa (String value) {
        gruppa = value;
    }
    public String getGruppa () {
        return gruppa;
    }
    public void setShifrGruppy (String value) {
        shifrGruppy = value;
    }
    public String getShifrGruppy () {
        return shifrGruppy;
    }
    public void setKodGruppy (Integer value) {
        kodGruppy = value;
    }
    public Integer getKodGruppy () {
        return kodGruppy;
    }
    public void setAmount (int value) {
        amount = value;
    }
    public int getAmount () {
        return amount;
    }
    public void setKodFakulteta (Integer value){
        kodFakulteta = value;
    } 
    public Integer getKodFakulteta () {
        return kodFakulteta;
    }
    public void setNomerPotoka (Integer value){
        nomerPotoka = value;
    } 
    public Integer getNomerPotoka () {
        return nomerPotoka;
    }
    public void setKodSpetsialnosti (Integer value) {
        kodSpetsialnosti = value;
    }
    public Integer getKodSpetsialnosti () {
        return kodSpetsialnosti;
    }
}