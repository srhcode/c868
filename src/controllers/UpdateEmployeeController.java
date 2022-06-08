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
  Controller for the Update Employee view.  
 */
public class UpdateEmployeeController implements Initializable {

    int empID;
    String empStatus;
    FullTimeEmployee emp;
    EmployeeDAO empDAO = new EmployeeDAO();
    
    @FXML
    private Button cancelBtn;
    @FXML
    private Button updateEmployeeBtn;
    @FXML
    private TextField empIDTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField wageTxt;
    @FXML
    private TextField benefitsTxt;
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
    private TextField passwordTxt;
    @FXML
    private Label passwordError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> statusList = FXCollections.observableArrayList("Volunteer", "Part", "Full", "Manager", "Admin");
        statusBox.setItems(statusList);
        
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
    private void updateEmployee(ActionEvent event) throws IOException {
        if (checkFields()) {
            emp.setName(nameTxt.getText());
            emp.setStatus(statusBox.getSelectionModel().getSelectedItem());
            emp.setBenefits(Float.parseFloat(benefitsTxt.getText()));
            emp.setWage(Float.parseFloat(wageTxt.getText()));
            emp.setPassword(passwordTxt.getText());
            empDAO.updateEmployee(emp);
            cancel(event);
        }
    }

    @FXML
    private void boxSelected(ActionEvent event) {
        empStatus  = statusBox.getSelectionModel().getSelectedItem();
        wageTxt.setEditable(true);
        benefitsTxt.setEditable(true);
        
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
    }
    
    public void setID(int id) {
        empID = id;
    }
    
    public void setEmployee(FullTimeEmployee employee) {
        emp = employee;
        empIDTxt.setText(Integer.toString(emp.getEmpID()));
        empIDTxt.setEditable(false);
        nameTxt.setText(emp.getName());
        wageTxt.setText(Float.toString(emp.getWage()));
        benefitsTxt.setText(Float.toString(emp.getBenefits()));
        statusBox.getSelectionModel().select(emp.getStatus());
        passwordTxt.setText(emp.getPassword());
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
