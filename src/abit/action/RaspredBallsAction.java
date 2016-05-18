package abit.action;

import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Locale;
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

public class RaspredBallsAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        PreparedStatement stmt1         = null;
        ResultSet         rs            = null;
        ResultSet         rs1           = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        ZchForm           form          = (ZchForm) actionForm;
        AbiturientBean    abit_A        = form.getBean(request, errors);
        boolean           error         = false;
        ActionForward     f             = null;
        String            kRajona       = null;
        String            kOblasti      = null;
        Integer           kAbiturienta  = null;
        ArrayList         cells         = new ArrayList();
        ArrayList         balls         = new ArrayList();
        ArrayList         kolls         = new ArrayList();
        ArrayList         abit_A_S1     = new ArrayList();
        ArrayList         abit_A_S2     = new ArrayList();
        ArrayList         abit_A_S3     = new ArrayList();
        ArrayList         abit_A_S4     = new ArrayList();
        ArrayList         abit_A_S5     = new ArrayList();
        int               cells_o[]     = new int[1];
        int               maxAbits      = 0;
        int               cur_count     = 0;
        int               oldBall       = -1;
        int               minExBall     = 0;
        int               maxExBall     = 0;
        float             konk          = 0;
        UserBean          user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "raspredBallsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "raspredBallsForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/*********  специальности  **********/
            stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,Abbreviatura FROM Spetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setSpecial3(rs.getString(1));
              abit_TMP.setAbbreviatura(rs.getString(2));
              abit_A_S5.add(abit_TMP);
            } 

System.out.println(">>Hist: 1");
// ****************** Конкурсные группы **********************

            stmt = conn.prepareStatement("SELECT DISTINCT KodKonGruppy,Nazvanie FROM KonGruppa WHERE KodVuza LIKE ? ORDER BY 1 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodKonGrp(new Integer(rs.getString(1)));
              abit_TMP.setNazvanie(rs.getString(2));
              abit_A_S4.add(abit_TMP);
            }
System.out.println(">>Hist: 2");
// ****************** Факультеты **********************

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setShifrFakulteta(rs.getString(1));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_A_S2.add(abit_TMP);
            }
System.out.println(">>Hist: 3");
// ****************** специальности на факультете ****************************
            boolean save = false;
            String oldKodFak = "";
            String kodeline = "";
            stmt = conn.prepareStatement("SELECT DISTINCT s.KodFakulteta,s.KodSpetsialnosti,s.Abbreviatura FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND KodVuza LIKE ? ORDER BY s.KodFakulteta,s.Abbreviatura ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
             if(!rs.getString(1).equals(oldKodFak)) {
               if(save) {
                 AbiturientBean tmp1 = new AbiturientBean();
                 tmp1.setKodFakulteta(new Integer(oldKodFak));
                 tmp1.setSpecial1(kodeline);
// KodFakulteta хранит Код факультета, к которому относятся специальности
// Special1 имеет формат списка: KodFakulteta%Spec1%KodSpec1%Spec2%KodSpec2%Spec3%KodSpec3...
                 abit_A_S3.add(tmp1);
               }
               oldKodFak = rs.getString(1);
               kodeline = rs.getString(3)+"%"+rs.getString(2);
               save = true;
             } else kodeline += "%"+rs.getString(3)+"%"+rs.getString(2);
            }
// Запись последней строки в массив кодов и специальностей
            AbiturientBean tmp2 = new AbiturientBean();
            tmp2.setKodFakulteta(new Integer(oldKodFak));
            tmp2.setSpecial1(kodeline);
            abit_A_S3.add(tmp2);
System.out.println(">>Hist: 4");

// ****************** группы на факультете ****************************
            save = false;
            oldKodFak = "";
            kodeline = "";
            boolean groups_exists = false;
            stmt = conn.prepareStatement("SELECT DISTINCT g.KodFakulteta,g.KodGruppy,g.Gruppa FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND KodVuza LIKE ? ORDER BY g.KodFakulteta,g.Gruppa ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
             groups_exists = true;
             if(!rs.getString(1).equals(oldKodFak)) {
               if(save) {
                 AbiturientBean tmp = new AbiturientBean();
                 tmp.setKodGruppy(new Integer(oldKodFak));
                 tmp.setSpecial4(kodeline);
// KodGruppy хранит Код Факультета, к которому относятся группы
// Special4 имеет формат списка: KodFakulteta%Gruppa1%KodGruppy1%Gruppa2%KodGruppy2%Gruppa3%KodGruppy3...
                 abit_A_S1.add(tmp);
               }
               oldKodFak = rs.getString(1);
               kodeline = rs.getString(3)+"%"+rs.getString(2);
               save = true;
             } else kodeline += "%"+rs.getString(3)+"%"+rs.getString(2);
            }

            if(groups_exists) {
// Запись последней строки в массив кодов и групп
              AbiturientBean tmp = new AbiturientBean();
              tmp.setKodGruppy(new Integer(oldKodFak));
              tmp.setSpecial4(kodeline);
              abit_A_S1.add(tmp);
            }
System.out.println(">>Hist: 5");
/********************** Подготовка данных для ввода с помощью селекторов ************************/
          if(form.getAction() == null ) {

            form.setAction(us.getClientIntName("histogramm","init"));
/************************************************************************************************/
          } else if( form.getAction().equals("draw")) {

              if(!((""+abit_A.getKodKonGrp()).equals("-1"))) {

/********************************/
/***** По конкурсной группе *****/
/********************************/
System.out.println(">>Hist: 6");
                stmt = conn.prepareStatement("SELECT Nazvanie FROM KonGruppa WHERE KodKonGruppy LIKE ?");
                stmt.setObject(1,abit_A.getKodKonGrp(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setNazvanie("Для конкурсной группы №"+rs.getString(1));

// Всего абитуриентов в конкурсной группе

                stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodKonGruppy LIKE ?");
                stmt.setObject(1,abit_A.getKodKonGrp(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setAmount(StringUtil.toInt(rs.getString(1),0));
                else abit_A.setAmount(0);
System.out.println(">>Hist: 7");
// План приема
                stmt = conn.prepareStatement("SELECT SUM(s.PlanPriema) FROM Spetsialnosti s WHERE s.KodKonGruppy LIKE ?");
                stmt.setObject(1,abit_A.getKodKonGrp(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setPlanPriema(new Integer(""+StringUtil.toInt(rs.getString(1),0)));
                else abit_A.setPlanPriema(new Integer("-"));

// Конкурс
                konk = Math.round((float)(abit_A.getAmount())/(float)(StringUtil.toInt(""+abit_A.getPlanPriema(),0))*100f)/100f;
                abit_A.setSpecial6(""+konk);

// Максимальное количество баллов

                stmt = conn.prepareStatement("SELECT COUNT(DISTINCT ens.KodPredmeta) FROM KonGruppa kg,Spetsialnosti sp,EkzamenyNaSpetsialnosti ens WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ens.KodSpetsialnosti=sp.KodSpetsialnosti AND kg.KodVuza LIKE ? AND kg.KodKonGruppy LIKE ?");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodKonGrp(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) maxExBall = StringUtil.toInt(rs.getString(1),0)*Constants.maxExBall;

// Максимальное количество абитуриентов, набравших один и тот же балл

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s,Abiturient a,KonGruppa kg,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND zso.OtsenkaEge NOT LIKE '-' AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodKonGrp(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   if(rs.getInt(2) != oldBall) {
                     if(cur_count > maxAbits) maxAbits = cur_count;
                     cur_count = 1;
                     oldBall = rs.getInt(2);
                   } else {
                       cur_count++;
                   }
                }

                cells_o = new int[maxExBall+1];

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso, Spetsialnosti s,Abiturient a,KonGruppa kg,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti=a.KodSpetsialnosti AND zso.OtsenkaEge NOT LIKE '-' AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodKonGrp(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   cells_o[StringUtil.toInt(rs.getString(2),0)]++;
                }

              } else {

              if(!((""+abit_A.getKodGruppy()).equals("-1") || (""+abit_A.getKodGruppy()).equals("0"))) {

/********************************/
/********** По группе ***********/
/********************************/

                stmt = conn.prepareStatement("SELECT Gruppa FROM Gruppy WHERE KodGruppy LIKE ?");
                stmt.setObject(1,abit_A.getKodGruppy(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setNazvanie("Для группы ''"+rs.getString(1).toUpperCase()+"''");

                abit_A.setPlanPriema(new Integer("0"));
                abit_A.setSpecial6("-");

// Максимальное количество баллов

                stmt = conn.prepareStatement("SELECT COUNT(DISTINCT ens.KodPredmeta) FROM Abiturient a,Gruppy g,EkzamenyNaSpetsialnosti ens WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=ens.KodSpetsialnosti AND a.KodVuza LIKE ? AND g.KodGruppy LIKE ?");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodGruppy(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) maxExBall = StringUtil.toInt(rs.getString(1),0)*Constants.maxExBall;

// Максимальное количество абитуриентов, набравших один и тот же балл

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso,Abiturient a,Gruppy g,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND a.KodGruppy=g.KodGruppy AND zso.OtsenkaEge NOT LIKE '-' AND g.KodGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodGruppy(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   if(rs.getInt(2) != oldBall) {
                     if(cur_count > maxAbits) maxAbits = cur_count;
                     cur_count = 1;
                     oldBall = rs.getInt(2);
                   } else {
                       cur_count++;
                   }
                }

                cells_o = new int[maxExBall+1];

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso,Abiturient a,Gruppy g,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND a.KodGruppy=g.KodGruppy AND zso.OtsenkaEge NOT LIKE '-' AND g.KodGruppy LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodGruppy(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   cells_o[StringUtil.toInt(rs.getString(2),0)]++;
                }

              } else if(!((""+abit_A.getKodFakulteta()).equals("-1") || (""+abit_A.getKodFakulteta()).equals("0"))) {

/********************************/
/******* По специальности *******/
/********************************/

                stmt = conn.prepareStatement("SELECT Abbreviatura,ShifrSpetsialnostiOKSO FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
                stmt.setObject(1,abit_A.getKodFakulteta(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setNazvanie("Для специальности ''"+rs.getString(1).toUpperCase()+"("+rs.getString(2)+")''");

// Всего абитуриентов на специальности

                stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodSpetsialnosti LIKE ?");
                stmt.setObject(1,abit_A.getKodFakulteta(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setAmount(StringUtil.toInt(rs.getString(1),0));
                else abit_A.setAmount(0);

// План приема
                stmt = conn.prepareStatement("SELECT SUM(s.PlanPriema) FROM Spetsialnosti s WHERE s.KodSpetsialnosti LIKE ?");
                stmt.setObject(1,abit_A.getKodFakulteta(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setPlanPriema(new Integer(""+StringUtil.toInt(rs.getString(1),0)));
                else abit_A.setPlanPriema(new Integer("-"));

// Конкурс
                konk = Math.round((float)(abit_A.getAmount())/(float)(StringUtil.toInt(""+abit_A.getPlanPriema(),0))*100f)/100f;
                abit_A.setSpecial6(""+konk);

// Максимальное количество баллов

                stmt = conn.prepareStatement("SELECT COUNT(DISTINCT ens.KodPredmeta) FROM Spetsialnosti sp, Fakultety f, EkzamenyNaSpetsialnosti ens WHERE f.KodFakulteta=sp.KodFakulteta AND ens.KodSpetsialnosti=sp.KodSpetsialnosti AND f.KodVuza LIKE ? AND ens.KodSpetsialnosti LIKE ?");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodFakulteta(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) maxExBall = StringUtil.toInt(rs.getString(1),0)*Constants.maxExBall;

// Максимальное количество абитуриентов, набравших один и тот же балл

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso,Abiturient a,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND zso.OtsenkaEge NOT LIKE '-' AND a.KodSpetsialnosti LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodFakulteta(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   if(rs.getInt(2) != oldBall) {
                     if(cur_count > maxAbits) maxAbits = cur_count;
                     cur_count = 1;
                     oldBall = rs.getInt(2);
                   } else {
                       cur_count++;
                   }
                }

                cells_o = new int[maxExBall+1];

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso,Abiturient a,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND zso.OtsenkaEge NOT LIKE '-' AND a.KodSpetsialnosti LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getKodFakulteta(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   cells_o[StringUtil.toInt(rs.getString(2),0)]++;
                }

              } else if(!(abit_A.getSpecial2().equals("-1"))) {

/********************************/
/******** По факультету *********/
/********************************/

                stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ?");
                stmt.setObject(1,abit_A.getSpecial2(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setNazvanie("Для факультета ''"+rs.getString(1).toUpperCase()+"''");

// Всего абитуриентов в конкурсной группе

                stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodFakulteta LIKE ?");
                stmt.setObject(1,abit_A.getSpecial2(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setAmount(StringUtil.toInt(rs.getString(1),0));
                else abit_A.setAmount(0);

// План приема
                stmt = conn.prepareStatement("SELECT SUM(s.PlanPriema) FROM Spetsialnosti s WHERE s.KodFakulteta LIKE ?");
                stmt.setObject(1,abit_A.getSpecial2(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) abit_A.setPlanPriema(new Integer(""+StringUtil.toInt(rs.getString(1),0)));
                else abit_A.setPlanPriema(new Integer("-"));

// Конкурс
                konk = Math.round((float)(abit_A.getAmount())/(float)(StringUtil.toInt(""+abit_A.getPlanPriema(),0))*100f)/100f;
                abit_A.setSpecial6(""+konk);

// Максимальное количество баллов

                stmt = conn.prepareStatement("SELECT COUNT(DISTINCT ens.KodPredmeta) FROM Fakultety f,Spetsialnosti sp,EkzamenyNaSpetsialnosti ens WHERE f.KodFakulteta=sp.KodFakulteta AND ens.KodSpetsialnosti=sp.KodSpetsialnosti AND f.KodVuza LIKE ? AND f.KodFakulteta LIKE ?");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getSpecial2(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) maxExBall = StringUtil.toInt(rs.getString(1),0)*Constants.maxExBall;

// Максимальное количество абитуриентов, набравших один и тот же балл

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso,Abiturient a,Spetsialnosti sp,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND sp.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND zso.OtsenkaEge NOT LIKE '-' AND sp.KodFakulteta LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getSpecial2(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   if(rs.getInt(2) != oldBall) {
                     if(cur_count > maxAbits) maxAbits = cur_count;
                     cur_count = 1;
                     oldBall = rs.getInt(2);
                   } else {
                       cur_count++;
                   }
                }

                cells_o = new int[maxExBall+1];

                stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM ZajavlennyeShkolnyeOtsenki zso,Abiturient a,Spetsialnosti sp,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=a.KodSpetsialnosti AND sp.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodAbiturienta = zso.KodAbiturienta AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND zso.OtsenkaEge NOT LIKE '-' AND sp.KodFakulteta LIKE ? GROUP BY a.KodAbiturienta,a.NomerLichnogoDela ORDER BY 2 DESC");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_A.getSpecial2(),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()) {
                   cells_o[StringUtil.toInt(rs.getString(2),0)]++;
                }

              }}

// Устанавливаем высоту столбца для одного человека, чтобы гистограмма уместилась на экране

                if(maxAbits == 0) {}
                else if(maxAbits < 40) abit_A.setSpecial3("5");
                else if(maxAbits < 60) abit_A.setSpecial3("4");
                else if(maxAbits < 70) abit_A.setSpecial3("3");
                else if(maxAbits < 80) abit_A.setSpecial3("3");
                else abit_A.setSpecial3("2");

// Построение гистограммы выполняется путем построчного формирования матрицы ячеек, начиная
// с ячеек самой нижней строки и поднимаясь построчно вверх. Число строк = maxAbits, столбцов = maxExBall

              for(int j=maxExBall;j>=0;j--) {
// Исключаем пустые столбцы
                if(cells_o[j]!=0) {
                  balls.add(""+j);
                  if(cells_o[j]!=0) kolls.add(""+cells_o[j]);
                  else kolls.add("");
                }
              }

              for(int i=maxAbits-1;i>=0;i--) {
                ArrayList row  = new ArrayList();
                for(int j=maxExBall;j>=0;j--) {
                    HistogrammBean cell = new HistogrammBean();
                  if((i+1-cells_o[j]) <= 0) {
                    cell.setColor("red");
                  } else {
                    cell.setColor("white");
                  }
// Исключаем пустые столбцы
                    if(cells_o[j]!=0) row.add(cell);
                }
                cells.add(row);
              }

              form.setAction(us.getClientIntName("histogramm","act"));
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
        request.setAttribute("cells", cells);
        request.setAttribute("balls", balls);
        request.setAttribute("kolls", kolls);
        request.setAttribute("abit_A_S1", abit_A_S1);
        request.setAttribute("abit_A_S2", abit_A_S2);
        request.setAttribute("abit_A_S3", abit_A_S3);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S5", abit_A_S5);
     }
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}