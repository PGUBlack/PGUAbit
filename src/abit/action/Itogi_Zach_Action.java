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

public class Itogi_Zach_Action extends Action {

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
        Forma_2_Form         form               = (Forma_2_Form) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_itogi_zach_f  	= false;
        boolean              error              = false;
        ActionForward        f                  = null;
        String               exclude_faks       = "";
        String               forma_Ob           = "";
        Date                 date               = new Date();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "itogi_Zach_Action", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "itogi_Zach_Form", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

/** ВНИМАНИЕ !!! ВНИМАНИЕ !!!**/
/** ВНИМАНИЕ !!! ВНИМАНИЕ !!!**/
/** ВНИМАНИЕ !!! ВНИМАНИЕ !!!**/

/**********************************/
/**** ИСКЛЮЧЕНЫ ИИТО, ДО и ФЗО, а также ЮК ****/
/**********************************/

  if(abit_SD.getSpecial1().equals("1")){

// Все ОЧНЫЕ факультеты

    exclude_faks = "'иито','до','фзо','юк'";
    forma_Ob = "Очная";

  } else if(abit_SD.getSpecial1().equals("2")){

// факультет ФЗО

    exclude_faks = "'енф','иивт','исуиб','ит','иэиу','ми','увц','фам','фпит','фрэ','юк','юф','иито','до'";
    forma_Ob = "Заочная";

  } else if(abit_SD.getSpecial1().equals("3")){

// факультет ИИТО

    exclude_faks = "'енф','иивт','исуиб','ит','иэиу','ми','увц','фам','фпит','фрэ','юк','юф','до','фзо'";
    forma_Ob = "иито";

  } else if(abit_SD.getSpecial1().equals("4")){

// факультет ДО

    exclude_faks = "'енф','иивт','исуиб','ит','иэиу','ми','увц','фам','фпит','фрэ','юк','юф','иито','фзо'";
    forma_Ob = "Дистанционная";
  }

  String name = "Итоги зачисления на 1й курс, форма об. "+forma_Ob+" от "+StringUtil.CurrDate("-");

  String file_con = "itogi_Zach_"+StringUtil.toEng(forma_Ob)+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

 report.write("{\\rtf1\\ansi\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\fet0\\sectd \\lndscpsxn\\psz9\\linex0");
 report.write("{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;}");
 report.write("\\par\\par\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\fs14\\b1\\ql{Приложение к приказу Рособразования}");
 report.write("\\par\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\fs14\\b1\\ql{от 17.04.2007     №687}");
 report.write("\\par{\\fs22\\b1\\qc{ИНФОРМАЦИЯ ОБ ИТОГАХ ЗАЧИСЛЕНИЯ В "+new String(date.getYear()+1902+"")+" ГОДУ НА ПЕРВЫЙ КУРС}");

 stmt = conn.prepareStatement("SELECT NazvanieVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\par\\qc{в ГОУ ВПО \\'ab{"+rs.getString(1).toUpperCase()+"}\\'bb,}");
 }
 report.write("\\par\\qc{расположенный на территории Пензенской области}");
 report.write("\\par\\fs18\\ql\\b1{Высшее профессиональное образование}");
 report.write("\\par\\ql{ФОРМА ОБУЧЕНИЯ: "+forma_Ob+"}\\line");
 report.write("\\par");
 report.write("\\fs16 \\b0 \\qc \\trowd \\trqc\\trgaph58\\trrh80\\trleft36");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\intbl{№ п/п}\\cell");
 report.write("\\intbl{Число направлений подготовки (специальностей) образовательного учреждения ВПО/СПО (филиала), по которым проводился прием}\\cell");
 report.write("\\intbl{Число направлений подготовки (специальностей) образовательного учреждения ВПО/СПО (филиала), по которым проводился прием по результатам ЕГЭ}\\cell");
 report.write("\\intbl{Число подан- ных заявлений}\\cell");
 report.write("\\intbl{Контрольные цифры приема (по данной форме обучения)}\\cell");
 report.write("\\intbl{Принято на 1 курс студентов}\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs16 \\b0\\trowd\\trqc\\trgaph58\\trrh80\\trleft36");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1\\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1\\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{Контрольные}\\cell");
 report.write("\\intbl{Включая поступивших на места с оплатой стоимости обучения}\\cell");
 report.write("\\intbl{На бюджетной основе}\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs16 \\b0\\trowd\\trqc\\trgaph58\\trrh80\\trleft36");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3400");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5260");
 report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10840");
 report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{Всего}\\cell");
 report.write("\\intbl{Из них по ЕГЭ}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{Всего}\\cell");
 report.write("\\intbl{Из них}\\cell");
 report.write("\\intbl{Всего}\\cell");
 report.write("\\intbl{Из них}\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs16 \\b0\\trowd\\trqc\\trgaph58\\trrh80\\trleft36");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3400");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5260");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5880");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500");
 report.write("\\clvertalc\\clshdngraw1000 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10840");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11460");
 report.write("\\clvmgf\\clvertalc\\cltxbtlr \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12080");
 report.write("\\clvertalc\\clshdngraw1000 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{Иногородних (из 7)}\\cell");
 report.write("\\intbl{Из сельской местности (из 7)}\\cell");
 report.write("\\intbl{В том числе по результатам ЕГЭ}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{Иногородних (из 16)}\\cell");
 report.write("\\intbl{Из сельской местности (из 16)}\\cell");
 report.write("\\intbl{В том числе по результатам ЕГЭ}\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs16 \\b0\\trowd\\trqc\\trgaph58\\trrh80\\trleft36");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3400");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5260");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5880");
 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000\\trrh3300 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7120");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7740");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8360");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8980");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9600");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvmrg\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10840");
 report.write("\\clvmrg\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11460");
 report.write("\\clvmrg\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12080");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13320");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13940");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14560");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15180");
 report.write("\\clvertalc\\cltxbtlr\\clshdngraw1000 \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{Всего (из 7)}\\cell");
 report.write("\\intbl{Иногородних (из 8)}\\cell");
 report.write("\\intbl{Из сельской местности (из 9)}\\cell");
 report.write("\\intbl{1 экзамен сдан в форме ЕГЭ (из 10)}\\cell");
 report.write("\\intbl{Часть экзаменов сдана в форме ЕГЭ (из 10)}\\cell");
 report.write("\\intbl{Все экзамены сданы в форме ЕГЭ (из 10)}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{}\\cell");
 report.write("\\intbl{Всего (из 16)}\\cell");
 report.write("\\intbl{Иногородних (из 17)}\\cell");
 report.write("\\intbl{Из сельской местности (из 18)}\\cell");
 report.write("\\intbl{1 экзамен сдан в форме ЕГЭ (из 19)}\\cell");
 report.write("\\intbl{Часть экзаменов сдана в форме ЕГЭ (из 19)}\\cell");
 report.write("\\intbl{Все экзамены сданы в форме ЕГЭ (из 19)}\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs16 \\b0 \\trowd\\trqc \\trgaph58\\trrh80\\trleft36");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3400");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5260");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5880");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7120");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7740");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8360");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8980");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9600");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10840");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11460");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12080");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13320");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13940");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14560");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15180");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\b0\\intbl{1}\\cell");
 report.write("\\intbl{2}\\cell");
 report.write("\\intbl{3}\\cell");
 report.write("\\intbl{4}\\cell");
 report.write("\\intbl{5}\\cell");
 report.write("\\intbl{6}\\cell");
 report.write("\\intbl{7}\\cell");
 report.write("\\intbl{8}\\cell");
 report.write("\\intbl{9}\\cell");
 report.write("\\intbl{10}\\cell");
 report.write("\\intbl{11}\\cell");
 report.write("\\intbl{12}\\cell");
 report.write("\\intbl{13}\\cell");
 report.write("\\intbl{14}\\cell");
 report.write("\\intbl{15}\\cell");
 report.write("\\intbl{16}\\cell");
 report.write("\\intbl{17}\\cell");
 report.write("\\intbl{18}\\cell");
 report.write("\\intbl{19}\\cell");
 report.write("\\intbl{20}\\cell");
 report.write("\\intbl{21}\\cell");
 report.write("\\intbl{22}\\cell");
 report.write("\\intbl{23}\\cell");
 report.write("\\intbl{24}\\cell");
 report.write("\\intbl\\row\\b0");
 report.write("\\fs20 \\b0 \\trowd\\trqc \\trgaph58\\trrh80\\trleft36");
 report.write("\\clvertalc\\trrh330 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\b1\\intbl{По образовательному учреждению в целом:}\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs20\\trowd\\trqc \\trgaph58\\trrh80\\trleft36");
 report.write("\\clvertalc\\trrh330 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3400");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5260");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5880");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7120");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7740");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8360");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8980");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9600");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10840");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11460");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12080");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13320");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13940");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14560");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15180");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\intbl\\qc\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs20 \\b0 \\trowd\\trqc \\trgaph58\\trrh80\\trleft36");
 report.write("\\clvertalc\\trrh330 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\b0\\intbl{По образовательному учреждению без учета филиалов:}\\cell");
 report.write("\\intbl\\row");

 report.write("\\fs20\\trowd\\trqc \\trgaph58\\trrh80\\trleft36");
 report.write("\\clvertalc\\trrh330 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3400");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5260");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5880");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7120");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7740");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8360");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8980");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9600");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10840");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11460");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12080");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13320");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13940");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14560");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15180");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");

// --1-- Аббревиатура ВУЗа

 stmt = conn.prepareStatement("SELECT AbbreviaturaVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl\\ql\\b1{ {"+rs.getString(1).toUpperCase()+"}}\\b0\\cell");
 }

// --2-- Количество специальностей, по которым проводился прием

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell");
 }

// --3-- Количество специальностей, по которым проводился прием по результатам ЕГЭ

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --4-- Всего подано заявлений

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Abiturient a, Konkurs k, Fakultety f,Spetsialnosti s WHERE a.KodAbiturienta=k.KodAbiturienta AND k.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.DokumentyHranjatsja LIKE 'д' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --5-- Всего подано заявлений по ЕГЭ

 stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Konkurs WHERE KodAbiturienta IN(SELECT a.KodAbiturienta FROM Abiturient a, Spetsialnosti s, Fakultety f, ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND zso.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND zso.OtsenkaEge NOT LIKE '0' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") GROUP BY a.KodAbiturienta)");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --6-- Контрольные цифры приема

 stmt = conn.prepareStatement("SELECT SUM(PlanPriema) FROM Fakultety f,Spetsialnosti s WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --7-- Всего зачислено абитуриентов

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Abiturient a, Fakultety f,Spetsialnosti s WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --8-- Всего зачислено абитуриентов (иногородних) не из Пензенской области

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Oblasti o, Abiturient a, Fakultety f,Spetsialnosti s WHERE o.KodOblasti=a.KodOblasti AND a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.DokumentyHranjatsja LIKE 'д' AND Prinjat IN ('1','2','3','4','д') AND o.NazvanieOblasti NOT LIKE 'Пензенская' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --9-- Всего зачислено абитуриентов (из сел пензенской области)

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Abiturient a, Fakultety f,Spetsialnosti s WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.DokumentyHranjatsja LIKE 'д' AND Prinjat IN ('1','2','3','4','д') AND a.GdePoluchilSrObrazovanie LIKE 'с' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --10-- Всего зачислено по результатам ЕГЭ

 stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE KodAbiturienta IN(SELECT a.KodAbiturienta FROM Abiturient a, Spetsialnosti s, Fakultety f, ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND zso.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND zso.OtsenkaEge NOT LIKE '0' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") GROUP BY a.KodAbiturienta)");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next())
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 
  

// --11-- Всего зачислено по результатам ЕГЭ иногородних

 stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE KodAbiturienta IN(SELECT a.KodAbiturienta FROM Oblasti ob, Abiturient a, Spetsialnosti s, Fakultety f, Otsenki o, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND ob.KodOblasti=a.KodOblasti AND o.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=o.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND o.From_Ege LIKE 'д' AND ob.NazvanieOblasti NOT LIKE 'Пензенская' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") GROUP BY a.KodAbiturienta,o.From_Ege)");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }
  
  
// --12-- Всего зачислено по результатам ЕГЭ из сел

 stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE KodAbiturienta IN(SELECT a.KodAbiturienta FROM Abiturient a, Spetsialnosti s, Fakultety f, Otsenki o, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND o.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=o.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND o.From_Ege LIKE 'д' AND a.GdePoluchilSrObrazovanie LIKE 'с' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") GROUP BY a.KodAbiturienta,o.From_Ege)");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }


// --13-14-15-- Подготовка данных по результатам ЕГЭ (1 экзамен, несколько и все)
 int single=0,notAll=0,all=0,oldKodAb=-1,kodAb;
 boolean sin = false, nall = false, noege = false;
 stmt = conn.prepareStatement("SELECT a.KodAbiturienta,zso.OtsenkaEge FROM Abiturient a, Spetsialnosti s, Fakultety f, ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens,Konkurs k WHERE a.KodAbiturienta=k.KodAbiturienta AND a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND zso.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") ORDER BY a.KodAbiturienta ASC");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 while(rs.next()){

   kodAb = rs.getInt(1);

   if(oldKodAb == -1) { 
     oldKodAb = kodAb;
     continue;
   }

   if(oldKodAb != kodAb) {

     if(sin) single++;    
     if(nall && !noege) all++;
     else if(nall) notAll++;

     oldKodAb = kodAb;
     sin = false;
     nall = false;
     noege = false;
   }

   if(oldKodAb == kodAb) {
// Считана очередная строка одного и того же абитуриента
// sin - сдан один единственный экзанем по ЕГЭ
// nall - сдано более одного, но не все все по ЕГЭ
// al - все экзамены сданы по ЕГЭ
// noege - вспомогательный признак наличия экзамена, сданного не по ЕГЭ
     if(rs.getInt(2) != 0) {
       if(!nall && !sin) { sin = true; continue; }
       if(sin && !nall) { sin = false; nall=true;}
     } else noege = true;
   } 
 }
// Данные последнего абитуриента
     if(sin) single++;    
     if(nall && !noege) all++;
     else if(nall) notAll++;

// Всего зачислено по результатам ЕГЭ (1 экзамен сдан)
  report.write("\\intbl{"+single+"}\\cell");

// Всего зачислено по результатам ЕГЭ (часть экзаменов сдана)
 report.write("\\intbl{"+notAll+"}\\cell");

// Всего зачислено по результатам ЕГЭ (все экзамены сданы)
 report.write("\\intbl{"+all+"}\\cell");

// --16-- Всего зачислено абитуриентов (БЮДЖЕТНИКИ)
 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Abiturient a, Fakultety f,Spetsialnosti s WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND f.KodVuza LIKE ? AND a.NomerPlatnogoDogovora IS NULL AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --17-- Всего зачислено абитуриентов (иногородних) не из Пензенской области (БЮДЖЕТНИКИ)

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Oblasti o, Abiturient a, Fakultety f,Spetsialnosti s WHERE o.KodOblasti=a.KodOblasti AND a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.DokumentyHranjatsja LIKE 'д' AND a.NomerPlatnogoDogovora IS NULL AND Prinjat IN ('1','2','3','4','д') AND o.NazvanieOblasti NOT LIKE 'Пензенская' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --18-- Всего зачислено абитуриентов (из сел пензенской области) (БЮДЖЕТНИКИ)

 stmt = conn.prepareStatement("SELECT COUNT(*) FROM Abiturient a, Fakultety f,Spetsialnosti s WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.NomerPlatnogoDogovora IS NULL AND a.DokumentyHranjatsja LIKE 'д' AND Prinjat IN ('1','2','3','4','д') AND a.GdePoluchilSrObrazovanie LIKE 'с' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+")");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }

// --19-- Всего зачислено по результатам ЕГЭ (БЮДЖЕТНИКИ)

 stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE KodAbiturienta IN(SELECT a.KodAbiturienta FROM Abiturient a, Spetsialnosti s, Fakultety f, ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND zso.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND a.NomerPlatnogoDogovora IS NULL AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND zso.OtsenkaEge NOT LIKE '0' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") GROUP BY a.KodAbiturienta)");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }


// --20-- Всего зачислено по результатам ЕГЭ иногородних (БЮДЖЕТНИКИ)

 stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE KodAbiturienta IN(SELECT a.KodAbiturienta FROM Oblasti ob, Abiturient a, Spetsialnosti s, Fakultety f, Otsenki o, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND ob.KodOblasti=a.KodOblasti AND o.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=o.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND a.NomerPlatnogoDogovora IS NULL AND o.From_Ege LIKE 'д' AND ob.NazvanieOblasti NOT LIKE 'Пензенская' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") GROUP BY a.KodAbiturienta,o.From_Ege)");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }
  
// --21-- Всего зачислено по результатам ЕГЭ из сел (БЮДЖЕТНИКИ)

 stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE KodAbiturienta IN(SELECT a.KodAbiturienta FROM Abiturient a, Spetsialnosti s, Fakultety f, Otsenki o, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND o.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=o.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND a.NomerPlatnogoDogovora IS NULL AND o.From_Ege LIKE 'д' AND a.GdePoluchilSrObrazovanie LIKE 'с' AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") GROUP BY a.KodAbiturienta,o.From_Ege)");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 if(rs.next()){
   report.write("\\intbl{"+rs.getString(1)+"}\\cell");
 }
    
// --22-23-24-- Подготовка данных по результатам ЕГЭ - БЮДЖЕТНИКИ (1 экзамен, несколько и все)
 single=0;
 notAll=0;
 all=0;
 oldKodAb=-1;
 sin = false;
 nall = false;
 noege = false;
 stmt = conn.prepareStatement("SELECT a.KodAbiturienta,zso.OtsenkaEge FROM Abiturient a, Spetsialnosti s, Fakultety f, ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND zso.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnZach=ens.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND a.Prinjat IN ('1','2','3','4','д') AND a.NomerPlatnogoDogovora IS NULL AND f.KodVuza LIKE ? AND f.AbbreviaturaFakulteta NOT IN ("+exclude_faks+") ORDER BY a.KodAbiturienta ASC");
 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
 rs = stmt.executeQuery();
 while(rs.next()){

   kodAb = rs.getInt(1);

   if(oldKodAb == -1) { 
     oldKodAb = kodAb;
     continue;
   }

   if(oldKodAb != kodAb) {

     if(sin) single++;    
     if(nall && !noege) all++;
     else if(nall) notAll++;

     oldKodAb = kodAb;
     sin = false;
     nall = false;
     noege = false;
   }

   if(oldKodAb == kodAb) {
// Считана очередная строка одного и того же абитуриента
// sin - сдан один единственный экзанем по ЕГЭ
// nall - сдано более одного, но не все все по ЕГЭ
// al - все экзамены сданы по ЕГЭ
// noege - вспомогательный признак наличия экзамена, сданного не по ЕГЭ
     if(rs.getInt(2) != 0) {
       if(!nall && !sin) { sin = true; continue; }
       if(sin && !nall) { sin = false; nall=true;}
     } else noege = true;
   } 
 }
// Данные последнего абитуриента
     if(sin) single++;    
     if(nall && !noege) all++;
     else if(nall) notAll++;

// Всего зачислено по результатам ЕГЭ (1 экзамен сдан)
  report.write("\\intbl{"+single+"}\\cell");

// Всего зачислено по результатам ЕГЭ (часть экзаменов сдана)
 report.write("\\intbl{"+notAll+"}\\cell");

// Всего зачислено по результатам ЕГЭ (все экзамены сданы)
 report.write("\\intbl{"+all+"}\\cell");

 report.write("\\intbl\\row");

 report.write("\\fs20 \\b0 \\trowd\\trqc \\trgaph58\\trrh80\\trleft36");
 report.write("\\clvertalc\\trrh330 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\b0\\intbl\\qc{В том числе по каждому из его филиалов:}\\cell");
 report.write("\\intbl\\row");
 report.write("\\fs20\\trowd\\trqc \\trgaph58\\trrh80\\trleft36");
 report.write("\\clvertalc\\trrh330 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1540");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2160");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2780");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3400");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4020");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4640");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5260");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5880");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7120");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7740");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8360");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8980");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9600");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10220");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10840");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11460");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12080");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13320");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13940");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14560");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15180");
 report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800");
 report.write("\\intbl\\ql\\b1{ КИИУТ }\\par{ г. Кузнецк}\\b0\\cell");
 report.write("\\intbl\\qc\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\row");
 report.write("\\intbl\\ql\\b1{ СФ ПГУ }\\par{ г. Сердобск}\\b0\\cell");
 report.write("\\intbl\\qc\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\cell");
 report.write("\\intbl\\row");
 report.write("\\pard\\par\\par");

 stmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%Ректор%'");
 rs = stmt.executeQuery();
 if(rs.next()) {
   report.write("\\tab\\tab\\tab{"+rs.getString(1)+"}:");
   report.write(" _____________________ ({"+rs.getString(2)+"})");
   report.write("\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\fs16{М.П.}");
 }

 report.write("\\fs16\\par\\tab\\tab\\tab\\tab\\tab{   (подпись)}");
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
        if(rep_itogi_zach_f) return mapping.findForward("rep_itogi_zach_f");
        return mapping.findForward("success");
    }
}