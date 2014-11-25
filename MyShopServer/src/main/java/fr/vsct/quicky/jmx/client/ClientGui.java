package fr.vsct.quicky.jmx.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.springframework.core.io.ClassPathResource;

public class ClientGui extends Application {

    public Slider threadSlider;
    public LineChart scenarioLineChart;


    public ClientGui() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(new ClassPathResource("ClientGui.fxml").getURL());

        Scene scene = new Scene(root);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }

}
