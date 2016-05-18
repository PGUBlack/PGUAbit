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

<logic:notPresent name='podtvMedAction' scope='request'>
 <logic:redirect forward='rep_podtv_med'/>
</logic:notPresent>

<logic:notPresent name="podtvMedForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function exec() {
 document.forms(0).shifrFakulteta.selectedIndex=0;
 document.forms(0).shifrFakulteta.focus();
}

</SCRIPT>

<logic:present name="podtvMedForm" property="action">
<bean:define id="action" name="podtvMedForm" property="action"/>

<body onLoad="exec()"></body>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Список медалистов, подтвердивших свои медали</template:put>
<template:put name="target_name">Медалисты, подтвердившие свои медали</template:put>
<template:put name="sub_name">Выберите факультет</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_podtv_med?action=report">
<table cols=3 align=center border=0>
<tr><td colspan=3></td></tr>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="shifrFakulteta" tabindex="1">
          <html:option value="*"/>
          <html:options collection="abit_SD_S1" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
</table>
<table align=center border=0>
<tr>
   <td height=14></td>
</tr>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="2" value="Создать отчет"/></td>
</html:form>
<html:form method="post" action="/rep_podtv_med?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="3" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>