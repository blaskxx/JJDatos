package sample.Model.entities;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Casa on 15/09/2014.
 */
public class Table {
    StringProperty name = new SimpleStringProperty();
    FloatProperty size = new SimpleFloatProperty();
    StringProperty tableSpace = new SimpleStringProperty();

    public Table(String name, Float size, String tableSpace) {
        this.name.set(name);
        this.size.set(size);
        this.tableSpace.set(tableSpace);
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

    public float getSize() {
        return size.get();
    }

    public FloatProperty sizeProperty() {
        return size;
    }

    public void setSize(float size) {
        this.size.set(size);
    }

    public String getTableSpace() {
        return tableSpace.get();
    }

    public StringProperty tableSpaceProperty() {
        return tableSpace;
    }

    public void setTableSpace(String tableSpace) {
        this.tableSpace.set(tableSpace);
    }
}
