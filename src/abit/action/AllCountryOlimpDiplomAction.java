package abit.action;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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


import abit.bean.AllCountryOlimpDiplomBean;
import abit.bean.UserBean;
import abit.sql.UserConn;
import abit.paket.Root;

public class AllCountryOlimpDiplomAction extends Action {
	
	 public ActionForward perform ( ActionMapping        mapping,
             ActionForm           actionForm,
             HttpServletRequest   request,
             HttpServletResponse  response )
	 
	  throws IOException, ServletException
	    {
		 HttpSession       session       = request.getSession();
		 Connection        conn          = null;
		 Connection        conn1          = null;
		 AllCountryOlimpDiplomForm    form          = (AllCountryOlimpDiplomForm) actionForm;
		 UserBean          user          = (UserBean)session.getAttribute("user");
	     ActionErrors      errors        = new ActionErrors();
	     ActionError       msg           = null;
	     boolean           error         = false;
	     PreparedStatement stmt          = null;
	     ResultSet         rs            = null;
	     PreparedStatement stmt1          = null;
	     ResultSet         rs1            = null;
	     AllCountryOlimpDiplomBean       list        = form.getBean(request, errors);
	     OutputStream os = null;
		 
		 if (user==null || user.getGroup()==null || !(user.getGroup().getTypeId()==1 || user.getGroup().getTypeId()==3)) {
	            msg = new ActionError( "logon.must" );
	            errors.add( "logon.login", msg );
	        }

	        if ( errors.empty() ) {

	        request.setAttribute( "allCountryOlimpDiplomAction", new Boolean(true) );
	        Locale locale = new Locale("ru","RU");
	        session.setAttribute( Action.LOCALE_KEY, locale );

	        try{

	/**********************************************************************/
	/*********  Получение соединения с БД и ведение статистики  ***********/

	          UserConn us = new UserConn(request, mapping);
	          conn = us.getConn(user.getSid());
	          conn1 = us.getConn(user.getSid());
	          request.setAttribute( "allCountryOlimpDiplomForm", form );

	/*****************  Возврат к предыдущей странице   *******************/
	          if(us.quit("exit")) return mapping.findForward("back"); 
	/**********************************************************************/
	/*****************  Изменение второго текстого поля   *******************/
	          if (form.getAction().equals("create")) {
	              
	        	  try {
	        		  
		        		//создаем объект
	        		  int kodAbiturienta=13;
	        		  Root root = new Root();
	        		  Root.PackageData packageData = new Root.PackageData();
	        		  Root.PackageData.Applications applications = new Root.PackageData.Applications();
	        		  stmt = conn.prepareStatement("SELECT KodAbiturienta, NomerLichnogoDela, Familija, Imja, Otchestvo, Pol, NujdaetsjaVObschejitii, DataInput, DataModify, SeriaDokumenta, NomerDokumenta, DataVydDokumenta, KemVydDokument, TipDokumenta, Grajdanstvo, DataRojdenija FROM Abiturient WHERE KodAbiturienta = ? ORDER BY 1 ASC");
	        		  stmt.setObject(1,kodAbiturienta,Types.INTEGER);
		              rs = stmt.executeQuery();
		              while (rs.next()) {
		            	  Root.PackageData.Applications.Application applic = new Root.PackageData.Applications.Application();
		            	  applic.setUID(rs.getString(1));
		            	  applic.setApplicationNumber(rs.getString(2));
		            	  Root.PackageData.Applications.Application.Entrant entrant = new Root.PackageData.Applications.Application.Entrant();
		            	  entrant.setUID(rs.getString(1));
		            	  entrant.setFirstName(rs.getString(4));
		            	  entrant.setMiddleName(rs.getString(5));
		            	  entrant.setLastName(rs.getString(3));
		            	  if(rs.getString(6).equals("м")){
		            		  entrant.setGenderID(1);
		            	  }else{
		            		  entrant.setGenderID(2);
		            	  }
		            	  applic.setEntrant(entrant);
		            	  XMLGregorianCalendar result = null;
		            	  Date date;
		            	  SimpleDateFormat simpleDateFormat;
		            	  GregorianCalendar gregorianCalendar;
		            	  
		            	  simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		            	  date = simpleDateFormat.parse(rs.getString(8));        
		            	  gregorianCalendar = 
		            	                     (GregorianCalendar)GregorianCalendar.getInstance();
		            	  gregorianCalendar.setTime(date);
		            	  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		            	  applic.setRegistrationDate(result);
		            	  simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
     	                  date = simpleDateFormat.parse(rs.getString(9));        
     	                  gregorianCalendar = 
     	                     (GregorianCalendar)GregorianCalendar.getInstance();
     	                  gregorianCalendar.setTime(date);
     	                  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		            	  applic.setLastDenyDate(result);
		            	  if(rs.getString(7).equals("н")){
		            		  applic.setNeedHostel(false);
		            	  }else{
		            		  applic.setNeedHostel(true);
		            	  }
		            	  Root.PackageData.Applications.Application.FinSourceAndEduForms finSourceAndEduForms = new Root.PackageData.Applications.Application.FinSourceAndEduForms();
		            	  stmt1 = conn1.prepareStatement("SELECT Forma_Ob, Bud, Dog, Target FROM Konkurs WHERE KodAbiturienta = ? ORDER BY 1 ASC");
		        		  stmt1.setObject(1,kodAbiturienta,Types.INTEGER);
			              rs1 = stmt1.executeQuery();
			              while (rs1.next()) {
			            	  Root.PackageData.Applications.Application.FinSourceAndEduForms.FinSourceEduForm finSourceEduForm = new Root.PackageData.Applications.Application.FinSourceAndEduForms.FinSourceEduForm();
			            	  if(rs1.getString(1).equals("о")||rs1.getString(1).equals("м")||rs1.getString(1).equals("ф")||rs1.getString(1).equals("ю")||rs1.getString(1).equals("п")||rs1.getString(1).equals("д")){
			            		  finSourceEduForm.setEducationFormID(11);
			            	  }else{
			            		  if(rs1.getString(1).equals("з")||rs1.getString(1).equals("н")){
			            			  finSourceEduForm.setEducationFormID(10);
			            		  }else{
			            			  if(rs1.getString(1).equals("в")){
			            				  finSourceEduForm.setEducationFormID(12);
			            			  }
			            		  }
			            	  }
			            	  if(rs1.getString(2).equals("д")){
			            		  finSourceEduForm.setFinanceSourceID(14);
			            	  }else{
			            		  if(rs1.getString(3).equals("д")){
			            			  finSourceEduForm.setFinanceSourceID(15);  
			            		  }else{
			            			  if(rs1.getString(4).equals("д")){
			            				  finSourceEduForm.setFinanceSourceID(16); 
			            			  }
			            		  }
			            	  }
			            	  finSourceAndEduForms.getFinSourceEduForm().add(finSourceEduForm);
			              }
			              applic.setFinSourceAndEduForms(finSourceAndEduForms);
			              Root.PackageData.Applications.Application.ApplicationDocuments applicationDocuments = new Root.PackageData.Applications.Application.ApplicationDocuments();
			              Root.PackageData.Applications.Application.ApplicationDocuments.IdentityDocument identityDocument = new Root.PackageData.Applications.Application.ApplicationDocuments.IdentityDocument();		
			              identityDocument.setOriginalReceived(false);
			              identityDocument.setDocumentSeries(rs.getString(10));
			              identityDocument.setDocumentNumber(rs.getString(11));
			              if(!rs.getString(12).isEmpty()){
			            	  simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	     	                  date = simpleDateFormat.parse(rs.getString(12));        
	     	                  gregorianCalendar = 
	     	                     (GregorianCalendar)GregorianCalendar.getInstance();
	     	                  gregorianCalendar.setTime(date);
	     	                  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
			            	  identityDocument.setDocumentDate(result);
			              }
			              if(!rs.getString(13).isEmpty()){
			            	  identityDocument.setDocumentOrganization(rs.getString(13));
			              }
			              if (rs.getString(14).equals("п")){
			            	  identityDocument.setIdentityDocumentTypeID(1);
			              }else{
			            	  if(rs.getString(14).equals("з")){
			            		  identityDocument.setIdentityDocumentTypeID(2);
			            	  }else{
			            		  if(rs.getString(14).equals("и")){
			            			  identityDocument.setIdentityDocumentTypeID(3);
			            		  }
			            	  }
			              }
			              if(rs.getString(15).equals("РФ")||rs.getString(15).equals("Россия")){
			            	  identityDocument.setNationalityTypeID(1);
			              }else{
			            	  if(rs.getString(15).equals("Казахстан")){
			            		  identityDocument.setNationalityTypeID(102);
			            	  }else{
			            		  if(rs.getString(15).equals("Вьетнам")){
			            			  identityDocument.setNationalityTypeID(183);
			            		  }else{
			            			  if(rs.getString(15).equals("Грузия")){
			            				  identityDocument.setNationalityTypeID(73);
			            			  }else{
			            				  if(rs.getString(15).equals("Туркмения")||rs.getString(15).equals("Туркменистан")){
			            					  identityDocument.setNationalityTypeID(205);
			            				  }else{
			            					  if(rs.getString(15).equals("Таджикистан")||rs.getString(15).equals("Республика Таджикистан")){
			            						  identityDocument.setNationalityTypeID(196);  
			            					  }else{
			            						  if(rs.getString(15).equals("Армения")){
			            							  identityDocument.setNationalityTypeID(13);  
			            						  }
			            					  }
			            				  }
			            			  }
			            		  }
			            	  }
			              }
			              simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
     	                  date = simpleDateFormat.parse(rs.getString(16));        
     	                  gregorianCalendar = 
     	                     (GregorianCalendar)GregorianCalendar.getInstance();
     	                  gregorianCalendar.setTime(date);
     	                  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		            	  identityDocument.setBirthDate(result);	
		            	  applicationDocuments.setIdentityDocument(identityDocument);
		            	  applic.setApplicationDocuments(applicationDocuments);
		            	  applications.getApplication().add(applic);
		              }

	        		  packageData.setApplications(applications);
	        		  root.setPackageData(packageData);
		        		//создаем выходной поток
		        		File of = new File("D:/book.xml");
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
	/**********************************************************************/
	        }catch (Exception e) {
                ;
            }
	          
	        }
	        
	        if(error) return mapping.findForward("error");
	        request.setAttribute("list", list);
	        return mapping.findForward("success");
	    }
	       

}
