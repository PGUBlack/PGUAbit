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

<logic:notPresent name='fakultetyAction' scope='request'>
 <logic:redirect forward='fakultety'/>
</logic:notPresent>

<logic:notPresent name="fakultetyForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
var valid = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-()."
var temp;

if(document.forms(0).fakultet.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Факультет''");
  document.forms(0).fakultet.focus();
  return false;
 }
for (var i=0; i<document.forms(0).fakultet.value.length; i++) 
 {
  temp = "" + document.forms(0).fakultet.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1")   {
   alert("Поле ''Факультет'' может состоять только из букв русского алфавита");
   document.forms(0).fakultet.focus();
   return false;
  }
 }
if(document.forms(0).abbreviaturaFakulteta.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Аббревиатура''");
  document.forms(0).abbreviaturaFakulteta.focus();
  return false;
 }
for (var i=0; i<document.forms(0).abbreviaturaFakulteta.value.length; i++) 
 {
  temp = "" + document.forms(0).abbreviaturaFakulteta.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("Поле ''Аббревиатура'' может состоять только из букв русского алфавита");
   document.forms(0).abbreviaturaFakulteta.focus();
   return false;
  }
 }
if(document.forms(0).shifrFakulteta.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Шифр''");
  document.forms(0).shifrFakulteta.focus();
  return false;
 }
for (var i=0; i<document.forms(0).shifrFakulteta.value.length; i++) 
 {
  temp = "" + document.forms(0).shifrFakulteta.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1")  {
   alert("Поле ''Шифр'' может состоять только из заглавных букв русского алфавита");
   document.forms(0).shifrFakulteta.focus();
   return false;
  }
 }
if(document.forms(0).nazvanieVRoditelnom.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Факультет (родительный)''");
  document.forms(0).nazvanieVRoditelnom.focus();
  return false;
 }
for (var i=0; i<document.forms(0).nazvanieVRoditelnom.value.length; i++) 
 {
  temp = "" + document.forms(0).nazvanieVRoditelnom.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("Поле ''Факультет (родительный)'' может состоять только из букв русского алфавита");
   document.forms(0).nazvanieVRoditelnom.focus();
   return false;
  }
 }
if(document.forms(0).dekan.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Декан''");
  document.forms(0).dekan.focus();
  return false;
 }
for (var i=0; i<document.forms(0).dekan.value.length; i++) 
 {
  temp = "" + document.forms(0).dekan.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("Поле ''Декан'' может состоять только из букв русского алфавита");
   document.forms(0).dekan.focus();
   return false;
  }
 }
}

function gap_me(){
  document.forms(0).fakultet.value = " ";
  document.forms(0).abbreviaturaFakulteta.value = " ";
  document.forms(0).shifrFakulteta.value = " ";
  document.forms(0).nazvanieVRoditelnom.value = " ";
  document.forms(0).dekan.value = " ";
}

function confirmation(){
  if(confirm('Удалить Факультет и ВСЕ что с ним связано?'))
   {
     return true;
   }
  else return false; 
}

function exec() {
if(document.forms(0).fakultet != null) {
document.forms(0).fakultet.focus();
}
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="fakultetyForm" property="action">
<bean:define id="action" name="fakultetyForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Факультеты</template:put>
<template:put name="target_name">Факультет ВУЗа</template:put>
<template:put name="sub_name">Добавление записи</template:put>
<template:put name='content'>

<html:form action="/fakultety?action=create" onsubmit="return checkFields();">
<table align=center border=0>

  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:text accesskey="ф" name="abit_F" styleClass="text_f10" property="fakultet" 
                     maxlength="100" size="50" tabindex="1" value=""/></td></tr>

  <tr><td><font class="text_10">Аббревиатура:</font></td>
      <td><html:text accesskey="а" name="abit_F" styleClass="text_f10" property="abbreviaturaFakulteta" 
                     maxlength="5" size="5" tabindex="2" value=""/></td></tr>

  <tr><td><font class="text_10">План приема:</font></td>
      <td><html:text accesskey="п" name="abit_F" styleClass="text_f10" property="planPriemaFakulteta" 
                     maxlength="10" size="10" tabindex="3" value=""/></td></tr>

  <tr><td><font class="text_10">Шифр:</font></td>
      <td><html:text accesskey="ш" name="abit_F" styleClass="text_f10" property="shifrFakulteta" 
                     maxlength="1" size="1" tabindex="4" value=""/></td></tr>

  <tr><td><font class="text_10">Факультет (родительный):</font></td>
      <td><html:text accesskey="ф" name="abit_F" styleClass="text_f10" property="nazvanieVRoditelnom" 
                     maxlength="100" size="50" tabindex="5" value=""/></td></tr>

  <tr><td><font class="text_10">Полупроходной балл:</font></td>
      <td><html:text accesskey="п" name="abit_F" styleClass="text_f10" property="poluProhodnoiBallFakulteta" 
                     maxlength="2" size="2" tabindex="6" value=""/></td></tr>

  <tr><td><font class="text_10">Проходной балл:</font></td>
      <td><html:text accesskey="п" name="abit_F" styleClass="text_f10" property="prohodnoiBallFakulteta" 
                     maxlength="2" size="2" tabindex="7" value=""/></td></tr>

  <tr><td><font class="text_10">Декан:</font></td>
      <td><html:text accesskey="д" name="abit_F" styleClass="text_f10" property="dekan" 
                     maxlength="50" size="50" tabindex="8" value=""/></td></tr>

<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="9" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="10" value="Отменить"/></td>
</html:form>
<html:form action="/fakultety?action=adjustment">
  <td><html:submit styleClass="button" tabindex="11" value="Просмотр таблицы"/></td>
</html:form>
<html:form action="/spetsialnosti?action=new">
  <td><html:submit styleClass="button" tabindex="12" value="Специальности"/></td>
</html:form>
<html:form action="/spetsialnosti?action=null">
  <td><html:submit styleClass="button" tabindex="13" onclick="gap_me();" property="exit" value="Выход"/></td>
</html:form>
</tr><tr><td colspan=2 height=50></td></tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Факультеты</template:put>
<template:put name="target_name">Факультеты ВУЗа</template:put>
<template:put name='content'>

<table cols=9 align=center border=1 cellSpacing=0>
<thead>
<tr><td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;№&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Факультет&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Аббр.&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;План &nbsp;приема&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Шифр&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30>
     <font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Факультет&nbsp;(родительный)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Декан&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
    <td colspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Балл&nbsp;</font></td></tr>
<tr><td align=center valign=center height=30><font class="text_th">&nbsp;п/пр.&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;прох.&nbsp;</font></td>
</tr>
</thead>
<% int i=0; %>
<logic:iterate id="abit_F" name="abits_F" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="fakultety.do?action=mod_del" paramName="abit_F" paramId="kodFakulteta" paramProperty="kodFakulteta" styleClass="link_hov_blue"><%=++i%></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="fakultet"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="abbreviaturaFakulteta"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="planPriemaFakulteta"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="shifrFakulteta"/></font>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="nazvanieVRoditelnom"/></font>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="dekan"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="poluProhodnoiBallFakulteta"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="prohodnoiBallFakulteta"/></font>&nbsp;</td></tr>
</logic:iterate>

<logic:notPresent name="abit_F" property="kodFakulteta">
<tr>
  <td align=center valign=center colspan=8>
    <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</tr>
</logic:notPresent>
<tr>
<td colspan=10>
<%-- Скрипт перемещения кнопок за скроллером --%>
<div id="SlideButtons" style="FONT-SIZE: 0.65em; WIDTH:1px ; FONT-FAMILY: 'Trebuchet MS',Tahoma,Verdana,Geneva,Arial,Helvetica,sans-serif; POSITION: relative">
<table valign=center align=middle cellSpacing=6 border=0>
<tr>
<td valign=center align=middle>
   <html:submit styleClass="button" value="<" onclick="move_left()"/></td>
<html:form action="/fakultety.do?action=new">
  <td valign=center align=middle><html:submit styleClass="button" value="Вернуться назад"/></td>
</html:form>
  <td valign=center align=middle>
   <html:submit styleClass="button" value=">" onclick="move_right()"/></td>
</tr></table>
<%-- Скрипт перемещения кнопок за скроллером --%>
<SCRIPT language=JavaScript src="layouts/all/slideButtons.js" defer type=text/javascript></SCRIPT></div>
</td></tr>
</table>
<br><BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ Модификация/удаление одной записи в БД -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Факультеты</template:put>
<template:put name="target_name">Факультет ВУЗа</template:put>
<template:put name="sub_name">Модификация записи</template:put>
<template:put name='content'>

<html:form action="/fakultety?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:text accesskey="ф" name="abit_F" styleClass="text_f10" property="fakultet" 
                     size="50" maxlength="100" tabindex="1"/></td></tr>

  <tr><td><font class="text_10">Аббревиатура:</font></td>
      <td><html:text accesskey="а" name="abit_F" styleClass="text_f10" property="abbreviaturaFakulteta" 
                     size="5" maxlength="5" tabindex="2"/></td></tr>

  <tr><td><font class="text_10">План приема:</font></td>
      <td><html:text accesskey="п" name="abit_F" styleClass="text_f10" property="planPriemaFakulteta" 
                     size="10" maxlength="10" tabindex="3"/></td></tr>

  <tr><td><font class="text_10">Шифр:</font></td>
      <td><html:text accesskey="ш" name="abit_F" styleClass="text_f10" property="shifrFakulteta" 
                     size="1" maxlength="1" tabindex="4"/></td></tr>

  <tr><td><font class="text_10">Факультет (родительный):</font></td>
      <td><html:text accesskey="ф" name="abit_F" styleClass="text_f10" property="nazvanieVRoditelnom" 
                     size="50" maxlength="100" tabindex="5"/></td></tr>

  <tr><td><font class="text_10">Полупроходной балл:</font></td>
      <td><html:text accesskey="п" name="abit_F" styleClass="text_f10" property="poluProhodnoiBallFakulteta" 
                     size="2" maxlength="2" tabindex="6"/></td></tr>

  <tr><td><font class="text_10">Проходной балл:</font></td>
      <td><html:text accesskey="п" name="abit_F" styleClass="text_f10" property="prohodnoiBallFakulteta" 
                     size="2" maxlength="2" tabindex="7"/></td></tr>

  <tr><td><font class="text_10">Декан:</font></td>
      <td><html:text accesskey="д" name="abit_F" styleClass="text_f10" property="dekan" 
                     size="50" maxlength="50" tabindex="8"/></td></tr>
<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_F" property="kodFakulteta"/>
<html:submit styleClass="button" tabindex="9" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="10" value="Удалить"/>
</td>
</html:form>
<html:form action="/fakultety?action=full">
<td valign=center align=center>
<html:submit styleClass="button" tabindex="11" value="Вернуться назад"/>
</td></html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------- Форма настройки параметров просмотра --------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="adjustment">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Факультеты</template:put>
<template:put name="target_name">Факультеты ВУЗа</template:put>
<template:put name="sub_name">Настройка параметров просмотра</template:put>
<template:put name='content'>
<html:form action="/fakultety?action=create" scope="request">
<table cols=5 align=center border=0>
<tr>
    <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
    <th align=center valign=center><font class="text_10">Порядок следования данных:</font></th>
    <td align=left valign=center>
           <html:select name="abit_F" styleClass="select_f1" property="priznakSortirovki" tabindex="2">
           <html:option value="по возрастанию"/>
           <html:option value="по убыванию"/>
           </html:select></td>
</tr>
<tr><td><BR></td></tr>
<tr>
    <th align=center valign=center colspan=10><font class="text_10">СОРТИРОВАТЬ ПО СТОЛБЦУ:</font></th>
</tr>
</table>
<table cols=8 align=center border=1 cellSpacing=0>
<tr><td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Факультет&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Аббр.&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;План &nbsp;приема&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Шифр&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30>
     <font class="text_th">&nbsp;Факультет (родительный)&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30>
       <font class="text_th">&nbsp;Декан&nbsp;</font></td>
    <td colspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Балл&nbsp;</font></td></tr><tr>
    <td align=center valign=center height=30><font class="text_th">&nbsp;п/пр.&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;прох.&nbsp;</font></td>
</tr><tr>
    <td border=1 align=center valign=center>
      <html:radio accesskey="1" name="abit_F" property="stolbetsSortirovki" value="1" tabindex="3" /></td>
    <td align=center valign=center>
      <html:radio accesskey="2" name="abit_F" property="stolbetsSortirovki" value="2" tabindex="4" /></td>
    <td align=center valign=center>
      <html:radio accesskey="3" name="abit_F" property="stolbetsSortirovki" value="3" tabindex="5" /></td>
    <td align=center valign=center>
      <html:radio accesskey="4" name="abit_F" property="stolbetsSortirovki" value="4" tabindex="6" /></td>
    <td align=center valign=center>
      <html:radio accesskey="5" name="abit_F" property="stolbetsSortirovki" value="5" tabindex="7" /></td>
    <td align=center valign=center>
      <html:radio accesskey="6" name="abit_F" property="stolbetsSortirovki" value="6" tabindex="8" /></td>
    <td align=center valign=center>
      <html:radio accesskey="7" name="abit_F" property="stolbetsSortirovki" value="7" tabindex="9" /></td>
    <td align=center valign=center>
      <html:radio accesskey="8" name="abit_F" property="stolbetsSortirovki" value="8" tabindex="10" /></td>
</tr>
</table>
<table align=center border=0>
<tr height=10><td></td></tr>
<tr align=center>
  <td><html:submit property="full" styleClass="button" tabindex="11" value="Показать таблицу"/></td>
</html:form></tr>
<tr><td height=55></td></tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>