<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
    import      = "abit.Constants"
    import      = "abit.util.StringUtil"
%>

<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='abitSrchAction' scope='request'>
 <logic:redirect forward='abit_srch'/>
</logic:notPresent>

<logic:notPresent name="abitSrchForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

var valid1 = " ���������������������������������1234567890*.,()"
var valid2 = " ������������������������������������������������������������������*.,()"
var valid4 = "1234567890*."
var valid5 = "0123456789,*."
var valid6 = "����*."
var valid7 = "���*."
var valid8 = "����*."
var valid9 = " ������������������������������������������������������������������1234567890*.,()"
var tmp;
var temp
var teamLength

function checkFields(){
//-------------------------------------------------------------------------------------------------
for(var i=0; i<document.forms(0).nazvanieRajona.value.length; i++) {
   temp = "" + document.forms(0).nazvanieRajona.value.substring(i, i+1)
   if(valid9.indexOf(temp) == "-1") {
      alert("���� ''�����'' ����� �������� ������ �� ���� �������� �������� ��� ����")
      document.forms(0).nazvanieRajona.focus()
      return false
   }
}
if(document.forms(0).gdePoluchilSrObrazovanie.value == "�") {
  if(document.forms(0).nazvanie.value == "�������"  || document.forms(0).nazvanie.value == "�������" ||
     document.forms(0).nazvanie.value == "�������"  || document.forms(0).nazvanie.value == "�������" ||
     document.forms(0).nazvanie.value == "��������" || document.forms(0).nazvanie.value == "��������" ||
     document.forms(0).nazvanie.value == "��������" || document.forms(0).nazvanie.value == "��������")
     return true 
  else {
   alert("���� ''���������� �����'' ����� ��������� ��������: (''�������'',''��������'',''��������'',''�������'')")
    document.forms(0).nazvanieRajona.focus()
    return false
  }
}
}

function autoInit(){
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "-") {
    document.forms(0).nazvanieOblasti.value = "*"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "�") {
    document.forms(0).nazvanieOblasti.value = "����������"
    document.forms(0).nazvanie.value = "�����"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "�") {
    document.forms(0).nazvanieOblasti.value = "����������"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "�") {
    document.forms(0).nazvanieOblasti.value = "����������"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "�") {
    document.forms(0).nazvanieOblasti.value = "*"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
}

function autoInit1(){
 var temp = "";
 var index = document.forms(0).special1.selectedIndex;
 var j=0;
    if(document.forms(0).special1.options[index].value == "*")
      document.forms(0).nomerLichnogoDela.value = "*";
    else 
      document.forms(0).nomerLichnogoDela.value = document.forms(0).special1.options[index].text.toLowerCase()+"*";
}

function autoInit2(){
    if(document.forms(0).nazvanie.value == "�������" || document.forms(0).nazvanie.value == "�������"){
      document.forms(0).nazvanieRajona.value = "���������"
      document.forms(0).tipOkonchennogoZavedenija.focus()
    } 
    if(document.forms(0).nazvanie.value == "�������" || document.forms(0).nazvanie.value == "�������"){
      document.forms(0).nazvanieRajona.value = "���������"
      document.forms(0).tipOkonchennogoZavedenija.focus()
    }
    if(document.forms(0).nazvanie.value == "��������" || document.forms(0).nazvanie.value == "��������"){
      document.forms(0).nazvanieRajona.value = "����������"
      document.forms(0).tipOkonchennogoZavedenija.focus()
    }
    if(document.forms(0).nazvanie.value == "��������" || document.forms(0).nazvanie.value == "��������"){
      document.forms(0).nazvanieRajona.value = ""
      document.forms(0).tipOkonchennogoZavedenija.focus()
    }
}

function exec() {
if(document.forms(0).special1 != null &&
   document.forms(0).shifrFakulteta != null &&
   document.forms(0).pol != null &&
   document.forms(0).gdePoluchilSrObrazovanie != null &&
   document.forms(0).polnoeNaimenovanieZavedenija != null &&
   document.forms(0).shifrMedali != null &&
   document.forms(0).shifrLgot != null &&
   document.forms(0).shifrKursov != null &&
   document.forms(0).kodTselevogoPriema != null) {

    teamLength = document.forms(0).special1.options.length
    teamTXT = new Array(teamLength)
    teamVAL = new Array(teamLength)
    for(var ind=0;ind<document.forms(0).special1.length;ind++) {
       eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
       eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
    }
    document.forms(0).shifrFakulteta.selectedIndex=0
    document.forms(0).special1.selectedIndex=0
    document.forms(0).pol.selectedIndex=0
    document.forms(0).gdePoluchilSrObrazovanie.selectedIndex=0
    document.forms(0).polnoeNaimenovanieZavedenija.selectedIndex=0
    document.forms(0).shifrMedali.selectedIndex=0
    document.forms(0).shifrLgot.selectedIndex=0
    document.forms(0).shifrKursov.selectedIndex=0
    document.forms(0).kodTselevogoPriema.selectedIndex=0
    fillSelect(document.forms(0).shifrFakulteta.value);
    document.forms(0).shifrFakulteta.focus()
    }
}

function fillSelect(selectCtrl) {
var i,j=0
if(selectCtrl == "*") {
// �������� ������������ �����
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// ���������� ����� "*"
   document.forms(0).special1.options[0] = new Option("*")
   document.forms(0).special1.options[0].value = "*"
   document.forms(0).special1.options[0].selected = true
return
}
j=0
// �������� ������������ �����
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// ���������� ����� �����
   document.forms(0).special1.options[0] = new Option("*")
   document.forms(0).special1.options[0].value = "*"
j++
for(i = 0; i < teamLength; i++) {
   if(teamTXT[i].charAt(0) == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     j++
   }
}
// ������� � ������ ������
document.forms(0).special1.options[0].selected = true
}

function help_me() {
  alert("��� ���������� ����� ����� ����� ������������ ����������� �������:\n\n\" * \" - ������������ ���������� ��������\n\" . \" - ��������� ��������� �������");
  return true;
}

</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="abitSrchForm" property="action">
<bean:define id="action" name="abitSrchForm" property="action"/>


<%-----------------------------------------------------------------%>
<%------------------------ ����� ������� --------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="search">
<bean:define id="values" name="abit_A" type="abit.bean.AbiturientBean"/>
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">����� ������������</template:put>
<template:put name="target_name">����� ������������ �� ������ ��������</template:put>
<template:put name="content">
<table cols=7 align=center border=1 cellSpacing=0>
<html:form action="/abit_srch_online?action=searching&offset" onsubmit="return checkFields();">
<tr><td width=3%><td width=24%><td width=22%><td width=2%><td width=3%><td width=24%><td width=22%>
<%-------------------- ������ ������� -------------------------%>
<tr>
<td></td>
<td valign=center><font class="text_9">&nbsp;���������:</font></td>
<td valign=center>
    <html:select onchange="fillSelect(this.value),autoInit1();" styleClass="select_f3" name="abit_A" property="shifrFakulteta" tabindex="1">
    <html:option value="*"/>
    <html:options collection="abit_A_S2" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta" />
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NomerShkoly"/>
<td valign=center><font class="text_9">&nbsp;����� ����� ��� �������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="nomerShkoly" maxlength="4" size='4' tabindex="28" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NazvanieSpetsialnosti"/>
<td valign=center><font class="text_9">&nbsp;����-��&nbsp;�����������:</font></td>
<td valign=center>
    <html:select onchange="autoInit1();" styleClass="select_f3" name="abit_A" property="special1" tabindex="2">
    <html:options collection="abit_A_S3" property="special1" labelProperty="abbreviatura" />
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="InostrannyjJazyk"/>
<td valign=center><font class="text_9">&nbsp;����������� ����:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="inostrannyjJazyk" 
               maxlength="1" size="1" tabindex="29" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="TipDokSredObraz"/>
<td valign=center><font class="text_9">&nbsp;����� ���������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="tipDokSredObraz" 
               maxlength="1" size="1" tabindex="3" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Grajdanstvo"/>
<td valign=center><font class="text_9">&nbsp;�����������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="grajdanstvo" 
               maxlength="20" size="20" tabindex="30" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="SeriaAtt,NomerAtt"/>
<td valign=center><font class="text_9">&nbsp;�����, ����� ���������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="seriaAtt" 
               maxlength="5" size="4" tabindex="4" value="*"/>&nbsp;
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="nomerAtt" 
               maxlength="10" size="9" tabindex="5" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="PolnoeNaimenovanieZavedenija"/>
<td valign=center><font class="text_9">&nbsp;��������-� �����. �����.:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="polnoeNaimenovanieZavedenija" tabindex="31">
    <html:option value="*" />
    <html:options collection="abit_A_S8" property="polnoeNaimenovanieZavedenija" labelProperty="sokr" />
    </html:select>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="KodFormyOb"/>
<td valign=center><font class="text_9">&nbsp;����� ��������:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="kodFormyOb" tabindex="6">
    <html:option value="*"/>
    <html:options collection="abit_forms" property="kodFormyOb" labelProperty="sokr"/>
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Prinjat"/>
<td valign=center><font class="text_9">&nbsp;��������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="prinjat" value="<%=values.getSpecial13()%>" size='25' maxlength='50' tabindex="32"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="KodOsnovyOb"/>
<td valign=center><font class="text_9">&nbsp;������ ��������:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="kodOsnovyOb" tabindex="7">
    <html:option value="*"/>
    <html:options collection="abit_osnovs" property="kodOsnovyOb" labelProperty="sokr"/>
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="gruppa"/>
<td valign=center><font class="text_9">&nbsp;������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="gruppa" 
               maxlength="8" size="6" tabindex="33" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NomerLichnogoDela"/>
<td valign=center><font class="text_9">&nbsp;����� ������� ����:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="nomerLichnogoDela" 
               maxlength="10" size="10" tabindex="8" value="*"/>
</td>
<td width=1%></td>
<td rowspan=2><html:radio name="abit_A" property="priznakSortirovki" value="NomerPlatnogoDogovora"/>
<td valign=center><font class="text_9">&nbsp;������� ��������:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="ege" tabindex="34">
    <html:option value="*" />
    <html:option value="�" />
    <html:option value="�" />
    </html:select>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Familija"/>
<td valign=center><font class="text_9">&nbsp;�������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="familija" 
               maxlength="50" size="25" tabindex="9" value="*"/>
</td>
<td width=1%></td>
<td valign=center><font class="text_9">&nbsp;�����&nbsp;��������&nbsp;��������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="nomerPlatnogoDogovora" 
               maxlength="50" size="25" tabindex="35" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Imja"/>
<td valign=center><font class="text_9">&nbsp;���:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="imja" 
               maxlength="50" size="25" tabindex="10" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NapravlenieOtPredprijatija"/>
<td valign=center><font class="text_9">&nbsp;���������. �� ������.:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="napravlenieOtPredprijatija" 
               maxlength="1" size="1" tabindex="36" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Otchestvo"/>
<td valign=center><font class="text_9">&nbsp;��������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="otchestvo" 
               maxlength="50" size="25" tabindex="11" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="ShifrLgot"/>
<td valign=center><font class="text_9">&nbsp;������ �����:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="shifrLgot" value="<%=values.getLgoty()%>" size='25' maxlength='50' tabindex="37"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Pol"/>
<td valign=center><font class="text_9">&nbsp;���:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="pol" tabindex="12">
    <html:option value="*" />
    <html:option value="�" />
    <html:option value="�" />
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Shifrmedali"/>
<td valign=center><font class="text_9">&nbsp;�������������� ����������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="shifrMedali" value="<%=values.getMedal()%>" size='25' maxlength='50' tabindex="38"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="TipDokumenta"/>
<td valign=center><font class="text_9">&nbsp;��� ���������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="tipDokumenta" 
               maxlength="1" size="1" tabindex="13" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NomerSertifikata"/>
<td valign=center><font class="text_9">&nbsp;����� �����������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="attestat" 
               maxlength="250" size="20" tabindex="39" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="SeriaDokumenta,NomerDokumenta"/>
<td valign=center><font class="text_9">&nbsp;������� (�����, �):</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="seriaDokumenta" 
               maxlength="10" size="4" tabindex="14" value="*"/>&nbsp;-&nbsp;
    <html:text styleClass="text_f9" name="abit_A" property="nomerDokumenta" 
               maxlength="10" size="10" tabindex="15" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="SrokObuchenija"/>
<td valign=center><font class="text_9">&nbsp;���� ��������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="srokObuchenija" 
               maxlength="3" size="3" tabindex="40" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="KemVydDokument"/>
<td valign=center><font class="text_9">&nbsp;���&nbsp;�����&nbsp;�������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="kemVydDokument" 
               maxlength="250" size="25" tabindex="16" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="TrudovajaDejatelnost"/>
<td valign=center><font class="text_9">&nbsp;�������� ������������:</font></td>
<td valign=center>
    <html:text accesskey="�"  styleClass="text_f9" name="abit_A" property="trudovajaDejatelnost" 
               maxlength="1" size="1" tabindex="41" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="DataVydDokumenta"/>
<td valign=center><font class="text_9">&nbsp;����&nbsp;������&nbsp;��������:</font></td>
<td valign=center><font class="text_9">�</font>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="special9" 
               maxlength="10" size='9' tabindex="17" value="00-00-0000"/>
	<font class="text_9">��</font>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="special10"
               maxlength="10" size='9' tabindex="18" value="99-99-9999"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="kopijaSertifikata"/>
<td valign=center><font class="text_9">&nbsp;����� �����������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="kopijaSertifikata" 
               maxlength="1" size="1" tabindex="42" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="DataRojdenija"/>
<td valign=center><font class="text_9">&nbsp;���� ��������:</font></td>
<td valign=center><font class="text_9">��</font>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="special2" 
               maxlength="10" size='9' tabindex="19" value="00-00-0000"/>
	<font class="text_9">��</font>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="special3" 
               maxlength="10" size='9' tabindex="20" value="99-99-9999"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="ShifrKursov"/>
<td valign=center><font class="text_9">&nbsp;���������������� �����:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="shifrKursov" value="<%=values.getInformatsijaOKursah()%>" size='25' maxlength='50' tabindex="43"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="GodOkonchanijaSrObrazovanija"/>
<td valign=center><font class="text_9">&nbsp;��� ��������� ��. �����.:</font></td>
<td valign=center><font class="text_9">��</font>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="special5" 
               maxlength="4" size='4' tabindex="21" value="1950"/><font class="text_9"> ��</font>
    <html:text accesskey="�" styleClass="text_f9_short" name="abit_A" property="special6" 
               maxlength="4" size='4' tabindex="22" value="9999"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NujdaetsjaVObschejitii"/>
<td valign=center><font class="text_9">&nbsp;����������� � ���������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="nujdaetsjaVObschejitii" 
               maxlength="1" size="1" tabindex="44" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="TipOkonchennogoZavedenija"/>
<td valign=center><font class="text_9">&nbsp;��� ����������� �������.:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="tipOkonchennogoZavedenija" 
               maxlength="1" size="1" tabindex="23" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Prinjat"/>
<td valign=center><font class="text_9">&nbsp;����-��&nbsp;����������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="special8" 
               maxlength="100" size="10" tabindex="45" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="GdePoluchilSrObrazovanie"/>
<td valign=center><font class="text_9">&nbsp;��� ������� ��. �����.:</font></td>
<td valign=center>
    <html:select onchange="autoInit();" styleClass="select_f3" name="abit_A" property="gdePoluchilSrObrazovanie" tabindex="24">
    <html:option value="*" />
    <html:option value="�" />
    <html:option value="�" />
    <html:option value="�" />
    <html:option value="�" />
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="DokumentyHranjatsja"/>
<td valign=center><font class="text_9">&nbsp;��������� ��������:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="dokumentyHranjatsja" tabindex="46">
    <html:option value="*" />
    <html:option value="�" />
    <html:option value="�" />
    </html:select>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Nazvanie"/>
<td valign=center><font class="text_9">&nbsp;���������� �����:</font></td>
<td valign=center>
    <html:text accesskey="�" onchange="autoInit2();" styleClass="text_f9" name="abit_A" property="nazvanie" 
               maxlength="150" size="25" tabindex="25" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Sobesedovanie"/>
<td valign=center><font class="text_9">&nbsp;�������������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="sobesedovanie" 
               maxlength="1" size="1" tabindex="47" value="*"/>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NazvanieRajona"/>
<td valign=center><font class="text_9">&nbsp;����� (�� ��� �. �����):</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="nazvanieRajona" 
               maxlength="50" size="25" tabindex="26" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="KodTselevogoPriema"/>
<td valign=center><font class="text_9">&nbsp;������� �����:</font></td>
<td valign=center>
<html:select styleClass="select_f3" name="abit_A" property="kodTselevogoPriema" tabindex="48">
    <html:option value="*"/>
    <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
</html:select>
</td>
</tr>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NazvanieOblasti"/>
<td valign=center><font class="text_9">&nbsp;������� ��� ����������:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="nazvanieOblasti" 
               maxlength="50" size="25" tabindex="27" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Ball"/>
<td valign=center><font class="text_9">&nbsp;��������� ����:</font></td>
<td valign=center>
    <html:text accesskey="�" styleClass="text_f9" name="abit_A" property="special7" 
               maxlength="3" size="2" tabindex="49" value="*"/>
</td>

<%-------------------- ������ ������� -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="PreemptiveRight"/>
<td valign=center><font class="text_9">&nbsp;���������������� �����:</font></td>
<td valign=center>
    <html:text styleClass="text_f9" name="abit_A" property="preemptiveRight"  size='25' maxlength='50' tabindex="51" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="ProvidingSpecialConditions"/>
<td valign=center><font class="text_9">&nbsp;����������� �������:</font></td>
<td valign=center>
    <html:text styleClass="text_f9" name="abit_A" property="providingSpecialCondition"  size='25' maxlength='50' tabindex="51" value="*"/>
</td>
</tr>
<%-------------------- ������ ������� -------------------------%>
<tr>
<td align="center" valign="middle" colspan="8"><font class="text_9">����������&nbsp;�����������&nbsp;��:</font>&nbsp;&nbsp;
         <html:select styleClass="select_f3" name="abit_A" property="special4" tabindex="50">
          <html:option value="ASC">�����������</html:option>
          <html:option value="DESC">��������</html:option>
         </html:select>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="text_9">&nbsp;��������&nbsp;���&nbsp;�������������&nbsp;��������:&nbsp;</font>
     <html:select styleClass="select_f1" name="abit_A" property="useAllSpecs" tabindex="51">
     <html:option value="no">���</html:option>
     <html:option value="yes">��</html:option>
    </html:select>
</td>
</tr>
</table>

<%-------------------- �������������� ������� ----------------------%>
<table border="0" align="center">
<tr align="center">
  <td><html:reset styleClass="button" value="����� ����������" tabindex="52"/></td>
  <td><html:submit styleClass="button" value="�����" tabindex="53"/></td>
</html:form>

<html:form action="/abit_srch.do">
  <td><html:button styleClass="button" onclick="help_me();" property="hlp" tabindex="55" value="�������"/>
  <td><html:submit styleClass="button" property="exit" tabindex="56" value="�����"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">���������� ������ ������������</template:put>
<template:put name="target_name">���������� ������ �� ������ ��������</template:put>
<template:put name="content">
<p align=left><font class="text_10">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
�����&nbsp;�������&nbsp;�����:&nbsp;&nbsp;<bean:write name="abit_A" property="special22"/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
�� <bean:write name="abit_A" property="priznakSortirovki"/>.
</font>
<table cols=34 align=center border=1 cellSpacing=0>
<thead>
<tr><td valign=middle align=middle><font class="text_th">&nbsp;�&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����.&nbsp; &nbsp;����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����&nbsp; &nbsp;������.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����&nbsp; &nbsp;����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;������&nbsp; &nbsp;����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;����. ����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;����. ������.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�����&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;����.&nbsp; ����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;���&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;��������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�������������� ����������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;������ �����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;&nbsp;�����&nbsp;�����.&nbsp;���.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;&nbsp;���&nbsp;����.&nbsp;&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;���&nbsp;�����.&nbsp; &nbsp;�����.&nbsp;���.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;���&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;���� ��������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;��� &nbsp;�������&nbsp; ��.&nbsp;���.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����&nbsp; &nbsp;�����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;��.&nbsp;��.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;����.&nbsp; &nbsp;�&nbsp;�����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;����������&nbsp;�����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;������&nbsp;������������&nbsp;���������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;���&nbsp; &nbsp;�����.&nbsp; &nbsp;�����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;����.&nbsp; &nbsp;����.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;���������.&nbsp; &nbsp;��&nbsp;������.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;&nbsp;���&nbsp;&nbsp; ���.</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�������&nbsp; &nbsp;(c����,&nbsp;�����)&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;����&nbsp;&nbsp;������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;���&nbsp;�����&nbsp;�������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�������.&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�������&nbsp; &nbsp;�����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;����&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����&nbsp; ����.&nbsp;���</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;�����&nbsp; ����.&nbsp;���</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;��������&nbsp;</font></td>
<td valign=middle align=middle><font class="text_th">&nbsp;��������&nbsp;</font></td></tr>

</thead>
<logic:iterate id="abit_A" name="abits_A" scope='request'>
<tr>
  <td valign=center align=center>&nbsp;<html:link href="abit_md_online.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="number"/></html:link>&nbsp;</td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="dokumentyHranjatsja"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="tipDokSredObraz"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="formaOb"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="osnovaOb"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="special1"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="special8"/>&nbsp;</font></td>
  <td valign=center align=center>&nbsp;<html:link href="abit_md_online.do?action=give_ots" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="nomerLichnogoDela"/></html:link>&nbsp;</td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="gruppa"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<html:link href="abit_md_online.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="familija"/></html:link>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="imja"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="otchestvo"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="shifrKursov"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="shifrMedali"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="shifrLgot"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nomerPlatnogoDogovora"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="dataRojdenija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="godOkonchanijaSrObrazovanija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="pol"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="srokObuchenija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="grajdanstvo"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="gdePoluchilSrObrazovanie"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nomerShkoly"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="inostrannyjJazyk"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nujdaetsjaVObschejitii"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nazvanie"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nazvanieRajona"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nazvanieOblasti"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="polnoeNaimenovanieZavedenija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="tipOkonchennogoZavedenija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="trudovajaDejatelnost"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="napravlenieOtPredprijatija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="tipDokumenta"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9"><bean:write name="abit_A" property="seriaDokumenta"/>&nbsp;<bean:write name="abit_A" property="nomerDokumenta"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="dataVydDokumenta"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="kemVydDokument"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="sobesedovanie"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="shifrPriema"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="ball"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nomerSertifikata"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="kopijaSertifikata"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="seriaAtt"/>&nbsp;
                                                            <bean:write name="abit_A" property="nomerAtt"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="prinjat"/>&nbsp;</font></td>
</tr>
</logic:iterate>
<%--
     for Debugging Purposes Only !!!
<tr><td colspan=1000><bean:write name="abit_A" property="special1"/></td></tr> --%>
<logic:notPresent name="abit_A" property="kodAbiturienta">
<tr>
  <td align=center valign=center colspan=44><font class="text_10">
     &nbsp;&nbsp;&nbsp;&nbsp;
     ��&nbsp;������&nbsp;�������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     ��&nbsp;������&nbsp;�������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     ��&nbsp;������&nbsp;�������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     ��&nbsp;������&nbsp;�������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     ��&nbsp;������&nbsp;�������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     ��&nbsp;������&nbsp;�������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������
  </font></td>
</logic:notPresent>
<tr><td colspan=60>
<%-- ������ ����������� ������ �� ���������� --%>
<div id="SlideButtons" style="FONT-SIZE: 0.65em; WIDTH:1px ; FONT-FAMILY: 'Trebuchet MS',Tahoma,Verdana,Geneva,Arial,Helvetica,sans-serif; POSITION: relative">
<table valign=center align=left width=100% cellSpacing=2 border=0>
<tr>
  <td valign=center align=middle>
<html:form action="/abit_srch?action=searching">
  <html:submit property="beg" styleClass="button_sd" value="������"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_srch?action=searching">
  <html:submit property="prev" styleClass="button_sd" value="����������"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/search_rtf.do">
  <html:submit styleClass="button_sd" value="RTF-�����"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/search_txt.do">
  <html:submit styleClass="button_sd" value="TXT-����"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_srch.do?action=search">
  <html:submit styleClass="button_sd" value="�����"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_srch.do?action=searching">
  <html:submit property="next" styleClass="button_sd" value="���������"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_srch.do?action=searching">
  <html:submit property="end" styleClass="button_sd" value="�����"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_srch.do?action=searching">
  <html:submit styleClass="button_sd" property="exit" value="�����"/>
</html:form></td>
</tr>
</table>
</td>
<%-- ������ ����������� ������ �� ���������� --%>
<SCRIPT language=JavaScript src="layouts/all/slideButtons.js" defer type=text/javascript></SCRIPT></div>
</tr></table>
<BR><BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>