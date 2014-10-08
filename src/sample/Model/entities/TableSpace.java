package sample.Model.entities;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.GrowthSpecification.GrowthTable;
import sample.Model.GrowthSpecification.GrowthTableContainer;
import sample.Model.access.tablespace.TableSpaceAccess;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Casa on 14/09/2014.
 */
public class TableSpace {
    StringProperty name = new SimpleStringProperty();
    BooleanProperty autoIncrement = new SimpleBooleanProperty();
    FloatProperty size = new SimpleFloatProperty();
    FloatProperty maxSize = new SimpleFloatProperty();
    FloatProperty used = new SimpleFloatProperty();
    FloatProperty free= new SimpleFloatProperty();
    FloatProperty Increase =new SimpleFloatProperty();
    FloatProperty pctFree=new SimpleFloatProperty();
    FloatProperty limitFirst=new SimpleFloatProperty();
    FloatProperty limitSecond=new SimpleFloatProperty();

    public float getLimitSecond() {
        return limitSecond.get();
    }

    public FloatProperty limitSecondProperty() {
        return limitSecond;
    }

    public void setLimitSecond(float limitSecond) {
        this.limitSecond.set(limitSecond);
    }

    public float getLimitFirst() {
        return limitFirst.get();
    }

    public FloatProperty limitFirstProperty() {
        return limitFirst;
    }

    public void setLimitFirst(float limitFirst) {
        this.limitFirst.set(limitFirst);
    }

    public void setLimitSecond(int limitSecond) {
        this.limitSecond.set(limitSecond);
    }


    //..
    static boolean stop = false;
    public static List<TableSpace> tableSpaceList = new ArrayList<>();
    static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    static Runnable tableSpacesRetriever;

    public static void begin(){
        tableSpaceList = TableSpaceAccess.retrieveTableSpaces();
        tableSpacesRetriever = ()->{
            try {
                if (stop) return;
                List<TableSpace> l = TableSpaceAccess.retrieveTableSpaces();
                tableSpaceList.clear();
                tableSpaceList.addAll(l);
            }catch (Exception e){ e.printStackTrace();}

        };
        executor.scheduleAtFixedRate(tableSpacesRetriever,5,5,TimeUnit.MINUTES);

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

    public TableSpace(String name, Boolean autoIncrement, Float size, Float maxSize, Float used,
                      Float free, Float increase, Float pctFree) {
        this.name.set(name);

        this.autoIncrement.set(autoIncrement);
        this.size.set(size);
        this.maxSize.set(maxSize);
        this.used.set(used);
        this.free.set(free);
        Increase.set(increase);
        this.pctFree.set(pctFree);
        this.limitFirst.set(0);
        this.limitSecond.set(size);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean getAutoIncrement() {
        return autoIncrement.get();
    }

    public BooleanProperty autoIncrementProperty() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement.set(autoIncrement);
    }

    public float getSize() {
        return size.get();
    }

    public FloatProperty sizeProperty() {
        return size;
    }

    public void setSize(float size) {
        this.size.set(size);
    }

    public float getMaxSize() {
        return maxSize.get();
    }

    public FloatProperty maxSizeProperty() {
        return maxSize;
    }

    public void setMaxSize(float maxSize) {
        this.maxSize.set(maxSize);
    }

    public float getUsed() {
        return used.get();
    }

    public FloatProperty usedProperty() {
        return used;
    }

    public void setUsed(float used) {
        this.used.set(used);
    }

    public float getFree() {
        return free.get();
    }

    public FloatProperty freeProperty() {
        return free;
    }

    public void setFree(float free) {
        this.free.set(free);
    }

    public float getIncrease() {
        return Increase.get();
    }

    public FloatProperty increaseProperty() {
        return Increase;
    }

    public void setIncrease(float increase) {
        this.Increase.set(increase);
    }

    public float getPctFree() {
        return pctFree.get();
    }

    public FloatProperty pctFreeProperty() {
        return pctFree;
    }

    public void setPctFree(float pctFree) {
        this.pctFree.set(pctFree);
    }
}
