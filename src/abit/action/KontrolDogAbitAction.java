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

public class KontrolDogAbitAction extends Action {

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
        ArrayList            abit_SD_S1         = new ArrayList();        
        boolean              error              = false;
        ActionForward        f                  = null;
        UserBean             user               = (UserBean)session.getAttribute("user");
        String               File               = new String();
        String               Name               = new String();
        String               Dog                = new String("%");
        String               Dog_Text           = new String();
        String               Dog_Ok             = new String("%");
        int                  number             = 0;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "kontrolDogAbitAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "kontrolDogAbitForm", form );

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

            if ( form.getAction() == null ) {

                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  if(abit_SD.getSpecial1().equals("%")) {
    Dog = "AND kon.Dog LIKE 'д'";
    Dog_Ok = "";
    Dog_Text = "желающих заключить договор на обучение";
    File = "_zhe";
    Name= "желающих закл. дог.";
  } else if(abit_SD.getSpecial1().equals("д")) {
    Dog = "";
    Dog_Ok = "AND kon.Dog_Ok LIKE 'д'";
    Dog_Text = "оплативших договор на обучение";
    File = "_opl";
    Name= "оплативших дог.";
  } else if(abit_SD.getSpecial1().equals("н")) {
    Dog = "AND kon.Dog LIKE 'д' AND a.NomerPlatnogoDogovora IS NOT NULL";
    Dog_Ok = "AND (kon.Dog_Ok IS NULL OR kon.Dog_Ok NOT LIKE 'д')";
    Dog_Text = "заключивших, но не оплативших договор";
    File = "_zak";
    Name= "закл. дог., но не опл.";
  }

  String name = "Список абит. "+Name+" на "+StringUtil.CurrDate("-");

  String file_con = "kontrol_dog_abits_"+StringUtil.CurrDate("-")+File;

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\endnhere\\sectdefaultcl\n");

  stmt = conn.prepareStatement("SELECT NazvanieRodit,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs24\\b1\\qc Список абитуриентов {"+rs.getString(1)+"},\\par{"+Dog_Text+"} по состоянию на {"+StringUtil.CurrDate(".")+"г.}\\par\\par\n");

    number = 0;

//    report.write("\\b1\\fs22\\qc{Факультет: }\\'ab{"+rs.getString(5).toUpperCase()+"}\\'bb{  Cпециальность: "+rs.getString(2)+" }\\'ab{"+rs.getString(3)+"}\\'bb\\b0\\par\\par\n");

  report.write("\\trowd\\trqc\\trgaph108\\trhdr\\trrh280\\trleft36\n");
  report.write("\\fs22\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10700\n");

  report.write("\\intbl\\b1{№}\\cell\n");
  report.write("\\intbl{Факультет}\\cell\n");
  report.write("\\intbl{Спец-ть}\\cell\n");
  report.write("\\intbl{Номер личного дела}\\cell\n");
  report.write("\\intbl{Фамилия И.О.}\\cell\n");
  report.write("\\intbl{Номер платного договора}\\cell\n");
  report.write("\\intbl{Договор оплачен}\\b0\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\trowd\\trqc\\trgaph108\\trhdr\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx700\n");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2100\n");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3700\n");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5200\n");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7900\n");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9600\n");
  report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10700\n");

  stmt = conn.prepareStatement("SELECT DISTINCT s.Abbreviatura,s.ShifrSpetsialnosti,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,f.AbbreviaturaFakulteta,s.Abbreviatura FROM Fakultety f, Spetsialnosti s, Konkurs kon, Abiturient a WHERE f.KodFakulteta=s.KodFakulteta AND a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti "+Dog+" "+Dog_Ok+" AND s.KodFakulteta LIKE ? ORDER BY f.AbbreviaturaFakulteta,s.Abbreviatura");
  if(StringUtil.toInt(""+abit_SD.getKodFakulteta(),0) != 0)
    stmt.setObject(1, abit_SD.getKodFakulteta(), Types.VARCHAR);
  else
    stmt.setObject(1, "%", Types.VARCHAR);
  rs = stmt.executeQuery();
  while(rs.next()) {

    stmt = conn.prepareStatement("SELECT a.KodAbiturienta,kon.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,a.NomerPlatnogoDogovora,kon.Dog_Ok FROM Spetsialnosti sp,Abiturient a,Konkurs kon WHERE sp.KodSpetsialnosti=kon.KodSpetsialnosti AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti LIKE ? "+Dog+" "+Dog_Ok+" AND a.DokumentyHranjatsja LIKE 'д' ORDER BY a.Familija,a.Imja,a.Otchestvo");
    stmt.setObject(1,rs.getString(4),Types.VARCHAR);
    rs2 = stmt.executeQuery();
    while (rs2.next()) {
      report.write("\\intbl\\fs22\\qc{"+(++number)+"}\\cell\n");
      report.write("\\intbl\\qc{"+rs.getString(5).toUpperCase()+"}\\cell\n");
      report.write("\\intbl\\qc{"+rs.getString(2)+" ("+rs.getString(1)+")}\\cell\n");
      report.write("\\intbl\\qc{"+rs2.getString(2)+"}\\cell\n");
      report.write("\\intbl\\ql{"+rs2.getString(3)+" "+rs2.getString(4).substring(0,1)+". "+rs2.getString(5).substring(0,1)+"."+"}\\cell\n");
      report.write("\\intbl\\qc{"+StringUtil.ntv(rs2.getString(6))+"}\\cell\n");
      report.write("\\intbl\\qc{"+StringUtil.ntv(rs2.getString(7))+"}\\cell\n");
      report.write("\\intbl\\row\n");
    }

  } //Перебор специальностей факультета (ов) ВУЗа

  report.write("\\pard\\par\n");
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
        request.setAttribute("abit_SD", abit_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}