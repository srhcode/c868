package models;

import java.text.ParseException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 Object class for hours log.  
 */
public class Hours {
    
    private int id;
    private int empID;
    private String punchIn;
    private String punchOut;

    public Hours(int id, int empID, String punchIn, String punchOut) {
        this.id = id;
        this.empID = empID;
        this.punchIn = punchIn;
        this.punchOut = punchOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getPunchIn() {
        return punchIn;
    }

    public void setPunchIn(String punchIn) {
        this.punchIn = punchIn;
    }

    public String getPunchOut() {
        return punchOut;
    }

    public void setPunchOut(String punchOut) {
        this.punchOut = punchOut;
    }
    
    public String getTotal(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String roundedTotal = "Error";
        LocalDateTime datePunchIn = LocalDateTime.parse(punchIn, format);
        LocalDateTime datePunchOut = LocalDateTime.parse(punchOut, format);
        DecimalFormat df = new DecimalFormat("0.00");
        float total = (float)Duration.between(datePunchIn, datePunchOut).getSeconds()/(float)3600;
        roundedTotal = df.format(total);
        return roundedTotal;
    }
}
