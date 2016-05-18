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

<logic:notPresent name='ekzamenyNaSpetsialnostiAction' scope='request'>
 <logic:redirect forward='ekzamenynaspetsialnosti'/>
</logic:notPresent>

<logic:notPresent name="ekzamenyNaSpetsialnostiForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! String kP = "0"; %>

<SCRIPT LANGUAGE="JavaScript">
var teamLength
predmetMass = new Array("","","","","","","","","","","","","","","","","","","","");

function confirmation(){
  if(confirm('Удалить запись?'))
       return true;
  else 
       return false; 
}

function exec() {
teamLength = document.forms(0).special2.options.length
teamTXT = new Array(teamLength)
teamVAL = new Array(teamLength)
for(var ind=0;ind<document.forms(0).special2.length;ind++) {
   eval("teamTXT["+ind+"]=document.forms(0).special2.options["+ind+"].text")
   eval("teamVAL["+ind+"]=document.forms(0).special2.options["+ind+"].value")
}

fillSelect(document.forms(0).kodFakulteta.value);
document.forms(0).kodFakulteta.focus();
// Заполнение массива предметов для последующей проверки числа назначенных предметов 
var j=-1
for(var i=0;i<document.forms(0).special1.value.length;i++)
   if(document.forms(0).special1.value.charAt(i) == "%") j++;
predmetALL = new Array(j);
var j=0
for (var i=0; i<document.forms(0).special1.value.length; i++) 
 {
  temp = "" + document.forms(0).special1.value.substring(i, i+1);
  if(temp == "%") 
   {
    temp = "";
    do
    {
     i++;
     tm = "" + document.forms(0).special1.value.substring(i,i+1);
     if(tm != "%") temp += document.forms(0).special1.value.substring(i, i+1);
      else {
            predmetALL[j] = temp;
            temp=""
            j++   
           }
       if(i==document.forms(0).special1.value.length) break;
     }while(1);
    }
   }
  spec.style.display = "block";
}

function autoInit() {
//снятие отметки с ранее отмеченных предметов
for(var j=0;j<predmetMass.length;j++) {
   if(predmetMass[j]!=""){
     eval("document.forms(0).NaznacCB"+predmetMass[j]+".checked=false");
     predmetMass[j]="";
   }
}
if(document.forms(0).special2.value != "-") {
if(document.forms(0).special2.value.indexOf("%")!=(document.forms(0).special2.value.length-1)) {
var j=0
index = document.forms(0).special2.selectedIndex
for (var i=0; i<document.forms(0).special2.options[index].value.length; i++) 
 {
  temp = "" + document.forms(0).special2.options[index].value.substring(i, i+1);
  if(temp == "%") 
   {
    temp = "";
    do
    {
     i++;
     tm = "" + document.forms(0).special2.options[index].value.substring(i,i+1);
//alert("TM=="+document.forms(0).special2.options[index].value);
     if(tm != "%") temp += document.forms(0).special2.options[index].value.substring(i, i+1);
      else {
            eval("document.forms(0).NaznacCB"+temp+".checked=true");
            predmetMass[j]=temp;
            temp = "";
            j++;
           }
       if(i==document.forms(0).special2.options[index].value.length) {
         eval("document.forms(0).NaznacCB"+temp+".checked=true");
         predmetMass[j]=temp;
         break;
       }
     }while(1);
    }
   }
  }
 }
}

function checkFields() {

 if(document.forms(0).kodFakulteta.value == "-") {
   alert("Выберите факультет")
   return false
 }
 for (var j=0;j<predmetMass.length;j++) predmetMass[j] = "";
var pass=0;
for(var i=0; i<predmetALL.length; i++) {
   if(eval("document.forms(0).NaznacCB"+predmetALL[i]+".checked") == true) pass++;
}
if(pass < 2 || pass > 4) {
  alert("Количество назначенных на специальности предметов должно быть равно 2,3 или 4");
  eval("document.forms(0).NaznacCB"+predmetALL[0]+".focus()")
  return false;
}
return true;
}

function fillSelect(selectCtrl) {
var i,j=0
// Удаление существующих строк
for (i = document.forms(0).special2.length; i >= 0; i--) {
document.forms(0).special2.options[i] = null 
}
if(selectCtrl == "-") {
// Добавление знака "-"
   document.forms(0).special2.options[0] = new Option("-")
   document.forms(0).special2.options[0].value = "-"
   document.forms(0).special2.options[0].selected = true
return
}
j=0
// Добавление новых строк
for(i = 0; i < teamLength; i++) {
   if(teamVAL[i].substring(teamVAL[i].indexOf("+")+1,teamVAL[i].indexOf("%")) == selectCtrl) {
     document.forms(0).special2.options[j] = new Option(teamTXT[i])
     document.forms(0).special2.options[j].value = teamVAL[i]
     j++
   }
}
// Переход в начало списка
document.forms(0).special2.options[0].selected = true
}

function help_me(){
 alert("ВНИМАНИЕ!\nДля правильной работы системы необходимо назначить предметы \nна ВСЕХ специальностях");
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="ekzamenyNaSpetsialnostiForm" property="action">
<bean:define id="action" name="ekzamenyNaSpetsialnostiForm" property="action"/>

<%-----------------------------------------------------------------%>
<%------------ Модификация/удаление одной записи в БД -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">Экзамены на специальности</template:put>
<template:put name="sub_name">Назначение экзаменов на специальность</template:put>
<template:put name='title'>Экзамены на специальности</template:put>
<template:put name='content'>
<BR>
<html:form action="/ekzamenynaspetsialnosti?action=mod_del" onsubmit="return checkFields();">
<html:hidden name="abit_ENS" property="special1"/>
<table cols=4 align=center border=1 cellspacing=0>
  <tr><td align=center><font class="text_10">Факультет:</font></td><td align=center colspan=3><font class="text_10">Специальность:</font></td>
  <tr><td align=center>
      <html:select onchange="fillSelect(this.value),autoInit();" styleClass="select_f2" name="abit_ENS" property="kodFakulteta">
       <html:option value="-"/>
       <html:options collection="abit_ENS_S5" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select>
  <td align=left colspan=3 id="spec" style="display:none">
      <html:select onchange="autoInit();" styleClass="select_f2" name="abit_ENS" property="special2">
       <html:options collection="abit_ENS_S1" property="special2" labelProperty="nazvanieSpetsialnosti"/>
      </html:select>
  <tr><td height=10></td></tr>
  <tr><td align=center colspan=2><font class="text_10">&nbsp;&nbsp;&nbsp;Назначить&nbsp;&nbsp;&nbsp;</font></td>
      <td align=center valign=center>
      <font class="text_10">             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Предмет&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </font></td>

<%-- HEAVY LOGIC INTERFACE BUILDER --%>
      <logic:iterate id="abit_ENS3" name="abit_ENS_S3" scope='request'>
       <bean:define id="kP" name="abit_ENS3" property="kodPredmeta"/>
      <tr><td align=center colspan=2>
      <%out.print("<input type=checkbox name="+"NaznacCB" + kP); %>
      <td>
      <td><font class="text_11"><bean:write name="abit_ENS3" property="predmet"/></font>
      </logic:iterate>

<tr><td colspan=4 align=center valign=center>
<font class="text_11">В&nbsp;настоящий&nbsp;момент&nbsp;в&nbsp;БД&nbsp;
<bean:write name="abit_ENS" property="number"/>&nbsp;специальности(ей)&nbsp;с&nbsp;неназначенными&nbsp;предметами
</font>
</td>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:submit property="change" styleClass="button_sd" value="Добавить/Изменить"/></td>
</html:form>
<html:form action="/ekzamenynaspetsialnosti.do">
  <td><html:button styleClass="button_sd" onclick="help_me();" property="hlp" value="Справка"/></td>
</html:form>
<html:form action="/ekzamenynaspetsialnosti.do">
  <td><html:submit styleClass="button_sd" property="exit" value="Выход"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>