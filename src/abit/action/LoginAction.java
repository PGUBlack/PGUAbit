package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import abit.util.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.sql.*;
import javax.naming.*;
import abit.bean.*;
import abit.Constants;
import abit.servlet.UserSessions; 
import java.util.Date;

public class LoginAction extends Action {

    public ActionForward perform
    (
        ActionMapping       mapping,
        ActionForm          actionForm,
        HttpServletRequest  request,
        HttpServletResponse response
    )
    throws IOException, ServletException
    {
        HttpSession       session       = request.getSession();
        DataSource        dataSource    = null;
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        ResultSet         rs            = null;
        ActionErrors      errors        = new ActionErrors();
        LoginForm         form          = (LoginForm) actionForm;
        LoginBean         login         = form.getBean(request,errors);
        ArrayList         uGroups       = new ArrayList();
        ArrayList         vuzy          = new ArrayList();
        LoginBean         abit          = form.getBean(request, errors);
        Date              date          = new Date();
        boolean           news          = false;
        boolean           index         = false;
        boolean           loginT        = false;

        request.setAttribute("loginAction", new Boolean(true));
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {        
           Context env = (Context) new InitialContext().lookup("java:comp/env");
           dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME);
           if (dataSource == null) throw new ServletException("'" + Constants.DATASOURCE_NAME + "' is an unknown DataSource");
           conn = dataSource.getConnection();

           request.setAttribute("loginForm", form);

             stmt = conn.prepareStatement("SELECT id,name FROM group_tbl ORDER BY Ordr ASC");
             rs = stmt.executeQuery();
             while (rs.next()) {
                LoginBean ugr = new LoginBean();
                ugr.setId(new Integer(rs.getString(1)));
                ugr.setUserGroup(rs.getString(2));
                uGroups.add(ugr);
             }
             stmt = conn.prepareStatement("SELECT KodVuza,AbbreviaturaVuza,NazvanieVuza FROM NazvanieVuza ORDER BY KodVuza ASC");
             rs = stmt.executeQuery();
             while (rs.next()) {
                AbiturientBean vuz = new AbiturientBean();
                vuz.setKodVuza(new Integer(rs.getString(1)));
                vuz.setAbbreviaturaVuza(rs.getString(2));
                vuzy.add(vuz);
             }


            if ( form.getAction() == null ) {
                 form.setAction("login");
                 loginT = true;
            } else if ( form.getAction().equals("logging") ) {
// ***************************************************************
        //        if(request.isSecure()) { //Ололо, убрать true!

// Опеределение типа браузера
                  String type_brw = request.getHeader("user-agent");
                //  if(type_brw != null && ((type_brw.indexOf("MSIE") == -1) && type_brw.indexOf("rv 11.0") == -1)) return mapping.findForward("noIE");
                  stmt = conn.prepareStatement("SELECT u.id,u.name,g.id, g.name, g.type, u.IdTema FROM users_tbl u LEFT JOIN group_tbl g ON u.group_id=g.id WHERE g.id LIKE ? AND u.name LIKE ? AND u.pass LIKE ?");
                  stmt.setObject(1,abit.getId(),Types.INTEGER);
                  stmt.setObject(2,abit.getUserName(),Types.VARCHAR);
                  stmt.setObject(3,""+abit.getPassword().hashCode(),Types.VARCHAR);
                  rs = stmt.executeQuery();
                  if (rs.next()) {
                    UserBean user = new UserBean();
                    GroupBean u_grp = new GroupBean();
                    u_grp.setType(rs.getString(5));
                    u_grp.setGroupName(rs.getString(4));
                    user.setUip(request.getRemoteAddr());
                    user.setIdTema(rs.getString(6));
                    user.setUid(new Integer(rs.getString(1)));
                    user.setName(rs.getString(2));
                    user.setGroup(u_grp);
                    user.setPass(abit.getPassword());

                    stmt = conn.prepareStatement("INSERT INTO user_stat(IdMonth,IdUser,IpAdress,LoginTime,LogoutTime,TotalWorkTime,CurWorkStatus) VALUES (?,?,?,?,?,?,?)");
                    stmt.setObject(1,new Integer(date.getMonth()+1),Types.INTEGER);
                    stmt.setObject(2,user.getUid(),Types.INTEGER);
                    stmt.setObject(3,user.getUip(),Types.VARCHAR);
                    stmt.setLong(4,date.getTime());
                    stmt.setLong(5,date.getTime());
                    stmt.setObject(6,new Integer(0),Types.INTEGER);
                    stmt.setObject(7,"д",Types.VARCHAR);

                    stmt.executeUpdate();
                    stmt = conn.prepareStatement("SELECT IDENT_CURRENT('user_stat')");
                    rs = stmt.executeQuery();
                    if(rs.next()) user.setSid(rs.getLong(1));
                    session.setAttribute("user",user);
                    session.setAttribute("kVuza",abit.getKodVuza());
                    UserSessions.manager.addSession(session);

// Начало новой сессии пользователя
                    stmt = conn.prepareStatement("SELECT ut.ViewNews FROM Users_tbl ut,Group_tbl gt WHERE ut.group_id=gt.Id AND ut.Id LIKE ? AND gt.Type NOT LIKE 'A'");
                    stmt.setObject(1,user.getUid(),Types.INTEGER);
                    rs = stmt.executeQuery();
                    if(rs.next()) { 

// Переход к странице новостей

                      if(rs.getString(1).equals("д")) news = true; 

                    } else news = true; 

                    index = true;
                  }
               // } else {
             //       return mapping.findForward("needSSL");
               // }

            }
        }
     catch (SQLException e) {
            request.setAttribute("SQLException", e);
            return mapping.findForward("error");

      } 
     catch (NamingException e) {
            request.setAttribute("SQLException", e);
            return mapping.findForward("error");

      } 
     catch (java.lang.Exception e) {
            request.setAttribute("JAVAException", e);
            return mapping.findForward("error");

      } finally {
            if (rs!=null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    ;
                }
            }
            if (stmt!=null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    ;
                }
            }
            if (conn!=null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    ;
                }
            }
        }
        request.setAttribute("uGroups", uGroups);
        request.setAttribute("vuzy", vuzy);
        request.setAttribute("abit", abit);

        if(news) return mapping.findForward("news");
        if(index) return mapping.findForward("index");
        if(loginT) return mapping.findForward("success");
        return mapping.findForward("error");
    }
}