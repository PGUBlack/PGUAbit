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


public class FillCGsAction extends Action{

	
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
	     
	        
	        
	    FillCGsForm ajaxForm = (FillCGsForm)form;
	    
	    if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }
	    
	    try {

	    	/**********************************************************************/
	    	/*********  Получение соединения с БД и ведение статистики  ***********/

	    	          UserConn us = new UserConn(request, mapping);
	    	          conn = us.getConn(user.getSid());
	    	        
	
	   /*response.setContentType("text/text;charset=Cp1251");
	
	    response.setHeader("cache-control", "no-cache");
	    
	    PrintWriter out = response.getWriter();
	    
	    stmt = conn.prepareStatement("select id, name, code, ugsname from dictionarycode10 where ugsname like ?");
	    stmt.setObject(1,"ХИМИЯ",Types.VARCHAR);
	 //   stmt.setObject(1,ajaxForm.getName(),Types.VARCHAR);
        rs = stmt.executeQuery();
        while (rs.next()) {
        	  //out.println("Hello " + rs.getString(2));
        //	out.println("<select name='fisId' tabindex='2' onchange='fillEduLevel();' class='select_f1'>");
           // out.println("<option value='-'></option>");
            
        	
        }
	    
      //  out.println("</select>");
       * 
       *    out.println("Hello Bitch");
	
	    out.flush();
	    return null;
	   
	*/
	    	          
	    	        response.setContentType("text/text;charset=utf-8");
	    	       // response.setContentType("text/text;charset = cp1251");
	    	      	
	    	  	    response.setHeader("cache-control", "no-cache");
	    	  	
	    	  	    PrintWriter out = response.getWriter();
	    	  	  String selectHTML = "";
	    	  	 int  v = 1;
	    	  
	    	  	 selectHTML = "<select name='fisId' tabindex='2' class='select_f1'>";
	    	  	 stmt = conn.prepareStatement("select id, name, code, ugsname from dictionarycode10 where KodKonGruppy like ? and eduLevel like ?");
	    		
	    	  	 String UTFString = ajaxForm.getName();
	    	
	    	  	String cpString = new String(UTFString.getBytes("utf8"), Charset.forName("cp1252"));
	    	  	System.out.println("cpString  "  + cpString);
	    	
	    		   stmt.setObject(1,cpString,Types.VARCHAR);
	    		   stmt.setObject(2,ajaxForm.getFormaOb(),Types.VARCHAR);
	    	        rs = stmt.executeQuery();
	    	        while (rs.next()) {
	    	        	 String eduLevelFromShifr = rs.getString(3).substring(3, 5);
	    	        	 String eduLevelChar = "";
	    	        	 if (eduLevelFromShifr.equals("02")) eduLevelChar = "п";      //спо          
	                     if (eduLevelFromShifr.equals("03")) eduLevelChar = "б";      //бакалавры	                
	                     if (eduLevelFromShifr.equals("04")) eduLevelChar = "м"; 	 //магистры
	                     if (eduLevelFromShifr.equals("05")) eduLevelChar = "с";     //специалисты	
	                     if (eduLevelFromShifr.equals("06")) eduLevelChar = "а";     //аспирантура
	                     if (eduLevelFromShifr.equals("07")) eduLevelChar = "х";     //ххх
	                     if (eduLevelFromShifr.equals("08")) eduLevelChar = "о";     //ординатура
	                     if (eduLevelFromShifr.equals("09")) eduLevelChar = "х";     //ххх
	                     
	                  
	            
	    	        	 //out.println("Hello BITCH" + ajaxForm.getName());
	    	        	//  out.println("Hello " + rs.getString(2));
	    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
	    	        	selectHTML = selectHTML + rs.getString(3) + " " + eduLevelChar + " " + rs.getString(2);
	    	        	selectHTML = selectHTML + "</option>";
	    	        //	v++;
	    	        //	out.println("<select name='fisId' tabindex='2' onchange='fillEduLevel();' class='select_f1'>");
	    	         // out.println("<option value='-'></option>");
	    	            
	    	        	
	    	        }
	    	        
	    	       selectHTML = selectHTML + "</select>";
	    	       /* selectHTML = "<select name='kodKonGrp' tabindex='1' onchange='fillCGs();' class='select_f1'><option value='1'>МАТЕМАТИКА И МЕХАНИКА</option>"+
	    	        "<option value='2'>КОМПЬЮТЕРНЫЕ И ИНФОРМАЦИОННЫЕ НАУКИ</option>"+
	    	        "<option value='3'>ФИЗИКА И АСТРОНОМИЯ</option></select>";*/
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
