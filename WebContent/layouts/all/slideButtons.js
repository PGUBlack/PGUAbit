var sb = {

//период повтора
   callRate : 10,
   slideTime   : 2500,

//document.body.clientWidth - ширина окна браузера 
   maxDiffX     : document.all ? document.body.clientWidth : window.innerWidth,

//определение совместимости объектной модели браузера с IE (если совместим, то isIE == true)
   isIE     : document.all ? true : false,

//???
   chaserDiv   : document[ document.all ? "all" : "layers"]["SlideButtons" ]
}

window.setInterval("sb.main( )", sb.callRate)
window.scroll(15)//oops

sb.main = function( )
{
//this.currentX - текущая координата  по X этих плавающих кнопок
   this.currentX        = this.isIE ? this.chaserDiv.style.pixelLeft : this.chaserDiv.left
   
//this.scrollLeft - смещение страницы по координате X    
   this.scrollLeft       = this.isIE ? document.body.scrollLeft : window.pageXOffset
  
//this.scrollWidth - ширина документа   
   this.scrollWidth    = this.isIE ? document.body.scrollWidth : window.outerWidth   

//ширина оставшейся справа части документа 
   this.availSpaceX      = this.scrollWidth - this.scrollLeft

//координата середины окна по Х (полловина ширины документа)
   this.midpointX        = this.scrollWidth / 2

// this.chaserDiv.offsetWidth - ширина (плавающего) элемента 
// this.chaserDiv.parentNode.offsetWidth - ширина внешнего раздела (по отношению к плавающему)
// например, ячейки таблицы, в которой находится элемент
// maxX - максимальное смещение вправо плавающего раздела
   var maxX          = this.chaserDiv.parentNode.offsetWidth - this.chaserDiv.offsetWidth;
   if(maxX < 0) 
      maxX = 0 
   var first = 1   
      
   // если смещение маленькое и не первый раз, то игнорируем его
   if ( this.scrollLeft < (this.maxDiffX/20) && first == 0 )
   {
      var newTargetX = 0;
      first = 0
   }
   // далеко уехали
   // делать нечего придётся что-нибудь посчитать
   else
   {
      // позиция для центрированного вывода плавающего элемента
      // от левого края страницы
      var middle = (this.maxDiffX/2) - (this.chaserDiv.offsetWidth/2)
      // позиция начала раздела
      var beg = this.scrollWidth - this.chaserDiv.parentNode.offsetWidth - 22         

      // просто поправляем координату и Т.О. центрируем раздел
      var newTargetX = 0
      newTargetX    = this.scrollLeft + middle - beg

      if(newTargetX > maxX)
      {
         newTargetX = maxX;
      }
   }

   if ( this.currentX != newTargetX )
   {
      if ( newTargetX != this.targetX )
      {
         this.targetX = newTargetX
         this.slideInit( )
      }
      this.slide( )
   }

}

sb.slideInit = function( )
{
   var now  = new Date( )
   
   this.AX      = this.targetX - this.currentX
   this.BX      = Math.PI / ( 2 * this.slideTime )
   this.CX      = now.getTime( )

   if (Math.abs(this.AX) > this.maxDiffX)
   {
      this.DX = this.AX > 0 ? this.targetX - this.maxDiffX : this.targetX + this.maxDiffX
      this.AX = this.AX > 0 ? this.maxDiffX : -this.maxDiffX
   } 
   else
   {
      this.DX = this.currentX
   }
}

sb.slide = function( )
{
   var now  = new Date( )
   var newX = this.AX * Math.sin( this.BX * ( now.getTime( ) - this.CX ) ) + this.DX
   newX     = Math.round( newX )

   if (( this.AX > 0 && newX > this.currentX ) ||
      ( this.AX < 0 && newX < this.currentX )) 
   {
         
      if ( this.isIE )
         this.chaserDiv.style.pixelLeft = newX;
      else
         this.chaserDiv.left = newX
   }

}
