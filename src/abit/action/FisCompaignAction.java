package abit.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
import abit.paket.PackageData;
import abit.paket.Root;
import abit.paket.TBasicDiplomaDocument;
import abit.paket.TEntranceTestSubject;
import abit.paket.THighEduDiplomaDocument;
import abit.paket.TIncomplHighEduDiplomaDocument;
import abit.paket.TMiddleEduDiplomaDocument;
import abit.paket.TSchoolCertificateDocument;
import abit.sql.UserConn;

public class FisCompaignAction extends Action{
	
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
FisCompaignForm          form             = (FisCompaignForm) actionForm;
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
String nameCompaign = "";

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

stmt = conn.prepareStatement("SELECT  NomerLichnogoDela, familija, Imja, Otchestvo, KodAbiturienta From Abiturient a INNER JOIN FisImport b ON a.kodAbiturienta = b.id AND (b.Status = '+' OR b.Status = 'и')");

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
	  nameCompaign = form.getNameCompaign();
	  try {
		  
 		//создаем объект

	  Root root = new Root();
	  
	  
		  PackageData packageData = new PackageData();
		  Root.AuthData authData = new Root.AuthData();
		  authData.setLogin("revalle88@gmail.com");
		  authData.setPass("Dreamlord88");
		  PackageData.AdmissionInfo admissionInfo = new PackageData.AdmissionInfo();
		  PackageData.AdmissionInfo.AdmissionVolume admissionVolume = new PackageData.AdmissionInfo.AdmissionVolume();
		  PackageData.AdmissionInfo.CompetitiveGroups competitiveGroups = new PackageData.AdmissionInfo.CompetitiveGroups();
		  
		  
		  stmt = conn.prepareStatement("SELECT KodSpetsialnosti, ShifrSpetsialnosti, edulevel, fisid,  PlanPriema, PlanPriemaLg, PlanPriemaDog, nazvaniespetsialnosti, TselPr_PGU, TselPr_ROS FROM Spetsialnosti WHERE eduLevel in ('б','с') and tip_spec = 'о' ORDER BY 1 ASC");
            rs = stmt.executeQuery();
            while (rs.next()) {
            	PackageData.AdmissionInfo.AdmissionVolume.Item item = new PackageData.AdmissionInfo.AdmissionVolume.Item();
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup competitiveGroup = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.Items items = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.Items();
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.Items.CompetitiveGroupItem competitiveGroupItem = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.Items.CompetitiveGroupItem();
            	
            	item.setUID(rs.getString(1));
            	competitiveGroup.setUID(rs.getString(1));
            	competitiveGroup.setName(rs.getString(1)+rs.getString(8));
            	competitiveGroupItem.setUID(rs.getString(1));
            	
            	 String kod = rs.getString(3);
            	 if(kod.equals("б")){
            		// item.setEducationLevelID(2);
            		 competitiveGroupItem.setEducationLevelID(2); 
            	 }
            	/* if(kod.equals("68")){
            		 item.setEducationLevelID(4);
            		 competitiveGroupItem.setEducationLevelID(4);
            	 }*/
            	 if(kod.equals("с")){
            		// item.setEducationLevelID(5);
            		 competitiveGroupItem.setEducationLevelID(5);
            	 }
            	/* if(kod.equals("51")||kod.equals("52")){
            		 item.setEducationLevelID(17);
            		 competitiveGroupItem.setEducationLevelID(17);
            	 }*/
            	 
            	//item.setCourse(1);
            	competitiveGroup.setCourse(1);
            	//item.setCampaignUID(nameCompaign);
            	competitiveGroup.setCampaignUID(nameCompaign);
            	competitiveGroupItem.setDirectionID(rs.getLong(4));
            	competitiveGroupItem.setNumberBudgetO(rs.getLong(5));
            	competitiveGroupItem.setNumberQuotaO(rs.getLong(6));
            	competitiveGroupItem.setNumberPaidO(rs.getLong(7));
            	
            	
            	
            	
            	/*stmt1 = conn.prepareStatement("SELECT ID FROM DictionaryCode10 WHERE Code = ? and QualificationCode = ? ORDER BY 1 ASC");
       		  	stmt1.setObject(1,rs.getString(2).substring(0, 6),Types.NVARCHAR);
       		  	stmt1.setObject(2,rs.getString(2).substring(7, 9),Types.NVARCHAR);
                rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                	item.setDirectionID(rs1.getLong(1));
                	competitiveGroupItem.setDirectionID(rs1.getLong(1));
                   }*/
                   
              //  item.setNumberPaidOZ(rs.getLong(3));
                
            	
            	
            	
            	
            	
           // 	competitiveGroupItem.setNumberPaidOZ(rs.getLong(3));
                
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems entranceTestItems = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
                stmt1 = conn.prepareStatement("SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti where kodSpetsialnosti = ? ORDER BY 1 ASC");		
                stmt1.setObject(1,item.getUID(),Types.NVARCHAR);
                rs1 = stmt1.executeQuery();
                while (rs1.next()){
//                	
                
                	
                	int subject = 0;
                	stmt2 = conn.prepareStatement("SELECT ID from DictionaryCode1 where ID_PNZGU = ?");
                  stmt2.setObject(1,rs1.getInt(1),Types.INTEGER);
                    rs2 = stmt2.executeQuery();
                    while (rs2.next()){
                    	
                    	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem entranceTestItem = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
                    	entranceTestItem.setUID("ekz"+rs1.getString(1) + rs.getString(1));
                    	entranceTestItem.setForm("ЕГЭ");
                    	entranceTestItem.setEntranceTestTypeID(1);
                    	TEntranceTestSubject entranceTestSubject = new TEntranceTestSubject();
                  	  	entranceTestSubject.setSubjectID(rs2.getLong(1));
                  	  	subject = rs2.getInt(1); 
                  	  	entranceTestItem.setEntranceTestSubject(entranceTestSubject);
                    	entranceTestItems.getEntranceTestItem().add(entranceTestItem);
                    	
                    	
                  	 // entranceTestItems.getEntranceTestItem().add(entranceTestItem);
                    }
                    
                  /*  stmt2 = conn.prepareStatement("SELECT MinBallEge from NazvanijaPredmetov where KodPredmeta = ?");
                    stmt2.setObject(1,subject,Types.INTEGER);
                    rs2 = stmt2.executeQuery();
                    while (rs2.next()){
                    	entranceTestItem.setMinScore(rs2.getLong(1));
                    }*/
                }
                	/*entranceTestItem.setForm("ЕГЭ");
          		  TEntranceTestSubject entranceTestSubject = new TEntranceTestSubject();
                    entranceTestSubject.setSubjectName("Собеседование");
                    entranceTestItem.setEntranceTestSubject(entranceTestSubject);
                    entranceTestItem.setEntranceTestTypeID(1);
                    long i = 50;*/
                  //  entranceTestItem.setMinScore(i);
                  
                	
                competitiveGroup.setEntranceTestItems(entranceTestItems);
            	admissionVolume.getItem().add(item);
            	items.getCompetitiveGroupItem().add(competitiveGroupItem);
            	competitiveGroup.setItems(items);
            	
            	
            	//целевеки
            	if ((rs.getInt(9)!=0) || (rs.getInt(10)!=0))
            	{        
            		//targetOrganizations
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations targetOrganizations = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations();
            	 	//targetOrganization
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization targetOrganization = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization();
            		//setUid
            	
            	if (rs.getInt(9)!=0){
                	//	competitiveGroupTargetItem.setUID("tipgu"+rs.getString(1));
                	
                	targetOrganization.setUID("tpgu");
                	}
                	else
                	{
                	//competitiveGroupTargetItem.setUID("ti"+rs.getString(1));
                		targetOrganization.setUID("tros");
                	}
            	
            	//targetOrganization.setUID("t"+rs.getString(1));
            	//targetItems
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization.Items targetItems = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization.Items();
            	//CGTItems
            	PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization.Items.CompetitiveGroupTargetItem competitiveGroupTargetItem = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization.Items.CompetitiveGroupTargetItem();
            	//CGTItem set
            	competitiveGroupTargetItem.setDirectionID(rs.getLong(4));
            	
            	kod = rs.getString(3);
            	if(kod.equals("б")){
            		// item.setEducationLevelID(2);
            		 competitiveGroupTargetItem.setEducationLevelID(2);
            	 }
            	/* if(kod.equals("68")){
            		 item.setEducationLevelID(4);
            		 competitiveGroupItem.setEducationLevelID(4);
            	 }*/
            	 if(kod.equals("с")){
            		// item.setEducationLevelID(5);
            		 competitiveGroupTargetItem.setEducationLevelID(5);
            	 }
            	/* if(kod.equals("51")||kod.equals("52")){
            		 item.setEducationLevelID(17);
            		 competitiveGroupItem.setEducationLevelID(17);
            	 }*/
            	
            	
            	competitiveGroupTargetItem.setUID("ti"+rs.getString(1));
            	
            	
            	if (rs.getInt(9)!=0){
            	//	competitiveGroupTargetItem.setUID("tipgu"+rs.getString(1));
            	targetOrganization.setTargetOrganizationName("ПГУ");
            	competitiveGroupTargetItem.setNumberTargetO(rs.getLong(9));
            	}
            	else
            	{
            	//competitiveGroupTargetItem.setUID("ti"+rs.getString(1));
            	competitiveGroupTargetItem.setNumberTargetO(rs.getLong(10));
            	targetOrganization.setTargetOrganizationName("РОС");
            	}
            	
           
            	targetItems.getCompetitiveGroupTargetItem().add(competitiveGroupTargetItem);
            	targetOrganization.setItems(targetItems);
         //   	entranceTestItems.getEntranceTestItem().add(entranceTestItem);
            	
            	targetOrganizations.getTargetOrganization().add(targetOrganization);
            	competitiveGroup.setTargetOrganizations(targetOrganizations);
            	}
            	competitiveGroups.getCompetitiveGroup().add(competitiveGroup);
            	
            }
            
            
		//  admissionInfo.setAdmissionVolume(admissionVolume);
		  admissionInfo.setCompetitiveGroups(competitiveGroups);
		  packageData.setAdmissionInfo(admissionInfo);
		  root.setPackageData(packageData);
		  root.setAuthData(authData);
		//создаем выходной поток
		File of = new File("D:/compgn.xml");
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
