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

public class AbitModDelOnlineAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        javax.sql.DataSource                    dataSource  = null;
        java.sql.Connection                     conn        = null;

        
      //  Connection        conn          = null;
        PreparedStatement stmt          = null;
        ResultSet         rs            = null;
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
        
        double 				summa = 0;
        String            ordr_ab       = new String();
        int pp=0;
        String            two_t_names[] = {"Konkurs","Kontrol_Kon"};
        
        int               kodPunkta       = 1;
       int               kodRajona       = 1;
       int               kodOblasti      = 1;
      //  int               kZavedenija   = 1;
        int               kAbiturienta  = 1;
     //   int               Col_Specs     = 0; 
        
        
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
        UserBean          user          = (UserBean)session.getAttribute("user");
        String spravka=null;

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

         // UserConn us = new UserConn(request, mapping);
         // conn = us.getConn(user.getSid());
        	 Context env = (Context) new InitialContext().lookup("java:comp/env");

             dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME_ONLINE);

             if (dataSource == null) throw new ServletException("'" + Constants.DATASOURCE_NAME + "' is an unknown DataSource");

             conn = dataSource.getConnection();
          request.setAttribute( "abitSrchForm", form );

/*****************  Возврат к предыдущей странице   *******************/

        //  if(us.quit("exit")) return mapping.findForward("back");

// Переход к странице поиска

          if (request.getParameter("srch_res")!=null) {
             session.setAttribute("resrch","1");
             return mapping.findForward("goto");
          }

/************************************************************************************************/
/********************** Подготовка данных для ввода с помощью селекторов ************************/
/************************************************************************************************/

          stmt = conn.prepareStatement("SELECT DISTINCT KodLgot,ShifrLgot FROM Lgoty WHERE KodVuza LIKE ? ORDER BY 1 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodLgot(new Integer(rs.getInt(1)));
            abit_TMP.setShifrLgot(rs.getString(2));
            abit_A_S4.add(abit_TMP);
          }

          stmt = conn.prepareStatement("SELECT DISTINCT KodMedali,ShifrMedali FROM Medali WHERE KodVuza LIKE ? ORDER BY 1 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodMedali(new Integer(rs.getInt(1)));
            abit_TMP.setShifrMedali(rs.getString(2));
            abit_A_S6.add(abit_TMP);
          }

          stmt = conn.prepareStatement("SELECT DISTINCT KodKursov,ShifrKursov FROM Kursy WHERE KodVuza LIKE ? ORDER BY 1 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodKursov(new Integer(rs.getInt(1)));
            abit_TMP.setShifrKursov(rs.getString(2));
            abit_A_S5.add(abit_TMP);
          }

          abit_A.setSpecial1("");
          stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ?  ORDER BY KodPredmeta ASC");
          stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
            abit_A.setSpecial1(abit_A.getSpecial1()+"%"+rs.getString(1));
            abit_TMP.setPredmet((rs.getString(2)).substring(0,1).toUpperCase()+(rs.getString(2)).substring(1));
            abit_A_S7.add(abit_TMP);
          }
          
          stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? and (KodPredmeta = 3 or KodPredmeta = 10 or KodPredmeta = 5 or KodPredmeta = 1 or KodPredmeta = 8 or KodPredmeta = 4 or KodPredmeta = 9 or KodPredmeta = 2) ORDER BY KodPredmeta ASC");
          stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = stmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
           // abit_A.setSpecial1(abit_A.getSpecial1()+"%"+rs.getString(1));
            abit_TMP.setPredmet((rs.getString(2)).substring(0,1).toUpperCase()+(rs.getString(2)).substring(1));
            abit_A_S10.add(abit_TMP);
          }

          stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodZavedenija FROM Zavedenija WHERE KodVuza LIKE ? ORDER BY 1 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
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
          /*
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setSokr(rs.getString(1));
            abit_TMP.setKodZavedenija(new Integer(rs.getInt(2)));
            abit_A_S8.add(abit_TMP);*/
          
          }

          stmt = conn.prepareStatement("SELECT DISTINCT KodTselevogoPriema,ShifrPriema FROM TselevojPriem WHERE KodVuza LIKE ? ORDER BY 1 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
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

            //   form.setAction(us.getClientIntName("md_dl","init"));


/************************************************************************************************/
/************************************************************************************************/
/**********  ОТОБРАЖЕНИЕ ДАННЫХ АБИТУРИЕНТА С УЧЕТОМ ОЦЕНОК ПО ЗАПРОСУ ИЗ ФОРМЫ ПОИСКА  *********/
/************************************************************************************************/
/************************************************************************************************/

            } else if ( form.getAction().equals("give_ots") ) {

            //us.getClientIntName("give_ots","ots_"+abit_A.getKodAbiturienta());

// Личные и учебные данные

            stmt = conn.prepareStatement("SELECT DISTINCT f.Fakultet,s.Abbreviatura,s.ShifrSpetsialnostiOKSO,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,a.SeriaAtt,a.NomerAtt,oo.Nazvanie,fo.Sokr,m.ShifrMedali,k.ShifrKursov,l.ShifrLgot,tp.ShifrPriema,a.Prinjat,a.DokumentyHranjatsja,g.Gruppa,a.NomerPlatnogoDogovora,a.SrokObuchenija,a.Pol,a.PodtvMed,a.Gorod_Prop,a.Ulica_Prop,a.Dom_Prop,a.Kvart_Prop,a.Tel,a.MestoRojdenija,a.DiplomOtlichija,a.UdostoverenieLgoty,a.PostgraduateStudies,a.Traineeship,a.Internship FROM Gruppy g,Osnova_Obuch oo,Forma_Obuch fo,Fakultety f,Spetsialnosti s,Abiturient a,Medali m,Kursy k,Lgoty l,TselevojPriem tp WHERE g.KodGruppy=a.KodGruppy AND a.KodMedali=m.KodMedali AND k.KodKursov=a.KodKursov AND l.KodLgot=a.KodLgot AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND oo.KodOsnovyOb=a.KodOsnovyOb AND fo.KodFormyOb=a.KodFormyOb AND f.KodFakulteta=s.KodFakulteta AND s.KodSpetsialnosti = a.KodSpetsialnosti AND a.KodVuza LIKE ? AND a.KodAbiturienta LIKE ?");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            stmt.setObject(2, abit_A.getKodAbiturienta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
              abit_A.setAbbreviaturaFakulteta(rs.getString(1));
              abit_A.setAbbreviatura(rs.getString(2));
              abit_A.setShifrSpetsialnosti(rs.getString(3));
              abit_A.setFamilija(rs.getString(4));
              abit_A.setImja(rs.getString(5));
              abit_A.setOtchestvo(rs.getString(6));
              abit_A.setNomerLichnogoDela(rs.getString(7));
              abit_A.setSeriaAtt(rs.getString(8));
              abit_A.setNomerAtt(rs.getString(9));
              abit_A.setOsnovaOb(rs.getString(10));
              abit_A.setFormaOb(rs.getString(11));
              abit_A.setShifrMedali(rs.getString(12));
              abit_A.setShifrKursov(rs.getString(13));
              abit_A.setShifrLgot(rs.getString(14));
              abit_A.setShifrPriema(rs.getString(15));
              abit_A.setPrinjat(rs.getString(16));
              abit_A.setDokumentyHranjatsja(rs.getString(17));
              abit_A.setGruppa(rs.getString(18));
              abit_A.setNomerPlatnogoDogovora(rs.getString(19));
              abit_A.setSrokObuchenija(rs.getString(20));
              abit_A.setPol(rs.getString(21));
              if(rs.getString(22) != null) abit_A.setSpecial1(rs.getString(22));
                else abit_A.setSpecial1("-");
              }
              abit_A.setGorod_Prop(rs.getString(23)+", ул."+rs.getString(24)+", д."+rs.getString(25)+", кв."+rs.getString(26));
              abit_A.setTel(rs.getString(27));
              abit_A.setMestoRojdenija(rs.getString(28));

              if(rs.getString(29)!= null && rs.getString(29).length() > 3)
                abit_A.setDiplomOtlichija(rs.getString(29));
              else
                abit_A.setDiplomOtlichija("нет");

              if(rs.getString(30)!= null && rs.getString(30).length() > 3)
                abit_A.setUdostoverenieLgoty(rs.getString(30));
              else
                abit_A.setUdostoverenieLgoty("не имеет");
              abit_A.setPostgraduateStudies(rs.getString(31));
              abit_A.setTraineeship(rs.getString(32));
              abit_A.setInternship(rs.getString(33));

// Предметы и их количество

              stmt = conn.prepareStatement("SELECT DISTINCT np.Sokr,ens.KodPredmeta FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Abiturient a,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=ens.KodSpetsialnosti AND a.KodSpetsialnosti=ens.KodSpetsialnosti AND np.KodPredmeta = ens.KodPredmeta AND a.KodAbiturienta LIKE ? ORDER BY ens.KodPredmeta ASC");
              stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
              rs = stmt.executeQuery();
              while (rs.next()) {
                AbiturientBean abit_TMP = new AbiturientBean();
                abit_TMP.setPredmet(rs.getString(1));
                predmets.add(abit_TMP);
                kol_predm++;
              }
              abit_A.setPredmCount(""+kol_predm);

// Оценки школьные

              int old_Ka = -1;
              int ball = 0;

              AbiturientBean abit_TMP = new AbiturientBean();

              stmt = conn.prepareStatement("SELECT a.KodAbiturienta,zo.OtsenkaAtt,zo.OtsenkaEge,zo.OtsenkaZajavl,zo.Examen FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zo,Spetsialnosti s,Gruppy g,Konkurs k WHERE k.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta=k.KodAbiturienta AND a.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND a.KodAbiturienta LIKE ? AND zo.KodPredmeta IN (SELECT DISTINCT np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens, Gruppy g,Abiturient a WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND a.KodAbiturienta LIKE ? AND a.KodVuza LIKE ?) GROUP BY a.KodAbiturienta,zo.OtsenkaZajavl,zo.OtsenkaEge,zo.OtsenkaAtt,zo.KodPredmeta,zo.Examen ORDER BY a.KodAbiturienta,zo.KodPredmeta ASC");
              stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
              stmt.setObject(2,abit_A.getKodAbiturienta(),Types.INTEGER);
              stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER); 
              rs = stmt.executeQuery();
              while (rs.next()) {

                if(rs.getInt(1) != old_Ka) {

                  if(old_Ka != -1) {
                    abit_TMP.setNotes(notes);
                    notesSk_A.add(abit_TMP);
                  }

                  old_Ka = rs.getInt(1);

                  notes = new ArrayList();

                  abit_TMP = new AbiturientBean();
                  abit_TMP.setKodAbiturienta(new Integer(old_Ka));

                  for(int i=2;i<=5;i++){
                   note = rs.getString(i);
                   if(i == 3) ball += StringUtil.toInt(note,0);
                   if(!note.equals("-"))
                     notes.add(StringUtil.voidFilter(note));
                    else 
                    notes.add(Constants.emptyNote);
                  }

                } else { 
                   for(int i=2;i<=5;i++){
                    note = rs.getString(i);
                   if(i == 3) ball += StringUtil.toInt(note,0);
                    if(!note.equals("-"))
                     notes.add(StringUtil.voidFilter(note));
                    else 
                     notes.add(Constants.emptyNote);
                   }
                  }
              }

// Добавление оценок абитуриента в список

              abit_TMP.setNotes(notes);
              notesSk_A.add(abit_TMP);

              abit_A.setBallEge(new Integer(""+ball));

// Оценки экзаменационные

              old_Ka = -1;
              ball = 0;
              abit_TMP = new AbiturientBean();

              notes = new ArrayList();

              stmt = conn.prepareStatement("SELECT a.KodAbiturienta,o.Otsenka,o.Apelljatsija FROM Abiturient a,Otsenki o,Spetsialnosti s,Gruppy g,Konkurs k WHERE k.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta=o.KodAbiturienta AND a.KodAbiturienta=k.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND a.KodAbiturienta LIKE ? AND o.KodPredmeta IN (SELECT DISTINCT np.KodPredmeta FROM NazvanijaPredmetov np, EkzamenyNaSpetsialnosti ens, Gruppy g,Abiturient a WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND a.KodAbiturienta LIKE ? AND a.KodVuza LIKE ?) GROUP BY a.KodAbiturienta,o.Otsenka,o.KodPredmeta,o.Apelljatsija ORDER BY a.KodAbiturienta,o.KodPredmeta ASC");
              stmt.setObject(1,abit_A.getKodAbiturienta(),Types.INTEGER);
              stmt.setObject(2,abit_A.getKodAbiturienta(),Types.INTEGER);
              stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER); 
              rs = stmt.executeQuery();
              while (rs.next()) {

                if(rs.getInt(1) != old_Ka) {

                  if(old_Ka != -1) {
                    abit_TMP.setNotes(notes);
                    notesEx_A.add(abit_TMP);
                  }

                  old_Ka = rs.getInt(1);

                  notes = new ArrayList();

                  abit_TMP = new AbiturientBean();
                  abit_TMP.setKodAbiturienta(new Integer(old_Ka));

                  for(int i=2;i<=3;i++){
                   note = new String(""+rs.getString(i));
                   if(i == 2) ball += StringUtil.toInt(note,0);
                   if(!note.equals("-"))
                     notes.add(StringUtil.ntv(note));
                   else 
                     notes.add(Constants.emptyNote);
                  }

                } else { 
                   for(int i=2;i<=3;i++){
                    note = new String(""+rs.getString(i));
                    if(i == 2) ball += StringUtil.toInt(note,0);
                    if(!note.equals("-"))
                      notes.add(StringUtil.ntv(note));
                    else 
                      notes.add(Constants.emptyNote);
                   }
                  }
              }

// Добавление оценок абитуриента в список

              abit_TMP.setNotes(notes);
              notesEx_A.add(abit_TMP);

              abit_A.setBall(new Integer(""+ball));

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

              stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Six,kon.Three,kon.Olimp,kon.Target,kon.Forma_Ob,kon.Dog_Ok,sp.NazvanieSpetsialnosti,SUM(zso.OtsenkaEge),kon.NomerLichnogoDela,kon.Zach,kon.Prioritet FROM Konkurs kon,Spetsialnosti sp,ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE ens.KodSpetsialnosti=kon.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND zso.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND zso.KodAbiturienta LIKE ? GROUP BY sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Six,kon.Three,kon.Olimp,kon.Target,kon.Forma_Ob,kon.Dog_Ok,sp.NazvanieSpetsialnosti,kon.NomerLichnogoDela,kon.Zach,kon.Prioritet ORDER BY kon.Forma_Ob DESC,kon.Prioritet ASC");
              stmt.setObject(1, abit_A.getKodAbiturienta(),Types.INTEGER);
              rs = stmt.executeQuery();
              while(rs.next()) {         
                AbiturientBean abit_TMPx = new AbiturientBean();
                abit_TMPx.setShifrSpetsialnosti(rs.getString(1));
                abit_TMPx.setBud_1(StringUtil.val_to_plus(rs.getString(2)));
                abit_TMPx.setDog_1(StringUtil.val_to_plus(rs.getString(3)));
                abit_TMPx.setFito_1(StringUtil.val_to_plus(rs.getString(4)));
                abit_TMPx.setSix_1(StringUtil.val_to_plus(rs.getString(5)));
                abit_TMPx.setThree_1(StringUtil.val_to_plus(rs.getString(6)));
                abit_TMPx.setOlimp_1(StringUtil.val_to_plus(rs.getString(7)));
                abit_TMPx.setTarget_1(StringUtil.val_to_plus(rs.getString(8)));
                abit_TMPx.setForma_Ob1(rs.getString(9));
                abit_TMPx.setDog_ok_1(rs.getString(10));
                abit_TMPx.setNazvanieSpetsialnosti(rs.getString(11));
                abit_TMPx.setSpecial1(rs.getString(12));
                abit_TMPx.setNomerLichnogoDela(rs.getString(13));
                abit_TMPx.setKonkurs_1(StringUtil.ntv(rs.getString(14)));
                specials.add(abit_TMPx);
              }

              otsenki = true;


/************************************************************************************************/
/************************************************************************************************/
/***************  ЧТЕНИЕ ИНФОРМАЦИИ ИЗ БАЗЫ ДАННЫХ ДЛЯ ЕЕ ОТОБРАЖЕНИЯ В КАРТОЧКЕ  ***************/
/************************************************************************************************/
/************************************************************************************************/

            } else if ( form.getAction().equals("mod_del") ) {

// Выводим кнопку удаления абитуриента из системы для оператора ( Id == 1 )

                if( user.getGroup().getTypeId()==1 ) abit_A.setMay_del("yes");

                stmt = conn.prepareStatement("SELECT DokumentyHranjatsja,Abiturient.KodSpetsialnosti,NomerLichnogoDela,Familija,Imja,Otchestvo,KodKursov,KodMedali,KodLgot,NomerPlatnogoDogovora,DataRojdenija,Pol,SrokObuchenija,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,NomerShkoly,InostrannyjJazyk,NujdaetsjaVObschejitii,Grajdanstvo,KodPunkta,KodRajona,KodOblasti,TipOkonchennogoZavedenija,TrudovajaDejatelnost,NapravlenieOtPredprijatija,NomerSertifikata,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,VidDokSredObraz,Abiturient.Sobesedovanie,KodTselevogoPriema,Ball,BallEge,Prinjat,ShifrFakulteta,AbbreviaturaFakulteta,KopijaSertifikata,KodZavedenija,Gruppa,KodFormyOb,KodOsnovyOb,SeriaAtt,NomerAtt,Gorod_Prop,Ulica_Prop,Dom_Prop,Kvart_Prop,NomerSertifikata,Exists_st_Mag,Tip_Spec,Need_Spo,Stepen_Mag,Tel,MestoRojdenija,DiplomOtlichija,UdostoverenieLgoty,PostgraduateStudies,Traineeship,Internship FROM Gruppy,Abiturient,Fakultety,Spetsialnosti WHERE Gruppy.KodGruppy=Abiturient.KodGruppy AND Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_A.setDokumentyHranjatsja(rs.getString(1));
                  abit_A.setKodSpetsialnosti(new Integer(rs.getInt(2)));
                  abit_A.setNomerLichnogoDela(rs.getString(3));
                  abit_A.setFamilija(rs.getString(4));
                  abit_A.setImja(rs.getString(5));
                  abit_A.setOtchestvo(rs.getString(6));
                  abit_A.setKodKursov(new Integer(rs.getInt(7)));
                  abit_A.setKodMedali(new Integer(rs.getInt(8)));
                  abit_A.setKodLgot(new Integer(rs.getInt(9)));
                  abit_A.setNomerPlatnogoDogovora(rs.getString(10));
                  abit_A.setDataRojdenija(rs.getString(11));
                  abit_A.setPol(rs.getString(12));
                  abit_A.setSrokObuchenija(rs.getString(13));
                  abit_A.setGodOkonchanijaSrObrazovanija(new Integer(rs.getString(14)));
                  abit_A.setGdePoluchilSrObrazovanie(rs.getString(15));
                  abit_A.setNomerShkoly(rs.getString(16));
                  abit_A.setInostrannyjJazyk(rs.getString(17));
                  abit_A.setNujdaetsjaVObschejitii(rs.getString(18));
                  abit_A.setGrajdanstvo(rs.getString(19));
                  abit_A.setKodPunkta(new Integer(rs.getInt(20)));
                  abit_A.setKodRajona(new Integer(rs.getInt(21)));
                  abit_A.setKodOblasti(new Integer(rs.getInt(22)));
                  abit_A.setTipOkonchennogoZavedenija(rs.getString(23));
                  abit_A.setTrudovajaDejatelnost(rs.getString(24));
                  abit_A.setNapravlenieOtPredprijatija(rs.getString(25));
                  abit_A.setSpecial7(rs.getString(26));
                  abit_A.setTipDokumenta(rs.getString(27));
                  abit_A.setNomerDokumenta(rs.getString(28));
                  abit_A.setSeriaDokumenta(rs.getString(29));
                  abit_A.setDataVydDokumenta(StringUtil.DataConverter(rs.getString(30)));
                  abit_A.setKemVydDokument(rs.getString(31));
                  abit_A.setTipDokSredObraz(rs.getString(32));
                  abit_A.setVidDokSredObraz(rs.getString(33));
                  abit_A.setSobesedovanie(rs.getString(34));
                  abit_A.setKodTselevogoPriema(new Integer(rs.getInt(35)));
                  abit_A.setBall(new Integer(rs.getInt(36)));
                  abit_A.setBallEge(new Integer(rs.getInt(37)));
                  abit_A.setPrinjat(rs.getString(38));
                  abit_A.setShifrFakulteta(rs.getString(39));
                  abit_A.setAbbreviaturaFakulteta(rs.getString(40));
                  abit_A.setKopijaSertifikata(rs.getString(41));
                  abit_A.setKodZavedenija(new Integer(rs.getInt(42)));
                  abit_A.setGruppa(rs.getString(43));
                  abit_A.setKodFormyOb(new Integer(rs.getString(44)));
                  abit_A.setKodOsnovyOb(new Integer(rs.getString(45)));
                  abit_A.setSeriaAtt(rs.getString(46));
                  abit_A.setNomerAtt(rs.getString(47));
                  abit_A.setGorod_Prop(rs.getString(48));
                  abit_A.setUlica_Prop(rs.getString(49));
                  abit_A.setDom_Prop(rs.getString(50));
                  abit_A.setKvart_Prop(rs.getString(51));
                  abit_A.setNomerSertifikata(rs.getString(52));
                  abit_A.setExists_st_Mag(rs.getString(53));
                  abit_A.setTip_Spec(rs.getString(54));
                  abit_A.setNeed_Spo(rs.getString(55));
                  abit_A.setStepen_Mag(rs.getString(56));
                  abit_A.setTel(rs.getString(57));
                  abit_A.setMestoRojdenija(rs.getString(58));
                  abit_A.setDiplomOtlichija(rs.getString(59));
                  abit_A.setUdostoverenieLgoty(rs.getString(60));
                  abit_A.setPostgraduateStudies(rs.getString(61));
                  abit_A.setTraineeship(rs.getString(62));
                  abit_A.setInternship(rs.getString(63));
                }

                stmt = conn.prepareStatement("SELECT NazvanieOblasti FROM Oblasti WHERE KodOblasti LIKE ?");
                stmt.setObject(1, abit_A.getKodOblasti(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setNazvanieOblasti(rs.getString(1));
                if(abit_A.getNazvanieOblasti()==null) abit_A.setNazvanieOblasti("");
                kPredmeta="";

                stmt = conn.prepareStatement("SELECT NazvanieRajona FROM Rajony WHERE KodRajona LIKE ?");
                stmt.setObject(1, abit_A.getKodRajona(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setNazvanieRajona(rs.getString(1));
                if(abit_A.getNazvanieRajona()==null) abit_A.setNazvanieRajona("");

                stmt = conn.prepareStatement("SELECT PolnoeNaimenovanieZavedenija,Sokr FROM Zavedenija WHERE KodZavedenija LIKE ?");
                stmt.setObject(1, abit_A.getKodZavedenija(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_A.setPolnoeNaimenovanieZavedenija(rs.getString(1));
                  abit_A.setSokr(rs.getString(2));
                }

                stmt = conn.prepareStatement("SELECT Nazvanie FROM Punkty WHERE KodPunkta LIKE ?");
                stmt.setObject(1, abit_A.getKodPunkta(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setNazvanie(rs.getString(1));
                if(abit_A.getNazvanie()==null) abit_A.setNazvanie("");
//System.out.println(">>AMD_1");
                abit_A_S1.clear();
                stmt = conn.prepareStatement("SELECT a.KodPredmeta, a.OtsenkaEge, b.sokr,a.Examen FROM zajavlennyeshkolnyeotsenki a, NazvanijaPredmetov b  WHERE  b.KodPredmeta=a.KodPredmeta and KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  AbiturientBean abit_TMP = new AbiturientBean();
                  abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
                  abit_TMP.setEge(rs.getString(2));
                  abit_TMP.setPredmet(rs.getString(3));
                  abit_TMP.setExamen(StringUtil.ntv(rs.getString(4)).trim());
                  abit_A_S1.add(abit_TMP);
                }
                
                att.clear();
                stmt = conn.prepareStatement("SELECT a.KodPredmeta, a.OtsenkaAtt, b.sokr FROM Oa a, NazvanijaPredmetov b  WHERE  b.KodPredmeta=a.KodPredmeta and KodAbiturienta=?");
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
                
                stmt = conn.prepareStatement("SELECT nomerSpravki FROM medSpravka  WHERE KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setMedSpravka(rs.getString(1));
                }
                
                if(abit_A.getMedSpravka()!=null){
                	abit_A.setPodtverjdenieMedSpravki("d");
                }
                
                stmt = conn.prepareStatement("SELECT a.abitEmail, a.address, a.providingSpecialConditions, a.returnDocument FROM abitDopInf a  WHERE KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setAbitEmail(rs.getString(1));
                  abit_A.setDopAddress(rs.getString(2));
                  abit_A.setProvidingSpecialCondition(rs.getString(3));
                  abit_A.setReturnDocument(rs.getString(4));
                }
                
                stmt = conn.prepareStatement("SELECT a.PreemptiveRight FROM PR a  WHERE KodAbiturienta=?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setPreemptiveRight(rs.getString(1));
                }
                
                
                
//System.out.println(">>AMD_2");
// Приоритет 1
                stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND Prioritet LIKE '1' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setS_okso_1(rs.getString(1)+rs.getString(9));

                  if(rs.getString(2) != null) abit_A.setBud_1("on");

                  if(rs.getString(3) != null) abit_A.setDog_1("on");

                  if(rs.getString(4) != null) abit_A.setFito_1("on");

                  if(rs.getString(5) != null) abit_A.setOlimp_1("on");

                  if(rs.getString(6) != null) abit_A.setTarget_1("on");

                  if(rs.getString(7) != null) abit_A.setSix_1("on");

                  if(rs.getString(8) != null) abit_A.setThree_1("on");

                  if(rs.getString(10) != null) abit_A.setDog_ok_1("on");

                  abit_A.setKonkurs_1(StringUtil.ntv(rs.getString(11)));
                }
//System.out.println(">>AMD_3");
// Приоритет 2
                stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND Prioritet LIKE '2' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setS_okso_2(rs.getString(1)+rs.getString(9));

                  if(rs.getString(2) != null) abit_A.setBud_2("on");

                  if(rs.getString(3) != null) abit_A.setDog_2("on");

                  if(rs.getString(4) != null) abit_A.setFito_2("on");

                  if(rs.getString(5) != null) abit_A.setOlimp_2("on");

                  if(rs.getString(6) != null) abit_A.setTarget_2("on");

                  if(rs.getString(7) != null) abit_A.setSix_2("on");

                  if(rs.getString(8) != null) abit_A.setThree_2("on");

                  if(rs.getString(10) != null) abit_A.setDog_ok_2("on");

                  abit_A.setKonkurs_2(StringUtil.ntv(rs.getString(11)));
                }
//System.out.println(">>AMD_4");
// Приоритет 3
                stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND Prioritet LIKE '3' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setS_okso_3(rs.getString(1)+rs.getString(9));

                  if(rs.getString(2) != null) abit_A.setBud_3("on");

                  if(rs.getString(3) != null) abit_A.setDog_3("on");

                  if(rs.getString(4) != null) abit_A.setFito_3("on");

                  if(rs.getString(5) != null) abit_A.setOlimp_3("on");

                  if(rs.getString(6) != null) abit_A.setTarget_3("on");

                  if(rs.getString(7) != null) abit_A.setSix_3("on");

                  if(rs.getString(8) != null) abit_A.setThree_3("on");

                  if(rs.getString(10) != null) abit_A.setDog_ok_3("on");

                  abit_A.setKonkurs_3(StringUtil.ntv(rs.getString(11)));
                }
//System.out.println(">>AMD_5");
// Приоритет 4
                stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND Prioritet LIKE '4' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setS_okso_4(rs.getString(1)+rs.getString(9));

                  if(rs.getString(2) != null) abit_A.setBud_4("on");

                  if(rs.getString(3) != null) abit_A.setDog_4("on");

                  if(rs.getString(4) != null) abit_A.setFito_4("on");

                  if(rs.getString(5) != null) abit_A.setOlimp_4("on");

                  if(rs.getString(6) != null) abit_A.setTarget_4("on");

                  if(rs.getString(7) != null) abit_A.setSix_4("on");

                  if(rs.getString(8) != null) abit_A.setThree_4("on");

                  if(rs.getString(10) != null) abit_A.setDog_ok_4("on");

                  abit_A.setKonkurs_4(StringUtil.ntv(rs.getString(11)));
                }
//System.out.println(">>AMD_6");
// Приоритет 5
                stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND Prioritet LIKE '5' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setS_okso_5(rs.getString(1)+rs.getString(9));

                  if(rs.getString(2) != null) abit_A.setBud_5("on");

                  if(rs.getString(3) != null) abit_A.setDog_5("on");

                  if(rs.getString(4) != null) abit_A.setFito_5("on");

                  if(rs.getString(5) != null) abit_A.setOlimp_5("on");

                  if(rs.getString(6) != null) abit_A.setTarget_5("on");

                  if(rs.getString(7) != null) abit_A.setSix_5("on");

                  if(rs.getString(8) != null) abit_A.setThree_5("on");

                  if(rs.getString(10) != null) abit_A.setDog_ok_5("on");

                  abit_A.setKonkurs_5(StringUtil.ntv(rs.getString(11)));
                }

// Приоритет 6
                stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,kon.Bud,kon.Dog,kon.Fito,kon.Olimp,kon.Target,kon.Six,kon.Three,kon.Forma_Ob,kon.Dog_Ok,kon.Zach FROM Konkurs kon,Spetsialnosti sp WHERE kon.KodSpetsialnosti=sp.KodSpetsialnosti AND Prioritet LIKE '6' AND KodAbiturienta LIKE ?");
                stmt.setObject(1, new Integer(request.getParameter("kodAbiturienta")),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                  abit_A.setS_okso_6(rs.getString(1)+rs.getString(9));

                  if(rs.getString(2) != null) abit_A.setBud_6("on");

                  if(rs.getString(3) != null) abit_A.setDog_6("on");

                  if(rs.getString(4) != null) abit_A.setFito_6("on");

                  if(rs.getString(5) != null) abit_A.setOlimp_6("on");

                  if(rs.getString(6) != null) abit_A.setTarget_6("on");

                  if(rs.getString(7) != null) abit_A.setSix_6("on");

                  if(rs.getString(8) != null) abit_A.setThree_6("on");

                  if(rs.getString(10) != null) abit_A.setDog_ok_6("on");

                  abit_A.setKonkurs_6(StringUtil.ntv(rs.getString(11)));
                }

//System.out.println(">>AMD_8");
            //    form.setAction(us.getClientIntName("md_dl","view_"+abit_A.getKodAbiturienta()));
                form.setAction("md_dl");
            } 


/************************************************************************************************/
/************************************************************************************************/
/******************  МОДИФИКАЦИЯ ДАННЫХ АБИТУРИЕНТА СОГЛАСНО ЛИЧНОЙ КАРТОЧКЕ  *******************/
/************************************************************************************************/
/************************************************************************************************/

  if ( form.getAction().equals("change") && (user.getGroup().getTypeId()==1 || user.getGroup().getTypeId()==3)) {


/************************************************************************************************/
/*************************** Подготовка вспомогательных переменных ******************************/
/************************************************************************************************/

// Начало транзакции
	  
	   env = (Context) new InitialContext().lookup("java:comp/env");

      dataSource = (DataSource) env.lookup(Constants.DATASOURCE_NAME);

      if (dataSource == null) throw new ServletException("'" + Constants.DATASOURCE_NAME + "' is an unknown DataSource");

      conn = dataSource.getConnection();

     conn.setAutoCommit(false);

// Проверка на уникальность по паспортным данным

     stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE SeriaDokumenta LIKE ? AND NomerDokumenta LIKE ?");
     stmt.setObject(1,abit_A.getSeriaDokumenta(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getNomerDokumenta(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Ошибка!");
       mess.setMessage("Заявление абитуриента с указанными паспортными данными уже существует в базе данных!");
       re_enter = true;
     }

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

     stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE SeriaAtt LIKE ? AND NomerAtt LIKE ?");
     stmt.setObject(1,abit_A.getSeriaAtt(),Types.VARCHAR);
     stmt.setObject(2,abit_A.getNomerAtt(),Types.VARCHAR);
     rs = stmt.executeQuery();
     if(rs.next()) {
       mess.setStatus("Ошибка!");
       mess.setMessage("Заявление абитуриента с указанными серией и номером номером аттестата уже существует в БД!");
       re_enter = true;
     }

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

     System.out.println("Определяем приоритетную специальность");
     System.out.println(abit_A.getS_okso_1());
     if(abit_A.getS_okso_1() != null && abit_A.getS_okso_1().length() >= 6) {

       if(abit_A.getS_okso_1().length() == 6 ) {

         Tip_Spec = "о";

       } else {

         if(abit_A.getS_okso_1().length() < 9) {

           Tip_Spec = abit_A.getS_okso_1().substring(6);
           s_okso_1 = abit_A.getS_okso_1().substring(0,6);

         } 
         else  if (abit_A.getS_okso_1().length() > 11)
         {
        	 Tip_Spec = abit_A.getS_okso_1().substring(11);
             s_okso_1 = abit_A.getS_okso_1().substring(0,11);
         }
         
         else {

           Tip_Spec = abit_A.getS_okso_1().substring(8);
           s_okso_1 = abit_A.getS_okso_1().substring(0,8);
         }
         // пушкарев 18062013 пед профили
        
         
       }
       
       System.out.println("SOkso"+s_okso_1+" TIP_SPEC_selected="+Tip_Spec);
       stmt = conn.prepareStatement("SELECT s.KodSpetsialnosti,s.KodFakulteta,s.Abbreviatura,f.ShifrFakulteta FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.ShifrSpetsialnosti LIKE ? AND s.Tip_Spec LIKE ?");
       stmt.setObject(1,s_okso_1,Types.VARCHAR);
       stmt.setObject(2,Tip_Spec,Types.VARCHAR);

       rs = stmt.executeQuery();
       
       if(rs.next()) {
//    	 System.out.println("есть такая специальность");
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

       } else {

// Введенная в карточку приоритетная специальность не найдена в БД

         mess.setStatus("Ошибка!");

         mess.setMessage("Шифр приоритетной специальности ("+abit_A.getS_okso_1()+") указан неверно!");

       //  form.setAction(us.getClientIntName("re_new","error"));
         form.setAction("error");

         re_enter = true;
       }
     }

// Выборка номера личного дела из БД

     stmt = conn.prepareStatement("SELECT DISTINCT Ordr FROM Abiturient WHERE KodVuza LIKE ? ORDER BY Ordr DESC");
     stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
     rs = stmt.executeQuery();
     if(rs.next()) {

// Хотя бы один абитуриент существует в БД

       if(rs.getInt(1) < 10) nomer_ab = "000"+(rs.getInt(1)+1);
       else if((rs.getInt(1) >= 10) && (rs.getInt(1) < 100)) nomer_ab = "00"+(rs.getInt(1)+1);
       else if((rs.getInt(1) >= 100) && (rs.getInt(1) < 1000)) nomer_ab = "0"+(rs.getInt(1)+1);
       else if((rs.getInt(1) >= 1000) && (rs.getInt(1) < 10000)) nomer_ab = ""+(rs.getInt(1)+1);

       ordr_ab = ""+(rs.getInt(1)+1);

     } else {

       nomer_ab = "0000";

       ordr_ab = "0";
     }

     if(!re_enter) {

/**********************************/
/***********   Область   **********/
/**********************************/

       if(abit_A.getNazvanieOblasti()!=null) {
         stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanieOblasti(),Types.VARCHAR);
         stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) kodOblasti = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodOblasti) FROM Oblasti WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kodOblasti = rs.getInt(1) + 1;
             else kodOblasti = 1;

             stmt = conn.prepareStatement("INSERT Oblasti(KodOblasti,NazvanieOblasti,KodVuza) VALUES(?,?,?)");
             stmt.setObject(1, new Integer(""+kodOblasti),Types.INTEGER);
             stmt.setObject(2, abit_A.getNazvanieOblasti(),Types.VARCHAR);
             stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
             stmt.executeUpdate();
         }
       }
       else {
           stmt = conn.prepareStatement("SELECT KodOblasti FROM Oblasti WHERE NazvanieOblasti IS NULL AND KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kodOblasti = rs.getInt(1);
           else {
               stmt = conn.prepareStatement("SELECT MAX(KodOblasti) FROM Oblasti WHERE KodVuza LIKE ?");
               stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
               rs = stmt.executeQuery();
               if(rs.next()) kodOblasti = rs.getInt(1) + 1;
               else kodOblasti = 1;

               stmt = conn.prepareStatement("INSERT Oblasti(KodOblasti,NazvanieOblasti,KodVuza) VALUES(?,?,?)");
               stmt.setObject(1, new Integer(""+kodOblasti),Types.INTEGER);
               stmt.setObject(2, abit_A.getNazvanieOblasti(),Types.VARCHAR);
               stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate();
           }
       }

/**********************************/
/***********    Район    **********/
/**********************************/

       if(abit_A.getNazvanieRajona()!=null) {
         stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE NazvanieRajona LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanieRajona(),Types.VARCHAR);
         stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) kodRajona = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodRajona) FROM Rajony WHERE KodVuza LIKE ?");
             stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kodRajona = rs.getInt(1) + 1;
             else kodRajona = 1;

             stmt = conn.prepareStatement("INSERT Rajony(KodRajona,NazvanieRajona,KodVuza) VALUES(?,?,?)");
             stmt.setObject(1, new Integer(""+kodRajona),Types.INTEGER);
             stmt.setObject(2, abit_A.getNazvanieRajona(),Types.VARCHAR);
             stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
             stmt.executeUpdate();                    
         }
       }
       else {
           stmt = conn.prepareStatement("SELECT KodRajona FROM Rajony WHERE (NazvanieRajona IS NULL OR NazvanieRajona LIKE '') AND KodVuza LIKE ?");
           stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kodRajona = rs.getInt(1);
           else {
               stmt = conn.prepareStatement("SELECT MAX(KodRajona) FROM Rajony WHERE KodVuza LIKE ?");
               stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
               rs = stmt.executeQuery();
               if(rs.next()) kodRajona = rs.getInt(1) + 1;
               else kodRajona = 1;

               stmt = conn.prepareStatement("INSERT Rajony(KodRajona,NazvanieRajona,KodVuza) VALUES(?,?,?)");
               stmt.setObject(1, new Integer(""+kRajona),Types.INTEGER);
               stmt.setObject(2, abit_A.getNazvanieRajona(),Types.VARCHAR);
               stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate();
           }
       }

/**********************************/
/***********    Пункт    **********/
/**********************************/

       if(abit_A.getNazvanie()!=null) {
         stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie LIKE ? AND KodVuza LIKE ?");
         stmt.setObject(1, abit_A.getNazvanie(),Types.VARCHAR);
         stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) kodPunkta = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodPunkta) FROM Punkty WHERE KodVuza LIKE ?");
             stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) kodPunkta = rs.getInt(1) + 1;
             else kodPunkta = 1;

             stmt = conn.prepareStatement("INSERT Punkty(KodPunkta,Nazvanie,KodVuza) VALUES(?,?,?)");
             stmt.setObject(1, new Integer(""+kodPunkta),Types.INTEGER);
             stmt.setObject(2, abit_A.getNazvanie(),Types.VARCHAR);
             stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
             stmt.executeUpdate();
         }
       }
       else {
           stmt = conn.prepareStatement("SELECT KodPunkta FROM Punkty WHERE Nazvanie IS NULL AND KodVuza LIKE ?");
           stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           rs = stmt.executeQuery();
           if(rs.next()) kodPunkta = rs.getInt(1);
           else {
               stmt = conn.prepareStatement("SELECT MAX(KodPunkta) FROM Punkty WHERE KodVuza LIKE ?");
               stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
               rs = stmt.executeQuery();
               if(rs.next()) kodPunkta = rs.getInt(1) + 1;
               else kodPunkta = 1;

               stmt = conn.prepareStatement("INSERT Punkty(KodPunkta,Nazvanie,KodVuza) VALUES(?,?,?)");
               stmt.setObject(1,new Integer(""+kodPunkta),Types.INTEGER);
               stmt.setObject(2, abit_A.getNazvanie(),Types.VARCHAR);
               stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.executeUpdate();
           }
       }

/**********************************/
/***********  Заведение  **********/
/**********************************/

       if(abit_A.getPolnoeNaimenovanieZavedenija() !=null) {
         stmt = conn.prepareStatement("SELECT KodZavedenija FROM Zavedenija WHERE PolnoeNaimenovanieZavedenija LIKE ?");
         stmt.setObject(1, abit_A.getPolnoeNaimenovanieZavedenija(),Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) kZavedenija = rs.getInt(1);
         else {
             stmt = conn.prepareStatement("SELECT MAX(KodZavedenija) FROM Zavedenija WHERE KodVuza LIKE ?");
             stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
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

       stmt = conn.prepareStatement("INSERT Abiturient(KodVuza,DokumentyHranjatsja,KodSpetsialnosti,NomerLichnogoDela,Familija,Imja,Otchestvo,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,MestoRojdenija,TipDokSredObraz,VidDokSredObraz,DataRojdenija,Pol,Grajdanstvo,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,TipOkonchennogoZavedenija,NomerShkoly,KodZavedenija,KodMedali,TrudovajaDejatelnost,NapravlenieOtPredprijatija,KodLgot,KodKursov,InostrannyjJazyk,NujdaetsjaVObschejitii,KodTselevogoPriema,KodOblasti,KodRajona,KodPunkta,SeriaAtt,NomerAtt,Ordr,Gorod_Prop,Ulica_Prop,Dom_Prop,Kvart_Prop,Stepen_Mag,Need_Spo,Exists_st_Mag,Tel,NomerPlatnogoDogovora,KodFormyOb,KodOsnovyOb,DataInput,DataModify,UdostoverenieLgoty,DiplomOtlichija,KodAbiturienta,PostgraduateStudies,Traineeship,Internship) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
       stmt.setObject(1, session.getAttribute("kVuza"), Types.INTEGER);
       stmt.setObject(2, abit_A.getDokumentyHranjatsja(), Types.VARCHAR);
       stmt.setObject(3, kSpec, Types.VARCHAR);

       stmt.setObject(4, Abbr_Spec+nomer_ab+"-1", Types.VARCHAR); // NLD

       stmt.setObject(5, (abit_A.getFamilija()).substring(0,1).toUpperCase()+(abit_A.getFamilija()).substring(1), Types.VARCHAR);
       stmt.setObject(6, (abit_A.getImja()).substring(0,1).toUpperCase()+(abit_A.getImja()).substring(1), Types.VARCHAR);
       stmt.setObject(7, (abit_A.getOtchestvo()).substring(0,1).toUpperCase()+(abit_A.getOtchestvo()).substring(1), Types.VARCHAR);
       stmt.setObject(8, abit_A.getTipDokumenta(), Types.VARCHAR);
       stmt.setObject(9, abit_A.getNomerDokumenta(), Types.VARCHAR);
       stmt.setObject(10, abit_A.getSeriaDokumenta(), Types.VARCHAR);
       stmt.setObject(11, StringUtil.DataConverter(abit_A.getDataVydDokumenta()), Types.VARCHAR);
       stmt.setObject(12, abit_A.getKemVydDokument(), Types.VARCHAR);
       stmt.setObject(13, abit_A.getMestoRojdenija(), Types.VARCHAR);
       stmt.setObject(14, abit_A.getTipDokSredObraz(), Types.VARCHAR);
       stmt.setObject(15, abit_A.getVidDokSredObraz(), Types.VARCHAR);
       stmt.setObject(16, StringUtil.DataConverter(abit_A.getDataRojdenija()), Types.VARCHAR);
       stmt.setObject(17, abit_A.getPol(), Types.VARCHAR);
//       stmt.setObject(18, abit_A.getNomerSertifikata(), Types.VARCHAR);
//       stmt.setObject(19, abit_A.getKopijaSertifikata(), Types.VARCHAR);
       stmt.setObject(18, abit_A.getGrajdanstvo(), Types.VARCHAR);
       stmt.setObject(19, ""+abit_A.getGodOkonchanijaSrObrazovanija(), Types.VARCHAR);
       stmt.setObject(20, abit_A.getGdePoluchilSrObrazovanie(), Types.VARCHAR);
       stmt.setObject(21, abit_A.getTipOkonchennogoZavedenija(), Types.VARCHAR);
       stmt.setObject(22, abit_A.getNomerShkoly(), Types.VARCHAR);
       stmt.setObject(23, new Integer(""+kZavedenija), Types.INTEGER);
       stmt.setObject(24, abit_A.getKodMedali(), Types.INTEGER);
       stmt.setObject(25, abit_A.getTrudovajaDejatelnost(), Types.VARCHAR);
       stmt.setObject(26, abit_A.getNapravlenieOtPredprijatija(), Types.VARCHAR);
       stmt.setObject(27, abit_A.getKodLgot(), Types.INTEGER);
      // stmt.setObject(30, abit_A.getKodKursov(), Types.INTEGER);
       Integer kKurs = 1;
       stmt.setObject(28, kKurs, Types.INTEGER);
       stmt.setObject(29, abit_A.getInostrannyjJazyk(), Types.VARCHAR);
       stmt.setObject(30, abit_A.getNujdaetsjaVObschejitii(), Types.VARCHAR);
       stmt.setObject(31, abit_A.getKodTselevogoPriema(), Types.INTEGER);
       stmt.setObject(32, new Integer(""+kodOblasti), Types.INTEGER);
       stmt.setObject(33, new Integer(""+kodRajona), Types.INTEGER);
       stmt.setObject(34, new Integer(""+kodPunkta), Types.INTEGER);
       stmt.setObject(35, abit_A.getSeriaAtt(), Types.VARCHAR);
       stmt.setObject(36, abit_A.getNomerAtt(), Types.VARCHAR);
       stmt.setObject(37, ordr_ab, Types.VARCHAR);
       stmt.setObject(38, abit_A.getGorod_Prop(), Types.VARCHAR);
       stmt.setObject(39, abit_A.getUlica_Prop(), Types.VARCHAR);
       stmt.setObject(40, abit_A.getDom_Prop(), Types.VARCHAR);
       stmt.setObject(41, abit_A.getKvart_Prop(), Types.VARCHAR);
       stmt.setObject(42, abit_A.getStepen_Mag(), Types.VARCHAR);
       stmt.setObject(43, abit_A.getNeed_Spo(), Types.VARCHAR);
       stmt.setNull(44, Types.VARCHAR); //Exists_st_Mag
       stmt.setObject(45, abit_A.getTel(), Types.VARCHAR);
       stmt.setObject(46, abit_A.getNomerPlatnogoDogovora(), Types.VARCHAR);
       stmt.setObject(47, kFormy_Ob, Types.INTEGER);  //KodFormyOb
       stmt.setObject(48, kOsnovy_Ob, Types.INTEGER); //KodOsnovyOb
       stmt.setObject(49, StringUtil.CurrDate("."), Types.VARCHAR); //DataInput
       stmt.setObject(50, StringUtil.CurrDate("."), Types.VARCHAR); //DataModify = DataInput
       stmt.setObject(51, abit_A.getUdostoverenieLgoty(), Types.VARCHAR);
       stmt.setObject(52, abit_A.getDiplomOtlichija(), Types.VARCHAR);
       stmt.setObject(53, new Integer(""+kAbiturienta), Types.INTEGER);
       stmt.setObject(54, abit_A.getPostgraduateStudies(), Types.VARCHAR);
       stmt.setObject(55, abit_A.getTraineeship(), Types.VARCHAR);
       stmt.setObject(56, abit_A.getInternship(), Types.VARCHAR);
       stmt.executeUpdate();

/* Создание пустых записей в таблице Оценки */
       stmt = conn.prepareStatement("INSERT INTO Otsenki(KodAbiturienta,KodPredmeta) SELECT DISTINCT ?,KodPredmeta FROM NazvanijaPredmetov ORDER BY KodPredmeta");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.executeUpdate();
       
       if(abit_A.getPodtverjdenieMedSpravki().equals("d")){
       
	       stmt = conn.prepareStatement("INSERT INTO medSpravka(kodAbiturienta, nomerSpravki) VALUES(?,?)");
	       stmt.setObject(1,new Integer(kAbiturienta),Types.INTEGER);
	       stmt.setObject(2, form.getMedSpravka(),Types.VARCHAR);
	       stmt.executeUpdate();
       }
       
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
       stmt = conn.prepareStatement("INSERT INTO AbitDopInf(KodAbiturienta, abitEmail, Address, ProvidingSpecialConditions, ReturnDocument) VALUES(?,?,?,?,?)");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.setObject(2,abit_A.getAbitEmail() ,Types.VARCHAR);
       stmt.setObject(3,abit_A.getDopAddress(),Types.VARCHAR);
       stmt.setObject(4,abit_A.getProvidingSpecialCondition(),Types.VARCHAR);
       stmt.setObject(5,abit_A.getReturnDocument(),Types.VARCHAR);
       stmt.executeUpdate();
       
       stmt = conn.prepareStatement("INSERT INTO PR(KodAbiturienta, PreemptiveRight, pr) VALUES(?,?,?)");
       stmt.setObject(1,new Integer(kAbiturienta+""),Types.INTEGER);
       stmt.setObject(2,abit_A.getPreemptiveRight() ,Types.VARCHAR);
       if(abit_A.getPreemptiveRight().equals("д")){
    	   stmt.setObject(3, 1,Types.INTEGER);
       }else{
    	   stmt.setObject(3, 0,Types.INTEGER); 
       }
       stmt.executeUpdate();
       
/* Добавление заявленных оценок, оценок ЕГЭ и оценок аттестата*/
       Enumeration paramNames = request.getParameterNames();

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
          
          if(paramName.indexOf("Attestat") != -1 && paramValue[0].length() != 0) {
        	  	ba=new Integer(paramValue[0]);
        	  	summa=summa+ba;
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
       summa=summa/13;
       System.out.println("summa"+summa);
       stmt = conn.prepareStatement("UPDATE Os SET Summ=? WHERE KodAbiturienta=?");
       stmt.setObject(1, summa,Types.FLOAT);            // Оценка
       stmt.setObject(2, new Integer(""+kAbiturienta),Types.INTEGER);      
       stmt.executeUpdate();
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

   if(abit_A.getS_okso_1() != null) spc1 = abit_A.getS_okso_1();
   if(abit_A.getS_okso_2() != null) spc2 = abit_A.getS_okso_2();
   if(abit_A.getS_okso_3() != null) spc3 = abit_A.getS_okso_3();
   if(abit_A.getS_okso_4() != null) spc4 = abit_A.getS_okso_4();
   if(abit_A.getS_okso_5() != null) spc5 = abit_A.getS_okso_5();
   if(abit_A.getS_okso_6() != null) spc6 = abit_A.getS_okso_6();

   if(spc1.equals(spc2) || spc1.equals(spc3) || spc1.equals(spc4) || spc1.equals(spc5) || spc1.equals(spc6) || spc2.equals(spc3) || 
      spc2.equals(spc4) || spc2.equals(spc5) || spc2.equals(spc6) || spc3.equals(spc4) || spc3.equals(spc5) || spc3.equals(spc6) || 
      spc4.equals(spc5) || spc4.equals(spc6) || spc5.equals(spc6)) {

          mess.setStatus("Ошибка!");
          mess.setMessage("Нельзя указывать в конкурсе две одинаковые специальности!");
          re_enter = true;
   }

/*****************************/
/****** 1. Приоритетная ******/
/*****************************/

       if(abit_A.getS_okso_1() != null && abit_A.getS_okso_1().length() >= 6 && !re_enter) {

// Определение типа специальности
System.out.println("TipSpec=>"+abit_A.getS_okso_1()+" LEN="+abit_A.getS_okso_1().length());
         if(abit_A.getS_okso_1().length() == 6) { 
           Tip_Spec2 = "о";
           s_okso_1 = abit_A.getS_okso_1().substring(0,6);
         } else if(abit_A.getS_okso_1().length() < 9) {
                  Tip_Spec2 = abit_A.getS_okso_1().substring(6);
                  s_okso_1 = abit_A.getS_okso_1().substring(0,6);
                }
         else  if (abit_A.getS_okso_1().length() > 11)
         {
        	 Tip_Spec2 = abit_A.getS_okso_1().substring(11);
             s_okso_1 = abit_A.getS_okso_1().substring(0,11);
         }
         
         else {
               Tip_Spec2 = abit_A.getS_okso_1().substring(8);
               s_okso_1 = abit_A.getS_okso_1().substring(0,8);
         }

// Очных специальностей не должно быть больше 3х

         kSpec = "-1";
         Abbr_Spec2 = "X1Y1Z1";

         if(Tip_Spec2.equals("о") || Tip_Spec2.equals("м") || Tip_Spec2.equals("ф") || Tip_Spec2.equals("ю") || Tip_Spec2.equals("п")) Col_Specs++;
//System.out.println("SOKSO1>"+s_okso_1+"<");
//System.out.println("Tip_Spec1="+StringUtil.toEng(Tip_Spec2));
         stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE ShifrSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
         stmt.setObject(1,s_okso_1,Types.VARCHAR);
         stmt.setObject(2,Tip_Spec2,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) {
           kSpec = rs.getString(1);
           Abbr_Spec2 = rs.getString(2);
           pp=rs.getInt(3);
         } else {

// Шифр специальности не найден в БД
           mess.setStatus("Ошибка!");
           mess.setMessage("Шифр специальности: "+abit_A.getS_okso_1()+" не найден в базе данных!");
           re_enter = true;
         }
System.out.println("SOKSO1>"+s_okso_1+"<");
// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {
System.out.println("1>"+two_t_names[i]+"<kA>"+kAbiturienta+"<kSp>"+kSpec+"<Abbr_Spec>"+Abbr_Spec2+nomer_ab+"-2"+"<TipS>"+Tip_Spec2);
           stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Fito,Olimp,Target,Six,Three,Dog_Ok) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
           stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
           stmt.setObject(2, kSpec, Types.VARCHAR);
           stmt.setObject(3, Abbr_Spec2+nomer_ab+"-1", Types.VARCHAR);
           stmt.setObject(4, "1", Types.VARCHAR);
           stmt.setObject(5, Tip_Spec2, Types.VARCHAR);
           if(abit_A.getBud_1() != null && pp!=0)
             stmt.setObject(6, "д", Types.VARCHAR);
           else
             stmt.setNull(6, Types.VARCHAR);
           if(abit_A.getDog_1() != null)
             stmt.setObject(7, "д", Types.VARCHAR);
           else
             stmt.setNull(7, Types.VARCHAR);
           if(abit_A.getFito_1() != null)
             stmt.setObject(8, "д", Types.VARCHAR);
           else
             stmt.setNull(8, Types.VARCHAR);
           if(abit_A.getOlimp_1() != null)
             stmt.setObject(9, "д", Types.VARCHAR);
           else
             stmt.setNull(9, Types.VARCHAR);
           if(abit_A.getTarget_1() != null)
             stmt.setObject(10, "д", Types.VARCHAR);
           else
             stmt.setNull(10, Types.VARCHAR);
           if(abit_A.getSix_1() != null)
             stmt.setObject(11, "д", Types.VARCHAR);
           else
             stmt.setNull(11, Types.VARCHAR);
           if(abit_A.getThree_1() != null)
             stmt.setObject(12, "д", Types.VARCHAR);
           else
             stmt.setNull(12, Types.VARCHAR);
           if(abit_A.getDog_ok_1() != null)
             stmt.setObject(13, "д", Types.VARCHAR);
           else
             stmt.setNull(13, Types.VARCHAR);

           stmt.executeUpdate();
         }

       }

/*********************/
/****** 2.      ******/
/*********************/

       if(abit_A.getS_okso_2() != null && abit_A.getS_okso_2().length() >= 6 && !re_enter) {

// Определение типа специальности

         if(abit_A.getS_okso_2().length() == 6) { 
           Tip_Spec2 = "о"; 
           s_okso_2 = abit_A.getS_okso_2().substring(0,6);
         } else if(abit_A.getS_okso_2().length() < 9) {
                  Tip_Spec2 = abit_A.getS_okso_2().substring(6);
                  s_okso_2 = abit_A.getS_okso_2().substring(0,6);
                }
         else  if (abit_A.getS_okso_2().length() > 11)
         {
        	 Tip_Spec2 = abit_A.getS_okso_2().substring(11);
             s_okso_2 = abit_A.getS_okso_2().substring(0,11);
         }
         else {
               Tip_Spec2 = abit_A.getS_okso_2().substring(8);
               s_okso_2 = abit_A.getS_okso_2().substring(0,8);
         }

// Очных специальностей не должно быть больше 3х

         kSpec = "-1";
         Abbr_Spec2 = "X1Y1Z1";
         int pp1=0;

         if(Tip_Spec2.equals("о") || Tip_Spec2.equals("м") || Tip_Spec2.equals("ф") || Tip_Spec2.equals("ю") || Tip_Spec2.equals("п")) Col_Specs++;
//System.out.println("SOKSO2>"+s_okso_2+"<");
//System.out.println("Tip_Spec2="+StringUtil.toEng(Tip_Spec2));
         stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE ShifrSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
         stmt.setObject(1,s_okso_2,Types.VARCHAR);
         stmt.setObject(2,Tip_Spec2,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) {
           kSpec = rs.getString(1);
           Abbr_Spec2 = rs.getString(2);
           pp1=rs.getInt(3);
System.out.println(">>>>>>>>>>> Event 1");
         } else {

// Шифр специальности не найден в БД
           mess.setStatus("Ошибка!");
           mess.setMessage("Шифр специальности: "+abit_A.getS_okso_2()+" не найден в базе данных!");
           re_enter = true;
System.out.println(">>>>>>>>>>> Event 2");
         }
System.out.println("SOKSO2>"+s_okso_2+"<");
// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {
System.out.println("2>"+two_t_names[i]+"<kA>"+kAbiturienta+"<kSp>"+kSpec+"<Abbr_Spec>"+Abbr_Spec2+nomer_ab+"-2"+"<TipS>"+Tip_Spec2);
           stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Fito,Olimp,Target,Six,Three,Dog_Ok) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
           stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
           stmt.setObject(2, kSpec, Types.VARCHAR);
           stmt.setObject(3, Abbr_Spec2+nomer_ab+"-2", Types.VARCHAR);
           stmt.setObject(4, "2", Types.VARCHAR);
           stmt.setObject(5, Tip_Spec2, Types.VARCHAR);
           if(abit_A.getBud_2() != null && pp1!=0)
             stmt.setObject(6, "д", Types.VARCHAR);
           else
             stmt.setNull(6, Types.VARCHAR);
           if(abit_A.getDog_2() != null)
             stmt.setObject(7, "д", Types.VARCHAR);
           else
             stmt.setNull(7, Types.VARCHAR);
           if(abit_A.getFito_2() != null)
             stmt.setObject(8, "д", Types.VARCHAR);
           else
             stmt.setNull(8, Types.VARCHAR);
           if(abit_A.getOlimp_2() != null)
             stmt.setObject(9, "д", Types.VARCHAR);
           else
             stmt.setNull(9, Types.VARCHAR);
           if(abit_A.getTarget_2() != null)
             stmt.setObject(10, "д", Types.VARCHAR);
           else
             stmt.setNull(10, Types.VARCHAR);
           if(abit_A.getSix_2() != null)
             stmt.setObject(11, "д", Types.VARCHAR);
           else
             stmt.setNull(11, Types.VARCHAR);
           if(abit_A.getThree_2() != null)
             stmt.setObject(12, "д", Types.VARCHAR);
           else
             stmt.setNull(12, Types.VARCHAR);
           if(abit_A.getDog_ok_2() != null)
             stmt.setObject(13, "д", Types.VARCHAR);
           else
             stmt.setNull(13, Types.VARCHAR);

           stmt.executeUpdate();
         }
       }

/*********************/
/****** 3.      ******/
/*********************/

       if(abit_A.getS_okso_3() != null && abit_A.getS_okso_3().length() >= 6 && !re_enter) {

// Определение типа специальности

         if(abit_A.getS_okso_3().length() == 6) { 
           Tip_Spec2 = "о"; 
           s_okso_3 = abit_A.getS_okso_3().substring(0,6);
         } else if(abit_A.getS_okso_3().length() < 9) {
                  Tip_Spec2 = abit_A.getS_okso_3().substring(6);
                  s_okso_3 = abit_A.getS_okso_3().substring(0,6);
                }
         else  if (abit_A.getS_okso_3().length() > 11)
         {
        	 Tip_Spec2 = abit_A.getS_okso_3().substring(11);
             s_okso_3 = abit_A.getS_okso_3().substring(0,11);
         }
         else {
               Tip_Spec2 = abit_A.getS_okso_3().substring(8);
               s_okso_3 = abit_A.getS_okso_3().substring(0,8);
         }

// Очных специальностей не должно быть больше 3х

         kSpec = "-1";
         Abbr_Spec2 = "X1Y1Z1";
         int pp2=0;
         if(Tip_Spec2.equals("о") || Tip_Spec2.equals("м") || Tip_Spec2.equals("ф") || Tip_Spec2.equals("ю") || Tip_Spec2.equals("п")) Col_Specs++;
//System.out.println("SOKSO3>"+s_okso_3+"<");
//System.out.println("Tip_Spec3="+StringUtil.toEng(Tip_Spec2));
         stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE ShifrSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
         stmt.setObject(1,s_okso_3,Types.VARCHAR);
         stmt.setObject(2,Tip_Spec2,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) {
           kSpec = rs.getString(1);
           Abbr_Spec2 = rs.getString(2);
           pp2=rs.getInt(3);
         } else {

// Шифр специальности не найден в БД
           mess.setStatus("Ошибка!");
           mess.setMessage("Шифр специальности: "+abit_A.getS_okso_3()+" не найден в базе данных!");
           re_enter = true;
         }

// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {
System.out.println("3>"+two_t_names[i]+"<kA>"+kAbiturienta+"<kSp>"+kSpec+"<Abbr_Spec>"+Abbr_Spec2+nomer_ab+"-2"+"<TipS>"+Tip_Spec2);
           stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Fito,Olimp,Target,Six,Three,Dog_Ok) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
           stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
           stmt.setObject(2, kSpec, Types.VARCHAR);
           stmt.setObject(3, Abbr_Spec2+nomer_ab+"-3", Types.VARCHAR);
           stmt.setObject(4, "3", Types.VARCHAR);
           stmt.setObject(5, Tip_Spec2, Types.VARCHAR);
           if(abit_A.getBud_3() != null && pp2!=0)
             stmt.setObject(6, "д", Types.VARCHAR);
           else
             stmt.setNull(6, Types.VARCHAR);
           if(abit_A.getDog_3() != null)
             stmt.setObject(7, "д", Types.VARCHAR);
           else
             stmt.setNull(7, Types.VARCHAR);
           if(abit_A.getFito_3() != null)
             stmt.setObject(8, "д", Types.VARCHAR);
           else
             stmt.setNull(8, Types.VARCHAR);
           if(abit_A.getOlimp_3() != null)
             stmt.setObject(9, "д", Types.VARCHAR);
           else
             stmt.setNull(9, Types.VARCHAR);
           if(abit_A.getTarget_3() != null)
             stmt.setObject(10, "д", Types.VARCHAR);
           else
             stmt.setNull(10, Types.VARCHAR);
           if(abit_A.getSix_3() != null)
             stmt.setObject(11, "д", Types.VARCHAR);
           else
             stmt.setNull(11, Types.VARCHAR);
           if(abit_A.getThree_3() != null)
             stmt.setObject(12, "д", Types.VARCHAR);
           else
             stmt.setNull(12, Types.VARCHAR);
           if(abit_A.getDog_ok_3() != null)
             stmt.setObject(13, "д", Types.VARCHAR);
           else
             stmt.setNull(13, Types.VARCHAR);

           stmt.executeUpdate();
         }
       }

/*********************/
/***** 4.        *****/
/*********************/

       if(abit_A.getS_okso_4() != null && abit_A.getS_okso_4().length() >= 6 && !re_enter) {

// Определение типа специальности

         if(abit_A.getS_okso_4().length() == 6) { 
           Tip_Spec2 = "о"; 
           s_okso_4 = abit_A.getS_okso_4().substring(0,6);
         } else if(abit_A.getS_okso_4().length() < 9) {
                  Tip_Spec2 = abit_A.getS_okso_4().substring(6);
                  s_okso_4 = abit_A.getS_okso_4().substring(0,6);
                }
         else if (abit_A.getS_okso_4().length() > 11)
         {
        	 Tip_Spec2 = abit_A.getS_okso_4().substring(11);
             s_okso_4 = abit_A.getS_okso_4().substring(0,11);
         }
         else {
               Tip_Spec2 = abit_A.getS_okso_4().substring(8);
               s_okso_4 = abit_A.getS_okso_4().substring(0,8);
         }

// Очных специальностей не должно быть больше 3х

         kSpec = "-1";
         Abbr_Spec2 = "X1Y1Z1";
         int pp3=0;

         if(Tip_Spec2.equals("о") || Tip_Spec2.equals("м") || Tip_Spec2.equals("ф") || Tip_Spec2.equals("ю") || Tip_Spec2.equals("п")) Col_Specs++;
//System.out.println("SOKSO4>"+s_okso_4+"<");
//System.out.println("Tip_Spec4="+StringUtil.toEng(Tip_Spec2));
         stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE ShifrSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
         stmt.setObject(1,s_okso_4,Types.VARCHAR);
         stmt.setObject(2,Tip_Spec2,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) {
           kSpec = rs.getString(1);
           Abbr_Spec2 = rs.getString(2);
           pp3=rs.getInt(3);
         } else {

// Шифр специальности не найден в БД
           mess.setStatus("Ошибка!");
           mess.setMessage("Шифр специальности: "+abit_A.getS_okso_4()+" не найден в базе данных!");
           re_enter = true;
         }

// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {

           stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Fito,Olimp,Target,Six,Three,Dog_Ok) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
           stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
           stmt.setObject(2, kSpec, Types.VARCHAR);
           stmt.setObject(3, Abbr_Spec2+nomer_ab+"-4", Types.VARCHAR);
           stmt.setObject(4, "4", Types.VARCHAR);
           stmt.setObject(5, Tip_Spec2, Types.VARCHAR);
           if(abit_A.getBud_4() != null && pp3!=0)
             stmt.setObject(6, "д", Types.VARCHAR);
           else
             stmt.setNull(6, Types.VARCHAR);
           if(abit_A.getDog_4() != null)
             stmt.setObject(7, "д", Types.VARCHAR);
           else
             stmt.setNull(7, Types.VARCHAR);
           if(abit_A.getFito_4() != null)
             stmt.setObject(8, "д", Types.VARCHAR);
           else
             stmt.setNull(8, Types.VARCHAR);
           if(abit_A.getOlimp_4() != null)
             stmt.setObject(9, "д", Types.VARCHAR);
           else
             stmt.setNull(9, Types.VARCHAR);
           if(abit_A.getTarget_4() != null)
             stmt.setObject(10, "д", Types.VARCHAR);
           else
             stmt.setNull(10, Types.VARCHAR);
           if(abit_A.getSix_4() != null)
             stmt.setObject(11, "д", Types.VARCHAR);
           else
             stmt.setNull(11, Types.VARCHAR);
           if(abit_A.getThree_4() != null)
             stmt.setObject(12, "д", Types.VARCHAR);
           else
             stmt.setNull(12, Types.VARCHAR);
           if(abit_A.getDog_ok_4() != null)
             stmt.setObject(13, "д", Types.VARCHAR);
           else
             stmt.setNull(13, Types.VARCHAR);

           stmt.executeUpdate();
         }
       }

/*********************/
/***** 5.        *****/
/*********************/

       if(abit_A.getS_okso_5() != null && abit_A.getS_okso_5().length() >= 6 && !re_enter) {

// Определение типа специальности

         if(abit_A.getS_okso_5().length() == 6) { 
           Tip_Spec2 = "о"; 
           s_okso_5 = abit_A.getS_okso_5().substring(0,6);
         } else if(abit_A.getS_okso_5().length() < 9) {
                  Tip_Spec2 = abit_A.getS_okso_5().substring(6);
                  s_okso_5 = abit_A.getS_okso_5().substring(0,6);
                }
         else if (abit_A.getS_okso_5().length() > 11)
         {
        	 Tip_Spec2 = abit_A.getS_okso_5().substring(11);
             s_okso_5 = abit_A.getS_okso_5().substring(0,11);
         }
         else {
               Tip_Spec2 = abit_A.getS_okso_5().substring(8);
               s_okso_5 = abit_A.getS_okso_5().substring(0,8);
         }

// Очных специальностей не должно быть больше 3х

         kSpec = "-1";
         Abbr_Spec2 = "X1Y1Z1";
         int pp4=0;

         if(Tip_Spec2.equals("о") || Tip_Spec2.equals("м") || Tip_Spec2.equals("ф") || Tip_Spec2.equals("ю") || Tip_Spec2.equals("п")) Col_Specs++;
//System.out.println("SOKSO5>"+s_okso_5+"<");
//System.out.println("Tip_Spec5="+StringUtil.toEng(Tip_Spec2));
         stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE ShifrSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
         stmt.setObject(1,s_okso_5,Types.VARCHAR);
         stmt.setObject(2,Tip_Spec2,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) {
           kSpec = rs.getString(1);
           Abbr_Spec2 = rs.getString(2);
           pp4=rs.getInt(3);
         } else {

// Шифр специальности не найден в БД
           mess.setStatus("Ошибка!");
           mess.setMessage("Шифр специальности: "+abit_A.getS_okso_5()+" не найден в базе данных!");
           re_enter = true;
         }

// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {

           stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Fito,Olimp,Target,Six,Three,Dog_Ok) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
           stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
           stmt.setObject(2, kSpec, Types.VARCHAR);
           stmt.setObject(3, Abbr_Spec2+nomer_ab+"-5", Types.VARCHAR);
           stmt.setObject(4, "5", Types.VARCHAR);
           stmt.setObject(5, Tip_Spec2, Types.VARCHAR);
           if(abit_A.getBud_5() != null && pp4!=0)
             stmt.setObject(6, "д", Types.VARCHAR);
           else
             stmt.setNull(6, Types.VARCHAR);
           if(abit_A.getDog_5() != null)
             stmt.setObject(7, "д", Types.VARCHAR);
           else
             stmt.setNull(7, Types.VARCHAR);
           if(abit_A.getFito_5() != null)
             stmt.setObject(8, "д", Types.VARCHAR);
           else
             stmt.setNull(8, Types.VARCHAR);
           if(abit_A.getOlimp_5() != null)
             stmt.setObject(9, "д", Types.VARCHAR);
           else
             stmt.setNull(9, Types.VARCHAR);
           if(abit_A.getTarget_5() != null)
             stmt.setObject(10, "д", Types.VARCHAR);
           else
             stmt.setNull(10, Types.VARCHAR);
           if(abit_A.getSix_5() != null)
             stmt.setObject(11, "д", Types.VARCHAR);
           else
             stmt.setNull(11, Types.VARCHAR);
           if(abit_A.getThree_5() != null)
             stmt.setObject(12, "д", Types.VARCHAR);
           else
             stmt.setNull(12, Types.VARCHAR);
           if(abit_A.getDog_ok_5() != null)
             stmt.setObject(13, "д", Types.VARCHAR);
           else
             stmt.setNull(13, Types.VARCHAR);

           stmt.executeUpdate();
         }
       }


/*********************/
/***** 6.        *****/
/*********************/

       if(abit_A.getS_okso_6() != null && abit_A.getS_okso_6().length() >= 6 && !re_enter) {

// Определение типа специальности

         if(abit_A.getS_okso_6().length() == 6) { 
           Tip_Spec2 = "о"; 
           s_okso_6 = abit_A.getS_okso_6().substring(0,6);
         } else if(abit_A.getS_okso_6().length() < 9) {
                  Tip_Spec2 = abit_A.getS_okso_6().substring(6);
                  s_okso_6 = abit_A.getS_okso_6().substring(0,6);
                }
         else if (abit_A.getS_okso_6().length() > 11)
         {
        	 Tip_Spec2 = abit_A.getS_okso_6().substring(11);
             s_okso_6 = abit_A.getS_okso_6().substring(0,11);
         }
         else {
               Tip_Spec2 = abit_A.getS_okso_6().substring(8);
               s_okso_6 = abit_A.getS_okso_6().substring(0,8);
         }

// Очных специальностей не должно быть больше 3х

         kSpec = "-1";
         Abbr_Spec2 = "X1Y1Z1";
         int pp5=0;

         if(Tip_Spec2.equals("о") || Tip_Spec2.equals("м") || Tip_Spec2.equals("ф") || Tip_Spec2.equals("ю") || Tip_Spec2.equals("п")) Col_Specs++;
//System.out.println("SOKSO6>"+s_okso_6+"<");
//System.out.println("Tip_Spec6="+StringUtil.toEng(Tip_Spec2));
         stmt = conn.prepareStatement("SELECT KodSpetsialnosti,Abbreviatura,PlanPriema FROM Spetsialnosti WHERE ShifrSpetsialnosti LIKE ? AND Tip_Spec LIKE ?");
         stmt.setObject(1,s_okso_6,Types.VARCHAR);
         stmt.setObject(2,Tip_Spec2,Types.VARCHAR);
         rs = stmt.executeQuery();
         if(rs.next()) {
           kSpec = rs.getString(1);
           Abbr_Spec2 = rs.getString(2);
           pp5=rs.getInt(3);
         } else {

// Шифр специальности не найден в БД
           mess.setStatus("Ошибка!");
           mess.setMessage("Шифр специальности: "+abit_A.getS_okso_6()+" не найден в базе данных!");
           re_enter = true;
         }

// Внесение данных в таблицу Konkurs и дублирующую таблицу Kontrol_Kon

         for(int i=0; i<2; i++) {

           stmt = conn.prepareStatement("INSERT INTO "+two_t_names[i]+"(KodAbiturienta,KodSpetsialnosti,NomerLichnogoDela,Prioritet,Forma_Ob,Bud,Dog,Fito,Olimp,Target,Six,Three,Dog_Ok) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
           stmt.setObject(1, new Integer(""+kAbiturienta), Types.INTEGER);
           stmt.setObject(2, kSpec, Types.VARCHAR);
           stmt.setObject(3, Abbr_Spec2+nomer_ab+"-6", Types.VARCHAR);
           stmt.setObject(4, "6", Types.VARCHAR);
           stmt.setObject(5, Tip_Spec2, Types.VARCHAR);
           if(abit_A.getBud_6() != null && pp5!=0)
             stmt.setObject(6, "д", Types.VARCHAR);
           else
             stmt.setNull(6, Types.VARCHAR);
           if(abit_A.getDog_6() != null)
             stmt.setObject(7, "д", Types.VARCHAR);
           else
             stmt.setNull(7, Types.VARCHAR);
           if(abit_A.getFito_6() != null)
             stmt.setObject(8, "д", Types.VARCHAR);
           else
             stmt.setNull(8, Types.VARCHAR);
           if(abit_A.getOlimp_6() != null)
             stmt.setObject(9, "д", Types.VARCHAR);
           else
             stmt.setNull(9, Types.VARCHAR);
           if(abit_A.getTarget_6() != null)
             stmt.setObject(10, "д", Types.VARCHAR);
           else
             stmt.setNull(10, Types.VARCHAR);
           if(abit_A.getSix_6() != null)
             stmt.setObject(11, "д", Types.VARCHAR);
           else
             stmt.setNull(11, Types.VARCHAR);
           if(abit_A.getThree_6() != null)
             stmt.setObject(12, "д", Types.VARCHAR);
           else
             stmt.setNull(12, Types.VARCHAR);
           if(abit_A.getDog_ok_6() != null)
             stmt.setObject(13, "д", Types.VARCHAR);
           else
             stmt.setNull(13, Types.VARCHAR);

           stmt.executeUpdate();
         }
       }

// Проверка количества очных специальностей (их по правилам приема не больше 3х)

       if(Col_Specs > 3) {
           mess.setStatus("Ошибка!");
           mess.setMessage("Количество специальностей(направлений) очной формы обучения должно быть не более трех!");
           re_enter = true;
       }

       if(!re_enter) {

// Закрепление транзакции

         conn.setAutoCommit(true);

         conn.commit();

         abit_A.setKodAbiturienta(new Integer(""+kAbiturienta));

         abit_A.setFamilija((abit_A.getFamilija()).substring(0,1).toUpperCase()+(abit_A.getFamilija()).substring(1));

         abit_A.setImja((abit_A.getImja()).substring(0,1).toUpperCase()+(abit_A.getImja()).substring(1));

         abit_A.setOtchestvo((abit_A.getOtchestvo()).substring(0,1).toUpperCase()+(abit_A.getOtchestvo()).substring(1));

         //form.setAction(us.getClientIntName("add_success","act-added"));
         form.setAction("add_success");
         mess.setMessage("Импорт прошел успешно");
         
         result = true;

       }

     } //if no re_enter
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

   //   form.setAction(us.getClientIntName("md_dl","re_new"));
      form.setAction("re_new");
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

      stmt = conn.prepareStatement("SELECT NazvanieOblasti FROM Oblasti WHERE KodOblasti LIKE ?");
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
      if(abit_A.getNazvanie()==null) abit_A.setNazvanie("");

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

  

/************************************************************************************************/
/**************************      УДАЛЕНИЕ ЗАПИСИ ОБ АБИТУРИЕНТЕ      ****************************/
/************************************************************************************************/

  if ( form.getAction().equals("delete")  && (user.getGroup().getTypeId()==1) ) {

  //   form.setAction(us.getClientIntName("delete","act_"+abit_A.getKodAbiturienta()));
	  form.setAction("delete");

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

        if(otsenki) return mapping.findForward("ots");
        if(request.getParameter("back_lst")!=null) return mapping.findForward("lst");
        if(error) return mapping.findForward("error");
        if(result) return mapping.findForward("result");
        return mapping.findForward("success");
  }
}                 