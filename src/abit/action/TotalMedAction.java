package abit.action;

import java.io.IOException;
import java.io.*;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

public class TotalMedAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession         session             = request.getSession();
        Connection          conn                = null;
        PreparedStatement   stmt                = null;
        ResultSet           rs                  = null;
        ActionErrors        errors              = new ActionErrors();
        ActionError         msg                 = null;
        TotalMedForm        form                = (TotalMedForm) actionForm;
        AbiturientBean      abit_TM             = form.getBean(request, errors);
        boolean             error               = false;
        ActionForward       f                   = null;
        ArrayList           abits_TM            = new ArrayList();
        ArrayList           abit_TM_S1          = new ArrayList();
        ArrayList           abit_TM_S2          = new ArrayList();
        ArrayList           abit_TM_S3          = new ArrayList();
        ArrayList           abit_TM_S5          = new ArrayList();
        int                 numberpotok         = 0;
        int                 cur_Otl             = 0;
        int                 cur_Fak             = 0;
        int                 kcur_Fak[];
        int                 kcur_Otl[];
        String              ResultSets[][];
        int                 kolotl              = 0;
        int                 kolfakults          = 0;
        int                 kolpotok            = 0;
        int                 oldKodeF            = 0;
        UserBean            user                = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "totalMedAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "totalMedForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/****************** ВЫВОД МЕДАЛИСТОВ ПО ВЫБРАННОМУ ПОТОКУ (ИЛИ ВСЕМ ПОТОКАМ) ********************/
/************************************************************************************************/

if(form.getAction()==null) { 

       form.setAction(us.getClientIntName("show","init"));
       stmt = conn.prepareStatement("SELECT DISTINCT Potok FROM Gruppy ORDER BY Potok ASC");
         rs = stmt.executeQuery();
       if(rs.next()) abit_TM.setNomerPotoka(new Integer(rs.getInt(1)));

       stmt = conn.prepareStatement("SELECT DISTINCT ShifrMedali,KodMedali FROM Medali ORDER BY KodMedali ASC");
         rs = stmt.executeQuery();
       if(rs.next()) {
         abit_TM.setShifrMedali(rs.getString(1));
         abit_TM.setKodMedali(new Integer(rs.getInt(2)));
       }
}
      stmt = conn.prepareStatement("SELECT DISTINCT Potok FROM Gruppy ORDER BY Potok ASC");
      rs = stmt.executeQuery();
      while(rs.next())
      {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setNomerPotoka(new Integer(rs.getInt(1)));
        abit_TM_S1.add(abit_TMP);
        numberpotok++;
      }

      stmt = conn.prepareStatement("SELECT DISTINCT ShifrMedali,KodMedali FROM Medali ORDER BY KodMedali ASC");
      rs = stmt.executeQuery();
      while(rs.next())
      {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setShifrMedali(rs.getString(1));
        abit_TMP.setKodMedali(new Integer(rs.getString(2)));
        abit_TM_S2.add(abit_TMP);
        cur_Otl++;
      }

      kolotl = cur_Otl;
      kcur_Otl = new int [cur_Otl];

      cur_Fak=0;
      stmt = conn.prepareStatement("SELECT COUNT(KodFakulteta) FROM Fakultety WHERE Fakultety.KodVuza LIKE ? ");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      if(rs.next()) cur_Fak=rs.getInt(1);

      kolfakults=cur_Fak;
      kcur_Fak = new int [cur_Fak];

      cur_Fak=0;
      stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE Fakultety.KodVuza LIKE ? ORDER BY Fakultety.KodFakulteta,AbbreviaturaFakulteta ASC");
      stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
      rs = stmt.executeQuery();
      while(rs.next())
      {
        kcur_Fak[cur_Fak] = rs.getInt(1);
        cur_Fak++;
      }

// Выборка абитуриентов

      StringBuffer query = new StringBuffer("SELECT f.AbbreviaturaFakulteta,COUNT(DISTINCT a.KodAbiturienta) FROM Abiturient a,Fakultety f,Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND g.Potok LIKE "+abit_TM.getNomerPotoka()+" AND a.KodMedali LIKE "+abit_TM.getKodMedali());

      if(abit_TM.getPodtvMed()!=null)
        if(abit_TM.getPodtvMed().equals("д"))
          query.append(" AND a.PodtvMed LIKE '"+abit_TM.getPodtvMed()+"'");

      query.append(" AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta GROUP BY f.KodFakulteta,f.AbbreviaturaFakulteta");

      stmt = conn.prepareStatement(query.toString());
      rs = stmt.executeQuery();
      while (rs.next()) {
        AbiturientBean abit_TMP = new AbiturientBean();
        abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
        abit_TMP.setVsegoMed(new Integer(rs.getInt(2)));
        abit_TM_S5.add(abit_TMP);
      }

      cur_Otl=0;
      stmt = conn.prepareStatement("SELECT DISTINCT KodMedali FROM Medali ORDER BY KodMedali ASC");
      rs = stmt.executeQuery();
      while(rs.next())
      {
         kcur_Otl[cur_Otl]=rs.getInt(1);
         cur_Otl++;
      }

// Создание массива для хранения результатов
      ResultSets = new String[kolfakults][kolotl+1];

/******************************************************************************/
/*********************** Генерация отчета *************************************/

      if(form.getAction().equals("report")){

       kolpotok=numberpotok-1;
       numberpotok=0;
       cur_Otl=0;
       cur_Fak=0;

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Медалисты по потокам";

    String file_con = new String("total_m_p");

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

    report.write("{\\rtf1\\ansi\n");
    stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza,NazvanieRodit FROM NazvanieVuza WHERE KodVuza LIKE ?");
    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
    rs = stmt.executeQuery();
    if(rs.next()) 
      report.write("\\pard\\fs40 \\qc "+rs.getString(1)+"\\par\n");

    report.write("\\par\n");
    report.write("\\fs32 \\b Медалисты по потокам\\par\\par\n");

    while(numberpotok<kolpotok)
    {
      report.write("\\fs28\\b\n");
      report.write("\\ql Поток №"+(++numberpotok)+"\n");
      report.write("\\par\n");
      report.write("\\intbl\\trowd\\row");

      report.write("\\fs24 \\trowd \\trql\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2000\n");
      report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(2000+800*kolotl)+"\n");
      report.write("\\qc");
      report.write("\\intbl ФАКУЛЬТЕТ \\cell\n");
      report.write("\\intbl ВСЕГО \\cell\n");
      report.write("\\intbl \\row\n");

      report.write("\\fs24 \\trowd \\trql\\trgaph108\\trrh280\\trleft36\n");
      report.write("\\clvmrg\\clvertalc \\clbrdrt \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2000\n");

      for(cur_Otl=0;cur_Otl<kolotl;cur_Otl++) {
        report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(2000+800*(cur_Otl+1))+"\n");
      }
                    
      stmt = conn.prepareStatement("SELECT ShifrMedali,KodMedali FROM Medali ORDER BY KodMedali ASC");
      rs = stmt.executeQuery();
      report.write("\\intbl \\cell\n");
      while(rs.next()) {
        report.write("\\intbl "+rs.getString(1)+" \\cell\n");
      }
      report.write("\\intbl \\row\n");
      report.write("\\b0");

// Обнуление массива для хранения результатов

      for(cur_Fak=0;cur_Fak<kolfakults;cur_Fak++)
         for(cur_Otl=0;cur_Otl<kolotl+1;cur_Otl++)
          ResultSets[cur_Fak][cur_Otl]="0";

// Выборка абитуриентов

      query = new StringBuffer("SELECT f.AbbreviaturaFakulteta,COUNT(DISTINCT a.KodAbiturienta),f.KodFakulteta,m.KodMedali FROM Abiturient a,Fakultety f,Spetsialnosti s,Gruppy g, Medali m WHERE m.KodMedali=a.KodMedali AND a.KodGruppy=g.KodGruppy AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND g.Potok LIKE "+numberpotok);

      if(abit_TM.getPodtvMed()!=null)
        if(abit_TM.getPodtvMed().equals("д"))
          query.append(" AND a.PodtvMed LIKE '"+abit_TM.getPodtvMed()+"'");

      query.append(" AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta GROUP BY f.KodFakulteta,f.AbbreviaturaFakulteta,m.KodMedali");

      stmt = conn.prepareStatement(query.toString());
      rs = stmt.executeQuery();
      while(rs.next()) {

// Формирование массива результатов

        for(cur_Fak=0;cur_Fak<kolfakults;cur_Fak++)
        {
           for(cur_Otl=1;cur_Otl<kolotl+1;cur_Otl++)
           {
              if(kcur_Fak[cur_Fak]==rs.getInt(3))
                if(kcur_Otl[cur_Otl-1]==rs.getInt(4)) 
                {
                  if(ResultSets[cur_Fak][0]=="0") ResultSets[cur_Fak][0]=rs.getString(1);
                    ResultSets[cur_Fak][cur_Otl]=rs.getString(2);
                    continue;
                }
              }
           }
        }

// Вывод данных из массива результатов в файл отчета

        for(cur_Fak=0;cur_Fak<kolfakults;cur_Fak++)
           if(ResultSets[cur_Fak][0]!="0")
           {
             report.write("\\fs24 \\trowd \\trql\\trgaph108\\trrh280\\trleft36\n");
             report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2000\n");

             for(cur_Otl=0;cur_Otl<kolotl;cur_Otl++)
             {
                report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx"+(2000+800*(cur_Otl+1))+"\n");
             }
           
             for(cur_Otl=0;cur_Otl<=kolotl;cur_Otl++)
                report.write("\\intbl "+ResultSets[cur_Fak][cur_Otl]+"\\cell\n");

             report.write("\\intbl \\row\n");
        }

        report.write("\\pard\\par\\par\n");
      }

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
        request.setAttribute("abit_TM", abit_TM);
        request.setAttribute("abits_TM", abits_TM);
        request.setAttribute("abit_TM_S1", abit_TM_S1);
        request.setAttribute("abit_TM_S2", abit_TM_S2);
        request.setAttribute("abit_TM_S5", abit_TM_S5);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}