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

public class UmuExcel20162132Action extends Action {

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
          cells.setColumnWidth(0, 80);
          cell.setValue("���������� ��������������� �����������");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 80);          
          cell.setValue("����� ��������"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 80);
          cell.setValue("2.1.3. ������������� ������ ������� ����������� ���������� (� ������������ � �������������� ���������� ���������� ���������, � ������������ �������� ��� ������������� �������������� ���������� ��������� ������) �� ������������ ���������� � ��������������");
          
          cell = cells.get("d5");
          cell.setValue("��� �� ����: ������� � 792");           
                  
          cell = cells.get("d6");
          cell.setValue("������� �� �������� �� ����");
          
          cell = cells.get("a7");
          cell.setValue("������������ ����������� ����������, �������������");
                  
          cell = cells.get("b7");
          cell.setValue("� ������");
          
          cell = cells.get("c7");
          cell.setValue("�������(����� ��. 5 � 7)");
    
          cell = cells.get("d7");
          cell.setValue("��������� ������������ ������������ �������");
          
          cell = cells.get("e7");
          cell.setValue("��������� ������������ ������� �������� ���������� ���������");
          
          cell = cells.get("f7");
          cell.setValue("��������� ������������ �������� �������");     
          
          cell = cells.get("a9");
          cell.setValue("��������� ������������");  
          
          cell = cells.get("a10");
          cell.setValue("��������� ������������");  
                  
          cell = cells.get("a11");
          cell.setValue("��������� ������������ � �����");          
          
          cell = cells.get("a12");
          cell.setValue("����� �� ���������� ������� ����������� (����� ����� 01, 02, 03)");          
                          
          cell = cells.get("b9");
          cell.setValue("01"); 
          
          cell = cells.get("b10");
          cell.setValue("02");         
            
          cell = cells.get("b11");
          cell.setValue("03"); 
          
          cell = cells.get("b12");
          cell.setValue("04"); 

          // �������(����� ��. 5 � 7) - ��������� ������������
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN1 = "c9";
     	   cell = cells.get(rowN1);
     	   
     	  cell.setValue(rs.getString(1));
        }
              
        // �������(����� ��. 5 � 7) - ��������� ������������
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN2 = "c10";
     	   cell = cells.get(rowN2);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // �������(����� ��. 5 � 7) - ��������� ������������     
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN3 = "c11";
     	   cell = cells.get(rowN3);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
        // ��������� ������������ ������������ ������� - ��������� ������������
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN4 = "d9";
     	   cell = cells.get(rowN4);
     	   
     	  cell.setValue(rs.getString(1));
        }
              
        // ��������� ������������ ������������ ������� - ��������� ������������
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN5 = "d10";
     	   cell = cells.get(rowN5);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // ��������� ������������ ������������ ������� - ��������� ������������
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN6 = "d11";
     	   cell = cells.get(rowN6);
     	   
     	  cell.setValue(rs.getString(1));
        }     
        
        // �������(����� ��. 5 � 7) - ����� �� ���������� ������� ����������� (����� ����� 01, 02, 03)
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and s.edulevel in ('�', '�', '�') and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN7 = "c12";
     	   cell = cells.get(rowN7);
     	   
     	  cell.setValue(rs.getString(1));
        } 
        
        // ��������� ������������ ������������ ������� - ����� �� ���������� ������� ����������� (����� ����� 01, 02, 03)
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and s.edulevel in ('�', '�', '�') and a.prinjat not like '�' and a.prinjat not like '�' and a.grajdanstvo not like '��' and a.grajdanstvo not like '���������� ���������' and a.grajdanstvo not like '��� �����������'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN8 = "d12";
     	   cell = cells.get(rowN8);
     	   
     	  cell.setValue(rs.getString(1));
        } 
        
        // ����� ����� ��� ��������� ������������ ������� �������� ���������� ��������� � ��������� ������������ �������� �������
        cell = cells.get("e9");
        cell.setValue("0");
        
        cell = cells.get("e10");
        cell.setValue("0");
        
        cell = cells.get("e11");
        cell.setValue("0");
        
        cell = cells.get("e12");
        cell.setValue("0");
        
        cell = cells.get("f9");
        cell.setValue("0");
        
        cell = cells.get("f10");
        cell.setValue("0");
        
        cell = cells.get("f11");
        cell.setValue("0");
        
        cell = cells.get("f12");
        cell.setValue("0");
  
                        
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