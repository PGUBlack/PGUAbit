<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
    import      = "abit.Constants"
    import      = "abit.util.StringUtil"
%>

<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>
<script src="/abiturient/exec.js"></script>
 <script src="/abiturient/jquery.js"></script>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='abitModDelAction' scope='request'>
 <logic:redirect forward='abit_md'/>
</logic:notPresent>

<logic:notPresent name="abitSrchForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<bean:define id="userGroup" name = "user" property = "group"/>
<bean:define id ="userGroupName" name = "userGroup" property = "groupName"/>

<%! 
   int count;
   int tabindex;
   String kP = "0";
%>

<script>
// A $( document ).ready() block.
$( document ).ready(function() {
   $('#foreignPunkt').toggle(false);
   $('#foreignObrPunkt').toggle(false);
});
</script>



<logic:present name="abitSrchForm" property="action">
<bean:define id="action" name="abitSrchForm" property="action"/>

<%-----------------------------------------------------------------%>
<%---------------------- ����������� ������ -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">

<body onLoad=""></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">����������� �������� �����������</template:put>
<template:put name="target_name">����������� ������ �������� �����������&nbsp;&nbsp;� <bean:write name="abit_A" property="nomerLichnogoDela"/></template:put>
<template:put name="content">

<%-------------------------------------%>
<%--   ������� �������� �����������  --%>
<%-------------------------------------%>

<html:form action="/abit_md.do?action=change" onsubmit="return checkFields1();">
<html:hidden name="abit_A" property="special1"/>
<html:hidden name="abit_A" property="kodAbiturienta"/>
<html:hidden name="abit_A" property="nomerLichnogoDela"/>
<html:hidden name="abit_A" property="nomerPotoka"/>
 <logic:equal name="userGroupName" value="��������� �����">
 <html:hidden name="abit_A" property="stob"/>
  <html:hidden name="abit_A" property="stob_2"/>
   <html:hidden name="abit_A" property="stob_3"/>
    <html:hidden name="abit_A" property="stob_4"/>
     <html:hidden name="abit_A" property="stob_5"/>
      <html:hidden name="abit_A" property="stob_6"/>
      <html:hidden name="abit_A" property="pr1"/>
      <html:hidden name="abit_A" property="pr1_2"/>
      <html:hidden name="abit_A" property="pr1_3"/>
      <html:hidden name="abit_A" property="pr1_4"/>
      <html:hidden name="abit_A" property="pr1_5"/>
      <html:hidden name="abit_A" property="pr1_6"/>
      <html:hidden name="abit_A" property="pr2"/>
      <html:hidden name="abit_A" property="pr2_2"/>
      <html:hidden name="abit_A" property="pr2_3"/>
      <html:hidden name="abit_A" property="pr2_4"/>
      <html:hidden name="abit_A" property="pr2_5"/>
      <html:hidden name="abit_A" property="pr2_6"/>
      <html:hidden name="abit_A" property="pr3"/>
      <html:hidden name="abit_A" property="pr3_2"/>
      <html:hidden name="abit_A" property="pr3_3"/>
      <html:hidden name="abit_A" property="pr3_4"/>
      <html:hidden name="abit_A" property="pr3_5"/>
      <html:hidden name="abit_A" property="pr3_6"/>
       <html:hidden name="abit_A" property="olimp_1"/>
        <html:hidden name="abit_A" property="olimp_2"/>
         <html:hidden name="abit_A" property="olimp_3"/>
          <html:hidden name="abit_A" property="olimp_4"/>
           <html:hidden name="abit_A" property="olimp_5"/>
            <html:hidden name="abit_A" property="olimp_6"/>
       <html:hidden name="abit_A" property="op1"/>
        <html:hidden name="abit_A" property="op2"/>
         <html:hidden name="abit_A" property="op3"/>
         <html:hidden name="abit_A" property="op4"/>
          <html:hidden name="abit_A" property="op5"/>
           <html:hidden name="abit_A" property="op6"/>
      <html:hidden name="abit_A" property="rlgot1"/>
      <html:hidden name="abit_A" property="rlgot2"/>
      <html:hidden name="abit_A" property="rlgot3"/>
      <html:hidden name="abit_A" property="rlgot4"/>
      <html:hidden name="abit_A" property="rlgot5"/>
      <html:hidden name="abit_A" property="rlgot6"/>
       <html:hidden name="abit_A" property="special8"/>
      <html:hidden name="abit_A" property="stepen_Mag"/>
      <html:hidden name="abit_A" property="trudovajaDejatelnost"/>
      <html:hidden name="abit_A" property="special9"/>
      <html:hidden name="abit_A" property="special222"/>
      <html:hidden name="abit_A" property="shifrKursov"/>
      
 </logic:equal>

<table width="100%" border="0" cellSpacing="0" cellPadding="0">
<tr><td></td>
<logic:present name="mess" property="message">
<td width="100%" align="center"><font class="message"><bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/></font></td>
</logic:present>
<td></td></tr>
</table>
<table background="" border="0" cellSpacing="2" cellPadding="0">
<tr><td height="5"></td></tr>
<tr><td vAlign="top">
<%-------------------------------------%>
<%-- ����� ��������� �������� �����  --%>
<%-------------------------------------%>

 <jsp:include page="abit2.jsp"/>
</td><td>&nbsp;&nbsp;</td></tr>

<logic:equal name="abit_A" property="nomerPotoka" value="5">
 <logic:notEqual name="userGroupName" value="��������� �����">
<tr><td vAlign="top">
<%------------------------------------------%>
<%-- 4� ������� (������ ������� ��������) --%>
<%------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="4" height="27" vAlign="middle"><font class="text_th9">&nbsp;�������.&nbsp;����������&nbsp;&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">   
    <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;��������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
             <html:text name="abit_A" styleClass="text_f9" property="special8" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;���������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">          
           <html:text name="abit_A" styleClass="text_f9" property="stepen_Mag" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;������������&nbsp;������������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:text name="abit_A" styleClass="text_f9" property="trudovajaDejatelnost" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;�������&nbsp;�����������&nbsp;������&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:text name="abit_A" styleClass="text_f9" property="special9" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;�������&nbsp;��������&nbsp;������&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:text name="abit_A" styleClass="text_f9" property="special222" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;�������&nbsp;�������&nbsp;����������&nbsp;/�������&nbsp;�����������&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:text name="abit_A" styleClass="text_f9" property="shifrKursov" size="3" maxlength="3" />
         </td>
     </tr>
    </tbody>
   </table>
    </td></tr>
     </logic:notEqual>
     </logic:equal>



<logic:equal name="abit_A" property="nomerPotoka" value="1">
 <logic:notEqual name="userGroupName" value="��������� �����">
<tr><td vAlign="top">
<%------------------------------------------%>
<%-- 4� ������� (������ ������� ��������) --%>
<%------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="4" height="27" vAlign="middle"><font class="text_th9">&nbsp;�������.&nbsp;����������&nbsp;&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">   
    <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;��������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
             <html:text name="abit_A" styleClass="text_f9" property="special8" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;���������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">          
           <html:text name="abit_A" styleClass="text_f9" property="stepen_Mag" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;������������&nbsp;������������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:text name="abit_A" styleClass="text_f9" property="trudovajaDejatelnost" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;�������&nbsp;�����������&nbsp;������&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:text name="abit_A" styleClass="text_f9" property="special9" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;�������&nbsp;��������&nbsp;������&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:text name="abit_A" styleClass="text_f9" property="special222" size="3" maxlength="3" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��&nbsp;�������&nbsp;�������&nbsp;����������&nbsp;/�������&nbsp;�����������&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:text name="abit_A" styleClass="text_f9" property="shifrKursov" size="3" maxlength="3" />
         </td>
     </tr>
    </tbody>
   </table>
    </td></tr>
     </logic:notEqual>
   <tr><td height="2" colspan="2"></td></tr>
   <tr><td vAlign="top">
 
<%------------------------------------------%>
<%-- 5� ������� (����� ���)               --%>
<%------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;����������&nbsp;�&nbsp;������&nbsp;��&nbsp;����������&nbsp;���������&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
    <tr>
<%--------------------- ����������  ������ ------------------------%>
<%------------ ������ ��������� � ���������� ������ ---------------%>
      <td colspan="2" rowspan="10" align="center" valign="center">
        <table width="100%" border="1" frame="VOID" cellspacing="0" cols="3">
          <thead>
            <td align="left" height="20">&nbsp;</td>
            <logic:iterate id="abit_A1" name="abit_A_S1" scope='request'>
             <td align=middle><font class="text_9_mark">&nbsp;<bean:write name="abit_A1" property="predmet"/>&nbsp;</font></td>
            </logic:iterate>
          </thead>
<%
   if(count != 0) count = 0;
   if(tabindex != 40) tabindex = 40;
%>
         <tbody class="dark">
         <tr>
           <td align="left" valign="middle">&nbsp;&nbsp;�������&nbsp;���������&nbsp;�����&nbsp;���:&nbsp;</td>
           <logic:iterate id="abit_A1" name="abit_A_S1" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kP" name="abit_A1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Ege_note\" + kP%>" value="<%=abit_A1.getEge()%>"
                       maxlength="3" size="1" />
             </td>
           </logic:iterate>
         </tr>
         <tr>
           <td align="left" valign="middle">&nbsp;&nbsp;���&nbsp;�����&nbsp;���:&nbsp;</td>
           <logic:iterate id="abit_A1" name="abit_A_S1" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kP" name="abit_A1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Ege_year\" + kP%>" value="<%=abit_A1.getPodtvMed()%>"
                       maxlength="4" size="4" />
             </td>
           </logic:iterate>
         </tr>
         <tr>
           <td align="left" valign="middle">&nbsp;&nbsp;���������&nbsp;�&nbsp;�����&nbsp;���������:&nbsp;</td>
           <logic:iterate id="abit_A1" name="abit_A_S1" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kP" name="abit_A1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Examen\" + kP%>" value="<%=abit_A1.getExamen()%>"
                       maxlength="3" size="1" />
             </td>
           </logic:iterate>
         </tr>
         <tr>
           <td align="center" valign="middle" colspan="25" height="21">&nbsp;("+" - ������� ������ � ������� ����)&nbsp;
           </td>
         </tr>

         </tbody>
        </table>
      </td>
    </tr>
    </tbody>
   </table>
   </logic:equal>
   <logic:equal name="abit_A" property="nomerPotoka" value="6">
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;����������&nbsp;�&nbsp;������&nbsp;���������&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
    <tr>
<%--------------------- ����������  ������ ------------------------%>
<%------------ ������ ���������---------------%>
      <td colspan="2" rowspan="10" align="center" valign="center">
        <table width="100%" border="1" frame="VOID" cellspacing="0" cols="3">
          <thead>
            <td align="left" height="20">&nbsp;</td>
            <logic:iterate id="abit_A1" name="att" scope='request'>
             <td align=middle><font class="text_9_mark">&nbsp;<bean:write name="abit_A1" property="predmet"/>&nbsp;</font></td>
            </logic:iterate>
          </thead>
<%
   if(count != 0) count = 0;
%>
         <tbody class="dark">
         <tr>
           <td align="left" valign="middle">&nbsp;&nbsp;�������&nbsp;���������&nbsp;�����&nbsp;���������:&nbsp;</td>
           <logic:iterate id="abit_A1" name="att" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kA" name="abit_A1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Attestat\" + kA%>" value="<%=abit_A1.getEge()%>"
                       maxlength="3" size="1" />
             </td>
           </logic:iterate>
         </tr>
         </tbody>
        </table>
      </td>
    </tr>
    </tbody>
   </table>
   </logic:equal>
   </td></tr>
   <tr><td vAlign="top">


<table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="4" height="27" vAlign="middle"><font class="text_th9">&nbsp;��������������&nbsp;��������&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
       <td align="center" width="300">
         <table border="1" cellSpacing="0" cellPadding="0" frame="VOID" class="text" width="300">
          <tbody class="dark">
           <tr>
               <td valign=center>&nbsp;������&nbsp;��������&nbsp;����������:&nbsp;</td>
				<td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="returnDocument" >
                  <html:option value="����� ����������� ��� ���������� �����">����� ����������� ��� ���������� �����</html:option>
                  <html:option value="����� �������� �������� �����">����� �������� �������� �����</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;��.&nbsp;��:&nbsp;</td>
               <td valign=center>
                 <html:select styleClass="select_f2" name="abit_A" property="inostrannyjJazyk" >
                  <html:option value="����������">����������</html:option>
                  <html:option value="��������">��������</html:option>
                  <html:option value="�����������">�����������</html:option>
                 </html:select>
               </td>
           </tr>
           
            <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;���&nbsp;��&nbsp;��&nbsp;�����:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="special13" size="32" maxlength="30" />
         </td>
     </tr>   
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;��&nbsp;����&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="providingSpecialCondition" size="32" maxlength="30" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;��&nbsp;�����&nbsp;���&nbsp;����:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="special10" size="32" maxlength="30" />
         </td>
     </tr>
     </logic:notEqual>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���������&nbsp;��������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="dokumentyHranjatsja" >
                  <html:option value="�">���</html:option>
                  <html:option value="�">��</html:option>
                 </html:select>
         </td>
     </tr>
          </tbody>
         </table>
       </td>
       <td width="50%" align="center" valign="middle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
       <td width="300">
         <table border="1" cellSpacing="0" cellPadding="0" frame="VOID" class="text" width="300">
          <tbody class="dark">
          <tr>
               <td valign=center>&nbsp;�����������&nbsp;�&nbsp;���������:&nbsp;&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="nujdaetsjaVObschejitii" >
                  <html:option value="-">-</html:option>
                  <html:option value="�">�� ���������</html:option>
                  <html:option value="�">��, ����������</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;����������&nbsp;�������:&nbsp;</td>
               <td valign=center>
                 <html:text styleClass="text_f9_short" name="abit_A" property="tel" 
                            maxlength="50" size="30" />&nbsp;
               </td>
           </tr>
            <tr>
               <td valign=center>&nbsp;Email:&nbsp;&nbsp;</td>
              <td valign=center>
                  <html:text name="abit_A" styleClass="text_f9_short" property="abitEmail" 
                  		size="32" maxlength="30" />&nbsp; 
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;��������&nbsp;�����:&nbsp;</td>
               <td valign=center>
                 <html:text styleClass="text_f9_short" name="abit_A" property="dopAddress" 
                            maxlength="50" size="30" />&nbsp; 
               </td>
			</tr>
			<tr>
               <td valign=center>&nbsp;������:&nbsp;</td>
               <td valign=center>
                 <html:text styleClass="text_f9_short" name="abit_A" property="gruppa" 
                            maxlength="50" size="30" />&nbsp; 
               </td>
			</tr>
          </tbody>
         </table>
       </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="6" colspan="2"></td></tr>
   <tr><td vAlign="top">
<tr><td colspan="6" vAlign="middle" align="center">
     <table align="center" border="1" �lass="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="3" height="28" vAlign="middle"><font class="text_th9">&nbsp;�������������/�����������&nbsp;����������:&nbsp;&nbsp;</font></td>
     </tr>
    </thead>
   <tbody class="dark">
     <tr>
       <td align="center" valign="middle" height="190">
         <table border="1" align="center" cellSpacing="0" cellPadding="0" frame="BOX" class="text">
          <thead>          
           <tr>
             <td valign="middle" align="center" height="26"><font class="text_10">&nbsp;&nbsp;�&nbsp;&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;&nbsp;���������&nbsp;&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;&nbsp;������������&nbsp;��(�)&nbsp;&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;����.&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;�����.&nbsp;</font></td>
            
<!--              <td valign="middle" align="center"><font class="text_10">&nbsp;&nbsp;����.&nbsp;&nbsp;</font></td> -->
<!--              <td valign="middle" align="center"><font class="text_10">&nbsp;����.&nbsp;</font></td> -->
            
             
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center"><font class="text_10">&nbsp;���.&nbsp;���.&nbsp;</font></td>
             </logic:equal>
             <td valign="middle" align="center"><font class="text_10">&nbsp;�����������&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;����.&nbsp;���.&nbsp;</font></td>
             <logic:equal name="abit_A" property="nomerPotoka" value="1">
              <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center"><font class="text_10">&nbsp;����&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;��1&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;��2&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;��3&nbsp;</font></td>
             </logic:equal>
             </logic:equal>
             <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
             <td valign="middle" align="center"><font class="text_10">&nbsp;���.��.&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;����.���.������.&nbsp;</font></td>
             
             </logic:notEqual>
             <logic:equal name="userGroupName" value="���������">
             <logic:equal name="abit_A" property="nomerPotoka" value="2">
             <td valign="middle" align="center"><font class="text_10">&nbsp;����.&nbsp;</font></td>
             </logic:equal>
              <logic:equal name="abit_A" property="nomerPotoka" value="1">
             <td valign="middle" align="center"><font class="text_10">&nbsp;�����&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;��.��.&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;����.&nbsp;</font></td>
              </logic:equal>
               </logic:equal>
                 
             <td valign="middle" align="center"><font class="text_10">&nbsp;����.���.&nbsp;</font></td>
       
             <logic:equal name="abit_A" property="nomerPotoka" value="5">
             <td valign="middle" align="center"><font class="text_10">&nbsp;���������&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;��.��&nbsp;</font></td>
             </logic:equal>
             <td valign="middle" align="center"><font class="text_10">&nbsp;����.&nbsp;</font></td>
             
           </tr>
           
          </thead>
          <tbody class="dark">
           <tr>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;1.�����&nbsp;</td>
      <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select styleClass="select_f2" onchange="fakChange1()" name="abit_A" property="kodFakulteta" >
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select style="width:550px" styleClass="select_f2" name="abit_A" property="special2" >
          <html:options collection="abit_SD_S2" property="special2" labelProperty="nazvanieSpetsialnosti"/>
     
          
      </html:select>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_1" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_1" />&nbsp;
             </td>           
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_1" />&nbsp;
             </td>
             </logic:equal>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="npd1"
                           maxlength="10" size="10" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="sog1" />&nbsp;
             </td>
             <logic:equal name="abit_A" property="nomerPotoka" value="1">
              <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="stob" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr1" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr2"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr3" />&nbsp;
             </td>
             </logic:equal>
               </logic:equal>
               <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
           <html:select styleClass="select_f2" name="abit_A" property="target_1" >
             <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
           </html:select>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="tname1"
                           maxlength="30" size="10" />
               </td>
              
               </logic:notEqual>
                <logic:equal name="userGroupName" value="���������">
             <logic:equal name="abit_A" property="nomerPotoka" value="2">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_1"
                           maxlength="3" size="3" />
               </td>
                </logic:equal>
               <logic:equal name="abit_A" property="nomerPotoka" value="1">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_1"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:select styleClass="select_f2" name="abit_A" property="op1" >
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
          </td>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="rlgot1"
                           maxlength="15" size="10" />
               </td>
                     </logic:equal>
                           </logic:equal>
                            
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="prr1">
            <html:option value="0">���</html:option>
            <html:option value="1">��</html:option>
        </html:select>
          </td>
               
                <logic:equal name="abit_A" property="nomerPotoka" value="5">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="three_1"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="six_1"
                           maxlength="3" size="3" />
               </td>
               </logic:equal>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="fito_1"
                           maxlength="3" size="3"/>
               </td>
          
           </tr>
           <tr>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;2.�����&nbsp;</td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select styleClass="select_f2" onchange="fakChange2()" name="abit_A" property="s_okso_2" >
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select style="width:550px" styleClass="select_f2" name="abit_A" property="special3" >
          <html:options collection="abit_SD_S22" property="special2" labelProperty="nazvanieSpetsialnosti"/>
          
      </html:select>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_2" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_2" />&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
            
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_2" />&nbsp;
             </td>
             </logic:equal>
              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="npd2"
                           maxlength="10" size="10" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="sog2" />&nbsp;
             </td>
             <logic:equal name="abit_A" property="nomerPotoka" value="1">
              <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="stob_2" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr1_2" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr2_2" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr3_2" />&nbsp;
             </td>
            </logic:equal>
             </logic:equal>
             
               <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
                 <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
           <html:select styleClass="select_f2" name="abit_A" property="target_2" >
             <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
           </html:select>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="tname2"
                           maxlength="30" size="10" />
               </td>
               
              
               </logic:notEqual>
                <logic:equal name="userGroupName" value="���������">
               <logic:equal name="abit_A" property="nomerPotoka" value="2">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_2"
                           maxlength="3" size="3" />
               </td>
                </logic:equal>
               <logic:equal name="abit_A" property="nomerPotoka" value="1">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_2"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:select styleClass="select_f2" name="abit_A" property="op2" >
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
          </td>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="rlgot2"
                           maxlength="15" size="10" />
               </td>
                </logic:equal>
                 </logic:equal>
                 
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="prr2">
            <html:option value="0">���</html:option>
            <html:option value="1">��</html:option>
        </html:select>
          </td>
               
                <logic:equal name="abit_A" property="nomerPotoka" value="5">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="three_2"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="six_2"
                           maxlength="3" size="3" />
               </td>
               </logic:equal>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="fito_2"
                           maxlength="3" size="3"/>
               </td>
           </tr>
           <tr>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;3.�����&nbsp;</td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select styleClass="select_f2" onchange="fakChange3()" name="abit_A" property="s_okso_3" >
          <html:option value="-"/>
          <html:options collection="abit_SD_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select style="width:550px" styleClass="select_f2" name="abit_A" property="special4" >
         <html:options collection="abit_SD_S222" property="special2" labelProperty="nazvanieSpetsialnosti"/>
          
      </html:select>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_3" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_3" />&nbsp;
             </td>
            
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
            
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_3" />&nbsp;
             </td>
             </logic:equal>
              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="npd3"
                           maxlength="10" size="10" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="sog3" />&nbsp;
             </td>
             <logic:equal name="abit_A" property="nomerPotoka" value="1">
              <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="stob_3" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr1_3" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr2_3" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr3_3" />&nbsp;
             </td>
             </logic:equal>
             </logic:equal>
             
               <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
                <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
           <html:select styleClass="select_f2" name="abit_A" property="target_3" >
             <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
           </html:select>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="tname3"
                           maxlength="30" size="10" />
               </td>
               
              
               </logic:notEqual>
                <logic:equal name="userGroupName" value="���������">
                  <logic:equal name="abit_A" property="nomerPotoka" value="2">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_3"
                           maxlength="3" size="3"/>
               </td>
                </logic:equal>
               <logic:equal name="abit_A" property="nomerPotoka" value="1">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_3"
                           maxlength="3" size="3"/>
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:select styleClass="select_f2" name="abit_A" property="op3" >
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
          </td>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="rlgot3"
                           maxlength="15" size="10" />
               </td>
                   </logic:equal>
                       </logic:equal>
                           
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="prr3">
            <html:option value="0">���</html:option>
            <html:option value="1">��</html:option>
        </html:select>
          </td>
              
                <logic:equal name="abit_A" property="nomerPotoka" value="5">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="three_3"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="six_3"
                           maxlength="3" size="3" />
               </td>
               </logic:equal>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="fito_3"
                           maxlength="3" size="3"/>
               </td>
           </tr>
            <tr>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;4.�������&nbsp;</td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select styleClass="select_f2" onchange="fakChange4();" name="abit_A" property="s_okso_4" >
          <html:option value="-"/>
          <html:options collection="abit_SD_S11" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select style="width:550px" styleClass="select_f2" name="abit_A" property="special5" >
       <html:options collection="abit_SD_S3" property="special2" labelProperty="nazvanieSpetsialnosti"/>
         
      </html:select>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_4" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_4" />&nbsp;
             </td>
            
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
            
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_4" />&nbsp;
             </td>
             </logic:equal>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="npd4"
                           maxlength="10" size="10" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="sog4" />&nbsp;
             </td>
             <logic:equal name="abit_A" property="nomerPotoka" value="1">
                <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="stob_4" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr1_4" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr2_4" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr3_4" />&nbsp;
             </td>
           </logic:equal>
             </logic:equal>
             
               <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
                  <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
           <html:select styleClass="select_f2" name="abit_A" property="target_4" >
             <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
           </html:select>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="tname4"
                           maxlength="30" size="10" />
               </td>
               
               
               </logic:notEqual>
                   <logic:equal name="userGroupName" value="���������">
                 <logic:equal name="abit_A" property="nomerPotoka" value="2">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_4"
                           maxlength="3" size="3"/>
               </td>
                </logic:equal>
               <logic:equal name="abit_A" property="nomerPotoka" value="1">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_4"
                           maxlength="3" size="3"/>
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:select styleClass="select_f2" name="abit_A" property="op4" >
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
          </td>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="rlgot4"
                           maxlength="15" size="10" />
               </td>
                     </logic:equal>
                           </logic:equal>
                            
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="prr4">
            <html:option value="0">���</html:option>
            <html:option value="1">��</html:option>
        </html:select>
          </td>
              
                <logic:equal name="abit_A" property="nomerPotoka" value="5">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="three_4"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="six_4"
                           maxlength="3" size="3" />
               </td>
               
               </logic:equal>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="fito_4"
                           maxlength="3" size="3"/>
               </td>
           </tr>
           
            <tr>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;5.�������&nbsp;</td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select styleClass="select_f2" onchange="fakChange5()" name="abit_A" property="s_okso_5" >
          <html:option value="-"/>
          <html:options collection="abit_SD_S11" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select style="width:550px" styleClass="select_f2" name="abit_A" property="special6" >
           <html:options collection="abit_SD_S33" property="special2" labelProperty="nazvanieSpetsialnosti"/>
          
      </html:select>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_5" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_5" />&nbsp;
             </td>
            
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
            
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_5" />&nbsp;
             </td>
             </logic:equal>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="npd5"
                           maxlength="10" size="10" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="sog5" />&nbsp;
             </td>
             <logic:equal name="abit_A" property="nomerPotoka" value="1">
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="stob_5" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr1_5" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr2_5" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr3_5" />&nbsp;
             </td>
             </logic:equal>
             </logic:equal>
             
               <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
           <html:select styleClass="select_f2" name="abit_A" property="target_5" >
             <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
           </html:select>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="tname5"
                           maxlength="30" size="10" />
               </td>
               
               
               </logic:notEqual>
               
               <logic:equal name="userGroupName" value="���������">
              <logic:equal name="abit_A" property="nomerPotoka" value="2">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_5"
                           maxlength="3" size="3" />
               </td>
                </logic:equal>
               <logic:equal name="abit_A" property="nomerPotoka" value="1">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_5"
                           maxlength="3" size="3"/>
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:select styleClass="select_f2" name="abit_A" property="op5" >
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
          </td>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="rlgot5"
                           maxlength="15" size="10" />
               </td>
                  </logic:equal>
                     </logic:equal>
                     
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="prr5">
            <html:option value="0">���</html:option>
            <html:option value="1">��</html:option>
        </html:select>
          </td>
               
                <logic:equal name="abit_A" property="nomerPotoka" value="5">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="three_5"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="six_5"
                           maxlength="3" size="3" />
               </td>
               </logic:equal>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="fito_5"
                           maxlength="3" size="3"/>
               </td>
           </tr>
           
            <tr>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;6.�������&nbsp;</td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select styleClass="select_f2" onchange="fakChange6()" name="abit_A" property="s_okso_6" >
          <html:option value="-"/>
          <html:options collection="abit_SD_S11" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select> 
      <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
      <html:select style="width:550px" styleClass="select_f2" name="abit_A" property="special7" >
           <html:options collection="abit_SD_S333" property="special2" labelProperty="nazvanieSpetsialnosti"/>
          
      </html:select>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_6" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_6" />&nbsp;
             </td>
            
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
            
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_6" />&nbsp;
             </td>
             </logic:equal>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="npd6"
                           maxlength="10" size="10" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="sog6" />&nbsp;
             </td>
             <logic:equal name="abit_A" property="nomerPotoka" value="1">
             <logic:equal name="userGroupName" value="���������">
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="stob_6" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr1_6" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr2_6" />&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="pr3_6" />&nbsp;
             </td>
              </logic:equal>
             </logic:equal>
             
               <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
           <html:select styleClass="select_f2" name="abit_A" property="target_6" >
             <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
           </html:select>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="tname6"
                           maxlength="30" size="10" />
               </td>
              
               
               </logic:notEqual>
               <logic:equal name="userGroupName" value="���������">
              <logic:equal name="abit_A" property="nomerPotoka" value="2">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_6"
                           maxlength="3" size="3"/>
               </td>
                </logic:equal>
               <logic:equal name="abit_A" property="nomerPotoka" value="1">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="olimp_6"
                           maxlength="3" size="3"/>
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:select styleClass="select_f2" name="abit_A" property="op6" >
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
          </td>
           <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="rlgot6"
                           maxlength="15" size="10" />
               </td>
                  </logic:equal>
                     </logic:equal>
                 
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="prr6">
            <html:option value="0">���</html:option>
            <html:option value="1">��</html:option>
        </html:select>
          </td>
              
                <logic:equal name="abit_A" property="nomerPotoka" value="5">
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="three_6"
                           maxlength="3" size="3" />
               </td>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="six_6"
                           maxlength="3" size="3" />
               </td>
               </logic:equal>
               <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
                <html:text styleClass="text_f9_short" name="abit_A" property="fito_6"
                           maxlength="3" size="3"/>
               </td>
           </tr>
           
          </tbody>
         </table>
       </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   </table>

   <tr><td colspan="6" vAlign="bottom" height="10" align="center">
     <table align="center" border="0">
       <tr align="center">
         <td><html:submit styleClass="button" value="��������" property="mod" />&nbsp;&nbsp;</td>
         <td><html:reset  styleClass="button" value="��������" />&nbsp;&nbsp;</td>
       </html:form>
       <logic:present name="abit_A" property="may_del">
        <html:form action="/abit_md.do?action=delete"  onsubmit="return confirmation();">
        <html:hidden name="abit_A" property="kodAbiturienta"/>
          <td nowrap><html:submit styleClass="button" value="�������" property="del" />&nbsp;&nbsp;</td>
        </html:form>
       </logic:present>
       <html:form action="/abit_srch.do">
         <td><html:submit styleClass="button" value="�����" />&nbsp;&nbsp;</td>
       </html:form>
       <html:form action="/abiturient.do">
         <td><html:submit styleClass="button" value="�����"  property="exit"/></td>
       </html:form>
       </tr>
     </table>
    </td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%---------------------   �������� ���������   --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="mod_success">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">�������� ����������� ������� ��������������</template:put>
<template:put name="target_name">�������� ����������� �������������� �������</template:put>
<template:put name="content">

<html:form action="/abit_doc.do?action=reports">
<html:hidden name="abit_A" property="kodAbiturienta"/>
<html:hidden name="abit_A" property="tip_Spec"/>
<table width="100%" border="0" cellSpacing="0" cellPadding="0">
<tbody>
 <tr>
   <td align="center" valign="middle"><font class="text_10">&nbsp;���������&nbsp;��������&nbsp;��&nbsp;�����������:&nbsp;</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_th">&nbsp;<bean:write name="abit_A" property="familija"/>&nbsp;<bean:write name="abit_A" property="imja"/>&nbsp;<bean:write name="abit_A" property="otchestvo"/>&nbsp;</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">&nbsp;�������&nbsp;�������&nbsp;�&nbsp;����&nbsp;������!&nbsp;</font></td>
 </tr>
 <tr><td height="20"></td></tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">����� ������������ ����� ���������� ��� ������ ����������</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">������ ������ ''������������ ����� ����������''.</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">��� ������ � ���� ������� ������ ''�����''.</font></td>
 </tr>
 <tr><td height="20"></td></tr>
</tbody>
</table>
<table align="center" border="0">
  <tr align="center">
    <td><html:submit styleClass="button" value="������������ ����� ����������" />&nbsp;&nbsp;</td>
</html:form>
<html:form action="/abit_srch.do">
    <td><html:submit styleClass="button" value="����� ��������" />&nbsp;&nbsp;</td>
    <td><html:submit styleClass="button" value="�����" property="exit"/></td>
  </tr>
</table>
</html:form>
</template:put>
</template:insert>
</logic:equal>
</logic:present>