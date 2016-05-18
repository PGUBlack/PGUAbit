<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>
<%@ page import="java.io.PrintWriter" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<html:html locale="true"> 
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="title">��� �������� ��������� �� ������ ����������</template:put>
<template:put name="sub_name">��������!</template:put>
<template:put name="target_name">�������� ����������</template:put>
<template:put name='content'>
<strong>
<p align=center><font class="text_18">
��������� �� ������ ���������� !!!</font>
</p>
<p align=center><html:image src="<%="layouts/"+tema+"/img/uc.jpg"%>" alt="�����������..."/></p>
</strong>
<br>
<p align=center>
<html:image src="<%="layouts/"+tema+"/img/uc3.gif"%>" alt="�����������..."/></p>
<TABLE border=0 cellPadding=0 cellSpacing=5>
<TBODY>
 <tr><td align=middle background="<%="layouts/"+tema+"/img/bar.gif"%>" height=27 width=180>
          <a class=menu href="index.jsp">����������</a></td></tr>
<tr><td height=6></td></tr>
</TBODY>
</TABLE>
<br>
</template:put>
</template:insert>