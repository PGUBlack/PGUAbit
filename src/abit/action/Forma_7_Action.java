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

public class Forma_7_Action extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    pstmt              = null;
        ResultSet            rs                 = null;
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        Forma_2_Form         form               = (Forma_2_Form) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_formaseven_f   = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        String               Bud_Dog              = "";
        String               PRINJAT              = "";
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "forma_7_Action", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute("forma_2_Form", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  int sub_table_step = 540;

  String name = "Форма №7";

  String fo = "_______________";

  if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("о")) fo = "очная";

  else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("з")) fo = "заочная";
  else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("в")) fo = "очно-заочная (вечерняя)";
  else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("м")) fo = "магистры";
  else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("д")) fo = "дистанционная";
  else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("у")) fo = "ускоренная";
  else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("ф")) fo = "ИИТО";
  else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("ю")) fo = "ЮК";

  if(abit_SD.getSpecial1() !=null && abit_SD.getSpecial1().equals("%")) PRINJAT = "IN('1','2','3','4','д')";
  else PRINJAT = "LIKE '"+abit_SD.getSpecial1()+"'";

  int min_val[] = new int[]{0,6,11,16,21,26,31,36,41,46,51,56,61,66,71,76,81,86,91,96};
  int max_val[] = new int[]{5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100};

  String file_con = "forma7_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;}\n");
  report.write("\\fs18\\b1\\qc Сведения о зачислении в вуз по результатам единого государственного экзамена (для программ высшего профессионального образования) на "+StringUtil.CurrDate(".")+" г.\\b0\\line\n");
//  report.write("\\par\\fs16\\ql\\i1 Данная страница заполняется только образовательными учреждениями, принимавшими участие в эксперименте по единому государственному экзамену в 2010 году.\\i0\n");

  report.write("\\par\\b1\\fs16\\qc\\trowd\\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3000\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3900\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(5300+sub_table_step*20)+"\n");

  String headline[] = new String[]{"Специальность\\par по ОКСО","Код","Форма обучения","Форма финанси-\\par рования","Предмет ЕГЭ","Количество зачисленных по результатам единого государственного экзамена, распределенное по интервалам тестовых баллов (чел.)"};
  for(int i=0;i<headline.length;i++)
     report.write("\\intbl{"+headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs14\\trowd \\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3900\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");

  for(int i=1;i<=20;i++)
     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(5300+sub_table_step*i)+"\n");

  String sub_headline[] = new String[]{"","","","","","0-5\\par бал-\\par лов","6-10\\par бал-\\par лов","11-15\\par бал-\\par лов","16-20\\par бал-\\par лов","21-25\\par бал-\\par лов","26-30\\par бал-\\par лов","31-35\\par бал-\\par лов","36-40\\par бал-\\par лов","41-45\\par бал-\\par лов","46-50\\par бал-\\par лов","51-55\\par бал-\\par лов","56-60\\par бал-\\par лов","61-65\\par бал-\\par лов","66-70\\par бал-\\par лов","71-75\\par бал-\\par лов","76-80\\par бал-\\par лов","81-85\\par бал-\\par лов","86-90\\par бал-\\par лов","91-95\\par бал-\\par лов","96-100\\par бал-\\par лов"};
  for(int i=0;i<sub_headline.length;i++)
     report.write("\\intbl{"+sub_headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs16\\b0\\trhdr\\trowd\\trqc \\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");

  for(int i=1;i<=20;i++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(5300+sub_table_step*i)+"\n");

  for(int i=1;i<=25;i++)
     report.write("\\intbl\\b1{"+i+"}\\b0\\cell\n");

  report.write("\\intbl\\row\n");
  report.write("\\pard\n");

/********************************/
/****  Формат строки таблицы  ***/
/********************************/

  int cnt_prdmts = 0, cur_cnt_prdms = 0, curr_kol_abts = 0;

  pstmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti FROM Spetsialnosti sp,Fakultety f WHERE sp.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND sp.Tip_Spec LIKE ? ORDER BY sp.ShifrSpetsialnosti ASC");
  pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  pstmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
  rs = pstmt.executeQuery();
  while(rs.next()){

/********************************/
/*****     Бюджетники       *****/
/********************************/

      pstmt = conn.prepareStatement("SELECT COUNT(ens.KodSpetsialnosti) FROM EkzamenyNaSpetsialnosti ens, NazvanijaPredmetov np WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti LIKE ?");
      pstmt.setObject(1,rs.getString(1),Types.VARCHAR);
      rs2 = pstmt.executeQuery();
      if(rs2.next()) cnt_prdmts = rs2.getInt(1);
      else cnt_prdmts = 3;

      cur_cnt_prdms = cnt_prdmts;

      pstmt = conn.prepareStatement("SELECT np.Predmet,np.KodPredmeta FROM EkzamenyNaSpetsialnosti ens, NazvanijaPredmetov np WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti LIKE ?");
      pstmt.setObject(1,rs.getString(1),Types.VARCHAR);
      rs2 = pstmt.executeQuery();
      if(rs2.next()) {

// Первый предмет

        report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx1406\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2106\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2906\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3806\n");
        report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
        for(int j=1;j<=20;j++)
          report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx"+(5206+sub_table_step*j)+"\n");

        report.write("\\pard\\intbl\\nowidctlpar\\qc{"+rs.getString(4)+"}\n");
        report.write("\\cell{"+rs.getString(2)+"}\n");
        report.write("\\cell{"+fo+"}\n");
        report.write("\\cell{бюджет}\n");
        report.write("\\cell{"+rs2.getString(1)+"}\n");
        report.write("\\cell");

        for(int i=0;i<20;i++) {
          pstmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso WHERE a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND ? AND a.Prinjat "+PRINJAT+" AND a.KodSpetsialnZach LIKE ?");
          pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
          pstmt.setObject(2,""+min_val[i],Types.VARCHAR);
          pstmt.setObject(3,""+max_val[i],Types.VARCHAR);
          pstmt.setObject(4,rs.getString(1),Types.VARCHAR);
          rs3 = pstmt.executeQuery();
          if(rs3.next()) curr_kol_abts = StringUtil.toInt(rs3.getString(1),0);
          else curr_kol_abts = 0;

          if(curr_kol_abts != 0)
            report.write("{"+curr_kol_abts+"}\\cell");
          else
            report.write("\\cell");
        }
        report.write("\\row");

        cur_cnt_prdms--;

        while(cur_cnt_prdms > 1) {

          if(rs2.next()) {

// Последующие предметы

            report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx1406\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2106\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2906\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3806\n");
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
            for(int i=1;i<=20;i++)
              report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx"+(5206+sub_table_step*i)+"\n");

            report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell{"+rs2.getString(1)+"}\n");
            report.write("\\cell");

            for(int i=0;i<20;i++) {
              pstmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso WHERE a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND ? AND a.Prinjat "+PRINJAT+" AND a.KodSpetsialnZach LIKE ?");
              pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
              pstmt.setObject(2,""+min_val[i],Types.VARCHAR);
              pstmt.setObject(3,""+max_val[i],Types.VARCHAR);
              pstmt.setObject(4,rs.getString(1),Types.VARCHAR);
              rs3 = pstmt.executeQuery();
              if(rs3.next()) curr_kol_abts = StringUtil.toInt(rs3.getString(1),0);
              else curr_kol_abts = 0;

              if(curr_kol_abts != 0)
                report.write("{"+curr_kol_abts+"}\\cell");
              else
                report.write("\\cell");
            }
            report.write("\\row");

          }
            cur_cnt_prdms--;
        }

// Последний предмет

        if(rs2.next()) {

          report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx1406\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2106\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx2906\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx3806\n");
          report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
          for(int i=1;i<=20;i++)
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx"+(5206+sub_table_step*i)+"\n");

          report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell{"+rs2.getString(1)+"}\n");
          report.write("\\cell");

          for(int i=0;i<20;i++) {
            pstmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso WHERE a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND ? AND a.Prinjat "+PRINJAT+" AND a.KodSpetsialnZach LIKE ?");
            pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
            pstmt.setObject(2,""+min_val[i],Types.VARCHAR);
            pstmt.setObject(3,""+max_val[i],Types.VARCHAR);
            pstmt.setObject(4,rs.getString(1),Types.VARCHAR);
            rs3 = pstmt.executeQuery();
            if(rs3.next()) curr_kol_abts = StringUtil.toInt(rs3.getString(1),0);
            else curr_kol_abts = 0;

            if(curr_kol_abts != 0)
              report.write("{"+curr_kol_abts+"}\\cell");
            else
              report.write("\\cell");
          }
          report.write("\\row");
        }

      }

/********************************/
/*****     Договорники      *****/
/********************************/

      cur_cnt_prdms = cnt_prdmts;

      pstmt = conn.prepareStatement("SELECT np.Predmet,np.KodPredmeta FROM EkzamenyNaSpetsialnosti ens, NazvanijaPredmetov np WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti LIKE ?");
      pstmt.setObject(1,rs.getString(1),Types.VARCHAR);
      rs2 = pstmt.executeQuery();
      if(rs2.next()) {

// Первый предмет

        report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
        report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx1406\n");
        report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2106\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2906\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3806\n");
        report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
        for(int i=1;i<=20;i++)
          report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx"+(5206+sub_table_step*i)+"\n");

        report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
        report.write("\\cell\n");
        report.write("\\cell{"+fo+"}\n");
        report.write("\\cell{контракт}\n");
        report.write("\\cell{"+rs2.getString(1)+"}\n");
        report.write("\\cell");

        for(int i=0;i<20;i++) {
          pstmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso WHERE a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND ? AND a.Prinjat "+PRINJAT+" AND a.KodSpetsialnZach LIKE ?");
          pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
          pstmt.setObject(2,""+min_val[i],Types.VARCHAR);
          pstmt.setObject(3,""+max_val[i],Types.VARCHAR);
          pstmt.setObject(4,rs.getString(1),Types.VARCHAR);
          rs3 = pstmt.executeQuery();
          if(rs3.next()) curr_kol_abts = StringUtil.toInt(rs3.getString(1),0);
          else curr_kol_abts = 0;

          if(curr_kol_abts != 0)
            report.write("{"+curr_kol_abts+"}\\cell");
          else
            report.write("\\cell");
        }
        report.write("\\row");

        cur_cnt_prdms--;

        while(cur_cnt_prdms > 1) {

          if(rs2.next()) {

// Последующие предметы

            report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx1406\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2106\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx2906\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3806\n");
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
            for(int i=1;i<=20;i++)
              report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx"+(5206+sub_table_step*i)+"\n");

            report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell{"+rs2.getString(1)+"}\n");
            report.write("\\cell");

            for(int i=0;i<20;i++) {
              pstmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso WHERE a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND ? AND a.Prinjat "+PRINJAT+" AND a.KodSpetsialnZach LIKE ?");
              pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
              pstmt.setObject(2,""+min_val[i],Types.VARCHAR);
              pstmt.setObject(3,""+max_val[i],Types.VARCHAR);
              pstmt.setObject(4,rs.getString(1),Types.VARCHAR);
              rs3 = pstmt.executeQuery();
              if(rs3.next()) curr_kol_abts = StringUtil.toInt(rs3.getString(1),0);
              else curr_kol_abts = 0;

              if(curr_kol_abts != 0)
                report.write("{"+curr_kol_abts+"}\\cell");
              else
                report.write("\\cell");
            }
            report.write("\\row");

          }
            cur_cnt_prdms--;
        }

// Последний предмет

        if(rs2.next()) {

          report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx1406\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx2106\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx2906\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx3806\n");
          report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
          for(int i=1;i<=20;i++)
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx"+(5206+sub_table_step*i)+"\n");

          report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell{"+rs2.getString(1)+"}\n");
          report.write("\\cell");

          for(int i=0;i<20;i++) {
            pstmt = conn.prepareStatement("SELECT COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso WHERE a.KodAbiturienta=zso.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND ? AND a.Prinjat "+PRINJAT+" AND a.KodSpetsialnZach LIKE ?");
            pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
            pstmt.setObject(2,""+min_val[i],Types.VARCHAR);
            pstmt.setObject(3,""+max_val[i],Types.VARCHAR);
            pstmt.setObject(4,rs.getString(1),Types.VARCHAR);
            rs3 = pstmt.executeQuery();
            if(rs3.next()) curr_kol_abts = StringUtil.toInt(rs3.getString(1),0);
            else curr_kol_abts = 0;

            if(curr_kol_abts != 0)
              report.write("{"+curr_kol_abts+"}\\cell");
            else
              report.write("\\cell");
          }
          report.write("\\row");
        }
     }
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
          if ( rs3 != null ) {
               try {
                     rs3.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( pstmt != null ) {
               try {
                     pstmt.close();
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
        if(rep_formaseven_f) return mapping.findForward("rep_formaseven_f");
        return mapping.findForward("success");
    }
}