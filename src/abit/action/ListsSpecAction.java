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

public class ListsSpecAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    stmt               = null;
        ResultSet            rs                 = null;
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsSpecForm        form               = (ListsSpecForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_spec_f       = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        int                  count_predm        = 0;
        String               str1               = new String();
        String               str2               = new String();
        String               str3               = new String();
        String               str4               = new String();
        String               note               = new String();
        ArrayList            notes              = new ArrayList();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsSpecAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsSpecForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************* Подготовка данных для ввода с помощью селекторов *************************/

            stmt = conn.prepareStatement("SELECT DISTINCT ShifrFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setShifrFakulteta(rs.getString(1));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_SD_S1.add(abit_TMP);
                 }
            boolean priznak = true;
            String kPredmeta = new String();
            String oldKode = new String();
            String oldAbbr = new String();
            stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,EkzamenyNaSpetsialnosti.KodPredmeta,Abbreviatura,AbbreviaturaFakulteta FROM Spetsialnosti,EkzamenyNaSpetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND KodVuza LIKE ? ORDER BY 3,2,4 ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial1(rs.getString(1));
              if(priznak) { oldKode = rs.getString(1); oldAbbr = rs.getString(3); priznak = false; }
              if(!oldKode.equals(rs.getString(1))) {
                if(kPredmeta.equals("")) kPredmeta += "%" + rs.getString(2);
                  abit_TMP.setSpecial1(oldKode + kPredmeta);
                  abit_TMP.setAbbreviatura(oldAbbr);
                  abit_SD_S2.add(abit_TMP);
                  priznak = true;
                  kPredmeta = "";
              }
              kPredmeta += "%" + rs.getString(2);
            }
            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial1(oldKode + kPredmeta);
            abit_TMP2.setAbbreviatura(oldAbbr);
            abit_SD_S2.add(abit_TMP2);

/************************************************************************************************/
            if ( form.getAction() == null ) {

                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report") || abit_SD.getSpecial1().equals("-1")) {

/**********************************************************************************/
/*****  Если action равен otchet , то входим в секцию - создание отчёта  **********/

	int number = 0;

        String name = new String();

	String file_con = new String();	   // Имя файла отчёта

// Cпециальность

        if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
        {
	  file_con = "lsts_sp_kon_"+StringUtil.CurrDate("-");

	} else {

	  file_con = "lsts_sp_bd_"+StringUtil.CurrDate("-");
	}

        if(abit_SD.getSpecial1() != null && abit_SD.getSpecial1().equals("-1")) {

          stmt = conn.prepareStatement("SELECT s.Abbreviatura,f.AbbreviaturaFakulteta FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.ShifrFakulteta LIKE ?");
          stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
          rs = stmt.executeQuery();
          if(rs.next()) {
            file_con = file_con+"_"+StringUtil.toEng(rs.getString(2));
            if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
              name = "Перечень бюдж-ов для фак. "+rs.getString(2).toUpperCase()+" на "+StringUtil.CurrDate(".");
            else
              name = "Перечень дог-ов для фак. "+rs.getString(2).toUpperCase()+" на "+StringUtil.CurrDate(".");
          }

        } else {

          stmt = conn.prepareStatement("SELECT s.Abbreviatura,f.AbbreviaturaFakulteta FROM Spetsialnosti s,Fakultety f WHERE f.KodFakulteta=s.KodFakulteta AND s.KodSpetsialnosti LIKE ?");
          stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
          rs = stmt.executeQuery();
          if(rs.next()) {
            file_con = file_con+"_"+StringUtil.toEng(rs.getString(1));
            if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
              name = "Перечень бюдж-ов по сп. "+rs.getString(1).toUpperCase()+" на "+StringUtil.CurrDate(".");
            else
              name = "Перечень контр-ов по сп. "+rs.getString(1).toUpperCase()+" на "+StringUtil.CurrDate(".");
          }
        }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

   session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

   String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

   BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

   report.write("{\\rtf1\\ansi\n");
   report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

// Перебор всех  специальностей указанного факультета (или выбор одной конкретной специальности)

   String Budj = new String();
   String Dogv = new String();

// Бюджетники
   if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
     Budj = " AND k.Bud LIKE 'д'";

// Договорники
   if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
     Dogv = " AND k.Dog LIKE 'д'";

   stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.ShifrSpetsialnosti,s.NazvanieSpetsialnosti,s.Abbreviatura,s.PlanPriema FROM Spetsialnosti s,Fakultety f,Konkurs k WHERE k.KodSpetsialnosti=s.KodSpetsialnosti AND f.KodFakulteta=s.KodFakulteta"+Budj+Dogv+" AND f.ShifrFakulteta LIKE ? AND s.KodSpetsialnosti LIKE ?");
   stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
// Специальность(и)
   if(abit_SD.getSpecial1() != null && abit_SD.getSpecial1().equals("-1"))
     stmt.setObject(2,"%",Types.VARCHAR);
   else
     stmt.setObject(2,abit_SD.getKodSpetsialnosti(),Types.VARCHAR);
   rs3 = stmt.executeQuery();
   while(rs3.next()) {

      stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) 
        report.write("\\fs36\\qc{"+rs.getString(1)+"}\\par\\fs14\\par\\fs28\\b0\n");

      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
      stmt.setObject(1,rs3.getString(1),Types.VARCHAR);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
          report.write("\\b1\\qc{Перечень лиц, поступающих на специальность/направление}\\par\\qc{"+rs.getString(1)+" }\\'ab{"+rs.getString(2)+"}\\'bb\\par{на места, финансируемые из бюджета}\\b0\n");
        if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
          report.write("\\b1\\qc{Перечень лиц, поступающих на специальность/направление}\\par\\qc{"+rs.getString(1)+" }\\'ab{"+rs.getString(2)+"}\\'bb\\par{на платной основе}\\b0\n");

        report.write("\\par\\b1\\qc{План приема: }"+rs.getString(4)+"\\b0\n");
      }

      number = 0;
      report.write("\\par\\par\n");
      report.write("\\fs20 \\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");

/***********************************/
/*****  Полная версия списка  ******/
/***********************************/

      if(abit_SD.getSpecial2() == null) {

        stmt = conn.prepareStatement("SELECT COUNT(Sokr) FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ?");
        stmt.setObject(1,rs3.getString(1),Types.VARCHAR);
        rs = stmt.executeQuery();
        while (rs.next()) {
          count_predm = rs.getInt(1);
        }

        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3700\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6900\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+count_predm*800)+"\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+count_predm*800)+"\n");

        report.write("\\intbl{№}\\cell\n");
        report.write("\\intbl{Номер личного дела}\\cell\n");
        report.write("\\intbl{Фамилия}\\cell\n");
        report.write("\\intbl{Имя}\\cell\n");
        report.write("\\intbl{Отчество}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Предметы/Оценки}\\cell\n");
        report.write("\\intbl{Сумма баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3700\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6900\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n");

        for(int col=1;col<=count_predm;col++)
         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+col*800)+"\n");

        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+count_predm*800)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt = conn.prepareStatement("SELECT Sokr,NazvanijaPredmetov.KodPredmeta FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ? ORDER BY EkzamenyNaSpetsialnosti.KodPredmeta ASC");
        stmt.setObject(1,rs3.getString(1),Types.VARCHAR);
        rs = stmt.executeQuery();
        while (rs.next()) {
          report.write("\\intbl{"+rs.getString(1)+"}\\cell\n");
        }
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3700\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5200\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6900\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n");
        for(int col=1;col<=count_predm;col++)
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+col*800)+"\n");

        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+count_predm*800)+"\n");

      } else {

/***********************************/
/***  Сокращенная версия списка  ***/
/***********************************/

        report.write("\\fs22\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4800\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n");

        report.write("\\intbl{№}\\cell\n");
        report.write("\\intbl{Номер личного дела}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Сумма баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4800\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n");
      }

/*************************** Выборка числа абитуриентов из БД ****************************/

      int old_Ka = -1, summa = 0;

      AbiturientBean abit_TMP = new AbiturientBean();

      if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
        stmt = conn.prepareStatement("SELECT a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,SUM(zso.OtsenkaEge) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k,EkzamenyNaSpetsialnosti ens WHERE k.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=k.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND k.Bud LIKE 'д' AND ens.KodSpetsialnosti LIKE ? AND zso.KodPredmeta=ens.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY 7 DESC");
      if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
        stmt = conn.prepareStatement("SELECT a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,SUM(zso.OtsenkaEge) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k,EkzamenyNaSpetsialnosti ens WHERE k.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=k.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND k.Dog LIKE 'д' AND ens.KodSpetsialnosti LIKE ? AND zso.KodPredmeta=ens.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY 7 DESC");

      stmt.setObject(1,rs3.getString(1),Types.VARCHAR);
      rs = stmt.executeQuery();
      while (rs.next()) {

/***********************************/
/*****  Полная версия списка  ******/
/***********************************/

         if(abit_SD.getSpecial2() == null) {

           report.write("\\intbl\\fs20\\qc{"+(++number)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
           report.write("\\intbl{" +rs.getString(4)+"}\\cell\n");
           report.write("\\intbl{" +rs.getString(5)+"}\\cell\n");
           report.write("\\intbl\\qc{" +rs.getString(6)+"}\\cell\n");

           stmt = conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge,ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens,Konkurs k,ZajavlennyeShkolnyeOtsenki zso WHERE zso.KodAbiturienta=k.KodAbiturienta AND k.KodSpetsialnosti=ens.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti LIKE ? AND k.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta");
           stmt.setObject(1,rs3.getString(1),Types.VARCHAR);
           stmt.setObject(2,rs.getString(1),Types.VARCHAR);
           rs2 = stmt.executeQuery();
           while (rs2.next()) {
             report.write("\\intbl\\qc{"+StringUtil.voidFilter(rs2.getString(1))+"}\\cell\n");
           }
           report.write("\\intbl\\qc{"+rs.getString(7)+"}\\cell\n");

         } else {

/***********************************/
/***  Сокращенная версия списка  ***/
/***********************************/

             report.write("\\intbl\\fs22\\qc{"+(++number)+"}\\cell\n");
             report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
             report.write("\\intbl\\ql{"+rs.getString(3)+" "+rs.getString(4).substring(0,1)+". "+rs.getString(5).substring(0,1)+"."+"}\\cell\n");
             report.write("\\intbl\\qc{"+rs.getString(6)+"}\\cell\n");
             report.write("\\intbl\\qc{"+rs.getString(7)+"}\\cell\n");
         }

         report.write("\\intbl\\row\n");
      }

      report.write("\\pard\\par\\par\\par\\par\\par");

      report.write("\\b0\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc\\cellx4000\n");
      report.write("\\clvertalc\\cellx5700\n");
      report.write("\\clvertalc\\cellx8200\n");

      report.write("\\intbl\\qr{Председатель приемной комиссии: }\\cell\n");

      stmt = conn.prepareStatement("SELECT Facsimile FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.VARCHAR);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
      } else {
        report.write("\\intbl\\cell\n");
      }

      String rektor = new String();
      stmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%Ректор%'");
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\intbl\\ql{  / "+rs.getString(2)+" /}\\cell\n");
      } else {
        report.write("\\intbl\\cell\n");
      }

      report.write("\\intbl\\row\n");
      report.write("\\pard");

      if(abit_SD.getSpecial1() != null && abit_SD.getSpecial1().equals("-1"))
        report.write("\\page");

   } // перебор специальностей на факультете

     report.write("}");
     report.close();

     form.setAction(us.getClientIntName("new_rep","crt"));
     return mapping.findForward("rep_brw");

   } else if ( form.getAction().equals("viewing")) {

/************************************************************************************************/
/*****  Если action равен viewing , то входим в секцию - создание записи или вывод таблицы  *****/

                 int number = 0;
                 int kol_predm = 0;              

                 form.setAction(us.getClientIntName("full","view"));

// Установка бина с кодом специальности по значение special1 из второго ComboBox в jsp
                 if(abit_SD.getSpecial1()!=null)
                   abit_SD.setKodSpetsialnosti(new Integer(abit_SD.getSpecial1().substring(0,abit_SD.getSpecial1().indexOf("%"))));

/*************************** Выборка числа абитуриентов из БД ****************************/

                 stmt = conn.prepareStatement("SELECT Sokr,NazvanijaPredmetov.KodPredmeta FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ? ORDER BY EkzamenyNaSpetsialnosti.KodPredmeta ASC");
                 stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setPredmet(rs.getString(1));
                   abit_SD_S4.add(abit_TMP);
                   kol_predm++;
                 }
                 abit_SD.setPredmCount(""+kol_predm);

// Данные по абитуриенту для вывода на JSP

                 int old_Ka = -1;

                 AbiturientBean abit_TMP = new AbiturientBean();

                 if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
                   stmt = conn.prepareStatement("SELECT a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,SUM(zso.OtsenkaEge) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k,EkzamenyNaSpetsialnosti ens WHERE k.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=k.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND k.Bud LIKE 'д' AND ens.KodSpetsialnosti LIKE ? AND zso.KodPredmeta=ens.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY 7 DESC");
                 if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
                   stmt = conn.prepareStatement("SELECT a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,SUM(zso.OtsenkaEge) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k,EkzamenyNaSpetsialnosti ens WHERE k.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=k.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND k.Dog LIKE 'д' AND ens.KodSpetsialnosti LIKE ? AND zso.KodPredmeta=ens.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY 7 DESC");

                 stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {

                     notes = new ArrayList();

                     abit_TMP = new AbiturientBean();
                     abit_TMP.setNumber(Integer.toString(++number));
                     abit_TMP.setKodAbiturienta(new Integer(rs.getString(1)));
                     abit_TMP.setNomerLichnogoDela(rs.getString(2));
                     abit_TMP.setFamilija(rs.getString(3));
                     abit_TMP.setImja(rs.getString(4));
                     abit_TMP.setOtchestvo(rs.getString(5));
                     abit_TMP.setTipDokSredObraz(rs.getString(6));

                     stmt = conn.prepareStatement("SELECT zso.OtsenkaEge,ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens,Konkurs k,ZajavlennyeShkolnyeOtsenki zso WHERE zso.KodAbiturienta=k.KodAbiturienta AND k.KodSpetsialnosti=ens.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti LIKE ? AND k.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta");
                     stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
                     stmt.setObject(2,rs.getString(1),Types.VARCHAR);
                     rs2 = stmt.executeQuery();
                     while (rs2.next()) {
                       note = StringUtil.voidFilter(rs2.getString(1));
                       notes.add(note);
                     }

                     abit_TMP.setNotes(notes);
                     abits_SD.add(abit_TMP);
                 }

// Количество абитуриентов на специальности

                 abit_SD.setSpecial22(new Integer(number));
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
          if ( rs2 != null ) {
               try {
                     rs2.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( rs3 != null ) {
               try {
                     rs3.close();
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
        request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S4", abit_SD_S4);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_spec_f) return mapping.findForward("lists_spec_f");
        return mapping.findForward("success");
    }
}