package PCHWRMClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        Platform.setImplicitExit(false);
        dash d = new dash(this, primaryStage);
        Scene s = new Scene(d);
        primaryStage.setScene(s);
        primaryStage.getIcons().add(new Image(getClass().getResource("assets/icon.png").toExternalForm()));
        primaryStage.setResizable(false);
        primaryStage.setTitle("PCHWRM Client By github.com/dubbadhar <3");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event->{
            try
            {
                if(d.isConnected){
                    d.writeToOS("QUIT");
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
