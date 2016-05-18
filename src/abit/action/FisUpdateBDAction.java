package abit.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

public class FisUpdateBDAction extends Action{
	
		
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
	ActionErrors            errors           = new ActionErrors();
	ActionError             msg              = null;
	//FisImportForm          form             = (FisImportForm) actionForm;
	//AbiturientBean          abit_F           = form.getBean(request, errors);
//	boolean                 fis_connect_f    = false;
	boolean                 error            = false;
	MessageBean             mess             = new MessageBean();
	ActionForward           f                = null;
	int                     kFak             = 1;
	UserBean                user             = (UserBean)session.getAttribute("user");


	if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
	    msg = new ActionError( "logon.must" );
	    errors.add( "logon.login", msg );
	}

	if ( errors.empty() ) {

	//request.setAttribute( "fisConnectAction", new Boolean(true) );
	Locale locale = new Locale("ru","RU");
	session.setAttribute( Action.LOCALE_KEY, locale );

	try {

	/**********************************************************************/
	/*********  Получение соединения с БД и ведение статистики  ***********/

	  UserConn us = new UserConn(request, mapping);
	  conn = us.getConn(user.getSid());
stmt = conn.prepareStatement("DELETE FROM FisImport");
      
      stmt.executeUpdate();
	  stmt = conn.prepareStatement("SELECT a.kodAbiturienta, a.NomerLichnogoDela,a.DokumentyHranjatsja, a.DataInput FROM abiturient a, konkurs k,spetsialnosti s WHERE a.kodabiturienta=k.kodabiturienta and a.kodspetsialnzach=k.kodspetsialnosti and a.kodspetsialnzach=s.kodspetsialnosti and s.tip_spec like 'з' and a.nomerpotoka=1 and a.NomerLichnogoDela IS NOT NULL  AND NOT EXISTS (SELECT * FROM FisImport b   WHERE b.AppNumber = a.NomerLichnogoDela) and a.dokumentyhranjatsja like 'д' and a.prinjat not like 'н' ");
      rs = stmt.executeQuery();
      while (rs.next()) {
    	  if(rs.getString(3).equals("н")){
    		  stmt = conn.prepareStatement("INSERT FisImport(id,AppNumber,Status, InputDate) VALUES(?,?, 'у',?)");
	          stmt.setObject(1, new Integer(rs.getInt(1)),Types.INTEGER);
	          stmt.setObject(2,rs.getString(2),Types.VARCHAR);
	          stmt.setObject(3,rs.getString(4),Types.VARCHAR);
	          stmt.executeUpdate();
    	  }else{
	    	  stmt = conn.prepareStatement("INSERT FisImport(id,AppNumber,Status, InputDate) VALUES(?,?, '+',?)");
	          stmt.setObject(1, new Integer(rs.getInt(1)),Types.INTEGER);
	          stmt.setObject(2,rs.getString(2),Types.VARCHAR);
	          stmt.setObject(3,rs.getString(4),Types.VARCHAR);
	          stmt.executeUpdate();
    	  }
      }
	  //request.setAttribute( "fisConnectForm", form );

	/*****************  Возврат к предыдущей странице   *******************/
	  if(us.quit("exit")) return mapping.findForward("back");
	/**********************************************************************/	   

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


