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

public class WaveSecondAction extends Action {

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
        Statement stmt9=null;
        PreparedStatement stmt10=null;
        PreparedStatement stmt11=null;
        ResultSet rs9=null;
        ResultSet rs10=null;
        ResultSet rs11=null;
        int ll=0;
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
        String               command            = new String();           // команда
        String               AS                 = new String();           // аббревиатура специальности
        String               SS                 = new String();           // шифр
        String               NS                 = new String();           // название специальности
        String               PP                 = new String();           // план приема
        String               VP                 = new String();           // вакансии приема
        String               ZACH_ABTs          = new String();           // кол-во зачисленных абитуриентов на текущую специальность
        String               ZACH_ABTsT          = new String();
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
        StringBuffer		 query1  			= new StringBuffer();
        StringBuffer		 query2  			= new StringBuffer();
        int                  kAbit              = -1;
        int                  summa              = -1;
        int                  oldBallAbt         = -1;
        boolean              only_one_run       = true;
        boolean              TselPriem_Ok       = false;
        boolean              header             = false;
        boolean              rez             = false;
        boolean              primechanie        = false;
        int                  nomer              = 0;
        int                  count_predm        = 4; // Только профильный предмет
        int counter=0;
        int countert=0;
        int ppl=0;
        int tp1=0;
        int tp2=0;
        int tp3=0;
        int tp4=0;
        int tp5=0;
        int ol=0;
        int ot1=0;
        int ot2=0;
        int ot3=0;
        int ot4=0;
        int ot5=0;
        int ns=0;
        int kl=0;
        String dt=new String();
        dt=StringUtil.CurrDate(".");
        //vremennie dannie
        String F=new String();
        String I=new String();
        String O=new String();
        String ko=new String();
        String op=new String();
        String ind=new String();
        int N=0;
        int summ=0;
        int total=0;
        int totalzach=0;
        int pptzach=0;
        String Shifr=new String();
        int pr1=0;
        int pr2=0;
        int pr3=0;
        int pr4=0;
        int pr5=0;
        int pr6=0;
        int pr7=0;
        int summt=0;
        String z=null;
        int lgn=0;
        int total_lgn=0;
        int idfak=0;
        int num=0;
        int sob=0;
        
        boolean flag=false;
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "waveSecondAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "waveSecondForm", form );

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
    	  stmt10 = conn.prepareStatement("DELETE FROM Forsen");
    	  stmt10.executeUpdate();
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/
    	  stmt10 = conn.prepareStatement("update abitdopinf set ballsoch='да' where ballsoch like 'Да'");
    	  stmt10.executeUpdate();
    	  stmt10 = conn.prepareStatement("update abitdopinf set ballatt='да' where ballatt like 'Да'");
    	  stmt10.executeUpdate();
    	  stmt10 = conn.prepareStatement("update abitdopinf set ballsgto='да' where ballsgto like 'Да'");
    	  stmt10.executeUpdate();
    	  stmt10 = conn.prepareStatement("update abitdopinf set ballzgto='да' where ballzgto like 'Да'");
    	  stmt10.executeUpdate();
    	  stmt10 = conn.prepareStatement("update abitdopinf set ballpoi='да' where ballpoi like 'Да'");
    	  stmt10.executeUpdate();
    	  stmt10 = conn.prepareStatement("update abitdopinf set trudovajadejatelnost='да' where trudovajadejatelnost like 'Да'");
    	  stmt10.executeUpdate();
    	  
    	  
    	  

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

  String name = "Список абитуриентов 1-го этапа "+AF+" "+priority+" ("+command+")";

  String file_con = "lists_"+StringUtil.toEng(AF)+"_sec_wave_"+StringUtil.toEng(command);

  if(priority_query.equals("%")) file_con += "_allPr";
  else file_con += priority_query;

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
 
  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

 // report.write("{\\rtf1\\ansi\n");
//  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");

  report.write("{\\rtf1\\ansi\n");
//report.write("\\landscape\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

  
  
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
      report.write("\\fs40\\qc{"+rs.getString(1)+"}\\par\\fs24{Данные сформированы по состоянию на: "+dt+"}\n");
  }

/**********************************************************/
/**                                                      **/
/**  Перебираем все специальности указанного факультета  **/
/**                                                      **/
/**********************************************************/

  stmt2 = conn.createStatement();
String forsen=null;
  query.delete(0,query.length());

  query.append("SELECT DISTINCT f.AbbreviaturaFakulteta,s.ShifrSpetsialnosti,s.Abbreviatura,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,s.PlanPriema,s.TselPr_PGU,s.TselPr_ROS,s.TselPr_1,s.TselPr_2,s.TselPr_3,s.PlanPriemaLg,s.planpriematech FROM Fakultety f,Spetsialnosti s, Konkurs kon, Abiturient a WHERE f.AbbreviaturaFakulteta NOT IN ('фзо','фозидо','мк','иито','инт','асп','орд') AND f.KodFakulteta=s.KodFakulteta AND a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti and s.edulevel in ('с','б') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki") || abit_SD.getPriznakSortirovki().equals("z_budgetniki")){
    query.append("AND s.PlanPriema NOT LIKE '0' ");
    forsen="('1','2','3','4')";
  }else{
	  forsen="('д')";
  }
  if(StringUtil.toInt(""+abit_SD.getKodFakulteta(),-1) != -1)
    query.append("AND s.KodFakulteta LIKE '"+abit_SD.getKodFakulteta()+"' ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
///      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
      else
///      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

///System.out.println("FAKS="+query.toString());
  rs2 = stmt2.executeQuery(query.toString());
  while(rs2.next()) {

// Для каждой специальности нумерация своя, свой список исключения и сбрасываются различные признаки
	  total=rs2.getInt(6);
	  tp1=rs2.getInt(7);
	  tp2=rs2.getInt(8);
	  tp3=rs2.getInt(9);
	  tp4=rs2.getInt(10);
	  tp5=rs2.getInt(11);
	  ppl=rs2.getInt(12);
	  int ppt=rs2.getInt(13);
	  
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
    else //if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
      BUD_DOG = "LIKE 'д'";
//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      BUD_DOG = "IS NULL";
//    else 
//      BUD_DOG = "LIKE 'д'";
 ll++;
 System.out.println(ll);
    stmt = conn.createStatement();
  // rs = stmt.executeQuery("SELECT COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon,zajavlennyeshkolnyeotsenki zso WHERE (ab.tipdoksredobraz like '%аттестат%' or (ab.tipdoksredobraz like '%диплом%' and ab.kodabiturienta=zso.kodabiturienta and zso.otsenkaege is not null)) AND kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach=sp.KodSpetsialnosti AND ab.KodSpetsialnZach LIKE '"+rs2.getString(5)+"' AND ab.Prinjat IN('1','2','3','4','д') AND kon.Dog_Ok "+BUD_DOG);
    rs=stmt.executeQuery("SELECT COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, zajavlennyeshkolnyeotsenki zso, ekzamenynaspetsialnosti ens WHERE zso.otsenkaege is not null and ens.kodspetsialnosti=ab.kodspetsialnzach and ens.kodpredmeta=zso.kodpredmeta and ens.prioritet=1 and ab.kodabiturienta=zso.kodabiturienta AND kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach=sp.KodSpetsialnosti AND ab.KodSpetsialnZach LIKE '"+rs2.getString(5)+"' AND ab.Prinjat IN"+forsen+" AND kon.Dog_Ok "+BUD_DOG);
    if(rs.next()){ 
    	totalzach=rs.getInt(1);
    	counter=total-totalzach;
    	if(counter<0){
    		counter=0;
    	}
    	ZACH_ABTs = rs.getString(1);
    }
    else{
    	counter=total;
    	totalzach=0;
    	ZACH_ABTs = "0";
    }
    stmt.close();
    rs.close();
    stmt = conn.createStatement();
    rs=stmt.executeQuery("SELECT COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, zajavlennyeshkolnyeotsenki zso, ekzamenynaspetsialnosti ens WHERE ab.viddoksredobraz like '%диплом%' and zso.otsenkaege=0 and ens.kodspetsialnosti=ab.kodspetsialnzach and ens.kodpredmeta=zso.kodpredmeta and ens.prioritet=1 and ab.kodabiturienta=zso.kodabiturienta AND kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach=sp.KodSpetsialnosti AND ab.KodSpetsialnZach LIKE '"+rs2.getString(5)+"' AND ab.Prinjat IN"+forsen+" AND kon.Dog_Ok "+BUD_DOG);
    if(rs.next()){ 
    	pptzach=rs.getInt(1);
    		
    			countert=ppt;
    		
    			countert=countert-pptzach;

    	ZACH_ABTsT = rs.getString(1);
    }
    else{
    	countert=ppt;
    	pptzach=0;
    	ZACH_ABTsT = "0";
    }
    if(countert>0){
    	if(counter>0){
    	counter=counter-countert;
    	}
    	if(counter<0){
    		counter=0;
    	}
    }else{
    	countert=0;
    }
// Определяем количество вакансий на спец-ти

    VP = ""+ (StringUtil.toInt(PP,0) - StringUtil.toInt(ZACH_ABTs,0));
   
    if(StringUtil.toInt(PP, 0) != 0) {
      if(abit_SD.getSpecial5() == null)
        report.write("\\fs24\\qc\\b1{План приёма: } "+PP+"\\b0\\par\n");
    }
    else {
      if(abit_SD.getSpecial5() == null)
        report.write("\\par");
    }

    if(abit_SD.getSpecial5() == null)
      report.write("\\fs28\\qc\\b1{Успешно прошедшие вступительные испытания}\\b0\\par\n");

   if(abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdtcmdep") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdepf"))) {
// размаркироватьвесьвуз - размаркировкавуза
     stmt3 = conn.createStatement();
     stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE Zach NOT IN ('д','о','н') AND KodAbiturienta NOT IN(SELECT KodAbiturienta FROM Abiturient WHERE Prinjat IN('1','2','3','4','д'))");
   }

/*************************************************************************/
/*** вычеркивание - вычитание - вычесть - вычеркнуть - вычеркнуть всех ***/
/*************************************************************************/
   if(abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("dsxthrbdfybt") || abit_SD.getSpecial5().equals("dsxbnfybt") || abit_SD.getSpecial5().equals("dsxtcnm") || abit_SD.getSpecial5().equals("dsxthryenm") || abit_SD.getSpecial5().equals("dsxthryenmdct["))) {

// Процедура вычеркивания из списков рекомендованных тех абитуриентов, которые прошли по приоритету № 1
/*
     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '1'  AND KodAbiturienta IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Prioritet IN('2') AND Zach LIKE 'р')");
     while(rs.next()) {
       stmt3 = conn.createStatement();
       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Zach LIKE 'р' AND Prioritet LIKE '2' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
     }
     
     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '1'  AND KodAbiturienta IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Prioritet IN('3') AND Zach LIKE 'р')");
     while(rs.next()) {
       stmt3 = conn.createStatement();
       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Zach LIKE 'р' AND Prioritet LIKE '3' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
     }

// Процедура вычеркивания из списков рекомендованных тех абитуриентов, которые прошли по приоритету № 2

     stmt = conn.createStatement();
     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '2'  AND KodAbiturienta IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Prioritet IN('3') AND Zach LIKE 'р')");
     while(rs.next()) {
       stmt3 = conn.createStatement();
       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Zach LIKE 'р' AND Prioritet LIKE '3' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
     }
*/
	   
	   stmt = conn.createStatement();
	     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '1'");
	     while(rs.next()) {
	       stmt3 = conn.createStatement();
	       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Prioritet in ('2','3','4','5') AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
	     }
	   
	     stmt = conn.createStatement();
	     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '2'");
	     while(rs.next()) {
	       stmt3 = conn.createStatement();
	       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Prioritet in ('3','4','5') AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
	     }
	   
	     stmt = conn.createStatement();
	     rs = stmt.executeQuery("SELECT KodAbiturienta FROM Konkurs WHERE Zach LIKE 'р' AND Prioritet LIKE '3'");
	     while(rs.next()) {
	       stmt3 = conn.createStatement();
	       stmt3.executeUpdate("UPDATE Konkurs SET Zach='в' WHERE Prioritet in ('4','5') AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
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


   stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
   stmt10.executeUpdate();
// ОЛИМПИЙЦЫ (без вступительных испытаний)

   
    

// По СУММЕ НАБРАННЫХ БАЛЛОВ
   flag=false;
    header = false;

    only_one_run = true;

    boolean evidence_exist = true;
    
    oldBallAbt = -1;

// В первом запросе объединения исключаются ВСЕ "Сурские таланты" и олимпиадники вообще

    query.delete(0,query.length());

    query.append("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'-' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
  //  query1= new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",pr.preemptiveright,'-',kon.NomerLichnogoDela,pr.pr FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon, PR pr WHERE pr.kodabiturienta=a.kodabiturienta AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+rs2.getString(5)+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+")");
  
    query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\" FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, lgoty l,TselevojPriem tp, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon,abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    
    
    //  query1.append("AND kon.Prioritet LIKE '"+priority_query+"' ");
    
// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");
    query1.append(" AND (a.Prinjat NOT IN ('1','2','3','4','5','7','д','о') OR a.Prinjat IS NULL) ");
    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");
    query1.append(" AND (kon.Zach NOT IN('в','о','н','д') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL) ");
  //AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')
    // query1.append("AND (kon.Stob IS NOT NULL AND ");
    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");
   // query1.append("AND kon.Prioritet LIKE '"+priority_query+"' ");
    query.append("AND m.ShifrMedali NOT IN ('о','к') ");

    if(abit_SD.getSpecial2().equals("orig")){
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
      query1.append("AND a.TipDokSredObraz LIKE ('о') ");
    }
    else if(abit_SD.getSpecial2().equals("copy")){
      query.append("AND a.TipDokSredObraz LIKE ('к') ");
      query1.append("AND a.TipDokSredObraz LIKE ('к') ");
    }

    if(abit_SD.getSpecial3().equals("rek")){
      query.append("AND a.Prinjat LIKE ('р') ");
      query1.append("AND a.Prinjat LIKE ('р') ");
    }

    if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
///      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
      query1.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
    
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
    }else{
///      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
      query1.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
    }
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");
  
    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");
 //   query1.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,pr.preemptiveright,kon.NomerLichnogoDela,pr.pr");
 //   query1.append(" ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,pr.preemptiveright,pr.pr");
    query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot");
    
// Подсоединяем Сурские таланты и др. Олимпиадников с баллом по математике == 100 (ИСКУСТВЕННО, согласно правилам приема (хотя в БД не 100))

    query.append(" UNION SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,100+SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'(о)' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

    query.append("AND (m.ShifrMedali IN ('о','к') AND np.Sokr NOT LIKE ('Мат') AND s.ShifrSpetsialnosti IN("+Constants.Sur_Talants_100B_MAT+"))");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
///      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
    else
///      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");


// Подсоединяем Сурские таланты и др. Олимпиадников с их РЕАЛЬНЫМИ БАЛЛАМИ для СПЕЦИАЛЬНОСТЕЙ НЕ ИЗ ЛЬГОТНОГО СПИСКА

    query.append(" UNION SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'-' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

// ИСКЛЮЧАЮТСЯ УЖЕ ЗАЧИСЛЕННЫЕ (целевики)

    query.append(" AND (a.Prinjat NOT IN ('1','2','3','4','д','о') OR a.Prinjat IS NULL) ");

    query.append(" AND a.KodAbiturienta NOT IN(SELECT DISTINCT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND (kon.Zach NOT IN('в','о','н','д') OR kon.Zach LIKE 'р' OR kon.Zach IS NULL)");

    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

    query.append("AND (m.ShifrMedali IN ('о','к') AND s.ShifrSpetsialnosti NOT IN("+Constants.Sur_Talants_100B_MAT+"))");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
///      query.append("AND (kon.Bud LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
    else
///      query.append("AND (kon.Dog LIKE 'д')");
      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('з','у','ф') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");
//      query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");

    query.append(" ORDER BY SummaEge DESC,a.TipDokSredObraz DESC,m.ShifrMedali ASC,a.Familija,a.Imja,a.Otchestvo");

//System.out.println(query);

    stmt9 = conn.createStatement();

    rs9 = stmt9.executeQuery(query1.toString());
    while(rs9.next()) {
    	stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
    	N++;
        stmt10.setObject(1, N,Types.INTEGER);
        stmt10.setObject(2, rs9.getString(2),Types.VARCHAR);
        stmt10.setObject(3, rs9.getString(3),Types.VARCHAR);
        stmt10.setObject(4, rs9.getString(4),Types.VARCHAR);
        stmt10.setObject(5, rs9.getString(5),Types.VARCHAR);
        stmt10.setObject(6, rs9.getString(6),Types.VARCHAR);
        stmt10.setObject(7, rs9.getString(7),Types.VARCHAR);
        if(rs9.getString(8).equals("1")){
        stmt10.setObject(8, "да",Types.VARCHAR);
        }else{
        stmt10.setObject(8, "нет",Types.VARCHAR);
        }
        stmt10.setObject(9, rs9.getString(10),Types.VARCHAR);
        stmt10.setObject(10, new Integer(rs9.getString(1)),Types.INTEGER);
        stmt10.setObject(11, rs9.getString(9),Types.VARCHAR);
        stmt10.executeUpdate();
    }
    
    stmt10=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
   /* stmt10=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '4'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr4=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }*/
    stmt10=conn.prepareStatement("SELECT DISTINCT k.prof,p.ka FROM Forsen p, konkurs k WHERE p.ka=k.kodabiturienta AND k.kodspetsialnosti LIKE "+rs2.getString(5)+"");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr4=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT kon.pr1,kon.pr2,kon.pr3,kon.Stob,p.ka FROM Forsen p, abiturient a,konkurs kon WHERE p.ka=kon.kodabiturienta AND kon.kodspetsialnosti LIKE "+rs2.getString(5)+" ");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET p1=?, p2=?,p3=?,Stob=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.setObject(3, rs10.getString(3),Types.INTEGER); 
    	stmt11.setObject(4, rs10.getString(4),Types.INTEGER); 
    	stmt11.setObject(5, rs10.getString(5),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    
    stmt10=conn.prepareStatement("SELECT p.p1,p.p2,p.p3,p.Stob,p.ka FROM Forsen p ");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	int kk=rs10.getInt(4);
    	if(rs10.getInt(1)==1 && kk==1){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=100 WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(5),Types.INTEGER); 
    	stmt11.executeUpdate();
    	}
    	if(rs10.getInt(2)==1 && kk==1){
        	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=100 WHERE ka=?");
        	stmt11.setObject(1, rs10.getString(5),Types.INTEGER); 
        	stmt11.executeUpdate();
        	}
    	if(rs10.getInt(3)==1 && kk==1){
        	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=100 WHERE ka=?");
        	stmt11.setObject(1, rs10.getString(5),Types.INTEGER); 
        	stmt11.executeUpdate();
        	}
    	
    }
    
    stmt10=conn.prepareStatement("SELECT pr1,pr2,pr3,pr4,ka FROM Forsen");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	pr1=rs10.getInt(1);
        pr2=rs10.getInt(2);
        pr3=rs10.getInt(3);
        pr4=rs10.getInt(4);
        summ=pr1+pr2+pr3+pr4;
        stmt11=conn.prepareStatement("UPDATE Forsen SET sum=? WHERE ka=?");
        stmt11.setObject(1, summ,Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(5),Types.INTEGER); 
    	stmt11.executeUpdate();
    }

    stmt10=conn.prepareStatement("SELECT kon.kodabiturienta,adi.BallAtt,adi.BallSGTO,adi.BALLZGTO,adi.BallPOI,adi.BallSoch,adi.TrudovajaDejatelnost,kon.olimp,f.sum from abitdopinf adi, konkurs kon, forsen f WHERE f.ka=kon.kodabiturienta AND adi.kodabiturienta=kon.kodabiturienta AND  kon.kodspetsialnosti LIKE "+rs2.getString(5)+" ");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	kAbit=rs10.getInt(1);
    	System.out.println(kAbit);
    	pr1=0;
    	pr2=0;
    	pr3=0;
    	pr4=0;
    	pr5=0;
    	pr6=0;
    	pr7=0;
    	if(!rs10.getString(2).equals("да")&&!rs10.getString(2).equals("нет")){
    	pr1=Integer.parseInt(rs10.getString(2));
    	}
    	if(!rs10.getString(3).equals("да")&&!rs10.getString(3).equals("нет")){
    	pr2=Integer.parseInt(rs10.getString(3));
    	}
    	if(!rs10.getString(4).equals("да")&&!rs10.getString(4).equals("нет")){
    	pr3=Integer.parseInt(rs10.getString(4));
    	}
    	if(!rs10.getString(5).equals("да")&&!rs10.getString(5).equals("нет")){
    	pr4=Integer.parseInt(rs10.getString(5));
    	}
    	if(!rs10.getString(6).equals("да")&&!rs10.getString(6).equals("нет")){
    	pr5=Integer.parseInt(rs10.getString(6));
    	}
    	if(!rs10.getString(7).equals("да")&&!rs10.getString(7).equals("нет")){
    	pr6=Integer.parseInt(rs10.getString(7));
    	}
    	if(!rs10.getString(8).equals("да")&&!rs10.getString(8).equals("нет")){
    	pr7=Integer.parseInt(rs10.getString(8));
    	}
    	summt=pr1+pr2+pr3+pr4+pr5+pr6+pr7;
    	stmt11=conn.prepareStatement("UPDATE Forsen SET ind=?, ind1=?, ind2=?, ind3=?, ind4=?, ind5=?, sum=? WHERE ka=?");
    	stmt11.setObject(1, summt,Types.INTEGER); 
    	stmt11.setObject(2, pr1,Types.INTEGER); 
    	stmt11.setObject(3, pr2+pr3+pr4,Types.INTEGER);
    	stmt11.setObject(4, pr6,Types.INTEGER);
    	stmt11.setObject(5, pr7,Types.INTEGER);
    	stmt11.setObject(6, pr5,Types.INTEGER);
    	stmt11.setObject(7, rs10.getInt(9)+summt,Types.INTEGER);
    	stmt11.setObject(8, rs10.getString(1),Types.INTEGER);
    	
    	
    	stmt11.executeUpdate();
    }
    
    
    
    
    
    
    lgn=0;
 //   stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,ind,s,ka,nld,pp,p1,p2,p3,Stob FROM Perelom ORDER BY sum DESC, pr1 DESC, pr2 DESC, pr3 DESC, pr4 DESC, pp DESC");
//    rs10=stmt10.executeQuery();
//    stmt = conn.createStatement();
//    rs = stmt.executeQuery(query.toString());
   // while(rs.next()) {
    
    stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,p1,p2,p3,Stob FROM Forsen WHERE (vd like '%аттестат%' or (vd like '%диплом%' and pr1>0 and pr2>0)) ORDER BY sum DESC, pr1 DESC, pr2 DESC, pr3 DESC, pr4 DESC ");
    rs10=stmt10.executeQuery();
    
    
    
    while(rs10.next()){
    	flag=false;
      if(abit_SD.getSpecial5() == null && header == false ) {

        report.write("\\pard\\par\n");

        if( !header )
        	if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
          report.write("\\b1\\ql\\fs28{Перечень лиц, рекомендованных к зачислению и поступающих на базе среднего общего образования по общему конкурсу на места в рамках контрольных цифр приема }\\b0\\par\\par\n");
       }else{
    	   report.write("\\b1\\ql\\fs28{Перечень лиц, рекомендованных к зачислению и поступающих на базе среднего общего образования, подавших документы по договору об оказании платных образовательных услуг }\\b0\\par\\par\n");
        }
        header = true;

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//shifr
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//ekz1
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//ekz2
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ekz3
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//ekz4
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//op
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//ind
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//att
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//spd
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//trud
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700\n");//olimp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13400\n");//soch


        report.write("\\intbl\\qc{№}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Копия-оригинал}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl{Первый}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Второй}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Третий}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Четвёртый}\\par{экзамен}\\cell\n");
        report.write("\\intbl{Особые}\\par{права}\\cell\n");
        report.write("\\intbl{Преим.пр.}\\cell\n");
        report.write("\\intbl{Сумма}\\par{инд.}\\par{дост.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Сп.дост.}\\cell\n");
        report.write("\\intbl{Вол.деят.}\\cell\n");
        report.write("\\intbl{Олимп.}\\cell\n");
        report.write("\\intbl{Соч.}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//shifr
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//ekz1
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//ekz2
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ekz3
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//ekz4
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//op
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//ind
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//att
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//spd
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//trud
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700\n");//olimp
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13400\n");//soch
  
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        
        kl=0;
        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT np.Sokr,ens.prioritet FROM NazvanijaPredmetov np, ekzamenynaspetsialnosti ens, spetsialnosti s WHERE np.kodpredmeta=ens.kodpredmeta AND s.kodspetsialnosti=ens.kodspetsialnosti AND s.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' ORDER BY ens.prioritet ASC");
        while(rs3.next()) {
        	kl++;
        	report.write("\\intbl "+rs3.getString(1)+" \\cell\n");
        }
        if(kl!=4)
        {report.write("\\intbl\\cell\n");}

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");
        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
     //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//shifr
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//ekz1
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//ekz2
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ekz3
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//ekz4
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//op
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//pp
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//ind
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//att
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//spd
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//trud
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700\n");//olimp
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13400\n");//soch
       }
      
      if(abit_SD.getSpecial5() == null) {
      if(counter!=0 && rs10.getString(5).substring(0,1).equals("о")){
    	  report.write("\\b1\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    	     //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//shifr
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//ekz1
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//ekz2
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ekz3
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//ekz4
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//op
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//pp
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//ind
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//att
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//spd
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//trud
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700\n");//olimp
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13400\n");//soch
    	       counter--;
      }
      }
      
      
      
      
      
      
      
      
      if(abit_SD.getSpecial5() == null) {
//        report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");
        
//System.out.println("PlanPriema="+PP+" Nomer="+nomer+" oldBall="+oldBallAbt+" CurrBall="+rs.getString(8));
        
        lgn++;
             
        nomer++;
    	//  }
        report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
        report.write("\\intbl\\qc "+rs10.getString(1)+"\\cell\n"); // NLD   
        report.write("\\intbl\\ql "+rs10.getString(2)+" "+rs10.getString(3).substring(0,1)+"."+rs10.getString(4).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
        report.write("\\intbl\\qc "+rs10.getString(5)+"\\cell\n"); //ko
        report.write("\\intbl\\qc "+rs10.getString(6)+"\\cell\n"); // sum
        report.write("\\intbl\\qc "+rs10.getString(7)+"\\cell\n"); //ekz1
        report.write("\\intbl\\qc "+rs10.getString(8)+"\\cell\n"); //ekz2
        report.write("\\intbl\\qc "+rs10.getString(9)+"\\cell\n"); //ekz3
        if(z==rs10.getString(10)){
        	  report.write("\\intbl\\qc{0}\\cell\n");
        	
        }else{
        report.write("\\intbl\\qc "+rs10.getString(10)+"\\cell\n");
        }
        report.write("\\intbl\\qc "+rs10.getString(11)+"\\cell\n"); //op
        report.write("\\intbl\\qc "+rs10.getString(12)+"\\cell\n"); //pp
        report.write("\\intbl\\qc "+rs10.getString(14)+"\\cell\n"); //ind
        report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
        
        
        
                       // SUMMA Ege
        report.write("\\intbl\\b0\\row\n");
        N++;
      }
// Подсчитываем общее количество абитуриентов для разбиения на рекомендованных к зачислению и резерв
// Если общее кол-во абитуриентов на специальность больше разности плана приема и кол-ва зачисленных, то оставшихся выводим в табличку резерва

      total_abits++;
System.out.println("total_abts="+total_abits+" PP="+PP+" ZACH="+ZACH_ABTs+" rezerv="+rezerv);
		if(counter!=0 && rs10.getString(5).substring(0,1).equals("о") && abit_SD.getSpecial5() != null){
				flag=true;
					counter--;
			}

     // if(total_abits > (StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0))) { 
	if (counter==0){
        overload = true;
 
        rezerv += 1;
      }
	if(rezerv>1 && abit_SD.getSpecial5() != null &&  (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))){
		break;
	}
// Установка признака "Рекомендован к зачислению" - "р"

System.out.println("preMARKING_!!! Sp="+rs2.getString(5)+" Abit="+rs10.getString(13)+" rezerv="+rezerv+" special5="+abit_SD.getSpecial5());
      if(rezerv <= 1 && flag==true && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) {
// маркировать - маркировка
System.out.println("MARKING_!!! Sp="+rs2.getString(5)+" Abit="+rs10.getString(13));
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach='р' WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs10.getString(13)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && flag==true && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) {
// размаркировать - размаркировка
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs10.getString(13)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
      } else if(rezerv <= 1 && flag==true && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) {
// размаркироватьвсех - размаркировкавсех
        stmt3 = conn.createStatement();
        stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs10.getString(13)+"'");
      }

    }
    
    if(header && abit_SD.getSpecial5() == null) report.write("\\pard\n");
    rezerv=0;
    header=false;
    flag=false;
    nomer=0;
    stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,p1,p2,p3,Stob FROM Forsen WHERE vd like '%диплом%' and pr1=0 and pr2=0 ORDER BY sum DESC, pr1 DESC, pr2 DESC, pr3 DESC, pr4 DESC ");
    rs10=stmt10.executeQuery();
    
    
    while(rs10.next()){
    	flag=false;
    if(abit_SD.getSpecial5() == null && header == false ) {

        report.write("\\pard\\par\n");

        if( !header )
        	if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
          report.write("\\b1\\ql\\fs28{Перечень лиц, рекомендованных к зачислению и поступающих на базе профессионального образования по общему конкурсу на места в рамках контрольных цифр приема }\\b0\\par\\par\n");
       }else{
    	   report.write("\\b1\\ql\\fs28{Перечень лиц, рекомендованных к зачислению и поступающих на базе профессионального образования, подавших документы по договору об оказании платных образовательных услуг }\\b0\\par\\par\n");
        }
        header = true;
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//shifr
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum

        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//op
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ind
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//att
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//spd
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//trud
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//olimp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//soch
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//soch

        report.write("\\intbl\\qc{№}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия И.О.}\\cell\n");
        report.write("\\intbl{Копия-оригинал}\\cell\n");
        report.write("\\intbl{Сумма}\\cell\n");
        report.write("\\intbl{Соб.}\\cell\n");
        report.write("\\intbl{Особые}\\par{права}\\cell\n");
        report.write("\\intbl{Преим.пр.}\\cell\n");
        report.write("\\intbl{Сумма}\\par{инд.}\\par{дост.}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Сп.дост.}\\cell\n");
        report.write("\\intbl{Вол.деят.}\\cell\n");
        report.write("\\intbl{Олимп.}\\cell\n");
        report.write("\\intbl{Соч.}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
     //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//shifr
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum

        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//op
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//pp
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ind
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//att
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//spd
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//trud
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//olimp
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//soch
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//soch
    }
    if(abit_SD.getSpecial5() == null){
    if(countert!=0 && rs10.getString(5).substring(0,1).equals("о")){
    	report.write("\\b1\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
      //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//shifr
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum

           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//op
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//pp
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ind
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//att
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//spd
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//trud
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//olimp
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//soch
           report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//soch
           countert--;
          
    }
    }
    
    if(abit_SD.getSpecial5() == null) {
    	nomer++;
    	  report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
          report.write("\\intbl\\qc "+rs10.getString(1)+"\\cell\n"); // NLD   
          report.write("\\intbl\\ql "+rs10.getString(2)+" "+rs10.getString(3).substring(0,1)+"."+rs10.getString(4).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
          report.write("\\intbl\\qc "+rs10.getString(5)+"\\cell\n"); //ko
         
          report.write("\\intbl\\qc "+rs10.getString(6)+"\\cell\n"); // sum
          report.write("\\intbl\\qc "+rs10.getString(10)+"\\cell\n"); // sum
          report.write("\\intbl\\qc "+rs10.getString(11)+"\\cell\n"); //op
          report.write("\\intbl\\qc "+rs10.getString(12)+"\\cell\n"); //pp
          report.write("\\intbl\\qc "+rs10.getString(14)+"\\cell\n"); //ind
       //   report.write("\\intbl\\qc "+(summ)+"\\cell\n");
        report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
          report.write("\\intbl\\b0\\row\n");
    }
    
    total_abits++;
    System.out.println("total_abts="+total_abits+" PP="+PP+" ZACH="+ZACH_ABTs+" rezerv="+rezerv);
    if(countert!=0 && rs10.getString(5).substring(0,1).equals("о") && abit_SD.getSpecial5() != null){
		flag=true;
			countert--;
	}

         // if(total_abits > (StringUtil.toInt(PP,0)-StringUtil.toInt(ZACH_ABTs,0))) { 
    	if (countert==0){
     
            rezerv += 1;
          }
    	if(rezerv>1 && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))){
    		break;
    	}
    // Установка признака "Рекомендован к зачислению" - "р"

    System.out.println("preMARKING_!!! Sp="+rs2.getString(5)+" Abit="+rs10.getString(13)+" rezerv="+rezerv+" special5="+abit_SD.getSpecial5());
          if(rezerv <= 1 && flag==true && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("vfhrbhjdrf") || abit_SD.getSpecial5().equals("vfhrbhjdfnm"))) {
    // маркировать - маркировка
    System.out.println("MARKING_!!! Sp="+rs2.getString(5)+" Abit="+rs10.getString(13));
            stmt3 = conn.createStatement();
            stmt3.executeUpdate("UPDATE Konkurs SET Zach='р' WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs10.getString(13)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
          } else if(rezerv <= 1 && flag==true && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnm") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrf"))) {
    // размаркировать - размаркировка
            stmt3 = conn.createStatement();
            stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs10.getString(13)+"' AND (Zach NOT IN ('в','о','н') OR Zach IS NULL)");
          } else if(rezerv <= 1 && flag==true && abit_SD.getSpecial5() != null && (abit_SD.getSpecial5().equals("hfpvfhrbhjdfnmdct[") || abit_SD.getSpecial5().equals("hfpvfhrbhjdrfdct["))) {
    // размаркироватьвсех - размаркировкавсех
            stmt3 = conn.createStatement();
            stmt3.executeUpdate("UPDATE Konkurs SET Zach=NULL WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND KodAbiturienta LIKE '"+rs10.getString(13)+"'");
          }
    
    
    }
    stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
    stmt10.executeUpdate();

    if(header && abit_SD.getSpecial5() == null) report.write("\\pard\n");

    if(primechanie && abit_SD.getSpecial5() == null) 
      report.write("\\par\\fs24\\ql\\tab\\tab{* - балл установлен согласно п.10.4. правил приёма ПГУ}\\par\n");


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