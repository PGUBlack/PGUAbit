package abit.action;

import java.io.IOException;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import abit.bean.*;
import abit.Constants;
import abit.sql.*; 

public class NazvanieVuzaAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session         = request.getSession();
        Connection        conn            = null;
        PreparedStatement stmt            = null;
        ResultSet         rs              = null;
        ActionErrors      errors          = new ActionErrors();
        ActionError       msg             = null;
        NazvanieVuzaForm  form            = (NazvanieVuzaForm) actionForm;
        AbiturientBean    abit_NV         = form.getBean(request, errors);
        boolean           nazvanievuza_f  = false;
        boolean           error           = false;
        int               kVuza           = 1;
        ActionForward     f               = null;
        ArrayList         abits_NV        = new ArrayList();
        UserBean          user            = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "nazvanieVuzaAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "nazvanieVuzaForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          if ( form.getAction() == null ) {
             
               form.setAction(us.getClientIntName("new","init"));

            } else if ( form.getAction().equals("create") || form.getAction().equals("full") ) {

/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

                if ( request.getParameter("full") == null &&
                             form.getAction().equals("create") ) {

                    stmt = conn.prepareStatement("SELECT MAX(KodVuza) FROM NazvanieVuza");
                    rs = stmt.executeQuery();
                    if(rs.next()) kVuza = rs.getInt(1)+1;
                     else kVuza = 2;

                    stmt = conn.prepareStatement("INSERT NazvanieVuza(KodVuza,NazvanieVuza,AbbreviaturaVuza,PostAdresVuza,NazvanieRodit) VALUES(?,?,?,?,?)");
                    stmt.setObject(1, new Integer(""+kVuza),Types.INTEGER);
                    stmt.setObject(2, abit_NV.getNazvanieVuza(),Types.VARCHAR);
                    stmt.setObject(3, abit_NV.getAbbreviaturaVuza(),Types.VARCHAR);
                    stmt.setObject(4, abit_NV.getPostAdresVuza(),Types.VARCHAR);
                    stmt.setObject(5, abit_NV.getNazvanieRodit(),Types.VARCHAR);
                    stmt.executeUpdate();
                    form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
                   stmt = conn.prepareStatement("SELECT KodVuza,NazvanieVuza,AbbreviaturaVuza,PostAdresVuza,NazvanieRodit FROM NazvanieVuza ORDER BY 1 ASC");
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodVuza(new Integer(rs.getInt(1)));
                     abit_TMP.setNazvanieVuza(rs.getString(2));
                     abit_TMP.setAbbreviaturaVuza(rs.getString(3));
                     abit_TMP.setPostAdresVuza(rs.getString(4));
                     abit_TMP.setNazvanieRodit(rs.getString(5));
                     abits_NV.add(abit_TMP);
                   }
                 }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodVuza,NazvanieVuza,AbbreviaturaVuza,PostAdresVuza,NazvanieRodit FROM NazvanieVuza WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_NV.setKodVuza(new Integer(rs.getInt(1)));
                  abit_NV.setNazvanieVuza(rs.getString(2));
                  abit_NV.setAbbreviaturaVuza(rs.getString(3));
                  abit_NV.setPostAdresVuza(rs.getString(4));
                  abit_NV.setNazvanieRodit(rs.getString(5));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("change","act"));
                stmt = conn.prepareStatement("UPDATE NazvanieVuza SET NazvanieVuza=?,AbbreviaturaVuza=?,PostAdresVuza=?,NazvanieRodit=? WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getNazvanieVuza(),Types.VARCHAR);
                stmt.setObject(2,abit_NV.getAbbreviaturaVuza(),Types.VARCHAR);
                stmt.setObject(3,abit_NV.getPostAdresVuza(),Types.VARCHAR);
                stmt.setObject(4,abit_NV.getNazvanieRodit(),Types.VARCHAR);
                stmt.setObject(5,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();
                nazvanievuza_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));

// Начало транзакции
                conn.setAutoCommit(false);

                stmt = conn.prepareStatement("DELETE FROM OtvetstvennyeLitsa WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Raspisanie WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Gruppy WHERE KodKonGruppy IN (SELECT KodKonGruppy FROM KonGruppa WHERE KodVuza LIKE ?)");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();
                
                stmt = conn.prepareStatement("DELETE FROM KonGruppa WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Otsenki WHERE KodAbiturienta IN (SELECT KodAbiturienta FROM Abiturient WHERE KodVuza LIKE ?)");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM ZajavlennyeShkolnyeOtsenki WHERE KodAbiturienta IN (SELECT KodAbiturienta FROM Abiturient WHERE KodVuza LIKE ?)");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Abiturient WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM NazvanijaPredmetov WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ?)");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Spetsialnosti WHERE KodFakulteta IN(SELECT KodFakulteta FROM Fakultety WHERE KodVuza LIKE ?)");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Fakultety WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Lgoty WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM Medali WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM TselevojPriem WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

                stmt = conn.prepareStatement("DELETE FROM NazvanieVuza WHERE KodVuza LIKE ?");
                stmt.setObject(1,abit_NV.getKodVuza(),Types.INTEGER);
                stmt.executeUpdate();

// Закрепление транзакции
                conn.setAutoCommit(true);
                conn.commit();

                nazvanievuza_f  = true;
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
        request.setAttribute("abit_NV", abit_NV);
        request.setAttribute("abits_NV", abits_NV);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(nazvanievuza_f) return mapping.findForward("nazvanievuza_f");
        return mapping.findForward("success");
    }
}