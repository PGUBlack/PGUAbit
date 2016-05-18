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
import abit.Constants;
import abit.util.*;
import abit.action.ListsDoneForm;
import java.lang.Object.*;
import abit.sql.*; 

public class ListsDoneAction extends Action {

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
        ResultSet            rs_a               = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsDoneForm        form               = (ListsDoneForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_lists_done_f   = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        ArrayList            notes              = new ArrayList();
        String               note               = new String();
        int                  kAbit              = 0;
        int                  count_predm        = 0;
        int                  number             = 0;
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsDoneAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsDoneForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
        
/************* Подготовка данных для ввода с помощью селекторов ****************/
/*******************************************************************************/
            stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta, ShifrFakulteta FROM fakultety GROUP BY ShifrFakulteta,AbbreviaturaFakulteta ORDER BY ShifrFakulteta ASC");
            rs = stmt.executeQuery();
            while (rs.next()) 
	    {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setShifrFakulteta(rs.getString(2));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
              abit_SD_S1.add(abit_TMP);
            }

            stmt = conn.prepareStatement("SELECT DISTINCT KodGruppy, Gruppa from Gruppy ORDER BY 2 ASC");
            rs = stmt.executeQuery();
            while (rs.next()) 
	    {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial1(rs.getString(1));
              abit_TMP.setGruppa(rs.getString(2));
              abit_SD_S2.add(abit_TMP);
                 }
/************************************************************************************************/

            if ( form.getAction() == null ) {
             
                 form.setAction(us.getClientIntName("view","init"));

 } else if ( form.getAction().equals("report") || (form.getAction().equals("viewing") && StringUtil.toInt(abit_SD.getSpecial1(),0)==-1)) {

/*********************************************************/
/********   Отчет по заданной группе факультета   ********/
/*********************************************************/

if(StringUtil.toInt(abit_SD.getSpecial1(),0)!=-1) {

	number = 0;

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

        String name = "Список группы "+abit_SD.getGruppa().toUpperCase()+" с экз. оценками";

        String file_con = new String("list_ex_ots_"+StringUtil.toEng(abit_SD.getGruppa()));

        session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

        String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

        BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

        report.write("{\\rtf1\\ansi\n");
        stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) 
          report.write("\\fs36\\qc{"+rs.getString(1)+"}\n");

          report.write("\\par\\fs28\\par\\b0\\qc Список абитуриентов группы \\b{"+ abit_SD.getGruppa() +"}\\b0  с экзаменационными оценками\\b1\\n");
          report.write("\\fs14\\par\\par\\fs28");

        stmt = conn.prepareStatement("SELECT COUNT(DISTINCT ens.KodPredmeta) FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Abiturient a WHERE a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta = ens.KodPredmeta AND a.KodGruppy LIKE  ?");
        stmt.setObject(1,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
        rs = stmt.executeQuery();
        while (rs.next()) {
          count_predm = rs.getInt(1);
        }

//******************************************************************************/
// Формирование таблицы отчета

  report.write("\\par\n");
  report.write("\\fs22 \\trowd \\trhdr \\trqc\\trgaph108\\trrh280\\trleft30\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");  // номер 
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1200\n"); //аббр
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n"); // нлд
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n"); //ф
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n"); //и
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n"); //о
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n"); // шм
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+count_predm*800)+"\n");

  report.write("\\intbl\\qc № \\cell\n");
  report.write("\\intbl Абб. спец \\cell\n");
  report.write("\\intbl Номер\n личного дела \\cell\n");
  report.write("\\intbl Фамилия \\cell\n");
  report.write("\\intbl Имя \\cell\n");
  report.write("\\intbl Отчество \\cell\n");
  report.write("\\intbl Шифр мед. \\cell\n");
  report.write("\\intbl Предметы / Оценки \\cell\n");
  report.write("\\intbl \\row\n");

  report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft30\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1200\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n");
  for(int col=1;col<=count_predm;col++)
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+col*800)+"\n");

  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");

  stmt = conn.prepareStatement("SELECT DISTINCT np.Sokr, np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens, Gruppy g,Abiturient a WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND a.KodVuza LIKE ? ORDER BY np.KodPredmeta ASC");
  stmt.setObject(1,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
  stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
    report.write("\\intbl\\fs22{"+rs.getString(1)+"}\\cell\n");
  }
  report.write("\\intbl\\row\\b0\\fs20");

  report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft30\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n");
  for(int col=1;col<=count_predm;col++)
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+col*800)+"\n");

// Выборка данных по абитуриенту для вывода их в отчет
   int old_Ka = -1;

   AbiturientBean abit_TMP = new AbiturientBean();

   stmt = conn.prepareStatement("SELECT a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka FROM Abiturient a,Otsenki o,Spetsialnosti s,Medali m,Gruppy g WHERE a.KodAbiturienta=o.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND o.KodPredmeta IN (SELECT DISTINCT np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens, Gruppy g,Abiturient a WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND a.KodVuza LIKE ?) GROUP BY a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,o.KodPredmeta ORDER BY g.Gruppa,Familija,Imja,Otchestvo,a.KodAbiturienta,o.KodPredmeta ASC");
   stmt.setObject(1,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
   stmt.setObject(2,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
   stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER); 
   rs = stmt.executeQuery();
   while (rs.next()) {

     if(rs.getInt(1) != old_Ka) {

       if(old_Ka != -1) {
         abit_TMP.setNotes(notes);
         report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
         report.write("\\intbl\\qc{"+abit_TMP.getAbbreviatura()+"}\\cell\n");
         report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
         report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
         report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
         report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
         report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
         for(int ots=0;ots<notes.size();ots++) {
          report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
         }
         report.write("\\intbl\\row\n");
       }

       old_Ka = rs.getInt(1);

       notes = new ArrayList();

       abit_TMP = new AbiturientBean();
       abit_TMP.setKodAbiturienta(new Integer(old_Ka));
       abit_TMP.setNumber(Integer.toString(++number));
       abit_TMP.setAbbreviatura(rs.getString(2));
       abit_TMP.setNomerLichnogoDela(rs.getString(4));
       abit_TMP.setFamilija(rs.getString(5));
       abit_TMP.setImja(rs.getString(6));
       abit_TMP.setOtchestvo(rs.getString(7));
       abit_TMP.setShifrMedali(rs.getString(8));

       note = rs.getString(9);

       if(!note.equals("-"))
         notes.add(note);
       else 
         notes.add(Constants.emptyNote);

     } else { 
         note = rs.getString(9);
         if(!note.equals("-")) {
           notes.add(note);
         } 
         else 
           notes.add(Constants.emptyNote);
       }
   }

// Добавление последнего абитуриента в список

   report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
   report.write("\\intbl\\qc{"+abit_TMP.getAbbreviatura()+"}\\cell\n");
   report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
   report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
   report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
   report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
   report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
   for(int ots=0;ots<notes.size();ots++) {
    report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
   }
   report.write("\\intbl\\row\n");
   report.write("}"); 
   report.close();

} else {
/*********************************************************/
/*********   Отчет по всем группам факультета   **********/
/*********************************************************/
String name = new String();
String file_con = new String();
stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE ShifrFakulteta LIKE ? AND KodVuza LIKE ?");
stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) {
  name = "Списки групп "+rs.getString(1).toUpperCase()+" с экз. оценками";
  file_con = new String("lists_ex_ots_"+StringUtil.toEng(rs.getString(1).toUpperCase()));
}

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");

// Выборка всех групп факультета в цикле
stmt = conn.prepareStatement("SELECT DISTINCT g.Gruppa,g.KodGruppy,COUNT(a.KodAbiturienta) FROM Gruppy g,Fakultety f,Abiturient a WHERE a.KodGruppy=g.KodGruppy AND g.KodFakulteta=f.KodFakulteta AND f.ShifrFakulteta LIKE ? AND g.KodGruppy NOT LIKE 1 GROUP BY a.KodGruppy,g.Gruppa,g.KodGruppy ORDER BY g.KodGruppy");
stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
rs_a = stmt.executeQuery();
while(rs_a.next()){

	number = 0;
	int b=0;
        stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) 
          report.write("\\fs36 \\qc "+rs.getString(1)+"\n");
        report.write("\\par\n");
        report.write("\\par\\fs28 \\b0 \\qc Список абитуриентов группы \\b"+ rs_a.getString(1) +" \\b0 с экзаменационными оценками\\b1\\n");
        report.write("\\fs14\\par\\par\\fs28");

//******************************************************************************/
// Формирование таблицы отчета

  stmt = conn.prepareStatement("SELECT COUNT(DISTINCT ens.KodPredmeta) FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Abiturient a WHERE a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta = ens.KodPredmeta AND a.KodGruppy LIKE  ?");
  stmt.setObject(1,rs_a.getString(2),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
     count_predm = rs.getInt(1);
  }

//******************************************************************************/
// Формирование таблицы отчета

  report.write("\\par\n");
  report.write("\\fs22 \\trowd \\trhdr \\trqc\\trgaph108\\trrh280\\trleft30\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");  // номер 
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1200\n"); //аббр
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n"); // нлд
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n"); //ф
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n"); //и
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n"); //о
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n"); // шм
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+count_predm*800)+"\n");

  report.write("\\intbl\\qc № \\cell\n");
  report.write("\\intbl Абб. спец \\cell\n");
  report.write("\\intbl Номер\n личного дела \\cell\n");
  report.write("\\intbl Фамилия \\cell\n");
  report.write("\\intbl Имя \\cell\n");
  report.write("\\intbl Отчество \\cell\n");
  report.write("\\intbl Шифр мед. \\cell\n");
  report.write("\\intbl Предметы / Оценки \\cell\n");
  report.write("\\intbl \\row\n");

  report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft30\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1200\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n");
  for(int col=1;col<=count_predm;col++)
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+col*800)+"\n");

  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");

  stmt = conn.prepareStatement("SELECT DISTINCT np.Sokr, np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens, Gruppy g,Abiturient a WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND a.KodVuza LIKE ? ORDER BY np.KodPredmeta ASC");
  stmt.setObject(1,rs_a.getString(2),Types.INTEGER);
  stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
    report.write("\\intbl\\fs22{"+rs.getString(1)+"}\\cell\n");
  }
  report.write("\\intbl\\row\\b0\\fs20");

  report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft30\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7600\n");
  for(int col=1;col<=count_predm;col++)
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(7600+col*800)+"\n");

// Выборка данных по абитуриенту для вывода их в отчет
   int old_Ka = -1;

   AbiturientBean abit_TMP = new AbiturientBean();

   stmt = conn.prepareStatement("SELECT a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka FROM Abiturient a,Otsenki o,Spetsialnosti s,Medali m,Gruppy g WHERE a.KodAbiturienta=o.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND o.KodPredmeta IN (SELECT DISTINCT np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens, Gruppy g,Abiturient a WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND a.KodVuza LIKE ?) GROUP BY a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,o.KodPredmeta ORDER BY g.Gruppa,Familija,Imja,Otchestvo,a.KodAbiturienta,o.KodPredmeta ASC");
   stmt.setObject(1,rs_a.getString(2),Types.INTEGER);
   stmt.setObject(2,rs_a.getString(2),Types.INTEGER);
   stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER); 
   rs = stmt.executeQuery();
   while (rs.next()) {

     if(rs.getInt(1) != old_Ka) {

       if(old_Ka != -1) {
         abit_TMP.setNotes(notes);
         report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
         report.write("\\intbl\\qc{"+abit_TMP.getAbbreviatura()+"}\\cell\n");
         report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
         report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
         report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
         report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
         report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
         for(int ots=0;ots<notes.size();ots++) {
          report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
         }
         report.write("\\intbl\\row\n");
       }

       old_Ka = rs.getInt(1);

       notes = new ArrayList();

       abit_TMP = new AbiturientBean();
       abit_TMP.setKodAbiturienta(new Integer(old_Ka));
       abit_TMP.setNumber(Integer.toString(++number));
       abit_TMP.setAbbreviatura(rs.getString(2));
       abit_TMP.setNomerLichnogoDela(rs.getString(4));
       abit_TMP.setFamilija(rs.getString(5));
       abit_TMP.setImja(rs.getString(6));
       abit_TMP.setOtchestvo(rs.getString(7));
       abit_TMP.setShifrMedali(rs.getString(8));

       note = rs.getString(9);

       if(!note.equals("-"))
         notes.add(note);
       else 
         notes.add(Constants.emptyNote);

     } else { 
         note = rs.getString(9);
         if(!note.equals("-")) {
           notes.add(note);
         } 
         else 
           notes.add(Constants.emptyNote);
       }
   }

// Добавление последнего абитуриента в список

   report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
   report.write("\\intbl\\qc{"+abit_TMP.getAbbreviatura()+"}\\cell\n");
   report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
   report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
   report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
   report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
   report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
   for(int ots=0;ots<notes.size();ots++) {
    report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
   }
   report.write("\\intbl\\row\n");
    report.write("\\pard\\page");
} //цикл перебора групп факультета

  report.write("}");
  report.close();
}
      form.setAction(us.getClientIntName("new_rep","crt"));
      return mapping.findForward("rep_brw");

/*******************************************************************************/
/********************* Предварительный просмотр данных *************************/
/*******************************************************************************/
            } else if ( form.getAction().equals("viewing") && StringUtil.toInt(abit_SD.getSpecial1(),0)!=-1) {

                 number = 0;
                 int kol_predm = 0;              

                 form.setAction(us.getClientIntName("full","view"));

// Название группы

                stmt = conn.prepareStatement("SELECT Gruppa FROM Gruppy WHERE KodGruppy LIKE ?");
                stmt.setObject(1,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_SD.setGruppa(rs.getString(1));

/*************************** Выборка числа абитуриентов из БД ****************************/

                 stmt = conn.prepareStatement("SELECT DISTINCT np.Sokr,ens.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Abiturient a WHERE a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta = ens.KodPredmeta AND a.KodGruppy LIKE ? ORDER BY ens.KodPredmeta ASC");
                 stmt.setObject(1,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setPredmet(rs.getString(1));
                   abit_SD_S4.add(abit_TMP);
                   kol_predm++;
                 }
                 abit_SD.setPredmCount(""+kol_predm);

// Данные по абитуриенту для вывода на JSP

                 int old_Ka = -1;

                 AbiturientBean abit_TMP = new AbiturientBean();

                 stmt = conn.prepareStatement("SELECT a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka FROM Abiturient a,Otsenki o,Spetsialnosti s,Medali m,Gruppy g WHERE a.KodAbiturienta=o.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND o.KodPredmeta IN (SELECT DISTINCT np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens, Gruppy g,Abiturient a WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND g.KodGruppy LIKE ? AND a.KodVuza LIKE ?) GROUP BY a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,o.KodPredmeta ORDER BY g.Gruppa,Familija,Imja,Otchestvo,a.KodAbiturienta,o.KodPredmeta ASC");
                 stmt.setObject(1,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
                 stmt.setObject(2,new Integer(abit_SD.getSpecial1()),Types.INTEGER);
                 stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER); 
                 rs = stmt.executeQuery();
                 while (rs.next()) {

                   if(rs.getInt(1) != old_Ka) {

                     if(old_Ka != -1) {
                       abit_TMP.setNotes(notes);
                       abits_SD.add(abit_TMP);
                     }

                     old_Ka = rs.getInt(1);

                     notes = new ArrayList();

                     abit_TMP = new AbiturientBean();
                     abit_TMP.setKodAbiturienta(new Integer(old_Ka));
                     abit_TMP.setNumber(Integer.toString(++number));
                     abit_TMP.setAbbreviatura(rs.getString(2));
                     abit_TMP.setNomerLichnogoDela(rs.getString(4));
                     abit_TMP.setFamilija(rs.getString(5));
                     abit_TMP.setImja(rs.getString(6));
                     abit_TMP.setOtchestvo(rs.getString(7));
                     abit_TMP.setShifrMedali(rs.getString(8));

                     note = rs.getString(9);

                     if(!note.equals("-"))
                       notes.add(note);
                     else 
                       notes.add(Constants.emptyNote);

                   } else { 
                       note = rs.getString(9);
                       if(!note.equals("-")) {
                         notes.add(note);
                       } 
                       else 
                         notes.add(Constants.emptyNote);
                     }
                 }

// Добавление последнего абитуриента в список

                 abit_TMP.setNotes(notes);
                 abits_SD.add(abit_TMP);

// Количество абитуриентов в группе

                 abit_SD.setSpecial22(new Integer(number));
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
        if(rep_lists_done_f) return mapping.findForward("rep_lists_done_f");
        return mapping.findForward("success");
    }
}