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

<logic:notPresent name='listsFakultsAction' scope='request'>
 <logic:redirect forward='rep_lists_fakults'/>
</logic:notPresent>

<logic:notPresent name="listsFakultsForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength
var groupsLength
var predmetsLength
var pred
var grup

function invokeAct(){
  document.forms(0).submit();
}

function checkFields(){
if(document.forms(0).special7.value == "-"){
  alert("Необходимо выбрать факультет");
  document.forms(0).special7.focus();
  return false;
}
}

/***************************	AUTOINIT	*************************************/

function autoInit(selectCtrl) {
var i=0,j=0
if(selectCtrl == "-") {
// Удаление существующих строк
for (i = document.forms(0).kodPredmeta.length; i >= 0; i--) {
document.forms(0).kodPredmeta.options[i] = null 
}
// Добавление знака "-"
   document.forms(0).kodPredmeta.options[0] = new Option("-")
   document.forms(0).kodPredmeta.options[0].value = "-"
   document.forms(0).kodPredmeta.options[0].selected = true
fillSelect("-");
return
}
j=0
// Удаление существующих строк
for (i = document.forms(0).kodPredmeta.length; i >= 0; i--) {
document.forms(0).kodPredmeta.options[i] = null 
}
// Добавление новых строк
var dis=0
while(selectCtrl.indexOf('%',dis)!=-1)
{
for(i = 0; i < groupsLength; i++) {
if(groupsVAL[i] == selectCtrl.substring(dis,selectCtrl.indexOf('%',dis))){
     document.forms(0).kodPredmeta.options[j] = new Option(groupsTXT[i])
     document.forms(0).kodPredmeta.options[j].value = groupsVAL[i]
     j++
  }
}
dis=selectCtrl.indexOf('%',dis)+1
}
// Переход в начало списка
document.forms(0).kodPredmeta.options[0].selected = true
fillSelect(document.forms(0).kodPredmeta.options[0].value);
}

/******************************	EXEC	************************************************/

function exec() {
if( document.forms(0).kodPredmeta!= null &&
    document.forms(0).special7!= null) {
 	document.forms(0).kodPredmeta.selectedIndex=0;
 	teamLength = document.forms(0).special1.options.length
 	teamTXT = new Array(teamLength)
 	teamVAL = new Array(teamLength)
 	for(var ind=0;ind<document.forms(0).special1.length;ind++) 
	{
    		eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")//text
    		eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
 	}
 	groupsLength = document.forms(0).kodPredmeta.options.length
	groupsTXT = new Array(groupsLength)
 	groupsVAL = new Array(groupsLength)
 	for(var ind=0;ind<document.forms(0).kodPredmeta.length;ind++) 
	{
    		eval("groupsTXT["+ind+"]=document.forms(0).kodPredmeta.options["+ind+"].text")
    		eval("groupsVAL["+ind+"]=document.forms(0).kodPredmeta.options["+ind+"].value")
 	}
 	fillSelect("-");
	autoInit("-");
} 
fak.style.display="block";
pr.style.display="block";
dat.style.display="block";
}

//   FILL SELECT   //

function fillSelect(selectCtrl) {
	var i,j,z=0
	if(selectCtrl == "-") {
		// Удаление существующих строк
		for (i =document.forms(0).special1.length; i >=0 ; i--) {
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
            if(teamVAL[i].substring(0,teamVAL[i].indexOf('%')) == selectCtrl){
              if(eval(teamVAL[i].substring(teamVAL[i].indexOf('%')+1,teamVAL[i].lastIndexOf('%'))) == eval(document.forms(0).special7.value.substring(document.forms(0).special7.value.lastIndexOf('%')+1))) {
			document.forms(0).special1.options[j] = new Option(teamTXT[i])
		  	document.forms(0).special1.options[j].value = teamVAL[i]
			z++
			j++
              }
	   	}
	}

	// Переход в начало списка
	if(z!=0)	document.forms(0).special1.options[0].selected = true
	else
	{
	 document.forms(0).special1.options[0] = new Option("-")
	 document.forms(0).special1.options[0].value = "-"
	 document.forms(0).special1.options[0].selected = true
	}
}
</SCRIPT>

<logic:present name="listsFakultsForm" property="action">
<bean:define id="action" name="listsFakultsForm" property="action"/>

<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Списки по факультетам</template:put>
<template:put name="target_name">Списки по факультетам</template:put>
<template:put name="sub_name">Результаты сдачи экзамена</template:put>
<template:put name='content'>
<body onLoad="exec()"></body>
<BR>
<p align=center>
<html:form action="/rep_lists_fakults?action=report" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td id="fak" style="display:none"><html:select styleClass="select_f2" onchange="autoInit(this.value)" name="abit_O" property="special7" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_O_S3" property="special7" labelProperty="gruppa"/>
      </html:select> 

  <tr><td><font class="text_10">Предмет:</font></td>
      <td id="pr" style="display:none"><html:select styleClass="select_f2" onchange="fillSelect(this.value)" name="abit_O" property="kodPredmeta" tabindex="2">
          <html:options collection="abit_O_S1" property="kodPredmeta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 

  <tr><td><font class="text_10">Дата экзамена:</font></td>
      <td id="dat" style="display:none"><html:select styleClass="select_f2"  name="abit_O" property="special1" tabindex="3">
          <html:options collection="abit_O_S2" property="special1" labelProperty="dataJekzamena"/>
      </html:select>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="4" value="Cоздать отчёт"/></td>
</html:form>
<html:form method="post" action="/rep_lists_fakults?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="5" value="Выход"/></td>
</html:form>
</tr></table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>