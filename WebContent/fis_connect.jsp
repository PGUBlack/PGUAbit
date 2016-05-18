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

<logic:notPresent name='fisConnectAction' scope='request'>
 <logic:redirect forward='fis_connect'/>
</logic:notPresent>

<logic:notPresent name="fisConnectForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
  var valid = " ������������������������������������������������������������������-()."
  var temp;
}

function gap_me(){
  document.forms(0).fakultet.value = " ";
  document.forms(0).abbreviaturaFakulteta.value = " ";
  document.forms(0).shifrFakulteta.value = " ";
  document.forms(0).nazvanieVRoditelnom.value = " ";
  document.forms(0).dekan.value = " ";
}

function confirmation(){
  if(confirm('������� ��������� � ��� ��� � ��� �������?'))
   {
     return true;
   }
  else return false; 
}

function exec() {
if(document.forms(0).fakultet != null) {
document.forms(0).fakultet.focus();
}
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="fisConnectForm" property="action">
<bean:define id="action" name="fisConnectForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>�������� ������� � ���</template:put>
<template:put name="target_name">�������� ������� � ���</template:put>
<template:put name="sub_name">��������� �������</template:put>
<template:put name='content'>
<logic:present name="mess" property="message">
<p align="center">
<font class="text_th"><bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/></font>
</p>
</logic:present>
<html:form action="fis_connect?action=toFIS" method="post" enctype="multipart/form-data" onsubmit="return checkFields();">
<table align=center border=0>

  <tr><td><font class="text_10">�������&nbsp;�����:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="baseAddr" 
                     maxlength="110" size="50" tabindex="1" value="http://priem.edu.ru:8000/import/importservice.svc"/></td></tr>

  <tr><td><font class="text_10">�����:</font></td>
      <td><html:select name="abit_F" styleClass="select_f1" property="method" tabindex="2">
            <html:option value="/institutioninfo">/institutioninfo (��������� �������� �� ��)</html:option>
            <html:option value="/checkapplication">checkapplication (�������� ���������)</html:option>
            <html:option value="/delete">/delete (��������)</html:option>
            <html:option value="/delete/result">/delete/result (��������� ���������� ��������)</html:option>
            <html:option value="/dictionary">/dictionary (������ ������������)</html:option>
            <html:option value="/dictionarydetails">/dictionarydetails (������ �� �����������)</html:option>
            <html:option value="/import">/import (������)</html:option>
            <html:option value="/import/result">/import/result (��������� ���������� �������)</html:option>
            <html:option value="/validate">/validate (���������)</html:option>
            <html:option value="/test/checkapplication">/test/checkapplication (���� �������� ���������)</html:option>
            <html:option value="/test/delete">/test/delete (���� ��������)</html:option>
            <html:option value="/test/dictionary">/test/dictionary (���� �����������)</html:option>
            <html:option value="/test/dictionarydetails">/test/dictionarydetails (���� ������� �����������)</html:option>
            <html:option value="/test/import">/test/import (���� �������)</html:option>
          </html:select>
      </td>

  <tr><td><font class="text_10">��� ����������:</font></td>
      <td><html:select name="abit_F" styleClass="select_f1" property="resultPath" tabindex="3">
            <html:option value="forView">��� ��������� �� ������</html:option>
            <html:option value="forImport">��� ������� � ��</html:option>
          </html:select>
      </td>

  <tr><td><font class="text_10">��� (��������) �������� �������:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="codeX" 
                     maxlength="150" size="20" tabindex="4" value="1"/></td></tr>

  <tr><td><font class="text_10">�������&nbsp;����������:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="timeOut" 
                     maxlength="5" size="3" tabindex="5" value="30"/></td></tr>

  <tr><td><font class="text_10">�����:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="user" 
                     maxlength="35" size="25" tabindex="6" value="polex@pnzgu.ru"/></td></tr>

  <tr><td><font class="text_10">������:</font></td>
      <td><html:password name="abit_F" styleClass="text_f10" property="password" 
                     maxlength="35" size="25" tabindex="7" value="polex@pnzgu.ru"/></td></tr>

  <tr><td><font class="text_10">���� ������� (XML):</font></td>
      <td><html:file name="abit_F" styleClass="select_f2" size='60' property="sourceFile" tabindex="8"/></td></tr>

<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="9" value="���������"/></td>
</html:form>
<html:form action="/fis_connect?action=null">
  <td><html:submit styleClass="button" tabindex="10" onclick="gap_me();" property="exit" value="�����"/></td>
</html:form>
</tr><tr><td colspan=2 height=50></td></tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="ansFIS">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>����� �� ������ �� ���</template:put>
<template:put name="target_name">����� �� ������ �� ���</template:put>
<template:put name='content'>
<bean:define id="FISans" name="abit_F" property="reqXML"/>
<logic:present name="mess" property="message">
<p align="center">
<font class="text_th"><bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/></font>
</p>
</logic:present>
<p align='center'>
<html:textarea property="reqXML" value="<%=\"\"+FISans%>" name="input" styleClass="text_f9" tabindex="6" cols="150" rows="35"/>
</p>

<table border='0'>
<tr>
<td colspan=10>
<table valign=center align=middle cellSpacing=6 border=0>
<tr>
<td valign=center align=middle></td>
<html:form action="/fis_connect.do?action=new">
  <td valign=center align=middle><html:submit styleClass="button" value="��������� �����"/></td>
</html:form>
  <td valign=center align=middle></td>
</tr></table>
</td></tr>
</table>
<br><BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ �����������/�������� ����� ������ � �� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>����������</template:put>
<template:put name="target_name">��������� ����</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='content'>

<html:form action="/fis_connect?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">���������:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="fakultet" 
                     size="50" maxlength="100" tabindex="1"/></td></tr>

  <tr><td><font class="text_10">������������:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="abbreviaturaFakulteta" 
                     size="5" maxlength="5" tabindex="2"/></td></tr>

  <tr><td><font class="text_10">���� ������:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="planPriemaFakulteta" 
                     size="10" maxlength="10" tabindex="3"/></td></tr>

  <tr><td><font class="text_10">����:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="shifrFakulteta" 
                     size="1" maxlength="1" tabindex="4"/></td></tr>

  <tr><td><font class="text_10">��������� (�����������):</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="nazvanieVRoditelnom" 
                     size="50" maxlength="100" tabindex="5"/></td></tr>

  <tr><td><font class="text_10">������������� ����:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="poluProhodnoiBallFakulteta" 
                     size="2" maxlength="2" tabindex="6"/></td></tr>

  <tr><td><font class="text_10">��������� ����:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="prohodnoiBallFakulteta" 
                     size="2" maxlength="2" tabindex="7"/></td></tr>

  <tr><td><font class="text_10">�����:</font></td>
      <td><html:text name="abit_F" styleClass="text_f10" property="dekan" 
                     size="50" maxlength="50" tabindex="8"/></td></tr>
<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_F" property="kodFakulteta"/>
<html:submit styleClass="button" tabindex="9" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="10" value="�������"/>
</td>
</html:form>
<html:form action="/fis_connect?action=full">
<td valign=center align=center>
<html:submit styleClass="button" tabindex="11" value="��������� �����"/>
</td></html:form>
</tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------- ����� ��������� ���������� ��������� --------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="adjustment">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>����������</template:put>
<template:put name="target_name">���������� ����</template:put>
<template:put name="sub_name">��������� ���������� ���������</template:put>
<template:put name='content'>
<html:form action="/fis_connect?action=create" scope="request">
<table cols=5 align=center border=0>
<tr>
    <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
    <th align=center valign=center><font class="text_10">������� ���������� ������:</font></th>
    <td align=left valign=center>
           <html:select name="abit_F" styleClass="select_f1" property="priznakSortirovki" tabindex="2">
           <html:option value="�� �����������"/>
           <html:option value="�� ��������"/>
           </html:select></td>
</tr>
<tr><td><BR></td></tr>
<tr>
    <th align=center valign=center colspan=10><font class="text_10">����������� �� �������:</font></th>
</tr>
</table>
<table cols=8 align=center border=1 cellSpacing=0>
<tr><td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;���������&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;����.&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;���� &nbsp;������&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30>
     <font class="text_th">&nbsp;��������� (�����������)&nbsp;</font></td>
    <td rowspan=2 align=center valign=center height=30>
       <font class="text_th">&nbsp;�����&nbsp;</font></td>
    <td colspan=2 align=center valign=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td></tr><tr>
    <td align=center valign=center height=30><font class="text_th">&nbsp;�/��.&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;����.&nbsp;</font></td>
</tr><tr>
    <td border=1 align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="1" tabindex="3" /></td>
    <td align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="2" tabindex="4" /></td>
    <td align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="3" tabindex="5" /></td>
    <td align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="4" tabindex="6" /></td>
    <td align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="5" tabindex="7" /></td>
    <td align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="6" tabindex="8" /></td>
    <td align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="7" tabindex="9" /></td>
    <td align=center valign=center>
      <html:radio name="abit_F" property="stolbetsSortirovki" value="8" tabindex="10" /></td>
</tr>
</table>
<table align=center border=0>
<tr height=10><td></td></tr>
<tr align=center>
  <td><html:submit property="full" styleClass="button" tabindex="11" value="�������� �������"/></td>
</html:form></tr>
<tr><td height=55></td></tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>