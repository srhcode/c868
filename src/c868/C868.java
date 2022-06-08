package c868;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 Main method.  
 */
public class C868 extends Application {

    /**
      Main method to start everything up.  
     */
    public static void main(String[] args) {
        DBConnector.startConnect();
        launch(args);
        DBConnector.closeConnect();
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("C868 Payroll System");
        stage.show();
    }
    
}
