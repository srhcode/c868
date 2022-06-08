
package c868;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 Database Connection class.  
 */
public class DBConnector {
        
    private static final String dbUrl = "jdbc:sqlite:c868.db";    
    private static Connection conn = null;
    
    /** Creates connection to the Database. */
    public static Connection startConnect() {
        try {
            conn = DriverManager.getConnection(dbUrl);
            createTables(conn);
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }
    
    /** Retrieves the current open database connection */
    public static Connection getConnect() {
        return conn;
    }
    
    /** Closes the current open database connection */
    public static void closeConnect() {
        try {
            conn.close();
        } catch (Exception e){
            // Ignore Exception
        }
    }

    private static void createTables(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS \"Employees\" (\n" +
       "	\"EmpID\"	INTEGER NOT NULL UNIQUE,\n" +
       "	\"Name\"	TEXT NOT NULL,\n" +
       "	\"Wage\"	NUMERIC NOT NULL,\n" +
       "	\"Status\"	TEXT NOT NULL,\n" +
       "	\"Benefits\"	NUMERIC NOT NULL,\n" +
       "	\"Password\"	TEXT NOT NULL DEFAULT \"default\",\n" +
       "	PRIMARY KEY(\"EmpID\" AUTOINCREMENT)\n" +
       ")";
        
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS \"Hours\" (\n" +
                  "	\"ID\"	INTEGER NOT NULL UNIQUE,\n" +
                  "	\"EmpID\"	INTEGER NOT NULL,\n" +
                  "	\"Punch_In\"	TEXT,\n" +
                  "	\"Punch_Out\"	TEXT,\n" +
                  "	PRIMARY KEY(\"ID\" AUTOINCREMENT),\n" +
                  "	FOREIGN KEY(\"EmpID\") REFERENCES \"Employees\"(\"EmpID\")\n" +
                  ")";
            
            stmt.execute(sql);
            
            sql = "INSERT INTO Employees (EmpID, Name, Wage, Status, Benefits, Password) VALUES (1, \"admin\", 0.0, \"Admin\", 0.0, \"admin\");";
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            
        }
    }
}
