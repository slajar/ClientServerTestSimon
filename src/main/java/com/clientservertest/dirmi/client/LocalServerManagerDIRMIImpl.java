/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.dirmi.client;

import com.clientservertest.dirmi.server.ServerDIRMI_IF;
import com.clientservertest.general.*;
import org.cojen.dirmi.Environment;
import org.cojen.dirmi.Pipe;
import org.cojen.dirmi.Session;

import java.io.*;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by TB on 06.03.15.
 */
public class LocalServerManagerDIRMIImpl implements LocalServerManager_IF
{
    public static LocalServerManagerDIRMIImpl instance;
    private final Environment env;
    private Session session;
    private ServerDIRMI_IF server;
    private Logger logger = Logger.getLogger(getClass().getName());

    public static LocalServerManagerDIRMIImpl getInstance()
    {
        if (instance == null)
        {
            instance = new LocalServerManagerDIRMIImpl();
        }
        return instance;
    }

    public LocalServerManagerDIRMIImpl()
    {
        this.env = new Environment();
    }

    @Override
    public void connect(String ip, int port) throws LocalServerConnectException
    {

        try
        {
            session = env.newSessionConnector(ip, port).connect();
        }
        catch (IOException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new LocalServerConnectException(e);
        }
        try
        {
            this.server = (ServerDIRMI_IF) session.receive();
        }
        catch (RemoteException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new LocalServerConnectException(e);
        }

        System.out.println("this.server = " + this.server);


    }

    @Override
    public boolean disconnect()
    {
        try
        {
            env.close();
            return true;
        }
        catch (IOException e)
        {
            logger.log(Level.WARNING, "", e);
        }
        return false;
    }

    @Override
    public void getFile(String path, MyFileReceiver_AC fileReceiver) throws FileNotFoundException
    {
        Runnable t = new Runnable()
        {
            public void run()
            {
                Pipe pipe = null;
                File file = new File(path);
                FileOutputStream fout = null;
                BufferedInputStream bin = null;

                try
                {
                    pipe = server._getFile(path, null);
                }
                catch (RemoteException e)
                {
                    logger.log(Level.WARNING, "", e);
                }


                try
                {
                    long fileLength = pipe.readLong();
                    //System.out.println("local: fileLength = " + fileLength);
                    fileReceiver.started(file, fileLength);

                    InputStream inputStream = pipe.getInputStream();
                    bin = new BufferedInputStream(inputStream);
                    File fileDownload = new File(fileReceiver.getDownloadDir(), "test.mp4");

                    fileDownload.getParentFile().mkdirs();

                    fout = new FileOutputStream(fileDownload);

                    byte[] buffer = new byte[ClientServerManager.getInstance().getLocalServerManager().getBufferSize()]; //8K
                    long totalBytes = 0;
                    int bytesRead = 0;
                    while ((bytesRead = bin.read(buffer)) != -1)
                    {
                        fout.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;

                        if( totalBytes%1024 == 0 )
                            fileReceiver.inProgress(file, totalBytes, fileLength);
                    }
                }
                catch (IOException e)
                {
                    logger.log(Level.WARNING, "", e);
                    fileReceiver.aborted(file, e);
                }
                finally
                {
                    if (fout != null)
                    {
                        try
                        {
                            fout.close();
                        }
                        catch (IOException e)
                        {
                            logger.log(Level.WARNING, "", e);
                        }

                        if (bin != null)
                        {
                            try
                            {
                                bin.close();
                            }
                            catch (IOException e)
                            {
                                logger.log(Level.WARNING, "", e);
                            }
                        }
                    }

                    try
                    {
                        if (pipe != null)
                        {
                            pipe.close();
                        }
                    }
                    catch (IOException e)
                    {
                        logger.log(Level.WARNING, "", e);
                    }

                    fileReceiver.completed(file);
                }


            }
        };
        new Thread(t).start();
        // t.run();
    }


    @Override
    public void pingAllClients()
    {
        try
        {
            server.pingAllClients();
        }
        catch (RemoteException e)
        {
            logger.log(Level.WARNING, "", e);
        }
    }

    @Override
    public InetSocketAddress login(ClientCallback_IF clientCallback) throws RemoteServerException
    {
        ClientCallbackDIRMIImpl cc = new ClientCallbackDIRMIImpl(clientCallback);

        try
        {
            return server.login(cc);
        }
        catch (RemoteException e)
        {
            logger.log(Level.WARNING, "", e);
            throw new RemoteServerException(e);
        }
    }
}
