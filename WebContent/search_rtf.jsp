<%@ page contentType = "text/html;charset=windows-1251"
         language = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name="searchAction" scope="request">
 <logic:redirect forward="search_rtf"/>
</logic:notPresent>

<logic:notPresent name="searchForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<script language="JavaScript">

var fields = new Array("dokumentyHranjatsja","tipDokSredObraz","nomerLichnogoDela","familija","imja","otchestvo","shifrKursov","medal","lgoty","nomerPlatnogoDogovora","special2","special5","pol","srokObuchenija","grajdanstvo","gdePoluchilSrObrazovanie","nomerShkoly","inostrannyjJazyk","nujdaetsjaVObschejitii","nazvanie","nazvanieRajona","nazvanieOblasti","polnoeNaimenovanieZavedenija","tipOkonchennogoZavedenija","trudovajaDejatelnost","gruppa","napravlenieOtPredprijatija","tipDokumenta","seriaDokumenta","nomerDokumenta","dataVydDokumenta","kemVydDokument","tselevojPriem","special7","sobesedovanie","prinjat","shifrFakulteta","special1","special8","attestat","kopijaSertifikata","formaOb","seriaAtt","nomerAtt","osnovaOb","preemptiveRight","providingSpecialCondition","dopAddress","returnDocument");
var cols;
var spec = 0;
function resetSelectorFields(){
    document.forms(0).special3.focus();
}

function corrector(){

for(var i=0;i<fields.length;i++) cols+= eval("document.forms(0)."+fields[i]+".value")
if(cols == "")	//���� ����� ����������� ���� ����� ����� ������ ������ ��:
{
 alert("���������� ������� ���� �� ���� �������");
 document.forms(0).dokumentyHranjatsja.focus();
 return false;
} else {

//********** ��������, ������ �� �������� ����� � �������� ***********
for(var i=0;i<fields.length;i++) { 
 if(eval("document.forms(0)."+fields[i]+".value >"+fields.length))
  {
   alert("������ ������� �� ������ ��������� �� ����������");
   eval("document.forms(0)."+fields[i]+".focus()");
   return false;
  }
}
 spec = 0;
 teamTXT = new Array(fields.length);	
 for(var ind=0;ind<fields.length;ind++) {
    eval("teamTXT["+ind+"]=null");	//��������� �������
 }

//���� ��������� ������ �������. ���� � ���� ������������ ����� �� ���������� ��� � ������
//������� � �������� = �����, ���� ������ �����, ����� ���������� ��� ������ ���� ���������
//������, �� ���� ������� ��� ������ ����� ��� ����������� ������, � ������� ����� ���������
//�� ������.

for(var i=0;i<fields.length;i++)
{
 if(eval("document.forms(0)."+fields[i]+".value != ''"))
  {
   if(eval("teamTXT[document.forms(0)."+fields[i]+".value] != null"))
   {
     alert("������ ��������� ������ ��������");
     eval("document.forms(0)."+fields[i]+".focus()");
     return false;
   }
   spec++;
   eval("teamTXT[document.forms(0)."+fields[i]+".value] = document.forms(0)."+fields[i]+".value");
  }
}

//----------------------------------------------------------------
   if(eval("teamTXT["+0+"]") != null)	// ���� ������� ������ ��������� ������ � �������� ������ ������� 
					// ��� ����� 0, ���� ������ ������, ������� ����� ���������.
   {
    alert("��������� �������� ������� �������� � 1");
    eval("document.forms(0).dokumentyHranjatsja.focus()");
    return false;
   }

//���� � ������ spec ������� ������� ���������� null, ������ ����� �������
//��� ��������. ������ ��������� �� ������.
   while(spec)	
   {
    if(eval("teamTXT["+spec+"]") == null)
    {
     alert("������ ���������� ������ ��������");
     return false;
    }
    spec--;
   }
 }
}
</script>

<body onLoad="resetSelectorFields()"></body>

<logic:present name="searchForm" property="action">
<bean:define id="action" name="searchForm" property="action"/>

<%-- ����������� ����������� ������ --%>
<logic:equal name="action" value="printForm">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">���������� � ������</template:put>
<template:put name="target_name">���������� ����������� ������ � ������</template:put>
<template:put name="content">

<logic:present name="abit_Srch" property="special22">
<%---------------- ������� �� ������ ������� ----------------%>
<font class="text_th">������ �������������� ������� ������� ������. ���������� ��������� ���������� ��������.</font>
</logic:present>

<html:form action="/search_rtf?action=makeRTF" onsubmit="return corrector();">
<table border=0 cellSpacing=0 cellPadding=0>
<tbody>

<logic:notPresent name="abit_Srch" property="special22">
<tr><td height=10></td></tr>
</logic:notPresent>

<tr>
    <td align=left vAlign=center width=13><font class="text_9">���������:</font></td><td><html:text styleClass="text_f9" name="abit_Srch" property="special3" value="���������� ��������������� �����������" size='98' maxlength='250'/></td>
</tr>

<tr><td height=10></td></tr>

<tr>
    <td align=left vAlign=center><font class="text_9">������������:</font></td><td><html:textarea styleClass="text_f9" name="abit_Srch" property="special4" cols='100' rows='3' /></td>
</tr>
<tr><td height=10></td></tr>
<tr><td colspan=2>
<table border=0 cellSpacing=0 cellPadding=0>
<tr align= center><td colspan=8 align=center><font class="text_9">������� ������� ���������� ��������, ������� ������ ���� �������� � �����</font></td></tr>
<tr><td height=15></td></tr>
     <td align=left vAlign=center><font class="text_9">������� ����� ���.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tipDokSredObraz" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerLichnogoDela" size='2' maxlength='2'/></td>
       <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="prinjat" size='2' maxlength='2'/></td>

</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">�������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="familija" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">���:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="imja" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="otchestvo" size='2' maxlength='2'/></td>
</tr>

<tr>
    <td align=left vAlign=center><font class="text_9">����� �������� ��������:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerPlatnogoDogovora" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">���� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="special2" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��� �����. �����. ���.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="special5" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">���:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="pol" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">�����������:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="grajdanstvo" size='2' maxlength='2'/></td>
     <td width=13></td>
     <td align=left vAlign=center><font class="text_9">������ �����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="lgoty" size='2' maxlength='2'/></td>
          
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">��� ������� �����. ���.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="gdePoluchilSrObrazovanie" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� �����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerShkoly" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="inostrannyjJazyk" size='2' maxlength='2'/></td>
</tr>
<tr>

    <td align=left vAlign=center><font class="text_9">��������� �����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nazvanie" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������. �����. �����.:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="polnoeNaimenovanieZavedenija" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��� �����. ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tipOkonchennogoZavedenija" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="gruppa" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����������� �� ������.:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="napravlenieOtPredprijatija" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">��� ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tipDokumenta" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="seriaDokumenta" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerDokumenta" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">���� ������ ����.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="dataVydDokumenta" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��� ����� ����.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="kemVydDokument" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tselevojPriem" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">��������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="special7" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="prinjat" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="shifrFakulteta" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����-�� �����������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="special1" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����-�� ����������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="special8" size='2' maxlength='2'/></td>

</tr>
<tr>

    <td align=left vAlign=center><font class="text_9">����� �����������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="kopijaSertifikata" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����. �������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="providingSpecialCondition" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="formaOb" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">����� ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="seriaAtt" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerAtt" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������ ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="osnovaOb" size='2' maxlength='2'/></td>
</tr>
<tr>
	 <td align=left vAlign=center><font class="text_9">����� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tel" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">�������� �����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="dopAddress" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����. �������� ���.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="returnDocument" size='2' maxlength='2'/></td>
    <td width=13></td>
   

</tr>
</table>
</td></tr>
<tr><td height=10></td></tr>
<tr><td align=left vAlign=center><font class="text_9">�������:</font></td><td><html:textarea styleClass="text_f9" name="abit_Srch" property="special6" cols='100' rows='3' /></td>
</tr>
<tr><td height=10></td></tr>
</tbody>
</table>

<table align=center border=0>
<tr>
<td colspan=8 height=10>
<font class="text_9">���������� ��������:</font>
<html:select styleClass="select_f1" name="abit_Srch" property="priznakSortirovki">
<html:option value="�������"/>
<html:option value="���������"/>
</html:select>
</td>
<td align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:submit styleClass="button" value="�����������" tabindex="1"/></td>
</html:form>
<html:form action="/abit_srch.do">
<td align=left>&nbsp;<html:submit styleClass="button" value="�����" tabindex="4" property="exit"/></td>
</html:form>
</tr>
<tr><td height=10></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>

<logic:equal name="action" value="doneRTF">

// RTF - �����, ����������� ��� ������
Here be placed a test RTF-file: <a href="reports/textRTF.doc">RTF-text-file</a>
</logic:equal>
</logic:present>