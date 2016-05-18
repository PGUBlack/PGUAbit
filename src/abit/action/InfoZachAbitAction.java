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

public class InfoZachAbitAction extends Action {

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
        int                  TotalVakancies     = 0;
        UserBean             user               = (UserBean)session.getAttribute("user");
        ArrayList            spec_list1         = new ArrayList();
        ArrayList            spec_list2         = new ArrayList();

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "infoZachAbitAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "infoZachAbitForm", form );

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

  String name = "Информация о зачислении абитуриентов на "+StringUtil.CurrDate("-");

  String file_con = "info_zach_abit_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("\\pard\n");

  stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs28\\b1\\qc Информация о зачислении абитуриентов\\par в {"+rs.getString(1)+"} на {"+StringUtil.CurrDate(".")+"г.}\\par\n");

  report.write("\\pard\\par\n");
  report.write("\\fs26\\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx7500\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx9200\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14700\n");

  report.write("\\intbl\\fs24\\qc\\b1{Наименование специальности\\par (направления)}\\cell\n");
  report.write("\\intbl\\fs24\\qc{Количество бюджетных мест}\\cell\n");
  report.write("\\intbl\\fs24\\qc{Количество поданных заявлений}\\cell\n");
  report.write("\\intbl\\fs24\\qc{Зачислено}\\b0\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs24 \\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\cellx7500\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx9200\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx13300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14700\n");

  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\fs24\\b1\\qc{30 июля}\\cell\n");
  report.write("\\intbl\\fs24\\qc{5 августа}\\cell\n");
  report.write("\\intbl\\fs24\\qc{вакансии}\\b0\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs24 \\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx7500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx9200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx10700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx13300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx14700\n");

// Формирование исходного списка spec_list1

  int PlanPriema = 0;
  int Zachisleny = 0;
  stmt = conn.prepareStatement("SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,COUNT(ab.KodAbiturienta) FROM Konkurs kon,Spetsialnosti sp, Abiturient ab,Fakultety fk WHERE kon.KodAbiturienta=ab.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.PlanPriema NOT LIKE '0' AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND ab.KodVuza LIKE ? GROUP BY sp.ShifrSpetsialnosti,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.Abbreviatura,sp.KodSpetsialnosti UNION SELECT sp.ShifrSpetsialnosti,sp.Abbreviatura,sp.NazvanieSpetsialnosti,sp.PlanPriema,sp.KodSpetsialnosti,0 FROM Spetsialnosti sp,Fakultety fk WHERE fk.KodFakulteta=sp.KodFakulteta AND fk.AbbreviaturaFakulteta NOT IN('фзо','юк','цдо','фозидо') AND sp.Abbreviatura NOT IN('цгу','цмп','цэг','цк','цку','цл','цп','цю') AND sp.NazvanieSpetsialnosti NOT LIKE '%магистр%' AND sp.PlanPriema NOT LIKE '0' AND sp.KodSpetsialnosti NOT IN (SELECT DISTINCT KodSpetsialnosti FROM Konkurs) ORDER BY sp.Abbreviatura");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  while(rs.next()){

    ListBean lb = new ListBean();

    lb.setProperty1(rs.getString(1)+" ("+rs.getString(2)+")");
    lb.setProperty2(rs.getString(3));
    lb.setProperty3(rs.getString(4));
    lb.setProperty4(rs.getString(6));

// Шифр спец-ти для сортировки

    lb.setId(StringUtil.toInt(rs.getString(1),0));

// 30 июля

    stmt = conn.prepareStatement("SELECT COUNT(DISTINCT a.KodAbiturienta) FROM Abiturient a WHERE a.KodSpetsialnZach LIKE ? AND a.Prinjat LIKE '1' AND KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')");
    stmt.setObject(1,rs.getString(5),Types.INTEGER);
    rs2 = stmt.executeQuery();
    if(rs2.next())
      lb.setProperty5(rs2.getString(1));
    else
      lb.setProperty5("0");

// 5 августа

    stmt = conn.prepareStatement("SELECT COUNT(DISTINCT a.KodAbiturienta) FROM Abiturient a WHERE a.KodSpetsialnZach LIKE ? AND a.Prinjat LIKE '2' AND KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')");
    stmt.setObject(1,rs.getString(5),Types.INTEGER);
    rs2 = stmt.executeQuery();
    if(rs2.next())
      lb.setProperty6(rs2.getString(1));
    else
      lb.setProperty6("0");

// вакансии

    PlanPriema = Zachisleny = 0;

    stmt = conn.prepareStatement("SELECT PlanPriema FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
    stmt.setObject(1,rs.getString(5),Types.INTEGER);
    rs2 = stmt.executeQuery();
    if(rs2.next())
      PlanPriema = StringUtil.toInt(rs2.getString(1),0);

    stmt = conn.prepareStatement("SELECT COUNT(DISTINCT a.KodAbiturienta) FROM Abiturient a WHERE a.KodSpetsialnZach LIKE ? AND a.Prinjat IN ('1','2') AND KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д')");
    stmt.setObject(1,rs.getString(5),Types.INTEGER);
    rs2 = stmt.executeQuery();
    if(rs2.next())
      Zachisleny = StringUtil.toInt(rs2.getString(1),0);

    lb.setProperty7(""+(PlanPriema-Zachisleny));

    TotalVakancies += StringUtil.toInt(""+(PlanPriema-Zachisleny),0);

    spec_list1.add(lb);
  }



// Сортировка списка spec_list1 (построение списка spec_list2 в порядке убывания SummaEge)

  int maxValue, maxValueIndex;

  while(spec_list1.size() != 0) {

    maxValue = StringUtil.toInt(""+((ListBean)spec_list1.get(0)).getId(),0);

    maxValueIndex = 0;

    for(int cur_ind=0;cur_ind<spec_list1.size();cur_ind++) {

      if(StringUtil.toInt(""+((ListBean)spec_list1.get(cur_ind)).getId(),0) <= maxValue) {

        maxValue = StringUtil.toInt(""+((ListBean)spec_list1.get(cur_ind)).getId(),0);

        maxValueIndex = cur_ind;
      }
    }

    spec_list2.add(spec_list1.get(maxValueIndex));

    spec_list1.remove(maxValueIndex);
  }

// Вывод отсортированного списка

  for(int ind=0;ind<spec_list2.size();ind++) {

    report.write("\\intbl\\fs20\\qc{"+((ListBean)spec_list2.get(ind)).getProperty1()+"}\\cell\n");

    report.write("\\intbl\\fs20\\ql{"+((ListBean)spec_list2.get(ind)).getProperty2()+"}\\cell\n");

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty3()+"}\\cell\n");

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty4()+"}\\cell\n");

// 30 июля

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty5()+"}\\cell\n");

// 5 августа

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty6()+"}\\cell\n");

// вакансии

    report.write("\\intbl\\fs24\\qc{"+((ListBean)spec_list2.get(ind)).getProperty7()+"}\\cell\n");

    report.write("\\intbl\\b0\\row\n");
  }

  report.write("\\intbl\\fs20\\qc\\cell\n");
  report.write("\\intbl\\fs20\\qr\\b1{ВСЕГО:  }\\cell\n");
  report.write("\\intbl\\fs20\\qc\\cell\n");
  report.write("\\intbl\\fs20\\qc\\cell\n");
  report.write("\\intbl\\fs20\\qc\\cell\n");
  report.write("\\intbl\\fs20\\qc\\cell\n");
  report.write("\\intbl\\fs20\\qc{"+TotalVakancies+"}\\b0\\cell\n");
  report.write("\\intbl\\row\n");

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