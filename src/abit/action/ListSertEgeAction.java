package abit.action;

import java.io.*;
import java.util.Date;
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

public class ListSertEgeAction extends Action {

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
        ListSertEgeForm              form           = (ListSertEgeForm) actionForm;
        AbiturientBean               abit_ENS       = form.getBean(request, errors);
        boolean                      error          = false;
        ActionForward                f              = null;
        ArrayList                    fgr_msgs       = new ArrayList();
        ArrayList                    abit_ENS_S1    = new ArrayList();
        ArrayList                    abit_ENS_S2    = new ArrayList();
        boolean                      priznak        = true;
        int                          nomer          = 0;
        int                          old_kodAb      = 0;
        int                          kAbit          = -1;
        int                          count_predm    = 0;
        String                       file_con       = new String();
        String                       kPredmeta      = new String();
        String                       oldKodeS       = new String();
        String                       oldKodeF       = new String();
        String                       oldNazv        = new String();
        UserBean                     user           = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
        request.setAttribute("listSertEgeAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listSertEgeForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/**************  ПОДГОТОВКА ДАННЫХ ДЛЯ ФОРМЫ НАСТРОЕК ПАРАМЕТРОВ ОТЧЕТА  ***************/
/***************************************************************************************/

 if(form.getAction() == null) {

            form.setAction(us.getClientIntName("translate","init"));

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
            stmt = conn.prepareStatement("SELECT ShifrMedali FROM Medali WHERE KodVuza LIKE ? ORDER BY KodMedali ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next())
              tmp += rs.getString(1) +",";
// В свойстве ShifrMedali будет храниться список сокращений для медалей
            if(tmp != null) abit_ENS.setShifrMedali(tmp.substring(0,tmp.length()-1));
 }
/*****************************************************************************/
/*******************  ГЕНЕРАЦИЯ RTF-ФАЙЛА РЕЗУЛЬТАТОВ  ***********************/
/*****************************************************************************/

 if(form.getAction().equals("report")) {

  abit_ENS.setKodSpetsialnosti(new Integer((abit_ENS.getSpecial1()).substring((abit_ENS.getSpecial1()).indexOf("$")+1,(abit_ENS.getSpecial1()).indexOf("%"))));

  file_con = "list_sert_ege_";
  String AA="",BB="";
// Название факультета
             stmt = conn.prepareStatement("SELECT f.Fakultet,f.AbbreviaturaFakulteta FROM Fakultety f WHERE f.KodFakulteta LIKE ? AND f.KodVuza LIKE ?");
             stmt.setObject(1,abit_ENS.getKodFakulteta(),Types.INTEGER);
             stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) {
               abit_ENS.setFakultet(rs.getString(1).toUpperCase());
               file_con += StringUtil.toEng(rs.getString(2));
               AA = rs.getString(2).toUpperCase();
             }
// Выбранная специальность
             stmt = conn.prepareStatement("SELECT NazvanieSpetsialnosti,Abbreviatura FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
             stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) {
               abit_ENS.setSpecial2(rs.getString(1));
               file_con += "_"+StringUtil.toEng(rs.getString(2));
               BB = rs.getString(2).toUpperCase();
             }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  String name = "Список с № серт. и ЕГЭ "+AA+" спец-ти "+BB;

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd\\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

  stmt = conn.prepareStatement("SELECT COUNT(Sokr) FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ?");
  stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
    count_predm = rs.getInt(1);
  }

  stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs40 \\qc "+rs.getString(1)+"\n");

    report.write("\\par\\par\n");

    report.write("\\fs28 \\b1\\qc "+abit_ENS.getFakultet().toUpperCase()+" \\b0\n");
    report.write("\\par\\fs28\\b1 \\qc "+abit_ENS.getSpecial2()+" \\b0\n");
    report.write("\\par\\par\\pard\n");

    report.write("\\fs22 \\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1700\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6900\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+count_predm*800)+"\n");
    report.write("\\intbl\\qc №\\cell\n");
    report.write("\\intbl Номер личного дела\\cell\n");
    report.write("\\intbl Фамилия И.О.\\cell\n");
    report.write("\\intbl Шифр отличий\\cell\n");
    report.write("\\intbl Номер сертификата\\cell\n");
    report.write("\\intbl Копия сертиф.\\cell\n");
    report.write("\\intbl Предметы/Баллы ЕГЭ\\cell\n");
    report.write("\\intbl \\row\n");
    report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1700\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6900\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");

    for(int col=1;col<=count_predm;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+col*800)+"\n");

    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");

    stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE ?) ORDER BY KodPredmeta ASC");
    stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next()) 
      report.write("\\intbl{"+rs.getString(1)+"}\\cell\n");

    report.write("\\intbl\\row\n");

// Формат строки данных отчета

    report.write("\\b0\\fs24\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1700\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6900\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");
    for(int col=1;col<=count_predm;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+col*800)+"\n");

/*************************************/
/***** Подготовка строки запроса *****/
/*************************************/

    int kol_vo = 0;
    int old_kA = -1;
    String query2 = new String();
      query2 = "SELECT zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.NomerSertifikata,a.KopijaSertifikata,m.ShifrMedali,zo.OtsenkaEge,zo.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o WHERE o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti();
      query2+= "' AND m.ShifrMedali IN(" + StringUtil.PlaceUnaryComas("m.ShifrMedali",abit_ENS.getShifrMedali())+") AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti()+"')";
    if((abit_ENS.getKopijaSertifikata()).equals("yes"))
      query2+= " AND a.KopijaSertifikata LIKE 'д'";
    else if((abit_ENS.getKopijaSertifikata()).equals("no"))
      query2+= " AND a.KopijaSertifikata LIKE 'н'";
    else if((abit_ENS.getKopijaSertifikata()).equals("-"))
      query2+= " AND a.KopijaSertifikata LIKE '-'";
    else
      query2+= " AND a.KopijaSertifikata LIKE '%'";
    query2+= " GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerSertifikata,a.KopijaSertifikata,zo.OtsenkaEge ORDER BY a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela";

    stmt = conn.prepareStatement(query2);
    rs = stmt.executeQuery();
    while(rs.next()) {
    if(kAbit != rs.getInt(1)) {
      if(kAbit != -1) report.write("\\intbl\\row\n");
      kAbit = rs.getInt(1); 
      report.write("\\intbl\\qc "+(++nomer)+"\\cell\n");
      report.write("\\intbl\\qc "+rs.getString(5)+"\\cell\n"); // NLD
      report.write("\\intbl\\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1)+". "+rs.getString(4).substring(0,1)+".\\cell\n"); // FIO
      report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SHIFR OTLICHIY
      report.write("\\intbl\\ql "+rs.getString(6)+"\\cell\n"); // N SERTIFIKATA
      report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // KOPIJA SERTIFIKATA
      report.write("\\intbl\\qc "+StringUtil.voidFilter(rs.getString(9))+"\\cell\n"); // OTSENKA
    } else {
      report.write("\\intbl\\qc "+StringUtil.voidFilter(rs.getString(9))+"\\cell\n");
    }
  }
  if(kAbit != -1) report.write("\\intbl \\row\n");

  report.write("}"); 
  report.close();
  form.setAction(us.getClientIntName("new_rep","crt"));
  return mapping.findForward("rep_brw"); 
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
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}