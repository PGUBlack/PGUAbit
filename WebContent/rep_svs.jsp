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
<template:put name='title'>Документы: Сводки</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Сводки</template:put>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_analiz_kg.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_analiz_kg.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_analiz_kg.jsp">&nbsp;Аналитический&nbsp;прогноз&nbsp;</A></TD></A>
      <TD align=left><A href="rep_analiz_kg.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="raspredballs.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="raspredballs.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="raspredballs.jsp">&nbsp;Распределение&nbsp;по&nbsp;баллам&nbsp;</A></TD></A>
      <TD align=left><A href="raspredballs.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_summary.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_summary.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_summary.jsp">&nbsp;По&nbsp;экзамену&nbsp;и&nbsp;по&nbsp;дате&nbsp;</A></TD></A>
      <TD align=left><A href="rep_summary.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_ball_potok.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_ball_potok.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_ball_potok.jsp">&nbsp;По&nbsp;баллам&nbsp;для&nbsp;факультетов&nbsp;</A></TD></A>
      <TD align=left><A href="rep_ball_potok.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_ball_potok_kon_grp.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_ball_potok_kon_grp.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_ball_potok_kon_grp.jsp">&nbsp;По&nbsp;баллам&nbsp;для&nbsp;конк.&nbsp;групп&nbsp;</A></TD></A>
      <TD align=left><A href="rep_ball_potok_kon_grp.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="reports.jsp">&nbsp;Выход&nbsp;в&nbsp;меню&nbsp;</A></TD></A>
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