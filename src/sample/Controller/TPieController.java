package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import sample.Main;
import sample.Model.access.Table.TableAccess;
import sample.Model.entities.Table;
import sample.Model.entities.TableSpace;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Casa on 15/09/2014.
 */
public class TPieController implements Initializable,ControlledScreen {
    ScreensController myController;
    public static TableSpace tbs;

    @FXML    PieChart piechart;
    @FXML    Label lblTitle;
    public TableSpace getTbs() {
        return tbs;
    }
    public void setTbs(TableSpace tbs) {
        this.tbs = tbs;
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Table> tables ;
        while((tables = TableAccess.Instance().retrieveForTableSpace(tbs))== null);
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(new ArrayList<PieChart.Data>());
        tables.stream().forEach(t->data.add(new PieChart.Data(t.getName(),t.getSize())));
        piechart.setData(data);
        lblTitle.setText(tbs.getName());
    }
    @FXML void handleReturn(){
        myController.setScreen(Main.screen2ID);
        myController.unloadScreen(Main.pieChart);
    }
}
