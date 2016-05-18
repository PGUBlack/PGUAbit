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

<logic:notPresent name='sqlrepAction' scope='request'>
 <logic:redirect forward='sqlrep'/>
</logic:notPresent>

<logic:notPresent name="sqlrepForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
function confirm1(param1){
  var box=confirm("\t  ��������������!!! \n ��������� ��������� ����� �� ����� �������!");
  if(box) {
    box=confirm("\t  ��������������!!! \n ��������� ��������� ����� �� ����� �������!");
    if(!box) {
	  return false;
	}
  }
  else{
    return false;
  }
}
function confirm2(param1){
  var box=confirm("\t  ��������������!!! \n �� ����� ��������� ���������� ���������");
  if(box) {
    box=confirm("\t  ��������������!!! \n �� ����� ��������� ���������� ���������");
    if(!box) {
	  return false;
	}
  }
  else{
    return false;
  }
}
</SCRIPT>

<logic:present name="sqlrepForm" property="action">
<bean:define id="action" name="sqlrepForm" property="action"/>

<%-----------------------------------------------------------------%>
<%---------------------- ����� ��������� --------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new_rep">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name='title'>����� ����� ��</template:put>
<template:put name="target_name">��������� ����� ��</template:put>
<template:put name="sub_name">��������� �������� ������</template:put>
<template:put name='content'>
<br>
<p align=center valign=center>
  <font class="text_10"><bean:write name="report" property="special1"/></font>
</p>
<table cols=4 align=center border=1 cellspacing=0>
 <thead>
  <tr><td align=center><font class="text_th">&nbsp;&nbsp;��������&nbsp;�������&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;���&nbsp;�����&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;����&nbsp;��������&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;�����&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;���&nbsp;������&nbsp;&nbsp;</font></td>
  </tr>
 </thead>
  <bean:define id="name" name="report" property="fileName"/>
  <tr><td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="name"/>&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="fileName"/>&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="date"/>&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="time"/>&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="author"/>&nbsp;&nbsp;</font></td>
  </tr>
</table>
<table align=center border=0 cols=2>
<tr>
<html:form action="/sqlrep.do?action=old">
<td align=center>
  <html:submit styleClass="button_sd" value="���� ������"/>
</td>
</html:form>
<html:form action="/sqlrep?action=null">
<td align=center>
  <html:submit styleClass="button_sd" property="exit" value="�����"/>
</td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%---------------------- ����� ��������� --------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="old_rep">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name='title'>��������� ����� ��</template:put>
<template:put name="target_name">��������� ����� ��</template:put>
<template:put name="sub_name">����������</template:put>
<template:put name='content'>
<br>
<p align=center><font class="text_11">
 <logic:iterate id="mess" name="msgs" scope="request" type="abit.bean.MessageBean">
  <bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/><br>
 </logic:iterate>
</font></p>
<table align=center border=1 cellspacing=0>
 <thead>
  <tr>
      <td height=30 align=center><font class="text_th">&nbsp;�&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;��������&nbsp;�������&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;���&nbsp;�����&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;����&nbsp;��������&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;�����&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;���&nbsp;������&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;��������&nbsp;&nbsp;</font></td>
  </tr>
 </thead>
<logic:iterate id="rept" name="reports">
     <bean:define id="f_name" name="rept" property="fileName"/>
  <tr>
     <td align="center"><font class="text_10">&nbsp;<bean:write name="rept" property="number"/>&nbsp;</font></td>
     <td align="center">&nbsp;<html:link href="sqlrep.do?action=restore" paramName="rept" paramId="special2" paramProperty="fileName" styleClass="link_hov_blue" onclick="return confirm2('111');">
                              <bean:write name="rept" property="name"/></html:link>&nbsp;</td>
     <td align="center"><font class="text_10">&nbsp;<bean:write name="rept" property="fileName"/>&nbsp;</font></td>
     <td align="center"><font class="text_10">&nbsp;<bean:write name="rept" property="date"/>&nbsp;</font></td>
     <td align="center"><font class="text_10">&nbsp;<bean:write name="rept" property="time"/>&nbsp;</font></td>
     <td align="center"><font class="text_10">&nbsp;<bean:write name="rept" property="author"/>&nbsp;</font></td>
     <td align="center">&nbsp;<html:link href="sqlrep.do?action=restore" paramName="rept" paramId="special2" paramProperty="fileName" styleClass="link_hov_blue" onclick="return confirm2('222');">������������</html:link>&nbsp;<br>
	 &nbsp;<html:link href="sqlrep.do?action=delete" paramName="rept" paramId="special2" paramProperty="fileName" styleClass="link_hov_blue" onclick="return confirm1('333');">�������</html:link>&nbsp;</td>
  </tr>
</logic:iterate>
<logic:notPresent name="rept" property="fileName">
<tr>
  <td align=center colspan=8>
     <font class="text_11">�&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>
<table align=center border=0 cols=2>
<tr>
<html:form action="/sqlrep?action=null">
<td align=center><html:submit styleClass="button_sd" property="exit" value="�����"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>