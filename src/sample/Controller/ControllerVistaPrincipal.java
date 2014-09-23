package sample.Controller;
//connection.initializeConnection("Johan", "sysdba", "root", "192.168.1.111", "XE", 1521, true);

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Main;
import sample.Model.access.tablespace.TableSpaceAccess;
import sample.Model.entities.TableSpace;
import sample.Model.series.cpu.CpuTimeSeries;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;


public class ControllerVistaPrincipal implements Initializable, ControlledScreen {

    ScreensController myController;
    ObservableList<BarChart.Series<String,Number>> barCharData;

    @FXML   BorderPane mainPane;
   //chart
    @FXML   LineChart<String,Number> cpu_chart;
    //Table View
    @FXML   TableView<TableSpace> tableSpaceTableView;
    @FXML
    TableColumn<TableSpace,String> TBC_name;
    @FXML   TableColumn<TableSpace,String> TBC_used;
    @FXML   TableColumn<TableSpace,String> TBC_free;
    //@FXML   TableColumn<TableSpace,String> TBC_file;
    //@FXML   TableColumn<TableSpace,String> TBC_auto;
    @FXML   TableColumn<TableSpace,String> TBC_max;
    //@FXML   TableColumn<TableSpace,String> TBC_grow;
    @FXML   TableColumn<TableSpace,String> TBC_pfree;
    @FXML   TableColumn<TableSpace,String> TBC_Size;
    @FXML   BarChart<String,Number> tableSpaceUseChart;

     @FXML
     public void handleExit(){
         frameClose();
         exit(0);
     }
    private ScheduledExecutorService ex;

    @FXML
    public void handleConfigurationEmailDBA(){
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.emailConfig, Main.emailConfigFile);
            if (!myController.setScreen(Main.emailConfig)) {
                System.out.println("Imposible to charge the screen");
            }
        };
         new Thread(r).start();
    }

    @FXML void handleConfigurationServerSMPT(){
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.spmtServer, Main.spmtServerFile);
            if (!myController.setScreen(Main.spmtServer)) {
                System.out.println("Imposible to charge the screen");
            }
        };
        new Thread(r).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TableView TableSpace
        TBC_name.setCellValueFactory(new PropertyValueFactory("name"));
        TBC_used.setCellValueFactory(new PropertyValueFactory("used"));
        TBC_free.setCellValueFactory(new PropertyValueFactory("free"));
        //TBC_file.setCellValueFactory(new PropertyValueFactory("file"));
        //TBC_auto.setCellValueFactory(new PropertyValueFactory("autoIncrement"));
        TBC_max.setCellValueFactory(new PropertyValueFactory("maxSize"));
        //TBC_grow.setCellValueFactory(new PropertyValueFactory("Increase"));
        TBC_pfree.setCellValueFactory(new PropertyValueFactory("pctFree"));
        TBC_Size.setCellValueFactory(new PropertyValueFactory("size"));

        TBC_name.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.5));
        TBC_used.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_free.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        //TBC_file.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(1.8));
        //TBC_auto.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        TBC_max.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
        //TBC_grow.prefWidthProperty().bind(tableSpaceTableView.prefWidthProperty().multiply(0.3));
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


        TableSpace.begin();
        tableSpaceTableView.setItems(FXCollections.observableList(TableSpace.tableSpaceList));
        cpu_chart.setData(CpuTimeSeries.getInstance().getCpu_use());

        cpu_chart.getYAxis().setAutoRanging(false);

        //Diagram 2

        ObservableList<TableSpace> aux = tableSpaceTableView.getItems();

        aux.forEach(e-> tableSpaceUseChart.getData().add(new BarChart.Series(
                e.getName(), FXCollections.observableArrayList(
                new BarChart.Data("Free", e.getFree()),
                new BarChart.Data("Used", e.getUsed())
        ))));
        Runnable r = () -> {
            Platform.runLater(() -> {
                        tableSpaceUseChart.getData().clear();
                        aux.forEach(e -> tableSpaceUseChart.getData().add(new BarChart.Series(
                                                e.getName(), FXCollections.observableArrayList(
                                                new BarChart.Data("Free", e.getFree()),
                                                new BarChart.Data("Used", e.getUsed()))
                                        )
                                )
                        );
                    }
            );
        };
       // Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(r,0,2, TimeUnit.SECONDS);
        ex = Executors.newSingleThreadScheduledExecutor();
        ex.scheduleAtFixedRate(r,0,5, TimeUnit.MINUTES);
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
        try{TableSpace.end();}catch (Exception e){}
        ex.shutdown();
    }

    @FXML void handleAutoLogin(){
        Runnable r = ()-> {
            Main.mainContainer.loadScreen(Main.autoLogin, Main.autoLoginFile);
            if (!myController.setScreen(Main.autoLogin)) {
                System.out.println("Imposible to charge the screen");
            }
        };
        new Thread(r).start();
    }
}
