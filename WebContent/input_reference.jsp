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
<template:put name='title'>Ввод справок</template:put>
<template:put name="target_name">АСУ "АБИТУРИЕНТ"</template:put>
<template:put name="sub_name">Ввод справок</template:put>
<template:put name="buttons">
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="school_olimp_diplom.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="school_olimp_diplom.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="school_olimp_diplom.jsp">&nbsp;Диплом&nbsp;призера&nbsp;олимпиад&nbsp;школников</A></TD></A>
      <TD align=left><A href="school_olimp_diplom.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="all_country_olimp_diplom.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="all_country_olimp_diplom.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="all_country_olimp_diplom.jsp">&nbsp;Диплом&nbsp;призера&nbsp;всероссийских&nbsp;олимпиад&nbsp;</A></TD></A>
      <TD align=left><A href="all_country_olimp_diplom.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>
  <%---- Кнопка -----%>
  <TR><TD align=left><A href="contraindication_conclusion.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_beg.gif" height=28 width=35></A></TD>
      <A href="contraindication_conclusion.jsp"><TD class="button" align="center" valign="middle" height="28">
        <A class=menu href="contraindication_conclusion.jsp">&nbsp;Заключение&nbsp;об&nbsp;отсутствии&nbsp;противопоказаний&nbsp;к&nbsp;обучению&nbsp;</A></TD></A>
      <TD align=left><A href="contraindication_conclusion.jsp"><img border=0 src="layouts/<bean:write name="user" property="idTema"/>/img/b_end.gif" height=28 width=35></A></TD>
  </TR>
  <TR><TD height=5></TD></TR>

</template:put>
<template:put name='content'>
<br>
<table border=0 sellPadding=0 sellSpacing=1>
<tbody>
<tr><td colspan=3 valign=middle height=35 width=50%><font class=text_11>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      В данном разделе можно выполнять следующие действия:</font></td>
<tr><td width=120 height=100 rowspan=20 valign=middle>
<tr><td valign=middle width=90%><font class=text_11>
      1. Занесение сведений о документах, предоставляемых абитуриентом приемной комиссии</font></td>
</tbody>
</table>
</template:put>
</template:insert>