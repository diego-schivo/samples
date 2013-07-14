/**
 *    Copyright 2013 Diego Schivo
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.diegoschivo.samples.threads;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public abstract class AsyncProcessor<T>
{

// private static final Logger log = LoggerFactory.getLogger(AsyncProcessor.class);

    public static final int DEFAULT_BUFFER_SIZE = 1000;

    private final Collection<T> buffer;

    private int bufferSize = DEFAULT_BUFFER_SIZE;

    private final Thread consumer;

    private boolean blocking = false;

    private boolean terminated = false;

    public AsyncProcessor()
    {
        this(false);
    }

    public AsyncProcessor(boolean set)
    {
        buffer = set ? new HashSet<T>() : new ArrayList<T>();
        consumer = new Thread(new Consumer());
        consumer.setDaemon(true);
        consumer.setName("AsyncProcessor-Consumer-" + consumer.getName());
        consumer.start();
    }

    public int getBufferSize()
    {
        return bufferSize;
    }

    public void setBufferSize(final int size)
    {
        if (size < 0)
        {
            throw new NegativeArraySizeException("size");
        }
        synchronized (buffer)
        {
            bufferSize = (size < 1) ? 1 : size;
            buffer.notifyAll();
        }
    }

    public boolean getBlocking()
    {
        return blocking;
    }

    public void setBlocking(final boolean value)
    {
        synchronized (buffer)
        {
            blocking = value;
            buffer.notifyAll();
        }
    }

    public void add(T item)
    {
        if ((consumer == null) || !consumer.isAlive() || (bufferSize <= 0))
        {
            consume(item);
            return;
        }
        synchronized (buffer)
        {
            while (true)
            {
                int previousSize = buffer.size();
                if (previousSize < bufferSize)
                {
                    buffer.add(item);
                    if (previousSize == 0)
                    {
                        buffer.notifyAll();
                    }
                    break;
                }
                boolean discard = true;
                if (blocking && !Thread.interrupted() && Thread.currentThread() != consumer)
                {
                    try
                    {
                        buffer.wait();
                        discard = false;
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                if (discard)
                {
// log.warn("Item {} discarded", item);
                    break;
                }
            }
        }
    }

    public void terminate()
    {
        synchronized (buffer)
        {
            terminated = true;
            buffer.notifyAll();
        }
        try
        {
            consumer.join();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
// log.error("Interrupted waiting for consumer to finish", e);
        }
    }

    public abstract void consume(T item);

    private class Consumer implements Runnable
    {

        public void run()
        {
            boolean isActive = true;
            try
            {
                while (isActive)
                {
                    List<T> items = null;
                    synchronized (buffer)
                    {
                        int bufferSize = buffer.size();
                        isActive = !terminated;
                        while ((bufferSize == 0) && isActive)
                        {
                            buffer.wait();
                            bufferSize = buffer.size();
                            isActive = !terminated;
                        }
                        if (bufferSize > 0)
                        {
                            items = new ArrayList<T>(bufferSize);
                            items.addAll(buffer);
                            buffer.clear();
                            buffer.notifyAll();
                        }
                    }
                    if (items != null)
                    {
                        for (T item : items)
                        {
                            consume(item);
                        }
                    }
                }
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
