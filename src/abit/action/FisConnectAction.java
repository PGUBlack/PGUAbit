package abit.action;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

import org.apache.struts.upload.FormFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

// DOM

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringReader;
import org.xml.sax.InputSource;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class FisConnectAction extends Action {


private static String removeNonUtf8CompliantCharacters( final String inString ) {
    if (null == inString ) return null;
    byte[] byteArr = inString.getBytes();
    for ( int i=0; i < byteArr.length; i++ ) {
        byte ch= byteArr[i]; 
        // remove any characters outside the valid UTF-8 range as well as all control characters
        // except tabs and new lines
        if ( !( (ch > 31 && ch < 253 ) || ch == '\t' || ch == '\n' || ch == '\r') ) {
            byteArr[i]=' ';
        }
    }
    return new String( byteArr );
}

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
        boolean                 fis_connect_f    = false;
        boolean                 error            = false;
        MessageBean             mess             = new MessageBean();
        ActionForward           f                = null;
        int                     kFak             = 1;
        ArrayList               abits_F          = new ArrayList();
        InputStream             from_file        = null;
        FileOutputStream        saveFileStream   = null;
        File                    to_file          = null;
        UserBean                user             = (UserBean)session.getAttribute("user");

        OutputStreamWriter      wr               = null;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "fisConnectAction", new Boolean(true) );
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

            } else if ( form.getAction().equals("toFIS") ) {

/*******************************************************************************************/
/**                                Чтение XM-файла запроса                                **/
/*******************************************************************************************/
     String input_from_file = new String();

     FisConnectForm fileForm = (FisConnectForm)actionForm;

     abit_F.setPassword("nanoTubus76");

     FormFile sourceFile = fileForm.getSourceFile();

     if( sourceFile.getFileSize() == 0 ) {

//       mess.setStatus("Ошибка!");
//       mess.setMessage("Не найден XML-файл запроса!");
//       form.setAction(us.getClientIntName("ansFIS","error"));

// Файл XML-запроса не указан, поэтому вместо него выдаем стандартную пустую форму аутентификации

         if(abit_F.getMethod().equals("/dictionarydetails"))
           input_from_file="<Root>  <GetDictionaryContent>  <DictionaryCode>"+abit_F.getCodeX()+"</DictionaryCode>  </GetDictionaryContent>  <AuthData>    <Login>"+abit_F.getUser()+"</Login>    <Pass>"+abit_F.getPassword()+"</Pass>  </AuthData></Root>";
         else
           input_from_file="<Root>  <AuthData>    <Login>"+abit_F.getUser()+"</Login>    <Pass>"+abit_F.getPassword()+"</Pass>  </AuthData></Root>";

     } else {

       from_file = sourceFile.getInputStream();

// Сохраняем файлик в отведенном каталоге

       ServletContext context = session.getServletContext();

       String realContextPath = context.getRealPath(request.getContextPath());        

       String fileSavePath = realContextPath.substring(0,realContextPath.lastIndexOf('\\'))+"\\WEB-INF\\xml\\";

       String fileName = sourceFile.getFileName();

       byte[] fileData = sourceFile.getFileData();

       to_file = new File(fileSavePath+StringUtil.CurrDate("_")+"_"+StringUtil.CurrTime("_")+"_"+fileName);

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

     abit_F.setReqXML(input_from_file);

/************************************************************************************************/
/**                                     Обращение к ФИС                                        **/
/************************************************************************************************/

//
//          URL url = new URL("https://lightning:8443/servlet/EditRequest");
//          HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
//          uc.connect();
//

// Устанавливаем соединение с ФИС

            String url = abit_F.getBaseAddr()+abit_F.getMethod();

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestProperty("Content-Length", ""+abit_F.getReqXML().getBytes());

            connection.setRequestProperty("Content-Type", "text/xml");

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

// Устанавливаем время ожидания ответа сервера - TimeOut

            connection.setReadTimeout(StringUtil.toInt(abit_F.getTimeOut(),1)*1000);

            connection.connect();

//          InputStream http_error = ((HttpURLConnection) connection).getErrorStream();

// Устанавливаем и отсылаем запрос на сервер

            wr = new OutputStreamWriter(connection.getOutputStream());

            wr.write(abit_F.getReqXML());

            wr.flush();

// Получаем статус ответа сервера

            int status = connection.getResponseCode();

            String buf_line = new String();

// Читаем ответ сервера в буфер ввода-вывода

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
//System.out.println("Read From File");
//            File x_file = new File("c:\\DictionaryCode10.xml");
//            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(x_file),"UTF-8"));

            StringBuilder sb = new StringBuilder();

            while ((buf_line = in.readLine()) != null) {
                  sb.append(buf_line);
            }

            buf_line = sb.toString();

            in.close();
System.out.println("Read From File ok");
            connection.disconnect();

// START DOM XML-parsing

            StringBuilder parsed = new StringBuilder();

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new InputSource(new StringReader(buf_line)));

            document.getDocumentElement().normalize();

            boolean xml_file_out = true;

/*
              NodeList children = document.getChildNodes();
              for (int i = 0; i < children.getLength(); i++) {
                  Node node = children.item(i);
                  NamedNodeMap attributes = node.getAttributes();
                  Node nameAttrib = attributes.getNamedItem("name");
                  String name = nameAttrib.getNodeValue();
                  if (node.getNodeType() == Node.ELEMENT_NODE) {
                  }
              }

System.out.println("NodeName="+document.getDocumentElement().getChildNodes().item(0).getNodeName());
System.out.println("NodeType>>"+node.getNodeType());
System.out.println("Attribute1>>"+element.hasAttributes());
System.out.println("Attribute2>>"+element.hasChildNodes());
System.out.println("Attribute3>>"+element);
System.out.println("Attribute4>>"+element.getElementsByTagName(xml_fields_2[i]));
System.out.println("Attribute5>>"+element.getElementsByTagName(xml_fields_2[i]).item(0));
System.out.println("Attribute6>>"+element.getElementsByTagName(xml_fields_2[i]).item(0).getChildNodes());
System.out.println("Attribute7>>"+element.getElementsByTagName(xml_fields_2[i]).item(0).getChildNodes().item(0));
//System.out.println("Attribute8>>"+element.getElementsByTagName(xml_fields_2[i]).item(0).getChildNodes().item(0).getNodeValue());
System.out.println("AttributeX>>"+(element.getElementsByTagName(xml_fields_2[i]).item(0).getChildNodes() == null));
*/

// Корневой элемент XML-документа определяет его содержимое и разбор

/***********************************************************************************/
/**************************** Набор словарей ***************************************/
/***********************************************************************************/
System.out.println("Dicts");
            if( document.getDocumentElement().getNodeName() != null && 
                document.getDocumentElement().getNodeName().equals("Dictionaries")) {

              NodeList nodeList = document.getElementsByTagName(document.getDocumentElement().getChildNodes().item(1).getNodeName());

              xml_file_out = false;

              for(int tmp = 0; tmp < nodeList.getLength(); tmp++)
              {
                  Node node = nodeList.item(tmp);
                  if(node.getNodeType() == Node.ELEMENT_NODE)
                  {
                     Element element = (Element)node;
// Номер элемента
//                   System.out.println("Dictionary #" + tmp + ":");

                     if(element.getElementsByTagName("Code") != null)
                       parsed.append("Code:"+StringUtil.CodeLetter(element.getElementsByTagName("Code").item(0).getChildNodes().item(0).getNodeValue())+"\n");

                     if(element.getElementsByTagName("Name") != null)
                       parsed.append("Name:"+(new String((element.getElementsByTagName("Name").item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+"\n");
                  }
              }
            }

/***********************************************************************************/
/*************************** Информация о ВУЗе *************************************/
/***********************************************************************************/
System.out.println("VuzInfo");
            String xml_fields_2[] = {"FullName","BriefName","FormOfLawID","RegionID","Address","Phone","HasMilitaryDepartment","INN","OGRN","LicenseDate"};

            if( document.getDocumentElement().getNodeName() != null && 
                document.getDocumentElement().getNodeName().equals("InstitutionExport")) {

              xml_file_out = false;

              NodeList nodeList = document.getElementsByTagName(document.getDocumentElement().getChildNodes().item(0).getNodeName());

              for(int tmp = 0; tmp < nodeList.getLength(); tmp++)
              {
                  Node node = nodeList.item(tmp);
                  if(node.getNodeType() == Node.ELEMENT_NODE)
                  {
                     Element element = (Element)node;
                     
                     for(int i=0; i<xml_fields_2.length; i++)
                     {
                       if(element.getElementsByTagName(xml_fields_2[i]) != null &&
                          element.getElementsByTagName(xml_fields_2[i]).item(0).getChildNodes() != null &&
                          element.getElementsByTagName(xml_fields_2[i]).item(0).getChildNodes().item(0) != null)
                         parsed.append(xml_fields_2[i]+":"+(new String((element.getElementsByTagName(xml_fields_2[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+"\n");
                     }
                  }
              }

            }

/***********************************************************************************/
/*******************     Справочники 1,7,8,9,17,21,24,25    ************************/
/***********************************************************************************/

            String xml_fields_3[] = {"ID","Name"};

            if( document.getDocumentElement().getNodeName() != null && 
                document.getDocumentElement().getNodeName().equals("DictionaryData")) {

              if(document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0) != null &&
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue() != null &&
                (document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("1")  ||
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("7")  ||
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("8")  ||
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("9")  ||
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("17") ||
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("21") ||
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("24") ||
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("25"))) 
              {

                xml_file_out = false;

// Нужно зайти в 3-й элемент и открыть список элементов 0-го уровня

                boolean first_elem = true;

                NodeList nodeList = document.getElementsByTagName(document.getDocumentElement().getChildNodes().item(2).getChildNodes().item(0).getNodeName());

                for(int tmp = 0; tmp < nodeList.getLength(); tmp++)
                {
                  Node node = nodeList.item(tmp);
                  if(node.getNodeType() == Node.ELEMENT_NODE)
                  {
                     Element element = (Element)node;

                     for(int i=0; i<xml_fields_3.length; i++)
                     {
                       if(abit_F.getResultPath().equals("forView")) 
                       {
                         if(element.getElementsByTagName(xml_fields_3[i]) != null &&
                            element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes() != null &&
                            element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes().item(0) != null)
                           parsed.append(xml_fields_3[i]+":"+(new String((element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+"\n");
                       } else if(abit_F.getResultPath().equals("forImport")){
                         if(first_elem) {
                           if(element.getElementsByTagName(xml_fields_3[i]) != null &&
                              element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes() != null &&
                              element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes().item(0) != null)
                             parsed.append("INSERT INTO DictionaryCode"+abit_F.getCodeX()+"(ID,Name) VALUES("+StringUtil.prepareForDB(new String((element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+",");
                             first_elem = false;
                         } else {
                           if(element.getElementsByTagName(xml_fields_3[i]) != null &&
                              element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes() != null &&
                              element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes().item(0) != null)
                             parsed.append("'"+StringUtil.prepareForDB(new String((element.getElementsByTagName(xml_fields_3[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+"');\n");
                             first_elem = true;
                         }
                       }
                     }
                  }
                }
              }
            }

/***********************************************************************************/
/***************************   Справочник 10   *************************************/
/***********************************************************************************/
System.out.println("Dictionary # 10");
            String xml_fields_4[] = {"ID","Name","NewCode","QualificationCode","Period","UGSCode","UGSName"};

//            int for_right_save = 1;

            if( document.getDocumentElement().getNodeName() != null && 
                document.getDocumentElement().getNodeName().equals("DictionaryData")) {
System.out.println("DOC"+document.getDocumentElement().getChildNodes().item(0));
              if(document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue() != null &&
                 document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeValue().equals("10")) 
              {

                xml_file_out = false;

// Нужно зайти в 3-й элемент и открыть список элементов 0-го уровня

                NodeList nodeList = document.getElementsByTagName(document.getDocumentElement().getChildNodes().item(2).getChildNodes().item(0).getNodeName());

                for(int tmp = 0; tmp < nodeList.getLength(); tmp++)
                {
                  Node node = nodeList.item(tmp);
                  if(node.getNodeType() == Node.ELEMENT_NODE)
                  {
                     Element element = (Element)node;
System.out.println("NodeN"+tmp);
                     for(int i=0; i<xml_fields_4.length; i++)
                     {
                       if(abit_F.getResultPath().equals("forView")) 
                       {
                         if(element.getElementsByTagName(xml_fields_4[i]) != null &&
                            element.getElementsByTagName(xml_fields_4[i]).item(0) != null &&
                            element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0) != null)
                           parsed.append(xml_fields_4[i]+":"+(new String((element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+"\n");
                         else
                           parsed.append(xml_fields_4[i]+": NULL\n");

                       } else if(abit_F.getResultPath().equals("forImport")){

                         if(i == 0) {
                           if(element.getElementsByTagName(xml_fields_4[i]) != null &&
                              element.getElementsByTagName(xml_fields_4[i]).item(0) != null &&
                              element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0) != null)
                             parsed.append("INSERT INTO DictionaryCode"+abit_F.getCodeX()+"(ID,Name,Code,QualificationCode,Period,UGSCode,UGSName) VALUES("+StringUtil.prepareForDB(new String((element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+",");
                           else
                             parsed.append("NULL,");

                         } else if(i >= 1 && i <= 5) {
                           if(element.getElementsByTagName(xml_fields_4[i]) != null &&
                              element.getElementsByTagName(xml_fields_4[i]).item(0) != null &&
                              element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0) != null)
                             parsed.append("'"+StringUtil.prepareForDB(new String((element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+"',");
                           else
                             parsed.append("NULL,");

                         } else if(i == 6){
                           if(element.getElementsByTagName(xml_fields_4[i]) != null &&
                              element.getElementsByTagName(xml_fields_4[i]).item(0) != null &&
                              element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0) != null)

                             parsed.append("'"+StringUtil.prepareForDB(new String((element.getElementsByTagName(xml_fields_4[i]).item(0).getChildNodes().item(0).getNodeValue()).getBytes(),"Cp1251"))+"');\n");
                           else
                             parsed.append("NULL);\n");
                         }
                       }
                     }
                  }
                }
              }
            }

// Вывод на экран либо результата разбора, либо сам XML-документ

            if(xml_file_out) {

              abit_F.setReqXML(buf_line);

            } else {

              abit_F.setReqXML(parsed.toString());
            }

            form.setAction(us.getClientIntName("ansFIS","FIS_rqst"));

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                      form.setAction(us.getClientIntName("md_dl","view"));
                      stmt = conn.prepareStatement("SELECT DISTINCT Fakultet,AbbreviaturaFakulteta,PlanPriemaFakulteta,ShifrFakulteta,NazvanieVRoditelnom,PoluProhodnoiBallFakulteta,ProhodnoiBallFakulteta,Dekan FROM Fakultet WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      rs = stmt.executeQuery();
                      if ( rs.next() ) {
                         abit_F.setFakultet( rs.getString(1) );
                         abit_F.setAbbreviaturaFakulteta(rs.getString(2));
                         abit_F.setPlanPriemaFakulteta(new Integer(rs.getInt(3)));
                         abit_F.setShifrFakulteta(rs.getString(4));
                         abit_F.setNazvanieVRoditelnom(rs.getString(5));
                         abit_F.setPoluProhodnoiBallFakulteta(new Integer(rs.getInt(6)));
                         abit_F.setProhodnoiBallFakulteta(new Integer(rs.getInt(7)));
                         abit_F.setDekan(rs.getString(8));
                      }

/************************************************************************************************/
/*********************  Если action="change", то изменяем указанную запись  *********************/
/*********** или если передается дополнительный параметр "delete" - удаляем запись **************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                      form.setAction(us.getClientIntName("change","act"));
                      stmt = conn.prepareStatement("UPDATE Fakultet SET Fakultet=?,AbbreviaturaFakulteta=?,PlanPriemaFakulteta=?,ShifrFakulteta=?,NazvanieVRoditelnom=?,PoluProhodnoiBallFakulteta=?,ProhodnoiBallFakulteta=?,Dekan=? WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1,abit_F.getFakultet(),Types.VARCHAR);
                      stmt.setObject(2,abit_F.getAbbreviaturaFakulteta(),Types.VARCHAR);
                      stmt.setObject(3, abit_F.getPlanPriemaFakulteta(),Types.INTEGER);
                      stmt.setObject(4,abit_F.getShifrFakulteta(),Types.VARCHAR);
                      stmt.setObject(5,abit_F.getNazvanieVRoditelnom(),Types.VARCHAR);
                      stmt.setObject(6, abit_F.getPoluProhodnoiBallFakulteta(),Types.INTEGER);
                      stmt.setObject(7, abit_F.getProhodnoiBallFakulteta(),Types.INTEGER);
                      stmt.setObject(8,abit_F.getDekan(),Types.VARCHAR);
                      stmt.setObject(9, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();
                      fis_connect_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                      form.setAction(us.getClientIntName("delete","act"));
// Начало транзакции
                      conn.setAutoCommit(false);

                      stmt = conn.prepareStatement("DELETE FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?)");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();

                      stmt = conn.prepareStatement("DELETE FROM Spetsialnosti WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();

                      stmt = conn.prepareStatement("DELETE FROM Fakultet WHERE KodFakulteta LIKE ?");
                      stmt.setObject(1, abit_F.getKodFakulteta(),Types.INTEGER);
                      stmt.executeUpdate();

// Закрепление транзакции
                      conn.setAutoCommit(true);
                      conn.commit();

                      fis_connect_f  = true;
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
        request.setAttribute("abits_F", abits_F);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(fis_connect_f) return mapping.findForward("fis_connect_f");
        return mapping.findForward("success");
    }
}