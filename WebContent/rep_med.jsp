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

<logic:notPresent name='listsMedAction' scope='request'>
 <logic:redirect forward='lists_med'/>
</logic:notPresent>

<logic:notPresent name="listsMedForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function exec() {

}

</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="listsMedForm" property="action">
<bean:define id="action" name="listsMedForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Списки отличников зачисленных и не зачисленных в ВУЗ</template:put>
<template:put name="target_name">Список отличников по ВУЗу</template:put>
<template:put name="sub_name">Выберите тип списка</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_med?action=report">
<table align=center border=0>
<tr><td><font class="text_10">Поступающие:</font></td>
<td><html:radio accesskey="п" name="abit_SD" property="priznakSortirovki" tabindex="1" value="post"/></td>
<tr><td><font class="text_10">Зачисленные:</font></td>
<td><html:radio accesskey="з" name="abit_SD" property="priznakSortirovki" tabindex="2" value="zach"/></td>
</tr></table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Создать отчет"/></td>
</html:form>
<html:form method="post" action="/rep_med?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>