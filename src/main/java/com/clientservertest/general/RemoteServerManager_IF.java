/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

/**
 * Created by TB on 05.03.15.
 */
public interface RemoteServerManager_IF
{
    public void startServer() throws RemoteServerStartException;

    public boolean stopServer();

    public boolean isRunning();

    public void pingAllClients();

    public String command(String command);
}
