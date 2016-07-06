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
import abit.paket.Root;
import abit.paket.TBasicDiplomaDocument;
import abit.paket.TEntranceTestSubject;
import abit.paket.TInstitutionDocument;
import abit.paket.THighEduDiplomaDocument;
import abit.paket.TIncomplHighEduDiplomaDocument;
import abit.paket.TMiddleEduDiplomaDocument;
import abit.paket.TSchoolCertificateDocument;


public class FisAdmAction extends Action{
	
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
		  
    		//создаем объект

	  Root root = new Root();
	  PackageData packageData = new PackageData();
		  Root.AuthData authData = new Root.AuthData();
		  authData.setLogin("revalle88@gmail.com");
		  authData.setPass("Dreamlord88");
		  
		 PackageData.AdmissionInfo admissioninfo = new PackageData.AdmissionInfo();
		 PackageData.AdmissionInfo.AdmissionVolume admvolume = new PackageData.AdmissionInfo.AdmissionVolume();
		 PackageData.AdmissionInfo.DistributedAdmissionVolume dvolume = new PackageData.AdmissionInfo.DistributedAdmissionVolume();
		 	stmt = conn.prepareStatement("select distinct shifrspetsialnosti, edulevel,fisid from spetsialnosti where edulevel in ('б','с')");
			rs = stmt.executeQuery();
			while (rs.next()) {
        	   PackageData.AdmissionInfo.DistributedAdmissionVolume.Item ditem = new PackageData.AdmissionInfo.DistributedAdmissionVolume.Item();
      		 	PackageData.AdmissionInfo.AdmissionVolume.Item litem = new PackageData.AdmissionInfo.AdmissionVolume.Item();

        	          	   litem.setUID("15-"+rs.getString(1));
        	          	   ditem.setAdmissionVolumeUID("15d-"+rs.getString(1));
        	          	   ditem.setLevelBudget(1);
        	          	   litem.setCampaignUID("15bakspec-1");
        	          	   if(rs.getString(2).equals("б")){
        	          		   litem.setEducationLevelID(2);
        	          	   }else{
        	          		   litem.setEducationLevelID(5);
        	          	   }
        	        
        	          	   litem.setDirectionID(rs.getInt(3));
        	          	 stmt2 = conn.prepareStatement("select shifrspetsialnosti, edulevel,fisid, sum(planpriema), sum(planpriemadog), sum(TselPr_PGU+TselPr_ROS+TselPr_1+TselPr_2+TselPr_3), sum(planpriemalg) from spetsialnosti where shifrspetsialnosti like ? and tip_spec like 'о' and edulevel in ('с','б') group by shifrspetsialnosti,edulevel,fisid");
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
        	          	   
        	             }
        	             stmt2 = conn.prepareStatement("select shifrspetsialnosti, edulevel,fisid, sum(planpriema), sum(planpriemadog), sum(TselPr_PGU+TselPr_ROS+TselPr_1+TselPr_2+TselPr_3), sum(planpriemalg) from spetsialnosti where shifrspetsialnosti like ? and tip_spec like 'з' and edulevel in ('с','б') group by shifrspetsialnosti,edulevel,fisid");
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
        	             }
        	             stmt2 = conn.prepareStatement("select shifrspetsialnosti, edulevel,fisid, sum(planpriema), sum(planpriemadog), sum(TselPr_PGU+TselPr_ROS+TselPr_1+TselPr_2+TselPr_3), sum(planpriemalg) from spetsialnosti where shifrspetsialnosti like ? and tip_spec like 'в' and edulevel in ('с','б') group by shifrspetsialnosti,edulevel,fisid");
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
        	             }
        	          	 dvolume.getItem().add(ditem);
        	          	 admvolume.getItem().add(litem);
           }
           admissioninfo.setAdmissionVolume(admvolume);
           admissioninfo.setDistributedAdmissionVolume(dvolume);
           
           
					PackageData.AdmissionInfo.CompetitiveGroups cgroups = new PackageData.AdmissionInfo.CompetitiveGroups();
					int flag = 0;
					stmt = conn
							.prepareStatement("select s.kodspetsialnosti,s.nazvaniespetsialnosti, s.tip_spec,s.edulevel, s.fisid, s.planpriema, s.planpriemadog, s.planpriemalg, s.TselPr_PGU,s.TselPr_ROS,s.TselPr_1,s.TselPr_2,s.TselPr_3, s.krim_obshee, s.krim_ok, s.) from spetsialnosti s where s.edulevel in ('б','с')");
					rs = stmt.executeQuery();
					while (rs.next()) {
						flag = 0;
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup cg = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup();
		//--------------------------------------------------------------------------
		//-----------------------Предметы конкурса----------------------------------
		//--------------------------------------------------------------------------
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems eti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems();
						stmt2 = conn
								.prepareStatement("select np.idfbs, ens.prioritet,np.predmet from nazvanijapredmetov np, ekzamenynaspetsialnosti ens where ens.kodpredmeta=np.kodpredmeta and ens.kodspetsialnosti like ? and np.idfbs not like '0' order by ens.prioritet");
						stmt2.setObject(1, rs.getString(1), Types.VARCHAR);
						rs2 = stmt2.executeQuery();
						while (rs2.next()) {
							PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem leti = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.EntranceTestItems.EntranceTestItem();
							leti.setUID("16leti-" + rs.getString(1) + "-"
									+ rs2.getString(2)); 
							if (rs2.getString(1) == null
									|| rs2.getString(1).equals("0")) {
								leti.setEntranceTestTypeID(2);
							} else {
								leti.setEntranceTestTypeID(1);
							}
							TEntranceTestSubject k = new TEntranceTestSubject();
							k.setSubjectID(rs2.getLong(1));
							k.setSubjectName(rs2.getString(3));
							leti.setEntranceTestSubject(k);
							BigInteger bi = BigInteger.valueOf(rs2.getInt(2));
							leti.setEntranceTestPriority(bi);

							eti.getEntranceTestItem().add(leti);
						}
						cg.setEntranceTestItems(eti);
						
		//--------------------------------------------------------------------------
		//-----------------------Конкурсные группы----------------------------------
		//--------------------------------------------------------------------------
						cg.setUID("cg16-" + rs.getString(1));
						cg.setCampaignUID("16bakspec-1");
						cg.setName("16-" + rs.getString(2) + "-"
								+ rs.getString(1) + "" + rs.getString(3));
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem cgis = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.CompetitiveGroupItem();
						if (rs.getString(4).equals("б")) {
							cg.setEducationLevelID(2);
						} else {
							cg.setEducationLevelID(5);
						}
						cg.setDirectionID(rs.getInt(5));

						if (rs.getString(3).equals("в")) {
							cgis.setNumberBudgetOZ(rs.getLong(6));
							cgis.setNumberPaidOZ(rs.getLong(7));
							cgis.setNumberQuotaOZ(rs.getLong(8));
							cg.setEducationFormID(12);
						} else if (rs.getString(3).equals("о")) {
							cgis.setNumberBudgetO(rs.getLong(6));
							cgis.setNumberPaidO(rs.getLong(7));
							cgis.setNumberQuotaO(rs.getLong(8));
							cg.setEducationFormID(11);
						} else {
							cgis.setNumberBudgetZ(rs.getLong(6));
							cgis.setNumberPaidZ(rs.getLong(7));
							cgis.setNumberQuotaZ(rs.getLong(8));
							cg.setEducationFormID(10);
						}
						
		//--------------------------------------------------------------------------
		//-----------------------Целевые организации--------------------------------
		//--------------------------------------------------------------------------						
											
						PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations t = new PackageData.AdmissionInfo.CompetitiveGroups.CompetitiveGroup.TargetOrganizations();
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
						
						cg.setCompetitiveGroupItem(cgis);

						cgroups.getCompetitiveGroup().add(cg);
					}
  				
  				
  		 	
  		 	admissioninfo.setCompetitiveGroups(cgroups);

           packageData.setAdmissionInfo(admissioninfo);
		  root.setPackageData(packageData);
		  root.setAuthData(authData);
  		//создаем выходной поток
  		File of = new File("D:/admission.xml");
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