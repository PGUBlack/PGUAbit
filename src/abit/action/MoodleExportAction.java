package abit.action;

import java.io.IOException;
import java.sql.*;

import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

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

public class MoodleExportAction extends Action {
	

	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://172.16.254.20:3306/asu?characterEncoding=UTF-8";
    static final String USER = "asu1";
    static final String PASS = "asuUI";
	 	   
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
	        Connection        conn          = null;
	        Connection        MoodleConn          = null;
	        PreparedStatement stmt          = null;
	        PreparedStatement stmt1          = null;
	        PreparedStatement stmt2          = null;
	        PreparedStatement stmtInsertAbit         = null;
	        PreparedStatement stmtInsertOtsenki         = null;
	        ResultSet         rs            = null;
	        ResultSet         rs1         = null;
	        ResultSet         rs2        = null;
	        ActionErrors      errors        = new ActionErrors();
	        ActionError       msg           = null;
	        MessageBean       mess          = new MessageBean();
	        boolean           error         = false;
	        ActionForward     f             = null;
	        UserBean          user          = (UserBean)session.getAttribute("user");
	        ArrayList         abit_A_S1     = new ArrayList();   
	        
	        
	        try {
                // The newInstance() call is a work around for some
                // broken Java implementations

                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception ex) {
                // handle the error
            }
	        
	        try {
	        	
				MoodleConn = DriverManager.getConnection(DB_URL,USER,PASS);
				UserConn us = new UserConn(request, mapping);
	            conn = us.getConn(user.getSid());
				 
//				 stmt = conn.prepareStatement("select a.kodabiturienta, z.kodpredmeta, a.datainput from abit.dbo.abiturient A, abit.dbo.ZajavlennyeShkolnyeOtsenki z where a.kodabiturienta = z.kodabiturienta and a.datainput = '"+currdate+"'");
	            String  dt=StringUtil.CurrDate(".");
	            
	            /******************импорт групп*****************************/
	          /*  stmt = conn.prepareStatement("select g.kodGruppy, g.Gruppa from Gruppy g");
	            rs = stmt.executeQuery();
			      while(rs.next()){
			    	 int kodGruppy = rs.getInt(1);
			    	 String groupName = rs.getString(2);
			    	//String strGroup = " \"group\" ";
			    	  stmt1 = MoodleConn.prepareStatement("select id from `group` where id = ?");
			             stmt1.setObject(1, kodGruppy,Types.INTEGER);
			             rs1  =  stmt1.executeQuery();
			             	if (rs1.next()){
			             		stmt2 = MoodleConn.prepareStatement("Update `group` set name = ? where id = ?");
			             		 stmt2.setObject(2, kodGruppy,Types.INTEGER);
			             		 stmt2.setObject(1, groupName,Types.VARCHAR);
			             		 stmt2.executeUpdate();
			             	}
			             	else {
			             		stmt2 = MoodleConn.prepareStatement("INSERT `group`(id, name) VALUES(?,?)");
			             		 stmt2.setObject(1, kodGruppy,Types.INTEGER);
			             		 stmt2.setObject(2, groupName,Types.VARCHAR);
			             		 stmt2.executeUpdate();
			             	}
			      }*/
	            
	            /********************импорт групп --- КОНЕЦ -----*****************/
	            /*****************ИМПОРТ НОВЫХ АБИТУРИЕНТВО СО СТАТУСОМ 0 ************************************/
	            
	            
				 stmt = conn.prepareStatement("select a.kodabiturienta, a.nomerlichnogodela,  a.familija, a.imja, a.otchestvo, a.kodgruppy, lang=case adi.vstup WHEN 'английский' then 'en' else 'ru' end, a.InostrannyjJazyk from abiturient a, abitDopInf adi where adi.kodabiturienta = a.kodabiturienta  and a.datainput = ? and dokumentyHranjatsja = 'д'");
				 stmt.setObject(1,dt,Types.VARCHAR);
				 rs = stmt.executeQuery();
			      while(rs.next()){
			    	  
			    	
			    	  
			    	  //insert abit info to moodle
			    	  String inosJaz = rs.getString(8);
			    	  boolean sdaetInjaz = false;		
			    	  String lang = rs.getString(7);
			    	  
			    	  
			    	  stmt2 = MoodleConn.prepareStatement("select kodabiturienta from abiturient where kodabiturienta = ?");
			    	  stmt2.setObject(1, rs.getString(1),Types.VARCHAR);
			    	  rs2 = stmt2.executeQuery();
			    	  if (!rs2.next()){
				        
			    	 // if (rs.getString(7).equals("английский")) lang = "en";
			    	  
			    	  stmtInsertAbit = MoodleConn.prepareStatement("INSERT abiturient(kodabiturienta, nomerlichnogodela,  familija, imja, otchestvo, status, nomerGroup, password, lang) VALUES(?,?,?,?,?,?,?,'',?)");
			             stmtInsertAbit.setObject(1, rs.getString(1),Types.VARCHAR);
			             stmtInsertAbit.setObject(2, rs.getString(2),Types.VARCHAR);
			             stmtInsertAbit.setObject(3, rs.getString(3),Types.VARCHAR);
			             stmtInsertAbit.setObject(4, rs.getString(4),Types.VARCHAR);
			             stmtInsertAbit.setObject(5, rs.getString(5),Types.VARCHAR);
			             stmtInsertAbit.setObject(6, 0,Types.INTEGER);
			             stmtInsertAbit.setObject(7, rs.getInt(6),Types.INTEGER);
			             stmtInsertAbit.setObject(8, lang, Types.VARCHAR);
			             stmtInsertAbit.executeUpdate();
			             //end of insert
			    	  
			    	  
			    	  stmt1 = conn.prepareStatement("select kodPredmeta, kodabiturienta, ex=case Examen WHEN '+' then 1 else 0 end from zajavlennyeShkolnyeOtsenki where kodabiturienta = ?");
			    	  stmt1.setObject(1,rs.getString(1),Types.VARCHAR);
			    	   rs1 = stmt1.executeQuery();
			    	   while(rs1.next()){
			    		   stmtInsertOtsenki = MoodleConn.prepareStatement("INSERT otsenki(kodabiturienta, kodpredmeta, ball,  status, flag, idType) VALUES (?, ?, -1, 0, ?, 2)");
			    		   stmtInsertOtsenki.setObject(1, rs1.getString(2),Types.VARCHAR);
			    		   stmtInsertOtsenki.setObject(2, rs1.getString(1),Types.VARCHAR);
			    		
			    		   
			    		   stmtInsertOtsenki.setObject(3, rs1.getInt(3),Types.INTEGER);
			    		   if ((rs1.getString(1).equals("5"))&&(rs1.getInt(3) == 1)) 
			    		   {	   
			    			   sdaetInjaz = true;
			    			   if (!inosJaz.equals("английский")&&(sdaetInjaz == true)) {
			    				   stmtInsertOtsenki.setObject(3, 0,Types.INTEGER);
			    			   }
			    		   } 
			    				   
			    		   stmtInsertOtsenki.executeUpdate();
			    		   
			    		   System.out.print(rs1.getString(1));
			    	   }
			    	   
			    	   //добавляем немецкий
			    	   stmtInsertOtsenki = MoodleConn.prepareStatement("INSERT otsenki(kodabiturienta, kodpredmeta, ball,  status, flag, idType) VALUES (?, 14, -1, 0, ?, 2)");
		    		   stmtInsertOtsenki.setObject(1, rs.getString(1),Types.VARCHAR);
		    		  
		    		   int flag = 0;
		    		   if (inosJaz.equals("немецкий")&&(sdaetInjaz == true)) flag = 1;  
		    		   stmtInsertOtsenki.setObject(2, flag,Types.INTEGER);
		    		   stmtInsertOtsenki.executeUpdate();
		    		  // добавляем французский 
		    		   stmtInsertOtsenki = MoodleConn.prepareStatement("INSERT otsenki(kodabiturienta, kodpredmeta, ball,  status, flag, idType) VALUES (?, 15, -1, 0, ?, 2)");
		    		   stmtInsertOtsenki.setObject(1, rs.getString(1),Types.VARCHAR);
		    		  
		    		   flag = 0;
		    		   if (inosJaz.equals("французский")&&(sdaetInjaz == true)) flag = 1;  
		    		   stmtInsertOtsenki.setObject(2, flag,Types.INTEGER);
		    		   stmtInsertOtsenki.executeUpdate();
		    		   
			    	//  AbiturientBean abit_TMP = new AbiturientBean();
			          //Retrieve by column name
			    	 // String kodabiturienta = rs.getString(1);
			         // String kodpredmeta = rs.getString(2); 
			         // abit_A_S1.add(abit_TMP);
			          //Display values
			        //  System.out.print(kodabiturienta);
			         // System.out.println(kodpredmeta);
//			        //  dt=StringUtil.CurrDate(".");

			       }
			      
	        }
			      
			      
			      /*****************ИМПОРТ НОВЫХ АБИТУРИЕНТоВ СО СТАТУСОМ 0   ------ КОНЕЦ ------ ************************************/
			      
			      /*****************АПДЕЙТ ОБНОВЛЕННЫХ АБИТУРИЕНТОВ СО СТАТУСОМ 3    ************************************/
			      
			      stmt = conn.prepareStatement("select a.kodabiturienta, a.nomerlichnogodela,  a.familija, a.imja, a.otchestvo, a.kodgruppy, lang=case adi.vstup WHEN 'английский' then 'en' else 'ru' end, a.InostrannyjJazyk, a.dokumentyHranjatsja from abiturient a, abitDopInf adi where adi.kodabiturienta = a.kodabiturienta  and a.dataModify = ? and a.dataInput not like a.dataModify");
				  stmt.setObject(1,dt,Types.VARCHAR);
					 rs = stmt.executeQuery();
				      while(rs.next()){
				    	  String dokumentyHranjatsja = rs.getString(9);
				    	  //insert abit info to moodle
				    	  String inosJaz = rs.getString(8);
				    	  boolean sdaetInjaz = false;		
				    	  String lang = rs.getString(7);
				    	 // if (rs.getString(7).equals("английский")) lang = "en";
				    	  
				    	  stmtInsertAbit = MoodleConn.prepareStatement("UPDATE abiturient SET nomerlichnogodela = ?,  familija =?, imja=?, otchestvo=?, status=?, nomerGroup=?, lang=? WHERE kodabiturienta = ?");
				             stmtInsertAbit.setObject(8, rs.getString(1),Types.VARCHAR);
				             stmtInsertAbit.setObject(1, rs.getString(2),Types.VARCHAR);
				             stmtInsertAbit.setObject(2, rs.getString(3),Types.VARCHAR);
				             stmtInsertAbit.setObject(3, rs.getString(4),Types.VARCHAR);
				             stmtInsertAbit.setObject(4, rs.getString(5),Types.VARCHAR);
				             if (dokumentyHranjatsja.equals("н")) stmtInsertAbit.setObject(5, -1,Types.INTEGER);
				             else stmtInsertAbit.setObject(5, 3,Types.INTEGER);
				             
				             stmtInsertAbit.setObject(6, rs.getInt(6),Types.INTEGER);
				             stmtInsertAbit.setObject(7, lang, Types.VARCHAR);
				             stmtInsertAbit.executeUpdate();
				             //end of update
				    	  
				    	  
				    	  stmt1 = conn.prepareStatement("select kodPredmeta, kodabiturienta, ex=case Examen WHEN '+' then 1 else 0 end from zajavlennyeShkolnyeOtsenki where kodabiturienta = ?");
				    	  stmt1.setObject(1,rs.getString(1),Types.VARCHAR);
				    	 	
				    	  
				    	   rs1 = stmt1.executeQuery();
				    	   while(rs1.next()){
				    		   System.out.println("код предмета");
				    		   System.out.println(rs1.getString(1));	
				    		   System.out.println("флаг");
						       System.out.println(rs1.getInt(3));
				    		   
				    		   stmtInsertOtsenki = MoodleConn.prepareStatement("UPDATE otsenki SET flag =?, idType=? WHERE kodabiturienta = ? and kodPredmeta = ?");
				    		   stmtInsertOtsenki.setObject(4, rs1.getString(1),Types.VARCHAR);  //кодпредмета
				    		   stmtInsertOtsenki.setObject(3, rs.getString(1),Types.VARCHAR);	//кодабитуриента
				    		   stmtInsertOtsenki.setObject(2, 2,Types.INTEGER);
				    		   
				    		   stmtInsertOtsenki.setObject(1, rs1.getInt(3),Types.INTEGER);
				    		   if ((rs1.getString(1).equals("5"))&&(rs1.getInt(3) == 1)) 
				    		   {	   
				    			   sdaetInjaz = true;
				    			   if (!inosJaz.equals("английский")&&(sdaetInjaz == true)) {
				    				   stmtInsertOtsenki.setObject(1, 0,Types.INTEGER);
				    			   }
				    		   } 
				    				   
				    		   stmtInsertOtsenki.executeUpdate();
				    		   
				    		  // System.out.print(rs1.getString(1));
				    	   }
				    	   
				    	   //добавляем немецкий
				    	   stmtInsertOtsenki = MoodleConn.prepareStatement("UPDATE otsenki SET flag =?, idType=? WHERE kodabiturienta = ? and kodPredmeta = 14");
			    		   stmtInsertOtsenki.setObject(3, rs.getString(1),Types.VARCHAR);
			    		   stmtInsertOtsenki.setObject(2, 2,Types.INTEGER);
			    		   int flag = 0;
			    		   if (inosJaz.equals("немецкий")&&(sdaetInjaz == true)) flag = 1;  
			    		   stmtInsertOtsenki.setObject(1, flag,Types.INTEGER);
			    		   stmtInsertOtsenki.executeUpdate();
			    		  // добавляем французский 
			    		   stmtInsertOtsenki = MoodleConn.prepareStatement("UPDATE otsenki SET flag =?, idType=? WHERE kodabiturienta = ? and kodPredmeta = 15");
			    		   stmtInsertOtsenki.setObject(3, rs.getString(1),Types.VARCHAR);
			    		   stmtInsertOtsenki.setObject(2, 2,Types.INTEGER);
			    		   flag = 0;
			    		   if (inosJaz.equals("французский")&&(sdaetInjaz == true)) flag = 1;  
			    		   stmtInsertOtsenki.setObject(1, flag,Types.INTEGER);
			    		   stmtInsertOtsenki.executeUpdate();
			    		   
				    

				       }
			      /*****************АПДЕЙТ ОБНОВЛЕННЫХ АБИТУРИЕНТОВ СО СТАТУСОМ 3   ------ КОНЕЦ ------ ************************************/
				      
				      /***************** ЗАГРУЗКА ОЦЕНОК  ************************************/
				      stmt = MoodleConn.prepareStatement("select kodabiturienta, kodpredmeta, ball, id, flag from otsenki where status = '1'");
					  rs = stmt.executeQuery();
				      while(rs.next()){
				    	int kod = rs.getInt(1);  
				    	int kodpredmeta = rs.getInt(2);
				    //	stmtInsertOtsenki =conn.prepareStatement("UPDATE zajavlennyeshkolnyeotsenki SET Examen = ? WHERE kodabiturienta = ? and kodPredmeta = ? and kodPredmeta not IN '14,15'");
				    	if ((kodpredmeta == 5) || (kodpredmeta == 14) || (kodpredmeta == 15)){
				    		if (rs.getInt(5) == 1){
				    		 stmtInsertOtsenki =conn.prepareStatement("UPDATE zajavlennyeshkolnyeotsenki SET Examen = ? WHERE kodabiturienta = ? and kodPredmeta = 5");
				    		// stmtInsertOtsenki.setObject(3, kodpredmeta,Types.INTEGER); //код предмета
				    		 stmtInsertOtsenki.setObject(2, rs.getString(1),Types.VARCHAR); //кодабитуриента
				    		 stmtInsertOtsenki.setObject(1, rs.getInt(3),Types.INTEGER); //балл
				    		 stmtInsertOtsenki.executeUpdate();
				    		 stmt1 = MoodleConn.prepareStatement("update otsenki set status = '2' where id = ?");
					    	 stmt1.setObject(1, rs.getInt(4),Types.INTEGER); //id оценки
					    	 stmt1.executeUpdate();
				    		}
						 //TODO   	
				    	}
				    	else
				    	{
				    	System.out.println(kod);
				    	 stmtInsertOtsenki = conn.prepareStatement("UPDATE zajavlennyeshkolnyeotsenki SET Examen = ? WHERE kodabiturienta = ? and kodPredmeta = ? and kodPredmeta not IN ('5','14','15')");
				    	 stmtInsertOtsenki.setObject(1, rs.getString(3),Types.INTEGER); //балл
				    	 stmtInsertOtsenki.setObject(2, rs.getString(1),Types.VARCHAR); //кодабитуриента
				    	 stmtInsertOtsenki.setObject(3, rs.getString(2),Types.VARCHAR); //кодпредмета
				    	 stmtInsertOtsenki.executeUpdate();
				    /*	 stmt1 = MoodleConn.prepareStatement("update otsenki set status = '2' where id = ?");
				    	 stmt1.setObject(1, rs.getInt(4),Types.INTEGER); //id оценки
				    	 stmt1.executeUpdate();
				    	 */
				    	}
				    	 
				      }
				      
		    		  
				      /*****************ЗАГРУЗКА ОЦЕНОК   ------ КОНЕЦ ------ ************************************/
				
		        // запрос, выполняющий соединение с локальной базой данных
	        	/*stmt = MoodleConn.prepareStatement("select * from asu.predmety");
				 rs = stmt.executeQuery();
			      while(rs.next()){
			    	  AbiturientBean abit_TMP = new AbiturientBean();
			          //Retrieve by column name
			    	  String kodPredmeta = rs.getString(1);
			          String predmet = rs.getString(2);   
			    	  String sokr = rs.getString(3);  			             
			          
			          abit_A_S1.add(abit_TMP);
			          //Display values
			          System.out.print(kodPredmeta);
			          System.out.print(predmet);	
			          System.out.println(sokr);	
				
				
			      } 
			      */
			    System.out.println("Connection is successful!");
			
			      }
	        catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Connection is unsuccessful!");
				return mapping.findForward("error");
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
