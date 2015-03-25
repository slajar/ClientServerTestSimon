/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import com.clientservertest.dirmi.client.LocalServerManagerDIRMIImpl;
import com.clientservertest.simon.client.LocalServerManagerSIMONImpl;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

/**
 * Created by TB on 06.03.15.
 */
public class LocalServerManager implements LocalServerManager_IF
{
    private final LocalServerManager_IF impl;


    // buffer size
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


    // retry Counter
    private SimpleIntegerProperty retryCounter = new SimpleIntegerProperty(3);

    public int getRetryCounter()
    {
        return retryCounter.get();
    }

    public SimpleIntegerProperty retryCounterProperty()
    {
        return retryCounter;
    }

    public void setRetryCounter(int retryCounter)
    {
        this.retryCounter.set(retryCounter);
    }

    public LocalServerManager(Class impl)
    {
        if (impl.equals(LocalServerManagerDIRMIImpl.class))
        {
            this.impl = LocalServerManagerDIRMIImpl.getInstance();
        }
        else if (impl.equals(LocalServerManagerSIMONImpl.class))
        {
            this.impl = LocalServerManagerSIMONImpl.getInstance();
        }
        else
        {
            this.impl = new LocalServerManagerGhost();
        }

    }

    @Override
    public void connect(String ip, int port) throws LocalServerConnectException
    {
        impl.connect(ip, port);
    }


    @Override
    public void getFile(String path, MyFileReceiver_AC fileReceiver) throws FileNotFoundException
    {
        this.impl.getFile(path, fileReceiver);
    }

    @Override
    public boolean disconnect()
    {
        return impl.disconnect();
    }

    @Override
    public void pingAllClients()
    {
        System.out.println("ping: " + System.currentTimeMillis());
        impl.pingAllClients();
    }

    @Override
    public InetSocketAddress login(ClientCallback_IF clientCallback) throws RemoteServerException
    {
        return impl.login(clientCallback);
    }
}
