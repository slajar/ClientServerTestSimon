/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import com.clientservertest.dirmi.server.RemoteServerManagerDIRMIImpl;
import com.clientservertest.simon.server.RemoteServerManagerSIMONImpl;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.logging.Logger;

/**
 * Created by TB on 05.03.15.
 */
public class RemoteServerManager implements RemoteServerManager_IF
{
    public static final int PORT = 22222;

    private Logger logger = Logger.getLogger(getClass().getName());
    private RemoteServerManager_IF impl;

    private SimpleIntegerProperty bufferSize = new SimpleIntegerProperty(16 * 1024);

    public int getBufferSize()
    {
        return bufferSize.get();
    }

    public SimpleIntegerProperty bufferSizeProperty()
    {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize)
    {
        this.bufferSize.set(bufferSize);
    }

    public RemoteServerManager(Class impl)
    {
        if (impl.equals(RemoteServerManagerDIRMIImpl.class))
        {
            this.impl = RemoteServerManagerDIRMIImpl.getInstance();
        }
        else if (impl.equals(RemoteServerManagerSIMONImpl.class))
        {
            this.impl = RemoteServerManagerSIMONImpl.getInstance();
        }
        else
        {
            this.impl = new RemoteServerManagerGhostImpl();
        }
    }


    @Override
    public void startServer() throws RemoteServerStartException
    {
        impl.startServer();
    }


    public RemoteServerManager_IF getImpl()
    {
        return impl;
    }

    @Override
    public boolean stopServer()
    {
        return impl.stopServer();
    }

    public boolean isRunning()
    {
        return impl.isRunning();
    }

    @Override
    public void pingAllClients()
    {
        impl.pingAllClients();
    }

    @Override
    public String command(String command)
    {
        return impl.command(command);
    }
}
