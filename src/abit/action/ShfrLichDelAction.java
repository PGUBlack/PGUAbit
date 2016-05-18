package abit.action;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import java.util.Date;
import java.io.*;
import abit.sql.*; 

public class ShfrLichDelAction extends Action {

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
        ShfrLichDelForm      form               = (ShfrLichDelForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        int                  number             = 0;
        int                  sum                = 0;
        int                  nld                = 0;
        int                  len                = 0;
        int                  maxLEN             = 0;
        String               SEKR_Priemn_Komiss = new String();
        String               strNLD             = new String();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "shfrLichDelAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "shfrLichDelForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************* Подготовка данных для ввода с помощью селекторов *************************/

            stmt = conn.prepareStatement("SELECT DISTINCT ShifrFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setShifrFakulteta(rs.getString(1));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_SD_S1.add(abit_TMP);
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
                  abit_SD_S2.add(abit_TMP);
                  priznak = true;
                  kPredmeta = "";
              }
              kPredmeta += "%" + rs.getString(2);
            }
            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial1(oldKode + kPredmeta);
            abit_TMP2.setAbbreviatura(oldAbbr);
            abit_SD_S2.add(abit_TMP2);

/************************************************************************************************/
            if ( form.getAction() == null ) {

                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("getRep")) {

/**********************************************************************************/
/*****  Если action равен getRep , то входим в секцию - создание отчёта  **********/

    String name = new String();
    String file_con = new String();
    stmt = conn.prepareStatement("SELECT f.Fakultet,f.AbbreviaturaFakulteta,s.NazvanieSpetsialnosti,s.Abbreviatura,s.ShifrSpetsialnostiOKSO FROM Fakultety f,Spetsialnosti s WHERE s.KodFakulteta=f.KodFakulteta AND f.ShifrFakulteta LIKE ? AND s.KodSpetsialnosti LIKE ?");
    stmt.setObject(1,abit_SD.getShifrFakulteta(),Types.VARCHAR);
    stmt.setObject(2,abit_SD.getSpecial1().substring(0,abit_SD.getSpecial1().indexOf('%')),Types.VARCHAR);
    rs = stmt.executeQuery();
    if(rs.next()) {
      file_con = file_con+StringUtil.toEng(rs.getString(2)+"_"+StringUtil.toEng(rs.getString(4)));
      name = "Список НЛД для "+rs.getString(2).toUpperCase()+" ("+rs.getString(4).toLowerCase()+")";
      abit_SD.setSpecial4(rs.getString(1));
      abit_SD.setSpecial5(rs.getString(2));
      abit_SD.setSpecial6(rs.getString(3));
      abit_SD.setSpecial7(rs.getString(4));
      abit_SD.setSpecial8(rs.getString(5));
    }

    stmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%секрет%'");
    rs = stmt.executeQuery();
    if(rs.next()) {
      SEKR_Priemn_Komiss = rs.getString(1)+": ______________ "+rs.getString(2);
    }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

    report.write("{\\rtf1\\ansi\\pgnid{\\footer\\tqr\\posxr{\\field{\\fldinst{PAGE}}}{\\par}}\n");
    report.write("\\landscape\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj");
    report.write("\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\n");
    report.write("\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\n");
    report.write("\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\n");

    report.write("\\fs32\\b1\\qc{"+abit_SD.getSpecial4()+" ("+abit_SD.getSpecial5().toUpperCase()+")}\\par\\fs14");
    report.write("\\fs32\\b1\\qc{"+abit_SD.getSpecial6()+" ("+abit_SD.getSpecial8().toUpperCase()+")}\\par\\fs14");

    report.write("\\b0\\par\n");
    report.write("\\fs28\\trowd\\trqc\\trgaph108\\trrh560\\trleft36\\trhdr\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2900\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8900\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10500\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12200\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13500\n");

    report.write("\\intbl\\qc\\b1{№}\\cell\n");
    report.write("\\intbl{ШИФР}\\cell\n");
    report.write("\\intbl{ФИО}\\cell\n");
    report.write("\\intbl{СДАЛ}\\cell\n");
    report.write("\\intbl{ПРИНЯЛ}\\cell\n");
    report.write("\\intbl{ДАТА}\\b0\\cell\n");
    report.write("\\intbl\\row\n");

    report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx2900\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx8900\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx10500\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx12200\n");
    report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx13500\n");

// Начальный номер личного дела в формате int

    nld = StringUtil.toInt(abit_SD.getSpecial2(),0);

    maxLEN = abit_SD.getSpecial2().length();

// Формирование списка номеров личных дел

    for(int i=0;i<StringUtil.toInt(abit_SD.getSpecial3(),0);i++) {

       report.write("\\intbl\\qc{"+(++number)+"}\\cell\n");

       while(true){

         strNLD = "" + nld;

         sum = 0;

         len = strNLD.length();

         for (int ind=0; ind<len; ind++) {
           sum += StringUtil.toInt(strNLD.substring(ind, ind+1),0);
         }

         nld++;

         if(sum == 10) { 

           strNLD = "";
           for(int l = 0; l < (maxLEN-len); l++) strNLD += "0";
           strNLD += ""+(nld-1);
           break;

         }
       }
       report.write("\\intbl\\qc{"+abit_SD.getSpecial7()+strNLD+"}\\cell\n");

       report.write("\\intbl\\cell\n");
       report.write("\\intbl\\cell\n");
       report.write("\\intbl\\cell\n");
       report.write("\\intbl\\cell\n");
       report.write("\\intbl\\row\n");

// Для первой страницы, после 23-й строки выводим подпись отв. секретаря приемной комисс.

       if((number == 23 || number % 24 == 0 ) && (number != 24 && number != StringUtil.toInt(abit_SD.getSpecial3(),0))) {


         report.write("\\pard\\par\\par");
         report.write("\\fs28\\qc{"+SEKR_Priemn_Komiss+"}");
         report.write("\\par");

         if (number % 24 == 0) report.write("\\pard\\par\\par");

         report.write("\\fs28\\trowd\\trqc\\trgaph108\\trrh560\\trleft36\\trhdr\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2900\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8900\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10500\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12200\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13500\n");

         report.write("\\intbl\\qc\\b1{№}\\cell\n");
         report.write("\\intbl{ШИФР}\\cell\n");
         report.write("\\intbl{ФИО}\\cell\n");
         report.write("\\intbl{СДАЛ}\\cell\n");
         report.write("\\intbl{ПРИНЯЛ}\\cell\n");
         report.write("\\intbl{ДАТА}\\b0\\cell\n");
         report.write("\\intbl\\row\n");

         report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx2900\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx8900\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx10500\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx12200\n");
         report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx13500\n");
       }
    }

    report.write("\\pard\\par\\par");
    report.write("\\fs28\\qc{"+SEKR_Priemn_Komiss+"}");

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
        request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S4", abit_SD_S4);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}