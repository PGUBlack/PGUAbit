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

<logic:notPresent name='abitModDelAction' scope='request'>
 <logic:redirect forward='abit_md_online'/>
</logic:notPresent>

<logic:notPresent name="abitSrchForm" property="action">
<%---------------- Реакция на вызов без параметров ----------------%>
</logic:notPresent>

<bean:define id="tema" name="user" property="idTema"/>

<bean:define id="userGroup" name = "user" property = "group"/>
<bean:define id ="userGroupName" name = "userGroup" property = "groupName"/>

<%! 
   int count;
   int tabindex;
   String kP = "0";
%>

<SCRIPT LANGUAGE="JavaScript">

function constructLink(){

  var FBS_link;

  FBS_link  = "http://10.0.3.1/Certificates/CommonNationalCertificates/RequestByPassportForOpenedFbs.aspx";

  myRef = window.open(FBS_link,'FBS_EGE','left=20,top=20,toolbar=1,resizable=1,scrollbars=1');

  myRef.document.forms(0).cSeries.value = "1122";
  myRef.document.forms(0).ctl00$cphCertificateContent$txtSeries.value = "1122";
  myRef.document.forms(1).cSeries.value = "1122";
  myRef.document.forms(1).ctl00$cphCertificateContent$txtSeries.value = "1122";
  myRef.document.forms(2).cSeries.value = "1122";
  myRef.document.forms(2).ctl00$cphCertificateContent$txtSeries.value = "1122";

//encodeURIComponent(document.forms(0).nomerSertifikata.value)

}

function checkFields(){
  var valid2 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-.,()"
  var valid3 = "пзис"
  var valid4 = "1234567890оздмфвуюп."
  var valid9 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ1234567890-.,()"
  var valid99 = "1234567890-"
  var temp

//-------------------------------------------------------------------------------------------------
//------ 1. ЛИЧНЫЕ ДАННЫЕ
//-------------------------------------------------------------------------------------------------

if(document.forms(0).familija.value.length == 0){
  alert("Необходимо заполнить поле ''Фамилия''")
  document.forms(0).familija.focus()
  return false
}
for (var i=0; i<document.forms(0).familija.value.length; i++){
  temp = "" + document.forms(0).familija.value.substring(i, i+1)
  if(valid2.indexOf(temp) == "-1") {
    alert("Поле ''Фамилия'' может состоять только из букв русского алфавита")
    document.forms(0).familija.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).imja.value.length == 0){
  alert("Необходимо заполнить поле ''Имя''")
  document.forms(0).imja.focus()
  return false
}
for (var i=0; i<document.forms(0).imja.value.length; i++){
  temp = "" + document.forms(0).imja.value.substring(i, i+1)
  if(valid2.indexOf(temp) == "-1") {
    alert("Поле ''Имя'' может состоять только из букв русского алфавита")
    document.forms(0).imja.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).otchestvo.value.length == 0){
  alert("Необходимо заполнить поле ''Отчество''")
  document.forms(0).otchestvo.focus()
  return false
}
for (var i=0; i<document.forms(0).otchestvo.value.length; i++){
  temp = "" + document.forms(0).otchestvo.value.substring(i, i+1)
  if(valid2.indexOf(temp) == "-1") {
    alert("Поле ''Отчество'' может состоять только из букв русского алфавита")
    document.forms(0).otchestvo.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).grajdanstvo.value.length == 0){
  alert("Необходимо заполнить поле ''Гражданство''")
  document.forms(0).grajdanstvo.focus()
  return false
} 
for (var i=0; i<document.forms(0).grajdanstvo.value.length; i++){
  temp = "" + document.forms(0).grajdanstvo.value.substring(i, i+1)
  if(valid2.indexOf(temp) == "-1") {                      
    alert("Поле ''Гражданство'' может состоять только из букв русского алфавита")
    document.forms(0).grajdanstvo.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).pol.value == "-"){
  alert("Необходимо указать Пол")
  document.forms(0).pol.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
var data = /^(\d{2})\-(\d{2})\-(\d{4})$/
	if(document.forms(0).dataRojdenija.value == "00-00-0000" || document.forms(0).dataRojdenija.value.length == 0||!data.test(document.forms(0).dataRojdenija.value)){
	  alert("Необходимо заполнить поле ''Дата рождения''")
	  document.forms(0).dataRojdenija.focus()
	  return false
	}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).tipDokumenta.value.length == 0){
  alert("Необходимо заполнить поле ''Тип документа''")
  document.forms(0).tipDokumenta.focus()
  return false
}
for (var i=0; i<document.forms(0).tipDokumenta.value.length; i++){
  temp = "" + document.forms(0).tipDokumenta.value.substring(i, i+1)
  if (valid3.indexOf(temp) == "-1") {
    alert("Поле ''Тип документа'' может состоять только из букв: ''п'', ''з'', ''и'' или ''с''")
    document.forms(0).tipDokumenta.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).seriaDokumenta.value == ""){
  alert("Необходимо заполнить ''Серию паспорта''")
  document.forms(0).seriaDokumenta.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).nomerDokumenta.value == ""){
  alert("Необходимо заполнить ''Номер паспорта''")
  document.forms(0).nomerDokumenta.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).mestoRojdenija.value.length == 0){
  alert("Необходимо заполнить поле ''Место рождения''")
  document.forms(0).mestoRojdenija.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).kemVydDokument.value.length == 0){
  alert("Необходимо заполнить поле ''Кем выдан паспорт''")
  document.forms(0).kemVydDokument.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).kemVydDokument.value.length == 0){
  alert("Необходимо заполнить ''Кем выдан паспорт''")
  document.forms(0).kemVydDokument.focus()
  return false
}
for (var i=0; i<document.forms(0).kemVydDokument.value.length; i++){
  temp = "" + document.forms(0).kemVydDokument.value.substring(i, i+1)
  if (valid9.indexOf(temp) == "-1") {
    alert("Поле ''Кем выдан паспорт'' может состоять только из букв или цифр")
    document.forms(0).kemVydDokument.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).dataVydDokumenta.value == "00-00-0000"||!data.test(document.forms(0).dataVydDokumenta.value)){
	  alert("Необходимо заполнить ''Дату выдачи паспорта''")
	  document.forms(0).dataVydDokumenta.focus()
	  return false
	}
for (var i=0; i<document.forms(0).dataVydDokumenta.value.length; i++){
  temp = "" + document.forms(0).dataVydDokumenta.value.substring(i, i+1)
  if (valid99.indexOf(temp) == "-1") {
    alert("Поле ''Дата выдачи паспорта'' может состоять из цифр и разделителей")
    document.forms(0).dataVydDokumenta.focus()
    return false
  }
}

//-------------------------------------------------------------------------------------------------
//------ 2. АДРЕС ПРОПИСКИ
//-------------------------------------------------------------------------------------------------

if(document.forms(0).gorod_Prop.value.length == 0){
  alert("Необходимо заполнить поле ''Населённый пункт''")
  document.forms(0).gorod_Prop.focus()
  return false
} 
for (var i=0; i<document.forms(0).gorod_Prop.value.length; i++){
  temp = "" + document.forms(0).gorod_Prop.value.substring(i, i+1)
  if(valid9.indexOf(temp) == "-1") {
    alert("Поле ''Населённый пункт'' может состоять только из букв русского алфавита или цифр")
    document.forms(0).gorod_Prop.focus()
    return false
  }
}
//------------------------------------------------------------------------------------------------
if(document.forms(0).ulica_Prop.value.length == 0){
  alert("Необходимо заполнить поле ''Улица, проспект''")
  document.forms(0).ulica_Prop.focus()
  return false
} 
for (var i=0; i<document.forms(0).ulica_Prop.value.length; i++){
  temp = "" + document.forms(0).ulica_Prop.value.substring(i, i+1)
  if(valid9.indexOf(temp) == "-1") {
    alert("Поле ''Улица, проспект'' может состоять только из букв русского алфавита или цифр")
    document.forms(0).ulica_Prop.focus()
    return false
  }
}
//------------------------------------------------------------------------------------------------
if(document.forms(0).dom_Prop.value == "0"){
  alert("Необходимо заполнить поле ''Дом, корпус''")
  document.forms(0).dom_Prop.focus()
  return false
} 
for (var i=0; i<document.forms(0).dom_Prop.value.length; i++){
  temp = "" + document.forms(0).dom_Prop.value.substring(i, i+1)
  if(valid9.indexOf(temp) == "-1") {
    alert("Поле ''Дом, корпус'' может состоять только из букв русского алфавита или цифр")
    document.forms(0).dom_Prop.focus()
    return false
  }
}
//------------------------------------------------------------------------------------------------
if(document.forms(0).kvart_Prop.value == "0"){
  alert("Необходимо заполнить поле ''Квартира, комната''")
  document.forms(0).kvart_Prop.focus()
  return false
} 
for (var i=0; i<document.forms(0).kvart_Prop.value.length; i++){
  temp = "" + document.forms(0).kvart_Prop.value.substring(i, i+1)
  if(valid9.indexOf(temp) == "-1") {
    alert("Поле ''Квартира, комната'' может состоять только из букв русского алфавита или цифр")
    document.forms(0).kvart_Prop.focus()
    return false
  }
}

//-------------------------------------------------------------------------------------------------
//------ 3. СВЕДЕНИЯ ОБ ОБРАЗОВАНИИ
//-------------------------------------------------------------------------------------------------

for (var i=0; i<document.forms(0).godOkonchanijaSrObrazovanija.value.length; i++){
  temp = "" + document.forms(0).godOkonchanijaSrObrazovanija.value.substring(i, i+1)
  if(valid4.indexOf(temp) == "-1") {
    alert("Поле ''Год получения образования'' может состоять только из цифр")
    document.forms(0).godOkonchanijaSrObrazovanija.focus()
    return false
  }
}
if(document.forms(0).godOkonchanijaSrObrazovanija.value < 1950 ||
   document.forms(0).godOkonchanijaSrObrazovanija.value > 2030) {
    alert("Поле ''Год получения образования'' может быть от 1950 до 2030")
    document.forms(0).godOkonchanijaSrObrazovanija.focus()
    return false
}
if(document.forms(0).gdePoluchilSrObrazovanie.value == "-") {
  alert("Необходимо указать ''Где получил образование''")
  document.forms(0).gdePoluchilSrObrazovanie.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).nazvanie.value.length == 0){
  alert("Необходимо заполнить поле ''Населенный пункт''")
  document.forms(0).nazvanie.focus()
  return false
}
for (var i=0; i<document.forms(0).nazvanie.value.length; i++){
  temp = "" + document.forms(0).nazvanie.value.substring(i, i+1)
  if(valid9.indexOf(temp) == "-1") {
    alert("Поле ''Населенный пункт'' может состоять только из букв русского алфавита или цифр")
    document.forms(0).nazvanie.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).nazvanieRajona.value.length == 0){
  alert("Необходимо заполнить поле ''Район''")
  document.forms(0).nazvanieRajona.focus()
  return false
}
for(var i=0; i<document.forms(0).nazvanieRajona.value.length; i++){
  temp = "" + document.forms(0).nazvanieRajona.value.substring(i, i+1)
  if(valid9.indexOf(temp) == "-1") {
    alert("Поле ''Район'' может состоять только из букв русского алфавита или цифр")
    document.forms(0).nazvanieRajona.focus()
     return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).nazvanieOblasti.value.length == 0){
  alert("Необходимо заполнить поле ''Область или республика''")
  document.forms(0).nazvanieOblasti.focus()
  return false
} 
for (var i=0; i<document.forms(0).nazvanieOblasti.value.length; i++){
  temp = "" + document.forms(0).nazvanieOblasti.value.substring(i, i+1)
  if(valid9.indexOf(temp) == "-1") {
    alert("Поле ''Область или республика'' может состоять только из букв русского алфавита или цифр")
    document.forms(0).nazvanieOblasti.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).tipOkonchennogoZavedenija.value == "-"){
  alert("Необходимо указать ''Тип оконченного заведения''")
  document.forms(0).tipOkonchennogoZavedenija.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).nomerShkoly.value.length == 0 && !(document.forms(0).tipOkonchennogoZavedenija.value == 'т' || 
   document.forms(0).tipOkonchennogoZavedenija.value == 'д' || document.forms(0).tipOkonchennogoZavedenija.value == 'у')) {
  alert("Необходимо задать ''Номер учебного заведения''")
  document.forms(0).nomerShkoly.focus()
  return false
}
for (var i=0; i<document.forms(0).nomerShkoly.value.length; i++){
  temp = "" + document.forms(0).nomerShkoly.value.substring(i, i+1)
  if(valid4.indexOf(temp) == "-1") {
    alert("Поле ''Номер школы или училища'' может состоять только из цифр")
    document.forms(0).nomerShkoly.focus()
    return false
  }
}
//-------------------------------------------------------------------------------------------------
if((document.forms(0).kodZavedenija.options[document.forms(0).kodZavedenija.selectedIndex].text == "-") && 
   (document.forms(0).polnoeNaimenovanieZavedenija.value == "")){
  alert("Необходимо выбрать ''Название уч. заведения'' или ввести другое название")
  document.forms(0).kodZavedenija.focus()
  return false
}
//------------------------------------------------------------------------------------------------
if(document.forms(0).vidDokSredObraz.value == "-"){
  alert("Необходимо выбрать значние поля ''Вид документа о полученном образовании''")
  document.forms(0).vidDokSredObraz.focus()
  return false
}

//------------------------------------------------------------------------------------------------
if(document.forms(0).tipDokSredObraz.value == "-"){
  alert("Необходимо заполнить поле ''Документ о полученном образовании (копия)''")
  document.forms(0).tipDokSredObraz.focus()
  return false
}
//------------------------------------------------------------------------------------------------
if(document.forms(0).seriaAtt.value == ""){
  alert("Необходимо заполнить поле ''Серия аттестата''")
  document.forms(0).seriaAtt.focus()
  return false
}
//------------------------------------------------------------------------------------------------
if(document.forms(0).nomerAtt.value == ""){
  alert("Необходимо заполнить поле ''Номер аттестата''")
  document.forms(0).nomerAtt.focus()
  return false
}
//------------------------------------------------------------------------------------------------
// if(document.forms(0).kopijaSertifikata.value == "-"){
//   alert("Необходимо указать признак копии сертификата")
//   document.forms(0).kopijaSertifikata.focus()
//   return false
// }
// //-------------------------------------------------------------------------------------------------
// if(document.forms(0).nomerSertifikata.value.length == 0){
//   alert("Необходимо заполнить поле ''Номер сертификата ЕГЭ''")
//   document.forms(0).nomerSertifikata.focus()
//   return false
// }

//-------------------------------------------------------------------------------------------------
//------ 4. ЛЬГОТЫ, ОТЛИЧИЯ, ЦЕЛЕВОЙ ПРИЁМ
//-------------------------------------------------------------------------------------------------

var cur_lgt = document.forms(0).kodLgot.options[document.forms(0).kodLgot.selectedIndex].text;

if(cur_lgt != "н" && document.forms(0).udostoverenieLgoty.value == ""){
  alert("Необходимо указать реквизиты документа, дающего право льготы")
  document.forms(0).udostoverenieLgoty.focus()
  return false
}

//-------------------------------------------------------------------------------------------------

var cur_med = document.forms(0).kodMedali.options[document.forms(0).kodMedali.selectedIndex].text;

if((cur_med == "о" || cur_med == "к") && document.forms(0).diplomOtlichija.value == ""){
  alert("Необходимо указать реквизиты диплома победителя олимпиады")
  document.forms(0).diplomOtlichija.focus()
  return false
}

//-------------------------------------------------------------------------------------------------

if(document.forms(0).inostrannyjJazyk.value == "-"){
  alert("Необходимо указать значение ''Иностранный язык''")
  document.forms(0).inostrannyjJazyk.focus()
  return false
}

//-------------------------------------------------------------------------------------------------
//------ 5. ИНФОРМАЦИЯ О БАЛЛАХ ПО КОНКУРСНЫМ ПРЕДМЕТАМ
//-------------------------------------------------------------------------------------------------

if(document.forms(0).special1.value.length != 0) {

var offset = 0, result = 0, tmp, tmp2;

temp = document.forms(0).special1.value;

do{

   if(temp.indexOf('%',offset+1)!=-1)

     result = temp.substring(temp.indexOf('%',offset)+1,temp.indexOf('%',offset+1));

   else

     result= temp.substring(temp.indexOf('%',offset)+1)

   offset = temp.indexOf('%',offset+1);

// Проверка правильности оценки

  tmp = eval("document.forms(0).Ege_note"+result+".value");

  for (var i=0; i<tmp.length; i++){
    tmp2 = "" + eval("document.forms(0).Ege_note"+result+".value.substring(i, i+1)");
    if(valid4.indexOf(tmp2) == "-1") {
      alert("Поле ''Оценка ЕГЭ'' может содержать только цифры")
      eval("document.forms(0).Ege_note"+result+".focus()")
      return false
    }
  }

  if(tmp == "") {
    alert("Поле ''Оценка ЕГЭ'' не может быть пустым")
    eval("document.forms(0).Ege_note"+result+".focus()")
    return false
  }

  if (tmp > 100 || tmp < 0) {

     alert("Оценка ЕГЭ должна лежать в диапазоне: от ''0'' до ''100''")

     eval("document.forms(0).Ege_note"+result+".focus()")

     return false
  }

  tmp2 = eval("document.forms(0).Examen"+result+".value");

  if (!(tmp2 == 'е' || tmp2 == 'в' || tmp2 == '' || tmp2 == ' ')) {

     alert("Поле ''Допустить к сдаче экзаменов'' может содержать только символ: ''е'' или ''в''")

     eval("document.forms(0).Examen"+result+".focus()")

     return false
  }

//  if ((tmp2 == 'е' || tmp2 == 'в') && tmp != 0) {

//     alert("Нельзя сдавать экзамен (в формате вуза или ЕГЭ),\n если заявленный балл ЕГЭ отличен от нуля")

 //    eval("document.forms(0).Examen"+result+".focus()")

 //    return false
// }   

} while( offset != -1 )
}

//-------------------------------------------------------------------------------------------------
//------ 6. ДОПУСТИТЬ К УЧАСТИЮ В КОНКУРСЕ
//-------------------------------------------------------------------------------------------------

for(var ind1=1;ind1<=6;ind1++){

  if(eval("document.forms(0).s_okso_"+ind1+".value") != "" && eval("document.forms(0).s_okso_"+ind1+".value") != " "){

    for (var i=0; i<eval("document.forms(0).s_okso_"+ind1+".value.length"); i++){
      temp = "" + eval("document.forms(0).s_okso_"+ind1+".value.substring(i, i+1)")
      if(valid4.indexOf(temp) == "-1") {
        alert("Поле ''Шифр ОКСО'' может состоять только из цифр и букв ''о'',''з'',''в'',''м'',''ф'',''у'',''ю'',''д'',''п''")
        eval("document.forms(0).s_okso_"+ind1+".focus()")
        return false
      }
    }

    if(eval("document.forms(0).bud_"+ind1+".checked") == false && 
       eval("document.forms(0).dog_"+ind1+".checked") == false ) {
           alert("Для текущего шифра ОКСО ("+eval("document.forms(0).s_okso_"+ind1+".value")+") необходимо отметить\n хотя бы одно значение из возможных: ''Бюдж.'',''Догов.''")
           eval("document.forms(0).s_okso_"+ind1+".focus()")
           return false
    }
//    if((eval("document.forms(0).fito_"+ind1+".checked") == false &&
//       eval("document.forms(0).three_"+ind1+".checked") == false &&
//       eval("document.forms(0).six_"+ind1+".checked") == false &&
//       eval("document.forms(0).olimp_"+ind1+".checked") == false &&
//       eval("document.forms(0).target_"+ind1+".checked") == false)) {
//           alert("Для текущего шифра ОКСО ("+eval("document.forms(0).s_okso_"+ind1+".value")+") необходимо отметить\n хотя бы одно значение из возможных: ''Олимп.'',''Целев'',''Фито'',''Полн.'',''Сокр.''")
//           eval("document.forms(0).s_okso_"+ind1+".focus()")
//           return false
//    }
  }
}

//-------------------------------------------------------------------------------------------------
if(document.forms(0).s_okso_1.value == "") {
  alert("Необходимо указать ''Шифр'' для приоритетной специальности")
  document.forms(0).s_okso_1.focus()
  return false
}

//-------------------------------------------------------------------------------------------------
//------ 7. ДОПОЛНИТЕЛЬНЫЕ СВЕДЕНИЯ
//-------------------------------------------------------------------------------------------------

if(document.forms(0).nujdaetsjaVObschejitii.value == "-"){
  alert("Необходимо указать ''Потребность в общежитии''")
  document.forms(0).nujdaetsjaVObschejitii.focus()
  return false
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).tel.value.length == 0){
  alert("Необходимо заполнить поле ''Контактный телефон''")
  document.forms(0).tel.focus()
  return false
}
  return true;
}

function autoInit(){
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "-") {
    document.forms(0).nazvanieOblasti.value = ""
    document.forms(0).nazvanie.value = ""
    document.forms(0).nazvanieRajona.value = ""
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "п") {
    document.forms(0).nazvanieOblasti.value = "Пензенская"
    document.forms(0).nazvanie.value = "Пенза"
    document.forms(0).nazvanieRajona.value = ""
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "г") {
    document.forms(0).nazvanieOblasti.value = "Пензенская"
    document.forms(0).nazvanie.value = ""
    document.forms(0).nazvanieRajona.value = ""
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "с") {
    document.forms(0).nazvanieOblasti.value = "Пензенская"
    document.forms(0).nazvanie.value = ""
    document.forms(0).nazvanieRajona.value = ""
  }
  if(document.forms(0).gdePoluchilSrObrazovanie.value == "о") {
    document.forms(0).nazvanieOblasti.value = ""
    document.forms(0).nazvanie.value = ""
    document.forms(0).nazvanieRajona.value = ""
  }
}

function autoInit2(){
  if(document.forms(0).nazvanie.value.toLowerCase() == "кузнецк"){
    document.forms(0).nazvanieRajona.value = "кузнецкий"
    document.forms(0).tipOkonchennogoZavedenija.focus()
  } 
  if(document.forms(0).nazvanie.value.toLowerCase() == "каменка"){
    document.forms(0).nazvanieRajona.value = "каменский"
    document.forms(0).tipOkonchennogoZavedenija.focus()
  }
  if(document.forms(0).nazvanie.value.toLowerCase() == "сердобск"){
    document.forms(0).nazvanieRajona.value = "сердобский"
    document.forms(0).tipOkonchennogoZavedenija.focus()
  }
  if(document.forms(0).nazvanie.value.toLowerCase() == "заречный"){
    document.forms(0).nazvanieRajona.value = ""
    document.forms(0).tipOkonchennogoZavedenija.focus()
  } 
}

function exec() {
  document.forms(0).familija.focus();
}

function autoInit4() {
  if(document.forms(0).tipOkonchennogoZavedenija.value == "ш" ||
     document.forms(0).tipOkonchennogoZavedenija.value == "д" ||
     document.forms(0).tipOkonchennogoZavedenija.value == "в" ||
     document.forms(0).tipOkonchennogoZavedenija.value == "п") {

     document.forms(0).nomerShkoly.disabled = false;
     document.forms(0).nomerShkoly.focus();
  }
  if(document.forms(0).tipOkonchennogoZavedenija.value == "т" ||
     document.forms(0).tipOkonchennogoZavedenija.value == "у") {

     document.forms(0).nomerShkoly.disabled = true;
     document.forms(0).kodZavedenija.focus();
  }
}

function confirmation(){
  if(confirm('Удалить личную карточку?'))
    return true;
  else 
    return false; 
}

</SCRIPT>

<logic:present name="abitSrchForm" property="action">
<bean:define id="action" name="abitSrchForm" property="action"/>

<%-----------------------------------------------------------------%>
<%---------------------- Модификация записи -----------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="md_dl">
<body onLoad="exec();"></body>
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">Модификация карточки абитуриента</template:put>
<template:put name="target_name">Модификация личной карточки абитуриента&nbsp;&nbsp;№ <bean:write name="abit_A" property="nomerLichnogoDela"/></template:put>
<template:put name="content">

<%-------------------------------------%>
<%--   ТАБЛИЦА КАРТОЧКИ АБИТУРИЕНТА  --%>
<%-------------------------------------%>

<html:form action="/abit_md_online.do?action=change" onsubmit="return checkFields();">
<html:hidden name="abit_A" property="special1"/>
<html:hidden name="abit_A" property="kodAbiturienta"/>
<html:hidden name="abit_A" property="nomerLichnogoDela"/>

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
<%-- ЛЕВАЯ ПОЛОВИНКА КАРТОЧКИ ВВОДА  --%>
<%-------------------------------------%>

 <table border="0" width="50%" height="100%" cellSpacing="0" cellPadding="0" class="text">
   <tr><td colspan="2" vAlign="top">

<%--------------------------------%>
<%-- 1я ТАБЛИЦА (ЛИЧНЫЕ ДАННЫЕ) --%>
<%--------------------------------%>
   <table width="100%" border="1" cellSpacing="0" cellPadding="0" frame="BOX" class="text">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle" ><font class="text_th9">&nbsp;1.&nbsp;ЛИЧНЫЕ&nbsp;ДАННЫЕ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Фамилия:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="familija" size="32" maxlength="30" tabindex="1"/>
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Имя:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="imja" size="24" maxlength="30" tabindex="2"/>
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Отчество:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="otchestvo" size="28" maxlength="30" tabindex="3"/>
         </td>
     </tr><tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Гражданство:&nbsp;&nbsp;</td>
         
          
         <td vAlign="middle" height="18">
           
            <html:select styleClass="select_f2" name="abit_A" property="grajdanstvo" tabindex="24">
            <html:options collection="nationalityList" property="name" />
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Пол:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f1" name="abit_A" property="pol" tabindex="5">
           <html:option value="м">муж</html:option>
           <html:option value="ж">жен</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Дата&nbsp;рождения:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dataRojdenija" 
                      maxlength="10" size="10" tabindex="6"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Тип&nbsp;документа:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="tipDokumenta" size="1" maxlength="1" tabindex="7"/>&nbsp;&nbsp;&nbsp;
           серия:&nbsp;<html:text name="abit_A" styleClass="text_f9_short" property="seriaDokumenta" size="4" maxlength="20" tabindex="8"/>&nbsp;&nbsp;
           №:&nbsp;<html:text name="abit_A" styleClass="text_f9_short" property="nomerDokumenta" size="14" maxlength="20" tabindex="9"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Место&nbsp;рождения:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="mestoRojdenija" size="52" maxlength="200" tabindex="10"/>
           </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Кем&nbsp;выдан:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="kemVydDokument" size="52" maxlength="100" tabindex="10"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Дата&nbsp;выдачи:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dataVydDokumenta" size="10" maxlength="10" tabindex="11"/></td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="6" colspan="2"></td></tr>
   <tr><td colspan='2' vAlign="top">

<%-------------------------------------%>
<%-- 2я ТАБЛИЦА (АДРЕСА ПРОПИСКИ)    --%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;2.&nbsp;АДРЕС&nbsp;ПРОПИСКИ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
         <td vAlign="middle" height="18" width="163">&nbsp;&nbsp;Насел.&nbsp;пункт:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="gorod_Prop" size="36" maxlength="60" tabindex="12"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Улица&nbsp;/&nbsp;просп.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="ulica_Prop" size="46" maxlength="60" tabindex="13"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Дом&nbsp;/&nbsp;корпус:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="dom_Prop" size="4" maxlength="6" tabindex="14"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Кварт.&nbsp;/&nbsp;комн.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="kvart_Prop" size="3" maxlength="6" tabindex="15"/>
         </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="6" colspan="2"></td></tr>
   <tr><td colspan="2" vAlign="top">
<%-------------------------------------%>
<%-- 3я ТАБЛИЦА (ОБРАЗОВАНИЕ)        --%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;3.&nbsp;СВЕДЕНИЯ&nbsp;ОБ&nbsp;ОБРАЗОВАНИИ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Год&nbsp;получения&nbsp;образования:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9_short" property="godOkonchanijaSrObrazovanija" size="4" maxlength="4" tabindex="16"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Где&nbsp;получил&nbsp;образование:&nbsp;&nbsp;</td>
         <td valign=center>
           <html:select styleClass="select_f2" onchange="autoInit()" name="abit_A" property="gdePoluchilSrObrazovanie" tabindex="17">
            <html:option value="п" />
            <html:option value="г" />
            <html:option value="с" />
            <html:option value="о" />
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Населенный&nbsp;пункт:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="nazvanie" size="36" maxlength="150" tabindex="18"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Район:&nbsp;&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="nazvanieRajona" size="36" maxlength="60" tabindex="19"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Область&nbsp;или&nbsp;республика:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="abit_A" styleClass="text_f9" property="nazvanieOblasti" size="36" maxlength="60" tabindex="20"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Тип&nbsp;оконченного&nbsp;заведения:&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" onchange="autoInit4();" name="abit_A" property="tipOkonchennogoZavedenija" tabindex="21">
            <html:option value="ш">школа</html:option>
            <html:option value="в">вечерняя школа</html:option>
            <html:option value="п">пту</html:option>
            <html:option value="т">техникум</html:option>
            <html:option value="у">вуз</html:option>
           </html:select>&nbsp;&nbsp;&nbsp;<font class="text_9">№:</font>
           <html:text name="abit_A" styleClass="text_f9_short" property="nomerShkoly" size="4" maxlength="5" tabindex="22"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Название&nbsp;уч.&nbsp;заведения:&nbsp;&nbsp;</td>
         <td valign=center>
           <html:select styleClass="select_f2" name="abit_A" property="kodZavedenija" tabindex="23">
            <html:options collection="abit_A_S8" property="kodZavedenija" labelProperty="sokr" />
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Другое&nbsp;название&nbsp;уч.&nbsp;заведен.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text styleClass="text_f9" name="abit_A" property="polnoeNaimenovanieZavedenija" maxlength="250" size='36' tabindex="24"/></td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Вид&nbsp;документа&nbsp;об&nbsp;образ.:&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="vidDokSredObraz" tabindex="25">
            <html:option value="Аттестат">аттестат</html:option>
            <html:option value="Диплом ВПО">диплом ВПО</html:option>
            <html:option value="Диплом СПО">диплом СПО</html:option>
            <html:option value="Диплом НПО">диплом НПО</html:option>
            <html:option value="Диплом НВПО">диплом НВПО</html:option>
            <html:option value="Ак. справка">ак. справка</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Документ&nbsp;об&nbsp;образ.&nbsp;(признак):&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:select styleClass="select_f2" name="abit_A" property="tipDokSredObraz" tabindex="26">
            <html:option value="о">оригинал</html:option>
            <html:option value="к">копия</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Аттестат&nbsp;/&nbsp;Диплом&nbsp;(серия,&nbsp;№):&nbsp;&nbsp;</td>
         <td vAlign="middle" height="18">
             <html:text styleClass="text_f9_short" name="abit_A" property="seriaAtt"
                        maxlength="10" size="6" tabindex="27"/>
             <html:text styleClass="text_f9" name="abit_A" property="nomerAtt"
                        maxlength="20" size="15" tabindex="28"/>
         </td>
     </tr>
<!--      <tr> -->
<!--          <td valign="middle" width="220">&nbsp;&nbsp;Сертификат&nbsp;ЕГЭ&nbsp;(признак):&nbsp;</td> -->
<!--          <td valign="middle"> -->
<%--              <html:select styleClass="select_f2" name="abit_A" property="kopijaSertifikata" tabindex="29"> --%>
<%--               <html:option value="о">оригинал</html:option> --%>
<%--               <html:option value="к">копия</html:option> --%>
<%--              </html:select> --%>
<!--          </td> -->
<!--     </tr> -->
<!--     <tr> -->
<!--          <td valign=center>&nbsp;&nbsp;Номер&nbsp;сертификата&nbsp;ЕГЭ:&nbsp;</td> -->
<!--          <td valign=center> -->
<%--              <html:text styleClass="text_f9" name="abit_A" property="nomerSertifikata"  --%>
<%--                         maxlength="250" size="30" tabindex="30"/> --%>
<!--          </td> -->
<!--     </tr> -->
    </tbody>
   </table>
   </td></tr>
 </table>
</td><td>&nbsp;&nbsp;</td><td vAlign="top">

<%-------------------------------------%>
<%-- ПРАВАЯ ПОЛОВИНКА КАРТОЧКИ ВВОДА  --%>
<%-------------------------------------%>

 <table width="50%" height="100%" border="0" cellSpacing="0" cellPadding="0" class="text">
   <tr><td vAlign="top">

<%------------------------------------------%>
<%-- 4я ТАБЛИЦА (ЛЬГОТЫ ОТЛИЧИЯ ЦЕЛЕВИКИ) --%>
<%------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;4.&nbsp;ЛЬГОТЫ,&nbsp;ОТЛИЧИЯ,&nbsp;ЦЕЛЕВОЙ&nbsp;ПРИЁМ&nbsp;</font></td>
         <td width="262" rowspan="2">&nbsp;</td>
     </tr>
    </thead>
    <tbody class="dark">
    <tr>
        <td valign=center>&nbsp;&nbsp;Мед.&nbsp;справка&nbsp;&nbsp;&nbsp;&nbsp;
         <html:select styleClass="select_f2" name="abit_A" property="podtverjdenieMedSpravki" tabindex="38">
            <html:option value="n">нет</html:option>
            <html:option value="d">да</html:option>
        </html:select>
        </td>
     <td valign=center width="100%">
            &nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text styleClass="text_f9" name="abit_A" property="medSpravka" maxlength="250" size="58" tabindex="34"/>
         </td>
        
         <td rowspan="6" align='center' valign='middle'><img src="layouts/1/img/add_card/add_card.jpg" border="0" width="232" height="140"></td>
    </tr>
    <tr>
         <td valign=center colspan='2'>&nbsp;&nbsp;Особые&nbsp;права:&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="kodLgot" tabindex="32">
            <html:options collection="abit_A_S4" property="kodLgot" labelProperty="shifrLgot"/>
          </html:select>
          &nbsp;-&nbsp;&nbsp;<html:text styleClass="text_f9" name="abit_A" property="udostoverenieLgoty" maxlength="250" size="58" tabindex="33"/>
         </td>
     </tr>
     <tr>
         <td vAlign="middle" colspan='2'>&nbsp;&nbsp;Индивид.&nbsp;достижения:&nbsp;&nbsp;
          <html:select styleClass="select_f2" name="abit_A" property="kodMedali" tabindex="34">
            <html:options collection="abit_A_S6" property="kodMedali" labelProperty="shifrMedali" />
          </html:select>
<%--           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;<html:text styleClass="text_f9" name="abit_A" property="diplomOtlichija" maxlength="250" size="30" tabindex="35"/> --%>
         &nbsp;&nbsp;Преимущ.&nbsp;право:&nbsp;&nbsp;&nbsp;&nbsp;
        <html:select styleClass="select_f2" name="abit_A" property="preemptiveRight" tabindex="38">
            <html:option value="н">нет</html:option>
            <html:option value="д">да</html:option>
        </html:select>
          
         </td>
     </tr>
     <tr>
         <td valign=center>&nbsp;&nbsp;Иностранный&nbsp;язык:&nbsp;</td>
         <td valign=center>
           <html:select styleClass="select_f2" name="abit_A" property="inostrannyjJazyk" tabindex="36">
            <html:option value="а">английский</html:option>
            <html:option value="н">немецкий</html:option>
            <html:option value="ф">французский</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td valign=center>&nbsp;&nbsp;Направлен.&nbsp;от&nbsp;предпр.:&nbsp;</td>
         <td valign=center>
           <html:select styleClass="select_f2" name="abit_A" property="napravlenieOtPredprijatija" tabindex="37">
            <html:option value="н">нет</html:option>
            <html:option value="д">да</html:option>
           </html:select>
         </td>
     </tr>
     <tr>
         <td valign=center>&nbsp;&nbsp;Целевой&nbsp;приём:&nbsp;</td>
         <td valign=center>
           <html:select styleClass="select_f2" name="abit_A" property="kodTselevogoPriema" tabindex="38">
             <html:options collection="abit_A_S9" property="kodTselevogoPriema" labelProperty="shifrPriema"/>
           </html:select>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Группа:&nbsp;&nbsp;
           <html:text styleClass="text_f9_short" name="abit_A" property="gruppa" maxlength="10" size="6" tabindex="39"/>
         </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="12" colspan="2"></td></tr>
   <tr><td vAlign="top">

<%------------------------------------------%>
<%-- 5я ТАБЛИЦА (БАЛЛЫ ЕГЭ)               --%>
<%------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;5.&nbsp;ИНФОРМАЦИЯ&nbsp;О&nbsp;БАЛЛАХ&nbsp;ПО&nbsp;КОНКУРСНЫМ&nbsp;ПРЕДМЕТАМ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
    <tr>
<%--------------------- Подтаблица  оценок ------------------------%>
<%------------ Оценки аттестата и заявленные оценки ---------------%>
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
           <td align="left" valign="middle">&nbsp;&nbsp;Зачесть&nbsp;следующие&nbsp;баллы&nbsp;ЕГЭ:&nbsp;</td>
           <logic:iterate id="abit_A1" name="abit_A_S1" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kP" name="abit_A1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Ege_note\" + kP%>" value="<%=abit_A1.getEge()%>"
                       maxlength="3" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>
             </td>
           </logic:iterate>
         </tr>
         <tr>
           <td align="left" valign="middle">&nbsp;&nbsp;Допустить&nbsp;к&nbsp;сдаче&nbsp;экзаменов:&nbsp;</td>
           <logic:iterate id="abit_A1" name="abit_A_S1" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kP" name="abit_A1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Examen\" + kP%>" value="<%=abit_A1.getExamen()%>"
                       maxlength="1" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>
             </td>
           </logic:iterate>
         </tr>
         <tr>
           <td align="center" valign="middle" colspan="25" height="30">&nbsp;("е" - экзамен сдаётся в формате ЕГЭ, "в" - экзамен сдаётся в формате вуза)&nbsp;
           </td>
         </tr>
         </tbody>
        </table>
      </td>
    </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="44" colspan="2" align="center" valign="middle">
        <input type="button" onclick="constructLink();" value="Посмотреть баллы ЕГЭ в ФБС" class="button" tabindex="<%=Integer.toString(tabindex++)%>">
       </td>
   </tr>
   <tr><td vAlign="top">


<%------------------------------------------------------------%>
<%-- 6я ТАБЛИЦА (СПЕЦИАЛЬНОСТИ ПРИЕМА В ПОРЯДКЕ ПРИОРИТЕТА) --%>
<%------------------------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="3" height="28" vAlign="middle"><font class="text_th9">&nbsp;6.&nbsp;ДОПУСТИТЬ&nbsp;К&nbsp;УЧАСТИЮ&nbsp;В&nbsp;КОНКУРСЕ&nbsp;ПО&nbsp;НАПРАВЛЕНИЯМ&nbsp;(СПЕЦИАЛЬНОСТЯМ):&nbsp;&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
       <td align="center" valign="middle" height="190">
         <table border="1" align="center" cellSpacing="0" cellPadding="0" frame="BOX" class="text">
          <thead>
           <tr>
             <td valign="middle" align="center" height="26"><font class="text_10">&nbsp;&nbsp;№&nbsp;&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;&nbsp;Шифр&nbsp;ОКСО&nbsp;&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;Бюдж.&nbsp;</font></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;Догов.&nbsp;</font></td>
<!--              <td valign="middle" align="center"><font class="text_10">&nbsp;Олимп.&nbsp;</font></td> -->
             <td valign="middle" align="center"><font class="text_10">&nbsp;Целев.&nbsp;</font></td>
<!--              <td valign="middle" align="center"><font class="text_10">&nbsp;Фито&nbsp;</font></td> -->
<!--              <td valign="middle" align="center"><font class="text_10">&nbsp;&nbsp;Полн.&nbsp;лет&nbsp;&nbsp;</font></td> -->
<!--              <td valign="middle" align="center"><font class="text_10">&nbsp;Сокр.&nbsp;</font></td> -->
              <logic:equal name="userGroupName" value="Операторы">
             <td valign="middle" align="center" width="4"></td>
             <td valign="middle" align="center"><font class="text_10">&nbsp;Опл.&nbsp;дог.&nbsp;</font></td>
             <td valign="middle" align="center" width="4"></td>
             </logic:equal>
             <td valign="middle" style="display:none" align="center"><font class="text_10">&nbsp;Конкурс&nbsp;</font></td>
           </tr>
          </thead>
          <tbody class="dark">
           <tr>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;1.&nbsp;</td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="s_okso_1" 
                          maxlength="14" size="14" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="olimp_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="target_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="fito_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
              <logic:equal name="userGroupName" value="Операторы">
             <td valign="middle" align="center" bgcolor="#FFD3E2"></td>
             <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2"></td>
             </logic:equal>
             <td valign="middle" style="display:none"  align="center" bgcolor="#FFD3E2">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="konkurs_1" readonly="true" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>

           </tr>
           <tr>
             <td valign="middle" align="center">&nbsp;2.&nbsp;</td>
             <td valign="middle" align="center">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="s_okso_2" 
                          maxlength="14" size="14" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="olimp_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="target_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="fito_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
              <logic:equal name="userGroupName" value="Операторы">
             <td valign="middle" align="center"></td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_2" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2"></td>
             </logic:equal>
             <td valign="middle" style="display:none" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="konkurs_2" readonly="true" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
           </tr>
           <tr>
             <td valign="middle" align="center">&nbsp;3.&nbsp;</td>
             <td valign="middle" align="center">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="s_okso_3" 
                          maxlength="14" size="14" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="olimp_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="target_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="fito_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
              <logic:equal name="userGroupName" value="Операторы">
             <td valign="middle" align="center"></td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_3" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2"></td>
             </logic:equal>
             <td valign="middle" style="display:none" align="center" bgcolor="#FFD3E2">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="konkurs_3" readonly="true" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
           </tr>
           <tr>
             <td valign="middle" align="center">&nbsp;4.&nbsp;</td>
             <td valign="middle" align="center">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="s_okso_4" 
                          maxlength="14" size="14" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="olimp_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="target_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="fito_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
              <logic:equal name="userGroupName" value="Операторы">
             <td valign="middle" align="center"></td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_4" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2"></td>
             </logic:equal>
             <td valign="middle" style="display:none"  align="center" bgcolor="#FFD3E2">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="konkurs_4" readonly="true" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
           </tr>
           <tr>
             <td valign="middle" align="center">&nbsp;5.&nbsp;</td>
             <td valign="middle" align="center">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="s_okso_5" 
                          maxlength="14" size="14" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="olimp_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="target_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="fito_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
              <logic:equal name="userGroupName" value="Операторы">
             <td valign="middle" align="center"></td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_5" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2"></td>
             </logic:equal>
             <td valign="middle" style="display:none"  align="center" bgcolor="#FFD3E2">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="konkurs_5" readonly="true" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
           </tr>
           <tr>
             <td valign="middle" align="center">&nbsp;6.&nbsp;</td>
             <td valign="middle" align="center">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="s_okso_6" 
                          maxlength="14" size="14" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="bud_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center" bgcolor="#FFD3E2">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="olimp_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="target_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="fito_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="six_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
<!--              <td valign="middle" align="center">&nbsp; -->
<%--                <html:checkbox styleClass="checkbox_1" name="abit_A" property="three_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; --%>
<!--              </td> -->
              <logic:equal name="userGroupName" value="Операторы">
             <td valign="middle" align="center"></td>
             <td valign="middle" align="center">&nbsp;
               <html:checkbox styleClass="checkbox_1" name="abit_A" property="dog_ok_6" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
             <td valign="middle" align="center" bgcolor="#FFD3E2"></td>
             </logic:equal>
             <td valign="middle" style="display:none"  align="center" bgcolor="#FFD3E2">&nbsp;
               <html:text styleClass="text_f9_short" name="abit_A" property="konkurs_6" readonly="true" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
             </td>
           </tr>
          </tbody>
         </table>
       </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   <tr><td height="12" colspan="2"></td></tr>
   <tr><td vAlign="top">


<%-------------------------------------%>
<%-- 7я ТАБЛИЦА (ПРОЧЕЕ)             --%>
<%-------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="4" height="27" vAlign="middle"><font class="text_th9">&nbsp;7.&nbsp;ДОПОЛНИТЕЛЬНЫЕ&nbsp;СВЕДЕНИЯ&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
     <tr>
       <td align="center" width="300">
         <table border="1" cellSpacing="0" cellPadding="0" frame="VOID" class="text" width="300">
          <tbody class="dark">
           <tr>
               <td valign="middle">&nbsp;Документы&nbsp;хранятся:&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="dokumentyHranjatsja" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="д">да</html:option>
                  <html:option value="н">нет</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign="middle">&nbsp;Номер&nbsp;платн.&nbsp;дог.:&nbsp;</td>
               <td valign="middle" align="center">
                <html:text styleClass="text_f9_short" property="nomerPlatnogoDogovora" name="abit_A"
                           maxlength="20" size="15" tabindex="<%=Integer.toString(tabindex++)%>"/>
               </td>
           </tr>
           <tr>
               <td valign="middle">&nbsp;Желает&nbsp;степень&nbsp;магистра:&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="tip_Spec" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="о">нет</html:option>
                  <html:option value="м">да</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign="middle">&nbsp;Желает&nbsp;получить&nbsp;СПО:&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="need_Spo" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="н">нет</html:option>
                  <html:option value="д">да</html:option>
                 </html:select>
               </td>
           </tr>
                          <tr>
               <td valign=center>&nbsp;Обеспечение&nbsp;специальных&nbsp;условий:&nbsp;&nbsp;</td>
              <td valign=center>
                 <html:text styleClass="text_f9_short" name="abit_A" property="providingSpecialCondition" 
                           maxlength="50" size="30" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; 
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Способ&nbsp;возврата&nbsp;документов:&nbsp;</td>
				<td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="returnDocument" tabindex="<%=Integer.toString(tabindex++)%>">
                   <html:option value="лично поступающим или доверенным лицом">лично поступающим или доверенным лицом</html:option>
                  <html:option value="через оператор почтовой связи">через оператор почтовой связи</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Аспирантура:&nbsp;</td>
				<td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="postgraduateStudies" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="н">нет</html:option>
                  <html:option value="д">да</html:option>
                 </html:select>
               </td>
           </tr>
            <tr>
               <td valign=center>&nbsp;Ординатура:&nbsp;</td>
				<td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="traineeship" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="н">нет</html:option>
                  <html:option value="д">да</html:option>
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
               <td valign="middle">&nbsp;Имеет&nbsp;высшее&nbsp;образование:&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="stepen_Mag" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="н">нет</html:option>
                  <html:option value="д">да</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Трудовая&nbsp;деятельность:&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="trudovajaDejatelnost" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="н">не работал</html:option>
                  <html:option value="д">да, имеется тр. стаж</html:option>
                 </html:select>
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Потребность&nbsp;в&nbsp;общежитии:&nbsp;&nbsp;</td>
               <td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="nujdaetsjaVObschejitii" tabindex="<%=Integer.toString(tabindex++)%>">
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
                            maxlength="50" size="30" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Email:&nbsp;&nbsp;</td>
              <td valign=center>
                 <html:text styleClass="text_f9_short" name="abit_A" property="abitEmail" 
                           maxlength="50" size="30" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; 
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Почтовый&nbsp;адрес:&nbsp;</td>
               <td valign=center>
                 <html:text styleClass="text_f9_short" name="abit_A" property="dopAddress" 
                            maxlength="50" size="30" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp; 
               </td>
           </tr>
           <tr>
               <td valign=center>&nbsp;Интернатура:&nbsp;</td>
				<td valign="middle" width="100%">
                 <html:select styleClass="select_f2" name="abit_A" property="internship" tabindex="<%=Integer.toString(tabindex++)%>">
                  <html:option value="н">нет</html:option>
                  <html:option value="д">да</html:option>
                 </html:select>
               </td>
           </tr>
			<td valign="middle" height="20px">
			</td>
			<td valign="middle" height="20px">
			</td>
          </tbody>
         </table>
       </td>
     </tr>
    </tbody>
   </table>
   </td></tr>
   
   <%------------------------------------------%>
<%-- 8я ТАБЛИЦА (БАЛЛЫ АТТЕСТАТА)               --%>
<%------------------------------------------%>
   <table border="1" cellSpacing="0" cellPadding="0" FRAME="BOX" class="text" width="100%">
    <thead>
     <tr>
         <td align="center" colspan="2" height="22" vAlign="middle"><font class="text_th9">&nbsp;8.&nbsp;ИНФОРМАЦИЯ&nbsp;О&nbsp;БАЛЛАХ&nbsp;АТТЕСТАТА&nbsp;</font></td>
     </tr>
    </thead>
    <tbody class="dark">
    <tr>
<%--------------------- Подтаблица  оценок ------------------------%>
<%------------ Оценки аттестата---------------%>
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
           <td align="left" valign="middle">&nbsp;&nbsp;Зачесть&nbsp;следующие&nbsp;баллы&nbsp;аттестата&nbsp;</td>
           <logic:iterate id="abit1" name="att" scope='request' type="abit.bean.AbiturientBean">
             <bean:define id="kA" name="abit1" property="kodPredmeta"/>
             <td valign=center align=center>
              <html:text styleClass="text_f9_short" property="<%=\"Attestat\" + kA%>" value="<%=abit1.getEge()%>"
                       maxlength="3" size="1" tabindex="<%=Integer.toString(tabindex++)%>"/>
             </td>
           </logic:iterate>
         </tr>
         <tr>
         <td vAlign="middle" height="18">&nbsp;&nbsp;Средний&nbsp;балл&nbsp;</td>
         <td vAlign="middle" height="18">
           <html:text name="sb" styleClass="text_f9" property="familija" size="1" maxlength="3" tabindex="1"/>
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
   </td></tr>

   <tr><td colspan="6" vAlign="bottom" height="10" align="center">
     <table align="center" border="0">
       <tr align="center">
         <td><html:submit styleClass="button" value="Импортировать" property="mod" tabindex="<%=Integer.toString(tabindex++)%>"/>&nbsp;&nbsp;</td>
           </html:form>
      
      
       <html:form action="/abiturient.do">
         <td><html:submit styleClass="button" value="Выход" tabindex="<%=Integer.toString(tabindex++)%>" property="exit"/></td>
       </html:form>
       </tr>
     </table>
    </td></tr>
</table>
</template:put>
</template:insert>
</logic:equal>


<%-----------------------------------------------------------------%>
<%---------------------   УСПЕШНОЕ ИЗМЕНЕНИЕ   --------------------%>
<%-----------------------------------------------------------------%>
<logic:equal name="action" value="mod_success">
<template:insert template="<%="layouts/"+tema+"/lay/layout_tiny.jsp"%>">
<template:put name="title">Карточка абитуриента успешно модифицирована</template:put>
<template:put name="target_name">Карточка абитуриента модифицирована успешно</template:put>
<template:put name="content">

<html:form action="/abit_doc.do?action=reports">
<html:hidden name="abit_A" property="kodAbiturienta"/>
<html:hidden name="abit_A" property="tip_Spec"/>
<table width="100%" border="0" cellSpacing="0" cellPadding="0">
<tbody>
 <tr>
   <td align="center" valign="middle"><font class="text_10">&nbsp;Изменения&nbsp;сведений&nbsp;об&nbsp;абитуриенте:&nbsp;</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_th">&nbsp;<bean:write name="abit_A" property="familija"/>&nbsp;<bean:write name="abit_A" property="imja"/>&nbsp;<bean:write name="abit_A" property="otchestvo"/>&nbsp;</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">&nbsp;успешно&nbsp;внесены&nbsp;в&nbsp;базу&nbsp;данных!&nbsp;</font></td>
 </tr>
 <tr><td height="20"></td></tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">Чтобы сформировать пакет документов для печати необходимо</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">нажать кнопку ''Сформировать пакет документов''.</font></td>
 </tr>
 <tr>
   <td align="center" valign="middle"><font class="text_10">Для выхода в меню нажмите кнопку ''Выход''.</font></td>
 </tr>
 <tr><td height="20"></td></tr>
</tbody>
</table>
<table align="center" border="0">
  <tr align="center">
    <td><html:submit styleClass="button" value="Сформировать пакет документов" tabindex="1"/>&nbsp;&nbsp;</td>
</html:form>
<html:form action="/abit_srch.do">
    <td><html:submit styleClass="button" value="Поиск карточки" tabindex="2"/>&nbsp;&nbsp;</td>
    <td><html:submit styleClass="button" value="Выход" tabindex="3" property="exit"/></td>
  </tr>
</table>
</html:form>
</template:put>
</template:insert>
</logic:equal>
</logic:present>