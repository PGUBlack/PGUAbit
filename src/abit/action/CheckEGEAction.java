package abit.action;
import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*;
import java.lang.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*; 

public class CheckEGEAction  extends Action {

	    public ActionForward perform ( ActionMapping        mapping,
	                                   ActionForm           actionForm,
	                                   HttpServletRequest   request,
	                                   HttpServletResponse  response )

	    throws IOException, ServletException
	    {   
	        HttpSession       session       = request.getSession();
	        Connection        conn          = null;
	        PreparedStatement stmt          = null;
	        ResultSet         rs            = null;
	        ActionErrors      errors        = new ActionErrors();
	        ActionError       msg           = null;
	        CheckEGEForm    form          = (CheckEGEForm) actionForm;
	        AbiturientBean    abit_A        = form.getBean(request, errors);
	        MessageBean       mess          = new MessageBean();
	        boolean           error         = false;
	        boolean           re_enter      = false;
	        ActionForward     f             = null;
	        int               kPunkta       = 1;
	        int               kRajona       = 1;
	        int               kOblasti      = 1;
	        int               kZavedenija   = 1;
	        int               kAbiturienta  = 1;
	        int               Col_Specs     = 0;
	        double 				summa = 0;
	        String            kFormy_Ob     = "1";
	        String            kOsnovy_Ob    = "1";
	        String            kSpec         = "0";
	        String            kFak          = "0";
	int pp=0;
	        String            Abbr_Spec     = new String();
	        String            Abbr_Spec2    = new String();
	        String            Tip_Spec      = new String();
	        String            Tip_Spec2     = new String();
	        String            nld           = new String();
	        String            nomer_ab      = new String();
	        String            ordr_ab       = new String();
	        String            shifr_Fak     = new String();
	        String            two_t_names[] = {"Konkurs","Kontrol_Kon"};
	        String            s_okso_1      = "";
	        String            s_okso_2      = "";
	        String            s_okso_3      = "";
	        String            s_okso_4      = "";
	        String            s_okso_5      = "";
	        String            s_okso_6      = "";
	        ArrayList         abits_A       = new ArrayList();
	        ArrayList         abit_forms    = new ArrayList();
	        ArrayList         abit_osnovs   = new ArrayList();
	        ArrayList         abit_A_S1     = new ArrayList();
	        ArrayList         abit_A_S4     = new ArrayList();
	        ArrayList         abit_A_S5     = new ArrayList();
	        ArrayList         abit_A_S6     = new ArrayList();
	        ArrayList         abit_A_S7     = new ArrayList();
	        ArrayList         abit_A_S8     = new ArrayList();
	        ArrayList         abit_A_S9     = new ArrayList();
	        ArrayList         abit_A_S10     = new ArrayList();
	        ArrayList         abit_A_S11     = new ArrayList();
	        ArrayList		  nationalityList = new ArrayList();
	        UserBean          user          = (UserBean)session.getAttribute("user");

	        if (user==null || user.getGroup()==null || !(user.getGroup().getTypeId()==1 || user.getGroup().getTypeId()==3)) {
	            msg = new ActionError( "logon.must" );
	            errors.add( "logon.login", msg );
	        }

	        if ( errors.empty() ) {

	        request.setAttribute( "checkEGEAction", new Boolean(true) );
	        Locale locale = new Locale("ru","RU");
	        session.setAttribute( Action.LOCALE_KEY, locale );

	        try {

	/**********************************************************************/
	/*********  Получение соединения с БД и ведение статистики  ***********/

	          UserConn us = new UserConn(request, mapping);
	          conn = us.getConn(user.getSid());
	          request.setAttribute( "checkEGEForm", form );

	          /*****************  Возврат к предыдущей странице   *******************/
	          if(us.quit("exit")) return mapping.findForward("back");
	/**********************************************************************/

	/********************** Подготовка данных для ввода с помощью селекторов ************************/

	   if ( form.getAction() == null ) {

	      form.setAction(us.getClientIntName("menu","init"));


	/*************************************************************/
	/*************** СОХРАНЕНИЕ/ОБНОВЛЕНИЕ ОЦЕНОК ****************/
	/*************************************************************/
	} else if( form.getAction().equals("save")) {


	      form.setAction(us.getClientIntName("show","act-save-view"));
	    }
	          
	   if( form.getAction().equals("show")) {
		   String reqXML;
		   URL openURL = null;


		   openURL = new URL("http://10.0.3.1:8080/wschecks.asmx");
           HttpURLConnection con = (HttpURLConnection) openURL.openConnection();
           con.setRequestMethod("POST");
           con.setRequestProperty("Host", "10.0.3.1");
           con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
           con.setRequestProperty("Content-lenght","777");
           con.setRequestProperty("SOAPAction","urn:fbs:v2/GetSingleCheckQuerySample");
           con.setDoOutput(true);
           con.setDoInput(true);
           reqXML="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
		            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+				            	
		            "<soap:Body>"+
		            "<GetSingleCheckQuerySample xmlns=\"urn:fbs:v2\" />"+
		            "</soap:Body>"+
		          "</soap:Envelope>";


        //   DataOutputStream reqStream = new DataOutputStream(con.getOutputStream());
           OutputStream reqStream = con.getOutputStream();
           reqStream.write(reqXML.getBytes());
           reqStream.flush();
           con.connect();
           System.out.println(reqXML);
           StringBuffer data = new StringBuffer();
           // получение ответа сервера
           int responseCode = con.getResponseCode();
           System.out.println(responseCode);
           if (responseCode == 200) { // если все прошло нормально(200), получаем результат
               InputStream in = con.getInputStream();
               InputStreamReader isr = new InputStreamReader(in, "UTF-8");               
               int c;
              while ((c = isr.read()) != -1) {  data.append((char) c);  }
           }
          // String response = IOUtils.toString(con.getInputStream());
           System.out.println(data);
          //InputStream resStream = con.getInputStream();
          // byte[] byteBuf = new byte[10240];
         //  int len = resStream.read(byteBuf);


	
		   //form.setAction(us.getClientIntName("new","init"));

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
	     }
	        request.setAttribute("mess", mess);
	        request.setAttribute("abit_A", abit_A);
	        request.setAttribute("abits_A", abits_A);
	        request.setAttribute("abit_forms", abit_forms);
	        request.setAttribute("abit_osnovs", abit_osnovs);
	        request.setAttribute("abit_A_S1", abit_A_S1);
	        request.setAttribute("abit_A_S4", abit_A_S4);
	        request.setAttribute("abit_A_S5", abit_A_S5);
	        request.setAttribute("abit_A_S6", abit_A_S6);
	        request.setAttribute("abit_A_S7", abit_A_S7);
	        request.setAttribute("abit_A_S8", abit_A_S8);
	        request.setAttribute("abit_A_S9", abit_A_S9);
	        request.setAttribute("abit_A_S10", abit_A_S10);
	        request.setAttribute("abit_A_S11", abit_A_S11);
	        request.setAttribute("nationalityList", nationalityList);

	        if(error) return mapping.findForward("error");

	        return mapping.findForward("success");
	    }


}
