<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
    import      = "abit.Constants"
    import      = "abit.util.StringUtil"
%>

<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='abitDocAction' scope='request'>
 <logic:redirect forward='abit_doc'/>
</logic:notPresent>

<logic:notPresent name="abiturientForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function exec() {
//  document.forms(0).familija.focus();
}

</SCRIPT>

<logic:present name="abiturientForm" property="action">
<bean:define id="action" name="abiturientForm" property="action"/>

<%-----------------------------------------------------------------%>
<%--------   ���������� ������������ ������ ����������   ----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="pakdoc">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">����� ���������� ������� �����������</template:put>
<template:put name="target_name">����� ���������� ������� �����������</template:put>
<template:put name="content">

<%-------------------------------------%>
<%--  ������ ���������� �����������  --%>
<%-------------------------------------%>

<html:form action="/abiturient.do">
<html:hidden name="abit_A" property="kodAbiturienta"/>

<table width="100%" border="0" cellSpacing="0" cellPadding="0">
<tbody>
 <tr>
   <td align="center" valign="middle"><font class="text_th">&nbsp;<bean:write name="abit_A" property="familija"/>&nbsp;<bean:write name="abit_A" property="imja"/>&nbsp;<bean:write name="abit_A" property="otchestvo"/>&nbsp;</font></td>
 </tr>
</tbody>
</table>
<br>
<table width="400" border="1" cellSpacing="0" cellPadding="0">
<thead>
 <tr>
   <td height="30" align="center" valign="middle">&nbsp;�&nbsp;</font</td>
   <td height="30" align="center" valign="middle">&nbsp;��������&nbsp;���������&nbsp;</font</td>
 </tr>
</thead>
<tbody>
 <tr>
   <td height="22" align="center" valign="middle"><font class="text_10">&nbsp;1.&nbsp;</font</td>
   <bean:define id="f_name1" name="abit_A" property="fileName1"/>
   <td height="22" align="left" valign="middle"><font class="text_10">&nbsp;<html:link href="<%="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/packets/"+f_name1%>" target="wndReportNew" styleClass="link_hov_blue">
                       &nbsp;���������&nbsp;&nbsp;��&nbsp;�������&nbsp;�&nbsp;��������&nbsp;</html:link>&nbsp;</font></td>
 </tr>
 <tr>
   <td height="22" align="center" valign="middle"><font class="text_10">&nbsp;2.&nbsp;</font</td>
   <bean:define id="f_name2" name="abit_A" property="fileName2"/>
   <td height="22" align="left" valign="middle"><font class="text_10">&nbsp;<html:link href="<%="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/packets/"+f_name2%>" target="wndReportNew" styleClass="link_hov_blue">
                       &nbsp;��������&nbsp;</html:link>&nbsp;</font></td>
 </tr>
 
  <tr>
   <td height="22" align="center" valign="middle"><font class="text_10">&nbsp;3.&nbsp;</font</td>
   <bean:define id="f_name3" name="abit_A" property="fileName3"/>
   <td height="22" align="left" valign="middle"><font class="text_10">&nbsp;<html:link href="<%="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/packets/"+f_name3%>" target="wndReportNew" styleClass="link_hov_blue">
                       &nbsp;��������&nbsp;�&nbsp;����������&nbsp;��&nbsp;��������&nbsp;</html:link>&nbsp;</font></td>
 </tr>
 
</tbody>
</table>
<BR>
<table align="center" border="0">
  <tr align="center">
    <td><html:submit styleClass="button" value="���� ����� ��������" tabindex="1"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/abit_srch.do">
    <td><html:submit styleClass="button" value="����� ��������" tabindex="2"/>&nbsp;&nbsp;</td>
    <td><html:submit styleClass="button" value="�����" tabindex="3" property="exit"/></td>
  </tr>
</table>
</html:form>
</template:put>
</template:insert>
</logic:equal>
</logic:present>