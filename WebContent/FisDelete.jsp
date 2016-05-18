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
function selChkSome(v){
	var cheks=window.document.body.getElementsByTagName("input");
	var max=window.document.getElementById("max").value;
	var min=window.document.getElementById("min").value;
	for (var i=max; i>=min; i--)
	{
	if ((cheks[i+4].getAttribute("type", 1)=="checkbox"))
	{
	cheks[i+4].checked=v;
	}
	}
	}
</script>
</HEAD>

<input type=button onclick='selChk("checked")' value='Выделить' ><br>

<input type=text id="min" ><br>
<input type=text id="max" ><br>

<input type=button onclick='selChkSome("checked")' value='Выделить несколько' ><br>

<BODY bgColor="#FFFFFF" style="MARGIN: 0px">




<html:form action="/fis_delete.do?action=select">
	<html:submit  value="Удалить"/>
	<br>
	<logic:iterate id="abit1" name="abits1" scope='request'>
 		 <html:multibox property="selectedKods">
  		 <bean:write name="abit1" property="kodAbiturienta"/>
  		 </html:multibox> 
  		 	<bean:write name="abit1" property="kodAbiturienta"/>&nbsp;&nbsp;
  		<bean:write name="abit1" property="nomerLichnogoDela"/>&nbsp;&nbsp;
	<bean:write name="abit1" property="familija"/>&nbsp;&nbsp;
	<bean:write name="abit1" property="imja"/>&nbsp;&nbsp;
	<bean:write name="abit1" property="otchestvo"/><br>
	</logic:iterate>

</html:form>	







</body>
</html>