/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Created by TB on 06.03.15.
 */
public class ClientCallback_AC implements ClientCallback_IF
{
    private SimpleStringProperty log = new SimpleStringProperty();

    @Override
    public SimpleStringProperty logProperty()
    {
        return log;
    }

    public String getLog()
    {
        return log.get();
    }

    public void setLog(String log)
    {
        this.log.set(getLog() + "\n" + log);
    }

    @Override
    public void callback(String text)
    {
        this.setLog("callback: " + text);
    }

    @Override
    public void ping()
    {
        System.out.println("ping on callback: " + System.currentTimeMillis());
        setLog(getLog() + "ping client!");

        if (Stage.impl_getWindows().hasNext())
        {
            try
            {
                String ssound = getClass().getResource("/com/clientservertest/test/res/ping.mp3").toExternalForm();
                Media sound = new Media(ssound);
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String command(String command)
    {
        return null;
    }
}
