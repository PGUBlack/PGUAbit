package abit.action;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import java.util.Date;
import java.io.*;
import abit.sql.*; 

public class InjazFakAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    stmt               = null;
        ResultSet            rs                 = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        InjazFakForm         form               = (InjazFakForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_injaz_fak_f  	= false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD          	= new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        int                  solve              = 0;
        String               str1               = new String();
        String               str2               = new String();
        String               str3               = new String();
        String               str4               = new String();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "injazFakAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "injazFakForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

      if ( form.getAction() == null ) form.setAction(us.getClientIntName("view","init"));

      if ( form.getAction().equals("view") ) {

/********************* Подготовка данных для ввода с помощью селекторов ******************/
	stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
	stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
	rs = stmt.executeQuery();
	while (rs.next())
	{
	 AbiturientBean abit_TMP = new AbiturientBean();
	 abit_TMP.setShifrFakulteta(Integer.toString(rs.getInt(1)));
	 abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
	 abit_SD_S1.add(abit_TMP);
	}
	
	stmt = conn.prepareStatement("SELECT DISTINCT s.KodFakulteta, r.DataJekzamena FROM Spetsialnosti s, Abiturient a, Raspisanie r, NazvanijaPredmetov np WHERE a.KodGruppy=r.KodGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND r.KodPredmeta=np.KodPredmeta AND r.KodVuza LIKE ? AND (np.Predmet LIKE '%инос%' OR np.Sokr LIKE '%иняз%') ORDER BY r.DataJekzamena");
        stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
	rs = stmt.executeQuery();
	while (rs.next()) 
	{
	 AbiturientBean abit_TMP = new AbiturientBean();
	 abit_TMP.setSpecial1(Integer.toString(rs.getInt(1)));
	 abit_TMP.setAbbreviatura(StringUtil.data_toApp(rs.getString(2)));
	 abit_SD_S2.add(abit_TMP);
	}
             

/*##################### Генерация отчёта #######################################*/

            } else if ( form.getAction().equals("report")) {

/*****  Если action равен otchet , то входим в секцию - создание отчёта  *****/

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

        String name = "Распределение по ин. яз.";

        String file_con = new String("injaz_fak");

	stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta = ?");
	stmt.setObject(1,new Integer(abit_SD.getPriznakSortirovki().substring(10)),Types.INTEGER);
	rs = stmt.executeQuery();
	if(rs.next()) file_con = file_con+"_"+StringUtil.toEng(rs.getString(1));

        session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

        String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

        BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

        report.write("{\\rtf1\\ansi\n");
        stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) 
          report.write("\\fs40 \\qc "+rs.getString(1)+"\n");
        report.write("\\par\\par\n");

	stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ?");
	stmt.setObject(1,new Integer(abit_SD.getPriznakSortirovki().substring(10)),Types.INTEGER);
	rs = stmt.executeQuery();
	if(rs.next())
	{
          report.write("\\fs28 \\b0 \\qc Распределение абитуриентов по иностранному языку для факультета: "+rs.getString(1).toUpperCase()+"\n");
	}

   report.write("\\fs28 \\line \\b0 \\qc Дата проведения экзамена: "+abit_SD.getPriznakSortirovki().substring(0,10)+"\n");
   report.write("\\par\n");
   report.write("\\par\n");
   report.write("\\fs24 \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr\n");
   report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
   report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
   report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
   report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
   report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");
   report.write("\\intbl\\b1 Группа \\cell\n");
   report.write("\\intbl Английский \\cell\n");
   report.write("\\intbl Немецкий \\cell\n");
   report.write("\\intbl Французский \\cell\n");
   report.write("\\intbl Прочие \\b0\\cell\n");
   report.write("\\intbl \\row\n");

	String Gruppa = new String();	
	int English = 0;	
	int German  = 0;	
	int French  = 0;	
	int Another = 0;	
	int En = 0;	
	int Ge = 0;	// для вывода
	int Fr = 0;	// вывода
	int Eno= 0;	// "Итого"

//Из базы данных считываются группы и сдаваемый ин.яз. для выбранного факультета и даты экзамена

	stmt = conn.prepareStatement("SELECT a.InostrannyjJazyk, g.Gruppa FROM Spetsialnosti s, Abiturient a, Raspisanie r, Gruppy g WHERE a.KodGruppy=r.KodGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodFakulteta LIKE ? AND r.DataJekzamena LIKE ? AND g.KodGruppy = r.KodGruppy AND r.KodVuza LIKE ? ORDER BY g.Gruppa");
	stmt.setObject(1,new Integer(abit_SD.getPriznakSortirovki().substring(10)),Types.INTEGER);
	stmt.setObject(2,StringUtil.data_toDB(abit_SD.getPriznakSortirovki().substring(0,10)),Types.VARCHAR);
        stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
	rs = stmt.executeQuery();
	if(rs.next())
	{
	 Gruppa = rs.getString(2);
	 if(rs.getString(1).equals("а"))       {English++; En++;}
	  else if(rs.getString(1).equals("н")) {German++;  Ge++;}
	  else if(rs.getString(1).equals("ф")) {French++;  Fr++;}
	  else {Another++; Eno++;}
	}
	while (rs.next())
	{
	 if(Gruppa.equals(rs.getString(2))) //Если группа не изменилась то:
	 {
	  if(rs.getString(1).equals("а")) 	{English++; En++;}
	   else if(rs.getString(1).equals("н")) {German++;  Ge++;}
	   else if(rs.getString(1).equals("ф")) {French++;  Fr++;}
	   else {Another++; Eno++;}
	 }
	  else	// Если группа изменилась то:
	  {

//Вывод
   report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");
   report.write("\\intbl "+Gruppa+"\\cell\n");
   report.write("\\intbl "+Integer.toString(English)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(German)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(French)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(Another)+"\\cell\n");
   report.write("\\intbl \\row\n");
	   Gruppa = rs.getString(2);
	   English = 0;
	   German = 0;
	   French = 0;
	   Another = 0;
	   if(rs.getString(1).equals("а"))       {English++; En++;}
	    else if(rs.getString(1).equals("н")) {German++;  Ge++;}
	    else if(rs.getString(1).equals("ф")) {French++;  Fr++;}
	    else {Another++; Eno++;}
	  }
	}
   report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");
   report.write("\\intbl "+Gruppa+"\\cell\n");
   report.write("\\intbl "+Integer.toString(English)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(German)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(French)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(Another)+"\\cell\n");
   report.write("\\intbl \\row\n");
	Gruppa = "Итого:";
   report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8500\n");
   report.write("\\intbl "+Gruppa+"\\cell\n");
   report.write("\\intbl "+Integer.toString(En)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(Ge)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(Fr)+"\\cell\n");
   report.write("\\intbl "+Integer.toString(Eno)+"\\cell\n");
   report.write("\\intbl \\row\n");
   report.write("}"); 
   report.close();
   form.setAction(us.getClientIntName("new_rep","crt"));
   return mapping.findForward("rep_brw");

 } else if ( form.getAction().equals("viewing")) {

/************************************************************************************************/
/*****  Если action равен viewing , то входим в секцию - создание записи или вывод таблицы  *****/
/************************************  Просмотр/Модификация **************************************/
	form.setAction(us.getClientIntName("full","view"));

	abit_SD.setPriznakSortirovki(abit_SD.getSpecial1());	//Сохраняем данные
								//для передачи в
								//action == "report" 
/*************************** Выборка числа абитуриентов из БД ****************************/
	stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta = ?");
	stmt.setObject(1,abit_SD.getSpecial1().substring(10),Types.VARCHAR);
	rs = stmt.executeQuery();
	if(rs.next())
	{
	 abit_SD.setSpecial6(rs.getString(1).toUpperCase());
	}
	abit_SD.setSpecial7(abit_SD.getSpecial1().substring(0,10));
	
	String Gruppa = new String();	//Название группы
	int English = 0;	
	int German  = 0;	
	int French  = 0;	
	int Another = 0;	
	int En = 0;	//Итого
	int Ge = 0;	//по
	int Fr = 0;	//всем
	int Eno= 0;	//группам
	
//Из базы данных считываем группы и сдаваемый ин.яз. для выбранного факультета и даты экзамена
	stmt = conn.prepareStatement("SELECT a.InostrannyjJazyk, g.Gruppa FROM Spetsialnosti s, Abiturient a, Raspisanie r, Gruppy g WHERE a.KodGruppy=r.KodGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodFakulteta LIKE ? AND r.DataJekzamena LIKE ? AND g.KodGruppy = r.KodGruppy AND r.KodVuza LIKE ? ORDER BY g.Gruppa");
	stmt.setObject(1,new Integer(abit_SD.getSpecial1().substring(10)),Types.INTEGER);
	stmt.setObject(2,StringUtil.data_toDB(abit_SD.getPriznakSortirovki().substring(0,10)),Types.VARCHAR);
        stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
	rs = stmt.executeQuery();
	if(rs.next())
	{		
	 Gruppa = rs.getString(2);
	 if(rs.getString(1).equals("а"))       {English++; En++;}
	  else if(rs.getString(1).equals("н")) {German++;  Ge++;}
	  else if(rs.getString(1).equals("ф")) {French++;  Fr++;}
	  else                                 {Another++; Eno++;}
	}
	while (rs.next())
	{
	 if(Gruppa.equals(rs.getString(2)))	
	 {
	  if(rs.getString(1).equals("а"))       {English++; En++;}
	   else if(rs.getString(1).equals("н")) {German++;  Ge++;}
	   else if(rs.getString(1).equals("ф")) {French++;  Fr++;}
	   else {Another++; Eno++;}
	 }
	  else
	  {
	   AbiturientBean abit_TMP = new AbiturientBean();
	   abit_TMP.setSpecial1(Gruppa);
	   abit_TMP.setSpecial2(Integer.toString(English));
	   abit_TMP.setSpecial3(Integer.toString(German));
	   abit_TMP.setSpecial4(Integer.toString(French));
	   abit_TMP.setSpecial5(Integer.toString(Another));
	   abits_SD.add(abit_TMP);

	   Gruppa = rs.getString(2);
	   English = 0;
	   German = 0;
	   French = 0;
	   Another = 0;
	   if(rs.getString(1).equals("а"))       {English++; En++;}
	    else if(rs.getString(1).equals("н")) {German++;  Ge++;}
	    else if(rs.getString(1).equals("ф")) {French++;  Fr++;}
	    else {Another++; Eno++;}
	  }
	}
	AbiturientBean abit_TMP = new AbiturientBean();	
	abit_TMP.setSpecial1(Gruppa);
	abit_TMP.setSpecial2(Integer.toString(English));
	abit_TMP.setSpecial3(Integer.toString(German));
	abit_TMP.setSpecial4(Integer.toString(French));
	abit_TMP.setSpecial5(Integer.toString(Another));
	abits_SD.add(abit_TMP);
	Gruppa = "Итого:";
	AbiturientBean abitt_TMP = new AbiturientBean();	
	abitt_TMP.setSpecial1(Gruppa);
	abitt_TMP.setSpecial2(Integer.toString(En));
	abitt_TMP.setSpecial3(Integer.toString(Ge));
	abitt_TMP.setSpecial4(Integer.toString(Fr));
	abitt_TMP.setSpecial5(Integer.toString(Eno));
	abits_SD.add(abitt_TMP);
/********************************************************************************************/

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
        request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S4", abit_SD_S4);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(rep_injaz_fak_f) return mapping.findForward("rep_injaz_fak_f");
        return mapping.findForward("success");
    }
}
