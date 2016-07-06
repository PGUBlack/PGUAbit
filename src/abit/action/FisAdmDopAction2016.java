package abit.action;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import abit.paket.PackageData.AdmissionInfo;
import abit.paket.delete.DataForDelete;
import abit.paket.Root;
import abit.paket.TBasicDiplomaDocument;
import abit.paket.TEntranceTestSubject;
import abit.paket.TInstitutionDocument;
import abit.paket.THighEduDiplomaDocument;
import abit.paket.TIncomplHighEduDiplomaDocument;
import abit.paket.TMiddleEduDiplomaDocument;
import abit.paket.TSchoolCertificateDocument;



public class FisAdmDopAction2016 extends Action{
	
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
OutputStream os = null;
UserBean                user             = (UserBean)session.getAttribute("user");

if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
    msg = new ActionError( "logon.must" );
    errors.add( "logon.login", msg );
}

if ( errors.empty() ) {

Locale locale = new Locale("ru","RU");
session.setAttribute( Action.LOCALE_KEY, locale );

try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

  UserConn us = new UserConn(request, mapping);
  conn = us.getConn(user.getSid());


	  try {
		  
			abit.paket.delete.Root dr=new abit.paket.delete.Root();
			abit.paket.delete.AuthData dad=new abit.paket.delete.AuthData();
			abit.paket.delete.DataForDelete dfd=new abit.paket.delete.DataForDelete();
			abit.paket.delete.DataForDelete.CompetitiveGroups dcg=new abit.paket.delete.DataForDelete.CompetitiveGroups();
			dad.setLogin("revalle88@gmail.com");
			dad.setPass("Dreamlord88");
			  
			
			
			
			
			
			
	  Root root = new Root();
	  PackageData packageData = new PackageData();
		  Root.AuthData authData = new Root.AuthData();
		  authData.setLogin("revalle88@gmail.com");
		  authData.setPass("Dreamlord88");
		  
		 PackageData.AdmissionInfo admissioninfo = new PackageData.AdmissionInfo();
		 
		 PackageData.AdmissionInfo.AdmissionVolume admvolume = new PackageData.AdmissionInfo.AdmissionVolume();
		 PackageData.AdmissionInfo.DistributedAdmissionVolume dvolume = new PackageData.AdmissionInfo.DistributedAdmissionVolume();
		 
		 	stmt = conn.prepareStatement("select distinct s.shifrspetsialnosti, s.edulevel,dc.id from spetsialnosti s, dictionarycode10 dc where s.edulevel in ('м') and s.shifrspetsialnosti like dc.code");
			rs = stmt.executeQuery();
			while (rs.next()) {
        	   PackageData.AdmissionInfo.DistributedAdmissionVolume.Item ditem = new PackageData.AdmissionInfo.DistributedAdmissionVolume.Item();
      		 	PackageData.AdmissionInfo.AdmissionVolume.Item litem = new PackageData.AdmissionInfo.AdmissionVolume.Item();

        	          	   litem.setUID("16-"+rs.getString(1));
        	          	   ditem.setAdmissionVolumeUID("16d-"+rs.getString(1));
        	          	   ditem.setLevelBudget(1);
        	          	   litem.setCampaignUID("16mag-1");
        	          	   
        	          		   litem.setEducationLevelID(4);
        	          	   
        	        
        	          	   litem.setDirectionID(rs.getInt(3));
        	          	 stmt2 = conn.prepareStatement("select shifrspetsialnosti, edulevel,'-', sum(planpriema)-sum(krim_ok)-sum(planpriemalg), sum(planpriemadog), sum(TselPr_ROS)+sum(krim_cp), sum(planpriemalg)+sum(krim_ok) from spetsialnosti where shifrspetsialnosti like ? and tip_spec like 'о' and edulevel in ('м') group by shifrspetsialnosti,edulevel");
        	          	 stmt2.setObject(1,rs.getString(1),Types.VARCHAR);
        	          	 rs2 = stmt2.executeQuery();
        	             if (rs2.next()) {
        	          	   litem.setNumberBudgetO(rs2.getLong(4));
        	          	   litem.setNumberPaidO(rs2.getLong(5));
        	          	   litem.setNumberTargetO(rs2.getLong(6));
        	          	   litem.setNumberQuotaO(rs2.getLong(7));
        	          	   ditem.setNumberBudgetO(rs2.getLong(4));
        	          	   ditem.setNumberTargetO(rs2.getLong(6));
        	          	   ditem.setNumberQuotaO(rs2.getLong(7));
        	          	/*   
        	          	 litem.setNumberBudgetO(Long.valueOf(0));
      	          	   litem.setNumberPaidO(Long.valueOf(0));
      	          	   litem.setNumberTargetO(Long.valueOf(0));
      	          	   litem.setNumberQuotaO(Long.valueOf(0));
      	          	   ditem.setNumberBudgetO(Long.valueOf(0));
      	          	   ditem.setNumberTargetO(Long.valueOf(0));
      	          	   ditem.setNumberQuotaO(Long.valueOf(0));
        	          	*/   
        	             }
        	             stmt2 = conn.prepareStatement("select shifrspetsialnosti, edulevel,'-', sum(planpriema)-sum(krim_ok)-sum(planpriemalg), sum(planpriemadog), sum(TselPr_ROS)+sum(krim_cp), sum(planpriemalg)+sum(krim_ok) from spetsialnosti where shifrspetsialnosti like ? and tip_spec like 'з' and edulevel in ('м') group by shifrspetsialnosti,edulevel");
        	          	 stmt2.setObject(1,rs.getString(1),Types.VARCHAR);
        	          	 rs2 = stmt2.executeQuery();
        	             if (rs2.next()) {
        	         	   litem.setNumberBudgetZ(rs2.getLong(4));
        	          	   litem.setNumberPaidZ(rs2.getLong(5));
        	          	   litem.setNumberTargetZ(rs2.getLong(6));
        	          	   litem.setNumberQuotaZ(rs2.getLong(7));
        	          	   ditem.setNumberBudgetO(rs2.getLong(4));
        	          	   ditem.setNumberTargetO(rs2.getLong(6));
        	          	   ditem.setNumberQuotaO(rs2.getLong(7));
        	          	/*   
        	          	 litem.setNumberBudgetZ(Long.valueOf(0));
      	          	   litem.setNumberPaidZ(Long.valueOf(0));
      	          	   litem.setNumberTargetZ(Long.valueOf(0));
      	          	   litem.setNumberQuotaZ(Long.valueOf(0));
      	          	   ditem.setNumberBudgetO(Long.valueOf(0));
      	          	   ditem.setNumberTargetO(Long.valueOf(0));
      	          	   ditem.setNumberQuotaO(Long.valueOf(0));
      	          	   */
        	             }
        	             stmt2 = conn.prepareStatement("select shifrspetsialnosti, edulevel,'-', sum(planpriema)-sum(krim_ok)-sum(planpriemalg), sum(planpriemadog), sum(TselPr_ROS)+sum(krim_cp), sum(planpriemalg)+sum(krim_ok) from spetsialnosti where shifrspetsialnosti like ? and tip_spec like 'в' and edulevel in ('м') group by shifrspetsialnosti,edulevel");
        	          	 stmt2.setObject(1,rs.getString(1),Types.VARCHAR);
        	          	 rs2 = stmt2.executeQuery();
        	             if (rs2.next()) {
        	          	   litem.setNumberBudgetOZ(rs2.getLong(4));
        	          	   litem.setNumberPaidOZ(rs2.getLong(5));
        	          	   litem.setNumberTargetOZ(rs2.getLong(6));
        	          	   litem.setNumberQuotaOZ(rs2.getLong(7));
        	          	   ditem.setNumberBudgetO(rs2.getLong(4));
        	          	   ditem.setNumberTargetO(rs2.getLong(6));
        	          	   ditem.setNumberQuotaO(rs2.getLong(7));
        	          	/*  
        	          	 litem.setNumberBudgetOZ(Long.valueOf(0));
      	          	   litem.setNumberPaidOZ(Long.valueOf(0));
      	          	   litem.setNumberTargetOZ(Long.valueOf(0));
      	          	   litem.setNumberQuotaOZ(Long.valueOf(0));
      	          	   ditem.setNumberBudgetO(Long.valueOf(0));
      	          	   ditem.setNumberTargetO(Long.valueOf(0));
      	          	   ditem.setNumberQuotaO(Long.valueOf(0));
        	          	*/   
        	             }
        	          	 dvolume.getItem().add(ditem);
        	          	 admvolume.getItem().add(litem);
           }
           admissioninfo.setAdmissionVolume(admvolume);
       //    admissioninfo.setDistributedAdmissionVolume(dvolume);
         

   		//--------------------------------------------------------------------------
   		//-----------------------Конкурсные группы----------------------------------
   		//--------------------------------------------------------------------------
					PackageData.AdmissionInfo.CompetitiveGroups cgroups = new PackageData.AdmissionInfo.CompetitiveGroups();
					
					  
					int flag = 0;
					stmt = conn
							.prepareStatement("select s.kodspetsialnosti,s.nazvaniespetsialnosti, s.tip_spec,s.edulevel, dc10.id, (s.planpriema-s.krim_obshee-s.tselpr_ros-s.planpriemalg), (s.planpriemadog-s.ppkrimdog-s.planpriemaig), (s.planpriemalg), s.TselPr_ROS, (s.krim_obshee-s.krim_ok-s.krim_cp), s.ppkrimdog, s.krim_ok, s.krim_cp,s.planpriemaig from spetsialnosti s, dictionarycode10 dc10 where s.edulevel in ('м') and s.shifrspetsialnosti=dc10.code");
					rs = stmt.executeQuery();
					while (rs.next()) {
						flag = 0;

						//--------------------------------------------------------------------------
						//-----------------------Бюджет---------------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(6)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "b");
							
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();

						
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1)); 
								leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						
						cg.setEntranceTestItems(eti);

						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms eps = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram ep = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram();
						ep.setName(rs.getString(2));
						ep.setUID("cgi16-"+rs.getString(2)+""+rs.getString(3)+"b");
						eps.getEduProgram().add(ep);
						cg.setEduPrograms(eps);
						
						
						
						cg.setUID("cg16-" + rs.getString(1) + "b");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "b");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(14);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberBudgetOZ(rs.getLong(6));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberBudgetO(rs.getLong(6));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberBudgetZ(rs.getLong(6));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}
						
						//--------------------------------------------------------------------------
						//-----------------------Договор--------------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(7)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "d");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();

							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) + "d"); 

							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						cg.setEntranceTestItems(eti);

						
						
						cg.setUID("cg16-" + rs.getString(1) + "d");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "d");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(15);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberPaidOZ(rs.getLong(7));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberPaidO(rs.getLong(7));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberPaidZ(rs.getLong(7));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}						
						
						
						//--------------------------------------------------------------------------
						//-----------------------Особая квота---------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(8)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "l");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
			
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) + "l"); 
							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						cg.setEntranceTestItems(eti);

						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms eps = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram ep = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram();
						ep.setName(rs.getString(2));
						ep.setUID("cgi16-"+rs.getString(2)+""+rs.getString(3)+"l");
						eps.getEduProgram().add(ep);
						cg.setEduPrograms(eps);
						
						cg.setUID("cg16-" + rs.getString(1) + "l");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "l");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
				
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(20);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberQuotaOZ(rs.getLong(8));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberQuotaO(rs.getLong(8));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberQuotaZ(rs.getLong(8));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}
						
						
						//--------------------------------------------------------------------------
						//-----------------------Целевой прием--------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(9)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "t");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
						
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) + "t"); 
							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						cg.setEntranceTestItems(eti);

						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms eps = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram ep = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram();
						ep.setName(rs.getString(2));
						ep.setUID("cgi16-"+rs.getString(2)+""+rs.getString(3)+"t");
						eps.getEduProgram().add(ep);
						cg.setEduPrograms(eps);
						
						cg.setUID("cg16-" + rs.getString(1) + "t");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "t");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();

							cg.setEducationLevelID(4);

						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(16);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberBudgetOZ(rs.getLong(9));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberBudgetO(rs.getLong(9));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberBudgetZ(rs.getLong(9));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}
						
						
//--------------------------------------------------------------------------
//-----------------------Крым-----------------------------------------------
//--------------------------------------------------------------------------	
						
						//--------------------------------------------------------------------------
						//-----------------------Бюджет---------------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(10)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "kb");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
				
					
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) + "k"); 
							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						
						cg.setEntranceTestItems(eti);
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms eps = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram ep = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram();
						ep.setName(rs.getString(2));
						ep.setUID("cgi16-"+rs.getString(2)+""+rs.getString(3)+"kb");
						eps.getEduProgram().add(ep);
						cg.setEduPrograms(eps);
						
						
						cg.setUID("cg16-" + rs.getString(1) + "kb");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "kb");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(14);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberBudgetOZ(rs.getLong(10));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberBudgetO(rs.getLong(10));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberBudgetZ(rs.getLong(10));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}
						
						//--------------------------------------------------------------------------
						//-----------------------Договор--------------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(11)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "kd");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
						
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) + "kd"); 
							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						cg.setEntranceTestItems(eti);

						
						
						cg.setUID("cg16-" + rs.getString(1) + "kd");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "kd");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(15);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberPaidOZ(rs.getLong(11));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberPaidO(rs.getLong(11));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberPaidZ(rs.getLong(11));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}						
						
						
						//--------------------------------------------------------------------------
						//-----------------------Особая квота---------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(12)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "kl");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
						
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) +"kl"); 
							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						cg.setEntranceTestItems(eti);
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms eps = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram ep = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram();
						ep.setName(rs.getString(2));
						ep.setUID("cgi16-"+rs.getString(2)+""+rs.getString(3)+"kl");
						eps.getEduProgram().add(ep);
						cg.setEduPrograms(eps);
						cg.setIsForKrym(true);
						
						cg.setUID("cg16-" + rs.getString(1) + "kl");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "kl");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(20);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberQuotaOZ(rs.getLong(12));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberQuotaO(rs.getLong(12));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberQuotaZ(rs.getLong(12));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}
						
						
						//--------------------------------------------------------------------------
						//-----------------------Целевой прием--------------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(13)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "kt");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
						
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) +"kt"); 
							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						cg.setEntranceTestItems(eti);

						cg.setIsForKrym(true);
						
						cg.setUID("cg16-" + rs.getString(1) + "kt");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "kt");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(16);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberBudgetOZ(rs.getLong(13));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberBudgetO(rs.getLong(13));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberBudgetZ(rs.getLong(13));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}
						
						
						//--------------------------------------------------------------------------
						//-----------------------Договор Иностранцы---------------------------------
						//--------------------------------------------------------------------------
						if(rs.getInt(14)>0){
							dcg.getCompetitiveGroupUID().add("cg16-" + rs.getString(1) + "dig");
						//--------------------------------------------------------------------------
						//-----------------------Предметы конкурса----------------------------------
						//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
				
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) + "dig"); 
							leti.setEntranceTestTypeID(1);
							
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectName("Профессиональное");

							leti.setEntranceTestSubject(k);
							leti.setEntranceTestPriority(1);
							java.math.BigDecimal bd = new java.math.BigDecimal(60);
							leti.setMinScore(bd);
							eti.getEntranceTestItem().add(leti);
						cg.setEntranceTestItems(eti);

						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms eps = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms();
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram ep = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EduPrograms.EduProgram();
						ep.setName(rs.getString(2));
						ep.setUID("cgi16-"+rs.getString(2)+""+rs.getString(3)+"dig");
						eps.getEduProgram().add(ep);
						cg.setEduPrograms(eps);
						
						cg.setUID("cg16-" + rs.getString(1) + "dig");
						cg.setCampaignUID("16mag-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3) + "dig");
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						
							cg.setEducationLevelID(4);
						
						cg.setDirectionID(rs.getInt(5));
						cg.setEducationSourceID(15);
						if (rs.getString(3).equals("в")) {
							cgis.setNumberPaidOZ(rs.getLong(14));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberPaidO(rs.getLong(14));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberPaidZ(rs.getLong(14));
							cg.setEducationFormID(10);
						}
						cg.setCompetitiveGroupItem(cgis);
						cgroups.getCompetitiveGroup().add(cg);
						
						}
						
						
						
						
						
						
						
						
						dfd.setCompetitiveGroups(dcg);
						dr.setAuthData(dad);
						dr.setDataForDelete(dfd);
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
		//--------------------------------------------------------------------------
		//-----------------------Целевые организации--------------------------------
		//--------------------------------------------------------------------------						
											
/*						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations t = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations();
						for (int i = 9; i < 14; i++) {
							if (rs.getInt(i) != 0 && i != 10) {
								flag = 1;
								PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization tc = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization();

								PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization.CompetitiveGroupTargetItem cgti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations.TargetOrganization.CompetitiveGroupTargetItem();
								if (i == 9) {
									tc.setUID("tcPGU-" + rs.getString(1));
								} else if (i == 11) {
									tc.setUID("tcRA-" + rs.getString(1));
								} else if (i == 12) {
									tc.setUID("tcRK-" + rs.getString(1));
								} else if (i == 13) {
									tc.setUID("tcMPT-" + rs.getString(1));
								}
								if (rs.getString(3).equals("в")) {
									cgti.setNumberTargetOZ(rs.getLong(i));
								} else if (rs.getString(3).equals("о")) {
									cgti.setNumberTargetO(rs.getLong(i));
								} else {
									cgti.setNumberTargetZ(rs.getLong(i));
								}
								tc.setCompetitiveGroupTargetItem(cgti);
								
								t.getTargetOrganization().add(tc);
							}
						}
						if (flag == 1) {
							cg.setTargetOrganizations(t);
						}
						*/
						
						
					}
  				
  				
  		 	
  		 	admissioninfo.setCompetitiveGroups(cgroups);

           packageData.setAdmissionInfo(admissioninfo);
		  root.setPackageData(packageData);
		  root.setAuthData(authData);
  		//создаем выходной поток
  		File of = new File("D:/admissionMag.xml");
  		os = new FileOutputStream(of);
  		// Маршаллизация
  		JAXBContext context = JAXBContext.newInstance(Root.class);
  		Marshaller m = context.createMarshaller();
  		m.marshal(root, os);
  		os.close();
  		
  		of = new File("D:/admissionMagDelete.xml");
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