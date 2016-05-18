package abit.action;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Enumeration; 
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*;

public class ReportsBrowserAction extends Action {

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
        ReportsBrowserForm      form       = (ReportsBrowserForm) actionForm;
        ReportsBrowserBean      report     = form.getBean(request, errors);
        boolean                 twins_f    = false;
        boolean                 error      = false;
        ActionForward           f          = null;
        ArrayList               reports    = new ArrayList();
        UserBean                user       = (UserBean)session.getAttribute("user");
        boolean                 view       = false;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "reportsBrowserAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "reportsBrowserForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************************************************************************************************/

            if ( form.getAction() == null || form.getAction().equals("old")) {

             if(request.getParameter("del")!=null){
// Удаление файлов из Браузера отчетов(БД) и с жесткого диска
               form.setAction(us.getClientIntName("old","delete"));
               String base_path = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\";

               Enumeration paramNames = request.getParameterNames();
               while(paramNames.hasMoreElements()) {
                String paramName = (String)paramNames.nextElement();
                if(paramName.indexOf("r_w") != -1) {
                    stmt = conn.prepareStatement("SELECT DISTINCT FileName FROM ReportsBrowser WHERE Id LIKE ? AND KodVuza LIKE ? AND OwnerId LIKE ?");
                    stmt.setObject(1,paramName.substring(3),Types.INTEGER);
                    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(3,user.getUid()+"",Types.INTEGER);
                    rs = stmt.executeQuery();
                    if(rs.next()) {
                      File need_to_delete = new File(base_path + rs.getString(1));
                    
// Если файл успешно удален с диска, удаляем также запись о нем из БД
                      if(need_to_delete.delete() || !need_to_delete.exists()) {
                        stmt = conn.prepareStatement("DELETE FROM ReportsBrowser WHERE Id LIKE ? AND KodVuza LIKE ? AND Unvisibility IS NULL AND OwnerId LIKE ?");
                        stmt.setObject(1,paramName.substring(3),Types.INTEGER);
                        stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                        stmt.setObject(3,user.getUid()+"",Types.INTEGER);
                        stmt.executeUpdate();
                      }
                    }
                }
               }
             }
              view = true;
            } else if(form.getAction().equals("vta")) {

// Изменение атрибута доступности
               form.setAction(us.getClientIntName("vta","change-attr"));
               Enumeration paramNames = request.getParameterNames();
               while(paramNames.hasMoreElements()) {
                String paramName = (String)paramNames.nextElement();
                if(paramName.indexOf("r_w") != -1) {
                    stmt = conn.prepareStatement("SELECT ViewToAll FROM ReportsBrowser WHERE OwnerId LIKE ? AND Id LIKE ? AND KodVuza LIKE ?");
                    stmt.setObject(1,user.getUid()+"",Types.INTEGER);
                    stmt.setObject(2,paramName.substring(3),Types.INTEGER);
                    stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
                    rs = stmt.executeQuery();
                    if(rs.next()){
                      if(rs.getString(1) != null) {
                        stmt = conn.prepareStatement("UPDATE ReportsBrowser SET ViewToAll=? WHERE OwnerId LIKE ? AND Id LIKE ? AND KodVuza LIKE ?");
                        stmt.setNull(1,Types.INTEGER);
                        stmt.setObject(2,user.getUid()+"",Types.INTEGER);
                        stmt.setObject(3,paramName.substring(3),Types.INTEGER);
                        stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                        stmt.executeUpdate();
                      } else {
                        stmt = conn.prepareStatement("UPDATE ReportsBrowser SET ViewToAll=1 WHERE OwnerId LIKE ? AND Id LIKE ? AND KodVuza LIKE ?");
                        stmt.setObject(1,user.getUid()+"",Types.INTEGER);
                        stmt.setObject(2,paramName.substring(3),Types.INTEGER);
                        stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
                        stmt.executeUpdate();
                        report.setViewToAll("1");
                      }
                    }
                }
               }
              view = true;
            } else if(form.getAction().equals("new")) {

                 ReportsBrowserBean rpt = (ReportsBrowserBean)session.getAttribute("rpt");
                 report = rpt;
                 form.setAction(us.getClientIntName("new_rep","add-act"));

                   if(rpt.getName()!=null) {
                     stmt = conn.prepareStatement("SELECT * FROM ReportsBrowser WHERE FileName LIKE ?");
                     stmt.setObject(1,rpt.getFileName(),Types.VARCHAR);
                     rs = stmt.executeQuery();
                     if(rs.next()) {
                      stmt = conn.prepareStatement("DELETE FROM ReportsBrowser WHERE FileName LIKE ? AND KodVuza LIKE ?");
                      stmt.setObject(1,rpt.getFileName(),Types.VARCHAR);
                      stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                      stmt.executeUpdate();
                     } 
                     int k_dok=1;
                     stmt = conn.prepareStatement("SELECT MAX(id) FROM ReportsBrowser");
                     rs = stmt.executeQuery();
                     if(rs.next()) k_dok = rs.getInt(1)+1;

                     stmt = conn.prepareStatement("INSERT INTO ReportsBrowser(id,KodVuza,OwnerId,Name,FileName,Date,Time,Author) VALUES(?,?,?,?,?,?,?,?)");

//Для MS SQL Server 2000
                     stmt.setObject(1,k_dok+"",Types.INTEGER);
                     stmt.setObject(2,session.getAttribute("kVuza")+"",Types.INTEGER);
                     stmt.setObject(3,user.getUid()+"",Types.INTEGER);
                     if(rpt.getName().length()>150)
                       stmt.setObject(4,rpt.getName().substring(0,149),Types.VARCHAR);
                     else
                       stmt.setObject(4,rpt.getName(),Types.VARCHAR);
                     if(rpt.getFileName().length()>150)
                       stmt.setObject(5,rpt.getFileName().substring(0,149),Types.VARCHAR);
                     else
                       stmt.setObject(5,rpt.getFileName(),Types.VARCHAR);
                     stmt.setObject(6,rpt.getDate(),Types.VARCHAR);
                     stmt.setObject(7,rpt.getTime(),Types.VARCHAR);
                     if((user.getName()+"").length()>50)
                       stmt.setObject(8,(user.getName()+"").substring(0,49),Types.VARCHAR);
                     else
                       stmt.setObject(8,user.getName(),Types.VARCHAR);
                     stmt.executeUpdate();
                   }

            } else if(form.getAction().equals("delete")){

               form.setAction(us.getClientIntName("delete","act"));
/*********************************************************************************************/
/****************************** Удаление отмеченных отчетов из БД ****************************/
              
            } 

           if(view){
// Вывод списка файлов отчетов на экран JSP-страницы
                 int number=0;
                 form.setAction(us.getClientIntName("old_rep","view"));

                 String all_rows = new String();
                 stmt = conn.prepareStatement("SELECT Name,FileName,Date,Time,Author,Id,ViewToAll FROM ReportsBrowser WHERE Unvisibility IS NULL AND KodVuza LIKE ? AND (OwnerId LIKE ? OR ViewToAll IS NOT NULL) ORDER BY Id DESC,Author,Name");
                 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 stmt.setObject(2,user.getUid()+"",Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   ReportsBrowserBean reportTMP = new ReportsBrowserBean();
                   reportTMP.setNumber(new Integer(++number+""));
                   reportTMP.setName(rs.getString(1));
                   reportTMP.setFileName(rs.getString(2));
                   reportTMP.setDate(rs.getString(3));
                   reportTMP.setTime(rs.getString(4));
                   reportTMP.setAuthor(rs.getString(5));
                   reportTMP.setId("r_w"+rs.getString(6));
                   reportTMP.setViewToAll(rs.getString(7));
                   reports.add(reportTMP);
                   all_rows += "%r_w"+rs.getString(6);
                 }
                 report.setSpecial1(all_rows);
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
        request.setAttribute("report", report);
        request.setAttribute("reports", reports);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}