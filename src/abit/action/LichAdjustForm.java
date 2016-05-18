package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.LichAdjustBean;
import abit.util.StringUtil;

public class LichAdjustForm extends ActionForm {

    private String  action = null;
    private Integer uid = null;
    private String  pass = null;
    private String  change_Pass = null;
    private String  resolution = null;
    private String  folder = null;
    private String  nazv = null;
    private String  descr = null;
    private Integer idTema = null;

//------------ GET methods -----------------

 public String getAction() {
        return action;
 }
 public Integer getUid() {
        return uid;
 }	
 public String getPass() {
        return pass;
 }
 public String getChange_Pass() {
        return change_Pass;
 }
 public String getResolution() {
        return resolution;
 }
 public String getFolder() {
        return folder;
 }	
 public String getNazv() {
        return nazv;
 }	
 public String getDescr() {
        return descr;
 }	
 public  Integer  getIdTema(){
	 return idTema;
 }

//----------- SET methods --------------------

 public void setAction(String value){
        action = value.trim();
 }
 public void setUid(Integer value){
        uid = value;
 }
 public void setPass(String value){
        pass = value.trim();
 }
 public void setChange_Pass(String value){
        change_Pass = value.trim();
 }
 public void setResolution(String value){
        resolution = value.trim();
 }
 public void setFolder(String value){
        folder = value.trim();
 }
 public void setNazv(String value){
        nazv = value.trim();
 }
 public void setDescr(String value){
        descr = value.trim();
 }
 public void setIdTema(Integer value){
        idTema = value;
 }

//---------- other methods ------------------

 public void reset(ActionMapping mapping, HttpServletRequest request ){
       String  action    = null;
       Integer uid  = null;
       String  pass      = null;
       String  change_Pass      = null;
       String  resolution = null;
       String  folder  = null;
       String  nazv  = null;
       String  descr     = null;
       Integer idTema    = null;
  }

 public void setBean(LichAdjustBean bean, HttpServletRequest request, ActionErrors errors)
                   throws ServletException {
        if (bean!=null) {
            if(bean.getAction() != null) action = bean.getAction();
            uid = bean.getUid();
            if(bean.getPass() != null) pass = bean.getPass();
            if(bean.getChange_Pass() != null) change_Pass = bean.getChange_Pass();
            if(bean.getResolution() != null) resolution = bean.getResolution();
            if(bean.getFolder() != null) folder = bean.getFolder();
            if(bean.getNazv() != null) nazv = bean.getNazv();
            if(bean.getDescr() != null) descr = bean.getDescr();
            idTema = bean.getIdTema();
        }
 }

 public LichAdjustBean getBean(HttpServletRequest request, ActionErrors errors)
                          throws ServletException {
       LichAdjustBean bean = new LichAdjustBean();

       if(action != null && !action.equals("")) bean.setAction(StringUtil.toDB(action));

       bean.setUid(uid);
    
       if(pass != null && !pass.equals("")) bean.setPass(StringUtil.toDB(pass));

       if(change_Pass != null && !change_Pass.equals("")) bean.setChange_Pass(StringUtil.toDB(change_Pass));

       if(resolution != null && !resolution.equals("")) bean.setResolution(StringUtil.toDB(resolution));

       if(folder != null && !folder.equals("")) bean.setFolder(StringUtil.toDB(folder));

       if(nazv != null && !nazv.equals("")) bean.setNazv(StringUtil.toDB(nazv));

       if(descr != null && !descr.equals("")) bean.setDescr(StringUtil.toDB(descr));

       bean.setIdTema(idTema);

       return bean;
    }
}
