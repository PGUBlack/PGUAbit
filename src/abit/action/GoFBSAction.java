package abit.action;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import java.net.*;
import abit.bean.*;
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

public class GoFBSAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession        session     = request.getSession();
        Connection         conn        = null;
        PreparedStatement  stmt        = null;
        ResultSet          rs          = null;
        ActionErrors       errors      = new ActionErrors();
        ActionError        msg         = null;
        GoFBSForm          form        = (GoFBSForm) actionForm;
        AbiturientBean     abit_ba     = form.getBean(request, errors);
        boolean            error       = false;
        ActionForward      f           = null;
        int                kBa         = 1;
        ArrayList          abits_ba    = new ArrayList();
        UserBean           user        = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
                     
        request.setAttribute( "goFBSAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/*************************************************************/
/*************  Получение соединения с БД ФБС  ***************/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());

          request.setAttribute( "goFBSForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/**********************************************************************/
/***   Установка соединения с Федеральной базой свидетельств ЕГЭ   ****/
/**********************************************************************/

  if ( form.getAction() == null) {

System.out.println(">>FBS_Connection: 1 - ОК");

  } 

  if ( form.getAction().equals("view") ) {

/***************************************************************/
/************* Установка соединения с ФБС ЕГЭ ******************/
/***************************************************************/


    URL url = new URL("http://fbsege.ru/Login.aspx");

    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

    connection.setRequestMethod("POST");

    //connection.setDoInput(true);

    connection.setDoOutput(true);

    connection.setUseCaches(false);

    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

    String web_data = URLEncoder.encode("login", Constants.WEB_ENCODING) + "=" + URLEncoder.encode("user5391", Constants.WEB_ENCODING);

    web_data += "&" + URLEncoder.encode("password", Constants.WEB_ENCODING) + "=" + URLEncoder.encode("vBGephi", Constants.WEB_ENCODING);

    out.write(web_data);

    out.flush();

    out.close();
        
    BufferedReader br = new BufferedReader( new InputStreamReader(connection.getInputStream()));

    while (br.ready()) {
      System.out.println(">polex>>"+br.readLine());
    }

    br.close();

    connection.disconnect();


/***************************************************************/
/***************** Соединение установлено **********************/
/***************************************************************/

     String query = null;

     stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,SeriaDokumenta,NomerDokumenta,NomerSertifikata FROM Abiturient WHERE KodAbiturienta LIKE ?");
     stmt.setObject(1, request.getParameter("kodAbiturienta"),Types.INTEGER);
     rs = stmt.executeQuery();
     if(rs.next()) {
       
       if(rs.getString(6) == null) {

          query="http://fbsege.ru/Certificates/CommonNationalCertificates/RequestByPassportResult.aspx?LastName="+StringUtil.toApp(rs.getString(1))+"&FirstName="+StringUtil.toApp(rs.getString(2))+"&PatronymicName="+StringUtil.toApp(rs.getString(3))+"&Series="+rs.getString(4)+"&Number="+rs.getString(5);

       } else {
          query="http://fbsege.ru/Certificates/CommonNationalCertificates/CheckResult.aspx?number="+rs.getString(6)+"&LastName="+StringUtil.toApp(rs.getString(1))+"&FirstName="+StringUtil.toApp(rs.getString(2))+"&PatronymicName="+StringUtil.toApp(rs.getString(3))+"&SubjectMarks=&Year=";
//          query="http://fbsege.ru/Certificates/CommonNationalCertificates/CheckResult.aspx?number="+rs.getString(6)+"&LastName="+rs.getString(1);
       }
     } 

    abit_ba.setSpecial1(query);

    url = new URL(query);

    connection = (HttpURLConnection)url.openConnection();

    connection.setRequestMethod("POST");

    //connection.setDoInput(true);

    connection.setDoOutput(true);

    connection.setUseCaches(false);

    out = new OutputStreamWriter(connection.getOutputStream());

    web_data = URLEncoder.encode("login", Constants.WEB_ENCODING) + "=" + URLEncoder.encode("user5391", Constants.WEB_ENCODING);

    web_data += "&" + URLEncoder.encode("password", Constants.WEB_ENCODING) + "=" + URLEncoder.encode("vBGephi", Constants.WEB_ENCODING);

    out.write(web_data);

    out.flush();

    out.close();

//    response.flushBuffer();

    response.setContentType("text/html;charset=UTF-8");

    PrintWriter to_client = response.getWriter();
     
    br = new BufferedReader( new InputStreamReader(connection.getInputStream()));

    while (br.ready()) {

      to_client.print(br.readLine());
    }

    br.close();

    connection.disconnect();
/*

    PrintWriter to_client = response.getWriter();

    to_client.print(connection.getInputStream());
*/

    form.setAction(us.getClientIntName("getInfo","act"));

/**********************************************************************/

  } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {

     form.setAction(us.getClientIntName("change","act"));


/**********************************************************************/

  } else if ( request.getParameter("delete") != null ) {
 
     form.setAction(us.getClientIntName("delete","act"));
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
        request.setAttribute("abit_ba", abit_ba);
        request.setAttribute("abits_ba", abits_ba);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}