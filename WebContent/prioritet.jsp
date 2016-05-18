<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
    import      = "abit.util.StringUtil"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='prioritetAction' scope='request'>
 <logic:redirect forward='gener_ege'/>
</logic:notPresent>

<logic:notPresent name="prioritetForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! String kP = "0"; %>

<SCRIPT LANGUAGE="JavaScript">
var teamLength

function checkFields()
{
 if(document.forms(0).kodFakulteta.value == "-")
 {
  alert("Необходимо выбрать Факультет");
  document.forms(0).kodFakulteta.focus();
  return false;
 }
}

function exec() {
    document.forms(0).kodFakulteta.selectedIndex=0;
    teamLength = document.forms(0).special1.options.length
    teamTXT = new Array(teamLength)
    teamVAL = new Array(teamLength)
    for(var ind=0;ind<document.forms(0).special1.length;ind++) {
       eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
       eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
    }
 fillSelect(document.forms(0).kodFakulteta.value);
 document.forms(0).kodFakulteta.focus();
}

function exec2() {
}

function fillSelect(selectCtrl) {
var i,j=0
if(selectCtrl == "-") {
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление знака "-"
   document.forms(0).special1.options[0] = new Option("-")
   document.forms(0).special1.options[0].value = "-"
   document.forms(0).special1.options[0].selected = true
return
}
j=0
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление новых строк
for(i = 0; i < teamLength; i++) {
   if((teamVAL[i]).substring(0,(teamVAL[i]).indexOf("$")) == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     j++
   }
}
// Переход в начало списка
document.forms(0).special1.options[0].selected = true
}


</SCRIPT>


<logic:present name="prioritetForm" property="action">
<bean:define id="action" name="prioritetForm" property="action"/>

<logic:equal name="action" value="translate">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Установка приоритетов</template:put>
<template:put name="sub_name">Укажите критерии</template:put>
<template:put name='title'>Установка приоритеты</template:put>
<template:put name='content'>
<BR>
<html:form action="/prioritet?action=md_dl" onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
  <tr>
    <td><font class="text_10">Факультет:</font></td>
    <td>
      <html:select onchange="fillSelect(this.value)" styleClass="select_f2" name="abit_ENS" property="kodFakulteta">
       <html:option value="-"/>
       <html:options collection="abit_ENS_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select>
    </td>  
  </tr>
  <tr>
    <td><font class="text_10">Специальность:</font></td>
    <td align=left>
      <html:select styleClass="select_f2" name="abit_ENS" property="special1">
       <html:options collection="abit_ENS_S3" property="special1" labelProperty="abbreviatura"/>
      </html:select>
    </td>
  </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="Задать приоритеты"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/prioritet.do">
  <td><html:submit styleClass="button" property="exit" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<logic:equal name="action" value="md_dl">
<body onLoad="exec2()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">Задание приоритетов</template:put>
<template:put name="sub_name">Приоритеты</template:put>
<template:put name='title'>Задание приоритетов для специальности <bean:write name="abit_ENS" property="nazvanieSpetsialnosti"/></template:put>
<template:put name='content'>
<BR>
<html:form action="/prioritet?action=mod_del">
<html:hidden name="abit_ENS" property="special1"/>
<html:hidden name="abit_ENS" property="kodFakulteta"/>
<html:hidden name="abit_ENS" property="kodSpetsialnosti"/>
<table align=center border=1 cellspacing=0>
  <tr>
    <td align=center width="90%" colspan=40>
        <font class="text_11">&nbsp;&nbsp;&nbsp;<bean:write name="abit_ENS" property="abbreviatura"/>&nbsp;&nbsp;&nbsp;</font>
    </td>
  </tr>
	<logic:iterate id="abit_ENS5" name="abit_ENS_S5" scope='request' type="abit.bean.AbiturientBean">
       <bean:define id="kP" name="abit_ENS5" property="kodPredmeta"/>
  
      <tr>
      <td>
      <td><font class="text_11"><bean:write name="abit_ENS5" property="predmet"/></font>
      <td> <html:text styleClass="text_f9_short" property="<%=\"Prioritet\" + kP%>" value="<%=abit_ENS5.getPrioritet()%>"
                       maxlength="3" size="1"/>
             </td>
      </logic:iterate>
</table>
<br>
<table align=center border=0 cols=2>
<tr><td align=center>
  <td><html:submit styleClass="button_sd" value="Задать"/></td>
</html:form>

<html:form action="/prioritet.do">
  <td><html:submit styleClass="button_sd" property="exit" value="Выход"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%-----------------------   УСПЕШНЫЙ ВВОД   -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="mod_del">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">Приоритеты успешно внесены в базу данных</template:put>
<template:put name="target_name">Приоритеты добавлены</template:put>
<template:put name="content">

<html:form action="/prioritet.do">
<html:hidden name="abit_ENS" property="kodSpetsialnosti"/>
<table align="center" border="0">
  <tr align="center">
    <td><html:submit styleClass="button" value="Ввод новых приоритетов" tabindex="2"/>&nbsp;&nbsp;</td>
</html:form>
   <html:form action="/prioritet.do">
  <td><html:submit styleClass="button_sd" property="exit" value="Выход"/></td>
</html:form>
  </tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>