package abit.action;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConstants;
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
import abit.util.StringUtil;
import abit.paket.PackageData;
import abit.paket.Root;
import abit.paket.TBasicDiplomaDocument;
import abit.paket.TEntranceTestSubject;
import abit.paket.THighEduDiplomaDocument;
import abit.paket.TIncomplHighEduDiplomaDocument;
import abit.paket.TMiddleEduDiplomaDocument;
import abit.paket.TSchoolCertificateDocument;


public class PrAction extends Action{
	
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
FisImportForm          form             = (FisImportForm) actionForm;
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
 String zach = "";
 
 OutputStreamWriter      wr               = null;

 PackageData packageData = new PackageData();

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
  
  //stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta, Prinjat From Abiturient WHERE vPrikaze LIKE 'д' and (KodSpetsialnZach =216 or KodSpetsialnZach = 217 or KodSpetsialnZach = 218)");
	  stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta From Abiturient INNER JOIN Spetsialnosti ON Spetsialnosti.KodSpetsialnosti = Abiturient.KodSpetsialnZach and (Spetsialnosti.Tip_Spec = 'о') and Abiturient.vPrikaze LIKE 'д' and abiturient.nomerpotoka = '1' and prinjat in ('2','3')");
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
  request.setAttribute("abits", abits);

}
  
  else if (form.getAction().equals("select")){
	  System.out.print("sdfsdf");
	  selectedItems = form.getSelectedItems();
	  selectedKods = form.getSelectedKods();
	  try {
		  
    		//создаем объект
		  int edForm=0, finSource=0, edLevel=0;
	  Root root = new Root();
		// PackageData packageData = new PackageData();
		  Root.AuthData authData = new Root.AuthData();
		  authData.setLogin("revalle88@gmail.com");
		  authData.setPass("Dreamlord88");
		  PackageData.OrdersOfAdmission ordersOfAdmission = new PackageData.OrdersOfAdmission();
	  for (int i=0;i<selectedKods.length; i++)
	  {
		 
		  kodAbiturienta = selectedKods[i];
		  
		  
		
		  
		  stmt = conn.prepareStatement("SELECT KodAbiturienta, DataInput, KodSpetsialnZach, Prinjat  FROM Abiturient WHERE KodAbiturienta = ? ORDER BY 1 ASC");
		  stmt.setObject(1,kodAbiturienta,Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
          	  PackageData.OrdersOfAdmission.OrderOfAdmission orderOfAdmission = new PackageData.OrdersOfAdmission.OrderOfAdmission();
          	  PackageData.OrdersOfAdmission.OrderOfAdmission.Application application = new PackageData.OrdersOfAdmission.OrderOfAdmission.Application();
          	  application.setApplicationNumber("2015-"+rs.getString(1));
          	  
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
         	  orderOfAdmission.setApplication(application);
         	 
         	  stmt1 = conn.prepareStatement("SELECT FisId, ShifrSpetsialnosti, eduLevel, tip_spec  FROM spetsialnosti WHERE KodSpetsialnosti = ? ORDER BY 1 ASC");
         	  stmt1.setObject(1,rs.getInt(3),Types.INTEGER);
              rs1 = stmt1.executeQuery();
              while (rs1.next()) {
            	 orderOfAdmission.setDirectionID(rs1.getLong(1)); //to_do change
            	 String kod = rs1.getString(3);
            	 if(kod.equals("б")){
            		 orderOfAdmission.setEducationLevelID((long) 2); //to_do edulevel
            		 edLevel=2;
           	 }
            	 if(kod.equals("м")){
            		 orderOfAdmission.setEducationLevelID((long) 4);
            		 edLevel=4;
            	 }
            	 if(kod.equals("с")){
            		 orderOfAdmission.setEducationLevelID((long) 5);
            		 edLevel=5;
            	 }
            	 if(kod.equals("02")){
            		 orderOfAdmission.setEducationLevelID((long) 17);
            		 edLevel=17;
            	 }
            	 
            	 
            	 if (rs1.getString(4).equals("о")){
            		 orderOfAdmission.setEducationFormID((long) 11);
            	 }
            	 else if(rs1.getString(1).equals("з")){
            		 orderOfAdmission.setEducationFormID((long) 10);
            	 }
            	 
            	 
            	 if (rs.getString(4).equals('д')){
            		 orderOfAdmission.setFinanceSourceID((long) 15); 
            	 }
            	 else
            	 {
            		 orderOfAdmission.setFinanceSourceID((long) 14); 
            	 }
            	 
            	 if (rs.getString(4).equals('2')){
            		 orderOfAdmission.setStage((long) 1);
            	 }
            	 else
            	 {
            		 orderOfAdmission.setStage((long) 2);
            	 }
            	 
            	 
            	 ordersOfAdmission.getOrderOfAdmission().add(orderOfAdmission);
            	 
            	 
              }
              
              
              /* 
               stmt1 = conn.prepareStatement("SELECT Forma_Ob, Bud, Dog, Target FROM Konkurs WHERE KodAbiturienta = ? AND KodSpetsialnosti = ? ORDER BY 1 ASC");
          	   stmt1.setObject(1,kodAbiturienta,Types.INTEGER);
          	   stmt1.setObject(2,rs.getInt(3),Types.INTEGER);
               rs1 = stmt1.executeQuery();
               while (rs1.next()) {
            	   if(rs1.getString(1).equals("о")||rs1.getString(1).equals("м")||rs1.getString(1).equals("ф")||rs1.getString(1).equals("ю")||rs1.getString(1).equals("п")||rs1.getString(1).equals("д")){
            		   orderOfAdmission.setEducationFormID((long) 11);
            		   edForm=11;
	            	  }else{
	            		  if(rs1.getString(1).equals("з")||rs1.getString(1).equals("н")||rs1.getString(1).equals("у")){
	            			  orderOfAdmission.setEducationFormID((long) 10);
	            			  edForm=10;
	            		  }else{
	            			  if(rs1.getString(1).equals("в")){
	            				  orderOfAdmission.setEducationFormID((long) 12);
	            				  edForm=12;
	            			  }
	            		  }
	            	  }
            	   
            	   
            	  
            	   
	            	  if (rs1.getString(4)!=null){
	            	  if(rs1.getString(4).equals("д")){
	            		  orderOfAdmission.setFinanceSourceID((long) 16);
	            		  finSource=16;
	            	  }
	            	  }else{
	            		  if (rs1.getString(3)!=null){
	            		  if(rs1.getString(3).equals("д")){
	            			  orderOfAdmission.setFinanceSourceID((long) 15); 
	            			  finSource=15;
	            		  }
	            		  }else{
	            			  if (rs1.getString(2)!=null){
	            			  if(rs1.getString(2).equals("д")){
	            				  orderOfAdmission.setFinanceSourceID((long) 14); 
	            				  finSource=14;
	            			  }
	            			  }
	            		  }
	            	  }
	            	  
	            	 
	            	  
               }
         	  */
         	  /*
         	  if((edForm==11 || edForm == 12) && finSource==14 && (edLevel==5 || edLevel == 2)){
	         	  if(rs.getString(4).equals("1")){
	         		 orderOfAdmission.setStage((long) 1);
	         	  }else{
	         		  if(rs.getString(4).equals("2")){
	         			 orderOfAdmission.setStage((long) 1);
	         		  }else{
	         			 orderOfAdmission.setStage((long) 2);
	         		  }
	         	  }
	         	  
	         	  
	         	  */
         	  }
         	 

            }
           
  		
		
		
	  
	  
	  	  packageData.setOrdersOfAdmission(ordersOfAdmission);
		  root.setPackageData(packageData);
		  root.setAuthData(authData);
  		//создаем выходной поток
  		File of = new File("D:/prikazy.xml");
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