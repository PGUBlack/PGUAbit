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

<logic:notPresent name='genEgeAction' scope='request'>
 <logic:redirect forward='gener_ege'/>
</logic:notPresent>

<logic:notPresent name="genEgeForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<%! String kP = "0"; %>

<SCRIPT LANGUAGE="JavaScript">
var teamLength

function checkFields()
{
 if(document.forms(0).kodFakulteta.value == "-")
 {
  alert("Необходимо выбрать Факультет");
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

function help_me() {
 alert("ВНИМАНИЕ! АСУ не следит за правильностью указанных интервалов оценок ЕГЭ, поэтому,"+
       "\n прежде чем выполнять перевод, рекомендуется ВНИМАТЕЛЬНО проверить диапазоны!"+
       "\n\nЕсли не диапазон оценок по предмету содержит нули, кроме последнего столбца с ''100'' баллами,"+
       "\n то перевод оценок абитуриентов по этому предмету не производится.");
}

function fillSelect(selectCtrl) {
var i,j=0
if(selectCtrl == "-") {
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление знака "-"
   document.forms(0).special1.options[0] = new Option("-")
   document.forms(0).special1.options[0].value = "-"
   document.forms(0).special1.options[0].selected = true
return
}
j=0
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление новых строк
for(i = 0; i < teamLength; i++) {
   if((teamVAL[i]).substring(0,(teamVAL[i]).indexOf("$")) == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     j++
   }
}
// Переход в начало списка
document.forms(0).special1.options[0].selected = true
}

function help_me1(){
  alert("ВНИМАНИЕ!!!\nЕсли в поле шифров отличий указана буква ''н'', то помимо абитуриентов,"+
        "\n не имеющих отличий, будут обрабатываться и те ''медалисты'', которые не подтвердили свои медали"+
        "\n баллами ЕГЭ на выбранную специальность. Таковыми являются абитуриенты, которые в результате"+
        "\n перевода баллов по диапазонам ЕГЭ для медалистов получили оценку ниже 10 по профилирующему"+
        "\n экзамену на специальности."+
        "\n\nАбитуриенты, подтвердившие свои отличия, не учитываются при дальнейших переводах баллов,"+
        "\n однако, для того чтобы в случае необходимости перевести баллы абитуриентам, подтвердившим свои отличия,"+
        "\n нужно в поле ''Подтверд. отличия'' выбрать ''обраб.''");
}
</SCRIPT>


<logic:present name="genEgeForm" property="action">
<bean:define id="action" name="genEgeForm" property="action"/>

<%-----------------------------------------------------------------%>
<%-------------   НАСТРОЙКА ПАРАМЕТРОВ ПЕРЕВОДА ОЦЕНОК  -----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="translate">
<body onLoad="exec()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Перевод оценок ЕГЭ</template:put>
<template:put name="sub_name">Укажите критерии отбора абитуриентов</template:put>
<template:put name='title'>Выполнение перевода оценок ЕГЭ</template:put>
<template:put name='content'>
<BR>
<html:form action="/gener_ege?action=md_dl" onsubmit="return checkFields();">
<table align=center border=0 cellspacing=0>
  <tr>
    <td><font class="text_10">Факультет:</font></td>
    <td>
      <html:select onchange="fillSelect(this.value)" styleClass="select_f2" name="abit_ENS" property="kodFakulteta">
       <html:option value="-"/>
       <html:options collection="abit_ENS_S1" property="kodFakulteta" labelProperty="abbreviaturaFakulteta"/>
      </html:select>
    </td>  
  </tr>
  <tr>
    <td><font class="text_10">Специальность:</font></td>
    <td align=left>
      <html:select styleClass="select_f2" name="abit_ENS" property="special1">
       <html:options collection="abit_ENS_S2" property="special1" labelProperty="abbreviatura"/>
      </html:select>
    </td>
  </tr>
  <tr>
     <td><font class="text_10">Шифры отличий:</font></td>
     <td>
      <html:text accesskey="ш" styleClass="text_f10_short" name="abit_ENS" size='12' maxlength='50' property="shifrMedali"/>
     </td>
  </tr>
  <tr>
     <td><font class="text_10">Платный договор:</font></td>
     <td valign=center>
      <html:select name="abit_ENS" styleClass="select_f2" property="nomerPlatnogoDogovora">
        <html:option value="no">нет</html:option>
        <html:option value="yes">да</html:option>
      </html:select>
     </td>
  </tr>
  <tr>
     <td><font class="text_10">Подтверд. отличия:</font></td>
     <td valign=center>
      <html:select name="abit_ENS" styleClass="select_f2" property="special3">
        <html:option value="y">не обраб.</html:option>
        <html:option value="n">обраб.</html:option>
      </html:select>
     </td>
  </tr>
</table>
<br>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="Ввод диапазонов"/>&nbsp;&nbsp;</td>
  <td><html:button styleClass="button" property="help" onclick="help_me1();" value="Справка"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/gener_ege.do">
  <td><html:submit styleClass="button" property="exit" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%--------------- ЗАДАНИЕ ДИАПАЗОНОВ ПЕРЕВОДА ОЦЕНОК ЕГЭ ----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<body onLoad="exec2()"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">Задание диапазонов перевода оценок ЕГЭ</template:put>
<template:put name="sub_name">Для требуемых специальностей</template:put>
<template:put name='title'>Задание диапазонов для перевода оценок ЕГЭ в оценки <bean:write name="abit_ENS" property="special22"/>-балльной системы</template:put>
<template:put name='content'>
<BR>
<html:form action="/gener_ege?action=mark" onsubmit="return checkFields();">
<html:hidden name="abit_ENS" property="special1"/>
<html:hidden name="abit_ENS" property="shifrMedali"/>
<html:hidden name="abit_ENS" property="special3"/>
<html:hidden name="abit_ENS" property="nomerPlatnogoDogovora"/>
<html:hidden name="abit_ENS" property="kodFakulteta"/>
<html:hidden name="abit_ENS" property="kodSpetsialnosti"/>
<table align=center border=1 cellspacing=0>
  <tr>
    <td align=center width="90%" colspan=40>
        <font class="text_11">&nbsp;&nbsp;&nbsp;<bean:write name="abit_ENS" property="abbreviaturaFakulteta"/>&nbsp;&nbsp;&nbsp;</font><br>
        <font class="text_11">&nbsp;&nbsp;&nbsp;<bean:write name="abit_ENS" property="abbreviatura"/>&nbsp;&nbsp;&nbsp;</font>
    </td>
  </tr>
  <tr>
    <td height=10 colspan=40><hr></td>
  </tr>
  <tr>
    <td align=center rowspan="2"><font class="text_10">&nbsp;Предмет&nbsp;</font></td>
    <td align=center colspan=40><font class="text_10">
     &nbsp;Диапазонам&nbsp;оценок&nbsp;ЕГЭ&nbsp;сопоставить&nbsp;следующие&nbsp;оценки&nbsp;<bean:write name="abit_ENS" property="special22"/>-балльной&nbsp;системы:&nbsp;</font></td>
  </tr>	 
  <tr>
  <td align=center>&nbsp;</td>
  <logic:iterate id="interval" name="abit_ENS_S4" type="abit.bean.AbiturientBean" scope='request'>
    <td align=center><font class="text_th"><&nbsp;<bean:write name="interval" property="special6"/>&nbsp;></font></td>
    <td align=center>&nbsp;</td>
  </logic:iterate>
  <td align=center></td>
  </tr>	 
<%-- HEAVY LOGIC INTERFACE BUILDER --%>
  <logic:iterate id="abit_ENS3" name="abit_ENS_S3" type="abit.bean.AbiturientBean" scope='request'>
  <bean:define id="kP" name="abit_ENS3" property="kodPredmeta"/>
   <tr>
     <td nowrap align=center><font class="text_11">&nbsp;<bean:write name="abit_ENS3" property="predmet"/>&nbsp;</font></td>
<%---------------------    Интервалы         -------------------------%>
    <td align=center nowrap>
      <html:text styleClass="text_f9_short" property="<%=\"abc0\"+abit_ENS3.getKodPredmeta()%>" value="0" maxlength='3' size='1'/>
    </td>
  <logic:iterate id="interval" collection="<%=abit_ENS3.getList()%>" type="abit.bean.AbiturientBean">
    <td align=center><font class="text_10">&nbsp;(...]&nbsp;</font></td>
    <td align=center nowrap>
      <html:text styleClass="text_f9_short" property="<%=\"int\"+interval.getSpecial7()+\"%\"+abit_ENS3.getKodPredmeta()%>"
                 value="<%=interval.getSpecial8()%>"
                     maxlength='3' size='1' />
    </td>
  </logic:iterate>
   </tr>
   </logic:iterate>
</table>
<br>
<table align=center border=0 cols=2>
  <tr>
    <td colspan=6 align=center>
    <font class="text_11">&nbsp;&nbsp;Перевод&nbsp;баллов&nbsp;будет&nbsp;выполнен&nbsp;для:&nbsp;
    <bean:write name="abit_ENS" property="number"/>&nbsp;абитуриента(ов)&nbsp;&nbsp;</font>
    </td>
  </tr>
</table>
<table align=center border=0 cols=2>
<tr><td align=center>
    <html:submit styleClass="button_sd" value="Выполнить перевод"/></td>
    <td align=center>
    <html:submit styleClass="button_sd" property="view" value="Просмотр без перевода"/></td>
    <td align=center>
    <html:button styleClass="button_sd" property="help" onclick="help_me();" value="Справка"/></td>
</html:form>
<html:form action="/gener_ege?action=translator">
  <td><html:submit styleClass="button_sd" value="К настройке"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%---------------     РЕЗУЛЬТАТЫ ПЕРЕВОДА ОЦЕНОК    ---------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="results">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="target_name">Результаты перевода оценок ЕГЭ</template:put>
<template:put name='title'>Результаты перевода оценок ЕГЭ в оценки <bean:write name="abit_ENS" property="special22"/>-балльной системы</template:put>
<template:put name='content'>
<br>
<table align=center border=0 cellspacing=0>
<logic:iterate id="mess" name="fgr_msgs" scope="request" type="abit.bean.MessageBean">
  <tr>
    <td colspan=6 align=center>
    <font class="text_th"><bean:write name="mess" property="message"/></font>
    </td>
  </tr>
</logic:iterate>
</table>
</br>
<p align=center>
<font class="text_10"><bean:write name="abit_ENS" property="fakultet"/></font><br>
<font class="text_10"><bean:write name="abit_ENS" property="special2"/></font>
<br>
<html:form action="/gener_ege?action=hand_mark">
<html:hidden name="abit_ENS" property="special1"/>
<html:hidden name="abit_ENS" property="special3"/>
<html:hidden name="abit_ENS" property="shifrMedali"/>
<html:hidden name="abit_ENS" property="nomerPlatnogoDogovora"/>
<html:hidden name="abit_ENS" property="kodFakulteta"/>
<html:hidden name="abit_ENS" property="kodSpetsialnosti"/>
<table align=center border=1 cellSpacing=0 cellPadding=0>
<thead>
  <tr>
     <td rowspan=3 valign=center align=center>&nbsp;№&nbsp;</td>
     <td rowspan=3 valign=center align=center>&nbsp;Фамилия&nbsp;И.О.&nbsp;</td>
     <td rowspan=3 valign=center align=center>&nbsp;Номер&nbsp;дела&nbsp;</td>
     <td rowspan=3 valign=center align=center>&nbsp;Номер&nbsp;договора&nbsp;</td>
     <td rowspan=3 valign=center align=center>&nbsp;Шифр отличий&nbsp;</td>
     <td colspan=30 valign=center align=center>&nbsp;Предметы&nbsp;/&nbsp;Оценки&nbsp;</td>
  </tr>
  <tr>
     <logic:iterate id="pr" name="predms" scope='request'>
      <td colspan=2 align=center>&nbsp;<bean:write name="pr" property="sokr"/>&nbsp;</td>
     </logic:iterate>
  </tr>
  <tr>
     <logic:iterate id="pr" name="predms" scope='request'>
      <td>&nbsp;ЕГЭ&nbsp;</td>
      <td>&nbsp;Экз&nbsp;оц.&nbsp;</td>
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
                     maxlength='2' size='1' /></td>
      <td align=center><html:text styleClass="text_f9_short" property="<%=\"ots\"+abit.getKodAbiturienta()+\"%\"+abit.getKodPredmeta()%>"
                 value="<%=abit.getExzamOtsenka()%>"
                     maxlength='2' size='1' /></td>
<% } else { %>
  <tr>
     <td align=center><font class="text_10"><%=++number%></font></td>
     <td>&nbsp;<font class="text_10"><bean:write name="abit" property="familija"/></font></td>
     <td align=center><font class="text_10"><bean:write name="abit" property="nomerLichnogoDela"/></font></td>
     <td align=center><font class="text_10">&nbsp;<bean:write name="abit" property="nomerPlatnogoDogovora"/></font></td>
     <td align=center><font class="text_10"><bean:write name="abit" property="shifrMedali"/></font></td>

      <td align=center><html:text styleClass="text_f9_short" property="<%=\"ege\"+abit.getKodAbiturienta()+\"%\"+abit.getKodPredmeta()%>"
                 value="<%=""+abit.getOtsenkaegeabiturienta()%>"
                     maxlength='2' size='1' /></td>
      <td align=center><html:text styleClass="text_f9_short" property="<%=\"ots\"+abit.getKodAbiturienta()+\"%\"+abit.getKodPredmeta()%>"
                 value="<%=abit.getExzamOtsenka()%>"
                     maxlength='2' size='1' /></td>

     <%--td align=center><font class="text_10"><bean:write name="abit" property="otsenkaegeabiturienta"/></font></td>
     <td align=center><font class="text_10"><bean:write name="abit" property="exzamOtsenka"/></font></td--%>

<% old_kod = StringUtil.toInt(abit.getKodAbiturienta()+"",-10); %>
<% } %>
</logic:iterate>
<logic:notPresent name="abit" property="familija">
<tr><td align=center valign=center colspan=30>
<p align=center>
     <font class="text_9">&nbsp;На&nbsp;данной&nbsp;специальности&nbsp;нет&nbsp;абитуриентов,&nbsp;удовлетворяющих&nbsp;критериям&nbsp;отбора</font></td>
  </td>
</logic:notPresent>
</table>
<br>
<table align=center border=0>
<tr>
   <td>
      <html:submit styleClass="button" property="back" value="Сохранить изменения"/>&nbsp;&nbsp;
   </td>
   <td>
</html:form>
<html:form action="/gener_ege?action=translator">
      <html:submit styleClass="button" value="К настройке"/>&nbsp;&nbsp;
   </td>
</html:form>
<html:form action="/gener_ege.do">
   <td>
      <html:submit styleClass="button" property="exit" value="Выход"/>
   </td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>