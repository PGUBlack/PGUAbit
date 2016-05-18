package abit.action;

import java.io.IOException;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*;

public class ErrorAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session           = request.getSession();
        Connection              conn              = null;
        PreparedStatement       stmt              = null;
        ResultSet               rs                = null;
        ActionErrors            errors            = new ActionErrors();
        ErrorForm               form              = (ErrorForm) actionForm;
        ErrorBean               err_bean          = form.getBean(request, errors);
        ActionError             msg               = null;
        boolean                 error_f           = false;
        boolean                 index             = false;
        ArrayList               errs_bean         = new ArrayList();
        Integer                 RemID             = new Integer(0);
        UserBean                user              = (UserBean)session.getAttribute("user");

        if ( errors.empty() ) {

        request.setAttribute( "errorAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "errorForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************************************************************************************************/
/********************** Подготовка данных для ввода с помощью селектора *************************/
            if( form.getAction() == null ) {

              form.setAction(us.getClientIntName("new","init"));

             if(session.getAttribute("except")!=null) {

               if( (""+session.getAttribute("except")).indexOf("sql") > 0 )
                 request.setAttribute("SQLException",session.getAttribute("except"));
               else
                 request.setAttribute("JAVAException",session.getAttribute("except"));

               session.removeAttribute("except");

// Если возникшая ошибка уже зарегистрирована, то выводим ее текущий статус
               StringWriter s_w = new StringWriter();
               PrintWriter p_w = new PrintWriter(s_w);

// Статус ошибки (может быть она уже была ?)
               stmt = conn.prepareStatement("SELECT IdRemark,Status FROM Remarks,Status WHERE Remarks.IdStatus=Status.IdStatus AND REPLACE(REPLACE(REPLACE(REPLACE(SUBSTRING(Remark,1,DATALENGTH(Remark)),0x0D,''),0x0A,''),' ',''),0x09,'') LIKE REPLACE(REPLACE(REPLACE(REPLACE(?,0x0D,''),0x0A,''),' ',''),0x09,'')");

               if(request.getAttribute("SQLException") != null) {

                 ((java.sql.SQLException)request.getAttribute("SQLException")).printStackTrace(p_w);
                 stmt.setObject(1, s_w.toString().substring(s_w.toString().lastIndexOf("]")+1),Types.VARCHAR);

               } else {

                 ((java.lang.Exception)request.getAttribute("JAVAException")).printStackTrace(p_w);
                 stmt.setObject(1, s_w.toString(),Types.VARCHAR);

// Тип ошибки
                 if((""+request.getAttribute("JAVAException")).lastIndexOf(".")>0)
                   err_bean.setTip((""+request.getAttribute("JAVAException")).substring((""+request.getAttribute("JAVAException")).lastIndexOf(".")+1));
                 else
                   err_bean.setTip(""+request.getAttribute("JAVAException"));

// Текст сообщения об ошибке

                   if((s_w.toString()).indexOf(err_bean.getTip()) >0 )
                     err_bean.setRemark((s_w.toString()).substring((s_w.toString()).indexOf(err_bean.getTip())+1+(err_bean.getTip()).length()));
                   else 
                     err_bean.setRemark(s_w.toString());

               }

               rs = stmt.executeQuery();

               if(rs.next()) err_bean.setStatus(rs.getString(2));

//                 err_bean.setTip(request.getAttribute("JAVAException"));

// Текст сообщения об ошибке
//                 err_bean.setRemark(s_w.toString());

             }
            } else if ( form.getAction().equals("new_rem") || form.getAction().equals("full") ) {

/************************************************************************************************/
/**  Если action равен new_rem или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

                if(form.getAction().equals("new_rem")) {
                  index = true;
                  String str =  new String(err_bean.getRemark());
                  if(request.getParameter("add")!=null){

                    stmt = conn.prepareStatement("SELECT MAX(IdRemark) FROM Remarks");
                    rs = stmt.executeQuery();
                    if(rs.next()) RemID = new Integer(rs.getInt(1)+1);
                    else RemID = new Integer("1");

                    stmt = conn.prepareStatement("SELECT IdRemark FROM Remarks WHERE REPLACE(REPLACE(REPLACE(REPLACE(SUBSTRING(Remark,1,DATALENGTH(Remark)),0x0D,''),0x0A,''),' ',''),0x09,'') LIKE REPLACE(REPLACE(REPLACE(REPLACE(?,0x0D,''),0x0A,''),' ',''),0x09,'')");
                    stmt.setObject(1, str,Types.VARCHAR);
                    rs = stmt.executeQuery();
                    if(!rs.next()) {

// Если в БД нет возникшей ошибки, то она добавляется. Иначе - ничего не происходит.

                      stmt = conn.prepareStatement("INSERT Remarks(IdRemark,IdStatus,Remark,Tip,Comment,IdDiv,IdUser,Data,Time) VALUES(?,?,?,?,?,?,?,?,?)");
                      stmt.setObject(1, RemID,Types.INTEGER);
                      stmt.setObject(2, "1",Types.INTEGER);
                      str=str.replace('\'','\"');

                      if(str.lastIndexOf("]")!=-1)
                        stmt.setObject(3, str.substring(str.lastIndexOf("]")+1),Types.VARCHAR);
                      else
                        stmt.setObject(3, str,Types.VARCHAR);

                      if((err_bean.getTip()).lastIndexOf("]")!=-1)
                        stmt.setObject(4, err_bean.getTip().substring(0,err_bean.getTip().lastIndexOf("]")+1),Types.VARCHAR);
                      else
                        stmt.setObject(4, err_bean.getTip(),Types.VARCHAR);

                      stmt.setObject(5, err_bean.getComment());
                      stmt.setObject(6, "1");
                      stmt.setObject(7, user.getUid());
                      stmt.setObject(8, StringUtil.CurrDate("."),Types.VARCHAR);
                      stmt.setObject(9, StringUtil.CurrTime(":"),Types.VARCHAR);
                      stmt.executeUpdate();
                    }
                 }
/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else if ( form.getAction().equals("full") ) {
                   form.setAction(us.getClientIntName("full","view"));
                 stmt = conn.prepareStatement("SELECT r.IdRemark,s.Status,r.Remark,r.Tip,r.Comment,d.Abbr,u.Name,u.Descr,r.Data,r.Time FROM Remarks r,users_tbl u,PodrVuza d,Status s WHERE r.IdStatus=s.IdStatus AND u.id=r.IdUser AND d.IdDiv=r.IdDiv ORDER BY r.Data,r.Time ASC");
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   ErrorBean TMP = new ErrorBean();
                   TMP.setIdRemark(new Integer(rs.getString(1)));
                   TMP.setStatus(rs.getString(2));
                   TMP.setRemark(rs.getString(3));
                   TMP.setTip(rs.getString(4));
                   TMP.setComment(rs.getString(5));
                   TMP.setAbbr(rs.getString(6));
                   TMP.setName(rs.getString(7));
                   TMP.setDescr(rs.getString(8));
                   TMP.setData(rs.getString(9));
                   TMP.setTime(rs.getString(10));
                   errs_bean.add(TMP);
                 }
              }
/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                form.setAction(us.getClientIntName("md_dl","form"));
/*
                stmt = conn.prepareStatement("SELECT DISTINCT Abbr_Fak,Abbr_Kaf,Nazv_Kaf,Zav_Kaf,Kaf_Tel,Fakultet.IdFak,Nazv_Fak FROM Kafedra,Fakultet WHERE Kafedra.IdFak = Fakultet.IdFak AND IdKaf LIKE ?");
                stmt.setObject(1,err_bean.getIdKaf());
                rs = stmt.executeQuery();
                if(rs.next()) {
                     err_bean.setAbbr_Fak(rs.getString(1));
                     err_bean.setAbbr_Kaf(rs.getString(2));
                     err_bean.setNazv_Kaf(rs.getString(3));                
                     err_bean.setZav_Kaf(rs.getString(4));
                     err_bean.setKaf_Tel(rs.getString(5));
                     err_bean.setKaf_1(rs.getString(6));
                     err_bean.setNazv_Fak(rs.getString(7));
                }
*/                

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                  form.setAction(us.getClientIntName("change","act"));
/*
                  stmt = conn.prepareStatement("UPDATE Kafedra SET IdFak=?,Abbr_Kaf=?,Nazv_Kaf=?,Zav_Kaf=?,Kaf_Tel=? WHERE ( (IdKaf  LIKE ?) )");
                  stmt.setObject(1,err_bean.getKaf_1());
                  stmt.setObject(2,err_bean.getAbbr_Kaf());
                  stmt.setObject(3,err_bean.getNazv_Kaf());
                  stmt.setObject(4,err_bean.getZav_Kaf());
                  stmt.setObject(5,err_bean.getKaf_Tel());
                  stmt.setObject(6,err_bean.getIdKaf());
                  stmt.executeUpdate();
                  error_f  = true;
*/
/**************** Удаление только из таблицы Кафедра *******************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM Remarks WHERE IdRemark LIKE ?");
                stmt.setObject(1,err_bean.getIdRemark());
                stmt.executeUpdate();
                error_f  = true;
              }
        }
         catch ( SQLException e ) {
          request.setAttribute("SQLException", e);
          return mapping.findForward("");
        }
        catch ( java.lang.Exception e ) {
          request.setAttribute("JAVAexception", e);
          return mapping.findForward("");
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
        request.setAttribute("err_bean", err_bean);
        request.setAttribute("errs_bean", errs_bean);
      }
        if(error_f) return mapping.findForward("error_f");

        if(index) return mapping.findForward("index");
        return mapping.findForward("success");
    }
}