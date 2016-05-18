package abit.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import abit.bean.UserBean;
import abit.sql.UserConn;


	
	public class DateValidationAction  extends Action{
		
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
	UserBean                user             = (UserBean)session.getAttribute("user");
	String vDate = "";
	ArrayList failDates = new ArrayList();
	String fails = "";
	
	
	try {

		/**********************************************************************/
		/*********  Получение соединения с БД и ведение статистики  ***********/

		  UserConn us = new UserConn(request, mapping);
		  conn = us.getConn(user.getSid());
		  
		  stmt = conn.prepareStatement("SELECT Datarojdenija, DataVydDokumenta, kodAbiturienta FROM Abiturient where 1=1");
		  rs = stmt.executeQuery();
		  while (rs.next()){
			  vDate = rs.getString(1);
			  if ((vDate.indexOf('-')!=2) || (vDate.lastIndexOf('-')!=5)) {
				  failDates.add(rs.getString(3));
				  fails = fails+"  "+rs.getString(3);
			  }
			  vDate = rs.getString(2);
			  if ((vDate.indexOf('-')!=2) || (vDate.lastIndexOf('-')!=5)) {
				  failDates.add(rs.getString(3));
				  fails = fails+"  "+rs.getString(3);
			  }
			  request.setAttribute("failList", failDates);
			  request.setAttribute("fails", fails);
			 // vDate.lastIndexOf('-');
			  
			  
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
		   
	return mapping.findForward("success");		   
		   
}
		   
}
