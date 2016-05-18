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

public class UmuExcelF3Action extends Action {

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
          cell.setValue("раздел 2.1. Сведения о количестве лиц, зачисленных на обучение по категориям");
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
          cell.setValue("всего зачисленных");
          

          cell = cells.get("g4");
          cell.setValue("всего");
          

          cell = cells.get("h4");
          cell.setValue("без победителей и призеров");
          

          cell = cells.get("i4");
          cell.setValue("победителей и призеров");
          

          cell = cells.get("j4");
          cell.setValue("по экзаменам в вузе");
          

          cell = cells.get("k4");
          cell.setValue("по творч.");
          

          cell = cells.get("l4");
          cell.setValue("льготы по ЕГЭ");
          

          cell = cells.get("m4");
          cell.setValue("Льготы по вступ.");
          

          cell = cells.get("n4");
          cell.setValue("Цел. ЕГЭ");
          

          cell = cells.get("o4");
          cell.setValue("Цел. Без ЕГЭ");
          

          cell = cells.get("p4");
          cell.setValue("Победители олимпиад");
          
          
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
          cell.setValue("p2.0");
          

          cell = cells.get("g7");
          cell.setValue("p2.6");
          

          cell = cells.get("h7");
          cell.setValue("p2.7");
          

          cell = cells.get("i7");
          cell.setValue("p2.8 ");
          

          cell = cells.get("j7");
          cell.setValue("p2.9");
          

          cell = cells.get("k7");
          cell.setValue("p2.10");
          

          cell = cells.get("l7");
          cell.setValue("p2.2");
          

          cell = cells.get("m7");
          cell.setValue("p2.3");
          

          cell = cells.get("n7");
          cell.setValue("p2.4");
          

          cell = cells.get("o7");
          cell.setValue("p2.5");
          

          cell = cells.get("p7");
          cell.setValue("p2.1");
          
          
          
         /***************************Бюджет************************************/ 
   
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
           //всего зачисленных 2.0
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ?  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "f"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_0 = doc.createElement("p2_0");
               p2_0.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_0);
        	  
           }
           
           //всего по общему конкурсу ЕГЭ 2.6
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ?  and k.op = '1' and k.target = '1'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "g"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_6 = doc.createElement("p2_6");
               p2_6.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_6);
        	  
           }
           
           
         //по результатам ЕГЭ 2.7 без стоб.
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д'  and k.kodspetsialnosti = ? and k.kodabiturienta not in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op = '1' and k.target = '1'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "h"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_7 = doc.createElement("p2_7");
               p2_7.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_7);
        	  
           }
           
           
           // стоб. 2.8
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.op = '1' and k.target = '1' and k.stob is not null  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "i"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_8 = doc.createElement("p2_8");
               p2_8.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_8);
        	  
           }
           
           // 2.9 по 'в'
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д'  and k.kodspetsialnosti = ? and k.kodabiturienta in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op = '1' and k.target = '1'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
          
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "j"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_9 = doc.createElement("p2_9");
               p2_9.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_9);
        	  
           }
           
           
           //2.10 to do
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti inner join abiturient a on a.kodabiturienta = k.kodabiturienta where zach ='д' and k.kodspetsialnosti = ? and  k.prof not like '0' and k.prof is not null and a.viddoksredobraz like '%аттестат%'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "k"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_10 = doc.createElement("p2_10");
               p2_10.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_10);
        	  
           }
           
           // льготы ЕГЭ
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  not in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op not like '1' and k.target = '1'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "l"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_2 = doc.createElement("p2_2");
               p2_2.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_2);
        	  
           }
           
           // льготы в
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op not like '1' and k.target = '1'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
     
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "m"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_3 = doc.createElement("p2_3");
               p2_3.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_3);
        	  
           }
           
           
           // цел ЕГЭ
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  not in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op  like '1' and k.target not like '1'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "n"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_4 = doc.createElement("p2_4");
               p2_4.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_4);
        	  
           }
           
           // цел в
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op  like '1' and k.target not like '1'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
     
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "o"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_5 = doc.createElement("p2_5");
               p2_5.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_5);
        	  
           }
           

           // вне конкурса
           pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ?  and k.op = '7'  and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat in ('1','2','3','4'))");
     
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "p"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	   
        	   Element p2_1 = doc.createElement("p2_1");
               p2_1.appendChild(doc.createTextNode(rs.getString(1)));
               lines.appendChild(p2_1);
        	  
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
          //всего зачисленных 2.0
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_0 = doc.createElement("p2_0");
              p2_0.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_0);
       	  
          }
          
          //всего по общему конкурсу ЕГЭ 2.6
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ?  and k.op = '1' and k.target = '1' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_6 = doc.createElement("p2_6");
              p2_6.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_6);
       	  
          }
          
          
        //по результатам ЕГЭ 2.7 без стоб.
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д'  and k.kodspetsialnosti = ? and k.kodabiturienta not in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op = '1' and k.target = '1' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_7 = doc.createElement("p2_7");
              p2_7.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_7);
       	  
          }
          
          
          // стоб. 2.8
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.op = '1' and k.target = '1' and k.stob is not null and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "i"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_8 = doc.createElement("p2_8");
              p2_8.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_8);
       	  
          }
          
          // 2.9 по 'в'
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д'  and k.kodspetsialnosti = ? and k.kodabiturienta in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op = '1' and k.target = '1' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
         
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "j"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_9 = doc.createElement("p2_9");
              p2_9.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_9);
       	  
          }
          
          
          //2.10 to do
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti inner join abiturient a on a.kodabiturienta = k.kodabiturienta where zach ='д' and k.kodspetsialnosti = ? and  k.prof not like '0' and k.prof is not null and a.viddoksredobraz like '%аттестат%' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "k"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_10 = doc.createElement("p2_10");
              p2_10.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_10);
       	  
          }
          
          // льготы ЕГЭ
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  not in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op not like '1' and k.target = '1' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "l"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_2 = doc.createElement("p2_2");
              p2_2.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_2);
       	  
          }
          
          // льготы в
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op not like '1' and k.target = '1' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
    
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "m"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_3 = doc.createElement("p2_3");
              p2_3.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_3);
       	  
          }
          
          
          // цел ЕГЭ
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  not in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op  like '1' and k.target not like '1' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "n"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_4 = doc.createElement("p2_4");
              p2_4.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_4);
       	  
          }
          
          // цел в
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ? and k.kodabiturienta  in (select distinct kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and k.stob is null and k.op  like '1' and k.target not like '1' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
    
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "o"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_5 = doc.createElement("p2_5");
              p2_5.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_5);
       	  
          }
          

          // вне конкурса
          pstmt = conn.prepareStatement("select count(kodkonkursa) from konkurs k inner join spetsialnosti s on  k.kodspetsialnosti = s.kodspetsialnosti where zach ='д' and k.kodspetsialnosti = ?  and k.op = '7' and k.kodabiturienta in (select kodabiturienta from abiturient where prinjat = 'д')");
    
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "p"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);
       	   
       	   Element p2_1 = doc.createElement("p2_1");
              p2_1.appendChild(doc.createTextNode(rs.getString(1)));
              lines.appendChild(p2_1);
       	  
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

       String efile_con = new String("umu_excel_f1_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

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
   	
    String efile_con_xml = new String("umu_xml_f1_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

    
         session.setAttribute("rpt_xml",StringUtil.AddToRepBrw(user.getName()+user.getUid(),ename,efile_con_xml,"xml"));
         System.out.println("RPT session xml " + ((ReportsBrowserBean)session.getAttribute("rpt_xml")).getFileName());

         String efile_name_xml = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt_xml")).getFileName();
         
   	StreamResult result = new StreamResult(efile_name_xml);

   	// Output to console for testing
   	// StreamResult result = new StreamResult(System.out);

   	transformer.transform(source, result);
   	
  
    
    abit_A.setFileName2(""+((ReportsBrowserBean)session.getAttribute("rpt_xml")).getFileName());
    abit_A.setSpecial10("Форма 2. Раздел 2.1. EXCEL");
    abit_A.setSpecial13("Форма 2. Раздел 2.1. XML");
    
   
    

       
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