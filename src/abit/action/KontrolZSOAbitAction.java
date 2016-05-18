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

public class KontrolZSOAbitAction extends Action {

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
        Forma_2_Form         form               = (Forma_2_Form) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              error              = false;
        ActionForward        f                  = null;
        UserBean             user               = (UserBean)session.getAttribute("user");
        ArrayList            spec_list1         = new ArrayList();
        ArrayList            spec_list2         = new ArrayList();

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "kontrolZSOAbitAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "infoAbitForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  int counter = 0;

  int num_orig = 0;

  boolean proh_ball_counted = false;

  String name = "Контроль баллов ЕГЭ на "+StringUtil.CurrDate(".");

  String file_con = "kontrol_zso_abit_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name));

  report.write("{\\rtf1\\ansi\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\fet0\\sectd \\lndscpsxn\\psz9\\linex0");
  report.write("{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;}");

  report.write("\\pard\n");

  stmt = conn.prepareStatement("SELECT NazvanieRodit,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs28\\b1\\qc{Подозрительные корректировки баллов ЕГЭ следующих абитуриентов}\\par {"+rs.getString(1)+"} на {"+StringUtil.CurrDate(".")+"г.}\\par\n");

  int col_predms = 0;

  stmt = conn.prepareStatement("SELECT COUNT(KodPredmeta) FROM NazvanijaPredmetov WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    col_predms = rs.getInt(1);

  int tab_step = 540;

  report.write("\\pard\\par\n");
  report.write("\\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(760+tab_step)+"\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(1260+4*tab_step)+"\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(1260+4*tab_step+2*col_predms*tab_step)+"\n");

  report.write("\\intbl\\fs24\\qc\\b1{Номер}\\par{личного}\\par{дела}\\cell\n");
  report.write("\\intbl\\fs24\\qc{Фамилия И.О.}\\cell\n");
  report.write("\\intbl\\fs18\\par\\fs24\\qc{Названия предметов вступительных испытаний}\\fs18\\par\\cell\n");
  report.write("\\intbl\\b0\\row\n");

  report.write("\\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx"+(760+tab_step)+"\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx"+(1260+4*tab_step)+"\n");

  for(int col=1;col<=(col_predms);col++) {
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx"+(1260+4*tab_step+2*col*tab_step)+"\n");
  }

  report.write("\\intbl{}\\cell\n");
  report.write("\\intbl{}\\cell\n");

  stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY KodPredmeta ASC");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()) {
    report.write("\\intbl\\fs10\\par\\fs24\\qc\\b1{"+rs.getString(1)+"}\\b0\\fs10\\par\\cell\n");
  }
  report.write("\\intbl\\row\n");

  report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(760+tab_step)+"\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(1260+4*tab_step)+"\n");
  for(int col=1;col<=2*(col_predms);col++) {
     if(col%2 != 0) report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(1260+4*tab_step+col*tab_step)+"\n");
     else report.write("\\clvertalc\\clshdngraw1000 \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(1260+4*tab_step+col*tab_step)+"\n");
  }

// Отбор абитуриентов с сомнительными баллами

  String oldKodAb = "-1", ball1 = "", ball2 = "";
  stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,kzso.OtsenkaEge,zso.OtsenkaEge FROM Abiturient a, Kontrol_ZSO kzso,ZajavlennyeShkolnyeOtsenki zso WHERE a.KodAbiturienta=kzso.KodAbiturienta AND zso.KodAbiturienta=kzso.KodAbiturienta AND zso.KodPredmeta=kzso.KodPredmeta AND a.KodAbiturienta IN (SELECT DISTINCT kz.KodAbiturienta FROM Kontrol_zso kz,ZajavlennyeShkolnyeOtsenki zso WHERE kz.KodAbiturienta=zso.KodAbiturienta AND kz.KodPredmeta=zso.KodPredmeta AND kz.OtsenkaEge <= zso.OtsenkaEge AND kz.OtsenkaEge NOT LIKE '0' AND kz.OtsenkaEge NOT LIKE zso.OtsenkaEge) AND a.KodVuza LIKE ? ORDER BY a.KodAbiturienta,zso.KodPredmeta ASC");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()){

// Убираем одинаковые баллы из таблицы, т.к. это здесь не интересно видеть

    if(rs.getString(6) != null && rs.getString(7) != null && rs.getString(6).equals(rs.getString(7))) {

      ball1 = "";
      ball2 = "";

    } else {

      ball1 = StringUtil.voidFilter(rs.getString(6));
      ball2 = StringUtil.voidFilter(rs.getString(7));
    }

    if(!oldKodAb.equals(rs.getString(1))) {

      if(!oldKodAb.equals("-1")) report.write("\\intbl\\row\n");

      report.write("\\intbl\\fs22\\qc{"+rs.getString(2)+"}\\cell\n");

      report.write("\\intbl\\fs22\\ql{"+(rs.getString(3)).substring(0,1).toUpperCase()+(rs.getString(3)).substring(1)+" "+(rs.getString(4)).substring(0,1).toUpperCase()+"."+(rs.getString(5)).substring(0,1).toUpperCase()+"."+"}\\cell\n");

      report.write("\\intbl\\fs20\\qc{"+ball1+"}\\cell\n");

      report.write("\\intbl\\fs20\\qc{"+ball2+"}\\cell\n");

      oldKodAb = rs.getString(1);

    } else {

      report.write("\\intbl\\fs20\\qc{"+ball1+"}\\cell\n");

      report.write("\\intbl\\fs20\\qc{"+ball2+"}\\cell\n");
    }
  }

  if(oldKodAb == "-1") {
    report.write("\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell");
    report.write("\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell");
    report.write("\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell");
    report.write("\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell");
    report.write("\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\\intbl{}\\cell\n");
  }
  report.write("\\intbl\\row\n");

  report.write("\\pard\\par\\par\\par\n");
  report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc\\clshdngraw1000 \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+tab_step+"\n");
  report.write("\\clvertalc \\cellx6260\n");
  report.write("\\intbl\\fs24\\qc{}\\cell\n");
  report.write("\\intbl\\fs24\\ql{ - баллы ЕГЭ согласно данным текущей БД}\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("}");
  report.close();

  spec_list2.clear();

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
        request.setAttribute("abit_SD", abit_SD);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}