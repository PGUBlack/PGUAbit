<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/> 

<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name='title'>Справка по системе...</template:put>
<template:put name="target_name">Руководство пользователя</template:put>
<template:put name='content'>
<table border=0 cellSpacing=0 width=50%>
<tbody>

<tr><td height=10></td></tr>
<tr><td colspan=6 height=15 align=center valign=center>
    <font class="text_th_ab">ЗДЕСЬ БУДЕТ РАСПОЛАГАТЬСЯ:</font></td></tr>
<tr><td height=2></td></tr>
<tr><td colspan=6 align=center valign=center><font class="text_11">Руководство пользователя</font></td></tr>
<tr><td height=10></td></tr>
<tr><td colspan=6 height=15 align=center valign=center>
    <font class="text_th_ab">ЧАСТО ЗАДАВАЕМЫЕ ВОПРОСЫ</font></td></tr>
<tr><td height=2></td></tr>
<tr><td colspan=6 align=center valign=center><font class="text_11">и конечно же</font></td></tr>
<tr><td height=5></td></tr>
<tr><td colspan=6 align=center valign=center><font class="text_11">горячие клавиши</font></td></tr>

<tr><td height=10></td></tr>
<tr><td colspan=6 align=middle rowSpan=2 vAlign=top>
    <font class="link_b_11_A">&copy 2002-2004 ПГУ, ИВЦ</font></td></tr>
<tr><td height=6></td></tr>

</tbody>
</table>
<br>
<TABLE border=0 cellPadding=0 cellSpacing=5>
<TBODY>
 <tr><td align=middle background="img/bar.gif" height=27 width=180>
          <a class=menu onclick="window.close();" href="help.jsp">Выход из справки</a></td></tr>
<tr><td height=6></td></tr>
</TBODY>
</TABLE>
</template:put>
</template:insert>