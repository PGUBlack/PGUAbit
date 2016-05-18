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

<logic:notPresent name='listsKgRukAction' scope='request'>
 <logic:redirect forward='rep_kgruk'/>
</logic:notPresent>

<logic:notPresent name="listsRukForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength

function checkFields() {
}

function exec() {
}

</SCRIPT>


<logic:present name="listsRukForm" property="action">
<bean:define id="action" name="listsRukForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- ��������� ��������� ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>������ ��� ����������� ����</template:put>
<template:put name="target_name">������ ������������ ��� ����������� ����</template:put>
<template:put name="sub_name">�������-���� �� ���������� �������</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_kgruk?action=report" onsubmit="return checkFields();">
<table align=center border="0" cellSpacing="0" cellPadding="0">
<thead>
  <tr><td height="26" colspan="2" align="center"><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�����&nbsp;����������:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td></tr>
</thead>
<tr><td width="120" align="right"><html:radio accesskey="�" name="abit_SD" property="priznakSortirovki" tabindex="1" value="sotsenkoi"/>
</td><td align="left"><font class="text_10">&nbsp;��&nbsp;���������&nbsp;</font></td>
</tr>
<tr><td width="120" align="right"><html:radio accesskey="�" name="abit_SD" property="priznakSortirovki" tabindex="2" value="first"/>
</td><td align="left"><font class="text_10">&nbsp;������&nbsp;����&nbsp;&nbsp;&nbsp;</font></td>
</tr>
<tr><td width="120" align="right"><html:radio accesskey="�" name="abit_SD" property="priznakSortirovki" tabindex="3" value="second"/>
</td><td align="left"><font class="text_10">&nbsp;������&nbsp;����&nbsp;&nbsp;&nbsp;</font></td>
</tr>
<tr><td width="120" align="right"><html:radio accesskey="�" name="abit_SD" property="priznakSortirovki" tabindex="4" value="third"/>
</td><td align="left"><font class="text_10">&nbsp;������&nbsp;����&nbsp;&nbsp;&nbsp;</font></td>
</tr>
<tr><td colspan="2" align="center" height="18"><hr></td></tr>
<tr><td colspan="2" align="center">&nbsp;&nbsp;&nbsp;&nbsp;<html:checkbox name="abit_SD" property="special1" value="all_predm"/>
<font class="text_10">&nbsp;����������&nbsp;�����&nbsp;��&nbsp;���������&nbsp;&nbsp;&nbsp;</font></td>
</tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="5" value="������� �����"/></td>
</html:form>
<html:form method="post" action="/rep_kgruk?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="6" value="�����"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>