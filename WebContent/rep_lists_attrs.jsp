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

<logic:notPresent name='listsAttrsAction' scope='request'>
 <logic:redirect forward='lists_attrs'/>
</logic:notPresent>

<logic:notPresent name="listsAttrsForm" property="action">
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

<logic:present name="listsAttrsForm" property="action">
<bean:define id="action" name="listsAttrsForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Списки по атрибутам</template:put>
<template:put name="target_name">Списки абитуриентов по атрибутам</template:put>
<template:put name="sub_name">Задайте параметры просмотра</template:put>
<template:put name='content'>
<BR>
<html:form action="/lists_attrs?action=report" onsubmit="return checkFields();">
  <html:hidden name="abit_SD" property="kodSpetsialnosti"/>
  <html:hidden name="abit_SD" property="bud_Kon"/>
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
<tr><td colspan=3></td><hr></tr>
<tr><td><font class="text_10">С отличиями:</font></td>
<td><html:radio accesskey="м" name="abit_SD" property="priznakSortirovki" tabindex="1" value="medalisti"/></td>
<tr><td><font class="text_10">Контрактники:</font></td>
<td><html:radio name="abit_SD" property="priznakSortirovki" tabindex="2" value="kontraktniki"/></td>
<tr><td><font class="text_10">Договор с предприятием:</font></td>
<td><html:radio name="abit_SD" property="priznakSortirovki" tabindex="3" value="dogovorniki"/></td>
<tr><td><font class="text_10">Целевой прием:</font></td>
<td><html:radio name="abit_SD" property="priznakSortirovki" tabindex="4" value="tspriem"/></td>
<tr><td><font class="text_10">Льготники:</font></td>
<td><html:radio name="abit_SD" property="priznakSortirovki" tabindex="5" value="ligotniki"/></td>
</tr></table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="6" value="Создать отчет"/></td>
</html:form>
<html:form method="post" action="/lists_attrs?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="7" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>