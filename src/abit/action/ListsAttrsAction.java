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

public class ListsAttrsAction extends Action {

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
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsAttrsForm       form               = (ListsAttrsForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_attrs_f      = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        int                  solve              = 0;
        String               str1               = new String();
        String               str2               = new String();
        String               str3               = new String();
        String               file_con           = new String();
        String               name               = new String();
        String               str4               = new String();
        UserBean             user               = (UserBean)session.getAttribute("user");
        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsAttrsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsAttrsForm", form );

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

/**************************** Генерация отчета ************************************/
            } else if ( form.getAction().equals("report")) {

/*****  Если action равен otchet , то входим в секцию - создание отчёта  **********/
/************************************ OTCHET **************************************/
	abit_SD.setKodSpetsialnosti(new Integer(abit_SD.getSpecial1().substring(0,abit_SD.getSpecial1().indexOf("%"))));
	abit_SD.setBud_Kon(abit_SD.getPriznakSortirovki());

	String abbr_sp = new String();	// Аббревиатура специальности
	String tip = new String();      // тип отчета
	int tipotch = 0;                // тип создаваемого отчета
	int kol = 0;                    // количество абитуриентов в таблице

/********************** задаём имя файла соответствующее признаку и тип отчета ********/
         stmt = conn.prepareStatement("SELECT Abbreviatura FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
         stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) abbr_sp = rs.getString(1);

	if(abit_SD.getBud_Kon().equals("medalisti"))
	{
       name = "Список абит. спец. "+abbr_sp.toUpperCase()+", имеющих отличия";
	 abit_SD.setPriznakSortirovki("medalisti");
	 file_con = "lists_med_"+StringUtil.toEng(abbr_sp);
	 tip = "имеющих отличия";
	 tipotch = 1;

	} else if(abit_SD.getBud_Kon().equals("kontraktniki")) {

       name = "Список контрактников спец. "+abbr_sp.toUpperCase();
	 abit_SD.setPriznakSortirovki("kontraktniki");
	 file_con = "lists_kon_"+StringUtil.toEng(abbr_sp);
	 tip = "имеющих платный договор";
	 tipotch = 2;

	} else if(abit_SD.getBud_Kon().equals("dogovorniki"))	{

       name = "Список договорников спец. "+abbr_sp.toUpperCase();
	 abit_SD.setPriznakSortirovki("dogovorniki");
	 file_con = "lists_dog_"+StringUtil.toEng(abbr_sp);
	 tip = "имеющих договор с предприятием";
 	 tipotch = 3;

	} else if(abit_SD.getBud_Kon().equals("ligotniki")) {

         name = "Список льготников спец. "+abbr_sp.toUpperCase();
	 abit_SD.setPriznakSortirovki("ligotniki");
	 file_con = "lists_lgotn_"+StringUtil.toEng(abbr_sp);
	 tip = "имеющих льготы";
	 tipotch = 4;

	} else if(abit_SD.getBud_Kon().equals("tspriem")) {

       name = "Список целевиков спец. "+abbr_sp.toUpperCase();
	 abit_SD.setPriznakSortirovki("tspriem");
	 file_con = "lists_tspriem_"+StringUtil.toEng(abbr_sp);
	 tip = "имеющих целевой прием";
	 tipotch = 5;
	}

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

report.write("{\\rtf1\\ansi\n");
report.write("\\fs24 \\qc Список абитуриентов, \n");
report.write("\\par"+tip+", поступающих на специальность\n");
stmt = conn.prepareStatement("SELECT NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
            stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
	     report.write("\\par\\b"+rs.getString(1)+"\\b0\n");
	    }
report.write("\\par\\par\n");

if(tipotch == 1){

/***************************************************/
/******************** C отличиями ******************/
/***************************************************/

//Шапка таблицы

report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx1800");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx3600");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx5100");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx7000");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx8500");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx10500");
report.write("\\intbl\\b № \\cell");
report.write("\\intbl Номер дела \\cell");
report.write("\\intbl Фамилия \\cell");
report.write("\\intbl Имя \\cell");
report.write("\\intbl Отчество \\cell");
report.write("\\intbl Отличия \\cell");
report.write("\\intbl Рекомендации \\b0\\cell");
report.write("\\intbl \\row");

report.write("\\fs24 \\b0 \\ql \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3600");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500");

stmt = conn.prepareStatement("SELECT a.Familija, a.Imja, a.Otchestvo, a.NomerLichnogoDela, m.ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodSpetsialnosti LIKE ? AND m.ShifrMedali NOT LIKE 'н' AND a.DokumentyHranjatsja LIKE 'д' ORDER BY a.Familija,a.Imja,a.Otchestvo ASC");
stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
rs = stmt.executeQuery();
while(rs.next()) 
{
  kol++;
  report.write("\\qc \\intbl "+kol+" \\cell");
  report.write("\\qc \\intbl "+rs.getString(4)+" \\cell");
  report.write("\\intbl\\ql "+rs.getString(1)+" \\cell");
  report.write("\\intbl "+rs.getString(2)+" \\cell");
  report.write("\\intbl "+rs.getString(3)+" \\cell");
  report.write("\\intbl\\qc "+rs.getString(5)+" \\cell");
  report.write("\\intbl \\cell");
  report.write("\\intbl \\row");
}

report.write("\\pard\\par\\par");
report.write("\\fs26 \\b\\ql Шифр отличия         Отличие");
stmt = conn.prepareStatement("SELECT ShifrMedali, Medal FROM Medali");
rs = stmt.executeQuery();
while(rs.next()) 
{
  report.write("\\par");
  report.write("\\fs24 \\b0 \\ql 	"+rs.getString(1)+"		"+rs.getString(2));
}

}else if(tipotch == 2){

/***************************************************/
/****************** Контрактники *******************/
/***************************************************/

//Шапка таблицы
report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx10500");
report.write("\\intbl \\b № \\cell");
report.write("\\intbl Номер дела \\cell");
report.write("\\intbl Фамилия \\cell");
report.write("\\intbl Имя \\cell");
report.write("\\intbl Отчество \\cell");
report.write("\\intbl Номер договора \\b0 \\cell");
report.write("\\intbl \\row");

report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdr10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500");

stmt = conn.prepareStatement("SELECT a.Familija, a.Imja, a.Otchestvo, a.NomerLichnogoDela, a.NomerPlatnogoDogovora FROM Abiturient a WHERE a.NomerPlatnogoDogovora IS NOT NULL AND a.KodSpetsialnosti LIKE ? AND a.DokumentyHranjatsja LIKE 'д' ORDER BY a.Familija,a.Imja,a.Otchestvo ASC");
stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
rs = stmt.executeQuery();
while(rs.next()) 
{
  kol++;
  report.write("\\intbl \\qc "+kol+"\\cell");
  report.write("\\intbl \\qc "+rs.getString(4)+" \\cell");
  report.write("\\intbl\\ql "+rs.getString(1)+" \\cell");
  report.write("\\intbl "+rs.getString(2)+" \\cell");
  report.write("\\intbl "+rs.getString(3)+" \\cell");
  report.write("\\intbl "+rs.getString(5)+" \\cell");
  report.write("\\intbl \\row");
}

}else if(tipotch == 3){

/***************************************************/
/************* Договор с предприятием **************/
/***************************************************/

//Шапка таблицы

report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx10500");
report.write("\\intbl \\b № \\cell");
report.write("\\intbl Номер дела \\cell");
report.write("\\intbl Фамилия \\cell");
report.write("\\intbl Имя \\cell");
report.write("\\intbl Отчество \\cell");
report.write("\\intbl Договор \\b0 \\cell");
report.write("\\intbl \\row");
report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500");

stmt = conn.prepareStatement("SELECT a.Familija, a.Imja, a.Otchestvo, a.NomerLichnogoDela, a.NapravlenieOtPredprijatija FROM Abiturient a WHERE a.NapravlenieOtPredprijatija LIKE 'д' and a.KodSpetsialnosti LIKE ? AND a.DokumentyHranjatsja LIKE 'д' ORDER BY a.Familija,a.Imja,a.Otchestvo ASC");
stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
rs = stmt.executeQuery();
while(rs.next()) 
{
  kol++;
  report.write("\\intbl\\qc "+kol+"\\cell");
  report.write("\\intbl\\qc "+rs.getString(4)+" \\cell");
  report.write("\\intbl\\ql "+rs.getString(1)+" \\cell");
  report.write("\\intbl "+rs.getString(2)+" \\cell");
  report.write("\\intbl "+rs.getString(3)+" \\cell");
  report.write("\\intbl\\qc "+rs.getString(5)+" \\cell");
  report.write("\\intbl\\row");
}

}else if(tipotch == 4){

/********************************/
/********   Льготники   *********/
/********************************/

report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx9000");
report.write("\\intbl \\b № \\cell");
report.write("\\intbl Номер дела \\cell");
report.write("\\intbl Фамилия \\cell");
report.write("\\intbl Имя \\cell");
report.write("\\intbl Отчество \\cell");
report.write("\\intbl Льгота \\b0 \\cell");
report.write("\\intbl \\row");

report.write("\\fs24\\b0\\ql \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");

stmt = conn.prepareStatement("SELECT a.Familija, a.Imja, a.Otchestvo, a.NomerLichnogoDela, l.ShifrLgot FROM Abiturient a,Lgoty l WHERE l.KodLgot=a.KodLgot AND a.KodSpetsialnosti LIKE ? AND l.ShifrLgot NOT LIKE 'н' AND a.DokumentyHranjatsja LIKE 'д' ORDER BY a.Familija,a.Imja,a.Otchestvo ASC");
stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
rs = stmt.executeQuery();
while(rs.next()) 
{
  kol++;
  report.write("\\qc \\intbl "+kol+" \\cell");
  report.write("\\qc \\intbl "+rs.getString(4)+" \\cell");
  report.write("\\intbl\\ql "+rs.getString(1)+" \\cell");
  report.write("\\intbl "+rs.getString(2)+" \\cell");
  report.write("\\intbl "+rs.getString(3)+" \\cell");
  report.write("\\intbl\\qc "+rs.getString(5)+" \\cell");
  report.write("\\intbl \\row");
}
report.write("\\pard\\par\\par");
report.write("\\fs26 \\b\\ql Шифр льгот            Льгота");
stmt = conn.prepareStatement("SELECT ShifrLgot,Lgoty FROM Lgoty");
            rs = stmt.executeQuery();
            while(rs.next()) {
report.write("\\par");
report.write("\\fs24 \\b0 \\ql 	"+rs.getString(1)+"		"+rs.getString(2));
		}

}else if(tipotch == 5){

/***************************************************/
/*************      Целевой приём     **************/
/***************************************************/

report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx9000");
report.write("\\intbl \\b № \\cell");
report.write("\\intbl Номер дела \\cell");
report.write("\\intbl Фамилия \\cell");
report.write("\\intbl Имя \\cell");
report.write("\\intbl Отчество \\cell");
report.write("\\intbl Шифр приема \\b0 \\cell");
report.write("\\intbl \\row");

report.write("\\fs24\\b0\\ql \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");

stmt = conn.prepareStatement("SELECT a.Familija, a.Imja, a.Otchestvo, a.NomerLichnogoDela, tp.ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE tp.KodTselevogoPriema=a.KodTselevogoPriema AND a.KodSpetsialnosti LIKE ? AND tp.ShifrPriema NOT LIKE 'н' AND a.DokumentyHranjatsja LIKE 'д' ORDER BY a.Familija,a.Imja,a.Otchestvo ASC");
stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
rs = stmt.executeQuery();
while(rs.next()) 
{
  kol++;
  report.write("\\qc \\intbl "+kol+" \\cell");
  report.write("\\qc \\intbl "+rs.getString(4)+" \\cell");
  report.write("\\intbl\\ql "+rs.getString(1)+" \\cell");
  report.write("\\intbl "+rs.getString(2)+" \\cell");
  report.write("\\intbl "+rs.getString(3)+" \\cell");
  report.write("\\intbl\\qc "+rs.getString(5)+" \\cell");
  report.write("\\intbl \\row");
}

report.write("\\pard\\par\\par");
report.write("\\fs26 \\b\\ql Шифр приема            Целевой прием");
stmt = conn.prepareStatement("SELECT ShifrPriema,TselevojPriem FROM TselevojPriem");
rs = stmt.executeQuery();
while(rs.next()) 
{
  report.write("\\par");
  report.write("\\fs24 \\b0 \\ql 	"+rs.getString(1)+"		"+rs.getString(2));
}

}
	

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
        request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S4", abit_SD_S4);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_attrs_f) return mapping.findForward("lists_attrs_f");
        return mapping.findForward("success");
    }
}