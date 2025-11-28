import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB implements Runnable {

    private Connection connection;

    @Override
    public void run() {
        try {
            String url = System.getenv("db_url");
            String user = System.getenv("db_user");
            String password = System.getenv("db_pass");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
