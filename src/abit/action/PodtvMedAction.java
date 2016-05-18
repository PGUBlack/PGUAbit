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

public class PodtvMedAction extends Action {

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
        ResultSet            rs_a               = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        PodtvMedForm         form               = (PodtvMedForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              podtv_med_f        = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        ArrayList            abit_SD_S4         = new ArrayList();
        int                  st                 = 0;
        String               file_con           = new String();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "podtvMedAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "podtvMedForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/************* Подготовка данных для ввода с помощью селекторов ****************/
/*******************************************************************************/
            stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta, ShifrFakulteta FROM fakultety GROUP BY ShifrFakulteta,AbbreviaturaFakulteta ORDER BY 1,2 ASC");
            rs = stmt.executeQuery();
            while (rs.next()) 
            {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setShifrFakulteta(rs.getString(2));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
              abit_SD_S1.add(abit_TMP);
            }

/************************************************************************************************/
            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

/************************** Генерация отчета ***********************************/

            } else if ( form.getAction().equals("report")) {

        String query = new String();
        if(!abit_SD.getShifrFakulteta().equals("*"))
          query="SELECT a.NomerLichnogoDela,s.Abbreviatura,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.DokumentyHranjatsja FROM Abiturient a,Spetsialnosti s,Medali m,Fakultety f WHERE a.KodMedali=m.KodMedali AND a.KodSpetsialnosti=s.KodSpetsialnosti AND f.KodFakulteta=s.KodFakulteta AND PodtvMed LIKE 'д' AND f.ShifrFakulteta LIKE '"+abit_SD.getShifrFakulteta()+"' ORDER BY Familija,Imja,Otchestvo ASC";
        else
          query="SELECT a.NomerLichnogoDela,s.Abbreviatura,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,a.DokumentyHranjatsja FROM Abiturient a,Spetsialnosti s,Medali m WHERE a.KodMedali=m.KodMedali AND a.KodSpetsialnosti=s.KodSpetsialnosti AND PodtvMed LIKE 'д' ORDER BY Familija,Imja,Otchestvo ASC";

  if(!abit_SD.getShifrFakulteta().equals("*"))
     file_con = "podtv_med_"+StringUtil.toEng(abit_SD.getShifrFakulteta());
  else
     file_con = "podtv_med_allF";

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = new String();

  if(abit_SD.getShifrFakulteta().equals("*")) name = "Подтвержд. медалисты";
  else {
    stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE ShifrFakulteta LIKE '"+abit_SD.getShifrFakulteta()+"'");
    rs = stmt.executeQuery();
    if(rs.next()) name ="Подтвержд. медалисты на "+rs.getString(1).toUpperCase();
    else name = "Подтвержд. медалисты";
  }
    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

  if(abit_SD.getShifrFakulteta().equals("*"))
    report.write("\\fs32 \\qc Список абитуриентов ВУЗа, подтвердивших свои медали \n");
  else {
    stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE ShifrFakulteta LIKE '"+abit_SD.getShifrFakulteta()+"'");
    rs = stmt.executeQuery();
    if(rs.next()) report.write("\\fs32 \\qc Список абитуриентов, подтвердивших свои медали на "+rs.getString(1).toUpperCase()+"\n");
    else report.write("\\fs32 \\qc Список абитуриентов, подтвердивших свои медали \n");
  }

  report.write("\\par\\par\n");
  report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36\\trhdr");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx500");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx1800");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx2800");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx4600");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx6700");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx9000");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx9900");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\cellx10900");
  report.write("\\intbl\\b №\\cell");
  report.write("\\intbl Номер дела\\cell");
  report.write("\\intbl Шифр спец.\\cell");
  report.write("\\intbl Фамилия\\cell");
  report.write("\\intbl Имя\\cell");
  report.write("\\intbl Отчество\\cell");
  report.write("\\intbl Отли-чие\\cell");
  report.write("\\intbl Док. хр.\\cell");
  report.write("\\intbl \\row");

  report.write("\\fs24 \\b0 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx500");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2800");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4600");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6700");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9900");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10900");

  st=0;
  stmt = conn.prepareStatement(query);
  rs = stmt.executeQuery();
  while(rs.next()){
    report.write("\\intbl \\qc "+(++st)+"\\cell");
    report.write("\\intbl \\qc "+rs.getString(1)+"\\cell");
    report.write("\\intbl \\qc "+rs.getString(2)+"\\cell");
    report.write("\\intbl \\ql "+rs.getString(3)+"\\cell");
    report.write("\\intbl \\ql "+rs.getString(4)+"\\cell");
    report.write("\\intbl \\ql "+rs.getString(5)+"\\cell");
    report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
    report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
    report.write("\\intbl \\row");
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
        request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
        request.setAttribute("abit_SD_S4", abit_SD_S4);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(podtv_med_f) return mapping.findForward("podtv_med_f");
        return mapping.findForward("success");
    }
}