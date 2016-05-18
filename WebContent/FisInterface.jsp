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



<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
  var valid = " ������������������������������������������������������������������-()."
  var temp;
}





</SCRIPT>





<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>

<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>�������� ������� � ���</template:put>
<template:put name="target_name">�������� ������� � ���</template:put>
<template:put name="sub_name">��������� �������</template:put>
<template:put name='content'>


<html:form action="fis_interface?action=toFIS" method="post" enctype="multipart/form-data" onsubmit="return checkFields();">
<table align=center border=0>

  <tr><td><font class="text_10">�������&nbsp;�����:</font></td>
      <td><html:text  styleClass="text_f10" property="address" 
                     maxlength="110" size="50" tabindex="1" value="http://10.0.3.1:8080/import/importservice.svc"/></td></tr>

  <tr><td><font class="text_10">�����:</font></td>
      <td><html:select  styleClass="select_f1" property="method" tabindex="2">
            <html:option value="/institutioninfo">/institutioninfo (��������� �������� �� ��)</html:option>
            <html:option value="/checkapplication">checkapplication (�������� ���������)</html:option>
            <html:option value="/delete">/delete (��������)</html:option>
            <html:option value="/delete/result">/delete/result (��������� ���������� ��������)</html:option>
            <html:option value="/dictionary">/dictionary (������ ������������)</html:option>
            <html:option value="/dictionarydetails">/dictionarydetails (������ �� �����������)</html:option>
            <html:option value="/import">/import (������)</html:option>
            <html:option value="/import/result">/import/result (��������� ���������� �������)</html:option>
            <html:option value="/validate">/validate (���������)</html:option>
            <html:option value="/test/checkapplication">/test/checkapplication (���� �������� ���������)</html:option>
            <html:option value="/test/delete">/test/delete (���� ��������)</html:option>
            <html:option value="/test/dictionary">/test/dictionary (���� �����������)</html:option>
            <html:option value="/test/dictionarydetails">/test/dictionarydetails (���� ������� �����������)</html:option>
            <html:option value="/test/import">/test/import (���� �������)</html:option>
          </html:select>
      </td>

  <tr>
  <tr><td><font class="text_10">��� (��������) �������� �������:</font></td>
      <td><html:text styleClass="text_f10" property="codeX" 
                     maxlength="150" size="20" tabindex="4" value="1"/></td></tr>

  <tr><td><font class="text_10">�������&nbsp;����������:</font></td>
      <td><html:text  styleClass="text_f10" property="timeOut" 
                     maxlength="5" size="3" tabindex="5" value="30"/></td></tr>

  <tr><td><font class="text_10">�����:</font></td>
      <td><html:text styleClass="text_f10" property="login" 
                     maxlength="35" size="25" tabindex="6" value="polex@pnzgu.ru"/></td></tr>

  <tr><td><font class="text_10">������:</font></td>
      <td><html:password  styleClass="text_f10" property="password" 
                     maxlength="35" size="25" tabindex="7" value="polex@pnzgu.ru"/></td></tr>

  <tr><td><font class="text_10">���� ������� (XML):</font></td>
      <td><html:file  styleClass="select_f2" size='60' property="sourceFile" tabindex="8"/></td></tr>

<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="9" value="���������"/></td>
</html:form>

</tr><tr><td colspan=2 height=50></td></tr>
</table>
<br>
</template:put>
</template:insert>


