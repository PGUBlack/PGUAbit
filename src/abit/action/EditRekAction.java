package abit.action;

import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*; 

public class EditRekAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        PreparedStatement stmt1         = null;
        ResultSet         rs            = null;
        ResultSet         rs1           = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        ZchForm           form          = (ZchForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        boolean           error         = false;
        ActionForward     f             = null;
        ArrayList         abits_A       = new ArrayList();
        ArrayList         abit_A_S1     = new ArrayList();
        ArrayList         abit_A_S2     = new ArrayList();
        ArrayList         abit_A_S3     = new ArrayList();
        ArrayList         abit_A_S4     = new ArrayList();
        ArrayList         abit_A_S5     = new ArrayList();
        String            kPredmeta     = new String();
        String            oldKodeS      = new String();
        String            oldKodeF      = new String();
        String            oldNazv       = new String();
        boolean           priznak       = true;
        int               count         = 0;
        int               count_rek     = 0;
        UserBean          user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "editRekAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "zchForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");


/********************** Подготовка данных для ввода с помощью селекторов ************************/
          if(form.getAction() == null ) {

            session.removeAttribute("doc_hran");
            session.removeAttribute("kod_fak");
            session.removeAttribute("kod_spec");

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_A_S1.add(abit_TMP);
            }

            priznak = true;
            kPredmeta = new String();
            String oldKode = new String();
            String oldKodFak = new String();
            String oldAbbr = new String();
            stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,EkzamenyNaSpetsialnosti.KodPredmeta,Abbreviatura,AbbreviaturaFakulteta,Spetsialnosti.KodFakulteta FROM Spetsialnosti,EkzamenyNaSpetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND KodVuza LIKE ? ORDER BY Abbreviatura,EkzamenyNaSpetsialnosti.KodPredmeta,AbbreviaturaFakulteta ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial1(rs.getString(1));
              if(priznak) { oldKode = rs.getString(1); oldKodFak = rs.getString(5); oldAbbr = rs.getString(3); priznak = false; }
              if(!oldKode.equals(rs.getString(1))) {
                if(kPredmeta.equals("")) kPredmeta += "%" + rs.getString(2);
                  abit_TMP.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
                  abit_TMP.setAbbreviatura(oldAbbr);
                  abit_A_S2.add(abit_TMP);
                  priznak = true;
                  kPredmeta = "";
              }
              kPredmeta += "%" + rs.getString(2);
            }
            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
            abit_TMP2.setAbbreviatura(oldAbbr);
            abit_A_S2.add(abit_TMP2);

            form.setAction(us.getClientIntName("menu","init"));

//***************************************************************
//****************** ОБНОВЛЕНИЕ ДАННЫХ В БД *********************
//***************************************************************
} else if( form.getAction().equals("updatebase")) {

         String kk = "";

// Если кодовое слово-верно, то редактируем

         if(abit_A.getSpecial6() != null && (abit_A.getSpecial6().equals("cybrthc") || abit_A.getSpecial6().equals("сникерс"))) {
           Enumeration paramNames = request.getParameterNames();
           while(paramNames.hasMoreElements()) {
             String paramName = (String)paramNames.nextElement();
             String paramValue[] = request.getParameterValues(paramName);
             if(paramName.indexOf("cls") != -1) {
               kk = new String(paramName.substring(3));
               stmt = conn.prepareStatement("UPDATE Konkurs SET Zach=? WHERE KodKonkursa LIKE ?");
               if(paramValue[0] == null || (paramValue[0] != null && paramValue[0].equals("")))
                 stmt.setNull(1,Types.VARCHAR);
               else
                 stmt.setObject(1,StringUtil.toDB(paramValue[0]),Types.VARCHAR);
               stmt.setObject(2,kk,Types.VARCHAR);
               stmt.executeUpdate();
             }
           }
         }

// Переходим к просмотру результатов модификации

    form.setAction(us.getClientIntName("show","view"));
  }

  if( form.getAction().equals("show")) {

// Сохранение параметров отбора для воспроизведения результатов на экране после их модификации

    if(session.getAttribute("doc_hran")==null)
      session.setAttribute("doc_hran",StringUtil.toMySQL((abit_A.getDokumentyHranjatsja()+"")));
    if(session.getAttribute("kod_fak")==null)
      session.setAttribute("kod_fak",StringUtil.toMySQL((abit_A.getKodFakulteta()+"")));
    if(session.getAttribute("kod_spec")==null)
      session.setAttribute("kod_spec",StringUtil.toMySQL(abit_A.getSpecial1().substring(abit_A.getSpecial1().indexOf("$")+1,abit_A.getSpecial1().indexOf("%"))+""));

/********* Выборка данных по абитуриентам **********/

    count=0;
    count_rek=0;
    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.DokumentyHranjatsja,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,kon.Zach,s.Abbreviatura,kon.NomerLichnogoDela,a.KodSpetsialnosti,a.TipDokSredObraz,kon.KodKonkursa FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND kon.KodSpetsialnosti LIKE ? AND a.KodVuza LIKE ? AND a.DokumentyHranjatsja LIKE ? ORDER BY kon.Zach DESC,a.Familija,a.Imja,a.Otchestvo");
    stmt.setObject(1,session.getAttribute("kod_spec"),Types.INTEGER);
    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(3,session.getAttribute("doc_hran"),Types.VARCHAR);
    rs = stmt.executeQuery();
    while(rs.next()) {
      AbiturientBean abit_TMP = new AbiturientBean();
      abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
      abit_TMP.setDokumentyHranjatsja(rs.getString(2));
      abit_TMP.setNomerLichnogoDela(rs.getString(3));
      abit_TMP.setFamilija(rs.getString(4));
      abit_TMP.setImja(rs.getString(5));
      abit_TMP.setOtchestvo(rs.getString(6));
      abit_TMP.setTipDokSredObraz(rs.getString(11));
      abit_TMP.setKodKonkursa(new Integer(rs.getInt(12)));
      abit_TMP.setSpecial5(StringUtil.ntv(rs.getString(7)));
      abit_TMP.setSpecial1(rs.getString(8));
      abit_TMP.setSpecial4(rs.getString(9));

      if(rs.getString(7) != null && rs.getString(7).equals("р")) count_rek++; 
      abit_TMP.setNumber(Integer.toString(++count));
      abits_A.add(abit_TMP);
    }

// Количество абитуриентов
      abit_A.setSpecial22(new Integer(count));
// Количество рекомендованных
      abit_A.setSpecial11(new Integer(count_rek));
      form.setAction(us.getClientIntName("full","view"));
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
        request.setAttribute("abit_A", abit_A);
        request.setAttribute("abits_A", abits_A);
        request.setAttribute("abit_A_S1", abit_A_S1);
        request.setAttribute("abit_A_S2", abit_A_S2);
        request.setAttribute("abit_A_S3", abit_A_S3);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S5", abit_A_S5);
     }
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}