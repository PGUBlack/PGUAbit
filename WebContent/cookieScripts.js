//cookieScripts.js - ������ � Cookie
//----------------------------------

//���������� Cookie
function addCookie(szName, szValue, nDayExpires)
//szName - ��� ��������� Cookie
//sz Value - �������� ��������� 
//nDayExpires - �����, ����� ������� Cookie ���������� �������
{
   var dtExpires = new Date()
   var szExpireDate = ""

   // ��������� ���� �������� Cookie, ��� ����������� � �������
   // ���������� ����������, ������� ������� �� ���� ����
   // 24 ����*60 �����*60 ������*1000 ���������� = 86 400 000 ����������.  
   dtExpires.setTime(dtExpires.getTime() + 
      nDayExpires*86400000)
   
   szExpireDate = dtExpires.toGMTString()

   document.cookie = 
      szName + "=" + szValue + "; expires=" + szExpireDate 
}

//��������� �������� Cookie
function getCookie(szName)
//szName - ��� ��������� Cookie
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

//�������� ���������� ������ � Cookie 
function isCookieEnabled(viewMsg)
{
   if(navigator.cookieEnabled)
      return(true)
   else
   {
      if(viewMsg)
      {
         msgWindow=window.open("","","width=550,height=125,top=100,left=100")
         msgWindow.document.write('<title>��������� �������</title><center>��� ���������� �������� ������� ����������, ����� � ���������� �������� ���� ��������� ������ � Cookie.<Br><form><input type="button" value="�������" onClick="window.close()"></form></center>')
         msgWindow.document.close()
         msgWindow.document.bgColor="white"
         return(false)
      }
   }
}