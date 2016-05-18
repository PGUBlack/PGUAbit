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
import java.util.Date;
import abit.action.ListSpecForm;
import java.lang.Object.*;
import abit.Constants;
import abit.sql.*; 

public class ListSpecAction extends Action {

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
        ListSpecForm         form               =  (ListSpecForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_list_spec_f    = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        int                  solve              = 0;
        String               str1               = new String();
        String               str2               = new String();
        String               str3               = new String();
        String               str4               = new String();
        String               str6               = new String();
        int                  a			= 0;
        int                  b                  = 0;
        Integer              all                = new Integer(0);
        UserBean             user               = (UserBean)session.getAttribute("user");
        String               buf1               = new String();
        int                  buf2               = -1;
        String               pr11               = new String();
        String               pr22               = new String();
        String               pr33               = new String();
        Integer              sb                 = new Integer(0);
        Integer              num_buf            = new Integer(0);
        int                  sdano              = 0;
        int                  nb                 = 0;
        int                  count_ab           = 0;
        String               num                = new String();
        String               name               = new String();
        Integer              ots                = new Integer(0);
        boolean              priznak1           = false;
        int                  b_buf              = 0;
      
        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listSpecAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listSpecForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************* Подготовка данных для ввода с помощью селекторов *************************/

            stmt = conn.prepareStatement("SELECT DISTINCT ShifrFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
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
            stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,EkzamenyNaSpetsialnosti.KodPredmeta,Abbreviatura,AbbreviaturaFakulteta FROM Spetsialnosti,EkzamenyNaSpetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND KodVuza LIKE ? ORDER BY 3,2,4 ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
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

/*****************************************************************************/
/*****  Если action равен otchet , то входим в секцию - создание отчёта  *****/
/************************************ OTCHET *********************************/
      int number = 0;
      int ind = 1;                        // Нумерация строк в отчёте
      String file_con = new String();     // Имя файла отчёта
      String pr1 = new String();          // Сокращения
      String pr2 = new String();          // предметов
      String pr3 = new String();
      String SS = new String();
      String NS = new String();

/*************************** Выборка числа абитуриентов из БД ****************************/

      buf1=abit_SD.getKodSpetsialnosti()+"";      
      sb = abit_SD.getSum_bal();
      
      stmt = conn.prepareStatement("SELECT ShifrSpetsialnosti,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
      stmt.setObject(1,new Integer(buf1),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next())
      {
        SS = rs.getString(1);
        NS = rs.getString(2).toUpperCase();
      }
      
      stmt = conn.prepareStatement("SELECT NazvanijaPredmetov.KodPredmeta FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ?");
      stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        pr11 = rs.getString(1);
        rs.next();
        pr22 = rs.getString(1);
        rs.next();
        pr33 = rs.getString(1);
      }
      stmt = conn.prepareStatement("SELECT Abbreviatura FROM Spetsialnosti WHERE ShifrSpetsialnosti LIKE ?");
      stmt.setObject(1,SS,Types.VARCHAR);
      rs = stmt.executeQuery();
      if(rs.next()) {
        file_con="list_spec_"+StringUtil.toEng(rs.getString(1))+"_"+sb;
        name = "Список абит-ов спец-ти "+rs.getString(1).toUpperCase()+", набр. "+sb+" баллов";
      }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

// Данные по абитуриенту для вывода в отчет

      stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,o.Otsenka FROM Abiturient a,Otsenki o WHERE a.KodAbiturienta = o.KodAbiturienta AND o.KodPredmeta IN (?,?,?) AND a.KodSpetsialnosti LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,o.KodPredmeta ORDER BY a.Familija,a.Imja,a.Otchestvo,o.KodPredmeta ASC");
      stmt.setObject(1,new Integer(pr11),Types.INTEGER);
      stmt.setObject(2,new Integer(pr22),Types.INTEGER);
      stmt.setObject(3,new Integer(pr33),Types.INTEGER);
      stmt.setObject(4,new Integer(buf1),Types.INTEGER);

      rs = stmt.executeQuery();
      solve = 0;
      b=0;
        sdano=0;
      number=0;
        
// Заполняем массив данными и сохраняем их в bean
while (rs.next()) {
                AbiturientBean abit_TMP = new AbiturientBean();             
      if(solve == 0) {
            
                      str2  = rs.getString(6);
                  if(!str2.equals("-"))
                  b=b+rs.getInt(6);
                      solve++;
                   } else if(solve == 1) {

                            str4 = rs.getString(6);
                      if (!str4.equals("-"))
                      b=b+rs.getInt(6);
                            solve++;
                          } else if(solve == 2) {
                                   if(!(rs.getString(6)).equals("-"))
                           b=b+rs.getInt(6);      
                           ots = new Integer(b); //конечное значение суммы
                           solve=0;

                              if(sb.intValue()!=b) b=0;
                                    else      priznak1=true;
                                    }// if(solve==2)

                              if(sb.intValue()==b && priznak1==true)
                           {
                             abit_TMP.setNumber(Integer.toString(++number));
                              nb=number;
                              abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));

                                     abit_TMP.setNomerLichnogoDela(rs.getString(2));
                                     abit_TMP.setFamilija(rs.getString(3));
                                     abit_TMP.setImja(rs.getString(4));
                                     abit_TMP.setOtchestvo(rs.getString(5));

                                        abit_TMP.setSpecial8(sdano+"");
                             abits_SD.add(abit_TMP);
                             priznak1=false;
                             b=0;
                           }
                 }

      report.write("{\\rtf1\\ansi\n");
        stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) 
      report.write("\\fs40 \\qc "+rs.getString(1)+"\n");
// Формируем запрос для получения информации и абитуриентах      

      
      report.write("\\par\\par\n");
      report.write("\\fs24 \\b0 \\qc На специальности \\b "+SS+"\\line\\qc \\b"+NS+"\\b0 \\par всего "+nb+" абитуриента(ов), набравших " + sb +" баллов \n");

number=0;
solve=0;
b=0;
      stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,o.Otsenka FROM Abiturient a,Otsenki o WHERE a.KodAbiturienta = o.KodAbiturienta AND o.KodPredmeta IN (?,?,?) AND a.KodSpetsialnosti LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,o.KodPredmeta ORDER BY a.Familija,a.Imja,a.Otchestvo,o.KodPredmeta ASC");
      stmt.setObject(1,new Integer(pr11),Types.INTEGER);
      stmt.setObject(2,new Integer(pr22),Types.INTEGER);
      stmt.setObject(3,new Integer(pr33),Types.INTEGER);
      stmt.setObject(4,abit_SD.getKodSpetsialnosti(),Types.INTEGER); // KodSpetsialnosti
      rs = stmt.executeQuery();


// Формирование таблицы

report.write("\\par\n");
report.write("\\par\n");
report.write("\\fs24 \\trowd \\trhdr \\trqc\\trgaph108\\trrh280\\trleft36\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4500\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9000\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");

report.write("\\intbl\\b1 № \\cell\n");
report.write("\\intbl Номер личного дела \\cell\n");
report.write("\\intbl Фамилия \\cell\n");
report.write("\\intbl Имя \\cell\n");
report.write("\\intbl Отчество \\cell\n");
report.write("\\intbl Сдано экз.\\cell\n");
report.write("\\intbl \\row\n");
report.write("\\b0 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4500\n");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500\n");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9000\n");
report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");

      solve = 0;
        buf2 = -1;
while (rs.next()) {
        if(buf2 == rs.getInt(1)) {
          if(!(rs.getString(6)).equals("-")) sdano++;
        }
        else { 
               buf2 = rs.getInt(1);
               sdano=0;
               if(!(rs.getString(6)).equals("-")) sdano++;
        }

                AbiturientBean abit_TMP = new AbiturientBean();             
      if(solve == 0) {
  
report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2000\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4500\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9000\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");
          
                      str2  = rs.getString(6);
                  if (!str2.equals("-"))
                  b=b+rs.getInt(6);
                      solve++;
                   } else if(solve == 1) {

                            str4 = rs.getString(6);
                      if (!str4.equals("-"))
                      b=b+rs.getInt(6);
                            solve++;
                          } else if(solve == 2) {
                                   if (!(rs.getString(6)).equals("-"))
                                   b=b+rs.getInt(6);      
                              ots = new Integer(b); //конечное значение суммы
                              solve=0;

                              if(sb.intValue()!=b) b=0;
                                    else      priznak1=true;
                                    }// if(solve==2)

                              if(sb.intValue()==b && priznak1==true)
                           {
                             abit_TMP.setNumber(Integer.toString(++number));
                              ind=number;
                                     abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));

                                     abit_TMP.setNomerLichnogoDela(rs.getString(2));
                                     abit_TMP.setFamilija(rs.getString(3));
                                     abit_TMP.setImja(rs.getString(4));
                                     abit_TMP.setOtchestvo(rs.getString(5));
                              
                                        abit_TMP.setSpecial8(sdano+"");
                              abits_SD.add(abit_TMP);
                             priznak1=false;
                              report.write("\\intbl "+number+"\\cell\n");
                              report.write("\\intbl "+rs.getString(2)+"\\cell\n");
                              report.write("\\intbl \\ql "+rs.getString(3)+"\\cell\n");
                              report.write("\\intbl " +rs.getString(4)+"\\cell\n");
                              report.write("\\intbl " +rs.getString(5)+"\\cell\n");
                              report.write("\\intbl \\qc "+sdano+"\\cell\n");
                              report.write("\\intbl \\row\n");                           
                              b=0; solve =0;
}                              
                 }

///////////////////////////////////////////////////////////
     
report.write("}"); 
report.close();
form.setAction(us.getClientIntName("new_rep","crt"));
return mapping.findForward("rep_brw");


//*********************************######################################################
            } else if ( form.getAction().equals("viewing")) {

/************************************************************************************************/
/*****  Если action равен viewing , то входим в секцию - создание записи или вывод таблицы  *****/
/************************************  Просмотр/Модификация **************************************/

      int number = 0;
      form.setAction(us.getClientIntName("full","view"));
// Сохранение кода специальности 
      if(abit_SD.getSpecial1()!=null)
        abit_SD.setKodSpetsialnosti(new Integer(abit_SD.getSpecial1().substring(0,abit_SD.getSpecial1().indexOf("%"))));
      sb=abit_SD.getSpecial23();
      abit_SD.setSum_bal(sb);

// заполнение временных строк кодами предметов
      stmt = conn.prepareStatement("SELECT NazvanijaPredmetov.KodPredmeta FROM NazvanijaPredmetov,EkzamenyNaSpetsialnosti WHERE NazvanijaPredmetov.KodPredmeta = EkzamenyNaSpetsialnosti.KodPredmeta AND KodSpetsialnosti LIKE ?");
      stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) {
          pr11 = rs.getString(1);
          rs.next();
          pr22 = rs.getString(1);
          rs.next();
          pr33 = rs.getString(1);
        }

/*************************** Общее число абитуриентов ****************************/
        count_ab = 0;
      stmt = conn.prepareStatement("SELECT a.KodAbiturienta FROM Abiturient a,Otsenki o WHERE a.KodAbiturienta = o.KodAbiturienta AND o.KodPredmeta IN(?,?,?) AND a.KodSpetsialnosti LIKE ?");
      stmt.setObject(1,new Integer(pr11),Types.INTEGER);
      stmt.setObject(2,new Integer(pr22),Types.INTEGER);
      stmt.setObject(3,new Integer(pr33),Types.INTEGER);
      stmt.setObject(4,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) count_ab++;
        abit_SD.setSpecial22(new Integer(count_ab/3));

//************************** Считываем сокращения предметов ********************************/
      stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens WHERE np.KodPredmeta = ens.KodPredmeta AND ens.KodSpetsialnosti LIKE ? ORDER BY ens.KodPredmeta ASC");
      stmt.setObject(1,abit_SD.getKodSpetsialnosti(),Types.INTEGER);
      rs = stmt.executeQuery();
      while (rs.next())
      {
       AbiturientBean abit_TMP = new AbiturientBean();
       abit_TMP.setPredmet(rs.getString(1));
       abit_SD_S4.add(abit_TMP);
      }

//************************** Выбранный Балл ********************************/
      abit_SD.setSpecial4(abit_SD.getSpecial23()+"");


//****** Формируем SQL - запрос для выборки сведений по абитуриентам

        count_ab=0;
        int for_compare = StringUtil.toInt(abit_SD.getSpecial23()+"",-1);
      stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,o.Otsenka FROM Abiturient a,Otsenki o WHERE a.KodAbiturienta = o.KodAbiturienta AND o.KodPredmeta IN (?,?,?) AND a.KodSpetsialnosti LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,o.KodPredmeta ORDER BY a.Familija,a.Imja,a.Otchestvo,o.KodPredmeta ASC");
      stmt.setObject(1,new Integer(pr11),Types.INTEGER); // pre
      stmt.setObject(2,new Integer(pr22),Types.INTEGER); // dme
      stmt.setObject(3,new Integer(pr33),Types.INTEGER); // ty
      stmt.setObject(4,abit_SD.getKodSpetsialnosti(),Types.INTEGER); // kodSpetsialnosti
      rs = stmt.executeQuery();
      solve = 0;
        sdano =0;
// Заполняем массив данными и сохраняем их в bean
        buf2 = -1;
while (rs.next()) {
        if(buf2 == rs.getInt(1)) {
          if(!(rs.getString(6)).equals("-")) sdano++;
        }
        else { 
               buf2 = rs.getInt(1);
               sdano=0;
               if(!(rs.getString(6)).equals("-")) sdano++;
        }
                AbiturientBean abit_TMP = new AbiturientBean();             
      if(solve == 0) {            
                      str2  = rs.getString(6);
                  if (!str2.equals("-"))
                  a=a+rs.getInt(6);
                      solve++;
                   } else if(solve == 1) {
                            str4 = rs.getString(6);
                      if (!str4.equals("-"))
                      a=a+rs.getInt(6);
                            solve++;
                          } else if(solve == 2) {
                                   if(!(rs.getString(6)).equals("-"))
                              a=a+rs.getInt(6);      
                              ots = new Integer(a); //конечное значение суммы баллов
//**************************************************
// Общее количество абитуриентов, набравших N баллов
if(a == for_compare) count_ab++;
                              solve=0;

                              if(sb.intValue()!=a) a=0;
                                    else      priznak1=true;
                                    }// if(solve==2)

                              if(sb.intValue()==a && priznak1==true)
                           {
                             abit_TMP.setNumber(Integer.toString(++number));
                                     nb=number;
                             abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
                                     abit_TMP.setNomerLichnogoDela(rs.getString(2));
                                     abit_TMP.setFamilija(rs.getString(3));
                                     abit_TMP.setImja(rs.getString(4));
                                     abit_TMP.setOtchestvo(rs.getString(5));
                             abit_TMP.setSpecial22(new Integer(a));
                                        abit_TMP.setSpecial8(sdano+"");
                             abits_SD.add(abit_TMP);
                             priznak1=false;
                             a=0;
                           }
                           
                           

                 }
abit_SD.setSpecial3(count_ab+"");
      
//********************************************************************************************/

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
        if(rep_list_spec_f) return mapping.findForward("rep_list_spec_f");
        return mapping.findForward("success");
    }
}