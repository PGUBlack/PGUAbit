package abit.action;

import java.io.IOException;
import java.io.PrintWriter;
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
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

public class EditSochAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession                  session        = request.getSession();
        Connection                   conn           = null;
        PreparedStatement            stmt           = null;
        PreparedStatement            stmt_a         = null;
        PreparedStatement            stmt_b         = null;
        ResultSet                    rs             = null;
        ResultSet                    rs_a           = null;
        ResultSet                    rs_b           = null;
        ActionErrors                 errors         = new ActionErrors();
        ActionError                  msg            = null;
        EditSertForm                 form           = (EditSertForm) actionForm;
        AbiturientBean               abit_ENS       = form.getBean(request, errors);
        boolean                      error          = false;
        ActionForward                f              = null;
        ArrayList                    fgr_msgs       = new ArrayList();
        ArrayList                    specs          = new ArrayList();
        ArrayList                    predms         = new ArrayList();
        ArrayList                    abit_ENS_S1    = new ArrayList();
        ArrayList                    abit_ENS_S2    = new ArrayList();
        ArrayList                    abit_ENS_S3    = new ArrayList();
        boolean                      priznak        = true;
        int                          number         = 0;
        int                          old_kodAb      = 0;
        String                       predmets       = new String();
        String                       codeLine       = new String();
        String                       specialQ       = new String();
        String                       kPredmeta      = new String();
        String                       oldKodeS       = new String();
        String                       oldKodeF       = new String();
        String                       oldNazv        = new String();
        UserBean                     user           = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
        request.setAttribute("editSochAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

    try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

    UserConn us = new UserConn(request, mapping);
    conn = us.getConn(user.getSid());
    request.setAttribute( "editSochForm", form );

/*****************  Возврат к предыдущей странице   *******************/
    if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

    if (form.getAction() == null) form.setAction(us.getClientIntName("translator","init"));

/****************************************************************************************/
    if (form.getAction().equals("save")) {

//  abit_ENS.setKodSpetsialnosti(new Integer((abit_ENS.getSpecial1()).substring((abit_ENS.getSpecial1()).indexOf("$")+1,(abit_ENS.getSpecial1()).indexOf("%"))));

    String kodAb="0",kodPr="0";
    Enumeration paramNames = request.getParameterNames();
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();
      String paramValue[] = request.getParameterValues(paramName);
     /* if(paramName.indexOf("att") != -1) {
        kodAb = new String(paramName.substring(3));
// Обновление признака копии аттестата

//        stmt = conn.prepareStatement("UPDATE Abiturient SET TipDokSredObraz='"+StringUtil.RecodeLetter((paramValue[0]).trim())+"' WHERE KodAbiturienta LIKE '"+kodAb+"'");
        stmt = conn.prepareStatement("UPDATE Abiturient SET TipDokSredObraz='"+StringUtil.toDB((paramValue[0]).trim())+"' WHERE KodAbiturienta LIKE '"+kodAb+"'");
        stmt.executeUpdate();
      }
      if(paramName.indexOf("nom") != -1) {
        kodAb = new String(paramName.substring(3));
// Обновление номера сертификата

        stmt = conn.prepareStatement("UPDATE Abiturient SET NomerSertifikata='"+(paramValue[0]).trim()+"' WHERE KodAbiturienta LIKE '"+kodAb+"'");
        stmt.executeUpdate();
      }
      if(paramName.indexOf("kop") != -1) {
        kodAb = new String(paramName.substring(3));
// Обновление признака копии сертификата

//        stmt = conn.prepareStatement("UPDATE Abiturient SET KopijaSertifikata='"+StringUtil.RecodeLetter((paramValue[0]).trim())+"' WHERE KodAbiturienta LIKE '"+kodAb+"'");
        stmt = conn.prepareStatement("UPDATE Abiturient SET KopijaSertifikata='"+StringUtil.toDB((paramValue[0]).trim())+"' WHERE KodAbiturienta LIKE '"+kodAb+"'");
        stmt.executeUpdate();
      }*/
      
      if(paramName.indexOf("ots") != -1) {
          kodAb = new String(paramName.substring(3));
           stmt = conn.prepareStatement("UPDATE AbitDopInf SET ballsoch='"+StringUtil.toDB((paramValue[0]).trim())+"' WHERE KodAbiturienta LIKE '"+kodAb+"'");
          stmt.executeUpdate();
        }
      
      
    

      if(paramName.indexOf("soc") != -1) {
        kodAb = new String(paramName.substring(3));

// Установка блокировки на оценки
//        if((paramValue[0]) != null && StringUtil.RecodeLetter((paramValue[0]).trim()).equals("д")) {
        if((paramValue[0]) != null && StringUtil.toDB((paramValue[0]).trim()).equals("д")) {

          stmt = conn.prepareStatement("UPDATE Abiturient SET Stepen_mag='д' WHERE kodabiturienta LIKE '"+kodAb+"'");
          stmt.executeUpdate();
        }
// Установка разблокировки на оценки
//        if((paramValue[0]) != null && StringUtil.RecodeLetter((paramValue[0]).trim()).equals("н")) {
        if((paramValue[0]) != null && StringUtil.toDB((paramValue[0]).trim()).equals("н")) {

          stmt = conn.prepareStatement("UPDATE Abiturient SET Stepen_mag=NULL WHERE kodabiturienta LIKE '"+kodAb+"'");
          stmt.executeUpdate();
        }
      }
    }

    form.setAction(us.getClientIntName("show","act-save"));

        }else if (form.getAction().equals("translator")) {
/**************************************************************************************************/
/**************  ПОДГОТОВКА ДАННЫХ ДЛЯ ФОРМЫ НАСТРОЕК ПАРАМЕТРОВ ПЕРЕВОДА ОЦЕНОК ЕГЭ ***************/
/**************************************************************************************************/

            form.setAction(us.getClientIntName("translate","view-form"));

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_ENS_S1.add(abit_TMP);
            }

        }

/*****************************************************************************/
/******************   ОТОБРАЖЕНИЕ РЕЗУЛЬТАТОВ ПЕРЕВОДА   *********************/
/*********************   С ВОЗМОЖНОСТЬЮ ИХ КОРРЕКЦИИ   ***********************/
/*****************************************************************************/

// Выводятся абитуриенты, включая тех, кто подтвердил свои медали

 if(form.getAction().equals("show")) {

// Название факультета
             stmt = conn.prepareStatement("SELECT f.Fakultet FROM Fakultety f WHERE f.KodFakulteta LIKE ? AND f.KodVuza LIKE ?");
             stmt.setObject(1,abit_ENS.getKodFakulteta(),Types.INTEGER);
             stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) 
               abit_ENS.setFakultet(rs.getString(1).toUpperCase());



             int kol_vo = 0;
             int old_kA = -1;
             String query2 = new String();

      

// Предметы на специальности

      

// Выборка абитуриентов со всеми оценками


// Выборка абитуриентов с оценками по специальности

               query2 = "SELECT DISTINCT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.SeriaDokumenta,a.NomerDokumenta,a.stepen_mag,adi.ballsoch FROM Abiturient a, abitdopinf adi,spetsialnosti s,fakultety f WHERE a.kodabiturienta=adi.kodabiturienta and adi.ballsoch not like '0' AND a.KodSpetsialnosti=s.KodSpetsialnosti and s.kodfakulteta like '"+abit_ENS.getKodFakulteta()+"' AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" GROUP BY a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.SeriaDokumenta,a.NomerDokumenta,a.stepen_mag,adi.ballsoch,a.kodabiturienta ORDER BY a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela";


             stmt = conn.prepareStatement(query2);
             rs = stmt.executeQuery();
             while(rs.next()){
               if(old_kA != rs.getInt(1)){
                 kol_vo++;
                 old_kA = rs.getInt(1);
               }
               AbiturientBean abit = new AbiturientBean();
               abit.setKodAbiturienta(new Integer(rs.getString(1)));
               abit.setFamilija(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
               abit.setNomerLichnogoDela(rs.getString(5));
               /*if(rs.getString(6)!=null) {
                 if(rs.getString(6).equals("null")) {
                   abit.setNomerSertifikata("");
                 } 
                 else abit.setNomerSertifikata(rs.getString(6));
               } 
               else abit.setNomerSertifikata("");*/

              // abit.setKopijaSertifikata(rs.getString(7));
              // abit.setShifrMedali(rs.getString(8));
              
              
               abit.setPasport(StringUtil.ntv(rs.getString(6))+" "+StringUtil.ntv(rs.getString(7)));
               if(rs.getString(8)!=null){
               abit.setStepen_Mag(rs.getString(8));
               }else{
            	   abit.setStepen_Mag("");
               }
               abit.setOlimp_1(rs.getString(9));
               abit_ENS_S1.add(abit);
             }

             abit_ENS.setNumber(""+kol_vo);
             request.setAttribute("specs", specs);

             form.setAction(us.getClientIntName("results","view"));
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
        request.setAttribute("abit_ENS", abit_ENS);
        request.setAttribute("fgr_msgs", fgr_msgs);
        request.setAttribute("abit_ENS_S1", abit_ENS_S1);
        request.setAttribute("abit_ENS_S2", abit_ENS_S2);
        request.setAttribute("abit_ENS_S3", abit_ENS_S3);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}