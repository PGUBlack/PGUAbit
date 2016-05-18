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

public class MedConfirmAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession         session		       = request.getSession();
        Connection          conn                 = null;
        PreparedStatement   stmt                 = null;
        ResultSet           rs                   = null;
        ActionErrors        errors               = new ActionErrors();
        ActionError         msg                  = null;
        MedConfirmForm      form                 = (MedConfirmForm) actionForm;
        AbiturientBean      abit_MC              = form.getBean(request, errors);
        boolean             error                = false;
        ActionForward       f                    = null;
        ArrayList           abits_MC             = new ArrayList();
        ArrayList           abit_MC_S1           = new ArrayList();
        ArrayList           abit_MC_S2           = new ArrayList();
        ArrayList           abit_MC_S3           = new ArrayList();
        ArrayList           abit_MC_S5           = new ArrayList();
        int                 numberpotok          = 0;
        int                 numberfakults        = 0;
        int                 knumberfakults[];
        String              ResultSets[][];
        int                 kodnotl              = 0;
        int                 kolotl               = 0;
        int                 kolfakults           = 0;
        int                 kolpotok             = 0;
        int                 oldKodeF             = 0;
        UserBean            user                 = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "medConfirmAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "medConfirmForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/****************** ВЫВОД МЕДАЛИСТОВ ПО ВЫБРАННОМУ ПОТОКУ (ИЛИ ВСЕМ ПОТОКАМ) ********************/
/************************************************************************************************/

                if(form.getAction()==null) { 
                  form.setAction(us.getClientIntName("show","init"));
                  stmt = conn.prepareStatement("SELECT DISTINCT Potok FROM Gruppy ORDER BY 1 ASC");
                  rs = stmt.executeQuery();
                  if(rs.next()) abit_MC.setNomerPotoka(new Integer(rs.getInt(1)));
                }
                numberpotok = 0;

                stmt = conn.prepareStatement("SELECT DISTINCT Potok FROM Gruppy ORDER BY 1 ASC");
                rs = stmt.executeQuery();
                while(rs.next())
                {
                    AbiturientBean abit_TMP = new AbiturientBean();
                    abit_TMP.setNomerPotoka(new Integer(rs.getInt(1)));
                    abit_MC_S1.add(abit_TMP);
                    numberpotok++;
                }

                kolpotok=numberpotok;
                stmt = conn.prepareStatement("SELECT COUNT(DISTINCT KodFakulteta) FROM Fakultety WHERE Fakultety.KodVuza LIKE ?");
                stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                rs = stmt.executeQuery();
                if(rs.next())
                {
                  numberfakults = rs.getInt(1);
                }

                kolfakults=numberfakults;
                knumberfakults = new int [numberfakults];

                numberfakults=0;
                stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta FROM Fakultety WHERE Fakultety.KodVuza LIKE ? ORDER BY 1 ASC");
                stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next())
                {
                  knumberfakults[numberfakults] = rs.getInt(1);
                  numberfakults++;
                }

                stmt = conn.prepareStatement("SELECT f.KodFakulteta,f.AbbreviaturaFakulteta,COUNT(DISTINCT a.KodAbiturienta) FROM Abiturient a,Fakultety f,Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodVuza LIKE ? AND g.Potok LIKE ? AND a.PodtvMed LIKE 'д' AND a.KodSpetsialnosti = s.KodSpetsialnosti AND f.KodFakulteta = s.KodFakulteta GROUP BY f.KodFakulteta,f.AbbreviaturaFakulteta ORDER BY 1 ASC");
                stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2, abit_MC.getNomerPotoka(),Types.INTEGER);
                rs = stmt.executeQuery();
                while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setVsegoMed(new Integer(rs.getInt(3)));
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
                   abit_MC_S5.add(abit_TMP);
                 }

                ResultSets = new String[kolfakults][2];
                for(numberfakults=0;numberfakults<kolfakults;numberfakults++)
                   {
                        ResultSets[numberfakults][0]="0";
                        ResultSets[numberfakults][1]="0";
                   }

/******************************************************************************/
/*********************** Генерация отчета *************************************/

                if(form.getAction().equals("report")){

                numberpotok=0;
                numberfakults=0;

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Подтвержденные медалисты";

    String file_con = new String("med_confirm");

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

                  report.write("{\\rtf1\\ansi\n");
                  stmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
                  stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                  rs = stmt.executeQuery();
                  if(rs.next()) 
                    report.write("\\pard\\fs40 \\qc "+rs.getString(1)+"\\par\n");

                    report.write("\\par\n");
                    report.write("\\fs32 \\b Подтвержденные медалисты по потокам\\par\\par\n");
                    while(numberpotok<kolpotok-1){

                       report.write("\\fs28\\b\n");
                       report.write("\\ql Поток №"+(++numberpotok)+"\n");
                       report.write("\\par\n");
                       report.write("\\intbl\\trowd\\row");
                       report.write("\\fs20 \\trowd \\trql\\trgaph108\\trrh280\\trleft36\n");
                       report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1600\n");
                       report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2800\n");
                       report.write("\\qc");
                       report.write("\\intbl ФАКУЛЬТЕТ \\cell\n");
                       report.write("\\intbl ВСЕГО \\cell\n");
                       report.write("\\intbl \\row\n");

                       stmt = conn.prepareStatement("SELECT f.KodFakulteta,f.AbbreviaturaFakulteta,COUNT(DISTINCT a.KodAbiturienta) FROM Abiturient a,Fakultety f,Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodVuza LIKE ? AND g.Potok LIKE ? AND a.PodtvMed LIKE 'д' AND a.KodSpetsialnosti = s.KodSpetsialnosti AND f.KodFakulteta = s.KodFakulteta GROUP BY f.KodFakulteta,f.AbbreviaturaFakulteta ORDER BY 1 ASC");
                       stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                       stmt.setObject(2, new Integer(""+numberpotok),Types.INTEGER);
                       rs = stmt.executeQuery();

                       for(numberfakults=0;numberfakults<kolfakults;numberfakults++)
                       {
                          ResultSets[numberfakults][0]="0";
                          ResultSets[numberfakults][1]="0";
                       }

                       while(rs.next()) {
                            for(numberfakults=0;numberfakults<kolfakults;numberfakults++)
                              {
                              if(knumberfakults[numberfakults]==rs.getInt(1))
                                {
                                  if(ResultSets[numberfakults][0]=="0") ResultSets[numberfakults][0]=rs.getString(2);
                                  ResultSets[numberfakults][1]=rs.getString(3);
                                  break;
                                }                   
                            }
                       }

// Признак того, что хотя бы один факультет имеет подтвержденных медалистов. Нужен для добавления прочерков в таблицы
                       int priznak=0;
                       for(numberfakults=0;numberfakults<kolfakults;numberfakults++)
                          if(ResultSets[numberfakults][0]!="0")
                           {  
                            priznak = 1;
                            report.write("\\b0\\fs20 \\trowd \\trql\\trgaph108\\trrh280\\trleft36\n");
                            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1600\n");
                            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2800\n");
                            report.write("\\intbl "+ResultSets[numberfakults][0]+"\\cell\n");
                            report.write("\\intbl "+ResultSets[numberfakults][1]+"\\cell\n");
                            report.write("\\intbl \\row\n");
                          }
                          if(priznak==0){
                            report.write("\\b0\\fs20 \\trowd \\trql\\trgaph108\\trrh280\\trleft36\n");
                            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx1600\n");
                            report.write("\\clvertalc \\clbrdrt\\brdrs\\brdrw15\\brdrcf1 \\clbrdrl\\brdrs\\brdrw15\\brdrcf1 \\clbrdrb\\brdrs\\brdrw15\\brdrcf1 \\clbrdrr\\brdrs\\brdrw15\\brdrcf1 \\cellx2800\n");
                            report.write("\\intbl - \\cell\n");
                            report.write("\\intbl - \\cell\n");
                            report.write("\\intbl \\row\n");
                          }
                          report.write("\\pard\\intbl\\trowd\\row\\pard\\par\\par\\par\\par\n");
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
        request.setAttribute("abit_MC", abit_MC);
        request.setAttribute("abits_MC", abits_MC);
        request.setAttribute("abit_MC_S1", abit_MC_S1);
        request.setAttribute("abit_MC_S2", abit_MC_S2);
        request.setAttribute("abit_MC_S5", abit_MC_S5);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}