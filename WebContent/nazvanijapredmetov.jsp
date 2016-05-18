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

<logic:notPresent name='nazvanijaPredmetovAction' scope='request'>
 <logic:redirect forward='nazvanijapredmetov'/>
</logic:notPresent>

<logic:notPresent name="nazvanijaPredmetovForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid = " ������������������������������������������������������������������-()."
var temp;

if(document.forms(0).predmet.value.length == 0) 
 {
  alert("���������� ��������� ���� ''�������� ��������''");
  document.forms(0).predmet.focus();
  return false;
 }
for (var i=0; i<document.forms(0).predmet.value.length; i++) 
 {
  temp = "" + document.forms(0).predmet.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("���� ''�������� ��������'' ����� ��������� ������ ����� �������� ��������");
   document.forms(0).predmet.focus();
   return false;
  }
 }
if(document.forms(0).datelnyj.value.length == 0) 
 {
  alert("���������� ��������� ���� ''���������''");
  document.forms(0).datelnyj.focus();
  return false;
 }
for (var i=0; i<document.forms(0).datelnyj.value.length; i++) 
 {
  temp = "" + document.forms(0).datelnyj.value.substring(i, i+1);
  if (valid.indexOf(temp) == "-1") {
   alert("���� ''���������'' ����� ��������� ������ ����� �������� ��������");
   document.forms(0).datelnyj.focus();
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
  if (valid.indexOf(temp) == "-1") {
   alert("���� ''����������'' ����� ��������� ������ ����� �������� ��������");
   document.forms(0).sokr.focus();
   return false;
  }
 }
}

function gap_me(){
  document.forms(0).predmet.value = " ";
  document.forms(0).datelnyj.value = " ";
  document.forms(0).sokr.value = " ";
}

function confirmation(){
  if(confirm('������� �������?'))
    return true;
  else 
    return false; 
}

function invokeAct(){
  document.forms(0).submit();
}

function exec() {
if(document.forms(0).predmet != null)
document.forms(0).predmet.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="nazvanijaPredmetovForm" property="action">
<bean:define id="action" name="nazvanijaPredmetovForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">�������������� �������</template:put>
<template:put name="sub_name">���������� ������</template:put>
<template:put name='title'>��������</template:put>
<template:put name='content'>
<BR>
<html:form action="/nazvanijapredmetov?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">�������� ��������:</font></td>
      <td><html:text accesskey="�" name="abit_NP" styleClass="text_f10" property="predmet" 
                     maxlength="50" size="50" tabindex="1" value=""/>

  <tr><td><font class="text_10">���������:</font></td>
      <td><html:text accesskey="�" name="abit_NP" styleClass="text_f10" property="datelnyj" 
                     maxlength="50" size="50" tabindex="2" value=""/>

  <tr><td><font class="text_10">����������� ��������:</font></td>
      <td><html:text accesskey="�" name="abit_NP" styleClass="text_f10" property="sokr" 
                     maxlength="10" size="10" tabindex="3" value=""/>
<tr><td height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="4" value="��������"/></td>
  <td><html:reset  styleClass="button" tabindex="5" value="��������"/></td>
  <td><html:submit property="full" onclick="return gap_me();" styleClass="button" tabindex="6" value="�������� �������"/></td>
</html:form>
<html:form action="/nazvanijapredmetov?action=null">
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
<template:put name="target_name">�������������� �������� ����</template:put>
<template:put name='title'>��������</template:put>
<template:put name='content'>
<BR>
<table cols=3 align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center height=30><font class="text_th">�������� ��������</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;���������&nbsp;�����&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;����������� ��������&nbsp;</font></td></tr>
</thead>
<logic:iterate id="abit_NP" name="abits_NP" scope='request'>
<tr>
  <td valign=center>&nbsp;<html:link href="nazvanijapredmetov.do?action=mod_del" paramName="abit_NP" 
                   paramId="kodPredmeta" paramProperty="kodPredmeta" styleClass="link_hov_blue">
                    <bean:write name="abit_NP" property="predmet"/></html:link>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="abit_NP" property="datelnyj"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_NP" property="sokr"/></font>&nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_NP" property="kodPredmeta">
<tr>
  <td align=center valign=center colspan=3><font class="text_11">
     �&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>
<table align=center border=0>
<tr>
<html:form action="/nazvanijapredmetov.do?action=new">
  <td align=center vAlign=middle height=35>
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
<template:put name="target_name">�������������� ������� ����</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='title'>��������</template:put>
<template:put name='content'>
<BR>
<html:form action="/nazvanijapredmetov?action=change" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">�������� ��������:</font></td>
      <td><html:text accesskey="�" name="abit_NP" styleClass="text_f10" property="predmet" 
                     size="50" maxlength="50" tabindex="1"/>
  <tr><td><font class="text_10">���������:</font></td>
      <td><html:text accesskey="�" name="abit_NP" styleClass="text_f10" property="datelnyj" 
                     size="50" maxlength="50" tabindex="2"/>
  <tr><td><font class="text_10">����������� ��������:</font></td>
      <td><html:text accesskey="�" name="abit_NP" styleClass="text_f10" property="sokr" 
                     size="10" maxlength="5" tabindex="3"/>
<tr><td height=6></td></tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_NP" property="kodPredmeta"/>
<html:submit styleClass="button" tabindex="4" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="5" value="�������"/>
</td></html:form>
<html:form action="/nazvanijapredmetov?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="6" value="��������� �����"/>
</td></html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>