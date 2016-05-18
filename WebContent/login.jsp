<%@ page
    contentType="text/html;charset=windows-1251"
    language="java"
    import="org.apache.struts.taglib.template.util.*"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name="loginAction" scope="request">
<logic:redirect forward="login"/>
</logic:notPresent>

<script language='Javascript'>
function checkFields(){
  if(document.forms(0).userName.value=="") {
    alert("Введите ''Имя пользователя''");
    document.forms(0).userName.focus();
    return false;
  }
  if(document.forms(0).password.value=="") {
    alert("Введите ''Пароль''");
    document.forms(0).password.focus();
    return false;
  }
  return true;
}

function exec(){
  document.forms(0).id.selectedIndex=1;
  document.forms(0).kodVuza.selectedIndex=0;
  document.forms(0).userName.value="";
  document.forms(0).password.value="";
  document.forms(0).userName.focus();
}

function help_me(){
  alert("Не удается войти в систему?\n\nПроверьте правильность раскладки клавиатуры и режим CapsLock.\nОбратитесь к администратору системы.");
  document.forms(0).id.focus();
}
</script>

<template:insert template="layouts/1/lay/layout_simple.jsp">
<template:put name="target_name">Регистрация пользователя</template:put>
<template:put name="sub_name">Вход в систему</template:put>
<template:put name="title">Вход в систему</template:put>

<logic:notPresent name="loginForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<template:put name="content">
<logic:present name="loginForm" property="action">
<bean:define id="action" name="loginForm" property="action"/>

<%--logic:notPresent name="this_session" scope="application">
<bean:define id="this_session" value="<%=session.getId()%>" scope="application"/>
</logic:notPresent>

<logic:present name="this_session" scope="application">
<logic:notEqual name="this_session" value="<%=session.getId()%>">
ВНИМАНИЕ! Сервер был перезапущен администратором в связи с добавлением новых модулей АСУ.
<bean:define id="this_session" value="<%=session.getId()%>" scope="application"/>
</logic:notEqual>
</logic:present--%>

<body bgcolor=#E9DFBA onLoad='exec()'>
<TABLE background="" border='0' cellPadding='0' cellSpacing='0' width="100%">
<TR><TD rowspan=6 align=left width=170>&nbsp;</TD>
<html:form action="login.jsp?action=logging" onsubmit="return checkFields();">
<TR><TD align='right' valign=middle>
     <FONT class=text_11>ВУЗ:</FONT>
    </TD>
<TD align='left'>&nbsp;
<html:select styleClass="select_f1" name="abit" property="kodVuza" tabindex="1">
<html:options collection="vuzy" property="kodVuza" labelProperty="abbreviaturaVuza"/>
</html:select> 
</TD></TR>
<TR><TD align='right' valign=middle>
<FONT class=text_11>Группа:</FONT></TD>
<TD align='left'>&nbsp;
<html:select styleClass="select_f1" name="abit" property="id" tabindex="2">
<html:options collection="uGroups" property="id" labelProperty="userGroup"/>
</html:select> 
</TD><TD width=35%>&nbsp;</TD></TR>
<TR><TD align='right' valign=middle>
<FONT class=text_11>Имя:</FONT></TD>
<TD valign=middle>&nbsp;
<html:password styleClass="text_f10" name="abit" property="userName" 
           accesskey="и" maxlength="18" size="20" tabindex="3"/></TD></TR>
<TR><TD align='right' valign=middle>
<FONT class=text_11>Пароль:</FONT></TD>
    <TD align=left valign=middle>&nbsp;
<html:password styleClass="text_f10" name="abit" property="password" 
           accesskey="п" maxlength="18" size="20" tabindex="4"/></TD></TR>
<TR><TD colspan="2" align=middle valign=middle height=8></TD></TR>
<TR><TD colspan="3" align=middle valign=middle>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<html:submit styleClass="button" value="Войти" tabindex="5"/>&nbsp;
<html:button styleClass="button" onclick="help_me();" value="Справка" property="help" tabindex="6"/></TD></TR>
</html:form>
</TBODY></TABLE>
</logic:present>
</body>
</template:put>
</template:insert>