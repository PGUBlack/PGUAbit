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

<logic:notPresent name='summaryAction' scope='request'>
 <logic:redirect forward='summary'/>
</logic:notPresent>

<logic:notPresent name="summaryForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var teamLength
var predmetsLength
var pred
function sohrSel() 
{
//���������� ���� ��������
 document.forms(0).kodPredmeta.options[0].value =  document.forms(0).kodPredmeta.options[document.forms(0).kodPredmeta.selectedIndex].value
//���������� ����
 document.forms(0).special1.options[document.forms(0).special1.selectedIndex].value = document.forms(0).special1.options[document.forms(0).special1.selectedIndex].text
}

function invokeAct(){
  document.forms(0).submit();
}

function checkFields(){
if(document.forms(0).kodPredmeta.value == "-"){
  alert("���������� ������� �������");
  document.forms(0).kodPredmeta.focus();
  return false;
}
}
function exec() {
if( document.forms(0).kodPredmeta!= null &&
    document.forms(0).special1!= null) {
 	document.forms(0).kodPredmeta.selectedIndex=0;
 	teamLength = document.forms(0).special1.options.length
 	teamTXT = new Array(teamLength)
 	teamVAL = new Array(teamLength)
 	for(var ind=0;ind<document.forms(0).special1.length;ind++) 
	{
    		eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")//text
    		eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
 	}
 	fillSelect("-");
 } 
if( document.forms(0).kodPredmeta == null && document.forms(0).special5 != null) {
 document.forms(0).special5.focus();
}
pr.style.display = "block";
dat.style.display = "block";
}

function fillSelect(selectCtrl) {
	var i,j,z=0
	if(selectCtrl == "-") {
		// �������� ������������ �����
		for (i =document.forms(0).special1.length; i >=0 ; i--) {
			document.forms(0).special1.options[i] = null
		}
		// ���������� ����� "-"
		document.forms(0).special1.options[0] = new Option("-")
		document.forms(0).special1.options[0].value = "-"
		document.forms(0).special1.options[0].selected = true
		return
	}
	j=0
	// �������� ������������ �����
	for (i = document.forms(0).special1.length; i >= 0; i--) {
		document.forms(0).special1.options[i] = null 
	}
// ���������� ����� �����
	for(i = 0; i < teamLength; i++) {
		if(teamVAL[i].substring(0,2 ) == selectCtrl) {
			document.forms(0).special1.options[j] = new Option(teamTXT[i])
		  	document.forms(0).special1.options[j].value = teamVAL[i]
			z++
			j++
	   	}
	}

	// ������� � ������ ������
	if(z!=0)	document.forms(0).special1.options[0].selected = true
	else
	{
	 document.forms(0).special1.options[0] = new Option("-")
	 document.forms(0).special1.options[0].value = "-"
	 document.forms(0).special1.options[0].selected = true
	}
}
</SCRIPT>

<logic:present name="summaryForm" property="action">
<bean:define id="action" name="summaryForm" property="action"/>

<logic:equal name="action" value="view">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">������ �� ��������</template:put>
<template:put name="sub_name">�������� ����</template:put>
<template:put name='title'>������ �� ��������</template:put>
<template:put name='content'>
<body onLoad="exec()"></body>
<BR>
<p align=center>
<html:form action="/summary?action=viewing" onsubmit="return checkFields();">
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">�������:</font></td>
      <td id="pr" style="display:none"><html:select styleClass="select_f2" onchange="fillSelect(this.value)" name="abit_O" property="kodPredmeta" tabindex="1">
          <html:option value="-"/>
          <html:options collection="abit_O_S1" property="kodPredmeta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 

  <tr><td><font class="text_10">���� ��������:</font></td>
      <td id="dat" style="display:none"><html:select styleClass="select_f2"  name="abit_O" property="special1" tabindex="2">
          <html:options collection="abit_O_S2" property="special1" labelProperty="dataJekzamena"/>
      </html:select>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" onclick="sohrSel()" tabindex="5" value="��������"/></td>
</html:form>
<html:form method="post" action="/summary?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="7" value="�����"/></td>
</html:form>
</tr></table>
<br>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>������ �� ��������</template:put>
<template:put name="target_name">������ �� ��������</template:put>
<template:put name='content'>
<BR>
<html:form action="/summary?action=report">
  <html:hidden name="abit_O" property="kodPredmeta"/>
  <html:hidden name="abit_O" property="special1"/>
<font class="text_11">
�������:&nbsp;<bean:write name="abit_O" property="nazvanie"/>
<br><br>����:&nbsp;<bean:write name="abit_O" property="data"/>
</font>
<br><br>
<table align=center border=1 cellSpacing=0>
<thead>
<tr>
<td align=center><font class="text_th">&nbsp;���������&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;10&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;9&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;8&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;7&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;6&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;5&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;4&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;3&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;2&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;1&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;0&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;10&nbsp;�&nbsp;0&nbsp;</font></td>
<td align=center><font class="text_th">&nbsp;�&nbsp;9&nbsp;��&nbsp;1&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_O" name="abits_O" scope='request'>
<tr valign=middle>
  <td align=center><font class="text_10">&nbsp;<bean:write name="abit_O" property="abbreviaturaFakulteta"/>&nbsp;</font></td>
  <logic:iterate id="curr_note" collection="<%=((abit.bean.AbiturientBean)abit_O).getNotes()%>" type="java.lang.String">
    <td align=center><font class="text_10">&nbsp;<%=curr_note%>&nbsp;</font></td>
  </logic:iterate>
</tr>
</logic:iterate>
</table>
<table align=center border=0>
<tr><td height=15></td></tr>
<tr>
<td><html:submit styleClass="button" value="������� �����"/></td>
</html:form>
<html:form action="/summary.do?action=view">
  <td align=center>&nbsp;&nbsp;<html:submit styleClass="button" value="�����"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>