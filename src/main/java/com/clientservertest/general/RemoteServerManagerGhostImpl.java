/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import java.util.logging.Logger;

/**
 * Created by TB on 06.03.15.
 */
public class RemoteServerManagerGhostImpl implements RemoteServerManager_IF
{
    private final ServerGhost server;
    private Logger logger = Logger.getLogger(getClass().getName());
    private boolean running;

    public RemoteServerManagerGhostImpl()
    {
        this.server = new ServerGhost();
    }


    @Override
    public void startServer()
    {
        logger.info("it's just a ghost");
        running = true;
    }

    @Override
    public boolean stopServer()
    {
        logger.info("it's just a ghost");
        running = false;
        return true;
    }

    @Override
    public boolean isRunning()
    {
        logger.info("it's just a ghost");
        return running;
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
