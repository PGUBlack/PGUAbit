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
import abit.util.StringUtil;

import java.io.*;

import abit.sql.*; 

public class SearchRTFAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        ResultSet         rs            = null;
        PreparedStatement stmt_a        = null;
        ResultSet         rs_a          = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        AbiturientForm    form          = (AbiturientForm) actionForm;
        AbiturientBean    abit_Srch     = form.getBean(request, errors);
        boolean           error         = false;
        ActionForward     f             = null;
        ArrayList         abits_Srch    = new ArrayList();
        UserBean          user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "searchAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/****************************************************************************************/
/********************  ��������� ���������� � �� � ������� ����������  *****************/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "searchForm", form );

/*****************  ������� � ������+���� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************** ���������� ������ ��� ����� � ������� ���������� ************************/
      if ( form.getAction() == null ) {
       form.setAction(us.getClientIntName("printForm","init"));
      }else if ( form.getAction().equals("makeRTF") ) {
	 int WidthOfTablo = 0;                          //���� ����������� ����� ������ ���� �������� ��������
       String MassForCol[][]   = new String[60][2];	//������, � ������� ����� �������� �����
	 								//��������� ��������

	 if(abit_Srch.getDokumentyHranjatsja()!=null)	//���� ���������� BEANa �������, ��
									//������� ��� ������
	 {
		//� ������ � ��������� ������� ����� ��������� �������
	  MassForCol[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][0] = "�����. ����.";
		//����� ������ ������� ��� �������� � ��� RTF
	  MassForCol[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][1] = "870";
		//����������� ���������� ���������� ������ �������
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getTipDokSredObraz()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokSredObraz())][0] = "����� ���.";
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokSredObraz())][1] = "870";
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getFormaOb()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getFormaOb())][0] = "����� ��.";
	  MassForCol[Integer.parseInt(abit_Srch.getFormaOb())][1] = "870";
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getOsnovaOb()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getOsnovaOb())][0] = "������ ��.";
	  MassForCol[Integer.parseInt(abit_Srch.getOsnovaOb())][1] = "870";
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getSpecial1()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial1())][0] = "����. ����.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial1())][1] = "740";
	  WidthOfTablo += 740;
	 }

	 if(abit_Srch.getNomerLichnogoDela()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][0] = "����� ����. ����";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][1] = "950";
	  WidthOfTablo += 950;
	 }

	 if(abit_Srch.getFamilija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getFamilija())][0] = "�������";
	  MassForCol[Integer.parseInt(abit_Srch.getFamilija())][1] = "1670";
	  WidthOfTablo += 1670;
	 }

	 if(abit_Srch.getImja()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getImja())][0] = "���";
	  MassForCol[Integer.parseInt(abit_Srch.getImja())][1] = "1357";
	  WidthOfTablo += 1357;
	 }

	 if(abit_Srch.getOtchestvo()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getOtchestvo())][0] = "��������";
	  MassForCol[Integer.parseInt(abit_Srch.getOtchestvo())][1] = "2100";
	  WidthOfTablo += 2250;
	 }

	/* if(abit_Srch.getShifrKursov()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getShifrKursov())][0] = "�����";
	  MassForCol[Integer.parseInt(abit_Srch.getShifrKursov())][1] = "803";
	  WidthOfTablo += 803;
	 }

	 if(abit_Srch.getMedal()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getMedal())][0] = "�������. ����������";
	  MassForCol[Integer.parseInt(abit_Srch.getMedal())][1] = "991";
	  WidthOfTablo += 991;
	 }
*/
	 if(abit_Srch.getLgoty()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getLgoty())][0] = "������ �����";
	  MassForCol[Integer.parseInt(abit_Srch.getLgoty())][1] = "890";
	  WidthOfTablo += 890;
	 }

	 if(abit_Srch.getNomerPlatnogoDogovora()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][0] = "����� �����. �����.";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][1] = "1390";
	  WidthOfTablo += 1390;
	 }

	 if(abit_Srch.getSpecial2()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial2())][0] = "��� ����.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial2())][1] = "1200";
	  WidthOfTablo += 1200;
	 }

	 if(abit_Srch.getSpecial5()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial5())][0] = "��� �����. �����. ���.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial5())][1] = "805";
	  WidthOfTablo += 805;
	 }

	 if(abit_Srch.getPol()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getPol())][0] = "���";
	  MassForCol[Integer.parseInt(abit_Srch.getPol())][1] = "590";
	  WidthOfTablo += 590;
	 }

	 if(abit_Srch.getSrokObuchenija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSrokObuchenija())][0] = "���� ��������";
	  MassForCol[Integer.parseInt(abit_Srch.getSrokObuchenija())][1] = "1060";
	  WidthOfTablo += 1060;
	 }

	 if(abit_Srch.getGrajdanstvo()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getGrajdanstvo())][0] = "�����.";
	  MassForCol[Integer.parseInt(abit_Srch.getGrajdanstvo())][1] = "850";
	  WidthOfTablo += 850;
	 }

	 if(abit_Srch.getGdePoluchilSrObrazovanie()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][0] = "��� ������� ��. ���.";
	  MassForCol[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][1] = "970";
	  WidthOfTablo += 970;
	 }

	 if(abit_Srch.getNomerShkoly()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerShkoly())][0] = "����� �����";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerShkoly())][1] = "840";
	  WidthOfTablo += 840;
	 }

	 if(abit_Srch.getInostrannyjJazyk()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getInostrannyjJazyk())][0] = "��. ��.";
	  MassForCol[Integer.parseInt(abit_Srch.getInostrannyjJazyk())][1] = "540";
	  WidthOfTablo += 540;
	 }

	 if(abit_Srch.getNujdaetsjaVObschejitii()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNujdaetsjaVObschejitii())][0] = "����. � �����.";
	  MassForCol[Integer.parseInt(abit_Srch.getNujdaetsjaVObschejitii())][1] = "890";
	  WidthOfTablo += 890;
	 }

	 if(abit_Srch.getNazvanie()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanie())][0] = "���������� �����";
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanie())][1] = "1970";
	  WidthOfTablo += 1970;
	 }

	 if(abit_Srch.getNazvanieRajona()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieRajona())][0] = "�����";
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieRajona())][1] = "2190";
	  WidthOfTablo += 2190;
	 }

	 if(abit_Srch.getNazvanieOblasti()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieOblasti())][0] = "�������";
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieOblasti())][1] = "1510";
	  WidthOfTablo += 1510;
	 }

	 if(abit_Srch.getPolnoeNaimenovanieZavedenija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][0] = "������ ������������ ���������";
	  MassForCol[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][1] = "2190";
	  WidthOfTablo += 2190;
	 }

	 if(abit_Srch.getTipOkonchennogoZavedenija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][0] = "��� �����. �����.";
	  MassForCol[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][1] = "803";
	  WidthOfTablo += 803;
	 }

	/* if(abit_Srch.getTrudovajaDejatelnost()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTrudovajaDejatelnost())][0] = "����. ����.";
	  MassForCol[Integer.parseInt(abit_Srch.getTrudovajaDejatelnost())][1] = "740";
	  WidthOfTablo += 740;
	 }*/

	 if(abit_Srch.getGruppa()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getGruppa())][0] = "������";
	  MassForCol[Integer.parseInt(abit_Srch.getGruppa())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getNapravlenieOtPredprijatija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNapravlenieOtPredprijatija())][0] = "���������. �� ������.";
	  MassForCol[Integer.parseInt(abit_Srch.getNapravlenieOtPredprijatija())][1] = "1330";
	  WidthOfTablo += 1330;
	 }

	 if(abit_Srch.getTipDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokumenta())][0] = "��� �����.";
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokumenta())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getSeriaDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaDokumenta())][0] = "����� ����.";
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaDokumenta())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getNomerDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerDokumenta())][0] = "� ��������";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerDokumenta())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getSeriaAtt()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaAtt())][0] = "����� ���.";
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaAtt())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getNomerAtt()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerAtt())][0] = "� ������.";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerAtt())][1] = "985";
	  WidthOfTablo += 985;
	 }

	 if(abit_Srch.getDataVydDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getDataVydDokumenta())][0] = "���� ���. ����.";
	  MassForCol[Integer.parseInt(abit_Srch.getDataVydDokumenta())][1] = "1200";
	  WidthOfTablo += 1200;
	 }

	 if(abit_Srch.getKemVydDokument()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getKemVydDokument())][0] = "��� ����� �������";
	  MassForCol[Integer.parseInt(abit_Srch.getKemVydDokument())][1] = "1600";
	  WidthOfTablo += 1600;
	 }

	 if(abit_Srch.getSobesedovanie()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSobesedovanie())][0] = "�������.";
	  MassForCol[Integer.parseInt(abit_Srch.getSobesedovanie())][1] = "1040";
	  WidthOfTablo += 1040;
	 }

	 if(abit_Srch.getKodTselevogoPriema()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTselevojPriem())][0] = "������� �����";
	  MassForCol[Integer.parseInt(abit_Srch.getTselevojPriem())][1] = "1040";
	  WidthOfTablo += 1040;
	 }

	 if(abit_Srch.getSpecial7()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial7())][0] = "����";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial7())][1] = "740";
	  WidthOfTablo += 740;
	 }

	 if(abit_Srch.getPrinjat()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getPrinjat())][0] = "��������";
	  MassForCol[Integer.parseInt(abit_Srch.getPrinjat())][1] = "1100";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getShifrFakulteta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getShifrFakulteta())][0] = "���������";
	  MassForCol[Integer.parseInt(abit_Srch.getShifrFakulteta())][1] = "1150";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getAttestat()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getAttestat())][0] = "����� ���. �������";
	  MassForCol[Integer.parseInt(abit_Srch.getAttestat())][1] = "1475";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getKopijaSertifikata()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getKopijaSertifikata())][0] = "����� ������.";
	  MassForCol[Integer.parseInt(abit_Srch.getKopijaSertifikata())][1] = "1100";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getSpecial8()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial8())][0] = "����. ���.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial8())][1] = "740";
	  WidthOfTablo += 740;
	 }
	 
	 if(abit_Srch.getTel()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTel())][0] = "�������";
	  MassForCol[Integer.parseInt(abit_Srch.getTel())][1] = "1670";
	  WidthOfTablo += 1670;
	 }
	 if(abit_Srch.getPreemptiveRight()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getPreemptiveRight())][0] = "�����. �����";
	  MassForCol[Integer.parseInt(abit_Srch.getPreemptiveRight())][1] = "890";
	  WidthOfTablo += 890;
	 }
	 if(abit_Srch.getReturnDocument()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getReturnDocument())][0] = "����. �������� ���.";
	  MassForCol[Integer.parseInt(abit_Srch.getReturnDocument())][1] = "890";
	  WidthOfTablo += 890;
	 }
	 if(abit_Srch.getDopAddress()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getDopAddress())][0] = "�������� �����";
	  MassForCol[Integer.parseInt(abit_Srch.getDopAddress())][1] = "890";
	  WidthOfTablo += 890;
	 }
	 if(abit_Srch.getProvidingSpecialCondition()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getProvidingSpecialCondition())][0] = "����. �������";
	  MassForCol[Integer.parseInt(abit_Srch.getProvidingSpecialCondition())][1] = "890";
	  WidthOfTablo += 890;
	 }

//<<<<<<<<<<<<<<<<<<<< ���� ������ ������� ��������� ������ ����� <<<<<<<<<<<<<<<<<<<<<<<
	 boolean prizn = false;
	 if(abit_Srch.getPriznakSortirovki().equals("���������"))
	 {
	  if(WidthOfTablo > 16840)
		prizn = true;
	 } else if(WidthOfTablo > 11907)
		{
		 prizn = true;
		}
	 if(prizn == true)
	 {
	  abit_Srch.setSpecial22(new Integer(20));	//������������� ������� �������� ��� JSP
        request.setAttribute("abit_Srch", abit_Srch);
        request.setAttribute("abits_Srch", abits_Srch);
        form.setAction(us.getClientIntName("printForm","reentr-out-page"));
        return mapping.findForward("success");
	 }

/************************************************/
/***** ����������� ����� � �������� ������� *****/
/************************************************/

    String name = "���������� ������ �� "+StringUtil.CurrDate(".")+" �� "+StringUtil.CurrTime(":");

    String file_con = new String("search_d_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

	 report.write("{\\rtf1\\ansi\n");

	 if(abit_Srch.getPriznakSortirovki().equals("���������"))
	 {
report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
	 }
	 if(abit_Srch.getSpecial3() != null)	//����� ���������
	 {
	  report.write("\\fs40 \\qc \\b1"+abit_Srch.getSpecial3()+"\\b0\n");
	  report.write("\\par\\par\n");
	 }
	 if(abit_Srch.getSpecial4() != null)	//����� ������������
	 {
	  report.write("\\fs32 \\qc "+abit_Srch.getSpecial4()+"\n");
	  report.write("\\par\\par\n");
	 }

	 int i = 1;
	 int tabulator = 700;   //������ ������� � �������� �����

// ������ ����� �������

	 report.write("\\fs20 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
	 report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");

	 while(MassForCol[i][0]!=null)	
	 {
	  tabulator += Integer.parseInt(MassForCol[i++][1]);
          report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
	 }

//����� ����� �������

	 i = 1;

         report.write("\\intbl{�}\\cell\n");

	 while(MassForCol[i][0]!=null)	
	 {
          report.write("\\intbl{"+MassForCol[i++][0]+"}\\cell\n");
	 }

         report.write("\\intbl\\row\n");

// ������ ����� �������

	 i = 1;
	 tabulator = 700;

	 report.write("\\fs20 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	 report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");

	 while(MassForCol[i][0]!=null)	
	 {
	  tabulator += Integer.parseInt(MassForCol[i++][1]);
          report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
	 }

/****************************** ������� ������������ �� �� ******************************/
  String MasSelector[][] = new String[60][2];	//������ ������������ ��� ����������
						//������ ���������� � ���������� 
						//���������� �������
  int masInc = 0;				//���� ����������� ���������� ��������� ��������
  int xxx = 1;					//������������ � �������� ������� ������� MasSelector
  int number = 1;				//����� ������ �������



  /****************************** ������� ������������ �� �� ******************************/
  StringBuffer query = new StringBuffer("SELECT Abiturient.KodAbiturienta,DokumentyHranjatsja,Abbreviatura,");

  if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
    query.append("Konkurs.NomerLichnogoDela");
  else
    query.append("Abiturient.NomerLichnogoDela");

  if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
      query.append(",Familija,Imja,Otchestvo,NomerPlatnogoDogovora,DataRojdenija,Pol,SrokObuchenija,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,NomerShkoly,InostrannyjJazyk,NujdaetsjaVObschejitii,Grajdanstvo,PolnoeNaimenovanieZavedenija,TipOkonchennogoZavedenija,Abiturient.TrudovajaDejatelnost,Gruppa,NapravlenieOtPredprijatija,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,Abiturient.Sobesedovanie,NomerSertifikata,KopijaSertifikata,Ball,Prinjat,KodSpetsialnZach,SeriaAtt,NomerAtt,Spetsialnosti.KodSpetsialnosti,ShifrPriema,ShifrLgot,AbbreviaturaFakulteta,Stob,Tel,abitemail, kodpunkta, kodformyob, kodOsnovyOb FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy,AbitDopInf,TselevojPriem,Lgoty,Konkurs");
  else
      query.append(",Familija,Imja,Otchestvo,NomerPlatnogoDogovora,DataRojdenija,Pol,SrokObuchenija,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,NomerShkoly,InostrannyjJazyk,NujdaetsjaVObschejitii,Grajdanstvo,PolnoeNaimenovanieZavedenija,TipOkonchennogoZavedenija,Abiturient.TrudovajaDejatelnost,Gruppa,NapravlenieOtPredprijatija,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,Abiturient.Sobesedovanie,NomerSertifikata,KopijaSertifikata,Ball,Prinjat,KodSpetsialnZach,SeriaAtt,NomerAtt,Spetsialnosti.KodSpetsialnosti,ShifrPriema,ShifrLgot,AbbreviaturaFakulteta,Stob,Prioritet,Tel,abitemail,kodpunkta,kodformyob, kodOsnovyOb FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy,AbitDopInf,TselevojPriem,Lgoty,Konkurs");/////

   //  if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
   //      query.append(",Konkurs");
  //if((""+session.getAttribute("otsenka_Ege")).equals("on"))  
   //  	query.append(",ZajavlennyeShkolnyeOtsenki.KodAbiturienta FROM ZajavlennyeShkolnyeOtsenki");
 
  query.append(" WHERE ");
  if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
	    query.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND Konkurs.Target = TselevojPriem.kodTselevogoPriema AND Konkurs.OP = Lgoty.KodLgot");//AND PreemptiveRight.KodAbiturienta=Abiturient.KodAbiturienta AND AbitDopInf.KodAbiturienta=Abiturient.KodAbiturienta 
	  else
	    query.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND Abiturient.kodTselevogoPriema = TselevojPriem.kodTselevogoPriema AND Konkurs.Prioritet = '1' AND  konkurs.kodabiturienta = abiturient.kodabiturienta AND Konkurs.OP = Lgoty.KodLgot");/////////////////////AND PreemptiveRight.KodAbiturienta=Abiturient.KodAbiturienta AND AbitDopInf.KodAbiturienta=Abiturient.KodAbiturienta 

	  
	  
	  query.append(" AND Gruppy.KodGruppy=Abiturient.KodGruppy AND  Abitdopinf.kodAbiturienta = Abiturient.kodAbiturienta AND  Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND Abiturient.KodZavedenija = Zavedenija.KodZavedenija  AND Abiturient.KodVuza = ");//���
	  query.append(session.getAttribute("kVuza"));


//******** ������ ��������������� ������� **********

  StringBuffer condition = new StringBuffer();
 if(!(""+session.getAttribute("SeriaAtt")).equals("%"))
  condition.append(" AND (SeriaAtt LIKE "+"'"+session.getAttribute("SeriaAtt")+"' OR SeriaAtt IS NULL)");
 if(!(""+session.getAttribute("NomerAtt")).equals("%"))
  condition.append(" AND (NomerAtt LIKE "+"'"+session.getAttribute("NomerAtt")+"' OR NomerAtt IS NULL)");

 //��������
 String vidDokCondition = "";
 
 if(!(""+session.getAttribute("SrokObuchenija")).equals("%"))
 {
	   condition.append(" AND NomerPotoka = '"+session.getAttribute("SrokObuchenija")+"'");
 }
 
 if(!(""+session.getAttribute("kodFormyOb")).equals("%"))
 {
	   condition.append(" AND kodFormyOb IN ("+session.getAttribute("KodFormyOb")+")");
 }
 
 if(!(""+session.getAttribute("kodOsnovyOb")).equals("%"))
 {
	   condition.append(" AND kodOsnovyOb IN ("+session.getAttribute("KodOsnovyOb")+")");
 }
 
	   
 
 if((""+session.getAttribute("bud1")).equals("on"))  
 {
	   condition.append(" AND AbitDopInf.Dist not in ('-')");
 }
 
 if((""+session.getAttribute("bud2")).equals("on"))  
 {
	   condition.append(" AND Konkurs.Stob = '1' ");
 }
 
 if(!(""+session.getAttribute("KodLgot")).equals("%"))  
	   condition.append(" AND Konkurs.OP LIKE "+"'"+session.getAttribute("KodLgot")+"'");
 if(!(""+session.getAttribute("KodTselevogoPriema")).equals("%")) {

	 //   if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
 condition.append(" AND Konkurs.target LIKE "+"'"+session.getAttribute("KodTselevogoPriema")+"'");
//   else
//      condition.append(" AND Abiturient.KodTselevogoPriema LIKE "+"'"+session.getAttribute("KodTselevogoPriema")+"'");
}
 

 
//   if(!(""+session.getAttribute("KodTselevogoPriema")).equals("%"))  
//	   condition.append(" AND Konkurs.target LIKE "+"'"+session.getAttribute("KodTselevogoPriema")+"'");
//       else
//  	   condition.append(" AND Abiturient.KodTselevogoPriema LIKE "+"'"+session.getAttribute("KodTselevogoPriema")+"'");
 
 
 if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))
	   vidDokCondition =   " AND viddoksredobraz ='"+session.getAttribute("VidDokSredObraz")+"'";
 
 if((""+session.getAttribute("VidDokSredObraz")).equals("������ ��/���"))
	   vidDokCondition = " AND (viddoksredobraz = '������ ���' OR viddoksredobraz = '������ ���')";
	   
 if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))  
				   condition.append(vidDokCondition);

	   
	/*   condition.append(" AND (viddoksredobraz = '������ ��/���' OR viddoksredobraz = '������ C��')");
 else if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))
     condition.append(" AND (NomerAtt LIKE "+"'"+session.getAttribute("NomerAtt")+"' OR NomerAtt IS NULL)");*/
 
 
 
/*  if(!(""+session.getAttribute("KodOsnovyOb")).equals("0"))
  condition.append(" AND Abiturient.KodOsnovyOb LIKE "+"'"+session.getAttribute("KodOsnovyOb")+"'");
 if(!(""+session.getAttribute("KodFormyOb")).equals("0"))
  condition.append(" AND Abiturient.KodFormyOb LIKE "+"'"+session.getAttribute("KodFormyOb")+"'");*/
 if(!(""+session.getAttribute("DokumentyHranjatsja")).equals("%"))
  condition.append(" AND DokumentyHranjatsja LIKE "+"'"+session.getAttribute("DokumentyHranjatsja")+"'");
 if(!(""+session.getAttribute("ShifrFakulteta")).equals("%"))
  condition.append(" AND Fakultety.ShifrFakulteta LIKE "+"'"+session.getAttribute("ShifrFakulteta")+"'");
//System.out.println(">>"+session.getAttribute("Special1"));
  condition.append(" AND Spetsialnosti.KodSpetsialnosti LIKE "+"'"+session.getAttribute("Special1")+"'");
 if(!(""+session.getAttribute("NomerLichnogoDela")).equals("%")) {
   if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
     condition.append(" AND Konkurs.NomerLichnogoDela LIKE "+"'"+session.getAttribute("NomerLichnogoDela")+"'");
   else
     condition.append(" AND Abiturient.NomerLichnogoDela LIKE "+"'"+session.getAttribute("NomerLichnogoDela")+"'");
 }
 if(!(""+session.getAttribute("Familija")).equals("%"))
  condition.append(" AND Familija LIKE "+"'"+session.getAttribute("Familija")+"'");
 if(!(""+session.getAttribute("Imja")).equals("%"))
  condition.append(" AND Imja LIKE "+"'"+session.getAttribute("Imja")+"'");
 if(!(""+session.getAttribute("Otchestvo")).equals("%"))
  condition.append(" AND Otchestvo LIKE "+"'"+session.getAttribute("Otchestvo")+"'");
/*  if(!(""+session.getAttribute("ShifrKursov")).equals("%"))
  condition.append(" AND (ShifrKursov IN(" + session.getAttribute("ShifrKursov")+") OR ShifrKursov IS NULL)");
 if(!(""+session.getAttribute("ShifrMedali")).equals("%"))
  condition.append(" AND (ShifrMedali IN(" + session.getAttribute("ShifrMedali")+") OR ShifrMedali IS NULL)");
 if(!(""+session.getAttribute("ShifrLgot")).equals("%"))
  condition.append(" AND (ShifrLgot IN(" + session.getAttribute("ShifrLgot")+") OR ShifrLgot IS NULL)");*/
 if(!(""+session.getAttribute("NomerPlatnogoDogovora")).equals("%"))
  condition.append(" AND (" + session.getAttribute("NomerPlatnogoDogovora")+session.getAttribute("PriznakDog")+")");
 if(!((""+session.getAttribute("DataRojdenija1")).equals("00-00-0000") && (""+session.getAttribute("DataRojdenija2")).equals("99-99-9999"))) {
  condition.append(" AND (DataRojdenija >= "+"'"+session.getAttribute("DataRojdenija1")+"'");
  condition.append(" AND DataRojdenija <= "+"'"+session.getAttribute("DataRojdenija2")+"'"+" OR DataRojdenija IS NULL)");
 }
 if(!(""+session.getAttribute("Pol")).equals("%"))
  condition.append(" AND (Pol LIKE "+"'"+session.getAttribute("Pol")+"'"+" OR Pol IS NULL)");
/* if(!(""+session.getAttribute("SrokObuchenija")).equals("%"))
  condition.append(" AND (SrokObuchenija LIKE "+"'"+session.getAttribute("SrokObuchenija")+"'"+" OR SrokObuchenija IS NULL)");*/
 if(!((""+session.getAttribute("GodOkonchanijaSrObrazovanija1")).equals("1950") && (""+session.getAttribute("GodOkonchanijaSrObrazovanija2")).equals("9999"))) {
  condition.append(" AND (GodOkonchanijaSrObrazovanija >= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija1")+"'");
  condition.append(" AND GodOkonchanijaSrObrazovanija <= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija2")+"'"+" OR GodOkonchanijaSrObrazovanija IS NULL)");
 }
 if(!(""+session.getAttribute("GdePoluchilSrObrazovanie")).equals("%"))
  condition.append(" AND (GdePoluchilSrObrazovanie LIKE "+"'"+session.getAttribute("GdePoluchilSrObrazovanie")+"'"+" OR GdePoluchilSrObrazovanie IS NULL)");
 if(!(""+session.getAttribute("NomerShkoly")).equals("%"))
	   condition.append(" AND NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'");
 // condition.append(" AND (NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'"+" OR NomerShkoly IS NULL)");
 if(!(""+session.getAttribute("InostrannyjJazyk")).equals("%"))
  condition.append(" AND (InostrannyjJazyk LIKE "+"'"+session.getAttribute("InostrannyjJazyk")+"'"+" OR InostrannyjJazyk IS NULL)");
 if(!(""+session.getAttribute("NujdaetsjaVObschejitii")).equals("%"))
  condition.append(" AND (NujdaetsjaVObschejitii LIKE "+"'"+session.getAttribute("NujdaetsjaVObschejitii")+"'"+" OR NujdaetsjaVObschejitii IS NULL)");
 if(!(""+session.getAttribute("Grajdanstvo")).equals("*"))
  condition.append(" AND (Grajdanstvo LIKE "+"'"+session.getAttribute("Grajdanstvo")+"'"+" OR Grajdanstvo IS NULL)");
 /*if(!(""+session.getAttribute("Nazvanie")).equals("%"))
  condition.append(" AND (Punkty.Nazvanie LIKE "+"'"+session.getAttribute("Nazvanie")+"'"+" OR Punkty.Nazvanie IS NULL)");
 /*if(!(""+session.getAttribute("NazvanieRajona")).equals("%"))
  condition.append(" AND (Rajony.NazvanieRajona LIKE "+"'"+session.getAttribute("NazvanieRajona")+"'"+" OR NazvanieRajona IS NULL)");
 if(!(""+session.getAttribute("NazvanieOblasti")).equals("%"))
  condition.append(" AND (Oblasti.NazvanieOblasti LIKE "+"'"+session.getAttribute("NazvanieOblasti")+"'"+" OR NazvanieOblasti IS NULL)");*/
 if(!(""+session.getAttribute("PolnoeNaimenovanieZavedenija")).equals("%"))
  condition.append(" AND (PolnoeNaimenovanieZavedenija LIKE "+"'"+session.getAttribute("PolnoeNaimenovanieZavedenija")+"'"+" OR PolnoeNaimenovanieZavedenija IS NULL)");
 if(!(""+session.getAttribute("TipOkonchennogoZavedenija")).equals("%"))
  condition.append(" AND (TipOkonchennogoZavedenija LIKE "+"'"+session.getAttribute("TipOkonchennogoZavedenija")+"'"+" OR TipOkonchennogoZavedenija IS NULL)");
 if(!(""+session.getAttribute("TrudovajaDejatelnost")).equals("%"))
  condition.append(" AND (TrudovajaDejatelnost LIKE "+"'"+session.getAttribute("TrudovajaDejatelnost")+"'"+" OR TrudovajaDejatelnost IS NULL)");
 if(!(""+session.getAttribute("Gruppa")).equals("%"))
  condition.append(" AND (Gruppa LIKE "+"'"+session.getAttribute("Gruppa")+"'"+" OR Gruppa IS NULL)");
if(!(""+session.getAttribute("NapravlenieOtPredprijatija")).equals("%"))
  condition.append(" AND (NapravlenieOtPredprijatija LIKE "+"'"+session.getAttribute("NapravlenieOtPredprijatija")+"'"+" OR NapravlenieOtPredprijatija IS NULL)");
 if(!(""+session.getAttribute("TipDokumenta")).equals("%"))
  condition.append(" AND (TipDokumenta LIKE "+"'"+session.getAttribute("TipDokumenta")+"'"+" OR TipDokumenta IS NULL)");
 if(!(""+session.getAttribute("NomerDokumenta")).equals("%"))
  condition.append(" AND (NomerDokumenta LIKE "+"'"+session.getAttribute("NomerDokumenta")+"'"+" OR NomerDokumenta IS NULL)");
 if(!(""+session.getAttribute("SeriaDokumenta")).equals("%"))
  condition.append(" AND (SeriaDokumenta LIKE "+"'"+session.getAttribute("SeriaDokumenta")+"'"+" OR SeriaDokumenta IS NULL)");
 if(!((""+session.getAttribute("DataVydDokumenta1")).equals("00-00-0000") && (""+session.getAttribute("DataVydDokumenta2")).equals("99-99-9999"))) {
  condition.append(" AND (DataVydDokumenta >= "+"'"+session.getAttribute("DataVydDokumenta1")+"'");
  condition.append(" AND DataVydDokumenta <= "+"'"+session.getAttribute("DataVydDokumenta2")+"'"+" OR DataVydDokumenta IS NULL)");
 }
 if(!(""+session.getAttribute("KemVydDokument")).equals("%"))
  condition.append(" AND (KemVydDokument LIKE "+"'"+session.getAttribute("KemVydDokument")+"'"+" OR KemVydDokument IS NULL)");
 if(!(""+session.getAttribute("TipDokSredObraz")).equals("%"))
  condition.append(" AND (TipDokSredObraz LIKE "+"'"+session.getAttribute("TipDokSredObraz")+"'"+" OR TipDokSredObraz IS NULL)");
  if(!(""+session.getAttribute("NomerSertifikata")).equals("%"))
  condition.append(" AND (NomerSertifikata LIKE '"+session.getAttribute("NomerSertifikata")+"'"+" OR NomerSertifikata IS NULL)");

//  condition.append(" AND (KopijaSertifikata LIKE '"+session.getAttribute("KopijaSertifikata")+"'"+" OR KopijaSertifikata IS NULL)");
 if(!(""+session.getAttribute("Ball")).equals("%"))
  condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"'"+" OR Ball IS NULL)");
 if((session.getAttribute("KodSpetsialnZach")+"").equals("%"))
   condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"' OR KodSpetsialnZach IS NULL)");
 else
   condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"')");
 if(!(""+session.getAttribute("Ball")).equals("%"))
     condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"'"+" OR Ball IS NULL)");
 
/* if(!(session.getAttribute("PreemptiveRight")+"").equals("%"))
	   condition.append(" AND PR.PreemptiveRight LIKE "+"'"+session.getAttribute("PreemptiveRight")+"'");*///"+" OR PreemptiveRight.PreemptiveRight IS NULL
 
/*     if(!(session.getAttribute("ProvidingSpecialConditions")+"").equals("%"))
	   condition.append(" AND AbitDopInf.ProvidingSpecialConditions NOT LIKE ''");*///"+" OR AbitDopInf.ProvidingSpecialConditions IS NULL
 if(!(""+session.getAttribute("Prinjat")).equals("%"))
  condition.append(" AND (Prinjat IN(" + session.getAttribute("Prinjat")+") OR Prinjat IS NULL)");
 
 //�����
 if(!(session.getAttribute("OblastP")+"").equals("%"))
	   condition.append("AND abiturient.kodOblastiP = "+"'"+session.getAttribute("OblastP")+"'");
 
 if(!(""+session.getAttribute("ShifrPriema")).equals("%"))
     condition.append(" AND ShifrPriema LIKE '%'");
 
 if(!(""+session.getAttribute("ShifrFakulteta")).equals("%"))
     condition.append(" AND ShifrFakulteta LIKE '%'");
 
 if(!(""+session.getAttribute("AbbreviaturaFakulteta")).equals("%"))
     condition.append(" AND AbbreviaturaFakulteta LIKE '%'");
 
/*  if(!(session.getAttribute("RajonP")+"").equals("%"))
	   condition.append("AND abiturient.kodRajonaP = "+"'"+session.getAttribute("RajonP")+"'");
 if(!(session.getAttribute("PunktP")+"").equals("%"))
	   condition.append("AND abiturient.Gorod_Prop = "+"'"+session.getAttribute("PunktP")+"'");*/
//************************************************
  query.append(condition);
  query.append(" ORDER BY "+ session.getAttribute("stSort")+" "+session.getAttribute("prSort"));
/*************************** ������� ����� ������������ �� �� ****************************/
  //totalCount = 0;
//System.out.println(">>SRCH_8");
/// For Debugging Purposes Only
///
/// abit_A.setSpecial1(query.toString());
System.out.println(query);

       stmt = conn.prepareStatement(query.toString());
       rs = stmt.executeQuery();
       while(rs.next()) {

       if(abit_Srch.getDokumentyHranjatsja()!=null){

 //��������� ���������� �� �������
          MasSelector[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][0] = rs.getString(2);
 //������������� ������������ ����������� �������
	  MasSelector[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][1] = "\\qc";
 //����������� ���������� ��������� ��������
	  masInc++;
	 }

	 if(abit_Srch.getSpecial1()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial1())][0] = rs.getString(3);
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial1())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerLichnogoDela()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][0] = rs.getString(4);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getFamilija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getFamilija())][0] = rs.getString(5);
	  MasSelector[Integer.parseInt(abit_Srch.getFamilija())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getImja()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getImja())][0] = rs.getString(6);
	  MasSelector[Integer.parseInt(abit_Srch.getImja())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getOtchestvo()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getOtchestvo())][0] = rs.getString(7);
	  MasSelector[Integer.parseInt(abit_Srch.getOtchestvo())][1] = "\\ql";
	  masInc++;
	 }

	/* if(abit_Srch.getShifrKursov()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getShifrKursov())][0] = rs.getString(8);
	  MasSelector[Integer.parseInt(abit_Srch.getShifrKursov())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getMedal()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getMedal())][0] = rs.getString(9);
	  MasSelector[Integer.parseInt(abit_Srch.getMedal())][1] = "\\qc";
	  masInc++;
	 }
*/
	 if(abit_Srch.getLgoty()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getLgoty())][0] = rs.getString(39);
	  MasSelector[Integer.parseInt(abit_Srch.getLgoty())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerPlatnogoDogovora()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][0] = rs.getString(8);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSpecial2()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial2())][0] = rs.getString(9);
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial2())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getPol()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getPol())][0] = rs.getString(10);
	  MasSelector[Integer.parseInt(abit_Srch.getPol())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSrokObuchenija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSrokObuchenija())][0] = rs.getString(11);
	  MasSelector[Integer.parseInt(abit_Srch.getSrokObuchenija())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSpecial5()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial5())][0] = Integer.toString(rs.getInt(12));
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial5())][1] = "\\qc";
	  masInc++;
	 }
	
	 if(abit_Srch.getGdePoluchilSrObrazovanie()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][0] = rs.getString(16);
	  MasSelector[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][1] = "\\qc";
	  masInc++;
	 }
	  
	 if(abit_Srch.getNomerShkoly()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerShkoly())][0] = rs.getString(14);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerShkoly())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getInostrannyjJazyk()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getInostrannyjJazyk())][0] = rs.getString(15);
	  MasSelector[Integer.parseInt(abit_Srch.getInostrannyjJazyk())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNujdaetsjaVObschejitii()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNujdaetsjaVObschejitii())][0] = rs.getString(16);
	  MasSelector[Integer.parseInt(abit_Srch.getNujdaetsjaVObschejitii())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getGrajdanstvo()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getGrajdanstvo())][0] = rs.getString(17);
	  MasSelector[Integer.parseInt(abit_Srch.getGrajdanstvo())][1] = "\\qc";
	  masInc++;
	 }
	
	 if(abit_Srch.getNazvanie()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanie())][0] = rs.getString(45);
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanie())][1] = "\\ql";
	  masInc++;
	 }
 /*
	 if(abit_Srch.getNazvanieRajona()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieRajona())][0] = rs.getString(22);
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieRajona())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getNazvanieOblasti()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieOblasti())][0] = rs.getString(23);
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieOblasti())][1] = "\\ql";
	  masInc++;
	 }
	*/
	 if(abit_Srch.getPolnoeNaimenovanieZavedenija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][0] = rs.getString(18);
	  MasSelector[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getTipOkonchennogoZavedenija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][0] = rs.getString(19);
	  MasSelector[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getTrudovajaDejatelnost()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTrudovajaDejatelnost())][0] = rs.getString(20);
	  MasSelector[Integer.parseInt(abit_Srch.getTrudovajaDejatelnost())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getGruppa()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getGruppa())][0] = rs.getString(21);
	  MasSelector[Integer.parseInt(abit_Srch.getGruppa())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNapravlenieOtPredprijatija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNapravlenieOtPredprijatija())][0] = rs.getString(22);
	  MasSelector[Integer.parseInt(abit_Srch.getNapravlenieOtPredprijatija())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getTipDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokumenta())][0] = rs.getString(23);
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerDokumenta())][0] = rs.getString(24);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSeriaDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaDokumenta())][0] = rs.getString(25);
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getDataVydDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getDataVydDokumenta())][0] = rs.getString(26);
	  MasSelector[Integer.parseInt(abit_Srch.getDataVydDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getKemVydDokument()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getKemVydDokument())][0] = rs.getString(27);
	  MasSelector[Integer.parseInt(abit_Srch.getKemVydDokument())][1] = "\\qc";
	  masInc++;
	 }
/*
	 if(abit_Srch.getTel()!=null)
	 {
		 if (rs.getString(38) != null){
	  MasSelector[Integer.parseInt(abit_Srch.getTel())][0] = rs.getString(38);
	  MasSelector[Integer.parseInt(abit_Srch.getTel())][1] = "\\qc";
	  masInc++;
		 }
	 }
*/
	 if(abit_Srch.getTipDokSredObraz()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokSredObraz())][0] = rs.getString(28);
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokSredObraz())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSobesedovanie()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSobesedovanie())][0] = rs.getString(29);
	  MasSelector[Integer.parseInt(abit_Srch.getSobesedovanie())][1] = "\\qc";
	  masInc++;
	 }
/*
	 if(abit_Srch.getKodTselevogoPriema()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTselevojPriem())][0] = rs.getString(47);
	  MasSelector[Integer.parseInt(abit_Srch.getTselevojPriem())][1] = "\\qc";
	  masInc++;
	 }
*/
// ����� �����������

	 if(abit_Srch.getAttestat()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getAttestat())][0] = rs.getString(37);
	  MasSelector[Integer.parseInt(abit_Srch.getAttestat())][1] = "\\qc";
	  masInc++;
	 }
	 /*
	 if(abit_Srch.getKopijaSertifikata()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getKopijaSertifikata())][0] = rs.getString(38);
	  MasSelector[Integer.parseInt(abit_Srch.getKopijaSertifikata())][1] = "\\qc";
	  masInc++;
		 }else{
			 MasSelector[Integer.parseInt(abit_Srch.getKopijaSertifikata())][0] = rs.getString(37);
			  MasSelector[Integer.parseInt(abit_Srch.getKopijaSertifikata())][1] = "\\qc";
			  masInc++; 
		 }
	 }

	 if(abit_Srch.getSpecial7()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial7())][0] = rs.getString(39);
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial7())][1] = "\\qc";
	  masInc++;
}else{
	MasSelector[Integer.parseInt(abit_Srch.getSpecial7())][0] = rs.getString(39);
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial7())][1] = "\\qc";
	  masInc++;	 
		 }
	 }
*/
	 if(abit_Srch.getPrinjat()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getPrinjat())][0] = rs.getString(33);
	  MasSelector[Integer.parseInt(abit_Srch.getPrinjat())][1] = "\\qc";
	  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getPrinjat())][0] = rs.getString(33);
	  MasSelector[Integer.parseInt(abit_Srch.getPrinjat())][1] = "\\qc";
	  masInc++;	 
		 }
	 }

	 if(abit_Srch.getFormaOb()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getFormaOb())][0] = rs.getString(45);
	  MasSelector[Integer.parseInt(abit_Srch.getFormaOb())][1] = "\\qc";
	  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getFormaOb())][0] = rs.getString(46);
	  MasSelector[Integer.parseInt(abit_Srch.getFormaOb())][1] = "\\qc";
	  masInc++; 
		 }
	 }

	 if(abit_Srch.getOsnovaOb()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getOsnovaOb())][0] = rs.getString(46);
	  MasSelector[Integer.parseInt(abit_Srch.getOsnovaOb())][1] = "\\qc";
	  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getOsnovaOb())][0] = rs.getString(47);
	  MasSelector[Integer.parseInt(abit_Srch.getOsnovaOb())][1] = "\\qc";
	  masInc++;	 
		 }
	 }

	 if(abit_Srch.getSeriaAtt()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaAtt())][0] = rs.getString(37);
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaAtt())][1] = "\\qc";
	  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getSeriaAtt())][0] = rs.getString(37);
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaAtt())][1] = "\\qc";
	  masInc++;	 
		 }
	 }

	 if(abit_Srch.getNomerAtt()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getNomerAtt())][0] = rs.getString(36);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerAtt())][1] = "\\qc";
	  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getNomerAtt())][0] = rs.getString(36);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerAtt())][1] = "\\qc";
	  masInc++; 
		 }
	 }
	 
	 if(abit_Srch.getShifrFakulteta()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getShifrFakulteta())][0] = rs.getString(40);
	  MasSelector[Integer.parseInt(abit_Srch.getShifrFakulteta())][1] = "\\qc";
	  masInc++;
}else{
	MasSelector[Integer.parseInt(abit_Srch.getShifrFakulteta())][0] = rs.getString(40);
	  MasSelector[Integer.parseInt(abit_Srch.getShifrFakulteta())][1] = "\\qc";
	  masInc++;
			}
	 }
	 ////////////////////////////////////////////////////////////////////////////////////
	 if(abit_Srch.getTel()!=null)
	 {
		 if((""+session.getAttribute("UseAllSpecs")).equals("yes")){
	  MasSelector[Integer.parseInt(abit_Srch.getTel())][0] = rs.getString(42);
	  MasSelector[Integer.parseInt(abit_Srch.getTel())][1] = "\\qc";
	  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getTel())][0] = rs.getString(43);
	  MasSelector[Integer.parseInt(abit_Srch.getTel())][1] = "\\qc";
	  masInc++;
			 
		 }
	 }
	 /* if(abit_Srch.getPreemptiveRight()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
	  MasSelector[Integer.parseInt(abit_Srch.getPreemptiveRight())][0] = rs.getString(49);
	  MasSelector[Integer.parseInt(abit_Srch.getPreemptiveRight())][1] = "\\qc";
	  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getPreemptiveRight())][0] = rs.getString(48);
	  MasSelector[Integer.parseInt(abit_Srch.getPreemptiveRight())][1] = "\\qc";
	  masInc++;
			 
		 }
	 }*/
	 /*	 if(abit_Srch.getReturnDocument()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
		 MasSelector[Integer.parseInt(abit_Srch.getReturnDocument())][0] = rs.getString(52);
		  MasSelector[Integer.parseInt(abit_Srch.getReturnDocument())][1] = "\\qc";
		  masInc++;
}else{
	MasSelector[Integer.parseInt(abit_Srch.getReturnDocument())][0] = rs.getString(51);
	  MasSelector[Integer.parseInt(abit_Srch.getReturnDocument())][1] = "\\qc";
	  masInc++;
			 
		 }
	 }*/
	 	 if(abit_Srch.getDopAddress()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){ 
			 MasSelector[Integer.parseInt(abit_Srch.getDopAddress())][0] = rs.getString(43);
		  MasSelector[Integer.parseInt(abit_Srch.getDopAddress())][1] = "\\qc";
		  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getDopAddress())][0] = rs.getString(44);
	  MasSelector[Integer.parseInt(abit_Srch.getDopAddress())][1] = "\\qc";
	  masInc++;	 
		 }
	 }
	 /*	 if(abit_Srch.getProvidingSpecialCondition()!=null)
	 {
		 if(abit_Srch.getAttestat()!=null){
		 MasSelector[Integer.parseInt(abit_Srch.getProvidingSpecialCondition())][0] = rs.getString(51);
		  MasSelector[Integer.parseInt(abit_Srch.getProvidingSpecialCondition())][1] = "\\qc";
		  masInc++;
}else{
	 MasSelector[Integer.parseInt(abit_Srch.getProvidingSpecialCondition())][0] = rs.getString(50);
	  MasSelector[Integer.parseInt(abit_Srch.getProvidingSpecialCondition())][1] = "\\qc";
	  masInc++;
			 
		 }
	 }
*/
// ����-�� ����������
	 if(abit_Srch.getSpecial8()!=null)
	 {
// ��������� ������������ ����-�� �� �� ����
           AbiturientBean abit_TMP = new AbiturientBean();
           stmt_a = conn.prepareStatement("SELECT DISTINCT s.Abbreviatura FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND s.KodSpetsialnosti LIKE ?");
           stmt_a.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           stmt_a.setObject(2,rs.getString(34),Types.INTEGER);
           rs_a = stmt_a.executeQuery();
           if(rs_a.next())    abit_TMP.setSpecial8(rs_a.getString(1));
           else               abit_TMP.setSpecial8("");

	  MasSelector[Integer.parseInt(abit_Srch.getSpecial8())][0] = abit_TMP.getSpecial8();
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial8())][1] = "\\qc";
	  masInc++;
	 }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	 xxx = 1;
	 tabulator = 0;
///	 report.write("\\fs20 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
///	 tabulator += 700;

// ����� ������ � ������� ������

///	 report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
	 report.write("\\b0 \\qc \\intbl "+number+"\\cell\n");
	 number++;
         while(masInc>0)  // ���� ������ ������ �������
         {
///	   tabulator += Integer.parseInt(MassForCol[xxx][1]);
///	   report.write("\\clvertalc \\clbrdrt\\brdrs \\brdrw20\\brdrcf1 \\clbrdrb\\brdrs \\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
           if(MasSelector[xxx][0] != null) report.write("\\b0 "+MasSelector[xxx][1]+" \\intbl "+MasSelector[xxx][0]+"\\cell\n");
           else report.write("\\b0 \\intbl \\cell\n");
           xxx++;
           masInc--;
         }

         report.write("\\intbl \\row\n");

 }

//########################################################################################

/*	 report.write("\\rtf1\\ansi\n");
	 if(abit_Srch.getPriznakSortirovki().equals("���������"))
	 {
	  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
	 }
*/
	 report.write("\\pard\\par\\par\n");
	 if(abit_Srch.getSpecial6() != null)	//����� ��������
		 report.write("\\fs28 \\ql "+abit_Srch.getSpecial6()+"\n");
	 report.write("}"); 
	 
	 report.close();
	 form.setAction(us.getClientIntName("new_rep","crt"));
       return mapping.findForward("rep_brw");
	 
	}//��������� "makeRTF"
	
        }// ��������� try
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
        request.setAttribute("abit_Srch", abit_Srch);
        request.setAttribute("abits_Srch", abits_Srch);
   }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}
