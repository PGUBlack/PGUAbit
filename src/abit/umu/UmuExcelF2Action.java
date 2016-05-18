package abit.umu;
import java.io.IOException;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.ArrayList;

import org.apache.struts.action.*;

import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;















import javax.naming.*;
import javax.sql.*;

import abit.bean.*;
import abit.Constants;
import abit.util.*;

import java.util.Date;
import java.io.*;
import java.math.BigDecimal;

import abit.sql.*; 

public class UmuExcelF2Action extends Action {

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
        Statement            stmt3              = null;
        
        float srBall13bud = 0;
       
     
        float srBall14bud = 0;
        float srBall15bud = 0;
        float srBall16bud = 0;
        float srBall12bud = 0;
        float srBall11bud = 0;
        float srBallObsh = 0;
        
        int count13bud = 0;
        int count14bud = 0;
        int count15bud = 0;
        int count16bud = 0;
        int count11bud = 0;
        int count12bud = 0;
        int countObsh = 0;
        
        float summBall13bud = 0;
        float summBall14bud = 0;
        float summBall15bud = 0;
        float summBall16bud = 0;
        float summBall11bud = 0;
        float summBall12bud = 0;
        float summBallObsh = 0;
        
        
        float srBall13 = 0;
        
        
        float srBall14 = 0;
        float srBall15 = 0;
        float srBall16 = 0;
        float srBall12 = 0;
        float srBall11 = 0;
        
        
        int count13 = 0;
        int count14 = 0;
        int count15 = 0;
        int count16 = 0;
        int count11 = 0;
        int count12 = 0;
     
        
        float summBall13 = 0;
        float summBall14 = 0;
        float summBall15 = 0;
        float summBall16 = 0;
        float summBall11 = 0;
        float summBall12 = 0;
      
        
        
        float srBall13dog = 0;
        
        
        float srBall14dog = 0;
        float srBall15dog = 0;
        float srBall16dog = 0;
        float srBall12dog = 0;
        float srBall11dog = 0;
        
        
        int count13dog = 0;
        int count14dog = 0;
        int count15dog = 0;
        int count16dog = 0;
        int count11dog = 0;
        int count12dog = 0;
       
        
        float summBall13dog = 0;
        float summBall14dog = 0;
        float summBall15dog = 0;
        float summBall16dog = 0;
        float summBall11dog = 0;
        float summBall12dog = 0;
       
        
        
      
       
        
        
       
        ResultSet               rs                    = null;
        
        Statement stmt9=null;
        PreparedStatement stmt10=null;
        PreparedStatement stmt11=null;
        ResultSet rs9=null;
        ResultSet rs10=null;
        ResultSet rs11=null;
        
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
      
     
        boolean              lists_dec_ege_f    = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList<Integer>			 specs              = new ArrayList();
        Integer kodSpec = null;
        String               AS                 = new String();           // аббревиатура специальности
        String               SS                 = new String();           // шифр
        String               NS                 = new String();           // название специальности
        String               PP                 = new String();           // план приема
        String               TP1                = new String();           // план целевого приема 1
        String               TP2                = new String();           // план целевого приема 2
        String               AF                 = new String();           // аббревиатура факультета
        StringBuffer         excludeList        = new StringBuffer("-1");
        StringBuffer         query              = new StringBuffer();
        StringBuffer		 query1  			= new StringBuffer();
        StringBuffer		 query2  			= new StringBuffer();
        int                  kAbit              = -1;
        int                  summa              = -1;
        int                  oldBallAbt         = -1;
        boolean              only_one_run       = true;
        boolean              header             = false;
        boolean              primechanie        = false;
        int                  nomer              = 0;
        int                  count_predm        = 4; // Только профильный предмет
        int ns=0;
        int kl=0;
        String dt=new String();
        dt=StringUtil.CurrDate(".");
        //vremennie dannie
        String F=new String();
        String I=new String();
        String O=new String();
        String ko=new String();
        String op=new String();
        String ind=new String();
        String spis=new String();
        int N=0;
        int summ=0;
        String Shifr=new String();
        int pr1=0;
        int pr2=0;
        int pr3=0;
        int pr4=0;
        int pr5=0;
        int pr6=0;
        int pr7=0;
        
        String z=null;
        int lgn=0;
        int total_lgn=0;
        int idfak=0;
        int num=0;
        int summt=0;
        
        
        

        UserBean             user               = (UserBean)session.getAttribute("user");
        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "waveFirstAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
        //  request.setAttribute( "waveFirstForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
        //создаем excel workbook
       //   Workbook workbook = new Workbook("D:\\output.xls");
          

          //добавляем новый лист Excel и получаем к нему доступ
         // int sheetIndex = workbook.getWorksheets().add();
       //   Worksheet worksheet = workbook.getWorksheets().get(0);
          
          Workbook workbook = new Workbook();

          //добавляем новый лист Excel и получаем к нему доступ
        //  int sheetIndex = workbook.getWorksheets().add();
          Worksheet worksheet = workbook.getWorksheets().get(0);
          
          Cells cells = worksheet.getCells();
        
       //   cells.setRowHeight(5, 100);
        //  cells.setColumnWidth(0, 40);

          Cell cell = cells.get("A2");
          cell.setValue("Раздел 2.2. Сведения о среднем балле ЕГЭ зачисленных");
          cell = cells.get("a4");
          cell.setValue("код спец.");
          
          cell = cells.get("b4");
          cell.setValue("наименование");
          
          cell = cells.get("c4");
          cell.setValue("шифр");
          

          cell = cells.get("d4");
          cell.setValue("форма об.");
          

          cell = cells.get("e4");
          cell.setValue("источник фин.");
          

          cell = cells.get("f4");
          cell.setValue("без творч. и стоб.");
          

          cell = cells.get("g4");
          cell.setValue("без творч + стоб.");
          

          cell = cells.get("h4");
          cell.setValue("творч без стоб.");
          

          cell = cells.get("i4");
          cell.setValue("творч + стоб. ");
          

          cell = cells.get("j4");
          cell.setValue("цел. без творч");
          

          cell = cells.get("k4");
          cell.setValue("льготн. без творч.");
          

          cell = cells.get("l4");
          cell.setValue("Проходной балл");
          
          
          cell = cells.get("c7");
          cell.setValue("p1.1");
          
          cell = cells.get("d7");
          cell.setValue("p1.2");
          
          cell = cells.get("e7");
          cell.setValue("p1.3");
          
          cell = cells.get("f7");
          cell.setValue("p2.13");
          
          cell = cells.get("g7");
          cell.setValue("p2.14");
          
          cell = cells.get("h7");
          cell.setValue("p2.15");
          
          cell = cells.get("i7");
          cell.setValue("p2.16");
          
          cell = cells.get("j7");
          cell.setValue("p2.12");
          
          cell = cells.get("k7");
          cell.setValue("p2.11");
          
          cell = cells.get("l7");
          cell.setValue("p2.17");
          

         
   
         //бюджет
         pstmt = conn.prepareStatement("Select Distinct k.kodspetsialnosti  from konkurs k , spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.eduLevel in ('б','с') ");
         rs = pstmt.executeQuery();
         while(rs.next()){
        	 specs.add(new Integer(rs.getInt(1)));
         }
        Iterator itr = specs.iterator();
        int rowIndex = 9;
        while(itr.hasNext()) {
           kodSpec = (Integer) itr.next();
           
           pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti, Tip_spec from spetsialnosti where kodspetsialnosti = ?");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "a"+rowIndex;
        	  cell = cells.get(rowN);
        	   cell.setValue(rs.getString(1));
        	   rowN = "b"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(rs.getString(2));
        	   rowN = "c"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(rs.getString(3));
        	   
        	   String FO = "";
        	   if (rs.getString(4).equals("о")) FO = "ф1";
        	   if (rs.getString(4).equals("з")) FO = "ф3";
        	   if (rs.getString(4).equals("в")) FO = "ф2";
        	   
        	   rowN = "d"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(FO);
        	   
        	   rowN = "e"+rowIndex;
          	   cell = cells.get(rowN);
          	   cell.setValue("1");
           	   
        	   
        	   
        	   
        	        	 
           }
           
         /***************************  без профильного  и стоб  ********************************************/
           
           
           int countPredmet = 3;
          
           pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   countPredmet = rs.getInt(1);
        	   int countSpec = rs.getInt(1);
        	  /* String rowN = "g"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);*/
        	   
           }
           
           
      int countAbiturient = 0;
           
           pstmt = conn.prepareStatement("select distinct count (a.kodabiturienta) from konkurs k, abiturient a where a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.prinjat in ('1','2','3') and k.kodspetsialnosti like ? and k.stob is NULL and k.op=1 and k.target=1 and a.viddoksredobraz like '%аттестат%' and (k.prof='0' or k.prof is null) and a.kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   countAbiturient = rs.getInt(1);
        	   int countSpec = rs.getInt(1);
        	/*   String rowN = "h"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);*/
        	   
           }
           
           
           
           
           pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta in (select distinct k.kodabiturienta from konkurs k, abiturient a where k.stob is NULL and k.kodspetsialnosti like ? and a.prinjat in ('1','2','3') and a.kodspetsialnzach=k.kodspetsialnosti and a.viddoksredobraz like '%аттестат%' and a.kodabiturienta=k.kodabiturienta and k.op = '1' and k.target = '1' and (k.prof = '0' or k.prof is null)) ");
           pstmt.setObject(1, kodSpec);
           pstmt.setObject(2, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int sumEGE = rs.getInt(1);
        	   float srBall = 0;
        	   BigDecimal bd;
        	   if (countAbiturient!=0){
        		   
        		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
        		   bd = new BigDecimal(Float.toString(srBall));
                   bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                	   System.out.println(bd);
                   String rowN = "f"+rowIndex;
            	   cell = cells.get(rowN);
            	   cell.setValue(bd);
            	   
            	   count13bud++;
            	   summBall13bud = summBall13bud+srBall;
            	   
            	 
            	   
        	   }
        	  
        	 /* 
        	  // else srBall = 0;
        	   String rowN = "f"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(sumEGE);*/
        	  
        	   
        	       	   
        	  
           }
           
           /***************************  без профильного  + стоб  ********************************************/ 
            countPredmet = 3;
           pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   countPredmet = rs.getInt(1);
        	   int countSpec = rs.getInt(1);
        	  /* String rowN = "g"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);*/
        	   
           }
           
           
       countAbiturient = 0;
           
           pstmt = conn.prepareStatement("select distinct count (k.kodabiturienta) from konkurs k, abiturient a where a.kodspetsialnzach=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.kodspetsialnosti like ? and a.kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and k.op = '1' and k.target = '1' and (k.prof = '0' or k.prof is null)");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   countAbiturient = rs.getInt(1);
        	   int countSpec = rs.getInt(1);
        	/*   String rowN = "h"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);*/
        	   
           }
           
           
           
           
           pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct a.kodabiturienta from konkurs k, abiturient a where a.kodspetsialnzach=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.kodspetsialnosti like ? and k.kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and k.op = '1' and k.target = '1' and (k.prof = '0' or k.prof is null))");
           pstmt.setObject(1, kodSpec);
           pstmt.setObject(2, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int sumEGE = rs.getInt(1);
        	   float srBall = 0;
        	   BigDecimal bd;
        	   if (countAbiturient!=0){
        		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
        		   bd = new BigDecimal(Float.toString(srBall));
                   bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                   String rowN = "g"+rowIndex;
            	   cell = cells.get(rowN);
            	   cell.setValue(bd);
            	   
            	   count14bud++;
            	   summBall14bud = summBall14bud+srBall;
        	   }
        	  
        	 /* 
        	  // else srBall = 0;
        	   String rowN = "f"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(sumEGE);*/
        	  
        	   
        	       	   
        	  
           } 
           
           
           
           /***************************  + профильный  - стоб  ********************************************/ 
           countPredmet = 3;
          pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? ");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countPredmet = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	  /* String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
      countAbiturient = 0;
          
          pstmt = conn.prepareStatement("select distinct count (a.kodabiturienta) from konkurs k, abiturient a where a.kodspetsialnzach=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.kodspetsialnosti like ? and a.kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and k.stob is NULL and k.op = '1' and k.target = '1'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countAbiturient = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	/*   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
          
          
          pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct a.kodabiturienta from konkurs k, abiturient a where a.kodspetsialnzach=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.kodspetsialnosti like ? and k.stob is NULL and k.op = '1' and k.target = '1')");
          pstmt.setObject(1, kodSpec);
          pstmt.setObject(2, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int sumEGE = rs.getInt(1);
       	   float srBall = 0;
       	   BigDecimal bd;
       	   if (countAbiturient!=0){
       		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
       		   bd = new BigDecimal(Float.toString(srBall));
                  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                  String rowN = "h"+rowIndex;
           	   cell = cells.get(rowN);
           	   cell.setValue(bd);
           	   
           	 count15bud++;
      	   summBall15bud = summBall15bud+srBall;
       	   }
       	  
       	 /* 
       	  // else srBall = 0;
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(sumEGE);*/
       	  
       	   
       	       	   
       	  
          } 
           
           
           /***************************  + профильный  + стоб  ********************************************/ 
           countPredmet = 3;
          pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? ");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countPredmet = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	  /* String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
      countAbiturient = 0;
          
          pstmt = conn.prepareStatement("select distinct count (a.kodabiturienta) from konkurs k, abiturient a where a.kodspetsialnzach=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.kodspetsialnosti like ? and k.kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and k.op = '1' and k.target = '1'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countAbiturient = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	/*   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
          
          
          pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where  kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta in (select distinct a.kodabiturienta from konkurs k, abiturient a where a.kodspetsialnzach=k.kodspetsialnosti and a.kodabiturienta=k.kodabiturienta and a.prinjat in ('1','2','3') and a.viddoksredobraz like '%аттестат%' and k.kodspetsialnosti like ? and k.op = '1' and k.target = '1')");
          pstmt.setObject(1, kodSpec);
          pstmt.setObject(2, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int sumEGE = rs.getInt(1);
       	   float srBall = 0;
       	   BigDecimal bd;
       	   if (countAbiturient!=0){
       		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
       		   bd = new BigDecimal(Float.toString(srBall));
                  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                  String rowN = "i"+rowIndex;
           	   cell = cells.get(rowN);
           	   cell.setValue(bd);
           	   
           	 count16bud++;
      	   summBall16bud = summBall16bud+srBall;
       	   }
       	  
       	 /* 
       	  // else srBall = 0;
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(sumEGE);*/
       	  
       	   
       	       	   
       	  
          } 
          
          
          //target
         countPredmet = 3;
          
          pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countPredmet = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	  /* String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
     countAbiturient = 0;
          
          pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and target  not like '1' and konkurs.npd is NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and (konkurs.prof = '0' or konkurs.prof is null)");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countAbiturient = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	/*   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
          
          
          pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where stob is NULL and kodspetsialnosti like ? and zach = 'д' and target  not like '1' and (konkurs.prof = '0' or konkurs.prof is null) and konkurs.npd is NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО'))");
          pstmt.setObject(1, kodSpec);
          pstmt.setObject(2, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int sumEGE = rs.getInt(1);
       	   float srBall = 0;
       	   BigDecimal bd;
       	   if (countAbiturient!=0){
       		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
       		   bd = new BigDecimal(Float.toString(srBall));
                  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                  String rowN = "j"+rowIndex;
           	   cell = cells.get(rowN);
           	   cell.setValue(bd);
           	   
           	 count12bud++;
      	   summBall12bud = summBall12bud+srBall;
       	   }
       	  
       	 /* 
       	  // else srBall = 0;
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(sumEGE);*/
       	  
       	   
       	       	   
       	  
          }
           
        //льготы   
           
        countPredmet = 3;
          
          pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countPredmet = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	  /* String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
     countAbiturient = 0;
          
          pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and op  not like '1' and konkurs.npd is NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and (konkurs.prof = '0' or konkurs.prof is null)");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countAbiturient = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	/*   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
          
          
          pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where  (konkurs.prof = '0' or konkurs.prof is null) and kodspetsialnosti like ? and zach = 'д' and op  not like '1' and konkurs.npd is NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО'))");
          pstmt.setObject(1, kodSpec);
          pstmt.setObject(2, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int sumEGE = rs.getInt(1);
       	   float srBall = 0;
       	   BigDecimal bd;
       	   if (countAbiturient!=0){
       		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
       		   bd = new BigDecimal(Float.toString(srBall));
                  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                  String rowN = "k"+rowIndex;
           	   cell = cells.get(rowN);
           	   cell.setValue(bd);
           	   
           	 count11bud++;
      	   summBall11bud = summBall11bud+srBall;
       	   }
       	  
       	 /* 
       	  // else srBall = 0;
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(sumEGE);*/
       	  
       	   
       	       	   
       	  
          }
           
           
          
          
          
          rowIndex++;
        }
        
        
        
        /***********************************ДОГОВОР******************************************/
        //бюджет
        specs.clear();   //!!!!!!!
        pstmt = conn.prepareStatement("Select Distinct k.kodspetsialnosti  from konkurs k , spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.eduLevel in ('б','с')");
        rs = pstmt.executeQuery();
        while(rs.next()){
       	 specs.add(new Integer(rs.getInt(1)));
        }
       
        itr = specs.iterator();
       
       while(itr.hasNext()) {
          kodSpec = (Integer) itr.next();
          
          pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti, Tip_spec from spetsialnosti where kodspetsialnosti = ?");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int countSpec = rs.getInt(1);
       	   String rowN = "a"+rowIndex;
       	  cell = cells.get(rowN);
       	   cell.setValue(rs.getString(1));
       	   rowN = "b"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(rs.getString(2));
       	   rowN = "c"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(rs.getString(3));
       	   
       	   String FO = "";
       	   if (rs.getString(4).equals("о")) FO = "ф1";
       	   if (rs.getString(4).equals("з")) FO = "ф3";
       	   if (rs.getString(4).equals("в")) FO = "ф2";
       	   
       	   rowN = "d"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(FO);
       	   
       	   rowN = "e"+rowIndex;
         	   cell = cells.get(rowN);
         	   cell.setValue("4");
          	   
       	   
       	   
       	   
       	        	 
          }
          
        /***************************  без профильного  и стоб  ********************************************/
          
          
          int countPredmet = 3;
         
          pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countPredmet = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	  /* String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
     int countAbiturient = 0;
          
          pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and konkurs.npd is not NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and konkurs.op = '1' and konkurs.target = '1'  and (konkurs.prof = '0' or konkurs.prof is null)");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countAbiturient = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	/*   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
          
          
          pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where stob is NULL and kodspetsialnosti like ? and zach = 'д' and konkurs.npd is not NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and konkurs.op = '1' and konkurs.target = '1' and (konkurs.prof = '0' or konkurs.prof is null)) ");
          pstmt.setObject(1, kodSpec);
          pstmt.setObject(2, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int sumEGE = rs.getInt(1);
       	   float srBall = 0;
       	   BigDecimal bd;
       	   if (countAbiturient!=0){
       		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
       		   bd = new BigDecimal(Float.toString(srBall));
                  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                  String rowN = "f"+rowIndex;
           	   cell = cells.get(rowN);
           	   cell.setValue(bd);
           	   
           	   count13dog++;
        	   summBall13dog = summBall13dog+srBall;
           	   
       	   }
       	  
       	 /* 
       	  // else srBall = 0;
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(sumEGE);*/
       	  
       	   
       	       	   
       	  
          }
          
          /***************************  без профильного  + стоб  ********************************************/ 
           countPredmet = 3;
          pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countPredmet = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	  /* String rowN = "g"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
      countAbiturient = 0;
          
          pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and konkurs.npd is not NULL and konkurs.op = '1' and konkurs.target = '1' and (konkurs.prof = '0' or konkurs.prof is null)");
          pstmt.setObject(1, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   countAbiturient = rs.getInt(1);
       	   int countSpec = rs.getInt(1);
       	/*   String rowN = "h"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(countSpec);*/
       	   
          }
          
          
          
          
          pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where kodspetsialnosti like ? and zach = 'д' and konkurs.npd is not NULL  and konkurs.op = '1' and konkurs.target = '1' and (konkurs.prof = '0' or konkurs.prof is null))");
          pstmt.setObject(1, kodSpec);
          pstmt.setObject(2, kodSpec);
          rs = pstmt.executeQuery();
          if(rs.next()){
       	   int sumEGE = rs.getInt(1);
       	   float srBall = 0;
       	   BigDecimal bd;
       	   if (countAbiturient!=0){
       		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
       		   bd = new BigDecimal(Float.toString(srBall));
                  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                  String rowN = "g"+rowIndex;
           	   cell = cells.get(rowN);
           	   cell.setValue(bd);
           	   
           	  count14dog++;
       	   summBall14dog = summBall14dog+srBall;
       	   }
       	  
       	 /* 
       	  // else srBall = 0;
       	   String rowN = "f"+rowIndex;
       	   cell = cells.get(rowN);
       	   cell.setValue(sumEGE);*/
       	  
       	   
       	       	   
       	  
          } 
          
          
          
          /***************************  + профильный  - стоб  ********************************************/ 
          countPredmet = 3;
         pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? ");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countPredmet = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	  /* String rowN = "g"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
     countAbiturient = 0;
         
         pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and konkurs.npd is not NULL  and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1'");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countAbiturient = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	/*   String rowN = "h"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
         
         
         pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where  kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and konkurs.npd is not NULL  and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1')");
         pstmt.setObject(1, kodSpec);
         pstmt.setObject(2, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   int sumEGE = rs.getInt(1);
      	   float srBall = 0;
      	   BigDecimal bd;
      	   if (countAbiturient!=0){
      		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
      		   bd = new BigDecimal(Float.toString(srBall));
                 bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                 String rowN = "h"+rowIndex;
          	   cell = cells.get(rowN);
          	   cell.setValue(bd);
          	   
          	  count15dog++;
       	   summBall15dog = summBall15dog+srBall;
      	   }
      	  
      	 /* 
      	  // else srBall = 0;
      	   String rowN = "f"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(sumEGE);*/
      	  
      	   
      	       	   
      	  
         } 
          
          
          /***************************  + профильный  + стоб  ********************************************/ 
          countPredmet = 3;
         pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? ");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countPredmet = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	  /* String rowN = "g"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
     countAbiturient = 0;
         
         pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and konkurs.npd is not NULL and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1'");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countAbiturient = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	/*   String rowN = "h"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
         
         
         pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where  kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where kodspetsialnosti like ? and zach = 'д' and konkurs.npd is not NULL and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1')");
         pstmt.setObject(1, kodSpec);
         pstmt.setObject(2, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   int sumEGE = rs.getInt(1);
      	   float srBall = 0;
      	   BigDecimal bd;
      	   if (countAbiturient!=0){
      		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
      		   bd = new BigDecimal(Float.toString(srBall));
                 bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                 String rowN = "i"+rowIndex;
          	   cell = cells.get(rowN);
          	   cell.setValue(bd);
          	   
          	  count16dog++;
       	   summBall16dog = summBall16dog+srBall;
      	   }
      	  
      	 /* 
      	  // else srBall = 0;
      	   String rowN = "f"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(sumEGE);*/
      	  
      	   
      	       	   
      	  
         } 
         
        countPredmet = 3;
         
         pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countPredmet = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	  /* String rowN = "g"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
    countAbiturient = 0;
         
         pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and target  not like '1' and konkurs.npd is not NULL  and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО')");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countAbiturient = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	/*   String rowN = "h"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
         
         
         pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where stob is NULL and kodspetsialnosti like ? and zach = 'д' and target  not like '1' and konkurs.npd is not NULL  and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО'))");
         pstmt.setObject(1, kodSpec);
         pstmt.setObject(2, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   int sumEGE = rs.getInt(1);
      	   float srBall = 0;
      	   BigDecimal bd;
      	   if (countAbiturient!=0){
      		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
      		   bd = new BigDecimal(Float.toString(srBall));
                 bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                 String rowN = "j"+rowIndex;
          	   cell = cells.get(rowN);
          	   cell.setValue(bd);
          	   
          	  count12dog++;
       	   summBall12dog = summBall12dog+srBall;
      	   }
      	  
      	 /* 
      	  // else srBall = 0;
      	   String rowN = "f"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(sumEGE);*/
      	  
      	   
      	       	   
      	  
         }
          
       //льготы   
          
       countPredmet = 3;
         
         pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countPredmet = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	  /* String rowN = "g"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
    countAbiturient = 0;
         
         pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and op  not like '1' and konkurs.npd is not NULL and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО')");
     //    pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and op  not like '1' and konkurs.npd is not NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО')");
         
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countAbiturient = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	/*   String rowN = "h"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
         
         
         pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where stob is NULL and kodspetsialnosti like ? and zach = 'д' and op  not like '1' and konkurs.npd is not NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО'))");
         pstmt.setObject(1, kodSpec);
         pstmt.setObject(2, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   int sumEGE = rs.getInt(1);
      	   float srBall = 0;
      	   BigDecimal bd;
      	   if (countAbiturient!=0){
      		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
      		   bd = new BigDecimal(Float.toString(srBall));
                 bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                 String rowN = "k"+rowIndex;
          	   cell = cells.get(rowN);
          	   cell.setValue(bd);
          	   
          	   
          	  count11dog++;
       	   summBall11dog = summBall11dog+srBall;
      	   }
      	  
      	 /* 
      	  // else srBall = 0;
      	   String rowN = "f"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(sumEGE);*/
      	  
      	   
      	       	   
      	  
         }
          
          
         
         
         
         rowIndex++;
       }
       
      
       
       cell = cells.get("n7");
	   cell.setValue("Средний балл");
	   cell = cells.get("o8");
	   cell.setValue("Бюд");
	   cell = cells.get("p8");
	   cell.setValue("Дог");
	   cell = cells.get("q8");
	   cell.setValue("Общ");
       
       srBall13bud = summBall13bud/(float)count13bud;
       srBall13dog = summBall13dog/(float)count13dog;
       
       srBall14bud = summBall14bud/(float)count14bud;
       srBall14dog = summBall14dog/(float)count14dog;
      
       srBall15bud = summBall15bud/(float)count15bud;
       srBall15dog = summBall15dog/(float)count15dog;
       
       srBall16bud = summBall16bud/(float)count16bud;
       srBall16dog = summBall16dog/(float)count16dog;
       
       srBall12bud = summBall12bud/(float)count12bud;
       srBall12dog = summBall12dog/(float)count12dog;
       
       srBall11bud = summBall11bud/(float)count11bud;
       srBall11dog = summBall11dog/(float)count11dog;
       
       
    
       
       BigDecimal bd = new BigDecimal(Float.toString(srBall13bud));
       bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
       String rowN = "o9";
	   cell = cells.get(rowN);
	   cell.setValue(bd);
	   cell = cells.get("n9");
	   cell.setValue("общ. Конкурс без творч. и стоб.:");
	   
	   ////////////////////дог/////////////////////////////////////////////
	   
       bd = new BigDecimal(Float.toString(srBall13dog));
       bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
      
	   cell = cells.get("p9");
	   cell.setValue(bd);
	   ///////////////////////////////////////////////////
	   
	  bd = new BigDecimal(Float.toString(srBall14bud));
	  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	      
	  cell = cells.get("o10");
	   cell.setValue(bd);
	  cell = cells.get("n10");
	  cell.setValue("общ. Конкурс без творч. + стоб.");
	  
	   ///////////////////////////////////////////////////
	  ////////////////////дог/////////////////////////////////////////////
	   
      bd = new BigDecimal(Float.toString(srBall14dog));
      bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
     
	   cell = cells.get("p10");
	   cell.setValue(bd);
	   
	   ///////////////////////////////////////////////////
	   
	  bd = new BigDecimal(Float.toString(srBall15bud));
	  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	      
	  cell = cells.get("o11");
	   cell.setValue(bd);
	  cell = cells.get("n11");
	  cell.setValue("общ. Конкурс с творч. без стоб.");
	  
	  ////////////////////дог/////////////////////////////////////////////
	   
      bd = new BigDecimal(Float.toString(srBall15dog));
      bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
     
	   cell = cells.get("p11");
	   cell.setValue(bd);
	 
		   
		 
	   ///////////////////////////////////////////////////
	   
	  bd = new BigDecimal(Float.toString(srBall16bud));
	  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	      
	  cell = cells.get("o12");
	   cell.setValue(bd);
	  cell = cells.get("n12");
	  cell.setValue("общ. Конкурс с творч. и стоб.:");
	   //////////////////////////дог/////////////////////////
	   
	  bd = new BigDecimal(Float.toString(srBall16dog));
	  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	      
	  cell = cells.get("p12");
	   cell.setValue(bd);
	 
	   ///////////////////////////////////////////////////
	 
	   ///////////////////////////////////////////////////
	   
	  bd = new BigDecimal(Float.toString(srBall12bud));
	  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	      
	  cell = cells.get("o13");
	   cell.setValue(bd);
	  cell = cells.get("n13");
	  cell.setValue("Цел. :");
	  
	  ////////////////////дог/////////////////////////////////////////////
	   
      bd = new BigDecimal(Float.toString(srBall12dog));
      bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
     
	   cell = cells.get("p13");
	   cell.setValue(bd);
	 
	   ///////////////////////////////////////////////////
	   
	   bd = new BigDecimal(Float.toString(srBall11bud));
		  bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
		      
		  cell = cells.get("o14");
		   cell.setValue(bd);
		  cell = cells.get("n14");
		  cell.setValue("Особые права.:");
		  
		  ////////////////////дог/////////////////////////////////////////////
		   
	     /* bd = new BigDecimal(Float.toString(srBall11dog));
	      bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	     
		   cell = cells.get("p14");
		   cell.setValue(bd);*/
		 
		   ///////////////////////////////////////////////////
		   
	   
	   
	   
	   
	   /****************************************СРЕДНИЙ БАЛЛ (бюджет + договор) *********************************************************/
	
	   //бюджет
       pstmt = conn.prepareStatement("Select Distinct k.kodspetsialnosti  from konkurs k , spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.eduLevel in ('б','с')");
       specs.clear();
       rs = pstmt.executeQuery();
       while(rs.next()){
      	 specs.add(new Integer(rs.getInt(1)));
       }
       itr = specs.iterator();
       rowIndex = 9;
      while(itr.hasNext()) {
         kodSpec = (Integer) itr.next();
         
         pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti, Tip_spec from spetsialnosti where kodspetsialnosti = ?");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   int countSpec = rs.getInt(1);
      	     	   
      	      	   
      	        	 
         }
         
       /***************************  без профильного  и стоб  ********************************************/
         
         
         int countPredmet = 3;
        
         pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countPredmet = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	 
      	   
         }
         
         
    int countAbiturient = 0;
         
         pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and konkurs.op = '1' and konkurs.target = '1'  and (konkurs.prof = '0' or konkurs.prof is null)");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countAbiturient = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	/*   String rowN = "h"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
         
         
         pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where stob is NULL and kodspetsialnosti like ? and zach = 'д'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and konkurs.op = '1' and konkurs.target = '1' and (konkurs.prof = '0' or konkurs.prof is null)) ");
         pstmt.setObject(1, kodSpec);
         pstmt.setObject(2, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   int sumEGE = rs.getInt(1);
      	   float srBall = 0;
      	   
      	   if (countAbiturient!=0){
      		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
      		   bd = new BigDecimal(Float.toString(srBall));
                 bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
               // rowN = "f"+rowIndex;  //!!!
          	   cell = cells.get(rowN);
          	   cell.setValue(bd);
          	   
          	   count13++;
          	   summBall13 = summBall13+srBall;
          	   
          	 
          	   
      	   }
      	  
      	 /* 
      	  // else srBall = 0;
      	   String rowN = "f"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(sumEGE);*/
      	  
      	   
      	       	   
      	  
         }
         
         /***************************  без профильного  + стоб  ********************************************/ 
          countPredmet = 3;
         pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countPredmet = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	  /* String rowN = "g"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
     countAbiturient = 0;
         
         pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and konkurs.op = '1' and konkurs.target = '1' and (konkurs.prof = '0' or konkurs.prof is null)");
         pstmt.setObject(1, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   countAbiturient = rs.getInt(1);
      	   int countSpec = rs.getInt(1);
      	/*   String rowN = "h"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(countSpec);*/
      	   
         }
         
         
         
         
         pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where kodspetsialnosti like ? and zach = 'д' and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and konkurs.op = '1' and konkurs.target = '1' and (konkurs.prof = '0' or konkurs.prof is null))");
         pstmt.setObject(1, kodSpec);
         pstmt.setObject(2, kodSpec);
         rs = pstmt.executeQuery();
         if(rs.next()){
      	   int sumEGE = rs.getInt(1);
      	   float srBall = 0;
      	  
      	   if (countAbiturient!=0){
      		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
      		   bd = new BigDecimal(Float.toString(srBall));
                 bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            
          	   
          	   count14++;
          	   summBall14 = summBall14+srBall;
      	   }
      	  
      	 /* 
      	  // else srBall = 0;
      	   String rowN = "f"+rowIndex;
      	   cell = cells.get(rowN);
      	   cell.setValue(sumEGE);*/
      	  
      	   
      	       	   
      	  
         } 
         
         
         
         /***************************  + профильный  - стоб  ********************************************/ 
         countPredmet = 3;
        pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? ");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countPredmet = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	  /* String rowN = "g"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
    countAbiturient = 0;
        
        pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1'");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countAbiturient = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	/*   String rowN = "h"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
        
        
        pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where  kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1')");
        pstmt.setObject(1, kodSpec);
        pstmt.setObject(2, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   int sumEGE = rs.getInt(1);
     	   float srBall = 0;
     	 
     	   if (countAbiturient!=0){
     		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
     		   bd = new BigDecimal(Float.toString(srBall));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                
         	   
         	 count15++;
    	   summBall15 = summBall15+srBall;
     	   }
     	  
     	 /* 
     	  // else srBall = 0;
     	   String rowN = "f"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(sumEGE);*/
     	  
     	   
     	       	   
     	  
        } 
         
         
         /***************************  + профильный  + стоб  ********************************************/ 
         countPredmet = 3;
        pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? ");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countPredmet = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	  /* String rowN = "g"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
    countAbiturient = 0;
        
        pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в')  and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1'");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countAbiturient = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	/*   String rowN = "h"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
        
        
        pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where  kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where kodspetsialnosti like ? and zach = 'д'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and konkurs.op = '1' and konkurs.target = '1')");
        pstmt.setObject(1, kodSpec);
        pstmt.setObject(2, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   int sumEGE = rs.getInt(1);
     	   float srBall = 0;
     	 
     	   if (countAbiturient!=0){
     		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
     		   bd = new BigDecimal(Float.toString(srBall));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
              
         	   
         	 count16++;
    	   summBall16 = summBall16+srBall;
     	   }
     	  
     	 /* 
     	  // else srBall = 0;
     	   String rowN = "f"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(sumEGE);*/
     	  
     	   
     	       	   
     	  
        } 
        
        
        //target
       countPredmet = 3;
        
        pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countPredmet = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	  /* String rowN = "g"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
   countAbiturient = 0;
        
        pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and stob is NULL and target  not like '1'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and (konkurs.prof = '0' or konkurs.prof is null)");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countAbiturient = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	/*   String rowN = "h"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
        
        
        pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where stob is NULL and kodspetsialnosti like ? and zach = 'д' and target  not like '1' and (konkurs.prof = '0' or konkurs.prof is null) and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО'))");
        pstmt.setObject(1, kodSpec);
        pstmt.setObject(2, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   int sumEGE = rs.getInt(1);
     	   float srBall = 0;
     	  
     	   if (countAbiturient!=0){
     		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
     		   bd = new BigDecimal(Float.toString(srBall));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
            
         	   
         	 count12++;
    	   summBall12 = summBall12+srBall;
     	   }
     	  
     	 /* 
     	  // else srBall = 0;
     	   String rowN = "f"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(sumEGE);*/
     	  
     	   
     	       	   
     	  
        }
         
      //льготы   
         
      countPredmet = 3;
        
        pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countPredmet = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	  /* String rowN = "g"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
   countAbiturient = 0;
        
        pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д' and op  not like '1'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and (konkurs.prof = '0' or konkurs.prof is null)");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   countAbiturient = rs.getInt(1);
     	   int countSpec = rs.getInt(1);
     	/*   String rowN = "h"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(countSpec);*/
     	   
        }
        
        
        
        
        pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where  (konkurs.prof = '0' or konkurs.prof is null) and kodspetsialnosti like ? and zach = 'д' and op  not like '1'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО'))");
        pstmt.setObject(1, kodSpec);
        pstmt.setObject(2, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   int sumEGE = rs.getInt(1);
     	   float srBall = 0;
     	 
     	   if (countAbiturient!=0){
     		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
     		   bd = new BigDecimal(Float.toString(srBall));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
             
         	   
         	 count11++;
    	   summBall11 = summBall11+srBall;
     	   }
     	  
     	 /* 
     	  // else srBall = 0;
     	   String rowN = "f"+rowIndex;
     	   cell = cells.get(rowN);
     	   cell.setValue(sumEGE);*/
     	  
     	   
     	       	   
     	  
        }
         
         
        
        
        
        rowIndex++;
      }
		   
		   
      srBall13 = summBall13/(float)count13;
     
      srBall14 = summBall14/(float)count14;
     
      srBall15 = summBall15/(float)count15;
     
      srBall16 = summBall16/(float)count16;
      
      srBall12 = summBall12/(float)count12;
      
      srBall11 = summBall11/(float)count11;
     
   
      
       bd = new BigDecimal(Float.toString(srBall13));
      bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
      
	   cell = cells.get("q9");
	   cell.setValue(bd);
	  
	 
      bd = new BigDecimal(Float.toString(srBall14));
      bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
     
	   cell = cells.get("q10");
	   cell.setValue(bd);
	 //////////////////////////////////////////// 
	   bd = new BigDecimal(Float.toString(srBall15));
	   bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	     
	  cell = cells.get("q11");
	  cell.setValue(bd);  
	   /////////////////////////////////////////////////// 
		 //////////////////////////////////////////// 
	   bd = new BigDecimal(Float.toString(srBall16));
	   bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	     
	  cell = cells.get("q12");
	  cell.setValue(bd);  
		 //////////////////////////////////////////// 
	   bd = new BigDecimal(Float.toString(srBall12));
	   bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	     
	  cell = cells.get("q13");
	  cell.setValue(bd);  
		 //////////////////////////////////////////// 
	   bd = new BigDecimal(Float.toString(srBall11));
	   bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	     
	  cell = cells.get("q14");
	  cell.setValue(bd);  
		 //////////////////////////////////////////// 
	  
/*************************************средний балл по всему вузу**********************************************/
	  pstmt = conn.prepareStatement("Select Distinct k.kodspetsialnosti  from konkurs k , spetsialnosti s where k.kodspetsialnosti = s.kodspetsialnosti and s.eduLevel in ('б','с')");
      specs.clear();
      rs = pstmt.executeQuery();
      while(rs.next()){
     	 specs.add(new Integer(rs.getInt(1)));
      }
      itr = specs.iterator();
      rowIndex = 9;
     while(itr.hasNext()) {
        kodSpec = (Integer) itr.next();
        
        pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti, Tip_spec from spetsialnosti where kodspetsialnosti = ?");
        pstmt.setObject(1, kodSpec);
        rs = pstmt.executeQuery();
        if(rs.next()){
     	   int countSpec = rs.getInt(1);
     	     	   
     	      	   
     	        	 
        }
	  
	    int countPredmet = 3;
	        
	        pstmt = conn.prepareStatement("select count (kodpredmeta)from ekzamenynaspetsialnosti where kodspetsialnosti like ? and kodpredmeta not like '12'");
	        pstmt.setObject(1, kodSpec);
	        rs = pstmt.executeQuery();
	        if(rs.next()){
	     	   countPredmet = rs.getInt(1);
	     	   int countSpec = rs.getInt(1);
	     	  /* String rowN = "g"+rowIndex;
	     	   cell = cells.get(rowN);
	     	   cell.setValue(countSpec);*/
	     	   
	        }
	        
	        
	   int countAbiturient = 0;
	        
	        pstmt = conn.prepareStatement("select distinct count (kodabiturienta) from konkurs where kodspetsialnosti like ? and zach = 'д'  and kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО') and (konkurs.prof = '0' or konkurs.prof is null)");
	        pstmt.setObject(1, kodSpec);
	        rs = pstmt.executeQuery();
	        if(rs.next()){
	     	   countAbiturient = rs.getInt(1);
	     	   int countSpec = rs.getInt(1);
	     	/*   String rowN = "h"+rowIndex;
	     	   cell = cells.get(rowN);
	     	   cell.setValue(countSpec);*/
	     	   
	        }
	        
	        
	        
	        
	        pstmt = conn.prepareStatement("select SUM(otsenkaege) from zajavlennyeShkolnyeOtsenki where   kodpredmeta not like '12' and kodpredmeta in (select kodpredmeta from ekzamenynaspetsialnosti where kodspetsialnosti like ?) and kodabiturienta in (select distinct kodabiturienta from konkurs where  (konkurs.prof = '0' or konkurs.prof is null) and kodspetsialnosti like ? and zach = 'д' and  kodabiturienta not in (select kodabiturienta from zajavlennyeshkolnyeotsenki where examen = 'в') and kodabiturienta not in (select kodabiturienta from abiturient where viddoksredobraz = 'Диплом СПО'))");
	        pstmt.setObject(1, kodSpec);
	        pstmt.setObject(2, kodSpec);
	        rs = pstmt.executeQuery();
	        if(rs.next()){
	     	   int sumEGE = rs.getInt(1);
	     	   float srBall = 0;
	     	 
	     	   if (countAbiturient!=0){
	     		   srBall = sumEGE/(float)countAbiturient/(float)countPredmet;
	     		   bd = new BigDecimal(Float.toString(srBall));
	                bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	             
	         	   
	         	 countObsh++;
	    	   summBallObsh = summBallObsh+srBall;
	     	   }
	     	  
	     	 /* 
	     	  // else srBall = 0;
	     	   String rowN = "f"+rowIndex;
	     	   cell = cells.get(rowN);
	     	   cell.setValue(sumEGE);*/
	     	  
	     	   
	     	       	   
	     	  
	        }
	         
	         
	        
	        
	        
	        rowIndex++;
	      }
	  
     srBallObsh = summBallObsh/(float)countObsh;
  //   srBallObsh = srBallObsh;
     
     
     
     bd = new BigDecimal(Float.toString(srBallObsh));
    bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
   
    
	   cell = cells.get("q17");
	   cell.setValue(bd);
	   
	   cell = cells.get("p17");
	   cell.setValue("ОБщий");

/*************************************средний балл по всему вузу**********************************************/

	
	  
	  
		   
        
        
        
        
        
        
         
       String ename = "Форма EXCEL  "+StringUtil.CurrDate(".")+" на "+StringUtil.CurrTime(":");

       String efile_con = new String("umu_excel_f1_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

  //     session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),ename,efile_con,"xls"));
       session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),ename,efile_con,"xls"));
       System.out.println("RPT session   " + ((ReportsBrowserBean)session.getAttribute("rpt")).getFileName());

       String efile_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
       
       
       
      
       
       System.out.println("EFILE_NAME  " + efile_name);
       workbook.save(efile_name);
       
    

       
       //workbook.save("D:\\monitoring.xls");
          //сохраняем файл
      //    workbook.save("D:\\output.xls");



         
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/


  
 // String name = "Список абитуриентов 1-го этапа (предв.) "+AF+" "+priority;

 // String file_con = "lists_"+StringUtil.toEng(AF)+"_predv_first_wave";

 
 // session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

 // String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
 
 

 
   
  //return mapping.findForward("rep_brw");

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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_ege_f) return mapping.findForward("lists_dec_ege_f");
        return mapping.findForward("rep_brw");
       // return mapping.findForward("success");
    }
}