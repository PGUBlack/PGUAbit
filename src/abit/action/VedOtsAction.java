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
import java.util.Date;
import java.io.*;
import abit.sql.*; 

public class VedOtsAction extends Action {

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
        VedOtsForm           form               = (VedOtsForm) actionForm;
        AbiturientBean       abit_O             = form.getBean(request, errors);
        boolean              ved_ots_f          = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_O            = new ArrayList();
        ArrayList            abit_O_S1          = new ArrayList();
        ArrayList            abit_O_S2          = new ArrayList();
        ArrayList            abit_O_S3          = new ArrayList();
        ArrayList            abit_O_S4          = new ArrayList();
        ArrayList            abit_O_S5          = new ArrayList();
        ArrayList            abit_O_S6          = new ArrayList();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "vedOtsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "vedOtsForm", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

  if ( form.getAction() == null ) {

/********************* ���������� ������ ��� ����� � ������� ���������� *************************/

            stmt = conn.prepareStatement("SELECT KodPredmeta,Sokr FROM NazvanijaPredmetov ORDER BY 1 ASC");
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              Integer _KodPredmeta = new Integer(rs.getInt(1));
              String _AbbreviaturaFakulteta = rs.getString(2);
              abit_TMP.setKodPredmeta(_KodPredmeta);
              abit_TMP.setAbbreviaturaFakulteta(_AbbreviaturaFakulteta);
              abit_O_S1.add(abit_TMP);
            }
          
            stmt = conn.prepareStatement("SELECT DISTINCT Raspisanie.KodPredmeta,DataJekzamena,NazvanijaPredmetov.KodPredmeta FROM NazvanijaPredmetov,Raspisanie WHERE NazvanijaPredmetov.KodPredmeta = Raspisanie.KodPredmeta AND Raspisanie.KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP2 = new AbiturientBean();
              abit_TMP2.setSpecial1(Integer.toString(rs.getInt(1)));
              abit_TMP2.setDataJekzamena(StringUtil.data_toApp(rs.getString(2)));
              abit_O_S2.add(abit_TMP2);
            }

            String Pred = new String();
            String Dat = new String();
            String Gru = new String();
            String _Special7 = new String();
            stmt = conn.prepareStatement("SELECT DISTINCT Gruppy.KodGruppy,Gruppy.Gruppa,Raspisanie.KodPredmeta,Raspisanie.DataJekzamena FROM Gruppy,Raspisanie WHERE Gruppy.KodGruppy = Raspisanie.KodGruppy AND Raspisanie.KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                AbiturientBean abit_TMP = new AbiturientBean();
                Pred = rs.getString(3);
                Dat = StringUtil.data_toApp(rs.getString(4));
                Gru = Integer.toString(rs.getInt(1));
                _Special7 = Dat +"%"+ Pred +"+"+ Gru;
                abit_TMP.setSpecial7(_Special7);
                abit_TMP.setGruppa(rs.getString(2));
                abit_O_S3.add(abit_TMP);
            }

            form.setAction(us.getClientIntName("view","init"));

}else if ( form.getAction().equals("report")) {

/************************************************/
/***** ����������� ����� � �������� ������� *****/
/************************************************/

String name = new String();

stmt = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov WHERE KodPredmeta LIKE ?");
stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) name = "��������� � �������� ��. "+abit_O.getSpecial7().toUpperCase()+" �� "+rs.getString(1);
else name = "��������� � �������� ��. "+abit_O.getSpecial7().toUpperCase();


String file_con = new String("ved_ots_"+StringUtil.toEng(abit_O.getSpecial7())+"_"+abit_O.getKodPredmeta());

session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

report.write("{\\rtf1\\ansi\n");
report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd\\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza,NazvanieRodit FROM NazvanieVuza WHERE KodVuza LIKE ?");
stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) 
report.write("\\fs36 \\b\\qc "+rs.getString(1)+"\n");
//***********************************************************************

report.write("\\par\\par");
report.write("\\fs24 \\qc �������, ��������, ������� �������� (�����������)");
report.write("\\par\\par");
report.write("\\fs34 \\qc ��������� �");
report.write("\\par\\par");
stmt = conn.prepareStatement("SELECT Datelnyj FROM NazvanijaPredmetov where KodPredmeta like ?");
stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next())
{
report.write("\\fs25 \\ql �������������� �������� �� \\ul   "+rs.getString(1).toUpperCase()+"   \\ulnone");
}

String Spec = new String();
stmt = conn.prepareStatement("SELECT KodGruppy FROM Gruppy WHERE Gruppa LIKE ?");
stmt.setObject(1,abit_O.getSpecial7(),Types.VARCHAR);
rs = stmt.executeQuery();
if(rs.next()) Spec = Integer.toString(rs.getInt(1));

String Potok = new String();
stmt = conn.prepareStatement("SELECT Potok FROM Gruppy WHERE KodGruppy like ?");
stmt.setObject(1,new Integer(Spec),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) Potok = Integer.toString(rs.getInt(1));

report.write("\\par\\par");  
report.write("\\fs25 \\ql �� ������ \\ul          "+abit_O.getSpecial7()+"          \\ulnone ������ \\ul_____"+Potok+"_____\\ulnone");
report.write("\\par\\par");
report.write("\\fs25 \\ql ���� �������� \\ul  "+abit_O.getSpecial1()+"   \\ulnone ");
report.write("\\par\\par");
report.write("\\fs25 \\ql ������ �������� _________________ ����� �������� ______________");
report.write("\\par\\par");
report.write("\\fs25 \\ql ������� � �������� ������������� ___________________________");
report.write("\\par\\par");
report.write("\\fs25 \\ql  _____________________________________________________________");


//����� �������
report.write("\\par");
report.write("\\par");
report.write("\\pard\\phpg\\posxc");
report.write("\\fs24 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx750");
report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1250");
report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3200");
report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5000");
report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7000");
report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10100");
report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx11300");

report.write("\\intbl ���� \\cell");
report.write("\\intbl � \\cell");
report.write("\\intbl ������� \\cell");
report.write("\\intbl ��� \\cell");
report.write("\\intbl �������� \\cell");
report.write("\\intbl ����� ���. ����� \\cell");
report.write("\\intbl ������ \\cell");
report.write("\\intbl ������� �����. \\cell");
report.write("\\intbl \\row");

report.write("\\fs22 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr");
report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx750");
report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx1250");
report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx3200");
report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx5000");
report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx7000");
report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\cellx9000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10100");
report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx11300");

report.write("\\intbl \\cell");
report.write("\\intbl \\cell");
report.write("\\intbl \\cell");
report.write("\\intbl \\cell");
report.write("\\intbl \\cell");
report.write("\\intbl \\cell");
report.write("\\intbl ����� \\cell");
report.write("\\intbl ������� \\cell");
report.write("\\intbl \\cell");
report.write("\\intbl \\row");

report.write("\\fs24\\b0 \\ql \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx750");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1250");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx11300");

int number = 0;

stmt = conn.prepareStatement("SELECT DISTINCT a.Familija, a.Imja,a.Otchestvo,a.NomerLichnogoDela,o.Otsenka FROM Abiturient a, Gruppy g,Otsenki o WHERE a.KodGruppy=g.KodGruppy AND a.KodAbiturienta=o.KodAbiturienta AND g.KodGruppy LIKE ? AND o.KodPredmeta LIKE ?");
stmt.setObject(1,new Integer(Spec),Types.INTEGER);
stmt.setObject(2,abit_O.getKodPredmeta(),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next())
{
number ++;
report.write("\\intbl\\cell");
report.write("\\intbl\\qc "+number+" \\cell");
report.write("\\intbl\\ql "+rs.getString(1)+" \\cell");
report.write("\\intbl\\ql "+rs.getString(2)+" \\cell");
report.write("\\intbl\\ql "+rs.getString(3)+" \\cell");
report.write("\\intbl\\qc "+rs.getString(4)+" \\cell");
report.write("\\intbl\\qc "+rs.getString(5)+" \\cell");

if(rs.getString(5).equals("10"))
        report.write("\\intbl ������ \\cell");
if(rs.getString(5).equals("9"))
        report.write("\\intbl  ������ \\cell");
if(rs.getString(5).equals("8"))
        report.write("\\intbl  ������ \\cell");
if(rs.getString(5).equals("7"))
        report.write("\\intbl  ���� \\cell");
if(rs.getString(5).equals("6"))
        report.write("\\intbl  ����� \\cell");
if(rs.getString(5).equals("5"))
        report.write("\\intbl  ���� \\cell");
if(rs.getString(5).equals("4"))
        report.write("\\intbl  ������ \\cell");
if(rs.getString(5).equals("3"))
        report.write("\\intbl  ��� \\cell");
if(rs.getString(5).equals("2"))
        report.write("\\intbl  ��� \\cell");
if(rs.getString(5).equals("1"))
        report.write("\\intbl  ���� \\cell");
if(rs.getString(5).equals("0"))
        report.write("\\intbl  ���� \\cell");
else  
        report.write("\\intbl\\cell");

report.write("\\intbl\\cell\\row");
//report.write("\\intbl");
}

report.write("\\pard\\par\\par");
report.write("\\fs24 \\b\\ql ����� ���������������� ������������ _____________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql �� ���: ���������� 10(������) ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  9(������) ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  8(������) ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  7(����)   ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  6(�����)  ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  5(����)   ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  4(������) ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  3(���)    ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  2(���)    ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  1(����)   ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql         ����������  0(����)   ______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql ����� ������������, �� ��������� �� ������� ___________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql ������������ ���������� ��������        _______________________");
report.write("\\par\\par");
report.write("\\fs24 \\ql ������������� ��������� �������� �������� _____________________");

//***********************************************************************
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
        request.setAttribute("abit_O_S4", abit_O_S4);
        request.setAttribute("abit_O_S5", abit_O_S5);
        request.setAttribute("abit_O_S6", abit_O_S6);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(ved_ots_f) return mapping.findForward("ved_ots_f");
        return mapping.findForward("success");
    }
}