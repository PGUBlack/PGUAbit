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

public class Forma_2Z_Action extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        Statement            stmt               = null;
        ResultSet            rs                 = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        Forma_2_Form         form               = (Forma_2_Form) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_formatwo_f  	= false;
        boolean              error              = false;
        ActionForward        f                  = null;
        int                  Summa[]            = new int[40];
        int                  ItogSumm[]         = new int[40];
        String               Col[][]            = new String[40][1000];
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "forma_2z_Action", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "forma_2z_Form", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  String name = "Форма №2z";

  String file_con = "forma2Z_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;}\n");
  report.write("{\\fs28 \\b1 \\qc Сведения о поступлении абитуриентов в ВУЗ по факультетам (F2z) на "+StringUtil.CurrDate(".")+" г.\\line\n");
  report.write("\\par\n");
  report.write("\\fs16 \\b0 \\qc \\trowd\\trhdr \\trqc\\trgaph58\\trrh160\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx650\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1300\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1950\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2600\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4550\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5100\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5650\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6650\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9250\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10850\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13250\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15650\n");

  String headline[] = new String[]{"Аббр Спец.","План приё- ма","Пода- но заявл.","Кон-курс","Контингент","Осо-бые права","Ориг. атт.","Пол","Образование","Инд. дост.","Занимались на курсах","Место проживания"};
  for(int i=0;i<headline.length;i++)
     report.write("\\intbl{"+headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs16 \\b0 \\trowd\\trhdr \\trqc\\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx650\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1300\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1950\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2600\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3250\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3900\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4550\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5100\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5650\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6150\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6650\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7300\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7950\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8600\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9250\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9650\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10050\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10450\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10850\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11250\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11650\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12050\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12450\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12850\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13250\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13700\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14350\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15000\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15650\n");

  String sub_headline[] = new String[]{"","","","","Бюдж.","Дог.","Уск.","","","М","Ж","Дневн школа","ВУЗ","ПТУ","Тех- никум","З","С","Т","П","И","П","К","Ш","Л","Н","Пенза","Гор.П обл.","Сел.П обл.","Др. обл."};
  for(int i=0;i<sub_headline.length;i++)
     report.write("\\intbl{"+sub_headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  int ind=0;     // Здесь хранится количество элементов массивов Abbr_Fak, Abbr_Spc, Kod_Spec
                 // т.е. эта переменная содержит количество строк таблицы
  int cur_ind=0; // С помощью этой переменной перебираются элементы массивов

  String  Abbr_Fak[] = new String[250];	// Аббревиатуры факультетов
  String  Abbr_Spc[] = new String[250];	// Аббревиатуры специальностей
  String  Kod_Spec[] = new String[250];	// Коды специальностей


/** ВНИМАНИЕ !!! ВНИМАНИЕ !!!**/
/** ВНИМАНИЕ !!! ВНИМАНИЕ !!!**/
/** ВНИМАНИЕ !!! ВНИМАНИЕ !!!**/

/************************************************/
/******  ВКЛЮЧЕНы только ЮК, ФОЗиДО и ФЗО  ******/
/************************************************/

      String include_faks = "'юк','фзо','фозидо','ЗФПО','иито','ИМС'";

	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT f.AbbreviaturaFakulteta, s.Abbreviatura, s.KodSpetsialnosti FROM Fakultety f, Spetsialnosti s WHERE s.KodFakulteta=f.KodFakulteta AND f.AbbreviaturaFakulteta IN ("+include_faks+") ORDER BY s.Abbreviatura");
	while(rs.next())
	{
	 Abbr_Fak[ind] = rs.getString(1);
	 Abbr_Spc[ind] = rs.getString(2);
	 Kod_Spec[ind] = rs.getString(3);
	 ind++;
	}

// Инициализация переменных

	float Konk     = 0; // переменные для
	float Konk1    = 0; // подсчета
	float Konk2    = 0; // конкурса

	int sel_ind = 0;    // Индексная переменная

// Здесь производится выборка данных из БД для каждого столбца отдельно. Используется
// группировка по коду специальности.

//PLAN_PR
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT PlanPriema,KodSpetsialnosti,Abbreviatura FROM Spetsialnosti ORDER BY Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[1][sel_ind] = rs.getString(2)+"%0";
	 else Col[1][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//DOK_HRAN
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[2][sel_ind] = rs.getString(2)+"%0";
	 else Col[2][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//BUDJETNIKI
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodFormyOb=fo.KodFormyOb AND kon.Bud LIKE 'д' AND fo.Sokr LIKE 'заочная' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[4][sel_ind] = rs.getString(2)+"%0";
	 else Col[4][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//DOG
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodFormyOb=fo.KodFormyOb AND kon.Dog LIKE 'д' AND fo.Sokr LIKE 'заочная' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[5][sel_ind] = rs.getString(2)+"%0";
	 else Col[5][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//USKORENNIKI
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodFormyOb=fo.KodFormyOb AND fo.Sokr LIKE 'заочно-уск' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[6][sel_ind] = rs.getString(2)+"%0";
	 else Col[6][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//LGOTY !('н')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Lgoty l,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodLgot=l.KodLgot AND l.ShifrLgot NOT LIKE 'н' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[7][sel_ind] = rs.getString(2)+"%0";
	 else Col[7][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//ATTESTAT
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.TipDokSredObraz LIKE 'о' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[8][sel_ind] = rs.getString(2)+"%0";
	 else Col[8][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//POL ('м')
      stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.Pol LIKE 'м' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[9][sel_ind] = rs.getString(2)+"%0";
	 else Col[9][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//POL ('ж')
      stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.Pol LIKE 'ж' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[10][sel_ind] = rs.getString(2)+"%0";
	 else Col[10][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('ш')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE 'ш' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[11][sel_ind] = rs.getString(2)+"%0";
	 else Col[11][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('в')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE 'в' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[12][sel_ind] = rs.getString(2)+"%0";
	 else Col[12][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('п')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE 'п' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[13][sel_ind] = rs.getString(2)+"%0";
	 else Col[13][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('т')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE 'т' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[14][sel_ind] = rs.getString(2)+"%0";
	 else Col[14][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('з','о')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali IN ('з','о') AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[15][sel_ind] = rs.getString(2)+"%0";
	 else Col[15][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('с')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali LIKE 'с' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[16][sel_ind] = rs.getString(2)+"%0";
	 else Col[16][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('т')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali LIKE 'т' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[17][sel_ind] = rs.getString(2)+"%0";
	 else Col[17][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('п')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali LIKE 'п' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[18][sel_ind] = rs.getString(2)+"%0";
	 else Col[18][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('и')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE 'и' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[19][sel_ind] = rs.getString(2)+"%0"; 
       else Col[19][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
	}
//SHIFR_KURSOV ('п')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE 'п' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[20][sel_ind] = rs.getString(2)+"%0";
	 else Col[20][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('к')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE 'к' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[21][sel_ind] = rs.getString(2)+"%0";
	 else Col[21][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('ш')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE 'ш' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[22][sel_ind] = rs.getString(2)+"%0";
	 else Col[22][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('л')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE 'л' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[23][sel_ind] = rs.getString(2)+"%0";
	 else Col[23][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('н')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE 'н' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[24][sel_ind] = rs.getString(2)+"%0";
	 else Col[24][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('п')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE 'п' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[25][sel_ind] = rs.getString(2)+"%0";
	 else Col[25][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('г')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE 'г' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[26][sel_ind] = rs.getString(2)+"%0";
	 else Col[26][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('с')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE 'с' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[27][sel_ind] = rs.getString(2)+"%0";
	 else Col[27][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('о')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE 'о' AND a.DokumentyHranjatsja LIKE 'д' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[28][sel_ind] = rs.getString(2)+"%0";
	 else Col[28][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
// Количество абитуриентов на бюджетных специальностях 
        int kSpec = -1;
        sel_ind = 0;
        for(int row=0;row<1000;row++) {
          if(Col[1][row] != null && StringUtil.toInt(Col[1][row].substring(Col[1][row].indexOf("%")+1),0) != 0) {
            kSpec = StringUtil.toInt(Col[1][row].substring(0,Col[1][row].indexOf("%")),-1);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE 'д' AND s.KodSpetsialnosti LIKE '"+kSpec+"' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
            while(rs.next())
            {
              if(rs.getString(1) == null) Col[29][sel_ind] = rs.getString(2)+"%0";
              else Col[29][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
              sel_ind++;
            }
          }
        }

boolean Find = false;   // Признак того что, для данной ячейки информация в массиве найдена.
                        // Выборка происходит по коду специальности.
int i = 0;

/***************************************/
/****  Формат первой строки таблицы  ***/
/***************************************/

  report.write("\\fs16 \\b0 \\trowd \\trqc \\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1950\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4550\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6150\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7950\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10050\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10450\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10850\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12050\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12450\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12850\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14350\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15650\n");

	while(cur_ind < ind) // Условие окончания заполнения таблицы
	{

//************************** Количество по специальностям ***************************

/*******************************/
/****  Вывод строк таблицы  ****/
/*******************************/

/******* 0 *******/
	report.write("\\b0\\intbl{"+Abbr_Spc[cur_ind]+"}\\cell\n");

/****** 1-28 ******/

for(int indx = 1; indx<=29; indx++) {
	i = 0;

	while(Col[indx][i] != null)
	{

// Если код специальности текущей строки таблицы ( Kod_Spec[cur_ind] )
// найден в массиве ( Col[XX][i].substring(0,3) ) то выводится соответствующая информация
// из этого массива в соответствующую ячейку, иначе записывается 0.

	 if(Col[indx][i].substring(0,Col[indx][i].indexOf("%")).equals(Kod_Spec[cur_ind]))
	 {
	  Find = true;
	  break;
	 }
	 i++;
	}
	if(Find == true)
	{

         if(indx == 29) {
           Summa[3] += Integer.parseInt(Col[29][i].substring(Col[29][i].indexOf("%")+1));
         }
         else
           Summa[indx] += Integer.parseInt(Col[indx][i].substring(Col[indx][i].indexOf("%")+1));

         if(indx == 1) Konk1 = Integer.parseInt(Col[1][i].substring(Col[1][i].indexOf("%")+1));
         if(indx == 2) Konk2 = Integer.parseInt(Col[2][i].substring(Col[2][i].indexOf("%")+1));

         if(indx != 29)
           report.write("\\intbl{"+Col[indx][i].substring(Col[indx][i].indexOf("%")+1)+"}\\cell\n");

	} else {
	 if(indx == 1) Konk1 = 0;
	 if(indx == 2) Konk2 = 0;
	 if(indx == 3) {
         if(Konk1 != 0) Konk = Math.round((Konk2/Konk1)*100f)/100f;
         else Konk = 0;
         if(indx != 29)
           report.write("\\intbl{"+Konk+"}\\cell\n");
       }
       else if(indx != 29) report.write("\\intbl{0}\\cell\n");
        }
	Find = false;
}

//*********************

report.write("\\intbl\\row\\b0\n");
cur_ind++;

//************************ Всего на факультете *************************
	 if(!Abbr_Fak[cur_ind-1].equals(Abbr_Fak[cur_ind])) { // Если текущая аббревиатура факультета не равна
                                                            // предыдущей то выводим строку содержащую
                                                            // суммарную информацию по всем специальностям
                                                            // факультета     
/********************************/
/****   ИТОГО ПО ФАКУЛЬТЕТУ   ***/
/********************************/

         report.write("\\b1\\intbl{"+Abbr_Fak[cur_ind-1]+"}\\cell\n");

         for(i=1;i<=28;i++) {
            if(i!=3) report.write("\\intbl{"+Summa[i]+"}\\cell\n");
            else {
              if(Summa[1]!=0)
                report.write("\\intbl{"+Math.round(((float)Summa[2]/(float)Summa[1])*100f)/100f+"}\\cell\n");
              else
                report.write("\\intbl{0}\\cell\n");
            }
         }

         report.write("\\intbl\\row\n");

         for(int in=0;in<40;in++) {
           ItogSumm[in] += Summa[in];
           Summa[in] = 0;
         }
       }
	}

/*******************************/
/****  ИТОГО ПО ВУЗУ В ЦЕЛОМ ***/
/*******************************/
/*
report.write("\\b1\\intbl{Итого}\\cell\n");

for(i=1;i<=28;i++) {
  if(i == 3) {
    if(ItogSumm[1]!=0){
      report.write("\\intbl{"+Math.round(((float)ItogSumm[2]/(float)ItogSumm[1])*100f)/100f+"}\\cell\n");
    }
    else{
      report.write("\\intbl{0}\\cell\n");
    }
  } else
      report.write("\\intbl{"+ItogSumm[i]+"}\\cell\n");
}
report.write("\\intbl\\row");
*/

/*********************************************************************/
/****************** Вывод списка факультетов *************************/
/*********************************************************************/

String kfaka[] = new String[250];
String fakul[] = new String[250];

///  report.write("\\sect\\cols2{\\colsx708\\endnhere\\colno1\\colw7525\\colsr708\\colno2\\colw7427\n");
report.write("}\\pard{\n");
  stmt = conn.createStatement();
  rs = stmt.executeQuery("SELECT KodFakulteta, Fakultet, AbbreviaturaFakulteta FROM Fakultety WHERE AbbreviaturaFakulteta IN("+include_faks+") GROUP BY Fakultet,KodFakulteta, AbbreviaturaFakulteta ORDER BY Fakultet");
  int stfak = 0;
  while(rs.next())
  {
    kfaka[stfak]=Integer.toString(rs.getInt(1));
    fakul[stfak]=rs.getString(2).toUpperCase();
    stfak++;
  }

  int stfak1 = stfak-1;
  stfak = 0;
  while(stfak<=stfak1)
  {
    if(stfak != 0) report.write("\\par");
    report.write("\\par\\fs16\\par\\highlight0\\fs22\\b\\ql{"+fakul[stfak]+"}\\highlight0\n");
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT Abbreviatura, NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE "+kfaka[stfak]);
    while(rs.next())
    {
     report.write("\\par");
     report.write("\\fs20\\b0\\ql{"+rs.getString(1)+"	"+rs.getString(2)+"}\n");
    }
    stfak++;
  }

report.write("}}"); 
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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(rep_formatwo_f) return mapping.findForward("rep_formatwo_f");
        return mapping.findForward("success");
    }
}