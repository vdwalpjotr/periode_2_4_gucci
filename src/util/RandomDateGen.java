package util;

import oracle.sql.DATE;

import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Class to generate.
 */
public class RandomDateGen {
    public String year;

    /**
     * The year you want to generate the random date for.
     * @param year
     */
    public RandomDateGen(int year){
        this.year = Integer.toString(year);
    }

    /**
     * Returns a random day till 28
     * @return random day.
     */
    private int randomDay(){
        Random rand = new Random();
        return rand.nextInt(28)+1;
    }

    /**
     * Returns a random month
     * @return
     */
    private int randomMonth(){
        Random rand = new Random();
        return rand.nextInt(12)+1;
    }

    /**
     *
     * @return
     */
    public String getRandomDate(){
        return year+"-"+randomMonth()+"-"+randomDay();
    }
}
