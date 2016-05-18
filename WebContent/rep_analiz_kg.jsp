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

<logic:notPresent name='analizKgAction' scope='request'>
 <logic:redirect forward='rep_analiz_kg'/>
</logic:notPresent>

<logic:notPresent name="analizKgForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
  var valid0 = "�� ";
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
    // �������� ������������ �����
    for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
      document.forms(0).kodGruppy.options[i] = null; 
    }
    // ���������� ����� "-"
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
  // �������� ������������ �����
  for (i = document.forms(0).kodFakulteta.length; i >= 0; i--){
    document.forms(0).kodFakulteta.options[i] = null; 
  }
  if(selectCtrl == "-1"){
    // ���������� ����� "-"
    document.forms(0).kodFakulteta.options[0] = new Option("-")
    document.forms(0).kodFakulteta.options[0].value = "-"
    document.forms(0).kodFakulteta.options[0].selected = true
    fillSelectGr(selectCtrl);
    return;
  }
  // ���������� ������� '*'
    document.forms(0).kodFakulteta.options[0] = new Option("*")
    document.forms(0).kodFakulteta.options[0].value = "-1"
j=1;
  // ���������� ����� ����� 
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
  // ������� � ������ ������
  document.forms(0).kodFakulteta.options[0].selected = true
}

function fillSelectGr(selectCtrl) {
  var i,j,offset,next_offset;
  flushKonGrp();
  if(selectCtrl == "-1"){
    // �������� ������������ �����
    for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
      document.forms(0).kodGruppy.options[i] = null; 
    }
    // ���������� ����� "-"
    document.forms(0).kodGruppy.options[0] = new Option("-")
    document.forms(0).kodGruppy.options[0].value = "-"
    document.forms(0).kodGruppy.options[0].selected = true
    return;
  }
j=1;
  // �������� ������������ �����
  for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
    document.forms(0).kodGruppy.options[i] = null
  }
// ���������� ������� '*'
 document.forms(0).kodGruppy.options[0] = new Option("*")
 document.forms(0).kodGruppy.options[0].value = "-1"

  // ���������� ����� ����� 
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
  // ������� � ������ ������
  document.forms(0).kodGruppy.options[0].selected = true
}

function checkfields(){
 if(document.forms(0).special2.options[document.forms(0).special2.selectedIndex].value == -1 &&
    document.forms(0).kodFakulteta.options[document.forms(0).kodFakulteta.selectedIndex].value == "-" &&
    document.forms(0).kodGruppy.options[document.forms(0).kodGruppy.selectedIndex].value == "-" &&
    document.forms(0).kodKonGrp.options[document.forms(0).kodKonGrp.selectedIndex].value == "-1"){

    alert("���������� ������� ''���������� ������'' ��� ''���������'' ��� ''�������������'' ��� ''������''");
    document.forms(0).kodKonGrp.focus();
    return false;
 }
  return true;
}

function help_me(){
 alert("��������� - �������� � ���� ���, ������� ��������� � ��������� ���������� � ������� 3 ��� ����� ������.\n"+
       "��������� - �������� ������ ��� ����������, ������� ����������� ���� ������.\n ");
}

</SCRIPT>

<logic:present name="analizKgForm" property="action">
<bean:define id="action" name="analizKgForm" property="action"/>

<%---------------- �������� ��������� ----------------------------%>

<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name='title'>������������� �������</template:put>
<template:put name="target_name">������������� �������</template:put>
<template:put name="sub_name"> </template:put>
<template:put name='content'>
<table border="1" cellspacing="0" cellpadding="0" align="center" frame="box">
<html:form action="rep_analiz_kg?action=show" onsubmit="return checkfields();">
<tr valign="middle">
<td><font class="text_10">&nbsp;���.&nbsp;������:&nbsp;</font></td>
<td height=25 align=right id="kng" style="display:none"> 
    <html:select styleClass="select_f1" onchange="flushOthers();" name="abit_TM" property="kodKonGrp" tabindex="1">
    <html:option value="-1">-</html:option>
    <html:options collection="abit_A_S4" property="kodKonGrp" labelProperty="nazvanie"/>
    </html:select>
</td>
</tr>
<tr valign="middle">
<td><font class="text_10">&nbsp;���������:</font></td>
<td height=25 align=right id="fakult" style="display:none"> 
    <html:select onchange="fillSelectSp(this.value)" styleClass="select_f1" 
                               name="abit_TM" property="special2" tabindex="2">
    <html:option value="-1">-</html:option>
    <html:options collection="abit_A_S2" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta" />
    </html:select>
</td>
</tr>
<tr valign="middle">
<td><font class="text_10">&nbsp;����-��:</font></td>
<td height=25 align=right id="spec" style="display:none">
    <html:select onchange="refillGruppy(this.value);" styleClass="select_f1" name="abit_TM" property="kodFakulteta" tabindex="3">
    <html:options collection="abit_A_S3" property="kodFakulteta" labelProperty="special1"/>
    </html:select>
</td>
</tr>
<tr>
<td><font class="text_10">&nbsp;������:</font></td>
<td height=25 align=right id="gruppa" style="display:none"> 
    <html:select styleClass="select_f1" onchange="flushKonGrp();" name="abit_TM" property="kodGruppy" tabindex="4">
    <html:options collection="abit_A_S1" property="kodGruppy" labelProperty="special4" />
    </html:select>
</td>
</tr>
</table>

<table align=center border=0 cellspacing=0>
<tr>
   <td><font class="text_10">������&nbsp���������:&nbsp&nbsp</font></td>
   <td width=30><font class="text_10"><bean:write name="abit_TM" property="amount"/></font></td>
</tr>
<tr>
   <td><font class="text_10">���� ������:</font></td>
   <td><font class="text_10"><bean:write name="abit_TM" property="planPriema"/></font></td>
</tr>
<tr>
   <td><font class="text_10">�������:</font></td>
   <td><font class="text_th"><bean:write name="abit_TM" property="special5"/></font></td>
</tr>
<tr>
   <td><font class="text_10">������� �����:</font></td>
   <td><font class="text_10"><bean:write name="abit_TM" property="tselevojPriem"/></font></td>
</tr>
<tr>
   <td><font class="text_10">���������:</font></td>
   <td><font class="text_10"><bean:write name="abit_TM" property="shifrLgot"/></font></td>
</tr>
<tr><td height=8></td></tr>
</table>
<table border=0>
<tr>
   <td align=center>
      <html:submit styleClass="button_sd" value="��������"/></td>
    </html:form>

   <td align=center>
    <html:form action="/rep_analiz_kg.do?action=report">
      <html:submit styleClass="button_sd" value="�����"/></td>
    </html:form>

   <td align=center>
    <html:form action="/rep_analiz_kg.do">
    <html:button styleClass="button_sd" onclick="help_me();" property="hlp" value="�������"/></td>
   </html:form>

    <html:form action="/rep_analiz_kg.do">
   <td><html:submit styleClass="button_sd" property="exit" value="�����"/></td>
       </html:form>
</tr>
</table>
</td>
<td width="100">
<td>
<table align=center border=1 cellspacing=0>
<thead>
<tr>
<td height="28" align=center><font class="text_th">&nbsp;&nbsp;�����&nbsp;&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;&nbsp;���-��&nbsp;&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;&nbsp;��������&nbsp;&nbsp;</font></td>
</tr>
</thead>
<tr>
   <td class="light" align=center><font class="text_th">&nbsp;������&nbsp;</font></td>
   <td align=center><font class="text_10">&nbsp;<bean:write name="abit_TM" property="shifrMedali"/>&nbsp;</font></td>
   <td align=center><font class="text_10">&nbsp;<bean:write name="abit_TM" property="special1"/>&nbsp;</font></td>
</tr>
<%-- HEAVY LOGIC INTERFACE BUILDER --%>
<logic:iterate id="balls" name="abits_TM" scope='request' type='abit.bean.AbiturientBean'>
<tr>
   <td class="light" align=center><font class="text_th">&nbsp;<bean:write name="balls" property="special2"/>&nbsp;</font></td>
   <td align=center><font class="text_10">&nbsp;<bean:write name="balls" property="special3"/>&nbsp;</font></td>
   <td align=center><font class="text_10">&nbsp;<bean:write name="balls" property="special4"/>&nbsp;</font></td>
</tr>
</logic:iterate>
</table>
</td>
</tr>
</table>
</template:put>
</template:insert>
</logic:present>