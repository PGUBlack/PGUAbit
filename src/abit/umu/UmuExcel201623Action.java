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
          cells.setColumnWidth(0, 120);
          cell.setValue("Пензенский государственный университет");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 120);          
          cell.setValue("Очное обучение"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 120);
          cell.setValue("2.3. Распределение численности студентов, приема и выпуска по источникам финансирования обучения.");
          
          cell = cells.get("A5");
          cells.setColumnWidth(0, 120);
          cell.setValue("Целевой прием и целевое обучение. Сведения о студентах с ОВЗ и инвалидах");        
          
          cell = cells.get("i6");
          cell.setValue("Код по ОКЕИ: человек – 792");     
          
          cell = cells.get("c7");
          cell.setValue("Программы бакалавриата");
          
          cell = cells.get("f7");
          cell.setValue("Программы специалитета");
          
          cell = cells.get("i7");
          cell.setValue("Программы магистратуры");
      
          cell = cells.get("b8");
          cell.setValue("№ строки");

          cell = cells.get("c8");
          cell.setValue("Принято");
          
          cell = cells.get("d8");
          cell.setValue("Численность студентов");
                    
          cell = cells.get("e8");
          cell.setValue("Выпуск");         
          
          cell = cells.get("f8");
          cell.setValue("Принято");
          
          cell = cells.get("g8");
          cell.setValue("Численность студентов");
                    
          cell = cells.get("h8");
          cell.setValue("Выпуск");   
          
          cell = cells.get("i8");
          cell.setValue("Принято");
          
          cell = cells.get("j8");
          cell.setValue("Численность студентов");
                    
          cell = cells.get("k8");
          cell.setValue("Выпуск");        
          
          cell = cells.get("a10");
          cell.setValue("Всего (сумма строк 02 – 05)");  

          cell = cells.get("a11");
          cell.setValue("в том числе студенты, обучающиеся: "); 

          cell = cells.get("a12");
          cell.setValue("за счет бюджетных ассигнований федерального бюджета"); 

          cell = cells.get("a13");
          cell.setValue("за счет бюджетных ассигнований бюджета субъекта Российской Федерации");
          
          cell = cells.get("a14");
          cell.setValue("за счет бюджетных ассигнований местного бюджета");  
          
          cell = cells.get("a15");
          cell.setValue("по договорам об оказании платных образовательных услуг (сумма строк 06 – 08)");    

          cell = cells.get("a16");
          cell.setValue("в том числе за счет средств: ");
          
          cell = cells.get("a17");
          cell.setValue("физических лиц");          
          
          cell = cells.get("a18");
          cell.setValue("юридических лиц");
          
          cell = cells.get("a19");
          cell.setValue("физических и юридических лиц");          
          
          cell = cells.get("a20");
          cell.setValue("Заключили контракты в соответствии с государственным планом подготовки специалистов для организаций ОПК (из строки 02)"); 
          
          cell = cells.get("a21");
          cell.setValue("Студенты, обучающиеся на условиях целевого приема (из суммы строк 02 – 04)");     
    
          cell = cells.get("a22");
          cell.setValue("Студенты, обучающиеся на условиях целевого обучения (из строки 01)");
          
          cell = cells.get("a23");
          cell.setValue("Студенты, получающие второе высшее образование (из строки 01)");
          
          cell = cells.get("a24");
          cell.setValue("Численность студентов с ограниченными возможностями здоровья (ОВЗ) (из строки 01)"); 
          
          cell = cells.get("a25");
          cell.setValue("из них обучаются по адаптированным образовательным программам");     
          
          cell = cells.get("a26");
          cell.setValue("Численность инвалидов (из строки 01; сумма строк 16 – 21)");      
          
          cell = cells.get("a27");
          cell.setValue("в том числе:");        
          
          cell = cells.get("a28");
          cell.setValue("с нарушениями зрения");  
          
          cell = cells.get("a29");
          cell.setValue("с нарушениями слуха и речи");  
          
          cell = cells.get("a30");
          cell.setValue("с нарушениями опорно-двигательного аппарата"); 
          
          cell = cells.get("a31");
          cell.setValue("с соматическими нарушениями");    
          
          cell = cells.get("a32");
          cell.setValue("с расстройствами аутистического спектра");   
          
          cell = cells.get("a33");
          cell.setValue("с другими нарушениями (кроме учтенных в строках 16 – 20)");    
          
          cell = cells.get("a34");
          cell.setValue("из них (из строки 15) обучаются по адаптированным образовательным программам");       
          
          cell = cells.get("a35");
          cell.setValue("Численность детей-инвалидов (из строки 15)");             
          
          cell = cells.get("a36");
          cell.setValue("из них обучаются по адаптированным образовательным программам");       
                          
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
          
          // принято (бакалавриат) - всего (сумма строк 02 – 05)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'н'");       
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN1 = "c10";
     	   cell = cells.get(rowN1);
     	   
     	  cell.setValue(rs.getString(1));
          }
        
          // принято (бакалавриат) - за счет бюджетных ассигнований федерального бюджета
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'н' and a.prinjat not like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN2 = "c12";
     	   cell = cells.get(rowN2);
     	   
     	  cell.setValue(rs.getString(1));
          }

          // принято (бакалавриат) - по договорам об оказании платных образовательных услуг (сумма строк 06 – 08)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'н' and a.prinjat like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN3 = "c15";
     	   cell = cells.get(rowN3);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // принято (специалитет) - всего (сумма строк 02 – 05) 
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'н'");       
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN4 = "f10";
     	   cell = cells.get(rowN4);
     	   
     	  cell.setValue(rs.getString(1));
          }
        
          // принято (специалитет) - за счет бюджетных ассигнований федерального бюджета
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'н' and a.prinjat not like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN5 = "f12";
     	   cell = cells.get(rowN5);
     	   
     	  cell.setValue(rs.getString(1));
          }

          // принято (специалитет) - по договорам об оказании платных образовательных услуг (сумма строк 06 – 08)         
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'н' and a.prinjat like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN6 = "f15";
     	   cell = cells.get(rowN6);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // принято (магистратура) - всего (сумма строк 02 – 05)
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'н'");       
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN7 = "i10";
     	   cell = cells.get(rowN7);
     	   
     	  cell.setValue(rs.getString(1));
          }
        
          // принято (магистратура) - за счет бюджетных ассигнований федерального бюджета
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'н' and a.prinjat not like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN8 = "i12";
     	   cell = cells.get(rowN8);
     	   
     	  cell.setValue(rs.getString(1));
          }

          // принято (магистратура) - по договорам об оказании платных образовательных услуг (сумма строк 06 – 08) 
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'н' and a.prinjat like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN9 = "i15";
     	   cell = cells.get(rowN9);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // принято (бакалавриат) - физические лица
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'н' and a.prinjat like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN10 = "c17";
     	   cell = cells.get(rowN10);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // принято (специалитет) - физические лица
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'н' and a.prinjat like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN11 = "f17";
     	   cell = cells.get(rowN11);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // принято (магистратура) - физические лица
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'н' and a.prinjat like 'д'");        
          rs = pstmt.executeQuery();
          while(rs.next()){
     	   String rowN12 = "i17";
     	   cell = cells.get(rowN12);
     	   
     	  cell.setValue(rs.getString(1));
          }
          
          // принято (бакалавриат) - целевой приём
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like 'н') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'б'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN13 = "c21";
             	   cell = cells.get(rowN13);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // принято (специалитет) - целевой приём
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like 'н') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'с'");
          		  rs = pstmt.executeQuery();
          		  while(rs.next()){
          		  String rowN14 = "f21";
          		  cell = cells.get(rowN14);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // принято (магистратура) - целевой приём
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like 'н') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'м'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN15 = "i21";
             	   cell = cells.get(rowN15);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
                 // ВТОРОЕ ВЫСШЕЕ НУЖНО ВЫВОДИТЬ в c23, f23, i23 ИЛИ НЕТ???
               
           // принято (бакалавриат) - численность инвалидов 
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'б'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN16 = "c26";
             	   cell = cells.get(rowN16);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
           // принято (специалитет) - численность инвалидов 
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'с'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN17 = "f26";
             	   cell = cells.get(rowN17);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // принято (магистратура) - численность инвалидов    
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'м'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN18 = "i26";
             	   cell = cells.get(rowN18);
             	   
             	  cell.setValue(rs.getString(1));
                  }                 
                     
          // принято (бакалавриат) - численность инвалидов (с другими нарушениями)        
           pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'б'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN19 = "c33";
             	   cell = cells.get(rowN19);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // принято (специалитет) - численность инвалидов (с другими нарушениями)  
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'с'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN20 = "f33";
             	   cell = cells.get(rowN20);
             	   
             	  cell.setValue(rs.getString(1));
                  }
                  
          // принято (магистратура) - численность инвалидов (с другими нарушениями) 
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'м'");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN21 = "i33";
             	   cell = cells.get(rowN21);
             	   
             	  cell.setValue(rs.getString(1));
                  }       
                  
          // принято (бакалавриат) - численность детей-инвалидов (до 18 лет - дата рождения - до 01.09.1997 г. ???)        
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'б' and (abiturient.datarojdenija like ('%09-1997') or abiturient.datarojdenija like ('%10-1997') or abiturient.datarojdenija like ('%11-1997') or abiturient.datarojdenija like ('%12-1997') or abiturient.datarojdenija like ('%1998') or abiturient.datarojdenija like ('%1999') or abiturient.datarojdenija like ('%2000') or abiturient.datarojdenija like ('%2001'))");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN22 = "c35";
             	   cell = cells.get(rowN22);
             	   
             	  cell.setValue(rs.getString(1));
                  }
          
          // принято (специалитет) - численность детей-инвалидов (до 18 лет - дата рождения - до 01.09.1997 г. ???)          
            pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.OP LIKE '4' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like ('н') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'с' and (abiturient.datarojdenija like ('%09-1997') or abiturient.datarojdenija like ('%10-1997') or abiturient.datarojdenija like ('%11-1997') or abiturient.datarojdenija like ('%12-1997') or abiturient.datarojdenija like ('%1998') or abiturient.datarojdenija like ('%1999') or abiturient.datarojdenija like ('%2000') or abiturient.datarojdenija like ('%2001'))");
                  rs = pstmt.executeQuery();
                  while(rs.next()){
             	   String rowN23 = "f35";
             	   cell = cells.get(rowN23);
             	   
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