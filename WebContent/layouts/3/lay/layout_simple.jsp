<%@ page
    contentType="text/html;charset=windows-1251"
    language="java"
    import="org.apache.struts.taglib.template.util.*"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<%
    String     refer   = null;
    String     refresh = null;
    ContentMap map   = ContentMapStack.peek(pageContext);
    Content    content;
    if( (content=map.get("refer")) != null && content.isDirect() ) refer = content.toString();
    if( (content= map.get("refresh")) != null && content.isDirect() ) refresh = content.toString();
%>
<html:html locale="true">
<head>
<% if (refresh!=null) { %>
   <META HTTP-EQUIV="Refresh" CONTENT="<%=refresh%>">
   <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<% } %>
<link rel="stylesheet" type"text/css" href="layouts/3/img/style3.css">
<title>Абитуриент - <template:get name="title"/></title>
<META content="Автоматизированная система управления Абитуриент" name="description">
<META content="АСУ,Абитуриент,Автоматизированная система управления,ИАИС,ПГУ" name='keywords'>
<META content="text/html; charset=windows-1251" http-equiv=Content-Type>
<META author="Потапов Алексей Александрович - ведущий программист ИВЦ ПГУ">
</head>
<body>
<TABLE border=1 bordercolor="008040" cellPadding=0 cellSpacing=4 width="100%" height="100%">
  <TBODY>
   <TR>
     <TD width=100%>
       <TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" height=10%>
         <TBODY>
           <TR>
             <TD rowspan="2" valign="middle" align="center" width="10%">
               <A target="_self" href="index.jsp">
                 <img height="90" width="112" border="0" alt="Главное меню" src="layouts/3/img/pgu_1k.gif">
               </A>
             </TD>
             <TD align=middle bgcolor="#272B22" vAlign="middle" width="100%"><font class="target_name">
               <template:get name="target_name"/></font>
             </TD>
           </TR>
           <TR>
             <TD bgcolor="#272B22" vAlign="middle" align=center>
               <font class="sub_name">"<template:get name="sub_name"/>"</font>
             </TD>
           </TR>
         </TBODY>
       </TABLE>
     </TD>
   </TR>
   <TR height=90%>
     <TD width=100%>
       <TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" height=100%>
         <TBODY>
           <TR>
             <TD valign='top' align=middle width="100%" height=100%>
               <!-- Содержимое страниц --><BR>
               <template:get name="content"/><BR>
             </TD>
           </TR>
         </TBODY>
       </TABLE>
     </TD>
   </TR>
  </TBODY>
</TABLE>
</body>
</html:html>