<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

 <script src="/abiturient/jquery.js"></script>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='spetsialnostiAction' scope='request'>
 <logic:redirect forward='spetsialnosti'/>
</logic:notPresent>

<logic:notPresent name="spetsialnostiForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>


<SCRIPT LANGUAGE="JavaScript">

$(document).ready(function() {
	 // alert('Error: ');
	
	
	var eduLvl1 = $("[name = 'special10']").val();
	var eduLvl = eduLvl1.substring(0, 1);
	var fId = $("[name = 'special11']").val();
	//var eduLvl = eduLvl1.substring(0, 1);
	$("[name = 'formaOb']").val(eduLvl);
	$("[name = 'fisId']").val(fId);
	
	});
function fillCGs() {
	
	
	
  
	//var sel = document.getElementById("kodKonGrp");
	
	//var ugsText = sel.options[sel.selectedIndex].text;
	var ugsText= $("[name = 'kodKonGrp'] option:selected").text();
	
	var ugsValue = $("[name = 'kodKonGrp'] option:selected").val();
	
	var eduLvl =  $("[name = 'formaOb'] option:selected").val();
	
	if(eduLvl == '�') eduLvl = 1;
	if(eduLvl == '�') eduLvl = 2;
	if(eduLvl == '�') eduLvl = 3;
	if(eduLvl == '�') eduLvl = 4;
	if(eduLvl == '�') eduLvl = 5;
	if(eduLvl == '�') eduLvl = 6;
	if(eduLvl == '�') eduLvl = 7;
	
	
	//  $('#info').html(ugsText);
  
	  $.ajax({

	        type: "POST",
	  
	        url: "/abiturient/ajaxFillCGs.do",
	        data: {name: ugsValue, formaOb: eduLvl },
	       // data: "name=" + ugsValue,
	       
	        success: function(response){

	            // we have the response

	          //$('#info').html(response);
	            $("[name = 'fisId']").remove();
	            $('#fillTd1').html(response);

	        },

	        error: function(e){

	            alert('Error: ' + e);

	        }

	    });
	
	
}

function fillSpecName() {
	
	//var sel =  document.getElementById("fisId");
	var specText = $("[name = 'fisId'] option:selected").text();
	var specName = specText.substring(11, specText.length);	
	$("[name = 'nazvanieSpetsialnosti']").val(specName);
	 
}

function fillSpecShifr() {
	
	//var sel =  document.getElementById("fisId");
	var specText = $("[name = 'fisId'] option:selected").text();
	var specName = specText.substring(0, 8);	
	$("[name = 'shifrSpetsialnosti']").val(specName);
	 
}

function fillSpecShifrOKSO() {
	
	//var sel =  document.getElementById("fisId");
	var specText = $("[name = 'fisId'] option:selected").text();
	var specName = specText.substring(0, 8);	
	$("[name = 'shifrSpetsialnostiOKSO']").val(specName);
	 
}




function checkFields(){
var valid1 = " ������������������������������������������������������������������().,-"
var valid3 = " 1234567890.-"
var temp;
var pp_all = document.forms(0).planPriemaT2.value;
var pp_1 = document.forms(0).planPriemaT3.value;
var pp_2 = document.forms(0).planPriemaT4.value;
var pp_3 = document.forms(0).planPriemaT5.value;
var pp_4 = document.forms(0).planPriemaT1.value;

if(document.forms(0).kodKonGrp.value == "-1")
 {
  alert("���������� ������� ''���������� ������''");
  document.forms(0).kodKonGrp.focus();
  return false;
 }

if(document.forms(0).special1.value == "-")
 {
  alert("���������� ������� ''���������''");
  document.forms(0).special1.focus();
  return false;
 }

for (var i=0; i<document.forms(0).nazvanieSpetsialnosti.value.length; i++) 
 {
  temp = "" + document.forms(0).nazvanieSpetsialnosti.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
   alert("���� ''�������������'' ����� �������� ������ �� ���� �������� ��������");
   document.forms(0).nazvanieSpetsialnosti.focus();
   return false;
  }
 }
if(document.forms(0).nazvanieSpetsialnosti.value.length == 0) 
 {
  alert("���������� ��������� ���� ''�������������''");
  document.forms(0).nazvanieSpetsialnosti.focus();
  return false;
 }

if (document.forms(0).abbreviatura.value.charAt(0) != document.forms(0).special1.value.charAt(document.forms(0).special1.value.indexOf("%")+1)) {
    alert("�������� ������ ����� ������������ �������������");
    document.forms(0).abbreviatura.focus();
    return false
}
for (var i=0; i<document.forms(0).abbreviatura.value.length; i++) 
 {
  temp = "" + document.forms(0).abbreviatura.value.substring(i, i+1);
  if (valid1.indexOf(temp) == "-1") {
     alert("���� ''������������'' ����� �������� ������ �� ���� �������� ��������");
     document.forms(0).abbreviatura.focus();
     return false;
  }
 }
if(document.forms(0).abbreviatura.value.length == 0) 
 {
  alert("���������� ��������� ���� ''������������''");
  document.forms(0).abbreviatura.focus();
  return false;
 }
if(document.forms(0).tip_Spec.value == "-")
 {
  alert("���������� ������� ''��� �������������''");
  document.forms(0).tip_Spec.focus();
  return false;
 }
if(document.forms(0).shifrSpetsialnosti.value.length == 0) 
 {
  alert("���������� ��������� ���� ''����''");
  document.forms(0).shifrSpetsialnosti.focus();
  return false;
 }
for (var i=0; i<document.forms(0).shifrSpetsialnosti.value.length; i++) 
 {
  temp = "" + document.forms(0).shifrSpetsialnosti.value.substring(i, i+1);
  if (valid3.indexOf(temp) == "-1") {
   alert("���� ''����'' ����� �������� ������ �� ����, ������ ''.'' � ''-''");
   document.forms(0).shifrSpetsialnosti.focus();
   return false;
  }
 }
if(pp_all != (eval(pp_1)+eval(pp_2)+eval(pp_3)+eval(pp_4))) 
 {
  alert("�������� � ���� ''���� �� (���)'' ������ ���� ����� ����� ������ �������� ����� ��������� �������������");
  document.forms(0).planPriemaT2.focus();
  return false;
 }
if(document.forms(0).kodPredmeta.value == "-") 
 {
  alert("���������� ������� ''������������� �������''");
  document.forms(0).kodPredmeta.focus();
  return false;
 }
}

function enDis(){
  if(document.forms(0).sobesedovanie.value == "�") {
     document.forms(0).jekzamenZachet.selectedIndex = 1
  }
  else {
     document.forms(0).jekzamenZachet.selectedIndex = 0
  }
     document.forms(0).kodPredmeta.focus()
}

function gap_me(){
  document.forms(0).nazvanieSpetsialnosti.value = " ";
  document.forms(0).abbreviatura.value = " ";
  document.forms(0).shifrSpetsialnosti.value = " ";
}

function confirmation(){
  if(confirm('������� C������������ � ��� ��� � ��� �������?'))
   {
    return true;
   }
  else 
       return false; 
}

function exec() {
if(document.forms(0).special1 != null &&
   document.forms(0).sobesedovanie != null &&
   document.forms(0).kodPredmeta != null &&
   document.forms(0).jekzamenZachet != null &&
   document.forms(0).special2 == null) {

document.forms(0).special1.selectedIndex=0;
document.forms(0).kodKonGrp.selectedIndex=0;
document.forms(0).sobesedovanie.selectedIndex=0;
document.forms(0).kodPredmeta.selectedIndex=0;
document.forms(0).jekzamenZachet.selectedIndex=0;
enDis();
document.forms(0).kodKonGrp.focus();
}
if(document.forms(0).special2 != null) {
  enDis();
  document.forms(0).special1.focus();
}
}

function fillField() {
if(document.forms(0).special1.value != "-"){
  document.forms(0).abbreviatura.value = document.forms(0).special1.value.charAt(document.forms(0).special1.value.indexOf("%")+1);
}
}



/*function fillEduLevel() {
	var sel = document.getElementById("fisId");
	var fo = sel.options[sel.selectedIndex].text.charAt(9);
	 document.forms(0).formaOb.value = sel.options[sel.selectedIndex].text.charAt(9);
	 $("[name = 'formaOb']").val(fo);
		

	
	}*/
</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="spetsialnostiForm" property="action">
<bean:define id="action" name="spetsialnostiForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------------------- ���������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">������������� ����</template:put>
<template:put name="sub_name">���������� ������</template:put>
<template:put name='title'>�������������</template:put>
<template:put name='content'>
<html:form action="/spetsialnosti?action=create" onsubmit="return checkFields();">
 <div id="info" style="color: green;"></div>
<table cols=2 align=center border=0>




  <tr><td><font class="text_10">���������:</font></td>
      <td><html:select name="abit_Spec" onchange="fillField();" styleClass="select_f1" property="special1" tabindex="2">
           <html:option value="-"/>
           <html:options collection="abit_Spec_S1" property="special1" labelProperty="abbreviaturaFakulteta"/>
          </html:select>
          
               <tr><td><font class="text_10">������� �����������:</font></td>
      <td><html:select name="abit_Spec" onchange="fillCGs()"  styleClass="select_f1" property="formaOb" tabindex="2">
           <html:options collection="abit_Spec_S6" property="special1" labelProperty="special2"/>
          </html:select>
          
            <tr><td><font class="text_10">������&nbsp;��������������:&nbsp;</font></td>
      <td><html:select name="abit_Spec" onchange="fillCGs();" styleClass="select_f1" property="kodKonGrp" tabindex="1">
           <html:options collection="abit_Spec_KonGrp" property="kodKonGrp" labelProperty="nazvanie"/>
          </html:select>
          
       <tr><td><font class="text_10">������������� (���)</font></td>
      <td  id = "fillTd1"><html:select name="abit_Spec" styleClass="select_f1" property="fisId" tabindex="2">
           <html:option value="-"/>
           <html:options collection="abit_Spec_S5" property="kodSpetsialnosti" labelProperty="special3"/>
          </html:select>



  <tr><td><font class="text_10">������������� (��������):</font></td>
      <td><html:text accesskey="�"   name="abit_Spec" styleClass="text_f10" property="nazvanieSpetsialnosti" 
                     maxlength="150" size="50" tabindex="3" value=""/>
      &nbsp;&nbsp;<html:button styleClass="button" property="button" onclick="fillSpecName();" tabindex="19" value="��������� �� �����������"/>
                     
                     


  <tr><td><font class="text_10">������������:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10" property="abbreviatura" 
                     maxlength="10" size="10" tabindex="4" value=""/>

  <tr><td><font class="text_10">����� ��������:</font></td>
      <td><html:select styleClass="select_f1" name="abit_Spec" property="tip_Spec" tabindex="5">
           <html:option value="-"/>
           <html:option value="�">� - �����</html:option>
           <html:option value="�">� - �������</html:option>
           <html:option value="�">� - ��������</html:option>
          
           </html:select>

  <tr><td><font class="text_10">����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10" property="shifrSpetsialnosti" 
                     maxlength="16" size="16" tabindex="6" value=""/>
  &nbsp;&nbsp;<html:button styleClass="button" property="button" onclick="fillSpecShifr();" tabindex="19" value="��������� �� �����������"/>

  <tr><td><font class="text_10">���� ����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10" property="shifrSpetsialnostiOKSO" 
                     maxlength="16" size="16" tabindex="7" value=""/>
                       &nbsp;&nbsp;<html:button styleClass="button" property="button" onclick="fillSpecShifrOKSO();" tabindex="19" value="��������� �� �����������"/>

  <tr><td><font class="text_10">���� ����� (�����):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriema" 
                     maxlength="3" size="3" tabindex="8" value=""/>
                     
  <tr><td><font class="text_10">���� ����� (������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaLg" 
                     maxlength="3" size="3" tabindex="8" value=""/>
                                          
<tr><td><font class="text_10">���� ����� (���� ���.):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="krimobshee" 
                     maxlength="3" size="3" tabindex="8" value=""/>
                     
<tr><td><font class="text_10">���� ����� (���� ������ �����):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="krimok" 
                     maxlength="3" size="3" tabindex="8" value=""/>
                     
<tr><td><font class="text_10">���� ����� (���� ������� ����):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="krimcp" 
                     maxlength="3" size="3" tabindex="8" value=""/>

 <tr><td><font class="text_10">���� ������ (���� ���.):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="ppKrimDog" 
                     maxlength="3" size="3" tabindex="8" value=""/>      
                     
  <tr><td><font class="text_10">���� ������ (���� ���. ������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="ppKrimDogLgot" 
                     maxlength="3" size="3" tabindex="8" value=""/>                                                                                             

  <tr><td><font class="text_10">���� ����� (�������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaDog" 
                     maxlength="3" size="3" tabindex="8" value=""/>
                     
<tr><td><font class="text_10">���� ������ (������� ��. ��.):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaIG" 
                     maxlength="3" size="3" tabindex="8" value=""/>         
                     
 <tr><td><font class="text_10">���� ������ (���. ������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="ppDogLgot" 
                     maxlength="3" size="3" tabindex="8" value=""/>                            

  <tr><td><font class="text_10">���� �� (���):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT2"
                     maxlength="3" size="3" tabindex="10" value=""/>
                     
  <tr><td><font class="text_10">���� ���. ����� (���):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT1" 
                     maxlength="3" size="3" tabindex="9" value=""/>
                     
  <tr><td><font class="text_10">���� �� (������� ''�''):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT3"
                     maxlength="3" size="3" tabindex="11" value=""/>

  <tr><td><font class="text_10">���� �� (��������� ''�''):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT4"
                     maxlength="3" size="3" tabindex="12" value=""/>

  <tr><td><font class="text_10">���� �� (����������� ''�''):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT5"
                     maxlength="3" size="3" tabindex="13" value=""/>

  <tr><td><font class="text_10">�������������:</font></td>
      <td><html:select onchange="enDis();" styleClass="select_f1" name="abit_Spec" property="sobesedovanie" tabindex="14">
           <html:option value="�">���</html:option>
           <html:option value="�">��</html:option>
           </html:select>

  <tr><td><font class="text_10">��������.&nbsp;�������:</font></td>
      <td><html:select name="abit_Spec" styleClass="select_f1" property="kodPredmeta" tabindex="15">
          <html:option value="-"/>
          <html:options collection="abit_Spec_S4" property="kodPredmeta" labelProperty="predmet"/></html:select>

  <tr><td><font class="text_10">�������:</font></td>
          <td><html:select styleClass="select_f1" name="abit_Spec" property="jekzamenZachet" tabindex="16">
           <html:option value="�">��</html:option>
           <html:option value="�">���</html:option>
           </html:select>

  <tr><td><font class="text_10">������������� ����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="poluProhodnoiBall" 
                     maxlength="2" size="2" tabindex="17" value=""/>

  <tr><td><font class="text_10">��������� ����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="prohodnoiBallNaSpetsialnosti" 
                     maxlength="2" size="2" tabindex="18" value=""/>

<tr><td colspan=2 height=9></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="19" value="��������"/></td>
  <td><html:reset  styleClass="button" tabindex="20" value="��������"/></td>
</html:form>
<html:form action="/spetsialnosti?action=adjustment">
  <td><html:submit styleClass="button" tabindex="21" value="�������� �������"/></td>
</html:form>
<html:form method="post" action="/spetsialnosti?action=null">
  <td><html:submit property="exit" styleClass="button" tabindex="22" value="�����"/></td>
</html:form>
</tr></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ ������ ���������� ������� --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">������������� ����</template:put>
<template:put name='title'>�������������</template:put>
<template:put name='content'>
<BR>
<p align=center><font class="text_10">''<bean:write name="abit_Spec" property="fakultet"/>''</font></p>
<table align=center border=1 cellSpacing=0>
<thead>
<tr><td rowspan=2 align=center height=30><font class="text_th">&nbsp;����.&nbsp;������&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;�������������&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����.&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;���.&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;���� ����&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(������)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(�������)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(����&nbsp;���.)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(����&nbsp;������&nbsp;�����)&nbsp;</font></td>  
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(����&nbsp;�������&nbsp;����)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(�������&nbsp;��.&nbsp;��.)&nbsp;</font></td> 
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(���.&nbsp;������)&nbsp;</font></td>         
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(����&nbsp;���.)&nbsp;</font></td>      
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp; &nbsp;�����&nbsp;(����&nbsp;���.&nbsp;������)&nbsp;</font></td>             
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;���.&nbsp; &nbsp;��.&nbsp;(���)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;���.&nbsp; &nbsp;��.&nbsp;(���)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;��&nbsp; &nbsp;�������&nbsp;(�)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;��&nbsp; &nbsp;���������&nbsp;(�)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;��&nbsp; &nbsp;�����������&nbsp;(�)&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;�������.&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;��������.&nbsp;�������&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;�������&nbsp;</font></td>
    <td colspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td></tr>
<tr><td align=center height=30><font class="text_th">&nbsp;�/����.&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;����.&nbsp;</font></td>
</tr>
</thead>
<logic:iterate id="abit_Spec" name="abits_Spec" scope='request'>
<tr>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="nazvanie"/></font>&nbsp;</td>
  <td valign=center>&nbsp;<html:link href="spetsialnosti.do?action=mod_del" paramName="abit_Spec" 
                   paramProperty="kodSpetsialnosti" paramId="kodSpetsialnosti" styleClass="link_hov_blue">
                    <bean:write name="abit_Spec" property="nazvanieSpetsialnosti"/></html:link>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="abbreviatura"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="tip_Spec"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="shifrSpetsialnosti"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="shifrSpetsialnostiOKSO"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriema"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaLg"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaDog"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="krimobshee"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="krimok"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="krimcp"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaIG"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="ppDogLgot"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="ppKrimDog"/></font>&nbsp;</td>  
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="ppKrimDogLgot"/></font>&nbsp;</td>    
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaT1"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaT2"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaT3"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaT4"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="planPriemaT5"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="sobesedovanie"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="predmet"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="jekzamenZachet"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="poluProhodnoiBall"/></font>&nbsp;</td>
  <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="abit_Spec" property="prohodnoiBallNaSpetsialnosti"/></font>&nbsp;</td>
</tr>
</logic:iterate>

<logic:notPresent name="abit_Spec" property="kodSpetsialnosti">
<tr>
  <td align=center valign=center colspan=17><font class="text_11">
     �&nbsp;����&nbsp;������&nbsp;��&nbsp;�������&nbsp;��&nbsp;�����&nbsp;������</font></td>
</logic:notPresent>
</table>
<table align=left border=0>
<tr align="left">
<html:form action="/spetsialnosti.do?action=new">
  <td height="40">
    <html:submit styleClass="button" value="��������� �����"/>
  </td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------ �����������/�������� ����� ������ � �� -------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">������������� ����</template:put>
<template:put name="sub_name">����������� ������</template:put>
<template:put name='title'>�������������</template:put>
<template:put name='content'>
<html:form action="/spetsialnosti?action=change" onsubmit="return checkFields();">
<html:hidden name="abit_Spec" property="special2"/>
<table cols=2 align=center border=0>

  <tr><td><font class="text_10">����������&nbsp;������:&nbsp;</font></td>
      <td><html:select name="abit_Spec" styleClass="select_f1" onchange="fillCGs();" property="kodKonGrp" tabindex="1">
           <html:options collection="abit_Spec_KonGrp" property="kodKonGrp" labelProperty="nazvanie"/>
          </html:select>

  <tr><td><font class="text_10">���������:</font></td>
      <td><html:select name="abit_Spec" onchange="fillField();" styleClass="select_f1" property="special1" tabindex="2">
           <html:options collection="abit_Spec_S1" property="special1" labelProperty="abbreviaturaFakulteta"/>
          </html:select>
          

          
  <tr><td><font class="text_10">������������� (���)</font></td>
      <td  id = "fillTd1"><html:select name="abit_Spec" onchange="fillEduLevel();" styleClass="select_f1" property="fisId" tabindex="2">
           <html:option value="-"/>
           <html:options collection="abit_Spec_S5" property="kodSpetsialnosti" labelProperty="special3"/>
          </html:select>
         

          <tr><td><font class="text_10">������� �����������:</font></td>
      <td><html:select name="abit_Spec"  onchange="fillCGs()"  styleClass="select_f1" property="formaOb" tabindex="2">
           <html:options collection="abit_Spec_S6" property="special1" labelProperty="special2"/>
          </html:select>
          
          <html:hidden name="abit_Spec" property="special10"/>
          <html:hidden name="abit_Spec" property="special11"/>
          
          
      

  <tr><td><font class="text_10">�������������:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10" property="nazvanieSpetsialnosti" 
                     size="50" maxlength="150" tabindex="3"/>
                      &nbsp;&nbsp;<html:button styleClass="button" property="button" onclick="fillSpecName();" tabindex="19" value="��������� �� �����������"/>
                     


  <tr><td><font class="text_10">������������:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10" property="abbreviatura" 
                     size="10" maxlength="10" tabindex="4"/>
                     

  <tr><td><font class="text_10">��� ����.:</font></td>
      <td><html:select styleClass="select_f1" name="abit_Spec" property="tip_Spec" tabindex="5">
           <html:option value="-"/>
           <html:option value="�">� - �����</html:option>
           <html:option value="�">� - �������</html:option>
           <html:option value="�">� - ��������</html:option>
          
           </html:select>

  <tr><td><font class="text_10">����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10" property="shifrSpetsialnosti" 
                     size="16" maxlength="16" tabindex="6"/>
                      &nbsp;&nbsp;<html:button styleClass="button" property="button" onclick="fillSpecShifr();" tabindex="19" value="��������� �� �����������"/>

  <tr><td><font class="text_10">���� ����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10" property="shifrSpetsialnostiOKSO" 
                     size="16" maxlength="16" tabindex="7"/>
                      &nbsp;&nbsp;<html:button styleClass="button" property="button" onclick="fillSpecShifrOKSO();" tabindex="19" value="��������� �� �����������"/>

  <tr><td><font class="text_10">���� ������:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriema" 
                     size="3" maxlength="3" tabindex="8"/>
                                                               
 <tr><td><font class="text_10">���� ������ (������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaLg" 
                     size="3" maxlength="3" tabindex="8"/>
 
<tr><td><font class="text_10">���� ����� (���� ���.):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="krimobshee" 
                     maxlength="3" size="3" tabindex="8"/>

<tr><td><font class="text_10">���� ����� (���� ������ �����):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="krimok" 
                     maxlength="3" size="3" tabindex="8"/>
                     
<tr><td><font class="text_10">���� ����� (���� ������� ����):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="krimcp" 
                     maxlength="3" size="3" tabindex="8"/>
          
 <tr><td><font class="text_10">���� ������ (���� ���.):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="ppKrimDog" 
                     maxlength="3" size="3" tabindex="8"/>    
                     
 <tr><td><font class="text_10">���� ������ (���� ���. ������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="ppKrimDogLgot" 
                     maxlength="3" size="3" tabindex="8"/>    
                    
 <tr><td><font class="text_10">���� ������ (�������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaDog" 
                     size="3" maxlength="3" tabindex="8"/>
                     
<tr><td><font class="text_10">���� ������ (������� ��. ��.):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaIG" 
                     maxlength="3" size="3" tabindex="8"/>        
                     
 <tr><td><font class="text_10">���� ������ (���. ������):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="ppDogLgot" 
                     maxlength="3" size="3" tabindex="8"/>                     
           
  <tr><td><font class="text_10">���� �� (���):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT2"
                     maxlength="3" size="3" tabindex="10"/>
                     
  <tr><td><font class="text_10">���� ���. ����� (���):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT1" 
                     maxlength="3" size="3" tabindex="9"/>

  <tr><td><font class="text_10">���� �� (������� ''�''):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT3"
                     maxlength="3" size="3" tabindex="11"/>

  <tr><td><font class="text_10">���� �� (��������� ''�''):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT4"
                     maxlength="3" size="3" tabindex="12"/>

  <tr><td><font class="text_10">���� �� (����������� ''�''):</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="planPriemaT5"
                     maxlength="3" size="3" tabindex="13"/>

  <tr><td><font class="text_10">�������������:</font></td>
      <td><html:select onchange="enDis();" styleClass="select_f1" name="abit_Spec" property="sobesedovanie" tabindex="14">
          <html:options collection="abit_Spec_S2" property="sobesedovanie" />
          </html:select>

  <tr><td><font class="text_10">��������.&nbsp;�������:</font></td>
      <td><html:select name="abit_Spec" styleClass="select_f1" property="kodPredmeta" tabindex="15">
          <html:options collection="abit_Spec_S4" property="kodPredmeta" labelProperty="predmet"/></html:select>

  <tr><td><font class="text_10">�������:</font></td>
      <td><html:select styleClass="select_f1" name="abit_Spec" property="jekzamenZachet" tabindex="16">
          <html:options collection="abit_Spec_S3" property="jekzamenZachet" />
          </html:select>

  <tr><td><font class="text_10">������������� ����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="poluProhodnoiBall" 
                     size="2" maxlength="2" tabindex="17"/>

  <tr><td><font class="text_10">��������� ����:</font></td>
      <td><html:text accesskey="�" name="abit_Spec" styleClass="text_f10_short" property="prohodnoiBallNaSpetsialnosti" 
                     size="2" maxlength="2" tabindex="18"/>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
<html:hidden name="abit_Spec" property="kodSpetsialnosti"/>
<html:submit styleClass="button" tabindex="19" value="��������"/>
<html:reset styleClass="button" tabindex="20" value="��������"/>
<html:submit onclick="return confirmation();" property="delete" styleClass="button" tabindex="21" value="�������"/>
</td></html:form>
<html:form action="/spetsialnosti?action=full">
<td align=center>
<html:submit styleClass="button" tabindex="22" value="��������� �����"/>
</td></html:form>
</tr></tr></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------- ����� ��������� ���������� ��������� --------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="adjustment">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">������������� ����</template:put>
<template:put name="sub_name">��������� ���������� ���������</template:put>
<template:put name='title'>�������������</template:put>
<template:put name='content'>
<BR>
<html:form action="/spetsialnosti?action=create">
<table cols=5 align=center border=0 cellSpacing=0>
<tr>
    <td align=center valign=center><font class="text_11">���������:</font></td>
    <td align=left valign=center>
       <html:select  name="abit_Spec" styleClass="select_f1" property="special1" tabindex="1">
         <html:options collection="abit_Spec_S1" property="special1"  labelProperty="abbreviaturaFakulteta"/>
       </html:select></td>
    <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
    <td align=center valign=center><font class="text_10">������� ���������� ������:</font></td>
    <td align=left valign=center>
           <html:select name="abit_Spec" styleClass="select_f1" property="priznakSortirovki" tabindex="2">
           <html:option value="�� �����������"/>
           <html:option value="�� ��������"/>
           </html:select></td>
</tr>
<tr><td><BR></td></tr>
<tr>
    <th align=center valign=center colspan=10><font class="text_10">����������� �� �������:</font></th>
</tr>
</table>
<table cols=10 align=center border=1 cellSpacing=0>
<tr><td rowspan=2 align=center height=30><font class="text_th">&nbsp;����.&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����.&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;���� ����&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;���� &nbsp;������&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;�������.&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;��������.&nbsp;�������&nbsp;</font></td>
    <td rowspan=2 align=center height=30><font class="text_th">&nbsp;�������&nbsp;</font></td>
    <td colspan=2 align=center height=30><font class="text_th">&nbsp;����&nbsp;</font></td></tr>
<tr><td align=center height=30><font class="text_th">&nbsp;�/�&nbsp;</font></td>
    <td align=center height=30><font class="text_th">&nbsp;����.&nbsp;</font></td>
</tr>
<tr>
    <td border=1 align=center valign=center>
      <html:radio accesskey="1" name="abit_Spec" property="stolbetsSortirovki" value="1"  tabindex="3" /></td>
    <td align=center valign=center>
      <html:radio accesskey="2" name="abit_Spec" property="stolbetsSortirovki" value="2"/></td>
    <td align=center valign=center>
      <html:radio accesskey="3" name="abit_Spec" property="stolbetsSortirovki" value="3"/></td>
    <td align=center valign=center>
      <html:radio accesskey="4" name="abit_Spec" property="stolbetsSortirovki" value="4"/></td>
    <td align=center valign=center>
      <html:radio accesskey="5" name="abit_Spec" property="stolbetsSortirovki" value="5"/></td>
    <td align=center valign=center>
      <html:radio accesskey="6" name="abit_Spec" property="stolbetsSortirovki" value="6"/></td>
    <td align=center valign=center>
      <html:radio accesskey="7" name="abit_Spec" property="stolbetsSortirovki" value="7"/></td>
    <td align=center valign=center>
      <html:radio accesskey="8" name="abit_Spec" property="stolbetsSortirovki" value="8"/></td>
    <td align=center valign=center>
      <html:radio accesskey="9" name="abit_Spec" property="stolbetsSortirovki" value="9"/></td>
    <td align=center valign=center>
      <html:radio accesskey="10" name="abit_Spec" property="stolbetsSortirovki" value="10"/></td>
</tr>
</table>
<table align=center border=0>
<tr align=center height=35 valign=center>
  <td><html:submit property="full" styleClass="button" tabindex="4" value="�������� �������"/></td>
</html:form>
</tr><tr><td height=90></td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>