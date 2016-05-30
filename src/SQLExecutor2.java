import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by timzi on 30/05/2016.
 */
public class SQLExecutor2 {
    public static final String GET_OFFICE_EMPLOYEES =   "SELECT mkantoormdw.MKMDWID " +
                                                "FROM funda.wo " +
                                                "INNER JOIN funda.mkantoormdw " +
                                                "ON wo.MKID = mkantoormdw.MKID " +
                                                "WHERE wo.WOID = ? " +
                                                "ORDER BY RAND() " +
                                                "LIMIT 1";

    public static final String GET_HOUSES_WITHOUT_EMPLOYEES = "SELECT wo.WOID " +
            "FROM funda.wo " +
            "WHERE wo.MKMDWID IS NULL ";

    public static final String SET_HOUSE_EMPLOYEE = "UPDATE `funda`.`wo` " +
            "SET " +
            "`MKMDWID` = ? " +
            "WHERE `WOID` = ?";

    private Connector conn;
    public SQLExecutor2(Connector conn){
        this.conn = conn;
    }

    public void setEmployeesHouses() throws SQLException {
        PreparedStatement houses = null;
        PreparedStatement employees = null;
        PreparedStatement update = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Random rand = new Random();

        try {
            houses = conn.getConnection().prepareStatement(GET_HOUSES_WITHOUT_EMPLOYEES);
            rs = houses.executeQuery();
            while (rs.next()) {
                int WOID = rs.getInt("WOID");
                System.out.println(WOID);

                employees = conn.getConnection().prepareStatement(GET_OFFICE_EMPLOYEES);
                employees.setInt(1, WOID);
                rs2 = employees.executeQuery();
                rs2.next();
                int MKMDWID = rs2.getInt("MKMDWID");

                update = conn.getConnection().prepareStatement(SET_HOUSE_EMPLOYEE);
                update.setInt(1, MKMDWID);
                update.setInt(2, WOID);
                update.executeUpdate();

                employees.close();
                rs2.close();
                update.close();
            }
            houses.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (employees != null) {
                employees.close();
            }
            if (houses != null) {
                houses.close();
            }
            if (update != null) {
                update.close();
            }
        }
    }


}
