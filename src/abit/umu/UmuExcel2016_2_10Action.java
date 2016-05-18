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

public class UmuExcel2016_2_10Action extends Action {

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
          cells.setColumnWidth(0, 150);
          cell.setValue("���������� ��������������� �����������");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 150);          
          cell.setValue("����� ��������"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 150);          
          cell.setValue("2.10. ������������� ����������� ���������, ������ � ������� �� �����������");  
          
          cell = cells.get("i5");
          cell.setValue("��� �� ����: ������� � 792");             
   
          cell = cells.get("d6");
          cell.setValue("�������");
          
          cell = cells.get("g6");
          cell.setValue("����������� ���������");    
          
          cell = cells.get("j6");
          cell.setValue("������");             
          
          cell = cells.get("e7");
          cell.setValue("�� ��� (�� ��. 4):");
          
          cell = cells.get("h7");
          cell.setValue("�� ��� (�� ��. 7):");     
  
          cell = cells.get("k7");
          cell.setValue("�� ��� (�� ��. 10):");   
          
          cell = cells.get("b8");
          cell.setValue("� ������"); 
          
          cell = cells.get("c8");
          cell.setValue("��� ����������� �� ����");
        
          cell = cells.get("d8");
          cell.setValue("�����");
         
          cell = cells.get("e8");   
          cell.setValue("�� ���� ��������� ������������ ������������ �������");
          
          cell = cells.get("f8");
          cell.setValue("�� ��������� �� �������� ������� ��������������� �����");
        
          cell = cells.get("g8");
          cell.setValue("�����");
         
          cell = cells.get("h8");   
          cell.setValue("�� ���� �������� ������������ ������������ �������");        

          cell = cells.get("i8");
          cell.setValue("�� ��������� �� �������� ������� ��������������� �����");
        
          cell = cells.get("j8");
          cell.setValue("�����");
         
          cell = cells.get("k8");   
          cell.setValue("�� ���� ��������� ������������ ������������ �������");
          
          cell = cells.get("l8");   
          cell.setValue("�� ��������� �� �������� ������� ��������������� �����");
          
  
          cell = cells.get("a10");   
          cell.setValue("��������, ����������� �� �������� ������ ������ � ����� (����� ����� 02, 03, 04)");  
          
          cell = cells.get("a11");   
          cell.setValue("�� ���: c������� �� ����� ���, ������, ������, ������� � ����� ������ � �����");  
          
          cell = cells.get("a12");   
          cell.setValue("�������� ������ ����������� ���������� (����� ���, ������, ������, ������� � ����� ������), ����������� �� �������� ������ ������ � �����");  
          
          cell = cells.get("a13");   
          cell.setValue("���� ��� �����������");  
          
          cell = cells.get("a14");   
          cell.setValue("����� ����: ����������� �������� �� ����� ���, ������, ������, ������� � ����� ������, ����������� �� ������������� ��������� � �����");  
     
          cell = cells.get("a15");   
          cell.setValue("�������� ������ ����������� ���������� (����� ���, ������, ������, ������� � ����� ������), ����������� �� ������������� ��������� � �����");  

          cell = cells.get("a16");   
          cell.setValue("���� ��� �����������, ����������� �� ������������� ���������");  

          cell = cells.get("b10");   
          cell.setValue("01");  
      
          cell = cells.get("b11"); 
          cell.setValue("02");
          
          cell = cells.get("b12"); 
          cell.setValue("03");     
          
          cell = cells.get("b13"); 
          cell.setValue("04");     
          
          cell = cells.get("b14"); 
          cell.setValue("05");     
          
          cell = cells.get("b15"); 
          cell.setValue("06");     
          
          cell = cells.get("b16"); 
          cell.setValue("07");    
          
          cell = cells.get("c10");   
          cell.setValue("0");  
      
          cell = cells.get("c11"); 
          cell.setValue("0");
          
          cell = cells.get("c12"); 
          cell.setValue("0");     
          
          cell = cells.get("c13"); 
          cell.setValue("0");     
          
          cell = cells.get("c14"); 
          cell.setValue("0");     
          
          cell = cells.get("c15"); 
          cell.setValue("0");     
          
          cell = cells.get("c16"); 
          cell.setValue("0");    
          
          cell = cells.get("f14"); 
          cell.setValue("X");     
          
          cell = cells.get("f15"); 
          cell.setValue("X");     
          
          cell = cells.get("f16"); 
          cell.setValue("X");
          
          cell = cells.get("i14"); 
          cell.setValue("X");     
          
          cell = cells.get("i15"); 
          cell.setValue("X");     
          
          cell = cells.get("i16"); 
          cell.setValue("X");
          
          cell = cells.get("l14"); 
          cell.setValue("X");     
          
          cell = cells.get("l15"); 
          cell.setValue("X");     
          
          cell = cells.get("l16"); 
          cell.setValue("X");
         
         
         // ��������, ����������� �� �������� ������ ������ � ����� (����� ����� 02, 03, 04)
         // ����������� �� ������� ���� ��� ����� ���� ��������� �������?
          pstmt = conn.prepareStatement("select count (*) from abiturient where prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN1 = "d10";
       	   cell = cells.get(rowN1);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
       // �� ���: c������� �� ����� ���, ������, ������, ������� � ����� ������ � �����       
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo in ('���������� ���������', '��', '������', '�������', '����������', '��������', '���������� ��������', '��������', '�������', '������', '�����', '�������', '������', '�������', '�����������', '���������', '��������', '����������', '���������', '������������', '����������', '�����������') and prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN2 = "d11";
       	   cell = cells.get(rowN2);
       	   
       	  cell.setValue(rs.getString(1));
          }    
          
       // �������� ������ ����������� ���������� (����� ���, ������, ������, ������� � ����� ������), ����������� �� �������� ������ ������ � �����    
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo not in ('���������� ���������', '��', '������', '�������', '����������', '��������', '���������� ��������', '��������', '�������', '������', '�����', '�������', '������', '�������', '�����������', '���������', '��������', '����������', '���������', '������������', '����������', '�����������', '��� �����������') and prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN3 = "d12";
       	   cell = cells.get(rowN3);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
       // ���� ��� ����������� - �����  
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo like '��� �����������' and prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN4 = "d13";
       	   cell = cells.get(rowN4);
       	   
       	  cell.setValue(rs.getString(1));
          }
      
       // ��������, ����������� �� �������� ������ ������ � ����� (����� ����� 02, 03, 04) - �� ���� �������� ������������ ������������ �������   
          pstmt = conn.prepareStatement("select count (*) from abiturient where prinjat not like '�' and prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN5 = "e10";
       	   cell = cells.get(rowN5);
       	   
       	  cell.setValue(rs.getString(1));
          }
      
       //  �� ���: c������� �� ����� ���, ������, ������, ������� � ����� ������ � �� ���� �������� ������������ ������������ ������� 
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo in ('���������� ���������', '��', '������', '�������', '����������', '��������', '���������� ��������', '��������', '�������', '������', '�����', '�������', '������', '�������', '�����������', '���������', '��������', '����������', '���������', '������������', '����������', '�����������') and prinjat not like '�' and prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN6 = "e11";
       	   cell = cells.get(rowN6);
       	   
       	  cell.setValue(rs.getString(1));
          }    
       
       // �������� ������ ����������� ���������� (����� ���, ������, ������, ������� � ����� ������), ����������� �� �������� ������ ������ - �� ���� �������� ������������ ������������ �������   
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo not in ('���������� ���������', '��', '������', '�������', '����������', '��������', '���������� ��������', '��������', '�������', '������', '�����', '�������', '������', '�������', '�����������', '���������', '��������', '����������', '���������', '������������', '����������', '�����������', '��� �����������') and prinjat not like '�' and prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN7 = "e12";
       	   cell = cells.get(rowN7);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
       // ���� ��� ����������� - �� ���� �������� ������������ ������������ �������   
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo like '��� �����������' and prinjat not like '�' and prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN8 = "e13";
       	   cell = cells.get(rowN8);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
       // ��������, ����������� �� �������� ������ ������ � ����� (����� ����� 02, 03, 04) - �� ��������� �� �������� ������� ��������������� �����   
          pstmt = conn.prepareStatement("select count (*) from abiturient where prinjat not like '�' and prinjat like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN9 = "f10";
       	   cell = cells.get(rowN9);
       	   
       	  cell.setValue(rs.getString(1));
          }
       
       //  �� ���: c������� �� ����� ���, ������, ������, ������� � ����� ������ � �� ��������� �� �������� ������� ��������������� �����  
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo in ('���������� ���������', '��', '������', '�������', '����������', '��������', '���������� ��������', '��������', '�������', '������', '�����', '�������', '������', '�������', '�����������', '���������', '��������', '����������', '���������', '������������', '����������', '�����������') and prinjat not like '�' and prinjat like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN10 = "f11";
       	   cell = cells.get(rowN10);
       	   
       	  cell.setValue(rs.getString(1));
          }  
          
       // �������� ������ ����������� ���������� (����� ���, ������, ������, ������� � ����� ������), ����������� �� �������� ������ ������ - �� ��������� �� �������� ������� ��������������� �����    
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo not in ('���������� ���������', '��', '������', '�������', '����������', '��������', '���������� ��������', '��������', '�������', '������', '�����', '�������', '������', '�������', '�����������', '���������', '��������', '����������', '���������', '������������', '����������', '�����������', '��� �����������') and prinjat not like '�' and prinjat like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN11 = "f12";
       	   cell = cells.get(rowN11);
       	   
       	  cell.setValue(rs.getString(1));
          }
       
       // ���� ��� ����������� - �� ��������� �� �������� ������� ��������������� �����   
          pstmt = conn.prepareStatement("select count (*) from abiturient where grajdanstvo like '��� �����������' and prinjat not like '�' and prinjat like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN12 = "f13";
       	   cell = cells.get(rowN12);
       	   
       	  cell.setValue(rs.getString(1));
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