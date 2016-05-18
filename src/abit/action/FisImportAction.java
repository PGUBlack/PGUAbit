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
/*********  ��������� ���������� � �� � ������� ����������  ***********/

  UserConn us = new UserConn(request, mapping);
  conn = us.getConn(user.getSid());
  request.setAttribute( "fisConnectForm", form );

/*****************  ������� � ���������� ��������   *******************/
  if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/	

  
  //���� ��������
 /* if (form.getAction().equals("test")){
	  
	  String url  =  "http://priem.edu.ru:8000/import/ImportService.svc/dictionary";
	  HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

     
      connection.setRequestProperty("Content-Type", "text/xml");

      connection.setRequestMethod("POST");

      connection.setDoOutput(true);
      connection.setReadTimeout(50*1000);

      connection.connect();
	  
   // ������������� � �������� ������ �� ������

      wr = new OutputStreamWriter(connection.getOutputStream());

      String s = "<Root><AuthData><Login>revalle88@gmail.com</Login><Pass>Dreamlord88</Pass></AuthData></Root>";
      wr.write(s);

      wr.flush();

//�������� ������ ������ �������

      int status = connection.getResponseCode();

      String buf_line = new String();

//������ ����� ������� � ����� �����-������

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
      StringBuilder sb = new StringBuilder();

      while ((buf_line = in.readLine()) != null) {
            sb.append(buf_line);
      }

      buf_line = sb.toString();

      in.close();
System.out.println("Read From File ok");
	  
	  
  }
  
  //���� ��������
 
  
  
  else 
  */ if ( form.getAction() == null ) {
  
 //stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta From Abiturient a INNER JOIN FisImport b ON a.kodAbiturienta = b.id AND (b.Status = '+' OR b.Status = '�')");
  //stmt = conn.prepareStatement("SELECT Distinct a.NomerLichnogoDela, a.familija, a.Imja, a.Otchestvo, a.KodAbiturienta From Abiturient a, FisImport f, konkurs k, competitiveGroups c where a.kodAbiturienta = f.id AND (f.Status = '+' OR f.Status = '�') and a.kodAbiturienta=k.kodAbiturienta and k.kodSpetsialnosti=c.kodSpetsialnosti order by kodAbiturienta");
	  stmt = conn.prepareStatement("SELECT Distinct a.NomerLichnogoDela, a.familija, a.Imja, a.Otchestvo, a.KodAbiturienta From Abiturient a, FisImport f where a.kodabiturienta=f.id order by kodAbiturienta");
	  //stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta From Abiturient INNER JOIN Spetsialnosti ON Spetsialnosti.KodSpetsialnosti = Abiturient.KodSpetsialnZach and (Spetsialnosti.Tip_Spec = '�')");
  
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
		  
    		//������� ������

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
		 kodAbiturienta = selectedKods[1];
		  
		
  		 
		  stmt = conn.prepareStatement("SELECT a.KodAbiturienta, a.NomerLichnogoDela, a.Familija, a.Imja, a.Otchestvo, a.Pol, a.NujdaetsjaVObschejitii, a.DataInput, a.DataModify, a.SeriaDokumenta, a.NomerDokumenta, a.DataVydDokumenta, a.KemVydDokument, a.TipDokumenta, a.Grajdanstvo, a.DataRojdenija, k.target, a.KopijaSertifikata, a.NomerSertifikata,adi.abitemail,a.kodlgot FROM Abiturient a, konkurs k, abitdopinf adi WHERE a.kodabiturienta=adi.kodabiturienta and a.KodAbiturienta = ? and a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti ORDER BY 1 ASC");
		  stmt.setObject(1,kodAbiturienta,Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
          	  PackageData.Applications.Application applic = new PackageData.Applications.Application();
          	  applic.setUID("2016-"+rs.getString(1));
          	  applic.setApplicationNumber("2016-"+rs.getString(2));
          	 PackageData.Applications.Application.Entrant entrant = new PackageData.Applications.Application.Entrant();
          	  entrant.setUID(rs.getString(1));
          	  entrant.setFirstName(rs.getString(4));
          	  entrant.setMiddleName(rs.getString(5));
          	  entrant.setLastName(rs.getString(3));
          	  if(rs.getString(6).equals("�")){
          		  entrant.setGenderID(1);
          	  }else{
          		  entrant.setGenderID(2);
          	  }
          	  PackageData.Applications.Application.Entrant.EmailOrMailAddress eoma = new PackageData.Applications.Application.Entrant.EmailOrMailAddress();
          	  eoma.setEmail(rs.getString(20));
          	  entrant.setEmailOrMailAddress(eoma);
          	  if(rs.getInt(21)==1){
              	  PackageData.Applications.Application.Entrant.IsFromKrym ifk = new PackageData.Applications.Application.Entrant.IsFromKrym();
              	  ifk.setDocumentUID(rs.getString(10)+" "+rs.getString(11));
              	  entrant.setIsFromKrym(ifk);
          	  }
          	  applic.setEntrant(entrant);
          	  XMLGregorianCalendar result = null;
          	  Date date;
          	  SimpleDateFormat simpleDateFormat;
          	  GregorianCalendar gregorianCalendar;
          	  
          	  
          	  
          	  simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
          	  date = simpleDateFormat.parse(rs.getString(8));        
          	  gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
          	  gregorianCalendar.setTime(date);
          	  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
          	  applic.setRegistrationDate(result);
          	  if(rs.getString(7).equals("�")){
          		  applic.setNeedHostel(false);
          	  }else{
          		  applic.setNeedHostel(true);
          	  }
          	  
          	  applic.setStatusID(4);
          	    
          	  //FinSource
          	 PackageData.Applications.Application.FinSourceAndEduForms finSourceAndEduForms = new PackageData.Applications.Application.FinSourceAndEduForms();
          	  stmt1 = conn.prepareStatement("SELECT k.kodspetsialnosti,k.sogl,k.target FROM Konkurs k WHERE k.KodAbiturienta = ? ORDER BY 1 ASC");
    		  stmt1.setObject(1,kodAbiturienta,Types.INTEGER);
	              rs1 = stmt1.executeQuery();
	              while(rs1.next()) {
	            	 PackageData.Applications.Application.FinSourceAndEduForms.FinSourceEduForm finSourceEduForm = new PackageData.Applications.Application.FinSourceAndEduForms.FinSourceEduForm();
	            	 finSourceEduForm.setCompetitiveGroupUID("cg16-"+rs1.getString(1));
	            	 if(rs1.getString(2).equals("�")){
	            	  finSourceEduForm.setIsAgreedDate(result);
	            	 }
	            	 if(rs1.getInt(3)!=1){
	            		 
	            	 if(rs1.getInt(3)==5){
							finSourceEduForm.setTargetOrganizationUID("tcPGU-"+rs1.getString(1));
						}else if(rs1.getInt(3)==2){
							finSourceEduForm.setTargetOrganizationUID("tcRA-"+rs1.getString(1));

						}else if(rs1.getInt(3)==3){
							finSourceEduForm.setTargetOrganizationUID("tcRK-"+rs1.getString(1));

						}else if(rs1.getInt(3)==4){
							finSourceEduForm.setTargetOrganizationUID("tcMPT-"+rs1.getString(1));
						}
	              }
	            	 finSourceAndEduForms.getFinSourceEduForm().add(finSourceEduForm);
	              }
	              applic.setFinSourceAndEduForms(finSourceAndEduForms);
	              
	             PackageData.Applications.Application.ApplicationDocuments applicationDocuments = new PackageData.Applications.Application.ApplicationDocuments();
	             PackageData.Applications.Application.ApplicationDocuments.IdentityDocument identityDocument = new PackageData.Applications.Application.ApplicationDocuments.IdentityDocument();		

	              
	           /*   simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                  date = simpleDateFormat.parse("15-07-2015");        
                  gregorianCalendar = 
                     (GregorianCalendar)GregorianCalendar.getInstance();
                  gregorianCalendar.setTime(date);
                  result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
              	  result.setHour(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setMinute(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setSecond(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
              	  result.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            		             
	              identityDocument.setOriginalReceivedDate(result);
	              */
	              identityDocument.setDocumentSeries(rs.getString(10));
	              identityDocument.setDocumentNumber(rs.getString(11));
	              if(!rs.getString(12).isEmpty()){
	            	  simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	                  date = simpleDateFormat.parse(rs.getString(12));        
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
	              if (rs.getString(14).equals("�")){
	            	  identityDocument.setIdentityDocumentTypeID(1);
	              }else{
	            	  if(rs.getString(14).equals("�")){
	            		  identityDocument.setIdentityDocumentTypeID(2);
	            	  }else{
	            		  if(rs.getString(14).equals("�")){
	            			  identityDocument.setIdentityDocumentTypeID(3);
	            		  }else{
	            			  identityDocument.setIdentityDocumentTypeID(9);
	            		  }
	            	  }
	              }
	              if(rs.getString(15).equals("��")||rs.getString(15).equals("������")||rs.getString(15).equals("���������� ���������")){
	            	  identityDocument.setNationalityTypeID(1);
	              }else{
	            	  if(rs.getString(15).equals("���������")||rs.getString(15).equals("���������")){
	            		  identityDocument.setNationalityTypeID(102);
	            	  }else{
	            		  if(rs.getString(15).equals("�������")){
	            			  identityDocument.setNationalityTypeID(183);
	            		  }else{
	            			  if(rs.getString(15).equals("�������")){
	            				  identityDocument.setNationalityTypeID(31);
	            			  }else{
	            				  if(rs.getString(15).equals("���������")||rs.getString(15).equals("���������")){
	            					  identityDocument.setNationalityTypeID(205);
	            				  }else{
	            					  if(rs.getString(15).equals("�����������")||rs.getString(15).equals("�����������")){
	            						  identityDocument.setNationalityTypeID(196);  
	            					  }else{
	            						  if(rs.getString(15).equals("�������")){
	            							  identityDocument.setNationalityTypeID(13);  
	            						  }else{
		            						  if(rs.getString(15).equals("����������")||rs.getString(15).equals("����������")){
		            							  identityDocument.setNationalityTypeID(220);  
		            						  }else{
			            						  if(rs.getString(15).equals("������")){
			            							  identityDocument.setNationalityTypeID(204);  
			            						  }else{
				            						  if(rs.getString(15).equals("����")){
				            							  identityDocument.setNationalityTypeID(95);  
				            						  }else{
					            						  if(rs.getString(15).equals("�����")||rs.getString(15).equals("���")){
					            							  identityDocument.setNationalityTypeID(39);  
					            						  }else{
						            						  if(rs.getString(15).equals("�����")){
						            							  identityDocument.setNationalityTypeID(104);  
						            						  }else{
							            						  if(rs.getString(15).equals("������")){
							            							  identityDocument.setNationalityTypeID(210);  
							            						  }else{
								            						  if(rs.getString(15).equals("�������")||rs.getString(15).equals("�������")){
								            							  identityDocument.setNationalityTypeID(209);  
								            						  }else{
									            						  if(rs.getString(15).equals("�����")){
									            							  identityDocument.setNationalityTypeID(203);  
									            						  }else{
										            						  if(rs.getString(15).equals("���������")){
										            							  identityDocument.setNationalityTypeID(93);  
										            						  }else{
											            						  if(rs.getString(15).equals("��������")){
											            							  identityDocument.setNationalityTypeID(153);  
											            						  }else{
												            						  if(rs.getString(15).equals("�����")){
												            							  identityDocument.setNationalityTypeID(92);  
												            						  }else{
													            						  if(rs.getString(15).equals("�������")){
													            							  identityDocument.setNationalityTypeID(132);  
													            						  }else{
														            						  if(rs.getString(15).equals("����")){
														            							  identityDocument.setNationalityTypeID(76);  
														            						  }else{
															            						  if(rs.getString(15).equals("�������, ����������")){
															            							  identityDocument.setNationalityTypeID(130);  
															            						  }else{
																            						  if(rs.getString(15).equals("������")){
																	            							  identityDocument.setNationalityTypeID(100);  
																	            						  }else{
																		            						  if(rs.getString(15).equals("��������")){
																		            							  identityDocument.setNationalityTypeID(29);  
																		            						  }else{
																			            						  if(rs.getString(15).equals("��������")){
																			            							  identityDocument.setNationalityTypeID(108);  
																			            						  }else{
																				            						  if(rs.getString(15).equals("��������")){
																				            							  identityDocument.setNationalityTypeID(103);  
																				            						  }else{
																					            						  if(rs.getString(15).equals("�������")){
																					            							  identityDocument.setNationalityTypeID(145);  
																					            						  }else{
																						            						  if(rs.getString(15).equals("������")){
																						            							  identityDocument.setNationalityTypeID(107);  
																						            						  }else{
																							            						  if(rs.getString(15).equals("����������")){
																							            							  identityDocument.setNationalityTypeID(230);  
																							            						  }else{
																								            						  if(rs.getString(15).equals("�����")){
																								            							  identityDocument.setNationalityTypeID(53);  
																								            						  }else{
																									            						  if(rs.getString(15).equals("��������� �������� ����������")){
																									            							  identityDocument.setNationalityTypeID(114);  
																									            						  }else{
																										            						  if(rs.getString(15).equals("���")){
																										            							  identityDocument.setNationalityTypeID(37);  
																										            						  }else{
																											            						  if(rs.getString(15).equals("��������, ������������ ����������")){
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
                 date = simpleDateFormat.parse(rs.getString(16));        
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
          	 stmt2.setObject(1,kodAbiturienta,Types.INTEGER);
          	 rs2 = stmt2.executeQuery();
          	 while (rs2.next()){
          		
          		 String seriaAtt = "-";
          		 String nomerAtt = rs2.getString(3);
          		 String vidAtt = rs2.getString(1);
          		 String originalReceived = rs2.getString(4);
          		 //��������
          		 if(vidAtt.equals("��������") ||vidAtt.equals("�������� ���") || vidAtt.equals("�������� ��� � ��������") || vidAtt.equals("�������� ���")||vidAtt.equals("�������� ��� � ��������")){
          			
          			 TSchoolCertificateDocument schoolCertificateDocument = new TSchoolCertificateDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			//schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			if (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(true);
          				
          				
          			//}
          			//else if  (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(false);
          			//}
          				
          				
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
                     	 schoolCertificateDocument.setOriginalReceivedDate(result);	
          			}
          			 eduDocument.setSchoolCertificateDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          			
          		 
          		 //������ ���
          		 else if(vidAtt.equals("������ ���")){
          			
          			 THighEduDiplomaDocument schoolCertificateDocument = new THighEduDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			if (originalReceived.equals("�")){
                    	  schoolCertificateDocument.setOriginalReceivedDate(result);
          			//}
          			//else if  (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(false);
          			//}
          			}
          			 eduDocument.setHighEduDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 //������ ���
          		 else if(vidAtt.equals("������ ���")){
          			
          			 TMiddleEduDiplomaDocument schoolCertificateDocument = new TMiddleEduDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			if (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(true);
          			//}
          			//else if  (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(false);
          			//}
          			
                     	 schoolCertificateDocument.setOriginalReceivedDate(result);
          			}
          			
          			 eduDocument.setMiddleEduDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 //������ ���
          		 else if(vidAtt.equals("������ ���")){
          			
          			 TBasicDiplomaDocument schoolCertificateDocument = new TBasicDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			if (originalReceived.equals("�")){
          		
          			//}
          			//else if  (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(false);
          			//}	 simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
         
                    	 schoolCertificateDocument.setOriginalReceivedDate(result);
          			}
          			 eduDocument.setBasicDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 //������ ���
          		 else if(vidAtt.equals("������ ����")){
          			
          			 TIncomplHighEduDiplomaDocument schoolCertificateDocument = new TIncomplHighEduDiplomaDocument();
          			schoolCertificateDocument.setDocumentNumber(nomerAtt);
          			schoolCertificateDocument.setDocumentSeries(seriaAtt);
          			if (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(true);
          			//}
          			//else if  (originalReceived.equals("�")){
          			//	schoolCertificateDocument.setOriginalReceived(false);
          			//}
          			
                     	 schoolCertificateDocument.setOriginalReceivedDate(result);
          			}
          			 eduDocument.setIncomplHighEduDiplomaDocument(schoolCertificateDocument);
          			 eduDocuments.getEduDocument().add(eduDocument);
          			 
          		 }
          		 
          		 if(vidAtt.equals("������ ���������")||vidAtt.equals("������ ��������� � ��������")||vidAtt.equals("������ �����������")||vidAtt.equals("������ ����������� � ��������")||vidAtt.equals("������ ��������")||vidAtt.equals("������ �������� � ��������")){
          			 vidAtt=vidAtt;
          		 }else{
          		 
          		 applicationDocuments.setEduDocuments(eduDocuments);
          		 }
          		 
          	 }
          	  
          	  
          	  
          	  
          	  
          	  //EgeDocuments
          	 
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments egeDocuments = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments();
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument egeDocument = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument();
////      	if (rs.getString(18).equals("�")){
////  		egeDocument.setOriginalReceived(false);
////  	}else{
//          	egeDocument.setUID(rs.getString(1));
//          	egeDocument.setDocumentYear(2014);
//  		egeDocument.setOriginalReceived(true);
//  	//}
//  	egeDocument.setDocumentNumber(rs.getString(1));
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects subjects = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects();
//          	Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects.SubjectData subjectData = new Root.PackageData.Applications.Application.ApplicationDocuments.EgeDocuments.EgeDocument.Subjects.SubjectData();
//          	 stmt1 = conn.prepareStatement("SELECT KodPredmeta, OtsenkaEge from ZajavlennyeShkolnyeOtsenki where KodAbiturienta = ? And (Examen Like '�' OR Examen Like '')");
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
          	
          	// EntranceTestResults
          	  PackageData.Applications.Application.EntranceTestResults entranceTestResults = new PackageData.Applications.Application.EntranceTestResults();
    //      	  stmt1 = conn.prepareStatement("SELECT KodPredmeta, OtsenkaEge, Examen from ZajavlennyeShkolnyeOtsenki where KodAbiturienta = ?");
          	stmt1 = conn.prepareStatement("SELECT zso.examen,k.kodspetsialnosti,zso.kodpredmeta from konkurs k, zajavlennyeshkolnyeotsenki zso where zso.KodAbiturienta = ? and zso.kodabiturienta=k.kodabiturienta and (zso.examen not in ('0','+','�','') and zso.examen>0)");

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
          	  applications.getApplication().add(applic);
            }

  		 
		  
		
//	  }
	  
	  packageData.setApplications(applications);
		  root.setPackageData(packageData);
		  root.setAuthData(authData);
  		//������� �������� �����
  		File of = new File("D:/book.xml");
  		os = new FileOutputStream(of);
  		// �������������
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