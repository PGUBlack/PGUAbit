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

<logic:notPresent name='orAction' scope='request'>
 <logic:redirect forward='or'/>
</logic:notPresent>

<logic:notPresent name="orForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>


<logic:present name="orForm" property="action">
<bean:define id="action" name="orForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Список абитуриентов к первому этапу</template:put>
<template:put name="target_name">Список абитуриентов к первому этапу</template:put>
<template:put name="sub_name">предварительный</template:put>
<template:put name='content'>
<BR>
<html:form action="/or?action=report">
<table align=center border=0>
<tr><td><font class="text_10">Бюджетники:</font></td>
<td><html:radio accesskey="б" name="abit_SD" property="priznakSortirovki" tabindex="1" value="budgetniki"/></td>
<tr><td><font class="text_10">Контрактники:</font></td>
<td><html:radio accesskey="к" name="abit_SD" property="priznakSortirovki" tabindex="2" value="kontraktniki"/></td>
</tr>
<tr><td>
<html:select styleClass="select_f1" name="abit_SD" property="special6" tabindex="3">
             <html:option value="rtf">rtf</html:option>
            <html:option value="xml">xml</html:option>
           
           
</html:select>
</td>
</tr>

</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Сформировать"/></td>
</html:form>
<html:form method="post" action="/or?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>