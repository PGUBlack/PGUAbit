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

<logic:notPresent name='editRekAction' scope='request'>
 <logic:redirect forward='edit_rek'/>
</logic:notPresent>

<logic:notPresent name="zchForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! 
   int tabindex;
%>

<SCRIPT LANGUAGE="JavaScript">

var teamLength;
var valid0 = "рвон";

function checkFields() {
  if(document.forms(0).kodFakulteta.value == "-") {
    alert("Необходимо выбрать Факультет");
    document.forms(0).kodFakulteta.focus();
    return false;
  }
}

function checkFields2(){
  if(document.forms(0).elements[4].value == null) {
   alert("Невозможно сохранить. Нет данных!");
   return false;
  }
  var tmp="";
  var nn="";
  var all=0;
  var j=0;
  for(i=0;i<document.forms(0).special22.value;i++){
    tmp=document.forms(0).elements[4+i].value;
    nn=document.forms(0).elements[4+i].name;
    if((valid0.indexOf(tmp) == "-1")&&(nn.indexOf("cls") != "-1")){
        alert("Поле ''Рекомендован'' может состоять только из букв ''р'',''в'',''о'' или ''н''");
        document.forms(0).elements[4+i].focus();
        return false;
    }
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
j=0
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


<logic:present name="zchForm" property="action">
<bean:define id="action" name="zchForm" property="action"/>

<%-----------------------------------------------------------------%>
<%-----------------------    Меню отбора   ------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="menu">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="title">Модификация рекомендованных к зачислению</template:put>
<template:put name="target_name">Модификация рекомендованных к зачислению</template:put>
<template:put name="sub_name">По заданной специальности</template:put>
<template:put name="content">
<br>
<html:form action="/edit_rek?action=show&offset=new" onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
  <tr>
    <td><font class="text_10">Факультет:</font></td>
    <td>
      <html:select onchange="fillSelect(this.value)" styleClass="select_f2" name="abit_A" property="kodFakulteta" tabindex="1">
       <html:option value="-"/>
       <html:options collection="abit_A_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select>
    </td>  
  </tr>
  <tr>
    <td><font class="text_10">Специальность:</font></td>
    <td align=left>
      <html:select styleClass="select_f2" name="abit_A" property="special1" tabindex="2">
       <html:options collection="abit_A_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
    </td>
  </tr>
  <tr valign="middle">
     <td><font class="text_10">&nbsp;Документы&nbsp;хранятся:&nbsp;&nbsp;</font></td>
     <td>
       <html:select styleClass="select_f1" name="abit_A" property="dokumentyHranjatsja" tabindex="3">
         <html:option value="д"/>
         <html:option value="н"/>
         <html:option value="*"/>
       </html:select>
     </td>
  </tr>
  <tr><td><font class="text_10">Доп.&nbsp;сведения:&nbsp;&nbsp;</font></td>
    <td><html:password name="abit_A" styleClass="text_f9_short" property="special6" size="16" maxlength="25" tabindex="4"/> 
    </td>
  </tr>
</table>
<%-----------------------------------------------------------------%>
<br>
<table border="0" align="center">
<tr align="center">
  <td><html:submit styleClass="button" value="Перейти к модификации" tabindex="5"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/abiturient.do">
  <td><html:submit styleClass="button" value="Выход" tabindex="6" property="exit"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%----------------    Зачисление абитуриентов     -----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">Модификация рекомендованных к зачислению</template:put>
<template:put name="target_name">Модификация рекомендованных к зачислению</template:put>
<template:put name="content">
<br>
<div align=center><font class="text_10">
  Всего выбрано:&nbsp;&nbsp;<bean:write name="abit_A" property="special22"/>&nbsp;&nbsp;абитуриента(ов)&nbsp;&nbsp;&nbsp;из&nbsp;них&nbsp;рекомендовано:&nbsp;<bean:write name="abit_A" property="special11"/></font>
</div>
<html:form action="/edit_rek?action=updatebase" onsubmit="return checkFields2();">
<html:hidden name="abit_A" property="special22"/>
<html:hidden name="abit_A" property="special11"/>
<html:hidden name="abit_A" property="special6"/>
<table border="1" cellspacing="0" cellpadding="0" align="center">
<tr style="display:none">
<td align=center>
    <html:select name="abit_A" property="special3">
    <html:options collection="abit_A_S5" property="special3" labelProperty="abbreviatura"/>
    </html:select>
</td>
</tr>
<thead>
<tr align="center">
  <td><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Спец.&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Номер дела&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Шифр&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Имя&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Аттестат&nbsp;</font></td>
  <td><font class="text_th">&nbsp;Рек.&nbsp;</font></td></tr>
</thead>
<%
   tabindex = 1;
%>
<tr>
<logic:iterate id="abit_A" name="abits_A" type="abit.bean.AbiturientBean" scope="request">
<logic:present name="abit_A" property="familija">

  <tr>
    <td align="center">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="number"/></html:link>&nbsp;</td>
    <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="special1"/>&nbsp;</font></td>
    <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="nomerLichnogoDela"/>&nbsp;</font></td>
    <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="special4"/>&nbsp;</font></td>
    <td><font class="text_10">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="familija"/>&nbsp;</font></html:link></td>
    <td><font class="text_10">&nbsp;<bean:write name="abit_A" property="imja"/>&nbsp;</font></td>
    <td><font class="text_10">&nbsp;<bean:write name="abit_A" property="otchestvo"/>&nbsp;</font></td>
    <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="tipDokSredObraz"/>&nbsp;</font></td>
    <td align="center"><input type="text" class="text_f9_short" name="<%="cls"+abit_A.getKodKonkursa()%>" value="<%=abit_A.getSpecial5()%>" maxlength="1" size="1" tabindex="<%=Integer.toString(tabindex++)%>"></td>
  </tr>
</logic:present>
</logic:iterate>

<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="29" align="center"><font class="text_11">
     Не&nbsp;найдено&nbsp;ни&nbsp;одного&nbsp;абитуриента&nbsp;</font>
  </td>
</tr>
</logic:notPresent>
</table>
<br>
<table border="0" align="center">
<tr align="center">
  <td>&nbsp;<html:submit styleClass="button" value="Сохранить" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/edit_rek.do">
  <td>&nbsp;&nbsp;<html:submit styleClass="button" value="Назад к настройке" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>

