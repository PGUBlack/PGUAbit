package abiturient.service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by black on 30.01.17.
 */
public class DBWork {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/abiturient?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;characterSetResults=utf8&amp;connectionCollation=utf8_general_ci";
    private static final String USER = "abiturient";
    private static final String PASS = "ilovejava";

    public static List<String> getLevels() {
        List<String> res = new ArrayList<String>();
        res.add("Не выбрано");

        Connection conn = null;
        Statement stmt = null;
        try{
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "SELECT DISTINCT `name` FROM levels";
            ResultSet rs = stmt.executeQuery(sql);
            //Extract data from result set
            while(rs.next()){
                res.add(rs.getString(1));
            }
            rs.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        return res;
    }//end getLevels
}
