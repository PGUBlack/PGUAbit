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

public class UmuExcel20162122Action extends Action {

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
        ResultSet            rs7                = null;        
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
          cells.setColumnWidth(0, 130);
          cell.setValue("Пензенский государственный университет");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 130);          
          cell.setValue("Очное обучение"); 
          
          cell = cells.get("l5");
          cell.setValue("Продолжение подраздела 2.1.2");      
          
          cell = cells.get("g6");
          cell.setValue("Из него (из гр.23):"); 
          
          cell = cells.get("l6");
          cell.setValue("Из него (из гр.28):");    
          
          cell = cells.get("l7");
          cell.setValue("за счет бюджетных ассигнований");  
           
          
          cell = cells.get("a8");
          cell.setValue("Наименование направления подготовки, специальности");
          
          cell = cells.get("b8");
          cell.setValue("№ строки");        
          
          cell = cells.get("c8");
          cell.setValue("Код классификатора");   
          
          cell = cells.get("d8");
          cell.setValue("Код НП(С)");   

          cell = cells.get("e8");
          cell.setValue("из общей численности студентов (из гр. 19) – женщины");    
          
          cell = cells.get("f8");
          cell.setValue("Выпуск фактический с 01.10.2014г. по 30.09.2015г.");           
          
          cell = cells.get("g8");
          cell.setValue("за счет бюджетных ассигнований федерального бюджета"); 
          
          cell = cells.get("h8");
          cell.setValue("по договорам об оказании платных образовательных услуг");      
          
          cell = cells.get("i8");
          cell.setValue("продолжили обучение в данной образовательной организации"); 
          
          cell = cells.get("j8");
          cell.setValue("женщины");      
          
          cell = cells.get("k8");
          cell.setValue("Выпуск ожидаемый с 01.10.2015г. по 30.09.2016г. (сумма гр. 29 – 32)"); 
          
          cell = cells.get("l8");
          cell.setValue("федерального бюджета");      
          
          cell = cells.get("m8");
          cell.setValue("бюджета субъекта Российской Федерации"); 
          
          cell = cells.get("n8");
          cell.setValue("местного бюджета");      
          
          cell = cells.get("o8");
          cell.setValue("по договорам об оказании платных образовательных услуг");   
        
       // выбор всех специальностей
          /****************************** Выборка ******************************/
          StringBuffer queryTotal = new StringBuffer("select distinct s.kodspetsialnosti, s.nazvaniespetsialnosti, s.shifrspetsialnosti, s.edulevel FROM konkurs k, spetsialnosti s");
         
          queryTotal.append(" WHERE ");
          queryTotal.append("k.kodspetsialnosti = s.kodspetsialnosti"); 

//******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

          StringBuffer condition = new StringBuffer();
 
//************************************************
          queryTotal.append(condition);
          
          int rowIndex = 10;
          int rowIndex2 = 10;
          int rowIndex3 = 10;     
          
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
       	  
       // Код НП(С)
    	   String rowN2 = "d"+rowIndex2;
    	   cell = cells.get(rowN2);
    	   rowIndex2 = rowIndex2+1;
    	   
    	   
    	  cell.setValue(rs.getString(3));
    	  
    	  // из общей численности студентов (из гр. 19) – женщины
		  PreparedStatement stmt3 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like 'н' and a.pol = 'ж'");
    	  stmt3.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs3 = stmt3.executeQuery();
    	  if (rs3.next()){
    		  specs.add(new Integer(rs3.getInt(1)));
    		  String rowN3 = "e"+rowIndex3;
    		  cell = cells.get(rowN3);
    		  rowIndex3 = rowIndex3+1;
    		  cell.setValue(rs3.getString(1));
	  }
 
    	  
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