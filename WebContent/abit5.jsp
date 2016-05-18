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
<%-- 7я ТАБЛИЦА (ПРОЧЕЕ)             --%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="50%">
    <thead>
     <tr>
         <td align="center" colspan="4" height="27" vAlign="middle"><font class="text_th9">&nbsp;ДОПОЛНИТЕЛЬНЫЕ&nbsp;СВЕДЕНИЯ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
       <td align="left" width="45%">
         <table border="1" cellSpacing="0" cellPadding="0" frame="VOID" class="text" width="45%">
          <tbody class="dark">
           <tr>
               <td valign=center>&nbsp;Способ&nbsp;возврата&nbsp;документов:&nbsp;</td>
				<td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="returnDocument" tabindex="63">
                  <html:option value="лично поступающим или доверенным лицом">лично поступающим или доверенным лицом</html:option>
                  <html:option value="через оператор почтовой связи">через оператор почтовой связи</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Ин.&nbsp;яз:&nbsp;</td>
               <td valign=center>             
              <html:select styleClass="select_f2" name="abit_A" property="inostrannyjJazyk" tabindex="64">
                  <html:option value="английский">английский</html:option>
                  <html:option value="немецкий">немецкий</html:option>
                  <html:option value="французский">французский</html:option>
                 </html:select>
               </td>
           </tr>
           
           <logic:notEqual name="abit_A" property="nomerPotoka" value="6">
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Вступ&nbsp;исп&nbsp;на&nbsp;ин&nbsp;языке:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="special13" size="32" maxlength="30" value="" tabindex="65"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Об&nbsp;спец&nbsp;усл:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="providingSpecialCondition" size="32" maxlength="30" value="" tabindex="66"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Сд&nbsp;вступ&nbsp;исп&nbsp;дист:&nbsp;&nbsp;</td>
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
               <td valign=center>&nbsp;Потребность&nbsp;в&nbsp;общежитии:&nbsp;&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="nujdaetsjaVObschejitii" tabindex="68">
                  <html:option value="-">-</html:option>
                  <html:option value="н">не нуждается</html:option>
                  <html:option value="д">да, необходимо</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Контактный&nbsp;телефон:&nbsp;</td>
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
               <td valign=center>&nbsp;Почтовый&nbsp;адрес:&nbsp;</td>
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