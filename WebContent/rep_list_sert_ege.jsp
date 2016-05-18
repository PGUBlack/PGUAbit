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

<logic:notPresent name='listSertEgeAction' scope='request'>
 <logic:redirect forward='rep_list_sert_ege'/>
</logic:notPresent>

<logic:notPresent name="listSertEgeForm" property="action">
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


<logic:present name="listSertEgeForm" property="action">
<bean:define id="action" name="listSertEgeForm" property="action"/>

<%-----------------------------------------------------------------%>
<%---------   НАСТРОЙКА ПАРАМЕТРОВ ГЕНЕРАЦИИ СЕРТИФИКАТОВ  --------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="translate">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Списки с сертификатами и баллами ЕГЭ</template:put>
<template:put name="sub_name">Введите параметры</template:put>
<template:put name='title'>Списки с сертификатами и баллами ЕГЭ</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_list_sert_ege?action=report" onsubmit="return checkFields();">
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
       <html:options collection="abit_ENS_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
    </td>
  </tr>
  <tr>
     <td><font class="text_10">Шифры отличий:</font></td>
     <td>
      <html:text accesskey="ш" styleClass="text_f10_short" name="abit_ENS" size='12' maxlength='50' property="shifrMedali"/>
     </td>
  </tr>
  <tr>
     <td><font class="text_10">Копия сертификата:</font></td>
     <td valign=center>
      <html:select name="abit_ENS" styleClass="select_f2" property="kopijaSertifikata">
        <html:option value="%">*</html:option>
        <html:option value="-">-</html:option>
        <html:option value="no">нет</html:option>
        <html:option value="yes">да</html:option>
      </html:select>
     </td>
  </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="Сформировать"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/rep_list_sert_ege.do">
  <td><html:submit styleClass="button" property="exit" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>