import java.sql.*;

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

    public void select_query(int customer_id, String month) {
        conn.connect();

        try {
            PreparedStatement get_id = null;
            String query1 = "SELECT KLANT.MOBIEL_NUMMER " +
                            "FROM BK.KLANT " +
                            "WHERE KLANT.KLANT_ID = ?";

            get_id = conn.getConnection().prepareStatement(query1);
            get_id.setInt(1,customer_id);
            ResultSet klant_set = get_id.executeQuery();
            klant_set.next();

            String phone_number = klant_set.getString("MOBIEL_NUMMER");
            System.out.println(phone_number);

            PreparedStatement count_sms_customer = null;
            String query2 = "SELECT COUNT(*) AS count_sms " +
                            "FROM BK.SMS_HISTORIE " +
                            "WHERE SMS_HISTORIE.MOBIEL_ZENDER = ?";

            count_sms_customer = conn.getConnection().prepareStatement(query2);
            count_sms_customer.setString(1,phone_number);
            ResultSet sms_set = count_sms_customer.executeQuery();
            sms_set.next();

            int count_sms = sms_set.getInt("count_sms");
            conn.getConnection().

            PreparedStatement count_bel_customer = null;
            String query3 = "SELECT " +
                    "  SUM( " +
                    "    ((EXTRACT(DAY FROM (BEL_HISTORIE.EIND_DATUM_TIJD - BEL_HISTORIE.START_DATUM_TIJD)) * (24 * 60)) + " +
                    "    (EXTRACT(HOUR FROM (BEL_HISTORIE.EIND_DATUM_TIJD - BEL_HISTORIE.START_DATUM_TIJD)) * 60) + " +
                    "    EXTRACT(MINUTE FROM (BEL_HISTORIE.EIND_DATUM_TIJD - BEL_HISTORIE.START_DATUM_TIJD))) " +
                    "  ) AS sum_minutes " +
                    "FROM BK.BEL_HISTORIE " +
                    "WHERE BEL_HISTORIE.MOBIEL_ZENDER = ? " +
                    "AND to_char(BEL_HISTORIE.START_DATUM_TIJD, 'Month') LIKE ? " +
                    "AND to_char(BEL_HISTORIE.EIND_DATUM_TIJD, 'Month') LIKE ?";

            count_bel_customer = conn.getConnection().prepareStatement(query3);
            count_bel_customer.setString(1, phone_number);
            count_bel_customer.setString(2, "%" + month + "%");
            count_bel_customer.setString(3, "%" + month + "%");
            ResultSet bel_set = count_bel_customer.executeQuery();
            bel_set.next();

            int count_bel = bel_set.getInt("sum_minutes");

            System.out.println("Klant: " + customer_id + " Maand: " + month);
            System.out.println("Aantal SMS: " + count_sms);
            System.out.println("Aantal Belminuten: " + count_bel);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        conn.closeConnection();
    }

}
