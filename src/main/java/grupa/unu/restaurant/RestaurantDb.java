package grupa.unu.restaurant;

import org.h2.tools.RunScript;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestaurantDb {
    private static final Logger logger = Logger.getLogger(RestaurantDb.class.getName());
    private static final String DB_URL = "jdbc:h2:./data/restaurant;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "H2 JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        
        // Check if the schema needs to be initialized
        try (ResultSet rs = conn.getMetaData().getTables(null, null, "MANAGER", null)) {
            if (!rs.next()) {
                // Tables don't exist, initialize schema
                initSchema(conn);
            }
        }
        
        return conn;
    }

    private static void initSchema(Connection conn) throws SQLException {
        logger.info("Initializing database schema...");
        try (InputStream in = RestaurantDb.class.getResourceAsStream("/grupa/unu/restaurant/schema.sql")) {
            if (in == null) {
                logger.severe("Could not find schema.sql file");
                throw new SQLException("Could not find schema.sql file");
            }
            try (InputStreamReader reader = new InputStreamReader(in)) {
                RunScript.execute(conn, reader);
                logger.info("Schema initialized successfully");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading schema.sql", e);
            throw new SQLException("Error reading schema.sql", e);
        }
    }
}
