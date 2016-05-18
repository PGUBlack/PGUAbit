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

<logic:notPresent name='medaliAction' scope='request'>
 <logic:redirect forward='medali'/>
</logic:notPresent>

<logic:notPresent name="medaliForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid1 = " ���������������������������������"
var valid2 = " ������������������������������������������������������������������-.,()"
var temp;

if(document.forms(0).shifrMedali.value.length == 0) 
 {
  alert("���������� ��������� ���� ''���� ������''");
  document.forms(0).shifrMedali.focus();
  return false;
 }
for (var i=0; i<document.forms(0).shifrMedali.value.length; i++) 
 {
  temp = "" + document.forms(0).shifrMedali.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
   alert("���� ''���� ������'' ����� ��������� ������ �������� ����� �������� ��������");
   document.forms(0).shifrMedali.focus();
   return false;
  }
 }
if(document.forms(0).medal.value.length == 0) 
 {
  alert("���������� ��������� ���� ''������''");
  document.forms(0).medal.focus();
  return false;
 }
for (var i=0; i<document.forms(0).medal.value.length; i++) 
 {
  temp = "" + document.forms(0).medal.value.substring(i, i+1);
  if (valid2.indexOf(temp) == "-1") {
   alert("���� ''������'' ����� ��������� ������ ����� �������� ��������");
   document.forms(0).medal.focus();
   return false;
  }
 }
}

function gap_me(){
  document.forms(0).shifrMedali.value = " ";
  document.forms(0).medal.value = " ";
}

function confirmation(){
  if(confirm('������� ������?')) return true;
   else return false; 
}

function exec() {
if(document.forms(0).shifrMedali != null)
document.forms(0).shifrMedali.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="medaliForm" property="action">
<bean:define id="action" name="medaliForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">���������� �� ��������</template:put>
<template:put name="sub_name">���������� ������</template:put>
<template:put name='title'>������</template:put>
<template:put name='content'>
<BR>
<html:form method="post" action="/medali?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">���� �������:</font></td>
      <td><html:text accesskey="�" name="abit_Medali" styleClass="text_f10" property="shifrMedali" 
                     maxlength="1" size="1" tabindex="1" value=""/>

  <tr><td><font class="text_10">��������:</font></td>
      <td><html:text accesskey="�" name="abit_Medali" styleClass="text_f10" property="medal"
                     maxlength="150" size="55" tabindex="2" value=""/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
  <td><html:reset  styleClass="button" tabindex="4" value="��������"/></td>
</html:form>
<html:form method="post" action="/medali?action=create">
  <td><html:submit property="full" styleClass="button" tabindex="5" value="�������� �������"/></td>
</html:form>
<html:form action="/tselevojpriem?action=new">
  <td><html:submit styleClass="button" tabindex="6" value="������� �����"/></td>
</html:form>
<html:form action="/medali?action=null">
  <td><html:submit styleClass="button" tabindex="7" onclick="gap_me();" property="exit" value="�����"/></td>
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
<template:put name="target_name">���������� �� ��������</template:put>
<template:put name='title'>�������</template:put>
<template:put name='content'>
<BR>
<table cols=3 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center height=30><font class="text_th">&nbsp;����&nbsp;�������&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;��������&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_Medali" name="abits_Medali" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="medali.do?action=mod_del" paramName="abit_Medali" paramId="kodMedali" paramProperty="kodMedali" styleClass="link_hov_blue">
                    <bean:write name="abit_Medali" property="shifrMedali"/></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Medali" property="medal"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_Medali" property="kodMedali">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/medali.do?action=new">
  <td align=center valign=center height="40">
    <html:submit styleClass="button" value="��������� �����"/>
  </td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ �����������/�������� ����� ������ � �� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">���������� �� ��������</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='title'>������</template:put>
<template:put name='content'>
<BR>
<html:form action="/medali?action=change" onsubmit="return checkFields();">

<table cols=2 align=center border=0>

  <tr><td><font class="text_10">���� �������:</font></td>
      <td><html:text accesskey="�" name="abit_Medali" styleClass="text_f10" property="shifrMedali" 
                     maxlength="1" size="1" tabindex="1"/>

  <tr><td><font class="text_10">��������:</font></td>
      <td><html:text accesskey="�" name="abit_Medali" styleClass="text_f10" property="medal"
                     maxlength="150" size="55" tabindex="2"/>
<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_Medali" property="kodMedali"/>
<html:submit styleClass="button" tabindex="3" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="�������"/>
</td></html:form>
<html:form action="/medali?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="��������� �����"/>
</td></html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>