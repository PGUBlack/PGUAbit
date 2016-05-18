package abit.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Locale;
import java.util.ArrayList;
import java.util.Enumeration;
import org.apache.struts.action.*;

import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.sql.*;



public class PrioritetAction extends Action {
	
	public ActionForward perform ( ActionMapping        mapping,
            ActionForm           actionForm,
            HttpServletRequest   request,
            HttpServletResponse  response )
	
	
	throws IOException, ServletException
    {   
        HttpSession                  session     = request.getSession();
        Connection                   conn        = null;
        PreparedStatement            stmt        = null;
        ResultSet                    rs          = null;
        ActionErrors                 errors      = new ActionErrors();
        ActionError                  msg         = null;
        EkzamenyNaSpetsialnostiForm  form        = (EkzamenyNaSpetsialnostiForm) actionForm;
        AbiturientBean               abit_ENS    = form.getBean(request, errors);
        boolean                      error       = false;
        ActionForward                f           = null;
        ArrayList                    abit_ENS_S1 = new ArrayList();
        ArrayList                    abit_ENS_S3 = new ArrayList();
        ArrayList                    abit_ENS_S5 = new ArrayList();
        UserBean                     user        = (UserBean)session.getAttribute("user");
        boolean                      priznak        = true;
        int                          number         = 0;
        int                          old_kodAb      = 0;
        String                       predmets       = new String();
        String                       codeLine       = new String();
        String                       specialQ       = new String();
        String                       kPredmeta      = new String();
        String                       ns      = new String();
        String                       oldKodeS       = new String();
        String                       oldKodeF       = new String();
        String                       oldNazv        = new String();
        String                       kSpec       = new String();


        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "prioritetAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "prioritetForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");

/********* Подготовка данных для ввода с помощью селекторов ***********/


          stmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta,ShifrFakulteta,KodFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
             AbiturientBean abit_TMP = new AbiturientBean();
             abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
             abit_TMP.setShifrFakulteta(rs.getString(2));
             abit_TMP.setKodFakulteta(new Integer(rs.getInt(3)));
             abit_ENS_S1.add(abit_TMP);
          }
/************************************************************************************************/

          if (form.getAction() == null) form.setAction(us.getClientIntName("translator","init"));

/************************************** Создание записи *****************************************/

          if ( form.getAction().equals("mod_del")) {

        	 abit_ENS.setKodSpetsialnosti(new Integer((abit_ENS.getSpecial1()).substring((abit_ENS.getSpecial1()).indexOf("$")+1,(abit_ENS.getSpecial1()).indexOf("%"))));
             kSpec = abit_ENS.getKodSpetsialnosti().toString();


             Enumeration paramNames = request.getParameterNames();
             while(paramNames.hasMoreElements()){
            	 String paramName = (String)paramNames.nextElement();
            	 String paramValue[] = request.getParameterValues(paramName);
            	 if(paramName.indexOf("Prioritet") != -1 && paramValue[0].length() != 0) {         	
                     stmt = conn.prepareStatement("UPDATE EkzamenyNaSpetsialnosti SET Prioritet=? WHERE KodSpetsialnosti LIKE '"+kSpec+"' AND KodPredmeta LIKE '"+(paramName.substring(9))+"'");
                     stmt.setObject(1,new Integer(paramValue[0]),Types.INTEGER);            // Оценка
                     stmt.executeUpdate();
                   }
             }
          }
          
          if (form.getAction().equals("translator")) {
        	  form.setAction(us.getClientIntName("translate","view-form"));
        	  
              priznak = true;
              kPredmeta = new String();
              String oldKode = new String();
              String oldKodFak = new String();
              String oldAbbr = new String();
              stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,EkzamenyNaSpetsialnosti.KodPredmeta,Abbreviatura,AbbreviaturaFakulteta,Spetsialnosti.KodFakulteta FROM Spetsialnosti,EkzamenyNaSpetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND KodVuza LIKE ? ORDER BY Abbreviatura,EkzamenyNaSpetsialnosti.KodPredmeta,AbbreviaturaFakulteta ASC");
              stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
              rs = stmt.executeQuery();
              while (rs.next()) {
                AbiturientBean abit_TMP = new AbiturientBean();
                abit_TMP.setSpecial1(rs.getString(1));
                if(priznak) { oldKode = rs.getString(1); oldKodFak = rs.getString(5); oldAbbr = rs.getString(3); priznak = false; }
                if(!oldKode.equals(rs.getString(1))) {
                  if(kPredmeta.equals("")) kPredmeta += "%" + rs.getString(2);
                    abit_TMP.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
                    abit_TMP.setAbbreviatura(oldAbbr);
                    abit_ENS_S3.add(abit_TMP);
                    priznak = true;
                    kPredmeta = "";
                }
                kPredmeta += "%" + rs.getString(2);
              }
              AbiturientBean abit_TMP2 = new AbiturientBean();
              abit_TMP2.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
              abit_TMP2.setAbbreviatura(oldAbbr);
              abit_ENS_S3.add(abit_TMP2);
          }
          
          if(form.getAction().equals("md_dl")){
        	  
        	  abit_ENS.setKodSpetsialnosti(new Integer((abit_ENS.getSpecial1()).substring((abit_ENS.getSpecial1()).indexOf("$")+1,(abit_ENS.getSpecial1()).indexOf("%"))));
        	  stmt = conn.prepareStatement("SELECT NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? ");
              stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
              rs = stmt.executeQuery();
              if(rs.next()) abit_ENS.setAbbreviatura(rs.getString(1).toUpperCase());
              
        	  stmt = conn.prepareStatement("SELECT DISTINCT ens.KodPredmeta, np.Predmet,ens.Prioritet FROM Spetsialnosti s,EkzamenyNaSpetsialnosti ens,Fakultety f, NazvanijaPredmetov np WHERE f.KodFakulteta = s.KodFakulteta AND s.KodSpetsialnosti = ens.KodSpetsialnosti AND ens.KodPredmeta=np.KodPredmeta AND s.KodSpetsialnosti LIKE ? AND f.KodVuza LIKE ?");
              stmt.setObject(1, abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
              stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
              rs = stmt.executeQuery();
              while (rs.next()) {
              	AbiturientBean abit_TMP= new AbiturientBean();
              	abit_TMP.setKodPredmeta(new Integer(rs.getString(1)));
              	abit_TMP.setPredmet(rs.getString(2));            	
              	abit_TMP.setPrioritet(rs.getString(3));
              	abit_ENS_S5.add(abit_TMP);
              }
        	  
        	  
        	  
          }

      } catch ( SQLException e ) {
          request.setAttribute("SQLException", e);
          return mapping.findForward("error");
        }
        catch ( java.lang.Exception e ) {
          request.setAttribute("JAVAexception", e);
          return mapping.findForward("error");
        }
        finally {
          if ( rs != null ) {
               try {
                     rs.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( stmt != null ) {
               try {
                     stmt.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( conn != null ) {
               try {
                     conn.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
        }
        request.setAttribute("abit_ENS", abit_ENS);
        request.setAttribute("abit_ENS_S1", abit_ENS_S1);
        request.setAttribute("abit_ENS_S3", abit_ENS_S3);
        request.setAttribute("abit_ENS_S5", abit_ENS_S5);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}
