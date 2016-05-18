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

public class BallPotokKonGrpAction extends Action {

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
        int                 kol_abits[]       = new int[301];
        ArrayList           abits_TM          = new ArrayList();
        ArrayList           abit_TM_S1        = new ArrayList();
        ArrayList           mass_otsenki      = new ArrayList();
        UserBean            user              = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "ballPotokKonGrpAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "ballPotokKonGrpForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************************************************************************************************/
/****************** ВЫВОД АБИТУРИЕНТОВ ПО ВЫБРАННОМУ ПОТОКУ  ********************/
/************************************************************************************************/

       if(form.getAction()==null || form.getAction().equals("show")) { 
         form.setAction(us.getClientIntName("show","init"));

         session.setAttribute("nomPot",abit_TM.getNomerPotoka());
         stmt = conn.prepareStatement("SELECT DISTINCT Potok FROM Gruppy ORDER BY Potok ASC");
         rs = stmt.executeQuery();
         if(rs.next()) {
           AbiturientBean abit_TMP = new AbiturientBean();
           if(session.getAttribute("nomPot") == null) 
             session.setAttribute("nomPot",rs.getString(1));
           abit_TMP.setNomerPotoka(new Integer(rs.getInt(1)));
           abit_TM_S1.add(abit_TMP);
         }
         while(rs.next()) {
           AbiturientBean abit_TMP = new AbiturientBean();
           abit_TMP.setNomerPotoka(new Integer(rs.getInt(1)));
           abit_TM_S1.add(abit_TMP);
         }

         abit_TM.setNomerPotoka(new Integer(""+session.getAttribute("nomPot")));
         stmt_a = conn.prepareStatement("SELECT Nazvanie,KodKonGruppy FROM KonGruppa ORDER BY KodKonGruppy ASC");
         rs_a = stmt_a.executeQuery();
         while(rs_a.next()) {
           AbiturientBean tmp_ots = new AbiturientBean();

           for(int i=0;i<300;i++) kol_abits[i] = 0;

           stmt = conn.prepareStatement("SELECT SUM(CONVERT(int,zso.OtsenkaEge)),Familija,a.KodAbiturienta FROM EkzamenyNaSpetsialnosti ens,KonGruppa kg,Spetsialnosti s,Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Gruppy g WHERE ens.KodSpetsialnosti=a.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND g.KodGruppy=a.KodGruppy AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodKonGruppy=kg.KodKonGruppy AND g.Potok LIKE ? AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.Familija ORDER BY 1 DESC");
           stmt.setObject(1,abit_TM.getNomerPotoka(),Types.INTEGER);
           stmt.setObject(2,rs_a.getString(2),Types.INTEGER);
           rs = stmt.executeQuery();
           while(rs.next()){

              if(rs.getInt(1)<=300) kol_abits[rs.getInt(1)]+=1;
           }
           ArrayList list = new ArrayList();
           for(int i=300;i>=0;i--) {
              AbiturientBean tmp = new AbiturientBean();
              if(kol_abits[i] != 0) tmp.setSpecial1(""+kol_abits[i]);
              else tmp.setSpecial1("");
              list.add(tmp);
           }
           tmp_ots.setList(list);
           tmp_ots.setNazvanie(rs_a.getString(1));
           mass_otsenki.add(tmp_ots);
        }
      }

/*********************** Генерация отчета *************************************/

  if(form.getAction().equals("report"))
   {
/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Сводка по сумме баллов для потока № "+session.getAttribute("nomPot");

    String file_con = new String("sum_ball_pot_kg_"+session.getAttribute("nomPot"));

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
      report.write("\\fs32 Сводка по сумме баллов для потока № "+session.getAttribute("nomPot")+"\\par\n");

  for(int balls=300; balls>0; balls-=20) {

      report.write("\\fs28\\b\n");
      report.write("\\par\n");
      report.write("\\fs24 \\trowd \\trhdr \\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\cellx1600\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14200\n");
      report.write("\\qc");
      report.write("\\intbl\\b1 Конк. гр.\\cell\n");
      report.write("\\intbl Число абитуриентов, набравших соответствующее количество баллов\\cell\n");
      report.write("\\intbl\\b0\\row\n");

      report.write("\\fs22 \\trowd \\trhdr \\trqc\\trgaph108\\trrh280\\trleft36\n");

      report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1600\n");

      for(int i=2200;i<=14200;i+=600) {

         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+i+"\n");
      }
      report.write("\\intbl \\cell\\b1\n");

      for(int i=balls;i>=(balls-20) && i>=0;i--) {

         report.write("\\intbl "+i+"\\cell\n");
      }
      report.write("\\b0\\intbl\\row\n");


         stmt_a = conn.prepareStatement("SELECT Nazvanie,KodKonGruppy FROM KonGruppa ORDER BY KodKonGruppy ASC");
         rs_a = stmt_a.executeQuery();
         while(rs_a.next()) {
           AbiturientBean tmp_ots = new AbiturientBean();

           for(int i=balls;i>=(balls-20) && i>=0;i--) kol_abits[i] = 0;

           stmt = conn.prepareStatement("SELECT SUM(CONVERT(int,zso.OtsenkaEge)),Familija,a.KodAbiturienta FROM EkzamenyNaSpetsialnosti ens,KonGruppa kg,Spetsialnosti s,Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Gruppy g WHERE ens.KodSpetsialnosti=a.KodSpetsialnosti AND ens.KodPredmeta=zso.KodPredmeta AND g.KodGruppy=a.KodGruppy AND a.KodAbiturienta=zso.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodKonGruppy=kg.KodKonGruppy AND g.Potok LIKE ? AND kg.KodKonGruppy LIKE ? GROUP BY a.KodAbiturienta,a.Familija ORDER BY 1 DESC");
           stmt.setObject(1,session.getAttribute("nomPot"),Types.INTEGER);
           stmt.setObject(2,rs_a.getString(2),Types.INTEGER);
           rs = stmt.executeQuery();
           while(rs.next()){

             if(rs.getInt(1)<=balls && rs.getInt(1)>=(balls-20)) kol_abits[rs.getInt(1)]+=1;
           }

           report.write("\\fs22\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");

           for(int i=1600;i<=14200;i+=600) {
 
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+i+"\n");
           }
           report.write("\\intbl\\qc\\b1 "+rs_a.getString(1)+"\\b0\\cell\n");

           for(int i=balls;i>=(balls-20) && i>=0;i--) {

              report.write("\\intbl\\qc "+StringUtil.ztv(""+kol_abits[i])+"\\cell\n"); // количество абитуриентов
           }
           report.write("\\intbl\\row\n");
        }
      report.write("\\pard\n");
  }
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
        request.setAttribute("abit_TM_S1", abit_TM_S1);
        request.setAttribute("mass_otsenki", mass_otsenki);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}