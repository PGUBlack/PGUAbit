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

<logic:notPresent name='lich_adjAction' scope='request'>
 <logic:redirect forward='lich_adj'/>
</logic:notPresent>

<logic:notPresent name="lich_adjForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
 var text;

</SCRIPT>

<logic:present name="lich_adjForm" property="action">
<bean:define id="action" name="lich_adjForm" property="action"/>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="chg_itf">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Личные настройки</template:put>
<template:put name="target_name">Управление личными настройками</template:put>
<template:put name="sub_name">Текущие настройки</template:put>
<template:put name='content'>
<html:form action="/lich_adj?action=new_itf">
<table align=center border=1 cellSpacing=0 width=100%>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;ИНТЕРФЕЙС СИСТЕМЫ&nbsp;</font></td></tr>
<tr><td colspan=3 height=1></td></tr>
<tr><td align=middle valign=middle>&nbsp;Название&nbsp;</td>
    <td align=middle valign=middle>&nbsp;Вид&nbsp;</td>
    <td align=middle valign=middle>&nbsp;Описание&nbsp;</td>
</tr>
</thead>
<logic:iterate id="thema" name="themes" scope='request' type="abit.bean.LichAdjustBean">
<tr><td align=center valign=center>&nbsp;<html:link href="lich_adj.do?action=newitf" paramName="thema" paramId="idTema" paramProperty="idTema" styleClass="link_hov_blue">
                    <bean:write name="thema" property="nazv"/><br>&nbsp;<bean:write name="thema" property="resolution"/>&nbsp;</font></td>
    </html:link>&nbsp;</td>
    <td align=center valign=center><font class="text_10">&nbsp;
      <A target="helpWindow" border=0 href="<%="layouts/"+thema.getFolder()+"/lay/showint.jsp"%>">
        <img src="<%="layouts/"+thema.getFolder()+"/img/present.jpg"%>" border="0" height="132" width="190" alt="Интерфейс системы"/>
      </A>&nbsp;</font>
    </td>
    <td align=center valign=center><font class="text_10">&nbsp;<bean:write name="thema" property="descr"/>&nbsp;<br>(Для просмотра нажмите на картинке)</font></td>
</tr>
</logic:iterate>
</table>
</html:form>
<html:form action="/lich_adj.do">
<table align=center border=0>
 <tr>
   <td>
    <html:submit styleClass="button" property="exit" value="Выход"/>
   </td>
 </tr>
</table>
</html:form>
</template:put>
</template:insert>
</logic:equal>
</logic:present>