import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.Random;

public class SQLExecutor {
    public static final String INSERT_CUSTOMERS = "INSERT INTO BK.KLANT "
            + "(VOORNAAM, ACHTERNAAM, GESLACHT, MOBIEL_NUMMER, HUISNUMMER, POSTCODE, PLAATS, START_DATUM_CONTRACT, EIND_DATUM_CONTRACT, ABONNEMENT_NAAM, STRAATNAAM) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String INSERT_SMS_HISTORIE = "INSERT INTO BK.SMS_HISTORIE "
            + "(MOBIEL_ZENDER, MOBIEL_ONTVANGER, BERICHT_LENGTE, DATUM_TIJD_VERSTUURD, CODED_SMS) "
            + "VALUES(?, ?, ?, ?, ?)";
    public static final String INSERT_BEL_HISTORIE = "INSERT INTO BK.BEL_HISTORIE "
            + "(MOBIEL_ZENDER, MOBIEL_ONTVANGER, START_DATUM_TIJD, EIND_DATUM_TIJD) "
            + "VALUES(?,?,?,?)";

    public static final String CUSTORMER_PHONE_NUMBER = "SELECT KLANT.MOBIEL_NUMMER " +
            "FROM BK.KLANT " +
            "WHERE KLANT.KLANT_ID = ?";

    public static final String CUSTOMER_COUNT_SMS = "SELECT COUNT(*) AS count_sms " +
            "FROM BK.SMS_HISTORIE " +
            "WHERE SMS_HISTORIE.MOBIEL_ZENDER = ? " +
            "AND to_char(SMS_HISTORIE.DATUM_TIJD_VERSTUURD, 'Month') LIKE ?";

    public static final String CUSTOMER_CALL_MINUTES = "SELECT " +
            "  SUM( " +
            "    ((EXTRACT(DAY FROM (BEL_HISTORIE.EIND_DATUM_TIJD - BEL_HISTORIE.START_DATUM_TIJD)) * (24 * 60)) + " +
            "    (EXTRACT(HOUR FROM (BEL_HISTORIE.EIND_DATUM_TIJD - BEL_HISTORIE.START_DATUM_TIJD)) * 60) + " +
            "    EXTRACT(MINUTE FROM (BEL_HISTORIE.EIND_DATUM_TIJD - BEL_HISTORIE.START_DATUM_TIJD))) " +
            "  ) AS sum_minutes " +
            "FROM BK.BEL_HISTORIE " +
            "WHERE BEL_HISTORIE.MOBIEL_ZENDER = ? " +
            "AND to_char(BEL_HISTORIE.START_DATUM_TIJD, 'Month') LIKE ? " +
            "AND to_char(BEL_HISTORIE.EIND_DATUM_TIJD, 'Month') LIKE ?";

    public static final String CUSTOMER_GET_INVOICES = "SELECT FACTUUR.FACTUUR_ID, FACTUUR.TOTAAL_BEDRAG " +
            "FROM BK.FACTUUR " +
            "WHERE FACTUUR.KLANT_ID = ? " +
            "AND FACTUUR.STATUS LIKE ?";

    public static final String CUSTOMER_UPDATE_SALDO = "UPDATE BK.BANK " +
            "SET SALDO = SALDO - ? " +
            "WHERE BANK.KLANT_ID = ?";

    public static final String UPDATE_INVOICE_STATUS = "UPDATE BK.FACTUUR " +
            "SET STATUS = ? " +
            "WHERE FACTUUR.FACTUUR_ID = ?";

    private Connector conn;
    public SQLExecutor(Connector conn){
        this.conn = conn;
    }

    /**
     * Insercustomer inserts customers into database with table BK.KLANT
     *
     */
    public void insertCustomers(){
        int phoneNumber = 16670000;
        int customerID = 2;
        int housenumber = 1;
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
                prep.setDate(8, Date.valueOf("2016-01-21"));
                prep.setDate(9, Date.valueOf("2016-04-21"));
                prep.setString(10, "SMS_BUDGET");
                prep.setString(11, "Klantenstraat");
                prep.executeQuery();


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

    /**
     * Inserts the sms historie using the customer IDs
     * @param klantIDZender
     * @param klantIDOntvanger
     * @param berichtLengte
     * @param datumVerstuurd
     * @param codedSMS
     */
    public void insertSMSHistorie(int klantIDZender, int klantIDOntvanger, int berichtLengte, String datumVerstuurd, String codedSMS)
    {

        String[] mobielNummers = getSMSHistorieMobiel(klantIDZender, klantIDOntvanger);
        String mobielZender = mobielNummers[0];
        String mobielOntvanger = mobielNummers[1];
        try{
            PreparedStatement insertSMS = conn.getConnection().prepareStatement(INSERT_SMS_HISTORIE);
            insertSMS.setString(1, mobielZender);
            insertSMS.setString(2, mobielOntvanger);
            insertSMS.setInt(3, berichtLengte);
            insertSMS.setDate(4, Date.valueOf(datumVerstuurd));
            insertSMS.setString(5, codedSMS);
            insertSMS.executeUpdate();
            insertSMS.close();
        }catch(SQLException SQL){
            SQL.printStackTrace();
        }
    }

    public void insertBelHistorie(int klantIDZender,int klantIDOntvanger, int conversationLength ){
        String[] mobielNummers = getSMSHistorieMobiel(klantIDZender, klantIDOntvanger);
        String mobielZender = mobielNummers[0];
        String mobielOntvanger = mobielNummers[1];
        long current_time = System.currentTimeMillis();
        long end_time = current_time + (conversationLength *1000);


        try{
            PreparedStatement insertBel = conn.getConnection().prepareStatement(INSERT_BEL_HISTORIE);
            insertBel.setString(1, mobielZender);
            insertBel.setString(2, mobielOntvanger);
            insertBel.setTimestamp(3, new Timestamp(current_time));
            insertBel.setTimestamp(4, new Timestamp(end_time));
            insertBel.executeUpdate();
            insertBel.close();
        }catch(SQLException SQL){
            SQL.printStackTrace();
        }
    }

    /**
     * Returns a random numberbetween the minimal and the maximum given
     * @param min the lower bound of the random number
     * @param max the upper bound of the random number
     * @return
     */
    private int getRandomNumber(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max) + min;

    }

    /**
     * Returns mobile numbers of 2 phonenumbers.
     * @param klantIDZender
     * @param klantIDOntvanger
     * @return
     */
    private String[] getSMSHistorieMobiel(int klantIDZender, int klantIDOntvanger){
        String[] awnser = new String[2];
        Statement stmt;
        String selectPhoneNumbersQuery = "SELECT KLANT_ID, MOBIEL_NUMMER FROM BK.KLANT WHERE KLANT_ID="+klantIDZender
                +" OR KLANT_ID="+klantIDOntvanger;

        ResultSet rs;
        try{
            stmt = conn.getConnection().createStatement();
            rs = stmt.executeQuery(selectPhoneNumbersQuery);
            while(rs.next()) {
                if (rs.getInt("KLANT_ID") == klantIDZender) {
                    awnser[0] = rs.getString("MOBIEL_NUMMER");
                }
                if (rs.getInt("KLANT_ID") == klantIDOntvanger) {
                    awnser[1] = rs.getString("MOBIEL_NUMMER");
                }
            }
            rs.close();
            stmt.close();
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        }finally
        {

        }
        return awnser;
    }

    /**
     * Calculates and prints the number of sms messages send and minutes called by the customer in a specific month.
     * @param customer_id
     * @param month         month for the amount
     */
    public void customer_call_sms_useage(int customer_id, String month) {
//        conn.connect();

        String phone_number = customerPhoneNumber(customer_id);
        int count_sms = countMessages(phone_number, month);
        int count_bel = countCallMinutes(phone_number, month);

        System.out.println("Klant: " + customer_id + " | Maand: " + month);
        System.out.println("-----------------------");
        System.out.println("Aantal SMS: " + count_sms);
        System.out.println("Aantal Belminuten: " + count_bel);
        System.out.println("-----------------------");

//        conn.closeConnection();
    }

    /**
     * Returns the customers phone number.
     * @param customer_id
     * @return  customers phone number
     */
    public String customerPhoneNumber(int customer_id) {
        String phone_number = new String();
        try {
            PreparedStatement get_id = null;
            get_id = conn.getConnection().prepareStatement(CUSTORMER_PHONE_NUMBER);
            get_id.setInt(1,customer_id);
            ResultSet klant_set = get_id.executeQuery();
            klant_set.next();

            phone_number = klant_set.getString("MOBIEL_NUMMER");
            get_id.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phone_number;
    }

    /**
     * Returns number of sms messages send by the customer.
     * @param phone_number
     * @param month
     * @return              number of messages
     */
    public int countMessages(String phone_number, String month) {
        int count_sms = 0;
        try {
            PreparedStatement count_sms_customer = null;
            count_sms_customer = conn.getConnection().prepareStatement(CUSTOMER_COUNT_SMS);
            count_sms_customer.setString(1,phone_number);
            count_sms_customer.setString(2, "%" + month + "%");
            ResultSet sms_set = count_sms_customer.executeQuery();
            sms_set.next();

            count_sms = sms_set.getInt("count_sms");
            count_sms_customer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count_sms;
    }

    /**
     * Returns the minutes called by the customer.
     * @param phone_number
     * @param month
     * @return              number of minutes
     */
    public int countCallMinutes(String phone_number, String month) {
        int count_call = 0;
        try {
            PreparedStatement count_bel_customer = null;
            count_bel_customer = conn.getConnection().prepareStatement(CUSTOMER_CALL_MINUTES);
            count_bel_customer.setString(1, phone_number);
            count_bel_customer.setString(2, "%" + month + "%");
            count_bel_customer.setString(3, "%" + month + "%");
            ResultSet bel_set = count_bel_customer.executeQuery();
            bel_set.next();

            count_call = bel_set.getInt("sum_minutes");
            count_bel_customer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count_call;
    }

    public void customerPayInvoices(int customer_id) throws SQLException {
        PreparedStatement get_invoices = null;
        PreparedStatement update_saldo = null;
        PreparedStatement update_invoice = null;
        ResultSet invoices = null;
        try {
            conn.getConnection().setAutoCommit(false);

            get_invoices = conn.getConnection().prepareStatement(CUSTOMER_GET_INVOICES);
            get_invoices.setInt(1, customer_id);
            get_invoices.setString(2,"Te betalen");
            invoices = get_invoices.executeQuery();
            while (invoices.next()) {
                int invoice_id = invoices.getInt("FACTUUR_ID");
                double total_costs = invoices.getDouble("TOTAAL_BEDRAG");
                System.out.println(invoice_id + " | " + total_costs);

                update_saldo = conn.getConnection().prepareStatement(CUSTOMER_UPDATE_SALDO);
                update_saldo.setDouble(1, total_costs);
                update_saldo.setInt(2, customer_id);
                update_saldo.executeUpdate();

                update_invoice = conn.getConnection().prepareStatement(UPDATE_INVOICE_STATUS);
                update_invoice.setString(1, "Betaalt");
                update_invoice.setInt(2, invoice_id);
                update_invoice.executeUpdate();

                conn.getConnection().commit();
                update_saldo.close();
                System.out.println("Betaalt");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            conn.getConnection().rollback();
        } finally {
            if (get_invoices != null) {
                get_invoices.close();
            }
            if (invoices != null) {
                invoices.close();
            }
            if (update_saldo != null) {
                update_saldo.close();
            }

            conn.getConnection().setAutoCommit(true);
        }
    }

    public void update_saldo(int customer_id) throws SQLException {
        PreparedStatement update_saldo = null;
        try {
            update_saldo = conn.getConnection().prepareStatement(CUSTOMER_UPDATE_SALDO);
            update_saldo.setDouble(1, 100);
            update_saldo.setInt(2, customer_id);
            update_saldo.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            update_saldo.close();
        }
    }
}
