<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>
<HTML>
<HEAD>
<%@ page contentType="text/html;charset=windows-1251" %>
<link rel="stylesheet" type="text/css" href="layouts/all/all_style.css">
<script>
function selChk(v){
var cheks=window.document.body.getElementsByTagName("input");
for (var i=cheks.length-1; i>=0; i--)
{
if ((cheks[i].getAttribute("type", 1)=="checkbox"))
{
cheks[i].checked=v;
}
}
}
</script>
</HEAD>

<input type=button onclick='selChk("checked")' value='Выделить' ><br>


<BODY bgColor="#FFFFFF" style="MARGIN: 0px">




<html:form action="/fis_import.do?action=select">
	<html:submit  value="Отправить"/>
	<br>
	<logic:iterate id="abit" name="abits" scope='request'>
 		 <html:multibox property="selectedKods">
  		 <bean:write name="abit" property="kodAbiturienta"/>
  		 </html:multibox> 
  		 	<bean:write name="abit" property="kodAbiturienta"/>&nbsp;&nbsp;
  		<bean:write name="abit" property="nomerLichnogoDela"/>&nbsp;&nbsp;
	<bean:write name="abit" property="familija"/>&nbsp;&nbsp;
	<bean:write name="abit" property="imja"/>&nbsp;&nbsp;
	<bean:write name="abit" property="otchestvo"/><br>
	</logic:iterate>

</html:form>	







</body>
</html>