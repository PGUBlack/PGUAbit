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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;


public class ORAction extends Action{

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
        int                  summa              = 0;
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
        String KF=new String();
        String range=new String();
        String b=new String();
        String fo=new String();
        float v1=0;
        int c1=0;
        float v2=0;
        int c2=0;
        float v3=0;
        int c3=0;
        float v4=0;
        int c4=0;
        float v5=0;
        int c5=0;
        float v6=0;
        int c6=0;
        float v7=0;
        int c7=0;
        float sc=0;

        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "orAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "orForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
          
          
      if ( form.getAction() == null ) {

           form.setAction(us.getClientIntName("view","init"));

      } else if ( form.getAction().equals("report")) {
    	  stmt10 = conn.prepareStatement("DELETE FROM sonicfox");
    	  stmt10.executeUpdate();
    	  
    	  
    	/****************************************************************/
    /*******************создание xml документа*************************/
    			
    	  int lineIndex = 1;
    	  		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    			// root element
    			Document doc = docBuilder.newDocument();
    			Element rootElement = doc.createElement("root");
    			Attr attr = doc.createAttribute("id");
    			attr.setValue("583");
    			rootElement.setAttributeNode(attr);
    			doc.appendChild(rootElement);
    			
    			/*Element lines = doc.createElement("lines");
    			Attr attrLid = doc.createAttribute("id");
    			attrLid.setValue(""+lineIndex);
    			lineIndex++;
    			lines.setAttributeNode(attrLid);
    			Element oo = doc.createElement("oo");
    			oo.setAttributeNode(attr);
    			Element spec = doc.createElement("spec");
    			Element foTag = doc.createElement("fo");
    			Element ff = doc.createElement("ff");*/
    			
    		//	Element p1_1 = doc.createElement("p1_1");
    		//	Element p1_2 = doc.createElement("p1_2");
    		//	Element p1_3 = doc.createElement("p1_3");
    			//Element p2_13 = doc.createElement("p2_13");
    		//	Element p2_14 = doc.createElement("p2_14");
    		//	Element p2_15 = doc.createElement("p2_15");
    		//	Element p2_16 = doc.createElement("p2_16");
    		//	Element p2_12 = doc.createElement("p2_12");
    		//	Element p2_11 = doc.createElement("p2_11");
    		//	Element p2_17 = doc.createElement("p2_17");
    		//	
    			
    			
    			
    			
    			
    		//	rootElement.appendChild(lines);

    		
    	  
    	  
    	  
    	  

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/


  String name = "Средний балл (rtf)";
  String name_xml = "Средний балл (xml)";

  String file_con = "lists_srball";
  if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
	  file_con = "lists_srball_budg";
      range="('1','2','3')";
      fo="1";
      b="о";//ф1
  }else{
	  file_con = "lists_srball_dog";
	  range="('д')";
	  fo="4";
	  b="з";//ф3
  }
  stmt11=conn.prepareStatement("update abitdopinf set ballzgto=0 where ballzgto in ('да','нет') and kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','д'))");
  stmt11.executeUpdate();
  stmt11=conn.prepareStatement("update abitdopinf set ballsgto=0 where ballsgto in ('да','нет') and kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','д'))");
  stmt11.executeUpdate();
  stmt11=conn.prepareStatement("update abitdopinf set ballatt=0 where ballatt in ('да','нет') and kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','д'))");
  stmt11.executeUpdate();
  stmt11=conn.prepareStatement("update abitdopinf set ballsoch=0 where ballsoch in ('да','нет') and kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','д'))");
  stmt11.executeUpdate();
  stmt11=conn.prepareStatement("update abitdopinf set ballpoi=0 where ballpoi in ('да','нет') and kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','д'))");
  stmt11.executeUpdate();
  stmt11=conn.prepareStatement("update abitdopinf set trudovajadejatelnost=0 where trudovajadejatelnost in ('да','нет') and kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','д'))");
  stmt11.executeUpdate();
  
  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));
  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
  
  
  if (abit_SD.getSpecial6().equals("xml")){
  
	  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name_xml,file_con,"xml"));
	
	 
  }
  session.setAttribute("rpt_xml",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name_xml,file_con,"xml"));
  String xml_file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt_xml")).getFileName();
  

 
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

       query1 = new StringBuffer("select s.nazvaniespetsialnosti, s.ShifrSpetsialnosti,s.tip_spec,s.KodSpetsialnosti, a.kodabiturienta, z1.otsenkaege,z2.otsenkaege,z3.otsenkaege, k.prof, k.stob, k.pr1,k.pr2,k.pr3, k.target,k.op, (cast(adi.ballsoch as int)+adi.ballatt+adi.ballpoi+adi.trudovajadejatelnost+adi.ballsgto+adi.ballzgto+k.olimp), s.gzgu from abiturient a, spetsialnosti s, konkurs k, abitdopinf adi, ekzamenynaspetsialnosti e1, ekzamenynaspetsialnosti e2, ekzamenynaspetsialnosti e3, zajavlennyeshkolnyeotsenki z1, zajavlennyeshkolnyeotsenki z2, zajavlennyeshkolnyeotsenki z3 where a.prinjat in "+range+" and a.kodabiturienta=k.kodabiturienta and a.kodabiturienta=adi.kodabiturienta and a.kodabiturienta=z1.kodabiturienta and a.kodabiturienta=z2.kodabiturienta and a.kodabiturienta=z3.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.kodspetsialnzach=s.kodspetsialnosti and a.kodspetsialnzach=e1.kodspetsialnosti and a.kodspetsialnzach=e2.kodspetsialnosti and a.kodspetsialnzach=e3.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and e1.prioritet=1 and e1.kodpredmeta=z1.kodpredmeta and e2.prioritet=2 and e2.kodpredmeta=z2.kodpredmeta and e3.prioritet=3 and e3.kodpredmeta=z3.kodpredmeta and z1.examen not like 'в'  and z2.examen not like 'в'  and z3.examen not like 'в' ");
    if(abit_SD.getPriznakSortirovki().equals("budgetniki")){

    query1.append("AND (k.Bud LIKE 'д') order by kodspetsialnosti ");

    }else{
      query1.append("AND (k.Dog LIKE 'д') order by kodspetsialnosti ");
    }
    
    stmt9 = conn.createStatement();

    rs9 = stmt9.executeQuery(query1.toString());
    while(rs9.next()) {
    	stmt10 = conn.prepareStatement("INSERT INTO sonicfox(Shifr,F,I,N,ka,pr1,pr2,pr3,pr4,Stob,p1,p2,p3,op,pp,ind, gzgu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
   // 	stmt10=conn.prepareStatement("INSERT INTO sonicfox(Shifr,F,I) VALUES(?,?,?)");
    	stmt10.setObject(1, rs9.getString(1),Types.VARCHAR);
        stmt10.setObject(2, rs9.getString(2),Types.VARCHAR);
        stmt10.setObject(3, rs9.getString(3),Types.VARCHAR);
        stmt10.setObject(4, rs9.getInt(4),Types.INTEGER);
        stmt10.setObject(5, rs9.getInt(5),Types.INTEGER);
        stmt10.setObject(6, rs9.getInt(6),Types.INTEGER);
        stmt10.setObject(7, rs9.getInt(7),Types.INTEGER);
        stmt10.setObject(8, rs9.getInt(8),Types.INTEGER);
        stmt10.setObject(9, rs9.getInt(9),Types.INTEGER);
        stmt10.setObject(10, rs9.getInt(10),Types.INTEGER);
        stmt10.setObject(11, rs9.getInt(11),Types.INTEGER);
        stmt10.setObject(12, rs9.getInt(12),Types.INTEGER);
        stmt10.setObject(13, rs9.getInt(13),Types.INTEGER);
        stmt10.setObject(14, rs9.getInt(15),Types.INTEGER);
        stmt10.setObject(15, rs9.getInt(14),Types.INTEGER);
        stmt10.setObject(16, rs9.getInt(16),Types.INTEGER);
        stmt10.setObject(17, rs9.getInt(17),Types.VARCHAR);
        
        stmt10.executeUpdate();
    }

        report.write("\\pard\\par\n");
        if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
        	report.write("\\b1\\ql\\fs28{Бюджет}\\b0\\par\\par\n"); 
        }else{
        	report.write("\\b1\\ql\\fs28{Договор}\\b0\\par\\par\n");
        }
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3000\n");//shifr
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");//fio
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");//ko
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5500\n");//sum
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11500\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13000\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14500\n");//pp

        
        report.write("\\intbl\\qc{Назв.Спец.}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Форма}\\cell\n");
        report.write("\\intbl{Тип}\\cell\n");
        report.write("\\intbl{Ср.балл Без проф.,100}\\cell\n");
        report.write("\\intbl{Ср.балл со 100}\\par{за}\\par{дост.}\\cell\n");
        report.write("\\intbl{Ср.балл с проф.}\\cell\n");
        report.write("\\intbl{Ср.балл с проф.,100}\\cell\n");
        report.write("\\intbl{Ср.балл цел.}\\cell\n");
        report.write("\\intbl{Ср.балл инв.}\\cell\n");
        report.write("\\intbl{Прох.балл с инд.}\\cell\n");


        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3000\n");//shifr
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");//fio
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");//ko
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5500\n");//sum
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11500\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13000\n");//pp
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14500\n");//pp
        report.write("\\intbl\\row\n");
        
        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
     //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3000\n");//shifr
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");//fio
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");//ko
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5500\n");//sum
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");//pp
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");//pp 
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");//pp 
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11500\n");//pp 
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13000\n");//pp 
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14500\n");//pp 
      


      stmt10 = conn.prepareStatement("SELECT DISTINCT N, Shifr,F, gzgu,I FROM sonicfox where N not in ('81','82','85','86','87','90','91','107','108','109','110','111','117','118','119','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334','67','103') ORDER BY N ASC");
      rs10=stmt10.executeQuery();
      
      while(rs10.next()){
    	  
    	  
    	  Element lines = doc.createElement("lines");
			Attr attrLid = doc.createAttribute("id");
			attrLid.setValue(""+lineIndex);
			lineIndex++;
			lines.setAttributeNode(attrLid);
			Element oo = doc.createElement("oo");
			oo.appendChild(doc.createTextNode("583"));
			lines.appendChild(oo);
			
			
			Element spec = doc.createElement("spec");
			spec.appendChild(doc.createTextNode(rs10.getString(4)));
			lines.appendChild(spec);
			
			
			
			Element foTag = doc.createElement("fo");
			foTag.appendChild(doc.createTextNode(""+b));
			lines.appendChild(foTag);
			
						
			Element ff = doc.createElement("ff");
			ff.appendChild(doc.createTextNode(""+fo));
			lines.appendChild(ff);
			//Element p1_1 = doc.createElement("p1_1");
		//	lines.appendChild(p1_1);
			
			
    	  
    	  
    	  report.write("\\intbl\\qc "+rs10.getString(2)+"\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc "+rs10.getString(3)+"\\cell\n"); // NLD 
    	  /*if(rs10.getString(4).equals("о")){
    	  report.write("\\intbl\\qc ф1\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc 1\\cell\n"); // NLD 
      }else{
    	  report.write("\\intbl\\qc ф3\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc 4\\cell\n"); // NLD 
      }*/
    	  report.write("\\intbl\\qc "+rs10.getString(5)+"\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc "+fo+"\\cell\n"); // NLD 
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();
    	  Element p2_13 = doc.createElement("p2_13");
    	  if(rs11.next() && rs11.getString(1)!=null){
    		
    		  p2_13.appendChild(doc.createTextNode(rs11.getString(1)));
    		 
    		 
    		  
    		  
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v1=v1+rs11.getFloat(1)*rs11.getInt(2);
    	  c1=c1+rs11.getInt(2);
    	  }else{
    		  p2_13.appendChild(doc.createTextNode("0"));
    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }
    	  lines.appendChild(p2_13);
    	  
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p2=1 THEN 100 ELSE pr2 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();
    	  Element p2_14 = doc.createElement("p2_14");
    	  if(rs11.next() && rs11.getString(1)!=null){
    		  
    		  
      		
    	  p2_14.appendChild(doc.createTextNode(rs11.getString(1)));
    		 
    		
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v2=v2+rs11.getFloat(1)*rs11.getInt(2);
    	  c2=c2+rs11.getInt(2);
    	  }else{
    		  p2_14.appendChild(doc.createTextNode("0"));
    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }
    	  lines.appendChild(p2_14);
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();
    	  Element p2_15 = doc.createElement("p2_15");
    	  if(rs11.next() && rs11.getString(1)!=null){
    		 
      		
    		  p2_15.appendChild(doc.createTextNode(rs11.getString(1)));
    		 
    	
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v3=v3+rs11.getFloat(1)*rs11.getInt(2);
    	  c3=c3+rs11.getInt(2);
    	  }else{
    		  p2_15.appendChild(doc.createTextNode("0"));
    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }
    	  lines.appendChild(p2_15);
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p2=1 THEN 100 ELSE pr2 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();
    	  Element p2_16 = doc.createElement("p2_16");
    	  if(rs11.next() && rs11.getString(1)!=null){
    	
    		  
    		  p2_16.appendChild(doc.createTextNode(rs11.getString(1)));
    		  
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v4=v4+rs11.getFloat(1)*rs11.getInt(2);
    	  c4=c4+rs11.getInt(2);
    	  }else{
    		  p2_16.appendChild(doc.createTextNode("0"));
    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }
    	  lines.appendChild(p2_16);
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp not like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();
    	  Element p2_12 = doc.createElement("p2_12");
    	  if(rs11.next() && rs11.getString(1)!=null){
    		  
    		 
    		  p2_12.appendChild(doc.createTextNode(rs11.getString(1)));
    		
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v5=v5+rs11.getFloat(1)*rs11.getInt(2);
    	  c5=c5+rs11.getInt(2);
    	  }else{
    		  p2_12.appendChild(doc.createTextNode("0"));
    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }
    	  lines.appendChild(p2_12);
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op not like '1' ");
    	  rs11=stmt11.executeQuery();
    	  Element p2_11 = doc.createElement("p2_11");
    	  if(rs11.next() && rs11.getString(1)!=null){
    		  
    		 
    		  p2_11.appendChild(doc.createTextNode(rs11.getString(1)));
    		 
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v6=v6+rs11.getFloat(1)*rs11.getInt(2);
    	  c6=c6+rs11.getInt(2);
    	  }else{
    		  p2_11.appendChild(doc.createTextNode("0"));
    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }
    	  lines.appendChild(p2_11);
    	  
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(MIN(((pr1+pr2+pr3)/3)+ind),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' ");
    	  rs11=stmt11.executeQuery();
    	  Element p2_17 = doc.createElement("p2_17");
    	  if(rs11.next() && rs11.getString(1)!=null){
    		 
    		  p2_17.appendChild(doc.createTextNode(rs11.getString(1)));
    		
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD 
    	  v7=v7+rs11.getFloat(1)*rs11.getInt(2);
    	  c7=c7+rs11.getInt(2);
    	  }else{
    		  p2_17.appendChild(doc.createTextNode("0"));
    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }
    	  lines.appendChild(p2_17);
		  rootElement.appendChild(lines);//TO_DO  
    	  
    	  
report.write("\\intbl\\row\n");

 
      }
      
  
    	  report.write("\\intbl\\qc Пед.Образование\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc 44.03.01\\cell\n"); // NLD 
    	  /*if(rs10.getString(4).equals("о")){
    	  report.write("\\intbl\\qc ф1\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc 1\\cell\n"); // NLD 
      }else{
    	  report.write("\\intbl\\qc ф3\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc 4\\cell\n"); // NLD 
      }*/
    	  report.write("\\intbl\\qc "+b+"\\cell\n"); // NLD 
    	  report.write("\\intbl\\qc "+fo+"\\cell\n"); // NLD 
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('82','85','86','87','107','108','117','118','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334') and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();

    	  if(rs11.next() && rs11.getString(1)!=null){
    		
    
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v1=v1+rs11.getFloat(1)*rs11.getInt(2);
    	  c1=c1+rs11.getInt(2);
    	  }else{

    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }

    	  
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p2=1 THEN 100 ELSE pr2 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/3),2),count(ka) from sonicfox where N in ('82','85','86','87','107','108','117','118','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334') and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();

    	  if(rs11.next() && rs11.getString(1)!=null){
    		  
    		  
      		

    		 
    		
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v2=v2+rs11.getFloat(1)*rs11.getInt(2);
    	  c2=c2+rs11.getInt(2);
    	  }else{

    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }

    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('82','85','86','87','107','108','117','118','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334') and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();

    	  if(rs11.next() && rs11.getString(1)!=null){
    		 
      		

    		 
    	
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v3=v3+rs11.getFloat(1)*rs11.getInt(2);
    	  c3=c3+rs11.getInt(2);
    	  }else{

    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }

    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p2=1 THEN 100 ELSE pr2 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/3),2),count(ka) from sonicfox where N in ('82','85','86','87','107','108','117','118','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334') and pp like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();
    	  if(rs11.next() && rs11.getString(1)!=null){
    	
    		  

    		  
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v4=v4+rs11.getFloat(1)*rs11.getInt(2);
    	  c4=c4+rs11.getInt(2);
    	  }else{

    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }

    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('82','85','86','87','107','108','117','118','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334') and pp not like '1' and op like '1' ");
    	  rs11=stmt11.executeQuery();

    	  if(rs11.next() && rs11.getString(1)!=null){
    		  
    		 

    		
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v5=v5+rs11.getFloat(1)*rs11.getInt(2);
    	  c5=c5+rs11.getInt(2);
    	  }else{

    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }

    	  
    	  stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('82','85','86','87','107','108','117','118','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334') and pp like '1' and op not like '1' ");
    	  rs11=stmt11.executeQuery();
  
    	  if(rs11.next() && rs11.getString(1)!=null){
    		  
    		 

    		 
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
    	  v6=v6+rs11.getFloat(1)*rs11.getInt(2);
    	  c6=c6+rs11.getInt(2);
    	  }else{

    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }

    	  
    	  
    	  stmt11=conn.prepareStatement("Select ROUND(MIN(((pr1+pr2+pr3)/3)+ind),2),count(ka) from sonicfox where N in ('82','85','86','87','107','108','117','118','124','125','126','127','128','129','130','131','132','133','134','135','136','137','334') ");
    	  rs11=stmt11.executeQuery();

    	  if(rs11.next() && rs11.getString(1)!=null){
    		 

    		
    		  
    	  report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD 
    	  v7=v7+rs11.getFloat(1)*rs11.getInt(2);
    	  c7=c7+rs11.getInt(2);
    	  }else{

    		  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
    	  }


    	  
    	  
report.write("\\intbl\\row\n");

 
report.write("\\intbl\\qc Пед.Образование\\cell\n"); // NLD 
report.write("\\intbl\\qc 44.03.05\\cell\n"); // NLD 
/*if(rs10.getString(4).equals("о")){
report.write("\\intbl\\qc ф1\\cell\n"); // NLD 
report.write("\\intbl\\qc 1\\cell\n"); // NLD 
}else{
report.write("\\intbl\\qc ф3\\cell\n"); // NLD 
report.write("\\intbl\\qc 4\\cell\n"); // NLD 
}*/
report.write("\\intbl\\qc "+b+"\\cell\n"); // NLD 
report.write("\\intbl\\qc "+fo+"\\cell\n"); // NLD 

stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('90','91','109','110','111','119') and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	

report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v1=v1+rs11.getFloat(1)*rs11.getInt(2);
c1=c1+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}



stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p2=1 THEN 100 ELSE pr2 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/3),2),count(ka) from sonicfox where N in ('90','91','109','110','111','119') and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	  
	  
	

	 
	
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v2=v2+rs11.getFloat(1)*rs11.getInt(2);
c2=c2+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('90','91','109','110','111','119') and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	 
	

	 

	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v3=v3+rs11.getFloat(1)*rs11.getInt(2);
c3=c3+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p2=1 THEN 100 ELSE pr2 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/3),2),count(ka) from sonicfox where N in ('90','91','109','110','111','119') and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();
if(rs11.next() && rs11.getString(1)!=null){

	  

	  
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v4=v4+rs11.getFloat(1)*rs11.getInt(2);
c4=c4+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('90','91','109','110','111','119') and pp not like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	  
	 

	
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v5=v5+rs11.getFloat(1)*rs11.getInt(2);
c5=c5+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr2+pr3)/3),2),count(ka) from sonicfox where N in ('90','91','109','110','111','119') and pp like '1' and op not like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	  
	 

	 
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v6=v6+rs11.getFloat(1)*rs11.getInt(2);
c6=c6+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}



stmt11=conn.prepareStatement("Select ROUND(MIN(((pr1+pr2+pr3)/3)+ind),2),count(ka) from sonicfox where N in ('90','91','109','110','111','119') ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	 

	
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD 
v7=v7+rs11.getFloat(1)*rs11.getInt(2);
c7=c7+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}

report.write("\\intbl\\row\n");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

stmt10 = conn.prepareStatement("SELECT DISTINCT N, Shifr,F, gzgu,I FROM sonicfox where N in ('67','81','103') ORDER BY N ASC");
rs10=stmt10.executeQuery();

while(rs10.next()){

      
report.write("\\intbl\\qc "+rs10.getString(2)+"\\cell\n"); // NLD 
report.write("\\intbl\\qc "+rs10.getString(3)+"\\cell\n"); // NLD 
/*if(rs10.getString(4).equals("о")){
report.write("\\intbl\\qc ф1\\cell\n"); // NLD 
report.write("\\intbl\\qc 1\\cell\n"); // NLD 
}else{
report.write("\\intbl\\qc ф3\\cell\n"); // NLD 
report.write("\\intbl\\qc 4\\cell\n"); // NLD 
}*/
report.write("\\intbl\\qc "+rs10.getString(5)+"\\cell\n"); // NLD 
report.write("\\intbl\\qc "+fo+"\\cell\n"); // NLD 

stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr3)/2),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	

report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v1=v1+rs11.getFloat(1)*rs11.getInt(2);
c1=c1+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}



stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/2),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	  
	  
	

	 
	
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v2=v2+rs11.getFloat(1)*rs11.getInt(2);
c2=c2+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr3+pr4)/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	 
	

	 

	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v3=v3+rs11.getFloat(1)*rs11.getInt(2);
c3=c3+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END)+pr4)/3),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op like '1' ");
rs11=stmt11.executeQuery();
if(rs11.next() && rs11.getString(1)!=null){

	  

	  
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v4=v4+rs11.getFloat(1)*rs11.getInt(2);
c4=c4+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr4)/2),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp not like '1' and op like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	  
	 

	
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v5=v5+rs11.getFloat(1)*rs11.getInt(2);
c5=c5+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}


stmt11=conn.prepareStatement("Select ROUND(AVG((pr1+pr3)/2),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' and pp like '1' and op not like '1' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	  
	 

	 
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD  
v6=v6+rs11.getFloat(1)*rs11.getInt(2);
c6=c6+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}



stmt11=conn.prepareStatement("Select ROUND(MIN(((pr1+pr3)/2)+ind),2),count(ka) from sonicfox where N like '"+rs10.getInt(1)+"' ");
rs11=stmt11.executeQuery();

if(rs11.next() && rs11.getString(1)!=null){
	 

	
	  
report.write("\\intbl\\qc "+rs11.getString(1)+"\\cell\n"); // NLD 
v7=v7+rs11.getFloat(1)*rs11.getInt(2);
c7=c7+rs11.getInt(2);
}else{

	  report.write("\\intbl\\qc 0\\cell\n"); // NLD    
}




report.write("\\intbl\\row\n");
}
      
      

      
      
      stmt11=conn.prepareStatement("Select count(ka) from sonicfox");
	  rs11=stmt11.executeQuery();
      report.write("\\pard\\par\n");
      report.write("\\fs40 \\tab\\qc {Ср.баллы}\n \\line");
      rs11.next();
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов всего: "+rs11.getString(1)+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Без Проф. и 100: "+v1/c1+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов: "+c1+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {С 100, без Проф.: "+v2/c2+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов: "+c2+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {С проф, без 100: "+v3/c3+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов: "+c3+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {С проф и 100: "+v4/c4+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов: "+c4+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Цел.: "+v5/c5+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов "+c5+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Инв, сироты: "+v6/c6+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов: "+c6+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Прох. балл с инд. дост: "+v7/c7+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов: "+c7+"}\n \\line");
      stmt11=conn.prepareStatement("Select ROUND(AVG(((CASE WHEN p1=1 THEN 100 ELSE pr1 END)+(CASE WHEN p2=1 THEN 100 ELSE pr2 END)+(CASE WHEN p3=1 THEN 100 ELSE pr3 END))/3),2),count(ka) from sonicfox");
	  rs11=stmt11.executeQuery();
	  rs11.next();
	  report.write("\\fs24 \\tab\\ql {Ср. балл с уч. цел., сирот и 100: "+rs11.getString(1)+"}\n \\line");
      report.write("\\fs24 \\tab\\ql {Кол-во абитуриентов: "+rs11.getString(2)+"}\n \\line");
  /*    report.write("\\par\\fs24\\ql\\tab\\tab{Ср.баллы}\\par\n");
      report.write("\\par\\fs24\\ql\\tab\\tab{"+v1/c1+"}\\par\n");
      report.write("\\par\\fs24\\ql\\tab\\tab{"+v2/c2+"}\\par\n");
      report.write("\\par\\fs24\\ql\\tab\\tab{"+v3/c3+"}\\par\n");
      report.write("\\par\\fs24\\ql\\tab\\tab{"+v4/c4+"}\\par\n");
      report.write("\\par\\fs24\\ql\\tab\\tab{"+v5/c5+"}\\par\n");
      report.write("\\par\\fs24\\ql\\tab\\tab{"+v6/c6+"}\\par\n");
      report.write("\\par\\fs24\\ql\\tab\\tab{"+v7/c7+"}\\par\n");
      
    */  
      
          if(header) report.write("\\pard\\par\n");
  stmt10 = conn.prepareStatement("DELETE FROM sonicfox WHERE 1=1");
  stmt10.executeUpdate();

  report.write("}"); 
  report.close();
  
  
  TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(new File(xml_file_name));

	// Output to console for testing
	// StreamResult result = new StreamResult(System.out);

	transformer.transform(source, result);

	System.out.println("File saved!");
  
  
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
