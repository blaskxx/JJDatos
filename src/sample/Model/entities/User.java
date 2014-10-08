package sample.Model.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.access.User.UserAccess;
import sample.Model.access.tablespace.TableSpaceAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by JoséPablo on 08/10/2014.
 */
public class User {
    StringProperty USERNAME= new SimpleStringProperty();
    IntegerProperty USER_ID= new SimpleIntegerProperty();
    StringProperty PASSWORD= new SimpleStringProperty();
    StringProperty ACCOUNT_STATUS= new SimpleStringProperty();
    StringProperty LOCK_DATE= new SimpleStringProperty();
    StringProperty EXPIRY_DATE= new SimpleStringProperty();
    StringProperty DEFAULT_TABLESPACE= new SimpleStringProperty();
    StringProperty TEMPORARY_TABLESPACE= new SimpleStringProperty();
    StringProperty CREATED= new SimpleStringProperty();
    StringProperty PROFILE= new SimpleStringProperty();
    StringProperty INITIAL_RSRC_CONSUMER_GROUP= new SimpleStringProperty();
    StringProperty EXTERNAL_NAME= new SimpleStringProperty();


    //..
    static boolean stop = false;
    public static List<User> userList = new ArrayList<>();
    static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    static Runnable tableSpacesRetriever;

    public static void begin(){
        userList = UserAccess.retrieveTableSpaces();
        tableSpacesRetriever = ()->{
            try {
                if (stop) return;
                List<User> l = UserAccess.retrieveTableSpaces();
                userList.clear();
                userList.addAll(l);
            }catch (Exception e){ e.printStackTrace();}

        };
        executor.scheduleAtFixedRate(tableSpacesRetriever,5,5, TimeUnit.MINUTES);

    }

    public static void end() throws InterruptedException {
        stop = true;
        executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }

    public ObservableList getTables() {
        return tables;
    }

    public void setTables(ObservableList tables) {
        this.tables = tables;
    }

    ObservableList tables = FXCollections.observableList(new ArrayList<>());

    //---------------------

    public User() {
    }

    public User(String USERNAME, Integer USER_ID, String PASSWORD, String ACCOUNT_STATUS, String LOCK_DATE,
                String EXPIRY_DATE, String DEFAULT_TABLESPACE, String TEMPORARY_TABLESPACE, String CREATED,
                String PROFILE, String INITIAL_RSRC_CONSUMER_GROUP, String EXTERNAL_NAME) {
        this.USERNAME.set(USERNAME);
        this.USER_ID.set(USER_ID);
        this.PASSWORD.set(PASSWORD);
        this.ACCOUNT_STATUS.set(ACCOUNT_STATUS);
        this.LOCK_DATE.set(LOCK_DATE);
        this.EXPIRY_DATE.set(EXPIRY_DATE);
        this.DEFAULT_TABLESPACE.set(DEFAULT_TABLESPACE);
        this.TEMPORARY_TABLESPACE.set(TEMPORARY_TABLESPACE);
        this.CREATED.set(CREATED);
        this.PROFILE.set(PROFILE);
        this.INITIAL_RSRC_CONSUMER_GROUP.set(INITIAL_RSRC_CONSUMER_GROUP);
        this.EXTERNAL_NAME.set(EXTERNAL_NAME);
    }

    public String getUSERNAME() {
        return USERNAME.get();
    }

    public StringProperty USERNAMEProperty() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME.set(USERNAME);
    }

    public int getUSER_ID() {
        return USER_ID.get();
    }

    public IntegerProperty USER_IDProperty() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID.set(USER_ID);
    }

    public String getPASSWORD() {
        return PASSWORD.get();
    }

    public StringProperty PASSWORDProperty() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD.set(PASSWORD);
    }

    public String getACCOUNT_STATUS() {
        return ACCOUNT_STATUS.get();
    }

    public StringProperty ACCOUNT_STATUSProperty() {
        return ACCOUNT_STATUS;
    }

    public void setACCOUNT_STATUS(String ACCOUNT_STATUS) {
        this.ACCOUNT_STATUS.set(ACCOUNT_STATUS);
    }

    public String getLOCK_DATE() {
        return LOCK_DATE.get();
    }

    public StringProperty LOCK_DATEProperty() {
        return LOCK_DATE;
    }

    public void setLOCK_DATE(String LOCK_DATE) {
        this.LOCK_DATE.set(LOCK_DATE);
    }

    public String getEXPIRY_DATE() {
        return EXPIRY_DATE.get();
    }

    public StringProperty EXPIRY_DATEProperty() {
        return EXPIRY_DATE;
    }

    public void setEXPIRY_DATE(String EXPIRY_DATE) {
        this.EXPIRY_DATE.set(EXPIRY_DATE);
    }

    public String getDEFAULT_TABLESPACE() {
        return DEFAULT_TABLESPACE.get();
    }

    public StringProperty DEFAULT_TABLESPACEProperty() {
        return DEFAULT_TABLESPACE;
    }

    public void setDEFAULT_TABLESPACE(String DEFAULT_TABLESPACE) {
        this.DEFAULT_TABLESPACE.set(DEFAULT_TABLESPACE);
    }

    public String getTEMPORARY_TABLESPACE() {
        return TEMPORARY_TABLESPACE.get();
    }

    public StringProperty TEMPORARY_TABLESPACEProperty() {
        return TEMPORARY_TABLESPACE;
    }

    public void setTEMPORARY_TABLESPACE(String TEMPORARY_TABLESPACE) {
        this.TEMPORARY_TABLESPACE.set(TEMPORARY_TABLESPACE);
    }

    public String getCREATED() {
        return CREATED.get();
    }

    public StringProperty CREATEDProperty() {
        return CREATED;
    }

    public void setCREATED(String CREATED) {
        this.CREATED.set(CREATED);
    }

    public String getPROFILE() {
        return PROFILE.get();
    }

    public StringProperty PROFILEProperty() {
        return PROFILE;
    }

    public void setPROFILE(String PROFILE) {
        this.PROFILE.set(PROFILE);
    }

    public String getINITIAL_RSRC_CONSUMER_GROUP() {
        return INITIAL_RSRC_CONSUMER_GROUP.get();
    }

    public StringProperty INITIAL_RSRC_CONSUMER_GROUPProperty() {
        return INITIAL_RSRC_CONSUMER_GROUP;
    }

    public void setINITIAL_RSRC_CONSUMER_GROUP(String INITIAL_RSRC_CONSUMER_GROUP) {
        this.INITIAL_RSRC_CONSUMER_GROUP.set(INITIAL_RSRC_CONSUMER_GROUP);
    }

    public String getEXTERNAL_NAME() {
        return EXTERNAL_NAME.get();
    }

    public StringProperty EXTERNAL_NAMEProperty() {
        return EXTERNAL_NAME;
    }

    public void setEXTERNAL_NAME(String EXTERNAL_NAME) {
        this.EXTERNAL_NAME.set(EXTERNAL_NAME);
    }

    @Override
    public String toString() {
        return "User{" +
                "USERNAME=" + USERNAME +
                ", USER_ID=" + USER_ID +
                ", PASSWORD=" + PASSWORD +
                ", ACCOUNT_STATUS=" + ACCOUNT_STATUS +
                ", LOCK_DATE=" + LOCK_DATE +
                ", EXPIRY_DATE=" + EXPIRY_DATE +
                ", DEFAULT_TABLESPACE=" + DEFAULT_TABLESPACE +
                ", TEMPORARY_TABLESPACE=" + TEMPORARY_TABLESPACE +
                ", CREATED=" + CREATED +
                ", PROFILE=" + PROFILE +
                ", INITIAL_RSRC_CONSUMER_GROUP=" + INITIAL_RSRC_CONSUMER_GROUP +
                ", EXTERNAL_NAME=" + EXTERNAL_NAME +
                '}';
    }
}
