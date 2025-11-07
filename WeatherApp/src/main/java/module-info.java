module edu.westga.cs6241.WeatherApp {
    requires javafx.controls;
    requires javafx.fxml;
	requires org.apache.commons.csv;

    opens edu.westga.cs6241.WeatherApp to javafx.fxml, javafx.controls;
    exports edu.westga.cs6241.WeatherApp;
}
