/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.simon.server;


import com.clientservertest.simon.client.ClientCallbackSIMON_IF;
import de.root1.simon.filetransmit.FileReceiver;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

/**
 * Created by TB on 06.03.15.
 */
public interface ServerSIMON_IF
{
    public static final String BIND_NAME = "UM-Server";

    void pingAllClients();

    public InetSocketAddress login(ClientCallbackSIMON_IF clientCallback);

    //public void getFile(String filename, MyFileReceiverSIMON_IF fileReceiver) throws FileNotFoundException;
    public void getFile(String filename, FileReceiver fileReceiver) throws FileNotFoundException;

    String command(String command);
}
