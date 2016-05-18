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

public class Forma_6F_Action extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        Statement            stmt               = null;
        ResultSet            rs                 = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        Forma_6_Form         form               = (Forma_6_Form) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_forma_6f_f     = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        int                  Summa[]            = new int[40];
        int                  ItogSumm[]         = new int[40];
        String               Col[][]            = new String[1000][1000];
        ArrayList            abits_SD           = new ArrayList();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "forma_6f_Action", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "forma_6f_Form", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

          if ( form.getAction() == null ) {
               form.setAction(us.getClientIntName("view","init"));

/************************************************************************************************/
          } else if ( form.getAction().equals("report")) {

/************************************************/
/***** ����������� ����� � �������� ������� *****/
/************************************************/

    String name = "����� �6F";

    String file_con = "forma6f_"+StringUtil.CurrDate("-");

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("\\fs28 \\b1 \\qc �������� � ���������� (F6f) �� "+StringUtil.CurrDate(".")+" �. �� �����������\\line\n");
  report.write("\\par\n");
  report.write("\\fs16 \\b0 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx850\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2150\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2800\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3450\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\cellx6800\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10050\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10700\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12300\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13600\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15550\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx16200\n");

  String headline[] = new String[]{"����. ����.","���� ���- ��","����- ����  � ����.","����- ��� ��� ���.","���������","����� ����- �����","���","��. ����","�����������","���-��� �����","���. ����.","������. �����","����� ����������","����. � ���."};
  for(int i=0;i<headline.length;i++)
     report.write("\\intbl{"+headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs16 \\b0 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx850\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2150\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2800\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3450\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4550\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6800\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7450\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8100\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8750\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9400\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10050\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10700\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11100\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11500\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11900\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12300\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12950\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13600\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14250\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14900\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15550\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx16200\n");

  String sub_headline[] = new String[]{"","","","","","","�","�","���.","���.","��.","����� �����","���","���","���- �����","�/� �����","","�","�","�","�","����� ��","���","�����","����. ���.","��. ���.",""};
  for(int i=0;i<sub_headline.length;i++)
     report.write("\\intbl{"+sub_headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  int ind=0;      // ����� ����� ������������� ���������� ��������� �������� Abbr_Fak, Abbr_Spec, Kod_Spec
                  // �.�. ��� ���������� ����� ��������� ���������� ����� �������
  int cur_ind=0;  // � ������� ���� ���������� ���������� �������� ��������

  String  Abbr_Fak[] = new String[1000];	// ������������ �����������
  String  Abbr_Spc[] = new String[1000];	// ������������ ��������������
  String  Kod_Spec[] = new String[1000];	// ���� ��������������

/** �������� !!! �������� !!!**/
/** �������� !!! �������� !!!**/
/** �������� !!! �������� !!!**/

/*************************************************************/
/**** �������� ������ �������������: (��������), �� � ��� ****/
/*************************************************************/

      String include_specs = "SELECT spc.KodSpetsialnosti FROM Spetsialnosti spc, Fakultety fk WHERE spc.KodFakulteta=fk.KodFakulteta AND (spc.NazvanieSpetsialnosti LIKE '%(��������)' OR fk.AbbreviaturaFakulteta IN ('��','���'))";

	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT f.AbbreviaturaFakulteta, s.Abbreviatura, s.KodSpetsialnosti FROM Fakultety f, Spetsialnosti s WHERE s.KodFakulteta=f.KodFakulteta AND s.KodSpetsialnosti IN ("+include_specs+") ORDER BY s.Abbreviatura");
	while(rs.next())
	{
          Abbr_Fak[ind] = rs.getString(1);
          Abbr_Spc[ind] = rs.getString(2);
          Kod_Spec[ind] = rs.getString(3);
          ind++;
	}

// ��������� ����������

      int sel_ind = 0;


// ������������ ������� ������ �� �� ��� ������� ������� ��������.
// ������������ ����������� �� ���� �������������.

//PLAN_PRIEMA
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT PlanPriema,KodSpetsialnosti FROM Spetsialnosti ORDER BY KodSpetsialnosti");
      sel_ind = 0;
      while(rs.next())
      {
       Col[1][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//PODANO_ZAJAVLENIY (�������� � ��������)
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnosti), a.KodSpetsialnosti FROM Abiturient a WHERE a.DokumentyHranjatsja LIKE '�' AND a.KodAbiturienta NOT IN (SELECT KodAbiturienta FROM Otsenki WHERE CONVERT(int,Otsenka)<3 AND Otsenka NOT LIKE '+' AND Otsenka NOT LIKE '-') GROUP BY a.KodSpetsialnosti ORDER BY a.KodSpetsialnosti");
      sel_ind = 0;
      while(rs.next())
      {
       Col[2][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//PODANO_ZAJAVLENIY (������� ��� ����������)
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.KodSpetsialnZach NOT LIKE a.KodSpetsialnosti AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[3][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//ZACHISLENO
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[4][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//PLATN_DOGOVOR
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.NomerPlatnogoDogovora IS NOT NULL AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[5][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//POL ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.Pol LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[6][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//POL ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.Pol LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[7][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//IN_JAZ ('����')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.InostrannyjJazyk LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[8][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//IN_JAZ ('���')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.InostrannyjJazyk LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[9][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//IN_JAZ ('�����')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.InostrannyjJazyk LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[10][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//OBRAZOVANIE ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.TipOkonchennogoZavedenija LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[11][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//OBRAZOVANIE ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.TipOkonchennogoZavedenija LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[12][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//OBRAZOVANIE ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.TipOkonchennogoZavedenija LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[13][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//OBRAZOVANIE ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.TipOkonchennogoZavedenija LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[14][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//OBRAZOVANIE ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.TipOkonchennogoZavedenija LIKE '�' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[15][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//LGOTY
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Lgoty l WHERE a.KodLgot=l.KodLgot AND l.ShifrLgot NOT LIKE ('�') AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[16][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
// MEDAL ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Medali m WHERE a.KodMedali=m.KodMedali AND m.ShifrMedali LIKE '�' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[17][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
// MEDAL ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Medali m WHERE a.KodMedali=m.KodMedali AND m.ShifrMedali LIKE '�' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[18][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
// MEDAL ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Medali m WHERE a.KodMedali=m.KodMedali AND m.ShifrMedali LIKE '�' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[19][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
// MEDAL ('�')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Medali m WHERE a.KodMedali=m.KodMedali AND m.ShifrMedali LIKE '�' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[20][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//KURSY (��� ����)
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Kursy k WHERE a.KodKursov=k.KodKursov and k.ShifrKursov NOT LIKE '�' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[21][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//KURSY (���)
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Kursy k WHERE a.KodKursov=k.KodKursov and k.ShifrKursov LIKE '�' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[22][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//MESTO_PROZHIVANIJA !('�����')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE a.Prinjat IN ('1','2','3','4','�') and kodpunkta in ('1','584') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[23][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//MESTO_PROZHIVANIJA !('���������� �������')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Rajony r, Oblasti o WHERE a.KodRajona=r.KodRajona AND a.KodOblasti=o.KodOblasti AND o.NazvanieOblasti LIKE '������%' AND r.NazvanieRajona NOT LIKE '������%' AND a.Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[24][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//MESTO_PROZHIVANIJA !('������ �������')
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a, Oblasti o WHERE a.KodOblasti=o.KodOblasti AND o.NazvanieOblasti NOT LIKE '������%' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[25][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }
//NUZHDAETSJA_V_OBSHEZHITII
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT COUNT(a.KodSpetsialnZach), a.KodSpetsialnZach FROM Abiturient a WHERE NujdaetsjaVObschejitii LIKE '�' AND Prinjat IN ('1','2','3','4','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY a.KodSpetsialnZach ORDER BY a.KodSpetsialnZach");
      sel_ind = 0;
      while(rs.next())
      {
       Col[26][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
      }

 boolean Find = false;  // ������� ���� ���, ��� ������ ������ ���������� � ������� �������.
                        // ������� ���������� �� ���� �������������.
 int i = 0;

/***************************************/
/****  ������ ������ ������ �������  ***/
/***************************************/

  report.write("\\fs16 \\b0 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx850\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2150\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2800\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3450\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4550\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6800\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7450\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8750\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10050\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10700\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12950\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15550\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx16200\n");

//���� ��� ������������� ������� ������ ������� ( Kod_Spec[cur_ind] )
//������ � ������� ( Col[XX][i].substring(0,3) ) �� ������� �������� ����������
//�� ����� ������� � ��������������� ������, ����� ���������� 0.

  while(cur_ind < ind) // ������� ��������� ���������� �������
  {

/**********************************************************/
/****  ����� ����� ������� �� ���������� ������������� ****/
/**********************************************************/

/******* 0 *******/
    report.write("\\intbl "+Abbr_Spc[cur_ind]+" \\cell\n");

/****** 1-26 ******/
    for(int indx = 1; indx<=26; indx++) {
        i = 0;

       while(Col[indx][i] != null)
       {
         if(Col[indx][i].substring(0,Col[indx][i].indexOf("%")).equals(Kod_Spec[cur_ind]))
         {
          Find = true;
          break;
         }
         i++;
       }
       if(Find == true)
       {
         Summa[indx] += Integer.parseInt(Col[indx][i].substring(Col[indx][i].indexOf("%")+1));
         report.write("\\b0\\intbl{"+Col[indx][i].substring(Col[indx][i].indexOf("%")+1)+"}\\cell\n");
       } else report.write("\\intbl{0}\\cell\n");

       Find = false;
    }

    report.write("\\intbl  \\row\n");
    cur_ind++;

/********************************/
/****   ����� �� ����������   ***/
/********************************/

    if(!Abbr_Fak[cur_ind-1].equals(Abbr_Fak[cur_ind])) { // ���� ������� ������������ ���������� �� �����
                                                         // ���������� �� ������� ������ ����������
                                                         // ��������� ���������� �� ���� ��������������
                                                         // ����������

      report.write("\\b1\\intbl{"+Abbr_Fak[cur_ind-1]+"}\\cell\n");

      for(int indx = 1; indx<=26; indx++) {

         report.write("\\intbl "+Summa[indx]+"\\cell\n");

      }
      report.write("\\intbl\\row\\b0\n");

      for(int in=0;in<40;in++) {
        ItogSumm[in] += Summa[in];
        Summa[in] = 0;
      }
    }
  }

/********************************/
/****  ����� �� ���� � �����  ***/
/********************************/

report.write("\\b1\\intbl{�����}\\cell\n");

for(i=1;i<=26;i++) {

   report.write("\\intbl{"+ItogSumm[i]+"}\\cell\n");

}
report.write("\\intbl\\row\\pard\\par\n}");

//********************* 

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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(rep_forma_6f_f) return mapping.findForward("rep_forma_6f_f");
        return mapping.findForward("success");
    }
}