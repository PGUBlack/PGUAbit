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
<template:put name='title'>���������</template:put>
<template:put name="target_name">��� "����������"</template:put>
<template:put name="sub_name">���������</template:put>
<template:put name="buttons">
  <%---- ������ -----%>

 

  <%---- ������ -----%>
 

  </TR>
  
  <%---- ������ -----%>
  <TR><TD align=left><A href="umu_kolvo_zajav.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_kolvo_zajav.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_kolvo_zajav.do">&nbsp;��������&nbsp;���������&nbsp;(�����������/�����������)&nbsp;</A></TD></A>
      <TD align=left><A href="umu_kolvo_zajav.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>

  
  <%---- ������ -----%>
  <TR><TD align=left><A href="umu_kolvo_zajav2.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_kolvo_zajav2.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_kolvo_zajav2.do">&nbsp;��������&nbsp;���������&nbsp;(������������)&nbsp;</A></TD></A>
      <TD align=left><A href="umu_kolvo_zajav2.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>

    <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_f1.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_f1.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_f1.do">&nbsp;�����&nbsp;1&nbsp;������&nbsp;1.2&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_f1.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
   <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_f3.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_f3.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_f3.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_f3.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  
     <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_f4.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_f4.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_f4.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.3&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_f4.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  
    <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2_4_1_.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2_4_1_.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2_4_1_.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.4.1&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2_4_1_.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
    <%---- ������ -----%>
    
     <%---- ������ -----%>
  <TR><TD align=left><A href="or.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="or.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="or.do">&nbsp;��.����&nbsp;���&nbsp;</A></TD></A>
      <TD align=left><A href="or.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
  
      <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_f2.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_f2.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_f2.do">&nbsp;���&nbsp;�����&nbsp;Excel&nbsp;F2&nbsp;�������&nbsp;����</A></TD></A>
      <TD align=left><A href="umu_excel_f2.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>

  
  
 
  <%---- ������ -----%>
    <TR><TD align=left><A href="abit_srch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_srch.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_srch.do">&nbsp;���&nbsp;����������&nbsp;Excel&nbsp;</A></TD></A>
      <TD align=left><A href="abit_srch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="rep_browser.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_browser.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_browser.jsp">&nbsp;����&nbsp;����������&nbsp;</A></TD></A>
      <TD align=left><A href="rep_browser.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- ������ -----%>
  <TR><TD align=left><A href="soch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="soch.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="soch.do">&nbsp;�����&nbsp;��&nbsp;���������&nbsp;</A></TD></A>
      <TD align=left><A href="soch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
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
<template:put name='content'>
<br>
<table border=0 sellPadding=0 sellSpacing=1>
<tbody>
<tr><td colspan=3 valign=middle height=35 width=50%><font class=text_11>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      � ������ ������� ����� ��������� ��������� ��������:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. ����������� ������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. ����������� ������ � �������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      3. ��� ����� ��������� ������� ��������������� ��������� � ����� ��:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ���������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ��������� ������ </font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;�) ��������</font></td>
<tr><td valign=middle width=90%><font class=text_11>
</tbody>
</table>
</template:put>
</template:insert>