package abit.action;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Date;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.util.StringUtil;
import abit.Constants;
import abit.sql.*; 

public class SqlrepAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session      = request.getSession();
        Connection              conn         = null;
        PreparedStatement       pstmt        = null;
        Statement               stmt         = null; 
        ResultSet               rs           = null;
	ResultSetMetaData       rsmd         = null;
        ActionErrors            errors       = new ActionErrors();
        ArrayList               msgs         = new ArrayList();
        ActionError             msg          = null;
        ReportsBrowserForm      form         = (ReportsBrowserForm) actionForm;
        ReportsBrowserBean      rpt          = form.getBean(request, errors);
        boolean                 twins_f      = false;
        boolean                 error        = false;
        boolean                 no_err       = true;
        ActionForward           f            = null;
        ArrayList               reports      = new ArrayList();
        UserBean                user         = (UserBean)session.getAttribute("user");
        int                     tablesCount  = 20;

// Таблицы БД, которые будут сохраняться или восстанавливаться.

        String base_path = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+"\\web-inf\\db_dumps\\"; 
        String[] database = new String[tablesCount];

                 database[0]="Abiturient";
                 database[1]="Gruppy";
                 database[2]="Zavedenija";
                 database[3]="ZajavlennyeShkolnyeOtsenki";
                 database[4]="Lgoty";
                 database[5]="Kursy";
                 database[6]="Medali";
                 database[7]="NazvanijaPredmetov";
                 database[8]="Oblasti";
                 database[9]="OtvetstvennyeLitsa";
                 database[10]="Otsenki";
                 database[11]="EkzamenyNaSpetsialnosti";
                 database[12]="Punkty";
                 database[13]="Raspisanie";
                 database[14]="Rajony";
                 database[15]="Spetsialnosti";
                 database[16]="Fakultety";
                 database[17]="TselevojPriem";
                 database[18]="IntervalEge";
                 database[19]="KonGruppa";

// В процесс резервного копирования/восстановления не включены таблицы, 
// связанные с управлением доступом к БД и хранением промежуточных результатов
// 
//                 database[20]="ReportsBrowser";
//                 database[21]="host_tbl";
//                 database[22]="group_tbl";
//                 database[23]="access_tbl";

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "sqlrepAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "sqlrepForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          if ( form.getAction() == null || form.getAction().equals("old")) {
                 int number=1;
                 form.setAction(us.getClientIntName("old_rep","init"));
                 pstmt = conn.prepareStatement("SELECT Name,FileName,Date,Time,Author FROM ReportsBrowser WHERE Unvisibility LIKE '1' AND KodVuza LIKE ? ORDER BY 1 ASC");
                 pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 rs = pstmt.executeQuery();
                 while (rs.next()) {
                   ReportsBrowserBean reportTMP = new ReportsBrowserBean();
                   reportTMP.setName(rs.getString(1));
                   reportTMP.setFileName(rs.getString(2));
                   reportTMP.setDate(rs.getString(3));
                   reportTMP.setTime(rs.getString(4));
                   reportTMP.setAuthor(rs.getString(5));
                   reportTMP.setNumber(new Integer(number++));
                   reports.add(reportTMP);
                 }
            } else if(form.getAction().equals("save")) {
                 form.setAction(us.getClientIntName("new_rep","act-save"));

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Резервная копия БД";

    String date = StringUtil.CurrDate(".");

    String fname = StringUtil.CurrDate("_");

    String time = StringUtil.CurrTime(":");

    String file_con = new String(StringUtil.CurrDate("_"));

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"sql"));

    rpt = ((ReportsBrowserBean)session.getAttribute("rpt"));

                 rpt.setSign("Генерация произведена успешно!");
                 rpt.setName("Резервная копия БД");
                 rpt.setFileName(fname);
                 rpt.setDate(date);
                 rpt.setTime(time);
                 rpt.setAuthor(user.getName());

                 if(rpt.getName()!=null) {
                   pstmt = conn.prepareStatement("SELECT * FROM ReportsBrowser WHERE FileName LIKE ? AND KodVuza LIKE ?");
                   pstmt.setObject(1,rpt.getFileName(),Types.VARCHAR);
                   pstmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                   rs = pstmt.executeQuery();
                   if(rs.next()) {
                     pstmt = conn.prepareStatement("DELETE FROM ReportsBrowser WHERE FileName LIKE ? AND KodVuza LIKE ?");
                     pstmt.setObject(1,rpt.getFileName(),Types.VARCHAR);
                     pstmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                     pstmt.executeUpdate();
                   }
// Выбираем макс id для отчета 
                   int k_dok=1;
                   pstmt = conn.prepareStatement("SELECT MAX(id) FROM ReportsBrowser WHERE KodVuza LIKE ?");
                   pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                   rs = pstmt.executeQuery();
                   if(rs.next()) k_dok = rs.getInt(1)+1;

                   pstmt = conn.prepareStatement("INSERT INTO ReportsBrowser(Name,FileName,Date,Time,Author,Unvisibility,KodVuza,id,OwnerId) VALUES(?,?,?,?,?,1,?,?,?)");
                   pstmt.setObject(1,rpt.getName(),Types.VARCHAR);
                   pstmt.setObject(2,rpt.getFileName(),Types.VARCHAR);
                   pstmt.setObject(3,rpt.getDate(),Types.VARCHAR);
                   pstmt.setObject(4,rpt.getTime(),Types.VARCHAR);
                   pstmt.setObject(5,rpt.getAuthor(),Types.VARCHAR);
                   pstmt.setObject(6,""+session.getAttribute("kVuza"),Types.INTEGER);
                   pstmt.setObject(7,""+k_dok,Types.INTEGER);
                   pstmt.setObject(8,user.getUid()+"",Types.INTEGER);
                   pstmt.executeUpdate();
                 }
               
                 String query = new String();
                 int current_table =0;

// --- Создание каталога для хранения экспортированных файлов данных таблиц ---

                    File dir = new File(base_path+rpt.getFileName());
                    File sub_dir = new File(base_path+rpt.getFileName()+"\\backup");
                   try{
                      if(dir.exists()){
                        for(current_table=0;current_table<tablesCount;current_table++){
                           File del_file = new File(base_path+rpt.getFileName()+"\\"+database[current_table]+".sql");
                           del_file.delete();
                        }
                      }
                      else
                           if(!dir.mkdirs()) System.err.println("Couldn't make directory for BACKUP/RESTORE operations");

                      if(sub_dir.exists()){
                           File del_file = new File(base_path+rpt.getFileName()+"\\backup\\abit_db.dat");
                           del_file.delete();
                      }
                      else
                           if(!sub_dir.mkdirs()) System.err.println("Couldn't make SubDirectory for BACKUP/RESTORE operations");
                 
                   } catch(SecurityException exc) {
                       System.err.println("Couldn't make directories for BACKUP/RESTORE operations");
                   }
                   //----------------------------------------------------------
                   // Удаление файлов данных таблиц
                   //----------------------------------------------------------
                     for(current_table=0;current_table<tablesCount;current_table++){
                        File del_file = new File(base_path+rpt.getFileName()+"\\"+database[current_table]+".sql");
                        del_file.delete();
		     }
                        File del_db_file = new File(base_path+rpt.getFileName()+"\\backup\\abit_db.dat");
                        del_db_file.delete();

                   //----------------------------------------------------------
                   // Запись содержимого таблиц в файл
                   //----------------------------------------------------------
                     for(current_table=0;current_table<tablesCount;current_table++){

                   try {
                        String buff = new String();
                        PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter(base_path+rpt.getFileName()+"\\"+database[current_table]+".sql")));
                        query="SELECT * FROM "+database[current_table];
                        pstmt = conn.prepareStatement(query);
                        rs = pstmt.executeQuery();
                        rsmd = rs.getMetaData();
                        while(rs.next()){
// Запись очередной строки таблицы в соответствующий файл
                         buff="";
                         for(int i=1;i<=rsmd.getColumnCount();i++)
                            buff+=StringUtil.null_to_Null(rs.getString(i))+",";
                         out1.println("("+buff.substring(0,buff.length()-1)+")");
                        }
                        out1.close();
                        } catch(EOFException e) {
                          ; // end of the stream
                        }
// Запись дампа всей БД
                        pstmt = conn.prepareStatement("BACKUP DATABASE abit_db TO DISK = '"+base_path+rpt.getFileName()+"\\backup\\abit_db.dat"+"' WITH FORMAT");
                        pstmt.executeUpdate();
                     }
                 MessageBean msg1 = new MessageBean();
                 msg1.setStatus("Внимание!");
                 msg1.setMessage("Резервная копия БД за "+request.getParameter("special2")+" успешно создана.");
                 msgs.add(msg1);

            } 
/****************************** Удаление дампов из БД ****************************/
			else if(form.getAction().equals("delete")){

                 File deldbfile = new File(base_path+request.getParameter("special2")+"\\backup\\abit_db.dat");
                 deldbfile.delete();

                 File deldbfolder = new File(base_path+request.getParameter("special2")+"\\backup");
                 deldbfolder.delete();

                 for(int current_table=0;current_table<tablesCount;current_table++){

                   File del_file = new File(base_path+request.getParameter("special2")+"\\"+database[current_table]+".sql");
                   if(del_file.exists()) {
                     del_file.delete();
                   }
                 }

                 File del_file = new File(base_path+request.getParameter("special2"));
                 if(del_file.exists()) {
                   del_file.delete();
                 }

                 pstmt = conn.prepareStatement("DELETE FROM ReportsBrowser WHERE FileName LIKE ? AND KodVuza LIKE ?");
                 pstmt.setObject(1,request.getParameter("special2"),Types.VARCHAR);
                 pstmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                 pstmt.executeUpdate();

                 int number=1;
                 form.setAction(us.getClientIntName("old_rep","act-del"));

                 pstmt = conn.prepareStatement("SELECT Name,FileName,Date,Time,Author FROM ReportsBrowser WHERE Unvisibility LIKE '1' AND KodVuza LIKE ? ORDER BY 1 ASC");
                 pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 rs = pstmt.executeQuery();
                 while (rs.next()) {
                   ReportsBrowserBean reportTMP = new ReportsBrowserBean();
                   reportTMP.setName(rs.getString(1));
                   reportTMP.setFileName(rs.getString(2));
                   reportTMP.setDate(rs.getString(3));
                   reportTMP.setTime(rs.getString(4));
                   reportTMP.setAuthor(rs.getString(5));
                   reportTMP.setNumber(new Integer(number++));
                   reports.add(reportTMP);
                 }
/****************************** Востановление БД ****************************/
              }	else if(form.getAction().equals("restore")){
                 form.setAction(us.getClientIntName("old_rep","act-repairBD"));
                 int current_table =0;
                   for(current_table=0;current_table<tablesCount;current_table++){
                     File file = new File(base_path+request.getParameter("special2")+"\\"+database[current_table]+".sql");
                     if(file.exists()){
                       BufferedReader input_file = new BufferedReader(new FileReader(file));
                   //----------------------------------------------------------
                   // Чтение строк таблицы из файла и помещение их в таблицу БД
                   //----------------------------------------------------------
                        
// Предварительно удаляем данные в текущей таблице
                       stmt = conn.createStatement();
                       stmt.execute("delete from "+database[current_table]+" where 1>0");
                       try {
                         String buff = new String();
                         while((buff = input_file.readLine())!= null){
// Заносим очередную строку в соответствующую таблицу
                           pstmt = conn.prepareStatement("INSERT INTO "+database[current_table]+" VALUES"+buff);
                           pstmt.executeUpdate();
                         }
////                       file.close();
                       } catch(EOFException e) {
                            ; // end of the stream
                       }
                     } else {
                         no_err = false;
                         MessageBean msg1 = new MessageBean();
                         msg1.setStatus("Ошибка!");
                         msg1.setMessage("Данные таблицы: "+database[current_table]+" не найдены.");
                         msgs.add(msg1);
                     }
                   }
                   if(no_err){
                     MessageBean msg1 = new MessageBean();
                     msg1.setStatus("Внимание!");
                     msg1.setMessage("Состояние БД от "+request.getParameter("special2")+" УСПЕШНО восстановлено.");
                     msgs.add(msg1);
                   } else {
                     MessageBean msg2 = new MessageBean();
                     msg2.setStatus("Внимание!");
                     msg2.setMessage("Состояние БД от "+request.getParameter("special2")+" восстановлено ЧАСТИЧНО.");
                     msgs.add(msg2);
                   }
                 int number=1;
                 pstmt = conn.prepareStatement("SELECT Name,FileName,Date,Time,Author FROM ReportsBrowser WHERE Unvisibility LIKE '1' AND KodVuza LIKE ? ORDER BY 1 ASC");
                 pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 rs = pstmt.executeQuery();
                 while (rs.next()) {
                   ReportsBrowserBean reportTMP = new ReportsBrowserBean();
                   reportTMP.setName(rs.getString(1));
                   reportTMP.setFileName(rs.getString(2));
                   reportTMP.setDate(rs.getString(3));
                   reportTMP.setTime(rs.getString(4));
                   reportTMP.setAuthor(rs.getString(5));
                   reportTMP.setNumber(new Integer(number++));
                   reports.add(reportTMP);
                 }
              }
/*********************************************************************************************/
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
          if ( pstmt != null ) {
               try {
                     pstmt.close();
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
        request.setAttribute("report", rpt);
        request.setAttribute("msgs", msgs);
        request.setAttribute("reports", reports);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}