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

public class PunktyAction extends Action {

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
        PunktyForm              form       = (PunktyForm) actionForm;
        AbiturientBean          abit_P     = form.getBean(request, errors);
        boolean                 punkty_f   = false;
        boolean                 error      = false;
        int                     kPunkta    = -1;
        ActionForward           f          = null;
        ArrayList               abits_P    = new ArrayList();
        ArrayList               abit_P_S1  = new ArrayList();
        UserBean                user       = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "punktyAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "punktyForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************* Подготовка данных для ввода с помощью селектора **************************/

                 stmt = conn.prepareStatement("SELECT DISTINCT UPPER(LEFT(Nazvanie,1)) FROM Punkty WHERE Nazvanie NOT LIKE -1 AND Nazvanie NOT LIKE '' ORDER BY 1 ASC");
                 rs = stmt.executeQuery();
                 if (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setSpecial1(StringUtil.toEng(rs.getString(1)));
                   abit_TMP.setSpecial2(rs.getString(1));
                   abit_P_S1.add(abit_TMP);
                   if(session.getAttribute("letter") == null) 
                   session.setAttribute("letter",StringUtil.toEng(rs.getString(1)));
                 }
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setSpecial1(StringUtil.toEng(rs.getString(1)));
                   abit_TMP.setSpecial2(rs.getString(1));
                   abit_P_S1.add(abit_TMP);
                 }

/************************************************************************************************/

            if ( form.getAction() == null || form.getAction().equals("full")) {

                 form.setAction(us.getClientIntName("full","init"));
                              
                   if( request.getParameter("letter") != null ) {    
                     String str = new String();
                     session.setAttribute("letter",request.getParameter("letter"));
                   }

                   String query = new String();
  
                   query += "SELECT DISTINCT KodPunkta,Nazvanie FROM Punkty WHERE Nazvanie LIKE '"+StringUtil.toRus(""+session.getAttribute("letter"))+"%' AND KodVuza LIKE '";

                   query += session.getAttribute("kVuza")+"' GROUP BY Nazvanie,KodPunkta ORDER BY Nazvanie ASC";

                   stmt = conn.prepareStatement(query);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodPunkta(new Integer(rs.getInt(1)));
                     abit_TMP.setNazvanie(rs.getString(2));
                     abits_P.add(abit_TMP);
                   }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/
/************************************************************************************************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodPunkta,Nazvanie FROM Punkty WHERE KodPunkta LIKE ?");
                stmt.setObject(1,abit_P.getKodPunkta(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_P.setKodPunkta(new Integer(rs.getInt(1)));
                  abit_P.setNazvanie(rs.getString(2));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************************************************************************************************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                form.setAction(us.getClientIntName("change","act"));
// Если вводимый пункт уже существует, то выбираем его код
                stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie LIKE ?");
                stmt.setObject(1,abit_P.getNazvanie(),Types.VARCHAR);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  kPunkta = rs.getInt(1);
                  if(!(kPunkta+"").equals(abit_P.getKodPunkta()+"")) {
// Запись о старом пункте уничтожаем
                   stmt = conn.prepareStatement("DELETE FROM Punkty WHERE KodPunkta LIKE ?");
                   stmt.setObject(1,abit_P.getKodPunkta(),Types.INTEGER);
                   stmt.executeUpdate();
                  }
                } else {
                    stmt = conn.prepareStatement("UPDATE Punkty SET Nazvanie=? WHERE KodPunkta LIKE ?");
                    stmt.setObject(1,abit_P.getNazvanie(),Types.VARCHAR);
                    stmt.setObject(2,abit_P.getKodPunkta(),Types.INTEGER);
                    stmt.executeUpdate();
                    kPunkta = StringUtil.toInt(abit_P.getKodPunkta()+"",1);
                }
                  stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE KodPunkta LIKE ? AND Abiturient.DokumentyHranjatsja LIKE 'д'");
                  stmt.setObject(1,abit_P.getKodPunkta(),Types.INTEGER);
                  rs = stmt.executeQuery();
                  while(rs.next()) {
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodPunkta=? WHERE KodAbiturienta LIKE ?");
                    stmt.setObject(1,new Integer(""+kPunkta),Types.INTEGER);
                    stmt.setObject(2,new Integer(rs.getString(1)),Types.INTEGER);
                    stmt.executeUpdate();                
                  }
                punkty_f  = true;
                }
                else {
                form.setAction(us.getClientIntName("change","act-fix-abit"));
// Коды абитуриентов у которых изменяем код пункта
                ArrayList abits = new ArrayList();
                stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE KodPunkta LIKE ?");
                stmt.setObject(1,abit_P.getKodPunkta(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) abits.add(rs.getString(1));
// Выборка пустого кода пункта
                stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie LIKE ''");
                rs = stmt.executeQuery();
                if(rs.next()) kPunkta = rs.getInt(1);

// Смена кодов пунктов на код пустой записи
                for(int i=0;i<abits.size();i++) {
                stmt = conn.prepareStatement("UPDATE Abiturient SET KodPunkta=? WHERE KodAbiturienta LIKE ?");
                stmt.setObject(1,new Integer(kPunkta+""),Types.INTEGER);
                stmt.setObject(2,new Integer(""+abits.get(i)),Types.INTEGER);
                stmt.executeUpdate();
                }              

// Удаление старой записи из пунктов
                stmt = conn.prepareStatement("DELETE FROM Punkty WHERE KodPunkta LIKE ?");
                stmt.setObject(1,abit_P.getKodPunkta(),Types.INTEGER);
                stmt.executeUpdate();
                punkty_f  = true;
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
        request.setAttribute("abit_P", abit_P);
        request.setAttribute("abits_P", abits_P);
        request.setAttribute("abit_P_S1", abit_P_S1);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(punkty_f) return mapping.findForward("punkty_f");
        return mapping.findForward("success");
    }
}