package abit.action;

import java.io.IOException;
import java.sql.*;

import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Locale;
import java.util.ArrayList;

import org.apache.struts.action.*;

import javax.naming.*;

import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*;

public class AbitSrchAction extends Action {

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#perform(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        ResultSet         rs            = null;
        ResultSet         rs_fo            = null;
        Statement         stmnt         = null;
        PreparedStatement stmt_a        = null;
        PreparedStatement stmt_fo        = null;
        ResultSet         rs_a          = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        AbiturientForm    form          = (AbiturientForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        boolean           error         = false;
        ActionForward     f             = null;
        String            tmp           = null;
        String            result        = null;
        int               currInd       = 0;
        int               nextInd       = 0;
        int               totalCount    = 0;
        ArrayList         abits_A       = new ArrayList();
        ArrayList         abit_forms    = new ArrayList();
        ArrayList         abit_osnovs   = new ArrayList();
        ArrayList         abit_A_S2     = new ArrayList();
        ArrayList         abit_A_S3     = new ArrayList();
        ArrayList         abit_A_S4     = new ArrayList();
        ArrayList         abit_A_S7     = new ArrayList();
        ArrayList         abit_A_S8     = new ArrayList();
        ArrayList         abit_A_S9     = new ArrayList();
        UserBean          user          = (UserBean)session.getAttribute("user");
        ArrayList         abit_A_Kladr     = new ArrayList();
        String FO = "";
        String OO = "";


        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute("abitSrchAction", new Boolean(true));
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

/**********************************************************************/

            if ( form.getAction() == null ) form.setAction(us.getClientIntName("search","init"));

            if ( form.getAction().equals("search")) {

/********************** Подготовка данных для ввода с помощью селекторов ************************/
            session.removeAttribute("position");

// Сброс признака просмотра результатов поиска после модификации
            session.removeAttribute("resrch");

            session.removeAttribute("DokumentyHranjatsja");
            session.removeAttribute("Special1");
            session.removeAttribute("ShifrFakulteta");
            session.removeAttribute("NomerLichnogoDela");
            session.removeAttribute("Familija");
            session.removeAttribute("Imja");
            session.removeAttribute("Otchestvo");
            session.removeAttribute("KodSpetsialnZach");
            session.removeAttribute("ShifrKursov");
            session.removeAttribute("ShifrMedali");
            session.removeAttribute("ShifrLgot");
            session.removeAttribute("DataRojdenija1");
            session.removeAttribute("DataRojdenija2");
            session.removeAttribute("DataVydDokumenta1");
            session.removeAttribute("DataVydDokumenta2");
            session.removeAttribute("Pol");
            session.removeAttribute("PriznakDog");
            session.removeAttribute("NomerPlatnogoDogovora");
            session.removeAttribute("SrokObuchenija");
            session.removeAttribute("GodOkonchanijaSrObrazovanija1");
            session.removeAttribute("GodOkonchanijaSrObrazovanija2");
            session.removeAttribute("GdePoluchilSrObrazovanie");
            session.removeAttribute("NomerShkoly");
            session.removeAttribute("InostrannyjJazyk");
            session.removeAttribute("NujdaetsjaVObschejitii");
            session.removeAttribute("Grajdanstvo");
            session.removeAttribute("Nazvanie");
            session.removeAttribute("NazvanieRajona");
            session.removeAttribute("NazvanieOblasti");
            session.removeAttribute("PolnoeNaimenovanieZavedenija");
            session.removeAttribute("TipOkonchennogoZavedenija");
            session.removeAttribute("TrudovajaDejatelnost");
            session.removeAttribute("Gruppa");
            session.removeAttribute("NomerAtt");
            session.removeAttribute("SeriaAtt");
            session.removeAttribute("KodFormyOb");
            session.removeAttribute("KodOsnovyOb");
            session.removeAttribute("NapravlenieOtPredprijatija");
            session.removeAttribute("TipDokumenta");
            session.removeAttribute("SeriaDokumenta");
            session.removeAttribute("NomerDokumenta");
            session.removeAttribute("KemVydDokument");
            session.removeAttribute("TipDokSredObraz");
            session.removeAttribute("Sobesedovanie");
            session.removeAttribute("KodTselevogoPriema");
            session.removeAttribute("Ball");
            session.removeAttribute("Prinjat");
            session.removeAttribute("NomerSertifikata");
            session.removeAttribute("KopijaSertifikata");
            session.removeAttribute("UseAllSpecs");
            session.removeAttribute("PreemptiveRight");
            session.removeAttribute("ProvidingSpecialCondition");
            session.removeAttribute("DopAddress");
            session.removeAttribute("ReturnDocument");
            session.removeAttribute("OblastP");
            session.removeAttribute("KodLgot");
            session.removeAttribute("bud1");
            
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


            stmt = conn.prepareStatement("SELECT DISTINCT KodFormyOb,Sokr FROM Forma_Obuch WHERE Sokr NOT LIKE '-' ORDER BY KodFormyOb ASC");
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodFormyOb(new Integer(rs.getInt(1)));
              abit_TMP.setSokr(rs.getString(2));
              abit_forms.add(abit_TMP);
            }
//System.out.println(">>SRCH_1");
            stmt = conn.prepareStatement("SELECT DISTINCT KodOsnovyOb,Sokr FROM Osnova_Obuch WHERE Sokr NOT LIKE '-' ORDER BY KodOsnovyOb ASC");
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodOsnovyOb(new Integer(rs.getInt(1)));
              abit_TMP.setSokr(rs.getString(2));
              abit_osnovs.add(abit_TMP);
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

            stmt = conn.prepareStatement("SELECT DISTINCT ShifrFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setShifrFakulteta(rs.getString(1));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_A_S2.add(abit_TMP);
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
                  abit_A_S3.add(abit_TMP);
                  priznak = true;
                  kPredmeta = "";
              }
              kPredmeta += "%" + rs.getString(2);
            }
            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial1(oldKode + kPredmeta);
            abit_TMP2.setAbbreviatura(oldAbbr);
            abit_A_S3.add(abit_TMP2);
//System.out.println(">>SRCH_2");
            stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Predmet FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY 1 ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
              abit_TMP.setPredmet(rs.getString(2));
              abit_A_S7.add(abit_TMP);
                 }

            tmp="";
            stmt = conn.prepareStatement("SELECT ShifrLgot FROM Lgoty WHERE KodVuza LIKE ? ORDER BY KodLgot ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              tmp += rs.getString(1) +",";
                 }
// В свойстве lgoty будет храниться список сокращений для льгот
            if(tmp != null && tmp.length() != 0) abit_A.setLgoty(tmp.substring(0,tmp.length()-1));

            tmp="";
            stmt = conn.prepareStatement("SELECT ShifrKursov FROM Kursy WHERE KodVuza LIKE ? ORDER BY KodKursov ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              tmp += rs.getString(1) +",";
                 }
// В свойстве InformatsijaOKursah будет храниться список сокращений для курсов
            if(tmp != null && tmp.length() != 0) abit_A.setInformatsijaOKursah(tmp.substring(0,tmp.length()-1));
//System.out.println(">>SRCH_3");
            tmp="";
            stmt = conn.prepareStatement("SELECT ShifrMedali FROM Medali WHERE KodVuza LIKE ? ORDER BY KodMedali ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              tmp += rs.getString(1) +",";
                 }
// В свойстве Medal будет храниться список сокращений для медалей
            if(tmp != null && tmp.length() != 0) abit_A.setMedal(tmp.substring(0,tmp.length()-1));

            tmp="";
            stmt = conn.prepareStatement("SELECT DISTINCT Prinjat FROM Abiturient WHERE KodVuza LIKE ? ORDER BY Prinjat DESC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              tmp += rs.getString(1) +",";
            }
// В свойстве Special13 будет храниться список сокращений для различных признаков зачисления
            if(tmp != null && tmp.length() != 0) abit_A.setSpecial13(tmp.substring(0,tmp.length()-1));

            stmt = conn.prepareStatement("SELECT DISTINCT PolnoeNaimenovanieZavedenija,Sokr,KodZavedenija FROM Zavedenija WHERE KodVuza LIKE ? ORDER BY 3 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setPolnoeNaimenovanieZavedenija(rs.getString(1));
              abit_TMP.setSokr(rs.getString(2));
              abit_A_S8.add(abit_TMP);
                 }
//System.out.println(">>SRCH_4");
            stmt = conn.prepareStatement("SELECT DISTINCT KodTselevogoPriema,ShifrPriema FROM TselevojPriem WHERE KodVuza LIKE ? ORDER BY 1 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodTselevogoPriema(new Integer(rs.getInt(1)));
              abit_TMP.setShifrPriema(rs.getString(2));
              abit_A_S9.add(abit_TMP);
                 }

            } else if ( form.getAction().equals("searching") ) {

/************************************************************************************************/
/**************************************      Поиск      *****************************************/
/************************************************************************************************/
//System.out.println(">>SRCH_5");
                    form.setAction(us.getClientIntName("full","view"));
                    int count = 0;

// Столбец сортировки
                    if(abit_A.getPriznakSortirovki() != null)
                       session.setAttribute("stSort",abit_A.getPriznakSortirovki()+"");
                    else if (session.getAttribute("stSort") == null)
                       session.setAttribute("stSort","familija");

// Тип сортировки ASC/DESC
                    if(abit_A.getSpecial4() != null)
                       session.setAttribute("prSort",abit_A.getSpecial4()+"");


if(abit_A.getDokumentyHranjatsja()!=null && session.getAttribute("resrch")==null) {

   session.removeAttribute("position");

   session.setAttribute("DokumentyHranjatsja",StringUtil.toMySQL((abit_A.getDokumentyHranjatsja()+"")));
   if((abit_A.getSpecial1()).indexOf("%")!= -1)
     session.setAttribute("Special1",StringUtil.toMySQL((""+abit_A.getSpecial1()).substring(0,(abit_A.getSpecial1()).indexOf("%"))));
   else 
   session.setAttribute("Special1",StringUtil.toMySQL(abit_A.getSpecial1()+""));
   session.setAttribute("ShifrFakulteta",StringUtil.toMySQL((abit_A.getShifrFakulteta()+"")));
   session.setAttribute("NomerLichnogoDela",StringUtil.toMySQL((abit_A.getNomerLichnogoDela()+"")));
   session.setAttribute("Familija",StringUtil.toMySQL((abit_A.getFamilija()+"%")));
   session.setAttribute("Imja",StringUtil.toMySQL((abit_A.getImja()+"%")));
   session.setAttribute("Otchestvo",StringUtil.toMySQL((abit_A.getOtchestvo()+"%")));

// Код спец-ти зачисления по ее аббревиатуре
   stmt = conn.prepareStatement("SELECT DISTINCT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND s.Abbreviatura LIKE ?");
   stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
   stmt.setObject(2,abit_A.getSpecial8(),Types.VARCHAR);
   rs = stmt.executeQuery();
   if(rs.next()) {
     session.setAttribute("KodSpetsialnZach",""+StringUtil.toInt(rs.getString(1),-1));
   } else {
     session.setAttribute("KodSpetsialnZach",""+StringUtil.toMySQL((abit_A.getSpecial8()+"")));
   }
//System.out.println(">>SRCH_6");
// Обработка последовательности значений запроса для шифра курсов
   session.setAttribute("ShifrKursov","%");

// Обработка последовательности значений запроса для шифра медали
   session.setAttribute("ShifrMedali",StringUtil.PlaceUnaryComas("ShifrMedali",abit_A.getShifrMedali()));

// Обработка последовательности значений запроса для шифра льгот
   session.setAttribute("ShifrLgot",StringUtil.PlaceUnaryComas("ShifrLgot",abit_A.getShifrLgot()));
   
   session.setAttribute("KodLgot", abit_A.getKodLgot());

// Обработка последовательности значений запроса для признака зачисления
   session.setAttribute("Prinjat",StringUtil.PlaceUnaryComas("Prinjat",abit_A.getPrinjat()));

   session.setAttribute("DataRojdenija1",abit_A.getSpecial2());
   session.setAttribute("DataRojdenija2",abit_A.getSpecial3());

   session.setAttribute("DataVydDokumenta1",abit_A.getSpecial9());
   session.setAttribute("DataVydDokumenta2",abit_A.getSpecial10());

   session.setAttribute("Pol",StringUtil.toMySQL((abit_A.getPol()+"")));

   if((abit_A.getNomerPlatnogoDogovora()+"").equals("*")) {
    if((abit_A.getEge()+"").equals("*")) {
      session.setAttribute("NomerPlatnogoDogovora","NomerPlatnogoDogovora LIKE '%'");
      session.setAttribute("PriznakDog"," OR NomerPlatnogoDogovora IS NULL");
    }
    else if((abit_A.getEge()+"").equals("н")) {
      session.setAttribute("NomerPlatnogoDogovora","");
      session.setAttribute("PriznakDog"," NomerPlatnogoDogovora IS NULL");
    }
    else {
      session.setAttribute("NomerPlatnogoDogovora","");    
      session.setAttribute("PriznakDog"," NomerPlatnogoDogovora IS NOT NULL");
    }
   } else {
      session.setAttribute("PriznakDog","");
      session.setAttribute("NomerPlatnogoDogovora","NomerPlatnogoDogovora LIKE '"+StringUtil.toMySQL((abit_A.getNomerPlatnogoDogovora()+""))+"'");    
     }
   session.setAttribute("SeriaAtt",StringUtil.toMySQL((abit_A.getSeriaAtt()+"")));
   session.setAttribute("NomerAtt",StringUtil.toMySQL((abit_A.getNomerAtt()+"")));
   
   Integer intForma = abit_A.getKodFormyOb();
   String strForma = "-";
   if (intForma == 2) strForma = "'о'";
   else if (intForma ==3) strForma = "'з'";
   else if (intForma ==5) strForma = "'в'";
   else strForma = "'о','з','в'";
   
   Integer intOsnova = abit_A.getKodOsnovyOb();
   String strOsnova = "'б', 'д'";
   if (intOsnova == 2) strOsnova = "'б'";
   else if (intOsnova == 3) strOsnova = "'д'";
   
   
   
   
   session.setAttribute("KodOsnovyOb",strOsnova);
   session.setAttribute("KodFormyOb",strForma);
   session.setAttribute("SrokObuchenija",StringUtil.toMySQL((abit_A.getSrokObuchenija()+"")));
   if((""+abit_A.getSrokObuchenija()).equals("*"))
	     session.setAttribute("SrokObuchenija","%");
	   else
	     session.setAttribute("SrokObuchenija",StringUtil.toMySQL((abit_A.getSrokObuchenija()+"")));
   session.setAttribute("GodOkonchanijaSrObrazovanija1",StringUtil.toMySQL((abit_A.getSpecial5()+"")));
   session.setAttribute("GodOkonchanijaSrObrazovanija2",StringUtil.toMySQL((abit_A.getSpecial6()+"")));
   session.setAttribute("GdePoluchilSrObrazovanie",StringUtil.toMySQL((abit_A.getGdePoluchilSrObrazovanie()+"")));
   session.setAttribute("NomerShkoly",StringUtil.toMySQL((abit_A.getNomerShkoly()+"")));
   session.setAttribute("InostrannyjJazyk",StringUtil.toMySQL((abit_A.getInostrannyjJazyk()+"")));
   session.setAttribute("NujdaetsjaVObschejitii","%");
   session.setAttribute("Grajdanstvo",StringUtil.toMySQL((abit_A.getGrajdanstvo()+"")));
   session.setAttribute("Nazvanie",StringUtil.toMySQL((abit_A.getNazvanie()+"")));
   session.setAttribute("NazvanieRajona",StringUtil.toMySQL((abit_A.getNazvanieRajona()+"")));
   session.setAttribute("NazvanieOblasti",StringUtil.toMySQL((abit_A.getNazvanieOblasti()+"")));
   session.setAttribute("PolnoeNaimenovanieZavedenija",StringUtil.toMySQL((abit_A.getPolnoeNaimenovanieZavedenija()+"")));
   session.setAttribute("TipOkonchennogoZavedenija",StringUtil.toMySQL((abit_A.getTipOkonchennogoZavedenija()+"")));
   session.setAttribute("TrudovajaDejatelnost","%");
   session.setAttribute("Gruppa",StringUtil.toMySQL((abit_A.getGruppa()+"")));
   session.setAttribute("NapravlenieOtPredprijatija",StringUtil.toMySQL((abit_A.getNapravlenieOtPredprijatija()+"")));
   session.setAttribute("TipDokumenta",StringUtil.toMySQL((abit_A.getTipDokumenta()+"")));
   session.setAttribute("SeriaDokumenta",StringUtil.toMySQL((abit_A.getSeriaDokumenta()+"")));
   session.setAttribute("NomerDokumenta",StringUtil.toMySQL((abit_A.getNomerDokumenta()+"")));
   session.setAttribute("KemVydDokument",StringUtil.toMySQL((abit_A.getKemVydDokument()+"")));
   session.setAttribute("TipDokSredObraz",StringUtil.toMySQL((abit_A.getTipDokSredObraz()+"")));
   session.setAttribute("Sobesedovanie","%");
   if((""+abit_A.getKodTselevogoPriema()).equals("0"))
     session.setAttribute("KodTselevogoPriema","%");
   else
     session.setAttribute("KodTselevogoPriema",StringUtil.toMySQL((abit_A.getKodTselevogoPriema()+"")));
   session.setAttribute("Ball",StringUtil.toMySQL((abit_A.getSpecial7()+"")));
   session.setAttribute("NomerSertifikata",StringUtil.toMySQL((abit_A.getAttestat()+"")));
   session.setAttribute("KopijaSertifikata",StringUtil.toMySQL((abit_A.getKopijaSertifikata()+"")));
   session.setAttribute("PreemptiveRight",StringUtil.toMySQL((abit_A.getPreemptiveRight()+"")));/////////////
   session.setAttribute("ProvidingSpecialConditions",StringUtil.toMySQL((abit_A.getProvidingSpecialCondition()+"")));
   session.setAttribute("ReturnDocument",StringUtil.toMySQL((abit_A.getReturnDocument()+"")));
   session.setAttribute("DopAddress",StringUtil.toMySQL((abit_A.getDopAddress()+"")));
   session.setAttribute("UseAllSpecs",StringUtil.toMySQL((abit_A.getUseAllSpecs()+"")));
   
   session.setAttribute("OblastP",abit_A.getNazv_DipSpec());
   session.setAttribute("RajonP",abit_A.getNeed_Spo());
   session.setAttribute("PunktP",abit_A.getGorod_Prop());
   session.setAttribute("VidDokSredObraz",abit_A.getVidDokSredObraz());
   if(abit_A.getBud_1()!=null){
   if(abit_A.getBud_1().equals("on"))
	     session.setAttribute("bud1","on");
   }
   
   /////////////////2204
   if((""+abit_A.getNazv_DipSpec()).equals("-"))
	     session.setAttribute("OblastP","%");
   if((""+abit_A.getNeed_Spo()).equals("-"))
	     session.setAttribute("RajonP","%");
   if((""+abit_A.getGorod_Prop()).equals("-"))
	     session.setAttribute("PunktP","%");
   if((""+abit_A.getVidDokSredObraz()).equals("-"))
	     session.setAttribute("VidDokSredObraz","%");
   if((""+abit_A.getKodLgot()).equals("1"))
	     session.setAttribute("KodLgot","%");
   
   
}
//System.out.println(">>SRCH_7");

/****************************** Выборка абитуриентов из БД ******************************/
                    StringBuffer query = new StringBuffer("SELECT Abiturient.KodAbiturienta,DokumentyHranjatsja,Abbreviatura,");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append("Konkurs.NomerLichnogoDela");
                    else
                      query.append("Abiturient.NomerLichnogoDela");

                    query.append(",Familija,Imja,Otchestvo,NomerPlatnogoDogovora,DataRojdenija,Pol,SrokObuchenija,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,NomerShkoly,InostrannyjJazyk,NujdaetsjaVObschejitii,Grajdanstvo,PolnoeNaimenovanieZavedenija,TipOkonchennogoZavedenija,Abiturient.TrudovajaDejatelnost,Gruppa,NapravlenieOtPredprijatija,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,Abiturient.Sobesedovanie,NomerSertifikata,KopijaSertifikata,Ball,Prinjat,KodSpetsialnZach,SeriaAtt,NomerAtt,Spetsialnosti.KodSpetsialnosti FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy,AbitDopInf");/////

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append(",Konkurs");
                    if(!(""+session.getAttribute("KopijaSertifikata")).equals("%"))
                   	  query.append(",medSpravka");
                    
                    query.append(" WHERE ");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti");//AND PreemptiveRight.KodAbiturienta=Abiturient.KodAbiturienta AND AbitDopInf.KodAbiturienta=Abiturient.KodAbiturienta 
                    else
                      query.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti ");/////////////////////AND PreemptiveRight.KodAbiturienta=Abiturient.KodAbiturienta AND AbitDopInf.KodAbiturienta=Abiturient.KodAbiturienta 

                    if(!(""+session.getAttribute("KopijaSertifikata")).equals("%"))
                 	  query.append("medSpravka.kodAbiturienta = Abiturient.kodAbiturienta AND");
                    
                    query.append(" AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND  Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija  AND Abiturient.KodVuza = ");//общ
                    query.append(session.getAttribute("kVuza"));

//******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

                    StringBuffer condition = new StringBuffer();
                   if(!(""+session.getAttribute("SeriaAtt")).equals("%"))
                    condition.append(" AND (SeriaAtt LIKE "+"'"+session.getAttribute("SeriaAtt")+"' OR SeriaAtt IS NULL)");
                   if(!(""+session.getAttribute("NomerAtt")).equals("%"))
                    condition.append(" AND (NomerAtt LIKE "+"'"+session.getAttribute("NomerAtt")+"' OR NomerAtt IS NULL)");
                  
                   //Аттестат
                   String vidDokCondition = "";
                   
                   if(!(""+session.getAttribute("SrokObuchenija")).equals("%"))
                   {
                	   condition.append(" AND NomerPotoka = '"+session.getAttribute("SrokObuchenija")+"'");
                   }
                   
                   if(!(""+session.getAttribute("kodFormyOb")).equals("%"))
                   {
                	   condition.append(" AND kodFormyOb IN ("+session.getAttribute("KodFormyOb")+")");
                   }
                   
                   if(!(""+session.getAttribute("kodOsnovyOb")).equals("%"))
                   {
                	   condition.append(" AND kodOsnovyOb IN ("+session.getAttribute("KodOsnovyOb")+")");
                   }
                   
                	   
                   
                   if((""+session.getAttribute("bud1")).equals("on"))  
    				   condition.append(" AND AbitDopInf.Dist not in ('-')");
                   
                   
                   if(!(""+session.getAttribute("KodLgot")).equals("%"))  
    				   condition.append(" AND Konkurs.OP LIKE "+"'"+session.getAttribute("KodLgot")+"'");
                   
                   if(!(""+session.getAttribute("KodTselevogoPriema")).equals("%"))  
    				   condition.append(" AND Konkurs.target LIKE "+"'"+session.getAttribute("KodTselevogoPriema")+"'");
                   
                   
                   if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))
                	   vidDokCondition =   " AND viddoksredobraz ='"+session.getAttribute("VidDokSredObraz")+"'";
                   
                   if((""+session.getAttribute("VidDokSredObraz")).equals("Диплом ВО/СПО"))
                	   vidDokCondition = " AND (viddoksredobraz = 'Диплом ВПО' OR viddoksredobraz = 'Диплом СПО')";
                	   
                   if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))  
                				   condition.append(vidDokCondition);
                 
                	   
                	/*   condition.append(" AND (viddoksredobraz = 'диплом ВО/ВПО' OR viddoksredobraz = 'Диплом CПО')");
                   else if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))
                       condition.append(" AND (NomerAtt LIKE "+"'"+session.getAttribute("NomerAtt")+"' OR NomerAtt IS NULL)");*/
                   
                   
                   
                 /*  if(!(""+session.getAttribute("KodOsnovyOb")).equals("0"))
                    condition.append(" AND Abiturient.KodOsnovyOb LIKE "+"'"+session.getAttribute("KodOsnovyOb")+"'");
                   if(!(""+session.getAttribute("KodFormyOb")).equals("0"))
                    condition.append(" AND Abiturient.KodFormyOb LIKE "+"'"+session.getAttribute("KodFormyOb")+"'");*/
                   if(!(""+session.getAttribute("DokumentyHranjatsja")).equals("%"))
                    condition.append(" AND DokumentyHranjatsja LIKE "+"'"+session.getAttribute("DokumentyHranjatsja")+"'");
                   if(!(""+session.getAttribute("ShifrFakulteta")).equals("%"))
                    condition.append(" AND Fakultety.ShifrFakulteta LIKE "+"'"+session.getAttribute("ShifrFakulteta")+"'");
//System.out.println(">>"+session.getAttribute("Special1"));
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
                 /*  if(!(""+session.getAttribute("ShifrKursov")).equals("%"))
                    condition.append(" AND (ShifrKursov IN(" + session.getAttribute("ShifrKursov")+") OR ShifrKursov IS NULL)");
                   if(!(""+session.getAttribute("ShifrMedali")).equals("%"))
                    condition.append(" AND (ShifrMedali IN(" + session.getAttribute("ShifrMedali")+") OR ShifrMedali IS NULL)");
                   if(!(""+session.getAttribute("ShifrLgot")).equals("%"))
                    condition.append(" AND (ShifrLgot IN(" + session.getAttribute("ShifrLgot")+") OR ShifrLgot IS NULL)");*/
                   if(!(""+session.getAttribute("NomerPlatnogoDogovora")).equals("%"))
                    condition.append(" AND (" + session.getAttribute("NomerPlatnogoDogovora")+session.getAttribute("PriznakDog")+")");
                   if(!((""+session.getAttribute("DataRojdenija1")).equals("00-00-0000") && (""+session.getAttribute("DataRojdenija2")).equals("99-99-9999"))) {
                    condition.append(" AND (DataRojdenija >= "+"'"+session.getAttribute("DataRojdenija1")+"'");
                    condition.append(" AND DataRojdenija <= "+"'"+session.getAttribute("DataRojdenija2")+"'"+" OR DataRojdenija IS NULL)");
                   }
                   if(!(""+session.getAttribute("Pol")).equals("%"))
                    condition.append(" AND (Pol LIKE "+"'"+session.getAttribute("Pol")+"'"+" OR Pol IS NULL)");
                  /* if(!(""+session.getAttribute("SrokObuchenija")).equals("%"))
                    condition.append(" AND (SrokObuchenija LIKE "+"'"+session.getAttribute("SrokObuchenija")+"'"+" OR SrokObuchenija IS NULL)");*/
                   if(!((""+session.getAttribute("GodOkonchanijaSrObrazovanija1")).equals("1950") && (""+session.getAttribute("GodOkonchanijaSrObrazovanija2")).equals("9999"))) {
                    condition.append(" AND (GodOkonchanijaSrObrazovanija >= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija1")+"'");
                    condition.append(" AND GodOkonchanijaSrObrazovanija <= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija2")+"'"+" OR GodOkonchanijaSrObrazovanija IS NULL)");
                   }
                /*   if(!(""+session.getAttribute("GdePoluchilSrObrazovanie")).equals("%"))
                    condition.append(" AND (GdePoluchilSrObrazovanie LIKE "+"'"+session.getAttribute("GdePoluchilSrObrazovanie")+"'"+" OR GdePoluchilSrObrazovanie IS NULL)");*/
                   if(!(""+session.getAttribute("NomerShkoly")).equals("%"))
                	   condition.append(" AND NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'");
                   // condition.append(" AND (NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'"+" OR NomerShkoly IS NULL)");
                   if(!(""+session.getAttribute("InostrannyjJazyk")).equals("%"))
                    condition.append(" AND (InostrannyjJazyk LIKE "+"'"+session.getAttribute("InostrannyjJazyk")+"'"+" OR InostrannyjJazyk IS NULL)");
                   if(!(""+session.getAttribute("NujdaetsjaVObschejitii")).equals("%"))
                    condition.append(" AND (NujdaetsjaVObschejitii LIKE "+"'"+session.getAttribute("NujdaetsjaVObschejitii")+"'"+" OR NujdaetsjaVObschejitii IS NULL)");
                   if(!(""+session.getAttribute("Grajdanstvo")).equals("%"))
                    condition.append(" AND (Grajdanstvo LIKE "+"'"+session.getAttribute("Grajdanstvo")+"'"+" OR Grajdanstvo IS NULL)");
                  /* if(!(""+session.getAttribute("Nazvanie")).equals("%"))
                    condition.append(" AND (Punkty.Nazvanie LIKE "+"'"+session.getAttribute("Nazvanie")+"'"+" OR Punkty.Nazvanie IS NULL)");
                   if(!(""+session.getAttribute("NazvanieRajona")).equals("%"))
                    condition.append(" AND (Rajony.NazvanieRajona LIKE "+"'"+session.getAttribute("NazvanieRajona")+"'"+" OR NazvanieRajona IS NULL)");
                   if(!(""+session.getAttribute("NazvanieOblasti")).equals("%"))
                    condition.append(" AND (Oblasti.NazvanieOblasti LIKE "+"'"+session.getAttribute("NazvanieOblasti")+"'"+" OR NazvanieOblasti IS NULL)");*/
                   if(!(""+session.getAttribute("PolnoeNaimenovanieZavedenija")).equals("%"))
                    condition.append(" AND (PolnoeNaimenovanieZavedenija LIKE "+"'"+session.getAttribute("PolnoeNaimenovanieZavedenija")+"'"+" OR PolnoeNaimenovanieZavedenija IS NULL)");
                   if(!(""+session.getAttribute("TipOkonchennogoZavedenija")).equals("%"))
                    condition.append(" AND (TipOkonchennogoZavedenija LIKE "+"'"+session.getAttribute("TipOkonchennogoZavedenija")+"'"+" OR TipOkonchennogoZavedenija IS NULL)");
                   if(!(""+session.getAttribute("TrudovajaDejatelnost")).equals("%"))
                    condition.append(" AND (TrudovajaDejatelnost LIKE "+"'"+session.getAttribute("TrudovajaDejatelnost")+"'"+" OR TrudovajaDejatelnost IS NULL)");
                   if(!(""+session.getAttribute("Gruppa")).equals("%"))
                    condition.append(" AND (Gruppa LIKE "+"'"+session.getAttribute("Gruppa")+"'"+" OR Gruppa IS NULL)");
                  /* if(!(""+session.getAttribute("NapravlenieOtPredprijatija")).equals("%"))
                    condition.append(" AND (NapravlenieOtPredprijatija LIKE "+"'"+session.getAttribute("NapravlenieOtPredprijatija")+"'"+" OR NapravlenieOtPredprijatija IS NULL)");*/
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
                 /*  if(!(""+session.getAttribute("Sobesedovanie")).equals("%"))
                    condition.append(" AND (Abiturient.Sobesedovanie LIKE "+"'"+session.getAttribute("Sobesedovanie")+"'"+" OR Abiturient.Sobesedovanie IS NULL)");
                    condition.append(" AND TselevojPriem.KodTselevogoPriema LIKE "+"'"+session.getAttribute("KodTselevogoPriema")+"'");*/
                   if(!(""+session.getAttribute("NomerSertifikata")).equals("%"))
                    condition.append(" AND (NomerSertifikata LIKE '"+session.getAttribute("NomerSertifikata")+"'"+" OR NomerSertifikata IS NULL)");
                   
//                    condition.append(" AND (KopijaSertifikata LIKE '"+session.getAttribute("KopijaSertifikata")+"'"+" OR KopijaSertifikata IS NULL)");
                   if(!(""+session.getAttribute("Ball")).equals("%"))
                    condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"'"+" OR Ball IS NULL)");
                   if((session.getAttribute("KodSpetsialnZach")+"").equals("%"))
                     condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"' OR KodSpetsialnZach IS NULL)");
                   else
                     condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"')");
                   if(!(""+session.getAttribute("Ball")).equals("%"))
                       condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"'"+" OR Ball IS NULL)");
                   
                  /* if(!(session.getAttribute("PreemptiveRight")+"").equals("%"))
                	   condition.append(" AND PR.PreemptiveRight LIKE "+"'"+session.getAttribute("PreemptiveRight")+"'");*///"+" OR PreemptiveRight.PreemptiveRight IS NULL
                   
              /*     if(!(session.getAttribute("ProvidingSpecialConditions")+"").equals("%"))
                	   condition.append(" AND AbitDopInf.ProvidingSpecialConditions NOT LIKE ''");*///"+" OR AbitDopInf.ProvidingSpecialConditions IS NULL
                   if(!(""+session.getAttribute("Prinjat")).equals("%"))
                    condition.append(" AND (Prinjat IN(" + session.getAttribute("Prinjat")+") OR Prinjat IS NULL)");
                   
                   //Кладр
                   if(!(session.getAttribute("OblastP")+"").equals("%"))
                	   condition.append("AND abiturient.kodOblastiP = "+"'"+session.getAttribute("OblastP")+"'");
                 
                 /*  if(!(session.getAttribute("RajonP")+"").equals("%"))
                	   condition.append("AND abiturient.kodRajonaP = "+"'"+session.getAttribute("RajonP")+"'");
                   if(!(session.getAttribute("PunktP")+"").equals("%"))
                	   condition.append("AND abiturient.Gorod_Prop = "+"'"+session.getAttribute("PunktP")+"'");*/
//************************************************
                    query.append(condition);
                    query.append(" ORDER BY "+ session.getAttribute("stSort")+" "+session.getAttribute("prSort"));
/*************************** Выборка числа абитуриентов из БД ****************************/
                    totalCount = 0;
//System.out.println(">>SRCH_8");
/// For Debugging Purposes Only
///
/// abit_A.setSpecial1(query.toString());
 System.out.println(query);

                    StringBuffer queryTotal = new StringBuffer("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      queryTotal.append(",Konkurs");
                    
                    if(!(""+session.getAttribute("KopijaSertifikata")).equals("%"))
                     	  queryTotal.append(",medSpravka");

                    queryTotal.append(" WHERE ");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      queryTotal.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND ");//AND PreemptiveRight.KodAbiturienta=Abiturient.KodAbiturienta AND AbitDopInf.KodAbiturienta=Abiturient.KodAbiturienta
                    else
                      queryTotal.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND ");//AND PreemptiveRight.KodAbiturienta=Abiturient.KodAbiturienta AND AbitDopInf.KodAbiturienta=Abiturient.KodAbiturienta 
                    
                    if(!(""+session.getAttribute("KopijaSertifikata")).equals("%"))
                   	  queryTotal.append("medSpravka.kodAbiturienta = Abiturient.kodAbiturienta AND");
                    
                    queryTotal.append(" Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza=");
                    queryTotal.append(session.getAttribute("kVuza"));

                    queryTotal.append(condition);

                    System.out.println(queryTotal);
                    
                    stmnt = conn.createStatement();
                    rs = stmnt.executeQuery(queryTotal.toString());

                    if(rs.next()) totalCount=StringUtil.toInt(rs.getString(1),0);

                    abit_A.setSpecial22(new Integer(totalCount));
                    session.setAttribute("total",""+totalCount);

/**************************** Выборка результатов *********************************/
/******************** Используем курсор для выборки элемента **********************/

   int count_ab  = StringUtil.toInt(""+session.getAttribute("total"),0);
   int totalRows = Constants.totalSelectedRows;
   int position  = StringUtil.toInt(""+session.getAttribute("position"),0);
System.out.println(count_ab);
   if(session.getAttribute("resrch")!=null) {

///     session.setAttribute("position",""+(StringUtil.toInt(""+session.getAttribute("position"),1)-totalRows));
     rs = stmnt.executeQuery("SELECT CURSOR_STATUS('global','sel_cursor')");
     if(rs.next()) if(rs.getInt(1) >=0) {
       stmnt = conn.createStatement();
       stmnt.executeUpdate("CLOSE sel_cursor;");
       stmnt = conn.createStatement();
       stmnt.executeUpdate("DEALLOCATE sel_cursor;");
     }
       stmnt = conn.createStatement();

       stmnt.executeUpdate("DECLARE sel_cursor CURSOR GLOBAL SCROLL READ_ONLY FOR "+query.toString()+";");

       stmnt.executeUpdate("OPEN sel_cursor;");
       stmnt = conn.createStatement();
   }
System.out.println(">>SRCH_10");
// Начало просмотра
         if(session.getAttribute("position") == null) { 

           session.setAttribute("position","0");

// Удаляем курсор, если он существует и освобождаем память
           stmnt = conn.createStatement();
           rs = stmnt.executeQuery("SELECT CURSOR_STATUS('global','sel_cursor')");
           if(rs.next()) if(rs.getInt(1) >=0) {
             stmnt = conn.createStatement();
             stmnt.executeUpdate("CLOSE sel_cursor;");
             stmnt = conn.createStatement();
             stmnt.executeUpdate("DEALLOCATE sel_cursor;");
           }
           stmnt = conn.createStatement();
           stmnt.executeUpdate("DECLARE sel_cursor CURSOR GLOBAL SCROLL READ_ONLY FOR "+query.toString()+"; OPEN sel_cursor;");
         }
         else {

           if(request.getParameter("prev")!=null) {

              if(( position - totalRows ) >= totalRows) {

                   position -= totalRows;

              }
              else { 

                   position = 0;

              }
           }
           else if( request.getParameter("next")!=null && count_ab != 0 && ((position + totalRows) != count_ab) ) {


             if(( position + totalRows ) > count_ab && (count_ab - totalRows) > 0){

                 position -= totalRows;

             } else if(( count_ab - totalRows ) > 0 ){

                 position += totalRows;
             }

           }
           else if(request.getParameter("beg")!=null) {

                position = 0;
           }
           else if(request.getParameter("end")!=null && count_ab != 0 && (count_ab-totalRows) > 0) {

                position = count_ab - totalRows;
           }

         }

         session.setAttribute("position",""+position);

/************************/

// Начало выборки
   stmt = conn.prepareStatement("FETCH ABSOLUTE ? FROM sel_cursor");
   stmt.setObject(1,new Integer(""+session.getAttribute("position")),Types.INTEGER);
   stmt.executeQuery();

   for(int row=1; row <= totalRows; row++) {

   stmnt = conn.createStatement();
   rs = stmnt.executeQuery("FETCH NEXT FROM sel_cursor");
   if(rs.next()) {
     AbiturientBean abit_TMP = new AbiturientBean();
     abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
     abit_TMP.setNumber(""+(StringUtil.toInt(""+session.getAttribute("position"),0)+row));
     abit_TMP.setDokumentyHranjatsja(rs.getString(2));
     abit_TMP.setSpecial1(rs.getString(3));// AbbreviaturaSpetsialnosti
     abit_TMP.setNomerLichnogoDela(rs.getString(4));
     abit_TMP.setFamilija(rs.getString(5));
     abit_TMP.setImja(rs.getString(6));
     abit_TMP.setOtchestvo(rs.getString(7));
   //  abit_TMP.setShifrKursov(rs.getString(8));
   //  abit_TMP.setShifrMedali(rs.getString(9));
   //  abit_TMP.setShifrLgot(rs.getString(10));
     abit_TMP.setNomerPlatnogoDogovora(rs.getString(8));
     abit_TMP.setDataRojdenija(rs.getString(9));
     abit_TMP.setPol(rs.getString(10));
     abit_TMP.setSrokObuchenija(rs.getString(11));
     abit_TMP.setGodOkonchanijaSrObrazovanija(new Integer(rs.getString(12)));
  //   abit_TMP.setGdePoluchilSrObrazovanie(rs.getString(16));
     abit_TMP.setNomerShkoly(rs.getString(14));
     abit_TMP.setInostrannyjJazyk(rs.getString(15));
     abit_TMP.setNujdaetsjaVObschejitii(rs.getString(16));
     abit_TMP.setGrajdanstvo(rs.getString(17));
 //    abit_TMP.setNazvanie(rs.getString(21));
 //    abit_TMP.setNazvanieRajona(rs.getString(22));
 //    abit_TMP.setNazvanieOblasti(rs.getString(23));
     abit_TMP.setPolnoeNaimenovanieZavedenija(rs.getString(18));
     abit_TMP.setTipOkonchennogoZavedenija(rs.getString(19));
     abit_TMP.setTrudovajaDejatelnost(rs.getString(20));
     abit_TMP.setGruppa(rs.getString(21));
     abit_TMP.setNapravlenieOtPredprijatija(rs.getString(22));
     abit_TMP.setTipDokumenta(rs.getString(23));
     abit_TMP.setNomerDokumenta(rs.getString(24));
     abit_TMP.setSeriaDokumenta(rs.getString(25));
     abit_TMP.setDataVydDokumenta(rs.getString(26));
     abit_TMP.setKemVydDokument(rs.getString(27));
     abit_TMP.setTipDokSredObraz(rs.getString(28));
     abit_TMP.setSobesedovanie(rs.getString(29));
     abit_TMP.setShifrPriema(rs.getString(30));
     abit_TMP.setNomerSertifikata(rs.getString(31));
     abit_TMP.setKopijaSertifikata(rs.getString(32));
   //  abit_TMP.setBall(new Integer(rs.getInt(33)));
     abit_TMP.setPrinjat(rs.getString(33));
     
    // abit_TMP.setFormaOb(rs.getString(34));
    // abit_TMP.setOsnovaOb(rs.getString(35));  ФОРМА И ОСНОВА ОБ 2015 new
     stmt_fo = conn.prepareStatement("SELECT DISTINCT FOrma_ob from konkurs where kodAbiturienta = ?");
     stmt_fo.setObject(1, new Integer(rs.getInt(1)),Types.INTEGER);
     rs_fo = stmt_fo.executeQuery();
     int fi = 0;
    while(rs_fo.next()){
    	 fi++;
    	 FO = rs_fo.getString(1);
    	 //rs_fo.last();
    	 if (fi>1) FO = "o, з";
     }

    
     
     abit_TMP.setFormaOb(FO);
     
     stmt_fo = conn.prepareStatement("SELECT DISTINCT bud from konkurs where kodAbiturienta = ? and bud = 'д'");
     stmt_fo.setObject(1, new Integer(rs.getInt(1)),Types.INTEGER);
     rs_fo = stmt_fo.executeQuery();
    String BudDog = "не указано";
    fi = 0;
    if(rs_fo.next()){
    	fi ++;
    	 BudDog = "бюд";
     }
    
    stmt_fo = conn.prepareStatement("SELECT DISTINCT dog from konkurs where kodAbiturienta = ? and dog = 'д'");
    stmt_fo.setObject(1, new Integer(rs.getInt(1)),Types.INTEGER);
    rs_fo = stmt_fo.executeQuery();
   
   
   if(rs_fo.next()){
   	fi ++;
   	 BudDog = "дог";
    }
   
   if (fi==2) BudDog = "б, д";

    
     
     abit_TMP.setFormaOb(FO);
     
     
    abit_TMP.setOsnovaOb(BudDog);
     abit_TMP.setSeriaAtt(rs.getString(36));
      abit_TMP.setNomerAtt(rs.getString(37));
     abit_TMP.setPreemptiveRight(rs.getString(38));
    // abit_TMP.setProvidingSpecialCondition(rs.getString(39));  
  //    abit_TMP.setDopAddress(rs.getString(40));
    //  abit_TMP.setReturnDocument(rs.getString(41));
// Получение Аббревиатуры спец-ти по ее коду
   stmt_a = conn.prepareStatement("SELECT DISTINCT s.Abbreviatura FROM Abiturient a, Spetsialnosti s WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND a.KodVuza LIKE ? AND a.KodSpetsialnZach LIKE ?");
   stmt_a.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);

// KodSpetsialnosti (Зачисления)
   stmt_a.setObject(2,rs.getString(34),Types.INTEGER);
   rs_a = stmt_a.executeQuery();
   if(rs_a.next())    abit_TMP.setSpecial8(rs_a.getString(1));
   else               abit_TMP.setSpecial8("");

                      abits_A.add(abit_TMP);
 } else {

// Прекращаем цикл выборки, если данных больше нет

          break;
   }

 }
/* 
// Увеличиваем позицию на значение скроллинга = totalRows
    session.setAttribute("position",""+(StringUtil.toInt(""+session.getAttribute("position"),0)+totalRows));
*/
    stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE Abiturient.KodVuza LIKE ?");
    stmt.setObject(1,session.getAttribute("kVuza"));
    rs = stmt.executeQuery();
    if(rs.next()) abit_A.setPriznakSortirovki(rs.getString(1));
    session.setAttribute("total_All",""+totalCount); 
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
        request.setAttribute("abit_A", abit_A);
        request.setAttribute("abits_A", abits_A);
        request.setAttribute("abit_forms", abit_forms);
        request.setAttribute("abit_osnovs", abit_osnovs);
        request.setAttribute("abit_A_S2", abit_A_S2);
        request.setAttribute("abit_A_S3", abit_A_S3);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S7", abit_A_S7);
        request.setAttribute("abit_A_S8", abit_A_S8);
        request.setAttribute("abit_A_S9", abit_A_S9);
        request.setAttribute("abit_A_Kladr", abit_A_Kladr);
   }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}