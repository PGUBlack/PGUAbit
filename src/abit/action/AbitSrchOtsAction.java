package abit.action;

import java.util.Enumeration;
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

public class AbitSrchOtsAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session           = request.getSession();
        Connection        conn              = null;
        PreparedStatement stmt              = null;
        ResultSet         rs                = null;
        Statement         stmnt             = null;
        PreparedStatement stmt_a            = null;
        PreparedStatement stmt_b            = null;
        ResultSet         rs_a              = null;
        ResultSet         rs_b              = null;
        ActionErrors      errors            = new ActionErrors();
        ActionError       msg               = null;
        AbiturientForm    form              = (AbiturientForm) actionForm;
        AbiturientBean    abit_A            = form.getBean(request, errors);
        boolean           error             = false;
        boolean           no_platn_dog      = false;
        boolean           is_platn_dog      = false;
        ActionForward     f                 = null;
        String            tmp               = null;
        String            result            = null;
        String            strFO[]           = new String[5];
        int               currInd           = 0;
        int               nextInd           = 0;
        int               totalCount        = 0;
        Enumeration       paramNames        = request.getParameterNames();
        ArrayList         abits_A           = new ArrayList();
        ArrayList         abit_forms        = new ArrayList();
        ArrayList         abit_osnovs       = new ArrayList();
        ArrayList         abit_A_S2         = new ArrayList();
        ArrayList         abit_A_S3         = new ArrayList();
        ArrayList         abit_A_S4     = new ArrayList();
        ArrayList         abit_A_S7         = new ArrayList();
        ArrayList         abit_A_S8         = new ArrayList();
        ArrayList         abit_A_S9         = new ArrayList();
        ArrayList         groups_bud        = new ArrayList();
        ArrayList         groups_kon        = new ArrayList();
        ArrayList         groups_z_bud      = new ArrayList();
        ArrayList         groups_z_kon      = new ArrayList();
        ArrayList         groups            = new ArrayList();
        ArrayList         fgr_msgs          = new ArrayList();
        ArrayList         predms            = new ArrayList();
        ArrayList         prdms             = new ArrayList();
        int               total_added       = 0;
        int               nomerGruppy       = 1;
        int               countAbiturients  = 0;
        boolean           no_err            = true;
        boolean           curr_err          = false;
        boolean           renew             = false;
        UserBean          user              = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null) {

            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );

        } else if( user.getGroup().getTypeId()==0 ) request.setAttribute("index","adminIndex");

        if ( errors.empty() ) {

        request.setAttribute("abitSrchOtsAction", new Boolean(true));
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

// Список предметов и их кодов

            tmp="%";
            stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY KodPredmeta ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while(rs.next())
             {
              AbiturientBean predm = new AbiturientBean();
              predm.setNazvaniePredmeta(rs.getString(1));
              predm.setPredmet(rs.getString(2));
              predms.add(predm);
              tmp += rs.getString(2) + "%";
             }
              abit_A.setSpecial10(tmp);

            if ( form.getAction().equals("fgr")) {

/***********************************************************/
/*****  ФОРМА НАСТРОЙКИ ПАРАМЕТРОВ ФОРМИРОВАНИЯ ГРУПП  *****/
/***********************************************************/

               abit_A.setSpecial10(""+session.getAttribute("total"));

               abit_A.setSpecial9(""+session.getAttribute("osnova_abits"));

               abit_A.setSpecial8(""+session.getAttribute("forma_abits"));

               form.setAction(us.getClientIntName("form_fgr","init"));

               stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
               stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
               rs = stmt.executeQuery();
               while (rs.next()) {
                 AbiturientBean abit_TMP = new AbiturientBean();
                 abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
                 abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
                 abit_A_S2.add(abit_TMP);
               }

// Выборка групп
                if(request.getParameter("kFak") != null)
                   session.setAttribute("kFak",request.getParameter("kFak"));

/****************************************************/
/********************* ДНЕВНОЕ и ВЕЧЕРНЕЕ ***********/
/****************************************************/

// Договорники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE g.KodGruppy NOT LIKE 1 AND a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'д' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'д' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'д' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  GroupsBean tmp0 = new GroupsBean();
                  tmp0.setKodGruppy(new Integer(rs.getString(1)));
                  tmp0.setAmount(rs.getInt(2));
                  tmp0.setGruppa(rs.getString(4));
                  tmp0.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_kon.add(tmp0);
                }
// Бюджетники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE g.KodGruppy NOT LIKE 1 AND a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'б' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'б' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'б' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  GroupsBean tmp1 = new GroupsBean();
                  tmp1.setKodGruppy(new Integer(rs.getString(1)));
                  tmp1.setAmount(rs.getInt(2));
                  tmp1.setGruppa(rs.getString(4));
                  tmp1.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_bud.add(tmp1);
                }

/****************************************************/
/******************** ЗАОЧНОЕ ***********************/
/****************************************************/

// Договорники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'зд' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'зд' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'зд' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  GroupsBean tmp2 = new GroupsBean();
                  tmp2.setKodGruppy(new Integer(rs.getString(1)));
                  tmp2.setAmount(rs.getInt(2));
                  tmp2.setGruppa(rs.getString(4));
                  tmp2.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_z_kon.add(tmp2);
                }
// Бюджетники
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE g.KodGruppy NOT LIKE 1 AND a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE 'зб' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE 'зб' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE 'зб' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.Gruppa");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  GroupsBean tmp3 = new GroupsBean();
                  tmp3.setKodGruppy(new Integer(rs.getString(1)));
                  tmp3.setAmount(rs.getInt(2));
                  tmp3.setGruppa(rs.getString(4));
                  tmp3.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_z_bud.add(tmp3);
                }

                request.setAttribute("groups_kon",groups_kon);
                request.setAttribute("groups_bud",groups_bud);
                request.setAttribute("groups_z_kon",groups_z_kon);
                request.setAttribute("groups_z_bud",groups_z_bud);

            } else if ( form.getAction().equals("go_fgr")) {

/********************************************************/
/*****   ФОРМИРОВАНИЕ ГРУПП ПО РЕЗУЛЬТАТАМ ПОИСКА   *****/
/********************************************************/

          form.setAction(us.getClientIntName("fg_res","act"));

// Создание списка групп

StringBuffer sel_grps_query = new StringBuffer("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL ");

// Выборка только заданных пользователем групп
           sel_grps_query.append("AND g.KodGruppy IN("+abit_A.getSpecial7()+"-1) ");

           sel_grps_query.append("GROUP BY g.KodGruppy,g.KodFakulteta,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1 FROM Gruppy g WHERE ");

// Выборка только заданных пользователем групп
           sel_grps_query.append("g.KodGruppy IN("+abit_A.getSpecial7()+"-1) ");

           sel_grps_query.append("AND KodGruppy IS NOT NULL AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL ");

// Выборка только заданных пользователем групп
           sel_grps_query.append("AND g.KodGruppy IN("+abit_A.getSpecial7()+"-1) ");

           sel_grps_query.append("GROUP BY g.KodGruppy,g.KodFakulteta,s.KodSpetsialnosti) ORDER BY g.KodGruppy");

           stmt = conn.prepareStatement(sel_grps_query.toString());
           rs = stmt.executeQuery();         

           while(rs.next()){
             GroupsBean tmp_grp = new GroupsBean();
             tmp_grp.setKodGruppy(new Integer(rs.getString(1)));
             tmp_grp.setAmount(rs.getInt(2));
             tmp_grp.setKodFakulteta(new Integer(rs.getString(3)));
             tmp_grp.setKodSpetsialnosti(new Integer(rs.getString(4)));
             groups.add(tmp_grp);
           }

/****************************** Выборка абитуриентов из БД ******************************/
           StringBuffer query = new StringBuffer("SELECT Abiturient.KodAbiturienta,DokumentyHranjatsja,Abbreviatura,");

           if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
             query.append("Konkurs.NomerLichnogoDela");
           else
             query.append("Abiturient.NomerLichnogoDela");

           query.append(",Familija,Imja,Otchestvo,NomerPlatnogoDogovora,Pol,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,NomerShkoly,PolnoeNaimenovanieZavedenija,TipOkonchennogoZavedenija,Gruppa,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,NomerSertifikata,KopijaSertifikata,Ball,Prinjat,KodSpetsialnZach,SeriaAtt,NomerAtt,Spetsialnosti.KodSpetsialnosti,Fakultety.ShifrFakulteta FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy,AbitDopInf");

           if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
             query.append(",Konkurs");

           query.append(" WHERE ");

           if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
             query.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND ");
           else
             query.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND ");


          StringBuffer noteQuery = new StringBuffer("Abiturient.KodAbiturienta IN(SELECT o.KodAbiturienta FROM Otsenki o,ZajavlennyeShkolnyeOtsenki zso WHERE o.KodAbiturienta=zso.KodAbiturienta AND o.KodPredmeta=zso.KodPredmeta");

//!!! WARNING !!! Здесь устанавливаются ограничения выборки по оценкам

//ограничение по аттестационной оценке

          if(!((""+session.getAttribute("otsenka_Att_ot")).equals("0") && (""+session.getAttribute("otsenka_Att_do")).equals("10"))) {
           noteQuery.append(" AND (zso.OtsenkaAtt BETWEEN "+session.getAttribute("otsenka_Att_ot")+" AND "+session.getAttribute("otsenka_Att_do"));
//           noteQuery.append(" OR zso.OtsenkaAtt IS NULL");
           noteQuery.append(")");
          }

//ограничение по баллам ЕГЭ

          if(!((""+session.getAttribute("otsenka_Ege_ot")).equals("0") && (""+session.getAttribute("otsenka_Ege_do")).equals("100"))) {
           noteQuery.append(" AND (zso.OtsenkaEge BETWEEN "+session.getAttribute("otsenka_Ege_ot")+" AND "+session.getAttribute("otsenka_Ege_do"));
//           noteQuery.append(" OR zso.OtsenkaEge IS NULL");
           noteQuery.append(")");
          }

//ограничение по заявленной оценке

          if(!((""+session.getAttribute("otsenka_Zaj_ot")).equals("0") && (""+session.getAttribute("otsenka_Zaj_do")).equals("10"))) {
           noteQuery.append(" AND (zso.OtsenkaZajavl BETWEEN "+session.getAttribute("otsenka_Zaj_ot")+" AND "+session.getAttribute("otsenka_Zaj_do"));
//           noteQuery.append(" OR zso.OtsenkaZajavl IS NULL");
           noteQuery.append(")");
          }

//ограничение по экзаменационной оценке

          if(!((""+session.getAttribute("otsenka_Exam_ot")).equals("0") && (""+session.getAttribute("otsenka_Exam_do")).equals("10"))) {
           noteQuery.append(" AND (o.Otsenka BETWEEN "+session.getAttribute("otsenka_Exam_ot")+" AND "+session.getAttribute("otsenka_Exam_do"));
//           noteQuery.append(" OR o.Otsenka IS NULL");
           noteQuery.append(")");
          }
          
          
          

//ограничение по дате апелляции

          if(!((""+session.getAttribute("DataApelljatsii")).equals("00-00-0000"))) {
           noteQuery.append(" AND (o.Data LIKE '"+session.getAttribute("DataApelljatsii")+"'");
//           noteQuery.append(" OR o.Data IS NULL");
           noteQuery.append(")");
          }

//ограничение по признаку апелляции

          if(!((""+session.getAttribute("Apelljatsija")).equals("%"))) {
           noteQuery.append(" AND (o.Apelljatsija LIKE '"+session.getAttribute("Apelljatsija")+"'");
//           noteQuery.append(" OR o.Apelljatsija IS NULL");
           noteQuery.append(")");
          }

        //ограничение по "где сдает экзамен"

          /*       if(!((""+session.getAttribute("Examen")).equals("%"))) {
                  noteQuery.append(" AND (zso.Examen LIKE '"+session.getAttribute("Examen")+"'");
//                  noteQuery.append(" OR zso.Examen IS NULL");
                  noteQuery.append(")");
                 }*/
                 if((""+session.getAttribute("Examen")).equals("+")) {
                     noteQuery.append(" AND zso.Examen LIKE '+' ");
//                   noteQuery.append(")");
                    }
                 if((""+session.getAttribute("Examen")).equals("*")) {
                     noteQuery.append(" AND zso.Examen LIKE '*' ");
//                   noteQuery.append(")");
                    }
                 if((""+session.getAttribute("Examen")).equals("егэ")) {
                     noteQuery.append(" AND zso.Examen is null ");
//                   noteQuery.append(")");
                    }

//ограничение по кодам предметов

          noteQuery.append(" AND o.KodPredmeta IN ("+session.getAttribute("KodyPredmetov")+")");

          noteQuery.append(")");
          query.append(noteQuery);

          query.append(" AND Gruppy.KodGruppy=Abiturient.KodGruppy AND Abiturient.kodAbiturienta = AbitDopInf.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND  Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza = ");
          query.append(session.getAttribute("kVuza"));

/// THIS LINE IS FOR
/// --- Debugging purposes ONLY ---
//System.out.println(query.toString());

//******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

          StringBuffer condition = new StringBuffer();
         /* if(!(""+session.getAttribute("KodOsnovyOb")).equals("0"))
           condition.append(" AND Abiturient.KodOsnovyOb LIKE "+"'"+session.getAttribute("KodOsnovyOb")+"'");
          if(!(""+session.getAttribute("KodFormyOb")).equals("0"))
           condition.append(" AND Abiturient.KodFormyOb LIKE "+"'"+session.getAttribute("KodFormyOb")+"'");*/
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
           condition.append(" AND (TipDokumenta IN(" + session.getAttribute("ShifrKursov")+") OR TipDokumenta IS NULL)");
          
          
          
          if(!(""+session.getAttribute("SrokObuchenija")).equals("%"))
          {
       	   condition.append(" AND NomerPotoka = '"+session.getAttribute("SrokObuchenija")+"'");
          }
          
          
          String vidDokCondition = "";
          if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))
       	   vidDokCondition =   " AND viddoksredobraz ='"+session.getAttribute("VidDokSredObraz")+"'";
          
          if((""+session.getAttribute("VidDokSredObraz")).equals("Диплом ВО/СПО"))
       	   vidDokCondition = " AND (viddoksredobraz = 'Диплом ВПО' OR viddoksredobraz = 'Диплом СПО')";
       	   
          if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))  
       				   condition.append(vidDokCondition);
          
          if(!(""+session.getAttribute("TipDokSredObraz")).equals("%"))
              condition.append(" AND (TipDokSredObraz LIKE "+"'"+session.getAttribute("TipDokSredObraz")+"'"+" OR TipDokSredObraz IS NULL)");
          
          
          if(!(""+session.getAttribute("KodLgot")).equals("%"))  
			   condition.append(" AND Konkurs.OP LIKE "+"'"+session.getAttribute("KodLgot")+"'");
          
          if((""+session.getAttribute("bud1")).equals("on"))  
			   condition.append(" AND AbitDopInf.Dist not in ('-')");
          
       /*   if(!(""+session.getAttribute("ShifrMedali")).equals("%"))
           condition.append(" AND (ShifrMedali IN(" + session.getAttribute("ShifrMedali")+") OR ShifrMedali IS NULL)");
          if(!(""+session.getAttribute("ShifrLgot")).equals("%"))
           condition.append(" AND (ShifrLgot IN(" + session.getAttribute("ShifrLgot")+") OR ShifrLgot IS NULL)");*/
          if(!(""+session.getAttribute("NomerPlatnogoDogovora")).equals("%"))
           condition.append(" AND (" + session.getAttribute("NomerPlatnogoDogovora")+session.getAttribute("PriznakDog")+")");
          if(!(""+session.getAttribute("Pol")).equals("%"))
           condition.append(" AND (Pol LIKE "+"'"+session.getAttribute("Pol")+"'"+" OR Pol IS NULL)");
          if(!((""+session.getAttribute("GodOkonchanijaSrObrazovanija1")).equals("1950") && (""+session.getAttribute("GodOkonchanijaSrObrazovanija2")).equals("9999"))) {
           condition.append(" AND (GodOkonchanijaSrObrazovanija >= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija1")+"'");
           condition.append(" AND GodOkonchanijaSrObrazovanija <= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija2")+"'"+" OR GodOkonchanijaSrObrazovanija IS NULL)");
          }
          if(!(""+session.getAttribute("GdePoluchilSrObrazovanie")).equals("%"))
           condition.append(" AND (GdePoluchilSrObrazovanie LIKE "+"'"+session.getAttribute("GdePoluchilSrObrazovanie")+"'"+" OR GdePoluchilSrObrazovanie IS NULL)");
          if(!(""+session.getAttribute("NomerShkoly")).equals("%"))
           condition.append(" AND (NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'"+" OR NomerShkoly IS NULL)");
        /*  if(!(""+session.getAttribute("Nazvanie")).equals("%"))
           condition.append(" AND (Punkty.Nazvanie LIKE "+"'"+session.getAttribute("Nazvanie")+"'"+" OR Punkty.Nazvanie IS NULL)");
          if(!(""+session.getAttribute("NazvanieRajona")).equals("%"))
           condition.append(" AND (Rajony.NazvanieRajona LIKE "+"'"+session.getAttribute("NazvanieRajona")+"'"+" OR NazvanieRajona IS NULL)");
          if(!(""+session.getAttribute("NazvanieOblasti")).equals("%"))
           condition.append(" AND (Oblasti.NazvanieOblasti LIKE "+"'"+session.getAttribute("NazvanieOblasti")+"'"+" OR NazvanieOblasti IS NULL)");
          if(!(""+session.getAttribute("PolnoeNaimenovanieZavedenija")).equals("%"))*/
           condition.append(" AND (PolnoeNaimenovanieZavedenija LIKE "+"'"+session.getAttribute("PolnoeNaimenovanieZavedenija")+"'"+" OR PolnoeNaimenovanieZavedenija IS NULL)");
          if(!(""+session.getAttribute("TipOkonchennogoZavedenija")).equals("%"))
           condition.append(" AND (TipOkonchennogoZavedenija LIKE "+"'"+session.getAttribute("TipOkonchennogoZavedenija")+"'"+" OR TipOkonchennogoZavedenija IS NULL)");
          if(!(""+session.getAttribute("Gruppa")).equals("%"))
           condition.append(" AND (Gruppa LIKE "+"'"+session.getAttribute("Gruppa")+"'"+" OR Gruppa IS NULL)");
          if(!(""+session.getAttribute("Ball")).equals("%"))
           condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"')");
          if((session.getAttribute("KodSpetsialnZach")+"").equals("%"))
            condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"' OR KodSpetsialnZach IS NULL)");
          else
            condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"')");
          if(!(""+session.getAttribute("Prinjat")).equals("%"))
           condition.append(" AND (Prinjat IN(" + session.getAttribute("Prinjat")+") OR Prinjat IS NULL)");
//************************************************
           query.append(condition);
           query.append(" ORDER BY "+ session.getAttribute("stSort")+" "+session.getAttribute("prSort"));
           
           System.out.println("QUERY:  " + query);
                    stmt = conn.prepareStatement(query.toString());

/// THIS LINE IS FOR
/// --- Debugging purposes ONLY ---
 System.out.println(query.toString());

                    rs = stmt.executeQuery();

/***** Распределение абитуриента в группу *****/

              while(rs.next()){

               curr_err = true;

// Перебор групп данного факультета

                for(int i=0;i<groups.size();i++){
	          GroupsBean curr_grp = new GroupsBean();
                  curr_grp = (GroupsBean)(groups.get(i));
// Если в текущей группе абитуриентов меньше заданного максимума, и 
// код факультета группы = коду факультета абитуриента, то можем добавлять абитуриента в группу

                  if(curr_grp.getAmount() < abit_A.getMaxCountAbiturients()) {

/******* проверка соответствия группы - подмножеству абитуриента ********/

                      stmt_b = conn.prepareStatement("SELECT * FROM Abiturient a,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodVuza LIKE '"+session.getAttribute("kVuza")+"' AND a.KodGruppy LIKE '"+curr_grp.getKodGruppy()+"'");
                      rs_b = stmt_b.executeQuery();
//Если в группе абитуриенты имеют другой набор специальностей, то выбираем следующую группу.
                      if(!rs_b.next() && curr_grp.getAmount()!=0) continue;
                      curr_err = false;
                      stmt_a = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=? WHERE KodAbiturienta LIKE ?");
                      stmt_a.setObject(1,curr_grp.getKodGruppy(),Types.INTEGER);
                      stmt_a.setObject(2,new Integer(rs.getString(1)),Types.INTEGER);
                      stmt_a.executeUpdate();

                      curr_grp.setAmount(curr_grp.getAmount()+1);
                      groups.remove(i);
                      groups.add(i,curr_grp);
                      total_added++;
                      break;
                  }
                }
              if(curr_err){
                 no_err = false;
                 stmt_a = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,AbbreviaturaFakulteta,NomerLichnogoDela FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND KodAbiturienta LIKE ?");
                 stmt_a.setObject(1,new Integer(rs.getString(1)),Types.INTEGER);
                 rs_a = stmt_a.executeQuery();
                 if(rs_a.next()){
                   MessageBean mesg = new MessageBean();
                   mesg.setStatus("Ошибка!");
                   mesg.setMessage("Абит. "+rs_a.getString(1)+" "+rs_a.getString(2).substring(0,1).toUpperCase()+"."+rs_a.getString(3).substring(0,1).toUpperCase()+". ("+rs_a.getString(5)+") не распр. в гр. "+rs_a.getString(4).toUpperCase());
                   fgr_msgs.add(mesg);
                 }
              }
            }

// Результаты работы
             if(no_err) { 
               MessageBean msg1 = new MessageBean();
               msg1.setMessage("Внимание! Группы успешно сформированы.");
               fgr_msgs.add(msg1);
             } else{
               MessageBean msg0 = new MessageBean();
               msg0.setMessage("---------------------------------------------------------------");
               fgr_msgs.add(msg0);
               MessageBean msg1 = new MessageBean();
               msg1.setMessage("Внимание! Для вышеперечисленных абитуриентов не хватило места.");
               fgr_msgs.add(msg1);
               MessageBean msg2 = new MessageBean();
               msg2.setMessage("Добавьте больше групп или увеличьте количество абитуриентов в группе!");
               fgr_msgs.add(msg2);
             }
             MessageBean msg2 = new MessageBean();
             msg2.setMessage("--------------------------------------------------");
             fgr_msgs.add(msg2);
             MessageBean msg3 = new MessageBean();
             msg3.setMessage("Всего было распределено: "+total_added+" абитуриентов");
             fgr_msgs.add(msg3);

// Блокировка полностью сформирорванных групп
             for(int i=0;i<groups.size();i++){
                GroupsBean curr_grp = new GroupsBean();
                curr_grp = (GroupsBean)(groups.get(i));
                if(curr_grp.getAmount() >= abit_A.getMaxCountAbiturients()){
                  stmt = conn.prepareStatement("UPDATE Gruppy SET Locked=1 WHERE KodGruppy LIKE ?");
                  stmt.setObject(1,curr_grp.getKodGruppy(),Types.INTEGER);
                  stmt.executeUpdate();
                }
             }

            } else if ( form.getAction().equals("search")) {

/********************** Подготовка данных для ввода с помощью селекторов ************************/
            session.removeAttribute("position");
            
            session.removeAttribute("bud1");
            
// Сброс признака просмотра результатов поиска после модификации
            session.removeAttribute("resrch");
            session.removeAttribute("DokumentyHranjatsja");
            session.removeAttribute("SrokObuchenija");
            session.removeAttribute("Special1");
//            session.removeAttribute("ShifrFakulteta");
            session.removeAttribute("NomerLichnogoDela");
            session.removeAttribute("TipDokSredObraz");
            session.removeAttribute("VidDokSredObraz");
            session.removeAttribute("Familija");
            session.removeAttribute("Imja");
            session.removeAttribute("KodLgot");
            session.removeAttribute("Otchestvo");
            session.removeAttribute("KodSpetsialnZach");
            session.removeAttribute("ShifrKursov");
            session.removeAttribute("ShifrMedali");
            session.removeAttribute("ShifrLgot");
            session.removeAttribute("otsenka_Att_ot");
            session.removeAttribute("otsenka_Att_do");
            session.removeAttribute("otsenka_Ege_ot");
            session.removeAttribute("otsenka_Ege_do");
            session.removeAttribute("otsenka_Zaj_ot");
            session.removeAttribute("otsenka_Zaj_do");
            session.removeAttribute("PriznakDog");
            session.removeAttribute("NomerPlatnogoDogovora");
            session.removeAttribute("GodOkonchanijaSrObrazovanija1");
            session.removeAttribute("GodOkonchanijaSrObrazovanija2");
            session.removeAttribute("GdePoluchilSrObrazovanie");
            session.removeAttribute("DataApelljatsii");
            session.removeAttribute("Apelljatsija");
            session.removeAttribute("Examen");
            session.removeAttribute("NomerShkoly");
            session.removeAttribute("Nazvanie");
            session.removeAttribute("NazvanieRajona");
            session.removeAttribute("NazvanieOblasti");
            session.removeAttribute("PolnoeNaimenovanieZavedenija");
            session.removeAttribute("TipOkonchennogoZavedenija");
            session.removeAttribute("Gruppa");
            session.removeAttribute("KodFormyOb");
            session.removeAttribute("KodOsnovyOb");
            session.removeAttribute("Ball");
            session.removeAttribute("Prinjat");
            session.removeAttribute("KodyPredmetov");
            session.removeAttribute("UseAllSpecs");

            stmt = conn.prepareStatement("SELECT DISTINCT KodFormyOb,Sokr FROM Forma_Obuch WHERE Sokr NOT LIKE '-' ORDER BY KodFormyOb ASC");
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodFormyOb(new Integer(rs.getInt(1)));
              abit_TMP.setSokr(rs.getString(2));
              abit_forms.add(abit_TMP);
            }

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

            } else if ( form.getAction().equals("searching") ) {

/************************************************************************************************/
/**************************************      Поиск      *****************************************/
/************************************************************************************************/

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
   session.setAttribute("Familija",StringUtil.toMySQL((abit_A.getFamilija()+"")));
   session.setAttribute("Imja",StringUtil.toMySQL((abit_A.getImja()+"")));
   session.setAttribute("Otchestvo",StringUtil.toMySQL((abit_A.getOtchestvo()+"")));
   
   
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
   
   
   
   if(abit_A.getBud_1()!=null){
	   if(abit_A.getBud_1().equals("on"))
		     session.setAttribute("bud1","on");
	   }

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

// Обработка последовательности значений запроса для шифра курсов
   session.setAttribute("ShifrKursov",StringUtil.PlaceUnaryComas("ShifrKursov",abit_A.getShifrKursov()));

// Обработка последовательности значений запроса для шифра медали
   session.setAttribute("ShifrMedali",StringUtil.PlaceUnaryComas("ShifrMedali",abit_A.getShifrMedali()));

// Обработка последовательности значений запроса для шифра льгот
   session.setAttribute("ShifrLgot",StringUtil.PlaceUnaryComas("ShifrLgot",abit_A.getShifrLgot()));
   
   session.setAttribute("KodLgot", abit_A.getKodLgot());

// Обработка последовательности значений запроса для признака зачисления
   session.setAttribute("Prinjat",StringUtil.PlaceUnaryComas("Prinjat",abit_A.getPrinjat()));

   session.setAttribute("otsenka_Att_ot",abit_A.getOtsenka_Att_ot());
   session.setAttribute("otsenka_Att_do",abit_A.getOtsenka_Att_do());
   session.setAttribute("otsenka_Zaj_ot",abit_A.getOtsenka_Zaj_ot());
   session.setAttribute("otsenka_Zaj_do",abit_A.getOtsenka_Zaj_do());
   session.setAttribute("otsenka_Ege_ot",abit_A.getOtsenka_Ege_ot());
   session.setAttribute("otsenka_Ege_do",abit_A.getOtsenka_Ege_do());
   session.setAttribute("otsenka_Exam_ot",abit_A.getOtsenka_Exam_ot());
   session.setAttribute("otsenka_Exam_do",abit_A.getOtsenka_Exam_do());

   session.setAttribute("Pol",StringUtil.toMySQL((abit_A.getPol()+"")));
   
   if((""+abit_A.getSrokObuchenija()).equals("*"))
	     session.setAttribute("SrokObuchenija","%");
	   else
	     session.setAttribute("SrokObuchenija",StringUtil.toMySQL((abit_A.getSrokObuchenija()+"")));

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
 //  session.setAttribute("KodOsnovyOb",StringUtil.toMySQL((abit_A.getKodOsnovyOb()+"")));
 //  session.setAttribute("KodFormyOb",StringUtil.toMySQL((abit_A.getKodFormyOb()+"")));
   session.setAttribute("GodOkonchanijaSrObrazovanija1",StringUtil.toMySQL((abit_A.getSpecial5()+"")));
   session.setAttribute("GodOkonchanijaSrObrazovanija2",StringUtil.toMySQL((abit_A.getSpecial6()+"")));
   session.setAttribute("GdePoluchilSrObrazovanie",StringUtil.toMySQL((abit_A.getGdePoluchilSrObrazovanie()+"")));
   session.setAttribute("NomerShkoly",StringUtil.toMySQL((abit_A.getNomerShkoly()+"")));
   session.setAttribute("Nazvanie",StringUtil.toMySQL((abit_A.getNazvanie()+"")));
   session.setAttribute("NazvanieRajona",StringUtil.toMySQL((abit_A.getNazvanieRajona()+"")));
   session.setAttribute("NazvanieOblasti",StringUtil.toMySQL((abit_A.getNazvanieOblasti()+"")));
   session.setAttribute("PolnoeNaimenovanieZavedenija",StringUtil.toMySQL((abit_A.getPolnoeNaimenovanieZavedenija()+"")));
   session.setAttribute("TipOkonchennogoZavedenija",StringUtil.toMySQL((abit_A.getTipOkonchennogoZavedenija()+"")));
   session.setAttribute("Gruppa",StringUtil.toMySQL((abit_A.getGruppa()+"")));
   session.setAttribute("Ball",StringUtil.toMySQL((abit_A.getSpecial7()+"")));
   session.setAttribute("DataApelljatsii",abit_A.getDataApelljatsii());
   session.setAttribute("Apelljatsija",StringUtil.toMySQL((abit_A.getApelljatsija()+"")));
   session.setAttribute("Examen",StringUtil.toMySQL((abit_A.getExamen()+"")));
   session.setAttribute("UseAllSpecs",StringUtil.toMySQL((abit_A.getUseAllSpecs()+"")));
   
   
   session.setAttribute("VidDokSredObraz",abit_A.getVidDokSredObraz());
   /////////////////0207
   if((""+abit_A.getVidDokSredObraz()).equals("-"))
	     session.setAttribute("VidDokSredObraz","%");
   
   if((""+abit_A.getKodLgot()).equals("1"))
	     session.setAttribute("KodLgot","%");
 
   
   session.setAttribute("TipDokSredObraz",StringUtil.toMySQL((abit_A.getTipDokSredObraz()+"")));
   
   

   StringBuffer codes = new StringBuffer("-1");
   while(paramNames.hasMoreElements()) {
     String paramName = (String)paramNames.nextElement();
     String paramValue[] = request.getParameterValues(paramName);
     if(paramName.indexOf("prdm") != -1) {
       codes.append(","+paramName.substring(4));  // Код предмета
     }
   }
   session.setAttribute("KodyPredmetov",codes.toString());
}


/****************************** Выборка абитуриентов из БД ******************************/
                    StringBuffer query = new StringBuffer("SELECT Abiturient.KodAbiturienta,DokumentyHranjatsja,Abbreviatura,");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append("Konkurs.NomerLichnogoDela");
                    else
                      query.append("Abiturient.NomerLichnogoDela");

                    query.append(",Familija,Imja,Otchestvo,NomerPlatnogoDogovora,Pol,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,NomerShkoly,PolnoeNaimenovanieZavedenija,TipOkonchennogoZavedenija,Gruppa,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,NomerSertifikata,KopijaSertifikata,Ball,Prinjat,KodSpetsialnZach,SeriaAtt,NomerAtt,Spetsialnosti.KodSpetsialnosti,Fakultety.ShifrFakulteta FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy,AbitDopInf");
 
                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append(",Konkurs");

                    query.append(" WHERE ");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      query.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND ");
                    else
                      query.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND ");


                   StringBuffer noteQuery = new StringBuffer("Abiturient.KodAbiturienta IN(SELECT o.KodAbiturienta FROM Otsenki o,ZajavlennyeShkolnyeOtsenki zso WHERE o.KodAbiturienta=zso.KodAbiturienta AND o.KodPredmeta=zso.KodPredmeta");

// !!! WARNING !!! Здесь устанавливаются ограничения выборки по оценкам

//ограничение по аттестационной оценке

                   if(!((""+session.getAttribute("otsenka_Att_ot")).equals("0") && (""+session.getAttribute("otsenka_Att_do")).equals("10"))) {
                    noteQuery.append(" AND (zso.OtsenkaAtt BETWEEN "+session.getAttribute("otsenka_Att_ot")+" AND "+session.getAttribute("otsenka_Att_do"));
//                    noteQuery.append(" OR zso.OtsenkaAtt IS NULL");
                    noteQuery.append(")");
                   }

//ограничение по баллам ЕГЭ

                   if(!((""+session.getAttribute("otsenka_Ege_ot")).equals("0") && (""+session.getAttribute("otsenka_Ege_do")).equals("100"))) {
                    noteQuery.append(" AND (zso.OtsenkaEge BETWEEN "+session.getAttribute("otsenka_Ege_ot")+" AND "+session.getAttribute("otsenka_Ege_do"));
//                    noteQuery.append(" OR zso.OtsenkaEge IS NULL");
                    noteQuery.append(")");
                   }

//ограничение по заявленной оценке

                   if(!((""+session.getAttribute("otsenka_Zaj_ot")).equals("0") && (""+session.getAttribute("otsenka_Zaj_do")).equals("10"))) {
                    noteQuery.append(" AND (zso.OtsenkaZajavl BETWEEN "+session.getAttribute("otsenka_Zaj_ot")+" AND "+session.getAttribute("otsenka_Zaj_do"));
//                    noteQuery.append(" OR zso.OtsenkaZajavl IS NULL");
                    noteQuery.append(")");
                   }

//ограничение по экзаменационной оценке

                   if(!((""+session.getAttribute("otsenka_Exam_ot")).equals("0") && (""+session.getAttribute("otsenka_Exam_do")).equals("10"))) {
                    noteQuery.append(" AND (o.Otsenka BETWEEN "+session.getAttribute("otsenka_Exam_ot")+" AND "+session.getAttribute("otsenka_Exam_do"));
//                    noteQuery.append(" OR o.Otsenka IS NULL");
                    noteQuery.append(")");
                   }
                   
                   
                   

//ограничение по дате апелляции

                   if(!((""+session.getAttribute("DataApelljatsii")).equals("00-00-0000"))) {
                    noteQuery.append(" AND (o.Data LIKE '"+session.getAttribute("DataApelljatsii")+"'");
//                    noteQuery.append(" OR o.Data IS NULL");
                    noteQuery.append(")");
                   }

//ограничение по признаку апелляции

                   if(!((""+session.getAttribute("Apelljatsija")).equals("%"))) {
                    noteQuery.append(" AND (o.Apelljatsija LIKE '"+session.getAttribute("Apelljatsija")+"'");
//                    noteQuery.append(" OR o.Apelljatsija IS NULL");
                    noteQuery.append(")");
                   }

                 //ограничение по "где сдает экзамен"

                   /*       if(!((""+session.getAttribute("Examen")).equals("%"))) {
                           noteQuery.append(" AND (zso.Examen LIKE '"+session.getAttribute("Examen")+"'");
//                           noteQuery.append(" OR zso.Examen IS NULL");
                           noteQuery.append(")");
                          }*/
                          if((""+session.getAttribute("Examen")).equals("+")) {
                              noteQuery.append(" AND zso.Examen LIKE '+' ");
//                            noteQuery.append(")");
                             }
                          if((""+session.getAttribute("Examen")).equals("*")) {
                              noteQuery.append(" AND zso.Examen LIKE '*' ");
//                            noteQuery.append(")");
                             }
                          if((""+session.getAttribute("Examen")).equals("егэ")) {
                              noteQuery.append(" AND zso.Examen is null ");
//                            noteQuery.append(")");
                             }

//ограничение по кодам предметов

                   noteQuery.append(" AND o.KodPredmeta IN ("+session.getAttribute("KodyPredmetov")+")");

                   noteQuery.append(")");
                   query.append(noteQuery);

                   query.append(" AND Gruppy.KodGruppy=Abiturient.KodGruppy AND Abiturient.kodAbiturienta = AbitDopInf.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND  Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza = ");
                   query.append(session.getAttribute("kVuza"));

/// THIS LINE IS FOR
/// --- Debugging purposes ONLY ---
//System.out.println(query.toString());

//******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

                   StringBuffer condition = new StringBuffer();
                  /* if(!(""+session.getAttribute("KodOsnovyOb")).equals("0"))
                    condition.append(" AND Abiturient.KodOsnovyOb LIKE "+"'"+session.getAttribute("KodOsnovyOb")+"'");
                   if(!(""+session.getAttribute("KodFormyOb")).equals("0"))
                    condition.append(" AND Abiturient.KodFormyOb LIKE "+"'"+session.getAttribute("KodFormyOb")+"'");*/
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
                    condition.append(" AND (TipDokumenta IN(" + session.getAttribute("ShifrKursov")+") OR TipDokumenta IS NULL)");
                   
                   String vidDokCondition = "";
                   if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))
                	   vidDokCondition =   " AND viddoksredobraz ='"+session.getAttribute("VidDokSredObraz")+"'";
                   
                   if((""+session.getAttribute("VidDokSredObraz")).equals("Диплом ВО/СПО"))
                	   vidDokCondition = " AND (viddoksredobraz = 'Диплом ВПО' OR viddoksredobraz = 'Диплом СПО')";
                	   
                   if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))  
                				   condition.append(vidDokCondition);
                   
                   if(!(""+session.getAttribute("TipDokSredObraz")).equals("%"))
                       condition.append(" AND (TipDokSredObraz LIKE "+"'"+session.getAttribute("TipDokSredObraz")+"'"+" OR TipDokSredObraz IS NULL)");
                   
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
                   
                   
                   
                   if(!(""+session.getAttribute("KodLgot")).equals("%"))  
    				   condition.append(" AND Konkurs.OP LIKE "+"'"+session.getAttribute("KodLgot")+"'");
                   
                   if((""+session.getAttribute("bud1")).equals("on"))  
    				   condition.append(" AND AbitDopInf.Dist not in ('-')");
                   
                /*   if(!(""+session.getAttribute("ShifrMedali")).equals("%"))
                    condition.append(" AND (ShifrMedali IN(" + session.getAttribute("ShifrMedali")+") OR ShifrMedali IS NULL)");
                   if(!(""+session.getAttribute("ShifrLgot")).equals("%"))
                    condition.append(" AND (ShifrLgot IN(" + session.getAttribute("ShifrLgot")+") OR ShifrLgot IS NULL)");*/
                   if(!(""+session.getAttribute("NomerPlatnogoDogovora")).equals("%"))
                    condition.append(" AND (" + session.getAttribute("NomerPlatnogoDogovora")+session.getAttribute("PriznakDog")+")");
                   if(!(""+session.getAttribute("Pol")).equals("%"))
                    condition.append(" AND (Pol LIKE "+"'"+session.getAttribute("Pol")+"'"+" OR Pol IS NULL)");
                   if(!((""+session.getAttribute("GodOkonchanijaSrObrazovanija1")).equals("1950") && (""+session.getAttribute("GodOkonchanijaSrObrazovanija2")).equals("9999"))) {
                    condition.append(" AND (GodOkonchanijaSrObrazovanija >= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija1")+"'");
                    condition.append(" AND GodOkonchanijaSrObrazovanija <= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija2")+"'"+" OR GodOkonchanijaSrObrazovanija IS NULL)");
                   }
                   if(!(""+session.getAttribute("GdePoluchilSrObrazovanie")).equals("%"))
                    condition.append(" AND (GdePoluchilSrObrazovanie LIKE "+"'"+session.getAttribute("GdePoluchilSrObrazovanie")+"'"+" OR GdePoluchilSrObrazovanie IS NULL)");
                   if(!(""+session.getAttribute("NomerShkoly")).equals("%"))
                    condition.append(" AND (NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'"+" OR NomerShkoly IS NULL)");
                 /*  if(!(""+session.getAttribute("Nazvanie")).equals("%"))
                    condition.append(" AND (Punkty.Nazvanie LIKE "+"'"+session.getAttribute("Nazvanie")+"'"+" OR Punkty.Nazvanie IS NULL)");
                   if(!(""+session.getAttribute("NazvanieRajona")).equals("%"))
                    condition.append(" AND (Rajony.NazvanieRajona LIKE "+"'"+session.getAttribute("NazvanieRajona")+"'"+" OR NazvanieRajona IS NULL)");
                   if(!(""+session.getAttribute("NazvanieOblasti")).equals("%"))
                    condition.append(" AND (Oblasti.NazvanieOblasti LIKE "+"'"+session.getAttribute("NazvanieOblasti")+"'"+" OR NazvanieOblasti IS NULL)");
                   if(!(""+session.getAttribute("PolnoeNaimenovanieZavedenija")).equals("%"))*/
                    condition.append(" AND (PolnoeNaimenovanieZavedenija LIKE "+"'"+session.getAttribute("PolnoeNaimenovanieZavedenija")+"'"+" OR PolnoeNaimenovanieZavedenija IS NULL)");
                   if(!(""+session.getAttribute("TipOkonchennogoZavedenija")).equals("%"))
                    condition.append(" AND (TipOkonchennogoZavedenija LIKE "+"'"+session.getAttribute("TipOkonchennogoZavedenija")+"'"+" OR TipOkonchennogoZavedenija IS NULL)");
                   if(!(""+session.getAttribute("Gruppa")).equals("%"))
                    condition.append(" AND (Gruppa LIKE "+"'"+session.getAttribute("Gruppa")+"'"+" OR Gruppa IS NULL)");
                   if(!(""+session.getAttribute("Ball")).equals("%"))
                    condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"')");
                   if((session.getAttribute("KodSpetsialnZach")+"").equals("%"))
                     condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"' OR KodSpetsialnZach IS NULL)");
                   else
                     condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"')");
                   if(!(""+session.getAttribute("Prinjat")).equals("%"))
                    condition.append(" AND (Prinjat IN(" + session.getAttribute("Prinjat")+") OR Prinjat IS NULL)");
//************************************************
                    query.append(condition);
                    query.append(" ORDER BY "+ session.getAttribute("stSort")+" "+session.getAttribute("prSort"));
                    
                    System.out.println("QUERY:  " + query);
/*************************** Выборка числа абитуриентов из БД ****************************/
                    totalCount = 0;

/// For Debugging Purposes Only
///
/// abit_A.setSpecial1(query.toString());
/// System.out.println(query);

                    StringBuffer queryTotal = new StringBuffer("SELECT COUNT(Abiturient.KodAbiturienta) FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy, AbitDopInf");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      queryTotal.append(",Konkurs");

                    queryTotal.append(" WHERE ");

                    if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
                      queryTotal.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND ");
                    else
                      queryTotal.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND ");

                    queryTotal.append(noteQuery);
                    queryTotal.append("AND Gruppy.KodGruppy=Abiturient.KodGruppy AND Abiturient.kodAbiturienta = AbitDopInf.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.KodVuza = ");
                    queryTotal.append(session.getAttribute("kVuza"));
                    queryTotal.append(condition);

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
       stmnt.executeUpdate("DECLARE sel_cursor CURSOR GLOBAL SCROLL READ_ONLY FOR "+query.toString()+"; OPEN sel_cursor;");
   }

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
           stmnt.executeUpdate("DECLARE sel_cursor CURSOR GLOBAL SCROLL READ_ONLY FOR "+query.toString()+";");
           stmnt = conn.createStatement();
           stmnt.executeUpdate("OPEN sel_cursor;");
         }
         else {

           if(request.getParameter("prev")!=null) {

              if(( position - totalRows ) >= totalRows) {

                   position -= totalRows;
              }
              else position = 0;
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
     abit_TMP.setNumber(""+(StringUtil.toInt(""+session.getAttribute("position"),0)+row));
     abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
     abit_TMP.setDokumentyHranjatsja(rs.getString(2));
     abit_TMP.setSpecial1(rs.getString(3));// AbbreviaturaSpetsialnosti
     abit_TMP.setNomerLichnogoDela(rs.getString(4));
     abit_TMP.setFamilija(rs.getString(5));
     abit_TMP.setImja(rs.getString(6));
     abit_TMP.setOtchestvo(rs.getString(7));
     abit_TMP.setFio(rs.getString(5)+" "+rs.getString(6).toUpperCase().substring(0,1)+"."+rs.getString(7).toUpperCase().substring(0,1)+".");
   /*  abit_TMP.setShifrKursov(rs.getString(8));
     abit_TMP.setShifrMedali(rs.getString(9));
     abit_TMP.setShifrLgot(rs.getString(10));*/
     abit_TMP.setNomerPlatnogoDogovora(rs.getString(8));

   if(abit_TMP.getNomerPlatnogoDogovora()!= null) is_platn_dog = true;
   else no_platn_dog = true;

     abit_TMP.setPol(rs.getString(9));
     abit_TMP.setGodOkonchanijaSrObrazovanija(new Integer(rs.getString(10)));
     abit_TMP.setGdePoluchilSrObrazovanie(rs.getString(11));
     abit_TMP.setNomerShkoly(rs.getString(12));
   /*  abit_TMP.setNazvanie(rs.getString(13));  // Nazvanie Punkta
     abit_TMP.setNazvanieRajona(rs.getString(17));
     abit_TMP.setNazvanieOblasti(rs.getString(18));*/
     abit_TMP.setPolnoeNaimenovanieZavedenija(rs.getString(13));
     abit_TMP.setTipOkonchennogoZavedenija(rs.getString(14));
     abit_TMP.setGruppa(rs.getString(15));
     abit_TMP.setTipDokumenta(rs.getString(16));
     abit_TMP.setNomerDokumenta(rs.getString(17));
     abit_TMP.setSeriaDokumenta(rs.getString(18));
     abit_TMP.setDataVydDokumenta(rs.getString(19));
     abit_TMP.setKemVydDokument(rs.getString(20));
     abit_TMP.setTipDokSredObraz(rs.getString(21));
     abit_TMP.setNomerSertifikata(rs.getString(22));
     abit_TMP.setKopijaSertifikata(rs.getString(23));
     abit_TMP.setBall(new Integer(rs.getInt(24)));
     abit_TMP.setPrinjat(rs.getString(25));
   //  abit_TMP.setFormaOb(rs.getString(33));

  /* if(abit_TMP.getFormaOb().equals("очная"))           { strFO[0] = "очная";}
   else if(abit_TMP.getFormaOb().equals("заочная"))    { strFO[1] = "заочная";}
   else if(abit_TMP.getFormaOb().equals("заочно-уск")) { strFO[2] = "заочно-уск";}
   else if(abit_TMP.getFormaOb().equals("экстернат"))  { strFO[3] = "экстернат";}*/

   //  abit_TMP.setOsnovaOb(rs.getString(34));
     abit_TMP.setSeriaAtt(rs.getString(27));
     abit_TMP.setNomerAtt(rs.getString(28));

// Чтение оценок абитуриента и сопутствующих им данных

   prdms = new ArrayList();

///   prdms.clear();

   stmt_a = conn.prepareStatement("SELECT zso.OtsenkaAtt,zso.OtsenkaEge,zso.OtsenkaZajavl,o.Otsenka,zso.Examen,zso.KodPredmeta FROM ZajavlennyeShkolnyeOtsenki zso, Otsenki o WHERE zso.KodAbiturienta=o.KodAbiturienta AND zso.KodPredmeta=o.KodPredmeta AND zso.KodAbiturienta LIKE ? AND o.KodPredmeta IN("+session.getAttribute("KodyPredmetov")+") ORDER BY zso.KodPredmeta");
   stmt_a.setObject(1,abit_TMP.getKodAbiturienta(),Types.INTEGER);
   rs_a = stmt_a.executeQuery();
   while(rs_a.next()) {
     AbiturientBean abit_TMP2 = new AbiturientBean();
     abit_TMP2.setOtsenka_Att(StringUtil.voidFilter(rs_a.getString(1)));
     abit_TMP2.setOtsenka_Ege(StringUtil.voidFilter(rs_a.getString(2)));
     abit_TMP2.setOtsenka_Zaj(StringUtil.voidFilter(rs_a.getString(3)));
     abit_TMP2.setOtsenka_Exam(StringUtil.voidFilter(rs_a.getString(4)));
     abit_TMP2.setExamen(StringUtil.voidFilter(rs_a.getString(5)));
     prdms.add(abit_TMP2);
   }
   abit_TMP.setNotes(prdms);
   
// Получение Аббревиатуры спец-ти по ее коду
   stmt_a = conn.prepareStatement("SELECT DISTINCT s.Abbreviatura FROM Abiturient a, Spetsialnosti s WHERE a.KodSpetsialnZach=s.KodSpetsialnosti AND a.KodVuza LIKE ? AND a.KodSpetsialnZach LIKE ?");
   stmt_a.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
// KodSpetsialnosti (Зачисления)
   stmt_a.setObject(2,rs.getString(29),Types.INTEGER);
   rs_a = stmt_a.executeQuery();
   if(rs_a.next())    abit_TMP.setSpecial8(rs_a.getString(1));
   else               abit_TMP.setSpecial8("");

                      abits_A.add(abit_TMP);
 } else {

// Прекращаем цикл выборки, если данных больше нет

          break;
   }

 }

// Выбираем только те предметы, которые были отмечены пользователем
// Список предметов и их кодов

   tmp="%";
   predms.clear();

   stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND KodPredmeta IN("+session.getAttribute("KodyPredmetov")+") ORDER BY KodPredmeta ASC");
   stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
   rs = stmt.executeQuery();
   while(rs.next())
    {
      AbiturientBean predm = new AbiturientBean();
      predm.setNazvaniePredmeta(rs.getString(1));
      predm.setPredmet(rs.getString(2));
      predms.add(predm);
      tmp += rs.getString(2) + "%";
    }
    abit_A.setSpecial10(tmp);

/* 
// Увеличиваем позицию на значение скроллинга = totalRows
    session.setAttribute("position",""+(StringUtil.toInt(""+session.getAttribute("position"),0)+totalRows));
*/
    stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM Abiturient WHERE Abiturient.KodVuza LIKE ?");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) abit_A.setPriznakSortirovki(rs.getString(1));
    session.setAttribute("total_All",""+totalCount); 
 }

// Строчки ниже нужны для вывода справочной информации при формировании групп

    String str = "";
    if( no_platn_dog ) str += "бюджетники ";
    if(str != "" && is_platn_dog) str+= "и ";
    if( is_platn_dog ) str += "договорники ";
    session.setAttribute("osnova_abits",str);

    stmt = conn.prepareStatement("SELECT KodFakulteta FROM Fakultety WHERE KodVuza LIKE ? AND ShifrFakulteta LIKE ?");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2,session.getAttribute("ShifrFakulteta"),Types.VARCHAR);
    rs = stmt.executeQuery();
    if(rs.next()) session.setAttribute("kFak",rs.getString(1));

    if(strFO[0] != null )  strFO[4] = ", " + strFO[0];
    if(strFO[1] != null )  strFO[4]+= ", " + strFO[1];
    if(strFO[2] != null )  strFO[4]+= ", " + strFO[2];
    if(strFO[3] != null )  strFO[4]+= ", " + strFO[3];

    if(strFO[4] !=null)
      session.setAttribute("forma_abits",strFO[4].substring(2));
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
        request.setAttribute("predms", predms);
        request.setAttribute("fgr_msgs", fgr_msgs);
        request.setAttribute("groups", groups);
        request.setAttribute("abit_forms", abit_forms);
        request.setAttribute("abit_osnovs", abit_osnovs);
        request.setAttribute("abit_A_S2", abit_A_S2);
        request.setAttribute("abit_A_S3", abit_A_S3);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S7", abit_A_S7);
        request.setAttribute("abit_A_S8", abit_A_S8);
        request.setAttribute("abit_A_S9", abit_A_S9);
   }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}