<%@ page
    contentType="text/html;charset=windows-1251"
    language="java"
    import="org.apache.struts.taglib.template.util.*"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<html:html locale="true">
<head>
<link rel="stylesheet" type"text/css" href="layouts/3/img/style3.css">
<title>Абитуриент - <template:get name="title"/></title>
<META content="Автоматизированная система управления Абитуриент" name="description">
<META content="АСУ,Абитуриент,Автоматизированная система управления,ИАИС,ПГУ" name=keywords>
<META content="text/html; charset=windows-1251" http-equiv=Content-Type>
<META author="Потапов Алексей Александрович - ведущий программист ИВЦ ПГУ">
</head>
<body bgcolor=#E9DFBA>
<TABLE border=1 bordercolor="008040" cellPadding=0 cellSpacing=4 width="100%" height="100%">
  <TBODY>
    <TR>
      <TD>
        <TABLE background="" border=0 cellPadding=0 cellSpacing=0 width="100%">
          <TBODY>
           <TR vAlign="center">
             <TD bgColor="#272B22" height=43 align=middle width="100%">
               <font class="target_name"><template:get name="target_name"/></font>
             </TD>
           </TR>
          </TBODY>
        </TABLE>
      </TD>
    </TR>
    <TR vAlign="middle">
      <TD colspan=3 height=30 align="center" width="100%">
        <font class="sub_name">"<template:get name="sub_name"/>"</font>
      </TD>
    </TR>
    <TR valign=top height="100%">
      <TD colspan=3 align=middle width=100%>
       <TABLE background="" border=0 cellPadding=0 cellSpacing=0 width="100%">
         <TBODY>
           <TR><TD align=middle valign=top>
           <template:get name="content"/>
           </TD></TR>
         </TBODY>
       </TABLE><BR>
      </TD>
    </TR>
  </TBODY>
</TABLE>
</body>
</html:html>