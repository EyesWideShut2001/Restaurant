package grupa.unu.restaurant;

import org.h2.tools.Server;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class H2ConsoleServer {
    private static final Logger logger = Logger.getLogger(H2ConsoleServer.class.getName());
    private static Server webServer;
    private static Server tcpServer;

    public static void startServer() {
        try {
            // Start TCP server
            tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
            logger.info("H2 TCP Server started on port 9092");

            // Start web console
            webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            logger.info("H2 Console server is running at http://localhost:8082/");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to start H2 Console server", e);
        }
    }

    public static void stopServer() {
        if (webServer != null) {
            webServer.stop();
            logger.info("H2 Console server stopped");
        }
        if (tcpServer != null) {
            tcpServer.stop();
            logger.info("H2 TCP server stopped");
        }
    }
} 