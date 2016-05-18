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

<logic:notPresent name='blankAction' scope='request'>
 <logic:redirect forward='rep_blank'/>
</logic:notPresent>

<logic:notPresent name="blankForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength
var groupsLength
var predmetsLength
var pred
var grup
function sohrSel() 
{
//сохранение кода предмета
  document.forms(0).kodPredmeta.options[0].value =  document.forms(0).kodPredmeta.options[document.forms(0).kodPredmeta.selectedIndex].value
//сохранение даты
  document.forms(0).special1.options[document.forms(0).special1.selectedIndex].value = document.forms(0).special1.options[document.forms(0).special1.selectedIndex].text
//сохранение группы
  document.forms(0).special7.options[document.forms(0).special7.selectedIndex].value =  document.forms(0).special7.options[document.forms(0).special7.selectedIndex].text
}

function invokeAct(){
  document.forms(0).submit();
}

function checkFields(){
 if(document.forms(0).kodPredmeta.value == "-"){
   alert("Необходимо выбрать предмет");
   document.forms(0).kodPredmeta.focus();
   return false;
 }
}

//**********************************************************************************************
//******************************	EXEC	************************************************
//**********************************************************************************************
function exec() {

 if( document.forms(0).kodPredmeta!= null &&
   document.forms(0).special1!= null) {
     document.forms(0).kodPredmeta.selectedIndex=0;
     teamLength = document.forms(0).special1.options.length
     teamTXT = new Array(teamLength)
     teamVAL = new Array(teamLength)
     for(var ind=0;ind<document.forms(0).special1.length;ind++) 
     {
       eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
       eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
     }
     groupsLength = document.forms(0).special7.options.length
     groupsTXT = new Array(groupsLength)
     groupsVAL = new Array(groupsLength)
     for(var ind=0;ind<document.forms(0).special7.length;ind++) 
     {
       eval("groupsTXT["+ind+"]=document.forms(0).special7.options["+ind+"].text")
       eval("groupsVAL["+ind+"]=document.forms(0).special7.options["+ind+"].value")
     }
     fillSelect("-");
     autoInit("-");
   }

   if( document.forms(0).kodPredmeta == null && document.forms(0).special5 != null) {

     document.forms(0).special5.focus();
   }
   pr.style.display = "block";
   dat.style.display = "block";
   gr.style.display = "block";
}

/***********************************************************************************/
/********************************	AUTOINIT	****************************/
/***********************************************************************************/
function autoInit(selectCtrl) {
 var i=0,j=0

// Удаление существующих строк

 for(i = document.forms(0).special7.length; i >= 0; i--) {
    document.forms(0).special7.options[i] = null 
 }

// Добавление знака "-"

 if(selectCtrl == "-" || groupsLength == 0) {
   document.forms(0).special7.options[0] = new Option("-");
   document.forms(0).special7.options[0].value = "-";
   document.forms(0).special7.options[0].selected = true;
   return;
 }

j=0
i=0

// Добавление новых строк

 if(document.forms(0).special1.value != "-"){
   for(i = 0; i < groupsLength; i++) {

      if(groupsVAL[i].substring(groupsVAL[i].indexOf("%")+1,groupsVAL[i].indexOf("+")) == selectCtrl && document.forms(0).special1.options[document.forms(0).special1.selectedIndex].text ==  groupsVAL[i].substring(0,10)) {
        document.forms(0).special7.options[j] = new Option(groupsTXT[i])
        document.forms(0).special7.options[j].value = groupsVAL[i]
        j++
      }
   }
 }
 if(selectCtrl == "-" || j == 0){
   document.forms(0).special7.options[0] = new Option("-")
   document.forms(0).special7.options[0].value = "-"
   document.forms(0).special7.options[0].selected = true
 }

// Переход в начало списка

 document.forms(0).special7.options[0].selected = true
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//			FILL SELECT							       //
/////////////////////////////////////////////////////////////////////////////////////////////////
function fillSelect(selectCtrl) {

  var i,j,z=0

// Удаление существующих строк

  for (i=document.forms(0).special1.length; i >=0 ; i--) {
      document.forms(0).special1.options[i] = null
  }

// Добавление знака "-"

  if(selectCtrl == "-" || teamLength == 0) {
    document.forms(0).special1.options[0] = new Option("-")
    document.forms(0).special1.options[0].value = "-"
    document.forms(0).special1.options[0].selected = true
    return;
  }

  j=0

// Добавление новых строк

  for(i = 0; i < teamLength; i++) {
     if(teamVAL[i].substring(0,2) == selectCtrl) {
       document.forms(0).special1.options[j] = new Option(teamTXT[i])
       document.forms(0).special1.options[j].value = teamVAL[i]
       z++
       j++
     }
  }

// Переход в начало списка

  if(z!=0) document.forms(0).special1.options[0].selected = true
  else {
   document.forms(0).special1.options[0] = new Option("-")
   document.forms(0).special1.options[0].value = "-"
   document.forms(0).special1.options[0].selected = true
  }

}
</SCRIPT>

<logic:present name="blankForm" property="action">
<bean:define id="action" name="blankForm" property="action"/>

<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Чистые ведомости по группам</template:put>
<template:put name="sub_name">Выберите группу</template:put>
<template:put name='title'>Чистые ведомости</template:put>
<template:put name='content'>
<body onLoad="exec()"></body>
<BR>
<p align=center>
<html:form action="/rep_blank?action=report" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">&nbsp;Предмет:&nbsp;</font></td>
      <td id="pr" style="display:none"><html:select styleClass="select_f2" onchange="fillSelect(this.value),autoInit(this.value);" name="abit_O" property="kodPredmeta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_O_S1" property="kodPredmeta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 

  <tr><td><font class="text_10">&nbsp;Дата&nbsp;экзамена:&nbsp;</font></td>
      <td id="dat" style="display:none"><html:select styleClass="select_f2" onchange="autoInit(this.value);" name="abit_O" property="special1" tabindex="2">
          <html:options collection="abit_O_S2" property="special1" labelProperty="dataJekzamena"/>
      </html:select>

  <tr><td><font class="text_10">&nbsp;Группа:&nbsp;</font></td>
      <td id="gr" style="display:none"><html:select styleClass="select_f2" name="abit_O" property="special7" tabindex="3">
          <html:options collection="abit_O_S3" property="special7" labelProperty="gruppa"/>
      </html:select> 

  <tr><td><font class="text_10">&nbsp;Искл.&nbsp;абит.&nbsp;с&nbsp;баллами:&nbsp;</font></td>
      <td><html:text accesskey="и" name="abit_O" styleClass="text_f10_short" property="special2" 
                     maxlength="25" size="10" value="0,1,2"/>

</table>
<BR>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" onclick="sohrSel()" tabindex="5" value="Cоздать отчёт"/>&nbsp;&nbsp;</td>
</html:form>
<html:form method="post" action="/rep_blank?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="7" value="Выход"/></td>
</html:form>
</tr></table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>