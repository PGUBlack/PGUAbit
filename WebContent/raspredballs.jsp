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

<logic:notPresent name='raspredBallsAction' scope='request'>
 <logic:redirect forward='raspredballs'/>
</logic:notPresent>

<logic:notPresent name="raspredBallsForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! 
   int tabindex;
   int row;
%>

<SCRIPT LANGUAGE="JavaScript">
  var valid0 = "нд ";
  var massLength;
  var massKodFak;
  var massGruppy;
  var massLength2;
  var massSpecs;
  var no_flush_kg = false;

function refill(){
//  document.forms(0).special5.value = val;
//  document.forms(0).submit();
}

function exec() {
  massLength = document.forms(0).kodGruppy.length-1;
  massLength2 = document.forms(0).kodFakulteta.length-1;
  massGruppy = new Array(massLength);
  massKodFak = new Array(massLength2);
  massSpecs = new Array(massLength2);
  for(var currentIndex=0;currentIndex<document.forms(0).kodGruppy.length;currentIndex++){
   massGruppy[currentIndex] = document.forms(0).kodGruppy.options[currentIndex].text;
  }
  for(var currentIndex=0;currentIndex<document.forms(0).kodFakulteta.length;currentIndex++){
   massKodFak[currentIndex] = document.forms(0).kodFakulteta.options[currentIndex].value;
   massSpecs[currentIndex] = document.forms(0).kodFakulteta.options[currentIndex].text;
  }
  document.forms(0).special2.options[0].selected = true;
  fillSelectSp("-1");
  fillSelectGr("-1");
  kng.style.display    = "block";
  gruppa.style.display = "block";
  fakult.style.display = "block";
  spec.style.display   = "block";
}

function refillGruppy(spec) {
  flushKonGrp();
  if(spec != -1) {
    // Удаление существующих строк
    for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
      document.forms(0).kodGruppy.options[i] = null; 
    }
    // Добавление знака "-"
    document.forms(0).kodGruppy.options[0] = new Option("-")
    document.forms(0).kodGruppy.options[0].value = "-"
    document.forms(0).kodGruppy.options[0].selected = true
  }
  else {
   fillSelectGr(document.forms(0).special2.options[document.forms(0).special2.selectedIndex].value);
  }
}

function flushKonGrp(){
  if(!no_flush_kg) document.forms(0).kodKonGrp.options[0].selected = true;
}

function flushOthers(){
  no_flush_kg = true;
  document.forms(0).special2.options[0].selected = true;
  fillSelectSp("-1");
  fillSelectGr("-1");
  no_flush_kg = false;  
//  document.forms(0).kodFakulteta.options[0].selected = true;
//  document.forms(0).kodGruppy.options[0].selected = true;
}

function fillSelectSp(selectCtrl) {
  var i,j,offset,next_offset;
  flushKonGrp();
  // Удаление существующих строк
  for (i = document.forms(0).kodFakulteta.length; i >= 0; i--){
    document.forms(0).kodFakulteta.options[i] = null; 
  }
  if(selectCtrl == "-1"){
    // Добавление знака "-"
    document.forms(0).kodFakulteta.options[0] = new Option("-")
    document.forms(0).kodFakulteta.options[0].value = "-"
    document.forms(0).kodFakulteta.options[0].selected = true
    fillSelectGr(selectCtrl);
    return;
  }
  // Добавление символа '*'
    document.forms(0).kodFakulteta.options[0] = new Option("*")
    document.forms(0).kodFakulteta.options[0].value = "-1"
j=1;
  // Добавление новых строк 
  for(i = 0; i <= massLength2; i++){
    if(massKodFak[i] == selectCtrl){
     offset=0;
      while(1){
          next_offset = massSpecs[i].indexOf('%',offset+1);
           document.forms(0).kodFakulteta.options[j] = new Option(massSpecs[i].substring(offset,next_offset));
          offset = massSpecs[i].indexOf('%',next_offset+1);
          if(offset == -1) {
           document.forms(0).kodFakulteta.options[j].value = massSpecs[i].substring(next_offset+1);
           break;
          } else 
           document.forms(0).kodFakulteta.options[j].value = massSpecs[i].substring(next_offset+1,offset);
          offset+=1;
          j++;
      }
     break;
    }
  }
  fillSelectGr(selectCtrl);
  // Переход в начало списка
  document.forms(0).kodFakulteta.options[0].selected = true
}

function fillSelectGr(selectCtrl) {
  var i,j,offset,next_offset;
  flushKonGrp();
  if(selectCtrl == "-1"){
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
j=1;
  // Удаление существующих строк
  for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
    document.forms(0).kodGruppy.options[i] = null
  }
// Добавление символа '*'
 document.forms(0).kodGruppy.options[0] = new Option("*")
 document.forms(0).kodGruppy.options[0].value = "-1"

  // Добавление новых строк 
  for(i = 0; i <= massLength; i++){
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
     break;
    }
  }
  // Переход в начало списка
  document.forms(0).kodGruppy.options[0].selected = true
}

function checkfields(){
 if(document.forms(0).special2.options[document.forms(0).special2.selectedIndex].value == -1 &&
    document.forms(0).kodFakulteta.options[document.forms(0).kodFakulteta.selectedIndex].value == "-" &&
    document.forms(0).kodGruppy.options[document.forms(0).kodGruppy.selectedIndex].value == "-" &&
    document.forms(0).kodKonGrp.options[document.forms(0).kodKonGrp.selectedIndex].value == "-1"){

    alert("Необходимо выбрать ''Конкурсную группу'' или ''Факультет'' или ''Специальность'' или ''Группу''");
    document.forms(0).kodKonGrp.focus();
    return false;
 }
  return true;
}
</SCRIPT>


<logic:present name="raspredBallsForm" property="action">
<bean:define id="action" name="raspredBallsForm" property="action"/>
<bean:define id="cellsx" name="cells" scope="request" type="java.util.ArrayList"/>
<bean:define id="ballsx" name="balls" scope="request" type="java.util.ArrayList"/>
<bean:define id="kollsx" name="kolls" scope="request" type="java.util.ArrayList"/>

<logic:equal name="action" value="histogramm">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="title">Распределение суммарных баллов</template:put>
<template:put name="target_name">Распределение суммарных баллов</template:put>
<template:put name="sub_name">&nbsp;<bean:write name="abit_A" property="nazvanie"/>&nbsp;</template:put>
<template:put name="content">
<table border="0" cellspacing="0" cellpadding="0" align="center" frame="box" height="100%">
<tr valign="top" height="100%">
<td width="20%" height="100%" align="center">
<br><br><br>
<table border="1" cellspacing="0" cellpadding="0" align="center" frame="box">
<html:form action="/raspredballs?action=draw" onsubmit="return checkfields();">
<tr valign="middle">
<td><font class="text_10">&nbsp;Кон.&nbsp;группа:&nbsp;</font></td>
<td height=25 align=right id="kng" style="display:none"> 
    <html:select styleClass="select_f1" onchange="flushOthers();" name="abit_A" property="kodKonGrp" tabindex="1">
    <html:option value="-1">-</html:option>
    <html:options collection="abit_A_S4" property="kodKonGrp" labelProperty="nazvanie"/>
    </html:select>
</td>
</tr>
<tr valign="middle">
<td><font class="text_10">&nbsp;Факультет:</font></td>
<td height=25 align=right id="fakult" style="display:none"> 
    <html:select onchange="fillSelectSp(this.value)" styleClass="select_f1" 
                               name="abit_A" property="special2" tabindex="2">
    <html:option value="-1">-</html:option>
    <html:options collection="abit_A_S2" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta" />
    </html:select>
</td>
</tr>
<tr valign="middle">
<td><font class="text_10">&nbsp;Спец-ть:</font></td>
<td height=25 align=right id="spec" style="display:none">
    <html:select onchange="refillGruppy(this.value);" styleClass="select_f1" name="abit_A" property="kodFakulteta" tabindex="3">
    <html:options collection="abit_A_S3" property="kodFakulteta" labelProperty="special1"/>
    </html:select>
</td>
</tr>
<tr>
<td><font class="text_10">&nbsp;Группа:</font></td>
<td height=25 align=right id="gruppa" style="display:none"> 
    <html:select styleClass="select_f1" onchange="flushKonGrp();" name="abit_A" property="kodGruppy" tabindex="4">
    <html:options collection="abit_A_S1" property="kodGruppy" labelProperty="special4" />
    </html:select>
</td>
</tr>
</table>
<table border="0">
<tr>
  <td><font class="text_10">Вид:&nbsp</font></td>
  <td height=25>
    <html:select onchange="refill(this.value);" styleClass="select_f1" name="abit_A" property="special5">
    <html:option value="1">дискретная 1</html:option>
    <html:option value="2">дискретная 2</html:option>
    <html:option value="0">сплошная</html:option>
    </html:select>
  </td>
</tr>
</table>
<table border="0" align="center">
<tr align="center">
  <td height="30"><html:submit styleClass="button" value="Построить" tabindex="5"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/raspredballs.do">
  <td><html:submit styleClass="button" value="Выход" tabindex="6" property="exit"/></td>
</html:form>
</tr>
</table>
</td>
<td width="80%" height="100%" align="center">

<%-------------------------------%>
<%----- Область гистограммы -----%>
<%-------------------------------%>

<logic:present name="abit_A" property="special3">
<table border="0" cellspacing=<bean:write name="abit_A" property="special5"/> cellpadding="0" align="center" valign="middle">
<tr><td height="20"></td></tr>
<tr>
   <td align=center colspan=100>
      <font class="text_11">План&nbsp;приёма:&nbsp;<bean:write name="abit_A" property="planPriema"/></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="text_11">Конкурс:&nbsp;<bean:write name="abit_A" property="special6"/></font>
   </td>
</tr>
<tr><td height="20"></td></tr>
<tr valign="middle">
    <td width="60"><font class="text_9">Кол-во:&nbsp;&nbsp;</td>
   <logic:iterate id="koll" collection="<%=(java.util.ArrayList)kollsx%>">
    <td width="20" align="center"><font class="text_8"><%=koll%></font></td>
    <td width="0"></td>
   </logic:iterate>
</tr>
<tr>
    <td><font class="text_10"></td>
   <logic:iterate id="koll" collection="<%=(java.util.ArrayList)kollsx%>">
    <td width="20" class="light" align="center"><font class="text_8"></font></td>
    <td width="0"></td>
   </logic:iterate>
</tr>
<logic:iterate id="rows" collection="<%=(java.util.ArrayList)cellsx%>">
  <tr>
    <td><font class="text_10"></td>
   <logic:iterate id="cell" collection="<%=(java.util.ArrayList)rows%>">
     <td bgcolor=<bean:write name="cell" property="color"/> width="20" height=<bean:write name="abit_A" property="special3"/>></td>
     <td width="0"></td>
    </logic:iterate>
  </tr>
</logic:iterate>
<tr>
    <td><font class="text_10"></td>
   <logic:iterate id="koll" collection="<%=(java.util.ArrayList)kollsx%>">
    <td width="20" class="light" align="center"><font class="text_8"></font></td>
    <td width="0"></td>
   </logic:iterate>
</tr>
<tr height="2"><td></td></tr>
<tr>
    <td><font class="text_9">Баллы:</td>
   <logic:iterate id="ball" collection="<%=(java.util.ArrayList)ballsx%>">
    <td width="20" class="light" align="center"><font class="text_8"><%=ball%></font></td>
    <td width="0"></td>
   </logic:iterate>
</tr>
</table>
</logic:present>
</td>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>

