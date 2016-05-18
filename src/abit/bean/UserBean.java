package abit.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import abit.util.StringUtil;

public class UserBean {

    public String getKeyName() {
       return "uid";
    }

    protected String     action       = null;
    protected String     pass         = null;
    protected String     status       = null;
    protected String     gruppa       = null;
    protected String     special1     = null;
    protected String     special3     = null;
    protected String     special11    = null;
    protected String     imja         = null;
    protected String     dataLogin    = null;
    protected String     timeLogin    = null;
    protected String     dataLogout   = null;
    protected String     timeLogout   = null;
    protected String     totalTime    = null;
    protected String     totalTimeCur = null;
    protected String     otch         = null;
    protected String     descr        = null;
    protected String     change_Pass  = null;
    protected String     uip          = null;
    protected String     kolLoginCur  = null;
    protected String     kolLogin     = null;
    protected String     kolZaprCur   = null;
    protected String     kolZapr      = null;
    protected Integer    idGruppa     = null;
    protected Integer    idUser       = null;
    protected Integer    idStud       = null;
    protected Integer    uid          = null;
    protected long       sid          = 0;
    protected String     idTema       = null;
    protected Integer    idStat       = null;
    protected GroupBean  group        = null;
    protected String     name         = null;

    public String getName() {
        return name;
    }
    public void setName(String value) {
        name = value;
    }

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

    public void setSpecial1 (String value) {
        special1 = value;
    }
    public String getSpecial1 () {
        return special1;
    }

    public void setStatus (String value) {
        status = value;
    }
    public String getStatus () {
        return status;
    }

    public void setGruppa (String value) {
        gruppa = value;
    }
    public String getGruppa () {
        return gruppa;
    }

    public void setSpecial3 (String value) {
        special3 = value;
    }
    public String getSpecial3 () {
        return special3;
    }

    public void setSpecial11 (String value) {
        special11 = value;
    }
    public String getSpecial11 () {
        return special11;
    }

    public void setDataLogin (String value) {
        dataLogin = value;
    }
    public String getDataLogin () {
        return dataLogin;
    }

    public void setTimeLogin (String value) {
        timeLogin = value;
    }
    public String getTimeLogin () {
        return timeLogin;
    }

    public void setDataLogout (String value) {
        dataLogout = value;
    }
    public String getDataLogout () {
        return dataLogout;
    }

    public void setTimeLogout (String value) {
        timeLogout = value;
    }
    public String getTimeLogout () {
        return timeLogout;
    }

    public void setImja (String value) {
        imja = value;
    }
    public String getImja () {
        return imja;
    }

    public void setTotalTime (String value) {
        totalTime = value;
    }
    public String getTotalTime () {
        return totalTime;
    }

    public void setTotalTimeCur (String value) {
        totalTimeCur = value;
    }
    public String getTotalTimeCur () {
        return totalTimeCur;
    }

    public void setOtch (String value) {
        otch = value;
    }
    public String getOtch () {
        return otch;
    }

    public String getDescr () {
        return descr;
    }
    public void setDescr (String value) {
        descr = value;
    }

    public void setChange_Pass (String value) {
        change_Pass = value;
    }
    public String getChange_Pass () {
        return change_Pass;
    }

    public void setUip (String value){
        uip = value;
    } 
    public String getUip () {
        return uip;
    }

    public void setKolLoginCur (String value){
        kolLoginCur = value;
    } 
    public String getKolLoginCur () {
        return kolLoginCur;
    }

    public void setKolLogin (String value){
        kolLogin = value;
    } 
    public String getKolLogin () {
        return kolLogin;
    }

    public void setKolZaprCur (String value){
        kolZaprCur = value;
    } 
    public String getKolZaprCur () {
        return kolZaprCur;
    }

    public void setKolZapr (String value){
        kolZapr = value;
    } 
    public String getKolZapr () {
        return kolZapr;
    }

    public void setUid (Integer value){
        uid = value;
    } 
    public Integer getUid () {
        return uid;
    }

    public void setIdGruppa (Integer value){
        idGruppa = value;
    } 
    public Integer getIdGruppa () {
        return idGruppa;
    }

    public void setIdUser (Integer value){
        idUser = value;
    } 
    public Integer getIdUser () {
        return idUser;
    }

    public void setIdStat (Integer value){
        idStat = value;
    } 
    public Integer getIdStat () {
        return idStat;
    }

    public void setIdStud (Integer value){
        idStud = value;
    } 
    public Integer getIdStud () {
        return idStud;
    }
    public void setSid (long value){
        sid = value;
    } 
    public long getSid () {
        return sid;
    }
    public void setIdTema (String value){
        idTema = value;
    } 
    public String getIdTema () {
        return idTema;
    }
    public GroupBean getGroup() {
        return group;
    }
    public void setGroup(GroupBean value) {
        group = value;
    }
}