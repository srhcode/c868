/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.*;
import c868.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Scott
 */
public class PayrollDAO {
    
    Connection con = DBConnector.getConnect();
    
    public ObservableList<Payroll> getPayroll() {
        ObservableList<Payroll> payroll = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT * FROM Employees;";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Payroll pay = new Payroll(result.getInt("EmpID"), result.getString("Name"), result.getFloat("Wage"), result.getFloat("Benefits"));
                payroll.add(pay);
            }
        } catch (SQLException sqlE) {
            
        }
        
        return payroll;
    }
}
