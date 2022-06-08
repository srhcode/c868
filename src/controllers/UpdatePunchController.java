/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.HoursDAO;

/**
 * FXML Controller class
 *
 * @author Scott
 */
public class UpdatePunchController implements Initializable {

    int empID;
    int hoursId;
    int id;
    
    
    @FXML
    private TextField inTimeTxt;
    @FXML
    private TextField inDateTxt;
    @FXML
    private Button updatePunchBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label inTimeError;
    @FXML
    private Label inDateError;
    @FXML
    private TextField outTimeTxt;
    @FXML
    private TextField outDateTxt;
    @FXML
    private Label outTimeError;
    @FXML
    private Label outDateError;

    HoursDAO hDAO = new HoursDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void updatePunch(ActionEvent event) throws IOException {
        if (validateDateTimeIn() && validateDateTimeOut()) {
            String inTime = inDateTxt.getText() + " " + inTimeTxt.getText();
            String outTime = "";
            if (!outDateTxt.equals("") && !outTimeTxt.equals("")) {
                outTime = outDateTxt.getText() + " " + outTimeTxt.getText();
            }
            hDAO.updatePunch(inTime, outTime, hoursId);
            cancel(event);
        } else {
            System.out.println("Not Valid");
        }
        
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManagerView.fxml"));
        Parent scene = loader.load();
        ManagerViewController mvc = loader.getController();
        mvc.setID(empID);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    private boolean validateDateTimeIn() {
        inTimeError.setText("");
        inDateError.setText("");
        boolean valid = true;
        
        String time = inTimeTxt.getText();
        String date = inDateTxt.getText();
        
        if(!time.matches("([01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)")) {
            inTimeError.setText("Invalid Time.");
            valid = false;
        } else if (!date.matches("^(\\d{4})\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
            inDateError.setText("Invalid Date.");
            valid = false;
        } else {
            String dateTime = date + " " + time;
           
            if(!hDAO.validatePunch(dateTime, "in", dateTime, hoursId, id)) {
                inTimeError.setText("Punch conflicts with existing punch.");
                valid = false;
            }
        }
        
        return valid;
    }
    
    private boolean validateDateTimeOut() {
        outTimeError.setText("");
        outDateError.setText("");
        boolean valid = true;
        
        String time = outTimeTxt.getText();
        String date = outDateTxt.getText();
        
        if(!time.matches("([01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)") && (!time.equals("") && !date.equals(""))) {
            outTimeError.setText("Invalid Time.");
            valid = false;
        } else if (!date.matches("^(\\d{4})\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$") && (!time.equals("") && !date.equals(""))) {
            outDateError.setText("Invalid Date.");
            valid = false;
        } else {
            String dateTime = date + " " + time;
            if (validateDateTimeIn()) {
                String timeIn = inTimeTxt.getText();
                String dateIn = inDateTxt.getText();
                String dateTimeIn = dateIn + " " + timeIn;
                if(!hDAO.validatePunch(dateTime, "out", dateTimeIn, hoursId, id)) {
                    outTimeError.setText("Punch conflicts with existing punch.");
                    valid = false;
                }
            }
                    }
        
        return valid;
    }
    
    public void setIds(int eId, int newId, int hId) {
        empID = eId;
        hoursId = hId;
        id = newId;
        String[] dateTimeOut = new String[2];
        String[] dateTimeIn = hDAO.getPunch(hoursId).getPunchIn().split(" ");
        String hOut = hDAO.getPunch(hoursId).getPunchOut();
        if (hOut != null && !hOut.equals("")) {
            dateTimeOut = hDAO.getPunch(hoursId).getPunchOut().split(" ");
        } else {
            dateTimeOut[0] = "";
            dateTimeOut[1] = "";
        }
        inTimeTxt.setText(dateTimeIn[1]);
        inDateTxt.setText(dateTimeIn[0]);
        outDateTxt.setText(dateTimeOut[0]);
        outTimeTxt.setText(dateTimeOut[1]);
    }
}
