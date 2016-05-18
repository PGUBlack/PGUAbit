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

<SCRIPT LANGUAGE="JavaScript">

function checkFields() {
  if(document.forms(0).kodFakulteta.value == "-") {
    alert("Необходимо выбрать Факультет");
    document.forms(0).kodFakulteta.focus();
    return false;
  }
}
</SCRIPT>

<logic:notPresent name='checkEGEAction' scope='request'>
 <logic:redirect forward='checkEGE'/>
</logic:notPresent>

<logic:notPresent name="checkEGEForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<logic:present name="checkEGEForm" property="action">
<bean:define id="action" name="checkEGEForm" property="action"/>

<logic:equal name="action" value="menu">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Проверка результатов ЕГЭ</template:put>
<template:put name="target_name">Проверка результатов ЕГЭ</template:put>
<template:put name="content">

<html:form action="/checkEGE?action=show&offset=new" onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
  <tr>
  </tr>
</table>
<br>
<table border="0" align="center">
<tr align="center">
  <td><html:submit styleClass="button" value="Проверить" tabindex="1"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/abiturient.do">
  <td><html:submit styleClass="button" value="Выход" tabindex="2" property="exit"/></td>
</html:form>
</tr>
</table>

</template:put>
</template:insert>
</logic:equal>
</logic:present>