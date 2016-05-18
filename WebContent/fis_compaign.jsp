<%@ page
    contentType = "text/html;charset=windows-1251"
    language    = "java"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>

<logic:notPresent name='user' scope='session'>
 <logic:redirect forward='login'/>
</logic:notPresent>



<bean:define id="tema" name="user" property="idTema"/>

<SCRIPT LANGUAGE="JavaScript">

function checkFields(){
  var valid = " ёйцукенгшщзхъфывапролджэячсмитьбюЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ-()."
  var temp;
}





</SCRIPT>





<%-----------------------------------------------------------------%>
<%----------------------- Добавление записи -----------------------%>
<%-----------------------------------------------------------------%>

<template:insert template="<%="layouts/"+tema+"/lay/layout_simple.jsp"%>">
<template:put name='title'>Отправка свдений о приемной комиссии в ФИС</template:put>
<template:put name="target_name">Отправка свдений о приемной комиссии в ФИС</template:put>
<template:put name="sub_name">Параметры</template:put>
<template:put name='content'>


<html:form action="/fis_compaign.do?action=select">
<table align=center border=0>

  <tr><td><font class="text_10">Имя приемной комиссии:</font></td>
       <td><html:text  styleClass="text_f10" property="nameCompaign" 
                      value=""/></td></tr>

<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <html:submit  value="Отправить"/>
</html:form>

</tr><tr><td colspan=2 height=50></td></tr>
</table>
<br>
</template:put>
</template:insert>