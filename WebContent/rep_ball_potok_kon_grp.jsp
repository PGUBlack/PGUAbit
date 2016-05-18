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

<logic:notPresent name='ballPotokKonGrpAction' scope='request'>
 <logic:redirect forward='rep_ball_potok_kon_grp'/>
</logic:notPresent>

<logic:notPresent name="ballPotokKonGrpForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function invokeAct(){
  document.forms(0).submit();
}

</SCRIPT>

<logic:present name="ballPotokKonGrpForm" property="action">
<bean:define id="action" name="ballPotokKonGrpForm" property="action"/>

<%---------------- Собственно страница ----------------------------%>
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name='title'>Сумма баллов по потокам</template:put>
<template:put name="target_name">Сумма баллов по потокам для конкурсных групп</template:put>
<template:put name="sub_name">Выберите поток</template:put>
<template:put name='content'>
<br>
<html:form action="rep_ball_potok_kon_grp?action=show">
<table align="left" border=0 cellspacing=0>
  <tr><td align="left">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="text_10">Номер потока:</font></td>
      <td align="left">
      <html:select onchange="invokeAct()" styleClass="select_f2" name="abit_TM" property="nomerPotoka">
       <html:options collection="abit_TM_S1" property="nomerPotoka"/>
      </html:select>
      </td>
   </tr>
</table>
</html:form>
<br><br>
<table align=center border=1 cellspacing=0>
<thead>
<tr>
<td align=center><font class="text_th">&nbsp;Конк.&nbsp;гр.&nbsp;</font></td>
<% int i; %>
<% for(i=300;i>=0;i--) {%>
<% if(i<=9) { %>
<td align=center><font class="text_th">&nbsp;&nbsp;<%=i%>&nbsp;&nbsp;</font></td>
<% } else { %>
<td align=center><font class="text_th">&nbsp;<%=i%>&nbsp;</font></td>
<% } } %>
</tr>
</thead>
<%-- HEAVY LOGIC INTERFACE BUILDER --%>
<tbody>
<logic:iterate id="otsenki" name="mass_otsenki" scope='request' type='abit.bean.AbiturientBean'>
<tr>
<td valign=center align=center><font class="text_10">  
  <bean:write name="otsenki" property="nazvanie"/>&nbsp;</font></td>
  <logic:iterate id="otsenka" collection="<%=otsenki.getList()%>" type="abit.bean.AbiturientBean"> 
   <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="otsenka" property="special1"/>&nbsp;</font></td>
  </logic:iterate>
</tr>
</logic:iterate>
</tbody>
</table>
<table align="left" border="0">
<td align="left">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>
<html:form action="/rep_ball_potok_kon_grp.do?action=report">
<html:submit styleClass="button_sd" value="Создать отчет"/></td>
</html:form>
<html:form action="/rep_ball_potok_kon_grp.do">
  <td><html:submit styleClass="button_sd" property="exit" value="Выход"/></td>
</html:form></tr>
</table>

</template:put>
</template:insert>
</logic:present>