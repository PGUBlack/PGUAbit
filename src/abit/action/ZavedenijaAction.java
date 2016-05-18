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
import abit.Constants;
import abit.sql.*; 

public class ZavedenijaAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session       = request.getSession();
        Connection              conn          = null;
        PreparedStatement       stmt          = null;
        ResultSet               rs            = null;
        ActionErrors            errors        = new ActionErrors();
        ActionError             msg           = null;
        ZavedenijaForm          form          = (ZavedenijaForm) actionForm;
        AbiturientBean          abit_Z        = form.getBean(request, errors);
        boolean                 zavedenija_f  = false;
        boolean                 error         = false;
        ActionForward           f             = null;
        int                     kZav          = 1;
        ArrayList               abits_Z       = new ArrayList();
        UserBean                user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "zavedenijaAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "zavedenijaForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          if ( form.getAction() == null ) {

               form.setAction(us.getClientIntName("new","init"));

          } else if ( form.getAction().equals("create") || form.getAction().equals("full") ) {

/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

                if ( request.getParameter("full") == null &&
                             form.getAction().equals("create")) {

                    stmt = conn.prepareStatement("SELECT MAX(KodZavedenija) FROM Zavedenija");
                    rs = stmt.executeQuery();
                    if(rs.next()) kZav = rs.getInt(1)+1;
                     else kZav = 2;

                    stmt = conn.prepareStatement("INSERT Zavedenija(KodZavedenija,PolnoeNaimenovaniezavedenija,Sokr,KodVuza) VALUES(?,?,?,?)");
                    stmt.setObject(1,new Integer(""+kZav),Types.INTEGER);
                    stmt.setObject(2, abit_Z.getPolnoeNaimenovanieZavedenija(),Types.VARCHAR);
                    if(abit_Z.getSokr().length()>25)
                      stmt.setObject(3, abit_Z.getSokr().substring(0,24),Types.VARCHAR);
                    else
                      stmt.setObject(3, abit_Z.getSokr(),Types.VARCHAR);
                    stmt.setObject(4, session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.executeUpdate();
                    form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {

                   form.setAction(us.getClientIntName("full","view"));

                   stmt = conn.prepareStatement("SELECT KodZavedenija,PolnoeNaimenovaniezavedenija,Sokr FROM Zavedenija WHERE KodVuza LIKE ? AND PolnoeNaimenovaniezavedenija NOT LIKE '' ORDER BY PolnoeNaimenovaniezavedenija ASC");
                   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodZavedenija(new Integer(rs.getInt(1)));
                     abit_TMP.setPolnoeNaimenovanieZavedenija(rs.getString(2));
                     abit_TMP.setSokr(rs.getString(3));
                     abits_Z.add(abit_TMP);
                   }
                 }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodZavedenija,PolnoeNaimenovaniezavedenija,Sokr FROM Zavedenija WHERE KodZavedenija LIKE ?");
                stmt.setObject(1,abit_Z.getKodZavedenija(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_Z.setKodZavedenija(new Integer(rs.getInt(1)));
                  abit_Z.setPolnoeNaimenovanieZavedenija(rs.getString(2));
                  abit_Z.setSokr(rs.getString(3));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("change","act"));
                stmt = conn.prepareStatement("UPDATE Zavedenija SET PolnoeNaimenovaniezavedenija=?,Sokr=? WHERE KodZavedenija LIKE ?");
                stmt.setObject(1,abit_Z.getPolnoeNaimenovanieZavedenija(),Types.VARCHAR);
                if(abit_Z.getSokr().length()>25)
                  stmt.setObject(2, abit_Z.getSokr().substring(0,24),Types.VARCHAR);
                else
                  stmt.setObject(2, abit_Z.getSokr(),Types.VARCHAR);
                stmt.setObject(3,abit_Z.getKodZavedenija(),Types.INTEGER);
                stmt.executeUpdate();
                zavedenija_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM Zavedenija WHERE KodZavedenija LIKE ?");
                stmt.setObject(1,abit_Z.getKodZavedenija(),Types.INTEGER);
                stmt.executeUpdate();

// Установка кода пустой строки для абитуриентов из удаленного заведения
                stmt = conn.prepareStatement("UPDATE Abiturient SET KodZavedenija = 1 WHERE KodZavedenija LIKE ?");
                stmt.setObject(1,abit_Z.getKodZavedenija(),Types.INTEGER);
                stmt.executeUpdate();

                zavedenija_f  = true;
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
        request.setAttribute("abit_Z", abit_Z);
        request.setAttribute("abits_Z", abits_Z);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(zavedenija_f) return mapping.findForward("zavedenija_f");
        return mapping.findForward("success");
    }
}