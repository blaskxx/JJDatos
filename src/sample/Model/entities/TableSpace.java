package sample.Model.entities;

import javafx.beans.property.*;

/**
 * Created by Casa on 14/09/2014.
 */
public class TableSpace {
    StringProperty name = new SimpleStringProperty();
    StringProperty file = new SimpleStringProperty();
    BooleanProperty autoIncrement = new SimpleBooleanProperty();
    FloatProperty size = new SimpleFloatProperty();
    FloatProperty maxSize = new SimpleFloatProperty();
    FloatProperty used = new SimpleFloatProperty();
    FloatProperty free= new SimpleFloatProperty();
    FloatProperty Increase =new SimpleFloatProperty();
    FloatProperty pctFree=new SimpleFloatProperty();

    public TableSpace(String name, String file, Boolean autoIncrement, Float size, Float maxSize, Float used,
                      Float free, Float increase, Float pctFree) {
        this.name.set(name);
        this.file.set(file);
        this.autoIncrement.set(autoIncrement);
        this.size.set(size);
        this.maxSize.set(maxSize);
        this.used.set(used);
        this.free.set(free);
        Increase.set(increase);
        this.pctFree.set(pctFree);
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

    public String getFile() {
        return file.get();
    }

    public StringProperty fileProperty() {
        return file;
    }

    public void setFile(String file) {
        this.file.set(file);
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
