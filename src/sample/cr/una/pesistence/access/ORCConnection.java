package sample.cr.una.pesistence.access;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by Casa on 13/09/2014.
 */
public class ORCConnection {
    //private static Connection connection = null;

    private String user = "/";
    private String roll = "sysdba";
    private String pass = "root";
    private String url = "localhost";
    private String service_name = "XE";
    private int port = 1521;

    //private Statement statement;

    private BasicDataSource ds = null;

    boolean initialized = false;

    private static ORCConnection instance = null;

    /**
     * Single Instance Method get
     * @return The ORCConnection Instance
     */
    public static ORCConnection Instance(){
        if(instance==null) instance = new ORCConnection();
        return instance;
    }

    /**
     * Initiates a Connection to the Database
     * @param user  The User in the DataBase
     * @param roll  The User Roll in the database
     * @param pass  The user Password
     * @param url   The Url to the server
     * @param service_name  The Dabatase Service Name <em>Example XE on 11g Express Edition</em>
     * @param port  The listening port on the Server specified by the URL
     * @param sysdba    If user is Sysdba or Sysoper
     * @return  <em>True</em> if connection success <em>False</em> otherwise
     */
    public boolean initializeConnection(String user, String roll, String pass, String url, String service_name, int port, boolean sysdba) throws ClassNotFoundException, SQLException {
        //if(initialized) close();
        this.user = user;
        this.roll = roll;
        this.pass = pass;
        this.url = url;
        this.service_name = service_name;
        this.port = port;

        String durl = "jdbc:oracle:thin:@" + this.url + ":" + this.port + "/" + this.service_name;

        ds = new BasicDataSource();

        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setPassword(pass);
        ds.setUsername(user);
        ds.setUrl(durl);
        ds.setInitialSize(5);
        ds.setMaxTotal(20);
        ds.setAbandonedLogWriter(new PrintWriter(System.out));
        try {
            ds.setLogWriter(new PrintWriter(System.out));
        } catch (SQLException e) {
            e.printStackTrace();
        }

            Connection con = ds.getConnection();
            initialized = true;
            con.close();


        return  initialized;
    }

    public synchronized Connection getOrcConnection() throws SQLException {
        return ds.getConnection();
    }
    //public synchronized void releaseOrcConnection(Object cnx){

    //}
    protected ORCConnection(){
    }

    /**
     * Indicates If the Connection was successful established
     * @return  <b>True</b> id the connection is open <b>False</b> otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Closes The Connection With the Database
     */
    public void close(){
        try {
            ds.close();
        } catch (SQLException e) {
           // e.printStackTrace();
        }
    }
}
