<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%
   StringWriter sw = new StringWriter();
   PrintWriter pw = new PrintWriter(sw);
%>

<logic:notPresent name="errorForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<script lang="JavaScript">
function checkFields(){
  if(document.forms(0).comment!=null){
    if(document.forms(0).comment.value.length==0){
      alert("ВНИМАНИЕ! Необходимо кратко описать Ваши действия, приведшие к возникновению ошибки системы.");
      document.forms(0).comment.focus();
      return false;
    }
  }
}
</script>

<logic:present name='SQLException' scope='request'>
<bean:define id='sqlException' name='SQLException' scope='request' type='java.sql.SQLException'/>
<%
if (sqlException!=null && (request.getSession()).getAttribute("except")==null) { 
   sqlException.printStackTrace(pw);
   (request.getSession()).setAttribute("except",sqlException);
}
%>
</logic:present>

<logic:present name='JAVAexception' scope='request'>
<bean:define id='except' name='JAVAexception' scope='request' type='java.lang.Exception'/>
<%
if (except!=null && (request.getSession()).getAttribute("except")==null) { 
   except.printStackTrace(pw);
   (request.getSession()).setAttribute("except",except);
}
%>
</logic:present>

<logic:notPresent name='errorAction' scope='request'>
 <logic:redirect forward='error'/>
</logic:notPresent>

<logic:present name="errorForm" property="action">
<bean:define id="action" name="errorForm" property="action"/>

<logic:equal name="action" value="new">
<%----------------------- SQL EXCEPTION ---------------------------%>
<logic:present name='SQLException' scope='request'>
<bean:define id='sqlException' name='SQLException' scope='request' type='java.sql.SQLException'/>

<template:insert template="<%="layouts/1/lay/layout_tiny.jsp"%>">
<template:put name="title">ОШИБКА - сообщите, пожалуйста, разработчику системы</template:put>
<template:put name="target_name">Произошла Ошибка! Сообщите разработчику</template:put>
<template:put name="sub_name">layouts/1/img/error/sub_name.jpg</template:put>
<template:put name='content'>
<br>
<html:form action="/error.do?action=new_rem" onsubmit="return checkFields()">
<html:hidden name="err_bean" property="remark" value="<%=sw.toString()%>"/>
<html:hidden name="err_bean" property="tip" value="<%=sqlException.getMessage()%>"/> 

<TABLE border=0 cellPadding=0 cellSpacing=0>
<TBODY>
<logic:present name="err_bean" property="status">
 <tr><td align=middle height=30><font class="text_th">ВНИМАНИЕ !!! </font></td></tr>
 <tr><td align=middle height=27><font class="text_10">Данная ошибка уже зарегистрирована в системе и 
<bean:write name="err_bean" property="status"/>.<br></font></td></tr>
 <tr><td align=middle valign=bottom height=30><html:submit styleClass="button" tabindex="12" value="Продолжить"/></td></tr>
</logic:present>
<logic:notPresent name="err_bean" property="status">
<body onLoad="if(document.forms(0).comment!=null) document.forms(0).comment.focus();">
 <tr><td align=middle height=30>
     <font class="text_th">Опишите, пожалуйста, кратко Ваши действия, приведшие к возникновению ошибки системы:</font></td></tr>
 <tr><td align=middle height=27><font class="text_10">
<html:textarea accesskey='к' styleClass='text_f9' name='err_bean' property='comment' value='' rows='6' cols='130' tabindex='5'/></font></td></tr>
 <tr><td align=middle height=30><font class="text_th">
Сообщение будет рассмотрено в ближайшее время. Благодарим Вас за сотрудничество!</font></td></tr>
 <tr><td align=middle valign=bottom height=30><html:submit styleClass="button" tabindex="12" property="add" value="Продолжить"/></td></tr>
</logic:notPresent>
</TBODY>
</TABLE>
</html:form>
<font class="text_8">
<% out.println("<pre>"+sw+"</pre>");%>
</font>
</template:put>
</template:insert>
</logic:present>

<%----------------------- JAVA EXCEPTION ---------------------------%>
<logic:present name='JAVAException' scope='request'>
<bean:define id='javaException' name='JAVAException' scope='request' type='java.lang.Exception'/>

<template:insert template="<%="layouts/1/lay/layout_tiny.jsp"%>">
<template:put name="title">ОШИБКА - сообщите, пожалуйста, разработчику системы</template:put>
<template:put name="target_name">Произошла Ошибка! Сообщите разработчику</template:put>
<template:put name="sub_name">layouts/1/img/error/sub_name.jpg</template:put>
<template:put name='content'>
<br>
<html:form action="/error.do?action=new_rem" onsubmit="return checkFields()">
<html:hidden name="err_bean" property="remark"/>
<html:hidden name="err_bean" property="tip"/>

<TABLE border=0 cellPadding=0 cellSpacing=0>
<TBODY>
<logic:present name="err_bean" property="status">
 <tr><td align=middle height=30><font class="text_th">ВНИМАНИЕ !!! </font></td></tr>
 <tr><td align=middle height=27><font class="text_10">Данная ошибка уже зарегистрирована в системе и 
<bean:write name="err_bean" property="status"/>.<br></font></td></tr>
 <tr><td align=middle valign=bottom height=30><html:submit styleClass="button" tabindex="12" value="Продолжить"/></td></tr>
</logic:present>
<logic:notPresent name="err_bean" property="status">
<body onLoad="if(document.forms(0).comment!=null) document.forms(0).comment.focus();">
 <tr><td align=middle height=30>
     <font class="text_th">Опишите, пожалуйста, кратко Ваши действия, приведшие к возникновению ошибки системы:</font></td></tr>
 <tr><td align=middle height=27><font class="text_10">
<html:textarea accesskey='к' styleClass='text_f9' name='err_bean' property='comment' value='' rows='6' cols='130' tabindex='5'/></font></td></tr>
 <tr><td align=middle height=30><font class="text_th">
Сообщение будет рассмотрено в ближайшее время. Благодарим Вас за сотрудничество!</font></td></tr>
 <tr><td align=middle valign=bottom height=30><html:submit styleClass="button" tabindex="12" property="add" value="Продолжить"/></td></tr>
</logic:notPresent>
</TBODY>
</TABLE>
</html:form>
<font class="text_8">
<pre><bean:write name="err_bean" property="remark"/></pre>
</font>
</template:put>
</template:insert>
</logic:present>
</logic:equal>

<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/1/lay/layout_small.jsp"%>">
<template:put name="title">Ошибки работы системы</template:put>
<template:put name="target_name">Ошибки работы системы</template:put>
<template:put name="content">
<table cols=7 align=center border=1 cellSpacing=0>
<thead>
<tr>
   <td  align=center valign=center height=30><font class="text_th">&nbsp;№&nbsp;</font></td>
    <td  align=center valign=center height=30>
	<font class="text_th">&nbsp;Статус&nbsp;ошибки&nbsp;</font></td>
    <td  align=center valign=center height=30><font class="text_th">&nbsp;Дата&nbsp;</font></td>
    <td  align=center valign=center height=30><font class="text_th">&nbsp;Время&nbsp;</font></td>
    <td  align=center valign=center width=150 height=30><font class="text_th">&nbsp;Тип&nbsp;</font></td>
    <td  align=center valign=center height=30><font class="text_th">&nbsp;Описание&nbsp;</font></td>
    <td  align=center valign=center height=30>
	<font class="text_th">&nbsp;Комментарий&nbsp;</font></td>
    <td  align=center valign=center height=30><font class="text_th">&nbsp;Подразделение&nbsp;</font></td>
    <td  align=center valign=center height=30><font class="text_th">&nbsp;Логин&nbsp;</font></td>
    <td  align=center valign=center height=30><font class="text_th">&nbsp;Пользователь&nbsp;</font></td>
</tr>
</thead>
<% int i=0; %>
<tbody>
<logic:iterate id="error" name="errs_bean" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<html:link href="error.do?action=m_d" paramName="error" paramId="idRemark" paramProperty="idRemark" styleClass="link_hov_blue"><%=++i%></html:link>&nbsp;</td>
  <td valign=center align=center>&nbsp;<font class="text_10"><bean:write name="error" property="status"/></font>&nbsp;</td>
  <td valign=center align=center>&nbsp;<font class="text_10"><bean:write name="error" property="data"/></font>&nbsp;</td>
  <td valign=center align=center>&nbsp;<font class="text_10"><bean:write name="error" property="time"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="error" property="tip"/></font>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><pre><bean:write name="error" property="remark"/></pre></font>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="error" property="comment"/></font>&nbsp;</td>
  <td valign=center align=center>&nbsp;<font class="text_10"><bean:write name="error" property="abbr"/></font>&nbsp;</td>
  <td valign=center align=center>&nbsp;<font class="text_10"><bean:write name="error" property="name"/></font>&nbsp;</td>
  <td valign=center>&nbsp;<font class="text_10"><bean:write name="error" property="descr"/></font>&nbsp;</td>
</tr>
</logic:iterate>
</tbody>
<logic:notPresent name="error" property="idRemark">
<tr>
  <td align=center valign=center colspan=10>
    <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr>
<html:form action="/error.do?action=exit">
  <td align=center>
    <html:submit styleClass="button" property="exit" value="Выход в меню"/>
  </td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>