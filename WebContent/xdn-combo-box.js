logSpan = document.createElement("SPAN");
document.body.appendChild(logSpan);
function log(txt) {
	logSpan.innerHTML += txt;
}

if ( typeof(xdn) == "undefined" ) {
	undef = "undefined";
	xdn = new XdnClass();
}

function sum_of_offset(s,na) {
	var w=0;
	while(s){
		w+=s[na];
		s=s.offsetParent;
	}
	return w
}
function text_selection_class( x, y ) {
	this.start = x;
	this.end   = y;
	log("("+x+","+y+")");
}

/**
*	Установить все методы и обработчики для поддержки comboBox
*/
function xdn_set_hooks_for_input_box(inputBox) {

	//------------
	//свойства:
	//------------
	
	inputBox.autocomplete = "off";	
	inputBox.cache        = new Array();// кэш значений для укорения отображения
	inputBox.comboText    = null; // текст в комбинированном элементе
	inputBox.typedText    = null; // вводимый текст
	
	//-----------
	//методы:
	//-----------
	
	// Метод получения позиции и размера выделенной области
	if (inputBox.setSelectionRange) {
		//firefox
		inputBox.getSelection = function() {
			return new text_selection_class(
				this.selectionStart, this.selectionEnd
			);
		}
		inputBox.setTextHilited = function(oldText,newText) {
			this.value = newText;
			this.setSelectionRange(oldText.length, newText.length);
		}
	} else if (inputBox.createTextRange) {
		//explorer
		inputBox.getSelection = function() {
			var range=document.selection.createRange().duplicate();			
			var size = range.text.length;
			range.moveEnd("textedit",1);
			var start = this.value.length - range.text.length;
			return new text_selection_class( start, start + size);
		}		
		inputBox.setTextHilited = function(oldText,newText) {			
			this.value = newText;
			var range = this.createTextRange();
			log('"'+oldText+'"');
			range.moveStart("character",oldText.length);
			range.moveEnd("textedit");
			range.select();
			this.focus();
		}		
	} else {
		//вообще то это хреново - броузер древний
		return "No Suport for selectionStart/End";
	}
	
	inputBox.onblur = function(e) {
		this.comboText = null;
		if(!e)
			e = window.event;
		xdn.inputBox = null;
		xdn.selectBox.hide();
	}
	
	inputBox.onfocus = function() {
		xdn.inputBox = this;		
	}
	
	inputBox.onkeyup = function(e) {
		if (this.comboText==null)
			setTimeout("xdn.delayedView()", 100);
		if (!e)
			e = window.event;
		var key = e.keyCode;		
		var txt = this.value;
		var sel = this.getSelection();
		if (sel.end==txt.length) 
			txt = txt.substring( 0, sel.start );
		this.typedText = txt;
log( ""+key+"");	
	
		if(key==13||key==3){
			if(this.createTextRange){
				var range = this.createTextRange();
				range.moveStart("character",this.value.length);
				range.select()
			}
		}

		if ( key==40 ){
			//клавиша вниз			
			if (xdn.selectBox.optionsCount) {
				var selectBox = xdn.selectBox;
				var index;
				if (!selectBox.selected)
					index = 0;
				else
					index = selectBox.selected.index + 1;					
				if (index < selectBox.optionsCount) {
					this.setTextHilited(txt,selectBox.options[index]);
					selectBox.setSelected(index);	
				}
			}
			e.returnValue = false;
			e.cancelBubble = true;
			return;
		}
		if(key==38){
			e.returnValue = false;
			if (xdn.selectBox.optionsCount) {
				var selectBox = xdn.selectBox;
				var index;
				if (!selectBox.selected)
					index = selectBox.optionsCount - 1;
				else
					index = selectBox.selected.index - 1;					
				if ( index>=0 ) {
					this.setTextHilited(txt,selectBox.options[index]);
					selectBox.setSelected(index);	
				}
			}
			e.returnValue = false;
			e.cancelBubble = true;
			return;
		}
	}
}

/**
*	Создания объекта  связанного с полем ввода списка выбра
*/
function xdn_select_box_class() {
	var selectBox = document.getElementById("selectBox");
	if (selectBox!=null)
		return selectBox;
		
	selectBox = document.createElement("DIV");	
	selectBox.id = "selectBox";
	with(selectBox.style) {		
		borderRight     = "black " + 1  + "px solid";
		borderLeft      = "black " + 1  + "px solid";
		borderTop       = "black " + 1  + "px solid";
		borderBottom    = "black " + 1  + "px solid";
		zIndex          ="1";
		paddingRight    = "0";
		paddingLeft     = "0";
		paddingTop      = "0";
		paddingBottom   = "0";
		visibility      = "hidden";
		position        = "absolute";
		backgroundColor = "white";
		//стиль
		fontSize        = "13px";
		fontFamily      = "arial,sans-serif";
		wordWrap        = "break-word";
	}		
	
	document.body.appendChild(selectBox);
	
	window.onresize = function() {
		if (xdn.selectBox.style.visibility=="visible")
			xdn.selectBox.adjust();
	}
	
	selectBox.hide = function () {
		this.style.visibility="hidden";
	}
	
	selectBox.show = function () {
		if (this.optionsCount) {
			this.style.visibility="visible";
			this.adjust();
		}
	}
	
	selectBox.adjust = function() {		
		var input = xdn.inputBox;
		if (!input) {
			this.hide();
			return;
		}
		var s = this.style;
		s.left  = sum_of_offset(input,"offsetLeft") + "px";
		s.top   = sum_of_offset(input,"offsetTop") + input.offsetHeight - 1 + "px";
		s.width = input.offsetWidth - (xdn.firefox?2:0);
	}
	
	selectBox.setOptions = function(cs,Rb){
		//тут ещё надо разрулить когда делать или просто показывать
		this.options       = cs;
		this.optionsCount  = cs.length;
		this.selected      = null;
		this.optionsDivs   = new Array();
		while( this.childNodes.length > 0 )
			this.removeChild( this.childNodes[ 0 ] );
		for(var c=0; c < cs.length; ++c) {
			var div=document.createElement("DIV");//раздел для опции
			this.optionsDivs[c] = div;
			div.index = c;
			xdn_style(div,"xdnNormal");
			div.onmousedown= function () {
				xdn.inputBox.value = xdn.selectBox.options[this.index];
				xdn.selectBox.hide();
			}
			div.onmouseover= function () {
				if (xdn.selectBox.selected)
					xdn_style(xdn.selectBox.selected, "xdnNormal");
				xdn_style(this,"xdnSelected");
			}
			div.onmouseout= function() {
				xdn_style(this, "xdnNormal")			
			}
			var option=document.createElement("SPAN");//Опция
			var item1=document.createElement("SPAN");//1-ый элемент
			var item2=document.createElement("SPAN");//2-ой элемент
			item1.innerHTML=cs[c];
			item2.innerHTML=Rb[c];
			xdn_style(option,"xdnItem");						
			xdn_style(item1,"xdnItemLeft");
			xdn_style(item2,"xdnItemRight");			
			option.appendChild(item1);
			option.appendChild(item2);
			div.appendChild(option);
			this.appendChild(div);	
		}
	}
	
	// выбор одного из вариантов визуально
	selectBox.setSelected = function( index ){
		if (this.selected) {
			if (this.selected.index==index)
				return;
			xdn_style(this.selected,"xdnNormal");
		}
		if (index<0 || index>=this.optionsCount) {
			this.selected = null;
			return;
		}
		this.selected = this.optionsDivs[index]; //childNodes ???
		xdn_style(this.selected,"xdnSelected");
	}
	
	return selectBox
}

function XdnClass() {
	//действие от запроса
	this.callback=function(txt,values,comments) {
		var inputBox = this.inputBox;
		if (!inputBox) 
			return;
		inputBox.cache[txt] = [values, comments];
		this.applySelectBox(txt,values,comments);
	};
	//создание и отображение списка выбора
	this.applySelectBox = function(txt,vals,coms) {
		var selectBox = this.selectBox;
		var inputBox  = this.inputBox;
		selectBox.setOptions(vals,coms);
		if ( selectBox.optionsCount ) {
			selectBox.show();
			/*
			if (inputBox && inputBox.selectionGood) {
				inputBox.setTextHilited(
					inputBox.typedText,
					selectBox.options[0]
				);
				selectBox.setSelected(0);	
			}
			*/
		} else {
			selectBox.hide();
		}
	}
	//отображение списка выбора с задержкой
	this.delayedView = function() {		
		var inputBox = this.inputBox;
		if (!inputBox)
			return true;// что должен возвращать то?		
		if (inputBox.typedText != inputBox.comboText) {
			var cache = inputBox.cache[ inputBox.typedText ];
			if (cache) {
				this.applySelectBox( inputBox.typedText, cache[0], cache[1] );
			} else {
				this.http.process( inputBox.typedText );
			}
			inputBox.comboText = inputBox.typedText;
		}			
		setTimeout("xdn.delayedView()",this.http.getTimeOut());
		return true;
	}
	
	//инициализировать все смешанные боксы
	this.initComboBoxes = function( inputBoxes ) {
		if ( typeof(inputBoxes.length) != "number" )
			inputBoxes = [ inputBoxes ];
		for( var i = inputBoxes.length; --i >=0 ; ) 
			xdn_set_hooks_for_input_box( inputBoxes[i] );
	};
	
	this.explorer  = false;
	this.firefox   = false;	
	this.inputBox  = null;
	this.selectBox = xdn_select_box_class();	
	this.http      = new xdn_http_class();

	//	прикрутить обработчики клавиш "ввод" и стрелки
	if (!document.captureEvents)
		this.explorer = true;
	else {
		this.firefox  = true;
		document.captureEvents(Event.KEYPRESS);
		document.onkeypress = function(e) {
			var k = e.keyCode;
			if (k==40 || k==38 || k==13) {
				if (e.target==xdn.inputBox) {
					if (k==13) {
						var b = xdn.inputBox;
						b.setSelectionRange(b.value.length,b.value.length);
					}
					return false;
				}
			}
		}
	}

}

/**
*	Движок обработки запросов
*/
function xdn_http_class() {
	this.count  = 0;
	this.engine = null;
	if ( typeof XMLHttpRequest!= undef) {
		this.engine = new XMLHttpRequest();		
	}
	if ( this.engine==null && typeof ActiveXObject != undef ) {
		try {
			this.engine = new ActiveXObject("Msxml2.XMLHTTP");
		} catch( e ) {
		}
		if (this.engine==null) {
			try {
				this.engine = new ActiveXObject("Microsoft.XMLHTTP");
			} catch( e ) {
			}
		}
	}
	this.onReady = function() {
		var e = xdn.http.engine;
		if(e.readyState==4 && e.responseText) {			
			xdn.http.count--;			
			if(e.responseText.charAt(0)=="<") {
				alert(e.responseText);
				return;
			} 
			log( e.responseText );
			eval(e.responseText);
		}
	}
	this.process = function(url) {		
		this.count++;
		var e = this.engine;
		if (e.readyState!=0)
			e.abort();
		//e.open("GET", "test.txt", true);//url
log(">");
		e.open("GET", "http://localhost:8080/resin-doc/servlet/tutorial/helloworld/WordsFind?word="+encodeURIComponent(xdn.inputBox.value)
, true);//url encodeURIComponent(xdn.inputBox.value)
log("<");

		e.onreadystatechange= this.onReady;
		try{
			e.send(null);
		} catch (ex) {
			log(ex.message + "<br>");
		}
	}
	//задержка списка выбора - по качеству обработки запросов
	this.getTimeOut = function() {
		var msec = 100;
		for(var p=1; p<=(this.count-2)/2; p++)
			msec = msec*2;
		msec += 50;
		return msec;
	}		
}

/**
*	Применение стилей.... лучше всё-таки задать стили в css
*/
function xdn_style(el,cl) {
	//el.className = cl;
	var s = el.style;
	switch(cl){
		case "xdnItem":
			s.display="block";
			s.paddingLeft="3";
			s.paddingRight="3";
			s.height="16px";
			s.overflow="hidden";
			break;
		case "xdnNormal":
			s.backgroundColor="white";
			s.color="black";
			break;		
		case "xdnSelected":
			s.backgroundColor="#3366cc";
			s.color="white";
			break;	
		case "xdnItemLeft":
			s.width="50%";
			s.cssFloat="left";
			s.textAlign="left";
			break;	
		case "xdnItemRight":
			s.cssFloat="right";
			s.width="50%";
			s.fontSize="10px";
			s.textAlign="right";
			s.color="green";
			s.paddingTop="3px";
			break;
		default:
			alert("Undef style '"+ cl + "'?");
			break;				
	}	
}

