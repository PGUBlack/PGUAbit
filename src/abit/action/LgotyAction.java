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

public class LgotyAction extends Action {

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
        LgotyForm          form        = (LgotyForm) actionForm;
        AbiturientBean     abit_Lgoty  = form.getBean(request, errors);
        boolean            lgoty_f     = false;
        boolean            error       = false;
        ActionForward      f           = null;
        int                kLg         = 1;
        ArrayList          abits_Lgoty = new ArrayList();
        UserBean           user        = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
                     
        request.setAttribute( "lgotyAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "lgotyForm", form );

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
                             form.getAction().equals("create") ) {

                    stmt = conn.prepareStatement("SELECT MAX(KodLgot) FROM Lgoty");
                    rs = stmt.executeQuery();
                    if(rs.next()) kLg = rs.getInt(1)+1;
                     else kLg = 2;

                    stmt = conn.prepareStatement("INSERT Lgoty(KodLgot,ShifrLgot,Lgoty,KodVuza) VALUES(?,?,?,?)");
                    stmt.setObject(1, new Integer(""+kLg),Types.INTEGER);
                    stmt.setObject(2, abit_Lgoty.getShifrLgot(),Types.VARCHAR);
                    stmt.setObject(3, abit_Lgoty.getLgoty(),Types.VARCHAR);
                    stmt.setObject(4, session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.executeUpdate();
                    form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
                   stmt = conn.prepareStatement("SELECT KodLgot,ShifrLgot,Lgoty FROM Lgoty WHERE KodVuza LIKE ? ORDER BY 1");
                   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodLgot(new Integer(rs.getInt(1)));
                     abit_TMP.setShifrLgot(rs.getString(2));
                     abit_TMP.setLgoty(rs.getString(3));
                     abits_Lgoty.add(abit_TMP);
                   }
                 }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodLgot,ShifrLgot,Lgoty FROM Lgoty WHERE KodLgot LIKE ?");
                stmt.setObject(1,abit_Lgoty.getKodLgot(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_Lgoty.setKodLgot(new Integer(rs.getInt(1)));
                  abit_Lgoty.setShifrLgot(rs.getString(2));
                  abit_Lgoty.setLgoty(rs.getString(3));
                }
                form.setAction(us.getClientIntName("md_dl","view"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("change","act"));
                stmt = conn.prepareStatement("UPDATE Lgoty SET ShifrLgot=?,Lgoty=? WHERE KodLgot LIKE ?");
                stmt.setObject(1,abit_Lgoty.getShifrLgot(),Types.VARCHAR);
                stmt.setObject(2,abit_Lgoty.getLgoty(),Types.VARCHAR);
                stmt.setObject(3,abit_Lgoty.getKodLgot(),Types.INTEGER);
                stmt.executeUpdate();
                lgoty_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM Lgoty WHERE KodLgot LIKE ?");
                stmt.setObject(1,abit_Lgoty.getKodLgot(),Types.INTEGER);
                stmt.executeUpdate();
                lgoty_f  = true;
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
        request.setAttribute("abit_Lgoty", abit_Lgoty);
        request.setAttribute("abits_Lgoty", abits_Lgoty);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lgoty_f) return mapping.findForward("lgoty_f");
        return mapping.findForward("success");
    }
}