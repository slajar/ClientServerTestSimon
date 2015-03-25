/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.simon.client;

import com.clientservertest.general.LocalServerConnectException;
import com.clientservertest.general.ClientCallback_IF;
import com.clientservertest.general.LocalServerManager_IF;
import com.clientservertest.general.MyFileReceiver_AC;
import com.clientservertest.simon.server.ServerSIMON_IF;
import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;
import de.root1.simon.filetransmit.DefaultFileReceiver;
import de.root1.simon.filetransmit.FileReceiverProgressListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: TB
 * Date: 19.02.14
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
public class LocalServerManagerSIMONImpl implements LocalServerManager_IF
{
    public static final int PORT = 22222;
    private static LocalServerManagerSIMONImpl ourInstance = new LocalServerManagerSIMONImpl();
    private ServerSIMON_IF server;

    private Logger logger = Logger.getLogger(getClass().getName());
    private Lookup nameLookup;
    private String ip;
    private ClientCallbackSIMON_IF clientCallback;

    public static LocalServerManagerSIMONImpl getInstance()
    {
        return ourInstance;
    }

    private LocalServerManagerSIMONImpl()
    {
    }

    public void connect(String ip, int port) throws LocalServerConnectException
    {
        this.ip = ip;

        // 'lookup' the server object
        try
        {
            nameLookup = Simon.createNameLookup(ip, port);
        }
        catch (UnknownHostException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new LocalServerConnectException(e);
        }
        try
        {
            server = (ServerSIMON_IF) nameLookup.lookup(ServerSIMON_IF.BIND_NAME);
            System.out.println("server = " + server);
        }
        catch (LookupFailedException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new LocalServerConnectException(e);
        }
        catch (EstablishConnectionFailed e)
        {
            logger.log(Level.WARNING, "", e);
            throw new LocalServerConnectException(e);
        }


    }

    public InetSocketAddress login(ClientCallback_IF clientCallback)
    {
        ClientCallbackSIMON_IF cc = new ClientCallbackSIMONImpl(clientCallback);

        /*
        {
            @Override
            public void callback(String text)
            {
                 clientCallback.callback(text);
            }

            @Override
            public void ping()
            {
                clientCallback.ping();

            }
        };
        */

        return server.login(cc);
    }


    public boolean disconnect()
    {
        // and finally 'release' the serverobject to release to connection to the server
        if (nameLookup != null && server != null)
        {
            return nameLookup.release(server);
        }
        else
        {
            return true;
        }

    }

    @Override
    public void getFile(String path, MyFileReceiver_AC fileReceiver) throws FileNotFoundException
    {

        DefaultFileReceiver ft = new DefaultFileReceiver();
        ft.setDownloadFolder(fileReceiver.getDownloadDir());
        ft.addProgressListener(new FileReceiverProgressListener()
        {
            @Override
            public void started(File f, long length)
            {
                //System.out.println("> started");
                fileReceiver.started(f, length);
            }

            @Override
            public void inProgress(File f, long bytesReceived, long length)
            {
                //System.out.println("> bytesReceived = " + bytesReceived);
                fileReceiver.inProgress(f, bytesReceived, length);
            }

            @Override
            public void completed(File f)
            {
                // System.out.println("> completed");
                fileReceiver.completed(f);
            }

            @Override
            public void aborted(File f, Exception e)
            {
                //System.out.println("> aborted = ");
                fileReceiver.aborted(f, e);
            }
        });

        if( server==null )
        {
            System.out.println("server = " + server);
        }
        server.getFile(path, ft);
    }

    @Override
    public void pingAllClients()
    {
        server.pingAllClients();
    }
}
