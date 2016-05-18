<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='prikazAction' scope='request'>
 <logic:redirect forward='rep_prikaz'/>
</logic:notPresent>

<logic:notPresent name="prikazForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function exec() {
   document.forms(0).nomerPrik.focus();  
}

function exec2() {
   document.forms(0).nomerLichnogoDela.focus();  
}

function checkFields()
{
 if(document.forms(0).nazvanie.value == "")
 {
  alert("Необходимо указать ''Тип приказа''");
  document.forms(0).nazvanie.focus();
  return false;
 }
 if(document.forms(0).nomerPrik.value == "")
 {
  alert("Необходимо задать ''Номер приказа''");
  document.forms(0).nomerPrik.focus();
  return false;
 }
 if(document.forms(0).dataPrik.value == "00.00.0000")
 {
  alert("Необходимо заполнить ''Дата приказа''");
  document.forms(0).dataPrik.focus();
  return false;
 }
}

function help_me(){
 alert("Внимание! Все абитуриенты, входящие в состав удаляемого приказа, исключаются из него."+
       "\n\nПримечание. Чтобы исключить абитуриента из приказа, необходимо щелкнуть указателем мышки на значении ''д'', расположенном в поле ''В приказе''.");
}

function help_me2(){
 alert("Внимание! Чтобы включить абитуриента, зачисленного на две и более специальностей в приказ, необходимо установить признак ''в приказе'' в значение ''в''. Для этого нужно щелкнуть указателем мышки на текущем значении поля ''в приказе'' или воспользоваться возможностью добавления абитуриента в готовый приказ непосредственно перед его печатью."+
       "\n\nПримечание. В большинстве случаев абитуриенты, подавшие документы на несколько специальностей, зачисляются в конечном итоге на одну из них. В связи с этим может потребоваться коррекция признака зачисления."+
       "\n\nСнять признак зачисления можно, кликнув указателем мышки на его текущем значении до появления значения ''н''. Вместе с признаком зачисления изменяется и признак копии аттестата.");
}

function help_me3(){
 alert("Внимание! Значение ''да'', присутствующее в поле ''Требуется уточнение (в приказе)'' указывает на необходимость дополнительной коррекции данных абитуриента, чтобы он смог попасть в приказ. Для этого нужно щелкнуть указателем мышки на соответствующем значении ''да''"+
       "\n\nАбитуриенты, имеющие значение ''да'' в поле ''Требуется уточнение (в приказе)'', в приказ не попадают."+
       "\n\nПримечание. Значение ''в'' означает, что абитуриент зачислен на две и более специальности и будет включен в приказ.");
}

function help_me4(){
 alert("Внимание! Каждому добавленному здесь абитуриенту устанавливается признак ''в приказе'' равный ''+''. При этом проверка на возможность внесения в приказ абитуриента, ранее попавшего в приказ не производится."+
       "\n\nПримечание. Чтобы исключить добавленного здесь абитуриента из приказа, необходимо перейти к форме ''Подготовка приказа'' и щелкнуть указателем мышки на значении ''+'', расположенном в поле ''В приказе'' напротив Фамилии нужного абитуриента.");
}

function confirmation(){
  if(confirm('Удалить выбранный Приказ?'))
   {
     return true;
   }
  else return false; 
}

</SCRIPT>

<logic:present name="prikazForm" property="action">
<bean:define id="action" name="prikazForm" property="action"/>

<%-----------------------------------------------------------------%>
<%------------------ Параметры подготовки приказа -----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<body onLoad="exec();"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Приказы на зачисление</template:put>
<template:put name="target_name">Приказы на зачисление</template:put>
<template:put name="sub_name">По факультету</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_prikaz?action=viewing" onsubmit="return checkFields();">
<table cols=3 align=center border=0>
  <tr><td><font class="text_10">Тип приказа:</font></td>
      <td><html:text accesskey="н" name="abit_SD" styleClass="text_f10" property="nazvanie"
                     maxlength="120" size="16" tabindex="1" value=" на зачисление"/>
  <tr><td><font class="text_10">Номер приказа:</font></td>
      <td><html:text accesskey="н" name="abit_SD" styleClass="text_f10_short" property="nomerPrik"
                     maxlength="12" size="6" tabindex="2" value=""/>
  <tr><td><font class="text_10">Дата приказа:</font></td>
      <td><html:text accesskey="д" name="abit_SD" styleClass="text_f10_short" property="dataPrik"
                     maxlength="10" size="10" tabindex="3"/>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:select styleClass="select_f1"  name="abit_SD" property="kodFakulteta" tabindex="4">
           <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
<tr><td colspan=3><hr></td></tr>
  <tr><td><font class="text_10">Бюджетники:</font></td>
      <td><html:radio styleClass="radio_b1" accesskey="б" name="abit_SD" property="priznakSortirovki" tabindex="5" value="budgetniki"/></td>
  <tr><td><font class="text_10">Контрактники:</font></td>
      <td><html:radio styleClass="radio_b1" accesskey="к" name="abit_SD" property="priznakSortirovki" tabindex="6" value="kontraktniki"/></td>
  </tr>
<tr><td colspan=3><hr></td></tr>
  <tr><td><font class="text_10">Описание:</font></td>
      <td><html:textarea accesskey="о" name="abit_SD" styleClass="text_f10" property="description"
                     cols="30" rows="3" tabindex="8" value=""/>
      </td>
  </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="9" value="Подготовить приказ"/></td>
</html:form>
<html:form method="post" action="/rep_prikaz?action=priks">
  <td><html:submit styleClass="button" tabindex="10" value="Просмотр приказов"/></td>
</html:form>
<html:form method="post" action="/rep_prikaz?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="11" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Подготовка нового приказа --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Подготовка нового приказа</template:put>
<template:put name="target_name">Подготовка нового приказа</template:put>
<template:put name="sub_name"><bean:write name="abit_SD" property="nazvanie"/> №
     <bean:write name="abit_SD" property="nomerPrik"/>&nbsp;&nbsp;от 
     <bean:write name="abit_SD" property="dataPrik"/>
</template:put>
<template:put name='content'>
<html:form action="/rep_prikaz?action=create">
 <html:hidden name="abit_SD" property="kodPrikaza"/>
 <html:hidden name="abit_SD" property="kodFakulteta"/>
<font class="text_11">Всего абитуриентов:&nbsp;<bean:write name="abit_SD" property="special22"/></font>
<br><font class="text_11">Описание:&nbsp;<bean:write name="abit_SD" property="description"/></font>
<br><br>
<table cols=5 align=center border=1 cellSpacing=0>
<thead>
<tr>
  <td height="30" align=center><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Номер личн. д.&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Имя&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
  <td width="120" align=center><font class="text_th">&nbsp;Требуется&nbsp; &nbsp;уточнение&nbsp; &nbsp;(в&nbsp;приказе)&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_SD" name="abits_SD" scope='request'>
<tr valign=middle>
<td align=center><font class="text_10">&nbsp;
      <bean:write name="abit_SD" property="number"/>&nbsp;</font></td>
  <td align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="nomerLichnogoDela"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abit_SD" property="familija"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abit_SD" property="imja"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abit_SD" property="otchestvo"/>&nbsp;</font></td>
  <td align=center>&nbsp;
<logic:notEqual name="abit_SD" property="in_Prik" value="">
                   <a href="<%="rep_prikaz.do?action=info&kAb="+((abit.bean.AbiturientBean)abit_SD).getKodAbiturienta()%>" class="link_hov_blue">
                             <bean:write name="abit_SD" property="in_Prik"/></a>
</logic:notEqual>
  &nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_SD" property="nomerLichnogoDela">
<tr>
  <td align=center valign=center colspan=11 rowspan=3>
  <p align=center><font class="text_11">
     В&nbsp;базе&nbsp;данных&nbsp;нет&nbsp;сведений&nbsp;о&nbsp;выбранных&nbsp;абитуриентах</font></td>
  </td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr><td height=15></td></tr>
<tr>
  <td><html:submit styleClass="button" value="Создать приказ"/></td>
</html:form>
<html:form action="/rep_prikaz.do?action=view">
  <td align=center>&nbsp;<html:button styleClass="button" onclick="help_me3();" property="help" value="Справка"/></td>
</html:form>
<html:form action="/rep_prikaz.do?action=view">
  <td align=center>&nbsp;&nbsp;<html:submit styleClass="button" value="Назад"/></td>
</html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Просмотр таблицы приказов --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="priks">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Приказы на зачисление</template:put>
<template:put name="target_name">Приказы на зачисление</template:put>
<template:put name="sub_name">Список подготовленных приказов</template:put>
<template:put name='content'>
<logic:present name="mess" property="message">
<p align=center>
<font class="text_th"><bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/></font>
</p>
</logic:present>
<html:form action="/rep_prikaz?action=view">
 <html:hidden name="abit_SD" property="kodPrikaza"/>
<table cols=5 align=center border=1 cellSpacing=0>
<thead>
<tr>
  <td height="30" align=center width="60"><font class="text_th">&nbsp;Номер&nbsp; &nbsp;приказа&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Тип&nbsp;приказа&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Дата приказа&nbsp;</font></td>
  <td align=center width="100"><font class="text_th">&nbsp;Количество &nbsp;абитуриентов&nbsp; &nbsp;в приказе&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Описание&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abt_SD" name="abits_SD" scope='request'>
<tr valign=middle>
  <td align="center">&nbsp;<html:link href="rep_prikaz.do?action=view_prk" styleClass="link_hov_blue" paramName="abt_SD" paramId="kodPr" paramProperty="kodPrikaza">
                             <bean:write name="abt_SD" property="nomerPrik"/></html:link>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="nazvanie"/>&nbsp;</font></td>
  <td align="center"><font class="text_10">&nbsp;<bean:write name="abt_SD" property="dataPrik"/>&nbsp;</font></td>
  <td align="center"><font class="text_10">&nbsp;<bean:write name="abt_SD" property="special2"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="description"/>&nbsp;</font></td>
</tr>
</logic:iterate>
<tr>
  <td align="right" colspan="3"><font class="text_10">Всего:&nbsp;&nbsp;</font></td>
  <td align="center"><font class="text_10"><bean:write name="abit_SD" property="amount"/></font></td>
  <td align="center"><font class="text_10">&nbsp;</font></td>
</tr>
<logic:notPresent name="abt_SD" property="nomerPrik">
<tr>
  <td align=center valign=center colspan=11 rowspan=3>
  <p align=center><font class="text_11">
     В&nbsp;базе&nbsp;данных&nbsp;нет&nbsp;сведений&nbsp;о&nbsp;приказах</font></td>
  </td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr><td height=15></td></tr>
<tr>
  <td align=center>&nbsp;&nbsp;<html:submit styleClass="button" value="Подготовка приказа"/></td>
</html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Просмотр готового приказа --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view_prik">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Готовый к печати приказ</template:put>
<template:put name="target_name">Готовый к печати приказ</template:put>
<template:put name="sub_name"><bean:write name="abit_SD" property="nazvanie"/> №
     <bean:write name="abit_SD" property="nomerPrik"/>&nbsp;&nbsp;от 
     <bean:write name="abit_SD" property="dataPrik"/>
</template:put>
<template:put name='content'>
<html:form action="/rep_prikaz?action=report">
 <html:hidden name="abit_SD" property="kodPrikaza"/>
<font class="text_11">Всего абитуриентов:&nbsp;<bean:write name="abit_SD" property="special22"/></font>
<br><font class="text_11">Описание:&nbsp;<bean:write name="abit_SD" property="description"/></font>
<br><font class="text_11">Форма:&nbsp;</font>
    <html:select styleClass="select_f1"  name="abit_SD" property="forma_Pr">
      <html:option value="forma1">первая</html:option>
      <html:option value="forma2">вторая</html:option>
    </html:select>
<br><br>
<table cols=5 align=center border=1 cellSpacing=0>
<thead>
<tr>
  <td height="30" align=center><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Номер личн. д.&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Имя&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Спец.&nbsp;</font></td>
  <td align=center width="100"><font class="text_th">&nbsp;Номер&nbsp; &nbsp;платн.&nbsp;дог.&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;В&nbsp;&nbsp;приказе&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abt_SD" name="abits_SD" scope='request'>
<tr valign=middle>
<td align=center><font class="text_10">&nbsp;
      <bean:write name="abt_SD" property="number"/>&nbsp;</font></td>
  <td align=center><font class="text_10">&nbsp;<bean:write name="abt_SD" property="nomerLichnogoDela"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="familija"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="imja"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="otchestvo"/>&nbsp;</font></td>
  <td align="center"><font class="text_10">&nbsp;<bean:write name="abt_SD" property="special2"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="nomerPlatnogoDogovora"/>&nbsp;</font></td>
  <td align=center>&nbsp;<a href="<%="rep_prikaz.do?action=rePrik&fin&kodPr="+((abit.bean.AbiturientBean)abt_SD).getKodPrikaza()+"&kAb="+((abit.bean.AbiturientBean)abt_SD).getKodAbiturienta()%>" class="link_hov_blue">
                             <bean:write name="abt_SD" property="in_Prik"/></a>&nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abt_SD" property="nomerLichnogoDela">
<tr>
  <td align=center valign=center colspan=11 rowspan=3>
  <p align=center><font class="text_11">
     В&nbsp;базе&nbsp;данных&nbsp;нет&nbsp;сведений&nbsp;о&nbsp;выбранных&nbsp;абитуриентах</font></td>
  </td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr><td height=15></td></tr>
<tr>
  <td><html:submit styleClass="button" value="Создать приказ"/></td>
</html:form>
<html:form action="/rep_prikaz.do?action=add_abit">
 <html:hidden name="abit_SD" property="kodPrikaza"/>
  <td align=center>&nbsp;<html:submit styleClass="button" value="Доб. Абит."/></td>
</html:form>
<html:form action="/rep_prikaz.do?action=del_prik">
 <html:hidden name="abit_SD" property="kodPrikaza"/>
  <td align=center>&nbsp;<html:submit onclick="return confirmation();" styleClass="button" value="Удалить"/></td>
</html:form>
<html:form action="/rep_prikaz.do?action=priks">
  <td align=center>&nbsp;<html:button styleClass="button" onclick="help_me();" property="help" value="Справка"/></td>
</html:form>
<html:form action="/rep_prikaz.do?action=priks">
  <td align=center>&nbsp;<html:submit styleClass="button" value="Назад к списку"/></td>
</html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------- Просмотр дополнительной информации по абитуриенту -------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="dop_info">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Дополнительные сведения по абитуриенту</template:put>
<template:put name="target_name">Дополнительные сведения по абитуриенту</template:put>
<template:put name="sub_name">подавшему документы на несколько специальностей</template:put>
<template:put name='content'>
<html:form action="/rep_prikaz?action=viewing">
 <html:hidden name="abit_SD" property="kodAbiturienta"/>
<br>
<table cols=5 align=center border=1 cellSpacing=0>
<thead>
<tr>
  <td height="30" align=center><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td width="60" align=center><font class="text_th">&nbsp;Номер&nbsp; &nbsp;личного&nbsp; &nbsp;дела&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Атт.&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Имя&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Спец.&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;Зачислен&nbsp;</font></td>
  <td align=center width="100"><font class="text_th">&nbsp;Номер&nbsp; &nbsp;платн.&nbsp;дог.&nbsp;</font></td>
  <td align=center><font class="text_th">&nbsp;В&nbsp;&nbsp;приказе&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abt_SD" name="abits_SD" scope='request'>
<tr valign=middle>
<td align=center><font class="text_10">&nbsp;
      <bean:write name="abt_SD" property="number"/>&nbsp;</font></td>
  <td align=center><font class="text_10">&nbsp;<bean:write name="abt_SD" property="nomerLichnogoDela"/>&nbsp;</font></td>
  <td align="center"><font class="text_10">&nbsp;<bean:write name="abt_SD" property="tipDokSredObraz"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="familija"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="imja"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="otchestvo"/>&nbsp;</font></td>
  <td align="center"><font class="text_10">&nbsp;<bean:write name="abt_SD" property="abbreviatura"/>&nbsp;</font></td>
  <td align=center>&nbsp;<a href="<%="rep_prikaz.do?action=rePrinjat&kAb="+((abit.bean.AbiturientBean)abt_SD).getKodAbiturienta()%>" class="link_hov_blue">
                             <bean:write name="abt_SD" property="prinjat"/></a>&nbsp;</td>
  <td><font class="text_10">&nbsp;<bean:write name="abt_SD" property="nomerPlatnogoDogovora"/>&nbsp;</font></td>
  <td align=center>&nbsp;<a href="<%="rep_prikaz.do?action=rePrik&ainf&kAb="+((abit.bean.AbiturientBean)abt_SD).getKodAbiturienta()%>" class="link_hov_blue">
                             <bean:write name="abt_SD" property="in_Prik"/></a>&nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abt_SD" property="nomerLichnogoDela">
<tr>
  <td align=center valign=center colspan=11 rowspan=3>
  <p align=center><font class="text_11">
     В&nbsp;базе&nbsp;данных&nbsp;нет&nbsp;сведений&nbsp;о&nbsp;выбранных&nbsp;абитуриентах</font></td>
  </td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr><td height=15></td></tr>
<tr>
  <td align=center>&nbsp;<html:button styleClass="button" onclick="help_me2();" property="help" value="Справка"/></td>
  <td align=center>&nbsp;<html:submit styleClass="button" value="Назад к подг. прик."/></td>
</html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%------------------------------------------------%>
<%------- Добавление абитуриентов в приказ -------%>
<%------------------------------------------------%>
<logic:equal name="action" value="add_abits">
<body onLoad="exec2();">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Добавление абитуриента в приказ</template:put>
<template:put name="target_name">Добавление абитуриента в приказ</template:put>
<template:put name="sub_name"><bean:write name="abit_SD" property="nazvanie"/> №
     <bean:write name="abit_SD" property="nomerPrik"/>&nbsp;&nbsp;от 
     <bean:write name="abit_SD" property="dataPrik"/>
</template:put>
<template:put name='content'>
<p align="center">
<font class="text_11">Всего абитуриентов:&nbsp;<bean:write name="abit_SD" property="special22"/></font>
<br><font class="text_11">Описание:&nbsp;<bean:write name="abit_SD" property="description"/></font>
<br>
<logic:present name="mess" property="message">
<br><font class="text_th"><bean:write name="mess" property="status"/><br><bean:write name="mess" property="message"/></font>
</p>
</logic:present>
<html:form action="/rep_prikaz?action=add_abit&add">
 <html:hidden name="abit_SD" property="kodPrikaza"/>
<table align=center border=0 cellSpacing=0>
<tr valign=middle>
<td align=center><font class="text_10">&nbsp;Номер&nbsp;личного&nbsp;дела:&nbsp;</font></td>
  <td align=center>&nbsp;<html:text name="abt_SD" styleClass="text_f11_short" property="nomerLichnogoDela" maxlength="12" size="6" value=""/>&nbsp;</td>
                             <bean:write name="abit_SD" property="in_Prik"/></a>&nbsp;</td>
</tr>
</table>
<table align=center border=0>
<tr><td height=15></td></tr>
<tr>
  <td align=center>&nbsp;<html:submit styleClass="button" value="Добавить"/></td>
  <td align=center>&nbsp;<html:button styleClass="button" onclick="help_me4();" property="help" value="Справка"/></td>
</html:form>
<html:form action="/rep_prikaz?action=view_prk">
 <html:hidden name="abit_SD" property="kodPrikaza"/>
  <td align=center>&nbsp;<html:submit styleClass="button" value="Назад к подг. прик."/></td>
</html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>