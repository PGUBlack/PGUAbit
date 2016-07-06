<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<%--------------------------------%>
<%--------------------------------%>
  <table border="0" width="100%" height="100%" cellSpacing="0" cellPadding="0" class="text">
   <tr><td colspan="2" vAlign="top">

<%--------------------------------%>
<%-- 1я ТАБЛИЦА (ЛИЧНЫЕ ДАННЫЕ) --%>
<%--------------------------------%>
   <table width="100%" border="1" cellSpacing="0" cellPadding="0" frame="BOX" class="text">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle" ><font class="text_th9">&nbsp;ЛИЧНЫЕ&nbsp;ДАННЫЕ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Фамилия:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="familija" size="32" maxlength="30" />
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Имя:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="imja" size="24" maxlength="30" />
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Отчество:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="otchestvo" size="28" maxlength="30" />
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Гражданство:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="grajdanstvo" size="52" maxlength="200" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Место&nbsp;рождения:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="mestoRojdenija" size="52" maxlength="200" />
           </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Дата&nbsp;рождения:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dataRojdenija" 
                      maxlength="10" size="10" /></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Пол:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f1" name="abit_A" property="pol" >
           <html:option value="м">муж</html:option>
           <html:option value="ж">жен</html:option>
           </html:select>
         </td>
     </tr>
     
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Тип&nbsp;документа:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f1" name="abit_A" property="tipDokumenta" >
           <html:option value="п">Паспорт</html:option>
           <html:option value="з">Загран.Паспорт</html:option>
           <html:option value="и">Паспорт иностранного гражданина</html:option>
           <html:option value="с">Справка</html:option>
           <html:option value="д">Другое</html:option>
           </html:select>&nbsp;&nbsp;&nbsp;
           серия:&nbsp;<html:text name="abit_A" styleClass="text_f9_short" property="seriaDokumenta" size="6" maxlength="20" />&nbsp;&nbsp;
           №:&nbsp;<html:text name="abit_A" styleClass="text_f9_short" property="nomerDokumenta" size="14" maxlength="40"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Кем&nbsp;выдан:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="kemVydDokument" size="52" maxlength="100" />&nbsp;&nbsp;
            Код&nbsp;подразделения:&nbsp;<html:text name="abit_A" styleClass="text_f9_short" property="zajavlen" size="8" maxlength="8"/></td></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Дата&nbsp;выдачи:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dataVydDokumenta" size="10" maxlength="10" /></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Житель&nbsp;Республики&nbsp;Крым:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:select styleClass="select_f2" name="abit_A" property="kodLgot">
            <html:option value="0">нет</html:option>
            <html:option value="1">да</html:option>
        </html:select>
        </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="2" colspan="2"></td></tr>
   <tr><td colspan='2' vAlign="top">

<%-------------------------------------%>
<%-- 2я ТАБЛИЦА (АДРЕСА ПРОПИСКИ)    --%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;МЕСТО&nbsp;РЕГИСТРАЦИИ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
    <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Страна:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="stranaChange();" styleClass="select_f2" name="abit_A" property="nazv_DipBak" tabindex="13">
            <html:options collection="nationalityList" property="name" />
           </html:select>
         
        
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Область:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="regionChange();" name="abit_A" styleClass="select_f1" style="width:550px"  property="nazv_DipSpec" tabindex="14">
           <html:option value="-"/>
           <html:options collection="abit_A_Kladr" property="special27" labelProperty="special28"/>
          </html:select>
         
          
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Район:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange='rajonChange();' name="abit_A" styleClass="select_f1" style="width:550px" property="need_Spo" tabindex="15">
           <html:options collection="abit_A_Rajon" property="special27" labelProperty="special28"/>
         
          </html:select>
         
         
         
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Насел.&nbsp;пункт:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         
          <html:select onchange="gorodChange();" name="abit_A" styleClass="select_f1" style="width:550px" property="gorod_Prop" styleId="gorod_Prop_id1" tabindex="16">
           <html:options collection="abit_A_Punkt" property="special27" labelProperty="special28"/>
           </html:select>
          <html:text name="abit_A" styleClass="text_f9_short" property="gorod_Prop" size="30" maxlength="30" styleId = "foreignPunkt" value="" tabindex="17" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Улица/&nbsp;просп.:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select name="abit_A" styleClass="select_f1" property="ulica_Prop" style="width:550px" tabindex="16">
           <html:options collection="abit_A_Ulica" property="ulica_Prop" />
           </html:select>
          <html:text name="abit_A" styleClass="text_f9_short" property="ulica_Prop" size="30" maxlength="30" styleId = "foreignUlica" value="" tabindex="17" />
         
         
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Дом&nbsp;/&nbsp;корпус:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dom_Prop" size="4" maxlength="6"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Кварт.&nbsp;/&nbsp;комн.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="kvart_Prop" size="3" maxlength="6"/>
         </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="2" colspan="2"></td></tr>
   <tr><td colspan="2" vAlign="top">
<%-------------------------------------%>
<%-- 3я ТАБЛИЦА (ОБРАЗОВАНИЕ)        --%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;СВЕДЕНИЯ&nbsp;ОБ&nbsp;ОБРАЗОВАНИИ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Год&nbsp;получения&nbsp;образования:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="godOkonchanijaSrObrazovanija" size="4" maxlength="4"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Страна:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="stranaObrChange();" styleClass="select_f2" name="abit_A" property="traineeship" tabindex="22">
            <html:options collection="nationalityList" property="name" />
           </html:select>
          
     </tr>
     
        <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Область:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="regionEduChange();" name="abit_A" styleClass="select_f1" style="width:550px" property="nazvanieOblasti" tabindex="23">
           <html:option value="-"/>
           <html:options collection="abit_A_Kladr" property="special27" labelProperty="special28"/>
          </html:select>
         
          
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Район:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange='rajonEduChange();' name="abit_A" styleClass="select_f1" property="nazvanieRajona" style="width:550px" tabindex="24">
            <html:options collection="obr_A_Rajon" property="special27" labelProperty="special28"/>
           </html:select>
                
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Насел.&nbsp;пункт:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         
          <html:select name="abit_A" styleClass="select_f1" style="width:550px" property="nazvanie" tabindex="25">
            <html:options collection="obr_A_Punkt" property="special27" labelProperty="special28"/>
           <html:text name="abit_A" styleClass="text_f9_short" property="nazvanie" size="30" maxlength="30" styleId = "foreignObrPunkt" value="" tabindex="26" />
       
        
          </html:select>
         
         
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Тип&nbsp;оконченного&nbsp;заведения:&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="tipOkonchennogoZavedenija">
            <html:option value="ш">школа</html:option>
            <html:option value="в">вечерняя школа</html:option>
            <html:option value="п">пту</html:option>
            <html:option value="т">техникум</html:option>
            <html:option value="к">колледж</html:option>
            <html:option value="у">вуз</html:option>
           </html:select>&nbsp;&nbsp;&nbsp;<font class="text_9">№:</font>
           <html:text name="abit_A" styleClass="text_f9_short" property="nomerShkoly" size="4" maxlength="5"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Название&nbsp;уч.&nbsp;заведения:&nbsp;&nbsp;</td>
         <td valign=center>
           <html:select styleClass="select_f2" onchange="autoInit7();" name="abit_A" property="kodZavedenija">
            <html:options collection="abit_A_S8" property="kodZavedenija" labelProperty="sokr" />
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Вид&nbsp;документа&nbsp;об&nbsp;образ.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="vidDokSredObraz">
            <html:option value="Аттестат ООО">аттестат ООО(9кл.)</html:option>
            <html:option value="Аттестат ООО с отличием">аттестат ООО с отличием</html:option>
            <html:option value="Аттестат СОО">аттестат СОО(11кл.)</html:option>
            <html:option value="Аттестат СОО с отличием">аттестат СОО с отличием</html:option>
            <html:option value="Диплом бакалавра">диплом бакалавра</html:option>
            <html:option value="Диплом бакалавра с отличием">диплом бакалавра с отличием</html:option>
            <html:option value="Диплом специалиста">диплом специалиста</html:option>
            <html:option value="Диплом специалиста с отличием">диплом специалиста с отличием</html:option>
            <html:option value="Диплом магистра">диплом магистра</html:option>
            <html:option value="Диплом магистра с отличием">диплом магистра с отличием</html:option>
            <html:option value="Диплом СПО">диплом СПО</html:option>
            <html:option value="Диплом СПО с отличием">диплом СПО с отличием</html:option>
            <html:option value="Диплом НПО">диплом НПО</html:option>
            <html:option value="Диплом НВПО">диплом НВПО</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Номер&nbsp;аттестата:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">&nbsp; №:&nbsp;<html:text name="abit_A" styleClass="text_f9_short" property="nomerAtt" size="14" maxlength="20"/>
         </td>
     </tr>
<tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Признак&nbsp;документа&nbsp;ориг.&nbsp;(копия):&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="tipDokSredObraz">
            <html:option value="-"/>
            <html:option value="о">оригинал</html:option>
            <html:option value="к">копия</html:option>
           </html:select>
         </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
 </table>
 