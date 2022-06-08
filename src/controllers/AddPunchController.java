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
  Controller for the AddPunch view. 
 */
public class AddPunchController implements Initializable {

    int empID;
    int id;
    int hoursId;
    
    @FXML
    private TextField timeTxt;
    @FXML
    private TextField dateTxt;
    @FXML
    private Button addPunchBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label timeError;
    @FXML
    private Label dateError;
    
     HoursDAO hDAO = new HoursDAO();
     
    /**
     Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addPunch(ActionEvent event) throws IOException {
        if (validateDateTime()) {
            hDAO.addPunchIn(dateTxt.getText() + " " + timeTxt.getText(), id);
            cancel(event);
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
    
    private boolean validateDateTime() {
        timeError.setText("");
        dateError.setText("");
        boolean valid = true;
        
        String time = timeTxt.getText();
        String date = dateTxt.getText();
        
        if(!time.matches("([01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)")) {
            timeError.setText("Invalid Time.");
            valid = false;
        } else if (!date.matches("^(\\d{4})\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
            dateError.setText("Invalid Date.");
            valid = false;
        } else {
            String dateTime = date + " " + time;
           
            if(!hDAO.validatePunch(dateTime, "in", dateTime, hoursId, id)) {
                timeError.setText("Punch conflicts with existing punch.");
                valid = false;
            }
        }
        
        
        
        return valid;
    }
    
    public void setEmpId(int eId) {
        empID = eId;
    }
    
    public void setId(int newId) {
        id = newId;
    }

    void setHoursId(int hId) {
        hoursId = hId;
    }
}
