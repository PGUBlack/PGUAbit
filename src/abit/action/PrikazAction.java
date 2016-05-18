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

public class PrikazAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    stmt               = null;
        PreparedStatement    stmt2              = null;
        ResultSet            rs                 = null;
        ResultSet            rs2                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        MessageBean          mess               = new MessageBean();
        PrikazForm           form               = (PrikazForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_dec_f        = false;
        boolean              error              = false;
        boolean              first              = true;
        PreparedStatement stmt10=null;
        PreparedStatement stmt11=null;
        ResultSet rs9=null;
        ResultSet rs10=null;
        boolean flag=false;
        int pr1=0;
        int pr2=0;
        int pr3=0;
        int pr4=0;
        int pr5=0;
        int pr6=0;
        int pr7=0;
        int summt=0;
        int summa=0;
        int summ=0;
        boolean              move_to_prik_list  = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        int                  solve              = 0;
        int                  num_ab_spec        = 0;
        int                  num_ab_fak         = 0;
        int                  NapSpec_ind        = 0;
        String               KSP                = new String();
        String               Fakultet           = new String();
        String               NapSpec[]          = {"Специальность","специальности","Направление","направлению"};
        StringBuffer         kods_abts_not_prik = new StringBuffer("-1");
        Date                 date               = new Date();
        UserBean             user               = (UserBean)session.getAttribute("user");
int N=0;
        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "prikazAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "prikazForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          if ( form.getAction() == null) form.setAction(us.getClientIntName("view","init"));

          if ( form.getAction().equals("view")) {
/********************* Подготовка данных для ввода с помощью селекторов *************************/

          stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
          stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
           AbiturientBean abit_TMP = new AbiturientBean();
           abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
           abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
           abit_SD_S1.add(abit_TMP);
          }

          session.removeAttribute("kFk");
          session.removeAttribute("prSort");
          session.removeAttribute("nomPr");
          session.removeAttribute("datPr");
          session.removeAttribute("nazvPr");

/**************************************************************************************/
/******************************** Генерация отчётов ***********************************/
/**************************************************************************************/

    } else if ( form.getAction().equals("report")) {

      String file_con = new String();
      String name = new String();

      int number  = 0;
      int ind     = 1;	                   // Нумерация строк в отчёте
  //    String pr1    = new String();        // Сокраще-
  //    String pr2    = new String();        // ния предме-
  //    String pr3    = new String();        // тов

      String FormaO = new String();        // Сюда зап-cя форма обучения для передачи в файл отчёта
      String naFormuO = new String();		// Сюда зап-cя форма обучения для передачи в файл отчёта
      String TeFoSp    = new String("-1"); // Хранит код специальности
      String TEMPS[]   = new String[250];  // Шифр специальности
      String TEMPN[]   = new String[350];  // Название специальности

//************* В зависимости от признака сортировки выбираем имя файла отчёта **************

// Определяем - нужно ли выводить признак платного договора в приказ

      boolean no_remove = false;

      stmt = conn.prepareStatement("SELECT DISTINCT(k.npd) FROM Abiturient a, konkurs k, Abit_In_Prik ap WHERE k.kodabiturienta = a.kodabiturienta AND a.KodAbiturienta=ap.KodAbiturienta AND a.KodVuza LIKE ? AND ap.KodPrikaza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      stmt.setObject(2, abit_SD.getKodPrikaza(), Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) { 
        if(!( StringUtil.ntv(rs.getString(1)).equals("") )) { 
          no_remove = true;
          //06082015
        //  session.setAttribute("kontraktniki","yes"); 
         // session.setAttribute("prSort","kontraktniki"); 
          
        }
      }
      if(!no_remove) session.removeAttribute("kontraktniki");

      stmt = conn.prepareStatement("SELECT p.KodFakulteta,p.Nom_Prik,p.Data_Prik,p.Nazvanie FROM Prikaz p WHERE p.KodVuza LIKE ? AND p.KodPrikaza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      stmt.setObject(2, abit_SD.getKodPrikaza(), Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) { 
        abit_SD.setKodFakulteta(new Integer(rs.getString(1)));
        abit_SD.setNomerPrik(rs.getString(2));
        abit_SD.setDataPrik(rs.getString(3));
        abit_SD.setNazvanie(rs.getString(4));
      }

      if(session.getAttribute("kontraktniki")==null) 
///      if((""+session.getAttribute("prSort")).equals("kontraktniki"))
      {
        FormaO = "бюджетную";
      	naFormuO = "по очной форме обучения";
      }
      else
      {
        FormaO = "контрактную";
        naFormuO = "на места с оплатой обучения";
      }

      if(abit_SD.getForma_Pr().equals("forma1"))
        file_con = "prikaz_f1_"+StringUtil.toEng(FormaO).substring(1,1)+"_N"+abit_SD.getNomerPrik();
      else
        file_con = "prikaz_f2_"+StringUtil.toEng(FormaO).substring(1,1)+"_N"+abit_SD.getNomerPrik();

      stmt = conn.prepareStatement("SELECT f.AbbreviaturaFakulteta FROM Fakultety f,Prikaz p WHERE f.KodFakulteta=p.KodFakulteta AND p.KodPrikaza LIKE ? AND f.KodVuza LIKE ?");
      stmt.setObject(1, abit_SD.getKodPrikaza(), Types.INTEGER);
      stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) { 
        file_con = file_con+"_"+StringUtil.toEng(rs.getString(1));
        Fakultet = rs.getString(1);
      }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

      if(abit_SD.getForma_Pr().equals("forma1")) {

        if(session.getAttribute("kontraktniki")!=null)
          name = "Приказ №"+abit_SD.getNomerPrik()+" (ф1) "+abit_SD.getNazvanie()+" на "+Fakultet.toUpperCase();
        else
          name = "Приказ №"+abit_SD.getNomerPrik()+" (ф1) "+abit_SD.getNazvanie()+" бюдж. на "+Fakultet.toUpperCase();

      } else {

        if(session.getAttribute("kontraktniki")!=null)
          name = "Приказ №"+abit_SD.getNomerPrik()+" (ф2) "+abit_SD.getNazvanie()+" контр. на "+Fakultet.toUpperCase();
        else 
          name = "Приказ №"+abit_SD.getNomerPrik()+" (ф2) "+abit_SD.getNazvanie()+" бюдж. на "+Fakultet.toUpperCase();
      }

      session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

      String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

      BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

      stmt = conn.prepareStatement("SELECT Gerb,Logo,NazvanieVuza,AbbreviaturaVuza,PostAdresVuza,RST FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()){
        report.write("{\\rtf1\\ansi\\paperw11906\\paperh16838\\margl1700\\margr1135\\margt1415\\margb2100\\viewscale90\n");
///        report.write("\\qc{"+rs.getString(1)+"}");
///        report.write("\\par");
        report.write("{\\trowd\\trgaph108\\trqc\\clvertalc\\cellx1700\\cellx8500\\intbl"+rs.getString(2)+"\\cell");
        //report.write("{\\trowd\\trgaph108\\trqc\\clvertalc\\cellx1700\\cellx8500\\intbl\\cell");
        report.write("\\intbl");
        report.write("\\fs18\\fs24\\qc\\b1{МИНОБРНАУКИ РОССИИ}\\fs12\\par");
        //report.write("\\fs18\\fs32\\qc\\b1{Российской федерации}\\fs12\\par");
        report.write("\\fs24\\qc{Федеральное государственное бюджетное образовательное учреждение высшего профессионального образования}\\b0\\par");
        //report.write("\\fs24\\qc\\b\\{учреждение высшего профессионального образования}\\b0\\par");
        report.write("\\fs24\\qc\\b\\'ab{"+rs.getString(3)+"}\\'bb\\b0\\par");
        report.write("\\fs24\\qc\\b({ФГБОУ ВПО «"+rs.getString(4)+"»})\\b0\\fs12\\par");
       // report.write("\\fs24\\qc\\b({_________________________________________________________________________________________________________________________________})\\b0\\fs12\\par");
        report.write("\\cell");
       // report.write("\\intbl"+rs.getString(6)+"\\cell");clvertalc\\cellx9500\\
        report.write("\\intbl\\row}");
        report.write("\\fs18\\fs24\\qc\\b1{___________________________________________________________________________}\\fs12\\par");
        //report.write("{\\do\\dobxcolumn\\dobypara\\dodhgt8193\\dpline\\dpptx0\\dppty0\\dpptx9720\\dppty0\\dpx0\\dpy360\\dpxsize9000\\dpysize0\\dplinew15\\dplinecor0\\dplinecog0\\dplinecob0}");
       // report.write("{\\do\\dobxcolumn\\dobypara\\dodhgt8192\\dpline\\dpptx0\\dppty0\\dpptx9720\\dppty0\\dpx0\\dpy420\\dpxsize9000\\dpysize0\\dplinew70\\dplinecor0\\dplinecog0\\dplinecob0}");
      } else report.write("{\\rtf1\\ansi\n");
      report.write("\\par\\par\\par\\fs28\\qc\\b ПРИКАЗ\\par");
      report.write("\\fs12\\par\\fs28 \\b1 \\qc   ____________________                 №__________________\\par\\par\n");
      report.write("\\par\\par\\par\\fs24\\qc\\b О зачислении на первый курс "+naFormuO+"\\par\\par\n");
      report.write("\\par\\qc{\\fs28\\b0 На основании решения приёмной комиссии от "+abit_SD.getDataPrik()+" (протокол №"+abit_SD.getNomerPrik()+")");
      report.write("\\par\\par\\par\\fs28\\qc\\b ПРИКАЗЫВАЮ:\\par");
      if((""+session.getAttribute("prSort")).equals("kontraktniki")){
    	  report.write("\\par зачислить с 01.09."+StringUtil.CurrYear()+" на места по договорам об оказании платных образовательных услуг, в число студентов \\par\\par");
      }else{
      report.write("\\par зачислить с 01.09."+StringUtil.CurrYear()+" на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований федерального бюджета Российской Федерации, выдержавших вступительные испытания и прошедших по общему конкурсу  в число студентов \\par\\par");
      }
      
    
      
      
      stmt = conn.prepareStatement("SELECT f.NazvanieVRoditelnom,nv.NazvanieRodit FROM Fakultety f,NazvanieVuza nv WHERE nv.KodVuza=f.KodVuza AND f.KodFakulteta LIKE ? AND f.KodVuza LIKE ?");
      stmt.setObject(1, abit_SD.getKodFakulteta(), Types.INTEGER);
      stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
	rs = stmt.executeQuery();
	if(rs.next())
	{
		
        report.write("\\par"+rs.getString(1).toUpperCase());
       // report.write("\\par\\b1{"+rs.getString(2)+"}");
      }

      report.write("}");

if(abit_SD.getForma_Pr().equals("forma1")) {

/**************************************************************************/
/*********************      1-я ФОРМА ПРИКАЗА      ************************/
/**************************************************************************/

String Dop_Usl[] = {"AND kon.op IN ('7')","AND kon.op IN ('3','4')","AND kon.Target LIKE '5'","AND kon.Target LIKE '2'","AND kon.Target LIKE '3'","AND kon.Target LIKE '4'"," AND kon.op='1' AND kon.target='1'"};

String Header1[] = {"Выдержавшие вступительные испытания и прошедшие на основании пункта 3.7 правил приема","Выдержавшие вступительные испытания и прошедшие на основании пункта 10.2 правил приема","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема (Росатом)","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема (Роскосмос)","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема (Минпромторг)","На базе среднего общего образования, по результатам испытаний, проводимых в формате ВУЗа:"};

String Header[] = {"Выдержавшие вступительные испытания и прошедшие на основании пункта 3.7 правил приема","Выдержавшие вступительные испытания и прошедшие на основании пункта 10.2 правил приема","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема (Росатом)","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема (Роскосмос)","Выдержавшие вступительные испытания и прошедшие на основании пункта 14 правил приема (Минпромторг)","На базе среднего общего образования, по результатам ЕГЭ:"};


/******** ПЕРЕБОР СПЕЦИАЛЬНОСТЕЙ ФАКУЛЬТЕТА *****/
///System.out.println("PRIK="+query.toString());
stmt2 = conn.prepareStatement("SELECT DISTINCT s.ShifrSpetsialnostiOKSO,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,COUNT(a.KodAbiturienta),s.edulevel FROM Spetsialnosti s,Abiturient a, Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=a.KodSpetsialnZach AND s.KodSpetsialnosti=a.KodSpetsialnZach AND a.KodSpetsialnZach IS NOT NULL AND a.Prinjat IN ('1','2','3','4','д','7') AND a.vPrikaze LIKE 'д' AND s.KodFakulteta LIKE ? AND a.KodVuza LIKE ? GROUP BY s.ShifrSpetsialnostiOKSO,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,s.edulevel ORDER BY s.KodSpetsialnosti ASC");
stmt2.setObject(1,abit_SD.getKodFakulteta(),Types.INTEGER);
stmt2.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
rs2 = stmt2.executeQuery();
while(rs2.next()){
	
   if(!first) report.write("\\pard\\par\\tab\\fs24\\b0 Всего по "+NapSpec[NapSpec_ind+1]+": "+num_ab_spec+"\n"); 

// Определяем название - направление или специальность

   if((rs2.getString(1).length() > 7 && rs2.getString(1).substring(7).equals("65")) || rs2.getString(1).length() == 6) NapSpec_ind = 0;
   else NapSpec_ind = 2;

   num_ab_fak += num_ab_spec;

   num_ab_spec = 0; 

   first=false;

   report.write("\\par\\pard\\par\\par\\fs24\\qc\\b1{ "+NapSpec[NapSpec_ind]+": "+rs2.getString(1)+" - "+rs2.getString(2).toUpperCase()+"}\\b0\\par\n");
   KSP = rs2.getString(3);

/*******************************************************************************/
/*** Вывод разделов с разными категориями абитуриентов текущей специальности ***/
/*******************************************************************************/

    report.write("\\fs24 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");

    report.write("\\fs24 \\b1 \\qc\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");

    boolean show_line;

for(int i=0; i<Dop_Usl.length; i++) {
	flag=false;
System.out.println(Dop_Usl[i]);
     number    = 0;
     show_line = true;
     stmt = conn.prepareStatement("DELETE from Forsen where 1=1 ");
     stmt.executeUpdate();
// ИСКЛЮЧАЕМ ТЕХ, КТО УЖЕ ДОБАВЛЕН В СПИСОК ПРИКАЗА
    // query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz, SUM(zso.OtsenkaEge)\"SummaEge\" FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, lgoty l,TselevojPriem tp, NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Konkurs kon,abitdopinf adi WHERE adi.BallAtt not like 'да' AND adi.ballsgto not like 'да' AND adi.ballzgto not like 'да' AND adi.ballsoch not like 'да' AND adi.ballpoi not like 'да' and adi.trudovajadejatelnost not like 'да' AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta=zso.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.Prinjat not in ('1','2','3','4','5','7','д') AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");
     stmt.close();
   //  stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.KodSpetsialnZach,a.NomerPlatnogoDogovora,SUM(zso.OtsenkaEge) FROM Abiturient a, Abit_In_Prik ap, Konkurs kon,ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE ens.KodSpetsialnosti=a.KodSpetsialnZach AND zso.KodAbiturienta=a.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=a.KodSpetsialnZach AND ap.KodAbiturienta=a.KodAbiturienta AND a.KodAbiturienta NOT IN ("+kods_abts_not_prik+") AND a.Prinjat IN ('1','2','3','4','д','7') AND a.vPrikaze IN ('д','в','+') AND ap.KodPrikaza LIKE "+abit_SD.getKodPrikaza()+" AND a.KodSpetsialnZach LIKE '"+KSP+"' "+Dop_Usl[i]+" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.KodSpetsialnZach,a.NomerPlatnogoDogovora ORDER BY KodSpetsialnZach, Familija, Imja, Otchestvo ASC");
     stmt = conn.prepareStatement("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.KodSpetsialnZach,a.NomerPlatnogoDogovora,a.viddoksredobraz FROM Abiturient a, Spetsialnosti s,Konkurs kon, Abit_In_Prik ap WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.kodspetsialnzach=kon.kodspetsialnosti AND ap.KodAbiturienta=a.KodAbiturienta AND a.KodAbiturienta NOT IN ("+kods_abts_not_prik+") AND a.Prinjat IN ('1','2','3','4','д','7') AND a.vPrikaze IN ('д','в','+') AND ap.KodPrikaza LIKE "+abit_SD.getKodPrikaza()+" AND a.KodSpetsialnZach LIKE '"+KSP+"' "+Dop_Usl[i]+" GROUP BY a.KodAbiturienta,kon.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.KodSpetsialnZach,a.NomerPlatnogoDogovora,a.viddoksredobraz ORDER BY KodSpetsialnZach, Familija, Imja, Otchestvo ASC");
     
     rs = stmt.executeQuery();
     while(rs.next()) {
    	 flag=true;
    	 stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ka,vd,op,ko) VALUES(?,?,?,?,?,?,?,?,?)");
     	N++;
     	 kods_abts_not_prik.append(","+rs.getString(1));
         stmt10.setObject(1, N,Types.INTEGER);
         stmt10.setObject(2, rs.getString(2),Types.VARCHAR);
         stmt10.setObject(3, rs.getString(3),Types.VARCHAR);
         stmt10.setObject(4, rs.getString(4),Types.VARCHAR);
         stmt10.setObject(5, rs.getString(5),Types.VARCHAR);
         stmt10.setObject(6, new Integer(rs.getString(1)),Types.INTEGER);
         stmt10.setObject(7, rs.getString(8),Types.VARCHAR);
         if(rs.getString(7)!=null){
         stmt10.setObject(8, rs.getString(7),Types.VARCHAR);
         }else{
         stmt10.setObject(8, "-",Types.VARCHAR); 
         }
         stmt10.setObject(9, "о",Types.VARCHAR);
         stmt10.executeUpdate();
     }
     if(abit_SD.getKodFakulteta()!=31 && abit_SD.getKodFakulteta()!=30 && abit_SD.getKodFakulteta()!=29 && !rs2.getString(5).equals("м")){ 	
     stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen>0 then zso.examen else zso.otsenkaege end,p.ka,zso.examen FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+KSP+"  AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '1' and zso.examen not in ('в','+')");
     rs10=stmt10.executeQuery();
     while(rs10.next()){
     	stmt11=conn.prepareStatement("UPDATE Forsen SET pr1=?, pp=? WHERE ka=?");
     	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
     	stmt11.setObject(2, rs10.getString(3),Types.VARCHAR); 
     	stmt11.setObject(3, rs10.getString(2),Types.INTEGER); 
     	stmt11.executeUpdate();
     }
     stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen>0 then zso.examen else zso.otsenkaege end,p.ka,zso.examen FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+KSP+"  AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '2' and zso.examen not in ('в','+')");
     rs10=stmt10.executeQuery();
     while(rs10.next()){
     	stmt11=conn.prepareStatement("UPDATE Forsen SET pr2=? WHERE ka=?");
     	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
     	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
     	stmt11.executeUpdate();
     }
     stmt10=conn.prepareStatement("SELECT DISTINCT case when zso.examen>0 then zso.examen else zso.otsenkaege end,p.ka,zso.examen FROM Forsen p, ZajavlennyeShkolnyeOtsenki zso, abiturient a, ekzamenynaspetsialnosti ens WHERE p.ka=a.kodabiturienta AND ens.kodspetsialnosti LIKE "+KSP+" AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta=p.ka AND ens.prioritet LIKE '3' and zso.examen not in ('в','+')");
     rs10=stmt10.executeQuery();
     while(rs10.next()){
     	stmt11=conn.prepareStatement("UPDATE Forsen SET pr3=? WHERE ka=?");
     	stmt11.setObject(1, rs10.getString(1),Types.INTEGER); 
     	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
     	stmt11.executeUpdate();
     }
     }
     if(abit_SD.getKodFakulteta()==35){
    	 stmt10 = conn.prepareStatement("SELECT ROUND(avg(cast (oa.otsenkaatt as float)),2),p.ka from oa oa, forsen p where oa.kodabiturienta=p.ka and oa.otsenkaatt not like '0' group by p.ka");
         rs10=stmt10.executeQuery();
         while(rs10.next()){
        	 stmt11=conn.prepareStatement("UPDATE Forsen set ko=? where ka=?");
        	 stmt11.setObject(1, rs10.getFloat(1),Types.VARCHAR); 
          	stmt11.setObject(2, rs10.getString(2),Types.INTEGER); 
          	stmt11.executeUpdate();
         }
     }else{
     stmt10=conn.prepareStatement("SELECT DISTINCT k.prof,p.ka FROM Forsen p, konkurs k WHERE p.ka=k.kodabiturienta AND k.kodspetsialnosti LIKE "+KSP+"");
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
    
     stmt10=conn.prepareStatement("SELECT kon.kodabiturienta,adi.BallAtt,adi.BallSGTO,adi.BALLZGTO,adi.BallPOI,adi.BallSoch,adi.TrudovajaDejatelnost,kon.olimp,f.sum from abitdopinf adi, konkurs kon, forsen f WHERE f.ka=kon.kodabiturienta AND adi.kodabiturienta=kon.kodabiturienta AND  kon.kodspetsialnosti LIKE "+KSP+" ");
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
     	stmt11.executeUpdate();
     	
     }
     }
     if(abit_SD.getKodFakulteta()==35){
    	 stmt = conn.prepareStatement("SELECT Shifr,F,I,O,ko,op FROM Forsen ORDER BY sum DESC");
    	 rs=stmt.executeQuery();  
    	 while(rs.next()){
    	      if(show_line) {
    	        report.write("\\pard\\par\\tab\\fs24\\b0\\ql Выдержавшие вступительные испытания и прошедшие по конкурсу:\n");
    	        report.write("\\pard\\par\n\\par\n");
    	        show_line = false;

    	 // Вывод данных по абитуриенту

    	        report.write("\\b0 \\ql\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    	        report.write("\\clvertalc \\cellx800\n");
    	        report.write("\\clvertalc \\cellx2800\n");
    	        report.write("\\clvertalc \\cellx4400\n");
    	        report.write("\\clvertalc \\cellx6400\n");
    	        report.write("\\clvertalc \\cellx7800\n");
    	        report.write("\\clvertalc \\cellx9100\n");

    	        if((""+session.getAttribute("prSort")).equals("kontraktniki"))
    	          report.write("\\clvertalc \\cellx12000\n");
    	      }

    	        num_ab_spec++;

    	        report.write("\\intbl\\qc "+Integer.toString(++number)+".\\cell\n");
    	        report.write("\\intbl\\ql "+rs.getString(2)+"\\cell\n");
    	        report.write("\\intbl "+rs.getString(3)+"\\cell\n");
    	        report.write("\\intbl "+rs.getString(4)+"\\cell\n");
    	        report.write("\\intbl "+rs.getString(1)+"\\cell\n");
    	       // report.write("\\intbl "+StringUtil.ztv(rs.getString(8))+"\\cell\n");
    	        report.write("\\intbl "+rs.getString(5)+"\\cell\n");
    	      if((""+session.getAttribute("prSort")).equals("kontraktniki"))
    	        report.write("\\intbl "+rs.getString(6)+"\\cell\n");

    	        report.write("\\intbl\\row\n");

    	   }//while

    	 
}else if(abit_SD.getKodFakulteta()!=31 && abit_SD.getKodFakulteta()!=30 && abit_SD.getKodFakulteta()!=29 && abit_SD.getKodFakulteta()!=35 && !rs2.getString(5).equals("м")){ 	 
stmt = conn.prepareStatement("SELECT Shifr,F,I,O,sum,op FROM Forsen ORDER BY sum DESC");
rs=stmt.executeQuery();  
while(rs.next()){
     if(show_line) {
       report.write("\\pard\\par\\tab\\fs24\\b0\\ql"+Header[i]+"\n");
       report.write("\\pard\\par\n\\par\n");
       show_line = false;

// Вывод данных по абитуриенту

       report.write("\\b0 \\ql\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
       report.write("\\clvertalc \\cellx800\n");
       report.write("\\clvertalc \\cellx2800\n");
       report.write("\\clvertalc \\cellx4400\n");
       report.write("\\clvertalc \\cellx6400\n");
       report.write("\\clvertalc \\cellx7800\n");
       report.write("\\clvertalc \\cellx9100\n");

       if((""+session.getAttribute("prSort")).equals("kontraktniki"))
         report.write("\\clvertalc \\cellx12000\n");
     }

       num_ab_spec++;

       report.write("\\intbl\\qc "+Integer.toString(++number)+".\\cell\n");
       report.write("\\intbl\\ql "+rs.getString(2)+"\\cell\n");
       report.write("\\intbl "+rs.getString(3)+"\\cell\n");
       report.write("\\intbl "+rs.getString(4)+"\\cell\n");
       report.write("\\intbl "+rs.getString(1)+"\\cell\n");
      // report.write("\\intbl "+StringUtil.ztv(rs.getString(8))+"\\cell\n");
       report.write("\\intbl "+rs.getString(5)+"\\cell\n");
     if((""+session.getAttribute("prSort")).equals("kontraktniki"))
       report.write("\\intbl "+rs.getString(6)+"\\cell\n");

       report.write("\\intbl\\row\n");

  }//while





 }else{
	 
	 show_line=true;
	 stmt = conn.prepareStatement("SELECT Shifr,F,I,O,sum,op FROM Forsen ORDER BY sum DESC");
	 rs=stmt.executeQuery();
	 // stmt = conn.createStatement();
	 //  rs = stmt.executeQuery(query.toString());

	 while(rs.next()) {
	 	
	 	if(show_line) {
	 	report.write("\\pard\\par\\tab\\fs24\\b0\\ql"+Header1[i]+"\n");
	     report.write("\\pard\\par\n\\par\n");
	     show_line = false;

	 //Вывод данных по абитуриенту

	     report.write("\\b0 \\ql\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	     report.write("\\clvertalc \\cellx800\n");
	     report.write("\\clvertalc \\cellx2800\n");
	     report.write("\\clvertalc \\cellx4400\n");
	     report.write("\\clvertalc \\cellx6400\n");
	     report.write("\\clvertalc \\cellx7800\n");
	     report.write("\\clvertalc \\cellx9100\n");

	     if((""+session.getAttribute("prSort")).equals("kontraktniki"))
	       report.write("\\clvertalc \\cellx12000\n");
	 	}
	 	
	 	num_ab_spec++;
	 	
	 	 report.write("\\intbl\\qc "+Integer.toString(++number)+".\\cell\n");
	      report.write("\\intbl\\ql "+rs.getString(2)+"\\cell\n");
	      report.write("\\intbl "+rs.getString(3)+"\\cell\n");
	      report.write("\\intbl "+rs.getString(4)+"\\cell\n");
	      report.write("\\intbl "+rs.getString(1)+"\\cell\n");
	     // report.write("\\intbl "+StringUtil.ztv(rs.getString(8))+"\\cell\n");
	      report.write("\\intbl "+rs.getString(5)+"\\cell\n");
	      if((""+session.getAttribute("prSort")).equals("kontraktniki"))
	          report.write("\\intbl "+rs.getString(6)+"\\cell\n");
	      report.write("\\intbl\\row\n");
	 
	 }
	 
	 
	 
	 
	 
	 
	 
	 
 }

 



 }//for

}//while

} else{

/************************************************************************/
/***************         2-я ФОРМА ПРИКАЗА                ***************/
/************************************************************************/

report.write("\\fs24 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");

/******** Выбираются специальности определённого факультета, чтобы подставить их в запрос *****/

	stmt = conn.prepareStatement("SELECT DISTINCT s.ShifrSpetsialnostiOKSO,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,COUNT(a.KodAbiturienta) FROM Spetsialnosti s,Abiturient a WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND a.KodSpetsialnZach IS NOT NULL AND a.Prinjat IN ('1','2','3','4','д','7') AND a.vPrikaze LIKE 'д' AND s.KodFakulteta LIKE ? AND a.KodVuza LIKE ? GROUP BY s.ShifrSpetsialnostiOKSO,s.NazvanieSpetsialnosti,s.KodSpetsialnosti ORDER BY s.KodSpetsialnosti ASC");
	stmt.setObject(1,abit_SD.getKodFakulteta(),Types.INTEGER);
	stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
	rs = stmt.executeQuery();
	while(rs.next()){
         TEMPS[ind]   = rs.getString(1);
         TEMPN[ind++] = rs.getString(2);
	}

	stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.KodSpetsialnZach,a.NomerPlatnogoDogovora,SUM(case when examen>0 then examen else otsenkaege end) FROM Abiturient a,Abit_In_Prik ap,ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE ens.KodSpetsialnosti=a.KodSpetsialnZach AND zso.KodAbiturienta=a.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND ap.KodAbiturienta=a.KodAbiturienta AND a.KodAbiturienta NOT IN ("+kods_abts_not_prik+") AND a.KodSpetsialnZach IN (SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE "+abit_SD.getKodFakulteta()+") AND a.vPrikaze IN ('д','в','+') AND a.Prinjat IN ('1','2','3','4','д','7') AND ap.KodPrikaza LIKE "+abit_SD.getKodPrikaza()+" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.KodSpetsialnZach,a.NomerPlatnogoDogovora ORDER BY a.KodSpetsialnZach,a.Familija,a.Imja,a.Otchestvo ASC");
	rs = stmt.executeQuery();
	while(rs.next())
	{
        
       kods_abts_not_prik.append(","+rs.getString(1));

// Если при выводе следующего абитуриента код специальности изменился то: вывод шифра и названия специальности

	 if(!TeFoSp.equals(rs.getString(6))) {

         if(first) report.write("\\par\\pard\n");
         else { 
           report.write("\\pard\\par\n");
           report.write("\\tab\\fs24\\b0 Всего по "+NapSpec[NapSpec_ind+1]+": "+num_ab_spec+"\n"); 
         }

         num_ab_fak += num_ab_spec;

         num_ab_spec = 0; 

         first=false;

         TeFoSp = rs.getString(6);

         report.write("\\pard\\par\\par\n");

         stmt2 = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
         stmt2.setObject(1,rs.getString(6),Types.INTEGER);
         rs2 = stmt2.executeQuery();
         if(rs2.next()) report.write("\\fs24\\b1 "+NapSpec[NapSpec_ind]+": "+rs2.getString(1)+" - "+rs2.getString(2).toUpperCase()+"\\b0\n");

         report.write("\\pard\\par\\par\\fs24 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");

//Вывод данных по абитуриенту

         report.write("\\b0 \\ql\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
         report.write("\\clvertalc \\cellx600\n");
         report.write("\\clvertalc \\cellx2000\n");
         report.write("\\clvertalc \\cellx4000\n");
         report.write("\\clvertalc \\cellx5500\n");
         report.write("\\clvertalc \\cellx7500\n");
         report.write("\\clvertalc \\cellx8800\n");

         if((""+session.getAttribute("prSort")).equals("kontraktniki"))
           report.write("\\clvertalc \\cellx11200\n");
       }

     report.write("\\intbl "+Integer.toString(++num_ab_spec)+"\\cell\n");
     report.write("\\intbl "+rs.getString(2)+"\\cell\n");
     report.write("\\intbl "+rs.getString(3)+"\\cell\n");
     report.write("\\intbl "+rs.getString(4)+"\\cell\n");
     report.write("\\intbl "+rs.getString(5)+"\\cell\n");
     //report.write("\\intbl "+StringUtil.ztv(rs.getString(8))+"\\cell\n");
     report.write("\\intbl "+0+"\\cell\n");
     if((""+session.getAttribute("prSort")).equals("kontraktniki"))
       report.write("\\intbl "+rs.getString(7)+"\\cell\n");

     report.write("\\intbl \\row\n");
}
abit_SD.setSpecial22(new Integer(num_ab_spec));

}// END Forma2


  num_ab_fak += num_ab_spec;

  report.write("\\pard\\par\\tab\\fs24\\b0 Всего по "+NapSpec[NapSpec_ind+1]+": "+num_ab_spec+"\n"); 
  report.write("\\par\\tab\\b0 Всего по факультету/институту: "+num_ab_fak+"\n"); 

/** Ответственные лица **/

  report.write("\\pard\\fs24\n");
  report.write("\\par\\par\\par\\b1\n");

  report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc\\cellx3750\n");
  report.write("\\clvertalc\\cellx5800\n");
  report.write("\\clvertalc\\cellx8400\n");

  stmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%Ректор%'");
  rs = stmt.executeQuery();
  if(rs.next()) {
    report.write("\\intbl\\b1{Ректор}\\cell\n");
    report.write("\\intbl\\b1{_____________}\\cell\n");
    report.write("\\intbl\\b0{"+rs.getString(2)+"}\\cell\n");
    report.write("\\intbl\\row\n");
  }

  //report.write("\\pard\\par\\page\n");
  report.write("\\pard\\insrsid\\page\\par");

  report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc\\cellx3750\n");
  report.write("\\clvertalc\\cellx5800\n");
  report.write("\\clvertalc\\cellx8400\n");

  report.write("\\intbl\\b1\\par{Приказ вносит:}\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\row\n");

  stmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%секрет%'");
  rs = stmt.executeQuery();
  if(rs.next()) {
    report.write("\\intbl\\b1\\par{"+rs.getString(1)+"}\\cell\n");
    report.write("\\intbl\\b1\\par\\par{_____________}\\cell\n");
    report.write("\\intbl\\b0\\par\\par{"+rs.getString(2)+"}\\cell\n");
    report.write("\\intbl\\row\n");
  }

  report.write("\\intbl\\b1\\par{Согласовано:}\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\row\n");
// Временно
//    report.write("\\intbl\\b1\\par{Первый проректор:}\\cell\n");
 //   report.write("\\intbl\\b1\\par{_____________}\\cell\n");
//    report.write("\\intbl\\b0\\par{В.А. Мещеряков}\\cell\n");
  //  report.write("\\intbl\\row\n");
//
  report.write("\\intbl\\b1\\par{Первый проректор}\\cell\n");
  report.write("\\intbl\\b1\\par{_____________}\\cell\n");
  report.write("\\intbl\\b0\\par{В.А. Мещеряков}\\cell\n");
  report.write("\\intbl\\row\n");
  
  stmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%Перв%прорек%' OR Doljnost LIKE '%прорек%'");
  rs = stmt.executeQuery();
  if(rs.next()){
    report.write("\\intbl\\b1\\par{"+rs.getString(1)+"}\\cell\n");
    report.write("\\intbl\\b1\\par{_____________}\\cell\n");
    report.write("\\intbl\\b0\\par{"+rs.getString(2)+"}\\cell\n");
    report.write("\\intbl\\row\n");
  }

  stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta, Dekan FROM Fakultety WHERE KodFakulteta LIKE ?");
  stmt.setObject(1,abit_SD.getKodFakulteta(),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) {
    report.write("\\intbl\\b1\\par{Декан/директор "+rs.getString(1).toUpperCase()+":}\\cell\n");
    report.write("\\intbl\\b1\\par{_____________}\\cell\n");
    report.write("\\intbl\\b0\\par{"+rs.getString(2)+"}\\cell\n");
    report.write("\\intbl\\row\n");
  }
  
  report.write("\\intbl\\b1\\par{Начальник ПУ}\\cell\n");
  report.write("\\intbl\\b1\\par{_____________}\\cell\n");
  report.write("\\intbl\\b0\\par{К.Б. Филиппов}\\cell\n");
  report.write("\\intbl\\row\n");

  stmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%канцеляр%' OR Doljnost LIKE '%ОДО%'");
  rs = stmt.executeQuery();
  if(rs.next()){
    report.write("\\intbl\\b1\\par{"+rs.getString(1)+"}\\cell\n");
    report.write("\\intbl\\b1\\par{_____________}\\cell\n");
    report.write("\\intbl\\b0\\par{"+rs.getString(2)+"}\\cell\n");
    report.write("\\intbl\\row\n");
  }

  report.write("}"); 
  report.close();
  form.setAction(us.getClientIntName("new_rep","crt"));
  return mapping.findForward("rep_brw");

} else if ( form.getAction().equals("create")) {

/*************************************************************************/
/*****  СОЗДАНИЕ НОВОГО ПРИКАЗА В ТАБЛИЦЕ ПРИКАЗОВ И ДОБАВЛЕНИЕ      *****/
/*****      АБИТУРИЕНТОВ В ПРИКАЗ, РАНЕЕ НЕ ПОПАДАВШИХ ТУДА          *****/
/*************************************************************************/

  stmt2 = conn.prepareStatement("INSERT INTO Prikaz(KodVuza,KodFakulteta,Nom_Prik,Nazvanie,Data_Prik,Descr) VALUES(?,?,?,?,?,?)");
  stmt2.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  stmt2.setObject(2,session.getAttribute("kFk"),Types.INTEGER);
  stmt2.setObject(3,session.getAttribute("nomPr"),Types.VARCHAR);
  stmt2.setObject(4,session.getAttribute("nazvPr"),Types.VARCHAR);
  stmt2.setObject(5,session.getAttribute("datPr"),Types.VARCHAR);
  stmt2.setObject(6,session.getAttribute("descPr"),Types.VARCHAR);
  stmt2.executeUpdate();

  stmt2 = conn.prepareStatement("SELECT IDENT_CURRENT('Prikaz')");
  rs2 = stmt2.executeQuery();
  if(rs2.next()) abit_SD.setKodPrikaza(new Integer(rs2.getString(1)));

  StringBuffer query = new StringBuffer("SELECT KodAbiturienta,NomerLichnogoDela,Familija,Imja,Otchestvo,KodSpetsialnZach,NomerPlatnogoDogovora,vPrikaze FROM Abiturient WHERE KodSpetsialnZach IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE "+session.getAttribute("kFk")+") AND Prinjat IN ('1','2','3','4','д','7')");

  if(session.getAttribute("prSort").equals("budgetniki"))
    query.append(" AND KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Bud LIKE 'д') AND KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')");
//    query.append(" AND NomerPlatnogoDogovora IS NULL");
  else
    query.append(" AND KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')");

//    query.append(" AND NomerPlatnogoDogovora IS NOT NULL");

  query.append(" AND KodAbiturienta NOT IN ("+session.getAttribute("not_in_prik")+")");

  query.append(" ORDER BY KodSpetsialnZach, Familija, Imja, Otchestvo ASC");

  stmt = conn.prepareStatement(query.toString());
  rs = stmt.executeQuery();
  while(rs.next()) {

    stmt2 = conn.prepareStatement("SELECT KodPrikaza FROM Abit_In_Prik WHERE KodAbiturienta LIKE ?");
    stmt2.setObject(1,rs.getString(1),Types.INTEGER);
    rs2=stmt2.executeQuery();
    if(!rs2.next()) {
// Жёсткое правило - добавляем абитуриента в приказ только, если он раньше не был в нем

// Нужно наверное ввести признак в приказе - ? для тех, кто под вопросом и с этим признаком не заносить в приказ никого

      stmt2 = conn.prepareStatement("INSERT Abit_In_Prik(KodAbiturienta,KodPrikaza) VALUES(?,?)");
      stmt2.setObject(1,rs.getString(1),Types.INTEGER);
      stmt2.setObject(2,abit_SD.getKodPrikaza(),Types.INTEGER);
      stmt2.executeUpdate();

      stmt2 = conn.prepareStatement("UPDATE Abiturient SET vPrikaze = ? WHERE KodAbiturienta LIKE ?");
      stmt2.setObject(1, "д",Types.VARCHAR);
      stmt2.setObject(2, rs.getString(1),Types.INTEGER);
      stmt2.executeUpdate();
    }
  }
  
  mess.setStatus("Внимание!");
  mess.setMessage("Приказ №"+session.getAttribute("nomPr")+" создан успешно");

  session.removeAttribute("not_in_prik");
  session.removeAttribute("kFk");
  session.removeAttribute("prSort");
  session.removeAttribute("nomPr");
  session.removeAttribute("datPr");
  session.removeAttribute("nazvPr");

  form.setAction(us.getClientIntName("priks","create"));

} else if ( form.getAction().equals("rePrinjat")) {

/****************************************************************/
/*****   ИЗМЕНЕНИЕ ПРИЗНАКА "ЗАЧИСЛЕН" НА ПРОТИВОПОЛОЖНЫЙ   *****/
/*****      И СВЯЗАННОГО С НИМ ПРИЗНАКА КОПИИ АТТЕСТАТА     *****/
/****************************************************************/

    String prinjat = new String("д");
    String att = new String(Constants.Att_orig);
    stmt = conn.prepareStatement("SELECT Prinjat FROM Abiturient WHERE KodAbiturienta LIKE ?");
    stmt.setObject(1,request.getParameter("kAb"),Types.VARCHAR);
    rs = stmt.executeQuery();
    if(rs.next()) {
      if(rs.getString(1) != null) {
        if(rs.getString(1).equals("д") || rs.getString(1).equals("1") || rs.getString(1).equals("2") || rs.getString(1).equals("3") || rs.getString(1).equals("7")) { 
          prinjat = "н";
          att = Constants.Att_copy;
        }
      }
    }

    stmt = conn.prepareStatement("UPDATE Abiturient SET Prinjat = ?, TipDokSredObraz = ? WHERE KodAbiturienta LIKE ?");
    stmt.setObject(1,prinjat,Types.VARCHAR);
    stmt.setObject(2,att,Types.VARCHAR);
    stmt.setObject(3,request.getParameter("kAb"),Types.INTEGER);
    stmt.executeUpdate();

    request.setAttribute("kAb",request.getParameter("kAb"));
    form.setAction(us.getClientIntName("info","rePrinjat"));

} else if ( form.getAction().equals("rePrik")) {

/*************************************************************************/
/*****  ИЗМЕНЕНИЕ ПРИЗНАКА "В ПРИКАЗЕ" НА ПРОТИВОПОЛОЖНЫЙ ДЛЯ        *****/
/*****  КОНКРЕТНОГО АБИТУРИЕНТА В СООТВЕТСТВИИ С КОДОМ АБИТУРИЕНТА   *****/
/*************************************************************************/

// Начало транзакции
    conn.setAutoCommit(false); 
    String vPrik = new String("д");
    stmt = conn.prepareStatement("SELECT vPrikaze FROM Abiturient WHERE KodAbiturienta LIKE ?");
    stmt.setObject(1,request.getParameter("kAb"),Types.VARCHAR);
    rs = stmt.executeQuery();
    if(rs.next()) {
      if(rs.getString(1) != null) {

        if(rs.getString(1).equals("д"))      vPrik = "н";

        else if(rs.getString(1).equals("+")) vPrik = "н";

        else if(rs.getString(1).equals("в")) vPrik = "н";

      }

      if((vPrik.equals("д") && request.getParameter("ainf") !=null)) vPrik = "в";
      if(vPrik.equals("н") || vPrik.equals("в")) {

        stmt = conn.prepareStatement("UPDATE Abiturient SET vPrikaze = ?, kodspetsialnzach=null,prinjat='н' WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,vPrik,Types.VARCHAR);
        stmt.setObject(2,request.getParameter("kAb"),Types.INTEGER);
        stmt.executeUpdate();
        
        stmt = conn.prepareStatement("UPDATE Konkurs SET Zach=null WHERE KodAbiturienta LIKE ? and Zach like 'д'");
        stmt.setObject(1,request.getParameter("kAb"),Types.INTEGER);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta),KodPrikaza FROM Abit_In_Prik WHERE KodPrikaza LIKE (SELECT KodPrikaza FROM Abit_In_Prik WHERE KodAbiturienta LIKE ?) GROUP BY KodPrikaza");
        stmt.setObject(1,request.getParameter("kAb"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          if(rs.getInt(1) == 1) {

// Удаляем запись о приказе, если в нем остался один абитуриент

            stmt = conn.prepareStatement("DELETE FROM Prikaz WHERE KodPrikaza LIKE ?");
            stmt.setObject(1,rs.getString(2),Types.INTEGER);
            stmt.executeUpdate();

// И перемещаем на страницу просмотра приказов 

            move_to_prik_list = true;

          }
// Следом удаляем последнего абитуриента

          stmt = conn.prepareStatement("DELETE FROM Abit_In_Prik WHERE KodAbiturienta LIKE ?");
          stmt.setObject(1,request.getParameter("kAb"),Types.INTEGER);
          stmt.executeUpdate();

        }       
      }

// Закрепление транзакции
    conn.setAutoCommit(true);
    conn.commit();

    }

    if(move_to_prik_list) { 

      form.setAction("priks");

      move_to_prik_list = false;

    } else if(request.getParameter("fin") !=null ) {

      form.setAction(us.getClientIntName("view_prk","re_vPrik"));

    } else if(request.getParameter("ainf") !=null ) {

      request.setAttribute("kAb",request.getParameter("kAb"));
      form.setAction(us.getClientIntName("info","re_vPrik"));

    } else form.setAction(us.getClientIntName("viewing","re_vPrik"));

} else if ( form.getAction().equals("del_prik")) {

/**********************************************************/
/*****  УДАЛЕНИЕ ТЕКУЩЕГО ПРИКАЗА ИЗ СПИСКА ПРИКАЗОВ  *****/
/**********************************************************/

// Начало транзакции
    conn.setAutoCommit(false); 

    stmt = conn.prepareStatement("UPDATE Abiturient SET vPrikaze = ? WHERE KodAbiturienta IN(SELECT KodAbiturienta FROM Abit_In_Prik WHERE KodPrikaza LIKE ?)");
    stmt.setObject(1,"н",Types.VARCHAR);
    stmt.setObject(2,abit_SD.getKodPrikaza(),Types.INTEGER);
    stmt.executeUpdate();

    stmt = conn.prepareStatement("DELETE FROM Abit_In_Prik WHERE KodPrikaza LIKE ?");
    stmt.setObject(1,abit_SD.getKodPrikaza(),Types.INTEGER);
    stmt.executeUpdate();

    stmt = conn.prepareStatement("DELETE FROM Prikaz WHERE KodPrikaza LIKE ?");
    stmt.setObject(1,abit_SD.getKodPrikaza(),Types.INTEGER);
    stmt.executeUpdate();

// Закрепление транзакции
    conn.setAutoCommit(true);
    conn.commit();
   
    form.setAction(us.getClientIntName("del_prik","act"));
    form.setAction("priks");
} 


/************************************/
/*****  ВЫВОД ТАБЛИЦЫ ПРИКАЗОВ  *****/
/************************************/

   if( form.getAction().equals("priks") ) {

      int amount = 0;

      stmt = conn.prepareStatement("SELECT COUNT(p.KodPrikaza),p.Data_Prik,p.Nazvanie,p.Nom_Prik,p.Descr,p.KodPrikaza FROM Prikaz p,Abit_In_Prik ap WHERE p.KodPrikaza=ap.KodPrikaza AND p.KodVuza LIKE ? GROUP BY p.KodPrikaza,p.Data_Prik,p.Nazvanie,p.Nom_Prik,p.Descr ORDER BY p.KodPrikaza DESC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
        AbiturientBean prik = new AbiturientBean();
        amount+=rs.getInt(1);
        prik.setSpecial2(rs.getString(1));
        prik.setDataPrik(rs.getString(2));
        prik.setNazvanie(rs.getString(3));
        prik.setNomerPrik(rs.getString(4));
        prik.setDescription(rs.getString(5));
        prik.setKodPrikaza(new Integer(rs.getString(6)));
        abits_SD.add(prik);
      }
      abit_SD.setAmount(amount);
      form.setAction(us.getClientIntName("priks","view"));
   }

/*************************************************************************/
/*****    ПОДГОТОВКА ПРИКАЗА С АБИТУРИЕНТАМИ ПО ЗАДАННОМУ ПРИЗНАКУ   *****/
/*************************************************************************/

    if ( form.getAction().equals("viewing") ) {

    int number = 0;

    form.setAction(us.getClientIntName("full","view"));

    if(abit_SD.getKodFakulteta() != null) session.setAttribute("kFk",""+abit_SD.getKodFakulteta());
    if(abit_SD.getPriznakSortirovki() != null) session.setAttribute("prSort",""+abit_SD.getPriznakSortirovki());
    if(abit_SD.getNomerPrik() != null) session.setAttribute("nomPr",""+abit_SD.getNomerPrik());
    if(abit_SD.getDataPrik() != null) session.setAttribute("datPr",""+abit_SD.getDataPrik());
    if(abit_SD.getNazvanie() != null) session.setAttribute("nazvPr",""+abit_SD.getNazvanie());
    if(abit_SD.getDescription() != null) session.setAttribute("descPr",""+abit_SD.getDescription());

    abit_SD.setKodFakulteta(new Integer(""+session.getAttribute("kFk")));
    abit_SD.setPriznakSortirovki(""+session.getAttribute("prSort"));
    abit_SD.setNomerPrik(""+session.getAttribute("nomPr"));
    abit_SD.setDataPrik(""+session.getAttribute("datPr"));
    abit_SD.setNazvanie(""+session.getAttribute("nazvPr"));
    abit_SD.setDescription(StringUtil.ntv(""+session.getAttribute("descPr")));

// ЗАПРОС НА ВЫБОРКУ АБИТУРИЕНТОВ

      StringBuffer query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.KodSpetsialnZach,a.NomerPlatnogoDogovora,a.vPrikaze FROM Abiturient a WHERE a.KodSpetsialnZach IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ");
      query.append(session.getAttribute("kFk")+") AND a.Prinjat IN ('1','2','3','4','д','7') AND (a.vPrikaze IN ('в','н') OR a.vPrikaze IS NULL)");

      if(!(""+session.getAttribute("prSort")).equals("budgetniki"))
        query.append(" AND KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')");
//        query.append(" AND a.NomerPlatnogoDogovora IS NOT NULL");
      else
        query.append(" AND KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Bud LIKE 'д') AND KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')");
      
      query.append(" ORDER BY a.KodSpetsialnZach,a.Familija,a.Imja,a.Otchestvo ASC");
      stmt = conn.prepareStatement(query.toString());
      rs = stmt.executeQuery();
      while(rs.next())
      {

        AbiturientBean abit_TMP = new AbiturientBean();

        abit_TMP.setNumber(Integer.toString(++number));
        abit_TMP.setKodAbiturienta(new Integer(rs.getString(1)));
        abit_TMP.setNomerLichnogoDela(rs.getString(2));
        abit_TMP.setFamilija(rs.getString(3));
        abit_TMP.setImja(rs.getString(4));
        abit_TMP.setOtchestvo(rs.getString(5));
        abit_TMP.setIn_Prik(StringUtil.ntv(rs.getString(8)));

        if(!abit_TMP.getIn_Prik().equals("в")) {
// Проверка - присутствует ли текущий абитуриент (в приказе или является зачисленным двойником)

          stmt2 = conn.prepareStatement("SELECT a.KodAbiturienta,a.vPrikaze,a.Prinjat FROM Abiturient a JOIN Spetsialnosti s ON s.KodSpetsialnosti=a.KodSpetsialnZach WHERE a.KodVuza LIKE ? AND a.KodAbiturienta LIKE ? AND s.KodFakulteta LIKE ? AND a.KodAbiturienta IN(SELECT a1.KodAbiturienta FROM Abiturient a1 JOIN Abiturient a2 ON a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a1.Prinjat LIKE a2.Prinjat AND a1.Prinjat LIKE 'д' WHERE a1.Familija LIKE a2.Familija AND a1.Imja LIKE a2.Imja AND a1.Otchestvo LIKE a2.Otchestvo OR (a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.NomerDokumenta LIKE a2.NomerDokumenta))");
          stmt2.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          stmt2.setObject(2,rs.getString(1),Types.INTEGER);
          stmt2.setObject(3,session.getAttribute("kFk"),Types.INTEGER);
          rs2 = stmt2.executeQuery();

          if(rs2.next()) {
            abit_TMP.setIn_Prik("да");
// Убираем из готовящегося приказа тех абитуриентов, которые имеют признак == 'да'
            kods_abts_not_prik.append(","+rs.getString(1));
          }
        }

	  abits_SD.add(abit_TMP);
        session.setAttribute("not_in_prik",kods_abts_not_prik.toString());
      }

	abit_SD.setSpecial22(new Integer(number));

  } else if ( form.getAction().equals("view_prk") ) {

/*************************************************************************/
/*****************  ПРОСМОТР ГОТОВОГО К ПЕЧАТИ ПРИКАЗА  ******************/
/*************************************************************************/

    int number = 0;

    form.setAction(us.getClientIntName("view_prik","view"));

    if(request.getParameter("kodPr")!=null) abit_SD.setKodPrikaza(new Integer(""+request.getParameter("kodPr")));
    if(request.getAttribute("kodPr")!=null) abit_SD.setKodPrikaza(new Integer(""+request.getAttribute("kodPr")));

    stmt = conn.prepareStatement("SELECT p.Data_Prik,p.Nazvanie,p.Nom_Prik,p.Descr,p.KodPrikaza FROM Prikaz p WHERE p.KodPrikaza LIKE ?");
    stmt.setObject(1,abit_SD.getKodPrikaza(),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next()) {
      abit_SD.setDataPrik(rs.getString(1));
      abit_SD.setNazvanie(rs.getString(2));
      abit_SD.setNomerPrik(rs.getString(3));
      abit_SD.setDescription(StringUtil.ntv(rs.getString(4)));
      abit_SD.setKodPrikaza(new Integer(rs.getString(5)));
    }

// ЗАПРОС НА ВЫБОРКУ АБИТУРИЕНТОВ

	stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,sp.Abbreviatura,a.NomerPlatnogoDogovora,a.vPrikaze,ap.KodPrikaza FROM Spetsialnosti sp,Abiturient a,Abit_In_Prik ap WHERE a.KodSpetsialnZach=sp.KodSpetsialnosti AND ap.KodAbiturienta=a.KodAbiturienta AND a.Prinjat IN ('1','2','3','4','д','7') AND ap.KodPrikaza LIKE ? ORDER BY a.KodSpetsialnZach,a.Familija,a.Imja,a.Otchestvo ASC");
      stmt.setObject(1,abit_SD.getKodPrikaza(),Types.INTEGER);
	rs = stmt.executeQuery();
	while(rs.next())
	{
	 AbiturientBean abit_TMP = new AbiturientBean();
	 abit_TMP.setNumber(Integer.toString(++number));
	 abit_TMP.setKodAbiturienta(new Integer(rs.getString(1)));
	 abit_TMP.setNomerLichnogoDela(rs.getString(2));
	 abit_TMP.setFamilija(rs.getString(3));
	 abit_TMP.setImja(rs.getString(4));
	 abit_TMP.setOtchestvo(rs.getString(5));
	 abit_TMP.setSpecial2(rs.getString(6));
	 abit_TMP.setNomerPlatnogoDogovora(StringUtil.ntv(rs.getString(7)));
       if(rs.getString(8) != null)
         abit_TMP.setIn_Prik(rs.getString(8));
       else
         abit_TMP.setIn_Prik("н");
         abit_TMP.setKodPrikaza(new Integer(rs.getString(9)));
	 abits_SD.add(abit_TMP);
	}
	abit_SD.setSpecial22(new Integer(number));

  } else if ( form.getAction().equals("info") ) {

/**************************************************************************************/
/***  ПРОСМОТР ДОПОЛНИТЕЛЬНОЙ ИНФО ПО АБИТУРИЕНТУ, ЗАЧИСЛЕННОМУ НА 2 И БОЛЕЕ СПЕЦ.  ***/
/**************************************************************************************/

    int number = 0;

    form.setAction(us.getClientIntName("dop_info","view_dop_inf"));

// ЗАПРОС НА ВЫБОРКУ АБИТУРИЕНТОВ

	stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.TipDokSredObraz,a.Familija,a.Imja,a.Otchestvo,a.vPrikaze,a.NomerPlatnogoDogovora,a.Prinjat,s.Abbreviatura FROM Abiturient a JOIN Spetsialnosti s ON s.KodSpetsialnosti=a.KodSpetsialnZach WHERE a.KodVuza LIKE ? AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta LIKE ?"+
                                   " UNION "+
                                   "SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.TipDokSredObraz,a.Familija,a.Imja,a.Otchestvo,a.vPrikaze,a.NomerPlatnogoDogovora,a.Prinjat,s.Abbreviatura FROM Abiturient a JOIN Spetsialnosti s ON s.KodSpetsialnosti=a.KodSpetsialnZach WHERE a.KodVuza LIKE ? AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta IN(SELECT a1.KodAbiturienta FROM Abiturient a1 JOIN Abiturient a2 ON a1.KodAbiturienta NOT LIKE a2.KodAbiturienta AND a2.KodAbiturienta LIKE ? WHERE a1.Familija LIKE a2.Familija AND a1.Imja LIKE a2.Imja AND a1.Otchestvo LIKE a2.Otchestvo OR (a1.SeriaDokumenta LIKE a2.SeriaDokumenta AND a1.NomerDokumenta LIKE a2.NomerDokumenta))"+
                                   " ORDER BY s.Abbreviatura");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      stmt.setObject(2,request.getParameter("kAb"),Types.INTEGER);
      stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
      stmt.setObject(4,request.getParameter("kAb"),Types.INTEGER);
	rs = stmt.executeQuery();
	while(rs.next())
	{
	 AbiturientBean abit_TMP = new AbiturientBean();
	 abit_TMP.setNumber(Integer.toString(++number));
	 abit_TMP.setKodAbiturienta(new Integer(rs.getString(1)));
	 abit_TMP.setNomerLichnogoDela(rs.getString(2));
	 abit_TMP.setTipDokSredObraz(rs.getString(3));
	 abit_TMP.setFamilija(rs.getString(4));
	 abit_TMP.setImja(rs.getString(5));
	 abit_TMP.setOtchestvo(rs.getString(6));
       if(rs.getString(7) != null)
         abit_TMP.setIn_Prik(rs.getString(7));
       else
         abit_TMP.setIn_Prik("н");
	 abit_TMP.setNomerPlatnogoDogovora(StringUtil.ntv(rs.getString(8)));
       if(rs.getString(9) != null)
         abit_TMP.setPrinjat(rs.getString(9));
       else
         abit_TMP.setPrinjat("н");
	 abit_TMP.setAbbreviatura(rs.getString(10));
	 abits_SD.add(abit_TMP);
	}
  } else if ( form.getAction().equals("add_abit") ) {

/***********************************************************************/
/*****************  ДОБАВЛЕНИЕ АБИТУРИЕНТОВ В ПРИКАЗ  ******************/
/***********************************************************************/

    form.setAction(us.getClientIntName("add_abits","add_abt"));

// ЗАПРОС НА ВЫБОРКУ АБИТУРИЕНТОВ

    if(request.getParameter("add")!=null && abit_SD.getKodPrikaza()!=null) {
	stmt = conn.prepareStatement("SELECT KodAbiturienta,Familija,Imja,Otchestvo,Prinjat FROM Abiturient WHERE NomerLichnogoDela LIKE ?");
        stmt.setObject(1,abit_SD.getNomerLichnogoDela(),Types.VARCHAR);
	rs = stmt.executeQuery();
	if(rs.next())
	{
        if(StringUtil.ntv(rs.getString(5)).equals("д") || StringUtil.ntv(rs.getString(5)).equals("1") || StringUtil.ntv(rs.getString(5)).equals("2") || StringUtil.ntv(rs.getString(5)).equals("3") || StringUtil.ntv(rs.getString(5)).equals("4") || StringUtil.ntv(rs.getString(5)).equals("5") || StringUtil.ntv(rs.getString(5)).equals("7")) {

// Добавляем только зачисленных

          stmt2 = conn.prepareStatement("SELECT ap.KodPrikaza,p.Nom_Prik,p.Data_Prik,Descr FROM Abit_In_Prik ap, Prikaz p WHERE ap.KodPrikaza=p.KodPrikaza AND ap.KodAbiturienta LIKE ?");
          stmt2.setObject(1,rs.getString(1),Types.INTEGER);
          rs2 = stmt2.executeQuery();
          if(rs2.next()) {

            mess.setStatus("Абит. "+rs.getString(2)+" "+rs.getString(3).substring(0,1)+". "+rs.getString(4).substring(0,1)+".");
            mess.setMessage("Не был добавлен в приказ, т.к. присутствует в приказе №"+rs2.getString(2)+" от "+rs2.getString(3)+" "+StringUtil.ntv(rs2.getString(4)));

          } else {

// Абитуриент должен отсутствовать в приказе

              stmt2 = conn.prepareStatement("INSERT Abit_In_Prik(KodAbiturienta,KodPrikaza) VALUES(?,?)");
              stmt2.setObject(1, rs.getString(1),Types.INTEGER);
              stmt2.setObject(2, abit_SD.getKodPrikaza(),Types.INTEGER);
              stmt2.executeUpdate();

              stmt2 = conn.prepareStatement("UPDATE Abiturient SET vPrikaze = ? WHERE KodAbiturienta LIKE ?");
              stmt2.setObject(1, "+",Types.VARCHAR);
              stmt2.setObject(2, rs.getString(1),Types.INTEGER);
              stmt2.executeUpdate();

              mess.setStatus("Абит. "+rs.getString(2)+" "+rs.getString(3).substring(0,1)+". "+rs.getString(4).substring(0,1)+".");
              mess.setMessage("Успешно добавлен в приказ.");

            }

        } else {

          mess.setStatus("Абит. "+rs.getString(2)+" "+rs.getString(3).substring(0,1)+". "+rs.getString(4).substring(0,1)+".");
          mess.setMessage("Не был добавлен в приказ, т.к. не является зачисленным.");

        }
	} else {
          mess.setStatus("Абит. с делом № "+abit_SD.getNomerLichnogoDela());
          mess.setMessage("Не был добавлен в приказ, т.к. не найден в БД");
        }
    }
      stmt = conn.prepareStatement("SELECT p.Data_Prik,p.Nazvanie,p.Nom_Prik,p.Descr,p.KodPrikaza,COUNT(p.KodPrikaza) FROM Prikaz p JOIN Abit_In_Prik ap ON p.KodPrikaza LIKE ap.KodPrikaza WHERE p.KodPrikaza LIKE ? GROUP BY p.KodPrikaza,p.Data_Prik,p.Nazvanie,p.Nom_Prik,p.Descr");
      stmt.setObject(1,abit_SD.getKodPrikaza(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
        abit_SD.setDataPrik(rs.getString(1));
        abit_SD.setNazvanie(rs.getString(2));
        abit_SD.setNomerPrik(rs.getString(3));
        abit_SD.setDescription(StringUtil.ntv(rs.getString(4)));
        abit_SD.setKodPrikaza(new Integer(rs.getString(5)));
        abit_SD.setSpecial22(new Integer(rs.getString(6)));
      }
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
        request.setAttribute("mess", mess);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S4", abit_SD_S4);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_f) return mapping.findForward("lists_dec_f");
        return mapping.findForward("success");
   }
}