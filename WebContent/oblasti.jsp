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

<logic:notPresent name='oblastiAction' scope='request'>
 <logic:redirect forward='oblasti'/>
</logic:notPresent>

<logic:notPresent name="oblastiForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
var valid1 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ().-"
var temp;

for (var i=0; i<document.forms(0).nazvanieOblasti.value.length; i++) 
 {
  temp = "" + document.forms(0).nazvanieOblasti.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1")  {
     alert("Поле ''Название области'' может содержать только буквы русского алфавита");
     document.forms(0).nazvanieOblasti.focus();
     return false;
  }
 }
if(document.forms(0).nazvanieOblasti.value.length == 0) {
  alert("Необходимо заполнить поле ''Название области''");
  document.forms(0).nazvanieOblasti.focus();
  return false;
 }
}
function exec(){
  document.forms(0).nazvanieOblasti.focus();
}
</SCRIPT>

<logic:present name="oblastiForm" property="action">
<bean:define id="action" name="oblastiForm" property="action"/>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Области</template:put>
<template:put name='target_name'>Области</template:put>
<template:put name='content'>
<BR>
<table cols=1 align=center border=1 cellSpacing=0>
<logic:iterate id="abit_O" name="abits_O" scope='request'>
<tr>
  <td valign=center>&nbsp;<html:link styleClass="link_hov_blue" href="oblasti.do?action=mod_del" paramName="abit_O" 
                               paramId="kodOblasti" paramProperty="kodOblasti">
                    <bean:write name="abit_O" property="nazvanieOblasti"/></html:link>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_O" property="kodOblasti">
<tr>
  <td align=center valign=center colspan=3><font class="text_11">
     В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr>
<html:form action="/rajony.do">
  <td align=center>
    <html:submit styleClass="button" value="Районы"/>
  </td>
</html:form>
<html:form action="/punkty.do">
  <td align=center>
    <html:submit styleClass="button" value="Пункты"/>
  </td>
</html:form>
<html:form action="/oblasti.do">
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
<template:put name='title'>Области</template:put>
<template:put name='target_name'>Область</template:put>
<template:put name='sub_name'>Модификация записи</template:put>
<template:put name='content'>
<body onLoad="exec();">
<BR>
<html:form action="/oblasti?action=change" onsubmit="return checkFields()">
<table cols=1 align=center border=0>

  <tr><td><font class="text_10">Название области:</font></td>
      <td><html:text accesskey="н" name="abit_O" styleClass="text_f10" property="nazvanieOblasti" 
                     size="50" maxlength="50" tabindex="1"/>
</table>
<br>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_O" property="kodOblasti"/>
<html:submit styleClass="button" tabindex="2" value="Изменить"/>
<td align=center>
<html:submit styleClass="button" tabindex="3" value="Удалить" property="delete"/>
</td></html:form>
<html:form action="/oblasti?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="4" value="Вернуться назад"/>
</td></html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>