package abit.action;

import java.io.IOException;
import java.sql.*;

import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;

import org.apache.struts.action.*;

import javax.naming.*;

import abit.action.AbiturientForm;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.servlet.UserSessions;
import abit.sql.*;

public class MoodleImportAction extends Action {
	
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  
	static final String DB_URL = "jdbc:sqlserver://localhost:1433";
    static final String USER = "moodle";
    static final String PASS = "12345678";
    
String currdate=StringUtil.CurrDate(".");
	 	   
public ActionForward perform
(
    ActionMapping       mapping,
    ActionForm          form,
    HttpServletRequest  request,
    HttpServletResponse response
    
)
throws IOException,
       ServletException
{
       
        HttpSession       session       = request.getSession();
        Connection        moodleConn          = null;
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        PreparedStatement stmt2          = null;
        ResultSet         rs            = null;
        ResultSet         rs2            = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
 //       MessageBean       mess          = new MessageBean();
        boolean           error         = false;
        ActionForward     f             = null;
        UserBean          user          = (UserBean)session.getAttribute("user");
        ArrayList         abit_A_S1     = new ArrayList();    
        
        try {
        	 UserConn us = new UserConn(request, mapping);
             conn = us.getConn(user.getSid());
			 
//			 stmt = conn.prepareStatement("select a.kodabiturienta, z.kodpredmeta, a.datainput from abit.dbo.abiturient A, abit.dbo.ZajavlennyeShkolnyeOtsenki z where a.kodabiturienta = z.kodabiturienta and a.datainput = '"+currdate+"'");
			 
			 stmt = conn.prepareStatement("select a.kodabiturienta, z.kodpredmeta, a.datainput from abit.dbo.abiturient A, abit.dbo.ZajavlennyeShkolnyeOtsenki z where a.kodabiturienta = z.kodabiturienta and a.datainput = '22.01.2016");
			 
			 rs = stmt.executeQuery();
		      while(rs.next()){
		    	  AbiturientBean abit_TMP = new AbiturientBean();
		          //Retrieve by column name
		    	  String kodabiturienta = rs.getString(1);
		          String kodpredmeta = rs.getString(2); 
		          abit_A_S1.add(abit_TMP);
		          //Display values
		          System.out.print(kodabiturienta);
		          System.out.println(kodpredmeta);
//		          dt=StringUtil.CurrDate(".");

		       }
		    System.out.println("Connection is successful!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connection is unsuccessful!");
		}

        try {
        	moodleConn = DriverManager.getConnection(DB_URL,USER,PASS);

        	stmt2 = moodleConn.prepareStatement("select a.kodabiturienta, z.kodpredmeta from abit.dbo.abiturient A, abit.dbo.ZajavlennyeShkolnyeOtsenki z where a.kodabiturienta = z.kodabiturienta INSERT INTO moodle.dbo.otsenki (a.kodabiturienta, z.kodpredmeta) select a.kodabiturienta, z.kodpredmeta from abit.dbo.abiturient A, abit.dbo.ZajavlennyeShkolnyeOtsenki z where a.kodabiturienta = z.kodabiturienta");
			 rs2 = stmt2.executeQuery();
		      while(rs2.next()){
		    	//  AbiturientBean abit_TMP = new AbiturientBean();
		          //Retrieve by column name
		    	  String kodabiturienta = rs2.getString(1);
		          String kodpredmeta = rs2.getString(2); 		             
		          
		   //       abit_A_S1.add(abit_TMP);
		          //Display values
		          System.out.print(kodabiturienta);
		          System.out.println(kodpredmeta);				          
		          
		          
		        
		       }
		    System.out.println("Connection is successful!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connection is unsuccessful!");
		}
        
        
        
        
        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( !errors.empty() ) {
        	return mapping.findForward("error");
        }
    if(request.getParameter("back")!=null) return mapping.findForward("back");
    return (mapping.findForward("success"));
    

}

}