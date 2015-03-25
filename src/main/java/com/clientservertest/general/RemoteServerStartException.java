/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

/**
 * Created by TB on 06.03.15.
 */
public class RemoteServerStartException extends Exception
{
    public RemoteServerStartException()
    {
        this("");
    }

    public RemoteServerStartException(String message)
    {
        super(String.format("Couldn't start remote server: %s", message));
    }

    public RemoteServerStartException(Throwable cause)
    {
        super("Couldn't start remote server.", cause);
    }
}
