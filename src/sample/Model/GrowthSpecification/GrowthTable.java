package sample.Model.GrowthSpecification;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

/**
 * Created by Jose on 06/10/2014.
 */
public class GrowthTable implements Serializable {
    private String nameTable;
    float firstLimit;
    float secondLimit;

    public GrowthTable() {
    }

    public GrowthTable(String nameTable, float firstLimit, float secondLimit) {
        this.nameTable = nameTable;
        this.firstLimit = firstLimit;
        this.secondLimit = secondLimit;
    }

    public float getFirstLimit() {
        return firstLimit;
    }

    public void setFirstLimit(float firstLimit) {
        this.firstLimit = firstLimit;
    }

    public float getSecondLimit() {
        return secondLimit;
    }

    public void setSecondLimit(float secondLimit) {
        this.secondLimit = secondLimit;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }



    @Override
    public String toString() {
        return "GrowthTable{" +
                "nameTable='" + nameTable + '\'' +
                ", firstLimit=" + firstLimit +
                ", secondLimit=" + secondLimit +
                '}';
    }
}
