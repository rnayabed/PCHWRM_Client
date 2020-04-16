package PCHWRMClient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


public class dashUI extends VBox {

    TextField serverIPAddressTextField, dataRefreshIntervalTextField, serverPortTextField, CPULoadNameTextField, GPULoadNameTextField, CPUTemperatureNameTextField, GPUTemperatureNameTextField, CPUFanNameTextField, GPUFanNameTextField, TotalVRAMNameTextField, UsedVRAMNameTextField, UsedRAMNameTextField, AvailableRAMNameTextField;

    Button connectDisconnectServerButton, updateValuesButton, donateButton, minimizeToSystemTrayButton, downloadOpenHardwareMonitorButton;

    ToggleButton runOnStartupToggleButton;

    public void loadNodes()
    {
        setPrefWidth(550);
        Font.loadFont(getClass().getResource("assets/Roboto.ttf").toExternalForm().replace("%20"," "), 13);
        getStylesheets().add(getClass().getResource("assets/style.css").toExternalForm());

        setPadding(new Insets(10));

        Label generalSettingsHeadingLabel = new Label("General Settings");
        generalSettingsHeadingLabel.getStyleClass().add("h3");

        Label refreshLabel = new Label("Refresh Data Interval (Millis)");
        dataRefreshIntervalTextField = new TextField();
        Region r33 = new Region();
        HBox.setHgrow(r33,Priority.ALWAYS);
        HBox refreshHBox = new HBox(refreshLabel, r33, dataRefreshIntervalTextField);
        refreshHBox.setSpacing(10);
        refreshHBox.setAlignment(Pos.CENTER_LEFT);

        Label serverConnectivityHeadingLabel = new Label("Server Connection");
        serverConnectivityHeadingLabel.getStyleClass().add("h3");

        Label ipAddressTFLabel = new Label("IP");
        serverIPAddressTextField = new TextField();
        Region r11 = new Region();
        HBox.setHgrow(r11,Priority.ALWAYS);
        HBox ipHBox = new HBox(ipAddressTFLabel, r11, serverIPAddressTextField);
        ipHBox.setSpacing(10);
        ipHBox.setAlignment(Pos.CENTER_LEFT);

        Label portTFLabel = new Label("Port");
        serverPortTextField = new TextField();
        Region r12 = new Region();
        HBox.setHgrow(r12,Priority.ALWAYS);
        HBox portHBox = new HBox(portTFLabel, r12, serverPortTextField);
        portHBox.setSpacing(10);
        portHBox.setAlignment(Pos.CENTER_LEFT);

        Label OpenHardwareMonitorHeadingLabel = new Label("Open Hardware Monitor Name Settings");
        OpenHardwareMonitorHeadingLabel.getStyleClass().add("h3");

        Label CPULoadNameLabel = new Label("CPU Load (%)");
        CPULoadNameTextField = new TextField();
        Region r0 = new Region();
        HBox.setHgrow(r0,Priority.ALWAYS);
        HBox CPULoadNameHBox = new HBox(CPULoadNameLabel, r0, CPULoadNameTextField);
        CPULoadNameHBox.setSpacing(10);
        CPULoadNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label GPULoadNameLabel = new Label("GPU Load (%)");
        GPULoadNameTextField = new TextField();
        Region r1 = new Region();
        HBox.setHgrow(r1,Priority.ALWAYS);
        HBox GPULoadNameHBox = new HBox(GPULoadNameLabel, r1, GPULoadNameTextField);
        GPULoadNameHBox.setSpacing(10);
        GPULoadNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label CPUTemperatureNameLabel = new Label("CPU Temperature (C)");
        CPUTemperatureNameTextField = new TextField();
        Region r2 = new Region();
        HBox.setHgrow(r2,Priority.ALWAYS);
        HBox CPUTemperatureNameHBox = new HBox(CPUTemperatureNameLabel, r2, CPUTemperatureNameTextField);
        CPUTemperatureNameHBox.setSpacing(10);
        CPUTemperatureNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label GPUTemperatureNameLabel = new Label("GPU Temperature (C)");
        GPUTemperatureNameTextField = new TextField();
        Region r3 = new Region();
        HBox.setHgrow(r3,Priority.ALWAYS);
        HBox GPUTemperatureNameHBox = new HBox(GPUTemperatureNameLabel, r3, GPUTemperatureNameTextField);
        GPUTemperatureNameHBox.setSpacing(10);
        GPUTemperatureNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label CPUFanNameLabel = new Label("CPU FAN (RPM)");
        CPUFanNameTextField = new TextField();
        Region r4 = new Region();
        HBox.setHgrow(r4,Priority.ALWAYS);
        HBox CPUFanNameHBox = new HBox(CPUFanNameLabel, r4, CPUFanNameTextField);
        CPUFanNameHBox.setSpacing(10);
        CPUFanNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label GPUFanNameLabel = new Label("GPU FAN (RPM)");
        GPUFanNameTextField = new TextField();
        Region r5 = new Region();
        HBox.setHgrow(r5,Priority.ALWAYS);
        HBox GPUFanNameHBox = new HBox(GPUFanNameLabel, r5, GPUFanNameTextField);
        GPUFanNameHBox.setSpacing(10);
        GPUFanNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label TotalVRAMNameLabel = new Label("Total VRAM (MB)");
        TotalVRAMNameTextField = new TextField();
        Region r6 = new Region();
        HBox.setHgrow(r6,Priority.ALWAYS);
        HBox TotalVRAMNameHBox = new HBox(TotalVRAMNameLabel, r6, TotalVRAMNameTextField);
        TotalVRAMNameHBox.setSpacing(10);
        TotalVRAMNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label UsedVRAMNameLabel = new Label("Used VRAM (MB)");
        UsedVRAMNameTextField = new TextField();
        Region r7 = new Region();
        HBox.setHgrow(r7,Priority.ALWAYS);
        HBox UsedVRAMNameHBox = new HBox(UsedVRAMNameLabel, r7, UsedVRAMNameTextField);
        UsedVRAMNameHBox.setSpacing(10);
        UsedVRAMNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label AvailableRAMNameLabel = new Label("Available RAM (GB)");
        AvailableRAMNameTextField = new TextField();
        Region r8 = new Region();
        HBox.setHgrow(r8,Priority.ALWAYS);
        HBox AvailableRAMNameHBox = new HBox(AvailableRAMNameLabel, r8, AvailableRAMNameTextField);
        AvailableRAMNameHBox.setSpacing(10);
        AvailableRAMNameHBox.setAlignment(Pos.CENTER_LEFT);

        Label UsedRAMNameLabel = new Label("Used RAM (GB)");
        UsedRAMNameTextField = new TextField();
        Region r9 = new Region();
        HBox.setHgrow(r9,Priority.ALWAYS);
        HBox UsedRAMNameHBox = new HBox(UsedRAMNameLabel, r9, UsedRAMNameTextField);
        UsedRAMNameHBox.setSpacing(10);
        UsedRAMNameHBox.setAlignment(Pos.CENTER_LEFT);

        HBox buttonBar1 = new HBox();
        buttonBar1.setSpacing(15);
        buttonBar1.setPadding(new Insets(15,0,0,0));

        connectDisconnectServerButton = new Button("Connect");
        updateValuesButton = new Button("Save Configuration");
        donateButton = new Button("DONATE");
        donateButton.getStyleClass().add("h3");
        minimizeToSystemTrayButton = new Button("Minimize To System Tray");
        runOnStartupToggleButton = new ToggleButton("Run On Startup");
        downloadOpenHardwareMonitorButton = new Button("Download Open Hardware Monitor");

        buttonBar1.setAlignment(Pos.CENTER);
        buttonBar1.getChildren().addAll(connectDisconnectServerButton, updateValuesButton, minimizeToSystemTrayButton, runOnStartupToggleButton);

        HBox buttonBar2 = new HBox();
        buttonBar2.setSpacing(15);

        buttonBar2.setAlignment(Pos.CENTER);
        buttonBar2.getChildren().addAll(downloadOpenHardwareMonitorButton);

        HBox buttonBar3 = new HBox();
        buttonBar3.setSpacing(15);

        buttonBar3.setAlignment(Pos.CENTER);
        buttonBar3.getChildren().addAll(donateButton);

        setSpacing(15);

        getChildren().addAll(generalSettingsHeadingLabel, refreshHBox, serverConnectivityHeadingLabel, ipHBox, portHBox, OpenHardwareMonitorHeadingLabel, CPULoadNameHBox, CPUFanNameHBox, GPULoadNameHBox, GPUFanNameHBox, GPUTemperatureNameHBox, CPUTemperatureNameHBox, TotalVRAMNameHBox, UsedVRAMNameHBox, AvailableRAMNameHBox, UsedRAMNameHBox, buttonBar1, buttonBar2, buttonBar3);
    }

    public void setTextFieldDisableStatus(boolean isDisable)
    {
        dataRefreshIntervalTextField.setDisable(isDisable);
        serverPortTextField.setDisable(isDisable);
        serverIPAddressTextField.setDisable(isDisable);
        CPULoadNameTextField.setDisable(isDisable);
        GPULoadNameTextField.setDisable(isDisable);
        CPUTemperatureNameTextField.setDisable(isDisable);
        GPUTemperatureNameTextField.setDisable(isDisable);
        CPUFanNameTextField.setDisable(isDisable);
        GPUFanNameTextField.setDisable(isDisable);
        TotalVRAMNameTextField.setDisable(isDisable);
        UsedVRAMNameTextField.setDisable(isDisable);
        UsedRAMNameTextField.setDisable(isDisable);
        AvailableRAMNameTextField.setDisable(isDisable);
    }
}
