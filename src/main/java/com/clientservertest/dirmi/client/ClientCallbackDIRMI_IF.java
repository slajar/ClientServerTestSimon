/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.dirmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by TB on 06.03.15.
 */
public interface ClientCallbackDIRMI_IF extends Remote
{
    public abstract void callback(String text) throws RemoteException;

    public void ping() throws RemoteException;

    public String command(String command) throws RemoteException;
}
