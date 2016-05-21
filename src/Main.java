import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by peter on 21-May-16.
 */
public class Main {

    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.connect();
    }
}
