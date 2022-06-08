package models;

import c868.DBConnector;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
  Employee Data Access Object class.  
 */
public class EmployeeDAO {
    
    Connection conn = DBConnector.getConnect();
    
    
    public Employee getEmployee(int id){
        try {
            String sql = "SELECT * FROM Employees WHERE EmpID = " + id + ";";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()){
                String status = result.getString("Status");
                if (status.equals("Manager") || status.equals("Full") || status.equals("Admin")){
                    Employee emp = new FullTimeEmployee(result.getFloat("Benefits"), result.getFloat("Wage"), result.getString("Name"), status, result.getInt("EmpID"), result.getString("Password"));
                    return emp;
                } else if (status.equals("Part")){
                    Employee emp = new PartTimeEmployee(result.getFloat("Wage"), result.getString("Name"), status, result.getString("Password"), result.getInt("EmpID"));
                    return emp;
                } else {
                    Employee emp = new Employee(result.getInt("EmpID"), result.getString("Name"), status, result.getString("Password"));
                    return emp;
                }
            }
        } catch (SQLException sqlE) {
            
        }
        Employee emp = new Employee(0, "Error", "Error", "Error");
        return emp;
    }
    
    public ObservableList<FullTimeEmployee> getAllEmployees() {
        ObservableList<FullTimeEmployee> empList = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT * FROM Employees";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                FullTimeEmployee emp = new FullTimeEmployee(result.getFloat("Benefits"), result.getFloat("Wage"), result.getString("Name"), result.getString("Status"), result.getInt("EmpID"), result.getString("Password"));
                empList.add(emp);
            }
        } catch (SQLException sqlE) {
            
        }
        
        return empList;
    }
    
    public boolean checkCredentials(String id, String pw){
        
        try {
            String sql = "Select * from Employees WHERE EmpID = " + id + ";";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                if (result.getString("Password").equals(pw)){
                    return true;
                }
            }
        } catch (SQLException sqlE) {
            
        }
        
        return false;
    }
    
    
    
    public void addEmployee(FullTimeEmployee emp) {
        String sql = "";
        if (emp.getStatus().equals("Volunteer")){
            sql = "INSERT INTO Employees (EmpID, Name, Wage, Status, Benefits, Password) VALUES (" +
                emp.getEmpID() + ", \"" +
                emp.getName() + "\", " +
                "0.00, \"" + 
                emp.getStatus() + "\", " +
                "0.00, \"" +
                emp.getPassword() + "\");";
        } else if (emp.getStatus().equals("Part")) {
            sql = "INSERT INTO Employees (EmpID, Name, Wage, Status, Benefits, Password) VALUES (" +
                emp.getEmpID() + ", \"" +
                emp.getName() + "\", " +
                emp.getWage() + ", \"" +
                emp.getStatus() + "\", " +
                "0.00, \"" +
                emp.getPassword() + "\");";
        } else if (emp.getStatus().equals("Full") || emp.getStatus().equals("Manager") || emp.getStatus().equals("Admin")) {
            sql = "INSERT INTO Employees (EmpID, Name, Wage, Status, Benefits, Password) VALUES (" +
                emp.getEmpID() + ", \"" +
                emp.getName() + "\", " +
                emp.getWage() + ", \"" +
                emp.getStatus() + "\", " +
                emp.getBenefits() + ", \"" +
                emp.getPassword() + "\");";
        } else {
            return;
        }
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            int result = stmt.executeUpdate();
        } catch (SQLException sqlE) {
            
        }
        
    }

    public String getStatus(int empID) {
        String status = "";
        
        try {
            String sql = "SELECT Status FROM Employees WHERE EmpID = " + empID + ";";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            if (result.next()){
                status = result.getString("Status");
            }
        } catch (SQLException sqlE) {
            
        }
        
        return status;
    }

    public boolean idExists(int empID) {
        try {
            String sql = "SELECT * FROM Employees WHERE EmpID = " + empID + ";";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqlE) {
            
        }
        return true;
    }

    public void updateEmployee(FullTimeEmployee emp) {
        
        try {
            String sql = "UPDATE Employees SET " +
                    "Name = \"" + emp.getName() + "\", " + 
                    "Status = \"" + emp.getStatus() + "\", " +
                    "Wage = " + emp.getWage() + ", " +
                    "Benefits = " + emp.getBenefits() + ", " + 
                    "Password = \"" + emp.getPassword() + "\" " +
                    "WHERE EmpID = " + emp.getEmpID() + ";";
            System.out.println(sql);
            PreparedStatement stmt = conn.prepareStatement(sql);
            int result = stmt.executeUpdate();
            System.out.println(result);
        } catch (SQLException sqlE) {
            
        }
    }
    
    public void deleteEmployee(int id) {
        HoursDAO hDAO = new HoursDAO();
        hDAO.deleteAllPunches(id);
        try {
            String sql = "DELETE FROM Employees WHERE EmpID = " + id + ";";
            PreparedStatement stmt = conn.prepareStatement(sql);
            int result = stmt.executeUpdate();
        } catch (SQLException sqlE) {
            
        }
    }

}
