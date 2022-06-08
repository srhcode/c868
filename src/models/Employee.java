
package models;

/**
  Basic Employee object class.  
 */
public class Employee {
    
    private int empID;
    private String name;
    private String status;
    private String password;

    public Employee(int empID, String name, String status, String password) {
        this.empID = empID;
        this.name = name;
        this.status = status;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }    
}
