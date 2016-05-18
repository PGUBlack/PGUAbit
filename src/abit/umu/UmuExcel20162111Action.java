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

public class UmuExcel20162111Action extends Action {

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
        
       
        ResultSet            rs                 = null;
        ResultSet            rs1                = null;
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ResultSet            rs4                = null;
        ResultSet            rs5                = null;
        ResultSet            rs6                = null;
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
        
          Cell cell = cells.get("A1");
          cells.setColumnWidth(0, 50);
          cell.setValue("Пензенский государственный университет");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 50);          
          cell.setValue("Очное обучение"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 50);
          cell.setValue("2.1.1. Распределение приема по направлениям подготовки и специальностям");
          
          cell = cells.get("i5");
          cell.setValue("Код по ОКЕИ: человек – 792");           
          
          cell = cells.get("a6");
          cells.setColumnWidth(1, 50);
          cell.setValue(" ");        
          
          cell = cells.get("b6");
          cells.setColumnWidth(2, 25);
          cell.setValue(" ");     
          
          cell = cells.get("c6");
          cells.setColumnWidth(3, 30);
          cell.setValue(" ");     
          
          cell = cells.get("d6");
          cells.setColumnWidth(4, 25);
          cell.setValue(" ");    
          
          cell = cells.get("e6");
          cells.setColumnWidth(5, 25);
          cell.setValue(" ");     
          
          cell = cells.get("f6");
          cells.setColumnWidth(6, 30);
          cell.setValue("Принято");
          
          cell = cells.get("g6");
          cells.setColumnWidth(7, 30);
          cell.setValue("на");
          
          cell = cells.get("h6");
          cells.setColumnWidth(8, 30);
          cell.setValue("обучение:");
          
          cell = cells.get("f7");
          cell.setValue("за счет бюджетных ассигнований");
 
          cell = cells.get("a8");
          cell.setValue("Наименование направления подготовки, специальности");
                  
          cell = cells.get("b8");
          cell.setValue("№ строки");
          
          cell = cells.get("c8");
          cell.setValue("Код специальности, направления подготовки");

          cell = cells.get("d8");
          cell.setValue("Подано заявлений");
          
          cell = cells.get("e8");
          cell.setValue("Принято(сумма гр. 6 – 9)");
    
          cell = cells.get("f8");
          cell.setValue("федерального бюджета");
          
          cell = cells.get("g8");
          cell.setValue("бюджета субъекта Российской Федерации");
          
          cell = cells.get("h8");
          cell.setValue("местного бюджета");     
          
          cell = cells.get("i8");
          cell.setValue("по договорам об оказании платных образовательных услуг");  
          
          
          cell = cells.get("a10");
          cell.setValue("Программы бакалавриата - всего");  
          
          cell = cells.get("b10");
          cell.setValue("01"); 
          
          cell = cells.get("c10");
          cell.setValue("0"); 
          
          cell = cells.get("g10");
          cell.setValue("0");           
                  
          cell = cells.get("h10");
          cell.setValue("0"); 
          
          cell = cells.get("a11");
          cell.setValue("в том числе по направлениям:");        
          
// выбор по бакалавриату
          /****************************** Выборка ******************************/
          StringBuffer queryTotal = new StringBuffer("select distinct s.kodspetsialnosti, s.nazvaniespetsialnosti, s.shifrspetsialnosti, s.edulevel FROM konkurs k, spetsialnosti s");
         
          queryTotal.append(" WHERE ");
          queryTotal.append("k.kodspetsialnosti = s.kodspetsialnosti and s.edulevel = 'б'"); 

//******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

          StringBuffer condition = new StringBuffer();
 
//************************************************
          queryTotal.append(condition);
          
          int rowIndex = 12;
          int rowIndex2 = 12;
          int rowIndex3 = 12;     
          int rowIndex4 = 12;  
          int rowIndex5 = 12; 
          int rowIndex6 = 12; 
          
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
       	  
      	// Код специальности, направления подготовки
    	   String rowN2 = "c"+rowIndex2;
    	   cell = cells.get(rowN2);
    	   rowIndex2 = rowIndex2+1;
    	   
    	   // Подано заявлений
    	  cell.setValue(rs.getString(3));
    	  PreparedStatement stmt1 = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k where kodspetsialnosti = ?");
    	  stmt1.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs1 = stmt1.executeQuery();
    	  if (rs1.next()){
    		  specs.add(new Integer(rs1.getInt(1)));
    		  String rowN3 = "d"+rowIndex3;
    		  cell = cells.get(rowN3);
    		  rowIndex3 = rowIndex3+1;
    		  cell.setValue(rs1.getString(1));
    	  }
    		  // Принято(сумма гр. 6 – 9)
    		  PreparedStatement stmt4 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н'");
        	  stmt4.setObject(1, rs.getInt(1), Types.INTEGER);
        	  rs4 = stmt4.executeQuery();
        	  if (rs4.next()){
        		  specs.add(new Integer(rs4.getInt(1)));
        		  String rowN4 = "e"+rowIndex4;
        		  cell = cells.get(rowN4);
        		  rowIndex4 = rowIndex4+1;
        		  cell.setValue(rs4.getString(1));
    	  
    	  }
    	    	  // Принято за счет бюджетных ассигнований федерального бюджета
    		  PreparedStatement stmt6 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н' and a.prinjat not like 'д'");
        	  stmt6.setObject(1, rs.getInt(1), Types.INTEGER);
        	  rs6 = stmt6.executeQuery();
        	  if (rs6.next()){
        		  specs.add(new Integer(rs6.getInt(1)));
        		  String rowN6 = "f"+rowIndex6;
        		  cell = cells.get(rowN6);
        		  rowIndex6 = rowIndex6+1;
        		  cell.setValue(rs6.getString(1));
    	  
    	  }    	  
        	  // по договорам об оказании платных образовательных услуг
    		  PreparedStatement stmt5 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat like 'д'");
        	  stmt5.setObject(1, rs.getInt(1), Types.INTEGER);
        	  rs5 = stmt5.executeQuery();
        	  if (rs5.next()){
        		  specs.add(new Integer(rs5.getInt(1)));
        		  String rowN5 = "i"+rowIndex5;
        		  cell = cells.get(rowN5);
        		  rowIndex5 = rowIndex5+1;
        		  cell.setValue(rs5.getString(1));
    	  
    	  }    	  
        	  

          }
          // Подано заявлений - всего
        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'б'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN7 = "d10";
     	   cell = cells.get(rowN7);
     	   
     	  cell.setValue(rs.getString(1));
        }
        // Принято(сумма гр. 6 – 9) - всего
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN8 = "e10";
     	   cell = cells.get(rowN8);
     	   
     	  cell.setValue(rs.getString(1));
        }
         // по договорам об оказании платных образовательных услуг - всего
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat like 'д'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN9 = "i10";
     	   cell = cells.get(rowN9);
     	   
     	  cell.setValue(rs.getString(1));
        }      
     // Принято за счет бюджетных ассигнований федерального бюджета - всего
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'д' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN10 = "f10";
     	   cell = cells.get(rowN10);
     	   
     	  cell.setValue(rs.getString(1));
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
    
      
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_ege_f) return mapping.findForward("lists_dec_ege_f");
        return mapping.findForward("rep_brw");
       // return mapping.findForward("success");
    }   
}