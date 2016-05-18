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

<logic:notPresent name='newsAction' scope='request'>
 <logic:redirect forward='news'/>
</logic:notPresent>

<logic:notPresent name="newsForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function checkFields(){
var valid1 = " �������������������������������"
var valid2 = " ������������������������������������������������������������������,.-1234567890()\""
var temp;

if(document.forms(0).data.value == "00.00.0000"){
  alert("���������� ������� ''����'' �������");
  document.forms(0).data.focus();
  return false;
 }

if(document.forms(0).description.value.length == 0){
  alert("���������� ������ ''��������'' �������");
  document.forms(0).description.focus();
  return false;
 }
}

function confirmation(){
  if(confirm('������� �������?'))
   {
    return true;
   }
  else 
    return false; 
}

function exec() {
  document.forms(0).data.focus();
}
</SCRIPT>

<logic:present name="newsForm" property="action">
<bean:define id="action" name="newsForm" property="action"/>


<%-----------------------------------------------------------------%>
<%---------------------- ���������� ������� -----------------------%>
<%-----------------------------------------------------------------%>

<logic:equal name="action" value="add_new">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>������� ��� ����������</template:put>
<template:put name="target_name">������� ��� ����������</template:put>
<template:put name="sub_name">���������� �������</template:put>
<template:put name='content'>
<html:form method="post" action="/news?action=create" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">����:</font></td>
      <td><html:text accesskey="�" name="abit_news" styleClass="text_f10_short" property="data" 
                     maxlength="10" size="10" tabindex="1"/>

  <tr><td><font class="text_10">����������:</font></td>
      <td><html:textarea accesskey="�" name="abit_news" styleClass="text_f10" property="description"
                     rows='8' cols='100' tabindex="2" value=""/>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
</html:form>
<html:form method="post" action="/news.do">
  <td><html:submit property="full" styleClass="button" tabindex="4" value="�������� ��������"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%---------------------------------------------------------------------%>
<%---------------------- �������������� ������� -----------------------%>
<%---------------------------------------------------------------------%>

<logic:equal name="action" value="mod_del">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>������� ��� ����������</template:put>
<template:put name="target_name">������� ��� ����������</template:put>
<template:put name="sub_name">�������������� �������</template:put>
<template:put name='content'>
<html:form method="post" action="/news?action=md" onsubmit="return checkFields();">
<html:hidden name="abit_news" property="idNews"/>
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">����:</font></td>
      <td><html:text accesskey="�" name="abit_news" styleClass="text_f10_short" property="data" 
                     maxlength="10" size="10" tabindex="1"/>

  <tr><td><font class="text_10">����������:</font></td>
      <td><html:textarea accesskey="�" name="abit_news" styleClass="text_f10" property="description"
                     rows='8' cols='100' tabindex="2"/>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
  <td>&nbsp;<html:submit property="del" onclick="return confirmation();" styleClass="button" tabindex="4" value="�������"/></td>
</html:form>
<html:form method="post" action="/news.do">
  <td>&nbsp;<html:submit property="full" styleClass="button" tabindex="5" value="�������� ��������"/></td>
</html:form>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>

<logic:equal name="action" value="view_news">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>������� �������</template:put>
<template:put name="target_name">������� ��� "����������"</template:put>
<template:put name='content'>
<p align=center>
<strong><font class="text_16">������ ����, <bean:write name="abit_news" property="special3"/>!<br> ������� <bean:write name="abit_news" property="special1"/></font></strong>
</p>
<table align=center border=0 cellSpacing=0>
<tr valign=middle>
<logic:equal name='user_type' scope='request' value='0'>
 <html:form method="post" action="/news.do?action=new">
  <td align=center height="40"><html:submit styleClass="button" value="���������� �������"/></td>
 </html:form>
</logic:equal>
<html:form action="/news.do">
  <td align=center vAlign=middle height=35>&nbsp;
    <html:submit styleClass="button" property="next" value="���������� ������"/>
  </td>
</tr>
<logic:notEqual name='user_type' scope='request' value='0'>
<tr valign=middle>
 <td colspan=2 height=35>
   <font class="text_11"><html:checkbox name="abit_news" property="special2"/> ������ ��� ��������, ���� �� �������� ����� �������</font>
 </td>
</tr>
</logic:notEqual>
 </html:form>
</table>
<table align=center border=1 cellSpacing=0>
<thead>
<tr><td width=90 align=center valign=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td>
    <logic:equal name='user_type' scope='request' value='0'>
      <td width=40 align=center valign=center height=30><font class="text_th">&nbsp;���.&nbsp;</font></td>
    </logic:equal>
    <td align=center valign=center height=30><font class="text_th">&nbsp;����������&nbsp;</font></td></tr>
</thead>
<logic:iterate id="abit_n" name="abits_news" scope='request'>
<tr valign="middle">
  <td align=center>&nbsp;<font class="text_10"><bean:write name="abit_n" property="data"/></font>&nbsp;</td>
    <logic:equal name='user_type' scope='request' value='0'>
      <td align=center>
        <html:link href="news.do?action=mod_del" paramName="abit_n" paramId="idNews" paramProperty="idNews" styleClass="link_hov_blue">
         &nbsp;<bean:write name="abit_n" property="idNews"/>
        </html:link>&nbsp;</td>
    </logic:equal>
  <td align=left>&nbsp;<font class="text_10"><bean:write name="abit_n" property="description"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_n" property="data">
<tr>
  <td align=center valign=center colspan=10>
     <font class="text_11">�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;�������</font></td>
</logic:notPresent>
</table>
</template:put>
</template:insert>
</logic:equal>

</logic:present>