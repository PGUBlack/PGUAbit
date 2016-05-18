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

<logic:notPresent name='konGrpAction' scope='request'>
 <logic:redirect forward='kongruppa'/>
</logic:notPresent>

<logic:notPresent name="konGrpForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
if(document.forms(0).nazvanie.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Название конкурсной группы''");
  document.forms(0).nazvanie.focus();
  return false;
 }
if(document.forms(0).abbreviatura.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Шифр конк. группы''");
  document.forms(0).abbreviatura.focus();
  return false;
 }
}

function gap_me(){
  document.forms(0).nazvanie.value = " ";
  document.forms(0).abbreviatura.value = " ";
}

function confirmation(){
  if(confirm('Удалить Конкурсную группу?'))
   {
     return true;
   }
  else return false; 
}

function exec() {
if(document.forms(0).nazvanie != null) {
  document.forms(0).nazvanie.focus();
}
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="konGrpForm" property="action">
<bean:define id="action" name="konGrpForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Конкурсная группа ВУЗа</template:put>
<template:put name="target_name">Конкурсная группа ВУЗа</template:put>
<template:put name="sub_name">Добавление записи</template:put>
<template:put name='content'>

<html:form action="/kongruppa?action=create" onsubmit="return checkFields();">
<table align=center border=0>

  <tr><td><font class="text_10">Название конкурсной гр.:</font></td>
      <td><html:text accesskey="ф" name="abit_F" styleClass="text_f10" property="nazvanie" 
                     maxlength="100" size="50" tabindex="1" value=""/></td></tr>

  <tr><td><font class="text_10">Шифр конк. группы:</font></td>
      <td><html:text accesskey="а" name="abit_F" styleClass="text_f10" property="abbreviatura" 
                     maxlength="8" size="8" tabindex="2" value=""/></td></tr>

<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="9" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="10" value="Отменить"/></td>
</html:form>
<html:form action="/kongruppa?action=full">
  <td><html:submit styleClass="button" tabindex="11" value="Просмотр таблицы"/></td>
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
<template:put name='title'>Конкурсные группы ВУЗа</template:put>
<template:put name="target_name">Конкурсные группы ВУЗа</template:put>
<template:put name='content'>
<br>
<table cols=9 align=center border=1 cellSpacing=0>
<thead>
<tr><td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Название&nbsp;конкурсной&nbsp;группы&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;Шифр&nbsp;конк.&nbsp;группы&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_F" name="abits_F" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="kongruppa.do?action=mod_del" paramName="abit_F" paramId="kodKonGrp" paramProperty="kodKonGrp" styleClass="link_hov_blue">&nbsp;<bean:write name="abit_F" property="nazvanie"/>&nbsp;</html:link>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_F" property="abbreviatura"/></font>&nbsp;</td>
</logic:iterate>

<logic:notPresent name="abit_F" property="kodKonGrp">
<tr>
  <td align=center valign=center colspan=8>
    <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</tr>
</logic:notPresent>
</table>
<p aling="center">
<html:form action="/kongruppa.do?action=new">
<html:submit styleClass="button" value="Вернуться назад"/>
</html:form></p>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ Модификация/удаление одной записи в БД -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Конкурсная группа ВУЗа</template:put>
<template:put name="target_name">Конкурсная группа ВУЗа</template:put>
<template:put name="sub_name">Модификация записи</template:put>
<template:put name='content'>

<html:form action="/kongruppa?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">Название конкурсной гр.:</font></td>
      <td><html:text accesskey="ф" name="abit_F" styleClass="text_f10" property="nazvanie" 
                     size="50" maxlength="100" tabindex="1"/></td></tr>

  <tr><td><font class="text_10">Шифр конк. группы:</font></td>
      <td><html:text accesskey="а" name="abit_F" styleClass="text_f10" property="abbreviatura" 
                     size="8" maxlength="8" tabindex="2"/></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_F" property="kodKonGrp"/>
<html:submit styleClass="button" tabindex="9" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="10" value="Удалить"/>
</td>
</html:form>
<html:form action="/kongruppa?action=full">
<td valign=center align=center>
<html:submit styleClass="button" tabindex="11" value="Вернуться назад"/>
</td></html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>