package abit.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
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

import abit.bean.UserBean;
import abit.util.StringUtil;

public class FisInterfaceAction extends Action{
	
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
PreparedStatement       stmt2             = null;
ResultSet               rs2               = null;
PreparedStatement       stmt3             = null;
ResultSet               rs3               = null;
ActionErrors            errors           = new ActionErrors();
ActionError             msg              = null;
FisInterfaceForm          form             = (FisInterfaceForm) actionForm; 
UserBean                user             = (UserBean)session.getAttribute("user");
InputStream             from_file        = null;

FileOutputStream        saveFileStream   = null;
File                    to_file          = null;
OutputStreamWriter      wr               = null;

File                    response_file          = null;


if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
    msg = new ActionError( "logon.must" );
    errors.add( "logon.login", msg );
}

try {

if (form.getAction()!=null){
if (form.getAction().equals("toFIS")){
	String input_from_file = new String();
	FormFile sourceFile = form.getSourceFile();
	 if( sourceFile.getFileSize() == 0 ) {

//       mess.setStatus("Ошибка!");
//       mess.setMessage("Не найден XML-файл запроса!");
//       form.setAction(us.getClientIntName("ansFIS","error"));

// Файл XML-запроса не указан, поэтому вместо него выдаем стандартную пустую форму аутентификации

         if(form.getMethod().equals("/dictionarydetails"))
           input_from_file="<Root>  <GetDictionaryContent>  <DictionaryCode>"+form.getCodeX()+"</DictionaryCode>  </GetDictionaryContent>  <AuthData>    <Login>"+form.getLogin()+"</Login>    <Pass>nanoTubus76</Pass>  </AuthData></Root>";
         else
           input_from_file="<Root>  <AuthData>    <Login>"+form.getLogin()+"</Login>    <Pass>"+form.getPassword()+"</Pass>  </AuthData></Root>";

     } else {

       from_file = sourceFile.getInputStream();
    // Сохраняем файлик в отведенном каталоге

       ServletContext context = session.getServletContext();

       String realContextPath = context.getRealPath(request.getContextPath());        

       String fileSavePath = realContextPath.substring(0,realContextPath.lastIndexOf('\\'))+"\\WEB-INF\\xml\\";

       String fileName = sourceFile.getFileName();

       byte[] fileData = sourceFile.getFileData();

       //to_file = new File(fileSavePath+StringUtil.CurrDate("_")+"_"+StringUtil.CurrTime("_")+"_"+fileName);
       
       to_file = new File("D:/xml/request/"+StringUtil.CurrDate("_")+"_"+StringUtil.CurrTime("_")+"request.xml");

       saveFileStream = new FileOutputStream(to_file);

       byte[] buffer = new byte[4096];

       int bytes_read;

       if(from_file.markSupported()) from_file.mark(0);

       if(to_file.exists()) {

         if(sourceFile.getFileSize() != to_file.length()) {

           if(to_file.canWrite()) {

// Запись в файл

             while ((bytes_read = from_file.read(buffer)) != -1) {

               saveFileStream.write(buffer, 0, bytes_read);

               input_from_file += new String(buffer);
             }
           }
         }
       } else {

// Запись в файл

             while ((bytes_read = from_file.read(buffer)) != -1) {

               saveFileStream.write(buffer, 0, bytes_read);

               input_from_file += new String(buffer);
             }
       }

       if(saveFileStream != null) saveFileStream.close();

       if(from_file.markSupported()) from_file.reset();
     }
	 
	 
	// Устанавливаем соединение с ФИС

     String url = form.getAddress()+form.getMethod();

     HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

     connection.setRequestProperty("Content-Length", ""+input_from_file.getBytes());

     connection.setRequestProperty("Content-Type", "text/xml");

     connection.setRequestMethod("POST");

     connection.setDoOutput(true);

//Устанавливаем время ожидания ответа сервера - TimeOut

     connection.setReadTimeout(form.getTimeOut()*1000);
     
     System.out.print("connect");

     connection.connect();

     InputStream http_error = ((HttpURLConnection) connection).getErrorStream();
     System.out.print(http_error);

//Устанавливаем и отсылаем запрос на сервер

     wr = new OutputStreamWriter(connection.getOutputStream());

     wr.write(input_from_file);

     wr.flush();
     
  // Получаем статус ответа сервера

     int status = connection.getResponseCode();
     
     System.out.print(status);

     String buf_line = new String();

//Читаем ответ сервера в буфер ввода-вывода

    // BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
     
     InputStream is = connection.getInputStream();
     byte[] buffer = new byte[4096];
     int bytesRead;
    
//System.out.println("Read From File");
//     File x_file = new File("c:\\DictionaryCode10.xml");
//     BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(x_file),"UTF-8"));

    /* StringBuilder sb = new StringBuilder();

     while ((buf_line = in.readLine()) != null) {
           sb.append(buf_line);
     }

     buf_line = sb.toString();*/
     
     ServletContext context = session.getServletContext();

     String realContextPath = context.getRealPath(request.getContextPath());        
     String fileSavePath = realContextPath.substring(0,realContextPath.lastIndexOf('\\'))+"\\WEB-INF\\xml\\";

     
    // to_file = new File(fileSavePath+StringUtil.CurrDate("_")+"_"+StringUtil.CurrTime("_")+"_response.xml");
     to_file = new File("D:/xml/response/response"+StringUtil.CurrDate("_")+"_"+StringUtil.CurrTime("_")+".xml");

     saveFileStream = new FileOutputStream(to_file);
     byte[] contentInBytes = buf_line.getBytes();
     
     while ((bytesRead = is.read(buffer)) != -1)
     {
    	 saveFileStream.write(buffer, 0, bytesRead);
     }
    
     
   //  saveFileStream.write(contentInBytes);
     saveFileStream.close();
     
    is.close();
   //  in.close();
System.out.println("Read From File ok");
     connection.disconnect();

	 
       
       
     
	
	
}
}
return mapping.findForward("success");
}


catch ( java.lang.Exception e ) {
  request.setAttribute("JAVAexception", e);
  return mapping.findForward("error");
}


	   

}
	   
}
	   	   
	   
	   
	   




