import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectDB{
    public Connection connectDB(){
        String url=System.getenv("db_url");
        String user=System.getenv("db_user");
        String password=System.getenv("db_pass");
        try{
            Connection conn= DriverManager.getConnection(url,user,password);
            return conn;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

}
