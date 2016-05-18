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

<logic:notPresent name='goFBSAction' scope='request'>
 <logic:redirect forward='goFBS'/>
</logic:notPresent>

<logic:notPresent name="goFBSForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<% int i=1; %>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid_nom = "0123456789"
var valid_ser = "������������������������������������������������������������������"
var temp;

if(document.forms(0).seriaAtt.value.length == 0){
  alert("���������� ��������� ���� ''����� ���������''");
  document.forms(0).seriaAtt.focus();
  return false;
 }
for (var i=0; i<document.forms(0).seriaAtt.value.length; i++) 
 {
  temp = "" + document.forms(0).seriaAtt.value.substring(i, i+1);
  if (valid_ser.indexOf(temp) == "-1") {
    alert("���� ''����� ���������'' ����� �������� ������ �� ���� �������� ��������");
    document.forms(0).seriaAtt.focus();
    return false;
  }

 }

if(document.forms(0).special1[0].checked) {

// ������ ���� �����

  if(document.forms(0).nomerAtt.value.length == 0){
    alert("���������� ��������� ���� ''����� ���������''");
    document.forms(0).nomerAtt.focus();
    return false;
   }
  for (var i=0; i<document.forms(0).nomerAtt.value.length; i++) 
   {
    temp = "" + document.forms(0).nomerAtt.value.substring(i, i+1);
    if (valid2.indexOf(temp) == "-1")  {
     alert("���� ''����� ���������'' ����� �������� ������ �� �������� ����");
     document.forms(0).nomerAtt.focus();
     return false;
    }
   }
} else {

// ������ ��������� �������

  if(document.forms(0).special2.value.length == 0){
    alert("���������� ������� ��������� ''����� ���������''");
    document.forms(0).special2.focus();
    return false;
   }
  for (var i=0; i<document.forms(0).special2.value.length; i++) 
   {
    temp = "" + document.forms(0).special2.value.substring(i, i+1);
    if (valid_nom.indexOf(temp) == "-1")  {
     alert("���� ''����� ���������'' ����� �������� ������ �� �������� ����");
     document.forms(0).special2.focus();
     return false;
    }
   }
  if(document.forms(0).special3.value.length == 0){
    alert("���������� ������� �������� ''����� ���������''");
    document.forms(0).special3.focus();
    return false;
   }
  for (var i=0; i<document.forms(0).special3.value.length; i++) 
   {
    temp = "" + document.forms(0).special3.value.substring(i, i+1);
    if (valid_nom.indexOf(temp) == "-1")  {
     alert("���� ''����� ���������'' ����� �������� ������ �� �������� ����");
     document.forms(0).special3.focus();
     return false;
    }
   }
  if(document.forms(0).special3.value - document.forms(0).special2.value <0) {
     alert("�������� ''����� ���������'' ������ ���� ������ ����������");
     document.forms(0).special3.focus();
     return false;
  }
 }
}

function gap_me(){
  document.forms(0).seriaAtt.value = " ";
  document.forms(0).nomerAtt.value = " ";
}

function confirmation(){
  if(confirm('������� ������?'))
   {
    return true;
   }
  else 
    return false; 
}

function selector() {
  if(document.forms(0).special1[0].checked) {
     document.forms(0).nomerAtt.disabled = false;
     if(document.forms(0).seriaAtt.value != "")
       document.forms(0).nomerAtt.focus();
     else
       document.forms(0).seriaAtt.focus();
  }
  else {
     document.forms(0).nomerAtt.disabled = true;
     document.forms(0).special2.focus();
  }
}

function exec() {
if(document.forms(0).login != null && document.forms(0).password != null)
   document.forms(0).submit();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="goFBSForm" property="action">
<bean:define id="action" name="goFBSForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� � ���  -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="login">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>��������� ���������� � ����������� ����� ������������ ���</template:put>
<template:put name="target_name">� � � � � � � �</template:put>
<template:put name="sub_name">��� ��������� ���������� � ����������� ����� ������������ ���</template:put>
<template:put name='content'>
<p align="center"><font class="text_th">�����</font></p>
 <form action="http://fbsege.ru/Login.aspx" method="post">
   <span>
     <input type="hidden" name="login" value="user5391"/><br/>
     <input type="hidden" name="password" value="vBGephi"/><br/>
   </span>
 </form>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%-----------------  ��������� ���������� �� ���  -----------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="getInfo">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>��������� ���������� �� ���</template:put>
<template:put name="target_name">��������� ����������</template:put>
<template:put name='content'>
<br>
<p align="center">
<a href="<bean:write name='abit_ba' property='special1'/>" styleClass="link_hov_blue">������� ��� ��������� ����������</a>
</p>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ �����������/�������� ����� ������ � �� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>���������������� ��������� �� ������ �������������</template:put>
<template:put name="target_name">���������������� ���������</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='content'>
<br>
<html:form action="/goFBS?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">����� ���������:</font></td>
      <td><html:text accesskey="�" name="abit_ba" styleClass="text_f10_short" property="seriaAtt" 
                     maxlength="5" size="4" tabindex="1"/>

  <tr><td><font class="text_10">����� ���������:</font></td>
      <td><html:text accesskey="�" name="abit_ba" styleClass="text_f10_short" property="nomerAtt"
                     maxlength="12" size="8" tabindex="2"/>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_ba" property="kodZapisi"/>
<html:submit styleClass="button" tabindex="3" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="�������"/>
</td></html:form>
<html:form action="/goFBS?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="��������� �����"/>
</td></html:form>
</table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%------------------- ���������� �������� ���. ghj,f --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="results">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>���������������� ��������� �� ������ �������������</template:put>
<template:put name="target_name">������ ���������������� ����������</template:put>
<template:put name='content'>
<table align=center border=1 cellSpacing=0>
<thead>
<tr valign="middle">
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;���&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;������� �.�.&nbsp;</font></td>
    <td colspan=2 align=center height=30><font class="text_th">&nbsp;��������&nbsp;</font></td>
</tr>
<tr>
    <td align=center height=30><font class="text_th">&nbsp;�����&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;�����&nbsp;</font></td></tr>
</tr>
</thead>
<logic:iterate id="abit_ba" name="abits_ba" scope='request'>
<tr>
  <td align=center valign=center height=30>&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_ba" paramId="kodAbiturienta"
                                                  paramProperty="kodAbiturienta" styleClass="link_hov_blue">
    <font class="text_10"><bean:write name="abit_ba" property="nomerLichnogoDela"/></html:link>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="familija"/></font>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="seriaAtt"/></font>&nbsp;</td>
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_ba" property="nomerAtt"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_ba" property="kodAbiturienta">
<tr>
  <td align=center valign=center colspan=10>
     <font class="text_11">&nbsp;&nbsp;�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;����������������&nbsp;����������&nbsp;&nbsp;</font></td>
</logic:notPresent>
</table>

<table align=center border=0>
<tr>
<html:form method="post" action="/goFBS?action=null">
  <td height=40><html:submit property="exit" styleClass="button" value="�����"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>