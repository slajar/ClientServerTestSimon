/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.simon.client;

import com.clientservertest.general.ClientCallback_IF;
import de.root1.simon.annotation.SimonRemote;

/**
 * Created by TB on 02.03.15.
 */
@SimonRemote(value = { ClientCallbackSIMON_IF.class })
public class ClientCallbackSIMONImpl implements ClientCallbackSIMON_IF
{
    private ClientCallback_IF clientCallback;


    public ClientCallbackSIMONImpl(ClientCallback_IF clientCallback)
    {
        this.clientCallback = clientCallback;
    }

    private static final long serialVersionUID = 1L;

    /*
    private SimpleStringProperty log = new SimpleStringProperty();

    public String getLog()
    {
        return log.get();
    }

    public SimpleStringProperty logProperty()
    {
        return log;
    }

    public void setLog(String txt)
    {
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                log.set(txt + "\n");
            }
        };

        if (!Stage.impl_getWindows().hasNext() || Platform.isFxApplicationThread())
        {
            task.run();
        }
        else
        {
            Platform.runLater(task);
        }

    }
    */

    public void callback(String text)
    {
        clientCallback.callback(text);
    }

    @Override
    public void ping()
    {
        //setLog(getLog() + "ping client!");
        clientCallback.ping();
    }


}
