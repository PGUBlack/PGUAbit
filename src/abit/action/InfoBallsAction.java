package abit.action;

import java.io.*;
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
import abit.action.InfoBallsForm;
import java.lang.Object.*;
import abit.sql.*; 

public class InfoBallsAction extends Action {

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
        ResultSet            rs2                 = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        InfoBallsForm        form               = (InfoBallsForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_info_balls_f   = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        ArrayList            notes              = new ArrayList();
        String               sort               = new String();
	String               SS                 = new String();
	String               NS                 = new String();
	String               AA                 = new String();
	String               PP                 = new String();
        int                  summa              = 0;
        UserBean             user               = (UserBean)session.getAttribute("user");
        int                  count_predm        = 0;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "infoBallsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "infoBallsForm", form );

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

            } else if ( form.getAction().equals("report")) {

/**********************************************************************************/
/*****    Если action равен otchet , то входим в секцию - создание отчёта    ******/
/************************************ OTCHET **************************************/

        int number = 0;  //Нумерация строк в отчёте

/**********************************************************************************/
        stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
        stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          SS = rs.getString(1);
          NS = rs.getString(2).toUpperCase();
          AA = rs.getString(3);
          PP = rs.getString(4);
        }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

        String name = "Сведения по баллам ЕГЭ для спец. "+AA.toUpperCase();

        String file_con = new String("info_balls_"+StringUtil.toEng(AA));

        session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

        String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

        BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

        report.write("{\\rtf1\\ansi\n");

        stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) 
          report.write("\\fs40 \\qc "+rs.getString(1)+"\n");
        report.write("\\par\\par\n");

        stmt = conn.prepareStatement("SELECT COUNT(Sokr) FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ?");
        stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
        rs = stmt.executeQuery();
        while (rs.next()) {
          count_predm = rs.getInt(1);
        }

// Данные по абитуриенту для вывода в отчет

  report.write("\\fs30 \\b0 \\qc Сведения по баллам ЕГЭ, специальность \\b "+SS+"\\line\\qc \\b\\fs22"+NS+"\n");
  report.write("\\par\n");
  report.write("\\fs24 \\b0 \\qc План приёма:\\b {"+PP+"}\\line\n");
  report.write("\\par\n");
  report.write("\\par\n");

// Шапка - Первая строка таблички (формат)

  report.write("\\fs20 \\trowd \\trhdr \\trqc\\trgaph60\\trrh80\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx520\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1098\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3509\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4642\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6343\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7194\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+count_predm*700)+"\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+700*count_predm+860)+"\n");

// Шапка - Первая строка таблички (данные)

  report.write("\\intbl № \\cell\n");
  report.write("\\intbl Абб. спец \\cell\n");
  report.write("\\intbl Номер личн. дела \\cell\n");
  report.write("\\intbl Фамилия \\cell\n");
  report.write("\\intbl Имя \\cell\n");
  report.write("\\intbl Отчество \\cell\n");
  report.write("\\intbl{Аттес-}\\par{тат}\\cell\n");
  report.write("\\intbl Предметы/Оценки \\cell\n");
  report.write("\\intbl Сумма баллов ЕГЭ\\cell\n");
  report.write("\\intbl \\row\n");

// Шапка - Вторая строка таблички (формат)

  report.write("\\b0 \\trowd \\trqc\\trgaph60\\trrh80\\trleft36\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx520\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1098\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3508\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4642\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6343\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7194\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");

  for(int col=1;col<=count_predm;col++)
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+col*700)+"\n");

  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+700*count_predm+860)+"\n");

// Шапка - Вторая строка таблички (данные)

  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");

  stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ? ORDER BY EkzamenyNaSpetsialnosti.KodPredmeta ASC");
  stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
    report.write("\\intbl{"+rs.getString(1)+"}\\cell\n");
  }

  report.write("\\intbl\\cell\n");
  report.write("\\intbl \\row\n");

// Тело - Строки таблички (формат)

  report.write("\\trowd \\trqc\\trgaph60\\trrh80\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx520\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1098\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3508\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4642\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6343\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7194\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");

  for(int col=1;col<=count_predm;col++)

   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+col*700)+"\n");

  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7900+700*count_predm+860)+"\n");


// Тело - Строки таблички (данные по абитуриентам)

  if(session.getAttribute("IB_Ord") != null && session.getAttribute("IB_Ord").equals("1")) 

       sort = "a.Familija,a.Imja,a.Otchestvo,a.KodAbiturienta ASC";
  else 
       sort = "SummaEge DESC,a.Familija,a.Imja,a.Otchestvo";

  stmt = conn.prepareStatement("SELECT a.KodAbiturienta,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\" FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti LIKE ? AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY "+sort);
  stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {

      report.write("\\intbl{"+(++number)+"}\\cell\n");
      report.write("\\intbl{"+rs.getString(2)+"}\\cell\n");
      report.write("\\intbl{"+rs.getString(3)+"}\\cell\n");
      report.write("\\intbl\\ql{"+rs.getString(4)+"}\\cell\n");
      report.write("\\intbl{" +rs.getString(5)+"}\\cell\n");
      report.write("\\intbl{" +rs.getString(6)+"}\\cell\n");
      report.write("\\intbl{" +rs.getString(7)+"}\\cell\n");

      stmt = conn.prepareStatement("SELECT zso.OtsenkaEge,ens.KodPredmeta FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti=ens.KodSpetsialnosti AND ens.KodSpetsialnosti LIKE ? AND a.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta ASC");
      stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
      stmt.setObject(2,rs.getString(1),Types.INTEGER);
      rs2 = stmt.executeQuery();
      while (rs2.next()) {

        report.write("\\intbl{"+rs2.getString(1)+"}\\cell\n");
      }

      report.write("\\intbl{"+rs.getString(8)+"}\\cell\n");
      report.write("\\intbl\\row\n");
  }

  report.write("}"); 

  report.close();

  session.removeAttribute("IB_kSp");

  session.removeAttribute("IB_Ord");

  form.setAction(us.getClientIntName("new_rep","crt"));
  return mapping.findForward("rep_brw");


            } else if ( form.getAction().equals("viewing")) {

/************************************************************************************************/
/*****  Если action равен viewing , то входим в секцию - создание записи или вывод таблицы  *****/

                 int kol_predm = 0;
                 int number = 0;

                 form.setAction(us.getClientIntName("full","view"));

// Установка бина с кодом специальности по значение special1 из второго ComboBox в jsp.

                 if(abit_SD.getKodSpetsialnosti() == null && abit_SD.getSpecial1() != null) session.setAttribute("IB_kSp", abit_SD.getSpecial1().substring(0,abit_SD.getSpecial1().indexOf("%")));

                 if(request.getParameter("ord") != null) session.setAttribute("IB_Ord",request.getParameter("ord"));

                 stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
                 stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 if(rs.next()) {
                   abit_SD.setSpecial2(rs.getString(2));
                   abit_SD.setSpecial3(rs.getString(4));
                 }

                 stmt = conn.prepareStatement("SELECT np.Sokr FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens WHERE np.KodPredmeta = ens.KodPredmeta AND ens.KodSpetsialnosti LIKE ? ORDER BY ens.KodPredmeta ASC");
                 stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setPredmet(rs.getString(1));
                   abit_SD_S4.add(abit_TMP);
                   kol_predm++;
                 }
                 abit_SD.setPredmCount(""+kol_predm);

// Данные по абитуриенту для вывода на JSP

                 AbiturientBean abit_TMP = new AbiturientBean();

                 if(session.getAttribute("IB_Ord") != null && session.getAttribute("IB_Ord").equals("1")) 

                      sort = "a.Familija,a.Imja,a.Otchestvo,a.KodAbiturienta ASC";
                 else 
                      sort = "SummaEge DESC,a.Familija,a.Imja,a.Otchestvo";

                 stmt = conn.prepareStatement("SELECT a.KodAbiturienta,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\" FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti LIKE ? AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,s.Abbreviatura,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY "+sort);
                 stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {

                     abit_TMP = new AbiturientBean();
                     abit_TMP.setNumber(Integer.toString(++number));
                     abit_TMP.setKodAbiturienta(new Integer(rs.getString(1)));
                     abit_TMP.setAbbreviatura(rs.getString(2));
                     abit_TMP.setNomerLichnogoDela(rs.getString(3));
                     abit_TMP.setFamilija(rs.getString(4));
                     abit_TMP.setImja(rs.getString(5));
                     abit_TMP.setOtchestvo(rs.getString(6));
                     abit_TMP.setTipDokSredObraz(rs.getString(7));
                     abit_TMP.setSpecial8(rs.getString(8));

                     notes = new ArrayList();

                     stmt = conn.prepareStatement("SELECT zso.OtsenkaEge,ens.KodPredmeta FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti=ens.KodSpetsialnosti AND ens.KodSpetsialnosti LIKE ? AND a.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta ASC");
                     stmt.setObject(1,session.getAttribute("IB_kSp"),Types.INTEGER);
                     stmt.setObject(2,rs.getString(1),Types.INTEGER);
                     rs2 = stmt.executeQuery();
                     while (rs2.next()) {

                       notes.add(rs2.getString(1));
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
        if(rep_info_balls_f) return mapping.findForward("rep_info_balls_f");
        return mapping.findForward("success");
    }
}