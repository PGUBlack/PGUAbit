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

public class FakultetyAction extends Action {

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
        FakultetyForm           form        = (FakultetyForm) actionForm;
        AbiturientBean          abit_F      = form.getBean(request, errors);
        boolean                 fakultety_f = false;
        boolean                 error       = false;
        ActionForward           f           = null;
        int                     kFak        = 1;
        ArrayList               abits_F     = new ArrayList();
        UserBean                user        = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "fakultetyAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "fakultetyForm", form );

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

                    kFak = 2;
                    stmt = conn.prepareStatement("SELECT MAX(KodFakulteta) FROM Fakultety");
                    rs = stmt.executeQuery();
                    if(rs.next()) kFak = rs.getInt(1)+1;

                    stmt = conn.prepareStatement("INSERT Fakultety(KodFakulteta,KodVuza,Fakultet,AbbreviaturaFakulteta,PlanPriemaFakulteta,ShifrFakulteta,NazvanieVRoditelnom,PoluProhodnoiBallFakulteta,ProhodnoiBallFakulteta,Dekan) VALUES(?,?,?,?,?,?,?,?,?,?)");
                    stmt.setObject(1, new Integer(kFak+""),Types.INTEGER);
                    stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(3, abit_F.getFakultet(),Types.VARCHAR);
                    stmt.setObject(4, abit_F.getAbbreviaturaFakulteta(),Types.VARCHAR);
                    stmt.setObject(5, abit_F.getPlanPriemaFakulteta(),Types.INTEGER);
                    stmt.setObject(6, abit_F.getShifrFakulteta(),Types.VARCHAR);
                    stmt.setObject(7, abit_F.getNazvanieVRoditelnom(),Types.VARCHAR);
                    stmt.setObject(8, abit_F.getPoluProhodnoiBallFakulteta(),Types.INTEGER);
                    stmt.setObject(9, abit_F.getProhodnoiBallFakulteta(),Types.INTEGER);
                    stmt.setObject(10, abit_F.getDekan(),Types.VARCHAR);
                    stmt.executeUpdate();
                    form.setAction(us.getClientIntName("new","crt"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/
/************************************************************************************************/

               } else {
                   form.setAction(us.getClientIntName("full","view"));

                   if ( request.getParameter("stolbetsSortirovki") != null ) {
                      session.setAttribute("stolbetsSortirovki", request.getParameter("stolbetsSortirovki"));
                      session.setAttribute("priznakSortirovki", request.getParameter("priznakSortirovki"));
                   }
                   String query = new String("SELECT Fakultet,AbbreviaturaFakulteta,PlanPriemaFakulteta,ShifrFakulteta,NazvanieVRoditelnom,Dekan,PoluProhodnoiBallFakulteta,ProhodnoiBallFakulteta,KodFakulteta FROM Fakultety WHERE KodVuza LIKE ");

                   query += session.getAttribute("kVuza");

                   query += " ORDER BY ";

                   query += session.getAttribute("stolbetsSortirovki");

                   if ( session.getAttribute("priznakSortirovki").toString().length() == 14) 
                      query += " ASC";
                   else
                      query += " DESC";

                   stmt = conn.prepareStatement(query);

                   rs = stmt.executeQuery();

                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setFakultet( rs.getString(1) );
                     abit_TMP.setAbbreviaturaFakulteta( rs.getString(2) );
                     abit_TMP.setPlanPriemaFakulteta( new Integer(rs.getInt(3)) );
                     abit_TMP.setShifrFakulteta( rs.getString(4) );
                     abit_TMP.setNazvanieVRoditelnom( rs.getString(5) );
                     abit_TMP.setDekan( rs.getString(6) );
                     abit_TMP.setPoluProhodnoiBallFakulteta( new Integer(rs.getInt(7)) );
                     abit_TMP.setProhodnoiBallFakulteta( new Integer(rs.getInt(8)) );
                     abit_TMP.setKodFakulteta( new Integer(rs.getInt(9)) );
                     abits_F.add(abit_TMP);
                   }
                }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                      form.setAction(us.getClientIntName("md_dl","view"));
                      stmt = conn.prepareStatement("SELECT DISTINCT Fakultet,AbbreviaturaFakulteta,PlanPriemaFakulteta,ShifrFakulteta,NazvanieVRoditelnom,PoluProhodnoiBallFakulteta,ProhodnoiBallFakulteta,Dekan FROM Fakultety WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      rs = stmt.executeQuery();
                      if ( rs.next() ) {
                         abit_F.setFakultet( rs.getString(1) );
                         abit_F.setAbbreviaturaFakulteta(rs.getString(2));
                         abit_F.setPlanPriemaFakulteta(new Integer(rs.getInt(3)));
                         abit_F.setShifrFakulteta(rs.getString(4));
                         abit_F.setNazvanieVRoditelnom(rs.getString(5));
                         abit_F.setPoluProhodnoiBallFakulteta(new Integer(rs.getInt(6)));
                         abit_F.setProhodnoiBallFakulteta(new Integer(rs.getInt(7)));
                         abit_F.setDekan(rs.getString(8));
                      }

/************************************************************************************************/
/*********************  Если action="change", то изменяем указанную запись  *********************/
/*********** или если передается дополнительный параметр "delete" - удаляем запись **************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                      form.setAction(us.getClientIntName("change","act"));
                      stmt = conn.prepareStatement("UPDATE Fakultety SET Fakultet=?,AbbreviaturaFakulteta=?,PlanPriemaFakulteta=?,ShifrFakulteta=?,NazvanieVRoditelnom=?,PoluProhodnoiBallFakulteta=?,ProhodnoiBallFakulteta=?,Dekan=? WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1,abit_F.getFakultet(),Types.VARCHAR);
                      stmt.setObject(2,abit_F.getAbbreviaturaFakulteta(),Types.VARCHAR);
                      stmt.setObject(3, abit_F.getPlanPriemaFakulteta(),Types.INTEGER);
                      stmt.setObject(4,abit_F.getShifrFakulteta(),Types.VARCHAR);
                      stmt.setObject(5,abit_F.getNazvanieVRoditelnom(),Types.VARCHAR);
                      stmt.setObject(6, abit_F.getPoluProhodnoiBallFakulteta(),Types.INTEGER);
                      stmt.setObject(7, abit_F.getProhodnoiBallFakulteta(),Types.INTEGER);
                      stmt.setObject(8,abit_F.getDekan(),Types.VARCHAR);
                      stmt.setObject(9, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();
                      fakultety_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                      form.setAction(us.getClientIntName("delete","act"));
// Начало транзакции
                      conn.setAutoCommit(false);

                      stmt = conn.prepareStatement("DELETE FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?)");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();

                      stmt = conn.prepareStatement("DELETE FROM Spetsialnosti WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();

                      stmt = conn.prepareStatement("DELETE FROM Fakultety WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();

// Закрепление транзакции
                      conn.setAutoCommit(true);
                      conn.commit();

                      fakultety_f  = true;
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
        if(fakultety_f) return mapping.findForward("fakultety_f");
        return mapping.findForward("success");
    }
}