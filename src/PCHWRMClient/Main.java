package PCHWRMClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        dash d = new dash(this, primaryStage);
        Scene s = new Scene(d);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("PCHWRM By github.com/dubbadhar <3");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event->{
            try
            {
                if(d.isConnected){
                    Thread.sleep(500);
                    d.isConnected=false;
                }
                Platform.exit();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void openDonation()
    {
        getHostServices().showDocument("https://www.paypal.me/ladiesman6969");
    }

    public void openOpenHardwareMonitorDownloads()
    {
        getHostServices().showDocument("https://openhardwaremonitor.org/downloads/");
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
