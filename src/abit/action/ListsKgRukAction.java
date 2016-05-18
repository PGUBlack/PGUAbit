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

public class ListsKgRukAction extends Action {

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
        PreparedStatement    stmt_b             = null;
        ResultSet            rs                 = null;
        ResultSet            rs_a               = null;
        ResultSet            rs_b               = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsKgBallForm      form               = (ListsKgBallForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_kgb_f        = false;
        boolean              error              = false;
        boolean              overload           = false;
        ActionForward        f                  = null;
        int                  num_cols           = 0;
        int                  total_abits        = 0;
        int                  count_predm        = 0;
        int                  kol_zach_abits     = 0;
        int                  plan_priema        = 0;
        StringBuffer         exclude_abits      = new StringBuffer("-1");
        String               seria_nomer_Pass   = "-";
        String               sql_query          = "";
        ArrayList            kodes_k_gr         = new ArrayList();
        UserBean             user               = (UserBean)session.getAttribute("user");
        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsKgRukAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsRukForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

      if ( form.getAction() == null ) {

           form.setAction(us.getClientIntName("view","init"));

      } else if ( form.getAction().equals("report")) {

          String Abbr_Vuza = new String();
          stmt = conn.prepareStatement("SELECT AbbreviaturaVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
          stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          if(rs.next()) Abbr_Vuza = rs.getString(1).toUpperCase();

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

          String name = "Список абит-в "+Abbr_Vuza+" для руководства ";

          String file_con = "lists_kgruk_"+StringUtil.toEng(Abbr_Vuza)+"_";

          if(abit_SD.getPriznakSortirovki().equals("sotsenkoi") && abit_SD.getSpecial1() != null) {

            name += "с оценками";
            file_con += "predm_ots";

          } else if(abit_SD.getPriznakSortirovki().equals("first")) {

            if(abit_SD.getSpecial1() != null) {

              name += "с оценками (первый этап)";
              file_con += "predm_ots_1e";

            } else {

              name += " (первый этап)";
              file_con += "1e";
            }

          } else if(abit_SD.getPriznakSortirovki().equals("second")) {

            if(abit_SD.getSpecial1() != null) {

              name += "с оценками (второй этап)";
              file_con += "predm_ots_2e";

            } else {

              name += " (второй этап)";
              file_con += "2e";
            }

          } else if(abit_SD.getPriznakSortirovki().equals("third")) {

            if(abit_SD.getSpecial1() != null) {

              name += "с оценками (третий этап)";
              file_con += "predm_ots_3e";

            } else {

              name += " (третий этап)";
              file_con += "3e";
            }
          }

          session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

          ServletContext context = session.getServletContext();

          String realContextPath = context.getRealPath(request.getContextPath());        

          String file_name = realContextPath.substring(0,realContextPath.lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
       
          BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

          stmt = conn.prepareStatement("SELECT COUNT(DISTINCT KodPredmeta) FROM NazvanijaPredmetov");
          rs = stmt.executeQuery();
          if(rs.next()) num_cols = rs.getInt(1);

          report.write("{\\rtf1\\ansi\n");
          report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

          int num = 0;
          String old_kod_kg = "-", kody_kg = "-1";
          boolean anybody_exist = true, merge_kgrs = false;

// Чтение и анализ конкурсных групп

// В список с оценками не включаются 10, 11, 12, 13 и 14 конкурсные группы, так как они содержат специальности с различным набором предметов

          if(abit_SD.getSpecial1() != null)
            stmt_a = conn.prepareStatement("SELECT DISTINCT kg.KodKonGruppy,kg.Nazvanie FROM KonGruppa kg,Spetsialnosti s,Abiturient a WHERE kg.KodKonGruppy=s.KodKonGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND kg.Nazvanie NOT IN('10','11','12','13','14') AND kg.KodVuza LIKE ? ORDER BY kg.KodKonGruppy ASC");
          else
            stmt_a = conn.prepareStatement("SELECT DISTINCT kg.KodKonGruppy,kg.Nazvanie FROM KonGruppa kg,Spetsialnosti s,Abiturient a WHERE kg.KodKonGruppy=s.KodKonGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND kg.KodVuza LIKE ? ORDER BY kg.KodKonGruppy ASC");
          stmt_a.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs_a = stmt_a.executeQuery();
          while(rs_a.next()){

            if(abit_SD.getPriznakSortirovki().equals("third") && rs_a.getString(2).indexOf("8.") == -1) merge_kgrs = false;

            if(old_kod_kg != rs_a.getString(1) && !merge_kgrs) {

              if(!old_kod_kg.equals("-")) {

                ListBean lb = new ListBean();

                lb.setName(name);

                lb.setProperty1(kody_kg);

                kodes_k_gr.add(lb);

                kody_kg = "-1";

                merge_kgrs = false;
              }

              kody_kg = old_kod_kg = rs_a.getString(1);

              name = rs_a.getString(2);

              if(abit_SD.getPriznakSortirovki().equals("third") && rs_a.getString(2).indexOf("8.") != -1) {

                merge_kgrs = true;

                name = rs_a.getString(2).substring(0,1);
              }

            } else {

              kody_kg += ","+rs_a.getString(1);

            }
          }
          if(!old_kod_kg.equals("-")) {

            ListBean lb = new ListBean();

            lb.setName(name);

            lb.setProperty1(kody_kg);

            kodes_k_gr.add(lb);
          }

// Перебор конкурсных групп

          for(int curr_kg = 0;curr_kg < kodes_k_gr.size();curr_kg++) {

            total_abits = 0;

            overload = false;

            stmt_b = conn.prepareStatement("SELECT SUM(PlanPriema) FROM Spetsialnosti s,KonGruppa kg WHERE s.KodKonGruppy=kg.KodKonGruppy AND kg.KodVuza LIKE ? AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+")");
            stmt_b.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs_b = stmt_b.executeQuery();
            if(rs_b.next()) plan_priema = rs_b.getInt(1);
            else plan_priema = 0;

            if(abit_SD.getPriznakSortirovki().equals("first")) {

              kol_zach_abits = 0;

            } else if(abit_SD.getPriznakSortirovki().equals("second") && !overload) {

              stmt_b = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,Spetsialnosti s,KonGruppa kg WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodKonGruppy=kg.KodKonGruppy AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND a.Prinjat IN ('1') AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND kg.KodVuza LIKE ? AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+")");
              stmt_b.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
              rs_b = stmt_b.executeQuery();
              if(rs_b.next()) kol_zach_abits = rs_b.getInt(1);
              else kol_zach_abits = 0;

            } else if(abit_SD.getPriznakSortirovki().equals("third") && !overload) {

              stmt_b = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,Spetsialnosti s,KonGruppa kg WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodKonGruppy=kg.KodKonGruppy AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND a.Prinjat IN ('1','2') AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND kg.KodVuza LIKE ? AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+")");
              stmt_b.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
              rs_b = stmt_b.executeQuery();
              if(rs_b.next()) kol_zach_abits = rs_b.getInt(1);
              else kol_zach_abits = 0;
            }

            if(abit_SD.getSpecial1() != null) {

              stmt_b = conn.prepareStatement("SELECT COUNT(DISTINCT np.Sokr) FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti=ens.KodSpetsialnosti AND s.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+")");
              rs_b = stmt_b.executeQuery();
              if(rs_b.next()) count_predm = rs_b.getInt(1);
            }

            report.write("\\par\n");

            report.write("\\fs28\\b1\\qc Список абитуриентов конкурсной группы №"+((ListBean)kodes_k_gr.get(curr_kg)).getName()+" с суммарным баллом ЕГЭ\n");

            report.write("\\b0\\par\\par\n");

            report.write("\\fs22\\b1\\qc План приёма: "+plan_priema+"  Вакансии: "+(plan_priema-kol_zach_abits)+"\n");

            report.write("\\b0\\par\\par\n");

            if(!abit_SD.getPriznakSortirovki().equals("sotsenkoi")) {

              report.write("\\fs28\\b1\\qc Рекомендованы к зачислению:\n");

              report.write("\\b0\\par\\par\n");
            }

// ЛЬГОТНИКИ

            num = 0;
            anybody_exist = true;

            if(abit_SD.getPriznakSortirovki().equals("sotsenkoi")) {

              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Lgoty l,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND a.KodAbiturienta = zo.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND l.ShifrLgot NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,l.ShifrLgot,NomerLichnogoDela ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("first")) {

              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Lgoty l,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND a.KodAbiturienta = zo.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND l.ShifrLgot NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,l.ShifrLgot,NomerLichnogoDela ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("second")) {

              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Lgoty l,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND a.KodAbiturienta = zo.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND l.ShifrLgot NOT LIKE 'н' AND Prinjat LIKE 'н' AND a.TipDokSredObraz IN('о','к') AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,l.ShifrLgot,NomerLichnogoDela ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("third")) {

              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Lgoty l,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND a.KodAbiturienta = zo.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND l.ShifrLgot NOT LIKE 'н' AND Prinjat LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,l.ShifrLgot,NomerLichnogoDela ASC";
            }

            stmt = conn.prepareStatement(sql_query);
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
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
                if(abit_SD.getSpecial1() != null) {
                  for(int i=1; i<=count_predm;i++) 
                     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(6000+i*720)+"\n");
                }
                report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(7200+count_predm*720)+"\n");
                report.write("\\intbl\\b1  №\\cell");
                report.write("\\intbl Номер дела\\cell");
                report.write("\\intbl Фамилия И.О.\\cell");
                report.write("\\intbl Атт.\\cell");
                if(abit_SD.getSpecial1() != null) {
                  stmt_b = conn.prepareStatement("SELECT DISTINCT np.Sokr,np.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti=ens.KodSpetsialnosti AND s.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") ORDER BY np.KodPredmeta");
                  rs_b = stmt_b.executeQuery();
                  while(rs_b.next())                  
                    report.write("\\intbl\\fs22{"+rs_b.getString(1)+"}\\fs24\\cell");
                }
                report.write("\\intbl{Сумма баллов}\\cell");
                report.write("\\intbl\\b0\\fs24\\row");
                anybody_exist = false;
              }

              exclude_abits.append(","+rs.getString(1));

              if(!seria_nomer_Pass.equals(rs.getString(9)+rs.getString(10))) {

                total_abits++;

                if(!abit_SD.getPriznakSortirovki().equals("sotsenkoi") && !overload) {

                  if(abit_SD.getPriznakSortirovki().equals("first")) {

                    if(total_abits > plan_priema) overload = true;

                  } else if(abit_SD.getPriznakSortirovki().equals("second") && !overload) {

                    if(total_abits > (plan_priema-kol_zach_abits)) overload = true;

                  } else if(abit_SD.getPriznakSortirovki().equals("third") && !overload) {

                    if(total_abits > (plan_priema-kol_zach_abits)) overload = true;
                  }

                    if(overload) {

                      num = 0;

                      report.write("\\pard\\par\\par\n");
                      report.write("\\fs28\\b1\\qc Список резерва для зачисления:\n");
                      report.write("\\b0\\par\\par\n");
// Шапка таблицы
                      report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx800");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2300");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5100");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6000");
                      if(abit_SD.getSpecial1() != null) {
                        for(int i=1; i<=count_predm;i++) 
                           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(6000+i*720)+"\n");
                      }
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(7200+count_predm*720)+"\n");

                      report.write("\\intbl\\b1  №\\cell");
                      report.write("\\intbl Номер дела\\cell");
                      report.write("\\intbl Фамилия И.О.\\cell");
                      report.write("\\intbl Атт.\\cell");
                      if(abit_SD.getSpecial1() != null) {
                        stmt_b = conn.prepareStatement("SELECT DISTINCT np.Sokr,np.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti=ens.KodSpetsialnosti AND s.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") ORDER BY np.KodPredmeta");
                        rs_b = stmt_b.executeQuery();
                        while(rs_b.next())                  
                          report.write("\\intbl\\fs22{"+rs_b.getString(1)+"}\\fs24\\cell");
                      }
                      report.write("\\intbl{Сумма баллов}\\cell");
                      report.write("\\intbl\\b0\\fs24\\row");
                    }
                }

                report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");

                seria_nomer_Pass = rs.getString(9)+rs.getString(10);

              } else {

                report.write("\\intbl \\qc\\b0 "+"\\cell");
              }

              report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
              report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
              report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
              if(abit_SD.getSpecial1() != null) {
                stmt_b = conn.prepareStatement("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens,Abiturient a WHERE zso.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta");
                stmt_b.setObject(1,rs.getString(1),Types.INTEGER);
                rs_b = stmt_b.executeQuery();
                while(rs_b.next())                  
                  report.write("\\intbl \\qc "+rs_b.getString(1)+"\\cell");
              }
              report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
              report.write("\\intbl\\row");
            }
              if(!anybody_exist) report.write("\\pard\\par\\par\n");

// ЦЕЛЕВОЙ ПРИЕМ

            num = 0;
            anybody_exist = true;

            if(abit_SD.getPriznakSortirovki().equals("sotsenkoi")) {

              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND tp.ShifrPriema NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,tp.ShifrPriema,NomerLichnogoDela ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("first")) {
              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND tp.ShifrPriema NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,tp.ShifrPriema,NomerLichnogoDela ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("second")) {
              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND Prinjat LIKE 'н' AND a.TipDokSredObraz IN('о','к') AND tp.ShifrPriema NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,tp.ShifrPriema,NomerLichnogoDela ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("third")) {
              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND Prinjat LIKE 'н' AND tp.ShifrPriema NOT LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,tp.ShifrPriema,NomerLichnogoDela ASC";
            }

            stmt = conn.prepareStatement(sql_query);
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
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
                if(abit_SD.getSpecial1() != null) {
                  for(int i=1; i<=count_predm;i++) 
                     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(6000+i*720)+"\n");
                }
                report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(7200+count_predm*720)+"\n");
                report.write("\\intbl\\b1  №\\cell");
                report.write("\\intbl Номер дела\\cell");
                report.write("\\intbl Фамилия И.О.\\cell");
                report.write("\\intbl Атт.\\cell");
                if(abit_SD.getSpecial1() != null) {
                  stmt_b = conn.prepareStatement("SELECT DISTINCT np.Sokr,np.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti=ens.KodSpetsialnosti AND s.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") ORDER BY np.KodPredmeta");
                  rs_b = stmt_b.executeQuery();
                  while(rs_b.next())                  
                    report.write("\\intbl\\fs22{"+rs_b.getString(1)+"}\\fs24\\cell");
                }
                report.write("\\intbl{Сумма баллов}\\cell");
                report.write("\\intbl\\b0\\fs24\\row");
                anybody_exist = false;
              }

              exclude_abits.append(","+rs.getString(1));

              if(!seria_nomer_Pass.equals(rs.getString(9)+rs.getString(10))) {

                total_abits++;

                if(!abit_SD.getPriznakSortirovki().equals("sotsenkoi") && !overload) {

                  stmt_b = conn.prepareStatement("SELECT SUM(PlanPriema) FROM Spetsialnosti s,KonGruppa kg WHERE s.KodKonGruppy=kg.KodKonGruppy AND kg.KodVuza LIKE ? AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+")");
                  stmt_b.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                  rs_b = stmt_b.executeQuery();
                  if(rs_b.next()) plan_priema = rs_b.getInt(1);
                  else plan_priema = 0;

                  if(abit_SD.getPriznakSortirovki().equals("first") && !overload) {

                    if(total_abits > plan_priema) overload = true;

                  } else if(abit_SD.getPriznakSortirovki().equals("second") && !overload) {

                    if(total_abits > (plan_priema-kol_zach_abits)) overload = true;

                  } else if(abit_SD.getPriznakSortirovki().equals("third") && !overload) {

                    if(total_abits > (plan_priema-kol_zach_abits)) overload = true;

                  }

                    if(overload) {

                      num = 0;

                      report.write("\\pard\\par\\par\n");
                      report.write("\\fs28\\b1\\qc Список резерва для зачисления:\n");
                      report.write("\\b0\\par\\par\n");
// Шапка таблицы
                      report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx800");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2300");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5100");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6000");
                      if(abit_SD.getSpecial1() != null) {
                        for(int i=1; i<=count_predm;i++) 
                           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(6000+i*720)+"\n");
                      }                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(7200+count_predm*720)+"\n");
                      report.write("\\intbl\\b1  №\\cell");
                      report.write("\\intbl Номер дела\\cell");
                      report.write("\\intbl Фамилия И.О.\\cell");
                      report.write("\\intbl Атт.\\cell");
                      if(abit_SD.getSpecial1() != null) {
                        stmt_b = conn.prepareStatement("SELECT DISTINCT np.Sokr,np.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti=ens.KodSpetsialnosti AND s.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") ORDER BY np.KodPredmeta");
                        rs_b = stmt_b.executeQuery();
                        while(rs_b.next())                  
                          report.write("\\intbl\\fs22{"+rs_b.getString(1)+"}\\fs24\\cell");
                      }
                      report.write("\\intbl{Сумма баллов}\\cell");
                      report.write("\\intbl\\b0\\fs24\\row");
                    }
                }

                report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");

                seria_nomer_Pass = rs.getString(9)+rs.getString(10);

              } else {

                report.write("\\intbl \\qc\\b0 "+"\\cell");
              }

              report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
              report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
              report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
              if(abit_SD.getSpecial1() != null) {
                stmt_b = conn.prepareStatement("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens,Abiturient a WHERE zso.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta");
                stmt_b.setObject(1,rs.getString(1),Types.INTEGER);
                rs_b = stmt_b.executeQuery();
                while(rs_b.next())                  
                  report.write("\\intbl \\qc "+rs_b.getString(1)+"\\cell");
              }
              report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
              report.write("\\intbl\\row");

            } 

              if(!anybody_exist) report.write("\\pard\\par\\par\n");

// ВСЕ ОСТАЛЬНЫЕ

            num = 0;
            anybody_exist = true;

            if(abit_SD.getPriznakSortirovki().equals("sotsenkoi")) {

              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Medali m,Lgoty l,TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND m.KodMedali=a.KodMedali AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND l.ShifrLgot LIKE 'н' AND tp.ShifrPriema LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,NomerLichnogoDela,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("first")) {
              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Medali m,Lgoty l,TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND m.KodMedali=a.KodMedali AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND l.ShifrLgot LIKE 'н' AND tp.ShifrPriema LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,NomerLichnogoDela,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("second")) {
              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Medali m,Lgoty l,TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND m.KodMedali=a.KodMedali AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND Prinjat LIKE 'н'  AND a.TipDokSredObraz IN('о','к') AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND l.ShifrLgot LIKE 'н' AND tp.ShifrPriema LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,NomerLichnogoDela,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema ASC";

            } else if(abit_SD.getPriznakSortirovki().equals("third")) {
              sql_query = "SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,TipDokSredObraz,SUM(CONVERT(int,zo.OtsenkaEge)),k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta FROM Kursy k, ZajavlennyeShkolnyeOtsenki zo, Spetsialnosti s,Abiturient a,KonGruppa kg, Medali m,Lgoty l,TselevojPriem tp,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND ens.KodPredmeta=zo.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND l.KodLgot=a.KodLgot AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND m.KodMedali=a.KodMedali AND a.KodAbiturienta = zo.KodAbiturienta AND (a.NomerPlatnogoDogovora IS NULL OR a.NomerPlatnogoDogovora LIKE '') AND DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN (SELECT a2.KodAbiturienta FROM Abiturient a1,Abiturient a2 WHERE a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat IN('1','2','3','д') AND a2.Prinjat NOT IN('1','2','3','д') AND a1.NomerDokumenta LIKE a2.NomerDokumenta AND a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.DokumentyHranjatsja LIKE 'д' AND a2.DokumentyHranjatsja LIKE 'д') AND Prinjat LIKE 'н' AND a.KodAbiturienta NOT IN ("+exclude_abits.toString()+") AND l.ShifrLgot LIKE 'н' AND tp.ShifrPriema LIKE 'н' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema,k.ShifrKursov,a.SeriaDokumenta,a.NomerDokumenta ORDER BY 7 DESC,Familija,Imja,Otchestvo,NomerLichnogoDela,m.ShifrMedali,l.ShifrLgot,tp.ShifrPriema ASC";
            }

            stmt = conn.prepareStatement(sql_query);
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
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
                if(abit_SD.getSpecial1() != null) {
                for(int i=1; i<=count_predm;i++) 
                   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(6000+i*720)+"\n");
                }
                report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(7200+count_predm*720)+"\n");
                report.write("\\intbl\\b1  №\\cell");
                report.write("\\intbl Номер дела\\cell");
                report.write("\\intbl Фамилия И.О.\\cell");
                report.write("\\intbl Атт.\\cell");
                if(abit_SD.getSpecial1() != null) {
                  stmt_b = conn.prepareStatement("SELECT DISTINCT np.Sokr,np.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti=ens.KodSpetsialnosti AND s.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") ORDER BY np.KodPredmeta");
                  rs_b = stmt_b.executeQuery();
                  while(rs_b.next())                  
                    report.write("\\intbl\\fs22{"+rs_b.getString(1)+"}\\fs24\\cell");
                }
                report.write("\\intbl{Сумма баллов}\\cell");
                report.write("\\intbl\\b0\\row");
                report.write("\\fs24");
                anybody_exist = false;
              }

              if(!seria_nomer_Pass.equals(rs.getString(9)+rs.getString(10))) {

                total_abits++;

                if(!abit_SD.getPriznakSortirovki().equals("sotsenkoi") && !overload) {

                  stmt_b = conn.prepareStatement("SELECT SUM(PlanPriema) FROM Spetsialnosti s,KonGruppa kg WHERE s.KodKonGruppy=kg.KodKonGruppy AND kg.KodVuza LIKE ? AND kg.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+")");
                  stmt_b.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                  rs_b = stmt_b.executeQuery();
                  if(rs_b.next()) plan_priema = rs_b.getInt(1);
                  else plan_priema = 0;

                  if(abit_SD.getPriznakSortirovki().equals("first") && !overload) {

                    if(total_abits > plan_priema) overload = true;

                  } else if(abit_SD.getPriznakSortirovki().equals("second") && !overload) {

                    if(total_abits > (plan_priema-kol_zach_abits)) overload = true;

                  } else if(abit_SD.getPriznakSortirovki().equals("third") && !overload) {

                    if(total_abits > (plan_priema-kol_zach_abits)) overload = true;

                  }

                    if(overload) {

                      num = 0;

                      report.write("\\pard\\par\\par\n");
                      report.write("\\fs28\\b1\\qc Список резерва для зачисления:\n");
                      report.write("\\b0\\par\\par\n");
// Шапка таблицы
                      report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx800");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2300");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5100");
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6000");
                      if(abit_SD.getSpecial1() != null) {
                        for(int i=1; i<=count_predm;i++) 
                           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(6000+i*720)+"\n");
                      }
                      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx"+(7200+count_predm*720)+"\n");
                      report.write("\\intbl\\b1  №\\cell");
                      report.write("\\intbl Номер дела\\cell");
                      report.write("\\intbl Фамилия И.О.\\cell");
                      report.write("\\intbl Атт.\\cell");
                      if(abit_SD.getSpecial1() != null) {
                        stmt_b = conn.prepareStatement("SELECT DISTINCT np.Sokr,np.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti=ens.KodSpetsialnosti AND s.KodKonGruppy IN ("+((ListBean)kodes_k_gr.get(curr_kg)).getProperty1()+") ORDER BY np.KodPredmeta");
                        rs_b = stmt_b.executeQuery();
                        while(rs_b.next())                  
                          report.write("\\intbl\\fs22{"+rs_b.getString(1)+"}\\fs24\\cell");
                      }
                      report.write("\\intbl{Сумма баллов}\\cell");
                      report.write("\\intbl\\b0\\row");
                      report.write("\\fs24");
                    }
                }

                report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");

                seria_nomer_Pass = rs.getString(9)+rs.getString(10);

              } else {

                report.write("\\intbl \\qc\\b0 "+"\\cell");
              }

              report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
              report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
              report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
              if(abit_SD.getSpecial1() != null) {
                stmt_b = conn.prepareStatement("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens,Abiturient a WHERE zso.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta");
                stmt_b.setObject(1,rs.getString(1),Types.INTEGER);
                rs_b = stmt_b.executeQuery();
                while(rs_b.next())                  
                  report.write("\\intbl \\qc "+rs_b.getString(1)+"\\cell");
              }
              report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
              report.write("\\intbl\\row");

            }

            if(!anybody_exist) report.write("\\pard\\page");
            anybody_exist = true;

       } // Перебор конкурсных групп

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
          if ( rs_a != null ) {
               try {
                     rs_a.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( rs_b != null ) {
               try {
                     rs_b.close();
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
          if ( stmt_a != null ) {
               try {
                     stmt_a.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( stmt_b != null ) {
               try {
                     stmt_b.close();
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
        request.setAttribute("abit_SD", abit_SD);
        if(error) return mapping.findForward("error");
        if(lists_kgb_f) return mapping.findForward("lists_kgb_f");
        return mapping.findForward("success");
    }
}