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

<logic:notPresent name='totalMedAction' scope='request'>
 <logic:redirect forward='rep_total_m_p'/>
</logic:notPresent>

<logic:notPresent name="totalMedForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function invokeAct(){
  document.forms(0).submit();
}

function help_me() {
  alert("����� ''0'' �������� � ���� ������������, �� �������������� �� �������");
  return true;
}

</SCRIPT>

<logic:present name="totalMedForm" property="action">
<bean:define id="action" name="totalMedForm" property="action"/>

<%---------------- ���������� �������� ----------------------------%>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>����� ���������� �� �������</template:put>
<template:put name="target_name">��������� �� �������</template:put>
<template:put name="sub_name">�������� �����</template:put>
<template:put name='content'>
<html:form action="rep_total_m_p?action=show">
<table align=center border=0 cellspacing=0>
  <tr><td align=left colspain=2><font class="text_10">����� ������:</font></td>
      <td align=center colspain=2>
      <html:select onchange="invokeAct()" styleClass="select_f2" name="abit_TM" property="nomerPotoka">
       <html:options collection="abit_TM_S1" property="nomerPotoka"/>
      </html:select>
      </td>
   </tr>
  <tr><td align=left colspain=2><font class="text_10">�������:</font></td>
      <td align=center colspain=2>
      <html:select onchange="invokeAct()" styleClass="select_f2" name="abit_TM" property="kodMedali">
	<html:options collection="abit_TM_S2" property="kodMedali" labelProperty="shifrMedali"/>
      </html:select>
      </td>
   </tr>
  <tr><td align=left colspain=2><font class="text_10">�����.&nbsp;���.:</font></td>
      <td align=center colspain=2>
      <html:select onchange="invokeAct()" styleClass="select_f2" name="abit_TM" property="podtvMed">
      <html:option value="%">*</html:option>
      <html:option value="�">��</html:option>
      </html:select>
      </td>
   </tr>
</table>
</html:form>
<table align=center border=1 cellspacing=0>
<tr><td><font class="text_th">&nbsp;���������&nbsp;</font></td>
    <td><font class="text_th">&nbsp;�����&nbsp;</font></td></tr>
<%-- HEAVY LOGIC INTERFACE BUILDER --%>
      <logic:iterate id="abit_TM5" name="abit_TM_S5" scope='request'>
      <tr><td align=center><font class="text_11">
	<bean:write name="abit_TM5" property="abbreviaturaFakulteta"/></font></td>
	<td align=center><font class="text_11">
	<bean:write name="abit_TM5" property="vsegoMed"/></font></td></tr>
      </logic:iterate>
<logic:notPresent name="abit_TM5" property="abbreviaturaFakulteta">
<tr><td colspan=2 align=center valign=center>
    <font class="text_th">&nbsp;���&nbsp;��������&nbsp;</font></td></tr>
</logic:notPresent>
</table>
<br>
<table align=center border=0 cols=2>
<tr valign="middle">
  <html:form action="/rep_total_m_p.do?action=report">
 <td align=center>
   <html:submit styleClass="button" value="������� �����"/>
 </td>
  </html:form>
 <td align=center>
  <html:button styleClass="button" onclick="help_me();" property="hlp" value="�������"/> 
 </td>
  <html:form action="/rep_total_m_p.do">
 <td align=center>
  <html:submit styleClass="button" property="exit" value="�����"/>
 </td>
  </html:form>
</tr>
</table>

</template:put>
</template:insert>
</logic:present>