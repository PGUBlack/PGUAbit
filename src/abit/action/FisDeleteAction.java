package abit.action;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import abit.bean.AbiturientBean;
import abit.bean.MessageBean;
import abit.bean.UserBean;
import abit.sql.UserConn;
import abit.paket.delete.*;



public class FisDeleteAction extends Action{
	
	   public ActionForward perform ( ActionMapping        mapping,
               ActionForm           actionForm,
               HttpServletRequest   request,
               HttpServletResponse  response )

throws IOException, ServletException
{   
HttpSession             session          = request.getSession();
Connection              conn             = null;
PreparedStatement       stmt             = null;
ResultSet               rs               = null;
PreparedStatement       stmt2             = null;
ResultSet               rs2               = null;
PreparedStatement       stmt3             = null;
ResultSet               rs3               = null;
ActionErrors            errors           = new ActionErrors();
ActionError             msg              = null;
FisDeleteForm          form             = (FisDeleteForm) actionForm;
//AbiturientBean          abit_F           = form.getBean(request, errors);
PreparedStatement stmt1          = null;
ResultSet         rs1            = null;
OutputStream os = null;
boolean                 fis_connect_f    = false;
boolean                 error            = false;
MessageBean             mess             = new MessageBean();
ActionForward           f                = null;
int                     kFak             = 1;
UserBean                user             = (UserBean)session.getAttribute("user");
String[] selectedItems = {}; 
 int kodAbiturienta;
 Integer[] selectedKods;
 String appNumber = "";
 String vidObraz = "";
 
 OutputStreamWriter      wr               = null;


ArrayList         abits       = new ArrayList();


if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
    msg = new ActionError( "logon.must" );
    errors.add( "logon.login", msg );
}

if ( errors.empty() ) {

request.setAttribute( "fisConnectAction", new Boolean(true) );
Locale locale = new Locale("ru","RU");
session.setAttribute( Action.LOCALE_KEY, locale );

try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

  UserConn us = new UserConn(request, mapping);
  conn = us.getConn(user.getSid());
  request.setAttribute( "fisConnectForm", form );

/*****************  Возврат к предыдущей странице   *******************/
  if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/	

  
  //тест отправки
 /* if (form.getAction().equals("test")){
	  
	  String url  =  "http://priem.edu.ru:8000/import/ImportService.svc/dictionary";
	  HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

     
      connection.setRequestProperty("Content-Type", "text/xml");

      connection.setRequestMethod("POST");

      connection.setDoOutput(true);
      connection.setReadTimeout(50*1000);

      connection.connect();
	  
   // Устанавливаем и отсылаем запрос на сервер

      wr = new OutputStreamWriter(connection.getOutputStream());

      String s = "<Root><AuthData><Login>revalle88@gmail.com</Login><Pass>Dreamlord88</Pass></AuthData></Root>";
      wr.write(s);

      wr.flush();

//Получаем статус ответа сервера

      int status = connection.getResponseCode();

      String buf_line = new String();

//Читаем ответ сервера в буфер ввода-вывода

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
      StringBuilder sb = new StringBuilder();

      while ((buf_line = in.readLine()) != null) {
            sb.append(buf_line);
      }

      buf_line = sb.toString();

      in.close();
System.out.println("Read From File ok");
	  
	  
  }
  
  //тест отправки
 
  
  
  else 
  */ if ( form.getAction() == null ) {
  
	  stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta From Abiturient INNER JOIN Spetsialnosti ON Spetsialnosti.KodSpetsialnosti = Abiturient.KodSpetsialnZach and (Spetsialnosti.Tip_Spec = 'м')");
  
  rs = stmt.executeQuery();
  while (rs.next()) {
	  AbiturientBean abit = new AbiturientBean();
	  abit.setKodAbiturienta(Integer.parseInt(rs.getString(5)));
	  abit.setImja(rs.getString(3));
	  abit.setFamilija(rs.getString(2));
	  abit.setNomerLichnogoDela(rs.getString(1));
	  abit.setOtchestvo(rs.getString(4));
	  abits.add(abit);      
  }
  request.setAttribute("abits1", abits);

}
  
  else if (form.getAction().equals("select")){
	  System.out.print("sdfsdf");
	  selectedItems = form.getSelectedItems();
	  selectedKods = form.getSelectedKods();
	  try {
		  
    		//создаем объект
		  Root root = new Root();
		  AuthData authData = new AuthData();
		  authData.setLogin("revalle88@gmail.com");
		  authData.setPass("Dreamlord88");
		  root.setAuthData(authData);
		  Root.DataForDelete dataForDelete = new Root.DataForDelete();
		  Root.DataForDelete.Applications applications = new Root.DataForDelete.Applications();
		  for (int i=0;i<selectedKods.length; i++)
		  {
			 
			  kodAbiturienta = selectedKods[i];
			  
			  
			
	  		 
			  stmt = conn.prepareStatement("SELECT NomerLichnogoDela,  DataInput FROM Abiturient WHERE KodAbiturienta = ? ORDER BY 1 ASC");
			  stmt.setObject(1,kodAbiturienta,Types.INTEGER);
	            rs = stmt.executeQuery();
	            while (rs.next()) {
	            	stmt1 = conn.prepareStatement("SELECT id FROM FisImport WHERE id = ? ORDER BY 1 ASC");
	  			  stmt1.setObject(1,kodAbiturienta,Types.INTEGER);
	  	            rs1 = stmt1.executeQuery();
	  	            while (rs1.next()) {
	             Root.DataForDelete.Applications.Application application = new Root.DataForDelete.Applications.Application();
	             application.setApplicationNumber(rs1.getString(1));
	             
	             XMLGregorianCalendar result = null;
	          	  Date date;
	          	  SimpleDateFormat simpleDateFormat;
	          	  GregorianCalendar gregorianCalendar;
	          	  
	             simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
	          	  date = simpleDateFormat.parse(rs.getString(2));        
	          	  gregorianCalendar = 
	          	                     (GregorianCalendar)GregorianCalendar.getInstance();
	          	  gregorianCalendar.setTime(date);
	          	  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	          	  application.setRegistrationDate(result);
	          	  applications.getApplication().add(application);
	  	            }
	            }
	           
	            
		  }
		  dataForDelete.setApplications(applications);
		  root.setDataForDelete(dataForDelete);
		  
		//создаем выходной поток
	  		File of = new File("D:/deleteAbit.xml");
	  		os = new FileOutputStream(of);
	  		// Маршаллизация
	  		JAXBContext context = JAXBContext.newInstance(Root.class);
	  		Marshaller m = context.createMarshaller();
	  		m.marshal(root, os);
	  		os.close();
	  
  		
		} catch (IOException ex) {
			;
		}
	  
	  
	  
	  
  }
  
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
    if ( conn != null ) {
         try {
               conn.close();
             } catch (Exception e) {
              ;
                                   }
    }
  }
	   
	   
	   
	  
	   
	   
	   
	   
	   
	   
	   
}

return mapping.findForward("success");

}
	   
}