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

<logic:notPresent name='medConfirmAction' scope='request'>
 <logic:redirect forward='rep_med_confirm'/>
</logic:notPresent>

<logic:notPresent name="medConfirmForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function invokeAct(){
  document.forms(0).submit();
}

</SCRIPT>

<logic:present name="medConfirmForm" property="action">
<bean:define id="action" name="medConfirmForm" property="action"/>

<%---------------- Собственно страница ----------------------------%>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Всего подтвержденных медалистов по потокам</template:put>
<template:put name="target_name">Подтвержденные медалисты по потокам</template:put>
<template:put name="sub_name">Выберите поток</template:put>
<template:put name='content'>
<html:form action="rep_med_confirm?action=show">
<table align=center border=0 cellspacing=0>
  <tr><td align=center colspain=2><font class="text_10">Номер потока:</font></td>
      <td align=center colspain=2>
      <html:select onchange="invokeAct()" styleClass="select_f2" name="abit_MC" property="nomerPotoka">
       <html:options collection="abit_MC_S1" property="nomerPotoka"/>
      </html:select>
      </td>
   </tr>
</table>
</html:form>
<table align=center border=1 cellspacing=0>
<tr><td><font class="text_th">&nbsp;ФАКУЛЬТЕТ&nbsp;</font></td>
    <td><font class="text_th">&nbsp;ВСЕГО&nbsp;</font></td></tr>
<%-- HEAVY LOGIC INTERFACE BUILDER --%>
      <logic:iterate id="abit_MC5" name="abit_MC_S5" scope='request'>
      <tr><td align=center><font class="text_11">
	<bean:write name="abit_MC5" property="abbreviaturaFakulteta"/></font></td>
	<td align=center><font class="text_11">
	<bean:write name="abit_MC5" property="vsegoMed"/></font></td></tr>
      </logic:iterate>
<logic:notPresent name="abit_MC5" property="abbreviaturaFakulteta">
<tr><td colspan=2 align=center valign=center>
    <font class="text_th">&nbsp;нет&nbsp;сведений&nbsp;</font></td></tr>
</logic:notPresent>
</table>
<table align=center border=0 cols=2>
<td align=center>
<html:form action="/rep_med_confirm.do?action=report">
<html:submit styleClass="button_sd" value="Создать отчет"/></td>
</html:form>
<html:form action="/rep_med_confirm.do">
  <td><html:submit styleClass="button_sd" property="exit" value="Выход"/></td>
</html:form></tr>
</table>

</template:put>
</template:insert>
</logic:present>