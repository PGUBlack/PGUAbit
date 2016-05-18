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

public class UmuExcel201628Action extends Action {

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
          cell.setValue("Результаты приема на обучение по программам магистратуры");
          
          cell = cells.get("d5");
          cell.setValue("Код по ОКЕИ: человек – 792");     
          
          cell = cells.get("d6");
          cell.setValue("Принято на обучение");
          
          cell = cells.get("e7");
          cell.setValue("из них");  
          
          cell = cells.get("b8");
          cell.setValue("№ строки"); 

          cell = cells.get("c8");
          cell.setValue("Подано заявлений");
          
          cell = cells.get("d8");
          cell.setValue("всего");    
          
          cell = cells.get("e8");
          cell.setValue("за счет бюджетных ассигнований федерального бюджета");
          
          cell = cells.get("f8");
          cell.setValue("по договорам об оказании платных образовательных услуг");
             
          
          cell = cells.get("a10");
          cell.setValue("Принято на обучение по программам магистратуры:"); 
          
          cell = cells.get("a11");
          cell.setValue("первое высшее образование");      
          
          cell = cells.get("a12");
          cell.setValue("из них (из строки 01) по результатам целевого приема");    
          
          cell = cells.get("a13");
          cell.setValue("второе высшее образование");        
          
          cell = cells.get("b11");
          cell.setValue("01");      
          
          cell = cells.get("b12");
          cell.setValue("02");    
          
          cell = cells.get("b13");
          cell.setValue("03");      
          
          cell = cells.get("e13");
          cell.setValue("X"); 
          
          // подано заявлений
          pstmt = conn.prepareStatement("select count(k.kodspetsialnosti) from konkurs k, spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and  s.edulevel = 'м'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN1 = "c11";
       	   cell = cells.get(rowN1);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // принято          
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'н'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN2 = "d11";
       	   cell = cells.get(rowN2);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // за счет бюджетных ассигнований федерального бюджета
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat not like 'д' and a.prinjat not like 'н'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN3 = "e11";
       	   cell = cells.get(rowN3);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // по договорам об оказании платных образовательных услуг       
          pstmt = conn.prepareStatement("select count(a.kodspetsialnzach) from abiturient a, spetsialnosti s where a.kodspetsialnzach = s.kodspetsialnosti and  s.edulevel = 'м' and a.prinjat like 'д'  and a.prinjat not like 'н'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN4 = "f11";
       	   cell = cells.get(rowN4);
       	   
       	  cell.setValue(rs.getString(1));
          }   
          
          // по результатам целевого приёма - подано заявлений
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND Konkurs.Target = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND  Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.target NOT LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat IN('н','д','7','4','3','2','1') OR Prinjat IS NULL) AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' AND Spetsialnosti.EduLevel = 'м'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN5 = "c12";
       	   cell = cells.get(rowN5);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // по результатам целевого приёма - принято всего
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б', 'д') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like 'н') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'м'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN6 = "d12";
       	   cell = cells.get(rowN6);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // по результатам целевого приёма - принято за счет бюджетных ассигнований федерального бюджета
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('б') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like 'н') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'м'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN7 = "e12";
       	   cell = cells.get(rowN7);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // по результатам целевого приёма - принято по договорам об оказании платных образовательных услуг 
          pstmt = conn.prepareStatement("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf, TselevojPriem, Konkurs, Lgoty WHERE Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot AND Konkurs.Prioritet = '1' AND konkurs.kodabiturienta = abiturient.kodabiturienta AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=1 AND kodFormyOb IN ('о','з','в') AND kodOsnovyOb IN ('д') AND Konkurs.target not LIKE '1' AND Spetsialnosti.KodSpetsialnosti LIKE '%' AND (NomerPlatnogoDogovora LIKE '%' OR NomerPlatnogoDogovora IS NULL) AND (KodSpetsialnZach LIKE '%' OR KodSpetsialnZach IS NULL) AND (Prinjat not like 'н') AND ShifrPriema LIKE '%' AND AbbreviaturaFakulteta LIKE '%' and Spetsialnosti.EduLevel = 'м'");
          rs = pstmt.executeQuery();
          while(rs.next()){
       	   String rowN8 = "f12";
       	   cell = cells.get(rowN8);
       	   
       	  cell.setValue(rs.getString(1));
          }
          
          // ВТОРОЕ ВЫСШЕЕ ВКЛЮЧАТЬ В C13, D13, F13 ИЛИ НЕТ?
          
          
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