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
<template:put name="sub_name">��������� �������</template:put>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="nazvanievuza.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="nazvanievuza.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="nazvanievuza.do">&nbsp;���������&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="nazvanievuza.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="otvetstvennyelitsa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="otvetstvennyelitsa.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="otvetstvennyelitsa.do">&nbsp;�������������&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="otvetstvennyelitsa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="kongruppa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="kongruppa.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="kongruppa.do">&nbsp;����������&nbsp;������&nbsp;</A></TD></A>
      <TD align=left><A href="kongruppa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="fakultety.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fakultety.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fakultety.do">&nbsp;����������&nbsp;</A></TD></A>
      <TD align=left><A href="fakultety.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="spetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="spetsialnosti.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="spetsialnosti.do">&nbsp;�������������&nbsp;</A></TD></A>
      <TD align=left><A href="spetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="nazvanijapredmetov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="nazvanijapredmetov.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="nazvanijapredmetov.do">&nbsp;��������&nbsp;</A></TD></A>
      <TD align=left><A href="nazvanijapredmetov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="ekzamenynaspetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="ekzamenynaspetsialnosti.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="ekzamenynaspetsialnosti.do">&nbsp;��������&nbsp;��&nbsp;����.&nbsp;</A></TD></A>
      <TD align=left><A href="ekzamenynaspetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="gruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="gruppy.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="gruppy.do">&nbsp;������&nbsp;</A></TD></A>
      <TD align=left><A href="gruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="raspisanie.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="raspisanie.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="raspisanie.do">&nbsp;����������&nbsp;</A></TD></A>
      <TD align=left><A href="raspisanie.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="kursy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="kursy.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="kursy.do">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="kursy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="lgoty.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lgoty.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lgoty.do">&nbsp;������&nbsp;</A></TD></A>
      <TD align=left><A href="lgoty.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="medali.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="medali.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="medali.do">&nbsp;�������&nbsp;</A></TD></A>
      <TD align=left><A href="medali.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="tselevojpriem.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="tselevojpriem.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="tselevojpriem.do">&nbsp;�������&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="tselevojpriem.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="badatt.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="badatt.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="badatt.do">&nbsp;����������.&nbsp;���.&nbsp;</A></TD></A>
      <TD align=left><A href="badatt.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
  <TR><TD align=left><A href="prioritet.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="prioritet.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="prioritet.do">&nbsp;����������&nbsp;</A></TD></A>
      <TD align=left><A href="prioritet.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
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