package abiturient.service;

import abiturient.dao.FisSpecDao;
import abiturient.model.FisSpec;
import abiturient.model.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by black on 30.01.17.
 */
public class DBWork {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/abiturient?autoReconnect=true&amp;characterEncoding=UTF-8&amp;characterSetResults=utf8&amp;connectionCollation=utf8_general_ci";
    private static final String USER = "abiturient";
    private static final String PASS = "ilovejava";


    public static List<Level> getLevels() {
        List<Level> res = new ArrayList<>();

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

            String sql = "SELECT DISTINCT `id`,`name` FROM levels ORDER by `id`";
            ResultSet rs = stmt.executeQuery(sql);
            //Extract data from result set
            while(rs.next()){
                Level level = new Level();
                level.setId(rs.getInt(1));
                level.setName(rs.getString(2));
                res.add(level);
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

    public static String getSpecs(int level, int action) {
        String res = new String();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection("jdbc:mysql://dpma.pnzgu.ru:3306/moodle?autoReconnect=true&amp;characterEncoding=UTF-8&amp;characterSetResults=utf8&amp;connectionCollation=utf8_general_ci", "asu", "asuUI");
            System.out.println("Connected database successfully...");
            //Execute a query
            System.out.println("Creating statement...");
            if(action==1) {
                res = res + "<option value='99999' selected>Не выбрано</option>";
                stmt = conn.prepareStatement("SELECT mbdsp.`id`, mbdsp.`name` from moodle.mdl_block_dof_s_programms mbdsp WHERE mbdsp.`edulevel` like ? and mbdsp.`status`='available' ");
                stmt.setInt(1, level);
                rs = stmt.executeQuery();
                //Extract data from result set
                while (rs.next()) {
                    res = res + "<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>";
                }
            }else{
                stmt = conn.prepareStatement("select distinct mbdsp.`shifr`, mcc1.`name` FROM moodle.mdl_block_dof_s_programms mbdsp LEFT JOIN moodle.mdl_course_categories mcc ON mcc.`id` = mbdsp.`mdl_category` LEFT JOIN moodle.mdl_course_categories mcc1 ON mcc1.`id` = mcc.`parent` WHERE mbdsp.`status` = 'available' and mbdsp.`id` like ? ");
                stmt.setInt(1, level);
                rs = stmt.executeQuery();
                //Extract data from result set
                while (rs.next()) {
                    FisSpec fs = new FisSpec();
                    fs.setCode(rs.getString(1));
                    fs.setUgsname(rs.getString(2));
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    res = ow.writeValueAsString(fs);

                }
            }
            System.out.println(res);
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
    }//end getSpecs
}
