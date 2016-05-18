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
<script>

function invokeAct(){
  document.forms(0).submit();
}

</script>
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
        <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td valign="middle" align="left"><A target="_self" border=0 href="index.jsp">
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
            <td height="38" colspan="3" align="center">
            </td>
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
    <td valign="top" width="170">
      <table border="0" cellpadding="0" cellspacing="0" width="170">
        <tbody>
          <tr>
            <td background="layouts/2/img/blocks_04.bmp" height="54">
              <table align="center" border="0" cellpadding="0" cellspacing="0" height="8" width="100%">
                <tbody>
                  <tr>
                    <td height="52" valign="middle" align="center"><font class="header">Меню</font></td>
                  </tr>
                </tbody>	
              </table>	
              <table border="0" cellpadding="0" cellspacing="0" height="27" width="100%">
                <tbody>
                  <tr>
                    <td align="left" background="layouts/2/img/blocks_04.bmp" valign="middle">
                      <img src="layouts/2/img/blocks_05.bmp" height="29" width="188"></td>
                  </tr>
                </tbody>
              </table>
              <table border="0" cellpadding="0" cellspacing="0" width="170">
                <tbody>
                  <tr>
                    <td background="layouts/2/img/blocks_07.bmp">
                      <table align="center" border="0" cellpadding="0" cellspacing="0" height="19" width="100%">
                        <tbody>
                          <template:get name='buttons'/>
                        </tbody>
                      </table>
                      <table border="0" cellpadding="0" cellspacing="0" height="27" width="100%">
                        <tbody>
                          <tr>
                            <td align="left" valign="middle">
                              <img src="layouts/2/img/blocks_09.bmp" height="33" width="188"></td>
                          </tr>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
    </td>

    <td>&nbsp;&nbsp;</td>

    <td width="100%">
<table align="center" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
  <tbody><tr>
    <td valign="top"><table align="center" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
        <tbody><tr>
          <td background="layouts/2/img/storyhome_25.html" height="26" valign="top" width="41"><img src="layouts/2/img/tables_03.jpeg" height="62" width="29"></td>
          <td background="layouts/2/img/tables_04.jpeg" width="100%"><div align="center">
              <font class="sub_name"><template:get name="sub_name"/></font>
          </div></td>
          <td valign="top" width="23"><div align="right"><img src="layouts/2/img/tables_07.bmp" height="62" width="56"></div></td>
        </tr>
      </tbody></table>
        <table align="center" border="0" cellpadding="0" cellspacing="0" height="26" width="100%">
          <tbody><tr>
            <td background="layouts/2/img/tables_09.jpeg" height="26" valign="top" width="19"><img src="layouts/2/img/tables_09.jpeg" height="9" width="25"></td>
            <td bgcolor="#2b2923" valign="top"><table border="0" cellpadding="3" cellspacing="0" width="100%">
                <tbody>
                  <tr>
                    <td class="infolt" align="center" bgcolor="#2b2923" height="25" valign="top"><font face="verdana,arial,helvetica" size="1">
                      <center><font class="content"><template:get name="content"/></font></td>
                  </tr>
                </tbody>
            </table></td>
            <td background="layouts/2/img/tables_11.jpeg" valign="top" width="15"><div align="right"><img src="layouts/2/img/tables_11.jpeg" height="9" width="27"></div></td>
          </tr>
        </tbody></table>
        <table align="center" border="0" cellpadding="0" cellspacing="0" height="24" width="100%">
          <tbody><tr>
            <td background="layouts/2/img/tables_17.jpeg" valign="top" width="41"><img src="layouts/2/img/tables_16.jpeg" height="24" width="25"></td>
            <td background="layouts/2/img/tables_17.jpeg" valign="top" width="1167">&nbsp;</td>
            <td background="layouts/2/img/tables_17.jpeg" valign="top" width="23"><div align="right"><img src="layouts/2/img/tables_20.jpeg" height="24" width="27"></div></td>
          </tr>
      </tbody></table></td>
  </tr>
</tbody></table>
    </td>

    <td valign="top">&nbsp;</td>
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