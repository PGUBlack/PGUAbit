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

public class ParusExportAction extends Action {
	

	
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
	            
	            /******************èìïîðò ãðóïï*****************************/
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
	            
	            /********************èìïîðò ãðóïï --- ÊÎÍÅÖ -----*****************/
	            /*****************ÈÌÏÎÐÒ ÍÎÂÛÕ ÀÁÈÒÓÐÈÅÍÒÂÎ ÑÎ ÑÒÀÒÓÑÎÌ 0 ************************************/
	            
	            
				 stmt = conn.prepareStatement("select distinct a.kodabiturienta, a.nomerlichnogodela, a.familija, a.imja, a.otchestvo, a.seriadokumenta, a.nomerdokumenta from konkurs k, abiturient a where a.kodabiturienta=k.kodabiturienta and k.dog like 'ä'");
				// stmt.setObject(1,dt,Types.VARCHAR);
				 rs = stmt.executeQuery();
			      while(rs.next()){
			    	  
			    	  stmt2 = MoodleConn.prepareStatement("select kodabiturienta from parus where kodabiturienta = ?");
			    	  stmt2.setObject(1, rs.getString(1),Types.VARCHAR);
			    	  rs2 = stmt2.executeQuery();
			    	  if (!rs2.next()){
			    	  
			    	  stmtInsertAbit = MoodleConn.prepareStatement("INSERT parus(kodabiturienta, NLD,  F, I, O, Seria, Nomer, Flag) VALUES(?,?,?,?,?,?,?,?)");
			             stmtInsertAbit.setObject(1, rs.getInt(1),Types.INTEGER);
			             stmtInsertAbit.setObject(2, rs.getString(2),Types.VARCHAR);
			             stmtInsertAbit.setObject(3, rs.getString(3),Types.VARCHAR);
			             stmtInsertAbit.setObject(4, rs.getString(4),Types.VARCHAR);
			             stmtInsertAbit.setObject(5, rs.getString(5),Types.VARCHAR);
			             stmtInsertAbit.setObject(6, rs.getString(6),Types.VARCHAR);
			             stmtInsertAbit.setObject(7, rs.getString(7),Types.VARCHAR);
			             stmtInsertAbit.setObject(8, "0",Types.INTEGER);
			             stmtInsertAbit.executeUpdate();
			             //end of insert

			    	  }
			      
			      }
			      
			     
			      stmt2 = MoodleConn.prepareStatement("DELETE FROM students_list WHERE 1 ");
		    	  stmt2.executeUpdate();
		    	  
		    	  /*
			      
		    	  stmt2 = conn
							.prepareStatement("update abitdopinf set ballsoch='0' where ballsoch like 'Äà'");
					stmt2.executeUpdate();
					stmt2 = conn
							.prepareStatement("update abitdopinf set ballatt='0' where ballatt like 'Äà'");
					stmt2.executeUpdate();
					stmt2 = conn
							.prepareStatement("update abitdopinf set ballsgto='0' where ballsgto like 'Äà'");
					stmt2.executeUpdate();
					stmt2 = conn
							.prepareStatement("update abitdopinf set ballzgto='0' where ballzgto like 'Äà'");
					stmt2.executeUpdate();
					stmt2 = conn
							.prepareStatement("update abitdopinf set ballpoi='0' where ballpoi like 'Äà'");
					stmt2.executeUpdate();
					stmt2 = conn
							.prepareStatement("update abitdopinf set trudovajadejatelnost='0' where trudovajadejatelnost like 'Äà'");
					stmt2.executeUpdate();
			      
			      */
		    	  
		    	  
			      stmt = conn.prepareStatement("select k.nomerlichnogodela, a.imja, a.otchestvo, a.familija, f.fakultet, f.abbreviaturafakulteta, s.nazvaniespetsialnosti, s.edulevel, a.tipdoksredobraz, case when k.op=7 then 'î' when k.op=3 then 'ñ' when k.op=4 then 'è' when k.target=2 then 'à' when k.target=3 then 'ê' when k.target=4 then 'ò' when k.target=5 then 'ö' when k.target=7 then 'óâö' else 'í' end, case when zso1.otsenkaege=0 then zso1.examen else zso1.otsenkaege end, case when zso2.otsenkaege=0 then zso2.examen else zso2.otsenkaege end, case when zso3.otsenkaege=0 then zso3.examen else zso3.otsenkaege end, k.prof, (case when zso1.otsenkaege=0 then zso1.examen else zso1.otsenkaege end)+ (case when zso2.otsenkaege=0 then zso2.examen else zso2.otsenkaege end) + (case when zso3.otsenkaege=0 then zso3.examen else zso3.otsenkaege end) + k.olimp, case when k.op=3 then 'ñ' when k.op=4 then 'è' when k.op=7 then 'î' when k.op=8 then 'ê' else 'í' end, case when k.pr=0 then 'íåò' else 'äà' end, (cast(adi.ballatt as int) + (cast(adi.ballsgto as int)+adi.ballzgto+adi.ballpoi) + cast (adi.trudovajadejatelnost as int) + cast(k.olimp as int) + cast(adi.ballsoch as int)),adi.ballatt, (cast(adi.ballsgto as int)+adi.ballzgto+adi.ballpoi), adi.trudovajadejatelnost, k.olimp, adi.ballsoch, case when k.sogl like 'ä' then 'äà' else 'íåò' end, k.kodspetsialnosti, case when k.bud like 'ä' then 1 else 0 end, case when k.dog like 'ä' then 1 else 0 end, g.gruppa, os.summ, k.prof  from os os, gruppy g, abiturient a, konkurs k, fakultety f, spetsialnosti s, abitdopinf adi,  ekzamenynaspetsialnosti ens1, ekzamenynaspetsialnosti ens2, ekzamenynaspetsialnosti ens3,  zajavlennyeshkolnyeotsenki zso1, zajavlennyeshkolnyeotsenki zso2, zajavlennyeshkolnyeotsenki zso3 where a.kodabiturienta=k.kodabiturienta and k.kodspetsialnosti=s.kodspetsialnosti and s.kodfakulteta=f.kodfakulteta and a.kodabiturienta=adi.kodabiturienta and s.kodspetsialnosti=ens1.kodspetsialnosti and ens1.prioritet=1 and ens1.kodpredmeta=zso1.kodpredmeta and zso1.kodabiturienta=a.kodabiturienta and s.kodspetsialnosti=ens2.kodspetsialnosti and ens2.prioritet=2 and ens2.kodpredmeta=zso2.kodpredmeta and zso2.kodabiturienta=a.kodabiturienta and s.kodspetsialnosti=ens3.kodspetsialnosti and ens3.prioritet=3 and ens3.kodpredmeta=zso3.kodpredmeta and zso3.kodabiturienta=a.kodabiturienta and a.vprikaze is NULL and a.kodgruppy=g.kodgruppy and a.kodabiturienta=os.kodabiturienta ");
					 rs = stmt.executeQuery();
				      while(rs.next()){

				    	  
				    	  stmtInsertAbit = MoodleConn.prepareStatement("INSERT students_list( case_num,firstname,  secondname, lastname, faculty, faculty_abbr, speciality, edu_level, docs, src, exam_1, exam_2, exam_3, exam_4, sum_exams, rights_special, rights_prerogative, sum_achievments, certificate, sports, volunteer, olymp, writing, agreement, speciality_id, budget, pay,group_title,cert_avg,exam_m) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				    	  		stmtInsertAbit.setObject(1, rs.getString(1),Types.VARCHAR);
				             stmtInsertAbit.setObject(2, rs.getString(2),Types.VARCHAR);
				             stmtInsertAbit.setObject(3, rs.getString(3),Types.VARCHAR);
				             stmtInsertAbit.setObject(4, rs.getString(4),Types.VARCHAR);
				             stmtInsertAbit.setObject(5, rs.getString(5),Types.VARCHAR);
				             stmtInsertAbit.setObject(6, rs.getString(6),Types.VARCHAR);
				             stmtInsertAbit.setObject(7, rs.getString(7),Types.VARCHAR);
				             stmtInsertAbit.setObject(8, rs.getString(8),Types.VARCHAR);
				             stmtInsertAbit.setObject(9, rs.getString(9),Types.VARCHAR);
				             stmtInsertAbit.setObject(10, rs.getString(10),Types.VARCHAR);
				             stmtInsertAbit.setObject(11, rs.getString(11),Types.INTEGER);
				             stmtInsertAbit.setObject(12, rs.getString(12),Types.INTEGER);
				             stmtInsertAbit.setObject(13, rs.getString(13),Types.INTEGER);
				             stmtInsertAbit.setObject(14, rs.getString(14),Types.INTEGER);
				             stmtInsertAbit.setObject(15, rs.getString(15),Types.INTEGER);
				             stmtInsertAbit.setObject(16, rs.getString(16),Types.VARCHAR);
				             stmtInsertAbit.setObject(17, rs.getString(17),Types.VARCHAR);
				             stmtInsertAbit.setObject(18, rs.getString(18),Types.INTEGER);
				             stmtInsertAbit.setObject(19, rs.getString(19),Types.INTEGER);
				             stmtInsertAbit.setObject(20, rs.getString(20),Types.INTEGER);
				             stmtInsertAbit.setObject(21, rs.getString(21),Types.INTEGER);
				             stmtInsertAbit.setObject(22, rs.getString(22),Types.INTEGER);
				             stmtInsertAbit.setObject(23, rs.getString(23),Types.INTEGER);
				             stmtInsertAbit.setObject(24, rs.getString(24),Types.VARCHAR);
				             stmtInsertAbit.setObject(25, rs.getString(25),Types.VARCHAR);
				             stmtInsertAbit.setObject(26, rs.getString(26),Types.VARCHAR);
				             stmtInsertAbit.setObject(27, rs.getString(27),Types.VARCHAR);
				             stmtInsertAbit.setObject(28, rs.getString(28),Types.VARCHAR);
				             if(rs.getFloat(29)>0){
				             stmtInsertAbit.setObject(29, rs.getString(29),Types.FLOAT);
				             }else{
				            	 stmtInsertAbit.setObject(29, 0,Types.FLOAT); 
				             }
				             if(rs.getInt(30)>0){
					             stmtInsertAbit.setObject(30, rs.getString(30),Types.INTEGER);
					             }else{
					            	 stmtInsertAbit.setObject(30, 0,Types.INTEGER); 
					             }
				             stmtInsertAbit.executeUpdate();
				             //end of insert
				      
				      }
			      
		      /*
				      stmt2 = MoodleConn.prepareStatement("DELETE FROM exam_groups WHERE 1 ");
			    	  stmt2.executeUpdate();
			    	  
				      stmt = conn.prepareStatement("select g.gruppa, np.predmet, r.datajekzamena from gruppy g, nazvanijapredmetov np, raspisanie r where g.kodgruppy=r.kodgruppy and r.kodpredmeta=np.kodpredmeta and substring(r.datajekzamena,0,5) like '2016'");
						 rs = stmt.executeQuery();
					      while(rs.next()){
					    	  stmtInsertAbit = MoodleConn.prepareStatement("INSERT exam_groups(group_title, subject, date) VALUES(?,?,?)");
				    	  		stmtInsertAbit.setObject(1, rs.getString(1),Types.VARCHAR);
				             stmtInsertAbit.setObject(2, rs.getString(2),Types.VARCHAR);
				             stmtInsertAbit.setObject(3, rs.getString(3),Types.VARCHAR);
				             stmtInsertAbit.executeUpdate();
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
