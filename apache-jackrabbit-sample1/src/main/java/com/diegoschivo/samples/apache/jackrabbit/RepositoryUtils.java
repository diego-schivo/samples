package com.diegoschivo.samples.apache.jackrabbit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.RepositoryException;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;


public final class RepositoryUtils
{

    private RepositoryUtils()
    {
    }

    public static JackrabbitRepository createRepository(File directory, InputStream configuration) throws IOException,
        RepositoryException
    {
        if (!directory.exists())
        {
            directory.mkdirs();
        }
        File file = new File(directory, "repository.xml");
        if (!file.exists())
        {
            OutputStream output = null;
            try
            {
                output = new FileOutputStream(file);
                IOUtils.copy(configuration, output);
            }
            finally
            {
                IOUtils.closeQuietly(configuration);
                IOUtils.closeQuietly(output);
            }
        }
        return RepositoryImpl.create(RepositoryConfig.install(directory));
    }
}
