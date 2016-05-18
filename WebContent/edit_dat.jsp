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
<template:put name='title'>������������� �������: �������������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">������������� ������</template:put>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="fgruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fgruppy.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fgruppy.do">&nbsp;������������&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="fgruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="gener_ege.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="gener_ege.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="gener_ege.do">&nbsp;�������&nbsp;������&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="gener_ege.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="sqlrep.do?action=save"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="sqlrep.do?action=save"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="sqlrep.do?action=save">&nbsp;�������&nbsp;��&nbsp;</A></TD></A>
      <TD align=left><A href="sqlrep.do?action=save"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="sqlrep.do?action=old"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="sqlrep.do?action=old"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="sqlrep.do?action=old">&nbsp;������&nbsp;��&nbsp;</A></TD></A>
      <TD align=left><A href="sqlrep.do?action=old"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="index.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="index.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="index.jsp">&nbsp;�����&nbsp;�&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="index.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
</template:put>
</template:insert>