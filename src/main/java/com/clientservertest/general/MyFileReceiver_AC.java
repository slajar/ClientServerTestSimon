/*
 * Copyright (c) 2002-2015.
 */

package com.clientservertest.general;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by TB on 06.03.15.
 */
public abstract class MyFileReceiver_AC
{
    private File downloadDir = null;
    private Logger logger = Logger.getLogger(getClass().getName());

    public MyFileReceiver_AC()
    {
        try
        {
            downloadDir = File.createTempFile("filetransfer", "").getParentFile();
        }
        catch (IOException e)
        {
            logger.log(Level.WARNING, "", e);
        }
    }

    public MyFileReceiver_AC(File downloadDir)
    {
        this.setDownloadDir(downloadDir);
    }


    public void setDownloadDir(File dir)
    {
        this.downloadDir = dir;
        if (downloadDir != null && !downloadDir.exists())
        {
            this.downloadDir.mkdirs();
        }
    }

    public File getDownloadDir()
    {
        return downloadDir;
    }

    public abstract void started(File f, long length);

    public abstract void inProgress(File f, long bytesReceived, long length);

    public abstract void completed(File f);

    public abstract void aborted(File f, Exception ex);


}
