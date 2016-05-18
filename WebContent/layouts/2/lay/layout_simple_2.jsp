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

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tbody>
   <tr>
    <td background="layouts/2/img/XtratoVeil_FT_07.bmp" width="55">
      <img src="layouts/2/img/XtratoVeil_FT_66.bmp" height="129" width="28">
    </td>
    <td valign="top" background="layouts/2/img/XtratoVeil_FT_77.bmp">
      <table align="center" border="0" cellpadding="0" cellspacing="0">
       <tbody>
        <tr><td height="20"></td></tr>
        <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign="middle" align="left"><A target="_self" border=0 href="index.jsp">
             <img src="layouts/2/img/pgu_1k.bmp" border="0" height="105" width="145" alt="Главное меню"></a></td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
        </tr>
       </tbody>
      </table>
    </td>
    <td valign="top" background="layouts/2/img/XtratoVeil_FT_77.bmp">
      <table align="center" border="0" cellpadding="0" cellspacing="0">
       <tbody>
        <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign="middle" align="left">
             <img src="layouts/2/img/XtratoVeil_FT_06.bmp" height="129" width="49">
            </td>
        </tr>
       </tbody>
      </table>
    </td>
    <td valign="top" background="layouts/2/img/XtratoVeil_FT_07.bmp" width="100%">
      <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
       <tbody>
        <tr>
            <td height="38"></td>
        </tr>
        <tr><td valign="top" align="right"><img src="layouts/2/img/but_l.bmp" height="34" width="13"></td>
            <td valign="center" align="center" background="layouts/2/img/but_c.bmp"><font class="target_name"><template:get name="target_name"/></font></td>
            <td valign="top" align="left"><img src="layouts/2/img/but_r.bmp" height="34" width="13"></td>
        </tr>
        <tr>
            <td height="38" colspan="3" align="center"><font class=""><template:get name="mail"/></font></td>
        </tr>
       </tbody>
      </table>
    </td>
    <td background="layouts/2/img/XtratoVeil_FT_07.bmp" width="60"><div align="right"><img src="layouts/2/img/XtratoVeil_FT_12.bmp" height="129" width="49"></div></td>
   </tr>
</tbody>
</table>

<table background="layouts/2/img/xtratoV2_HD_09.gif" border="0" cellpadding="0" cellspacing="0" width="100%">
 <tbody>
  <tr>
   <td width="100%">
   </td>
  </tr>
 </tbody>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
 <tbody>
  <tr>
    <td><img src="layouts/2/img/xtratoV2_HD_07.bmp" height="47" width="39"></td>
    <td width="100%" background="layouts/2/img/xtratoV2_HD_09.bmp"></td>
    <td><img src="layouts/2/img/xtratoV2_HD_17.bmp" height="47" width="39"></td>
  </tr>
 </tbody>
</table>

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
    <td height="26" valign="top"><table align="center" background="layouts/2/img/xtratov2_SH_02.gif" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
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
                    <font class="sub_name"><template:get name="sub_name"/></font>
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
          <td valign="top" width="23"><div align="right"><img src="layouts/2/img/xtratov2_SH_04.gif" alt="" height="72" width="31"></div></td>
        </tr>
      </tbody></table>
        <table align="center" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
          <tbody><tr>
            <td background="layouts/2/img/xtratov2_SH_08.gif" height="26" valign="top" width="17"><img src="layouts/2/img/xtratov2_SH_08.gif" alt="" height="1" width="17"></td>
            <td bgcolor="#2b2923" valign="top" width="1225"><table align="left" bgcolor="#2b2923" border="0" cellpadding="3" cellspacing="0" width="100%">
                <tbody>
                  <tr>
                    <td class="infolt" align="center" valign="top" width="19%"><div align="center"><a href="http://sking.jino-net.ru/modules.php?name=News&amp;new_topic=7"><img src="layouts/2/img/video.gif" alt="Фильмы" border="0"></a><br>
                            <br>
                            <a href="http://sking.jino-net.ru/modules.php?name=News&amp;new_topic=7"><b>Фильмы</b></a></div></td>
                    <td class="infort" align="left" valign="top" width="81%">NUTRO!!!<br>
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
                  <td height="24" valign="top"><span class="infort"> 17/11/2005 </span></td>
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

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tbody><tr>
    <td><table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody><tr>
          <td background="layouts/2/img/xtro_04.jpeg" width="36"> <img src="layouts/2/img/xtro_04.bmp" border="0" height="10" width="38"></td>
          <td width="100%"><table align="center" border="0" cellpadding="0" cellspacing="0">
            <tbody>
             <tr>
               <td width="36"></td>
             </tr>
          </tbody></table></td>
          <td background="layouts/2/img/xtro_05.jpeg" width="10"><img src="layouts/2/img/xtro_05.bmp" border="0" height="10" width="36"></td>
        </tr>
  </tbody>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tbody>
   <tr>
    <td background="layouts/2/img/XtratoVeil_FT_072.bmp" width="55"> <img src="layouts/2/img/XtratoVeil_FT_062.bmp" height="21" width="57"></td>
    <td background="layouts/2/img/XtratoVeil_FT_072.bmp" width="1133"><table align="center" border="0" cellpadding="0" cellspacing="0" width="586">
    </tbody></table></td>
    <td background="layouts/2/img/XtratoVeil_FT_072.bmp" width="60"><div align="right"><img src="layouts/2/img/XtratoVeil_FT_122.bmp" height="21" width="61"></div></td>
   </tr>
</tbody></table>

</body>
</html:html>