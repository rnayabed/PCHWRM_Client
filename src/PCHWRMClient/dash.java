package PCHWRMClient;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class dash extends dashUI{

    io io;
    HashMap<String, String> config = new HashMap<>();

    String currentDir = System.getProperty("user.dir");
    enum os{
        windows
    }

    os currentOS;

    boolean isConnected = false;

    Stage stage;
    public dash(Main m, Stage ps)
    {
        stage = ps;
        if(System.getProperty("os.name").toLowerCase().contains("windows"))
            currentOS = os.windows;

        io = new io();
        readConfig();
        loadNodes();
        donateButton.setOnAction(event -> m.openDonation());
        downloadOpenHardwareMonitorButton.setOnAction(event -> m.openOpenHardwareMonitorDownloads());

        applyConfigReadingsToFields();

        new Thread(new Task<Void>() {
            @Override
            protected Void call()
            {
                try {
                    initGPUCPURAM();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        }).start();

        updateValuesButton.setOnAction(event -> {
            updateConfig("DATA_REFRESH_INTERVAL",dataRefreshIntervalTextField.getText());
            updateConfig("SERVER_IP",serverIPAddressTextField.getText());
            updateConfig("SERVER_PORT",serverPortTextField.getText());
            updateConfig("CPU_LOAD_NAME",CPULoadNameTextField.getText());
            updateConfig("GPU_LOAD_NAME",GPULoadNameTextField.getText());
            updateConfig("CPU_FAN_NAME",CPUFanNameTextField.getText());
            updateConfig("GPU_FAN_NAME",GPUFanNameTextField.getText());
            updateConfig("CPU_TEMPERATURE_NAME",CPUTemperatureNameTextField.getText());
            updateConfig("GPU_TEMPERATURE_NAME",GPUTemperatureNameTextField.getText());
            updateConfig("GPU_TOTAL_VRAM_NAME",TotalVRAMNameTextField.getText());
            updateConfig("GPU_USED_VRAM_NAME",UsedVRAMNameTextField.getText());
            updateConfig("USED_RAM_NAME",UsedRAMNameTextField.getText());
            updateConfig("AVAILABLE_RAM_NAME",AvailableRAMNameTextField.getText());
        });

        runOnStartupToggleButton.setOnAction(event -> {
            if(runOnStartupToggleButton.isSelected())
            {
                if(!Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run","PCHWRM"))
                {
                    Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "PCHWRM", currentDir+"\\start.vbs");
                }
            }
            else
            {
                if(Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run","PCHWRM"))
                {
                    Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run","PCHWRM");
                }
            }
        });

        minimizeToTraySetup();
        minimizeToSystemTrayButton.setOnAction(event -> minimizeToTray());
    }

    PopupMenu popup;
    SystemTray tray;
    TrayIcon ti;

    public void minimizeToTraySetup()
    {
        try
        {
            if (SystemTray.isSupported()) {
                popup = new PopupMenu();
                tray = SystemTray.getSystemTray();

                MenuItem showItem = new MenuItem("Show");
                showItem.addActionListener(l->{
                    tray.remove(ti);
                    Platform.runLater(()->stage.show());
                });

                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(l->{
                    try {
                        if(isConnected){
                            writeToOS("QUIT");
                            Thread.sleep(500);
                            isConnected=false;
                        }
                        tray.remove(ti);
                        Platform.exit();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

                popup.add(showItem);
                popup.addSeparator();
                popup.add(exitItem);
                ti = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("assets/icon.png")), "PCHWRM",popup);
                ti.setImageAutoSize(true);
            } else {
                io.pln("Your System doesnt support minimize to System Tray");
                minimizeToSystemTrayButton.setDisable(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void minimizeToTray() {
        try {
            tray.add(ti);
            stage.hide();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showErrorAlert(String headerText, String contentText)
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(headerText);
        errorAlert.setTitle("Error!");
        errorAlert.setContentText(contentText);
        errorAlert.show();
    }

    public void updateConfig(String key, String newValue)
    {
        config.put(key, newValue);
        io.writeToFile(config.get("SERVER_IP")+"::"+
                config.get("SERVER_PORT")+"::"+
                config.get("CPU_LOAD_NAME")+"::"+
                config.get("GPU_LOAD_NAME")+"::"+
                config.get("CPU_FAN_NAME")+"::"+
                config.get("GPU_FAN_NAME")+"::"+
                config.get("CPU_TEMPERATURE_NAME")+"::"+
                config.get("GPU_TEMPERATURE_NAME")+"::"+
                config.get("GPU_TOTAL_VRAM_NAME")+"::"+
                config.get("GPU_USED_VRAM_NAME")+"::"+
                config.get("USED_RAM_NAME")+"::"+
                config.get("AVAILABLE_RAM_NAME")+"::"+
                config.get("DATA_REFRESH_INTERVAL")+"::","config");
    }


    public void readConfig()
    {
        try
        {
            config.clear();
            if(new File("config").exists())
            {
                String[] configArray = io.readFileArranged("config","::");
                config.put("SERVER_IP",configArray[0]);
                config.put("SERVER_PORT",configArray[1]);
                config.put("CPU_LOAD_NAME",configArray[2]);
                config.put("GPU_LOAD_NAME",configArray[3]);
                config.put("CPU_FAN_NAME",configArray[4]);
                config.put("GPU_FAN_NAME",configArray[5]);
                config.put("CPU_TEMPERATURE_NAME",configArray[6]);
                config.put("GPU_TEMPERATURE_NAME",configArray[7]);
                config.put("GPU_TOTAL_VRAM_NAME",configArray[8]);
                config.put("GPU_USED_VRAM_NAME",configArray[9]);
                config.put("USED_RAM_NAME",configArray[10]);
                config.put("AVAILABLE_RAM_NAME",configArray[11]);
                config.put("DATA_REFRESH_INTERVAL",configArray[12]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void applyConfigReadingsToFields()
    {
        if(!config.get("SERVER_IP").equals("NULL"))
            serverIPAddressTextField.setText(config.get("SERVER_IP"));

        if(!config.get("SERVER_PORT").equals("NULL"))
            serverPortTextField.setText(config.get("SERVER_PORT"));

        CPULoadNameTextField.setText(config.get("CPU_LOAD_NAME"));
        GPULoadNameTextField.setText(config.get("GPU_LOAD_NAME"));
        CPUFanNameTextField.setText(config.get("CPU_FAN_NAME"));
        GPUFanNameTextField.setText(config.get("GPU_FAN_NAME"));
        CPUTemperatureNameTextField.setText(config.get("CPU_TEMPERATURE_NAME"));
        GPUTemperatureNameTextField.setText(config.get("GPU_TEMPERATURE_NAME"));
        TotalVRAMNameTextField.setText(config.get("GPU_TOTAL_VRAM_NAME"));
        UsedVRAMNameTextField.setText(config.get("GPU_USED_VRAM_NAME"));
        UsedRAMNameTextField.setText(config.get("USED_RAM_NAME"));
        AvailableRAMNameTextField.setText(config.get("AVAILABLE_RAM_NAME"));
        dataRefreshIntervalTextField.setText(config.get("DATA_REFRESH_INTERVAL"));

        if(Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run","PCHWRM"))
        {
            runOnStartupToggleButton.setSelected(true);
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "PCHWRM", currentDir+"\\start.vbs");
        }
    }

    String cpuModel,gpuModel;

    Socket client;

    BufferedWriter osx;

    public void initGPUCPURAM() throws Exception
    {
        if(currentOS == os.windows)
        {
            cpuModel = io.getShellOutput("wmic cpu get name").replace("\r","").replace("\n","").replace("Name                                  ","");
            gpuModel = io.getShellOutput("wmic path win32_VideoController get name").replace("\r","").replace("\n","").replace("Name                        ","");
        }


        connectDisconnectServerButton.setOnAction(event-> new Thread(new Task<Void>() {
            @Override
            protected Void call()
            {
                try {
                    if(isConnected)
                    {
                        writeToOS("QUIT");
                        osx.close();
                        client.close();
                        isConnected=false;
                        Platform.runLater(()->{
                            setTextFieldDisableStatus(false);
                            connectDisconnectServerButton.setText("Connect");
                            connectDisconnectServerButton.setDisable(false);
                        });
                    }
                    else
                    {
                        Platform.runLater(()-> {
                            setTextFieldDisableStatus(true);
                            connectDisconnectServerButton.setDisable(true);
                        });

                        String formValidationResult = doFormValidation();
                        if(formValidationResult.equals("OK"))
                        {
                            if(getValuesFromWMI().size()==0)
                            {
                                Platform.runLater(()->{
                                    showErrorAlert("Open Hardware Monitor not Running!","Please run Open Hardware Monitor with Admin Rights");
                                    setTextFieldDisableStatus(false);
                                    connectDisconnectServerButton.setDisable(false);
                                    connectDisconnectServerButton.setText("Connect");
                                });
                            }
                            else
                            {
                                Platform.runLater(()->connectDisconnectServerButton.setText("Connecting ..."));
                                client = new Socket();
                                client.connect(new InetSocketAddress(serverIPAddressTextField.getText(), Integer.parseInt(serverPortTextField.getText())), 2500);

                                osx = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                                writeToOS("CPU_GPU_MODELS!!"+cpuModel+"::"+gpuModel+"::");


                                Platform.runLater(()->{
                                    connectDisconnectServerButton.setText("Disconnect");
                                    connectDisconnectServerButton.setDisable(false);
                                });
                                isConnected = true;

                                new Thread(new Task<Void>() {
                                    @Override
                                    protected Void call()
                                    {
                                        while(isConnected)
                                        {
                                            try
                                            {
                                                ArrayList<String[]> list = getValuesFromWMI();

                                                ArrayList<String> toSend = new ArrayList<>();
                                                for(String[] each : list)
                                                {
                                                    if(each[0].equals(CPULoadNameTextField.getText()) && each[1].equals("Load"))
                                                        toSend.add("CPU_LOAD<>"+each[2]);
                                                    else if(each[0].equals(GPULoadNameTextField.getText()) && each[1].equals("Load"))
                                                        toSend.add("GPU_LOAD<>"+each[2]);
                                                    else if(each[0].equals(CPUFanNameTextField.getText()) && each[1].equals("Fan"))
                                                        toSend.add("CPU_FAN<>"+each[2]);
                                                    else if(each[0].equals(GPUFanNameTextField.getText()) && each[1].equals("Fan"))
                                                        toSend.add("GPU_FAN<>"+each[2]);
                                                    else if(each[0].equals(CPUTemperatureNameTextField.getText()) && each[1].equals("Temperature"))
                                                        toSend.add("CPU_TEMP<>"+each[2]);
                                                    else if(each[0].equals(GPUTemperatureNameTextField.getText()) && each[1].equals("Temperature"))
                                                        toSend.add("GPU_TEMP<>"+each[2]);
                                                    else if(each[0].equals(TotalVRAMNameTextField.getText()) && each[1].equals("SmallData"))
                                                        toSend.add("TOTAL_VRAM<>"+each[2]);
                                                    else if(each[0].equals(UsedVRAMNameTextField.getText()) && each[1].equals("SmallData"))
                                                        toSend.add("USED_VRAM<>"+each[2]);
                                                    else if(each[0].equals(UsedRAMNameTextField.getText()) && each[1].equals("Data"))
                                                        toSend.add("USED_RAM<>"+each[2]);
                                                    else if(each[0].equals(AvailableRAMNameTextField.getText()) && each[1].equals("Data"))
                                                        toSend.add("FREE_RAM<>"+each[2]);
                                                }

                                                StringBuilder sb = new StringBuilder();
                                                for(String ec : toSend)
                                                {
                                                    sb.append(ec).append("::");
                                                }
                                                if(isConnected) writeToOS("PC_DATA!!"+sb.toString());
                                                Thread.sleep(Integer.parseInt(dataRefreshIntervalTextField.getText()));
                                            }
                                            catch (Exception e)
                                            {
                                                isConnected = false;
                                                Platform.runLater(()->{
                                                    setTextFieldDisableStatus(false);
                                                    isConnected=false;
                                                    connectDisconnectServerButton.setText("Connect");
                                                    connectDisconnectServerButton.setDisable(false);
                                                    showErrorAlert("Exception","An error has occurred\n"+e.getMessage());
                                                });
                                                e.printStackTrace();
                                            }
                                        }
                                        return null;
                                    }
                                }).start();
                            }
                        }
                        else
                        {
                            Platform.runLater(()-> showErrorAlert("Error!", "Please fix the following errors and try again : \n"+formValidationResult));
                        }
                    }
                }
                catch (Exception e)
                {
                    isConnected = false;
                    Platform.runLater(()->{
                        setTextFieldDisableStatus(false);
                        connectDisconnectServerButton.setText("Connect");
                        connectDisconnectServerButton.setDisable(false);

                        showErrorAlert("Exception!","An error has occurred\n"+e.getMessage());
                    });
                    e.printStackTrace();
                }
                return null;
            }
        }).start());
    }

    public void writeToOS(String txt) throws Exception
    {
        osx.write(txt+"\n");
        osx.flush();
    }


    public String doFormValidation()
    {
        String toReturn = "";
        boolean error = false;

        if(dataRefreshIntervalTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "Server Port cannot be empty!\n";
        }
        else
        {
            try {
                Integer.parseInt(dataRefreshIntervalTextField.getText());
            }
            catch (Exception e)
            {
                error = true;
                toReturn += "Refresh Interval must be a valid number\n";
            }
        }

        if(serverIPAddressTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "Server IP Address cannot be empty!\n";
        }

        if(serverPortTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "Server Port cannot be empty!\n";
        }
        else
        {
            try {
                Integer.parseInt(serverPortTextField.getText());
            }
            catch (Exception e)
            {
                error = true;
                toReturn += "Server Port must be a valid number\n";
            }
        }

        if(CPULoadNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "CPU Load Name cannot be empty!\n";
        }

        if(GPULoadNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "GPU Load Name cannot be empty!\n";
        }

        if(CPUTemperatureNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "CPU Temperature Name cannot be empty!\n";
        }

        if(GPUTemperatureNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "GPU Temperature Name cannot be empty!\n";
        }

        if(CPUFanNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "CPU Fan Name cannot be empty!\n";
        }

        if(GPUFanNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "GPU Fan Name cannot be empty!\n";
        }

        if(TotalVRAMNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "Total VRAM Name cannot be empty!\n";
        }

        if(UsedVRAMNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "Used VRAM Name cannot be empty!\n";
        }

        if(UsedRAMNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "Used RAM Name cannot be empty!\n";
        }

        if(AvailableRAMNameTextField.getText().length() == 0)
        {
            error = true;
            toReturn += "Available RAM Name cannot be empty!\n";
        }

        if(error) return toReturn;
        else return "OK";
    }

    public ArrayList<String[]> getValuesFromWMI() throws Exception
    {
        String out = io.getShellOutput("powershell.exe get-wmiobject -namespace root\\OpenHardwareMonitor -query 'SELECT Value,Name,SensorType FROM Sensor'").replace("\r\n\r\n__GENUS          : 2\r\n__CLASS          : Sensor\r\n__SUPERCLASS     : \r\n__DYNASTY        : \r\n__RELPATH        : \r\n__PROPERTY_COUNT : 3\r\n__DERIVATION     : {}\r\n__SERVER         : \r\n__NAMESPACE      : \r\n__PATH           : \r\n","");
        ArrayList<String[]> returnable = new ArrayList<>();

        String[] x = out.split("PSComputerName {3}:");

        for(int i =0;i<x.length - 1;i++)
        {
            String[] cd = x[i].split("\r\n");
            returnable.add(new String[]{cd[0].substring(cd[0].indexOf("Name             : ")).replace("Name             : ",""), cd[1].substring(cd[1].indexOf("SensorType       : ")).replace("SensorType       : ",""), cd[2].substring(cd[2].indexOf("Value            : ")).replace("Value            : ","")});
        }

        return returnable;
    }
}

