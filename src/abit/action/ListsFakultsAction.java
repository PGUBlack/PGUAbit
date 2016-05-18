package abit.action;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Enumeration;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import java.util.Date;
import java.io.*;
import abit.sql.*; 

public class ListsFakultsAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    stmt               = null;
        ResultSet            rs                 = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsFakultsForm     form               = (ListsFakultsForm) actionForm;
        AbiturientBean       abit_O             = form.getBean(request, errors);
        boolean              blank_f            = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_O            = new ArrayList();
        ArrayList            abit_O_S1          = new ArrayList();
        ArrayList            abit_O_S2          = new ArrayList();
        ArrayList            abit_O_S3          = new ArrayList();
        String               file_con           = new String();
        String               name               = new String();
        String               KodFakults         = new String();
        String               Fakultet           = new String();
        int                  i                  = 0;
        int                  j                  = 0;
        int                  n                  = 0;
        ArrayList            KGRP               = new ArrayList();
        String               S                  = new String();
        UserBean             user               = (UserBean)session.getAttribute("user");
        boolean              reportdo           = true;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsFakultsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsFakultsForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

  if ( form.getAction() == null ) form.setAction(us.getClientIntName("view","init"));
  
  if ( form.getAction().equals("view") ) {

/************************************************************************************************/
/********************* Подготовка данных для ввода с помощью селекторов *************************/

  int kFak=-1;
  String buffer="",oldAbbr="";
  stmt = conn.prepareStatement("SELECT DISTINCT Fakultety.AbbreviaturaFakulteta,EkzamenyNaSpetsialnosti.KodPredmeta,Fakultety.KodFakulteta FROM Fakultety,Spetsialnosti,EkzamenyNaSpetsialnosti WHERE Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND Fakultety.KodVuza LIKE ? AND Fakultety.KodFakulteta =  Spetsialnosti.KodFakulteta ORDER BY 1 ASC");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
      AbiturientBean abit_TMP = new AbiturientBean();
      if(kFak == rs.getInt(3)){
        buffer += "%"+rs.getString(2);
        oldAbbr = rs.getString(1);
      }
      else {
        if(kFak != -1) {
          buffer += "% "+kFak;
          abit_TMP.setGruppa(oldAbbr);
          abit_TMP.setSpecial7(buffer);
          abit_O_S3.add(abit_TMP);
        }
        kFak = rs.getInt(3);
        buffer = rs.getString(2);
      }
  }
  AbiturientBean abit_TMP22 = new AbiturientBean();     
  abit_TMP22.setGruppa(oldAbbr);
  abit_TMP22.setSpecial7(buffer+"% "+kFak);
  abit_O_S3.add(abit_TMP22);

  stmt = conn.prepareStatement("SELECT KodPredmeta,Predmet FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY 1 ASC");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while (rs.next()) {
    AbiturientBean abit_TMP = new AbiturientBean();
    abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
    abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
    abit_O_S1.add(abit_TMP);
  }

  stmt = conn.prepareStatement("SELECT DISTINCT ens.KodPredmeta,rs.DataJekzamena,f.KodFakulteta FROM Fakultety f,Spetsialnosti s,EkzamenyNaSpetsialnosti ens,NazvanijaPredmetov np,Raspisanie rs,Gruppy g WHERE g.KodFakulteta=f.KodFakulteta AND g.KodGruppy=rs.KodGruppy AND rs.KodPredmeta=np.KodPredmeta AND np.KodPredmeta=ens.KodPredmeta AND s.KodSpetsialnosti = ens.KodSpetsialnosti  AND f.KodFakulteta=s.KodFakulteta AND f.KodVuza LIKE ? ORDER BY f.KodFakulteta ASC");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()) {
    AbiturientBean abit_TMP2 = new AbiturientBean();
    abit_TMP2.setSpecial1(rs.getString(1)+"%"+rs.getString(3)+"%"+StringUtil.data_toApp(rs.getString(2)));
    abit_TMP2.setDataJekzamena(StringUtil.data_toApp(rs.getString(2)));
    abit_O_S2.add(abit_TMP2);
  } 

} else if ( form.getAction().equals("report")) {
/*************************** Формирование отчета ****************************/

    KodFakults = abit_O.getSpecial7().substring(abit_O.getSpecial7().indexOf(' ')+1);

    stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ?");
    stmt.setObject(1,KodFakults,Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) {
      file_con="lists_fakults_"+StringUtil.toEng(rs.getString(1))+"_"+abit_O.getKodPredmeta()+"_"+abit_O.getSpecial1().substring(abit_O.getSpecial1().lastIndexOf('%')+1);
        Fakultet = rs.getString(1);
    }
    else 
      file_con="lists_fakults";

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    stmt = conn.prepareStatement("SELECT Datelnyj,Sokr FROM NazvanijaPredmetov WHERE KodPredmeta LIKE ?");
    stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) 
        name = "Список аб. "+Fakultet.toUpperCase()+", сдавших экз. по "+rs.getString(2)+" от "+abit_O.getSpecial1().substring(abit_O.getSpecial1().lastIndexOf('%')+1);
    else
        name = "Список аб. "+Fakultet.toUpperCase()+", сдавших экзамен";

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

    report.write("{\\rtf1\\ansi\n");
    stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) 
    report.write("\\fs32 \\b \\qc "+rs.getString(1)+"\n");
    report.write("\\par\\par\\fs28 Список абитуриентов ");

    report.write(Fakultet.toUpperCase()+", сдавших экзамен по ");

    stmt = conn.prepareStatement("SELECT Datelnyj,Sokr FROM NazvanijaPredmetov WHERE KodPredmeta LIKE ?");
    stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) 
      report.write(rs.getString(1));

    report.write("\\parДата экзамена: "+abit_O.getSpecial1().substring(abit_O.getSpecial1().lastIndexOf('%')+1));
    report.write("\\fs22\\par\\par \\trowd \\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx700\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2100\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx4100\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx5800\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx7800\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx9000\n");
    report.write("\\intbl № \\cell\n");
    report.write("\\intbl НОМЕР ЛИЧНОГО ДЕЛА \\cell\n");
    report.write("\\intbl ФАМИЛИЯ \\cell\n");
    report.write("\\intbl ИМЯ \\cell\n");
    report.write("\\intbl ОТЧЕСТВО \\cell\n");
    report.write("\\intbl ОЦЕНКА \\cell\n");
    report.write("\\intbl\\b0 \\row\n");

    report.write("\\fs24\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx700\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2100\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx4100\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx5800\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx7800\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx9000\n");

//    stmt = conn.prepareStatement("SELECT o.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,zso.Otsenka FROM Otsenki o,Spetsialnosti s,Abiturient a WHERE a.KodVuza LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE ? AND o.KodAbiturienta=a.KodAbiturienta AND o.KodPredmeta LIKE ? AND a.KodGruppy IN (SELECT DISTINCT KodGruppy FROM Raspisanie WHERE DataJekzamena LIKE ? AND KodPredmeta LIKE ?) AND o.Otsenka NOT LIKE '-' ORDER BY 3,4,5 ASC");
    stmt = conn.prepareStatement("SELECT zso.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,zso.OtsenkaEge FROM ZajavlennyeShkolnyeOtsenki zso,Spetsialnosti s,Abiturient a WHERE a.KodVuza LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE ? AND zso.KodAbiturienta=a.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodGruppy IN (SELECT DISTINCT KodGruppy FROM Raspisanie WHERE DataJekzamena LIKE ? AND KodPredmeta LIKE ?) AND zso.OtsenkaEge NOT LIKE '-' ORDER BY 3,4,5 ASC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2,KodFakults,Types.INTEGER);
    stmt.setObject(3,abit_O.getKodPredmeta(),Types.INTEGER);
    stmt.setObject(4,StringUtil.data_toDB(abit_O.getSpecial1().substring(abit_O.getSpecial1().lastIndexOf('%')+1)),Types.VARCHAR);
    stmt.setObject(5,abit_O.getKodPredmeta(),Types.INTEGER);
    rs = stmt.executeQuery();
    while(rs.next()) {
      i++;
      report.write("\\intbl "+i+"\\cell\n");
      report.write("\\intbl "+rs.getString(2)+"\\cell\n");
      report.write("\\ql\\intbl "+rs.getString(3)+"\\cell\n");
      report.write("\\intbl "+rs.getString(4)+"\\cell\n");
      report.write("\\intbl "+rs.getString(5)+"\\cell\n");
      report.write("\\qc\\intbl "+rs.getString(6)+"\\cell\n");
      report.write("\\intbl \\row\n");
    }

    report.write("}");
    report.close();
    form.setAction(us.getClientIntName("new_rep","crt"));
    return mapping.findForward("rep_brw");
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
        request.setAttribute("abit_O", abit_O);
        request.setAttribute("abits_O", abits_O);
        request.setAttribute("abit_O_S1", abit_O_S1);
        request.setAttribute("abit_O_S2", abit_O_S2);
        request.setAttribute("abit_O_S3", abit_O_S3);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}