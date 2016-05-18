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

<logic:notPresent name='gruppyAction' scope='request'>
 <logic:redirect forward='gruppy'/>
</logic:notPresent>

<logic:notPresent name="gruppyForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var execute = "true";
var oldName;
var valid1 = " абвгдеёжзийклмнопрстуфхцчшщьъэюя_-1234567890"
var del = false;
var teamLength;
var konGrLen;
var teamTXT;
var kongTXT;
var kongVAL;

function checkFields(){
var temp;
if (del) return true;
if(!execute) return true;

if(document.forms(0).kodKonGrp.options[document.forms(0).kodKonGrp.selectedIndex].text == "-")
 {
  alert("Необходимо указать ''Конкурсную группу''");
  document.forms(0).kodKonGrp.focus();
  return false;
 }

for (var i=0; i<document.forms(0).gruppa.value.length; i++) 
 {
  temp = "" + document.forms(0).gruppa.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
   alert("Название группы может состоять только \nиз строчных букв русского алфавита и цифр");
   document.forms(0).gruppa.focus();
   return false;
  }
 }
if(document.forms(0).gruppa.value.length == 0) 
 {
  alert("Необходимо заполнить поле ''Название группы''");
  document.forms(0).gruppa.focus();
  return false;
 }
 else if (document.forms(0).shifrFakulteta.value==" "){
                      alert("Не выбран факультет");
                      document.forms(0).shifrFakulteta.focus();
                      return false;
                     }
 else if (document.forms(0).shifrFakulteta.value!=document.forms(0).gruppa.value.substring(0,1)){
                      alert("Первый символ не является шифром выбранного факультета");
                      document.forms(0).gruppa.focus();
                      return false;
                     }
 else if (document.forms(0).nomerPotoka.value=="-1"){
                      alert("Выберите поток");
                      document.forms(0).nomerPotoka.focus();
                      return false;
                     }

 return true;
}

function autoInit(){
  document.forms(0).gruppa.value = document.forms(0).shifrFakulteta.value;
}

function autoInit2(){
if(document.forms(0).kodKonGrp.options[document.forms(0).kodKonGrp.selectedIndex].text != "-")
  document.forms(0).gruppa.value = document.forms(0).shifrFakulteta.value + kongVAL[document.forms(0).kodKonGrp.selectedIndex-1];
else 
  document.forms(0).gruppa.value = document.forms(0).shifrFakulteta.value;
}

function gap_me(){
  execute = false;
}

function invokeAct(){
  document.forms(0).submit();
}

function confirmation(){
  if(confirm('Удалить Группу?')) {
    del = true;
    return true;
  }
  else 
    return false; 
}

function exec() {
   teamLength = document.forms(0).shifrFakulteta.options.length
   teamTXT = new Array(teamLength)
   for(var i=0;i<teamLength-1;i++) {
   teamTXT[i] = document.forms(0).shifrFakulteta.options[i+1].value
   }
   fillKonGrp(); 
   document.forms(0).shifrFakulteta.selectedIndex=0;
   document.forms(0).dogovornaja.selectedIndex=0;
   document.forms(0).kodKonGrp.selectedIndex=0;
   document.forms(0).nomerPotoka.selectedIndex=0;
   document.forms(0).shifrFakulteta.focus();
   
}

function fillKonGrp() {
   konGrLen = document.forms(0).kodKonGrp.options.length
   kongTXT = new Array(konGrLen)
   kongVAL = new Array(konGrLen)
   for(var i=0;i<konGrLen-1;i++) {
     kongTXT[i] = document.forms(0).kodKonGrp.options[i+1].text.substring(0,document.forms(0).kodKonGrp.options[i+1].text.indexOf("%"))
     kongVAL[i] = document.forms(0).kodKonGrp.options[i+1].text.substring(document.forms(0).kodKonGrp.options[i+1].text.indexOf("%")+1)
     document.forms(0).kodKonGrp.options[i+1].text = kongTXT[i];
   }
}

function saveLetter() {
  var index = document.forms(0).kodKonGrp.selectedIndex;
  oldName = document.forms(0).gruppa.value.charAt(0);
  fillKonGrp();
  document.forms(0).kodKonGrp.selectedIndex = index;
}

function checkFields2() {
if (del) return true;
if(document.forms(0).gruppa.value.charAt(0) != oldName) {
   alert("Нельзя изменить первую букву названия группы");
   document.forms(0).gruppa.focus();
   return false;
}
for (var i=0; i<document.forms(0).gruppa.value.length; i++) 
 {
  temp = "" + document.forms(0).gruppa.value.substring(i, i+1);
  if(valid1.indexOf(temp) == "-1")  {
    alert("Название группы может состоять только \nиз строчных букв русского алфавита и цифр");
    document.forms(0).gruppa.focus();
    return false;
  }
 }
return true;
}
</SCRIPT>

<logic:present name="gruppyForm" property="action">
<bean:define id="action" name="gruppyForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<body onLoad="exec(),autoInit();"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Группы</template:put>
<template:put name="target_name">Группы абитуриентов</template:put>
<template:put name="sub_name">Добавление записи</template:put>
<template:put name='content'>
<logic:present name="message" property="message">
<p align=center><font class="message"><bean:write name="message" property="message"/></font></p>
</logic:present>
<logic:notPresent name="message" property="message">
<br>
</logic:notPresent>
<html:form action="/gruppy?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:select styleClass="select_f1" onchange="autoInit();" name="abit_Gr" property="shifrFakulteta" tabindex="1">
          <html:option value=" ">-</html:option>
          <html:options collection="abit_G_S2" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 

  <tr><td><font class="text_10">Конкурсная гр.:</font></td>
      <td><html:select styleClass="select_f1" onchange="autoInit2();" name="abit_Gr" property="kodKonGrp" tabindex="2">
          <html:option value=" ">-</html:option>
          <html:options collection="abit_G_S3" property="kodKonGrp" labelProperty="abbr"/>
      </html:select> 

  <tr><td><font class="text_10">Название группы:</font></td>
      <td><html:text accesskey="н" name="abit_Gr" styleClass="text_f10" property="gruppa"
                     maxlength="10" size="10" tabindex="3" value=""/>

  <tr><td><font class="text_10">Тип группы:</font></td>
      <td><html:select name="abit_Gr" styleClass="select_f1" property="dogovornaja" tabindex="4">
           <html:option value="б">дневная бюдж.</html:option>
           <html:option value="д">дневная контр.</html:option>
           <html:option value="зб">заочная бюдж.</html:option>
           <html:option value="зд">заочная контр.</html:option>
          </html:select>

  <tr><td><font class="text_10">Поток:</font></td>
      <td><html:select name="abit_Gr" styleClass="select_f1" property="nomerPotoka" tabindex="5">
           <html:option value="-1">-</html:option>
           <html:option value="1">1</html:option>
           <html:option value="2">2</html:option>
           <html:option value="3">3</html:option>
           <html:option value="4">4</html:option>
           <html:option value="5">5</html:option>
           <html:option value="6">6</html:option>
           <html:option value="7">7</html:option>
           <html:option value="8">8</html:option>
           <html:option value="9">9</html:option>
           <html:option value="10">10</html:option>
           <html:option value="11">11</html:option>
           <html:option value="12">12</html:option>
          </html:select>

<tr><td height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="6" value="Добавить"/></td>
  <td><html:reset  styleClass="button" tabindex="7" value="Отменить"/></td>
  <td><html:submit onclick="gap_me();" property="full" styleClass="button" tabindex="8" value="Просмотр таблицы"/></td>
</html:form>
<html:form action="/raspisanie?action=new">
  <td><html:submit styleClass="button" tabindex="9" value="Расписание"/></td>
</html:form>
<html:form action="/gruppy?action=null">
  <td><html:submit styleClass="button" tabindex="10" onclick="gap_me();" property="exit" value="Выход"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Группы</template:put>
<template:put name="target_name">Группы абитуриентов по факультетам</template:put>
<template:put name='content'>
<br>
<html:form action="gruppy.do?action=create&full">
<p align=center><font class="text_11">Выберите факультет:</font><br>
<logic:iterate id="abit_Gr" name="abit_G_S2" scope='request'>
<html:link href="gruppy.do?action=full" paramId="kFak" paramName="abit_Gr" paramProperty="kodFakulteta" styleClass="link_hov_blue">
        <bean:write name="abit_Gr" property="abbreviaturaFakulteta"/>
</html:link>
</logic:iterate>
</p>
</html:form>
<font class="text_th">Д Н Е В Н О Е&nbsp;&nbsp;&nbsp;И&nbsp;&nbsp;&nbsp;В Е Ч Е Р Н Е Е</font>
<br>
<table align=center border=1 cellSpacing=0>
<tr><td valign=top>
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Бюджетные&nbsp;группы&nbsp;</font></td></tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info1" name="groups_bud" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info1" property="nomerPotoka"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="gruppy.do?action=mod_del" paramName="group_info1" paramId="kodGruppy" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info1" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info1" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info1" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td><td width=1>
<td valign=top><table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Контрактные&nbsp;группы&nbsp;</font></td></tr>
     <td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info2" name="groups_kon" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info2" property="nomerPotoka"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="gruppy.do?action=mod_del" paramName="group_info2" paramId="kodGruppy" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info2" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info2" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info2" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td></tr>
</table>
<br><br>
<font class="text_th">З А О Ч Н О Е</font>
<br>
<table align=center border=1 cellSpacing=0>
<tr><td valign=top>
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Бюджетные&nbsp;группы&nbsp;</font></td></tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info11" name="groups_z_bud" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info11" property="nomerPotoka"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="gruppy.do?action=mod_del" paramName="group_info11" paramId="kodGruppy" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info11" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info11" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info11" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td><td width=1>
<td valign=top><table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Контрактные&nbsp;группы&nbsp;</font></td></tr>
     <td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info22" name="groups_z_kon" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info22" property="nomerPotoka"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="gruppy.do?action=mod_del" paramName="group_info22" paramId="kodGruppy" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info22" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info22" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info22" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td></tr>
</table>
<table align=center border=0>
<tr>
<html:form action="/gruppy.do?action=new">
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
<body onLoad="saveLetter();"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Группы</template:put>
<template:put name="target_name">Группы абитуриентов</template:put>
<template:put name="sub_name">Модификация записи</template:put>
<template:put name='content'>
<br>
<html:form action="/gruppy?action=change" onsubmit="return checkFields2();">
<table align=center border=0>

  <tr><td><font class="text_10">Факультет:</font></td>
      <td><html:select styleClass="select_f1" onchange="autoInit();" name="abit_Gr" property="shifrFakulteta" tabindex="1">
          <html:options collection="abit_G_S2" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 

  <tr><td><font class="text_10">Конкурсная гр.:</font></td>
      <td><html:select styleClass="select_f1" onchange="autoInit2();" name="abit_Gr" property="kodKonGrp" tabindex="2">
          <html:option value=" ">-</html:option>
          <html:options collection="abit_G_S3" property="kodKonGrp" labelProperty="abbr"/>
      </html:select> 

  <tr><td><font class="text_10">Название группы:</font></td>
      <td><html:text accesskey="н" name="abit_Gr" styleClass="text_f10" property="gruppa" 
                     size="10" maxlength="10" tabindex="3"/>

  <tr><td><font class="text_10">Тип группы:</font></td>
      <td><html:select name="abit_Gr" styleClass="select_f1" property="dogovornaja" tabindex="4">
           <html:option value="б">дневная бюдж.</html:option>
           <html:option value="д">дневная контр.</html:option>
           <html:option value="зб">заочная бюдж.</html:option>
           <html:option value="зд">заочная контр.</html:option>
          </html:select>

  <tr><td><font class="text_10">Поток:</font></td>
      <td><html:select name="abit_Gr" styleClass="select_f1" property="nomerPotoka" tabindex="5">
           <html:option value="1">1</html:option>
           <html:option value="2">2</html:option>
           <html:option value="3">3</html:option>
           <html:option value="4">4</html:option>
           <html:option value="5">5</html:option>
           <html:option value="6">6</html:option>
           <html:option value="7">7</html:option>
           <html:option value="8">8</html:option>
           <html:option value="9">9</html:option>
           <html:option value="10">10</html:option>
           <html:option value="11">11</html:option>
           <html:option value="12">12</html:option>
          </html:select>

<tr><td height=6></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_Gr" property="kodGruppy"/>
<html:submit onclick="gap_me();" styleClass="button" tabindex="6" value="Изменить"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="7" value="Удалить"/>
</td></html:form>
<html:form action="/gruppy?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="8" value="Вернуться назад"/>
</td></html:form>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>