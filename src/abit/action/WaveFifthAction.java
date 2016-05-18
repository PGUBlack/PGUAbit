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

public class WaveFifthAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    pstmt              = null;
        Statement            stmt               = null;
        Statement            stmt2              = null;
        Statement            stmt3              = null;
        ResultSet            rs                 = null;
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsDecForm         form               = (ListsDecForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_dec_ege_f    = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        String               BUD_DOG            = new String();          
        String               TIP_SPEC           = new String();          
        String               command            = new String();           // команда
        String               AS                 = new String();           // аббревиатура специальности
        String               SS                 = new String();           // шифр
        String               NS                 = new String();           // название специальности
        String               PP                 = new String();           // план приема
        String               VP                 = new String();           // вакансии приема
        String               ZACH_ABTs          = new String();           // кол-во зачисленных абитуриентов на текущую специальность
        int                  rezerv             = 0;                      // признак необходимости вывода шапки таблицы для резерва зачисления
        boolean              overload           = false;                  // признак переполнения плана приема
        int                  total_abits        = 1;                      // ВСЕГО РЕКОМЕНДОВАНО абитуриентов на тек. спец-ть
        int                  TP1_vak            = 0;                      // план целевого приема 1
        int                  TP1_zan            = 0;                      // занято мест целевого приема 1
        int                  TP2_vak            = 0;                      // план целевого приема 2
        int                  TP2_zan            = 0;                      // занято мест целевого приема 2
        String               AF                 = new String();           // аббревиатура факультета
        StringBuffer         excludeList        = new StringBuffer("-1");
        StringBuffer         query              = new StringBuffer();
        int                  kAbit              = -1;
        int                  summa              = -1;
        int                  oldBallAbt         = -1;
        boolean              only_one_run       = true;
        boolean              TselPriem_Ok       = false;
        boolean              header             = false;
        boolean              primechanie        = false;
        int                  nomer              = 0;
        int                  count_predm        = 1; // Только профильный предмет

        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "waveFifthAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "waveFifthForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          pstmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
          pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
            abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
            abit_SD_S1.add(abit_TMP);
          }
 
      if ( form.getAction() == null ) {

           form.setAction(us.getClientIntName("view","init"));

      } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  String priority = new String();

  String priority_query = new String();

// ИСХОДНЫЕ ПАРАМЕТРЫ

  abit_SD.setSpecial2("-");

  abit_SD.setSpecial3("-");

  priority_query = "%";

/// Если не хватит ОП на обработку всех факультетов ВУЗа сразу, то - закомментировать строку ниже и по каждому факультету отдельно
 if(abit_SD.getSpecial5() != null) abit_SD.setKodFakulteta(new Integer("-1"));

  if(!(abit_SD.getSpecial4() != null && abit_SD.getSpecial4().length() > 1 )) { 
    priority = abit_SD.getSpecial4()+"-го приоритета";
    priority_query = abit_SD.getSpecial4();
  }

  if(abit_SD.getSpecial5() != null) {

    if((abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) command = "маркировка";
    else if((abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdtcmdep") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct[ddept"))) command = "размаркировкавуза";
    else if((abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) command = "размаркировка";
    else if((abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) command = "размаркировкавсех";
    else if((abit_SD.getSpecial5().equals("dsxthrbdfybt") || abit_SD.getSpecial5().equals("dsxbnfybt") || abit_SD.getSpecial5().equals("dsxtcnm") || abit_SD.getSpecial5().equals("dsxthryenm") || abit_SD.getSpecial5().equals("dsxthryenmdct["))) command = "вычеркивание";
    else if((abit_SD.getSpecial5().equals("jnrfpfnmdpfxbcktybb") || abit_SD.getSpecial5().equals("jnrfpdpfxbcktybb"))) command = "отказвзачислении";
    else if((abit_SD.getSpecial5().equals("pfxbcktybt") || abit_SD.getSpecial5().equals("pfxbckbnm") || abit_SD.getSpecial5().equals("pfxbckbnmhtrjvtyljdfyys["))) command = "зачисление (этап1)";
    else if((abit_SD.getSpecial5().equals("pfxbcktybt2") || abit_SD.getSpecial5().equals("pfxbckbnm2") || abit_SD.getSpecial5().equals("pfxbckbnmhtrjvtyljdfyys[2"))) command = "зачисление (этап2)";
    else command = "unknown";
  }

  if(StringUtil.toInt(""+abit_SD.getKodFakulteta(),-1) != -1) {
    pstmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ?");
    pstmt.setObject(1,abit_SD.getKodFakulteta(),Types.INTEGER);
    rs = pstmt.executeQuery();
    if(rs.next()) {
      AF = rs.getString(1).toUpperCase();
    }
  } else {
// Все факультеты ВУЗа
      AF = "ALL";
  }

  String name = "Список абитуриентов 2-го этапа (финал) "+AF+" "+priority+" ("+command+")";

  String file_con = "lists_"+StringUtil.toEng(AF)+"_sec_wave_fin_"+StringUtil.toEng(command);

  if(priority_query.equals("%")) file_con += "_allPr";
  else file_con += priority_query;

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
 
  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");

  if(abit_SD.getSpecial5() != null) {
    report.write("\\fs40 \\qc{Процедура:}\\par{"+command+"}\\par");
    if(AF.equals("ALL")) report.write("{для абитуриентов всех факультетов ВУЗа выполнена!}\\par\n");
    else report.write("{для факультета "+AF+" выполнена!}\\par\n");
  }

  pstmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza,DataFormFirstEtap FROM NazvanieVuza WHERE KodVuza LIKE ?");
  pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()) {
    if(abit_SD.getSpecial5() == null) 
      report.write("\\fs40\\qc{"+rs.getString(1)+"}\\par\\fs24{Данные сформированы по состоянию на: "+rs.getString(3)+"}\n");
  }

/**********************************************************/
/**                                                      **/
/**  Перебираем все специальности указанного факультета  **/
/**                                                      **/
/**********************************************************/

  stmt2 = conn.createStatement();

  query.delete(0,query.length());

  query.append("SELECT DISTINCT f.AbbreviaturaFakulteta,s.ShifrSpetsialnosti,s.Abbreviatura,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,s.PlanPriema,s.TselPr_PGU,s.TselPr_ROS FROM Fakultety f,Spetsialnosti s, Konkurs kon, Abiturient a,Forma_Obuch fo WHERE f.KodFakulteta=s.KodFakulteta AND fo.KodFormyOb=a.KodFormyOb AND a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki") || abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
    query.append("AND s.PlanPriema NOT LIKE '0' ");

  if(StringUtil.toInt(""+abit_SD.getKodFakulteta(),-1) != -1)
    query.append("AND s.KodFakulteta LIKE '"+abit_SD.getKodFakulteta()+"' ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

///System.out.println("FAKS="+query.toString());
  rs2 = stmt2.executeQuery(query.toString());
  while(rs2.next()) {

// Для каждой специальности нумерация своя, свой список исключения и сбрасываются различные признаки

    nomer = 0;

    rezerv = 0;

    total_abits = 1;

    excludeList.delete(0,excludeList.length());

    excludeList = new StringBuffer("-1");

    TselPriem_Ok = false;

    overload = false;

    SS = rs2.getString(2);
    AS = rs2.getString(3);
    NS = rs2.getString(4).toUpperCase();

    if(StringUtil.toInt(rs2.getString(6), 0) == 0) PP = "0";
    else PP  = "" + StringUtil.toInt(rs2.getString(6), 0);

    TP1_vak = StringUtil.toInt(rs2.getString(7), 0);
    TP2_vak = StringUtil.toInt(rs2.getString(8), 0);

    if(abit_SD.getSpecial5() == null) {
      report.write("\\fs24\\par\\par\n");
      report.write("\\fs26\\b0\\qc{Список абитуриентов }\\b1{"+AF+"}\\b0\\par{поступающих на специальность (направление):}\\par\\fs24\\b1{"+SS+" }\\'ab{"+NS+"}\\'bb\\qc{ ("+AS+")}\\b0\\par\n");
    }

// Определяем количество зачисленных абитуриентов

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
      BUD_DOG = "IS NULL";
    else
      BUD_DOG = "LIKE 'д'";
//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      BUD_DOG = "IS NULL AND kon.Forma_Ob LIKE 'з' AND Six LIKE 'д'";
//    else 
//      BUD_DOG = "LIKE 'д' AND kon.Forma_Ob LIKE 'з' AND Six LIKE 'д'";

    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon WHERE kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach=sp.KodSpetsialnosti AND ab.KodSpetsialnZach LIKE '"+rs2.getString(5)+"' AND ab.Prinjat IN('1','2','3','4','д') AND kon.Dog_Ok "+BUD_DOG);
    if(rs.next()) ZACH_ABTs = rs.getString(1);
    else ZACH_ABTs = "0";

// Определяем количество вакансий на спец-ти

    VP = ""+ (StringUtil.toInt(PP,0) - StringUtil.toInt(ZACH_ABTs,0));

    if(StringUtil.toInt(PP, 0) != 0) {
      if(abit_SD.getSpecial5() == null)
        report.write("\\fs24\\qc\\b1{План приёма: } "+PP+"{  Вакансии: } "+VP+"\\b0\\par\n");
    }
    else {
      if(abit_SD.getSpecial5() == null)
        report.write("\\par");
    }

    if(abit_SD.getSpecial5() == null && StringUtil.toInt(VP,0) != 0)
      report.write("\\fs28\\qc\\b1{Рекомендованные к зачислению}\\b0\\par\n");

// Процедура размаркировки всех абитуриентов ВУЗа

/***************************************************/
/***  размаркироватьвесьвуз - размаркировкавуза  ***/
/***************************************************/
   if(abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdtcmdep") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdepf"))) {
     stmt3 = conn.createStatement();
     stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE Zach NOT IN ('д','о','н') AND KodAbiturienta NOT IN(SELECT KodAbiturienta FROM Abiturient WHERE Prinjat IN('1','2','3','4','д'))");
   }

// Процедура зачисления рекомендованных

/******************************************************************/
/***  зачисление - зачислить - зачислитьрекомендованных (этап1) ***/
/******************************************************************/
   if(abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("pfxbcktybt") || abit_SD.getSpecial5().equals("pfxbckbnm") || abit_SD.getSpecial5().equals("pfxbckbnmhtrjvtyljdfyys["))) {

     stmt3 = conn.createStatement();
     stmt3.executeUpdate("UPDATE Abiturient SET Prinjat='2' WHERE TipDokSredObraz LIKE 'о' AND KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р')");

     stmt3 = conn.createStatement();
     stmt3.executeUpdate("UPDATE Konkurs SET Zach='д' WHERE Zach LIKE 'р' AND KodAbiturienta IN (SELECT KodAbiturienta FROM Abiturient WHERE Prinjat LIKE '2')");

//     stmt3 = conn.createStatement();
//     stmt3.executeUpdate("UPDATE Konkurs SET Zach='' WHERE KodAbiturienta IN (SELECT kon.KodAbiturienta FROM Konkurs kon WHERE kon.Zach LIKE 'д')");

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT KodSpetsialnosti,KodAbiturienta FROM Konkurs WHERE Zach LIKE 'д'");
     while(rs.next()) {
       stmt3 = conn.createStatement();
       stmt3.executeUpdate("UPDATE Abiturient SET KodSpetsialnZach='"+rs.getString(1)+"' WHERE KodAbiturienta LIKE '"+rs.getString(2)+"'");
     }

// Выходим из цикла обработки факультетов

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'в'");
     if(rs.next()) {
       report.write("\\par\\par\\fs28\\qc\\b1{Вычеркнуто: "+rs.getString(1)+"}\\b0\n");
     }

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'р'");
     if(rs.next()) {
       report.write("\\par\\fs28\\qc\\b1{Рекомендовано: "+rs.getString(1)+"}\\b0\\par\n");
     }

     report.write("}"); 
     report.close();
     form.setAction(us.getClientIntName("new_rep","crt"));
     return mapping.findForward("rep_brw");
   }

// Процедура зачисления рекомендованных

/******************************************************************/
/***  зачисление - зачислить - зачислитьрекомендованных (этап2) ***/
/******************************************************************/
   if(abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("pfxbcktybt2") || abit_SD.getSpecial5().equals("pfxbckbnm2") || abit_SD.getSpecial5().equals("pfxbckbnmhtrjvtyljdfyys[2"))) {

     stmt3 = conn.createStatement();
     stmt3.executeUpdate("UPDATE Abiturient SET Prinjat='3' WHERE TipDokSredObraz LIKE 'о' AND KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р')");

     stmt3 = conn.createStatement();
     stmt3.executeUpdate("UPDATE Konkurs SET Zach='д' WHERE Zach LIKE 'р' AND KodAbiturienta IN (SELECT KodAbiturienta FROM Abiturient WHERE Prinjat LIKE '3')");

//     stmt3 = conn.createStatement();
//     stmt3.executeUpdate("UPDATE Konkurs SET Zach='' WHERE KodAbiturienta IN (SELECT kon.KodAbiturienta FROM Konkurs kon WHERE kon.Zach LIKE 'д')");

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT KodSpetsialnosti,KodAbiturienta FROM Konkurs WHERE Zach LIKE 'д'");
     while(rs.next()) {
       stmt3 = conn.createStatement();
       stmt3.executeUpdate("UPDATE Abiturient SET KodSpetsialnZach='"+rs.getString(1)+"' WHERE KodAbiturienta LIKE '"+rs.getString(2)+"'");
     }

// Выходим из цикла обработки факультетов

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'в'");
     if(rs.next()) {
       report.write("\\par\\par\\fs28\\qc\\b1{Вычеркнуто: "+rs.getString(1)+"}\\b0\n");
     }

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'р'");
     if(rs.next()) {
       report.write("\\par\\fs28\\qc\\b1{Рекомендовано: "+rs.getString(1)+"}\\b0\\par\n");
     }

     report.write("}"); 
     report.close();
     form.setAction(us.getClientIntName("new_rep","crt"));
     return mapping.findForward("rep_brw");
   }

// Процедура установки признака отказа в зачислении

/***************************************************/
/***  отказатьвзачислении - отказвзачислении  ***/
/***************************************************/
   if(abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("jnrfpfnmdpfxbcktybb") || abit_SD.getSpecial5().equals("jnrfpdpfxbcktybb"))) {

     if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
       TIP_SPEC = "IN('о')";
     else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
       TIP_SPEC = "IN('о')";
     else 
       TIP_SPEC = "IN('з')";

     stmt3 = conn.createStatement();
     stmt3.executeUpdate("UPDATE Konkurs SET Zach='х' WHERE KodSpetsialnosti IN (SELECT KodSpetsialnosti FROM Spetsialnosti WHERE Tip_Spec "+TIP_SPEC+") AND KodAbiturienta IN (SELECT KodAbiturienta FROM Abiturient WHERE TipDokSredObraz LIKE 'к')");

// Выходим из цикла обработки факультетов

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'х'");
     if(rs.next()) {
       report.write("\\par\\par\\fs28\\qc\\b1{Вычеркнуто: "+rs.getString(1)+"}\\b0\n");
     }

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'р'");
     if(rs.next()) {
       report.write("\\par\\fs28\\qc\\b1{Рекомендовано: "+rs.getString(1)+"}\\b0\\par\n");
     }

     report.write("}"); 
     report.close();
     form.setAction(us.getClientIntName("new_rep","crt"));
     return mapping.findForward("rep_brw");
   }

/*************************************************************************/
/*** вычеркивание - вычитание - вычесть - вычеркнуть - вычеркнуть всех ***/
/*************************************************************************/
   if(abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("dsxthrbdfybt") || abit_SD.getSpecial5().equals("dsxbnfybt") || abit_SD.getSpecial5().equals("dsxtcnm") || abit_SD.getSpecial5().equals("dsxthryenm") || abit_SD.getSpecial5().equals("dsxthryenmdct["))) {

// Процедура вычеркивания из списков рекомендованных тех абитуриентов, которые прошли по приоритету № 1

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '1'");
     while(rs.next()) {
       stmt3 = conn.createStatement();
       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Zach LIKE 'р' AND Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
     }

// Процедура вычеркивания из списков рекомендованных тех абитуриентов, которые прошли по приоритету № 2

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '2'");
     while(rs.next()) {
       stmt3 = conn.createStatement();
       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Zach LIKE 'р' AND Prioritet LIKE '3' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
     }

// Выходим из цикла обработки факультетов

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'в'");
     if(rs.next()) {
       report.write("\\par\\par\\fs28\\qc\\b1{Вычеркнуто: "+rs.getString(1)+"}\\b0\n");
     }

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'р'");
     if(rs.next()) {
       report.write("\\par\\fs28\\qc\\b1{Рекомендовано: "+rs.getString(1)+"}\\b0\\par\n");
     }

     report.write("}"); 
     report.close();
     form.setAction(us.getClientIntName("new_rep","crt"));
     return mapping.findForward("rep_brw");
   }


// Проверка на количество вакансий - если их 0, то не выводим "рекомендованные к зачислению" (для корректного отображения списка)

   if( StringUtil.toInt(VP,0) == 0 ) rezerv = 1;


/******************************************************/
/**********  ЗАПРОСЫ НА ВЫБОРКУ АБИТУРИЕНТОВ **********/
/******************************************************/



// ОЛИМПИЙЦЫ (без вступительных испытаний)

    header = false;

    query.delete(0,query.length());

    query.append("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+rs2.getString(5)+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (на данный момент это только целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д','х') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

    query.append(" AND kon.Prioritet LIKE '"+priority_query+"' ");

// Мешали спец-ти Сур. Тал-ов    query.append(" AND (m.ShifrMedali IN ('о','к') AND (s.ShifrSpetsialnosti IN ("+Constants.Sur_Talants_NO_KONKURS+"))) ");
    query.append(" AND (m.ShifrMedali IN ('о','к')) ");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");


    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");
///System.out.println("OLIMP="+query.toString());
    stmt = conn.createStatement();
    rs = stmt.executeQuery(query.toString());
    while(rs.next()) {

      if(abit_SD.getSpecial5() == null && (!header || rezerv == 1)) {

        report.write("\\pard\\par\n");

        if( !header )
          report.write("\\b1\\ql\\fs28{Поступающие без вступительных испытаний}\\b0\\par\\par\n");
        else
          report.write("\\b1\\qc\\fs28{Список резерва для зачисления:}\\b0\\par\\par\n");

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        report.write("\\intbl{Номер личного дела}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Отл.}\\cell\n");
        report.write("\\intbl{Проф.}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"') ORDER BY KodPredmeta ASC");
        if(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");

        header = true;
      }

      if(abit_SD.getSpecial5() == null) {
        report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");
        report.write("\\intbl\\qc "+rs.getString(2)+"\\cell\n"); // NLD
        report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR (текущий НЛД)
        report.write("\\intbl\\ql "+rs.getString(3)+" "+rs.getString(4).substring(0,1)+"."+rs.getString(5).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
        report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
        report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // LGOTA

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti sp WHERE zso.KodPredmeta=sp.KodPredmeta AND sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND zso.KodAbiturienta LIKE '"+rs.getString(1)+"' ORDER BY zso.KodPredmeta ASC");
        if(rs3.next()) {
          report.write("\\intbl\\qc "+rs3.getString(1)+"\\cell\n"); // OtsenkaEge
        }
        report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
        report.write("\\intbl\\row\n");
      }
// Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
      excludeList.append(","+rs.getString(1));

// Подсчитываем общее количество абитуриентов для разбиения на рекомендованных к зачислению и резерв
// Если общее кол-во абитуриентов на специальность больше разности плана приема и кол-ва зачисленных, то оставшихся выводим в табличку резерва

      total_abits++;
///System.out.println("total_abts="+total_abits+" PP="+PP+" ZACH="+ZACH_ABTs+" rezerv="+rezerv);
///System.out.println("Raznost="+(StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0)));
      if(total_abits > (StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0))) { 

        overload = true;

        rezerv += 1;
      }

// Установка признака "Рекомендован к зачислению" - "р"

      if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) {
// маркировать - маркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach='р' WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) {
// размаркировать - размаркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) {
// размаркироватьвсех - размаркировкавсех
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      }
    }

    if(header && abit_SD.getSpecial5() == null) report.write("\\pard\\par\n");




// ЛЬГОТНИКИ (только инвалиды и сироты)

    header = false;

    query.delete(0,query.length());

    query.append("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND l.ShifrLgot IN ('и','с') AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д','х') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");
 
    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

    stmt = conn.createStatement();
    rs = stmt.executeQuery(query.toString());
    while(rs.next()) {

      if(abit_SD.getSpecial5() == null && (!header || rezerv == 1)) {

        report.write("\\pard\\par\n");

        if( !header )
          report.write("\\b1\\ql\\fs28{Поступающие вне конкурса}\\b0\\par\\par\n");
        else
          report.write("\\b1\\qc\\fs28{Список резерва для зачисления:}\\b0\\par\\par\n");

        header = true;

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        report.write("\\intbl{Номер личного дела}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Льг.}\\cell\n");
        report.write("\\intbl{Проф.}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"') ORDER BY KodPredmeta ASC");
        if(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");
      }

      if(abit_SD.getSpecial5() == null) {
        report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");
        report.write("\\intbl\\qc "+rs.getString(2)+"\\cell\n"); // NLD
        report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR (текущий НЛД)
        report.write("\\intbl\\ql "+rs.getString(3)+" "+rs.getString(4).substring(0,1)+"."+rs.getString(5).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
        report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
        report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // LGOTA

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti sp WHERE zso.KodPredmeta=sp.KodPredmeta AND sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND zso.KodAbiturienta LIKE '"+rs.getString(1)+"' ORDER BY zso.KodPredmeta ASC");
        if(rs3.next()) {
          report.write("\\intbl\\qc "+rs3.getString(1)+"\\cell\n"); // OtsenkaEge
        }
        report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
        report.write("\\intbl\\row\n");
      }
//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
      excludeList.append(","+rs.getString(1));

// Подсчитываем общее количество абитуриентов для разбиения на рекомендованных к зачислению и резерв
// Если общее кол-во абитуриентов на специальность больше разности плана приема и кол-ва зачисленных, то оставшихся выводим в табличку резерва

      total_abits++;
///System.out.println("total_abts="+total_abits+" PP="+PP+" ZACH="+ZACH_ABTs+" rezerv="+rezerv);
///System.out.println("Raznost="+(StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0)));

      if(total_abits > (StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0))) { 

        overload = true;

        rezerv += 1;
      }

// Установка признака "Рекомендован к зачислению" - "р"

      if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) {
// маркировать - маркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach='р' WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) {
// размаркировать - размаркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) {
// размаркироватьвсех - размаркировкавсех
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      }
    }
  
    if(header && abit_SD.getSpecial5() == null) report.write("\\pard\\par\n");
      




// ЦЕЛЕВОЙ ПРИЕМ ( Уч. совет ПГУ )


    header = false;

    int total_amount = 0, tselev_nomer = 0;

    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT COUNT(kon.KodSpetsialnosti) FROM Spetsialnosti sp, Konkurs kon, Abiturient ab,TselevojPriem tp WHERE tp.KodTselevogoPriema=ab.KodTselevogoPriema AND ab.KodSpetsialnZach=sp.KodSpetsialnosti AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND kon.Target LIKE 'д' AND ab.Prinjat LIKE '1' AND tp.ShifrPriema IN ('ц','ф')");
    if( rs.next() ) TP1_zan = StringUtil.toInt(rs.getString(1),0);
    else TP1_zan = 0;

    total_amount = TP1_vak - TP1_zan;

    if( (TP1_vak - TP1_zan) > 0 ) {

      if(abit_SD.getSpecial5() == null && (!header || rezerv == 1)) {

        report.write("\\pard\\par\n");

        if( !header )
          report.write("\\b1\\ql\\fs28{Поступающие по целевому приёму (в интересах органов гос. власти)}\\b0\\par\\par\n");
        else
          report.write("\\b1\\qc\\fs28{Список резерва для зачисления:}\\b0\\par\\par\n");

        header = true;

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8900+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        report.write("\\intbl{Номер личного дела}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Целев.}\\par{приём}\\cell\n");
        report.write("\\intbl{Проф.}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8400\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8900+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"') ORDER BY KodPredmeta ASC");
        if(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8400\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8900+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");
      }

// Абитуриенты-целевики

      query.delete(0,query.length());

      query.append("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND tp.ShifrPriema IN ('ц','ф') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

//  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

      query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

      query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д','х') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

      if(abit_SD.getSpecial2().equals("orig"))
        query.append("AND a.TipDokSredObraz LIKE ('о') ");
      else if(abit_SD.getSpecial2().equals("copy"))
        query.append("AND a.TipDokSredObraz LIKE ('к') ");

      if(abit_SD.getSpecial3().equals("rek"))
        query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

      query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

      stmt = conn.createStatement();
      rs = stmt.executeQuery(query.toString());
//System.out.println(query.toString());
      while(rs.next()) {

        if((++tselev_nomer) <= total_amount) ++nomer;

        if(abit_SD.getSpecial5() == null) {

          TselPriem_Ok = true;

          report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
          report.write("\\intbl\\qc "+rs.getString(2)+"\\cell\n"); // NLD
          report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
          report.write("\\intbl\\ql "+rs.getString(3)+" "+rs.getString(4).substring(0,1)+"."+rs.getString(5).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
          report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
          report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // KOD TSELEVOGO PRIEMA

          stmt3 = conn.createStatement();
          rs3 = stmt3.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti sp WHERE zso.KodPredmeta=sp.KodPredmeta AND sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND zso.KodAbiturienta LIKE '"+rs.getString(1)+"' ORDER BY zso.KodPredmeta ASC");
          if(rs3.next()) {
            report.write("\\intbl\\qc "+rs3.getString(1)+"\\cell\n"); // OtsenkaEge
          }
          report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
          report.write("\\intbl\\row\n");
        }
//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
        excludeList.append(","+rs.getString(1));

// Подсчитываем общее количество абитуриентов для разбиения на рекомендованных к зачислению и резерв
// Если общее кол-во абитуриентов на специальность больше разности плана приема и кол-ва зачисленных, то оставшихся выводим в табличку резерва

        total_abits++;
///System.out.println("total_abts="+total_abits+" PP="+PP+" ZACH="+ZACH_ABTs+" rezerv="+rezerv);
///System.out.println("Raznost="+(StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0)));

        if(total_abits > (StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0))) { 

          overload = true;

          rezerv += 1;
        }

// Установка признака "Рекомендован к зачислению" - "р"

      if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) {
// маркировать - маркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach='р' WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) {
// размаркировать - размаркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) {
// размаркироватьвсех - размаркировкавсех
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      }
      }

///      if((TP1_vak - TP1_zan) > 0)
// Добавляем строки целевиков с надписью "вакантно"
      for(;tselev_nomer<total_amount;tselev_nomer++) {

        if(abit_SD.getSpecial5() == null) {
          report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // №
          report.write("\\intbl\\qc\\cell\n");                          // NLD
          report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
          report.write("\\intbl\\ql{вакантно}\\cell\n");                // FAMIL I.O.
          report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
          report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA

          stmt3 = conn.createStatement();
          rs3 = stmt3.executeQuery("SELECT sp.KodPredmeta FROM Spetsialnosti sp WHERE sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' ORDER BY sp.KodPredmeta ASC");
          if(rs3.next()) {
            report.write("\\intbl\\qc\\cell\n");                        // OtsenkaEge
          }
          report.write("\\intbl\\qc\\cell\n");                          // SUMMA Ege
          report.write("\\intbl\\row\n");
        }
      }

      if(header && abit_SD.getSpecial5() == null) report.write("\\pard\\par\n");
    }





// ЦЕЛЕВОЙ ПРИЕМ ( Предприятия Минпромторг, Росатом, Роскосмос )


    header = false;

    total_amount = 0;

    tselev_nomer = 0;

    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT TselPr_ROS FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"'");

    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT COUNT(kon.KodSpetsialnosti) FROM Spetsialnosti sp, Konkurs kon, Abiturient ab,TselevojPriem tp WHERE tp.KodTselevogoPriema=ab.KodTselevogoPriema AND ab.KodAbiturienta=kon.KodAbiturienta AND ab.KodSpetsialnZach=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND kon.Target LIKE 'д' AND ab.Prinjat LIKE '1' AND tp.ShifrPriema IN ('а','к','т')");
    if( rs.next() ) TP2_zan = StringUtil.toInt(rs.getString(1),0);
    else TP2_zan = 0;

    total_amount = TP2_vak - TP2_zan;

    if( (TP2_vak - TP2_zan) > 0 ) {

      if(abit_SD.getSpecial5() == null && (!header || rezerv == 1)) {

        report.write("\\pard\\par\n");

        if( !header )
          report.write("\\b1\\ql\\fs28{Поступающие по целевому приёму (в интересах Минпромторга, Росатома, Роскосмоса)}\\b0\\par\\par\n");
        else
          report.write("\\b1\\qc\\fs28{Список резерва для зачисления:}\\b0\\par\\par\n");

        header = true;

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8900+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        report.write("\\intbl{Номер личного дела}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Целев.}\\par{приём}\\cell\n");
        report.write("\\intbl{Проф.}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8400\n");

        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8900+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"') ORDER BY KodPredmeta ASC");
        if(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8400\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8900+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");
      }

// Абитуриенты-целевики

    query.delete(0,query.length());

    query.append("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND tp.ShifrPriema IN ('а','к','т') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

//  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

      query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

      query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д','х') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

      if(abit_SD.getSpecial2().equals("orig"))
        query.append("AND a.TipDokSredObraz LIKE ('о') ");
      else if(abit_SD.getSpecial2().equals("copy"))
        query.append("AND a.TipDokSredObraz LIKE ('к') ");

      if(abit_SD.getSpecial3().equals("rek"))
        query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

      query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

      stmt = conn.createStatement();
      rs = stmt.executeQuery(query.toString());
      while(rs.next()) {

        if((++tselev_nomer) <= total_amount) ++nomer;

        if(abit_SD.getSpecial5() == null) {

          TselPriem_Ok = true;

          report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
          report.write("\\intbl\\qc "+rs.getString(2)+"\\cell\n"); // NLD
          report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
          report.write("\\intbl\\ql "+rs.getString(3)+" "+rs.getString(4).substring(0,1)+"."+rs.getString(5).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
          report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
          report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // KOD TSELEVOGO PRIEMA

          stmt3 = conn.createStatement();
          rs3 = stmt3.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti sp WHERE zso.KodPredmeta=sp.KodPredmeta AND sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND zso.KodAbiturienta LIKE '"+rs.getString(1)+"' ORDER BY zso.KodPredmeta ASC");
          if(rs3.next()) {
            report.write("\\intbl\\qc "+rs3.getString(1)+"\\cell\n"); // OtsenkaEge
          }
          report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
          report.write("\\intbl\\row\n");
        }

//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
        excludeList.append(","+rs.getString(1));

// Подсчитываем общее количество абитуриентов для разбиения на рекомендованных к зачислению и резерв
// Если общее кол-во абитуриентов на специальность больше разности плана приема и кол-ва зачисленных, то оставшихся выводим в табличку резерва

        total_abits++;
///System.out.println("total_abts="+total_abits+" PP="+PP+" ZACH="+ZACH_ABTs+" rezerv="+rezerv);
///System.out.println("Raznost="+(StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0)));

        if(total_abits > (StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0))) { 

          overload = true;

          rezerv += 1;
        }

// Установка признака "Рекомендован к зачислению" - "р"

      if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) {
// маркировать - маркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach='р' WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) {
// размаркировать - размаркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) {
// размаркироватьвсех - размаркировкавсех
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      }
      }

// Добавляем строки целевиков с надписью "вакантно"
      for(;tselev_nomer<total_amount;tselev_nomer++) {

        if(abit_SD.getSpecial5() == null) {
          report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // №
          report.write("\\intbl\\qc\\cell\n");                          // NLD
          report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
          report.write("\\intbl\\ql{вакантно}\\cell\n");                // FAMIL I.O.
          report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
          report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA

          stmt3 = conn.createStatement();
          rs3 = stmt3.executeQuery("SELECT sp.KodPredmeta FROM Spetsialnosti sp WHERE sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' ORDER BY sp.KodPredmeta ASC");
          if(rs3.next()) {
            report.write("\\intbl\\qc\\cell\n");                        // OtsenkaEge
          }
          report.write("\\intbl\\qc\\cell\n");                          // SUMMA Ege
          report.write("\\intbl\\row\n");
        }
      }

      if(header && abit_SD.getSpecial5() == null) report.write("\\pard\\par\n");
    }





// По СУММЕ НАБРАННЫХ БАЛЛОВ

    header = false;

    only_one_run = true;

    boolean evidence_exist = true;
    
    oldBallAbt = -1;

// В первом запросе объединения исключаются ВСЕ "Сурские таланты" и олимпиадники вообще

    query.delete(0,query.length());

    query.append("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'-' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д','х') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

    query.append("AND m.ShifrMedali NOT IN ('о','к') ");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");

// Подсоединяем Сурские таланты и др. Олимпиадников с баллом по математике == 100 (ИСКУСТВЕННО, согласно правилам приема (хотя в БД не 100))

    query.append(" UNION SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,100+SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'(о)' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д','х') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

    query.append("AND (m.ShifrMedali IN ('о','к') AND np.Sokr NOT LIKE ('Мат') AND s.ShifrSpetsialnosti IN("+Constants.Sur_Talants_100B_MAT+"))");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");


// Подсоединяем Сурские таланты и др. Олимпиадников с их РЕАЛЬНЫМИ БАЛЛАМИ для СПЕЦИАЛЬНОСТЕЙ НЕ ИЗ ЛЬГОТНОГО СПИСКА

    query.append(" UNION SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'-' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д','х') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

    query.append("AND (m.ShifrMedali IN ('о','к') AND s.ShifrSpetsialnosti NOT IN("+Constants.Sur_Talants_100B_MAT+"))");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

    else
//      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з') AND kon.Six LIKE 'д' AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");

    query.append(" ORDER BY SummaEge DESC,a.TipDokSredObraz DESC,m.ShifrMedali ASC,a.Familija,a.Imja,a.Otchestvo");

//System.out.println(query);

    stmt = conn.createStatement();
    rs = stmt.executeQuery(query.toString());
    while(rs.next()) {

      if(abit_SD.getSpecial5() == null && (!header || rezerv == 1)) {

        report.write("\\pard\\par\n");

        if( !header )
          report.write("\\b1\\ql\\fs28{Успешно прошедшие вступительные испытания}\\b0\\par\\par\n");
        else
          report.write("\\b1\\qc\\fs28{Список резерва для зачисления:}\\b0\\par\\par\n");

        header = true;

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        report.write("\\intbl{Номер личного дела}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Отл.}\\cell\n");
        report.write("\\intbl{Проф.}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"') ORDER BY KodPredmeta ASC");
        if(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(8600+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(10000+count_predm*720)+"\n");
      }

      if(abit_SD.getSpecial5() == null) {
//        report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");
        if(nomer >= StringUtil.toInt(PP, 0) && (rs.getInt(8) == oldBallAbt) && only_one_run ) { nomer+=0; evidence_exist = true; }

        else {

          nomer+=1;

          if(evidence_exist && (rs.getInt(8) != oldBallAbt)) only_one_run = false;

          oldBallAbt = rs.getInt(8);
        }
//System.out.println("PlanPriema="+PP+" Nomer="+nomer+" oldBall="+oldBallAbt+" CurrBall="+rs.getString(8));

        report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
        report.write("\\intbl\\qc "+rs.getString(2)+"\\cell\n"); // NLD
        report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
        report.write("\\intbl\\ql "+rs.getString(3)+" "+rs.getString(4).substring(0,1)+"."+rs.getString(5).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
        report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
        report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // LGOTA

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT zso.OtsenkaEge,np.Sokr FROM ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti sp,NazvanijaPredmetov np WHERE np.KodPredmeta=zso.KodPredmeta AND zso.KodPredmeta=sp.KodPredmeta AND sp.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta,np.Sokr ASC");
        if(rs3.next()) {
          if(rs.getString(10).equals("(о)") && rs3.getString(2).equals("Мат")) {
            report.write("\\intbl\\qc{100*}\\cell\n");                           // OtsenkaEge (для Сурских талантов устанавливаем 100 баллов принудительно)
            primechanie = true;
          }
          else
            report.write("\\intbl\\qc{"+rs3.getString(1)+"}\\cell\n");           // OtsenkaEge
        }
        report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n");                 // SUMMA Ege
        report.write("\\intbl\\row\n");
      }
// Подсчитываем общее количество абитуриентов для разбиения на рекомендованных к зачислению и резерв
// Если общее кол-во абитуриентов на специальность больше разности плана приема и кол-ва зачисленных, то оставшихся выводим в табличку резерва

      total_abits++;
System.out.println("total_abts="+total_abits+" PP="+PP+" ZACH="+ZACH_ABTs+" rezerv="+rezerv);


      if(total_abits > (StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0))) { 

        overload = true;
 
        rezerv += 1;
      }

// Установка признака "Рекомендован к зачислению" - "р"

System.out.println("preMARKING_!!! Sp="+rs2.getString(5)+" Abit="+rs.getString(1)+" rezerv="+rezerv+" special5="+abit_SD.getSpecial5());
      if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) {
// маркировать - маркировка
System.out.println("MARKING_!!! Sp="+rs2.getString(5)+" Abit="+rs.getString(1));
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach='р' WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) {
// размаркировать - размаркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) {
// размаркироватьвсех - размаркировкавсех
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      }
    }


    if(header && abit_SD.getSpecial5() == null) report.write("\\pard\n");

    if(primechanie && abit_SD.getSpecial5() == null) 
      report.write("\\par\\fs24\\ql\\tab\\tab{* - балл установлен согласно п.3.8. правил приёма ПГУ}\\par\n");


// Подпись Ректора

    if(abit_SD.getSpecial5() == null) {
      report.write("\\pard\\par\\par");

      report.write("\\b0\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc\\cellx4000\n");
      report.write("\\clvertalc\\cellx5700\n");
      report.write("\\clvertalc\\cellx8200\n");

      report.write("\\intbl\\qr{Председатель приемной комиссии: }\\cell\n");

      pstmt = conn.prepareStatement("SELECT Facsimile FROM NazvanieVuza WHERE KodVuza LIKE ?");
      pstmt.setObject(1,session.getAttribute("kVuza"),Types.VARCHAR);
      rs = pstmt.executeQuery();
      if(rs.next()) {
        report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
      } else {
        report.write("\\intbl\\cell\n");
      }

      String rektor = new String();
      pstmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%Ректор%'");
      rs = pstmt.executeQuery();
      if(rs.next()) {
        report.write("\\intbl\\ql{  / "+rs.getString(2)+" /}\\cell\n");
      } else {
        report.write("\\intbl\\cell\n");
      }

      report.write("\\intbl\\row\n");
      report.write("\\pard\\par");

      if(TselPriem_Ok) {
        report.write("\\i1");
        report.write("\\par\\fs20\\ql\\tab\\tab\\b1{Примечание.}\\b0\n");
        report.write("\\par\\fs20\\ql\\tab\\tab{ В случае равного количества набранных баллов при прочих равных условиях}\n");
        report.write("\\par\\fs20\\ql\\tab\\tab{зачисляются лица, имеющие более высокий балл по профильному предмету.}\\par\n");
        report.write("\\par\\fs20\\ql\\tab\\tab{ Коды целевого приёма:}\n");
        report.write("\\par\\fs20\\ql\\tab\\tab\\'ab{а}\\'bb{ - прием в интересах предприятий Росатома}\n");
        report.write("\\par\\fs20\\ql\\tab\\tab\\'ab{к}\\'bb{ - прием в интересах предприятий Роскосмоса}\n");
        report.write("\\par\\fs20\\ql\\tab\\tab\\'ab{т}\\'bb{ - прием в интересах предприятий Минпромторга}\n");
        report.write("\\par\\fs20\\ql\\tab\\tab\\'ab{ф}\\'bb{ - прием в интересах войсковой части}\n");
        report.write("\\par\\fs20\\ql\\tab\\tab\\'ab{ц}\\'bb{ - прием в интересах органов власти и местного самоуправления}\n");
        report.write("\\par\\i0");
      }

      report.write("\\page");
    }

   } // Перебор специальностей выбранного факультета


  if(abit_SD.getSpecial5() != null) {
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'в'");
    if(rs.next()) {
      report.write("\\par\\par\\fs28\\qc\\b1{Вычеркнуто: "+rs.getString(1)+"}\\b0\n");
    }

    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT count(KodAbiturienta) FROM Konkurs WHERE Zach LIKE 'р'");
    if(rs.next()) {
      report.write("\\par\\fs28\\qc\\b1{Рекомендовано: "+rs.getString(1)+"}\\b0\\par\n");
    }
  }
  report.write("}"); 
  report.close();

  stmt = conn.createStatement();
  stmt.executeUpdate("UPDATE NazvanieVuza SET DataFormFirstEtap='"+StringUtil.CurrDate(".")+"'");
  
  form.setAction(us.getClientIntName("new_rep","crt"));
  return mapping.findForward("rep_brw");

  }

 }      catch ( SQLException e ) {
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
          if ( stmt2 != null ) {
               try {
                     stmt2.close();
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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_ege_f) return mapping.findForward("lists_dec_ege_f");
        return mapping.findForward("success");
    }
}