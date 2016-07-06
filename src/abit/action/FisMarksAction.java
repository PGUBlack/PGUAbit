package abit.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.math.BigDecimal;
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
import abit.paket.TInstitutionDocument;
import abit.paket.THighEduDiplomaDocument;
import abit.paket.TIncomplHighEduDiplomaDocument;
import abit.paket.TMiddleEduDiplomaDocument;
import abit.paket.TSchoolCertificateDocument;

import java.io.*;
import java.nio.charset.Charset;


public class FisMarksAction extends Action{
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
 int kSpec=0;
 BigDecimal bd;
 StringBuffer data = new StringBuffer();
 StringBuffer resp = new StringBuffer();
 String reqXML;
 String zapros="";
 String kodi="";
 URL openURL = null;
 openURL = new URL("http://10.0.3.1/wschecks.asmx");
 OutputStreamWriter      wr               = null;
 AbiturientBean abit = new AbiturientBean();

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
	UserConn us = new UserConn(request, mapping);
	  conn = us.getConn(user.getSid());
	  request.setAttribute( "fisConnectForm", form );

	 if(us.quit("exit")) return mapping.findForward("back");


/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/
	/*
	 if(form.getAction()==null){
		 stmt = conn.prepareStatement("select batchid, status, date from batchcheck");
		   rs = stmt.executeQuery();
		   while (rs.next()) {
			   abit.setFamilija(rs.getString(1));
			   abit.setImja(rs.getString(2));
			   abit.setOtchestvo(rs.getString(3));
			   abits.add(abit);
		   }
		   
	 
	 }else if(form.getAction().equals("send")){
	   */
	   stmt = conn.prepareStatement("select distinct familija, imja, otchestvo, nomerdokumenta, seriadokumenta, kodabiturienta from abiturient where nomerpotoka=1 and viddoksredobraz like '%аттестат%'");
	   rs = stmt.executeQuery();
	   while (rs.next()) {
		   kodi=kodi+rs.getString(6)+",";
	   zapros=zapros+"<query><lastName>"+rs.getString(1)+"</lastName>"+ 
	   "<firstName>"+rs.getString(2)+"</firstName>"+ 
	   "<patronymicName>"+rs.getString(3)+"</patronymicName>"+ 
	   "<passportSeria>"+rs.getString(4)+"</passportSeria>"+ 
	   "<passportNumber>"+rs.getString(5)+"</passportNumber>"+ 
	   "<certificateNumber></certificateNumber>"+ 
	   "<typographicNumber></typographicNumber>"+ 
	   "</query>";
	   }
	   kodi=kodi+rs.getString(6);
	   
       HttpURLConnection con = (HttpURLConnection) openURL.openConnection();
       con.setRequestMethod("POST");
       con.setRequestProperty("Host", "10.0.3.1");
       con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
       con.setRequestProperty("Content-lenght","5000");
       con.setRequestProperty("SOAPAction","urn:fbs:v2/BatchCheck");
       con.setDoOutput(true);
       con.setDoInput(true);
       reqXML="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
	            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+	
    		   "<soap:Header>"+
	            "<UserCredentials xmlns=\"urn:fbs:v2\">"+ 
    		   "<Client>revalle88@gmail.com</Client>"+ 
	            "<Login>revalle88@gmail.com</Login>"+ 
    		   "<Password>Dreamlord88</Password>"+ 
	            "</UserCredentials>"+ 
    		   "</soap:Header>"+ 
	            "<soap:Body>"+
	            "<BatchCheck xmlns=\"urn:fbs:v2\">"+ 
	            "<queryXML>"+ 
	            "<![CDATA["+ 
	            "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+ 
	            "<items>"+ 
	            zapros+
	            "</items>]]>"+ 
	            "</queryXML>"+ 
	            "</BatchCheck>"+ 
	            "</soap:Body>"+
	          "</soap:Envelope>";

       
    //   DataOutputStream reqStream = new DataOutputStream(con.getOutputStream());
     OutputStream reqStream = con.getOutputStream();
       reqStream.write(reqXML.getBytes(Charset.forName("UTF-8")));
       reqStream.flush();
       con.connect();
       
       // получение ответа сервера
       int responseCode = con.getResponseCode();
       String respMessage = con.getResponseMessage();
       System.out.println(responseCode);
       resp.append(responseCode+" ");
       resp.append(respMessage+" ");
       if (responseCode == 200) { // если все прошло нормально(200), получаем результат
    	   
           InputStream in = con.getInputStream();
           InputStreamReader isr = new InputStreamReader(in, "UTF-8");               
           int c;
          while ((c = isr.read()) != -1) {  data.append((char) c);  }
          int k=data.indexOf("batchId")+11;
          int e=data.indexOf("batchId",k)-5;
          stmt = conn.prepareStatement("INSERT INTO BatchCheck(BatchId, Status, Date) VALUES ('"+data.substring(k,e)+"','Обрабатывается','"+StringUtil.CurrDate(".")+"')");
          stmt.executeQuery();
       }
       
       /*
       
       stmt = conn.prepareStatement("select batchid, status, date from batchcheck");
	   rs = stmt.executeQuery();
	   while (rs.next()) {
		   abit.setFamilija(rs.getString(1));
		   abit.setImja(rs.getString(2));
		   abit.setOtchestvo(rs.getString(3));
		   abits.add(abit);
	   }
       
       
   //--------------------------------------------------------------------------------------------------------------//
   //--------------------------------------------------------------------------------------------------------------//
       
	 }else 
	if(form.getAction().equals("pick")){
		  
		 String batchId=request.getParameter("familija");
       HttpURLConnection con1 = (HttpURLConnection) openURL.openConnection();
       con1.setRequestMethod("POST");
       con1.setRequestProperty("Host", "10.0.3.1");
       con1.setRequestProperty("Content-type", "text/xml; charset=utf-8");
       con1.setRequestProperty("Content-lenght","5000");
       con1.setRequestProperty("SOAPAction","urn:fbs:v2/GetBatchCheckResult");
       con1.setDoOutput(true);
       con1.setDoInput(true);
       reqXML="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
	            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+	
	            "<soap:Header>"+
	            	"<UserCredentials xmlns=\"urn:fbs:v2\">"+ 
	            		"<Client>revalle88@gmail.com</Client>"+ 
	            		"<Login>revalle88@gmail.com</Login>"+ 
	            		"<Password>Dreamlord88</Password>"+ 
	            	"</UserCredentials>"+ 
	            "</soap:Header>"+ 
	            "<soap:Body>"+
	            "<GetBatchCheckResult xmlns=\"urn:fbs:v2\">"+ 
	            "<queryXML>"+ 
	            "<![CDATA["+ 
	            "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+ 
	            "<items>"+ 
	            "<batchId>"+batchId+"</batchId>"+
	            "</items>]]>"+ 
	            "</queryXML>"+ 
	            "</GetBatchCheckResult>"+ 
	            "</soap:Body>"+
	          "</soap:Envelope>";
       OutputStream reqStream1 = con1.getOutputStream();
       reqStream1.write(reqXML.getBytes(Charset.forName("UTF-8")));
       reqStream1.flush();
       con1.connect();
       
       int responseCode = con1.getResponseCode();
       String respMessage = con1.getResponseMessage();
       
              if (responseCode == 200) { // если все прошло нормально(200), получаем результат
                  InputStream in = con1.getInputStream();
                  InputStreamReader isr = new InputStreamReader(in, "UTF-8");               
                  int c;
                 while ((c = isr.read()) != -1 ) {  data.append((char) c);  }
                 
                 
              }else{
            	  stmt = conn.prepareStatement("update batchcheck set status='Ошибка "+responseCode+"' where batchid like "+batchId+"");
     		   		rs = stmt.executeQuery(); 
              }

       
       
              stmt = conn.prepareStatement("select batchid, status, date from batchcheck");
   		   		rs = stmt.executeQuery();
   		   	while (rs.next()) {
   			   abit.setFamilija(rs.getString(1));
   			   abit.setImja(rs.getString(2));
   			   abit.setOtchestvo(rs.getString(3));
   			   abits.add(abit);
   		   }
     
	 }*/
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
request.setAttribute("abits", abits);
return mapping.findForward("success");

}
	   
}
	


