/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import javafx.beans.property.SimpleStringProperty;

import java.util.logging.Logger;

/**
 * Created by TB on 06.03.15.
 */
public class ClientCallbackGhostImpl implements ClientCallback_IF
{
    private SimpleStringProperty log = new SimpleStringProperty();

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void callback(String text)
    {
        logger.info("It's just a ghost: " + text);
        setLog(text);
    }

    @Override
    public void ping()
    {
        logger.info("It's just a ghost");
        setLog("ping");
    }

    @Override
    public String command(String command)
    {
        logger.info("It's just a ghost command:"+ command);
        return null;
    }

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
        this.log.set(log);
    }
}
