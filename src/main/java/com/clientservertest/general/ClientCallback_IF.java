/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import javafx.beans.property.SimpleStringProperty;

import java.rmi.RemoteException;

/**
 * Created by TB on 02.03.15.
 */
public interface ClientCallback_IF
{
    public SimpleStringProperty logProperty();

    public void setLog(String msg);

    public String getLog();

    public void callback(String text);

    void ping();

    /** general command with general answer (easier to implement?) */
    String command(String command);
}
