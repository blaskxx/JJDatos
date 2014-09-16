package sample.Model.series.cpu;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import sample.Model.access.cpu.Cpu_Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Casa on 13/09/2014.
 */
public class CpuTimeSeries {
    static int MAX = 20;
    static int cpu_count = 1;

    private final   ObservableList<XYChart.Series<String,Number>> d = FXCollections.observableArrayList(new ArrayList<XYChart.Series<String, Number>>());
    private static  CpuTimeSeries instance;
    private ConcurrentLinkedQueue< XYChart.Data<String, Number> > dataQArr[];
    private ExecutorService executor;
    private AddToQueue addToQueue;

    private boolean stop;

    public static CpuTimeSeries getInstance() {
        if(instance == null) instance = new CpuTimeSeries();
        return instance;
    }

    static {
        try {cpu_count = Cpu_Data.cpu_count();} catch (SQLException e) { e.printStackTrace();  }
    }


    protected CpuTimeSeries() {
        try {
            cpu_count = Cpu_Data.cpu_count();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cpu_count <= 0) cpu_count = 1;
        dataQArr = new ConcurrentLinkedQueue[cpu_count];

        for(int i = 0; i < cpu_count ; i++){
            dataQArr[i] = new ConcurrentLinkedQueue<>();
            d.add(new XYChart.Series<String,Number>());
            d.get(i).setName("Uso CPU "+i);
        }
        executor = Executors.newCachedThreadPool();
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        prepareTimeline();
    }

    private void prepareTimeline() {
        new AnimationTimer() {
            @Override public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for(int j = 0; j<cpu_count;j++) {
            while(!dataQArr[j].isEmpty()) {
                XYChart.Data<String, Number> f = dataQArr[j].remove();
                d.get(j).getData().add(f);
            }
            if (d.get(j).getData().size() > MAX)
                d.get(j).getData().remove(0, d.get(j).getData().size() - MAX);
        }
    }

    public ObservableList<XYChart.Series<String,Number>> getCpu_use() {
        return d;
    }

    private class AddToQueue implements Runnable{

        @Override
        public void run() {
            try {
            if(stop) return;
            Pair<String, Float> psf[];
            psf = Cpu_Data.lastUpdate();
            for(int i = 0; i< cpu_count; i++){
                    if (psf != null) {
                        if(i>=psf.length) break;
                        dataQArr[i].add(new XYChart.Data<String, Number>(psf[i].getKey(), psf[i].getValue()));
                    }
                }
                Thread.sleep(1000);
                executor.execute(this);
            }
            catch (SQLException e) {}
            catch (InterruptedException e) {}
            catch (NullPointerException ne){
                executor.execute(this);
            }
        }
    }
    public void stopThread(){
        try {
            stop = true;
            executor.awaitTermination(2, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            executor.shutdown();
            e.printStackTrace();
        }
        executor.shutdown();

    }
}
