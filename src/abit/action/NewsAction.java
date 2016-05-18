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

public class NewsAction extends Action {

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
        NewsForm           form        = (NewsForm) actionForm;
        AbiturientBean     abit_news   = form.getBean(request, errors);
        boolean            news_f      = false;
        boolean            error       = false;
        ActionForward      f           = null;
        ArrayList          abits_news  = new ArrayList();
        UserBean           user        = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
                     
        request.setAttribute( "newsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);

          conn = us.getConn(user.getSid());

          request.setAttribute( "newsForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");

/************** Переход к индексной странице и настройка **************/
          if(us.quit("next")) {
            if(abit_news.getSpecial2() != null) {
              stmt = conn.prepareStatement("UPDATE Users_tbl SET ViewNews='н' WHERE Id LIKE ?");
              stmt.setObject(1,user.getUid(),Types.INTEGER);
              stmt.executeUpdate();
            }
            return mapping.findForward("next");
          }
/**********************************************************************/

// Для разграничения прав при просмотре и редактировании новостей

          request.setAttribute("user_type",""+user.getGroup().getTypeId());

          abit_news.setSpecial1(StringUtil.CurrDate("."));

            if ( form.getAction() == null) {

// Просмотр новостей - секция чтения данных для просмотра находится ниже

               form.setAction(us.getClientIntName("view_news","init"));
           
            } else if ( form.getAction().equals("new") ) {

// Переход к форме добавления

                abit_news.setData(StringUtil.CurrDate("."));

                form.setAction(us.getClientIntName("add_new","act"));

            } else if ( form.getAction().equals("create") ) {

/*********************************************************************/
/************************ Добавление новостей ************************/
/*********************************************************************/

// Сброс признаков пропуска страницы новостей у пользователей системы
               stmt = conn.prepareStatement("UPDATE Users_tbl SET ViewNews='д'");
               stmt.executeUpdate();

               int idN = 2;
               stmt = conn.prepareStatement("SELECT MAX(IdNews) FROM News");
               rs = stmt.executeQuery();
               if(rs.next()) idN = rs.getInt(1)+1;

               stmt = conn.prepareStatement("INSERT News(IdNews,Data,Description,KodVuza) VALUES(?,?,?,?)");
               stmt.setObject(1, new Integer(""+idN),Types.INTEGER);
               stmt.setObject(2, abit_news.getData(),Types.VARCHAR);
               stmt.setObject(3, abit_news.getDescription(),Types.VARCHAR);
               stmt.setObject(4, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate();

               form.setAction(us.getClientIntName("view_news","full"));

            } else if ( form.getAction().equals("mod_del") ) {

/*********************************************************************/
/***************** Подготовка к модификации новостей *****************/
/*********************************************************************/

               stmt = conn.prepareStatement("SELECT IdNews,Data,Description FROM News WHERE KodVuza LIKE ? AND IdNews LIKE ?");
               stmt.setObject(1, session.getAttribute("kVuza"), Types.INTEGER);
               stmt.setObject(2, abit_news.getIdNews(), Types.INTEGER);
               rs = stmt.executeQuery();
               if (rs.next()) {
                 abit_news.setIdNews(new Integer(rs.getInt(1)));
                 abit_news.setData(rs.getString(2));
                 abit_news.setDescription(rs.getString(3));
               }

               form.setAction(us.getClientIntName("mod_del","act"));

            } else if ( form.getAction().equals("md") && request.getParameter("del") == null) {

/*********************************************************************/
/************************ Модификация новости ************************/
/*********************************************************************/

// Сброс признаков пропуска страницы новостей у пользователей системы
               stmt = conn.prepareStatement("UPDATE Users_tbl SET ViewNews='д'");
               stmt.executeUpdate();

               stmt = conn.prepareStatement("UPDATE News SET Data=?,Description=? WHERE IdNews LIKE ? AND KodVuza LIKE ?");
               stmt.setObject(1,abit_news.getData(),Types.VARCHAR);
               stmt.setObject(2,abit_news.getDescription(),Types.VARCHAR);
               stmt.setObject(3,abit_news.getIdNews(),Types.INTEGER);
               stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate(); 

               form.setAction(us.getClientIntName("view_news","full"));

            } else if( request.getParameter("del") != null) {

/*********************************************************************/
/************************  Удаление новости  *************************/
/*********************************************************************/

// Сброс признаков пропуска страницы новостей у пользователей системы
               stmt = conn.prepareStatement("UPDATE Users_tbl SET ViewNews='д'");
               stmt.executeUpdate();

               stmt = conn.prepareStatement("DELETE FROM News WHERE IdNews LIKE ? AND KodVuza LIKE ?");
               stmt.setObject(1,abit_news.getIdNews(),Types.VARCHAR);
               stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate(); 

               form.setAction(us.getClientIntName("view_news","full"));
            }

/*********************************************************************/
/************************* Просмотр новостей *************************/
/*********************************************************************/

          if ( form.getAction().equals("view_news")) {

            form.setAction(us.getClientIntName("view_news","full"));

            stmt = conn.prepareStatement("SELECT ViewNews,Famil,Imja,Otch FROM Users_tbl WHERE Id LIKE ?");
            stmt.setObject(1,user.getUid(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
              abit_news.setSpecial2(rs.getString(1));
              if(rs.getString(3) != null && rs.getString(3).substring(1).equals("."))
                abit_news.setSpecial3(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
              else
                abit_news.setSpecial3(rs.getString(3)+" "+rs.getString(4));
            } else abit_news.setSpecial3("Внимание!!! Обнаружен НСД в систему");

            stmt = conn.prepareStatement("SELECT IdNews,Data,Description FROM News WHERE KodVuza LIKE ? ORDER BY IdNews DESC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setIdNews(new Integer(rs.getInt(1)));
              abit_TMP.setData(rs.getString(2));
              abit_TMP.setDescription(rs.getString(3));
              abits_news.add(abit_TMP);
            }

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
        request.setAttribute("abit_news", abit_news);
        request.setAttribute("abits_news", abits_news);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(news_f) return mapping.findForward("news_f");
        return mapping.findForward("success");
    }
}