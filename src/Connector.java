import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connector class connects to the oracle database
 */
public class Connector {
    public static final String DRIVER_URL = "oracle.jdbc.OracleDriver";
    public static final String DATABASE_URL = "jdbc:oracle:thin:system/oracle@192.168.99.100:49161:xe";
    private Connection conn;
    public void connect(){
        try {
            Class.forName(DRIVER_URL);
        } catch (ClassNotFoundException NFE) {
            NFE.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }finally{
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            }catch(SQLException SQLE){
                SQLE.printStackTrace();
            }
        }

    }
}
