<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<template:insert template="layouts/1/lay/layout_tiny.jsp">
<template:put name="title">Критическое замечание!</template:put>
<template:put name="target_name">ВНИМАНИЕ! Установка защитного соединения!</template:put>
<template:put name='content'>
<br><br>
<p align=center>
<FONT class="text_12">Для доступа к АСУ "Абитуриент" необходимо использовать защищенное SSL-соединение:</FONT>
<br><br>
<p align=center>
<a class="link_hov_blue" align=center href="https://iais.stup.ac.ru:8443/abiturient">https://iais.stup.ac.ru:8443/abiturient</a>
</strong>
<br>
</template:put>
</template:insert>