/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.logging.Logger;

/**
 * Created by TB on 02.03.15.
 */
public class TestClient extends Application
{
    private Logger logger = Logger.getLogger(getClass().getName());

    private Scene scene;
    private ClientMainController controller;

    @Override
    public void init() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clientservertest/test/res/client-main.fxml"));
        MigPane root = loader.load();
        this.controller = loader.getController();
        scene = new Scene(root);

    }

    public TestClient()
    {
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
        System.out.println("stop");
        controller.handleDisconnect();
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }


}
