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

<logic:notPresent name='infoAbitAction' scope='request'>
 <logic:redirect forward='info_abits'/>
</logic:notPresent>

<logic:notPresent name="infoAbitsForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields() {
}

function exec() {
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="infoAbitsForm" property="action">
<bean:define id="action" name="infoAbitsForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Проходной балл по специальностям</template:put>
<template:put name="target_name">Проходной балл по специальностям</template:put>
<template:put name="sub_name">Учитываются все бюджетные и некоторые платные</template:put>
<template:put name='content'>
<BR>
<html:form action="/info_abits?action=report" onsubmit="return checkFields();">
<table cols=3 align=center border=0>
  <tr><td><font class="text_10">&nbsp;Приоритеты&nbsp;специальностей:&nbsp;</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="special1" tabindex="1">
          <html:option value="%">Все</html:option>
          <html:option value="1">1</html:option>
          <html:option value="2">2</html:option>
          <html:option value="3">3</html:option>
          <html:option value="4">4</html:option>
          <html:option value="5">5</html:option>
          <html:option value="6">6</html:option>
      </html:select> 
      </td></tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="2" value="Сформировать"/></td>
</html:form>
<html:form method="post" action="/info_abits?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="3" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>