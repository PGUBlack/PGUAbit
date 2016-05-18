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

<logic:notPresent name='zchAction' scope='request'>
 <logic:redirect forward='zachislen'/>
</logic:notPresent>

<logic:notPresent name="zchForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! 
   int tabindex;
   int row;
%>

<SCRIPT LANGUAGE="JavaScript">
  var valid0 = "��1234567 ";
  var massLength;
  var massKodFak;
  var massGruppy;
  var massLength2;
  var massSpecs;

function help_me() {
  alert("1. ���� �� ������� ���������� �������� �������������,"+
         "\n    �� � ���������� �������� ������ ���� ������������,"+
         "\n    �������� ��������� �� ��������� ������������� ���������� �� �� ������.\n"+
       "\n2. ���� �������� ������������� �� ���������� (*),"+
         "\n    �� ����������� ����� ���������� ������ �� ��������� ������.");
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
  fillSelectSp("-1");
  fillSelectGr("-1");
  gruppa.style.display = "block";
  fakult.style.display = "block";
  spec.style.display   = "block";
}

function exec1(){
  teamLength = document.forms(0).special3.options.length;
  teamTXT = new Array(teamLength);
  teamVAL = new Array(teamLength)
  for(var ind=0;ind<document.forms(0).special3.length;ind++) {
    eval("teamTXT["+ind+"]=document.forms(0).special3.options["+ind+"].text");
    eval("teamVAL["+ind+"]=document.forms(0).special3.options["+ind+"].value");
  }
}

function refillGruppy(spec) {
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

function fillSelectSp(selectCtrl) {
  var i,j,offset,next_offset;
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
j=0;
  // �������� ������������ �����
  for (i = document.forms(0).kodGruppy.length; i >= 0; i--){
    document.forms(0).kodGruppy.options[i] = null
  }
// ���������� ����� "-"
 document.forms(0).kodGruppy.options[0] = new Option("-")
 document.forms(0).kodGruppy.options[0].value = "-"

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
 if(document.forms(0).special2.options[document.forms(0).special2.selectedIndex].value == -1){
  alert("���������� ������� ���������!");
  document.forms(0).special2.focus();
  return false;
 }
 if(document.forms(0).kodFakulteta.options[document.forms(0).kodFakulteta.selectedIndex].value == -1 &&
    document.forms(0).kodGruppy.options[document.forms(0).kodGruppy.selectedIndex].value == "-"){
  alert("���������� ������� ������������� ��� ������!");
  document.forms(0).kodFakulteta.focus();
  return false;
 }
  return true;
}

function checkfields2(){
  if(document.forms(0).elements[1].value == null) {
   alert("���������� ���������. ��� ������!");
   return false;
  }
  var tmp="";
  var nn="";
  var all=0;
  var j=0;
  for(i=0;i<document.forms(0).special22.value;i++){
    tmp=document.forms(0).elements[2+i*2].value;
    nn=document.forms(0).elements[2+i*2].name;
    if((valid0.indexOf(tmp) == "-1")&&(nn.indexOf("cells1") != "-1")){
        alert("���� ''��������'' ����� �������� ������ �� ����� ''1'',''2'',''3'',''4'',''�'',''7'' ��� ''�''");
        document.forms(0).elements[2+i*2].focus();
        return false;
    }
    tmp=document.forms(0).elements[3+i*2].value;
    nn=document.forms(0).elements[3+i*2].name;
    if(tmp == "") continue;
    all=0;
	for(j = 0; j < teamLength; j++) {
      if((tmp.indexOf(teamTXT[j]) != "-1")&&(nn.indexOf("cells1") != "-1")){
        all=1;
	  }
    }
    if(all==0){
      alert("��������! � �� ��� ��������� ���� �������������");
      document.forms(0).elements[3+i*2].focus();
      return false;
	}
  }
}
</SCRIPT>


<logic:present name="zchForm" property="action">
<bean:define id="action" name="zchForm" property="action"/>

<%-----------------------------------------------------------------%>
<%-----------------------    ���� ������   ------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="menu">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="title">���������� ������������</template:put>
<template:put name="target_name">���������� ������������ � ���</template:put>
<template:put name="sub_name">�� ������ ��� �� �������������</template:put>
<template:put name="content">
<br>
<table border="1" cellspacing="0" cellpadding="0" align="center">
<html:form action="/zachislen?action=show&offset=new" onsubmit="return checkfields();">
<%--------------------- ������ �1 ������� -------------------------%>
<tr valign="middle">
<td><font class="text_10">&nbsp;���������:</font></td>
<td height=27 align=center id="fakult" style="display:none"> 
    <html:select onchange="fillSelectSp(this.value)" styleClass="select_f1" 
                               name="abit_A" property="special2" tabindex="1">
    <html:option value="-1">-</html:option>
    <html:options collection="abit_A_S2" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta" />
    </html:select>
</td>
</tr>

<%--------------------- ������ �2 ������� -------------------------%>
<tr valign="middle">
<td><font class="text_10">&nbsp;�������������:</font></td>
<td height=27 align=center id="spec" style="display:none">
    <html:select onchange="refillGruppy(this.value);" styleClass="select_f1" name="abit_A" property="kodFakulteta" tabindex="2">
    <html:options collection="abit_A_S3" property="kodFakulteta" labelProperty="special1"/>
    </html:select>
</td>
</tr>

<%--------------------- ������ �3 ������� -------------------------%>
<tr>
<td><font class="text_10">&nbsp;������:</font></td>
<td height=27 align=center id="gruppa" style="display:none"> 
    <html:select styleClass="select_f1" name="abit_A" property="kodGruppy" tabindex="3">
    <html:options collection="abit_A_S1" property="kodGruppy" labelProperty="special4" />
    </html:select>
</td>
</tr>

<%--------------------- ������ �4 ������� -------------------------%>
<tr valign="middle">
<td><font class="text_10">&nbsp;��������� ��������:</font></td>
<td height=27 align=center >
    <html:select styleClass="select_f1" name="abit_A" property="dokumentyHranjatsja" tabindex="5">
    <html:option value="�"/>
    <html:option value="�"/>
    <html:option value="*"/>
    </html:select>
</td>
</tr>
</table>
<%-----------------------------------------------------------------%>
<br>
<table border="0" align="center">
<tr align="center">
  <td><html:submit styleClass="button" value="������� � ����������" tabindex=""/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/abiturient.do">
  <td><html:button styleClass="button" value="�������" tabindex="" property="help" onclick="help_me();"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/abiturient.do">
  <td><html:submit styleClass="button" value="�����" tabindex="" property="exit"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%----------------    ���������� ������������     -----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<body onLoad="exec1()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">���������� ������������</template:put>
<template:put name="target_name">���������� ������������ � ���</template:put>
<template:put name="content">
<br>
<div align=center><font class="text_10">
  ����� �������:&nbsp;
  <bean:write name="abit_A" property="special22"/>&nbsp;&nbsp;�����������(��)</font>
</div>
<html:form action="/zachislen?action=updatebase" onsubmit="return checkfields2();">
<html:hidden name="abit_A" property="special22"/>
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
  <td rowspan=2><font class="text_th">&nbsp;�&nbsp;</font></td>
  <td rowspan=2><font class="text_th">&nbsp;����.&nbsp;</font></td>
  <td rowspan=2><font class="text_th">&nbsp;������&nbsp;</font></td>
  <td rowspan=2><font class="text_th">&nbsp;����� ����&nbsp;</font></td>
  <td rowspan=2><font class="text_th">&nbsp;�������&nbsp;</font></td>
  <td rowspan=2><font class="text_th">&nbsp;���&nbsp;</font></td>
  <td rowspan=2><font class="text_th">&nbsp;��������&nbsp;</font></td>
  <td rowspan=2><font class="text_th">&nbsp;��������&nbsp;</font></td>
  <td colspan=2><font class="text_th">&nbsp;��������&nbsp;</font></td></tr>
<tr>
  <td><font class="text_th">&nbsp;�������&nbsp;</font></td>
  <td><font class="text_th">&nbsp;����-��&nbsp;</font></td>
</tr>
</thead>
<%
   row   = 0;
   tabindex = 1;
%>
<tr>
<logic:iterate id="abit_A" name="abits_A" type="abit.bean.AbiturientBean" scope="request">
<logic:present name="abit_A" property="familija"> 
  <tr>
      <td align="center">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="number"/></html:link>&nbsp;</td>
	  <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="special1"/>&nbsp;</font></td>
	  <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="gruppa"/>&nbsp;</font></td>
	  <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="nomerLichnogoDela"/>&nbsp;</font></td>
	  <td><font class="text_10">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="familija"/>&nbsp;</font></html:link></td>
	  <td><font class="text_10">&nbsp;<bean:write name="abit_A" property="imja"/>&nbsp;</font></td>
	  <td><font class="text_10">&nbsp;<bean:write name="abit_A" property="otchestvo"/>&nbsp;</font></td>
	  <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_A" property="tipDokSredObraz"/>&nbsp;</font></td>
      <% row=0;%>
          <td align="center"><input type="text" class="text_f9_short" name="<%="cells1%"+row+"%"+abit_A.getKodAbiturienta()+"|"%>" value="<%=abit_A.getPrinjat()%>" maxlength="1" size="1" tabindex="<%=Integer.toString(tabindex++)%>"></td>
      <% row++;%>
<% if(abit_A.getSpecial6() != null && abit_A.getSpecial6().equals("")) { %>
          <td align="center"><input type="text" class="text_f9_short" name="<%="cells1%"+row+"%"+abit_A.getKodAbiturienta()+"|"%>" value="<%=abit_A.getSpecial1()%>" maxlength="4" size="1" tabindex="<%=Integer.toString(tabindex++)%>"></td>
<% } else { %>
          <td align="center"><input type="text" class="text_f9_short" name="<%="cells1%"+row+"%"+abit_A.getKodAbiturienta()+"|"%>" value="<%=abit_A.getSpecial6()%>" maxlength="4" size="1" tabindex="<%=Integer.toString(tabindex++)%>"></td>
<% } %>
 </tr>
</logic:present>
</logic:iterate>

<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="29" align="center"><font class="text_11">
     ��&nbsp;�������&nbsp;��&nbsp;������&nbsp;�����������&nbsp;</font>
  </td>
</tr>
</logic:notPresent>
</table>
<br>
<table border="0" align="center">
<tr align="center">
  <td>&nbsp;<html:submit styleClass="button" value="���������" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/zachislen.do">
  <td>&nbsp;&nbsp;<html:submit styleClass="button" value="����� � ���������" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>

