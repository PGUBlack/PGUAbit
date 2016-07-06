<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
    import      = "abit.util.StringUtil"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='editSochAction' scope='request'>
 <logic:redirect forward='edit_soch'/>
</logic:notPresent>

<logic:notPresent name="editSochForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! String kP = "0"; %>

<SCRIPT LANGUAGE="JavaScript">
var teamLength

function checkFields()
{
 if(document.forms(0).kodFakulteta.value == "-")
 {
  alert("Необходимо выбрать Факультет");
  document.forms(0).kodFakulteta.focus();
  return false;
 }
}

function exec() {
    document.forms(0).kodFakulteta.selectedIndex=0;
    teamLength = document.forms(0).special1.options.length
    teamTXT = new Array(teamLength)
    teamVAL = new Array(teamLength)
    for(var ind=0;ind<document.forms(0).special1.length;ind++) {
       eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
       eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
    }
 document.forms(0).kodFakulteta.focus();
}

function exec2() {
}
</SCRIPT>

<logic:present name="editSochForm" property="action">
<bean:define id="action" name="editSochForm" property="action"/>

<%-----------------------------------------------------------------%>
<%-------------    НАСТРОЙКА ПАРАМЕТРОВ КОРРЕКТИРОВКИ   -----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="translate">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Сочинения</template:put>
<template:put name="sub_name">Задайте параметры</template:put>
<template:put name='title'>Сочинения</template:put>
<template:put name='content'>
<BR>
<html:form action="/edit_soch?action=show" onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
  <tr>
    <td><font class="text_10">&nbsp;Факультет:&nbsp;</font></td>
    <td>
      <html:select styleClass="select_f2" name="abit_ENS" property="kodFakulteta">
       <html:option value="-"/>
       <html:options collection="abit_ENS_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select>
    </td>  
  </tr>
<%--   <tr>
     <td><font class="text_10">&nbsp;Шифры&nbsp;отличий:&nbsp;</font></td>
     <td>
      <html:text accesskey="ш" styleClass="text_f10_short" name="abit_ENS" size='12' maxlength='50' property="shifrMedali"/>
     </td>
  </tr> --%>
<!--   <tr> -->
<!--      <td><font class="text_10">&nbsp;Копия&nbsp;сертификата:&nbsp;</font></td> -->
<!--      <td valign=center> -->
<%--       <html:select name="abit_ENS" styleClass="select_f2" property="kopijaSertifikata"> --%>
<%--         <html:option value="%">*</html:option> --%>
<%--         <html:option value="-">-</html:option> --%>
<%--         <html:option value="n">нет</html:option> --%>
<%--         <html:option value="y">да</html:option> --%>
<%--       </html:select> --%>
<!--      </td> -->
<!--   </tr> -->
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="Корректировка"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/edit_soch.do">
  <td><html:submit styleClass="button" property="exit" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%---------------   КОРРЕКТИРОВКА И ЕЕ РЕЗУЛЬТАТЫ   ---------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="results">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">Корректировка сертификатов и баллов ЕГЭ</template:put>
<template:put name='title'>Корректировка сертификатов и баллов ЕГЭ</template:put>
<template:put name='content'>
<br>
<p align=center>
<font class="text_10"><bean:write name="abit_ENS" property="fakultet"/></font><br>
<br>
<font class="text_10"><bean:write name="abit_ENS" property="number"/> абитуриента(ов)</font>
<br>
<html:form action="/edit_soch?action=save">
<html:hidden name="abit_ENS" property="special1"/>
<html:hidden name="abit_ENS" property="special3"/>
<%-- <html:hidden name="abit_ENS" property="shifrMedali"/> --%>
<%-- <html:hidden name="abit_ENS" property="attestat"/> --%>
<html:hidden name="abit_ENS" property="prinjat"/>

<html:hidden name="abit_ENS" property="kodFakulteta"/>
<html:hidden name="abit_ENS" property="kodSpetsialnosti"/>
<table align=center border=1 cellSpacing=0 cellPadding=0>
<thead>
  <tr>
     <td rowspan=2 valign=center align="center">&nbsp;№&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Фамилия&nbsp;И.О.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;&nbsp;&nbsp;Номер&nbsp;&nbsp;&nbsp;<br>&nbsp;&nbsp;личн.&nbsp;<br>&nbsp;дела&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;&nbsp;&nbsp;Паспорт&nbsp;&nbsp;</td>
<!--      <td rowspan=2 valign=center align="center">&nbsp;Шифр&nbsp;<BR>&nbsp;&nbsp;&nbsp;отл.&nbsp;</td> -->
     <td rowspan=2 valign=center align="center">&nbsp;Сочинение&nbsp;<br>&nbsp;загружено&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;Балл&nbsp;</td>
<!--      <td rowspan=2 valign=center align="center">&nbsp;Аттест.&nbsp;</td> -->
<!--      <td rowspan=2 valign=center align="center">&nbsp;Копия&nbsp;<br>&nbsp;серт.&nbsp;</td> -->
<!--      <td rowspan=2 valign=center align="center">&nbsp;Сертиф.&nbsp;ЕГЭ&nbsp;</td> -->

  </tr>
</thead>
<% int number=0; 
   int old_kod=-1;
%>
<logic:iterate id="abit" name="abit_ENS_S1" scope='request' type="abit.bean.AbiturientBean">
  <tr>
     <td align=center><font class="text_10">&nbsp;<%=++number%>&nbsp;</font></td>
     <td><font class="text_10">&nbsp;&nbsp;<bean:write name="abit" property="familija"/>&nbsp;&nbsp;</font></td>
     <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="nomerLichnogoDela"/>&nbsp;</font></td>
     <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="pasport"/>&nbsp;</font></td>
<%--      <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="shifrMedali"/>&nbsp;</font></td> --%>
<%--      <td align=center><html:text styleClass="text_f9_short" property="<%=\"att\"+abit.getKodAbiturienta()%>" --%>
<%--                  value="<%=""+abit.getAttestat()%>" --%>
<%--                      maxlength='1' size='1' /></td> --%>
<%--      <td align=center><html:text styleClass="text_f9_short" property="<%=\"kop\"+abit.getKodAbiturienta()%>" --%>
<%--                  value="<%=abit.getKopijaSertifikata()%>" --%>
<%--                      maxlength='1' size='1' /></td> --%>
<%--      <td align=center>&nbsp;<html:text styleClass="text_f9_short" property="<%=\"nom\"+abit.getKodAbiturienta()%>" --%>
<%--                  value="<%=""+abit.getNomerSertifikata()%>" --%>
<%--                      maxlength='30' size='16' />&nbsp;</td> --%>
     <td align=center><html:text styleClass="text_f9_short" property="<%=\"soc\"+abit.getKodAbiturienta()%>"
                 value="<%=""+abit.getNeed_Spo()%>"
                     maxlength='1' size='1' /></td>
                     <td align=center><html:text styleClass="text_f9_short" property="<%=\"ots\"+abit.getKodAbiturienta()%>"
                 value="<%=""+abit.getOlimp_1()%>"
                     maxlength='2' size='2' /></td>
</logic:iterate>
<logic:notPresent name="abit" property="familija">
<tr><td align=center valign=center colspan=30>
<p align=center>
     <font class="text_9">&nbsp;На&nbsp;данной&nbsp;специальности&nbsp;нет&nbsp;абитуриентов,&nbsp;удовлетворяющих&nbsp;критериям&nbsp;отбора</font></td>
  </td>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr>
   <td>
      <html:submit styleClass="button" property="back" value="Сохранить изменения"/>&nbsp;&nbsp;
   </td>
   <td>
</html:form>
<html:form action="/edit_soch.do">
   <td>
      <html:submit styleClass="button" property="exit" value="Выход"/>
   </td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>