package abit.action;

import java.io.IOException;
import java.io.*;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;
import java.util.Enumeration;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.util.*;
import abit.Constants;
import abit.sql.*;

public class AnalizKgAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession         session           = request.getSession();
        Connection          conn              = null;
        PreparedStatement   stmt              = null;
        ResultSet           rs                = null;
        PreparedStatement   stmt_a            = null;
        ResultSet           rs_a              = null;
        ActionErrors        errors            = new ActionErrors();
        ActionError         msg               = null;
        BallPotokForm       form              = (BallPotokForm) actionForm;
        AbiturientBean      abit_TM           = form.getBean(request, errors);
        boolean             error             = false;
        ActionForward       f                 = null;
        int                 kol[]             = new int[401];
        int                 counter           = 0;
        float               konk              = 0;
        ArrayList           abits_TM          = new ArrayList();
        ArrayList           abit_A_S1         = new ArrayList();
        ArrayList           abit_A_S2         = new ArrayList();
        ArrayList           abit_A_S3         = new ArrayList();
        ArrayList           abit_A_S4         = new ArrayList();
        ArrayList           abit_A_S5         = new ArrayList();
        ArrayList           mass_otsenki      = new ArrayList();
        UserBean            user              = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "analizKgAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "analizKgForm", form );

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

/************************************************************************************************/
/*********************   ВЫВОД АБИТУРИЕНТОВ ПО ВЫБРАННОЙ КОНКУРСНОЙ ГРУППЕ   ********************/
/************************************************************************************************/

       if(form.getAction()==null || form.getAction().equals("show")) { 
         form.setAction(us.getClientIntName("show","init"));

// Всего абитуриентов в конкурсной группе

         stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodKonGruppy LIKE ?");
         stmt.setObject(1,abit_TM.getNomerPotoka(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) abit_TM.setAmount(StringUtil.toInt(rs.getString(1),0));
         else abit_TM.setAmount(0);

// План приема

         stmt = conn.prepareStatement("SELECT SUM(s.PlanPriema) FROM Spetsialnosti s WHERE s.KodKonGruppy LIKE ?");
         stmt.setObject(1,abit_TM.getNomerPotoka(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) abit_TM.setPlanPriema(new Integer(""+StringUtil.toInt(rs.getString(1),0)));
         else abit_TM.setPlanPriema(new Integer("-"));

// Конкурс

         konk = Math.round((float)(abit_TM.getAmount())/(float)(StringUtil.toInt(""+abit_TM.getPlanPriema(),0))*100f)/100f;

         abit_TM.setSpecial5(""+konk);

// Целевой прием 

         stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s, TselevojPriem tp WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodTselevogoPriema=tp.KodTselevogoPriema AND tp.ShifrPriema NOT LIKE 'н' AND s.KodKonGruppy LIKE ?");
         stmt.setObject(1,abit_TM.getNomerPotoka(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) abit_TM.setTselevojPriem(""+StringUtil.toInt(rs.getString(1),0));
         else abit_TM.setTselevojPriem("-");

// Льготники (с оценками /кроме тех, кто набрал 0,1 и 2/)

         counter = 0;
         stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM ZajavlennyeShkolnyeOtsenki zso WHERE zso.KodAbiturienta IN (SELECT KodAbiturienta FROM Abiturient a, Spetsialnosti s, Lgoty l WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodLgot=l.KodLgot AND l.ShifrLgot NOT LIKE 'н' AND s.KodKonGruppy LIKE ?) AND zso.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM ZajavlennyeShkolnyeOtsenki WHERE CONVERT(int,OtsenkaEge)<3 AND OtsenkaEge NOT LIKE '-') GROUP BY zso.KodAbiturienta");
         stmt.setObject(1,abit_TM.getNomerPotoka(),Types.INTEGER);
         rs = stmt.executeQuery();
         while(rs.next()) counter++;
         if(counter!=0) abit_TM.setShifrLgot(""+counter);
         else abit_TM.setShifrLgot("-");

// Медалисты (подтвержденные)

         stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s, Medali m WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodMedali=m.KodMedali AND m.ShifrMedali NOT LIKE 'н' AND a.PodtvMed LIKE 'д' AND s.KodKonGruppy LIKE ?");
         stmt.setObject(1,abit_TM.getNomerPotoka(),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) abit_TM.setShifrMedali(""+StringUtil.toInt(rs.getString(1),0));
         else abit_TM.setShifrMedali("-");

         abit_TM.setSpecial1(""+(StringUtil.toInt(""+abit_TM.getPlanPriema(),0)-StringUtil.toInt(abit_TM.getTselevojPriem(),0)-StringUtil.toInt(abit_TM.getShifrLgot(),0)-StringUtil.toInt(abit_TM.getShifrMedali(),0)));

// Оценочки

         stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM Abiturient a, Spetsialnosti s,ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND zso.KodAbiturienta=a.KodAbiturienta AND s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta ORDER BY 2 DESC");
         stmt.setObject(1,abit_TM.getNomerPotoka(),Types.INTEGER);
         rs = stmt.executeQuery();
         while(rs.next()) { 
            if(rs.getInt(2)<=kol.length) kol[rs.getInt(2)]++;
         }

         int amount = StringUtil.toInt(abit_TM.getSpecial1(),0);

         for(int ball=kol.length-1;ball>=0;ball--){

            if(kol[ball]!=0) {
              AbiturientBean abit_TMP = new AbiturientBean();
//балл
              abit_TMP.setSpecial2(""+ball);

              abit_TMP.setSpecial3(""+kol[ball]);
//вакансии
              amount -= kol[ball];

              abit_TMP.setSpecial4(""+amount);

              abits_TM.add(abit_TMP);

            } else continue;
         }
      }

/*****************************************************************************/
/**************************** Генерация отчета *******************************/
/*****************************************************************************/

  if(form.getAction().equals("report"))
   {
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    stmt = conn.prepareStatement("SELECT Nazvanie FROM KonGruppa WHERE KodKonGruppy LIKE ?");
    stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) 
      abit_TM.setSokr(rs.getString(1));

    String name = "Аналитика для кон.гр. №"+abit_TM.getSokr()+" на "+StringUtil.CurrDate(".");

    String file_con = new String("analiz_kg_"+abit_TM.getSokr()+StringUtil.CurrDate("_"));

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

      report.write("{\\rtf1\\ansi\n");
      report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

      stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) 
        report.write("\\pard\\fs40\\b1\\qc "+rs.getString(1)+"\\par\n");
      report.write("\\par\n");
      report.write("\\fs28\\b1 Аналитический прогноз для конкурсной группы № "+abit_TM.getSokr()+" от "+StringUtil.CurrDate(".")+"\\b0\\par\n");

      report.write("\\fs28\\b\\par\\ql\n");

// Всего абитуриентов в конкурсной группе

      stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodKonGruppy LIKE ?");
      stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) abit_TM.setAmount(StringUtil.toInt(rs.getString(1),0));
      else abit_TM.setAmount(0);

      report.write("\\fs24\\b0\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Подано заявлений:}\\tab\\b1{"+abit_TM.getAmount()+"}\\b0\\par\n");

// План приема

      stmt = conn.prepareStatement("SELECT SUM(s.PlanPriema) FROM Spetsialnosti s WHERE s.KodKonGruppy LIKE ?");
      stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) abit_TM.setPlanPriema(new Integer(""+StringUtil.toInt(rs.getString(1),0)));
      else abit_TM.setPlanPriema(new Integer("-"));

      report.write("\\fs24\\b0\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{План приема:}\\tab\\tab\\b1{"+abit_TM.getPlanPriema()+"}\\b0\\par\n");

// Конкурс

      konk = Math.round((float)(abit_TM.getAmount())/(float)(StringUtil.toInt(""+abit_TM.getPlanPriema(),0))*100f)/100f;

      report.write("\\fs24\\b0\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Конкурс:}\\tab\\tab\\b1{"+konk+"}\\b0\\par\n");

// Целевой прием 

      stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s, TselevojPriem tp WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodTselevogoPriema=tp.KodTselevogoPriema AND tp.ShifrPriema NOT LIKE 'н' AND s.KodKonGruppy LIKE ?");
      stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) abit_TM.setTselevojPriem(""+StringUtil.toInt(rs.getString(1),0));
      else abit_TM.setTselevojPriem("-");

      report.write("\\fs24\\b0\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Целевой прием:}\\tab\\b1{"+abit_TM.getTselevojPriem()+"}\\b0\\par\n");

// Льготники (с оценками /надо бы с баллами >2, но пока так/)

      counter = 0;
      stmt = conn.prepareStatement("SELECT COUNT(KodAbiturienta) FROM ZajavlennyeShkolnyeOtsenki zso WHERE zso.KodAbiturienta IN (SELECT KodAbiturienta FROM Abiturient a, Spetsialnosti s, Lgoty l WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodLgot=l.KodLgot AND l.ShifrLgot NOT LIKE 'н' AND s.KodKonGruppy LIKE ?) AND zso.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM ZajavlennyeShkolnyeOtsenki WHERE CONVERT(int,OtsenkaEge)<3 AND OtsenkaEge NOT LIKE '-') GROUP BY zso.KodAbiturienta");
      stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) counter++;
      if(counter!=0) abit_TM.setShifrLgot(""+counter);
      else abit_TM.setShifrLgot("-");
// Льготники (с оценками /кроме тех, кто набрал 0,1 и 2/)

      report.write("\\fs24\\b0\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Льготники:}\\tab\\tab\\b1{"+abit_TM.getShifrLgot()+"}\\b0\\par\\par\n");

// Медалисты (подтвержденные)

      stmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a, Spetsialnosti s, Medali m WHERE s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodMedali=m.KodMedali AND m.ShifrMedali NOT LIKE 'н' AND a.PodtvMed LIKE 'д' AND s.KodKonGruppy LIKE ?");
      stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) abit_TM.setShifrMedali(""+StringUtil.toInt(rs.getString(1),0));
      else abit_TM.setShifrMedali("-");

      abit_TM.setSpecial1(""+(StringUtil.toInt(""+abit_TM.getPlanPriema(),0)-StringUtil.toInt(abit_TM.getTselevojPriem(),0)-StringUtil.toInt(abit_TM.getShifrLgot(),0)-StringUtil.toInt(abit_TM.getShifrMedali(),0)));


      report.write("\\fs24 \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr\n");
      report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
      report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
      report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
      report.write("\\intbl\\qc\\b1{Баллы}\\cell\n");
      report.write("\\intbl{Кол-во}\\cell\n");
      report.write("\\intbl{Вакансии}\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\fs24 \\trowd \\trqc\\trgaph108\\trrh380\\trleft36\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");

      report.write("\\intbl\\qc{Медаль}\\cell\n");
      report.write("\\intbl{"+abit_TM.getShifrMedali()+"}\\cell\n");
      report.write("\\intbl{"+abit_TM.getSpecial1()+"}\\cell\n");
      report.write("\\intbl\\row\n");

// Оценочки

      stmt = conn.prepareStatement("SELECT a.KodAbiturienta,SUM(CONVERT(int,zso.OtsenkaEge)) FROM Abiturient a, Spetsialnosti s,ZajavlennyeShkolnyeOtsenki zso,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=zso.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND zso.KodAbiturienta=a.KodAbiturienta AND s.KodSpetsialnosti=a.KodSpetsialnosti AND s.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta ORDER BY 2 DESC");
      stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next()) { 
         if(rs.getInt(2)<=kol.length) kol[rs.getInt(2)]++;
      }
      int amount = StringUtil.toInt(abit_TM.getSpecial1(),0);

      for(int ball=kol.length-1;ball>=0;ball--){

         if(kol[ball]!=0) {

           amount -= kol[ball];

           report.write("\\intbl\\qc{"+ball+"}\\cell\n");
           report.write("\\intbl{"+kol[ball]+"}\\cell\n");
           report.write("\\intbl{"+amount+"}\\cell\n");
           report.write("\\intbl\\row\n");

           if(amount<0) break;

         } else continue;
      }


      report.write("\\pard\\par\n");
      report.write("}");
      report.close();

      form.setAction(us.getClientIntName("new_rep","crt"));
      return mapping.findForward("rep_brw");
                }

/******************************************************************************/
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
        request.setAttribute("abit_TM", abit_TM);
        request.setAttribute("abits_TM", abits_TM);
        request.setAttribute("abit_A_S1", abit_A_S1);
        request.setAttribute("abit_A_S2", abit_A_S2);
        request.setAttribute("abit_A_S3", abit_A_S3);
        request.setAttribute("abit_A_S4", abit_A_S4);
        request.setAttribute("abit_A_S5", abit_A_S5);
        request.setAttribute("mass_otsenki", mass_otsenki);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}