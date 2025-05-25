package grupa.unu.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RestaurantApplication extends Application {
    private static final Logger logger = Logger.getLogger(RestaurantApplication.class.getName());
    private Connection connection;

    @Override
    public void init() throws Exception {
        // Start H2 Console server
        H2ConsoleServer.startServer();
        
        // Initialize database connection
        try {
            connection = RestaurantDb.getConnection();
            logger.info("Database connection established");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to initialize database connection", e);
            throw e;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/grupa/unu/restaurant/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Restaurant Management System");
            stage.setScene(scene);
            
            // Set window size to match other views
            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            
            // Center the window on screen
            stage.centerOnScreen();
            
            stage.show();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to start application", e);
            throw e;
        }
    }

    @Override
    public void stop() {
        // Stop H2 Console server
        H2ConsoleServer.stopServer();
        
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}