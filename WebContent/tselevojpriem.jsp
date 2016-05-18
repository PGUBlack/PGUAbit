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

<logic:notPresent name='tselevojPriemAction' scope='request'>
 <logic:redirect forward='tselevojpriem'/>
</logic:notPresent>

<logic:notPresent name="tselevojPriemForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid = " ������������������������������������������������������������������-.,()"
var temp;

for (var i=0; i<document.forms(0).shifrPriema.value.length; i++) 
 {
  temp = "" + document.forms(0).shifrPriema.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("���� ''���� �������� ������'' ����� ��������� ������ ����� �������� ��������");
   document.forms(0).shifrPriema.focus();
   return false;
  }
 }
if(document.forms(0).shifrPriema.value.length == 0) 
 {
  alert("���������� ��������� ���� ''���� �������� ������''");
  document.forms(0).shifrPriema.focus();
  return false;
 }
if(document.forms(0).tselevojPriem.value.length == 0) 
 {
  alert("���������� ��������� ���� ''������� �����''");
  document.forms(0).tselevojPriem.focus();
  return false;
 }
for (var i=0; i<document.forms(0).tselevojPriem.value.length; i++) 
 {
  temp = "" + document.forms(0).tselevojPriem.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("���� ''������� �����'' ����� ��������� ������ ����� �������� ��������");
   document.forms(0).tselevojPriem.focus();
   return false;
  }
 }
}

function gap_me(){
  document.forms(0).shifrPriema.value = " ";
  document.forms(0).tselevojPriem.value = " ";
}

function confirmation(){
  if(confirm('������� ������?'))
   {
    return true;
   }
  else 
       return false; 
}

function exec() {
if(document.forms(0).shifrPriema != null)
  document.forms(0).shifrPriema.focus();
}
</SCRIPT> 

<body onLoad="exec()"></body>

<logic:present name="tselevojPriemForm" property="action">
<bean:define id="action" name="tselevojPriemForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">���������� � ������� ������ � ���</template:put>
<template:put name="sub_name">���������� ������</template:put>
<template:put name='title'>������� �����</template:put>
<template:put name='content'>
<BR>
<html:form action="/tselevojpriem?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">���� �������� ������:</font></td>
      <td><html:text accesskey="�" name="abit_TP" styleClass="text_f10" property="shifrPriema" 
                     maxlength="5" size="5" tabindex="1" value=""/>

  <tr><td><font class="text_10">��������:</font></td>
      <td><html:text accesskey="�" name="abit_TP" styleClass="text_f10" property="tselevojPriem" 
                     maxlength="50" size="50" tabindex="2" value=""/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
  <td><html:reset  styleClass="button" tabindex="4" value="��������"/></td>
</html:form>
<html:form action="/tselevojpriem?action=create">
  <td><html:submit property="full" styleClass="button" tabindex="5" value="�������� �������"/></td>
</html:form>
<html:form method="post" action="/tselevojpriem?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="6" value="�����"/></td>
</html:form>
</tr><tr><td colspan=2 height=155></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">���������� � ������� ������ � ���</template:put>
<template:put name='title'>������� �����</template:put>
<template:put name='content'>
<BR>
<table cols=2 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;��������&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_TP" name="abits_TP" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="tselevojpriem.do?action=mod_del" paramName="abit_TP" 
                   paramId="kodZapisi" paramProperty="kodZapisi" styleClass="link_hov_blue">
                    <bean:write name="abit_TP" property="shifrPriema"/></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_TP" property="tselevojPriem"/></font>&nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_TP" property="shifrPriema">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/tselevojpriem.do?action=new">
  <td align=center valign=center height="40">
    <html:submit styleClass="button" value="��������� �����"/>
  </td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ �����������/�������� ����� ������ � �� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">���������� � ������� ������ � ���</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='title'>������� �����</template:put>
<template:put name='content'>
<BR>
<html:form action="/tselevojpriem?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">���� �������� ������:</font></td>
      <td><html:text accesskey="�" name="abit_TP" styleClass="text_f10" property="shifrPriema" 
                     size="5" maxlength="5" tabindex="1"/>

  <tr><td><font class="text_10">��������:</font></td>
      <td><html:text accesskey="�" name="abit_TP" styleClass="text_f10" property="tselevojPriem" 
                     size="50" maxlength="50" tabindex="2"/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_TP" property="kodZapisi"/>
<html:submit styleClass="button" tabindex="3" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="�������"/>
</td></html:form>
<html:form action="/tselevojpriem?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="��������� �����"/>
</td></html:form>
</tr><tr><td colspan=2 height=155></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>