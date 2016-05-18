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

<logic:notPresent name='reportsBrowserAction' scope='request'>
 <logic:redirect forward='rep_browser'/>
</logic:notPresent>

<logic:notPresent name="reportsBrowserForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function mark_all(){
 var line=document.forms(0).special1.value;
 var curr_ind = line.indexOf('%');
 var next_ind = line.indexOf('%',1);
 if(line.indexOf('%')!=-1){
   while(next_ind !=-1){
     eval("document.forms(0)."+line.substring(curr_ind+1,next_ind)+".checked=true");
     curr_ind=next_ind;
     next_ind = line.indexOf('%',next_ind+1);
   }
   eval("document.forms(0)."+line.substring(curr_ind+1)+".checked=true");
 }
 return true
}

</SCRIPT>

<logic:present name="reportsBrowserForm" property="action">
<bean:define id="action" name="reportsBrowserForm" property="action"/>

<%-----------------------------------------------------------------%>
<%---------------------- вновь созданный --------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new_rep">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">База данных Документов</template:put>
<template:put name="sub_name">Результат создания документа</template:put>
<template:put name='title'>Вновь созданный отчет</template:put>
<template:put name='content'>
<br>
<p align=center valign=center>
  <font class="text_11"><bean:write name="report" property="special1"/></font>
</p>
<table cols=4 align=center border=1 cellspacing=0>
<thead>
  <tr><td align=center><font class="text_th">&nbsp;&nbsp;Название&nbsp;отчета&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Дата&nbsp;создания&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Время&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Кем&nbsp;создан&nbsp;&nbsp;</font></td>
  </tr>
</thead>
  <bean:define id="f_name" name="report" property="fileName"/>
  <tr><td align=center><font class="text_10">&nbsp;&nbsp;<html:link href="<%="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/reports/"+f_name%>" target="wndReportNew" styleClass="link_hov_blue">
                       <bean:write name="report" property="name"/></html:link>&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="date"/>&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="time"/>&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_10">&nbsp;&nbsp;<bean:write name="report" property="author"/>&nbsp;&nbsp;</font></td>
  </tr>
</table>
<table align=center border=0 cols=2>
<tr>
<td align=center><html:form action="/rep_browser.do?action=old">
  <html:submit styleClass="button_sd" value="БД документов"/>
</html:form></td>
<td align=center><html:form action="/rep_browser.do">&nbsp;&nbsp;
  <html:submit styleClass="button_sd" property="exit" value="Выход"/>
</html:form></td>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%---------------------- ранее созданные --------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="old_rep">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">База данных Документов</template:put>
<template:put name="sub_name">Содержание</template:put>
<template:put name='title'>БД созданных ранее отчетов</template:put>
<template:put name='content'>
<BR>
<br>
<html:form action="/rep_browser?action=old">
<html:hidden name="report" property="special1"/>
<table cols=7 align=center border=1 cellspacing=0>
<thead>
  <tr>
      <td valign=center align=center><font class="text_th">&nbsp;№&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Название&nbsp;отчета&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Дата&nbsp;создания&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Время&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Кем&nbsp;создан&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Доступен всем&nbsp;&nbsp;</font></td>
      <td align=center><font class="text_th">&nbsp;&nbsp;Удалить&nbsp;&nbsp;</font></td>
  </tr>
</thead>
<logic:iterate id="rept" name="reports">
     <bean:define id="f_name" name="rept" property="fileName"/>
     <bean:define id="id_Dok" name="rept" property="id"/>
  <tr>
     <td valign=center align=center>
        <font class="text_10">&nbsp;<bean:write name="rept" property="number"/>&nbsp;</font></td>
     <td valign=center>&nbsp;<a href="<%="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/reports/"+f_name%>" target="wndReportOld" class="link_hov_blue">
        <bean:write name="rept" property="name"/></a>&nbsp;</td>
     <td valign=center align=center>
        <font class="text_10">&nbsp;<bean:write name="rept" property="date"/>&nbsp;</font></td>
     <td valign=center align=center>
        <font class="text_10">&nbsp;<bean:write name="rept" property="time"/>&nbsp;</font></td>
     <td valign=center><font class="text_10">&nbsp;<bean:write name="rept" property="author"/>&nbsp;</font></td>
     <td valign=center align=center><font class="text_10">&nbsp;
       <%if(((abit.bean.ReportsBrowserBean)rept).getViewToAll()==null){ out.println("<a href='"+"rep_browser.do?action=vta&"+id_Dok+"'"+" class='link_hov_blue'>нет</a>"); }
         else{ out.println("<a href='"+"rep_browser.do?action=vta&"+id_Dok+"'"+" class='link_hov_blue'>да</a>"); } %>
       &nbsp;</font></td>
     <td valign=center align=center><input type="checkbox" name="<%=id_Dok%>">&nbsp;&nbsp;
        <a href="<%="rep_browser.do?action=old&del=1&"+id_Dok%>" class="link_hov_blue">del</a>
     </font></td>
  </tr>
</logic:iterate>
<logic:notPresent name="rept" property="fileName">
<tr>
  <td align=center valign=center colspan=7>
     <font class="text_11">В&nbsp;базе&nbsp;данных&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи</font></td>
</logic:notPresent>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:submit property="del" styleClass="button_sd" value="Удалить"/></td>
<td align=center>
<html:button property="marker" onclick="mark_all()" styleClass="button_sd" value="Отметить все"/></td>
<td align=center>
<html:reset onclick="mark_all" styleClass="button_sd" value="Сброс"/></td>
</html:form>
<html:form action="/rep_browser.do">
  <td><html:submit styleClass="button_sd" property="exit" value="Выход"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>