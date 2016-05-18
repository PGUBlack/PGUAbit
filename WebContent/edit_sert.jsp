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

<logic:notPresent name='editSertAction' scope='request'>
 <logic:redirect forward='edit_sert'/>
</logic:notPresent>

<logic:notPresent name="editSertForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! String kP = "0"; %>

<SCRIPT LANGUAGE="JavaScript">
var teamLength

function checkFields()
{
 if(document.forms(0).kodFakulteta.value == "-")
 {
  alert("���������� ������� ���������");
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
 fillSelect(document.forms(0).kodFakulteta.value);
 document.forms(0).kodFakulteta.focus();
}

function exec2() {
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
j=0
// �������� ������������ �����
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// ���������� ����� �����
for(i = 0; i < teamLength; i++) {
   if((teamVAL[i]).substring(0,(teamVAL[i]).indexOf("$")) == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     j++
   }
}
// ������� � ������ ������
document.forms(0).special1.options[0].selected = true
}
</SCRIPT>

<logic:present name="editSertForm" property="action">
<bean:define id="action" name="editSertForm" property="action"/>

<%-----------------------------------------------------------------%>
<%-------------    ��������� ���������� �������������   -----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="translate">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">������������� ������������ � ������ ���</template:put>
<template:put name="sub_name">������� ���������</template:put>
<template:put name='title'>������������� ������������ � ������ ���</template:put>
<template:put name='content'>
<BR>
<html:form action="/edit_sert?action=show" onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
  <tr>
    <td><font class="text_10">&nbsp;���������:&nbsp;</font></td>
    <td>
      <html:select onchange="fillSelect(this.value)" styleClass="select_f2" name="abit_ENS" property="kodFakulteta">
       <html:option value="-"/>
       <html:options collection="abit_ENS_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select>
    </td>  
  </tr>
  <tr>
    <td><font class="text_10">&nbsp;�������������:&nbsp;</font></td>
    <td align=left>
      <html:select styleClass="select_f2" name="abit_ENS" property="special1">
       <html:options collection="abit_ENS_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
    </td>
  </tr>
<%--   <tr>
     <td><font class="text_10">&nbsp;�����&nbsp;�������:&nbsp;</font></td>
     <td>
      <html:text accesskey="�" styleClass="text_f10_short" name="abit_ENS" size='12' maxlength='50' property="shifrMedali"/>
     </td>
  </tr> --%>
<!--   <tr> -->
<!--      <td><font class="text_10">&nbsp;�����&nbsp;�����������:&nbsp;</font></td> -->
<!--      <td valign=center> -->
<%--       <html:select name="abit_ENS" styleClass="select_f2" property="kopijaSertifikata"> --%>
<%--         <html:option value="%">*</html:option> --%>
<%--         <html:option value="-">-</html:option> --%>
<%--         <html:option value="n">���</html:option> --%>
<%--         <html:option value="y">��</html:option> --%>
<%--       </html:select> --%>
<!--      </td> -->
<!--   </tr> -->
  <tr>
     <td><font class="text_10">&nbsp;���������&nbsp;����&nbsp;������:&nbsp;</font></td>
     <td valign=center>
      <html:select name="abit_ENS" styleClass="select_f2" property="special3">
        <html:option value="not">���</html:option>
        <html:option value="all_balls">��</html:option>
      </html:select>
     </td>
  </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="�������������"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/edit_sert.do">
  <td><html:submit styleClass="button" property="exit" value="�����"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%---------------   ������������� � �� ����������   ---------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="results">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">������������� ������������ � ������ ���</template:put>
<template:put name='title'>������������� ������������ � ������ ���</template:put>
<template:put name='content'>
<br>
<p align=center>
<font class="text_10"><bean:write name="abit_ENS" property="fakultet"/></font><br>
<font class="text_10"><bean:write name="abit_ENS" property="special2"/></font>
<br>
<font class="text_10"><bean:write name="abit_ENS" property="number"/> �����������(��)</font>
<br>
<html:form action="/edit_sert?action=save">
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
     <td rowspan=2 valign=center align="center">&nbsp;�&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������&nbsp;�.�.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;&nbsp;&nbsp;�����&nbsp;&nbsp;&nbsp;<br>&nbsp;&nbsp;����.&nbsp;<br>&nbsp;����&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;&nbsp;&nbsp;�������&nbsp;&nbsp;</td>
<!--      <td rowspan=2 valign=center align="center">&nbsp;����&nbsp;<BR>&nbsp;&nbsp;&nbsp;���.&nbsp;</td> -->
     <td rowspan=2 valign=center align="center">&nbsp;�������&nbsp;<br>&nbsp;������.&nbsp;</td>
     <td rowspan=2 valign=center align="center">&nbsp;���.&nbsp;</td>
<!--      <td rowspan=2 valign=center align="center">&nbsp;������.&nbsp;</td> -->
<!--      <td rowspan=2 valign=center align="center">&nbsp;�����&nbsp;<br>&nbsp;����.&nbsp;</td> -->
<!--      <td rowspan=2 valign=center align="center">&nbsp;������.&nbsp;���&nbsp;</td> -->
     <td rowspan=2 valign=center align="center" width="50">&nbsp;&nbsp;�����&nbsp;<br>&nbsp;&nbsp;&nbsp;&nbsp;���&nbsp;<br>&nbsp;������.&nbsp;</td>
     <td colspan=30 valign=center align="center">&nbsp;��������&nbsp;/&nbsp;�����&nbsp;���&nbsp;</td>
  </tr>
  <tr>
     <logic:iterate id="pr" name="predms" scope='request'>
      <td align=center>&nbsp;<bean:write name="pr" property="sokr"/>&nbsp;</td>
     </logic:iterate>
  </tr>
</thead>
<% int number=0; 
   int old_kod=-1;
%>
<logic:iterate id="abit" name="abit_ENS_S1" scope='request' type="abit.bean.AbiturientBean">
<% if(StringUtil.toInt(abit.getKodAbiturienta()+"",-10) == old_kod) { %>
      <td align=center><html:text styleClass="text_f9_short" property="<%=\"ege\"+abit.getKodAbiturienta()+\"%\"+abit.getKodPredmeta()%>"
                 value="<%=""+abit.getOtsenkaegeabiturienta()%>"
                     maxlength='3' size='2' /></td>
<% } else { %>
  <tr>
     <td align=center><font class="text_10">&nbsp;<%=++number%>&nbsp;</font></td>
     <td><font class="text_10">&nbsp;&nbsp;<bean:write name="abit" property="familija"/>&nbsp;&nbsp;</font></td>
     <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="nomerLichnogoDela"/>&nbsp;</font></td>
     <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="pasport"/>&nbsp;</font></td>
<%--      <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="shifrMedali"/>&nbsp;</font></td> --%>
     <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="prinjat"/>&nbsp;</font></td>
     <td align=center><html:text styleClass="text_f9_short" property="<%=\"sob\"+abit.getKodAbiturienta()%>"
                 value="<%=""+abit.getNpd1()%>"
                     maxlength='3' size='3' /></td>
<%--      <td align=center><html:text styleClass="text_f9_short" property="<%=\"att\"+abit.getKodAbiturienta()%>" --%>
<%--                  value="<%=""+abit.getAttestat()%>" --%>
<%--                      maxlength='1' size='1' /></td> --%>
<%--      <td align=center><html:text styleClass="text_f9_short" property="<%=\"kop\"+abit.getKodAbiturienta()%>" --%>
<%--                  value="<%=abit.getKopijaSertifikata()%>" --%>
<%--                      maxlength='1' size='1' /></td> --%>
<%--      <td align=center>&nbsp;<html:text styleClass="text_f9_short" property="<%=\"nom\"+abit.getKodAbiturienta()%>" --%>
<%--                  value="<%=""+abit.getNomerSertifikata()%>" --%>
<%--                      maxlength='30' size='16' />&nbsp;</td> --%>
     <td align=center><html:text styleClass="text_f9_short" property="<%=\"lck\"+abit.getKodAbiturienta()%>"
                 value="<%=""+abit.getLocked()%>"
                     maxlength='1' size='1' /></td>
     <td align=center><html:text styleClass="text_f9_short" property="<%=\"ege\"+abit.getKodAbiturienta()+\"%\"+abit.getKodPredmeta()%>"
                 value="<%=""+abit.getOtsenkaegeabiturienta()%>"
                     maxlength='3' size='2' /></td>

<% old_kod = StringUtil.toInt(abit.getKodAbiturienta()+"",-10); %>
<% } %>
</logic:iterate>
<logic:notPresent name="abit" property="familija">
<tr><td align=center valign=center colspan=30>
<p align=center>
     <font class="text_9">&nbsp;��&nbsp;������&nbsp;�������������&nbsp;���&nbsp;������������,&nbsp;���������������&nbsp;���������&nbsp;������</font></td>
  </td>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr>
   <td>
      <html:submit styleClass="button" property="back" value="��������� ���������"/>&nbsp;&nbsp;
   </td>
   <td>
</html:form>
<html:form action="/edit_sert?action=translator">
      <html:submit styleClass="button" value="� ���������"/>&nbsp;&nbsp;
   </td>
</html:form>
<html:form action="/edit_sert.do">
   <td>
      <html:submit styleClass="button" property="exit" value="�����"/>
   </td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>