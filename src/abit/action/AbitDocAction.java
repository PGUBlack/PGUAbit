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
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "abiturientForm", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************************************************************************************************/
/************************ ������������ ������������ ������ ���������� ***************************/
/************************************************************************************************/

 if ( form.getAction().equals("reports") ) {

    abit_A.setFileName1("-");
    abit_A.setFileName2("-");
    abit_A.setFileName3("-");   

// ��������� �� �� �������������� �������� �� ���� �����������

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
    
//   if ( abit_A.getTip_Spec().equals("�") && !abit_A.getNeed_Spo().equals("�")) {
 
  
   // if ( abit_A.getTip_Spec().equals("�") && abit_A.getInternship().equals("�")) {//&& abit_A.getInternship().equals("�")
    	 if ( abit_A.getNomerPotoka() == 3) {//&& abit_A.getInternship().equals("�")

    	/************************************************/
    	/************************************************/
    	/******        ��������� � �����������(�) *******/
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
         if(rs.next()) report.write("\\fs24\\b\\qc{������� }{"+rs.getString(1)+"}\\b0\n");

         report.write("\\par\\par\n");

   /************************************************/

         String tip_Dok = new String();
         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           report.write("\\fs22\\ql{�� }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{���������(��): }\\i{"+rs.getString(4)+"}\\i0,  ���� ��������: \\i{"+rs.getString(5)+"�.}\\i0,\n");//\\i0{, ����� ��������: }\\i{"+rs.getString(16)+"}
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{�����������(���) �� ������: }\\i{"+rs.getString(6)+", ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           if(rs.getString(10) != null && rs.getString(10).equals("�"))
             tip_Dok = "�������(�����-�)";
           else
             tip_Dok = "�������";
           report.write("\\i{"+tip_Dok+" }{"+rs.getString(11)+" � "+rs.getString(12)+", �����: "+rs.getString(13)+", "+rs.getString(14)+"�.}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{���������� �������: }\\i{"+rs.getString(15)+"}\\i0\n");
           report.write("\\par\\par\n");
         }

   /************************************************/
         report.write("\\qc\\fs28\\b{���������}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("�")) forma_Ob = "�����";
           else forma_Ob = "�������";

           if(rs.getString(3) != null && rs.getString(3).equals("�")) {osnova_Ob1 = "�� �����, ������������� �� �������";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("�")) {osnova_Ob2 = "�� ������� ������";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " � " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ��������� �����������:}\n");
           report.write("\\par\n");
       //    report.write("\\par\\i\\tab{�� "+forma_Ob+" ����� ��������}\\i0\n");//"+osnova_Ob+"
       //    report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("�")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//             } else {
         	
             
             report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\qc\\b{�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
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
       /*  report.write("\\qc\\fs28\\b{���������}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("�")) forma_Ob = "�����";
           else forma_Ob = "�������";

           if(rs.getString(3) != null && rs.getString(3).equals("�")) {osnova_Ob1 = "�� �����, ������������� �� �������";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("�")) {osnova_Ob2 = "�� ������� ������";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " � " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ��������� �����������: }\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}\\'bb\\i0\n");
           //report.write("\\par\n");
           //report.write("\\par\\i\\tab{�� "+forma_Ob+" ����� ��������}\\i0\n");//"+osnova_Ob+"
           //report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("�")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//             } else {
         	
             
             //report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             //report.write("\\intbl\\qc\\b{�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
             //report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{������ ������������ ���������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
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
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
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
         report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

         String tsel = new String();
         String tip_Ok_Zav = new String();
         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,tp.TselevojPriem, a.TipDokSredObraz FROM TselevojPriem tp, Abiturient a, Zavedenija z WHERE a.kodtselevogopriema=tp.kodtselevogopriema AND a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

             tip_Ok_Zav = "������������������� ����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������ ������� ���������";
           }
           
           String DokSrObr = null;
           if(rs.getString(9) != null && rs.getString(9).equals("�"))
               DokSrObr = " (��������)";
             else
               DokSrObr = " (�����)";

           report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i   � "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
           tsel=rs.getString(8);
         }
         
         
         /************************************************/
         
         
         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         String predpr = new String();
         report.write("\\b0\\fs6\\par\n");
         report.write("\\par\\ql\\fs22{������� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
         while(rs.next()) {
           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
           //  report.write("\\b0\\fs6\\par\n");
          
             if(rs.getString(1).equals("2")) predpr = "�������";
             else if(rs.getString(1).equals("3")) predpr = "���������";
             else if(rs.getString(1).equals("4")) predpr = "�����������";
             else if(rs.getString(1).equals("1")) predpr = "";
             else predpr = "������";
             report.write("\\i{"+predpr+"   }");//\\i0{ ��� ������� � ������� ��������
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
           if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "����������";

           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "��������";

           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "�����������";
           }
          

           if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             need_Obshaga = "��������";

           } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

             need_Obshaga = "�� ��������";
           }
//           report.write("\\par\n");

          
           report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{������� �����������: }\\i{"+tsel+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{�������� � ������� �������������� �����, ����������� � ������� �� ���___________________________}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
           
           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
            // if(rs.getString(2) != null && rs.getString(2).length() > 3)
              //  report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
             if(rs.getString(1) != null && rs.getString(1).length() > 3){
               report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
           }else{
        	   report.write("\\par\\ql\\fs22{�������������� ����������: ���}\\fs6\\par\n");
           }
         }
         }
   /************************************************/
         
         stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
         	
         if(rs.getString(1)!=null){
            report.write("\\par\\ql\\fs22\\tab{� ������������ � �������� 8 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� �������������� �������� � ������������:}\n");
            report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
         else{
        	 report.write("\\par\\ql\\fs22\\tab{� ������������ � �������� 8 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� �������������� �������� � ������������:}\n");
             report.write("\\par\\ql\\fs22\\tab\\i{���}\\i0");}
         }
         
         
         
         /************************************************/

         //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
         //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\par\n");
            stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
            stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
          	  if(rs.getString(1) != null){
            report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
            report.write("\\par\\ql\\fs22{���������� ����������) }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
            //report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+"   �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");
            }
            /************************************************/

            //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
            //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\par\n");
               //stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
               //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
               //rs = stmt.executeQuery();
               //if(rs.next()) {
             	//  if(rs.getString(1) != null){
               //report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
               //report.write("\\par\\ql\\fs22{���������� ����������)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
               //report.write("\\par\\par\n");
               //report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+" �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
               //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
               //report.write("\\par\\par\n");
             //  }
            report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{� ������ �������� �� ������������� �������� ������������(� ������������) ����� 90��1 �0000704, ��}");
	         report.write("\\par\\ql\\fs22{21 ����� 2013 �.; � ������ ������������� � ��������������� ������������ (� ������������) �� 17.05.2013 �.}");  
	         report.write("\\par\\ql\\fs22{����� 90��1 �0000631 ���.�0627; c ����������� � ��������������� ����������� ������ ������ � }\n");
	         report.write("\\par\\ql\\fs22{������������� ��� ������ �� �������� �� ���������� ������������ � ���������� ������������ � ������}\n");
	         report.write("\\par\\ql\\fs22{���������� ������������� ������������ ��������� ��������� �������������� ������� �� ������ �����}\n");
	         report.write("\\par\\ql\\fs22{���������� �� ����� � ������ ����������� ����, � ������ ���������� ������������� ������������ �������� �}\n");
	         report.write("\\par\\ql\\fs22{�������� �� ���������� �� ����� �� ��������� �� �������� ������� ��������������� �����; � ��������� ������}\n");
	         report.write("\\par\\ql\\fs22{��������� �� ����������� ������������� ���������, ���������� ������������ �������������� ����������(�)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\n");
               
               //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
               //report.write("\\pard\\page\n");
               
               /************************************************/

            /*   report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
               report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");*/
               report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{�������� �� ��������� ����� ������������ ������                                            }\\i0{_________________}\\i0\n"); 
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

         /************************************************/
              // report.write("\\par\\par\\ql\\fs22\\tab{������ ���������������� ����������� ������� }\\i{�������.}\\i0\n");
              //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
              //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               
               report.write("\\par\\par\\par\n");
             
               report.write("\\par\\par\\ql\\fs22\\tab{����������(�) � ����������� �� ��������������� �� ������������� ��������, ����������� � ��������� � }\n");
               report.write("\\par\\par\\ql\\fs22\\tab{������, � �� ����������� ����������, ���������� ��� �����������                }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               report.write("\\par\\par\\par\n");

         /************************************************/
               report.write("\\par\\par\\ql\\fs22\\tab{����������� ��������� ������� ����������� �� ���������� ����������� �������}\n");
               report.write("\\par\\par\\ql\\fs22\\tab{(��� ����������� �� �������� �� ����� � ������ ����������� ����)              }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               report.write("\\par\\par\\par\n");
               
              
             

            
      /************************************************/

            //report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");

      /************************************************/

            //report.write("\\ql\\fs22\\tab{������� �������� ������� }\\i{�������.}\\i0\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\par\\qj\\fs22\\tab{� ��������� �� ����� ������������� ��������������� ������������ ����� 90��1 �0000704, �� 21 ����� 2013 �., �������������� � ��������������� ������������ �� 25.06.2012 ����� 90��1 � 000007 ���.�0007 � ���������� � ���, ����������� ������������� ���������, ��������� ����� ���, ��������� ���������� ��������� ����������(�).}\n");
            //report.write("\\par\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab\\b{����������(�) � ����� �������������� ��������� ��������� ���������������� ������� �� ����������� � ������������ � �������� ���������� (�.14) ������ ������.}\\b0\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22\\tab{����: "+StringUtil.CurrDate(".")+"�.}\n");

            report.write("}"); 
            report.close();
    }
    

    /************************************************/

    // if ( abit_A.getTip_Spec().equals("�") && abit_A.getPostgraduateStudies().equals("�") ) {//&& abit_A.getPostgraduateStudies().equals("�")
    	
    	 if ( abit_A.getNomerPotoka() == 5) {//&
    	 /************************************************/
    	/************************************************/
    	/******        ��������� � �����������  (�)   *******/
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
         if(rs.next()) report.write("\\fs24\\b\\qc{������� }{"+rs.getString(1)+"}\\b0\n");

         report.write("\\par\\par\n");

   /************************************************/

         String tip_Dok = new String();
         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE  KodAbiturienta LIKE ? and k.code = a.gorod_prop");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           report.write("\\fs22\\ql{�� }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{���������(��): }\\i{"+rs.getString(4)+"}\\i0,  ���� ��������: \\i{"+rs.getString(5)+"�.}\\i0,\n");//\\i0{, ����� ��������: }\\i{"+rs.getString(16)+"}
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{�����������(���) �� ������: }\\i{"+rs.getString(6)+", ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           if(rs.getString(10) != null && rs.getString(10).equals("�"))
             tip_Dok = "�������(�����-�)";
           else
             tip_Dok = "�������";
           report.write("\\i{"+tip_Dok+" }{"+rs.getString(11)+" � "+rs.getString(12)+", �����: "+rs.getString(13)+", "+rs.getString(14)+"�.}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{���������� �������: }\\i{"+rs.getString(15)+"}\\i0\n");
           report.write("\\par\\par\n");
         }

   /************************************************/
         report.write("\\qc\\fs28\\b{���������}\\b0\n");
         report.write("\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("�")) forma_Ob = "�����";
           else forma_Ob = "�������";

           if(rs.getString(3) != null && rs.getString(3).equals("�")) {osnova_Ob1 = "�� �����, ������������� �� �������";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("�")) {osnova_Ob2 = "�� ������� ������";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " � " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ��������� ���������� ������-�������������� ������ � �����������: }\n"); //\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}
           report.write("\\par\n");
         //  report.write("\\par\\i\\tab{�� "+forma_Ob+" ����� ��������}\\i0\n");//"+osnova_Ob+"
      //     report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�','�') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("�")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//             } else {
         	
             
             report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\qc\\b{�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�','�') ORDER BY kon.Prioritet ASC");
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

       /*  report.write("\\qc\\fs28\\b{���������}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("�")) forma_Ob = "�����";
           else forma_Ob = "�������";

           if(rs.getString(3) != null && rs.getString(3).equals("�")) {osnova_Ob1 = "�� �����, ������������� �� �������";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("�")) {osnova_Ob2 = "�� ������� ������";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " � " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ��������� ���������� ������-�������������� ������ � �����������: }\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}\\'bb\\i0\n");
           //report.write("\\par\n");
           //report.write("\\par\\i\\tab{�� "+forma_Ob+" ����� ��������}\\i0\n");//"+osnova_Ob+"
           //report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("�")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//             } else {
         	
             
             //report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             //report.write("\\intbl\\qc\\b{�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
             //report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
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
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
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
         report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

         
         String tip_Ok_Zav = new String();
         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata, a.TipDokSredObraz FROM Abiturient a, Zavedenija z WHERE a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

             tip_Ok_Zav = "������������������� ����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������ ������� ���������";
           }

           String DokSrObr = null;
           if(rs.getString(8) != null && rs.getString(8).equals("�"))
               DokSrObr = " (��������)";
             else
               DokSrObr = " (�����)";
           
           
           report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  � "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
         }
         
         
         /************************************************/
         
         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         String predpr = new String();
         report.write("\\b0\\fs6\\par\n");
         report.write("\\par\\ql\\fs22{������� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
         while(rs.next()) {
           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
           //  report.write("\\b0\\fs6\\par\n");
          
             if(rs.getString(1).equals("2")) predpr = "�������";
             else if(rs.getString(1).equals("3")) predpr = "���������";
             else if(rs.getString(1).equals("4")) predpr = "�����������";
             else if(rs.getString(1).equals("1")) predpr = "";
             else predpr = "������";
             report.write("\\i{"+predpr+"   }");//\\i0{ ��� ������� � ������� ��������
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
           if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "����������";

           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "��������";

           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "�����������";
           }

           if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             need_Obshaga = "��������";

           } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

             need_Obshaga = "�� ��������";
           }
//           report.write("\\par\n");

          
           report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{�������� � ������� �������������� �����, ����������� � ������� �� ���___________________________}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
           
           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
            // if(rs.getString(2) != null && rs.getString(2).length() > 3)
              //  report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
             if(rs.getString(1) != null && rs.getString(1).length() > 3)
               report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
           }
         }

   /************************************************/
         
         stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
         	
         if(rs.getString(1)!=null){
            report.write("\\par\\ql\\fs22\\tab{� ������������ � �������� 7 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� �������������� �������� � ������������:}\n");
            report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
         }
         
         
         /************************************************/

         //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
         //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\par\n");
            stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
            stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
          	  if(rs.getString(1) != null){
            report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
            report.write("\\par\\ql\\fs22{���������� ����������) }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
            //report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+"   �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");
            }
            /************************************************/

            //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
            //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\par\n");
             //  stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
              // stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
              // rs = stmt.executeQuery();
              // if(rs.next()) {
             	 // if(rs.getString(1) != null){
              // report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
              // report.write("\\par\\ql\\fs22{���������� ����������)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
               //report.write("\\par\\par\n");
              // report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+" �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
               //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
               //report.write("\\par\\par\n");
              // }
            report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{� ������ �������� �� ������������� �������� ������������(� ������������) ����� 90��1 �0000704, ��}");
	         report.write("\\par\\ql\\fs22{21 ����� 2013 �.; � ������ ������������� � ��������������� ������������ (� ������������) �� 17.05.2013 �.}");  
	         report.write("\\par\\ql\\fs22{����� 90��1 �0000631 ���.�0627; c ����������� � ��������������� ����������� ������ ������ � }\n");
	         report.write("\\par\\ql\\fs22{������������� ��� ������ �� �������� �� ���������� ������������ � ���������� ������������ � ������}\n");
	         report.write("\\par\\ql\\fs22{���������� ������������� ������������ ��������� ��������� �������������� ������� �� ������ �����}\n");
	         report.write("\\par\\ql\\fs22{���������� �� ����� � ������ ����������� ����, � ������ ���������� ������������� ������������ �������� �}\n");
	         report.write("\\par\\ql\\fs22{�������� �� ���������� �� ����� �� ��������� �� �������� ������� ��������������� �����; � ��������� ������}\n");
	         report.write("\\par\\ql\\fs22{��������� �� ����������� ������������� ���������, ���������� ������������ �������������� ����������(�)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\n");
               
               //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
               //report.write("\\pard\\page\n");
               
               /************************************************/

            /*   report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
               report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");*/
               report.write("\\par\n");
               report.write("\\qj\\fs22\\tab{�������� �� ��������� ����� ������������ ������                                          }\\'ab{_________________}\\'bb.\n"); 
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

         /************************************************/
              // report.write("\\par\\par\\ql\\fs22\\tab{������ ���������������� ����������� ������� }\\i{�������.}\\i0\n");
              //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
              //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               
               report.write("\\par\\par\\par\n");
             
               report.write("\\par\\par\\ql\\fs22\\tab{����������(�) � ����������� �� ��������������� �� ������������� ��������, ����������� � ��������� � }\n");
               report.write("\\par\\par\\ql\\fs22\\tab{������, � �� ����������� ����������, ���������� ��� �����������                }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               report.write("\\par\\par\\par\n");

         /************************************************/
               report.write("\\par\\par\\ql\\fs22\\tab{����������� ��������� ������� ����������� ������� ������ �������}\n");
               report.write("\\par\\par\\ql\\fs22\\tab{(��� ����������� �� �������� �� ����� � ������ ����������� ����)               }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               report.write("\\par\\par\\par\n");
               
              
             

            
      /************************************************/

            //report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");

      /************************************************/

            //report.write("\\ql\\fs22\\tab{������� �������� ������� }\\i{�������.}\\i0\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\par\\qj\\fs22\\tab{� ��������� �� ����� ������������� ��������������� ������������ ����� 90��1 �0000704, �� 21 ����� 2013 �., �������������� � ��������������� ������������ �� 25.06.2012 ����� 90��1 � 000007 ���.�0007 � ���������� � ���, ����������� ������������� ���������, ��������� ����� ���, ��������� ���������� ��������� ����������(�).}\n");
            //report.write("\\par\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab\\b{����������(�) � ����� �������������� ��������� ��������� ���������������� ������� �� ����������� � ������������ � �������� ���������� (�.14) ������ ������.}\\b0\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22\\tab{����: "+StringUtil.CurrDate(".")+"�.}\n");

            report.write("}"); 
            report.close();
    }
    

    /************************************************/
  
 //    if ( abit_A.getTip_Spec().equals("�") && abit_A.getTraineeship().equals("�")) {//&& abit_A.getTraineeship().equals("�")
    	 if ( abit_A.getNomerPotoka() == 4) {//&
    	/************************************************/
    	/************************************************/
    	/******        ��������� � ����������   (�)   *******/
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
         if(rs.next()) report.write("\\fs24\\b\\qc{������� }{"+rs.getString(1)+"}\\b0\n");

         report.write("\\par\\par\n");

   /************************************************/

         String tip_Dok = new String();
         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           report.write("\\fs22\\ql{�� }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{���������(��): }\\i{"+rs.getString(4)+"}\\i0,  ���� ��������: \\i{"+rs.getString(5)+"�.}\\i0,\n");//\\i0{, ����� ��������: }\\i{"+rs.getString(16)+"}
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{�����������(���) �� ������: }\\i{"+rs.getString(6)+", ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           if(rs.getString(10) != null && rs.getString(10).equals("�"))
             tip_Dok = "�������(�����-�)";
           else
             tip_Dok = "�������";
           report.write("\\i{"+tip_Dok+" }{"+rs.getString(11)+" � "+rs.getString(12)+", �����: "+rs.getString(13)+", "+rs.getString(14)+"�.}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{���������� �������: }\\i{"+rs.getString(15)+"}\\i0\n");
           report.write("\\par\\par\n");
         }

   /************************************************/
         report.write("\\qc\\fs28\\b{���������}\\b0\n");
         report.write("\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("�")) forma_Ob = "�����";
           else forma_Ob = "�������";

           if(rs.getString(3) != null && rs.getString(3).equals("�")) {osnova_Ob1 = "�� �����, ������������� �� �������";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("�")) {osnova_Ob2 = "�� ������� ������";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " � " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ��������� ����������: }\n");//\\i{"+rs.getString(1)+"} \\'ab{"+rs.getString(2)+"}\\'bb\\i0
           report.write("\\par\n");
         // report.write("\\par\\i\\tab{�� "+forma_Ob+" ����� ��������}\\i0\n");//"+osnova_Ob+"
       //   report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("�")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//             } else {
         	
             
             report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\qc\\b{�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
             report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
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

       /*  report.write("\\qc\\fs28\\b{���������}\\b0\n");
         report.write("\\par\\par\n");

         String forma_Ob = new String();
         String osnova_Ob = new String();
         String osnova_Ob1 = new String();
         String osnova_Ob2 = new String();
         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Six,Three,kon.Forma_Ob FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? ORDER BY kon.Prioritet ASC");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(8) != null && rs.getString(8).equals("�")) forma_Ob = "�����";
           else forma_Ob = "�������";

           if(rs.getString(3) != null && rs.getString(3).equals("�")) {osnova_Ob1 = "�� �����, ������������� �� �������";}
           else {osnova_Ob1 = "";}
           if(rs.getString(4) != null && rs.getString(4).equals("�")) {osnova_Ob2 = "�� ������� ������";}
           else {osnova_Ob2 = "";}

           if(osnova_Ob1 != "" && osnova_Ob2 != "") {osnova_Ob = ", "+osnova_Ob1 + " � " + osnova_Ob2+".";}
           else if(osnova_Ob1 != "" && osnova_Ob2 == "") {osnova_Ob =  ", "+osnova_Ob1+".";}
           else if(osnova_Ob1 == "" && osnova_Ob2 != "") {osnova_Ob =  ", "+osnova_Ob2+".";}

           report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ��������� ����������: }\\i{"+rs.getString(1)+"} \\i0{"+rs.getString(2)+"}\\i0\n");
           //report.write("\\par\n");
           //report.write("\\par\\i\\tab{�� "+forma_Ob+" ����� ��������}\\i0\n");//"+osnova_Ob+"
           //report.write("\\par\n");
         }
   //////////////////////////////////////////////////////////////////////////
         
       
           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
//             if(abit_A.getTip_Spec().equals("�")) {
//               report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//             } else {
         	
             
             //report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             //report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             //report.write("\\intbl\\qc\\b{�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
             //report.write("\\intbl\\row\n");

             report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
             report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
             report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
             //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

             report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
             report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             //report.write("\\intbl\\qc\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
             report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
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
             stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
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
         report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

         String tsel1=new String();
         String tip_Ok_Zav = new String();
         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,tp.TselevojPriem, a.tipDokSredObraz FROM Tselevojpriem tp, Abiturient a, Zavedenija z WHERE tp.kodtselevogopriema=a.kodtselevogopriema AND a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

             tip_Ok_Zav = "������������������� ����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";

           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             tip_Ok_Zav = "������ ������� ���������";
           }
           tsel1=rs.getString(8);
           
           String DokSrObr = null;
           if(rs.getString(9) != null && rs.getString(9).equals("�"))
               DokSrObr = " (��������)";
             else
               DokSrObr = " (�����)";
           report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i   � "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
         }
         
         
         /************************************************/
         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         String predpr = new String();
         report.write("\\b0\\fs6\\par\n");
         report.write("\\par\\ql\\fs22{������� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
         while(rs.next()) {
           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
           //  report.write("\\b0\\fs6\\par\n");
          
             if(rs.getString(1).equals("2")) predpr = "�������";
             else if(rs.getString(1).equals("3")) predpr = "���������";
             else if(rs.getString(1).equals("4")) predpr = "�����������";
             else if(rs.getString(1).equals("1")) predpr = "";
             else predpr = "������";
             report.write("\\i{"+predpr+"   }");//\\i0{ ��� ������� � ������� ��������
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
           if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "����������";

           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "��������";

           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

             in_Jaz = "�����������";
           }

           if(rs.getString(2) != null && rs.getString(2).equals("�")) {

             need_Obshaga = "��������";

           } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

             need_Obshaga = "�� ��������";
           }
//           report.write("\\par\n");

          
           report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
           stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	      //   String predpr = new String();
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{������� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
	         while(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
	           //  report.write("\\b0\\fs6\\par\n");
	          
	             if(rs.getString(1).equals("2")) predpr = "�������";
	             else if(rs.getString(1).equals("3")) predpr = "���������";
	             else if(rs.getString(1).equals("4")) predpr = "�����������";
	             else if(rs.getString(1).equals("1")) predpr = "";
	             else predpr = "������";
	             report.write("\\i{"+predpr+"   }");//\\i0{ ��� ������� � ������� ��������
	           }
	         }
	         report.write("\\i0\n");
           
          // report.write("\\par\\ql\\fs22{������� ����������� }\\i{"+tsel1+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{�������� � ������� �������������� �����, ����������� � ������� �� ���___________________________}\\i0\\fs6\\par\n");
           report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
           
           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) {
            // if(rs.getString(2) != null && rs.getString(2).length() > 3)
              //  report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
             if(rs.getString(1) != null && rs.getString(1).length() > 3)
               report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
           }
         }

   /************************************************/
         
         stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
         	
         if(rs.getString(1)!=null){
            report.write("\\par\\ql\\fs22\\tab{� ������������ � �������� 8 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� �������������� �������� � ������������:}\n");
            report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
         }
         
         
         /************************************************/

         //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
         //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //   report.write("\\par\\par\n");
            stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
            stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
          	  if(rs.getString(1) != null){
            report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
            report.write("\\par\\ql\\fs22{���������� ����������) }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
            //report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+"    �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");
            }
            /************************************************/

            //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
            //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //   report.write("\\par\\par\n");
              // stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
               //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
              // rs = stmt.executeQuery();
               //if(rs.next()) {
             	//  if(rs.getString(1) != null){
              // report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
               //report.write("\\par\\ql\\fs22{���������� ����������)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
               //report.write("\\par\\par\n");
              // report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+" �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
               //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
               //report.write("\\par\\par\n");
               //}
            report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{� ������ �������� �� ������������� �������� ������������(� ������������) ����� 90��1 �0000704, ��}");
	         report.write("\\par\\ql\\fs22{21 ����� 2013 �.; � ������ ������������� � ��������������� ������������ (� ������������) �� 17.05.2013 �.}");  
	         report.write("\\par\\ql\\fs22{����� 90��1 �0000631 ���.�0627; c ����������� � ��������������� ����������� ������ ������ � }\n");
	         report.write("\\par\\ql\\fs22{������������� ��� ������ �� �������� �� ���������� ������������ � ���������� ������������ � ������}\n");
	         report.write("\\par\\ql\\fs22{���������� ������������� ������������ ��������� ��������� �������������� ������� �� ������ �����}\n");
	         report.write("\\par\\ql\\fs22{���������� �� ����� � ������ ����������� ����, � ������ ���������� ������������� ������������ �������� �}\n");
	         report.write("\\par\\ql\\fs22{�������� �� ���������� �� ����� �� ��������� �� �������� ������� ��������������� �����; � ��������� ������}\n");
	         report.write("\\par\\ql\\fs22{��������� �� ����������� ������������� ���������, ���������� ������������ �������������� ����������(�)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\n");
               
               //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
               //report.write("\\pard\\page\n");
               
               /************************************************/

            /*   report.write("\\par\\par\\par\n");
               report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
               report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");*/
               report.write("\\qj\\fs22\\tab{�������� �� ��������� ����� ������������ ������                                           }\\'ab{_________________}\\'bb.\n"); 
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

         /************************************************/
              // report.write("\\par\\par\\ql\\fs22\\tab{������ ���������������� ����������� ������� }\\i{�������.}\\i0\n");
              //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
              //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               
               report.write("\\par\n");
             
               report.write("\\par\\par\\ql\\fs22\\tab{����������(�) � ����������� �� ��������������� �� ������������� ��������, ����������� � ��������� � }\n");
               report.write("\\par\\par\\ql\\fs22\\tab{������, � �� ����������� ����������, ���������� ��� �����������               }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               report.write("\\par\\par\\par\n");

         /************************************************/
               report.write("\\par\\par\\ql\\fs22\\tab{����������� ��������� ������� ����������� �� ���������� ���������� �������}\n");
               report.write("\\par\\par\\ql\\fs22\\tab{(��� ����������� �� �������� �� ����� � ������ ����������� ����)      }\\i{_________________}\\i0\n");
               report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
               report.write("\\par\\par\\par\n");
               
              
             

            
      /************************************************/

            //report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
            //report.write("\\par\\par\n");

      /************************************************/

            //report.write("\\ql\\fs22\\tab{������� �������� ������� }\\i{�������.}\\i0\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\par\\qj\\fs22\\tab{� ��������� �� ����� ������������� ��������������� ������������ ����� 90��1 �0000704, �� 21 ����� 2013 �., �������������� � ��������������� ������������ �� 25.06.2012 ����� 90��1 � 000007 ���.�0007 � ���������� � ���, ����������� ������������� ���������, ��������� ����� ���, ��������� ���������� ��������� ����������(�).}\n");
            //report.write("\\par\n");
            //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab\\b{����������(�) � ����� �������������� ��������� ��������� ���������������� ������� �� ����������� � ������������ � �������� ���������� (�.14) ������ ������.}\\b0\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            //report.write("\\par\\par\\par\n");
            //report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
            //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
            //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

      /************************************************/

            report.write("\\par\\par\n");
            report.write("\\par\\ql\\fs22\\tab{����: "+StringUtil.CurrDate(".")+"�.}\n");

            report.write("}"); 
            report.close();
    }

  // if ( abit_A.getTip_Spec().equals("�") ) {
    	 if ( abit_A.getNomerPotoka() == 2) {//&

/************************************************/
/************************************************/
/******        ��������� ��������   (�)   *******/
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
      if(rs.next()) report.write("\\fs24\\b\\qc{������� }{"+rs.getString(1)+"}\\b0\n");

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

        report.write("\\fs22\\ql{�, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{���������(��): }\\i{"+rs.getString(4)+"}\\i0,  ���� ��������: \\i{"+rs.getString(5)+"�.}\\i0,\n");//\\i0{, ����� ��������: }\\i{"+rs.getString(16)+"}
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{�����������(���) �� ������: }\\i{"+rs.getString(6)+", ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        if(rs.getString(10) != null && rs.getString(10).equals("�"))
          tip_Dok = "�������(�����-�)";
        else
          tip_Dok = "�������";
        report.write("{��������, �������������� ��������: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" � "+rs.getString(12)+", �����: "+rs.getString(13)+", "+rs.getString(14)+"�.}\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{���������� �������: }\\i{"+rs.getString(15)+"}\\i0\n");
        report.write("\\par\\par\n");
      }

/************************************************/

      report.write("\\qc\\fs28\\b{���������}\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ������������� ��� ����������� ���������� ��������:}\n");
      report.write("\\par\\par\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Bud LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      boolean OchFormOutput = false;
      if(rs.next()) {
//        if(abit_A.getTip_Spec().equals("�")) {
//          report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//        } else {
    
        report.write("\\ql\\fs24\\b\\i\\tab{�� ����� ����� ��������}\\i0\\b0\n");
        OchFormOutput = true;
//        }
        
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� � ������ ����������� ���� ������ ������� �� �������� �� ���� ��������� ������������ ������������ �������}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
          // if(rs.getString(4).equals("�")){
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
    	
        stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Dog LIKE '�'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {  
	        if (!OchFormOutput)	 report.write("\\ql\\fs24\\b\\i\\tab{�� ����� ����� ��������}\\i0\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
      report.write("\\par\\par\n");
      
      report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
      report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
      stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
         //if(rs.getString(5).equals("�"))
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
      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

      
        report.write("\\ql\\fs24\\b\\i\\tab{�� ����-������� ����� ��������}\\i0\\b0\n");

        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND (kon.Bud LIKE '�' OR  kon.Dog LIKE '�') AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
      
      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE '�'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        //////////////////////////////////////////////////////////
        
        report.write("\\ql\\fs24\\b\\i\\tab{�� ������� ����� ��������}\\i0\\b0\n");
        ////////////////
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� � ������ ����������� ���� ������ ������� �� �������� �� ���� ��������� ������������}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");
        report.write("\\intbl\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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

stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) {
report.write("\\ql\\fs24\\b\\i\\tab{�� ������� ����� ��������}\\i0\\b0\n");
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
      report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

      
      String tip_Ok_Zav = new String();
      stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,z.PolnoeNaimenovanieZavedenija,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata, a.tipDokSredObraz FROM Abiturient a, Zavedenija z WHERE a.KodZavedenija=z.KodZavedenija AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

          tip_Ok_Zav = "������������������� ����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������ ������� ���������";
        }

        String DokSrObr = null;
        if(rs.getString(8) != null && rs.getString(8).equals("�"))
            DokSrObr = " (��������)";
          else
            DokSrObr = " (�����)";
        
        report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"} \\i0{"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  � "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
      }
      
      
      /************************************************/
      
      stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      String predpr = new String();
      report.write("\\b0\\fs6\\par\n");
      report.write("\\par\\ql\\fs22{������� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
      while(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
        //  report.write("\\b0\\fs6\\par\n");
       
          if(rs.getString(1).equals("2")) predpr = "�������";
          else if(rs.getString(1).equals("3")) predpr = "���������";
          else if(rs.getString(1).equals("4")) predpr = "�����������";
          else if(rs.getString(1).equals("1")) predpr = "";
          else predpr = "������";
          report.write("\\i{"+predpr+"   }");//\\i0{ ��� ������� � ������� ��������
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
        if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "����������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "��������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "�����������";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "��������";

        } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "�� ��������";
        }
//        report.write("\\par\n");

       
        report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
        
        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
         // if(rs.getString(2) != null && rs.getString(2).length() > 3)
           //  report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
            report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
        }
      }

/************************************************/
      
      stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
      	
      if(rs.getString(1)!=null){
         report.write("\\par\\ql\\fs22\\tab{� ������������ � �������� 9 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� �������������� �������� � ������������:}\n");
         report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
      }
      
      
      /************************************************/

      //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
      //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //   report.write("\\par\\par\n");
         stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {
       	  if(rs.getString(1) != null){
         report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
         report.write("\\par\\ql\\fs22{���������� ����������)}\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
         //report.write("\\par\\par\n");
         report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+" �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
         //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
         //report.write("\\par\\par\n");
         }
         report.write("\\par\\par\n");
         report.write("\\par\\ql\\fs22{� ������ �������� �� ������������� �������� ������������(� ������������) ����� 90��1 �0000704, ��}");
         report.write("\\par\\ql\\fs22{21 ����� 2013 �.; � ������ ������������� � ��������������� ������������ (� ������������) �� 17.05.2013 �.}");  
         report.write("\\par\\ql\\fs22{����� 90��1 �0000631 ���.�0627; c ����������� � ��������������� ����������� ������ ������ � }\n");
         report.write("\\par\\ql\\fs22{������������� ��� ������ �� �������� �� ���������� ������������ � ���������� ������������ � ������}\n");
         report.write("\\par\\ql\\fs22{���������� ������������� ������������ ��������� ��������� �������������� ������� �� ������ �����}\n");
         report.write("\\par\\ql\\fs22{���������� �� ����� � ������ ����������� ����, � ������ ���������� ������������� ������������ �������� �}\n");
         report.write("\\par\\ql\\fs22{�������� �� ���������� �� ����� �� ��������� �� �������� ������� ��������������� �����; � ��������� ������}\n");
         report.write("\\par\\ql\\fs22{��������� �� ����������� ������������� ���������, ���������� ������������ �������������� ����������(�)}\n");
         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
         report.write("\\par\\par\n");
         //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
         //report.write("\\pard\\page\n");
         
         /************************************************/

      /*   report.write("\\par\\par\\par\n");
         report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
         report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");*/
         report.write("\\par\\par\\par\n");
         report.write("\\qj\\fs22\\tab{�������� �� ��������� ����� ������������ ������                                           }\\i{_________________}\\i0\n"); 
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

   /************************************************/
        // report.write("\\par\\par\\ql\\fs22\\tab{������ ���������������� ����������� ������� }\\i{�������.}\\i0\n");
        //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
        //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
         
         report.write("\\par\\par\\par\n");
       
         report.write("\\par\\par\\ql\\fs22\\tab{����������(�) � ����������� �� �������������� �� ������������� ��������, ����������� � ��������� � }\n");
         report.write("\\par\\par\\ql\\fs22\\tab{������, � �� ����������� ����������, ���������� ��� �����������                }\\i{_________________}\\i0\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
         report.write("\\par\\par\\par\n");

   /************************************************/
         report.write("\\par\\par\\ql\\fs22\\tab{����������� ���������� ������� ���������, ������� �����������, ������� ��������(��� ����������� }\n");
         report.write("\\par\\par\\ql\\fs22\\tab{�� �������� �� ����� � ������ ����������� ����)                                               }\\i{_________________}\\i0\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
         report.write("\\par\\par\\par\n");
         
         report.write("\\ql\\fs22\\tab{����������� ���� ������ ��������� �� ����� ��� � ���� ����� ��.                  }\\b0{_________________}\\bb\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
        
         report.write("\\par\\par\\ql\\fs22\\tab{����������� ������ ��������� � ������ �� ��������� ���������������� ������� ����� ������ � ��� �}\n");
         report.write("\\par\\par\\ql\\fs22\\tab{������ �� ���� ��������������� ���������                                                          }\\i{_________________}\\i0\n");
         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
         report.write("\\par\\par\\par\n");

   /************************************************/
      
/************************************************/

      //report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\par\n");

/************************************************/

      //report.write("\\ql\\fs22\\tab{������� �������� ������� }\\i{�������.}\\i0\n");
      //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\par\\qj\\fs22\\tab{� ��������� �� ����� ������������� ��������������� ������������ ����� 90��1 �0000704, �� 21 ����� 2013 �., �������������� � ��������������� ������������ �� 25.06.2012 ����� 90��1 � 000007 ���.�0007 � ���������� � ���, ����������� ������������� ���������, ��������� ����� ���, ��������� ���������� ��������� ����������(�).}\n");
      //report.write("\\par\n");
      //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\qj\\fs22\\tab\\b{����������(�) � ����� �������������� ��������� ��������� ���������������� ������� �� ����������� � ������������ � �������� ���������� (�.14) ������ ������.}\\b0\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22\\tab{����: "+StringUtil.CurrDate(".")+"�.}\n");

      report.write("}"); 
      report.close();

   } //else if ( abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") && abit_A.getInternship().equals("�")  && abit_A.getTraineeship().equals("�")  &&  abit_A.getPostgraduateStudies().equals("�")) {
  // else if (abit_A.getNeed_Spo().equals("�") && abit_A.getTip_Spec().equals("�") || abit_A.getNeed_Spo().equals("�") && ((abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�") || abit_A.getTip_Spec().equals("�")) && ((abit_A.getInternship().equals("�") || abit_A.getInternship().equals("null")) && (abit_A.getTraineeship().equals("�") || abit_A.getTraineeship().equals("null")) && (abit_A.getPostgraduateStudies().equals("�") || abit_A.getPostgraduateStudies().equals("null"))))) {
    	 else if ( abit_A.getNomerPotoka() == 1) {//&
	   /***********************************************************/
	   /***********************************************************/
	   /*****  ��������� ���������/����������� (�,�,�,�,�,�) ******/
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
	         if(rs.next()) report.write("\\fs24\\b\\qc{������� }{"+rs.getString(1)+"}\\b0\n");

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

	           report.write("\\fs22\\ql{�, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
	           report.write("\\fs6\\par\\par\\fs22");
	           report.write("{���������(��): }\\i{"+rs.getString(4)+"}\\i0,  ���� ��������: \\i{"+rs.getString(5)+"�.},\n");//\\i0{, ����� ��������: }\\i{"+rs.getString(16)+"}\\i0
	           report.write("\\fs6\\par\\par\\fs22");
	           if(oblastIsEmpty){
	        	   report.write("{�����������(���) �� ������: }\\i{ ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
		             
	           }
	           else
	           report.write("{�����������(���) �� ������: }\\i{"+rs.getString(6)+", ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
	           report.write("\\fs6\\par\\par\\fs22");
	           if(rs.getString(10) != null && rs.getString(10).equals("�"))
	             tip_Dok = "������� (�����-�)";
	           else
	             tip_Dok = "�������";
	           report.write("{��������, �������������� ��������: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" � "+rs.getString(12)+", �����: "+rs.getString(13)+", "+rs.getString(14)+"�.}\\i0\n");
	           report.write("\\fs6\\par\\par\\fs22");
	           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{���������� �������: }\\i{"+rs.getString(15)+"}\\i0\n");
	           report.write("\\par\\par\n");
	         }

	   /************************************************/

	         report.write("\\qc\\fs28\\b{���������}\\b0\n");
	         report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ������������� ��� ����������� ���������� ������������ �(���) ��������� ������������:}\n");
	         report.write("\\par\\par\n");

	   /************************************************/

	         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Bud LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         boolean OchFormOutput = false;
	         if(rs.next()) {
//	           if(abit_A.getTip_Spec().equals("�")) {
//	             report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//	           } else {
	       
	           report.write("\\ql\\fs24\\b\\i\\tab{�� ����� ����� ��������}\\i0\\b0\n");
	           OchFormOutput = true;
//	           }
	           
	           report.write("\\par\\par\\qj\\fs22\\tab{�� ����� � ������ ����������� ���� ������ ������� �� �������� �� ���� ��������� ������������ ������������ �������}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	           report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	           report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
	             // if(rs.getString(4).equals("�")){
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
	       	
	           stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti  AND kon.Dog LIKE '�'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�') ORDER BY kon.Prioritet ASC");
		         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
		         rs = stmt.executeQuery();
		         if(rs.next()) {  
		        if (!OchFormOutput)	 report.write("\\ql\\fs24\\b\\i\\tab{�� ����� ����� ��������}\\i0\\b0\n");
	         report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
	         report.write("\\par\\par\n");
	         
	         report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	         //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	         report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	         //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
	         report.write("\\intbl\\row\n");

	         report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	         report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	         report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	         //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

	         report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
	         report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
	         stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
	            //if(rs.getString(5).equals("�"))
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
	         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	         
	           report.write("\\ql\\fs24\\b\\i\\tab{�� ����-������� ����� ��������}\\i0\\b0\n");

	           report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND (kon.Bud LIKE '�' OR  kon.Dog LIKE '�') AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
	         
	         stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE '�'  AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           //////////////////////////////////////////////////////////
	           
	           report.write("\\ql\\fs24\\b\\i\\tab{�� ������� ����� ��������}\\i0\\b0\n");
	           ////////////////
	           report.write("\\par\\par\\qj\\fs22\\tab{�� ����� � ������ ����������� ���� ������ ������� �� �������� �� ���� ��������� ������������}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");
	           report.write("\\intbl\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three, AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
  
   stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
   stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
   rs = stmt.executeQuery();
   if(rs.next()) {
	   report.write("\\ql\\fs24\\b\\i\\tab{�� ������� ����� ��������}\\i0\\b0\n");
	           report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
	           report.write("\\par\\par\n");
	           
	           report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
	           //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
	           report.write("\\intbl\\row\n");

	           report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	           report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
	           report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
	           //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

	           report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
	           report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

	           stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three,  AbbreviaturaFakulteta FROM Konkurs kon,Spetsialnosti sp, Fakultety f WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') and sp.kodfakulteta = f.kodfakulteta ORDER BY kon.Prioritet ASC");
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
	         report.write("\\par\\ql\\fs24\\tab\\b{�����:}\\b0\\fs6\\par\n");
	   //System.out.println("Doc-!->4");
	         int counter = 1;
	   ////////////////////////////////////////////////���� �� ������ !!!!
	         stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\par\\ql\\fs22\\tab{�	��������� � �������� ����������� ������������� ��������� ������, ���������� ��� ����� }\\fs24{���:}\n");

	           stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". ��������� � �������� ����������� ������������� ��������� ������, ���������� ��� �����    }\\fs24{���:}\n");
	           while(rs.next()) {
	              report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+" - "+rs.getString(2)+" ������;}\\i0");
	           }
	         }

	   /************************************************/
	         //report.write("\\par\\par\\ql\\fs24\\tab\\b{�����:}\\b0\\fs6\\par\n");
	         //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".� ������������ � �������� 8 ������ ������ ��� ��������� � ����� ���������, ���������� ������������� �������������� �� ���������:}\n");      

	         stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('�') or zso.Examen IN('�')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\par\\ql\\fs22\\tab{�	� ������������ � �������� 8 ������ ������ ��� ��������� � ����� ���������, ���������� ������������� �������������� �� ���������:}\n");      

	           stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('�') or zso.Examen IN('�')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           while(rs.next()) {
	           	//report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".� ������������ � �������� 8 ������ ������ ��� ��������� � ����� ���������, ���������� ������������� �������������� �� ���������:}\n");
	              report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
	           }
	         }

	   /************************************************/
	         //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". � ������������ � �������� 9 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� ������������� ���������, ���������� ������������� ��������������:}\n");
	         
	         //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	         //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         //rs = stmt.executeQuery();
	         //if(rs.next()) {
	         
	           //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". � ������������ � �������� 9 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� ������������� ���������, ���������� ������������� ��������������:}\n");

	           //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
	           //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           //rs = stmt.executeQuery();
	           //while(rs.next()) {
	         
	         report.write("\\par\\ql\\fs22\\tab{�	� ������������ � �������� 2 ������ ������ � ��� ��������� � ����� ���������, ���������� ������������� �������������� �� ����������� ����� �� ��������� ���������:}\n");      
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         
	           stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           if(rs.next()) {
	           	
	           if(rs.getString(1)!=null){
	              report.write("\\par\\ql\\fs22\\tab{�	� ������������ � �������� 9 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� ������������� ���������, ���������� ������������� ��������������:}\n");
	              report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+"}\\i0");}
	           }
	          // }
	        // }

	   /************************************************/
	   //////////////////////////////////////////////���� �� ������ !!! �����
	      /*   report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

	         String tip_Ok_Zav = new String();
	         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

	             tip_Ok_Zav = "������������������� ����������";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

	             tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

	             tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";
	           }

	           report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"}, ����������� � {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  "+rs.getString(5)+" � "+rs.getString(6)+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{����� ����������� ��� / ����� ����� ���: }\\i{"+rs.getString(7)+"}\\i0\n");
	         }
	   /////
	         report.write("\\par\\ql\\fs22{����� ����� ��� � �������������� �����___________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{����� ����� ������������� ��������� � �������������� ������������� ���������� ___________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{����� ��������� ����������� �����������__________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	    //////


	         stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
	             report.write("\\b0\\fs6\\par\n");
	             String predpr = new String();
	             if(rs.getString(1).equals("�")) predpr = "(���������)";
	             else if(rs.getString(1).equals("�")) predpr = "(�������)";
	             else if(rs.getString(1).equals("�")) predpr = "(�����������)";
	             else predpr = "�����������";
	             report.write("\\par\\ql\\fs22{������� ����������� }\\i{"+predpr+"}\n");//\\i0{ ��� ������� � ������� ��������
	           }
	         }



	         stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
	             report.write("\\b0\\fs6\\par\n");
	             report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1).toUpperCase()+"  (\"�\", \"�\" - ������� � ���������� ������; \"�\" - ������ � �������� ����)}\\i0\\fs6\\par\n");
	           }
	         }

	   //System.out.println("Doc-!->5");


	         String in_Jaz = new String();
	         String need_Obshaga = new String();
	         stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && rs.getString(1).equals("�")) {

	             in_Jaz = "����������";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

	             in_Jaz = "��������";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

	             in_Jaz = "�����������";
	           }

	           if(rs.getString(2) != null && rs.getString(2).equals("�")) {

	             need_Obshaga = "��������";

	           } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

	             need_Obshaga = "�� ��������";
	           }
//	           report.write("\\par\n");

	           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           if(rs.next()) {
	             if(rs.getString(1) != null && rs.getString(1).length() > 3)
	                report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
	             if(rs.getString(2) != null && rs.getString(2).length() > 3)
	               report.write("\\par\\ql\\fs22{���������������� ����� ��� �����������: }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
	           }
	           report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
	         }



	      //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
	      //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\par\n");
	         
	         report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
	         report.write("\\par\\ql\\fs22{���������� ����������)      ____________________________________________________________________}\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): _____________________________}\n");
	         report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	         report.write("\\par\\par\n");
	         
	         //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
	         //report.write("\\pard\\page\n");
	   */
	   /************************************************/
	   /***********      �������� � 2      *************/
	   /************************************************/
	         report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

	         String tip_Ok_Zav = new String();
	       //  stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,z.PolnoeNaimenovanieZavedenija FROM Zavedenija z,Abiturient a, Punkty p WHERE a.kodzavedenija=z.kodzavedenija AND a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
	         stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija, k.name, a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,z.PolnoeNaimenovanieZavedenija, a.TipDokSredObraz, a.nomerShkoly FROM Zavedenija z, Abiturient a, KLADR k where a.kodzavedenija=z.kodzavedenija and k.code = a.kodPunkta AND a.KodAbiturienta LIKE  ?");
		       
	          stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

	             tip_Ok_Zav = "������������������� ����������";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

	             tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

	           } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

	             tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";
	           }
	           
	           String DokSrObr = null;
	           if(rs.getString(9) != null && rs.getString(9).equals("�"))
	               DokSrObr = " (��������)";
	             else
	               DokSrObr = " (�����)";
	           String nomerShkoly = "";
	           if (rs.getString(10)!=null){
	        	   nomerShkoly = nomerShkoly + "� " +  rs.getString(10);
	           }

	           report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"} {" +rs.getString(8)+" "+nomerShkoly+"}, ����������� � {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i   � "+rs.getString(6)+DokSrObr+"}\\i0\\fs6\\par\n");
	           //report.write("\\par\\ql\\fs22{�������� / ������: }\\i{"+rs.getString(7)+"}\\i0\n");
	         }
	   /////
	         report.write("\\par\\ql\\fs22{����� ����� ��� � �������������� �����___________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{����� ����� ������������� ��������� � �������������� ������������� ���������� ___________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{����� ��������� ����������� �����������__________________________________________________________}\n");
	         report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
	    //////
	   /************************************************/

	         stmt = conn.prepareStatement("select distinct k.target from konkurs k, TselevojPriem t where kodabiturienta  LIKE ? and k.target = t.kodTselevogoPriema");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         String predpr = new String();
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{������� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
	         while(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
	           //  report.write("\\b0\\fs6\\par\n");
	          
	             if(rs.getString(1).equals("2")) predpr = "�������";
	             else if(rs.getString(1).equals("3")) predpr = "���������";
	             else if(rs.getString(1).equals("4")) predpr = "�����������";
	             else if(rs.getString(1).equals("1")) predpr = "";
	             else predpr = "������";
	             report.write("\\i{"+predpr+"   }");//\\i0{ ��� ������� � ������� ��������
	           }
	         }
	         report.write("\\i0\n");
	         //���� 01.03.2016
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{������� �����, ��������� ����������� � ����� }:  ");
	         
	         
	         
	        // Select * from abitdopinf where kodabiturienta LIKE '66' and (ballAtt not in ('null', '0', '���') or ballSGTO  not in ('null', '0', '���') or ballZGTO  not in ('null', '0', '���') or ballSoch  not in ('null', '0', '���') or ballPOI  not in ('null', '0', '���') or TrudovajaDejatelnost  not in ('null', '0', '���')) 
	         
	         
   stmt = conn.prepareStatement("Select * from abitdopinf where kodabiturienta LIKE ? and (ballAtt not in ('null', '0', '���') or ballSGTO  not in ('null', '0', '���') or ballZGTO  not in ('null', '0', '���') or ballSoch  not in ('null', '0', '���') or ballPOI  not in ('null', '0', '���') or TrudovajaDejatelnost  not in ('null', '0', '���'))");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	        predpr = new String();
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{�������������� ���������� }:  ");//\\i0{ ��� ������� � ������� ��������
	         if(rs.next()) {
	          
	             report.write("\\i{  �� }");//\\i0{ ��� ������� � ������� ��������
	           }
	         else report.write("\\i{  ��� }");
	         
	         report.write("\\i0\n");
	   /************************************************/
	         
	         
	         //select distinct l.Lgoty from konkurs k, lgoty l where kodabiturienta like '29' and l.kodLgot = k.op
	         //   select distinct pr from konkurs where kodabiturienta like '66'
	         
	         
	         stmt = conn.prepareStatement("select distinct l.Lgoty from konkurs k, lgoty l where kodabiturienta like ? and l.kodLgot = k.op");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	        
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
	         while(rs.next()) {
	         
	            if (!rs.getString(1).equals("�� �������"))
	             report.write("\\i{"+rs.getString(1)+"   }");//\\i0{ ��� ������� � ������� ��������
	           }
	         
	         report.write("\\i0\n");
	         
	         
	         
	         
	         ///////////////////////////////////
	         
	         
	         
	         stmt = conn.prepareStatement("select distinct pr from konkurs where kodabiturienta like ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         String pr  = "�� �������";
	         report.write("\\b0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{���������������� ����� ��� ����������� }:  ");//\\i0{ ��� ������� � ������� ��������
	         while(rs.next()) {
	         
	             if (rs.getString(1).equals("1")) pr = "�������";
	            	 
	             
	           }
	         report.write("\\i{"+pr+"   }");//���������������� ����� �� ���
	         report.write("\\i0\n");

	         stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	           if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
	             report.write("\\b0\\fs6\\par\n");
	             report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1).toUpperCase()+"  (\"�\", \"�\" - ������� � ���������� ������; \"�\" - ������ � �������� ����)}\\i0\\fs6\\par\n");
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
	           if(rs.getString(1) != null && rs.getString(1).equals("�")) {

	             in_Jaz = "����������";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

	             in_Jaz = "��������";

	           } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

	             in_Jaz = "�����������";
	           }

	           if(rs.getString(2) != null && rs.getString(2).equals("�")) {

	             need_Obshaga = "��������";

	           } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

	             need_Obshaga = "�� ��������";
	           }
//	           report.write("\\par\n");

	           stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
	           stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	           rs = stmt.executeQuery();
	           if(rs.next()) {
	             if(rs.getString(2) != null && rs.getString(2).length() > 3)
	                report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
	             if(rs.getString(1) != null && rs.getString(1).length() > 3)
	               report.write("\\par\\ql\\fs22{���������������� ����� ��� �����������: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
	           }
	           report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
	           report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
	         }

	   /************************************************/

	      //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
	      //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	      //   report.write("\\par\\par\n");
	         stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {
	       	  if(rs.getString(1) != null){
	         report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
	         report.write("\\par\\ql\\fs22{���������� ����������):  }\\i{ "+rs.getString(1)+"}\\i0\\fs6\\par\n");}
	         //report.write("\\par\\par\n");
	       	  
	       	  
	       	  String Email = "";
	       	  String Address = "";
	       	  if (rs.getString(2)==null)Email = "-";
	       	  else Email = rs.getString(2);
	       	 if (rs.getString(3)==null)Address = "-";
	       	  else Email = rs.getString(3);
	       	  
	       			  
	         //report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+" �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
	         report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+Email+" �������� �����-"+Address+"}\\i0\\fs6\\par\n");
		       
	         //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
	         //report.write("\\par\\par\n");
	         }
	         report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22{� ������ �������� �� ������������� �������� ������������(� ������������) ����� 90��1 �0000704, ��}");
	         report.write("\\par\\ql\\fs22{21 ����� 2013 �.; � ������ ������������� � ��������������� ������������ (� ������������) �� 17.05.2013 �.}");  
	         report.write("\\par\\ql\\fs22{����� 90��1 �0000631 ���.�0627; c ����������� � ��������������� ����������� ������ ������ � }\n");
	         report.write("\\par\\ql\\fs22{������������� ��� ������ �� �������� �� ���������� ������������ � ���������� ������������ � ������}\n");
	         report.write("\\par\\ql\\fs22{���������� ������������� ������������ ��������� ��������� �������������� ������� �� ������ �����}\n");
	         report.write("\\par\\ql\\fs22{���������� �� ����� � ������ ����������� ����, � ������ ���������� ������������� ������������ �������� �}\n");
	         report.write("\\par\\ql\\fs22{�������� �� ���������� �� ����� �� ��������� �� �������� ������� ��������������� �����; � ��������� ������}\n");
	         report.write("\\par\\ql\\fs22{��������� �� ����������� ������������� ���������, ���������� ������������ �������������� ����������(�)}\n");
	         report.write("\\par\\ql\\fs22{							                               _________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\n");
	         //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
	         //report.write("\\pard\\page\n");
	         
	         /************************************************/

	      /*   report.write("\\par\\par\\par\n");
	         report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
	         report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");*/
	         report.write("\\par\\par\\par\n");
	         report.write("\\qj\\fs22\\tab{�������� �� ��������� ����� ������������ ������                                            }\\i{_________________}\\i0\n"); 
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

	   /************************************************/
	        // report.write("\\par\\par\\ql\\fs22\\tab{������ ���������������� ����������� ������� }\\i{�������.}\\i0\n");
	        //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
	        //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         
	         report.write("\\par\\par\\par\n");
	         // ��������� 29 ������� 2016 �.
	         report.write("\\par\\par\\ql\\fs22\\tab{����������(�) � ����������� � ������������� �������� � ��������� � ����� ����������� �������� � }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{������������� ��������� ����������                                                                   }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\\par\n");

	   /************************************************/
	         report.write("\\par\\par\\ql\\fs22\\tab{����������� ���������� ������� ���������, ������� �����������, ������� ��������(��� ����������� }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{�� �������� �� ����� � ������ ����������� ����)                                               }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\\par\n");
	         
		     // ��������� 29 ������� 2016 �.
	         report.write("\\par\\par\\ql\\fs22\\tab{����������� ���� ������ ��������� � ����� �� ����� ��� � 5 ����������� ������� �����������, }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{������� �����������, � ������� ������� ������ ���������                              }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\\par\n");
	         
//	         report.write("\\ql\\fs22\\tab{����������� ���� ������ ��������� �� ����� ��� � ���� ����� ��.                  }\\b0{_________________}\\bb\n");
//	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	        
		     // ��������� 29 ������� 2016 �.
	         report.write("\\par\\par\\ql\\fs22\\tab{����������� ���� ������ ��������� � ����� � ��� �� ����� ��� �� 3 �������������� � (���) }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{������������ ����������                                                                                          }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\\par\n");
	        
	         report.write("\\par\\par\\ql\\fs22\\tab{����������� ������ ��������� � ������ �� ��������� ���������������� ������� ����� ������ � ��� �}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{������ �� ���� ��������������� ���������.                                                         }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
	         report.write("\\par\\par\\par\n");

		     // ��������� 29 ������� 2016 �.
	         report.write("\\par\\par\\ql\\fs22\\tab{����������� ���� ������ ��������� � ����� �� ��������, ������������� ������� 14.1 ������ }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{����� �� ����� ��� � 3 ����������� ������� �����������, ������� �����������, � ������� ������� }\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{������ ��������� (������ ��� ���, ��������� ����������� � �����)              }\\i{_________________}\\i0\n");
	         report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
//	         report.write("\\par\\par\\par\n");

	   /************************************************/

	         //report.write("\\par\\par\\par\n");
	         //report.write("\\par\\qj\\fs22\\tab{� ��������� �� ����� ������������� ��������������� ������������ ����� 90��1 �0000704, �� 21 ����� 2013 �., �������������� � ��������������� ������������ �� 25.06.2012 ����� 90��1 � 000007 ���.�0007 � ���������� � ���, ����������� ������������� ���������, ��������� ����� ���, ��������� ���������� ��������� ����������(�).}\n");
	         //report.write("\\par\n");
	         //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
	         //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");*/

	   /************************************************/

	         //report.write("\\par\\par\\par\n");
	         //report.write("\\ql\\fs22\\tab{�������� � ����� ��� � ��� ����������� � �������� �������� �����������(�).}\n");
	         //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
	         //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

	   /************************************************/

	         //report.write("\\par\\par\\par\n");
	         //report.write("\\qj\\fs22\\tab\\b{����������(�) � ����� �������������� ��������� ��������� ���������������� ������� �� ����������� � ������������ � �������� ���������� (�.14) ������ ������.}\\b0\n");
	         //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
	         //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");



	         report.write("\\par\\par\n");
	         report.write("\\par\\ql\\fs22\\tab{����: "+StringUtil.CurrDate(".")+"�.}\n");
	         
	      // ��������� 29 ������� - 2 ����� 2016 �.
	         report.write("\\par\\par\n");
	         report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab\\tab{������������ ������� �������� ���, ������� �.�. ��������}\n");        

	      // ��������� 29 ������� - 2 ����� 2016 �.
	         String tip_Dok2 = new String();
	         boolean oblastIsEmpty2 = false;
	         stmt = conn.prepareStatement("SELECT kodOblastiP FROM Abiturient where kodAbiturienta = ?");
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()){
	        	 if(rs.getString(1).equals("-")) oblastIsEmpty = true;
	         }
	         
	      // ��������� 29 ������� - 2 ����� 2016 �.
	         if (oblastIsEmpty){
	         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,a.gorod_prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a WHERE KodAbiturienta LIKE ?");
	   	      	 
	         }
	         else{
	         stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
	         }
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	         report.write("\\par\\qc\\fs22\\tab\\tab\\tab{  ����������� }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
	         }
	      // ��������� 29 ������� - 2 ����� 2016 �.
	         report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab{��������� ��������� �� ������� � �������� �� ��������}\n");  
	         
	         report.write("\\par\\qc\\fs22\\tab{         �� ��������������� ���������  }\n");
	         report.write("\\par\\par\n");
	         

	         // ��������� 29 ������� - 2 ����� 2016 �.
	         stmt = conn.prepareStatement("SELECT s.ShifrSpetsialnosti, s.NazvanieSpetsialnosti, e.name, ko.Forma_ob from konkurs ko, spetsialnosti s, edulevel e where ko.kodabiturienta like ? and ko.kodspetsialnosti=s.kodspetsialnosti and e.socr = s.edulevel");
	         
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\tab\\tab\\tab\\tab\\tab\\fs22\\ql{ }\\i{"+rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+"}\\i0\n");
	           report.write("\\qc\\fs6\\par\\par\\fs22");
	         }
	      // ��������� 29 ������� - 2 ����� 2016 �.
	         stmt = conn.prepareStatement("select nomerlichnogodela from abiturient where kodabiturienta like ?");
	         
	         stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
	         rs = stmt.executeQuery();
	         if(rs.next()) {

	           report.write("\\tab\\fs22\\ql{������ ���� � }\\i{"+rs.getString(1)+"}\\i0\n");
	           report.write("\\qc\\fs6\\par\\par\\fs22");
	           report.write("\\par\\par\n");
	           report.write("\\par\\par\n");
	           
	         }
	      // ��������� 29 ������� - 2 ����� 2016 �.           
	         report.write("\\qc\\fs28\\b{���������}\\b0\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{����� ������ �� �������� � ���������� �� �������� �� ���������}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(���, ��, ����� ��������)}\\b0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         
	         report.write("\\par\\par\\ql\\fs22\\tab{��� ����� �� �������� �� ����� �� ��������� � ������� ��������� ��������:}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\tab{- �� ������ ��������.}\n");
	        
	         report.write("\\par\\par\\ql\\fs22\\tab{� ������� ��������� ��������}\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(�������� ��������� �� �����������, �����, ���. �)}\\b0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{�����______________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(�������� ��������������� �����������, �������� ��������, ���� ������:)}\\b0\n");	
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	         report.write("\\qc\\fs16\\b{(��������, �����, ���������� ����������� / ������� ��������� (�������� ����������))}\\b0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
	      
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{(�.�.�.)                                                                                                             }\\i{(�������)}\\i0\n");
	         report.write("\\par\\par\\ql\\fs22\\tab{''___''________________ 2016 �.}\n");
	         
	         report.write("}"); 
	         report.close();

	   //System.out.println("Doc-!->6");

	      }
   
/***********************************************************/
/***********************************************************/
/*****  ��������� ���������/����������� (�,�,�,�,�,�) ******/
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
      if(rs.next()) report.write("\\fs24\\b\\qc{������� }{"+rs.getString(1)+"}\\b0\n");

      report.write("\\par\\par\n");

/************************************************/

    /*  String tip_Dok = new String();
      stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,Gorod_Prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\fs22\\ql{�, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{����������: }\\i{"+rs.getString(4)+"}\\i0,  ���� ��������: \\i{"+rs.getString(5)+"�.},\n");//\\i0{, ����� ��������: }\\i{"+rs.getString(16)+"}\\i0
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{������������ �� ������: }\\i{"+rs.getString(6)+", ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        if(rs.getString(10) != null && rs.getString(10).equals("�"))
          tip_Dok = "������� (�����-�)";
        else
          tip_Dok = "�������";
        report.write("{��������, �������������� ��������: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" � "+rs.getString(12)+", �����: "+rs.getString(13)+", "+rs.getString(14)+"�.}\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{���������� �������: }\\i{"+rs.getString(15)+"}\\i0\n");
        report.write("\\par\\par\n");
      }

/************************************************/

     /* report.write("\\qc\\fs28\\b{���������}\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{����� ��������� ���� � ������� � �������� ��� ����������� �� ������������� ��� ����������� ���������� ������������ �(���) ��������� ������������:}\n");
      report.write("\\par\\par\n");

/************************************************/

    /*  stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
//        if(abit_A.getTip_Spec().equals("�")) {
//          report.write("\\ql\\fs24\\b\\i\\tab{�� ������������� ����� ��������}\\i0\\b0\n");
//        } else {
    	
        report.write("\\ql\\fs24\\b\\i\\tab{�� ����� ����� ��������}\\i0\\b0\n");
//        }
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� � ������ ����������� ���� ������ ������� �� �������� �� ���� ��������� ������������}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') ORDER BY kon.Prioritet ASC");
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
if(rs.getString(4).equals("�")){
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
     /* stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') AND kon.Dog IN('�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) { 
      report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
      report.write("\\par\\par\n");
      
      report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
      //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
      report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
      //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

      report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
      report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
      stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�') AND kon.Dog IN('�') ORDER BY kon.Prioritet ASC");
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
         //if(rs.getString(5).equals("�"))
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
     /* stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\pard\\par\n");
        report.write("\\ql\\fs24\\b\\i\\tab{�� ����-������� ����� ��������}\\i0\\b0\n");

        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
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
        report.write("\\ql\\fs24\\b\\i\\tab{�� ������� ����� ��������}\\i0\\b0\n");
        ////////////////
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� � ������ ����������� ���� ������ ������� �� �������� �� ���� ��������� ������������}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");
        report.write("\\intbl\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
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
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� ��������������� �����}\n");
        report.write("\\par\\par\n");
        
        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{�������������/�����������, ������������� � ������� ���������� ����������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2500\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�') ORDER BY kon.Prioritet ASC");
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
      report.write("\\par\\ql\\fs24\\tab\\b{�����:}\\b0\\fs6\\par\n");
//System.out.println("Doc-!->4");
      int counter = 1;
////////////////////////////////////////////////���� �� ������ !!!!
      stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". ��������� � �������� ����������� ������������� ��������� ������, ���������� ��� �����}\\fs24{���:}\n");

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". ��������� � �������� ����������� ������������� ��������� ������, ���������� ��� �����    }\\fs24{���:}\n");
        while(rs.next()) {
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+" - "+rs.getString(2)+" ������;}\\i0");
        }
      }

/************************************************/
      //report.write("\\par\\par\\ql\\fs24\\tab\\b{�����:}\\b0\\fs6\\par\n");
      //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".� ������������ � �������� 8 ������ ������ ��� ��������� � ����� ���������, ���������� ������������� �������������� �� ���������:}\n");      

    /*  stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('�') or zso.Examen IN('�')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".� ������������ � �������� 8 ������ ������ ��� ��������� � ����� ���������, ���������� ������������� �������������� �� ���������:}\n");      

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND (zso.Examen IN('�') or zso.Examen IN('�')) AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()) {
        	//report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+".� ������������ � �������� 8 ������ ������ ��� ��������� � ����� ���������, ���������� ������������� �������������� �� ���������:}\n");
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
        }
      }

/************************************************/
      //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". � ������������ � �������� 9 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� ������������� ���������, ���������� ������������� ��������������:}\n");
      
      //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      //rs = stmt.executeQuery();
      //if(rs.next()) {
      
        //report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". � ������������ � �������� 9 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� ������������� ���������, ���������� ������������� ��������������:}\n");

        //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
        //stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        //rs = stmt.executeQuery();
        //while(rs.next()) {
      /*  stmt = conn.prepareStatement("SELECT ProvidingSpecialConditions FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
        	
        if(rs.getString(1)!=null){
           report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". � ������������ � �������� 9 ������ ������ ��� ���������� ��������� ����������� ������� ��� ����� ������������� ���������, ���������� ������������� ��������������:}\n");
           report.write("\\par\\ql\\fs22\\tab\\i{"+rs.getString(1)+";}\\i0");}
        }
       // }
     // }

/************************************************/
//////////////////////////////////////////////���� �� ������ !!! �����
   /*   report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

      String tip_Ok_Zav = new String();
      stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

          tip_Ok_Zav = "������������������� ����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";
        }

        report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"}, ����������� � {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  "+rs.getString(5)+" � "+rs.getString(6)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{����� ����������� ��� / ����� ����� ���: }\\i{"+rs.getString(7)+"}\\i0\n");
      }
/////
      report.write("\\par\\ql\\fs22{����� ����� ��� � �������������� �����___________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{����� ����� ������������� ��������� � �������������� ������������� ���������� ___________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{����� ��������� ����������� �����������__________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
 //////


      stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
          report.write("\\b0\\fs6\\par\n");
          String predpr = new String();
          if(rs.getString(1).equals("�")) predpr = "(���������)";
          else if(rs.getString(1).equals("�")) predpr = "(�������)";
          else if(rs.getString(1).equals("�")) predpr = "(�����������)";
          else predpr = "�����������";
          report.write("\\par\\ql\\fs22{������� ����������� }\\i{"+predpr+"}\n");//\\i0{ ��� ������� � ������� ��������
        }
      }



      stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
          report.write("\\b0\\fs6\\par\n");
          report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1).toUpperCase()+"  (\"�\", \"�\" - ������� � ���������� ������; \"�\" - ������ � �������� ����)}\\i0\\fs6\\par\n");
        }
      }

//System.out.println("Doc-!->5");


      String in_Jaz = new String();
      String need_Obshaga = new String();
      stmt = conn.prepareStatement("SELECT InostrannyjJazyk,NujdaetsjaVObschejitii FROM Abiturient WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "����������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "��������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "�����������";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "��������";

        } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "�� ��������";
        }
//        report.write("\\par\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
             report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
          if(rs.getString(2) != null && rs.getString(2).length() > 3)
            report.write("\\par\\ql\\fs22{���������������� ����� ��� �����������: }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
        }
        report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
      }



   //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
   //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\par\n");
      
      report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
      report.write("\\par\\ql\\fs22{���������� ����������)      ____________________________________________________________________}\n");
      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): _____________________________}\n");
      report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      report.write("\\par\\par\n");
      
      //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
      //report.write("\\pard\\page\n");
*/
/************************************************/
/***********      �������� � 2      *************/
/************************************************/
/*      report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

      String tip_Ok_Zav = new String();
      stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

          tip_Ok_Zav = "������������������� ����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";
        }

        report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"}, ����������� � {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  "+rs.getString(5)+" � "+rs.getString(6)+"}\\i0\\fs6\\par\n");
        //report.write("\\par\\ql\\fs22{�������� / ������: }\\i{"+rs.getString(7)+"}\\i0\n");
      }
/////
      report.write("\\par\\ql\\fs22{����� ����� ��� � �������������� �����___________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{����� ����� ������������� ��������� � �������������� ������������� ���������� ___________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{����� ��������� ����������� �����������__________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{_________________________________________________________________________________________________}\n");
 //////
/************************************************/

   /*   stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
          report.write("\\b0\\fs6\\par\n");
          String predpr = new String();
          if(rs.getString(1).equals("�")) predpr = "(���������)";
          else if(rs.getString(1).equals("�")) predpr = "(�������)";
          else if(rs.getString(1).equals("�")) predpr = "(�����������)";
          else predpr = "�����������";
          report.write("\\par\\ql\\fs22{������� ����������� }\\i{"+predpr+"}\n");//\\i0{ ��� ������� � ������� ��������
        }
      }

/************************************************/

   /*   stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
          report.write("\\b0\\fs6\\par\n");
          report.write("\\par\\ql\\fs22{�������������� ����������: }\\i{"+rs.getString(1).toUpperCase()+"  (\"�\", \"�\" - ������� � ���������� ������; \"�\" - ������ � �������� ����)}\\i0\\fs6\\par\n");
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
        if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "����������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "��������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "�����������";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "��������";

        } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "�� ��������";
        }
//        report.write("\\par\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          if(rs.getString(2) != null && rs.getString(2).length() > 3)
             report.write("\\par\\ql\\fs22{������ ����� ��� ����������� }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");//���� ������ ��� ����������� ��������:
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
            report.write("\\par\\ql\\fs22{���������������� ����� ��� �����������: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");//���������� ���������
        }
        report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
      }

/************************************************/

   //  report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
   //  report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
   //   report.write("\\par\\par\n");
     /* stmt = conn.prepareStatement("SELECT ReturnDocument,AbitEmail,Address FROM AbitDopInf WHERE KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
    	  if(rs.getString(1) != null){
      report.write("\\par\\ql\\fs22{������ �������� ���������� � ������ �� ����������� � �����������(� ������ ��������������        }\n");
      report.write("\\par\\ql\\fs22{���������� ����������)}\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");}
      //report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22{�������� ����� �(���) ����������� ����� (�� ������� ������������): Email-"+rs.getString(2)+" �������� �����-"+rs.getString(3)+"}\\i0\\fs6\\par\n");
      //report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      //report.write("\\par\\par\n");
      }
      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22{� ������ �������� �� ������������� �������� ������������(� ������������) ����� 90��1 �0000704, ��}");
      report.write("\\par\\ql\\fs22{21 ����� 2013 �.; � ������ ������������� � ��������������� ������������ (� ������������) �� 17.05.2013 �.}");  
      report.write("\\par\\ql\\fs22{����� 90��1 �0000631 ���.�0627; c ����������� � ��������������� ����������� ������ ������ � �������������}\n");
      report.write("\\par\\ql\\fs22{��� ������ �� �������� �� ���������� ������������ � ���������� ������������ � ������ ���������� �������������}\n");
      report.write("\\par\\ql\\fs22{������������ ��������� ��������� �������������� ������� �� ������ ����� ���������� �� ����� � ������}\n");
      report.write("\\par\\ql\\fs22{����������� ����, � ������ ���������� ������������� ������������ �������� � �������� �� ���������� �� ����� ��}\n");
      report.write("\\par\\ql\\fs22{��������� �� �������� ������� ��������������� �����; � ��������� ������ ��������� �� ����������� �������������}\n");
      report.write("\\par\\ql\\fs22{���������, ���������� ������������ �������������� ����������(�)}\n");
      report.write("\\par\\ql\\fs22{                                                                                        _________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
      report.write("\\par\\par\n");
      
      //report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
      //report.write("\\pard\\page\n");
      
      /************************************************/

   /*   report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab{������� ���� �������� �� ��������� ������������ ������ � �������, ������������� ����������� ������� �� 27 ���� 2006�. � 152-�� }\\'ab{� ������������ ������}\\'bb.\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");*/
      /*report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab{�������� �� ��������� ����� ������������ ������                                }\\'ab{_________________}\\'bb.\n"); 
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/
     // report.write("\\par\\par\\ql\\fs22\\tab{������ ���������������� ����������� ������� }\\i{�������.}\\i0\n");
     //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
     //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
      
    /*  report.write("\\par\\par\\par\n");
    
      report.write("\\par\\par\\ql\\fs22\\tab{����������(�) � ����������� �� �������������� �� ������������� ��������, ����������� � ��������� � }\n");
      report.write("\\par\\par\\ql\\fs22\\tab{������, � �� ����������� ����������, ���������� ��� �����������)         }\\i{_________________}\\i0\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
      report.write("\\par\\par\\par\n");

/************************************************/
     /* report.write("\\par\\par\\ql\\fs22\\tab{����������� ���������� ������� ���������, ������� �����������, ������� ��������(��� ����������� }\n");
      report.write("\\par\\par\\ql\\fs22\\tab{�� �������� �� ����� � ������ ����������� ����)                          }\\i{_________________}\\i0\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
      report.write("\\par\\par\\par\n");
      
      report.write("\\ql\\fs22\\tab{����������� ���� ������ ��������� �� ����� ��� � ���� ����� ��.                  }\\b0{_________________}\\bb.\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
     
      report.write("\\par\\par\\ql\\fs22\\tab{����������� ������ ��������� � ������ �� ��������� ��������������� ������� ����� ������ � ��� �}\n");
      report.write("\\par\\par\\ql\\fs22\\tab{������ �� ���� ��������������� ���������)                                }\\i{_________________}\\i0\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
      report.write("\\par\\par\\par\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\par\\qj\\fs22\\tab{� ��������� �� ����� ������������� ��������������� ������������ ����� 90��1 �0000704, �� 21 ����� 2013 �., �������������� � ��������������� ������������ �� 25.06.2012 ����� 90��1 � 000007 ���.�0007 � ���������� � ���, ����������� ������������� ���������, ��������� ����� ���, ��������� ���������� ��������� ����������(�).}\n");
      //report.write("\\par\n");
      //report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\ql\\fs22\\tab{�������� � ����� ��� � ��� ����������� � �������� �������� �����������(�).}\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      //report.write("\\par\\par\\par\n");
      //report.write("\\qj\\fs22\\tab\\b{����������(�) � ����� �������������� ��������� ��������� ���������������� ������� �� ����������� � ������������ � �������� ���������� (�.14) ������ ������.}\\b0\n");
      //report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      //report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");



     /* report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22\\tab{����: "+StringUtil.CurrDate(".")+"�.}\n");
      report.write("}"); 
      report.close();
*/
//System.out.println("Doc-!->6");

   // else if ( abit_A.getNeed_Spo().equals("�")) {
    	  else if ( abit_A.getNomerPotoka() == 6) {

/***********************************************************/
/***********************************************************/
/*****            ��������� �� ��� (�,�)              ******/
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
      if(rs.next()) report.write("\\fs24\\b\\qc{������� }{"+rs.getString(1)+"}\\b0\n");

      report.write("\\par\\par\n");

/************************************************/

      String tip_Dok = new String();
      stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\fs22\\ql{�, }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{���������(��): }\\i{"+rs.getString(4)+"}\\i0,  ���� ��������: \\i{"+rs.getString(5)+"�.}\\i0,\n");//\\i0{, ����� ��������: }\\i{"+rs.getString(16)+"}
        report.write("\\fs6\\par\\par\\fs22");
        report.write("{�����������(���) �� ������: }\\i{"+rs.getString(6)+", ��."+rs.getString(7)+", �."+rs.getString(8)+", ��."+rs.getString(9)+"}.\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        if(rs.getString(10) != null && rs.getString(10).equals("�"))
          tip_Dok = "�������";
        else
          tip_Dok = "�������";
        report.write("{��������, �������������� ��������: }\\i{"+tip_Dok+" }{"+rs.getString(11)+" � "+rs.getString(12)+", �����: "+rs.getString(13)+", "+rs.getString(14)+"�.}\\i0\n");
        report.write("\\fs6\\par\\par\\fs22");
        report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{���������� �������: }\\i{"+rs.getString(15)+"}\\i0\n");
        report.write("\\par\\par\n");
      }

/************************************************/

      report.write("\\qc\\fs28\\b{���������}\\b0\n");
      report.write("\\par\\par\\qj\\fs22\\tab{����� ����������� ����������� ���������� �� ������������� �������� ����������������� �����������:}\n");
      report.write("\\par\\par\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Bud LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�','�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\ql\\fs24\\b\\i\\tab{�� ����� ����� ��������}\\i0\\b0\n");
        
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� � ������ ����������� ���� ������ ������� �� �������� �� ���� ��������� ������������}\n");

        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");

        report.write("\\intbl\\qc\\b{�������������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����}\\b0\\cell\n");
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
        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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
        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Fito,Target FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�','�') AND kon.Bud LIKE ('�') ORDER BY kon.Prioritet ASC");
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

      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Dog LIKE '�' AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN('�','�','�','�','�','�') ORDER BY kon.Prioritet ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\pard\\par\n");
        //report.write("\\ql\\fs24\\b\\i\\tab{�� �����}\\i0\\b0\n");
        report.write("\\ql\\fs24\\b\\i\\tab{�� ����� ����� ��������}\\i0\\b0\n");
        
        report.write("\\par\\par\\qj\\fs22\\tab{�� ����� �� ��������� �� �������� ������� �����}\n");

        report.write("\\par\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmgf\\clvertalc \\cltxbtlr \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\qc\\b{�������������}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{���}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        //report.write("\\intbl\\qc\\b{����.}\\b0\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\fs20\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2000\n");
        report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9500\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10000\n");
        //report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10500\n");

        report.write("\\intbl\\ql{\\tab\\b{      ���}\\b0}\\cell\n");
        report.write("\\intbl\\qc\\b{������ ������������}\\b0\\cell\n");
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

        stmt = conn.prepareStatement("SELECT Prioritet,ShifrSpetsialnosti,NazvanieSpetsialnosti,Bud,Dog,Six,Three FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND kon.Forma_Ob IN ('�','�','�','�','�','�') AND kon.Dog IN('�') ORDER BY kon.Prioritet ASC");
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
      //report.write("\\par\\ql\\fs24\\tab\\b{�����:}\\b0\\fs6\\par\n");

      int counter = 1;

      //stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
     // stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      //rs = stmt.executeQuery();
      //if(rs.next()) {

       // report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". ��������� � �������� ����������� ������������� ��������� ������, ���������� ��� ����� }\\fs24{���:}\n");

       // stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.OtsenkaEge,ens.KodPredmeta FROM Konkurs kon,EkzamenyNaSpetsialnosti ens,ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE np.KodPredmeta=ens.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND zso.KodPredmeta=ens.KodPredmeta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND kon.KodAbiturienta LIKE ? AND OtsenkaEge NOT LIKE '0' ORDER BY ens.KodPredmeta ASC");
      //  stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       // rs = stmt.executeQuery();
      //  while(rs.next()) {
      //     report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+" - "+rs.getString(2)+" ������;}\\i0");
      //  }
     // }

/************************************************/
/*
      stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". ��������� � ����� ��������� � ������� }\\fs24{��� }\\fs22{�� ���������:}\n");      

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
       while(rs.next()) {
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
       }
      }
*/
/************************************************/
/*
      stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
     stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
      
        report.write("\\par\\ql\\fs22\\tab{  "+(counter++)+". ��������� � ����� ��������� � ������� ���� �� ���������:}\n");

        stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.KodAbiturienta LIKE ? AND zso. Examen IN('�') AND OtsenkaEge LIKE '0' ORDER BY zso.KodPredmeta ASC");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
        while(rs.next()) {
           report.write("\\par\\ql\\fs22\\tab\\i{          - "+rs.getString(1)+";}\\i0");
        }
      }
*/
/************************************************/

      report.write("\\par\\par\\ql\\fs24\\tab\\b{� ���� ������� ���������:}\\b0\\fs6\\par\n");

      String tip_Ok_Zav = new String();
   //   stmt = conn.prepareStatement("SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija,p.Nazvanie,a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata FROM Abiturient a, Punkty p WHERE a.KodPunkta=p.KodPunkta AND a.KodAbiturienta LIKE ?");
      stmt = conn.prepareStatement(" SELECT a.GodOkonchanijaSrObrazovanija,a.TipOkonchennogoZavedenija, k.name, a.VidDokSredObraz,a.SeriaAtt,a.NomerAtt,a.NomerSertifikata,z.PolnoeNaimenovanieZavedenija FROM Zavedenija z, Abiturient a, KLADR k where a.kodzavedenija=z.kodzavedenija and k.code = a.gorod_Prop AND a.KodAbiturienta LIKE  ?");
      

      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {

          tip_Ok_Zav = "������������������� ����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� ���������� ����������������� �����������";

        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          tip_Ok_Zav = "������������������� ���������� �������� ����������������� �����������";
        }

        report.write("\\par\\ql\\fs22{�������(�) � }\\i{"+rs.getString(1)+"�. }{"+tip_Ok_Zav+"}, ����������� � {"+rs.getString(3)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{"+rs.getString(4)+":\\i  � "+rs.getString(6)+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{������� ����������� �����������:}\n"); 
        if(rs.getString(2) != null && (rs.getString(2).equals("�") || rs.getString(2).equals("�") || rs.getString(2).equals("�"))) {
        	 report.write("\\par\\ql\\fs22{������� (������) ����� �����������}\n");
        } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

        	report.write("\\par\\ql\\fs22{�������� ����� �����������}\n");

          } else if(rs.getString(2) != null && rs.getString(2).equals("�")) {

        	  report.write("\\par\\ql\\fs22{������� ���������������� �����������}\n");
          }
          else if(rs.getString(2) != null && rs.getString(2)!="�" && rs.getString(2)!="�" && rs.getString(2)!="�" && rs.getString(2)!="�" && rs.getString(2)!="�" && rs.getString(2)!="�") {

        	  report.write("\\par\\ql\\fs22{������ �����������}\n");
          }
        
        //report.write("\\par\\ql\\fs22{����� ����������� ��� / ����� ����� ���: }\\i{"+rs.getString(7)+"}\\i0\n");
      }

/************************************************/
/*
      stmt = conn.prepareStatement("SELECT ShifrPriema FROM Abiturient a,TselevojPriem tp WHERE a.KodTselevogoPriema=tp.KodTselevogoPriema AND KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
          report.write("\\b0\\fs6\\par\n");
          String predpr = new String();
          if(rs.getString(1).equals("�")) predpr = "����������� (���������)";
          else if(rs.getString(1).equals("�")) predpr = "����������� (�������)";
          else if(rs.getString(1).equals("�")) predpr = "����������� (�����������)";
          else predpr = "�����������";
          report.write("\\par\\ql\\fs22{���� ����������� �� }\\i{"+predpr+"}\\i0{ ��� ������� � ������� ��������}\n");
        }
      }
*/
/************************************************/
/*
      stmt = conn.prepareStatement("SELECT ShifrMedali FROM Abiturient a,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        if(rs.getString(1) != null && !rs.getString(1).equals("�")) {
          report.write("\\b0\\fs6\\par\n");
          report.write("\\par\\ql\\fs22{�������: }\\i{"+rs.getString(1).toUpperCase()+"  (\"�\", \"�\" - ������� � ���������� ������; \"�\" - ������ � �������� ����)}\\i0\\fs6\\par\n");
        }
      }
*/
      report.write("\\par\\ql\\fs22{����� ��������� ����������� �����������__________________________________________________________}\n");      
      
//report.write("\\pard\\page\n");
      
      report.write("\\par\\par\n");
      report.write("\\par\\par\\qj\\fs22\\tab{���������� �������� ��������� ������ ��� �������� (�������) ������ �����������:}\n");
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
      report.write("��������\\intbl\\cell");
      report.write("������\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("������� ����\\intbl\\cell");
      report.write("{"+rus+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������\\intbl\\cell");
      report.write("{"+lit+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������� ����\\intbl\\cell");
      report.write("{"+inYz+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������\\intbl\\cell");
      report.write("{"+matem+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������� � ���\\intbl\\cell");
      report.write("{"+inf+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("�������\\intbl\\cell");
      report.write("{"+ist+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("��������������\\intbl\\cell");
      report.write("{"+obch+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("������\\intbl\\cell");
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
        if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "����������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "��������";

        } else if(rs.getString(1) != null && rs.getString(1).equals("�")) {

          in_Jaz = "�����������";
        }

        if(rs.getString(2) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "��������";

        } else if(rs.getString(1) != null && rs.getString(2).equals("�")) {

          need_Obshaga = "�� ��������";

        }
//        report.write("\\par\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          if(rs.getString(1) != null && rs.getString(1).length() > 3)
             report.write("\\par\\ql\\fs22{���������� ���������: }\\i{"+rs.getString(1)+"}\\i0\\fs6\\par\n");
          if(rs.getString(2) != null && rs.getString(2).length() > 3)
            report.write("\\par\\ql\\fs22{���� ������ ��� ����������� ��������: }\\i{"+rs.getString(2)+"}\\i0\\fs6\\par\n");
        }
        report.write("\\par\\ql\\fs22{������(�) ����������� ����: }\\i{"+in_Jaz+"}\\i0\\fs6\\par\n");
        report.write("\\par\\ql\\fs22{� ���������: }\\i{"+need_Obshaga+"}\\i0\\fs6\\par\n");
      }

/************************************************/
/*
      report.write("\\par\\ql\\fs22{� ���� ������������� �������: ___________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      report.write("\\par\\ql\\fs22{________________________________________________________________________________________________}\n");
      report.write("\\par\\par\n");
      report.write("\\par\\qj\\fs20\\b\\i{�������� ������������! ����, ������� ������ ��� ����������� � ������������ � ��.3.5, 3.6, 3.7 ������ ������, ������ ������������ ���������� �������������� ���������� �������������� ��������� �������� ��������.}\\i0\\b0\n");
      report.write("\\par\\par");
  */    
     /* 
      report.write("\\pard\\page\n");
      
      report.write("\\par\\par\n");
      report.write("\\par\\par\\qj\\fs22\\tab{���������� �������� ��������� ������ ��� �������� (�������) ������ �����������:}\n");
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
      report.write("��������\\intbl\\cell");
      report.write("������\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("������� ����\\intbl\\cell");
      report.write("{"+rus+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������\\intbl\\cell");
      report.write("{"+lit+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������� ����\\intbl\\cell");
      report.write("{"+inYz+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������\\intbl\\cell");
      report.write("{"+matem+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("����������� � ���\\intbl\\cell");
      report.write("{"+inf+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("�������\\intbl\\cell");
      report.write("{"+ist+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("��������������\\intbl\\cell");
      report.write("{"+obch+"}\\intbl\\cell");
      report.write("\\row");
      report.write("\\trowd\\trgaph144");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx3000");
      report.write("\\clbrdrt\\brdrs\\clbrdrl\\brdrs\\clbrdrb\\brdrs\\clbrdrr\\brdrs");
      report.write("\\cellx6000");
      report.write("������\\intbl\\cell");
      report.write("{"+fiz+"}\\intbl\\cell");
      report.write("\\row");
      report.write("");
      report.write("\\pard\\par\n");
      
      
      
      */
      
      /*
       * 
       * */
      
       
       
      
      

/************************************************/
/***********      �������� � 2      *************/
/************************************************/
      report.write("\\par\\par\\qr\\fs22{ }\n");
      report.write("\\par\\par\\ql\\fs22\\tab{������� ���������������� ����������� ������� }\\i{�������.}\\i0\n");
      report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      report.write("\\par\\par\\par\n");
      report.write("\\par\\qj\\fs22\\tab{� ������ �������� �� ����� ������������� ��������������� ������������ ����� 90��1 �0000704, �� 21 ����� 2013 �., � ������ ������������� � ��������������� ������������ �� 17.05.2013 ����� 90��1 � 000631 ���.�0627 ����������(�).}\n");
      report.write("\\par\n");
      report.write("\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/
/*
      report.write("\\par\\par\\par\n");
      report.write("\\ql\\fs22\\tab{�������� � ����� ��� � ��� ����������� � �������� �������� �����������(�).}\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");
*/
/************************************************/

      report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab\\b{����������(�) � ����� �������������� ��������� ��������� ���������������� ������� �� ����������� � ������������ � �������� ���������� (�.7) ������ ������.}\\b0\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      report.write("\\par\\par\\par\n");
      report.write("\\qj\\fs22\\tab{C������� �� ��������� ����� ������������ ������}\n");
      report.write("\\par\\par\\par\\qr\\fs22{________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{(������� ������������)}\n");

/************************************************/

      report.write("\\par\\par\n");
      report.write("\\par\\ql\\fs22\\tab{����: "+StringUtil.CurrDate(".")+"�.}\n");

      report.write("}"); 
      report.close();
   }



/************************************************/
/************************************************/
/******             ��������              *******/
/************************************************/
/************************************************/

      String file_con = new String("receipt"+abit_A.getKodAbiturienta()+".rtf");
 
      String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;

      abit_A.setFileName2(file_con);

      BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

      report.write("{\\rtf1\\ansi\n");
      report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
      report.write("\\fs24\\i{��������� �������� � ������ ����}\\i0\\par\\par\n");

      report.write("\\trowd\\ts15\\trqc\\trgaph108\\trleft-108\\brdrs\\brdrw10\n");
      report.write("\\clvertalt\\cltxlrtb\\cellx7011\n");
      report.write("\\clvertalt\\clbrdrr\\brdrdash\\brdrw20\\cltxlrtb\\cellx7364\n");
      report.write("\\clvertalt\\clbrdrl\\brdrdash\\brdrw20\\cltxlrtb\\cellx7717\n");
      report.write("\\clvertalt\\cltxlrtb\\cellx14730");

/************************************************/
/************** ����� ��������� *****************/
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
      if(rs.next()) report.write("\\qc\\fs28\\b{����� ������� ���� � "+rs.getString(1)+"}\\par\\b0\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT UPPER(f.AbbreviaturaFakulteta) FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\fs24\\par\\ql{������������ �� ��������� (��������):  }\\b{"+rs.getString(1)+"}\\b0\n");
        report.write("\\fs6\\par\\par\\fs24{�� �������������� (������������):}\n");
        report.write("\\fs6\\par\\par\\par\n");
      }

/************************************************/

      report.write("\\pard\\plain\n");
      report.write("\\qc \\intbl\\itap2\n");

      report.write("\\b{����}\\nestcell{\\nonesttables\\par }\n");
      report.write("{������������ ������������� (�����������)}\\b0\\nestcell\\b0{\\nonesttables\\par }\n");
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
        report.write("\\par\\ql\\fs24\\tab{����������  }\\b{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\b0\\par\n");
      }

      String copyDokSrObr = new String();
      String copySert = new String();
      stmt = conn.prepareStatement("SELECT a.VidDokSredObraz,SeriaAtt,NomerAtt,NomerSertifikata,a.Tel,a.TipDokSredObraz,a.KopijaSertifikata FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(6) != null && rs.getString(6).equals("�"))
          copyDokSrObr = " (��������)";
        else
          copyDokSrObr = " (�����)";

        if(rs.getString(7) != null && rs.getString(7).equals("�"))
          copySert = " (��������)";
        else
          copySert = " (�����)";

        report.write("\\par\\ql\\fs24\\tab{�����������(�) ��������� ���������:}\n");
        report.write("\\fs4\\par\\par\\fs24\\ql{1. ���������}\n");
        report.write("\\fs4\\par\\par\\fs24\\ql{2. ����� ����������}\n");
        report.write("\\fs4\\par\\par\\fs24\\ql{3. "+(rs.getString(1)).substring(0,1).toUpperCase()+(rs.getString(1)).substring(1)+": � "+rs.getString(3)+copyDokSrObr+"}\n");
        if(rs.getString(4) != null && !rs.getString(4).equals("-"))
          report.write("\\fs4\\par\\par\\fs24\\ql{4. ������������� ���: "+rs.getString(4)+copySert+"}\n");
        else
          report.write("\\fs4\\par\\par\\fs24\\ql{4.}\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(1) != null && rs.getString(1).length() > 3) {
             report.write("\\ql{5. ������ ���������� ���������: }\\i{"+rs.getString(1)+"}\\i0\n");
          } else {
             report.write("\\ql{5.}\n");
          }

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(2) != null && rs.getString(2).length() > 3) {
             report.write("\\ql{6. ��������, �������������� ������: }\\i{"+rs.getString(2)+"}\\i0\n");
          } else {
             report.write("\\ql{6.}\n");
          }
        }
        report.write("\\par\\par\n");
      }

/************************************************/

      report.write("\\par\\ql\\fs24{��������� ������}\n");
      report.write("\\par\\ql\\i\\tab{��������� �������� ��������: _______________________}\\i0\n");
      report.write("\\par\\ql\\i\\tab{"+StringUtil.CurrDate(".")+"�.}\\i0\\par\n");
      report.write("\\par\\ql\\fs24{���� �������� �� ���������: ___________________________}\n");
      report.write("\\par\\ql\\i\\tab{������}\\i0\n");
      report.write("\\par\\ql\\i\\tab{��������� �������� ��������: _______________________}\\i0\n");
      report.write("\\par\\ql\\i\\tab{�____�_________"+StringUtil.CurrYear()+"�.}\\i0\n");
      report.write("\\cell\\pard\\plain\n");

/*************************************************/
/************** ������ ��������� *****************/
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
      if(rs.next()) report.write("\\qc\\fs28\\b{�������� � "+rs.getString(1)+"}\\par\\b0\n");

/************************************************/

      stmt = conn.prepareStatement("SELECT UPPER(f.AbbreviaturaFakulteta) FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
        report.write("\\fs24\\par\\ql{� ������ ���������� �� ��������� (��������):  }\\b{"+rs.getString(1)+"}\\b0\n");
        report.write("\\fs6\\par\\par\\fs24\n");
        report.write("{�� �������������� (������������):}\n");
        report.write("\\fs6\\par\\par\\fs22\n");
      }

/************************************************/

      report.write("\\pard\\plain\n");
      report.write("\\qc \\intbl\\itap2\n");

      report.write("\\b{����}\\nestcell{\\nonesttables\\par }\n");
      report.write("{������������ ������������� (�����������)}\\b0\\nestcell\\b0{\\nonesttables\\par }\n");
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
        report.write("\\par\\ql\\fs24\\tab{����������  }{\\b"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\b0\\par\n");
      }

      stmt = conn.prepareStatement("SELECT a.VidDokSredObraz,SeriaAtt,NomerAtt,NomerSertifikata,a.Tel,a.TipDokSredObraz,a.KopijaSertifikata FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
      stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {

        if(rs.getString(6) != null && rs.getString(6).equals("�"))
          copyDokSrObr = " (��������)";
        else
          copyDokSrObr = " (�����)";

        if(rs.getString(7) != null && rs.getString(7).equals("�"))
          copySert = " (��������)";
        else
          copySert = " (�����)";

        report.write("\\par\\ql\\fs24\\tab{�����������(�) ��������� ���������:}\n");
        report.write("\\fs4\\par\\par\\fs24");
        report.write("\\ql{1. ���������}\n");
        report.write("\\fs4\\par\\par\\fs24");
        report.write("\\ql{2. ����� ����������}\n");
        report.write("\\fs4\\par\\par\\fs24");
        report.write("\\ql{3. "+(rs.getString(1)).substring(0,1).toUpperCase()+(rs.getString(1)).substring(1)+": � "+rs.getString(3)+copyDokSrObr+"}\n");
        report.write("\\fs4\\par\\par\\fs24");
        if(rs.getString(4) != null && rs.getString(4).length() > 3)
          report.write("\\ql{4. ������������� ���: "+rs.getString(4)+copySert+"}\n");
        else
          report.write("\\ql{4.}\n");

        stmt = conn.prepareStatement("SELECT DiplomOtlichija,UdostoverenieLgoty FROM Abiturient WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(1) != null && rs.getString(1).length() > 3) {
             report.write("\\ql{5. ������ ���������� ���������: }\\i{"+rs.getString(1)+"}\\i0\n");
          } else {
             report.write("\\ql{5.}\n");
          }

          report.write("\\fs4\\par\\par\\fs24");

          if(rs.getString(2) != null && rs.getString(2).length() > 3) {
             report.write("\\ql{6. ��������, �������������� ������: }\\i{"+rs.getString(2)+"}\\i0\n");
          } else {
             report.write("\\ql{6.}\n");
          }
        }

      }

/************************************************/

      report.write("\\par");
      report.write("\\par\\qj\\fs24{������������(�) �� ����������� ������ ����� ���������� �� ����������� (��������/������) �� ���������� � ������������ � �������� ���������� ������ ����� ���}\n");
      report.write("\\par\\par\\par\\qc\\fs22{                          ________________________________}\n");
      report.write("\\par\\qc\\fs16\\tab\\tab{(������� ������������)}\n");
      report.write("\\par\n");
      report.write("\\par\\ql\\fs24{��������� ������}\n");
      report.write("\\par\\ql\\i\\tab{��������� �������� ��������: _______________________}\\i0\n");
      report.write("\\par\\ql\\i\\tab{"+StringUtil.CurrDate(".")+"�.}\\i0\n");
      report.write("\\pard\\plain\\ql\\intbl\\cell\\pard\\plain\\ql\\intbl\n");
      report.write("\\trowd\\trqc\n");
      report.write("\\clvertalt\\cltxlrtb \\cellx7011\n");
      report.write("\\clvertalt\\clbrdrr\\brdrdash\\brdrw20 \\cltxlrtb \\cellx7364\n");
      report.write("\\clvertalt\\clbrdrl\\brdrdash\\brdrw20\\cltxlrtb\\cellx7717\n");
      report.write("\\clvertalt\\cltxlrtb\\cellx14730\\row }\\pard\\ql\\itap0\n");
      report.write("}\n");
      report.close();

/************************************************/
/**** ���������� ������ ��� ������ �� ����� *****/
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
 /******             ����� 3               *******/
 /************************************************/
 /************************************************/

       String file_con = new String("agreement"+abit_A.getKodAbiturienta()+".rtf");
  
       String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH_ABT+"\\"+file_con;

       abit_A.setFileName3(file_con);

       BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

 /************************************************/

       report.write("{\\rtf1\\ansi{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;\\ctextone\\ctint255\\cshade255\\red0\\green0\\blue0;}\n");
       report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");

    // ��������� 29 ������� - 2 ����� 2016 �.
    //   report.write("\\par\\par\n");
       report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab\\tab{������������ ������� �������� ���, ������� �.�. ��������}\n");        

    // ��������� 29 ������� - 2 ����� 2016 �.
       String tip_Dok2 = new String();
       boolean oblastIsEmpty2 = false;
       stmt = conn.prepareStatement("SELECT kodOblastiP FROM Abiturient where kodAbiturienta = ?");
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()){
      	 if(rs.getString(1).equals("-")) oblastIsEmpty2 = true;
       }
       
    // ��������� 29 ������� - 2 ����� 2016 �.
       if (oblastIsEmpty2){
       stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,a.gorod_prop,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a WHERE KodAbiturienta LIKE ?");
 	      	 
       }
       else{
       stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,Grajdanstvo,DataRojdenija,k.name,Ulica_Prop,Dom_Prop,Kvart_Prop,TipDokumenta,SeriaDokumenta,NomerDokumenta,KemVydDokument,DataVydDokumenta,Tel,MestoRojdenija FROM Abiturient a, kladr k WHERE KodAbiturienta LIKE ? and k.code = a.gorod_prop");
       }
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) {

       report.write("\\par\\qc\\fs22\\tab\\tab\\tab{  ����������� }\\i{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"}\\i0,\n");
       }
    // ��������� 29 ������� - 2 ����� 2016 �.
       report.write("\\par\\qc\\fs22\\tab\\tab\\tab\\tab\\tab{��������� ��������� �� ������� � �������� �� ��������}\n");  
       
       report.write("\\par\\qc\\fs22\\tab{         �� ��������������� ���������  }\n");
       report.write("\\par\\par\n");
       

       // ��������� 29 ������� - 2 ����� 2016 �.
       stmt = conn.prepareStatement("SELECT s.ShifrSpetsialnosti, s.NazvanieSpetsialnosti, e.name, ko.Forma_ob from konkurs ko, spetsialnosti s, edulevel e where ko.kodabiturienta like ? and ko.kodspetsialnosti=s.kodspetsialnosti and e.socr = s.edulevel");
       
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) {

         report.write("\\tab\\tab\\tab\\tab\\tab\\fs22\\ql{ }\\i{"+rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+"}\\i0\n");
         report.write("\\qc\\fs6\\par\\par\\fs22");
       }
    // ��������� 29 ������� - 2 ����� 2016 �.
       stmt = conn.prepareStatement("select nomerlichnogodela from abiturient where kodabiturienta like ?");
       
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) {

         report.write("\\tab\\fs22\\ql{������ ���� � }\\i{"+rs.getString(1)+"}\\i0\n");
         report.write("\\qc\\fs6\\par\\par\\fs22");
         report.write("\\par\\par\n");
         report.write("\\par\\par\n");
         
       }
    // ��������� 29 ������� - 2 ����� 2016 �.           
       report.write("\\qc\\fs28\\b{���������}\\b0\n");
       report.write("\\par\\par\n");
       report.write("\\par\\par\\ql\\fs22\\tab{����� ������ �� �������� � ���������� �� �������� �� ���������}\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(���, ��, ����� ��������)}\\b0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       
       report.write("\\par\\par\\ql\\fs22\\tab{��� ����� �� �������� �� ����� �� ��������� � ������� ��������� ��������:}\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\tab{- �� ������ ��������.}\n");
      
       report.write("\\par\\par\\ql\\fs22\\tab{� ������� ��������� ��������}\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(�������� ��������� �� �����������, �����, ���. �)}\\b0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab{�����______________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(�������� ��������������� �����������, �������� ��������, ���� ������:)}\\b0\n");	
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
       report.write("\\qc\\fs16\\b{(��������, �����, ���������� ����������� / ������� ��������� (�������� ����������))}\\b0\n");
       report.write("\\par\\par\\ql\\fs22\\tab\\i{___________________________________________________________________________________________}\\i0\n");
    
       report.write("\\par\\par\n");
       report.write("\\par\\par\n");
       report.write("\\par\\par\n");
       report.write("\\par\\par\\ql\\fs22\\tab{(�.�.�.)                                                                                                             }\\i{(�������)}\\i0\n");
       report.write("\\par\\par\\ql\\fs22\\tab{''___''________________ 2016 �.}\n");
       
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