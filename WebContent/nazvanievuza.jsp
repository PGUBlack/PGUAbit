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

<logic:notPresent name='nazvanieVuzaAction' scope='request'>
 <logic:redirect forward='nazvanievuza'/>
</logic:notPresent>

<logic:notPresent name="nazvanieVuzaForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-()."
var temp;

if(document.forms(0).nazvanieVuza.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Реквизиты ВУЗа''");
  document.forms(0).nazvanieVuza.focus();
  return false;
 }
for (var i=0; i<document.forms(0).nazvanieVuza.value.length; i++) 
 {
  temp = "" + document.forms(0).nazvanieVuza.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
    alert("Поле ''Реквизиты ВУЗа'' может содержать только буквы русского алфавита");
    document.forms(0).nazvanieVuza.focus();
    return false;
  }
 }
if(document.forms(0).nazvanieRodit.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Реквизиты ВУЗа''");
  document.forms(0).nazvanieRodit.focus();
  return false;
 }
for (var i=0; i<document.forms(0).nazvanieRodit.value.length; i++) 
 {
  temp = "" + document.forms(0).nazvanieRodit.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
    alert("Поле ''Реквизиты ВУЗа'' может содержать только буквы русского алфавита");
    document.forms(0).nazvanieRodit.focus();
    return false;
  }
 }
if(document.forms(0).abbreviaturaVuza.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Аббревиатура''");
  document.forms(0).abbreviaturaVuza.focus();
  return false;
 }
for (var i=0; i<document.forms(0).abbreviaturaVuza.value.length; i++) 
 {
  temp = "" + document.forms(0).abbreviaturaVuza.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("Поле ''Аббревиатура'' может содержать только буквы русского алфавита");
   document.forms(0).abbreviaturaVuza.focus();
   return false;
  }
 }
if(document.forms(0).postAdresVuza.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Почтовый адрес ВУЗа''");
  document.forms(0).postAdresVuza.focus();
  return false;
 }
}

function gap_me(){
  document.forms(0).nazvanieVuza.value = " ";
  document.forms(0).nazvanieRodit.value = " ";
  document.forms(0).abbreviaturaVuza.value = " ";
  document.forms(0).postAdresVuza.value = " ";
}

function confirmation(){
  if(confirm('Удалить ВСЮ информацию из БД, связанную с этим ВУЗом?')) {
     if(confirm('Вы уверены? Восстановить будет невозможно!')) return true;
     else return false;
  }
  else return false;
}

function exec() {
if(document.forms(0).nazvanieVuza != null)
 document.forms(0).nazvanieVuza.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="nazvanieVuzaForm" property="action">
<bean:define id="action" name="nazvanieVuzaForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Добавление ВУЗа в систему</template:put>
<template:put name="sub_name">Добавление записи</template:put>
<template:put name='title'>Реквизиты ВУЗа</template:put>
<template:put name='content'>
<html:form action="/nazvanievuza?action=create" onsubmit="return checkFields();">
<table cols=3 align=center border=0 cellspacing=1 cellpadding=2>

  <tr><td><font class="text_10">Название ВУЗа:</font></td>
      <td><html:text accesskey="н" name="abit_NV" styleClass="text_f10" property="nazvanieVuza" 
                     maxlength="150" size="55" tabindex="1" value=""/>

  <tr><td><font class="text_10">Название в родит. пад.:</font></td>
      <td><html:text accesskey="н" name="abit_NV" styleClass="text_f10" property="nazvanieRodit" 
                     maxlength="150" size="55" tabindex="2" value=""/>

  <tr><td><font class="text_10">Аббревиатура:</font></td>
      <td><html:text accesskey="а" name="abit_NV" styleClass="text_f10" property="abbreviaturaVuza" 
                     maxlength="15" size="15" tabindex="3" value=""/>

  <tr><td><font class="text_10">Почтовый адрес ВУЗа:</font></td>
      <td><html:text accesskey="п" name="abit_NV" styleClass="text_f10" property="postAdresVuza" 
                     maxlength="250" size="55" tabindex="4" value=""/>
</table>
<BR>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="5" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="6" value="Отменить"/></td>
</html:form>
<html:form method="post" action="/nazvanievuza?action=create">
  <td><html:submit property="full" styleClass="button" tabindex="7" value="Просмотр таблицы"/></td>
</html:form>
<html:form action="/otvetstvennyelitsa?action=new">
  <td><html:submit styleClass="button" tabindex="8" value="Ответств. лица"/></td>
</html:form>
<html:form action="/nazvanievuza?action=null">
  <td><html:submit styleClass="button" tabindex="9" onclick="gap_me();" property="exit" value="Выход"/></td>
</html:form></tr>
<tr><td colspan=2 height=155></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">Список ВУЗов</template:put>
<template:put name='title'>Реквизиты ВУЗа</template:put>
<template:put name='content'>
<BR>
<table cols=3 align=center border=1 cellspacing=0 cellpadding=0>
<thead>
<tr><td align=center height=30><font class="text_th">&nbsp;Название&nbsp;ВУЗа&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;Название&nbsp;в&nbsp;родит.&nbsp;падеже&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;Аббревиатура&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;Почтовый&nbsp;адрес&nbsp;ВУЗа&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_NV" name="abits_NV" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link styleClass="link_hov_blue" href="nazvanievuza.do?action=mod_del" paramName="abit_NV" paramId="kodVuza" paramProperty="kodVuza">
                    <bean:write name="abit_NV" property="nazvanieVuza"/></html:link>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_11"><bean:write name="abit_NV" property="nazvanieRodit"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_11"><bean:write name="abit_NV" property="abbreviaturaVuza"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_11"><bean:write name="abit_NV" property="postAdresVuza"/></font>&nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_NV" property="kodVuza">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<table align=center border="0">
<tr>
<html:form action="/nazvanievuza.do?action=new">
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
<template:put name="target_name">Информация о ВУЗе</template:put>
<template:put name="sub_name">Модификация записи</template:put>
<template:put name='title'>Реквизиты ВУЗа</template:put>
<template:put name='content'>
<BR>
<html:form action="/nazvanievuza?action=change" onsubmit="return checkFields();">
<table cols=3 align=center border=0 cellspacing=0 cellpadding=0>

  <tr><td><font class="text_11">Название ВУЗа:</font></td>
      <td><html:text accesskey="н" name="abit_NV" styleClass="text_f10" property="nazvanieVuza" 
                     maxlength="150" size="55" tabindex="1"/>

  <tr><td><font class="text_10">Название в родит. пад.:</font></td>
      <td><html:text accesskey="н" name="abit_NV" styleClass="text_f10" property="nazvanieRodit" 
                     maxlength="150" size="55" tabindex="2"/>

  <tr><td><font class="text_10">Аббревиатура:</font></td>
      <td><html:text accesskey="а" name="abit_NV" styleClass="text_f10" property="abbreviaturaVuza" 
                     maxlength="15" size="15" tabindex="3"/>

  <tr><td><font class="text_10">Почтовый адрес ВУЗа:</font></td>
      <td><html:text accesskey="п" name="abit_NV" styleClass="text_f10" property="postAdresVuza" 
                     maxlength="250" size="55" tabindex="4"/>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_NV" property="kodVuza"/>
<html:submit styleClass="button" tabindex="5" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="6" value="Удалить"/>
</td></html:form>
<html:form action="/nazvanievuza?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="7" value="Вернуться назад"/>
</td></html:form>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>