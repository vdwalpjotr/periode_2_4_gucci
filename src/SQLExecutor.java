import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by peter on 21-May-16.
 */
public class SQLExecutor {
    public static final String INSERT_CUSTOMERS = "INSERT INTO BK.KLANT "
            + "(VOORNAAM, ACHTERNAAM, GESLACHT, MOBIEL_NUMMER, HUISNUMMER, POSTCODE, PLAATS, START_DATUM_CONTRACT, EIND_DATUM_CONTRACT, ABONNEMENT_NAAM) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String INSERT_CUSTOMERS_TEST = "INSERT INTO BK.KLANT "
            + "(VOORNAAM, ACHTERNAAM, GESLACHT, STRAATNAAM, HUISNUMMER, POSTCODE, "
            + "MOBIEL_NUMMER, PLAATS, START_DATUM_CONTRACT, EIND_DATUM_CONTRACT, ABONNEMENT_NAAM) "
            + "VALUES( 'Peter', 'van, 'm' , ?, ?, ?, ?, ?, ?) ;";

    private Connector conn;
    public SQLExecutor(Connector conn){
        this.conn = conn;
    }

    public void insertCustomers(){
        conn.connect();
        PreparedStatement prep = null;
        try {
            prep = conn.getConnection().prepareStatement(INSERT_CUSTOMERS);
            prep.setString(1, "Henk");
            prep.setString(2, "deVries");
            prep.setString(3, String.valueOf("m"));
            prep.setString(4, "05134528917");
            prep.setInt(5, 442);
            prep.setString(6, "9741MC");
            prep.setString(7, "Groningen");
            prep.setDate(8,  Date.valueOf("2016-04-21"));
            prep.setDate(9, Date.valueOf("2016-03-21"));
            prep.setString(10, "SMS_BUDGET");

//            prep.setString(1, "TestKlant");
//            prep.setString(2, "Achternaam");
//            //prep.setString(3, String.valueOf('m'));
//            prep.setString(3, "TestStraat");
//            prep.setInt(4, 1);
//            prep.setString(5, "9741MC");
//            prep.setString(6, "0616675928");
//            prep.setString(7, "Groningen");
//           // prep.setDate(8, Date.valueOf("2016-04-21"));
//           // prep.setDate(9, Date.valueOf("2016-05-21"));
//            prep.setString(8, "SMS_BUDGET");
//            System.out.println(prep.toString());
            ResultSet rs = prep.executeQuery();
            String executed_query = rs.getStatement().toString();
            System.out.println("Test: " + executed_query);


        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            if(prep != null){
                try {
                    prep.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            conn.closeConnection();
        }

    }

}
