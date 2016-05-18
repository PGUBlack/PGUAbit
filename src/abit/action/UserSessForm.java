package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.UserBean;
import abit.util.StringUtil;

public class UserSessForm extends ActionForm {

    private String action = null;
    private Integer idGruppa = null;
    private String otch = null;
    private String imja = null;
    private String special11 = null;
    private String special1 = null;
    private String special3 = null;
    private String descr = null;
    private Integer idStud = null;

//------------ GET methods -----------------

 public String getAction() {
        return action;
 }
 public Integer getIdGruppa() {
        return idGruppa;
 }	
 public String getOtch() {
        return otch;
 }
 public String getImja() {
        return imja;
 }
 public String getSpecial11() {
        return special11;
 }
 public String getSpecial1() {
        return special1;
 }	
 public String getSpecial3() {
        return special3;
 }	
 public String getDescr() {
        return descr;
 }	
 public  Integer  getIdStud(){
	 return idStud;
 }

//----------- SET methods --------------------

 public void setAction(String value){
        action = value.trim();
 }
 public void setIdGruppa(Integer value){
        idGruppa = value;
 }
 public void setOtch(String value){
        otch = value.trim();
 }
 public void setImja(String value){
        imja = value.trim();
 }
 public void setSpecial11(String value){
        special11 = value.trim();
 }
 public void setSpecial1(String value){
        special1 = value.trim();
 }
 public void setSpecial3(String value){
        special3 = value.trim();
 }
 public void setDescr(String value){
        descr = value.trim();
 }
 public void setIdStud(Integer value){
        idStud = value;
 }

//---------- other methods ------------------

 public void reset(ActionMapping mapping, HttpServletRequest request ){
       String action = null;
       Integer idGruppa = null;
       String otch = null;
       String imja = null;
       String special11 = null;
       String special1 = null;
       String special3 = null;
       String descr = null;
       Integer idStud = null;
  }

 public void setBean(UserBean bean, HttpServletRequest request, ActionErrors errors)
                   throws ServletException {
        if (bean!=null) {

            idStud = bean.getIdStud();
            idGruppa = bean.getIdGruppa();

            if(bean.getAction() != null) action = bean.getAction();
            if(bean.getOtch() != null) otch = bean.getOtch();
            if(bean.getImja() != null) imja = bean.getImja();
            if(bean.getSpecial11() != null) special11 = bean.getSpecial11();
            if(bean.getSpecial1() != null) special1 = bean.getSpecial1();
            if(bean.getSpecial3() != null) special3 = bean.getSpecial3();
            if(bean.getDescr() != null) descr = bean.getDescr();
        }
 }

 public UserBean getBean(HttpServletRequest request, ActionErrors errors)
                          throws ServletException {

       UserBean bean = new UserBean();

       bean.setIdStud(idStud);

       bean.setIdGruppa(idGruppa);

       if(action != null && !action.equals("")) bean.setAction(StringUtil.toDB(action));
    
       if(otch != null && !otch.equals("")) bean.setOtch(StringUtil.toDB(otch));

       if(imja != null && !imja.equals("")) bean.setImja(StringUtil.toDB(imja));

       if(special11 != null && !special11.equals("")) bean.setSpecial11(StringUtil.toDB(special11));

       if(special1 != null && !special1.equals("")) bean.setSpecial1(StringUtil.toDB(special1));

       if(special3 != null && !special3.equals("")) bean.setSpecial3(StringUtil.toDB(special3));

       if(descr != null && !descr.equals("")) bean.setDescr(StringUtil.toDB(descr));

       return bean;
    }
}