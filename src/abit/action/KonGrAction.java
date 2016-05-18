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

public class KonGrAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session     = request.getSession();
        Connection              conn        = null;
        PreparedStatement       stmt        = null;
        ResultSet               rs          = null;
        ActionErrors            errors      = new ActionErrors();
        ActionError             msg         = null;
        KonGrForm               form        = (KonGrForm) actionForm;
        AbiturientBean          abit_F      = form.getBean(request, errors);
        boolean                 kongruppa_f = false;
        boolean                 error       = false;
        ActionForward           f           = null;
        int                     kKGr        = 1;
        ArrayList               abits_F     = new ArrayList();
        UserBean                user        = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "konGrpAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "konGrpForm", form );

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

                    kKGr = 2;
                    stmt = conn.prepareStatement("SELECT MAX(KodKonGruppy) FROM KonGruppa");
                    rs = stmt.executeQuery();
                    if(rs.next()) kKGr = rs.getInt(1)+1;

                    stmt = conn.prepareStatement("INSERT KonGruppa(KodKonGruppy,KodVuza,Nazvanie,Abbr) VALUES(?,?,?,?)");
                    stmt.setObject(1, new Integer(kKGr+""),Types.INTEGER);
                    stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(3, abit_F.getNazvanie(),Types.VARCHAR);
                    stmt.setObject(4, abit_F.getAbbreviatura(),Types.VARCHAR);
                    stmt.executeUpdate();
                    form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/
/************************************************************************************************/

               } else {
                   form.setAction(us.getClientIntName("full","view"));

                   stmt = conn.prepareStatement("SELECT KodKonGruppy,Nazvanie,Abbr FROM KonGruppa WHERE KodVuza LIKE ? ORDER BY 1 ASC");

                   stmt.setObject(1,""+session.getAttribute("kVuza"));

                   rs = stmt.executeQuery();

                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodKonGrp( new Integer(rs.getString(1) ));
                     abit_TMP.setNazvanie( rs.getString(2) );
                     abit_TMP.setAbbreviatura( rs.getString(3) );
                     abits_F.add(abit_TMP);
                   }
                }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                      form.setAction(us.getClientIntName("md_dl","view"));
                      stmt = conn.prepareStatement("SELECT DISTINCT Nazvanie,Abbr FROM KonGruppa WHERE KodKonGruppy LIKE ?");
                      stmt.setObject(1, abit_F.getKodKonGrp(),Types.INTEGER);
                      rs = stmt.executeQuery();
                      if ( rs.next() ) {
                         abit_F.setNazvanie( rs.getString(1) );
                         abit_F.setAbbreviatura(rs.getString(2));
                      }

/************************************************************************************************/
/*********************  Если action="change", то изменяем указанную запись  *********************/
/*********** или если передается дополнительный параметр "delete" - удаляем запись **************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                      form.setAction(us.getClientIntName("change","act"));
                      stmt = conn.prepareStatement("UPDATE KonGruppa SET Nazvanie=?,Abbr=? WHERE KodKonGruppy LIKE ?");
                      stmt.setObject(1,abit_F.getNazvanie(),Types.VARCHAR);
                      stmt.setObject(2,abit_F.getAbbreviatura(),Types.VARCHAR);
                      stmt.setObject(3,abit_F.getKodKonGrp(),Types.INTEGER);
                      stmt.executeUpdate();
                      kongruppa_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                      form.setAction(us.getClientIntName("delete","act"));

// Присвоение специальностям, имеющим удаляемый код конкурсной группы, код пустой конкурсной группы "-", т.е. 1 

                      stmt = conn.prepareStatement("UPDATE Spetsialnosti SET KodKonGruppy = 1 WHERE KodKonGruppy LIKE ?");
                      stmt.setObject(1, abit_F.getKodKonGrp(),Types.INTEGER);
                      stmt.executeUpdate();

// Удаление самой конкурсной группы из таблицы KonGruppa

                      stmt = conn.prepareStatement("DELETE FROM KonGruppa WHERE KodKonGruppy LIKE ?");
                      stmt.setObject(1, abit_F.getKodKonGrp(),Types.INTEGER);
                      stmt.executeUpdate();

                      kongruppa_f  = true;
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
        request.setAttribute("abit_F", abit_F);
        request.setAttribute("abits_F", abits_F);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(kongruppa_f) return mapping.findForward("kongruppa_f");
        return mapping.findForward("success");
    }
}