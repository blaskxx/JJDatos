package sample.Controller;
//connection.initializeConnection("Johan", "sysdba", "root", "192.168.1.111", "XE", 1521, true);

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.access.tablespace.TableSpaceAccess;
import sample.Model.entities.TableSpace;
import sample.Model.series.cpu.CpuTimeSeries;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerVistaPrincipal implements Initializable, ControlledScreen {

    ScreensController myController;


    @FXML   BorderPane mainPane;
   //chart
    @FXML   LineChart<String,Number> cpu_chart;
    //Table View
    @FXML   TableView<TableSpace> tableSpaceTableView;
    @FXML
    TableColumn<TableSpace,String> TBC_name;
    @FXML   TableColumn<TableSpace,String> TBC_used;
    @FXML   TableColumn<TableSpace,String> TBC_free;
    @FXML   TableColumn<TableSpace,String> TBC_file;
    @FXML   TableColumn<TableSpace,String> TBC_auto;
    @FXML   TableColumn<TableSpace,String> TBC_max;
    @FXML   TableColumn<TableSpace,String> TBC_grow;
    @FXML   TableColumn<TableSpace,String> TBC_pfree;
    @FXML   TableColumn<TableSpace,String> TBC_Size;
    @FXML   BarChart<String,Number> tableSpaceUseChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TableView TableSpace
        TBC_name.setCellValueFactory(new PropertyValueFactory("name"));
        TBC_used.setCellValueFactory(new PropertyValueFactory("used"));
        TBC_free.setCellValueFactory(new PropertyValueFactory("free"));
        TBC_file.setCellValueFactory(new PropertyValueFactory("file"));
        TBC_auto.setCellValueFactory(new PropertyValueFactory("autoIncrement"));
        TBC_max.setCellValueFactory(new PropertyValueFactory("maxSize"));
        TBC_grow.setCellValueFactory(new PropertyValueFactory("Increase"));
        TBC_pfree.setCellValueFactory(new PropertyValueFactory("pctFree"));
        TBC_Size.setCellValueFactory(new PropertyValueFactory("size"));

        TBC_name.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_used.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_free.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_file.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(1.8));
        TBC_auto.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_max.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_grow.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_pfree.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));

        tableSpaceTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableSpaceTableView.setOnMouseClicked(e-> {
                if (e.getClickCount()>1) {
                    TableSpace tbs = tableSpaceTableView.getSelectionModel().getSelectedItem();
                    if(tbs!=null){
                        TPieController.tbs = tbs;
                            myController.loadScreen(Main.pieChart,Main.pieChartFile);
                            myController.setScreen(Main.pieChart);
                    }
                }
            });

        //diagram 1
        TableSpaceAccess.initializate();
        tableSpaceTableView.setItems(TableSpaceAccess.getTableSpaces());
        cpu_chart.setData(CpuTimeSeries.getInstance().getCpu_use());

        cpu_chart.getYAxis().setAutoRanging(false);

        //Diagram 2

        ObservableList<TableSpace> aux=tableSpaceTableView.getItems();
        aux.forEach(e-> tableSpaceUseChart.getData().addAll( new BarChart.Series(
                e.getName(),FXCollections.observableArrayList(
                new BarChart.Data("Free",e.getFree()),
                new BarChart.Data("Used",e.getUsed())
        ))));

    }

    @FXML void stupid(){
        for( XYChart.Data<String, Number> i:cpu_chart.getData().get(0).getData()) System.out.print(", "+i.getXValue()+":"+i.getYValue());
        System.out.println();
    }
    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
        Stage stage = (Stage) myController.getScene().getWindow();
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setOnCloseRequest(e->frameClose());
        //ORCConnection.Instance().close();
    }
    @FXML void frameClose(){
        CpuTimeSeries.getInstance().stopThread();
    }

}
