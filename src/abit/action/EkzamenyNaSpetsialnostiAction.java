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

public class EkzamenyNaSpetsialnostiAction extends Action {

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
        boolean                      priznak     = true;
        int                          number      = 0;
        String                       codeLine    = new String("");
        String                       kSpec       = new String();
        String                       kPredmeta   = new String();
        String                       oldKodeS    = new String();
        String                       oldKodeF    = new String();
        String                       oldNazv     = new String();

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "ekzamenyNaSpetsialnostiAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "ekzamenyNaSpetsialnostiForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");

/********* Подготовка данных для ввода с помощью селекторов ***********/

          String predmets = new String("%");
          stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Predmet FROM NazvanijaPredmetov ORDER BY 1");
          rs = stmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
            abit_TMP.setPredmet(rs.getString(2));
            abit_ENS_S3.add(abit_TMP);
            predmets += rs.getString(1)+"%";
          }

          abit_ENS.setSpecial1(predmets);

          stmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta,ShifrFakulteta,KodFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
             AbiturientBean abit_TMP = new AbiturientBean();
             abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
             abit_TMP.setShifrFakulteta(rs.getString(2));
             abit_TMP.setKodFakulteta(new Integer(rs.getInt(3)));
             abit_ENS_S5.add(abit_TMP);
          }

/************************************************************************************************/

          if ( form.getAction() == null ) {

             form.setAction(us.getClientIntName("md_dl","init"));

          } 

/************************************** Создание записи *****************************************/

          if ( form.getAction().equals("mod_del") && request.getParameter("change") != null) {

/************************************************************************************************/
/********************** Подготовка данных для ввода с помощью селекторов ************************/

             kSpec = (""+abit_ENS.getSpecial2()).substring(0,(abit_ENS.getSpecial2()).indexOf("+"));
             stmt = conn.prepareStatement("DELETE FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE '"+kSpec+"'");
             stmt.executeUpdate();

             Enumeration paramNames = request.getParameterNames();
             while(paramNames.hasMoreElements()) {
               String paramName = (String)paramNames.nextElement();
               String paramValue[] = request.getParameterValues(paramName);
               if(paramName.indexOf("NaznacCB") != -1) {
                 if(paramValue[0].equals("on")) {
                   stmt = conn.prepareStatement("INSERT EkzamenyNaSpetsialnosti(KodSpetsialnosti,KodPredmeta,Prioritet) VALUES(?,?,0)");
                   stmt.setObject(1, new Integer(kSpec),Types.INTEGER);                  // Код специальности
                   stmt.setObject(2, new Integer(paramName.substring(8)),Types.INTEGER); // Код предмета
                   stmt.executeUpdate();
                 }
               }
             }

             form.setAction(us.getClientIntName("md_dl","act"));
          }
System.out.println("Action="+form.getAction());
          if(form.getAction().equals("md_dl")) {

// Начало выборки в Special2() в формате: Факультет + Специальность + {% <Код предмета>}

            priznak = true;
            abit_ENS_S1 = new ArrayList();
            oldKodeF = "";
            oldKodeS = "";
            codeLine = "";

            stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,ens.KodPredmeta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,EkzamenyNaSpetsialnosti ens,Fakultety f WHERE f.KodFakulteta = s.KodFakulteta AND s.KodSpetsialnosti = ens.KodSpetsialnosti AND f.KodVuza LIKE ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta,ens.KodPredmeta ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial2(rs.getString(1));
              if(priznak) {
                oldKodeS = rs.getString(1);
                oldKodeF = "+" + rs.getString(2);
                oldNazv = rs.getString(4);
                priznak = false;
              }
              if(!oldKodeS.equals(rs.getString(1))) {
                if(kPredmeta.equals("")) kPredmeta += "%" + rs.getString(3);
                codeLine += "'"+oldKodeS+"'"+",";
                abit_TMP.setSpecial2(oldKodeS + oldKodeF + kPredmeta);
                abit_TMP.setNazvanieSpetsialnosti(oldNazv);
                abit_ENS_S1.add(abit_TMP);
                priznak = true;
                kPredmeta = "";
              }
              kPredmeta += "%" + rs.getString(3);
            }

            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial2(oldKodeS + oldKodeF + kPredmeta);
            codeLine += "'"+oldKodeS+"'";
            abit_TMP2.setNazvanieSpetsialnosti(oldNazv);
            abit_ENS_S1.add(abit_TMP2);
            number = 0;
            if(codeLine.equals("")) codeLine = "''";

            stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.KodFakulteta = s.KodFakulteta AND f.KodVuza LIKE '"+session.getAttribute("kVuza")+"' AND s.KodSpetsialnosti NOT IN("+codeLine+") ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial2(rs.getString(1)+"+"+rs.getString(2)+"%");
              abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
              abit_ENS_S1.add(abit_TMP);
              number++;
            }

            abit_ENS.setNumber(Integer.toString(number));
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