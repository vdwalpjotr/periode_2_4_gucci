import util.RandomDateGen;

import java.io.Console;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by peter on 21-May-16.
 */
public class Main {

    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.connect();
//        SQLExecutor sqlExecutor = new SQLExecutor(connector);
        SQLExecutor2 sqlExecutor2 = new SQLExecutor2(connector);
        //sqlExecutor.insertBankAccounts();
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

//        System.out.println("Welcome,");
//        System.out.println("This console app will give you the history of a certain customer.");
//        boolean correctQuestion = false;
//        while( correctQuestion == false) {
//            Scanner reader = new Scanner(System.in);
//            System.out.println("Please enter the customer_id");
//            int input = reader.nextInt();
//            System.out.println("Which month would you like to find (write in English) :");
//            String month = reader.next();
//            System.out.println("Would you like to find customer: " + input + "\nin the month:" + month + "? \n10(Type 'y' to continue )");
//            String y = reader.next();
//            if (y.equals("y")) {
//                correctQuestion = true;
//                sqlExecutor.customer_call_sms_useage(input, month);
//
//            }
//            else if(y.equals("q")){
//                System.out.println("Goodbye");
//                correctQuestion = true;
//            }
//        }
        try {
            long startTime = System.currentTimeMillis();
            sqlExecutor2.setEmployeesHouses();
            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.println(estimatedTime + " ms");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.closeConnection();


    }
}
