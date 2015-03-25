/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.dirmi.server;

import com.clientservertest.dirmi.client.ClientCallbackDIRMI_IF;
import org.cojen.dirmi.Asynchronous;
import org.cojen.dirmi.Pipe;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by TB on 06.03.15.
 */
public interface ServerDIRMI_IF extends Remote
{
    @Asynchronous //(CallMode.EVENTUAL)
    public Pipe _getFile(String path, Pipe pipe) throws RemoteException;

    public void pingAllClients() throws RemoteException;

    public InetSocketAddress login(ClientCallbackDIRMI_IF clientCallback) throws RemoteException;

    public String command(String command) throws RemoteException;
}
