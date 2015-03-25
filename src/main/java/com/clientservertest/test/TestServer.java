/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tbee.javafx.scene.layout.MigPane;

/**
 * Created by TB on 02.03.15.
 */
public class TestServer extends Application
{
    private Scene scene;
    private ServerMainController controller;

    @Override
    public void init() throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clientservertest/test/res/server-main.fxml"));
        MigPane root = loader.load();
        controller = loader.getController();
        scene = new Scene(root);

    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
        controller.handleStopServer();
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setScene(scene);
        stage.show();
    }

    public TestServer()
    {


    }

    public static void main(String[] args)
    {
        launch(args);
    }


}
