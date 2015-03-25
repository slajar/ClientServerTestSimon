/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.dirmi.server;

import com.clientservertest.general.RemoteServerManager;
import com.clientservertest.general.RemoteServerStartException;
import com.clientservertest.general.RemoteServerManager_IF;
import org.cojen.dirmi.Environment;
import org.cojen.dirmi.SessionAcceptor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by TB on 06.03.15.
 */
public class RemoteServerManagerDIRMIImpl implements RemoteServerManager_IF
{
    private static RemoteServerManagerDIRMIImpl ourInstance = new RemoteServerManagerDIRMIImpl();
    private Environment env;

    private Logger logger = Logger.getLogger(getClass().getName());
    private ServerDIRMIImpl server;
    private SessionAcceptor sessionAdapter;

    public static RemoteServerManagerDIRMIImpl getInstance()
    {
        return ourInstance;
    }

    private RemoteServerManagerDIRMIImpl()
    {

    }

    @Override
    public void startServer() throws RemoteServerStartException
    {
        this.env = new Environment();
        this.server = new ServerDIRMIImpl();

        try
        {
            this.sessionAdapter = env.newSessionAcceptor(RemoteServerManager.PORT);
            sessionAdapter.acceptAll(server);
        }
        catch (IOException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new RemoteServerStartException(e);
        }
    }

    @Override
    public boolean stopServer()
    {
        try
        {
            sessionAdapter.close();
            env.close();
            server = null;
            return true;
        }
        catch (IOException e)
        {
            logger.log(Level.WARNING, "", e);
        }
        return false;
    }

    public Environment getEnv()
    {
        return env;
    }

    public SessionAcceptor getSessionAdapter()
    {
        return sessionAdapter;
    }

    @Override
    public boolean isRunning()
    {
        return server != null;
    }

    @Override
    public void pingAllClients()
    {
        server.pingAllClients();
    }

    @Override
    public String command(String command)
    {
        return server.command(command);
    }
}
