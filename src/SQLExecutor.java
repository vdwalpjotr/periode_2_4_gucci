import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by peter on 21-May-16.
 */
public class SQLExecutor {
    public static final String INSERT_CUSTOMERS = "INSERT INTO BK.KLANT "
            + "(VOORNAAM, ACHTERNAAM, GESLACHT, MOBIEL_NUMMER, HUISNUMMER, POSTCODE, PLAATS, START_DATUM_CONTRACT, EIND_DATUM_CONTRACT, ABONNEMENT_NAAM, STRAATNAAM) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    private Connector conn;
    public SQLExecutor(Connector conn){
        this.conn = conn;
    }

    public void insertCustomers(){
        int phoneNumber = 16670000;
        int customerID = 2;
        int housenumber = 1;
        conn.connect();
        PreparedStatement prep = null;
        for(int i=0; i<1000; i++) {
            try {
                prep = conn.getConnection().prepareStatement(INSERT_CUSTOMERS);
                prep.setString(1, "Klant-" + customerID);
                prep.setString(2, "achternaam");
                prep.setString(3, String.valueOf("m"));
                prep.setString(4, "06-" + phoneNumber);
                prep.setInt(5, housenumber);
                prep.setString(6, "9741MC");
                prep.setString(7, "Groningen");
                prep.setDate(8, Date.valueOf("2016-04-21"));
                prep.setDate(9, Date.valueOf("2016-03-21"));
                prep.setString(10, "SMS_BUDGET");
                prep.setString(11, "Klantenstraat");

                ResultSet rs = prep.executeQuery();
                String executed_query = rs.getStatement().toString();
                System.out.println("Test: " + executed_query);
                phoneNumber++;
                customerID++;
                housenumber++;


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (prep != null) {
                    try {
                        prep.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        conn.closeConnection();
    }

}
