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
    public static String emailConfig="configurationDBAEmail";
    public static String emailConfigFile="../View/EmailConfiguration.fxml";
    public static String spmtServer="configurationSMPTServer";
    public static String spmtServerFile="../View/EmailServerConfiguration.fxml";
    public static String autoLogin="autoLogin";
    public static String autoLoginFile="../View/AutoLogin.fxml";
    public static String growthConfiguration="GrowthConfiguration";
    public static String growthConfigurationFile="../View/GrowthConfiguration.fxml";
    public static ScreensController mainContainer;
    public static final String keyEncrypth="Mary has one ca1";



    @Override
    public void start(Stage primaryStage) throws Exception{
        mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.screen1ID, Main.screen1File);

        mainContainer.setScreen(Main.screen1ID);

        Scene scene = new Scene(mainContainer,400,400);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(450);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    //TODO Abrir hilo para ver si exite información para pedir el tamaño del TableSpace
    //TODO Poner hora de corte

}
