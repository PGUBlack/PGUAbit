package abit.action;
import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*; 


public class ZachLgotnikovAction extends Action {

	 public ActionForward perform ( ActionMapping        mapping,
             ActionForm           actionForm,
             HttpServletRequest   request,
             HttpServletResponse  response )
	 
	 throws IOException, ServletException
	    {   
	        HttpSession       session       = request.getSession();
	        Connection        conn          = null;
	        PreparedStatement pstmt          = null;
	        PreparedStatement pstmt1         = null;
	        PreparedStatement pstmt2         = null;
	        PreparedStatement pstmt3         = null;
	        
	        PreparedStatement lpstmt          = null;
	        PreparedStatement lpstmt1         = null;
	        PreparedStatement lpstmt2         = null;
	        PreparedStatement lpstmt3         = null;
	        
	        
	        Statement stmt         = null;
	        Statement stmt1         = null;
	        Statement stmt2         = null;
	        Statement stmt3         = null;
	        Statement stmt4         = null;
	        Statement stmt5         = null;
	        
	        Statement lstmt         = null;
	        Statement lstmt1         = null;
	        Statement lstmt2         = null;
	        Statement lstmt3         = null;
	        
	        ResultSet        lrs            = null;
	        ResultSet         lrs1           = null;
	        ResultSet         lrs2           = null;
	        ResultSet         rs            = null;
	        ResultSet         rs1           = null;
	        ResultSet         rs2           = null;
	        ResultSet         rs3           = null;
	        ResultSet         rs4           = null;
	        ResultSet         rs5           = null;
	        UserBean          user          = (UserBean)session.getAttribute("user");
	        ActionErrors      errors        = new ActionErrors();
	        ActionError       msg           = null;
	        StringBuffer         query              = new StringBuffer();
	        String ShifrSpetsialnosti = null;
      	  String KodSpetsialnosti = null;
	        
	        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
	            msg = new ActionError( "logon.must" );
	            errors.add( "logon.login", msg );
	           	}
	        
	        Locale locale = new Locale("ru","RU");
	        session.setAttribute( Action.LOCALE_KEY, locale );
	        
	        /**********************************************************************/
	        /*********  Получение соединения с БД и ведение статистики  ***********/
	        try {
	                  UserConn us = new UserConn(request, mapping);
	                  conn = us.getConn(user.getSid());
	                  
	                  
	                  
	               /*   stmt = conn.createStatement();
	                  stmt.executeUpdate("UPDATE Abiturient SET Prinjat='1' WHERE KodLgot IN ('2', '3', '4', '5') and KodFormyOb = '2' and kodOsnovyOb = '2' and TipDokSredObraz = 'о'");
	                  stmt = null;*/
	                  
	                  lpstmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE KodLgot IN ('2', '3', '4', '5') and KodFormyOb = '2' and kodOsnovyOb = '2' and TipDokSredObraz = 'о'");
	                  lrs1 = lpstmt.executeQuery();
	                  while (lrs1.next()){
	                	  lpstmt2 = conn.prepareStatement("SELECT KodAbiturienta, KodSpetsialnosti, KodKonkursa FROM Konkurs WHERE kodAbiturienta = '"+ lrs1.getString(1)+"' and Forma_Ob = 'о' and Bud = 'д'");
	                	  lrs2 = lpstmt2.executeQuery();
	                	  while (lrs2.next()){
	                	  KodSpetsialnosti = lrs2.getString(2);
	                	  lstmt1 = conn.createStatement();		
	                	  lstmt1.executeUpdate("UPDATE Abiturient SET Prinjat='1', kodSpetsialnZach = '"+KodSpetsialnosti+"'  WHERE kodAbiturienta = '"+lrs1.getString(1)+"'  and KodFormyOb = '2' and kodOsnovyOb = '2' and TipDokSredObraz = 'о'");
	                	  lstmt2 = conn.createStatement();		
	                	  lstmt2.executeUpdate("UPDATE konkurs SET Zach = 'д' WHERE kodKonkursa = '"+lrs2.getString(3) +"'");
	                	  
	                	  
	                	  }
	                  }
	        
	      
	                  
	                  pstmt1 = conn.prepareStatement("SELECT KodAbiturienta, KodSpetsialnosti, Target, KodKonkursa FROM Konkurs WHERE Target = 'д' and Forma_Ob = 'о' and Bud = 'д'");
	                  rs1 = pstmt1.executeQuery();
	                  while (rs1.next())
	                  {
	                	  pstmt2 = conn.prepareStatement("SELECT ShifrSpetsialnosti FROM spetsialnosti WHERE KodSpetsialnosti = '"+ rs1.getString(2)+"'");
	                	  rs2 = pstmt2.executeQuery();
	                	  while (rs2.next())
	                	  {	  	                	 
	                	 ShifrSpetsialnosti = rs2.getString(1);
	                	  KodSpetsialnosti = rs1.getString(2);
	                	  }
	                	  int indexofspec = ShifrSpetsialnosti.indexOf('.');
	                	  if (indexofspec == 6)
	                	  {	  
	                	  String LastKod = ShifrSpetsialnosti.substring(indexofspec+1, indexofspec+3);
	                	  	if (!LastKod.equals("68"))
	                	  	{
	                	  	   stmt1 = conn.createStatement();		
	                	  stmt1.executeUpdate("UPDATE Abiturient SET Prinjat='1', kodSpetsialnZach = '"+ KodSpetsialnosti+"'  WHERE kodAbiturienta = '"+rs1.getString(1)+"' and KodTselevogoPriema IN ('2', '4', '5') and KodFormyOb = '2' and kodOsnovyOb = '2' and TipDokSredObraz = 'о'");
	                	  stmt2 = conn.createStatement();		
	                	  stmt2.executeUpdate("UPDATE konkurs SET Zach = 'д' WHERE kodKonkursa = '"+rs1.getString(4) +"'");
	                      	}
	                      
	                	  }
	                	  
	                	  
	                	  
	                  }
	                  
	               //   stmt1 = conn.prepareStatement("UPDATE Abiturient SET Prinjat='1' WHERE KodTselPriema IN ('2', '4', '5') and KodFormyOb = '2' and kodOsnovyOb = '2' and TipDokSredObraz = 'о'");
	                //  rs1 = stmt1.executeQuery();
	                 // rs1.close();
	                            
	                  
	                  
	                  pstmt1 = null;
	                  conn = null;
	                  
	                  
	        }
	        catch ( SQLException e ) {
	            request.setAttribute("SQLException", e);
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
	            if ( pstmt != null ) {
	                 try {
	                       pstmt.close();
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
