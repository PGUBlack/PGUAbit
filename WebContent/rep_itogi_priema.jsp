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

<logic:notPresent name='itogi_Priema_Action' scope='request'>
 <logic:redirect forward='rep_itogi_Priema'/>
</logic:notPresent>

<logic:notPresent name="forma_2_Form" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function exec() {

}

</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="forma_2_Form" property="action">
<bean:define id="action" name="forma_2_Form" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- ��������� ��������� ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>����� ������ � ������ ������� ���������</template:put>
<template:put name="target_name">����� ������ � ������ ������� ���������</template:put>
<template:put name="sub_name">������� �������� ������</template:put>
<template:put name='content'>
<BR>
<html:form action="/rep_itogi_priema?action=report">
<table align=center border=0>
<tr><td><font class="text_10">����&nbsp;����������:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="special1" tabindex="1">
      <html:option value="%">���</html:option>
      <html:option value="1">30 ����</html:option>
      <html:option value="2">5 �������</html:option>
      <html:option value="3">10 �������</html:option>
      <html:option value="4">17 �������</html:option>
      <html:option value="�">����, �� � �.�.</html:option>
    </html:select>
</td>
</tr>
<tr><td><font class="text_10">�����&nbsp;��������:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="formaOb" tabindex="2">
      <html:option value="�">�����</html:option>
      <html:option value="�">�������</html:option>
      <html:option value="�">����-�������</html:option>
      <html:option value="�">��������</html:option>
      <html:option value="�">�������������</html:option>
      <html:option value="�">����������</html:option>
      <html:option value="�">����</html:option>
      <html:option value="�">��</html:option>
    </html:select>
</td>
</tr>
<tr><td><font class="text_10">������&nbsp;��������:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="special2" tabindex="3">
      <html:option value="bud">���������</html:option>
      <html:option value="dog">����������</html:option>
    </html:select>
</td>
</tr>
<tr><td><font class="text_10">�������������&nbsp;�����:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="special5" tabindex="4">
      <html:option value="ev">���</html:option>
      <html:option value="e">���</html:option>
      <html:option value="v">������� ����</html:option>
    </html:select>
</td>
</tr>
<tr><td><font class="text_10">�����������:&nbsp;</font></td>
<td><html:select name="abit_SD" styleClass="select_f1" property="special3" tabindex="5">
      <html:option value="spec">�� ��������������</html:option>
      <html:option value="ukgr">�� ����������� �������</html:option>
    </html:select>
</td>
</tr>
<tr><td><font class="text_10">��&nbsp;��������&nbsp;����:&nbsp;</font></td>
<td><html:checkbox name="abit_SD" property="special4" tabindex="6"/>
</td>
</tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="7" value="������������ �����"/></td>
</html:form>
<html:form method="post" action="/rep_itogi_priema?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="8" value="�����"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>
</logic:present>