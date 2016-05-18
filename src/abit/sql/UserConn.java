package abit.sql;

import java.util.*;
import java.io.IOException;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForward;
import abit.Constants;
import java.util.Date;

public final class UserConn {

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

  public UserConn(HttpServletRequest rqst, org.apache.struts.action.ActionMapping mpg) throws ServletException, SQLException {

    try {        

      Context env = (Context) new InitialContext().lookup("java:comp/env");

      dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME);

      if (dataSource == null) throw new ServletException("'" + Constants.DATASOURCE_NAME + "' is an unknown DataSource");

      conn = dataSource.getConnection();

      request = rqst;

      session = rqst.getSession();

      mapping = mpg;

    } catch (NamingException e) {

      throw new ServletException(e);
    }
  }

  public Connection getConn(long sid) throws IllegalStateException, SQLException {

        Date date = new Date();

        String modName = mapping.getType();

        if(modName != null && modName.length()>1) {
          modName = modName.substring(modName.lastIndexOf(".")+1,modName.indexOf("Action"));
          if(modName.length()>20) modName = modName.substring(0,19);
        }
        else 
          modName = "?";

        try {
              pstmt = conn.prepareStatement("INSERT INTO user_sess(IdStat,TimeLoadMod,ModuleName) VALUES(?,?,?)");
              pstmt.setObject(1,new Integer(""+sid),Types.INTEGER);
              pstmt.setLong(2,date.getTime());
              pstmt.setObject(3,modName,Types.VARCHAR);
              pstmt.executeUpdate();

              stmt = conn.createStatement();
              rs = stmt.executeQuery("SELECT IDENT_CURRENT('user_sess')");
              if(rs.next()) request.setAttribute("sid",rs.getString(1));
              else request.setAttribute("sid","0");

        } catch(SQLException e) {

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
    return conn;
  }

// get Client Interface Name to Servlet for it know that Client Section 
// must loaded after the end work of Servlet

  public String getClientIntName(String action,String add_info) throws SQLException, java.lang.Exception {
    if(request.getAttribute("sid") != null) {
    long sid = (new Long(request.getAttribute("sid").toString())).longValue();
      String tmp = action;
      if(add_info != null && add_info != "") tmp += ":"+add_info;

      if(tmp.length()>20) tmp = tmp.substring(0,19);

      stmt = conn.createStatement();
      stmt.execute("UPDATE user_sess SET Action='"+tmp+"' WHERE IdSess LIKE '"+sid+"'");

    }
    return action;
  }

  public boolean quit(String param) throws SQLException, java.lang.Exception {
    if (request.getParameter(param)!=null) { 
       if(request.getAttribute("sid") != null) {
         long sid = (new Long(request.getAttribute("sid").toString())).longValue();
         stmt = conn.createStatement();
         stmt.execute("UPDATE user_sess SET Action='quit' WHERE IdSess LIKE '"+sid+"'");
       }
      return true; 
    }
    else return false;
  }
}
