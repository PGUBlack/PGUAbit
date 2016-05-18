<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<bean:define id="tema" name="user" property="idTema"/>

<%-----------------------------------------------------------------%>
<%------------------ ��������� ������� -----------------%>
<%-----------------------------------------------------------------%>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>������ ������� ������������� ��������� ����������</template:put>
<template:put name="target_name">������ ������� ������������� ��������� ����������</template:put>
<template:put name='content'>
<table align="center" border="0">
<html:form action="all_country_olimp_diplom.do?action=create">
<html:text name="AllCountryOlimpDiplomBean"  property="name" size="32" maxlength="30" value=""/>
<br/>
<html:text name="family"  property="" size="32" maxlength="30" value="AllCountryOlimpDiplomBean.family"/>

    <td><html:submit styleClass="button" value="����" tabindex="2" property="add"/>&nbsp;&nbsp;</td>
    <td><html:submit styleClass="button" value="�����" tabindex="3" property="exit"/></td>
</html:form>
</table>
</template:put>
</template:insert>
