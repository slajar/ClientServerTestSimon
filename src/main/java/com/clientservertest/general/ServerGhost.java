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
public class ServerGhost
{
    private Logger logger = Logger.getLogger(getClass().getName());

    public void getFile(String path, MyFileReceiver_AC fileReceiver) throws FileNotFoundException
    {
        logger.info("It's just a ghost");
    }

    public void pingAllClients()
    {
        logger.info("It's just a ghost");
    }

    public InetSocketAddress login(ClientCallback_IF clientCallback)
    {
        logger.info("It's just a ghost");
        return InetSocketAddress.createUnresolved("127.0.0.1", 22222);
    }

    public String command(String command)
    {
        logger.info("It's just a ghost - command: "+ command);
        return null;
    }
}
