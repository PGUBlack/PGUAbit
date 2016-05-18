<%@ page contentType = "text/html;charset=windows-1251"
         language    = "java"
         isErrorPage = "true"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>
<%@ page import="java.io.PrintWriter" %>
<title>Внешний вид интерфейса системы</title>
<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>
<body bgcolor="#808040">
<bean:define id="tema" name="user" property="idTema"/>
<table border=0 height="100%" width="100%">
<tbody>
<tr><td valign="middle" align="center" height="35"><font size="4" color="white"><strong>Тема: "Строгая". Внешний вид интерфейса АСУ</strong></font></td></tr>
 <tr valign="middle">
   <td align="center">
      <img src="../img/present_full.jpg" width="980" height="800" alt="Скриншоты интерфейса...">
   </td>
 </tr>
</tbody>
</table>
</body>