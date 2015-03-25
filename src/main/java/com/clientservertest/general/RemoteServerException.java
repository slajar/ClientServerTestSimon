/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

/**
 * Created by TB on 17.03.15.
 */
public class RemoteServerException extends Exception
{
    public RemoteServerException()
    {
    }

    public RemoteServerException(String message)
    {
        super(message);
    }

    public RemoteServerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RemoteServerException(Throwable cause)
    {
        super(cause);
    }

    public RemoteServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
