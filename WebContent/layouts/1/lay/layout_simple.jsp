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
<% } else {%>
   <meta http-equiv="refresh" content="1800">
<% }%>
<link rel="stylesheet" type"text/css" href="layouts/1/img/style1.css">
<title>Абитуриент - <template:get name="title"/></title>
<META content="Автоматизированная система управления Абитуриент" name="description">
<META content="АСУ,Абитуриент,Автоматизированная система управления,ИАИС,ПГУ" name='keywords'>
<META content="text/html; charset=windows-1251" http-equiv=Content-Type>
<META author="Потапов Алексей Александрович - ведущий программист ИВЦ ПГУ">
</head>
<body bgcolor="#E9DFBA">
<TABLE background="" border="0" cellPadding="0" cellSpacing="0" width="100%" height="100%">
  <TBODY>
   <TR>
      <TD valign="top" align="right">
        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
          <TBODY>
            <TR><TD height="50" align="center"></TD></TR>
            <TR><TD valign="top"><img src="layouts/1/img/left.gif"></TD></TR>
          </TBODY>
        </TABLE>
      </TD>
      <TD width="10%" height="100%">
        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%" height="100%">
          <TBODY>
            <TR>
              <TD valign="top" height="100%">
                <TABLE border="0" cellPadding="0" cellSpacing="0" height="100%">
                  <TBODY>
                    <TR>
                      <TD valign="top" width="30%">
                        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
                          <TBODY>
                            <TR>
                              <TD height="50" align="center"></TD>
                            </TR>
                            <TR>
                              <TD height="70" align="center" width="100%" bgColor="#005836">&nbsp;&nbsp;</TD>
                            </TR>
                            <TR>
                              <TD height="30" align="center"></TD>
                            </TR>
                          </TBODY>
                        </TABLE>
                      </TD>
                      <TD valign="top" width="40%" height="100%">
                        <TABLE border="0" cellPadding="0" cellSpacing="0" height="100%" width="100%">
                          <TBODY>
                            <TR>
                               <TD align="center" width="30%" valign="middle"></TD>
                               <TD align="center" width="70" valign="bottom"><img src="layouts/1/img/top.gif"></TD>
                               <TD align="center" width="30%" valign="middle"></TD>
                            </TR>
                            <TR>
                               <TD colspan="3"><A target="_self" href="index.jsp"><img height="140" border="0" alt="Главное меню" src="layouts/1/img/pgu_1k.jpg"></a></TD>
                            </TR>
                            <TR>
                               <TD valign="top" width="40%" height="100%" colspan="3">
                                 <TABLE border="0" cellPadding="0" cellSpacing="0" height="100%" width="100%">
                                   <TBODY>
                                     <TR>
                                       <TD align="center" width="30%" height="100%" valign="middle"></TD>
                                       <TD align="center" width="70" height="100%" bgColor="#005836" valign="top">&nbsp;</TD>
                                       <TD align="center" width="30%" height="100%" valign="middle"></TD>
                                     </TR>
                                     <TR>
                                       <TD align="center" width="30%" height="100%" valign="middle"></TD>
                                       <TD align="center" width="70" height="100%" valign="top"><img src="layouts/1/img/down.gif"></TD>
                                       <TD align="center" width="30%" height="100%" valign="bottom" background="layouts/1/img/pearl.gif">&nbsp;</TD>
                                     </TR>
                                   </TBODY>
                                 </TABLE>
                               </TD>
                            </TR>
                          </TBODY>
                        </TABLE>
                      </TD>
                      <TD valign="top" width="30%">
                        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
                          <TBODY>
                            <TR>
                              <TD height="50" align="center"></TD>
                            </TR>
                            <TR>
                              <TD height="70" align="center" width="100%" bgColor="#005836"></TD>
                            </TR>
                            <TR>
                              <TD height="30" align="center"></TD>
                            </TR>
                          </TBODY>
                        </TABLE>
                      </TD>
                    </TR>
                  </TBODY>
                </TABLE>
              </TD>
            </TR>
          </TBODY>
        </TABLE>
      </TD>
      <TD width="100%" height="100%">
         <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%" height="100%">
           <TBODY>
             <TR><TD height="50" align="center"></TD></TR>
             <TR>
                 <TD align="center" bgColor="#005836" height="70" vAlign="middle" width="100%"><font class="target_name">
                   <template:get name="target_name"/></font>
                 </TD>
             </TR>
             <TR>
                 <TD height="30" vAlign="bottom" align="center">
                   <font class="sub_name"><template:get name="sub_name"/></font>
                 </TD>
             </TR>
             <TR>  
                 <TD valign='top' align="center" width="100%" height="100%"><BR>
                   <!-- Содержимое страниц -->
                   <template:get name="content"/><BR>
                 </TD>
             </TR>
             <TR>
                <TD align="center" width="100%" height="100%" valign="bottom" background="layouts/1/img/pearl.gif">&nbsp;</TD>
             </TR>
           </TBODY>
         </TABLE>
      </TD>
      <TD valign="top" align="left">
        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%" height="100%">
          <TBODY>
            <TR><TD height="50" align="center"></TD></TR>
            <TR><TD align="left" valign="top"><img src="layouts/1/img/right.gif"></TD></TR>
            <TR><TD background="layouts/1/img/pearl_v.gif" height="100%" width="100%">&nbsp;</TD></TR>
          </TBODY>
        </TABLE>
      </TD>
   </TR>
  </TBODY>
</TABLE>
</body>
</html:html>