package sample.Model.access.tablespace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import sample.Model.entities.TableSpace;
import sample.cr.una.pesistence.access.ORCConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casa on 14/09/2014.
 */
public class TableSpaceAccess {
    public static ObservableList<TableSpace> tableSpaces;

   // public static ObservableList<TableSpace> getTableSpaces() {
      //  return tableSpaces;
    //}
    private static Connection connection;
    private static PreparedStatement pps;

    static {
        try {
            connection = ORCConnection.Instance().getOrcConnection();
            String sql = "select df.tablespace_name \"Tablespace\",totalusedspace \"Used MB\",(df.totalspace - tu.totalusedspace) \"Free MB\", df.MAXBYTES \"MAX\", df.totalspace \"Total MB\",round(100 * ( (df.totalspace - tu.totalusedspace)/ df.totalspace)) \"Pct. Free\" from (select tablespace_name,SUM(MAXBYTES) MAXBYTES, round(sum(bytes) / 1048576) TotalSpace from dba_data_files group by tablespace_name,AUTOEXTENSIBLE) df,(select round(sum(bytes)/(1024*1024)) totalusedspace, tablespace_name from dba_segments group by tablespace_name) tu  where df.tablespace_name = tu.tablespace_name";
            pps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   // public static void initializate(){
     //   tableSpaces = FXCollections.observableList(retrieveTableSpaces());
    //}
    public static List<TableSpace> retrieveTableSpaces(){
        List<TableSpace> tableSpaces = new ArrayList<>();
        try{
        if(ORCConnection.Instance().isInitialized()) {

            ResultSet rs =pps.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                float used = rs.getBigDecimal(2).floatValue();
                float free = rs.getBigDecimal(3).floatValue();
               // String file = rs.getString(4);
                //Boolean auto = rs.getString(4).equals("YES");
                float max = rs.getBigDecimal(4).floatValue();
                //float increment = rs.getBigDecimal(6).floatValue();
                float total = rs.getBigDecimal(5).floatValue();
                float pctFree = rs.getBigDecimal(6).floatValue();
                TableSpace tbs = new TableSpace(name,true,total,max,used,free,0.0f,pctFree);
                tableSpaces.add(tbs);
            }
            //rs.close();
            return tableSpaces;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
