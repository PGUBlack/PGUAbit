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

import abit.sql.*; 

public class UmuExcelAction extends Action {

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
          Workbook workbook = new Workbook();

          //добавляем новый лист Excel и получаем к нему доступ
          int sheetIndex = workbook.getWorksheets().add();
          Worksheet worksheet = workbook.getWorksheets().get(sheetIndex);
          Cells cells = worksheet.getCells();
          
          
            Cell cell = cells.get("A2");
            cell.setValue("2.4.1. Распределение приема студентов по направлениям подготовки и специальностям (мониторинг)");
            
            
          //  Наименование напрваления подготовки, специальности
            cell = cells.get("A4");
            cells.merge(3, 0, 3, 1);
            cells.setRowHeight(5, 100);
            cells.setColumnWidth(0, 40);
            
            Style style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue("Наименование напрваления подготовки, специальности");
            cell = cells.get("A7");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cell.setValue("1");
            
            //номер строки
            cell = cells.get("b4");
            cells.merge(3, 1, 3, 1);
          //  cells.setRowHeight(3, 100);
            cells.setColumnWidth(1, 8);
            
            style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue("№ Строки");
            cell = cells.get("B7");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cell.setValue("2");
            
            //Код направления подготовки
            cell = cells.get("c4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 2, 3, 1);
         
            cells.setColumnWidth(2, 8);
            
            style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue("Код направления подготовки");
            cell = cells.get("C7");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cell.setValue("3");
            
            
            //Подано заявлений
            cell = cells.get("d4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 3, 3, 1);
         
            cells.setColumnWidth(3, 8);
            
            style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue("Подано заявлений");
            cell = cells.get("d7");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cell.setValue("4");
            
            //Принято сумма
            cell = cells.get("e4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 4, 3, 1);
         
            cells.setColumnWidth(4, 8);
            
            style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue("Принято (сумма гр.6-9)");
            cell = cells.get("e7");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cell.setValue("5");
            
            
            //приняты на обучение
            
            cell = cells.get("f4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 5, 1, 4);
            cell.setValue("Приняты на обучение");
            
       //           за счет бюджетных ассигнований
            
            cell = cells.get("f5");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(4, 5, 1, 3);
            cell.setValue("за счет бюджетных ассигнований");
            
            //федерального бюджета
            cell = cells.get("f6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("федерального бюджета");
            
            
            //бюджета субъекта РФ
            cell = cells.get("g6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("бюджета субъекта РФ");
            
            //местного бюджета
            cell = cells.get("h6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("местного бюджета");
            
            
            
           // с полным возмещением стоимости обучения
            cell = cells.get("i5");
            style = cell.getStyle();
            cells.merge(4, 8, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("с полным возмещением стоимости обучения");
            
            
            
            
       //из них (из гр.5) по результатам целевого приема
  
            cell = cells.get("j4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 9, 3, 1);
         
            cells.setColumnWidth(9, 8);
            
            style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue("из них (из гр.5) по результатам целевого приема");
         
            
            
            
           // из них (из гр.5)принято на обучение для получения первого высшего образовани
            cell = cells.get("m4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 12, 3, 1);
         
            cells.setColumnWidth(12, 8);
            
            style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue("из них (из гр.5)принято на обучение для получения первого высшего образовани");
         
            //    
            cell = cells.get("n4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 13, 3, 1);
         
            cells.setColumnWidth(13, 8);
            
            style = cell.getStyle();
            style.setShrinkToFit(true);
            style.setTextWrapped(true);
            style.setVerticalAlignment(TextAlignmentType.CENTER);
            style.setIndentLevel(2);
            cell.setValue(" ");
            
            
            //предыдущее образование в другом регионе
            cell = cells.get("k6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("предыдущее образование в другом регионе");
            
            //диплом бакалавра, спциалиста или магистра в данной организации
            cell = cells.get("l6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("диплом бакалавра, спциалиста или магистра в данной организации");
            
            //мпобедители и призе-ры заключ.этапа всеросс.олимп.школ, члены сборн.команд РФ,участв.в между-нар.олимп.по обще-образ.предметам
            cell = cells.get("v6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("победители и призе-ры заключ.этапа всеросс.олимп.школ, члены сборн.команд РФ,участв.в между-нар.олимп.по обще-образ.предметам");
            
            //победители и призеры олимпиад школьников
            cell = cells.get("w6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("победители и призеры олимпиад школьников");
            
            //учтенных в гр.16
            cell = cells.get("x6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.16");
            
            //учтенных в гр.18
            cell = cells.get("y6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.18");
            
            //учтенных в гр.17
            cell = cells.get("z6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.17");
            
            //учтенных в гр.19
            cell = cells.get("aa6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.19");
            
            //учтенных в гр.16
            cell = cells.get("ab6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.16");
            
            //учтенных в гр.18
            cell = cells.get("ac6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.18");
            
            //учтенных в гр.17
            cell = cells.get("ad6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.17");
            
            //учтенных в гр.19
            cell = cells.get("ae6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.19");
            
            //учтенных в гр.10
            cell = cells.get("af6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.10");
            
            //учтенных в гр.15
            cell = cells.get("ag6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.15");
            
            
            //учтенных в гр.18
            cell = cells.get("ah6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.18");
            
            
            //учтенных в гр.19
            cell = cells.get("ai6");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("учтенных в гр.19");
            
            // из них  по программам акад.бак-та и спец-та
            cell = cells.get("o5");
            style = cell.getStyle();
            cells.merge(4, 14, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("из них  по программам акад.бак-та и спец-та");
            
            //по программам прикладного бакалавриата
            cell = cells.get("p5");
            style = cell.getStyle();
            cells.merge(4, 15, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("по программам прикладного бакалавриата");
            
            // по результатам ЕГЭ
            cell = cells.get("q5");
            style = cell.getStyle();
            cells.merge(4, 16, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("по результатам ЕГЭ");
            
            // из них (из гр.16) с полным возмеще-нием стоимости обучения
            cell = cells.get("r5");
            style = cell.getStyle();
            cells.merge(4, 17, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("из них (из гр.16) с полным возмеще-нием стоимости обучения");
            
            // по резуль-татам ЕГЭ и дополни-тельных испытаний
            cell = cells.get("s5");
            style = cell.getStyle();
            cells.merge(4, 18, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("по резуль-татам ЕГЭ и дополни-тельных испытаний");
            
            
            // из них (из гр.18) с полным возмещ. стоимости обучения
            cell = cells.get("t5");
            style = cell.getStyle();
            cells.merge(4, 19, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("из них (из гр.18) с полным возмещ. стоимости обучения");
            
            //лица, иеющие право на прием без вступитель-ных испытаний
            cell = cells.get("u5");
            style = cell.getStyle();
            cells.merge(4, 20, 2, 1);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("лица, иеющие право на прием без вступитель-ных испытаний");
            
            
            
            // из них (из графы 5) получивших
            cell = cells.get("k4");
            style = cell.getStyle();
            cells.merge(3, 10, 2, 2);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            
            cell.setValue("из них (из графы 5) получивших");
            
            //из них (из гр.13)
            
            cell = cells.get("o4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 14, 1, 2);
            cell.setValue("из них (из гр.13)");
            
           //из гр.14
            
            cell = cells.get("q4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 16, 1, 7);
            cell.setValue("из гр.14");
            
            
            //Средний минимальный балл ЕГЭ
        
            
            cell = cells.get("x4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 23, 1, 4);
            cell.setValue("из гр.14");
            
        //из гр.14
            
            cell = cells.get("ab4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 27, 1, 5);
            cell.setValue("из гр.14");
            
        //из гр.14
            
            cell = cells.get("ah4");
            style = cell.getStyle();
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cells.merge(3, 32, 1, 3);
            cell.setValue("из гр.14");
            
            
            //       из них (из гр.20)
                 
         cell = cells.get("v5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cells.merge(4, 21, 1, 2);
         cell.setValue("из них (из гр.20)");
         
         //     принятые на бюджет
         
         cell = cells.get("x5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cells.merge(4, 23, 1, 2);
         cell.setValue("принятые на бюджет");
         
         //     с полным возмещением
         
         cell = cells.get("z5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cells.merge(4, 25, 1, 2);
         cell.setValue("с полным возмещением");
         
         //    принятые на бюджет
         
         cell = cells.get("ab5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cells.merge(4, 27, 1, 2);
         cell.setValue("принятые на бюджет");
         
         //       с полным возмещением
         
         cell = cells.get("ad5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cells.merge(4, 29, 1, 2);
         cell.setValue("с полным возмещением");
         
         //       целевики
         
         cell = cells.get("af5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
       //  cells.merge(4, 21, 1, 2);
         cell.setValue("целевики");
         
         //      по программам прикладног бакалавриата
         
         cell = cells.get("ag5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
        // cells.merge(4, 21, 1, 2);
         cell.setValue("по программам прикладног бакалавриата");
         
         //      бюджет
         
         cell = cells.get("ah5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
       //  cells.merge(4, 21, 1, 2);
         cell.setValue("бюджет");
         
         //      дОГОВОР
         
         cell = cells.get("ai5");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
       //  cells.merge(4, 21, 1, 2);
         cell.setValue("договор");
         
         //6
         cell = cells.get("f7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("6");
         
         
         
         //7
         cell = cells.get("g7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("7");
            
         //8
         cell = cells.get("h7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("8");
         
         //10
         cell = cells.get("i7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("9");
         
         //11
         cell = cells.get("j7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("10");
         
         //12
         cell = cells.get("k7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("11");
         
         //13
         cell = cells.get("l7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("12");
         
         //14
         cell = cells.get("m7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("13");
         
         // 
         cell = cells.get("n7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue(" ");
         
         //15
         cell = cells.get("o7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("14");
         
       //16
         cell = cells.get("p7");
         style = cell.getStyle();
         style.setHorizontalAlignment(TextAlignmentType.CENTER);
         cell.setValue("15");
            
            
          
            
     /*     
          //Setting the line of the top border
          style.setBorder(BorderType.TOP_BORDER,CellBorderType.THICK,Color.getBlack());

          //Setting the line of the bottom border
          style.setBorder(BorderType.BOTTOM_BORDER,CellBorderType.THICK,Color.getBlack());

          //Setting the line of the left border
          style.setBorder(BorderType.LEFT_BORDER,CellBorderType.THICK,Color.getBlack());

          //Setting the line of the right border
          style.setBorder(BorderType.RIGHT_BORDER,CellBorderType.THICK,Color.getBlack());

          //Saving the modified style to the "A1" cell.
          cell.setStyle(style);*/
            
            
           
            
            
            
        //добавляем строку в ячейку A1
        /*  Cell cell = cells.get("A1");
          cell.setValue("Hello World");

          //добавляем double  в ячейку A2
          cell = cells.get("A2");
          cell.setValue(20.5);

          //заносим целое число в ячейку
          cell = cells.get("A3");
          cell.setValue(15);

          //добавляем boolean  в ячейку
          cell = cells.get("A4");
          cell.setValue(true);

          //заносим дату в ячейку
          cell = cells.get("A5");
          cell.setValue(Calendar.getInstance());

          //меняем формат отображения даты
          Style style = cell.getStyle();
          style.setNumber(15);
          cell.setStyle(style);*/

         
         pstmt = conn.prepareStatement("Select Distinct kodspetsialnosti  from konkurs");
         rs = pstmt.executeQuery();
         while(rs.next()){
        	 specs.add(new Integer(rs.getInt(1)));
         }
        Iterator itr = specs.iterator();
        int rowIndex = 9;
        while(itr.hasNext()) {
           kodSpec = (Integer) itr.next();
           
           pstmt = conn.prepareStatement("Select kodspetsialnosti, nazvaniespetsialnosti, shifrspetsialnosti from spetsialnosti where kodspetsialnosti = ?");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "a"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(rs.getString(2));
        	   rowN = "b"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(rs.getString(1));
        	   rowN = "c"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(rs.getString(3));
        	   
        	   
        	   
        	 
           }
           
           pstmt = conn.prepareStatement("SELECT COUNT(kodKonkursa) AS kodKonkursa FROM konkurs WHERE kodspetsialnosti = ?");
           pstmt.setObject(1, kodSpec);
           rs = pstmt.executeQuery();
           if(rs.next()){
        	   int countSpec = rs.getInt(1);
        	   String rowN = "m"+rowIndex;
        	   cell = cells.get(rowN);
        	   cell.setValue(countSpec);
        	  
           }
           
           rowIndex++;
         }
         
         
          //сохраняем файл
          workbook.save("D:\\output.xls");



         
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
        return mapping.findForward("success");
    }
}