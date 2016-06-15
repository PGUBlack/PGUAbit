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

<logic:notPresent name='listsInfoAction' scope='request'>
 <logic:redirect forward='lists_info'/>
</logic:notPresent>

<logic:notPresent name="ListsInfoForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength

function checkFields()
{
 if(document.forms(0).shifrFakulteta.value == "-")
 {
  alert("���������� ������� ���������");
  document.forms(0).shifrFakulteta.focus();
  return false;
 }
}

function exec() {
if( document.forms(0).shifrFakulteta!= null && document.forms(0).special1!= null) {
 document.forms(0).shifrFakulteta.selectedIndex=0;
 teamLength = document.forms(0).special1.options.length
 teamTXT = new Array(teamLength)
 teamVAL = new Array(teamLength)
 for(var ind=0;ind<document.forms(0).special1.length;ind++) {
    eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
    eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
 }
 fillSelect(document.forms(0).shifrFakulteta.value);
 document.forms(0).shifrFakulteta.focus();
}
//if( document.forms(0).special1 == null) {
 document.forms(0).special2.checked = true;
//}
}

function fillSelect(selectCtrl) {
var i,j=0
if(selectCtrl == "-") {
// �������� ������������ �����
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// ���������� ����� "-"
   document.forms(0).special1.options[0] = new Option("-")
   document.forms(0).special1.options[0].value = "-"
   document.forms(0).special1.options[0].selected = true
return
}

// �������� ������������ �����
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}

// ���������� ����� "*"
  document.forms(0).special1.options[0] = new Option("*")
  document.forms(0).special1.options[0].value = "-1"
  document.forms(0).special1.options[0].selected = true
j=1
// ���������� ����� �����
for(i = 0; i < teamLength; i++) {
   if(teamTXT[i].charAt(0) == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     j++
   }
}
// ������� � ������ ������
document.forms(0).special1.options[0].selected = true
}
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="listsInfoForm" property="action">
<bean:define id="action" name="listsInfoForm" property="action"/>

<%-----------------------------------------------------------------%>
<%----------------------- ��������� ��������� ---------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>�������� ������������ � ���������������� ��������</template:put>
<template:put name="target_name">�������� ������������</template:put>
<template:put name="sub_name">�������� ���������</template:put>
<template:put name='content'>
<BR>
<html:form action="/lists_info?action=viewing" onsubmit="return checkFields();">
<table align=center border=0>
<tr><td><font class="text_10">����������:</font></td>
<td><html:radio accesskey="�" name="abit_SD" property="priznakSortirovki" tabindex="1" value="budgetniki"/></td>
<tr><td><font class="text_10">������������:</font></td>
<td><html:radio name="abit_SD" property="priznakSortirovki" tabindex="2" value="kontraktniki"/></td>
</tr></table>

<table cols=3 align=center border=0>
<tr><td colspan=3><hr></td></tr>
  <tr><td><font class="text_10">���������:</font></td>
      <td><html:select styleClass="select_f1" onchange="fillSelect(this.value)" name="abit_SD" property="shifrFakulteta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
  <tr><td><font class="text_10">�������������:</font></td>
      <td><html:select styleClass="select_f1" name="abit_SD" property="special1" tabindex="2">
          <html:options collection="abit_SD_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
</table>
<p align="center"><html:checkbox styleClass="checkbox_1" name="abit_SD" property="special2"/><font class="text_10">����������� ����������� ������ ������</font></p>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="3" value="��������"/></td>
</html:form>
<html:form method="post" action="/lists_info?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="4" value="�����"/></td>
</html:form>
</tr>
</table>
<br>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>�������� ������������ �� ������������� � ���������������� ��������</template:put>
<template:put name="target_name">�������� ������������ �� �������������</template:put>
<template:put name='content'>
<%!
   int summa = 0;
%>
<BR>
<html:form action="/lists_info?action=report1">
  <html:hidden name="abit_SD" property="kodSpetsialnosti"/>
  <html:hidden name="abit_SD" property="shifrFakulteta"/>
  <html:hidden name="abit_SD" property="priznakSortirovki"/>
<font class="text_11">
�� ��������� ������������� �����:&nbsp;<bean:write name="abit_SD" property="special22"/>&nbsp;�����������(��)
</font>
<br><br>
<table cols=8 align=center border=1 cellSpacing=0>
<thead>
<td rowspan=2 align=center><font class="text_th">&nbsp;�&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;����� ����. ����&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;�������&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;���&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;��������&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;���.&nbsp;</font></td>
<td colspan=<bean:write name="abit_SD" property="predmCount"/> align=center><font class="text_th">&nbsp;��������&nbsp;/&nbsp;������&nbsp;</font></td>
<td rowspan=2 align=center><font class="text_th">&nbsp;�����&nbsp;<br>&nbsp;������&nbsp;</font></td>
<tr>
<logic:iterate id="abit_SD" name="abit_SD_S4" scope='request'>
<td align=center><font class="text_th">&nbsp;<bean:write name="abit_SD" property="predmet"/>&nbsp;</font></td>
</logic:iterate>
</tr>
</thead>
<%-- HEAVY LOGIC INTERFACE BUILDER --%>
<logic:iterate id="abit_SD" name="abits_SD" scope='request'>
<% summa = 0; %>
<tr valign=middle>
<td align=center><font class="text_10">
  <bean:write name="abit_SD" property="number"/>&nbsp;</font></td>
  <td align=center><font class="text_10">&nbsp;<bean:write name="abit_SD" property="nomerLichnogoDela"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;&nbsp;<html:link action="/lists_info?action=report" paramName="abit_SD" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue">&nbsp;<bean:write name="abit_SD" property="familija"/>&nbsp;</html:link>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abit_SD" property="imja"/>&nbsp;</font></td>
  <td><font class="text_10">&nbsp;<bean:write name="abit_SD" property="otchestvo"/>&nbsp;</font></td>
  <td align="center"><font class="text_10">&nbsp;<bean:write name="abit_SD" property="tipDokSredObraz"/>&nbsp;</font></td>
  <logic:iterate id="curr_note" collection="<%=((abit.bean.AbiturientBean)abit_SD).getNotes()%>" type="java.lang.String">
   <td align=center><font class="text_10">&nbsp;<%=curr_note%>&nbsp;</font></td>
   <% summa += StringUtil.toInt(curr_note,0); %>
  </logic:iterate>
   <td align=center><font class="text_10">&nbsp;<%=summa%>&nbsp;</font></td>
</tr>
</logic:iterate>
<logic:notPresent name="abit_SD" property="kodAbiturienta">
<tr>
  <td align=center valign=center colspan=20 rowspan=3>
<p align=center><font class="text_11">
     �&nbsp;����&nbsp;������&nbsp;���&nbsp;��������&nbsp;�&nbsp;���������&nbsp;������������</font></td>
  </td>
</logic:notPresent>
</table>
<p align="center"><html:checkbox styleClass="checkbox_1" name="abit_SD" property="special2"/><font class="text_10">����������� ����������� ������ ������</font></p>
<table align=center border=0>
<tr>
  <td><html:submit styleClass="button" value="������� �����"/></td>
</html:form>
<html:form action="/lists_info.do?action=view">
  <td align=center>&nbsp;&nbsp;<html:submit styleClass="button" value="�����"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>
