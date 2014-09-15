package sample.Model.access.cpu;

import javafx.util.Pair;
import sample.cr.una.pesistence.access.ORCConnection;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by Casa on 13/09/2014.
 */
public class Cpu_Data {
    public static Pair<String,Float>[] lastUpdate() throws SQLException {
        Float f = (float)0;
        Date d = new Date(0);
        String s;
        int j = 0;
        int c = cpu_count();
        if(c<=0) c=1;
        Pair<String,Float>[] pairs = new Pair[c];
        if(ORCConnection.Instance().isInitialized()){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            ResultSet rs = ORCConnection.Instance().executeQuery("SELECT VALUE FROM V$SYSMETRIC WHERE METRIC_ID = 2057");
            while (rs.next()) {
                if(j>=c) break;
                BigDecimal bd = rs.getBigDecimal(1);
                f = bd.floatValue();
                d = new Date(System.currentTimeMillis());
                pairs[j++] =  new Pair<>(sdf.format(d),f);
            }
            //rs.close();
            return pairs;
        }
        return null;
    }
    public static int cpu_count() throws SQLException {
        int f = 0;
        Date d = new Date(0);
        String s;
        if(ORCConnection.Instance().isInitialized()){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            ResultSet rs = ORCConnection.Instance().executeQuery("SELECT VALUE FROM V$PARAMETER WHERE NUM = 84");
            while (rs.next()) {
                String v = rs.getString("VALUE");
                f = Integer.valueOf(v);
                if(f<=0) f = 1;
            }
           // rs.close();
            return f;
        }
        return 1;
    }
}
