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

<logic:notPresent name='punktyAction' scope='request'>
 <logic:redirect forward='punkty'/>
</logic:notPresent>

<logic:notPresent name="punktyForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
var valid1 = " ������������������������������������������������������������������().-01234567890"
var temp;

for (var i=0; i<document.forms(0).nazvanie.value.length; i++) {
  temp = "" + document.forms(0).nazvanie.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
    alert("���� ''�������� ������'' ����� ��������� ������ ��������� ����� �������� ��������");
    document.forms(0).nazvanie.focus();
    return false;
  }
 }
if(document.forms(0).nazvanie.value.length == 0) 
 {
  alert("���������� ��������� ���� ''�������� ������''");
  document.forms(0).nazvanie.focus();
  return false;
 }
}
function exec(){
  document.forms(0).nazvanie.focus();
}
</SCRIPT>


<logic:present name="punktyForm" property="action">
<bean:define id="action" name="punktyForm" property="action"/>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>���������� ������</template:put>
<template:put name='target_name'>���������� ������</template:put>
<template:put name='content'>
<BR>
<html:form action="punkty.do?action=create">
<p align=center>
<logic:iterate id="abit_P" name="abit_P_S1" scope='request'>
        <html:link styleClass="link_hov_blue" href="punkty.do?action=full" paramId="letter" paramName="abit_P" paramProperty="special1">
        <bean:write name="abit_P" property="special2"/>
        </html:link>
</logic:iterate>
</p>
</html:form>
<table cols=100 align=center border=0 cellSpacing=0>
<logic:iterate id="abit_P" name="abits_P" scope='request'>
<tr>
  <td valign=center>&nbsp;<html:link styleClass="link_hov_blue" href="punkty.do?action=mod_del" paramName="abit_P" 
                               paramId="kodPunkta" paramProperty="kodPunkta">
                    <bean:write name="abit_P" property="nazvanie"/></html:link>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_P" property="kodPunkta">
<tr>
  <td align=center valign=center colspan=13><font class="text_11">
     �&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������&nbsp;</font></td>
</tr><tr>
  <td align=center valign=center colspan=13><font class="text_11">&nbsp;��������&nbsp;������&nbsp;�����!</font></td>
</tr>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr>
<html:form action="/abit_srch.do">
  <td align=center>
    <html:submit styleClass="button" value="�����������"/>
  </td>
</html:form>
<html:form action="/rajony.do">
  <td align=center>
    <html:submit styleClass="button" value="������"/>
  </td>
</html:form>
<html:form action="/oblasti.do">
  <td align=center>
    <html:submit styleClass="button" value="�������"/>
  </td>
</html:form>
<html:form action="/punkty.do">
  <td align=center>
    <html:submit styleClass="button" property="exit" value="�����"/>
  </td>
</html:form>

</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%---------------- ����������� ����� ������ � �� ------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>���������� ������</template:put>
<template:put name='target_name'>���������� �����</template:put>
<template:put name='sub_name'>����������� ������</template:put>
<template:put name='content'>
<body onLoad="exec()">
<BR>
<html:form action="/punkty?action=change" onsubmit="return checkFields();">
<table cols=1 align=center border=0>

  <tr><td><font class="text_11">�������� ������:</font></td>
      <td><html:text accesskey="�" name="abit_P" styleClass="text_f11" property="nazvanie" 
                     size="50" maxlength="150" tabindex="1"/>
</table>
<br>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_P" property="kodPunkta"/>
<html:submit styleClass="button" tabindex="2" value="��������"/>
</td><td align=center>
<html:submit styleClass="button" tabindex="3" value="�������" property="delete"/>
</td></html:form>
<html:form action="/punkty?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="4" value="��������� �����"/>
</td></html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>