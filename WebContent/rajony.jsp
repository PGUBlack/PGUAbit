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

<logic:notPresent name='rajonyAction' scope='request'>
 <logic:redirect forward='rajony'/>
</logic:notPresent>

<logic:notPresent name="rajonyForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
var valid1 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ().-"
var temp;

for (var i=0; i<document.forms(0).nazvanieRajona.value.length; i++) 
 {
  temp = "" + document.forms(0).nazvanieRajona.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
     alert("Поле ''Название района'' может содержать только заглавные буквы русского алфавита");
     document.forms(0).nazvanieRajona.focus();
     return false;
  }
 }
if(document.forms(0).nazvanieRajona.value.length == 0) {
  alert("Необходимо заполнить поле ''Название района''");
  document.forms(0).nazvanieRajona.focus();
  return false;
}
}
function exec(){
  document.forms(0).nazvanieRajona.focus();
}
</SCRIPT>

<logic:present name="rajonyForm" property="action">
<bean:define id="action" name="rajonyForm" property="action"/>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Районы</template:put>
<template:put name='target_name'>Районы</template:put>
<template:put name='content'>
<BR>
<table cols=3 align=center border=0 cellSpacing=0>
<logic:iterate id="abit_R" name="abits_R" scope='request'>
<tr>
  <td valign=center>&nbsp;<html:link href="rajony.do?action=mod_del" styleClass="link_hov_blue" paramName="abit_R" 
                               paramId="kodRajona" paramProperty="kodRajona">
                    <bean:write name="abit_R" property="nazvanieRajona"/></html:link>&nbsp;</td>
  <td>
  <td valign=center>&nbsp;<html:link href="rajony.do?action=mod_del" styleClass="link_hov_blue"  paramName="abit_R" 
                               paramId="kodRajona" paramProperty="special11">
                    <bean:write name="abit_R" property="special1"/></html:link>&nbsp;</td>
  <td>
  <td valign=center>&nbsp;<html:link href="rajony.do?action=mod_del" styleClass="link_hov_blue"  paramName="abit_R" 
                               paramId="kodRajona" paramProperty="special22">
                    <bean:write name="abit_R" property="special2"/></html:link>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_R" property="kodRajona">
<tr>
  <td align=center valign=center colspan=5><font class="text_11">
     В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr>
<html:form action="/abit_srch.do">
  <td align=center>
    <html:submit styleClass="button" value="Абитуриенты"/>
  </td>
</html:form>
<html:form action="/punkty.do">
  <td align=center>
    <html:submit styleClass="button" value="Пункты"/>
  </td>
</html:form>
<html:form action="/oblasti.do">
  <td align=center>
    <html:submit styleClass="button" value="Области"/>
  </td>
</html:form>
<html:form action="/rajony.do">
  <td align=center>
    <html:submit styleClass="button" property="exit" value="Выход"/>
  </td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%---------------- Модификация одной записи в БД ------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Районы</template:put>
<template:put name='target_name'>Район</template:put>
<template:put name='sub_name'>Модификация записи</template:put>
<template:put name='content'>
<body onLoad="exec();">
<BR>
<html:form action="/rajony?action=change" onsubmit="return checkFields();">
<table cols=1 align=center border=0>

  <tr><td><font class="text_11">Название района:</font></td>
      <td><html:text accesskey="н" name="abit_R" styleClass="text_f1" property="nazvanieRajona" 
                     size="50" maxlength="50" tabindex="1"/>
</table>
<BR>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_R" property="kodRajona"/>
<html:submit styleClass="button" tabindex="2" value="Изменить"/>
<td align=center>
<html:submit styleClass="button" tabindex="3" value="Удалить" property="delete"/>
</td></html:form>
<html:form action="/rajony?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="4" value="Вернуться назад"/>
</td></html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>