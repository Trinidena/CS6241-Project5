package edu.westga.cs6241.WeatherApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/edu/westga/cs6241/WeatherApp/ForecastView.fxml"));

        BorderPane root = loader.load();

        Scene scene = new Scene(root, 700, 540);
        stage.setTitle("CS6241 Weather – 7-Day Forecast");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
