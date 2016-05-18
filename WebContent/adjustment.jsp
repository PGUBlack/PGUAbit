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
<template:put name='title'>Администратор системы: Настройка системы</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Настройка системы</template:put>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="nazvanievuza.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="nazvanievuza.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="nazvanievuza.do">&nbsp;Реквизиты&nbsp;ВУЗа&nbsp;</A></TD></A>
      <TD align=left><A href="nazvanievuza.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="otvetstvennyelitsa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="otvetstvennyelitsa.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="otvetstvennyelitsa.do">&nbsp;Ответственные&nbsp;лица&nbsp;</A></TD></A>
      <TD align=left><A href="otvetstvennyelitsa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="kongruppa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="kongruppa.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="kongruppa.do">&nbsp;Конкурсные&nbsp;группы&nbsp;</A></TD></A>
      <TD align=left><A href="kongruppa.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="fakultety.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fakultety.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fakultety.do">&nbsp;Факультеты&nbsp;</A></TD></A>
      <TD align=left><A href="fakultety.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="spetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="spetsialnosti.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="spetsialnosti.do">&nbsp;Специальности&nbsp;</A></TD></A>
      <TD align=left><A href="spetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="nazvanijapredmetov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="nazvanijapredmetov.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="nazvanijapredmetov.do">&nbsp;Предметы&nbsp;</A></TD></A>
      <TD align=left><A href="nazvanijapredmetov.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="ekzamenynaspetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="ekzamenynaspetsialnosti.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="ekzamenynaspetsialnosti.do">&nbsp;Экзамены&nbsp;на&nbsp;спец.&nbsp;</A></TD></A>
      <TD align=left><A href="ekzamenynaspetsialnosti.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="gruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="gruppy.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="gruppy.do">&nbsp;Группы&nbsp;</A></TD></A>
      <TD align=left><A href="gruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="raspisanie.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="raspisanie.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="raspisanie.do">&nbsp;Расписание&nbsp;</A></TD></A>
      <TD align=left><A href="raspisanie.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="kursy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="kursy.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="kursy.do">&nbsp;Курсы&nbsp;</A></TD></A>
      <TD align=left><A href="kursy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="lgoty.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lgoty.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lgoty.do">&nbsp;Льготы&nbsp;</A></TD></A>
      <TD align=left><A href="lgoty.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="medali.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="medali.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="medali.do">&nbsp;Отличия&nbsp;</A></TD></A>
      <TD align=left><A href="medali.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="tselevojpriem.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="tselevojpriem.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="tselevojpriem.do">&nbsp;Целевой&nbsp;прием&nbsp;</A></TD></A>
      <TD align=left><A href="tselevojpriem.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="badatt.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="badatt.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="badatt.do">&nbsp;Недействит.&nbsp;атт.&nbsp;</A></TD></A>
      <TD align=left><A href="badatt.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  
  <TR><TD align=left><A href="prioritet.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="prioritet.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="prioritet.do">&nbsp;Приоритеты&nbsp;</A></TD></A>
      <TD align=left><A href="prioritet.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
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
</template:insert>