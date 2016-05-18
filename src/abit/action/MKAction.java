package abit.action;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
import abit.util.*;

import java.util.Date;
import java.io.*;
import java.math.*;

import abit.sql.*; 


public class MKAction extends Action{

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   

        HttpSession          session            = request.getSession();
        Connection           conn               = null;
        PreparedStatement    pstmt              = null;
        Statement            stmt               = null;
        Statement            stmt2              = null;
        Statement            stmt3              = null;
        Statement stmt9=null;
        PreparedStatement stmt10=null;
        PreparedStatement stmt11=null;
        ResultSet rs9=null;
        ResultSet rs10=null;
        ResultSet rs11=null;
        ResultSet            rs                 = null;
        ResultSet            rs2                = null;
        ResultSet            rs3                = null;
        ActionErrors         errors             = new ActionErrors();
        ActionError          msg                = null;
        ListsDecForm         form               = (ListsDecForm) actionForm;
        AbiturientBean       abit_SD            = form.getBean(request, errors);
        boolean              lists_dec_ege_f    = false;
        boolean              error              = false;
        ActionForward        f                  = null;
        ArrayList            abits_SD           = new ArrayList();
        ArrayList            abit_SD_S1         = new ArrayList();
        ArrayList            abit_SD_S2         = new ArrayList();
        String               AS                 = new String();           // ������������ �������������
        String               SS                 = new String();           // ����
        String               NS                 = new String();           // �������� �������������
        String               PP                 = new String();           // ���� ������
        String               TP1                = new String();           // ���� �������� ������ 1
        String               TP2                = new String();           // ���� �������� ������ 2
        String               AF                 = new String();           // ������������ ����������
        StringBuffer         excludeList        = new StringBuffer("-1");
        StringBuffer         query              = new StringBuffer();
        StringBuffer		 query1  			= new StringBuffer();
        StringBuffer		 query2  			= new StringBuffer();
        int                  kAbit              = -1;
       float                  summa              = 0;
        int                  oldBallAbt         = -1;
        boolean              only_one_run       = true;
        boolean              header             = false;
        boolean              primechanie        = false;
        int                  nomer              = 1;
        int                  count_predm        = 4; // ������ ���������� �������
        int ns=0;
        int kl=0;
        String dt=new String();
        dt=StringUtil.CurrDate(".");
        //vremennie dannie
        String F=new String();
        String I=new String();
        String O=new String();
        String ko=new String();
        String op=new String();
        String ind=new String();
        String spis=new String();
        int N=0;
        int summ=0;
        String Shifr=new String();
        int pr1=0;
        int pr2=0;
        int pr3=0;
        int pr4=0;
        int pr5=0;
        int pr6=0;
        int pr7=0;
        
        String z=null;
        int lgn=0;
        int total_lgn=0;
        int idfak=0;
        int num=0;
        int summt=0;
        BigDecimal bd;
        String KF=new String();
        
        

        UserBean             user               = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "mkAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "mkForm", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/
          
          pstmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE kodfakulteta=35 AND KodVuza LIKE ? ORDER BY 2 ASC");
          pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
          rs = pstmt.executeQuery();
          while (rs.next()) {
            AbiturientBean abit_TMP = new AbiturientBean();
            abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
            abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
            abit_SD_S1.add(abit_TMP);
          }
 
      if ( form.getAction() == null ) {

           form.setAction(us.getClientIntName("view","init"));

      } else if ( form.getAction().equals("report")) {
    	  stmt10 = conn.prepareStatement("DELETE FROM Forsen");
    	  stmt10.executeUpdate();

/************************************************/
/***** ����������� ����� � �������� ������� *****/
/************************************************/


	KF=abit_SD.getKodFakulteta().toString();

// �������� ���������

  abit_SD.setSpecial2("-");

  abit_SD.setSpecial3("-");

  pstmt = conn.prepareStatement("SELECT DISTINCT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ?");
  pstmt.setObject(1,KF,Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()) {
    AF = rs.getString(1).toUpperCase();
  }

  
  String name = "������ �������� ��������� � ��������������� �������(�����.) "+AF;

  String file_con = "lists_"+StringUtil.toEng(AF)+"_predv_MK";


  session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

  String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();
 
  BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 
//\\paperw11906\\paperh16838
  report.write("{\\rtf1\\ansi\n");
//  report.write("\\landscape\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\n");
  report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace100\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl\\trhdr\\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
  pstmt = conn.prepareStatement("SELECT NazvanieVuza,PostAdresVuza FROM NazvanieVuza WHERE KodVuza LIKE ?");
  pstmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
  rs = pstmt.executeQuery();
  if(rs.next()) 
    report.write("\\fs40 \\qc "+rs.getString(1)+"\n");

/**********************************************************/
/**                                                      **/
/**  ���������� ��� ������������� ���������� ����������  **/
/**                                                      **/
/**********************************************************/

  stmt2 = conn.createStatement();

  query = new StringBuffer("SELECT DISTINCT f.AbbreviaturaFakulteta,s.ShifrSpetsialnosti,s.Abbreviatura,s.NazvanieSpetsialnosti,s.KodSpetsialnosti,s.PlanPriema,s.TselPr_PGU,s.TselPr_ROS,s.PlanPriemaDog,f.kodFakulteta FROM Fakultety f,Spetsialnosti s, Konkurs kon, Abiturient a WHERE f.KodFakulteta=s.KodFakulteta AND a.KodAbiturienta=kon.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodFakulteta LIKE '"+KF+"' ");
  
    if(abit_SD.getPriznakSortirovki().equals("budgetniki"))
      query.append("AND (kon.Bud LIKE '�')");
//      query.append("AND fo.Sokr IN ('�����') AND (kon.Bud LIKE '�')");
//      query.append("AND fo.Sokr IN ('�����') AND (a.NomerPlatnogoDogovora IS NULL)");
//    else if(abit_SD.getPriznakSortirovki().equals("kontraktniki"))
    else
      query.append("AND (kon.Dog LIKE '�')");
//      query.append("AND kon.Forma_Ob IN ('�') AND (kon.Dog LIKE '�')");
//      query.append("AND fo.Sokr IN ('�����') AND (kon.Dog LIKE '�')");
//      query.append("AND fo.Sokr IN ('�����') AND (a.NomerPlatnogoDogovora IS NOT NULL)");

//    else if(abit_SD.getPriznakSortirovki().equals("z_budgetniki"))
//      query.append("AND kon.Forma_Ob IN ('�','�','�') AND (kon.Bud LIKE '�')");
//      query.append("AND fo.Sokr IN ('�������','������-���') AND (kon.Bud LIKE '�')");
//      query.append("AND fo.Sokr IN ('�������','������-���') AND (a.NomerPlatnogoDogovora IS NULL)");

//    else 
//      query.append("AND kon.Forma_Ob IN ('�','�','�') AND (kon.Dog LIKE '�')");
//      query.append("AND fo.Sokr IN ('�������','������-���') AND (kon.Dog LIKE '�')");
//      query.append("AND fo.Sokr IN ('�������','������-���') AND (a.NomerPlatnogoDogovora IS NOT NULL)");
    query.append(" ORDER BY f.KodFakulteta,s.KodSpetsialnosti");
  rs2 = stmt2.executeQuery(query.toString());
  while(rs2.next()) {

// ��� ������ ������������� ��������� ���� � ���� ������ ����������

    nomer = 0;
    excludeList = new StringBuffer("-1");

    SS = rs2.getString(2);
    AS = rs2.getString(3);
    NS = rs2.getString(4).toUpperCase();
    if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
    if(StringUtil.toInt(rs2.getString(6), 0) == 0) PP = "0";
    else PP  = "" + StringUtil.toInt(rs2.getString(6), 0);
    }else{
    	if(StringUtil.toInt(rs2.getString(9), 0) == 0) PP = "0";
        else PP  = "" + StringUtil.toInt(rs2.getString(9), 0);
    	total_lgn=rs2.getInt(9);
    }
    TP1 = "" + StringUtil.toInt(rs2.getString(7), 0);
    TP2 = "" + StringUtil.toInt(rs2.getString(8), 0);

    report.write("\\fs24\\par\\par\n");
    report.write("\\fs26\\b0\\qc{������ ������������ }\\b1{"+rs2.getString(1)+"}\\b0\\par{����������� �� ������������� (�����������):}\\par\\fs24\\b1{"+SS+" }\\'ab{"+NS+"}\\'bb\\qc{ ("+AS+")}\\b0\\par\n");
    report.write("\\par\\fs26\\qc\\b0{������ ������������ ��: } "+dt+"\\b0\\par\n");
    if(StringUtil.toInt(PP, 0) != 0)
      report.write("\\par\\fs26\\qc\\b1{���� �����: } "+PP+"{  ��������: } "+PP+"\\b0\\par\n");
    else
      report.write("\\par");



/******************************************************/
/**********  ������� �� ������� ������������ **********/
/******************************************************/


// �� ����� ��������� ������

    header = false;

    only_one_run = true;

    boolean evidence_exist = false;
    
    oldBallAbt = -1;

       query1 = new StringBuffer("SELECT DISTINCT a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.prof,kon.olimp FROM Abiturient a, Spetsialnosti s, lgoty l, Konkurs kon, oa oa WHERE a.kodabiturienta=oa.kodabiturienta AND kon.op=l.kodlgot AND kon.KodAbiturienta=a.KodAbiturienta AND kon.KodSpetsialnosti=s.KodSpetsialnosti AND a.DokumentyHranjatsja LIKE '�' AND kon.KodSpetsialnosti LIKE '"+rs2.getString(5)+"' AND a.Prinjat not in ('1','2','3','4','5','7','�') AND a.KodAbiturienta NOT IN ("+excludeList.toString()+") ");

    if(abit_SD.getPriznakSortirovki().equals("budgetniki")){

    query1.append("AND (kon.Bud LIKE '�')");

    }else{
      query1.append("AND (kon.Dog LIKE '�')");
    }
  
query1.append(" GROUP BY a.KodAbiturienta,kon.nomerlichnogodela,a.Familija,a.Imja,a.Otchestvo,a.TipDokSredObraz,l.ShifrLgot,kon.PR,a.viddoksredobraz,kon.prof,kon.olimp ORDER BY a.Familija,a.Imja,a.Otchestvo,l.ShifrLgot,a.KodAbiturienta");
   
//System.out.println(query);
    
    stmt9 = conn.createStatement();

    rs9 = stmt9.executeQuery(query1.toString());
    while(rs9.next()) {
    	summa=0;
    	num=0;
    	stmt11 = conn.prepareStatement("SELECT oa.otsenkaatt from oa oa where oa.kodabiturienta like '"+rs9.getString(1)+"' and oa.otsenkaatt not like '0'");
        rs11=stmt11.executeQuery();

        while(rs11.next()){
        	summa=summa+rs11.getInt(1);
        	num++;
        	System.out.print(summa+" ");
        }
        System.out.println("kab "+rs9.getString(1));
        if(summa!=0){
    	summa=summa/num;
   /* 	bd = new BigDecimal(Float.toString(summa));
    	BigDecimal denominator = new BigDecimal(Integer.toString(num));
    	BigDecimal result = bd.divide(denominator,2,RoundingMode.DOWN);*/
    	
    	
            bd = new BigDecimal(Float.toString(summa));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    	}else{
    		bd= new BigDecimal("0.00");
    	}
        
    	
    	stmt10 = conn.prepareStatement("INSERT INTO Forsen(N,Shifr,F,I,O,ko,op,ka,vd,ind,pp) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
    	N++;
        stmt10.setObject(1, N,Types.INTEGER);
        stmt10.setObject(2, rs9.getString(2),Types.VARCHAR);
        stmt10.setObject(3, rs9.getString(3),Types.VARCHAR);
        stmt10.setObject(4, rs9.getString(4),Types.VARCHAR);
        stmt10.setObject(5, rs9.getString(5),Types.VARCHAR);
        stmt10.setObject(6, rs9.getString(6),Types.VARCHAR);
        stmt10.setObject(7, rs9.getString(7),Types.VARCHAR);
        
        stmt10.setObject(8, new Integer(rs9.getString(1)),Types.INTEGER);
        stmt10.setObject(9, rs9.getString(9),Types.VARCHAR);
        stmt10.setObject(10, rs9.getString(11),Types.VARCHAR);
        stmt10.setObject(11, bd,Types.VARCHAR);
        stmt10.executeUpdate();
        

        
    }
    
    
    
    
    
    
    
    lgn=0;
    
      if(header == false) {
        header = true;
        lgn++;
        report.write("\\pard\\par\n");
        if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
        	report.write("\\b1\\ql\\fs28{�������� ���, ����������� �� ������ �������� �� ����� � ������ ����������� ���� ������}\\b0\\par\\par\n"); 
        }else{
        	report.write("\\b1\\ql\\fs28{�������� ���, �������� ��������� �� �������� �� �������� ������� ��������������� �����}\\b0\\par\\par\n");
        }
        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");//shifr
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8000\n");//fio
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9000\n");//ko
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");//sum
        report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");//pp
   //     report.write("\\clvmgf\\clvertalc \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//pp


        report.write("\\intbl\\qc{�}\\cell\n");
        report.write("\\intbl{����}\\cell\n");
        report.write("\\intbl{������� �.�.}\\cell\n");
        report.write("\\intbl{�����-��������}\\cell\n");
        report.write("\\intbl{�������}\\par{���-��}\\par{������}\\cell\n");
        report.write("\\intbl{�����}\\par{��}\\par{���.}\\cell\n");


        report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvmgf\\clvertalc  \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");//shifr
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8000\n");//fio
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9000\n");//ko
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");//sum
        report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");//pp
   //     report.write("\\clvmgf\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//pp
        report.write("\\intbl\\row\n");
        
        report.write("\\b0\\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
     //   report.write("\\fs22\\b1 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n");//Nomer
   //     report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx500\n"); //Nomer
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx4000\n");//shifr
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx8000\n");//fio
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx9000\n");//ko
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx10000\n");//sum
        report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx11000\n");//pp
     //   report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx12000\n");//pp 
      }


      stmt10 = conn.prepareStatement("SELECT Shifr,F,I,O,ko,pp,ind FROM Forsen ORDER BY pp DESC");
      rs10=stmt10.executeQuery();

      while(rs10.next()){

    	  if(lgn<total_lgn){
    		  nomer++;
    	  }

report.write("\\fs24\\intbl\\qc "+(nomer)+"\\cell\n");
report.write("\\intbl\\qc "+rs10.getString(1)+"\\cell\n"); // NLD   
report.write("\\intbl\\ql "+rs10.getString(2)+" "+rs10.getString(3).substring(0,1)+"."+rs10.getString(4).substring(0,1)+"."+"\\cell\n"); // FAMIL I.O.
report.write("\\intbl\\qc "+rs10.getString(5)+"\\cell\n"); //ko
report.write("\\intbl\\qc "+rs10.getString(6)+"\\cell\n"); // sum
report.write("\\intbl\\qc "+rs10.getString(7)+"\\cell\n"); //pp
report.write("\\intbl\\row\n");

 
//��������� ��� ����������� � ������ ���������� ��� ����, ����� �� ��������� � ������ ������ ���� ���


    }
     /* if(abit_SD.getPriznakSortirovki().equals("budgetniki")){
    	  while(nomer<StringUtil.toInt(PP, 0)){

              report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // �
              report.write("\\intbl\\qc\\cell\n");  
              report.write("\\intbl\\ql{��������}\\cell\n"); // NLD
              report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
                             // FAMIL I.O.
              report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
              report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA                        // SUMMA Ege
              report.write("\\intbl\\row\n");
            }
      }else{
    	  while(nomer<total_lgn){

              report.write("\\fs24\\intbl\\qc "+(++nomer)+"\\cell\n");      // �
              report.write("\\intbl\\qc\\cell\n");  
              report.write("\\intbl\\ql{��������}\\cell\n"); // NLD
              report.write("\\intbl\\qc\\cell\n");                          // SHIFR LICHNOGO DELA
                             // FAMIL I.O.
              report.write("\\intbl\\qc\\cell\n");                          // ATTESTAT (KOPIJA)
              report.write("\\intbl\\qc\\cell\n");                          // KOD TSELEVOGO PRIEMA                        // SUMMA Ege
              report.write("\\intbl\\row\n");
            }
      }*/

      
      
      

          if(header) report.write("\\pard\\par\n");
  stmt10 = conn.prepareStatement("DELETE FROM Forsen WHERE 1=1");
  stmt10.executeUpdate();

  
    
    
    
    
    
    
    
    
    
    
    
    if(primechanie) report.write("\\par\\fs24\\ql\\tab\\tab{* - ���� ���������� �������� �.10.4. ������ ����� ���}\\par\n");


// ������� �������

    report.write("\\pard\\par\\par\\par");

    report.write("\\b0\\trowd\\trqc\\trgaph108\\trrh280\\trleft36\n");
    report.write("\\clvertalc\\cellx4000\n");
    report.write("\\clvertalc\\cellx5700\n");
    report.write("\\clvertalc\\cellx8200\n");

    report.write("\\intbl\\qr{������������ �������� ��������: }\\cell\n");

    pstmt = conn.prepareStatement("SELECT Facsimile FROM NazvanieVuza WHERE KodVuza LIKE ?");
    pstmt.setObject(1,session.getAttribute("kVuza"),Types.VARCHAR);
    rs = pstmt.executeQuery();
    if(rs.next()) {
      report.write("\\intbl\\qc{"+rs.getString(1)+"}\\cell\n");
    } else {
      report.write("\\intbl\\cell\n");
    }

    String rektor = new String();
    pstmt = conn.prepareStatement("SELECT Doljnost, Fio FROM Otvetstvennyelitsa WHERE Doljnost LIKE '%������%'");
    rs = pstmt.executeQuery();
    if(rs.next()) {
      report.write("\\intbl\\ql{  / "+rs.getString(2)+" /}\\cell\n");
    } else {
      report.write("\\intbl\\cell\n");
    }

    report.write("\\intbl\\row\n");
    report.write("\\pard\\par\\i1");

    report.write("\\par\\fs20\\ql\\tab\\tab\\b1{����������.}\\b0\n");
    report.write("\\par\\fs20\\ql\\tab\\tab{ � ������ ������� ���������� ��������� ������ ��� ������ ������ ��������}\n");
    report.write("\\par\\fs20\\ql\\tab\\tab{����������� ����, ������� ����� ������� ���� �� ����������� ��������.}\\par\n");


    report.write("\\par\\i0");

    report.write("\\page");
    stmt10 = conn.prepareStatement("DELETE FROM Forsen");
	  stmt10.executeUpdate();

   } // ������� �������������� ���������� ����������

  report.write("}"); 
  report.close();
  form.setAction(us.getClientIntName("new_rep","crt"));
  return mapping.findForward("rep_brw");

  }

 }      catch ( SQLException e ) {
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
          if ( rs2 != null ) {
               try {
                     rs2.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( rs3 != null ) {
               try {
                     rs3.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( stmt2 != null ) {
               try {
                     stmt2.close();
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
        request.setAttribute("abits_SD", abits_SD);
        request.setAttribute("abit_SD_S1", abit_SD_S1);
        request.setAttribute("abit_SD_S2", abit_SD_S2);
      }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(lists_dec_ege_f) return mapping.findForward("lists_dec_ege_f");
        return mapping.findForward("success");
    }


}
