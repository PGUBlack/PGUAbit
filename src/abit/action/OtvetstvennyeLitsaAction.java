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

public class OtvetstvennyeLitsaAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session               = request.getSession();
        Connection              conn                  = null;
        PreparedStatement       stmt                  = null;
        ResultSet               rs                    = null;
        ActionErrors            errors                = new ActionErrors();
        ActionError             msg                   = null;
        OtvetstvennyeLitsaForm  form                  = (OtvetstvennyeLitsaForm) actionForm;
        AbiturientBean          abit_OL               = form.getBean(request, errors);
        boolean                 otvetstvennyelitsa_f  = false;
        boolean                 error                 = false;
        ActionForward           f                     = null;
        int                     kOL                   = 1;
        ArrayList               abits_OL              = new ArrayList();
        UserBean                user                  = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "otvetstvennyeLitsaAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "otvetstvennyeLitsaForm", form );

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

                    stmt = conn.prepareStatement("SELECT MAX(KodZapisi) FROM OtvetstvennyeLitsa");
                    rs = stmt.executeQuery();
                    if(rs.next()) kOL = rs.getInt(1)+1;
                     else kOL = 2;

                    stmt = conn.prepareStatement("INSERT OtvetstvennyeLitsa(KodZapisi,KodVuza,Doljnost,Fio) VALUES(?,?,?,?)");
                    stmt.setObject(1, new Integer(""+kOL),Types.INTEGER);
                    stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(3, abit_OL.getDoljnost(),Types.VARCHAR);
                    stmt.setObject(4, abit_OL.getFio(),Types.VARCHAR);
                    stmt.executeUpdate();
                    form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
                   stmt = conn.prepareStatement("SELECT KodZapisi,Doljnost,Fio FROM OtvetstvennyeLitsa WHERE KodVuza LIKE ? ORDER BY 1 ASC");
                   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodZapisi(new Integer(rs.getInt(1)));
                     abit_TMP.setDoljnost(rs.getString(2));
                     abit_TMP.setFio(rs.getString(3));
                     abits_OL.add(abit_TMP);
                   }
                 }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT Doljnost,Fio FROM OtvetstvennyeLitsa WHERE KodZapisi LIKE ?");
                stmt.setObject(1,abit_OL.getKodZapisi(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_OL.setDoljnost(rs.getString(1));
                  abit_OL.setFio(rs.getString(2));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("change","act"));
                stmt = conn.prepareStatement("UPDATE OtvetstvennyeLitsa SET Doljnost=?,Fio=? WHERE KodZapisi LIKE ?");
                stmt.setObject(1,abit_OL.getDoljnost(),Types.VARCHAR);
                stmt.setObject(2,abit_OL.getFio(),Types.VARCHAR);
                stmt.setObject(3,abit_OL.getKodZapisi(),Types.INTEGER);
                stmt.executeUpdate();
                otvetstvennyelitsa_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM OtvetstvennyeLitsa WHERE KodZapisi LIKE ?");
                stmt.setObject(1,abit_OL.getKodZapisi(),Types.INTEGER);
                stmt.executeUpdate();
                otvetstvennyelitsa_f  = true;
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
        request.setAttribute("abit_OL", abit_OL);
        request.setAttribute("abits_OL", abits_OL);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(otvetstvennyelitsa_f) return mapping.findForward("otvetstvennyelitsa_f");
        return mapping.findForward("success");
    }
}