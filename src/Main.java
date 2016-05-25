import util.RandomDateGen;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by peter on 21-May-16.
 */
public class Main {

    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.connect();
        SQLExecutor sqlExecutor = new SQLExecutor(connector);
//        Random rand = new Random();
//        RandomDateGen randomDateGen = new RandomDateGen(2016);
//        String randomDate = "";
//
//        for(int i=0; i<1000; i++){
//            int verzender = rand.nextInt(1000)+1019;
//            int ontvanger = rand.nextInt(1000)+1019;
//            int time = rand.nextInt(299)+1;
//            if(verzender == ontvanger){
//                ontvanger++;
//            }
//            randomDate = randomDateGen.getRandomDate();
//            System.out.println(randomDate);
//            sqlExecutor.insertSMSHistorie(verzender, ontvanger, 20, randomDate,"Dit is een voorbeeld smsje");
//            sqlExecutor.insertBelHistorie(verzender, ontvanger, time);
//        }
//        sqlExecutor.customer_call_sms_useage(2, "May");
        try {
//            sqlExecutor.customerPayInvoices(2);
            sqlExecutor.update_saldo(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.closeConnection();


    }
}
