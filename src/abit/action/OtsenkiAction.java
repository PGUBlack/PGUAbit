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

public class OtsenkiAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        ResultSet         rs            = null;
        ResultSet         rs1           = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        OtsenkiForm       form          = (OtsenkiForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        boolean           error         = false;
        ActionForward     f             = null;
        String            kRajona       = null;
        String            kOblasti      = null;
        Integer           kAbiturienta  = null;
        int               count         = 0;
        ArrayList         predmets      = new ArrayList();
        ArrayList         abits_A       = new ArrayList();
        ArrayList         otsenki       = new ArrayList();
        ArrayList         abit_A_S1     = new ArrayList();
        ArrayList         abit_A_S2     = new ArrayList();
        UserBean          user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "otsenkiAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "otsenkiForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************** Подготовка данных для ввода с помощью селекторов ************************/

   if ( form.getAction() == null ) {

      form.setAction(us.getClientIntName("menu","init"));

// ****************** факультеты ****************************

   stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
   stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
   rs = stmt.executeQuery();
   while (rs.next()) {
     AbiturientBean abit_TMP = new AbiturientBean();
     abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
     abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
     abit_A_S1.add(abit_TMP);
   }


// ****************** группы на факультете ****************************

   boolean save = false;
   String oldKodFak = "";
   String kodeline = "";

   stmt = conn.prepareStatement("SELECT DISTINCT g.KodFakulteta,g.KodGruppy,g.Gruppa FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND KodGruppy NOT LIKE 1 AND KodVuza LIKE ? ORDER BY g.KodFakulteta,g.Gruppa ASC");
   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
   rs = stmt.executeQuery();
   while (rs.next()) {
     if(!rs.getString(1).equals(oldKodFak)) {
       if(save) {
         AbiturientBean tmp = new AbiturientBean();
         tmp.setKodGruppy(new Integer(oldKodFak));
         tmp.setSpecial2(kodeline);
// KodGruppy хранит Код Факультета, к которому относятся группы
// Special2 имеет формат списка: KodFakulteta%Gruppa1%KodGruppy1%Gruppa2%KodGruppy2%Gruppa3%KodGruppy3...
         abit_A_S2.add(tmp);
       }
         oldKodFak = rs.getString(1);
         kodeline = rs.getString(3)+"%"+rs.getString(2);
         save = true;
     } else kodeline += "%"+rs.getString(3)+"%"+rs.getString(2);
   }
// Запись последней строки в массив кодов и групп
   AbiturientBean tmp = new AbiturientBean();
   tmp.setKodGruppy(new Integer(oldKodFak));
   tmp.setSpecial2(kodeline);
   abit_A_S2.add(tmp);


// ****************** предметы ****************************

   stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY KodPredmeta ASC");
   stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
   rs = stmt.executeQuery();
   while (rs.next()) {
     AbiturientBean predm = new AbiturientBean();
     predm.setPredmet(rs.getString(1));
     predm.setKodPredmeta(new Integer(rs.getString(2)));
     predmets.add(predm);
   }

/*************************************************************/
/*************** СОХРАНЕНИЕ/ОБНОВЛЕНИЕ ОЦЕНОК ****************/
/*************************************************************/
} else if( form.getAction().equals("save")) {

    String kodAb="0",kodPr="0";
    Enumeration paramNames = request.getParameterNames();
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();
      String paramValue[] = request.getParameterValues(paramName);
      if(paramName.indexOf("otsn") != -1) {
        kodAb = new String(paramName.substring(paramName.indexOf("%")+1));
        kodPr = new String(paramName.substring(4,paramName.indexOf("%")));

// Обновление оценки
        if(!(""+abit_A.getKodPredmeta()).equals("-1")) {
// С установкой даты
//            stmt = conn.prepareStatement("UPDATE Otsenki SET Otsenka='"+(paramValue[0]).trim()+"',Data='"+StringUtil.DataConverter(abit_A.getDataJekzamena())+"' WHERE KodAbiturienta LIKE '"+kodAb+"' AND KodPredmeta LIKE '"+kodPr+"'");    
//            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge='"+(paramValue[0]).trim()+"',Data='"+StringUtil.DataConverter(abit_A.getDataJekzamena())+"' WHERE KodAbiturienta LIKE '"+kodAb+"' AND KodPredmeta LIKE '"+kodPr+"'");    
            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge='"+(paramValue[0]).trim()+"' WHERE KodAbiturienta LIKE '"+kodAb+"' AND KodPredmeta LIKE '"+kodPr+"'");
        } else
// Без установки даты
//            stmt = conn.prepareStatement("UPDATE Otsenki SET Otsenka='"+(paramValue[0]).trim()+"' WHERE KodAbiturienta LIKE '"+kodAb+"' AND KodPredmeta LIKE '"+kodPr+"'");
            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge='"+(paramValue[0]).trim()+"' WHERE KodAbiturienta LIKE '"+kodAb+"' AND KodPredmeta LIKE '"+kodPr+"'");
        }

        stmt.executeUpdate();

// Установка признака подтверждения медали
//        stmt = conn.prepareStatement("SELECT * FROM Abiturient a, Medali m, Otsenki o,Spetsialnosti s WHERE a.KodAbiturienta=o.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodMedali=m.KodMedali AND m.ShifrMedali IN ('з','с') AND s.KodPredmeta=o.KodPredmeta AND o.Otsenka LIKE '"+Constants.maxExBall+"' AND a.KodAbiturienta LIKE '"+kodAb+"'");
//        rs = stmt.executeQuery();
//        if(rs.next()){
//          stmt = conn.prepareStatement("UPDATE Abiturient SET PodtvMed='д' WHERE KodAbiturienta LIKE '"+kodAb+"'");
//          stmt.executeUpdate();
//        }
      }

      form.setAction(us.getClientIntName("show","act-save-view"));
    }

    if( form.getAction().equals("show")) {

// Всего абитуриентов на факультете
    count = 0;
    stmt = conn.prepareStatement("SELECT a.KodAbiturienta FROM Abiturient a,Spetsialnosti s WHERE a.KodVuza LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE ?");
    stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2, abit_A.getKodFakulteta(),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next()) count++;
    abit_A.setSpecial1(""+count);

// Всего абитуриентов в группе
    count = 0;
    stmt = conn.prepareStatement("SELECT a.KodAbiturienta FROM Abiturient a WHERE a.KodVuza LIKE ? AND a.KodGruppy LIKE ?");
    stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2, abit_A.getKodGruppy(),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next()) count++;
    abit_A.setSpecial3(""+count);

    if(!(""+abit_A.getKodPredmeta()).equals("-1")) {

//***********************************************************************************************************
//************************************** Вывод оценок по заданному предмету *********************************
//***********************************************************************************************************

// Название предмета
       stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND KodPredmeta LIKE ?");
       stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
       stmt.setObject(2, abit_A.getKodPredmeta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) abit_A.setPredmet(rs.getString(1));

// Абитуриенты с оценками
//       stmt = conn.prepareStatement("SELECT a.KodAbiturienta,o.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,g.Gruppa FROM Gruppy g,Abiturient a,Otsenki o,Spetsialnosti s,Medali m WHERE m.KodMedali=a.KodMedali AND g.KodGruppy=a.KodGruppy AND a.KodGruppy LIKE ? AND a.DokumentyHranjatsja LIKE ? AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta=o.KodAbiturienta AND o.KodPredmeta LIKE ? GROUP BY a.KodAbiturienta,o.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,g.Gruppa ORDER BY a.Familija,a.Imja,a.Otchestvo");
       stmt = conn.prepareStatement("SELECT a.KodAbiturienta,zso.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,zso.OtsenkaEge,g.Gruppa FROM Gruppy g,Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti s,Medali m WHERE m.KodMedali=a.KodMedali AND g.KodGruppy=a.KodGruppy AND a.KodGruppy LIKE ? AND a.DokumentyHranjatsja LIKE ? AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta LIKE ? GROUP BY a.KodAbiturienta,zso.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,zso.OtsenkaEge,g.Gruppa ORDER BY a.Familija,a.Imja,a.Otchestvo");
       stmt.setObject(1, abit_A.getKodGruppy(),Types.INTEGER);
       stmt.setObject(2, abit_A.getDokumentyHranjatsja(),Types.VARCHAR);
       stmt.setObject(3, abit_A.getKodPredmeta(),Types.INTEGER);
       rs = stmt.executeQuery();
       while (rs.next()) {
         AbiturientBean abit = new AbiturientBean();
         abit.setKodAbiturienta(new Integer(rs.getString(1)));
         abit.setKodPredmeta(new Integer(rs.getString(2)));
         abit.setDokumentyHranjatsja(rs.getString(3));
         abit.setShifrSpetsialnosti(rs.getString(4));
         abit.setNomerLichnogoDela(rs.getString(5));
         abit.setFamilija(rs.getString(6)+" "+rs.getString(7).substring(0,1).toUpperCase()+"."+rs.getString(8).substring(0,1).toUpperCase()+".");
         abit.setShifrMedali(rs.getString(9));
         abit.setSpecial1(rs.getString(10));
         abits_A.add(abit);
       }
       form.setAction(us.getClientIntName("full1","view-1-pr"));
    } else if(abit_A.getSpecial8().equals("all") && (""+abit_A.getKodPredmeta()).equals("-1")) {

//***********************************************************************************************************
//***************************************** Вывод оценок по ВСЕМ предметам **********************************
//***********************************************************************************************************

// Названия предметов
       stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY KodPredmeta ASC");
       stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
       rs = stmt.executeQuery();
       while (rs.next()) {
         AbiturientBean predm = new AbiturientBean();
         predm.setSokr(rs.getString(1));
         predmets.add(predm);
       }

// Абитуриенты с оценками
//       stmt = conn.prepareStatement("SELECT a.KodAbiturienta,o.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,g.Gruppa FROM Medali m,Gruppy g,Abiturient a,Otsenki o,Spetsialnosti s WHERE m.KodMedali=a.KodMedali AND g.KodGruppy=a.KodGruppy AND a.KodGruppy LIKE ? AND a.DokumentyHranjatsja LIKE ? AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta=o.KodAbiturienta AND o.KodPredmeta IN(SELECT DISTINCT KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ?) GROUP BY a.KodAbiturienta,o.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,g.Gruppa ORDER BY a.Familija,a.Imja,a.Otchestvo");
       stmt = conn.prepareStatement("SELECT a.KodAbiturienta,zso.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,zso.OtsenkaEge,g.Gruppa FROM Medali m,Gruppy g,Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti s WHERE m.KodMedali=a.KodMedali AND g.KodGruppy=a.KodGruppy AND a.KodGruppy LIKE ? AND a.DokumentyHranjatsja LIKE ? AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta IN(SELECT DISTINCT KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ?) GROUP BY a.KodAbiturienta,zso.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,zso.OtsenkaEge,g.Gruppa ORDER BY a.Familija,a.Imja,a.Otchestvo");
       stmt.setObject(1, abit_A.getKodGruppy(),Types.INTEGER);
       stmt.setObject(2, abit_A.getDokumentyHranjatsja(),Types.VARCHAR);
       stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
       rs = stmt.executeQuery();
       while (rs.next()) {
         AbiturientBean abit = new AbiturientBean();
         abit.setKodAbiturienta(new Integer(rs.getString(1)));
         abit.setKodPredmeta(new Integer(rs.getString(2)));
         abit.setDokumentyHranjatsja(rs.getString(3));
         abit.setShifrSpetsialnosti(rs.getString(4));
         abit.setNomerLichnogoDela(rs.getString(5));
         abit.setFamilija(rs.getString(6)+" "+rs.getString(7).substring(0,1).toUpperCase()+"."+rs.getString(8).substring(0,1).toUpperCase()+".");
         abit.setShifrMedali(rs.getString(9));
         abit.setSpecial1(rs.getString(10));
         abits_A.add(abit);
       }
       form.setAction(us.getClientIntName("full","view-all-pr"));
       } else if(abit_A.getSpecial8().equals("needed") && (""+abit_A.getKodPredmeta()).equals("-1")) {
//***********************************************************************************************************
//********************************* Вывод оценок по предметам на СПЕЦИАЛЬНОСТИ ******************************
//***********************************************************************************************************

// Названия предметов
       stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND KodPredmeta IN(SELECT DISTINCT ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens,Spetsialnosti sp WHERE ens.KodSpetsialnosti=sp.KodSpetsialnosti AND sp.KodFakulteta LIKE ?) ORDER BY KodPredmeta ASC");
       stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
       stmt.setObject(2, abit_A.getKodFakulteta(),Types.INTEGER);
       rs = stmt.executeQuery();
       while (rs.next()) {
         AbiturientBean predm = new AbiturientBean();
         predm.setSokr(rs.getString(1));
         predmets.add(predm);
       }

// Абитуриенты с оценками
//       stmt = conn.prepareStatement("SELECT a.KodAbiturienta,o.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,g.Gruppa FROM Medali m,Gruppy g,Abiturient a,Otsenki o,Spetsialnosti s WHERE m.KodMedali=a.KodMedali AND g.KodGruppy=a.KodGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta=o.KodAbiturienta AND a.DokumentyHranjatsja LIKE ? AND o.KodPredmeta IN(SELECT DISTINCT KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND a.KodGruppy LIKE ? AND KodPredmeta IN(SELECT DISTINCT ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens,Spetsialnosti sp WHERE ens.KodSpetsialnosti=sp.KodSpetsialnosti AND sp.KodFakulteta LIKE ?)) GROUP BY a.KodAbiturienta,o.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,g.Gruppa ORDER BY a.Familija,a.Imja,a.Otchestvo");
       stmt = conn.prepareStatement("SELECT a.KodAbiturienta,zso.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,zso.OtsenkaEge,g.Gruppa FROM Medali m,Gruppy g,Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti s WHERE m.KodMedali=a.KodMedali AND g.KodGruppy=a.KodGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND a.DokumentyHranjatsja LIKE ? AND zso.KodPredmeta IN(SELECT DISTINCT KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND a.KodGruppy LIKE ? AND KodPredmeta IN(SELECT DISTINCT ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens,Spetsialnosti sp WHERE ens.KodSpetsialnosti=sp.KodSpetsialnosti AND sp.KodFakulteta LIKE ?)) GROUP BY a.KodAbiturienta,zso.KodPredmeta,a.DokumentyHranjatsja,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,zso.OtsenkaEge,g.Gruppa ORDER BY a.Familija,a.Imja,a.Otchestvo");
       stmt.setObject(1, abit_A.getDokumentyHranjatsja(),Types.VARCHAR);
       stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
       stmt.setObject(3, abit_A.getKodGruppy(),Types.INTEGER);
       stmt.setObject(4, abit_A.getKodFakulteta(),Types.INTEGER);
       rs = stmt.executeQuery();
       while (rs.next()) {
         AbiturientBean abit = new AbiturientBean();
         abit.setKodAbiturienta(new Integer(rs.getString(1)));
         abit.setKodPredmeta(new Integer(rs.getString(2)));
         abit.setDokumentyHranjatsja(rs.getString(3));
         abit.setShifrSpetsialnosti(rs.getString(4));
         abit.setNomerLichnogoDela(rs.getString(5));
         abit.setFamilija(rs.getString(6)+" "+rs.getString(7).substring(0,1).toUpperCase()+"."+rs.getString(8).substring(0,1).toUpperCase()+".");
         abit.setShifrMedali(rs.getString(9));
         abit.setSpecial1(rs.getString(10));
         abits_A.add(abit);
       }
       form.setAction(us.getClientIntName("full3","view-spec-pr"));
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
        request.setAttribute("predmets", predmets);
        request.setAttribute("abit_A", abit_A);
        request.setAttribute("abits_A", abits_A);
        request.setAttribute("abit_A_S1", abit_A_S1);
        request.setAttribute("abit_A_S2", abit_A_S2);
     }
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}