import oracle.sql.DATE;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Random;

/**
 * Created by peter on 21-May-16.
 */
public class SQLExecutor {
    public static final String INSERT_CUSTOMERS = "INSERT INTO BK.KLANT "
            + "(VOORNAAM, ACHTERNAAM, GESLACHT, MOBIEL_NUMMER, HUISNUMMER, POSTCODE, PLAATS, START_DATUM_CONTRACT, EIND_DATUM_CONTRACT, ABONNEMENT_NAAM, STRAATNAAM) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String INSERT_SMS_HISTORIE = "INSERT INTO BK.SMS_HISTORIE "
            + "(MOBIEL_ZENDER, MOBIEL_ONTVANGER, BERICHT_LENGTE, DATUM_TIJD_VERSTUURD, CODED_SMS) "
            + "VALUES(?, ?, ?, ?, ?)";

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
     * Inserts the sms historie
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

    private String[] getSMSHistorieMobiel(int klantIDZender, int klantIDOntvanger){
        String[] awnser = new String[2];
        Statement stmt;
        String selectPhoneNumbersQuery = "SELECT KLANT_ID, MOBIEL_NUMMER FROM BK.KLANT WHERE KLANT_ID="+klantIDZender
                +" OR KLANT_ID="+klantIDOntvanger;
        System.out.println(selectPhoneNumbersQuery);
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
            System.out.println(awnser[0]);
            System.out.println(awnser[1]);
        }
        return awnser;
    }

}
