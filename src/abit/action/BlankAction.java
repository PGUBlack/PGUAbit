package abit.action;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Enumeration;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import java.io.*;
import abit.sql.*; 

public class BlankAction extends Action {

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
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        BlankForm            form               = (BlankForm) actionForm;
        AbiturientBean       abit_O             = form.getBean(request, errors);
        boolean              blank_f            = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        String               Predmet            = null;
        String               Data               = null;
        ArrayList            abits_O            = new ArrayList();
        ArrayList            abit_O_S1          = new ArrayList();
        ArrayList            abit_O_S2          = new ArrayList();
        ArrayList            abit_O_S3          = new ArrayList();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
        request.setAttribute( "blankAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "blankForm", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

  if ( form.getAction() == null ) {

/************************************************************************************************/
/********************* ���������� ������ ��� ����� � ������� ���������� *************************/

        stmt = conn.prepareStatement("SELECT KodPredmeta,Sokr FROM NazvanijaPredmetov ORDER BY 1 ASC");
        rs = stmt.executeQuery();
        while (rs.next()) {
		AbiturientBean abit_TMP = new AbiturientBean();
		abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
		abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
		abit_O_S1.add(abit_TMP);
        }
//**********************************************************************************************************//            
	  stmt = conn.prepareStatement("SELECT DISTINCT Raspisanie.KodPredmeta,DataJekzamena FROM NazvanijaPredmetov,Raspisanie WHERE NazvanijaPredmetov.KodPredmeta = Raspisanie.KodPredmeta AND Raspisanie.KodVuza LIKE ? ORDER BY 2 ASC");
        stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        while (rs.next()) {
            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial1(Integer.toString(rs.getInt(1)));
            abit_TMP2.setDataJekzamena(StringUtil.data_toApp(rs.getString(2)));
            abit_O_S2.add(abit_TMP2);
        }

//**************************************************
	 stmt = conn.prepareStatement("SELECT DISTINCT Gruppy.KodGruppy,Gruppy.Gruppa,Raspisanie.KodPredmeta,Raspisanie.DataJekzamena FROM Gruppy,Raspisanie WHERE Gruppy.KodGruppy = Raspisanie.KodGruppy AND Raspisanie.KodVuza LIKE ? ORDER BY 2 ASC");
       stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
       rs = stmt.executeQuery();
       while (rs.next()) {
           AbiturientBean abit_TMP = new AbiturientBean();
           abit_TMP.setSpecial7(StringUtil.data_toApp(rs.getString(4))+ "%" + rs.getString(3) + "+" + Integer.toString(rs.getInt(1)));
           abit_TMP.setGruppa(rs.getString(2));
           abit_O_S3.add(abit_TMP);
       }

  form.setAction(us.getClientIntName("view","init"));

}else if ( form.getAction().equals("report")) {

/************************************************/
/***** ����������� ����� � �������� ������� *****/
/************************************************/
  String name;
  stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND KodPredmeta LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  stmt.setObject(2,abit_O.getKodPredmeta(),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) 
    name = "������ ��������� "+abit_O.getSpecial7()+" �� "+rs.getString(1)+" �� "+abit_O.getSpecial1();
  else 
    name = "������ ���������";

  String file_con = new String("blank_"+StringUtil.toEng(abit_O.getSpecial7())+"_"+abit_O.getKodPredmeta()+"_"+abit_O.getSpecial1());

  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

  report.write("{\\rtf1\\ansi\n");
  report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

  stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next()) report.write("\\fs40\\b\\qc "+rs.getString(1)+"\\fs20\n");

//***********************************************************************
  report.write("\\par\\par");
  report.write("\\fs20 \\qc �����, ����-�������, �������� ����� �������� (�����������)");
  report.write("\\par\\par");
  report.write("\\fs35 \\qc ��������� � _________");
  report.write("\\par\\par");

  stmt = conn.prepareStatement("SELECT Datelnyj,Sokr FROM NazvanijaPredmetov where KodPredmeta like ?");
  stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next())
  {
    report.write("\\fs24 \\ql �������������� �������� �� \\ul   "+rs.getString(1).toUpperCase()+"   \\ulnone");
    Predmet = rs.getString(2);
  }

  String KodGruppy = new String();//����������� ��� ������
  stmt = conn.prepareStatement("select KodGruppy from Gruppy where Gruppa like ?");
  stmt.setObject(1,abit_O.getSpecial7(),Types.VARCHAR);
  rs = stmt.executeQuery();
  if(rs.next())
  {
    KodGruppy = Integer.toString(rs.getInt(1));
  }

  stmt = conn.prepareStatement("select Potok from Gruppy where KodGruppy like ?");
  stmt.setObject(1,new Integer(KodGruppy),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next())
  {
    report.write("\\par\\par");  
    report.write("\\fs24\\ql{������ }\\ul          "+abit_O.getSpecial7()+"          \\ulnone{, ����� }\\ul_____"+Integer.toString(rs.getInt(1))+"_____\\ulnone");
    report.write("\\par\\par");
    report.write("\\fs24\\ql{���� �������� }\\ul  "+abit_O.getSpecial1()+"   \\ulnone ");
    report.write("\\par\\par");
    report.write("\\fs24\\ql{������ �������� _________________ ����� �������� ______________}");
    report.write("\\par\\par");
    report.write("\\fs24\\ql{������� � �������� ������������� ______________________________________________}");
    report.write("\\par\\par");
    report.write("\\fs24\\ql{ ________________________________________________________________________________}");
  }

//����� �������
  report.write("\\par");
  report.write("\\par\n");
  report.write("\\fs22 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx750\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1250\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2850\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4150\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5850\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7150\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9250\n");
  report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10550\n");

  report.write("\\intbl ���� \\cell");
  report.write("\\intbl � \\cell");
  report.write("\\intbl ������� \\cell");
  report.write("\\intbl ��� \\cell");
  report.write("\\intbl �������� \\cell");
  report.write("\\intbl ����� ��������-�������� ����� \\cell");
  report.write("\\intbl ������ \\cell");
  report.write("\\intbl ������� ��������-���� \\cell");
  report.write("\\intbl \\row\n");

  report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
  report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx750\n");
  report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx1250\n");
  report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2850\n");
  report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx4150\n");
  report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx5850\n");
  report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx7150\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\cellx8100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9250\n");
  report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10550\n");

  report.write("\\intbl \\cell");
  report.write("\\intbl \\cell");
  report.write("\\intbl \\cell");
  report.write("\\intbl \\cell");
  report.write("\\intbl \\cell");
  report.write("\\intbl \\cell");
  report.write("\\fs20\\intbl ����� \\cell");
  report.write("\\intbl ������� \\cell");
  report.write("\\fs24\\intbl \\cell");
  report.write("\\intbl \\row\n");

  report.write("\\b0 \\ql \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx750\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2850\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4150\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5850\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7150\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8100\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9250\n");
  report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10550\n");

int number = 0;// ������� ��������� ������������

stmt = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,NomerLichnogoDela FROM Abiturient WHERE (PodtvMed NOT LIKE '�' OR PodtvMed IS NULL) AND KodGruppy LIKE ? AND KodAbiturienta NOT IN (SELECT o.KodAbiturienta FROM Abiturient a, Otsenki o WHERE a.KodAbiturienta=o.KodAbiturienta AND o.Otsenka IN ("+StringUtil.PlaceUnaryComas("",abit_O.getSpecial2())+") AND a.KodGruppy LIKE ?) ORDER BY Familija,Imja,Otchestvo,NomerLichnogoDela");
stmt.setObject(1,new Integer(KodGruppy),Types.INTEGER);
stmt.setObject(2,new Integer(KodGruppy),Types.INTEGER);
	rs = stmt.executeQuery();
	while(rs.next())
{
  number ++;
  report.write("\\intbl\\qc\\cell");
  report.write("\\intbl\\qc{"+number+"}\\cell");
  report.write("\\intbl\\ql{"+rs.getString(1)+"}\\cell");
  report.write("\\intbl\\ql{"+rs.getString(2)+"}\\cell");
  report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell");
  report.write("\\intbl\\qc{"+rs.getString(4)+"}\\cell");
  report.write("\\intbl\\cell");
  report.write("\\intbl\\cell");
  report.write("\\intbl\\cell");
  report.write("\\intbl\\row");
}

  report.write("\\pard");
  report.write("\\par\\par\\par");
  report.write("\\fs22\\b\\ql{���������� ���������������� ������������ _____________}");
  report.write("\\par\\par");
 /* report.write("\\fs22\\ql{�� ��� ���������� ������������� ������ _______________}");
  report.write("\\par\\par");*/
  report.write("\\fs22\\ql{���������� �� ��������� �� ������� ������������ _____________}");
  report.write("\\par\\par");

  stmt = conn.prepareStatement("SELECT Fio FROM OtvetstvennyeLitsa WHERE KodVuza LIKE ? AND Doljnost LIKE '�������%'");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next())
    report.write("\\fs22\\ql{������������ ���������� �������� _____________________________ / "+rs.getString(1)+"} /");
  else
    report.write("\\fs22\\ql{������������ ���������� �������� _____________________________}");

  report.write("\\par\\par");
  stmt = conn.prepareStatement("SELECT Fio FROM OtvetstvennyeLitsa WHERE KodVuza LIKE ? AND Doljnost LIKE '���%���%'");
  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = stmt.executeQuery();
  if(rs.next())
    report.write("\\fs22\\ql{������������� ��������� �������� �������� _________________ / "+rs.getString(1)+"} /");
  else
    report.write("\\fs22\\ql{������������� ��������� �������� �������� _________________}");

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
        request.setAttribute("abit_O", abit_O);
        request.setAttribute("abits_O", abits_O);
        request.setAttribute("abit_O_S1", abit_O_S1);
        request.setAttribute("abit_O_S2", abit_O_S2);
        request.setAttribute("abit_O_S3", abit_O_S3);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(blank_f) return mapping.findForward("blank_f");
        return mapping.findForward("success");
    }
}