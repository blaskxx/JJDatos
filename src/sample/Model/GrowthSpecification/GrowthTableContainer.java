package sample.Model.GrowthSpecification;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Jose on 06/10/2014.
 */
public class GrowthTableContainer implements Serializable {
    private static GrowthTableContainer growthTableContainer;
    public static HashMap<String,GrowthTable> container= new HashMap<>();


    public GrowthTableContainer() {

    }

    public static GrowthTableContainer getContainer() {
        if(growthTableContainer == null) growthTableContainer = new GrowthTableContainer();
        return growthTableContainer;
    }

    public boolean addTable(GrowthTable growthTable){
        if(container!=null){
            container.put(growthTable.getNameTable(),growthTable);
            return true;
        }
        return false;
    }

    public GrowthTable getTable(String nameTable){
        return container.get(nameTable);
    }


}
