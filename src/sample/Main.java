package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controller.ScreensController;

public class Main extends Application {

    public static String screen1ID = "main";
    public static String screen1File = "../View/Login.fxml";
    public static String screen2ID = "screen2";
    public static String screen2File = "../View/VistaPrincipal.fxml";
    public static String pieChart = "piechart";
    public static String pieChartFile =  "../View/TPieGraph.fxml";
    public static String barChart="barChart";
    public static String barChartFile="../View/TBarGraph.fxml";

    public static ScreensController mainContainer;



    @Override
    public void start(Stage primaryStage) throws Exception{
        mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.screen1ID, Main.screen1File);
        //mainContainer.loadScreen(Main.screen2ID, Main.screen2File);

        mainContainer.setScreen(Main.screen1ID);

        //Group root = new Group(mainContainer);
        //root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(mainContainer,400,400);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(450);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
