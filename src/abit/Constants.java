package abit;

public class Constants {

// Количество строк, выводимое на экране одновременно
    public final static int totalSelectedRows = 20;

    public final static String EMPTY                       = "";
    public final static String APP_ENCODING                = "windows-1251";
    public final static String DB_ENCODING                 = "latin1";
    public final static String WEB_ENCODING                = "UTF-8";
    public final static String SESSIONS                    = "user-sessions";
    public final static String DATASOURCE_NAME             = "jdbc/sqlx";
    public final static String DATASOURCE_NAME_ONLINE             = "jdbc/sqlo";
    public final static String RELATIVE_PATH               = "\\reports";
    public final static String RELATIVE_PATH_ABT           = "\\packets";
    public final static String FBS_EGE_LOGIN               = "user10832";
    public final static String FBS_EGE_PASSWD              = "Ow3qSFH";
    public final static String FIS_CHECKING_BY_PASSPORT    = "http://fbsege.ru/AllUsers/BatchCheckFileFormatByPassport.aspx";
    public final static String FIS_CHECKING_BY_SERTIFICATE = "http://fbsege.ru/AllUsers/BatchCheckFileFormatByNumber.aspx";
//    public final static String Sur_Talants_NO_KONKURS    = "'010101','010701','010503','050501','140205','140501','140607','150202','150204','151001','151002','170105','190201','190205','190702','200101','200106','200501','200503','210104','210201','210302','210600','220201','220203','230401','230104','280202'";
//    public final static String Sur_Talants_100B_MAT      = "'060114','080800','090105','090106','230101','230102','230105'";
    public final static String Sur_Talants_NO_KONKURS      = "'-1'";
    public final static String Sur_Talants_100B_MAT        = "'-1'";
    public final static int    SESSION_TIME                = 3*600*1000; // 30 минут
    public final static String emptyNote                   = "-";
    public final static String emptyRepNote                = "-";
    public final static int    minExBall                   = 0;
    public final static int    maxExBall                   = 100;
    public final static String Att_orig                    = "о";
    public final static String Att_copy                    = "к";
}