<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<tr><td align="center" vAlign="top">
<%-------------------------------------%>
<%-- 7� ������� (������)             --%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="50%">
    <thead>
     <tr>
         <td align="center" colspan="4" height="27" vAlign="middle"><font class="text_th9">&nbsp;��������������&nbsp;��������&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
       <td align="left" width="45%">
         <table border="1" cellSpacing="0" cellPadding="0" frame="VOID" class="text" width="45%">
          <tbody class="dark">
           <tr>
               <td valign=center>&nbsp;������&nbsp;��������&nbsp;����������:&nbsp;</td>
				<td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="returnDocument" tabindex="63">
                  <html:option value="����� ����������� ��� ���������� �����">����� ����������� ��� ���������� �����</html:option>
                  <html:option value="����� �������� �������� �����">����� �������� �������� �����</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;��.&nbsp;��:&nbsp;</td>
               <td valign=center>             
              <html:select styleClass="select_f2" name="abit_A" property="inostrannyjJazyk" tabindex="64">
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
           <html:text name="abit_A" styleClass="text_f9" property="special13" size="32" maxlength="30" value="" tabindex="65"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;��&nbsp;����&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="providingSpecialCondition" size="32" maxlength="30" value="" tabindex="66"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;��&nbsp;�����&nbsp;���&nbsp;����:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="special10" size="32" maxlength="30" value="" tabindex="67"/>
         </td>
     </tr>
     </logic:notEqual>
          </tbody>
         </table>
</td>
       <td align="left" width="50%">
         <table border="1" cellSpacing="0" cellPadding="0" frame="VOID" class="text" width="50%">
          <tbody class="dark">
          <tr>
               <td valign=center>&nbsp;�����������&nbsp;�&nbsp;���������:&nbsp;&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="nujdaetsjaVObschejitii" tabindex="68">
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
                            maxlength="50" size="30" value="" tabindex="69"/>&nbsp;
               </td>
           </tr>
            <tr>
               <td valign=center>&nbsp;Email:&nbsp;&nbsp;</td>
              <td valign=center>
                  <html:text name="abit_A" styleClass="text_f9_short" property="abitEmail" 
                  		size="32" maxlength="30" tabindex="70"/>&nbsp; 
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;��������&nbsp;�����:&nbsp;</td>
               <td valign=center>
                 <html:text styleClass="text_f9_short" name="abit_A" property="dopAddress" 
                            maxlength="50" size="30" value="" tabindex="71"/>&nbsp; 
               </td>
</tr>
          </tbody>
         </table>
       </td>
     </tr>
    </tbody>
   </table>
    </td></tr>