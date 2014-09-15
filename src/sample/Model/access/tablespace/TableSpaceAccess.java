package sample.Model.access.tablespace;

import javafx.util.Pair;
import sample.Model.entities.TableSpace;
import sample.cr.una.pesistence.access.ORCConnection;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casa on 14/09/2014.
 */
public class TableSpaceAccess {

    public static List<TableSpace> retrieveTableSpaces(){
        List<TableSpace> tableSpaces = new ArrayList<>();
        try{
        if(ORCConnection.Instance().isInitialized()) {
            ResultSet rs = ORCConnection.Instance().executeQuery("SELECT VALUE, END_TIME FROM V$SYSMETRIC WHERE METRIC_ID = 2057");
            while (rs.next()) {
                String name = rs.getString(1);
                float used = rs.getBigDecimal(2).floatValue();
                float free = rs.getBigDecimal(3).floatValue();
                String file = rs.getString(4);
                Boolean auto = rs.getString(5).equals("YES");
                float max = rs.getBigDecimal(6).floatValue();
                float increment = rs.getBigDecimal(7).floatValue();
                float total = rs.getBigDecimal(8).floatValue();
                float pctFree = rs.getBigDecimal(9).floatValue();
                TableSpace tbs = new TableSpace(name,file,auto,total,max,used,free,increment,pctFree);
            }
            return null;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
