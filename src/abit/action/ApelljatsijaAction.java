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
import abit.sql.*; 

public class ApelljatsijaAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession        session        = request.getSession();
        Connection         conn           = null;
        PreparedStatement  stmt           = null;
        ResultSet          rs             = null;
        PreparedStatement  stmt2          = null;
        ResultSet          rs2            = null;
        ActionErrors       errors         = new ActionErrors();
        ActionError        msg            = null;
        ApelljatsijaForm   form           = (ApelljatsijaForm) actionForm;
        AbiturientBean     abit_Gr        = form.getBean(request, errors);
        boolean            apelljatsija_f = false;
        boolean            error          = false;
        ActionForward      f              = null;
        int                kGr            = 1;
        ArrayList          abits_ap       = new ArrayList();
        ArrayList          predmets       = new ArrayList();
        ArrayList          no_nlds        = new ArrayList();
        UserBean           user           = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "apelljatsijaAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute("apelljatsijaForm", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
 
          if ( form.getAction() == null ) {
             
               form.setAction(us.getClientIntName("new","init"));

// ****************** �������� ****************************

            stmt = conn.prepareStatement("SELECT DISTINCT Predmet,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? ORDER BY KodPredmeta ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean predm = new AbiturientBean();
              predm.setPredmet(rs.getString(1));
              predm.setKodPredmeta(new Integer(rs.getString(2)));
              predmets.add(predm);
            }

            } else if( form.getAction().equals("save")) {

              String kodAb="0";
              Enumeration paramNames = request.getParameterNames();
              while(paramNames.hasMoreElements()) {
                String paramName = (String)paramNames.nextElement();
                String paramValue[] = request.getParameterValues(paramName);
// ���������� ������
                if(paramName.indexOf("ots") != -1) {
                  if(!((paramValue[0]).trim()).equals("")){
                    kodAb = new String(paramName.substring(3));
                    stmt = conn.prepareStatement("UPDATE Otsenki SET Otsenka='"+(paramValue[0]).trim()+"',Apelljatsija='�' WHERE KodAbiturienta LIKE '"+kodAb+"' AND KodPredmeta LIKE '"+abit_Gr.getKodPredmeta()+"'");
                    stmt.executeUpdate();
                  }
                }
// ���������� ����
                if(paramName.indexOf("dap") != -1) {
                  if(!((paramValue[0]).trim()).equals("")){
                    kodAb = new String(paramName.substring(3));
                    stmt = conn.prepareStatement("UPDATE Otsenki SET Data='"+StringUtil.DataConverter((paramValue[0]).trim())+"',Apelljatsija='�' WHERE KodAbiturienta LIKE '"+kodAb+"' AND KodPredmeta LIKE '"+abit_Gr.getKodPredmeta()+"'");
                    stmt.executeUpdate();
                  }
                }
              }
              form.setAction(us.getClientIntName("doit","act-save"));

            } 

            if(form.getAction().equals("doit")) {

            stmt = conn.prepareStatement("SELECT DISTINCT Datelnyj,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND KodPredmeta LIKE ?");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            stmt.setObject(2, abit_Gr.getKodPredmeta(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) {
              abit_Gr.setPredmet(rs.getString(1).toUpperCase());
            }

/****************************************************************/
// ������ ������ ������ ������� ������ ��� � �������: ���%���%...
/****************************************************************/
            int cur_delo=0;
            int por_num1=0;
            int por_num2=0;
            String dela = abit_Gr.getSpecial3();

            while(cur_delo != -1 && cur_delo < dela.length()){
               String NomerDela = dela.substring(cur_delo,dela.indexOf('%',cur_delo+1));
               cur_delo = dela.indexOf('%',cur_delo)+1;

               if(cur_delo < 0) break;
// ������ ������ ����������� � ������� ���
///               stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.KodPredmeta,np.Sokr,o.Otsenka,o.Data,Apelljatsija FROM Abiturient a,Otsenki o,NazvanijaPredmetov np WHERE np.KodPredmeta=o.KodPredmeta AND a.KodAbiturienta=o.KodAbiturienta AND a.KodVuza LIKE ? AND o.KodPredmeta LIKE ? AND a.NomerLichnogoDela LIKE ? AND (From_Ege IS NULL OR From_Ege LIKE '�')");
               stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.KodPredmeta,np.Sokr,o.Otsenka,o.Data,Apelljatsija FROM Abiturient a,Otsenki o,NazvanijaPredmetov np WHERE np.KodPredmeta=o.KodPredmeta AND a.KodAbiturienta=o.KodAbiturienta AND a.KodVuza LIKE ? AND o.KodPredmeta LIKE ? AND a.NomerLichnogoDela LIKE ?");
               stmt.setObject(1, session.getAttribute("kVuza"),Types.VARCHAR);
               stmt.setObject(2, abit_Gr.getKodPredmeta(),Types.VARCHAR);
               stmt.setObject(3, NomerDela,Types.VARCHAR);
               rs=stmt.executeQuery();
               if(rs.next()) {
// ����� ������� ���� ����������
                 AbiturientBean abit_TMP = new AbiturientBean();
                 abit_TMP.setKodAbiturienta(new Integer(rs.getString(1)));
                 abit_TMP.setFamilija(rs.getString(2)+" "+rs.getString(3).substring(0,1).toUpperCase()+"."+rs.getString(4).substring(0,1).toUpperCase()+".");
                 abit_TMP.setKodPredmeta(new Integer(rs.getString(5)));
                 abit_TMP.setPredmet(rs.getString(6));
                 abit_TMP.setSpecial1(rs.getString(7));
                 abit_TMP.setDataJekzamena(rs.getString(8));
                 abit_TMP.setApelljatsija(rs.getString(9));
                 abit_TMP.setNumber(""+(++por_num1));
                 abits_ap.add(abit_TMP);
               } else {
// ��� �� ����������
                     abit_Gr.setSpecial5("yes");
                     MessageBean no_nld = new MessageBean();
                     no_nld.setStatus("������!");
                     no_nld.setMessage("����: "+NomerDela+" �� �������.");
                     no_nld.setId(++por_num2);
                     no_nlds.add(no_nld);
                 }
}//while
              form.setAction(us.getClientIntName("full","view"));
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
        request.setAttribute("abit_Gr", abit_Gr);
        request.setAttribute("abits_ap", abits_ap);
        request.setAttribute("predmets", predmets);
        request.setAttribute("no_nlds", no_nlds);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(apelljatsija_f) return mapping.findForward("apelljatsija_f");
        return mapping.findForward("success");
    }
}