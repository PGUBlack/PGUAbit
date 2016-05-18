package abit.action;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Date;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

public class TwinsAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session    = request.getSession();
        Connection              conn       = null;
        PreparedStatement       stmt       = null;
        ResultSet               rs         = null;
        ActionErrors            errors     = new ActionErrors();
        ActionError             msg        = null;
        TwinsForm               form       = (TwinsForm) actionForm;
        AbiturientBean          abit_T     = form.getBean(request, errors);
        boolean                 twins_f    = false;
        boolean                 error      = false;
        ActionForward           f          = null;
        ArrayList               abits_T    = new ArrayList();
        ArrayList               abit_T_S1  = new ArrayList();
        UserBean                user       = (UserBean)session.getAttribute("user");
        int	                    n          = 0;
        int                     nom        = 0;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "twinsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "twinsForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************************ Вывод букв, c которых начинаются фамилии ******************************/

                 stmt = conn.prepareStatement("SELECT UPPER(LEFT(abit1.Familija,1)) FROM Abiturient abit1,Abiturient abit2  WHERE REPLACE(abit1.NomerDokumenta,' ','')=REPLACE(abit2.NomerDokumenta,' ','') AND abit1.KodAbiturienta <> abit2.KodAbiturienta AND abit1.KodVuza =? AND abit1.DokumentyHranjatsja LIKE 'д' UNION SELECT UPPER(LEFT(abit2.Familija,1)) FROM Abiturient abit1,Abiturient abit2  WHERE REPLACE(abit1.NomerDokumenta,' ','')=REPLACE(abit2.NomerDokumenta,' ','') AND REPLACE(abit1.SeriaDokumenta,' ','')=REPLACE(abit2.SeriaDokumenta,' ','') AND abit1.KodAbiturienta <> abit2.KodAbiturienta AND (abit1.DokumentyHranjatsja LIKE 'д' OR abit2.DokumentyHranjatsja LIKE 'д') ORDER BY 1 ASC");
                 stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 if (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setSpecial1(StringUtil.toEng(rs.getString(1)));
                   abit_TMP.setSpecial2(rs.getString(1));
                   abit_T_S1.add(abit_TMP);
                   if( session.getAttribute("letter") == null) 
                   session.setAttribute("letter",StringUtil.toEng(rs.getString(1)));
                 }
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setSpecial1(StringUtil.toEng(rs.getString(1)));
                   abit_TMP.setSpecial2(rs.getString(1));
                   abit_T_S1.add(abit_TMP);
                 }

/******************************************************************************/
/************************ ДВОЙНИКИ - логика работы ****************************/

            if( session.getAttribute("letter") == null) session.setAttribute("letter","a");

            if ( form.getAction() == null || form.getAction().equals("doubles")) {

                 form.setAction(us.getClientIntName("doubles","init"));

                   if( request.getParameter("letter") != null ) {    
                     String str = new String();
                     session.setAttribute("letter",StringUtil.toRus(""+request.getParameter("letter")));
                   }
                    stmt = conn.prepareStatement("SELECT abit1.KodAbiturienta,abit1.Familija,abit1.Imja,abit1.Otchestvo,abit1.NomerLichnogoDela,abit1.NomerDokumenta,abit1.SeriaDokumenta FROM Abiturient abit1,Abiturient abit2  WHERE REPLACE(abit1.NomerDokumenta,' ','') LIKE REPLACE(abit2.NomerDokumenta,' ','') AND REPLACE(abit1.SeriaDokumenta,' ','') LIKE REPLACE(abit2.SeriaDokumenta,' ','') AND abit1.KodAbiturienta <> abit2.KodAbiturienta AND abit1.KodVuza LIKE ? AND abit1.Familija LIKE '"+StringUtil.toRus(""+session.getAttribute("letter"))+"%' AND abit1.DokumentyHranjatsja LIKE 'д' UNION SELECT abit2.KodAbiturienta,abit2.Familija,abit2.Imja,abit2.Otchestvo,abit2.NomerLichnogoDela,abit2.NomerDokumenta,abit2.SeriaDokumenta FROM Abiturient abit1,Abiturient abit2  WHERE REPLACE(abit1.NomerDokumenta,' ','') LIKE REPLACE(abit2.NomerDokumenta,' ','') AND REPLACE(abit1.SeriaDokumenta,' ','') LIKE REPLACE(abit2.SeriaDokumenta,' ','') AND abit1.KodAbiturienta <> abit2.KodAbiturienta AND abit1.Familija LIKE '"+StringUtil.toRus(""+session.getAttribute("letter"))+"%' AND (abit1.DokumentyHranjatsja LIKE 'д' OR abit2.DokumentyHranjatsja LIKE 'д') ORDER BY 2,3,4,5,6 ASC");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                      AbiturientBean abit_TMP = new AbiturientBean();
                      abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
                      abit_TMP.setFamilija(rs.getString(2));
                      abit_TMP.setImja(rs.getString(3));
                      abit_TMP.setOtchestvo(rs.getString(4));
                      abit_TMP.setNomerLichnogoDela(rs.getString(5));
                      abit_TMP.setNomerDokumenta(rs.getString(7)+" "+rs.getString(6));
                      abits_T.add(abit_TMP);
                    }
            } else if(form.getAction().equals("twins")) {

/******************************************************************************/
/************************ БЛИЗНЕЦЫ - логика работы ****************************/

                   form.setAction(us.getClientIntName("twins","view"));
                   stmt = conn.prepareStatement("SELECT DISTINCT abit1.KodAbiturienta,abit1.Familija,abit1.Imja,abit1.Otchestvo,abit1.NomerLichnogoDela,abit1.NomerDokumenta,abit1.SeriaDokumenta FROM Abiturient abit1,Abiturient abit2 WHERE abit1.KodAbiturienta <> abit2.KodAbiturienta AND REPLACE(abit1.NomerDokumenta,' ','') <> REPLACE(abit2.NomerDokumenta,' ','') AND REPLACE(abit1.SeriaDokumenta,' ','') <> REPLACE(abit2.SeriaDokumenta,' ','') AND abit1.NomerLichnogoDela <> abit2.NomerLichnogoDela AND abit1.Familija LIKE abit2.Familija AND abit1.Imja NOT LIKE abit2.Imja AND abit1.Otchestvo LIKE abit2.Otchestvo AND abit1.NomerShkoly LIKE abit2.NomerShkoly AND abit1.KodVuza LIKE ? AND abit1.DokumentyHranjatsja LIKE 'д' AND abit2.DokumentyHranjatsja LIKE 'д' ORDER BY 2,3,4,6,5 ASC");
                   stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
                     abit_TMP.setFamilija(rs.getString(2));
                     abit_TMP.setImja(rs.getString(3));
                     abit_TMP.setOtchestvo(rs.getString(4));
                     abit_TMP.setNomerLichnogoDela(rs.getString(5));
                     abit_TMP.setNomerDokumenta(rs.getString(7)+" "+rs.getString(6));
                     abits_T.add(abit_TMP);
                   }
            } else if(form.getAction().equals("report1")){

/******************************************************************************/
/*********************** БЛИЗНЕЦЫ - генерация отчета **************************/

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Близнецы ВУЗа";

    String file_con = new String("twins");

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

            report.write("{\\rtf1\\ansi\n");
            stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza,NazvanieRodit FROM NazvanieVuza WHERE KodVuza LIKE ?");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) 
            report.write("\\fs40 \\qc "+rs.getString(1)+"\n");
            report.write("\\par\\par\n");
            report.write("\\fs32 \\b \\qc Близнецы ВУЗа\n");
            report.write("\\par\n");
            report.write("\\par\n");
            report.write("\\fs20 \\trowd \\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8800\n");
            report.write("\\intbl №\\cell\n");
            report.write("\\intbl ФАМИЛИЯ\\cell\n");
            report.write("\\intbl ИМЯ\\cell\n");
            report.write("\\intbl ОТЧЕСТВО\\cell\n");
            report.write("\\intbl НОМЕР ЛИЧНОГО ДЕЛА\\cell\n");
            report.write("\\intbl ПАСПОРТ (СЕРИЯ,№)\\cell\n");
            report.write("\\intbl\\row\n");
            report.write("\\b0\n");

        nom=0;
        stmt = conn.prepareStatement("SELECT DISTINCT abit1.KodAbiturienta,abit1.Familija,abit1.Imja,abit1.Otchestvo,abit1.NomerLichnogoDela,abit1.NomerDokumenta,abit1.SeriaDokumenta FROM Abiturient abit1,Abiturient abit2 WHERE abit1.KodAbiturienta <> abit2.KodAbiturienta AND REPLACE(abit1.NomerDokumenta,' ','') <> REPLACE(abit2.NomerDokumenta,' ','') AND REPLACE(abit1.SeriaDokumenta,' ','') <> REPLACE(abit2.SeriaDokumenta,' ','') AND abit1.NomerLichnogoDela <> abit2.NomerLichnogoDela AND abit1.Familija LIKE abit2.Familija AND abit1.Imja NOT LIKE abit2.Imja AND abit1.Otchestvo LIKE abit2.Otchestvo AND abit1.NomerShkoly LIKE abit2.NomerShkoly AND abit1.KodVuza LIKE ? AND abit1.DokumentyHranjatsja LIKE 'д' AND abit2.DokumentyHranjatsja LIKE 'д' ORDER BY 2,3,4,6,5 ASC");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
            report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8800\n");
            report.write("\\qc \\intbl "+(++nom)+"\\cell\n");
            report.write("\\ql \\intbl "+rs.getString(2)+"\\cell\n");
            report.write("\\intbl "+rs.getString(3)+"\\cell\n");
            report.write("\\intbl "+rs.getString(4)+"\\cell\n");
            report.write("\\qc \\intbl "+rs.getString(5)+"\\cell\n");
            report.write("\\intbl "+rs.getString(7)+" "+rs.getString(6)+"\\cell\n");
            report.write("\\intbl \\row\n");
	}
	while (rs.next()) {
            report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8800\n");
            report.write("\\qc \\intbl "+(++nom)+"\\cell\n");
            report.write("\\ql \\intbl "+rs.getString(2)+"\\cell\n");
            report.write("\\intbl "+rs.getString(3)+"\\cell\n");
            report.write("\\intbl "+rs.getString(4)+"\\cell\n");
            report.write("\\qc \\intbl "+rs.getString(5)+"\\cell\n");
            report.write("\\intbl "+rs.getString(7)+" "+rs.getString(6)+"\\cell\n");
            report.write("\\intbl \\row\n");
        }
       report.write("}");
       report.close();

       form.setAction(us.getClientIntName("new_rep","crt-twins"));
       return mapping.findForward("rep_brw");
            } else if(form.getAction().equals("report2")){

/******************************************************************************/
/*********************** ДВОЙНИКИ - генерация отчета **************************/
        nom=0;

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Двойники ВУЗа";

    String file_con = new String("doubles");

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

            report.write("{\\rtf1\\ansi\n");
            stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza,NazvanieRodit FROM NazvanieVuza WHERE KodVuza LIKE ?");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) 
            report.write("\\fs40 \\qc "+rs.getString(1)+"\n");
            report.write("\\par\\par\n");
            report.write("\\fs32 \\b \\qc Двойники ВУЗа\n");
            report.write("\\par\n");
            report.write("\\par\n");
            report.write("\\fs20 \\trowd \\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8800\n");
            report.write("\\intbl №\\cell\n");
            report.write("\\intbl ФАМИЛИЯ\\cell\n");
            report.write("\\intbl ИМЯ\\cell\n");
            report.write("\\intbl ОТЧЕСТВО\\cell\n");
            report.write("\\intbl НОМЕР ЛИЧНОГО ДЕЛА\\cell\n");
            report.write("\\intbl ПАСПОРТ (СЕРИЯ,№)\\cell\n");
            report.write("\\intbl\\row\n");
            report.write("\\b0\n");

       stmt = conn.prepareStatement("SELECT abit1.KodAbiturienta,abit1.Familija,abit1.Imja,abit1.Otchestvo,abit1.NomerLichnogoDela,abit1.NomerDokumenta,abit1.SeriaDokumenta FROM Abiturient abit1,Abiturient abit2 WHERE REPLACE(abit1.NomerDokumenta,' ','')=REPLACE(abit2.NomerDokumenta,' ','') AND REPLACE(abit1.SeriaDokumenta,' ','')=REPLACE(abit2.SeriaDokumenta,' ','') AND abit1.KodAbiturienta <> abit2.KodAbiturienta AND abit1.KodVuza LIKE ? AND abit1.DokumentyHranjatsja LIKE 'д' UNION SELECT abit2.KodAbiturienta,abit2.Familija,abit2.Imja,abit2.Otchestvo,abit2.NomerLichnogoDela,abit2.NomerDokumenta,abit2.SeriaDokumenta FROM Abiturient abit1,Abiturient abit2 WHERE REPLACE(abit1.NomerDokumenta,' ','')=REPLACE(abit2.NomerDokumenta,' ','') AND REPLACE(abit1.SeriaDokumenta,' ','')=REPLACE(abit2.SeriaDokumenta,' ','') AND abit1.KodAbiturienta <> abit2.KodAbiturienta AND (abit1.DokumentyHranjatsja LIKE 'д' OR abit2.DokumentyHranjatsja LIKE 'д') ORDER BY 2,3,4,5,6 ASC");
       stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
       rs = stmt.executeQuery();
       if(rs.next()) {
            report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
            report.write("\\clvertalc \\clbrdrt\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8800\n");
            report.write("\\qc \\intbl "+(++nom)+"\\cell\n");
            report.write("\\ql \\intbl "+rs.getString(2)+"\\cell\n");
            report.write("\\intbl "+rs.getString(3)+"\\cell\n");
            report.write("\\intbl "+rs.getString(4)+"\\cell\n");
            report.write("\\qc \\intbl "+rs.getString(5)+"\\cell\n");
            report.write("\\intbl "+rs.getString(7)+" "+rs.getString(6)+"\\cell\n");
            report.write("\\intbl \\row\n");
       }
       while (rs.next()) {
            report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2400\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7000\n");
            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8800\n");
            report.write("\\qc \\intbl "+(++nom)+"\\cell\n");
            report.write("\\ql \\intbl "+rs.getString(2)+"\\cell\n");
            report.write("\\intbl "+rs.getString(3)+"\\cell\n");
            report.write("\\intbl "+rs.getString(4)+"\\cell\n");
            report.write("\\qc \\intbl "+rs.getString(5)+"\\cell\n");
            report.write("\\intbl "+rs.getString(7)+" "+rs.getString(6)+"\\cell\n");
            report.write("\\intbl \\row\n");
       }
         report.write("}");
         report.close();
       form.setAction(us.getClientIntName("new_rep","crt-doubles"));
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
        request.setAttribute("abit_T", abit_T);
        request.setAttribute("abits_T", abits_T);
        request.setAttribute("abit_T_S1", abit_T_S1);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(twins_f) return mapping.findForward("twins_f");
        return mapping.findForward("success");
    }
}