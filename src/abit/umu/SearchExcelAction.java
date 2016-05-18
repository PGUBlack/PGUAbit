package abit.umu;

import java.util.Enumeration;
import java.io.IOException;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;


import java.util.Locale;
import java.util.ArrayList;

import org.apache.struts.action.*;

import javax.naming.*;
import javax.sql.*;

import abit.action.AbiturientForm;
import abit.bean.*;
import abit.Constants;
import abit.util.StringUtil;

import java.io.*;

import abit.sql.*; 

public class SearchExcelAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession       session       = request.getSession();
        Connection        conn          = null;
        PreparedStatement stmt          = null;
        ResultSet         rs            = null;
        PreparedStatement stmt_a        = null;
        ResultSet         rs_a          = null;
        ActionErrors      errors        = new ActionErrors();
        ActionError       msg           = null;
        AbiturientForm    form          = (AbiturientForm) actionForm;
        AbiturientBean    abit_Srch     = form.getBean(request, errors);
        Enumeration       paramNames    = request.getParameterNames();
        boolean           error         = false;
        ActionForward     f             = null;
        ArrayList         abits_Srch    = new ArrayList();
        ArrayList         predms        = new ArrayList();
        UserBean          user          = (UserBean)session.getAttribute("user");
        int               total_amount  = 0;
        
        int rowIndex = 0;
        int colIndex = 0;


        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()==0) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "searchOtsAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  Получение соединения с БД и ведение статистики  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "searchOtsForm", form );

/*****************  Возврат к предыдущей странице   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

/********************** Подготовка данных для ввода с помощью селекторов ************************/
      if ( form.getAction() == null ) {

       form.setAction(us.getClientIntName("printOtsForm","init"));

// Список предметов и их кодов

       String tmp = new String("%");
       stmt = conn.prepareStatement("SELECT DISTINCT Sokr,KodPredmeta FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND KodPredmeta IN("+session.getAttribute("KodyPredmetov")+") ORDER BY KodPredmeta ASC");
       stmt.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
       rs = stmt.executeQuery();
       while(rs.next())
        {
         AbiturientBean predm = new AbiturientBean();
         predm.setNazvaniePredmeta(rs.getString(1));
         predm.setPredmet(rs.getString(2));
         predms.add(predm);
         tmp += rs.getString(2) + "%";
        }
         abit_Srch.setSpecial10(tmp);

      }else if ( form.getAction().equals("makeRTFOts") ) {
	 int WidthOfTablo = 0;                             //  Сюда накапливаем сумму ширины всех выбраных столбцов
         String MassForCol[][] = new String[100][2];       //  Массив в который будут записаны имена
                                                           //  выводимых столбцов

         String MassPredmsForCol[][] = new String[50][2];  //  Названия предметов (с кодом предмета)
         String MassOtsForCol[][]    = new String[100][3]; //  Оценки по предмету
         int    WIDTH_ONE_PREDM      = 800;

         int amount = 0;                                  //  Индекс массива с названиями предметов


	 if(abit_Srch.getDokumentyHranjatsja()!=null)     // Если содержимое BEANa не пусто то
                                                          // столбец был выбран
         {
		//В ячейку с выбранным номером пишем заголовок столбца
	  MassForCol[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][0] = "Докум. хран.";
		//Пишем ширину столбца для передачи в тэг RTF
	  MassForCol[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][1] = "870";
		//Увеличиваем переменную содержащую ширину таблицы
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getTipDokSredObraz()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokSredObraz())][0] = "Копия атт.";
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokSredObraz())][1] = "870";
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getFormaOb()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getFormaOb())][0] = "Форма об.";
	  MassForCol[Integer.parseInt(abit_Srch.getFormaOb())][1] = "870";
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getOsnovaOb()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getOsnovaOb())][0] = "Основа об.";
	  MassForCol[Integer.parseInt(abit_Srch.getOsnovaOb())][1] = "870";
	  WidthOfTablo += 870;
	 }

	 if(abit_Srch.getSpecial1()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial1())][0] = "Спец. пост.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial1())][1] = "740";
	  WidthOfTablo += 740;
	 }

	 if(abit_Srch.getNomerLichnogoDela()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][0] = "Номер личн. дела";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][1] = "1050";
	  WidthOfTablo += 950;
	 }

	 if(abit_Srch.getFamilija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getFamilija())][0] = "Фамилия";
	  MassForCol[Integer.parseInt(abit_Srch.getFamilija())][1] = "1670";
	  WidthOfTablo += 1670;
	 }

	 if(abit_Srch.getImja()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getImja())][0] = "Имя";
	  MassForCol[Integer.parseInt(abit_Srch.getImja())][1] = "1357";
	  WidthOfTablo += 1357;
	 }

	 if(abit_Srch.getOtchestvo()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getOtchestvo())][0] = "Отчество";
	  MassForCol[Integer.parseInt(abit_Srch.getOtchestvo())][1] = "2100";
	  WidthOfTablo += 2250;
	 }

	 if(abit_Srch.getFio()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getFio())][0] = "Фамилия И.О.";
	  MassForCol[Integer.parseInt(abit_Srch.getFio())][1] = "2400";
	  WidthOfTablo += 2400;
	 }

	 if(abit_Srch.getShifrKursov()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getShifrKursov())][0] = "Курсы";
	  MassForCol[Integer.parseInt(abit_Srch.getShifrKursov())][1] = "803";
	  WidthOfTablo += 803;
	 }

	 if(abit_Srch.getMedal()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getMedal())][0] = "Отличия";
	  MassForCol[Integer.parseInt(abit_Srch.getMedal())][1] = "991";
	  WidthOfTablo += 991;
	 }

	 if(abit_Srch.getLgoty()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getLgoty())][0] = "Льготы";
	  MassForCol[Integer.parseInt(abit_Srch.getLgoty())][1] = "890";
	  WidthOfTablo += 890;
	 }

	 if(abit_Srch.getNomerPlatnogoDogovora()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][0] = "Номер платн. догов.";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][1] = "1390";
	  WidthOfTablo += 1390;
	 }

	 if(abit_Srch.getSpecial2()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial2())][0] = "Год рожд.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial2())][1] = "1200";
	  WidthOfTablo += 1200;
	 }

	 if(abit_Srch.getSpecial5()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial5())][0] = "Год оконч. средн. обр.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial5())][1] = "805";
	  WidthOfTablo += 805;
	 }

	 if(abit_Srch.getPol()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getPol())][0] = "Пол";
	  MassForCol[Integer.parseInt(abit_Srch.getPol())][1] = "590";
	  WidthOfTablo += 590;
	 }

	 if(abit_Srch.getSrokObuchenija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSrokObuchenija())][0] = "Срок обучения";
	  MassForCol[Integer.parseInt(abit_Srch.getSrokObuchenija())][1] = "1060";
	  WidthOfTablo += 1060;
	 }

	 if(abit_Srch.getGrajdanstvo()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getGrajdanstvo())][0] = "Гражд.";
	  MassForCol[Integer.parseInt(abit_Srch.getGrajdanstvo())][1] = "850";
	  WidthOfTablo += 850;
	 }

	 if(abit_Srch.getGdePoluchilSrObrazovanie()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][0] = "Где получил ср. обр.";
	  MassForCol[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][1] = "970";
	  WidthOfTablo += 970;
	 }

	 if(abit_Srch.getNomerShkoly()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerShkoly())][0] = "Номер школы";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerShkoly())][1] = "840";
	  WidthOfTablo += 840;
	 }

	 if(abit_Srch.getInostrannyjJazyk()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getInostrannyjJazyk())][0] = "Ин. яз.";
	  MassForCol[Integer.parseInt(abit_Srch.getInostrannyjJazyk())][1] = "540";
	  WidthOfTablo += 540;
	 }

	 if(abit_Srch.getNujdaetsjaVObschejitii()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNujdaetsjaVObschejitii())][0] = "Потр. в общеж.";
	  MassForCol[Integer.parseInt(abit_Srch.getNujdaetsjaVObschejitii())][1] = "890";
	  WidthOfTablo += 890;
	 }

	 if(abit_Srch.getNazvanie()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanie())][0] = "Населенный пункт";
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanie())][1] = "1970";
	  WidthOfTablo += 1970;
	 }

	 if(abit_Srch.getNazvanieRajona()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieRajona())][0] = "Район";
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieRajona())][1] = "2190";
	  WidthOfTablo += 2190;
	 }

	 if(abit_Srch.getNazvanieOblasti()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieOblasti())][0] = "Область";
	  MassForCol[Integer.parseInt(abit_Srch.getNazvanieOblasti())][1] = "1510";
	  WidthOfTablo += 1510;
	 }

	 if(abit_Srch.getPolnoeNaimenovanieZavedenija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][0] = "Полное наименование заведения";
	  MassForCol[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][1] = "2190";
	  WidthOfTablo += 2190;
	 }

	 if(abit_Srch.getTipOkonchennogoZavedenija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][0] = "Тип оконч. завед.";
	  MassForCol[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][1] = "803";
	  WidthOfTablo += 803;
	 }

	 if(abit_Srch.getTrudovajaDejatelnost()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTrudovajaDejatelnost())][0] = "Труд. деят.";
	  MassForCol[Integer.parseInt(abit_Srch.getTrudovajaDejatelnost())][1] = "740";
	  WidthOfTablo += 740;
	 }

	 if(abit_Srch.getGruppa()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getGruppa())][0] = "Группа";
	  MassForCol[Integer.parseInt(abit_Srch.getGruppa())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getNapravlenieOtPredprijatija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNapravlenieOtPredprijatija())][0] = "Направлен. от предпр.";
	  MassForCol[Integer.parseInt(abit_Srch.getNapravlenieOtPredprijatija())][1] = "1330";
	  WidthOfTablo += 1330;
	 }

	 if(abit_Srch.getTipDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokumenta())][0] = "Тип докум.";
	  MassForCol[Integer.parseInt(abit_Srch.getTipDokumenta())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getSeriaDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaDokumenta())][0] = "серия пасп.";
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaDokumenta())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getNomerDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerDokumenta())][0] = "№ паспорта";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerDokumenta())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getSeriaAtt()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaAtt())][0] = "серия атт.";
	  MassForCol[Integer.parseInt(abit_Srch.getSeriaAtt())][1] = "885";
	  WidthOfTablo += 885;
	 }

	 if(abit_Srch.getNomerAtt()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerAtt())][0] = "№ аттест.";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerAtt())][1] = "985";
	  WidthOfTablo += 985;
	 }

	 if(abit_Srch.getDataVydDokumenta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getDataVydDokumenta())][0] = "Дата выд. пасп.";
	  MassForCol[Integer.parseInt(abit_Srch.getDataVydDokumenta())][1] = "1200";
	  WidthOfTablo += 1200;
	 }

	 if(abit_Srch.getKemVydDokument()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getKemVydDokument())][0] = "Кем выдан паспорт";
	  MassForCol[Integer.parseInt(abit_Srch.getKemVydDokument())][1] = "1600";
	  WidthOfTablo += 1600;
	 }

	 if(abit_Srch.getSobesedovanie()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSobesedovanie())][0] = "Собесед.";
	  MassForCol[Integer.parseInt(abit_Srch.getSobesedovanie())][1] = "1040";
	  WidthOfTablo += 1040;
	 }

	 if(abit_Srch.getKodTselevogoPriema()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getTselevojPriem())][0] = "Целевой прием";
	  MassForCol[Integer.parseInt(abit_Srch.getTselevojPriem())][1] = "1040";
	  WidthOfTablo += 1040;
	 }

	 if(abit_Srch.getSpecial7()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial7())][0] = "Балл";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial7())][1] = "740";
	  WidthOfTablo += 740;
	 }

	 if(abit_Srch.getPrinjat()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getPrinjat())][0] = "Зачислен";
	  MassForCol[Integer.parseInt(abit_Srch.getPrinjat())][1] = "1100";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getShifrFakulteta()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getShifrFakulteta())][0] = "Факультет";
	  MassForCol[Integer.parseInt(abit_Srch.getShifrFakulteta())][1] = "1150";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getAttestat()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getAttestat())][0] = "Копия аттестата";
	  MassForCol[Integer.parseInt(abit_Srch.getAttestat())][1] = "1475";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getDataRojdenija()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getDataRojdenija())][0] = "Дата рождения";
	  MassForCol[Integer.parseInt(abit_Srch.getDataRojdenija())][1] = "1475";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getNomerSertifikata()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getNomerSertifikata())][0] = "Номер сертификата";
	  MassForCol[Integer.parseInt(abit_Srch.getNomerSertifikata())][1] = "1475";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getKopijaSertifikata()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getKopijaSertifikata())][0] = "Копия сертиф.";
	  MassForCol[Integer.parseInt(abit_Srch.getKopijaSertifikata())][1] = "1100";
	  WidthOfTablo += 1100;
	 }

	 if(abit_Srch.getSpecial8()!=null)
	 {
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial8())][0] = "Спец. зач.";
	  MassForCol[Integer.parseInt(abit_Srch.getSpecial8())][1] = "740";
	  WidthOfTablo += 740;
	 }

         int mass_index = 0;
         while(paramNames.hasMoreElements()) {
           String paramName = (String)paramNames.nextElement();
           String paramValue[] = request.getParameterValues(paramName);
           if(paramName.indexOf("prdm") != -1) {
             stmt_a = conn.prepareStatement("SELECT Sokr FROM NazvanijaPredmetov WHERE KodVuza LIKE ? AND KodPredmeta LIKE ?");
             stmt_a.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
             stmt_a.setObject(2,paramName.substring(4),Types.INTEGER);
             rs_a = stmt_a.executeQuery();
             if(rs_a.next()) MassPredmsForCol[amount][0] = rs_a.getString(1); // Название предмета
             MassPredmsForCol[amount++][1] = paramName.substring(4);
           }
           else if(paramName.indexOf("ex_att") != -1 && StringUtil.toInt(paramValue[0],-1) != -1) {
             MassOtsForCol[mass_index][0]   = paramName.substring(6);
             MassOtsForCol[mass_index][1]   = "Атт.";       // Оценка Аттестата
             MassOtsForCol[mass_index++][2] = "OtsenkaAtt"; // Имя поля в БД для выборки
           }
           else if(paramName.indexOf("ex_ege") != -1 && StringUtil.toInt(paramValue[0],-1) != -1) {
             MassOtsForCol[mass_index][0]   = paramName.substring(6);
             MassOtsForCol[mass_index][1]   = "ЕГЭ.";       // Балл ЕГЭ
             MassOtsForCol[mass_index++][2] = "OtsenkaEge"; // Имя поля в БД для выборки
           }
           else if(paramName.indexOf("ex_zaj") != -1 && StringUtil.toInt(paramValue[0],-1) != -1) {
             MassOtsForCol[mass_index][0]   = paramName.substring(6);
             MassOtsForCol[mass_index][1]   = "Заявл.";        // Оценка Заявленная
             MassOtsForCol[mass_index++][2] = "OtsenkaZajavl"; // Имя поля в БД для выборки
           }
           else if(paramName.indexOf("ex_exm") != -1 && StringUtil.toInt(paramValue[0],-1) != -1) {
             MassOtsForCol[mass_index][0]   = paramName.substring(6);
             MassOtsForCol[mass_index][1]   = "Экз.";    // Оценка Экзамена
             MassOtsForCol[mass_index++][2] = "Otsenka"; // Имя поля в БД для выборки
           }
           else if(paramName.indexOf("ex_gse") != -1 && StringUtil.toInt(paramValue[0],-1) != -1) {
             MassOtsForCol[mass_index][0]   = paramName.substring(6);
             MassOtsForCol[mass_index][1]   = "ГСЭ";    // Признак - "где сдает экзамен"
             MassOtsForCol[mass_index++][2] = "Examen"; // Имя поля в БД для выборки
           }
         }

//<<<<<<<<<<<<<<<<<<<< Если ширина таблицы превышает размер листа <<<<<<<<<<<<<<<<<<<<<<<

	 boolean prizn = false;
	 if(abit_Srch.getPriznakSortirovki().equals("Альбомная"))
	 {
	  if(WidthOfTablo > 16840)
		prizn = true;
	 } else if(WidthOfTablo > 11907)
                {
                  prizn = true;
                }
         if(prizn == true)
         {
	   abit_Srch.setSpecial22(new Integer(20));	//Устанавливаем признак возврата для JSP
           request.setAttribute("abit_Srch", abit_Srch);
           request.setAttribute("abits_Srch", abits_Srch);
           form.setAction(us.getClientIntName("printForm","reentr-out-page"));
           return mapping.findForward("success");
         }

/************************************************/
/***** Регистрация файла в Браузере отчетов *****/
/************************************************/

    String name = "Результаты поиска по оценкам от "+StringUtil.CurrDate(".")+" на "+StringUtil.CurrTime(":");

    String file_con = new String("srch_ots_d_"+StringUtil.CurrDate(".")+"_t_"+StringUtil.CurrTime("_"));

    session.setAttribute("rpt",StringUtil.AddToRepBrw(user.getName()+user.getUid(),name,file_con,"rtf"));

    String file_name = (request.getRealPath(request.getContextPath())).substring(0,request.getRealPath(request.getContextPath()).lastIndexOf('\\'))+Constants.RELATIVE_PATH+"\\"+((ReportsBrowserBean)session.getAttribute("rpt")).getFileName();

    BufferedWriter report = new BufferedWriter(new FileWriter(file_name)); 

	 report.write("{\\rtf1\\ansi\n");

	 if(abit_Srch.getPriznakSortirovki().equals("Альбомная"))
	 {
report.write("\\paperw16838\\paperh11906\\margl567\\margr567\\margt567\\margb567\\widowctrl\\ftnbj\\aenddoc\\noxlattoyen\\expshrtn\\noultrlspc\\dntblnsbdb\\nospaceforul\\formshade\\horzdoc\\dghspace180\\dgvspace180\\dghorigin1701\\dgvorigin1984\\dghshow0\\dgvshow0\\jexpand\\viewkind1\\viewscale90\\viewzk2\\pgbrdrhead\\pgbrdrfoot\\nolnhtadjtbl \\fet0\\sectd \\lndscpsxn\\psz9\\linex0\\endnhere\\sectdefaultcl\n");
	 }
	 if(abit_Srch.getSpecial3() != null)	//Вывод заголовка
	 {
	  report.write("\\fs40 \\qc \\b1"+abit_Srch.getSpecial3()+"\\b0\n");
	  report.write("\\par\\par\n");
	 }
	 if(abit_Srch.getSpecial4() != null)	//Вывод подзаголовка
	 {
	  report.write("\\fs32 \\qc "+abit_Srch.getSpecial4()+"\n");
	  report.write("\\par\\par\n");
	 }

	 int i = 1;
         int col_predm = amount;
	 int tabulator = 700;   //Ширина колонки с номерами строк

// Формат шапки таблицы (1-я строка)

	 report.write("\\fs20 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");

///         if(col_predm!=0)
         report.write("\\clvmgf");

         report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");

	 while(MassForCol[i][0]!=null)	
	 {
           tabulator += Integer.parseInt(MassForCol[i++][1]);

///           if(col_predm!=0) 
report.write("\\clvmgf");

           report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
	 }

         int curr_code = -2, curr_amount;

         for(int ind = 0; ind < col_predm; ind++) {

            curr_code = StringUtil.toInt(MassPredmsForCol[ind][1],-1); // Код предмета

            curr_amount = 0;

            for(int j = 0; j < mass_index; j++) {

               if(curr_code == StringUtil.toInt(MassOtsForCol[j][0],-1)) curr_amount ++;
            }

            if(curr_amount != 0) {
              tabulator += curr_amount*WIDTH_ONE_PREDM;
              report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
            }
         }

//Вывод шапки таблицы (данные 1-й строки)

	 i = 1;

         report.write("\\intbl{№}\\cell\n");

	 while(MassForCol[i][0]!=null)	
	 {
          report.write("\\intbl{"+MassForCol[i++][0]+"}\\cell\n");
	 }

         for(int ind = 0; ind < col_predm; ind++) {

            report.write("\\intbl{"+MassPredmsForCol[ind][0]+"}\\cell\n"); // Название предмета

         }

         report.write("\\intbl\\row\n");

// Формат шапки таблицы (2-я строка)

	 i = 1;
	 tabulator = 700;
	 report.write("\\fs20 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\\trhdr\n");
	 report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");

	 while(MassForCol[i][0]!=null)	
	 {
	  tabulator += Integer.parseInt(MassForCol[i++][1]);
          report.write("\\clvmrg\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
	 }

         curr_code = -1;
         total_amount=0;

         for(int ind = 0; ind < col_predm; ind++) {

            curr_code = StringUtil.toInt(MassPredmsForCol[ind][1],-1); // Код предмета

            curr_amount = 0;

            for(int j = 0; j < mass_index; j++) {

               if(curr_code == StringUtil.toInt(MassOtsForCol[j][0],-2)) {
                 total_amount++;
                 report.write("\\clvertalc \\clbrdrb\\brdrs \\brdrdb\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(tabulator+total_amount*WIDTH_ONE_PREDM)+"\n");
               }
            }
         }

//Вывод шапки таблицы (данные 2-й строки)

	 i = 1;

         report.write("\\intbl\\cell\n");

	 while(MassForCol[i][0]!=null)	
	 {
          report.write("\\intbl\\cell\n");
          i++;
	 }

// Оценки Аттестата, Экзамена, Заявленные. Баллы ЕГЭ и признак "Где сдает экзамен"

         for(int ind = 0; ind < col_predm; ind++) {

            curr_code = StringUtil.toInt(MassPredmsForCol[ind][1],-1); // Код предмета

            for(int j = 0; j < mass_index; j++) {

               if(curr_code == StringUtil.toInt(MassOtsForCol[j][0],-1)) {

                 report.write("\\intbl{"+MassOtsForCol[j][1]+"}\\cell\n"); // Название Оценки
               }
            }
         }


         report.write("\\intbl\\row\n");

// Формат строк данных таблицы

	 i = 1;
	 tabulator = 700;

	 report.write("\\fs20 \\trowd \\trqc\\trgaph108\\trrh280\\trleft36\n");
	 report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");

	 while(MassForCol[i][0]!=null)	
	 {
	  tabulator += Integer.parseInt(MassForCol[i++][1]);
          report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+tabulator+"\n");
	 }
         for(int ind=0;ind<total_amount;ind++) 
          report.write("\\clvertalc \\clbrdrb\\brdrs\\brdrw20\\brdrcf1 \\clbrdrt\\brdrs\\brdrw20\\brdrcf1 \\clbrdrl\\brdrs\\brdrw20\\brdrcf1 \\clbrdrr\\brdrs\\brdrw20\\brdrcf1 \\cellx"+(tabulator+(ind+1)*WIDTH_ONE_PREDM)+"\n");


/****************************** Выборка абитуриентов из БД ******************************/
  String MasSelector[][] = new String[100][2];  // Массив предназначен для сохранения
                                                // данных полученных в результате 
                                                // выполнения запроса
  int masInc = 0;                               // Сюда накапливаем количество выводимых столбцов
  int xxx = 1;	                                // Используется в качестве индекса массива MasSelector
  int number = 1;                               // Номер строки таблицы


  
  Workbook workbook = new Workbook();

  //добавляем новый лист Excel и получаем к нему доступ
  int sheetIndex = workbook.getWorksheets().add();
  Worksheet worksheet = workbook.getWorksheets().get(sheetIndex);
  Cells cells = worksheet.getCells();

/****************************** Выборка абитуриентов из БД ******************************/

  StringBuffer query = new StringBuffer("SELECT Abiturient.KodAbiturienta,DokumentyHranjatsja,Abbreviatura,");

  if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
    query.append("Konkurs.NomerLichnogoDela");
  else
    query.append("Abiturient.NomerLichnogoDela");

  query.append(",Familija,Imja,Otchestvo,NomerPlatnogoDogovora,Pol,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,NomerShkoly,PolnoeNaimenovanieZavedenija,TipOkonchennogoZavedenija,Gruppa,TipDokumenta,NomerDokumenta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,NomerSertifikata,KopijaSertifikata,Ball,Prinjat,KodSpetsialnZach,SeriaAtt,NomerAtt,Spetsialnosti.KodSpetsialnosti,Fakultety.ShifrFakulteta FROM Abiturient,Spetsialnosti,Fakultety,Zavedenija,Gruppy,AbitDopInf");

  if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
    query.append(",Konkurs");

  query.append(" WHERE ");

  if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
    query.append("Konkurs.KodAbiturienta=Abiturient.KodAbiturienta AND Konkurs.KodSpetsialnosti=Spetsialnosti.KodSpetsialnosti AND ");
  else
    query.append("Abiturient.KodSpetsialnosti = Spetsialnosti.KodSpetsialnosti AND ");


 StringBuffer noteQuery = new StringBuffer("Abiturient.KodAbiturienta IN(SELECT o.KodAbiturienta FROM Otsenki o,ZajavlennyeShkolnyeOtsenki zso WHERE o.KodAbiturienta=zso.KodAbiturienta AND o.KodPredmeta=zso.KodPredmeta");

//!!! WARNING !!! Здесь устанавливаются ограничения выборки по оценкам

//ограничение по аттестационной оценке

 if(!((""+session.getAttribute("otsenka_Att_ot")).equals("0") && (""+session.getAttribute("otsenka_Att_do")).equals("10"))) {
  noteQuery.append(" AND (zso.OtsenkaAtt BETWEEN "+session.getAttribute("otsenka_Att_ot")+" AND "+session.getAttribute("otsenka_Att_do"));
//  noteQuery.append(" OR zso.OtsenkaAtt IS NULL");
  noteQuery.append(")");
 }

//ограничение по баллам ЕГЭ

 if(!((""+session.getAttribute("otsenka_Ege_ot")).equals("0") && (""+session.getAttribute("otsenka_Ege_do")).equals("100"))) {
  noteQuery.append(" AND (zso.OtsenkaEge BETWEEN "+session.getAttribute("otsenka_Ege_ot")+" AND "+session.getAttribute("otsenka_Ege_do"));
//  noteQuery.append(" OR zso.OtsenkaEge IS NULL");
  noteQuery.append(")");
 }

//ограничение по заявленной оценке

 if(!((""+session.getAttribute("otsenka_Zaj_ot")).equals("0") && (""+session.getAttribute("otsenka_Zaj_do")).equals("10"))) {
  noteQuery.append(" AND (zso.OtsenkaZajavl BETWEEN "+session.getAttribute("otsenka_Zaj_ot")+" AND "+session.getAttribute("otsenka_Zaj_do"));
//  noteQuery.append(" OR zso.OtsenkaZajavl IS NULL");
  noteQuery.append(")");
 }

//ограничение по экзаменационной оценке

 if(!((""+session.getAttribute("otsenka_Exam_ot")).equals("0") && (""+session.getAttribute("otsenka_Exam_do")).equals("10"))) {
  noteQuery.append(" AND (o.Otsenka BETWEEN "+session.getAttribute("otsenka_Exam_ot")+" AND "+session.getAttribute("otsenka_Exam_do"));
//  noteQuery.append(" OR o.Otsenka IS NULL");
  noteQuery.append(")");
 }
 
 
 

//ограничение по дате апелляции

 if(!((""+session.getAttribute("DataApelljatsii")).equals("00-00-0000"))) {
  noteQuery.append(" AND (o.Data LIKE '"+session.getAttribute("DataApelljatsii")+"'");
//  noteQuery.append(" OR o.Data IS NULL");
  noteQuery.append(")");
 }

//ограничение по признаку апелляции

 if(!((""+session.getAttribute("Apelljatsija")).equals("%"))) {
  noteQuery.append(" AND (o.Apelljatsija LIKE '"+session.getAttribute("Apelljatsija")+"'");
//  noteQuery.append(" OR o.Apelljatsija IS NULL");
  noteQuery.append(")");
 }

//ограничение по "где сдает экзамен"

 if(!((""+session.getAttribute("Examen")).equals("%"))) {
  noteQuery.append(" AND (zso.Examen LIKE '"+session.getAttribute("Examen")+"'");
//  noteQuery.append(" OR zso.Examen IS NULL");
  noteQuery.append(")");
 }

//ограничение по кодам предметов

 noteQuery.append(" AND o.KodPredmeta IN ("+session.getAttribute("KodyPredmetov")+")");

 noteQuery.append(")");
 query.append(noteQuery);

 query.append(" AND Gruppy.KodGruppy=Abiturient.KodGruppy AND Abiturient.kodAbiturienta = AbitDopInf.kodAbiturienta AND Spetsialnosti.KodFakulteta = Fakultety.KodFakulteta AND  Abiturient.KodZavedenija = Zavedenija.KodZavedenija AND Abiturient.KodVuza = ");
 query.append(session.getAttribute("kVuza"));

/// THIS LINE IS FOR
/// --- Debugging purposes ONLY ---
//System.out.println(query.toString());

//******** ЛОГИКА ПАРАМЕТРИЧЕСКОЙ ВЫБОРКИ **********

 StringBuffer condition = new StringBuffer();
/* if(!(""+session.getAttribute("KodOsnovyOb")).equals("0"))
  condition.append(" AND Abiturient.KodOsnovyOb LIKE "+"'"+session.getAttribute("KodOsnovyOb")+"'");
 if(!(""+session.getAttribute("KodFormyOb")).equals("0"))
  condition.append(" AND Abiturient.KodFormyOb LIKE "+"'"+session.getAttribute("KodFormyOb")+"'");*/
 if(!(""+session.getAttribute("DokumentyHranjatsja")).equals("%"))
  condition.append(" AND DokumentyHranjatsja LIKE "+"'"+session.getAttribute("DokumentyHranjatsja")+"'");
 
 if(!(""+session.getAttribute("ShifrFakulteta")).equals("%"))
  condition.append(" AND Fakultety.ShifrFakulteta LIKE "+"'"+session.getAttribute("ShifrFakulteta")+"'");
  condition.append(" AND Spetsialnosti.KodSpetsialnosti LIKE "+"'"+session.getAttribute("Special1")+"'");
 if(!(""+session.getAttribute("NomerLichnogoDela")).equals("%")) {
   if((""+session.getAttribute("UseAllSpecs")).equals("yes"))
     condition.append(" AND Konkurs.NomerLichnogoDela LIKE "+"'"+session.getAttribute("NomerLichnogoDela")+"'");
   else
     condition.append(" AND Abiturient.NomerLichnogoDela LIKE "+"'"+session.getAttribute("NomerLichnogoDela")+"'");
 }
 if(!(""+session.getAttribute("Familija")).equals("%"))
  condition.append(" AND Familija LIKE "+"'"+session.getAttribute("Familija")+"'");
 if(!(""+session.getAttribute("Imja")).equals("%"))
  condition.append(" AND Imja LIKE "+"'"+session.getAttribute("Imja")+"'");
 if(!(""+session.getAttribute("Otchestvo")).equals("%"))
  condition.append(" AND Otchestvo LIKE "+"'"+session.getAttribute("Otchestvo")+"'");
 if(!(""+session.getAttribute("ShifrKursov")).equals("%"))
  condition.append(" AND (TipDokumenta IN(" + session.getAttribute("ShifrKursov")+") OR TipDokumenta IS NULL)");
 
 String vidDokCondition = "";
 if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))
	   vidDokCondition =   " AND viddoksredobraz ='"+session.getAttribute("VidDokSredObraz")+"'";
 
 if((""+session.getAttribute("VidDokSredObraz")).equals("Диплом ВО/СПО"))
	   vidDokCondition = " AND (viddoksredobraz = 'Диплом ВПО' OR viddoksredobraz = 'Диплом СПО')";
	   
 if(!(""+session.getAttribute("VidDokSredObraz")).equals("%"))  
				   condition.append(vidDokCondition);
 
 if(!(""+session.getAttribute("TipDokSredObraz")).equals("%"))
     condition.append(" AND (TipDokSredObraz LIKE "+"'"+session.getAttribute("TipDokSredObraz")+"'"+" OR TipDokSredObraz IS NULL)");
 
 
 if(!(""+session.getAttribute("KodLgot")).equals("%"))  
	   condition.append(" AND Konkurs.OP LIKE "+"'"+session.getAttribute("KodLgot")+"'");
 
 if((""+session.getAttribute("bud1")).equals("on"))  
	   condition.append(" AND AbitDopInf.Dist not in ('-')");
 
/*   if(!(""+session.getAttribute("ShifrMedali")).equals("%"))
  condition.append(" AND (ShifrMedali IN(" + session.getAttribute("ShifrMedali")+") OR ShifrMedali IS NULL)");
 if(!(""+session.getAttribute("ShifrLgot")).equals("%"))
  condition.append(" AND (ShifrLgot IN(" + session.getAttribute("ShifrLgot")+") OR ShifrLgot IS NULL)");*/
 if(!(""+session.getAttribute("NomerPlatnogoDogovora")).equals("%"))
  condition.append(" AND (" + session.getAttribute("NomerPlatnogoDogovora")+session.getAttribute("PriznakDog")+")");
 if(!(""+session.getAttribute("Pol")).equals("%"))
  condition.append(" AND (Pol LIKE "+"'"+session.getAttribute("Pol")+"'"+" OR Pol IS NULL)");
 if(!((""+session.getAttribute("GodOkonchanijaSrObrazovanija1")).equals("1950") && (""+session.getAttribute("GodOkonchanijaSrObrazovanija2")).equals("9999"))) {
  condition.append(" AND (GodOkonchanijaSrObrazovanija >= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija1")+"'");
  condition.append(" AND GodOkonchanijaSrObrazovanija <= "+"'"+session.getAttribute("GodOkonchanijaSrObrazovanija2")+"'"+" OR GodOkonchanijaSrObrazovanija IS NULL)");
 }
 if(!(""+session.getAttribute("GdePoluchilSrObrazovanie")).equals("%"))
  condition.append(" AND (GdePoluchilSrObrazovanie LIKE "+"'"+session.getAttribute("GdePoluchilSrObrazovanie")+"'"+" OR GdePoluchilSrObrazovanie IS NULL)");
 if(!(""+session.getAttribute("NomerShkoly")).equals("%"))
  condition.append(" AND (NomerShkoly LIKE "+"'"+session.getAttribute("NomerShkoly")+"'"+" OR NomerShkoly IS NULL)");
/*  if(!(""+session.getAttribute("Nazvanie")).equals("%"))
  condition.append(" AND (Punkty.Nazvanie LIKE "+"'"+session.getAttribute("Nazvanie")+"'"+" OR Punkty.Nazvanie IS NULL)");
 if(!(""+session.getAttribute("NazvanieRajona")).equals("%"))
  condition.append(" AND (Rajony.NazvanieRajona LIKE "+"'"+session.getAttribute("NazvanieRajona")+"'"+" OR NazvanieRajona IS NULL)");
 if(!(""+session.getAttribute("NazvanieOblasti")).equals("%"))
  condition.append(" AND (Oblasti.NazvanieOblasti LIKE "+"'"+session.getAttribute("NazvanieOblasti")+"'"+" OR NazvanieOblasti IS NULL)");
 if(!(""+session.getAttribute("PolnoeNaimenovanieZavedenija")).equals("%"))*/
  condition.append(" AND (PolnoeNaimenovanieZavedenija LIKE "+"'"+session.getAttribute("PolnoeNaimenovanieZavedenija")+"'"+" OR PolnoeNaimenovanieZavedenija IS NULL)");
 if(!(""+session.getAttribute("TipOkonchennogoZavedenija")).equals("%"))
  condition.append(" AND (TipOkonchennogoZavedenija LIKE "+"'"+session.getAttribute("TipOkonchennogoZavedenija")+"'"+" OR TipOkonchennogoZavedenija IS NULL)");
 if(!(""+session.getAttribute("Gruppa")).equals("%"))
  condition.append(" AND (Gruppa LIKE "+"'"+session.getAttribute("Gruppa")+"'"+" OR Gruppa IS NULL)");
 if(!(""+session.getAttribute("Ball")).equals("%"))
  condition.append(" AND (Ball LIKE '"+session.getAttribute("Ball")+"')");
 if((session.getAttribute("KodSpetsialnZach")+"").equals("%"))
   condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"' OR KodSpetsialnZach IS NULL)");
 else
   condition.append(" AND (KodSpetsialnZach LIKE '"+session.getAttribute("KodSpetsialnZach")+"')");
 if(!(""+session.getAttribute("Prinjat")).equals("%"))
  condition.append(" AND (Prinjat IN(" + session.getAttribute("Prinjat")+") OR Prinjat IS NULL)");
//************************************************
  query.append(condition);
  query.append(" ORDER BY "+ session.getAttribute("stSort")+" "+session.getAttribute("prSort"));

       stmt = conn.prepareStatement(query.toString());
       rs = stmt.executeQuery();
       while(rs.next()) {
    	   
    	   rowIndex ++;


       if(abit_Srch.getDokumentyHranjatsja()!=null){

 //Сохраняем информацию из запроса
          MasSelector[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][0] = rs.getString(2);
 //Устанавливаем выравнивание содержимого столбца
	  MasSelector[Integer.parseInt(abit_Srch.getDokumentyHranjatsja())][1] = "\\qc";
 //Увеличиваем количество выводимых столбцов
	  masInc++;
	 }

	 if(abit_Srch.getSpecial1()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial1())][0] = rs.getString(3);
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial1())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerLichnogoDela()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][0] = rs.getString(4);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerLichnogoDela())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getFamilija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getFamilija())][0] = rs.getString(5);
	  MasSelector[Integer.parseInt(abit_Srch.getFamilija())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getImja()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getImja())][0] = rs.getString(6);
	  MasSelector[Integer.parseInt(abit_Srch.getImja())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getOtchestvo()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getOtchestvo())][0] = rs.getString(7);
	  MasSelector[Integer.parseInt(abit_Srch.getOtchestvo())][1] = "\\ql";
	  masInc++;
	 }


	 if(abit_Srch.getFio()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getFio())][0] = rs.getString(5)+" "+rs.getString(6).toUpperCase().substring(0,1)+"."+rs.getString(7).toUpperCase().substring(0,1)+".";
	  MasSelector[Integer.parseInt(abit_Srch.getFio())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getShifrKursov()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getShifrKursov())][0] = rs.getString(8);
	  MasSelector[Integer.parseInt(abit_Srch.getShifrKursov())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getMedal()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getMedal())][0] = rs.getString(9);
	  MasSelector[Integer.parseInt(abit_Srch.getMedal())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getLgoty()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getLgoty())][0] = rs.getString(10);
	  MasSelector[Integer.parseInt(abit_Srch.getLgoty())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerPlatnogoDogovora()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][0] = rs.getString(11);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerPlatnogoDogovora())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getPol()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getPol())][0] = rs.getString(12);
	  MasSelector[Integer.parseInt(abit_Srch.getPol())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSpecial5()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial5())][0] = Integer.toString(rs.getInt(13));
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial5())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getGdePoluchilSrObrazovanie()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][0] = rs.getString(14);
	  MasSelector[Integer.parseInt(abit_Srch.getGdePoluchilSrObrazovanie())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerShkoly()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerShkoly())][0] = rs.getString(15);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerShkoly())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNazvanie()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanie())][0] = rs.getString(16);
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanie())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getNazvanieRajona()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieRajona())][0] = rs.getString(17);
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieRajona())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getNazvanieOblasti()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieOblasti())][0] = rs.getString(18);
	  MasSelector[Integer.parseInt(abit_Srch.getNazvanieOblasti())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getPolnoeNaimenovanieZavedenija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][0] = rs.getString(19);
	  MasSelector[Integer.parseInt(abit_Srch.getPolnoeNaimenovanieZavedenija())][1] = "\\ql";
	  masInc++;
	 }

	 if(abit_Srch.getTipOkonchennogoZavedenija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][0] = rs.getString(20);
	  MasSelector[Integer.parseInt(abit_Srch.getTipOkonchennogoZavedenija())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getGruppa()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getGruppa())][0] = rs.getString(15);
	  MasSelector[Integer.parseInt(abit_Srch.getGruppa())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getTipDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokumenta())][0] = rs.getString(22);
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerDokumenta())][0] = rs.getString(23);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSeriaDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaDokumenta())][0] = rs.getString(24);
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getDataVydDokumenta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getDataVydDokumenta())][0] = rs.getString(25);
	  MasSelector[Integer.parseInt(abit_Srch.getDataVydDokumenta())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getKemVydDokument()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getKemVydDokument())][0] = rs.getString(26);
	  MasSelector[Integer.parseInt(abit_Srch.getKemVydDokument())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getTipDokSredObraz()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokSredObraz())][0] = rs.getString(27);
	  MasSelector[Integer.parseInt(abit_Srch.getTipDokSredObraz())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerSertifikata()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerSertifikata())][0] = rs.getString(28);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerSertifikata())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getKopijaSertifikata()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getKopijaSertifikata())][0] = rs.getString(29);
	  MasSelector[Integer.parseInt(abit_Srch.getKopijaSertifikata())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSpecial7()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial7())][0] = rs.getString(30);
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial7())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getPrinjat()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getPrinjat())][0] = rs.getString(31);
	  MasSelector[Integer.parseInt(abit_Srch.getPrinjat())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getFormaOb()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getFormaOb())][0] = rs.getString(33);
	  MasSelector[Integer.parseInt(abit_Srch.getFormaOb())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getOsnovaOb()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getOsnovaOb())][0] = rs.getString(34);
	  MasSelector[Integer.parseInt(abit_Srch.getOsnovaOb())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getSeriaAtt()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaAtt())][0] = rs.getString(35);
	  MasSelector[Integer.parseInt(abit_Srch.getSeriaAtt())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getNomerAtt()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getNomerAtt())][0] = rs.getString(36);
	  MasSelector[Integer.parseInt(abit_Srch.getNomerAtt())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getShifrFakulteta()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getShifrFakulteta())][0] = rs.getString(38);
	  MasSelector[Integer.parseInt(abit_Srch.getShifrFakulteta())][1] = "\\qc";
	  masInc++;
	 }

         if(abit_Srch.getAttestat()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getAttestat())][0] = rs.getString(39);
	  MasSelector[Integer.parseInt(abit_Srch.getAttestat())][1] = "\\qc";
	  masInc++;
	 }

	 if(abit_Srch.getDataRojdenija()!=null)
	 {
	  MasSelector[Integer.parseInt(abit_Srch.getDataRojdenija())][0] = rs.getString(40);
	  MasSelector[Integer.parseInt(abit_Srch.getDataRojdenija())][1] = "\\qc";
	  masInc++;
	 }

// Спец-ть зачисления
	 if(abit_Srch.getSpecial8()!=null)
	 {
// Получение Аббревиатуры спец-ти по ее коду
           AbiturientBean abit_TMP = new AbiturientBean();
           stmt_a = conn.prepareStatement("SELECT DISTINCT s.Abbreviatura FROM Spetsialnosti s,Fakultety f WHERE s.KodFakulteta=f.KodFakulteta AND f.KodVuza LIKE ? AND s.KodSpetsialnosti LIKE ?");
           stmt_a.setObject(1,session.getAttribute("kVuza"),Types.INTEGER);
           stmt_a.setObject(2,rs.getString(32),Types.INTEGER);
           rs_a = stmt_a.executeQuery();
           if(rs_a.next())    abit_TMP.setSpecial8(rs_a.getString(1));
           else               abit_TMP.setSpecial8("");

	  MasSelector[Integer.parseInt(abit_Srch.getSpecial8())][0] = abit_TMP.getSpecial8();
	  MasSelector[Integer.parseInt(abit_Srch.getSpecial8())][1] = "\\qc";
	  masInc++;
	 }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

       xxx = 1;
       tabulator = 0;

// Вывод ячейки с номером строки

       report.write("\\b0\\qc\\intbl{"+number+"}\\cell\n");
       number++;
       while(masInc>0)	// Цикл вывода строки таблицы
       {
      	 colIndex++;

    	 if(MasSelector[xxx][0] != null){
    		 Cell cell = cells.get(rowIndex,colIndex);
    		 cell.setValue(MasSelector[xxx][0]);
    		 
    	 }

         if(MasSelector[xxx][0] != null) report.write("\\b0"+MasSelector[xxx][1]+"\\intbl{"+MasSelector[xxx][0]+"}\\cell\n");
         else report.write("\\b0\\intbl\\cell\n");
         xxx++;
         masInc--;
       

       }
       colIndex = 0;

// Оценки Аттестата, Экзамена, Заявленные. Баллы ЕГЭ и признак "Где сдает экзамен"

       for(int ind = 0; ind < col_predm; ind++) {

          curr_code = StringUtil.toInt(MassPredmsForCol[ind][1],-1); // Код предмета

          for(int j = 0; j < mass_index; j++) {

             if(curr_code == StringUtil.toInt(MassOtsForCol[j][0],-1)) {
               stmt_a = conn.prepareStatement("SELECT "+MassOtsForCol[j][2]+" FROM Otsenki,ZajavlennyeShkolnyeOtsenki WHERE Otsenki.KodAbiturienta=ZajavlennyeShkolnyeOtsenki.KodAbiturienta AND Otsenki.KodPredmeta=ZajavlennyeShkolnyeOtsenki.KodPredmeta AND Otsenki.KodPredmeta LIKE ? AND Otsenki.KodAbiturienta LIKE ?");
              System.out.println("SELECT "+MassOtsForCol[j][2]+" FROM Otsenki,ZajavlennyeShkolnyeOtsenki WHERE Otsenki.KodAbiturienta=ZajavlennyeShkolnyeOtsenki.KodAbiturienta AND Otsenki.KodPredmeta=ZajavlennyeShkolnyeOtsenki.KodPredmeta AND Otsenki.KodPredmeta LIKE "+MassOtsForCol[j][0]+" AND Otsenki.KodAbiturienta LIKE "+ rs.getString(1));
               
               stmt_a.setObject(1,MassOtsForCol[j][0],Types.INTEGER);
               stmt_a.setObject(2,rs.getString(1),Types.INTEGER);
               rs_a = stmt_a.executeQuery();
               if(rs_a.next()) report.write("\\intbl\\qc{"+StringUtil.voidFilter(rs_a.getString(1))+"}\\cell\n");
               else report.write("\\intbl\\qc\\cell\n");
             }
          }
       }

       report.write("\\intbl \\row\n");
 }
       
       
       workbook.save("D:\\monitoring.xls");



//########################################################################################
       
       report.write("\\pard\\par\\par\n");
       if(abit_Srch.getSpecial6() != null)	//Вывод подписей
         report.write("\\fs28 \\ql "+abit_Srch.getSpecial6()+"\n");

       report.write("}"); 
	 
       report.close();

       form.setAction(us.getClientIntName("new_rep","crt"));
       return mapping.findForward("rep_brw");
	 
	}//Закрываем "makeRTFOts"
	
        }// Закрываем try
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
        request.setAttribute("abit_Srch", abit_Srch);
        request.setAttribute("abits_Srch", abits_Srch);
        request.setAttribute("predms", predms);
   }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        return mapping.findForward("success");
    }
}
