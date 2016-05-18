<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<%--------------------------------%>
<%--------------------------------%>
   <table width="100%" border="1" cellSpacing="0" cellPadding="0" frame="BOX" class="text">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle" ><font class="text_th9">&nbsp;������&nbsp;������&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">            
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="familija" size="32" maxlength="30" value="" tabindex="1"/>
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="imja" size="24" maxlength="30" value="" tabindex="2"/>
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;��������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="otchestvo" size="28" maxlength="30" value="" tabindex="3"/>
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����������:&nbsp;&nbsp;</td>
           <td valign=center>
           <html:select styleClass="select_f2" name="abit_A" property="grajdanstvo" tabindex="4">
            <html:options collection="nationalityList" property="name" />
           </html:select>
         </td>
        
     </tr>
          <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;��������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text value="" name="abit_A" styleClass="text_f9" property="mestoRojdenija" size="52" maxlength="200" tabindex="5"/>
           </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;����&nbsp;��������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dataRojdenija" 
                      maxlength="10" size="10" value="00-00-0000" tabindex="6"/></td>
     </tr>
          <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f1" name="abit_A" property="pol" tabindex="7">
           <html:option value="-"/>
           <html:option value="�">���</html:option>
           <html:option value="�">���</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���&nbsp;���������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f1" name="abit_A" property="tipDokumenta" tabindex="8">
           <html:option value="-"/>
           <html:option value="�">�������</html:option>
           <html:option value="�">������.�������</html:option>
           <html:option value="�">������� ������������ ����������</html:option>
           <html:option value="�">�������</html:option>
           <html:option value="�">������</html:option>
           </html:select>&nbsp;&nbsp;&nbsp;
           �����:&nbsp;<html:text value="" name="abit_A" styleClass="text_f9_short" property="seriaDokumenta" size="6" maxlength="20" tabindex="9"/>&nbsp;&nbsp;
           �:&nbsp;<html:text value="" name="abit_A" styleClass="text_f9_short" property="nomerDokumenta" size="14" maxlength="40" tabindex="10"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���&nbsp;�����:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text value="" name="abit_A" styleClass="text_f9" property="kemVydDokument" size="52" maxlength="100" tabindex="11"/>&nbsp;&nbsp;
           ���&nbsp;�������������:&nbsp;<html:text value="" name="abit_A" styleClass="text_f9_short" property="zajavlen" size="8" maxlength="8"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;����&nbsp;������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dataVydDokumenta" size="10" maxlength="10" value="00-00-0000" tabindex="12"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;������&nbsp;����������&nbsp;����:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         <html:select styleClass="select_f2" name="abit_A" property="kodLgot">
            <html:option value="0">���</html:option>
            <html:option value="1">��</html:option>
        </html:select>
        </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="2" colspan="2"></td></tr>
   <tr><td colspan='2' vAlign="top">

<%-------------------------------------%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;�����&nbsp;�����������&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
    <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;������:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="stranaChange();" styleClass="select_f2" name="abit_A" property="nazv_DipBak" tabindex="13">
            <html:options collection="nationalityList" property="name" />
           </html:select>
         
        
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;�������:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="regionChange();" name="abit_A" styleClass="select_f1"  property="nazv_DipSpec" tabindex="14">
           <html:option value="-"/>
           <html:options collection="abit_A_Kladr" property="special27" labelProperty="special28"/>
          </html:select>
         
          
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;�����:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange='rajonChange();' name="abit_A" styleClass="select_f1" property="need_Spo" tabindex="15">
           <html:option value="-"/>
         
          </html:select>
         
         
         
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;�����.&nbsp;�����:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         
          <html:select name="abit_A" styleClass="select_f1" property="gorod_Prop" tabindex="16">
           <html:option value="-"/>
           </html:select>
          <html:text name="abit_A" styleClass="text_f9_short" property="gorod_Prop" size="30" maxlength="30" styleId = "foreignPunkt" value="" tabindex="17" />
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;/&nbsp;����.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="ulica_Prop" size="46" maxlength="60" value="" tabindex="18"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���&nbsp;/&nbsp;����:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dom_Prop" size="4" maxlength="6" value="0" tabindex="19"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����.&nbsp;/&nbsp;����.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="kvart_Prop" size="3" maxlength="6" value="0" tabindex="20"/>
         </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="2" colspan="2"></td></tr>
   <tr><td colspan="2" vAlign="top">
<%-------------------------------------%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;��������&nbsp;��&nbsp;�����������&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���&nbsp;���������&nbsp;�����������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="godOkonchanijaSrObrazovanija" size="4" maxlength="4" value="2016" tabindex="21"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="stranaObrChange();" styleClass="select_f2" name="abit_A" property="traineeship" tabindex="22">
            <html:options collection="nationalityList" property="name" />
           </html:select>
          
     </tr>
     
        <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;�������:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange="regionEduChange();" name="abit_A" styleClass="select_f1"  property="nazvanieOblasti" tabindex="23">
           <html:option value="-"/>
           <html:options collection="abit_A_Kladr" property="special27" labelProperty="special28"/>
          </html:select>
         
          
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;�����:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
          <html:select onchange='rajonEduChange();' name="abit_A" styleClass="select_f1" property="nazvanieRajona" tabindex="24">
           <html:option value="-"/>
           </html:select>
                
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;�����.&nbsp;�����:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
         
          <html:select name="abit_A" styleClass="select_f1" property="nazvanie" tabindex="25">
           <html:option value="-"/>
           <html:text name="abit_A" styleClass="text_f9_short" property="nazvanie" size="30" maxlength="30" styleId = "foreignObrPunkt" value="" tabindex="26" />
       
        
          </html:select>
         
         
         </td>
     </tr>
     
      <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���&nbsp;�����.&nbsp;���������:&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2"  name="abit_A" property="tipOkonchennogoZavedenija" tabindex="27">
            <html:option value="-"/>
            <html:option value="�">�����</html:option>
            <html:option value="�">�������� �����</html:option>
            <html:option value="�">���</html:option>
            <html:option value="�">��������</html:option>
             <html:option value="�">�������</html:option>
            <html:option value="�">���</html:option>
           </html:select>&nbsp;&nbsp;&nbsp;<font class="text_9">�:</font>
           <html:text name="abit_A" styleClass="text_f9_short" property="nomerShkoly" size="4" maxlength="5" value="" tabindex="28"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;��������&nbsp;��.&nbsp;���������:&nbsp;&nbsp;</td>
         <td valign=center>
           <html:select styleClass="select_f2" name="abit_A" property="kodZavedenija" tabindex="29">
            <html:options collection="abit_A_S8" property="kodZavedenija" labelProperty="sokr" />
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;���&nbsp;���������&nbsp;��&nbsp;�����.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="vidDokSredObraz" tabindex="30">
            <html:option value="-"/>
            <html:option value="�������� ���">�������� ���</html:option>
            <html:option value="�������� ��� � ��������">�������� ��� � ��������</html:option>
            <html:option value="�������� ���">�������� ���</html:option>
            <html:option value="�������� ��� � ��������">�������� ��� � ��������</html:option>
            <html:option value="������ ���������">������ ���������</html:option>
            <html:option value="������ ��������� � ��������">������ ��������� � ��������</html:option>
            <html:option value="������ �����������">������ �����������</html:option>
            <html:option value="������ ����������� � ��������">������ ����������� � ��������</html:option>
            <html:option value="������ ��������">������ ��������</html:option>
            <html:option value="������ �������� � ��������">������ �������� � ��������</html:option>
            <html:option value="������ ���">������ ���</html:option>
            <html:option value="������ ���">������ ���</html:option>
            <html:option value="������ ����">������ ����</html:option>
           </html:select>
         </td>
     </tr>
<tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�����&nbsp;���������:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">&nbsp; �:&nbsp;<html:text value="" name="abit_A" styleClass="text_f9_short" property="nomerAtt" size="14" maxlength="20" tabindex="31"/>
         </td>
     </tr>
<tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;�������&nbsp;���������&nbsp;����.&nbsp;(�����):&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="tipDokSredObraz" tabindex="32">
            <html:option value="�">��������</html:option>
            <html:option value="�">�����</html:option>
           </html:select>
         </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   