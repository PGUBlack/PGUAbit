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

public class ExListAction extends Action {

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
        BlankForm            form               = (BlankForm) actionForm;
        AbiturientBean       abit_O             = form.getBean(request, errors);
        boolean              error              = false;
        String               Predmet            = null;
        String               Data               = null;
        String               kSpec              = null;
        ArrayList            abits_O            = new ArrayList();
        ArrayList            abit_O_S1          = new ArrayList();
        ArrayList            abit_O_S2          = new ArrayList();
        String               kPredmeta          = new String();
        String               oldKodeS           = new String();
        String               oldKodeF           = new String();
        String               oldNazv            = new String();
        boolean              priznak            = true;
        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {
        request.setAttribute( "exListAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "blankForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

  if ( form.getAction() == null ) {

/************************************************************************************************/
/********************* Подготовка данных для ввода с помощью селекторов *************************/

     stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
     stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
     rs = stmt.executeQuery();
     while (rs.next()) {
       AbiturientBean abit_TMP = new AbiturientBean();
       abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
       abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
       abit_O_S1.add(abit_TMP);
     }

     priznak = true;
     kPredmeta = new String();
     String oldKode = new String();
     String oldKodFak = new String();
     String oldAbbr = new String();

     stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,EkzamenyNaSpetsialnosti.KodPredmeta,Abbreviatura,AbbreviaturaFakulteta,Spetsialnosti.KodFakulteta FROM Spetsialnosti,EkzamenyNaSpetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND KodVuza LIKE ? ORDER BY Abbreviatura,EkzamenyNaSpetsialnosti.KodPredmeta,AbbreviaturaFakulteta ASC");
     stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
     rs = stmt.executeQuery();
     while (rs.next()) {
       AbiturientBean abit_TMP = new AbiturientBean();
       abit_TMP.setSpecial1(rs.getString(1));
       if(priznak) { oldKode = rs.getString(1); oldKodFak = rs.getString(5); oldAbbr = rs.getString(3); priznak = false; }
       if(!oldKode.equals(rs.getString(1))) {
         if(kPredmeta.equals("")) kPredmeta += "%" + rs.getString(2);
           abit_TMP.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
           abit_TMP.setAbbreviatura(oldAbbr);
           abit_O_S2.add(abit_TMP);
           priznak = true;
           kPredmeta = "";
       }

       kPredmeta += "%" + rs.getString(2);
     }

     AbiturientBean abit_TMP2 = new AbiturientBean();
     abit_TMP2.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
     abit_TMP2.setAbbreviatura(oldAbbr);
     abit_O_S2.add(abit_TMP2);

     form.setAction(us.getClientIntName("view","init"));

  } else if ( form.getAction().equals("report")) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

      String name = new String();
      String file_con = new String();

      if(abit_O.getSpecial1().equals("-1")) {

        stmt = conn.prepareStatement("SELECT UPPER(f.AbbreviaturaFakulteta) FROM Fakultety f WHERE f.KodFakulteta LIKE ? AND f.KodVuza LIKE ?");
        stmt.setObject(1,abit_O.getKodFakulteta(),Types.VARCHAR);
        stmt.setObject(2, session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          name = "Экз. листы по факультету "+rs.getString(1);
          file_con = "ex_list_"+StringUtil.toEng(rs.getString(1));
        }
      } else {

        kSpec = abit_O.getSpecial1().substring(abit_O.getSpecial1().indexOf("$")+1,abit_O.getSpecial1().indexOf("%"));

        stmt = conn.prepareStatement("SELECT UPPER(f.AbbreviaturaFakulteta),s.Abbreviatura FROM Spetsialnosti s,Fakultety f WHERE f.KodFakulteta=s.KodFakulteta AND f.KodFakulteta LIKE ? AND s.KodSpetsialnosti LIKE ? AND f.KodVuza LIKE ?");
        stmt.setObject(1,abit_O.getKodFakulteta(),Types.VARCHAR);
        stmt.setObject(2,kSpec,Types.VARCHAR);
        stmt.setObject(3, session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        if(rs.next()) {
          name = "Экз. листы по факультету "+rs.getString(1)+" спец. "+rs.getString(2);
          file_con = "ex_list_"+StringUtil.toEng(rs.getString(1))+"_"+StringUtil.toEng(rs.getString(2));
        }
      }

      session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

      String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

      BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

/************************************************/

      report.write("{\\rtf1\\ansi\n");
      report.write("\\paperw11906\\paperh16838\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");

      if(abit_O.getSpecial1().equals("-1")) {
        stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela FROM Abiturient a,Spetsialnosti s,Fakultety f,ZajavlennyeShkolnyeOtsenki zso WHERE zso.KodAbiturienta=a.KodAbiturienta AND f.KodFakulteta=s.KodFakulteta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE 'д' AND zso.Examen IN('+') AND f.KodFakulteta LIKE ? GROUP BY a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela ORDER BY a.Familija,a.Imja,a.Otchestvo");
        stmt.setObject(1,abit_O.getKodFakulteta(),Types.VARCHAR);
      }
      else {

        kSpec = abit_O.getSpecial1().substring(abit_O.getSpecial1().indexOf("$")+1,abit_O.getSpecial1().indexOf("%"));

        stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela FROM Abiturient a,Spetsialnosti s,Fakultety f,ZajavlennyeShkolnyeOtsenki zso WHERE zso.KodAbiturienta=a.KodAbiturienta AND f.KodFakulteta=s.KodFakulteta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE 'д' AND zso.Examen IN('+') AND f.KodFakulteta LIKE ? AND s.KodSpetsialnosti LIKE ? GROUP BY a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,a.NomerLichnogoDela ORDER BY a.Familija,a.Imja,a.Otchestvo");
        stmt.setObject(1,abit_O.getKodFakulteta(),Types.VARCHAR);
        stmt.setObject(2,kSpec,Types.VARCHAR);
      }
      rs2 = stmt.executeQuery();
      while(rs2.next()) {


         stmt = conn.prepareStatement("SELECT NazvanieVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
         stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) report.write("\\fs20\\b\\caps\\qc{"+rs.getString(1)+"}\\b0\\caps0\n");

         report.write("\\par\\par\n");
         report.write("\\qc\\fs28\\b{ЭКЗАМЕНАЦИОННЫЙ ЛИСТ № "+rs2.getString(5)+"}\\par\\b0\n");

/************************************************/

         String tip_Dok = new String();
         stmt = conn.prepareStatement("SELECT a.Familija,a.Imja,a.Otchestvo,UPPER(f.AbbreviaturaFakulteta),s.ShifrSpetsialnosti,s.NazvanieSpetsialnosti,g.Gruppa FROM Abiturient a,Fakultety f,Spetsialnosti s,Gruppy g WHERE g.KodGruppy=a.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND a.KodAbiturienta LIKE ?");
         stmt.setObject(1,rs2.getString(1),Types.INTEGER);
         rs = stmt.executeQuery();
         if(rs.next()) {

           report.write("\\fs22\\par\\ql{Факультет/институт:  }\\i{"+rs.getString(4)+"}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{Направление подготовки/Специальность:  }\\i{"+rs.getString(5)+"}\\'ab{"+rs.getString(6)+"}\\'bb\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           if(rs.getString(7) != null && rs.getString(7).trim().equals("-"))
             report.write("{Группа:  }\\i{"+"}\\i0\n");
           else
             report.write("{Группа:  }\\i{"+rs.getString(7)+"}\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("{Ф.И.О. поступающего:  }\\i\\b{"+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+"}\\b0\\i0\n");
           report.write("\\fs6\\par\\par\\fs22");
           report.write("\\qc\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab\\tab{Дата получения: }\\i{\\'ab"+"___\\'bb ____________ "+StringUtil.CurrYear()+"г.}\\i0\n");
         /*  report.write("\\par\\par\\par\\ql\\fs22{         ________________________________}\n");
           report.write("\\par\\ql\\fs16\\tab\\tab{(подпись поступающего)}\n");*/
           report.write("\\par\\par\n");
         }

/************************************************/

         report.write("\\qc\\fs28\\par\\b{ОЦЕНКИ, ПОЛУЧЕННЫЕ НА ВСТУПИТЕЛЬНЫХ ЭКЗАМЕНАХ}\\b0\n");

         report.write("\\par\\par\n");
         report.write("\\fs22 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
         report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx750\n");
         report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3500\n");
         report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5000\n");
         report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw10 \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6500\n");
         report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8900\n");
         report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10400\n");

         report.write("\\intbl{№}\\cell");
         report.write("\\intbl{Наименование предмета}\\cell");
         report.write("\\intbl{Дата}\\cell");
         report.write("\\intbl{Оценка}\\cell");
         report.write("\\intbl{Фамилия экзаменатора}\\cell");
         report.write("\\intbl{Подпись экзаменатора}\\cell");
         report.write("\\intbl\\row\n");

      /*   report.write("\\fs20 \\qc \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
         report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx750\n");
         report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx2800\n");
         report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx3800\n");
        // report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx4800\n");
         report.write("\\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx7400\n");
         report.write("\\clvmrg \\clvertalc \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx8900\n");
         report.write("\\clvmrg \\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrdb \\clbrdrr\\brdrs\\brdrw10 \\cellx10400\n");*/

      /*   report.write("\\intbl\\cell");
         report.write("\\intbl\\cell");
         report.write("\\intbl\\cell");
         report.write("\\intbl{цифрой}\\cell");
     //    report.write("\\intbl{прописью}\\cell");
         report.write("\\intbl\\cell");
         report.write("\\intbl\\cell");
         report.write("\\intbl \\row\n");*/

         report.write("\\b0\\fs22\\ql\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx750\n");
         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx3500\n");
         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx5000\n");
      //   report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx4800\n");
         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx6500\n");
         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx8900\n");
         report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw10 \\clbrdrl\\brdrs\\brdrw10 \\clbrdrb\\brdrs\\brdrw10 \\clbrdrr\\brdrs\\brdrw10 \\cellx10400\n");

         int counter = 0;
         stmt = conn.prepareStatement("SELECT np.Predmet,np.KodPredmeta, otsenkaege FROM ZajavlennyeShkolnyeOtsenki zso,NazvanijaPredmetov np WHERE zso.KodPredmeta=np.KodPredmeta AND zso.Examen IN ('+') AND zso.KodAbiturienta LIKE ? ORDER BY np.KodPredmeta");
         stmt.setObject(1,rs2.getString(1),Types.INTEGER);
         rs = stmt.executeQuery();
         while(rs.next()) {
           report.write("\\intbl\\qc{"+(counter+1)+"}\\cell");
           report.write("\\intbl\\ql{"+rs.getString(1)+"}\\cell");
           report.write("\\intbl\\cell");
           report.write("\\intbl\\ql{"+rs.getString(3)+"}\\cell");
          // report.write("\\intbl\\cell");
           report.write("\\intbl\\cell");
           report.write("\\intbl\\cell");
           report.write("\\intbl\\row");
           counter++;
         }

         for(;counter<=3;counter++) {
            report.write("\\intbl\\cell");
            report.write("\\intbl\\cell");
            report.write("\\intbl\\cell");
            report.write("\\intbl\\cell");
         //   report.write("\\intbl\\cell");
            report.write("\\intbl\\cell");
            report.write("\\intbl\\cell");
            report.write("\\intbl\\row");
         }

/************************************************/

         report.write("\\pard");
         report.write("\\par\\par\\ql\\fs24\\tab\\b{Общее количество баллов}\\b0{ (на конкретное направление }\\par\n");
         report.write("\\par\\par\\b0{ подготовки/специальность): ____________________ }  \\par\n");
         report.write("\\par\\par\\par\\ql\\fs22\\b0\\tab{Ответственный секретарь приемной комиссии: ________________/_____________/}\n");
//      report.write("\\par\\ql\\fs22\\tab{          приемной комиссии: ______________________________________}\n");
         report.write("\\par\\par\\par\\par\\par\\par\\par\\par\\par\\par\\par\\par\\par\\par\n");

/************************************************/

         report.write("\\par\\qj\\fs24\\b{    Примечания:}\\b0\n");
         report.write("\\par\\qj\\fs22\\tab\\i{1. Экзаменационный лист служит пропуском на экзамен при предъявлении паспорта.}\\i0\n");
         report.write("\\par\\qj\\fs22\\tab\\i{2. Экзаменационный лист выдается абитуриенту на консультации перед экзаменом.}\\i0\n");
         report.write("\\par\\qj\\fs22\\tab\\i{3. По окончании каждого вступительного экзамена экзаменационный лист }\\b\\ul{в обязательном порядке}\\b0\\ulnone{ возвращается в приемную комиссию.}\\i0\n");
         report.write("\\par\\qj\\fs22\\tab\\i{4. При получении неудовлетворительной оценки абитуриент }\\b\\ul{не допускается}\\b0\\ulnone{ к следующему экзамену и экзаменационный лист ему }\\b\\ul{не выдается.}\\b0\\ulnone\\i0\n");
         report.write("\\par\\qj\\fs22\\tab\\i{5. Опоздавшие на экзамен допускаются на него с разрешения ответственного секретаря приемной комиссии.}\\i0\n");

         report.write("\\pard\\par\\page");

     }// abits

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
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}