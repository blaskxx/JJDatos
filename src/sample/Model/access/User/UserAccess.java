package sample.Model.access.User;

import javafx.collections.ObservableList;
import sample.Model.entities.TableSpace;
import sample.Model.entities.User;
import sample.cr.una.pesistence.access.ORCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoséPablo on 08/10/2014.
 */
public class UserAccess {

    public static ObservableList<User> tableSpaces;

    private static Connection connection;
    private static PreparedStatement pps;

    static {
        try {
            connection = ORCConnection.Instance().getOrcConnection();
            String sql = "select USERNAME,USER_ID,PASSWORD,ACCOUNT_STATUS,LOCK_DATE,EXPIRY_DATE,DEFAULT_TABLESPACE," +
                    "TEMPORARY_TABLESPACE,CREATED,PROFILE,INITIAL_RSRC_CONSUMER_GROUP,EXTERNAL_NAME from DBA_USERS";
            pps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> retrieveTableSpaces() {
        List<User> tableUser = new ArrayList<>();
        try {
            if (ORCConnection.Instance().isInitialized()) {

                ResultSet rs = pps.executeQuery();
                while (rs.next()) {
                    String username = rs.getString(1);
                    Integer user_id = rs.getBigDecimal(2).intValue();
                    String password = rs.getString(3);
                    String account_status = rs.getString(4);
                    String lock_date = rs.getString(5);
                    String expiry_date = rs.getString(6);
                    String default_tablespace = rs.getString(7);
                    String temporary_tablespace= rs.getString(8);
                    String created=rs.getString(9);
                    String profile= rs.getString(10);
                    String inicial_rsrc_customer_group=rs.getString(11);
                    String external_name=rs.getString(12);
                    User tbs = new User(username,user_id,password,account_status,lock_date,expiry_date,default_tablespace,
                            temporary_tablespace,created,profile,inicial_rsrc_customer_group,external_name);
                    tableUser.add(tbs);
                }

                return tableUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}