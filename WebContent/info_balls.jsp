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

<logic:notPresent name='infoBallsAction' scope='request'>
 <logic:redirect forward='info_balls'/>
</logic:notPresent>

<logic:notPresent name="infoBallsForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength

function checkFields()
{
 if(document.forms(0).shifrFakulteta.value == "-")
 {
  alert("Необходимо выбрать Факультет");
  document.forms(0).shifrFakulteta.focus();
  return false;
 }
}

function exec() {
if( document.forms(0).shifrFakulteta!= null && document.forms(0).special1!= null) {
 document.forms(0).shifrFakulteta.selectedIndex=0;
 teamLength = document.forms(0).special1.options.length
 teamTXT = new Array(teamLength)
 teamVAL = new Array(teamLength)
 for(var ind=0;ind<document.forms(0).special1.length;ind++) {
    eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
    eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
 }
fillSelect(document.forms(0).shifrFakulteta.value);
 document.forms(0).shifrFakulteta.focus();
}
if( document.forms(0).shifrFakulteta == null && document.forms(0).special5 != null) {
 document.forms(0).special5.focus();
}
}

function fillSelect(selectCtrl) {
var i,j=0
if(selectCtrl == "-") {
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление знака "-"
   document.forms(0).special1.options[0] = new Option("-")
   document.forms(0).special1.options[0].value = "-"
   document.forms(0).special1.options[0].selected = true
return
}
j=0
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление новых строк
for(i = 0; i < teamLength; i++) {
   if(teamTXT[i].charAt(0) == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     j++
   }
}
// Переход в начало списка
document.forms(0).special1.options[0].selected = true
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="infoBallsForm" property="action">
<bean:define id="action" name="infoBallsForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Сведения по баллам</template:put>
<template:put name="target_name">Сведения по баллам</template:put>
<template:put name="sub_name">Выберите специальность</template:put>
<template:put name='content'>
<BR>
<html:form action="/info_balls?action=viewing" onsubmit="return checkFields();">

<table cols=3 align=center border=0>
<tr><td colspan=3></td></tr>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:select styleClass="select_f1" onchange="fillSelect(this.value)" name="abit_SD" property="shifrFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
  <tr><td><font class="text_10">Специальность:</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="special1" tabindex="2">
          <html:options collection="abit_SD_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Просмотр"/></td>
</html:form>
<html:form method="post" action="/info_balls?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="Выход"/></td>
</html:form>
</tr>
<tr><td height=150></td></tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Сведения по баллам</template:put>
<template:put name="target_name">Сведения по баллам</template:put>
<template:put name='content'>
<BR>
<html:form action="/info_balls?action=report">
  <html:hidden name="abit_SD" property="kodSpetsialnosti"/>

<font class="text_11">
На выбранной специальности всего:&nbsp;<bean:write name="abit_SD" property="special22"/>&nbsp;абитуриента(ов)
</font>
<br><br>
<table cols=8 align=center border=1 cellSpacing=0>
<thead>
<td rowspan=2 align=center><font class="text_th">&nbsp;№&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Аббр. спец.&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Номер личн. дела&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Имя&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Мед.&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Курсы&nbsp;</font></td>
<td colspan=3 align=center><font class="text_th">&nbsp;Предметы&nbsp;/&nbsp;Оценки&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;Сумма&nbsp;</font></td>
<tr>
<logic:iterate id="abit_SD" name="abit_SD_S4" scope='request'>
<td align=center><font class="text_th">&nbsp;<bean:write name="abit_SD" property="predmet"/>&nbsp;</font></td>
</logic:iterate>
</tr>
</thead>
<logic:iterate id="abit_SD" name="abits_SD" scope='request'>
<tr>
<td valign=center align=center><font class="text_10">
  <bean:write name="abit_SD" property="number"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="abbreviatura"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="nomerLichnogoDela"/>&nbsp;</font></td>
  <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="familija"/>&nbsp;</font></td>
  <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="imja"/>&nbsp;</font></td>
  <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="otchestvo"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="shifrMedali"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="shifrKursov"/>&nbsp;</font></td>  
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special5"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special3"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special7"/>&nbsp;</font></td>
  <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special8"/>&nbsp;</font></td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_SD" property="kodAbiturienta">
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
  <td><html:submit styleClass="button" value="Создать отчёт"/></td>
</html:form>
<html:form action="/info_balls.do?action=view">
  <td align=center>&nbsp;&nbsp;<html:submit styleClass="button" value="К настройке"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>

