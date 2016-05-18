package abit.action;

import java.io.*;
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
import abit.action.AllZachOtsForm;
import java.lang.Object.*;
import abit.sql.*;

public class AllZachOtsAction extends Action {

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
        AllZachOtsForm       form               = (AllZachOtsForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              rep_all_zach_ots_f = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        String               note               = new String();
        ArrayList            notes              = new ArrayList();
        int                  count_predm        = 0;
        int                  number             = 0;
	  UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "allZachOtsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "allZachOtsForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
        
/********************************************************************/
/********  Отчет по всем зачисленным абитуриентам с оценками ********/
/********************************************************************/
if(StringUtil.toInt(abit_SD.getSpecial1(),0)!=-1) {

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Список зачисл. абит-в с оценками и суммой баллов";

    String file_con = new String("list_all_ab_ots");

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

    report.write("{\\rtf1\\ansi\n");
    report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
    stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) 
    report.write("\\fs36 \\qc "+rs.getString(1)+"\n");

    report.write("\\par\\par\n");
    report.write("\\fs28 \\b0 \\qc Абитуриенты зачисленные на очную форму обучения на бюджетной основе\\line\\qc \\b \\n");

// максимальное кол-во экзаменов при поступления в ВУЗ
    stmt = conn.prepareStatement("SELECT COUNT(ens.KodPredmeta) FROM EkzamenyNaSpetsialnosti ens, Spetsialnosti s,Fakultety f WHERE ens.KodSpetsialnosti=s.KodSpetsialnosti AND f.KodFakulteta=s.KodFakulteta AND f.KodVuza LIKE ? GROUP BY ens.KodSpetsialnosti ORDER BY 1 DESC");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) {
      count_predm = rs.getInt(1);
    }

    report.write("\\par\n");
    report.write("\\fs22 \\trowd \\trhdr \\trqc\\trgaph108\\trrh280\\trleft30\n");
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1000\n");  // номер 
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n"); //аббр
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3200\n"); // нлд
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n"); //ф
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n"); //и
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8600\n"); //о
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9500\n"); // шм
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(9500+count_predm*1200)+"\n"); // предметы / оценки / баллы ЕГЭ
    report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(9500+count_predm*1200+1000)+"\n");

    report.write("\\intbl № \\cell\n");
    report.write("\\intbl Спец-ть зачисл. \\cell\n");
    report.write("\\intbl Номер\n личн. дела \\cell\n");
    report.write("\\intbl Фамилия \\cell\n");
    report.write("\\intbl Имя \\cell\n");
    report.write("\\intbl Отчество \\cell\n");
    report.write("\\intbl Шифр отл. \\cell\n");
    report.write("\\intbl Предметы / \\n Оценки / Баллы ЕГЭ\\cell\n");
    report.write("\\intbl Сумма оценок \\cell\n");
    report.write("\\intbl \\row\n");

    report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft30\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1000\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3200\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8600\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9500\n");
   for(int col=1;col<=count_predm;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(9500+col*1200)+"\n");
    report.write("\\clvmrg\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrdb \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(9500+count_predm*1200+1000)+"\n");

    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\cell\n");
   for(int col=1;col<=count_predm;col++)
     report.write("\\intbl\\fs22{Экз. "+col+"}\\cell\n");
    report.write("\\intbl\\cell\n");
    report.write("\\intbl\\row\\b0\\fs20");

// Формат очередной строки
    report.write("\\trowd \\trqc\\trgaph108\\trrh280\\trleft30\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx1000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx2200\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx3200\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx5000\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx6600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8600\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9500\n");
   for(int col=1;col<=count_predm*2;col++)
     report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(9500+col*600)+"\n");
    report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(9500+count_predm*1200+1000)+"\n");

// Выборка данных по абитуриенту для вывода их в отчет
   int summa  =  0;
   int old_Ka = -1;

   AbiturientBean abit_TMP = new AbiturientBean();
   stmt = conn.prepareStatement("SELECT a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,zo.OtsenkaEge,np.KodPredmeta FROM Abit_In_Prik ap,Abiturient a,Otsenki o,ZajavlennyeShkolnyeOtsenki zo,Spetsialnosti s,Medali m,Gruppy g,NazvanijaPredmetov np,Forma_Obuch fo WHERE ap.KodAbiturienta=a.KodAbiturienta AND fo.KodFormyOb=a.KodFormyOb AND np.KodPredmeta=o.KodPredmeta AND np.KodPredmeta=zo.KodPredmeta AND zo.KodAbiturienta=a.KodAbiturienta AND zo.KodPredmeta=o.KodPredmeta AND a.KodAbiturienta=o.KodAbiturienta AND a.KodMedali=m.KodMedali AND a.KodSpetsialnZach=s.KodSpetsialnosti AND a.KodGruppy=g.KodGruppy AND o.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE a.KodSpetsialnZach) AND a.KodVuza LIKE ? AND NomerPlatnogoDogovora IS NULL AND fo.Sokr IN ('очная') GROUP BY a.KodAbiturienta,s.Abbreviatura,g.Gruppa,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,m.ShifrMedali,o.Otsenka,zo.OtsenkaEge,np.KodPredmeta,g.KodGruppy ORDER BY Familija,Imja,Otchestvo,a.KodAbiturienta,np.KodPredmeta ASC");
   stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER); 
   rs = stmt.executeQuery();
   while (rs.next()) {

     if(rs.getInt(1) != old_Ka) {

       if(old_Ka != -1) {
         abit_TMP.setNotes(notes);
         abit_TMP.setSumma(""+summa);
         summa = 0;
         report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
         report.write("\\intbl\\qc{"+abit_TMP.getAbbreviatura()+"}\\cell\n");
         report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
         report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
         report.write("\\intbl{"+abit_TMP.getImja()+"}\\cell\n");
         report.write("\\intbl{"+abit_TMP.getOtchestvo()+"}\\cell\n");
         report.write("\\intbl\\qc{"+abit_TMP.getShifrMedali()+"}\\cell\n");
         for(int ots=0;ots<notes.size();ots++) {
          report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
         }
         for(int ots=0;ots<(count_predm*2-notes.size());ots++) {
// Дополняем пустыми ячейками строки тех, кто сдавал меньше экзаменов, чем столбцов в таблице
          report.write("\\intbl\\qc{}\\cell\n");
         }
         report.write("\\intbl\\qc{"+abit_TMP.getSumma()+"}\\cell\n");
         report.write("\\intbl\\row\n");
       }

       old_Ka = rs.getInt(1);

       notes = new ArrayList();

       abit_TMP = new AbiturientBean();
       abit_TMP.setKodAbiturienta(new Integer(old_Ka));
       abit_TMP.setNumber(Integer.toString(++number));
       abit_TMP.setAbbreviatura(rs.getString(2));
       abit_TMP.setNomerLichnogoDela(rs.getString(4));
       abit_TMP.setFamilija(rs.getString(5));
       abit_TMP.setImja(rs.getString(6));
       abit_TMP.setOtchestvo(rs.getString(7));
       abit_TMP.setShifrMedali(rs.getString(8));

// 2 оценки на один Экз.

       for(int col=9;col<=10;col++) {
         note = rs.getString(col);

         if(col == 9) summa += StringUtil.toInt(note,0);

         if(!note.equals("-"))
           notes.add(note);
         else 
           notes.add(Constants.emptyNote);
       }

     } else { 
         for(int col=9;col<=10;col++) {
           note = rs.getString(col);

           if(col == 9) summa += StringUtil.toInt(note,0);

           if(!note.equals("-")) {
             notes.add(note);
           } 
           else 
             notes.add(Constants.emptyNote);
         }
       }
   }

// Добавление последнего абитуриента в список

   report.write("\\intbl\\qc{"+(number)+"}\\cell\n");
   report.write("\\intbl\\qc{"+abit_TMP.getAbbreviatura()+"}\\cell\n");
   report.write("\\intbl\\qc{"+abit_TMP.getNomerLichnogoDela()+"}\\cell\n");
   report.write("\\intbl\\ql{"+abit_TMP.getFamilija()+"}\\cell\n");
   report.write("\\intbl{"+abit_TMP.getImja()+"}\\cell\n");
   report.write("\\intbl{"+abit_TMP.getOtchestvo()+"}\\cell\n");
   report.write("\\intbl\\qc{"+abit_TMP.getShifrMedali()+"}\\cell\n");
   for(int ots=0;ots<notes.size();ots++) {
    report.write("\\intbl\\qc{"+StringUtil.voidFilter(""+notes.get(ots))+"}\\cell\n");
   }
   for(int ots=0;ots<(count_predm*2-notes.size());ots++) {
// Дополняем пустыми ячейками строки тех, кто сдавал меньше экзаменов, чем столбцов в таблице
    report.write("\\intbl\\qc{}\\cell\n");
   }
   report.write("\\intbl\\qc{"+summa+"}\\cell\n");
   report.write("\\intbl\\row\n");

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
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(rep_all_zach_ots_f) return mapping.findForward("rep_all_zach_ots_f");
        return mapping.findForward("success");
    }
}