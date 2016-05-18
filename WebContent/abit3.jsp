<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<tr><td vAlign="top">
<%------------------------------------------%>
<%-- 4€ “јЅЋ»÷ј (Ћ№√ќ“џ ќ“Ћ»„»я ÷≈Ћ≈¬» ») --%>
<%------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="4" height="27" vAlign="middle"><font class="text_th9">&nbsp;»Ќƒ»¬»ƒ.&nbsp;ƒќ—“»∆≈Ќ»я&nbsp;&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">   
    <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Ѕаллы&nbsp;за&nbsp;аттестат:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f1" name="abit_A" property="special8">
           <html:option value="0">нет</html:option>
           <html:option value="да">да</html:option>
           
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Ѕаллы&nbsp;за&nbsp;сочинение:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f1" name="abit_A" property="stepen_Mag">
           <html:option value="0">нет</html:option>
            <html:option value="да">да</html:option>

           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;¬олонтерска€&nbsp;де€тельность:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:select styleClass="select_f1" name="abit_A" property="trudovajaDejatelnost" >
         <html:option value="0">нет</html:option>
           <html:option value="да">да</html:option>

           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Ѕаллы&nbsp;за&nbsp;наличие&nbsp;серебр€ного&nbsp;значка&nbsp;√“ќ:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select styleClass="select_f1" name="abit_A" property="special9">
          <html:option value="0">нет</html:option>
           <html:option value="да">да</html:option>

           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Ѕаллы&nbsp;за&nbsp;наличие&nbsp;золотого&nbsp;значка&nbsp;√“ќ:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:select styleClass="select_f1" name="abit_A" property="special222">
         <html:option value="0">нет</html:option>
           <html:option value="да">да</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Ѕаллы&nbsp;за&nbsp;наличие&nbsp;статуса&nbsp;победител€&nbsp;/призера&nbsp;ќлимпийских&nbsp;игр:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select styleClass="select_f1" name="abit_A" property="shifrKursov" >
          <html:option value="0">нет</html:option>
           <html:option value="да">да</html:option>
           </html:select>
         </td>
     </tr>
    </tbody>
   </table>
    </td></tr>
   <tr><td height="2" colspan="2"></td></tr>
   <tr><td vAlign="top">
 