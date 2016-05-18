package abit.servlet;

import java.util.*;
import java.io.IOException;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForward;
import abit.Constants;
import abit.bean.*;
import java.util.Date;
import org.quartz.*;

public final class FixStatistics extends GenericServlet  {
	
//	 Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
  public FixStatistics() { }

  private ArrayList                              sessS       = new ArrayList();  
  public javax.sql.DataSource                    dataSource  = null;
  public java.sql.Connection                     conn        = null;
  public java.sql.PreparedStatement              pstmt       = null;
  public java.sql.Statement                      stmt        = null;
  public java.sql.ResultSet                      rs          = null;
  public org.apache.struts.action.ActionMapping  mapping     = null;
  public org.apache.struts.action.ActionForm     actionForm  = null;
  public javax.servlet.http.HttpServletRequest   request     = null;
  public javax.servlet.http.HttpServletResponse  response    = null;
  public javax.servlet.http.HttpSession          session     = null; 

  /*public void execute(JobExecutionContext ctx)
  {
  	try 
  	{
  		service(request, response);
  		
  	} catch (Exception e)
  	{
  		System.out.println("Error in [abit]FixStatistics.execute(JobExecutionContext ctx)");
  		e.printStackTrace();
  	}
  }*/  
  
// Сервисная функция
public void service(ServletRequest request, ServletResponse response)
    throws IOException, ServletException {
	
    try {

      Context env = (Context) new InitialContext().lookup("java:comp/env");

      dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME);

      if (dataSource == null) throw new ServletException("'" + Constants.DATASOURCE_NAME + "' is an unknown DataSource");

      conn = dataSource.getConnection();

// Подсчет времени активности пользователя
      StringBuffer active_sess= new StringBuffer("'-1'");
      Date cur_dat = new Date();

///      UserSessions.manager.service(request,response);

// Очистка хранилища сессий
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
         rs = stmt.executeQuery("SELECT us.IdStat FROM User_stat us WHERE us.CurWorkStatus LIKE 'д' AND us.IdStat NOT IN ("+active_sess.toString()+")");
//         rs = stmt.executeQuery("SELECT us.IdStat,MAX(uss.TimeLoadMod) FROM User_stat us, User_sess uss WHERE us.IdStat=uss.IdStat AND us.CurWorkStatus LIKE 'д' AND us.IdStat NOT IN ("+active_sess.toString()+") GROUP BY us.IdStat");
         while (rs.next()) {

// Коррекция времени работы пользователя в системе и Сброс признака завершения сессии
          stmt = conn.createStatement();
          stmt.execute("UPDATE User_stat SET CurWorkStatus='н' WHERE IdStat LIKE '"+rs.getLong(1)+"'");
//          stmt.execute("UPDATE User_stat SET CurWorkStatus='н',TotalWorkTime=FLOOR(("+rs.getLong(2)+"-LoginTime)/1000),LogoutTime="+rs.getLong(2)+" WHERE IdStat LIKE "+rs.getLong(1)+"");
         }
    } catch (NamingException e) {

      throw new ServletException(e);
    }
      catch (SQLException e) {

      throw new ServletException(e);
    }
	finally {
       if ( rs != null ) {
         try {
               rs.close();
             } catch (Exception ex) {
              ;
                                    }
         }
       if ( stmt != null ) {
         try {
               stmt.close();
             } catch (Exception ex) {
              ;
                                    }
       }
       if ( pstmt != null ) {
         try {
               pstmt.close();
             } catch (Exception ex) {
              ;
                                    }
       }
       if ( conn != null ) {
         try {
               conn.close();
             } catch (Exception ex) {
              ;
                                    }
       }
    }
  }
}