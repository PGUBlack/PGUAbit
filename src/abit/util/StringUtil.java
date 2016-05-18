package abit.util;

import abit.Constants;
import java.util.Date;
import abit.bean.*;

public class StringUtil {

//---------------------------------------------------------------------
//   ������� �������������� ������ � ������ ��:��:��
//---------------------------------------------------------------------
    public final static String getMonthOnDigit(int mon) {
      String str = new String();
      if(mon == 1) str = "������";
      else if(mon == 2) str = "�������";
      else if(mon == 3) str = "����";
      else if(mon == 4) str = "������";
      else if(mon == 5) str = "���";
      else if(mon == 6) str = "����";
      else if(mon == 7) str = "����";
      else if(mon == 8) str = "������";
      else if(mon == 9) str = "��������";
      else if(mon == 10) str = "�������";
      else if(mon == 11) str = "������";
      else if(mon == 12) str = "�������";
      else str = "-";
      return str;
    }

//---------------------------------------------------------------------
//   ������� �������������� ������ � ������ ��:��:��
//---------------------------------------------------------------------
    public final static String StoHMS(long secs) {
      long h=0, m=0, s=0;
      String hr,min,sec;
      h = secs/3600;
      m = (secs-3600*h)/60;
      s = secs-3600*h-60*m;
      if(h<10) hr = "0" + h;
      else  hr = ""+h;
      if(m<10) min = "0" + m;
      else  min = ""+m;
      if(s<10) sec = "0" + s;
      else  sec = ""+s;
      return new String(hr+":"+min+":"+sec);
    }

//---------------------------------------------------------------------
//   ������� ��� �������������� ������� ����, 
//   ������������ ����� ����������
//---------------------------------------------------------------------
    public final static String toWEB(String str) {
        if (str!=null) {
            try {
                str = new String(str.getBytes(),Constants.WEB_ENCODING);
            } catch(Exception e) {
                ; //Nothing to do. It seems wrong.
            }
        }
        return str;
    }

//---------------------------------------------------------------------
//   ������� ��� �������������� ������� ����, 
//   ������������ ����� ����������
//---------------------------------------------------------------------
    public final static String toAFU(String str) {
        if (str!=null) {
            try {
                str = new String(str.getBytes(),Constants.APP_ENCODING);
            } catch(Exception e) {
                ; //Nothing to do. It seems wrong.
            }
        }
        return str;
    }

//---------------------------------------------------------------------
//   ������� �������������� �������� "0" ��� "-" � ������ ""
//---------------------------------------------------------------------
    public final static String voidFilter(String str) {
      if(str!=null && (str.equals("0") || str.equals("-"))) return "";
      else if(str == null) return "";
      else return str;
    }


//---------------------------------------------------------------------
//   ������� ��������� �������� ����
//---------------------------------------------------------------------
    public final static String CurrYear() {

// For old Resin Server

////      return new String((new Date()).getYear()+1900+"");
      return new String("2016");
    }


//---------------------------------------------------------------------
//   ������� ������ �������� ������� �� �����������
//---------------------------------------------------------------------
    public final static String UnSpace() {
      
      return new String("_");
      
    }

//---------------------------------------------------------------------
//   ������� ������ ������ ����� � ���� ����� �� �����. � 2� �������
//---------------------------------------------------------------------
    public final static String digitCursToWord(int dig, int type) {
      if(type == 1) {      
        if(dig == 1) return new String("������");
        else if(dig == 2) return new String("������");
        else if(dig == 3) return new String("������");
        else if(dig == 4) return new String("��������");
        else if(dig == 5) return new String("�����");
        else if(dig == 6) return new String("������");
        else if(dig == 7) return new String("�������");
        else if(dig == 8) return new String("�������");
        else if(dig == 9) return new String("�������");
        else if(dig == 10) return new String("�������");
        else if(dig == 11) return new String("������������");
        else if(dig == 12) return new String("�����������");
        else if(dig == 13) return new String("�����������");
        else if(dig == 14) return new String("�������������");
        else if(dig == 15) return new String("�����������");
        else return new String(""+dig);
      } else {
        if(dig == 1) return new String("�������");
        else if(dig == 2) return new String("�������");
        else if(dig == 3) return new String("��������");
        else if(dig == 4) return new String("���������");
        else if(dig == 5) return new String("������");
        else if(dig == 6) return new String("�������");
        else if(dig == 7) return new String("��������");
        else if(dig == 8) return new String("��������");
        else if(dig == 9) return new String("��������");
        else if(dig == 10) return new String("��������");
        else if(dig == 11) return new String("�������������");
        else if(dig == 12) return new String("������������");
        else if(dig == 13) return new String("������������");
        else if(dig == 14) return new String("��������������");
        else if(dig == 15) return new String("������������");
        else return new String(""+dig);
      }
    }

//---------------------------------------------------------------------
//   ������� ������ �������� ����� �� �������
//---------------------------------------------------------------------
    public final static String toRomeLetter(int dig) {
      
      if(dig == 1) return new String("I");
      else if(dig == 2) return new String("II");
      else if(dig == 3) return new String("III");
      else if(dig == 4) return new String("IV");
      else if(dig == 5) return new String("V");
      else if(dig == 6) return new String("VI");
      else if(dig == 7) return new String("VII");
      else if(dig == 8) return new String("VIII");
      else if(dig == 9) return new String("IX");
      else if(dig == 10) return new String("X");
      else if(dig == 11) return new String("XI");
      else if(dig == 12) return new String("XII");
      else if(dig == 13) return new String("XIII");
      else if(dig == 14) return new String("XIV");
      else if(dig == 15) return new String("XV");
      else if(dig == 16) return new String("XVI");
      else if(dig == 17) return new String("XVII");
      else if(dig == 18) return new String("XVIII");
      else if(dig == 19) return new String("XIX");
      else if(dig == 20) return new String("XX");
      else if(dig == 21) return new String("XXI");
      else if(dig == 22) return new String("XXII");
      else if(dig == 23) return new String("XXIII");
      else if(dig == 24) return new String("XXIV");
      else if(dig == 25) return new String("XXV");
      else return new String(""+dig);
      
    }


//---------------------------------------------------------------------
// ����������� �������� null � '' - �����. ������������ � �������
//---------------------------------------------------------------------

    public final static String ntv(String str) {
       if(str == null){
          return "";
       } else if(str.equals("null"))
          return "";
       else
       return str;
    }


//---------------------------------------------------------------------
// ����������� ������� �������� � '+' ������������ � �������
//---------------------------------------------------------------------

    public final static String val_to_plus(String str) {
       if(str != null)
          return "+";
       else if(str == null)
          return "";
       else if(str.equals("null"))
          return "";
       else
       return str;
    }


//---------------------------------------------------------------------
// ����������� �������� 0 � '' - �����. ������������ � �������
//---------------------------------------------------------------------

    public final static String ztv(String str) {
       if(str == null){
          return "";
       } else if(str.equals("0"))
          return "";
       else
          return str;
    }


//---------------------------------------------------------------------
//   ������� �������������� ����������� �������� ������,
//   ������������ � ����������, � ��������, �������� � ����
//---------------------------------------------------------------------
    public final static String toSQL(String str) {
           if(str!=null || str!= "" || str!="null") {
             str=str.replace('*','%');
             str=str.replace('.','_');
           } else return "%";
           return str;
    }

//---------------------------------------------------------------------
// ����������� �������� null � '-'. ������������ � �������
//---------------------------------------------------------------------

    public final static String nts(String str) {
       if(str == null){
          return "-";
       }
       return str;
    }

//---------------------------------------------------------------------
//   ������� ��������� ������� ����
//---------------------------------------------------------------------
    public final static String getDateFromDT(long datetime) {
      if(datetime == 0) return "-";
      int mnt;
      Date date = new Date(datetime);
      String data,month,year;

        if(date.getDate()<10) data = new String("0"+date.getDate());
            else data = new String(date.getDate()+"");
        if((mnt = date.getMonth()+1)<10) month = new String("0"+mnt);
            else month = new String(mnt+"");

// For Old Resin Server
//        year = new String(date.getYear()+1900+"");
        year = new String("2016");
 
      return data+"."+month+"."+year;
}

//---------------------------------------------------------------------
//   ������� ��������� �������� �������
//---------------------------------------------------------------------
    public final static String getTimeFromDT(long datetime) {
      if(datetime == 0) return "-";
      Date date = new Date(datetime);
      String hrs,min,sec;

        if(date.getHours()<10) hrs = new String("0"+date.getHours());
            else hrs = new String(date.getHours()+"");
        if(date.getMinutes()<10) min = new String("0"+date.getMinutes());
            else min = new String(date.getMinutes()+"");
        if(date.getSeconds()<10) sec = new String("0"+date.getSeconds());
            else sec = new String(date.getSeconds()+"");
 
      return hrs+":"+min+":"+sec;
}

//---------------------------------------------------------------------
//   ������� ��������� ������� ����
//---------------------------------------------------------------------
    public final static String CurrDate(String type) {
      int mnt;
      Date date = new Date();
      String data,month,year;

        if(date.getDate()<10) data = new String("0"+date.getDate());
            else data = new String(date.getDate()+"");
        if((mnt = date.getMonth()+1)<10) month = new String("0"+mnt);
            else month = new String(mnt+"");

// For Old Resin Server
//        year = new String(date.getYear()+1900+"");
        year = new String("2016");
 
      if(type.equals(".")) return data+"."+month+"."+year;
      else if(type.equals("-")) return data+"-"+month+"-"+year;
      else return data+"_"+month+"_"+year;
}

//---------------------------------------------------------------------
//   ������� ��������� �������� �������
//---------------------------------------------------------------------
    public final static String CurrTime(String type) {

      Date date = new Date();
      String hrs,min,sec;

        if(date.getHours()<10) hrs = new String("0"+date.getHours());
            else hrs = new String(date.getHours()+"");
        if(date.getMinutes()<10) min = new String("0"+date.getMinutes());
            else min = new String(date.getMinutes()+"");
        if(date.getSeconds()<10) sec = new String("0"+date.getSeconds());
            else sec = new String(date.getSeconds()+"");
 
      if(type.equals(":")) return hrs+":"+min+":"+sec;
      else if(type.equals("-")) return hrs+"-"+min+"-"+sec;
      else return hrs+"_"+min+"_"+sec;
}

//---------------------------------------------------------------------
//   ������� ������ ����� � ������� �������
//   sign - ������ ������� ���������� ��������
//---------------------------------------------------------------------
    public final static ReportsBrowserBean AddToRepBrw(String user,String name,String file_con,String file_ext) {

      String file_name;
      ReportsBrowserBean rpt = new ReportsBrowserBean();
      
      rpt.setName(name);
      rpt.setFileName(user+"_"+file_con+"."+file_ext);
      rpt.setAuthor(user);
      rpt.setDate(StringUtil.CurrDate("."));
      rpt.setTime(StringUtil.CurrTime(":"));
      return rpt;
}

//---------------------------------------------------------------------
//   ������� ��� �������������� ������� ����, 
//   ���������� �� �� ��� ����������
//---------------------------------------------------------------------
    public final static String toApp(String str) {
        if (str!=null) {
            try {
                str = new String(str.getBytes(),Constants.APP_ENCODING);
            } catch(Exception e) {
                ; //Nothing to do. It seems wrong.
            }
        }
        return str;
    }
//---------------------------------------------------------------------
//   ������� ��� �������������� ������� ����,
//   ���������� � ���������� ��� ��
//---------------------------------------------------------------------
    public final static String toDB(String str) {
        if (str!=null) {
            try {
                str = new String(str.getBytes(Constants.DB_ENCODING));
            } catch(Exception e) {
                ; //Nothing to do. It seems wrong.
            }
        }
        return str;
    }
//---------------------------------------------------------------------
//   ������� �������������� ����������� �������� ������,
//   ������������ � ����������, � ��������, �������� � ����
//---------------------------------------------------------------------
    public final static String toMySQL(String str) {
           if(str!=null || str!= "" || str!="null") {
             str=str.replace('*','%');
             str=str.replace('.','_');
           } else return "%";
           return str;
    }

//---------------------------------------------------------------------
//   ������� ��������� �������������� ������� ���� �� ����,
//   ��������� � ����������: �:�:� � ����, ������������� � ����: �:�:� 
//---------------------------------------------------------------------
    public final static String data_toDB(String str) {
        if (str!=null && str.length() == 10) {//safe for null
            try {
                str = new String(str.substring(6)+"-"+str.substring(3,5)+"-"+str.substring(0,2));
            } catch(Exception e) {
                ;//Nothing to do. It seems wrong.
            }
        }
        return str;
    }

//---------------------------------------------------------------------
//   �������, ��������������� ��� ����������� ������������� ���������� 
//   � ����, ������������� ��������� ������� �������� ����.
//   ��������, � MS SQL Server ������ � ����� �������� � �����,
//   � � MySQL �������� ������ ����
//   ������� ��������� ����� �������������� ������� ���� � ����: �:�:�
//---------------------------------------------------------------------
    public final static String data_toApp(String str) {
        if (str!=null && (str.length() == 10 || str.length() == 21)) {
            try {
                str = new String(str.substring(8,10)+"-"+str.substring(5,7)+"-"+str.substring(0,4));
            } catch(Exception e) {
                ;//Nothing to do. It seems wrong.
            }
        }
        return str;
    }

//---------------------------------------------------------------------
//   ������� ������������� ��� ���������� ������ ��� �� 
//   � ��������� ��� �������� ���� ���� ��������� - �����
//---------------------------------------------------------------------
    public final static String prepareForDB(String str) {
        StringBuffer res = new StringBuffer();
        if (str!=null) {
            try {
                for(int i=0;i<str.length();i++) {
                   if(str.charAt(i) != '\'') res.append(str.charAt(i));
                   else res.append("''");
                }
            } catch(Exception e) {
                ;//Nothing to do. It seems wrong.
            }
        }
        return res.toString();
    }

//---------------------------------------------------------------------
//   �������, ��������������� ��� �������������� ��������� �������� ���
//   ��������, 1-1-4 ��� 01-1-04 ��� 1-01-04 ��� 1-1-2004 � �.�. �
//   ��������������� ������: 01-01-2004
//---------------------------------------------------------------------

    public final static String DataConverter(String str) {
        if (str!=null) {//safe for null
            try {
			 String Den = new String();
			 String Mes = new String();
			 String God = new String();
			 String Razd = new String();
			 if(str.indexOf('-') == -1) Razd = ".";
				else Razd = "-";
			 int fi = str.indexOf(Razd);
			 int li = str.indexOf(Razd,fi+1);

			 Den = str.substring(0,fi);
			 Mes = str.substring(fi+1,li);
			 God = str.substring(li+1);
			 if(Den.length() < 2) Den = "0"+Den;
			 if(Mes.length() < 2) Mes = "0"+Mes;
			 if(God.length() == 1) God = "200"+God;
                         else if(God.length() < 4) 
			 {
			  if(Integer.parseInt(God) < 20) God = "20"+God;
				else God = "19"+God;
			 }
			 str = new String(Den+"-"+Mes+"-"+God);
		
	           } catch(Exception e) {
                ;//Nothing to do. It seems wrong.
            }
        }
        return str;
    }

//---------------------------------------------------------------------
//   ������� �������������� ������ � �����
//   ���������� ����������� ������� parseInt()
//---------------------------------------------------------------------
    public final static int toInt(String str, int def) {
        int val;
        try {
            val = Integer.parseInt(str);
        }
        catch (Exception e) {
            val = def;
        }
        return val;
    }

//---------------------------------------------------------------------
//   ������� ������������� ����
//---------------------------------------------------------------------
    public final static String RecodeLetter(String str) {

        try {

        if(str != null) str = new String(str.getBytes("UTF-8"),"Cp1251");

        } catch (Exception e) {

        }
        return str;
    }

//---------------------------------------------------------------------
//   ������� ����������� ����
//---------------------------------------------------------------------
    public final static String CodeLetter(String str) {

        try {

        if(str != null) str = new String(str.getBytes("Cp1251"),"UTF-8");

        } catch (Exception e) {

        }
        return str;
    }

//---------------------------------------------------------------------
//   ������� �������������� ������ � �����
//   ���������� ����������� ������� parseInt()
//---------------------------------------------------------------------
    public final static float toFloat(String str, int def) {
        float val;
        try {
            val = (new Float(str)).floatValue();
        }
        catch (Exception e) {
            val = def;
        }
        return val;
    }

//---------------------------------------------------------------------
//   �������������� ������� ���� � ���������� ����������
//   ������������ ��� �������� ���� �������
//---------------------------------------------------------------------
    public final static String toEng(String str) {
       int i;
       StringBuffer strb = new StringBuffer(str);
       String val = new String(); 
        for(i=0;i<strb.length();i++){ 
           switch(strb.charAt(i)) {
             case '�': val+="a"; break;
             case '�': val+="b"; break;
             case '�': val+="v"; break;
             case '�': val+="g"; break;
             case '�': val+="d"; break;
             case '�': val+="e"; break;
             case '�': val+="jo"; break;
             case '�': val+="zh"; break;
             case '�': val+="z"; break;
             case '�': val+="i"; break;
             case '�': val+="j"; break;
             case '�': val+="k"; break;
             case '�': val+="l"; break;
             case '�': val+="m"; break;
             case '�': val+="n"; break;
             case '�': val+="o"; break;
             case '�': val+="p"; break;
             case '�': val+="r"; break;
             case '�': val+="s"; break;
             case '�': val+="t"; break;
             case '�': val+="u"; break;
             case '�': val+="f"; break;
             case '�': val+="h"; break;
             case '�': val+="th"; break;
             case '�': val+="'0"; break;
             case '�': val+="`0"; break;
             case '�': val+="c"; break;
             case '�': val+="sh"; break;
             case '�': val+="sz"; break;
             case '�': val+="y"; break;
             case '�': val+="je"; break;
             case '�': val+="ju"; break;
             case '�': val+="ja"; break;
             case '�': val+="A"; break;
             case '�': val+="B"; break;
             case '�': val+="V"; break;
             case '�': val+="G"; break;
             case '�': val+="D"; break;
             case '�': val+="E"; break;
             case '�': val+="JO"; break;
             case '�': val+="ZH"; break;
             case '�': val+="Z"; break;
             case '�': val+="I"; break;
             case '�': val+="J"; break;
             case '�': val+="K"; break;
             case '�': val+="L"; break;
             case '�': val+="M"; break;
             case '�': val+="N"; break;
             case '�': val+="O"; break;
             case '�': val+="P"; break;
             case '�': val+="R"; break;
             case '�': val+="S"; break;
             case '�': val+="T"; break;
             case '�': val+="U"; break;
             case '�': val+="F"; break;
             case '�': val+="H"; break;
             case '�': val+="TH"; break;
             case '�': val+="'1"; break;
             case '�': val+="`1"; break;
             case '�': val+="C"; break;
             case '�': val+="SH"; break;
             case '�': val+="SZ"; break;
             case '�': val+="Y"; break;
             case '�': val+="JE"; break;
             case '�': val+="JU"; break;
             case '�': val+="JA"; break;
             default:  val+=strb.charAt(i);
           }
        }
    return val;
    }


//---------------------------------------------------------------------
//   �������������� ���������� ���� � ������� ����������
//   ������������ ��� �������� ������� ���������� (����)
//---------------------------------------------------------------------
    public final static String toRus(String str) {
       int i;
       StringBuffer strb = new StringBuffer(str);
       String str_tmp = new String();
       String val = new String(); 
        for(i=0;i<strb.length();i++){ 
         if(strb.length()>1) {
           if((i+1)<strb.length()) { 
             str_tmp = strb.substring(i,i+2);
             if(str_tmp.equals("jo")) { val+="�"; }
             if(str_tmp.equals("zh")) { val+="�"; }
             if(str_tmp.equals("th")) { val+="�"; }
             if(str_tmp.equals("sh")) { val+="�"; }
             if(str_tmp.equals("sz")) { val+="�"; }
             if(str_tmp.equals("ju")) { val+="�"; }
             if(str_tmp.equals("ja")) { val+="�"; }
             if(str_tmp.equals("je")) { val+="�"; }
             if(str_tmp.equals("'0")) { val+="�"; }
             if(str_tmp.equals("`0")) { val+="�"; }

             if(str_tmp.equals("JO")) { val+="�"; }
             if(str_tmp.equals("ZH")) { val+="�"; }
             if(str_tmp.equals("TH")) { val+="�"; }
             if(str_tmp.equals("SH")) { val+="�"; }
             if(str_tmp.equals("SZ")) { val+="�"; }
             if(str_tmp.equals("JU")) { val+="�"; }
             if(str_tmp.equals("JA")) { val+="�"; }
             if(str_tmp.equals("JE")) { val+="�"; }
             if(str_tmp.equals("'1")) { val+="�"; }
             if(str_tmp.equals("`1")) { val+="�"; }

             i+=2; 
             continue;
           }
         }
           switch(strb.charAt(i)) {
             case 'a': val+="�"; break;
             case 'b': val+="�"; break;
             case 'v': val+="�"; break;
             case 'g': val+="�"; break;
             case 'd': val+="�"; break;
             case 'e': val+="�"; break;
             case 'z': val+="�"; break;
             case 'i': val+="�"; break;
             case 'j': val+="�"; break;
             case 'k': val+="�"; break;
             case 'l': val+="�"; break;
             case 'm': val+="�"; break;
             case 'n': val+="�"; break;
             case 'o': val+="�"; break;
             case 'p': val+="�"; break;
             case 'r': val+="�"; break;
             case 's': val+="�"; break;
             case 't': val+="�"; break;
             case 'u': val+="�"; break;
             case 'y': val+="�"; break;
             case 'f': val+="�"; break;
             case 'h': val+="�"; break;
             case 'c': val+="�"; break;
             case 'A': val+="�"; break;
             case 'B': val+="�"; break;
             case 'V': val+="�"; break;
             case 'G': val+="�"; break;
             case 'D': val+="�"; break;
             case 'E': val+="�"; break;
             case 'Z': val+="�"; break;
             case 'I': val+="�"; break;
             case 'J': val+="�"; break;
             case 'K': val+="�"; break;
             case 'L': val+="�"; break;
             case 'M': val+="�"; break;
             case 'N': val+="�"; break;
             case 'O': val+="�"; break;
             case 'P': val+="�"; break;
             case 'R': val+="�"; break;
             case 'S': val+="�"; break;
             case 'T': val+="�"; break;
             case 'U': val+="�"; break;
             case 'Y': val+="�"; break;
             case 'F': val+="�"; break;
             case 'H': val+="�"; break;
             case 'C': val+="�"; break;
             default:  val+=strb.charAt(i);
           }
        }
    return val;
    }

//---------------------------------------------------------------------
//   ������� ���� ������������� ��� ������� ������ ����: a,b,c,d
//   � �������������� �� � ������: 'a','b','c','d'
//   @name - �������� �������� (��������: ShifrKursov) 
//   @src - ������ ��� �������
//   @result - �������������� ������
//   ��� ������������: � AbitSrchAction, FGruppyAction,BlankAction � �.�. 
//---------------------------------------------------------------------
public final static String PlaceUnaryComas(String name,String src){
       int currInd = 0;
       int nextInd = 0;
       String result="";
       src += ",";
       if(src.indexOf("*") != -1)
         result += name+" LIKE '%'";
       else {
         while(src.indexOf(",",nextInd) != -1) {
         nextInd = src.indexOf(',',nextInd);
         result+= "'" + src.substring(currInd,nextInd)+"',";
         currInd = ++nextInd;
         } 
       }
       return result.substring(0,result.length()-1);
}

//---------------------------------------------------------------------
//   ������� ���� ������������� ��� ������� ��������������
//   ���������� �������� "null" � "\N"
//   @src - ������ ��� �������
//   ��� ������������: � SqlrepAction ��� ����������� �������� ������
//---------------------------------------------------------------------
public final static String null_to_Null(String src){
       if(src == null) return "NULL";
       else if (src.equals("null")) return "NULL";
       return "'"+src.trim()+"'";
}

}