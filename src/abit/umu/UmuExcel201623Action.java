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

public class UmuExcel201623Action extends Action {

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
          cells.setColumnWidth(0, 120);
          cell.setValue("���������� ��������������� �����������");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 120);          
          cell.setValue("����� ��������"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 120);
          cell.setValue("2.3. ������������� ����������� ���������, ������ � ������� �� ���������� �������������� ��������.");
          
          cell = cells.get("A5");
          cells.setColumnWidth(0, 120);
          cell.setValue("������� ����� � ������� ��������. �������� � ��������� � ��� � ���������");        
          
          cell = cells.get("i6");
          cell.setValue("��� �� ����: ������� � 792");     
          
          cell = cells.get("c7");
          cell.setValue("��������� ������������");
          
          cell = cells.get("f7");
          cell.setValue("��������� ������������");
          
          cell = cells.get("i7");
          cell.setValue("��������� ������������");
      
          cell = cells.get("b8");
          cell.setValue("� ������");

          cell = cells.get("c8");
          cell.setValue("�������");
          
          cell = cells.get("d8");
          cell.setValue("����������� ���������");
                    
          cell = cells.get("e8");
          cell.setValue("������");         
          
          cell = cells.get("f8");
          cell.setValue("�������");
          
          cell = cells.get("g8");
          cell.setValue("����������� ���������");
                    
          cell = cells.get("h8");
          cell.setValue("������");   
          
          cell = cells.get("i8");
          cell.setValue("�������");
          
          cell = cells.get("j8");
          cell.setValue("����������� ���������");
                    
          cell = cells.get("k8");
          cell.setValue("������");        
          
          cell = cells.get("a10");
          cell.setValue("����� (����� ����� 02 � 05)");  

          cell = cells.get("a11");
          cell.setValue("� ��� ����� ��������, �����������: "); 

          cell = cells.get("a12");
          cell.setValue("�� ���� ��������� ������������ ������������ �������"); 

          cell = cells.get("a13");
          cell.setValue("�� ���� ��������� ������������ ������� �������� ���������� ���������");
          
          cell = cells.get("a14");
          cell.setValue("�� ���� ��������� ������������ �������� �������");  
          
          cell = cells.get("a15");
          cell.setValue("�� ��������� �� �������� ������� ��������������� ����� (����� ����� 06 � 08)");    

          cell = cells.get("a16");
          cell.setValue("� ��� ����� �� ���� �������: ");
          
          cell = cells.get("a17");
          cell.setValue("���������� ���");          
          
          cell = cells.get("a18");
          cell.setValue("����������� ���");
          
          cell = cells.get("a19");
          cell.setValue("���������� � ����������� ���");          
          
          cell = cells.get("a20");
          cell.setValue("��������� ��������� � ������������ � ��������������� ������ ���������� ������������ ��� ����������� ��� (�� ������ 02)"); 
          
          cell = cells.get("a21");
          cell.setValue("��������, ����������� �� �������� �������� ������ (�� ����� ����� 02 � 04)");     
    
          cell = cells.get("a22");
          cell.setValue("��������, ����������� �� �������� �������� �������� (�� ������ 01)");
          
          cell = cells.get("a23");
          cell.setValue("��������, ���������� ������ ������ ����������� (�� ������ 01)");
          
          cell = cells.get("a24");
          cell.setValue("����������� ��������� � ������������� ������������� �������� (���) (�� ������ 01)"); 
          
          cell = cells.get("a25");
          cell.setValue("�� ��� ��������� �� �������������� ��������������� ����������");     
          
          cell = cells.get("a26");
          cell.setValue("����������� ��������� (�� ������ 01; ����� ����� 16 � 21)");      
          
          cell = cells.get("a27");
          cell.setValue("� ��� �����:");        
          
          cell = cells.get("a28");
          cell.setValue("� ����������� ������");  
          
          cell = cells.get("a29");
          cell.setValue("� ����������� ����� � ����");  
          
          cell = cells.get("a30");
          cell.setValue("� ����������� ������-������������� ��������"); 
          
          cell = cells.get("a31");
          cell.setValue("� ������������� �����������");    
          
          cell = cells.get("a32");
          cell.setValue("� �������������� �������������� �������");   
          
          cell = cells.get("a33");
          cell.setValue("� ������� ����������� (����� �������� � ������� 16 � 20)");    
          
          cell = cells.get("a34");
          cell.setValue("�� ��� (�� ������ 15) ��������� �� �������������� ��������������� ����������");       
          
          cell = cells.get("a35");
          cell.setValue("����������� �����-��������� (�� ������ 15)");             
          
          cell = cells.get("a36");
          cell.setValue("�� ��� ��������� �� �������������� ��������������� ����������");       
                          
          cell = cells.get("b10");
          cell.setValue("01"); 
          
          cell = cells.get("b12");
          cell.setValue("02");         
            
          cell = cells.get("b13");
          cell.setValue("03"); 

          cell = cells.get("b14");
          cell.setValue("04");    
          
          cell = cells.get("b15");
          cell.setValue("05"); 
          
          cell = cells.get("b17");
          cell.setValue("06");         
            
          cell = cells.get("b18");
          cell.setValue("07"); 

          cell = cells.get("b19");
          cell.setValue("08");  
          
          cell = cells.get("b20");
          cell.setValue("09");    
          
          cell = cells.get("b21");
          cell.setValue("10"); 
          
          cell = cells.get("b22");
          cell.setValue("11");         
            
          cell = cells.get("b23");
          cell.setValue("12"); 

          cell = cells.get("b24");
          cell.setValue("13");  
          
          cell = cells.get("b25");
          cell.setValue("14"); 

          cell = cells.get("b26");
          cell.setValue("15");      
          
          cell = cells.get("b28");
          cell.setValue("16");  
          
          cell = cells.get("b29");
          cell.setValue("17");    
          
          cell = cells.get("b30");
          cell.setValue("18"); 
          
          cell = cells.get("b31");
          cell.setValue("19");         
            
          cell = cells.get("b32");
          cell.setValue("20"); 

          cell = cells.get("b33");
          cell.setValue("21");  
          
          cell = cells.get("b34");
          cell.setValue("22"); 

          cell = cells.get("b35");
          cell.setValue("23");      
          
          cell = cells.get("b36");
          cell.setValue("24");  
          
          cell = cells.get("c22");
          cell.setValue("X");   
          
          cell = cells.get("f22");
          cell.setValue("X");  
        
          cell = cells.get("i22");
          cell.setValue("X");  
          
          cell = cells.get("e35");
          cell.setValue("X"); 
          
          cell = cells.get("h35");
          cell.setValue("X");    
          
          cell = cells.get("i35");
          cell.setValue("X"); 
          
          cell = cells.get("j35");
          cell.setValue("X"); 
          
          cell = cells.get("k35");
          cell.setValue("X"); 
          
          cell = cells.get("e36");
          cell.setValue("X"); 
          
          cell = cells.get("h36");
          cell.setValue("X");    
          
          cell = cells.get("i36");
          cell.setValue("X"); 
          
          cell = cells.get("j36");
          cell.setValue("X"); 
          
          cell = cells.get("k36");
          cell.setValue("X"); 
          
          // ������� (�����������) - ����� (����� ����� 02 � 05)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�'");       
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN1 = "c10";
     	   cell = cells.get(rowN1);
     	   
     	  cell.setValue(rs.getString(1));
          }
        
          // ������� (�����������) - �� ���� ��������� ������������ ������������ �������
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN2 = "c12";
     	   cell = cells.get(rowN2);
     	   
     	  cell.setValue(rs.getString(1));
          }

          // ������� (�����������) - �� ��������� �� �������� ������� ��������������� ����� (����� ����� 06 � 08)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN3 = "c15";
     	   cell = cells.get(rowN3);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // ������� (�����������) - ����� (����� ����� 02 � 05) 
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�'");       
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN4 = "f10";
     	   cell = cells.get(rowN4);
     	   
     	  cell.setValue(rs.getString(1));
          }
        
          // ������� (�����������) - �� ���� ��������� ������������ ������������ �������
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN5 = "f12";
     	   cell = cells.get(rowN5);
     	   
     	  cell.setValue(rs.getString(1));
          }

          // ������� (�����������) - �� ��������� �� �������� ������� ��������������� ����� (����� ����� 06 � 08)         
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN6 = "f15";
     	   cell = cells.get(rowN6);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // ������� (������������) - ����� (����� ����� 02 � 05)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�'");       
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN7 = "i10";
     	   cell = cells.get(rowN7);
     	   
     	  cell.setValue(rs.getString(1));
          }
        
          // ������� (������������) - �� ���� ��������� ������������ ������������ �������
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat not like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN8 = "i12";
     	   cell = cells.get(rowN8);
     	   
     	  cell.setValue(rs.getString(1));
          }

          // ������� (������������) - �� ��������� �� �������� ������� ��������������� ����� (����� ����� 06 � 08) 
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN9 = "i15";
     	   cell = cells.get(rowN9);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // ������� (�����������) - ���������� ����
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN10 = "c17";
     	   cell = cells.get(rowN10);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // ������� (�����������) - ���������� ����
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN11 = "f17";
     	   cell = cells.get(rowN11);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // ������� (������������) - ���������� ����
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = '�' and a.prinjat not like '�' and a.prinjat like '�'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN12 = "i17";
     	   cell = cells.get(rowN12);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // ������� (�����������) - ������� ����
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like '�') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN13 = "c21";
             	   cell = cells.get(rowN13);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // ������� (�����������) - ������� ����
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like '�') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
          		  rs = pstmt.executeQuery();
          		  while(rs.next()){
          		  String rowN14 = "f21";
          		  cell = cells.get(rowN14);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // ������� (������������) - ������� ����
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like '�') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN15 = "i21";
             	   cell = cells.get(rowN15);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
                 // ������ ������ ����� �������� � c23, f23, i23 ��� ���???
               
           // ������� (�����������) - ����������� ��������� 
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN16 = "c26";
             	   cell = cells.get(rowN16);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
           // ������� (�����������) - ����������� ��������� 
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN17 = "f26";
             	   cell = cells.get(rowN17);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // ������� (������������) - ����������� ���������    
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN18 = "i26";
             	   cell = cells.get(rowN18);
             	   
             	  cell.setValue(rs.getString(1));
                  }                 
                     
          // ������� (�����������) - ����������� ��������� (� ������� �����������)        
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN19 = "c33";
             	   cell = cells.get(rowN19);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // ������� (�����������) - ����������� ��������� (� ������� �����������)  
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN20 = "f33";
             	   cell = cells.get(rowN20);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // ������� (������������) - ����������� ��������� (� ������� �����������) 
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN21 = "i33";
             	   cell = cells.get(rowN21);
             	   
             	  cell.setValue(rs.getString(1));
                  }       
                  
          // ������� (�����������) - ����������� �����-��������� (�� 18 ��� - ���� �������� - �� 01.09.1997 �. ???)        
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�' and (abiturient.datarojdenija like ('%09-1997') or abiturient.datarojdenija like ('%10-1997') or abiturient.datarojdenija like ('%11-1997') or abiturient.datarojdenija like ('%12-1997') or abiturient.datarojdenija like ('%1998') or abiturient.datarojdenija like ('%1999') or abiturient.datarojdenija like ('%2000') or abiturient.datarojdenija like ('%2001'))");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN22 = "c35";
             	   cell = cells.get(rowN22);
             	   
             	  cell.setValue(rs.getString(1));
                  }
          
          // ������� (�����������) - ����������� �����-��������� (�� 18 ��� - ���� �������� - �� 01.09.1997 �. ???)          
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('�','�','�') AND kodOsnovyOb IN ('�', '�') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('�') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = '�' and (abiturient.datarojdenija like ('%09-1997') or abiturient.datarojdenija like ('%10-1997') or abiturient.datarojdenija like ('%11-1997') or abiturient.datarojdenija like ('%12-1997') or abiturient.datarojdenija like ('%1998') or abiturient.datarojdenija like ('%1999') or abiturient.datarojdenija like ('%2000') or abiturient.datarojdenija like ('%2001'))");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN23 = "f35";
             	   cell = cells.get(rowN23);
             	   
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