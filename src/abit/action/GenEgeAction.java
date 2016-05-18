package abit.action;

import java.io.IOException;
import java.io.PrintWriter;
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
import abit.util.*;
import abit.Constants;
import abit.sql.*; 

public class GenEgeAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession                  session        = request.getSession();
        Connection                   conn           = null;
        PreparedStatement            stmt           = null;
        PreparedStatement            stmt_a         = null;
        PreparedStatement            stmt_b         = null;
        PreparedStatement            stmt_c         = null;
        ResultSet                    rs             = null;
        ResultSet                    rs_a           = null;
        ResultSet                    rs_b           = null;
        ResultSet                    rs_c           = null;
        ActionErrors                 errors         = new ActionErrors();
        ActionError                  msg            = null;
        GenEgeForm                   form           = (GenEgeForm) actionForm;
        AbiturientBean               abit_ENS       = form.getBean(request, errors);
        boolean                      error          = false;
        ActionForward                f              = null;
        ArrayList                    fgr_msgs       = new ArrayList();
        ArrayList                    specs          = new ArrayList();
        ArrayList                    predms         = new ArrayList();
        ArrayList                    abit_ENS_S1    = new ArrayList();
        ArrayList                    abit_ENS_S2    = new ArrayList();
        ArrayList                    abit_ENS_S3    = new ArrayList();
        boolean                      priznak        = true;
        int                          number         = 0;
        int                          old_kodAb      = 0;
        String                       predmets       = new String();
        String                       codeLine       = new String();
        String                       specialQ       = new String();
        String                       kPredmeta      = new String();
        String                       oldKodeS       = new String();
        String                       oldKodeF       = new String();
        String                       oldNazv        = new String();
        UserBean                     user           = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "genEgeAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "genEgeForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if (form.getAction() == null) form.setAction(us.getClientIntName("translator","init"));

            if (form.getAction().equals("md_dl")) {

/**************************************************************************************************/
/***********************  ПОДГОТОВКА ДАННЫХ ДЛЯ ФОРМЫ ПЕРЕВОДА ОЦЕНОК ЕГЭ *************************/
/**************************************************************************************************/

            abit_ENS.setKodSpetsialnosti(new Integer((abit_ENS.getSpecial1()).substring((abit_ENS.getSpecial1()).indexOf("$")+1,(abit_ENS.getSpecial1()).indexOf("%"))));

// Количество баллов системы, в которые будут переводиться оценки ЕГЭ

            stmt = conn.prepareStatement("SELECT COUNT(KodIntervala)/2-1 FROM IntervalEge WHERE KodVuza LIKE ? AND KodSpetsialnosti LIKE ? GROUP BY KodPredmeta");
            stmt.setObject(1, session.getAttribute("kVuza"),Types.INTEGER);
            stmt.setObject(2, abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
            rs = stmt.executeQuery();
            if(rs.next()) abit_ENS.setSpecial22(new Integer(rs.getString(1)));
            else abit_ENS.setSpecial22(new Integer("0"));

// Предметы
            abit_ENS_S3.clear();
            stmt_a = conn.prepareStatement("SELECT DISTINCT np.KodPredmeta,np.Sokr FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodSpetsialnosti LIKE ? ORDER BY np.KodPredmeta");
            stmt_a.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
            rs_a = stmt_a.executeQuery();
            while (rs_a.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodPredmeta(new Integer(rs_a.getInt(1)));
              abit_TMP.setPredmet(rs_a.getString(2));
            
// Названия интервалов оценок
              ArrayList abit_ENS_S4 = new ArrayList();
              boolean intervals_exists = false;
              String query_int = new String();
              query_int += "SELECT DISTINCT Ball,KodIntervala,Value FROM IntervalEge WHERE KodVuza LIKE '";
              query_int += session.getAttribute("kVuza")+"' AND KodSpetsialnosti LIKE '";
              query_int += abit_ENS.getKodSpetsialnosti()+"' AND KodPredmeta LIKE '";
              query_int += rs_a.getString(1)+"' AND Tip LIKE '";
// Tip == 1, если бюджетники
              if(abit_ENS.getNomerPlatnogoDogovora().equals("no")) {
                query_int += "1' ORDER BY KodIntervala";
              }
// Tip == 2, если контрактники
              else {
                query_int += "2' ORDER BY KodIntervala";
              }
              stmt = conn.prepareStatement(query_int);
              rs = stmt.executeQuery();
              while(rs.next()) {
                intervals_exists = true;
                AbiturientBean interval = new AbiturientBean();
                interval.setSpecial6(rs.getString(1)); // Название интервала
                interval.setSpecial7(rs.getString(2)); // Идентификатор интервала
                interval.setSpecial8(rs.getString(3)); // Значение интервала
                abit_ENS_S4.add(interval);
              }
// Проверка: Если нет интервалов ЕГЭ, то создаем их по умолчанию ! от 0 до 100
if(!intervals_exists) {
  int kInt;
for(int ball=0;ball<=10;ball++){
   AbiturientBean interval = new AbiturientBean();
   stmt = conn.prepareStatement("SELECT MAX(KodIntervala) FROM IntervalEge");
   rs = stmt.executeQuery();
   if(rs.next()) kInt = rs.getInt(1)+1;
   else kInt = 1;

   interval.setSpecial7(""+kInt); // Идентификатор интервала

   query_int = "INSERT INTO IntervalEge(KodIntervala,KodSpetsialnosti,Tip,KodPredmeta,KodVuza,Value,Ball) VALUES(";

   query_int+= kInt+","+abit_ENS.getKodSpetsialnosti()+",";
// Tip == 1, если бюджетники
              if(abit_ENS.getNomerPlatnogoDogovora().equals("no")) {
                query_int += "1";
              }
// Tip == 2, если контрактники
              else {
                query_int += "2";
              }

   query_int+= ","+rs_a.getString(1)+","+session.getAttribute("kVuza")+",";

   if(ball != 10) {
     query_int+= "0";
     interval.setSpecial8("0"); // Значение интервала
   }
   else {
     query_int+= "100";
     interval.setSpecial8("100"); // Значение интервала
   }

   query_int+= ","+ball+")";
   interval.setSpecial6(""+ball); // Название интервала

   stmt = conn.prepareStatement(query_int);
   stmt.executeUpdate();

   abit_ENS_S4.add(interval);
 }
}


              AbiturientBean interval = new AbiturientBean();
              interval = (AbiturientBean)abit_ENS_S4.get(abit_ENS_S4.size()-1);
              interval.setSpecial8("100");
              abit_ENS_S4.add(interval);
              abit_ENS_S4.remove(abit_ENS_S4.size()-2);
              abit_TMP.setList(abit_ENS_S4);
              abit_ENS_S3.add(abit_TMP);

              request.setAttribute("abit_ENS_S4", abit_ENS_S4);
            }

// Название специальности

                 stmt = conn.prepareStatement("SELECT NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ? ");
                 stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
                 rs = stmt.executeQuery();
                 if(rs.next()) abit_ENS.setAbbreviatura(rs.getString(1).toUpperCase());

// Название факультета

                 stmt = conn.prepareStatement("SELECT AbbreviaturaFakulteta FROM Fakultety WHERE KodFakulteta LIKE ? ");
                 stmt.setObject(1,abit_ENS.getKodFakulteta(),Types.INTEGER);
                 rs = stmt.executeQuery();
                 if(rs.next()) abit_ENS.setAbbreviaturaFakulteta(rs.getString(1).toUpperCase());

// Количество найденных абитуриентов
                 int count=0;
                 String query2 = new String();
if((abit_ENS.getShifrMedali()).indexOf('н') != -1){
// Выбираем только отличников
                      query2 = "SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=o.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query2+= " AND m.ShifrMedali IN(" + StringUtil.PlaceUnaryComas("m.ShifrMedali",abit_ENS.getShifrMedali())+") ";
                      query2+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if((abit_ENS.getNomerPlatnogoDogovora()).equals("yes"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               else if((abit_ENS.getNomerPlatnogoDogovora()).equals("no"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query2+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
                      query2+= " AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti()+") ";
                      query2+= " GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora ORDER BY a.Familija,a.Imja,a.Otchestvo,zo.KodAbiturienta,zo.KodPredmeta";
} else {
// Выбираем абитуриентов без отличий и тех отличников, которые не подтвердили свои результаты по профилирующему экзамену на специальность
                      query2 = "SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=o.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query2+= " AND m.ShifrMedali IN(" + StringUtil.PlaceUnaryComas("m.ShifrMedali",abit_ENS.getShifrMedali())+") ";
                      query2+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if((abit_ENS.getNomerPlatnogoDogovora()).equals("yes"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               else if((abit_ENS.getNomerPlatnogoDogovora()).equals("no"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query2+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
                      query2+= " AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti()+") ";
                      query2+= " UNION SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o,EkzamenyNaSpetsialnosti ens WHERE ens.KodPredmeta=o.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query2+= " AND o.Otsenka NOT LIKE '10' AND o.From_Ege LIKE 'д' AND m.ShifrMedali NOT LIKE 'н' ";
                      query2+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if((abit_ENS.getNomerPlatnogoDogovora()).equals("yes"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query2+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
               else if((abit_ENS.getNomerPlatnogoDogovora()).equals("no"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NULL ";
                      query2+= " AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti()+") ";
                      query2+= " GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora ORDER BY a.Familija,a.Imja,a.Otchestvo,zo.KodAbiturienta,zo.KodPredmeta";
}
               stmt = conn.prepareStatement(query2);
               rs = stmt.executeQuery();
               int oldKodAb = -1;
          while(rs.next()) {
             if(oldKodAb!=rs.getInt(3)) {
                count++;
                oldKodAb=rs.getInt(3);
             }
          }
          abit_ENS.setNumber(""+count);

        }else if (form.getAction().equals("translator")) {
/**************************************************************************************************/
/**************  ПОДГОТОВКА ДАННЫХ ДЛЯ ФОРМЫ НАСТРОЕК ПАРАМЕТРОВ ПЕРЕВОДА ОЦЕНОК ЕГЭ ***************/
/**************************************************************************************************/

            form.setAction(us.getClientIntName("translate","view-form"));

            stmt = conn.prepareStatement("SELECT DISTINCT KodFakulteta,AbbreviaturaFakulteta FROM Fakultety WHERE KodVuza LIKE ? ORDER BY 2 ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next()) {
              AbiturientBean abit_TMP = new AbiturientBean();
              abit_TMP.setKodFakulteta(new Integer(rs.getString(1)));
              abit_TMP.setAbbreviaturaFakulteta(rs.getString(2));
              abit_ENS_S1.add(abit_TMP);
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
                  abit_ENS_S2.add(abit_TMP);
                  priznak = true;
                  kPredmeta = "";
              }
              kPredmeta += "%" + rs.getString(2);
            }
            AbiturientBean abit_TMP2 = new AbiturientBean();
            abit_TMP2.setSpecial1(oldKodFak +"$"+oldKode+ kPredmeta);
            abit_TMP2.setAbbreviatura(oldAbbr);
            abit_ENS_S2.add(abit_TMP2);

// Выборка кода медали и шифра для формы ввода
            String tmp="";
            stmt = conn.prepareStatement("SELECT ShifrMedali FROM Medali WHERE KodVuza LIKE ? ORDER BY KodMedali ASC");
            stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
            rs = stmt.executeQuery();
            while (rs.next())
              tmp += rs.getString(1) +",";
// В свойстве ShifrMedali будет храниться список сокращений для медалей
            if(tmp != null) abit_ENS.setShifrMedali(tmp.substring(0,tmp.length()-1));


/**************************************************************************************************/
/******************************  ВЫПОЛНЕНИЕ ПЕРЕВОДА ОЦЕНОК ЕГЭ ***********************************/
/**************************************************************************************************/

            } else if(form.getAction().equals("hand_mark")) {

/*****************************************************************************/
/*****************        ДЛЯ "РУЧНОГО" РЕДАКТИРОВАНИЯ      ******************/
/*****************************************************************************/

    String kodAb="0",kodPr="0";
    Enumeration paramNames = request.getParameterNames();
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();
      String paramValue[] = request.getParameterValues(paramName);
      if(paramName.indexOf("ots") != -1) {
        kodAb = new String(paramName.substring(3,paramName.indexOf("%")));
        kodPr = new String(paramName.substring(paramName.indexOf("%")+1));
// Обновление диапазона
        stmt = conn.prepareStatement("UPDATE Otsenki SET Otsenka='"+(paramValue[0]).trim()+"',From_Ege='д' WHERE KodPredmeta LIKE '"+kodPr+"' AND KodAbiturienta LIKE '"+kodAb+"'");
        stmt.executeUpdate();
      }
      if(paramName.indexOf("ege") != -1) {
        kodAb = new String(paramName.substring(3,paramName.indexOf("%")));
        kodPr = new String(paramName.substring(paramName.indexOf("%")+1));
// Обновление диапазона
        stmt = conn.prepareStatement("UPDATE ZajavlennyeShkolnyeOtsenki SET OtsenkaEge='"+(paramValue[0]).trim()+"' WHERE KodPredmeta LIKE '"+kodPr+"' AND KodAbiturienta LIKE '"+kodAb+"'");
        stmt.executeUpdate();
      }
    }

// Установка признака подтверждения медали тем медалистам, что набрали 10 баллов по профилирующему предмету специальности

        stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.PodtvMed FROM Abiturient a,Otsenki o,Spetsialnosti s,Medali m WHERE m.KodMedali=a.KodMedali AND a.KodAbiturienta=o.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodPredmeta=o.KodPredmeta AND o.Otsenka LIKE '"+Constants.maxExBall+"' AND m.ShifrMedali IN ('з','с') AND a.KodVuza LIKE ?");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()){
          stmt = conn.prepareStatement("UPDATE Abiturient SET PodtvMed='д' WHERE KodAbiturienta LIKE ?");
        stmt.setObject(1,rs.getString(1),Types.INTEGER);
          stmt.executeUpdate();
        }

    form.setAction(us.getClientIntName("results","act-hand-mark"));

            } else if(form.getAction().equals("mark")) {

/*****************************************************************************/
/****************        ОБНОВЛЕНИЕ ДИАПАЗОНОВ ОЦЕНОК ЕГЭ       **************/
/*****************************************************************************/

    form.setAction(us.getClientIntName("results","act-auto-mark"));

    String kodInt="0",kodPr="0";
    Enumeration paramNames = request.getParameterNames();
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();
      String paramValue[] = request.getParameterValues(paramName);
      if(paramName.indexOf("int") != -1) {
        kodInt = new String(paramName.substring(3,paramName.indexOf("%")));
        kodPr = new String(paramName.substring(paramName.indexOf("%")+1));
// Обновление диапазона
if(abit_ENS.getNomerPlatnogoDogovora().equals("no")){
        stmt = conn.prepareStatement("UPDATE IntervalEge SET Value='"+(paramValue[0]).trim()+"' WHERE KodPredmeta LIKE '"+kodPr+"' AND KodIntervala LIKE '"+kodInt+"' AND KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti()+"' AND KodVuza LIKE '"+session.getAttribute("kVuza")+"' AND Tip LIKE '1'");
}
else{
        stmt = conn.prepareStatement("UPDATE IntervalEge SET Value='"+(paramValue[0]).trim()+"' WHERE KodPredmeta LIKE '"+kodPr+"' AND KodIntervala LIKE '"+kodInt+"' AND KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti()+"' AND KodVuza LIKE '"+session.getAttribute("kVuza")+"' AND Tip LIKE '2'");
}

        stmt.executeUpdate();
      }
    }

/*****************************************************************************/
/****************        ОТБОР АБИТУРИЕНТОВ ДЛЯ ПЕРЕВОДА       ***************/
/*****************************************************************************/
               number = 0;
               old_kodAb = -1;
               String query = new String();

/*****************************************************************************************************************/
// Выбираем абитуриентов, удовлетворяющих заданным в форме настроек критериям, с ненулевой оценкой ЕГЭ, документы которых хранятся
/*****************************************************************************************************************/

if(abit_ENS.getShifrMedali().indexOf('н') != -1){
// Выбираем только отличников
                      query = "SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.KodSpetsialnosti FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o,EkzamenyNaSpetsialnosti ens WHERE o.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query+= " AND m.ShifrMedali IN(" + StringUtil.PlaceUnaryComas("m.ShifrMedali",abit_ENS.getShifrMedali())+") ";
                      query+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if(abit_ENS.getNomerPlatnogoDogovora().equals("yes"))
                      query+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
               else if(abit_ENS.getNomerPlatnogoDogovora().equals("no"))
                      query+= " AND a.NomerPlatnogoDogovora IS NULL ";
                      query+= " GROUP BY zo.KodAbiturienta,zo.OtsenkaEge,zo.KodPredmeta,a.KodSpetsialnosti ORDER BY zo.KodAbiturienta,zo.KodPredmeta";
} else {
// Выбираем абитуриентов без отличий и тех отличников, которые не подтвердили свои результаты по профилирующему экзамену на специальность
                      query = "SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.KodSpetsialnosti FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o,EkzamenyNaSpetsialnosti ens WHERE o.KodPredmeta=ens.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query+= " AND m.ShifrMedali IN(" + StringUtil.PlaceUnaryComas("m.ShifrMedali",abit_ENS.getShifrMedali())+") ";
                      query+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if(abit_ENS.getNomerPlatnogoDogovora().equals("yes"))
                      query+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
               else if(abit_ENS.getNomerPlatnogoDogovora().equals("no"))
                      query+= " AND a.NomerPlatnogoDogovora IS NULL ";
                      query+= " UNION SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.KodSpetsialnosti FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o WHERE o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query+= " AND o.Otsenka NOT LIKE '10' AND m.ShifrMedali NOT LIKE 'н' ";
                      query+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if(abit_ENS.getNomerPlatnogoDogovora().equals("yes"))
                      query+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
               else if(abit_ENS.getNomerPlatnogoDogovora().equals("no"))
                      query+= " AND a.NomerPlatnogoDogovora IS NULL ";
                      query+= " GROUP BY zo.KodAbiturienta,zo.OtsenkaEge,zo.KodPredmeta,a.KodSpetsialnosti ORDER BY zo.KodAbiturienta,zo.KodPredmeta";
}
               stmt = conn.prepareStatement(query);
               rs = stmt.executeQuery();
               while(rs.next()){
                 if(rs.getInt(3)!=old_kodAb) { 
                   number++; 
                   old_kodAb = rs.getInt(3);
                 }

if(request.getParameter("view") == null){
/*************************************************************************************************/
/************************** АВТОМАТИЧЕСКИЙ ПЕРЕВОД БАЛЛОВ ЕГЭ В ОЦЕНКИ ***************************/
/*******  Определяем, в какой диапазон входит оценка ЕГЭ и проставляем данному абитуриенту  ******/
/*******              экзаменационную оценку по соответствующему предмету                   ******/
/*************************************************************************************************/


// Проверка - задан ли диапазон оценок ЕГЭ, отличный от того, что стоит по умолчанию.
// Если задан, то выставляем оценки, если не задан - ничего не производится.

       String spec_query = new String();
       String kodPR = rs.getString(1);
         spec_query = "SELECT KodIntervala,Value,Ball FROM IntervalEge WHERE KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti();
         spec_query+= "' AND KodPredmeta LIKE '"+kodPR+"' AND KodVuza LIKE '"+session.getAttribute("kVuza");
         spec_query+= "' AND ( Value NOT LIKE '0' AND NOT(Value LIKE '100' AND Ball LIKE '10')) AND Tip LIKE '";
       if(abit_ENS.getNomerPlatnogoDogovora().equals("no"))
         spec_query+="1'";
       else 
         spec_query+="2'";
       stmt_b = conn.prepareStatement(spec_query);
       rs_b = stmt_b.executeQuery();
       if(rs_b.next()){

                 int oldNote = -1;

// переменная mod_complete нужна для искючения повторной установки оценки, если интервалы заданы неверно
                 boolean mod_complete = false;

                 String query_int = new String();
                   query_int = "SELECT KodIntervala,Value,Ball FROM IntervalEge WHERE KodSpetsialnosti LIKE '"+abit_ENS.getKodSpetsialnosti();
                   query_int+= "' AND KodPredmeta LIKE '"+kodPR+"' AND KodVuza LIKE '"+session.getAttribute("kVuza");
                   query_int+= "' AND Tip LIKE '";
                 if(abit_ENS.getNomerPlatnogoDogovora().equals("no"))
                   query_int+="1'";
                 else 
                   query_int+="2'";
                 stmt_a = conn.prepareStatement(query_int);

                 rs_a = stmt_a.executeQuery();
                 while(rs_a.next()){
                   if(oldNote == -1) { 
                     oldNote = rs_a.getInt(2); 
                     mod_complete = false;
                     continue; 
                   }

// Левая граница интервала < Оценка ЕГЭ <= Правая граница интервала

                   if(!mod_complete && (oldNote < rs.getInt(2)) && (rs.getInt(2) <= rs_a.getInt(2))) {
                     mod_complete = true;

                     stmt_b = conn.prepareStatement("UPDATE Otsenki SET Otsenka=?,From_Ege='д' WHERE KodAbiturienta LIKE ? AND KodPredmeta LIKE ?");
                     stmt_b.setObject(1,rs_a.getString(3),Types.INTEGER);
                     stmt_b.setObject(2,rs.getString(3),Types.INTEGER);
                     stmt_b.setObject(3,rs.getString(1),Types.INTEGER);
                     stmt_b.executeUpdate();
                   }
                   oldNote = rs_a.getInt(2);
                 }
               }

       }//проверка заданы ли диапазоны
}


 if(request.getParameter("view") == null){

// Установка признака подтверждения медали тем медалистам, что набрали 10 баллов по профилирующему предмету специальности

        stmt = conn.prepareStatement("SELECT a.KodAbiturienta,a.Familija,a.PodtvMed FROM Abiturient a,Otsenki o,Spetsialnosti s,Medali m WHERE m.KodMedali=a.KodMedali AND a.KodAbiturienta=o.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodPredmeta=o.KodPredmeta AND o.Otsenka LIKE '10' AND m.ShifrMedali NOT LIKE 'н' AND a.KodVuza LIKE ?");
        stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
        rs = stmt.executeQuery();
        while(rs.next()){
          stmt = conn.prepareStatement("UPDATE Abiturient SET PodtvMed='д' WHERE KodAbiturienta LIKE ?");
          stmt.setObject(1,rs.getString(1),Types.INTEGER);
          stmt.executeUpdate();
        }

  // Результаты работы
               MessageBean msg1 = new MessageBean();
               msg1.setMessage("Внимание! Всего обработано "+number+" абитуриента(ов)");
               fgr_msgs.add(msg1);
  } else {
        MessageBean msg1 = new MessageBean();
        msg1.setMessage("Просмотр/редактирование результатов перевода баллов ЕГЭ");
        fgr_msgs.add(msg1);
  }
}


/*****************************************************************************/
/******************   ОТОБРАЖЕНИЕ РЕЗУЛЬТАТОВ ПЕРЕВОДА   *********************/
/*********************   С ВОЗМОЖНОСТЬЮ ИХ КОРРЕКЦИИ   ***********************/
/*****************************************************************************/

// Выводятся абитуриенты, включая подтвердивших свои медали

 if(form.getAction().equals("results")) {

// Название факультета
             stmt = conn.prepareStatement("SELECT f.Fakultet FROM Fakultety f WHERE f.KodFakulteta LIKE ? AND f.KodVuza LIKE ?");
             stmt.setObject(1,abit_ENS.getKodFakulteta(),Types.INTEGER);
             stmt.setObject(2,session.getAttribute("kVuza"),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) 
               abit_ENS.setFakultet(rs.getString(1).toUpperCase());

// Выбранная специальность
             stmt = conn.prepareStatement("SELECT NazvanieSpetsialnosti FROM Spetsialnosti WHERE KodSpetsialnosti LIKE ?");
             stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
             rs = stmt.executeQuery();
             if(rs.next()) 
               abit_ENS.setSpecial2(rs.getString(1));

// Предметы на специальности

             stmt = conn.prepareStatement("SELECT DISTINCT np.KodPredmeta,np.Sokr FROM NazvanijaPredmetov np,EkzamenyNaSpetsialnosti ens,Spetsialnosti s WHERE ens.KodPredmeta=np.KodPredmeta AND ens.KodSpetsialnosti=s.KodSpetsialnosti AND s.KodSpetsialnosti LIKE ? ORDER BY np.KodPredmeta");
             stmt.setObject(1,abit_ENS.getKodSpetsialnosti(),Types.INTEGER);
             rs = stmt.executeQuery();
             while (rs.next()) {
               AbiturientBean predm = new AbiturientBean();
               predm.setSokr(rs.getString(2));
               predms.add(predm);
             }

             String query2 = new String();
//Выбираем абитуриентов, удовлетворяющих заданным в форме настроек критериям, с ненулевой оценкой ЕГЭ, документы которых хранятся
if((abit_ENS.getShifrMedali()).indexOf('н') != -1){
// Выбираем только отличников
                      query2 = "SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o WHERE o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query2+= " AND m.ShifrMedali IN(" + StringUtil.PlaceUnaryComas("m.ShifrMedali",abit_ENS.getShifrMedali())+") ";
                      query2+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if((abit_ENS.getNomerPlatnogoDogovora()).equals("yes"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query2+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
               else if((abit_ENS.getNomerPlatnogoDogovora()).equals("no"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NULL ";
                      query2+= " AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti()+") ";
                      query2+= " GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora ORDER BY a.Familija,a.Imja,a.Otchestvo,zo.KodAbiturienta,zo.KodPredmeta";
} else {
// Выбираем абитуриентов без отличий и тех отличников, которые не подтвердили свои результаты по профилирующему экзамену на специальность
                      query2 = "SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o WHERE o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query2+= " AND m.ShifrMedali IN(" + StringUtil.PlaceUnaryComas("m.ShifrMedali",abit_ENS.getShifrMedali())+") ";
                      query2+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if((abit_ENS.getNomerPlatnogoDogovora()).equals("yes"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query2+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
               else if((abit_ENS.getNomerPlatnogoDogovora()).equals("no"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NULL ";
                      query2+= " AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti()+") ";
                      query2+= " UNION SELECT zo.KodPredmeta,zo.OtsenkaEge,zo.KodAbiturienta,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora FROM ZajavlennyeShkolnyeOtsenki zo,Abiturient a,Medali m,Spetsialnosti s,Otsenki o WHERE o.KodPredmeta=zo.KodPredmeta AND o.KodAbiturienta=a.KodAbiturienta AND o.KodAbiturienta=zo.KodAbiturienta AND a.KodSpetsialnosti=s.KodSpetsialnosti AND m.KodMedali=a.KodMedali AND zo.KodAbiturienta=a.KodAbiturienta AND a.KodVuza LIKE "+session.getAttribute("kVuza")+" AND a.DokumentyHranjatsja LIKE 'д' ";//AND zo.OtsenkaEge>0 ";
                      query2+= " AND o.Otsenka NOT LIKE '10' AND o.From_Ege LIKE 'д' AND m.ShifrMedali NOT LIKE 'н' ";
                      query2+= " AND a.KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti();
               if((abit_ENS.getNomerPlatnogoDogovora()).equals("yes"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NOT NULL ";
               if((abit_ENS.getSpecial3()).equals("y"))
                      query2+= " AND (a.PodtvMed LIKE 'н' OR a.PodtvMed IS NULL) ";
               else if((abit_ENS.getNomerPlatnogoDogovora()).equals("no"))
                      query2+= " AND a.NomerPlatnogoDogovora IS NULL ";
                      query2+= " AND zo.KodPredmeta IN(SELECT KodPredmeta FROM EkzamenyNaSpetsialnosti WHERE KodSpetsialnosti LIKE "+abit_ENS.getKodSpetsialnosti()+") ";
                      query2+= " GROUP BY zo.KodAbiturienta,zo.KodPredmeta,zo.OtsenkaEge,a.Familija,a.Imja,a.Otchestvo,o.Otsenka,a.NomerLichnogoDela,m.ShifrMedali,a.NomerPlatnogoDogovora ORDER BY a.Familija,a.Imja,a.Otchestvo,zo.KodAbiturienta,zo.KodPredmeta";
}
               stmt = conn.prepareStatement(query2);
               rs = stmt.executeQuery();
               while(rs.next()){
                 AbiturientBean abit = new AbiturientBean();
                 abit.setKodAbiturienta(new Integer(rs.getString(3)));
                 abit.setFamilija(rs.getString(4)+" "+rs.getString(5).substring(0,1)+". "+rs.getString(6).substring(0,1)+".");
                 abit.setOtsenkaegeabiturienta(new Integer(rs.getString(2)));
                 abit.setExzamOtsenka(rs.getString(7));
                 abit.setNomerLichnogoDela(rs.getString(8));
                 abit.setShifrMedali(rs.getString(9));
                 abit.setNomerPlatnogoDogovora(rs.getString(10));
                 abit.setKodPredmeta(new Integer(rs.getString(1)));
                 abit_ENS_S1.add(abit);
               }
               request.setAttribute("specs", specs);
               request.setAttribute("predms", predms);
               form.setAction(us.getClientIntName("results","view"));
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
        request.setAttribute("abit_ENS", abit_ENS);
        request.setAttribute("fgr_msgs", fgr_msgs);
        request.setAttribute("abit_ENS_S1", abit_ENS_S1);
        request.setAttribute("abit_ENS_S2", abit_ENS_S2);
        request.setAttribute("abit_ENS_S3", abit_ENS_S3);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}