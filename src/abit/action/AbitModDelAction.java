package abit.action;


import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import javax.naming.*;
import javax.sql.*;

import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*;

public class AbitModDelAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        PreparedStatement stmt1          = null;
        PreparedStatement    pstmt              = null;
        PreparedStatement    pstmt1              = null;
        ResultSet         rs            = null;
        ResultSet         rs1            = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        AbiturientForm    form          = (AbiturientForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        AbiturientBean    abit_A1       = form.getBean(request, errors);
        AbiturientBean 	  sb = form.getBean(request, errors);
        MessageBean       mess          = new MessageBean();
        boolean           error         = false;
        boolean           result        = false;
        boolean           re_enter      = false;
        boolean           otsenki       = false;
        ActionForward     f             = null;
        String            nomer_ab      = null;
        String            kPunkta       = null;
        String            kRajona       = null;
        String            kOblasti      = null;
        String            kPredmeta     = new String();
        String            kFormy_Ob     = new String();
        String            kOsnovy_Ob    = new String();
        String            kSpec         = new String();
        String            kFak          = new String();
        String            Abbr_Spec     = new String();
        String            Abbr_Spec2    = new String();
        String            Tip_Spec      = new String();
        String            Tip_Spec2     = new String();
        String            note          = new String();
        String            shifr_Fak     = new String();
        String            s_okso_1      = "";
        String            s_okso_2      = "";
        String            s_okso_3      = "";
        String            s_okso_4      = "";
        String            s_okso_5      = "";
        String            s_okso_6      = "";
        int               kZavedenija   = 1;
        int               kol_predm     = 0;
        int               Col_Specs     = 0;
        int ba=0;
        int ordr_ab=0;
        ArrayList         abits_A       = new ArrayList();
        ArrayList         notesSk_A     = new ArrayList();
        ArrayList         notesEx_A     = new ArrayList();
        ArrayList         notes         = new ArrayList();
        ArrayList         predmets      = new ArrayList();
        ArrayList         specials      = new ArrayList();
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
        ArrayList		  att			= new ArrayList();
        ArrayList		  nationalityList = new ArrayList();
        ArrayList		  abit_A_Strany	= new ArrayList();
        
        ArrayList         abit_A_Kladr     = new ArrayList();
        ArrayList         abit_A_Rajon     = new ArrayList();
        ArrayList         abit_A_Punkt     = new ArrayList();
        ArrayList         obr_A_Rajon     = new ArrayList();
        ArrayList         obr_A_Punkt     = new ArrayList();
        
        int countt=0;
        int tar=1;
        int chet=1;
        int pr1=0;
        int pr2=0;
        int pr3=0;
        int pr4=0;
        int pr5=0;
        int pr6=0;
        int pr7=0;
        int sum=0;
        int sumd=0;
        int sumt=0;
        String s4 = "Карточка поступающего на БАКАЛАВРИАТ/СПЕЦИАЛИТЕТ";
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

        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S22         = new ArrayList();
        ArrayList            abit_SD_S222         = new ArrayList();
        ArrayList            abit_SD_S3         = new ArrayList();
        ArrayList            abit_SD_S33         = new ArrayList();
        ArrayList            abit_SD_S333        = new ArrayList();
        ArrayList            abit_SD_S11         = new ArrayList();
        UserBean          user          = (UserBean)session.getAttribute("user");
        String spravka=null;
        int pp=0, pp1=0, pp2=0, pp3=0, pp4=0, pp5=0;

        if (user==null || user.getGroup()==null || !(user.getGroup().getTypeId()==1 || user.getGroup().getTypeId()==2 || user.getGroup().getTypeId()==3)) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

// Установка локализации
        request.setAttribute( "abitModDelAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "abitSrchForm", form );

/*****************  Возврат к предыдущей странице   *******************/

          if(us.quit("exit")) return mapping.findForward("back");

// Переход к странице поиска

          if (request.getParameter("srch_res")!=null) {
             session.setAttribute("resrch","1");
             return mapping.findForward("goto");
          }

/************************************************************************************************/
/********************** Подготовка данных для ввода с помощью селекторов ************************/
/************************************************************************************************/

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
          
          
          pstmt = conn.prepareStatement("SELECT DISTINCT f.KodFakulteta,f.AbbreviaturaFakulteta,f.Fakultet FROM Fakultety f, Spetsialnosti s WHERE KodVuza LIKE ? AND f.kodfakulteta=s.kodfakulteta AND s.tip_spec like 'о' ORDER BY AbbreviaturaFakulteta ASC");
          pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodFakulteta(rs.getInt(1));
            abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
            abit_SD_S1.add(abit_TMP);
          }
          
          pstmt = conn.prepareStatement("SELECT DISTINCT f.KodFakulteta,f.AbbreviaturaFakulteta,f.Fakultet FROM Fakultety f, Spetsialnosti s WHERE KodVuza LIKE ? AND f.kodfakulteta=s.kodfakulteta AND s.tip_spec not like 'о' ORDER BY AbbreviaturaFakulteta ASC");
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
            stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ?  and kodpredmeta not like '12' and kodpredmeta not like '13' ORDER BY KodPredmeta ASC");
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
            stmt = conn.prepareStatement("SELECT DISTINCT ID, name from DictionaryCode21");
            rs = stmt.executeQuery();
            while (rs.next()) {
            NationalityBean nationalityBean = new NationalityBean();
            nationalityBean.setId((Integer)rs.getInt(1));
            nationalityBean.setName(rs.getString(2));
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


/************************************************************************************************/
/**************************    Установка соединения с ФБС ЕГЭ    ********************************/
/************************   и получение информации по баллам ЕГЭ    *****************************/
/************************************************************************************************/

   /* String FBS_query = null;

     stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,SeriaDokumenta,NomerDokumenta,NomerSertifikata FROM Abiturient WHERE KodAbiturienta LIKE ?");
     stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
     rs = stmt.executeQuery();
     if(rs.next()) {
       
       if(rs.getString(6) == null) {

// Неизвестен номер сертификата

          FBS_query="http://fbsege.ru/Certificates/CommonNationalCertificates/RequestByPassportResult.aspx?LastName="+URLEncoder.encode(rs.getString(1), Constants.WEB_ENCODING)+"&FirstName="+URLEncoder.encode(rs.getString(2), Constants.WEB_ENCODING)+"&PatronymicName="+URLEncoder.encode(rs.getString(3), Constants.WEB_ENCODING)+"&Series="+URLEncoder.encode(rs.getString(4), Constants.WEB_ENCODING)+"&Number="+URLEncoder.encode(rs.getString(5), Constants.WEB_ENCODING);

       } else {

// Номер сертификата известен

          FBS_query="http://fbsege.ru/Certificates/CommonNationalCertificates/CheckResult.aspx?number="+URLEncoder.encode(rs.getString(6), Constants.WEB_ENCODING)+"&LastName="+URLEncoder.encode(rs.getString(1), Constants.WEB_ENCODING)+"&FirstName="+URLEncoder.encode(rs.getString(2), Constants.WEB_ENCODING)+"&PatronymicName="+URLEncoder.encode(rs.getString(3), Constants.WEB_ENCODING)+"&SubjectMarks=&Year=";
       }
     } 
      try {
           URL url = new URL(FBS_query);

           HttpURLConnection connection = (HttpURLConnection)url.openConnection();

           connection.setRequestMethod("POST");

           connection.setDoOutput(true);

           connection.setUseCaches(false);

           OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

           String web_data = URLEncoder.encode("login", Constants.WEB_ENCODING) + "=" + URLEncoder.encode(Constants.FBS_EGE_LOGIN, Constants.WEB_ENCODING);

           web_data += "&" + URLEncoder.encode("password", Constants.WEB_ENCODING) + "=" + URLEncoder.encode(Constants.FBS_EGE_PASSWD, Constants.WEB_ENCODING);

           out.write(web_data);

           out.flush();

           out.close();

           BufferedReader br = new BufferedReader( new InputStreamReader(connection.getInputStream()));

           br.close();

           connection.disconnect();

       } catch(IOException ex) { // Нет соединения с сайтом ФБС по какой-то причине  
    	   
       }

    abit_A.setSpecial6(FBS_query);*/

/************************************************************************************************/


            if ( form.getAction() == null )  { 

               form.setAction(us.getClientIntName("md_dl","init"));


/************************************************************************************************/
/************************************************************************************************/
/**********  ОТОБРАЖЕНИЕ ДАННЫХ АБИТУРИЕНТА С УЧЕТОМ ОЦЕНОК ПО ЗАПРОСУ ИЗ ФОРМЫ ПОИСКА  *********/
/************************************************************************************************/
/************************************************************************************************/

            } else if ( form.getAction().equals("give_ots") ) {

            us.getClientIntName("give_ots","ots_"+abit_A.getKodAbiturienta());

// Личные и учебные данные

            stmt = conn.prepareStatement("SELECT DISTINCT a.Familija,a.Imja,a.Otchestvo,a.Prinjat,a.DokumentyHranjatsja,a.Pol,kl.name,a.Ulica_Prop,a.Dom_Prop,a.Kvart_Prop, a.Tel,kl.socr FROM Abiturient a, kladr kl WHERE a.gorod_prop=kl.code AND a.KodVuza LIKE ? AND a.KodAbiturienta LIKE ?");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            stmt.setObject(2, abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
              abit_A.setFamilija(rs.getString(1));
              abit_A.setImja(rs.getString(2));
              abit_A.setOtchestvo(rs.getString(3));
              abit_A.setPrinjat(rs.getString(4));
              abit_A.setDokumentyHranjatsja(rs.getString(5));
              abit_A.setPol(rs.getString(6));
              abit_A.setGorod_Prop(rs.getString(12)+" "+rs.getString(7)+", ул."+rs.getString(8)+", д."+rs.getString(9)+", кв."+rs.getString(10));
              abit_A.setTel(rs.getString(11));
            }
// Предметы и их количество

            abit_A_S1.clear();
            stmt = conn.prepareStatement("SELECT a.KodPredmeta, a.OtsenkaEge, b.sokr,a.Examen FROM zajavlennyeshkolnyeotsenki a, NazvanijaPredmetov b  WHERE b.kodpredmeta not like 13 AND b.KodPredmeta=a.KodPredmeta and KodAbiturienta=?");
            stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            while(rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
              abit_TMP.setEge(rs.getString(2));
              abit_TMP.setPredmet(rs.getString(3));
              abit_TMP.setExamen(StringUtil.ntv(rs.getString(4)).trim());
              abit_A_S1.add(abit_TMP);
            }

// Специальность зачисления

              stmt = conn.prepareStatement("SELECT s.Abbreviatura,s.ShifrSpetsialnostiOKSO FROM Spetsialnosti s,Abiturient a WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND a.KodVuza LIKE ? AND a.KodAbiturienta LIKE ?");
              stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
              stmt.setObject(2, abit_A.getKodAbiturienta(),Types.INTEGER);
              rs = stmt.executeQuery();
              if(rs.next()) {
                abit_A.setSpecial4(rs.getString(1));
                abit_A.setSpecial5("("+rs.getString(2)+")");
              } else abit_A.setSpecial4("-");
            

// Конкурс

              stmt = conn.prepareStatement("select kon.nomerlichnogodela, s.nazvaniespetsialnosti, s.tip_spec, kon.bud,kon.dog,tp.TselevojPriem,kon.tname, kon.olimp, l.shifrlgot, kon.rlgot,kon.dog_ok,kon.npd, kon.prioritet,kon.kodspetsialnosti from konkurs kon, spetsialnosti s, tselevojpriem tp, lgoty l where l.kodlgot=kon.op AND kon.kodspetsialnosti=s.kodspetsialnosti AND kon.kodabiturienta LIKE ? AND tp.kodtselevogopriema=kon.target ORDER BY kon.prioritet asc");
              stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
              rs = stmt.executeQuery();
              while(rs.next()) {        
            	pr1=0;
              	pr2=0;
              	pr3=0;
              	pr4=0;
              	pr5=0;
              	pr6=0;
              	pr7=0;
              	sum=0;
              	sumt=0;
              	sumd=0;
                AbiturientBean abit_TMPx = new AbiturientBean();
                abit_TMPx.setNomerLichnogoDela(rs.getString(1));
                abit_TMPx.setNazvanieSpetsialnosti(rs.getString(2));
                abit_TMPx.setTip_Spec(rs.getString(3));
                abit_TMPx.setBud_1(StringUtil.val_to_plus(rs.getString(4)));
                abit_TMPx.setDog_1(StringUtil.val_to_plus(rs.getString(5)));
                abit_TMPx.setTarget_1(rs.getString(6));
                abit_TMPx.setTname1(rs.getString(7));
                abit_TMPx.setOlimp_1(rs.getString(8));
                abit_TMPx.setOp1(rs.getString(9));
                abit_TMPx.setRlgot1(rs.getString(10));
                abit_TMPx.setDog_ok_1(rs.getString(11));
                abit_TMPx.setNpd1(rs.getString(12));
                abit_TMPx.setKonkurs_1(rs.getString(13));
                stmt1=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso, ekzamenynaspetsialnosti ens WHERE ens.kodspetsialnosti LIKE "+rs.getString(14)+"  AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta LIKE ? AND ens.prioritet LIKE '1'");
                stmt1.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
                rs1=stmt1.executeQuery();
                if(rs1.next()){
                	pr1=Integer.parseInt(rs1.getString(1));
                }
                stmt1=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso, ekzamenynaspetsialnosti ens WHERE ens.kodspetsialnosti LIKE "+rs.getString(14)+"  AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta LIKE ? AND ens.prioritet LIKE '2'");
                stmt1.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
                rs1=stmt1.executeQuery();
                if(rs1.next()){
                	pr2=Integer.parseInt(rs1.getString(1));
                }
                stmt1=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso, ekzamenynaspetsialnosti ens WHERE ens.kodspetsialnosti LIKE "+rs.getString(14)+"  AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta LIKE ? AND ens.prioritet LIKE '3'");
                stmt1.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
                rs1=stmt1.executeQuery();
                if(rs1.next()){
                	pr3=Integer.parseInt(rs1.getString(1));
                }
                stmt1=conn.prepareStatement("SELECT DISTINCT zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso, ekzamenynaspetsialnosti ens WHERE ens.kodspetsialnosti LIKE "+rs.getString(14)+"  AND ens.kodpredmeta=zso.kodpredmeta AND zso.kodabiturienta LIKE ? AND ens.prioritet LIKE '4'");
                stmt1.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
                rs1=stmt1.executeQuery();
                if(rs1.next()){
                	pr4=Integer.parseInt(rs1.getString(1));
                }
                sum=pr1+pr2+pr3+pr4;
                abit_TMPx.setSpecial23(sum);
                
                pr1=0;
            	pr2=0;
            	pr3=0;
            	pr4=0;
            	pr5=0;
            	pr6=0;
            	pr7=0;
                stmt1=conn.prepareStatement("SELECT kon.kodabiturienta,adi.BallAtt,adi.BallSGTO,adi.BALLZGTO,adi.BallPOI,adi.BallSoch,adi.TrudovajaDejatelnost,kon.olimp from abitdopinf adi, konkurs kon WHERE kon.kodabiturienta LIKE ? AND adi.kodabiturienta=kon.kodabiturienta AND  kon.kodspetsialnosti LIKE "+rs.getString(14)+" ");
                stmt1.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
                rs1=stmt1.executeQuery();
                if(rs1.next()){
                	
                	if(!rs1.getString(2).equals("да")&&!rs1.getString(2).equals("нет")){
                	pr1=Integer.parseInt(rs1.getString(2));
                	}
                	if(!rs1.getString(3).equals("да")&&!rs1.getString(3).equals("нет")){
                	pr2=Integer.parseInt(rs1.getString(3));
                	}
                	if(!rs1.getString(4).equals("да")&&!rs1.getString(4).equals("нет")){
                	pr3=Integer.parseInt(rs1.getString(4));
                	}
                	if(!rs1.getString(5).equals("да")&&!rs1.getString(5).equals("нет")){
                	pr4=Integer.parseInt(rs1.getString(5));
                	}
                	if(!rs1.getString(6).equals("да")&&!rs1.getString(6).equals("нет")){
                	pr5=Integer.parseInt(rs1.getString(6));
                	}
                	if(!rs1.getString(7).equals("да")&&!rs1.getString(7).equals("нет")){
                	pr6=Integer.parseInt(rs1.getString(7));
                	}
                	if(!rs1.getString(8).equals("да")&&!rs1.getString(8).equals("нет")){
                	pr7=Integer.parseInt(rs1.getString(8));
                	}
                	sumd=pr1+pr2+pr3+pr4+pr5+pr6+pr7;
                }
                abit_TMPx.setSpecial24(sumd);
                abit_TMPx.setSpecial25(sumd+sum);
                
                
                
                
                
                specials.add(abit_TMPx);
              }

              otsenki = true;


/************************************************************************************************/
/************************************************************************************************/
/***************  ЧТЕНИЕ ИНФОРМАЦИИ ИЗ БАЗЫ ДАННЫХ ДЛЯ ЕЕ ОТОБРАЖЕНИЯ В КАРТОЧКЕ  ***************/
/************************************************************************************************/
/************************************************************************************************/

            } else if ( form.getAction().equals("mod_del") ) {

                String kladrOblast = "";
                String kladrRajon = "";
                
                String ObrOblast = "";
                
                String ObrRajon = "";

// Выводим кнопку удаления абитуриента из системы для оператора ( Id == 1 )

                if( user.getGroup().getTypeId()==1 ) abit_A.setMay_del("yes");

                stmt = conn.prepareStatement("SELECT Abiturient.KodSpetsialnosti,NomerLichnogoDela,Familija,Imja,Otchestvo,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,MestoRojdenija,TipDokSredObraz,VidDokSredObraz,DataRojdenija,Pol,Grajdanstvo,GodOkonchanijaSrObrazovanija,TipOkonchennogoZavedenija,NomerShkoly,KodZavedenija,InostrannyjJazyk,NujdaetsjaVObschejitii,KodOblasti,KodRajona,KodPunkta,SeriaAtt,NomerAtt,Ordr,Gorod_Prop,Ulica_Prop,Dom_Prop,Kvart_Prop,Tel,NomerPlatnogoDogovora,KodAbiturienta,KodStrany,KodStranyP,KodOblastiP,KodRajonaP,NomerPotoka,DokumentyHranjatsja,Gruppy.Gruppa,abiturient.kodlgot,abiturient.kodpodrazdelenija FROM Gruppy,Abiturient,Fakultety,Spetsialnosti WHERE Abiturient.kodgruppy=gruppy.kodgruppy AND Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_A.setKodSpetsialnosti(new Integer(rs.getInt(1)));
                  abit_A.setNomerLichnogoDela(rs.getString(2));
                  abit_A.setFamilija(rs.getString(3));
                  abit_A.setImja(rs.getString(4));
                  abit_A.setOtchestvo(rs.getString(5));
                  abit_A.setTipDokumenta(rs.getString(6));
                  abit_A.setNomerDokumenta(rs.getString(7));
                  abit_A.setSeriaDokumenta(rs.getString(8));
                  abit_A.setDataVydDokumenta(rs.getString(9));
                  abit_A.setKemVydDokument(rs.getString(10));
                  abit_A.setMestoRojdenija(rs.getString(11));
                  abit_A.setTipDokSredObraz(rs.getString(12));
                  abit_A.setVidDokSredObraz(rs.getString(13));
                  abit_A.setDataRojdenija(rs.getString(14));
                  abit_A.setPol(rs.getString(15));
                  abit_A.setGrajdanstvo(rs.getString(16));
                  abit_A.setGodOkonchanijaSrObrazovanija(rs.getInt(17));
                  abit_A.setTipOkonchennogoZavedenija(rs.getString(18));
                  abit_A.setNomerShkoly(rs.getString(19));
                  abit_A.setKodZavedenija(rs.getInt(20));
                  abit_A.setInostrannyjJazyk(rs.getString(21));
                  abit_A.setNujdaetsjaVObschejitii(rs.getString(22));
                  abit_A.setNazvanieOblasti(rs.getString(23));
                  ObrOblast = rs.getString(23);
                  abit_A.setNazvanieRajona(rs.getString(24));
                  ObrRajon = rs.getString(24);
                  abit_A.setNazvanie(rs.getString(25));
                  //String ObrPunkr = rs.getString(25);
                  abit_A.setSeriaAtt(rs.getString(26));
                  abit_A.setNomerAtt(rs.getString(27));
                  ordr_ab=rs.getInt(28);
                  abit_A.setGorod_Prop(rs.getString(29));
                  abit_A.setUlica_Prop(rs.getString(30));
                  abit_A.setDom_Prop(rs.getString(31));
                  abit_A.setKvart_Prop(rs.getString(32));
                  abit_A.setTel(rs.getString(33));
                  abit_A.setNpd1(rs.getString(34));
                  abit_A.setKodAbiturienta(rs.getInt(35));
                  abit_A.setTraineeship(rs.getString(36));
                  abit_A.setNazv_DipBak(rs.getString(37));
                  abit_A.setNazv_DipSpec(rs.getString(38));
                  kladrOblast = rs.getString(38);
                  abit_A.setNeed_Spo(rs.getString(39));
                  kladrRajon  = rs.getString(39);
                  abit_A.setNomerPotoka(rs.getInt(40));
                //  kladrPunkt = rs.getInt(40);
                }
                
                if(abit_A.getNomerPotoka()==1){
            		 mess.setStatus(s4);
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
            	abit_A.setDokumentyHranjatsja(rs.getString(41));
            	abit_A.setGruppa(rs.getString(42));
            	abit_A.setKodLgot(rs.getInt(43));
            	abit_A.setZajavlen(rs.getString(44));
            	
            	
            	
            	String code = "";
	    	  	String code1 = "";
	    	  	String code2 = "";
	    	  	String codeA = "";
	    	  	String codeB = "";
	    	  	String codeC = "";
	    	  	boolean flag = true;
            	
            	
            	if (!kladrOblast.equals("-")){
            	 stmt = conn.prepareStatement("SELECT code, socr, name FROM KLADR WHERE (CODE Like ? or code like ?) and CODE not like ? ");
 	    	  	
            	 code = kladrOblast;
	    	  	 code1 = code.substring(0,2);
	    	  	 codeA = code1+"___000000__";
	    	  	 codeB = code1+"0000__00000";
	    	  	 codeC = code1+"000000000__";
	    	    stmt.setObject(1,codeA,Types.VARCHAR);
	    		stmt.setObject(2,codeB,Types.VARCHAR);
	    		stmt.setObject(3,codeC,Types.VARCHAR);
	    		  rs = stmt.executeQuery();
	    		   flag = true;
	    	        while (rs.next()) {
	    	        	  AbiturientBean abit_TMP = new AbiturientBean();
	    	              abit_TMP.setSpecial27(rs.getString(1));
	    	              //kladrRajon = rs.getString(1);
	    	              abit_TMP.setSpecial28(rs.getString(2)+" "+rs.getString(3));
	    	              abit_A_Rajon.add(abit_TMP);
	    	    
	    	
	    	        }
	    	        
     stmt = conn.prepareStatement("SELECT code, socr, name FROM KLADR WHERE CODE Like ? and code not like ?");
		    	  	 
		    		
		    	  	 code = kladrRajon;
		    	   code1 = code.substring(0,5);
		    	   code2 = code.substring(0,8);
		    	  //	String codeA = code2+"_____";
		    		 codeA = code1+"________";

		    	   codeB = code1+"000000__";
		    	  
		    	    stmt.setObject(1,codeA,Types.VARCHAR);
		    		stmt.setObject(2,codeB,Types.VARCHAR);
		    		
		    		  rs = stmt.executeQuery();
		    	        while (rs.next()) {
		    	        	
		    	        	 AbiturientBean abit_TMP = new AbiturientBean();
		    	              abit_TMP.setSpecial27(rs.getString(1));
		    	             
		    	              abit_TMP.setSpecial28(rs.getString(2)+" "+rs.getString(3));
		    	              abit_A_Punkt.add(abit_TMP);
		    	        	
		    	        }
		    	        
            	}
            	
            	if (!ObrOblast.equals("-")){
		    	        
		    	        
		    	        
		    	        /////обр Кладр
		    	        
		    	   	 stmt = conn.prepareStatement("SELECT code, socr, name FROM KLADR WHERE (CODE Like ? or code like ?) and CODE not like ? ");
		 	    	  	
		            	 code = ObrOblast;
			    	  	 code1 = code.substring(0,2);
			    	  	 codeA = code1+"___000000__";
			    	  	 codeB = code1+"0000__00000";
			    	  	 codeC = code1+"000000000__";
			    	    stmt.setObject(1,codeA,Types.VARCHAR);
			    		stmt.setObject(2,codeB,Types.VARCHAR);
			    		stmt.setObject(3,codeC,Types.VARCHAR);
			    		  rs = stmt.executeQuery();
			    		 flag = true;
			    	        while (rs.next()) {
			    	        	  AbiturientBean abit_TMP = new AbiturientBean();
			    	              abit_TMP.setSpecial27(rs.getString(1));
			    	              //kladrRajon = rs.getString(1);
			    	              abit_TMP.setSpecial28(rs.getString(2)+" "+rs.getString(3));
			    	              obr_A_Rajon.add(abit_TMP);
			    	    
			    	
			    	        }
			    	        
		     stmt = conn.prepareStatement("SELECT code, socr, name FROM KLADR WHERE CODE Like ? and code not like ?");
				    	  	 
				    		
				    	  	 code = ObrRajon;
				    	   code1 = code.substring(0,5);
				    	   code2 = code.substring(0,8);
				    	  //	String codeA = code2+"_____";
				    		 codeA = code1+"________";

				    	   codeB = code1+"000000__";
				    	  
				    	    stmt.setObject(1,codeA,Types.VARCHAR);
				    		stmt.setObject(2,codeB,Types.VARCHAR);
				    		
				    		  rs = stmt.executeQuery();
				    	        while (rs.next()) {
				    	        	
				    	        	 AbiturientBean abit_TMP = new AbiturientBean();
				    	              abit_TMP.setSpecial27(rs.getString(1));
				    	             
				    	              abit_TMP.setSpecial28(rs.getString(2)+" "+rs.getString(3));
				    	              obr_A_Punkt.add(abit_TMP);
				    	        	
				    	        }
				    	        
		    	        
            	}
		    	        
		    	        
                
                stmt = conn.prepareStatement("SELECT PolnoeNaimenovanieZavedenija,Sokr FROM Zavedenija WHERE KodZavedenija LIKE ?");
                stmt.setObject(1, abit_A.getKodZavedenija(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_A.setPolnoeNaimenovanieZavedenija(rs.getString(1));
                  abit_A.setSokr(rs.getString(2));
                }

                abit_A_S1.clear();
                stmt = conn.prepareStatement("SELECT a.KodPredmeta, a.OtsenkaEge, b.sokr,a.Examen,a.god FROM zajavlennyeshkolnyeotsenki a, NazvanijaPredmetov b  WHERE b.kodpredmeta not in ('13','12') AND b.KodPredmeta=a.KodPredmeta and KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  AbiturientBean abit_TMP = new AbiturientBean();
                  abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
                  abit_TMP.setEge(rs.getString(2));
                  abit_TMP.setPodtvMed(rs.getString(5));
                  abit_TMP.setPredmet(rs.getString(3));
                  abit_TMP.setExamen(StringUtil.ntv(rs.getString(4)).trim());
                  abit_A_S1.add(abit_TMP);
                }
                
                att.clear();
                stmt = conn.prepareStatement("SELECT a.KodPredmeta, a.OtsenkaAtt, b.sokr FROM Oa a, NazvanijaPredmetov b  WHERE b.KodPredmeta=a.kodPredmeta AND (b.KodPredmeta = 3 or b.KodPredmeta = 10 or b.KodPredmeta = 5 or b.KodPredmeta = 1 or b.KodPredmeta = 8 or b.KodPredmeta = 4 or b.KodPredmeta = 9 or b.KodPredmeta = 2) and KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  AbiturientBean abit_TMP = new AbiturientBean();
                  abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
                  abit_TMP.setEge(rs.getString(2));
                  abit_TMP.setPredmet(rs.getString(3));
                  att.add(abit_TMP);
                }
                
                stmt = conn.prepareStatement("SELECT a.Summ FROM Os a  WHERE KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  sb.setFamilija(rs.getString(1));
                }
                
                
                stmt = conn.prepareStatement("SELECT a.abitEmail, a.address, a.providingSpecialConditions, a.returnDocument, a.vstup, a.dist, a.ballatt, a.ballsgto, a.ballzgto, a.ballsoch, a.ballpoi, a.trudovajadejatelnost FROM abitDopInf a  WHERE KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setAbitEmail(rs.getString(1));
                  abit_A.setDopAddress(rs.getString(2));
                  abit_A.setProvidingSpecialCondition(rs.getString(3));
                  abit_A.setReturnDocument(rs.getString(4));
                  abit_A.setSpecial13(rs.getString(5));
                  abit_A.setSpecial10(rs.getString(6));
                  abit_A.setSpecial8(rs.getString(7));
                  abit_A.setStepen_Mag(rs.getString(10));
                  abit_A.setSpecial9(rs.getString(8));
                  abit_A.setSpecial222(rs.getString(9));
                  abit_A.setShifrKursov(rs.getString(11));
                  abit_A.setTrudovajaDejatelnost(rs.getString(12));
                }      
                
                
                stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti,s.edulevel FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=1 ORDER BY s.KodSpetsialnosti ASC");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
	    		  rs = stmt.executeQuery();
	    	        if (rs.next()) {
	    	        	AbiturientBean abit_TMP = new AbiturientBean();
	    	        	abit_TMP.setSpecial2(rs.getString(1));
	    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
	    	        	abit_SD_S2.add(abit_TMP);
                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? AND s.edulevel like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
	        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
		    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
		    	   stmt1.setObject(3,rs.getString(4),Types.VARCHAR);
		    	   rs1 = stmt1.executeQuery();
                while (rs1.next()) {
              	  abit_TMP = new AbiturientBean();
                    abit_TMP.setSpecial2(rs1.getString(1));
                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
                    abit_SD_S2.add(abit_TMP);
                  }
	    	        }else{
	    	        	AbiturientBean abit_TMP=new AbiturientBean();
	    	        	abit_TMP.setSpecial2("-");
	    	        	abit_TMP.setNazvanieSpetsialnosti("-");
	    	        	abit_SD_S2.add(abit_TMP);
	    	        }
	    	        
	    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti,s.edulevel FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=2 ORDER BY s.KodSpetsialnosti ASC");
	                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	AbiturientBean abit_TMP = new AbiturientBean();
		    	        	abit_TMP.setSpecial2(rs.getString(1));
		    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
		    	        	abit_SD_S22.add(abit_TMP);
	                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? and s.edulevel like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
			    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
			    	   stmt1.setObject(3,rs.getString(4),Types.VARCHAR);
			    	   rs1 = stmt1.executeQuery();
	                while (rs1.next()) {
	              	  abit_TMP = new AbiturientBean();
	                    abit_TMP.setSpecial2(rs1.getString(1));
	                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
	                    abit_SD_S22.add(abit_TMP);
	                  }
		    	        }else{
		    	        	AbiturientBean abit_TMP=new AbiturientBean();
		    	        	abit_TMP.setSpecial2("-");
		    	        	abit_TMP.setNazvanieSpetsialnosti("-");
		    	        	abit_SD_S22.add(abit_TMP);
		    	        }
		    	        
		    	        
		    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti,s.edulevel FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=3 ORDER BY s.KodSpetsialnosti ASC");
		                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
			    		  rs = stmt.executeQuery();
			    	        if (rs.next()) {
			    	        	AbiturientBean abit_TMP = new AbiturientBean();
			    	        	abit_TMP.setSpecial2(rs.getString(1));
			    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
			    	        	abit_SD_S222.add(abit_TMP);
		                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? and s.edulevel like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
			        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
				    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
				    	   stmt1.setObject(3,rs.getString(4),Types.VARCHAR);
				    	   rs1 = stmt1.executeQuery();
		                while (rs1.next()) {
		              	  abit_TMP = new AbiturientBean();
		                    abit_TMP.setSpecial2(rs1.getString(1));
		                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
		                    abit_SD_S222.add(abit_TMP);
		                  }
			    	        }else{
			    	        	AbiturientBean abit_TMP=new AbiturientBean();
			    	        	abit_TMP.setSpecial2("-");
			    	        	abit_TMP.setNazvanieSpetsialnosti("-");
			    	        	abit_SD_S222.add(abit_TMP);
			    	        }
	    	        
	    	        
	    	        
	    	        
	    	        
	    	        
                
			    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti,s.edulevel FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=4 ORDER BY s.KodSpetsialnosti ASC");
			                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
				    		  rs = stmt.executeQuery();
				    	        if (rs.next()) {
				    	        	AbiturientBean abit_TMP = new AbiturientBean();
				    	        	abit_TMP.setSpecial2(rs.getString(1));
				    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
				    	        	abit_SD_S3.add(abit_TMP);
			                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? and s.edulevel like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
				        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
					    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
					    	   stmt1.setObject(3,rs.getString(4),Types.VARCHAR);
					    	   rs1 = stmt1.executeQuery();
			                while (rs1.next()) {
			              	  abit_TMP = new AbiturientBean();
			                    abit_TMP.setSpecial2(rs1.getString(1));
			                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
			                    abit_SD_S3.add(abit_TMP);
			                  }
				    	        }else{
				    	        	AbiturientBean abit_TMP=new AbiturientBean();
				    	        	abit_TMP.setSpecial2("-");
				    	        	abit_TMP.setNazvanieSpetsialnosti("-");
				    	        	abit_SD_S3.add(abit_TMP);
				    	        }
                
				    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti,s.edulevel FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=5 ORDER BY s.KodSpetsialnosti ASC");
				                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
					    		  rs = stmt.executeQuery();
					    	        if (rs.next()) {
					    	        	AbiturientBean abit_TMP = new AbiturientBean();
					    	        	abit_TMP.setSpecial2(rs.getString(1));
					    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
					    	        	abit_SD_S33.add(abit_TMP);
				                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? and s.edulevel like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
					        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
						    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
						    	   stmt1.setObject(3,rs.getString(4),Types.VARCHAR);
						    	   rs1 = stmt1.executeQuery();
				                while (rs1.next()) {
				              	  abit_TMP = new AbiturientBean();
				                    abit_TMP.setSpecial2(rs1.getString(1));
				                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
				                    abit_SD_S33.add(abit_TMP);
				                  }
					    	        }else{
					    	        	AbiturientBean abit_TMP=new AbiturientBean();
					    	        	abit_TMP.setSpecial2("-");
					    	        	abit_TMP.setNazvanieSpetsialnosti("-");
					    	        	abit_SD_S33.add(abit_TMP);
					    	        }
					    	        
					    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti,s.edulevel FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=6 ORDER BY s.KodSpetsialnosti ASC");
					                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
						    		  rs = stmt.executeQuery();
						    	        if (rs.next()) {
						    	        	AbiturientBean abit_TMP = new AbiturientBean();
						    	        	abit_TMP.setSpecial2(rs.getString(1));
						    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
						    	        	abit_SD_S333.add(abit_TMP);
					                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? and s.edulevel like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
						        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
							    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
							    	   stmt1.setObject(3,rs.getString(4),Types.VARCHAR);
							    	   rs1 = stmt1.executeQuery();
					                while (rs1.next()) {
					              	  abit_TMP = new AbiturientBean();
					                    abit_TMP.setSpecial2(rs1.getString(1));
					                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
					                    abit_SD_S333.add(abit_TMP);
					                  }
						    	        }else{
						    	        	AbiturientBean abit_TMP=new AbiturientBean();
						    	        	abit_TMP.setSpecial2("-");
						    	        	abit_TMP.setNazvanieSpetsialnosti("-");
						    	        	abit_SD_S333.add(abit_TMP);
						    	        }
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
//System.out.println(">>AMD_2");
// Приоритет 1
                
                
                             
                stmt = conn.prepareStatement("SELECT kon.kodspetsialnosti,kon.Bud,kon.Dog,kon.Prof,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach,kon.stob,kon.pr1,kon.pr2,kon.pr3,kon.tname,kon.rlgot,kon.pr,kon.op,kon.npd,sp.kodfakulteta,sp.nazvaniespetsialnosti,kon.sogl FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.fito LIKE '1' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setSpecial2(rs.getString(1));

                  if(rs.getString(2) != null) abit_A.setBud_1("on");

                  if(rs.getString(3) != null) abit_A.setDog_1("on");

                  abit_A.setFito_1(rs.getString(4));

                  abit_A.setOlimp_1(rs.getString(5));

                  abit_A.setTarget_1(rs.getString(6));

                  abit_A.setSix_1(rs.getString(7));

                  abit_A.setThree_1(rs.getString(8));

                  if(rs.getString(10) != null) abit_A.setDog_ok_1("on");

                  abit_A.setKonkurs_1(StringUtil.ntv(rs.getString(11)));
                  if(rs.getString(12) != null) abit_A.setStob("on");
                  if(rs.getString(13) != null) abit_A.setPr1("on");
                  if(rs.getString(14) != null) abit_A.setPr2("on");
                  if(rs.getString(15) != null) abit_A.setPr3("on");
                  abit_A.setTname1(rs.getString(16));
                  abit_A.setRlgot1(rs.getString(17));
                  abit_A.setPrr1(rs.getString(18));
                  abit_A.setOp1(rs.getString(19));
                  abit_A.setNpd1(rs.getString(20));
                  abit_A.setKodFakulteta(rs.getInt(21));
                  if(rs.getString(23) != null) abit_A.setSog1("on");
                }
//System.out.println(">>AMD_3");
// Приоритет 2
                stmt = conn.prepareStatement("SELECT kon.kodspetsialnosti,kon.Bud,kon.Dog,kon.Prof,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach,kon.stob,kon.pr1,kon.pr2,kon.pr3,kon.tname,kon.rlgot,kon.pr,kon.op,kon.npd,sp.kodfakulteta,kon.sogl FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.fito LIKE '2' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setSpecial3(rs.getString(1));

                  if(rs.getString(2) != null) abit_A.setBud_2("on");

                  if(rs.getString(3) != null) abit_A.setDog_2("on");

                  abit_A.setFito_2(rs.getString(4));

                  abit_A.setOlimp_2(rs.getString(5));

                  abit_A.setTarget_2(rs.getString(6));

                  abit_A.setSix_2(rs.getString(7));

                  abit_A.setThree_2(rs.getString(8));

                  if(rs.getString(10) != null) abit_A.setDog_ok_2("on");

                  abit_A.setKonkurs_2(StringUtil.ntv(rs.getString(11)));
                  if(rs.getString(12) != null) abit_A.setStob_2("on");
                  if(rs.getString(13) != null) abit_A.setPr1_2("on");
                  if(rs.getString(14) != null) abit_A.setPr2_2("on");
                  if(rs.getString(15) != null) abit_A.setPr3_2("on");
                  abit_A.setTname2(rs.getString(16));
                  abit_A.setRlgot2(rs.getString(17));
                  abit_A.setPrr2(rs.getString(18));
                  abit_A.setOp2(rs.getString(19));
                  abit_A.setNpd2(rs.getString(20));
                  abit_A.setS_okso_2(rs.getString(21));
                  if(rs.getString(22) != null) abit_A.setSog2("on");
                }
//System.out.println(">>AMD_4");
// Приоритет 3
                stmt = conn.prepareStatement("SELECT kon.kodspetsialnosti,kon.Bud,kon.Dog,kon.Prof,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach,kon.stob,kon.pr1,kon.pr2,kon.pr3,kon.tname,kon.rlgot,kon.pr,kon.op,kon.npd,sp.kodfakulteta,kon.sogl FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.fito LIKE '3' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setSpecial4(rs.getString(1));

                  if(rs.getString(2) != null) abit_A.setBud_3("on");

                  if(rs.getString(3) != null) abit_A.setDog_3("on");

                  abit_A.setFito_3(rs.getString(4));

                  abit_A.setOlimp_3(rs.getString(5));

                  abit_A.setTarget_3(rs.getString(6));

                  abit_A.setSix_3(rs.getString(7));

                  abit_A.setThree_3(rs.getString(8));

                  if(rs.getString(10) != null) abit_A.setDog_ok_3("on");

                  abit_A.setKonkurs_3(StringUtil.ntv(rs.getString(11)));
                  if(rs.getString(12) != null) abit_A.setStob_3("on");
                  if(rs.getString(13) != null) abit_A.setPr1_3("on");
                  if(rs.getString(14) != null) abit_A.setPr2_3("on");
                  if(rs.getString(15) != null) abit_A.setPr3_3("on");
                  abit_A.setTname3(rs.getString(16));
                  abit_A.setRlgot3(rs.getString(17));
                  abit_A.setPrr3(rs.getString(18));
                  abit_A.setOp3(rs.getString(19));
                  abit_A.setNpd3(rs.getString(20));
                  abit_A.setS_okso_3(rs.getString(21));
                  if(rs.getString(22) != null) abit_A.setSog3("on");
                }
//System.out.println(">>AMD_5");
// Приоритет 4
                stmt = conn.prepareStatement("SELECT kon.kodspetsialnosti,kon.Bud,kon.Dog,kon.Prof,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach,kon.stob,kon.pr1,kon.pr2,kon.pr3,kon.tname,kon.rlgot,kon.pr,kon.op,kon.npd,sp.kodfakulteta,kon.sogl FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.fito LIKE '4' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setSpecial5(rs.getString(1));

                  if(rs.getString(2) != null) abit_A.setBud_4("on");

                  if(rs.getString(3) != null) abit_A.setDog_4("on");

                  abit_A.setFito_4(rs.getString(4));

                  abit_A.setOlimp_4(rs.getString(5));

                  abit_A.setTarget_4(rs.getString(6));

                  abit_A.setSix_4(rs.getString(7));

                  abit_A.setThree_4(rs.getString(8));

                  if(rs.getString(10) != null) abit_A.setDog_ok_4("on");

                  abit_A.setKonkurs_4(StringUtil.ntv(rs.getString(11)));
                  if(rs.getString(12) != null) abit_A.setStob_4("on");
                  if(rs.getString(13) != null) abit_A.setPr1_4("on");
                  if(rs.getString(14) != null) abit_A.setPr2_4("on");
                  if(rs.getString(15) != null) abit_A.setPr3_4("on");
                  abit_A.setTname4(rs.getString(16));
                  abit_A.setRlgot4(rs.getString(17));
                  abit_A.setPrr4(rs.getString(18));
                  abit_A.setOp4(rs.getString(19));
                  abit_A.setNpd4(rs.getString(20));
                  abit_A.setS_okso_4(rs.getString(21));
                  if(rs.getString(22) != null) abit_A.setSog4("on");
                }
//System.out.println(">>AMD_6");
// Приоритет 5
                stmt = conn.prepareStatement("SELECT kon.kodspetsialnosti,kon.Bud,kon.Dog,kon.Prof,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach,kon.stob,kon.pr1,kon.pr2,kon.pr3,kon.tname,kon.rlgot,kon.pr,kon.op,kon.npd,sp.kodfakulteta,kon.sogl FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.fito LIKE '5' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setSpecial6(rs.getString(1));

                  if(rs.getString(2) != null) abit_A.setBud_5("on");

                  if(rs.getString(3) != null) abit_A.setDog_5("on");

                  abit_A.setFito_5(rs.getString(4));

                  abit_A.setOlimp_5(rs.getString(5));

                  abit_A.setTarget_5(rs.getString(6));

                  abit_A.setSix_5(rs.getString(7));

                  abit_A.setThree_5(rs.getString(8));

                  if(rs.getString(10) != null) abit_A.setDog_ok_5("on");

                  abit_A.setKonkurs_5(StringUtil.ntv(rs.getString(11)));
                  if(rs.getString(12) != null) abit_A.setStob_5("on");
                  if(rs.getString(13) != null) abit_A.setPr1_5("on");
                  if(rs.getString(14) != null) abit_A.setPr2_5("on");
                  if(rs.getString(15) != null) abit_A.setPr3_5("on");
                  abit_A.setTname5(rs.getString(16));
                  abit_A.setRlgot5(rs.getString(17));
                  abit_A.setPrr5(rs.getString(18));
                  abit_A.setOp5(rs.getString(19));
                  abit_A.setNpd5(rs.getString(20));
                  abit_A.setS_okso_5(rs.getString(21));
                  if(rs.getString(22) != null) abit_A.setSog5("on");
                }

// Приоритет 6
                stmt = conn.prepareStatement("SELECT kon.kodspetsialnosti,kon.Bud,kon.Dog,kon.Prof,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach,kon.stob,kon.pr1,kon.pr2,kon.pr3,kon.tname,kon.rlgot,kon.pr,kon.op,kon.npd,sp.kodfakulteta,kon.sogl FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.fito LIKE '6' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setSpecial7(rs.getString(1));

                  if(rs.getString(2) != null) abit_A.setBud_6("on");

                  if(rs.getString(3) != null) abit_A.setDog_6("on");

                  abit_A.setFito_6(rs.getString(4));

                  abit_A.setOlimp_6(rs.getString(5));

                  abit_A.setTarget_6(rs.getString(6));

                  abit_A.setSix_6(rs.getString(7));

                  abit_A.setThree_6(rs.getString(8));

                  if(rs.getString(10) != null) abit_A.setDog_ok_6("on");

                  abit_A.setKonkurs_6(StringUtil.ntv(rs.getString(11)));
                  if(rs.getString(12) != null) abit_A.setStob_6("on");
                  if(rs.getString(13) != null) abit_A.setPr1_6("on");
                  if(rs.getString(14) != null) abit_A.setPr2_6("on");
                  if(rs.getString(15) != null) abit_A.setPr3_6("on");
                  abit_A.setTname6(rs.getString(16));
                  abit_A.setRlgot6(rs.getString(17));
                  abit_A.setPrr6(rs.getString(18));
                  abit_A.setOp6(rs.getString(19));
                  abit_A.setNpd6(rs.getString(20));
                  abit_A.setS_okso_6(rs.getString(21));
                  if(rs.getString(22) != null) abit_A.setSog6("on");
                }

//System.out.println(">>AMD_8");
                form.setAction(us.getClientIntName("md_dl","view_"+abit_A.getKodAbiturienta()));
            } 


/************************************************************************************************/
/************************************************************************************************/
/******************  МОДИФИКАЦИЯ ДАННЫХ АБИТУРИЕНТА СОГЛАСНО ЛИЧНОЙ КАРТОЧКЕ  *******************/
/************************************************************************************************/
/************************************************************************************************/

  if ( form.getAction().equals("change") && (user.getGroup().getTypeId()==1 || user.getGroup().getTypeId()==3 || user.getGroup().getTypeId()==4)) {
//System.out.println(">>AMD_9");
// Проверка на уникальность по паспортным данным

    /* stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE SeriaDokumenta LIKE ? AND NomerDokumenta LIKE ? AND KodAbiturienta NOT LIKE ?");
     stmt.setObject(1,abit_A.getSeriaDokumenta(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getNomerDokumenta(),Types.VARCHAR);
     stmt.setObject(3,abit_A.getKodAbiturienta(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Ошибка!");
       mess.setMessage("Заявление абитуриента с указанными паспортными данными уже существует в базе данных!");
       re_enter = true;
     }*/

// Проверка на уникальность по номеру сертификата ЕГЭ

    /* stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE NomerSertifikata LIKE ? AND NomerSertifikata NOT LIKE '-' AND KodAbiturienta NOT LIKE ?");
     stmt.setObject(1,abit_A.getNomerSertifikata(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getKodAbiturienta(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Ошибка!");
       mess.setMessage("Заявление абитуриента с указанным номером сертификата ЕГЭ уже существует в базе данных!");
       re_enter = true;
     }
*/
// Проверка на уникальность по серии и номеру аттестата

    /* stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE SeriaAtt LIKE ? AND NomerAtt LIKE ?  AND VidDokSredObraz LIKE ? AND KodAbiturienta NOT LIKE ?");
     stmt.setObject(1,abit_A.getSeriaAtt(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getNomerAtt(),Types.VARCHAR);
     stmt.setObject(3,abit_A.getVidDokSredObraz(),Types.VARCHAR);
     stmt.setObject(4,abit_A.getKodAbiturienta(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Ошибка!");
       mess.setMessage("Заявление абитуриента с указанными серией и номером номером аттестата уже существует в БД!");
       re_enter = true;
     }*/

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

    /* if(abit_A.getBud_1() != null && abit_A.getBud_1().equals("on"))
       stmt = conn.prepareStatement("SELECT KodOsnovyOb FROM Osnova_Obuch WHERE Sokr LIKE '%бюдж%'");
     else
       stmt = conn.prepareStatement("SELECT KodOsnovyOb FROM Osnova_Obuch WHERE Sokr LIKE '%дог%'");
     rs = stmt.executeQuery();
     if(rs.next()) kOsnovy_Ob = rs.getString(1);

// Приоритетная специальность

     if(abit_A.getSpecial2() != "-" && abit_A.getSpecial2() != null ) {
         Tip_Spec = "о";
           s_okso_1 = abit_A.getSpecial2();
       }else if(abit_A.getSpecial5() != "-" || abit_A.getSpecial5() != null){
Tip_Spec = "з";
s_okso_4 =abit_A.getSpecial5();
       }
       stmt = conn.prepareStatement("SELECT s.KodSpetsialnosti,s.KodFakulteta,s.Abbreviatura,f.ShifrFakulteta FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodSpetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
       if(s_okso_1!=null){
       stmt.setObject(1,s_okso_1,Types.VARCHAR);
       }else{
    	   stmt.setObject(1,s_okso_4,Types.VARCHAR);
       }
       stmt.setObject(2,Tip_Spec,Types.VARCHAR);
       rs = stmt.executeQuery();
       if(rs.next()) {
         kSpec = rs.getString(1);
         kFak = rs.getString(2);
         Abbr_Spec = rs.getString(3);
         shifr_Fak = rs.getString(4);

// Получение кода формы обучения для наиболее приоритетной специальности

         String Sokr = new String();

         if(Tip_Spec.equals("о") || Tip_Spec.equals("м") || Tip_Spec.equals("ф") || Tip_Spec.equals("ю") || Tip_Spec.equals("п")) Sokr = "очная";

         else if((Tip_Spec.equals("у") && shifr_Fak.equals("з")) || (abit_A.getThree_1() != null && shifr_Fak.equals("з")) || (Tip_Spec.equals("у") && shifr_Fak.equals("г"))) Sokr = "заочно-уск";

         else if(Tip_Spec.equals("з")) Sokr = "заочная";

         else if(Tip_Spec.equals("в")) Sokr = "очно-заочная";

         else if(Tip_Spec.equals("у")) Sokr = "очно-уск";

         else if(Tip_Spec.equals("д")) Sokr = "дист";

         else Tip_Spec = "-";

         stmt = conn.prepareStatement("SELECT KodFormyOb FROM Forma_Obuch WHERE Sokr LIKE ?");
         stmt.setObject(1,Sokr,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) kFormy_Ob = rs.getString(1);

     } */
     

// Выборка номера личного дела из БД

     stmt = conn.prepareStatement("SELECT DISTINCT Ordr FROM Abiturient WHERE KodAbiturienta LIKE ? ORDER BY Ordr DESC");
     stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
     rs = stmt.executeQuery();
     if(rs.next()) {

// Хотя бы один абитуриент существует в БД

       if(rs.getInt(1) < 9) nomer_ab = "000"+rs.getInt(1);
       else if((rs.getInt(1) >= 9) && (rs.getInt(1) < 99)) nomer_ab = "00"+rs.getInt(1);
       else if((rs.getInt(1) >= 99) && (rs.getInt(1) < 999)) nomer_ab = "0"+rs.getInt(1);
       else if((rs.getInt(1) >= 999) && (rs.getInt(1) < 9999)) nomer_ab = ""+rs.getInt(1);

     } else {

// Порядковый номер абитуриента не найден в БД

        mess.setStatus("Ошибка!");
        mess.setMessage("В базе данных не найден порядковый номер абитуриента!");

        form.setAction(us.getClientIntName("re_new","error"));

        re_enter = true;
     }


/************************************************************************************************/
/********************          НЕПОСРЕДСТВЕННО МОДИФИКАЦИЯ ДАННЫХ           *********************/
/************************************************************************************************/
//System.out.println(">>AMD_10");
    if(!re_enter) {

// Начало транзакции

      conn.setAutoCommit(false);

      abit_A.setKodAbiturienta(new Integer(request.getParameter("kodAbiturienta")));

// Область

      /* if(abit_A.getNazvanieOblasti()!=null) {
         stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanieOblasti(),Types.VARCHAR);
         stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) { 
           kOblasti = rs.getString(1);
           stmt = conn.prepareStatement("UPDATE Oblasti SET NazvanieOblasti=?,KodVuza=? WHERE KodOblasti LIKE ?");
           stmt.setObject(1,abit_A.getNazvanieOblasti(),Types.VARCHAR);
           stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
           stmt.setObject(3,kOblasti,Types.INTEGER);
           stmt.executeUpdate();

         } else {

           stmt = conn.prepareStatement("SELECT MAX(KodOblasti) FROM Oblasti WHERE KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kOblasti = ""+(rs.getInt(1)+1);
           else kOblasti="1";

           stmt = conn.prepareStatement("INSERT Oblasti(KodOblasti,NazvanieOblasti,KodVuza) VALUES(?,?,?)");
           stmt.setObject(1,kOblasti,Types.INTEGER);
           stmt.setObject(2,abit_A.getNazvanieOblasti(),Types.VARCHAR);
           stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
           stmt.executeUpdate();
         }

       } else {

           stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti IS NULL AND KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kOblasti = rs.getString(1);
           else {

             stmt = conn.prepareStatement("SELECT MAX(KodOblasti) FROM Oblasti WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kOblasti = ""+(rs.getInt(1)+1);
             else kOblasti="1";

             stmt = conn.prepareStatement("INSERT Oblasti(KodOblasti,NazvanieOblasti,KodVuza) VALUES(?,?,?)");
             stmt.setObject(1,kOblasti,Types.INTEGER);
             stmt.setObject(2,abit_A.getNazvanieOblasti(),Types.VARCHAR);
             stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
             stmt.executeUpdate();
           }
       }*/

// Район

      /* if(abit_A.getNazvanieRajona()!=null) {
         stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE NazvanieRajona LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanieRajona(),Types.VARCHAR);
         stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           kRajona = rs.getString(1);
           stmt = conn.prepareStatement("UPDATE Rajony SET NazvanieRajona=?,KodVuza=? WHERE KodRajona LIKE ?");
           stmt.setObject(1, ""+abit_A.getNazvanieRajona(),Types.VARCHAR);
           stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
           stmt.setObject(3,kRajona,Types.INTEGER);
           stmt.executeUpdate();                    

         } else {

           stmt = conn.prepareStatement("SELECT MAX(KodRajona) FROM Rajony WHERE KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kRajona = ""+(rs.getInt(1)+1);
           else kRajona="1";

           stmt = conn.prepareStatement("INSERT Rajony(NazvanieRajona,KodVuza,KodRajona) VALUES(?,?,?)");
           stmt.setObject(1, ""+abit_A.getNazvanieRajona(),Types.VARCHAR);
           stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
           stmt.setObject(3,kRajona,Types.INTEGER);
           stmt.executeUpdate();                    
         }

       } else {

           stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE (NazvanieRajona IS NULL OR NazvanieRajona LIKE '') AND KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kRajona = rs.getString(1);
           else {

             stmt = conn.prepareStatement("SELECT MAX(KodRajona) FROM Rajony WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kRajona = ""+(rs.getInt(1)+1);
             else kRajona="1";

             stmt = conn.prepareStatement("INSERT Rajony(NazvanieRajona,KodVuza,KodRajona) VALUES(?,?,?)");
             stmt.setObject(1, ""+abit_A.getNazvanieRajona(),Types.VARCHAR);
             stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
             stmt.setObject(3,kRajona,Types.INTEGER);
             stmt.executeUpdate();
           }
       }*/

// Пункт

   /*    if(abit_A.getNazvanie()!=null) {
         stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanie(),Types.VARCHAR);
         stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           kPunkta = rs.getString(1);
           stmt = conn.prepareStatement("UPDATE Punkty SET Nazvanie=?,KodVuza=? WHERE KodPunkta LIKE ?");
           stmt.setObject(1, abit_A.getNazvanie(),Types.VARCHAR);
           stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
           stmt.setObject(3,kPunkta,Types.INTEGER);
           stmt.executeUpdate();

         } else {

             stmt = conn.prepareStatement("SELECT MAX(KodPunkta) FROM Punkty WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kPunkta = ""+(rs.getInt(1)+1);
             else kPunkta = "1";

             stmt = conn.prepareStatement("INSERT Punkty(Nazvanie,KodVuza,KodPunkta) VALUES(?,?,?)");
             stmt.setObject(1, abit_A.getNazvanie(),Types.VARCHAR);
             stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
             stmt.setObject(3,kPunkta,Types.INTEGER);
             stmt.executeUpdate();
         }

       } else {

           stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie IS NULL AND KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kPunkta = rs.getString(1);
           else {

             stmt = conn.prepareStatement("SELECT MAX(KodPunkta) FROM Punkty WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kPunkta = ""+(rs.getInt(1)+1);
             else kPunkta = "1";

             stmt = conn.prepareStatement("INSERT Punkty(Nazvanie,KodVuza,KodPunkta) VALUES(?,?,?)");
             stmt.setObject(1, abit_A.getNazvanie(),Types.VARCHAR);
             stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
             stmt.setObject(3,kPunkta,Types.INTEGER);
             stmt.executeUpdate();
           }
       }
*/
// Заведение

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
           stmt.setObject(4,new Integer(kZavedenija+""),Types.INTEGER);
           stmt.executeUpdate();
         }

       } else kZavedenija = StringUtil.toInt(""+abit_A.getKodZavedenija(),1);

// Группа

       String kGrp = "1";
       stmt = conn.prepareStatement("SELECT Gruppy.KodGruppy FROM Gruppy,Fakultety WHERE Gruppy.KodFakulteta=Fakultety.KodFakulteta AND Gruppa LIKE ? AND Fakultety.KodVuza LIKE ?");
       stmt.setObject(1, abit_A.getGruppa(),Types.VARCHAR);
       stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) kGrp = rs.getString(1);

/** Абитуриент **/

       /*stmt = conn.prepareStatement("UPDATE Abiturient SET KodVuza=?,DokumentyHranjatsja=?,KodSpetsialnosti=?,NomerLichnogoDela=?,Familija=?,Imja=?,Otchestvo=?,TipDokumenta=?,NomerDokumenta=?,SeriaDokumenta=?,DataVydDokumenta=?,KemVydDokument=?,TipDokSredObraz=?,VidDokSredObraz=?,DataRojdenija=?,Pol=?,NomerSertifikata=?,KopijaSertifikata=?,Grajdanstvo=?,GodOkonchanijaSrObrazovanija=?,GdePoluchilSrObrazovanie=?,TipOkonchennogoZavedenija=?,NomerShkoly=?,KodZavedenija=?,KodMedali=?,TrudovajaDejatelnost=?,NapravlenieOtPredprijatija=?,KodLgot=?,InostrannyjJazyk=?,NujdaetsjaVObschejitii=?,KodTselevogoPriema=?,KodOblasti=?,KodRajona=?,KodPunkta=?,KodGruppy=?,KodFormyOb=?,KodOsnovyOb=?,SeriaAtt=?,NomerAtt=?,Gorod_Prop=?,Ulica_Prop=?,Dom_Prop=?,Kvart_Prop=?,Stepen_Mag=?,Need_Spo=?,Tel=?,NomerPlatnogoDogovora=?,MestoRojdenija=?,DiplomOtlichija=?,UdostoverenieLgoty=?,PostgraduateStudies=?,Traineeship=?,Internship=? WHERE KodAbiturienta=?");
       stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
       stmt.setObject(2, abit_A.getDokumentyHranjatsja(),Types.VARCHAR);
       stmt.setObject(3, kSpec,Types.VARCHAR);
       stmt.setObject(4, Abbr_Spec+nomer_ab+"-1", Types.VARCHAR); // NLD
       stmt.setObject(5, (abit_A.getFamilija()).substring(0,1).toUpperCase()+(abit_A.getFamilija()).substring(1),Types.VARCHAR);
       stmt.setObject(6, (abit_A.getImja()).substring(0,1).toUpperCase()+(abit_A.getImja()).substring(1),Types.VARCHAR);
       stmt.setObject(7, (abit_A.getOtchestvo()).substring(0,1).toUpperCase()+(abit_A.getOtchestvo()).substring(1),Types.VARCHAR);
       stmt.setObject(8, abit_A.getTipDokumenta(),Types.VARCHAR);
       stmt.setObject(9, abit_A.getNomerDokumenta(),Types.VARCHAR);
       stmt.setObject(10, abit_A.getSeriaDokumenta(),Types.VARCHAR);
       stmt.setObject(11, StringUtil.DataConverter(abit_A.getDataVydDokumenta()),Types.VARCHAR);
       stmt.setObject(12, abit_A.getKemVydDokument(),Types.VARCHAR);
       stmt.setObject(13, abit_A.getTipDokSredObraz(),Types.VARCHAR);
       stmt.setObject(14, abit_A.getVidDokSredObraz(),Types.VARCHAR);
       stmt.setObject(15, abit_A.getDataRojdenija(),Types.VARCHAR);
       stmt.setObject(16, abit_A.getPol(),Types.VARCHAR);
       stmt.setObject(17, abit_A.getNomerSertifikata(),Types.VARCHAR);
       stmt.setObject(18, abit_A.getKopijaSertifikata(),Types.VARCHAR);
       stmt.setObject(19, abit_A.getGrajdanstvo(),Types.VARCHAR);
       stmt.setObject(20, abit_A.getGodOkonchanijaSrObrazovanija(),Types.VARCHAR);
       stmt.setObject(21, abit_A.getGdePoluchilSrObrazovanie(),Types.VARCHAR);
       stmt.setObject(22, abit_A.getTipOkonchennogoZavedenija(),Types.VARCHAR);
       stmt.setObject(23, abit_A.getNomerShkoly(),Types.VARCHAR);
       stmt.setObject(24, new Integer(""+kZavedenija),Types.INTEGER);
       stmt.setObject(25, abit_A.getKodMedali(),Types.INTEGER);
       stmt.setObject(26, abit_A.getTrudovajaDejatelnost(),Types.VARCHAR);
       stmt.setObject(27, abit_A.getNapravlenieOtPredprijatija(),Types.VARCHAR);
       stmt.setObject(28, abit_A.getKodLgot(),Types.INTEGER);
       //stmt.setObject(29, abit_A.getKodKursov(),Types.INTEGER);
       stmt.setObject(29, abit_A.getInostrannyjJazyk(),Types.VARCHAR);
       stmt.setObject(30, abit_A.getNujdaetsjaVObschejitii(),Types.VARCHAR);
       stmt.setObject(31, abit_A.getKodTselevogoPriema(),Types.INTEGER);
       stmt.setObject(32, new Integer(""+kOblasti),Types.INTEGER);
       stmt.setObject(33, new Integer(""+kRajona),Types.INTEGER);
       stmt.setObject(34, new Integer(""+kPunkta),Types.INTEGER);
       stmt.setObject(35, kGrp, Types.VARCHAR);
       stmt.setObject(36, kFormy_Ob,Types.INTEGER);
       stmt.setObject(37, kOsnovy_Ob,Types.INTEGER);
       stmt.setObject(38, abit_A.getSeriaAtt(),Types.VARCHAR);
       stmt.setObject(39, abit_A.getNomerAtt(),Types.VARCHAR);
       stmt.setObject(40, abit_A.getGorod_Prop(),Types.VARCHAR);
       stmt.setObject(41, abit_A.getUlica_Prop(),Types.VARCHAR);
       stmt.setObject(42, abit_A.getDom_Prop(),Types.VARCHAR);
       stmt.setObject(43, abit_A.getKvart_Prop(),Types.VARCHAR);
       stmt.setObject(44, abit_A.getStepen_Mag(),Types.VARCHAR);
       stmt.setObject(45, abit_A.getNeed_Spo(),Types.VARCHAR);
       stmt.setObject(46, abit_A.getTel(),Types.VARCHAR);
       stmt.setObject(47, abit_A.getNomerPlatnogoDogovora(),Types.VARCHAR);
       stmt.setObject(48, abit_A.getMestoRojdenija(),Types.VARCHAR);
       stmt.setObject(49, abit_A.getDiplomOtlichija(),Types.VARCHAR);
       stmt.setObject(50, abit_A.getUdostoverenieLgoty(),Types.VARCHAR);
       stmt.setObject(51, abit_A.getPostgraduateStudies(),Types.VARCHAR);
       stmt.setObject(52, abit_A.getTraineeship(),Types.VARCHAR);
       stmt.setObject(53, abit_A.getInternship(),Types.VARCHAR);
       stmt.setObject(54, abit_A.getKodAbiturienta(), Types.INTEGER);
       stmt.executeUpdate();*/
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
       
       
       
       
       stmt = conn.prepareStatement("UPDATE Abiturient SET KodVuza=?,KodSpetsialnosti=?,NomerLichnogoDela=?,Familija=?,Imja=?,Otchestvo=?,TipDokumenta=?,NomerDokumenta=?,SeriaDokumenta=?,DataVydDokumenta=?,KemVydDokument=?,MestoRojdenija=?,TipDokSredObraz=?,VidDokSredObraz=?,DataRojdenija=?,Pol=?,Grajdanstvo=?,GodOkonchanijaSrObrazovanija=?,TipOkonchennogoZavedenija=?,NomerShkoly=?,KodZavedenija=?,InostrannyjJazyk=?,NujdaetsjaVObschejitii=?,KodOblasti=?,KodRajona=?,KodPunkta=?,SeriaAtt=?,NomerAtt=?,Gorod_Prop=?,Ulica_Prop=?,Dom_Prop=?,Kvart_Prop=?,Tel=?,NomerPlatnogoDogovora=?,KodStrany=?,KodStranyP=?,KodOblastiP=?,KodRajonaP=?,NomerPotoka=?,kodGruppy=?,DokumentyHranjatsja=?,kodlgot=?,kodpodrazdelenija=? WHERE KodAbiturienta LIKE ?");
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
//       stmt.setObject(18, abit_A.getNomerSertifikata(), Types.VARCHAR);
//       stmt.setObject(19, abit_A.getKopijaSertifikata(), Types.VARCHAR);
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
//       stmt.setObject(31, abit_A.getKodTselevogoPriema(), Types.INTEGER);
       stmt.setObject(24, abit_A.getNazvanieOblasti(), Types.VARCHAR);
       stmt.setObject(25, abit_A.getNazvanieRajona(), Types.VARCHAR);
       stmt.setObject(26, abit_A.getNazvanie(), Types.VARCHAR);
       stmt.setObject(27, abit_A.getSeriaAtt(), Types.VARCHAR);
       stmt.setObject(28, abit_A.getNomerAtt(), Types.VARCHAR);
      // stmt.setObject(29, ordr_ab, Types.VARCHAR);
       stmt.setObject(29, abit_A.getGorod_Prop(), Types.VARCHAR);
       stmt.setObject(30, abit_A.getUlica_Prop(), Types.VARCHAR);
       stmt.setObject(31, abit_A.getDom_Prop(), Types.VARCHAR);
       stmt.setObject(32, abit_A.getKvart_Prop(), Types.VARCHAR);
     /*  stmt.setObject(42, abit_A.getStepen_Mag(), Types.VARCHAR);*/
       /*stmt.setObject(43, abit_A.getNeed_Spo(), Types.VARCHAR);*/
//       stmt.setNull(44, Types.VARCHAR); //Exists_st_Mag
       stmt.setObject(33, abit_A.getTel(), Types.VARCHAR);
       stmt.setObject(34, abit_A.getNpd1(), Types.VARCHAR);
      /* stmt.setObject(40, kFormy_Ob, Types.INTEGER);  //KodFormyOb
       stmt.setObject(41, kOsnovy_Ob, Types.INTEGER); //KodOsnovyOb
*/     
//       stmt.setObject(39, abit_A.getUdostoverenieLgoty(), Types.VARCHAR);
//       stmt.setObject(52, abit_A.getDiplomOtlichija(), Types.VARCHAR);
       
/*       stmt.setObject(54, abit_A.getPostgraduateStudies(), Types.VARCHAR);
       stmt.setObject(55, abit_A.getTraineeship(), Types.VARCHAR);
       stmt.setObject(56, abit_A.getInternship(), Types.VARCHAR);*/
       
       
       stmt.setObject(35, abit_A.getTraineeship(), Types.VARCHAR);
       stmt.setObject(36, abit_A.getNazv_DipBak(), Types.VARCHAR);
       stmt.setObject(37, abit_A.getNazv_DipSpec(), Types.VARCHAR);
       stmt.setObject(38, abit_A.getNeed_Spo(), Types.VARCHAR);
       stmt.setObject(39, abit_A.getNomerPotoka(), Types.INTEGER);
       stmt.setObject(40, kGrp, Types.VARCHAR);
       stmt.setObject(41, abit_A.getDokumentyHranjatsja(), Types.VARCHAR);
       stmt.setObject(42, abit_A.getKodLgot(), Types.VARCHAR);
       stmt.setObject(43, abit_A.getZajavlen(), Types.VARCHAR);
        //DataModify = DataInput
       stmt.setObject(44, abit_A.getKodAbiturienta(), Types.INTEGER);
       stmt.executeUpdate();
       
       
       
       
       
       stmt = conn.prepareStatement("UPDATE FisImport SET Status = 'и' Where AppNumber = ?");
       stmt.setObject(1, Abbr_Spec+nomer_ab+"-1", Types.VARCHAR); // NLD
       stmt.executeUpdate();
       
       stmt = conn.prepareStatement("UPDATE AbitDopInf SET abitEmail=?, Address=?, ProvidingSpecialConditions=?, ReturnDocument=?, DopSrok=?, Vstup=?, Dist=?, BallAtt=?, BallSoch=?, BallSGTO=?, BallZGTO=?, BallPOI=?, TrudovajaDejatelnost=? WHERE KodAbiturienta LIKE ?");
       stmt.setObject(1,abit_A.getAbitEmail() ,Types.VARCHAR);
       stmt.setObject(2,abit_A.getDopAddress(),Types.VARCHAR);
       stmt.setObject(3,abit_A.getProvidingSpecialCondition(),Types.VARCHAR);
       stmt.setObject(4,abit_A.getReturnDocument(),Types.VARCHAR);
       stmt.setObject(5,abit_A.getInternship(),Types.VARCHAR);
       stmt.setObject(6,abit_A.getSpecial13(),Types.VARCHAR);
       stmt.setObject(7,abit_A.getSpecial10(),Types.VARCHAR);
       if(abit_A.getSpecial8() != null){
           stmt.setObject(8,abit_A.getSpecial8(),Types.VARCHAR);
           }else{
        	   stmt.setObject(8,0,Types.VARCHAR);
           }
           if(abit_A.getStepen_Mag() != null){
           stmt.setObject(9,abit_A.getStepen_Mag(),Types.VARCHAR);
           }else{
        	   stmt.setObject(9,0,Types.VARCHAR);
           }
           if(abit_A.getSpecial9() != null){
           stmt.setObject(10,abit_A.getSpecial9(),Types.VARCHAR);
           }else{
        	   stmt.setObject(10,0,Types.VARCHAR);
           }
           if(abit_A.getSpecial222() != null){
           stmt.setObject(11,abit_A.getSpecial222(),Types.VARCHAR);
           }else{
        	   stmt.setObject(11,0,Types.VARCHAR);
           }
           if(abit_A.getShifrKursov() != null){
           stmt.setObject(12,abit_A.getShifrKursov(),Types.VARCHAR);
           }else{
        	   stmt.setObject(12,0,Types.VARCHAR);
           }
           if(abit_A.getTrudovajaDejatelnost() != null){
           stmt.setObject(13,abit_A.getTrudovajaDejatelnost(),Types.VARCHAR);
           }else{
        	   stmt.setObject(13,0,Types.VARCHAR);
           }
       stmt.setObject(14, abit_A.getKodAbiturienta(), Types.INTEGER);
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
          
     /*  
       if(abit_A.getPodtverjdenieMedSpravki().equals("d")){ 
    	   stmt = conn.prepareStatement("SELECT nomerSpravki FROM medSpravka  WHERE KodAbiturienta=?");
           stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
           rs = stmt.executeQuery();
           while(rs.next()) {
             spravka = rs.getString(1);
           }
           
           if(spravka!=null){
    	   
		       stmt = conn.prepareStatement("UPDATE medSpravka SET nomerSpravki = ? Where kodAbiturienta = ?");
		       stmt.setObject(1, abit_A.getMedSpravka(), Types.VARCHAR);
		       stmt.setObject(2, abit_A.getKodAbiturienta(), Types.INTEGER);  
		       stmt.executeUpdate();
           }else{
        	   stmt = conn.prepareStatement("INSERT INTO medSpravka(kodAbiturienta, nomerSpravki) VALUES(?,?)");
    	       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
    	       stmt.setObject(2, form.getMedSpravka(),Types.VARCHAR);
    	       stmt.executeUpdate();
           }
       } else{
    	   stmt = conn.prepareStatement("DELETE FROM MedSpravka WHERE KodAbiturienta=?");
    	     stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
    	     stmt.executeUpdate();
       }*/

/***********************************************************************/
/******  Модификация оценок ЕГЭ и признака места сдачи экзамена  *******/
/***********************************************************************/

       boolean may_mod_ots = false;
//System.out.println(">>AMD_10-0-KodAbita="+abit_A.getKodAbiturienta());
       stmt = conn.prepareStatement("SELECT DISTINCT KodAbiturienta FROM ZajavlennyeShkolnyeOtsenki WHERE KodAbiturienta LIKE ? AND (Locked IS NULL OR Locked NOT LIKE 'д')");
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.VARCHAR);
       rs = stmt.executeQuery();
       if(rs.next()) {

         may_mod_ots = true;
//System.out.println(">>AMD_10-PREPARED-mod_Ots");
         stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge='0' WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
         stmt.executeUpdate();
       }

// Модификация оценок ЕГЭ разрешена, если оценки абитуриента не заблокированы

       stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET Examen='' WHERE KodAbiturienta LIKE ?");
       stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
       stmt.executeUpdate();

       Enumeration paramNames = request.getParameterNames();
int l=1;
       while(paramNames.hasMoreElements()) {

          String paramName = (String)paramNames.nextElement();
          String paramValue[] = request.getParameterValues(paramName);

// Оценки ЕГЭ

          if(may_mod_ots && (paramName.indexOf("Ege_note") != -1 && paramValue[0].length() != 0)) {
//System.out.println(">>AMD_10-mod_Ots");
            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge=? WHERE KodAbiturienta=? AND KodPredmeta=?");
            stmt.setObject(1, new Integer(paramValue[0]),Types.INTEGER);                           // Оценка
            stmt.setObject(2, abit_A.getKodAbiturienta(),Types.INTEGER);                           // Код абитуриента
            stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);                  // Код предмета
            stmt.executeUpdate();
            


          }
          
          if(may_mod_ots && (paramName.indexOf("Ege_year") != -1 && paramValue[0].length() != 0)) {
        	//System.out.println(">>AMD_10-mod_Ots");
        	            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET God=? WHERE KodAbiturienta=? AND KodPredmeta=?");
        	            stmt.setObject(1, new Integer(paramValue[0]),Types.INTEGER);                           // Оценка
        	            stmt.setObject(2, abit_A.getKodAbiturienta(),Types.INTEGER);                           // Код абитуриента
        	            stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);                  // Код предмета
        	            stmt.executeUpdate();
        	            

        	          }
          
          if(paramName.indexOf("Attestat") != -1 && paramValue[0].length() != 0) {
        	//System.out.println(">>AMD_10-mod_Ots");
        	            stmt = conn.prepareStatement("UPDATE Oa SET OtsenkaAtt=? WHERE KodAbiturienta=? AND KodPredmeta=?");
        	            stmt.setObject(1, new Integer(paramValue[0]),Types.INTEGER);                           // Оценка
        	            stmt.setObject(2, abit_A.getKodAbiturienta(),Types.INTEGER);                           // Код абитуриента
        	            stmt.setObject(3, new Integer(paramName.substring(8)),Types.INTEGER);                  // Код предмета
        	            stmt.executeUpdate();
        	          }
          
// Признак экзамена (в формате ВУЗа или в формате ЕГЭ)

          if(paramName.indexOf("Examen") != -1 && paramValue[0].length() != 0) {
//System.out.println(">>AMD_10-mod_PARAMs");
            stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET Examen=? WHERE KodAbiturienta=? AND KodPredmeta=?");
            stmt.setObject(1, StringUtil.toDB((paramValue[0]).trim()),Types.VARCHAR);              // Признак экзамена
            stmt.setObject(2, abit_A.getKodAbiturienta(),Types.INTEGER);                           // Код абитуриента
            stmt.setObject(3, new Integer(paramName.substring(6)),Types.INTEGER);                  // Код предмета
            stmt.executeUpdate();
          }
       }

/**********************************/
/**********   Конкурс   ***********/
/**********************************/

// ОБЯЗАТЕЛЬНО ПРОВЕРЯЕМ - ИЗМЕНИЛАСЬ ЛИ ПРОФИЛИРУЮЩАЯ СПЕЦИАЛЬНОСТЬ, если ДА, то вносим дату модификации карточки в БД

       boolean need_modify = false;

// Определение типа специальности

       

       stmt = conn.prepareStatement("SELECT kon.KodKonkursa FROM Konkurs kon, Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Prioritet LIKE '1' AND kon.KodAbiturienta LIKE ? AND sp.KodSpetsialnosti LIKE ?");
       stmt.setObject(1,abit_A.getKodAbiturienta(),Types.VARCHAR);
       stmt.setObject(2,specc,Types.INTEGER);
 
       rs = stmt.executeQuery();
       if(!rs.next()) {

// Приоритетная спец-ть не найдена, значит она изменилась и устанавливаем новую дату модификации (чтобы абитуриент попал в журнал)

         stmt = conn.prepareStatement("UPDATE Abiturient SET DataModify=? WHERE KodAbiturienta LIKE ?");
         stmt.setObject(1, StringUtil.CurrDate("."),Types.VARCHAR);
         stmt.setObject(2, abit_A.getKodAbiturienta(),Types.VARCHAR);
         stmt.executeUpdate();
       }

// Удаляем старые данные о конкурсе
// ??? Надо как-то блокировать изменение набора очных специальностей у тех, кто уже участвовал и не прошел по конкурсу
// ВРЕМЕННО РАЗРЕШАЮ ПОЛНОЦЕННОЕ ИЗМЕНЕНИЕ КАРТОЧКИ ДЛЯ ОПЕРАТОРОВ ИВЦ (не операторов ввода)
// !!! А для зачисленных нужно запрещать (полностью блокировать) изменение карточки вообще

     if(user.getGroup().getTypeId()!=1) 
       stmt = conn.prepareStatement("DELETE FROM Konkurs WHERE KodAbiturienta LIKE ? AND Zach IS NULL"); // Для операторов ввода
     else
       stmt = conn.prepareStatement("DELETE FROM Konkurs WHERE KodAbiturienta LIKE ?");

       stmt.setObject(1, abit_A.getKodAbiturienta(),Types.VARCHAR);
       stmt.executeUpdate();

       need_modify = true;
       
       
       
       
       

/*****************************/
/****** 1. Приоритетная ******/
/*****************************/
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
           if(!abit_A.getSpecial2().equals(spc7) && need_modify) {
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

         
         
         stmt = conn.prepareStatement("SELECT KodKonkursa FROM Konkurs WHERE KodAbiturienta LIKE ? AND KodSpetsialnosti LIKE ? AND Forma_Ob LIKE ?");
         stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
         stmt.setObject(2, kSpec, Types.VARCHAR);
         stmt.setObject(3, Tip_Spec, Types.VARCHAR);
         rs = stmt.executeQuery();
         if(!rs.next()) {
        	 stmt = conn.prepareStatement("INSERT INTO Konkurs(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
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
             
            
                 stmt.setObject(16, 1, Types.VARCHAR);
             
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
             if(abit_A.getSog1() != null)
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
l=1;
if(!abit_A.getSpecial3().equals(spc7) && need_modify) {
	   kSpec = "-1";
    Abbr_Spec2 = "X1Y1Z1";
//Определение типа специальности
   /* stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
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
//Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon


stmt = conn.prepareStatement("SELECT KodKonkursa FROM Konkurs WHERE KodAbiturienta LIKE ? AND KodSpetsialnosti LIKE ? AND Forma_Ob LIKE ?");
stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
stmt.setObject(2, kSpec, Types.VARCHAR);
stmt.setObject(3, Tip_Spec, Types.VARCHAR);
rs = stmt.executeQuery();
if(!rs.next()) {
 	 stmt = conn.prepareStatement("INSERT INTO Konkurs(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
      stmt.setObject(2, Spec2, Types.VARCHAR);
      stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
      stmt.setObject(4, chet, Types.VARCHAR);
      stmt.setObject(5, Tip_Spec, Types.VARCHAR);
      if(abit_A.getBud_2() != null && pp1!=0)
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
      
      stmt.setObject(16, 2, Types.VARCHAR);
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
      stmt.setObject(23, abit_A.getFito_2(), Types.INTEGER);
      if(abit_A.getSog2() != null)
          stmt.setObject(24, "д", Types.VARCHAR);
        else
          stmt.setNull(24, Types.VARCHAR);
    stmt.executeUpdate();
  
  chet++;
}
}
/*********************/
/****** 3.      ******/
/*********************/
if(!abit_A.getSpecial4().equals(spc7) && need_modify) {
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

	           stmt = conn.prepareStatement("SELECT KodKonkursa FROM Konkurs WHERE KodAbiturienta LIKE ? AND KodSpetsialnosti LIKE ? AND Forma_Ob LIKE ?");
	           stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
	           stmt.setObject(2, kSpec, Types.VARCHAR);
	           stmt.setObject(3, Tip_Spec, Types.VARCHAR);
	           rs = stmt.executeQuery();
	           if(!rs.next()) {
	        	 stmt = conn.prepareStatement("INSERT INTO Konkurs (KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	             stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
	             stmt.setObject(2, Spec3, Types.VARCHAR);
	             stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
	             stmt.setObject(4, chet, Types.VARCHAR);
	             stmt.setObject(5, Tip_Spec, Types.VARCHAR);
	             if(abit_A.getBud_3() != null && pp2!=0)
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
	             
	             stmt.setObject(16, 3, Types.VARCHAR);
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
	             stmt.setObject(23, abit_A.getFito_3(), Types.INTEGER);
	             if(abit_A.getSog3() != null)
	                 stmt.setObject(24, "д", Types.VARCHAR);
	               else
	                 stmt.setNull(24, Types.VARCHAR);
	             chet++;
	           stmt.executeUpdate();
	         }
	        
	       }
       

/*********************/
/***** 4.        *****/
/*********************/

if(!abit_A.getSpecial5().equals(spc7) && need_modify) {
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
	    	   /*l=1;
	           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
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

         
         
         stmt = conn.prepareStatement("SELECT KodKonkursa FROM Konkurs WHERE KodAbiturienta LIKE ? AND KodSpetsialnosti LIKE ? AND Forma_Ob LIKE ?");
         stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
         stmt.setObject(2, Spec4, Types.VARCHAR);
         stmt.setObject(3, Tip_Spec, Types.VARCHAR);
         rs = stmt.executeQuery();
         if(!rs.next()) {
        	 stmt = conn.prepareStatement("INSERT INTO Konkurs(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
             stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
             stmt.setObject(2, Spec4, Types.VARCHAR);
             stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
             stmt.setObject(4, chet, Types.VARCHAR);
             stmt.setObject(5, Tip_Spec, Types.VARCHAR);
             if(abit_A.getBud_4() != null && pp3!=0)
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
             stmt.setObject(23, abit_A.getFito_4(), Types.INTEGER);
             if(abit_A.getSog4() != null)
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

if(!abit_A.getSpecial6().equals(spc7) && need_modify) {
	stmt = conn.prepareStatement("SELECT Tip_Spec FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
	   stmt.setObject(1,Spec5,Types.VARCHAR);
	   rs = stmt.executeQuery();
    if(rs.next()) {
	   Tip_Spec = rs.getString(1);  
    }else{
 	   Tip_Spec="з";
    }
	 kSpec = "-1";
	 Abbr_Spec2 = "X1Y1Z1";
	// Определение типа специальности
	    	   /*l=1;
	           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
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

        
        
        stmt = conn.prepareStatement("SELECT KodKonkursa FROM Konkurs WHERE KodAbiturienta LIKE ? AND KodSpetsialnosti LIKE ? AND Forma_Ob LIKE ?");
        stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
        stmt.setObject(2, Spec5, Types.VARCHAR);
        stmt.setObject(3, Tip_Spec, Types.VARCHAR);
        rs = stmt.executeQuery();
        if(!rs.next()) {
       	 stmt = conn.prepareStatement("INSERT INTO Konkurs(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
            stmt.setObject(2, Spec5, Types.VARCHAR);
            stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
            stmt.setObject(4, chet, Types.VARCHAR);
            stmt.setObject(5, Tip_Spec, Types.VARCHAR);
            if(abit_A.getBud_5() != null && pp4!=0)
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
            stmt.setObject(23, abit_A.getFito_5(), Types.INTEGER);
            if(abit_A.getSog5() != null)
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

if(!abit_A.getSpecial7().equals(spc7) && need_modify) {
	stmt = conn.prepareStatement("SELECT Tip_Spec FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
	   stmt.setObject(1,Spec6,Types.VARCHAR);
	   rs = stmt.executeQuery();
    if(rs.next()) {
	   Tip_Spec = rs.getString(1);  
    }else{
 	   Tip_Spec="з";
    }
	 kSpec = "-1";
	 Abbr_Spec2 = "X1Y1Z1";
	// Определение типа специальности
	    	   /*l=1;
	           stmt = conn.prepareStatement("SELECT ens.KodPredmeta,s.nazvaniespetsialnosti FROM spetsialnosti s, ekzamenynaspetsialnosti ens WHERE s.kodspetsialnosti=ens.kodspetsialnosti and s.kodspetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
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
	           }*/
	           
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

        
        
        stmt = conn.prepareStatement("SELECT KodKonkursa FROM Konkurs WHERE KodAbiturienta LIKE ? AND KodSpetsialnosti LIKE ? AND Forma_Ob LIKE ?");
        stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
        stmt.setObject(2, Spec6, Types.VARCHAR);
        stmt.setObject(3, Tip_Spec, Types.VARCHAR);
        rs = stmt.executeQuery();
        if(!rs.next()) {
       	 stmt = conn.prepareStatement("INSERT INTO Konkurs(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Dog_Ok,NPD,Stob,Pr1,Pr2,Pr3,Target,Tname,Fito,Olimp,Op,Rlgot,PR,Three,Six,Prof,Sogl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
            stmt.setObject(2, Spec6, Types.VARCHAR);
            stmt.setObject(3, Abbr_Spec2+nomer_ab+"-"+chet, Types.VARCHAR);
            stmt.setObject(4, chet, Types.VARCHAR);
            stmt.setObject(5, Tip_Spec, Types.VARCHAR);
            if(abit_A.getBud_6() != null && pp5!=0)
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
            stmt.setObject(23, abit_A.getFito_6(), Types.INTEGER);
            if(abit_A.getSog6() != null)
                stmt.setObject(24, "д", Types.VARCHAR);
              else
                stmt.setNull(24, Types.VARCHAR);
          stmt.executeUpdate();
        }
        chet++;
      }

// Проверка количества очных специальностей (их по правилам приема не больше 3х)


       
       
       


//System.out.println(">>AMD_11-7");
       if( !re_enter) {

// Закрепление транзакции

         conn.setAutoCommit(true);

         conn.commit();

         mess.setStatus("Внимание!");

         mess.setMessage("Сведения об абитуриенте были успешно изменены!");

//         result = true;

         abit_A.setKodAbiturienta(abit_A.getKodAbiturienta());

         abit_A.setFamilija((abit_A.getFamilija()).substring(0,1).toUpperCase()+(abit_A.getFamilija()).substring(1));

         abit_A.setImja((abit_A.getImja()).substring(0,1).toUpperCase()+(abit_A.getImja()).substring(1));

         abit_A.setOtchestvo((abit_A.getOtchestvo()).substring(0,1).toUpperCase()+(abit_A.getOtchestvo()).substring(1));
         
         String fo = new String();
         String oo = new String();
         stmt = conn.prepareStatement("SELECT forma_ob,bud from konkurs where kodabiturienta like ? and prioritet=1");
         stmt.setObject(1, abit_A.getKodAbiturienta(), Types.INTEGER);
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
         stmt.setObject(3, abit_A.getKodAbiturienta(), Types.INTEGER);
         stmt.executeUpdate();
         
         
         

         form.setAction(us.getClientIntName("mod_success","act-mod-ok"));

       }
    }



/************************************************************************************************/
/************************************************************************************************/
/**************    ПОДГОТОВКА ДАННЫХ ДЛЯ ПОВТОРНОГО ВВОДА КАРТОЧКИ АБИТУРИЕНТА   ****************/
/************************************************************************************************/
/************************************************************************************************/
//System.out.println(">>AMD_13");
    if( re_enter ) {
//System.out.println(">>AMD_14");
// Ошибка. Откат транзакции.

      conn.setAutoCommit(false);

      conn.rollback();

      conn.setAutoCommit(true);

      form.setAction(us.getClientIntName("md_dl","re_new"));

//      result = false;

      abit_A.setKodAbiturienta(abit_A.getKodAbiturienta());
      abit_A.setNomerLichnogoDela(abit_A.getNomerLichnogoDela());
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
      abit_A.setDataVydDokumenta(abit_A.getDataVydDokumenta());
      abit_A.setGorod_Prop(abit_A.getGorod_Prop());
      abit_A.setUlica_Prop(abit_A.getUlica_Prop());
      abit_A.setDom_Prop(abit_A.getDom_Prop());
      abit_A.setKvart_Prop(abit_A.getKvart_Prop());
      abit_A.setGodOkonchanijaSrObrazovanija(abit_A.getGodOkonchanijaSrObrazovanija());
      abit_A.setGdePoluchilSrObrazovanie(abit_A.getGdePoluchilSrObrazovanie());
      abit_A.setNazvanie(abit_A.getNazvanie());
      abit_A.setNazvanieRajona(abit_A.getNazvanieRajona());
      abit_A.setNazvanieOblasti(abit_A.getNazvanieOblasti());
      abit_A.setTipOkonchennogoZavedenija(abit_A.getTipOkonchennogoZavedenija());
      abit_A.setNomerShkoly(abit_A.getNomerShkoly());
      abit_A.setKodLgot(abit_A.getKodLgot());
      abit_A.setKodZavedenija(abit_A.getKodZavedenija());
      abit_A.setKodKursov(abit_A.getKodKursov());
    
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
      abit_A.setS_okso_1(abit_A.getS_okso_1());
      abit_A.setS_okso_2(abit_A.getS_okso_2());
      abit_A.setS_okso_3(abit_A.getS_okso_3());
      abit_A.setS_okso_4(abit_A.getS_okso_4());
      abit_A.setS_okso_5(abit_A.getS_okso_5());
      abit_A.setS_okso_6(abit_A.getS_okso_6());
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
      abit_A.setGruppa(abit_A.getGruppa());
      abit_A.setMestoRojdenija(abit_A.getMestoRojdenija());
      abit_A.setDiplomOtlichija(abit_A.getDiplomOtlichija());
      abit_A.setUdostoverenieLgoty(abit_A.getUdostoverenieLgoty());
      
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
      abit_A.setSpecial8(abit_A.getSpecial8());
      abit_A.setSpecial9(abit_A.getSpecial9());
      abit_A.setExists_st_Mag(abit_A.getExists_st_Mag());
      abit_A.setStepen_Mag(abit_A.getStepen_Mag());

      abit_A.setSpecial222(abit_A.getSpecial222());
      
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
      
      
      stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=1 ORDER BY s.KodSpetsialnosti ASC");
      stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
		  rs = stmt.executeQuery();
	        if (rs.next()) {
	        	AbiturientBean abit_TMP = new AbiturientBean();
	        	abit_TMP.setSpecial2(rs.getString(1));
	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
	        	abit_SD_S2.add(abit_TMP);
      stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
  	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
  	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
  	   rs1 = stmt1.executeQuery();
      while (rs1.next()) {
    	  abit_TMP = new AbiturientBean();
          abit_TMP.setSpecial2(rs1.getString(1));
          abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
          abit_SD_S2.add(abit_TMP);
        }
	        }else{
	        	AbiturientBean abit_TMP=new AbiturientBean();
	        	abit_TMP.setSpecial2("-");
	        	abit_TMP.setNazvanieSpetsialnosti("-");
	        	abit_SD_S2.add(abit_TMP);
	        }
	        
	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=2 ORDER BY s.KodSpetsialnosti ASC");
          stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
  		  rs = stmt.executeQuery();
  	        if (rs.next()) {
  	        	AbiturientBean abit_TMP = new AbiturientBean();
  	        	abit_TMP.setSpecial2(rs.getString(1));
  	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
  	        	abit_SD_S22.add(abit_TMP);
          stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
      	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
	    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
	    	   rs1 = stmt1.executeQuery();
          while (rs1.next()) {
        	  abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial2(rs1.getString(1));
              abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
              abit_SD_S22.add(abit_TMP);
            }
  	        }else{
  	        	AbiturientBean abit_TMP=new AbiturientBean();
  	        	abit_TMP.setSpecial2("-");
  	        	abit_TMP.setNazvanieSpetsialnosti("-");
  	        	abit_SD_S22.add(abit_TMP);
  	        }
  	        
  	        
  	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=3 ORDER BY s.KodSpetsialnosti ASC");
              stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
	    		  rs = stmt.executeQuery();
	    	        if (rs.next()) {
	    	        	AbiturientBean abit_TMP = new AbiturientBean();
	    	        	abit_TMP.setSpecial2(rs.getString(1));
	    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
	    	        	abit_SD_S222.add(abit_TMP);
              stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
	        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
		    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
		    	   rs1 = stmt1.executeQuery();
              while (rs1.next()) {
            	  abit_TMP = new AbiturientBean();
                  abit_TMP.setSpecial2(rs1.getString(1));
                  abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
                  abit_SD_S222.add(abit_TMP);
                }
	    	        }else{
	    	        	AbiturientBean abit_TMP=new AbiturientBean();
	    	        	abit_TMP.setSpecial2("-");
	    	        	abit_TMP.setNazvanieSpetsialnosti("-");
	    	        	abit_SD_S222.add(abit_TMP);
	    	        }
	        
	        
	        
	        
	        
	        
      
	    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=4 ORDER BY s.KodSpetsialnosti ASC");
	                stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
		    		  rs = stmt.executeQuery();
		    	        if (rs.next()) {
		    	        	AbiturientBean abit_TMP = new AbiturientBean();
		    	        	abit_TMP.setSpecial2(rs.getString(1));
		    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
		    	        	abit_SD_S3.add(abit_TMP);
	                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
		        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
			    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
			    	   rs1 = stmt1.executeQuery();
	                while (rs1.next()) {
	              	  abit_TMP = new AbiturientBean();
	                    abit_TMP.setSpecial2(rs1.getString(1));
	                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
	                    abit_SD_S3.add(abit_TMP);
	                  }
		    	        }else{
		    	        	AbiturientBean abit_TMP=new AbiturientBean();
		    	        	abit_TMP.setSpecial2("-");
		    	        	abit_TMP.setNazvanieSpetsialnosti("-");
		    	        	abit_SD_S3.add(abit_TMP);
		    	        }
      
		    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=5 ORDER BY s.KodSpetsialnosti ASC");
		                stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
			    		  rs = stmt.executeQuery();
			    	        if (rs.next()) {
			    	        	AbiturientBean abit_TMP = new AbiturientBean();
			    	        	abit_TMP.setSpecial2(rs.getString(1));
			    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
			    	        	abit_SD_S33.add(abit_TMP);
		                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
			        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
				    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
				    	   rs1 = stmt1.executeQuery();
		                while (rs1.next()) {
		              	  abit_TMP = new AbiturientBean();
		                    abit_TMP.setSpecial2(rs1.getString(1));
		                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
		                    abit_SD_S33.add(abit_TMP);
		                  }
			    	        }else{
			    	        	AbiturientBean abit_TMP=new AbiturientBean();
			    	        	abit_TMP.setSpecial2("-");
			    	        	abit_TMP.setNazvanieSpetsialnosti("-");
			    	        	abit_SD_S33.add(abit_TMP);
			    	        }
			    	        
			    	        stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,konkurs k WHERE k.kodabiturienta like ? AND k.kodspetsialnosti=s.kodspetsialnosti AND k.fito=6 ORDER BY s.KodSpetsialnosti ASC");
			                stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
				    		  rs = stmt.executeQuery();
				    	        if (rs.next()) {
				    	        	AbiturientBean abit_TMP = new AbiturientBean();
				    	        	abit_TMP.setSpecial2(rs.getString(1));
				    	        	abit_TMP.setNazvanieSpetsialnosti(rs.getString(3));
				    	        	abit_SD_S333.add(abit_TMP);
			                stmt1 = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti,s.KodFakulteta,s.NazvanieSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE f.kodfakulteta like ? AND f.KodFakulteta = s.KodFakulteta AND s.Tip_Spec not like 'о' AND s.kodspetsialnosti not like ? ORDER BY s.KodSpetsialnosti,s.KodFakulteta ASC");
				        	stmt1.setObject(1,rs.getString(2),Types.VARCHAR);
					    	   stmt1.setObject(2,rs.getString(1),Types.VARCHAR);
					    	   rs1 = stmt1.executeQuery();
			                while (rs1.next()) {
			              	  abit_TMP = new AbiturientBean();
			                    abit_TMP.setSpecial2(rs1.getString(1));
			                    abit_TMP.setNazvanieSpetsialnosti(rs1.getString(3));
			                    abit_SD_S333.add(abit_TMP);
			                  }
				    	        }else{
				    	        	AbiturientBean abit_TMP=new AbiturientBean();
				    	        	abit_TMP.setSpecial2("-");
				    	        	abit_TMP.setNazvanieSpetsialnosti("-");
				    	        	abit_SD_S333.add(abit_TMP);
				    	        }
      
      
      
      
      
				    	        abit_A.setNazv_DipBak(abit_A.getNazv_DipBak());
				    	        abit_A.setNazv_DipSpec(abit_A.getNazv_DipSpec());
				    	        abit_A.setNomerPotoka(abit_A.getNomerPotoka());
				    	        abit_A.setShifrFakulteta(abit_A.getShifrFakulteta());
				    	        abit_A.setSpecial2(abit_A.getSpecial2());
				    	        abit_A.setSpecial3(abit_A.getSpecial3());
				    	        abit_A.setSpecial4(abit_A.getSpecial4());
				    	        abit_A.setSpecial5(abit_A.getSpecial5());
				    	        abit_A.setSpecial6(abit_A.getSpecial6());
				    	        abit_A.setSpecial7(abit_A.getSpecial7());
				    	        abit_A.setSpecial13(abit_A.getSpecial13());
				    	        abit_A.setSpecial10(abit_A.getSpecial10());
      
      
      
      
      
      /*stmt = conn.prepareStatement("SELECT NazvanieOblasti FROM Oblasti WHERE KodOblasti LIKE ?");
      stmt.setObject(1, abit_A.getKodOblasti(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) abit_A.setNazvanieOblasti(rs.getString(1));
      if(abit_A.getNazvanieOblasti()==null) abit_A.setNazvanieOblasti("");

      stmt = conn.prepareStatement("SELECT NazvanieRajona FROM Rajony WHERE KodRajona LIKE ?");
      stmt.setObject(1, abit_A.getKodRajona(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) abit_A.setNazvanieRajona(rs.getString(1));
      if(abit_A.getNazvanieRajona()==null) abit_A.setNazvanieRajona("");

      stmt = conn.prepareStatement("SELECT Nazvanie FROM Punkty WHERE KodPunkta LIKE ?");
      stmt.setObject(1, abit_A.getKodPunkta(),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) abit_A.setNazvanie(rs.getString(1));
      if(abit_A.getNazvanie()==null) abit_A.setNazvanie("");*/

// Данные о полученных оценках для автоматизации повторного ввода

    Enumeration paramNames = request.getParameterNames();
    ArrayList ege  = new ArrayList();
    ArrayList exam = new ArrayList();
    ArrayList kpr  = new ArrayList();
    ArrayList kprx = new ArrayList();
    ArrayList kpre = new ArrayList();
    ArrayList pr   = new ArrayList();

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
        exam.add(StringUtil.RecodeLetter(paramValue[0]));
        kprx.add(paramName.substring(6));  // Код предмета
      }
    }

// Сортировка массивов оценок 
    int index=0,maxKod=0;
    ArrayList sortedMass = new ArrayList();
    ArrayList sortedMass2 = new ArrayList();

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
      abit_TMP.setEge(""+ege.get(i));
      abit_TMP.setExamen(""+exam.get(i));
      abit_TMP.setKodPredmeta(new Integer(""+kpr.get(i)));
      abit_TMP.setPredmet(""+pr.get(i));
      abit_A_S1.add(abit_TMP);
    }
   }

  }

/************************************************************************************************/
/**************************      УДАЛЕНИЕ ЗАПИСИ ОБ АБИТУРИЕНТЕ      ****************************/
/************************************************************************************************/

  if ( form.getAction().equals("delete")  && (user.getGroup().getTypeId()==1) ) {

     form.setAction(us.getClientIntName("delete","act_"+abit_A.getKodAbiturienta()));

// Начало транзакции

     stmt = conn.prepareStatement("INSERT INTO DrWatson(UserName,UserId,UserType,UserIP,AcTime,UAction) VALUES(?,?,?,?,?,?)");
     stmt.setObject(1,user.getName(),Types.VARCHAR);
     stmt.setObject(2,user.getUid(),Types.VARCHAR);
     stmt.setObject(3,user.getGroup().getTypeId(),Types.VARCHAR);
     stmt.setObject(4,user.getUip(),Types.VARCHAR);
     stmt.setObject(5,StringUtil.CurrTime(":"),Types.VARCHAR);
     stmt.setObject(6,"Detete Abit Kod="+request.getParameter("kodAbiturienta"),Types.VARCHAR);
     stmt.executeUpdate();

// Здесь при удалении возможно нужно делать проверку - участвовал ли абитуриент в конкурсе и если участвовал, то запрещать его удаление

     conn.setAutoCommit(false);

     stmt = conn.prepareStatement("DELETE FROM Abiturient WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();

     stmt = conn.prepareStatement("DELETE FROM ZajavlennyeShkolnyeOtsenki WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();

     stmt = conn.prepareStatement("DELETE FROM Kontrol_ZSO WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();

     stmt = conn.prepareStatement("DELETE FROM Kontrol_Kon WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();

     stmt = conn.prepareStatement("DELETE FROM Otsenki WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();

     stmt = conn.prepareStatement("DELETE FROM Konkurs WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();
     
     stmt = conn.prepareStatement("DELETE FROM Oa WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();
     stmt = conn.prepareStatement("DELETE FROM Os WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();

     stmt = conn.prepareStatement("DELETE FROM MedSpravka WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();
     
     stmt = conn.prepareStatement("DELETE FROM AbitDopInf WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();
     
     stmt = conn.prepareStatement("DELETE FROM PR WHERE KodAbiturienta=?");
     stmt.setObject(1,new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
     stmt.executeUpdate();
// Закрепление транзакции

     conn.setAutoCommit(true);
     conn.commit();

     mess.setStatus("Внимание!");
     mess.setMessage("Сведения об абитуриенте были успешно удалены из базы данных!");

     result = true;
  }

/************************************************************************************************/
/****************************      ОБНОВЛЕНИЕ ПОЛЯ ЗАВЕДЕНИЙ      *******************************/
/************************************************************************************************/

    abit_A_S8.clear();
    stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodZavedenija FROM Zavedenija WHERE KodVuza LIKE ? ORDER BY 1 ASC");
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
        request.setAttribute("abit_A", abit_A);
        request.setAttribute("sb", sb);
        request.setAttribute("abits_A", abits_A);
        request.setAttribute("predmets", predmets);
        request.setAttribute("specials", specials);
        request.setAttribute("notesEx_A", notesEx_A);
        request.setAttribute("notesSk_A", notesSk_A);
        request.setAttribute("abit_forms", abit_forms);
        request.setAttribute("abit_osnovs", abit_osnovs); 
        request.setAttribute("abit_A_S1", abit_A_S1);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S22", abit_SD_S22);
        request.setAttribute("abit_SD_S222", abit_SD_S222);
        request.setAttribute("abit_SD_S3", abit_SD_S3);
        request.setAttribute("abit_SD_S33", abit_SD_S33);
        request.setAttribute("abit_SD_S333", abit_SD_S333);
        request.setAttribute("abit_SD_S11", abit_SD_S11);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S5", abit_A_S5);
        request.setAttribute("abit_A_S6", abit_A_S6);
        request.setAttribute("abit_A_S7", abit_A_S7);
        request.setAttribute("abit_A_S8", abit_A_S8);
        request.setAttribute("abit_A_S9", abit_A_S9);
        request.setAttribute("abit_A_S10", abit_A_S10);
        request.setAttribute("att", att);
        request.setAttribute("ba", ba);
        request.setAttribute("mess", mess);
        request.setAttribute("nationalityList", nationalityList);
        request.setAttribute("abit_A_Kladr", abit_A_Kladr);
        request.setAttribute("abit_A_Rajon", abit_A_Rajon);
        request.setAttribute("abit_A_Punkt", abit_A_Punkt);
        request.setAttribute("obr_A_Rajon", obr_A_Rajon);
        request.setAttribute("obr_A_Punkt", obr_A_Punkt);

        if(otsenki) return mapping.findForward("ots");
        if(request.getParameter("back_lst")!=null) return mapping.findForward("lst");
        if(error) return mapping.findForward("error");
        if(result) return mapping.findForward("result");
        return mapping.findForward("success");
  }
}                 