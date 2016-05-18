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

<logic:notPresent name='apelljatsijaAction' scope='request'>
 <logic:redirect forward='apelljatsija'/>
</logic:notPresent>

<logic:notPresent name="apelljatsijaForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var str,old,news

function add_val() {
 if(document.forms(0).special1.value.length != 0) {
    document.forms(0).shifrFakulteta.options[document.forms(0).shifrFakulteta.length] = new Option(document.forms(0).special1.value);
    document.forms(0).special1.value = "";
  } else {
    alert("Внимание! Необходимо задать номер личного дела.");
    document.forms(0).special1.focus();
    return;
  }
}

function checkFields() {
 if(document.forms(0).shifrFakulteta.length==0) {
   alert("Список номеров апеллируемых дел не может быть пустым!");
   document.forms(0).shifrFakulteta.focus();
   return false;
 }
//Компановка строки списка номеров
  document.forms(0).special3.value="";
    for(var i=0;i<document.forms(0).shifrFakulteta.length;i++){
       str = document.forms(0).shifrFakulteta.options[i].text;
       document.forms(0).special3.value += str + "%";
    }
}

function del_val() {
var index = document.forms(0).shifrFakulteta.selectedIndex;
if(index==-1) return;
document.forms(0).shifrFakulteta.options[index] = null;
}
</SCRIPT>

<logic:present name="apelljatsijaForm" property="action">
<bean:define id="action" name="apelljatsijaForm" property="action"/>


<%-----------------------------------------------------------------%>
<%---------------------- ФОРМА ВВОДА №-в ДЕЛ ----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<body onLoad="document.forms(0).special1.focus();"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Апелляция экзаменационных оценок</template:put>
<template:put name="target_name">Апелляция экзаменационных оценок</template:put>
<template:put name='content'>
<html:form action="/apelljatsija?action=doit" onsubmit="return checkFields();">
<html:hidden name="abit_Gr" property="special3"/>
<table valign=top align=center border=0 cellSpacing=0 cellPadding=0>
  <tr><td align=center height=16><font class="text_11">Апеллируемый предмет:</font>&nbsp;</td>
      <td align=center height=27>
       <html:select styleClass="select_f1" name="abit_Gr" property="kodPredmeta">
        <html:options collection="predmets" property="kodPredmeta" labelProperty="predmet"/>
       </html:select>
      </td>
  </tr>
  <tr><td align=center height=16 colspan=6><hr></td></tr>
  <tr><td align=center height=36><font class="text_11">Номер дела:</font>&nbsp;
        <html:text accesskey="с" name="abit_Gr" styleClass="text_f10_short" property="special1" maxlength="10" size="10" tabindex="1" value=""/>
  <tr><td align=center height=16 colspan=6><hr></td></tr>
  <tr><td height=36 colspan=6 align=center><html:button styleClass="button" property="add" tabindex="3" onclick="add_val();" value="Добавить"/></td></tr>
</table	>
<table align=center border=0>
  <tr><td><font colspan=6 align=center class="text_10">Номера апеллируемых дел:</font></td></tr>
  <tr>
      <td align=center><html:select styleClass="select_f1" size='5' name="abit_Gr" property="shifrFakulteta" tabindex="4">
      </html:select> 
</table>
<table align=center border=0>
  <tr><td colspan=6><html:button styleClass="button" onclick="del_val();" property="del" tabindex="5" value="Исключить"/></td>
      <td colspan=6><html:submit property="full" styleClass="button" tabindex="6" value="Выполнить"/></td>
</html:form>
<html:form action="/apelljatsija?action=null">
  <td><html:submit styleClass="button" tabindex="7" property="exit" value="Выход"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%-----------------   РЕЗУЛЬТАТЫ ПОИСКА № ДЕЛ   -------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Апелляция экзаменационных оценок</template:put>
<template:put name="target_name">Апелляция экзаменационных оценок</template:put>
<template:put name='content'>
<br>
<html:form action="apelljatsija.do?action=save">
<html:hidden name="abit_Gr" property="special3"/>
<html:hidden name="abit_Gr" property="kodPredmeta"/>
<table align=center border=1 cellSpacing=0>
<tr><td valign=top>
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=8 valign=center height=30><font class="text_th">&nbsp;Апелляция экзаменационных оценок по
      <bean:write name="abit_Gr" property="predmet"/></font>
    </td>
</tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;№&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Фамилия И.О.&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Уже&nbsp;была&nbsp;апелляция&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Дата&nbsp;экзамена&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Экз.&nbsp;оц.&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Дата&nbsp;апелляции&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Апелл.&nbsp;оц.&nbsp;</font></td></tr>
<tr><td colspan=8 height=2></td></tr>
</thead>
<logic:iterate id="abits" name="abits_ap" type='abit.bean.AbiturientBean' scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abits" property="number"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abits" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue">
                    <bean:write name="abits" property="familija"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abits" property="apelljatsija"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abits" property="dataJekzamena"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abits" property="special1"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10">
      <html:text accesskey="д" name="abit_Gr" styleClass="text_f10_short" property="<%=\"dap\"+abits.getKodAbiturienta()%>" maxlength="10" size="10" value=""/>
   <td align=center valign=center>&nbsp;<font class="text_10">
      <html:text accesskey="о" name="abit_Gr" styleClass="text_f10_short" property="<%=\"ots\"+abits.getKodAbiturienta()%>" maxlength="2" size="2" value=""/>
</logic:iterate>
<logic:notPresent name="abits" property="familija">
<tr><td align=center valign=center colspan=8>
<p align=center>
     <font class="text_9">Не&nbsp;удалось&nbsp;найти&nbsp;ни&nbsp;одного&nbsp;дела&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=8 height=2></td></tr>
</table></td></tr>
</table>
<br>
<logic:present name="abit_Gr" property="special5">
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<tr><td valign=top><table align=center border=1 cellSpacing=0 FRAME=BOX>
<thead>
<tr><td align=center colspan=2 valign=center height=30><font class="text_th">&nbsp;Ошибки&nbsp;</font></td></tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;№&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Сообщение&nbsp;</font></td>

<tr><td colspan=2 height=2></td></tr>
</thead>
<logic:iterate id="no_nld" name="no_nlds" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="no_nld" property="id"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="no_nld" property="status"/></font>&nbsp;
                                  &nbsp;<font class="text_10"><bean:write name="no_nld" property="message"/></font>&nbsp;</td>
</logic:iterate>
<tr><td colspan=2 height=2></td></tr>
</table>
</logic:present>
<table align=center border=0>
<tr>
  <td align=center vAlign=middle height=35>
    <html:submit styleClass="button" value="Сохранить"/>
  </td>
</html:form>
<html:form action="/apelljatsija.do">
  <td align=center vAlign=middle height=35>
    <html:submit styleClass="button" value="Вернуться назад"/>
  </td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>