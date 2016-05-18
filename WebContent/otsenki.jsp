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

<logic:notPresent name='otsenkiAction' scope='request'>
 <logic:redirect forward='otsenki'/>
</logic:notPresent>

<logic:notPresent name="otsenkiForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! 
   int count;
   int tabindex;
   int row;
   String kP = "0";
%>

<SCRIPT LANGUAGE="JavaScript">
  var validx = "02345";
  var massLength;
  var massKodFak;
  var massGruppy;

function exec() {
    massLength = document.forms(0).kodGruppy.length-1;
    massKodFak = new Array(massLength);
    massGruppy = new Array(massLength);
  for(var currentIndex=1;currentIndex<document.forms(0).kodGruppy.length;currentIndex++){
   massKodFak[currentIndex-1] = document.forms(0).kodGruppy.options[currentIndex].value;
   massGruppy[currentIndex-1] = document.forms(0).kodGruppy.options[currentIndex].text;
  }
  fillSelect("-");
  gruppa.style.display = "block";
  document.forms(0).kodFakulteta.focus();
}

function fillSelect(selectCtrl) {
  var i,j,offset,next_offset;
  if(selectCtrl == "-"){
    // Удаление существующих строк
    for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
      document.forms(0).kodGruppy.options[i] = null; 
    }
    // Добавление знака "-"
    document.forms(0).kodGruppy.options[0] = new Option("-")
    document.forms(0).kodGruppy.options[0].value = "-"
    document.forms(0).kodGruppy.options[0].selected = true
    return;
  }
j=0;
  // Удаление существующих строк
  for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
    document.forms(0).kodGruppy.options[i] = null
  }
  // Добавление новых строк 
//  if(document.forms(0).kodGruppy)
//  else
  for(i = 0; i < massLength; i++){
    if(massKodFak[i] == selectCtrl){
     offset=0;
      while(1){
          next_offset = massGruppy[i].indexOf('%',offset+1);
           document.forms(0).kodGruppy.options[j] = new Option(massGruppy[i].substring(offset,next_offset));
          offset = massGruppy[i].indexOf('%',next_offset+1);
          if(offset == -1) {
           document.forms(0).kodGruppy.options[j].value = massGruppy[i].substring(next_offset+1);
           break;
          } else 
           document.forms(0).kodGruppy.options[j].value = massGruppy[i].substring(next_offset+1,offset);
          offset+=1;
          j++;
      }
    }
  }
  if(document.forms(0).kodGruppy.length==0){
  // Добавление знака "-"
    document.forms(0).kodGruppy.options[0] = new Option("-")
    document.forms(0).kodGruppy.options[0].value = "-"
    document.forms(0).kodGruppy.options[0].selected = true
  }
  // Переход в начало списка
  document.forms(0).kodGruppy.options[0].selected = true
}

function checkFields(){
 if(document.forms(0).kodFakulteta.value == "-"){
   alert("Необходимо выбрать факультет");
   document.forms(0).kodFakulteta.focus();
   return false;
 }

if(document.forms(0).kodPredmeta.value != -1)
 if(document.forms(0).dataJekzamena.value == "00-00-0000"){
   alert("Необходимо указать дату проведения экзамена по выбранному предмету");
   document.forms(0).dataJekzamena.focus();
   return false;
 }

}

function autoInit(){
}
</SCRIPT>

<logic:present name="otsenkiForm" property="action">
<bean:define id="action" name="otsenkiForm" property="action"/>

<%-----------------------------------------------------------------%>
<%-----------------------  Начальное Меню  ------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="menu">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="title">Экзаменационные оценки (внутренний экзамен ВУЗа)</template:put>
<template:put name="target_name">Экзаменационные оценки (результаты экзамена ВУЗа)</template:put>
<template:put name="sub_name">Для факультета или все полученные</template:put>
<template:put name="content">
<br>
<table border="0" cellspacing="0" cellpadding="0" align="center">
<html:form action="/otsenki?action=show" onsubmit="return checkFields();">
<%--------------------- Строка №1 таблицы -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;Факультет:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select onchange="fillSelect(this.value);" styleClass="select_f1" 
                               name="abit_A" property="kodFakulteta" tabindex="1">
    <html:option value="-"/>
    <html:options collection="abit_A_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta" />
    </html:select>
</td>
</tr>

<%--------------------- Строка №2 таблицы -------------------------%>
<tr>
<td align=right><font class="text_9">&nbsp;Группа:&nbsp;&nbsp;</font></td>
<td align=center id="gruppa" style="display:none" height=27>
    <html:select styleClass="select_f1" name="abit_A" property="kodGruppy" tabindex="2">
    <html:option value="-"/>
    <html:options collection="abit_A_S2" property="kodGruppy" labelProperty="special2" />
    </html:select>
</td>
</tr>
<%--------------------- Строка №3 таблицы -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;Оценки:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select styleClass="select_f1" name="abit_A" property="special8" tabindex="3">
    <html:option value="needed">на фак.</html:option>
    <html:option value="all">все</html:option>
    </html:select>
</td>
</tr>
<%--------------------- Строка №4 таблицы -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;Предмет:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select styleClass="select_f1" name="abit_A" property="kodPredmeta" tabindex="4">
    <html:option value="-1">&nbsp;&nbsp;&nbsp;*</html:option>
    <html:options collection="predmets" property="kodPredmeta" labelProperty="predmet" />
    </html:select>
</td>
</tr>
<%--------------------- Строка №5 таблицы -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;Дата экзамена:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:text accesskey="д" styleClass="text_f11_short" name="abit_A" size='10' maxlength='10' property="dataJekzamena" value="00-00-0000" tabindex="5"/>
</td>
</tr>
<%--------------------- Строка №6 таблицы -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;Документы хранятся:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select styleClass="select_f1" name="abit_A" property="dokumentyHranjatsja" tabindex="6">
    <html:option value="д"/>
    <html:option value="н"/>
    <html:option value="*"/>
    </html:select>
</td>
</tr>
<%-----------------------------------------------------------------%>
</table>
<br>
<table border="0" align="center">
<tr align="center">
  <td><html:submit styleClass="button" value="Корректировка" tabindex="7"/></td>
</html:form>
<html:form action="/abiturient.do">
  <td><html:submit styleClass="button" value="Выход" tabindex="8" property="exit"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%---------- Корректировка по всем полученным оценкам -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="title">Корректировка экзаменационных оценок</template:put>
<template:put name="target_name">Корректировка экзаменационных оценок</template:put>
<template:put name="sub_name">Для выбранного факультета</template:put>
<template:put name="content">
<div align=center><font class="text_10">Абитуриентов&nbsp;в&nbsp;группе:&nbsp;
  <bean:write name="abit_A" property="special3"/>&nbsp;&nbsp;&nbsp; На&nbsp;факультете:&nbsp;
  <bean:write name="abit_A" property="special1"/></font>
</div>
<html:form action="/otsenki?action=save">
<html:hidden name="abit_A" property="kodGruppy"/>
<html:hidden name="abit_A" property="kodFakulteta"/>
<html:hidden name="abit_A" property="special8"/>
<html:hidden name="abit_A" property="dokumentyHranjatsja"/>
<html:hidden name="abit_A" property="kodPredmeta"/>
<html:hidden name="abit_A" property="dataJekzamena"/>
<table border="1" cellspacing="0" cellpadding="0" align="center">
<thead>
<tr align="center">
  <td rowspan=2 valign=center><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Док. хр.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Спец.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Номер дела&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ФИО&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Отличие&nbsp;</font></td>
  <td colspan=20 align=center valign=center><font class="text_th">&nbsp;Предметы&nbsp;</font></td>
</tr>
<tr>
<logic:iterate id="predm" name="predmets" scope="request">
  <td><font class="text_th">&nbsp;<bean:write name="predm" property="sokr"/>&nbsp;</font></td>
</logic:iterate>
</tr>
</thead>
<%
   int number = 0;
   tabindex = 1;
   int old_ka = -1;
%>

<logic:iterate id="abit_A" name="abits_A" type="abit.bean.AbiturientBean" scope="request">
<logic:present name="abit_A" property="familija"> 
  <% if(old_ka != StringUtil.toInt(""+abit_A.getKodAbiturienta(),0)) { %>
  <% old_ka = StringUtil.toInt(""+abit_A.getKodAbiturienta(),0); %>
   <tr>
      <td align="center">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><%= ++number %></html:link>&nbsp;</td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="dokumentyHranjatsja"/>&nbsp;</font></td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="shifrSpetsialnosti"/>&nbsp;</font></td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="nomerLichnogoDela"/>&nbsp;</font></td>
      <td><font class="text_10">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="familija"/>&nbsp;</font></html:link></td>
      <td align=center><font class="text_10">&nbsp;<bean:write name="abit_A" property="shifrMedali"/>&nbsp;</font></td>
  <% } %>
      <td align="center"><html:text accesskey='о' name="abit_A" styleClass="text_f9_short" property="<%=\"otsn\"+abit_A.getKodPredmeta()+\"%\"+abit_A.getKodAbiturienta()%>" value="<%=abit_A.getSpecial1()%>" maxlength="3" size="2" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</logic:present>
</logic:iterate>
<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="20" align="center">
     <font class="text_11">&nbsp;По&nbsp;указанным&nbsp;параметрам&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи&nbsp;</font>
  </td>
</tr>
</table>
</logic:notPresent>
<table border="0" align="center" valign="center">
<tr align="center">
  <td height=30><html:submit styleClass="button" value="Сохранить" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/otsenki.do">
  <td>&nbsp;<html:submit styleClass="button" value="Назад к меню" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%----------- Корректировка по оценкам факультета -----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full3">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="title"> Корректировка экзаменационных оценок</template:put>
<template:put name="target_name">Корректировка экзаменационных оценок</template:put>
<template:put name="sub_name">Для выбранного факультета</template:put>
<template:put name="content">
<div align=center><font class="text_10">Абитуриентов&nbsp;в&nbsp;группе:&nbsp;
  <bean:write name="abit_A" property="special3"/>&nbsp;&nbsp;&nbsp; На&nbsp;факультете:&nbsp;
  <bean:write name="abit_A" property="special1"/></font>
</div>
<html:form action="/otsenki?action=save">
<html:hidden name="abit_A" property="kodGruppy"/>
<html:hidden name="abit_A" property="kodFakulteta"/>
<html:hidden name="abit_A" property="special8"/>
<html:hidden name="abit_A" property="dokumentyHranjatsja"/>
<html:hidden name="abit_A" property="kodPredmeta"/>
<html:hidden name="abit_A" property="dataJekzamena"/>
<table border="1" cellspacing="0" cellpadding="0" align="center">
<thead>
<tr align="center">
  <td rowspan=2 valign=center><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Док. хр.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Спец.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Номер дела&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ФИО&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Отличие&nbsp;</font></td>
  <td colspan=20 align=center valign=center><font class="text_th">&nbsp;Предметы&nbsp;</font></td>
</tr>
<tr>
<logic:iterate id="predm" name="predmets" scope="request">
  <td><font class="text_th">&nbsp;<bean:write name="predm" property="sokr"/>&nbsp;</font></td>
</logic:iterate>
</tr>
</thead>
<%
   int number = 0;
   tabindex = 1;
   int old_ka = -1;
%>

<logic:iterate id="abit_A" name="abits_A" type="abit.bean.AbiturientBean" scope="request">
<logic:present name="abit_A" property="familija"> 
  <% if(old_ka != StringUtil.toInt(""+abit_A.getKodAbiturienta(),0)) { %>
  <% old_ka = StringUtil.toInt(""+abit_A.getKodAbiturienta(),0); %>
   <tr>
      <td align="center">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><%= ++number %></html:link>&nbsp;</td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="dokumentyHranjatsja"/>&nbsp;</font></td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="shifrSpetsialnosti"/>&nbsp;</font></td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="nomerLichnogoDela"/>&nbsp;</font></td>
      <td><font class="text_10">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="familija"/>&nbsp;</font></html:link></td>
      <td align=center><font class="text_10">&nbsp;<bean:write name="abit_A" property="shifrMedali"/>&nbsp;</font></td>
  <% } %>
      <td align="center"><html:text accesskey='о' name="abit_A" styleClass="text_f9_short" property="<%=\"otsn\"+abit_A.getKodPredmeta()+\"%\"+abit_A.getKodAbiturienta()%>" value="<%=abit_A.getSpecial1()%>" maxlength="3" size="2" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</logic:present>
</logic:iterate>
<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="20" align="center">
     <font class="text_11">&nbsp;По&nbsp;указанным&nbsp;параметрам&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи&nbsp;</font>
  </td>
</tr>
</table>
</logic:notPresent>
<table border="0" align="center" valign="center">
<tr align="center">
  <td height=30><html:submit styleClass="button" value="Сохранить" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/otsenki.do">
  <td>&nbsp;<html:submit styleClass="button" value="Назад к меню" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%----------- Корректировка по выбранному предмету ----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full1">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="title">Корректировка экзаменационных оценок</template:put>
<template:put name="target_name">Корректировка экзаменационных оценок</template:put>
<template:put name="sub_name">Для выбранного факультета</template:put>
<template:put name="content">
<div align=center><font class="text_10">Абитуриентов&nbsp;в&nbsp;группе:&nbsp;
  <bean:write name="abit_A" property="special3"/>&nbsp;&nbsp;&nbsp; На&nbsp;факультете:&nbsp;
  <bean:write name="abit_A" property="special1"/></font>
</div>
<html:form action="/otsenki?action=save">
<html:hidden name="abit_A" property="kodGruppy"/>
<html:hidden name="abit_A" property="kodFakulteta"/>
<html:hidden name="abit_A" property="special8"/>
<html:hidden name="abit_A" property="dokumentyHranjatsja"/>
<html:hidden name="abit_A" property="kodPredmeta"/>
<html:hidden name="abit_A" property="dataJekzamena"/>
<table border="1" cellspacing="0" cellpadding="0" align="center">
<thead>
<tr align="center">
  <td rowspan=2 valign=center><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Док. хр.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Спец.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Номер дела&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ФИО&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;Отличие&nbsp;</font></td>
  <td align=center valign=center><font class="text_th">&nbsp;Предмет&nbsp;</font></td>
</tr>
<tr>
  <td align=center><font class="text_th">&nbsp;<bean:write name="abit_A" property="predmet"/>&nbsp;</font></td>
</tr>
</thead>
<%
   int number = 0;
   tabindex = 1;
   int old_ka = -1;
%>

<logic:iterate id="abit_A" name="abits_A" type="abit.bean.AbiturientBean" scope="request">
<logic:present name="abit_A" property="familija"> 
  <% if(old_ka != StringUtil.toInt(""+abit_A.getKodAbiturienta(),0)) { %>
  <% old_ka = StringUtil.toInt(""+abit_A.getKodAbiturienta(),0); %>
   <tr>
      <td align="center">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><%= ++number %></html:link>&nbsp;</td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="dokumentyHranjatsja"/>&nbsp;</font></td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="shifrSpetsialnosti"/>&nbsp;</font></td>
      <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="nomerLichnogoDela"/>&nbsp;</font></td>
      <td><font class="text_10">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="familija"/>&nbsp;</font></html:link></td>
      <td align=center><font class="text_10">&nbsp;<bean:write name="abit_A" property="shifrMedali"/>&nbsp;</font></td>
  <% } %>
      <td align="center"><html:text accesskey='о' name="abit_A" styleClass="text_f9_short" property="<%=\"otsn\"+abit_A.getKodPredmeta()+\"%\"+abit_A.getKodAbiturienta()%>" value="<%=abit_A.getSpecial1()%>" maxlength="3" size="2" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</logic:present>
</logic:iterate>
<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="20" align="center">
     <font class="text_11">&nbsp;По&nbsp;указанным&nbsp;параметрам&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи&nbsp;</font>
  </td>
</tr>
</table>
</logic:notPresent>
<table border="0" align="center" valign="center">
<tr align="center">
  <td height=30><html:submit styleClass="button" value="Сохранить" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/otsenki.do">
  <td>&nbsp;<html:submit styleClass="button" value="Назад к меню" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>