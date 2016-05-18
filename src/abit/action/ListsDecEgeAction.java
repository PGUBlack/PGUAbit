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

public class ListsDecEgeAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    pstmt              = null;
        Statement            stmt               = null;
        Statement            stmt2              = null;
        Statement            stmt3              = null;
        ResultSet            rs                 = null;
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsDecForm         form               = (ListsDecForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_dec_ege_f    = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        String               SS                 = new String();           // шифр
        String               NS                 = new String();           // название специальности
        String               PP                 = new String();           // план приема
        String               AF                 = new String();           // аббревиатура факультета
        StringBuffer         excludeList        = new StringBuffer("-1");
        StringBuffer         query              = new StringBuffer();
        int                  kAbit              = -1;
        int                  summa              = -1;
        String               nlds               = new String();
        boolean              header             = false;
        boolean              primechanie        = false;
        int                  nomer              = 0;
        int                  count_predm        = 0;
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsDecEgeAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsDecEgeForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
 
/********************* Подготовка данных для ввода с помощью селекторов *************************/

    pstmt = conn.prepareStatement("SELECT DISTINCT ShifrFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY AbbreviaturaFakulteta ASC");
    pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = pstmt.executeQuery();
    while (rs.next()) {
      AbiturientBean abit_TMP = new AbiturientBean();
      abit_TMP.setShifrFakulteta(rs.getString(1));
      abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
      abit_SD_S1.add(abit_TMP);
    }
    boolean priznak = true;
    String kPredmeta = new String();
    String oldKode = new String();
    String oldAbbr = new String();
    pstmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,EkzamenyNaSpetsialnosti.KodPredmeta,Abbreviatura,AbbreviaturaFakulteta FROM Spetsialnosti,EkzamenyNaSpetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND KodVuza LIKE ? ORDER BY Abbreviatura,EkzamenyNaSpetsialnosti.KodPredmeta,AbbreviaturaFakulteta ASC");
    pstmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
    rs = pstmt.executeQuery();
    while (rs.next()) {
      AbiturientBean abit_TMP = new AbiturientBean();
      abit_TMP.setSpecial1(rs.getString(1));
      if(priznak) { oldKode = rs.getString(1); oldAbbr = rs.getString(3); priznak = false; }
        if(!oldKode.equals(rs.getString(1))) {
          if(kPredmeta.equals("")) kPredmeta += "%" + rs.getString(2);
            abit_TMP.setSpecial1(oldKode + kPredmeta);
            abit_TMP.setAbbreviatura(oldAbbr);
            abit_SD_S2.add(abit_TMP);
            priznak = true;
            kPredmeta = "";
        }
        kPredmeta += "%" + rs.getString(2);
    }
    AbiturientBean abit_TMP2 = new AbiturientBean();
    abit_TMP2.setSpecial1(oldKode + kPredmeta);
    abit_TMP2.setAbbreviatura(oldAbbr);
    abit_SD_S2.add(abit_TMP2);

/************************************************************************************************/
      if ( form.getAction() == null ) {

           form.setAction(us.getClientIntName("view","init"));

      } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  String file_con = new String();

  String name = new String();

  abit_SD.setKodSpetsialnosti(new Integer((abit_SD.getSpecial1()).substring(0,(abit_SD.getSpecial1()).indexOf("%"))));

  if(abit_SD.getPriznakSortirovki().equals("budgetniki")) file_con = "lists_dec_ege_budj";

  if(abit_SD.getPriznakSortirovki().equals("kontraktniki")) file_con = "lists_dec_ege_dog";

//  if(abit_SD.getPriznakSortirovki().equals("z_budgetniki")) file_con = "lists_dec_ege_z_budj";

//  if(abit_SD.getPriznakSortirovki().equals("z_kontraktniki")) file_con = "lists_dec_ege_z_dog";

  String priority = new String();
  String priority_query = new String();

  if(abit_SD.getSpecial4() != null && abit_SD.getSpecial4().length() > 1 ) { 
    priority = "всех приоритетов";
    priority_query = "%";
  }
  else {
    priority = abit_SD.getSpecial4()+"-го приоритета";
    priority_query = abit_SD.getSpecial4();
  }

  pstmt = conn.prepareStatement("SELECT s.ShifrSpetsialnostiOKSO,s.NazvanieSpetsialnosti,s.Abbreviatura,s.PlanPriema,f.AbbreviaturaFakulteta FROM Spetsialnosti s,Fakultety f WHERE s.KodSpetsialnosti LIKE ? AND f.KodFakulteta=s.KodFakulteta");
  pstmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()){
    SS = rs.getString(1);
    NS = rs.getString(2).toUpperCase();
    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
      name = "Список бюдж. для дек. (ЕГЭ) "+AF+", спец-ти "+rs.getString(3).toUpperCase()+" "+priority;

    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
      name = "Список контр. для дек. (ЕГЭ) "+AF+", спец-ти "+rs.getString(3).toUpperCase()+" "+priority;

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      name = "Список заочн. бюдж. для дек. (ЕГЭ) "+AF+", спец-ти "+rs.getString(3).toUpperCase()+" "+priority;
//    else 
//      name = "Список заочн. контр. для дек. (ЕГЭ) "+AF+", спец-ти "+rs.getString(3).toUpperCase()+" "+priority;

    PP = rs.getString(4);
    AF = rs.getString(5).toUpperCase();
  }

  pstmt = conn.prepareStatement("SELECT Abbreviatura FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
  pstmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()) file_con = file_con+"_"+StringUtil.toEng(rs.getString(1))+"_pr_"+abit_SD.getSpecial4();
  else file_con = file_con+"_unknown";

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

  pstmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs40 \\qc "+rs.getString(1)+"\n");

  report.write("\\fs24\\par\\par\n");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
    report.write("\\fs24\\b0\\qc{Список бюджетников "+priority+" с баллами ЕГЭ для деканата }\\b1{"+AF+"}\\b0\\par{на специальность (направление):}\\par\\b1\\'ab{"+NS+"}\\'bb\\qc{ ("+SS+")}\\b0\n");
    report.write("\\par\\par План приёма: \\b1 "+PP+"\\par\n");

  } else if(abit_SD.getPriznakSortirovki().equals("kontraktniki")){
    report.write("\\fs24\\b0\\qc{Список договорников "+priority+" с баллами ЕГЭ для деканата }\\b1{"+AF+"}\\b0\\par{на специальность (направление):}\\par\\b1\\'ab{"+NS+"}\\'bb\\qc{ ("+SS+")}\\b0\n");
    report.write("\\par\\par План приёма: \\b1 "+PP+"\\par\n");

//  } else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki")){
//    report.write("\\fs24\\b0\\qc{Список заочн. бюджетников "+priority+" с баллами ЕГЭ для деканата }\\b1{"+AF+"}\\b0\\par{на специальность (направление):}\\par\\b1\\'ab{"+NS+"}\\'bb\\qc{ ("+SS+")}\\b0\n");
//    report.write("\\par\\par План приёма: \\b1 "+PP+"\\par\n");

//  } else {
//    report.write("\\fs24\\b0\\qc{Список заочн. договорников "+priority+" с баллами ЕГЭ для деканата }\\b1{"+AF+"}\\b0\\par{на специальность (направление):}\\par\\b1\\'ab{"+NS+"}\\'bb\\qc{ ("+SS+")}\\b0\n");
//    report.write("\\par\\par План приёма: \\b1 "+PP+"\\par\n");
  }

  pstmt = conn.prepareStatement("SELECT COUNT(DISTINCT ens.KodPredmeta) FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens WHERE np.KodPredmeta = ens.KodPredmeta AND ens.KodSpetsialnosti LIKE  ?");
  pstmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
  rs = pstmt.executeQuery();
  while (rs.next()) {
    count_predm = rs.getInt(1);
  }


/******************************************************/
/**********  ЗАПРОСЫ НА ВЫБОРКУ АБИТУРИЕНТОВ **********/
/******************************************************/




// ОЛИМПИЙЦЫ (без вступительных испытаний)

  header = false;

  query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

  query.append(" AND (m.ShifrMedali IN ('о','к') AND (s.ShifrSpetsialnosti IN ("+Constants.Sur_Talants_NO_KONKURS+"))) ");

  if(abit_SD.getSpecial2().equals("orig"))
    query.append("AND a.TipDokSredObraz LIKE ('о') ");
  else if(abit_SD.getSpecial2().equals("copy"))
    query.append("AND a.TipDokSredObraz LIKE ('к') ");

  if(abit_SD.getSpecial3().equals("rek"))
    query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

  query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

  stmt = conn.createStatement();

  rs = stmt.executeQuery(query.toString());

  while(rs.next()) {

    if(header == false) {

      header = true;
      report.write("\\pard\\par\n");
      report.write("\\b1\\ql\\fs28{Поступающие без вступительных испытаний}\\b0\\par\\par\n");
      report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+count_predm*720)+"\n"); 
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n"); 

      report.write("\\intbl\\qc{№}\\cell\n");

      if(abit_SD.getSpecial6().equals("pr1"))
        report.write("\\intbl{Номер личного дела}\\cell\n");
      else
        report.write("\\intbl{Номера личных дел}\\cell\n");

      report.write("\\intbl{Шифр}\\cell\n");
      report.write("\\intbl{Фамилия}\\cell\n");
      report.write("\\intbl{Имя}\\cell\n");
      report.write("\\intbl{Отчество}\\cell\n");
      report.write("\\intbl{Атт.}\\cell\n");
      report.write("\\intbl{Отл.}\\cell\n");
      report.write("\\intbl{Предм. / Баллы}\\cell\n");
      report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      for(int col=1;col<=count_predm;col++)
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");

      stmt3 = conn.createStatement();
      rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+") ORDER BY KodPredmeta ASC");
      while(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

      report.write("\\intbl\\cell\n");
      report.write("\\intbl \\row\n");

      report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      for(int col=1;col<=count_predm;col++)
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");
    }

    report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");

    nlds = rs.getString(2);
    if(abit_SD.getSpecial6().equals("all")) {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT NomerLichnogoDela FROM Konkurs WHERE Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      while(rs2.next()) nlds += "\\par{"+rs2.getString(1)+"}";
    }
    report.write("\\intbl\\qc "+nlds+"\\cell\n");            // NLDs
    report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR (текущий НЛД)
    report.write("\\intbl\\ql "+rs.getString(3)+"\\cell\n"); // FAMIL
    report.write("\\intbl\\ql "+rs.getString(4)+"\\cell\n"); // IMJA
    report.write("\\intbl\\ql "+rs.getString(5)+"\\cell\n"); // OTCH
    report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
    report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // LGOTA

    stmt2 = conn.createStatement();
    rs2 = stmt2.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta ASC");
    while(rs2.next()) {
      report.write("\\intbl\\qc "+rs2.getString(1)+"\\cell\n"); // OtsenkaEge
    }
    report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
    report.write("\\intbl\\row\n");

//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
    excludeList.append(","+rs.getString(1));
  }
  if(header) report.write("\\pard\\par\n");




// ЛЬГОТНИКИ (только инвалиды и сироты)

  header = false;

  query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND l.ShifrLgot IN ('и','с') AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

  if(abit_SD.getSpecial2().equals("orig"))
    query.append("AND a.TipDokSredObraz LIKE ('о') ");
  else if(abit_SD.getSpecial2().equals("copy"))
    query.append("AND a.TipDokSredObraz LIKE ('к') ");

  if(abit_SD.getSpecial3().equals("rek"))
    query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

  query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

  stmt = conn.createStatement();
  rs = stmt.executeQuery(query.toString());
  while(rs.next()) {

    if(header == false) {
      header = true;
      report.write("\\pard\\par\n");
      report.write("\\b1\\ql\\fs28{Поступающие вне конкурса}\\b0\\par\\par\n");
      report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+count_predm*720)+"\n"); 
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n"); 

      report.write("\\intbl\\qc{№}\\cell\n");
      if(abit_SD.getSpecial6().equals("pr1"))
        report.write("\\intbl{Номер личного дела}\\cell\n");
      else
        report.write("\\intbl{Номера личных дел}\\cell\n");
      report.write("\\intbl{Шифр}\\cell\n");
      report.write("\\intbl{Фамилия}\\cell\n");
      report.write("\\intbl{Имя}\\cell\n");
      report.write("\\intbl{Отчество}\\cell\n");
      report.write("\\intbl{Атт.}\\cell\n");
      report.write("\\intbl{Льг.}\\cell\n");
      report.write("\\intbl{Предм. / Баллы}\\cell\n");
      report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      for(int col=1;col<=count_predm;col++)
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");

      stmt3 = conn.createStatement();
      rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+") ORDER BY KodPredmeta ASC");
      while(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

      report.write("\\intbl\\cell\n");
      report.write("\\intbl \\row\n");

      report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      for(int col=1;col<=count_predm;col++)
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");
    }

    report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");
    nlds = rs.getString(2);
    if(abit_SD.getSpecial6().equals("all")) {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT NomerLichnogoDela FROM Konkurs WHERE Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      while(rs2.next()) nlds += "\\par{"+rs2.getString(1)+"}";
    }
    report.write("\\intbl\\qc "+nlds+"\\cell\n");            // NLDs
    report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR (текущий НЛД)
    report.write("\\intbl\\ql "+rs.getString(3)+"\\cell\n"); // FAMIL
    report.write("\\intbl\\ql "+rs.getString(4)+"\\cell\n"); // IMJA
    report.write("\\intbl\\ql "+rs.getString(5)+"\\cell\n"); // OTCH
    report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
    report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // LGOTA

    stmt2 = conn.createStatement();
    rs2 = stmt2.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta ASC");
    while(rs2.next()) {
      report.write("\\intbl\\qc "+rs2.getString(1)+"\\cell\n"); // OtsenkaEge
    }
    report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
    report.write("\\intbl\\row\n");

//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
    excludeList.append(","+rs.getString(1));
  }
  if(header) report.write("\\pard\\par\n");




// ЦЕЛЕВОЙ ПРИЕМ ( Уч. совет ПГУ )


  header = false;

  int total_amount = 0, tselev_nomer = 0;

  stmt = conn.createStatement();
  rs = stmt.executeQuery("SELECT TselPr_PGU FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+abit_SD.getKodSpetsialnosti()+"'");
  if( rs.next() ) {

    if(StringUtil.toInt(rs.getString(1),0) != 0) {

      if(header == false) {
        header = true;
        report.write("\\pard\\par\n");
        report.write("\\b1\\ql\\fs28{Поступающие по целевому приёму (в интересах органов гос. власти)}\\b0\\par\\par\n");
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        if(abit_SD.getSpecial6().equals("pr1"))
          report.write("\\intbl{Номер личного дела}\\cell\n");
        else
          report.write("\\intbl{Номера личных дел}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия}\\cell\n");
        report.write("\\intbl{Имя}\\cell\n");
        report.write("\\intbl{Отчество}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Код}\\par{цел.}\\par{приёма}\\cell\n");
        report.write("\\intbl{Предм. / Баллы}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+") ORDER BY KodPredmeta ASC");
        while(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");

        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        total_amount = StringUtil.toInt(rs.getString(1),0);
      }
   } else report.write("\\pard\\par\n");

// Абитуриенты-целевики

    query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND tp.ShifrPriema IN ('ц','ф') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY tp.ShifrPriema DESC,SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

    stmt = conn.createStatement();
    rs = stmt.executeQuery(query.toString());
    while(rs.next()) {

      if((++tselev_nomer) <= total_amount) ++nomer;

      report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
      nlds = rs.getString(2);
      if(abit_SD.getSpecial6().equals("all")) {
        stmt2 = conn.createStatement();
        rs2 = stmt2.executeQuery("SELECT NomerLichnogoDela FROM Konkurs WHERE Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
        while(rs2.next()) nlds += "\\par{"+rs2.getString(1)+"}";
      }
      report.write("\\intbl\\qc "+nlds+"\\cell\n");            // NLDs
      report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql "+rs.getString(3)+"\\cell\n"); // FAMIL
      report.write("\\intbl\\ql "+rs.getString(4)+"\\cell\n"); // IMJA
      report.write("\\intbl\\ql "+rs.getString(5)+"\\cell\n"); // OTCH
      report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc "+rs2.getString(1)+"\\cell\n"); // OtsenkaEge
      }
      report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
      report.write("\\intbl\\row\n");

//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
      excludeList.append(","+rs.getString(1));

    }

// Добавляем строки целевиков с надписью "вакантно"
    for(;tselev_nomer<total_amount;tselev_nomer++) {

      report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // №
      report.write("\\intbl\\qc\\cell\n");                          // NLD
      report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql{вакантно}\\cell\n");                // FAMIL
      report.write("\\intbl\\ql\\cell\n");                          // IMJA
      report.write("\\intbl\\ql\\cell\n");                          // OTCH
      report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens WHERE ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" ORDER BY ens.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc\\cell\n");                        // OtsenkaEge
      }
      report.write("\\intbl\\qc\\cell\n");                          // SUMMA Ege
      report.write("\\intbl\\row\n");
    }

    if(header) report.write("\\pard\\par\n");
  }





// ЦЕЛЕВОЙ ПРИЕМ (РосАтом)

  header = false;

  total_amount = 0;

  tselev_nomer = 0;

  stmt = conn.createStatement();
  rs = stmt.executeQuery("SELECT TselPr_1 FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+abit_SD.getKodSpetsialnosti()+"'");
  if( rs.next() ) {

    if( StringUtil.toInt(rs.getString(1),0) != 0 ) {
      if(header == false) {
        header = true;
        report.write("\\pard\\par\n");
        report.write("\\b1\\ql\\fs28{Поступающие по целевому приёму (в интересах Росатома)}\\b0\\par\\par\n");
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        if(abit_SD.getSpecial6().equals("pr1"))
          report.write("\\intbl{Номер личного дела}\\cell\n");
        else
          report.write("\\intbl{Номера личных дел}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия}\\cell\n");
        report.write("\\intbl{Имя}\\cell\n");
        report.write("\\intbl{Отчество}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Код}\\par{цел.}\\par{приёма}\\cell\n");
        report.write("\\intbl{Предм. / Баллы}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+") ORDER BY KodPredmeta ASC");
        while(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");

        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        total_amount = StringUtil.toInt(rs.getString(1),0);
      }
   } else report.write("\\pard\\par\n");

// Абитуриенты-целевики

    query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND tp.ShifrPriema IN ('а') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

    stmt = conn.createStatement();
    rs = stmt.executeQuery(query.toString());
    while(rs.next()) {

      if((++tselev_nomer) <= total_amount) ++nomer;

      report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
      nlds = rs.getString(2);
      if(abit_SD.getSpecial6().equals("all")) {
        stmt2 = conn.createStatement();
        rs2 = stmt2.executeQuery("SELECT NomerLichnogoDela FROM Konkurs WHERE Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
        while(rs2.next()) nlds += "\\par{"+rs2.getString(1)+"}";
      }
      report.write("\\intbl\\qc "+nlds+"\\cell\n");            // NLDs
      report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql "+rs.getString(3)+"\\cell\n"); // FAMIL
      report.write("\\intbl\\ql "+rs.getString(4)+"\\cell\n"); // IMJA
      report.write("\\intbl\\ql "+rs.getString(5)+"\\cell\n"); // OTCH
      report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc "+rs2.getString(1)+"\\cell\n"); // OtsenkaEge
      }
      report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
      report.write("\\intbl\\row\n");

//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
      excludeList.append(","+rs.getString(1));

    }

// Добавляем строки целевиков с надписью "вакантно"
    for(;tselev_nomer<total_amount;tselev_nomer++) {

      report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // №
      report.write("\\intbl\\qc\\cell\n");                          // NLD
      report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql{вакантно}\\cell\n");                // FAMIL
      report.write("\\intbl\\ql\\cell\n");                          // IMJA
      report.write("\\intbl\\ql\\cell\n");                          // OTCH
      report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens WHERE ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" ORDER BY ens.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc\\cell\n");                        // OtsenkaEge
      }
      report.write("\\intbl\\qc\\cell\n");                          // SUMMA Ege
      report.write("\\intbl\\row\n");
    }

    if(header) report.write("\\pard\\par\n");
  }




// ЦЕЛЕВОЙ ПРИЕМ (РосКосмос)

  header = false;

  total_amount = 0;

  tselev_nomer = 0;

  stmt = conn.createStatement();
  rs = stmt.executeQuery("SELECT TselPr_2 FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+abit_SD.getKodSpetsialnosti()+"'");
  if( rs.next() ) {

    if( StringUtil.toInt(rs.getString(1),0) != 0 ) {
      if(header == false) {
        header = true;
        report.write("\\pard\\par\n");
        report.write("\\b1\\ql\\fs28{Поступающие по целевому приёму (в интересах Роскосмоса)}\\b0\\par\\par\n");
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        if(abit_SD.getSpecial6().equals("pr1"))
          report.write("\\intbl{Номер личного дела}\\cell\n");
        else
          report.write("\\intbl{Номера личных дел}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия}\\cell\n");
        report.write("\\intbl{Имя}\\cell\n");
        report.write("\\intbl{Отчество}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Код}\\par{цел.}\\par{приёма}\\cell\n");
        report.write("\\intbl{Предм. / Баллы}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+") ORDER BY KodPredmeta ASC");
        while(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");

        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        total_amount = StringUtil.toInt(rs.getString(1),0);
      }
   } else report.write("\\pard\\par\n");

// Абитуриенты-целевики

    query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND tp.ShifrPriema IN ('к') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

    stmt = conn.createStatement();
    rs = stmt.executeQuery(query.toString());
    while(rs.next()) {

      if((++tselev_nomer) <= total_amount) ++nomer;

      report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
      nlds = rs.getString(2);
      if(abit_SD.getSpecial6().equals("all")) {
        stmt2 = conn.createStatement();
        rs2 = stmt2.executeQuery("SELECT NomerLichnogoDela FROM Konkurs WHERE Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
        while(rs2.next()) nlds += "\\par{"+rs2.getString(1)+"}";
      }
      report.write("\\intbl\\qc "+nlds+"\\cell\n");            // NLDs
      report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql "+rs.getString(3)+"\\cell\n"); // FAMIL
      report.write("\\intbl\\ql "+rs.getString(4)+"\\cell\n"); // IMJA
      report.write("\\intbl\\ql "+rs.getString(5)+"\\cell\n"); // OTCH
      report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc "+rs2.getString(1)+"\\cell\n"); // OtsenkaEge
      }
      report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
      report.write("\\intbl\\row\n");

//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
      excludeList.append(","+rs.getString(1));

    }

// Добавляем строки целевиков с надписью "вакантно"
    for(;tselev_nomer<total_amount;tselev_nomer++) {

      report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // №
      report.write("\\intbl\\qc\\cell\n");                          // NLD
      report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql{вакантно}\\cell\n");                // FAMIL
      report.write("\\intbl\\ql\\cell\n");                          // IMJA
      report.write("\\intbl\\ql\\cell\n");                          // OTCH
      report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens WHERE ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" ORDER BY ens.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc\\cell\n");                        // OtsenkaEge
      }
      report.write("\\intbl\\qc\\cell\n");                          // SUMMA Ege
      report.write("\\intbl\\row\n");
    }

    if(header) report.write("\\pard\\par\n");
  }




// ЦЕЛЕВОЙ ПРИЕМ (МинПромТорг)

  header = false;

  total_amount = 0;

  tselev_nomer = 0;

  stmt = conn.createStatement();
  rs = stmt.executeQuery("SELECT TselPr_3 FROM Spetsialnosti WHERE KodSpetsialnosti LIKE '"+abit_SD.getKodSpetsialnosti()+"'");
  if( rs.next() ) {

    if( StringUtil.toInt(rs.getString(1),0) != 0 ) {
      if(header == false) {
        header = true;
        report.write("\\pard\\par\n");
        report.write("\\b1\\ql\\fs28{Поступающие по целевому приёму (в интересах Минпромторга)}\\b0\\par\\par\n");
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+count_predm*720)+"\n"); 
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n"); 

        report.write("\\intbl\\qc{№}\\cell\n");
        if(abit_SD.getSpecial6().equals("pr1"))
          report.write("\\intbl{Номер личного дела}\\cell\n");
        else
          report.write("\\intbl{Номера личных дел}\\cell\n");
        report.write("\\intbl{Шифр}\\cell\n");
        report.write("\\intbl{Фамилия}\\cell\n");
        report.write("\\intbl{Имя}\\cell\n");
        report.write("\\intbl{Отчество}\\cell\n");
        report.write("\\intbl{Атт.}\\cell\n");
        report.write("\\intbl{Код}\\par{цел.}\\par{приёма}\\cell\n");
        report.write("\\intbl{Предм. / Баллы}\\cell\n");
        report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
        report.write("\\intbl\\row\n");

        report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");
        report.write("\\intbl\\cell\n");

        stmt3 = conn.createStatement();
        rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+") ORDER BY KodPredmeta ASC");
        while(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

        report.write("\\intbl\\cell\n");
        report.write("\\intbl \\row\n");

        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");

        for(int col=1;col<=count_predm;col++)
          report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

        total_amount = StringUtil.toInt(rs.getString(1),0);
      }
   } else report.write("\\pard\\par\n");

// Абитуриенты-целевики

    query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,TselevojPriem tp, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND tp.ShifrPriema IN ('т') AND kon.Target LIKE 'д' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

    if(abit_SD.getSpecial2().equals("orig"))
      query.append("AND a.TipDokSredObraz LIKE ('о') ");
    else if(abit_SD.getSpecial2().equals("copy"))
      query.append("AND a.TipDokSredObraz LIKE ('к') ");

    if(abit_SD.getSpecial3().equals("rek"))
      query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

    query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,tp.ShifrPriema,a.TipDokSredObraz,kon.NomerLichnogoDela ORDER BY SummaEge DESC,a.Familija,a.Imja,a.Otchestvo");

    stmt = conn.createStatement();
    rs = stmt.executeQuery(query.toString());
    while(rs.next()) {

      if((++tselev_nomer) <= total_amount) ++nomer;

      report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
      nlds = rs.getString(2);
      if(abit_SD.getSpecial6().equals("all")) {
        stmt2 = conn.createStatement();
        rs2 = stmt2.executeQuery("SELECT NomerLichnogoDela FROM Konkurs WHERE Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
        while(rs2.next()) nlds += "\\par{"+rs2.getString(1)+"}";
      }
      report.write("\\intbl\\qc "+nlds+"\\cell\n");            // NLDs
      report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql "+rs.getString(3)+"\\cell\n"); // FAMIL
      report.write("\\intbl\\ql "+rs.getString(4)+"\\cell\n"); // IMJA
      report.write("\\intbl\\ql "+rs.getString(5)+"\\cell\n"); // OTCH
      report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE zso.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc "+rs2.getString(1)+"\\cell\n"); // OtsenkaEge
      }
      report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n"); // SUMMA Ege
      report.write("\\intbl\\row\n");

//Добавляем код абитуриента в список исключения для того, чтобы он появлялся в отчете только один раз
 
      excludeList.append(","+rs.getString(1));

    }

// Добавляем строки целевиков с надписью "вакантно"
    for(;tselev_nomer<total_amount;tselev_nomer++) {

      report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // №
      report.write("\\intbl\\qc\\cell\n");                          // NLD
      report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
      report.write("\\intbl\\ql{вакантно}\\cell\n");                // FAMIL
      report.write("\\intbl\\ql\\cell\n");                          // IMJA
      report.write("\\intbl\\ql\\cell\n");                          // OTCH
      report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
      report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA

      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT ens.KodPredmeta FROM EkzamenyNaSpetsialnosti ens WHERE ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" ORDER BY ens.KodPredmeta ASC");
      while(rs2.next()) {
        report.write("\\intbl\\qc\\cell\n");                        // OtsenkaEge
      }
      report.write("\\intbl\\qc\\cell\n");                          // SUMMA Ege
      report.write("\\intbl\\row\n");
    }

    if(header) report.write("\\pard\\par\n");
  }





// По СУММЕ НАБРАННЫХ БАЛЛОВ

  header = false;

/**********************************************************************************************/
// В первом запросе объединения исключаются ВСЕ "Сурские таланты" и олимпиадники вообще
/**********************************************************************************************/

  query = new StringBuffer("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'-' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

  query.append("AND m.ShifrMedali NOT IN ('о','к') ");

  if(abit_SD.getSpecial2().equals("orig"))
    query.append("AND a.TipDokSredObraz LIKE ('о') ");
  else if(abit_SD.getSpecial2().equals("copy"))
    query.append("AND a.TipDokSredObraz LIKE ('к') ");

  if(abit_SD.getSpecial3().equals("rek"))
    query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

  query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");

/**********************************************************************************************/
// Подсоединяем Сурские таланты и др. Олимпиадников с баллом по математике == 100 (ИСКУСТВЕННО, согласно правилам приема (хотя в БД не 100))
/**********************************************************************************************/

  query.append(" UNION SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,100+SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'(о)' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

  query.append("AND m.ShifrMedali IN ('о','к') AND np.Sokr NOT LIKE ('Мат') ");

  query.append("AND s.ShifrSpetsialnosti IN("+Constants.Sur_Talants_100B_MAT+") ");

  if(abit_SD.getSpecial2().equals("orig"))
    query.append("AND a.TipDokSredObraz LIKE ('о') ");
  else if(abit_SD.getSpecial2().equals("copy"))
    query.append("AND a.TipDokSredObraz LIKE ('к') ");

  if(abit_SD.getSpecial3().equals("rek"))
    query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

  query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");

/**********************************************************************************************/
// Подсоединяем Сурские таланты и др. Олимпиадников с их РЕАЛЬНЫМИ БАЛЛАМИ для СПЕЦИАЛЬНОСТЕЙ НЕ ИЗ ЛЬГОТНОГО СПИСКА
/**********************************************************************************************/

  query.append(" UNION SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,SUM(zso.OtsenkaEge)\"SummaEge\",kon.NomerLichnogoDela,'-' FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s, Medali m,Lgoty l, NazvanijaPredmetov np,Kursy k,EkzamenyNaSpetsialnosti ens,Forma_Obuch fo,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND fo.KodFormyOb=a.KodFormyOb AND k.KodKursov=a.KodKursov AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodLgot=l.KodLgot AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND zso.KodPredmeta=np.KodPredmeta AND a.KodAbiturienta=zso.KodAbiturienta AND ens.KodPredmeta=zso.KodPredmeta AND kon.KodSpetsialnosti LIKE '"+abit_SD.getKodSpetsialnosti()+"' AND a.DokumentyHranjatsja LIKE 'д' AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

  query.append("AND kon.Prioritet LIKE '"+priority_query+"' ");

  query.append("AND (m.ShifrMedali IN ('о','к') AND s.ShifrSpetsialnosti NOT IN("+Constants.Sur_Talants_100B_MAT+"))");

  if(abit_SD.getSpecial2().equals("orig"))
    query.append("AND a.TipDokSredObraz LIKE ('о') ");
  else if(abit_SD.getSpecial2().equals("copy"))
    query.append("AND a.TipDokSredObraz LIKE ('к') ");

  if(abit_SD.getSpecial3().equals("rek"))
    query.append("AND a.Prinjat LIKE ('р') ");

  if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Bud LIKE 'д')");
    query.append("AND (kon.Bud LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
//    query.append("AND fo.Sokr IN ('очная') AND (kon.Dog LIKE 'д')");
    else
    query.append("AND (kon.Dog LIKE 'д')");

//  else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Bud LIKE 'д')");
//  else 
//    query.append("AND fo.Sokr IN ('заочная','заочно-уск') AND (kon.Dog LIKE 'д')");

  query.append(" GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.TipDokSredObraz,kon.NomerLichnogoDela");

  query.append(" ORDER BY SummaEge DESC,a.TipDokSredObraz DESC,m.ShifrMedali ASC,a.Familija,a.Imja,a.Otchestvo");

  stmt = conn.createStatement();
  rs = stmt.executeQuery(query.toString());
  while(rs.next()) {

    if(header == false) {
      header = true;
      report.write("\\pard\\par\n");
      report.write("\\b1\\ql\\fs28{Успешно прошедшие вступительные испытания}\\b0\\par\\par\n");
      report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+count_predm*720)+"\n"); 
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n"); 

      report.write("\\intbl\\qc{№}\\cell\n");
      if(abit_SD.getSpecial6().equals("pr1"))
        report.write("\\intbl{Номер личного дела}\\cell\n");
      else
        report.write("\\intbl{Номера личных дел}\\cell\n");
      report.write("\\intbl{Шифр}\\cell\n");
      report.write("\\intbl{Фамилия}\\cell\n");
      report.write("\\intbl{Имя}\\cell\n");
      report.write("\\intbl{Отчество}\\cell\n");
      report.write("\\intbl{Атт.}\\cell\n");
      report.write("\\intbl{Отл.}\\cell\n");
      report.write("\\intbl{Предм. / Баллы}\\cell\n");
      report.write("\\intbl{Сумма}\\par{баллов}\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      for(int col=1;col<=count_predm;col++)
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");

      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");

      stmt3 = conn.createStatement();
      rs3 = stmt3.executeQuery("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta IN (SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+") ORDER BY KodPredmeta ASC");
      while(rs3.next()) report.write("\\intbl "+rs3.getString(1)+" \\cell\n");

      report.write("\\intbl\\cell\n");
      report.write("\\intbl \\row\n");

      report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10200\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11800\n");
      for(int col=1;col<=count_predm;col++)
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(11800+col*720)+"\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(12800+count_predm*720)+"\n");
    }

    report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");
    nlds = rs.getString(2);
    if(abit_SD.getSpecial6().equals("all")) {
      stmt2 = conn.createStatement();
      rs2 = stmt2.executeQuery("SELECT NomerLichnogoDela FROM Konkurs WHERE Prioritet NOT LIKE '1' AND KodAbiturienta LIKE '"+rs.getString(1)+"'");
      while(rs2.next()) nlds += "\\par{"+rs2.getString(1)+"}";
    }
    report.write("\\intbl\\qc "+nlds+"\\cell\n");            // NLDs
    report.write("\\intbl\\qc "+rs.getString(9)+"\\cell\n"); // SHIFR LICHNOGO DELA
    report.write("\\intbl\\ql "+rs.getString(3)+"\\cell\n"); // FAMIL
    report.write("\\intbl\\ql "+rs.getString(4)+"\\cell\n"); // IMJA
    report.write("\\intbl\\ql "+rs.getString(5)+"\\cell\n"); // OTCH
    report.write("\\intbl\\qc "+rs.getString(7)+"\\cell\n"); // ATTESTAT (KOPIJA)
    report.write("\\intbl\\qc "+rs.getString(6)+"\\cell\n"); // LGOTA

    stmt2 = conn.createStatement();
    rs2 = stmt2.executeQuery("SELECT zso.OtsenkaEge,np.Sokr FROM ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens,NazvanijaPredmetov np WHERE np.KodPredmeta=zso.KodPredmeta AND zso.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti LIKE "+abit_SD.getKodSpetsialnosti()+" AND zso.KodAbiturienta LIKE "+rs.getString(1)+" ORDER BY zso.KodPredmeta,np.Sokr ASC");
    while(rs2.next()) {
      if(rs.getString(10).equals("(о)") && rs2.getString(2).equals("Мат")) {
        report.write("\\intbl\\qc{100*}\\cell\n");                           // OtsenkaEge (для Сурских талантов устанавливаем 100 баллов принудительно)
        primechanie = true;
      }
      else
        report.write("\\intbl\\qc{"+rs2.getString(1)+"}\\cell\n");           // OtsenkaEge
    }
    report.write("\\intbl\\qc "+rs.getString(8)+"\\cell\n");                 // SUMMA Ege
    report.write("\\intbl\\row\n");
  }

  if(header) report.write("\\pard\n");

  if(primechanie) report.write("\\par\\fs24\\ql\\tab\\tab{* - балл установлен согласно п.3.8. правил приёма ПГУ}\\par\n");

  report.write("}"); 
  report.close();
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
          if ( rs2 != null ) {
               try {
                     rs2.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( rs3 != null ) {
               try {
                     rs3.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( stmt2 != null ) {
               try {
                     stmt2.close();
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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_ege_f) return mapping.findForward("lists_dec_ege_f");
        return mapping.findForward("success");
    }
}