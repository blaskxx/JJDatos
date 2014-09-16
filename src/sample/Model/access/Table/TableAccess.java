package sample.Model.access.Table;

import sample.Model.access.tablespace.TableSpaceAccess;
import sample.Model.entities.Table;
import sample.Model.entities.TableSpace;
import sample.cr.una.pesistence.access.ORCConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Casa on 15/09/2014.
 */
public class TableAccess {
    private static TableAccess instance=null;
    public static TableAccess Instance(){
        if(instance==null) instance=new TableAccess();
        return instance;
    }
    protected TableAccess(){

    }
    public List<Table> retrieveForTableSpace(String TBS_name){
        ORCConnection connection = ORCConnection.Instance();
        List<Table> tables = new ArrayList<>();
        if(!connection.isInitialized()) return tables;
        try{
            String sql = "select dbs.segment_name,dbs.bytes/1024/1024 MB, allt.TBS from dba_segments dbs,(SELECT TABLE_NAME \"TN\",TABLESPACE_NAME \"TBS\" FROM ALL_TABLES) allt where segment_type='TABLE' and segment_name=allt.TN AND allt.TBS='"+TBS_name+"'";
            ResultSet rs = connection.executeQuery(sql);
            while (rs.next()){
                String tableName = rs.getString(1);
                float size = rs.getBigDecimal(2).floatValue();
                String tbs_name = rs.getString(3);
                Table tmp = new Table(tableName,size,tbs_name);
                tables.add(tmp);
            }
            return tables;
        } catch (SQLException e) {
           // e.printStackTrace();
            return null;
        }
    }
    public List<Table> retrieveForTableSpace(TableSpace TBS_name){
        return retrieveForTableSpace(TBS_name.getName());
    }
    public Map<TableSpace,List<Table>> retriveAllTables(){
        Map<TableSpace,List<Table>> allTables = new HashMap<>();
        List<TableSpace> tableSpaces = TableSpaceAccess.retrieveTableSpaces();
        tableSpaces.parallelStream().forEach(t->allTables.put(t,retrieveForTableSpace(t)));
        return allTables;
    }
}
