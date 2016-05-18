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

public class UmuExcel2016_2_11Action extends Action {

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
          cells.setColumnWidth(0, 80);
          cell.setValue("Пензенский государственный университет");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 80);          
          cell.setValue("Очное обучение"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 80);          
          cell.setValue("2.11. Распределение численности студентов, приема и выпуска по возрасту и полу");  
          
          cell = cells.get("i5");
          cell.setValue("Код по ОКЕИ: человек – 792");             
   
          cell = cells.get("c6");
          cell.setValue("Принято");
          
          cell = cells.get("f6");
          cell.setValue("Численность студентов");    
          
          cell = cells.get("i6");
          cell.setValue("Выпуск");             
          
          cell = cells.get("d7");
          cell.setValue("из них");
          
          cell = cells.get("g7");
          cell.setValue("из них");     
  
          cell = cells.get("j7");
          cell.setValue("из них");   
          
          cell = cells.get("b8");
          cell.setValue("№ строки"); 
          
          cell = cells.get("c8");
          cell.setValue("Всего");
        
          cell = cells.get("d8");
          cell.setValue("по договорам об оказании платных образовательных услуг");
         
          cell = cells.get("e8");   
          cell.setValue("женщины");
          
          cell = cells.get("f8");
          cell.setValue("Всего");
        
          cell = cells.get("g8");
          cell.setValue("по договорам об оказании платных образовательных услуг");
         
          cell = cells.get("h8");   
          cell.setValue("женщины");        

          cell = cells.get("i8");
          cell.setValue("Всего");
        
          cell = cells.get("j8");
          cell.setValue("по договорам об оказании платных образовательных услуг");
         
          cell = cells.get("k8");   
          cell.setValue("женщины");
        
          cell = cells.get("a10");   
          cell.setValue("Всего");  
      
          cell = cells.get("a11"); 
          cell.setValue("в том числе в возрасте (число полных лет на 1 января): 15");
          
          cell = cells.get("a12"); 
          cell.setValue("16");     
          
          cell = cells.get("a13"); 
          cell.setValue("17");     
          
          cell = cells.get("a14"); 
          cell.setValue("18");     
          
          cell = cells.get("a15"); 
          cell.setValue("19");     
          
          cell = cells.get("a16"); 
          cell.setValue("20");     
          
          cell = cells.get("a17"); 
          cell.setValue("21");   
          
          cell = cells.get("a18"); 
          cell.setValue("22");     
          
          cell = cells.get("a19"); 
          cell.setValue("23");     
          
          cell = cells.get("a20"); 
          cell.setValue("24");     
          
          cell = cells.get("a21"); 
          cell.setValue("25");     
          
          cell = cells.get("a22"); 
          cell.setValue("26");     
          
          cell = cells.get("a23"); 
          cell.setValue("27");   
          
          cell = cells.get("a24"); 
          cell.setValue("28");     
          
          cell = cells.get("a25"); 
          cell.setValue("29");   
          
          cell = cells.get("a26"); 
          cell.setValue("30-34");           
          
          cell = cells.get("a27"); 
          cell.setValue("35-39");       
          
          cell = cells.get("a28"); 
          cell.setValue("40 и старше");   
          
          cell = cells.get("a29"); 
          cell.setValue("в возрасте моложе 15 лет");     
          
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
          
          cell = cells.get("b17"); 
          cell.setValue("08");   
          
          cell = cells.get("b18"); 
          cell.setValue("09");     
          
          cell = cells.get("b19"); 
          cell.setValue("10");     
          
          cell = cells.get("b20"); 
          cell.setValue("11");     
          
          cell = cells.get("b21"); 
          cell.setValue("12");     
          
          cell = cells.get("b22"); 
          cell.setValue("13");     
          
          cell = cells.get("b23"); 
          cell.setValue("14");   
          
          cell = cells.get("b24"); 
          cell.setValue("15");     
          
          cell = cells.get("b25"); 
          cell.setValue("16");   
          
          cell = cells.get("b26"); 
          cell.setValue("17");           
          
          cell = cells.get("b27"); 
          cell.setValue("18");       
          
          cell = cells.get("b28"); 
          cell.setValue("19");   
          
          cell = cells.get("b29"); 
          cell.setValue("20");    
          
          cell = cells.get("a30"); 
          cell.setValue("Для граф 3 – 5, 9 – 11 указать возраст по состоянию на 1 января текущего календарного года, для граф 6 – 8 –  по состоянию на 1 января следующего календарного года.");

          // принято студентов - всего
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN1 = "c10";
       	   cell = cells.get(rowN1);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // в том числе в возрасте (число полных лет на 1 января): 15
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%2000'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN2 = "c11";
       	   cell = cells.get(rowN2);
       	   
       	  cell.setValue(rs.getString(1));
          }    
          
          // 16
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1999'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN3 = "c12";
       	   cell = cells.get(rowN3);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 17
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1998'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN4 = "c13";
       	   cell = cells.get(rowN4);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 18
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1997'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN5 = "c14";
       	   cell = cells.get(rowN5);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 19
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1996'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN6 = "c15";
       	   cell = cells.get(rowN6);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 20
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1995'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN7 = "c16";
       	   cell = cells.get(rowN7);
       	   
       	  cell.setValue(rs.getString(1));
          }        
          
          // 21
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1994'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN8 = "c17";
       	   cell = cells.get(rowN8);
       	   
       	  cell.setValue(rs.getString(1));
          }      
          
          // 22
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1993'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN9 = "c18";
       	   cell = cells.get(rowN9);
       	   
       	  cell.setValue(rs.getString(1));
          }    
          
          // 23
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1992'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN10 = "c19";
       	   cell = cells.get(rowN10);
       	   
       	  cell.setValue(rs.getString(1));
          }    
          
          // 24
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1991'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN11 = "c20";
       	   cell = cells.get(rowN11);
       	   
       	  cell.setValue(rs.getString(1));
          }    
          
          // 25
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1990'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN12 = "c21";
       	   cell = cells.get(rowN12);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // 26
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1989'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN13 = "c22";
       	   cell = cells.get(rowN13);
       	   
       	  cell.setValue(rs.getString(1));
          }   
            
          // 27
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1988'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN14 = "c23";
       	   cell = cells.get(rowN14);
       	   
       	  cell.setValue(rs.getString(1));
          }       
          
          // 28
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1987'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN15 = "c24";
       	   cell = cells.get(rowN15);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 29
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.datarojdenija like '%1986'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN16 = "c25";
       	   cell = cells.get(rowN16);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 30-34
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1981' or datarojdenija like '%1982' or datarojdenija like '%1983' or datarojdenija like '%1984' or datarojdenija like '%1985') and prinjat not like 'н'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN17 = "c26";
       	   cell = cells.get(rowN17);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 35-39
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1976' or datarojdenija like '%1977' or datarojdenija like '%1978' or datarojdenija like '%1979' or datarojdenija like '%1980') and prinjat not like 'н'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN18 = "c27";
       	   cell = cells.get(rowN18);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 40 и старше
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1960' or datarojdenija like '%1961' or datarojdenija like '%1962' or datarojdenija like '%1963' or datarojdenija like '%1964' or datarojdenija like '%1965' or datarojdenija like '%1966' or datarojdenija like '%1967' or datarojdenija like '%1968' or datarojdenija like '%1969' or datarojdenija like '%1970' or datarojdenija like '%1971' or datarojdenija like '%1972' or datarojdenija like '%1973' or datarojdenija like '%1974' or datarojdenija like '%1975') and prinjat not like 'н'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN19 = "c28";
       	   cell = cells.get(rowN19);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // в возрасте моложе 15 лет
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%2001' or datarojdenija like '%2002') and prinjat not like 'н'");          
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN20 = "c29";
       	   cell = cells.get(rowN20);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // по договорам об оказании платных образовательных услуг - всего
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN21 = "d10";
       	   cell = cells.get(rowN21);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // в том числе в возрасте (число полных лет на 1 января): 15
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%2000'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN22 = "d11";
       	   cell = cells.get(rowN22);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 16
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1999'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN23 = "d12";
       	   cell = cells.get(rowN23);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 17
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1998'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN24 = "d13";
       	   cell = cells.get(rowN24);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 18
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1997'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN25 = "d14";
       	   cell = cells.get(rowN25);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 19
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1996'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN26 = "d15";
       	   cell = cells.get(rowN26);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 20
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1995'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN27 = "d16";
       	   cell = cells.get(rowN27);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 21
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1994'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN28 = "d17";
       	   cell = cells.get(rowN28);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 22
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1993'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN29 = "d18";
       	   cell = cells.get(rowN29);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 23
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1992'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN30 = "d19";
       	   cell = cells.get(rowN30);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 24
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1991'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN31 = "d20";
       	   cell = cells.get(rowN31);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 25
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1990'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN32 = "d21";
       	   cell = cells.get(rowN32);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 26
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1989'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN33 = "d22";
       	   cell = cells.get(rowN33);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 27
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1988'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN34 = "d23";
       	   cell = cells.get(rowN34);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 28
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1987'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN35 = "d24";
       	   cell = cells.get(rowN35);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 29
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.prinjat like 'д'  and a.datarojdenija like '%1986'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN36 = "d25";
       	   cell = cells.get(rowN36);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 30-34
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1981' or datarojdenija like '%1982' or datarojdenija like '%1983' or datarojdenija like '%1984' or datarojdenija like '%1985') and prinjat not like 'н' and prinjat like 'д'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN37 = "d26";
       	   cell = cells.get(rowN37);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 35-39
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1976' or datarojdenija like '%1977' or datarojdenija like '%1978' or datarojdenija like '%1979' or datarojdenija like '%1980') and prinjat not like 'н' and prinjat like 'д'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN38 = "d27";
       	   cell = cells.get(rowN38);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 40 и старше
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1960' or datarojdenija like '%1961' or datarojdenija like '%1962' or datarojdenija like '%1963' or datarojdenija like '%1964' or datarojdenija like '%1965' or datarojdenija like '%1966' or datarojdenija like '%1967' or datarojdenija like '%1968' or datarojdenija like '%1969' or datarojdenija like '%1970' or datarojdenija like '%1971' or datarojdenija like '%1972' or datarojdenija like '%1973' or datarojdenija like '%1974' or datarojdenija like '%1975') and prinjat not like 'н' and prinjat like 'д'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN39 = "d28";
       	   cell = cells.get(rowN39);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // в возрасте моложе 15 лет 
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%2001' or datarojdenija like '%2002') and prinjat not like 'н' and prinjat like 'д'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN40 = "d29";
       	   cell = cells.get(rowN40);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // женщины - всего
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN41 = "e10";
       	   cell = cells.get(rowN41);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // в том числе в возрасте (число полных лет на 1 января): 15
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%2000'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN42 = "e11";
       	   cell = cells.get(rowN42);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 16
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1999'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN43 = "e12";
       	   cell = cells.get(rowN43);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // 17
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1998'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN44 = "e13";
       	   cell = cells.get(rowN44);
       	   
       	  cell.setValue(rs.getString(1));
          }      
          
          // 18
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1997'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN45 = "e14";
       	   cell = cells.get(rowN45);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // 19
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1996'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN46 = "e15";
       	   cell = cells.get(rowN46);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // 20
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1995'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN47 = "e16";
       	   cell = cells.get(rowN47);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // 21
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1994'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN48 = "e17";
       	   cell = cells.get(rowN48);
       	   
       	  cell.setValue(rs.getString(1));
          }  
          
          // 22
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1993'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN49 = "e18";
       	   cell = cells.get(rowN49);
       	   
       	  cell.setValue(rs.getString(1));
          }  
          
          // 23
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1992'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN50 = "e19";
       	   cell = cells.get(rowN50);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 24
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1991'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN51 = "e20";
       	   cell = cells.get(rowN51);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 25
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1990'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN52 = "e21";
       	   cell = cells.get(rowN52);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 26
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1989'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN53 = "e22";
       	   cell = cells.get(rowN53);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 27
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1988'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN54 = "e23";
       	   cell = cells.get(rowN54);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 28
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1987'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN55 = "e24";
       	   cell = cells.get(rowN55);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 29
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and a.prinjat not like 'н' and a.pol like 'ж' and a.datarojdenija like '%1986'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN56 = "e25";
       	   cell = cells.get(rowN56);
       	   
       	  cell.setValue(rs.getString(1));
          } 
                  
          // 30-34
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1981' or datarojdenija like '%1982' or datarojdenija like '%1983' or datarojdenija like '%1984' or datarojdenija like '%1985') and prinjat not like 'н' and prinjat like 'д' and pol like 'ж'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN57 = "e26";
       	   cell = cells.get(rowN57);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 35-39
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1976' or datarojdenija like '%1977' or datarojdenija like '%1978' or datarojdenija like '%1979' or datarojdenija like '%1980') and prinjat not like 'н' and prinjat like 'д' and pol like 'ж'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN58 = "e27";
       	   cell = cells.get(rowN58);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // 40 и старше
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%1960' or datarojdenija like '%1961' or datarojdenija like '%1962' or datarojdenija like '%1963' or datarojdenija like '%1964' or datarojdenija like '%1965' or datarojdenija like '%1966' or datarojdenija like '%1967' or datarojdenija like '%1968' or datarojdenija like '%1969' or datarojdenija like '%1970' or datarojdenija like '%1971' or datarojdenija like '%1972' or datarojdenija like '%1973' or datarojdenija like '%1974' or datarojdenija like '%1975') and prinjat not like 'н' and prinjat like 'д' and pol like 'ж'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN59 = "e28";
       	   cell = cells.get(rowN59);
       	   
       	  cell.setValue(rs.getString(1));
          } 
          
          // в возрасте моложе 15 лет
          pstmt = conn.prepareStatement("select count(*) from abiturient where (datarojdenija like '%2001' or datarojdenija like '%2002') and prinjat not like 'н' and prinjat like 'д' and pol like 'ж'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN60 = "e29";
       	   cell = cells.get(rowN60);
       	   
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