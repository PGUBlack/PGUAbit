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

<logic:notPresent name="abitModDelAction" scope="request">
<logic:redirect forward="abit_md"/>
</logic:notPresent>

<logic:notPresent name="abitSrchForm" property="action">
<%---------------- ������� �� ����� ��� ���������� ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
  var sp1,sp;
  var faksLength;
  var groupsLength;
  var propusk = false;
</SCRIPT>

<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">������ �����������</template:put>
<template:put name="target_name">������ �����������</template:put>

<template:put name="content">
<logic:present name="abitSrchForm" property="action">

<html:form action="/abit_md.do?action=goto">
<html:hidden name="abit_A" property="dokumentyHranjatsja"/>
<html:hidden name="abit_A" property="kodAbiturienta"/>
<table align="center" border="0" cellSpacing=0 cellPadding=0 FRAME="BOX" class="text">
<tr>
  <td valign="top">
   <table align="center" width="50%" border="1" cellSpacing=0 cellPadding=0 FRAME="VOID" class="text">
     <thead>
       <tr>
          <td width="100%" align=center colspan="2" height="20">
            <font class="text_th9">&nbsp;������&nbsp;������&nbsp;</font></td>
       </tr>
     </thead>
     <tr>
        <td><font class="text_11">&nbsp;�������:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="familija"/>&nbsp;</font>
        </td>
     </tr>
     <tr>
        <td><font class="text_11">&nbsp;���:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="imja"/>&nbsp;</font>
        </td>
     </tr>
     <tr>
        <td><font class="text_11">&nbsp;��������:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="otchestvo"/>&nbsp;</font>
        </td>
     </tr>
     <tr>
        <td><font class="text_11">&nbsp;���:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="pol"/></font>
        </td>
     </tr>
     <tr>
        <td><font class="text_11">&nbsp;�����:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="gorod_Prop"/></font>
        </td>
     </tr>
     <tr>
        <td><font class="text_11">&nbsp;�������:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="tel"/></font>
        </td>
     </tr>
     <thead>
       <tr vAlign=center>
          <td width="100%" align=center colspan="2" height="20">
            <font class="text_th9">&nbsp;�������&nbsp;������&nbsp;</font></td>
       </tr>
     </thead>
     <tr>
        <td><font class="text_11">&nbsp;�����.&nbsp;����.:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="dokumentyHranjatsja"/></font>
        </td>
     </tr>
     <thead>
       <tr vAlign=center>
          <td align=center colspan="2" height="20">
            <font class="text_th9">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��������&nbsp;������&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><br>
          </td>
       </tr>
     </thead>
     <tr>
        <td height="5" colspan=2></td>
     </tr>
       <tr vAlign=center>
          <td align=center colspan="2" height="25">
            <table width="100%" border="1" frame="VOID" cellspacing="0" cols="3">
          <thead>
            <td align="left" height="20">&nbsp;</td>
            <logic:iterate id="abit_A1" name="abit_A_S1" scope='request'>
             <td align=middle><font class="text_9_mark">&nbsp;<bean:write name="abit_A1" property="predmet"/>&nbsp;</font></td>
            </logic:iterate>
          </thead>
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
           <td align="left" valign="middle">&nbsp;&nbsp;���������&nbsp;�&nbsp;�����&nbsp;���������:&nbsp;</td>
           <logic:iterate id="abit_A1" name="abit_A_S1" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kP" name="abit_A1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Examen\" + kP%>" value="<%=abit_A1.getExamen()%>"
                       maxlength="1" size="1" />
             </td>
           </logic:iterate>
         </tr>
         <tr>
           <td align="center" valign="middle" colspan="25" height="21">&nbsp;("�" - ������� ������ � ������� ���, "�" - ������� ������ � ������� ����)&nbsp;
           </td>
         </tr>

         </tbody>
        </table>
          </td>
       </tr>
      <tr>
       <td valign="middle" align="center" colspan="2" height="40">
           <a href="<bean:write name='abit_A' property='special6'/>" target="blank" class="link_hov_blue">���������� ����� ��� � ���</a>
       </td>
      </tr>
     <thead>
       <tr vAlign=center>
          <td width="100%" align=center colspan="2" height="20">
            <font class="text_th9">&nbsp;����������&nbsp;</font></td>
       </tr>
     </thead>
     <tr>
        <td height="20"><font class="text_11">&nbsp;��������:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="prinjat"/></font>
        </td>
     </tr>
     <tr>
        <td height="20"><font class="text_11">&nbsp;�������������&nbsp;����������:&nbsp;</font></td>
        <td>
          <font class="text_11"><bean:write name="abit_A" property="special4"/></font>
          <font class="text_11"><bean:write name="abit_A" property="special5"/></font>
        </td>
     </tr>
     <tr>
        <td height="5" colspan=2></td>
     </tr>
    </table>
  </td>
<tr><td colspan="4">&nbsp;</td></tr>
<tr>
  <td colspan="4" align="middle">
   <table border="0">
     <thead>
       <tr vAlign=center>
          <td align=center colspan="2" height="20">
            <font class="text_th9">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��������������&nbsp;�������������&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><br>
          </td>
       </tr>
     </thead>
     <tr>
        <td height="2" colspan="2"></td>
     </tr>
       <tr>
          <td align="center" height="25">
            <table width="1000" border=1 cellSpacing=0 cellPadding=0>
             <thead>
                <tr valign="middle">
                   <td align="center">&nbsp;�����&nbsp; &nbsp;�������&nbsp;����&nbsp;</td> 
                   <td align="center">&nbsp;��������&nbsp;�������������&nbsp;</td>
                   <td align="center">&nbsp;���&nbsp; &nbsp;����.&nbsp;</td>
                   <td align="center">&nbsp;�����&nbsp;��&nbsp;���</td>
                   <td align="center">&nbsp;�����&nbsp;��&nbsp;����.</td>
                   <td align="center">&nbsp;�����&nbsp;</td>
                   <td align="center">&nbsp;����&nbsp;</td>
                   <td align="center">&nbsp;���&nbsp;</td>     
                   <td align="center">&nbsp;�����.&nbsp;</td>
                   <td align="center">&nbsp;����.&nbsp;���.&nbsp;������.</td>
                   <td align="center">&nbsp;�����.&nbsp;</td>
                   <td align="center">&nbsp;��.��.&nbsp;</td>
                   <td align="center">&nbsp;���������.&nbsp;</td>
                   <td align="center" width="6"></td>
                   <td align="center">&nbsp;����.&nbsp;���.&nbsp;</td>
                   <td align="center" width="6"></td>
                   <td align="center">&nbsp;���.&nbsp;��.&nbsp;���.</td>
                   <td align="center" width="6"></td>
                   <td align="center">&nbsp;���������&nbsp;</td>
                </tr>
             </thead>
              <logic:iterate id="spec" name="specials" type="abit.bean.AbiturientBean">
                <tr valign="middle">
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="nomerLichnogoDela"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="nazvanieSpetsialnosti"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="tip_Spec"/>&nbsp;</font>
                   </td>
                   <td align="left">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="special23"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="special24"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="special25"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="bud_1"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="dog_1"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="target_1"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="tname1"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="olimp_1"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="op1"/>&nbsp;</font>
                   </td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="rlgot1"/>&nbsp;</font>
                   </td>
                   <td align="center"></td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="dog_ok_1"/>&nbsp;</font>
                   </td>
                   <td align="center"></td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="npd1"/>&nbsp;</font>
                   </td>
                   <td align="center"></td>
                   <td align="center">
                      <font class="text_10">&nbsp;<bean:write name="spec" property="konkurs_1"/>&nbsp;</font>
                   </td>
                </tr>
              </logic:iterate>
            </table>
          </td>
       </tr>
   </table>
  </td>
</tr>
</tr>
 <table align=middle border=0>
  <tr>
    <td height="10">
  </tr>
  <tr>
   <td valign=middle align=middle>
     <html:submit styleClass="button" value="������� � ����" property="exit"/></td>
   <td valign=middle align=middle>
     <html:submit styleClass="button" value="���������� ������" property="srch_res"/>&nbsp;&nbsp;</td>
  </tr>
 </table>
</html:form>
</logic:present>
</template:put>
</template:insert>