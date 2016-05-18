<%@ page
    contentType="text/html;charset=windows-1251"
    language="java"
    import="org.apache.struts.taglib.template.util.*"
%>
<%@ taglib uri = "/WEB-INF/struts-html.tld"     prefix = "html"     %>
<%@ taglib uri = "/WEB-INF/struts-bean.tld"     prefix = "bean"     %>
<%@ taglib uri = "/WEB-INF/struts-logic.tld"    prefix = "logic"    %>
<%@ taglib uri = "/WEB-INF/struts-template.tld" prefix = "template" %>
<html:html locale="true">
<head>
<link rel="stylesheet" type"text/css" href="layouts/2/img/style2.css">

<title>Абитуриент - <template:get name="title"/></title>
<META content="Автоматизированная система управления Абитуриент" name="description">
<META content="АСУ,Абитуриент,Автоматизированная система управления,ИАИС,ПГУ" name='keywords'>
<META content="text/html; charset=windows-1251" http-equiv=Content-Type>
<META author="Потапов Алексей Александрович - ведущий программист ИВЦ ПГУ">
</head>

<body leftmargin="0" topmargin="0" background="layouts/2/img/bg.gif">

<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
 <tbody>
  <tr valign="top">
    <td background="layouts/2/img/xtro_04.bmp" valign="top" width="10">
      <img src="layouts/2/img/xtro_04.bmp">
    </td>
    <td>&nbsp;&nbsp;</td>
    <td width="100%">
<table align="center" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
  <tbody><tr>
    <td height="26" valign="top">
      <table align="center" background="layouts/2/img/xtratov2_SH_02.gif" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
        <tbody><tr>
          <td height="26" valign="top" width="41"><img src="layouts/2/img/xtratov2_SH_01.gif" alt="" height="72" width="31"></td>
          <td align="center" valign="top">
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
              <tbody>
                <tr>
                  <td height="12"></td>
                </tr>
                <tr>
                  <td height="24" valign="top" align="center">
                    <font class="sub_name"><template:get name="target_name"/></font>
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
          <td valign="top" width="23"><div align="right"><img src="layouts/2/img/xtratov2_SH_04.gif" alt="" height="72" width="31"></div></td>
        </tr>
      </tbody></table>

      <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody><tr>
          <td height="26" valign="top" width="41"><img src="layouts/2/img/xtratov2_SH_08.gif" alt="" height="30" width="17"></td>
          <td align="center" valign="top">
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
              <tbody>
                <tr>
                  <td height="28" align="center" valign="top">
                    <font class="sub_name"><template:get name="sub_name"/></font>
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
          <td valign="top" align="right"><img src="layouts/2/img/xtratov2_SH_09.gif" height="30" width="22"></td>
        </tr>
      </tbody></table>

        <table align="center" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
          <tbody><tr>
            <td background="layouts/2/img/xtratov2_SH_08.gif" height="26" valign="top" width="17"><img src="layouts/2/img/xtratov2_SH_08.gif" alt="" height="1" width="17"></td>
            <td bgcolor="#2b2923" valign="top" width="1225"><table align="left" bgcolor="#2b2923" border="0" cellpadding="3" cellspacing="0" width="100%">
                <tbody>
                  <tr>
                    <td class="infort" align="center" valign="middle" width="100%">
                      <template:get name="content"/>
                    </td>
                  </tr>
                </tbody>
            </table></td>
            <td background="layouts/2/img/xtratov2_SH_09.gif" valign="top" width="22"><div align="right"><img src="layouts/2/img/xtratov2_SH_09.gif" alt="" height="1" width="22"></div></td>
          </tr>
        </tbody></table>
        <table align="center" background="layouts/2/img/xtratov2_SH_14.gif" border="0" cellpadding="0" cellspacing="0" height="24" width="100%">
          <tbody><tr>
            <td height="24" valign="top" width="42"><img src="layouts/2/img/xtratov2_SH_12.gif" alt="" height="60" width="41"></td>
            <td valign="middle"><table align="center" border="0" cellpadding="0" cellspacing="0" height="24" width="100%">
                <tbody><tr>
                  <td height="24" valign="top"><span class="infort"> Автоматизированная система управления </span></td>
                </tr>
            </tbody></table></td>
            <td valign="top" width="44"><div align="right"><img src="layouts/2/img/xtratov2_SH_16.gif" alt="" height="60" width="43"></div></td>
          </tr>
      </tbody></table></td>
  </tr>
</tbody></table>
    </td>

    <td valign="top">&nbsp;&nbsp;</td>
    <td background="layouts/2/img/xtro_05.bmp" valign="top" width="10">
      <img src="layouts/2/img/xtro_05.bmp" border="0" height="10" width="36">
    </td>
  </tr>
 </tbody>
</table>

</body>
</html:html>