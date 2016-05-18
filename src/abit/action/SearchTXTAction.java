package abit.action;
import java.util.Enumeration;
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
import abit.util.StringUtil;
import java.util.*;
import java.io.*;
import abit.sql.*; 
import java.net.URL;
import java.net.HttpURLConnection;

public class SearchTXTAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        Statement         stmnt         = null;
        ResultSet         rs            = null;
        ResultSet         rs1           = null;     
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        AbiturientForm    form          = (AbiturientForm) actionForm;
        AbiturientBean    abit_Srch     = form.getBean(request, errors);
        boolean           error         = false;
        ActionForward     f             = null;
        ArrayList         abits_Srch    = new ArrayList();
        ArrayList         lists_3       = new ArrayList();
        ArrayList         lists_4       = new ArrayList();
        UserBean          user          = (UserBean)session.getAttribute("user");
        int               file_size     = 0;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "searchTxtAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "searchForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************** Подготовка данных для ввода с помощью селекторов ************************/
      if ( form.getAction() == null ) 
      {

       StringBuffer query = new StringBuffer("SELECT DISTINCT np.Sokr,np.Predmet,np.Kodpredmeta,np.IdFBS FROM Nazvanijapredmetov np,EkzamenyNaSpetsialnosti ens,Fakultety f,Abiturient a,Spetsialnosti s WHERE np.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta = f.KodFakulteta AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodVuza = "+session.getAttribute("kVuza")+" ORDER BY np.IdFBS ASC");

       StringBuffer condition = new StringBuffer();
       condition.append(" AND Fakultety.ShifrFakulteta LIKE "+"'"+session.getAttribute("ShifrFakulteta")+"'");
       if((session.getAttribute("KodSpetsialnZach")+"").equals("%"))
         condition.append(" AND Spetsialnosti.KodSpetsialnosti LIKE "+"'"+session.getAttribute("Special1")+"'");
       else
         condition.append(" AND Spetsialnosti.KodSpetsialnosti LIKE "+"'"+session.getAttribute("KodSpetsialnZach")+"'");
       condition.append(" AND Abiturient.NomerLichnogoDela LIKE "+"'"+session.getAttribute("NomerLichnogoDela")+"'");
       condition.append(" AND Familija LIKE "+"'"+session.getAttribute("Familija")+"'");
       condition.append(" AND Imja LIKE "+"'"+session.getAttribute("Imja")+"'");
       condition.append(" AND Otchestvo LIKE "+"'"+session.getAttribute("Otchestvo")+"'");
       condition.append(" AND (Prinjat IN ("+session.getAttribute("Prinjat")+") OR Prinjat IS NULL)");

// ВЫВОДИМ ВСЕ ПРЕДМЕТЫ ВНЕ ЗАВИСИМОСТИ ОТ СПЕЦ-ТИ, ФАКУЛЬТЕТА и т.п.

//       query.append(condition.toString());

       stmt = conn.prepareStatement(query.toString());
       rs = stmt.executeQuery();
       while(rs.next())
        {
         AbiturientBean list_3 = new AbiturientBean();
         list_3.setNazvaniePredmeta(rs.getString(1));
         list_3.setPredmet(rs.getString(3));
         lists_3.add(list_3);
        }
             
       stmt = conn.prepareStatement(query.toString());
       rs = stmt.executeQuery();
       while(rs.next())
        {
         AbiturientBean list_4 = new AbiturientBean();
         list_4.setNazvaniePredmeta(rs.getString(1));
         list_4.setPredmet(rs.getString(3));
         lists_4.add(list_4);
        }
     
       form.setAction(us.getClientIntName("printForm","init"));
      }
      else if ( form.getAction().equals("makeTXT") ) 
      {

// СОЗДАНИЕ ТЕКСТОВОГО ФАЙЛА

       String resStr=""; 
       String str ="";
       StringTokenizer st = new StringTokenizer(request.getParameter("resultString"),"%");  

       ArrayList names    = new ArrayList();//массив содержащий имена полей(кроме оценок)
       ArrayList predmety = new ArrayList();//массив содержащий имена полей предметов
       ArrayList ots      = new ArrayList();//массив содержащий названия полей, которые будут подставлены в запрос
       ArrayList ots1     = new ArrayList();//массив содержащий значения которые содержаться в рез-й строке и которые нужно заменить
       ArrayList ots2     = new ArrayList();//массив содержащий коды предметов
       String ch=(request.getParameter("resultString"));

// Разбор результирующей строки и заполнение массивов

       while(st.hasMoreTokens())
        { 
         str=st.nextToken();
         if(str.equals("%")==false)
          {
           if(str.startsWith("predmet")==true){ predmety.add(str); }
           else if(str.startsWith("exzamOtsenkaPo")==true){ots.add("otsenka");ots1.add(str);ots2.add(str.substring(str.indexOf("(")+1,str.indexOf(")")));}
           else if(str.startsWith("egePo")==true){ots.add("otsenkaEge");ots1.add(str);ots2.add(str.substring(str.indexOf("(")+1,str.indexOf(")")));}
           else if(str.startsWith("zaiavlOtsenkaPo")==true){ots.add("otsenkaZajavl");ots1.add(str);ots2.add(str.substring(str.indexOf("(")+1,str.indexOf(")")));}
           else if(str.startsWith("atestatOtsenkaPo")==true){ots.add("otsenkaAtt");ots1.add(str);ots2.add(str.substring(str.indexOf("(")+1,str.indexOf(")")));}
           else if(str.startsWith("zagolovok")==false)
            {
             if(str.startsWith("sobesedovanie")){resStr+="Abiturient."+str+',';}
             if(str.startsWith("formaOb")) { resStr += "Forma_Obuch.Sokr,"; names.add(str);  continue;}
             if(str.startsWith("osnovaOb")){ resStr += "Osnova_Obuch.Sokr,"; names.add(str); continue;}
             if(str.startsWith("kodSpetsialnZach")){ resStr += "Abiturient.KodSpetsialnZach,"; names.add(str); continue;}

             if(str.startsWith("nomerLichnogoDela")) {
               if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                 { resStr += "Konkurs.NomerLichnogoDela,"; names.add(str); continue;}
               else
                 { resStr += "Abiturient.NomerLichnogoDela,"; names.add(str); continue;}
             }
             resStr+=str+',';
             names.add(str);
            }
          }
	    } 
 		if(resStr.equals("")==true) resStr="*,";

/****************************** Выборка абитуриентов из БД ******************************/

                    StringBuffer query = new StringBuffer("SELECT "+resStr+"KodAbiturienta,Abiturient.KodSpetsialnZach FROM Forma_Obuch,Osnova_Obuch,TselevojPriem,Abiturient,Lgoty,Medali,Kursy,Spetsialnosti,Fakultety,Punkty,Rajony,Oblasti,Zavedenija,Gruppy");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append(",Konkurs");

                    query.append(" WHERE ");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND ");
                    else
                      query.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND ");

                    query.append("Forma_Obuch.KodFormyOb=Abiturient.KodFormyOb AND Osnova_Obuch.KodOsnovyOb=Abiturient.KodOsnovyOb AND Gruppy.KodGruppy=Abiturient.KodGruppy AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodPunkta = Punkty.KodPunkta AND Abiturient.KodRajona = Rajony.KodRajona AND Abiturient.KodOblasti = Oblasti.KodOblasti AND Abiturient.KodLgot = Lgoty.KodLgot AND Abiturient.KodMedali = Medali.KodMedali AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodKursov = Kursy.KodKursov AND TselevojPriem.KodTselevogoPriema=Abiturient.KodTselevogoPriema AND Abiturient.KodVuza = ");
                    query.append(session.getAttribute("kVuza"));

//******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

                    StringBuffer condition = new StringBuffer();
                   if(!(""+session.getAttribute("SeriaAtt")).equals("%"))
                    condition.append(" AND (SeriaAtt LIKE "+"'"+session.getAttribute("SeriaAtt")+"' OR SeriaAtt IS NULL)");
                   if(!(""+session.getAttribute("NomerAtt")).equals("%"))
                    condition.append(" AND (NomerAtt LIKE "+"'"+session.getAttribute("NomerAtt")+"' OR NomerAtt IS NULL)");
                   if(!(""+session.getAttribute("KodOsnovyOb")).equals("0"))
                    condition.append(" AND Abiturient.KodOsnovyOb LIKE "+"'"+session.getAttribute("KodOsnovyOb")+"'");
                   if(!(""+session.getAttribute("KodFormyOb")).equals("0"))
                    condition.append(" AND Abiturient.KodFormyOb LIKE "+"'"+session.getAttribute("KodFormyOb")+"'");
                   if(!(""+session.getAttribute("DokumentyHranjatsja")).equals("%"))
                    condition.append(" AND DokumentyHranjatsja LIKE "+"'"+session.getAttribute("DokumentyHranjatsja")+"'");
                   if(!(""+session.getAttribute("ShifrFakulteta")).equals("%"))
                    condition.append(" AND Fakultety.ShifrFakulteta LIKE "+"'"+session.getAttribute("ShifrFakulteta")+"'");
                    condition.append(" AND Spetsialnosti.KodSpetsialnosti LIKE "+"'"+session.getAttribute("Special1")+"'");
                   if(!(""+session.getAttribute("NomerLichnogoDela")).equals("%")) {
                     if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                       condition.append(" AND Konkurs.NomerLichnogoDela LIKE "+"'"+session.getAttribute("NomerLichnogoDela")+"'");
                     else
                       condition.append(" AND Abiturient.NomerLichnogoDela LIKE "+"'"+session.getAttribute("NomerLichnogoDela")+"'");
                   }
                   if(!(""+session.getAttribute("Familija")).equals("%"))
                    condition.append(" AND Familija LIKE "+"'"+session.getAttribute("Familija")+"'");
                   if(!(""+session.getAttribute("Imja")).equals("%"))
                    condition.append(" AND Imja LIKE "+"'"+session.getAttribute("Imja")+"'");
                   if(!(""+session.getAttribute("Otchestvo")).equals("%"))
                    condition.append(" AND Otchestvo LIKE "+"'"+session.getAttribute("Otchestvo")+"'");
                   if(!(""+session.getAttribute("ShifrKursov")).equals("%"))
                    condition.append(" AND (ShifrKursov IN(" + session.getAttribute("ShifrKursov")+") OR ShifrKursov IS NULL)");
                   if(!(""+session.getAttribute("ShifrMedali")).equals("%"))
                    condition.append(" AND (ShifrMedali IN(" + session.getAttribute("ShifrMedali")+") OR ShifrMedali IS NULL)");
                   if(!(""+session.getAttribute("ShifrLgot")).equals("%"))
                    condition.append(" AND (ShifrLgot IN(" + session.getAttribute("ShifrLgot")+") OR ShifrLgot IS NULL)");
                   if(!(""+session.getAttribute("NomerPlatnogoDogovora")).equals("%"))
                    condition.append(" AND (" + session.getAttribute("NomerPlatnogoDogovora")+session.getAttribute("PriznakDog")+")");
                   if(!((""+session.getAttribute("DataRojdenija1")).equals("00-00-0000") && (""+session.getAttribute("DataRojdenija2")).equals("99-99-9999"))) {
                    condition.append(" AND (DataRojdenija >= "+"'"+session.getAttribute("DataRojdenija1")+"'");
                    condition.append(" AND DataRojdenija <= "+"'"+session.getAttribute("DataRojdenija2")+"'"+" OR DataRojdenija IS NULL)");
                   }
                   if(!(""+session.getAttribute("Pol")).equals("%"))
                    condition.append(" AND (Pol LIKE "+"'"+session.getAttribute("Pol")+"'"+" OR Pol IS NULL)");
                   if(!(""+session.getAttribute("SrokObuchenija")).equals("%"))
                    condition.append(" AND (SrokObuchenija LIKE "+"'"+session.getAttribute("SrokObuchenija")+"'"+" OR SrokObuchenija IS NULL)");
                   if(!((""+session.getAttribute("GodOkonchanijaSrObrazovanija1")).equals("1950") && (""+session.getAttribute("GodOkonchanijaSrObrazovanija2")).equals("9999"))) {
                    condition.append(" AND (GodOkonchanijaSrObrazovanija >= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija1")+"'");
                    condition.append(" AND GodOkonchanijaSrObrazovanija <= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija2")+"'"+" OR GodOkonchanijaSrObrazovanija IS NULL)");
                   }
                   if(!(""+session.getAttribute("GdePoluchilSrObrazovanie")).equals("%"))
                    condition.append(" AND (GdePoluchilSrObrazovanie LIKE "+"'"+session.getAttribute("GdePoluchilSrObrazovanie")+"'"+" OR GdePoluchilSrObrazovanie IS NULL)");
                   if(!(""+session.getAttribute("NomerShkoly")).equals("%"))
                    condition.append(" AND (NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'"+" OR NomerShkoly IS NULL)");
                   if(!(""+session.getAttribute("InostrannyjJazyk")).equals("%"))
                    condition.append(" AND (InostrannyjJazyk LIKE "+"'"+session.getAttribute("InostrannyjJazyk")+"'"+" OR InostrannyjJazyk IS NULL)");
                   if(!(""+session.getAttribute("NujdaetsjaVObschejitii")).equals("%"))
                    condition.append(" AND (NujdaetsjaVObschejitii LIKE "+"'"+session.getAttribute("NujdaetsjaVObschejitii")+"'"+" OR NujdaetsjaVObschejitii IS NULL)");
                   if(!(""+session.getAttribute("Grajdanstvo")).equals("%"))
                    condition.append(" AND (Grajdanstvo LIKE "+"'"+session.getAttribute("Grajdanstvo")+"'"+" OR Grajdanstvo IS NULL)");
                   if(!(""+session.getAttribute("Nazvanie")).equals("%"))
                    condition.append(" AND (Punkty.Nazvanie LIKE "+"'"+session.getAttribute("Nazvanie")+"'"+" OR Punkty.Nazvanie IS NULL)");
                   if(!(""+session.getAttribute("NazvanieRajona")).equals("%"))
                    condition.append(" AND (Rajony.NazvanieRajona LIKE "+"'"+session.getAttribute("NazvanieRajona")+"'"+" OR NazvanieRajona IS NULL)");
                   if(!(""+session.getAttribute("NazvanieOblasti")).equals("%"))
                    condition.append(" AND (Oblasti.NazvanieOblasti LIKE "+"'"+session.getAttribute("NazvanieOblasti")+"'"+" OR NazvanieOblasti IS NULL)");
                   if(!(""+session.getAttribute("PolnoeNaimenovanieZavedenija")).equals("%"))
                    condition.append(" AND (PolnoeNaimenovanieZavedenija LIKE "+"'"+session.getAttribute("PolnoeNaimenovanieZavedenija")+"'"+" OR PolnoeNaimenovanieZavedenija IS NULL)");
                   if(!(""+session.getAttribute("TipOkonchennogoZavedenija")).equals("%"))
                    condition.append(" AND (TipOkonchennogoZavedenija LIKE "+"'"+session.getAttribute("TipOkonchennogoZavedenija")+"'"+" OR TipOkonchennogoZavedenija IS NULL)");
                   if(!(""+session.getAttribute("TrudovajaDejatelnost")).equals("%"))
                    condition.append(" AND (TrudovajaDejatelnost LIKE "+"'"+session.getAttribute("TrudovajaDejatelnost")+"'"+" OR TrudovajaDejatelnost IS NULL)");
                   if(!(""+session.getAttribute("Gruppa")).equals("%"))
                    condition.append(" AND (Gruppa LIKE "+"'"+session.getAttribute("Gruppa")+"'"+" OR Gruppa IS NULL)");
                   if(!(""+session.getAttribute("NapravlenieOtPredprijatija")).equals("%"))
                    condition.append(" AND (NapravlenieOtPredprijatija LIKE "+"'"+session.getAttribute("NapravlenieOtPredprijatija")+"'"+" OR NapravlenieOtPredprijatija IS NULL)");
                   if(!(""+session.getAttribute("TipDokumenta")).equals("%"))
                    condition.append(" AND (TipDokumenta LIKE "+"'"+session.getAttribute("TipDokumenta")+"'"+" OR TipDokumenta IS NULL)");
                   if(!(""+session.getAttribute("NomerDokumenta")).equals("%"))
                    condition.append(" AND (NomerDokumenta LIKE "+"'"+session.getAttribute("NomerDokumenta")+"'"+" OR NomerDokumenta IS NULL)");
                   if(!(""+session.getAttribute("SeriaDokumenta")).equals("%"))
                    condition.append(" AND (SeriaDokumenta LIKE "+"'"+session.getAttribute("SeriaDokumenta")+"'"+" OR SeriaDokumenta IS NULL)");
                   if(!((""+session.getAttribute("DataVydDokumenta1")).equals("00-00-0000") && (""+session.getAttribute("DataVydDokumenta2")).equals("99-99-9999"))) {
                    condition.append(" AND (DataVydDokumenta >= "+"'"+session.getAttribute("DataVydDokumenta1")+"'");
                    condition.append(" AND DataVydDokumenta <= "+"'"+session.getAttribute("DataVydDokumenta2")+"'"+" OR DataVydDokumenta IS NULL)");
                   }
                   if(!(""+session.getAttribute("KemVydDokument")).equals("%"))
                    condition.append(" AND (KemVydDokument LIKE "+"'"+session.getAttribute("KemVydDokument")+"'"+" OR KemVydDokument IS NULL)");
                   if(!(""+session.getAttribute("TipDokSredObraz")).equals("%"))
                    condition.append(" AND (TipDokSredObraz LIKE "+"'"+session.getAttribute("TipDokSredObraz")+"'"+" OR TipDokSredObraz IS NULL)");
                   if(!(""+session.getAttribute("Sobesedovanie")).equals("%"))
                    condition.append(" AND (Abiturient.Sobesedovanie LIKE "+"'"+session.getAttribute("Sobesedovanie")+"'"+" OR Abiturient.Sobesedovanie IS NULL)");
                    condition.append(" AND TselevojPriem.KodTselevogoPriema LIKE "+"'"+session.getAttribute("KodTselevogoPriema")+"'");
                   if(!(""+session.getAttribute("NomerSertifikata")).equals("%"))
                    condition.append(" AND (NomerSertifikata LIKE '"+session.getAttribute("NomerSertifikata")+"'"+" OR NomerSertifikata IS NULL)");
                   if(!(""+session.getAttribute("KopijaSertifikata")).equals("%"))
                    condition.append(" AND (KopijaSertifikata LIKE '"+session.getAttribute("KopijaSertifikata")+"'"+" OR KopijaSertifikata IS NULL)");
                   if(!(""+session.getAttribute("Ball")).equals("%"))
                    condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"'"+" OR Ball IS NULL)");
                   if((session.getAttribute("KodSpetsialnZach")+"").equals("%"))
                     condition.append(" AND (Abiturient.KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"' OR Abiturient.KodSpetsialnZach IS NULL)");
                   else
                     condition.append(" AND (Abiturient.KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"')");
                   if(!(""+session.getAttribute("Prinjat")).equals("%"))
                    condition.append(" AND (Prinjat IN(" + session.getAttribute("Prinjat")+") OR Prinjat IS NULL)");
//************************************************
                    query.append(condition);
                    query.append("ORDER BY "+ resStr.substring(0,resStr.length()-1));
//System.out.println(query);
        stmnt = conn.createStatement();

        rs = stmnt.executeQuery(query.toString());

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

        String name = "Файл пакетной проверки: "+StringUtil.ntv(abit_Srch.getSpecial3())+" от "+StringUtil.CurrDate(".")+" "+StringUtil.CurrTime(":");

        String file_con = new String("file"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

        session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"csv"));

        String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+"\\reports\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

        BufferedWriter report = new BufferedWriter(new FileWriter(file_name));

        int j,beg,end;

        String predmets = new String();

        while(rs.next())
         {        
          ch=(request.getParameter("resultString"));
          for(j=0;j<names.size();j++)
           {
            beg=ch.indexOf(String.valueOf(names.get(j)));
            end=ch.indexOf(String.valueOf(names.get(j)))+String.valueOf(names.get(j)).length();
            if(rs.getString(j+1)!=null)//если выбранное значение != null
             {

// !!!! ВНИМАНИЕ !!! ниже указано имя поля - код спец-ти для его перевода в аббревиатуру
//System.out.println(">>>kSpecZach>>0<<");
               if(names.get(j).equals("kodSpetsialnZach")) {

/*** Получение Аббревиатуры спец-ти по ее коду ***/
//System.out.println(">>>kSpecZach>>1<<");
                  String abbr_spec = new String();
                  stmt = conn.prepareStatement("SELECT DISTINCT s.Abbreviatura FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND s.KodSpetsialnosti LIKE ?");
                  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                  stmt.setObject(2,rs.getString(j+1),Types.INTEGER);
                  rs1 = stmt.executeQuery();
                  if(rs1.next()) abbr_spec = rs1.getString(1);
                  else           abbr_spec = "";
//Замещение
                  if(beg>=0 && end>0){ch = new StringBuffer(ch).replace(beg,end,abbr_spec).toString();}

               } else
                  if(beg>=0 && end>0){ch = new StringBuffer(ch).replace(beg,end,rs.getString(j+1)).toString();}
             }

            else{if(beg>=0 && end>0)ch = new StringBuffer(ch).replace(beg,end,"null").toString();}

// Формирование строки предметов согласно выбору пользователя

            if(!(abit_Srch.getSpecial1() != null && abit_Srch.getSpecial1().equals("on"))) {

// выбираем предметы и заменяем их

              int a;
              for(a=0;a<predmety.size();a++)
               {
                beg=ch.indexOf(String.valueOf(predmety.get(a)));
                end=ch.indexOf(String.valueOf(predmety.get(a)))+String.valueOf(predmety.get(a)).length();
                stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov where KodPredmeta LIKE ?");
                String tmp = String.valueOf(predmety.get(a));
                stmt.setObject(1,tmp.substring(tmp.indexOf("(")+1,tmp.indexOf(")")),Types.INTEGER);
                rs1 = stmt.executeQuery();
                if(rs1.next())
        	{
        	  if(beg>=0 && end>0){ch = new StringBuffer(ch).replace(beg,end,rs1.getString(1)).toString();}
        	} 
               }

// выбираем и заменяем оценки 

               int b;
               for(b=0;b<ots.size();b++)
               {
                 beg=ch.indexOf(String.valueOf(ots1.get(b)));
                 end=ch.indexOf(String.valueOf(ots1.get(b)))+String.valueOf(ots1.get(b)).length();
                 String qer="SELECT "+String.valueOf(ots.get(b))+" FROM Otsenki,nazvanijapredmetov,zajavlennyeshkolnyeotsenki where nazvanijapredmetov.kodpredmeta=otsenki.kodpredmeta and otsenki.kodpredmeta LIKE ? and otsenki.kodabiturienta LIKE ? and zajavlennyeshkolnyeotsenki.kodpredmeta LIKE ? and zajavlennyeshkolnyeotsenki.kodabiturienta LIKE ? ";
                 stmt = conn.prepareStatement(qer);
                 stmt.setObject(1,ots2.get(b),Types.INTEGER);
                 stmt.setObject(2,new Integer(rs.getString(names.size()+1)),Types.INTEGER);
                 stmt.setObject(3,ots2.get(b),Types.INTEGER);
                 stmt.setObject(4,new Integer(rs.getString(names.size()+1)),Types.INTEGER);
                 rs1 = stmt.executeQuery();
    	         if(rs1.next())
    	         {
                   if(beg>=0 && end>0){ch = new StringBuffer(ch).replace(beg,end,rs1.getString(1)).toString();}
                 }
               }
            }

           }

           report.write(ch);
           report.write("\n");

// Формирование строки предметов автоматически согласно требованиям ФБС ЕГЭ и приёма
// Согласно ФБС ЕГЭ и приёма строка предметов включает в себя:
// <Русский язык>%<Математика>%<Физика>%<Химия>%<Биология>%<История России>%<География>%<Английский язык>%<Немецкий язык>%<Французский язык>%<Обществознание>%<Литература>%<Испанский язык>%<Информатика>
// Содержимое БД включает не все предметы, поэтому вывод сопровождается спец. счетчиком для упорядочивания данных строки

           if(abit_Srch.getSpecial1() != null && abit_Srch.getSpecial1().equals("on")) {

             int cntr = 0;
             predmets = "";
             stmt = conn.prepareStatement("SELECT zso.KodPredmeta,zso.OtsenkaEge,np.Predmet,a.InostrannyjJazyk FROM Abiturient a,Otsenki o,NazvanijaPredmetov np,ZajavlennyeShkolnyeOtsenki zso WHERE  np.KodPredmeta=o.KodPredmeta AND o.KodPredmeta=zso.KodPredmeta AND zso.kodPredmeta Not In (12, 13) AND a.KodAbiturienta=o.KodAbiturienta AND o.KodAbiturienta=zso.KodAbiturienta AND o.KodAbiturienta LIKE ? ORDER BY np.IdFBS ASC");
             stmt.setObject(1,new Integer(rs.getString(names.size()+1)),Types.INTEGER);
             rs1 = stmt.executeQuery();
    	     while(rs1.next())
             {
               if( ++cntr == 8) {

                 if( rs1.getString(4).equals("а") ) predmets += StringUtil.voidFilter(rs1.getString(2))+"%%%";
                 else if( rs1.getString(4).equals("н") ) predmets += "%"+StringUtil.voidFilter(rs1.getString(2))+"%%";
                 else if( rs1.getString(4).equals("ф") ) predmets += "%%"+StringUtil.voidFilter(rs1.getString(2))+"%";

               } else if( cntr == 11) {

//Выводим последний балл по информатике с учётом % по испанскому. У нас его просто нет. В конце % не ставится

                 predmets += "%"+StringUtil.voidFilter(rs1.getString(2));

                 break;

               } else 

                 predmets += StringUtil.voidFilter(rs1.getString(2))+"%";
             }
           }

   //        report.write(predmets+"\n");
         

// Размер создаваемого файла

           file_size += ch.length();

         } //while
         
        report.close();

System.out.println("FileSIZE="+file_size);

/************************************************/
/*****    Отправка пакетного файла в ФИС    *****/
/************************************************/

        if( request.getParameter("toFIS") != null ) {

            String url = new String();

            String buf_line = new String();

            FileReader fr = new FileReader(file_name);

            BufferedReader rpt_reader = new BufferedReader(fr);

// Устанавливаем соединение с ФИС по адресу в зависимости от содержимого файла пакета
System.out.println("RESULT="+resStr);

            if(resStr.indexOf("nomerSertifikata") != -1)
              url = "http://10.0.3.1/AllUsers/BatchCheckFileFormatByPassport.aspx";
            else
              url = "http://10.0.3.1/AllUsers/BatchCheckFileFormatByNumber.aspx";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestProperty("Content-Length", ""+file_size);

            connection.setRequestProperty("Content-Type", "multipart/form-data");

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

// Устанавливаем время ожидания ответа сервера - 30 секунд

            connection.setReadTimeout(30*1000);

            connection.connect();

// Устанавливаем соединение и отсылаем запрос на сервер

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            
            while((buf_line = rpt_reader.readLine()) != null) wr.write(buf_line);
            System.out.println(buf_line);
            wr.flush();

// Получаем статус ответа сервера

            int status = connection.getResponseCode();
            System.out.println(status);

            buf_line = "";

// Читаем ответ сервера в буфер ввода-вывода

//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder sb = new StringBuilder();

            while ((buf_line = in.readLine()) != null) {
                  sb.append(buf_line);
            }

            buf_line = sb.toString();
System.out.println(buf_line);
            fr.close();

            connection.disconnect();
        }


        form.setAction(us.getClientIntName("new_rep","crt"));
        return mapping.findForward("rep_brw");
	}//Закрываем "makeTXT"
	
        }// Закрываем try
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
        request.setAttribute("abit_Srch", abit_Srch);
        request.setAttribute("abits_Srch", abits_Srch);
   		request.setAttribute("lists_3", lists_3);
        request.setAttribute("lists_4", lists_4);
   }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}
