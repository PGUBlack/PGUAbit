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

public class Itogi_Priema_Action extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session              = request.getSession();
        Connection           conn                 = null;
        PreparedStatement    stmt                 = null;
        ResultSet            rs                   = null;
        ResultSet            rs2                  = null;
        ActionErrors         errors               = new ActionErrors();
        ActionError          msg                  = null;
        Forma_2_Form         form                 = (Forma_2_Form) actionForm;
        AbiturientBean       abit_SD              = form.getBean(request, errors);
        boolean              rep_itogi_priema_f   = false;
        boolean              error                = false;
        ActionForward        f                    = null;
        int                  itogo[]              = new int[6];
        String               exclude_faks         = "";
        String               Bud_Dog              = "";
        String               NO_USE_NULL          = "";
        String               B_D                  = "";
        String               PRINJAT              = "";
        Date                 date                 = new Date();
        UserBean             user                 = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "itogi_Priema_Action", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "itogi_Priema_Form", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {


/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

  if(abit_SD.getSpecial2() !=null && abit_SD.getSpecial2().equals("bud")) { 
    Bud_Dog = "бюджетные";
    B_D = "NOT";
  }
  else {
    Bud_Dog = "внебюджетные";
    B_D = "";
  }

  for(int i=0;i<itogo.length;i++) itogo[i] = 0;

  if(abit_SD.getSpecial4() != null && abit_SD.getSpecial4().equals("on")) NO_USE_NULL = "HAVING SUM(zso.OtsenkaEge) NOT LIKE '0'";

  if(abit_SD.getSpecial1() !=null && abit_SD.getSpecial1().equals("%")) PRINJAT = "IN('1','2','3','4','д')";
  else PRINJAT = "LIKE '"+abit_SD.getSpecial1()+"'";

  String name = "Итоги приема ";

  if(abit_SD.getSpecial3() !=null && abit_SD.getSpecial3().equals("ukgr"))
    name += "(по укр. группам)";
  else
    name += "(по спец-тям)";

  name += " на "+Bud_Dog+" ("+abit_SD.getFormaOb()+") места от "+StringUtil.CurrDate("-");

  String file_con = "itogi_Priema_"+abit_SD.getSpecial3()+"_"+StringUtil.toEng(Bud_Dog)+"_"+StringUtil.toEng(abit_SD.getFormaOb())+"_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("\\pard\n");

  stmt = conn.prepareStatement("SELECT NazvanieVuzaFull,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs28\\b1\\qc{"+rs.getString(1)+"}\\par\\b0\\ul1{Итоги приема в высшие учебные заведения в "+StringUtil.CurrYear()+" году* на "+Bud_Dog+" места}\\ul0\\par\n");

  report.write("\\pard\\par\n");
  report.write("\\fs26\\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx6000\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx7000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12700\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx13900\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15000\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx16200\n");

  if(abit_SD.getSpecial3() !=null && abit_SD.getSpecial3().equals("ukgr"))
    report.write("\\intbl\\fs24\\qc\\b1{Наименование укрупненных групп направлений\\par (специальностей) ВПО}\\cell\n");
  else
    report.write("\\intbl\\fs24\\qc\\b1{Наименование направлений\\par (специальностей) ВПО}\\cell\n");

  report.write("\\intbl\\fs22\\qc{Кол-во подан-ных заявле-ний\\par(всего)}\\cell\n");
  report.write("\\intbl\\fs24\\qc{Итоги приема в высшие учебные заведения в "+StringUtil.CurrYear()+" году на бюджетные места, из них:}\\cell\n");
  report.write("\\intbl\\fs22\\qc{Всего зачисле-ны на "+Bud_Dog+" места}\\cell\n");
  report.write("\\intbl\\fs22\\qc{Средний балл по ЕГЭ}\\cell\n");
  report.write("\\intbl\\fs22\\qc{Минима-льный проход-ной балл по ЕГЭ}\\cell\n");
  report.write("\\intbl\\b0\\row\n");

  report.write("\\fs24 \\trowd\\trhdr\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx6000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx7000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx8000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx9600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx11000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12700\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx13900\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15000\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrdb\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx16200\n");

  report.write("\\intbl\\b1\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\fs22\\qc{На общих основа-ниях по резуль-татам ЕГЭ}\\cell\n");
  report.write("\\intbl\\fs22\\qc{Победители и призеры олимпиад школьников}\\cell\n");
  report.write("\\intbl\\fs22\\qc{Зачислен-ных из числа лиц, имеющих льготы}\\cell\n");
  report.write("\\intbl\\fs22\\qc{Зачисленных по целевому набору}\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\cell\n");
  report.write("\\intbl\\b0\\row\n");

  report.write("\\fs24 \\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx6000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx7000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx8000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx9600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx11000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx12700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx13900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx15000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx16200\n");


  if(abit_SD.getSpecial2() != null && abit_SD.getSpecial2().equals("bud")) Bud_Dog = "AND sp.PlanPriema NOT LIKE '0'";
  else Bud_Dog = "";

    if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
      stmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp,Fakultety f, Abiturient ab,Konkurs kon WHERE ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND sp.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+" GROUP BY sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti ORDER BY sp.ShifrSpetsialnosti ASC");
    else
      stmt = conn.prepareStatement("SELECT kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie, COUNT(ab.KodAbiturienta) FROM Abiturient ab,Konkurs kon,KonGruppa kg,Spetsialnosti sp WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kg.KodVuza LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+" GROUP BY kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie ORDER BY kg.Shifr ASC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
    rs = stmt.executeQuery();
    while(rs.next()){

// -- 1 -- Название специальности --

      report.write("\\intbl\\fs22\\ql{"+rs.getString(2)+"  "+rs.getString(4)+"}\\cell\n");

// -- 2 -- Количество заявлений --

      report.write("\\intbl\\qc{"+rs.getString(5)+"}\\cell\n");

      itogo[0] += StringUtil.toInt(rs.getString(5),0);

// -- 3 -- На общих основаниях --
      if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
        stmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med WHERE ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach LIKE ? AND sp.Tip_Spec LIKE ? AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti ORDER BY sp.ShifrSpetsialnosti ASC");
      else
        stmt = conn.prepareStatement("SELECT kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med,KonGruppa kg WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach IN (SELECT sp.KodSpetsialnosti FROM Spetsialnosti sp WHERE sp.KodKonGruppy LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+") AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie ORDER BY kg.Shifr ASC");
      stmt.setObject(1,rs.getString(1),Types.INTEGER);
      stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
      rs2 = stmt.executeQuery();
      if(rs2.next()){
        report.write("\\intbl\\qc{"+rs2.getString(5)+"}\\cell\n");

        itogo[1] += StringUtil.toInt(rs2.getString(5),0);

      } else {
        report.write("\\intbl\\qc{0}\\cell\n");
      }

// -- 4 -- Олимпиадники --
      if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
        stmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med WHERE ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach LIKE ? AND sp.Tip_Spec LIKE ? AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti ORDER BY sp.ShifrSpetsialnosti ASC");
      else
        stmt = conn.prepareStatement("SELECT kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med,KonGruppa kg WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach IN (SELECT sp.KodSpetsialnosti FROM Spetsialnosti sp WHERE sp.KodKonGruppy LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+") AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie ORDER BY kg.Shifr ASC");
      stmt.setObject(1,rs.getString(1),Types.INTEGER);
      stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
      rs2 = stmt.executeQuery();
      if(rs2.next()){
        report.write("\\intbl\\qc{"+rs2.getString(5)+"}\\cell\n");

        itogo[2] += StringUtil.toInt(rs2.getString(5),0);

      } else {
        report.write("\\intbl\\qc{0}\\cell\n");
      }

// -- 5 -- Льготники --
      if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
        stmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med WHERE ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach LIKE ? AND sp.Tip_Spec LIKE ? AND lg.ShifrLgot IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti ORDER BY sp.ShifrSpetsialnosti ASC");
      else
        stmt = conn.prepareStatement("SELECT kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med,KonGruppa kg WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach IN (SELECT sp.KodSpetsialnosti FROM Spetsialnosti sp WHERE sp.KodKonGruppy LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+") AND lg.ShifrLgot IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie ORDER BY kg.Shifr ASC");
      stmt.setObject(1,rs.getString(1),Types.INTEGER);
      stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
      rs2 = stmt.executeQuery();
      if(rs2.next()){
        report.write("\\intbl\\qc{"+rs2.getString(5)+"}\\cell\n");

        itogo[3] += StringUtil.toInt(rs2.getString(5),0);

      } else {
        report.write("\\intbl\\qc{0}\\cell\n");
      }

// -- 6 -- Целевики --
      if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
        stmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med WHERE ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach LIKE ? AND sp.Tip_Spec LIKE ? AND kon.Target LIKE 'д' AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti ORDER BY sp.ShifrSpetsialnosti ASC");
      else
        stmt = conn.prepareStatement("SELECT kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med,KonGruppa kg WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach IN (SELECT sp.KodSpetsialnosti FROM Spetsialnosti sp WHERE sp.KodKonGruppy LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+") AND kon.Target LIKE 'д' AND ab.Prinjat "+PRINJAT+" AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') GROUP BY kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie ORDER BY kg.Shifr ASC");
      stmt.setObject(1,rs.getString(1),Types.INTEGER);
      stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
      rs2 = stmt.executeQuery();
      if(rs2.next()){
        report.write("\\intbl\\qc{"+rs2.getString(5)+"}\\cell\n");

        itogo[4] += StringUtil.toInt(rs2.getString(5),0);

      } else {
        report.write("\\intbl\\qc{0}\\cell\n");
      }

// -- 7 -- Всего зачислено --
      if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
        stmt = conn.prepareStatement("SELECT sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon WHERE ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach LIKE ? AND sp.Tip_Spec LIKE ? AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND ab.Prinjat "+PRINJAT+" GROUP BY sp.PlanPriema,sp.KodSpetsialnosti,sp.ShifrSpetsialnosti, sp.Abbreviatura, sp.NazvanieSpetsialnosti ORDER BY sp.ShifrSpetsialnosti ASC");
      else
        stmt = conn.prepareStatement("SELECT kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie, COUNT(ab.KodAbiturienta) FROM Spetsialnosti sp, Abiturient ab,Konkurs kon,KonGruppa kg WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach IN (SELECT sp.KodSpetsialnosti FROM Spetsialnosti sp WHERE sp.KodKonGruppy LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+") AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND ab.Prinjat "+PRINJAT+" GROUP BY kg.KodKonGruppy,kg.Shifr, kg.Abbr, kg.Nazvanie ORDER BY kg.Shifr ASC");
      stmt.setObject(1,rs.getString(1),Types.INTEGER);
      stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
      rs2 = stmt.executeQuery();
      if(rs2.next()){
        report.write("\\intbl\\qc{"+rs2.getString(5)+"}\\cell\n");

        itogo[5] += StringUtil.toInt(rs2.getString(5),0);

      } else {
        report.write("\\intbl\\qc{0}\\cell\n");
      }

// -- 8 -- Средний балл --
      if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
        stmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(ens.KodPredmeta) FROM ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens, Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med WHERE ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ens.KodPredmeta=zso.KodPredmeta AND zso.KodAbiturienta=ab.KodAbiturienta AND ens.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach LIKE ? AND sp.Tip_Spec LIKE ? AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND ab.Prinjat "+PRINJAT+" AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL)");
      else
        stmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge),COUNT(ens.KodPredmeta) FROM ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens, Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med,KonGruppa kg WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ens.KodPredmeta=zso.KodPredmeta AND zso.KodAbiturienta=ab.KodAbiturienta AND ens.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach IN (SELECT sp.KodSpetsialnosti FROM Spetsialnosti sp WHERE sp.KodKonGruppy LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+") AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND ab.Prinjat "+PRINJAT+" AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL)");
      stmt.setObject(1,rs.getString(1),Types.INTEGER);
      stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
      rs2 = stmt.executeQuery();
      if(rs2.next()){
        report.write("\\intbl\\qc{"+Math.round(((float)StringUtil.toInt(rs2.getString(1),0)/(float)StringUtil.toInt(rs2.getString(2),1))*10f)/10f+"}\\cell\n");
      } else {
        report.write("\\intbl\\qc{-}\\cell\n");
      }

// -- 9 -- Минимальная сумма баллов --
      if(abit_SD.getSpecial3() != null && abit_SD.getSpecial3().equals("spec"))
        stmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge) FROM ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens, Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med WHERE ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ens.KodPredmeta=zso.KodPredmeta AND zso.KodAbiturienta=ab.KodAbiturienta AND ens.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach LIKE ? AND sp.Tip_Spec LIKE ? AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND ab.Prinjat "+PRINJAT+" AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) GROUP BY ab.KodAbiturienta "+NO_USE_NULL+" ORDER BY 1 ASC");
      else
        stmt = conn.prepareStatement("SELECT SUM(zso.OtsenkaEge) FROM ZajavlennyeShkolnyeOtsenki zso, EkzamenyNaSpetsialnosti ens, Spetsialnosti sp, Abiturient ab,Konkurs kon, Lgoty lg, Medali med,KonGruppa kg WHERE kg.KodKonGruppy=sp.KodKonGruppy AND ab.KodMedali=med.KodMedali AND ab.KodLgot=lg.KodLgot AND ab.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=sp.KodSpetsialnosti AND kon.KodSpetsialnosti=ab.KodSpetsialnZach AND ens.KodPredmeta=zso.KodPredmeta AND zso.KodAbiturienta=ab.KodAbiturienta AND ens.KodSpetsialnosti=ab.KodSpetsialnZach AND ab.KodSpetsialnZach IN (SELECT sp.KodSpetsialnosti FROM Spetsialnosti sp WHERE sp.KodKonGruppy LIKE ? AND sp.Tip_Spec LIKE ? "+Bud_Dog+") AND ab.KodAbiturienta "+B_D+" IN (SELECT KodAbiturienta FROM Konkurs WHERE Dog_Ok LIKE 'д') AND ab.Prinjat "+PRINJAT+" AND lg.ShifrLgot NOT IN('с','и') AND med.ShifrMedali NOT IN ('о','к') AND (kon.Target NOT LIKE 'д' OR kon.Target IS NULL) GROUP BY ab.KodAbiturienta "+NO_USE_NULL+" ORDER BY 1 ASC");
      stmt.setObject(1,rs.getString(1),Types.INTEGER);
      stmt.setObject(2,abit_SD.getFormaOb(),Types.VARCHAR);
      rs2 = stmt.executeQuery();
      if(rs2.next()){
        report.write("\\intbl\\qc{"+rs2.getString(1)+"}\\cell\n");
      } else {
        report.write("\\intbl\\qc{-}\\cell\n");
      }

      report.write("\\intbl\\row\n");
    }

// -- ИТОГО --

    report.write("\\intbl\\b1\\ql{ВСЕГО:}\\cell\n");
    report.write("\\intbl\\qc{"+itogo[0]+"}\\cell\n");
    report.write("\\intbl\\qc{"+itogo[1]+"}\\cell\n");
    report.write("\\intbl\\qc{"+itogo[2]+"}\\cell\n");
    report.write("\\intbl\\qc{"+itogo[3]+"}\\cell\n");
    report.write("\\intbl\\qc{"+itogo[4]+"}\\cell\n");
    report.write("\\intbl\\qc{"+itogo[5]+"}\\cell\n");
    report.write("\\intbl\\qc{}\\cell\n");
    report.write("\\intbl\\qc{}\\b0\\cell\n");
    report.write("\\intbl\\row\n");

    report.write("\\pard\\par\n");

    String fo = "_______________";

    if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("о")) fo = "очной";

    else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("з")) fo = "заочной";
    else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("в")) fo = "очно-заочной (вечерней)";
    else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("м")) fo = "магистерской";
    else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("д")) fo = "дистанционной";
    else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("у")) fo = "ускоренной";
    else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("ф")) fo = "ИИТО";
    else if(abit_SD.getFormaOb() != null && abit_SD.getFormaOb().equals("ю")) fo = "ЮК";

    report.write("\\tab{*) по "+fo+" форме обучения}\n");
    report.write("}");
    report.close();

    form.setAction(us.getClientIntName("new_rep","crt"));
    return mapping.findForward("rep_brw");

  }

 }  catch ( SQLException e ) {
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
        if(rep_itogi_priema_f) return mapping.findForward("rep_itogi_priema_f");
        return mapping.findForward("success");
    }
}