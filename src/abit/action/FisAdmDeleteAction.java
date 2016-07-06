package abit.action;

import java.util.List;
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
import abit.paket.delete.*;



public class FisAdmDeleteAction extends Action{
	
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
	  DataForDelete packageData = new DataForDelete();
		  AuthData authData = new AuthData();
		  authData.setLogin("revalle88@gmail.com");
		  authData.setPass("Dreamlord88");

		  DataForDelete.CompetitiveGroups cg=new DataForDelete.CompetitiveGroups();
		  DataForDelete.TargetOrganizations tg=new DataForDelete.TargetOrganizations();
		  
		  stmt = conn.prepareStatement("select distinct shifrspetsialnosti, edulevel,fisid from spetsialnosti where edulevel in ('б','с')");
			rs = stmt.executeQuery();
			while (rs.next()) {
				  cg.getCompetitiveGroupUID().add("cgi16-" + rs.getString(1));
				  cg.getCompetitiveGroupUID().add("cgti16-" + rs.getString(1));
				  tg.getTargetOrganizationUID().add("tcPGU-" + rs.getString(1));
				  tg.getTargetOrganizationUID().add("tcRA-" + rs.getString(1));
				  tg.getTargetOrganizationUID().add("tcRK-" + rs.getString(1));
				  tg.getTargetOrganizationUID().add("tcMPT-" + rs.getString(1));

			}
		  
			
			packageData.setCompetitiveGroups(cg);
			packageData.setTargetOrganizations(tg);
		 

		  root.setDataForDelete(packageData);
		  root.setAuthData(authData);
  		//создаем выходной поток
  		File of = new File("D:/admDelHandy.xml");
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