package abit.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import abit.util.StringUtil;

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
import org.apache.struts.upload.FormFile;

import abit.bean.AbiturientBean;
import abit.bean.MessageBean;
import abit.bean.UserBean;
import abit.sql.UserConn;


public class FisCheckMarksAction extends Action {

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
		FisConnectForm          form             = (FisConnectForm) actionForm;
		AbiturientBean          abit_F           = form.getBean(request, errors);
		boolean                 error            = false;
		MessageBean             mess             = new MessageBean();
		ActionForward         	f                = null;
		int                   	kFak             = 1;
		InputStream             from_file        = null;
		FileOutputStream        saveFileStream   = null;
		File                    to_file          = null;
		OutputStreamWriter      wr               = null;
		UserBean                user             = (UserBean)session.getAttribute("user");

		if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
			msg = new ActionError( "logon.must" );
			errors.add( "logon.login", msg );
		}

		if ( errors.empty() ) {

			request.setAttribute( "fisCheckMarksAction", new Boolean(true) );
			Locale locale = new Locale("ru","RU");
			session.setAttribute( Action.LOCALE_KEY, locale );

			try {

				/**********************************************************************/
				/*********  Получение соединения с БД и ведение статистики  ***********/

				UserConn us = new UserConn(request, mapping);
				conn = us.getConn(user.getSid());
				request.setAttribute( "fisConnectForm", form );

				/*****************  Возврат к предыдущей странице   *******************/
				if(us.quit("exit")) return mapping.findForward("back");
				/**********************************************************************/

				if ( form.getAction() == null ) {

					form.setAction(us.getClientIntName("new","init"));

				} else if (form.getAction().equals("upload")) {
					/*String input_from_file = new String();*/
					FormFile sourceFile = form.getSourceFile();
					if( sourceFile.getFileSize() == 0 ) {
						mess.setMessage("Файл не найден");
					} else {
						mess.setMessage("Файл найден");
					}

					String csv_row = new String();
					ArrayList abits = new ArrayList();

					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(sourceFile.getInputStream()));
						while((csv_row = br.readLine()) != null) {
							if(!csv_row.equals("")) {
								String[] pieces = csv_row.split("%");
								if(pieces[9].equals("Не найдено")) {
									AbiturientBean abit = new AbiturientBean();
									abit.setImja(pieces[1]);
									abit.setFamilija(pieces[0]);
									abit.setOtchestvo(pieces[2]);
									abits.add(abit);      
								} else if(pieces[9].equals("Действующий")) {
									stmt = conn.prepareStatement("UPDATE o "
											+ "SET o.OtsenkaEge=? "
											+ "FROM ZajavlennyeShkolnyeOtsenki o "
											+ "LEFT OUTER JOIN NazvanijaPredmetov n ON n.KodPredmeta=o.KodPredmeta "
											+ "LEFT OUTER JOIN Abiturient a ON a.KodAbiturienta=o.KodAbiturienta "
											+ "WHERE n.Predmet=? AND a.SeriaDokumenta=? AND a.NomerDokumenta=? AND o.God=?");
									
									stmt.setObject(1, pieces[6], Types.INTEGER);
									stmt.setObject(2, pieces[5], Types.VARCHAR);
									stmt.setObject(3, pieces[3], Types.VARCHAR);
									stmt.setObject(4, pieces[4], Types.VARCHAR);
									stmt.setObject(5, pieces[7], Types.VARCHAR);
									stmt.executeUpdate();
								}
							}

						}

						request.setAttribute("abits", abits);

						br.close();
					} catch(IOException ioe) {

					}

					form.setAction("upload");
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
			request.setAttribute("abit_F", abit_F);

		}
		/*if(f!=null) return f;*/
		if(error) return mapping.findForward("error");
		return mapping.findForward("success");
	}
}