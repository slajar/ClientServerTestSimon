/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by TB on 06.03.15.
 */
public class ClientServerManager
{
    public static ClientServerManager instance;
    private LocalServerManager localServerManager;
    private RemoteServerManager remoteServerManager;


    private SimpleObjectProperty<ClientServerImplementations> clientServerImplementation = new SimpleObjectProperty<>(ClientServerImplementations.DIRMI);

    public ClientServerImplementations getClientServerImplementation()
    {
        return clientServerImplementation.get();
    }

    public SimpleObjectProperty<ClientServerImplementations> clientServerImplementationProperty()
    {
        return clientServerImplementation;
    }

    public void setClientServerImplementation(ClientServerImplementations clientServerImplementation)
    {
        this.clientServerImplementation.set(clientServerImplementation);
    }

    public static ClientServerManager getInstance()
    {
        if (instance == null)
        {
            instance = new ClientServerManager();
        }
        return instance;
    }

    private ClientServerManager()
    {
        clientServerImplementationProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                ClientServerImplementations impl = getClientServerImplementation();
                System.out.println("impl = " + impl);

                if (localServerManager != null)
                {
                    localServerManager.disconnect();
                    localServerManager = null;
                }

                if (remoteServerManager != null && remoteServerManager.isRunning())
                {
                    remoteServerManager.stopServer();
                    remoteServerManager = null;
                }
            }
        });
    }

    public LocalServerManager getLocalServerManager()
    {
        return getLocalServerManager(false);
    }

    public LocalServerManager getLocalServerManager(boolean useNew)
    {
        if (localServerManager == null || useNew)
        {
            this.localServerManager = new LocalServerManager(getClientServerImplementation().localServerImplClass);
        }
        return localServerManager;
    }

    public RemoteServerManager getRemoteServerManager()
    {
        return getRemoteServerManager(false);
    }


    public RemoteServerManager getRemoteServerManager(boolean useNew)
    {
        if (remoteServerManager == null || useNew)
        {
            remoteServerManager = new RemoteServerManager(getClientServerImplementation().remoteServerImplClass);
        }
        return remoteServerManager;
    }
}
