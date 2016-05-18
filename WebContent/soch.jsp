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

<logic:notPresent name='sochAction' scope='request'>
 <logic:redirect forward='soch'/>
</logic:notPresent>

<logic:notPresent name="sochForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields() {
 if(document.forms(0).kodFakulteta.value == "-")
 {
  alert("���������� ������� ���������");
  document.forms(0).kodFakulteta.focus();
  return false;
 }
}

function exec() {
   document.forms(0).kodFakulteta.focus();
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="sochForm" property="action">
<bean:define id="action" name="sochForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- ��������� ��������� ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>������ ������������ � ������� �����</template:put>
<template:put name="target_name">������ ������������ � ������� �����</template:put>
<template:put name="sub_name">���������������</template:put>
<template:put name='content'>
<BR>
<html:form action="/soch?action=report" onsubmit="return checkFields();">
<table cols=3 align=center border=0>
<tr><td colspan=3><hr></td></tr>
<tr><td><font class="text_10">���������:&nbsp;&nbsp;</font></td>
    <td><html:select styleClass="select_f1" name="abit_SD" property="kodFakulteta" tabindex="1">
     <html:option value="0">���</html:option>
          <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
        </html:select> 
    </td></tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="������������"/></td>
</html:form>
<html:form method="post" action="/soch?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="�����"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>