<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java" %>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='userSessAction' scope='request'>
  <logic:redirect href='user_sess.do'/>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<logic:notPresent name="userSessForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<logic:present name="userSessForm" property="action">
<bean:define id="action" name="userSessForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- Активные сессии   -----------------------%>
<%-----------------------------------------------------------------%>

<logic:equal name="action" value="active">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Текущие активные соединения</template:put>
<template:put name='title'>Текущие активные соединения</template:put>
<template:put name='refresh' direct='true'>30</template:put>
<template:put name='content'>
<BR>
  <table align=center border=1 cellspacing=0>
  <thead>
   <tr valign="middle">
    <td align=center height="30"><font class="text_th">&nbsp;Логин&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Группа&nbsp;</font></td>
    <td align=center height="30" width="100%"><font class="text_th">&nbsp;Фамилия&nbsp;И.О.&nbsp;&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">IP &nbsp;адрес&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Вход&nbsp; &nbsp;в&nbsp;систему&nbsp;
    <td align=center height="30"><font class="text_th">&nbsp;Время&nbsp; &nbsp;работы&nbsp; &nbsp;в&nbsp;системе&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Всего&nbsp; &nbsp;выполнено&nbsp; &nbsp;действий&nbsp;</font></td>
   </tr>
  </thead>
<logic:iterate id='user' name='stats' scope='request'>
  <tr valign="middle">
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='name'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='gruppa'/>&nbsp;</font></td>
   <td align=left><font class="text_11"><bean:write name='user' property='descr'/></font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='uip'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='dataLogin'/>&nbsp;<br>
       &nbsp;<bean:write name='user' property='timeLogin'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='totalTime'/>&nbsp;</font></td>
   <td align=center>&nbsp;<html:link href="user_sess.do?action=get_fulld" paramName="user" 
                   paramId="idStat" paramProperty="idStat" styleClass="link_hov_blue">
                   <bean:write name='user' property='kolZaprCur'/></html:link>&nbsp;</td>
  </tr>
</logic:iterate>
</table>
<table align=center border=0 cellspacing=0>
  <tr><td height="13"></td></tr>
  <tr><td>
   <html:form action="/user_sess.do?action=full_st">
    <html:submit styleClass="button" tabindex="1" value="Статистика"/>
   </html:form>
  </td>
  <td>&nbsp;&nbsp;&nbsp;
  </td>
  <td>
   <html:form action="/users.do">
    <html:submit styleClass="button" property="exit" tabindex="2" value="Выход"/>
   </html:form>
  </td></tr>
</table>
<BR>
</template:put>
</template:insert>
</logic:equal>

<logic:equal name="action" value="statist">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Статистика работы пользователей</template:put>
<template:put name='title'>Статистика работы пользователей</template:put>
<template:put name='content'>
<p align=center>
<logic:iterate id="month" name="months" scope='request'>
        &nbsp;<html:link styleClass="link_hov_blue" href="user_sess.do?action=full_st" paramId="letter" paramName="month" paramProperty="special1">
        <bean:write name="month" property="special2"/>
        </html:link>
</logic:iterate>
</p>
  <table align=center border=1 cellspacing=0>
  <thead>
   <tr valign="middle">
    <td align=center height="30"><font class="text_th">&nbsp;Логин&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Группа&nbsp;</font></td>
    <td align=center height="30" width="100%"><font class="text_th">&nbsp;Фамилия&nbsp;И.О.&nbsp;&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Дата&nbsp;и&nbsp;время&nbsp; &nbsp;последн.&nbsp;входа&nbsp;&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Всего&nbsp; &nbsp;работал&nbsp; &nbsp;в&nbsp;системе&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Количество&nbsp; &nbsp;входов&nbsp; &nbsp;в&nbsp;систему&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Всего&nbsp; &nbsp;выполнено&nbsp; &nbsp;действий&nbsp;</font></td>
   </tr>
  </thead>
<logic:iterate id='user' name='stats' scope='request'>
  <tr valign="middle">
   <td align=center><html:link href="user_sess.do?action=get_detail" paramName="user" 
                   paramId="idUser" paramProperty="idUser" styleClass="link_hov_blue">
&nbsp;<bean:write name='user' property='name'/>&nbsp;</html:link></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='gruppa'/>&nbsp;</font></td>
   <td align=left><font class="text_11"><bean:write name='user' property='descr'/></font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='dataLogin'/>&nbsp;<br>
       &nbsp;<bean:write name='user' property='timeLogin'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='totalTime'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='kolLogin'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='kolZapr'/>&nbsp;</font></td>
  </tr>
</logic:iterate>
</table>
<table align=center border=0 cellspacing=0>
  <tr><td height="13"></td></tr>
  <tr>
   <td>
    <html:form action="/users.do">
     <html:submit styleClass="button" property="exit" value="Выход"/>
    </html:form>
   </td>
  </tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<logic:equal name="action" value="detail">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Подробности работы пользователя</template:put>
<template:put name='title'>Подробности работы пользователя</template:put>
<template:put name='content'>
<p align="center"><font class="text_11">
<bean:write name='u_bean' property='gruppa'/>&nbsp;(<bean:write name='u_bean' property='name'/>):
&nbsp;<bean:write name='u_bean' property='descr'/></font>
</p>
  <table align=center border=1 cellspacing=0>
  <thead>
   <tr valign="middle">
    <td align=center height="30"><font class="text_th">IP &nbsp;адрес&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Вход&nbsp; &nbsp;в&nbsp;систему&nbsp;
    <td align=center height="30"><font class="text_th">&nbsp;Выход&nbsp; &nbsp;из&nbsp;системы&nbsp;
    <td align=center height="30"><font class="text_th">&nbsp;Время&nbsp; &nbsp;работы&nbsp; &nbsp;в&nbsp;системе&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Всего&nbsp; &nbsp;выполнено&nbsp; &nbsp;действий&nbsp;</font></td>
   </tr>
  </thead>
<logic:iterate id='user' name='stats' scope='request'>
  <tr valign="middle">
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='uip'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='dataLogin'/>&nbsp;<br>
       &nbsp;<bean:write name='user' property='timeLogin'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='dataLogout'/>&nbsp;<br>
       &nbsp;<bean:write name='user' property='timeLogout'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='totalTime'/>&nbsp;</font></td>
   <td align=center>&nbsp;<html:link href="user_sess.do?action=get_fulld" paramName="user" 
                   paramId="idStat" paramProperty="idStat" styleClass="link_hov_blue">
                   <bean:write name='user' property='kolZapr'/></html:link>&nbsp;</td>
  </tr>
</logic:iterate>
</table>
<table align=center border=0 cellspacing=0>
  <tr><td height="13"></td></tr>
  <tr>
   <td>
    <html:form action="/users.do">
     <html:submit styleClass="button" property="exit" value="Выход"/>
    </html:form>
   </td>
  </tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<logic:equal name="action" value="full_det">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">АСУ "Абитуриент"</template:put>
<template:put name="sub_name">Действия пользователя</template:put>
<template:put name='title'>Действия пользователя</template:put>
<template:put name='content'>
<p align="center"><font class="text_11">
<bean:write name='u_bean' property='imja'/>&nbsp;&nbsp;(<bean:write name='u_bean' property='dataLogin'/>)</font>
</p>
  <table align=center border=1 cellspacing=0>
  <thead>
   <tr valign="middle">
    <td align=center height="30"><font class="text_th">&nbsp;№&nbsp;
    <td align=center height="30"><font class="text_th">&nbsp;Вход&nbsp;в&nbsp;модуль&nbsp;
    <td align=center height="30"><font class="text_th">&nbsp;Название&nbsp;модуля&nbsp;</font></td>
    <td align=center height="30"><font class="text_th">&nbsp;Действие&nbsp;</font></td>
   </tr>
  </thead>
<logic:iterate id='user' name='stats' scope='request'>
  <tr valign="middle">
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='special1'/>&nbsp;</font></td>
   <td align=center><font class="text_11">&nbsp;<bean:write name='user' property='timeLogin'/>&nbsp;</font></td>
   <td align=left><font class="text_11">&nbsp;<bean:write name='user' property='name'/>&nbsp;</font></td>
   <td align=left><font class="text_11">&nbsp;<bean:write name='user' property='descr'/>&nbsp;</font></td>
  </tr>
</logic:iterate>
</table>
<table align=center border=0 cellspacing=0>
  <tr><td height="13"></td></tr>
  <tr>
   <td>
    <html:form action="/users.do">
     <html:submit styleClass="button" property="exit" value="Выход"/>
    </html:form>
   </td>
  </tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>