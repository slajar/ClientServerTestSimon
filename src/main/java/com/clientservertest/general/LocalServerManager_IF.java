/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

/**
 * Created by TB on 06.03.15.
 */
public interface LocalServerManager_IF
{
    public void connect(String ip, int port) throws LocalServerConnectException;

    public boolean disconnect();

    public void getFile(String path, MyFileReceiver_AC fileReceiver) throws FileNotFoundException;

    public void pingAllClients();

    public InetSocketAddress login(ClientCallback_IF clientCallback) throws RemoteServerException;
}
