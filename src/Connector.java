import java.sql.*;

/**
 * Connector class connects to the oracle database
 */
public class Connector {
    public static final String DRIVER_URL = "oracle.jdbc.OracleDriver";
    public static final String DATABASE_URL = "jdbc:oracle:thin:system/oracle@192.168.99.100:49161:xe";
//    public static final String DATABASE_URL = "jdbc:oracle:thin:system/oracle@192.168.178.22:49161:xe";
    private Connection conn;

    public void connect() {
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
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Disconnected");
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    /**
     * Check connection executes a select 1 query on the specific table BK.KLANT
     * Note that this returns true ONLY if BK.KLANT exists.
     * @return False if an SQL execption occurred, True if the connection was valid.
     */
    public boolean checkConnection(){
        boolean connected = false;
        Statement stmt;
        String testQuery = "SELECT 1 FROM BK.KLANT";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(testQuery);
            while(rs.next()) {
                if(rs.getInt(1) == 1){
                   connected = true;
                }
            }
            rs.close();
        } catch (SQLException e) {
        }
        return connected;
    }

    public Connection getConnection(){
        return conn;
    }
}
