package abit.ajax;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Locale;
import java.util.ArrayList;

import org.apache.struts.action.*;

import abit.action.SpetsialnostiForm;
import abit.bean.AbiturientBean;
import abit.bean.UserBean;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Locale;
import java.util.ArrayList;

import org.apache.struts.action.*;

import javax.sql.*;
import javax.naming.*;

import abit.bean.*;
import abit.Constants;
import abit.sql.*;
import abit.util.*;


public class StreetAction extends Action{

	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
	
	HttpServletRequest request, HttpServletResponse response)
	
	
	throws Exception {
		
		 HttpSession             session               = request.getSession();
	        Connection              conn                  = null;
	        PreparedStatement       stmt                  = null;
	        ResultSet               rs                    = null;
	        PreparedStatement       stmt1                  = null;
	        ResultSet               rs1                    = null;
	        ActionErrors            errors                = new ActionErrors();
	       
	        
	        ActionError             msg                   = null;
	        UserBean                user                  = (UserBean)session.getAttribute("user");
	        
	      request.setCharacterEncoding("UTF-8");
	        System.out.println(request.getContentType());
	        System.out.println(request.getCharacterEncoding());
	     
	        
	        
	    StreetForm ajaxForm = (StreetForm)form;
	    
	    if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }
	    
	    try {

	    	/**********************************************************************/
	    	/*********  Получение соединения с БД и ведение статистики  ***********/

	    	          UserConn us = new UserConn(request, mapping);
	    	          conn = us.getConn(user.getSid());
	    	        

	    	          
	    	        response.setContentType("text/text;charset=utf-8");
	    	       // response.setContentType("text/text;charset = cp1251");
	    	      	
	    	  	    response.setHeader("cache-control", "no-cache");
	    	  	
	    	  	   PrintWriter out = response.getWriter();
	    	  	  String selectHTML = "";
	    	  	String action=null;
	    	  	String nomerPotoka=null;
	    	  	int pp=0;
	    	 action = ajaxForm.getAction();
	    	  
	    	  if (action.equals("1"))
	    	  {

		    	   stmt = conn.prepareStatement("SELECT code, socr, name FROM STREET WHERE CODE Like ? order by name");
		    	  	 
		    		
		    	  	String code = ajaxForm.getCode();
		    	  	String code1 = code.substring(0,5);
		    	  	
		    	  //	String codeA = code2+"_____";
		    		String codeA = code1+"%";
		    		System.out.println(code1);
		    	  
		    	    stmt.setObject(1,codeA,Types.VARCHAR);
		    		
		    		
		    		  rs = stmt.executeQuery();
		    	        while (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(2)+" "+rs.getString(3)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(2) + " " + rs.getString(3);
		    	        	selectHTML = selectHTML + "</option>";
		    	        //	v++;
		    	        //	out.println("<select name='fisId' tabindex='2' onchange='fillEduLevel();' class='select_f1'>");
		    	         // out.println("<option value='-'></option>");	    	        	
		    	        }
		    	        	selectHTML = selectHTML + "<option value='Другая'>Другая</option>";
	    	     //  selectHTML = selectHTML + "</select>";
		    	        System.out.println(selectHTML);
	    	       
	    	  }  
	    	  if (action.equals("2"))
	    	  {

		    	   stmt = conn.prepareStatement("SELECT code, socr, name FROM STREET WHERE CODE Like ? order by name");
		    	  	 
		    		
		    	  	String code = ajaxForm.getCode();
		    	  	String code1 = code.substring(0,5);
		    	  	
		    	  //	String codeA = code2+"_____";
		    		String codeA = code1+"%";
		    		System.out.println(code1);
		    	  
		    	    stmt.setObject(1,codeA,Types.VARCHAR);
		    		
		    		
		    		  rs = stmt.executeQuery();
		    	        while (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(2)+" "+rs.getString(3)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(2) + " " + rs.getString(3);
		    	        	selectHTML = selectHTML + "</option>";
		    	        //	v++;
		    	        //	out.println("<select name='fisId' tabindex='2' onchange='fillEduLevel();' class='select_f1'>");
		    	         // out.println("<option value='-'></option>");	    	        	
		    	        }
		    	        	selectHTML = selectHTML + "<option value='Другая'>Другая</option>";
	    	     //  selectHTML = selectHTML + "</select>";
		    	        System.out.println(selectHTML);
	    	       
	    	  }  
	    	  
	    	  
	    	  
	    	  
	    	  out.println(selectHTML);
  		    
	    	  	
  	  	    out.flush();
  	  	    return null;


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
}
