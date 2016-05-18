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

public class KontrolTPAbitAction extends Action {

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

        request.setAttribute( "kontrolTPAbitAction", new Boolean(true) );
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

  String name = "Контроль целевиков на "+StringUtil.CurrDate(".");

  String file_con = "kontrol_tp_abit_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name));

  report.write("{\\rtf1\\ansi\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\fet0\\sectd \\lndscpsxn\\psz9\\linex0");
  report.write("{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;}");

  report.write("\\pard\\par\\par\n");

  report.write("\\fs24\\b1\\qc{Абитуриенты, подавшие несколько заявлений о целевом приёме}\\par{на специальности (направления) по данным на "+StringUtil.CurrDate(".")+"г.}\\par\\par\n");

  int col_predms = 0;

  stmt = conn.prepareStatement("SELECT COUNT(KodPredmeta) FROM NazvanijaPredmetov WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    col_predms = rs.getInt(1);

  int tab_step = 540;

  report.write("\\pard\\par\n");
  report.write("\\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx"+(960+tab_step)+"\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx"+(1560+4*tab_step)+"\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx"+(1560+7*tab_step)+"\n");

  report.write("\\intbl\\fs24\\qc\\b1{Номер}\\par{личного}\\par{дела}\\cell\n");
  report.write("\\intbl\\fs24\\qc{Фамилия И.О.}\\cell\n");
  report.write("\\intbl\\fs24\\qc{Кол-во}\\par{заявлений}\\cell\n");
  report.write("\\intbl\\b0\\row\n");

  report.write("\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(960+tab_step)+"\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(1560+4*tab_step)+"\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(1560+7*tab_step)+"\n");

// Отбор абитуриентов с несколькими специальностями целевого приема

  stmt = conn.prepareStatement("SELECT ab.KodAbiturienta,ab.Familija,ab.Imja,ab.Otchestvo,ab.NomerLichnogoDela,COUNT(ab.KodAbiturienta) FROM Abiturient ab, Konkurs kon, Spetsialnosti sp WHERE ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.Target LIKE 'д' AND ab.KodVuza LIKE ? GROUP BY ab.KodAbiturienta,ab.Familija,ab.Imja,ab.Otchestvo,ab.NomerLichnogoDela HAVING COUNT(ab.KodAbiturienta) > 1 ORDER BY ab.KodAbiturienta,ab.Familija,ab.Imja,ab.Otchestvo");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()){

     report.write("\\intbl\\fs22\\qc{"+rs.getString(5)+"}\\cell\n");
     report.write("\\intbl\\fs22\\ql{"+(rs.getString(2)).substring(0,1).toUpperCase()+(rs.getString(2)).substring(1)+" "+(rs.getString(3)).substring(0,1).toUpperCase()+"."+(rs.getString(4)).substring(0,1).toUpperCase()+"."+"}\\cell\n");
     report.write("\\intbl\\fs22\\qc{"+rs.getString(6)+"}\\cell\n");
     report.write("\\intbl\\row\n");
  }

  report.write("\\pard\\par\n");

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