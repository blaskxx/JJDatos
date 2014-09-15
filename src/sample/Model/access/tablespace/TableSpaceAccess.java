package sample.Model.access.tablespace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public static ObservableList<TableSpace> tableSpaces;

    public static ObservableList<TableSpace> getTableSpaces() {
        return tableSpaces;
    }

    static {

    }
    public static void initializate(){
        tableSpaces = FXCollections.observableList(retrieveTableSpaces());
    }
    public static List<TableSpace> retrieveTableSpaces(){
        List<TableSpace> tableSpaces = new ArrayList<>();
        try{
        if(ORCConnection.Instance().isInitialized()) {
            String sql = "select df.tablespace_name \"Tablespace\",totalusedspace \"Used MB\",(df.totalspace - tu.totalusedspace) \"Free MB\", df.file_name \"File name\",df.AUTOEXTENSIBLE \"Auto\", df.MAXBYTES \"MAX\", df.INCREMENT_BY \"grow\",df.totalspace \"Total MB\",round(100 * ( (df.totalspace - tu.totalusedspace)/ df.totalspace)) \"Pct. Free\"from (select tablespace_name,file_name,AUTOEXTENSIBLE,MAXBYTES,INCREMENT_BY, round(sum(bytes) / 1048576) TotalSpace from dba_data_files group by tablespace_name,file_name,AUTOEXTENSIBLE,MAXBYTES,INCREMENT_BY) df,(select round(sum(bytes)/(1024*1024)) totalusedspace, tablespace_name from dba_segments group by tablespace_name) tu  where df.tablespace_name = tu.tablespace_name";
            ResultSet rs = ORCConnection.Instance().executeQuery(sql);
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
