package models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;

/**
 Object class for payroll entries. 
 */
public class Payroll {
    
    private int empId;
    private String name;
    private float wage;
    private float benefits;
    private float hours;

    public Payroll(int empId, String name, float wage, float benefits) {
        this.empId = empId;
        this.name = name;
        this.wage = wage;
        this.benefits = benefits;
    }

    public BigDecimal getBenefits() {
        return convertToBD(benefits);
    }

    public void setBenefits(float benefits) {
        this.benefits = benefits;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWage() {
        return convertToBD(wage);
    }

    public void setWage(float wage) {
        this.wage = wage;
    }
 
    public void incrementHours(float hrs) {
        this.hours += hrs;
    }
    
    public BigDecimal getHours() {
        accumulateHours();
        return convertToBD(this.hours);
    }
    
    public void accumulateHours() {
        this.hours = 0;
        HoursDAO hDAO = new HoursDAO();
        ObservableList<Hours> punches = hDAO.getEmpPunches(empId);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        punches.forEach((punch) -> {
            if (punch.getPunchOut() != null && punch.getPunchOut() != "") {
                LocalDateTime in = LocalDateTime.parse(punch.getPunchIn(), format);
                LocalDateTime out = LocalDateTime.parse(punch.getPunchOut(), format);
                float difference = (float)Duration.between(in, out).getSeconds()/(float)3600;
                incrementHours(difference);
            }
            
        });
        
    }
    
    public BigDecimal getTotal() {
        accumulateHours();
        return getHours().multiply(getWage()).subtract(getBenefits()).setScale(2, RoundingMode.HALF_EVEN);
    }
    
    private BigDecimal convertToBD(float num) {
        BigDecimal bd = new BigDecimal(Float.toString(num));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
