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
<body>
<TABLE border=1 bordercolor="008040" cellPadding=0 cellSpacing=4 width="100%" height="100%">
  <TBODY>
   <TR>
     <TD width=100%>
       <TABLE background="" border=0 cellPadding=0 cellSpacing=0 width="100%">
         <TBODY>
          <TD align=right border=0 valign=middle></TD>
          <TD height=32 bgcolor="#272B22" align=middle vAlign=center width="100%">
            <font class="target_name"><template:get name="target_name"/></font>
          </TD>
         </TBODY>
       </TABLE>
     </TD>
   </TR>
   <TR height="100%" valign=top>
      <TD align=middle width=100%><BR>
       <template:get name="content"/><BR>
      </TD>
   </TR>
  </TBODY>
</TABLE>
</body>
</html:html>