//cookieScripts.js - –абота с Cookie
//----------------------------------

//ƒобавление Cookie
function addCookie(szName, szValue, nDayExpires)
//szName - им€ параметра Cookie
//sz Value - значение параметра 
//nDayExpires - врем€, через которое Cookie необходимо удалить
{
   var dtExpires = new Date()
   var szExpireDate = ""

   // ”становка даты удалени€ Cookie, как прибавлени€ к текущей
   // количества милисекунд, который пройдут до этой даты
   // 24 часа*60 минут*60 секунд*1000 милисекунд = 86 400 000 милисекунд.  
   dtExpires.setTime(dtExpires.getTime() + 
      nDayExpires*86400000)
   
   szExpireDate = dtExpires.toGMTString()

   document.cookie = 
      szName + "=" + szValue + "; expires=" + szExpireDate 
}

//ѕолучение значени€ Cookie
function getCookie(szName)
//szName - им€ параметра Cookie
{
   var i=0
   var nStartPosition = 0
   var nEndPosition = 0
   var szCookieString = document.cookie

   while(i <= szCookieString.length)
   {
      nStartPosition = i
      nEndPosition = nStartPosition + szName.length

      if(szCookieString.substring(nStartPosition, nEndPosition) == szName)
      // && charAt(nEndPosition) == "=" )
      {
         nStartPosition = nEndPosition + 1
         nEndPosition = szCookieString.indexOf(";", nStartPosition)
         
         if(nEndPosition < nStartPosition)
            nEndPosition = szCookieString.length
         
         return(document.cookie.substring(nStartPosition, nEndPosition)) 
         //break        
      }
      i++
   }

   return("")
}

//ѕроверка разрашени€ работы с Cookie 
function isCookieEnabled(viewMsg)
{
   if(navigator.cookieEnabled)
      return(true)
   else
   {
      if(viewMsg)
      {
         msgWindow=window.open("","","width=550,height=125,top=100,left=100")
         msgWindow.document.write('<title>Ќастройка системы</title><center>ƒл€ сохранени€ настроек системы необходимо, чтобы в настройках браузера была разрешена работа с Cookie.<Br><form><input type="button" value="«акрыть" onClick="window.close()"></form></center>')
         msgWindow.document.close()
         msgWindow.document.bgColor="white"
         return(false)
      }
   }
}