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

public class EditSertAction extends Action {

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
        request.setAttribute("editSertAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

    try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

    UserConn us = new UserConn(request, mapping);
    conn = us.getConn(user.getSid());
    request.setAttribute( "editSertForm", form );

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
      if(paramName.indexOf("sob") != -1) {
    	  kodAb = new String(paramName.substring(3));
  // Обновление диапазона ЕГЭ

          stmt = conn.prepareStatement("UPDATE konkurs SET prof='"+(paramValue[0]).trim()+"' WHERE kodspetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti()+"' AND KodAbiturienta LIKE '"+kodAb+"'");
          stmt.executeUpdate();
        }
      
      
      
      if(paramName.indexOf("ege") != -1) {
        kodAb = new String(paramName.substring(3,paramName.indexOf("%")));
        kodPr = new String(paramName.substring(paramName.indexOf("%")+1));
// Обновление диапазона ЕГЭ

        stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge='"+(paramValue[0]).trim()+"' WHERE KodPredmeta LIKE '"+kodPr+"' AND KodAbiturienta LIKE '"+kodAb+"'");
        stmt.executeUpdate();
      }

      if(paramName.indexOf("lck") != -1) {
        kodAb = new String(paramName.substring(3));

// Установка блокировки на оценки
//        if((paramValue[0]) != null && StringUtil.RecodeLetter((paramValue[0]).trim()).equals("д")) {
        if((paramValue[0]) != null && StringUtil.toDB((paramValue[0]).trim()).equals("д")) {

          stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET Locked='д' WHERE KodAbiturienta LIKE '"+kodAb+"'");
          stmt.executeUpdate();
        }
// Установка разблокировки на оценки
//        if((paramValue[0]) != null && StringUtil.RecodeLetter((paramValue[0]).trim()).equals("н")) {
        if((paramValue[0]) != null && StringUtil.toDB((paramValue[0]).trim()).equals("н")) {

          stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET Locked=NULL WHERE KodAbiturienta LIKE '"+kodAb+"'");
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
                  abit_ENS_S2.add(abit_TMP);
                  priznak = true;
                  kPredmeta = "";
              }
              kPredmeta += "%" + rs.getString(2);
            }
            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
            abit_TMP2.setAbbreviatura(oldAbbr);
            abit_ENS_S2.add(abit_TMP2);

// Выборка кода медали и шифра для формы ввода
            String tmp="";
            /*stmt = conn.prepareStatement("SELECT ShifrMedali FROM Medali WHERE KodVuza LIKE ? ORDER BY KodMedali ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next())
              tmp += rs.getString(1) +",";
// В свойстве ShifrMedali будет храниться список сокращений для медалей
            if(tmp != null) abit_ENS.setShifrMedali(tmp.substring(0,tmp.length()-1));*/
}


/*****************************************************************************/
/******************   ОТОБРАЖЕНИЕ РЕЗУЛЬТАТОВ ПЕРЕВОДА   *********************/
/*********************   С ВОЗМОЖНОСТЬЮ ИХ КОРРЕКЦИИ   ***********************/
/*****************************************************************************/

// Выводятся абитуриенты, включая тех, кто подтвердил свои медали

 if(form.getAction().equals("show")) {

            abit_ENS.setKodSpetsialnosti(new Integer((abit_ENS.getSpecial1()).substring((abit_ENS.getSpecial1()).indexOf("$")+1,(abit_ENS.getSpecial1()).indexOf("%"))));
            abit_ENS.setSpecial1(abit_ENS.getSpecial1());

// Название факультета
             stmt = conn.prepareStatement("SELECT f.Fakultet FROM Fakultety f WHERE f.KodFakulteta LIKE ? AND f.KodVuza LIKE ?");
             stmt.setObject(1,abit_ENS.getKodFakulteta(),Types.INTEGER);
             stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) 
               abit_ENS.setFakultet(rs.getString(1).toUpperCase());

// Выбранная специальность
             stmt = conn.prepareStatement("SELECT NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
             stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) 
               abit_ENS.setSpecial2(rs.getString(1));


             int kol_vo = 0;
             int old_kA = -1;
             String query2 = new String();

             if(abit_ENS.getSpecial3().equals("not")) {

// Предметы на специальности

               stmt = conn.prepareStatement("SELECT DISTINCT np.KodPredmeta,np.Sokr FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodSpetsialnosti LIKE ? ORDER BY np.KodPredmeta");
               stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
               rs = stmt.executeQuery();
               while (rs.next()) {
                 AbiturientBean predm = new AbiturientBean();
                 predm.setSokr(rs.getString(2));
                 predms.add(predm);
               }

// Выборка абитуриентов со всеми оценками

               query2 = "SELECT zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaEge,zo.KodPredmeta,a.TipDokSredObraz,a.Prinjat,zo.Locked,a.SeriaDokumenta,a.NomerDokumenta,k.prof FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Spetsialnosti s,konkurs k WHERE a.kodspetsialnosti=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.KodSpetsialnosti=s.KodSpetsialnosti AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti();
               query2+= "' AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti()+"')";
//               if((abit_ENS.getKopijaSertifikata()).equals("y"))
//                 query2+= " AND a.KopijaSertifikata LIKE 'к'";
//               else if((abit_ENS.getKopijaSertifikata()).equals("n"))
//                 query2+= " AND a.KopijaSertifikata LIKE 'о'";
//               else if((abit_ENS.getKopijaSertifikata()).equals("-"))
//                 query2+= " AND a.KopijaSertifikata LIKE '-'";
//               else
//                 query2+= " AND a.KopijaSertifikata LIKE '%'";
                 query2+= " GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaEge,a.TipDokSredObraz,a.Prinjat,zo.Locked,a.SeriaDokumenta,a.NomerDokumenta,k.prof ORDER BY a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.KodAbiturienta,zo.KodPredmeta";

             } else {

// ВСЕ ПРЕДМЕТЫ вст. испытаний ВУЗа

               stmt = conn.prepareStatement("SELECT DISTINCT np.KodPredmeta,np.Sokr FROM NazvanijaPredmetov np ORDER BY np.KodPredmeta");
               rs = stmt.executeQuery();
               while (rs.next()) {
                 AbiturientBean predm = new AbiturientBean();
                 predm.setSokr(rs.getString(2));
                 predms.add(predm);
               }

// Выборка абитуриентов с оценками по специальности

               query2 = "SELECT zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaEge,zo.KodPredmeta,a.TipDokSredObraz,a.Prinjat,zo.Locked,a.SeriaDokumenta,a.NomerDokumenta,k.prof FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Spetsialnosti s,konkurs k WHERE a.kodspetsialnosti=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.KodSpetsialnosti=s.KodSpetsialnosti AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti()+"' GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaEge,a.TipDokSredObraz,a.Prinjat,zo.Locked,a.SeriaDokumenta,a.NomerDokumenta,k.prof ORDER BY a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.KodAbiturienta,zo.KodPredmeta";
              // query2+= "' ";
//               if((abit_ENS.getKopijaSertifikata()).equals("y"))
//                 query2+= " AND a.KopijaSertifikata LIKE 'к'";
//               else if((abit_ENS.getKopijaSertifikata()).equals("n"))
//                 query2+= " AND a.KopijaSertifikata LIKE 'о'";
//               else if((abit_ENS.getKopijaSertifikata()).equals("-"))
//                 query2+= " AND a.KopijaSertifikata LIKE '-'";
//               else
//                 query2+= " AND a.KopijaSertifikata LIKE '%'";
                 //query2+= " GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,m.ShifrMedali,a.NomerSertifikata,a.KopijaSertifikata,zo.OtsenkaEge,a.TipDokSredObraz,a.Prinjat,zo.Locked,a.SeriaDokumenta,a.NomerDokumenta ORDER BY a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.KodAbiturienta,zo.KodPredmeta";
             }

             stmt = conn.prepareStatement(query2);
             rs = stmt.executeQuery();
             while(rs.next()){
               if(old_kA != rs.getInt(1)){
                 kol_vo++;
                 old_kA = rs.getInt(1);
               }
               AbiturientBean abit = new AbiturientBean();
               abit.setKodAbiturienta(new Integer(rs.getString(1)));
               abit.setFamilija(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" ");
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
               abit.setAttestat(rs.getString(8));
               abit.setPrinjat(rs.getString(9));
               abit.setLocked(StringUtil.ntv(rs.getString(10)));
               abit.setPasport(StringUtil.ntv(rs.getString(11))+" "+StringUtil.ntv(rs.getString(12)));
               abit.setOtsenkaegeabiturienta(new Integer(rs.getString(6)));
               abit.setKodPredmeta(new Integer(rs.getString(7)));
               abit.setNpd1(StringUtil.ntv(rs.getString(13)));
               abit_ENS_S1.add(abit);
             }

             abit_ENS.setNumber(""+kol_vo);
             request.setAttribute("specs", specs);
             request.setAttribute("predms", predms);
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