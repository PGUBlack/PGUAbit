<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<template:insert template="layouts/1/lay/layout_tiny.jsp">
<template:put name="title">����������� ���������!</template:put>
<template:put name="target_name">��������! ������������ Web-�������!</template:put>
<template:put name='content'>
<br><br>
<p align=center>
<FONT class="text_12">��� ������ � ��� "����������" ���������� ������������ <strong>������</strong> Internet Explorer!</FONT>
<br><br>
<table align=center border=0>
<tr>
<html:form action="/error.do?action=exit">
  <td align=center>
    <html:submit styleClass="button" property="exit" value="���� � �������"/>
  </td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>