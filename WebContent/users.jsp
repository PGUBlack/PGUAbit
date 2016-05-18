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

<logic:notPresent name='usersAction' scope='request'>
 <logic:redirect forward='users'/>
</logic:notPresent>

<logic:notPresent name="usersForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var execute = "true";
var oldName;
var del = false;

function checkFields(){
var temp;
if (del) return true;

if(execute == "true") {
if(document.forms(0).familija.value.length == 0) 
 {
  alert("���������� ������� ''�������''");
  document.forms(0).familija.focus();
  return false;
 }
if(document.forms(0).imja.value.length == 0) 
 {
  alert("���������� ������� ''���''");
  document.forms(0).imja.focus();
  return false;
 }
if(document.forms(0).otchestvo.value.length == 0) 
 {
  alert("���������� ������� ''��������''");
  document.forms(0).otchestvo.focus();
  return false;
 }
if(document.forms(0).userName.value.length == 0) 
 {
  alert("���������� ��������� ���� ''�����''");
  document.forms(0).userName.focus();
  return false;
 }
if(document.forms(0).password.value.length <8) 
 {
  alert("������ ������ �������� �� ������� ���� �� 8 ��������");
  document.forms(0).password.focus();
  return false;
 }
if(document.forms(0).password2.value.length == 0) 
 {
  alert("���������� ��������� ''������������� ������''");
  document.forms(0).password2.focus();
  return false;
 }
if(document.forms(0).password.value != document.forms(0).password2.value){
  alert("��������� ������ �� ���������! ����������, ���������� ����.");
  document.forms(0).password.value="";
  document.forms(0).password2.value="";
  document.forms(0).password.focus();
  return false;
}
 return true;
}
}

function checkFields2(){
var temp;
if (del) return true;

if(execute == "true") {
if(document.forms(0).familija.value.length == 0) 
 {
  alert("���������� ������� ''�������''");
  document.forms(0).familija.focus();
  return false;
 }
if(document.forms(0).imja.value.length == 0) 
 {
  alert("���������� ������� ''���''");
  document.forms(0).imja.focus();
  return false;
 }
if(document.forms(0).otchestvo.value.length == 0) 
 {
  alert("���������� ������� ''��������''");
  document.forms(0).otchestvo.focus();
  return false;
 }
if(document.forms(0).userName.value.length == 0) 
 {
  alert("���������� ��������� ���� ''�����''");
  document.forms(0).userName.focus();
  return false;
 }
if(document.forms(0).special3.checked == true){
if(document.forms(0).password.value.length <8) 
 {
  alert("������ ������ �������� �� ������� ���� �� 8 ��������");
  document.forms(0).password.focus();
  return false;
 }
if(document.forms(0).password2.value.length == 0) 
 {
  alert("���������� ��������� ''������������� ������''");
  document.forms(0).password2.focus();
  return false;
 }
if(document.forms(0).password.value != document.forms(0).password2.value){
  alert("��������� ������ �� ���������! ����������, ���������� ����.");
  document.forms(0).password.value="";
  document.forms(0).password2.value="";
  document.forms(0).password.focus();
  return false;
}
}
 return true;
}
}

function gap_me(){
  execute = "false";
}

function confirmation(){
  if(confirm('������� ������������ �� �������?')) {
    del = true;
    return true;
  }
  else 
    return false; 
}

function exec() {
   document.forms(0).kodGruppy.selectedIndex=0;
   document.forms(0).familija.focus();
}
function exec2() {
   document.forms(0).familija.focus();
}
</SCRIPT>

<logic:present name="usersForm" property="action">
<bean:define id="action" name="usersForm" property="action"/>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>������������ �������</template:put>
<template:put name="target_name">���������� �������� � �������</template:put>
<template:put name="sub_name">������������ �������</template:put>
<template:put name='content'>
<table align=center border=0 cellSpacing=0 width=80%>
<tr><td valign=middle>
<table align=center border=1 cellSpacing=0 width=100%>
<thead>
<tr><td align=center colspan=2 valign=center height=30><font class="text_th">&nbsp;� � � � � � � � � � � � � �&nbsp;</font></td></tr>
<tr><td colspan=2 height=1></td></tr>
<tr><td align=middle valign=middle>&nbsp;�����&nbsp;</td>
    <td align=middle valign=middle>&nbsp;��������&nbsp;</td>
</tr>
</thead>
<logic:iterate id="group_info" name="administrat" scope='request' type="abit.bean.AbiturientBean">
<tr><td align=center valign=center>&nbsp;<html:link href="users.do?action=mod_del" paramName="group_info" paramId="id" paramProperty="special11" styleClass="link_hov_blue">
                    <bean:write name="group_info" property="special1"/></html:link>&nbsp;</td>
    <td align=left valign=center><font class="text_11">&nbsp;<bean:write name="group_info" property="description"/>&nbsp;</font></td></tr>
</logic:iterate>
</table></td></tr>
<tr><td valign=middle><BR></td></tr>
<tr><td valign=middle>
<table align=center border=1 cellSpacing=0 width=100%>
<thead>
<tr><td align=center colspan=2 valign=center height=30><font class="text_th">&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;&nbsp;&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;�&nbsp;</font></td></tr>
<tr><td colspan=2 height=1></td></tr>
<tr><td align=middle valign=middle>&nbsp;�����&nbsp;</td>
    <td align=middle valign=middle>&nbsp;��������&nbsp;</td>
</tr>
</thead>
<logic:iterate id="group_info" name="operats_inp" scope='request' type="abit.bean.AbiturientBean">
<tr><td align=center valign=center>&nbsp;<html:link href="users.do?action=mod_del" paramName="group_info" paramId="id" paramProperty="special11" styleClass="link_hov_blue">
                    <bean:write name="group_info" property="special1"/></html:link>&nbsp;</td>
    <td align=left valign=center><font class="text_11">&nbsp;<bean:write name="group_info" property="description"/>&nbsp;</font></td></tr>
</logic:iterate>
</table></td></tr>
<tr><td valign=middle><BR></td></tr>
<tr><td valign=middle>
<table align=center border=1 cellSpacing=0 width=100%>
<thead>
<tr><td align=center colspan=2 valign=center height=30><font class="text_th">&nbsp;� � � � � � � � �&nbsp;</font></td></tr>
<tr><td colspan=2 height=1></td></tr>
<tr><td align=middle valign=middle>&nbsp;�����&nbsp;</td>
    <td align=middle valign=middle>&nbsp;��������&nbsp;</td>
</tr>
</thead>
<logic:iterate id="group_info" name="operats" scope='request' type="abit.bean.AbiturientBean">
<tr><td align=center valign=center>&nbsp;<html:link href="users.do?action=mod_del" paramName="group_info" paramId="id" paramProperty="special11" styleClass="link_hov_blue">
                    <bean:write name="group_info" property="special1"/></html:link>&nbsp;</td>
    <td align=left valign=center><font class="text_11">&nbsp;<bean:write name="group_info" property="description"/>&nbsp;</font></td></tr>
</logic:iterate>
</table></td></tr>
<tr><td valign=middle><BR></td></tr>
<tr><td valign=middle>
<table align=center border=1 cellSpacing=0 width=100%>
<thead>
<tr><td align=center colspan=2 valign=center height=30><font class="text_th">&nbsp;&nbsp;&nbsp;� � � � � � � � � � �&nbsp;&nbsp;&nbsp;</font></td></tr>
<tr><td colspan=2 height=1></td></tr>
<tr><td align=middle valign=middle>&nbsp;�����&nbsp;</td>
    <td align=middle valign=middle>&nbsp;��������&nbsp;</td>
</tr>
</thead>
<logic:iterate id="group_info" name="others" scope='request' type="abit.bean.AbiturientBean">
<tr><td align=center valign=center>&nbsp;<html:link href="users.do?action=mod_del" paramName="group_info" paramId="id" paramProperty="special11" styleClass="link_hov_blue">
                    <bean:write name="group_info" property="special1"/></html:link>&nbsp;</td>
    <td align=left valign=center><font class="text_11">&nbsp;<bean:write name="group_info" property="description"/>&nbsp;</font></td></tr>
</logic:iterate>
</table></td></tr>
<logic:notPresent name="group_info" property="special11">
<tr><td align=center valign=center colspan=3 rowspan=3>
<p align=center>
     <font class="text_11">��������!&nbsp;&nbsp;������&nbsp;��������.&nbsp;</font></td>
  </td>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr><td>
<html:form action="/users?action=new">
  <html:submit styleClass="button" onclick="gap_me();" value="���������� ������"/>
</html:form></td><td>
<html:form action="/users?action=null">
  <html:submit styleClass="button" onclick="gap_me();" property="exit" value="�����"/>
</html:form></td>
</tr></table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>���������� ������ ������������</template:put>
<template:put name="target_name">���������� ������ ������������</template:put>
<template:put name="sub_name">���������� ������ ������������</template:put>
<template:put name='content'>
<br>
<html:form method='POST' action="/users?action=create" scope='request' onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
    <tr><td><font class="text_10">�������:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='familija' size='20' value="" maxlength='30' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">���:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='imja' size='10' value="" maxlength='25' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">��������:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='otchestvo' size='20' value="" maxlength='30' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">�����:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='userName' size='30' maxlength='30' value="" styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">������:</font></td>
        <td><html:select name='u_bean' property='kodGruppy' styleClass="select_f2">
             <html:options collection="groups" property="kodGruppy" labelProperty="userName"/>   
            </html:select>
        </td>
    </tr>
    <tr><td><font class="text_10">��������:</font></td>
        <td><html:textarea accesskey="�" name='u_bean' property='description' rows='6' cols='50' value="" styleClass="text_f10"/></td>
    </tr>
    <%--tr><td><font class="text_10">�������� ������:</font></td>
        <td><html:checkbox accesskey="�" styleClass="checkbox_1" name='u_bean' property='changePass'/></td>
    </tr--%>
    <tr><td><font class="text_10">������:</font></td>
        <td><html:password accesskey="�" name='u_bean' property='password' size='15' value="" maxlength='15' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">�������������:</font></td>
        <td><html:password accesskey="�" name='u_bean' property='password2' size='15' value="" maxlength='15' styleClass="text_f10"/></td>
    </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="��������"/></td>
  <td><html:reset  styleClass="button" value="��������"/></td>
  <td><html:submit onclick="gap_me();" property="full" styleClass="button" value="����� � ������"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ �����������/�������� ����� ������ � �� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<body onLoad="exec2()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>����������� ������������ �������</template:put>
<template:put name="target_name">����������� ������ ������������</template:put>
<template:put name="sub_name">����������� ���������� ������������</template:put>
<template:put name='content'>
<br>
<html:form method='POST' action="/users?action=change" scope='request' onsubmit="return checkFields2();">
<html:hidden name="u_bean" property="kodAbiturienta"/>
<table align=center border=0 cellspacing=0>
    <tr><td><font class="text_10">�������:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='familija' size='20' maxlength='30' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">���:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='imja' size='10' maxlength='25' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">��������:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='otchestvo' size='20' maxlength='30' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">�����:</font></td>
        <td><html:text accesskey="�" name='u_bean' property='userName' size='30' maxlength='30' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">������:</font></td>
        <td><html:select name='u_bean' property='kodGruppy' styleClass="select_f2">
             <html:options collection="groups" property="kodGruppy" labelProperty="userName"/>   
            </html:select>
        </td>
    </tr>
    <tr><td><font class="text_10">��������:</font></td>
        <td><html:textarea accesskey="�" name='u_bean' property='description' rows='6' cols='50' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">�������� ������:</font></td>
        <td><html:checkbox accesskey="�" styleClass="checkbox_1" name='u_bean' property='special3' value='repass'/></td>
    </tr>
    <tr><td><font class="text_10">������:</font></td>
        <td><html:password accesskey="�" onchange="document.forms(0).special3.checked=true;" name='u_bean' property='password' size='15' maxlength='15' styleClass="text_f10"/></td>
    </tr>
    <tr><td><font class="text_10">�������������:</font></td>
        <td><html:password accesskey="�" onchange="document.forms(0).special3.checked=true;" name='u_bean' property='password2' size='15' maxlength='15' styleClass="text_f10"/></td>
    </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="��������"/></td>
  <td><html:submit property="delete" onclick="confirmation()" styleClass="button" value="������� ������������"/></td>
  <td><html:submit onclick="gap_me();" property="full" styleClass="button" value="����� � ������"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>