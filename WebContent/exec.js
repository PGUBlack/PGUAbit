function checkFields(){
  var valid2 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-.,()"
  var valid3 = "пзис"
  var valid4 = "1234567890оздмфвуюп."
	  var valid5 = "1234567890в+"
  var valid9 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ1234567890-.,()"
  var valid99 = "1234567890-"
  var validpas = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
  var temp 
var result


var pasp=validate_id($("[name = 'nomerDokumenta']").val());
  if(pasp==1){
	  
	  alert("Абитуриент с такими паспортными данными уже подавал заявление на выбранный уровень образования!");
	  document.forms(0).nomerDokumenta.focus()
	  return false
  }


  


var rs=validate_spec($("[name = 'special2']").val());
  if(rs==1 && eval("document.forms(0).bud_1.checked") == true){

	  alert("Специальность "+$("[name = 'special2'] option:selected").text()+" не предусматривает бюджетные места.");
		  rs=0;
		  document.forms(0).special2.focus()
		  return false
  }
  rs=validate_spec($("[name = 'special3']").val());
  if(rs==1 && eval("document.forms(0).bud_2.checked") == true){
	 
		  alert("Специальность "+$("[name = 'special3'] option:selected").text()+" не предусматривает бюджетные места.");
		  rs=0;
		  document.forms(0).special3.focus()
		  return false
  }
  rs=validate_spec($("[name = 'special4']").val());
  if(rs==1 && eval("document.forms(0).bud_3.checked") == true){

	  alert("Специальность "+$("[name = 'special4'] option:selected").text()+" не предусматривает бюджетные места.");
		  rs=0;
		  document.forms(0).special4.focus()
		  return false
  }
  rs=validate_spec($("[name = 'special5']").val());
  if(rs==1 && eval("document.forms(0).bud_4.checked") == true){
	
	  alert("Специальность "+$("[name = 'special5'] option:selected").text()+" не предусматривает бюджетные места.");
		  rs=0;
		  document.forms(0).special5.focus()
		  return false
  }
  rs=validate_spec($("[name = 'special6']").val());
  if(rs==1 && eval("document.forms(0).bud_5.checked") == true){
	  alert("Специальность "+$("[name = 'special6'] option:selected").text()+" не предусматривает бюджетные места.");
		  rs=0;
		  document.forms(0).special6.focus()
		  return false
  }
  rs=validate_spec($("[name = 'special7']").val());
  if(rs==1 && eval("document.forms(0).bud_6.checked") == true){
	  alert("Специальность "+$("[name = 'special7'] option:selected").text()+" не предусматривает бюджетные места.");
		  rs=0;
		  document.forms(0).special7.focus()
		  return false
  }
//------ 1. ?????? ??????------------------------------------------------------------
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
//-------------------------------------------------------------------------------------------------
if(!(document.forms(0).seriaDokumenta.value.length == 0)){
for (var i=0; i<document.forms(0).seriaDokumenta.value.length; i++){
	  temp = "" + document.forms(0).seriaDokumenta.value.substring(i, i+1)
	  if (validpas.indexOf(temp) == "-1") {
	    alert("Поле ''Серия документа'' может состоять только из латинских букв или цифр")
	    document.forms(0).seriaDokumenta.focus()
	    return false
	  }
}
}
//-------------------------------------------------------------------------------------------------
if(document.forms(0).nomerDokumenta.value.length == 0){
  alert("Необходимо заполнить ''Номер паспорта''")
  document.forms(0).nomerDokumenta.focus()
  return false
}
  for (var i=0; i<document.forms(0).nomerDokumenta.value.length; i++){
  temp = "" + document.forms(0).nomerDokumenta.value.substring(i, i+1)
  if (validpas.indexOf(temp) == "-1") {
    alert("Поле ''Номер документа'' может состоять только из латинских букв или цифр")
    document.forms(0).nomerDokumenta.focus()
    return false
  }
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
//------ 2. АДРЕС ПРОПИСКИ------------------------------------------------------




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
//------ 3. СВЕДЕНИЯ ОБ ОБРАЗОВАНИИ---------------------------------------------
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

//------------------------------------------------------------------------------------------------
if(document.forms(0).vidDokSredObraz.value == "-"){
  alert("Необходимо выбрать значение поля ''Вид документа о полученном образовании''")
  document.forms(0).vidDokSredObraz.focus()
  return false
}

//------------------------------------------------------------------------------------------------
if(document.forms(0).tipDokSredObraz.value == "-"){
  alert("Необходимо заполнить поле ''Документ о полученном образовании (копия)''")
  document.forms(0).tipDokSredObraz.focus()
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
} while( offset != -1 )
}


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
	  tmp = eval("document.forms(0).Examen"+result+".value");
	  for (var i=0; i<tmp.length; i++){
	    tmp2 = "" + eval("document.forms(0).Examen"+result+".value.substring(i, i+1)");
	    if(valid5.indexOf(tmp2) == "-1") {
	      alert("Поле признака сдачи экзамена в формате ВУЗа может содержать только цифры, ''+'' или ''в''")
	      eval("document.forms(0).Examen"+result+".focus()")
	      return false
	    }
	  }
	  
	  if ((tmp > 100 || tmp < 0)&&(tmp!="в" || tmp!="+")) {
	     alert("Оценка экзамена, сданного в ВУЗе должна лежать в диапазоне: от ''0'' до ''100''")
	     eval("document.forms(0).Examen"+result+".focus()")
	     return false
	  }
	} while( offset != -1 )
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
if(document.forms(0).kodFakulteta.value == "-" && document.forms(0).s_okso_2.value == "-" && document.forms(0).s_okso_3.value == "-" && document.forms(0).s_okso_4.value == "-" && document.forms(0).s_okso_5.value == "-" && document.forms(0).s_okso_6.value == "-"){
	   alert("Необходимо выбрать Факультет")
	   document.forms(0).kodFakulteta.focus()
	   return false
}



  return true;
}


function checkFields1(){
	  var valid2 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-.,()"
	  var valid3 = "пзис"
	  var valid4 = "1234567890оздмфвуюп."
	  var valid9 = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ1234567890-.,()"
	  var valid99 = "1234567890-"
	  var temp 
	var result




	  


	var rs=validate_spec($("[name = 'special2']").val());
	  if(rs==1 && eval("document.forms(0).bud_1.checked") == true){

		  alert("Специальность "+$("[name = 'special2'] option:selected").text()+" не предусматривает бюджетные места.");
			  rs=0;
			  document.forms(0).special2.focus()
			  return false
	  }
	  rs=validate_spec($("[name = 'special3']").val());
	  if(rs==1 && eval("document.forms(0).bud_2.checked") == true){
		 
			  alert("Специальность "+$("[name = 'special3'] option:selected").text()+" не предусматривает бюджетные места.");
			  rs=0;
			  document.forms(0).special3.focus()
			  return false
	  }
	  rs=validate_spec($("[name = 'special4']").val());
	  if(rs==1 && eval("document.forms(0).bud_3.checked") == true){

		  alert("Специальность "+$("[name = 'special4'] option:selected").text()+" не предусматривает бюджетные места.");
			  rs=0;
			  document.forms(0).special4.focus()
			  return false
	  }
	  rs=validate_spec($("[name = 'special5']").val());
	  if(rs==1 && eval("document.forms(0).bud_4.checked") == true){
		
		  alert("Специальность "+$("[name = 'special5'] option:selected").text()+" не предусматривает бюджетные места.");
			  rs=0;
			  document.forms(0).special5.focus()
			  return false
	  }
	  rs=validate_spec($("[name = 'special6']").val());
	  if(rs==1 && eval("document.forms(0).bud_5.checked") == true){
		  alert("Специальность "+$("[name = 'special6'] option:selected").text()+" не предусматривает бюджетные места.");
			  rs=0;
			  document.forms(0).special6.focus()
			  return false
	  }
	  rs=validate_spec($("[name = 'special7']").val());
	  if(rs==1 && eval("document.forms(0).bud_6.checked") == true){
		  alert("Специальность "+$("[name = 'special7'] option:selected").text()+" не предусматривает бюджетные места.");
			  rs=0;
			  document.forms(0).special7.focus()
			  return false
	  }
	//------ 1. ?????? ??????------------------------------------------------------------
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
	//------ 2. АДРЕС ПРОПИСКИ------------------------------------------------------




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
	//------ 3. СВЕДЕНИЯ ОБ ОБРАЗОВАНИИ---------------------------------------------
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

	//------------------------------------------------------------------------------------------------
	if(document.forms(0).vidDokSredObraz.value == "-"){
	  alert("Необходимо выбрать значение поля ''Вид документа о полученном образовании''")
	  document.forms(0).vidDokSredObraz.focus()
	  return false
	}

	//------------------------------------------------------------------------------------------------
	if(document.forms(0).tipDokSredObraz.value == "-"){
	  alert("Необходимо заполнить поле ''Документ о полученном образовании (копия)''")
	  document.forms(0).tipDokSredObraz.focus()
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
	} while( offset != -1 )
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
	if(document.forms(0).kodFakulteta.value == "-" && document.forms(0).s_okso_2.value == "-" && document.forms(0).s_okso_3.value == "-" && document.forms(0).s_okso_4.value == "-" && document.forms(0).s_okso_5.value == "-" && document.forms(0).s_okso_6.value == "-"){
		   alert("Необходимо выбрать Факультет")
		   document.forms(0).kodFakulteta.focus()
		   return false
	}



	  return true;
	}






















function exec() {
	      
			document.forms(0).kodFakulteta.selectedIndex = 0;
			document.forms(0).s_okso_2.selectedIndex = 0;
			document.forms(0).s_okso_3.selectedIndex = 0;
			document.forms(0).s_okso_4.selectedIndex = 0;
			document.forms(0).s_okso_5.selectedIndex = 0;
			document.forms(0).s_okso_6.selectedIndex = 0;
			
			$('#foreignPunkt').toggle(false);
			
			teamLength = document.forms(0).special2.options.length
			teamTXT1 = new Array(teamLength)
			teamVAL1 = new Array(teamLength)
			for(var ind=0;ind<document.forms(0).special2.length;ind++) {
				   eval("teamTXT1["+ind+"]=document.forms(0).special2.options["+ind+"].text")
				   eval("teamVAL1["+ind+"]=document.forms(0).special2.options["+ind+"].value")
				}
			
			var teamLength2 = document.forms(0).special5.options.length
			 teamTXT2 = new Array(teamLength2)
			 teamVAL2 = new Array(teamLength2)
			 for(var ind=0;ind<document.forms(0).special5.length;ind++) {
			     eval("teamTXT2["+ind+"]=document.forms(0).special5.options["+ind+"].text")
			     eval("teamVAL2["+ind+"]=document.forms(0).special5.options["+ind+"].value")
			  }
			
			fillSelect(document.forms(0).kodFakulteta.value);
			fillSelect2(document.forms(0).s_okso_2.value);
			fillSelect3(document.forms(0).s_okso_3.value);
			fillSelect4(document.forms(0).s_okso_4.value);
			fillSelect5(document.forms(0).s_okso_5.value);
			fillSelect6(document.forms(0).s_okso_6.value);
			document.forms(0).kodFakulteta.focus();
		setCurrentYear();
		/*   
		 document.forms(0).stob.checked = false;
		 document.forms(0).pr1.checked = false;
		 document.forms(0).pr2.checked = false;
		 document.forms(0).pr3.checked = false;
		 */
		document.forms(0).pol.selectedIndex = 0
		document.forms(0).tipOkonchennogoZavedenija.selectedIndex = 0
		document.forms(0).kodZavedenija.selectedIndex = 0
		document.forms(0).vidDokSredObraz.selectedIndex = 0
		document.forms(0).tipDokSredObraz.selectedIndex = 0
		//  document.forms(0).kopijaSertifikata.selectedIndex=0
		document.forms(0).inostrannyjJazyk.selectedIndex = 0
		document.forms(0).nujdaetsjaVObschejitii.selectedIndex = 0
		document.forms(0).familija.focus();
		// document.forms(0).polnoeNaimenovanieZavedenija.disabled = true;
	}
	function fillSelect(selectCtrl) {
		var i, j = 0
		// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ
		for (i = document.forms(0).special2.length; i >= 0; i--) {
			document.forms(0).special2.options[i] = null
		}
		if (selectCtrl == "-") {
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ "-"
			document.forms(0).special2.options[0] = new Option("-")
			document.forms(0).special2.options[0].value = "-"
			document.forms(0).special2.options[0].selected = true
			return
		}
		j=0
		// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ
		for(i = 0; i < teamLength; i++) {
   if(teamVAL1[i].substring(teamVAL1[i].indexOf("+")+1,teamVAL1[i].indexOf("%")) == selectCtrl) {
     document.forms(0).special2.options[j] = new Option(teamTXT1[i])
     document.forms(0).special2.options[j].value = teamVAL1[i]
     j++
   }
}
		// пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ
		document.forms(0).special2.options[0].selected = true
	}


	function exec2() {
		document.forms(0).familija.focus();
	}

	function validate_spec(x) {
		var action = "5";
		var code = x;
		
		  return $.ajax({
			 	type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action},
		        async: false
		    }).responseText;

	}
	
	function validate_id(x) {
		var action = $("[name = 'nomerPotoka']").val() + "6";
		var code = x;
		
		  return $.ajax({
			 	type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action},
		        async: false
		    }).responseText;

	}
	
	
	
	
	
	function stranaChange(){
		var code = $("[name = 'nazv_DipBak'] option:selected").val();
		if (code != "Российская Федерация"){		
			$("[name = 'gorod_Prop']").val('-');
			$("[name = 'gorod_Prop']").toggle(false);
			$("[name = 'need_Spo']").val('-');
			$("[name = 'need_Spo']").toggle(false);
			$("[name = 'nazv_DipSpec']").toggle(false);
			$("[name = 'nazv_DipSpec']").val('-');
			$('#foreignPunkt').toggle(true);
			$('#foreignPunkt').val('');
		}
		if (code == "Российская Федерация"){		
			$("[name = 'nazv_DipSpec']").toggle(true);
			$("[name = 'need_Spo']").toggle(true);
			$("[name = 'gorod_Prop']").toggle(true);
			$('#foreignPunkt').toggle(false);
		}
	}

	function stranaObrChange(){
		var code = $("[name = 'traineeship'] option:selected").val();
		if (code != "Российская Федерация"){		
			$("[name = 'nazvanie']").toggle(false);
			$("[name = 'nazvanieRajona']").toggle(false);
			$("[name = 'nazvanieOblasti']").toggle(false);
			$("[name = 'nazvanieOblasti']").val('-');
			$('#foreignObrPunkt').toggle(true);
			$('#foreignObrPunkt').val('');
		}
		if (code == "Российская Федерация"){		
			$("[name = 'nazvanieOblasti']").toggle(true);
			$("[name = 'nazvanieRajona']").toggle(true);
			$("[name = 'nazvanie']").toggle(true);
			$('#foreignObrPunkt').toggle(false);
		}
		
	}
	function regionChange() {
		
		var code = $("[name = 'nazv_DipSpec'] option:selected").val();
		var action = "1"
			  $.ajax({
		        type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action},
		        success: function(response){
				//$("[name = 'rajon_KLADR']").remove();
				//$('#need_Spo').html(response);
		        	
		        	var resString1 = response.substring(0,response.indexOf("@"));
		        	var resString2 = response.substring(response.indexOf("@")+1);
		        	
		        	
		        $("[name = 'need_Spo']").html(resString1);
		       
		    	$("[name = 'gorod_Prop']").html(resString2);
				 },
		        error: function(e){
		            alert('Error: ' + e);
		        }
	      
		    });
	//	$("[name = 'gorod_Prop']").html('');
		
	
		
		
		}
function fakChange1() {
		
		var code = $("[name = 'kodFakulteta'] option:selected").val();
		var nomerPotoka = $("[name = 'nomerPotoka']").val();
		var action = nomerPotoka + 3;
			  $.ajax({
		        type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action},
		        success: function(response){
				//$("[name = 'rajon_KLADR']").remove();
				//$('#need_Spo').html(response);
		        $("[name = 'special2']").html(response);
				 },
		        error: function(e){
		            alert('Error: ' + e);
		        }
	      
		    });
			

		}
	function rajonChange() {
		var code =  $("[name = 'need_Spo']").val();
		var action = "2"
			  $.ajax({
		        type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action },
		        success: function(response){
					$("[name = 'gorod_Prop']").html(response);
		           
		        },
		        error: function(e){

		            alert('Error: ' + e);

		        }
	       

		    });
			
	};

	function regionEduChange() {
		
//		var sel = document.getElementById("nazvanieOblasti");
		var code =  $("[name = 'nazvanieOblasti']").val();
//		var code = sel.value; 
		var action = "1"
		//	alert(code);	
//		var code =  $("[name = 'nazv_DipSpec']").val();
		//alert(code);
		
			  $.ajax({
		        type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action},
		        success: function(response){
				//$("[name = 'rajon_KLADR']").remove();
				//$('#need_Spo').html(response);
		       // $("[name = 'nazvanieRajona']").html(response);
					//$('#fillTd1').html(response);
		        	var resString1 = response.substring(0,response.indexOf("@"));
		        	var resString2 = response.substring(response.indexOf("@")+1);
		        	
		        	
		        $("[name = 'nazvanieRajona']").html(resString1);
		       
		    	$("[name = 'nazvanie']").html(resString2);

		        },
		        error: function(e){
		            alert('Error: ' + e);
		        }

		       
		    });
		$("[name = 'nazvanie']").html('');
		}
	function rajonEduChange() {
		//alert('Error: ');
		//var sel = document.getElementById("nazvanieRajona");
		
		//var code = sel.value; 
		
		var code =  $("[name = 'nazvanieRajona']").val();
		
		var action = "2"
			  $.ajax({
		        type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action },
		        success: function(response){
					$("[name = 'nazvanie']").html(response);
		           
		        },
		        error: function(e){

		            alert('Error: ' + e);

		        }
	       

		    });
			
	};
	function fakChange2() {
		
		var code = $("[name = 's_okso_2'] option:selected").val();
		var nomerPotoka = $("[name = 'nomerPotoka']").val();
		var action = nomerPotoka + 3;
			  $.ajax({
		        type: "POST",
		        url: "/abiturient/ajaxKladr.do",
		        data: {code: code, action: action},
		        success: function(response){
				//$("[name = 'rajon_KLADR']").remove();
				//$('#need_Spo').html(response);
		        $("[name = 'special3']").html(response);
				 },
		        error: function(e){
		            alert('Error: ' + e);
		        }
	      
		    });
			
		
		};
		function fakChange3() {
			
			var code = $("[name = 's_okso_3'] option:selected").val();
			var nomerPotoka = $("[name = 'nomerPotoka']").val();
			var action = nomerPotoka + 3;
				  $.ajax({
			        type: "POST",
			        url: "/abiturient/ajaxKladr.do",
			        data: {code: code, action: action},
			        success: function(response){
					//$("[name = 'rajon_KLADR']").remove();
					//$('#need_Spo').html(response);
			        $("[name = 'special4']").html(response);
					 },
			        error: function(e){
			            alert('Error: ' + e);
			        }
		      
			    });
				
			
			}
		;
		function fakChange4() {
			
				var code = $("[name = 's_okso_4'] option:selected").val();
				var nomerPotoka = $("[name = 'nomerPotoka']").val();
				var action = nomerPotoka + 4;
					  $.ajax({
				        type: "POST",
				        url: "/abiturient/ajaxKladr.do",
				        data: {code: code, action: action},
				        success: function(response){
						//$("[name = 'rajon_KLADR']").remove();
						//$('#need_Spo').html(response);
				        $("[name = 'special5']").html(response);
						 },
				        error: function(e){
				            alert('Error: ' + e);
				        }
			      
				    });
					
				
				}
		;
		function fakChange5() {
		
				var code = $("[name = 's_okso_5'] option:selected").val();
				
				var nomerPotoka = $("[name = 'nomerPotoka']").val();
				var action = nomerPotoka + 4;
					  $.ajax({
				        type: "POST",
				        url: "/abiturient/ajaxKladr.do",
				        data: {code: code, action: action},
				        success: function(response){
						//$("[name = 'rajon_KLADR']").remove();
						//$('#need_Spo').html(response);
				        $("[name = 'special6']").html(response);
						 },
				        error: function(e){
				            alert('Error: ' + e);
				        }
			      
				    });
					
				
				}
		;
		function fakChange6() {
		
			var code = $("[name = 's_okso_6'] option:selected").val();
			
			var nomerPotoka = $("[name = 'nomerPotoka']").val();
			var action = nomerPotoka + 4;
				  $.ajax({
			        type: "POST",
			        url: "/abiturient/ajaxKladr.do",
			        data: {code: code, action: action},
			        success: function(response){
					//$("[name = 'rajon_KLADR']").remove();
					//$('#need_Spo').html(response);
			        $("[name = 'special7']").html(response);
					 },
			        error: function(e){
			            alert('Error: ' + e);
			        }
		      
			    });
				
			
			};
			
				
				