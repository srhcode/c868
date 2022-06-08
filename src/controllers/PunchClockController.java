/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Employee;
import models.EmployeeDAO;
import models.Hours;
import models.HoursDAO;

/**
 * FXML Controller class
 *
 * @author Scott
 */
public class PunchClockController implements Initializable {

    @FXML
    private TableView<Hours> punchTable;
    @FXML
    private TableColumn<Hours, String> punchInCol;
    @FXML
    private TableColumn<Hours, String> punchOutCol;
    @FXML
    private TableColumn<Hours, String> totalCol;
    @FXML
    private Button punchInBtn;
    @FXML
    private Button punchOutBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private RadioButton weekBtn;
    @FXML
    private RadioButton monthBtn;
    @FXML
    private Label messageLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;
    
    private int empID;
    
    ToggleGroup toggle = new ToggleGroup();
    HoursDAO hDAO = new HoursDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            timeLabel.setText(format.format(time));
        }), new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        
        punchInCol.setCellValueFactory(new PropertyValueFactory<>("punchIn"));
        punchOutCol.setCellValueFactory(new PropertyValueFactory<>("punchOut"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        
        weekBtn.setToggleGroup(toggle);
        monthBtn.setToggleGroup(toggle);
        toggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                setTable();
            }
        });
        weekBtn.setSelected(true);
        setTable();
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
                setTable();
            }
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
                setTable();
            }
        } else {
            messageLabel.setText("You are already punched out.");
        }
    }
    
    @FXML
    private void logOut(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    public void setID(int id){
        empID = id;
        EmployeeDAO dao = new EmployeeDAO();
        Employee emp = dao.getEmployee(empID);
        nameLabel.setText("Hello, " + emp.getName() + "!");
        weekBtn.setSelected(true);
        setTable();
    }
    
    public void setTable(){
        RadioButton selected = (RadioButton) toggle.getSelectedToggle();
                if (selected.getText().equals("Week")) {
                    punchTable.setItems(hDAO.getWeek(empID));
                } else {
                    punchTable.setItems(hDAO.getMonth(empID));
                }
    }
}
