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

<logic:notPresent name='waveFourthAction' scope='request'>
 <logic:redirect forward='rep_wave_fourth'/>
</logic:notPresent>

<logic:notPresent name="waveFourthForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields() {
 if(document.forms(0).kodFakulteta.value == "-")
 {
  alert("Необходимо выбрать Факультет");
  document.forms(0).kodFakulteta.focus();
  return false;
 }
}

function exec() {
   document.forms(0).kodFakulteta.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="waveFourthForm" property="action">
<bean:define id="action" name="waveFourthForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Список абитуриентов ко второму этапу</template:put>
<template:put name="target_name">Список абитуриентов ко второму этапу</template:put>
<template:put name="sub_name">претенденты на оставшиеся после 1-го этапа вакансии</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_wave_fourth?action=report" onsubmit="return checkFields();">
<table align=center border=0>
<tr><td><font class="text_10">Бюджетники:</font></td>
<td><html:radio accesskey="б" name="abit_SD" property="priznakSortirovki" tabindex="1" value="budgetniki"/></td>
<tr><td><font class="text_10">Контрактники:</font></td>
<td><html:radio accesskey="к" name="abit_SD" property="priznakSortirovki" tabindex="2" value="kontraktniki"/></td>
</tr></table>
<table cols=3 align=center border=0>
<tr><td colspan=3><hr></td></tr>
<tr><td><font class="text_10">Факультет:&nbsp;&nbsp;</font></td>
    <td><html:select styleClass="select_f1" name="abit_SD" property="kodFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
        </html:select> 
    </td></tr>
<tr><td><font class="text_10">Учитывать&nbsp;приоритеты:</font></td>
    <td>
       <html:select styleClass="select_f1" name="abit_SD" property="special4" tabindex="2">
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
<tr><td><font class="text_10">Доп.&nbsp;сведения:&nbsp;&nbsp;</font></td>
    <td><html:password name="abit_SD" styleClass="text_f9_short" property="special5" size="16" maxlength="25" tabindex="3"/> 
    </td>
</tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="4" value="Сформировать"/></td>
</html:form>
<html:form method="post" action="/rep_wave_fourth?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="5" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>