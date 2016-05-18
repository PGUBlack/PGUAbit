package abit.action;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

public class BadAttAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession        session     = request.getSession();
        Connection         conn        = null;
        PreparedStatement  stmt        = null;
        ResultSet          rs          = null;
        ActionErrors       errors      = new ActionErrors();
        ActionError        msg         = null;
        BadAttForm         form        = (BadAttForm) actionForm;
        AbiturientBean     abit_ba     = form.getBean(request, errors);
        boolean            badAtt_f    = false;
        boolean            error       = false;
        ActionForward      f           = null;
        int                kBa         = 1;
        ArrayList          abits_ba    = new ArrayList();
        UserBean           user        = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
                     
        request.setAttribute( "badAttAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "badAttForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {

                 form.setAction(us.getClientIntName("new","init"));

            } else if ( form.getAction().equals("create") || form.getAction().equals("full") ) {
/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

  if ( request.getParameter("full") == null && form.getAction().equals("create") ) {

      if(abit_ba.getSpecial1().equals("one")) {

// Вводим только один номер и серию аттестата

        stmt = conn.prepareStatement("SELECT MAX(KodZapisi) FROM Bad_Attestat");
        rs = stmt.executeQuery();
        if(rs.next()) kBa = rs.getInt(1)+1;
        else kBa = 2;

        stmt = conn.prepareStatement("INSERT Bad_Attestat(KodZapisi,Seria,Nomer,KodVuza) VALUES(?,?,?,?)");
        stmt.setObject(1, new Integer(""+kBa),Types.INTEGER);
        stmt.setObject(2, abit_ba.getSeriaAtt().toUpperCase(),Types.VARCHAR);
        stmt.setObject(3, abit_ba.getNomerAtt(),Types.VARCHAR);
        stmt.setObject(4, session.getAttribute("kVuza"),Types.INTEGER);
        stmt.executeUpdate();
        form.setAction(us.getClientIntName("new","act"));

      } else {

// Вводим несколько номеров и аттестатов (от Special2 до Special3) одной серии

// Анализ начального номера аттестата на предмет обнаружения нулей слева
// prefix - хранит количество нулей dпереди номера аттестата

  StringBuffer prefix = new StringBuffer();

  for(int kol_zeros=0;kol_zeros<abit_ba.getSpecial2().length();kol_zeros++)
     if(abit_ba.getSpecial2().substring(kol_zeros,kol_zeros+1).equals("0")) prefix.append("0");
     else break;

  for(int curr_nom=StringUtil.toInt(abit_ba.getSpecial2(),0);curr_nom<=StringUtil.toInt(abit_ba.getSpecial3(),0);curr_nom++) {

     stmt = conn.prepareStatement("SELECT MAX(KodZapisi) FROM Bad_Attestat");
     rs = stmt.executeQuery();
     if(rs.next()) kBa = rs.getInt(1)+1;
     else kBa = 2;

     stmt = conn.prepareStatement("INSERT Bad_Attestat(KodZapisi,Seria,Nomer,KodVuza) VALUES(?,?,?,?)");
     stmt.setObject(1, new Integer(""+kBa),Types.INTEGER);
     stmt.setObject(2, abit_ba.getSeriaAtt().toUpperCase(),Types.VARCHAR);
     stmt.setObject(3, prefix.toString()+curr_nom,Types.VARCHAR);
     stmt.setObject(4, session.getAttribute("kVuza"),Types.INTEGER);
     stmt.executeUpdate();
     form.setAction(us.getClientIntName("new","act"));
  }
} 

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
                   stmt = conn.prepareStatement("SELECT KodZapisi,Seria,Nomer FROM Bad_Attestat WHERE KodVuza LIKE ? ORDER BY Seria,Nomer");
                   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodZapisi(new Integer(rs.getInt(1)));
                     abit_TMP.setSeriaAtt(rs.getString(2));
                     abit_TMP.setNomerAtt(rs.getString(3));
                     abits_ba.add(abit_TMP);
                   }
                 }

/************************************************************************************************/
/********************  Если action="check", то выполняем проверку аттестата  ********************/

            } else if ( form.getAction().equals("check") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija+' '+SUBSTRING(Imja,1,1)+'. '+SUBSTRING(Otchestvo,1,1)+'. ',ba.Seria,ba.Nomer FROM Bad_Attestat ba, Abiturient a WHERE a.KodVuza=ba.KodVuza AND a.SeriaAtt LIKE ba.Seria AND a.NomerAtt LIKE ba.Nomer AND a.KodVuza LIKE ?");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  AbiturientBean abit_TMP = new AbiturientBean();
                  abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
                  abit_TMP.setNomerLichnogoDela(rs.getString(2));
                  abit_TMP.setFamilija(rs.getString(3));
                  abit_TMP.setSeriaAtt(rs.getString(4));
                  abit_TMP.setNomerAtt(rs.getString(5));
                  abits_ba.add(abit_TMP);
                }
                form.setAction(us.getClientIntName("results","view"));

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodZapisi,Seria,Nomer FROM Bad_Attestat WHERE KodZapisi LIKE ?");
                stmt.setObject(1,abit_ba.getKodZapisi(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_ba.setKodZapisi(new Integer(rs.getInt(1)));
                  abit_ba.setSeriaAtt(rs.getString(2));
                  abit_ba.setNomerAtt(rs.getString(3));
                }
                form.setAction(us.getClientIntName("md_dl","view"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("change","act"));
                stmt = conn.prepareStatement("UPDATE Bad_Attestat SET Seria=?,Nomer=? WHERE KodZapisi LIKE ?");
                stmt.setObject(1,abit_ba.getSeriaAtt().toUpperCase(),Types.VARCHAR);
                stmt.setObject(2,abit_ba.getNomerAtt(),Types.VARCHAR);
                stmt.setObject(3,abit_ba.getKodZapisi(),Types.INTEGER);
                stmt.executeUpdate();
                badAtt_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM Bad_Attestat WHERE KodZapisi LIKE ?");
                stmt.setObject(1,abit_ba.getKodZapisi(),Types.INTEGER);
                stmt.executeUpdate();
                badAtt_f  = true;
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
        request.setAttribute("abit_ba", abit_ba);
        request.setAttribute("abits_ba", abits_ba);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(badAtt_f) return mapping.findForward("badAtt_f");
        return mapping.findForward("success");
    }
}