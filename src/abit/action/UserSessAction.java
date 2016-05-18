package abit.action;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Date;
import abit.servlet.UserSessions;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*;

public class UserSessAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession        session     = request.getSession();
        DataSource         dataSource  = null;
        Connection         conn        = null;
        PreparedStatement  pstmt       = null;
        Statement          stmt        = null;
        ResultSet          rs          = null;
        ResultSet          rs2         = null;
        ActionErrors       errors      = new ActionErrors();
        ActionError        msg         = null;
        UserSessForm       form        = (UserSessForm) actionForm;
        UserBean           u_bean      = form.getBean(request, errors);
        boolean            error       = false;
        ArrayList          months      = new ArrayList();
        ArrayList          stats       = new ArrayList();
        ArrayList          sessS       = new ArrayList();
        UserBean           user        = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "userSessAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          Context env = (Context) new InitialContext().lookup("java:comp/env");
          dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME);
          if (dataSource == null) throw new ServletException("'" + Constants.DATASOURCE_NAME + "' is an unknown DataSource");
          conn = dataSource.getConnection();
          request.setAttribute( "userSessForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(request.getParameter("exit")!=null) return mapping.findForward("back");
/**********************************************************************/

        UserSessions sessions = UserSessions.manager;
        request.setAttribute("sessions", sessions.getUsers(true));

// Принудительная очистка брошенных сессий

///      UserSessions.manager.service(request,response);

// Подсчет времени активности пользователя
      StringBuffer active_sess= new StringBuffer("'-1'");
      Date cur_dat = new Date();

// Список активных сессий
      sessS = UserSessions.manager.getUsers(true);

      for(int i=0;i<sessS.size();i++){
         UserBean usb = new UserBean();
         usb = ((UserBean)sessS.get(i));
         if(usb == null) continue;
         cur_dat = new Date();
         stmt = conn.createStatement();
         stmt.execute("UPDATE user_stat SET TotalWorkTime=FLOOR(("+cur_dat.getTime()+"-LoginTime)/1000) WHERE IdStat LIKE "+usb.getSid());
         active_sess.append(","+usb.getSid());
      }
         stmt = conn.createStatement(); 
//         rs = stmt.executeQuery("SELECT us.IdStat FROM User_stat us WHERE us.CurWorkStatus LIKE 'д' AND us.IdStat NOT IN ("+active_sess.toString()+")");
         rs = stmt.executeQuery("SELECT us.IdStat,MAX(uss.TimeLoadMod) FROM User_stat us, User_sess uss WHERE us.IdStat=uss.IdStat AND us.CurWorkStatus LIKE 'д' AND us.IdStat NOT IN ("+active_sess.toString()+") GROUP BY us.IdStat");
         while (rs.next()) {

// Коррекция времени работы пользователя в системе и Сброс признака завершения сессии
          stmt = conn.createStatement();
//          stmt.execute("UPDATE User_stat SET CurWorkStatus='н' WHERE IdStat LIKE '"+rs.getLong(1)+"'");
          stmt.execute("UPDATE User_stat SET CurWorkStatus='н',LogoutTime="+rs.getLong(2)+" WHERE IdStat LIKE "+rs.getLong(1)+"");
         }
        if(form.getAction() == null) form.setAction("active");

        if(form.getAction().equals("active")) {

/////////////////////////
// Активные соединения //
/////////////////////////

          pstmt = conn.prepareStatement("SELECT us.IdUser,us.IpAdress,us.LoginTime,us.TotalWorkTime,us.CurWorkStatus,ut.Name,ut.Descr,grp.Type,us.IdStat FROM user_stat us,users_tbl ut, group_tbl grp WHERE grp.id=ut.group_id AND us.IdUser=ut.Id AND IdMonth LIKE ? AND CurWorkStatus LIKE 'д'");
          pstmt.setObject(1,""+(cur_dat.getMonth()+1),Types.INTEGER);
          rs = pstmt.executeQuery();
          while(rs.next()) {
            UserBean ubn = new UserBean();
            ubn.setName(rs.getString(6));
            if(rs.getString(8).equals("A"))
              ubn.setGruppa("Админ.");
            else if(rs.getString(8).equals("O"))
              ubn.setGruppa("Операт.");
            else ubn.setGruppa("Набл.");
            ubn.setDescr(rs.getString(7));
            ubn.setStatus(rs.getString(5));
            ubn.setUip(rs.getString(2));
            ubn.setDataLogin(StringUtil.getDateFromDT(rs.getLong(3)));
            ubn.setTimeLogin(StringUtil.getTimeFromDT(rs.getLong(3)));
            ubn.setTotalTime(StringUtil.StoHMS(rs.getLong(4)));
            ubn.setIdStat(new Integer(rs.getString(9)));

            stmt = conn.createStatement();
            rs2 = stmt.executeQuery("SELECT COUNT(us.IdSess) FROM User_sess us,User_stat ust WHERE ust.IdStat=us.IdStat AND us.IdStat LIKE "+rs.getString(9)+" AND ust.IdMonth LIKE "+(cur_dat.getMonth()+1));
            if(rs2.next()) 
              ubn.setKolZaprCur(rs2.getString(1));
            else 
              ubn.setKolZaprCur("N/A");

            stats.add(ubn);
          }

        } else if (form.getAction().equals("full_st")) {

/////////////////////////
//  Полная статистика  //
/////////////////////////

// Месяцы, в которые использовалась система

          stmt = conn.createStatement();
          rs = stmt.executeQuery("SELECT DISTINCT IdMonth FROM User_stat ORDER BY IdMonth ASC");
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setSpecial1(rs.getString(1));
            abit_TMP.setSpecial2(StringUtil.getMonthOnDigit(rs.getInt(1)));
            months.add(abit_TMP);
          }
          if(session.getAttribute("letter") == null) session.setAttribute("letter",""+(cur_dat.getMonth()+1));
          if(request.getParameter("letter")!=null) session.setAttribute("letter",request.getParameter("letter"));

// Чтение статистики

          pstmt = conn.prepareStatement("SELECT DISTINCT us.IdUser,SUM(us.TotalWorkTime),ut.Name,ut.Descr,grp.Type,grp.Id FROM user_stat us,users_tbl ut, group_tbl grp WHERE grp.id=ut.group_id AND us.IdUser=ut.Id AND IdMonth LIKE ? AND CurWorkStatus NOT LIKE 'д' GROUP BY us.IdUser,ut.Name,ut.Descr,grp.Type,grp.Id ORDER BY grp.Id ASC");
          pstmt.setObject(1,session.getAttribute("letter"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while(rs.next()) {
            UserBean ubn = new UserBean();
            ubn.setIdUser(new Integer(rs.getString(1)));
            ubn.setName(rs.getString(3));
            if(rs.getString(5).equals("A"))
              ubn.setGruppa("Админ.");
            else if(rs.getString(5).equals("O"))
              ubn.setGruppa("Операт.");
            else ubn.setGruppa("Набл.");
            ubn.setDescr(rs.getString(4));
            ubn.setTotalTime(StringUtil.StoHMS(rs.getLong(2)));

            stmt = conn.createStatement();
            rs2 = stmt.executeQuery("SELECT MAX(ust.LoginTime) FROM User_stat ust WHERE ust.IdUser LIKE "+rs.getString(1)+" AND ust.IdMonth LIKE "+session.getAttribute("letter"));
            if(rs2.next()) {
              ubn.setDataLogin(StringUtil.getDateFromDT(rs2.getLong(1)));
              ubn.setTimeLogin(StringUtil.getTimeFromDT(rs2.getLong(1)));
            }
            else {
              ubn.setDataLogin("-");
              ubn.setTimeLogin("-");
            }

            stmt = conn.createStatement();
            rs2 = stmt.executeQuery("SELECT COUNT(us.IdSess) FROM User_sess us,User_stat ust WHERE ust.IdStat=us.IdStat AND ust.IdUser LIKE "+rs.getString(1)+" AND ust.IdMonth LIKE "+session.getAttribute("letter"));
            if(rs2.next()) 
              ubn.setKolZapr(rs2.getString(1));
            else 
              ubn.setKolZapr("N/A");

            stmt = conn.createStatement();
            rs2 = stmt.executeQuery("SELECT COUNT(us.IdStat) FROM User_stat us WHERE us.IdUser LIKE "+rs.getString(1)+" AND us.IdMonth LIKE "+session.getAttribute("letter"));
            if(rs2.next()) 
              ubn.setKolLogin(rs2.getString(1));
            else 
              ubn.setKolLogin("N/A");

            stats.add(ubn);
          }
          form.setAction("statist");

        } else if (form.getAction().equals("get_detail")) {

/////////////////////////////////////////////////////////////
//  Детальная статистика о работе выбранного пользователя  //
/////////////////////////////////////////////////////////////

          pstmt = conn.prepareStatement("SELECT DISTINCT us.IdUser,us.TotalWorkTime,ut.Name,ut.Descr,grp.Type,us.LoginTime,us.LogoutTime,us.IpAdress,us.IdStat FROM user_stat us,users_tbl ut, group_tbl grp WHERE grp.id=ut.group_id AND us.IdUser=ut.Id AND IdMonth LIKE ? AND us.IdUser LIKE ? AND CurWorkStatus NOT LIKE 'д' ORDER BY us.LoginTime ASC");
          pstmt.setObject(1,session.getAttribute("letter"),Types.INTEGER);
          pstmt.setObject(2,request.getParameter("idUser"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while(rs.next()) {
            UserBean ubn = new UserBean();
            ubn.setIdUser(new Integer(rs.getString(1)));
            ubn.setIdStat(new Integer(rs.getString(9)));
            ubn.setUip(rs.getString(8));
            u_bean.setName(rs.getString(3));
            if(rs.getString(5).equals("A"))
              u_bean.setGruppa("Админ.");
            else if(rs.getString(5).equals("O"))
              u_bean.setGruppa("Операт.");
            else u_bean.setGruppa("Набл.");
            u_bean.setDescr(rs.getString(4));
            ubn.setTotalTime(StringUtil.StoHMS(rs.getLong(2)));
            ubn.setDataLogin(StringUtil.getDateFromDT(rs.getLong(6)));
            ubn.setTimeLogin(StringUtil.getTimeFromDT(rs.getLong(6)));
            ubn.setDataLogout(StringUtil.getDateFromDT(rs.getLong(7)));
            ubn.setTimeLogout(StringUtil.getTimeFromDT(rs.getLong(7)));

            stmt = conn.createStatement();
            rs2 = stmt.executeQuery("SELECT COUNT(us.IdSess) FROM User_sess us,User_stat ust WHERE ust.IdStat=us.IdStat AND ust.IdStat LIKE "+rs.getString(9)+" AND ust.IdMonth LIKE "+session.getAttribute("letter"));
            if(rs2.next()) 
              ubn.setKolZapr(rs2.getString(1));
            else 
              ubn.setKolZapr("N/A");

            stats.add(ubn);
          }
          form.setAction("detail");
        } else if (form.getAction().equals("get_fulld")) {

/////////////////////////////////////////////////////////////
//  Детальная статистика о работе выбранного пользователя  //
/////////////////////////////////////////////////////////////

          int num = 1;
          pstmt = conn.prepareStatement("SELECT DISTINCT us.IdSess,us.TimeLoadMod,us.ModuleName,us.Action,ut.Name FROM User_sess us, User_stat ust,Users_tbl ut WHERE ut.Id=ust.IdUser AND us.IdStat=ust.IdStat AND us.IdStat LIKE ? ORDER BY us.TimeLoadMod ASC");
          pstmt.setObject(1,request.getParameter("idStat"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while(rs.next()) {
            UserBean ubn = new UserBean();
            u_bean.setDataLogin(StringUtil.getDateFromDT(rs.getLong(2)));
            ubn.setSpecial1(""+(num++));
            ubn.setTimeLogin(StringUtil.getTimeFromDT(rs.getLong(2)));
            ubn.setName(rs.getString(3));
            ubn.setDescr(rs.getString(4));
            u_bean.setImja(rs.getString(5));
            stats.add(ubn);
          }
          form.setAction("full_det");
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
          if ( rs2 != null ) {
               try {
                     rs2.close();
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
          if ( pstmt != null ) {
               try {
                     pstmt.close();
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
        request.setAttribute("u_bean", u_bean);
        request.setAttribute("stats", stats);
        request.setAttribute("months", months);
     }
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}