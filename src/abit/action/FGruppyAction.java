package abit.action;

import java.io.IOException;
import java.util.Enumeration;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

public class FGruppyAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession             session               = request.getSession();
        Connection              conn                  = null;
        PreparedStatement       stmt                  = null;
        PreparedStatement       stmt_a                = null;
        PreparedStatement       stmt_b                = null;
        ResultSet               rs                    = null;
        ResultSet               rs_a                  = null;
        ResultSet               rs_b                  = null;
        ActionErrors            errors                = new ActionErrors();
        ArrayList               fgr_msgs              = new ArrayList();
        ActionError             msg                   = null;
        FGruppyForm             form                  = (FGruppyForm) actionForm;
        AbiturientBean          abit_Spec             = form.getBean(request, errors);
        boolean                 fgruppy_f             = false;
        boolean                 error                 = false;
        ActionForward           f                     = null;
        String                  shifrFakulteta        = null;
        String                  kAbiturienta          = null;
        ArrayList               abits_Spec            = new ArrayList();
        ArrayList               groups_kon            = new ArrayList();
        ArrayList               groups_bud            = new ArrayList();
        ArrayList               groups_z_kon          = new ArrayList();
        ArrayList               groups_z_bud          = new ArrayList();
        ArrayList               abit_Spec_S1          = new ArrayList();
        ArrayList               groups                = new ArrayList();
        ArrayList               fakults               = new ArrayList();
        int                     total_added           = 0;
        int                     nomerGruppy           = 1;
        int                     countAbiturients      = 0;
        UserBean                user                  = (UserBean)session.getAttribute("user");
        boolean                 no_err                = true;
        boolean                 curr_err              = false;
        boolean                 renew                 = false;

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "fGruppyAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "fGruppyForm", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
               form.setAction(us.getClientIntName("new","init"));
               session.removeAttribute("kFak");
            }

            if( form.getAction().equals("new")) {
/************************************************************************************************/
/********************** ���������� ������ ��� ����� � ������� ��������� *************************/

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                AbiturientBean abit_TMP = new AbiturientBean();
                abit_TMP.setKodFakulteta( new Integer(rs.getString(1)));
                abit_TMP.setAbbreviaturaFakulteta( rs.getString(2));
                abit_Spec_S1.add(abit_TMP);
            }

            String s_line = new String();
            stmt = conn.prepareStatement("SELECT DISTINCT KodKursov,ShifrKursov FROM Kursy WHERE KodVuza LIKE ? ORDER BY 1 ASC");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
                s_line += rs.getString(2)+",";
            }
            abit_Spec.setShifrKursov(s_line.substring(0,s_line.length()-1));              

/********************************************************************/
/*************   �������� �������������� �����    *******************/
/********************************************************************/
            } else if (  form.getAction().equals("main") && request.getParameter("view")!=null ) {

                form.setAction(us.getClientIntName("full","view-done-grp"));
                 session.removeAttribute("kg");
// ������ ����������� �����������                   
                 stmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta,KodFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 1 ASC");
                 stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
                 rs = stmt.executeQuery();
                 if (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
                   abit_TMP.setKodFakulteta(new Integer(rs.getString(2)));  // KodFakulteta
                   fakults.add(abit_TMP);
                   if( session.getAttribute("kFak") == null) 
                   session.setAttribute("kFak",rs.getString(2));
                 }
                 while (rs.next()) {
                   AbiturientBean abit_TMP = new AbiturientBean();
                   abit_TMP.setAbbreviaturaFakulteta(rs.getString(1));
                   abit_TMP.setKodFakulteta(new Integer(rs.getString(2)));  // KodFakulteta
                   fakults.add(abit_TMP);
                 }
                 AbiturientBean abit_TMPx = new AbiturientBean();
                 abit_TMPx.setAbbreviaturaFakulteta("-");
                 abit_TMPx.setKodFakulteta(new Integer("-1"));  // KodFakulteta
                 fakults.add(abit_TMPx);

// ������� �����
                   if( request.getParameter("kFak") != null ) 
                     session.setAttribute("kFak",request.getParameter("kFak"));
/****************************************************/
/********************* ������� � �������� ***********/
/****************************************************/

// �����������
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.KodGruppy");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_kon.add(tmp);
                }
// ����������
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.KodGruppy");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_bud.add(tmp);
                }

/****************************************************/
/******************** ������� ***********************/
/****************************************************/

// �����������
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.KodGruppy");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_z_kon.add(tmp);
                }
// ����������
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,g.Gruppa,g.Potok FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok UNION SELECT KodGruppy,0,g.KodFakulteta,g.Gruppa,g.Potok FROM Gruppy g WHERE g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Gruppa,g.Potok) ORDER BY g.Potok,g.KodGruppy");
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(3,session.getAttribute("kFak"),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kFak"),Types.INTEGER);
                rs = stmt.executeQuery();
                while(rs.next()){
                  AbiturientBean tmp = new AbiturientBean();
                  tmp.setKodGruppy(new Integer(rs.getString(1)));
                  tmp.setAmount(rs.getInt(2));
                  tmp.setGruppa(rs.getString(4));
                  tmp.setNomerPotoka(new Integer(rs.getString(5)));
                  groups_z_bud.add(tmp);
                }
/********************************************************************/
/*****************       ��������� ������       *********************/
/********************************************************************/
            } else if (  form.getAction().equals("hand") ) {
               curr_err = false;
               if(request.getParameter("kg")!=null){
                 session.setAttribute("kg",request.getParameter("kg"));
               }
               if(request.getParameter("renew")!= null){
               renew = true;
//������ ������ ������� � ��������� �� ��� ���� ����������� � ������.
                 Enumeration paramNames = request.getParameterNames();
                 while(paramNames.hasMoreElements()) {
                   String paramName = (String)paramNames.nextElement();
                   String paramValue[] = request.getParameterValues(paramName);
                   if(paramName.indexOf("abt") != -1) {
// ������� ���� ������ �� �� �� �� ��������
                     stmt = conn.prepareStatement("SELECT KodGruppy FROM Gruppy WHERE Gruppa LIKE ?");
                     stmt.setObject(1, StringUtil.toDB(paramValue[0]),Types.VARCHAR);
                     rs = stmt.executeQuery();
                     if(rs.next()){
// ��������� ������ ����������
// ��������� ����������
                       stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=? WHERE KodAbiturienta LIKE ?");
                       stmt.setObject(1, new Integer(rs.getInt(1)),Types.INTEGER);
                       stmt.setObject(2, paramName.substring(3),Types.INTEGER);    // KodAbiturienta
                       stmt.executeUpdate();
                     } else {
// ���������� ������
                         curr_err = true;
                         MessageBean msg1 = new MessageBean();
                         msg1.setMessage("��������! ������: "+StringUtil.toDB(paramValue[0])+" �� ������� � ��. ��������� ����������.");
                         fgr_msgs.add(msg1);
                       }
                   }
                 }
               }
               if(!curr_err && request.getParameter("renew")!= null) {
                 MessageBean msg1 = new MessageBean();
                 msg1.setMessage("��������! ��� ��������� ������ �������.");
                 fgr_msgs.add(msg1);
               }
// ����� ������������ � �� ����� �� �����
               stmt = conn.prepareStatement("SELECT Abiturient.NomerLichnogoDela,Abiturient.Familija,Abiturient.Imja,Abiturient.Otchestvo,Gruppa,Abiturient.KodAbiturienta,NomerPlatnogoDogovora,SrokObuchenija from Abiturient,Gruppy WHERE  Abiturient.KodGruppy=Gruppy.KodGruppy AND Abiturient.DokumentyHranjatsja LIKE '�' AND Abiturient.KodVuza LIKE ? AND Abiturient.KodGruppy LIKE ? ORDER BY NomerLichnogoDela,Familija,Imja,Otchestvo ASC");
               stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
               stmt.setObject(2, session.getAttribute("kg"),Types.INTEGER);
               rs = stmt.executeQuery();
               while(rs.next()){
                 AbiturientBean abit_TMP = new AbiturientBean();
                 abit_TMP.setNomerLichnogoDela( rs.getString(1));
                 abit_TMP.setFamilija( rs.getString(2));
                 abit_TMP.setImja( rs.getString(3));
                 abit_TMP.setOtchestvo( rs.getString(4) );
                 abit_TMP.setGruppa( rs.getString(5) );
                 abit_TMP.setKodAbiturienta(new Integer(rs.getString(6)));
                 abit_TMP.setNomerPlatnogoDogovora( rs.getString(7) );
                 abit_TMP.setSrokObuchenija( rs.getString(8) );
               //  abit_TMP.setShifrKursov( rs.getString(9) );
                 abits_Spec.add(abit_TMP);
               }
               if(!renew) {
                   MessageBean msg1 = new MessageBean();
                   msg1.setMessage("��� ������ ���������� � �� ������� ������ ''���������''.");
                   fgr_msgs.add(msg1);
               }
               form.setAction(us.getClientIntName("hands","act-save"));

/********************************************************************/
/************** �������������� ������������ ����� *******************/
/********************************************************************/
            } else if (  form.getAction().equals("main") && abit_Spec.getSpecial1().equals("auto")) {
              total_added = 0;

// ������� ����� ����� � ���������� ������������ � ��� � ������������ � ��������� �������� ��������, ���� ���������� � ������� ������
            if(abit_Spec.getNomerPlatnogoDogovora().equals("yes")){
// �����������
//---��� ����������---
              if(StringUtil.toInt(""+abit_Spec.getKodFakulteta(),0)==-1){
              if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// --��� ������ ����������--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
              } else {
// --������ ��������� �����--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(4,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kVuza"),Types.INTEGER);
              }
              }
              else {
//---��������� ���������---
              if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// --��� ������ ����������--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,abit_Spec.getKodFakulteta(),Types.INTEGER);
              } else {
// --������ ��������� �����--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(4,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(5,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(6,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(7,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(8,abit_Spec.getKodFakulteta(),Types.INTEGER);
              }
              }
            }
            else if(abit_Spec.getNomerPlatnogoDogovora().equals("no")){
// ����������
              if(StringUtil.toInt(""+abit_Spec.getKodFakulteta(),0)==-1){
//---��� ����������---
              if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// --��� ������ ����������--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
              } else {
// --������ ��������� �����--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(4,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kVuza"),Types.INTEGER);
              }
              } else {
//---��������� ���������---
              if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// --��� ������ ����������--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,abit_Spec.getKodFakulteta(),Types.INTEGER);
              } else {
// --������ ��������� �����--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '�' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '�' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '�' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? AND g.Dogovornaja LIKE '��' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND g.Dogovornaja LIKE '��' AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND g.Dogovornaja LIKE '��' AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(4,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(5,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(6,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(7,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(8,abit_Spec.getKodFakulteta(),Types.INTEGER);
              }
              }
            }
            else {
// ����������� � ���������� ������ ����������
//---��� ����������---
              if(StringUtil.toInt(""+abit_Spec.getKodFakulteta(),0)==-1){
              if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// --��� ������ ����������--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja IN ('�','�') GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja IN ('��','��') GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
              } else {
// --������ ��������� �����--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja IN ('�','�') GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND g.Dogovornaja IN ('�','�') AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Dogovornaja IN ('�','�') AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' AND g.Dogovornaja IN ('��','��') GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE '%' AND g.Locked NOT LIKE 1 AND g.Dogovornaja IN ('��','��') AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE a.KodGruppy=g.KodGruppy AND g.Dogovornaja IN ('��','��') AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE '%' GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(4,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(5,session.getAttribute("kVuza"),Types.INTEGER);
              }
              } else {
//---��������� ���������---
              if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// --��� ������ ����������--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE g.Dogovornaja IN ('�','�') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Dogovornaja IN ('�','�') AND KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE g.Dogovornaja IN ('�','�') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE g.Dogovornaja IN ('��','��') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Dogovornaja IN ('��','��') AND KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE g.Dogovornaja IN ('��','��') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
              }
                stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(5,abit_Spec.getKodFakulteta(),Types.INTEGER);
              } else {
// --������ ��������� �����--
// ����
              if(abit_Spec.getSpecial2().equals("och"))
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE g.Dogovornaja IN ('�','�') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Dogovornaja IN ('�','�') AND g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE g.Dogovornaja IN ('�','�') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
// ������
              else {
                stmt = conn.prepareStatement("SELECT g.KodGruppy,COUNT(g.KodGruppy),g.KodFakulteta,s.KodSpetsialnosti,g.Potok,g.Gruppa FROM Abiturient a, Spetsialnosti s,Gruppy g WHERE g.Dogovornaja IN ('��','��') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa,s.KodSpetsialnosti UNION SELECT KodGruppy,0,g.KodFakulteta,-1,g.Potok,g.Gruppa FROM Gruppy g WHERE g.Dogovornaja IN ('��','��') AND g.Potok LIKE ? AND KodGruppy NOT LIKE 1 AND KodGruppy IS NOT NULL AND KodFakulteta LIKE ? AND g.Locked NOT LIKE 1 AND KodGruppy NOT IN(SELECT g.KodGruppy FROM Abiturient a, Spetsialnosti s,Gruppy WHERE g.Dogovornaja IN ('��','��') AND a.KodGruppy=g.KodGruppy AND g.Locked NOT LIKE 1 AND g.Potok LIKE ? AND a.KodSpetsialnosti=s.KodSpetsialnosti AND g.KodGruppy NOT LIKE 1 AND g.KodGruppy IS NOT NULL AND a.KodVuza LIKE ? AND g.KodFakulteta LIKE ? GROUP BY g.KodGruppy,g.KodFakulteta,g.Potok,g.Gruppa) ORDER BY g.Potok,g.KodGruppy");
}
                stmt.setObject(1,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(3,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(4,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(5,abit_Spec.getKodFakulteta(),Types.INTEGER);
                stmt.setObject(6,abit_Spec.getNomerPotoka(),Types.INTEGER);
                stmt.setObject(7,session.getAttribute("kVuza"),Types.INTEGER);
                stmt.setObject(8,abit_Spec.getKodFakulteta(),Types.INTEGER);
              }
              }
}
              rs = stmt.executeQuery();
              while(rs.next()){
                AbiturientBean tmp = new AbiturientBean();
                tmp.setKodGruppy(new Integer(rs.getString(1)));
                tmp.setAmount(rs.getInt(2));
                tmp.setKodFakulteta(new Integer(rs.getString(3)));
                tmp.setKodSpetsialnosti(new Integer(rs.getString(4)));
                groups.add(tmp);
              }

/************ ��������� ������������ ������������ ����������  **********/
/************             �� ������� ��������� ���������      **********/

// �������� ������������� ������� ��� �������� ����������� ��������������
              ArrayList spec_set = new ArrayList();
              spec_set.clear();
              String set = new String();

// ������� � �������: �������������1,�������������2(������������ � ������), ���-�� ����������� ���������.
              stmt = conn.prepareStatement("SELECT ens1.KodSpetsialnosti\"SP1\",ens2.KodSpetsialnosti\"SP2\",COUNT(ens2.KodSpetsialnosti)\"CNT\" FROM EkzamenyNaSpetsialnosti ens1,EkzamenyNaSpetsialnosti ens2, Spetsialnosti s1, Spetsialnosti s2 WHERE s1.KodSpetsialnosti=ens1.KodSpetsialnosti AND s2.KodSpetsialnosti=ens2.KodSpetsialnosti AND ens1.KodPredmeta LIKE ens2.KodPredmeta AND s1.KodFakulteta LIKE s2.KodFakulteta AND s2.KodFakulteta LIKE ? GROUP BY ens2.KodSpetsialnosti,ens1.KodSpetsialnosti ORDER BY ens1.KodSpetsialnosti,ens2.KodSpetsialnosti ASC");
              stmt.setObject(1,abit_Spec.getKodFakulteta(),Types.INTEGER);
              rs = stmt.executeQuery();
              while(rs.next()){

// ���������� �������� �� �������������. ����� ��� ��������� �������������� ����������.
              stmt_b = conn.prepareStatement("SELECT COUNT(KodSpetsialnosti) FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE ?");
              stmt_b.setObject(1,rs.getString(1),Types.INTEGER);
              rs_b = stmt_b.executeQuery();
              rs_b.next();
// ���� ������������� ��������� �� ������ ���������, �� ����������� �� ������������
              if(rs.getInt(3) == rs_b.getInt(1)) {
// ������� ������� ����������� �������������� ��� ���������� ���� ����� ����-���
                if(spec_set.size() == 0) {
                  spec_set.add(","+rs.getString(1)+",");
                } else {
                  for(int item=0;item<spec_set.size();item++){
                    set = (String) spec_set.get(item);
                    if(set.indexOf(","+rs.getString(1)+",")!=-1){
// ��� 1� ������������� ��� ���� � ������������, - ��������� ��� ������
                      if(set.indexOf(","+rs.getString(2)+",")==-1){
// ���� ���� 2� ������������� ��� � ������������, �� ��������� ���
                        set += rs.getString(2)+",";
                        spec_set.remove(item);
                        spec_set.add(item,set);
                      }
                    } else {
                      spec_set.add(","+rs.getString(1)+",");
                    }
                  }
                }
              }
              }
// � ���������� ������ ���������������� ���� ����� ������ ��������������,
// ������������ ��������. ������ ������ ������������ ����� ������������ ��������������.

for(int subset=0;subset<spec_set.size();subset++) {

/********************************************************************/
/***** ������� ������������ (��� ������������� �� �� �������) *******/
/********************************************************************/

              String query = new String("SELECT KodAbiturienta,s.KodFakulteta,a.KodSpetsialnosti FROM Forma_Obuch fo, Abiturient a,Gruppy g,Spetsialnosti s,Kursy k WHERE k.KodKursov=a.KodKursov AND a.KodGruppy=g.KodGruppy AND fo.KodFormyOb=a.KodFormyOb AND s.KodSpetsialnosti=a.KodSpetsialnosti AND a.KodSpetsialnosti IN(NULL");
              query+= spec_set.get(subset) + "NULL) AND (a.KodGruppy LIKE '1' OR a.KodGruppy IS NULL)";
              if(abit_Spec.getNomerPlatnogoDogovora().equals("yes")) query+=" AND NomerPlatnogoDogovora IS NOT NULL";
              if(abit_Spec.getNomerPlatnogoDogovora().equals("no"))  query+=" AND NomerPlatnogoDogovora IS NULL";
// ��������� ������������������ �������� ������� ��� ����� ������
              query+= " AND ShifrKursov IN("+StringUtil.PlaceUnaryComas("ShifrKursov",abit_Spec.getShifrKursov());
              query+= ") AND a.DokumentyHranjatsja LIKE '�'";
if(abit_Spec.getSpecial2().equals("och"))
              query+= " AND fo.Sokr IN ('�����') ORDER BY s.KodFakulteta ASC";
else
              query+= " AND fo.Sokr IN ('�����') ORDER BY s.KodSpetsialnosti ASC";
              stmt = conn.prepareStatement(query);
              rs = stmt.executeQuery();

/***** ������������� ����������� � ������ *****/

              while(rs.next()){
               curr_err = true;
// ������� ����� ������� ����������
                for(int i=0;i<groups.size();i++){
	          AbiturientBean curr_grp = new AbiturientBean();
                  curr_grp = (AbiturientBean)(groups.get(i));
// ���� � ������� ������ ������������ ������ ��������� ���������, � 
// ��� ���������� ������ = ���� ���������� �����������, �� ����� ��������� ����������� � ������
                  if(curr_grp.getAmount() < abit_Spec.getMaxCountAbiturients()) {
                if(abit_Spec.getSpecial2().equals("och")) {
// ������
                  if(StringUtil.toInt(""+curr_grp.getKodFakulteta(),0) == rs.getInt(2)) {
// �������������� �� �����������
/******* �������� ������������ ������ - ������������ ����������� ********/
                      stmt_b = conn.prepareStatement("SELECT * FROM Abiturient a,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodVuza LIKE '"+session.getAttribute("kVuza")+"' AND a.KodGruppy LIKE '"+curr_grp.getKodGruppy()+"' AND a.KodSpetsialnosti IN(NULL"+spec_set.get(subset)+"NULL)");
                      rs_b = stmt_b.executeQuery();
//���� � ������ ����������� ����� ������ ����� ��������������, �� �������� ��������� ������.
                      if(!rs_b.next() && curr_grp.getAmount()!=0) continue;
                      curr_err = false;
                      stmt_a = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=? WHERE KodAbiturienta LIKE ?");
                      stmt_a.setObject(1,curr_grp.getKodGruppy(),Types.INTEGER);
                      stmt_a.setObject(2,new Integer(rs.getString(1)),Types.INTEGER);
                      stmt_a.executeUpdate();
                      curr_grp.setAmount(curr_grp.getAmount()+1);
                      groups.remove(i);
                      groups.add(i,curr_grp);
                      total_added++;
                      break;
                  }
                } 
                if(StringUtil.toInt(""+curr_grp.getKodSpetsialnosti(),0) == -1)
                      curr_grp.setKodSpetsialnosti(new Integer(rs.getString(3)));

                if(StringUtil.toInt(""+curr_grp.getKodSpetsialnosti(),0) == rs.getInt(3)){
// ��������, ������������� �� ��������������
/******* �������� ������������ ������ - ������������ ����������� ********/
                      stmt_b = conn.prepareStatement("SELECT * FROM Abiturient a,Gruppy g WHERE a.KodGruppy=g.KodGruppy AND a.KodVuza LIKE '"+session.getAttribute("kVuza")+"' AND a.KodGruppy LIKE '"+curr_grp.getKodGruppy()+"' AND a.KodSpetsialnosti IN(NULL"+spec_set.get(subset)+"NULL)");
                      rs_b = stmt_b.executeQuery();
//���� � ������ ����������� ����� ������ ����� ��������������, �� �������� ��������� ������.
                      if(!rs_b.next() && curr_grp.getAmount()!=0) continue;
                      curr_err = false;
                      stmt_a = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=? WHERE KodAbiturienta LIKE ?");
                      stmt_a.setObject(1,curr_grp.getKodGruppy(),Types.INTEGER);
                      stmt_a.setObject(2,new Integer(rs.getString(1)),Types.INTEGER);
                      stmt_a.executeUpdate();
                      curr_grp.setAmount(curr_grp.getAmount()+1);
                      groups.remove(i);
                      groups.add(i,curr_grp);
                      total_added++;
                      break;
                    }
              }
             }
              if(curr_err){
                 no_err = false;
                 stmt_a = conn.prepareStatement("SELECT Familija,Imja,Otchestvo,AbbreviaturaFakulteta,NomerLichnogoDela FROM Abiturient a, Fakultety f, Spetsialnosti s WHERE a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta=f.KodFakulteta AND KodAbiturienta LIKE ?");
                 stmt_a.setObject(1,new Integer(rs.getString(1)),Types.INTEGER);
                 rs_a = stmt_a.executeQuery();
                 if(rs_a.next()){
                   MessageBean mesg = new MessageBean();
                   mesg.setStatus("������!");
                   mesg.setMessage("����. "+rs_a.getString(1)+" "+rs_a.getString(2).substring(0,1).toUpperCase()+"."+rs_a.getString(3).substring(0,1).toUpperCase()+". ("+rs_a.getString(5)+") �� �����. � ��. "+rs_a.getString(4).toUpperCase());
                   fgr_msgs.add(mesg);
                 }
               }
            }
}//���� �������� ��������������

// ���������� ������
             if(no_err) { 
               MessageBean msg1 = new MessageBean();
               msg1.setMessage("��������! ������ ������� ������������.");
               fgr_msgs.add(msg1);
             } else{
               MessageBean msg0 = new MessageBean();
               msg0.setMessage("---------------------------------------------------------------");
               fgr_msgs.add(msg0);
               MessageBean msg1 = new MessageBean();
               msg1.setMessage("��������! ��� ����������������� ������������ �� ������� �����.");
               fgr_msgs.add(msg1);
               MessageBean msg2 = new MessageBean();
               msg2.setMessage("�������� ������ ����� ��� ��������� ���������� ������������ � ������!");
               fgr_msgs.add(msg2);
             }
             MessageBean msg2 = new MessageBean();
             msg2.setMessage("--------------------------------------------------");
             fgr_msgs.add(msg2);
             MessageBean msg3 = new MessageBean();
             msg3.setMessage("����� ���� ������������: "+total_added+" ������������");
             fgr_msgs.add(msg3);

// ���������� ��������� ��������������� �����
             for(int i=0;i<groups.size();i++){
                AbiturientBean curr_grp = new AbiturientBean();
                curr_grp = (AbiturientBean)(groups.get(i));
                if(curr_grp.getAmount() >= abit_Spec.getMaxCountAbiturients()){
                  stmt = conn.prepareStatement("UPDATE Gruppy SET Locked=1 WHERE KodGruppy LIKE ?");
                  stmt.setObject(1,curr_grp.getKodGruppy(),Types.INTEGER);
                  stmt.executeUpdate();
                }
             }
             form.setAction(us.getClientIntName("results","act-auto-crt"));

/**********************************************/
/***********  ��������������� �����  **********/
/**********************************************/
      } else if ( form.getAction().equals("main") && abit_Spec.getSpecial1().equals("reset")) {
              if(StringUtil.toInt(""+abit_Spec.getKodFakulteta(),0) == -1){
//--��� ����������--
                if(abit_Spec.getNomerPlatnogoDogovora().equals("yes")) {
// ����������� + ���_������
                  if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                  } else {
// ����������� +> ��������_�����
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                    stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                  }
                }
                else if(abit_Spec.getNomerPlatnogoDogovora().equals("no")) {
// ���������� + ���_������
                  if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NULL");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NULL");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                  } else {
// ���������� +> ��������_�����
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND NomerPlatnogoDogovora IS NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                    stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                  }
                }
                else {
// ����������� � ���������� + ���_������
                  if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                  } else{
// ����������� � ���������� +> ��������_�����
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT s.KodSpetsialnosti FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND s.KodFakulteta LIKE '%' AND f.KodVuza LIKE ?) AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                    stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                  }
                }
                  stmt.executeUpdate();

//������������� ����� ���� + ��� ������
                  if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {

                    stmt = conn.prepareStatement("UPDATE Gruppy SET Locked=0 WHERE KodFakulteta IN(SELECT KodFakulteta FROM Fakultety WHERE KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                  } else {
//������������� ����� ���� +> ��������_�����
                    stmt = conn.prepareStatement("UPDATE Gruppy SET Locked=0 WHERE KodFakulteta IN(SELECT KodFakulteta FROM Fakultety WHERE KodVuza LIKE ?) AND Potok LIKE ?");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,abit_Spec.getNomerPotoka(),Types.INTEGER);
                  }
                    stmt.executeUpdate();
              }
              else{
//--��������� ���������--
                if(abit_Spec.getNomerPlatnogoDogovora().equals("yes")) {
// ����������� + ��� ������
                  if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                  } else {
// ����������� +> ��������_�����
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NOT NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                    stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                    stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                  }
                }
                else if(abit_Spec.getNomerPlatnogoDogovora().equals("no")) {
// ���������� + ��� ������
                  if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NULL");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NULL");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                  } else {
// ���������� +> ��������_�����
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND NomerPlatnogoDogovora IS NULL AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                    stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                    stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                  }
                }
                else {
// ����������� � ���������� + ��� ������
                  if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                  } else {
// ����������� � ���������� +> ��������_�����
// �����
                  if(abit_Spec.getSpecial2().equals("och"))
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('�','�')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
// �������
                  else
                    stmt = conn.prepareStatement("UPDATE Abiturient SET KodGruppy=1 WHERE KodGruppy IN (SELECT KodGruppy FROM Gruppy WHERE Dogovornaja IN('��','��')) AND KodVuza LIKE ? AND KodSpetsialnosti IN(SELECT KodSpetsialnosti FROM Spetsialnosti WHERE KodFakulteta LIKE ?) AND KodGruppy IN (SELECT g.KodGruppy FROM Gruppy g,Fakultety f WHERE g.KodFakulteta=f.KodFakulteta AND g.Potok LIKE ? AND f.KodVuza LIKE ?)");
                    stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
                    stmt.setObject(2,abit_Spec.getKodFakulteta(),Types.INTEGER);
                    stmt.setObject(3,abit_Spec.getNomerPotoka(),Types.INTEGER);
                    stmt.setObject(4,session.getAttribute("kVuza"),Types.INTEGER);
                  }
                }
                  stmt.executeUpdate();

//������������� ����� ���������� + ��� ������
                if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {

                  stmt = conn.prepareStatement("UPDATE Gruppy SET Locked=0 WHERE KodFakulteta LIKE ?");
                  stmt.setObject(1,abit_Spec.getKodFakulteta(),Types.INTEGER);
                  stmt.executeUpdate();
                } else {
//������������� ����� ���������� +> ��������_�����
                  stmt = conn.prepareStatement("UPDATE Gruppy SET Locked=0 WHERE KodFakulteta LIKE ? AND Potok LIKE ?");
                  stmt.setObject(1,abit_Spec.getKodFakulteta(),Types.INTEGER);
                  stmt.setObject(2,abit_Spec.getNomerPotoka(),Types.INTEGER);
                  stmt.executeUpdate();
                }
              }

// ���������� ������
             stmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta From Fakultety WHERE KodFakulteta LIKE ?");
             stmt.setObject(1,abit_Spec.getKodFakulteta(),Types.INTEGER);
             rs = stmt.executeQuery();
             MessageBean msg_1 = new MessageBean();
             if(rs.next()){
               if(abit_Spec.getNomerPlatnogoDogovora().equals("no")) {
                if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
                  if(abit_Spec.getSpecial2().equals("och"))
                    msg_1.setMessage("��������! ��� ����� ��������� ������ "+rs.getString(1).toUpperCase()+" �� ���� ������� ������� ��������������.");
                  else
                    msg_1.setMessage("��������! ��� ������� ��������� ������ "+rs.getString(1).toUpperCase()+" �� ���� ������� ������� ��������������.");
                } else {
                  if(abit_Spec.getSpecial2().equals("och"))
                    msg_1.setMessage("��������! ��� ����� ��������� ������ "+rs.getString(1).toUpperCase()+" "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                  else
                    msg_1.setMessage("��������! ��� ������� ��������� ������ "+rs.getString(1).toUpperCase()+" "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                }
               } else if(abit_Spec.getNomerPlatnogoDogovora().equals("yes")) {
                        if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
                  if(abit_Spec.getSpecial2().equals("och"))
                          msg_1.setMessage("��������! ��� ����� ����������� ������ "+rs.getString(1).toUpperCase()+" �� ���� ������� ������� ��������������.");
                  else
                          msg_1.setMessage("��������! ��� ������� ����������� ������ "+rs.getString(1).toUpperCase()+" �� ���� ������� ������� ��������������.");
                        } else {
                  if(abit_Spec.getSpecial2().equals("och"))
                          msg_1.setMessage("��������! ��� ����� ����������� ������ "+rs.getString(1).toUpperCase()+" "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                  else
                          msg_1.setMessage("��������! ��� ������� ����������� ������ "+rs.getString(1).toUpperCase()+" "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                        }
               } else {
                       if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
                  if(abit_Spec.getSpecial2().equals("och"))
                         msg_1.setMessage("��������! ��� ����� ������ "+rs.getString(1).toUpperCase()+" �� ���� ������� ������� ��������������.");
                  else
                         msg_1.setMessage("��������! ��� ������� ������ "+rs.getString(1).toUpperCase()+" �� ���� ������� ������� ��������������.");
                       } else {
                  if(abit_Spec.getSpecial2().equals("och"))
                         msg_1.setMessage("��������! ��� ����� ������ "+rs.getString(1).toUpperCase()+" "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                  else
                         msg_1.setMessage("��������! ��� ������� ������ "+rs.getString(1).toUpperCase()+" "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                       }
               }
             } else{
               if(abit_Spec.getNomerPlatnogoDogovora().equals("no")) {
                 if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
                  if(abit_Spec.getSpecial2().equals("och"))
                   msg_1.setMessage("��������! ��� ����� ��������� ������ ���� �� ���� ������� ������� ��������������.");
                  else
                   msg_1.setMessage("��������! ��� ������� ��������� ������ ���� �� ���� ������� ������� ��������������.");
                 } else {
                  if(abit_Spec.getSpecial2().equals("och"))
                   msg_1.setMessage("��������! ��� ����� ��������� ������ ���� "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                  else
                   msg_1.setMessage("��������! ��� ������� ��������� ������ ���� "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                 }
               } else if(abit_Spec.getNomerPlatnogoDogovora().equals("yes")) {
                        if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
                  if(abit_Spec.getSpecial2().equals("och"))
                          msg_1.setMessage("��������! ��� ����� ����������� ������ ���� �� ���� ������� ������� ��������������.");
                  else
                          msg_1.setMessage("��������! ��� ������� ����������� ������ ���� �� ���� ������� ������� ��������������.");
                        } else {
                  if(abit_Spec.getSpecial2().equals("och"))
                          msg_1.setMessage("��������! ��� ����� ����������� ������ ���� "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                  else
                          msg_1.setMessage("��������! ��� ������� ����������� ������ ���� "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                        }
               } else {
                       if(StringUtil.toInt(""+abit_Spec.getNomerPotoka(),0) == -2) {
                  if(abit_Spec.getSpecial2().equals("och"))
                         msg_1.setMessage("��������! ��� ����� ������ ���� �� ���� ������� ������� ��������������.");
                  else
                         msg_1.setMessage("��������! ��� ������� ������ ���� �� ���� ������� ������� ��������������.");
                       } else {
                  if(abit_Spec.getSpecial2().equals("och"))
                         msg_1.setMessage("��������! ��� ����� ������ ���� "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                  else
                         msg_1.setMessage("��������! ��� ������� ������ ���� "+abit_Spec.getNomerPotoka()+"-�� ������ ������� ��������������.");
                       }
               }
               }
             fgr_msgs.add(msg_1);

              form.setAction(us.getClientIntName("results","view2"));
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
          request.setAttribute("abit_Spec", abit_Spec);
          request.setAttribute("fgr_msgs", fgr_msgs);
          request.setAttribute("groups", groups);
          request.setAttribute("groups_bud", groups_bud);
          request.setAttribute("groups_kon", groups_kon);
          request.setAttribute("groups_z_bud", groups_z_bud);
          request.setAttribute("groups_z_kon", groups_z_kon);
          request.setAttribute("fakults", fakults);
          request.setAttribute("abits_Spec", abits_Spec);
          request.setAttribute("abit_Spec_S1", abit_Spec_S1);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(fgruppy_f) return mapping.findForward("fgruppy_f");
        return mapping.findForward("success");
    }
}