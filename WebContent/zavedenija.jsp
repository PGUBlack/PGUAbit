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

<logic:notPresent name='zavedenijaAction' scope='request'>
 <logic:redirect forward='zavedenija'/>
</logic:notPresent>

<logic:notPresent name="zavedenijaForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! int number; %>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
var valid1 = " ������������������������������������������������������������������.,1234567890�\""
var temp;

if(document.forms(0).polnoeNaimenovanieZavedenija.value.length == 0) 
 {
  alert("���������� ��������� ���� ''������ �������� ���������''");
  document.forms(0).polnoeNaimenovanieZavedenija.focus();
  return false;
 }
for (var i=0; i<document.forms(0).polnoeNaimenovanieZavedenija.value.length; i++) 
 {
  temp = "" + document.forms(0).polnoeNaimenovanieZavedenija.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
    alert("���� ''������ �������� ���������'' ����� ��������� ������ ����� �������� �������� � �����");
    document.forms(0).polnoeNaimenovanieZavedenija.focus();
    return false;
  }
 }
if(document.forms(0).sokr.value.length == 0) 
 {
  alert("���������� ��������� ���� ''����������''");
  document.forms(0).sokr.focus();
  return false;
 }
for (var i=0; i<document.forms(0).sokr.value.length; i++) 
 {
  temp = "" + document.forms(0).sokr.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
    alert("���� ''����������'' ����� ��������� ������ ����� �������� �������� � �����");
    document.forms(0).sokr.focus();
    return false;
  }
 }
}
function gap_me(){
  document.forms(0).polnoeNaimenovanieZavedenija.value = " ";
  document.forms(0).sokr.value = " ";
}

function confirmation(){
  if(confirm('������� �������� ���������?'))
   {
    return true;
   }
  else 
       return false; 
}

function invokeAct(){
 document.forms(0).submit();
}

function exec() {
 if(document.forms(0).polnoeNaimenovanieZavedenija != null)
   document.forms(0).polnoeNaimenovanieZavedenija.focus();
}

function help_me(){
 alert("��������!\n�������� ��������� �� �������� � �������� ���������� \n�� ������������, ��������� � ���.");
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="zavedenijaForm" property="action">
<bean:define id="action" name="zavedenijaForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">������� ���������</template:put>
<template:put name="sub_name">���������� ������</template:put>
<template:put name='title'>������� ���������</template:put>
<template:put name='content'>
<BR>
<html:form action="/zavedenija?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
 
  <tr><td><font class="text_10">������ �������� �����.:</font></td>
      <td><html:text accesskey="�" name="abit_Z" styleClass="text_f10" property="polnoeNaimenovanieZavedenija" maxlength="50" size="50" tabindex="1" value=""/>
      <td width=30>
  <tr><td><font class="text_10">����������:</font></td>
      <td><html:text accesskey="�" name="abit_Z" styleClass="text_f10" property="sokr"
                     maxlength="27" size="27" tabindex="2" value=""/>

<tr><td colspan=3 height=8></td>
<tr><td colspan=2 valign=center height=40>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
  <td><html:reset  styleClass="button" tabindex="4" value="��������"/></td>
  <td><html:submit property="full" onclick="return gap_me();" styleClass="button" tabindex="5" value="�������� �������"/></td>
</html:form>
<html:form action="/zavedenija.do">
  <td><html:button styleClass="button" onclick="help_me();" property="hlp" tabindex="6" value="�������"/></td>
  <td><html:submit styleClass="button" onclick="return gap_me();" property="exit" tabindex="7" value="�����"/></td>
</html:form>
</tr>
</table></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">������� ���������</template:put>
<template:put name='title'>������� ���������</template:put>
<template:put name='content'>
<BR>
<table cols=3 align=center border=1 cellSpacing=0>
<tr><td align=center valign=center height=30><font class="text_th">�</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;������&nbsp;��������&nbsp;���������&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;����������&nbsp;</font></td></tr>
<% number = 0; %>
<logic:iterate id="abit_Z" name="abits_Z" scope='request'>
<tr>
  <td valign=center align=center>&nbsp;<html:link href="zavedenija.do?action=mod_del" paramName="abit_Z"       styleClass="link_hov_blue" paramId="kodZavedenija" paramProperty="kodZavedenija">
  <%=++number%>
  </html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Z" property="polnoeNaimenovanieZavedenija"/></font>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Z" property="sokr"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_Z" property="kodZavedenija">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/zavedenija.do?action=new">
  <td align=center>
    <html:submit styleClass="button_sd" value="��������� �����"/>
  </td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ �����������/�������� ����� ������ � �� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">������� ���������</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='title'>������� ���������</template:put>
<template:put name='content'>
<BR>
<html:form action="/zavedenija?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">������ �������� ���������:</font></td>
      <td><html:text accesskey="�" name="abit_Z" styleClass="text_f10" property="polnoeNaimenovanieZavedenija" 
                     size="50" maxlength="50" tabindex="1"/>

  <tr><td><font class="text_10">����������:</font></td>
      <td><html:text accesskey="�" name="abit_Z" styleClass="text_f10" property="sokr" 
                     size="27" maxlength="27" tabindex="2"/>
</table>
<table align=center border=0>
<tr align=center>
 <td><html:hidden name="abit_Z" property="kodZavedenija"/></td>
 <td><html:submit styleClass="button" tabindex="3" value="��������" property="mod_del"/></td>
 <td><html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="�������"/>
</td></html:form>
<html:form action="/zavedenija?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="��������� �����"/>
</td></html:form></tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>