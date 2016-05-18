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
  <TR><TD align=left><A href="umu_excel_2016_2111.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2111.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2111.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.1&nbsp;�����&nbsp;1&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2111.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
      <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2112.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2112.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2112.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.1&nbsp;�����&nbsp;2&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2112.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
        <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2121.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2121.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2121.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.2&nbsp;�����&nbsp;1&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2121.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
          <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2122.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2122.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2122.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.2&nbsp;�����&nbsp;2&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2122.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
            <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2131.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2131.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2131.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.3&nbsp;�����&nbsp;1&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2131.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
              <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2132.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2132.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2132.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.3&nbsp;�����&nbsp;2&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2132.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
              <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2141.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2141.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2141.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.4&nbsp;�����&nbsp;1&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2141.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
                <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2142.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2142.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2142.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.1.4&nbsp;�����&nbsp;2&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2142.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
  
                      <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_23.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_23.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_23.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.3&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_23.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
                           <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_24.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_24.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_24.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.4&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_24.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
                           <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_26.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_26.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_26.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.6&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_26.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
                         <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_27.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_27.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_27.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.7&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_27.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
                       <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_28.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_28.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_28.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.8&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_28.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
   
                    <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2_10.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2_10.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2_10.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.10&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2_10.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
                  <%---- ������ -----%>
  <TR><TD align=left><A href="umu_excel_2016_2_11.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_excel_2016_2_11.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_excel_2016_2_11.do">&nbsp;�����&nbsp;2&nbsp;������&nbsp;2.11&nbsp;</A></TD></A>
      <TD align=left><A href="umu_excel_2016_2_11.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
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