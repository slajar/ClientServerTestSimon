/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import com.clientservertest.dirmi.client.LocalServerManagerDIRMIImpl;
import com.clientservertest.dirmi.server.RemoteServerManagerDIRMIImpl;
import com.clientservertest.simon.client.LocalServerManagerSIMONImpl;
import com.clientservertest.simon.server.RemoteServerManagerSIMONImpl;

/**
 * Created by TB on 20.03.15.
 */
public enum ClientServerImplementations
{
    DIRMI("DiRMI", LocalServerManagerDIRMIImpl.class, RemoteServerManagerDIRMIImpl.class),
    SIMON("Simon", LocalServerManagerSIMONImpl.class, RemoteServerManagerSIMONImpl.class),;

    public String name;
    public Class localServerImplClass;
    public Class remoteServerImplClass;

    ClientServerImplementations(String name, Class localServerImplClass, Class remoteServerImplClass)
    {

        this.name = name;
        this.localServerImplClass = localServerImplClass;
        this.remoteServerImplClass = remoteServerImplClass;
    }
}
