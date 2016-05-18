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
import abit.Constants;
import abit.util.*;
import abit.sql.*; 

public class UsersAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession        session      = request.getSession();
        Connection         conn         = null;
        PreparedStatement  stmt         = null;
        ResultSet          rs           = null;
        ActionErrors       errors       = new ActionErrors();
        ActionError        msg          = null;
        UsersForm          form         = (UsersForm) actionForm;
        AbiturientBean     u_bean       = form.getBean(request, errors);
        boolean            users_f      = false;
        boolean            error        = false;
        ActionForward      f            = null;
        int                kGr          = 1;
        ArrayList          groups       = new ArrayList();
        ArrayList          administrat  = new ArrayList();
        ArrayList          operats      = new ArrayList();
        ArrayList          operats_inp  = new ArrayList();
        ArrayList          others       = new ArrayList();
        ArrayList          abits_Gr     = new ArrayList();
        ArrayList          abit_G_S2    = new ArrayList();
        UserBean           user         = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "usersAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "usersForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            stmt = conn.prepareStatement("SELECT id, name, type FROM group_tbl ORDER BY 2");
            rs = stmt.executeQuery();
            while(rs.next()){
               AbiturientBean tmp = new AbiturientBean();
               tmp.setKodGruppy(new Integer(rs.getString(1)));
               tmp.setUserName(rs.getString(2));
               groups.add(tmp);
            }

/************************************************************************************************/

            if ( form.getAction() == null || request.getParameter("full")!=null) 
                 form.setAction(us.getClientIntName("full","init"));

            if ( form.getAction().equals("create") || form.getAction().equals("full") ) {

/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

                if ( request.getParameter("full") == null &&
                             form.getAction().equals("create") ) {
                int next_id = 1;
                stmt = conn.prepareStatement("SELECT MAX(id) FROM users_tbl");
                rs = stmt.executeQuery();
                if(rs.next()) next_id=rs.getInt(1)+1;

                stmt = conn.prepareStatement("INSERT INTO users_tbl(id,name,group_id,pass,descr,famil,imja,otch) VALUES(?,?,?,?,?,?,?,?)");
                stmt.setObject(1,""+next_id,Types.INTEGER);
                stmt.setObject(2,u_bean.getUserName(),Types.VARCHAR);
                stmt.setObject(3,u_bean.getKodGruppy(),Types.INTEGER);
                stmt.setObject(4,""+u_bean.getPassword().hashCode(),Types.VARCHAR);
                stmt.setObject(5,u_bean.getDescription(),Types.VARCHAR);
                stmt.setObject(6,u_bean.getFamilija(),Types.VARCHAR);
                stmt.setObject(7,u_bean.getImja(),Types.VARCHAR);
                stmt.setObject(8,u_bean.getOtchestvo(),Types.VARCHAR);
                stmt.executeUpdate();

                form.setAction(us.getClientIntName("new","create"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
// Выборка пользователей
// Администраторы
                stmt = conn.prepareStatement("SELECT u.id,u.name,u.descr,famil,imja,otch FROM users_tbl u,group_tbl g WHERE u.group_id=g.id AND g.type LIKE 'a'");
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setSpecial11(new Integer(rs.getString(1)));
                  tmp.setSpecial1(rs.getString(2));
                  tmp.setDescription(rs.getString(3));
                  tmp.setFamilija(rs.getString(4));
                  tmp.setImja(rs.getString(5));
                  tmp.setOtchestvo(rs.getString(6));
                  administrat.add(tmp);
                }
// Операторы
                stmt = conn.prepareStatement("SELECT u.id,u.name,u.descr,famil,imja,otch FROM users_tbl u,group_tbl g WHERE u.group_id=g.id AND g.type LIKE 'o'");
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setSpecial11(new Integer(rs.getString(1)));
                  tmp.setSpecial1(rs.getString(2));
                  tmp.setDescription(rs.getString(3));
                  tmp.setFamilija(rs.getString(4));
                  tmp.setImja(rs.getString(5));
                  tmp.setOtchestvo(rs.getString(6));
                  operats.add(tmp);
                }

// Операторы ввода
                stmt = conn.prepareStatement("SELECT u.id,u.name,u.descr,famil,imja,otch FROM users_tbl u,group_tbl g WHERE u.group_id=g.id AND g.type LIKE 'i'");
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setSpecial11(new Integer(rs.getString(1)));
                  tmp.setSpecial1(rs.getString(2));
                  tmp.setDescription(rs.getString(3));
                  tmp.setFamilija(rs.getString(4));
                  tmp.setImja(rs.getString(5));
                  tmp.setOtchestvo(rs.getString(6));
                  operats_inp.add(tmp);
                }
// Другие
                stmt = conn.prepareStatement("SELECT u.id,u.name,u.descr,famil,imja,otch FROM users_tbl u,group_tbl g WHERE u.group_id=g.id AND g.type LIKE 'd'");
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setSpecial11(new Integer(rs.getString(1)));
                  tmp.setSpecial1(rs.getString(2));
                  tmp.setDescription(rs.getString(3));
                  tmp.setFamilija(rs.getString(4));
                  tmp.setImja(rs.getString(5));
                  tmp.setOtchestvo(rs.getString(6));
                  others.add(tmp);
                }
              }
/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del")) {

                stmt = conn.prepareStatement("SELECT id,name,group_id,descr,famil,imja,otch FROM users_tbl WHERE id LIKE ?");
                stmt.setObject(1, request.getParameter("id"),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  u_bean.setKodAbiturienta(new Integer(rs.getString(1)));
                  u_bean.setUserName(rs.getString(2));
                  u_bean.setKodGruppy(new Integer(rs.getString(3)));
                  u_bean.setDescription(rs.getString(4));
                  u_bean.setFamilija(rs.getString(5));
                  u_bean.setImja(rs.getString(6));
                  u_bean.setOtchestvo(rs.getString(7));
                }

                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null) {
             
               if(u_bean.getSpecial3() == null) {
                  form.setAction(us.getClientIntName("change","with-upd-pass"));
// Без смены пароля
                  stmt = conn.prepareStatement("UPDATE users_tbl SET famil=?,imja=?,otch=?,name=?,group_id=?,Descr=? WHERE id LIKE ?");
                  stmt.setObject(1, u_bean.getFamilija(),Types.VARCHAR);
                  stmt.setObject(2, u_bean.getImja(),Types.VARCHAR);
                  stmt.setObject(3, u_bean.getOtchestvo(),Types.VARCHAR);
                  stmt.setObject(4, u_bean.getUserName(),Types.VARCHAR);
                  stmt.setObject(5, u_bean.getKodGruppy(),Types.INTEGER);
                  stmt.setObject(6, u_bean.getDescription(),Types.VARCHAR);
                  stmt.setObject(7, u_bean.getKodAbiturienta(),Types.INTEGER);
                  stmt.executeUpdate();
               } else{
// Со сменой пароля
                  form.setAction(us.getClientIntName("change","no-upd-pass")); 
                  stmt = conn.prepareStatement("UPDATE users_tbl SET famil=?,imja=?,otch=?,name=?,group_id=?,pass=?,Descr=? WHERE id LIKE ?");
                  stmt.setObject(1, u_bean.getFamilija(),Types.VARCHAR);
                  stmt.setObject(2, u_bean.getImja(),Types.VARCHAR);
                  stmt.setObject(3, u_bean.getOtchestvo(),Types.VARCHAR);
                  stmt.setObject(4, u_bean.getUserName(),Types.VARCHAR);
                  stmt.setObject(5, u_bean.getKodGruppy(),Types.INTEGER);
                  stmt.setObject(6, ""+u_bean.getPassword().hashCode(),Types.VARCHAR);
                  stmt.setObject(7, u_bean.getDescription(),Types.VARCHAR);
                  stmt.setObject(8, u_bean.getKodAbiturienta(),Types.INTEGER);
                  stmt.executeUpdate();
               }
                  users_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM users_tbl WHERE id LIKE ? AND su IS NULL");
                stmt.setObject(1, u_bean.getKodAbiturienta(),Types.INTEGER);
                stmt.executeUpdate();
                users_f  = true;
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
        request.setAttribute("u_bean", u_bean);
        request.setAttribute("groups", groups);
        request.setAttribute("abits_Gr", abits_Gr);
        request.setAttribute("abit_G_S2", abit_G_S2);
        request.setAttribute("operats", operats);
        request.setAttribute("operats_inp", operats_inp);
        request.setAttribute("others", others);
        request.setAttribute("administrat", administrat);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(users_f) return mapping.findForward("users_f");
        return mapping.findForward("success");
    }
}