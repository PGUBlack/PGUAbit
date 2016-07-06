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
import abit.paket.PackageData.Applications.Application.IndividualAchievements;
import abit.paket.Root;
import abit.paket.TBasicDiplomaDocument;
import abit.paket.TEntranceTestSubject;
import abit.paket.TInstitutionDocument;
import abit.paket.THighEduDiplomaDocument;
import abit.paket.TIncomplHighEduDiplomaDocument;
import abit.paket.TMiddleEduDiplomaDocument;
import abit.paket.TSchoolCertificateDocument;
import abit.paket.TSchoolCertificateBasicDocument;


public class FisImportAction extends Action{
	
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
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
  
 //stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta From Abiturient a INNER JOIN FisImport b ON a.kodAbiturienta = b.id AND (b.Status = '+' OR b.Status = 'и')");
  //stmt = conn.prepareStatement("SELECT Distinct a.NomerLichnogoDela, a.familija, a.Imja, a.Otchestvo, a.KodAbiturienta From Abiturient a, FisImport f, konkurs k, competitiveGroups c where a.kodAbiturienta = f.id AND (f.Status = '+' OR f.Status = 'и') and a.kodAbiturienta=k.kodAbiturienta and k.kodSpetsialnosti=c.kodSpetsialnosti order by kodAbiturienta");
	  stmt = conn.prepareStatement("SELECT Distinct a.NomerLichnogoDela, a.familija, a.Imja, a.Otchestvo, a.KodAbiturienta From Abiturient a, FisImport f where a.kodabiturienta=f.id order by kodAbiturienta");
	  //stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta From Abiturient INNER JOIN Spetsialnosti ON Spetsialnosti.KodSpetsialnosti = Abiturient.KodSpetsialnZach and (Spetsialnosti.Tip_Spec = 'о')");
  
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

	  
	  
	  
	  
	  
	  
	  
	  
	  
	  selectedItems = form.getSelectedItems();
	  selectedKods = form.getSelectedKods();
	  try {
		  
    		//создаем объект

	  Root root = new Root();
	  PackageData packageData = new PackageData();
		//  Root.PackageData packageData = new Root.PackageData();
		  Root.AuthData authData = new Root.AuthData();
		  authData.setLogin("revalle88@gmail.com");
		  authData.setPass("Dreamlord88");
		 PackageData.Applications applications = new PackageData.Applications();
//	  for (int i=0;i<selectedKods.length; i++)
//	  {
		  
//		  kodAbiturienta = selectedKods[i];
		 abit.paket.delete.Root dr=new abit.paket.delete.Root();
			abit.paket.delete.AuthData dad=new abit.paket.delete.AuthData();
			abit.paket.delete.DataForDelete dfd=new abit.paket.delete.DataForDelete();
			abit.paket.delete.DataForDelete.Applications dcg=new abit.paket.delete.DataForDelete.Applications();
			
			dad.setLogin("revalle88@gmail.com");
			dad.setPass("Dreamlord88");
		  
		
  		 // and a.datamodify like '"+StringUtil.CurrDate(".")+"'
		 stmt = conn.prepareStatement("select a.kodabiturienta, a.nomerlichnogodela, a.familija, a.imja, a.otchestvo, a.pol, adi.abitemail, a.kodlgot, a.seriadokumenta, a.nomerdokumenta, a.datainput, a.NujdaetsjaVObschejitii, a.viddoksredobraz, a.DataVydDokumenta, a.grajdanstvo, a.KemVydDokument, a.tipdokumenta, a.kodpodrazdelenija, a.datarojdenija, a.mestorojdenija from abiturient a, abitdopinf adi where a.kodabiturienta=adi.kodabiturienta and (a.prinjat like 'н' or a.prinjat is null)  and a.kodabiturienta not in (select kodabiturienta from konkurs where bud is null and dog is null) and substring(a.datainput,4,2) >5 and a.nomeratt is not null ");
		 rs = stmt.executeQuery();
            while (rs.next()) {
            	System.out.println(rs.getString(1));
          	  PackageData.Applications.Application applic = new PackageData.Applications.Application();
          	abit.paket.delete.DataForDelete.Applications.Application del=new abit.paket.delete.DataForDelete.Applications.Application();
          	del.setApplicationNumber(rs.getString(2));
          	
          	  applic.setUID("2016-"+rs.getString(1));
          	  applic.setApplicationNumber(rs.getString(2));
          	  PackageData.Applications.Application.Entrant entrant = new PackageData.Applications.Application.Entrant();
          	  entrant.setUID(rs.getString(1));
          	  entrant.setFirstName(rs.getString(4));
          	  entrant.setMiddleName(rs.getString(5));
          	  entrant.setLastName(rs.getString(3));
          	  if(rs.getString(6).equals("м")){
          		  entrant.setGenderID(1);
          	  }else{
          		  entrant.setGenderID(2);
          	  }
          	  PackageData.Applications.Application.Entrant.EmailOrMailAddress eoma = new PackageData.Applications.Application.Entrant.EmailOrMailAddress();
          	  if(rs.getString(7)!=null && !rs.getString(7).equals("")){
          	  eoma.setEmail(rs.getString(7));
          	  }else{
          		  eoma.setEmail("example@yandex.ru");
          	  }
          	  entrant.setEmailOrMailAddress(eoma);
          	  if(rs.getInt(8)==1){
              	  PackageData.Applications.Application.Entrant.IsFromKrym ifk = new PackageData.Applications.Application.Entrant.IsFromKrym();
              	  ifk.setDocumentUID(rs.getString(9)+" "+rs.getString(10));
              	  entrant.setIsFromKrym(ifk);
          	  }
          	  applic.setEntrant(entrant);
          	  XMLGregorianCalendar result = null;
          	  Date date;
          	  SimpleDateFormat simpleDateFormat;
          	  GregorianCalendar gregorianCalendar;
          	  
          	  
          	  
          	  simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
          	  date = simpleDateFormat.parse(rs.getString(11));        
          	  gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
          	  gregorianCalendar.setTime(date);
          	  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
          	  applic.setRegistrationDate(result);
          	  if(rs.getString(12).equals("н")){
          		  applic.setNeedHostel(false);
          	  }else{
          		  applic.setNeedHostel(true);
          	  }
          	  del.setRegistrationDate(result);
        	  dcg.getApplication().add(del);
          	  applic.setStatusID(2);
          	    
          	  //FinSource
          	  PackageData.Applications.Application.FinSourceAndEduForms finSourceAndEduForms = new PackageData.Applications.Application.FinSourceAndEduForms();
          	   stmt1 = conn.prepareStatement("SELECT k.kodspetsialnosti,k.bud,k.dog, k.op, k.target,k.rlgot  FROM Konkurs k WHERE k.KodAbiturienta = ? ORDER BY 1 ASC");
    		  stmt1.setObject(1,rs.getString(1),Types.VARCHAR);
	              rs1 = stmt1.executeQuery();
	              while(rs1.next()) {
	            	 PackageData.Applications.Application.FinSourceAndEduForms.FinSourceEduForm finSourceEduForm = new PackageData.Applications.Application.FinSourceAndEduForms.FinSourceEduForm();
	            	if(rs1.getString(2)!=null && rs1.getString(2).equals("д")){
	            		if(rs.getInt(8)==1){
	            			if(rs1.getInt(4)==3 || rs1.getInt(4)==4)
	            			{
	            				finSourceEduForm.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "kl");
	            			}
	            			else{finSourceEduForm.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "kb");}
	            		}else{
	            			if(rs1.getInt(4)==3 || rs1.getInt(4)==4){finSourceEduForm.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "l");}
	            			else{finSourceEduForm.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "b");}
	            		}
	            	//	if(!rs.getString(13).equals("Аттестат ООО") && !rs.getString(13).equals("Аттестат ООО с отличием") && !rs.getString(13).equals("Аттестат СОО") && !rs.getString(13).equals("Аттестат СОО с отличием") ){
		            //		finSourceEduForm.setIsForSPOandVO(true);
		            //	}
	            	 finSourceAndEduForms.getFinSourceEduForm().add(finSourceEduForm);
	            	}
	            	if(rs1.getString(3)!=null && rs1.getString(3).equals("д")){
	            		if(rs.getInt(8)==1){
	            			finSourceEduForm.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "kd");
	            		}else{
	            			finSourceEduForm.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "d");
	            		}
	            	//	if(!rs.getString(13).equals("Аттестат ООО") && !rs.getString(13).equals("Аттестат ООО с отличием") && !rs.getString(13).equals("Аттестат СОО") && !rs.getString(13).equals("Аттестат СОО с отличием") ){
		            //		finSourceEduForm.setIsForSPOandVO(true);
		            //	}
	            	 finSourceAndEduForms.getFinSourceEduForm().add(finSourceEduForm);
	            	}
	              }
	              applic.setFinSourceAndEduForms(finSourceAndEduForms);
	              
	              
	              /*
	              stmt1 = conn.prepareStatement("SELECT k.kodspetsialnosti, k.op,k.rlgot, k.stob  FROM Konkurs k WHERE k.KodAbiturienta = ? and k.op in ('3','4') and k.bud like 'д' ORDER BY 1 ASC");
	    		  stmt1.setObject(1,rs.getString(1),Types.VARCHAR);
		              rs1 = stmt1.executeQuery();
		              while(rs1.next()) {
		            	     
		 	             PackageData.Applications.Application.ApplicationCommonBenefits appCBs = new PackageData.Applications.Application.ApplicationCommonBenefits();
		 	             PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit appCB = new PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit();
		 	            PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit.DocumentReason dr = new PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit.DocumentReason();
		 	             appCB.setUID("appCB-"+rs1.getString(1)+"-"+rs.getString(1));
		 	            if(rs.getInt(8)==1){
		 	            	if(rs1.getInt(2)==3 || rs1.getInt(2)==4){
		 	            		
		 	            		 appCB.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "kl");
		 	            	}else{appCB.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "kb");}
		 	            }else{
		 	            	if(rs1.getInt(2)==3 || rs1.getInt(2)==4){
		 	            		
		 	            		 appCB.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "l");
		 	            	}else{appCB.setCompetitiveGroupUID("cg16-" + rs1.getString(1) + "b");}
		 	            }
		 	           appCB.setBenefitKindID(4);
		 	           if(rs1.getInt(2)==3){
		 	        	  appCB.setDocumentTypeID(30);
		 	        	  
		 	        	  
		 	           }else if(rs1.getInt(2)==4){
		 	        	   appCB.setDocumentTypeID(11);
		 	        	  PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit.DocumentReason.MedicalDocuments md = new PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit.DocumentReason.MedicalDocuments();
		 	        	 PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit.DocumentReason.MedicalDocuments. md = new PackageData.Applications.Application.ApplicationCommonBenefits.ApplicationCommonBenefit.DocumentReason.MedicalDocuments();
		 	        	  
		 	        	  dr.setMedicalDocuments(value);
		 	           }
		 	           
		 	           appCB.setDocumentReason(dr);
		 	           
		              }
	              */
	              
	              
	             PackageData.Applications.Application.ApplicationDocuments applicationDocuments = new PackageData.Applications.Application.ApplicationDocuments();
	             PackageData.Applications.Application.ApplicationDocuments.IdentityDocument identityDocument = new PackageData.Applications.Application.ApplicationDocuments.IdentityDocument();		
	             identityDocument.setUID("id-"+rs.getString(1));
	              identityDocument.setDocumentSeries(rs.getString(9));
	              identityDocument.setLastName(rs.getString(3));
	              identityDocument.setFirstName(rs.getString(4));
	              identityDocument.setMiddleName(rs.getString(5));
	              if(rs.getString(6).equals("м")){
	            	  identityDocument.setGenderID(Long.valueOf(1));
	          	  }else{
	          		identityDocument.setGenderID(Long.valueOf(2));
	          	  }
	              
	              
	              identityDocument.setDocumentNumber(rs.getString(10));
	              if(!rs.getString(14).isEmpty()){
	            	  simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	                  date = simpleDateFormat.parse(rs.getString(14));        
	                  gregorianCalendar = 
	                     (GregorianCalendar)GregorianCalendar.getInstance();
	                  gregorianCalendar.setTime(date);
	                  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	              	  result.setHour(DatatypeConstants.FIELD_UNDEFINED);
	              	  result.setMinute(DatatypeConstants.FIELD_UNDEFINED);
	              	  result.setSecond(DatatypeConstants.FIELD_UNDEFINED);
	              	  result.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
	              	  result.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
	            	  identityDocument.setDocumentDate(result);
	              }  
	              identityDocument.setDocumentOrganization(rs.getString(16));
	             //identityDocument.setSubdivisionCode(rs.getString(18));
	              
	              if (rs.getString(17).equals("п")){
	            	  identityDocument.setIdentityDocumentTypeID(1);
	              }else{
	            	  if(rs.getString(17).equals("з")){
	            		  identityDocument.setIdentityDocumentTypeID(2);
	            	  }else{
	            		  if(rs.getString(17).equals("и")){
	            			  identityDocument.setIdentityDocumentTypeID(3);
	            		  }else{
	            			  identityDocument.setIdentityDocumentTypeID(9);
	            		  }
	            	  }
	              }
	              if(rs.getString(15).equals("РФ")||rs.getString(15).equals("Россия")||rs.getString(15).equals("Российская Федерация")){
	            	  identityDocument.setNationalityTypeID(1);
	              }else{
	            	  if(rs.getString(15).equals("Казахстан")||rs.getString(15).equals("КАЗАХСТАН")){
	            		  identityDocument.setNationalityTypeID(102);
	            	  }else{
	            		  if(rs.getString(15).equals("ВЬЕТНАМ")){
	            			  identityDocument.setNationalityTypeID(183);
	            		  }else{
	            			  if(rs.getString(15).equals("КАМЕРУН")){
	            				  identityDocument.setNationalityTypeID(31);
	            			  }else{
	            				  if(rs.getString(15).equals("Туркмения")||rs.getString(15).equals("ТУРКМЕНИЯ")){
	            					  identityDocument.setNationalityTypeID(205);
	            				  }else{
	            					  if(rs.getString(15).equals("Таджикистан")||rs.getString(15).equals("ТАДЖИКИСТАН")){
	            						  identityDocument.setNationalityTypeID(196);  
	            					  }else{
	            						  if(rs.getString(15).equals("АРМЕНИЯ")){
	            							  identityDocument.setNationalityTypeID(13);  
	            						  }else{
		            						  if(rs.getString(15).equals("Узбекистан")||rs.getString(15).equals("УЗБЕКИСТАН")){
		            							  identityDocument.setNationalityTypeID(220);  
		            						  }else{
			            						  if(rs.getString(15).equals("ТУРЦИЯ")){
			            							  identityDocument.setNationalityTypeID(204);  
			            						  }else{
				            						  if(rs.getString(15).equals("ИРАК")){
				            							  identityDocument.setNationalityTypeID(95);  
				            						  }else{
					            						  if(rs.getString(15).equals("КИТАЙ")||rs.getString(15).equals("КНР")){
					            							  identityDocument.setNationalityTypeID(39);  
					            						  }else{
						            						  if(rs.getString(15).equals("КЕНИЯ")){
						            							  identityDocument.setNationalityTypeID(104);  
						            						  }else{
							            						  if(rs.getString(15).equals("ЕГИПЕТ")){
							            							  identityDocument.setNationalityTypeID(210);  
							            						  }else{
								            						  if(rs.getString(15).equals("Украина")||rs.getString(15).equals("УКРАИНА")){
								            							  identityDocument.setNationalityTypeID(209);  
								            						  }else{
									            						  if(rs.getString(15).equals("ТУНИС")){
									            							  identityDocument.setNationalityTypeID(203);  
									            						  }else{
										            						  if(rs.getString(15).equals("ИНДОНЕЗИЯ")){
										            							  identityDocument.setNationalityTypeID(93);  
										            						  }else{
											            						  if(rs.getString(15).equals("ПАКИСТАН")){
											            							  identityDocument.setNationalityTypeID(153);  
											            						  }else{
												            						  if(rs.getString(15).equals("ИНДИЯ")){
												            							  identityDocument.setNationalityTypeID(92);  
												            						  }else{
													            						  if(rs.getString(15).equals("МАРОККО")){
													            							  identityDocument.setNationalityTypeID(132);  
													            						  }else{
														            						  if(rs.getString(15).equals("ГАНА")){
														            							  identityDocument.setNationalityTypeID(76);  
														            						  }else{
															            						  if(rs.getString(15).equals("МОЛДОВА, РЕСПУБЛИКА")){
															            							  identityDocument.setNationalityTypeID(130);  
															            						  }else{
																            						  if(rs.getString(15).equals("ЯМАЙКА")){
																	            							  identityDocument.setNationalityTypeID(100);  
																	            						  }else{
																		            						  if(rs.getString(15).equals("БЕЛАРУСЬ")){
																		            							  identityDocument.setNationalityTypeID(29);  
																		            						  }else{
																			            						  if(rs.getString(15).equals("КИРГИЗИЯ")){
																			            							  identityDocument.setNationalityTypeID(108);  
																			            						  }else{
																				            						  if(rs.getString(15).equals("ИОРДАНИЯ")){
																				            							  identityDocument.setNationalityTypeID(103);  
																				            						  }else{
																					            						  if(rs.getString(15).equals("НИГЕРИЯ")){
																					            							  identityDocument.setNationalityTypeID(145);  
																					            						  }else{
																						            						  if(rs.getString(15).equals("КУВЕЙТ")){
																						            							  identityDocument.setNationalityTypeID(107);  
																						            						  }else{
																							            						  if(rs.getString(15).equals("АФГАНИСТАН")){
																							            							  identityDocument.setNationalityTypeID(230);  
																							            						  }else{
																								            						  if(rs.getString(15).equals("БЕНИН")){
																								            							  identityDocument.setNationalityTypeID(53);  
																								            						  }else{
																									            						  if(rs.getString(15).equals("ЛИВИЙСКАЯ АРАБСКАЯ ДЖАМАХИРИЯ")){
																									            							  identityDocument.setNationalityTypeID(114);  
																									            						  }else{
																										            						  if(rs.getString(15).equals("ЧАД")){
																										            							  identityDocument.setNationalityTypeID(37);  
																										            						  }else{
																											            						  if(rs.getString(15).equals("ТАНЗАНИЯ, ОБЪЕДИНЕННАЯ РЕСПУБЛИКА")){
																											            							  identityDocument.setNationalityTypeID(215);  
																											            						  
																										            						  }
																									            						  }
																								            						  }
																							            						  }
																						            						  }
																					            						  }
																				            						  }
																			            						  }
																		            						  }
																	            						  }
																            						  }
															            						  }
														            						  }
													            						  }
												            						  }
											            						  }
										            						  }
									            						  }
								            						  }
							            						  }
						            						  }
					            						  }
				            						  }
			            						  }
		            						  }
	            						  }
	            					  }
	            				  }
	            			  }
	            		  }
	            	  }
	              }
	              simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                 date = simpleDateFormat.parse(rs.getString(19));        
                 gregorianCalendar = 
                    (GregorianCalendar)GregorianCalendar.getInstance();
                 gregorianCalendar.setTime(date);
                 result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
             	  result.setHour(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setMinute(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setSecond(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
          	  identityDocument.setBirthDate(result);	
          	identityDocument.setBirthPlace(rs.getString(20));
          	  applicationDocuments.setIdentityDocument(identityDocument);
        	  
          	  
          	  
        /* 	PackageData.Applications.Application.SelectedCompetitiveGroups competitiveGroups = new Root.PackageData.Applications.Application.SelectedCompetitiveGroups();
         	 PackageData.Applications.Application.SelectedCompetitiveGroupItems competitiveGroupItems = new Root.PackageData.Applications.Application.SelectedCompetitiveGroupItems();
          	
         	 stmt1 = conn.prepareStatement("SELECT Konkurs.KodSpetsialnosti FROM Konkurs INNER JOIN Spetsialnosti ON Spetsialnosti.KodSpetsialnosti = Konkurs.KodSpetsialnosti and  Konkurs.KodAbiturienta = ?");
         	 stmt1.setObject(1,kodAbiturienta,Types.INTEGER);
         	 rs1 = stmt1.executeQuery();
            while (rs1.next()) {
           	int KodSpetsialnosti = rs1.getInt(1);
           	stmt2 = conn.prepareStatement("SELECT uidCG, uidItem FROM CompetitiveGroups Where KodSpetsialnosti = ?");
           	stmt2.setObject(1,KodSpetsialnosti,Types.INTEGER);
           	rs2 = stmt2.executeQuery();
           	while(rs2.next()){
               competitiveGroups.getCompetitiveGroupID().add(rs2.getString(1));
           	competitiveGroupItems.getCompetitiveGroupItemID().add(rs2.getString(2));
           	}
           	 
            }
            applic.setSelectedCompetitiveGroups(competitiveGroups);
            applic.setSelectedCompetitiveGroupItems(competitiveGroupItems);
          	  
          	  */
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  //eduDocuments
          	 PackageData.Applications.Application.ApplicationDocuments.EduDocuments eduDocuments = new PackageData.Applications.Application.ApplicationDocuments.EduDocuments();
          	 PackageData.Applications.Application.ApplicationDocuments.EduDocuments.EduDocument eduDocument = new  PackageData.Applications.Application.ApplicationDocuments.EduDocuments.EduDocument();
          	 stmt2 = conn.prepareStatement("SELECT VidDokSredObraz, SeriaAtt, NomerAtt, tipDokSredObraz from Abiturient where KodAbiturienta = ?");
          	 stmt2.setObject(1,rs.getString(1),Types.INTEGER);
          	 rs2 = stmt2.executeQuery();
          	 while (rs2.next()){
          		
          		 String seriaAtt = "-";
          		 String nomerAtt = rs2.getString(3);
          		 String vidAtt = rs2.getString(1);
          		 String originalReceived = rs2.getString(4);
          		 //Аттестат
          		 if( vidAtt.equals("Аттестат СОО")||vidAtt.equals("Аттестат СОО с отличием")){
          			
          			 TSchoolCertificateDocument schoolCertificateDocument = new TSchoolCertificateDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			//schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			//if (originalReceived.equals("о")){
          			//	schoolCertificateDocument.setOriginalReceived(true);
          				
          				
          			//}
          			//else if  (originalReceived.equals("к")){
          			//	schoolCertificateDocument.setOriginalReceived(false);
          			//}
          				
          			schoolCertificateDocument.setUID("AttestatSOO-"+rs.getString(1));	
          			//	 simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                     //    date = simpleDateFormat.parse("15-07-2015");        
                    //     gregorianCalendar = 
                    //        (GregorianCalendar)GregorianCalendar.getInstance();
                     //    gregorianCalendar.setTime(date);
                    //     result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
                    // 	  result.setHour(DatatypeConstants.FIELD_UNDEFINED);
                    // 	  result.setMinute(DatatypeConstants.FIELD_UNDEFINED);
                   //  	  result.setSecond(DatatypeConstants.FIELD_UNDEFINED);
                    // 	  result.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
                    // 	  result.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
                    // 	 schoolCertificateDocument.setOriginalReceivedDate(result);	
          			//}
          			 eduDocument.setSchoolCertificateDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          			
          		 if(vidAtt.equals("Аттестат ООО") || vidAtt.equals("Аттестат ООО с отличием") ){
          			TSchoolCertificateDocument schoolCertificateDocument = new TSchoolCertificateDocument();
          			schoolCertificateDocument.setUID("AttestatOOO-"+rs.getString(1));
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			eduDocument.setSchoolCertificateBasicDocument(schoolCertificateDocument);
         			 eduDocuments.getEduDocument().add(eduDocument);
          		 }
          		 
          		 //Диплом ВПО
          		 else if(vidAtt.equals("Диплом ВПО")){
          			
          			 THighEduDiplomaDocument schoolCertificateDocument = new THighEduDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			schoolCertificateDocument.setUID("DiplomVPO-"+rs.getString(1));
          			 eduDocument.setHighEduDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 //Диплом ВПО
          		 else if(vidAtt.equals("Диплом СПО") || vidAtt.equals("Диплом СПО с отличием")){
          			
          			 TMiddleEduDiplomaDocument schoolCertificateDocument = new TMiddleEduDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			schoolCertificateDocument.setUID("DiplomSPO-"+rs.getString(1));
          			
          			 eduDocument.setMiddleEduDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 //Диплом ВПО
          		 else if(vidAtt.equals("Диплом НПО")){
          			
          			 TBasicDiplomaDocument schoolCertificateDocument = new TBasicDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			schoolCertificateDocument.setUID("DiplomNPO-"+rs.getString(1));
          			 eduDocument.setBasicDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 //Диплом ВПО
          		 else if(vidAtt.equals("Диплом НВПО")){
          			
          			 TIncomplHighEduDiplomaDocument schoolCertificateDocument = new TIncomplHighEduDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			schoolCertificateDocument.setUID("DiplomNVPO-"+rs.getString(1));
          			 eduDocument.setIncomplHighEduDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 if(vidAtt.equals("Диплом бакалавра")||vidAtt.equals("Диплом бакалавра с отличием")||vidAtt.equals("Диплом специалиста")||vidAtt.equals("Диплом специалиста с отличием")||vidAtt.equals("Диплом магистра")||vidAtt.equals("Диплом магистра с отличием")){
          			 vidAtt=vidAtt;
          		 }else{
          		 
          		 applicationDocuments.setEduDocuments(eduDocuments);
          		 }
          		 
          	 }
          	  
          	  
          	  
          	  
          	  
          	  //EgeDocuments
          	 
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments egeDocuments = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments();
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument egeDocument = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument();
////      	if (rs.getString(18).equals("к")){
////  		egeDocument.setOriginalReceived(false);
////  	}else{
//          	egeDocument.setUID(rs.getString(1));
//          	egeDocument.setDocumentYear(2014);
//  		egeDocument.setOriginalReceived(true);
//  	//}
//  	egeDocument.setDocumentNumber(rs.getString(1));
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects subjects = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects();
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects.SubjectData subjectData = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects.SubjectData();
//          	 stmt1 = conn.prepareStatement("SELECT KodPredmeta, OtsenkaEge from ZajavlennyeShkolnyeOtsenki where KodAbiturienta = ? And (Examen Like 'в' OR Examen Like '')");
//             stmt1.setObject(1,kodAbiturienta,Types.INTEGER);
//             rs1 = stmt1.executeQuery();
//             while (rs1.next()){
//            	 if((rs1.getInt(1)!=12)&&(rs1.getInt(1)!=13)&&(rs1.getInt(2)!=0)){
//            		 subjectData = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects.SubjectData();
//            		 stmt2 = conn.prepareStatement("SELECT ID from DictionaryCode1 where ID_PNZGU = ?");
//                     stmt2.setObject(1,rs1.getInt(1),Types.INTEGER);
//                     rs2 = stmt2.executeQuery();
//                     while (rs2.next()){
//                    	 subjectData.setSubjectID(rs2.getLong(1));
//                     }
//            		 subjectData.setValue(rs1.getBigDecimal(2));
//            		 subjects.getSubjectData().add(subjectData);
//            	 }
//            	 
//             }
//             egeDocument.setSubjects(subjects);
//          	egeDocuments.getEgeDocument().add(egeDocument);
//          	applicationDocuments.setEgeDocuments(egeDocuments);
          	
          	applic.setApplicationDocuments(applicationDocuments);
          	/*
          	// EntranceTestResults
          	  PackageData.Applications.Application.EntranceTestResults entranceTestResults = new PackageData.Applications.Application.EntranceTestResults();
    //      	  stmt1 = conn.prepareStatement("SELECT KodPredmeta, OtsenkaEge, Examen from ZajavlennyeShkolnyeOtsenki where KodAbiturienta = ?");
          	stmt1 = conn.prepareStatement("SELECT zso.examen,k.kodspetsialnosti,zso.kodpredmeta from konkurs k, zajavlennyeshkolnyeotsenki zso where zso.KodAbiturienta = ? and zso.kodabiturienta=k.kodabiturienta and (zso.examen not in ('0','+','в','') and zso.examen>0)");

          	  stmt1.setObject(1,kodAbiturienta,Types.INTEGER);
              rs1 = stmt1.executeQuery();
              while(rs1.next()){
            	  
            		  PackageData.Applications.Application.EntranceTestResults.EntranceTestResult entranceTestResult = new PackageData.Applications.Application.EntranceTestResults.EntranceTestResult();
            		  entranceTestResult.setUID("E" + kodAbiturienta +""+ rs1.getString(2) + "inner"+rs1.getString(3)); 
            		  
            		  if(rs1.getInt(1)==0){
            			  bd = new BigDecimal(70);
            			  entranceTestResult.setResultValue(bd);
            		  }else{
            			  bd = new BigDecimal(rs1.getInt(1));
            			  entranceTestResult.setResultValue(bd);
            		  }
            		  entranceTestResult.setResultSourceTypeID(2);
		            		  TEntranceTestSubject entranceTestSubject = new TEntranceTestSubject();
		            		  stmt2 = conn.prepareStatement("SELECT ID,name from DictionaryCode1 where ID_PNZGU = ?");
		                      stmt2.setObject(1,rs1.getInt(3),Types.INTEGER);
		                      rs2 = stmt2.executeQuery();
		                      while (rs2.next()){
		                    	  entranceTestSubject.setSubjectID(rs2.getLong(1));
		                    	  entranceTestSubject.setSubjectName(rs2.getString(2));
		                      }
		                      
		                      entranceTestResult.setEntranceTestSubject(entranceTestSubject);
		                      entranceTestResult.setEntranceTestTypeID(1);
		                      entranceTestResult.setCompetitiveGroupUID("cg16-"+rs1.getString(2));
		                      entranceTestResults.getEntranceTestResult().add(entranceTestResult);
                    }


              if (!entranceTestResults.getEntranceTestResult().isEmpty()){
            	  applic.setEntranceTestResults(entranceTestResults);
              }
              */
          	
          	
          	
          	
          	
          	IndividualAchievements ias = new IndividualAchievements();
          	
          	
          	stmt2 = conn.prepareStatement("SELECT ballpoi, ballsgto, ballsoch, ballzgto, ballatt from abitdopinf where KodAbiturienta = ?");
         	 stmt2.setObject(1,rs.getString(1),Types.VARCHAR);
         	 rs2 = stmt2.executeQuery();
         	 if (rs2.next()){
         		IndividualAchievements.IndividualAchievement ia = new IndividualAchievements.IndividualAchievement();
         		if(rs2.getString(1)!=null && !rs2.getString(1).equals("Да") && !rs2.getString(1).equals("0") && !rs2.getString(1).equals("да")){
         			ia.setIAUID("d1-"+rs.getString(1));
         			ia.setIAMark(rs2.getLong(1));
         			ia.setInstitutionAchievementUID("d1");
         			ia.setIADocumentUID("d1-"+rs.getString(1));
         			ias.getIndividualAchievement().add(ia);
         		}
         		if(rs2.getString(2)!=null && !rs2.getString(2).equals("Да") && !rs2.getString(2).equals("0") && !rs2.getString(2).equals("да")){
         			ia.setIAUID("d10-"+rs.getString(1));
         			ia.setIAMark(rs2.getLong(2));
         			ia.setInstitutionAchievementUID("d10");
         			ia.setIADocumentUID("d10-"+rs.getString(1));
         			ias.getIndividualAchievement().add(ia);
         		}
         		if(rs2.getString(3)!=null && !rs2.getString(3).equals("Да") && !rs2.getString(3).equals("0") && !rs2.getString(3).equals("да")){
         			ia.setIAUID("d15-"+rs.getString(1));
         			ia.setIAMark(rs2.getLong(3));
         			ia.setInstitutionAchievementUID("d15");
         			ia.setIADocumentUID("d15-"+rs.getString(1));
         			ias.getIndividualAchievement().add(ia);
         		}
         		if(rs2.getString(4)!=null && !rs2.getString(4).equals("Да") && !rs2.getString(4).equals("0") && !rs2.getString(4).equals("да")){
         			ia.setIAUID("d8-"+rs.getString(1));
         			ia.setIAMark(rs2.getLong(4));
         			ia.setInstitutionAchievementUID("d8");
         			ia.setIADocumentUID("d8-"+rs.getString(1));
         			ias.getIndividualAchievement().add(ia);
         		}
         		if(rs2.getString(5)!=null && !rs2.getString(5).equals("Да") && !rs2.getString(5).equals("0")  && !rs2.getString(5).equals("да")){
         			ia.setIAUID("d9-"+rs.getString(1));
         			ia.setIAMark(rs2.getLong(5));
         			ia.setInstitutionAchievementUID("d9");
         			ia.setIADocumentUID("d9-"+rs.getString(1));
         			ias.getIndividualAchievement().add(ia);
         		}
         		if(!ias.getIndividualAchievement().isEmpty()){
         		applic.setIndividualAchievements(ias);
         		}
         	 }
          	
          	
          	  applications.getApplication().add(applic);
            }

            dfd.setApplications(dcg);
    		dr.setAuthData(dad);
    		dr.setDataForDelete(dfd);
           
		
//	  }
	  
	  packageData.setApplications(applications);
		  root.setPackageData(packageData);
		  root.setAuthData(authData);
  		//создаем выходной поток
  		File of = new File("D:/book.xml");
  		os = new FileOutputStream(of);
  		// Маршаллизация
  		JAXBContext context = JAXBContext.newInstance(Root.class);
  		Marshaller m = context.createMarshaller();
  		m.marshal(root, os);
  		os.close();
		
		of = new File("D:/applicationsDelete.xml");
		os = new FileOutputStream(of);
		// Маршаллизация
		context = JAXBContext.newInstance(abit.paket.delete.Root.class);
		m = context.createMarshaller();
		m.marshal(dr, os);
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