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

<logic:notPresent name="searchTxtAction" scope="request">
<logic:redirect forward="search_txt"/>
</logic:notPresent>

<logic:notPresent name="searchForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<script language="JavaScript">

function resetSelectorFields(){
    document.forms(0).special1.checked = true;
    document.forms(0).special3.focus();
}

function corrector()
{
// if(document.forms(0).special3.value.length==0){   
//   alert("��������! ���������� ������ ��� ����� ������.");
//   document.forms(0).special3.focus();
//   return false;
// }
 if(document.forms(0).resultString.value == "" || document.forms(0).resultString.value == "%" || document.forms(0).resultString.value == "%%")
  {
   alert("���������� ������� ���� �� 1 ����");
   return false;
  }

 if(document.forms(0).special1.checked != true)
  {

   var need_percents = 0;

   var str=document.forms(0).resultString.value;

// ��� ������, ���������� �� ������� ������������, ����� ����� 14 ������ % � ������ ������

   if(str.indexOf("nomerSertifikata") != -1) need_percents = 14;

// ��� ������, ���������� �� ����� � ����� ��������, ����� ����� 15 ������ % � ������ ������

   if(str.indexOf("nomerDokumenta") != -1) need_percents = 15;

   var num_of_percents = 0;

   for(var i=0;i<str.length;i++) if(str.charAt(i) == "%") num_of_percents++;

   need_percents = need_percents - num_of_percents

   while( need_percents-- > 0 )
   {
    str += "%";
   }

   document.forms(0).resultString.value = str;
  }
}

var str="";
var arrnames= new Array();
var str1=""

function myfun(oObject) {
  if(event.keyCode == 48||event.keyCode == 49 ||event.keyCode == 50||event.keyCode == 51||event.keyCode == 52||event.keyCode ==53||event.keyCode == 54||event.keyCode == 55||event.keyCode == 56||event.keyCode == 57||event.keyCode == 8||event.keyCode == 46)
  {
   //����� � ������ ����� � ������ 
     var h='|'+oObject.name  //�����
     var dlsl=h.length
     var dlstr=str.length
     var i=0;
     var find =false
     while(i<str.length-1)
     {
      var ch=str.substr(i,dlsl)
      if(ch==h)
      {
       str=str+'%'
   
      //�������� �� ������� �������� 
       if(oObject.value==""){str=str.replace(str.substring(str.substring(0,i).lastIndexOf('%'),str.indexOf('%',i)),"");}  
       else {str=str.replace(str.substring(str.substring(0,i).lastIndexOf('%'),str.indexOf('%',i)),'%'+oObject.value+'|'+oObject.name);}
   
       str=str.substring(0,str.length-1)
    
       i = i+dlsl-1
   
       find=true
      }
      else i++
     }
     if(find==false){str+='%'+oObject.value+'|'+oObject.name;}
     str=str+'%'
    //��������� �������� � ������
     arrnames=new Array()
     var j=0;
     //str1=""
     while(j<str.length-1)
     {
       if(str.substring(j+1,str.indexOf('|',j)).length==1)
       {
        arrnames[arrnames.length]='0'+str.substring(j+1,str.indexOf('%',j+1))}
        else{arrnames[arrnames.length]=str.substring(j+1,str.indexOf('%',j+1))
       }
       
       j=str.indexOf('%',j+1);
     }
     //����������
     arrnames.sort ()  
    //��������� �������������� ������
     if( document.forms(0).special3.value=="")
      {
       str1="";
      }
      else {
       //str1="%zagolovok";
       str1="";
      }
      
     for(var i=0;i<arrnames.length;i++)
     {
      if(arrnames[i].substring(0,arrnames[i].indexOf('|'))!="")
      {
       if(str1 != "") str1+='%'+arrnames[i].substring(arrnames[i].indexOf('|')+1,arrnames[i].length)
       else str1+=arrnames[i].substring(arrnames[i].indexOf('|')+1,arrnames[i].length)
      }
     }
    document.forms(0).resultString.value=str1+'%';
    str=str.substring(0,str.length-1)
 }
 
}
</script>

<logic:present name="searchForm" property="action">
<bean:define id="action" name="searchForm" property="action"/>

<body onLoad="resetSelectorFields()"></body>

<%-- ����������� ����������� ������ --%>
<logic:equal name="action" value="printForm">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">���������� � ������</template:put>
<template:put name="target_name">���������� ����������� ������ � ������</template:put>
<template:put name="content">

<html:form action="/search_txt?action=makeTXT" onsubmit="return corrector();">
<table border=0 cellSpacing=0 cellPadding=0 align=center>
<tbody>
<logic:notPresent name="abit_Srch" property="special22">
<tr><td height=10></td></tr>
</logic:notPresent>
<tr>
    <td align=center vAlign=center>
    <font class="text_9">��������&nbsp;������:&nbsp;&nbsp;</font>
    <html:text styleClass="text_f9" name="abit_Srch" property="special3" value="" size='98' maxlength='250'/>
    </td>
</tr>
<tr><td height=10></td></tr>
<tr><td colspan=2>
<table border=0 cellSpacing=0 cellPadding=0 align=center>
<tr align= center><td colspan=8 align=center><font class="text_9">������� ����� ������� ������ ���� �������� � ����� � ������� �� ����������</font></td></tr>
<tr><td height=15></td></tr>
    <td align=left vAlign=center><font class="text_9">��������� ��������:</font></td>
    <td align=left vAlign=middle><html:text styleClass="text_f9_short" name="abit_Srch" property="dokumentyHranjatsja" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������� ����� ���.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tipDokSredObraz" size='2' onkeyup ="myfun(this)" maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerLichnogoDela" onkeyup ="myfun(this)"  size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">�������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="familija" onkeyup ="myfun(this)"  size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">���:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="imja" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="otchestvo" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">�����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="shifrKursov" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">�������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="medal" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="lgoty" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">����� �������� ��������:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerPlatnogoDogovora" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">���� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="dataRojdenija" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��� �����. �����. ���.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="godOkonchanijaSrObrazovanija" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">���:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="pol" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">���� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="srokObuchenija" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">�����������:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="grajdanstvo" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">��� ������� �����. ���.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="gdePoluchilSrObrazovanie" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� �����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerShkoly" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="inostrannyjJazyk" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">�������. � ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nujdaetsjaVObschejitii"  onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��������� �����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nazvanie" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">�����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nazvanieRajona" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">�������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nazvanieOblasti" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������. �����. �����.:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="polnoeNaimenovanieZavedenija" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��� �����. ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tipOkonchennogoZavedenija" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">�������� ������������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="trudovajaDejatelnost" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="gruppa" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����������� �� ������.:&nbsp;</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="napravlenieOtPredprijatija" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">��� ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tipDokumenta" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="seriaDokumenta" size='2' onkeyup ="myfun(this)" maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerDokumenta" size='2' onkeyup ="myfun(this)" maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">���� ������ ����.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="dataVydDokumenta" size='2' onkeyup ="myfun(this)" maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��� ����� ����.:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="kemVydDokument" size='2' onkeyup ="myfun(this)" maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="tselevojPriem" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">��������� ����:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="ball" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">�������������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="sobesedovanie" size='2' onkeyup ="myfun(this)" maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="prinjat" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="abbreviaturaFakulteta" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����-�� ����������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="kodSpetsialnZach" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">C���-�� �����������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="abbreviatura" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>    
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">����� �����������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerSertifikata" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� �����������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="kopijaSertifikata" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="formaOb" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center><font class="text_9">����� ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="seriaAtt" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">����� ���������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="nomerAtt" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
    <td width=13></td>
    <td align=left vAlign=center><font class="text_9">������ ��������:</font></td>
    <td align=left vAlign=middle><html:text  styleClass="text_f9_short" name="abit_Srch" property="osnovaOb" onkeyup ="myfun(this)" size='2' maxlength='2'/></td>
</tr>
<tr>
    <td align=left vAlign=center colspan="8">&nbsp;</td>
</tr>
<tr>
    <td align=center vAlign=center colspan="8">&nbsp;<font class="text_9">����������������&nbsp;������&nbsp;������:&nbsp;</font><html:checkbox styleClass="checkbox_1" name="abit_Srch" property="special1"/>&nbsp;</td>
</tr>
<tr>
    <td align=left vAlign=center colspan="8">&nbsp;</td>
</tr>
</table>
<table border="1" cellspacing="0" cellpadding="0" align=center>
<tr>
<%int i=1;%>
<logic:iterate id='lists_3' name='lists_3' scope='request'>
   <bean:define id='output' name='lists_3' type="abit.bean.AbiturientBean" />
   <td align=center vAlign=center colspan=4><font class="text_9"colspan="8"><bean:write name="lists_3" property="nazvaniePredmeta"/>:</font>
   &nbsp;&nbsp;&nbsp;<input type="text" Class="text_f9_short" name="predmet<%=String.valueOf(output.getNazvaniePredmeta())%>(<%=String.valueOf(output.getPredmet())%>)"onkeyup ="myfun(this)" size='2' maxlength='2' tabindex="<%=String.valueOf(i)%>"></td>
  
<%i=i+5;%>
</logic:iterate>
</tr>
<tr>
<logic:iterate id='list' name='lists_3' scope='request'>
  <td align="center" vAlign=center><font class="text_9">���.</font></td>
  <td align="center" vAlign=center><font class="text_9">���</font></td>
  <td align="center" vAlign=center><font class="text_9">�����.</font></td>
  <td align="center" vAlign=center><font class="text_9">���.</font></td>
</logic:iterate>
</tr>
<tr>
<%i=2;%>
<logic:iterate id='lists_4' name='lists_4' scope='request'>
   <bean:define id='output1' name='lists_4' type="abit.bean.AbiturientBean" />
   <td align="center" vAlign=middle><input type="text" Class="text_f9_short" name="exzamOtsenkaPo<%=String.valueOf(output1.getNazvaniePredmeta())%>(<%=String.valueOf(output1.getPredmet())%>)"onkeyup ="myfun(this)" size='2' maxlength='2' tabindex="<%=String.valueOf(i++)%>"></td>
   <td align="center" vAlign=middle><input type="text" Class="text_f9_short" name="egePo<%=String.valueOf(output1.getNazvaniePredmeta())%>(<%=String.valueOf(output1.getPredmet())%>)"onkeyup ="myfun(this)" size='2' maxlength='2' tabindex="<%=String.valueOf(i++)%>"></td>
   <td align="center" vAlign=middle><input type="text" Class="text_f9_short" name="zaiavlOtsenkaPo<%=String.valueOf(output1.getNazvaniePredmeta())%>(<%=String.valueOf(output1.getPredmet())%>)"onkeyup ="myfun(this)" size='2' maxlength='2' tabindex="<%=String.valueOf(i++)%>"></td>
   <td align="center" vAlign=middle><input type="text" Class="text_f9_short" name="atestatOtsenkaPo<%=String.valueOf(output1.getNazvaniePredmeta())%>(<%=String.valueOf(output1.getPredmet())%>)"onkeyup ="myfun(this)" size='2' maxlength='2' tabindex="<%=String.valueOf(i++)%>"></td>
 <%i++;%>
</logic:iterate>
</tr>
</table>
<table border="0" align=center>
<tr><td height=4></td></tr>
<tr>
    <td align=left vAlign=center colspan="2"><font class="text_9">���������:</font></td>
    <td align=left vAlign=middle colspan="6"><html:text  styleClass="text_f9_short" name="abit_Srch" property="resultString" value="" size='120'/></td>   
</tr>
</table>
</td></tr>
</tbody>
</table>

<table align=center border=0>
<tr>
<%-- <td align=left>&nbsp;&nbsp;&nbsp;&nbsp;<html:submit styleClass="button" value="�����������" tabindex="100"/></td> --%>
<td align=left>&nbsp;&nbsp;<html:submit styleClass="button" value="������������ � ��������� � ���" property="toFIS" tabindex="101"/></td>
</html:form>
<html:form action="/abit_srch.do">
<td align=left>&nbsp;<html:submit styleClass="button" value="�����" tabindex="300" property="exit"/></td>
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