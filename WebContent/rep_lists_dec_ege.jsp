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

<logic:notPresent name='listsDecEgeAction' scope='request'>
 <logic:redirect forward='lists_dec_ege'/>
</logic:notPresent>

<logic:notPresent name="listsDecEgeForm" property="action">
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

<logic:present name="listsDecEgeForm" property="action">
<bean:define id="action" name="listsDecEgeForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Списки для деканата c баллами ЕГЭ</template:put>
<template:put name="target_name">Списки абитуриентов для деканата c баллами ЕГЭ</template:put>
<template:put name="sub_name">Выберите специальность</template:put>
<template:put name='content'>
<BR>
<html:form action="/lists_dec_ege?action=report" onsubmit="return checkFields();">
<table align=center border=0>
<tr><td><font class="text_10">Бюджетники:</font></td>
<td><html:radio accesskey="б" name="abit_SD" property="priznakSortirovki" tabindex="1" value="budgetniki"/></td>
<tr><td><font class="text_10">Контрактники:</font></td>
<td><html:radio accesskey="к" name="abit_SD" property="priznakSortirovki" tabindex="2" value="kontraktniki"/></td></tr>
</table>
<table cols=3 align=center border=0>
<tr><td colspan=3><hr></td></tr>
<tr><td><font class="text_10">Аттестат:</font></td>
<td>
    <html:select styleClass="select_f1" name="abit_SD" property="special2" tabindex="5">
       <html:option value="all">все</html:option>
       <html:option value="orig">оригинал</html:option>
       <html:option value="copy">копия</html:option>
    </html:select> 
</td>
</tr>
<tr><td><font class="text_10">Рекомендованы&nbsp;к&nbsp;зачислению:</font></td>
<td>
    <html:select styleClass="select_f1" name="abit_SD" property="special3" tabindex="6">
       <html:option value="all">не учитывать</html:option>
       <html:option value="rek">да</html:option>
    </html:select> 
</td>
</tr>
<tr><td><font class="text_10">Учитывать&nbsp;приоритеты:</font></td>
<td>
    <html:select styleClass="select_f1" name="abit_SD" property="special4" tabindex="7">
       <html:option value="all">все</html:option>
       <html:option value="1">1</html:option>
       <html:option value="2">2</html:option>
       <html:option value="3">3</html:option>
       <html:option value="4">4</html:option>
       <html:option value="5">5</html:option>
       <html:option value="6">6</html:option>
    </html:select> 
</td>
</tr>
<tr><td><font class="text_10">Выводить&nbsp;НЛД&nbsp;приоритетов:</font></td>
<td>
    <html:select styleClass="select_f1" name="abit_SD" property="special6" tabindex="8">
       <html:option value="pr1">1-го</html:option>
       <html:option value="all">всех</html:option>
    </html:select> 
</td>
</tr>
<tr><td colspan=3><hr></td></tr>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:select styleClass="select_f1" onchange="fillSelect(this.value)" name="abit_SD" property="shifrFakulteta" tabindex="9">
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
  <tr><td><font class="text_10">Специальность:</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="special1" tabindex="10">
          <html:options collection="abit_SD_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="11" value="Создать отчет"/></td>
</html:form>
<html:form method="post" action="/lists_dec_ege?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="12" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>