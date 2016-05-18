<!-- 
var scroller = 0;
var max = 0;
var position = 20;


function  hide_scrolls()
{
	
	document.body.style.overflow='hidden';
}



function move_left()
{
	block = blockDiv.style;
	if(scroller>0)window.scrollBy(-(screen.width-20), 0);
	if(scroller>0) block.left =document.body.scrollLeft + position;
	scroller = scroller - (screen.width-20);
	if(scroller<0)scroller=0;
	max = 0;
}

function move_right()
{	
	block = blockDiv.style;
	if(max!=1)scroller = scroller + (screen.width-20);
	if(max!=1)window.scrollBy((screen.width-20), 0);
	if(scroller != document.body.scrollLeft)
	{
		max = 1;
		scroller = document.body.scrollLeft;
	}
	block.left = position + scroller;
}

//  End -->
