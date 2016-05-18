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

public class KonkursAction extends Action {

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
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        Forma_2_Form         form               = (Forma_2_Form) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_konkurs_f  	= false;
        boolean              error              = false;
        ActionForward        f                  = null;
        UserBean             user               = (UserBean)session.getAttribute("user");
        ArrayList            spec_list1         = new ArrayList();
        ArrayList            spec_list2         = new ArrayList();

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "konkursAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "infoAbitForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  int counter = 0;

  boolean proh_ball_counted = false;

  String name = "Конкурс по специальностям на "+StringUtil.CurrDate("-");

  if(!abit_SD.getSpecial1().equals("1")) name += " с факультетами";

  String file_con = "info_konkurs_"+StringUtil.CurrDate("-");

  if(abit_SD.getSpecial1().equals("1"))
    file_con += "_sp";
  else
    file_con += "_fk";

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("\\pard\n");

  stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs28\\b1\\qc Количество заявлений, поданных абитуриентами и конкурс по специальностям\\par в {"+rs.getString(1)+"} на {"+StringUtil.CurrDate(".")+"г.}\\par\n");

  report.write("\\pard\\par\n");

  if(abit_SD.getSpecial1().equals("1"))
  {
    report.write("\\fs26\\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx8000\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15700\n");

    report.write("\\intbl\\fs24\\qc\\b1{Наименование специальности\\par (направления)}\\cell\n");
    report.write("\\intbl\\fs24\\qc{Количество бюджетных мест\\par(план приема)}\\cell\n");
    report.write("\\intbl\\fs22\\qc{Подано заявлений на очную форму обучения}\\cell\n");
    report.write("\\intbl\\fs24\\qc{Текущий конкурс}\\cell\n");
    report.write("\\intbl\\b0\\row\n");

    report.write("\\fs24 \\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx8000\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx11000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15700\n");

    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\b1\\fs22\\qc{Всего}\\cell\n");
    report.write("\\intbl\\fs22\\qc{В т.ч.}\\par{оригиналы}\\par{документов}\\b0\\cell\n");
    report.write("\\intbl\\b1\\fs22\\qc{по}\\par{заявлениям}\\cell\n");
    report.write("\\intbl\\fs22\\qc{по}\\par{оригиналам*}\\b0\\cell\n");
    report.write("\\intbl\\b0\\row\n");

    report.write("\\fs24 \\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1400\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx8000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx11000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15700\n");

  } else {

// С факультетом

    report.write("\\fs26\\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1200\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx8000\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15700\n");

    report.write("\\intbl\\fs24\\qc\\b1{Факуль-тет}\\cell\n");
    report.write("\\intbl\\fs24\\qc\\b1{Наименование специальности\\par (направления)}\\cell\n");
    report.write("\\intbl\\fs24\\qc{Количество бюджетных мест\\par(план приема)}\\cell\n");
    report.write("\\intbl\\fs22\\qc{Подано заявлений на очную форму обучения}\\cell\n");
    report.write("\\intbl\\fs24\\qc{Текущий конкурс}\\cell\n");
    report.write("\\intbl\\b0\\row\n");

    report.write("\\fs24 \\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx1200\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx8000\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx11000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15700\n");

    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\b1\\fs22\\qc{Всего}\\cell\n");
    report.write("\\intbl\\fs22\\qc{В т.ч.}\\par{оригиналы}\\par{документов}\\b0\\cell\n");
    report.write("\\intbl\\b1\\fs22\\qc{по}\\par{заявлениям}\\cell\n");
    report.write("\\intbl\\fs22\\qc{по}\\par{оригиналам*}\\b0\\cell\n");
    report.write("\\intbl\\b0\\row\n");

    report.write("\\fs24 \\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1200\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2400\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx8000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx11000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15700\n");
  }

// Формирование исходного списка spec_list1

//  stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,COUNT(ab.KodAbiturienta) FROM Konkurs kon,Spetsialnosti sp, Abiturient ab,Fakultety fk WHERE kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND ab.KodVuza LIKE ? GROUP BY sp.ShifrSpetsialnosti,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.Abbreviatura,sp.KodSpetsialnosti UNION SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,0 FROM Spetsialnosti sp,Fakultety fk WHERE fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND sp.KodSpetsialnosti NOT IN (SELECT DISTINCT KodSpetsialnosti FROM Konkurs) ORDER BY sp.ShifrSpetsialnosti");
  if(abit_SD.getSpecial1().equals("1"))
    stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,COUNT(ab.KodAbiturienta) FROM Konkurs kon,Spetsialnosti sp, Abiturient ab,Fakultety fk WHERE kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо','иито') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND sp.PlanPriema NOT LIKE '0' AND ab.KodVuza LIKE ? GROUP BY sp.ShifrSpetsialnosti,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.Abbreviatura,sp.KodSpetsialnosti UNION SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,0 FROM Spetsialnosti sp,Fakultety fk WHERE fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо','иито') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.PlanPriema NOT LIKE '0' AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND sp.KodSpetsialnosti NOT IN (SELECT DISTINCT KodSpetsialnosti FROM Konkurs) ORDER BY sp.ShifrSpetsialnosti");
  else
    stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,COUNT(ab.KodAbiturienta),fk.AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Abiturient ab,Fakultety fk WHERE kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо','иито') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND sp.PlanPriema NOT LIKE '0' AND ab.KodVuza LIKE ? GROUP BY sp.ShifrSpetsialnosti,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.Abbreviatura,sp.KodSpetsialnosti,fk.AbbreviaturaFakulteta UNION SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,0,fk.AbbreviaturaFakulteta FROM Spetsialnosti sp,Fakultety fk WHERE fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо','иито') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.PlanPriema NOT LIKE '0' AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND sp.KodSpetsialnosti NOT IN (SELECT DISTINCT KodSpetsialnosti FROM Konkurs) ORDER BY fk.AbbreviaturaFakulteta");

  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);

  rs = stmt.executeQuery();

  while(rs.next()){

    ListBean lb = new ListBean();

    if(abit_SD.getSpecial1().equals("1"))
      lb.setProperty6("-");
    else
      lb.setProperty6(rs.getString(7));

    lb.setProperty1(rs.getString(1)+" ("+rs.getString(2)+")");

    if(StringUtil.toInt(rs.getString(4),0) != 0) {
      lb.setProperty3(rs.getString(4));
      lb.setProperty2(rs.getString(3));
    }
    else {
      lb.setProperty3("-");
      lb.setProperty2(rs.getString(3)+" (дог.)");
    }
    lb.setProperty4(rs.getString(6));

// Шифр спец-ти для сортировки

    lb.setProperty7(rs.getString(1));

    counter = 0;

    proh_ball_counted = false;

//    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(zso.OtsenkaEge)\"SummaEge\",a.TipDokSredObraz FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Konkurs kon, Spetsialnosti s, Medali m, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=kon.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND kon.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti LIKE ? AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,a.TipDokSredObraz ORDER BY SummaEge DESC");

    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(zso.OtsenkaEge)\"SummaEge\",a.TipDokSredObraz FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Konkurs kon, Spetsialnosti s, Medali m, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens WHERE k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=kon.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND kon.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti LIKE ? AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.TipDokSredObraz LIKE 'о' GROUP BY a.KodAbiturienta,a.TipDokSredObraz ORDER BY SummaEge DESC");
    stmt.setObject(1,rs.getString(5),Types.INTEGER);
    rs2 = stmt.executeQuery();
    while(rs2.next()){

      if(rs2.getInt(2) > 0) {

        if(++counter >= rs.getInt(4)) {

          lb.setId(rs2.getInt(2));

          proh_ball_counted = true;

          break;
        }
      }
    }

    if(!proh_ball_counted) {

      stmt = conn.prepareStatement("SELECT ens.KodSpetsialnosti,SUM(MinBallEge) FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens WHERE np.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE ? GROUP BY ens.KodSpetsialnosti");
      stmt.setObject(1,rs.getString(5),Types.INTEGER);
      rs2 = stmt.executeQuery();
      if(rs2.next())
        lb.setId(rs2.getInt(2));
      else
        lb.setId(0);
    }

// Количество оригиналов документов об образовании

    stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Konkurs kon, Spetsialnosti s WHERE a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE 'д' AND a.TipDokSredObraz LIKE 'о' AND kon.KodSpetsialnosti LIKE ?");
    stmt.setObject(1,rs.getString(5),Types.INTEGER);
    rs2 = stmt.executeQuery();
    if(rs2.next())
      lb.setProperty5(""+rs2.getString(1));
    else
      lb.setProperty5("-");

    spec_list1.add(lb);
  }



// Сортировка списка spec_list1 (построение списка spec_list2 в порядке убывания SummaEge)

  int maxValue, maxValueIndex;

  while(spec_list1.size() != 0) {

    maxValue = StringUtil.toInt(((ListBean)spec_list1.get(0)).getProperty7(),0);

    maxValueIndex = 0;

    for(int cur_ind=0;cur_ind<spec_list1.size();cur_ind++) {

      if(StringUtil.toInt(((ListBean)spec_list1.get(cur_ind)).getProperty7(),0) <= maxValue) {

        maxValue = StringUtil.toInt(((ListBean)spec_list1.get(cur_ind)).getProperty7(),0);

        maxValueIndex = cur_ind;
      }
    }

    spec_list2.add(spec_list1.get(maxValueIndex));

    spec_list1.remove(maxValueIndex);
  }

  float a = 0, b = 0, c = 0;

// Вывод отсортированного списка

  for(int ind=0;ind<spec_list2.size();ind++) {

    a = StringUtil.toFloat(""+((ListBean)spec_list2.get(ind)).getProperty4(),0);

    b = StringUtil.toFloat(""+((ListBean)spec_list2.get(ind)).getProperty5(),0);

    c = StringUtil.toFloat(""+((ListBean)spec_list2.get(ind)).getProperty3(),0);

    if(!abit_SD.getSpecial1().equals("1"))

      report.write("\\intbl\\fs20\\qc{"+((ListBean)spec_list2.get(ind)).getProperty6()+"}\\cell\n");

    report.write("\\intbl\\fs20\\qc{"+((ListBean)spec_list2.get(ind)).getProperty1()+"}\\cell\n");

    report.write("\\intbl\\fs20\\ql{"+((ListBean)spec_list2.get(ind)).getProperty2()+"}\\cell\n");

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty3()+"}\\cell\n");

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty4()+"}\\cell\n");

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty5()+"}\\cell\n");

    if(c != 0)
      report.write("\\intbl\\fs24\\qc{"+(Math.round((a/c)*100f)/100f)+"}\\cell\n");
    else
      report.write("\\intbl\\fs24\\qc{-}\\cell\n");

    if(c != 0)
      report.write("\\intbl\\fs24\\qc{"+(Math.round((b/c)*100f)/100f)+"}\\cell\n");
    else
      report.write("\\intbl\\fs24\\qc{-}\\cell\n");

    report.write("\\intbl\\b0\\row\n");
  }

  report.write("\\pard\n");
  report.write("\\par\\fs24\\qc{* - приказ о зачислении издается по результатам конкурса среди лиц, подавших }\\ul1\\b1{оригиналы}\\b0\\ulnone{ документов об образовании}\\par\n");
  report.write("}");
  report.close();

  spec_list2.clear();

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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(rep_konkurs_f) return mapping.findForward("rep_konkurs_f");
        return mapping.findForward("success");
    }
}