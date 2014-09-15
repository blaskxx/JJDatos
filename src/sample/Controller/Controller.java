package sample.Controller;
//connection.initializeConnection("Johan", "sysdba", "root", "192.168.1.111", "XE", 1521, true);
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import sample.Model.access.tablespace.TableSpaceAccess;
import sample.Model.entities.TableSpace;
import sample.Model.series.cpu.CpuTimeSeries;
import sample.cr.una.pesistence.access.ORCConnection;


import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML   BorderPane mainPane;
   //chart
    @FXML   LineChart<String,Number> cpu_chart;
    //Table View
    @FXML   TableView<TableSpace> tableSpaceTableView;
    @FXML   TableColumn<TableSpace,String> TBC_name;
    @FXML   TableColumn<TableSpace,String> TBC_used;
    @FXML   TableColumn<TableSpace,String> TBC_free;
    @FXML   TableColumn<TableSpace,String> TBC_file;
    @FXML   TableColumn<TableSpace,String> TBC_auto;
    @FXML   TableColumn<TableSpace,String> TBC_max;
    @FXML   TableColumn<TableSpace,String> TBC_grow;
    @FXML   TableColumn<TableSpace,String> TBC_pfree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ORCConnection connection = ORCConnection.Instance();
        connection.initializeConnection("SYSTEM", "sysdba", "root", "192.168.1.111", "XE", 1521, false);


        //TableView TableSpace
        TBC_name.setCellValueFactory(new PropertyValueFactory("name"));
        TBC_used.setCellValueFactory(new PropertyValueFactory("used"));
        TBC_free.setCellValueFactory(new PropertyValueFactory("free"));
        TBC_file.setCellValueFactory(new PropertyValueFactory("file"));
        TBC_auto.setCellValueFactory(new PropertyValueFactory("autoIncrement"));
        TBC_max.setCellValueFactory(new PropertyValueFactory("maxSize"));
        TBC_grow.setCellValueFactory(new PropertyValueFactory("Increase"));
        TBC_pfree.setCellValueFactory(new PropertyValueFactory("pctFree"));
        tableSpaceTableView.setItems(TableSpaceAccess.getTableSpaces());

        //Chart
        cpu_chart.setData(CpuTimeSeries.getInstance().getCpu_use());
        cpu_chart.getYAxis().setAutoRanging(false);


    }
    @FXML void stupid(){
        for( XYChart.Data<String, Number> i:cpu_chart.getData().get(0).getData()) System.out.print(", "+i.getXValue()+":"+i.getYValue());
        System.out.println();
    }
}
