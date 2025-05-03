package grupa.unu.restaurant;

import org.h2.tools.RunScript;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantDb {

    private static final String DB_URL = "jdbc:h2:mem:restaurant;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            maybeInitSchema(conn);
        }
        return conn;
    }

    private static void maybeInitSchema(Connection conn) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, "RESTAURANT", null)) {
            if (!rs.next()) {
                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("schema.sql");
                RunScript.execute(conn, new InputStreamReader(in));
            }
        }
    }

}
