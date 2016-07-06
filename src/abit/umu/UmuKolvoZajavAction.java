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

public class UmuKolvoZajavAction extends Action {

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
        
          Cell cell = cells.get("B1");
          cells.setColumnWidth(0, 150);
          cell.setValue("Количество поданных заявлений в ПГУ на");
          
          cell = cells.get("F1");
          cells.setColumnWidth(0, 100);          
          cell.setValue(StringUtil.CurrDate("."));
          
                    
          cell = cells.get("G1");
          cells.setColumnWidth(0, 100);          
          cell.setValue("по"); 
          
          cell = cells.get("B2");
          cells.setColumnWidth(0, 100);          
          cell.setValue("факультетам и институтам"); 

          cell = cells.get("A3");
          cells.setColumnWidth(0, 100);
          cell.setValue("Наименование подразделения"); 

          cell = cells.get("B3");
          cells.setColumnWidth(0, 100);
          cell.setValue("Количество");           
          
          cell = cells.get("B4");
          cells.setColumnWidth(0, 100);
          cell.setValue("заявлений");            
          
          cell = cells.get("B5");
          cells.setColumnWidth(0, 100);
          cell.setValue("Всего");  
          
          cell = cells.get("C3");
          cells.setColumnWidth(0, 100);
          cell.setValue("Количество");           
          
          cell = cells.get("C4");
          cells.setColumnWidth(0, 100);
          cell.setValue("абитуриентов");            
          
          cell = cells.get("C5");
          cells.setColumnWidth(0, 100);
          cell.setValue("Всего");  
          
          cell = cells.get("D3");
          cells.setColumnWidth(0, 100);
          cell.setValue("Количество");           
          
          cell = cells.get("D4");
          cells.setColumnWidth(0, 100);
          cell.setValue("абитуриентов");            
          
          cell = cells.get("D5");
          cells.setColumnWidth(0, 100);
          cell.setValue("Сегодня");
          
          cell = cells.get("A6");
          cells.setColumnWidth(0, 100);
          cell.setValue("Политехнический институт:"); 
          
          cell = cells.get("A7");
          cells.setColumnWidth(0, 100);
          cell.setValue("ФВТ");      
          
          cell = cells.get("A8");
          cells.setColumnWidth(0, 100);
          cell.setValue("ФПИТЭ");   
          
          cell = cells.get("A9");
          cells.setColumnWidth(0, 100);
          cell.setValue("ФМТ");   
          
          cell = cells.get("A10");
          cells.setColumnWidth(0, 100);
          cell.setValue("Заочное отделение ПИ");  
          
          cell = cells.get("A11");
          cells.setColumnWidth(0, 100);
          cell.setValue("Педагогический институт им. В.Г. Белинского:"); 
          
          cell = cells.get("A12");
          cells.setColumnWidth(0, 100);
          cell.setValue("ИФФ"); 
          
          cell = cells.get("A13");
          cells.setColumnWidth(0, 100);
          cell.setValue("ФППИСН"); 
          
          cell = cells.get("A14");
          cells.setColumnWidth(0, 100);
          cell.setValue("ФФМЕН"); 
          
          cell = cells.get("A15");
          cells.setColumnWidth(0, 100);
          cell.setValue("Заочное отделение ПИ им. В.Г. Белинского");  
          
          cell = cells.get("A16");
          cells.setColumnWidth(0, 100);
          cell.setValue("Медицинский институт:"); 
          
          cell = cells.get("A17");
          cells.setColumnWidth(0, 100);
          cell.setValue("Лечебный факультет"); 
          
          cell = cells.get("A18");
          cells.setColumnWidth(0, 100);
          cell.setValue("Факультет стоматологии"); 
          
          cell = cells.get("A19");
          cells.setColumnWidth(0, 100);
          cell.setValue("Институт физкультуры и спорта"); 
              
          cell = cells.get("A20");
          cells.setColumnWidth(0, 100);
          cell.setValue("Факультет экономики и управления"); 
          
          cell = cells.get("A21");
          cells.setColumnWidth(0, 100);
          cell.setValue("Юридический факультет"); 
          
          cell = cells.get("A22");
          cells.setColumnWidth(0, 100);
          cell.setValue("Многопрофильный колледж"); 
          
          cell = cells.get("A23");
          cells.setColumnWidth(0, 100);
          cell.setValue("Интернатура/Ординатура"); 
          
          cell = cells.get("A24");
          cells.setColumnWidth(0, 100);
          cell.setValue("Остальное"); 
          
          cell = cells.get("A25");
          cells.setColumnWidth(0, 100);
          cell.setValue("ВСЕГО");      
          
          cell = cells.get("A26");
          cells.setColumnWidth(0, 100);
          cell.setValue("Ответственный секретарь ПК");    
          
          cell = cells.get("D26");
          cells.setColumnWidth(0, 100);
          cell.setValue("В.А.Соловьёв");   

          // Количество заявлений - Политехнический институт
        pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('1', '4', '10', '14')");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN = "b6";
     	   cell = cells.get(rowN);
     	   
     	  cell.setValue(rs.getString(1));
        }
          
          
          // Количество заявлений - ФВТ
        pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '1'");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN = "b7";
     	   cell = cells.get(rowN);
     	   
     	  cell.setValue(rs.getString(1));
        }
          
        // Количество заявлений - ФПИТЭ
      pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '4'");
      rs = pstmt.executeQuery();
      while(rs.next()){
   	   String rowN = "b8";
   	   cell = cells.get(rowN);
   	   
   	  cell.setValue(rs.getString(1));
      }
        
      // Количество заявлений - ФМТ
    pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '10'");
    rs = pstmt.executeQuery();
    while(rs.next()){
 	   String rowN = "b9";
 	   cell = cells.get(rowN);
 	   
 	  cell.setValue(rs.getString(1));
    }
          
    // Количество заявлений - ЗОПИ
  pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '14'");
  rs = pstmt.executeQuery();
  while(rs.next()){
	   String rowN = "b10";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
  }
  
  // Количество заявлений - Педагогический институт им. В.Г.Белинского
pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('21', '22', '18', '19')");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "b11";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}
    
  // Количество заявлений - ИФФ
pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '21'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "b12";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}
  
// Количество заявлений - ФППИСН
pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '22'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "b13";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ФФМЕН
pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '18'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "b14";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ЗОПЕД
pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '19'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "b15";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - Медицинский институт
pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('33', '32')");
rs = pstmt.executeQuery();
while(rs.next()){
	String rowN = "b16";
	cell = cells.get(rowN);
   
	cell.setValue(rs.getString(1));
}

	//Количество заявлений - Лечебный факультет
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '33'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b17";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}

	//Количество заявлений - ФС
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '32'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b18";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - ИФКИС
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '20'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b19";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}

	//Количество заявлений - ФЭиУ
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '6'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b20";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - ЮФ
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '8'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b21";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - МК
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '35'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b22";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - Ординатура/интернатура
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('30', '31')");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b23";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - остальное
	pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta not in ('1', '4', '6', '8', '10', '14', '18', '19', '20', '21', '22', '30', '31', '32', '33', '35')");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "b24";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
          // Количество заявлений - всего
        pstmt = conn.prepareStatement("select count (k.kodkonkursa) from konkurs k, spetsialnosti s, fakultety f where k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta");
        rs = pstmt.executeQuery();
        while(rs.next()){
     	   String rowN = "b25";
     	   cell = cells.get(rowN);
     	   
     	  cell.setValue(rs.getString(1));
        }
        
        // Количество заявлений - Политехнический институт
      pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('1', '4', '10', '14')");
      rs = pstmt.executeQuery();
      while(rs.next()){
   	   String rowN = "c6";
   	   cell = cells.get(rowN);
   	   
   	  cell.setValue(rs.getString(1));
      }
        
        
        // Количество заявлений - ФВТ
      pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '1'");
      rs = pstmt.executeQuery();
      while(rs.next()){
   	   String rowN = "c7";
   	   cell = cells.get(rowN);
   	   
   	  cell.setValue(rs.getString(1));
      }
        
      // Количество заявлений - ФПИТЭ
    pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '4'");
    rs = pstmt.executeQuery();
    while(rs.next()){
 	   String rowN = "c8";
 	   cell = cells.get(rowN);
 	   
 	  cell.setValue(rs.getString(1));
    }
      
    // Количество заявлений - ФМТ
  pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '10'");
  rs = pstmt.executeQuery();
  while(rs.next()){
	   String rowN = "c9";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
  }
        
  // Количество заявлений - ЗОПИ
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '14'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "c10";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

// Количество заявлений - Педагогический институт им. В.Г.Белинского
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('21', '22', '18', '19')");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "c11";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}
  
// Количество заявлений - ИФФ
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '21'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "c12";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ФППИСН
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '22'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "c13";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ФФМЕН
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '18'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "c14";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ЗОПЕД
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '19'");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "c15";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - Медицинский институт
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('33', '32')");
rs = pstmt.executeQuery();
while(rs.next()){
	String rowN = "c16";
	cell = cells.get(rowN);
 
	cell.setValue(rs.getString(1));
}

	//Количество заявлений - Лечебный факультет
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '33'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c17";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}

	//Количество заявлений - ФС
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '32'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c18";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - ИФКИС
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '20'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c19";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}

	//Количество заявлений - ФЭиУ
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '6'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c20";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - ЮФ
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '8'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c21";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - МК
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '35'");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c22";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - Ординатура/интернатура
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('30', '31')");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c23";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - остальное
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta not in ('1', '4', '6', '8', '10', '14', '18', '19', '20', '21', '22', '30', '31', '32', '33', '35')");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "c24";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
        // Количество заявлений - всего
      pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta");
      rs = pstmt.executeQuery();
      while(rs.next()){
   	   String rowN = "c25";
   	   cell = cells.get(rowN);
   	   
   	  cell.setValue(rs.getString(1));
      }
        
      // Количество заявлений - Политехнический институт
    pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('1', '4', '10', '14') and a.datainput = convert(varchar(30), getdate(), 104)");
    rs = pstmt.executeQuery();
    while(rs.next()){
 	   String rowN = "d6";
 	   cell = cells.get(rowN);
 	   
 	  cell.setValue(rs.getString(1));
    }
      
      
      // Количество заявлений - ФВТ
    pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '1' and a.datainput = convert(varchar(30), getdate(), 104)");
    rs = pstmt.executeQuery();
    while(rs.next()){
 	   String rowN = "d7";
 	   cell = cells.get(rowN);
 	   
 	  cell.setValue(rs.getString(1));
    }
      
    // Количество заявлений - ФПИТЭ
  pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '4' and a.datainput = convert(varchar(30), getdate(), 104)");
  rs = pstmt.executeQuery();
  while(rs.next()){
	   String rowN = "d8";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
  }
    
  // Количество заявлений - ФМТ
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '10' and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "d9";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}
      
// Количество заявлений - ЗОПИ
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '14' and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "d10";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - Педагогический институт им. В.Г.Белинского
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('21', '22', '18', '19') and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "d11";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ИФФ
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '21' and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "d12";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ФППИСН
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '22' and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "d13";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ФФМЕН
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '18' and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "d14";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - ЗОПЕД
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '19' and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	   String rowN = "d15";
	   cell = cells.get(rowN);
	   
	  cell.setValue(rs.getString(1));
}

//Количество заявлений - Медицинский институт
pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('33', '32') and a.datainput = convert(varchar(30), getdate(), 104)");
rs = pstmt.executeQuery();
while(rs.next()){
	String rowN = "d16";
	cell = cells.get(rowN);

	cell.setValue(rs.getString(1));
}

	//Количество заявлений - Лечебный факультет
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '33' and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d17";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}

	//Количество заявлений - ФС
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '32' and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d18";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - ИФКИС
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '20' and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d19";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}

	//Количество заявлений - ФЭиУ
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '6' and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d20";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - ЮФ
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '8' and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d21";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - МК
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta = '35' and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d22";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - Ординатура/интернатура
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta in ('30', '31') and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d23";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
	
	//Количество заявлений - остальное
	pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and f.kodfakulteta not in ('1', '4', '6', '8', '10', '14', '18', '19', '20', '21', '22', '30', '31', '32', '33', '35')  and a.datainput = convert(varchar(30), getdate(), 104)");
	rs = pstmt.executeQuery();
	while(rs.next()){
		String rowN = "d24";
		cell = cells.get(rowN);
	   
		cell.setValue(rs.getString(1));
	}
      
      // Количество заявлений - всего (текущая дата)
    pstmt = conn.prepareStatement("select count (a.kodabiturienta) from abiturient a, spetsialnosti s, fakultety f where a.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta = f.kodfakulteta and a.datainput = convert(varchar(30), getdate(), 104)");
    rs = pstmt.executeQuery();
    while(rs.next()){
 	   String rowN = "d25";
 	   cell = cells.get(rowN);
 	   
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