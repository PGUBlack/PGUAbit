package abit.action;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
public class ListsInfoAction extends Action{

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
ListsInfoForm        form               = (ListsInfoForm) actionForm;
AbiturientBean       abit_SD            = form.getBean(request, errors);
ActionForward        f                  = null;
ArrayList            abits_SD           = new ArrayList();
ArrayList            abit_SD_S1         = new ArrayList();
ArrayList            abit_SD_S2         = new ArrayList();
ArrayList            abit_SD_S4         = new ArrayList();
int                  count_predm        = 0;
int					 i					= 0;
String               str1               = new String();
String               str2               = new String();
int					 ks					= 0;
String               str3               = new String();
String               str4               = new String();
String               note               = new String();
String				 req			= new String();
ArrayList            notes              = new ArrayList();
ArrayList            abits 		        = new ArrayList();
UserBean             user               = (UserBean)session.getAttribute("user");

if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
msg = new ActionError( "logon.must" );
errors.add( "logon.login", msg );
}

if ( errors.empty() ) {

request.setAttribute( "listsInfoAction", new Boolean(true) );
Locale locale = new Locale("ru","RU");
session.setAttribute( Action.LOCALE_KEY, locale );
try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsInfoForm", form );

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

            } else if ( form.getAction().equals("report") || form.getAction().equals("report1")) {

/**********************************************************************************/
/*****  Если action равен otchet , то входим в секцию - создание отчёта  **********/

            	  	
	int number = 0;

        String name = new String();

	String file_con = new String();	   // Имя файла отчёта

// Cпециальность
	
	if( form.getAction().equals("report"))
	{
		req = request.getParameter("kodAbiturienta");
	} else if(form.getAction().equals("report1"))
	{
		ks = abit_SD.getKodSpetsialnosti();
		AbiturientBean abit_TMP = new AbiturientBean();
        if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
            stmt = conn.prepareStatement("SELECT a.KodAbiturienta FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k,EkzamenyNaSpetsialnosti ens WHERE k.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=k.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND k.Bud LIKE 'д' AND ens.KodSpetsialnosti LIKE ? AND zso.KodPredmeta=ens.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY 1 DESC");
          if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
            stmt = conn.prepareStatement("SELECT a.KodAbiturienta FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k,EkzamenyNaSpetsialnosti ens WHERE k.KodAbiturienta=a.KodAbiturienta AND ens.KodSpetsialnosti=k.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND k.Dog LIKE 'д' AND ens.KodSpetsialnosti LIKE ? AND zso.KodPredmeta=ens.KodPredmeta AND a.DokumentyHranjatsja LIKE 'д' GROUP BY a.KodAbiturienta,k.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz ORDER BY 1 DESC");

          stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {

              notes = new ArrayList();

              abit_TMP = new AbiturientBean();
              abit_TMP.setNumber(Integer.toString(++number));
              abit_TMP.setKodAbiturienta(new Integer(rs.getString(1)));
              
      		req = req + "," + abit_TMP.getKodAbiturienta();
     		i++;

              abits_SD.add(abit_TMP);
          }
          if (req!= null) req = req.substring(1);
	}
        if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
        {
	  file_con = "lsts_sp_kon_"+StringUtil.CurrDate("-");

	} else {

	  file_con = "lsts_sp_bd_"+StringUtil.CurrDate("-");
	}


              name = "Справка о результатах ЕГЭ на "+StringUtil.CurrDate(".");


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
   
   /************************************************/

     report.write("\\par\\qc\\fs22\\tab\\b{СПРАВКА}\\b0\n");        
     report.write("\\par\\qc\\fs18\\tab\\b{о результатах единого государственного экзамена}\\b0\\n");
     
     // Добавление ФИО
     String tip_Dok3 = new String();
     boolean oblastIsEmpty3 = false;
     stmt = conn.prepareStatement("SELECT kodOblastiP FROM Abiturient where kodAbiturienta IN ("+req+")");
     rs = stmt.executeQuery();
     if(rs.next()){
    	 if(rs.getString(1).equals("-")) oblastIsEmpty3 = true;
     }
     
  // Добавление ФИО
     
     if (oblastIsEmpty3){
     stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,a.gorod_prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija, KodAbiturienta FROM Abiturient a WHERE KodAbiturienta IN ("+req+")");
	      	 // 16, их было 16!
     }
     else{
     stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija, KodAbiturienta FROM Abiturient a, kladr k WHERE KodAbiturienta IN ("+req+") and k.code = a.gorod_prop");
     }
     rs = stmt.executeQuery();
     while(rs.next()) {
     report.write("\\par\\qc\\fs22\\tab{ }\\b{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\b0\n");
     
     
     report.write("\\par\\qc\\fs18\\tab{документ, удостоверяющий личность:  }{"+rs.getString(11)+"  "+rs.getString(12)+"}\n"); 
      
     report.write("\\par\\qc\\fs18\\tab{ }\n"); 
     report.write("\\par\\qc\\fs18\\tab{по результатам сдачи единого государственного экзамена обнаружил(а)}\n");
     report.write("\\par\\qc\\fs18\\tab{следующие знания по общеобразовательным предметам:}\n");
     
  // сформировать таблицу по 3 предметам ЕГЭ
     report.write("\\par\\qc\\fs18\\tab{ }\n"); 

      {

       report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      report.write("\\intbl\\qc\\b{Общеобразовательные предметы}\\b0\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx4000\n");	 
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx5500\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx7000\n");	 
      report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");

      report.write("\\intbl\\ql{\\tab\\b{       Наименование предмета}\\b0}\\cell\n");
      report.write("\\intbl\\qc\\b{Балл}\\b0\\cell\n");
      report.write("\\intbl\\qc\\b{Год сдачи}\\b0\\cell\n");
      report.write("\\intbl\\qc\\b{Статус результата}\\b0\\cell\n");           
      report.write("\\intbl\\qc\\b{Номер свидетельства}\\b0\\cell\n");	           
      report.write("\\intbl\\row\n");

      report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4000\n");	
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5500\n");	
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7000\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");

        int first_line = 0;
        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta,zso.god FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta like '"+rs.getString(17)+"' AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
// '"+rs.getString(1)+"' 
        rs2 = stmt.executeQuery();
        while(rs2.next()) {
           if(first_line++ > 0) {
             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");                  
           }

           report.write("\\intbl\\ql{"+rs2.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs2.getString(2)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs2.getString(4)+"}\\cell\n");
           report.write("\\intbl\\qc{ }\\cell\n");
           report.write("\\intbl\\qc{ }\\cell\n");
           report.write("\\intbl\\row\n");
        
    	  }
      
      
      
      report.write("\\pard\\par\n");
}
      

     report.write("\\par\\par\\ql\\fs22\\tab{Справка для личного дела абитуриента сформирована из ФИС ГИА и приема для образовательной организации:}\n");
     report.write("\\par\\par\\ql\\fs22\\tab{Федеральное государственное бюджетное образовательное учреждение высшего профессионального образования ''Пензенский государственный университет''}\n");
     report.write("\\par\\par\\ql\\fs22\\tab{Дата и время формирования справки: }{"+StringUtil.CurrDate(".")+"}{    }{"+StringUtil.CurrTime(".")+"}\n");
     report.write("\\par\\par\\ql\\fs22\\tab{ }\n");  
     report.write("\\par\\par\\ql\\fs22\\tab{Лицо, сформировавшее справку: }\n");             
     report.write("\\par\\par\\ql\\fs22\\tab\\i{____________________}{                    }{____________________}{                    }{____________________}\\i0\n");
     report.write("\\par\\qc\\fs2\\tab{ }\n");
     report.write("\\ql\\fs16\\tab\\b{(должность)}\\tab\\tab\\tab\\tab\\b{(подпись)}\\tab\\tab\\tab\\tab\\b{(Фамилия И.О.)}\\b0\n");
     report.write("\\par\\par\\ql\\fs22\\tab{Ответственное лицо приёмной комиссии: }\n");             
     report.write("\\par\\par\\ql\\fs22\\tab\\i{____________________}{                    }{____________________}{                    }{____________________}\\i0\n");
     report.write("\\par\\qc\\fs2\\tab{ }\n");
     report.write("\\ql\\fs16\\tab\\b{(должность)}\\tab\\tab\\tab\\tab\\b{(подпись)}\\tab\\tab\\tab\\tab\\b{(Фамилия И.О.)}\\b0\n");
     report.write("\\par\\qc\\fs22\\tab{ }\n"); 
     report.write("\\par\\qc\\fs22\\tab{ }\n");
     report.write("\\par\\qc\\fs22\\tab{М.П.}\n"); 
     report.write("\\par\\qc\\fs22\\tab{ }\n");
     report.write("\\par\\qc\\fs22\\tab{ }\n");
     report.write("\\par\\par\\ql\\fs22\\tab{Дата выдачи ''___''________________ ____ г.   регистрационный № ______________}\\page");  
     }
     report.write("}");
     report.close();
     session.removeAttribute("kodAbiturienta");
     req = null;
     form.setAction(us.getClientIntName("new_rep","crt"));
     return mapping.findForward("rep_brw");

   } else if ( form.getAction().equals("viewing")) {

/************************************************************************************************/
/*****  Если action равен viewing , то входим в секцию - создание записи или вывод таблицы  *****/

                 int number = 0;
                 int kol_predm = 0;              

                 form.setAction(us.getClientIntName("full","view"));
                 
                 //TODO : abit_SD.setKodSpetsialnosti(new Integer()); <----- вынимаем код специальности, потом по этому запросу сформировать еще один список.

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
//TODO here bug stops
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
                     
             		req = req + "," + abit_TMP.getKodAbiturienta();
            		i++;

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
                 if (req!=null && !req.equals("")) req = req.substring(1);
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
        if(!errors.empty()) return mapping.findForward("error");

        return mapping.findForward("success");
        
    }
	
}
