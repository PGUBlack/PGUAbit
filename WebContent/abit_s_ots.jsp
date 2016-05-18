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

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>

<logic:notPresent name='abitSrchOtsAction' scope='request'>
 <logic:redirect forward='abit_s_ots'/>
</logic:notPresent>

<logic:notPresent name="abitSrchForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>
<SCRIPT LANGUAGE="JavaScript">

var valid1 = " ёйцукенгшщзхъфывапролджэячсмитьбю1234567890*.,()"
var valid2 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ*.,()"
var valid4 = "1234567890*."
var valid5 = "0123456789,*."
var valid6 = "шдвпт*."
var valid7 = "ндв*."
var valid8 = "анфп*."
var valid9 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ1234567890*.,()"
var i;
var tmp;
var temp;
var teamLength;

var str,old,news

function add_val(p_num) {
var index = eval("document.forms(0).special"+p_num+".selectedIndex");
 if(index != -1) {
    document.forms(0).special1.options[document.forms(0).special1.length] = new Option(eval("document.forms(0).special"+p_num+".options[index].text"));
    document.forms(0).special1.options[document.forms(0).special1.length-1].value = eval("document.forms(0).special"+p_num+".value");
  } else {
    alert("Внимание! Необходимо щелкнуть левой кнопкой мыши по названию группы");
    return;
  }
}

function checkFields_fgr() {
 if(document.forms(0).special1.length==0) {
   alert("Список выбранных для формирования групп не может быть пустым!");
   document.forms(0).special1.focus();
   return false;
 }
//Компановка строки списка номеров
  document.forms(0).special7.value="";
    for(var i=0;i<document.forms(0).special1.length;i++){
       str = document.forms(0).special1.options[i].value;
       document.forms(0).special7.value += str + ",";
    }
}

function del_val() {
var index = document.forms(0).special1.selectedIndex;
if(index==-1) return;
  document.forms(0).special1.options[index] = null;
}

function CheckAllPr(){

  tmp = document.forms(0).special10.value;

  for(i = 0; i<tmp.length-1;i++){
    temp = tmp.substring(i+1,tmp.indexOf("%",i+1));
    i = tmp.indexOf("%",i+1)-1;
    if(document.forms(0).all_pr.checked == true) {   
      eval("document.forms(0).prdm"+temp+".checked = true");
    }
    else {
      eval("document.forms(0).prdm"+temp+".checked = false");
    }
  }
}
function checkFields(){
//-------------------------------------------------------------------------------------------------
for(var i=0; i<document.forms(0).nazvanieRajona.value.length; i++) {
   temp = "" + document.forms(0).nazvanieRajona.value.substring(i, i+1)
   if(valid9.indexOf(temp) == "-1") {
      alert("Поле ''Район'' может состоять только из букв русского алфавита или цифр")
      document.forms(0).nazvanieRajona.focus()
      return false
   }
}
if(document.forms(0).gdePoluchilSrObrazovanie.value == "Г") {
  if(document.forms(0).nazvanie.value == "каменка"  || document.forms(0).nazvanie.value == "Каменка" ||
     document.forms(0).nazvanie.value == "кузнецк"  || document.forms(0).nazvanie.value == "Кузнецк" ||
     document.forms(0).nazvanie.value == "заречный" || document.forms(0).nazvanie.value == "Заречный" ||
     document.forms(0).nazvanie.value == "сердобск" || document.forms(0).nazvanie.value == "Сердобск")
     return true 
  else {
   alert("Поле ''Населенный пункт'' может содержать значения: (''кузнецк'',''заречный'',''сердобск'',''каменка'')")
    document.forms(0).nazvanieRajona.focus()
    return false
  }
}
}

function autoInit(){
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "-") {
    document.forms(0).nazvanieOblasti.value = "*"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "п") {
    document.forms(0).nazvanieOblasti.value = "пензенская"
    document.forms(0).nazvanie.value = "Пенза"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "г") {
    document.forms(0).nazvanieOblasti.value = "пензенская"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "с") {
    document.forms(0).nazvanieOblasti.value = "пензенская"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "о") {
    document.forms(0).nazvanieOblasti.value = "*"
    document.forms(0).nazvanie.value = "*"
    document.forms(0).nazvanieRajona.value = "*"
  }
}

function autoInit1(){
 var temp = "";
 var index = document.forms(0).special1.selectedIndex;
 var j=0;
    if(document.forms(0).special1.options[index].value == "*")
      document.forms(0).nomerLichnogoDela.value = "*";
    else 
      document.forms(0).nomerLichnogoDela.value = document.forms(0).special1.options[index].text.toLowerCase()+"*";
}

function autoInit2(){
    if(document.forms(0).nazvanie.value == "Кузнецк" || document.forms(0).nazvanie.value == "кузнецк"){
      document.forms(0).nazvanieRajona.value = "кузнецкий"
      document.forms(0).tipOkonchennogoZavedenija.focus()
    } 
    if(document.forms(0).nazvanie.value == "Каменка" || document.forms(0).nazvanie.value == "каменка"){
      document.forms(0).nazvanieRajona.value = "каменский"
      document.forms(0).tipOkonchennogoZavedenija.focus()
    }
    if(document.forms(0).nazvanie.value == "Сердобск" || document.forms(0).nazvanie.value == "сердобск"){
      document.forms(0).nazvanieRajona.value = "сердобский"
      document.forms(0).tipOkonchennogoZavedenija.focus()
    }
    if(document.forms(0).nazvanie.value == "Заречный" || document.forms(0).nazvanie.value == "заречный"){
      document.forms(0).nazvanieRajona.value = ""
      document.forms(0).tipOkonchennogoZavedenija.focus()
    }
}

function exec() {

if(document.forms(0).special1 != null &&
   document.forms(0).shifrFakulteta != null &&
   document.forms(0).pol != null &&
   document.forms(0).gdePoluchilSrObrazovanie != null &&
   document.forms(0).polnoeNaimenovanieZavedenija != null &&
   document.forms(0).shifrMedali != null &&
   document.forms(0).shifrLgot != null &&
   document.forms(0).shifrKursov != null) {

    CheckAllPr();
    teamLength = document.forms(0).special1.options.length
    teamTXT = new Array(teamLength)
    teamVAL = new Array(teamLength)
    for(var ind=0;ind<document.forms(0).special1.length;ind++) {
       eval("teamTXT["+ind+"]=document.forms(0).special1.options["+ind+"].text")
       eval("teamVAL["+ind+"]=document.forms(0).special1.options["+ind+"].value")
    }
    document.forms(0).shifrFakulteta.selectedIndex=0
    document.forms(0).special1.selectedIndex=0
    document.forms(0).pol.selectedIndex=0
    document.forms(0).gdePoluchilSrObrazovanie.selectedIndex=0
    document.forms(0).polnoeNaimenovanieZavedenija.selectedIndex=0
    fillSelect(document.forms(0).shifrFakulteta.value);
    document.forms(0).shifrFakulteta.focus()
    }
}

function fillSelect(selectCtrl) {
var i,j=0
if(selectCtrl == "*") {
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление знака "*"
   document.forms(0).special1.options[0] = new Option("*")
   document.forms(0).special1.options[0].value = "*"
   document.forms(0).special1.options[0].selected = true
return
}
j=0
// Удаление существующих строк
for (i = document.forms(0).special1.length; i >= 0; i--) {
document.forms(0).special1.options[i] = null 
}
// Добавление новых строк
   document.forms(0).special1.options[0] = new Option("*")
   document.forms(0).special1.options[0].value = "*"
j++
for(i = 0; i < teamLength; i++) {
   if(teamTXT[i].charAt(0) == selectCtrl) {
     document.forms(0).special1.options[j] = new Option(teamTXT[i])
     document.forms(0).special1.options[j].value = teamVAL[i]
     j++
   }
}
// Переход в начало списка
document.forms(0).special1.options[0].selected = true
}

function help_me() {
  alert("При заполнении полей можно также использовать специальные символы:\n\n\" * \" - произвольное количество символов\n\" . \" - одиночное вхождение символа");
  return true;
}

</SCRIPT>

<body onLoad="exec()"></body>

<logic:present name="abitSrchForm" property="action">
<bean:define id="action" name="abitSrchForm" property="action"/>


<%-----------------------------------------------------------------%>
<%------------------------ Поиск записей --------------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="search">
<bean:define id="values" name="abit_A" type="abit.bean.AbiturientBean"/>
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<logic:notEqual name='index' scope='request' value='adminIndex'>
<template:put name="title">Поиск Абитуриентов</template:put>
<template:put name="target_name">Поиск абитуриентов по оценкам</template:put>
</logic:notEqual>
<logic:equal name='index' scope='request' value='adminIndex'>
<template:put name="title">Поиск абитуриентов для формирования групп</template:put>
<template:put name="target_name">Поиск абитуриентов для формирования групп</template:put>
</logic:equal>
<template:put name="content">
<table cols=7 align=center border=1 cellSpacing=0>
<html:form action="/abit_s_ots?action=searching&offset" onsubmit="return checkFields();">
<html:hidden name="abit_A" property="special10"/>
<tr><td width=3%><td width=24%><td width=22%><td width=2%><td width=3%><td width=24%><td width=22%>
<%-------------------- Строка таблицы -------------------------%>
<tr>
<td></td>
<td valign=center><font class="text_9">&nbsp;Факультет:</font></td>
<td valign=center>
    <html:select onchange="fillSelect(this.value),autoInit1();" styleClass="select_f3" name="abit_A" property="shifrFakulteta" tabindex="1">
    <html:option value="*"/>
    <html:options collection="abit_A_S2" property="shifrFakulteta" labelProperty="abbreviaturaFakulteta" />
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="KodFormyOb"/>
<td valign=center><font class="text_9">&nbsp;Форма обучения:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="kodFormyOb" tabindex="22">
    <html:option value="*"/>
    <html:options collection="abit_forms" property="kodFormyOb" labelProperty="sokr"/>
    </html:select>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NazvanieSpetsialnosti"/>
<td valign=center><font class="text_9">&nbsp;Спец-ть&nbsp;поступления:</font></td>
<td valign=center>
    <html:select onchange="autoInit1();" styleClass="select_f3" name="abit_A" property="special1" tabindex="2">
    <html:options collection="abit_A_S3" property="special1" labelProperty="abbreviatura" />
    </html:select>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="KodOsnovyOb"/>
<td valign=center><font class="text_9">&nbsp;Основа обучения:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="kodOsnovyOb" tabindex="23">
    <html:option value="*"/>
    <html:options collection="abit_osnovs" property="kodOsnovyOb" labelProperty="sokr"/>
    </html:select>
</td>
</tr>


<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="SrokObuchenija"/>
<td valign=center><font class="text_9">&nbsp;Уровень образования:</font></td>
<td valign=center>

	    <html:select styleClass="select_f2" name="abit_A" property="srokObuchenija" value = "*">
	      <html:option value="*">*</html:option>
            <html:option value="1">Специалитет/Бакалавриат</html:option>
            <html:option value="2">Магистратура</html:option>
            <html:option value="3">Интернатура</html:option>
             <html:option value="4">Ординатура</html:option>
              <html:option value="5">Аспирантура</html:option>
               <html:option value="6">СПО</html:option>
           </html:select>

 
</td>

<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="ShifrLgot"/>
<td valign=center><font class="text_9">&nbsp;Особые права:</font></td>
<td valign=center>
      <html:select styleClass="select_f2" name="abit_A" property="kodLgot" >
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
    
    </td>
    </tr>



<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Grajdanstvo"/>
<td valign=center><font class="text_9">&nbsp;Аттестат:</font></td>
<td>
     <html:select styleClass="select_f2" name="abit_A" property="vidDokSredObraz">
            <html:option value="-">-</html:option>
            <html:option value="Аттестат ООО">аттестат ООО</html:option>
            <html:option value="Аттестат ООО с отличием">аттестат ООО с отличием</html:option>
            <html:option value="Аттестат СОО">аттестат СОО</html:option>
            <html:option value="Аттестат СОО с отличием">аттестат СОО с отличием</html:option>
            <html:option value="Диплом ВПО">диплом ВО/ВПО</html:option>
            <html:option value="Диплом СПО">диплом СПО</html:option>
            <html:option value="Диплом НПО">диплом НПО</html:option>
            <html:option value="Диплом НВПО">диплом НВПО</html:option>
            <html:option value="Диплом ВО/СПО">Диплом ВО/СПО</html:option>
           </html:select>
           </td>
           <td width=1%></td>
           
           <td><html:radio name="abit_A" property="priznakSortirovki" value="TipDokSredObraz"/>
<td valign=center><font class="text_9">&nbsp;Копия аттестата:</font></td>
<td valign=center>
    <html:text accesskey="к" styleClass="text_f9" name="abit_A" property="tipDokSredObraz" 
               maxlength="1" size="1" tabindex="3" value="*"/>
</td>

           </tr>


<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Prinjat"/>
<td valign=center><font class="text_9">&nbsp;Зачислен:</font></td>
<td valign=center>
    <html:text accesskey="п" styleClass="text_f9" name="abit_A" property="prinjat" value="<%=values.getSpecial13()%>" size='25' maxlength='50' tabindex="3"/>
</td>
<td width=1%></td>
<td rowspan=2><html:radio name="abit_A" property="priznakSortirovki" value="NomerPlatnogoDogovora"/>
<td valign=center><font class="text_9">&nbsp;Признак договора:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="ege" tabindex="24">
    <html:option value="*" />
    <html:option value="д" />
    <html:option value="н" />
    </html:select>
</td>
</tr>
<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Prinjat"/>
<td valign=center><font class="text_9">&nbsp;Спец-ть&nbsp;зачисления:</font></td>
<td valign=center>
    <html:text accesskey="с" styleClass="text_f9" name="abit_A" property="special8" 
               maxlength="100" size="10" tabindex="4" value="*"/>
</td>
<td width=1%></td>
<td valign=center><font class="text_9">&nbsp;Номер&nbsp;платного&nbsp;договора:</font></td>
<td valign=center>
    <html:text accesskey="н" styleClass="text_f9" name="abit_A" property="nomerPlatnogoDogovora" 
               maxlength="50" size="25" tabindex="25" value="*"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Gruppa"/>
<td valign=center><font class="text_9">&nbsp;Группа:</font></td>
<td valign=center>
    <html:text accesskey="г" styleClass="text_f9" name="abit_A" property="gruppa" 
               maxlength="8" size="6" tabindex="5" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Shifrmedali"/>
<td valign=center><font class="text_9">&nbsp;Индивидуальные достижения:</font></td>
<td valign=center>
    <html:text accesskey="о" styleClass="text_f9" name="abit_A" property="shifrMedali" value="<%=values.getMedal()%>" size='25' maxlength='50' tabindex="26"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NomerLichnogoDela"/>
<td valign=center><font class="text_9">&nbsp;Номер личного дела:</font></td>
<td valign=center>
    <html:text accesskey="н" styleClass="text_f9" name="abit_A" property="nomerLichnogoDela" 
               maxlength="10" size="10" tabindex="6" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="ShifrLgot"/>
<td valign=center><font class="text_9">&nbsp;Особые права:</font></td>
<td valign=center>
    <html:text accesskey="с" styleClass="text_f9" name="abit_A" property="shifrLgot" value="<%=values.getLgoty()%>" size='25' maxlength='50' tabindex="27"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Familija"/>
<td valign=center><font class="text_9">&nbsp;Фамилия:</font></td>
<td valign=center>
    <html:text accesskey="ф" styleClass="text_f9" name="abit_A" property="familija" 
               maxlength="50" size="25" tabindex="7" value="*"/>
</td>
<td width=1%></td>
<td><html:radio name="abit_A" property="priznakSortirovki" value="ShifrKursov"/>
<td valign=center><font class="text_9">&nbsp;Паспорт:</font></td>
<td valign=center>
    <html:text accesskey="п" styleClass="text_f9" name="abit_A" property="shifrKursov" value="п,з,и" size='25' maxlength='50' tabindex="28"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Imja"/>
<td valign=center><font class="text_9">&nbsp;Имя:</font></td>
<td valign=center>
    <html:text accesskey="и" styleClass="text_f9" name="abit_A" property="imja" 
               maxlength="50" size="25" tabindex="8" value="*"/>
</td>
<td width=1%></td>
<td>&nbsp;</td>
<td valign=center><font class="text_9">&nbsp;Где сдаёт экзамен:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="examen" tabindex="29">
    <html:option value="*" />
    <html:option value="е" />
    <html:option value="в" />
    </html:select>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Otchestvo"/>
<td valign=center><font class="text_9">&nbsp;Отчество:</font></td>
<td valign=center>
    <html:text accesskey="о" styleClass="text_f9" name="abit_A" property="otchestvo" 
               maxlength="50" size="25" tabindex="9" value="*"/>
</td>
<td width=1%></td>
<td><%--html:radio name="abit_A" property="priznakSortirovki" value="OtsenkaAtt"/--%>&nbsp;</td>
<td valign=center><font class="text_9">&nbsp;Оценка&nbsp;аттестата:&nbsp;</font></td>
<td valign=center><font class="text_9">от</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Att_ot" 
               maxlength="3" size='1' tabindex="30" value="0"/><font class="text_9"> до</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Att_do" 
               maxlength="3" size='1' tabindex="31" value="10"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Pol"/>
<td valign=center><font class="text_9">&nbsp;Пол:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="pol" tabindex="10">
    <html:option value="*" />
    <html:option value="м" />
    <html:option value="ж" />
    </html:select>
</td>
<td width=1%></td>
<td><%--html:radio name="abit_A" property="priznakSortirovki" value="OtsenkaZajavl"/--%>&nbsp;</td>
<td valign=center><font class="text_9">&nbsp;Оценка&nbsp;заявленная:&nbsp;</font></td>
<td valign=center><font class="text_9">от</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Zaj_ot" 
               maxlength="3" size='1' tabindex="32" value="0"/><font class="text_9"> до</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Zaj_do" 
               maxlength="3" size='1' tabindex="33" value="10"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="TipOkonchennogoZavedenija"/>
<td valign=center><font class="text_9">&nbsp;Тип оконченного заведен.:</font></td>
<td valign=center>
    <html:text accesskey="т" styleClass="text_f9" name="abit_A" property="tipOkonchennogoZavedenija" 
               maxlength="1" size="1" tabindex="11" value="*"/>
</td>
<td width=1%></td>
<td><%--html:radio name="abit_A" property="priznakSortirovki" value="Otsenka"/--%>&nbsp;</td>
<td valign=center><font class="text_9">&nbsp;Оценка&nbsp;экзамена:&nbsp;</font></td>
<td valign=center><font class="text_9">от</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Exam_ot" 
               maxlength="3" size='1' tabindex="34" value="0"/><font class="text_9"> до</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Exam_do" 
               maxlength="3" size='1' tabindex="35" value="10"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NomerShkoly"/>
<td valign=center><font class="text_9">&nbsp;Номер школы или училища:</font></td>
<td valign=center>
    <html:text accesskey="н" styleClass="text_f9" name="abit_A" property="nomerShkoly" maxlength="4" size='4' tabindex="12" value="*"/>
</td>
<td width=1%></td>
<td><%--html:radio name="abit_A" property="priznakSortirovki" value="OtsenkaEge"/--%>&nbsp;</td>
<td valign=center><font class="text_9">&nbsp;Балл&nbsp;ЕГЭ:&nbsp;</font></td>
<td valign=center><font class="text_9">от</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Ege_ot" 
               maxlength="3" size='1' tabindex="36" value="0"/><font class="text_9"> до</font>
    <html:text accesskey="о" styleClass="text_f9_short" name="abit_A" property="otsenka_Ege_do" 
               maxlength="3" size='1' tabindex="37" value="100"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="PolnoeNaimenovanieZavedenija"/>
<td valign=center><font class="text_9">&nbsp;Наименов-е оконч. завед.:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="polnoeNaimenovanieZavedenija" tabindex="13">
    <html:option value="*" />
    <html:options collection="abit_A_S8" property="polnoeNaimenovanieZavedenija" labelProperty="sokr" />
    </html:select>
</td>
<td width=1%></td>
<td><%--html:radio name="abit_A" property="priznakSortirovki" value="Apelljatsija"/--%>&nbsp;</td>
<td valign=center><font class="text_9">&nbsp;Апелляция:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="apelljatsija" tabindex="39">
    <html:option value="*" />
    <html:option value="д" />
    <html:option value="н" />
    </html:select>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="GodOkonchanijaSrObrazovanija"/>
<td valign=center><font class="text_9">&nbsp;Год окончания ср. образ.:</font></td>
<td valign=center><font class="text_9">от</font>
    <html:text accesskey="г" styleClass="text_f9_short" name="abit_A" property="special5" 
               maxlength="4" size='4' tabindex="14" value="1950"/><font class="text_9"> до</font>
    <html:text accesskey="г" styleClass="text_f9_short" name="abit_A" property="special6" 
               maxlength="4" size='4' tabindex="15" value="9999"/>
</td>
<td width=1%></td>
<td><%--html:radio name="abit_A" property="priznakSortirovki" value="Data"/--%>&nbsp;</td>
<td valign=center><font class="text_9">&nbsp;Дата апелляции:</font></td>
<td valign=center>
    <html:text accesskey="н" styleClass="text_f9_short" name="abit_A" property="dataApelljatsii" maxlength="10" size='10' tabindex="40" value="00-00-0000"/>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="GdePoluchilSrObrazovanie"/>
<td valign=center><font class="text_9">&nbsp;Где получил ср. образ.:</font></td>
<td valign=center>
    <html:select onchange="autoInit();" styleClass="select_f3" name="abit_A" property="gdePoluchilSrObrazovanie" tabindex="16">
    <html:option value="*" />
    <html:option value="п" />
    <html:option value="г" />
    <html:option value="с" />
    <html:option value="о" />
    </html:select>
</td>
<td width=1%></td>
<td colspan=3 valign=center align=center><font class="text_9">&nbsp;ПРЕДМЕТЫ&nbsp;ВСТУПИТЕЛЬНЫХ&nbsp;ИСПЫТАНИЙ&nbsp;</font></td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Nazvanie"/>
<td valign=center><font class="text_9">&nbsp;Населенный пункт:</font></td>
<td valign=center>
    <html:text accesskey="г" onchange="autoInit2();" styleClass="text_f9" name="abit_A" property="nazvanie" 
               maxlength="150" size="25" tabindex="17" value="*"/>
</td>
<td width=1%></td>
<td colspan="3" rowspan="4">
 <table align=left border=0 cellSpacing=0>
   <tr>
<%int i=41,j=1;%>
<logic:iterate id='predms' name='predms' scope='request'>
   <bean:define id='predmets' name='predms' type="abit.bean.AbiturientBean" />
   <td width=20></td>
   <td align=center vAlign=center width=80><font class="text_9">&nbsp;<bean:write name="predmets" property="nazvaniePredmeta"/>:&nbsp;</font><input type="checkbox" name="prdm<%=String.valueOf(predmets.getPredmet())%>" tabindex="<%=String.valueOf(i++)%>">
   </td>
<% if(j % 6 == 0) out.println("<tr>"); j++; %>
</logic:iterate>
  <tr><td height="12"></tr>
  <tr>
   <td colspan=12 valign=center><font class="text_9">&nbsp;&nbsp;&nbsp;&nbsp;ВСЕ&nbsp;Предметы:</font>&nbsp;<input type="checkbox" name="all_pr" value="all_pr" checked onclick="CheckAllPr();" tabindex="<%=String.valueOf(i++)%>"></td>
  </tr>
 </table>
</td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NazvanieRajona"/>
<td valign=center><font class="text_9">&nbsp;Район (не для г. Пензы):</font></td>
<td valign=center>
    <html:text accesskey="р" styleClass="text_f9" name="abit_A" property="nazvanieRajona" 
               maxlength="50" size="25" tabindex="18" value="*"/>
</td>
<td width=1%></td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="NazvanieOblasti"/>
<td valign=center><font class="text_9">&nbsp;Область или республика:</font></td>
<td valign=center>
    <html:text accesskey="о" styleClass="text_f9" name="abit_A" property="nazvanieOblasti" 
               maxlength="50" size="25" tabindex="19" value="*"/>
</td>
<td width=1%></td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="DokumentyHranjatsja"/>
<td valign=center><font class="text_9">&nbsp;Документы хранятся:</font></td>
<td valign=center>
    <html:select styleClass="select_f3" name="abit_A" property="dokumentyHranjatsja" tabindex="20">
    <html:option value="*" />
    <html:option value="д" />
    <html:option value="н" />
    </html:select>
</td>
<td width=1%></td>
</tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td><html:radio name="abit_A" property="priznakSortirovki" value="Ball"/>
<td valign=center><font class="text_9">&nbsp;Набранный балл:</font></td>
<td valign=center>
    <html:text accesskey="н" styleClass="text_f9" name="abit_A" property="special7" 
               maxlength="3" size="2" tabindex="21" value="*"/>
</td>
<td width=1%></td>
<td width=1%></td>

</tr>
<%-------------------- Строка таблицы -------------------------%>
<tr>

<td><html:radio name="abit_A" property="priznakSortirovki" value="PreemptiveRight"/>
<td valign=center><font class="text_9">&nbsp;Преимущественные права:</font></td>
<td valign=center>
    <html:text accesskey="о" styleClass="text_f9" name="abit_A" property="preemptiveRight"  size='25' maxlength='50' tabindex="51" value="*"/>
</td>
</tr>


<tr>
<td>
</td>
<td>

   <font class="text_9">&nbsp;Сдача вступ. исп. Дист.</font></td>
   <td><html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_1" /></td></tr>

<%-------------------- Строка таблицы -------------------------%>
<tr>
<td align="center" valign="middle" colspan="8"><font class="text_9">Результаты сортировать по:</font>&nbsp;&nbsp;
         <html:select styleClass="select_f3" name="abit_A" property="special4" tabindex="<%=String.valueOf(i++)%>">
          <html:option value="ASC">возрастанию</html:option>
          <html:option value="DESC">убыванию</html:option>
         </html:select>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="text_9">&nbsp;Находить&nbsp;все&nbsp;специальности&nbsp;конкурса:&nbsp;</font>
     <html:select styleClass="select_f1" name="abit_A" property="useAllSpecs" tabindex="<%=String.valueOf(i++)%>">
     <html:option value="no">нет</html:option>
     <html:option value="yes">да</html:option>
    </html:select>
</td>
</tr>
</table>

<%-------------------- Дополнительная таблица ----------------------%>
<table border="0" align="center">
<tr align="center">
  <td><html:reset styleClass="button" value="Сброс параметров"/></td>
  <td><html:submit styleClass="button" value="Найти"/></td>
</html:form>
<logic:notEqual name='index' scope='request' value='adminIndex'>
<html:form action="/abiturient.do">
  <td><html:submit styleClass="button" value="Добавить абит-та"/></td>
</html:form>
</logic:notEqual>
<html:form action="/abit_s_ots.do">
  <td><html:button styleClass="button" onclick="help_me();" property="hlp" value="Справка"/>
  <td><html:submit styleClass="button" property="exit" value="Выход"/></td>
</html:form>
</tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="full">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<logic:notEqual name='index' scope='request' value='adminIndex'>
<template:put name="title">Результаты поиска абитуриентов</template:put>
<template:put name="target_name">Результаты поиска по оценкам</template:put>
</logic:notEqual>
<logic:equal name='index' scope='request' value='adminIndex'>
<template:put name="title">Результаты поиска абитуриентов для формирования групп</template:put>
<template:put name="target_name">Результаты поиска абитуриентов для формирования групп</template:put>
</logic:equal>
<template:put name="content">
<html:form action="/abit_s_ots?action=searching">
<p align=left><font class="text_10">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Всего&nbsp;найдено&nbsp;строк:&nbsp;&nbsp;<bean:write name="abit_A" property="special22"/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
из <bean:write name="abit_A" property="priznakSortirovki"/>.
</font>
<table align=center border=1 cellSpacing=0>
<thead>
<tr>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;№&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Докум.&nbsp; &nbsp;хран.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Копия&nbsp; &nbsp;аттест.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Форма&nbsp; &nbsp;обуч.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Основа&nbsp; &nbsp;обуч.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Спец. пост.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Спец. зачисл.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Номер&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;личн.&nbsp; дела&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Группа&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Фамилия&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Имя&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Отчество&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Курсы&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Отличия&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Льготы&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;&nbsp;Номер&nbsp;платн.&nbsp;дог.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Год&nbsp;оконч.&nbsp; &nbsp;средн.&nbsp;обр.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Пол&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Где &nbsp;получил&nbsp; ср.&nbsp;обр.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Номер&nbsp; &nbsp;школы&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Населенный&nbsp;пункт&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Район&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Область&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Полное&nbsp;наименование&nbsp;заведения&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Тип&nbsp; &nbsp;оконч.&nbsp; &nbsp;завед.&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;&nbsp;Тип&nbsp;&nbsp; док.</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Паспорт&nbsp; &nbsp;(cерия,&nbsp;номер)&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Дата&nbsp;&nbsp;выдачи&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Кем&nbsp;выдан&nbsp;паспорт&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Балл&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Номер&nbsp; серт.&nbsp;ЕГЭ</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Копия&nbsp; серт.&nbsp;ЕГЭ</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;Аттестат&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;Зачислен&nbsp;</font></td>
  <td rowspan=2 valign=middle align=middle><font class="text_th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ФИО&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
  <td width='5'></td>
  <logic:iterate id='predms' name='predms' scope='request'>
     <bean:define id='predmets' name='predms' type="abit.bean.AbiturientBean" />
     <td colspan=5 valign=middle align=middle><font class="text_th">&nbsp;<bean:write name="predmets" property="nazvaniePredmeta"/>&nbsp;</font></td>
     <td width='5'></td>
  </logic:iterate>
</tr>
<tr>
  <td width='5' bgcolor="black"></td>
  <logic:iterate id='predms' name='predms' scope='request'>
  <td bgcolor="#ADD8E6" valign=middle align=middle><font class="text_th">&nbsp;А&nbsp;</font></td>
  <td bgcolor="#EEE8AA" valign=middle align=middle><font class="text_th">&nbsp;Е&nbsp;</font></td>
  <td bgcolor="#DCDCDC" valign=middle align=middle><font class="text_th">&nbsp;З&nbsp;</font></td>
  <td bgcolor="#8FBC8B" valign=middle align=middle><font class="text_th">&nbsp;Э&nbsp;</font></td>
  <td bgcolor="#FFB6C1" valign=middle align=middle><font class="text_th">&nbsp;ГСЭ&nbsp;</font></td>
  <td width='5' bgcolor="black"></td>
  </logic:iterate>
</tr>
</thead>
<logic:iterate id="abit_A" name="abits_A" scope='request'>
<tr>
  <td valign=center align=center>&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="number"/></html:link>&nbsp;</td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="dokumentyHranjatsja"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="tipDokSredObraz"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="formaOb"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="osnovaOb"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="special1"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="special8"/>&nbsp;</font></td>
  <td valign=center align=center>&nbsp;<html:link href="abit_md.do?action=give_ots" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="nomerLichnogoDela"/></html:link>&nbsp;</td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="gruppa"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<html:link href="abit_md.do?action=mod_del" paramName="abit_A" paramId="kodAbiturienta" paramProperty="kodAbiturienta" styleClass="link_hov_blue"><bean:write name="abit_A" property="familija"/></html:link>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="imja"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="otchestvo"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="shifrKursov"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="shifrMedali"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="shifrLgot"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nomerPlatnogoDogovora"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="godOkonchanijaSrObrazovanija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="pol"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="gdePoluchilSrObrazovanie"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nomerShkoly"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nazvanie"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nazvanieRajona"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nazvanieOblasti"/>&nbsp;</font></td>
  <td valign=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="polnoeNaimenovanieZavedenija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="tipOkonchennogoZavedenija"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="tipDokumenta"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9"><bean:write name="abit_A" property="seriaDokumenta"/>&nbsp;<bean:write name="abit_A" property="nomerDokumenta"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="dataVydDokumenta"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="kemVydDokument"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="ball"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="nomerSertifikata"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="kopijaSertifikata"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="seriaAtt"/>&nbsp;
                                                            <bean:write name="abit_A" property="nomerAtt"/>&nbsp;</font></td>
  <td valign=center align=center><font class="text_9">&nbsp;<bean:write name="abit_A" property="prinjat"/>&nbsp;</font></td>
  <td valign=center align=left><font class="text_9">&nbsp;<bean:write name="abit_A" property="fio"/>&nbsp;</font></td>
  <td width='5' bgcolor="black"></td>
  <bean:define id="prdms" name="abit_A" property="notes"/>
  <logic:iterate id='prdm' name='prdms'>
    <td bgcolor="#ADD8E6" valign=middle align=middle><font class="text_th">&nbsp;<bean:write name="prdm" property="otsenka_Att"/>&nbsp;</font></td>
    <td bgcolor="#EEE8AA" valign=middle align=middle><font class="text_th">&nbsp;<bean:write name="prdm" property="otsenka_Ege"/>&nbsp;</font></td>
    <td bgcolor="#DCDCDC" valign=middle align=middle><font class="text_th">&nbsp;<bean:write name="prdm" property="otsenka_Zaj"/>&nbsp;</font></td>
    <td bgcolor="#8FBC8B" valign=middle align=middle><font class="text_th">&nbsp;<bean:write name="prdm" property="otsenka_Exam"/>&nbsp;</font></td>
    <td bgcolor="#FFB6C1" valign=middle align=middle><font class="text_th">&nbsp;<bean:write name="prdm" property="examen"/>&nbsp;</font></td>
    <td width='5' bgcolor="black"></td>
  </logic:iterate>

</tr>
</logic:iterate>
<%-- 
     for Debugging Purposes Only !!!
<tr><td colspan=1000><bean:write name="abit_A" property="special1"/></td></tr> --%>
<logic:notPresent name="abit_A" property="kodAbiturienta">
<tr>
  <td align=center valign=center colspan=110><font class="text_10">
     &nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     по&nbsp;Вашему&nbsp;запросу&nbsp;не&nbsp;найдено&nbsp;ни&nbsp;одной&nbsp;записи
  </font></td>
</logic:notPresent>
<tr><td colspan="110">
<%-- Скрипт перемещения кнопок за скроллером --%>
<div id="SlideButtons" style="FONT-SIZE: 0.65em; WIDTH:1px ; FONT-FAMILY: 'Trebuchet MS',Tahoma,Verdana,Geneva,Arial,Helvetica,sans-serif; POSITION: relative">
<table valign=center align=left width=100% cellSpacing=2 border=0>
<tr>
  <td valign=center align=middle>
  <html:submit property="beg" styleClass="button_sd" value="Начало"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_s_ots?action=searching">
  <html:submit property="prev" styleClass="button_sd" value="Предыдущие"/>
</html:form></td>
<logic:notEqual name='index' scope='request' value='adminIndex'>
  <td valign=center align=middle>
<html:form action="/search_rtf_ots.do">
  <html:submit styleClass="button_sd" value="RTF-отчёт"/>
</html:form>
<html:form action="/search_excel.do">
  <html:submit styleClass="button_sd" value="Excel-Отчет"/>
</html:form>
</td>
</logic:notEqual>
<logic:equal name='index' scope='request' value='adminIndex'>
  <td valign=center align=middle>
<html:form action="/abit_s_ots.do?action=fgr">
  <html:submit styleClass="button_sd" value="Формирование групп"/>
</html:form></td>
</logic:equal>
  <td valign=center align=middle>
<html:form action="/abit_s_ots.do?action=search">
  <html:submit styleClass="button_sd" value="Поиск"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_s_ots.do?action=searching">
  <html:submit property="next" styleClass="button_sd" value="Следующие"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_s_ots.do?action=searching">
  <html:submit property="end" styleClass="button_sd" value="Конец"/>
</html:form></td>
  <td valign=center align=middle>
<html:form action="/abit_s_ots.do?action=searching">
  <html:submit styleClass="button_sd" property="exit" value="Выход"/>
</html:form></td>
</tr>
</table>
</td>
<%-- Скрипт перемещения кнопок за скроллером --%>
<SCRIPT language=JavaScript src="layouts/all/slideButtons.js" defer type=text/javascript></SCRIPT></div>
</tr></table>
<BR><BR>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%------------------ Полное содержимое таблицы --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="form_fgr">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">Формирование групп по результатам поиска абитуриентов</template:put>
<template:put name="target_name">Формирование групп по результатам поиска абитуриентов</template:put>
<template:put name="content">
<html:form action="/abit_s_ots?action=go_fgr">
<html:hidden name="abit_A" property="special7"/>
<table align=center border=0 cellSpacing=0> 
<tr>
    <td valign=center colspan="5" align=center><font class="text_10">Всего&nbsp;найдено:&nbsp;</font><font class="text_10_mark"><bean:write name="abit_A" property="special10"/>&nbsp;абитуриента(ов)</font></td>
</tr>
<tr>
    <td valign=center colspan="5" align=center><font class="text_10">Форма&nbsp;обучения:&nbsp;</font><font class="text_10_mark"><bean:write name="abit_A" property="special8"/></font></td>
</tr>
<tr>
    <td valign=center colspan="5" align=center><font class="text_10">Основа&nbsp;обучения:&nbsp;</font><font class="text_10_mark"><bean:write name="abit_A" property="special9"/></font></td>
</tr>
<tr>
    <td valign=center colspan="5" align=center><hr></td>
</tr>
<tr>
    <td valign=center colspan="5" align=center><font class="text_11">Целевой&nbsp;факультет:</font></td>
</tr>
<tr>
    <td valign=center colspan="5" align=center><font class="text_11">
      <logic:iterate id="abit_A" name="abit_A_S2" scope='request'>
        <html:link href="abit_s_ots.do?action=fgr" paramId="kFak" paramName="abit_A" paramProperty="kodFakulteta" styleClass="link_hov_blue">
          <bean:write name="abit_A" property="abbreviaturaFakulteta"/>
        </html:link>
      </logic:iterate>
    </td>
</tr>
<tr>
    <td></td><td valign=center colspan="3" align=center><hr></td><td></td>
</tr>
<tr>
    <td></td>
    <td valign=center align=center width="40%">
        <font class="text_10_mark">Доступные&nbsp;для&nbsp;формирования&nbsp;группы:</font>
    </td>
    <td width="450"></td>
    <td valign=center align=center width="20%">
        <font class="text_10_mark">Выбранные&nbsp;группы:</font>
    </td>
    <td></td>
</tr>
<tr>
<td width="20%"></td>
<td align="center" valign="middle" width="40%">
<table border=1 cellPadding="1" cellSpacing="0" align="center" valign="middle">
<tr>
    <td align="center" colspan="2" width="150">
     <font class="text_10">&nbsp;Очные&nbsp;</font>
    </td>
    <td width="25"></td>
    <td align="center" colspan="2" width="150">
     <font class="text_10">&nbsp;Заочные&nbsp;</font>
    </td>
</tr>
<tr>
    <td valign="middle" align="center"><font class="text_10">&nbsp;Бюдж.&nbsp;</font></td>
    <td valign="middle" align="center"><font class="text_10">&nbsp;Дог.&nbsp;</font></td>
    <td width="25">
    <td valign="middle" align="center"><font class="text_10">&nbsp;Бюдж.&nbsp;</font></td>
    <td valign="middle" align="center"><font class="text_10">&nbsp;Дог.&nbsp;</font></td>
<tr>
    <td valign="middle" align="center">
     <html:select name="abit_A" styleClass="select_fix_width" property="special3" size="10" onclick="add_val(3);" tabindex="1">
       <html:options collection="groups_bud" property="kodGruppy"  labelProperty="gruppa"/>
     </html:select>
    </td>
    <td valign="middle" align="center">
     <html:select name="abit_A" styleClass="select_fix_width" property="special4" size="10" onclick="add_val(4);" tabindex="2">
       <html:options collection="groups_kon" property="kodGruppy"  labelProperty="gruppa"/>
     </html:select>
    </td>
    <td width="15"></td>
    <td valign="middle" align="center">
     <html:select name="abit_A" styleClass="select_fix_width" property="special5" size="10" onclick="add_val(5);" tabindex="3">
       <html:options collection="groups_z_bud" property="kodGruppy"  labelProperty="gruppa"/>
     </html:select>
    </td>
    <td valign="middle" align="center">
     <html:select name="abit_A" styleClass="select_fix_width" property="special6" size="10" onclick="add_val(6);" tabindex="4">
       <html:options collection="groups_z_kon" property="kodGruppy"  labelProperty="gruppa"/>
     </html:select>
    </td>
</tr>
</table>
</td>
  <td valign="middle" align="center" width="450"><img src="layouts/all/right_arr.gif"></td>
  <td valign="middle" align="center" width="20%"><br>
    <table border=5 cellPadding="0" cellSpacing="0" align="center" valign="middle">
      <tr><td></td></tr>
      <tr>
        <td>
          <html:select styleClass="select_fix_width" size='14' name="abit_A" property="special1" onclick="del_val();" tabindex="5">
          </html:select> 
        </td>
      </tr>
    </table>
  </td>
  <td valign="middle" align="center" width="45%"></td>
</tr>
<tr>
    <td></td><td valign=center colspan="3" align=center><hr></td><td></td>
</tr>
<tr>
    <td align=center colspan="5">
        <font class="text_10_mark">В&nbsp;каждой&nbsp;группе&nbsp;должно&nbsp;быть:</font>
    </td>
</tr>
<tr><td height="3">
<tr>
    <td align=center colspan="5"><font class="text_10">не более:</font>&nbsp;&nbsp;
    <html:text accesskey="н" name="abit_A" styleClass="text_f10_short" property="maxCountAbiturients" value="25" size="2" maxlength="2" tabindex="6"/>
    &nbsp;&nbsp;человек
    </td>
</tr>
<tr>
    <td></td><td align=center><hr></td><td align=center><hr></td><td align=center><hr></td>
</tr>
<tr><td height="15"></td></tr>
</table>
<table>
<tr><td>
  <html:submit styleClass="button" value="Формировать" onclick="return checkFields_fgr();" tabindex="7"/></td>
</html:form>
<html:form action="/abit_s_ots.do?action=searching">
  <td><html:submit styleClass="button" property="exit" tabindex="8" value="Выход"/></td>
</html:form>
</template:put>
</template:insert>
</logic:equal>

<%-----------------------------------------------------------------%>
<%-------------          Результаты обработки           -----------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="fg_res">
<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name="target_name">Результаты формирования групп</template:put>
<template:put name="sub_name">Результаты формирования групп</template:put>
<template:put name='title'>Формирование групп</template:put>
<template:put name='content'>
<BR>
<p align=center><font class="text_11">
 <logic:iterate id="mess" name="fgr_msgs" scope="request" type="abit.bean.MessageBean">
  <br><bean:write name="mess" property="status"/>&nbsp;&nbsp;<bean:write name="mess" property="message"/>
 </logic:iterate>
</font></p>
<html:form action="/abit_s_ots.do?action=search">
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" value="Поиск для ФГ"/></td>
</html:form>&nbsp;&nbsp;
<html:form action="/fgruppy?action=main&view">
  <td><html:submit styleClass="button" value="Просмотр групп"/></td>
</html:form>&nbsp;&nbsp;
<html:form action="/gruppy?action=new">
  <td><html:submit styleClass="button" value="Создание групп"/></td>
</html:form>
&nbsp;&nbsp;
<html:form action="/abit_s_ots.do?action=searching">
  <td><html:submit styleClass="button" property="exit" value="Выход"/></td>
</html:form>
</tr></table>
</template:put>
</template:insert>
</logic:equal>
</logic:present>