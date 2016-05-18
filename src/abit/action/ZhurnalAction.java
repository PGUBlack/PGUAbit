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

public class ZhurnalAction extends Action {

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
        ResultSet            rs2                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ShfrLichDelForm      form               = (ShfrLichDelForm) actionForm;
        AbiturientBean       daydate       	= form.getBean(request, errors);
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
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

        request.setAttribute( "zhurnalAction", new Boolean(true) );
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

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_SD_S1.add(abit_TMP);
            }

/************************************************************************************************/
            if ( form.getAction() == null ) {
                 daydate.setDataRojdenija(StringUtil.CurrDate(".")); 
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("getRep")) {

/**********************************************************************************/
/*****  Если action равен getRep , то входим в секцию - создание отчёта  **********/

    String name = new String();
    String file_con = new String();
    String analizing_data = daydate.getDataRojdenija();


    stmt = conn.prepareStatement("SELECT f.Fakultet,f.AbbreviaturaFakulteta FROM Fakultety f WHERE f.KodFakulteta LIKE ?");
    stmt.setObject(1,abit_SD.getKodFakulteta(),Types.VARCHAR);
    rs = stmt.executeQuery();
    if(rs.next()) {

      if(abit_SD.getSpecial1() != null && abit_SD.getSpecial1().equals("on")) {
       file_con = "zhurnal_all_"+StringUtil.toEng(rs.getString(2))+"_"+analizing_data;
        name = "Журнал всех заявлений от начала приёма для "+rs.getString(2).toUpperCase()+" на "+analizing_data;
      } else {
        file_con = "zhurnal_"+StringUtil.toEng(rs.getString(2))+"_"+analizing_data;
        name = "Журнал заявлений для "+rs.getString(2).toUpperCase()+" на "+analizing_data;
      }
    }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 
    if(abit_SD.getSpecial1() != null && abit_SD.getSpecial1().equals("on")) analizing_data = "%";

/************************************************/

    report.write("{\\rtf1\\ansi\n");
    report.write("\\landscape\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj");
    report.write("\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\n");
    report.write("\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\n");
    report.write("\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

    String tip_Dok = "Паспорт";

    stmt = conn.prepareStatement("SELECT DISTINCT s.Abbreviatura,s.ShifrSpetsialnosti,s.NazvanieSpetsialnosti,s.KodSpetsialnosti FROM Spetsialnosti s, Konkurs k, Abiturient a WHERE a.KodAbiturienta=k.KodAbiturienta AND k.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE ? AND a.DataModify LIKE ?");
    stmt.setObject(1,abit_SD.getKodFakulteta(),Types.VARCHAR);
    stmt.setObject(2,analizing_data,Types.VARCHAR);
    rs = stmt.executeQuery();
    while(rs.next()) {

      report.write("\\fs24\\b1\\qc{Специальность(направление): }\\ul{"+rs.getString(2)+"} \\'ab{"+rs.getString(3)+"}\\'bb\\ulnone\\tab{      Дата: }\\ul1\\i1{"+StringUtil.CurrDate(".")+"г.}\\i0\\ulnone");
      report.write("\\b0\\par\\par");

      report.write("\\fs24\\trowd\\trqc\\trgaph108\\trrh560\\trleft36\\trhdr\n");
      report.write("\\clvmgf\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
      report.write("\\clvmgf\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2300\n");
      report.write("\\clvmgf\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6000\n");
      report.write("\\clvmgf\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9000\n");
      report.write("\\clvmgf\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11600\n");
      report.write("\\clvmgf\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13000\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx16000\n");

      report.write("\\intbl\\qc{№}\\par{п/п}\\cell\n");
      report.write("\\intbl{Номер личного}\\par{дела}\\cell\n");
      report.write("\\intbl{Фамилия, имя, отчество}\\cell\n");
      report.write("\\intbl{Домашний адрес}\\cell\n");
      report.write("\\intbl{Представленный документ}\\par{(серия, номер)}\\cell\n");
      report.write("\\intbl{№}\\par{договора,}\\par{оплата}\\cell\n");
      report.write("\\intbl{Документы}\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmrg\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx900\n");
      report.write("\\clvmrg\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx2300\n");
      report.write("\\clvmrg\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx6000\n");
      report.write("\\clvmrg\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx9000\n");
      report.write("\\clvmrg\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx11600\n");
      report.write("\\clvmrg\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx13000\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx14500\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrdb \\cellx16000\n");

      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\cell\n");
      report.write("\\intbl\\fs20{переданы на}\\par{факультет}\\cell\n");
      report.write("\\intbl\\fs20{возвращены}\\par{абитуриенту}\\cell\n");
      report.write("\\intbl\\row\n");

      report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\cellx900\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx2300\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx6000\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx9000\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx11600\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx13000\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx14500\n");
      report.write("\\clvertalc\\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1\\cellx16000\n");

// Количество предыдущих абитуриентов на специальности

      int kol_abt = 0;

      if(!(abit_SD.getSpecial1() != null && abit_SD.getSpecial1().equals("on"))) {

        stmt = conn.prepareStatement("SELECT sz.LastNumber FROM Statements_Zhurnal sz WHERE sz.KodSpetsialnosti LIKE ?");
        stmt.setObject(1,rs.getString(4),Types.VARCHAR);
        rs2 = stmt.executeQuery();
        if(rs2.next()) kol_abt = rs2.getInt(1);
      }

      stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,k.name,a.Ulica_Prop,a.Dom_Prop,a.Kvart_Prop,a.TipDokumenta,a.SeriaDokumenta,a.NomerDokumenta FROM Abiturient a, KLADR k WHERE a.KodSpetsialnosti LIKE ? AND a.DataModify LIKE ? and a.Gorod_Prop = k.code");
      stmt.setObject(1,rs.getString(4),Types.VARCHAR);
      stmt.setObject(2,analizing_data,Types.VARCHAR);
      rs2 = stmt.executeQuery();
      while(rs2.next()) {
        report.write("\\intbl\\qc\\fs20\\i1{"+(++kol_abt)+"}\\cell\n");
        report.write("\\intbl{"+rs2.getString(2)+"}\\cell\n");
        report.write("\\intbl\\ql{"+rs2.getString(3)+" "+rs2.getString(4)+" "+rs2.getString(5)+"}\\cell\n");
        if(rs2.getString(9) != null && !(rs2.getString(9).equals("-") || rs2.getString(9).equals("0")))
          report.write("\\intbl\\ql{"+rs2.getString(6)+", "+rs2.getString(7)+", "+rs2.getString(8)+"-"+rs2.getString(9)+"}\\cell\n");
        else
          report.write("\\intbl\\ql{"+rs2.getString(6)+", "+rs2.getString(7)+", "+rs2.getString(8)+"}\\cell\n");

        if(rs2.getString(10) != null && rs2.getString(10).equals("с")) tip_Dok = "Справка";
        System.out.println(" ГОРОД:   " + rs2.getString(6));

        report.write("\\intbl\\ql{"+tip_Dok+" }{"+rs2.getString(11)+" №"+rs2.getString(12)+"}\\i0\\cell\n");
        report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\qc\\cell\n");
        report.write("\\intbl\\row\n");
      }

// Закрепление последнего номера в списке журнала (ДОСТУПНО ТОЛЬКО ОПЕРАТОРАМ  Id == 1 ) и если не формируется весь список

      if(!(abit_SD.getSpecial1() != null && abit_SD.getSpecial1().equals("on"))) {

        if( user.getGroup().getTypeId() == 1 ) {

          stmt = conn.prepareStatement("SELECT KodSpetsialnosti FROM Statements_Zhurnal WHERE KodSpetsialnosti LIKE ?");
          stmt.setObject(1,rs.getString(4),Types.VARCHAR);
          rs2 = stmt.executeQuery();
          if(!rs2.next()) {
            stmt = conn.prepareStatement("INSERT INTO Statements_Zhurnal(KodSpetsialnosti,LastNumber,LastData) VALUES(?,?,?)");
            stmt.setObject(1,rs.getString(4),Types.VARCHAR);
            stmt.setObject(2,""+kol_abt,Types.VARCHAR);
            stmt.setObject(3,analizing_data,Types.VARCHAR);
            stmt.executeUpdate();

          } else {

            stmt = conn.prepareStatement("UPDATE Statements_Zhurnal SET LastNumber=?,LastData=? WHERE KodSpetsialnosti LIKE ? AND LastData NOT LIKE ?");
            stmt.setObject(1,""+kol_abt,Types.VARCHAR);
            stmt.setObject(2,analizing_data,Types.VARCHAR);
            stmt.setObject(3,rs.getString(4),Types.VARCHAR);
            stmt.setObject(4,analizing_data,Types.VARCHAR);
            stmt.executeUpdate();
          }
        }
      }

      report.write("\\fs24\\pard\\par\\par\\par");
      report.write("\\fs24\\ql{Ответственный секретарь: ______________________}");
      report.write("\\par\\par\\fs20\\tab\\tab\\tab\\tab\\tab\\ql{       м.п.}\\par");
      report.write("\\pard\\page\n");
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
          if ( rs2 != null ) {
               try {
                     rs2.close();
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
        request.setAttribute("daydate", daydate);
        request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}