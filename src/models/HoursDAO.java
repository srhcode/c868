package models;

import c868.DBConnector;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 Data Access class for hours table.  
 */
public class HoursDAO {
    Connection con = DBConnector.getConnect();
    
    public void punchIn(int empID, LocalDateTime time) {
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeStr = format.format(time);
        
        try {
            String sql = "INSERT INTO Hours (EmpID, Punch_In) VALUES (" + empID + ", \"" + timeStr + "\");";
            PreparedStatement stmt = con.prepareStatement(sql);
            int result = stmt.executeUpdate();
            
        } catch (SQLException sqlE) {
            System.out.println(sqlE);
        }
    }
    
    public void punchOut(int empID, LocalDateTime time) {
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeStr = format.format(time);
        
        try {
            String sql = "UPDATE Hours SET Punch_Out = \"" + timeStr + "\" WHERE EmpID = " + empID + " AND Punch_Out IS NULL;";
            PreparedStatement stmt = con.prepareStatement(sql);
            int result = stmt.executeUpdate();
            
        } catch (SQLException sqlE) {
            
        }
    }
    
    public boolean isOnTheClock(int empID) {
        boolean punchedIn = false;
        
        try {
            String sql = "SELECT * FROM Hours WHERE EmpID = " + empID + " ORDER BY ID DESC LIMIT 1;";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            if (result.next()){
                String out = result.getString("Punch_Out");
                String in = result.getString("Punch_In");
                if (out == null && in != null) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            
        }
        return punchedIn;
    }

    public ObservableList<Hours> getWeek(int empID) {
        ObservableList weekHours = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT * FROM Hours WHERE EmpID = " + empID + " AND Punch_In BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime')";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                Hours punch = new Hours(result.getInt("ID"), result.getInt("EmpID"), result.getString("Punch_In"), result.getString("Punch_Out"));
                weekHours.add(punch);
            }
        } catch (SQLException sqlE) {
            
        }
        
        return weekHours;
    }

    public ObservableList<Hours> getMonth(int empID) {
        ObservableList monthHours = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT * FROM Hours WHERE EmpID = " + empID + " AND Punch_In BETWEEN datetime('now', '-30 days') AND datetime('now', 'localtime')";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                Hours punch = new Hours(result.getInt("ID"), result.getInt("EmpID"), result.getString("Punch_In"), result.getString("Punch_Out"));
                monthHours.add(punch);
            }
        } catch (SQLException sqlE) {
            
        }
        
        return monthHours;
    }
    
    public Hours getPunch(int hoursId) {
        Hours punch = new Hours(0, 0, "", "");
        try {
            String sql = "SELECT * FROM Hours WHERE ID = " + hoursId + ";";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                punch = new Hours(result.getInt("ID"), result.getInt("EmpID"), result.getString("Punch_In"), result.getString("Punch_Out"));
            }
        } catch (SQLException sqlE) {
            
        }
        return punch;
    }
    
    public ObservableList<Hours> getEmpPunches(int empID){
        ObservableList empPunches = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT * FROM Hours WHERE empID = " + empID + ";";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Hours punch = new Hours(result.getInt("ID"), result.getInt("EmpID"), result.getString("Punch_In"), result.getString("Punch_Out"));
                empPunches.add(punch);
            }
        } catch (SQLException sqlE) {
            
        }
        
        return empPunches;
    }

    void deleteAllPunches(int id) {
        try {
            String sql = "DELETE FROM Hours WHERE EmpID = " + id + ";";
            PreparedStatement stmt = con.prepareStatement(sql);
            int result = stmt.executeUpdate();
        } catch (SQLException sqlE) {
            
        }
    }

    public void deletePunch(int id) {
        
        try {
            String sql = "DELETE FROM Hours WHERE ID = " + id +";";
            PreparedStatement stmt = con.prepareStatement(sql);
            int result = stmt.executeUpdate();
        } catch (SQLException sqlE) {
            
        }
    }
    
    public void addPunchIn(String time, int id) {
        String sql = "INSERT INTO Hours (Punch_In, EmpID) VALUES (\"" + time + "\", " + id + ");";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            int result = stmt.executeUpdate();
        } catch (SQLException sqlE) {
            
        }
    }
    
    public void addPunchOut(String time, int id) {
        String sql = "UPDATE Hours SET Punch_Out = \"" + time + "\" WHERE ID = " + id + ";";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            int result = stmt.executeUpdate();
        } catch (SQLException sqlE) {
            
        }
    }
    
    public void updatePunch(String inTime, String outTime, int id) {
        String sql = "UPDATE Hours SET ";
        sql = sql + "Punch_In = \"" + inTime + "\", ";
        sql = sql + "Punch_Out = \"" + outTime + "\" ";
        sql = sql + "WHERE ID = " + id + ";";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            int result = stmt.executeUpdate();
        } catch (SQLException sqlE) {
            
        }
    }
    
    public boolean validatePunch(String time, String inOut, String timeIn, int id, int empID) {
        boolean valid = true;
        ResultSet result = null;
        try {
            String sql = "SELECT * FROM Hours WHERE EmpID = " + empID + ";";
            PreparedStatement stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();
        } catch (SQLException sqlE) {
            
        }
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime newTime = LocalDateTime.parse(time, format);
        
        if (inOut.equals("in")) {
            try {
                while (result.next()) {
                    String in = result.getString("Punch_In");
                    String out = result.getString("Punch_Out");
                    LocalDateTime inTime = LocalDateTime.parse(in, format);
                    LocalDateTime outTime = null;
                    if (out != null) {
                        outTime = LocalDateTime.parse(out, format);
                    } else {
                        outTime = LocalDateTime.parse("9999-01-01 00:00:00", format);
                    }
                    if ((time.equals(in) || time.equals(out)) && result.getInt("ID") != id) {
                        valid = false;
                    } else if (result.getInt("ID") != id && out == null && newTime.isAfter(inTime)) {
                        valid = false;
                    } else if (result.getInt("ID") != id && out != null) {
                        if (newTime.isAfter(inTime) && newTime.isBefore(outTime)) {
                            valid = false;
                        }
                    } else if (result.getInt("ID") == id && !newTime.isBefore(outTime)) {
                        valid = false;
                    }
                }
            } catch (SQLException ex) {
                
            }
        }
        
        if (inOut.equals("out")) {
            try {
                while (result.next()) {
                    String in = result.getString("Punch_In");
                    String out = result.getString("Punch_Out");
                    LocalDateTime inTime = LocalDateTime.parse(in, format);
                    LocalDateTime outTime = null;
                    if ((time.equals(in) || time.equals(out)) && result.getInt("ID") != id) {
                        valid = false;
                    } else if (result.getInt("ID") != id && out == null && newTime.isAfter(LocalDateTime.parse(in, format))) {
                        valid = false;
                    } else if (result.getInt("ID") != id && out != null) {
                        if (newTime.isAfter(LocalDateTime.parse(in, format)) && newTime.isBefore(LocalDateTime.parse(out, format))) {
                            valid = false;
                        }
                    } else if (result.getInt("ID") == id && !newTime.isAfter(LocalDateTime.parse(timeIn, format))) {
                        valid = false;
                    }
                }
            } catch (SQLException ex) {
                
            }
        }
        
        return valid;
    }
}
