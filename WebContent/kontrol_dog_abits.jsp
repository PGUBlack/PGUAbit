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

<logic:notPresent name='kontrolDogAbitAction' scope='request'>
 <logic:redirect forward='kontrol_dog_abits'/>
</logic:notPresent>

<logic:notPresent name="kontrolDogAbitForm" property="action">
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

<logic:present name="kontrolDogAbitForm" property="action">
<bean:define id="action" name="kontrolDogAbitForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Контроль оплаты договоров</template:put>
<template:put name="target_name">Контроль оплаты заключенных договоров</template:put>
<template:put name="sub_name">По абитуриентам всего ВУЗа или по факультетам</template:put>
<template:put name='content'>
<BR>
<html:form action="/kontrol_dog_abits?action=report" onsubmit="return checkFields();">
<table align=center border=0>
  <tr><td><font class="text_10">Факультет:&nbsp;&nbsp;</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="kodFakulteta" tabindex="1">
          <html:option value="%">все</html:option>
          <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      </td></tr>
  <tr><td><font class="text_10">&nbsp;Находить&nbsp;договорников:&nbsp;</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="special1" tabindex="1">
          <html:option value="%">всех</html:option>
          <html:option value="д">только оплативших</html:option>
          <html:option value="н">только закл., но не опл.</html:option>
      </html:select> 
      </td></tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="2" value="Сформировать"/></td>
</html:form>
<html:form method="post" action="/kontrol_dog_abits?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="3" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>