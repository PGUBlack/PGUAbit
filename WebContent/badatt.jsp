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

<logic:notPresent name='badAttAction' scope='request'>
 <logic:redirect forward='badatt'/>
</logic:notPresent>

<logic:notPresent name="badAttForm" property="action">
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
if(document.forms(0).seriaAtt != null)
   document.forms(0).nomerAtt.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="badAttForm" property="action">
<bean:define id="action" name="badAttForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Недействительные аттестаты по данным Рособрнадзора</template:put>
<template:put name="target_name">Недействительные аттестаты</template:put>
<template:put name="sub_name">Добавление новых данных</template:put>
<template:put name='content'>
<html:form method="post" action="/badatt?action=create" onsubmit="return checkFields();">
<table cols=5 align=center border=0>
  <tr><td><html:radio name="abit_ba" property="special1" onclick="selector();" value="one"/></td>
      <td colspan=4><font class="text_10">Аттестат (серия, номер):</font>&nbsp;
          <html:text accesskey="с" name="abit_ba" styleClass="text_f10_short" property="seriaAtt" 
                     maxlength="5" size="4" tabindex="1"/>&nbsp;
          <html:text accesskey="н" name="abit_ba" styleClass="text_f10_short" property="nomerAtt"
                     maxlength="12" size="8" tabindex="2" value=""/></td>

  <tr><td colspan=6><hr></td>
  <tr><td><html:radio name="abit_ba" property="special1" onclick="selector();" value="many"/></td>
      <td><font class="text_10">Номер аттестата с:</font></td>
      <td><html:text accesskey="н" name="abit_ba" styleClass="text_f10_short" property="special2"
                     maxlength="12" size="9" tabindex="2" value=""/>
      <td><font class="text_10">по:</font></td>
      <td><html:text accesskey="п" name="abit_ba" styleClass="text_f10_short" property="special3"
                     maxlength="12" size="9" tabindex="3" value=""/>
  <tr><td colspan=6><hr></td>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="4" value="Добавить"/></td>
</html:form>
<html:form method="post" action="/badatt?action=create">
  <td><html:submit property="full" styleClass="button" tabindex="5" value="Просмотр таблицы"/></td>
</html:form>
<html:form method="post" action="/badatt?action=check">
  <td><html:submit styleClass="button" tabindex="6" value="Проверить"/></td>
</html:form>
<html:form method="post" action="/badatt?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="7" value="Выход"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Недействительные аттестаты по данным Рособрнадзора</template:put>
<template:put name="target_name">Список недействительных аттестатов</template:put>
<template:put name='content'>
<br>
<table cols=3 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center height=30><font class="text_th">&nbsp;№&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Серия&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Номер&nbsp;</font></td></tr>
</thead>
<logic:iterate id="abit_ba" name="abits_ba" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="badatt.do?action=mod_del" paramName="abit_ba" paramId="kodZapisi"                                                  paramProperty="kodZapisi" styleClass="link_hov_blue">
    <%=i++%></html:link>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="seriaAtt"/></font>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="nomerAtt"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_ba" property="kodZapisi">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>

<table align=center border=0>
<tr>
<html:form action="/badatt.do?action=new">
  <td align=center vAlign=middle height=35>
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
<template:put name='title'>Недействительные аттестаты по данным Рособрнадзора</template:put>
<template:put name="target_name">Недействительные аттестаты</template:put>
<template:put name="sub_name">Модификация данных</template:put>
<template:put name='content'>
<br>
<html:form action="/badatt?action=change" onsubmit="return checkFields();">
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
<html:form action="/badatt?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="Вернуться назад"/>
</td></html:form>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%------------------- Результаты проверки атт. --------------------%>
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
<html:form method="post" action="/badatt?action=null">
  <td height=40><html:submit property="exit" styleClass="button" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>