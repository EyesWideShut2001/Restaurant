package grupa.unu.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RestaurantApplication extends Application {

    private Server tcpServer, webServer;

    @Override
    public void init() throws Exception {
        // 1) TCP server on 9092
        tcpServer = Server.createTcpServer(
                "-tcpAllowOthers",
                "-tcpPort", "9092"
        ).start();

        // 2) Web console on 8082 – browser console
        webServer = Server.createWebServer(
                "-webAllowOthers",
                "-webPort", "8082"
        ).start();
    }


    @Override
    public void start(Stage stage) throws IOException, SQLException {

        Connection connection = RestaurantDb.getConnection();

        FXMLLoader fxmlLoader = new FXMLLoader(RestaurantApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 480);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws SQLException {
        RestaurantDb.getConnection().close();
        if (webServer != null) webServer.stop();
        if (tcpServer != null) tcpServer.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}