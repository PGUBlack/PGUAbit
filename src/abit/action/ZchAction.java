package abit.action;

import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import javax.naming.*;
import javax.sql.*;

import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*; 

public class ZchAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        PreparedStatement stmt1         = null;
        ResultSet         rs            = null;
        ResultSet         rs1           = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        ZchForm           form          = (ZchForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        boolean           error         = false;
        ActionForward     f             = null;
        String            kRajona       = null;
        String            kOblasti      = null;
        Integer           kAbiturienta  = null;
        String           kodab         = null;
        ArrayList         abits_A       = new ArrayList();
        ArrayList         abit_A_S1     = new ArrayList();
        ArrayList         abit_A_S2     = new ArrayList();
        ArrayList         abit_A_S3     = new ArrayList();
        ArrayList         abit_A_S4     = new ArrayList();
        ArrayList         abit_A_S5     = new ArrayList();
        int               count         = 0;
        UserBean          user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "zchAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "zchForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/*********  Специальности  **********/

            stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,Abbreviatura FROM Spetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial3(rs.getString(1));
              abit_TMP.setAbbreviatura(rs.getString(2));
              abit_A_S5.add(abit_TMP);
            } 

/********************** Подготовка данных для ввода с помощью селекторов ************************/
          if(form.getAction() == null ) {

            session.removeAttribute("kod_gr");
            session.removeAttribute("doc_hran");
            session.removeAttribute("kod_fak");
            session.removeAttribute("kod_spec");

// ****************** Факультеты **********************

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setShifrFakulteta(rs.getString(1));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_A_S2.add(abit_TMP);
                 }

// ****************** специальности на факультете ****************************
            boolean save = false;
            String oldKodFak = "";
            String kodeline = "";
            stmt = conn.prepareStatement("SELECT DISTINCT s.KodFakulteta,s.KodSpetsialnosti,s.Abbreviatura FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND KodVuza LIKE ? ORDER BY s.KodFakulteta,s.Abbreviatura ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
             if(!rs.getString(1).equals(oldKodFak)) {
               if(save) {
                 AbiturientBean tmp1 = new AbiturientBean();
                 tmp1.setKodFakulteta(new Integer(oldKodFak));
                 tmp1.setSpecial1(kodeline);
// KodFakulteta хранит Код факультета, к которому относятся специальности
// Special1 имеет формат списка: KodFakulteta%Spec1%KodSpec1%Spec2%KodSpec2%Spec3%KodSpec3...
                 abit_A_S3.add(tmp1);
               }
               oldKodFak = rs.getString(1);
               kodeline = rs.getString(3)+"%"+rs.getString(2);
               save = true;
             } else kodeline += "%"+rs.getString(3)+"%"+rs.getString(2);
            }
// Запись последней строки в массив кодов и специальностей
            AbiturientBean tmp2 = new AbiturientBean();
            tmp2.setKodFakulteta(new Integer(oldKodFak));
            tmp2.setSpecial1(kodeline);
            abit_A_S3.add(tmp2);

// ****************** группы на факультете ****************************
            save = false;
            oldKodFak = "";
            kodeline = "";
            stmt = conn.prepareStatement("SELECT DISTINCT g.KodFakulteta,g.KodGruppy,g.Gruppa FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND KodVuza LIKE ? ORDER BY g.KodFakulteta,g.Gruppa ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
             if(!rs.getString(1).equals(oldKodFak)) {
               if(save) {
                 AbiturientBean tmp = new AbiturientBean();
                 tmp.setKodGruppy(new Integer(oldKodFak));
                 tmp.setSpecial4(kodeline);
// KodGruppy хранит Код Факультета, к которому относятся группы
// Special4 имеет формат списка: KodFakulteta%Gruppa1%KodGruppy1%Gruppa2%KodGruppy2%Gruppa3%KodGruppy3...
                 abit_A_S1.add(tmp);
               }
               oldKodFak = rs.getString(1);
               kodeline = rs.getString(3)+"%"+rs.getString(2);
               save = true;
             } else kodeline += "%"+rs.getString(3)+"%"+rs.getString(2);
            }
// Запись последней строки в массив кодов и групп
            AbiturientBean tmp = new AbiturientBean();
            tmp.setKodGruppy(new Integer(oldKodFak));
            tmp.setSpecial4(kodeline);
            abit_A_S1.add(tmp);

            form.setAction(us.getClientIntName("menu","init"));


//***************************************************************
//****************** ОБНОВЛЕНИЕ ДАННЫХ В БД *********************
//***************************************************************

          } if( form.getAction().equals("updatebase")) {
        	  		String pr= new String();
        	     int row_id,col_id;
        	     String row ="",col="";
        	     Enumeration paramNames = request.getParameterNames();
        	     while(paramNames.hasMoreElements()) {
        	        String paramName = (String)paramNames.nextElement();
        	        String paramValue[] = request.getParameterValues(paramName);
        	        if(paramName.indexOf("cells1") != -1) {
        	          row = new String(paramName.substring(paramName.indexOf("%")+1,paramName.lastIndexOf("%")));
        	          col = new String(paramName.substring(paramName.lastIndexOf("%")+1,paramName.indexOf("|")));
        	        
        	          if(row.equals("0")){        	        	
        	        	stmt = conn.prepareStatement("UPDATE Abiturient SET Prinjat=? WHERE KodAbiturienta LIKE ?");
      	                stmt.setObject(1,StringUtil.toDB(paramValue[0]),Types.VARCHAR);
        	            stmt.setObject(2,col,Types.INTEGER);
        	            stmt.executeUpdate();   
        	            pr=StringUtil.toDB(paramValue[0]);
        	                    	          }
        	          if(row.equals("1")){
        	            stmt = conn.prepareStatement("SELECT KodSpetsialnosti FROM Spetsialnosti WHERE Abbreviatura LIKE ?");
        	            stmt.setObject(1,StringUtil.toDB((paramValue[0]+"")),Types.VARCHAR);
        	            rs = stmt.executeQuery();
        	            if(rs.next()){
        	              stmt = conn.prepareStatement("UPDATE Abiturient SET KodSpetsialnZach=? WHERE KodAbiturienta LIKE ?");
        	              stmt.setObject(1,rs.getString(1),Types.INTEGER);
        	              stmt.setObject(2,col,Types.INTEGER);
        	              stmt.executeUpdate();
        	              if(pr!=null && !pr.equals("н")){
        	              stmt = conn.prepareStatement("UPDATE Konkurs SET Zach = 'д' WHERE KodAbiturienta LIKE ? and   kodspetsialnosti like  ? ");
        	              stmt.setObject(1,col,Types.INTEGER);
        	              System.out.println(rs.getString(1));
          	              stmt.setObject(2,rs.getString(1),Types.INTEGER);        
          	              stmt.executeUpdate();        	 
        	              stmt = conn.prepareStatement("update Konkurs set Zach='в' where zach is null and kodabiturienta in (select kodabiturienta  from konkurs where zach like 'д')");
        	              //stmt.setObject(1,col,Types.INTEGER);
        	              stmt.executeUpdate();
        	              pr=null;
        	              }else{
        	            	  stmt = conn.prepareStatement("UPDATE Konkurs SET Zach = NULL WHERE KodAbiturienta LIKE ?");
            	              stmt.setObject(1,col,Types.INTEGER);    	            
              	              stmt.executeUpdate();        	 
        	              }
        	            } else {


// сброс специальности зачисления
               stmt = conn.prepareStatement("UPDATE Abiturient SET KodSpetsialnZach=NULL WHERE KodAbiturienta LIKE ?");
               stmt.setObject(1,col,Types.INTEGER);
               stmt.executeUpdate();
               stmt = conn.prepareStatement("UPDATE Konkurs SET Zach = NULL WHERE KodAbiturienta LIKE ?");
	              stmt.setObject(1,col,Types.INTEGER);    	            
	              stmt.executeUpdate(); 
            }
         }
      }
    }

    form.setAction(us.getClientIntName("show","act-save"));
 }


/************************************************************************************************/

 if( form.getAction().equals("show")) {

// Сохранение параметров отбора для воспроизведения результатов на экране после их модификации
   if(session.getAttribute("kod_gr")==null)
     session.setAttribute("kod_gr",abit_A.getKodGruppy());
   if(session.getAttribute("doc_hran")==null)
     session.setAttribute("doc_hran",StringUtil.toMySQL((abit_A.getDokumentyHranjatsja()+"")));
   if(session.getAttribute("kod_fak")==null)
     session.setAttribute("kod_fak",StringUtil.toMySQL((abit_A.getSpecial2()+"")));
   if(session.getAttribute("kod_spec")==null)
     session.setAttribute("kod_spec",StringUtil.toMySQL((abit_A.getKodFakulteta()+"")));


/********* Выборка данных по абитуриентам **********/

   if(session.getAttribute("kod_fak").equals("-1"))
     stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE KodSpetsialnosti LIKE -1");
   else{
     if(session.getAttribute("kod_spec").equals("-1")){
       stmt = conn.prepareStatement("SELECT KodAbiturienta,DokumentyHranjatsja,NomerLichnogoDela,Familija,Imja,Otchestvo,Prinjat,Abbreviatura,Gruppy.Gruppa,Abiturient.KodSpetsialnZach,TipDokSredObraz FROM Abiturient,Spetsialnosti,Gruppy WHERE Gruppy.KodGruppy=Abiturient.KodGruppy AND Abiturient.KodGruppy LIKE ? AND Spetsialnosti.KodSpetsialnosti=Abiturient.KodSpetsialnosti AND Abiturient.KodVuza LIKE ? AND DokumentyHranjatsja LIKE ? ORDER BY Abiturient.Familija,Abiturient.Imja,Abiturient.Otchestvo");
       stmt.setObject(1,session.getAttribute("kod_gr"),Types.INTEGER);
       stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
       stmt.setObject(3,session.getAttribute("doc_hran"),Types.VARCHAR);
     } else {
       stmt = conn.prepareStatement("SELECT KodAbiturienta,DokumentyHranjatsja,NomerLichnogoDela,Familija,Imja,Otchestvo,Prinjat,Abbreviatura,Gruppy.Gruppa,Abiturient.KodSpetsialnZach,TipDokSredObraz FROM Abiturient,Spetsialnosti,Gruppy WHERE Gruppy.KodGruppy=Abiturient.KodGruppy AND Abiturient.KodSpetsialnosti LIKE ? AND Spetsialnosti.KodSpetsialnosti=Abiturient.KodSpetsialnosti AND Abiturient.KodVuza LIKE ? AND DokumentyHranjatsja LIKE ? ORDER BY Abiturient.Familija,Abiturient.Imja,Abiturient.Otchestvo");
       stmt.setObject(1,session.getAttribute("kod_spec"),Types.INTEGER);
       stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
       stmt.setObject(3,session.getAttribute("doc_hran"),Types.VARCHAR);
     }
   }

   rs = stmt.executeQuery();
   count=0;
   while(rs.next()) {

      AbiturientBean abit_TMP = new AbiturientBean();
      abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
      abit_TMP.setDokumentyHranjatsja(rs.getString(2));
      abit_TMP.setNomerLichnogoDela(rs.getString(3));
      abit_TMP.setFamilija(rs.getString(4));
      abit_TMP.setImja(rs.getString(5));
      abit_TMP.setOtchestvo(rs.getString(6));
      abit_TMP.setTipDokSredObraz(rs.getString(11));
      if(rs.getString(7) != null && !rs.getString(7).equals(""))
        abit_TMP.setPrinjat(rs.getString(7));
      else
        abit_TMP.setPrinjat("н");
      abit_TMP.setSpecial1(rs.getString(8));
      abit_TMP.setGruppa(rs.getString(9));
// Если код специальности, на которую абитуриент зачислен, не равен NULL,
// то считываем аббревиатуру этой специальности
      if(rs.getString(10) != null) {
        stmt1 = conn.prepareStatement("SELECT Abbreviatura FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
        stmt1.setObject(1,rs.getString(10),Types.INTEGER);
        rs1 = stmt1.executeQuery();
        if(rs1.next()) abit_TMP.setSpecial6(rs1.getString(1)); //Аббревиатура спец-ти зачисления
      } else abit_TMP.setSpecial6("");
          abit_TMP.setNumber(Integer.toString(++count));
          abits_A.add(abit_TMP);
      }
// Количество абитуриентов
      abit_A.setSpecial22(new Integer(count));
      form.setAction(us.getClientIntName("full","view"));
  }
} 
        catch ( SQLException e ) {
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
        request.setAttribute("abit_A", abit_A);
        request.setAttribute("abits_A", abits_A);
        request.setAttribute("abit_A_S1", abit_A_S1);
        request.setAttribute("abit_A_S2", abit_A_S2);
        request.setAttribute("abit_A_S3", abit_A_S3);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S5", abit_A_S5);
     }
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}