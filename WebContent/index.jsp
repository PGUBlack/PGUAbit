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

<%----- ВХОД В СИСТЕМУ ДЛЯ АДМИНИСТРАТОРА -----%>
<logic:equal name='index' scope='request' value='adminIndex'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>Администратор системы</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Администратор системы</template:put>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="moodle.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="moodle.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="moodle.jsp">&nbsp;Moodle&nbsp;</A></TD></A>
      <TD align=left><A href="moodle.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="adjustment.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="adjustment.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="adjustment.jsp">&nbsp;Настройка&nbsp;системы&nbsp;</A></TD></A>
      <TD align=left><A href="adjustment.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="fis_work.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fis_work.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fis_work.jsp">&nbsp;Работа&nbsp;с&nbsp;ФИС&nbsp;</A></TD></A>
      <TD align=left><A href="fis_work.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="abit_s_ots.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_s_ots.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_s_ots.do">&nbsp;Поиск&nbsp;абит.&nbsp;для&nbsp;ФГ&nbsp;</A></TD></A>
      <TD align=left><A href="abit_s_ots.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="fgruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="fgruppy.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="fgruppy.do">&nbsp;Формирование&nbsp;групп&nbsp;</A></TD></A>
      <TD align=left><A href="fgruppy.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="gener_ege.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="gener_ege.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="gener_ege.do">&nbsp;Оценки&nbsp;ЕГЭ&nbsp;</A></TD></A>
      <TD align=left><A href="gener_ege.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="sqlrep.do?action=save"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="sqlrep.do?action=save"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="sqlrep.do?action=save">&nbsp;Экспорт&nbsp;БД&nbsp;</A></TD></A>
      <TD align=left><A href="sqlrep.do?action=save"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="sqlrep.do?action=old"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="sqlrep.do?action=old"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="sqlrep.do?action=old">&nbsp;Импорт&nbsp;БД&nbsp;</A></TD></A>
      <TD align=left><A href="sqlrep.do?action=old"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="news.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="news.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="news.jsp">&nbsp;Новости&nbsp;</A></TD></A>
      <TD align=left><A href="news.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="access.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="access.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="access.jsp">&nbsp;Управление&nbsp;доступом&nbsp;</A></TD></A>
      <TD align=left><A href="access.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">Личные&nbsp;настройки</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;Выход&nbsp;</A></TD></A>
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
      Администратор системы может выполнять следующие действия:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. Настраивать систему, заполняя системные таблицы</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. Корректировать данные:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;а) формировать группы абитуриентов</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      3. Управлять доступом к системе:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;а) создавать и редактировать пользователей</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;б) задавать пароли пользователей</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;в) просматривать текущие соединения с системой</font></td>
</tbody>
</table>
</logic:equal>
</logic:present>
<BR>
</template:put>
</template:insert>
</logic:equal>

<%----- ВХОД В СИСТЕМУ ДЛЯ ОПЕРАТОРА -----%>
<logic:equal name='index' scope='request' value='opIndex'>
<logic:equal name='userGroup' property='typeId' value='1'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>Оператор системы</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Оператор системы</template:put>
<logic:present name='user' scope='session'>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abiturient.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abiturient.do">&nbsp;Карточка&nbsp;абит-та&nbsp;</A></TD></A>
      <TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="search.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="search.jsp">&nbsp;Поиск&nbsp;</A></TD></A>
      <TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
   <%---- Кнопка -----%>
  <TR><TD align=left><A href="abit_srch_online.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_srch_online.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_srch_online.do">&nbsp;Поиск&nbsp;Онлайн&nbsp;</A></TD></A>
      <TD align=left><A href="abit_srch_online.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  
   <%---- Кнопка -----%>
  <TR><TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_reports.jsp">&nbsp;Формы&nbsp;ГЗГУ&nbsp;</A></TD></A>
      <TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
     <%---- Кнопка -----%>
  <TR><TD align=left><A href="abit_attr.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_attr.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_attr.jsp">&nbsp;Корректировка&nbsp;</A></TD></A>
      <TD align=left><A href="abit_attr.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="zachislen.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="zachislen.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="zachislen.jsp">&nbsp;Зачисление&nbsp;в&nbsp;ВУЗ&nbsp;</A></TD></A>
      <TD align=left><A href="zachislen.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="rep_orders.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="rep_orders.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="rep_orders.jsp">&nbsp;Приказы&nbsp;</A></TD></A>
      <TD align=left><A href="rep_orders.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="reports.jsp">&nbsp;Документы&nbsp;</A></TD></A>
      <TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">Личные&nbsp;настройки</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
    <%---- Кнопка -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="checkEGE.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="checkEGE.jsp">Проверка&nbsp;ЕГЭ</A></TD></A>
      <TD align=left><A href="checkEGE.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;Выход&nbsp;</A></TD></A>
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
      Оператор системы может выполнять следующие действия:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. Вводить личные карточки абитуриентов</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. Производить параметрический поиск абитуриентов</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      3. Вводить и корректировать оценки</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      4. Корректировать атрибуты абитуриента:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;а) области</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;б) районы</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;в) населенные пункты</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;г) учебные заведения</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      5. Формировать отчеты и бланки</font></td>
<tr><td valign=middle width=90%><font class=text_11>
</tbody>
</table>
<BR>
</template:put>
</logic:present>
</template:insert>
</logic:equal>
</logic:equal>

<%----- ВХОД В СИСТЕМУ ДЛЯ ОПЕРАТОРА ВВОДА -----%>
<logic:equal name='index' scope='request' value='inIndex'>
<logic:equal name='userGroup' property='typeId' value='3'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>Оператор ввода</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Оператор ввода</template:put>
<logic:present name='user' scope='session'>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abiturient.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abiturient.do">&nbsp;Карточка&nbsp;абит-та&nbsp;</A></TD></A>
      <TD align=left><A href="abiturient.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="search.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="search.jsp">&nbsp;Поиск&nbsp;</A></TD></A>
      <TD align=left><A href="search.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
    <%---- Кнопка -----%>
  <TR><TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="umu_reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="umu_reports.jsp">&nbsp;Формы&nbsp;ГЗГУ&nbsp;</A></TD></A>
      <TD align=left><A href="umu_reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
     <%---- Кнопка -----%>
     <%---- Кнопка -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">&nbsp;&nbsp;Личные&nbsp;настройки&nbsp;&nbsp;</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;Выход&nbsp;</A></TD></A>
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
      Оператор ввода может выполнять следующие действия:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. Вводить личные карточки абитуриентов</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. Производить параметрический поиск абитуриентов</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      3. Корректировать личные данные абитуриентов</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      4. Просматривать доступную статистику.</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      5. Формировать пакеты документов</font></td>
<tr><td valign=middle width=90%><font class=text_11>
</tbody>
</table>
<BR>
</template:put>
</logic:present>
</template:insert>
</logic:equal>
</logic:equal>

<%----- ВХОД В СИСТЕМУ ДЛЯ ДРУГИХ -----%>
<logic:equal name='index' scope='request' value='drIndex'>
<logic:equal name='userGroup' property='typeId' value='2'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_menu.jsp"%>">
<template:put name='title'>Наблюдатель системы</template:put>
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Наблюдатель системы</template:put>
<logic:present name='user' scope='session'>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="abit_srch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="abit_srch.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="abit_srch.do">&nbsp;Поиск&nbsp;</A></TD></A>
      <TD align=left><A href="abit_srch.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="reports.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="reports.jsp">&nbsp;Документы&nbsp;</A></TD></A>
      <TD align=left><A href="reports.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="lich_adj.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="lich_adj.jsp">Личные&nbsp;настройки</A></TD></A>
      <TD align=left><A href="lich_adj.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="logoff.do"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="logoff.do"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="logoff.do">&nbsp;Выход&nbsp;</A></TD></A>
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
      Наблюдатель системы может выполнять следующие действия:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. Формировать разные виды отчетности</font></td>
<tr><td valign=middle width=90%><font class=text_11>
      2. Просматривать данные о ходе приемной кампании:</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;а) экзаменационные оценки</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;б) школьные оценки</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;в) сводки и списки</font></td>
<tr><td valign=middle width=90%><font class=text_11>
&nbsp;&nbsp;&nbsp;г) официальные формы</font></td></tr>
</tbody>
</table>
<BR>
</template:put>
</logic:present>
</template:insert>
</logic:equal>
</logic:equal>

<%----- ВЫБОР ГРУППЫ ПОЛЬЗОВАТЕЛЯ -----%>
<logic:equal name='index' scope='request' value='pubIndex'>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Регистрация пользователя</template:put>
<template:put name="sub_name">Выбор группы</template:put>
<template:put name='title'>Ошибка регистрации пользователя!</template:put>
<template:put name='content'>
  <table align=center border=0 cellspacing=0 cellPadding=0>
    <tr><td width=20%></td></tr>
    <tr><td><td height="220"><td width="20%"></td></tr>
  </table>
</template:put>
</template:insert>
</logic:equal>