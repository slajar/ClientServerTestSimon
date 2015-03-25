/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.simon.server;


import com.clientservertest.general.ClientServerManager;
import com.clientservertest.simon.client.ClientCallbackSIMON_IF;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import de.root1.simon.filetransmit.DefaultFileSender;
import de.root1.simon.filetransmit.FileReceiver;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: TB
 * Date: 19.02.14
 * Time: 08:18
 * To change this template use File | Settings | File Templates.
 */
@SimonRemote(value = { ServerSIMON_IF.class })
public class ServerSIMONImpl implements ServerSIMON_IF
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(getClass().getName());
    private ArrayList<ClientCallbackSIMON_IF> clientCallbacks = new ArrayList<ClientCallbackSIMON_IF>();


    public ServerSIMONImpl()
    {
    }

    public boolean hasFileArchiveChanged()
    {
        return true;
    }


    public void getFile(String filename, FileReceiver fileReceiver) throws FileNotFoundException
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
        //File file = new File("_stuff/test.mp3");
        //File file = new File(filename); //todo:

        if (!file.exists())
        {
            throw new FileNotFoundException(file.getAbsolutePath());
        }


        DefaultFileSender fs = new DefaultFileSender(fileReceiver);
        fs.setTxBlockSize(ClientServerManager.getInstance().getRemoteServerManager().getBufferSize());

        fs.sendFile(file, true);

        // dont't forget to close, otherwise the send-thread will continue idle'ing around
        fs.close();

    }

    @Override
    public String command(String command)
    {
        return null;
    }

    @Override
    public void pingAllClients()
    {
        System.out.println("ping all clients");
        for (ClientCallbackSIMON_IF clientCallback : clientCallbacks)
        {
            System.out.println("clientCallback = " + clientCallback);
            clientCallback.ping(

            );
        }
    }

    @Override
    public InetSocketAddress login(ClientCallbackSIMON_IF clientCallback)
    {
        System.out.println("Simon.getLocalInetSocketAddress(clientCallback) = " + Simon.getLocalInetSocketAddress(clientCallback));
        System.out.println("Simon.getRemoteInetSocketAddress(clientCallback) = " + Simon.getRemoteInetSocketAddress(clientCallback));

        clientCallbacks.add(clientCallback);
        clientCallback.callback("This is the callback. " +
                "Your address is " +
                Simon.getRemoteInetSocketAddress(clientCallback).getAddress() + " " +
                "and your are connected from port " +
                Simon.getRemoteInetSocketAddress(clientCallback).getPort());

        return Simon.getLocalInetSocketAddress(clientCallback);

    }


}
