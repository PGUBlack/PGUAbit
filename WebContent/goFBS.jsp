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

<logic:notPresent name='goFBSAction' scope='request'>
 <logic:redirect forward='goFBS'/>
</logic:notPresent>

<logic:notPresent name="goFBSForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<% int i=1; %>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid_nom = "0123456789"
var valid_ser = "ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ"
var temp;

if(document.forms(0).seriaAtt.value.length == 0){
  alert("Необходимо заполнить поле ''Серия аттестата''");
  document.forms(0).seriaAtt.focus();
  return false;
 }
for (var i=0; i<document.forms(0).seriaAtt.value.length; i++) 
 {
  temp = "" + document.forms(0).seriaAtt.value.substring(i, i+1);
  if (valid_ser.indexOf(temp) == "-1") {
    alert("Поле ''Серия аттестата'' может состоять только из букв русского алфавита");
    document.forms(0).seriaAtt.focus();
    return false;
  }

 }

if(document.forms(0).special1[0].checked) {

// Вводим один номер

  if(document.forms(0).nomerAtt.value.length == 0){
    alert("Необходимо заполнить поле ''Номер аттестата''");
    document.forms(0).nomerAtt.focus();
    return false;
   }
  for (var i=0; i<document.forms(0).nomerAtt.value.length; i++) 
   {
    temp = "" + document.forms(0).nomerAtt.value.substring(i, i+1);
    if (valid2.indexOf(temp) == "-1")  {
     alert("Поле ''Номер аттестата'' может состоять только из арабских цифр");
     document.forms(0).nomerAtt.focus();
     return false;
    }
   }
} else {

// Вводим несколько номеров

  if(document.forms(0).special2.value.length == 0){
    alert("Необходимо указать начальный ''Номер аттестата''");
    document.forms(0).special2.focus();
    return false;
   }
  for (var i=0; i<document.forms(0).special2.value.length; i++) 
   {
    temp = "" + document.forms(0).special2.value.substring(i, i+1);
    if (valid_nom.indexOf(temp) == "-1")  {
     alert("Поле ''Номер аттестата'' может состоять только из арабских цифр");
     document.forms(0).special2.focus();
     return false;
    }
   }
  if(document.forms(0).special3.value.length == 0){
    alert("Необходимо указать конечный ''Номер аттестата''");
    document.forms(0).special3.focus();
    return false;
   }
  for (var i=0; i<document.forms(0).special3.value.length; i++) 
   {
    temp = "" + document.forms(0).special3.value.substring(i, i+1);
    if (valid_nom.indexOf(temp) == "-1")  {
     alert("Поле ''Номер аттестата'' может состоять только из арабских цифр");
     document.forms(0).special3.focus();
     return false;
    }
   }
  if(document.forms(0).special3.value - document.forms(0).special2.value <0) {
     alert("Конечный ''Номер аттестата'' должен быть больше начального");
     document.forms(0).special3.focus();
     return false;
  }
 }
}

function gap_me(){
  document.forms(0).seriaAtt.value = " ";
  document.forms(0).nomerAtt.value = " ";
}

function confirmation(){
  if(confirm('Удалить запись?'))
   {
    return true;
   }
  else 
    return false; 
}

function selector() {
  if(document.forms(0).special1[0].checked) {
     document.forms(0).nomerAtt.disabled = false;
     if(document.forms(0).seriaAtt.value != "")
       document.forms(0).nomerAtt.focus();
     else
       document.forms(0).seriaAtt.focus();
  }
  else {
     document.forms(0).nomerAtt.disabled = true;
     document.forms(0).special2.focus();
  }
}

function exec() {
if(document.forms(0).login != null && document.forms(0).password != null)
   document.forms(0).submit();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="goFBSForm" property="action">
<bean:define id="action" name="goFBSForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- Соединение с ФБС  -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="login">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Установка соединения с Федеральной базой свидетельств ЕГЭ</template:put>
<template:put name="target_name">В Н И М А Н И Е</template:put>
<template:put name="sub_name">Идёт установка соединения с Федеральной базой свидетельств ЕГЭ</template:put>
<template:put name='content'>
<p align="center"><font class="text_th">Ждите</font></p>
 <form action="http://fbsege.ru/Login.aspx" method="post">
   <span>
     <input type="hidden" name="login" value="user5391"/><br/>
     <input type="hidden" name="password" value="vBGephi"/><br/>
   </span>
 </form>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%-----------------  Получение информации из ФБС  -----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="getInfo">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Получение информации из ФБС</template:put>
<template:put name="target_name">Получение информации</template:put>
<template:put name='content'>
<br>
<p align="center">
<a href="<bean:write name='abit_ba' property='special1'/>" styleClass="link_hov_blue">Нажмите для получения информации</a>
</p>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ Модификация/удаление одной записи в БД -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Недействительные аттестаты по данным Рособрнадзора</template:put>
<template:put name="target_name">Недействительные аттестаты</template:put>
<template:put name="sub_name">Модификация данных</template:put>
<template:put name='content'>
<br>
<html:form action="/goFBS?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">Серия аттестата:</font></td>
      <td><html:text accesskey="с" name="abit_ba" styleClass="text_f10_short" property="seriaAtt" 
                     maxlength="5" size="4" tabindex="1"/>

  <tr><td><font class="text_10">Номер аттестата:</font></td>
      <td><html:text accesskey="н" name="abit_ba" styleClass="text_f10_short" property="nomerAtt"
                     maxlength="12" size="8" tabindex="2"/>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_ba" property="kodZapisi"/>
<html:submit styleClass="button" tabindex="3" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="Удалить"/>
</td></html:form>
<html:form action="/goFBS?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="Вернуться назад"/>
</td></html:form>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%------------------- Результаты проверки атт. ghj,f --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="results">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Недействительные аттестаты по данным Рособрнадзора</template:put>
<template:put name="target_name">Список недействительных аттестатов</template:put>
<template:put name='content'>
<table align=center border=1 cellSpacing=0>
<thead>
<tr valign="middle">
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;НЛД&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;Фамилия И.О.&nbsp;</font></td>
    <td colspan=2 align=center height=30><font class="text_th">&nbsp;Аттестат&nbsp;</font></td>
</tr>
<tr>
    <td align=center height=30><font class="text_th">&nbsp;Серия&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;Номер&nbsp;</font></td></tr>
</tr>
</thead>
<logic:iterate id="abit_ba" name="abits_ba" scope='request'>
<tr>
  <td align=center valign=center height=30>&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_ba" paramId="kodAbiturienta"
                                                  paramProperty="kodAbiturienta" styleClass="link_hov_blue">
    <font class="text_10"><bean:write name="abit_ba" property="nomerLichnogoDela"/></html:link>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="familija"/></font>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="seriaAtt"/></font>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="nomerAtt"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_ba" property="kodAbiturienta">
<tr>
  <td align=center valign=center colspan=10>
     <font class="text_11">&nbsp;&nbsp;В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;недействительных&nbsp;аттестатов&nbsp;&nbsp;</font></td>
</logic:notPresent>
</table>

<table align=center border=0>
<tr>
<html:form method="post" action="/goFBS?action=null">
  <td height=40><html:submit property="exit" styleClass="button" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>