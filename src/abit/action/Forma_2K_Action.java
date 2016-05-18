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

public class Forma_2K_Action extends Action {

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
        Forma_2_Form         form               = (Forma_2_Form) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_formatwo_f  	= false;
        boolean              error              = false;
        ActionForward        f                  = null;
        int                  Summa[]            = new int[40];
        int                  ItogSumm[]         = new int[40];
        String               Col[][]            = new String[40][1000];
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "forma_2K_Action", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "forma_2K_Form", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
                 form.setAction(us.getClientIntName("view","init"));

            } else if ( form.getAction().equals("report")) {

/************************************************/
/***** ����������� ����� � �������� ������� *****/
/************************************************/

  String name = "����� �2�";

  String file_con = "forma2K_"+StringUtil.CurrDate("-");

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  report.write("{\\colortbl;\\red0\\green0\\blue0;\\red0\\green0\\blue255;\\red0\\green255\\blue255;\\red0\\green255\\blue0;\\red255\\green0\\blue255;\\red255\\green0\\blue0;\\red255\\green255\\blue0;\\red255\\green255\\blue255;\\red0\\green0\\blue128;\\red0\\green128\\blue128;\\red0\\green128\\blue0;\\red128\\green0\\blue128;\\red128\\green0\\blue0;\\red128\\green128\\blue0;\\red128\\green128\\blue128;\\red192\\green192\\blue192;}\n");
  report.write("{\\fs28 \\b1 \\qc �������� � ����������� ������������ � ��� �� ���������� ������� (F2�) �� "+StringUtil.CurrDate(".")+" �.\\line\n");
  report.write("\\par\n");
  report.write("\\fs16 \\b0 \\qc \\trowd\\trhdr \\trqc\\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx650\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1300\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1950\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2600\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3250\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4350\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5350\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7650\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8300\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10400\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13050\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15200\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800\n");

  String headline[] = new String[]{"���� ����.","���� ���- ��","����- �� �����.","���-����","�����. ����- �����","���-��� �����","����. ���.","���","�����������","� ���.","���. ����.","���������� �� ������","����� ����������","����- ��� �����"};
  for(int i=0;i<headline.length;i++)
     report.write("\\intbl{"+headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  report.write("\\fs16 \\b0 \\trowd\\trhdr \\trqc\\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx650\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1300\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1950\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2600\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3250\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4350\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4850\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5350\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5900\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7650\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8300\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8950\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9400\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9850\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10400\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10950\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11350\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11750\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12150\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12600\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13050\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13500\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14100\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14650\n");
  report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15200\n");
  report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800\n");

  String sub_headline[] = new String[]{"","","","","","","","�","�","����� �����","���","���","���- �����","","�","�","�","�","�","�","�","�","�","�","�����","���.� ���.","���.� ���.","��. ���.",""};
  for(int i=0;i<sub_headline.length;i++)
     report.write("\\intbl{"+sub_headline[i]+"}\\cell\n");
  report.write("\\intbl\\row\n");

  int ind=0;     // ����� �������� ���������� ��������� �������� Abbr_Fak, Abbr_Spc, Kod_Spec
                 // �.�. ��� ���������� �������� ���������� ����� �������
  int cur_ind=0; // � ������� ���� ���������� ������������ �������� ��������

  String  Abbr_Fak[] = new String[250];	// ������������ �����������
  String  Abbr_Spc[] = new String[250];	// ������������ ��������������
  String  Kod_Spec[] = new String[250];	// ���� ��������������
  String  Kod_Kong[] = new String[250];	// ���� ���������� �����


/** �������� !!! �������� !!!**/
/** �������� !!! �������� !!!**/
/** �������� !!! �������� !!!**/

/********************************************************************/
/**** ��������� �������������: (��������), ��, ���, ������ � ��� ****/
/********************************************************************/

      String exclude_specs = "SELECT spc.KodSpetsialnosti FROM Spetsialnosti spc, Fakultety fk WHERE spc.KodFakulteta=fk.KodFakulteta AND (spc.NazvanieSpetsialnosti LIKE '%(��������)' OR fk.AbbreviaturaFakulteta IN ('��','���','������','���'))";

	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT kg.Nazvanie, s.Abbreviatura, s.KodSpetsialnosti, kg.KodKonGruppy FROM KonGruppa kg, Spetsialnosti s,Fakultety f WHERE f.KodFakulteta=s.KodFakulteta AND s.KodKonGruppy=kg.KodKonGruppy AND s.KodSpetsialnosti NOT IN ("+exclude_specs+") ORDER BY kg.KodKonGruppy,s.Abbreviatura");
	while(rs.next())
	{
          Abbr_Fak[ind] = rs.getString(1);
          Abbr_Spc[ind] = rs.getString(2);
          Kod_Spec[ind] = rs.getString(3);
          Kod_Kong[ind] = rs.getString(4);
          ind++;
	}

// ������������� ����������

//      for(int i=0;i<40;i++) Summa[i] = 0; // ����� ����. ����� �������� � �������� ��� ������� ����������

//      for(int i=0;i<40;i++) ItogSumm[i] = 0; // �������� ��������

//      for(int i=0;i<40;i++) ItogSumm[i] = 0; // ����� ������� ���������� ������� ������� ������      

	float Konk     = 0; // ���������� ���
	float Konk1    = 0; // ��������
	float Konk2    = 0; // ��������

	int sel_ind = 0;    // ��������� ����������

// ����� ������������ ������� ������ �� �� ��� ������� ������� ��������. ������������
// ����������� �� ���� �������������.

//PLAN_PR
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT PlanPriema,KodSpetsialnosti,Abbreviatura FROM Spetsialnosti ORDER BY Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[1][sel_ind] = rs.getString(2)+"%0";
	 else Col[1][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//DOK_HRAN
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[2][sel_ind] = rs.getString(2)+"%0";
	 else Col[2][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//DOG
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND kon.Dog LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[4][sel_ind] = rs.getString(2)+"%0";
	 else Col[4][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//LGOTY !('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Lgoty l,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodLgot=l.KodLgot AND l.ShifrLgot NOT LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[5][sel_ind] = rs.getString(2)+"%0";
	 else Col[5][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//ATTESTAT
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.TipDokSredObraz LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[6][sel_ind] = rs.getString(2)+"%0";
	 else Col[6][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//POL ('�')
      stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.Pol LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[7][sel_ind] = rs.getString(2)+"%0";
	 else Col[7][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//POL ('�')
      stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.Pol LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[8][sel_ind] = rs.getString(2)+"%0";
	 else Col[8][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[9][sel_ind] = rs.getString(2)+"%0";
	 else Col[9][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[10][sel_ind] = rs.getString(2)+"%0";
	 else Col[10][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[11][sel_ind] = rs.getString(2)+"%0";
	 else Col[11][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TIP_ZAVED ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND TipOkonchennogoZavedenija LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[12][sel_ind] = rs.getString(2)+"%0";
	 else Col[12][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBSHEZHITIE
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND NujdaetsjaVObschejitii LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[13][sel_ind] = rs.getString(2)+"%0";
	 else Col[13][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('�','�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali IN ('�','�') AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[14][sel_ind] = rs.getString(2)+"%0";
	 else Col[14][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[15][sel_ind] = rs.getString(2)+"%0";
	 else Col[15][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[16][sel_ind] = rs.getString(2)+"%0";
	 else Col[16][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_MED ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Medali m,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND m.ShifrMedali LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[17][sel_ind] = rs.getString(2)+"%0";
	 else Col[17][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[18][sel_ind] = rs.getString(2)+"%0"; 
       else Col[18][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
       sel_ind++;
	}
//SHIFR_KURSOV ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[19][sel_ind] = rs.getString(2)+"%0";
	 else Col[19][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[20][sel_ind] = rs.getString(2)+"%0";
	 else Col[20][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[21][sel_ind] = rs.getString(2)+"%0";
	 else Col[21][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[22][sel_ind] = rs.getString(2)+"%0";
	 else Col[22][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//SHIFR_KURSOV ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Kursy k,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.KodKursov=k.KodKursov AND k.ShifrKursov LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[23][sel_ind] = rs.getString(2)+"%0";
	 else Col[23][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[24][sel_ind] = rs.getString(2)+"%0";
	 else Col[24][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[25][sel_ind] = rs.getString(2)+"%0";
	 else Col[25][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[26][sel_ind] = rs.getString(2)+"%0";
	 else Col[26][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//OBRAZOVANIE ('�')
	stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.GdePoluchilSrObrazovanie LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[27][sel_ind] = rs.getString(2)+"%0";
	 else Col[27][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
//TSELEVOJ_PRIEM
      stmt = conn.createStatement();
	rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,TselevojPriem tp,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND tp.KodTselevogoPriema=a.KodTselevogoPriema AND tp.ShifrPriema NOT LIKE '�' AND a.DokumentyHranjatsja LIKE '�' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
	sel_ind = 0;
	while(rs.next())
	{
         if(rs.getString(1) == null) Col[28][sel_ind] = rs.getString(2)+"%0";
	 else Col[28][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
	 sel_ind++;
	}
// ���������� ������������ �� ��������� �������������� 
        int kSpec = -1;
        sel_ind = 0;
        for(int row=0;row<1000;row++) {
          if(Col[1][row] != null && StringUtil.toInt(Col[1][row].substring(Col[1][row].indexOf("%")+1),0) != 0) {
            kSpec = StringUtil.toInt(Col[1][row].substring(0,Col[1][row].indexOf("%")),-1);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(s.KodSpetsialnosti), s.KodSpetsialnosti,s.Abbreviatura FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE '�' AND s.KodSpetsialnosti LIKE '"+kSpec+"' GROUP BY s.KodSpetsialnosti,s.Abbreviatura ORDER BY s.Abbreviatura");
            while(rs.next())
            {
              if(rs.getString(1) == null) Col[29][sel_ind] = rs.getString(2)+"%0";
              else Col[29][sel_ind] = rs.getString(2)+"%"+rs.getString(1);
              sel_ind++;
            }
          }
        }

boolean Find = false;   // ������� ���� ���, ��� ������ ������ ���������� � ������� �������.
                        // ������� ���������� �� ���� �������������.
int i = 0;

/***************************************/
/****  ������ ������ ������ �������  ***/
/***************************************/

  report.write("\\fs16 \\b0 \\trowd \\trqc \\trgaph58\\trrh80\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1950\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3800\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4350\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4850\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5350\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5900\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx7650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8300\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8950\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9850\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10400\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10950\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11350\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11750\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12150\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12600\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13050\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx13500\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx14650\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15200\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx15800\n");

	while(cur_ind < ind) // ������� ��������� ���������� �������
	{

//************************** ���������� �� �������������� ***************************

/*******************************/
/****  ����� ����� �������  ****/
/*******************************/

/******* 0 *******/
	report.write("\\b0\\intbl{"+Abbr_Spc[cur_ind]+"}\\cell\n");

/****** 1-28 ******/

for(int indx = 1; indx<=29; indx++) {
	i = 0;

	while(Col[indx][i] != null)
	{

// ���� ��� ������������� ������� ������ ������� ( Kod_Spec[cur_ind] )
// ������ � ������� ( Col[XX][i].substring(0,3) ) �� ��������� ��������������� ����������
// �� ����� ������� � ��������������� ������, ����� ������������ 0.

	 if(Col[indx][i].substring(0,Col[indx][i].indexOf("%")).equals(Kod_Spec[cur_ind]))
	 {
	  Find = true;
	  break;
	 }
	 i++;
	}
	if(Find == true)
	{

         if(indx == 29) {
           Summa[3] += Integer.parseInt(Col[29][i].substring(Col[29][i].indexOf("%")+1));
         }
         else
           Summa[indx] += Integer.parseInt(Col[indx][i].substring(Col[indx][i].indexOf("%")+1));

         if(indx == 1) Konk1 = Integer.parseInt(Col[1][i].substring(Col[1][i].indexOf("%")+1));
         if(indx == 2) Konk2 = Integer.parseInt(Col[2][i].substring(Col[2][i].indexOf("%")+1));

         if(indx != 29)
           report.write("\\intbl{"+Col[indx][i].substring(Col[indx][i].indexOf("%")+1)+"}\\cell\n");

	} else {
	 if(indx == 1) Konk1 = 0;
	 if(indx == 2) Konk2 = 0;
	 if(indx == 3) {
         if(Konk1 != 0) Konk = Math.round((Konk2/Konk1)*100f)/100f;
         else Konk = 0;
         if(indx != 29)
           report.write("\\intbl{"+Konk+"}\\cell\n");
       }
       else if(indx != 29) report.write("\\intbl{0}\\cell\n");
        }
	Find = false;
}

//*********************

report.write("\\intbl\\row\\b0\n");
cur_ind++;

//************************ ����� �� ���������� *************************
	 if(!Kod_Kong[cur_ind-1].equals(Kod_Kong[cur_ind])) { // ���� ������� ��� ���������� ������ �� �����
                                                            // ���� ���������� �� ������� ������ ����������
                                                            // ��������� ���������� �� ���� ��������������
                                                            // ���������� ������     
/********************************/
/****   ����� �� ����������   ***/
/********************************/

         report.write("\\b1\\intbl{"+Abbr_Fak[cur_ind-1]+"}\\cell\n");

         for(i=1;i<=28;i++) {
            if(i!=3) report.write("\\intbl{"+Summa[i]+"}\\cell\n");
            else {
              if(Summa[1]!=0)
                report.write("\\intbl{"+Math.round(((float)Summa[2]/(float)Summa[1])*100f)/100f+"}\\cell\n");
              else
                report.write("\\intbl{0}\\cell\n");
            }
         }

         report.write("\\intbl\\row\n");

         for(int in=0;in<40;in++) {
           ItogSumm[in] += Summa[in];
           Summa[in] = 0;
         }
       }
	}

/*******************************/
/****  ����� �� ���� � ����� ***/
/*******************************/

report.write("\\b1\\intbl{�����}\\cell\n");

for(i=1;i<=28;i++) {
  if(i == 3) {
    if(ItogSumm[1]!=0){
      report.write("\\intbl{"+Math.round(((float)ItogSumm[2]/(float)ItogSumm[1])*100f)/100f+"}\\cell\n");
    }
    else{
      report.write("\\intbl{0}\\cell\n");
    }
  } else
      report.write("\\intbl{"+ItogSumm[i]+"}\\cell\n");
}
report.write("\\intbl\\row\\pard\\par\n}");


/*********************************************************************/
/************ ����� ������ ���������� ����� � �������������� *********/
/*********************************************************************/

String kfaka[] = new String[250];
String fakul[] = new String[250];

  report.write("\\sect\\cols2{\\colsx708\\endnhere\\colno1\\colw7525\\colsr708\\colno2\\colw7427\n");
  report.write("\\fs24 \\b\\qc ���������� ������ � �� �������������\n");
  stmt = conn.createStatement();
  rs = stmt.executeQuery("SELECT kg.KodKonGruppy, kg.Nazvanie FROM KonGruppa kg,Fakultety f,Spetsialnosti s WHERE kg.KodKonGruppy=s.KodKonGruppy AND s.KodFakulteta=f.KodFakulteta AND s.KodSpetsialnosti NOT IN ("+exclude_specs+") GROUP BY kg.Nazvanie,kg.KodKonGruppy ORDER BY kg.KodKonGruppy");
  int stfak = 0;
  while(rs.next())
  {
    kfaka[stfak]=Integer.toString(rs.getInt(1));
    fakul[stfak]=rs.getString(2).toUpperCase();
    stfak++;
  }

  int stfak1 = stfak-1;
  stfak = 0;
  while(stfak<=stfak1)
  {
    if(stfak != 0) report.write("\\par");
    report.write("\\par\\fs16\\par\\highlight0\\fs22\\b\\qc{- "+fakul[stfak]+" -}\\highlight0\n");
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT Abbreviatura, NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodKonGruppy LIKE "+kfaka[stfak]+" ORDER BY Abbreviatura ASC");
    while(rs.next())
    {
     report.write("\\par");
     report.write("\\fs20\\b0\\ql{"+rs.getString(1)+"	"+rs.getString(2)+"}\n");
    }
    stfak++;
  }

report.write("}}"); 
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
        if(rep_formatwo_f) return mapping.findForward("rep_formatwo_f");
        return mapping.findForward("success");
    }
}