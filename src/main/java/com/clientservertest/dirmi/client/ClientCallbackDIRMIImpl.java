/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.dirmi.client;

import com.clientservertest.general.ClientCallback_IF;

import java.rmi.RemoteException;

/**
 * Created by TB on 06.03.15.
 */
public class ClientCallbackDIRMIImpl implements ClientCallbackDIRMI_IF
{
    //private SimpleStringProperty log = new SimpleStringProperty();

    private ClientCallback_IF clientCallback;

    public ClientCallbackDIRMIImpl(ClientCallback_IF clientCallback)
    {
        this.clientCallback = clientCallback;
    }

    public ClientCallback_IF getClientCallback()
    {
        return clientCallback;
    }

    public void setClientCallback(ClientCallback_IF clientCallback)
    {
        this.clientCallback = clientCallback;
    }

    @Override
    public void callback(String text) throws RemoteException
    {
        //    setLog("DIRMI-callback: " + text);
        clientCallback.callback(text);
    }

    @Override
    public void ping() throws RemoteException
    {
        //setLog("ping");
        clientCallback.ping();
    }

    @Override
    public String command(String command) throws RemoteException
    {
        return clientCallback.command(command);
    }

    /*
    //@Override
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
    */
}
