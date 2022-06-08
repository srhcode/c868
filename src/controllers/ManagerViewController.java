package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.EmployeeDAO;
import models.FullTimeEmployee;
import models.Hours;
import models.HoursDAO;

/**
  Controller for the Manager View.  
 */
public class ManagerViewController implements Initializable {

    @FXML
    private TableView<FullTimeEmployee> empTable;
    @FXML
    private TableColumn<FullTimeEmployee, Integer> empIDCol;
    @FXML
    private TableColumn<FullTimeEmployee, String> nameCol;
    @FXML
    private TableColumn<FullTimeEmployee, String> statusCol;
    @FXML
    private TableColumn<FullTimeEmployee, Float> wageCol;
    @FXML
    private TableColumn<FullTimeEmployee, Float> benefitCol;
    
    @FXML
    private TableView<Hours> hoursTable;
    @FXML
    private TableColumn<Hours, String> punchInCol;
    @FXML
    private TableColumn<Hours, String> punchOutCol;
    
    @FXML
    private Button punchInBtn;
    @FXML
    private Button punchOutBtn;
    @FXML
    private Button addEmployeeBtn;
    @FXML
    private Button updateEmployeeBtn;
    @FXML
    private Button deleteEmployeeBtn;
    @FXML
    private Button updatePunchBtn;
    @FXML
    private Button deletePunchBtn;
    @FXML
    private Button addPunchBtn;
    @FXML
    private Button payrollReportBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Label messageLabel;

    private int empID;
    
    /**
      Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        empIDCol.setCellValueFactory(new PropertyValueFactory<>("empID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        wageCol.setCellValueFactory(new PropertyValueFactory<>("wage"));
        benefitCol.setCellValueFactory(new PropertyValueFactory<>("benefits"));
        
        punchInCol.setCellValueFactory(new PropertyValueFactory<>("punchIn"));
        punchOutCol.setCellValueFactory(new PropertyValueFactory<>("punchOut"));
        
        EmployeeDAO empDAO = new EmployeeDAO();
        HoursDAO hDAO = new HoursDAO();
        
        empTable.setItems(empDAO.getAllEmployees());
        
        empTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            /* Disable Edit, Add, Delete buttons for Punches and Employees if the logged in user is not an admin and selected
                employee is a manager or admin. */
            if (newSelection != null && (newSelection.getStatus().equals("Manager") || newSelection.getStatus().equals("Admin")) && empDAO.getStatus(empID).equals("Manager")) {
                updateEmployeeBtn.setDisable(true);
                deleteEmployeeBtn.setDisable(true);
                updatePunchBtn.setDisable(true);
                deletePunchBtn.setDisable(true);
                addPunchBtn.setDisable(true);
            } else {
                updateEmployeeBtn.setDisable(false);
                deleteEmployeeBtn.setDisable(false);
                updatePunchBtn.setDisable(false);
                deletePunchBtn.setDisable(false);
                addPunchBtn.setDisable(false);
            }
            // Sets the hours table to display the selected employees punch history. 
            if (newSelection != null) {
                hoursTable.setItems(hDAO.getEmpPunches(newSelection.getEmpID()));
            } else {
                hoursTable.setItems(FXCollections.observableArrayList());
            }
        });
        empTable.getSelectionModel().select(0);
    }    

    @FXML
    private void punchIn(ActionEvent event) {
        HoursDAO dao = new HoursDAO();
        if(!dao.isOnTheClock(empID)){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String nowStr = format.format(now);
            dao.punchIn(empID, LocalDateTime.now());
            if (dao.isOnTheClock(empID)) {
                messageLabel.setText("You were punched in at " + nowStr);
            }
            HoursDAO hDAO = new HoursDAO();
            hoursTable.setItems(hDAO.getEmpPunches(empID));
        } else {
            messageLabel.setText("You are already punched in.");
        }
    }

    @FXML
    private void punchOut(ActionEvent event) {
        HoursDAO dao = new HoursDAO();
        if(dao.isOnTheClock(empID)){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String nowStr = format.format(now);
            dao.punchOut(empID, LocalDateTime.now());
            if (!dao.isOnTheClock(empID)){
                messageLabel.setText("You were punched out at " + nowStr);
            }
            HoursDAO hDAO = new HoursDAO();
            hoursTable.setItems(hDAO.getEmpPunches(empID));
        } else {
            messageLabel.setText("You are already punched out.");
        }
    }

    @FXML
    private void addEmployee(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddEmployee.fxml"));
        Parent scene = loader.load();
        AddEmployeeController aec = loader.getController();
        aec.setID(empID);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void updateEmployee(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UpdateEmployee.fxml"));
        Parent scene = loader.load();
        UpdateEmployeeController uec = loader.getController();
        uec.setID(empID);
        uec.setEmployee(empTable.getSelectionModel().getSelectedItem());
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void deleteEmployee(ActionEvent event) {
        FullTimeEmployee emp = empTable.getSelectionModel().getSelectedItem();
        EmployeeDAO empDAO = new EmployeeDAO();
        empDAO.deleteEmployee(emp.getEmpID());
        empTable.setItems(empDAO.getAllEmployees());
        empTable.getSelectionModel().select(0);
    }

    @FXML
    private void updatePunch(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UpdatePunch.fxml"));
        Parent scene = loader.load();
        UpdatePunchController upc = loader.getController();
        upc.setIds(empID, empTable.getSelectionModel().getSelectedItem().getEmpID(), hoursTable.getSelectionModel().getSelectedItem().getId());
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void deletePunch(ActionEvent event) {
        HoursDAO hDAO = new HoursDAO();
        hDAO.deletePunch(hoursTable.getSelectionModel().getSelectedItem().getId());
        hoursTable.setItems(hDAO.getEmpPunches(empTable.getSelectionModel().getSelectedItem().getEmpID()));
    }

    @FXML
    private void addPunch(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddPunch.fxml"));
        Parent scene = loader.load();
        AddPunchController apc = loader.getController();
        apc.setEmpId(empID);
        apc.setId(empTable.getSelectionModel().getSelectedItem().getEmpID());
        apc.setHoursId(-1);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void payrollReport(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PayrollReport.fxml"));
        Parent scene = loader.load();
        PayrollReportController prc = loader.getController();
        prc.setEmpId(empID);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void logOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    void setID(int id) {
        empID = id;
    }
    
}
