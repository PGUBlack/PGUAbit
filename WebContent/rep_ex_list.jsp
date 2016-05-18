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

<logic:notPresent name='exListAction' scope='request'>
 <logic:redirect forward='rep_ex_list'/>
</logic:notPresent>

<logic:notPresent name="blankForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

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
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление знака "-"
if(selectCtrl == "-") {
   document.forms(0).special1.options[0] = new Option("-");
   document.forms(0).special1.options[0].value = "-";
   document.forms(0).special1.options[0].selected = true;
   return;
}
j=1
   document.forms(0).special1.options[0] = new Option("*");
   document.forms(0).special1.options[0].value = "-1";

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

<body onLoad="exec()"></body>

<logic:present name="blankForm" property="action">
<bean:define id="action" name="blankForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Параметры просмотра ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Экзаменационные листы абитуриентов</template:put>
<template:put name="target_name">Экзаменационные листы абитуриентов</template:put>
<template:put name="sub_name">по факультету или специальности</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_ex_list?action=report" onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
  <tr>
    <td><font class="text_10">Факультет:</font></td>
    <td>
      <html:select onchange="fillSelect(this.value)" styleClass="select_f2" name="abit_O" property="kodFakulteta">
       <html:option value="-"/>
       <html:options collection="abit_O_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select>
    </td>  
  </tr>
  <tr>
    <td><font class="text_10">Специальность:</font></td>
    <td align=left>
      <html:select styleClass="select_f2" name="abit_O" property="special1">
       <html:options collection="abit_O_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
    </td>
  </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="Просмотр"/>&nbsp;&nbsp;</td>
</html:form>
<html:form method="post" action="/rep_ex_list?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="Выход"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>