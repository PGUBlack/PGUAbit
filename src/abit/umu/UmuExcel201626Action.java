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

public class UmuExcel201626Action extends Action {

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
          cells.setColumnWidth(0, 170);
          cell.setValue("���������� ��������������� �����������");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 170);          
          cell.setValue("����� ��������"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 170);
          cell.setValue("2.6. ���������� ������ �� ������ ����������� ������������");
          
          cell = cells.get("k5");
          cell.setValue("��� �� ����: ������� � 792");
          
          cell = cells.get("c6");
          cell.setValue("��������� ������������");
          
          cell = cells.get("g6");
          cell.setValue("��������� ������������");          
          
          cell = cells.get("k6");
          cell.setValue("��������� ������������");    
          
          cell = cells.get("d7");
          cell.setValue("�������");
          
          cell = cells.get("h7");
          cell.setValue("�������");
          
          cell = cells.get("l7");
          cell.setValue("�������");  
          
          cell = cells.get("e8");
          cell.setValue("�� ���");
                 
          cell = cells.get("i8");
          cell.setValue("�� ���");   
          
          cell = cells.get("m8");
          cell.setValue("�� ���");   
          
          cell = cells.get("b9");
          cell.setValue("� ������");
          
          cell = cells.get("c9");
          cell.setValue("������ ���������");

          cell = cells.get("d9");
          cell.setValue("�����");
          
          cell = cells.get("e9");
          cell.setValue("�� ���� ��������� ������������ ������������ �������");
          
          cell = cells.get("f9");
          cell.setValue("�� ��������� �� �������� ������� ��������������� �����");        
          
          cell = cells.get("g9");
          cell.setValue("������ ���������");

          cell = cells.get("h9");
          cell.setValue("�����");
          
          cell = cells.get("i9");
          cell.setValue("�� ���� ��������� ������������ ������������ �������");
          
          cell = cells.get("j9");
          cell.setValue("�� ��������� �� �������� ������� ��������������� �����");        

          cell = cells.get("k9");
          cell.setValue("������ ���������");

          cell = cells.get("l9");
          cell.setValue("�����");
          
          cell = cells.get("m9");
          cell.setValue("�� ���� ��������� ������������ ������������ �������");
          
          cell = cells.get("n9");
          cell.setValue("�� ��������� �� �������� ������� ��������������� �����");   
          
          cell = cells.get("a11");
          cell.setValue("����� (����� ����� 02, 06, 08, 10, 12, 14)"); 
          
          cell = cells.get("a12");
          cell.setValue("� ��� ����� ����� �����������: ������� �����");
                  
          cell = cells.get("a13");
          cell.setValue("�� ��� (�� ������ 02) �������� ��������� ����������� � ������� ����");
          
          cell = cells.get("a14");
          cell.setValue("�� ��� (�� ������ 03) ����������: ������������������� �����������");
          
          cell = cells.get("a15");
          cell.setValue("� ��� ����� (�� ������ 04) ���������� ����������� (�������������) ��������������� ����������� � ������� ��� �����������, ������������� � ������������� ������������� ��������");
          
          cell = cells.get("a16");
          cell.setValue("������� ���������������� ����������� �� ���������� ���������� ����������������� ������� (��������)");
          
          cell = cells.get("a17");
          cell.setValue("�� ��� �������� ��������� ����������� � ������� ����");
          
          cell = cells.get("a18");
          cell.setValue("������� ���������������� ����������� �� ���������� ���������� ������������ �������� �����");          
          
          cell = cells.get("a19");
          cell.setValue("�� ��� �������� ��������� ����������� � ������� ����");     
          
          cell = cells.get("a20");
          cell.setValue("������, �������������� ��������: ���������"); 

          cell = cells.get("a21");
          cell.setValue("�� ��� �������� ��������� ����������� � ������� ����");
          
          cell = cells.get("a22");
          cell.setValue("- �����������");         
          
          cell = cells.get("a23");
          cell.setValue("�� ��� �������� ��������� ����������� � ������� ����");
          
          cell = cells.get("a24");
          cell.setValue("- ��������");   
          
          cell = cells.get("a25");
          cell.setValue("�� ��� �������� ��������� ����������� � ������� ����");
          
          cell = cells.get("b11");
          cell.setValue("01"); 
          
          cell = cells.get("b12");
          cell.setValue("02");
                  
          cell = cells.get("b13");
          cell.setValue("03");
          
          cell = cells.get("b14");
          cell.setValue("04");
          
          cell = cells.get("b15");
          cell.setValue("05");
          
          cell = cells.get("b16");
          cell.setValue("06");
          
          cell = cells.get("b17");
          cell.setValue("07");
          
          cell = cells.get("b18");
          cell.setValue("08");          
          
          cell = cells.get("b19");
          cell.setValue("09");     
          
          cell = cells.get("b20");
          cell.setValue("10"); 

          cell = cells.get("b21");
          cell.setValue("11");
          
          cell = cells.get("b22");
          cell.setValue("12");         
          
          cell = cells.get("b23");
          cell.setValue("13");
          
          cell = cells.get("b24");
          cell.setValue("14");   
          
          cell = cells.get("b25");
          cell.setValue("15");
          
          cell = cells.get("k12");
          cell.setValue("X");
          
          cell = cells.get("l12");
          cell.setValue("X");
          
          cell = cells.get("m12");
          cell.setValue("X");
          
          cell = cells.get("n12");
          cell.setValue("X");   
          
          cell = cells.get("k13");
          cell.setValue("X");
          
          cell = cells.get("l13");
          cell.setValue("X");
          
          cell = cells.get("m13");
          cell.setValue("X");
          
          cell = cells.get("n13");
          cell.setValue("X");  
          
          cell = cells.get("k14");
          cell.setValue("X");
          
          cell = cells.get("l14");
          cell.setValue("X");
          
          cell = cells.get("m14");
          cell.setValue("X");
          
          cell = cells.get("n14");
          cell.setValue("X");  
          
          cell = cells.get("k15");
          cell.setValue("X");
          
          cell = cells.get("l15");
          cell.setValue("X");
          
          cell = cells.get("m15");
          cell.setValue("X");
          
          cell = cells.get("n15");
          cell.setValue("X");  
          
          cell = cells.get("k16");
          cell.setValue("X");
          
          cell = cells.get("l16");
          cell.setValue("X");
          
          cell = cells.get("m16");
          cell.setValue("X");
          
          cell = cells.get("n16");
          cell.setValue("X");  
          
          cell = cells.get("k17");
          cell.setValue("X");
          
          cell = cells.get("l17");
          cell.setValue("X");
          
          cell = cells.get("m17");
          cell.setValue("X");
          
          cell = cells.get("n17");
          cell.setValue("X");  
          
          cell = cells.get("k18");
          cell.setValue("X");
          
          cell = cells.get("l18");
          cell.setValue("X");
          
          cell = cells.get("m18");
          cell.setValue("X");
          
          cell = cells.get("n18");
          cell.setValue("X");  
          
          cell = cells.get("k19");
          cell.setValue("X");
          
          cell = cells.get("l19");
          cell.setValue("X");
          
          cell = cells.get("m19");
          cell.setValue("X");
          
          cell = cells.get("n19");
          cell.setValue("X");  
          
          // ������ ��������� (�)
          pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN1 = "c11";
       	   cell = cells.get(rowN1);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ������� (�)          
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN2 = "d11";
       	   cell = cells.get(rowN2);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN3 = "e11";
       	   cell = cells.get(rowN3);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (�)       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN4 = "f11";
       	   cell = cells.get(rowN4);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ������ ��������� (�)
          pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN5 = "g11";
       	   cell = cells.get(rowN5);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ������� (�)          
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN6 = "h11";
       	   cell = cells.get(rowN6);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN7 = "i11";
       	   cell = cells.get(rowN7);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (�)       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN8 = "j11";
       	   cell = cells.get(rowN8);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ������ ��������� (�)
          pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN9 = "k11";
       	   cell = cells.get(rowN9);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ������� (�)          
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN10 = "l11";
       	   cell = cells.get(rowN10);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN11 = "m11";
       	   cell = cells.get(rowN11);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (�)       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN12 = "n11";
       	   cell = cells.get(rowN12);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ���: ����� ������� ����� ����������� (�) (�.�. �� ���������� �� �����������, ������� ����� �������� � ������� ����������� ����� "�������� ���" ��� "�������� ��� � ��������")
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN13 = "d12";
       	   cell = cells.get(rowN13);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (������� ������� ����� �����������) (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN14 = "e12";
       	   cell = cells.get(rowN14);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (������� ������� ����� �����������) (�)       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN15 = "f12";
       	   cell = cells.get(rowN15);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ���: ����� ������� ����� ����������� (�) (�.�. �� ���������� �� �����������, ������� ����� �������� � ������� ����������� ����� "�������� ���" ��� "�������� ��� � ��������")
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN16 = "h12";
       	   cell = cells.get(rowN16);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (������� ������� ����� �����������) (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN17 = "i12";
       	   cell = cells.get(rowN17);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (������� ������� ����� �����������) (�)       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN18 = "j12";
       	   cell = cells.get(rowN18);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ���: �������� ��������� ����������� � ������� ���� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN19 = "d13";
       	   cell = cells.get(rowN19);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (�������� ��������� ����������� � ������� ����) (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')  and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN20 = "e13";
       	   cell = cells.get(rowN20);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (�������� ��������� ����������� � ������� ����) (�)       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN21 = "f13";
       	   cell = cells.get(rowN21);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ���: �������� ��������� ����������� � ������� ���� (c)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN22 = "h13";
       	   cell = cells.get(rowN22);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (�������� ��������� ����������� � ������� ����) (c)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')  and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN23 = "i13";
       	   cell = cells.get(rowN23);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (�������� ��������� ����������� � ������� ����) (c)       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN24 = "j13";
       	   cell = cells.get(rowN24);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ���: �������� ��������� ����������� � ������� ���� (�) - ������������������� �����������
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015' and a.tipokonchennogozavedenija in ('�', '�', '�', '�', '�', '�')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN25 = "d14";
       	   cell = cells.get(rowN25);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (�������� ��������� ����������� � ������� ����) (�) - ������������������� �����������
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')  and a.godokonchanijasrobrazovanija = '2015' and a.tipokonchennogozavedenija in ('�', '�', '�', '�', '�', '�')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN26 = "e14";
       	   cell = cells.get(rowN26);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (�������� ��������� ����������� � ������� ����) (�) - ������������������� �����������      
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015' and a.tipokonchennogozavedenija in ('�', '�', '�', '�', '�', '�')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN27 = "f14";
       	   cell = cells.get(rowN27);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ���: �������� ��������� ����������� � ������� ���� (c) - ������������������� �����������
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015' and a.tipokonchennogozavedenija in ('�', '�', '�', '�', '�', '�')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN28 = "h14";
       	   cell = cells.get(rowN28);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // �� ���� ��������� ������������ ������������ ������� (�������� ��������� ����������� � ������� ����) (c) - ������������������� �����������
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������')  and a.godokonchanijasrobrazovanija = '2015' and a.tipokonchennogozavedenija in ('�', '�', '�', '�', '�', '�')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN29 = "i14";
       	   cell = cells.get(rowN29);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // �� ��������� �� �������� ������� ��������������� ����� (�������� ��������� ����������� � ������� ����) (c) - ������������������� �����������       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz not in ('�������� ���', '�������� ��� � ��������') and a.godokonchanijasrobrazovanija = '2015' and a.tipokonchennogozavedenija in ('�', '�', '�', '�', '�', '�')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN30 = "j14";
       	   cell = cells.get(rowN30);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ����� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN31 = "d18";
       	   cell = cells.get(rowN31);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ������ - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN32 = "e18";
       	   cell = cells.get(rowN32);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ������� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)      
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN33 = "f18";
       	   cell = cells.get(rowN33);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ����� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN34 = "h18";
       	   cell = cells.get(rowN34);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ������ - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN35 = "i18";
       	   cell = cells.get(rowN35);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ������� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)             
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���')");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN36 = "j18";
       	   cell = cells.get(rowN36);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ����� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN37 = "d19";
       	   cell = cells.get(rowN37);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ������ - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN38 = "e19";
       	   cell = cells.get(rowN38);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ������� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)      
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN39 = "f19";
       	   cell = cells.get(rowN39);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ����� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN40 = "h19";
       	   cell = cells.get(rowN40);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ������ - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN41 = "i19";
       	   cell = cells.get(rowN41);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // ������� - ������� ���������������� ����������� �� ���������� ���������� ������������ �������� ����� (�)             
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat like '�'  and a.prinjat not like '�' and a.viddoksredobraz in ('������ ���') and a.godokonchanijasrobrazovanija = '2015'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN42 = "j19";
       	   cell = cells.get(rowN42);
       	   
       	  cell.setValue(rs.getString(1));
          }  
          
          // ����� �� ��������� "������, ������������� �������� ���������, �����������, ��������?
          // ����� ����, ����� ��������� ������ ���� "select * from abiturient where stepen_mag = '�' and prinjat not in ('�') and tipokonchennogozavedenija not in ('�')" ?
          
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