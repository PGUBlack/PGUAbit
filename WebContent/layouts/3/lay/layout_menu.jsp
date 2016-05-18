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
<META content="АСУ,Абитуриент,Автоматизированная система управления,ИАИС,ПГУ" name='keywords'>
<META content="text/html; charset=windows-1251" http-equiv=Content-Type>
<META author="Потапов Алексей Александрович - ведущий программист ИВЦ ПГУ">
</head>
<body leftmargin="0" topmargin="0">
<TABLE background="" border=0 width="100%" height="100%">
  <TBODY>
   <TR>
      <TD align=middle valign=top width="10%" height="100%">
        <TABLE border=1 bordercolor="008040" cellPadding=0 cellSpacing=4 width="100%" height="100%">
          <TBODY>
            <TR>
              <TD valign=top>
                <TABLE border=0 cellPadding=0 cellSpacing=0>
                  <TBODY>
                    <TR>
                      <TD valign=middle width=30%>
                        <TABLE border=0 cellPadding=0 cellSpacing=0 width=100%>
                          <TBODY>
                            <TR valign="middle">
                              <TD align=center width=100%><A target="_self" border=0 href="index.jsp">
                                <img src="layouts/3/img/pgu_1k_big.gif" alt="Главное меню" height="132" width="190" border="0"></A>
                              </TD>
                            </TR>
                          </TBODY>
                        </TABLE>
                      </TD>
                    </TR>
                  </TBODY>
                </TABLE>
              </TD>
            </TR>
            <TR height="100%">
               <TD valign="top"><BR>
                   <TABLE border=0 cellPadding=0 cellSpacing=0>
                     <TBODY>
                       <template:get name='buttons'/>
                     </TBODY>
                   </TABLE>
               </TD>
            </TR>
          </TBODY>
        </TABLE>
      </TD>
      <TD align=middle vAlign=top width=100% height="100%">
         <TABLE border=1 bordercolor="008040" cellPadding=0 cellSpacing=4 width="100%" height="100%">
           <TBODY>
             <TR>
                 <TD align=middle height=70 vAlign=center width="100%"><font class="target_name">
                   <img src="layouts/3/img/decanat.gif" height="98" width="427"></font>
                 </TD>
             </TR>
             <TR>
                 <TD height=30 vAlign=middle align=center>
                   <font class="sub_name">"<template:get name="sub_name"/>"</font>
                 </TD>
             </TR>
             <TR height="100%">
                <TD align=middle vAlign=top width="100%">
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