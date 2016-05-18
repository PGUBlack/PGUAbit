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


public class KladrAction extends Action{

	
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
	     
	        
	        
	    KladrForm ajaxForm = (KladrForm)form;
	    
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
	    	  String razbor = ajaxForm.getAction();
	    	  if(razbor.length()==1){
	    		  action = razbor;
	    	  }else{
	    	      action = razbor.substring(1);
	    	      nomerPotoka = razbor.substring(0,1);
	    	  }
	    	  if (action.equals("1"))
	    	  {
	    	  	// selectHTML = "<select name='rajon_KLADR' tabindex='2' onchange='rajonChange();'  class='select_f1'>";
	    	  	 stmt = conn.prepareStatement("SELECT code, socr, name FROM KLADR WHERE (CODE Like ? or code like ?) and CODE not like ? ");
	    	  	 
	    	  	String xcode = null;
	    	  	String code = ajaxForm.getCode();
	    	  	String code1 = code.substring(0,2);
	    	  	String codeA = code1+"___000000__";
	    	  	String codeB = code1+"0000__00000";
	    	  	String codeC = code1+"000000000__";
	    	    stmt.setObject(1,codeA,Types.VARCHAR);
	    		stmt.setObject(2,codeB,Types.VARCHAR);
	    		stmt.setObject(3,codeC,Types.VARCHAR);
	    		  rs = stmt.executeQuery();
	    		  boolean flag = true;
	    	        while (rs.next()) {
	    	        	if (flag == true){
	    	        	xcode = rs.getString(1); 
	    	        	flag = false;
	    	        	}
	    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
	    	        	selectHTML = selectHTML + rs.getString(2) + " " + rs.getString(3);
	    	        	selectHTML = selectHTML + "</option>";
	    	        //	v++;
	    	        //	out.println("<select name='fisId' tabindex='2' onchange='fillEduLevel();' class='select_f1'>");
	    	         // out.println("<option value='-'></option>");
	    	
	    	
	    	        }
	    	        selectHTML = selectHTML + "@";
	    	       
		    	
		    	  
		    	   stmt = conn.prepareStatement("SELECT code, socr, name FROM KLADR WHERE CODE Like ? and code not like ?");
		    	  	 
		    		
		    	  
		    	  	String xcode1 = xcode.substring(0,5);
		    	  	String xcode2 = xcode.substring(0,8);
		    	  //	String codeA = code2+"_____";
		    		String xcodeA = xcode1+"________";

		    	  	String xcodeB = xcode1+"000000__";
		    	  
		    	    stmt.setObject(1,xcodeA,Types.VARCHAR);
		    		stmt.setObject(2,xcodeB,Types.VARCHAR);
		    		
		    		  rs = stmt.executeQuery();
		    	        while (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(2) + " " + rs.getString(3);
		    	        	selectHTML = selectHTML + "</option>";
		    	        //	v++;
		    	        //	out.println("<select name='fisId' tabindex='2' onchange='fillEduLevel();' class='select_f1'>");
		    	         // out.println("<option value='-'></option>");
		    	            
		    	        	
		    	        }
	    	        
	    	
	    	
	    	        
	    	     //  selectHTML = selectHTML + "</select>";
	    	       
	    	  }
	    	  else if(action.equals("2")) {
	    		  
		    	//  	 selectHTML = "<select name='punkt_KLADR' tabindex='2' class='select_f1'>";
		    	
		    	  
		    	   stmt = conn.prepareStatement("SELECT code, socr, name FROM KLADR WHERE CODE Like ? and code not like ?");
		    	  	 
		    		
		    	  	String code = ajaxForm.getCode();
		    	  	String code1 = code.substring(0,5);
		    	  	String code2 = code.substring(0,8);
		    	  //	String codeA = code2+"_____";
		    		String codeA = code1+"________";

		    	  	String codeB = code1+"000000__";
		    	  
		    	    stmt.setObject(1,codeA,Types.VARCHAR);
		    		stmt.setObject(2,codeB,Types.VARCHAR);
		    		
		    		  rs = stmt.executeQuery();
		    	        while (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(2) + " " + rs.getString(3);
		    	        	selectHTML = selectHTML + "</option>";
		    	        //	v++;
		    	        //	out.println("<select name='fisId' tabindex='2' onchange='fillEduLevel();' class='select_f1'>");
		    	         // out.println("<option value='-'></option>");
		    	            
		    	        	
		    	        }
		    	        
		    	
		    	
		    	        
		    	      // selectHTML = selectHTML + "</select>";
		    	       
		    	  }else if(action.equals("3")) {
		    		  String code = ajaxForm.getCode();
		    		  if(code.equals("-")){
		    			  selectHTML = "<option value='-'> - ";
		    		  }else{
		    		  
		    		  String zapros=null;
		    		  if(nomerPotoka.equals("1")){
		    			  zapros="('с','б')";
		    		  }else if(nomerPotoka.equals("2")){
		    			  zapros="('м')";
		    		  }else if(nomerPotoka.equals("3")){
		    			  zapros="('и')";
		    		  }else if(nomerPotoka.equals("4")){
		    			  zapros="('о')";
		    		  }else if(nomerPotoka.equals("5")){
		    			  zapros="('а')";
		    		  }else if(nomerPotoka.equals("6")){
		    			  zapros="('п')";
		    		  }
		    		  StringBuffer query = new StringBuffer("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like '"+code+"' AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.edulevel IN "+zapros+" ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		    		  System.out.println(query.toString());
		    		  stmt = conn.prepareStatement(query.toString());
			    	  	 
			    		
			    	  	
			    	  	
			    	   // stmt.setObject(1,code,Types.VARCHAR);
			    	  //  stmt.setObject(2,zapros,Types.VARCHAR);
			    	
			    		  rs = stmt.executeQuery();
			    	        while (rs.next()) {
			    	        	
			    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
			    	        	selectHTML = selectHTML + rs.getString(3);
		    		  
			    	        }
		    		  }
		    		  selectHTML = selectHTML + "</option>";
		    		  
		    	  }else if(action.equals("4")) {
		    		  String code = ajaxForm.getCode();
		    		  if(code.equals("-")){
		    			  selectHTML = "<option value='-'> - ";
		    		  }else{
		    		  String zapros=null;
		    		  if(nomerPotoka.equals("1")){
		    			  zapros="('с','б')";
		    		  }else if(nomerPotoka.equals("2")){
		    			  zapros="('м')";
		    		  }else if(nomerPotoka.equals("3")){
		    			  zapros="('и')";
		    		  }else if(nomerPotoka.equals("4")){
		    			  zapros="('о')";
		    		  }else if(nomerPotoka.equals("5")){
		    			  zapros="('а')";
		    		  }else if(nomerPotoka.equals("6")){
		    			  zapros="('п')";
		    		  }
		    		  StringBuffer query = new StringBuffer("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like '"+code+"' AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.edulevel IN "+zapros+" ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		    		  System.out.println(query.toString());
		    		  stmt = conn.prepareStatement(query.toString());
			    		
			    		  rs = stmt.executeQuery();
			    	        while (rs.next()) {
			    	        	
			    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
			    	        	selectHTML = selectHTML + rs.getString(3);
		    		  
			    	        }
		    		  }
		    		  selectHTML = selectHTML + "</option>";
		    		  
		    	  }else if(action.equals("5")) {
		    		  String code = ajaxForm.getCode();
		    		 if(!code.equals("-")){
		    		  stmt = conn.prepareStatement("SELECT DISTINCT planpriema, kodspetsialnosti from spetsialnosti where kodspetsialnosti like '"+code+"'");
			    		
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	
		    	        	pp=rs.getInt(1);
		    	        	if(pp==0){
		    	        		selectHTML = "1";
		    	        	}else{
		    	        		selectHTML = "0";
		    	        	}
		    	        }
		    		 }
			    	    
		    		  
		    		  
		    	  }else if(action.equals("6")) {
		    		  String code = ajaxForm.getCode();
		    		  stmt = conn.prepareStatement("SELECT KodAbiturienta FROM abiturient where nomerdokumenta like '"+code+"' and nomerpotoka like '"+nomerPotoka+"'");
			    		
		    		  rs = stmt.executeQuery();
		    		  if(rs.next()){
		    			  selectHTML = "1";
		    		  }else{
		    			  selectHTML = "0";
	    	        	}
		    		  
		    	  
		    		  
		    	  }else if(action.equals("7")) {
		    		  String code = ajaxForm.getCode();
		    		 
		    		  stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=3 ORDER BY s.KodSpetsialnosti ASC");
		    		  stmt.setObject(1,code,Types.VARCHAR);
			    		
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(3);
		    	        	
		    	        	stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
					    	   stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
					    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
					    	   rs1 = stmt1.executeQuery();
		    	        	while (rs1.next()) {
		    	        		selectHTML = selectHTML + "<option value='"+rs1.getString(1)+"'>";
			    	        	selectHTML = selectHTML + rs1.getString(3);
		    	        	}
		    	        	
		    	        }else{
		    			  selectHTML = "<option value='-'> - ";
		    	        }
		    	        selectHTML = selectHTML + "</option>";	  
			    	    
		    		  
		    		  
		    	  
		    		  
		    	  }else if(action.equals("7")) {
		    		  String code = ajaxForm.getCode();
		    		 
		    		  stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=3 ORDER BY s.KodSpetsialnosti ASC");
		    		  stmt.setObject(1,code,Types.VARCHAR);
			    		
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(3);
		    	        	
		    	        	stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		    	        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
					    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
					    	   rs1 = stmt1.executeQuery();
		    	        	while (rs1.next()) {
		    	        		selectHTML = selectHTML + "<option value='"+rs1.getString(1)+"'>";
			    	        	selectHTML = selectHTML + rs1.getString(3);
		    	        	}
		    	        	
		    	        }else{
		    			  selectHTML = "<option value='-'> - ";
		    	        }
		    	        selectHTML = selectHTML + "</option>";	  
			    	    
		    		  
		    		  
		    	  }else if(action.equals("8")) {
		    		  String code = ajaxForm.getCode();
		    		 
		    		  stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=4 ORDER BY s.KodSpetsialnosti ASC");
		    		  stmt.setObject(1,code,Types.VARCHAR);
			    		
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(3);
		    	        	
		    	        	stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		    	        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
					    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
					    	   rs1 = stmt1.executeQuery();
		    	        	while (rs1.next()) {
		    	        		selectHTML = selectHTML + "<option value='"+rs1.getString(1)+"'>";
			    	        	selectHTML = selectHTML + rs1.getString(3);
		    	        	}
		    	        	
		    	        }else{
		    			  selectHTML = "<option value='-'> - ";
		    	        }
			    			  
		    	        selectHTML = selectHTML + "</option>";
		    		  
		    		  
		    	  }else if(action.equals("9")) {
		    		  String code = ajaxForm.getCode();
		    		 
		    		  stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=5 ORDER BY s.KodSpetsialnosti ASC");
		    		  stmt.setObject(1,code,Types.VARCHAR);
			    		
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(3);
		    	        	
		    	        	stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		    	        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
					    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
					    	   rs1 = stmt1.executeQuery();
		    	        	while (rs1.next()) {
		    	        		selectHTML = selectHTML + "<option value='"+rs1.getString(1)+"'>";
			    	        	selectHTML = selectHTML + rs1.getString(3);
		    	        	}
		    	        	
		    	        }else{
		    			  selectHTML = "<option value='-'> - ";
		    	        }
			    			  
		    	        selectHTML = selectHTML + "</option>";
		    		  
		    		  
		    	  }else if(action.equals("10")) {
		    		  String code = ajaxForm.getCode();
		    		 
		    		  stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=6 ORDER BY s.KodSpetsialnosti ASC");
		    		  stmt.setObject(1,code,Types.VARCHAR);
			    		
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	
		    	        	selectHTML = selectHTML + "<option value='"+rs.getString(1)+"'>";
		    	        	selectHTML = selectHTML + rs.getString(3);
		    	        	
		    	        	stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		    	        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
					    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
					    	   rs1 = stmt1.executeQuery();
		    	        	while (rs1.next()) {
		    	        		selectHTML = selectHTML + "<option value='"+rs1.getString(1)+"'>";
			    	        	selectHTML = selectHTML + rs1.getString(3);
		    	        	}
		    	        	
		    	        }else{
		    			  selectHTML = "<option value='-'> - ";
		    	        }
			    			  
			    	    
		    	        selectHTML = selectHTML + "</option>";
		    		  
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
