package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.EmployeeDAO;
import models.Payroll;
import models.PayrollDAO;

/**
  Payroll Report view controller. 
 */
public class PayrollReportController implements Initializable {

    int empId;
    
    @FXML
    private Button managerPanelBtn;
    @FXML
    private TableView<Payroll> payrollTable;
    @FXML
    private TableColumn<Payroll, Integer> empIdCol;
    @FXML
    private TableColumn<Payroll, String> nameCol;
    @FXML
    private TableColumn<Payroll, BigDecimal> wageCol;
    @FXML
    private TableColumn<Payroll, BigDecimal> benefitsCol;
    @FXML
    private TableColumn<Payroll, BigDecimal> hoursCol;
    @FXML
    private TableColumn<Payroll, BigDecimal> totalCol;

    PayrollDAO payroll = new PayrollDAO();
    
    /**
      Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("empId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        wageCol.setCellValueFactory(new PropertyValueFactory<>("wage"));
        benefitsCol.setCellValueFactory(new PropertyValueFactory<>("benefits"));
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("hours"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
       
        payrollTable.setItems(payroll.getPayroll());
    }    

    @FXML
    private void managerPanel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManagerView.fxml"));
        Parent scene = loader.load();
        ManagerViewController mvc = loader.getController();
        mvc.setID(empId);
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    void setEmpId(int id) {
        empId = id;
    }
    
}
