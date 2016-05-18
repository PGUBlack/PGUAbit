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
import abit.sql.*; 

public class RaspisanieAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session      = request.getSession();
        Connection        conn         = null;
        PreparedStatement stmt         = null;
        ResultSet         rs           = null;
        ActionErrors      errors       = new ActionErrors();
        ActionError       msg          = null;
        RaspisanieForm    form         = (RaspisanieForm) actionForm;
        AbiturientBean    abit_Rasp    = form.getBean(request, errors);
        MessageBean       message      = new MessageBean();
        boolean           raspisanie_f = false;
        boolean           error        = false;
        ActionForward     f            = null;
        int               kRaspisanija = 1;
        ArrayList         abits_Rasp   = new ArrayList();
        ArrayList         abits_Rasp2  = new ArrayList();
        ArrayList         abit_Rasp_S1 = new ArrayList();
        ArrayList         abit_Rasp_S2 = new ArrayList();
        ArrayList         abit_Rasp_S3 = new ArrayList();
        ArrayList         abit_Rasp_S4 = new ArrayList();
        ArrayList         abit_Rasp_S5 = new ArrayList();
        ArrayList         abit_Rasp_S6 = new ArrayList();
        UserBean          user         = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "raspisanieAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "raspisanieForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************** Подготовка данных для ввода с помощью селекторов ************************/

// ************************* факультеты *******************************

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_Rasp_S1.add(abit_TMP);
            }

// ******************** группы на факультете ***************************

            boolean save = false;
            String oldKodFak = "";
            String kodeline = "";

            stmt = conn.prepareStatement("SELECT DISTINCT g.KodFakulteta,g.KodGruppy,g.Gruppa,g.Potok FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND KodGruppy NOT LIKE 1 AND KodVuza LIKE ? ORDER BY g.KodFakulteta,g.KodGruppy ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
             if(!rs.getString(1).equals(oldKodFak)) {
               if(save) {
                 AbiturientBean tmp = new AbiturientBean();
                 tmp.setSpecial1(oldKodFak);
                 tmp.setSpecial2(kodeline);
// KodGruppy хранит Код Факультета, к которому относятся группы
// Special2 имеет формат списка: KodFakulteta%Gruppa1%KodGruppy1$Potok1%Gruppa2%KodGruppy2$Potok2%Gruppa3%KodGruppy3$Potok3...
                 abit_Rasp_S5.add(tmp);
               }
               oldKodFak = rs.getString(1);
               kodeline = rs.getString(3)+"%"+rs.getString(2)+"$"+rs.getString(4);
               save = true;
             } else kodeline += "%"+rs.getString(3)+"%"+rs.getString(2)+"$"+rs.getString(4);
            }
// Запись последней строки в массив кодов и групп
            AbiturientBean tmp = new AbiturientBean();
            tmp.setSpecial1(oldKodFak);
            tmp.setSpecial2(kodeline);
            abit_Rasp_S5.add(tmp);

// *********************************************************************

                 stmt = conn.prepareStatement("SELECT DISTINCT KodPredmeta,Sokr,Predmet FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY 1 ASC");
                 stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setKodPredmeta(new Integer(rs.getInt(1)));
                   abit_TMP.setSokr(rs.getString(2));
                   abit_TMP.setPredmet(rs.getString(3));
                   abit_Rasp_S2.add(abit_TMP);
                 }

                 stmt = conn.prepareStatement("SELECT DISTINCT ShifrFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 1 ASC");
                 stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 if (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setShifrFakulteta(rs.getString(1));
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
                   abit_Rasp_S3.add(abit_TMP);
                 }
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setShifrFakulteta(rs.getString(1));
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
                   abit_Rasp_S3.add(abit_TMP);
                 }

                 stmt = conn.prepareStatement("SELECT DISTINCT Potok FROM Gruppy ORDER BY 1 ASC");
                 rs = stmt.executeQuery();
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setNomerPotoka(new Integer(rs.getInt(1)));
                   abit_Rasp_S4.add(abit_TMP);
                 }

                 stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety ORDER BY 2 ASC");
                 rs = stmt.executeQuery();
                 if (rs.next()) {
                   if(session.getAttribute("letter") == null) session.setAttribute("letter",rs.getString(1));
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
                   abit_Rasp_S6.add(abit_TMP);
                 } while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
                   abit_Rasp_S6.add(abit_TMP);
                 }

/************************************************************************************************/

            if ( form.getAction() == null ) {

                 form.setAction(us.getClientIntName("new","init"));

            } else if ( form.getAction().equals("create") || form.getAction().equals("full") ) {

/************************************************************************************************/
/**  Если action равен create или full, то входим в секцию - создание записи или вывод таблицы **/
/************************************** Создание записи *****************************************/

                if ( request.getParameter("full") == null &&
                             form.getAction().equals("create") ) {

                    stmt = conn.prepareStatement("SELECT KodRaspisanija FROM Raspisanie WHERE KodGruppy LIKE ? AND KodPredmeta LIKE ?");
                    stmt.setObject(1,abit_Rasp.getSpecial1().substring(0,abit_Rasp.getSpecial1().indexOf('$')),Types.INTEGER);
                    stmt.setObject(2, abit_Rasp.getKodPredmeta(),Types.INTEGER);
                    rs = stmt.executeQuery();
                    if(rs.next()) {
                      message.setMessage("Ошибка! Расписание для заданных группы и предмета уже существует.");
//                      abit_Rasp.setKodFakulteta(abit_Rasp.getKodFakulteta());
//                      abit_Rasp.setAbbreviaturaFakulteta(abit_Rasp.getAbbreviaturaFakulteta());
//                      abit_Rasp.setSpecial1(abit_Rasp.getSpecial1());
//                      abit_Rasp.setSpecial2(abit_Rasp.getSpecial2());
                      abit_Rasp.setKodPredmeta(abit_Rasp.getKodPredmeta());
                      abit_Rasp.setSokr(abit_Rasp.getSokr());
                      abit_Rasp.setNomerPotoka(abit_Rasp.getNomerPotoka());
                      abit_Rasp.setDataKonsultatsii(abit_Rasp.getDataKonsultatsii());
                      abit_Rasp.setAuditorijaKonsultatsii(abit_Rasp.getAuditorijaKonsultatsii());
                      abit_Rasp.setDataJekzamena(abit_Rasp.getDataJekzamena());
                      abit_Rasp.setAuditorijaJekzamena(abit_Rasp.getAuditorijaJekzamena());
                    } else {
                       stmt = conn.prepareStatement("SELECT MAX(KodRaspisanija) FROM Raspisanie");
                       rs = stmt.executeQuery();
                       if(rs.next()) kRaspisanija = rs.getInt(1)+1;
                         else kRaspisanija = 2;

                       stmt = conn.prepareStatement("INSERT Raspisanie(KodRaspisanija,KodGruppy,DataKonsultatsii,AuditorijaKonsultatsii,DataJekzamena,AuditorijaJekzamena,KodPredmeta,KodVuza) VALUES(?,?,?,?,?,?,?,?)");
                       stmt.setObject(1, new Integer(""+kRaspisanija),Types.INTEGER);
                       stmt.setObject(2, abit_Rasp.getSpecial1().substring(0,abit_Rasp.getSpecial1().indexOf('$')),Types.INTEGER);
                       stmt.setObject(3, StringUtil.data_toDB(abit_Rasp.getDataKonsultatsii()),Types.VARCHAR);
                       stmt.setObject(4, abit_Rasp.getAuditorijaKonsultatsii(),Types.VARCHAR);
                       stmt.setObject(5, StringUtil.data_toDB(abit_Rasp.getDataJekzamena()),Types.VARCHAR);
                       stmt.setObject(6, abit_Rasp.getAuditorijaJekzamena(),Types.VARCHAR);
                       stmt.setObject(7, abit_Rasp.getKodPredmeta(),Types.INTEGER);
                       stmt.setObject(8, session.getAttribute("kVuza"),Types.INTEGER);
                       stmt.executeUpdate();

                 }
                 form.setAction(us.getClientIntName("new","act"));

/************************************************************************************************/
/********* Если параметр full в запросе присутствует, то выводим всю табличку целиком  **********/

               } else {
                   form.setAction(us.getClientIntName("full","view"));
                   String query;
                   if( request.getParameter("letter") != null )
                     session.setAttribute("letter",request.getParameter("letter"));

                   if( request.getParameter("priznakSortirovki") !=null )
                     if(Integer.parseInt(request.getParameter("priznakSortirovki")) == -15)
                       session.setAttribute("priznakSortirovki","'%'");
                     else
                       session.setAttribute("priznakSortirovki",request.getParameter("priznakSortirovki"));

                   if( request.getParameter("stolbetsSortirovki") !=null )
                     if(Integer.parseInt(request.getParameter("stolbetsSortirovki")) == -1)
                       session.setAttribute("stolbetsSortirovki","'%'");
                     else
                       session.setAttribute("stolbetsSortirovki",request.getParameter("stolbetsSortirovki"));
                   query = new String("SELECT Gruppa,DataKonsultatsii,AuditorijaKonsultatsii,DataJekzamena,AuditorijaJekzamena,Predmet,Gruppy.Potok,KodRaspisanija,NazvanijaPredmetov.KodVuza FROM Raspisanie,Gruppy,NazvanijaPredmetov WHERE Raspisanie.KodGruppy = Gruppy.KodGruppy AND Raspisanie.KodPredmeta = NazvanijaPredmetov.KodPredmeta AND NazvanijaPredmetov.KodVuza LIKE ");
                   query += session.getAttribute("kVuza");
                   query += " AND Gruppy.KodFakulteta LIKE "+session.getAttribute("letter")+" AND NazvanijaPredmetov.KodPredmeta LIKE ";
                   query += session.getAttribute("priznakSortirovki");
                   query += " AND Gruppy.Potok LIKE ";
                   query += session.getAttribute("stolbetsSortirovki");
                   query += " ORDER BY 1,6 ASC";
                   stmt = conn.prepareStatement(query);
                   rs = stmt.executeQuery();
                   while (rs.next()) {
                     AbiturientBean abit_TMP = new AbiturientBean();
                     abit_TMP.setGruppa(rs.getString(1));
                     abit_TMP.setDataKonsultatsii(StringUtil.data_toApp(rs.getString(2)));
                     abit_TMP.setAuditorijaKonsultatsii(rs.getString(3));
                     abit_TMP.setDataJekzamena(StringUtil.data_toApp(rs.getString(4)));
                     abit_TMP.setAuditorijaJekzamena(rs.getString(5));
                     abit_TMP.setPredmet(rs.getString(6));
                     abit_TMP.setNomerPotoka(new Integer(rs.getInt(7)));
                     abit_TMP.setKodRaspisanija(new Integer(rs.getInt(8)));
                     abits_Rasp.add(abit_TMP);
                   }
                   if((session.getAttribute("priznakSortirovki")+"").equals("'%'"))
                   abit_Rasp.setPriznakSortirovki("-15");
                   else 
                   abit_Rasp.setPriznakSortirovki(session.getAttribute("priznakSortirovki")+"");
                   if((session.getAttribute("stolbetsSortirovki")+"").equals("'%'"))
                   abit_Rasp.setStolbetsSortirovki(new Integer(-1));
                   else 
                   abit_Rasp.setStolbetsSortirovki(new Integer(session.getAttribute("stolbetsSortirovki")+""));
                 }

/************************************************************************************************/
/*******************  Если action="mod_del", то зачитываем одну запись из БД ********************/

            } else if ( form.getAction().equals("mod_del") ) {
                stmt = conn.prepareStatement("SELECT DISTINCT KodRaspisanija,Gruppa,DataKonsultatsii,AuditorijaKonsultatsii,DataJekzamena,AuditorijaJekzamena,KodPredmeta,Gruppy.Potok,Gruppy.KodGruppy FROM Raspisanie,Gruppy WHERE Raspisanie.KodGruppy = Gruppy.KodGruppy AND KodRaspisanija LIKE ? ORDER BY 2,8,4,6,3,5 ASC");
                stmt.setObject(1,abit_Rasp.getKodRaspisanija(),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  abit_Rasp.setKodRaspisanija(new Integer(rs.getInt(1)));
                  abit_Rasp.setGruppa(rs.getString(2));
                  abit_Rasp.setDataKonsultatsii(StringUtil.data_toApp(rs.getString(3)));
                  abit_Rasp.setAuditorijaKonsultatsii(rs.getString(4));
                  abit_Rasp.setDataJekzamena(StringUtil.data_toApp(rs.getString(5)));
                  abit_Rasp.setAuditorijaJekzamena(rs.getString(6));
                  abit_Rasp.setKodPredmeta(new Integer(rs.getInt(7)));
                  abit_Rasp.setNomerPotoka(new Integer(rs.getInt(8)));
                  abit_Rasp.setKodGruppy(new Integer(rs.getInt(9)));
                }
                form.setAction(us.getClientIntName("md_dl","form"));

/************************************************************************************************/
/********************  Если action="change", то изменяем указанную запись  **********************/
/************ или если передается дополнительный параметр "delete" - удаляем запись *************/

            } else if ( form.getAction().equals("change") && request.getParameter("delete") == null ) {
                  form.setAction(us.getClientIntName("change","act"));
                  stmt = conn.prepareStatement("UPDATE Raspisanie SET DataKonsultatsii=?,AuditorijaKonsultatsii=?,DataJekzamena=?,AuditorijaJekzamena=?,KodPredmeta=? WHERE KodRaspisanija LIKE ?");
                  stmt.setObject(1, StringUtil.data_toDB(abit_Rasp.getDataKonsultatsii()),Types.VARCHAR);
                  stmt.setObject(2, abit_Rasp.getAuditorijaKonsultatsii(),Types.VARCHAR);
                  stmt.setObject(3, StringUtil.data_toDB(abit_Rasp.getDataJekzamena()),Types.VARCHAR);
                  stmt.setObject(4, abit_Rasp.getAuditorijaJekzamena(),Types.VARCHAR);
                  stmt.setObject(5, abit_Rasp.getKodPredmeta(),Types.INTEGER);
                  stmt.setObject(6, abit_Rasp.getKodRaspisanija(),Types.INTEGER);
                  stmt.executeUpdate();
                  stmt = conn.prepareStatement("UPDATE Gruppy SET Potok=? WHERE KodGruppy LIKE ?");
                  stmt.setObject(1, abit_Rasp.getNomerPotoka(),Types.INTEGER);
                  stmt.setObject(2, abit_Rasp.getKodGruppy(),Types.INTEGER);
                  stmt.executeUpdate();
                  raspisanie_f  = true;

/************************************************************************************************/
            } else if ( request.getParameter("delete") != null ) {
                form.setAction(us.getClientIntName("delete","act"));
                stmt = conn.prepareStatement("DELETE FROM Raspisanie WHERE KodRaspisanija LIKE ?");
                stmt.setObject(1,abit_Rasp.getKodRaspisanija(),Types.INTEGER);
                stmt.executeUpdate();
                raspisanie_f  = true;
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
        request.setAttribute("abit_Rasp", abit_Rasp);
        request.setAttribute("message", message);
        request.setAttribute("abits_Rasp", abits_Rasp);
        request.setAttribute("abits_Rasp2", abits_Rasp2);
        request.setAttribute("abit_Rasp_S1", abit_Rasp_S1);
        request.setAttribute("abit_Rasp_S2", abit_Rasp_S2);
        request.setAttribute("abit_Rasp_S3", abit_Rasp_S3);
        request.setAttribute("abit_Rasp_S4", abit_Rasp_S4);
        request.setAttribute("abit_Rasp_S5", abit_Rasp_S5);
        request.setAttribute("abit_Rasp_S6", abit_Rasp_S6);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(raspisanie_f) return mapping.findForward("raspisanie_f");
        return mapping.findForward("success");
    }
}