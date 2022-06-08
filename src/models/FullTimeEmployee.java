package models;

/**
 Object class for a full time employee.  
 */
public class FullTimeEmployee extends Employee {
    
    private float benefits;
    private float wage;

    public FullTimeEmployee(float benefits, float wage, String name, String status, int empID, String password) {
        super(empID, name, status, password);
        this.benefits = benefits;
        this.wage = wage;
    }

    public float getBenefits() {
        return benefits;
    }

    public void setBenefits(float benefits) {
        this.benefits = benefits;
    }

    public float getWage() {
        return wage;
    }

    public void setWage(float wage) {
        this.wage = wage;
    }
    
    
}
