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
<link rel="stylesheet" type="text/css" href="layouts/1/img/style1.css">
<title>Абитуриент - <template:get name="title"/></title>
<meta http-equiv="refresh" content="1800">
<META content="Автоматизированная система управления Абитуриент" name="description">
<META content="АСУ,Абитуриент,Автоматизированная система управления,ИАИС,ПГУ" name=keywords>
<META content="text/html; charset=windows-1251" http-equiv=Content-Type>
<META author="Потапов Алексей Александрович - ведущий программист ИВЦ ПГУ">

 

</head>
<body bgcolor="#E9DFBA">
<TABLE background="" border="0" cellPadding="0" cellSpacing="0" width="100%">
  <TBODY>
   <TR>
     <TD width="100%">
       <TABLE background="" border="0" cellPadding="0" cellSpacing="0" width="100%">
         <TR>
           <TD align="right" valign="middle"><img src="layouts/1/img/sub_s_left.gif"></TD>
           <TD bgColor="#005836" height="27" align="center" vAlign="middle" width="100%">
             <font class="sub_target_name"><template:get name="target_name"/></font>
           </TD>
           <TD align="left" valign="middle"><img border="0" src="layouts/1/img/sub_s_right.gif"></TD>
         </TR> 
       </TABLE>
     </TD>
   </TR>
   <TR>
      <TD height="22"></TD>
   </TR>   
   <TR>
      <TD align="center" valign="middle" width="100%">
       <template:get name="content"/>
      </TD>
   </TR>
  </TBODY>
</TABLE>
</body>
</html:html>