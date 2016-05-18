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

public class WaveFirstAction extends Action {

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
        Statement stmt9=null;
        PreparedStatement stmt10=null;
        PreparedStatement stmt11=null;
        ResultSet rs9=null;
        ResultSet rs10=null;
        ResultSet rs11=null;
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
        String               AS                 = new String();           // аббревиатура специальности
        String               SS                 = new String();           // шифр
        String               NS                 = new String();           // название специальности
        String               PP                 = new String();           // план приема
        String               TP1                = new String();           // план целевого приема 1
        String               TP2                = new String();           // план целевого приема 2
        String               AF                 = new String();           // аббревиатура факультета
        StringBuffer         excludeList        = new StringBuffer("-1");
        StringBuffer         query              = new StringBuffer();
        StringBuffer		 query1  			= new StringBuffer();
        StringBuffer		 query2  			= new StringBuffer();
        int                  kAbit              = -1;
        int                  summa              = -1;
        int                  oldBallAbt         = -1;
        boolean              only_one_run       = true;
        boolean              header             = false;
        boolean              primechanie        = false;
        int                  nomer              = 0;
        int                  count_predm        = 4; // Только профильный предмет
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
        String spis=new String();
        int N=0;
        int summ=0;
        String Shifr=new String();
        int pr1=0;
        int pr2=0;
        int pr3=0;
        int pr4=0;
        int pr5=0;
        int pr6=0;
        int pr7=0;
        
        String z=null;
        int lgn=0;
        int total_lgn=0;
        int idfak=0;
        int num=0;
        int summt=0;
        int sob=0;
        String KF=new String();
        
        

        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "waveFirstAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "waveFirstForm", form );

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
    	  stmt10 = conn.prepareStatement("UPDATE Konkurs set sogl='н' where sogl is null");
    	  stmt10.executeUpdate();
    	  
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
    	  
    	  
    	  
    	 if(abit_SD.getSpecial7().equals("2")){
    	  spis="f ASC, i ASC, o ASC";
    	  }else{
    		  spis="sum DESC, pr1 DESC, pr2 DESC, pr3 DESC, pr4 DESC";
    	  }
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/
    	 if(abit_SD.getKodFakulteta()==0){
    			KF="%";
    		}else{
    			KF=abit_SD.getKodFakulteta().toString();
    		}
  String priority = new String();

  String priority_query = new String();

// ИСХОДНЫЕ ПАРАМЕТРЫ

  abit_SD.setSpecial2("-");

  abit_SD.setSpecial3("-");

  priority_query = "%";

  if(!(abit_SD.getSpecial4() != null && abit_SD.getSpecial4().length() > 1 )) { 
    priority = abit_SD.getSpecial4()+"-го приоритета";
    priority_query = abit_SD.getSpecial4();
  }
  if(abit_SD.getKodFakulteta()!=0){
  pstmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ?");
  pstmt.setObject(1,abit_SD.getKodFakulteta(),Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()) {
    AF = rs.getString(1).toUpperCase();
  }
  if(AF.equals("ФМС")){
	  AF="ИМС";
  }
  }else{
		AF = "Все";
	}
  
  String name = "Список абитуриентов 1-го этапа (предв.) "+AF+" "+priority;

  String file_con = "lists_"+StringUtil.toEng(AF)+"_predv_first_wave";

  if(priority_query.equals("%")) file_con += "_allPr";
  else file_con += priority_query;

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
 
  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 
//\\paperw11906\\paperh16838
  report.write("{\\rtf1\\ansi\n");
//  report.write("\\landscape\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  pstmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs40 \\qc "+rs.getString(1)+"\n");

/**********************************************************/
/**                                                      **/
/**  Перебираем все специальности указанного факультета  **/
/**                                                      **/
/**********************************************************/

  stmt2 = conn.createStatement();

  query = new StringBuffer("SELECT DISTINCT f.AbbreviaturaFakulteta,s.ShifrSpetsialnosti,s.Abbreviatura,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,s.PlanPriema,s.TselPr_PGU,s.TselPr_ROS,s.PlanPriemaDog,f.kodfakulteta FROM Fakultety f,Spetsialnosti s, Konkurs kon, Abiturient a WHERE f.KodFakulteta=s.KodFakulteta AND a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE '"+KF+"' AND s.edulevel IN ('с','б')");
  
    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
      query.append("AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");
//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
    else
      query.append("AND (kon.Dog LIKE 'д')");
//      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
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
    query.append(" ORDER BY f.KodFakulteta,s.KodSpetsialnosti");
  rs2 = stmt2.executeQuery(query.toString());
  while(rs2.next()) {

// Для каждой специальности нумерация своя и свой список исключения

    nomer = 0;
    excludeList = new StringBuffer("-1");

    SS = rs2.getString(2);
    AS = rs2.getString(3);
    NS = rs2.getString(4).toUpperCase();
    if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
    if(StringUtil.toInt(rs2.getString(6), 0) == 0) PP = "0";
    else PP  = "" + StringUtil.toInt(rs2.getString(6), 0);
    }else{
    	if(StringUtil.toInt(rs2.getString(9), 0) == 0) PP = "0";
        else PP  = "" + StringUtil.toInt(rs2.getString(9), 0);
    	total_lgn=rs2.getInt(9);
    }
    TP1 = "" + StringUtil.toInt(rs2.getString(7), 0);
    TP2 = "" + StringUtil.toInt(rs2.getString(8), 0);

    report.write("\\fs24\\par\\par\n");
    report.write("\\fs26\\b0\\qc{Список абитуриентов }\\b1{"+rs2.getString(1)+"}\\b0\\par{поступающих на специальность (направление):}\\par\\fs24\\b1{"+SS+" }\\'ab{"+NS+"}\\'bb\\qc{ ("+AS+")}\\b0\\par\n");
    report.write("\\par\\fs26\\qc\\b0{Данные сформированы на: } "+dt+"\\b0\\par\n");
    if(StringUtil.toInt(PP, 0) != 0)
      report.write("\\par\\fs26\\qc\\b1{План приёма: } "+PP+"{  Вакансии: } "+PP+"\\b0\\par\n");
    else
      report.write("\\par");



/******************************************************/
/**********  ЗАПРОСЫ НА ВЫБОРКУ АБИТУРИЕНТОВ **********/
/******************************************************/

    if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
    	
// ОЛИМПИЙЦЫ (без вступительных испытаний)

    header = false;

    query = new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+rs2.getString(5)+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    //query1= new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",l.ShifrLgot,kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+rs2.getString(5)+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+")");
    query1= new StringBuffer("SELECT a.KodAbiturienta,kon.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\",kon.sogl FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon, Lgoty l, abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+rs2.getString(5)+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    
    query1.append(" AND kon.Prioritet LIKE '"+priority_query+"' ");
  //  query1.append("AND kon.Prioritet LIKE '"+priority_query+"' ");
    //query1.append("AND kon.NomerLichnogoDela LIKE '%-1' ");
    
//Мешали спец-ти СурТал    query.append(" AND (m.ShifrMedali IN ('о','к') AND (s.ShifrSpetsialnosti IN ("+Constants.Sur_Talants_NO_KONKURS+"))) ");
    query.append(" AND (m.shifrmedali IN ('о','к')) ");
    query1.append(" AND (l.shifrlgot IN ('о')) ");

   if(abit_SD.getSpecial2().equals("orig")){
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    query1.append(" AND a.TipDokSredObraz LIKE ('о') ");
    }
    else if(abit_SD.getSpecial2().equals("copy")){
      query.append("AND a.TipDokSredObraz LIKE ('к') ");
      query1.append("AND a.TipDokSredObraz LIKE ('к') "); 
    }
   // query1.append("AND kon.Pr1=1 "); 
 //   query1.append("AND kon.Stob IS NULL "); 
    if(abit_SD.getSpecial3().equals("rek")){
      query.append("AND a.Prinjat LIKE ('р') ");
    query1.append("AND a.Prinjat LIKE ('р') ");
    }

    if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
      query.append("AND (kon.Bud LIKE 'д')");
    query1.append("AND (kon.Bud LIKE 'д')");
    }
//      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");
    else{
      query.append("AND (kon.Dog LIKE 'д')");
    query1.append("AND (kon.Dog LIKE 'д')");
    }
   
//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
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

    query.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");
    query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.sogl ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot");
//System.out.println(query.toString());
String s=null;
    stmt9 = conn.createStatement();

    rs9 = stmt9.executeQuery(query1.toString());
    while(rs9.next()) {
    	stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd,sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
		stmt10.setObject(12, rs9.getString(11),Types.VARCHAR);
        stmt10.executeUpdate();
    }
    
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end ,p.ka  FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER);  
    	stmt11.executeUpdate();
    }

    
    stmt10=conn.prepareStatement("SELECT DISTINCT k.prof,p.ka FROM Forsen p, konkurs k WHERE p.ka=k.kodabiturienta AND k.kodspetsialnosti LIKE "+rs2.getString(5)+"");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr4=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
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
    	
    	kAbit=rs10.getInt(1);
    	
    	stmt11.executeUpdate();
    	
    }
    
    
  
    
    
    
    stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,sogl FROM Forsen ORDER BY "+spis+"");
    rs10=stmt10.executeQuery();
   N=1;
   // stmt = conn.createStatement();
  //  rs = stmt.executeQuery(query.toString());
    while(rs10.next()) {

      if(header == false) {
        header = true;
        report.write("\\pard\\par\n");
        report.write("\\b1\\ql\\fs28{Перечень лиц, имеющих право на прием без вступительных испытаний, поступающих на базе среднего общего образования}\\b0\\par\\par\n"); 
    //    report.write("\\fs16 \\b0 \\qc \\trowd \\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
		report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
        
        
        
  

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
		report.write("\\intbl{Согл.}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
		report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
  
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
		report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");
        
        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
     //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
		report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
      }
      nomer++;
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
   //   report.write("\\intbl\\qc "+(summ)+"\\cell\n");
    report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
	report.write("\\intbl\\qc "+rs10.getString(20)+"\\cell\n"); // 
      report.write("\\intbl\\row\n");
      N++;
//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
      excludeList.append(","+rs10.getString(13));
    }


    if(header) report.write("\\pard\\par\n");
	stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
    stmt10.executeUpdate();



nomer=0;
    header = false;
// ЛЬГОТНИКИ (только инвалиды и сироты) 
    lgn=0;
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT planPriemaLg FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"'");
    if( rs.next() && StringUtil.toInt(rs.getString(1),0) != 0 ) {
    	total_lgn=rs.getInt(1);
    
   

    query = new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND l.ShifrLgot IN ('и','с') AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    query1= new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\",kon.sogl  FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon, abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND l.ShifrLgot IN ('и','с','ик','ск') AND a.DokumentyHranjatsja LIKE 'д' AND kon.NomerLichnogoDela LIKE '%-1' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");
    query1.append("AND kon.Prioritet LIKE '"+priority_query+"' ");
    query1.append("AND kon.NomerLichnogoDela LIKE '%-1' ");

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
      query.append("AND (kon.Bud LIKE 'д')");
      query1.append("AND (kon.Bud LIKE 'д')");
    }
//      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
      else{
      query.append("AND (kon.Dog LIKE 'д')");
      query1.append("AND (kon.Dog LIKE 'д')");
      }
//      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
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

    query.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");
    query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,l.shifrlgot,kon.PR,a.viddoksredobraz,kon.sogl ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot");
    stmt9 = conn.createStatement();
N=0;
rs9 = stmt9.executeQuery(query1.toString());
while(rs9.next()) {
	stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd,sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
	stmt10.setObject(12, rs9.getString(11),Types.VARCHAR);
    stmt10.executeUpdate();
}

stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end ,p.ka  FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER);  
    	stmt11.executeUpdate();
    }

    
    stmt10=conn.prepareStatement("SELECT DISTINCT k.prof,p.ka FROM Forsen p, konkurs k WHERE p.ka=k.kodabiturienta AND k.kodspetsialnosti LIKE "+rs2.getString(5)+"");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr4=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
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
	
	kAbit=rs10.getInt(1);

	stmt11.executeUpdate();
	
}









stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,sogl FROM Forsen ORDER BY "+spis+"");
rs10=stmt10.executeQuery();
    N=1;
   // stmt = conn.createStatement();
  //  rs = stmt.executeQuery(query.toString());
    
    while(rs10.next()) {
      if(header == false) {
    	  header = true;
          report.write("\\pard\\par\n");
          report.write("\\b1\\ql\\fs28{Перечень лиц, поступающих на базе среднего общего образования, имеющих особые права и поступающих на места в пределах квоты приема: "+total_lgn+"}\\b0\\par\\par\n"); 
      //    report.write("\\fs16 \\b0 \\qc \\trowd \\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
          report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
          report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
     //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
          report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
           report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
 

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
		  report.write("\\intbl{Согл.}\\cell\n");
          report.write("\\intbl\\row\n");

          report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
     //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");//fio
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4300\n");//ko
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");//sum
          report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5700\n");//ekz1
          report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6400\n");//ekz2
          report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");//ekz3
          report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");//ekz4
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//op
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9200\n");//pp
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9900\n");//ind
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10600\n");//att
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11300\n");//spd
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//trud
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12700\n");//olimp
          report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13400\n");//soch
		  report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
    
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
		  report.write("\\intbl\\cell\n");
          report.write("\\intbl \\row\n");
          
          report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
       //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
          report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
     //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
          report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
		  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
          num=nomer+total_lgn;
        }
   
     
  //  	  if(nomer<num){
      nomer++;
 //   	  }
      lgn++;
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
   //   report.write("\\intbl\\qc "+(summ)+"\\cell\n");
    report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
    report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
	report.write("\\intbl\\qc "+rs10.getString(20)+"\\cell\n"); // 
      report.write("\\intbl\\row\n");
      N++;
//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
      rs9 = stmt9.executeQuery("select a.kodabiturienta from abiturient a, konkurs k where k.target NOT LIKE '1' AND a.kodabiturienta=k.kodabiturienta AND k.kodspetsialnosti LIKE '"+rs2.getString(5)+"' AND a.kodabiturienta LIKE '"+rs10.getString(13)+"'");
      if(!rs9.next()){
      excludeList.append(","+rs10.getString(13));
      }
    }
      if(header) report.write("\\pard\\par\n");
     
    }
//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
    stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
    stmt10.executeUpdate();
    
    if(header) report.write("\\pard\\par\n");





// ЦЕЛЕВОЙ ПРИЕМ ( Уч. совет ПГУ )


    header = false;
    nomer=0;
    int total_amount = 0, tselev_nomer = 0;

    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT TselPr_PGU FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"'");
    if( rs.next() && StringUtil.toInt(rs.getString(1),0) != 0 ) {
    	  num=nomer+rs.getInt(1);
      

// Абитуриенты-целевики

      query = new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND tp.ShifrPriema IN ('ц','ф') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
      query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\",kon.sogl FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, lgoty l,TselevojPriem tp, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon,abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=kon.target AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND tp.ShifrPriema IN ('цел','ф') AND a.DokumentyHranjatsja LIKE 'д' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
      query1.append("AND kon.NomerLichnogoDela LIKE '%-1' ");
//  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

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
      query.append("AND (kon.Bud LIKE 'д')");
    query1.append("AND (kon.Bud LIKE 'д')");
    }

    else{
      query.append("AND (kon.Dog LIKE 'д')");
      query1.append("AND (kon.Dog LIKE 'д')");
    }


      query.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY tp.ShifrPriema DESC,SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");
      query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.sogl ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot");
   
      

      stmt9 = conn.createStatement();
N=0;
rs9 = stmt9.executeQuery(query1.toString());
while(rs9.next()) {
	stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd,sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
	 stmt10.setObject(12, rs9.getString(11),Types.VARCHAR);
    stmt10.executeUpdate();
}

stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end ,p.ka  FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER);  
    	stmt11.executeUpdate();
    }

    
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
	
	kAbit=rs10.getInt(1);

	stmt11.executeUpdate();
	
}

stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,p1,p2,p3,Stob,sogl FROM Forsen ORDER BY "+spis+"");
rs10=stmt10.executeQuery();
     N=1;
   
     // stmt = conn.createStatement();
    //  rs = stmt.executeQuery(query.toString());
      while(rs10.next()) {
    	  
    	  
    	  if(header == false) {
    	        header = true;
    	        report.write("\\pard\\par\n");
    	        report.write("\\b1\\ql\\fs28{Перечень лиц, поступающих на базе среднего общего образования по целевому приему (в интересах органов гос. власти) в пределах квоты: "+rs.getString(1)+"}\\b0\\par\\par\n"); 
    	        //    report.write("\\fs16 \\b0 \\qc \\trowd \\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
					report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch

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
					 report.write("\\intbl{Согл.}\\cell\n");
    	            report.write("\\intbl\\row\n");

    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	            report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	            report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
					report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
    	      
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
					report.write("\\intbl\\cell\n");
    	            report.write("\\intbl \\row\n");
    	            
    	            report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    	         //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	          report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
    	    
    	      }
    	  
    	  
    	  ns=0;
    	//  if(nomer<num){
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
       //   report.write("\\intbl\\qc "+(summ)+"\\cell\n");
        report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
        report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
		  report.write("\\intbl\\qc "+rs10.getString(24)+"\\cell\n"); // 
          report.write("\\intbl\\row\n");
         
           
//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
        excludeList.append(","+rs10.getString(13));
        if((++tselev_nomer) <= total_amount) ++N;
      }


      if(header) report.write("\\pard\\par\n");
    
    stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
    stmt10.executeUpdate();

    }


// ЦЕЛЕВОЙ ПРИЕМ ( РосАтом )

    header = false;

    total_amount = 0;

    tselev_nomer = 0;
    nomer=0;
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT TselPr_1 FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"'");
    if( rs.next() && StringUtil.toInt(rs.getString(1),0) != 0 ) {
    	num=rs.getInt(1);
    	
    	// Абитуриенты-целевики

    	      query = new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND tp.ShifrPriema IN ('ц','ф') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    	      query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\",kon.sogl FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, lgoty l,TselevojPriem tp, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon,abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=kon.target AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND tp.ShifrPriema IN ('а') AND a.DokumentyHranjatsja LIKE 'д' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    	      query1.append("AND kon.NomerLichnogoDela LIKE '%-1' ");
    	//  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

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
    	      query.append("AND (kon.Bud LIKE 'д')");
    	    query1.append("AND (kon.Bud LIKE 'д')");
    	    }

    	    else{
    	      query.append("AND (kon.Dog LIKE 'д')");
    	      query1.append("AND (kon.Dog LIKE 'д')");
    	    }


    	      query.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY tp.ShifrPriema DESC,SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");
    	      query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.sogl ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot");
    	   
    	      

    	      stmt9 = conn.createStatement();
    	N=0;
    	rs9 = stmt9.executeQuery(query1.toString());
    	while(rs9.next()) {
    		stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd,sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
			stmt10.setObject(12, rs9.getString(11),Types.VARCHAR);
    	    stmt10.executeUpdate();
    	}

    	stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end ,p.ka  FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER);  
    	stmt11.executeUpdate();
    }

    
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
        	
        	kAbit=rs10.getInt(1);

        	stmt11.executeUpdate();
        	
        }

    	stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,p1,p2,p3,Stob,sogl FROM Forsen ORDER BY "+spis+"");
    	rs10=stmt10.executeQuery();
    	     N=1;
    	     
    	     // stmt = conn.createStatement();
    	    //  rs = stmt.executeQuery(query.toString());
    	      while(rs10.next()) {
    	    	  
    	    	  if(header == false) {
    	    	        header = true;
    	    	        report.write("\\pard\\par\n");
    	    	        report.write("\\b1\\ql\\fs28{Перечень лиц, поступающих на базе среднего общего образования по целевому приему (в интересах предприятий Росатома ) в пределах квоты:  "+rs.getString(1)+"}\\b0\\par\\par\n"); 
    	    	        //    report.write("\\fs16 \\b0 \\qc \\trowd \\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
    	    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
							report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch


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
							report.write("\\intbl{Согл.}\\cell\n");
    	    	            report.write("\\intbl\\row\n");

    	    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	    	      report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
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
							report.write("\\intbl\\cell\n");
    	    	            report.write("\\intbl \\row\n");
    	    	            
    	    	            report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    	    	         //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	    	          report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
    	    	        total_amount = StringUtil.toInt(rs.getString(1),0);
    	    	      }

    	    	  
    	    	  
    	    	  
    	    	  
    	    	  
    	    	  ns=0;
    	    	 //if(nomer<num){
    	    	  nomer++;
    	    	 // }
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
    	       //   report.write("\\intbl\\qc "+(summ)+"\\cell\n");
    	        report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
				report.write("\\intbl\\qc "+rs10.getString(24)+"\\cell\n"); // 
    	          report.write("\\intbl\\row\n");
    	         
    	           
    	//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
    	 
    	        excludeList.append(","+rs10.getString(13));
    	        if((++tselev_nomer) <= total_amount) ++N;
    	      }

    	            	
    	   
    	            if(header) report.write("\\pard\\par\n");
    	    stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
    	    stmt10.executeUpdate();

    	    }




// ЦЕЛЕВОЙ ПРИЕМ ( РосКосмос )

    header = false;
nomer=0;
    total_amount = 0;

   tselev_nomer = 0;

    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT TselPr_2 FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"'");
    if( rs.next() && StringUtil.toInt(rs.getString(1),0) != 0 ) {
    	num=rs.getInt(1);
    	
    	// Абитуриенты-целевики

    	      query = new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND tp.ShifrPriema IN ('ц','ф') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    	      query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\",kon.sogl FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, lgoty l,TselevojPriem tp, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon,abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=kon.target AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND tp.ShifrPriema IN ('к') AND a.DokumentyHranjatsja LIKE 'д' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    	      query1.append("AND kon.NomerLichnogoDela LIKE '%-1' ");
    	//  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

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
    	      query.append("AND (kon.Bud LIKE 'д')");
    	    query1.append("AND (kon.Bud LIKE 'д')");
    	    }

    	    else{
    	      query.append("AND (kon.Dog LIKE 'д')");
    	      query1.append("AND (kon.Dog LIKE 'д')");
    	    }


    	      query.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY tp.ShifrPriema DESC,SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");
    	      query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.sogl ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot");
    	   
    	      

    	      stmt9 = conn.createStatement();
    	N=0;
    	rs9 = stmt9.executeQuery(query1.toString());
    	while(rs9.next()) {
    		stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd,sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
			stmt10.setObject(12, rs9.getString(11),Types.VARCHAR);
    	    stmt10.executeUpdate();
    	}

    	stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end ,p.ka  FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER);  
    	stmt11.executeUpdate();
    }

    
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
        	
        	kAbit=rs10.getInt(1);
        	
        	stmt11.executeUpdate();
        	
        }
    	stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,p1,p2,p3,Stob,sogl FROM Forsen ORDER BY "+spis+"");
    	rs10=stmt10.executeQuery();
    	     N=1;
    	    
    	     // stmt = conn.createStatement();
    	    //  rs = stmt.executeQuery(query.toString());
    	      while(rs10.next()) {
    	    	  
    	    	  
    	    	  if(header == false) {
    	    	        header = true;
    	    	        report.write("\\pard\\par\n");
    	    	        report.write("\\b1\\ql\\fs28{Перечень лиц, поступающих на базе среднего общего образования по целевому приему (в интересах предприятий Роскосмоса ) в пределах квоты: "+rs.getString(1)+"}\\b0\\par\\par\n"); 
    	    	        //    report.write("\\fs16 \\b0 \\qc \\trowd \\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
    	    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
							report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch


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
							report.write("\\intbl{Согл.}\\cell\n");
    	    	            report.write("\\intbl\\row\n");

    	    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
							report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
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
							report.write("\\intbl\\cell\n");
    	    	            report.write("\\intbl \\row\n");
    	    	            
    	    	            report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    	    	         //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
							report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
    	    	          
    	    	        total_amount = StringUtil.toInt(rs.getString(1),0);
    	    	      }

    	    	  
    	    	  
    	    	  
    	    	  ns=0;
    	    	//  if(nomer<num){
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
    	       //   report.write("\\intbl\\qc "+(summ)+"\\cell\n");
    	        report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
				report.write("\\intbl\\qc "+rs10.getString(24)+"\\cell\n"); // 
    	          report.write("\\intbl\\row\n");
    	         
    	           
    	//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
    	 
    	        excludeList.append(","+rs10.getString(13));
    	        if((++tselev_nomer) <= total_amount) ++N;
    	      }


    	      if(header) report.write("\\pard\\par\n");
    	    
    	    stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
    	    stmt10.executeUpdate();

    	    }




// ЦЕЛЕВОЙ ПРИЕМ ( МинПромТорг )

    header = false;

    total_amount = 0;

    tselev_nomer = 0;
nomer=0;
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT TselPr_3 FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+rs2.getString(5)+"'");
    if( rs.next() && StringUtil.toInt(rs.getString(1),0) != 0 ) {
    	num=rs.getInt(1);
    	
    	// Абитуриенты-целевики

    	      query = new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND tp.ShifrPriema IN ('ц','ф') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    	      query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\",kon.sogl FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, lgoty l,TselevojPriem tp, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon,abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=kon.target AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND tp.ShifrPriema IN ('т') AND a.DokumentyHranjatsja LIKE 'д' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    	      query1.append("AND kon.NomerLichnogoDela LIKE '%-1' ");
    	//  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

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
    	      query.append("AND (kon.Bud LIKE 'д')");
    	    query1.append("AND (kon.Bud LIKE 'д')");
    	    }

    	    else{
    	      query.append("AND (kon.Dog LIKE 'д')");
    	      query1.append("AND (kon.Dog LIKE 'д')");
    	    }


    	      query.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY tp.ShifrPriema DESC,SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");
    	      query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.sogl ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot");
    	   
    	      

    	      stmt9 = conn.createStatement();
    	N=0;
    	rs9 = stmt9.executeQuery(query1.toString());
    	while(rs9.next()) {
    		stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd,sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
			stmt10.setObject(12, rs9.getString(11),Types.VARCHAR);
    	    stmt10.executeUpdate();
    	}

    	stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end ,p.ka  FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER);  
    	stmt11.executeUpdate();
    }

    
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
        	
        	kAbit=rs10.getInt(1);
     
        	stmt11.executeUpdate();
        	
        }

    	stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,p1,p2,p3,Stob,Sogl FROM Forsen ORDER BY "+spis+"");
    	rs10=stmt10.executeQuery();
    	     N=1;
    	    
    	     // stmt = conn.createStatement();
    	    //  rs = stmt.executeQuery(query.toString());
    	      while(rs10.next()) {
    	    	  
    	    	  if(header == false) {
    	    	        header = true;
    	    	        report.write("\\pard\\par\n");
    	    	        report.write("\\b1\\ql\\fs28{Перечень лиц, поступающих на базе среднего общего образования по целевому приему (в интересах предприятий Минпромторга ) в пределах квоты: "+rs.getString(1)+"}\\b0\\par\\par\n"); 
    	    	        //    report.write("\\fs16 \\b0 \\qc \\trowd \\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
    	    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	        	        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
 



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
    	    	            report.write("\\intbl{Согл.}\\cell\n");

    	    	            report.write("\\intbl\\row\n");

    	    	            report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
							report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch
    	    	      
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
							report.write("\\intbl\\cell\n");
							
    	    	            report.write("\\intbl \\row\n");
    	    	            
    	    	            report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    	    	         //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	    	       //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	    	            report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	        	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch

    	    	          

    	    	      }

    	    	  
    	    	  
    	    	  
    	    	  
    	    	  ns=0;
    	    	//  if(nomer<num){
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
    	       //   report.write("\\intbl\\qc "+(summ)+"\\cell\n");
    	        report.write("\\intbl\\qc "+rs10.getString(15)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(16)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(17)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(18)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(19)+"\\cell\n"); // 
    	        report.write("\\intbl\\qc "+rs10.getString(24)+"\\cell\n"); // 

    	          report.write("\\intbl\\row\n");
    	         
    	           
    	//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
    	 
    	        excludeList.append(","+rs10.getString(13));
    	        if((++tselev_nomer) <= total_amount) ++N;
    	      }


    	      if(header) report.write("\\pard\\par\n");

			stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
    	    stmt10.executeUpdate();
    	    
    }
    }


// По СУММЕ НАБРАННЫХ БАЛЛОВ

    header = false;

    only_one_run = true;
    nomer=0;
    boolean evidence_exist = false;
    
    oldBallAbt = -1;

// В первом запросе объединения исключаются ВСЕ "Сурские таланты" и олимпиадники вообще
    System.out.println(rs2.getString(5));
    query = new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'-' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
 //   query1= new StringBuffer("SELECT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",pr.preemptiveright,'-',kon.NomerLichnogoDela,pr.pr FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon, PR pr WHERE pr.kodabiturienta=a.kodabiturienta AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+rs2.getString(5)+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") AND a.Prinjat not in ('1','2','3','4','5','7','д')");
    query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\",kon.sogl FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, lgoty l,TselevojPriem tp, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon,abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.Prinjat not in ('1','2','3','4','5','7','д') AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
    
    
    query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");
    query1.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

  //  query.append("AND l.shifrlgot NOT IN ('к') ");
   // query1.append("AND l.shifrlgot NOT IN ('о','к') ");

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
      query.append("AND (kon.Bud LIKE 'д')");
//      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
//      query.append("AND fo.Sokr IN ('очная') AND (a.NomerPlatnogoDogovora IS NULL)");
    query1.append("AND (kon.Bud LIKE 'д')");
//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
    }else{
      query.append("AND (kon.Dog LIKE 'д')");
      query1.append("AND (kon.Dog LIKE 'д')");
//      query.append("AND kon.Forma_Ob IN ('о') AND (kon.Dog LIKE 'д')");
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
    }
    query.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");
  //  query1.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,pr.preemptiveright,kon.NomerLichnogoDela,pr.pr");
// Подсоединяем Сурские таланты и др. Олимпиадников с баллом по математике == 100 (ИСКУСТВЕННО, согласно правилам приема (хотя в БД не 100))

query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.sogl ORDER BY SummaEge DESC,a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,kon.sogl");
   
//System.out.println(query);
    
    stmt9 = conn.createStatement();

    rs9 = stmt9.executeQuery(query1.toString());
    while(rs9.next()) {
    	stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,pp,sum,ka,vd,sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
        stmt10.setObject(12, rs9.getString(11),Types.VARCHAR);
        stmt10.executeUpdate();
    }
    
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end,p.ka FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
    	stmt11.executeUpdate();
    }
    stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen in ('',' ','в','+','0','=') then zso.OtsenkaEge else zso.examen end ,p.ka  FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+rs2.getString(5)+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3'");
    rs10=stmt10.executeQuery();
    while(rs10.next()){
    	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
    	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
    	stmt11.setObject(2, rs10.getString(2),Types.INTEGER);  
    	stmt11.executeUpdate();
    }

    
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
    
     

      stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,sum,pr1,pr2,pr3,pr4,op,pp,ka,ind,ind1,ind2,ind3,ind4,ind5,p1,p2,p3,Stob,Sogl FROM Forsen ORDER BY "+spis+"");
      rs10=stmt10.executeQuery();

      while(rs10.next()){
    	  
    	  if(header == false) {
    	        header = true;
    	        N=1;
    	        report.write("\\pard\\par\n");
    	        if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
    	        	report.write("\\b1\\ql\\fs28{Перечень лиц, поступающих на базе среднего общего образования по общему конкурсу на места в рамках контрольных цифр приема}\\b0\\par\\par\n"); 
    	        }else{
    	        	report.write("\\b1\\ql\\fs28{Перечень лиц, поступающих на базе среднего общего образования, подавших документы по договору об оказании платных образовательных услуг}\\b0\\par\\par\n");
    	        }
    	        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch



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
    	        report.write("\\intbl{Согл.}\\cell\n");
    	        report.write("\\intbl\\row\n");

    	        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	        report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch

    	        
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
    	        report.write("\\intbl\\cell\n");

    	        report.write("\\intbl \\row\n");
    	        
    	        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    	     //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");//Nomer
    	   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1900\n");//shifr
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
    	        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13900\n");//soch

    	       }
    	  
    	  
    	  
    	  
//if(nomer<StringUtil.toInt(PP, 0)){    	  
nomer++;
//}
lgn++;
ns=0;
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
report.write("\\intbl\\qc "+rs10.getString(24)+"\\cell\n"); // 

report.write("\\intbl\\row\n");

 
//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз

      N++;
    }

          if(header) report.write("\\pard\\par\n");
  stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
  stmt10.executeUpdate();

  
    
    
    
    
    
    
    
    
    
    
    
    if(primechanie) report.write("\\par\\fs24\\ql\\tab\\tab{* - балл установлен согласно п.10.4. правил приёма ПГУ}\\par\n");


// Подпись Ректора

    report.write("\\pard\\par\\par\\par");

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
    report.write("\\pard\\par\\i1");

    report.write("\\par\\fs20\\ql\\tab\\tab\\b1{Примечание.}\\b0\n");
    report.write("\\par\\fs20\\ql\\tab\\tab{ В случае равного количества набранных баллов при прочих равных условиях}\n");
    report.write("\\par\\fs20\\ql\\tab\\tab{зачисляются лица, имеющие более высокий балл по профильному предмету.}\\par\n");

    report.write("\\par\\i0");

    report.write("\\page");
    stmt10 = conn.prepareStatement("DELETE FROM Forsen");
	  stmt10.executeUpdate();

   } // Перебор специальностей выбранного факультета

  report.write("}"); 
  report.close();
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