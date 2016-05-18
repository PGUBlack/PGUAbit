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

public class OblastiAction extends Action {

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
        OblastiForm             form       = (OblastiForm) actionForm;
        AbiturientBean          abit_O     = form.getBean(request, errors);
        boolean                 oblasti_f  = false;
        boolean                 error      = false;
        int                     kOblasti   = -1;
        ActionForward           f          = null;
        ArrayList               abits_O    = new ArrayList();
        ArrayList               abit_O_S1  = new ArrayList();
        UserBean                user       = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "oblastiAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "oblastiForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null || form.getAction().equals("full")) {

                   form.setAction(us.getClientIntName("full","init"));
                              
                   stmt = conn.prepareStatement("SELECT DISTINCT KodOblasti,NazvanieOblasti FROM Oblasti WHERE KodVuza LIKE ? AND NazvanieOblasti NOT LIKE '' GROUP BY NazvanieOblasti,KodOblasti ORDER BY NazvanieOblasti ASC");
                   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodOblasti(new Integer(rs.getInt(1)));
                     abit_TMP.setNazvanieOblasti(rs.getString(2));
                     abits_O.add(abit_TMP);
                   }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/
/************************************************************************************************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodOblasti,NazvanieOblasti FROM Oblasti WHERE KodOblasti LIKE ?");
                stmt.setObject(1, abit_O.getKodOblasti(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_O.setKodOblasti(new Integer(rs.getInt(1)));
                  abit_O.setNazvanieOblasti(rs.getString(2));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************************************************************************************************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("delete","act-exist"));
// Если вводимая область уже существует, то выбираем ее код
                stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti LIKE ?");
                stmt.setObject(1,abit_O.getNazvanieOblasti(),Types.VARCHAR);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  kOblasti = rs.getInt(1);
                  if(!(kOblasti+"").equals(abit_O.getKodOblasti()+"")) {
// Запись о старой области уничтожаем
                  stmt = conn.prepareStatement("DELETE FROM Oblasti WHERE KodOblasti LIKE ?");
                  stmt.setObject(1,abit_O.getKodOblasti(),Types.INTEGER);
                  stmt.executeUpdate();
                  }
                } else {
                    form.setAction(us.getClientIntName("change","act"));
                    stmt = conn.prepareStatement("UPDATE Oblasti SET NazvanieOblasti=? WHERE KodOblasti LIKE ?");
                    stmt.setObject(1,abit_O.getNazvanieOblasti(),Types.VARCHAR);
                    stmt.setObject(2,abit_O.getKodOblasti(),Types.INTEGER);
                    stmt.executeUpdate();
                    kOblasti = StringUtil.toInt(abit_O.getKodOblasti()+"",1);
                }
                  stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE KodOblasti LIKE ?");
                  stmt.setObject(1,abit_O.getKodOblasti(),Types.INTEGER);
                  rs = stmt.executeQuery();
                  while(rs.next()) {
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodOblasti=? WHERE KodAbiturienta LIKE ?");
                    stmt.setObject(1,new Integer(""+kOblasti),Types.INTEGER);
                    stmt.setObject(2,new Integer(rs.getString(1)),Types.INTEGER);
                    stmt.executeUpdate();                
                  }
                oblasti_f  = true;
                }
                else {
                form.setAction(us.getClientIntName("change","act-fix-abit"));
// Коды абитуриентов у которых изменяем код области
                ArrayList abits = new ArrayList();
                stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE KodOblasti LIKE ?");
                stmt.setObject(1,abit_O.getKodOblasti(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) abits.add(rs.getString(1));
// Выборка пустого кода области
                stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti LIKE ''");
                rs = stmt.executeQuery();
                if(rs.next()) kOblasti = rs.getInt(1);

// Смена кодов области на код пустой записи
                for(int i=0;i<abits.size();i++) {
                stmt = conn.prepareStatement("UPDATE Abiturient SET KodOblasti=? WHERE KodAbiturienta LIKE ?");
                stmt.setObject(1,new Integer(kOblasti+""),Types.INTEGER);
                stmt.setObject(2,new Integer(""+abits.get(i)),Types.INTEGER);
                stmt.executeUpdate();
                }              

// Удаление старой записи из областей
                stmt = conn.prepareStatement("DELETE FROM Oblasti WHERE KodOblasti LIKE ?");
                stmt.setObject(1,abit_O.getKodOblasti(),Types.INTEGER);
                stmt.executeUpdate();
                oblasti_f  = true;
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
        request.setAttribute("abit_O", abit_O);
        request.setAttribute("abits_O", abits_O);
        request.setAttribute("abit_O_S1", abit_O_S1);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(oblasti_f) return mapping.findForward("oblasti_f");
        return mapping.findForward("success");
    }
}