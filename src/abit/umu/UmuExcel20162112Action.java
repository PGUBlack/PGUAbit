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

public class UmuExcel20162112Action extends Action {

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
          cells.setColumnWidth(0, 80);
          cell.setValue("Пензенский государственный университет");          
          
          cell = cells.get("A2");
          cells.setColumnWidth(0, 80);          
          cell.setValue("Очное обучение"); 
          
          cell = cells.get("A4");
          cells.setColumnWidth(0, 80);
          cell.setValue("2.1.1. Распределение приема по направлениям подготовки и специальностям");
          
          cell = cells.get("h5");
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
          cell.setValue("Подано заявлений");
          
          cell = cells.get("d8");
          cell.setValue("Принято(сумма гр. 6 – 9)");
    
          cell = cells.get("e8");
          cell.setValue("федерального бюджета");
          
          cell = cells.get("f8");
          cell.setValue("бюджета субъекта Российской Федерации");
          
          cell = cells.get("g8");
          cell.setValue("местного бюджета");     
          
          cell = cells.get("h8");
          cell.setValue("по договорам об оказании платных образовательных услуг");  
          
          cell = cells.get("a10");
          cell.setValue("Программы бакалавриата - всего");  

          cell = cells.get("a11");
          cell.setValue("Из стр. 01 по индивидуальному учебному плану ускоренно по программам бакалавриата"); 

          cell = cells.get("a12");
          cell.setValue("Из стр. 01 с применением сетевой формы обучения по программам бакалавриата"); 

          cell = cells.get("a13");
          cell.setValue("Из стр. 01 с использованием дистанционных образовательных технологий по программам бакалавриата");
          
          cell = cells.get("a14");
          cell.setValue("Программы специалитета – всего");  
          
          cell = cells.get("a15");
          cell.setValue("Из стр. 05 по индивидуальному учебному плану ускоренно по программам специалитета");    
          
          cell = cells.get("a16");
          cell.setValue("Из стр. 05 с применением сетевой формы обучения по программам специалитета");        
          
          cell = cells.get("a17");
          cell.setValue("Из стр. 05 с использованием дистанционных образовательных технологий по программам специалитета");
          
          cell = cells.get("a18");
          cell.setValue("Программы магистратуры – всего");          
          
          cell = cells.get("a19");
          cell.setValue("Из стр. 09 по индивидуальному учебному плану ускоренно по программам магистратуры");
          
          cell = cells.get("a20");
          cell.setValue("Из стр. 09 с применением сетевой формы обучения по программам магистратуры");          
          
          cell = cells.get("a21");
          cell.setValue("Из стр. 09 с использованием дистанционных образовательных технологий по программам магистратуры"); 
          
          cell = cells.get("a22");
          cell.setValue("Всего по программам высшего образования (сумма строк 01, 05, 09)");          
                          
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

          cell = cells.get("b10");
          cell.setValue("13");  
        
          cell = cells.get("f10");
          cell.setValue("0");           
                  
          cell = cells.get("g10");
          cell.setValue("0");    

          cell = cells.get("b14");
          cell.setValue("13");  
        
          cell = cells.get("f14");
          cell.setValue("0");           
                  
          cell = cells.get("g14");
          cell.setValue("0"); 
          
          cell = cells.get("b18");
          cell.setValue("13");  
        
          cell = cells.get("f18");
          cell.setValue("0");           
                  
          cell = cells.get("g18");
          cell.setValue("0"); 
          
          cell = cells.get("b22");
          cell.setValue("13");  
        
          cell = cells.get("f22");
          cell.setValue("0");           
                  
          cell = cells.get("g22");
          cell.setValue("0"); 

          // подано заявлений - программы бакалавриата - всего
        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'б'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN7 = "c10";
     	   cell = cells.get(rowN7);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // принято(сумма гр. 6 – 9) - программы бакалавриата - всего      
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN8 = "d10";
     	   cell = cells.get(rowN8);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // по договорам об оказании платных образовательных услуг - программы бакалавриата - всего              
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat like 'д'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN9 = "h10";
     	   cell = cells.get(rowN9);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
        // федерального бюджета - программы бакалавриата - всего         
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'д' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN10 = "e10";
     	   cell = cells.get(rowN10);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
        //дистанционное обучение у бакалавриата
//        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s, AbitDopInf d where d.kodabiturienta=k.kodabiturienta and k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'б' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN11 = "c13";
//     	   cell = cells.get(rowN11);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'н' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN12 = "d13";
//     	   cell = cells.get(rowN12);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat like 'д' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN13 = "e13";
//     	   cell = cells.get(rowN13);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }   
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'б' and a.prinjat not like 'д' and a.prinjat not like 'н' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN14 = "h13";
//     	   cell = cells.get(rowN14);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
        
        // подано заявлений - программы специалитета - всего
        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'с'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN15 = "c14";
     	   cell = cells.get(rowN15);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // принято(сумма гр. 6 – 9) - программы специалитета - всего   
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN16 = "d14";
     	   cell = cells.get(rowN16);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
     // по договорам об оказании платных образовательных услуг - программы специалитета - всего 
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat like 'д'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN17 = "h14";
     	   cell = cells.get(rowN17);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
        // федерального бюджета - программы специалитета - всего        
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'д' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN18 = "e14";
     	   cell = cells.get(rowN18);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
//        дистанционное обучение у специалитета
//        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s, AbitDopInf d where d.kodabiturienta=k.kodabiturienta and k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'с' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN19 = "c17";
//     	   cell = cells.get(rowN19);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'н' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN20 = "d17";
//     	   cell = cells.get(rowN20);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat like 'д' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN21 = "e17";
//     	   cell = cells.get(rowN21);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }   
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'с' and a.prinjat not like 'д' and a.prinjat not like 'н' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN22 = "h17";
//     	   cell = cells.get(rowN22);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
        
        // подано заявлений - программы магистратуры - всего
        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'м'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN23 = "c18";
     	   cell = cells.get(rowN23);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // принято(сумма гр. 6 – 9) - программы магистратуры - всего          
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN24 = "d18";
     	   cell = cells.get(rowN24);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // по договорам об оказании платных образовательных услуг - программы магистратуры - всего       
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat like 'д'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN25 = "h18";
     	   cell = cells.get(rowN25);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
        // федерального бюджета - программы магистратуры - всего     
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'д' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN26 = "e18";
     	   cell = cells.get(rowN26);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
        //дистанционное обучение у магистратуры
//        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s, AbitDopInf d where d.kodabiturienta=k.kodabiturienta and k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'м' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN27 = "c21";
//     	   cell = cells.get(rowN27);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'н' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN28 = "d21";
//     	   cell = cells.get(rowN28);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat like 'д' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN29 = "e21";
//     	   cell = cells.get(rowN29);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }   
//        
//        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s, abitdopinf d where d.kodabiturienta=a.kodabiturienta and a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'д' and a.prinjat not like 'н' and d.Dist not in ('-')");
//        rs = pstmt.executeQuery();
//        while(rs.next()){
//     	   String rowN30 = "h21";
//     	   cell = cells.get(rowN30);
//     	   
//     	  cell.setValue(rs.getString(1));
//        }
           
        // подано заявлений - всего по программам высшего образования
        pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel in ('б', 'с', 'м')");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN31 = "c22";
     	   cell = cells.get(rowN31);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // принято(сумма гр. 6 – 9) - всего по программам высшего образования        
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel in ('б', 'с', 'м') and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN32 = "d22";
     	   cell = cells.get(rowN32);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // по договорам об оказании платных образовательных услуг - всего по программам высшего образования          
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel in ('б', 'с', 'м') and a.prinjat like 'д'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN33 = "h22";
     	   cell = cells.get(rowN33);
     	   
     	  cell.setValue(rs.getString(1));
        }      
        
        // федерального бюджета - всего по программам высшего образования
        pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel in ('б', 'с', 'м') and a.prinjat not like 'д' and a.prinjat not like 'н'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN34 = "e22";
     	   cell = cells.get(rowN34);
     	   
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