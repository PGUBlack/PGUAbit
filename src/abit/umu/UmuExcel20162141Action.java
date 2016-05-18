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

public class UmuExcel20162141Action extends Action {

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
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
         

          //��������� ����� ���� Excel � �������� � ���� ������        
           int lineIndex = 1;

          Workbook workbook = new Workbook();

          //��������� ����� ���� Excel � �������� � ���� ������
          Worksheet worksheet = workbook.getWorksheets().get(0);
          
          Cells cells = worksheet.getCells();
        
          Cell cell = cells.get("A1");
          cells.setColumnWidth(0, 130);
          cell.setValue("���������� ��������������� �����������");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 130);          
          cell.setValue("����� ��������"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 130);
          cell.setValue("������ 2.1.4. ������������� ����������� ��������� � ������� ������� ����������� ���������� (� ������������ � �������������� ���������� ���������� ���������, � ������������ �������� ��� ������������� �������������� ���������� ��������� ������) �� ������������ ���������� � ��������������");
          
          cell = cells.get("s5");
          cell.setValue("��� �� ����: ������� � 792");      
          
          cell = cells.get("e6");
          cell.setValue("����������� ��������� �� ������");     
          
          cell = cells.get("a8");
          cell.setValue("������������ ����������� ����������, �������������");
          
          cell = cells.get("b8");
          cell.setValue("� ������");        
          
          cell = cells.get("c8");
          cell.setValue("��� ��������������");   
          
          cell = cells.get("d8");
          cell.setValue("��� ��(�)");   
          
          cell = cells.get("e7");
          cell.setValue("1 ����");             

          cell = cells.get("g7");
          cell.setValue("2 ����"); 
          
          cell = cells.get("i7");
          cell.setValue("3 ����"); 
          
          cell = cells.get("k7");
          cell.setValue("4 ����"); 
          
          cell = cells.get("m7");
          cell.setValue("5 ����"); 
          
          cell = cells.get("o7");
          cell.setValue("6 ����");   
          
          cell = cells.get("q7");
          cell.setValue("7 ����");    
          
          cell = cells.get("s8");
          cell.setValue("����������� ��������� �� ���� ������ (����� ��. 5,7,9,11,13,15,17)");    
          
          cell = cells.get("t6");
          cell.setValue("�� ��� ��������� (�� ��.19):");
          
          cell = cells.get("e8");
          cell.setValue("�����");    
          
          cell = cells.get("f8");
          cell.setValue("�� ��� �� ���� ��������� ������������ ������������ �������");           
          
          cell = cells.get("g8");
          cell.setValue("�����"); 
          
          cell = cells.get("h8");
          cell.setValue("�� ��� �� ���� ��������� ������������ ������������ �������");      
          
          cell = cells.get("i8");
          cell.setValue("�����"); 
          
          cell = cells.get("j8");
          cell.setValue("�� ��� �� ���� ��������� ������������ ������������ �������");      
          
          cell = cells.get("k8");
          cell.setValue("�����"); 
          
          cell = cells.get("l8");
          cell.setValue("�� ��� �� ���� ��������� ������������ ������������ �������");      
          
          cell = cells.get("m8");
          cell.setValue("�����"); 
          
          cell = cells.get("n8");
          cell.setValue("�� ��� �� ���� ��������� ������������ ������������ �������");      
          
          cell = cells.get("o8");
          cell.setValue("�����");   
          
          cell = cells.get("p8");
          cell.setValue("�� ��� �� ���� ��������� ������������ ������������ �������");      
          
          cell = cells.get("q8");
          cell.setValue("�����");           
          
          cell = cells.get("r8");
          cell.setValue("�� ��� �� ���� ��������� ������������ ������������ �������");      
          
          cell = cells.get("t8");
          cell.setValue("�� ���� ��������� ������������ ������������ ������� (����� ��.6,8,10,12,14,16,18)");          
      
    	  // �������� ���������, ����� "���������" �� ��������
          cell = cells.get("u8");
          cell.setValue("�� ��������� �� �������� ������� ��������������� �����");  
     
          
          /****************************** ������� ******************************/
          StringBuffer queryTotal = new StringBuffer("select distinct s.kodspetsialnosti, s.nazvaniespetsialnosti, s.shifrspetsialnosti, s.edulevel FROM konkurs k, spetsialnosti s");
         
          queryTotal.append(" WHERE ");
          queryTotal.append("k.kodspetsialnosti = s.kodspetsialnosti"); 

//******** ������ ��������������� ������� **********

          StringBuffer condition = new StringBuffer();
 
//************************************************
          queryTotal.append(condition);
          
          int rowIndex = 10;
          int rowIndex2 = 10;
          int rowIndex3 = 10;     
          int rowIndex4 = 10;  
          int rowIndex5 = 10; 
          int rowIndex6 = 10; 
    	  // �������� ���������, ����� "���������" �� ��������
          int rowIndex7 = 10;     
          
          System.out.println(queryTotal);
          stmt = conn.createStatement();
          rs = stmt.executeQuery(queryTotal.toString());
          
  	  
          while(rs.next()){
        	  
         	 specs.add(new Integer(rs.getInt(1)));
         	// ������������ ����������� ����������, �������������
       	   String rowN = "a"+rowIndex;
       	   cell = cells.get(rowN);
       	   rowIndex = rowIndex+1;
       	   
       	   
       	  cell.setValue(rs.getString(2));
       	  
      	// ��� ��(�)
    	   String rowN2 = "d"+rowIndex2;
    	   cell = cells.get(rowN2);
    	   rowIndex2 = rowIndex2+1;
    	   
    	   
    	  cell.setValue(rs.getString(3));
    	  
    	  // 1 ���� - �����
		  PreparedStatement stmt3 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
    	  stmt3.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs3 = stmt3.executeQuery();
    	  if (rs3.next()){
    		  specs.add(new Integer(rs3.getInt(1)));
    		  String rowN3 = "e"+rowIndex3;
    		  cell = cells.get(rowN3);
    		  rowIndex3 = rowIndex3+1;
    		  cell.setValue(rs3.getString(1));
	  }
    	  
    	  // 1 ���� - �� ��� - �� ���� ��������� ������������ ������������ �������
		  PreparedStatement stmt4 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
    	  stmt4.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs4 = stmt4.executeQuery();
    	  if (rs4.next()){
    		  specs.add(new Integer(rs4.getInt(1)));
    		  String rowN4 = "f"+rowIndex4;
    		  cell = cells.get(rowN4);
    		  rowIndex4 = rowIndex4+1;
    		  cell.setValue(rs4.getString(1));
	  }    	  
    	  
    	  // ����������� ��������� �� ���� ������ (����� ��. 5,7,9,11,13,15,17)
		  PreparedStatement stmt5 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
    	  stmt5.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs5 = stmt5.executeQuery();
    	  if (rs5.next()){
    		  specs.add(new Integer(rs5.getInt(1)));
    		  String rowN5 = "s"+rowIndex5;
    		  cell = cells.get(rowN5);
    		  rowIndex5 = rowIndex5+1;
    		  cell.setValue(rs5.getString(1));
	  }
    	  
    	  // �� ���� ��������� ������������ ������������ ������� (����� ��.6,8,10,12,14,16,18)
		  PreparedStatement stmt6 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
    	  stmt6.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs6 = stmt6.executeQuery();
    	  if (rs6.next()){
    		  specs.add(new Integer(rs6.getInt(1)));
    		  String rowN6 = "t"+rowIndex6;
    		  cell = cells.get(rowN6);
    		  rowIndex6 = rowIndex6+1;
    		  cell.setValue(rs6.getString(1));
	  }    	  
    	  
    	  // �� ��������� �� �������� ������� ��������������� �����; �������� ���������, ����� "���������" �� ��������
		  PreparedStatement stmt7 = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a where kodspetsialnzach = ? and a.prinjat like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
    	  stmt7.setObject(1, rs.getInt(1), Types.INTEGER);
    	  rs7 = stmt7.executeQuery();
    	  if (rs7.next()){
    		  specs.add(new Integer(rs7.getInt(1)));
    		  String rowN7 = "u"+rowIndex7;
    		  cell = cells.get(rowN7);
    		  rowIndex7 = rowIndex7+1;
    		  cell.setValue(rs7.getString(1));
	  
	  }    	
    	  
    	  
   	  }
   
                
/************************************************/
/***** ����������� ����� � �������� ������� *****/
/************************************************/

        String ename = "����� EXCEL  "+StringUtil.CurrDate(".")+" �� "+StringUtil.CurrTime(":");

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