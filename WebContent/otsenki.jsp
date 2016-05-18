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
<%---------------- ������� �� ����� ��� ���������� ----------------%>
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
  // ���������� ����� ����� 
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
  // ���������� ����� "-"
    document.forms(0).kodGruppy.options[0] = new Option("-")
    document.forms(0).kodGruppy.options[0].value = "-"
    document.forms(0).kodGruppy.options[0].selected = true
  }
  // ������� � ������ ������
  document.forms(0).kodGruppy.options[0].selected = true
}

function checkFields(){
 if(document.forms(0).kodFakulteta.value == "-"){
   alert("���������� ������� ���������");
   document.forms(0).kodFakulteta.focus();
   return false;
 }

if(document.forms(0).kodPredmeta.value != -1)
 if(document.forms(0).dataJekzamena.value == "00-00-0000"){
   alert("���������� ������� ���� ���������� �������� �� ���������� ��������");
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
<%-----------------------  ��������� ����  ------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="menu">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="title">��������������� ������ (���������� ������� ����)</template:put>
<template:put name="target_name">��������������� ������ (���������� �������� ����)</template:put>
<template:put name="sub_name">��� ���������� ��� ��� ����������</template:put>
<template:put name="content">
<br>
<table border="0" cellspacing="0" cellpadding="0" align="center">
<html:form action="/otsenki?action=show" onsubmit="return checkFields();">
<%--------------------- ������ �1 ������� -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;���������:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select onchange="fillSelect(this.value);" styleClass="select_f1" 
                               name="abit_A" property="kodFakulteta" tabindex="1">
    <html:option value="-"/>
    <html:options collection="abit_A_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta" />
    </html:select>
</td>
</tr>

<%--------------------- ������ �2 ������� -------------------------%>
<tr>
<td align=right><font class="text_9">&nbsp;������:&nbsp;&nbsp;</font></td>
<td align=center id="gruppa" style="display:none" height=27>
    <html:select styleClass="select_f1" name="abit_A" property="kodGruppy" tabindex="2">
    <html:option value="-"/>
    <html:options collection="abit_A_S2" property="kodGruppy" labelProperty="special2" />
    </html:select>
</td>
</tr>
<%--------------------- ������ �3 ������� -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;������:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select styleClass="select_f1" name="abit_A" property="special8" tabindex="3">
    <html:option value="needed">�� ���.</html:option>
    <html:option value="all">���</html:option>
    </html:select>
</td>
</tr>
<%--------------------- ������ �4 ������� -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;�������:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select styleClass="select_f1" name="abit_A" property="kodPredmeta" tabindex="4">
    <html:option value="-1">&nbsp;&nbsp;&nbsp;*</html:option>
    <html:options collection="predmets" property="kodPredmeta" labelProperty="predmet" />
    </html:select>
</td>
</tr>
<%--------------------- ������ �5 ������� -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;���� ��������:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:text accesskey="�" styleClass="text_f11_short" name="abit_A" size='10' maxlength='10' property="dataJekzamena" value="00-00-0000" tabindex="5"/>
</td>
</tr>
<%--------------------- ������ �6 ������� -------------------------%>
<tr valign="middle">
<td align=right><font class="text_9">&nbsp;��������� ��������:&nbsp;&nbsp;</font></td>
<td align=center height=27>
    <html:select styleClass="select_f1" name="abit_A" property="dokumentyHranjatsja" tabindex="6">
    <html:option value="�"/>
    <html:option value="�"/>
    <html:option value="*"/>
    </html:select>
</td>
</tr>
<%-----------------------------------------------------------------%>
</table>
<br>
<table border="0" align="center">
<tr align="center">
  <td><html:submit styleClass="button" value="�������������" tabindex="7"/></td>
</html:form>
<html:form action="/abiturient.do">
  <td><html:submit styleClass="button" value="�����" tabindex="8" property="exit"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%---------- ������������� �� ���� ���������� ������� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="title">������������� ��������������� ������</template:put>
<template:put name="target_name">������������� ��������������� ������</template:put>
<template:put name="sub_name">��� ���������� ����������</template:put>
<template:put name="content">
<div align=center><font class="text_10">������������&nbsp;�&nbsp;������:&nbsp;
  <bean:write name="abit_A" property="special3"/>&nbsp;&nbsp;&nbsp; ��&nbsp;����������:&nbsp;
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
  <td rowspan=2 valign=center><font class="text_th">&nbsp;�&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;���. ��.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;����.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;����� ����&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;�������&nbsp;</font></td>
  <td colspan=20 align=center valign=center><font class="text_th">&nbsp;��������&nbsp;</font></td>
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
      <td align="center"><html:text accesskey='�' name="abit_A" styleClass="text_f9_short" property="<%=\"otsn\"+abit_A.getKodPredmeta()+\"%\"+abit_A.getKodAbiturienta()%>" value="<%=abit_A.getSpecial1()%>" maxlength="3" size="2" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</logic:present>
</logic:iterate>
<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="20" align="center">
     <font class="text_11">&nbsp;��&nbsp;���������&nbsp;����������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������&nbsp;</font>
  </td>
</tr>
</table>
</logic:notPresent>
<table border="0" align="center" valign="center">
<tr align="center">
  <td height=30><html:submit styleClass="button" value="���������" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/otsenki.do">
  <td>&nbsp;<html:submit styleClass="button" value="����� � ����" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%----------- ������������� �� ������� ���������� -----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full3">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="title"> ������������� ��������������� ������</template:put>
<template:put name="target_name">������������� ��������������� ������</template:put>
<template:put name="sub_name">��� ���������� ����������</template:put>
<template:put name="content">
<div align=center><font class="text_10">������������&nbsp;�&nbsp;������:&nbsp;
  <bean:write name="abit_A" property="special3"/>&nbsp;&nbsp;&nbsp; ��&nbsp;����������:&nbsp;
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
  <td rowspan=2 valign=center><font class="text_th">&nbsp;�&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;���. ��.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;����.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;����� ����&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;�������&nbsp;</font></td>
  <td colspan=20 align=center valign=center><font class="text_th">&nbsp;��������&nbsp;</font></td>
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
      <td align="center"><html:text accesskey='�' name="abit_A" styleClass="text_f9_short" property="<%=\"otsn\"+abit_A.getKodPredmeta()+\"%\"+abit_A.getKodAbiturienta()%>" value="<%=abit_A.getSpecial1()%>" maxlength="3" size="2" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</logic:present>
</logic:iterate>
<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="20" align="center">
     <font class="text_11">&nbsp;��&nbsp;���������&nbsp;����������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������&nbsp;</font>
  </td>
</tr>
</table>
</logic:notPresent>
<table border="0" align="center" valign="center">
<tr align="center">
  <td height=30><html:submit styleClass="button" value="���������" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/otsenki.do">
  <td>&nbsp;<html:submit styleClass="button" value="����� � ����" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%----------- ������������� �� ���������� �������� ----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full1">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="title">������������� ��������������� ������</template:put>
<template:put name="target_name">������������� ��������������� ������</template:put>
<template:put name="sub_name">��� ���������� ����������</template:put>
<template:put name="content">
<div align=center><font class="text_10">������������&nbsp;�&nbsp;������:&nbsp;
  <bean:write name="abit_A" property="special3"/>&nbsp;&nbsp;&nbsp; ��&nbsp;����������:&nbsp;
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
  <td rowspan=2 valign=center><font class="text_th">&nbsp;�&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;���. ��.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;����.&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;����� ����&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td rowspan=2 valign=center><font class="text_th">&nbsp;�������&nbsp;</font></td>
  <td align=center valign=center><font class="text_th">&nbsp;�������&nbsp;</font></td>
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
      <td align="center"><html:text accesskey='�' name="abit_A" styleClass="text_f9_short" property="<%=\"otsn\"+abit_A.getKodPredmeta()+\"%\"+abit_A.getKodAbiturienta()%>" value="<%=abit_A.getSpecial1()%>" maxlength="3" size="2" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</logic:present>
</logic:iterate>
<logic:notPresent name="abit_A" property="familija">
<tr>
  <td colspan="20" align="center">
     <font class="text_11">&nbsp;��&nbsp;���������&nbsp;����������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������&nbsp;</font>
  </td>
</tr>
</table>
</logic:notPresent>
<table border="0" align="center" valign="center">
<tr align="center">
  <td height=30><html:submit styleClass="button" value="���������" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
<html:form action="/otsenki.do">
  <td>&nbsp;<html:submit styleClass="button" value="����� � ����" tabindex="<%=Integer.toString(tabindex++)%>"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>