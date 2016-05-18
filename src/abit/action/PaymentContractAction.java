package abit.action;

import abit.bean.AbiturientBean;
import abit.bean.GroupBean;
import abit.bean.UserBean;
import abit.sql.UserConn;
import abit.util.StringUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PaymentContractAction
  extends Action
{
  public ActionForward perform(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    HttpSession session = request.getSession();
    Connection conn = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    ActionErrors errors = new ActionErrors();
    ActionError msg = null;
    PaymentContractForm form = (PaymentContractForm)actionForm;
    AbiturientBean abit_A = form.getBean(request, errors);
    boolean error = false;
    UserBean user = (UserBean)session.getAttribute("user");
    boolean priznak = true;
    String kPredmeta = new String();
    ArrayList abits_A = new ArrayList();
    ArrayList abit_A_S1 = new ArrayList();
    ArrayList abit_A_S2 = new ArrayList();
    ArrayList abit_A_S3 = new ArrayList();
    ArrayList abit_A_S4 = new ArrayList();
    ArrayList abit_A_S5 = new ArrayList();
    int count = 0;
    int count_rek = 0;
    if ((user == null) || (user.getGroup() == null) || (user.getGroup().getTypeId() != 1))
    {
      msg = new ActionError("logon.must");
      errors.add("logon.login", msg);
    }
    if (errors.empty())
    {
      request.setAttribute("paymentContractAction", new Boolean(true));
      Locale locale = new Locale("ru", "RU");
      session.setAttribute("org.apache.struts.action.LOCALE", locale);
      try
      {
        UserConn us = new UserConn(request, mapping);
        conn = us.getConn(user.getSid());
        request.setAttribute("paymentContractForm", form);
        if (us.quit("exit")) {
          return mapping.findForward("back");
        }
        if (form.getAction() == null)
        {
          session.removeAttribute("kod_fak");
          session.removeAttribute("kod_spec");
          
          stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
          stmt.setObject(1, session.getAttribute("kVuza"), 4);
          rs = stmt.executeQuery();
          while (rs.next())
          {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
            abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
            abit_A_S1.add(abit_TMP);
          }
          priznak = true;
          kPredmeta = new String();
          String oldKode = new String();
          String oldKodFak = new String();
          String oldAbbr = new String();
          stmt = conn.prepareStatement("SELECT DISTINCT Spetsialnosti.KodSpetsialnosti,EkzamenyNaSpetsialnosti.KodPredmeta,Abbreviatura,AbbreviaturaFakulteta,Spetsialnosti.KodFakulteta FROM Spetsialnosti,EkzamenyNaSpetsialnosti,Fakultety WHERE Fakultety.KodFakulteta = Spetsialnosti.KodFakulteta AND Spetsialnosti.KodSpetsialnosti = EkzamenyNaSpetsialnosti.KodSpetsialnosti AND KodVuza LIKE ? ORDER BY Abbreviatura,EkzamenyNaSpetsialnosti.KodPredmeta,AbbreviaturaFakulteta ASC");
          stmt.setObject(1, session.getAttribute("kVuza"), 4);
          rs = stmt.executeQuery();
          while (rs.next())
          {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setSpecial1(rs.getString(1));
            if (priznak)
            {
              oldKode = rs.getString(1);oldKodFak = rs.getString(5);oldAbbr = rs.getString(3);priznak = false;
            }
            if (!oldKode.equals(rs.getString(1)))
            {
              if (kPredmeta.equals("")) {
                kPredmeta = kPredmeta + "%" + rs.getString(2);
              }
              abit_TMP.setSpecial1(oldKodFak + "$" + oldKode + kPredmeta);
              abit_TMP.setAbbreviatura(oldAbbr);
              abit_A_S2.add(abit_TMP);
              priznak = true;
              kPredmeta = "";
            }
            kPredmeta = kPredmeta + "%" + rs.getString(2);
          }
          AbiturientBean abit_TMP2 = new AbiturientBean();
          abit_TMP2.setSpecial1(oldKodFak + "$" + oldKode + kPredmeta);
          abit_TMP2.setAbbreviatura(oldAbbr);
          abit_A_S2.add(abit_TMP2);
          
          form.setAction(us.getClientIntName("menu", "init"));
        }
        else if (form.getAction().equals("updatebase"))
        {
          String kk = "";
          



          Enumeration paramNames = request.getParameterNames();
          while (paramNames.hasMoreElements())
          {
            String paramName = (String)paramNames.nextElement();
            String[] paramValue = request.getParameterValues(paramName);
            if (paramName.indexOf("clsc") != -1)
            {
              kk = new String(paramName.substring(4));
              String ks=abit_A.getSpecial1().substring(abit_A.getSpecial1().indexOf("$") + 1, abit_A.getSpecial1().indexOf("%"));
              
              stmt = conn.prepareStatement("UPDATE konkurs SET npd=? WHERE KodAbiturienta LIKE ? and kodspetsialnosti like ?");
              if ((paramValue[0] == null) || ((paramValue[0] != null) && StringUtil.toDB((paramValue[0]).trim()).equals(""))) {
                stmt.setNull(1, 12);
              } else {
                stmt.setObject(1, StringUtil.toDB(paramValue[0]), 12);
              }
              stmt.setObject(2, kk, 12);
              stmt.setObject(3, ks, Types.VARCHAR);
              stmt.executeUpdate();
            }
            else if (paramName.indexOf("cls") != -1)
            {
              kk = new String(paramName.substring(3));
              stmt = conn.prepareStatement("UPDATE Konkurs SET Dog_Ok=? WHERE KodKonkursa LIKE ?");
              if ((paramValue[0] == null) || ((paramValue[0] != null) && StringUtil.toDB((paramValue[0]).trim()).equals("í"))) {
                stmt.setNull(1, 12);
              } else {
                stmt.setObject(1, StringUtil.toDB(paramValue[0]), 12);
              }
              stmt.setObject(2, kk, 12);
              stmt.executeUpdate();
            }
          }
          form.setAction(us.getClientIntName("show", "view"));
        }
        if (form.getAction().equals("show"))
        {
          if (session.getAttribute("kod_fak") == null) {
            session.setAttribute("kod_fak", StringUtil.toMySQL(abit_A.getKodFakulteta().toString()));
          }
          if (session.getAttribute("kod_spec") == null) {
            session.setAttribute("kod_spec", StringUtil.toMySQL(abit_A.getSpecial1().substring(abit_A.getSpecial1().indexOf("$") + 1, abit_A.getSpecial1().indexOf("%"))));
          }
          count = 0;
          count_rek = 0;
          stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.NomerLichnogoDela,a.Familija,a.Imja,a.Otchestvo,kon.Dog_Ok,s.Abbreviatura,a.KodSpetsialnosti,kon.KodKonkursa, s.Abbreviatura, kon.Zach, kon.Dog_Ok, kon.npd FROM Abiturient a,Spetsialnosti s,Konkurs kon WHERE kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND kon.KodSpetsialnosti LIKE ? AND a.KodVuza LIKE ? and kon.Dog LIKE 'ä'  ORDER BY kon.Zach DESC,a.Familija,a.Imja,a.Otchestvo");
          stmt.setObject(1, session.getAttribute("kod_spec"), 4);
          stmt.setObject(2, session.getAttribute("kVuza"), 4);
          rs = stmt.executeQuery();
          while (rs.next())
          {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodAbiturienta(new Integer(rs.getInt(1)));
            abit_TMP.setNomerLichnogoDela(rs.getString(2));
            abit_TMP.setFamilija(rs.getString(3));
            abit_TMP.setImja(rs.getString(4));
            abit_TMP.setOtchestvo(rs.getString(5));
            abit_TMP.setKodKonkursa(new Integer(rs.getInt(9)));
            abit_TMP.setSpecial1(rs.getString(10));
            abit_TMP.setSpecial4(rs.getString(8));
            if ((rs.getString(11) != null) && (rs.getString(11).equals("ð"))) {
              count_rek++;
            }
            abit_TMP.setNumber(Integer.toString(++count));
            if (rs.getString(13) == null) {
              abit_TMP.setNomerPlatnogoDogovora("");
            } else {
              abit_TMP.setNomerPlatnogoDogovora(rs.getString(13));
            }
            if (rs.getString(12) == null) {
              abit_TMP.setDog_ok_1("í");
            } else {
              abit_TMP.setDog_ok_1(rs.getString(12));
            }
            abits_A.add(abit_TMP);
          }
          abit_A.setSpecial22(new Integer(count));
          
          abit_A.setSpecial11(new Integer(count_rek));
          form.setAction(us.getClientIntName("full", "view"));
        }
      }
      catch (SQLException e)
      {
        request.setAttribute("SQLException", e);
        return mapping.findForward("error");
      }
      catch (Exception e)
      {
        ActionForward localActionForward;
        request.setAttribute("JAVAexception", e);
        return mapping.findForward("error");
      }
      finally
      {
        if (rs != null) {
          try
          {
            rs.close();
          }
          catch (Exception localException10) {}
        }
        if (stmt != null) {
          try
          {
            stmt.close();
          }
          catch (Exception localException11) {}
        }
        if (conn != null) {
          try
          {
            conn.close();
          }
          catch (Exception localException12) {}
        }
      }
      if (rs != null) {
        try
        {
          rs.close();
        }
        catch (Exception localException13) {}
      }
      if (stmt != null) {
        try
        {
          stmt.close();
        }
        catch (Exception localException14) {}
      }
      if (conn != null) {
        try
        {
          conn.close();
        }
        catch (Exception localException15) {}
      }
      request.setAttribute("abit_A", abit_A);
      request.setAttribute("abits_A", abits_A);
      request.setAttribute("abit_A_S1", abit_A_S1);
      request.setAttribute("abit_A_S2", abit_A_S2);
      request.setAttribute("abit_A_S3", abit_A_S3);
      request.setAttribute("abit_A_S4", abit_A_S4);
      request.setAttribute("abit_A_S5", abit_A_S5);
    }
    if (error) {
      return mapping.findForward("error");
    }
    return mapping.findForward("success");
  }
}
