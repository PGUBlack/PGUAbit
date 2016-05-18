

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


public class ZachZelevikovAction extends Action {

	 public ActionForward perform ( ActionMapping        mapping,
             ActionForm           actionForm,
             HttpServletRequest   request,
             HttpServletResponse  response )
	 
	 throws IOException, ServletException
	    {   
	        HttpSession       session       = request.getSession();
	        Connection        conn          = null;
	        PreparedStatement stmt          = null;
	        PreparedStatement stmt1         = null;
	        Statement            stmt2              = null;
	        Statement            stmt3              = null;
	        Statement 			stmt4         				= null;
	        Statement            stmt5              = null;
	        ResultSet         rs            = null;
	        ResultSet         rs1           = null;
	        ResultSet         rs3           = null;
	        ResultSet         rs4           = null;
	        ResultSet         rs5           = null;
	        UserBean          user          = (UserBean)session.getAttribute("user");
	        ActionErrors      errors        = new ActionErrors();
	        ActionError       msg           = null;
	        PreparedStatement    pstmt              = null;
	        PreparedStatement    pstmt1              = null;
	        PreparedStatement    pstmt2             = null;
	        ArrayList            abit_SD_S1         = new ArrayList();
	      //  ListsDecForm         form               = (ListsDecForm) actionForm;
	      //  AbiturientBean       abit_SD            = form.getBean(request, errors);
	        String               AF                 = new String();           // аббревиатура факультета
	        StringBuffer         query              = new StringBuffer();
	       
	        
	        ResultSet            rs2                = null;
	        
	        
	        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
	            msg = new ActionError( "logon.must" );
	            errors.add( "logon.login", msg );
	           	}
	    //    request.setAttribute( "zachZelevikovAction", new Boolean(true) );
	        Locale locale = new Locale("ru","RU");
	        session.setAttribute( Action.LOCALE_KEY, locale );
	        try {
	                  UserConn us = new UserConn(request, mapping);
	                  conn = us.getConn(user.getSid()); 
	                  pstmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
	                  pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
	                  rs = pstmt.executeQuery(); //выборка факультетов
	                  while (rs.next()) { //факультеты
	                   // AbiturientBean abit_TMP = new AbiturientBean();
	                	
	                	  
	                	  /* if ( stmt2 != null ) {
        	                       stmt2.close();
	                	   } */
	                	  
	                    stmt2 = conn.createStatement();  //stmt специальностей
	                    query = new StringBuffer("SELECT DISTINCT f.AbbreviaturaFakulteta,s.ShifrSpetsialnosti,s.Abbreviatura,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,s.PlanPriema,s.TselPr_PGU,s.TselPr_ROS FROM Fakultety f,Spetsialnosti s, Konkurs kon, Abiturient a,Forma_Obuch fo WHERE f.KodFakulteta=s.KodFakulteta AND fo.KodFormyOb=a.KodFormyOb AND a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE '"+rs.getString(1)+"' ");
	                    query.append("AND (kon.Bud LIKE 'д')");
	                    rs2 = stmt2.executeQuery(query.toString()); //rs2 выборка специальностей
	                    while(rs2.next()) { //специальности
	                    	String KodSpec = rs2.getString(5);
	                        int total_amount = 0, tselev_nomer = 0; //число целевиков 
	                      
	                        if ( stmt3 != null ) {
	       	                 	                       stmt3.close();
	       	                 } 
	                        
	                        stmt3 = conn.createStatement();  //stmt числа целевиков
	                        rs3 = stmt3.executeQuery("SELECT TselPr_PGU FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+KodSpec+"'");
	                        if( rs3.next() || StringUtil.toInt(rs3.getString(1),0) != 0 ) { //rs3 число целевиков
	                        	  total_amount = StringUtil.toInt(rs3.getString(1),0); //число целевиков
	                        	  query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND tp.ShifrPriema IN ('ц','ф') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' ");
	                        	  query.append("AND a.TipDokSredObraz LIKE ('о') ");
	                        	  query.append("AND (kon.Bud LIKE 'д')");
	                              query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY tp.ShifrPriema DESC,SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

	                              if ( stmt4 != null ) {
               	                       stmt4.close();
	                              	} 
	                              
	                              
	                              stmt4 = conn.createStatement(); //stmt4 выборка абитуреиентов
	                              rs4 = stmt4.executeQuery(query.toString());
	                              while(rs4.next()) {

	                                if((++tselev_nomer) <= total_amount)
	                                {
	                                	stmt5 = conn.createStatement();
	                                    stmt5.executeUpdate("UPDATE Abiturient SET Prinjat='1' WHERE KodAbiturienta = '"+ rs4.getString(1) +"'");
	                                	System.out.println("зачислен " + rs4.getString(1));
	                                	
	                                }  // rs 5 зачисление 
	                              } //rs4  абитуриенты
	                        
	                        
	                        
	                        
	                        
	                        }  //    if( rs3.next()  целевой номер 
	                        
	                    	
	                    	
	                    } //rs2 специальности
	                   // abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
	                  //  abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
	                   // abit_SD_S1.add(abit_TMP);
	                  }  //rs факультеты
	                  
	           /*   
	                //  stmt2 = conn.createStatement();
	                  if ( form.getAction() == null ) {

	                      form.setAction(us.getClientIntName("view","init"));
	                  }
	               //   query = new StringBuffer("SELECT DISTINCT f.AbbreviaturaFakulteta,s.ShifrSpetsialnosti,s.Abbreviatura,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,s.PlanPriema,s.TselPr_PGU,s.TselPr_ROS FROM Fakultety f,Spetsialnosti s, Konkurs kon, Abiturient a,Forma_Obuch fo WHERE f.KodFakulteta=s.KodFakulteta AND fo.KodFormyOb=a.KodFormyOb AND a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE '"+abit_SD.getKodFakulteta()+"' ");
	                      else if ( form.getAction().equals("zach")) {
	                	  
	                	  
	                  
	                  				pstmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ?");
	                  				pstmt.setObject(1,abit_SD.getKodFakulteta(),Types.INTEGER);
	                  				rs = pstmt.executeQuery();
	                  				if(rs.next()) {
	                  									AF = rs.getString(1).toUpperCase();
	                  								}
	                  
	                  
	                  					stmt = conn.prepareStatement("UPDATE Abiturient SET Prinjat='1' WHERE KodLgot IN ('2', '3', '4', '5')");
	                  					rs = stmt.executeQuery();
	                  					rs.close();
	                  					stmt = null;
	                  					conn = null;
	                  					
	                  			        
	                  			      
	                  			        return mapping.findForward("success");
	                  
	                  	}     */
	                  	
	                  
	             //     SELECT TselPr_PGU FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '50'    //количество целевиков
	                  
	                  
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
	          }  //finally
	        
	     //   request.setAttribute("abit_SD_S1", abit_SD_S1);            
		      
	        return mapping.findForward("success");
	    } //throws exception
	        
	        
	 
	 
	
} //class
