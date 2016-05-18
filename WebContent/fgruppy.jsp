<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='fGruppyAction' scope='request'>
 <logic:redirect forward='fgruppy'/>
</logic:notPresent>

<logic:notPresent name="fGruppyForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">
var ShifrFakult;

function checker(){
if(document.forms(0).special1[0].checked){
  kursy.style.display = "block";
  nums.style.display = "block";
  krit.style.display = "block";
  razd.style.display = "block";
} else if(document.forms(0).special1[1].checked){
  kursy.style.display = "none";
  nums.style.display = "none";
  krit.style.display = "none";
  razd.style.display = "none";
}
}

function exec(){
  document.forms(0).kodFakulteta.focus(0);
}

function checkFields(){
  if(document.forms(0).kodFakulteta.value == -2 && document.forms(0).special1.value != "reset"){
   alert("Выберите факультет!");
   document.forms(0).kodFakulteta.focus();
   return false;
  }
  if(document.forms(0).nomerPotoka.value == -1){
    alert("Задайте номер потока!");
    document.forms(0).nomerPotoka.focus();
    return false;
  }
 return true;
}
</SCRIPT>


<logic:present name="fGruppyForm" property="action">
<bean:define id="action" name="fGruppyForm" property="action"/>


<%-----------------------------------------------------------------%>
<%----------     Задание параметров формирования групп     --------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="new">
<body onLoad="exec()">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Формирование групп абитуриентов</template:put>
<template:put name='title'>Формирование групп</template:put>
<template:put name='content'>
<BR>
<html:form action="/fgruppy?action=main" onsubmit="return checkFields();">
<table cols=5 align=center border=0 cellSpacing=0>
<tr><td valign=center colspan=2 align=center>
        <font class="text_11">К&nbsp;р&nbsp;и&nbsp;т&nbsp;е&nbsp;р&nbsp;и&nbsp;и&nbsp;&nbsp;о&nbsp;т&nbsp;б&nbsp;о&nbsp;р&nbsp;а&nbsp;&nbsp;а&nbsp;б&nbsp;и&nbsp;т&nbsp;у&nbsp;р&nbsp;и&nbsp;е&nbsp;н&nbsp;т&nbsp;о&nbsp;в:</font>
    </td>
</tr>
<tr>
    <td valign=center><font class="text_11">Абитуриенты факультета:</font></td>
    <td valign=center>&nbsp;
     <html:select name="abit_Spec" styleClass="select_f2" property="kodFakulteta" tabindex="1">
       <html:option value="-2">-</html:option>
       <%--html:option value="-1">*</html:option--%>
       <html:options collection="abit_Spec_S1" property="kodFakulteta"  labelProperty="abbreviaturaFakulteta"/>
     </html:select></td>
</tr><tr id="kursy">
    <td valign=center><font class="text_11">Окончившие подготовительные курсы:</font></td>
    <td valign=center>&nbsp;
     <html:text accesskey="о" name="abit_Spec" styleClass="text_f10_short" property="shifrKursov" tabindex="2" size='15'/></td>
</tr><tr>
    <td valign=center><font class="text_11">Имеющие платный договор:</font></td>
    <td valign=center>&nbsp;
     <html:select name="abit_Spec" styleClass="select_f2" property="nomerPlatnogoDogovora" tabindex="3">
       <html:option value="no">нет</html:option>
       <html:option value="yes">да</html:option>
       <html:option value="%">*</html:option>
     </html:select></td>
</tr><tr>
    <td valign=center><font class="text_11">Форма обучения:</font></td>
    <td valign=center>&nbsp;
     <html:select name="abit_Spec" styleClass="select_f2" property="special2" tabindex="4">
       <html:option value="och">очная</html:option>
       <html:option value="zao">заочная</html:option>
     </html:select></td>
</tr>
<tr id="razd">
    <td valign=center colspan=2><hr></td>
</tr>
<tr id="krit"><td valign=center colspan=2 align=center>
        <font class="text_11">К&nbsp;р&nbsp;и&nbsp;т&nbsp;е&nbsp;р&nbsp;и&nbsp;и&nbsp;&nbsp;в&nbsp;ы&nbsp;б&nbsp;о&nbsp;р&nbsp;а&nbsp;&nbsp;г&nbsp;р&nbsp;у&nbsp;п&nbsp;п:</font>
    </td>
</tr>
<tr>
    <td valign=center><font class="text_11">Использовать группы потока №:</font></td>
    <td valign=center>&nbsp;
     <html:select name="abit_Spec" styleClass="select_f2" property="nomerPotoka" tabindex="5">
       <html:option value="-1">-</html:option>
       <html:option value="-2">*</html:option>
       <html:option value="1">1</html:option>
       <html:option value="2">2</html:option>
       <html:option value="3">3</html:option>
       <html:option value="4">4</html:option>
       <html:option value="5">5</html:option>
       <html:option value="6">6</html:option>
       <html:option value="7">7</html:option>
       <html:option value="8">8</html:option>
       <html:option value="9">9</html:option>
       <html:option value="10">10</html:option>
       <html:option value="11">11</html:option>
       <html:option value="12">12</html:option>
     </html:select>
    </td>
</tr>
<tr id="nums">
    <td valign=center><font class="text_11">Количество абитуриентов в группе:</font></td>
    <td valign=center>&nbsp;&nbsp;<html:text accesskey="к" name="abit_Spec" styleClass="text_f10" property="maxCountAbiturients" value="25" size="2" maxlength="2"/></td>
</tr>
<tr>
    <td valign=center colspan=2><hr></td>
</tr>
<tr><td valign=center colspan=2 align=center>
              <font class="text_11">Т&nbsp;е&nbsp;к&nbsp;у&nbsp;щ&nbsp;а&nbsp;я&nbsp;&nbsp;о&nbsp;п&nbsp;е&nbsp;р&nbsp;а&nbsp;ц&nbsp;и&nbsp;я:</font></td></tr>
<tr><td colspan=2>&nbsp;&nbsp;<html:radio accesskey="т" name="abit_Spec" property="special1" value="auto" onclick="checker();"/>&nbsp;<font class="text_11">Автоматическое&nbsp;формирование</font></td>
</tr><tr><td colspan=2>&nbsp;&nbsp;<html:radio accesskey="т" name="abit_Spec"property="special1" value="reset" onclick="checker();"/>&nbsp;<font class="text_11">Расформирование&nbsp;групп</font></td>
</tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="6" value="Выполнить"/></td>
</html:form>&nbsp;&nbsp;
<html:form action="/fgruppy?action=main&view">
  <td><html:submit styleClass="button" tabindex="7" value="Просмотр групп"/></td>
</html:form>&nbsp;&nbsp;
<html:form action="/abit_s_ots.do?action=search">
    <td><html:submit styleClass="button" tabindex="8" value="Поиск для ФГ"/></td>
</html:form>
<html:form action="/fgruppy?action=null">
  <td><html:submit styleClass="button" tabindex="9" property="exit" value="Выход"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name='title'>Просмотр групп</template:put>
<template:put name="target_name">Группы абитуриентов по факультетам</template:put>
<template:put name='content'>
<br>
<html:form action="fgruppy.do?action=main&view">
<p align=center><font class="text_11">Выберите факультет:</font><br>
<logic:iterate id="kF" name="fakults" scope='request'>
<html:link href="fgruppy.do?action=main&view" paramId="kFak" paramName="kF" paramProperty="kodFakulteta" styleClass="link_hov_blue">
        <bean:write name="kF" property="abbreviaturaFakulteta"/>
</html:link>
</logic:iterate>
</p>
</html:form>
<font class="text_th">Д Н Е В Н О Е&nbsp;&nbsp;&nbsp;И&nbsp;&nbsp;&nbsp;В Е Ч Е Р Н Е Е</font>
<br>
<table align=center border=1 cellSpacing=0>
<tr><td valign=top>
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Бюджетные&nbsp;группы&nbsp;</font></td></tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info1" name="groups_bud" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info1" property="nomerPotoka"/></font>&nbsp;</td>                                       
   <td align=center valign=center>&nbsp;<html:link href="fgruppy.do?action=hand" paramName="group_info1" paramId="kg" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info1" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info1" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info1" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td><td width=1>
<td valign=top><table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Контрактные&nbsp;группы&nbsp;</font></td></tr>
     <td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info2" name="groups_kon" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info2" property="nomerPotoka"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="fgruppy.do?action=hand" paramName="group_info2" paramId="kg" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info2" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info2" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info2" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td></tr>
</table>
<br><br>
<font class="text_th">З А О Ч Н О Е</font>
<br>
<table align=center border=1 cellSpacing=0>
<tr><td valign=top>
<table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Бюджетные&nbsp;группы&nbsp;</font></td></tr>
<tr><td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info11" name="groups_z_bud" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info11" property="nomerPotoka"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="fgruppy.do?action=hand" paramName="group_info11" paramId="kg" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info11" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info11" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info11" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td><td width=1>
<td valign=top><table align=center border=1 cellSpacing=0 FRAME=VOID>
<thead>
<tr><td align=center colspan=3 valign=center height=30><font class="text_th">&nbsp;Контрактные&nbsp;группы&nbsp;</font></td></tr>
     <td align=center valign=center><font class="text_9_mark">&nbsp;Поток&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Название&nbsp;</font></td>
    <td align=center valign=center><font class="text_9_mark">&nbsp;Кол-во&nbsp;</font></td></tr>
<tr><td colspan=3 height=2></td></tr>
</thead>
<logic:iterate id="group_info22" name="groups_z_kon" scope='request'>
<tr>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info22" property="nomerPotoka"/></font>&nbsp;</td>
   <td align=center valign=center>&nbsp;<html:link href="fgruppy.do?action=hand" paramName="group_info22" paramId="kg" paramProperty="kodGruppy" styleClass="link_hov_blue">
                    <bean:write name="group_info22" property="gruppa"/></html:link>&nbsp;</td>
   <td align=center valign=center>&nbsp;<font class="text_10"><bean:write name="group_info22" property="amount"/></font>&nbsp;</td>
</logic:iterate>
<logic:notPresent name="group_info22" property="kodGruppy">
<tr><td align=center valign=center colspan=3>
<p align=center>
     <font class="text_9">Группы&nbsp;не&nbsp;найдены&nbsp;</font></td>
  </td>
</logic:notPresent>
<tr><td colspan=3 height=2></td></tr>
</table></td></tr>
</table>
<table align=center border=0>
<tr>
<html:form action="/fgruppy.do?action=new">
  <td align=center vAlign=middle height=35>
    <html:submit styleClass="button" value="Перейти к формированию групп"/>
  </td>
</html:form>
<html:form action="/abit_s_ots.do?action=search">
  <td align=center vAlign=middle height=35>
    <html:submit styleClass="button" value="Поиск для ФГ"/>
  </td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%-------------          Результаты обработки           -----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="results">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Результаты формирования групп</template:put>
<template:put name="sub_name">Результаты</template:put>
<template:put name='title'>Формирование групп</template:put>
<template:put name='content'>
<BR>
<p align=center><font class="text_11">
 <logic:iterate id="mess" name="fgr_msgs" scope="request" type="abit.bean.MessageBean">
  <br><bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/>
 </logic:iterate>
</font></p>
<html:form action="/fgruppy?action=new">
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="Вернуться назад"/></td>
</html:form>&nbsp;&nbsp;
<html:form action="/fgruppy?action=main&view">
  <td><html:submit styleClass="button" value="Просмотр групп"/></td>
</html:form>&nbsp;&nbsp;
<html:form action="/gruppy?action=new">
  <td><html:submit styleClass="button" value="Создание групп"/></td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%-----------             Коррекция группы              -----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="hands">
<template:insert template="<%="layouts/"+tema+"/lay/layout_small.jsp"%>">
<template:put name="target_name">Коррекция группы</template:put>
<template:put name='title'>Коррекция группы</template:put>
<template:put name='content'>
<p align=center><font class="text_11">
 <logic:iterate id="mess" name="fgr_msgs" scope="request" type="abit.bean.MessageBean">
  <bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/><br>
 </logic:iterate>
</font></p>
<html:form action="/fgruppy?action=hand">
<table align=center border=1 cellSpacing=0>
<thead>
<tr><td align=center valign=center><font class="text_th">&nbsp;№ ЛИЧН. ДЕЛА&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;ФАМИЛИЯ&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;ИМЯ&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;ОТЧЕСТВО&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;№ ДОГОВОРА&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;СРОК &nbsp;ОБУЧ.&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;КУРСЫ&nbsp;</font></td>
    <td align=center valign=center><font class="text_th">&nbsp;ГРУППА&nbsp;</font></td>
</tr>
</thead>
<logic:iterate name="abits_Spec" id="abit" type="abit.bean.AbiturientBean">
<tr>
    <td align=center valign=center><font class="text_10">&nbsp;<bean:write name="abit" property="nomerLichnogoDela"/>&nbsp;</font></td>
    <td valign=center><font class="text_10">&nbsp;<bean:write name="abit" property="familija"/></font>&nbsp;</td>
    <td valign=center><font class="text_10">&nbsp;<bean:write name="abit" property="imja"/>&nbsp;</font></td>
    <td valign=center><font class="text_10">&nbsp;<bean:write name="abit" property="otchestvo"/>&nbsp;</font></td>
    <td valign=center><font class="text_10">&nbsp;<bean:write name="abit" property="nomerPlatnogoDogovora"/>&nbsp;</font></td>
    <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit" property="srokObuchenija"/>&nbsp;</font></td>
    <td valign=center align=center><font class="text_10">&nbsp;<bean:write name="abit" property="shifrKursov"/>&nbsp;</font></td>
    <td align=center valign=center>&nbsp;
      <html:text name="abit" property="<%="abt"+abit.getKodAbiturienta()%>" maxlength='15' size='5' accesskey="г" value="<%=abit.getGruppa()%>" styleClass="text_f10_short"/>&nbsp;</td>
</tr>
</logic:iterate>
<logic:notPresent name="abit" property="familija">
<tr>
  <td align=center valign=center colspan=8>
    <font class="text_11">В&nbsp;данной&nbsp;группе&nbsp;нет&nbsp;ни&nbsp;одного&nbsp;абитуриента</font></td>
</tr>
</logic:notPresent>
</table>
<table align=center border=0>
<tr align=center>
  <td height="35"><html:submit property="renew" styleClass="button" value="Сохранить"/>&nbsp;</td>
  <td><html:reset  styleClass="button" value="Отмена"/>&nbsp;</td>
</html:form>
<html:form action="/fgruppy?action=main&view">
  <td><html:submit styleClass="button" value="Вернуться назад"/></td>
</html:form>
</tr></table>
<BR>
</template:put>
</template:insert>
</logic:equal>
</logic:present>