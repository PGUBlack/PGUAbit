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

<logic:notPresent name='medaliAction' scope='request'>
 <logic:redirect forward='medali'/>
</logic:notPresent>

<logic:notPresent name="medaliForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid1 = " ёйцукенгшщзхъфывапролджэячсмитьбю"
var valid2 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-.,()"
var temp;

if(document.forms(0).shifrMedali.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Шифр медали''");
  document.forms(0).shifrMedali.focus();
  return false;
 }
for (var i=0; i<document.forms(0).shifrMedali.value.length; i++) 
 {
  temp = "" + document.forms(0).shifrMedali.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
   alert("Поле ''Шифр медали'' может содержать только строчные буквы русского алфавита");
   document.forms(0).shifrMedali.focus();
   return false;
  }
 }
if(document.forms(0).medal.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Медаль''");
  document.forms(0).medal.focus();
  return false;
 }
for (var i=0; i<document.forms(0).medal.value.length; i++) 
 {
  temp = "" + document.forms(0).medal.value.substring(i, i+1);
  if (valid2.indexOf(temp) == "-1") {
   alert("Поле ''Медаль'' может содержать только буквы русского алфавита");
   document.forms(0).medal.focus();
   return false;
  }
 }
}

function gap_me(){
  document.forms(0).shifrMedali.value = " ";
  document.forms(0).medal.value = " ";
}

function confirmation(){
  if(confirm('Удалить запись?')) return true;
   else return false; 
}

function exec() {
if(document.forms(0).shifrMedali != null)
document.forms(0).shifrMedali.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="medaliForm" property="action">
<bean:define id="action" name="medaliForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Информация об отличиях</template:put>
<template:put name="sub_name">Добавление записи</template:put>
<template:put name='title'>Медали</template:put>
<template:put name='content'>
<BR>
<html:form method="post" action="/medali?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">Шифр отличия:</font></td>
      <td><html:text accesskey="ш" name="abit_Medali" styleClass="text_f10" property="shifrMedali" 
                     maxlength="1" size="1" tabindex="1" value=""/>

  <tr><td><font class="text_10">Описание:</font></td>
      <td><html:text accesskey="о" name="abit_Medali" styleClass="text_f10" property="medal"
                     maxlength="150" size="55" tabindex="2" value=""/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="4" value="Отменить"/></td>
</html:form>
<html:form method="post" action="/medali?action=create">
  <td><html:submit property="full" styleClass="button" tabindex="5" value="Просмотр таблицы"/></td>
</html:form>
<html:form action="/tselevojpriem?action=new">
  <td><html:submit styleClass="button" tabindex="6" value="Целевой прием"/></td>
</html:form>
<html:form action="/medali?action=null">
  <td><html:submit styleClass="button" tabindex="7" onclick="gap_me();" property="exit" value="Выход"/></td>
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
<template:put name="target_name">Информация об отличиях</template:put>
<template:put name='title'>Отличия</template:put>
<template:put name='content'>
<BR>
<table cols=3 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center height=30><font class="text_th">&nbsp;Шифр&nbsp;отличия&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;Описание&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_Medali" name="abits_Medali" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="medali.do?action=mod_del" paramName="abit_Medali" paramId="kodMedali" paramProperty="kodMedali" styleClass="link_hov_blue">
                    <bean:write name="abit_Medali" property="shifrMedali"/></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Medali" property="medal"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_Medali" property="kodMedali">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/medali.do?action=new">
  <td align=center valign=center height="40">
    <html:submit styleClass="button" value="Вернуться назад"/>
  </td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ Модификация/удаление одной записи в БД -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Информация об отличиях</template:put>
<template:put name="sub_name">Модификация записи</template:put>
<template:put name='title'>Медали</template:put>
<template:put name='content'>
<BR>
<html:form action="/medali?action=change" onsubmit="return checkFields();">

<table cols=2 align=center border=0>

  <tr><td><font class="text_10">Шифр отличия:</font></td>
      <td><html:text accesskey="ш" name="abit_Medali" styleClass="text_f10" property="shifrMedali" 
                     maxlength="1" size="1" tabindex="1"/>

  <tr><td><font class="text_10">Описание:</font></td>
      <td><html:text accesskey="о" name="abit_Medali" styleClass="text_f10" property="medal"
                     maxlength="150" size="55" tabindex="2"/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_Medali" property="kodMedali"/>
<html:submit styleClass="button" tabindex="3" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="Удалить"/>
</td></html:form>
<html:form action="/medali?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="Вернуться назад"/>
</td></html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>