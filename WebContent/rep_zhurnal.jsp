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

<logic:notPresent name='zhurnalAction' scope='request'>
 <logic:redirect forward='rep_zhurnal'/>
</logic:notPresent>

<logic:notPresent name="shfrLichDelForm" property="action">
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

<logic:present name="shfrLichDelForm" property="action">
<bean:define id="action" name="shfrLichDelForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Журнал заявлений абитуриентов</template:put>
<template:put name="target_name">Журнал заявлений абитуриентов</template:put>
<template:put name="sub_name">По умолчанию формируется на текущий день</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_zhurnal?action=getRep" onsubmit="return checkFields();">
<table align=center border=0>
  		<tr>
         <td><font class="text_10">&nbsp;&nbsp;Дата&nbsp;для&nbsp;формирования:&nbsp;&nbsp;</font></td>
         <td vAlign="middle" height="18">
           <html:text name="daydate" styleClass="text_f9_short" property="dataRojdenija" 
                      maxlength="10" size="10" tabindex="6"/></td>
     	</tr>
  <tr><td><font class="text_10">&nbsp;&nbsp;Факультет:&nbsp;&nbsp;</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="kodFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      </td></tr>
</table>
<br>
<html:checkbox styleClass="checkbox_1" name="abit_SD" property="special1" tabindex="2"/><font class="text_10">&nbsp;&nbsp;Формировать&nbsp;от&nbsp;начала&nbsp;приёмной&nbsp;кампании&nbsp;</font>
<br><br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Сформировать"/></td>
</html:form>
<html:form method="post" action="/rep_zhurnal?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>