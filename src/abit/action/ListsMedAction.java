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

public class ListsMedAction extends Action {

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
        ListsMedForm         form               = (ListsMedForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_Med_f        = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        int                  num_cols           = 0;
        UserBean             user               = (UserBean)session.getAttribute("user");
        String               name               = new String();
        String               file_con           = new String();

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "listsMedAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "listsMedForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

  if ( form.getAction() == null ) {
  
      form.setAction(us.getClientIntName("view","init"));

  } else if(form.getAction().equals("report")) {

String Abbr_Vuza = new String();
stmt = conn.prepareStatement("SELECT AbbreviaturaVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) Abbr_Vuza = rs.getString(1).toUpperCase();

if(abit_SD.getPriznakSortirovki().equals("post")){
  name = "Список поступающих медалистов "+Abbr_Vuza+" с оценками";
  file_con = "lists_post_med_"+StringUtil.toEng(Abbr_Vuza)+"_ots";
} else {
  name = "Список зачисленных медалистов "+Abbr_Vuza+" с оценками";
  file_con = "lists_zach_med_"+StringUtil.toEng(Abbr_Vuza)+"_ots";
}

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));
  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  stmt = conn.prepareStatement("SELECT COUNT(DISTINCT KodPredmeta) FROM NazvanijaPredmetov");
  rs = stmt.executeQuery();
  if(rs.next()) num_cols = rs.getInt(1);

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

report.write("\\par\n");

if(abit_SD.getPriznakSortirovki().equals("post"))
  report.write("\\fs32 \\qc Список поступающих абитуриентов "+Abbr_Vuza+" с отличиями\n");
else
  report.write("\\fs32 \\qc Список зачисленных абитуриентов "+Abbr_Vuza+" с отличиями\n");

report.write("\\par\\par\n");
//Шапка таблицы
report.write("\\trowd \\fs20 \\trgaph108\\trrh280\\trleft36\\trhdr\n");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx700");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx2000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx4200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx4850");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx5600");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\cellx6200");

for(int i=0; i<=num_cols;i++)
   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx"+((int)8000+(int)i*700));

report.write("\\intbl\\b №\\cell");
report.write("\\intbl Номер дела\\cell");
report.write("\\intbl Фамилия И.О.\\cell");
report.write("\\intbl Отл.\\cell");
report.write("\\intbl Тип уч. зав.\\cell");
report.write("\\intbl № уч. зав.\\cell");
report.write("\\intbl Место уч. зав.\\cell");

stmt = conn.prepareStatement("SELECT DISTINCT np.Sokr,np.KodPredmeta FROM NazvanijaPredmetov np ORDER BY np.KodPredmeta ASC");
rs = stmt.executeQuery();
while(rs.next()) report.write("\\intbl "+rs.getString(1)+"\\cell");

report.write("\\intbl\\b0 \\row");


int num = 0;
int oldKodAb = -1;

if(abit_SD.getPriznakSortirovki().equals("post"))
     stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,m.ShifrMedali,a.NomerShkoly,TipOkonchennogoZavedenija,p.Nazvanie,o.Otsenka FROM NazvanijaPredmetov np, Otsenki o, Abiturient a, Medali m, Punkty p WHERE np.KodPredmeta=o.KodPredmeta AND a.KodMedali=m.KodMedali AND a.KodAbiturienta = o.KodAbiturienta AND m.ShifrMedali NOT LIKE 'н' AND DokumentyHranjatsja LIKE 'д' AND a.KodVuza LIKE ? AND p.KodPunkta=a.KodPunkta GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,np.KodPredmeta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,NomerPlatnogoDogovora,m.ShifrMedali,a.NomerShkoly,TipOkonchennogoZavedenija,p.Nazvanie ORDER BY NomerLichnogoDela,Familija,Imja,Otchestvo,np.KodPredmeta ASC");
else
     stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela,m.ShifrMedali,a.NomerShkoly,TipOkonchennogoZavedenija,p.Nazvanie,o.Otsenka FROM NazvanijaPredmetov np, Otsenki o, Abiturient a, Medali m, Punkty p WHERE np.KodPredmeta=o.KodPredmeta AND a.KodMedali=m.KodMedali AND a.KodAbiturienta = o.KodAbiturienta AND m.ShifrMedali NOT LIKE 'н' AND DokumentyHranjatsja LIKE 'д' AND Prinjat IN ('1','2','3','д') AND a.KodVuza LIKE ? AND p.KodPunkta=a.KodPunkta GROUP BY a.KodAbiturienta,a.NomerLichnogoDela,np.KodPredmeta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,NomerPlatnogoDogovora,m.ShifrMedali,a.NomerShkoly,TipOkonchennogoZavedenija,p.Nazvanie ORDER BY NomerLichnogoDela,Familija,Imja,Otchestvo,np.KodPredmeta ASC");

stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
rs = stmt.executeQuery();
while(rs.next())
{
	if(rs.getInt(1)!=oldKodAb){
          if(oldKodAb!=-1) report.write("\\intbl\\row");

          report.write("\\intbl \\qc\\b0 "+(++num)+"\\cell");
          report.write("\\intbl \\qc "+rs.getString(5)+"\\cell");
          report.write("\\intbl \\ql "+rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".\\cell");
          report.write("\\intbl \\qc "+rs.getString(6)+"\\cell");
          report.write("\\intbl \\qc "+rs.getString(8)+"\\cell");
          if(rs.getString(7) != null)
            report.write("\\intbl \\qc "+rs.getString(7)+"\\cell");
          else
            report.write("\\intbl \\qc \\cell");
          report.write("\\intbl \\ql "+rs.getString(9)+"\\cell");
          if(!rs.getString(10).equals("-"))
            report.write("\\intbl \\qc "+rs.getString(10)+"\\cell");
          else
            report.write("\\intbl \\qc\\cell");

          oldKodAb=rs.getInt(1);
        } else if(!rs.getString(10).equals("-"))
                 report.write("\\intbl \\qc "+rs.getString(10)+"\\cell");
               else
                 report.write("\\intbl \\qc\\cell");

}// цикл 

        if(oldKodAb != -1) report.write("\\intbl\\row\\pard");

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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}