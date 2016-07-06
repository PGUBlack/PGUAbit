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
<template:put name='title'>Получение оценок</template:put>
<template:put name="target_name">Получение оценок из ФИС</template:put>
<template:put name="sub_name">Параметры запроса</template:put>
<template:put name='content'>


<html:form action="fis_marks_import?action=toFIS" method="post" enctype="multipart/form-data" onsubmit="return checkFields();">
<table align=center border=0>
  <tr><td><font class="text_10">Файл:</font></td>
      <td><html:file  styleClass="select_f2" size='60' property="sourceFile" tabindex="8"/></td></tr>
<tr><td colspan=2 height=6></td></tr>
</table>
<table align=center border=0>
<tr align=center>
  <td><html:submit styleClass="button" tabindex="9" value="Отправить"/></td>
</html:form>

</tr><tr><td colspan=2 height=50></td></tr>
</table>
<br>
</template:put>
</template:insert>