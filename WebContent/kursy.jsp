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

<logic:notPresent name='kursyAction' scope='request'>
 <logic:redirect forward='kursy'/>
</logic:notPresent>

<logic:notPresent name="kursyForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid1 = " �������������������������������"
var valid2 = " ������������������������������������������������������������������,.-1234567890()\""
var temp;

if(document.forms(0).shifrKursov.value.length == 0){
  alert("���������� ��������� ���� ''���� ������''");
  document.forms(0).shifrKursov.focus();
  return false;
 }
for (var i=0; i<document.forms(0).shifrKursov.value.length; i++) 
 {
  temp = "" + document.forms(0).shifrKursov.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
    alert("���� ''���� ������'' ����� ��������� ������ �������� ����� �������� ��������");
    document.forms(0).shifrKursov.focus();
    return false;
  }

 }
if(document.forms(0).informatsijaOKursah.value.length == 0){
  alert("���������� ��������� ���� ''���������� � ������''");
  document.forms(0).informatsijaOKursah.focus();
  return false;
 }

for (var i=0; i<document.forms(0).informatsijaOKursah.value.length; i++) 
 {
  temp = "" + document.forms(0).informatsijaOKursah.value.substring(i, i+1);
  if (valid2.indexOf(temp) == "-1")  {
   alert("���� ''���������� �������'' ����� ��������� ������ ����� �������� �������� � �����");
   document.forms(0).informatsijaOKursah.focus();
   return false;
  }
 }
}

function gap_me(){
  document.forms(0).shifrKursov.value = " ";
  document.forms(0).informatsijaOKursah.value = " ";
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
if(document.forms(0).shifrKursov != null)
  document.forms(0).shifrKursov.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="kursyForm" property="action">
<bean:define id="action" name="kursyForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>����� ����</template:put>
<template:put name="target_name">���������� � ������ ����</template:put>
<template:put name="sub_name">���������� ������</template:put>
<template:put name='content'>
<html:form method="post" action="/kursy?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">���� ������:</font></td>
      <td><html:text accesskey="�" name="abit_Kursy" styleClass="text_f10" property="shifrKursov" 
                     maxlength="1" size="1" tabindex="1" value=""/>

  <tr><td><font class="text_10">��������:</font></td>
      <td><html:text accesskey="�" name="abit_Kursy" styleClass="text_f10" property="informatsijaOKursah"
                     maxlength="150" size="50" tabindex="2" value=""/>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
</html:form>
<html:form method="post" action="/kursy?action=create">
  <td><html:submit property="full" styleClass="button" tabindex="4" value="�������� �������"/></td>
</html:form>
<html:form action="/lgoty?action=new">
  <td><html:submit styleClass="button" tabindex="5" value="������"/></td>
</html:form>
<html:form method="post" action="/kursy?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="6" value="�����"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>����� ����</template:put>
<template:put name="target_name">���������� � ������ ����</template:put>
<template:put name='content'>
<br>
<table cols=2 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center height=30><font class="text_th">&nbsp;���� &nbsp;������&nbsp;</font></td>
    <td align=center valign=center height=30><font class="text_th">&nbsp;��������&nbsp;</font></td></tr>
</thead>
<logic:iterate id="abit_Kursy" name="abits_Kursy" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="kursy.do?action=mod_del" paramName="abit_Kursy" paramId="kodKursov"                                                  paramProperty="kodKursov" styleClass="link_hov_blue">
    <bean:write name="abit_Kursy" property="shifrKursov"/></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Kursy" property="informatsijaOKursah"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_Kursy" property="kodKursov">
<tr>
  <td align=center valign=center colspan=3>
     <font class="text_11">�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>

<table align=center border=0>
<tr>
<html:form action="/kursy.do?action=new">
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
<template:put name='title'>����� ����</template:put>
<template:put name="target_name">���������� � ������ ����</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='content'>
<br>
<html:form action="/kursy?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>
  <tr><td><font class="text_10">���� ������:</font></td>
      <td><html:text accesskey="�" name="abit_Kursy" styleClass="text_f10" property="shifrKursov" 
                     maxlength="1" size="1" tabindex="1"/>

  <tr><td><font class="text_10">��������:</font></td>
      <td><html:text accesskey="�" name="abit_Kursy" styleClass="text_f10" property="informatsijaOKursah"
                     maxlength="150" size="50" tabindex="2"/>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_Kursy" property="kodKursov"/>
<html:submit styleClass="button" tabindex="3" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="4" value="�������"/>
</td></html:form>
<html:form action="/kursy?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="5" value="��������� �����"/>
</td></html:form>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>