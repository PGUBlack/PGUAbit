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

<logic:notPresent name='otvetstvennyeLitsaAction' scope='request'>
 <logic:redirect forward='otvetstvennyelitsa'/>
</logic:notPresent>

<logic:notPresent name="otvetstvennyeLitsaForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid = " йцукенгшщзхъфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-()."
var temp;

if(document.forms(0).doljnost.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Должность''");
  document.forms(0).doljnost.focus();
  return false;
 }
for (var i=0; i<document.forms(0).doljnost.value.length; i++) 
 {
  temp = "" + document.forms(0).doljnost.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("Поле ''Должность'' может состоять только из букв русского алфавита!");
   document.forms(0).doljnost.focus();
   return false;
  }
 }
if(document.forms(0).fio.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''ФИО''");
  document.forms(0).fio.focus();
  return false;
 }
for (var i=0; i<document.forms(0).fio.value.length; i++) 
 {
  temp = "" + document.forms(0).fio.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("Поле ''ФИО'' может состоять только из букв русского алфавита!");
   document.forms(0).fio.focus();
   return false;
  }
 }
}

function invokeAct(){
  document.forms(0).submit();
}

function gap_me(){
  document.forms(0).doljnost.value = " ";
  document.forms(0).fio.value = " ";
}

function confirmation(){
  if(confirm('Удалить запись?'))
   {
    return true;
   }
  else 
       return false; 
}

function exec() {
if(document.forms(0).doljnost != null)
document.forms(0).doljnost.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="otvetstvennyeLitsaForm" property="action">
<bean:define id="action" name="otvetstvennyeLitsaForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Ответственное лицо приемной кампании</template:put>
<template:put name="sub_name">Добавление записи</template:put>
<template:put name='title'>Ответственные лица</template:put>
<template:put name='content'>
<BR>
<html:form action="/otvetstvennyelitsa?action=create" onsubmit="return checkFields();">
<table cols=3 align=center border=0>
  <tr><td><font class="text_10">Должность:</font></td>
      <td><html:text accesskey="д" name="abit_OL" styleClass="text_f10" property="doljnost" 
                     maxlength="50" size="50" tabindex="1" value=""/>

  <tr><td><font class="text_10">ФИО:</font></td>
      <td><html:text accesskey="ф" name="abit_OL" styleClass="text_f10" property="fio" 
                     maxlength="50" size="50" tabindex="2" value=""/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="4" value="Отменить"/></td>
  <td><html:submit property="full" onclick="gap_me();" styleClass="button" tabindex="5" value="Просмотр таблицы"/></td>
</html:form>
<html:form action="/fakultety?action=new">
  <td><html:submit styleClass="button" tabindex="6" value="Факультеты"/></td>
</html:form>
<html:form method="post" action="/otvetstvennyelitsa?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="7" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">Ответственные лица приемной кампании ВУЗа</template:put>
<template:put name='title'>Ответственные лица</template:put>
<template:put name='content'>
<BR>
<table cols=3 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center height=30><font class="text_th">&nbsp;Должность&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;ФИО&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_OL" name="abits_OL" scope='request'>
<tr>
  <td valign=center>&nbsp;<html:link href="otvetstvennyelitsa.do?action=mod_del" paramName="abit_OL" 
                   paramId="kodZapisi" paramProperty="kodZapisi" styleClass="link_hov_blue">
                    <bean:write name="abit_OL" property="doljnost"/></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_OL" property="fio"/></font>&nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_OL" property="kodZapisi">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/otvetstvennyelitsa.do?action=new">
  <td align=center valign=center height="40">
    <html:submit styleClass="button" value="Вернуться назад"/>
  </td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ Модификация/удаление одной записи в БД -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Ответственное лицо</template:put>
<template:put name="sub_name">Модификация записи</template:put>
<template:put name='title'>Ответственные лица</template:put>
<template:put name='content'>
<BR>
<html:form action="/otvetstvennyelitsa?action=change" onsubmit="return checkFields();">
<table cols=3 align=center border=0>
  <tr><td><font class="text_10">Должность:</font></td>
      <td><html:text accesskey="д" name="abit_OL" styleClass="text_f10" property="doljnost" 
                     size="50" maxlength="50" tabindex="1"/>

  <tr><td><font class="text_10">ФИО:</font></td>
      <td><html:text accesskey="ф" name="abit_OL" styleClass="text_f10" property="fio" 
                     size="50" maxlength="50" tabindex="2"/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_OL" property="kodZapisi"/>
<html:submit styleClass="button" tabindex="3" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="Удалить"/>
</td></html:form>
<html:form action="/otvetstvennyelitsa?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="Вернуться назад"/>
</td></html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>