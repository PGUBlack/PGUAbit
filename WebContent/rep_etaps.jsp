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
<template:put name='title'>����� ����������: ������ ������������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">����� ����������: ������ ������������</template:put>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_wave_first.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_wave_first.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_wave_first.do">&nbsp;����1&nbsp;(��������������)&nbsp;</A></TD></A>
      <TD align=left><A href="rep_wave_first.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_wave_second.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_wave_second.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_wave_second.do">&nbsp;����1&nbsp;</A></TD></A>
      <TD align=left><A href="rep_wave_second.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_wave_third.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_wave_third.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_wave_third.do">&nbsp;����2&nbsp;(��������������)&nbsp;</A></TD></A>
      <TD align=left><A href="rep_wave_third.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_wave_fourth.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_wave_fourth.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_wave_fourth.do">&nbsp;����2&nbsp;</A></TD></A>
      <TD align=left><A href="rep_wave_fourth.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_wave_fifth.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_wave_fifth.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_wave_fifth.do">&nbsp;����2&nbsp;(�����)&nbsp;</A></TD></A>
      <TD align=left><A href="rep_wave_fifth.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="ukaction.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="ukaction.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="ukaction.do">&nbsp;������&nbsp;������������&nbsp;</A></TD></A>
      <TD align=left><A href="ukaction.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
   <%---- ������ -----%>
  <TR><TD align=left><A href="ord.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="ord.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="ord.do">&nbsp;������&nbsp;����������&nbsp;</A></TD></A>
      <TD align=left><A href="ord.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="asp.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="asp.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="asp.do">&nbsp;������&nbsp;�����������&nbsp;</A></TD></A>
      <TD align=left><A href="asp.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
   <%---- ������ -----%>
  <TR><TD align=left><A href="int.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="int.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="int.do">&nbsp;������&nbsp;�����������&nbsp;</A></TD></A>
      <TD align=left><A href="int.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="mk.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="mk.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="mk.do">&nbsp;������&nbsp;��&nbsp;</A></TD></A>
      <TD align=left><A href="mk.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="krym.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="krym.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="krym.do">&nbsp;������&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="krym.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="reports.jsp">&nbsp;�����&nbsp;�&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  
  <TR><TD height=25></TD></TR>
  
  <TR><TD align=left><A href="zachLgotnikov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="zachLgotnikov.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="zachLgotnikov.do">&nbsp;���������&nbsp;����������&nbsp;�&nbsp;���������&nbsp;</A></TD></A>
      <TD align=left><A href="zachLgotnikov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=25></TD></TR>
   <%---- ������ -----%>
  <TR><TD align=left><A href="mali.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="mali.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="mali.do">&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="mali.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
   <%----
  <TR><TD align=left><A href="zachZelevikov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="zachZelevikov.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="zachZelevikov.do">&nbsp;���������&nbsp;���������&nbsp;</A></TD></A>
      <TD align=left><A href="zachZelevikov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>  ----%>
  
  <TR><TD height=5></TD></TR>
</template:put>
<template:put name='content'>
<br>
<table cols=5 align=center border=0 cellSpacing=5>
<tr><td></td></tr>
</table>
</template:put>
</template:insert>