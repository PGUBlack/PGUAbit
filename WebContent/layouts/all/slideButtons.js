var sb = {

//������ �������
   callRate : 10,
   slideTime   : 2500,

//document.body.clientWidth - ������ ���� �������� 
   maxDiffX     : document.all ? document.body.clientWidth : window.innerWidth,

//����������� ������������� ��������� ������ �������� � IE (���� ���������, �� isIE == true)
   isIE     : document.all ? true : false,

//???
   chaserDiv   : document[ document.all ? "all" : "layers"]["SlideButtons" ]
}

window.setInterval("sb.main( )", sb.callRate)
window.scroll(15)//oops

sb.main = function( )
{
//this.currentX - ������� ����������  �� X ���� ��������� ������
   this.currentX        = this.isIE ? this.chaserDiv.style.pixelLeft : this.chaserDiv.left
   
//this.scrollLeft - �������� �������� �� ���������� X    
   this.scrollLeft       = this.isIE ? document.body.scrollLeft : window.pageXOffset
  
//this.scrollWidth - ������ ���������   
   this.scrollWidth    = this.isIE ? document.body.scrollWidth : window.outerWidth   

//������ ���������� ������ ����� ��������� 
   this.availSpaceX      = this.scrollWidth - this.scrollLeft

//���������� �������� ���� �� � (��������� ������ ���������)
   this.midpointX        = this.scrollWidth / 2

// this.chaserDiv.offsetWidth - ������ (����������) �������� 
// this.chaserDiv.parentNode.offsetWidth - ������ �������� ������� (�� ��������� � ����������)
// ��������, ������ �������, � ������� ��������� �������
// maxX - ������������ �������� ������ ���������� �������
   var maxX          = this.chaserDiv.parentNode.offsetWidth - this.chaserDiv.offsetWidth;
   if(maxX < 0) 
      maxX = 0 
   var first = 1   
      
   // ���� �������� ��������� � �� ������ ���, �� ���������� ���
   if ( this.scrollLeft < (this.maxDiffX/20) && first == 0 )
   {
      var newTargetX = 0;
      first = 0
   }
   // ������ ������
   // ������ ������ ������� ���-������ ���������
   else
   {
      // ������� ��� ��������������� ������ ���������� ��������
      // �� ������ ���� ��������
      var middle = (this.maxDiffX/2) - (this.chaserDiv.offsetWidth/2)
      // ������� ������ �������
      var beg = this.scrollWidth - this.chaserDiv.parentNode.offsetWidth - 22         

      // ������ ���������� ���������� � �.�. ���������� ������
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
