/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.simon.server;

import com.clientservertest.general.RemoteServerManager;
import com.clientservertest.general.RemoteServerStartException;
import com.clientservertest.general.RemoteServerManager_IF;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: TB
 * Date: 18.02.14
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */
public class RemoteServerManagerSIMONImpl implements RemoteServerManager_IF
{
    private static RemoteServerManagerSIMONImpl ourInstance = new RemoteServerManagerSIMONImpl();
    private Logger logger = Logger.getLogger(getClass().getName());

    private Registry registry;
    private ServerSIMONImpl serverImpl;

    public static RemoteServerManagerSIMONImpl getInstance()
    {
        return ourInstance;
    }

    private RemoteServerManagerSIMONImpl()
    {
    }


    public ServerSIMONImpl getServer()
    {
        return serverImpl;
    }

    public void startServer() throws RemoteServerStartException
    {


        // create the server's registry ...
        try
        {
            registry = Simon.createRegistry(RemoteServerManager.PORT);
            registry.start();
            registry.setKeepAliveInterval(10);
            registry.setKeepAliveTimeout(120);
            Simon.setWorkerThreadPoolSize(100);
        }
        catch (IOException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new RemoteServerStartException(e);
        }

        try
        {
            // create the serverobject
            serverImpl = new ServerSIMONImpl();
            registry.bind(ServerSIMON_IF.BIND_NAME, serverImpl);
            logger.info("Server up and running!");
        }
        catch (NameBindingException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new RemoteServerStartException(e);
        }


        // some mechanism to shutdown the server should be placed here
        // this should include the following command:

    }

    public boolean stopServer()
    {
        // boolean unpublish = registry.unpublish(ServerInterface.BIND_NAME);
        boolean unbind = registry.unbind(ServerSIMON_IF.BIND_NAME);
        System.out.println("unbind = " + unbind);

        registry.stop();

        return !registry.isRunning();
    }

    @Override
    public boolean isRunning()
    {
        return registry.isRunning();
    }

    @Override
    public void pingAllClients()
    {
        serverImpl.pingAllClients();
    }

    @Override
    public String command(String command)
    {
        return serverImpl.command(command);
    }
}

