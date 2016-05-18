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

<logic:notPresent name='indexViewAction' scope='request'>
 <logic:redirect forward='index'/>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%----- ���� � ������� ��� �������������� -----%>
<logic:equal name='index' scope='request' value='adminIndex'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>������������� �������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">������������� �������</template:put>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="moodle.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="moodle.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="moodle.jsp">&nbsp;Moodle&nbsp;</A></TD></A>
      <TD align=left><A href="moodle.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="adjustment.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="adjustment.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="adjustment.jsp">&nbsp;���������&nbsp;�������&nbsp;</A></TD></A>
      <TD align=left><A href="adjustment.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="fis_work.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_work.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_work.jsp">&nbsp;������&nbsp;�&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="fis_work.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="abit_s_ots.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_s_ots.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_s_ots.do">&nbsp;�����&nbsp;����.&nbsp;���&nbsp;��&nbsp;</A></TD></A>
      <TD align=left><A href="abit_s_ots.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
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
        <A class=menu href="gener_ege.do">&nbsp;������&nbsp;���&nbsp;</A></TD></A>
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
  <TR><TD align=left><A href="news.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="news.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="news.jsp">&nbsp;�������&nbsp;</A></TD></A>
      <TD align=left><A href="news.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="access.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="access.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="access.jsp">&nbsp;����������&nbsp;��������&nbsp;</A></TD></A>
      <TD align=left><A href="access.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">������&nbsp;���������</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
</template:put>
<template:put name='content'>
<logic:present name='user' scope='session'>
<logic:equal name='userGroup' property='typeId' value='0'>
<br>
<table border=0 sellPadding=0 sellSpacing=1>
<tbody>
<tr><td colspan=3 valign=middle height=35 width=50%><font class=text_11>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      ������������� ������� ����� ��������� ��������� ��������:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. ����������� �������, �������� ��������� �������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. �������������� ������:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ����������� ������ ������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      3. ��������� �������� � �������:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ��������� � ������������� �������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) �������� ������ �������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ������������� ������� ���������� � ��������</font></td>
</tbody>
</table>
</logic:equal>
</logic:present>
<BR>
</template:put>
</template:insert>
</logic:equal>

<%----- ���� � ������� ��� ��������� -----%>
<logic:equal name='index' scope='request' value='opIndex'>
<logic:equal name='userGroup' property='typeId' value='1'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>�������� �������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">�������� �������</template:put>
<logic:present name='user' scope='session'>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abiturient.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abiturient.do">&nbsp;��������&nbsp;����-��&nbsp;</A></TD></A>
      <TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="search.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="search.jsp">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
   <%---- ������ -----%>
  <TR><TD align=left><A href="abit_srch_online.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_srch_online.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_srch_online.do">&nbsp;�����&nbsp;������&nbsp;</A></TD></A>
      <TD align=left><A href="abit_srch_online.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  
   <%---- ������ -----%>
  <TR><TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_reports.jsp">&nbsp;�����&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
     <%---- ������ -----%>
  <TR><TD align=left><A href="abit_attr.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_attr.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_attr.jsp">&nbsp;�������������&nbsp;</A></TD></A>
      <TD align=left><A href="abit_attr.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="zachislen.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="zachislen.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="zachislen.jsp">&nbsp;����������&nbsp;�&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="zachislen.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_orders.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_orders.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_orders.jsp">&nbsp;�������&nbsp;</A></TD></A>
      <TD align=left><A href="rep_orders.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="reports.jsp">&nbsp;���������&nbsp;</A></TD></A>
      <TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">������&nbsp;���������</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
    <%---- ������ -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="checkEGE.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="checkEGE.jsp">��������&nbsp;���</A></TD></A>
      <TD align=left><A href="checkEGE.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
</template:put>
<template:put name='content'>
<br>
<table border=0 sellPadding=0 sellSpacing=1>
<tbody>
<tr><td colspan=3 valign=middle height=35 width=50%><font class=text_11>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      �������� ������� ����� ��������� ��������� ��������:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. ������� ������ �������� ������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. ����������� ��������������� ����� ������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      3. ������� � �������������� ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      4. �������������� �������� �����������:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) �������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ���������� ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ������� ���������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      5. ����������� ������ � ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
</tbody>
</table>
<BR>
</template:put>
</logic:present>
</template:insert>
</logic:equal>
</logic:equal>

<%----- ���� � ������� ��� ��������� ����� -----%>
<logic:equal name='index' scope='request' value='inIndex'>
<logic:equal name='userGroup' property='typeId' value='3'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>�������� �����</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">�������� �����</template:put>
<logic:present name='user' scope='session'>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abiturient.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abiturient.do">&nbsp;��������&nbsp;����-��&nbsp;</A></TD></A>
      <TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="search.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="search.jsp">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
    <%---- ������ -----%>
  <TR><TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_reports.jsp">&nbsp;�����&nbsp;����&nbsp;</A></TD></A>
      <TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
     <%---- ������ -----%>
     <%---- ������ -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">&nbsp;&nbsp;������&nbsp;���������&nbsp;&nbsp;</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
</template:put>
<template:put name='content'>
<br>
<table border=0 sellPadding=0 sellSpacing=1>
<tbody>
<tr><td colspan=3 valign=middle height=35 width=50%><font class=text_11>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      �������� ����� ����� ��������� ��������� ��������:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. ������� ������ �������� ������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. ����������� ��������������� ����� ������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      3. �������������� ������ ������ ������������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      4. ������������� ��������� ����������.</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      5. ����������� ������ ����������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
</tbody>
</table>
<BR>
</template:put>
</logic:present>
</template:insert>
</logic:equal>
</logic:equal>

<%----- ���� � ������� ��� ������ -----%>
<logic:equal name='index' scope='request' value='drIndex'>
<logic:equal name='userGroup' property='typeId' value='2'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>����������� �������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">����������� �������</template:put>
<logic:present name='user' scope='session'>
<template:put name="buttons">
  <%---- ������ -----%>
  <TR><TD align=left><A href="abit_srch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_srch.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_srch.do">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="abit_srch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="reports.jsp">&nbsp;���������&nbsp;</A></TD></A>
      <TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">������&nbsp;���������</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;�����&nbsp;</A></TD></A>
      <TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
</template:put>
<template:put name='content'>
<br>
<table border=0 sellPadding=0 sellSpacing=1>
<tbody>
<tr><td colspan=3 valign=middle height=35 width=50%><font class=text_11>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      ����������� ������� ����� ��������� ��������� ��������:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. ����������� ������ ���� ����������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. ������������� ������ � ���� �������� ��������:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ��������������� ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) �������� ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ������ � ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ����������� �����</font></td></tr>
</tbody>
</table>
<BR>
</template:put>
</logic:present>
</template:insert>
</logic:equal>
</logic:equal>

<%----- ����� ������ ������������ -----%>
<logic:equal name='index' scope='request' value='pubIndex'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">����������� ������������</template:put>
<template:put name="sub_name">����� ������</template:put>
<template:put name='title'>������ ����������� ������������!</template:put>
<template:put name='content'>
  <table align=center border=0 cellspacing=0 cellPadding=0>
    <tr><td width=20%></td></tr>
    <tr><td><td height="220"><td width="20%"></td></tr>
  </table>
</template:put>
</template:insert>
</logic:equal>