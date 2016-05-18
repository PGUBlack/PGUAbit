<%@ page contentType = "text/html;charset=windows-1251"
         language = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name="abitModDelAction" scope="request">
<logic:redirect forward="abit_md_res"/>
</logic:notPresent>

<logic:notPresent name="abitSrchForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
  var sp1,sp;
  var faksLength;
  var groupsLength;
  var propusk = false;
</SCRIPT>

<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">Результаты модификации личной карточки абитуриента</template:put>
<template:put name="target_name">Результаты модификации личной карточки абитуриента</template:put>

<template:put name="content">
<logic:present name="abitSrchForm" property="action">

<html:form action="/abit_md.do?action=goto">
<html:hidden name="abit_A" property="dokumentyHranjatsja"/>
<table width="100%" border=0 cellSpacing=0 cellPadding=0>
<tr><td></td>
<logic:present name="mess" property="message">
<td width="100%" align=center><font class="message"><bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/></font></td>
</logic:present>
<td></td></tr>
</table>
<br>
 <table align=middle border=0>
  <tr>
   <td valign=middle align=middle>
     <html:submit styleClass="button" value="Перейти к меню" property="exit"/></td>
   <td valign=middle align=middle>
     <html:submit styleClass="button" value="Результаты поиска" property="srch_res"/>&nbsp;&nbsp;</td>
  </tr>
 </table>
</html:form>
</logic:present>

</template:put>
</template:insert>