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
<template:put name='title'>Оператор системы: Корректировка данных</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Корректировка данных [[[]]]</template:put>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="copy_ld.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="copy_ld.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="copy_ld.jsp">&nbsp;Перенос&nbsp;личного&nbsp;дела&nbsp;</A></TD></A>
      <TD align=left><A href="copy_ld.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="apelljatsija.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="apelljatsija.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="apelljatsija.jsp">&nbsp;Апелляция&nbsp;</A></TD></A>
      <TD align=left><A href="apelljatsija.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="otsenki.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="otsenki.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="otsenki.jsp">&nbsp;Экзаменационные&nbsp;оценки&nbsp;</A></TD></A>
      <TD align=left><A href="otsenki.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="edit_rek.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="edit_rek.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="edit_rek.jsp">&nbsp;Рекоменд.&nbsp;к&nbsp;зачислению&nbsp;</A></TD></A>
      <TD align=left><A href="edit_rek.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="edit_sert.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="edit_sert.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="edit_sert.jsp">&nbsp;Баллы&nbsp;ЕГЭ&nbsp;</A></TD></A>
      <TD align=left><A href="edit_sert.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="edit_soch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="edit_soch.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="edit_soch.do">&nbsp;Выгрузка&nbsp;сочинений&nbsp;</A></TD></A>
      <TD align=left><A href="edit_soch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="oblasti.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="oblasti.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="oblasti.jsp">&nbsp;Области&nbsp;</A></TD></A>
      <TD align=left><A href="oblasti.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rajony.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rajony.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rajony.jsp">&nbsp;Районы&nbsp;</A></TD></A>
      <TD align=left><A href="rajony.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="punkty.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="punkty.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="punkty.jsp">&nbsp;Населенные&nbsp;пункты&nbsp;</A></TD></A>
      <TD align=left><A href="punkty.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="zavedenija.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="zavedenija.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="zavedenija.jsp">&nbsp;Учебные&nbsp;заведения&nbsp;</A></TD></A>
      <TD align=left><A href="zavedenija.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="payment_contract.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="payment_contract.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="payment_contract.jsp">&nbsp;Оплата&nbsp;договора&nbsp;</A></TD></A>
      <TD align=left><A href="payment_contract.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="index.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="index.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="index.jsp">&nbsp;Выход&nbsp;в&nbsp;меню&nbsp;</A></TD></A>
      <TD align=left><A href="index.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
</template:put>
<template:put name='content'>
<br>
<table cols=5 align=center border=0 cellSpacing=5>
<tr>
  <td width=30%>
  <td align=center valign=center width=40%><font class="text_11">Выберите нужный Вам пункт</font><td width=30%>
</tr>
</table>
</template:put>
</template:insert>