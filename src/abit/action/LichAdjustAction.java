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

public class LichAdjustAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession        session        = request.getSession();
        Connection         conn           = null;
        PreparedStatement  stmt           = null;
        ResultSet          rs             = null;
        ActionErrors       errors         = new ActionErrors();
        ActionError        msg            = null;
        LichAdjustForm     form           = (LichAdjustForm) actionForm;
        LichAdjustBean     ladj           = form.getBean(request, errors);
        boolean            lich_adj_f     = false;
        boolean            error          = false;
        int                kGr            = 1;
        ArrayList          themes         = new ArrayList();
        ArrayList          administrat    = new ArrayList();
        ArrayList          operats        = new ArrayList();
        ArrayList          others         = new ArrayList();
        ArrayList          studs_Gr       = new ArrayList();
        ArrayList          stud_G_S2      = new ArrayList();
        UserBean           user           = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }
        if ( errors.empty() ) {

        request.setAttribute( "lich_adjAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "lich_adjForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          stmt = conn.prepareStatement("SELECT idTema, Folder, Nazvanie, Resolution,Descr,Ordr FROM TemaInt ORDER BY Ordr ASC");
          rs = stmt.executeQuery();
          while(rs.next()){
             LichAdjustBean tmp = new LichAdjustBean();
             tmp.setIdTema(new Integer(rs.getString(1)));
             tmp.setFolder(rs.getString(2));
             tmp.setNazv(rs.getString(3));
             tmp.setResolution(rs.getString(4));
             tmp.setDescr(rs.getString(5));
             themes.add(tmp);
          }

          if ( form.getAction() == null ) { 
           form.setAction(us.getClientIntName("chg_itf","init"));
//             form.setAction(us.getClientIntName("view","init")); 
          }

/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

          if ( form.getAction().equals("newitf") ) {
                form.setAction(us.getClientIntName("chg_itf","change"));
                stmt = conn.prepareStatement("UPDATE users_tbl SET IdTema=? WHERE Id LIKE ?");
                stmt.setObject(1,ladj.getIdTema(),Types.INTEGER);
                stmt.setObject(2,user.getUid(),Types.INTEGER);
                stmt.executeUpdate();
                
                user.setIdTema(""+ladj.getIdTema());
                session.removeAttribute("user");
                session.setAttribute("user",user);

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del")) {
/*
                stmt = conn.prepareStatement("SELECT id,name,group_id,descr FROM users_tbl WHERE id LIKE ?");
                stmt.setObject(1, request.getParameter("id"),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  ladj.setIdStud(new Integer(rs.getString(1)));
                  ladj.setImja(rs.getString(2));
                  ladj.setIdGruppa(new Integer(rs.getString(3)));
                  ladj.setDescr(rs.getString(4));
                }
*/
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null) {
/*             
               if(ladj.getSpecial3() == null) {
// Без смены пароля
                  stmt = conn.prepareStatement("UPDATE users_tbl SET name=?,group_id=?,Descr=? WHERE id LIKE ?");
                  stmt.setObject(1, ladj.getImja(),Types.VARCHAR);
                  stmt.setObject(2, ladj.getIdGruppa(),Types.INTEGER);
                  stmt.setObject(3, ladj.getDescr(),Types.VARCHAR);
                  stmt.setObject(4, ladj.getIdStud(),Types.INTEGER);
                  stmt.executeUpdate();
               } else{
// Со сменой пароля
                  stmt = conn.prepareStatement("UPDATE users_tbl SET name=?,group_id=?,pass=ENCRYPT(?),Descr=? WHERE id LIKE ?");
                  stmt.setObject(1, ladj.getImja(),Types.VARCHAR);
                  stmt.setObject(2, ladj.getIdGruppa(),Types.INTEGER);
                  stmt.setObject(3, ladj.getOtch(),Types.VARCHAR);
                  stmt.setObject(4, ladj.getDescr(),Types.VARCHAR);
                  stmt.setObject(5, ladj.getIdStud(),Types.INTEGER);
                  stmt.executeUpdate();
               }
*/
               lich_adj_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null) {
/*
                stmt = conn.prepareStatement("DELETE FROM users_tbl WHERE id LIKE ? AND su IS NULL");
                stmt.setObject(1, ladj.getIdStud(),Types.INTEGER);
                stmt.executeUpdate();
                lich_adj_f  = true;
*/
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
        request.setAttribute("ladj", ladj);
        request.setAttribute("themes", themes);
        request.setAttribute("studs_Gr", studs_Gr);
        request.setAttribute("stud_G_S2", stud_G_S2);
        request.setAttribute("operats", operats);
        request.setAttribute("others", others);
        request.setAttribute("administrat", administrat);
     }
        if(error) return mapping.findForward("error");
        if(lich_adj_f) return mapping.findForward("lich_adj_f");
        return mapping.findForward("success");
    }
}