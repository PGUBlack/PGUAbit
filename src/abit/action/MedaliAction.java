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

public class MedaliAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session      = request.getSession();
        Connection        conn         = null;
        PreparedStatement stmt         = null;
        ResultSet         rs           = null;
        ActionErrors      errors       = new ActionErrors();
        ActionError       msg          = null;
        MedaliForm        form         = (MedaliForm) actionForm;
        AbiturientBean    abit_Medali  = form.getBean(request, errors);
        boolean           medali_f     = false;
        boolean           error        = false;
        ActionForward     f            = null;
        int               kMed         = 1;
        ArrayList         abits_Medali = new ArrayList();
        UserBean          user         = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "medaliAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "medaliForm", form );

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

                    stmt = conn.prepareStatement("SELECT MAX(KodMedali) FROM Medali");
                    rs = stmt.executeQuery();
                    if(rs.next()) kMed = rs.getInt(1)+1;
                     else kMed = 2;

                    stmt = conn.prepareStatement("INSERT Medali(KodMedali,ShifrMedali,Medal,KodVuza) VALUES(?,?,?,?)");
                    stmt.setObject(1, new Integer(""+kMed),Types.INTEGER);
                    stmt.setObject(2, abit_Medali.getShifrMedali(),Types.VARCHAR);
                    stmt.setObject(3, abit_Medali.getMedal(),Types.VARCHAR);
                    stmt.setObject(4, session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.executeUpdate();
                    form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
                   stmt = conn.prepareStatement("SELECT KodMedali,ShifrMedali,Medal FROM Medali WHERE KodVuza LIKE ? ORDER BY 1");
                   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodMedali(new Integer(rs.getInt(1)));
                     abit_TMP.setShifrMedali(rs.getString(2));
                     abit_TMP.setMedal(rs.getString(3));
                     abits_Medali.add(abit_TMP);
                   }
                 }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodMedali,ShifrMedali,Medal FROM Medali WHERE KodMedali LIKE ?");
                stmt.setObject(1,abit_Medali.getKodMedali(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_Medali.setKodMedali(new Integer(rs.getInt(1)));
                  abit_Medali.setShifrMedali(rs.getString(2));
                  abit_Medali.setMedal(rs.getString(3));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("change","act"));
                stmt = conn.prepareStatement("UPDATE Medali SET ShifrMedali=?,Medal=? WHERE KodMedali LIKE ?");
                stmt.setObject(1,abit_Medali.getShifrMedali(),Types.VARCHAR);
                stmt.setObject(2,abit_Medali.getMedal(),Types.VARCHAR);
                stmt.setObject(3,abit_Medali.getKodMedali(),Types.INTEGER);
                stmt.executeUpdate();
                medali_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM Medali WHERE KodMedali LIKE ?");
                stmt.setObject(1,abit_Medali.getKodMedali(),Types.INTEGER);
                stmt.executeUpdate();
                medali_f  = true;
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
        request.setAttribute("abit_Medali", abit_Medali);
        request.setAttribute("abits_Medali", abits_Medali);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(medali_f) return mapping.findForward("medali_f");
        return mapping.findForward("success");
    }
}