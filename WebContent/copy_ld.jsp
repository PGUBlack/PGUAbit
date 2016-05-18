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

<logic:notPresent name='copyldAction' scope='request'>
 <logic:redirect forward='copy_ld'/>
</logic:notPresent>

<logic:notPresent name="copyldForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var str,old,news

function add_val() {
 if(document.forms(0).special1.value.length != 0 && 
    document.forms(0).special2.value.length != 0 && 
    document.forms(0).special1.value != document.forms(0).special2.value) {
//Проверка на соответствие ранее введенным
    for(var i=0;i<document.forms(0).shifrFakulteta.length;i++){
       str = document.forms(0).shifrFakulteta.options[i].text;
       old=str.substring(0,str.indexOf('-')-1);
       news=str.substring(str.indexOf('>')+2);
       if(document.forms(0).special1.value == old && document.forms(0).special2.value == news){
         alert("Внимание! Вводимые номера дел уже присутствуют в списке");
         document.forms(0).special1.focus();
         return;
       }
    }
    document.forms(0).shifrFakulteta.options[document.forms(0).shifrFakulteta.length] = new Option(document.forms(0).special1.value+
     " -> "+document.forms(0).special2.value);
    document.forms(0).special1.value = "";
    document.forms(0).special2.value = "";
  } else {
    alert("Внимание! Необходимо задать различные старый и новый номера личных дел.");
    document.forms(0).special1.focus();
    return;
  }
}

function checkFields() {
 if(document.forms(0).shifrFakulteta.length==0) {
   alert("Список номеров копируемых дел не может быть пустым!");
   document.forms(0).shifrFakulteta.focus();
   return false;
 }
//Компановка строки списка номеров
  document.forms(0).special3.value="";
    for(var i=0;i<document.forms(0).shifrFakulteta.length;i++){
       str = document.forms(0).shifrFakulteta.options[i].text;
       old=str.substring(0,str.indexOf('-')-1);
       news=str.substring(str.indexOf('>')+2);
       document.forms(0).special3.value += ">"+old+"<"+news;
    }
  document.forms(0).special3.value+=">";
}

function del_val() {
var index = document.forms(0).shifrFakulteta.selectedIndex;
if(index==-1) return;
document.forms(0).shifrFakulteta.options[index] = null;
}
</SCRIPT>

<logic:present name="copyldForm" property="action">
<bean:define id="action" name="copyldForm" property="action"/>


<%-----------------------------------------------------------------%>
<%---------------------- ФОРМА ВВОДА №-в ДЕЛ ----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<body onLoad="document.forms(0).special1.focus();"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Копирование личных дел абитуриентов</template:put>
<template:put name="target_name">Копирование личных дел абитуриентов</template:put>
<template:put name='content'>
<html:form action="/copy_ld?action=doit" onsubmit="return checkFields();">
<html:hidden name="abit_Gr" property="special3"/>
<table valign=top align=center border=0 cellSpacing=0 cellPadding=0>
  <tr><td align=center height=16 colspan=6><font class="text_11">Номер личного дела:</font></td></tr>
  <tr><td align=center height=16 colspan=6><hr></td></tr>

  <tr><td height=36><font class="text_10">Старый:&nbsp;</font></td>

      <td height=36><html:text accesskey="с" name="abit_Gr" styleClass="text_f10" property="special1"
                     maxlength="10" size="10" tabindex="1" value=""/>

      <td height=36>&nbsp;&nbsp;&nbsp;<img border=0 src="<%="layouts/"+tema+"/img/arrow.gif"%>">&nbsp;&nbsp;&nbsp;</td>

      <td height=36><font class="text_10">Новый:&nbsp;</font></td>

      <td height=36><html:text accesskey="н" name="abit_Gr" styleClass="text_f10" property="special2"
                     maxlength="10" size="10" tabindex="2" value=""/>

  <tr><td align=center height=16 colspan=6><hr></td></tr>
  <tr><td height=36 colspan=6 align=center><html:button styleClass="button" property="add" tabindex="3" onclick="add_val();" value="Добавить"/></td></tr>
</table>
<table align=center border=0>
  <tr><td><font colspan=6 align=center class="text_10">Номера копируемых дел:</font></td></tr>
  <tr>
      <td align=center><html:select styleClass="select_f1" size='5' name="abit_Gr" property="shifrFakulteta" tabindex="4">
      </html:select> 
</table>
<table align=center border=0>
  <tr><td></td>
      <td height=5><td></td></tr>
  <tr><td><html:checkbox accesskey="у" styleClass="checkbox_1" name='abit_Gr' property='special7'/></td>
      <td><font colspan=6 align=center class="text_10">Установить срок обуч.:&nbsp;</font></td>
      <td>
          <html:select styleClass="select_f1" name="abit_Gr" property="special6" tabindex="5">
            <html:option value="6,0">6,0</html:option>
            <html:option value="5,0">5,0</html:option>
            <html:option value="5,5">5,5</html:option>
            <html:option value="4,5">4,5</html:option>
          </html:select>
      </td>
  </tr>
  <tr><td></td>
      <td><font colspan=6 align=center class="text_10">Сохранять старые дела:&nbsp;</font></td>
      <td>
        <html:select styleClass="select_f1" name="abit_Gr" property="special4" tabindex="6">
         <html:option value="no">нет</html:option>
         <html:option value="yes">да</html:option>
        </html:select>
      </td>
  </tr>
  <tr><td></td><td height=5></tr>
</table>
<table align=center border=0>
  <tr><td colspan=6><html:button styleClass="button" onclick="del_val();" property="del" tabindex="7" value="Исключить"/></td>
      <td colspan=6><html:submit property="full" styleClass="button" tabindex="8" value="Выполнить"/></td>
</html:form>
<html:form action="/copy_ld?action=null">
  <td><html:submit styleClass="button" tabindex="9" property="exit" value="Выход"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ РЕЗУЛЬТАТЫ КОПИРОВАНИЯ ДЕЛ -------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Копирование личных дел абитуриентов</template:put>
<template:put name="target_name">Копирование личных дел абитуриентов</template:put>
<template:put name='content'>
<br>
<html:form action="copy_ld.do?action=create">
</html:form>
<table align=center border=1 cellSpacing=0>
<tr><td valign=top>
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=4 valign=center height=30><font class="text_th">&nbsp;Успешно&nbsp;</font></td></tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;№&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Фамилия И.О.&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Было&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Стало&nbsp;</font></td></tr>
<tr><td colspan=4 height=2></td></tr>
</thead>
<logic:iterate id="good_mes" name="messages_good" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="good_mes" property="id"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="good_mes" paramId="kodAbiturienta" paramProperty="initiator" styleClass="link_hov_blue">
                    <bean:write name="good_mes" property="descr"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="good_mes" property="status"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="good_mes" property="message"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="good_mes" property="id">
<tr><td align=center valign=center colspan=4>
<p align=center>
     <font class="text_9">Не&nbsp;удалось&nbsp;скопировать&nbsp;ни&nbsp;одного&nbsp;дела&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=4 height=2></td></tr>
</table></td></tr>
</table>
<br>
<logic:present name="abit_Gr" property="special5">
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<tr><td valign=top><table align=center border=1 cellSpacing=0 FRAME=BOX>
<thead>
<tr><td align=center colspan=2 valign=center height=30><font class="text_th">&nbsp;Ошибка&nbsp;</font></td></tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;№&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Сообщение&nbsp;</font></td>

<tr><td colspan=2 height=2></td></tr>
</thead>
<logic:iterate id="bad_mes" name="messages_bad" scope='request'>
<tr valign="center">
   <td align=center>&nbsp;<font class="text_10"><bean:write name="bad_mes" property="id"/></font>&nbsp;</td>
   <td>&nbsp;<font class="text_10"><bean:write name="bad_mes" property="status"/></font>&nbsp;
                                  &nbsp;<font class="text_10"><bean:write name="bad_mes" property="message"/></font>&nbsp;</td>
</logic:iterate>
<tr><td colspan=2 height=2></td></tr>
</table>
</logic:present>
<table align=center border=0>
<tr>
<html:form action="/copy_ld.do?action=new">
  <td align=center vAlign=middle height=35>
    <html:submit styleClass="button" value="Вернуться назад"/>
  </td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>