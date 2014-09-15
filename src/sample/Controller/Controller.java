package sample.Controller;
//connection.initializeConnection("Johan", "sysdba", "root", "192.168.1.111", "XE", 1521, true);
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import sample.Model.access.cpu.Cpu_Data;
import sample.Model.series.cpu.CpuTimeSeries;
import sample.cr.una.pesistence.access.ORCConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class Controller implements Initializable {
   @FXML   BorderPane mainPane;
   @FXML   LineChart<String,Number> cpu_chart;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ORCConnection connection = ORCConnection.Instance();
        connection.initializeConnection("SYSTEM", "sysdba", "root", "192.168.1.111", "XE", 1521, false);
        cpu_chart.setData(CpuTimeSeries.getInstance().getCpu_use());
        cpu_chart.getYAxis().setAutoRanging(false);

    }
    @FXML void stupid(){
        for( XYChart.Data<String, Number> i:cpu_chart.getData().get(0).getData()) System.out.print(", "+i.getXValue()+":"+i.getYValue());
        System.out.println();
    }
}
