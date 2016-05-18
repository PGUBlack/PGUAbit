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

<logic:notPresent name='shfrLichDelAction' scope='request'>
 <logic:redirect forward='rep_sld'/>
</logic:notPresent>

<logic:notPresent name="shfrLichDelForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength
var valid = "1234567890"
var sum = 0
var temp

function checkFields()
{
 if(document.forms(0).shifrFakulteta.value == "-")
 {
  alert("Необходимо выбрать Факультет");
  document.forms(0).shifrFakulteta.focus();
  return false;
 }
 if(document.forms(0).special2.value == "0000" || document.forms(0).special2.value == "")
 {
  alert("Необходимо задать начальный шифр личного дела");
  document.forms(0).special2.focus();
  return false;
 }
sum = 0;
for (var i=0; i<document.forms(0).special2.value.length; i++) {
  temp = "" + document.forms(0).special2.value.substring(i, i+1)
  if(valid.indexOf(temp) == "-1") {
    alert("''Шифр личного дела'' может включать только цифры")
    document.forms(0).special2.focus()
    return false
  }
  sum += eval(temp)
}
if(sum != 10 || document.forms(0).special2.value.length < 4) {
  alert("Цифры шифра личного дела в сумме должны быть равны 10\n и их количество должно быть не менее 4")
  document.forms(0).special2.focus()
  return false
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

<logic:present name="shfrLichDelForm" property="action">
<bean:define id="action" name="shfrLichDelForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Списки шифров личных дел</template:put>
<template:put name="target_name">Списки шифров личных дел</template:put>
<template:put name="sub_name">Выберите параметры</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_sld?action=getRep" onsubmit="return checkFields();">
<table cols=3 align=center border=0>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:select styleClass="select_f1" onchange="fillSelect(this.value)" name="abit_SD" property="shifrFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
  <tr><td><font class="text_10">Специальность:</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="special1" tabindex="2">
          <html:options collection="abit_SD_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
  <tr><td colspan=3><hr></td></tr>
  <tr><td><font class="text_10">Начальный&nbsp;НЛД:&nbsp;</font></td>
      <td><html:text name="abit_SD" property="special2" value="0000" maxlength="8" tabindex="3" styleClass="text_10_short"/></td>
  <tr><td><font class="text_10">Количество&nbsp;шифров:&nbsp;</font></td>
      <td><html:text name="abit_SD" property="special3" value="240" maxlength="4" tabindex="4" styleClass="text_10_short"/></td>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="5" value="Сформировать"/></td>
</html:form>
<html:form method="post" action="/rep_sld?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="6" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>