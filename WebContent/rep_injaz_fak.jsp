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

<logic:notPresent name='injazFakAction' scope='request'>
 <logic:redirect forward='rep_injaz_fak'/>
</logic:notPresent>

<logic:notPresent name="injazFakForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength
var extream
function enterSelect() 
{
document.forms(0).special1.options[document.forms(0).special1.selectedIndex].value = document.forms(0).special1.options[document.forms(0).special1.selectedIndex].text+extream
}
function checkFields()
{
 if(document.forms(0).shifrFakulteta.value == "-")
 {
  alert("Необходимо выбрать Факультет!");
  document.forms(0).shifrFakulteta.focus();
  return false;
 } else if(document.forms(0).special1.options[0].text == "-")
        {
 	 alert("Либо на выбранном факультете нет абитуриентов\nсдающих экзамен по иностранноу языку,\nлибо дата экзамена не назначена.");
	 document.forms(0).shifrFakulteta.focus();
	 return false;
	}
}

function exec() {
if( document.forms(0).shifrFakulteta!= null &&
    document.forms(0).special1!= null) {
 document.forms(0).shifrFakulteta.selectedIndex=0;
 teamLength = document.forms(0).special1.options.length
 teamTXT = new Array(teamLength)
 teamVAL = new Array(teamLength)
 for(var ind=0;ind<document.forms(0).special1.length;ind++) {
    eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
    eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
 }
fillSelect(document.forms(0).shifrFakulteta.value);
}
  fak.style.display = "block";
  dat.style.display = "block";
}

function fillSelect(selectCtrl) {
extream = selectCtrl
var i,j,priz=0
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
   if(teamVAL[i] == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     priz++
     j++
   }
}
// Переход в начало списка
if(priz == 0)
{
  document.forms(0).special1.options[0] = new Option("-")
  document.forms(0).special1.options[0].value = "-"
  document.forms(0).special1.options[0].selected = true

} else { document.forms(0).special1.options[0].selected = true }
}
</SCRIPT>

<logic:present name="injazFakForm" property="action">
<bean:define id="action" name="injazFakForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Распределение по иностранному языку</template:put>
<template:put name="target_name">Распределение по иностранному языку</template:put>
<template:put name="sub_name">Выберите дату экзамена</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_injaz_fak?action=viewing&offset" onsubmit="return checkFields();">
<table cols=3 align=center border=0>
<tr><td colspan=3></td></tr>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td id="fak" style="display:none"><html:select styleClass="select_f1" onchange="fillSelect(this.value)" name="abit_SD" property="shifrFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
  <tr><td><font class="text_10">Дата экзамена:</font></td>
      <td id="dat" style="display:none"><html:select styleClass="select_f1" name="abit_SD" property="special1" tabindex="2">
          <html:options collection="abit_SD_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" onclick="enterSelect()" tabindex="3" value="Просмотр"/></td>
</html:form>
<html:form method="post" action="/rep_injaz_fak?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="Выход"/></td>
</html:form>
</tr>
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
<template:put name='title'>Списки по факультетам</template:put>
<template:put name="target_name">Распределение абитуриентов по ин. яз. на факультете</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_injaz_fak?action=report">
  <html:hidden name="abit_SD" property="priznakSortirovki"/>
<font class="text_11">Факультет:&nbsp;<font class="text_th"><bean:write name="abit_SD" property="special6"/>&nbsp;</font>
<br>
Дата проведения экзамена:&nbsp;<font class="text_th"><bean:write name="abit_SD" property="special7"/>&nbsp;</font>
</font>
<br><br>
<table cols=8 align=center border=1 cellSpacing=0>
<thead>
<tr>
  <td rowspan=2 align=center><font class="text_th">&nbsp;Группа&nbsp;</font></td>
  <td rowspan=2 align=center><font class="text_th">&nbsp;Английский&nbsp;</font></td>
  <td rowspan=2 align=center><font class="text_th">&nbsp;Немецкий&nbsp;</font></td>
  <td rowspan=2 align=center><font class="text_th">&nbsp;Французский&nbsp;</font></td>
  <td rowspan=2 align=center><font class="text_th">&nbsp;Прочие&nbsp;</font></td>
</tr>
</thead>
<tr>
<logic:iterate id="abit_SD" name="abit_SD_S4" scope='request'>
<td align=center><font class="text_th">&nbsp;<bean:write name="abit_SD" property="predmet"/>&nbsp;</font></td>
</logic:iterate>
</tr>

<logic:iterate id="abit_SD" name="abits_SD" scope='request'>
<tr>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special1"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special2"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special3"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special4"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="special5"/>&nbsp;</font></td>
 </tr>
</logic:iterate>
<logic:notPresent name="abit_SD" property="special1">
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
<html:form action="/rep_injaz_fak.do?action=view">
  <td align=center>&nbsp;&nbsp;<html:submit styleClass="button" value="Назад"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>