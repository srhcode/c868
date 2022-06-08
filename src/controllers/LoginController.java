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
import models.EmployeeDAO;

/**
  FXML Login View Controller class.
  
  */
public class LoginController implements Initializable {

    @FXML
    TextField empIDTxt;
    @FXML
    TextField passwordTxt;
    @FXML
    Button loginBtn;
    @FXML
    Label errorLabel;
    
    
    /**
     Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void login (ActionEvent event) throws IOException {
        String id = empIDTxt.getText();
        String pw = passwordTxt.getText();
        EmployeeDAO dao = new EmployeeDAO();
        if (dao.checkCredentials(id, pw)) {
            int empID = Integer.parseInt(id);
            if(dao.getEmployee(empID).getStatus().equals("Manager") || dao.getEmployee(empID).getStatus().equals("Admin")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManagerView.fxml"));
                Parent scene = loader.load();
                ManagerViewController mvc = loader.getController();
                mvc.setID(empID);
                Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PunchClock.fxml"));
                Parent scene = loader.load();
                PunchClockController pcc = loader.getController();
                pcc.setID(empID);
                Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } else {
            empIDTxt.setText("");
            passwordTxt.setText("");
            errorLabel.setText("The id or password entered is incorrect.");
        }
    }
}
