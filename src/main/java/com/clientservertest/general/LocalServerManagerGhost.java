/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * Created by TB on 06.03.15.
 */
public class LocalServerManagerGhost implements LocalServerManager_IF
{
    private final ServerGhost server;
    private Logger logger = Logger.getLogger(getClass().getName());

    public LocalServerManagerGhost()
    {
        this.server = new ServerGhost();
    }

    @Override
    public void connect(String ip, int port) throws LocalServerConnectException
    {
        logger.info("It's just a ghost");
    }


    @Override
    public boolean disconnect()
    {
        logger.info("It's just a ghost");
        return true;
    }


    @Override
    public void getFile(String path, MyFileReceiver_AC fileReceiver) throws FileNotFoundException
    {
        server.getFile(path, fileReceiver);
    }

    @Override
    public void pingAllClients()
    {
        server.pingAllClients();
    }

    @Override
    public InetSocketAddress login(ClientCallback_IF clientCallback)
    {
        return server.login(clientCallback);
    }
}
