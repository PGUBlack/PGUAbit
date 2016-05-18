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

<logic:notPresent name='twinsAction' scope='request'>
 <logic:redirect forward='twins'/>
</logic:notPresent>

<logic:notPresent name="twinsForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){

}

function invokeAct(){
  document.forms(0).submit();
}
</SCRIPT>

<logic:present name="twinsForm" property="action">
<bean:define id="action" name="twinsForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------- Алфавитный список ДВОЙНИКОВ -------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="doubles">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">Двойники системы</template:put>
<template:put name="sub_name">Двойники</template:put>
<template:put name='title'>Алфавитный список двойников</template:put>
<template:put name='content'>
<BR>
<html:form action="twins.do?action=doubles">
<logic:iterate id="abit_T" name="abit_T_S1" scope='request'>
        <html:link href="twins.do?action=doubles" paramId="letter" paramName="abit_T" paramProperty="special1" styleClass="link_hov_blue">
        <bean:write name="abit_T" property="special2"/>
        </html:link>
</logic:iterate>
</html:form>
<table cols=7 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center height=30><font class="text_th">&nbsp;№&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Имя&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;№&nbsp;личн.&nbsp;дела&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Номер докум.&nbsp;</font></td>
</tr>
</thead>
<% int number=0; %>
<logic:iterate id="abit_T" name="abits_T" scope='request'>
<tr>
  <td valign=center align=center><html:link href="abit_md.do?action=mod_del" paramName="abit_T" styleClass="link_hov_blue"
                                            paramId="kodAbiturienta" paramProperty="kodAbiturienta">
  <%=++number%>
  </html:link></td>
    <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_T" property="familija"/>&nbsp;</font></td>
    <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_T" property="imja"/>&nbsp;</font></td>
    <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_T" property="otchestvo"/>&nbsp;</font></td>
    <td valign=center align=center><font class="text_10">&nbsp;
                         <bean:write name="abit_T" property="nomerLichnogoDela"/>&nbsp;</font></td>
    <td align=center valign=center><font class="text_10">&nbsp;
                         <bean:write name="abit_T" property="nomerDokumenta"/>&nbsp;</font></td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_T" property="familija">
<tr>
  <td align=center valign=center colspan=7>
     <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/twins.do?action=report2">
  <td align=center>
    <html:submit styleClass="button_sd" value="Создать отчет"/>
  </td>
</html:form>
<td>&nbsp;</td>
<html:form action="/twins?action=twins">
  <td align=center>
    <html:submit styleClass="button_sd" value="Близнецы"/>
  </td>
</html:form>
<td>&nbsp;</td>
<html:form method="post" action="/twins?action=null">
  <td><html:submit property="exit" styleClass="button_sd" value="Выход"/></td>
</html:form>
</tr></table>
<br>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%---------------------- Все БЛИЗНЕЦЫ ВУЗа ------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="twins">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">Близнецы ВУЗа</template:put>
<template:put name="sub_name">Близнецы ВУЗа</template:put>
<template:put name='title'>Все близнецы ВУЗа</template:put>
<template:put name='content'>
<BR>
<table cols=7 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center height=30><font class="text_th">&nbsp;№&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Имя&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;№&nbsp;личн.&nbsp;дела&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;Номер докум.&nbsp;</font></td>
</tr>
</thead>
<% int number=0; %>
<logic:iterate id="abit_T" name="abits_T" scope='request'>
<tr>
  <td valign=center align=center><html:link href="abit_md.do?action=mod_del" paramName="abit_T" styleClass="link_hov_blue"
                                            paramId="kodAbiturienta" paramProperty="kodAbiturienta">
  <%=++number%>
  </html:link></td>
  <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_T" property="familija"/>&nbsp;</font></td>
  <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_T" property="imja"/>&nbsp;</font></td>
  <td valign=center><font class="text_10">&nbsp;<bean:write name="abit_T" property="otchestvo"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;
                    <bean:write name="abit_T" property="nomerLichnogoDela"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_10">&nbsp;
                    <bean:write name="abit_T" property="nomerDokumenta"/>&nbsp;</font></td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_T" property="familija">
<tr>
  <td align=center valign=center colspan=7>
     <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>

<table align=center border=0>
<tr>
<html:form action="/twins.do?action=report1">
  <td align=center><html:submit styleClass="button_sd" value="Создать отчет"/></td>
</html:form>
<td>&nbsp;</td>
<html:form action="/twins?action=doubles">
  <td align=center><html:submit styleClass="button_sd" value="Двойники"/></td>
</html:form>
<td>&nbsp;</td>
<html:form method="post" action="/twins?action=null">
  <td><html:submit property="exit" styleClass="button_sd" value="Выход"/></td>
</html:form>
</tr></table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>