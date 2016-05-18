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

public class Forma_8_Action extends Action {

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
        String               Bud_Dog            = "";
        String               USE_ZEROs          = null;
        String               PRINJAT            = null;
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "forma_8_Action", new Boolean(true) );
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

  String name = "Форма №8";

  String fo = "_______________";

  if(abit_SD.getSpecial1() !=null && abit_SD.getSpecial1().equals("1")) USE_ZEROs = "Yes";
  else USE_ZEROs = null;

  if(abit_SD.getSpecial2() !=null && abit_SD.getSpecial2().equals("0")) PRINJAT = "";
  else if(abit_SD.getSpecial2() !=null && abit_SD.getSpecial2().equals("%")) PRINJAT = " AND a.Prinjat IN ('1','2','3','4','5','д')";
  else PRINJAT = " AND a.Prinjat LIKE '"+abit_SD.getSpecial2()+"'";

  String file_con = "forma8_"+abit_SD.getSpecial1()+"_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
  report.write("{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;}\n");
  report.write("\\fs18\\b1\\qc Сведения о средних баллах ЕГЭ по предметам вступительных испытаний на "+StringUtil.CurrDate(".")+" г.\\b0\\line\n");

  report.write("\\par\\b1\\fs16\\qc\\trowd\\trhdr\\trqc\\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9100\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");

  String headline[] = new String[]{"Специальность\\par по ОКСО","Аббр.","Код","Форма обучения","Форма финанси-\\par рования","Кол-во","Предмет ЕГЭ","Средний балл"};
  for(int i=0;i<headline.length;i++)
     report.write("\\intbl{"+headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs16\\b0\\trhdr\\trowd\\trqc \\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7800\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");

  for(int i=1;i<=8;i++)
     report.write("\\intbl\\b1{"+i+"}\\b0\\cell\n");

  report.write("\\intbl\\row\n");
  report.write("\\pard\n");

/********************************/
/****  Формат строки таблицы  ***/
/********************************/

  int cnt_prdmts = 0, cur_cnt_prdms = 0, curr_kol_abts = 0;

  float curr_ball = 0;

  pstmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti, sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti, sp.Tip_Spec FROM Spetsialnosti sp,Fakultety f WHERE sp.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND sp.Tip_Spec IN ('о','м') ORDER BY f.KodFakulteta,sp.Abbreviatura ASC");
  pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = pstmt.executeQuery();
  while(rs.next()){

  if(rs.getString(5) != null && rs.getString(5).equals("о")) fo = "очная";
  else if(rs.getString(5) != null && rs.getString(5).equals("з")) fo = "заочная";
  else if(rs.getString(5) != null && rs.getString(5).equals("в")) fo = "очно-заочная (вечерняя)";
  else if(rs.getString(5) != null && rs.getString(5).equals("м")) fo = "магистры";
  else if(rs.getString(5) != null && rs.getString(5).equals("д")) fo = "дистанционная";
  else if(rs.getString(5) != null && rs.getString(5).equals("у")) fo = "ускоренная";
  else if(rs.getString(5) != null && rs.getString(5).equals("ф")) fo = "ИИТО";
  else if(rs.getString(5) != null && rs.getString(5).equals("ю")) fo = "ЮК";

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
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3406\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx4106\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx6106\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7006\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7706\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx9006\n");
        report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9906\n");

        report.write("\\pard\\intbl\\nowidctlpar\\qc{"+rs.getString(4)+"}\n");
        report.write("\\cell{"+rs.getString(3)+"}\n");
        report.write("\\cell{"+rs.getString(2)+"}\n");
        report.write("\\cell{"+fo+"}\n");
        report.write("\\cell{бюджет}\n");

          pstmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=k.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND 100 AND k.KodSpetsialnosti LIKE ?"+PRINJAT);
          pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
          if(USE_ZEROs != null)
            pstmt.setObject(2,"0",Types.VARCHAR);
          else
            pstmt.setObject(2,"1",Types.VARCHAR);
          pstmt.setObject(3,rs.getString(1),Types.VARCHAR);
          rs3 = pstmt.executeQuery();
          if(rs3.next()) { 
            curr_ball = StringUtil.toInt(rs3.getString(1),0);
            curr_kol_abts = StringUtil.toInt(rs3.getString(2),0);
          }
          else {
            curr_ball = 0;
            curr_kol_abts = 0;
          }
          report.write("\\cell{"+curr_kol_abts+"}\n");
          report.write("\\cell{"+rs2.getString(1)+"}\n");
          report.write("\\cell");
          if(curr_kol_abts != 0)
            report.write("{"+(Math.round((curr_ball/curr_kol_abts)*100f)/100f)+"}\\cell");
          else
            report.write("\\cell");

        report.write("\\row");

        cur_cnt_prdms--;

        while(cur_cnt_prdms > 1) {

          if(rs2.next()) {

// Последующие предметы

            report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3406\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx4106\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx6106\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7006\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7706\n");
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9006\n");
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9906\n");

            report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");

            pstmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=k.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND 100 AND k.KodSpetsialnosti LIKE ?"+PRINJAT);
            pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
            if(USE_ZEROs != null)
              pstmt.setObject(2,"0",Types.VARCHAR);
            else
              pstmt.setObject(2,"1",Types.VARCHAR);
            pstmt.setObject(3,rs.getString(1),Types.VARCHAR);
            rs3 = pstmt.executeQuery();
            if(rs3.next()) { 
              curr_ball = StringUtil.toInt(rs3.getString(1),0);
              curr_kol_abts = StringUtil.toInt(rs3.getString(2),0);
            }
            else {
              curr_ball = 0;
              curr_kol_abts = 0;
            }
            report.write("\\cell{"+rs2.getString(1)+"}\n");
            report.write("\\cell");
            if(curr_kol_abts != 0)
              report.write("{"+(Math.round((curr_ball/curr_kol_abts)*100f)/100f)+"}\\cell");
            else
              report.write("\\cell");
            report.write("\\row");
          }
            cur_cnt_prdms--;
        }

// Последний предмет

        if(rs2.next()) {

          report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3406\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx4106\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx6106\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx7006\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx7706\n");
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9006\n");
          report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9906\n");

          report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");

          pstmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=k.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND 100 AND k.KodSpetsialnosti LIKE ?"+PRINJAT);
          pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
          if(USE_ZEROs != null)
            pstmt.setObject(2,"0",Types.VARCHAR);
          else
            pstmt.setObject(2,"1",Types.VARCHAR);
          pstmt.setObject(3,rs.getString(1),Types.VARCHAR);
          rs3 = pstmt.executeQuery();
          if(rs3.next()) { 
            curr_ball = StringUtil.toInt(rs3.getString(1),0);
            curr_kol_abts = StringUtil.toInt(rs3.getString(2),0);
          }
          else {
            curr_ball = 0;
            curr_kol_abts = 0;
          }
          report.write("\\cell{"+rs2.getString(1)+"}\n");
          report.write("\\cell");
          if(curr_kol_abts != 0)
            report.write("{"+(Math.round((curr_ball/curr_kol_abts)*100f)/100f)+"}\\cell");
          else
            report.write("\\cell");

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
        report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3406\n");
        report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx4106\n");
        report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx6106\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7006\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7706\n");
        report.write("\\clvmgf\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx9006\n");
        report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9906\n");

        report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
        report.write("\\cell\n");
        report.write("\\cell\n");
        report.write("\\cell{"+fo+"}\n");
        report.write("\\cell{контракт}\n");

        pstmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=k.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND 100 AND k.KodSpetsialnosti LIKE ?"+PRINJAT);
        pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
        if(USE_ZEROs != null)
          pstmt.setObject(2,"0",Types.VARCHAR);
        else
          pstmt.setObject(2,"1",Types.VARCHAR);
        pstmt.setObject(3,rs.getString(1),Types.VARCHAR);
        rs3 = pstmt.executeQuery();
        if(rs3.next()) { 
          curr_ball = StringUtil.toInt(rs3.getString(1),0);
          curr_kol_abts = StringUtil.toInt(rs3.getString(2),0);
        }
        else {
          curr_ball = 0;
          curr_kol_abts = 0;
        }
        report.write("\\cell{"+curr_kol_abts+"}\n");
        report.write("\\cell{"+rs2.getString(1)+"}\n");
        report.write("\\cell");
        if(curr_kol_abts != 0)
          report.write("{"+(Math.round((curr_ball/curr_kol_abts)*100f)/100f)+"}\\cell");
        else
          report.write("\\cell");

        report.write("\\row");

        cur_cnt_prdms--;

        while(cur_cnt_prdms > 1) {

          if(rs2.next()) {

// Последующие предметы

            report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx3406\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx4106\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx6106\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7006\n");
            report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1 \\cellx7706\n");
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9006\n");
            report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9906\n");

            report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");
            report.write("\\cell\n");

            pstmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=k.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND 100 AND k.KodSpetsialnosti LIKE ?"+PRINJAT);
            pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
            if(USE_ZEROs != null)
              pstmt.setObject(2,"0",Types.VARCHAR);
            else
              pstmt.setObject(2,"1",Types.VARCHAR);
            pstmt.setObject(3,rs.getString(1),Types.VARCHAR);
            rs3 = pstmt.executeQuery();
            if(rs3.next()) { 
              curr_ball = StringUtil.toInt(rs3.getString(1),0);
              curr_kol_abts = StringUtil.toInt(rs3.getString(2),0);
            }
            else {
              curr_ball = 0;
              curr_kol_abts = 0;
            }
            report.write("\\cell{"+rs2.getString(1)+"}\n");
            report.write("\\cell");
            if(curr_kol_abts != 0)
              report.write("{"+(Math.round((curr_ball/curr_kol_abts)*100f)/100f)+"}\\cell");
            else
              report.write("\\cell");

            report.write("\\row");
          }
            cur_cnt_prdms--;
        }

// Последний предмет

        if(rs2.next()) {

          report.write("\\trowd\\trgaph58\\trleft-58\\trqc\\trrh80\\trpaddl58\\trpaddr58\\trpaddfl3\\trpaddfr3\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx3406\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx4106\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx5206\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx6106\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx7006\n");
          report.write("\\clvmrg\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx7706\n");
          report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9006\n");
          report.write("\\clvertalc\\clbrdrl\\brdrw20\\brdrs\\brdrcf1\\clbrdrt\\brdrw20\\brdrs\\brdrcf1\\clbrdrr\\brdrw20\\brdrs\\brdrcf1\\clbrdrb\\brdrw20\\brdrs\\brdrcf1 \\cellx9906\n");

          report.write("\\pard\\intbl\\nowidctlpar\\qc\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");
          report.write("\\cell\n");

          pstmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(a.KodAbiturienta) FROM Abiturient a,ZajavlennyeShkolnyeOtsenki zso,Konkurs k WHERE a.KodAbiturienta=zso.KodAbiturienta AND a.KodAbiturienta=k.KodAbiturienta AND zso.KodPredmeta LIKE ? AND a.KodAbiturienta IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND zso.OtsenkaEge BETWEEN ? AND 100 AND k.KodSpetsialnosti LIKE ?"+PRINJAT);
          pstmt.setObject(1,rs2.getString(2),Types.VARCHAR);
          if(USE_ZEROs != null)
            pstmt.setObject(2,"0",Types.VARCHAR);
          else
            pstmt.setObject(2,"1",Types.VARCHAR);
          pstmt.setObject(3,rs.getString(1),Types.VARCHAR);
          rs3 = pstmt.executeQuery();
          if(rs3.next()) { 
            curr_ball = StringUtil.toInt(rs3.getString(1),0);
            curr_kol_abts = StringUtil.toInt(rs3.getString(2),0);
          }
          else {
            curr_ball = 0;
            curr_kol_abts = 0;
          }
          report.write("\\cell{"+rs2.getString(1)+"}\n");
          report.write("\\cell");
          if(curr_kol_abts != 0)
            report.write("{"+(Math.round((curr_ball/curr_kol_abts)*100f)/100f)+"}\\cell");
          else
            report.write("\\cell");

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
        if(rep_formaseven_f) return mapping.findForward("rep_formaeith_f");
        return mapping.findForward("success");
    }
}