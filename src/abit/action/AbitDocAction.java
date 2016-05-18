package abit.action;

import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*; 
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import javax.naming.*; 
import javax.sql.*;
import abit.bean.*; 
import abit.Constants;
import abit.util.*;
import abit.sql.*; 
import java.io.FileWriter;
import java.io.FileOutputStream;

public class AbitDocAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response)

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        ResultSet         rs            = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        AbiturientForm    form          = (AbiturientForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        MessageBean       mess          = new MessageBean();
        boolean           error         = false;
        ActionForward     f             = null;
        UserBean          user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || !(user.getGroup().getTypeId()==1 || user.getGroup().getTypeId()==3)) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "abitDocAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "abiturientForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************************************************************************************************/
/************************ Формирование стандартного пакета документов ***************************/
/************************************************************************************************/

 if ( form.getAction().equals("reports") ) {

    abit_A.setFileName1("-");
    abit_A.setFileName2("-");
    abit_A.setFileName3("-");   

// Получение из БД дополнительных сведений по коду абитуриента

 /*   stmt = conn.prepareStatement("SELECT s.Tip_Spec FROM Abiturient a,Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta LIKE ?");
    stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) {
      abit_A.setTip_Spec(rs.getString(1));
    } else {
      abit_A.setTip_Spec("-");
    }

    stmt = conn.prepareStatement("SELECT a.Need_Spo FROM Abiturient a WHERE a.KodAbiturienta LIKE ?");
    stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) {
      abit_A.setNeed_Spo(rs.getString(1));
    } else {
      abit_A.setNeed_Spo("-");
    }
    
    stmt = conn.prepareStatement("SELECT a.Internship,a.Traineeship,a.PostgraduateStudies FROM Abiturient a WHERE a.KodAbiturienta LIKE ?");
    stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) {
      abit_A.setInternship(rs.getString(1));
      if(rs.getString(1)==null){abit_A.setInternship("-"); }
      abit_A.setTraineeship(rs.getString(2));
      if(rs.getString(2)==null){abit_A.setTraineeship("-"); }
      abit_A.setPostgraduateStudies(rs.getString(3));
      if(rs.getString(3)==null){abit_A.setPostgraduateStudies("-");}
      
      
    } else {
    	abit_A.setInternship("-");
    	abit_A.setTraineeship("-");
    	abit_A.setPostgraduateStudies("-");
    }
   
*/
//System.out.println("1>"+StringUtil.toEng(abit_A.getTip_Spec()));
//System.out.println("2>"+StringUtil.toEng(abit_A.getNeed_Spo()));
    stmt = conn.prepareStatement("SELECT nomerPotoka from abiturient where kodAbiturienta like ? ");
    stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) {
    	abit_A.setNomerPotoka(rs.getInt(1));
    }
    
//   if ( abit_A.getTip_Spec().equals("м") && !abit_A.getNeed_Spo().equals("д")) {
 
  
   // if ( abit_A.getTip_Spec().equals("о") && abit_A.getInternship().equals("д")) {//&& abit_A.getInternship().equals("д")
    	 if ( abit_A.getNomerPotoka() == 3) {//&& abit_A.getInternship().equals("д")

    	/************************************************/
    	/************************************************/
    	/******        ЗАЯВЛЕНИЕ в интернатуру(и) *******/
    	/************************************************/
    	/************************************************/
    	 String file_con = new String("statementINT"+abit_A.getKodAbiturienta()+".rtf");
    	 
         String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;


         abit_A.setFileName1(file_con);

         BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

   /************************************************/

         report.write("{\\rtf1\\ansi\n");
         report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
         stmt = conn.prepareStatement("SELECT NazvanieRoditFull FROM NazvanieVuza WHERE KodVuza LIKE ?");
         stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) report.write("\\fs24\\b\\qc{Ректору }{"+rs.getString(1)+"}\\b0\n");

         report.write("\\par\\par\n");

   /************************************************/

         String tip_Dok = new String();
         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           report.write("\\fs22\\ql{от }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{гражданин(ка): }\\i{"+rs.getString(4)+"}\\i0,  дата рождения: \\i{"+rs.getString(5)+"г.}\\i0,\n");//\\i0{, место рождения: }\\i{"+rs.getString(16)+"}
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{проживающий(щая) по адресу: }\\i{"+rs.getString(6)+", ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           if(rs.getString(10) != null && rs.getString(10).equals("п"))
             tip_Dok = "Паспорт(серия-№)";
           else
             tip_Dok = "справка";
           report.write("\\i{"+tip_Dok+" }{"+rs.getString(11)+" № "+rs.getString(12)+", выдан: "+rs.getString(13)+", "+rs.getString(14)+"г.}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Контактный телефон: }\\i{"+rs.getString(15)+"}\\i0\n");
           report.write("\\par\\par\n");
         }

   /************************************************/
         report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("м")) forma_Ob = "очной";
           else forma_Ob = "заочной";

           if(rs.getString(3) != null && rs.getString(3).equals("д")) {osnova_Ob1 = "на места, финансируемые из бюджета";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("д")) {osnova_Ob2 = "на платной основе";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " и " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на программу интернатуры:}\n");
           report.write("\\par\n");
       //    report.write("\\par\\i\\tab{по "+forma_Ob+" форме обучения}\\i0\n");//"+osnova_Ob+"
       //    report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("д")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//             } else {
         	
             
             report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\qc\\b{Направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

         	 
             int first_line = 0;
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
             stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
             rs = stmt.executeQuery();
             while(rs.next()) {
                if(first_line++ > 0) {
                  report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
                  //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
                  //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
                }
    
                report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
                report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
                report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
                report.write("\\intbl\\row\n");
             
         	  }
            
           }
         
           report.write("\\pard\\par\n");
///////////////////////////////////////////////
       /*  report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("м")) forma_Ob = "очной";
           else forma_Ob = "заочной";

           if(rs.getString(3) != null && rs.getString(3).equals("д")) {osnova_Ob1 = "на места, финансируемые из бюджета";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("д")) {osnova_Ob2 = "на платной основе";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " и " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на программу интернатуры: }\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}\\'bb\\i0\n");
           //report.write("\\par\n");
           //report.write("\\par\\i\\tab{по "+forma_Ob+" форме обучения}\\i0\n");//"+osnova_Ob+"
           //report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("д")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//             } else {
         	
             
             //report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             //report.write("\\intbl\\qc\\b{Направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
             //report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{Полное наименование программы}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

         	 
             int first_line = 0;
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
             stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
             rs = stmt.executeQuery();
             while(rs.next()) {
                if(first_line++ > 0) {
                  report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
                  //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
                  //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
                }
    
                report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
                report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
                report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
                report.write("\\intbl\\row\n");
             
         	  }
            
           }
         
           report.write("\\pard\\par\n");*/
         
   /************************************************/
         report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

         String tsel = new String();
         String tip_Ok_Zav = new String();
         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,tp.TselevojPriem, a.TipDokSredObraz FROM TselevojPriem tp, Abiturient a, Zavedenija z WHERE a.kodtselevogopriema=tp.kodtselevogopriema AND a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

             tip_Ok_Zav = "общеобразовательное учреждение";

           } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

             tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

           } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

             tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";

           } else if(rs.getString(2) != null && rs.getString(2).equals("у")) {

             tip_Ok_Zav = "высшее учебное заведение";
           }
           
           String DokSrObr = null;
           if(rs.getString(9) != null && rs.getString(9).equals("о"))
               DokSrObr = " (оригинал)";
             else
               DokSrObr = " (копия)";

           report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i   № "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
           tsel=rs.getString(8);
         }
         
         
         /************************************************/
         
         
         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         String predpr = new String();
         report.write("\\b0\\fs6\\par\n");
         report.write("\\par\\ql\\fs22{Целевое направление }:  ");//\\i0{ для участия в целевом конкурсе
         while(rs.next()) {
           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
           //  report.write("\\b0\\fs6\\par\n");
          
             if(rs.getString(1).equals("2")) predpr = "Росатом";
             else if(rs.getString(1).equals("3")) predpr = "Роскосмос";
             else if(rs.getString(1).equals("4")) predpr = "Минпромторг";
             else if(rs.getString(1).equals("1")) predpr = "";
             else predpr = "другое";
             report.write("\\i{"+predpr+"   }");//\\i0{ для участия в целевом конкурсе
           }
         }
         report.write("\\i0\n");
         
         

         String in_Jaz = new String();
         String need_Obshaga = new String();
         stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
        	in_Jaz = rs.getString(1);
           if(rs.getString(1) != null && rs.getString(1).equals("а")) {

             in_Jaz = "английский";

           } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

             in_Jaz = "немецкий";

           } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

             in_Jaz = "французский";
           }
          

           if(rs.getString(2) != null && rs.getString(2).equals("д")) {

             need_Obshaga = "нуждаюсь";

           } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

             need_Obshaga = "не нуждаюсь";
           }
//           report.write("\\par\n");

          
           report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{Целевое направление: }\\i{"+tsel+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{Сведения о наличии опубликованных работ, изобретений и отчетов по НИР___________________________}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
           
           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
            // if(rs.getString(2) != null && rs.getString(2).length() > 3)
              //  report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
             if(rs.getString(1) != null && rs.getString(1).length() > 3){
               report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
           }else{
        	   report.write("\\par\\ql\\fs22{Индивидуальные достижения: Нет}\\fs6\\par\n");
           }
         }
         }
   /************************************************/
         
         stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
         	
         if(rs.getString(1)!=null){
            report.write("\\par\\ql\\fs22\\tab{В соответствии с разделом 8 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительного экзамена в магистратуру:}\n");
            report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
         else{
        	 report.write("\\par\\ql\\fs22\\tab{В соответствии с разделом 8 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительного экзамена в магистратуру:}\n");
             report.write("\\par\\ql\\fs22\\tab\\i{Нет}\\i0");}
         }
         
         
         
         /************************************************/

         //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
         //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\par\n");
            stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
            stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
          	  if(rs.getString(1) != null){
            report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
            report.write("\\par\\ql\\fs22{оригиналов документов) }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
            //report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+"   Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");
            }
            /************************************************/

            //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
            //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\par\n");
               //stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
               //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
               //rs = stmt.executeQuery();
               //if(rs.next()) {
             	//  if(rs.getString(1) != null){
               //report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
               //report.write("\\par\\ql\\fs22{оригиналов документов)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
               //report.write("\\par\\par\n");
               //report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+" Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
               //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
               //report.write("\\par\\par\n");
             //  }
            report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{С копией лицензии на осуществление трудовой деятельности(с приложениями) серия 90ЛО1 №0000704, от}");
	         report.write("\\par\\ql\\fs22{21 марта 2013 г.; с копией свидетельства о государственной аккредитации (с приложениями) от 17.05.2013 г.}");  
	         report.write("\\par\\ql\\fs22{серия 90АО1 №0000631 рег.№0627; c информацией о предоставляемых поступающим особых правах и }\n");
	         report.write("\\par\\ql\\fs22{преимуществах при приеме на обучение по программам бакалавриата и программам специалитета с датами}\n");
	         report.write("\\par\\ql\\fs22{завершения представления поступающими оригинала документа установленного образца на каждом этапе}\n");
	         report.write("\\par\\ql\\fs22{зачисления на места в рамках контрольных цифр, с датами завершения представления поступающими сведений о}\n");
	         report.write("\\par\\ql\\fs22{согласии на зачисление на места по договорам об оказании платных образовательных услуг; с правилами подачи}\n");
	         report.write("\\par\\ql\\fs22{апелляции по результатам вступительных испытаний, проводимых организацией самостоятельно ознакомлен(а)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\n");
               
               //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
               //report.write("\\pard\\page\n");
               
               /************************************************/

            /*   report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
               report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");*/
               report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{Согласен на обработку своих персональных данных                                            }\\i0{_________________}\\i0\n"); 
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

         /************************************************/
              // report.write("\\par\\par\\ql\\fs22\\tab{Высшее профессиональное образование получаю }\\i{впервые.}\\i0\n");
              //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
              //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               
               report.write("\\par\\par\\par\n");
             
               report.write("\\par\\par\\ql\\fs22\\tab{Ознакомлен(а) с информацией об ответственности за достоверность сведений, указываемых в заявлении о }\n");
               report.write("\\par\\par\\ql\\fs22\\tab{приеме, и за подлинность документов, подаваемых для поступления                }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               report.write("\\par\\par\\par\n");

         /************************************************/
               report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю получение высшего образования по программам интернатуры впервые}\n");
               report.write("\\par\\par\\ql\\fs22\\tab{(при поступлении на обучение на места в рамках контрольных цифр)              }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               report.write("\\par\\par\\par\n");
               
              
             

            
      /************************************************/

            //report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");

      /************************************************/

            //report.write("\\ql\\fs22\\tab{Степень магистра получаю }\\i{впервые.}\\i0\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\par\\qj\\fs22\\tab{С Лицензией на право осуществления образовательной деятельности серия 90ЛО1 №0000704, от 21 марта 2013 г., свидетельством о государственной аккредитации от 25.06.2012 серия 90АО1 № 000007 рег.№0007 и приложений к ним, программами вступительных испытаний, правилами приёма ПГУ, правилами проведения апелляций ознакомлен(а).}\n");
            //report.write("\\par\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab\\b{Ознакомлен(а) с датой предоставления оригинала документа государственного образца об образовании в соответствии с порядком зачисления (п.14) правил приема.}\\b0\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22\\tab{Дата: "+StringUtil.CurrDate(".")+"г.}\n");

            report.write("}"); 
            report.close();
    }
    

    /************************************************/

    // if ( abit_A.getTip_Spec().equals("о") && abit_A.getPostgraduateStudies().equals("д") ) {//&& abit_A.getPostgraduateStudies().equals("д")
    	
    	 if ( abit_A.getNomerPotoka() == 5) {//&
    	 /************************************************/
    	/************************************************/
    	/******        ЗАЯВЛЕНИЕ в аспирантуру  (о)   *******/
    	/************************************************/
    	/************************************************/
    	 String file_con = new String("statementASP"+abit_A.getKodAbiturienta()+".rtf");
    	 
         String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;


         abit_A.setFileName1(file_con);

         BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

   /************************************************/

         report.write("{\\rtf1\\ansi\n");
         report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
         stmt = conn.prepareStatement("SELECT NazvanieRoditFull FROM NazvanieVuza WHERE KodVuza LIKE ?");
         stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) report.write("\\fs24\\b\\qc{Ректору }{"+rs.getString(1)+"}\\b0\n");

         report.write("\\par\\par\n");

   /************************************************/

         String tip_Dok = new String();
         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE  KodAbiturienta LIKE ? and k.code = a.gorod_prop");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           report.write("\\fs22\\ql{от }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{гражданин(ка): }\\i{"+rs.getString(4)+"}\\i0,  дата рождения: \\i{"+rs.getString(5)+"г.}\\i0,\n");//\\i0{, место рождения: }\\i{"+rs.getString(16)+"}
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{проживающий(щая) по адресу: }\\i{"+rs.getString(6)+", ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           if(rs.getString(10) != null && rs.getString(10).equals("п"))
             tip_Dok = "Паспорт(серия-№)";
           else
             tip_Dok = "справка";
           report.write("\\i{"+tip_Dok+" }{"+rs.getString(11)+" № "+rs.getString(12)+", выдан: "+rs.getString(13)+", "+rs.getString(14)+"г.}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Контактный телефон: }\\i{"+rs.getString(15)+"}\\i0\n");
           report.write("\\par\\par\n");
         }

   /************************************************/
         report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
         report.write("\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("м")) forma_Ob = "очной";
           else forma_Ob = "заочной";

           if(rs.getString(3) != null && rs.getString(3).equals("д")) {osnova_Ob1 = "на места, финансируемые из бюджета";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("д")) {osnova_Ob2 = "на платной основе";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " и " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на программу подготовки научно-педагогических кадров в аспирантуре: }\n"); //\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}
           report.write("\\par\n");
         //  report.write("\\par\\i\\tab{по "+forma_Ob+" форме обучения}\\i0\n");//"+osnova_Ob+"
      //     report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п','з') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("д")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//             } else {
         	
             
             report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\qc\\b{Направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

         	 
             int first_line = 0;
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п','з') ORDER BY kon.Prioritet ASC");
             stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
             rs = stmt.executeQuery();
             while(rs.next()) {
                if(first_line++ > 0) {
                  report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
                  //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
                  //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
                }
    
                report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
                report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
                report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
                report.write("\\intbl\\row\n");
             
         	  }
            
           }
         
           report.write("\\pard\\par\n");
         /////////////////////////

       /*  report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("м")) forma_Ob = "очной";
           else forma_Ob = "заочной";

           if(rs.getString(3) != null && rs.getString(3).equals("д")) {osnova_Ob1 = "на места, финансируемые из бюджета";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("д")) {osnova_Ob2 = "на платной основе";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " и " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на программу подготовки научно-педагогических кадров в аспирантуре: }\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}\\'bb\\i0\n");
           //report.write("\\par\n");
           //report.write("\\par\\i\\tab{по "+forma_Ob+" форме обучения}\\i0\n");//"+osnova_Ob+"
           //report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("д")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//             } else {
         	
             
             //report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             //report.write("\\intbl\\qc\\b{Направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
             //report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

         	 
             int first_line = 0;
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
             stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
             rs = stmt.executeQuery();
             while(rs.next()) {
                if(first_line++ > 0) {
                  report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
                  //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
                  //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
                }
    
                report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
                report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
                report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
                report.write("\\intbl\\row\n");
             
         	  }
            
           }
         
           report.write("\\pard\\par\n");*/
         
   /************************************************/
         report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

         
         String tip_Ok_Zav = new String();
         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata, a.TipDokSredObraz FROM Abiturient a, Zavedenija z WHERE a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

             tip_Ok_Zav = "общеобразовательное учреждение";

           } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

             tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

           } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

             tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";

           } else if(rs.getString(2) != null && rs.getString(2).equals("у")) {

             tip_Ok_Zav = "высшее учебное заведение";
           }

           String DokSrObr = null;
           if(rs.getString(8) != null && rs.getString(8).equals("о"))
               DokSrObr = " (оригинал)";
             else
               DokSrObr = " (копия)";
           
           
           report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  № "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
         }
         
         
         /************************************************/
         
         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         String predpr = new String();
         report.write("\\b0\\fs6\\par\n");
         report.write("\\par\\ql\\fs22{Целевое направление }:  ");//\\i0{ для участия в целевом конкурсе
         while(rs.next()) {
           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
           //  report.write("\\b0\\fs6\\par\n");
          
             if(rs.getString(1).equals("2")) predpr = "Росатом";
             else if(rs.getString(1).equals("3")) predpr = "Роскосмос";
             else if(rs.getString(1).equals("4")) predpr = "Минпромторг";
             else if(rs.getString(1).equals("1")) predpr = "";
             else predpr = "другое";
             report.write("\\i{"+predpr+"   }");//\\i0{ для участия в целевом конкурсе
           }
         }
         report.write("\\i0\n");
         

         String in_Jaz = new String();
         String need_Obshaga = new String();
         stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
        	 in_Jaz = rs.getString(1);
           if(rs.getString(1) != null && rs.getString(1).equals("а")) {

             in_Jaz = "английский";

           } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

             in_Jaz = "немецкий";

           } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

             in_Jaz = "французский";
           }

           if(rs.getString(2) != null && rs.getString(2).equals("д")) {

             need_Obshaga = "нуждаюсь";

           } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

             need_Obshaga = "не нуждаюсь";
           }
//           report.write("\\par\n");

          
           report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{Сведения о наличии опубликованных работ, изобретений и отчетов по НИР___________________________}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
           
           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
            // if(rs.getString(2) != null && rs.getString(2).length() > 3)
              //  report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
             if(rs.getString(1) != null && rs.getString(1).length() > 3)
               report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
           }
         }

   /************************************************/
         
         stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
         	
         if(rs.getString(1)!=null){
            report.write("\\par\\ql\\fs22\\tab{В соответствии с разделом 7 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительного экзамена в магистратуру:}\n");
            report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
         }
         
         
         /************************************************/

         //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
         //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\par\n");
            stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
            stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
          	  if(rs.getString(1) != null){
            report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
            report.write("\\par\\ql\\fs22{оригиналов документов) }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
            //report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+"   Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");
            }
            /************************************************/

            //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
            //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\par\n");
             //  stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
              // stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
              // rs = stmt.executeQuery();
              // if(rs.next()) {
             	 // if(rs.getString(1) != null){
              // report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
              // report.write("\\par\\ql\\fs22{оригиналов документов)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
               //report.write("\\par\\par\n");
              // report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+" Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
               //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
               //report.write("\\par\\par\n");
              // }
            report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{С копией лицензии на осуществление трудовой деятельности(с приложениями) серия 90ЛО1 №0000704, от}");
	         report.write("\\par\\ql\\fs22{21 марта 2013 г.; с копией свидетельства о государственной аккредитации (с приложениями) от 17.05.2013 г.}");  
	         report.write("\\par\\ql\\fs22{серия 90АО1 №0000631 рег.№0627; c информацией о предоставляемых поступающим особых правах и }\n");
	         report.write("\\par\\ql\\fs22{преимуществах при приеме на обучение по программам бакалавриата и программам специалитета с датами}\n");
	         report.write("\\par\\ql\\fs22{завершения представления поступающими оригинала документа установленного образца на каждом этапе}\n");
	         report.write("\\par\\ql\\fs22{зачисления на места в рамках контрольных цифр, с датами завершения представления поступающими сведений о}\n");
	         report.write("\\par\\ql\\fs22{согласии на зачисление на места по договорам об оказании платных образовательных услуг; с правилами подачи}\n");
	         report.write("\\par\\ql\\fs22{апелляции по результатам вступительных испытаний, проводимых организацией самостоятельно ознакомлен(а)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\n");
               
               //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
               //report.write("\\pard\\page\n");
               
               /************************************************/

            /*   report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
               report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");*/
               report.write("\\par\n");
               report.write("\\qj\\fs22\\tab{Согласен на обработку своих персональных данных                                          }\\'ab{_________________}\\'bb.\n"); 
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

         /************************************************/
              // report.write("\\par\\par\\ql\\fs22\\tab{Высшее профессиональное образование получаю }\\i{впервые.}\\i0\n");
              //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
              //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               
               report.write("\\par\\par\\par\n");
             
               report.write("\\par\\par\\ql\\fs22\\tab{Ознакомлен(а) с информацией об ответственности за достоверность сведений, указываемых в заявлении о }\n");
               report.write("\\par\\par\\ql\\fs22\\tab{приеме, и за подлинность документов, подаваемых для поступления                }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               report.write("\\par\\par\\par\n");

         /************************************************/
               report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю получение высшего образования данного уровня впервые}\n");
               report.write("\\par\\par\\ql\\fs22\\tab{(при поступлении на обучение на места в рамках контрольных цифр)               }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               report.write("\\par\\par\\par\n");
               
              
             

            
      /************************************************/

            //report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");

      /************************************************/

            //report.write("\\ql\\fs22\\tab{Степень магистра получаю }\\i{впервые.}\\i0\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\par\\qj\\fs22\\tab{С Лицензией на право осуществления образовательной деятельности серия 90ЛО1 №0000704, от 21 марта 2013 г., свидетельством о государственной аккредитации от 25.06.2012 серия 90АО1 № 000007 рег.№0007 и приложений к ним, программами вступительных испытаний, правилами приёма ПГУ, правилами проведения апелляций ознакомлен(а).}\n");
            //report.write("\\par\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab\\b{Ознакомлен(а) с датой предоставления оригинала документа государственного образца об образовании в соответствии с порядком зачисления (п.14) правил приема.}\\b0\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22\\tab{Дата: "+StringUtil.CurrDate(".")+"г.}\n");

            report.write("}"); 
            report.close();
    }
    

    /************************************************/
  
 //    if ( abit_A.getTip_Spec().equals("о") && abit_A.getTraineeship().equals("д")) {//&& abit_A.getTraineeship().equals("д")
    	 if ( abit_A.getNomerPotoka() == 4) {//&
    	/************************************************/
    	/************************************************/
    	/******        ЗАЯВЛЕНИЕ в ординатуру   (д)   *******/
    	/************************************************/
    	/************************************************/
    	 String file_con = new String("statementORD"+abit_A.getKodAbiturienta()+".rtf");
    	 
         String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;


         abit_A.setFileName1(file_con);

         BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

   /************************************************/

         report.write("{\\rtf1\\ansi\n");
         report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
         stmt = conn.prepareStatement("SELECT NazvanieRoditFull FROM NazvanieVuza WHERE KodVuza LIKE ?");
         stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) report.write("\\fs24\\b\\qc{Ректору }{"+rs.getString(1)+"}\\b0\n");

         report.write("\\par\\par\n");

   /************************************************/

         String tip_Dok = new String();
         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           report.write("\\fs22\\ql{от }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{гражданин(ка): }\\i{"+rs.getString(4)+"}\\i0,  дата рождения: \\i{"+rs.getString(5)+"г.}\\i0,\n");//\\i0{, место рождения: }\\i{"+rs.getString(16)+"}
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{проживающий(щая) по адресу: }\\i{"+rs.getString(6)+", ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           if(rs.getString(10) != null && rs.getString(10).equals("п"))
             tip_Dok = "Паспорт(серия-№)";
           else
             tip_Dok = "справка";
           report.write("\\i{"+tip_Dok+" }{"+rs.getString(11)+" № "+rs.getString(12)+", выдан: "+rs.getString(13)+", "+rs.getString(14)+"г.}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Контактный телефон: }\\i{"+rs.getString(15)+"}\\i0\n");
           report.write("\\par\\par\n");
         }

   /************************************************/
         report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
         report.write("\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("м")) forma_Ob = "очной";
           else forma_Ob = "заочной";

           if(rs.getString(3) != null && rs.getString(3).equals("д")) {osnova_Ob1 = "на места, финансируемые из бюджета";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("д")) {osnova_Ob2 = "на платной основе";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " и " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на программу ординатуры: }\n");//\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}\\'bb\\i0
           report.write("\\par\n");
         // report.write("\\par\\i\\tab{по "+forma_Ob+" форме обучения}\\i0\n");//"+osnova_Ob+"
       //   report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("д")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//             } else {
         	
             
             report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\qc\\b{Направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

         	 
             int first_line = 0;
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
             stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
             rs = stmt.executeQuery();
             while(rs.next()) {
                if(first_line++ > 0) {
                  report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
                  //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
                  //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
                }
    
                report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
                report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
                report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
                report.write("\\intbl\\row\n");
             
         	  }
            
           }
         
           report.write("\\pard\\par\n");
         
   //////////////////////////////////////////////////////////////      

       /*  report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("м")) forma_Ob = "очной";
           else forma_Ob = "заочной";

           if(rs.getString(3) != null && rs.getString(3).equals("д")) {osnova_Ob1 = "на места, финансируемые из бюджета";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("д")) {osnova_Ob2 = "на платной основе";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " и " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на программу ординатуры: }\\i{"+rs.getString(1)+"} \\i0{"+rs.getString(2)+"}\\i0\n");
           //report.write("\\par\n");
           //report.write("\\par\\i\\tab{по "+forma_Ob+" форме обучения}\\i0\n");//"+osnova_Ob+"
           //report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("д")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//             } else {
         	
             
             //report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             //report.write("\\intbl\\qc\\b{Направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
             //report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

         	 
             int first_line = 0;
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
             stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
             rs = stmt.executeQuery();
             while(rs.next()) {
                if(first_line++ > 0) {
                  report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
                  //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
                  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
                  //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
                }
    
                report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
                report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
                report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
                report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
                //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
                report.write("\\intbl\\row\n");
             
         	  }
            
           }
         
           report.write("\\pard\\par\n");*/
         
   /************************************************/
         report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

         String tsel1=new String();
         String tip_Ok_Zav = new String();
         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,tp.TselevojPriem, a.tipDokSredObraz FROM Tselevojpriem tp, Abiturient a, Zavedenija z WHERE tp.kodtselevogopriema=a.kodtselevogopriema AND a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

             tip_Ok_Zav = "общеобразовательное учреждение";

           } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

             tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

           } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

             tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";

           } else if(rs.getString(2) != null && rs.getString(2).equals("у")) {

             tip_Ok_Zav = "высшее учебное заведение";
           }
           tsel1=rs.getString(8);
           
           String DokSrObr = null;
           if(rs.getString(9) != null && rs.getString(9).equals("о"))
               DokSrObr = " (оригинал)";
             else
               DokSrObr = " (копия)";
           report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i   № "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
         }
         
         
         /************************************************/
         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         String predpr = new String();
         report.write("\\b0\\fs6\\par\n");
         report.write("\\par\\ql\\fs22{Целевое направление }:  ");//\\i0{ для участия в целевом конкурсе
         while(rs.next()) {
           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
           //  report.write("\\b0\\fs6\\par\n");
          
             if(rs.getString(1).equals("2")) predpr = "Росатом";
             else if(rs.getString(1).equals("3")) predpr = "Роскосмос";
             else if(rs.getString(1).equals("4")) predpr = "Минпромторг";
             else if(rs.getString(1).equals("1")) predpr = "";
             else predpr = "другое";
             report.write("\\i{"+predpr+"   }");//\\i0{ для участия в целевом конкурсе
           }
         }
         report.write("\\i0\n");
         
         

         String in_Jaz = new String();
         String need_Obshaga = new String();
         stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
        	 in_Jaz = rs.getString(1);
           if(rs.getString(1) != null && rs.getString(1).equals("а")) {

             in_Jaz = "английский";

           } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

             in_Jaz = "немецкий";

           } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

             in_Jaz = "французский";
           }

           if(rs.getString(2) != null && rs.getString(2).equals("д")) {

             need_Obshaga = "нуждаюсь";

           } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

             need_Obshaga = "не нуждаюсь";
           }
//           report.write("\\par\n");

          
           report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
           stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	      //   String predpr = new String();
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{Целевое направление }:  ");//\\i0{ для участия в целевом конкурсе
	         while(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
	           //  report.write("\\b0\\fs6\\par\n");
	          
	             if(rs.getString(1).equals("2")) predpr = "Росатом";
	             else if(rs.getString(1).equals("3")) predpr = "Роскосмос";
	             else if(rs.getString(1).equals("4")) predpr = "Минпромторг";
	             else if(rs.getString(1).equals("1")) predpr = "";
	             else predpr = "другое";
	             report.write("\\i{"+predpr+"   }");//\\i0{ для участия в целевом конкурсе
	           }
	         }
	         report.write("\\i0\n");
           
          // report.write("\\par\\ql\\fs22{Целевое направление }\\i{"+tsel1+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{Сведения о наличии опубликованных работ, изобретений и отчетов по НИР___________________________}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
           
           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
            // if(rs.getString(2) != null && rs.getString(2).length() > 3)
              //  report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
             if(rs.getString(1) != null && rs.getString(1).length() > 3)
               report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
           }
         }

   /************************************************/
         
         stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
         	
         if(rs.getString(1)!=null){
            report.write("\\par\\ql\\fs22\\tab{В соответствии с разделом 8 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительного экзамена в магистратуру:}\n");
            report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
         }
         
         
         /************************************************/

         //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
         //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\par\n");
            stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
            stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
          	  if(rs.getString(1) != null){
            report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
            report.write("\\par\\ql\\fs22{оригиналов документов) }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
            //report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+"    Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");
            }
            /************************************************/

            //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
            //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\par\n");
              // stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
               //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
              // rs = stmt.executeQuery();
               //if(rs.next()) {
             	//  if(rs.getString(1) != null){
              // report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
               //report.write("\\par\\ql\\fs22{оригиналов документов)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
               //report.write("\\par\\par\n");
              // report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+" Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
               //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
               //report.write("\\par\\par\n");
               //}
            report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{С копией лицензии на осуществление трудовой деятельности(с приложениями) серия 90ЛО1 №0000704, от}");
	         report.write("\\par\\ql\\fs22{21 марта 2013 г.; с копией свидетельства о государственной аккредитации (с приложениями) от 17.05.2013 г.}");  
	         report.write("\\par\\ql\\fs22{серия 90АО1 №0000631 рег.№0627; c информацией о предоставляемых поступающим особых правах и }\n");
	         report.write("\\par\\ql\\fs22{преимуществах при приеме на обучение по программам бакалавриата и программам специалитета с датами}\n");
	         report.write("\\par\\ql\\fs22{завершения представления поступающими оригинала документа установленного образца на каждом этапе}\n");
	         report.write("\\par\\ql\\fs22{зачисления на места в рамках контрольных цифр, с датами завершения представления поступающими сведений о}\n");
	         report.write("\\par\\ql\\fs22{согласии на зачисление на места по договорам об оказании платных образовательных услуг; с правилами подачи}\n");
	         report.write("\\par\\ql\\fs22{апелляции по результатам вступительных испытаний, проводимых организацией самостоятельно ознакомлен(а)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\n");
               
               //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
               //report.write("\\pard\\page\n");
               
               /************************************************/

            /*   report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
               report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");*/
               report.write("\\qj\\fs22\\tab{Согласен на обработку своих персональных данных                                           }\\'ab{_________________}\\'bb.\n"); 
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

         /************************************************/
              // report.write("\\par\\par\\ql\\fs22\\tab{Высшее профессиональное образование получаю }\\i{впервые.}\\i0\n");
              //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
              //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               
               report.write("\\par\n");
             
               report.write("\\par\\par\\ql\\fs22\\tab{Ознакомлен(а) с информацией об ответственности за достоверность сведений, указываемых в заявлении о }\n");
               report.write("\\par\\par\\ql\\fs22\\tab{приеме, и за подлинность документов, подаваемых для поступления               }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               report.write("\\par\\par\\par\n");

         /************************************************/
               report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю получение высшего образования по программам ординатуры впервые}\n");
               report.write("\\par\\par\\ql\\fs22\\tab{(при поступлении на обучение на места в рамках контрольных цифр)      }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
               report.write("\\par\\par\\par\n");
               
              
             

            
      /************************************************/

            //report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");

      /************************************************/

            //report.write("\\ql\\fs22\\tab{Степень магистра получаю }\\i{впервые.}\\i0\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\par\\qj\\fs22\\tab{С Лицензией на право осуществления образовательной деятельности серия 90ЛО1 №0000704, от 21 марта 2013 г., свидетельством о государственной аккредитации от 25.06.2012 серия 90АО1 № 000007 рег.№0007 и приложений к ним, программами вступительных испытаний, правилами приёма ПГУ, правилами проведения апелляций ознакомлен(а).}\n");
            //report.write("\\par\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab\\b{Ознакомлен(а) с датой предоставления оригинала документа государственного образца об образовании в соответствии с порядком зачисления (п.14) правил приема.}\\b0\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

      /************************************************/

            report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22\\tab{Дата: "+StringUtil.CurrDate(".")+"г.}\n");

            report.write("}"); 
            report.close();
    }

  // if ( abit_A.getTip_Spec().equals("м") ) {
    	 if ( abit_A.getNomerPotoka() == 2) {//&

/************************************************/
/************************************************/
/******        ЗАЯВЛЕНИЕ магистра   (м)   *******/
/************************************************/
/************************************************/

      String file_con = new String("statementM"+abit_A.getKodAbiturienta()+".rtf");
 
      String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;


      abit_A.setFileName1(file_con);

      BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

      report.write("{\\rtf1\\ansi\n");
      report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
      stmt = conn.prepareStatement("SELECT NazvanieRoditFull FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) report.write("\\fs24\\b\\qc{Ректору }{"+rs.getString(1)+"}\\b0\n");

      report.write("\\par\\par\n");

/************************************************/

      String tip_Dok = new String();
     
      boolean oblastIsEmpty = false;
      stmt = conn.prepareStatement("SELECT kodOblastiP FROM Abiturient where kodAbiturienta = ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()){
     	 if(rs.getString(1).equals("-")) oblastIsEmpty = true;
      }
      
      
      if (oblastIsEmpty){
    	  stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a WHERE KodAbiturienta LIKE ? ");
    	   
      }
      else
      stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\fs22\\ql{Я, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{гражданин(ка): }\\i{"+rs.getString(4)+"}\\i0,  дата рождения: \\i{"+rs.getString(5)+"г.}\\i0,\n");//\\i0{, место рождения: }\\i{"+rs.getString(16)+"}
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{проживающий(щая) по адресу: }\\i{"+rs.getString(6)+", ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        if(rs.getString(10) != null && rs.getString(10).equals("п"))
          tip_Dok = "Паспорт(серия-№)";
        else
          tip_Dok = "справка";
        report.write("{Документ, удостоверяющий личность: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" № "+rs.getString(12)+", выдан: "+rs.getString(13)+", "+rs.getString(14)+"г.}\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Контактный телефон: }\\i{"+rs.getString(15)+"}\\i0\n");
        report.write("\\par\\par\n");
      }

/************************************************/

      report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на специальности или направления подготовки магистра:}\n");
      report.write("\\par\\par\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Bud LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','п') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      boolean OchFormOutput = false;
      if(rs.next()) {
//        if(abit_A.getTip_Spec().equals("д")) {
//          report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//        } else {
    
        report.write("\\ql\\fs24\\b\\i\\tab{по очной форме обучения}\\i0\\b0\n");
        OchFormOutput = true;
//        }
        
        report.write("\\par\\par\\qj\\fs22\\tab{на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований федерального бюджета}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

    	 
        int first_line = 0;
        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           if(first_line++ > 0) {
             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
           }
          // if(rs.getString(4).equals("д")){
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           report.write("\\intbl\\row\n");
       // }
        }
        //report.write("\\intbl");
        report.write("\\pard\\par\n");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      }
    	
        stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Dog LIKE 'д'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','п') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {  
	        if (!OchFormOutput)	 report.write("\\ql\\fs24\\b\\i\\tab{по очной форме обучения}\\i0\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
      report.write("\\par\\par\n");
      
      report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
      report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

     int first_line = 0;
      stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
           if(first_line++ > 0) {
           report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
           //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
         }
         //if(rs.getString(5).equals("д"))
           //{
         report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
         report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
         report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
         report.write("\\intbl\\row\n");
         //}
      }
     
      report.write("\\pard\\par\n");
    	}  
    
      
      /************************************************/   
      
//System.out.println("Doc-!->3");
      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('в','ф') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

      
        report.write("\\ql\\fs24\\b\\i\\tab{по очно-заочной форме обучения}\\i0\\b0\n");

        report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");
        //report.write("\\intbl\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND (kon.Bud LIKE 'д' OR  kon.Dog LIKE 'д') AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('в','ф') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           report.write("\\intbl\\row\n");
        }
        report.write("\\pard\\par\n");
      }
      
      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE 'д'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        //////////////////////////////////////////////////////////
        
        report.write("\\ql\\fs24\\b\\i\\tab{по заочной форме обучения}\\i0\\b0\n");
        ////////////////
        report.write("\\par\\par\\qj\\fs22\\tab{на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");
        report.write("\\intbl\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        ///report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           report.write("\\intbl\\row\n");
        }
        //report.write("\\intbl\n");
        report.write("\\pard\\par\n");
      }
        /////////////////////////////////////////////////////////////////////

stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') ORDER BY kon.Prioritet ASC");
stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) {
report.write("\\ql\\fs24\\b\\i\\tab{по заочной форме обучения}\\i0\\b0\n");
        report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           report.write("\\intbl\\row\n");
          
        }
        report.write("\\intbl\n");
        report.write("\\pard\\par\n");
      }
      
        report.write("\\pard\\par\n");
      
/************************************************/
      report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

      
      String tip_Ok_Zav = new String();
      stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata, a.tipDokSredObraz FROM Abiturient a, Zavedenija z WHERE a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

          tip_Ok_Zav = "общеобразовательное учреждение";

        } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

          tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

        } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

          tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";

        } else if(rs.getString(2) != null && rs.getString(2).equals("у")) {

          tip_Ok_Zav = "высшее учебное заведение";
        }

        String DokSrObr = null;
        if(rs.getString(8) != null && rs.getString(8).equals("о"))
            DokSrObr = " (оригинал)";
          else
            DokSrObr = " (копия)";
        
        report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  № "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
      }
      
      
      /************************************************/
      
      stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      String predpr = new String();
      report.write("\\b0\\fs6\\par\n");
      report.write("\\par\\ql\\fs22{Целевое направление }:  ");//\\i0{ для участия в целевом конкурсе
      while(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
        //  report.write("\\b0\\fs6\\par\n");
       
          if(rs.getString(1).equals("2")) predpr = "Росатом";
          else if(rs.getString(1).equals("3")) predpr = "Роскосмос";
          else if(rs.getString(1).equals("4")) predpr = "Минпромторг";
          else if(rs.getString(1).equals("1")) predpr = "";
          else predpr = "другое";
          report.write("\\i{"+predpr+"   }");//\\i0{ для участия в целевом конкурсе
        }
      }
      report.write("\\i0\n");
      

      String in_Jaz = new String();
      String need_Obshaga = new String();
      stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
    	  in_Jaz = rs.getString(1);
        if(rs.getString(1) != null && rs.getString(1).equals("а")) {

          in_Jaz = "английский";

        } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

          in_Jaz = "немецкий";

        } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

          in_Jaz = "французский";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("д")) {

          need_Obshaga = "нуждаюсь";

        } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

          need_Obshaga = "не нуждаюсь";
        }
//        report.write("\\par\n");

       
        report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
        
        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
         // if(rs.getString(2) != null && rs.getString(2).length() > 3)
           //  report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
            report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
        }
      }

/************************************************/
      
      stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
      	
      if(rs.getString(1)!=null){
         report.write("\\par\\ql\\fs22\\tab{В соответствии с разделом 9 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительного экзамена в магистратуру:}\n");
         report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
      }
      
      
      /************************************************/

      //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
      //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //   report.write("\\par\\par\n");
         stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
       	  if(rs.getString(1) != null){
         report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
         report.write("\\par\\ql\\fs22{оригиналов документов)}\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
         //report.write("\\par\\par\n");
         report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+" Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
         //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //report.write("\\par\\par\n");
         }
         report.write("\\par\\par\n");
         report.write("\\par\\ql\\fs22{С копией лицензии на осуществление трудовой деятельности(с приложениями) серия 90ЛО1 №0000704, от}");
         report.write("\\par\\ql\\fs22{21 марта 2013 г.; с копией свидетельства о государственной аккредитации (с приложениями) от 17.05.2013 г.}");  
         report.write("\\par\\ql\\fs22{серия 90АО1 №0000631 рег.№0627; c информацией о предоставляемых поступающим особых правах и }\n");
         report.write("\\par\\ql\\fs22{преимуществах при приеме на обучение по программам бакалавриата и программам специалитета с датами}\n");
         report.write("\\par\\ql\\fs22{завершения представления поступающими оригинала документа установленного образца на каждом этапе}\n");
         report.write("\\par\\ql\\fs22{зачисления на места в рамках контрольных цифр, с датами завершения представления поступающими сведений о}\n");
         report.write("\\par\\ql\\fs22{согласии на зачисление на места по договорам об оказании платных образовательных услуг; с правилами подачи}\n");
         report.write("\\par\\ql\\fs22{апелляции по результатам вступительных испытаний, проводимых организацией самостоятельно ознакомлен(а)}\n");
         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
         report.write("\\par\\par\n");
         //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
         //report.write("\\pard\\page\n");
         
         /************************************************/

      /*   report.write("\\par\\par\\par\n");
         report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
         report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");*/
         report.write("\\par\\par\\par\n");
         report.write("\\qj\\fs22\\tab{Согласен на обработку своих персональных данных                                           }\\i{_________________}\\i0\n"); 
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

   /************************************************/
        // report.write("\\par\\par\\ql\\fs22\\tab{Высшее профессиональное образование получаю }\\i{впервые.}\\i0\n");
        //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
        //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
         
         report.write("\\par\\par\\par\n");
       
         report.write("\\par\\par\\ql\\fs22\\tab{Ознакомлен(а) с информацией об ответсвенности за достоверность сведений, указываемых в заявлении о }\n");
         report.write("\\par\\par\\ql\\fs22\\tab{приеме, и за подлинность документов, подаваемых для поступления                }\\i{_________________}\\i0\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
         report.write("\\par\\par\\par\n");

   /************************************************/
         report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю отсутствие диплома бакалавра, диплома специалиста, диплома магистра(при поступлении }\n");
         report.write("\\par\\par\\ql\\fs22\\tab{на обучение на места в рамках контрольных цифр)                                               }\\i{_________________}\\i0\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
         report.write("\\par\\par\\par\n");
         
         report.write("\\ql\\fs22\\tab{Подтверждаю факт подачи заявления не более чем в пять вузов РФ.                  }\\b0{_________________}\\bb\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
        
         report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю подачу заявления о приеме на основании соответствующего особого права только в ПГУ и}\n");
         report.write("\\par\\par\\ql\\fs22\\tab{только на одну образовательную программу                                                          }\\i{_________________}\\i0\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
         report.write("\\par\\par\\par\n");

   /************************************************/
      
/************************************************/

      //report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\par\n");

/************************************************/

      //report.write("\\ql\\fs22\\tab{Степень магистра получаю }\\i{впервые.}\\i0\n");
      //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\par\\qj\\fs22\\tab{С Лицензией на право осуществления образовательной деятельности серия 90ЛО1 №0000704, от 21 марта 2013 г., свидетельством о государственной аккредитации от 25.06.2012 серия 90АО1 № 000007 рег.№0007 и приложений к ним, программами вступительных испытаний, правилами приёма ПГУ, правилами проведения апелляций ознакомлен(а).}\n");
      //report.write("\\par\n");
      //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\qj\\fs22\\tab\\b{Ознакомлен(а) с датой предоставления оригинала документа государственного образца об образовании в соответствии с порядком зачисления (п.14) правил приема.}\\b0\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22\\tab{Дата: "+StringUtil.CurrDate(".")+"г.}\n");

      report.write("}"); 
      report.close();

   } //else if ( abit_A.getTip_Spec().equals("о") || abit_A.getTip_Spec().equals("з") || abit_A.getTip_Spec().equals("ф") || abit_A.getTip_Spec().equals("в") || abit_A.getTip_Spec().equals("д") || abit_A.getTip_Spec().equals("у") || abit_A.getTip_Spec().equals("д") && abit_A.getInternship().equals("н")  && abit_A.getTraineeship().equals("н")  &&  abit_A.getPostgraduateStudies().equals("н")) {
  // else if (abit_A.getNeed_Spo().equals("н") && abit_A.getTip_Spec().equals("п") || abit_A.getNeed_Spo().equals("н") && ((abit_A.getTip_Spec().equals("о") || abit_A.getTip_Spec().equals("з") || abit_A.getTip_Spec().equals("ф") || abit_A.getTip_Spec().equals("в") || abit_A.getTip_Spec().equals("д") || abit_A.getTip_Spec().equals("у") || abit_A.getTip_Spec().equals("д")) && ((abit_A.getInternship().equals("н") || abit_A.getInternship().equals("null")) && (abit_A.getTraineeship().equals("н") || abit_A.getTraineeship().equals("null")) && (abit_A.getPostgraduateStudies().equals("н") || abit_A.getPostgraduateStudies().equals("null"))))) {
    	 else if ( abit_A.getNomerPotoka() == 1) {//&
	   /***********************************************************/
	   /***********************************************************/
	   /*****  ЗАЯВЛЕНИЕ бакалавра/специалиста (о,з,ф,в,у,д) ******/
	   /***********************************************************/
	   /***********************************************************/
	   //System.out.println("Doc-!->1");
	         String file_con = new String("statement"+abit_A.getKodAbiturienta()+".rtf");
	    
	         String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;

	         abit_A.setFileName1(file_con);
	   //System.out.println("Doc-!->2");
	         BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

	   /************************************************/

	         report.write("{\\rtf1\\ansi{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;\\ctextone\\ctint255\\cshade255\\red0\\green0\\blue0;}\n");
	         report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");

	         stmt = conn.prepareStatement("SELECT NazvanieRoditFull FROM NazvanieVuza WHERE KodVuza LIKE ?");
	         stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) report.write("\\fs24\\b\\qc{Ректору }{"+rs.getString(1)+"}\\b0\n");

	         report.write("\\par\\par\n");

	   /************************************************/

	         String tip_Dok = new String();
	         boolean oblastIsEmpty = false;
	         stmt = conn.prepareStatement("SELECT kodOblastiP FROM Abiturient where kodAbiturienta = ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()){
	        	 if(rs.getString(1).equals("-")) oblastIsEmpty = true;
	         }
	         
	         
	         if (oblastIsEmpty){
	        	 stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,a.gorod_prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a WHERE KodAbiturienta LIKE ?");
	   	      	 
	         }
	         else{
	         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
	         }
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\fs22\\ql{Я, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
	           report.write("\\fs6\\par\\par\\fs22");
	           report.write("{гражданин(ка): }\\i{"+rs.getString(4)+"}\\i0,  дата рождения: \\i{"+rs.getString(5)+"г.},\n");//\\i0{, место рождения: }\\i{"+rs.getString(16)+"}\\i0
	           report.write("\\fs6\\par\\par\\fs22");
	           if(oblastIsEmpty){
	        	   report.write("{проживающий(щая) по адресу: }\\i{ ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
		             
	           }
	           else
	           report.write("{проживающий(щая) по адресу: }\\i{"+rs.getString(6)+", ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
	           report.write("\\fs6\\par\\par\\fs22");
	           if(rs.getString(10) != null && rs.getString(10).equals("п"))
	             tip_Dok = "Паспорт (серия-№)";
	           else
	             tip_Dok = "справка";
	           report.write("{Документ, удостоверяющий личность: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" № "+rs.getString(12)+", выдан: "+rs.getString(13)+", "+rs.getString(14)+"г.}\\i0\n");
	           report.write("\\fs6\\par\\par\\fs22");
	           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Контактный телефон: }\\i{"+rs.getString(15)+"}\\i0\n");
	           report.write("\\par\\par\n");
	         }

	   /************************************************/

	         report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
	         report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на специальности или направления подготовки бакалавриата и(или) программы специалитета:}\n");
	         report.write("\\par\\par\n");

	   /************************************************/

	         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Bud LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','п') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         boolean OchFormOutput = false;
	         if(rs.next()) {
//	           if(abit_A.getTip_Spec().equals("д")) {
//	             report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//	           } else {
	       
	           report.write("\\ql\\fs24\\b\\i\\tab{по очной форме обучения}\\i0\\b0\n");
	           OchFormOutput = true;
//	           }
	           
	           report.write("\\par\\par\\qj\\fs22\\tab{на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований федерального бюджета}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	           report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	           report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	       	 
	           int first_line = 0;
	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           while(rs.next()) {
	              if(first_line++ > 0) {
	                report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	                report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
	                report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	                report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	                //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	                //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	                //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	                //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	              }
	             // if(rs.getString(4).equals("д")){
	              report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
	              report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
	              report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
	              report.write("\\intbl\\row\n");
	          // }
	           }
	           //report.write("\\intbl");
	           report.write("\\pard\\par\n");
	   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	         }
	       	
	           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Dog LIKE 'д'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','п') ORDER BY kon.Prioritet ASC");
		         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
		         rs = stmt.executeQuery();
		         if(rs.next()) {  
		        if (!OchFormOutput)	 report.write("\\ql\\fs24\\b\\i\\tab{по очной форме обучения}\\i0\\b0\n");
	         report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
	         report.write("\\par\\par\n");
	         
	         report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	         report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
	         report.write("\\intbl\\row\n");

	         report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	         report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	         report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	         report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
	         report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\cell\n");
	         //report.write("\\intbl\\qc\\cell\n");
	         //report.write("\\intbl\\qc\\cell\n");
	         //report.write("\\intbl\\qc\\cell\n");
	         report.write("\\intbl\\row\n");

	         report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
	         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	         //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	         //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	         //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	         //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	        int first_line = 0;
	         stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         while(rs.next()) {
	              if(first_line++ > 0) {
	              report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	              report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
	              report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	              report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	              //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	              //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	              //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	              //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	            }
	            //if(rs.getString(5).equals("д"))
	              //{
	            report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
	            report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
	            report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
	            //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
	            //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
	            //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
	            //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
	            report.write("\\intbl\\row\n");
	            //}
	         }
	        
	         report.write("\\pard\\par\n");
	       	}  
	       
	         
	         /************************************************/   
	         
	   //System.out.println("Doc-!->3");
	         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('в','ф') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	         
	           report.write("\\ql\\fs24\\b\\i\\tab{по очно-заочной форме обучения}\\i0\\b0\n");

	           report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           report.write("\\intbl\\row\n");
	           //report.write("\\intbl\n");

	           report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND (kon.Bud LIKE 'д' OR  kon.Dog LIKE 'д') AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('в','ф') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           while(rs.next()) {
	              report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
	              report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
	              report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
	              report.write("\\intbl\\row\n");
	           }
	           report.write("\\pard\\par\n");
	         }
	         
	         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE 'д'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           //////////////////////////////////////////////////////////
	           
	           report.write("\\ql\\fs24\\b\\i\\tab{по заочной форме обучения}\\i0\\b0\n");
	           ////////////////
	           report.write("\\par\\par\\qj\\fs22\\tab{на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");
	           report.write("\\intbl\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           ///report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           while(rs.next()) {
	              report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
	              report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
	              report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
	              report.write("\\intbl\\row\n");
	           }
	           //report.write("\\intbl\n");
	           report.write("\\pard\\par\n");
	         }
	           /////////////////////////////////////////////////////////////////////
  
   stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') ORDER BY kon.Prioritet ASC");
   stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
   rs = stmt.executeQuery();
   if(rs.next()) {
	   report.write("\\ql\\fs24\\b\\i\\tab{по заочной форме обучения}\\i0\\b0\n");
	           report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           //report.write("\\intbl\\qc\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three,  AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           while(rs.next()) {
	              report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
	              report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
	              report.write("\\intbl\\ql{"+"("+rs.getString(8) + ")  "+rs.getString(3)+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
	              //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
	              report.write("\\intbl\\row\n");
	             
	           }
	           report.write("\\intbl\n");
	           report.write("\\pard\\par\n");
	         }

	   /************************************************/

	         report.write("\\pard\\par\n");
	         report.write("\\par\\ql\\fs24\\tab\\b{Прошу:}\\b0\\fs6\\par\n");
	   //System.out.println("Doc-!->4");
	         int counter = 1;
	   ////////////////////////////////////////////////ПОКА НЕ ТРОГАЛ !!!!
	         stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\par\\ql\\fs22\\tab{•	засчитать в качестве результатов вступительных испытаний оценки, полученные при сдаче }\\fs24{ЕГЭ:}\n");

	           stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". засчитать в качестве результатов вступительных испытаний оценки, полученные при сдаче    }\\fs24{ЕГЭ:}\n");
	           while(rs.next()) {
	              report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+" - "+rs.getString(2)+" баллов;}\\i0");
	           }
	         }

	   /************************************************/
	         //report.write("\\par\\par\\ql\\fs24\\tab\\b{Прошу:}\\b0\\fs6\\par\n");
	         //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".в соответствии с разделом 8 правил приема ПГУ допустить к сдаче экзаменов, проводимых университетом самостоятельно по предметам:}\n");      

	         stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('е') or zso.Examen IN('в')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\par\\ql\\fs22\\tab{•	в соответствии с разделом 8 правил приема ПГУ допустить к сдаче экзаменов, проводимых университетом самостоятельно по предметам:}\n");      

	           stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('е') or zso.Examen IN('в')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           while(rs.next()) {
	           	//report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".в соответствии с разделом 8 правил приема ПГУ допустить к сдаче экзаменов, проводимых университетом самостоятельно по предметам:}\n");
	              report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
	           }
	         }

	   /************************************************/
	         //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". в соответствии с разделом 9 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительных испытаний, проводимых университетом самостоятельно:}\n");
	         
	         //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('в') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	         //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         //rs = stmt.executeQuery();
	         //if(rs.next()) {
	         
	           //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". в соответствии с разделом 9 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительных испытаний, проводимых университетом самостоятельно:}\n");

	           //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('в') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	           //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           //rs = stmt.executeQuery();
	           //while(rs.next()) {
	         
	         report.write("\\par\\ql\\fs22\\tab{•	в соответствии с разделом 2 правил приема в ПГУ допустить к сдаче экзаменов, проводимых университетом самостоятельно на иностранном языке по следующим предметам:}\n");      
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         
	           stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           if(rs.next()) {
	           	
	           if(rs.getString(1)!=null){
	              report.write("\\par\\ql\\fs22\\tab{•	в соответствии с разделом 9 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительных испытаний, проводимых университетом самостоятельно:}\n");
	              report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+"}\\i0");}
	           }
	          // }
	        // }

	   /************************************************/
	   //////////////////////////////////////////////ПОКА НЕ ТРОГАЛ !!! конец
	      /*   report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

	         String tip_Ok_Zav = new String();
	         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

	             tip_Ok_Zav = "общеобразовательное учреждение";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

	             tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

	             tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";
	           }

	           report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"}, находящееся в {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  "+rs.getString(5)+" № "+rs.getString(6)+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{Номер сертификата ЕГЭ / Место сдачи ЕГЭ: }\\i{"+rs.getString(7)+"}\\i0\n");
	         }
	   /////
	         report.write("\\par\\ql\\fs22{Место сдачи ЕГЭ в дополнительные сроки___________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{Место сдачи вступительных испытаний с использованием дистанционных технологий ___________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{Место получения предыдущего образования__________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	    //////


	         stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
	             report.write("\\b0\\fs6\\par\n");
	             String predpr = new String();
	             if(rs.getString(1).equals("к")) predpr = "(Роскосмос)";
	             else if(rs.getString(1).equals("а")) predpr = "(Росатом)";
	             else if(rs.getString(1).equals("т")) predpr = "(Минпромторг)";
	             else predpr = "предприятия";
	             report.write("\\par\\ql\\fs22{Целевое направление }\\i{"+predpr+"}\n");//\\i0{ для участия в целевом конкурсе
	           }
	         }



	         stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
	             report.write("\\b0\\fs6\\par\n");
	             report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1).toUpperCase()+"  (\"З\", \"С\" - золотая и серебряная медали; \"Т\" - диплом с отличием УСПО)}\\i0\\fs6\\par\n");
	           }
	         }

	   //System.out.println("Doc-!->5");


	         String in_Jaz = new String();
	         String need_Obshaga = new String();
	         stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && rs.getString(1).equals("а")) {

	             in_Jaz = "английский";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

	             in_Jaz = "немецкий";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

	             in_Jaz = "французский";
	           }

	           if(rs.getString(2) != null && rs.getString(2).equals("д")) {

	             need_Obshaga = "нуждаюсь";

	           } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

	             need_Obshaga = "не нуждаюсь";
	           }
//	           report.write("\\par\n");

	           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           if(rs.next()) {
	             if(rs.getString(1) != null && rs.getString(1).length() > 3)
	                report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
	             if(rs.getString(2) != null && rs.getString(2).length() > 3)
	               report.write("\\par\\ql\\fs22{Преимущественное право при поступлении: }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
	           }
	           report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
	         }



	      //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
	      //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\par\n");
	         
	         report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
	         report.write("\\par\\ql\\fs22{оригиналов документов)      ____________________________________________________________________}\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): _____________________________}\n");
	         report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	         report.write("\\par\\par\n");
	         
	         //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
	         //report.write("\\pard\\page\n");
	   */
	   /************************************************/
	   /***********      СТРАНИЦА № 2      *************/
	   /************************************************/
	         report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

	         String tip_Ok_Zav = new String();
	       //  stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,z.PolnoeNaimenovanieZavedenija FROM Zavedenija z,Abiturient a, Punkty p WHERE a.kodzavedenija=z.kodzavedenija AND a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
	         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija, k.name, a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,z.PolnoeNaimenovanieZavedenija, a.TipDokSredObraz, a.nomerShkoly FROM Zavedenija z, Abiturient a, KLADR k where a.kodzavedenija=z.kodzavedenija and k.code = a.kodPunkta AND a.KodAbiturienta LIKE  ?");
		       
	          stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

	             tip_Ok_Zav = "общеобразовательное учреждение";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

	             tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

	             tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";
	           }
	           
	           String DokSrObr = null;
	           if(rs.getString(9) != null && rs.getString(9).equals("о"))
	               DokSrObr = " (оригинал)";
	             else
	               DokSrObr = " (копия)";
	           String nomerShkoly = "";
	           if (rs.getString(10)!=null){
	        	   nomerShkoly = nomerShkoly + "№ " +  rs.getString(10);
	           }

	           report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"} {" +rs.getString(8)+" "+nomerShkoly+"}, находящееся в {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i   № "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
	           //report.write("\\par\\ql\\fs22{Аттестат / Диплом: }\\i{"+rs.getString(7)+"}\\i0\n");
	         }
	   /////
	         report.write("\\par\\ql\\fs22{Место сдачи ЕГЭ в дополнительные сроки___________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{Место сдачи вступительных испытаний с использованием дистанционных технологий ___________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{Место получения предыдущего образования__________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	    //////
	   /************************************************/

	         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         String predpr = new String();
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{Целевое направление }:  ");//\\i0{ для участия в целевом конкурсе
	         while(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
	           //  report.write("\\b0\\fs6\\par\n");
	          
	             if(rs.getString(1).equals("2")) predpr = "Росатом";
	             else if(rs.getString(1).equals("3")) predpr = "Роскосмос";
	             else if(rs.getString(1).equals("4")) predpr = "Минпромторг";
	             else if(rs.getString(1).equals("1")) predpr = "";
	             else predpr = "другое";
	             report.write("\\i{"+predpr+"   }");//\\i0{ для участия в целевом конкурсе
	           }
	         }
	         report.write("\\i0\n");
	         //КРЫМ 01.03.2016
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{Являюсь лицом, постоянно проживающим в Крыму }:  ");
	         
	         
	         
	        // Select * from abitdopinf where kodabiturienta LIKE '66' and (ballAtt not in ('null', '0', 'нет') or ballSGTO  not in ('null', '0', 'нет') or ballZGTO  not in ('null', '0', 'нет') or ballSoch  not in ('null', '0', 'нет') or ballPOI  not in ('null', '0', 'нет') or TrudovajaDejatelnost  not in ('null', '0', 'нет')) 
	         
	         
   stmt = conn.prepareStatement("Select * from abitdopinf where kodabiturienta LIKE ? and (ballAtt not in ('null', '0', 'нет') or ballSGTO  not in ('null', '0', 'нет') or ballZGTO  not in ('null', '0', 'нет') or ballSoch  not in ('null', '0', 'нет') or ballPOI  not in ('null', '0', 'нет') or TrudovajaDejatelnost  not in ('null', '0', 'нет'))");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	        predpr = new String();
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{Индивидуальные достижения }:  ");//\\i0{ для участия в целевом конкурсе
	         if(rs.next()) {
	          
	             report.write("\\i{  да }");//\\i0{ для участия в целевом конкурсе
	           }
	         else report.write("\\i{  нет }");
	         
	         report.write("\\i0\n");
	   /************************************************/
	         
	         
	         //select distinct l.Lgoty from konkurs k, lgoty l where kodabiturienta like '29' and l.kodLgot = k.op
	         //   select distinct pr from konkurs where kodabiturienta like '66'
	         
	         
	         stmt = conn.prepareStatement("select distinct l.Lgoty from konkurs k, lgoty l where kodabiturienta like ? and l.kodLgot = k.op");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	        
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{Особые права при поступлении }:  ");//\\i0{ для участия в целевом конкурсе
	         while(rs.next()) {
	         
	            if (!rs.getString(1).equals("не имеется"))
	             report.write("\\i{"+rs.getString(1)+"   }");//\\i0{ для участия в целевом конкурсе
	           }
	         
	         report.write("\\i0\n");
	         
	         
	         
	         
	         ///////////////////////////////////
	         
	         
	         
	         stmt = conn.prepareStatement("select distinct pr from konkurs where kodabiturienta like ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         String pr  = "не имеется";
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{Преимущественное право при поступлении }:  ");//\\i0{ для участия в целевом конкурсе
	         while(rs.next()) {
	         
	             if (rs.getString(1).equals("1")) pr = "имеется";
	            	 
	             
	           }
	         report.write("\\i{"+pr+"   }");//преимущественное право да нет
	         report.write("\\i0\n");

	         stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
	             report.write("\\b0\\fs6\\par\n");
	             report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1).toUpperCase()+"  (\"З\", \"С\" - золотая и серебряная медали; \"Т\" - диплом с отличием УСПО)}\\i0\\fs6\\par\n");
	           }
	         }

	   //System.out.println("Doc-!->5");
	   /************************************************/

	         String in_Jaz = new String();
	         String need_Obshaga = new String();
	         stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	        	 in_Jaz = rs.getString(1);
	           if(rs.getString(1) != null && rs.getString(1).equals("а")) {

	             in_Jaz = "английский";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

	             in_Jaz = "немецкий";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

	             in_Jaz = "французский";
	           }

	           if(rs.getString(2) != null && rs.getString(2).equals("д")) {

	             need_Obshaga = "нуждаюсь";

	           } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

	             need_Obshaga = "не нуждаюсь";
	           }
//	           report.write("\\par\n");

	           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           if(rs.next()) {
	             if(rs.getString(2) != null && rs.getString(2).length() > 3)
	                report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
	             if(rs.getString(1) != null && rs.getString(1).length() > 3)
	               report.write("\\par\\ql\\fs22{Преимущественное право при поступлении: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
	           }
	           report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
	         }

	   /************************************************/

	      //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
	      //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\par\n");
	         stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	       	  if(rs.getString(1) != null){
	         report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
	         report.write("\\par\\ql\\fs22{оригиналов документов):  }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
	         //report.write("\\par\\par\n");
	       	  
	       	  
	       	  String Email = "";
	       	  String Address = "";
	       	  if (rs.getString(2)==null)Email = "-";
	       	  else Email = rs.getString(2);
	       	 if (rs.getString(3)==null)Address = "-";
	       	  else Email = rs.getString(3);
	       	  
	       			  
	         //report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+" Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+Email+" Почтовый адрес-"+Address+"}\\i0\\fs6\\par\n");
		       
	         //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	         //report.write("\\par\\par\n");
	         }
	         report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{С копией лицензии на осуществление трудовой деятельности(с приложениями) серия 90ЛО1 №0000704, от}");
	         report.write("\\par\\ql\\fs22{21 марта 2013 г.; с копией свидетельства о государственной аккредитации (с приложениями) от 17.05.2013 г.}");  
	         report.write("\\par\\ql\\fs22{серия 90АО1 №0000631 рег.№0627; c информацией о предоставляемых поступающим особых правах и }\n");
	         report.write("\\par\\ql\\fs22{преимуществах при приеме на обучение по программам бакалавриата и программам специалитета с датами}\n");
	         report.write("\\par\\ql\\fs22{завершения представления поступающими оригинала документа установленного образца на каждом этапе}\n");
	         report.write("\\par\\ql\\fs22{зачисления на места в рамках контрольных цифр, с датами завершения представления поступающими сведений о}\n");
	         report.write("\\par\\ql\\fs22{согласии на зачисление на места по договорам об оказании платных образовательных услуг; с правилами подачи}\n");
	         report.write("\\par\\ql\\fs22{апелляции по результатам вступительных испытаний, проводимых организацией самостоятельно ознакомлен(а)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\n");
	         //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
	         //report.write("\\pard\\page\n");
	         
	         /************************************************/

	      /*   report.write("\\par\\par\\par\n");
	         report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
	         report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");*/
	         report.write("\\par\\par\\par\n");
	         report.write("\\qj\\fs22\\tab{Согласен на обработку своих персональных данных                                            }\\i{_________________}\\i0\n"); 
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

	   /************************************************/
	        // report.write("\\par\\par\\ql\\fs22\\tab{Высшее профессиональное образование получаю }\\i{впервые.}\\i0\n");
	        //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
	        //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         
	         report.write("\\par\\par\\par\n");
	         // ДОБАВЛЕНО 29 ФЕВРАЛЯ 2016 Г.
	         report.write("\\par\\par\\ql\\fs22\\tab{Ознакомлен(а) с информацией о необходимости указания в заявлении о приёме достоверных сведений и }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{представления подлинных документов                                                                   }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\\par\n");

	   /************************************************/
	         report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю отсутствие диплома бакалавра, диплома специалиста, диплома магистра(при поступлении }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{на обучение на места в рамках контрольных цифр)                                               }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\\par\n");
	         
		     // ДОБАВЛЕНО 29 ФЕВРАЛЯ 2016 Г.
	         report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю факт подачи заявления о приёме не более чем в 5 организаций высшего образования, }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{включая организацию, в которую подаётся данное заявление                              }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\\par\n");
	         
//	         report.write("\\ql\\fs22\\tab{Подтверждаю факт подачи заявления не более чем в пять вузов РФ.                  }\\b0{_________________}\\bb\n");
//	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	        
		     // ДОБАВЛЕНО 29 ФЕВРАЛЯ 2016 Г.
	         report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю факт подачи заявления о приёме в ПГУ не более чем по 3 специальностям и (или) }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{направлениям подготовки                                                                                          }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\\par\n");
	        
	         report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю подачу заявления о приеме на основании соответствующего особого права только в ПГУ и}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{только на одну образовательную программу.                                                         }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
	         report.write("\\par\\par\\par\n");

		     // ДОБАВЛЕНО 29 ФЕВРАЛЯ 2016 Г.
	         report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю факт подачи заявления о приёме на условиях, установленных пунктом 14.1 Правил }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{приёма не более чем в 3 организации высшего образования, включая организацию, в которую подаётся }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{данное заявление (только для лиц, постоянно проживающих в Крыму)              }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
//	         report.write("\\par\\par\\par\n");

	   /************************************************/

	         //report.write("\\par\\par\\par\n");
	         //report.write("\\par\\qj\\fs22\\tab{С Лицензией на право осуществления образовательной деятельности серия 90ЛО1 №0000704, от 21 марта 2013 г., свидетельством о государственной аккредитации от 25.06.2012 серия 90АО1 № 000007 рег.№0007 и приложений к ним, программами вступительных испытаний, правилами приёма ПГУ, правилами проведения апелляций ознакомлен(а).}\n");
	         //report.write("\\par\n");
	         //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
	         //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");*/

	   /************************************************/

	         //report.write("\\par\\par\\par\n");
	         //report.write("\\ql\\fs22\\tab{Сведения о сдаче ЕГЭ и его результатах в приемную комиссию предоставил(а).}\n");
	         //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
	         //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

	   /************************************************/

	         //report.write("\\par\\par\\par\n");
	         //report.write("\\qj\\fs22\\tab\\b{Ознакомлен(а) с датой предоставления оригинала документа государственного образца об образовании в соответствии с порядком зачисления (п.14) правил приема.}\\b0\n");
	         //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
	         //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");



	         report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22\\tab{Дата: "+StringUtil.CurrDate(".")+"г.}\n");
	         
	      // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
	         report.write("\\par\\par\n");
	         report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab\\tab{Председателю приёмной комиссии ПГУ, ректору А.Д. Гулякову}\n");        

	      // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
	         String tip_Dok2 = new String();
	         boolean oblastIsEmpty2 = false;
	         stmt = conn.prepareStatement("SELECT kodOblastiP FROM Abiturient where kodAbiturienta = ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()){
	        	 if(rs.getString(1).equals("-")) oblastIsEmpty = true;
	         }
	         
	      // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
	         if (oblastIsEmpty){
	         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,a.gorod_prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a WHERE KodAbiturienta LIKE ?");
	   	      	 
	         }
	         else{
	         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
	         }
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	         report.write("\\par\\qc\\fs22\\tab\\tab\\tab{  абитуриента }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
	         }
	      // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
	         report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab{подавшего заявление на участие в конкурсе на обучение}\n");  
	         
	         report.write("\\par\\qc\\fs22\\tab{         по образовательной программе  }\n");
	         report.write("\\par\\par\n");
	         

	         // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
	         stmt = conn.prepareStatement("SELECT s.ShifrSpetsialnosti, s.NazvanieSpetsialnosti, e.name, ko.Forma_ob from konkurs ko, spetsialnosti s, edulevel e where ko.kodabiturienta like ? and ko.kodspetsialnosti=s.kodspetsialnosti and e.socr = s.edulevel");
	         
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\tab\\tab\\tab\\tab\\tab\\fs22\\ql{ }\\i{"+rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+"}\\i0\n");
	           report.write("\\qc\\fs6\\par\\par\\fs22");
	         }
	      // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
	         stmt = conn.prepareStatement("select nomerlichnogodela from abiturient where kodabiturienta like ?");
	         
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\tab\\fs22\\ql{Личное дело № }\\i{"+rs.getString(1)+"}\\i0\n");
	           report.write("\\qc\\fs6\\par\\par\\fs22");
	           report.write("\\par\\par\n");
	           report.write("\\par\\par\n");
	           
	         }
	      // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.           
	         report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{Прошу учесть моё согласие о зачислении на обучение по программе}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(код, НП, форма обучения)}\\b0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         
	         report.write("\\par\\par\\ql\\fs22\\tab{При приёме на обучение на места по договорам с оплатой стоимости обучения:}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\tab{- по общему конкурсу.}\n");
	        
	         report.write("\\par\\par\\ql\\fs22\\tab{К данному заявлению прилагаю}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(название документа об образовании, серия, рег. №)}\\b0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{выдан______________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(название образовательной организации, выдавшей документ, дата выдачи:)}\\b0\n");	
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(оригинал, копия, заверенная нотариально / приёмной комиссией (ненужное вычеркнуть))}\\b0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	      
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{(Ф.И.О.)                                                                                                             }\\i{(подпись)}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{''___''________________ 2016 г.}\n");
	         
	         report.write("}"); 
	         report.close();

	   //System.out.println("Doc-!->6");

	      }
   
/***********************************************************/
/***********************************************************/
/*****  ЗАЯВЛЕНИЕ бакалавра/специалиста (о,з,ф,в,у,д) ******/
/***********************************************************/
/***********************************************************/
//System.out.println("Doc-!->1");
     /* String file_con = new String("statement"+abit_A.getKodAbiturienta()+".rtf");
 
      String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;

      abit_A.setFileName1(file_con);
//System.out.println("Doc-!->2");
      BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

     /* report.write("{\\rtf1\\ansi{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;\\ctextone\\ctint255\\cshade255\\red0\\green0\\blue0;}\n");
      report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");

      stmt = conn.prepareStatement("SELECT NazvanieRoditFull FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) report.write("\\fs24\\b\\qc{Ректору }{"+rs.getString(1)+"}\\b0\n");

      report.write("\\par\\par\n");

/************************************************/

    /*  String tip_Dok = new String();
      stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,Gorod_Prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\fs22\\ql{Я, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{гражданина: }\\i{"+rs.getString(4)+"}\\i0,  дата рождения: \\i{"+rs.getString(5)+"г.},\n");//\\i0{, место рождения: }\\i{"+rs.getString(16)+"}\\i0
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{проживающего по адресу: }\\i{"+rs.getString(6)+", ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        if(rs.getString(10) != null && rs.getString(10).equals("п"))
          tip_Dok = "Паспорт (серия-№)";
        else
          tip_Dok = "справка";
        report.write("{Документ, удостоверяющий личность: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" № "+rs.getString(12)+", выдан: "+rs.getString(13)+", "+rs.getString(14)+"г.}\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Контактный телефон: }\\i{"+rs.getString(15)+"}\\i0\n");
        report.write("\\par\\par\n");
      }

/************************************************/

     /* report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{Прошу допустить меня к участию в конкурсе для поступления на специальности или направления подготовки бакалавриата и(или) программы специалитета:}\n");
      report.write("\\par\\par\n");

/************************************************/

    /*  stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
//        if(abit_A.getTip_Spec().equals("д")) {
//          report.write("\\ql\\fs24\\b\\i\\tab{по дистанционной форме обучения}\\i0\\b0\n");
//        } else {
    	
        report.write("\\ql\\fs24\\b\\i\\tab{по очной форме обучения}\\i0\\b0\n");
//        }
        report.write("\\par\\par\\qj\\fs22\\tab{на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

    	 
        int first_line = 0;
        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           if(first_line++ > 0) {
             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
           }
if(rs.getString(4).equals("д")){
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           report.write("\\intbl\\row\n");
        }
    	  }
        //report.write("\\intbl");
        report.write("\\pard\\par\n");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       
      }

/************************************************/
     /* stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') AND kon.Dog IN('д') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) { 
      report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
      report.write("\\par\\par\n");
      
      report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{цел}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
      report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      //report.write("\\intbl\\qc\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      int first_line = 0;
      stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','ф','п') AND kon.Dog IN('д') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
           if(first_line++ > 0) {
           report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
           //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
           //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
         }
         //if(rs.getString(5).equals("д"))
           //{
         report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
         report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
         report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
         //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
         report.write("\\intbl\\row\n");
         //}
      }
     
      report.write("\\pard\\par\n");
      
   } 
      
      /************************************************/   
      
//System.out.println("Doc-!->3");
     /* stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('в','ф','п') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\pard\\par\n");
        report.write("\\ql\\fs24\\b\\i\\tab{по очно-заочной форме обучения}\\i0\\b0\n");

        report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");
        //report.write("\\intbl\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('в','ф','п') ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           report.write("\\intbl\\row\n");
        }
        
        //////////////////////////////////////////////////////////
        report.write("\\pard\\par\n");
        report.write("\\ql\\fs24\\b\\i\\tab{по заочной форме обучения}\\i0\\b0\n");
        ////////////////
        report.write("\\par\\par\\qj\\fs22\\tab{на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");
        report.write("\\intbl\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        ///report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           report.write("\\intbl\\row\n");
        }
        //report.write("\\intbl\n");
        report.write("\\pard\\par\n");
        /////////////////////////////////////////////////////////////////////
        report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных образовательных услуг}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{Специальности/направления, перечисленные в порядке приоритета зачисления}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('з','д','у') ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           report.write("\\intbl\\row\n");
          
        }
        report.write("\\intbl\n");
        report.write("\\pard\\par\n");
      }

/************************************************/

    /*  report.write("\\pard\\par\n");
      report.write("\\par\\ql\\fs24\\tab\\b{Прошу:}\\b0\\fs6\\par\n");
//System.out.println("Doc-!->4");
      int counter = 1;
////////////////////////////////////////////////ПОКА НЕ ТРОГАЛ !!!!
      stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". засчитать в качестве результатов вступительных испытаний оценки, полученные при сдаче}\\fs24{ЕГЭ:}\n");

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". засчитать в качестве результатов вступительных испытаний оценки, полученные при сдаче    }\\fs24{ЕГЭ:}\n");
        while(rs.next()) {
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+" - "+rs.getString(2)+" баллов;}\\i0");
        }
      }

/************************************************/
      //report.write("\\par\\par\\ql\\fs24\\tab\\b{Прошу:}\\b0\\fs6\\par\n");
      //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".в соответствии с разделом 8 правил приема ПГУ допустить к сдаче экзаменов, проводимых университетом самостоятельно по предметам:}\n");      

    /*  stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('е') or zso.Examen IN('в')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".в соответствии с разделом 8 правил приема ПГУ допустить к сдаче экзаменов, проводимых университетом самостоятельно по предметам:}\n");      

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('е') or zso.Examen IN('в')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
        	//report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".в соответствии с разделом 8 правил приема ПГУ допустить к сдаче экзаменов, проводимых университетом самостоятельно по предметам:}\n");
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
        }
      }

/************************************************/
      //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". в соответствии с разделом 9 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительных испытаний, проводимых университетом самостоятельно:}\n");
      
      //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('в') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      //rs = stmt.executeQuery();
      //if(rs.next()) {
      
        //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". в соответствии с разделом 9 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительных испытаний, проводимых университетом самостоятельно:}\n");

        //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('в') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
        //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        //rs = stmt.executeQuery();
        //while(rs.next()) {
      /*  stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
        	
        if(rs.getString(1)!=null){
           report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". в соответствии с разделом 9 правил приема ПГУ обеспечить следующие специальные условия при сдаче вступительных испытаний, проводимых университетом самостоятельно:}\n");
           report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
        }
       // }
     // }

/************************************************/
//////////////////////////////////////////////ПОКА НЕ ТРОГАЛ !!! конец
   /*   report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

      String tip_Ok_Zav = new String();
      stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

          tip_Ok_Zav = "общеобразовательное учреждение";

        } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

          tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

        } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

          tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";
        }

        report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"}, находящееся в {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  "+rs.getString(5)+" № "+rs.getString(6)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{Номер сертификата ЕГЭ / Место сдачи ЕГЭ: }\\i{"+rs.getString(7)+"}\\i0\n");
      }
/////
      report.write("\\par\\ql\\fs22{Место сдачи ЕГЭ в дополнительные сроки___________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{Место сдачи вступительных испытаний с использованием дистанционных технологий ___________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{Место получения предыдущего образования__________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
 //////


      stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
          report.write("\\b0\\fs6\\par\n");
          String predpr = new String();
          if(rs.getString(1).equals("к")) predpr = "(Роскосмос)";
          else if(rs.getString(1).equals("а")) predpr = "(Росатом)";
          else if(rs.getString(1).equals("т")) predpr = "(Минпромторг)";
          else predpr = "предприятия";
          report.write("\\par\\ql\\fs22{Целевое направление }\\i{"+predpr+"}\n");//\\i0{ для участия в целевом конкурсе
        }
      }



      stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
          report.write("\\b0\\fs6\\par\n");
          report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1).toUpperCase()+"  (\"З\", \"С\" - золотая и серебряная медали; \"Т\" - диплом с отличием УСПО)}\\i0\\fs6\\par\n");
        }
      }

//System.out.println("Doc-!->5");


      String in_Jaz = new String();
      String need_Obshaga = new String();
      stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && rs.getString(1).equals("а")) {

          in_Jaz = "английский";

        } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

          in_Jaz = "немецкий";

        } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

          in_Jaz = "французский";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("д")) {

          need_Obshaga = "нуждаюсь";

        } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

          need_Obshaga = "не нуждаюсь";
        }
//        report.write("\\par\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
             report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
          if(rs.getString(2) != null && rs.getString(2).length() > 3)
            report.write("\\par\\ql\\fs22{Преимущественное право при поступлении: }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
        }
        report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
      }



   //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
   //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\par\n");
      
      report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
      report.write("\\par\\ql\\fs22{оригиналов документов)      ____________________________________________________________________}\n");
      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): _____________________________}\n");
      report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      report.write("\\par\\par\n");
      
      //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
      //report.write("\\pard\\page\n");
*/
/************************************************/
/***********      СТРАНИЦА № 2      *************/
/************************************************/
/*      report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

      String tip_Ok_Zav = new String();
      stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

          tip_Ok_Zav = "общеобразовательное учреждение";

        } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

          tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

        } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

          tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";
        }

        report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"}, находящееся в {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  "+rs.getString(5)+" № "+rs.getString(6)+"}\\i0\\fs6\\par\n");
        //report.write("\\par\\ql\\fs22{Аттестат / Диплом: }\\i{"+rs.getString(7)+"}\\i0\n");
      }
/////
      report.write("\\par\\ql\\fs22{Место сдачи ЕГЭ в дополнительные сроки___________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{Место сдачи вступительных испытаний с использованием дистанционных технологий ___________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{Место получения предыдущего образования__________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
 //////
/************************************************/

   /*   stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
          report.write("\\b0\\fs6\\par\n");
          String predpr = new String();
          if(rs.getString(1).equals("к")) predpr = "(Роскосмос)";
          else if(rs.getString(1).equals("а")) predpr = "(Росатом)";
          else if(rs.getString(1).equals("т")) predpr = "(Минпромторг)";
          else predpr = "предприятия";
          report.write("\\par\\ql\\fs22{Целевое направление }\\i{"+predpr+"}\n");//\\i0{ для участия в целевом конкурсе
        }
      }

/************************************************/

   /*   stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
          report.write("\\b0\\fs6\\par\n");
          report.write("\\par\\ql\\fs22{Индивидуальные достижения: }\\i{"+rs.getString(1).toUpperCase()+"  (\"З\", \"С\" - золотая и серебряная медали; \"Т\" - диплом с отличием УСПО)}\\i0\\fs6\\par\n");
        }
      }

//System.out.println("Doc-!->5");
/************************************************/

     /* String in_Jaz = new String();
      String need_Obshaga = new String();
      stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && rs.getString(1).equals("а")) {

          in_Jaz = "английский";

        } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

          in_Jaz = "немецкий";

        } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

          in_Jaz = "французский";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("д")) {

          need_Obshaga = "нуждаюсь";

        } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

          need_Obshaga = "не нуждаюсь";
        }
//        report.write("\\par\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          if(rs.getString(2) != null && rs.getString(2).length() > 3)
             report.write("\\par\\ql\\fs22{Особые права при поступлении }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//Имею льготу при поступлении согласно:
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
            report.write("\\par\\ql\\fs22{Преимущественное право при поступлении: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//Победитель олимпиады
        }
        report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
      }

/************************************************/

   //  report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
   //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\par\n");
     /* stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
    	  if(rs.getString(1) != null){
      report.write("\\par\\ql\\fs22{Способ возврата документов в случае не поступления в университет(в случае предоставления        }\n");
      report.write("\\par\\ql\\fs22{оригиналов документов)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
      //report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22{Почтовый адрес и(или) электронный адрес (по желанию поступающего): Email-"+rs.getString(2)+" Почтовый адрес-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\par\n");
      }
      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22{С копией лицензии на осуществление трудовой деятельности(с приложениями) серия 90ЛО1 №0000704, от}");
      report.write("\\par\\ql\\fs22{21 марта 2013 г.; с копией свидетельства о государственной аккредитации (с приложениями) от 17.05.2013 г.}");  
      report.write("\\par\\ql\\fs22{серия 90АО1 №0000631 рег.№0627; c информацией о предоставляемых поступающим особых правах и преимуществах}\n");
      report.write("\\par\\ql\\fs22{при приеме на обучение по программам бакалавриата и программам специалитета с датами завершения представления}\n");
      report.write("\\par\\ql\\fs22{поступающими оригинала документа установленного образца на каждом этапе зачисления на места в рамках}\n");
      report.write("\\par\\ql\\fs22{контрольных цифр, с датами завершения представления поступающими сведений о согласии на зачисление на места по}\n");
      report.write("\\par\\ql\\fs22{договорам об оказании платных образовательных услуг; с правилами подачи апелляции по результатам вступительных}\n");
      report.write("\\par\\ql\\fs22{испытаний, проводимых организацией самостоятельно ознакомлен(а)}\n");
      report.write("\\par\\ql\\fs22{                                                                                        _________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
      report.write("\\par\\par\n");
      
      //report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
      //report.write("\\pard\\page\n");
      
      /************************************************/

   /*   report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab{Выражаю свое согласие на обработку персональных данных в порядке, установленном Федеральным законом от 27 июля 2006г. № 152-ФЗ }\\'ab{О персональных данных}\\'bb.\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");*/
      /*report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab{Согласен на обработку своих персональных данных                                }\\'ab{_________________}\\'bb.\n"); 
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/
     // report.write("\\par\\par\\ql\\fs22\\tab{Высшее профессиональное образование получаю }\\i{впервые.}\\i0\n");
     //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
     //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
      
    /*  report.write("\\par\\par\\par\n");
    
      report.write("\\par\\par\\ql\\fs22\\tab{Ознакомлен(а) с информацией об ответсвенности за достоверность сведений, указываемых в заявлении о }\n");
      report.write("\\par\\par\\ql\\fs22\\tab{приеме, и за подлинность документов, подаваемых для поступления)         }\\i{_________________}\\i0\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
      report.write("\\par\\par\\par\n");

/************************************************/
     /* report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю отсутствие диплома бакалавра, диплома специалиста, диплома магистра(при поступлении }\n");
      report.write("\\par\\par\\ql\\fs22\\tab{на обучение на места в рамках контрольных цифр)                          }\\i{_________________}\\i0\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
      report.write("\\par\\par\\par\n");
      
      report.write("\\ql\\fs22\\tab{Подтверждаю факт подачи заявления не более чем в пять вузов РФ.                  }\\b0{_________________}\\bb.\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
     
      report.write("\\par\\par\\ql\\fs22\\tab{Подтверждаю подачу заявления о приеме на основании соответсвующего особого права только В ПГУ и}\n");
      report.write("\\par\\par\\ql\\fs22\\tab{только на одну образовательную программу)                                }\\i{_________________}\\i0\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
      report.write("\\par\\par\\par\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\par\\qj\\fs22\\tab{С Лицензией на право осуществления образовательной деятельности серия 90ЛО1 №0000704, от 21 марта 2013 г., свидетельством о государственной аккредитации от 25.06.2012 серия 90АО1 № 000007 рег.№0007 и приложений к ним, программами вступительных испытаний, правилами приёма ПГУ, правилами проведения апелляций ознакомлен(а).}\n");
      //report.write("\\par\n");
      //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\ql\\fs22\\tab{Сведения о сдаче ЕГЭ и его результатах в приемную комиссию предоставил(а).}\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\qj\\fs22\\tab\\b{Ознакомлен(а) с датой предоставления оригинала документа государственного образца об образовании в соответствии с порядком зачисления (п.14) правил приема.}\\b0\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");



     /* report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22\\tab{Дата: "+StringUtil.CurrDate(".")+"г.}\n");
      report.write("}"); 
      report.close();
*/
//System.out.println("Doc-!->6");

   // else if ( abit_A.getNeed_Spo().equals("д")) {
    	  else if ( abit_A.getNomerPotoka() == 6) {

/***********************************************************/
/***********************************************************/
/*****            ЗАЯВЛЕНИЕ на СПО (ю,п)              ******/
/***********************************************************/
/***********************************************************/

      String file_con = new String("statementSPO"+abit_A.getKodAbiturienta()+".rtf");
 
      String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;

      abit_A.setFileName1(file_con);

      BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

      report.write("{\\rtf1\\ansi{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;\\ctextone\\ctint255\\cshade255\\red0\\green0\\blue0;}\n");
      report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");

      stmt = conn.prepareStatement("SELECT NazvanieRoditFull FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) report.write("\\fs24\\b\\qc{Ректору }{"+rs.getString(1)+"}\\b0\n");

      report.write("\\par\\par\n");

/************************************************/

      String tip_Dok = new String();
      stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\fs22\\ql{Я, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{гражданин(ка): }\\i{"+rs.getString(4)+"}\\i0,  дата рождения: \\i{"+rs.getString(5)+"г.}\\i0,\n");//\\i0{, место рождения: }\\i{"+rs.getString(16)+"}
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{проживающий(щая) по адресу: }\\i{"+rs.getString(6)+", ул."+rs.getString(7)+", д."+rs.getString(8)+", кв."+rs.getString(9)+"}.\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        if(rs.getString(10) != null && rs.getString(10).equals("п"))
          tip_Dok = "паспорт";
        else
          tip_Dok = "справка";
        report.write("{Документ, удостоверяющий личность: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" № "+rs.getString(12)+", выдан: "+rs.getString(13)+", "+rs.getString(14)+"г.}\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Контактный телефон: }\\i{"+rs.getString(15)+"}\\i0\n");
        report.write("\\par\\par\n");
      }

/************************************************/

      report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{Прошу рассмотреть возможность зачисления на специальности среднего профессионального образования:}\n");
      report.write("\\par\\par\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','у','ф','п') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\ql\\fs24\\b\\i\\tab{по очной форме обучения}\\i0\\b0\n");
        
        report.write("\\par\\par\\qj\\fs22\\tab{на места в рамках контрольных цифр приема граждан на обучение за счет бюджетных ассигнований}\n");

        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\qc\\b{Специальность}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{цел.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{иито}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        report.write("\\intbl\\ql{\\tab\\b{N}\\b0}\\cell\n");
        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        int first_line = 0;
        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','у','ф','п') AND kon.Bud LIKE ('д') ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           if(first_line++ > 0) {
             report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvertalc \\clshdngraw9999 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
           }
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           report.write("\\intbl\\row\n");
        }
      }

/************************************************/

      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE 'д' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('о','м','ю','у','ф','п') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\pard\\par\n");
        //report.write("\\ql\\fs24\\b\\i\\tab{по очной}\\i0\\b0\n");
        report.write("\\ql\\fs24\\b\\i\\tab{по очной форме обучения}\\i0\\b0\n");
        
        report.write("\\par\\par\\qj\\fs22\\tab{на места по договорам об оказании платных услуг}\n");

        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{Специальность}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{бюд}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{дог}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{полн.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{сокр.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2000\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      Код}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{Полное наименование}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        //report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('о','м','ю','у','ф','п') AND kon.Dog IN('д') ORDER BY kon.Prioritet ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
           report.write("\\intbl\\qc{"+rs.getString(2)+"}\\cell\n");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(4))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(5))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(6))+"}\\cell\n");
           //report.write("\\intbl\\qc{"+StringUtil.val_to_plus(rs.getString(7))+"}\\cell\n");
           report.write("\\intbl\\row\n");
        }
      }

/************************************************/

      report.write("\\pard\\par\n");
      //report.write("\\par\\ql\\fs24\\tab\\b{Прошу:}\\b0\\fs6\\par\n");

      int counter = 1;

      //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
     // stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      //rs = stmt.executeQuery();
      //if(rs.next()) {

       // report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". засчитать в качестве результатов вступительных испытаний оценки, полученные при сдаче }\\fs24{ЕГЭ:}\n");

       // stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
      //  stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       // rs = stmt.executeQuery();
      //  while(rs.next()) {
      //     report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+" - "+rs.getString(2)+" баллов;}\\i0");
      //  }
     // }

/************************************************/
/*
      stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('е') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". допустить к сдаче экзаменов в формате }\\fs24{ЕГЭ }\\fs22{по предметам:}\n");      

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('е') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
       while(rs.next()) {
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
       }
      }
*/
/************************************************/
/*
      stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('в') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
     stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
      
        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". допустить к сдаче экзаменов в формате вуза по предметам:}\n");

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('в') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
        }
      }
*/
/************************************************/

      report.write("\\par\\par\\ql\\fs24\\tab\\b{О себе сообщаю следующее:}\\b0\\fs6\\par\n");

      String tip_Ok_Zav = new String();
   //   stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
      stmt = conn.prepareStatement(" SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija, k.name, a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,z.PolnoeNaimenovanieZavedenija FROM Zavedenija z, Abiturient a, KLADR k where a.kodzavedenija=z.kodzavedenija and k.code = a.gorod_Prop AND a.KodAbiturienta LIKE  ?");
      

      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {

          tip_Ok_Zav = "общеобразовательное учреждение";

        } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

          tip_Ok_Zav = "общеобразовательное учреждение начального профессионального образования";

        } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

          tip_Ok_Zav = "общеобразовательное учреждение среднего профессионального образования";
        }

        report.write("\\par\\ql\\fs22{Окончил(а) в }\\i{"+rs.getString(1)+"г. }{"+tip_Ok_Zav+"}, находящееся в {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  № "+rs.getString(6)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{Уровень предыдущего образования:}\n"); 
        if(rs.getString(2) != null && (rs.getString(2).equals("ш") || rs.getString(2).equals("д") || rs.getString(2).equals("в"))) {
        	 report.write("\\par\\ql\\fs22{среднее (полное) общее образование}\n");
        } else if(rs.getString(2) != null && rs.getString(2).equals("п")) {

        	report.write("\\par\\ql\\fs22{основное общее образование}\n");

          } else if(rs.getString(2) != null && rs.getString(2).equals("т")) {

        	  report.write("\\par\\ql\\fs22{среднее профессиональное образование}\n");
          }
          else if(rs.getString(2) != null && rs.getString(2)!="т" && rs.getString(2)!="п" && rs.getString(2)!="п" && rs.getString(2)!="ш" && rs.getString(2)!="д" && rs.getString(2)!="в") {

        	  report.write("\\par\\ql\\fs22{высшее образование}\n");
          }
        
        //report.write("\\par\\ql\\fs22{Номер сертификата ЕГЭ / Место сдачи ЕГЭ: }\\i{"+rs.getString(7)+"}\\i0\n");
      }

/************************************************/
/*
      stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
          report.write("\\b0\\fs6\\par\n");
          String predpr = new String();
          if(rs.getString(1).equals("к")) predpr = "предприятия (Роскосмос)";
          else if(rs.getString(1).equals("а")) predpr = "предприятия (Росатом)";
          else if(rs.getString(1).equals("т")) predpr = "предприятия (Минпромторг)";
          else predpr = "предприятия";
          report.write("\\par\\ql\\fs22{Имею направление от }\\i{"+predpr+"}\\i0{ для участия в целевом конкурсе}\n");
        }
      }
*/
/************************************************/
/*
      stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("н")) {
          report.write("\\b0\\fs6\\par\n");
          report.write("\\par\\ql\\fs22{Отличия: }\\i{"+rs.getString(1).toUpperCase()+"  (\"З\", \"С\" - золотая и серебряная медали; \"Т\" - диплом с отличием УСПО)}\\i0\\fs6\\par\n");
        }
      }
*/
      report.write("\\par\\ql\\fs22{Место получения предыдущего образования__________________________________________________________}\n");      
      
//report.write("\\pard\\page\n");
      
      report.write("\\par\\par\n");
      report.write("\\par\\par\\qj\\fs22\\tab{Результаты освоения основного общего или среднего (полного) общего образования:}\n");
      report.write("\\par\\par\n");
      
      int rus=0, lit=0, inYz=0, matem=0, inf=0, ist=0, obch=0, fiz=0;
      
      stmt = conn.prepareStatement("SELECT KodPredmeta, OtsenkaAtt FROM Oa WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
    	if(rs.getInt(1)==3){
    		rus = rs.getInt(2);
    	}else{
			if(rs.getInt(1)==10){
				lit = rs.getInt(2);  		  
			}else{
				if(rs.getInt(1)==5){
					inYz = rs.getInt(2);
				}else{
					if(rs.getInt(1)==1){
						matem = rs.getInt(2);	  
					}else{
						if(rs.getInt(1)==8){
							inf = rs.getInt(2);  
						}else{
							if(rs.getInt(1)==4){
								ist = rs.getInt(2);	  
							}else{
								if(rs.getInt(1)==9){
									obch = rs.getInt(2);	  
								}else{
									if(rs.getInt(1)==2){
										fiz = rs.getInt(2);	  
									}
								}
							}
						}
					}
				}
			}
    	}
    	  
      }
      
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Предметы\\intbl\\cell");
      report.write("Оценки\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Русский язык\\intbl\\cell");
      report.write("{"+rus+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Литература\\intbl\\cell");
      report.write("{"+lit+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Иностранный язык\\intbl\\cell");
      report.write("{"+inYz+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Математика\\intbl\\cell");
      report.write("{"+matem+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Информатика и ИКТ\\intbl\\cell");
      report.write("{"+inf+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("История\\intbl\\cell");
      report.write("{"+ist+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Обществознание\\intbl\\cell");
      report.write("{"+obch+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Физика\\intbl\\cell");
      report.write("{"+fiz+"}\\intbl\\cell");
      report.write("\\row");
      report.write("");
      report.write("\\pard\\par\n");
      
/************************************************/

      String in_Jaz = new String();
      String need_Obshaga = new String();
      stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
    	  in_Jaz = rs.getString(1);
        if(rs.getString(1) != null && rs.getString(1).equals("а")) {

          in_Jaz = "английский";

        } else if(rs.getString(1) != null && rs.getString(1).equals("н")) {

          in_Jaz = "немецкий";

        } else if(rs.getString(1) != null && rs.getString(1).equals("ф")) {

          in_Jaz = "французский";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("д")) {

          need_Obshaga = "нуждаюсь";

        } else if(rs.getString(1) != null && rs.getString(2).equals("н")) {

          need_Obshaga = "не нуждаюсь";

        }
//        report.write("\\par\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
             report.write("\\par\\ql\\fs22{Победитель олимпиады: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");
          if(rs.getString(2) != null && rs.getString(2).length() > 3)
            report.write("\\par\\ql\\fs22{Имею льготу при поступлении согласно: }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");
        }
        report.write("\\par\\ql\\fs22{Изучал(а) иностранный язык: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{В общежитии: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
      }

/************************************************/
/*
      report.write("\\par\\ql\\fs22{О себе дополнительно сообщаю: ___________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      report.write("\\par\\par\n");
      report.write("\\par\\qj\\fs20\\b\\i{Вниманию абитуриентов! Лица, имеющие льготы при поступлении в соответствии с пп.3.5, 3.6, 3.7 правил приема, должны предоставить ксерокопии подтверждающих документов ответственному секретарю приемной комиссии.}\\i0\\b0\n");
      report.write("\\par\\par");
  */    
     /* 
      report.write("\\pard\\page\n");
      
      report.write("\\par\\par\n");
      report.write("\\par\\par\\qj\\fs22\\tab{Результаты освоения основного общего или среднего (полного) общего образования:}\n");
      report.write("\\par\\par\n");
      
      int rus=0, lit=0, inYz=0, matem=0, inf=0, ist=0, obch=0, fiz=0;
      
      stmt = conn.prepareStatement("SELECT KodPredmeta, OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
    	if(rs.getInt(1)==3){
    		rus = rs.getInt(2);
    	}else{
			if(rs.getInt(1)==10){
				lit = rs.getInt(2);  		  
			}else{
				if(rs.getInt(1)==5){
					inYz = rs.getInt(2);
				}else{
					if(rs.getInt(1)==1){
						matem = rs.getInt(2);	  
					}else{
						if(rs.getInt(1)==8){
							inf = rs.getInt(2);  
						}else{
							if(rs.getInt(1)==4){
								ist = rs.getInt(2);	  
							}else{
								if(rs.getInt(1)==9){
									obch = rs.getInt(2);	  
								}else{
									if(rs.getInt(1)==2){
										fiz = rs.getInt(2);	  
									}
								}
							}
						}
					}
				}
			}
    	}
    	  
      }
      
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Предметы\\intbl\\cell");
      report.write("Оценки\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Русский язык\\intbl\\cell");
      report.write("{"+rus+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Литература\\intbl\\cell");
      report.write("{"+lit+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Иностранный язык\\intbl\\cell");
      report.write("{"+inYz+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Математика\\intbl\\cell");
      report.write("{"+matem+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Информатика и ИКТ\\intbl\\cell");
      report.write("{"+inf+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("История\\intbl\\cell");
      report.write("{"+ist+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Обществознание\\intbl\\cell");
      report.write("{"+obch+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("Физика\\intbl\\cell");
      report.write("{"+fiz+"}\\intbl\\cell");
      report.write("\\row");
      report.write("");
      report.write("\\pard\\par\n");
      
      
      
      */
      
      /*
       * 
       * */
      
       
       
      
      

/************************************************/
/***********      СТРАНИЦА № 2      *************/
/************************************************/
      report.write("\\par\\par\\qr\\fs22{ }\n");
      report.write("\\par\\par\\ql\\fs22\\tab{Среднее профессиональное образование получаю }\\i{впервые.}\\i0\n");
      report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      report.write("\\par\\par\\par\n");
      report.write("\\par\\qj\\fs22\\tab{С копией лицензии на право осуществления образовательной деятельности серия 90ЛО1 №0000704, от 21 марта 2013 г., с копией свидетельства о государственной аккредитации от 17.05.2013 серия 90АО1 № 000631 рег.№0627 ознакомлен(а).}\n");
      report.write("\\par\n");
      report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/
/*
      report.write("\\par\\par\\par\n");
      report.write("\\ql\\fs22\\tab{Сведения о сдаче ЕГЭ и его результатах в приемную комиссию предоставил(а).}\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");
*/
/************************************************/

      report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab\\b{Ознакомлен(а) с датой предоставления оригинала документа государственного образца об образовании в соответствии с порядком зачисления (п.7) правил приема.}\\b0\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab{Cогласен на обработку своих персональных данных}\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(подпись поступающего)}\n");

/************************************************/

      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22\\tab{Дата: "+StringUtil.CurrDate(".")+"г.}\n");

      report.write("}"); 
      report.close();
   }



/************************************************/
/************************************************/
/******             РАСПИСКА              *******/
/************************************************/
/************************************************/

      String file_con = new String("receipt"+abit_A.getKodAbiturienta()+".rtf");
 
      String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;

      abit_A.setFileName2(file_con);

      BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

      report.write("{\\rtf1\\ansi\n");
      report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
      report.write("\\fs24\\i{Экземпляр хранится в личном деле}\\i0\\par\\par\n");

      report.write("\\trowd\\ts15\\trqc\\trgaph108\\trleft-108\\brdrs\\brdrw10\n");
      report.write("\\clvertalt\\cltxlrtb\\cellx7011\n");
      report.write("\\clvertalt\\clbrdrr\\brdrdash\\brdrw20\\cltxlrtb\\cellx7364\n");
      report.write("\\clvertalt\\clbrdrl\\brdrdash\\brdrw20\\cltxlrtb\\cellx7717\n");
      report.write("\\clvertalt\\cltxlrtb\\cellx14730");

/************************************************/
/************** ЛЕВАЯ ПОЛОВИНКА *****************/
/************************************************/

      report.write("\\pard\\plain\\qc\\intbl\n");

      stmt = conn.prepareStatement("SELECT NazvanieVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) report.write("\\fs24\\caps\\ul\\qc{"+rs.getString(1)+"}\\ul0\\caps0\n");

      report.write("\\par\\par\n");

      stmt = conn.prepareStatement("SELECT NomerLichnogoDela FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) report.write("\\qc\\fs28\\b{ОПИСЬ ЛИЧНОГО ДЕЛА № "+rs.getString(1)+"}\\par\\b0\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT UPPER(f.AbbreviaturaFakulteta) FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\fs24\\par\\ql{поступающего на факультет (институт):  }\\b{"+rs.getString(1)+"}\\b0\n");
        report.write("\\fs6\\par\\par\\fs24{по специальностям (направлениям):}\n");
        report.write("\\fs6\\par\\par\\par\n");
      }

/************************************************/

      report.write("\\pard\\plain\n");
      report.write("\\qc \\intbl\\itap2\n");

      report.write("\\b{Шифр}\\nestcell{\\nonesttables\\par }\n");
      report.write("{Наименование специальности (направления)}\\b0\\nestcell\\b0{\\nonesttables\\par }\n");
      report.write("\\pard\n");
      report.write("\\intbl\\itap2\n");
      report.write("{{\\*\\nesttableprops\\trowd \\irow0\\irowband0\n");
      report.write("\\ts11\\trqc\\trgaph108\\trrh280\\trleft5\\trhdr\\trpaddl108\\trpaddr108\\trpaddfl3\\trpaddfr3\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10\\clbrdrl\\brdrs\\brdrw10\\clbrdrr\\brdrs\\brdrw10\\cltxlrtb\\cellx1341\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10\\clbrdrl\\brdrs\\brdrw10\\clbrdrr\\brdrs\\brdrw10\\cltxlrtb\\cellx6898\\nestrow}\n");
      report.write("{\\nonesttables\\par }}\n");

      int counter = 1;
      stmt = conn.prepareStatement("SELECT s.ShifrSpetsialnosti,s.NazvanieSpetsialnosti FROM Konkurs k, Spetsialnosti s WHERE k.KodSpetsialnosti=s.KodSpetsialnosti AND k.KodAbiturienta LIKE ? Order BY k.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
        report.write("\\pard \\qc \\intbl\\itap2 {"+rs.getString(1)+"\\nestcell{\\nonesttables\\par }}\n");
        report.write("\\pard \\ql \\intbl\\itap2 {"+rs.getString(2)+"\\nestcell{\\nonesttables\\par }}\n");
        report.write("\\pard \\ql \\intbl\\itap2 {\n");
        report.write("{\\*\\nesttableprops\\trowd \\irow"+counter+"\\irowband"+(counter++)+"\n");
        report.write("\\ts11\\trqc\\trgaph108\\trrh280\\trleft5\\trpaddl108\\trpaddr108\\trpaddfl3\\trpaddfr3\n");
        report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cltxlrtb \\cellx1341\n");
        report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cltxlrtb \\cellx6898\\nestrow}\n");
        report.write("{\\nonesttables\\par }}\n");
      }

/************************************************/

      report.write("\\trowd \\irow0\\irowband0\\lastrow \\ts15\\trqc\\trgaph108\\trleft-108\\trbrdrt\\brdrs\\brdrw10\\brdrcf17 \\trbrdrl\\brdrs\\brdrw10\\brdrcf17 \\trbrdrb\\brdrs\\brdrw10\\brdrcf17 \\trbrdrr\\brdrs\\brdrw10\\brdrcf17 \\trbrdrh\\brdrs\\brdrw10\\brdrcf17 \\trbrdrv\n");
      report.write("\\brdrs\\brdrw10\\brdrcf17 \\trpaddl108\\trpaddr108 \\clvertalt \\cltxlrtb \\cellx7011\\clvertalt\\clbrdrr\\brdrdash\\brdrw20 \\cltxlrtb \\cellx7364\n");
      report.write("\\clvertalt\\clbrdrl\\brdrdash\\brdrw20\\cltxlrtb \\cellx7717\\clvertalt\\cltxlrtb \\cellx14730\\pard\\plain \\ql \\intbl\n");

      stmt = conn.prepareStatement("SELECT a.Familija,a.Imja,a.Otchestvo,a.Tel FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\par\\ql\\fs24\\tab{Абитуриент  }\\b{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\b0\\par\n");
      }

      String copyDokSrObr = new String();
      String copySert = new String();
      stmt = conn.prepareStatement("SELECT a.VidDokSredObraz,SeriaAtt,NomerAtt,NomerSertifikata,a.Tel,a.TipDokSredObraz,a.KopijaSertifikata FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(6) != null && rs.getString(6).equals("о"))
          copyDokSrObr = " (оригинал)";
        else
          copyDokSrObr = " (копия)";

        if(rs.getString(7) != null && rs.getString(7).equals("о"))
          copySert = " (оригинал)";
        else
          copySert = " (копия)";

        report.write("\\par\\ql\\fs24\\tab{Предоставил(а) следующие документы:}\n");
        report.write("\\fs4\\par\\par\\fs24\\ql{1. Заявление}\n");
        report.write("\\fs4\\par\\par\\fs24\\ql{2. Шесть фотографий}\n");
        report.write("\\fs4\\par\\par\\fs24\\ql{3. "+(rs.getString(1)).substring(0,1).toUpperCase()+(rs.getString(1)).substring(1)+": № "+rs.getString(3)+copyDokSrObr+"}\n");
        if(rs.getString(4) != null && !rs.getString(4).equals("-"))
          report.write("\\fs4\\par\\par\\fs24\\ql{4. Свидетельство ЕГЭ: "+rs.getString(4)+copySert+"}\n");
        else
          report.write("\\fs4\\par\\par\\fs24\\ql{4.}\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(1) != null && rs.getString(1).length() > 3) {
             report.write("\\ql{5. Диплом победителя олимпиады: }\\i{"+rs.getString(1)+"}\\i0\n");
          } else {
             report.write("\\ql{5.}\n");
          }

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(2) != null && rs.getString(2).length() > 3) {
             report.write("\\ql{6. Документ, подтверждающий льготу: }\\i{"+rs.getString(2)+"}\\i0\n");
          } else {
             report.write("\\ql{6.}\n");
          }
        }
        report.write("\\par\\par\n");
      }

/************************************************/

      report.write("\\par\\ql\\fs24{Документы принял}\n");
      report.write("\\par\\ql\\i\\tab{сотрудник приемной комиссии: _______________________}\\i0\n");
      report.write("\\par\\ql\\i\\tab{"+StringUtil.CurrDate(".")+"г.}\\i0\\par\n");
      report.write("\\par\\ql\\fs24{Дело передано на факультет: ___________________________}\n");
      report.write("\\par\\ql\\i\\tab{Принял}\\i0\n");
      report.write("\\par\\ql\\i\\tab{сотрудник приемной комиссии: _______________________}\\i0\n");
      report.write("\\par\\ql\\i\\tab{«____»_________"+StringUtil.CurrYear()+"г.}\\i0\n");
      report.write("\\cell\\pard\\plain\n");

/*************************************************/
/************** ПРАВАЯ ПОЛОВИНКА *****************/
/*************************************************/

      report.write("\\qc\\intbl\n");
      report.write("\\cell\\pard\n");
      report.write("\\intbl\\cell\n");

      stmt = conn.prepareStatement("SELECT NazvanieVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) report.write("\\fs24\\caps\\ul\\qc{"+rs.getString(1)+"}\\ul0\\caps0\n");

      report.write("\\par\\par\n");

      stmt = conn.prepareStatement("SELECT NomerLichnogoDela FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) report.write("\\qc\\fs28\\b{РАСПИСКА № "+rs.getString(1)+"}\\par\\b0\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT UPPER(f.AbbreviaturaFakulteta) FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\fs24\\par\\ql{в приеме документов на факультет (институт):  }\\b{"+rs.getString(1)+"}\\b0\n");
        report.write("\\fs6\\par\\par\\fs24\n");
        report.write("{по специальностям (направлениям):}\n");
        report.write("\\fs6\\par\\par\\fs22\n");
      }

/************************************************/

      report.write("\\pard\\plain\n");
      report.write("\\qc \\intbl\\itap2\n");

      report.write("\\b{Шифр}\\nestcell{\\nonesttables\\par }\n");
      report.write("{Наименование специальности (направления)}\\b0\\nestcell\\b0{\\nonesttables\\par }\n");
      report.write("\\pard\n");
      report.write("\\intbl\\itap2\n");
      report.write("{{\\*\\nesttableprops\\trowd \\irow0\\irowband0\n");
      report.write("\\ts11\\trqc\\trgaph108\\trrh280\\trleft5\\trhdr\\trpaddl108\\trpaddr108\\trpaddfl3\\trpaddfr3\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10\\clbrdrl\\brdrs\\brdrw10\\clbrdrr\\brdrs\\brdrw10\\cltxlrtb\\cellx1341\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10\\clbrdrl\\brdrs\\brdrw10\\clbrdrr\\brdrs\\brdrw10\\cltxlrtb\\cellx6898\\nestrow}\n");
      report.write("{\\nonesttables\\par }}\n");

      counter = 1;
      stmt = conn.prepareStatement("SELECT s.ShifrSpetsialnosti,s.NazvanieSpetsialnosti FROM Konkurs k, Spetsialnosti s WHERE k.KodSpetsialnosti=s.KodSpetsialnosti AND k.KodAbiturienta LIKE ? Order BY k.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) {
        report.write("\\pard \\qc \\intbl\\itap2 {"+rs.getString(1)+"\\nestcell{\\nonesttables\\par }}\n");
        report.write("\\pard \\ql \\intbl\\itap2 {"+rs.getString(2)+"\\nestcell{\\nonesttables\\par }}\n");
        report.write("\\pard \\ql \\intbl\\itap2 {\n");
        report.write("{\\*\\nesttableprops\\trowd \\irow"+counter+"\\irowband"+(counter++)+"\n");
        report.write("\\ts11\\trqc\\trgaph108\\trrh280\\trleft5\\trpaddl108\\trpaddr108\\trpaddfl3\\trpaddfr3\n");
        report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10\\clbrdrl\\brdrs\\brdrw10\\clbrdrb\\brdrs\\brdrw10\\clbrdrr\\brdrs\\brdrw10\\cltxlrtb\\cellx1341\n");
        report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw10\\clbrdrl\\brdrs\\brdrw10\\clbrdrb\\brdrs\\brdrw10\\clbrdrr\\brdrs\\brdrw10\\cltxlrtb\\cellx6898\\nestrow}\n");
        report.write("{\\nonesttables\\par }}\n");
      }

      report.write("\\trowd \\irow0\\irowband0\\lastrow \\ts15\\trqc\\trgaph108\\trleft-108\\trbrdrt\\brdrs\\brdrw10\\brdrcf17 \\trbrdrl\\brdrs\\brdrw10\\brdrcf17 \\trbrdrb\\brdrs\\brdrw10\\brdrcf17 \\trbrdrr\\brdrs\\brdrw10\\brdrcf17 \\trbrdrh\\brdrs\\brdrw10\\brdrcf17 \\trbrdrv\n");
      report.write("\\brdrs\\brdrw10\\brdrcf17 \\trpaddl108\\trpaddr108 \\clvertalt\\cltxlrtb \\cellx7011\\clvertalt\\clbrdrr\\brdrdash\\brdrw20 \\cltxlrtb \\cellx7364\n");
      report.write("\\clvertalt\\clbrdrl\\brdrdash\\brdrw20\\cltxlrtb \\cellx7717\\clvertalt\\cltxlrtb \\cellx14730\\pard\\plain \\ql \\intbl\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT a.Familija,a.Imja,a.Otchestvo,a.Tel FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\par\\ql\\fs24\\tab{Абитуриент  }{\\b"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\b0\\par\n");
      }

      stmt = conn.prepareStatement("SELECT a.VidDokSredObraz,SeriaAtt,NomerAtt,NomerSertifikata,a.Tel,a.TipDokSredObraz,a.KopijaSertifikata FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(6) != null && rs.getString(6).equals("о"))
          copyDokSrObr = " (оригинал)";
        else
          copyDokSrObr = " (копия)";

        if(rs.getString(7) != null && rs.getString(7).equals("о"))
          copySert = " (оригинал)";
        else
          copySert = " (копия)";

        report.write("\\par\\ql\\fs24\\tab{Предоставил(а) следующие документы:}\n");
        report.write("\\fs4\\par\\par\\fs24");
        report.write("\\ql{1. Заявление}\n");
        report.write("\\fs4\\par\\par\\fs24");
        report.write("\\ql{2. Шесть фотографий}\n");
        report.write("\\fs4\\par\\par\\fs24");
        report.write("\\ql{3. "+(rs.getString(1)).substring(0,1).toUpperCase()+(rs.getString(1)).substring(1)+": № "+rs.getString(3)+copyDokSrObr+"}\n");
        report.write("\\fs4\\par\\par\\fs24");
        if(rs.getString(4) != null && rs.getString(4).length() > 3)
          report.write("\\ql{4. Свидетельство ЕГЭ: "+rs.getString(4)+copySert+"}\n");
        else
          report.write("\\ql{4.}\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(1) != null && rs.getString(1).length() > 3) {
             report.write("\\ql{5. Диплом победителя олимпиады: }\\i{"+rs.getString(1)+"}\\i0\n");
          } else {
             report.write("\\ql{5.}\n");
          }

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(2) != null && rs.getString(2).length() > 3) {
             report.write("\\ql{6. Документ, подтверждающий льготу: }\\i{"+rs.getString(2)+"}\\i0\n");
          } else {
             report.write("\\ql{6.}\n");
          }
        }

      }

/************************************************/

      report.write("\\par");
      report.write("\\par\\qj\\fs24{Предупрежден(а) об обязанности замены копий документов об образовании (аттестат/диплом) на подлинники в соответствии с Порядком зачисления правил приёма ПГУ}\n");
      report.write("\\par\\par\\par\\qc\\fs22{                          ________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab{(подпись поступающего)}\n");
      report.write("\\par\n");
      report.write("\\par\\ql\\fs24{Документы принял}\n");
      report.write("\\par\\ql\\i\\tab{сотрудник приемной комиссии: _______________________}\\i0\n");
      report.write("\\par\\ql\\i\\tab{"+StringUtil.CurrDate(".")+"г.}\\i0\n");
      report.write("\\pard\\plain\\ql\\intbl\\cell\\pard\\plain\\ql\\intbl\n");
      report.write("\\trowd\\trqc\n");
      report.write("\\clvertalt\\cltxlrtb \\cellx7011\n");
      report.write("\\clvertalt\\clbrdrr\\brdrdash\\brdrw20 \\cltxlrtb \\cellx7364\n");
      report.write("\\clvertalt\\clbrdrl\\brdrdash\\brdrw20\\cltxlrtb\\cellx7717\n");
      report.write("\\clvertalt\\cltxlrtb\\cellx14730\\row }\\pard\\ql\\itap0\n");
      report.write("}\n");
      report.close();

/************************************************/
/**** ПОДГОТОВКА ДАННЫХ ДЛЯ ВЫВОДА НА ЭКРАН *****/
/************************************************/

      stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        abit_A.setFamilija(rs.getString(1));
        abit_A.setImja(rs.getString(2));
        abit_A.setOtchestvo(rs.getString(3));
      }

      form.setAction(us.getClientIntName("pakdoc","reports"));
//      return mapping.findForward("rep_brw");
    }

 /************************************************/
 /************************************************/
 /******             ЧАСТЬ 3               *******/
 /************************************************/
 /************************************************/

       String file_con = new String("agreement"+abit_A.getKodAbiturienta()+".rtf");
  
       String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;

       abit_A.setFileName3(file_con);

       BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

 /************************************************/

       report.write("{\\rtf1\\ansi{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;\\ctextone\\ctint255\\cshade255\\red0\\green0\\blue0;}\n");
       report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");

    // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
    //   report.write("\\par\\par\n");
       report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab\\tab{Председателю приёмной комиссии ПГУ, ректору А.Д. Гулякову}\n");        

    // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
       String tip_Dok2 = new String();
       boolean oblastIsEmpty2 = false;
       stmt = conn.prepareStatement("SELECT kodOblastiP FROM Abiturient where kodAbiturienta = ?");
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()){
      	 if(rs.getString(1).equals("-")) oblastIsEmpty2 = true;
       }
       
    // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
       if (oblastIsEmpty2){
       stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,a.gorod_prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a WHERE KodAbiturienta LIKE ?");
 	      	 
       }
       else{
       stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
       }
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) {

       report.write("\\par\\qc\\fs22\\tab\\tab\\tab{  абитуриента }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
       }
    // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
       report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab{подавшего заявление на участие в конкурсе на обучение}\n");  
       
       report.write("\\par\\qc\\fs22\\tab{         по образовательной программе  }\n");
       report.write("\\par\\par\n");
       

       // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
       stmt = conn.prepareStatement("SELECT s.ShifrSpetsialnosti, s.NazvanieSpetsialnosti, e.name, ko.Forma_ob from konkurs ko, spetsialnosti s, edulevel e where ko.kodabiturienta like ? and ko.kodspetsialnosti=s.kodspetsialnosti and e.socr = s.edulevel");
       
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) {

         report.write("\\tab\\tab\\tab\\tab\\tab\\fs22\\ql{ }\\i{"+rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+"}\\i0\n");
         report.write("\\qc\\fs6\\par\\par\\fs22");
       }
    // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.
       stmt = conn.prepareStatement("select nomerlichnogodela from abiturient where kodabiturienta like ?");
       
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) {

         report.write("\\tab\\fs22\\ql{Личное дело № }\\i{"+rs.getString(1)+"}\\i0\n");
         report.write("\\qc\\fs6\\par\\par\\fs22");
         report.write("\\par\\par\n");
         report.write("\\par\\par\n");
         
       }
    // ДОБАВЛЕНО 29 ФЕВРАЛЯ - 2 МАРТА 2016 Г.           
       report.write("\\qc\\fs28\\b{ЗАЯВЛЕНИЕ}\\b0\n");
       report.write("\\par\\par\n");
       report.write("\\par\\par\\ql\\fs22\\tab{Прошу учесть моё согласие о зачислении на обучение по программе}\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(код, НП, форма обучения)}\\b0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       
       report.write("\\par\\par\\ql\\fs22\\tab{При приёме на обучение на места по договорам с оплатой стоимости обучения:}\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\tab{- по общему конкурсу.}\n");
      
       report.write("\\par\\par\\ql\\fs22\\tab{К данному заявлению прилагаю}\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(название документа об образовании, серия, рег. №)}\\b0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab{выдан______________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(название образовательной организации, выдавшей документ, дата выдачи:)}\\b0\n");	
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(оригинал, копия, заверенная нотариально / приёмной комиссией (ненужное вычеркнуть))}\\b0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
    
       report.write("\\par\\par\n");
       report.write("\\par\\par\n");
       report.write("\\par\\par\n");
       report.write("\\par\\par\\ql\\fs22\\tab{(Ф.И.О.)                                                                                                             }\\i{(подпись)}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab{''___''________________ 2016 г.}\n");
       
       report.write("}"); 
       

       report.close();

 //System.out.println("Doc-!->6");

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
     }
     request.setAttribute("mess", mess);
     request.setAttribute("abit_A", abit_A);

     if(error) return mapping.findForward("error");

     return mapping.findForward("success");
    }
} 