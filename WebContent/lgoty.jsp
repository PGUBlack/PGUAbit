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

<logic:notPresent name='lgotyAction' scope='request'>
 <logic:redirect forward='lgoty'/>
</logic:notPresent>

<logic:notPresent name="lgotyForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid1 = " �������������������������������"
var valid2 = " ������������������������������������������������������������������,.-1234567890()\""
var temp;

if(document.forms(0).shifrLgot.value.length == 0){
  alert("���������� ��������� ���� ''���� �����''");
  document.forms(0).shifrLgot.focus();
  return false;
 }
for (var i=0; i<document.forms(0).shifrLgot.value.length; i++) 
 {
  temp = "" + document.forms(0).shifrLgot.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
    alert("���� ''���� �����'' ����� ��������� ������ �������� ����� �������� ��������");
    document.forms(0).shifrLgot.focus();
    return false;
  }

 }
if(document.forms(0).lgoty.value.length == 0){
  alert("���������� ��������� ���� ''������������ ������''");
  document.forms(0).lgoty.focus();
  return false;
 }

for (var i=0; i<document.forms(0).lgoty.value.length; i++) 
 {
  temp = "" + document.forms(0).lgoty.value.substring(i, i+1);
  if (valid2.indexOf(temp) == "-1")  {
   alert("���� ''������������ ������'' ����� ��������� ������ ����� �������� �������� � �����");
   document.forms(0).lgoty.focus();
   return false;
  }
 }
}

function gap_me(){
  document.forms(0).shifrLgot.value = " ";
  document.forms(0).lgoty.value = " ";
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
if(document.forms(0).shifrLgot != null)
  document.forms(0).shifrLgot.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="lgotyForm" property="action">
<bean:define id="action" name="lgotyForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>������ ����</template:put>
<template:put name="target_name">���������� � ������� ����</template:put>
<template:put name="sub_name">���������� ������</template:put>
<template:put name='content'>
<html:form method="post" action="/lgoty?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">���� �����:</font></td>
      <td><html:text accesskey="�" name="abit_Lgoty" styleClass="text_f10" property="shifrLgot" 
                     maxlength="1" size="1" tabindex="1" value=""/>

  <tr><td><font class="text_10">������������ ������:</font></td>
      <td><html:text accesskey="�" name="abit_Lgoty" styleClass="text_f10" property="lgoty"
                     maxlength="150" size="50" tabindex="2" value=""/>
<tr><td height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
</html:form>
<html:form method="post" action="/lgoty?action=create">
  <td><html:submit property="full" styleClass="button" tabindex="4" value="�������� �������"/></td>
</html:form>
<html:form action="/medali?action=new">
  <td><html:submit styleClass="button" tabindex="5" value="�������"/></td>
</html:form>
<html:form method="post" action="/lgoty?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="6" value="�����"/></td>
</html:form>
</tr><tr><td height=165></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>������ ����</template:put>
<template:put name="target_name">���������� � ������� ����</template:put>
<template:put name='content'>
<br>
<table cols=2 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center height=30><font class="text_th">&nbsp;���� &nbsp;������&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;������������ ������&nbsp;</font></td></tr>
</thead>
<logic:iterate id="abit_Lgoty" name="abits_Lgoty" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="lgoty.do?action=mod_del" paramName="abit_Lgoty" paramId="kodLgot"                                                  paramProperty="kodLgot" styleClass="link_hov_blue">
    <bean:write name="abit_Lgoty" property="shifrLgot"/></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Lgoty" property="lgoty"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_Lgoty" property="kodLgot">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/lgoty.do?action=new">
  <td align=center vAlign=middle height=35>
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
<template:put name='title'>������ ����</template:put>
<template:put name="target_name">���������� � ������� ����</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='content'>
<br>
<html:form action="/lgoty?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">���� �����:</font></td>
      <td><html:text accesskey="�" name="abit_Lgoty" styleClass="text_f10" property="shifrLgot" 
                     maxlength="1" size="1" tabindex="1"/>

  <tr><td><font class="text_10">������������ ������:</font></td>
      <td><html:text accesskey="�" name="abit_Lgoty" styleClass="text_f10" property="lgoty"
                     maxlength="150" size="50" tabindex="2"/>
<tr><td height=6></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_Lgoty" property="kodLgot"/>
<html:submit styleClass="button" tabindex="3" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="�������"/>
</td></html:form>
<html:form action="/lgoty?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="��������� �����"/>
</td></html:form>
</tr></tr><tr><td height=165></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>