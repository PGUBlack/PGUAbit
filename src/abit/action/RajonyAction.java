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

public class RajonyAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session    = request.getSession();
        Connection              conn       = null;
        PreparedStatement       stmt       = null;
        ResultSet               rs         = null;
        ActionErrors            errors     = new ActionErrors();
        ActionError             msg        = null;
        RajonyForm              form       = (RajonyForm) actionForm;
        AbiturientBean          abit_R     = form.getBean(request, errors);
        boolean                 rajony_f   = false;
        boolean                 error      = false;
        int                     kRajona    = -1;
        ActionForward           f          = null;
        ArrayList               abits_R    = new ArrayList();
        ArrayList               abit_R_S1  = new ArrayList();
        UserBean                user       = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "rajonyAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "rajonyForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          if ( form.getAction() == null || form.getAction().equals("full")) {
                 form.setAction(us.getClientIntName("full","init"));
                 stmt = conn.prepareStatement("SELECT DISTINCT KodRajona,NazvanieRajona FROM Rajony WHERE KodVuza LIKE ? AND NazvanieRajona NOT LIKE '' GROUP BY NazvanieRajona,KodRajona ORDER BY NazvanieRajona ASC");
                 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setKodRajona(new Integer(rs.getInt(1)));
                   abit_TMP.setNazvanieRajona(rs.getString(2));
                   if(rs.next()) {
                     abit_TMP.setSpecial11(new Integer(rs.getInt(1)));
                     abit_TMP.setSpecial1(rs.getString(2));
                   }
                   if(rs.next()) {
                     abit_TMP.setSpecial22(new Integer(rs.getInt(1)));
                     abit_TMP.setSpecial2(rs.getString(2));
                   }
                   abits_R.add(abit_TMP);
                 }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/
/************************************************************************************************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodRajona,NazvanieRajona FROM Rajony WHERE KodRajona LIKE ?");
                stmt.setObject(1,abit_R.getKodRajona(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_R.setKodRajona(new Integer(rs.getInt(1)));
                  abit_R.setNazvanieRajona(rs.getString(2));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************************************************************************************************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {

                form.setAction(us.getClientIntName("change","act"));
// Если вводимый район уже существует, то выбираем его код
                stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE NazvanieRajona LIKE ?");
                stmt.setObject(1,abit_R.getNazvanieRajona(),Types.VARCHAR);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  kRajona = rs.getInt(1);
                  if(!(kRajona+"").equals(abit_R.getKodRajona()+"")) {
// Запись о старом районе уничтожаем
                   stmt = conn.prepareStatement("DELETE FROM Rajony WHERE KodRajona LIKE ?");
                   stmt.setObject(1,abit_R.getKodRajona(),Types.INTEGER);
                   stmt.executeUpdate();
                  }
                } else {
                    stmt = conn.prepareStatement("UPDATE Rajony SET NazvanieRajona=? WHERE KodRajona LIKE ?");
                    stmt.setObject(1,abit_R.getNazvanieRajona(),Types.VARCHAR);
                    stmt.setObject(2,abit_R.getKodRajona(),Types.INTEGER);
                    stmt.executeUpdate();
                    kRajona = StringUtil.toInt(abit_R.getKodRajona()+"",1);
                }
                  stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE KodRajona LIKE ?");
                  stmt.setObject(1,abit_R.getKodRajona(),Types.INTEGER);
                  rs = stmt.executeQuery();
                  while(rs.next()) {
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodRajona=? WHERE KodAbiturienta LIKE ?");
                    stmt.setObject(1,new Integer(kRajona),Types.INTEGER);
                    stmt.setObject(2,new Integer(rs.getString(1)),Types.INTEGER);
                    stmt.executeUpdate();                
                  }
                rajony_f  = true;
                }
                else {
                form.setAction(us.getClientIntName("change","act-fix-abit"));
// Коды абитуриентов у которых изменяем код района
                ArrayList abits = new ArrayList();
                stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE KodRajona LIKE ?");
                stmt.setObject(1,abit_R.getKodRajona(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) abits.add(rs.getString(1));
// Выборка пустого кода района
                stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE NazvanieRajona LIKE ''");
                rs = stmt.executeQuery();
                if(rs.next()) kRajona = rs.getInt(1);

// Смена кодов районов на код пустой записи
                for(int i=0;i<abits.size();i++) {
                stmt = conn.prepareStatement("UPDATE Abiturient SET KodRajona=? WHERE KodAbiturienta LIKE ?");
                stmt.setObject(1,new Integer(kRajona+""),Types.INTEGER);
                stmt.setObject(2,new Integer(""+abits.get(i)),Types.INTEGER);
                stmt.executeUpdate();
                }              

// Удаление старой записи из районов
                stmt = conn.prepareStatement("DELETE FROM Rajony WHERE KodRajona LIKE ?");
                stmt.setObject(1,abit_R.getKodRajona(),Types.INTEGER);
                stmt.executeUpdate();
                rajony_f  = true;
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
        request.setAttribute("abit_R", abit_R);
        request.setAttribute("abits_R", abits_R);
        request.setAttribute("abit_R_S1", abit_R_S1);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(rajony_f) return mapping.findForward("rajony_f");
        return mapping.findForward("success");
    }
}