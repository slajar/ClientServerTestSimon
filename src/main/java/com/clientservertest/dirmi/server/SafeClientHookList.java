/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.dirmi.server;

import com.clientservertest.dirmi.client.ClientCallbackDIRMI_IF;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by matthias on 23.03.15.
 */
public class SafeClientHookList
{
    private ArrayList<ClientCallbackDIRMI_IF> clientCallbacks = new ArrayList<ClientCallbackDIRMI_IF>();

    public void add(ClientCallbackDIRMI_IF client)
    {
        clientCallbacks.add(client);
        try
        {
            client.callback("This is the callback. ");
        }
        catch (RemoteException e)
        {
            handleExceptionalClientCall(client);
        }
    }

    public void pingAllClients()
    {
        //System.out.println("clientCallbacks.size() = " + clientCallbacks.size());

        for (ClientCallbackDIRMI_IF client : clientCallbacks)
        {
            try
            {
                client.ping();
            }
            catch (RemoteException e)
            {
                handleExceptionalClientCall(client);
            }
        }
    }

    /**
     * error handling -> retry etc.
     * @param client
     */
    private void handleExceptionalClientCall(ClientCallbackDIRMI_IF client)
    {
        // error handling? retry?
        clientCallbacks.remove(client);
    }
}
