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
<template:put name='title'>������������� �������: ��������� �������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">������ � ��� ��� � �����</template:put>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="fis_connect.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_connect.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_connect.do">&nbsp;��������&nbsp;�������&nbsp;�&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="fis_connect.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
   <TR><TD height=5></TD></TR>
  <%---- �������� ������ --%>
  <TR><TD align=left><A href="fis_import.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_connect.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_import.do">&nbsp;������&nbsp;���������&nbsp;�&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="fis_import.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  
     <TR><TD height=5></TD></TR>
  <%---- �������� ������ --%>
  <TR><TD align=left><A href="fis_compaign.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_compaign.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_compaign.do">&nbsp;������&nbsp;��������&nbsp;��������&nbsp;�&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="fis_compaign.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  
  <TR><TD height=5></TD></TR>
  
  <%---- �������� ������ --%>
  <TR><TD align=left><A href="fis_delete.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_delete.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_delete.do">&nbsp;��������&nbsp;���������&nbsp;��&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="fis_delete.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  
  
  <TR><TD height=5></TD></TR>
  
  
     <%---- �������� ������ 2--%>
  <TR><TD align=left><A href="fis_interface.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_interface.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_interface.do">&nbsp;���&nbsp;���������&nbsp;</A></TD></A>
      <TD align=left><A href="fis_interface.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  
  
  <TR><TD height=5></TD></TR>
  
    <%---- �������� ������ 2--%>
  <TR><TD align=left><A href="fis_bd_update.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_bd_update.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_bd_update.do">&nbsp;������&nbsp;�������&nbsp;���&nbsp;������&nbsp;</A></TD></A>
      <TD align=left><A href="fis_bd_update.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
    <TR><TD height=5></TD></TR>
  
    <%---- ������ --%>
  <TR><TD align=left><A href="fis_marks.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_marks.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_marks.do">&nbsp;������&nbsp;���&nbsp;&nbsp;</A></TD></A>
      <TD align=left><A href="fis_marks.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
    
    
    <TR><TD height=5></TD></TR>
  
    <%---- �������� ������ 2--%>
  <TR><TD align=left><A href="fis_adm.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_adm.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_adm.do">&nbsp;������&nbsp;���������&nbsp;������&nbsp;</A></TD></A>
      <TD align=left><A href="fis_adm.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  
  <TR><TD height=5></TD></TR>
  
  <%---- ������ Max-----%>
  <TR><TD align=left><A href="fis_pr.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_pr.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_pr.do">&nbsp;������&nbsp;��������&nbsp;�&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="fis_pr.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
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