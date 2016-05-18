package abit.util;

import abit.Constants;
import java.util.Date;
import abit.bean.*;

public class StringUtil {

//---------------------------------------------------------------------
//   Функция преобразования секунд в формат ЧЧ:ММ:СС
//---------------------------------------------------------------------
    public final static String getMonthOnDigit(int mon) {
      String str = new String();
      if(mon == 1) str = "Январь";
      else if(mon == 2) str = "Февраль";
      else if(mon == 3) str = "Март";
      else if(mon == 4) str = "Апрель";
      else if(mon == 5) str = "Май";
      else if(mon == 6) str = "Июнь";
      else if(mon == 7) str = "Июль";
      else if(mon == 8) str = "Август";
      else if(mon == 9) str = "Сентябрь";
      else if(mon == 10) str = "Октябрь";
      else if(mon == 11) str = "Ноябрь";
      else if(mon == 12) str = "Декабрь";
      else str = "-";
      return str;
    }

//---------------------------------------------------------------------
//   Функция преобразования секунд в формат ЧЧ:ММ:СС
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
//   Функция для преобразования русских букв, 
//   передаваемых между сервлетами
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
//   Функция для преобразования русских букв, 
//   передаваемых между сервлетами
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
//   Функция преобразования значений "0" или "-" в пустое ""
//---------------------------------------------------------------------
    public final static String voidFilter(String str) {
      if(str!=null && (str.equals("0") || str.equals("-"))) return "";
      else if(str == null) return "";
      else return str;
    }


//---------------------------------------------------------------------
//   Функция получения текущего года
//---------------------------------------------------------------------
    public final static String CurrYear() {

// For old Resin Server

////      return new String((new Date()).getYear()+1900+"");
      return new String("2016");
    }


//---------------------------------------------------------------------
//   Функция замены обычного пробела на неразрывный
//---------------------------------------------------------------------
    public final static String UnSpace() {
      
      return new String("_");
      
    }

//---------------------------------------------------------------------
//   Функция замены номера курса в виде цифры на слово. В 2х падежах
//---------------------------------------------------------------------
    public final static String digitCursToWord(int dig, int type) {
      if(type == 1) {      
        if(dig == 1) return new String("первый");
        else if(dig == 2) return new String("второй");
        else if(dig == 3) return new String("третий");
        else if(dig == 4) return new String("четвёртый");
        else if(dig == 5) return new String("пятый");
        else if(dig == 6) return new String("шестой");
        else if(dig == 7) return new String("седьмой");
        else if(dig == 8) return new String("восьмой");
        else if(dig == 9) return new String("девятый");
        else if(dig == 10) return new String("десятый");
        else if(dig == 11) return new String("одиннадцатый");
        else if(dig == 12) return new String("двенадцатый");
        else if(dig == 13) return new String("тринадцатый");
        else if(dig == 14) return new String("четырнадцатый");
        else if(dig == 15) return new String("пятнадцатый");
        else return new String(""+dig);
      } else {
        if(dig == 1) return new String("первого");
        else if(dig == 2) return new String("второго");
        else if(dig == 3) return new String("третьего");
        else if(dig == 4) return new String("четвёртого");
        else if(dig == 5) return new String("пятого");
        else if(dig == 6) return new String("шестого");
        else if(dig == 7) return new String("седьмого");
        else if(dig == 8) return new String("восьмого");
        else if(dig == 9) return new String("девятого");
        else if(dig == 10) return new String("десятого");
        else if(dig == 11) return new String("одиннадцатого");
        else if(dig == 12) return new String("двенадцатого");
        else if(dig == 13) return new String("тринадцатого");
        else if(dig == 14) return new String("четырнадцатого");
        else if(dig == 15) return new String("пятнадцатого");
        else return new String(""+dig);
      }
    }

//---------------------------------------------------------------------
//   Функция замены арабской цифры на римскую
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
// Преобразует значение null в '' - пусто. Используется в отчетах
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
// Преобразует текущее значение в '+' Используется в отчетах
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
// Преобразует значение 0 в '' - пусто. Используется в отчетах
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
//   Функция преобразования специальных символов поиска,
//   используемых в приложении, к символам, принятым в СУБД
//---------------------------------------------------------------------
    public final static String toSQL(String str) {
           if(str!=null || str!= "" || str!="null") {
             str=str.replace('*','%');
             str=str.replace('.','_');
           } else return "%";
           return str;
    }

//---------------------------------------------------------------------
// Преобразует значение null в '-'. Используется в отчетах
//---------------------------------------------------------------------

    public final static String nts(String str) {
       if(str == null){
          return "-";
       }
       return str;
    }

//---------------------------------------------------------------------
//   Функция получения текущей даты
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
//   Функция получения текущего времени
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
//   Функция получения текущей даты
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
//   Функция получения текущего времени
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
//   Функция записи файла в браузер отчетов
//   sign - хранит признак завершения операции
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
//   Функция для преобразования русских букв, 
//   выбираемых из БД для приложения
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
//   Функция для преобразования русских букв,
//   получаемых в приложении для БД
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
//   Функция преобразования специальных символов поиска,
//   используемых в приложении, к символам, принятым в СУБД
//---------------------------------------------------------------------
    public final static String toMySQL(String str) {
           if(str!=null || str!= "" || str!="null") {
             str=str.replace('*','%');
             str=str.replace('.','_');
           } else return "%";
           return str;
    }

//---------------------------------------------------------------------
//   Функция выполняет преобразование формата даты от вида,
//   принятого в приложении: Д:М:Г к виду, используемому в СУБД: Г:М:Д 
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
//   Функция, предназначенная для обеспечения совместимости приложения 
//   с СУБД, использующими различные форматы хранения даты.
//   Например, в MS SQL Server вместе с датой хранится и время,
//   а в MySQL хранится только дата
//   Функция выполняет также преобразование формата даты к виду: Д:М:Г
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
//   Функция предназначена для подготовки данных для БД 
//   В частности она заменяет один знак апострофа - двумя
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
//   Функция, предназначенная для преобразования различных форматов дат
//   Например, 1-1-4 или 01-1-04 или 1-01-04 или 1-1-2004 и т.п. в
//   унифицированный формат: 01-01-2004
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
//   Функция преобразования строки в целое
//   использует стандартную функцию parseInt()
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
//   Функция декодирования букв
//---------------------------------------------------------------------
    public final static String RecodeLetter(String str) {

        try {

        if(str != null) str = new String(str.getBytes("UTF-8"),"Cp1251");

        } catch (Exception e) {

        }
        return str;
    }

//---------------------------------------------------------------------
//   Функция кодирования букв
//---------------------------------------------------------------------
    public final static String CodeLetter(String str) {

        try {

        if(str != null) str = new String(str.getBytes("Cp1251"),"UTF-8");

        } catch (Exception e) {

        }
        return str;
    }

//---------------------------------------------------------------------
//   Функция преобразования строки в целое
//   использует стандартную функцию parseInt()
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
//   Перекодировщик русских букв в английский эквивалент
//   Используется для создания имен отчетов
//---------------------------------------------------------------------
    public final static String toEng(String str) {
       int i;
       StringBuffer strb = new StringBuffer(str);
       String val = new String(); 
        for(i=0;i<strb.length();i++){ 
           switch(strb.charAt(i)) {
             case 'а': val+="a"; break;
             case 'б': val+="b"; break;
             case 'в': val+="v"; break;
             case 'г': val+="g"; break;
             case 'д': val+="d"; break;
             case 'е': val+="e"; break;
             case 'ё': val+="jo"; break;
             case 'ж': val+="zh"; break;
             case 'з': val+="z"; break;
             case 'и': val+="i"; break;
             case 'й': val+="j"; break;
             case 'к': val+="k"; break;
             case 'л': val+="l"; break;
             case 'м': val+="m"; break;
             case 'н': val+="n"; break;
             case 'о': val+="o"; break;
             case 'п': val+="p"; break;
             case 'р': val+="r"; break;
             case 'с': val+="s"; break;
             case 'т': val+="t"; break;
             case 'у': val+="u"; break;
             case 'ф': val+="f"; break;
             case 'х': val+="h"; break;
             case 'ч': val+="th"; break;
             case 'ь': val+="'0"; break;
             case 'ъ': val+="`0"; break;
             case 'ц': val+="c"; break;
             case 'ш': val+="sh"; break;
             case 'щ': val+="sz"; break;
             case 'ы': val+="y"; break;
             case 'э': val+="je"; break;
             case 'ю': val+="ju"; break;
             case 'я': val+="ja"; break;
             case 'А': val+="A"; break;
             case 'Б': val+="B"; break;
             case 'В': val+="V"; break;
             case 'Г': val+="G"; break;
             case 'Д': val+="D"; break;
             case 'Е': val+="E"; break;
             case 'Ё': val+="JO"; break;
             case 'Ж': val+="ZH"; break;
             case 'З': val+="Z"; break;
             case 'И': val+="I"; break;
             case 'Й': val+="J"; break;
             case 'К': val+="K"; break;
             case 'Л': val+="L"; break;
             case 'М': val+="M"; break;
             case 'Н': val+="N"; break;
             case 'О': val+="O"; break;
             case 'П': val+="P"; break;
             case 'Р': val+="R"; break;
             case 'С': val+="S"; break;
             case 'Т': val+="T"; break;
             case 'У': val+="U"; break;
             case 'Ф': val+="F"; break;
             case 'Х': val+="H"; break;
             case 'Ч': val+="TH"; break;
             case 'Ь': val+="'1"; break;
             case 'Ъ': val+="`1"; break;
             case 'Ц': val+="C"; break;
             case 'Ш': val+="SH"; break;
             case 'Щ': val+="SZ"; break;
             case 'Ы': val+="Y"; break;
             case 'Э': val+="JE"; break;
             case 'Ю': val+="JU"; break;
             case 'Я': val+="JA"; break;
             default:  val+=strb.charAt(i);
           }
        }
    return val;
    }


//---------------------------------------------------------------------
//   Перекодировщик английских букв в русский эквивалент
//   Используется для передачи русских параметров (букв)
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
             if(str_tmp.equals("jo")) { val+="ё"; }
             if(str_tmp.equals("zh")) { val+="ж"; }
             if(str_tmp.equals("th")) { val+="ч"; }
             if(str_tmp.equals("sh")) { val+="ш"; }
             if(str_tmp.equals("sz")) { val+="щ"; }
             if(str_tmp.equals("ju")) { val+="ю"; }
             if(str_tmp.equals("ja")) { val+="я"; }
             if(str_tmp.equals("je")) { val+="э"; }
             if(str_tmp.equals("'0")) { val+="ь"; }
             if(str_tmp.equals("`0")) { val+="ъ"; }

             if(str_tmp.equals("JO")) { val+="Ё"; }
             if(str_tmp.equals("ZH")) { val+="Ж"; }
             if(str_tmp.equals("TH")) { val+="Ч"; }
             if(str_tmp.equals("SH")) { val+="Ш"; }
             if(str_tmp.equals("SZ")) { val+="Щ"; }
             if(str_tmp.equals("JU")) { val+="Ю"; }
             if(str_tmp.equals("JA")) { val+="Я"; }
             if(str_tmp.equals("JE")) { val+="Э"; }
             if(str_tmp.equals("'1")) { val+="Ь"; }
             if(str_tmp.equals("`1")) { val+="Ъ"; }

             i+=2; 
             continue;
           }
         }
           switch(strb.charAt(i)) {
             case 'a': val+="а"; break;
             case 'b': val+="б"; break;
             case 'v': val+="в"; break;
             case 'g': val+="г"; break;
             case 'd': val+="д"; break;
             case 'e': val+="е"; break;
             case 'z': val+="з"; break;
             case 'i': val+="и"; break;
             case 'j': val+="й"; break;
             case 'k': val+="к"; break;
             case 'l': val+="л"; break;
             case 'm': val+="м"; break;
             case 'n': val+="н"; break;
             case 'o': val+="о"; break;
             case 'p': val+="п"; break;
             case 'r': val+="р"; break;
             case 's': val+="с"; break;
             case 't': val+="т"; break;
             case 'u': val+="у"; break;
             case 'y': val+="ы"; break;
             case 'f': val+="ф"; break;
             case 'h': val+="х"; break;
             case 'c': val+="ц"; break;
             case 'A': val+="А"; break;
             case 'B': val+="Б"; break;
             case 'V': val+="В"; break;
             case 'G': val+="Г"; break;
             case 'D': val+="Д"; break;
             case 'E': val+="Е"; break;
             case 'Z': val+="З"; break;
             case 'I': val+="И"; break;
             case 'J': val+="Й"; break;
             case 'K': val+="К"; break;
             case 'L': val+="Л"; break;
             case 'M': val+="М"; break;
             case 'N': val+="Н"; break;
             case 'O': val+="О"; break;
             case 'P': val+="П"; break;
             case 'R': val+="Р"; break;
             case 'S': val+="С"; break;
             case 'T': val+="Т"; break;
             case 'U': val+="У"; break;
             case 'Y': val+="Ы"; break;
             case 'F': val+="Ф"; break;
             case 'H': val+="Х"; break;
             case 'C': val+="Ц"; break;
             default:  val+=strb.charAt(i);
           }
        }
    return val;
    }

//---------------------------------------------------------------------
//   Функция ниже предназначена для разбора строки вида: a,b,c,d
//   и преобразования ее в строку: 'a','b','c','d'
//   @name - Название свойства (Например: ShifrKursov) 
//   @src - строка для разбора
//   @result - результирующая строка
//   Это используется: в AbitSrchAction, FGruppyAction,BlankAction и т.п. 
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
//   Функция ниже предназначена для разбора преобразования
//   строкового значения "null" в "\N"
//   @src - строка для анализа
//   Это используется: в SqlrepAction для корректного создания таблиц
//---------------------------------------------------------------------
public final static String null_to_Null(String src){
       if(src == null) return "NULL";
       else if (src.equals("null")) return "NULL";
       return "'"+src.trim()+"'";
}

}