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

public class ListsGroupsAction extends Action {

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
        ListsGroupsForm      form               = (ListsGroupsForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_Groups_f     = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        ArrayList            notes              = new ArrayList();
        int                  count_predm        = 0;
        String               str1               = new String();
        String               str2               = new String();
        String               str3               = new String();
        String               str4               = new String();
        UserBean             user               = (UserBean)session.getAttribute("user");
        String               name               = new String();
        String               file_con           = new String();


        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsGroupsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsGroupsForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************* Подготовка данных для ввода с помощью селекторов ****************/
/*******************************************************************************/
            stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta, ShifrFakulteta FROM fakultety GROUP BY ShifrFakulteta,AbbreviaturaFakulteta ORDER BY 1,2 ASC");
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
              abit_TMP.setSpecial2(rs.getString(2));
              abit_SD_S2.add(abit_TMP);
                 }
/************************************************************************************************/
            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

/************************** Генерация отчета ***********************************/

            } else if ( form.getAction().equals("report")) {

/***  Если action равен otchet , то входим в секцию - создание отчёта  ***/
/************************************ OTCHET ***************************************/
        if(!abit_SD.getSpecial1().equals("*"))
          abit_SD.setKodSpetsialnosti(new Integer(abit_SD.getSpecial1()));
	abit_SD.setBud_Kon(abit_SD.getPriznakSortirovki());

	int tipotch = 0;                //тип создаваемого отчета
	int kol = 0;                    //количество абитуриентов в таблице

/**************** задаём имя файла соответствующее признаку и тип отчета********/
	if(abit_SD.getBud_Kon().equals("sotsenkoi"))
	{
	 abit_SD.setPriznakSortirovki("sotsenkoi");
	 tipotch = 1;
         if(abit_SD.getSpecial1().equals("*"))
	 tipotch = 11;
	}

	if(abit_SD.getBud_Kon().equals("bezotsenki"))
	{
	 abit_SD.setPriznakSortirovki("bezotsenki");
	 tipotch = 2;
         if(abit_SD.getSpecial1().equals("*"))
	 tipotch = 22;
	}

String Gru = new String();
String KPred[] = new String[10];

/**************************************************************/
/***********  тип отчета = 1 - группа с оценками   ************/
/**************************************************************/
if(tipotch == 1){
  stmt = conn.prepareStatement("SELECT Gruppa FROM Gruppy WHERE KodGruppy LIKE ?");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();

if(rs.next())  Gru=rs.getString(1);

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    name = "Список группы "+Gru+" со школьн. оценками";

    file_con = new String("list_gr_"+StringUtil.toEng(Gru)+"_ots");

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\fs32 \\qc Список абитуриентов группы\\b1{ "+Gru+" }\\b0 со школьными оценками\n");
  report.write("\\par\\par\n");

  stmt = conn.prepareStatement("SELECT COUNT(DISTINCT NazvanijaPredmetov.KodPredmeta) FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti,Abiturient WHERE Abiturient.KodSpetsialnosti=EkzamenyNaSpetsialnosti.KodSpetsialnosti AND NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodGruppy LIKE ?");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
    count_predm = rs.getInt(1);
  }

  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("\\trowd\\trqc \\fs20 \\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3400\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4700\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6200\n");
 // report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6900\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8600\n");
  for(int col=1;col<=count_predm;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx"+(8600+col*3*620)+"\n");

  report.write("\\intbl\\b №\\cell\n");
  report.write("\\intbl Номер дела\\cell\n");
  report.write("\\intbl Фамилия\\cell\n");
  report.write("\\intbl Имя\\cell\n");
  report.write("\\intbl Отчество\\cell\n");
//  report.write("\\intbl Отл.\\cell\n");
  report.write("\\intbl № договора\\cell\n");

  stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens,Abiturient a WHERE a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta=ens.KodPredmeta AND a.KodGruppy LIKE ? ORDER BY np.KodPredmeta ASC");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()) { 
    report.write("\\intbl{"+rs.getString(1)+"}\\cell\n");
  }
  report.write("\\intbl\\b0\\row\n");

  report.write("\\trowd\\trqc \\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx1800\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx3400\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx4700\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx6200\n");
 // report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx6900\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8600\n");
  for(int col=1;col<=count_predm*3;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx"+(8600+col*620)+"\n");

  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
 // report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");

  for(int col=1;col<=count_predm;col++) {
     report.write("\\intbl \\fs16 \\b атт.\\cell\n");
     report.write("\\intbl ЕГЭ\\cell\n");
     report.write("\\intbl заявл.\\cell\n");
  }
  report.write("\\intbl \\row\n");

  report.write("\\b0\\trowd\\trqc \\fs20 \\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6200\n");
 // report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8600\n");
  for(int col=1;col<=count_predm*3;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx"+(8600+col*620)+"\n");

  int number = 0;
  int old_Ka = -1;
  String note = new String();
  AbiturientBean abit_TMP = new AbiturientBean();

  stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaAtt,zo.OtsenkaEge,zo.OtsenkaZajavl,NomerPlatnogoDogovora FROM NazvanijaPredmetov np, ZajavlennyeShkolnyeOtsenki zo, Abiturient a WHERE np.KodPredmeta=zo.KodPredmeta AND a.KodAbiturienta = zo.KodAbiturienta AND a.KodGruppy LIKE ? AND zo.KodPredmeta IN (SELECT DISTINCT np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens,Abiturient a WHERE a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta=ens.KodPredmeta AND a.KodGruppy LIKE ?) GROUP BY a.KodAbiturienta,np.KodPredmeta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaAtt,zo.OtsenkaEge,zo.OtsenkaZajavl,NomerPlatnogoDogovora ORDER BY Familija,Imja,Otchestvo,np.KodPredmeta ASC");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  stmt.setObject(2,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next())
  {
        if(rs.getInt(1) != old_Ka) {

          if(old_Ka != -1) {
            report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
            report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
            report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
            report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
            report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
            report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
            report.write("\\intbl\\ql{" +StringUtil.ntv(abit_TMP.getNomerPlatnogoDogovora())+"}\\cell\n");
            for(int ots=0;ots<notes.size();ots++) {
             report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
            }
            report.write("\\intbl\\row\n");
          }

          old_Ka = rs.getInt(1);
          notes = new ArrayList();

          abit_TMP = new AbiturientBean();
          abit_TMP.setNumber(Integer.toString(++number));
          abit_TMP.setKodAbiturienta(new Integer(old_Ka));
          abit_TMP.setFamilija(rs.getString(2));
          abit_TMP.setImja(rs.getString(3));
          abit_TMP.setOtchestvo(rs.getString(4));
          abit_TMP.setNomerLichnogoDela(rs.getString(5));
          abit_TMP.setNomerPlatnogoDogovora(rs.getString(9));
       //   abit_TMP.setShifrMedali(rs.getString(10));

// Оц. Атт. / ЕГЭ / Заявл.

          for(int i=6;i<=8;i++){
            note = rs.getString(i);

            if(!note.equals("-"))
              notes.add(note);
            else 
              notes.add(Constants.emptyNote);
          }
        } else { 
            for(int i=6;i<=8;i++){
              note = rs.getString(i);
              if(!note.equals("-")) {
                notes.add(note);
              } 
              else 
                notes.add(Constants.emptyNote);
            }
          }
  }
// Добавление последнего абитуриента в список

        report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
        report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
        report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
        report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
        report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
      //  report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
        report.write("\\intbl\\ql{" +StringUtil.ntv(abit_TMP.getNomerPlatnogoDogovora())+"}\\cell\n");
        for(int ots=0;ots<notes.size();ots++) {
         report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
        }
        report.write("\\intbl\\row\n");

        report.write("}");
        report.close();
}

/**************************************************************/
/***  тип отчета = 11 - список групп факультета с оценками ***/
/**************************************************************/

if(tipotch == 11){
stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE ShifrFakulteta LIKE ? AND KodVuza LIKE ?");
stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) {
  name = "Списки групп "+rs.getString(1).toUpperCase()+" со школьн. оценками";
  file_con = "lists_grps_"+StringUtil.toEng(rs.getString(1).toUpperCase())+"_ots";
}

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

// Выборка всех групп факультета в цикле
  stmt = conn.prepareStatement("SELECT DISTINCT g.Gruppa,g.KodGruppy FROM Gruppy g,Fakultety f,Abiturient a WHERE a.KodGruppy=g.KodGruppy AND f.KodFakulteta=g.KodFakulteta AND f.ShifrFakulteta LIKE ? AND g.KodGruppy NOT LIKE 1 ORDER BY g.KodGruppy");
  stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
  rs_a = stmt.executeQuery();

while(rs_a.next()){
  Gru=rs_a.getString(1);
  abit_SD.setKodSpetsialnosti(new Integer(rs_a.getString(2)));

  report.write("\\par\n");
  report.write("\\fs32 \\qc Список абитуриентов группы\\b1{ "+Gru+" }\\b0 со школьными оценками\n");
  report.write("\\par\\par\n");

  stmt = conn.prepareStatement("SELECT COUNT(DISTINCT NazvanijaPredmetov.KodPredmeta) FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti,Abiturient WHERE Abiturient.KodSpetsialnosti=EkzamenyNaSpetsialnosti.KodSpetsialnosti AND NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodGruppy LIKE ?");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
   count_predm = rs.getInt(1);
  }

  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("\\trowd \\fs20\\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3400\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4700\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6200\n");
 // report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6900\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8600\n");
  for(int col=1;col<=count_predm;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx"+(8600+col*3*620)+"\n");

  report.write("\\intbl\\b №\\cell\n");
  report.write("\\intbl Номер дела\\cell\n");
  report.write("\\intbl Фамилия\\cell\n");
  report.write("\\intbl Имя\\cell\n");
  report.write("\\intbl Отчество\\cell\n");
 // report.write("\\intbl Отл.\\cell\n");
  report.write("\\intbl № договора\\cell\n");

  stmt = conn.prepareStatement("SELECT DISTINCT np.Predmet,np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens,Abiturient a WHERE a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta=ens.KodPredmeta AND a.KodGruppy LIKE ? ORDER BY np.KodPredmeta ASC");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()) { 
    report.write("\\intbl{"+rs.getString(1)+"}\\cell\n");
  }
  report.write("\\intbl\\b0\\row\n");

  report.write("\\trowd\\trqc \\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx1800\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx3400\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx4700\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx6200\n");
//  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx6900\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8600\n");
  for(int col=1;col<=count_predm*3;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx"+(8600+col*620)+"\n");

  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");
 // report.write("\\intbl \\cell\n");
  report.write("\\intbl \\cell\n");

  for(int col=1;col<=count_predm;col++) {
     report.write("\\intbl \\fs16 \\b атт.\\cell\n");
     report.write("\\intbl ЕГЭ\\cell\n");
     report.write("\\intbl заявл.\\cell\n");
  }
  report.write("\\intbl \\row\n");

  report.write("\\b0\\trowd\\trqc \\fs20 \\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6200\n");
 // report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8600\n");
  for(int col=1;col<=count_predm*3;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx"+(8600+col*620)+"\n");

  int number = 0;
  int old_Ka = -1;
  String note = new String();
  AbiturientBean abit_TMP = new AbiturientBean();

  stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaAtt,zo.OtsenkaEge,zo.OtsenkaZajavl,NomerPlatnogoDogovora FROM NazvanijaPredmetov np, ZajavlennyeShkolnyeOtsenki zo, Abiturient a WHERE np.KodPredmeta=zo.KodPredmeta  AND a.KodAbiturienta = zo.KodAbiturienta AND a.KodGruppy LIKE ? AND zo.KodPredmeta IN (SELECT DISTINCT np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens,Abiturient a WHERE a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta=ens.KodPredmeta AND a.KodGruppy LIKE ?) GROUP BY a.KodAbiturienta,np.KodPredmeta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,zo.OtsenkaAtt,zo.OtsenkaEge,zo.OtsenkaZajavl,NomerPlatnogoDogovora ORDER BY Familija,Imja,Otchestvo,np.KodPredmeta ASC");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  stmt.setObject(2,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next())
  {
        if(rs.getInt(1) != old_Ka) {

          if(old_Ka != -1) {
            report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
            report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
            report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
            report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
            report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
            report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
            report.write("\\intbl\\ql{" +StringUtil.ntv(abit_TMP.getNomerPlatnogoDogovora())+"}\\cell\n");
            for(int ots=0;ots<notes.size();ots++) {
             report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
            }
            report.write("\\intbl\\row\n");
          }

          old_Ka = rs.getInt(1);
          notes = new ArrayList();

          abit_TMP = new AbiturientBean();
          abit_TMP.setNumber(Integer.toString(++number));
          abit_TMP.setKodAbiturienta(new Integer(old_Ka));
          abit_TMP.setFamilija(rs.getString(2));
          abit_TMP.setImja(rs.getString(3));
          abit_TMP.setOtchestvo(rs.getString(4));
          abit_TMP.setNomerLichnogoDela(rs.getString(5));
          abit_TMP.setNomerPlatnogoDogovora(rs.getString(9));
        //  abit_TMP.setShifrMedali(rs.getString(10));

// Оц. Атт. / ЕГЭ / Заявл.

          for(int i=6;i<=8;i++){
            note = rs.getString(i);

            if(!note.equals("-"))
              notes.add(note);
            else 
              notes.add(Constants.emptyNote);
          }
        } else { 
            for(int i=6;i<=8;i++){
              note = rs.getString(i);
              if(!note.equals("-")) {
                notes.add(note);
              } 
              else 
                notes.add(Constants.emptyNote);
            }
          }
  }
// Добавление последнего абитуриента в список

        report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
        report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
        report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
        report.write("\\intbl{" +abit_TMP.getImja()+"}\\cell\n");
        report.write("\\intbl{" +abit_TMP.getOtchestvo()+"}\\cell\n");
       // report.write("\\intbl\\qc{" +abit_TMP.getShifrMedali()+"}\\cell\n");
        report.write("\\intbl\\ql{" +StringUtil.ntv(abit_TMP.getNomerPlatnogoDogovora())+"}\\cell\n");
        for(int ots=0;ots<notes.size();ots++) {
         report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
        }
        report.write("\\intbl\\row\n");
        report.write("\\pard\\page");

}// Закрывает цикл перебора групп факультета

        report.write("}");
        report.close();
}

if(tipotch == 2){

/**************************************************************/
/***********  тип отчета = 2 - группа без оценок  *************/
/**************************************************************/

  stmt = conn.prepareStatement("SELECT Gruppa FROM Gruppy WHERE KodGruppy LIKE ?");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next())  Gru=rs.getString(1);

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    name = "Список группы "+Gru;

    file_con = "list_gr_"+StringUtil.toEng(Gru);

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 
 
  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("\\fs32 \\qc Список абитуриентов группы\\b1{ "+Gru+"}\n");
  report.write("\\par\\par\n");

  report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5600");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7700");
 // report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10900");

  report.write("\\intbl\\b №\\cell");
  report.write("\\intbl Номер дела\\cell");
  report.write("\\intbl Фамилия\\cell");
  report.write("\\intbl Имя\\cell");
  report.write("\\intbl Отчество\\cell");
  //report.write("\\intbl Отличие\\cell");
  report.write("\\intbl № договора\\cell");
  report.write("\\intbl \\row");

  report.write("\\fs24 \\b0 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5600");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7700");
 // report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10900");

  int st=1;

  stmt = conn.prepareStatement("SELECT a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.NomerPlatnogoDogovora FROM Abiturient a WHERE  a.KodGruppy LIKE ? GROUP BY a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.NomerPlatnogoDogovora ORDER BY a.Familija,a.Imja,a.Otchestvo ASC");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()){
    report.write("\\intbl\\qc{"+(st++)+"}\\cell");
    report.write("\\intbl\\qc{"+rs.getString(4)+"}\\cell");
    report.write("\\intbl\\ql{"+rs.getString(1)+"}\\cell");
    report.write("\\intbl\\ql{"+rs.getString(2)+"}\\cell");
    report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell");
  //  report.write("\\intbl\\qc{"+rs.getString(6)+"}\\cell");
    report.write("\\intbl\\ql{"+StringUtil.ntv(rs.getString(5))+"}\\cell");
    report.write("\\intbl\\row");
  }
  report.write("}");
  report.close();
}

/**************************************************************/
/******  тип отчета = 22 - группы факультета без оценок  ******/
/**************************************************************/
if(tipotch == 22){
stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE ShifrFakulteta LIKE ? AND KodVuza LIKE ?");
stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) {
  name = "Списки групп "+rs.getString(1).toUpperCase();
  file_con = "lists_grps_"+StringUtil.toEng(rs.getString(1).toUpperCase());
}
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

// Выборка всех групп факультета в цикле
stmt = conn.prepareStatement("SELECT DISTINCT g.Gruppa,g.KodGruppy FROM Gruppy g,Fakultety f,Abiturient a WHERE a.KodGruppy=g.KodGruppy AND f.KodFakulteta=g.KodFakulteta AND f.ShifrFakulteta LIKE ? AND g.KodGruppy NOT LIKE 1 ORDER BY g.KodGruppy");
stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
rs_a = stmt.executeQuery();
while(rs_a.next()){
Gru=rs_a.getString(1);
abit_SD.setKodSpetsialnosti(new Integer(rs_a.getString(2)));

  report.write("\\fs32 \\qc Список абитуриентов группы\\b1{ "+Gru+"}\\b0\n");
  report.write("\\par\\par\n");

  report.write("\\fs26 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5600");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7700");
 // report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10900");

  report.write("\\intbl\\b №\\cell");
  report.write("\\intbl Номер дела\\cell");
  report.write("\\intbl Фамилия\\cell");
  report.write("\\intbl Имя\\cell");
  report.write("\\intbl Отчество\\cell");
 // report.write("\\intbl Отличие\\cell");
  report.write("\\intbl № договора\\cell");
  report.write("\\intbl \\row");

  report.write("\\fs24 \\b0 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5600");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7700");
 // report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10900");

  int st=1;

  stmt = conn.prepareStatement("SELECT a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.NomerPlatnogoDogovora FROM Abiturient a WHERE a.KodGruppy LIKE ? GROUP BY a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.NomerPlatnogoDogovora ORDER BY a.Familija,a.Imja,a.Otchestvo ASC");
  stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()) {
    report.write("\\intbl\\qc{"+(st++)+"}\\cell");
    report.write("\\intbl\\qc{"+rs.getString(4)+"}\\cell");
    report.write("\\intbl\\ql{"+rs.getString(1)+"}\\cell");
    report.write("\\intbl\\ql{"+rs.getString(2)+"}\\cell");
    report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell");
 //   report.write("\\intbl\\qc{"+rs.getString(6)+"}\\cell");
    report.write("\\intbl\\ql{"+StringUtil.ntv(rs.getString(5))+"}\\cell");
    report.write("\\intbl\\row");
  }
  report.write("\\pard\\page\n");

}// Закрывается while-цикл

report.write("}");
report.close();
}
form.setAction(us.getClientIntName("new_rep","crt"));
return mapping.findForward("rep_brw");         
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
        if(lists_Groups_f) return mapping.findForward("lists_Groups_f");
        return mapping.findForward("success");
    }
}