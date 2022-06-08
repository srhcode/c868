package controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.EmployeeDAO;
import models.FullTimeEmployee;

/**
  Controller for Add Employee view.  
 */
public class AddEmployeeController implements Initializable {

    int empID;
    String empStatus= "SELECT STATUS";
    
    @FXML
    private Button cancelBtn;
    @FXML
    private Button addEmployeeBtn;
    @FXML
    private TextField empIDTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField wageTxt;
    @FXML
    private TextField benefitsTxt;
    @FXML
    private TextField passwordTxt;
    @FXML
    private ComboBox<String> statusBox;
    @FXML
    private Label empIDError;
    @FXML
    private Label nameError;
    @FXML
    private Label statusError;
    @FXML
    private Label wageError;
    @FXML
    private Label benefitsError;
    @FXML
    private Label passwordError;
    
    EmployeeDAO empDAO = new EmployeeDAO();
    
    /**
      Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> statusList = FXCollections.observableArrayList("Volunteer", "Part", "Full", "Manager", "Admin");
        statusBox.setItems(statusList);
        
    }    

    @FXML
    private void boxSelected() {
        empStatus  = statusBox.getSelectionModel().getSelectedItem();
        wageTxt.setEditable(true);
        benefitsTxt.setEditable(true);
        addEmployeeBtn.setDisable(false);
        
        if (empStatus.equals("Volunteer")) {
            wageTxt.setText("0.00");
            wageTxt.setEditable(false);
            benefitsTxt.setText("0.00");
            benefitsTxt.setEditable(false);
        }
        if(empStatus.equals("Part")) {
            benefitsTxt.setText("0.00");
            benefitsTxt.setEditable(false);
        }
        
        if(!empDAO.getStatus(empID).equals("Admin") && (empStatus.equals("Manager") || empStatus.equals("Admin"))) {
            addEmployeeBtn.setDisable(true);
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

    @FXML
    private void addEmployee(ActionEvent event) throws IOException {
        if (checkFields()) {
            FullTimeEmployee emp = new FullTimeEmployee(
                    Float.parseFloat(benefitsTxt.getText()),
                    Float.parseFloat(wageTxt.getText()),
                    nameTxt.getText(),
                    empStatus,
                    Integer.parseInt(empIDTxt.getText()),
                    passwordTxt.getText()
            );
            empDAO.addEmployee(emp);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManagerView.fxml"));
            Parent scene = loader.load();
            ManagerViewController mvc = loader.getController();
            mvc.setID(empID);
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    public void setID (int id) {
        empID = id;
    }

    private boolean checkFields() {
        boolean fieldsGood = true;
        nameError.setText("");
        empIDError.setText("");
        statusError.setText("");
        wageError.setText("");
        benefitsError.setText("");
        passwordError.setText("");
        if (!nameTxt.getText().matches(".*[a-zA-Z]+.*")) {
            nameError.setText("The employee name must contain at least one letter.");
            fieldsGood = false;
        }
        if (!empIDTxt.getText().matches("\\d+")) {
            fieldsGood = false;
            empIDError.setText("The employee ID may only containt numbers.");
        } else if (empDAO.idExists(Integer.parseInt(empIDTxt.getText()))) {
            fieldsGood = false;
            empIDError.setText("This employee ID is taken.  Please choose another.");
        }
        if (statusBox.getSelectionModel().getSelectedIndex() < 0) {
            fieldsGood = false;
            statusError.setText("Please select an employee status.");
        }
        if (!wageTxt.getText().matches("^[0-9]+(\\.[0-9]{2})$")) {
            fieldsGood = false;
            wageError.setText("The wage must be only numbers and follow the #.## format.");
        }
        if (!benefitsTxt.getText().matches("^[0-9]+(\\.[0-9]{2})$")) {
            fieldsGood = false;
            benefitsError.setText("The benefits must be only numbers and follow the #.## format.");
        }
        if (passwordTxt.getText().matches("^$|\\s+")) {
            fieldsGood = false;
            passwordError.setText("Password cannot be blank.");
        }
        return fieldsGood;
    }
}
