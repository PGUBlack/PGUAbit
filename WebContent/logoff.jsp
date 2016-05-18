<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java" %>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='logoutViewAction' scope='request'>
  <logic:forward name='logout'/>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">����� �� �������</template:put>
<template:put name="sub_name">�������������</template:put>
<template:put name='title'>����� �� �������</template:put>
<template:put name='content'>
<BR>
 <html:form action="logout.do" method='POST' name='logonForm' type='abit.action.LoginForm'> 
 <table border=0 cellpadding=0 cellspacing=0 align=center>
 <tr>
   <td colspan=2 align=center>
     <font class="text_11">���� ��������������� ������</font>
   </td>
 </tr>
 <tr><td colspan=2><hr></td>
 <tr>
   <td><font class="text_11">&nbsp;���:</font></td>
   <td><font class="text_11">
       <html:text readonly="true" styleClass="text_f11" name="user" property="name" tabindex="-1"/></td>
 </tr>
 <tr>
   <td><font class="text_11">&nbsp;������:</font></td>
   <td><font class="text_11">
       <html:text readonly="true" styleClass="text_f11" name="user" property="group.groupName" tabindex="-1"/></td>
 </tr>
 <tr>
   <td colspan=2 align=center>
   <hr>
   </td>
 </tr>
 <tr><td height=10></td></tr>
 <tr>
   <td colspan=2 align=center>
     <html:submit styleClass="button">�����</html:submit>
     &nbsp;&nbsp;&nbsp;
     <html:submit styleClass="button" property="back">�������</html:submit>
   </td>
 </tr>
 </table>
 <BR>
</html:form>
</template:put>
</template:insert>