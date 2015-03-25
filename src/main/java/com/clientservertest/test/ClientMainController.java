/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.test;


import com.clientservertest.general.*;
import com.clientservertest.simon.client.LocalServerManagerSIMONImpl;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by TB on 03.03.15.
 */
public class ClientMainController implements Initializable
{
    public ProgressBar fileProgress;
    public TextField tfIP;
    public TextField tfPort;
    public Spinner bufferSizeSpinner;
    private File downloadDir = new File("_stuff/download");
    private ClientCallback_IF clientCallback;

    private Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    public ComboBox<ClientServerImplementations> implementationCombobox;


    public String getIP()
    {
        return tfIP.getText().trim();

    }

    public int getPort()
    {
        try
        {
            return Integer.valueOf(tfPort.getText().trim());
        }
        catch (NumberFormatException e)
        {
            logger.log(Level.WARNING, "", e);
        }
        return LocalServerManagerSIMONImpl.PORT;

    }

    public ClientMainController()
    {

    }

    @FXML
    public MigPane rootPane;
    @FXML
    public TextArea taLog;

    private LocalServerManager localServerManager;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {


        this.localServerManager = ClientServerManager.getInstance().getLocalServerManager();

        this.clientCallback = new ClientCallback_AC();
        taLog.textProperty().bind(clientCallback.logProperty());


        ObservableList<ClientServerImplementations> list = FXCollections.observableArrayList(ClientServerImplementations.values());
        implementationCombobox.setItems(list);
        implementationCombobox.getSelectionModel().selectFirst();
        implementationCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClientServerImplementations>()
        {
            @Override
            public void changed(ObservableValue observable, ClientServerImplementations oldValue, ClientServerImplementations newValue)
            {
                System.out.println("newValue = " + newValue);
                ClientServerManager.getInstance().setClientServerImplementation(newValue);
                localServerManager = ClientServerManager.getInstance().getLocalServerManager(true);
            }
        });

        bufferSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1024, 16));
        bufferSizeSpinner.valueProperty().addListener(new ChangeListener<Integer>()
        {
            @Override
            public void changed(ObservableValue observable, Integer oldValue, Integer newValue)
            {
                localServerManager.setBufferSize(newValue * 1024);
            }
        });
    }

    @FXML
    public void handleConnect()
    {
        try
        {
            info("Connecting...");
            localServerManager.connect(getIP(), getPort());
            info("Connected with Server");
        }
        catch (LocalServerConnectException e)
        {
            logger.log(Level.WARNING, "", e);
            info(e.getMessage());
        }

        InetSocketAddress login = null;
        try
        {
            login = localServerManager.login(clientCallback);
        }
        catch (RemoteServerException e)
        {
            logger.log(Level.WARNING, "", e);
            this.info("Failed to logon: " + e.toString());

        }
        System.out.println("login = " + login);

    }


    @FXML
    public void handleTest()
    {
        info("Send Ping to all clients: " + System.currentTimeMillis());
        localServerManager.pingAllClients();
    }


    @FXML
    public void handleGetFile()
    {

        //prof.start();

        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle("Save Location");
        downloadDir = fc.showDialog(Window.impl_getWindows().next());
        if (downloadDir == null)
        {
            return;
        }

        MyFileReceiver_AC fileReceiver = new MyFileReceiver_AC(downloadDir)
        {
            @Override
            public void started(File f, long length)
            {
                info(String.format("Started receive file: %s - size: %d", f, length));
                Runnable t = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fileProgress.setProgress(-1);
                    }
                };

                if (!Platform.isFxApplicationThread())
                {
                    Platform.runLater(t);
                }
                else
                {
                    t.run();
                }

            }

            @Override
            public void inProgress(File f, long bytesReceived, long length)
            {
                //info(String.format("In progress receive file: %s received: %d/%d", new Object[] { f, bytesReceived, length }));
                double progress = bytesReceived * 1.0 / length;

                Runnable t = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fileProgress.setProgress(progress);
                    }
                };
                if (!Platform.isFxApplicationThread())
                {
                    Platform.runLater(t);
                }
                else
                {
                    t.run();
                }
            }

            @Override
            public void completed(File f)
            {
                Runnable t = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        info(String.format("Completed receive file: %s", f));
                    }
                };

                if (!Platform.isFxApplicationThread())
                {
                    Platform.runLater(t);
                }
                else
                {
                    t.run();
                }
            }

            @Override
            public void aborted(File f, Exception ex)
            {
                Runnable t = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        info(String.format("Aborted receive file %s: %s", f, ex));
                    }
                };

                if (!Platform.isFxApplicationThread())
                {
                    Platform.runLater(t);
                }
                else
                {
                    t.run();
                }
            }
        };

        try
        {
            localServerManager.getFile("", fileReceiver);
        }
        catch (FileNotFoundException e)
        {
            logger.log(Level.WARNING, "", e);
        }

    }

    @FXML
    public void handleDisconnect()
    {
        boolean b = localServerManager.disconnect();
        if (b)
        {
            info("disconnected");
        }
        else
        {
            info("Error disconnecting");
        }
    }

    public void info(String info)
    {
        clientCallback.setLog(info);
    }
}
