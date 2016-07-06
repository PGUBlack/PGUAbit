<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>���������: ������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">������</template:put>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="twins.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="twins.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="twins.jsp">&nbsp;��������&nbsp;�&nbsp;��������&nbsp;</A></TD></A>
      <TD align=left><A href="twins.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>

  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_lists_fakults.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_lists_fakults.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_lists_fakults.jsp">&nbsp;��&nbsp;�����������&nbsp;</A></TD></A>
      <TD align=left><A href="rep_lists_fakults.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_lists_done.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_lists_done.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_lists_done.jsp">&nbsp;������&nbsp;�&nbsp;�����.&nbsp;��.&nbsp;</A></TD></A>
      <TD align=left><A href="rep_lists_done.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_lists_edone.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_lists_edone.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_lists_edone.jsp">&nbsp;������&nbsp;�&nbsp;���.&nbsp;��.&nbsp;�&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="rep_lists_edone.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
<%---- ������ -----%>
  <TR><TD align=left><A href="lists_info.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lists_info.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lists_info.do">&nbsp;������&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="lists_info.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="reports.jsp">&nbsp;�����&nbsp;�&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
</template:put>
<template:put name='content'>
<br>
<table cols=5 align=center border=0 cellSpacing=5>
<tr><td></td></tr>
</table>
</template:put>
</template:insert>