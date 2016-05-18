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
<template:put name='title'>Документы: Отчеты</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Формы</template:put>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_2.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_2.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_2.do?action=report">&nbsp;Форма&nbsp;2&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_2.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_2f.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_2f.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_2f.do?action=report">&nbsp;Форма&nbsp;2Ф&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_2f.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_2k.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_2k.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_2k.do?action=report">&nbsp;Форма&nbsp;2К&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_2k.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_2z.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_2z.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_2z.do?action=report">&nbsp;Форма&nbsp;2Z&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_2z.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_6.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_6.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_6.do?action=report">&nbsp;Форма&nbsp;6&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_6.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_6f.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_6f.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_6f.do?action=report">&nbsp;Форма&nbsp;6Ф&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_6f.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_6k.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_6k.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_6k.do?action=report">&nbsp;Форма&nbsp;6K&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_6k.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_6z.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_6z.do?action=report"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_6z.do?action=report">&nbsp;Форма&nbsp;6Z&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_6z.do?action=report"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_7.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_7.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_7.do">&nbsp;Форма&nbsp;7&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_7.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_forma_8.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_forma_8.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_forma_8.do">&nbsp;Форма&nbsp;8&nbsp;</A></TD></A>
      <TD align=left><A href="rep_forma_8.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_itogi_priema.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_itogi_priema.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_itogi_priema.do">&nbsp;Итоги&nbsp;приёма&nbsp;</A></TD></A>
      <TD align=left><A href="rep_itogi_priema.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR> 
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_itogi_zach.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_itogi_zach.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_itogi_zach.do">&nbsp;Итоги&nbsp;зачисления&nbsp;</A></TD></A>
      <TD align=left><A href="rep_itogi_zach.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
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