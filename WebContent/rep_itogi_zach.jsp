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

<logic:notPresent name='itogi_Zach_Action' scope='request'>
 <logic:redirect forward='rep_itogi_zach'/>
</logic:notPresent>

<logic:notPresent name="forma_2_Form" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function exec() {

}

</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="forma_2_Form" property="action">
<bean:define id="action" name="forma_2_Form" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Информация об итогах зачисления на первый курс</template:put>
<template:put name="target_name">Информация об итогах зачисления</template:put>
<template:put name="sub_name">Выберите исходные данные</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_itogi_zach?action=report">
<table align=center border=0>
<tr><td><font class="text_10">Факультеты&nbsp;с:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="special1" tabindex="1">
      <html:option value="1">очной</html:option>
      <html:option value="2">заочной</html:option>
      <html:option value="3">иито</html:option>
      <html:option value="4">дистанционной</html:option>
    </html:select>
</td>
<td><font class="text_10">&nbsp;формой&nbsp;обучения&nbsp;</font></td>
</tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="2" value="Сформировать отчет"/></td>
</html:form>
<html:form method="post" action="/rep_itogi_zach?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="3" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>