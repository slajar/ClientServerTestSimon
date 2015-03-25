/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest;

import com.clientservertest.general.*;
import org.junit.Assert;

import java.net.InetSocketAddress;

/**
 * Created by TB on 06.03.15.
 */
public class Test
{
    public static final String ip = "127.0.0.1";
    public static final int port = RemoteServerManager.PORT;


    @org.junit.Test
    public void test()
    {
        ClientServerManager clientServerManager = ClientServerManager.getInstance();

        RemoteServerManager remoteServerManager = clientServerManager.getRemoteServerManager();
        org.junit.Assert.assertNotNull("No RemoteServerManager instance", remoteServerManager);
        try
        {
            remoteServerManager.startServer();
        }
        catch (RemoteServerStartException e)
        {
            e.printStackTrace();
            Assert.fail("Error while starting server: " + e.toString());
        }
        Assert.assertTrue("Server should run at the moment", remoteServerManager.isRunning());


        LocalServerManager localServerManager = clientServerManager.getLocalServerManager();
        org.junit.Assert.assertNotNull("No LocalServerManager instance", localServerManager);

        try
        {
            localServerManager.connect(ip, port);
        }
        catch (LocalServerConnectException e)
        {
            e.printStackTrace();
            Assert.fail("Error whiling connecting to server: " + e.toString());
        }


        ClientCallback_IF callback = new ClientCallback_AC()
        {
            @Override
            public void callback(String text)
            {
                System.out.println("text = " + text);
            }

            @Override
            public void ping()
            {
                System.out.println("ping");

            }
        };
        InetSocketAddress address = null;
        try
        {
            address = localServerManager.login(callback);
        }
        catch (RemoteServerException e)
        {
            Assert.fail(e.toString());
        }
        Assert.assertNotNull("Failed logging on", address);
        System.out.println("address = " + address);

        //in UM jetzt der Datenbankaufbau


        localServerManager.pingAllClients();


        //Discount client
        Assert.assertTrue("Failed disconnecting from server", localServerManager.disconnect());


        //stop Server
        Assert.assertTrue("Error while stopping Server", remoteServerManager.stopServer());
        Assert.assertFalse("Server should not run at the moment", remoteServerManager.isRunning());


    }
}
