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

<logic:notPresent name='forma_8_Action' scope='request'>
 <logic:redirect forward='rep_forma_8'/>
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
<template:put name='title'>Сведения о средних баллах ЕГЭ по предметам вступительных испытаний</template:put>
<template:put name="target_name">Сведения о средних баллах ЕГЭ по предметам</template:put>
<template:put name="sub_name">Задайте необходимые параметры</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_forma_8?action=report">
<table align=center border=0>
<tr><td><font class="text_10">Этап&nbsp;зачисления:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="special2" tabindex="1">
      <html:option value="0">не учитывать</html:option>
      <html:option value="%">все этапы</html:option>
      <html:option value="1">30 июля</html:option>
      <html:option value="2">5 августа</html:option>
      <html:option value="3">10 августа</html:option>
      <html:option value="4">17 августа</html:option>
      <html:option value="д">иито, юк и т.п.</html:option>
    </html:select>
</td>
</tr>
<tr><td><font class="text_10">Учитывать&nbsp;нулевые&nbsp;баллы:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="special1" tabindex="2">
      <html:option value="1">да</html:option>
      <html:option value="0">нет</html:option>
    </html:select>
</td>
</tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Сформировать отчет"/></td>
</html:form>
<html:form method="post" action="/rep_forma_8?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>