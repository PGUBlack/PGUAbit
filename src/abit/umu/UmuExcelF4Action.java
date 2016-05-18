package abit.umu;
import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.ArrayList;

import org.apache.struts.action.*;

import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

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















import javax.naming.*;
import javax.sql.*;

import abit.bean.*;
import abit.Constants;
import abit.util.*;

import java.util.Date;
import java.io.*;

import abit.sql.*; 

public class UmuExcelF4Action extends Action {

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
        
       
        ResultSet               rs                    = null;
        
        Statement stmt9=null;
        PreparedStatement stmt10=null;
        PreparedStatement stmt11=null;
        ResultSet rs9=null;
        ResultSet rs10=null;
        ResultSet rs11=null;
        
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
      
     
        boolean              lists_dec_ege_f    = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList<Integer>			 specs              = new ArrayList();
        Integer kodSpec = null;
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
        AbiturientBean abit_A = new AbiturientBean();
        
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
        //  request.setAttribute( "waveFirstForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
        //создаем excel workbook
       //   Workbook workbook = new Workbook("D:\\output.xls");
          

          //добавляем новый лист Excel и получаем к нему доступ
         // int sheetIndex = workbook.getWorksheets().add();
       //   Worksheet worksheet = workbook.getWorksheets().get(0);
          
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
			
          
          
          
          
          
          
          
          Workbook workbook = new Workbook();

          //добавляем новый лист Excel и получаем к нему доступ
        //  int sheetIndex = workbook.getWorksheets().add();
          Worksheet worksheet = workbook.getWorksheets().get(0);
          
          Cells cells = worksheet.getCells();
        
       //   cells.setRowHeight(5, 100);
        //  cells.setColumnWidth(0, 40);

          Cell cell = cells.get("A2");
          cell.setValue("раздел 2.3. Сведения об учете индивидуальных достижений");
          cell = cells.get("a4");
          cell.setValue("код спец.");
          
          cell = cells.get("b4");
          cell.setValue("наименование");
          
          cell = cells.get("c4");
          cell.setValue("шифр");
          

          cell = cells.get("d4");
          cell.setValue("форма об.");
          

          cell = cells.get("e4");
          cell.setValue("Основа об.");
          

          cell = cells.get("f4");
          cell.setValue("чемпионы");
          

          cell = cells.get("g4");
          cell.setValue("аттестат с отличием");
          

          cell = cells.get("h4");
          cell.setValue("волонтеры");
          

          cell = cells.get("i4");
          cell.setValue("олимпиадники");
          

          cell = cells.get("j4");
          cell.setValue("сочинения");
          

          cell = cells.get("k4");
          cell.setValue("чемпионы");
          

          cell = cells.get("l4");
          cell.setValue("аттестат с отличием");
          

          cell = cells.get("m4");
          cell.setValue("волонтеры");
          

          cell = cells.get("n4");
          cell.setValue("олимпиадники");
          

          cell = cells.get("o4");
          cell.setValue("сочинения");
          

          cell = cells.get("p4");
          cell.setValue("чемпионы");
          
        
          

          cell = cells.get("q4");
          cell.setValue("аттестат с отличием");
          

          cell = cells.get("r4");
          cell.setValue("волонтеры");
          

          cell = cells.get("s4");
          cell.setValue("олимпиадники");
          

          cell = cells.get("t4");
          cell.setValue("сочинения");
          
          
          /*********************p пункты*******************************/
          
          cell = cells.get("a7");
          cell.setValue("код спец");
          
          cell = cells.get("b7");
          cell.setValue("p1.1");
          
          cell = cells.get("c7");
          cell.setValue("p1.1");
          

          cell = cells.get("d7");
          cell.setValue("p1.2");
          

          cell = cells.get("e7");
          cell.setValue("p1.3");
          

          cell = cells.get("f7");
          cell.setValue("p2.18");
          
          cell = cells.get("f6");
          cell.setValue("Кол-во баллов");
          

          cell = cells.get("g7");
          cell.setValue("p2.19");
          

          cell = cells.get("h7");
          cell.setValue("p2.20");
          

          cell = cells.get("i7");
          cell.setValue("p2.21");
          

          cell = cells.get("j7");
          cell.setValue("p2.22");
          

          cell = cells.get("k7");
          cell.setValue("p2.23");
          
          cell = cells.get("k6");
          cell.setValue("средний балл");
          

          cell = cells.get("l7");
          cell.setValue("p2.24");
          

          cell = cells.get("m7");
          cell.setValue("p2.25");
          

          cell = cells.get("n7");
          cell.setValue("p2.26");
          

          cell = cells.get("o7");
          cell.setValue("p2.27");
          

          cell = cells.get("p7");
          cell.setValue("p2.28");
          
          cell = cells.get("p6");
          cell.setValue("Кол-во лиц");
          

          cell = cells.get("q7");
          cell.setValue("p2.29");
          

          cell = cells.get("r7");
          cell.setValue("p2.30");
          

          cell = cells.get("s7");
          cell.setValue("p2.31");
          

          cell = cells.get("t7");
          cell.setValue("p2.32");
          
          
          
          /***************бюджет****************/
   
         //бюджет
         pstmt = conn.prepareStatement("Select Distinct k.kodspetsialnosti  from konkurs k , spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.eduLevel in ('б','с') and k.zach = 'д'");
         rs = pstmt.executeQuery();
         while(rs.next()){
        	 specs.add(new Integer(rs.getInt(1)));
         }
        Iterator itr = specs.iterator();
        int rowIndex = 9;
        while(itr.hasNext()) {
           kodSpec = (Integer) itr.next();
           
       	   
         	Element lines = doc.createElement("lines");
 			Attr attrLid = doc.createAttribute("id");
 			attrLid.setValue(""+lineIndex);
 			lineIndex++;
 			lines.setAttributeNode(attrLid);
           
           pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti, Tip_spec, gzgu from spetsialnosti where kodspetsialnosti = ?");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "a"+rowIndex;
        	  cell = cells.get(rowN);
        	   cell.setValue(rs.getString(1));
        	   rowN = "b"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(rs.getString(2));
        	   rowN = "c"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(rs.getString(3));
        	   
        	   String FO = "";
        	   if (rs.getString(4).equals("о")) FO = "ф1";
        	   if (rs.getString(4).equals("з")) FO = "ф3";
        	   if (rs.getString(4).equals("в")) FO = "ф2";
        	   
        	   rowN = "d"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(FO);
        	   
        	   rowN = "e"+rowIndex;
          	   cell = cells.get(rowN);
          	   cell.setValue("1");
        
  			Element oo = doc.createElement("oo");
  			oo.appendChild(doc.createTextNode("583"));
  			lines.appendChild(oo);
  			
  			
  			Element spec = doc.createElement("spec");
  			spec.appendChild(doc.createTextNode(rs.getString(5)));
  			lines.appendChild(spec);
  			
  			
  			
  			Element foTag = doc.createElement("fo");
  			foTag.appendChild(doc.createTextNode(FO));
  			lines.appendChild(foTag);
  			
  						
  			Element ff = doc.createElement("ff");
  			ff.appendChild(doc.createTextNode("1"));
  			lines.appendChild(ff);
        	   
        	   
        	        	 
           }
         
           
           //количество чемпионов 2.18
           pstmt = conn.prepareStatement("select sum(cast (adi.ballpoi as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballpoi>0 and adi.ballpoi not in ('да','нет') and k.kodspetsialnosti = ?");
         pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "f"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   
        	   
        	   Element p2_18 = doc.createElement("p2_18");
        	   if (rs.getString(1)!=null)
               p2_18.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_18.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_18);
        	  
           }
           
           //аттестат с отличием количество 2.19
           pstmt = conn.prepareStatement("select sum(cast (adi.ballatt as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballatt>0 and adi.ballatt not in ('да','нет') and k.kodspetsialnosti = ?");
          pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "g"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_19 = doc.createElement("p2_19");
        	   if (rs.getString(1)!=null)
               p2_19.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        	p2_19.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_19);
        	  
           }
           
           
         //волонтерская деятельность количество 2.20
           pstmt = conn.prepareStatement("select sum(cast (adi.trudovajadejatelnost as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.trudovajadejatelnost>0 and adi.trudovajadejatelnost not in ('да','нет') and k.kodspetsialnosti = ?");
    pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "h"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_20 = doc.createElement("p2_20");
        	   if (rs.getString(1)!=null)
               p2_20.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        	   p2_20.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_20);
        	  
           }
           
           
           //количество олимпиадников 2.21
           pstmt = conn.prepareStatement("select sum(cast (k.olimp as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.olimp>0 and k.olimp not in ('да','нет') and k.kodspetsialnosti = ?");
    pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "i"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_21 = doc.createElement("p2_21");
        	   if (rs.getString(1)!=null)
        	   p2_21.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_21.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_21);
        	  
           }
           
           //количество сочинения 2.22
           pstmt = conn.prepareStatement("select sum(cast (adi.ballsoch as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballsoch>0 and adi.ballsoch not in ('да','нет') and k.kodspetsialnosti = ?");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "j"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   

        	   Element p2_22 = doc.createElement("p2_22");
        	   if (rs.getString(1)!=null)
        	   p2_22.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_22.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_22);
        	  
           }
           
           
           //средний балл чемпионы 2.23
           pstmt = conn.prepareStatement("select avg(cast (adi.ballpoi as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballpoi>0 and adi.ballpoi not in ('да','нет') and k.kodspetsialnosti = ?");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   float ballSoch = rs.getFloat(1);
        	   String rowN = "k"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(ballSoch);
        	   
        	   Element p2_23 = doc.createElement("p2_23");
        	   if (rs.getString(1)!=null)
               p2_23.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_23.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_23);
        	  
        	  
           }
           
           //средний балл отличие 2.24
           pstmt = conn.prepareStatement("select avg(cast (adi.ballatt as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballatt>0 and adi.ballatt not in ('да','нет') and k.kodspetsialnosti = ?");
            pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   float ballSoch = rs.getFloat(1);
        	   String rowN = "l"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(ballSoch);
        	   
        	   Element p2_24 = doc.createElement("p2_24");
        	   if (rs.getString(1)!=null)
        	   p2_24.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_24.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_24);
        	  
           }
           
           //средний балл волонтерская 2.25
           pstmt = conn.prepareStatement("select avg(cast (adi.trudovajadejatelnost as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.trudovajadejatelnost>0 and adi.trudovajadejatelnost not in ('да','нет') and k.kodspetsialnosti = ?");
     
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   float ballSoch = rs.getFloat(1);
        	   String rowN = "m"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(ballSoch);
        	   
        	   Element p2_25 = doc.createElement("p2_25");
        	   if (rs.getString(1)!=null)
        	   p2_25.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_25.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_25);
        	  
           }
           
           
           //средний балл олимпиада 2.26
           pstmt = conn.prepareStatement("select avg(cast (k.olimp as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.olimp>0 and k.olimp not in ('да','нет') and k.kodspetsialnosti = ?");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   float ballSoch = rs.getFloat(1);
        	   String rowN = "n"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(ballSoch);
        	   
        	   Element p2_26 = doc.createElement("p2_26");
        	   if (rs.getString(1)!=null)
               p2_26.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_26.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_26);
        	  
           }
           
           //средний балл сочинения 2.27
           pstmt = conn.prepareStatement("select avg(cast (adi.ballsoch as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballsoch>0 and adi.ballsoch not in ('да','нет') and k.kodspetsialnosti = ?");
        
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   float ballSoch = rs.getFloat(1);
        	   String rowN = "o"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(ballSoch);
        	  
        	   Element p2_27 = doc.createElement("p2_27");
        	   if (rs.getString(1)!=null)
               p2_27.appendChild(doc.createTextNode(rs.getString(1)));
               else
            	   p2_27.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_27);
        	  
           }
           

           // количество лиц чемпионы 2.28
           pstmt = conn.prepareStatement("select count(cast (adi.ballpoi as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballpoi>0 and adi.ballpoi not in ('да','нет') and k.kodspetsialnosti = ?");

           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "p"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	  Element p2_28 = doc.createElement("p2_28");
        	  if (rs.getString(1)!=null)
               p2_28.appendChild(doc.createTextNode(rs.getString(1)));
        	  else
        		  p2_28.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_28);
        	  
           }
           
           // количество лиц отличие 2.29
           pstmt = conn.prepareStatement("select count(cast (adi.ballatt as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballatt>0 and adi.ballatt not in ('да','нет') and k.kodspetsialnosti = ?");

           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "q"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_29 = doc.createElement("p2_29");
        	   if (rs.getString(1)!=null)
               p2_29.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_29.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_29);
        	  
           }
           
           // количество лиц волонтеры 2.30
           pstmt = conn.prepareStatement("select count(cast (adi.trudovajadejatelnost as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.trudovajadejatelnost>0 and adi.trudovajadejatelnost not in ('да','нет') and k.kodspetsialnosti = ?");

           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "r"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	  
        	   
        	   Element p2_30 = doc.createElement("p2_30");
        	   if (rs.getString(1)!=null)
               p2_30.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_30.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_30);
        	  
           }
           
           // количество лиц олимпиада 2.31
           pstmt = conn.prepareStatement("select count(cast (k.olimp as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.olimp>0 and k.olimp not in ('да','нет') and k.kodspetsialnosti = ?");

           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "s"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_31 = doc.createElement("p2_31");
        	   if (rs.getString(1)!=null)
               p2_31.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_31.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_31);
        	  
           }
           
           // количество лиц сочинение 2.32
           pstmt = conn.prepareStatement("select count(cast (adi.ballsoch as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and adi.ballsoch>0 and adi.ballsoch not in ('да','нет') and k.kodspetsialnosti = ?");

           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "t"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_32 = doc.createElement("p2_32");
        	   if (rs.getString(1)!=null)
        	   p2_32.appendChild(doc.createTextNode(rs.getString(1)));
        	   else
        		   p2_32.appendChild(doc.createTextNode("0"));   
               lines.appendChild(p2_32);
        	  
           }
           
           
           
           
           
        
           
           rootElement.appendChild(lines);
           rowIndex++;
         }
        
        
        
        /***************************************************************
        //ДОГОВОР
         * ****************************************************************
         */
        
        //бюджет
        pstmt = conn.prepareStatement("Select Distinct k.kodspetsialnosti  from konkurs k , spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.eduLevel in ('б','с') and k.zach = 'д'");
        rs = pstmt.executeQuery();
        specs.clear();
        while(rs.next()){
       	 specs.add(new Integer(rs.getInt(1)));
        }
        itr = specs.iterator();
       
       while(itr.hasNext()) {
          kodSpec = (Integer) itr.next();
          
      	   
        	Element lines = doc.createElement("lines");
			Attr attrLid = doc.createAttribute("id");
			attrLid.setValue(""+lineIndex);
			lineIndex++;
			lines.setAttributeNode(attrLid);
          
          pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti, Tip_spec, gzgu from spetsialnosti where kodspetsialnosti = ?");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "a"+rowIndex;
       	  cell = cells.get(rowN);
       	   cell.setValue(rs.getString(1));
       	   rowN = "b"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(rs.getString(2));
       	   rowN = "c"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(rs.getString(3));
       	   
       	   String FO = "";
       	   if (rs.getString(4).equals("о")) FO = "ф1";
       	   if (rs.getString(4).equals("з")) FO = "ф3";
       	   if (rs.getString(4).equals("в")) FO = "ф2";
       	   
       	   rowN = "d"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(FO);
       	   
       	   rowN = "e"+rowIndex;
         	   cell = cells.get(rowN);
         	   cell.setValue("4");
       
 			Element oo = doc.createElement("oo");
 			oo.appendChild(doc.createTextNode("583"));
 			lines.appendChild(oo);
 			
 			
 			Element spec = doc.createElement("spec");
 			spec.appendChild(doc.createTextNode(rs.getString(5)));
 			lines.appendChild(spec);
 			
 			
 			
 			Element foTag = doc.createElement("fo");
 			foTag.appendChild(doc.createTextNode(FO));
 			lines.appendChild(foTag);
 			
 						
 			Element ff = doc.createElement("ff");
 			ff.appendChild(doc.createTextNode("1"));
 			lines.appendChild(ff);
       	   
       	   
       	        	 
          }
        
          
          //количество чемпионов 2.18
          pstmt = conn.prepareStatement("select sum(cast (adi.ballpoi as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.ballpoi>0 and adi.ballpoi not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat ='д'");
        pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   
       	   
       	   Element p2_18 = doc.createElement("p2_18");
       	   if (rs.getString(1)!=null)
              p2_18.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_18.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_18);
       	  
          }
          
          //аттестат с отличием количество 2.19
          pstmt = conn.prepareStatement("select sum(cast (adi.ballatt as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.ballatt>0 and adi.ballatt not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
         pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_19 = doc.createElement("p2_19");
       	   if (rs.getString(1)!=null)
              p2_19.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       	p2_19.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_19);
       	  
          }
          
          
        //волонтерская деятельность количество 2.20
          pstmt = conn.prepareStatement("select sum(cast (adi.trudovajadejatelnost as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.trudovajadejatelnost>0 and adi.trudovajadejatelnost not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
   pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_20 = doc.createElement("p2_20");
       	   if (rs.getString(1)!=null)
              p2_20.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       	   p2_20.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_20);
       	  
          }
          
          
          //количество олимпиадников 2.21
          pstmt = conn.prepareStatement("select sum(cast (k.olimp as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti  and a.viddoksredobraz like '%аттестат%' and k.olimp>0 and k.olimp not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
   pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "i"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_21 = doc.createElement("p2_21");
       	   if (rs.getString(1)!=null)
       	   p2_21.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_21.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_21);
       	  
          }
          
          //количество сочинения 2.22
          pstmt = conn.prepareStatement("select sum(cast (adi.ballsoch as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti  and a.viddoksredobraz like '%аттестат%' and adi.ballsoch>0 and adi.ballsoch not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "j"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   

       	   Element p2_22 = doc.createElement("p2_22");
       	   if (rs.getString(1)!=null)
       	   p2_22.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_22.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_22);
       	  
          }
          
          
          //средний балл чемпионы 2.23
          pstmt = conn.prepareStatement("select avg(cast (adi.ballpoi as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.ballsoch>0 and adi.ballsoch not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   float ballSoch = rs.getFloat(1);
       	   String rowN = "k"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(ballSoch);
       	   
       	   Element p2_23 = doc.createElement("p2_23");
       	   if (rs.getString(1)!=null)
              p2_23.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_23.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_23);
       	  
       	  
          }
          
          //средний балл отличие 2.24
          pstmt = conn.prepareStatement("select avg(cast (adi.ballatt as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.ballatt>0 and adi.ballatt not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
           pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   float ballSoch = rs.getFloat(1);
       	   String rowN = "l"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(ballSoch);
       	   
       	   Element p2_24 = doc.createElement("p2_24");
       	   if (rs.getString(1)!=null)
       	   p2_24.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_24.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_24);
       	  
          }
          
          //средний балл волонтерская 2.25
          pstmt = conn.prepareStatement("select avg(cast (adi.trudovajadejatelnost as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.trudovajadejatelnost>0 and adi.trudovajadejatelnost not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
    
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   float ballSoch = rs.getFloat(1);
       	   String rowN = "m"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(ballSoch);
       	   
       	   Element p2_25 = doc.createElement("p2_25");
       	   if (rs.getString(1)!=null)
       	   p2_25.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_25.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_25);
       	  
          }
          
          
          //средний балл олимпиада 2.26
          pstmt = conn.prepareStatement("select avg(cast (k.olimp as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and k.olimp>0 and k.olimp not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   float ballSoch = rs.getFloat(1);
       	   String rowN = "n"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(ballSoch);
       	   
       	   Element p2_26 = doc.createElement("p2_26");
       	   if (rs.getString(1)!=null)
              p2_26.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_26.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_26);
       	  
          }
          
          //средний балл сочинения 2.27
          pstmt = conn.prepareStatement("select avg(cast (adi.ballsoch as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.ballsoch>0 and adi.ballsoch not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");
       
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   float ballSoch = rs.getFloat(1);
       	   String rowN = "o"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(ballSoch);
       	  
       	   Element p2_27 = doc.createElement("p2_27");
       	   if (rs.getString(1)!=null)
              p2_27.appendChild(doc.createTextNode(rs.getString(1)));
              else
           	   p2_27.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_27);
       	  
          }
          

          // количество лиц чемпионы 2.28
          pstmt = conn.prepareStatement("select count(cast (adi.ballpoi as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.ballpoi>0 and adi.ballpoi not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");

          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "p"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	  Element p2_28 = doc.createElement("p2_28");
       	  if (rs.getString(1)!=null)
              p2_28.appendChild(doc.createTextNode(rs.getString(1)));
       	  else
       		  p2_28.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_28);
       	  
          }
          
          // количество лиц отличие 2.29
          pstmt = conn.prepareStatement("select count(cast (adi.ballatt as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti  and a.viddoksredobraz like '%аттестат%' and adi.ballatt>0 and adi.ballatt not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");

          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "q"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_29 = doc.createElement("p2_29");
       	   if (rs.getString(1)!=null)
              p2_29.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_29.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_29);
       	  
          }
          
          // количество лиц волонтеры 2.30
          pstmt = conn.prepareStatement("select count(cast (adi.trudovajadejatelnost as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and adi.trudovajadejatelnost>0 and adi.trudovajadejatelnost not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");

          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "r"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	  
       	   
       	   Element p2_30 = doc.createElement("p2_30");
       	   if (rs.getString(1)!=null)
              p2_30.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_30.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_30);
       	  
          }
          
          // количество лиц олимпиада 2.31
          pstmt = conn.prepareStatement("select count(cast (k.olimp as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and k.olimp>0 and k.olimp not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");

          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "s"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_31 = doc.createElement("p2_31");
       	   if (rs.getString(1)!=null)
              p2_31.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_31.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_31);
       	  
          }
          
          // количество лиц сочинение 2.32
          pstmt = conn.prepareStatement("select count(cast (adi.ballsoch as float)) from abiturient a, konkurs k, abitdopinf adi where adi.kodabiturienta=k.kodabiturienta and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti  and a.viddoksredobraz like '%аттестат%' and adi.ballsoch>0 and adi.ballsoch not in ('да','нет') and k.kodspetsialnosti = ? and a.prinjat = 'д'");

          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "t"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_32 = doc.createElement("p2_32");
       	   if (rs.getString(1)!=null)
       	   p2_32.appendChild(doc.createTextNode(rs.getString(1)));
       	   else
       		   p2_32.appendChild(doc.createTextNode("0"));   
              lines.appendChild(p2_32);
       	  
          }
          
          
          
          
          
       
          
          rootElement.appendChild(lines);
          rowIndex++;
        }
        
       /* pstmt = conn.prepareStatement("Select Distinct k.kodspetsialnosti  from konkurs k , spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.eduLevel in ('б','с','м')");
        rs = pstmt.executeQuery();
        specs.clear();
        while(rs.next()){
       	 specs.add(new Integer(rs.getInt(1)));
        }
       itr = specs.iterator();
       //int rowIndex = 9;
       while(itr.hasNext()) {
          kodSpec = (Integer) itr.next();
          
          Element lines = doc.createElement("lines");
			Attr attrLid = doc.createAttribute("id");
			attrLid.setValue(""+lineIndex);
			lineIndex++;
			lines.setAttributeNode(attrLid);
			Element oo = doc.createElement("oo");
			oo.appendChild(doc.createTextNode("583"));
			lines.appendChild(oo);
          
          pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti, Tip_spec, gzgu from spetsialnosti where kodspetsialnosti = ?");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "a"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(rs.getString(1));
       	   rowN = "b"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(rs.getString(2));
       	   rowN = "c"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(rs.getString(3));
       	   
       	   String FO = "";
       	   if (rs.getString(4).equals("о")) FO = "ф1";
       	   if (rs.getString(4).equals("з")) FO = "ф3";
       	   if (rs.getString(4).equals("в")) FO = "ф2";
       	   
       	   rowN = "d"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(FO);
       	   
       	  rowN = "e"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue("4");
      	   
      	   
      		
  			
  			
  			Element spec = doc.createElement("spec");
  			spec.appendChild(doc.createTextNode(rs.getString(5)));
  			lines.appendChild(spec);
  			
  			
  			
  			Element foTag = doc.createElement("fo");
  			foTag.appendChild(doc.createTextNode(FO));
  			lines.appendChild(foTag);
  			
  						
  			Element ff = doc.createElement("ff");
  			ff.appendChild(doc.createTextNode("4"));
  			lines.appendChild(ff);
  			
  			
  			
       	   
       	   
       	        	 
          }
          //крым всего
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs WHERE kodspetsialnosti = ? and  konkurs.dog = 'д' and kodabiturienta in (select kodabiturienta from abiturient where kodoblastiP in ('9100000000000','9200000000000'))");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	 Element p1_10 = doc.createElement("p1_10");
         p1_10.appendChild(doc.createTextNode(rs.getString(1)));
         lines.appendChild(p1_10);       	  
          }
          
          //крым льготы
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs WHERE kodspetsialnosti = ?  and  konkurs.dog = 'д' and op  not like '1' and kodabiturienta in (select kodabiturienta from abiturient where kodoblastiP in ('9100000000000','9200000000000'))");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	  Element p1_11 = doc.createElement("p1_11");
          p1_11.appendChild(doc.createTextNode(rs.getString(1)));
          lines.appendChild(p1_11);
   	  
       	  
          }
          
          
          //крым целевой
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs WHERE kodspetsialnosti = ?  and  konkurs.dog = 'д' and target  not like '1' and kodabiturienta in (select kodabiturienta from abiturient where kodoblastiP in ('9100000000000','9200000000000'))");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	 Element p1_12 = doc.createElement("p1_12");
         p1_12.appendChild(doc.createTextNode(rs.getString(1)));
         lines.appendChild(p1_12);
  	  
       	  
          }
          
          
          // всего
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs WHERE kodspetsialnosti = ?  and  konkurs.dog = 'д' and kodabiturienta not in (select kodabiturienta from abiturient where kodoblastiP in ('9100000000000','9200000000000'))");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "l"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);

       	 Element p1_16 = doc.createElement("p1_16");
         p1_16.appendChild(doc.createTextNode(rs.getString(1)));
         lines.appendChild(p1_16);
       	  
          }
          
          // льготы
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs WHERE kodspetsialnosti = ?  and  konkurs.dog = 'д' and op  not like '1' and kodabiturienta not in (select kodabiturienta from abiturient where kodoblastiP in ('9100000000000','9200000000000'))");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "m"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	 Element p1_17 = doc.createElement("p1_17");
         p1_17.appendChild(doc.createTextNode(rs.getString(1)));
         lines.appendChild(p1_17);
       	  
          }
          
          
          //целевой
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs WHERE kodspetsialnosti = ?  and  konkurs.dog = 'д' and target  not like '1' and kodabiturienta not in (select kodabiturienta from abiturient where kodoblastiP in ('9100000000000','9200000000000'))");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "n"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	 Element p1_18 = doc.createElement("p1_18");
         p1_18.appendChild(doc.createTextNode(rs.getString(1)));
         lines.appendChild(p1_18);
       	  
          }
          
          // всего после 24.07.2015
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs, Abiturient WHERE konkurs.kodspetsialnosti = ? and  konkurs.dog = 'д'  and abiturient.dataModify in ('24.07.2015','25.07.2015','26.07.2015','27.07.2015','28.07.2015') and konkurs.kodabiturienta = abiturient.kodabiturienta");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "o"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	 Element p1_19 = doc.createElement("p1_19");
         p1_19.appendChild(doc.createTextNode(rs.getString(1)));
         lines.appendChild(p1_19);
       	  
          }
          
       // всего после 09/08/2015
          pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs, Abiturient WHERE konkurs.kodspetsialnosti = ? and  konkurs.dog = 'д'  and abiturient.dataModify in ('24.07.2015','25.07.2015','26.07.2015','27.07.2015','28.07.2015') and konkurs.kodabiturienta = abiturient.kodabiturienta");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "p"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	 Element p1_20 = doc.createElement("p1_20");
         p1_20.appendChild(doc.createTextNode(rs.getString(1)));
         lines.appendChild(p1_20);
       	  
          }
          
          
          
          rootElement.appendChild(lines);
          rowIndex++;
        }
        
        */ 
       String ename = "Форма EXCEL  "+StringUtil.CurrDate(".")+" на "+StringUtil.CurrTime(":");

       String efile_con = new String("umu_excel_f4_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

  //     session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),ename,efile_con,"xls"));
       session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),ename,efile_con,"xls"));
       System.out.println("RPT session   " + ((ReportsBrowserBean)session.getAttribute("rpt")).getFileName());

       String efile_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
       abit_A.setFileName1(""+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName());
       
       
      
       
       System.out.println("EFILE_NAME  " + efile_name);
       workbook.save(efile_name);
       
       
     TransformerFactory transformerFactory = TransformerFactory.newInstance();
   	Transformer transformer = transformerFactory.newTransformer();
   	DOMSource source = new DOMSource(doc);
   	
    String efile_con_xml = new String("umu_xml_f4_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

    
         session.setAttribute("rpt_xml",StringUtil.AddToRepBrw(user.getName()+user.getUid(),ename,efile_con_xml,"xml"));
         System.out.println("RPT session xml " + ((ReportsBrowserBean)session.getAttribute("rpt_xml")).getFileName());

         String efile_name_xml = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt_xml")).getFileName();
         
   	StreamResult result = new StreamResult(efile_name_xml);

   	// Output to console for testing
   	// StreamResult result = new StreamResult(System.out);

   	transformer.transform(source, result);
   	
  
    
    abit_A.setFileName2(""+((ReportsBrowserBean)session.getAttribute("rpt_xml")).getFileName());
    abit_A.setSpecial10("Форма 2. Раздел 2.4. EXCEL");
    abit_A.setSpecial13("Форма 2. Раздел 2.4. XML");
    
   
    

       
       //workbook.save("D:\\monitoring.xls");
          //сохраняем файл
      //    workbook.save("D:\\output.xls");



         
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/


  
 // String name = "Список абитуриентов 1-го этапа (предв.) "+AF+" "+priority;

 // String file_con = "lists_"+StringUtil.toEng(AF)+"_predv_first_wave";

 
 // session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

 // String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
 
 

 
   
  //return mapping.findForward("rep_brw");

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
        
     
    
    //    request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_A", abit_A);
       
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_ege_f) return mapping.findForward("lists_dec_ege_f");
        return mapping.findForward("success");
        //return mapping.findForward("rep_brw");
       // return mapping.findForward("success");
    }
}