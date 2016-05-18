<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
    import      = "abit.Constants"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='raspisanieAction' scope='request'>
 <logic:redirect forward='raspisanie'/>
</logic:notPresent>

<logic:notPresent name="raspisanieForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

  var massLength;
  var massKodFak;
  var massGruppy;

function checkFields(){
var valid = " 1234567890"
var temp;

if(document.forms(0).kodFakulteta!= null){
  if(document.forms(0).kodFakulteta.value == "-") {
    alert("Необходимо выбрать факультет");
    document.forms(0).kodFakulteta.focus();
    return false;
   }
  if(document.forms(0).special1.value == "-") {
    alert("Необходимо выбрать группу");
    document.forms(0).special1.focus();
    return false;
   }
}
if(document.forms(0).kodPredmeta.value == "-") 
 {
  alert("Необходимо выбрать предмет");
  document.forms(0).kodPredmeta.focus();
  return false;
 }
if(document.forms(0).nomerPotoka.options == null){
   for (var i=0; i<document.forms(0).nomerPotoka.value.length; i++) 
    {
     temp = "" + document.forms(0).nomerPotoka.value.substring(i, i+1);
     if (valid.indexOf(temp) == "-1") {
         alert("Поле ''Номер потока'' может содержать только цифры!");
         document.forms(0).nomerPotoka.focus();
         return false;
     }
    }
   if(document.forms(0).nomerPotoka.value.length == 0) 
    {
     alert("Необходимо заполнить поле ''Номер потока''");
     document.forms(0).nomerPotoka.focus();
     return false;
    }
 }
}
 
function invokeAct(){
  document.forms(0).submit();
}

function chgPotok(){
  alert("Предупреждение! Если Вы измените поток, то\nон изменится сразу у всей группы.");
}

function fillGroupField(){
  var tmp = document.forms(0).special1.value;
  if(tmp != "" && tmp != "-"){
    tmp = tmp.substring(tmp.indexOf('$')+1,tmp.indexOf('$')+2);
    document.forms(0).nomerPotoka.value = tmp;
  } else
    document.forms(0).nomerPotoka.value = "";
}

function confirmation(){
  if(confirm('Удалить Расписание для выбранной группы?'))
    return true;
  else 
    return false; 
}

function exec1() {
    massLength = document.forms(0).special1.length-1;
    massKodFak = new Array(massLength);
    massGruppy = new Array(massLength);
  for(var currentIndex=1;currentIndex<document.forms(0).special1.length;currentIndex++){
   massKodFak[currentIndex-1] = document.forms(0).special1.options[currentIndex].value;
   massGruppy[currentIndex-1] = document.forms(0).special1.options[currentIndex].text;
  }
   fillSelect("-");
   fak.style.display = "block";
   gruppa.style.display = "block";
   document.forms(0).kodPredmeta.options[0].selected = true;
   document.forms(0).kodFakulteta.focus();
}

function exec2() {
    massLength = document.forms(0).special1.length-1;
    massKodFak = new Array(massLength);
    massGruppy = new Array(massLength);
  for(var currentIndex=1;currentIndex<document.forms(0).special1.length;currentIndex++){
   massKodFak[currentIndex-1] = document.forms(0).special1.options[currentIndex].value;
   massGruppy[currentIndex-1] = document.forms(0).special1.options[currentIndex].text;
  }
//   fillSelect("-");
   fillSelect(document.forms(0).kodFakulteta.value);
   fak.style.display = "block";
   gruppa.style.display = "block";
   document.forms(0).kodFakulteta.focus();
}

function exec3() {
   document.forms(0).dataKonsultatsii.focus();
}
function fillSelect(selectCtrl) {
  var i,j,offset,next_offset;
  if(selectCtrl == "-"){
    // Удаление существующих строк
    for (i = document.forms(0).special1.length; i >= 0; i--){
      document.forms(0).special1.options[i] = null; 
    }
    // Добавление знака "-"
    document.forms(0).special1.options[0] = new Option("-");
    document.forms(0).special1.options[0].value = "-";
    document.forms(0).special1.options[0].selected = true;
    fillGroupField();
    return;
  }
j=0;
  // Удаление существующих строк
  for (i = document.forms(0).special1.length; i >= 0; i--){
    document.forms(0).special1.options[i] = null
  }
    document.forms(0).special1.options[0] = new Option("-");
    document.forms(0).special1.options[0].value = "-";
  // Добавление новых строк 
  for(i = 0; i < massLength; i++){
    if(massKodFak[i] == selectCtrl){
     offset=0;
      while(1){
          next_offset = massGruppy[i].indexOf('%',offset+1);
           document.forms(0).special1.options[j] = new Option(massGruppy[i].substring(offset,next_offset));
          offset = massGruppy[i].indexOf('%',next_offset+1);
          if(offset == -1) {
           document.forms(0).special1.options[j].value = massGruppy[i].substring(next_offset+1);
           break;
          } else 
           document.forms(0).special1.options[j].value = massGruppy[i].substring(next_offset+1);
          offset+=1;
          j++;
      }
    }
  }
  // Переход в начало списка
  fillGroupField();
  document.forms(0).special1.options[0].selected = true
}
</SCRIPT>

<logic:present name="raspisanieForm" property="action">
<bean:define id="action" name="raspisanieForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<body onLoad="exec2()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Расписание</template:put>
<template:put name="target_name">Расписание вступительных экзаменов</template:put>
<template:put name="sub_name">Добавление записи</template:put>
<template:put name='content'>
<logic:present name="message" property="message">
<p align=center><font class="message"><bean:write name="message" property="message"/></font></p>
<%-- Повторный ввод --%>
<html:form action="/raspisanie?action=create&priznakSortirovki=-15&stolbetsSortirovki=-1" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td id="fak" style="display:none"><html:select onchange="fillSelect(this.value);" name="abit_Rasp" 
                       styleClass="select_f1" property="kodFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_Rasp_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta" />
          </html:select>

  <tr><td><font class="text_10">Группа:</font></td>
      <td id="gruppa" style="display:none"><html:select onchange="fillGroupField();" name="abit_Rasp" styleClass="select_f1" property="special1" tabindex="2">
          <html:option value="-"/>
          <html:options collection="abit_Rasp_S5" property="special1" labelProperty="special2" />
          </html:select>

  <tr><td><font class="text_10">Предмет:</font></td>
      <td><html:select name="abit_Rasp" styleClass="select_f1" property="kodPredmeta" tabindex="3">
          <html:option value="-"/>
          <html:options collection="abit_Rasp_S2" property="kodPredmeta" labelProperty="sokr" />
          </html:select>

  <tr><td><font class="text_10">Номер потока:</font></td>
      <td><html:text accesskey="н" name="abit_Rasp" styleClass="text_f10_short" readonly='true' property="nomerPotoka" 
                     maxlength="2" size="2"/>

  <tr><td><font class="text_10">Дата консультации:</font></td>
      <td><html:text accesskey="д" name="abit_Rasp" styleClass="text_f10_short" property="dataKonsultatsii"
                     maxlength="10" size="10" tabindex="4"/>

  <tr><td><font class="text_10">Аудитория консультации:</font></td>
      <td><html:text accesskey="а" name="abit_Rasp" styleClass="text_f10_short" property="auditorijaKonsultatsii" 
                     maxlength="10" size="10" tabindex="5"/>

  <tr><td><font class="text_10">Дата экзамена:</font></td>
      <td><html:text accesskey="д" name="abit_Rasp" styleClass="text_f10_short" property="dataJekzamena" 
                     maxlength="10" size="10" tabindex="6"/>

  <tr><td><font class="text_10">Аудитория экзамена:</font></td>
      <td><html:text accesskey="а" name="abit_Rasp" styleClass="text_f10_short" property="auditorijaJekzamena" 
                     maxlength="10" size="10" tabindex="7"/>

</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="8" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="9" value="Отменить"/></td>
</html:form>
<html:form action="/raspisanie?action=create&priznakSortirovki=-15&stolbetsSortirovki=-1">
  <td><html:submit property="full" styleClass="button" tabindex="10" value="Просмотр таблицы"/></td>
</html:form>
<html:form action="/gruppy?action=new">
  <td><html:submit styleClass="button" tabindex="11" value="Группы"/></td>
</html:form>
<html:form method="post" action="/raspisanie?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="12" value="Выход"/></td>
</html:form>
</table>
</logic:present>
<logic:notPresent name="message" property="message">
<body onLoad="exec1()"></body>
<br><br>
<%-- Первоначальный ввод --%>
<html:form action="/raspisanie?action=create&priznakSortirovki=-15&stolbetsSortirovki=-1" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">Факультет:</font></td>
      <td id="fak" style="display:none"><html:select onchange="fillSelect(this.value);" name="abit_Rasp" 
                       styleClass="select_f1" property="kodFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_Rasp_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta" />
          </html:select>

  <tr><td><font class="text_10">Группа:</font></td>
      <td id="gruppa" style="display:none"><html:select onchange="fillGroupField();" name="abit_Rasp" styleClass="select_f1" property="special1" tabindex="2">
          <html:option value="-"/>
          <html:options collection="abit_Rasp_S5" property="special1" labelProperty="special2" />
          </html:select>

  <tr><td><font class="text_10">Предмет:</font></td>
      <td><html:select name="abit_Rasp" styleClass="select_f1" property="kodPredmeta" tabindex="3">
          <html:option value="-"/>
          <html:options collection="abit_Rasp_S2" property="kodPredmeta" labelProperty="sokr" />
          </html:select>

  <tr><td><font class="text_10">Номер потока:</font></td>
      <td><html:text accesskey="н" name="abit_Rasp" styleClass="text_f10_short" readonly='true' property="nomerPotoka" 
                     maxlength="2" size="2" value=""/>

  <tr><td><font class="text_10">Дата консультации:</font></td>
      <td><html:text accesskey="д" name="abit_Rasp" styleClass="text_f10_short" property="dataKonsultatsii"
                     maxlength="10" size="10" tabindex="4" value="00-00-0000"/>

  <tr><td><font class="text_10">Аудитория консультации:</font></td>
      <td><html:text accesskey="а" name="abit_Rasp" styleClass="text_f10_short" property="auditorijaKonsultatsii" 
                     maxlength="10" size="10" tabindex="5" value=""/>

  <tr><td><font class="text_10">Дата экзамена:</font></td>
      <td><html:text accesskey="д" name="abit_Rasp" styleClass="text_f10_short" property="dataJekzamena" 
                     maxlength="10" size="10" tabindex="6" value="00-00-0000"/>

  <tr><td><font class="text_10">Аудитория экзамена:</font></td>
      <td><html:text accesskey="а" name="abit_Rasp" styleClass="text_f10_short" property="auditorijaJekzamena" 
                     maxlength="10" size="10" tabindex="7" value=""/>

</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="8" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="9" value="Отменить"/></td>
</html:form>
<html:form action="/raspisanie?action=create&priznakSortirovki=-15&stolbetsSortirovki=-1">
  <td><html:submit property="full" styleClass="button" tabindex="10" value="Просмотр таблицы"/></td>
</html:form>
<html:form action="/gruppy?action=new">
  <td><html:submit styleClass="button" tabindex="11" value="Группы"/></td>
</html:form>
<html:form method="post" action="/raspisanie?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="12" value="Выход"/></td>
</html:form>
</table>
</logic:notPresent>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Расписание</template:put>
<template:put name="target_name">Расписание вступительных экзаменов</template:put>
<template:put name='content'>
<br>
<html:form action="raspisanie.do?action=create&full">
<p align=center>
<font class="text_10">Предмет:</font>
      <html:select onchange="invokeAct();" styleClass="select_f1" name="abit_Rasp" property="priznakSortirovki" tabindex="2">
       <html:option value="-15">все предметы</html:option>
       <html:options collection="abit_Rasp_S2" property="kodPredmeta" labelProperty="predmet" />
      </html:select>
&nbsp;&nbsp;&nbsp;&nbsp;
<font class="text_10">Поток:</font>
      <html:select onchange="invokeAct();" styleClass="select_f1" name="abit_Rasp" property="stolbetsSortirovki" tabindex="3">
       <html:option value="-1">все потоки</html:option>
       <html:options collection="abit_Rasp_S4" property="nomerPotoka"/>
      </html:select>
<BR><BR>
<logic:iterate id="abit_Rasp" name="abit_Rasp_S6" scope='request'>
<html:link href="raspisanie.do?action=full" paramId="letter" paramName="abit_Rasp" paramProperty="kodFakulteta" styleClass="link_hov_blue">
        <bean:write name="abit_Rasp" property="abbreviaturaFakulteta"/>
</html:link>
</logic:iterate>
</html:form>

<table cols=7 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center height=30><font class="text_th">&nbsp;Группа&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Дата&nbsp;консультации&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Ауд.&nbsp;конс.&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Дата&nbsp;экзамена&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Ауд.&nbsp;экз.&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Предмет&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;Поток&nbsp;</font></td></tr>
</thead>
<logic:iterate id="abit_Rasp" name="abits_Rasp" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Rasp" property="gruppa"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<html:link href="raspisanie.do?action=mod_del" paramName="abit_Rasp" 
                   paramProperty="kodRaspisanija" paramId="kodRaspisanija" styleClass="link_hov_blue">
                                 <bean:write name="abit_Rasp" property="dataKonsultatsii"/></html:link>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Rasp" property="auditorijaKonsultatsii"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Rasp" property="dataJekzamena"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Rasp" property="auditorijaJekzamena"/></font>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Rasp" property="predmet"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Rasp" property="nomerPotoka"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_Rasp" property="kodRaspisanija">
<tr>
  <td align=center valign=center colspan=7><font class="text_11">
     Для&nbsp;выбранного&nbsp;факультета&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;строки</font></td>
</logic:notPresent>
</table>

<table align=center border=0>
<tr>
<html:form action="/raspisanie.do?action=new">
  <td align=center vAlign=middle height=35>
    <html:submit styleClass="button" value="Вернуться назад"/>
  </td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ Модификация/удаление одной записи в БД -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<body onLoad="exec3()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Расписание</template:put>
<template:put name="target_name">Расписание вступительных экзаменов</template:put>
<template:put name="sub_name">Модификация записи</template:put>
<template:put name='content'>
<p align=center><font class="text_11">ГРУППА:&nbsp;&nbsp;<bean:write name="abit_Rasp" property="gruppa"/></font></p>
<html:form action="/raspisanie?action=change" onsubmit="return checkFields();">
<html:hidden name="abit_Rasp" property="kodGruppy"/>
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">Дата консультации:</font></td>
      <td><html:text accesskey="д" name="abit_Rasp" styleClass="text_f10_short" property="dataKonsultatsii" 
                     size="10" maxlength="10" tabindex="1"/>

  <tr><td><font class="text_10">Аудитория консультации:</font></td>
      <td><html:text accesskey="а" name="abit_Rasp" styleClass="text_f10_short" property="auditorijaKonsultatsii" 
                     size="10" maxlength="10" tabindex="2"/>

  <tr><td><font class="text_10">Дата экзамена:</font></td>
      <td><html:text accesskey="д" name="abit_Rasp" styleClass="text_f10_short" property="dataJekzamena" 
                     size="10" maxlength="10" tabindex="3"/>

  <tr><td><font class="text_10">Аудитория экзамена:</font></td>
      <td><html:text accesskey="а" name="abit_Rasp" styleClass="text_f10_short" property="auditorijaJekzamena" 
                     size="10" maxlength="10" tabindex="4"/>

  <tr><td><font class="text_10">Предмет:</font></td>
      <td><html:select name="abit_Rasp" styleClass="select_f1" property="kodPredmeta" tabindex="5">
          <html:options collection="abit_Rasp_S2" property="kodPredmeta" labelProperty="sokr" />
          </html:select>

  <tr><td><font class="text_10">Номер потока:</font></td>
      <td><html:select onchange="chgPotok();" styleClass="select_f1" name="abit_Rasp" property="nomerPotoka" tabindex="6">
            <html:options collection="abit_Rasp_S4" property="nomerPotoka"/>
          </html:select>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_Rasp" property="kodRaspisanija"/>
<html:submit styleClass="button" tabindex="7" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="8" value="Удалить"/>
</td></html:form>
<html:form action="/raspisanie?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="9" value="Вернуться назад"/>
</td></html:form>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>