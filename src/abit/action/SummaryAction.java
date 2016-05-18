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

public class SummaryAction extends Action {

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
        ResultSet            rs_a               = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        SummaryForm          form               = (SummaryForm) actionForm;
        AbiturientBean       abit_O             = form.getBean(request, errors);
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_O            = new ArrayList();
        ArrayList            notes              = new ArrayList();
        ArrayList            abit_O_S1          = new ArrayList();
        ArrayList            abit_O_S2          = new ArrayList();
        int                  Summa_10_0         = 0;
        int                  Summa_9_1          = 0;
        int                  itog[]             = new int[14];
        int                  count              = 0;
        String               predmet            = new String();
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "summaryAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "summaryForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************* Подготовка данных для ввода с помощью селекторов *************************/
      stmt = conn.prepareStatement("SELECT KodPredmeta,Sokr FROM NazvanijaPredmetov ORDER BY KodPredmeta ASC");
      rs = stmt.executeQuery();
	while (rs.next()) 
	{
		AbiturientBean abit_TMP = new AbiturientBean();
		abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
		abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
		abit_O_S1.add(abit_TMP);
       	}
        
        stmt = conn.prepareStatement("SELECT DISTINCT Raspisanie.KodPredmeta,DataJekzamena,NazvanijaPredmetov.KodPredmeta FROM NazvanijaPredmetov,Raspisanie WHERE NazvanijaPredmetov.KodPredmeta = Raspisanie.KodPredmeta AND Raspisanie.KodVuza LIKE ? ORDER BY DataJekzamena ASC");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        while (rs.next()) {
          AbiturientBean abit_TMP2 = new AbiturientBean();
          abit_TMP2.setSpecial1(Integer.toString(rs.getInt(1)));
          abit_TMP2.setDataJekzamena(StringUtil.data_toApp(rs.getString(2)));
          abit_O_S2.add(abit_TMP2);
        }

  if ( form.getAction() == null ) {

     form.setAction(us.getClientIntName("view","init"));

  } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

stmt = conn.prepareStatement("SELECT Predmet,Sokr FROM NazvanijaPredmetov WHERE KodPredmeta LIKE ?");
stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) predmet = rs.getString(2);

String name = "Сводка по экзамену ("+predmet+") на "+ abit_O.getSpecial1();

String file_con = new String("summary_"+abit_O.getKodPredmeta()+"_"+abit_O.getSpecial1());

session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

report.write("{\\rtf1\\ansi\n");
report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza,NazvanieRodit FROM NazvanieVuza WHERE KodVuza LIKE ?");
stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) 
report.write("\\fs40 \\b\\qc "+rs.getString(1)+"\n");
report.write("\\par\\par");
report.write("\\fs32 \\b\\qc Результаты сдачи экзамена\n");
report.write("\\par\\par");

stmt = conn.prepareStatement("SELECT Predmet,Sokr FROM NazvanijaPredmetov WHERE KodPredmeta LIKE ?");
stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) {
  report.write("\\fs28 \\b \\qc Предмет: \\ul  "+rs.getString(1).toUpperCase()+"   \\ulnone");
  predmet = rs.getString(2);
}
report.write("\\par\\par");
report.write("\\fs28 \\b \\qc Дата:        \\ul  "+abit_O.getSpecial1()+"   \\ulnone ");

//Шапка таблицы
report.write("\\par\\par");
report.write("\\fs28 \\qc \\trowd \\trqc\\trgaph108\\trrh560\\trleft36");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx1900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2700");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx3500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx4300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx5100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx5900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx6700");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx7500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8400");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx9900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx11000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx12200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdb \\clbrdrr\\brdrs\\brdrw10 \\cellx13400");

report.write("\\intbl Факультет\\cell");
for(int otsn=10;otsn>=0;otsn--) report.write("\\intbl{"+otsn+"}\\cell");
report.write("\\intbl 10 и 0\\cell");
report.write("\\intbl с 9 по 1\\cell");
report.write("\\intbl \\row");

report.write("\\fs28\\b0 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx1900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx2700");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4300");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6700");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx7500");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8400");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9100");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx9900");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx11000");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx12200");
report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx13400");

stmt = conn.prepareStatement("SELECT f.KodFakulteta,f.AbbreviaturaFakulteta FROM Gruppy g, Fakultety f, Raspisanie r WHERE f.KodFakulteta=g.KodFakulteta AND r.KodGruppy=g.KodGruppy AND r.KodPredmeta LIKE ? AND r.DataJekzamena LIKE ? AND r.KodVuza LIKE ? GROUP BY f.KodFakulteta,f.AbbreviaturaFakulteta ORDER BY f.KodFakulteta ASC");
stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
stmt.setObject(2,StringUtil.data_toDB(abit_O.getSpecial1()),Types.VARCHAR);
stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
rs_a = stmt.executeQuery();
while(rs_a.next()) {

report.write("\\intbl "+rs_a.getString(2)+" \\cell");

/******** Подсчет кол-ва оценок *********/

notes = new ArrayList();
Summa_10_0 = Summa_9_1 = 0;

for(int otsn=12;otsn>=2;otsn--) {
   stmt = conn.prepareStatement("SELECT COUNT(f.KodFakulteta) FROM Otsenki o,Abiturient a,Spetsialnosti s,Fakultety f,Raspisanie r WHERE o.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND f.KodFakulteta=s.KodFakulteta AND r.KodGruppy=a.KodGruppy AND o.KodPredmeta LIKE ? AND r.DataJekzamena LIKE ? AND o.Otsenka LIKE ? AND r.KodVuza LIKE ? AND f.KodFakulteta LIKE ?");
   stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
   stmt.setObject(2,StringUtil.data_toDB(abit_O.getSpecial1()),Types.VARCHAR);
   stmt.setObject(3,""+(otsn-2),Types.INTEGER);
   stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
   stmt.setObject(5,rs_a.getString(1),Types.INTEGER);
   rs = stmt.executeQuery();
   if(rs.next()) report.write("\\intbl{"+rs.getString(1)+"}\\cell");
   else report.write("\\intbl\\cell");

   if(otsn == 12 || otsn == 2) Summa_10_0 += rs.getInt(1);
   else Summa_9_1 += rs.getInt(1);

   itog[otsn]+=rs.getInt(1);
}

// -- 10 + 0 --
report.write("\\intbl "+Summa_10_0+" \\cell");

// -- 9 + 1 --
report.write("\\intbl "+Summa_9_1+" \\cell");

itog[1] += Summa_10_0;
itog[0] += Summa_9_1;

report.write("\\intbl\\row");
}

report.write("\\intbl\\b1{итого}\\cell");
for(int col=12;col>=0;col--) report.write("\\intbl{"+itog[col]+"}\\cell");
report.write("\\intbl\\b0\\row");

report.write("}");
report.close();

form.setAction(us.getClientIntName("new_rep","crt"));
return mapping.findForward("rep_brw");

}//закрывает report

else if ( form.getAction().equals("viewing")) {

  form.setAction(us.getClientIntName("full","view"));

  if(abit_O.getSpecial1()!=null)

    abit_O.setKodPredmeta(abit_O.getKodPredmeta());
    abit_O.setSpecial1(abit_O.getSpecial1());


stmt = conn.prepareStatement("SELECT Predmet FROM NazvanijaPredmetov where KodPredmeta like ?");
stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
rs = stmt.executeQuery();
if(rs.next()) abit_O.setNazvanie(rs.getString(1));

abit_O.setData(abit_O.getSpecial1());

AbiturientBean currFakult = new AbiturientBean();

stmt = conn.prepareStatement("SELECT f.KodFakulteta,f.AbbreviaturaFakulteta FROM Gruppy g, Fakultety f, Raspisanie r WHERE f.KodFakulteta=g.KodFakulteta AND r.KodGruppy=g.KodGruppy AND r.KodPredmeta LIKE ? AND r.DataJekzamena LIKE ? AND r.KodVuza LIKE ? GROUP BY f.KodFakulteta,f.AbbreviaturaFakulteta ORDER BY f.KodFakulteta ASC");
stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
stmt.setObject(2,StringUtil.data_toDB(abit_O.getSpecial1()),Types.VARCHAR);
stmt.setObject(3,session.getAttribute("kVuza"),Types.INTEGER);
rs_a = stmt.executeQuery();
while(rs_a.next()) {

currFakult = new AbiturientBean();

currFakult.setAbbreviaturaFakulteta(rs_a.getString(2));

notes = new ArrayList();
Summa_10_0 = Summa_9_1 = 0;

/******** Подсчет кол-ва оценок *********/

for(int otsn=12;otsn>=2;otsn--) {
   stmt = conn.prepareStatement("SELECT COUNT(f.KodFakulteta) FROM Otsenki o,Abiturient a,Spetsialnosti s,Fakultety f,Raspisanie r WHERE o.KodAbiturienta=a.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND f.KodFakulteta=s.KodFakulteta AND r.KodGruppy=a.KodGruppy AND o.KodPredmeta LIKE ? AND r.DataJekzamena LIKE ? AND o.Otsenka LIKE ? AND r.KodVuza LIKE ? AND f.KodFakulteta LIKE ?");
   stmt.setObject(1,abit_O.getKodPredmeta(),Types.INTEGER);
   stmt.setObject(2,StringUtil.data_toDB(abit_O.getSpecial1()),Types.VARCHAR);
   stmt.setObject(3,""+(otsn-2),Types.INTEGER);
   stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
   stmt.setObject(5,rs_a.getString(1),Types.INTEGER);
   rs = stmt.executeQuery();
   if(rs.next()) notes.add(rs.getString(1));

   if(otsn == 12 || otsn == 2) Summa_10_0 += rs.getInt(1);
   else Summa_9_1 += rs.getInt(1);

   itog[otsn] += rs.getInt(1);
}
notes.add(""+Summa_10_0);
notes.add(""+Summa_9_1);

itog[1] += Summa_10_0;
itog[0] += Summa_9_1;
currFakult.setNotes(notes);

abits_O.add(currFakult);
 }

notes = new ArrayList();

currFakult = new AbiturientBean();

currFakult.setAbbreviaturaFakulteta("итого");

for(int col=12;col>=0;col--) notes.add(""+itog[col]);

currFakult.setNotes(notes);

abits_O.add(currFakult);

}//закрывает viewing

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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");

        return mapping.findForward("success");
    }
}