package abit.action;

import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*; 

public class AbiturientAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
    	int tar = 1;
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        PreparedStatement    pstmt              = null;
        PreparedStatement    pstmt1              = null;
        ResultSet         rs            = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        AbiturientForm    form          = (AbiturientForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        MessageBean       mess          = new MessageBean();
        MessageBean       mess1          = new MessageBean();
        boolean           error         = false;
        boolean           re_enter      = false;
        ActionForward     f             = null;
        int               kPunkta       = 1;
        int               kRajona       = 1;
        int               kOblasti      = 1;
        int               kZavedenija   = 1;
        int               kAbiturienta  = 1;
        int               Col_Specs     = 0;
        double 				summa = 0;
        String            kFormy_Ob     = "1";
        String            kOsnovy_Ob    = "1";
        String            kSpec         = "0";
        String            kFak          = "0";
int pp=0;
        String            Abbr_Spec     = new String();
        String            Abbr_Spec2    = new String();
        String            Tip_Spec      = new String();
        String            Tip_Spec2     = new String();
        String            nld           = new String();
        String            nomer_ab      = new String();
        String            ordr_ab       = new String();
        String            shifr_Fak     = new String();
        String            two_t_names[] = {"Konkurs","Kontrol_Kon"};
        String            s_okso_1      = "";
        String            s_okso_2      = "";
        String            s_okso_3      = "";
        String            s_okso_4      = "";
        String            s_okso_5      = "";
        String            s_okso_6      = "";
        String sb = "Карточка поступающего на БАКАЛАВРИАТ/СПЕЦИАЛИТЕТ";
        String ma = "Карточка поступающего в МАГИСТРАТУРУ";
        String or = "Карточка поступающего в ОРДИНАТУРУ";
        String in = "Карточка поступающего в ИНТЕРНАТУРУ";
        String sp = "Карточка поступающего на СПО";
        String asp = "Карточка поступающего в АСПИРАНТУРУ";
        int Spec1=0;
        int Spec2=0;
        int Spec3=0;
        int Spec4=0;
        int Spec5=0;
        int Spec6=0;

        int countt=0;
        int chet=1;
        ArrayList         abits_A       = new ArrayList();
        ArrayList         abit_forms    = new ArrayList();
        ArrayList         abit_osnovs   = new ArrayList();
        ArrayList         abit_A_S1     = new ArrayList();
        ArrayList         abit_A_S4     = new ArrayList();
        ArrayList         abit_A_S5     = new ArrayList();
        ArrayList         abit_A_S6     = new ArrayList();
        ArrayList         abit_A_S7     = new ArrayList();
        ArrayList         abit_A_S8     = new ArrayList();
        ArrayList         abit_A_S9     = new ArrayList();
        ArrayList         abit_A_S10     = new ArrayList();
        ArrayList         abit_A_S11     = new ArrayList();
        ArrayList		  abit_A_Strany	= new ArrayList();
        ArrayList         abit_A_Kladr     = new ArrayList();
        ArrayList         abit_A_Rajon     = new ArrayList();
        ArrayList         abit_A_Punkt     = new ArrayList();
        ArrayList         obr_A_Rajon     = new ArrayList();
        ArrayList         obr_A_Punkt     = new ArrayList();
        
        int nomerPotoka=0;
        String zapros=null;
        
        
        ArrayList		  nationalityList = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S3         = new ArrayList();
        ArrayList            abit_SD_S11         = new ArrayList();
        UserBean          user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || !(user.getGroup().getTypeId()==1 || user.getGroup().getTypeId()==3)) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "abiturientAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "abiturientForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          if ( form.getAction() == null ) {
        	  form.setAction(us.getClientIntName("choice","init"));
            }
          
          
          if ( form.getAction().equals("chosen") ) {
         	form.setAction(us.getClientIntName("new","init"));
         	abit_A.setNomerPotoka(abit_A.getNomerPotoka());
         	if(abit_A.getNomerPotoka()==1){
         		 mess.setStatus(sb);
         	}
         	if(abit_A.getNomerPotoka()==2){
         		 mess.setStatus(ma);
         	}
         	if(abit_A.getNomerPotoka()==3){
         		 mess.setStatus(in);
         	}
         	if(abit_A.getNomerPotoka()==4){
         		 mess.setStatus(or);
         	}
         	if(abit_A.getNomerPotoka()==5){
         		 mess.setStatus(asp);
         	}
         	if(abit_A.getNomerPotoka()==6){
         		 mess.setStatus(sp);
         	}
          
/********************** Подготовка данных для ввода с помощью селекторов ************************/
          
          /*********<КЛАДР ПУШКАРЕВ*******************/ 
          
          stmt = conn.prepareStatement("SELECT CODE, socr, name FROM KLADR WHERE CODE Like '__000000000__'");
        //  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setSpecial27(rs.getString(1));
            abit_TMP.setSpecial28(rs.getString(2)+" "+rs.getString(3));
            abit_A_Kladr.add(abit_TMP);
          }
          /*********КЛАДР ПУШКАРЕВ>*******************/ 
          
          nomerPotoka=abit_A.getNomerPotoka();
		  if(nomerPotoka==1){
			  zapros="('с','б')";
		  }else if(nomerPotoka==2){
			  zapros="('м')";
		  }else if(nomerPotoka==3){
			  zapros="('и')";
		  }else if(nomerPotoka==4){
			  zapros="('о')";
		  }else if(nomerPotoka==5){
			  zapros="('а')";
		  }else if(nomerPotoka==6){
			  zapros="('п')";
		  }
          
          pstmt = conn.prepareStatement("SELECT DISTINCT f.KodFakulteta,f.AbbreviaturaFakulteta,f.Fakultet FROM Fakultety f, Spetsialnosti s WHERE KodVuza LIKE ? AND f.kodfakulteta=s.kodfakulteta AND s.tip_spec like 'о' AND s.edulevel IN "+zapros+" ORDER BY AbbreviaturaFakulteta ASC");
          pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodFakulteta(rs.getInt(1));
            abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
            abit_SD_S1.add(abit_TMP);
          }
          
          pstmt = conn.prepareStatement("SELECT DISTINCT f.KodFakulteta,f.AbbreviaturaFakulteta,f.Fakultet FROM Fakultety f, Spetsialnosti s WHERE KodVuza LIKE ? AND f.kodfakulteta=s.kodfakulteta AND s.tip_spec not like 'о' AND s.edulevel IN "+zapros+" ORDER BY AbbreviaturaFakulteta ASC");
          pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodFakulteta(rs.getInt(1));
            abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
            abit_SD_S11.add(abit_TMP);
          }

          stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.KodFakulteta = s.KodFakulteta AND f.KodVuza LIKE ? AND s.Tip_Spec like 'о' ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
        	  AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial2(rs.getString(1)+"+"+rs.getInt(2)+"%");
              abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
              abit_SD_S2.add(abit_TMP);
            }
     
          stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.Tip_Spec not like 'о' AND f.KodFakulteta = s.KodFakulteta AND f.KodVuza LIKE ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
        	  AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial2(rs.getString(1)+"+"+rs.getInt(2)+"%");
              abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
              abit_SD_S3.add(abit_TMP);
            }
          
            stmt = conn.prepareStatement("SELECT DISTINCT KodLgot,ShifrLgot FROM Lgoty WHERE KodVuza LIKE ? ORDER BY 1 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodLgot(new Integer(rs.getInt(1)));
              abit_TMP.setShifrLgot(rs.getString(2));
              abit_A_S4.add(abit_TMP);
            }
            
            stmt = conn.prepareStatement("SELECT DISTINCT nazvanie FROM Strany");
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setNazv_DipBak(rs.getString(1));
              abit_A_Strany.add(abit_TMP);
            }

            stmt = conn.prepareStatement("SELECT DISTINCT KodMedali,ShifrMedali FROM Medali WHERE KodVuza LIKE ? ORDER BY 1 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodMedali(new Integer(rs.getInt(1)));
              abit_TMP.setShifrMedali(rs.getString(2));
              abit_A_S6.add(abit_TMP);
            }

            abit_A.setSpecial1("");
            stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? and kodpredmeta not in ('13','12') ORDER BY KodPredmeta ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
              abit_A.setSpecial1(abit_A.getSpecial1()+"%"+rs.getString(1));
              abit_TMP.setPredmet((rs.getString(2)).substring(0,1).toUpperCase()+(rs.getString(2)).substring(1));
              abit_A_S7.add(abit_TMP);
            }
            
            stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? and  (KodPredmeta = 3 or KodPredmeta = 10 or KodPredmeta = 5 or KodPredmeta = 1 or KodPredmeta = 8 or KodPredmeta = 4 or KodPredmeta = 9 or KodPredmeta = 2) ORDER BY KodPredmeta ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
             // abit_A.setSpecial1(abit_A.getSpecial1()+"%"+rs.getString(1));
              abit_TMP.setPredmet((rs.getString(2)).substring(0,1).toUpperCase()+(rs.getString(2)).substring(1));
              abit_A_S10.add(abit_TMP);
            }

            stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodZavedenija,Ordr FROM Zavedenija WHERE KodVuza LIKE ? ORDER BY Ordr,Sokr ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSokr(rs.getString(1));
              abit_TMP.setKodZavedenija(new Integer(rs.getInt(2)));
              abit_A_S8.add(abit_TMP);
            }
            
            
            /*Пушкарев добавление классификатора Национальностей 20 03 2014*/
            stmt = conn.prepareStatement("SELECT DISTINCT ID, name from DictionaryCode21 order by name ");
            rs = stmt.executeQuery();
            while (rs.next()) {
            NationalityBean nationalityBean = new NationalityBean();
            nationalityBean.setId((Integer)rs.getInt(1));
            nationalityBean.setName(rs.getString(2));
            if (nationalityBean.getId() == 1){
            	nationalityList.add(0, nationalityBean);
            }
            if (nationalityBean.getId() == 2){
            	nationalityList.add(0, nationalityBean);
            }
            else
            nationalityList.add(nationalityBean);
            
            
            }

            stmt = conn.prepareStatement("SELECT DISTINCT KodTselevogoPriema,ShifrPriema FROM TselevojPriem WHERE KodVuza LIKE ? ORDER BY 1 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodTselevogoPriema(new Integer(rs.getInt(1)));
              abit_TMP.setShifrPriema(rs.getString(2));
              abit_A_S9.add(abit_TMP);
            }
          }
/************************************************************************************************/
/************************************** Создание записи *****************************************/
/************************************************************************************************/

   if ( form.getAction().equals("create") ) {

/************************************************************************************************/
/*************************** Подготовка вспомогательных переменных ******************************/
/************************************************************************************************/

// Начало транзакции

     conn.setAutoCommit(false);

// Проверка на уникальность по паспортным данным

    /* stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE SeriaDokumenta LIKE ? AND NomerDokumenta LIKE ?");
     stmt.setObject(1,abit_A.getSeriaDokumenta(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getNomerDokumenta(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Ошибка!");
       mess.setMessage("Заявление абитуриента с указанными паспортными данными уже существует в базе данных!");
       re_enter = true;
     }*/

// Проверка на уникальность по номеру сертификата ЕГЭ

//     stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE NomerSertifikata LIKE ? AND NomerSertifikata NOT LIKE '-'");
//     stmt.setObject(1,abit_A.getNomerSertifikata(),Types.VARCHAR);
//     rs = stmt.executeQuery();
//     if(rs.next()) {
//       mess.setStatus("Ошибка!");
//       mess.setMessage("Заявление абитуриента с указанным номером сертификата ЕГЭ уже существует в базе данных!");
//       re_enter = true;
//     }

// Проверка на уникальность по серии и номеру аттестата

/*     stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE SeriaAtt LIKE ? AND NomerAtt LIKE ? AND VidDokSredObraz LIKE ?");
     stmt.setObject(1,abit_A.getSeriaAtt(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getNomerAtt(),Types.VARCHAR);
     stmt.setObject(3,abit_A.getVidDokSredObraz(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Ошибка!");
       mess.setMessage("Заявление абитуриента с указанными серией и номером номером аттестата уже существует в БД!");
       re_enter = true;
     }
*/
// Проверка на подлинность аттестата (диплома)

     stmt = conn.prepareStatement("SELECT KodZapisi FROM Bad_Attestat WHERE Seria LIKE ? AND Nomer LIKE ?");
     stmt.setObject(1,abit_A.getSeriaAtt(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getNomerAtt(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Тревога!");
       mess.setMessage("Предоставленный абитуриентом документ об образовании является недействительным!");
       re_enter = true;
     }

// Установка значения кода основы обучения

     if(abit_A.getBud_1() != null && abit_A.getBud_1().equals("on"))
       stmt = conn.prepareStatement("SELECT KodOsnovyOb FROM Osnova_Obuch WHERE Sokr LIKE '%бюдж%'");
     else
       stmt = conn.prepareStatement("SELECT KodOsnovyOb FROM Osnova_Obuch WHERE Sokr LIKE '%дог%'");
     rs = stmt.executeQuery();
     if(rs.next()) kOsnovy_Ob = rs.getString(1);

// Код Абитуриента

     stmt = conn.prepareStatement("SELECT MAX(KodAbiturienta) FROM Abiturient");
     rs = stmt.executeQuery();
     if(rs.next()) kAbiturienta = rs.getInt(1)+1;
     else kAbiturienta = 2;

// Код основной Специальности и аббревиатура

// Приоритетная специальность

// Выборка номера личного дела из БД

     stmt = conn.prepareStatement("SELECT DISTINCT Ordr FROM Abiturient WHERE KodVuza LIKE ? ORDER BY Ordr DESC");
     stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
     rs = stmt.executeQuery();
     if(rs.next()) {

// Хотя бы один абитуриент существует в БД

       if(rs.getInt(1) < 9) nomer_ab = "000"+(rs.getInt(1)+1);
       else if((rs.getInt(1) >= 9) && (rs.getInt(1) < 99)) nomer_ab = "00"+(rs.getInt(1)+1);
       else if((rs.getInt(1) >= 99) && (rs.getInt(1) < 999)) nomer_ab = "0"+(rs.getInt(1)+1);
       else if((rs.getInt(1) >= 999) && (rs.getInt(1) < 9999)) nomer_ab = ""+(rs.getInt(1)+1);

       ordr_ab = ""+(rs.getInt(1)+1);

     } else {

       nomer_ab = "0001";

       ordr_ab = "1";
     }
     
     
     //sokso1
     
      

     if(!re_enter) {

/**********************************/
/***********   Область   **********/
/**********************************/

       /*if(abit_A.getNazvanieOblasti()!=null) {
         stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanieOblasti(),Types.VARCHAR);
         stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) kOblasti = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodOblasti) FROM Oblasti WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kOblasti = rs.getInt(1) + 1;
             else kOblasti = 1;

             stmt = conn.prepareStatement("INSERT Oblasti(KodOblasti,NazvanieOblasti,KodVuza) VALUES(?,?,?)");
             stmt.setObject(1, new Integer(""+kOblasti),Types.INTEGER);
             stmt.setObject(2, abit_A.getNazvanieOblasti(),Types.VARCHAR);
             stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
             stmt.executeUpdate();
         }
       }*/
      /* else {
           stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti IS NULL AND KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kOblasti = rs.getInt(1);
           else {
               stmt = conn.prepareStatement("SELECT MAX(KodOblasti) FROM Oblasti WHERE KodVuza LIKE ?");
               stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
               rs = stmt.executeQuery();
               if(rs.next()) kOblasti = rs.getInt(1) + 1;
               else kOblasti = 1;

               stmt = conn.prepareStatement("INSERT Oblasti(KodOblasti,NazvanieOblasti,KodVuza) VALUES(?,?,?)");
               stmt.setObject(1, new Integer(""+kOblasti),Types.INTEGER);
               stmt.setObject(2, abit_A.getNazvanieOblasti(),Types.VARCHAR);
               stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate();
           }
       }*/

/**********************************/
/***********    Район    **********/
/**********************************/

    /*   if(abit_A.getNazvanieRajona()!=null) {
         stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE NazvanieRajona LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanieRajona(),Types.VARCHAR);
         stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) kRajona = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodRajona) FROM Rajony WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kRajona = rs.getInt(1) + 1;
             else kRajona = 1;

             stmt = conn.prepareStatement("INSERT Rajony(KodRajona,NazvanieRajona,KodVuza) VALUES(?,?,?)");
             stmt.setObject(1, new Integer(""+kRajona),Types.INTEGER);
             stmt.setObject(2, abit_A.getNazvanieRajona(),Types.VARCHAR);
             stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
             stmt.executeUpdate();                    
         }
       }
       else {
           stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE (NazvanieRajona IS NULL OR NazvanieRajona LIKE '') AND KodVuza LIKE ?");
           stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kRajona = rs.getInt(1);
           else {
               stmt = conn.prepareStatement("SELECT MAX(KodRajona) FROM Rajony WHERE KodVuza LIKE ?");
               stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
               rs = stmt.executeQuery();
               if(rs.next()) kRajona = rs.getInt(1) + 1;
               else kRajona = 1;

               stmt = conn.prepareStatement("INSERT Rajony(KodRajona,NazvanieRajona,KodVuza) VALUES(?,?,?)");
               stmt.setObject(1, new Integer(""+kRajona),Types.INTEGER);
               stmt.setObject(2, abit_A.getNazvanieRajona(),Types.VARCHAR);
               stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate();
           }
       }*/

/**********************************/
/***********    Пункт    **********/
/**********************************/

     /*  if(abit_A.getNazvanie()!=null) {
         stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanie(),Types.VARCHAR);
         stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) kPunkta = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodPunkta) FROM Punkty WHERE KodVuza LIKE ?");
             stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kPunkta = rs.getInt(1) + 1;
             else kPunkta = 1;

             stmt = conn.prepareStatement("INSERT Punkty(KodPunkta,Nazvanie,KodVuza) VALUES(?,?,?)");
             stmt.setObject(1, new Integer(""+kPunkta),Types.INTEGER);
             stmt.setObject(2, abit_A.getNazvanie(),Types.VARCHAR);
             stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
             stmt.executeUpdate();
         }
       }
       else {
           stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie IS NULL AND KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kPunkta = rs.getInt(1);
           else {
               stmt = conn.prepareStatement("SELECT MAX(KodPunkta) FROM Punkty WHERE KodVuza LIKE ?");
               stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
               rs = stmt.executeQuery();
               if(rs.next()) kPunkta = rs.getInt(1) + 1;
               else kPunkta = 1;

               stmt = conn.prepareStatement("INSERT Punkty(KodPunkta,Nazvanie,KodVuza) VALUES(?,?,?)");
               stmt.setObject(1,new Integer(""+kPunkta),Types.INTEGER);
               stmt.setObject(2, abit_A.getNazvanie(),Types.VARCHAR);
               stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate();
           }
       }*/

/**********************************/
/***********  Заведение  **********/
/**********************************/

       if(abit_A.getPolnoeNaimenovanieZavedenija() !=null) {
         stmt = conn.prepareStatement("SELECT KodZavedenija FROM Zavedenija WHERE PolnoeNaimenovanieZavedenija LIKE ?");
         stmt.setObject(1, abit_A.getPolnoeNaimenovanieZavedenija(),Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) kZavedenija = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodZavedenija) FROM Zavedenija");
           // stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kZavedenija = rs.getInt(1) + 1;
             else kZavedenija = 1;

             stmt = conn.prepareStatement("INSERT Zavedenija(PolnoeNaimenovanieZavedenija,Sokr,KodVuza,Kodzavedenija) VALUES(?,?,?,?)");
             stmt.setObject(1, abit_A.getPolnoeNaimenovanieZavedenija(),Types.VARCHAR);

             if((abit_A.getPolnoeNaimenovanieZavedenija()).length()>25)
               stmt.setObject(2, (abit_A.getPolnoeNaimenovanieZavedenija()).substring(0,25),Types.VARCHAR);
             else
               stmt.setObject(2, abit_A.getPolnoeNaimenovanieZavedenija(),Types.VARCHAR);

             stmt.setObject(3,9999,Types.INTEGER);
             stmt.setObject(4,new Integer(""+kZavedenija),Types.INTEGER);
             stmt.executeUpdate();
         }

       } else kZavedenija = StringUtil.toInt(""+abit_A.getKodZavedenija(),1);

/**********************************/
/**********  Абитуриент  **********/
/**********************************/

// Формирование НЛД для наиболее приоритетной специальности
String specc= new String();
if(!abit_A.getSpecial2().equals("-")&& !abit_A.getSpecial2().equals(null)&& !abit_A.getSpecial2().equals("")){
specc=abit_A.getSpecial2();
}else if(!abit_A.getSpecial3().equals("-")&& !abit_A.getSpecial3().equals(null)&& !abit_A.getSpecial3().equals("")){
	specc=abit_A.getSpecial3();
}else if(!abit_A.getSpecial4().equals("-")&& !abit_A.getSpecial4().equals(null)&& !abit_A.getSpecial4().equals("")){
	specc=abit_A.getSpecial4();
}else if(!abit_A.getSpecial5().equals("-")&& !abit_A.getSpecial5().equals(null)&& !abit_A.getSpecial5().equals("")){
	specc=abit_A.getSpecial5();
}else if(!abit_A.getSpecial6().equals("-")&& !abit_A.getSpecial6().equals(null)&& !abit_A.getSpecial6().equals("")){
	specc=abit_A.getSpecial6();
}else if(!abit_A.getSpecial7().equals("-")&& !abit_A.getSpecial7().equals(null)&& !abit_A.getSpecial7().equals("")){
	specc=abit_A.getSpecial7();
}
stmt = conn.prepareStatement("SELECT Abbreviatura FROM Spetsialnosti where kodspetsialnosti LIKE ?");
stmt.setObject(1, specc,Types.VARCHAR);
rs = stmt.executeQuery();
if(rs.next()) Abbr_Spec = rs.getString(1);


stmt = conn.prepareStatement("INSERT Abiturient(KodVuza,KodSpetsialnosti,NomerLichnogoDela,Familija,Imja,Otchestvo,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,MestoRojdenija,TipDokSredObraz,VidDokSredObraz,DataRojdenija,Pol,Grajdanstvo,GodOkonchanijaSrObrazovanija,TipOkonchennogoZavedenija,NomerShkoly,KodZavedenija,InostrannyjJazyk,NujdaetsjaVObschejitii,KodOblasti,KodRajona,KodPunkta,SeriaAtt,NomerAtt,Ordr,Gorod_Prop,Ulica_Prop,Dom_Prop,Kvart_Prop,Tel,NomerPlatnogoDogovora,DataInput,DataModify,KodAbiturienta,KodStrany,KodStranyP,KodOblastiP,KodRajonaP,NomerPotoka,kodlgot,kodpodrazdelenija) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


stmt.setObject(1, session.getAttribute("kVuza"), Types.INTEGER);
// stmt.setObject(2, abit_A.getDokumentyHranjatsja(), Types.VARCHAR);
stmt.setObject(2, specc, Types.INTEGER);

stmt.setObject(3, Abbr_Spec+nomer_ab+"-1", Types.VARCHAR); // NLD

stmt.setObject(4, (abit_A.getFamilija()).substring(0,1).toUpperCase()+(abit_A.getFamilija()).substring(1), Types.VARCHAR);
stmt.setObject(5, (abit_A.getImja()).substring(0,1).toUpperCase()+(abit_A.getImja()).substring(1), Types.VARCHAR);
stmt.setObject(6, (abit_A.getOtchestvo()).substring(0,1).toUpperCase()+(abit_A.getOtchestvo()).substring(1), Types.VARCHAR);
stmt.setObject(7, abit_A.getTipDokumenta(), Types.VARCHAR);
stmt.setObject(8, abit_A.getNomerDokumenta(), Types.VARCHAR);
stmt.setObject(9, abit_A.getSeriaDokumenta(), Types.VARCHAR);
stmt.setObject(10, StringUtil.DataConverter(abit_A.getDataVydDokumenta()), Types.VARCHAR);
stmt.setObject(11, abit_A.getKemVydDokument(), Types.VARCHAR);
stmt.setObject(12, abit_A.getMestoRojdenija(), Types.VARCHAR);
stmt.setObject(13, abit_A.getTipDokSredObraz(), Types.VARCHAR);
stmt.setObject(14, abit_A.getVidDokSredObraz(), Types.VARCHAR);
stmt.setObject(15, StringUtil.DataConverter(abit_A.getDataRojdenija()), Types.VARCHAR);
stmt.setObject(16, abit_A.getPol(), Types.VARCHAR);
//stmt.setObject(18, abit_A.getNomerSertifikata(), Types.VARCHAR);
//stmt.setObject(19, abit_A.getKopijaSertifikata(), Types.VARCHAR);
stmt.setObject(17, abit_A.getGrajdanstvo(), Types.VARCHAR);
stmt.setObject(18, ""+abit_A.getGodOkonchanijaSrObrazovanija(), Types.VARCHAR);
stmt.setObject(19, abit_A.getTipOkonchennogoZavedenija(), Types.VARCHAR);
stmt.setObject(20, abit_A.getNomerShkoly(), Types.VARCHAR);
stmt.setObject(21, new Integer(""+kZavedenija), Types.INTEGER);
/*       stmt.setObject(25, abit_A.getTrudovajaDejatelnost(), Types.VARCHAR);
stmt.setObject(26, abit_A.getNapravlenieOtPredprijatija(), Types.VARCHAR);*/
//     stmt.setObject(22, abit_A.getKodLgot(), Types.INTEGER);
// stmt.setObject(30, abit_A.getKodKursov(), Types.INTEGER);
/* Integer kKurs = 1;
stmt.setObject(28, kKurs, Types.INTEGER);*/
stmt.setObject(22, abit_A.getInostrannyjJazyk(), Types.VARCHAR);
stmt.setObject(23, abit_A.getNujdaetsjaVObschejitii(), Types.VARCHAR);
//stmt.setObject(31, abit_A.getKodTselevogoPriema(), Types.INTEGER);
stmt.setObject(24, abit_A.getNazvanieOblasti(), Types.VARCHAR);
stmt.setObject(25, abit_A.getNazvanieRajona(), Types.VARCHAR);
stmt.setObject(26, abit_A.getNazvanie(), Types.VARCHAR);
stmt.setObject(27, abit_A.getSeriaAtt(), Types.VARCHAR);
stmt.setObject(28, abit_A.getNomerAtt(), Types.VARCHAR);
stmt.setObject(29, ordr_ab, Types.VARCHAR);
stmt.setObject(30, abit_A.getGorod_Prop(), Types.VARCHAR);
stmt.setObject(31, abit_A.getUlica_Prop(), Types.VARCHAR);
stmt.setObject(32, abit_A.getDom_Prop(), Types.VARCHAR);
stmt.setObject(33, abit_A.getKvart_Prop(), Types.VARCHAR);
/*  stmt.setObject(42, abit_A.getStepen_Mag(), Types.VARCHAR);*/
/*stmt.setObject(43, abit_A.getNeed_Spo(), Types.VARCHAR);*/
//stmt.setNull(44, Types.VARCHAR); //Exists_st_Mag
stmt.setObject(34, abit_A.getTel(), Types.VARCHAR);
stmt.setObject(35, abit_A.getNpd1(), Types.VARCHAR);
/* stmt.setObject(40, kFormy_Ob, Types.INTEGER);  //KodFormyOb
stmt.setObject(41, kOsnovy_Ob, Types.INTEGER); //KodOsnovyOb
*/       stmt.setObject(36, StringUtil.CurrDate("."), Types.VARCHAR); //DataInput
stmt.setObject(37, StringUtil.CurrDate("."), Types.VARCHAR); //DataModify = DataInput
//stmt.setObject(39, abit_A.getUdostoverenieLgoty(), Types.VARCHAR);
//stmt.setObject(52, abit_A.getDiplomOtlichija(), Types.VARCHAR);
stmt.setObject(38, new Integer(""+kAbiturienta), Types.INTEGER);
/*       stmt.setObject(54, abit_A.getPostgraduateStudies(), Types.VARCHAR);
stmt.setObject(55, abit_A.getTraineeship(), Types.VARCHAR);
stmt.setObject(56, abit_A.getInternship(), Types.VARCHAR);*/
stmt.setObject(39, abit_A.getTraineeship(), Types.VARCHAR);
stmt.setObject(40, abit_A.getNazv_DipBak(), Types.VARCHAR);
stmt.setObject(41, abit_A.getNazv_DipSpec(), Types.VARCHAR);
stmt.setObject(42, abit_A.getNeed_Spo(), Types.VARCHAR);
stmt.setObject(43, abit_A.getNomerPotoka(), Types.INTEGER);
stmt.setObject(44, abit_A.getKodLgot(), Types.INTEGER);
stmt.setObject(45, abit_A.getZajavlen(), Types.VARCHAR);
stmt.executeUpdate();

if(abit_A.getTarget_1() != null) tar=Integer.parseInt(abit_A.getTarget_1());
if(abit_A.getTarget_2() != null) tar=Integer.parseInt(abit_A.getTarget_2());
if(abit_A.getTarget_3() != null) tar=Integer.parseInt(abit_A.getTarget_3());
if(abit_A.getTarget_4() != null) tar=Integer.parseInt(abit_A.getTarget_4());
if(abit_A.getTarget_5() != null) tar=Integer.parseInt(abit_A.getTarget_5());
if(abit_A.getTarget_6() != null) tar=Integer.parseInt(abit_A.getTarget_6());


    stmt = conn.prepareStatement("UPDATE Abiturient SET KodTselevogoPriema=? WHERE NomerDokumenta like '"+abit_A.getNomerDokumenta()+"' and SeriaDokumenta like '"+abit_A.getSeriaDokumenta()+"'");
    stmt.setObject(1, tar, Types.INTEGER);
    stmt.executeUpdate();
/* Создание пустых записей в таблице Оценки */
       stmt = conn.prepareStatement("INSERT INTO Otsenki(KodAbiturienta,KodPredmeta) SELECT DISTINCT ?,KodPredmeta FROM NazvanijaPredmetov ORDER BY KodPredmeta");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.executeUpdate();
       
      /* if(abit_A.getPodtverjdenieMedSpravki().equals("d")){
       
	       stmt = conn.prepareStatement("INSERT INTO medSpravka(kodAbiturienta, nomerSpravki) VALUES(?,?)");
	       stmt.setObject(1,new Integer(kAbiturienta),Types.INTEGER);
	       stmt.setObject(2, form.getMedSpravka(),Types.VARCHAR);
	       stmt.executeUpdate();
       }*/
       
       /* Создание пустых записей в таблице Оa */
       stmt = conn.prepareStatement("INSERT INTO Oa(KodAbiturienta,KodPredmeta) SELECT DISTINCT ?,KodPredmeta FROM NazvanijaPredmetov ORDER BY KodPredmeta");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.executeUpdate();
       
       stmt = conn.prepareStatement("INSERT INTO Os(KodAbiturienta) SELECT DISTINCT ?");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.executeUpdate();

/* Создание пустых записей в таблице Заявленные Школьные Оценки */
       stmt = conn.prepareStatement("INSERT INTO ZajavlennyeShkolnyeOtsenki(KodAbiturienta,KodPredmeta) SELECT DISTINCT ?,KodPredmeta FROM NazvanijaPredmetov ORDER BY KodPredmeta");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.executeUpdate();

/* Создание пустых записей в контрольной таблице */
       stmt = conn.prepareStatement("INSERT INTO Kontrol_ZSO(KodAbiturienta,KodPredmeta) SELECT DISTINCT ?,KodPredmeta FROM NazvanijaPredmetov ORDER BY KodPredmeta");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.executeUpdate();

  /* Создание пустых записей в таблице дополнительной информации об абитуриентах*/
       stmt = conn.prepareStatement("INSERT INTO AbitDopInf(KodAbiturienta, abitEmail, Address, ProvidingSpecialConditions, ReturnDocument, DopSrok, Vstup, Dist, BallAtt, BallSoch, BallSGTO, BallZGTO, BallPOI, TrudovajaDejatelnost) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.setObject(2,abit_A.getAbitEmail() ,Types.VARCHAR);
       stmt.setObject(3,abit_A.getDopAddress(),Types.VARCHAR);
       stmt.setObject(4,abit_A.getProvidingSpecialCondition(),Types.VARCHAR);
       stmt.setObject(5,abit_A.getReturnDocument(),Types.VARCHAR);
       stmt.setObject(6,abit_A.getInternship(),Types.VARCHAR);
       stmt.setObject(7,abit_A.getSpecial13(),Types.VARCHAR);
       stmt.setObject(8,abit_A.getSpecial10(),Types.VARCHAR);
       if(abit_A.getSpecial8() != null){
       stmt.setObject(9,abit_A.getSpecial8(),Types.VARCHAR);
       }else{
    	   stmt.setObject(9,0,Types.VARCHAR);
       }
       if(abit_A.getStepen_Mag() != null){
       stmt.setObject(10,abit_A.getStepen_Mag(),Types.VARCHAR);
       }else{
    	   stmt.setObject(10,0,Types.VARCHAR);
       }
       if(abit_A.getSpecial9() != null){
       stmt.setObject(11,abit_A.getSpecial9(),Types.VARCHAR);
       }else{
    	   stmt.setObject(11,0,Types.VARCHAR);
       }
       if(abit_A.getSpecial222() != null){
       stmt.setObject(12,abit_A.getSpecial222(),Types.VARCHAR);
       }else{
    	   stmt.setObject(12,0,Types.VARCHAR);
       }
       if(abit_A.getShifrKursov() != null){
       stmt.setObject(13,abit_A.getShifrKursov(),Types.VARCHAR);
       }else{
    	   stmt.setObject(13,0,Types.VARCHAR);
       }
       if(abit_A.getTrudovajaDejatelnost() != null){
       stmt.setObject(14,abit_A.getTrudovajaDejatelnost(),Types.VARCHAR);
       }else{
    	   stmt.setObject(14,0,Types.VARCHAR);
       }
       stmt.executeUpdate();
       
/* Добавление заявленных оценок, оценок ЕГЭ и оценок аттестата*/
       Enumeration paramNames = request.getParameterNames();
       int l=1;
       while(paramNames.hasMoreElements()) {

          String paramName = (String)paramNames.nextElement();
          String paramValue[] = request.getParameterValues(paramName);

// Оценки ЕГЭ 

          if(paramName.indexOf("Ege_note") != -1 && paramValue[0].length() != 0) {

            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge=? WHERE KodAbiturienta=? AND KodPredmeta=?");
            stmt.setObject(1,new Integer(paramValue[0]),Types.INTEGER);            // Оценка
            stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);         // Код абитуриента
            stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);  // Код предмета
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE Kontrol_ZSO SET OtsenkaEge=? WHERE KodAbiturienta=? AND KodPredmeta=?");
            stmt.setObject(1,new Integer(paramValue[0]),Types.INTEGER);            // Оценка
            stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);         // Код абитуриента
            stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);  // Код предмета
            stmt.executeUpdate();
            
           
          }
          if(paramName.indexOf("Ege_year") != -1 && paramValue[0].length() != 0) {

              stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET God=? WHERE KodAbiturienta=? AND KodPredmeta=?");
              stmt.setObject(1,new Integer(paramValue[0]),Types.INTEGER);            // Оценка
              stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);         // Код абитуриента
              stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);  // Код предмета
              stmt.executeUpdate();

              stmt = conn.prepareStatement("UPDATE Kontrol_ZSO SET God=? WHERE KodAbiturienta=? AND KodPredmeta=?");
              stmt.setObject(1,new Integer(paramValue[0]),Types.INTEGER);            // Оценка
              stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);         // Код абитуриента
              stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);  // Код предмета
              stmt.executeUpdate();
              
             
            }
          
          if(paramName.indexOf("Attestat") != -1 && paramValue[0].length() != 0) {
        	  	int ba=new Integer(paramValue[0]);
        	  	summa=summa+ba;
        	  	countt++;
        	  	System.out.println("summa"+summa);
        	
              stmt = conn.prepareStatement("UPDATE Oa SET OtsenkaAtt=? WHERE KodAbiturienta=? AND KodPredmeta=?");
              stmt.setObject(1,new Integer(paramValue[0]),Types.INTEGER);            // Оценка
              stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);         // Код абитуриента
              stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);  // Код предмета
              stmt.executeUpdate();

            }

// Признак экзамена (в формате ВУЗа или в формате ЕГЭ)

          if(paramName.indexOf("Examen") != -1 && paramValue[0].length() != 0) {

            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET Examen=? WHERE KodAbiturienta=? AND KodPredmeta=?");
            stmt.setObject(1, StringUtil.toDB(paramValue[0]),Types.VARCHAR);           // Признак внутреннего экзамена
            stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);             // Код абитуриента
            stmt.setObject(3, new Integer(paramName.substring(6)),Types.INTEGER);      // Код предмета
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE Kontrol_ZSO SET Examen=? WHERE KodAbiturienta=? AND KodPredmeta=?");
            stmt.setObject(1, StringUtil.toDB(paramValue[0]),Types.VARCHAR);           // Признак внутреннего экзамена
            stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);             // Код абитуриента
            stmt.setObject(3, new Integer(paramName.substring(6)),Types.INTEGER);      // Код предмета
            stmt.executeUpdate();
          }
       }
       if(countt!=0){
       summa=summa/countt;
       System.out.println("summa"+summa);
       stmt = conn.prepareStatement("UPDATE Os SET Summ=? WHERE KodAbiturienta=?");
       stmt.setObject(1, summa,Types.FLOAT);            // Оценка
       stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);      
       stmt.executeUpdate();
       }
/**********************************/
/**********   Конкурс   ***********/
/**********************************/

// Проверка - разные ли специальности планируются ко вводу
   String spc1 = new String("X1Y1Z1");
   String spc2 = new String("X2Y2Z2");
   String spc3 = new String("X3Y3Z3");
   String spc4 = new String("X4Y4Z4");
   String spc5 = new String("X5Y5Z5");
   String spc6 = new String("X6Y6Z6");
   String spc7 = new String("-");
   if(!abit_A.getSpecial2().equals(spc7)) spc1 = abit_A.getSpecial2(); 
   if(!abit_A.getSpecial3().equals(spc7)) spc2 = abit_A.getSpecial3();
   if(!abit_A.getSpecial4().equals(spc7)) spc3 = abit_A.getSpecial4();
   if(!abit_A.getSpecial5().equals(spc7)) spc4 = abit_A.getSpecial5();
   if(!abit_A.getSpecial6().equals(spc7)) spc5 = abit_A.getSpecial6();
   if(!abit_A.getSpecial7().equals(spc7)) spc6 = abit_A.getSpecial7();

   if(spc1.equals(spc2) || spc1.equals(spc3) || spc1.equals(spc4) || spc1.equals(spc5) || spc1.equals(spc6) || spc2.equals(spc3) || 
      spc2.equals(spc4) || spc2.equals(spc5) || spc2.equals(spc6) || spc3.equals(spc4) || spc3.equals(spc5) || spc3.equals(spc6) || 
      spc4.equals(spc5) || spc4.equals(spc6) || spc5.equals(spc6)) {

          mess.setStatus("Ошибка!");
          mess.setMessage("Нельзя указывать в конкурсе две одинаковые специальности!");
          re_enter = true;
   }
   Tip_Spec = "о";
   if(!abit_A.getSpecial2().equals(spc7)) Spec1 = new Integer(abit_A.getSpecial2());
   if(!abit_A.getSpecial3().equals(spc7)) Spec2 = new Integer(abit_A.getSpecial3());
   if(!abit_A.getSpecial4().equals(spc7)) Spec3 = new Integer(abit_A.getSpecial4());
   if(!abit_A.getSpecial5().equals(spc7)) Spec4 = new Integer(abit_A.getSpecial5());
   if(!abit_A.getSpecial6().equals(spc7)) Spec5 = new Integer(abit_A.getSpecial6());
   if(!abit_A.getSpecial7().equals(spc7)) Spec6 = new Integer(abit_A.getSpecial7());
/*****************************/
/****** 1. Приоритетная ******/
/*****************************/
l =1;
       if(!abit_A.getSpecial2().equals(spc7) && !re_enter) {
    	   kSpec = "-1";
           Abbr_Spec2 = "X1Y1Z1";
           
    	   
           
       
         //sokso1
         /*l=1;
         stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
         stmt.setObject(1,Spec1,Types.INTEGER);
         stmt.setObject(2,Tip_Spec,Types.VARCHAR);
         rs = stmt.executeQuery();
         while(rs.next()) {
        	 for(l=1;l<15;l++){
        		 if(tek[l][2]==rs.getInt(1) && tek[l][1]==0){
        			 mess.setStatus("Ошибка!");
                     mess.setMessage("Cпециальность "+rs.getString(2)+" требует наличие других экзаменов");
                     re_enter = true;
                     form.setAction(us.getClientIntName("re_new","error"));
        		 }        		 
        	 }
         }*/
         
// Очных специальностей не должно быть больше 3х

     
         stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
         stmt.setObject(1,Spec1,Types.VARCHAR);
         stmt.setObject(2,Tip_Spec,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) {
           kSpec = rs.getString(1);
           Abbr_Spec2 = rs.getString(2);
           pp=rs.getInt(3);
           if(abit_A.getBud_1() != null && pp==0){
               mess.setStatus("Ошибка!");
               mess.setMessage("Cпециальность "+rs.getString(4)+" не предусматривает бюджетные места!");
               re_enter = true;
             }
         }
System.out.println("SOKSO1>"+s_okso_1+"<");
// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {
//System.out.println("1>"+two_t_names[i]+"<kA>"+kAbiturienta+"<kSp>"+Spec1+"<Abbr_Spec>"+Abbr_Spec+nomer_ab+"-2"+"<TipS>"+Tip_Spec);
           stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
           stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
           stmt.setObject(2, Spec1, Types.VARCHAR);
           stmt.setObject(3, Abbr_Spec+nomer_ab+"-"+chet, Types.VARCHAR);
           stmt.setObject(4, chet, Types.VARCHAR);
           stmt.setObject(5, Tip_Spec, Types.VARCHAR);
           if(abit_A.getBud_1() != null && pp!=0)
             stmt.setObject(6, "д", Types.VARCHAR);
           else
             stmt.setNull(6, Types.VARCHAR);
           if(abit_A.getDog_1() != null)
             stmt.setObject(7, "д", Types.VARCHAR);
           else
             stmt.setNull(7, Types.VARCHAR);
           if(abit_A.getDog_ok_1() != null)
               stmt.setObject(8, "д", Types.VARCHAR);
             else
               stmt.setNull(8, Types.VARCHAR);
           
           if(abit_A.getNpd1() != null)
             stmt.setObject(9, abit_A.getNpd1(), Types.VARCHAR);
           else
             stmt.setNull(9, Types.VARCHAR);

           if(abit_A.getStob() != null)
               stmt.setObject(10, 1, Types.INTEGER);
           else
               stmt.setNull(10, Types.INTEGER);
           if(abit_A.getPr1() != null)
               stmt.setObject(11, 1, Types.INTEGER);
           else
               stmt.setNull(11, Types.INTEGER);
           if(abit_A.getPr2() != null)
               stmt.setObject(12, 1, Types.INTEGER);
           else
               stmt.setNull(12, Types.INTEGER);
           if(abit_A.getPr3() != null)
               stmt.setObject(13, 1, Types.INTEGER);
           else
               stmt.setNull(13, Types.INTEGER);
           if(abit_A.getTarget_1() != null){
               stmt.setObject(14, abit_A.getTarget_1(), Types.VARCHAR);
           }else{
            	 stmt.setObject(14, 1, Types.VARCHAR);
           }
           if(abit_A.getTname1() != null)
               stmt.setObject(15, abit_A.getTname1(), Types.VARCHAR);
             else
               stmt.setNull(15, Types.VARCHAR);
           
        
               stmt.setObject(16,1,Types.VARCHAR);
           if(abit_A.getOlimp_1() != null){
               stmt.setObject(17, abit_A.getOlimp_1(), Types.VARCHAR);
           }else{
        	   stmt.setObject(17, 0, Types.VARCHAR);
           }
           if(abit_A.getOp1() != null){
               stmt.setObject(18, abit_A.getOp1(), Types.VARCHAR);
           }else{
        	   stmt.setObject(18, 1, Types.VARCHAR);
           }
            
           if(abit_A.getRlgot1() != null)
               stmt.setObject(19, abit_A.getRlgot1(), Types.VARCHAR);
             else
               stmt.setNull(19, Types.VARCHAR);
           if(abit_A.getPrr1() != null){
               stmt.setObject(20, abit_A.getPrr1(), Types.VARCHAR);    
         }else{
      	   stmt.setObject(20, 0, Types.VARCHAR);
         }

           if(abit_A.getThree_1() != null)
               stmt.setObject(21, abit_A.getThree_1(), Types.VARCHAR);
             else
               stmt.setNull(21, Types.VARCHAR);
           if(abit_A.getSix_1() != null)
               stmt.setObject(22, abit_A.getSix_1(), Types.VARCHAR);
             else
               stmt.setNull(22, Types.VARCHAR);
           stmt.setObject(23, abit_A.getFito_1(), Types.INTEGER);
           if(abit_A.getSog1() != null )
               stmt.setObject(24, "д", Types.VARCHAR);
             else
               stmt.setNull(24, Types.VARCHAR);
           stmt.executeUpdate();
          
         }
chet++;
       }

/*********************/
/****** 2.      ******/
/*********************/

       if(!abit_A.getSpecial3().equals(spc7) && !re_enter) {
    	   kSpec = "-1";
           Abbr_Spec2 = "X1Y1Z1";
// Определение типа специальности
    	  /* l=1;
           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
           stmt.setObject(1,Spec2,Types.INTEGER);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           while(rs.next()) {
          	 for(l=1;l<15;l++){
          		 if(tek[l][2]==rs.getInt(1) && tek[l][1]==0){
          			 mess.setStatus("Ошибка!");
                       mess.setMessage("Cпециальность "+rs.getString(2)+" требует наличие других экзаменов");
                       re_enter = true;
                       form.setAction(us.getClientIntName("re_new","error"));
          		 }        		 
          	 }
           }*/
           
  // Очных специальностей не должно быть больше 3х
 int pp1=0;
       
           stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
           stmt.setObject(1,Spec2,Types.VARCHAR);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           if(rs.next()) {
             kSpec = rs.getString(1);
             Abbr_Spec2 = rs.getString(2);
             pp1=rs.getInt(3);
             if(abit_A.getBud_2() != null && pp1==0){
                 mess.setStatus("Ошибка!");
                 mess.setMessage("Cпециальность "+rs.getString(4)+" не предусматривает бюджетные места!");
                 re_enter = true;
               }
           }  
System.out.println("SOKSO2>"+s_okso_2+"<");
// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon
	
	
         for(int i=0; i<2; i++) {
        	 stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
             stmt.setObject(2, Spec2, Types.VARCHAR);
             stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
             stmt.setObject(4, chet, Types.VARCHAR);
             stmt.setObject(5, Tip_Spec, Types.VARCHAR);
             if(abit_A.getBud_2() != null && pp!=0)
               stmt.setObject(6, "д", Types.VARCHAR);
             else
               stmt.setNull(6, Types.VARCHAR);
             if(abit_A.getDog_2() != null)
               stmt.setObject(7, "д", Types.VARCHAR);
             else
               stmt.setNull(7, Types.VARCHAR);
             if(abit_A.getDog_ok_2() != null)
                 stmt.setObject(8, "д", Types.VARCHAR);
               else
                 stmt.setNull(8, Types.VARCHAR);
             
             if(abit_A.getNpd2() != null)
               stmt.setObject(9, abit_A.getNpd2(), Types.VARCHAR);
             else
               stmt.setNull(9, Types.VARCHAR);

             if(abit_A.getStob_2() != null)
                 stmt.setObject(10, 1, Types.INTEGER);
             else
                 stmt.setNull(10, Types.INTEGER);
             if(abit_A.getPr1_2() != null)
                 stmt.setObject(11, 1, Types.INTEGER);
             else
                 stmt.setNull(11, Types.INTEGER);
             if(abit_A.getPr2_2() != null)
                 stmt.setObject(12, 1, Types.INTEGER);
             else
                 stmt.setNull(12, Types.INTEGER);
             if(abit_A.getPr3_2() != null)
                 stmt.setObject(13, 1, Types.INTEGER);
             else
                 stmt.setNull(13, Types.INTEGER);
             if(abit_A.getTarget_2() != null){
                 stmt.setObject(14, abit_A.getTarget_2(), Types.VARCHAR);
             }else{
            	 stmt.setObject(14, 1, Types.VARCHAR);
             }
             if(abit_A.getTname2() != null)
                 stmt.setObject(15, abit_A.getTname2(), Types.VARCHAR);
               else
                 stmt.setNull(15, Types.VARCHAR);
             
             stmt.setObject(16,2,Types.VARCHAR);
             
             if(abit_A.getOlimp_2() != null){
                 stmt.setObject(17, abit_A.getOlimp_2(), Types.VARCHAR);
             }else{
            	 stmt.setObject(17, 0, Types.VARCHAR);
             }
             if(abit_A.getOp2() != null){
                 stmt.setObject(18, abit_A.getOp2(), Types.VARCHAR);
             }else{
            	 stmt.setObject(18, 1, Types.VARCHAR);
             }
                 if(abit_A.getRlgot2() != null)
                 stmt.setObject(19, abit_A.getRlgot2(), Types.VARCHAR);
               else
                 stmt.setNull(19, Types.VARCHAR);
             
                 if(abit_A.getPrr2() != null){
                     stmt.setObject(20, abit_A.getPrr2(), Types.VARCHAR);    
               }else{
            	   stmt.setObject(20, 0, Types.VARCHAR);
               }

             if(abit_A.getThree_2() != null)
                 stmt.setObject(21, abit_A.getThree_2(), Types.VARCHAR);
               else
                 stmt.setNull(21, Types.VARCHAR);
             if(abit_A.getSix_2() != null)
                 stmt.setObject(22, abit_A.getSix_2(), Types.VARCHAR);
               else
                 stmt.setNull(22, Types.VARCHAR);
             stmt.setObject(23, abit_A.getFito_2(), Types.VARCHAR);
             if(abit_A.getSog2() != null )
                 stmt.setObject(24, "д", Types.VARCHAR);
               else
                 stmt.setNull(24, Types.VARCHAR);
           stmt.executeUpdate();
         }
         chet++;
       }

/*********************/
/****** 3.      ******/
/*********************/

       if(!abit_A.getSpecial4().equals(spc7) && !re_enter) {
 int pp2=0;
 kSpec = "-1";
 Abbr_Spec2 = "X1Y1Z1";
// Определение типа специальности
    	  /* l=1;
           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
           stmt.setObject(1,Spec3,Types.INTEGER);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           while(rs.next()) {
          	 for(l=1;l<15;l++){
          		 if(tek[l][2]==rs.getInt(1) && tek[l][1]==0){
          			 mess.setStatus("Ошибка!");
                       mess.setMessage("Cпециальность "+rs.getString(2)+" требует наличие других экзаменов");
                       re_enter = true;
                       form.setAction(us.getClientIntName("re_new","error"));
          		 }        		 
          	 }
           }*/
           
  // Очных специальностей не должно быть больше 3х

       
           stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
           stmt.setObject(1,Spec3,Types.VARCHAR);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           if(rs.next()) {
             kSpec = rs.getString(1);
             Abbr_Spec2 = rs.getString(2);
             pp2=rs.getInt(3);
             if(abit_A.getBud_3() != null && pp2==0){
                 mess.setStatus("Ошибка!");
                 mess.setMessage("Cпециальность "+rs.getString(4)+" не предусматривает бюджетные места!");
                 re_enter = true;
               }
           }
         
// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {
        	 stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
             stmt.setObject(2, Spec3, Types.VARCHAR);
             stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
             stmt.setObject(4, chet, Types.VARCHAR);
             stmt.setObject(5, Tip_Spec, Types.VARCHAR);
             if(abit_A.getBud_3() != null && pp!=0)
               stmt.setObject(6, "д", Types.VARCHAR);
             else
               stmt.setNull(6, Types.VARCHAR);
             if(abit_A.getDog_3() != null)
               stmt.setObject(7, "д", Types.VARCHAR);
             else
               stmt.setNull(7, Types.VARCHAR);
             if(abit_A.getDog_ok_3() != null)
                 stmt.setObject(8, "д", Types.VARCHAR);
               else
                 stmt.setNull(8, Types.VARCHAR);
             
             if(abit_A.getNpd3() != null)
               stmt.setObject(9, abit_A.getNpd3(), Types.VARCHAR);
             else
               stmt.setNull(9, Types.VARCHAR);

             if(abit_A.getStob_3() != null)
                 stmt.setObject(10, 1, Types.INTEGER);
             else
                 stmt.setNull(10, Types.INTEGER);
             if(abit_A.getPr1_3() != null)
                 stmt.setObject(11, 1, Types.INTEGER);
             else
                 stmt.setNull(11, Types.INTEGER);
             if(abit_A.getPr2_3() != null)
                 stmt.setObject(12, 1, Types.INTEGER);
             else
                 stmt.setNull(12, Types.INTEGER);
             if(abit_A.getPr3_3() != null)
                 stmt.setObject(13, 1, Types.INTEGER);
             else
                 stmt.setNull(13, Types.INTEGER);
             if(abit_A.getTarget_3() != null){
                 stmt.setObject(14, abit_A.getTarget_3(), Types.VARCHAR);
             }else{
            	 stmt.setObject(14, 1, Types.VARCHAR);
             }
             if(abit_A.getTname3() != null)
                 stmt.setObject(15, abit_A.getTname3(), Types.VARCHAR);
               else
                 stmt.setNull(15, Types.VARCHAR);
             
             stmt.setObject(16,3,Types.VARCHAR);
             if(abit_A.getOlimp_3() != null){
                 stmt.setObject(17, abit_A.getOlimp_3(), Types.VARCHAR);
             }else{
            	 stmt.setObject(17, 0, Types.VARCHAR);
             }
             if(abit_A.getOp3() != null){
                 stmt.setObject(18, abit_A.getOp3(), Types.VARCHAR);
             }else{
            	 stmt.setObject(18, 1, Types.VARCHAR);
             }
             if(abit_A.getRlgot3() != null)
                 stmt.setObject(19, abit_A.getRlgot3(), Types.VARCHAR);
               else
                 stmt.setNull(19, Types.VARCHAR);
             
             if(abit_A.getPrr3() != null){
                 stmt.setObject(20, abit_A.getPrr3(), Types.VARCHAR);    
           }else{
        	   stmt.setObject(20, 0, Types.VARCHAR);
           }

             if(abit_A.getThree_3() != null)
                 stmt.setObject(21, abit_A.getThree_3(), Types.VARCHAR);
               else
                 stmt.setNull(21, Types.VARCHAR);
             if(abit_A.getSix_3() != null)
                 stmt.setObject(22, abit_A.getSix_3(), Types.VARCHAR);
               else
                 stmt.setNull(22, Types.VARCHAR);
             stmt.setObject(23, abit_A.getFito_3(), Types.VARCHAR);
             if(abit_A.getSog3() != null )
                 stmt.setObject(24, "д", Types.VARCHAR);
               else
                 stmt.setNull(24, Types.VARCHAR);
           stmt.executeUpdate();
         }
         chet++;
       }

/*********************/
/***** 4.        *****/
/*********************/
         
       if(!abit_A.getSpecial5().equals(spc7) && !re_enter) {
    	   stmt = conn.prepareStatement("SELECT Tip_Spec FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
    	   stmt.setObject(1,Spec4,Types.VARCHAR);
    	   rs = stmt.executeQuery();
           if(rs.next()) {
    	   Tip_Spec = rs.getString(1);  
           }else{
        	   Tip_Spec="з";
           }
    	   kSpec = "-1";
           Abbr_Spec2 = "X1Y1Z1";
// Определение типа специальности
    	   int pp3=0;
    	  /* l=1;
           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec NOT LIKE ?");
           stmt.setObject(1,Spec4,Types.INTEGER);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           while(rs.next()) {
          	 for(l=1;l<15;l++){
          		 if(tek[l][2]==rs.getInt(1) && tek[l][1]==0){
          			 mess.setStatus("Ошибка!");
                       mess.setMessage("Cпециальность "+rs.getString(2)+" требует наличие других экзаменов");
                       re_enter = true;
                       form.setAction(us.getClientIntName("re_new","error"));
          		 }        		 
          	 }
           }*/
           
  // Очных специальностей не должно быть больше 3х

       
           stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
           stmt.setObject(1,Spec4,Types.VARCHAR);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           if(rs.next()) {
             kSpec = rs.getString(1);
             Abbr_Spec2 = rs.getString(2);
             pp3=rs.getInt(3);
             if(abit_A.getBud_4() != null && pp3==0){
                 mess.setStatus("Ошибка!");
                 mess.setMessage("Cпециальность "+rs.getString(4)+" не предусматривает бюджетные места!");
                 re_enter = true;
               }
           }

// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon
         
         for(int i=0; i<2; i++) {

        	 stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
             stmt.setObject(2, Spec4, Types.VARCHAR);
             stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
             stmt.setObject(4, chet, Types.VARCHAR);
             stmt.setObject(5, Tip_Spec, Types.VARCHAR);
             if(abit_A.getBud_4() != null && pp!=0)
               stmt.setObject(6, "д", Types.VARCHAR);
             else
               stmt.setNull(6, Types.VARCHAR);
             if(abit_A.getDog_4() != null)
               stmt.setObject(7, "д", Types.VARCHAR);
             else
               stmt.setNull(7, Types.VARCHAR);
             if(abit_A.getDog_ok_4() != null)
                 stmt.setObject(8, "д", Types.VARCHAR);
               else
                 stmt.setNull(8, Types.VARCHAR);
             
             if(abit_A.getNpd4() != null)
               stmt.setObject(9, abit_A.getNpd4(), Types.VARCHAR);
             else
               stmt.setNull(9, Types.VARCHAR);

             if(abit_A.getStob_4() != null)
                 stmt.setObject(10, 1, Types.INTEGER);
             else
                 stmt.setNull(10, Types.INTEGER);
             if(abit_A.getPr1_4() != null)
                 stmt.setObject(11, 1, Types.INTEGER);
             else
                 stmt.setNull(11, Types.INTEGER);
             if(abit_A.getPr2_4() != null)
                 stmt.setObject(12, 1, Types.INTEGER);
             else
                 stmt.setNull(12, Types.INTEGER);
             if(abit_A.getPr3_4() != null)
                 stmt.setObject(13, 1, Types.INTEGER);
             else
                 stmt.setNull(13, Types.INTEGER);
             if(abit_A.getTarget_4() != null){
                 stmt.setObject(14, abit_A.getTarget_4(), Types.VARCHAR);
             }else{
            	 stmt.setObject(14, 1, Types.VARCHAR);
             }
             if(abit_A.getTname4() != null)
                 stmt.setObject(15, abit_A.getTname4(), Types.VARCHAR);
               else
                 stmt.setNull(15, Types.VARCHAR);
             
             stmt.setObject(16,4,Types.VARCHAR);
             if(abit_A.getOlimp_4() != null){
                 stmt.setObject(17, abit_A.getOlimp_4(), Types.VARCHAR);
             }else{
            	 stmt.setObject(17, 0, Types.VARCHAR);
             }
             if(abit_A.getOp4() != null){
                 stmt.setObject(18, abit_A.getOp4(), Types.VARCHAR);
             }else{
            	 stmt.setObject(18, 1, Types.VARCHAR);
             }
             if(abit_A.getRlgot4() != null)
                 stmt.setObject(19, abit_A.getRlgot4(), Types.VARCHAR);
               else
                 stmt.setNull(19, Types.VARCHAR);
             
             if(abit_A.getPrr4() != null){
                 stmt.setObject(20, abit_A.getPrr4(), Types.VARCHAR);    
           }else{
        	   stmt.setObject(20, 0, Types.VARCHAR);
           }

             if(abit_A.getThree_4() != null)
                 stmt.setObject(21, abit_A.getThree_4(), Types.VARCHAR);
               else
                 stmt.setNull(21, Types.VARCHAR);
             if(abit_A.getSix_4() != null)
                 stmt.setObject(22, abit_A.getSix_4(), Types.VARCHAR);
               else
                 stmt.setNull(22, Types.VARCHAR);
             stmt.setObject(23, abit_A.getFito_4(), Types.VARCHAR);
             if(abit_A.getSog4() != null )
                 stmt.setObject(24, "д", Types.VARCHAR);
               else
                 stmt.setNull(24, Types.VARCHAR);
           stmt.executeUpdate();
         }
         chet++;
       }

/*********************/
/***** 5.        *****/
/*********************/

       if(!abit_A.getSpecial6().equals(spc7) && !re_enter) {
    	   
    	   stmt = conn.prepareStatement("SELECT Tip_Spec FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
    	   stmt.setObject(1,Spec5,Types.VARCHAR);
    	   rs = stmt.executeQuery();
           if(rs.next()) {
    	   Tip_Spec = rs.getString(1);  
           }else{
        	   Tip_Spec="з";
           }
// Определение типа специальности
    	   kSpec = "-1";
           Abbr_Spec2 = "X1Y1Z1";
    	   int pp4=0;
    	   /*l=1;
           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec NOT LIKE ?");
           stmt.setObject(1,Spec5,Types.INTEGER);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           while(rs.next()) {
          	 for(l=1;l<15;l++){
          		 if(tek[l][2]==rs.getInt(1) && tek[l][1]==0){
          			 mess.setStatus("Ошибка!");
                       mess.setMessage("Cпециальность "+rs.getString(2)+" требует наличие других экзаменов");
                       re_enter = true;
                       form.setAction(us.getClientIntName("re_new","error"));
          		 }        		 
          	 }
           }*/
           
  // Очных специальностей не должно быть больше 3х

       
           stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
           stmt.setObject(1,Spec5,Types.VARCHAR);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           if(rs.next()) {
             kSpec = rs.getString(1);
             Abbr_Spec2 = rs.getString(2);
             pp4=rs.getInt(3);
             if(abit_A.getBud_5() != null && pp4==0){
                 mess.setStatus("Ошибка!");
                 mess.setMessage("Cпециальность "+rs.getString(4)+" не предусматривает бюджетные места!");
                 re_enter = true;
               }
           }
         
// Очных специальностей не должно быть больше 3х

        


// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon
         
         for(int i=0; i<2; i++) {

        	 stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
             stmt.setObject(2, Spec5, Types.VARCHAR);
             stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
             stmt.setObject(4, chet, Types.VARCHAR);
             stmt.setObject(5, Tip_Spec, Types.VARCHAR);
             if(abit_A.getBud_5() != null && pp!=0)
               stmt.setObject(6, "д", Types.VARCHAR);
             else
               stmt.setNull(6, Types.VARCHAR);
             if(abit_A.getDog_5() != null)
               stmt.setObject(7, "д", Types.VARCHAR);
             else
               stmt.setNull(7, Types.VARCHAR);
             if(abit_A.getDog_ok_5() != null)
                 stmt.setObject(8, "д", Types.VARCHAR);
               else
                 stmt.setNull(8, Types.VARCHAR);
             
             if(abit_A.getNpd5() != null)
               stmt.setObject(9, abit_A.getNpd5(), Types.VARCHAR);
             else
               stmt.setNull(9, Types.VARCHAR);

             if(abit_A.getStob_5() != null)
                 stmt.setObject(10, 1, Types.INTEGER);
             else
                 stmt.setNull(10, Types.INTEGER);
             if(abit_A.getPr1_5() != null)
                 stmt.setObject(11, 1, Types.INTEGER);
             else
                 stmt.setNull(11, Types.INTEGER);
             if(abit_A.getPr2_5() != null)
                 stmt.setObject(12, 1, Types.INTEGER);
             else
                 stmt.setNull(12, Types.INTEGER);
             if(abit_A.getPr3_5() != null)
                 stmt.setObject(13, 1, Types.INTEGER);
             else
                 stmt.setNull(13, Types.INTEGER);
             if(abit_A.getTarget_5() != null){
                 stmt.setObject(14, abit_A.getTarget_5(), Types.VARCHAR);
             }else{
            	 stmt.setObject(14, 1, Types.VARCHAR);
             }
             if(abit_A.getTname5() != null)
                 stmt.setObject(15, abit_A.getTname5(), Types.VARCHAR);
               else
                 stmt.setNull(15, Types.VARCHAR);
             
             stmt.setObject(16,5,Types.VARCHAR);
             if(abit_A.getOlimp_5() != null){
                 stmt.setObject(17, abit_A.getOlimp_5(), Types.VARCHAR);
             }else{
            	 stmt.setObject(17, 0, Types.VARCHAR);
             }
             if(abit_A.getOp5() != null){
                 stmt.setObject(18, abit_A.getOp5(), Types.VARCHAR);
             }else{
            	 stmt.setObject(18, 1, Types.VARCHAR);
             }
             if(abit_A.getRlgot5() != null)
                 stmt.setObject(19, abit_A.getRlgot5(), Types.VARCHAR);
               else
                 stmt.setNull(19, Types.VARCHAR);
             
             if(abit_A.getPrr5() != null){
                 stmt.setObject(20, abit_A.getPrr5(), Types.VARCHAR);    
           }else{
        	   stmt.setObject(20, 0, Types.VARCHAR);
           }

             if(abit_A.getThree_5() != null)
                 stmt.setObject(21, abit_A.getThree_5(), Types.VARCHAR);
               else
                 stmt.setNull(21, Types.VARCHAR);
             if(abit_A.getSix_5() != null)
                 stmt.setObject(22, abit_A.getSix_5(), Types.VARCHAR);
               else
                 stmt.setNull(22, Types.VARCHAR);
             stmt.setObject(23, abit_A.getFito_5(), Types.VARCHAR);
             if(abit_A.getSog5() != null )
                 stmt.setObject(24, "д", Types.VARCHAR);
               else
                 stmt.setNull(24, Types.VARCHAR);
           stmt.executeUpdate();
         }
         chet++;
       }


/*********************/
/***** 6.        *****/
/*********************/

       if(!abit_A.getSpecial7().equals(spc7) && !re_enter) {
    	   stmt = conn.prepareStatement("SELECT Tip_Spec FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
    	   stmt.setObject(1,Spec6,Types.VARCHAR);
    	   rs = stmt.executeQuery();
           if(rs.next()) {
    	   Tip_Spec = rs.getString(1);  
           }else{
        	   Tip_Spec="з";
           }
// Определение типа специальности

    	   kSpec = "-1";
           Abbr_Spec2 = "X1Y1Z1";
           int pp5=0;
           /*l=1;
           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec NOT LIKE ?");
           stmt.setObject(1,Spec6,Types.INTEGER);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           while(rs.next()) {
          	 for(l=1;l<15;l++){
          		 if(tek[l][2]==rs.getInt(1) && tek[l][1]==0){
          			 mess.setStatus("Ошибка!");
                       mess.setMessage("Cпециальность "+rs.getString(2)+" требует наличие других экзаменов");
                       re_enter = true;
                       form.setAction(us.getClientIntName("re_new","error"));
          		 }        		 
          	 }
           }
           */
  // Очных специальностей не должно быть больше 3х

       
           stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema,NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
           stmt.setObject(1,Spec6,Types.VARCHAR);
           stmt.setObject(2,Tip_Spec,Types.VARCHAR);
           rs = stmt.executeQuery();
           if(rs.next()) {
             kSpec = rs.getString(1);
             Abbr_Spec2 = rs.getString(2);
             pp5=rs.getInt(3);
             if(abit_A.getBud_6() != null && pp5==0){
                 mess.setStatus("Ошибка!");
                 mess.setMessage("Cпециальность "+rs.getString(4)+" не предусматривает бюджетные места!");
                 re_enter = true;
               }
           }

// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon
         
         for(int i=0; i<2; i++) {

        	 stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
             stmt.setObject(2, Spec6, Types.VARCHAR);
             stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
             stmt.setObject(4, chet, Types.VARCHAR);
             stmt.setObject(5, Tip_Spec, Types.VARCHAR);
             if(abit_A.getBud_6() != null && pp!=0)
               stmt.setObject(6, "д", Types.VARCHAR);
             else
               stmt.setNull(6, Types.VARCHAR);
             if(abit_A.getDog_6() != null)
               stmt.setObject(7, "д", Types.VARCHAR);
             else
               stmt.setNull(7, Types.VARCHAR);
             if(abit_A.getDog_ok_6() != null)
                 stmt.setObject(8, "д", Types.VARCHAR);
               else
                 stmt.setNull(8, Types.VARCHAR);
             
             if(abit_A.getNpd6() != null)
               stmt.setObject(9, abit_A.getNpd6(), Types.VARCHAR);
             else
               stmt.setNull(9, Types.VARCHAR);

             if(abit_A.getStob_6() != null)
                 stmt.setObject(10, 1, Types.INTEGER);
             else
                 stmt.setNull(10, Types.INTEGER);
             if(abit_A.getPr1_6() != null)
                 stmt.setObject(11, 1, Types.INTEGER);
             else
                 stmt.setNull(11, Types.INTEGER);
             if(abit_A.getPr2_6() != null)
                 stmt.setObject(12, 1, Types.INTEGER);
             else
                 stmt.setNull(12, Types.INTEGER);
             if(abit_A.getPr3_6() != null)
                 stmt.setObject(13, 1, Types.INTEGER);
             else
                 stmt.setNull(13, Types.INTEGER);
             if(abit_A.getTarget_6() != null){
                 stmt.setObject(14, abit_A.getTarget_6(), Types.VARCHAR);
             }else{
            	 stmt.setObject(14, 1, Types.VARCHAR);
             }
             if(abit_A.getTname6() != null)
                 stmt.setObject(15, abit_A.getTname6(), Types.VARCHAR);
               else
                 stmt.setNull(15, Types.VARCHAR);
             
             stmt.setObject(16,6,Types.VARCHAR);
             if(abit_A.getOlimp_6() != null){
                 stmt.setObject(17, abit_A.getOlimp_6(), Types.VARCHAR);
             }else{
            	 stmt.setObject(17, 0, Types.VARCHAR);
             }
             if(abit_A.getOp6() != null){
                 stmt.setObject(18, abit_A.getOp6(), Types.VARCHAR);
             }else{
            	 stmt.setObject(18, 1, Types.VARCHAR);
             }
             if(abit_A.getRlgot6() != null)
                 stmt.setObject(19, abit_A.getRlgot6(), Types.VARCHAR);
               else
                 stmt.setNull(19, Types.VARCHAR);
             
             if(abit_A.getPrr6() != null){
                 stmt.setObject(20, abit_A.getPrr6(), Types.VARCHAR);    
           }else{
        	   stmt.setObject(20, 0, Types.VARCHAR);
           }

             if(abit_A.getThree_6() != null)
                 stmt.setObject(21, abit_A.getThree_6(), Types.VARCHAR);
               else
                 stmt.setNull(21, Types.VARCHAR);
             if(abit_A.getSix_6() != null)
                 stmt.setObject(22, abit_A.getSix_6(), Types.VARCHAR);
               else
                 stmt.setNull(22, Types.VARCHAR);
             stmt.setObject(23, abit_A.getFito_6(), Types.VARCHAR);
             if(abit_A.getSog6() != null )
                 stmt.setObject(24, "д", Types.VARCHAR);
               else
                 stmt.setNull(24, Types.VARCHAR);
           stmt.executeUpdate();
         }
       }

      
       
       
       
       
       
       
       

       if(!re_enter) {

// Закрепление транзакции

         conn.setAutoCommit(true);

         conn.commit();

         abit_A.setKodAbiturienta(new Integer(""+kAbiturienta));

         abit_A.setFamilija((abit_A.getFamilija()).substring(0,1).toUpperCase()+(abit_A.getFamilija()).substring(1));

         abit_A.setImja((abit_A.getImja()).substring(0,1).toUpperCase()+(abit_A.getImja()).substring(1));

         abit_A.setOtchestvo((abit_A.getOtchestvo()).substring(0,1).toUpperCase()+(abit_A.getOtchestvo()).substring(1));
         
         
         String fo = new String();
         String oo = new String();
         stmt = conn.prepareStatement("SELECT forma_ob,bud from konkurs where kodabiturienta like ? and prioritet=1");
         stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
         rs = stmt.executeQuery();
         if (rs.next()) {
      	   fo=rs.getString(1);
      	   if(rs.getString(2) != null && rs.getString(2).equals("д")){
      		   oo="б"; 
      	   }else{
      		   oo="д";
      	   }
         }
         
         stmt = conn.prepareStatement("UPDATE Abiturient SET KodFormyOb=?,KodOsnovyOb=? WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1,fo,Types.VARCHAR);
         stmt.setObject(2,oo,Types.VARCHAR);
         stmt.setObject(3, new Integer(""+kAbiturienta), Types.INTEGER);
         stmt.executeUpdate();

         form.setAction(us.getClientIntName("add_success","act-added"));

       }

     } //if no re_enter
   }


/************************************************************************************************/
/*********************** Повторный ввод карточки абитуриента из-за ошибки ***********************/
/************************************************************************************************/

if( re_enter ){

// Ошибка. Откат транзакции.

    conn.setAutoCommit(false);

    conn.rollback();

    conn.setAutoCommit(true);

    form.setAction(us.getClientIntName("re_new","error"));

    abit_A.setKodAbiturienta(abit_A.getKodAbiturienta());
    abit_A.setSpecial1(abit_A.getSpecial1());
    abit_A.setFamilija((abit_A.getFamilija()).substring(0,1).toUpperCase()+(abit_A.getFamilija()).substring(1));
    abit_A.setImja((abit_A.getImja()).substring(0,1).toUpperCase()+(abit_A.getImja()).substring(1));
    abit_A.setOtchestvo((abit_A.getOtchestvo()).substring(0,1).toUpperCase()+(abit_A.getOtchestvo()).substring(1));
    abit_A.setGrajdanstvo(abit_A.getGrajdanstvo());
    abit_A.setPol(abit_A.getPol());
    abit_A.setDataRojdenija(abit_A.getDataRojdenija());
    abit_A.setTipDokumenta(abit_A.getTipDokumenta());
    abit_A.setSeriaDokumenta(abit_A.getSeriaDokumenta());
    abit_A.setNomerDokumenta(abit_A.getNomerDokumenta());
    abit_A.setKemVydDokument(abit_A.getKemVydDokument());
    abit_A.setMestoRojdenija(abit_A.getMestoRojdenija());
    abit_A.setUdostoverenieLgoty(abit_A.getUdostoverenieLgoty());
    abit_A.setDiplomOtlichija(abit_A.getDiplomOtlichija());
    abit_A.setDataVydDokumenta(abit_A.getDataVydDokumenta());
    abit_A.setGorod_Prop(abit_A.getGorod_Prop());
    abit_A.setUlica_Prop(abit_A.getUlica_Prop());
    abit_A.setDom_Prop(abit_A.getDom_Prop());
    abit_A.setKvart_Prop(abit_A.getKvart_Prop());
    abit_A.setGodOkonchanijaSrObrazovanija(abit_A.getGodOkonchanijaSrObrazovanija());
    abit_A.setNazvanie(abit_A.getNazvanie());
    abit_A.setNazvanieRajona(abit_A.getNazvanieRajona());
    abit_A.setNazvanieOblasti(abit_A.getNazvanieOblasti());
    abit_A.setTipOkonchennogoZavedenija(abit_A.getTipOkonchennogoZavedenija());
    abit_A.setNomerShkoly(abit_A.getNomerShkoly());
    abit_A.setKodLgot(abit_A.getKodLgot());
    abit_A.setKodZavedenija(abit_A.getKodZavedenija());
    abit_A.setNazv_DipBak(abit_A.getNazv_DipBak());
    abit_A.setNazv_DipSpec(abit_A.getNazv_DipSpec());
    abit_A.setNeed_Spo(abit_A.getNeed_Spo());
   // Integer kKurs = 1;
 //   stmt.setObject(30, kKurs, Types.INTEGER);
 //   abit_A.setKodKursov(abit_A.getKodKursov());
    abit_A.setKodLgot(abit_A.getKodLgot());
    abit_A.setKodMedali(abit_A.getKodMedali());
    abit_A.setKodTselevogoPriema(abit_A.getKodTselevogoPriema());
    abit_A.setSokr(abit_A.getSokr());
    abit_A.setPolnoeNaimenovanieZavedenija(abit_A.getPolnoeNaimenovanieZavedenija());
    abit_A.setVidDokSredObraz(abit_A.getVidDokSredObraz());
    abit_A.setTipDokSredObraz(abit_A.getTipDokSredObraz());
    abit_A.setSeriaAtt(abit_A.getSeriaAtt());
    abit_A.setNomerAtt(abit_A.getNomerAtt());
    abit_A.setKopijaSertifikata(abit_A.getKopijaSertifikata());
    abit_A.setNomerSertifikata(abit_A.getNomerSertifikata());
    abit_A.setShifrKursov(abit_A.getShifrKursov());
    abit_A.setShifrLgot(abit_A.getShifrLgot());
    abit_A.setShifrMedali(abit_A.getShifrMedali());
    abit_A.setInostrannyjJazyk(abit_A.getInostrannyjJazyk());
    abit_A.setNapravlenieOtPredprijatija(abit_A.getNapravlenieOtPredprijatija());
    abit_A.setShifrPriema(abit_A.getShifrPriema());
    /*abit_A.setSpecial2(abit_A.getSpecial2());
    abit_A.setS_okso_2(abit_A.getS_okso_2());
    abit_A.setS_okso_3(abit_A.getS_okso_3());
    abit_A.setS_okso_4(abit_A.getS_okso_4());
    abit_A.setS_okso_5(abit_A.getS_okso_5());
    abit_A.setS_okso_6(abit_A.getS_okso_6());*/
    
  
    
    abit_A.setBud_1(abit_A.getBud_1());
    abit_A.setBud_2(abit_A.getBud_2());
    abit_A.setBud_3(abit_A.getBud_3());
    abit_A.setBud_4(abit_A.getBud_4());
    abit_A.setBud_5(abit_A.getBud_5());
    abit_A.setBud_6(abit_A.getBud_6());
    abit_A.setDog_1(abit_A.getDog_1());
    abit_A.setDog_2(abit_A.getDog_2());
    abit_A.setDog_3(abit_A.getDog_3());
    abit_A.setDog_4(abit_A.getDog_4());
    abit_A.setDog_5(abit_A.getDog_5());
    abit_A.setDog_6(abit_A.getDog_6());
    abit_A.setDog_ok_1(abit_A.getDog_ok_1());
    abit_A.setDog_ok_2(abit_A.getDog_ok_2());
    abit_A.setDog_ok_3(abit_A.getDog_ok_3());
    abit_A.setDog_ok_4(abit_A.getDog_ok_4());
    abit_A.setDog_ok_5(abit_A.getDog_ok_5());
    abit_A.setDog_ok_6(abit_A.getDog_ok_6());
    abit_A.setOlimp_1(abit_A.getOlimp_1());
    abit_A.setOlimp_2(abit_A.getOlimp_2());
    abit_A.setOlimp_3(abit_A.getOlimp_3());
    abit_A.setOlimp_4(abit_A.getOlimp_4());
    abit_A.setOlimp_5(abit_A.getOlimp_5());
    abit_A.setOlimp_6(abit_A.getOlimp_6());
    abit_A.setTarget_1(abit_A.getTarget_1());
    abit_A.setTarget_2(abit_A.getTarget_2());
    abit_A.setTarget_3(abit_A.getTarget_3());
    abit_A.setTarget_4(abit_A.getTarget_4());
    abit_A.setTarget_5(abit_A.getTarget_5());
    abit_A.setTarget_6(abit_A.getTarget_6());
    abit_A.setFito_1(abit_A.getFito_1());
    abit_A.setFito_2(abit_A.getFito_2());
    abit_A.setFito_3(abit_A.getFito_3());
    abit_A.setFito_4(abit_A.getFito_4());
    abit_A.setFito_5(abit_A.getFito_5());
    abit_A.setFito_6(abit_A.getFito_6());
    abit_A.setSix_1(abit_A.getSix_1());
    abit_A.setSix_2(abit_A.getSix_2());
    abit_A.setSix_3(abit_A.getSix_3());
    abit_A.setSix_4(abit_A.getSix_4());
    abit_A.setSix_5(abit_A.getSix_5());
    abit_A.setSix_6(abit_A.getSix_6());
    abit_A.setThree_1(abit_A.getThree_1());
    abit_A.setThree_2(abit_A.getThree_2());
    abit_A.setThree_3(abit_A.getThree_3());
    abit_A.setThree_4(abit_A.getThree_4());
    abit_A.setThree_5(abit_A.getThree_5());
    abit_A.setThree_6(abit_A.getThree_6());
    abit_A.setDokumentyHranjatsja(abit_A.getDokumentyHranjatsja());
    abit_A.setTip_Spec(abit_A.getTip_Spec());
    abit_A.setStepen_Mag(abit_A.getStepen_Mag());
    abit_A.setNeed_Spo(abit_A.getNeed_Spo());
    abit_A.setTrudovajaDejatelnost(abit_A.getTrudovajaDejatelnost());
    abit_A.setNujdaetsjaVObschejitii(abit_A.getNujdaetsjaVObschejitii());
    abit_A.setNomerPlatnogoDogovora(abit_A.getNomerPlatnogoDogovora());
    abit_A.setForma_Ob1(abit_A.getForma_Ob1());
    abit_A.setForma_Ob2(abit_A.getForma_Ob2());
    abit_A.setForma_Ob3(abit_A.getForma_Ob3());
    abit_A.setTel(abit_A.getTel());
    abit_A.setPostgraduateStudies(abit_A.getPostgraduateStudies());
    abit_A.setTraineeship(abit_A.getTraineeship());
    abit_A.setInternship(abit_A.getInternship());
    abit_A.setAbitEmail(abit_A.getAbitEmail());
    abit_A.setDopAddress(abit_A.getDopAddress());
    abit_A.setProvidingSpecialCondition(abit_A.getProvidingSpecialCondition());
    abit_A.setReturnDocument(abit_A.getReturnDocument());
    abit_A.setPreemptiveRight(abit_A.getPreemptiveRight());
    abit_A.setSpecial8(abit_A.getSpecial8());
    abit_A.setSpecial9(abit_A.getSpecial9());
    abit_A.setExists_st_Mag(abit_A.getExists_st_Mag());
    abit_A.setStepen_Mag(abit_A.getStepen_Mag());
    
    
    abit_A.setStob(abit_A.getStob());
    abit_A.setStob_2(abit_A.getStob_2());
    abit_A.setStob_3(abit_A.getStob_3());
    abit_A.setStob_4(abit_A.getStob_4());
    abit_A.setStob_5(abit_A.getStob_5());
    abit_A.setStob_6(abit_A.getStob_6());
    abit_A.setPr1(abit_A.getPr1());
    abit_A.setPr1_2(abit_A.getPr1_2());
    abit_A.setPr1_3(abit_A.getPr1_3());
    abit_A.setPr1_4(abit_A.getPr1_4());
    abit_A.setPr1_5(abit_A.getPr1_5());
    abit_A.setPr1_6(abit_A.getPr1_6());
    abit_A.setPr2(abit_A.getPr2());
    abit_A.setPr2_2(abit_A.getPr2_2());
    abit_A.setPr2_3(abit_A.getPr2_3());
    abit_A.setPr2_4(abit_A.getPr2_4());
    abit_A.setPr2_5(abit_A.getPr2_5());
    abit_A.setPr2_6(abit_A.getPr2_6());
    abit_A.setPr3(abit_A.getPr3());
    abit_A.setPr3_2(abit_A.getPr3_2());
    abit_A.setPr3_3(abit_A.getPr3_3());
    abit_A.setPr3_4(abit_A.getPr3_4());
    abit_A.setPr3_5(abit_A.getPr3_5());
    abit_A.setPr3_6(abit_A.getPr3_6());
    
    abit_A.setNazv_DipBak(abit_A.getNazv_DipBak());
    abit_A.setNazv_DipSpec(abit_A.getNazv_DipSpec());
    abit_A.setNomerPotoka(abit_A.getNomerPotoka());
    abit_A.setShifrFakulteta(abit_A.getShifrFakulteta());
    abit_A.setSpecial2(abit_A.getSpecial2());
    abit_A.setSpecial222(abit_A.getSpecial222());
    abit_A.setSpecial3(abit_A.getSpecial3());
    abit_A.setSpecial4(abit_A.getSpecial4());
    abit_A.setSpecial5(abit_A.getSpecial5());
    abit_A.setSpecial6(abit_A.getSpecial6());
    abit_A.setSpecial7(abit_A.getSpecial7());
    abit_A.setSpecial13(abit_A.getSpecial13());
    abit_A.setSpecial10(abit_A.getSpecial10());
    
    abit_A.setNpd1(abit_A.getNpd1());
    abit_A.setNpd2(abit_A.getNpd2());
    abit_A.setNpd3(abit_A.getNpd3());
    abit_A.setNpd4(abit_A.getNpd4());
    abit_A.setNpd5(abit_A.getNpd5());
    abit_A.setNpd6(abit_A.getNpd6());
    
    abit_A.setTname1(abit_A.getTname1());
    abit_A.setTname2(abit_A.getTname2());
    abit_A.setTname3(abit_A.getTname3());
    abit_A.setTname4(abit_A.getTname4());
    abit_A.setTname5(abit_A.getTname5());
    abit_A.setTname6(abit_A.getTname6());
    
    abit_A.setOp1(abit_A.getOp1());
    abit_A.setOp2(abit_A.getOp2());
    abit_A.setOp3(abit_A.getOp3());
    abit_A.setOp4(abit_A.getOp4());
    abit_A.setOp5(abit_A.getOp5());
    abit_A.setOp6(abit_A.getOp6());
    
    abit_A.setRlgot1(abit_A.getRlgot1());
    abit_A.setRlgot2(abit_A.getRlgot2());
    abit_A.setRlgot3(abit_A.getRlgot3());
    abit_A.setRlgot4(abit_A.getRlgot4());
    abit_A.setRlgot5(abit_A.getRlgot5());
    abit_A.setRlgot6(abit_A.getRlgot6());

    abit_A.setPrr1(abit_A.getPrr1());
    abit_A.setPrr2(abit_A.getPrr2());
    abit_A.setPrr3(abit_A.getPrr3());
    abit_A.setPrr4(abit_A.getPrr4());
    abit_A.setPrr5(abit_A.getPrr5());
    abit_A.setPrr6(abit_A.getPrr6());
    
    abit_A.setSog1(abit_A.getSog1());
    abit_A.setSog2(abit_A.getSog2());
    abit_A.setSog3(abit_A.getSog3());
    abit_A.setSog4(abit_A.getSog4());
    abit_A.setSog5(abit_A.getSog5());
    abit_A.setSog6(abit_A.getSog6());
    abit_A.setZajavlen(abit_A.getZajavlen());
    
// Данные о полученных оценках для автоматизации повторного ввода

    Enumeration paramNames = request.getParameterNames();
    ArrayList ege  = new ArrayList();
    ArrayList exam = new ArrayList();
    ArrayList kpr  = new ArrayList();
    ArrayList kprx = new ArrayList();
    ArrayList kpre = new ArrayList();
    ArrayList pr   = new ArrayList();
    ArrayList oat	= new ArrayList();
    ArrayList at	= new ArrayList();
    ArrayList kpreat = new ArrayList();
    ArrayList kprat = new ArrayList();
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();
      String paramValue[] = request.getParameterValues(paramName);

      if(paramName.indexOf("Ege_note") != -1) {
        ege.add(paramValue[0]);                 
        kpre.add(paramName.substring(8));  // Код предмета
        stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta LIKE ?");
        stmt.setObject(1,new Integer(paramName.substring(8)),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) pr.add(rs.getString(1));  // Предмет
      }
      if(paramName.indexOf("Examen") != -1) {
        exam.add(StringUtil.toDB(paramValue[0]));
        kprx.add(paramName.substring(6));  // Код предмета
      }
      if(paramName.indexOf("Attestat") != -1) {
    	  at.add(paramValue[0]);                 
          kpreat.add(paramName.substring(8));  // Код предмета
          stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta LIKE ?");
          stmt.setObject(1,new Integer(paramName.substring(8)),Types.INTEGER);
          rs = stmt.executeQuery();
          if(rs.next()) oat.add(rs.getString(1));  // Предмет
        }
      
    }

// Сортировка массивов оценок 
    int index=0,maxKod=0;
    ArrayList sortedMass = new ArrayList();
    ArrayList sortedMass2 = new ArrayList();
    
    while(kpreat.size()!=0){
        index=0;
        maxKod=StringUtil.toInt(""+kpreat.get(0),0);
        for(int cur_ind=0;cur_ind<kpreat.size();cur_ind++){   
           if(StringUtil.toInt(""+kpreat.get(cur_ind),0)<=maxKod){
             maxKod=StringUtil.toInt(""+kpreat.get(cur_ind),0);
             index=cur_ind;
           }
        }
        sortedMass.add(at.get(index));
        sortedMass2.add(oat.get(index));
        kprat.add(kpreat.get(index));
        kpreat.remove(index);
        at.remove(index);
        oat.remove(index);
     }
    at.clear();
    oat.clear();
    for(int i=0;i<kprat.size();i++) at.add(sortedMass.get(i));
    for(int i=0;i<kprat.size();i++) oat.add(sortedMass2.get(i));
    index=maxKod=0;
    sortedMass.clear();
    sortedMass2.clear();

// оценки ЕГЭ по коду предмета

    while(kpre.size()!=0){
       index=0;
       maxKod=StringUtil.toInt(""+kpre.get(0),0);
       for(int cur_ind=0;cur_ind<kpre.size();cur_ind++){   
          if(StringUtil.toInt(""+kpre.get(cur_ind),0)<=maxKod){
            maxKod=StringUtil.toInt(""+kpre.get(cur_ind),0);
            index=cur_ind;
          }
       }
       sortedMass.add(ege.get(index));
       sortedMass2.add(pr.get(index));
       kpr.add(kpre.get(index));
       kpre.remove(index);
       ege.remove(index);
       pr.remove(index);
    }

// Копирование отсортированного массива в исходный массив
    ege.clear();
    pr.clear();
    for(int i=0;i<kpr.size();i++) ege.add(sortedMass.get(i));
    for(int i=0;i<kpr.size();i++) pr.add(sortedMass2.get(i));

// Признак экзамена по коду предмета
    index=maxKod=0;
    sortedMass.clear();

    while(kprx.size()!=0){
      index=0;
      maxKod=StringUtil.toInt(""+kprx.get(0),0);
      for(int cur_ind=0;cur_ind<kprx.size();cur_ind++){   
         if(StringUtil.toInt(""+kprx.get(cur_ind),0)<=maxKod){
           maxKod=StringUtil.toInt(""+kprx.get(cur_ind),0);
           index=cur_ind;
         }
      }
      sortedMass.add(exam.get(index));
      kprx.remove(index);
      exam.remove(index);
    }

// Копирование отсортированного массива в исходный массив
    exam.clear();
    for(int i=0;i<kpr.size();i++) exam.add(sortedMass.get(i));

    abit_A_S1.clear();

    for(int i=0;i<kpr.size();i++){
      AbiturientBean abit_TMP = new AbiturientBean();
      abit_TMP.setExamen(""+exam.get(i));
      abit_TMP.setEge(""+ege.get(i));
//      if(at.size()<=i){
//    	  abit_TMP.setOtsenkaAtt(""+at.get(i));
//      }else{
//      abit_TMP.setOtsenka_Att("0");
//      }
      abit_TMP.setKodPredmeta(new Integer(""+kpr.get(i)));
      abit_TMP.setPredmet(""+pr.get(i));
      abit_A_S1.add(abit_TMP);
    }
    /*for(int i=0;i<at.size();i++){
    	 AbiturientBean abit_TMP = new AbiturientBean();
    	 abit_TMP.setOtsenkaAtt(""+at.get(i));
    	 abit_TMP.setKodPredmeta(new Integer(""+kpr.get(i)));
         abit_TMP.setPredmet(""+pr.get(i));
    	 abit_A_S11.add(abit_TMP);
    }*/
    
    nomerPotoka=abit_A.getNomerPotoka();
	  if(nomerPotoka==1){
		  zapros="('с','б')";
	  }else if(nomerPotoka==2){
		  zapros="('м')";
	  }else if(nomerPotoka==3){
		  zapros="('и')";
	  }else if(nomerPotoka==4){
		  zapros="('о')";
	  }else if(nomerPotoka==5){
		  zapros="('а')";
	  }else if(nomerPotoka==6){
		  zapros="('п')";
	  }
	  
    pstmt = conn.prepareStatement("SELECT DISTINCT f.KodFakulteta,f.AbbreviaturaFakulteta,f.Fakultet FROM Fakultety f, Spetsialnosti s WHERE KodVuza LIKE ? AND f.kodfakulteta=s.kodfakulteta AND s.tip_spec like 'о' AND s.edulevel IN "+zapros+" ORDER BY AbbreviaturaFakulteta ASC");
    pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = pstmt.executeQuery();
    while (rs.next()) {
      AbiturientBean abit_TMP = new AbiturientBean();
      abit_TMP.setKodFakulteta(rs.getInt(1));
      abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
      abit_SD_S1.add(abit_TMP);
    }
    
    pstmt = conn.prepareStatement("SELECT DISTINCT f.KodFakulteta,f.AbbreviaturaFakulteta,f.Fakultet FROM Fakultety f, Spetsialnosti s WHERE KodVuza LIKE ? AND f.kodfakulteta=s.kodfakulteta AND s.tip_spec not like 'о' AND s.edulevel IN "+zapros+" ORDER BY AbbreviaturaFakulteta ASC");
    pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = pstmt.executeQuery();
    while (rs.next()) {
      AbiturientBean abit_TMP = new AbiturientBean();
      abit_TMP.setKodFakulteta(rs.getInt(1));
      abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
      abit_SD_S11.add(abit_TMP);
    }

    
      stmt = conn.prepareStatement("SELECT DISTINCT KodLgot,ShifrLgot FROM Lgoty WHERE KodVuza LIKE ? ORDER BY 1 ASC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setKodLgot(new Integer(rs.getInt(1)));
        abit_TMP.setShifrLgot(rs.getString(2));
        abit_A_S4.add(abit_TMP);
      }
      
      stmt = conn.prepareStatement("SELECT DISTINCT nazvanie FROM Strany");
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setNazv_DipBak(rs.getString(1));
        abit_A_Strany.add(abit_TMP);
      }

      stmt = conn.prepareStatement("SELECT DISTINCT KodMedali,ShifrMedali FROM Medali WHERE KodVuza LIKE ? ORDER BY 1 ASC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setKodMedali(new Integer(rs.getInt(1)));
        abit_TMP.setShifrMedali(rs.getString(2));
        abit_A_S6.add(abit_TMP);
      }

      abit_A.setSpecial1("");
      stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? and kodpredmeta not like 13 ORDER BY KodPredmeta ASC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
        abit_A.setSpecial1(abit_A.getSpecial1()+"%"+rs.getString(1));
        abit_TMP.setPredmet((rs.getString(2)).substring(0,1).toUpperCase()+(rs.getString(2)).substring(1));
        abit_A_S7.add(abit_TMP);
      }
      
      stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? and  (KodPredmeta = 3 or KodPredmeta = 10 or KodPredmeta = 5 or KodPredmeta = 1 or KodPredmeta = 8 or KodPredmeta = 4 or KodPredmeta = 9 or KodPredmeta = 2) ORDER BY KodPredmeta ASC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
       // abit_A.setSpecial1(abit_A.getSpecial1()+"%"+rs.getString(1));
        abit_TMP.setPredmet((rs.getString(2)).substring(0,1).toUpperCase()+(rs.getString(2)).substring(1));
        abit_A_S10.add(abit_TMP);
      }

      stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodZavedenija,Ordr FROM Zavedenija WHERE KodVuza LIKE ? ORDER BY Ordr,Sokr ASC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setSokr(rs.getString(1));
        abit_TMP.setKodZavedenija(new Integer(rs.getInt(2)));
        abit_A_S8.add(abit_TMP);
      }
      
      
      /*Пушкарев добавление классификатора Национальностей 20 03 2014*/
      stmt = conn.prepareStatement("SELECT DISTINCT ID, name from DictionaryCode21 order by name ");
      rs = stmt.executeQuery();
      while (rs.next()) {
      NationalityBean nationalityBean = new NationalityBean();
      nationalityBean.setId((Integer)rs.getInt(1));
      nationalityBean.setName(rs.getString(2));
      if (nationalityBean.getId() == 1){
      	nationalityList.add(0, nationalityBean);
      }
      if (nationalityBean.getId() == 2){
      	nationalityList.add(0, nationalityBean);
      }
      else
      nationalityList.add(nationalityBean);
      
      
      }

      stmt = conn.prepareStatement("SELECT DISTINCT KodTselevogoPriema,ShifrPriema FROM TselevojPriem WHERE KodVuza LIKE ? ORDER BY 1 ASC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setKodTselevogoPriema(new Integer(rs.getInt(1)));
        abit_TMP.setShifrPriema(rs.getString(2));
        abit_A_S9.add(abit_TMP);
      }
    
    
    
    
    
    
 }



/************************************************************************************************/
/****************************      ОБНОВЛЕНИЕ ПОЛЯ ЗАВЕДЕНИЙ      *******************************/
/************************************************************************************************/

    abit_A_S8.clear();
    stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodZavedenija,Ordr FROM Zavedenija WHERE KodVuza LIKE ? ORDER BY Ordr,Sokr ASC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = stmt.executeQuery();
    while (rs.next()) {
       AbiturientBean abit_TMP = new AbiturientBean();
       abit_TMP.setSokr(rs.getString(1));
       abit_TMP.setKodZavedenija(new Integer(rs.getInt(2)));
       abit_A_S8.add(abit_TMP);
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
     }
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S3", abit_SD_S3);
        request.setAttribute("abit_SD_S11", abit_SD_S11);
        request.setAttribute("mess", mess);
        request.setAttribute("abit_A", abit_A);
        request.setAttribute("abits_A", abits_A);
        request.setAttribute("abit_forms", abit_forms);
        request.setAttribute("abit_osnovs", abit_osnovs);
        request.setAttribute("abit_A_S1", abit_A_S1);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S5", abit_A_S5);
        request.setAttribute("abit_A_S6", abit_A_S6);
        request.setAttribute("abit_A_S7", abit_A_S7);
        request.setAttribute("abit_A_S8", abit_A_S8);
        request.setAttribute("abit_A_S9", abit_A_S9);
        request.setAttribute("abit_A_S10", abit_A_S10);
        request.setAttribute("abit_A_S11", abit_A_S11);
        request.setAttribute("nationalityList", nationalityList);
        request.setAttribute("abit_A_Kladr", abit_A_Kladr);
        request.setAttribute("abit_A_Rajon", abit_A_Rajon);
        request.setAttribute("abit_A_Punkt", abit_A_Punkt);
        request.setAttribute("obr_A_Rajon", obr_A_Rajon);
        request.setAttribute("obr_A_Punkt", obr_A_Punkt);


        if(error) return mapping.findForward("error");

        return mapping.findForward("success");
    }
}