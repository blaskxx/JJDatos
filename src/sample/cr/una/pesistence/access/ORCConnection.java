package sample.cr.una.pesistence.access;

//import oracle.jdbc.pool.OracleDataSource;


import java.sql.*;
import java.util.Properties;

/**
 * Created by Casa on 13/09/2014.
 */
public class ORCConnection {
    private static Connection connection = null;

    private String user = "/";
    private String roll = "sysdba";
    private String pass = "root";
    private String url = "localhost";
    private String service_name = "XE";
    private int port = 1521;
    private Statement statement;

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
    public boolean initializeConnection(String user, String roll, String pass, String url, String service_name, int port, boolean sysdba) {
        if(initialized) close();
        this.user = user;
        this.roll = roll;
        this.pass = pass;
        this.url = url;
        this.service_name = service_name;
        this.port = port;
        //OracleDataSource od;

        Properties pps = new Properties();
        pps.put("user",user);
        pps.put("password", pass);

        if(sysdba) pps.put("internal_logon", roll);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@" + url + ":" + port + "/" + service_name, pps);
            statement = connection.createStatement();

        } catch (SQLException|ClassNotFoundException e) {
            if(e.getClass().getName().equals("ClassNotFoundException")) System.out.println("Failed to load JDBC driver for Oracle");
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return false;
        }
        if (connection == null)
            return false;
        initialized = true;

        return  true;
    }

    /**
     * Executes a query in the Database
     * @param q The query
     * @return A ResultSet containing the query result <b>null</b> if connection was not properly initialized
     */
    public ResultSet executeQuery(String q){
        try {
            return (isInitialized()) ? statement.executeQuery(q): null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes and Update (No data returning) query on the DataBase
     * @param q The Update Query
     * @return A integer indicating how many rows where affected <b>null</b> if connection was not properly initialized
     */
    public int executeUpdateQuery(String q) {
        try {
            return (isInitialized()) ? statement.executeUpdate(q): null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Protected Constructor For Singleton Pattern
     */
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
        if(initialized) try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
