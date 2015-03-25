/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.dirmi.server;

import com.clientservertest.dirmi.client.ClientCallbackDIRMI_IF;
import com.clientservertest.general.ClientServerManager;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.cojen.dirmi.Asynchronous;
import org.cojen.dirmi.Pipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by TB on 06.03.15.
 */
public class ServerDIRMIImpl implements ServerDIRMI_IF
{
    public SafeClientHookList safeClientHookList = new SafeClientHookList();

    public ServerDIRMIImpl()
    {
        System.out.println("ServerDIRMIImpl: SO");
    }

    private Logger logger = Logger.getLogger(getClass().getName());


    @Asynchronous //(CallMode.EVENTUAL)
    public Pipe _getFile(String path, Pipe pipe) throws RemoteException
    {
        CountDownLatch latch = new CountDownLatch(1);
        final File[] _file = { null };

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                FileChooser fc = new FileChooser();
                _file[0] = fc.showOpenDialog(Window.impl_getWindows().next());
                latch.countDown();
            }
        });
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        File file = _file[0];

        if (!file.exists())
        {
            //throw new FileNotFoundException(file.getAbsolutePath());
            return null;
        }

        byte[] buffer = new byte[ClientServerManager.getInstance().getRemoteServerManager().getBufferSize()];
        FileInputStream fr = null;

        try
        {
            fr = new FileInputStream(file);

            pipe.writeLong(file.length());

            int bytesRead = 0;
            while ((bytesRead = fr.read(buffer)) != -1)
            {
                pipe.write(buffer, 0, bytesRead);
            }
        }
        catch (Exception e)
        {
            logger.log(Level.WARNING, "", e);

        }
        finally
        {
            if (pipe != null)
            {
                try
                {
                    pipe.close();
                }
                catch (IOException e)
                {
                    logger.log(Level.WARNING, "", e);
                }
            }
            try
            {
                if (fr != null)
                {
                    fr.close();
                }
            }
            catch (IOException e)
            {
                logger.log(Level.WARNING, "", e);
            }
        }

        return null;
    }

    public void pingAllClients()
    {
        System.out.println("ping all clients");
        safeClientHookList.pingAllClients();
    }

    public InetSocketAddress login(ClientCallbackDIRMI_IF clientCallback)
    {
        safeClientHookList.add(clientCallback);
        return (InetSocketAddress) RemoteServerManagerDIRMIImpl.getInstance().getSessionAdapter().getLocalAddress();
    }

    @Override
    public String command(String command)
    {
        return null;
    }

}
