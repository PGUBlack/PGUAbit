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

public class GruppyAction extends Action {

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
        GruppyForm         form         = (GruppyForm) actionForm;
        AbiturientBean     abit_Gr      = form.getBean(request, errors);
        MessageBean        message      = new MessageBean();
        boolean            gruppy_f     = false;
        boolean            error        = false;
        ActionForward      f            = null;
        int                kGr          = 1;
        ArrayList          groups_bud   = new ArrayList();
        ArrayList          groups_kon   = new ArrayList();
        ArrayList          groups_z_bud = new ArrayList();
        ArrayList          groups_z_kon = new ArrayList();
        ArrayList          abits_Gr     = new ArrayList();
        ArrayList          abit_G_S2    = new ArrayList();
        ArrayList          abit_G_S3    = new ArrayList();
        UserBean           user         = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "gruppyAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "gruppyForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************* Подготовка данных для ввода с помощью селекторов *************************/

                 stmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta,ShifrFakulteta,KodFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
                 stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 if (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
                   abit_TMP.setShifrFakulteta(rs.getString(2));
                   abit_TMP.setKodFakulteta(new Integer(rs.getString(3)));
                   abit_G_S2.add(abit_TMP);
                   if( session.getAttribute("kFak") == null) 
                   session.setAttribute("kFak",rs.getString(3));
                 }
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
                   abit_TMP.setShifrFakulteta(rs.getString(2));
                   abit_TMP.setKodFakulteta(new Integer(rs.getString(3)));
                   abit_G_S2.add(abit_TMP);
                 }

                 stmt = conn.prepareStatement("SELECT DISTINCT KodKonGruppy,Nazvanie,Abbr FROM KonGruppa WHERE KodVuza LIKE ? ORDER BY 1 ASC");
                 stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setKodKonGrp(new Integer(rs.getString(1)));
                   abit_TMP.setAbbr(rs.getString(2)+"%"+rs.getString(3));
                   abit_G_S3.add(abit_TMP);
                 }

/************************************************************************************************/

            if ( form.getAction() == null ) {
             
                 form.setAction(us.getClientIntName("new","init"));
                 session.removeAttribute("kFak");

            } else if ( form.getAction().equals("create") || form.getAction().equals("full") ) {

/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

                if ( request.getParameter("full") == null &&
                             form.getAction().equals("create") ) {

                    stmt = conn.prepareStatement("SELECT KodGruppy FROM Gruppy WHERE Gruppa LIKE ?");
                    stmt.setObject(1, abit_Gr.getGruppa(),Types.VARCHAR);
                    rs = stmt.executeQuery();
                    if(rs.next()) {
                      message.setMessage("Группа уже существует в БД! Измените имя группы и повторите ввод.");
                    } else {
                       stmt = conn.prepareStatement("SELECT MAX(KodGruppy) FROM Gruppy");
                       rs = stmt.executeQuery();
                       if(rs.next()) kGr = rs.getInt(1)+1;
                          else kGr = 2;
                       stmt = conn.prepareStatement("SELECT KodFakulteta FROM Fakultety WHERE ShifrFakulteta LIKE ?");
                       stmt.setObject(1, abit_Gr.getShifrFakulteta(),Types.VARCHAR);
                       rs = stmt.executeQuery();
                       if(rs.next()){
                          stmt = conn.prepareStatement("INSERT Gruppy(KodGruppy,Gruppa,KodFakulteta,Dogovornaja,Potok,KodKonGruppy) VALUES(?,?,?,?,?,?)");
                          stmt.setObject(1, new Integer(""+kGr),Types.INTEGER);
                          stmt.setObject(2, abit_Gr.getGruppa(),Types.VARCHAR);
                          stmt.setObject(3, rs.getString(1));
                          stmt.setObject(4, abit_Gr.getDogovornaja(),Types.VARCHAR);
                          stmt.setObject(5, abit_Gr.getNomerPotoka(),Types.INTEGER);
                          stmt.setObject(6, abit_Gr.getKodKonGrp(),Types.INTEGER);
                          stmt.executeUpdate();
                       }
                    }
                    form.setAction(us.getClientIntName("new","crt"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
// Выборка групп
                if(request.getParameter("kFak") != null)
                   session.setAttribute("kFak",request.getParameter("kFak"));

/****************************************************/
/********************* ДНЕВНОЕ и ВЕЧЕРНЕЕ ***********/
/****************************************************/

// Договорники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'д' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'д' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'д' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_kon.add(tmp);
                }
// Бюджетники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'б' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'б' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'б' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_bud.add(tmp);
                }

/****************************************************/
/******************** ЗАОЧНОЕ ***********************/
/****************************************************/

// Договорники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'зд' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'зд' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'зд' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_z_kon.add(tmp);
                }
// Бюджетники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'зб' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'зб' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'зб' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_z_bud.add(tmp);
                }
              }
/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT Gruppa,f.ShifrFakulteta,Dogovornaja,g.Potok,g.KodKonGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.KodGruppy LIKE ?");
                stmt.setObject(1, new Integer(""+abit_Gr.getKodGruppy()),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_Gr.setGruppa(rs.getString(1));
                  abit_Gr.setShifrFakulteta(rs.getString(2));
                  abit_Gr.setDogovornaja(rs.getString(3));
                  abit_Gr.setNomerPotoka(new Integer(rs.getString(4)));
                  abit_Gr.setKodKonGrp(new Integer(rs.getString(5)));
                }
                form.setAction(us.getClientIntName("md_dl","view"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
               form.setAction(us.getClientIntName("change","act"));
               stmt = conn.prepareStatement("SELECT KodFakulteta FROM Fakultety WHERE ShifrFakulteta LIKE ?");
               stmt.setObject(1, abit_Gr.getShifrFakulteta(),Types.VARCHAR);
               rs = stmt.executeQuery();
               if(rs.next()){
                 stmt = conn.prepareStatement("UPDATE Gruppy SET Gruppa=?, KodFakulteta=?, Dogovornaja=?, Potok=?, KodKonGruppy=? WHERE KodGruppy LIKE ?");
                 stmt.setObject(1,abit_Gr.getGruppa(),Types.VARCHAR);
                 stmt.setObject(2,rs.getString(1));
                 stmt.setObject(3, abit_Gr.getDogovornaja(),Types.VARCHAR);
                 stmt.setObject(4,abit_Gr.getNomerPotoka(),Types.INTEGER);
                 stmt.setObject(5,abit_Gr.getKodKonGrp(),Types.INTEGER);
                 stmt.setObject(6,new Integer(""+abit_Gr.getKodGruppy()),Types.INTEGER);
                 stmt.executeUpdate();
               }
               gruppy_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
               form.setAction(us.getClientIntName("delete","act"));

// Начало транзакции
                conn.setAutoCommit(false);

                stmt = conn.prepareStatement("DELETE FROM Raspisanie WHERE KodGruppy LIKE ?");
                stmt.setObject(1,new Integer(""+abit_Gr.getKodGruppy()),Types.INTEGER);
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM Gruppy WHERE KodGruppy LIKE ?");
                stmt.setObject(1,new Integer(""+abit_Gr.getKodGruppy()),Types.INTEGER);
                stmt.executeUpdate();

// Закрепление транзакции
                conn.setAutoCommit(true);
                conn.commit();

                gruppy_f  = true;
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
        request.setAttribute("abit_Gr", abit_Gr);
        request.setAttribute("abits_Gr", abits_Gr);
        request.setAttribute("abit_G_S2", abit_G_S2);
        request.setAttribute("abit_G_S3", abit_G_S3);
        request.setAttribute("message", message);
        request.setAttribute("groups_kon", groups_kon);
        request.setAttribute("groups_bud", groups_bud);
        request.setAttribute("groups_z_kon", groups_z_kon);
        request.setAttribute("groups_z_bud", groups_z_bud);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(gruppy_f) return mapping.findForward("gruppy_f");
        return mapping.findForward("success");
    }
}