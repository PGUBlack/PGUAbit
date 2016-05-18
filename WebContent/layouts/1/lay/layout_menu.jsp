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
<META content="АСУ,Абитуриент,Автоматизированная система управления,ИАИС,ПГУ" name='keywords'>
<META content="text/html; charset=windows-1251" http-equiv=Content-Type>
<META author="Потапов Алексей Александрович - ведущий программист ИВЦ ПГУ">
</head>
<body bgcolor="#E9DFBA">
<TABLE background="" border="0" cellPadding="0" cellSpacing="0" width="100%">
  <TBODY>
   <TR>
      <TD valign="top" align="right">
        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
          <TBODY>
            <TR><TD height="30" align="center"></TD></TR>
            <TR><TD valign="top"><img src="layouts/1/img/left.gif"></TD></TR>
          </TBODY>
        </TABLE>
      </TD>
      <TD align="center" valign="top" width="10%">
        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
          <TBODY>
            <TR>
              <TD valign="top">
                <TABLE border="0" cellPadding="0" cellSpacing="0">
                  <TBODY>
                    <TR>
                      <TD valign="top" width="30%">
                        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
                          <TBODY>
                            <TR>
                              <TD height="30" align="center"></TD>
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
                      <TD valign="top" width="40%">
                        <TABLE border="0" cellPadding="0" cellSpacing="0">
                          <TBODY>
                            <TR>
                              <TD><A target="_self" href="index.jsp"><img height="140" alt="Главное меню" border="0" src="layouts/1/img/pgu_1k.jpg"></a></TD>
                            </TR>
                          </TBODY>
                        </TABLE>
                      </TD>
                      <TD valign="top" width="30%">
                        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
                          <TBODY>
                            <TR>
                              <TD height="30" align="center"></TD>
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
            <TR>
               <TD><BR>
                   <TABLE border="0" cellPadding="0" cellSpacing="0" >
                     <TBODY>
                       <template:get name='buttons'/>
                     </TBODY>
                   </TABLE>
               </TD>
            </TR>
          </TBODY>
        </TABLE>
      </TD>
      <TD align="center" vAlign="top" width="100%">
         <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
           <TBODY>
             <TR><TD height="30" align="center"></TD></TR>
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
                <TD align="center" height="1" width="100%" background="layouts/1/img/pearl.gif" colspan="3">&nbsp;</TD>
             </TR>
             <TR>
                <TD align="center" vAlign="top" width="100%">
                <!-- Содержимое страниц -->
                <template:get name="content"/><BR>
                </TD>
             </TR>
           </TBODY>
         </TABLE>
      </TD>
      <TD valign="top" align="left">
        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
          <TBODY>
            <TR><TD height="30" align="center"></TD></TR>
            <TR><TD align="left" valign="top"><img src="layouts/1/img/right.gif"></TD></TR>
          </TBODY>
        </TABLE>
      </TD>
   </TR>
  </TBODY>
</TABLE>
</body>
</html:html>