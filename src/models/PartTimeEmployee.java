
package models;

/**
 Object class for a part time employee.  
 */
public class PartTimeEmployee extends Employee {
    
    private float wage;

    public PartTimeEmployee(float wage, String name, String status, String password, int empID) {
        super(empID, name, status, password);
        this.wage = wage;
    }

    public float getWage() {
        return wage;
    }

    public void setWage(float wage) {
        this.wage = wage;
    }
    
    
}
