package abit.action;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.sql.*;
import javax.naming.*;
import abit.bean.*;
import abit.Constants;
import abit.sql.*; 

public class SpetsialnostiAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session               = request.getSession();
        Connection              conn                  = null;
        PreparedStatement       stmt                  = null;
        ResultSet               rs                    = null;
        PreparedStatement       stmt1                  = null;
        ResultSet               rs1                    = null;
        ActionErrors            errors                = new ActionErrors();
        SpetsialnostiForm       form                  = (SpetsialnostiForm) actionForm;
        AbiturientBean          abit_Spec             = form.getBean(request, errors);
        ActionError             msg                   = null;
        boolean                 spetsialnosti_f       = false;
        boolean                 error                 = false;
        ActionForward           f                     = null;
        int                     kSp                   = 1;
        ArrayList               abits_Spec            = new ArrayList();
        ArrayList               abit_Spec_S1          = new ArrayList();
        ArrayList               abit_Spec_S2          = new ArrayList();
        ArrayList               abit_Spec_S3          = new ArrayList();
        ArrayList               abit_Spec_S4          = new ArrayList();
        ArrayList               abit_Spec_S5          = new ArrayList();
        ArrayList               abit_Spec_S6          = new ArrayList();
        ArrayList               abit_Spec_KonGrp      = new ArrayList();
        UserBean                user                  = (UserBean)session.getAttribute("user");
        
        

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "spetsialnostiAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "spetsialnostiForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************** Подготовка данных для ввода с помощью селектора *************************/

                 stmt = conn.prepareStatement("SELECT DISTINCT KodKonGruppy,Nazvanie FROM KonGruppa WHERE KodVuza LIKE ? ORDER BY 1 ASC");
                 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setKodKonGrp(new Integer(rs.getString(1)));
                   abit_TMP.setNazvanie( rs.getString(2));
                   abit_Spec_KonGrp.add(abit_TMP);
                 }

                 stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta,ShifrFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
                 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setSpecial1( rs.getString(1) + "%" + rs.getString(3));
                   abit_TMP.setAbbreviaturaFakulteta( rs.getString(2));
                   abit_Spec_S1.add(abit_TMP);
                 }

                 stmt = conn.prepareStatement("SELECT DISTINCT Predmet,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ?");
                 stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setPredmet( rs.getString(1) );
                   abit_TMP.setKodPredmeta( new Integer(rs.getString(2)));
                   abit_Spec_S4.add(abit_TMP);
                 }
                 
                 
                 //08.12.2014 добавление справочника фис Пушкарев
                 
                 stmt = conn.prepareStatement("select id, code, name from DictionaryCode10");
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodSpetsialnosti( new Integer(rs.getString(1)) );
                     abit_TMP.setSpecial1(rs.getString(2) );
                     abit_TMP.setSpecial2(rs.getString(3) );
                     String eduLevelFromShifr = rs.getString(2).substring(3, 5);
                     
                     if (eduLevelFromShifr.equals("02"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "п" + "  " + rs.getString(3));       //спо
                     if (eduLevelFromShifr.equals("03"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "б" + "  " + rs.getString(3));       //бакалавры
                     if (eduLevelFromShifr.equals("04"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "м" + "  " + rs.getString(3) ); 		 //специалисты
                     if (eduLevelFromShifr.equals("05"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "с" + "  " + rs.getString(3) );	  //магистратура
                     if (eduLevelFromShifr.equals("06"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "а" + "  " + rs.getString(3) );	  //аспирантура
                     if (eduLevelFromShifr.equals("07"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "х" + "  " + rs.getString(3) );	   //
                     if (eduLevelFromShifr.equals("08"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "о" + "  " + rs.getString(3) );	   //ординатура
                     if (eduLevelFromShifr.equals("09"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "и" + "  " + rs.getString(3) );		//инт
                     
                     
                 
                       abit_Spec_S5.add(abit_TMP);
                   }
                 
                 stmt = conn.prepareStatement("select socr, name from EduLevel");
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setSpecial1(rs.getString(1) );
                     abit_TMP.setSpecial2(rs.getString(2) );
                     abit_Spec_S6.add(abit_TMP);
                   }
                 
/************************************************************************************************/

            if ( form.getAction() == null ) {

                 form.setAction(us.getClientIntName("new","init"));

            } else if ( form.getAction().equals("create") || form.getAction().equals("full") ) {

/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

                if ( request.getParameter("full") == null &&
                             form.getAction().equals("create") ) {

                    stmt = conn.prepareStatement("SELECT MAX(KodSpetsialnosti) FROM Spetsialnosti");
                    rs = stmt.executeQuery();
                    if(rs.next()) kSp = rs.getInt(1)+1;
                     else kSp = 2;

                    stmt = conn.prepareStatement("INSERT Spetsialnosti(KodSpetsialnosti,KodFakulteta,NazvanieSpetsialnosti,Abbreviatura,ShifrSpetsialnosti,ShifrSpetsialnostiOKSO,PlanPriema,Sobesedovanie,KodPredmeta,JekzamenZachet,PoluProhodnoiBall,ProhodnoiBallNaSpetsialnosti,KodKonGruppy,Tip_Spec,TselPr_PGU,TselPr_ROS,TselPr_1,TselPr_2,TselPr_3, PlanPriemaLg, PlanPriemaDog, eduLevel, fisId,krim_obshee,krim_ok,krim_cp,planpriemaig,ppdoglgot,ppkrimdog,ppkrimdoglgot) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    stmt.setObject(1, new Integer(""+kSp),Types.INTEGER);
                    stmt.setObject(2, new Integer(abit_Spec.getSpecial1().substring(0,abit_Spec.getSpecial1().indexOf("%"))),Types.INTEGER);
                    stmt.setObject(3, abit_Spec.getNazvanieSpetsialnosti(),Types.VARCHAR);
                    stmt.setObject(4, abit_Spec.getAbbreviatura(),Types.VARCHAR);
                    stmt.setObject(5, abit_Spec.getShifrSpetsialnosti(),Types.VARCHAR);
                    stmt.setObject(6, abit_Spec.getShifrSpetsialnostiOKSO(),Types.VARCHAR);
                    stmt.setObject(7, abit_Spec.getPlanPriema(),Types.INTEGER);
                    stmt.setObject(8, abit_Spec.getSobesedovanie(),Types.VARCHAR);
                    if(abit_Spec.getKodPredmeta()!= null) stmt.setObject(9, abit_Spec.getKodPredmeta(),Types.INTEGER);
                    else stmt.setObject(9, new Integer("0"),Types.INTEGER);
                    stmt.setObject(10, ""+abit_Spec.getJekzamenZachet(),Types.VARCHAR);
                    stmt.setObject(11, abit_Spec.getPoluProhodnoiBall(),Types.INTEGER);
                    stmt.setObject(12, abit_Spec.getProhodnoiBallNaSpetsialnosti(),Types.INTEGER);
                    stmt.setObject(13, abit_Spec.getKodKonGrp(),Types.INTEGER);
                    stmt.setObject(14, abit_Spec.getTip_Spec(),Types.VARCHAR);
                    stmt.setObject(15, abit_Spec.getPlanPriemaT1(),Types.INTEGER);
                    stmt.setObject(16, abit_Spec.getPlanPriemaT2(),Types.INTEGER);
                    stmt.setObject(17, abit_Spec.getPlanPriemaT3(),Types.INTEGER);
                    stmt.setObject(18, abit_Spec.getPlanPriemaT4(),Types.INTEGER);
                    stmt.setObject(19, abit_Spec.getPlanPriemaT5(),Types.INTEGER);
                    stmt.setObject(20, abit_Spec.getPlanPriemaLg(),Types.INTEGER);
                    stmt.setObject(21, abit_Spec.getPlanPriemaDog(),Types.INTEGER);
                  
                    
                 
                    String eduLevelFromShifr = abit_Spec.getShifrSpetsialnosti().substring(3, 5);
                    if (eduLevelFromShifr.equals("03"))  stmt.setObject(22, "б");       //бакалавры
                    if (eduLevelFromShifr.equals("02"))  stmt.setObject(22, "п");		//спо
                    if (eduLevelFromShifr.equals("05"))  stmt.setObject(22, "с"); 		 //специалисты
                    if (eduLevelFromShifr.equals("04"))  stmt.setObject(22, "м");		//Магистры
                    if (eduLevelFromShifr.equals("06"))  stmt.setObject(22, "а");		//Магистры
                    if (eduLevelFromShifr.equals("08"))  stmt.setObject(22, "о");		//Магистры
                    if (eduLevelFromShifr.equals("09"))  stmt.setObject(22, "и");		//Магистры
                   
                    stmt.setObject(23, abit_Spec.getFisId());
                   // stmt.setObject(24, abit_Spec.getIdNews(),Types.INTEGER);
                    stmt.setObject(24, abit_Spec.getKrimobshee(),Types.INTEGER);
                    stmt.setObject(25, abit_Spec.getKrimok(),Types.INTEGER);
                    stmt.setObject(26, abit_Spec.getKrimcp(),Types.INTEGER);
                    stmt.setObject(27, abit_Spec.getPlanPriemaIG(),Types.INTEGER);
                    stmt.setObject(28, abit_Spec.getPpDogLgot(),Types.INTEGER);     
                    stmt.setObject(29, abit_Spec.getPpKrimDog(),Types.INTEGER);    
                    stmt.setObject(30, abit_Spec.getPpKrimDogLgot(),Types.INTEGER);                       
                    stmt.executeUpdate();
                    
                 /*   stmt = conn.prepareStatement("INSERT specFisId(KodSpetsialnosti, eduLevel, fisId) VALUES (?,?,?)");
                    stmt.setObject(1, new Integer(""+kSp),Types.INTEGER);
                    String eduLevelFromShifr = abit_Spec.getShifrSpetsialnosti().substring(3, 5);
                    if (eduLevelFromShifr.equals("03"))  stmt.setObject(2, "б");       //бакалавры
                    if (eduLevelFromShifr.equals("02"))  stmt.setObject(2, "д");		//дневное отделение
                    if (eduLevelFromShifr.equals("04"))  stmt.setObject(2, "с"); 		 //специалисты
                    if (eduLevelFromShifr.equals("05"))  stmt.setObject(2, "м");		//Магистры
                   
                    stmt.setObject(3, abit_Spec.getFisId());
                    stmt.executeUpdate();*/
                    
                    form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));

                   if ( request.getParameter("stolbetsSortirovki") != null ) {
                      session.setAttribute("special1", request.getParameter("special1"));
                      session.setAttribute("stolbetsSortirovki", request.getParameter("stolbetsSortirovki"));
                      session.setAttribute("priznakSortirovki", request.getParameter("priznakSortirovki"));
                   }

                 String query = new String("SELECT NazvanieSpetsialnosti,Abbreviatura,ShifrSpetsialnosti,shifrSpetsialnostiOKSO,PlanPriema,Sobesedovanie,Predmet,JekzamenZachet,PoluProhodnoiBall,ProhodnoiBallNaSpetsialnosti,KodSpetsialnosti,KonGruppa.Nazvanie,Tip_Spec,TselPr_PGU,TselPr_ROS,TselPr_1,TselPr_2,TselPr_3,PlanPriemaLg,PlanPriemaDog,krim_obshee,krim_ok,krim_cp,planPriemaIG,ppdoglgot,ppkrimdog,ppkrimdoglgot  FROM NazvanijaPredmetov,Spetsialnosti,Fakultety,KonGruppa WHERE KonGruppa.KodKonGruppy=Spetsialnosti.KodKonGruppy AND NazvanijaPredmetov.KodPredmeta=Spetsialnosti.KodPredmeta AND Spetsialnosti.KodFakulteta LIKE Fakultety.KodFakulteta AND Fakultety.KodVuza LIKE ");

                   query += session.getAttribute("kVuza");

                   query += " AND Spetsialnosti.KodFakulteta LIKE ";

                   query += (""+session.getAttribute("special1")).substring(0,(session.getAttribute("special1")+"").indexOf("%"));

                   query += " ORDER BY ";

                   query += session.getAttribute("stolbetsSortirovki");

                   if ( session.getAttribute("priznakSortirovki").toString().length() == 14) 
                      query += " ASC";
                   else
                      query += " DESC";

                   stmt = conn.prepareStatement(query);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setNazvanieSpetsialnosti( rs.getString(1) );
                     abit_TMP.setAbbreviatura( rs.getString(2) );
                     abit_TMP.setShifrSpetsialnosti( rs.getString(3) );
                     abit_TMP.setShifrSpetsialnostiOKSO( rs.getString(4) );
                     abit_TMP.setPlanPriema( new Integer(rs.getInt(5)) );
                     abit_TMP.setSobesedovanie( rs.getString(6) );
                     abit_TMP.setPredmet(rs.getString(7) );
                     abit_TMP.setJekzamenZachet( rs.getString(8) );
                     abit_TMP.setPoluProhodnoiBall( new Integer( rs.getInt(9)) );
                     abit_TMP.setProhodnoiBallNaSpetsialnosti( new Integer( rs.getInt(10)) );
                     abit_TMP.setKodSpetsialnosti( new Integer(rs.getInt(11)) );
                     abit_TMP.setNazvanie( rs.getString(12) );
                     abit_TMP.setTip_Spec( rs.getString(13) );
                     abit_TMP.setPlanPriemaT1( new Integer(rs.getInt(14)) );
                     abit_TMP.setPlanPriemaT2( new Integer(rs.getInt(15)) );
                     abit_TMP.setPlanPriemaT3( new Integer(rs.getInt(16)) );
                     abit_TMP.setPlanPriemaT4( new Integer(rs.getInt(17)) );
                     abit_TMP.setPlanPriemaT5( new Integer(rs.getInt(18)) );
                     abit_TMP.setPlanPriemaLg( new Integer(rs.getInt(19)) );
                     abit_TMP.setPlanPriemaDog( new Integer(rs.getInt(20)) );
                     abit_TMP.setKrimobshee( new Integer(rs.getInt(21)) );
                     abit_TMP.setKrimok( new Integer(rs.getInt(22)) );
                     abit_TMP.setKrimcp( new Integer(rs.getInt(23)) );
                     abit_TMP.setPlanPriemaIG( new Integer(rs.getInt(24)) );
                     abit_TMP.setPpDogLgot( new Integer(rs.getInt(25)) );
                     abit_TMP.setPpKrimDog( new Integer(rs.getInt(26)) );
                     abit_TMP.setPpKrimDogLgot( new Integer(rs.getInt(27)) );                 
                     abits_Spec.add(abit_TMP);
                   }

                   stmt = conn.prepareStatement("SELECT DISTINCT Fakultet FROM Fakultety WHERE KodFakulteta LIKE " + (""+session.getAttribute("special1")).substring(0,(session.getAttribute("special1")+"").indexOf("%")));
                   rs = stmt.executeQuery();

                   if (rs.next()) {
                      abit_Spec.setFakultet((rs.getString(1)).toUpperCase());
                   }
                 }
/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta,NazvanieSpetsialnosti,Abbreviatura,ShifrSpetsialnosti,ShifrSpetsialnostiOKSO,PlanPriema,Sobesedovanie,KodPredmeta,JekzamenZachet,PoluProhodnoiBall,ProhodnoiBallNaSpetsialnosti,Fakultety.KodFakulteta,ShifrFakulteta,Fakultet,KodKonGruppy,Tip_Spec,TselPr_PGU,TselPr_ROS,TselPr_1,TselPr_2,TselPr_3, PlanPriemaLg, PlanPriemaDog, eduLevel,fisId, krim_obshee,krim_ok,krim_cp,planpriemaig,ppdoglgot,ppkrimdog,ppkrimdoglgot FROM Spetsialnosti,Fakultety WHERE Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND KodSpetsialnosti LIKE ?");
                stmt.setObject(1,abit_Spec.getKodSpetsialnosti(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                     abit_Spec.setAbbreviaturaFakulteta(rs.getString(1));
                     abit_Spec.setNazvanieSpetsialnosti( rs.getString(2) );                
                     abit_Spec.setAbbreviatura( rs.getString(3) );
                     abit_Spec.setShifrSpetsialnosti( rs.getString(4) );
                     abit_Spec.setShifrSpetsialnostiOKSO( rs.getString(5) );
                     abit_Spec.setPlanPriema( new Integer(rs.getInt(6)) );
                     abit_Spec.setSobesedovanie( rs.getString(7) );
                     abit_Spec.setKodPredmeta( new Integer(rs.getString(8)) );
                     abit_Spec.setJekzamenZachet( rs.getString(9) );
                     abit_Spec.setPoluProhodnoiBall( new Integer(rs.getInt(10)));
                     abit_Spec.setProhodnoiBallNaSpetsialnosti(new Integer(rs.getInt(11)));
                     abit_Spec.setSpecial1( rs.getString(12) + "%" + rs.getString(13));
                     abit_Spec.setFakultet(rs.getString(14));
                     abit_Spec.setKodKonGrp(new Integer(rs.getString(15)));
                     abit_Spec.setTip_Spec(rs.getString(16));
                     abit_Spec.setPlanPriemaT1( new Integer(rs.getInt(17)) );
                     abit_Spec.setPlanPriemaT2( new Integer(rs.getInt(18)) );
                     abit_Spec.setPlanPriemaT3( new Integer(rs.getInt(19)) );
                     abit_Spec.setPlanPriemaT4( new Integer(rs.getInt(20)) );
                     abit_Spec.setPlanPriemaT5( new Integer(rs.getInt(21)) );
                     abit_Spec.setPlanPriemaLg( new Integer(rs.getInt(22)) );
                     abit_Spec.setPlanPriemaDog( new Integer(rs.getInt(23)));
                     abit_Spec.setFormaOb(rs.getString(24));
                     abit_Spec.setFisId(new Integer(rs.getInt(25)));
                   //  abit_Spec.setIdNews(new Integer(rs.getInt(26)));
                     abit_Spec.setSpecial10(rs.getString(24));
                     abit_Spec.setSpecial11(new Integer(rs.getInt(25)));
                     abit_Spec.setKrimobshee( new Integer(rs.getInt(26)) );
                     abit_Spec.setKrimok( new Integer(rs.getInt(27)) );
                     abit_Spec.setKrimcp( new Integer(rs.getInt(28)) );
                     abit_Spec.setPlanPriemaIG( new Integer(rs.getInt(29)) );
                     abit_Spec.setPpDogLgot( new Integer(rs.getInt(30)) );
                     abit_Spec.setPpKrimDog( new Integer(rs.getInt(31)) );
                     abit_Spec.setPpKrimDogLgot( new Integer(rs.getInt(32)) );
                     
                }
                
                stmt = conn.prepareStatement("select id, code, name from DictionaryCode10");
                rs = stmt.executeQuery();
                while (rs.next()) {
                    AbiturientBean abit_TMP = new AbiturientBean();
                    abit_TMP.setKodSpetsialnosti( new Integer(rs.getString(1)) );
                    abit_TMP.setSpecial1(rs.getString(2) );
                    abit_TMP.setSpecial2(rs.getString(3) );
                    String eduLevelFromShifr = rs.getString(2).substring(3, 5);
                    if (eduLevelFromShifr.equals("02"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "п" + "  " + rs.getString(3));       //спо
                    if (eduLevelFromShifr.equals("03"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "б" + "  " + rs.getString(3));       //бакалавры
                    if (eduLevelFromShifr.equals("04"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "м" + "  " + rs.getString(3) ); 	    //магистратура
                    if (eduLevelFromShifr.equals("05"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "с" + "  " + rs.getString(3) );		//специалитет
                    if (eduLevelFromShifr.equals("06"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "а" + "  " + rs.getString(3) );		//аспирантура
                    if (eduLevelFromShifr.equals("08"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "о" + "  " + rs.getString(3) );		//ординатура
                    if (eduLevelFromShifr.equals("09"))  abit_TMP.setSpecial3(rs.getString(2) + " " +  "и" + "  " + rs.getString(3) );		//ординатура
                 //   if (!(eduLevelFromShifr.equals("05")||eduLevelFromShifr.equals("04")||eduLevelFromShifr.equals("03")))  abit_TMP.setSpecial3(rs.getString(2)  + "  " + rs.getString(3) );//Магистры
                    abit_Spec_S5.add(abit_TMP);
                  }
                
                /*stmt = conn.prepareStatement("select socr, name from EduLevel");
                rs = stmt.executeQuery();
                while (rs.next()) {
                    AbiturientBean abit_TMP = new AbiturientBean();
                    abit_TMP.setSpecial1(rs.getString(1) );
                    abit_TMP.setSpecial2(rs.getString(2) );
                    abit_Spec_S6.add(abit_TMP);
                  }*/
                
                /////////////////////////////////////
            /*    stmt=conn.prepareStatement("SELECT DISTINCT fisId from SpecFisId where kodSpetsialnosti  LIKE ?" );
                		stmt.setObject(1, abit_Spec.getKodSpetsialnosti(),Types.INTEGER);
                		 rs = stmt.executeQuery();
 
                		 if(rs.next()) {
                		
                		
                        stmt1 = conn.prepareStatement("select id, code, name from DictionaryCode10");
                        rs1 = stmt1.executeQuery();
                        while (rs1.next()) {
                            AbiturientBean abit_TMP = new AbiturientBean();
                            abit_TMP.setKodSpetsialnosti( new Integer(rs1.getString(1)) );
                            abit_TMP.setSpecial1(rs1.getString(2) );
                            abit_TMP.setSpecial2(rs1.getString(3) );
                            abit_TMP.setSpecial3( rs1.getString(3) + rs1.getString(2) );
                            abit_Spec_S5.add(abit_TMP);
                          }
                		 
                		 
                		 
                }*/
                /////////////////////////////////////
                
                
                abit_Spec.setSpecial2("modify");
                stmt = conn.prepareStatement("SELECT DISTINCT Sobesedovanie FROM Spetsialnosti ORDER BY 1 ASC");
                rs = stmt.executeQuery();
                while (rs.next()) {
                  AbiturientBean abit_TMP = new AbiturientBean();
                  abit_TMP.setSobesedovanie(rs.getString(1));
                  abit_Spec_S2.add(abit_TMP);
                }
                stmt = conn.prepareStatement("SELECT DISTINCT JekzamenZachet FROM Spetsialnosti ORDER BY 1 ASC");
                rs = stmt.executeQuery();
                while (rs.next()) {
                  AbiturientBean abit_TMP = new AbiturientBean();
                  abit_TMP.setJekzamenZachet(rs.getString(1));
                  abit_Spec_S3.add(abit_TMP);
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                  form.setAction(us.getClientIntName("change","act"));
                  stmt = conn.prepareStatement("UPDATE Spetsialnosti SET KodFakulteta=?,NazvanieSpetsialnosti=?,Abbreviatura=?,ShifrSpetsialnosti=?,ShifrSpetsialnostiOKSO=?,PlanPriema=?,Sobesedovanie=?,KodPredmeta=?,JekzamenZachet=?,PoluProhodnoiBall=?,ProhodnoiBallNaSpetsialnosti=?,KodKonGruppy=?,Tip_Spec=?,TselPr_PGU=?,TselPr_ROS=?,TselPr_1=?,TselPr_2=?,TselPr_3=?,PlanPriemaLg=?,PlanPriemaDog=?,  eduLevel=?, fisId=?, Krim_obshee=?, krim_ok=?, krim_cp=?,planpriemaIG=?, ppdoglgot=?,ppkrimdog=?,ppkrimdoglgot=? WHERE KodSpetsialnosti LIKE ?");
                  stmt.setObject(1,new Integer((abit_Spec.getSpecial1()+"").substring(0,(abit_Spec.getSpecial1()+"").indexOf("%"))),Types.INTEGER);
                  stmt.setObject(2,abit_Spec.getNazvanieSpetsialnosti(),Types.VARCHAR);
                  stmt.setObject(3,abit_Spec.getAbbreviatura(),Types.VARCHAR);
                  stmt.setObject(4,abit_Spec.getShifrSpetsialnosti(),Types.VARCHAR);
                  stmt.setObject(5,abit_Spec.getShifrSpetsialnostiOKSO(),Types.VARCHAR);
                  stmt.setObject(6,abit_Spec.getPlanPriema(),Types.INTEGER);
                  stmt.setObject(7,abit_Spec.getSobesedovanie(),Types.VARCHAR);
                  stmt.setObject(8,abit_Spec.getKodPredmeta(),Types.INTEGER);
                  stmt.setObject(9,abit_Spec.getJekzamenZachet(),Types.VARCHAR);
                  stmt.setObject(10,abit_Spec.getPoluProhodnoiBall(),Types.INTEGER);
                  stmt.setObject(11,abit_Spec.getProhodnoiBallNaSpetsialnosti(),Types.INTEGER);
                  stmt.setObject(12,abit_Spec.getKodKonGrp(),Types.INTEGER);
                  stmt.setObject(13,abit_Spec.getTip_Spec(),Types.VARCHAR);
                  stmt.setObject(14,abit_Spec.getPlanPriemaT1(),Types.INTEGER);
                  stmt.setObject(15,abit_Spec.getPlanPriemaT2(),Types.INTEGER);
                  stmt.setObject(16,abit_Spec.getPlanPriemaT3(),Types.INTEGER);
                  stmt.setObject(17,abit_Spec.getPlanPriemaT4(),Types.INTEGER);
                  stmt.setObject(18,abit_Spec.getPlanPriemaT5(),Types.INTEGER);
                  stmt.setObject(19,abit_Spec.getPlanPriemaLg(),Types.INTEGER);
                  stmt.setObject(20,abit_Spec.getPlanPriemaDog(),Types.INTEGER);
                 
                  stmt.setObject(30,abit_Spec.getKodSpetsialnosti(),Types.INTEGER);
              //  stmt.setObject(23,abit_Spec.getIdNews(),Types.INTEGER);
                  
                  String eduLevelFromShifr = abit_Spec.getShifrSpetsialnosti().substring(3, 5);
                  
                  if (eduLevelFromShifr.equals("03"))  stmt.setObject(21, "б");       //бакалавры
                  if (eduLevelFromShifr.equals("02"))  stmt.setObject(21, "п");		//спо
                  if (eduLevelFromShifr.equals("05"))  stmt.setObject(21, "с"); 		 //специалисты
                  if (eduLevelFromShifr.equals("04"))  stmt.setObject(21, "м");		//Магистры
                  if (eduLevelFromShifr.equals("06"))  stmt.setObject(21, "а");		//асп
                  if (eduLevelFromShifr.equals("08"))  stmt.setObject(21, "о");		//орд
                  if (eduLevelFromShifr.equals("09"))  stmt.setObject(21, "и");		//орд
                 
                  
                
                  stmt.setObject(22, abit_Spec.getFisId());
             //     stmt.setObject(23, abit_Spec.getIdNews(),Types.INTEGER);
                  stmt.setObject(23,abit_Spec.getKrimobshee(),Types.INTEGER);
                  
                  stmt.setObject(24,abit_Spec.getKrimok(),Types.INTEGER);
                  stmt.setObject(25,abit_Spec.getKrimcp(),Types.INTEGER);
                  stmt.setObject(26,abit_Spec.getPlanPriemaIG(),Types.INTEGER);
                		  
                  stmt.setObject(27,abit_Spec.getPpDogLgot(),Types.INTEGER);
                  stmt.setObject(28,abit_Spec.getPpKrimDog(),Types.INTEGER);
                  stmt.setObject(29,abit_Spec.getPpKrimDogLgot(),Types.INTEGER);
                  
                  stmt.executeUpdate();
                  spetsialnosti_f  = true;

/************************************************************************************************/

            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE ?");
                stmt.setObject(1,abit_Spec.getKodSpetsialnosti(),Types.INTEGER);
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
                stmt.setObject(1,abit_Spec.getKodSpetsialnosti(),Types.INTEGER);
                stmt.executeUpdate();
                spetsialnosti_f  = true;
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
     
        request.setAttribute("abit_Spec", abit_Spec);
        request.setAttribute("abits_Spec", abits_Spec);
        request.setAttribute("abit_Spec_KonGrp", abit_Spec_KonGrp);
        request.setAttribute("abit_Spec_S1", abit_Spec_S1);
        request.setAttribute("abit_Spec_S2", abit_Spec_S2);
        request.setAttribute("abit_Spec_S3", abit_Spec_S3);
        request.setAttribute("abit_Spec_S4", abit_Spec_S4);
        request.setAttribute("abit_Spec_S5", abit_Spec_S5);
        request.setAttribute("abit_Spec_S6", abit_Spec_S6);
       
       // form.setFormaOb("м");
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(spetsialnosti_f) return mapping.findForward("spetsialnosti_f");
        return mapping.findForward("success");
    }
}