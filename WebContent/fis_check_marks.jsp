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

<logic:notPresent name='fisCheckMarksAction' scope='request'>
 <logic:redirect forward='fis_check_marks'/>
</logic:notPresent>

<logic:notPresent name="fisConnectForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<logic:present name="fisConnectForm" property="action">
<bean:define id="action" name="fisConnectForm" property="action"/>

<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Парсинг оценок ЕГЭ</template:put>
<template:put name="target_name">Парсинг оценок ЕГЭ</template:put>
<template:put name="sub_name">Парсинг оценок ЕГЭ</template:put>
<template:put name='content'>

<html:form enctype="multipart/form-data" action="fis_check_marks?action=upload">
<table align=center border=0>

  <tr><td><font class="text_10">Файл с данными (CSV):</font></td>
      <td><html:file styleClass="select_f2" size='60' property="sourceFile" tabindex="8"/></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="9" value="Отправить"/></td>
  </tr>
  </table>
</html:form>
</template:put>
</template:insert>
</logic:equal>
<logic:equal name="action" value="upload">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Результат парсинга оценок ЕГЭ</template:put>
<template:put name="target_name">Результат парсинга оценок ЕГЭ</template:put>
<template:put name="sub_name">Результат парсинга оценок ЕГЭ</template:put>
<template:put name='content'>
<p>Парсинг завершён.</p>
<p>Список не найденных абитуриентов</p>
	<logic:iterate id="abit" name="abits" scope='request'>
		<bean:write name="abit" property="familija"/>&nbsp;&nbsp;
		<bean:write name="abit" property="imja"/>&nbsp;&nbsp;
		<bean:write name="abit" property="otchestvo"/><br>
	</logic:iterate>
</template:put>
</template:insert>
</logic:equal>
</logic:present>