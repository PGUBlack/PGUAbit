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
import java.util.Date;
import java.io.*;
import abit.sql.*; 

public class ListsKgBallAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    stmt               = null;
        PreparedStatement    stmt_a             = null;
        ResultSet            rs                 = null;
        ResultSet            rs_a               = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsKgBallForm      form               = (ListsKgBallForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_kgb_f        = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        int                  num_cols           = 0;
        StringBuffer         exclude_abits      = new StringBuffer("-1");
        UserBean             user               = (UserBean)session.getAttribute("user");
        String               name               = new String();
        String               file_con           = new String();

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsKgBallAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsKgBallForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

  String Abbr_Vuza = new String();
  stmt = conn.prepareStatement("SELECT AbbreviaturaVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) Abbr_Vuza = rs.getString(1).toUpperCase();

  name = "Список абит-в "+Abbr_Vuza+" по конк. гр. с набр. баллом";
  file_con = "lists_kgball_"+StringUtil.toEng(Abbr_Vuza)+"_ots";

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));
  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  stmt = conn.prepareStatement("SELECT COUNT(DISTINCT KodPredmeta) FROM NazvanijaPredmetov");
  rs = stmt.executeQuery();
  if(rs.next()) num_cols = rs.getInt(1);

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

  int num = 0;
  boolean anybody_exist = true;

// Перебор конкурсных групп

  stmt_a = conn.prepareStatement("SELECT DISTINCT kg.KodKonGruppy,kg.Nazvanie FROM KonGruppa kg,Spetsialnosti s,Abiturient a WHERE kg.KodKonGruppy=s.KodKonGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti ORDER BY kg.KodKonGruppy ASC");
  rs_a = stmt_a.executeQuery();
  while(rs_a.next()){

    report.write("\\par\n");
    report.write("\\fs28\\b1\\qc Список абитуриентов конкурсной группы №"+rs_a.getString(2)+"\n");
    report.write("\\b0\\par\\par\n");

// МЕДАЛИСТЫ

    num = 0;
    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,o.Otsenka)),k.ShifrKursov FROM Kursy k, Otsenki o, Spetsialnosti s,Abiturient a,KonGruppa kg, Medali m,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=o.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND a.KodAbiturienta = o.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND m.ShifrMedali NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,m.ShifrMedali,k.ShifrKursov ORDER BY 7 DESC,Familija,Imja,Otchestvo,m.ShifrMedali,NomerLichnogoDela ASC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2,rs_a.getString(1),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next())
    {
      if(anybody_exist) {
        report.write("\\fs26\\b1\\qc Медалисты\\b0\n");
        report.write("\\par\\par\n");
        // Шапка таблицы
        report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx800");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2300");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5100");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx7000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx8000");
        report.write("\\intbl\\b1  №\\cell");
        report.write("\\intbl Номер дела\\cell");
        report.write("\\intbl Фамилия И.О.\\cell");
        report.write("\\intbl Атт.\\cell");
        report.write("\\intbl Курсы\\cell");
        report.write("\\intbl Балл\\cell");
        report.write("\\intbl\\b0\\fs24\\row");
        anybody_exist = false;
      }
      exclude_abits.append(","+rs.getString(1));
      report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
      report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
      report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(8)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
      report.write("\\intbl\\row");
    }// цикл 
      if(!anybody_exist) report.write("\\pard\\par\\par\n");

// ЦЕЛЕВОЙ ПРИЕМ

    num = 0;
    anybody_exist = true;
    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,o.Otsenka)),k.ShifrKursov FROM Kursy k, Otsenki o, Spetsialnosti s,Abiturient a,KonGruppa kg, TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=o.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND a.KodAbiturienta = o.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND tp.ShifrPriema NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,tp.ShifrPriema,k.ShifrKursov ORDER BY 7 DESC,Familija,Imja,Otchestvo,tp.ShifrPriema,NomerLichnogoDela ASC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2,rs_a.getString(1),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next())
    {
      if(anybody_exist) {
        report.write("\\fs26\\b1\\qc Целевой прием\\b0\n");
        report.write("\\par\\par\n");
        // Шапка таблицы
        report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx800");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2300");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5100");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx7000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx8000");
        report.write("\\intbl\\b1  №\\cell");
        report.write("\\intbl Номер дела\\cell");
        report.write("\\intbl Фамилия И.О.\\cell");
        report.write("\\intbl Атт.\\cell");
        report.write("\\intbl Курсы\\cell");
        report.write("\\intbl Балл\\cell");
        report.write("\\intbl\\b0\\fs24\\row");
        anybody_exist = false;
      }
      exclude_abits.append(","+rs.getString(1));
      report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
      report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
      report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(8)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
      report.write("\\intbl\\row");
    }// цикл 
      if(!anybody_exist) report.write("\\pard\\par\\par\n");

// ЛЬГОТНИКИ

    num = 0;
    anybody_exist = true;
    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,o.Otsenka)),k.ShifrKursov FROM Kursy k, Otsenki o, Spetsialnosti s,Abiturient a,KonGruppa kg, Lgoty l,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=o.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND a.KodAbiturienta = o.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND l.ShifrLgot NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,k.ShifrKursov ORDER BY 7 DESC,Familija,Imja,Otchestvo,l.ShifrLgot,NomerLichnogoDela ASC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2,rs_a.getString(1),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next())
    {
      if(anybody_exist) {
        report.write("\\fs26\\qc\\b1 Льготники\\b0\n");
        report.write("\\par\\par\n");
        // Шапка таблицы
        report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx800");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2300");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5100");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx7000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx8000");
        report.write("\\intbl\\b1  №\\cell");
        report.write("\\intbl Номер дела\\cell");
        report.write("\\intbl Фамилия И.О.\\cell");
        report.write("\\intbl Атт.\\cell");
        report.write("\\intbl Курсы\\cell");
        report.write("\\intbl Балл\\cell");
        report.write("\\intbl\\b0\\fs24\\row");
        anybody_exist = false;
      }
      exclude_abits.append(","+rs.getString(1));
      report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
      report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
      report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(8)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
      report.write("\\intbl\\row");
    }// цикл 
      if(!anybody_exist) report.write("\\pard\\par\\par\n");

// ВСЕ ОСТАЛЬНЫЕ

    num = 0;
    anybody_exist = true;
    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,o.Otsenka)),k.ShifrKursov FROM Kursy k, Otsenki o, Spetsialnosti s,Abiturient a,KonGruppa kg, Medali m,Lgoty l,TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=o.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND m.KodMedali=a.KodMedali AND a.KodAbiturienta = o.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND m.ShifrMedali LIKE 'н' AND l.ShifrLgot LIKE 'н' AND tp.ShifrPriema LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema,k.ShifrKursov ORDER BY 7 DESC,Familija,Imja,Otchestvo,NomerLichnogoDela,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema ASC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2,rs_a.getString(1),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next())
    {
      if(anybody_exist) {
        report.write("\\fs26\\qc\\b1 Основные\\b0\n");
        report.write("\\par\\par\n");

        // Шапка таблицы
        report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx800");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2300");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5100");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx7000");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx8000");
        report.write("\\intbl\\b1  №\\cell");
        report.write("\\intbl Номер дела\\cell");
        report.write("\\intbl Фамилия И.О.\\cell");
        report.write("\\intbl Атт.\\cell");
        report.write("\\intbl Курсы\\cell");
        report.write("\\intbl Балл\\cell");
        report.write("\\intbl\\b0\\row");
        report.write("\\fs24");
        anybody_exist = false;
      }
      report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
      report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
      report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(8)+"\\cell");
      report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
      report.write("\\intbl\\row");
    }// цикл 

    if(!anybody_exist) report.write("\\pard\\page");
    anybody_exist = true;
}
    report.write("}");
    report.close();

    form.setAction(us.getClientIntName("new_rep","crt"));
    return mapping.findForward("rep_brw");         

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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_kgb_f) return mapping.findForward("lists_kgb_f");
        return mapping.findForward("success");
    }
}