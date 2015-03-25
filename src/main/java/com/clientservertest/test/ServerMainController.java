/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.test;

import com.clientservertest.general.RemoteServerStartException;
import com.clientservertest.general.ClientServerImplementations;
import com.clientservertest.general.ClientServerManager;
import com.clientservertest.general.RemoteServerManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by TB on 03.03.15.
 */
public class ServerMainController implements Initializable
{
    @FXML
    public MigPane rootPane;
    @FXML
    public TextArea taLog;
    public ComboBox implementationCombobox;
    public Spinner<Integer> bufferSizeSpinner;

    private RemoteServerManager remoteServerManager;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.remoteServerManager = ClientServerManager.getInstance().getRemoteServerManager();

        ObservableList<Object> list = FXCollections.observableArrayList(ClientServerImplementations.values());
        implementationCombobox.setItems(list);
        implementationCombobox.getSelectionModel().selectFirst();
        implementationCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                ClientServerImplementations impl = (ClientServerImplementations) newValue;
                System.out.println("impl = " + impl);
                ClientServerManager.getInstance().setClientServerImplementation(impl);
                remoteServerManager = ClientServerManager.getInstance().getRemoteServerManager(true);
            }
        });

        bufferSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1024, 16));
        bufferSizeSpinner.valueProperty().addListener(new ChangeListener<Integer>()
        {
            @Override
            public void changed(ObservableValue observable, Integer oldValue, Integer newValue)
            {
                remoteServerManager.setBufferSize(newValue * 1024);
            }
        });
    }

    @FXML
    public void handleStartServer()
    {
        try
        {
            info(String.format("Server: Starting Server...[%d]", System.currentTimeMillis()));
            remoteServerManager.startServer();
            info(String.format("Server: Server started: %s [%d]", remoteServerManager.getImpl().getClass().getSimpleName(), System.currentTimeMillis()));
        }
        catch (RemoteServerStartException e)
        {
            logger.log(Level.WARNING, "", e);
            info(String.format("Server: %s [%d]", e.getMessage(), System.currentTimeMillis()));
        }


    }


    @FXML
    public void handlePingAll()
    {
        try
        {
            info(String.format("Server: Ping all [%d]", System.currentTimeMillis()));
            remoteServerManager.pingAllClients();
            info(String.format("Server: pinged [%d]", System.currentTimeMillis()));
        }
        catch (Exception e)
        {
            logger.log(Level.WARNING, "", e);
            info(e.toString());
        }
    }

    @FXML
    public void handleStopServer()
    {
        info(String.format("Server: Stopping Server... [%d]", System.currentTimeMillis()));
        boolean b = remoteServerManager.stopServer();
        if (b)
        {
            info(String.format("Server: Server stopped. [%d]", System.currentTimeMillis()));
        }
        else
        {
            info("Error while stopping server");
        }
    }


    public void info(String info)
    {
        taLog.setText(taLog.getText() + "\n");
        taLog.setText(taLog.getText() + info);
    }
}
