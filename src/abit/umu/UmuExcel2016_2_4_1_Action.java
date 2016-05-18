package abit.umu;
import java.io.IOException;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.ArrayList;

import org.apache.struts.action.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*; 

public class UmuExcel2016_2_4_1_Action extends Action {

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
        
       
        ResultSet            rs                 = null;
        ResultSet            rs1                = null;
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ResultSet            rs4                = null;
        ResultSet            rs5                = null;
        ResultSet            rs6                = null;
        ResultSet            rs7                = null;    
        ResultSet            rs8                = null;  
        ResultSet            rs9                = null;    
        ResultSet            rs10               = null; 
        ResultSet            rs11               = null;
        ResultSet            rs12               = null; 
        ResultSet            rs13               = null; 
        ResultSet            rs14               = null; 
        ResultSet            rs15               = null; 
        ResultSet            rs16               = null;   
        ResultSet            rs17               = null; 
        ResultSet            rs18               = null; 
        ResultSet            rs19               = null; 
        ResultSet            rs20               = null; 
        ResultSet            rs21               = null;
        ResultSet            rs22               = null;      
        ResultSet            rs23               = null;   
        ResultSet            rs24               = null;  
        ResultSet            rs25               = null;  
        ResultSet            rs26               = null;  
        ResultSet            rs27               = null;  
        ResultSet            rs28               = null;  
        ResultSet            rs29               = null;  
        ResultSet            rs30               = null;  
        ResultSet            rs31               = null; 
        ResultSet            rs32               = null;
        ResultSet            rs33               = null; 
        ResultSet            rs34               = null; 
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
      
     
        boolean              lists_dec_ege_f    = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList<Integer>			 specs              = new ArrayList();
        ArrayList<Integer>			 specs2              = new ArrayList();
        Integer kodSpec = null;
        AbiturientBean abit_A = new AbiturientBean();
        
        UserBean             user               = (UserBean)session.getAttribute("user");
        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
         

          //добавляем новый лист Excel и получаем к нему доступ        
           int lineIndex = 1;

          Workbook workbook = new Workbook();

          //добавляем новый лист Excel и получаем к нему доступ
          Worksheet worksheet = workbook.getWorksheets().get(0);
          
          Cells cells = worksheet.getCells();
               
          Cell cell = cells.get("A2");
          cells.setColumnWidth(0, 130);
          cell.setValue("2.4.1. Распределение приема студентов по направлениям подготовки и специальностям (мониторинг) 2016");
          
          cell = cells.get("F4");
          cell.setValue("Приняты на обучение");
         
          cell = cells.get("K4");
          cell.setValue("из них (из графы 5) получивших");
          
          cell = cells.get("T4");
          cell.setValue("из гр.14");
          
          cell = cells.get("W4");
          cell.setValue("Средний минимальный балл ЕГЭ");
          
          cell = cells.get("AA4");
          cell.setValue("Средний балл ЕГЭ");
          
          cell = cells.get("AG4");
          cell.setValue("Ср.б.ЕГЭ с доп.испыт.");
          
          cell = cells.get("F5");
          cell.setValue("за счет бюджетных ассигнований");
          
          cell = cells.get("U5");
          cell.setValue("из них (из гр.20)");
          
          cell = cells.get("W5");
          cell.setValue("принятые на бюджет");
          
          cell = cells.get("Y5");
          cell.setValue("с полным возмещением");
          
          cell = cells.get("AA5");
          cell.setValue("принятые на бюджет");
          
          cell = cells.get("AC5");
          cell.setValue("с полным возмещением");
          
          cell = cells.get("AE5");
          cell.setValue("целевики");
          
          cell = cells.get("AF5");
          cell.setValue("пост. на прикл.бак.");
          
          cell = cells.get("AG5");
          cell.setValue("бюджет");
          
          cell = cells.get("AH5");
          cell.setValue("договор");
          
          cell = cells.get("A6");
          cell.setValue("Наименование напрваления подготовки, специальности");
          
          cell = cells.get("B6");
          cell.setValue("№ строки");    
          
          cell = cells.get("C6");
          cell.setValue("Код направления подготовки, специальности по перечню направлений подготовки (специальностей)"); 
          
          cell = cells.get("D6");
          cell.setValue("Подано заявлений"); 
          
          cell = cells.get("E6");
          cell.setValue("Принято (сумма гр.6-9)"); 
          
          cell = cells.get("F6");
          cell.setValue("федерального бюджета"); 
          
          cell = cells.get("G6");
          cell.setValue("бюджета субъекта РФ"); 
          
          cell = cells.get("H6");
          cell.setValue("местного бюджета"); 
          
          cell = cells.get("I6");
          cell.setValue("с полным возмещением стоимости обучения");    
          
          cell = cells.get("J6");
          cell.setValue("в рамках целевого приема");    
          
          cell = cells.get("K6");
          cell.setValue("предыдущее образование в другом регионе");           
          
          cell = cells.get("L6");
          cell.setValue("диплом бакалавра, специалиста или магистра в данной организации");     
          
          cell = cells.get("M6");
          cell.setValue("из них (из гр.5) - принято на обучение для получения первого высшего образования");       
          
          cell = cells.get("N6");
          cell.setValue("из них (из гр.13) - по программам академ. бак-та и спец-та");
          
          cell = cells.get("O6");
          cell.setValue("из них (из гр.13) - по программам прикл.бак-та"); 
          
          cell = cells.get("P6");
          cell.setValue("по результатам ЕГЭ"); 
          
          cell = cells.get("Q6");
          cell.setValue("из них (из гр.16) - с полным возмеще-нием стоимости обучения");
          
          cell = cells.get("R6");
          cell.setValue("по результатам ЕГЭ и дополнительных испытаний");
          
          cell = cells.get("S6");
          cell.setValue("из них (из гр.18) - с полным возмещ. стоимости обучения");
          
          cell = cells.get("T6");
          cell.setValue("лица, имеющие право на прием без вступительных испытаний");    
          
          cell = cells.get("U6");
          cell.setValue("победители и призеры заключ.этапа всеросс.олимп.школ, члены сборн.команд РФ,участв.в междунар.олимп.по общеобраз.предметам"); 
          
          cell = cells.get("V6");
          cell.setValue("победители и призеры олимпиад школьников"); 
          
          cell = cells.get("W6");
          cell.setValue("учтенных в гр.16");      
          
          cell = cells.get("X6");
          cell.setValue("учтенных в гр.18");   
          
          cell = cells.get("Y6");
          cell.setValue("учтенных в гр.17");      
          
          cell = cells.get("Z6");
          cell.setValue("учтенных в гр.19");    
          
          cell = cells.get("AA6");
          cell.setValue("учтенных в гр.16");      
          
          cell = cells.get("AB6");
          cell.setValue("учтенных в гр.18");   
          
          cell = cells.get("AC6");
          cell.setValue("учтенных в гр.17");      
          
          cell = cells.get("AD6");
          cell.setValue("учтенных в гр.19");  
          
          cell = cells.get("AE6");
          cell.setValue("учтенных в гр.10");      
          
          cell = cells.get("AF6");
          cell.setValue("учтенных в гр.15");   
          
          cell = cells.get("AG6");
          cell.setValue("учтенных в гр.18");      
          
          cell = cells.get("AH6");
          cell.setValue("учтенных в гр.19");  
          
          cell = cells.get("A8");
          cell.setValue("Очная форма");

          /****************************** Выборка (очная форма)  ******************************/
          StringBuffer queryTotal = new StringBuffer("select distinct s.kodspetsialnosti, s.nazvaniespetsialnosti, s.shifrspetsialnosti, s.Tip_Spec, s.edulevel FROM konkurs k, spetsialnosti s");
         
          queryTotal.append(" WHERE ");
          queryTotal.append("k.kodspetsialnosti = s.kodspetsialnosti and s.Tip_Spec = 'о' and s.edulevel in ('б', 'с')"); 
          
        //******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

          StringBuffer condition = new StringBuffer();
 
//************************************************
          queryTotal.append(condition);
          
          int rowIndex = 9;
          int rowIndex2 = 9;
          int rowIndex3 = 9;
          int rowIndex4 = 9;
          int rowIndex5 = 9;
          int rowIndex6 = 9;
          int rowIndex7 = 9;
          int rowIndex8 = 9;
          int rowIndex21 = 9;
          int rowIndex24 = 9;
          int rowIndex27 = 9;
          int rowIndex30 = 9;
          int rowIndex33 = 9;
          int rowIndex36 = 9;
          
          System.out.println(queryTotal);
          stmt = conn.createStatement();
          rs = stmt.executeQuery(queryTotal.toString());
          
  	  
          while(rs.next()){
        	  
          	 specs.add(new Integer(rs.getInt(1)));
          	// Наименование направления подготовки, специальности
        	   String rowN = "a"+rowIndex;
        	   cell = cells.get(rowN);
        	   rowIndex = rowIndex+1;	   
        	  cell.setValue(rs.getString(2));  	  
        	  
       	// Шифр специальности, направления подготовки
     	   String rowN2 = "c"+rowIndex2;
     	   cell = cells.get(rowN2);
     	   rowIndex2 = rowIndex2+1;
     	  cell.setValue(rs.getString(3));
     	  
     	  // Код строки (специальности)
    	   String rowN24 = "b"+rowIndex21;
    	   cell = cells.get(rowN24);
    	   rowIndex21 = rowIndex21+1;
    	  cell.setValue(rs.getString(1));
     	  
     	  
     	  
      	   // Подано заявлений
       	  PreparedStatement stmt3 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ?");
       	  stmt3.setObject(1, rs.getInt(1), Types.INTEGER);
       	  rs3 = stmt3.executeQuery();
       	  if (rs3.next()){
       		  specs.add(new Integer(rs3.getInt(1)));
       		  String rowN4 = "d"+rowIndex4;
       		  cell = cells.get(rowN4);
       		  rowIndex4 = rowIndex4+1;
       		  cell.setValue(rs3.getString(1));
       	  }
       	  

       	         	  
		  // Принято
		  PreparedStatement stmt4 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н'");
    	  stmt4.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs4 = stmt4.executeQuery();
    	  if (rs4.next()){
    		  specs.add(new Integer(rs4.getInt(1)));
    		  String rowN5 = "e"+rowIndex5;
    		  cell = cells.get(rowN5);
    		  rowIndex5 = rowIndex5+1;
    		  cell.setValue(rs4.getString(1));
           }
    	  
    	  // Принято за счет бюджетных ассигнований федерального бюджета
    	  PreparedStatement stmt5 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н' and a.prinjat not like 'д'");
    	  stmt5.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs5 = stmt5.executeQuery();
    	  if (rs5.next()){
    		  specs.add(new Integer(rs5.getInt(1)));
    		  String rowN6 = "f"+rowIndex6;
    		  cell = cells.get(rowN6);
    		  rowIndex6 = rowIndex6+1;
    		  cell.setValue(rs5.getString(1));
      	  }    	
                
    	  // С полным возмещением стоимости обучения
		  PreparedStatement stmt6 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat like 'д'");
    	  stmt6.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs6 = stmt6.executeQuery();
    	  if (rs6.next()){
    		  specs.add(new Integer(rs6.getInt(1)));
    		  String rowN7 = "i"+rowIndex7;
    		  cell = cells.get(rowN7);
    		  rowIndex7 = rowIndex7+1;
    		  cell.setValue(rs6.getString(1));
    		           
	      }    
    	     	
      	// целевой приём (очная форма)
		  PreparedStatement stmt15 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ? and k.target not like '1'");
  	  stmt15.setObject(1, rs.getInt(1), Types.INTEGER);
  	  rs20 = stmt15.executeQuery();
  	  if (rs20.next()){
  		  specs.add(new Integer(rs20.getInt(1)));
  		  String rowN25 = "j"+rowIndex24;
  		  cell = cells.get(rowN25);
  		  rowIndex24 = rowIndex24+1;
  		  cell.setValue(rs20.getString(1));
         }
  	  
    	// предыдущее образование в другом регионе (очная форма)
		  PreparedStatement stmt18 = conn.prepareStatement("SELECT COUNT(a.kodspetsialnzach) FROM Abiturient a, Zavedenija z WHERE a.kodspetsialnzach = ? and a.kodzavedenija=z.kodzavedenija and z.PolnoeNaimenovanieZavedenija LIKE 'Другое' OR z.PolnoeNaimenovanieZavedenija IS NULL");
	  stmt18.setObject(1, rs.getInt(1), Types.INTEGER);
	  rs23 = stmt18.executeQuery();
	  if (rs23.next()){
		  specs.add(new Integer(rs23.getInt(1)));
		  String rowN28 = "k"+rowIndex27;
		  cell = cells.get(rowN28);
		  rowIndex27 = rowIndex27+1;
		  cell.setValue(rs23.getString(1));
       }
	  
//	  	// поступление на первое высшее (очное) 
//	  	PreparedStatement stmt21 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where a.kodspetsialnzach = ? and a.prinjat not like 'н'");
//	  	stmt21.setObject(1, rs.getInt(1), Types.INTEGER);
//	  	rs26 = stmt21.executeQuery();
//	  	if (rs26.next()){
//	  		specs.add(new Integer(rs26.getInt(1)));
//	  		String rowN31 = "m"+rowIndex30;
//	  		cell = cells.get(rowN31);
//	  		rowIndex30 = rowIndex30+1;
//	  		cell.setValue(rs26.getString(1));
//	  	}

	  
	  	// лица, иеющие право на прием без вступительных испытаний (очная)
	  	PreparedStatement stmt24 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ? and k.op like '7'");
	  	stmt24.setObject(1, rs.getInt(1), Types.INTEGER);
	  	rs29 = stmt24.executeQuery();
	  	if (rs29.next()){
	  		specs.add(new Integer(rs29.getInt(1)));
	  		String rowN34 = "t"+rowIndex33;
	  		cell = cells.get(rowN34);
	  		rowIndex33 = rowIndex33+1;
	  		cell.setValue(rs29.getString(1));
	  	}
          
//	  	// бакалавры и специалисты 
//	  	PreparedStatement stmt27 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = ? and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel not like 'м' and a.prinjat not like 'н'");
//	  	stmt27.setObject(1, rs.getInt(1), Types.INTEGER);
//	  	rs32 = stmt27.executeQuery();
//	  	if (rs32.next()){
//	  		specs.add(new Integer(rs32.getInt(1)));
//	  		String rowN37 = "n"+rowIndex36;
//	  		cell = cells.get(rowN37);
//	  		rowIndex36 = rowIndex36+1;
//	  		cell.setValue(rs32.getString(1));
//	  	}
	  	
          cell = cells.get("C94");
          cell.setValue("Очно");
          
          cell = cells.get("A97");
          cell.setValue("Заочная форма");
          
      		
       	  }
          
          /****************************** Выборка (заочная форма)  ******************************/
          StringBuffer queryTotal2 = new StringBuffer("select distinct s.kodspetsialnosti, s.nazvaniespetsialnosti, s.shifrspetsialnosti, s.Tip_Spec, s.edulevel FROM konkurs k, spetsialnosti s WHERE k.kodspetsialnosti = s.kodspetsialnosti and s.Tip_Spec = 'з' and s.edulevel in ('б', 'с')");
          
        //******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

          StringBuffer condition2 = new StringBuffer();
 
//************************************************
          queryTotal2.append(condition);
          
          int rowIndex9 = 98;
          int rowIndex10 = 98;
          int rowIndex11 = 98;
          int rowIndex12 = 98;
          int rowIndex13 = 98;
          int rowIndex14 = 98;
          int rowIndex22 = 98;
          int rowIndex25 = 98;
          int rowIndex28 = 98;
          int rowIndex31 = 98;
          int rowIndex34 = 98;
          int rowIndex37 = 98;
          
          System.out.println(queryTotal2);
          stmt = conn.createStatement();
          rs = stmt.executeQuery(queryTotal2.toString());
          
  	  
          while(rs.next()){
        	  
          	 specs.add(new Integer(rs.getInt(1)));
          	// Наименование направления подготовки, специальности
        	   String rowN8 = "a"+rowIndex9;
        	   cell = cells.get(rowN8);
        	   rowIndex9 = rowIndex9+1;	   
        	  cell.setValue(rs.getString(2));  	  
        	  
       	// Шифр специальности, направления подготовки
     	   String rowN9 = "c"+rowIndex10;
     	   cell = cells.get(rowN9);
     	   rowIndex10 = rowIndex10+1;
     	  cell.setValue(rs.getString(3));
     	  
     	  // Код строки (специальности)
    	   String rowN25 = "b"+rowIndex22;
    	   cell = cells.get(rowN25);
    	   rowIndex22 = rowIndex22+1;
    	  cell.setValue(rs.getString(1));
    	  
     	// Подано заявлений
       	  PreparedStatement stmt7 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ?");
       	  stmt7.setObject(1, rs.getInt(1), Types.INTEGER);
       	  rs7 = stmt7.executeQuery();
       	  if (rs7.next()){
       		  specs.add(new Integer(rs7.getInt(1)));
       		  String rowN10 = "d"+rowIndex11;
       		  cell = cells.get(rowN10);
       		  rowIndex11 = rowIndex11+1;
       		  cell.setValue(rs7.getString(1));
       	  }
       	  
		  // Принято
		  PreparedStatement stmt8 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н'");
    	  stmt8.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs8 = stmt8.executeQuery();
    	  if (rs8.next()){
    		  specs.add(new Integer(rs8.getInt(1)));
    		  String rowN11 = "e"+rowIndex12;
    		  cell = cells.get(rowN11);
    		  rowIndex12 = rowIndex12+1;
    		  cell.setValue(rs8.getString(1));
           }
    	  
    	  // Принято за счет бюджетных ассигнований федерального бюджета
    	  PreparedStatement stmt9 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н' and a.prinjat not like 'д'");
    	  stmt9.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs9 = stmt9.executeQuery();
    	  if (rs9.next()){
    		  specs.add(new Integer(rs9.getInt(1)));
    		  String rowN12 = "f"+rowIndex13;
    		  cell = cells.get(rowN12);
    		  rowIndex13 = rowIndex13+1;
    		  cell.setValue(rs9.getString(1));
      	  }    	
                
    	  // С полным возмещением стоимости обучения
		  PreparedStatement stmt10 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat like 'д'");
    	  stmt10.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs10 = stmt10.executeQuery();
    	  if (rs10.next()){
    		  specs.add(new Integer(rs10.getInt(1)));
    		  String rowN13 = "i"+rowIndex14;
    		  cell = cells.get(rowN13);
    		  rowIndex14 = rowIndex14+1;
    		  cell.setValue(rs10.getString(1));
	      }    
    	  
      	// целевой приём (заочная форма)
		  PreparedStatement stmt16 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ? and k.target not like '1'");
  	  stmt16.setObject(1, rs.getInt(1), Types.INTEGER);
  	  rs21 = stmt16.executeQuery();
  	  if (rs21.next()){
  		  specs.add(new Integer(rs21.getInt(1)));
  		  String rowN26 = "j"+rowIndex25;
  		  cell = cells.get(rowN26);
  		  rowIndex25 = rowIndex25+1;
  		  cell.setValue(rs21.getString(1));
         }
     	  
  	// предыдущее образование в другом регионе (заочная форма)
		  PreparedStatement stmt19 = conn.prepareStatement("SELECT COUNT(a.kodspetsialnzach) FROM Abiturient a, Zavedenija z WHERE a.kodspetsialnzach = ? and a.kodzavedenija=z.kodzavedenija and z.PolnoeNaimenovanieZavedenija LIKE 'Другое' OR z.PolnoeNaimenovanieZavedenija IS NULL");
	  stmt19.setObject(1, rs.getInt(1), Types.INTEGER);
	  rs24 = stmt19.executeQuery();
	  if (rs24.next()){
		  specs.add(new Integer(rs24.getInt(1)));
		  String rowN29 = "k"+rowIndex28;
		  cell = cells.get(rowN29);
		  rowIndex28 = rowIndex28+1;
		  cell.setValue(rs24.getString(1));
     }
	  
//	  	// поступление на первое высшее (заочное) ???
//	  	PreparedStatement stmt22 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where a.kodspetsialnzach = ? and a.prinjat not like 'н'");
//	  	stmt22.setObject(1, rs.getInt(1), Types.INTEGER);
//	  	rs27 = stmt22.executeQuery();
//	  	if (rs27.next()){
//	  		specs.add(new Integer(rs27.getInt(1)));
//	  		String rowN32 = "m"+rowIndex31;
//	  		cell = cells.get(rowN32);
//	  		rowIndex31 = rowIndex31+1;
//	  		cell.setValue(rs27.getString(1));
//	  	}
	  
	  	// лица, иеющие право на прием без вступительных испытаний (заочная)
	  	PreparedStatement stmt25 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ? and k.op like '7'");
	  	stmt25.setObject(1, rs.getInt(1), Types.INTEGER);
	  	rs30 = stmt25.executeQuery();
	  	if (rs30.next()){
	  		specs.add(new Integer(rs30.getInt(1)));
	  		String rowN35 = "t"+rowIndex34;
	  		cell = cells.get(rowN35);
	  		rowIndex34 = rowIndex34+1;
	  		cell.setValue(rs30.getString(1));
	  	}
	  	
//	  	// бакалавры и специалисты 
//	  	PreparedStatement stmt28 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = ? and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel not like 'м' and a.prinjat not like 'н'");
//	  	stmt28.setObject(1, rs.getInt(1), Types.INTEGER);
//	  	rs33 = stmt28.executeQuery();
//	  	if (rs33.next()){
//	  		specs.add(new Integer(rs33.getInt(1)));
//	  		String rowN38 = "n"+rowIndex37;
//	  		cell = cells.get(rowN38);
//	  		rowIndex37 = rowIndex37+1;
//	  		cell.setValue(rs33.getString(1));
//	  	}
  	  
          cell = cells.get("A150");
          cell.setValue("Очно-заочная форма");
          }
                
          /****************************** Выборка (очно-заочная форма) ******************************/
          StringBuffer queryTotal3 = new StringBuffer("select distinct s.kodspetsialnosti, s.nazvaniespetsialnosti, s.shifrspetsialnosti, s.Tip_Spec, s.edulevel FROM konkurs k, spetsialnosti s WHERE k.kodspetsialnosti = s.kodspetsialnosti and s.Tip_Spec = 'в' and s.edulevel in ('б', 'с')");
          
        //******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

          StringBuffer condition3 = new StringBuffer();
 
//************************************************
          queryTotal3.append(condition);
          
          int rowIndex15 = 151;
          int rowIndex16 = 151;
          int rowIndex17 = 151;
          int rowIndex18 = 151;
          int rowIndex19 = 151;
          int rowIndex20 = 151;
          int rowIndex23 = 151;
          int rowIndex26 = 151;
          int rowIndex29 = 151;     
          int rowIndex32 = 151;  
          int rowIndex35 = 151;  
          int rowIndex38 = 151; 
          
          System.out.println(queryTotal3);
          stmt = conn.createStatement();
          rs = stmt.executeQuery(queryTotal3.toString());
          
  	  
          while(rs.next()){
        	  
          	 specs.add(new Integer(rs.getInt(1)));
          	// Наименование направления подготовки, специальности
        	   String rowN14 = "a"+rowIndex15;
        	   cell = cells.get(rowN14);
        	   rowIndex15 = rowIndex15+1;	   
        	  cell.setValue(rs.getString(2));  	  
        	  
       	// Шифр специальности, направления подготовки
     	   String rowN15 = "c"+rowIndex16;
     	   cell = cells.get(rowN15);
     	   rowIndex16 = rowIndex16+1;
     	  cell.setValue(rs.getString(3));
     	  
     	  // Код строки (специальности)
    	   String rowN26 = "b"+rowIndex23;
    	   cell = cells.get(rowN26);
    	   rowIndex23 = rowIndex23+1;
    	  cell.setValue(rs.getString(1));
    	  
     	// Подано заявлений
       	  PreparedStatement stmt11 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ?");
       	  stmt11.setObject(1, rs.getInt(1), Types.INTEGER);
       	  rs11 = stmt11.executeQuery();
       	  if (rs11.next()){
       		  specs.add(new Integer(rs11.getInt(1)));
       		  String rowN16 = "d"+rowIndex17;
       		  cell = cells.get(rowN16);
       		  rowIndex17 = rowIndex17+1;
       		  cell.setValue(rs11.getString(1));
       	  }
       	  
		  // Принято
		  PreparedStatement stmt12 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н'");
    	  stmt12.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs12 = stmt12.executeQuery();
    	  if (rs12.next()){
    		  specs.add(new Integer(rs12.getInt(1)));
    		  String rowN17 = "e"+rowIndex18;
    		  cell = cells.get(rowN17);
    		  rowIndex18 = rowIndex18+1;
    		  cell.setValue(rs12.getString(1));
           }
    	  
    	  // Принято за счет бюджетных ассигнований федерального бюджета
    	  PreparedStatement stmt13 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н' and a.prinjat not like 'д'");
    	  stmt13.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs13 = stmt13.executeQuery();
    	  if (rs13.next()){
    		  specs.add(new Integer(rs13.getInt(1)));
    		  String rowN18 = "f"+rowIndex19;
    		  cell = cells.get(rowN18);
    		  rowIndex19 = rowIndex19+1;
    		  cell.setValue(rs13.getString(1));
      	  }    	
                
    	  // С полным возмещением стоимости обучения
		  PreparedStatement stmt14 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat like 'д'");
    	  stmt14.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs14 = stmt14.executeQuery();
    	  if (rs14.next()){
    		  specs.add(new Integer(rs14.getInt(1)));
    		  String rowN19 = "i"+rowIndex20;
    		  cell = cells.get(rowN19);
    		  rowIndex20 = rowIndex20+1;
    		  cell.setValue(rs14.getString(1));
	      }    
    	  
        	// целевой приём (очно-заочная форма)
  		  PreparedStatement stmt17 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ? and k.target not like '1'");
    	  stmt17.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs22 = stmt17.executeQuery();
    	  if (rs22.next()){
    		  specs.add(new Integer(rs22.getInt(1)));
    		  String rowN27 = "j"+rowIndex26;
    		  cell = cells.get(rowN27);
    		  rowIndex26 = rowIndex26+1;
    		  cell.setValue(rs22.getString(1));
           }
          
    	  	// предыдущее образование в другом регионе (очно-заочная форма)
		  PreparedStatement stmt20 = conn.prepareStatement("SELECT COUNT(a.kodspetsialnzach) FROM Abiturient a, Zavedenija z WHERE a.kodspetsialnzach = ? and a.kodzavedenija=z.kodzavedenija and z.PolnoeNaimenovanieZavedenija LIKE 'Другое' OR z.PolnoeNaimenovanieZavedenija IS NULL");
	  stmt20.setObject(1, rs.getInt(1), Types.INTEGER);
	  rs25 = stmt20.executeQuery();
	  if (rs25.next()){
		  specs.add(new Integer(rs25.getInt(1)));
		  String rowN30 = "k"+rowIndex29;
		  cell = cells.get(rowN30);
		  rowIndex29 = rowIndex29+1;
		  cell.setValue(rs25.getString(1));
     }
	  
//	  	// поступление на первое высшее (очно-заочное) 
//	  	PreparedStatement stmt23 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where a.kodspetsialnzach = ? and a.prinjat not like 'н'");
//	  	stmt23.setObject(1, rs.getInt(1), Types.INTEGER);
//	  	rs28 = stmt23.executeQuery();
//	  	if (rs28.next()){
//	  		specs.add(new Integer(rs28.getInt(1)));
//	  		String rowN33 = "m"+rowIndex32;
//	  		cell = cells.get(rowN33);
//	  		rowIndex32 = rowIndex32+1;
//	  		cell.setValue(rs28.getString(1));
//	  	}
    	  
	  	// лица, иеющие право на прием без вступительных испытаний (очно-заочная)
	  	PreparedStatement stmt26 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ? and k.op like '7'");
	  	stmt26.setObject(1, rs.getInt(1), Types.INTEGER);
	  	rs31 = stmt26.executeQuery();
	  	if (rs31.next()){
	  		specs.add(new Integer(rs31.getInt(1)));
	  		String rowN36 = "t"+rowIndex35;
	  		cell = cells.get(rowN36);
	  		rowIndex35 = rowIndex35+1;
	  		cell.setValue(rs31.getString(1));
	  	}
	  	
//  	  	// бакалавры и специалисты 
//  	  	PreparedStatement stmt29 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = ? and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel not like 'м' and a.prinjat not like 'н'");
//  	  	stmt29.setObject(1, rs.getInt(1), Types.INTEGER);
//  	  	rs34 = stmt29.executeQuery();
//  	  	if (rs34.next()){
//  	  		specs.add(new Integer(rs34.getInt(1)));
//  	  		String rowN39 = "n"+rowIndex38;
//  	  		cell = cells.get(rowN39);
//  	  		rowIndex38 = rowIndex38+1;
//  	  		cell.setValue(rs34.getString(1));
//  	  	}
	  	
          }
          

          
          // Сумма (подано заявлений - очное образование)
          pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.Tip_Spec = 'о' and s.edulevel in ('б', 'с')");
          rs15 = pstmt.executeQuery();
          while(rs15.next()){
       	   String rowN20 = "d94";
       	   cell = cells.get(rowN20);
       	   
       	  cell.setValue(rs15.getString(1));
          }
          
          // Сумма (принято)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and s.Tip_Spec = 'о' and s.edulevel in ('б', 'с') and a.prinjat not like 'н'");
          rs16 = pstmt.executeQuery();
          while(rs16.next()){
       	   String rowN21 = "e94";
       	   cell = cells.get(rowN21);
       	   
       	  cell.setValue(rs16.getString(1));
          }
          
          // Сумма (принято - за счёт общих ассигнований федерального бюджета - очное образование)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and s.Tip_Spec = 'о' and s.edulevel in ('б', 'с') and a.prinjat not like 'н' and a.prinjat not like 'д'");
          rs17 = pstmt.executeQuery();
          while(rs17.next()){
       	   String rowN22 = "f94";
       	   cell = cells.get(rowN22);
       	   
       	  cell.setValue(rs17.getString(1));
          }
          
          // Сумма (принято - c полным возмещением стоимости обучения - очное образование)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and s.Tip_Spec = 'о' and s.edulevel in ('б', 'с') and a.prinjat like 'д'");
          rs18 = pstmt.executeQuery();
          while(rs18.next()){
       	   String rowN23 = "i94";
       	   cell = cells.get(rowN23);
       	   
       	  cell.setValue(rs18.getString(1));
          }
          


          
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

        String ename = "Форма EXCEL  "+StringUtil.CurrDate(".")+" на "+StringUtil.CurrTime(":");

        String efile_con = new String("umu_excel_f1_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

        session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),ename,efile_con,"xls"));
        System.out.println("RPT session   " + ((ReportsBrowserBean)session.getAttribute("rpt")).getFileName());

        String efile_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

        
        System.out.println("EFILE_NAME  " + efile_name);
        workbook.save(efile_name);


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
          if ( rs4 != null ) {
              try {
                    rs4.close();
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
    
      
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_ege_f) return mapping.findForward("lists_dec_ege_f");
        return mapping.findForward("rep_brw");
       // return mapping.findForward("success");
    }   
}