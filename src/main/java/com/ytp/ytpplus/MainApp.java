package com.ytp.ytpplus;

import java.io.File;
import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = loader.load();
        FXMLController controller = (FXMLController)loader.getController();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("YTP+");
        stage.getIcons().add(new Image("file:resources/TheKingSq.png"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
        // Load config if there is one.
        File config = new File("config");
        if (config.exists()) {
            try (Scanner in = new Scanner(config)) {
                
                String line = in.nextLine();
                String[] fields = line.split(";");
                controller.getTfFFMPEG().setText(fields[0]);
                controller.getTfFFPROBE().setText(fields[1]);
                controller.getTfMAGICK().setText(fields[2]);
                    
            } catch (Exception e) {
                controller.errorAlert("Failed to read config file: " + e.getMessage());
            }
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
