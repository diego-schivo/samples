package com.diegoschivo.samples.threads;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class AsyncProcessorTest
{

    @Test
    public void foo()
    {
        final List<String> list = new ArrayList<String>();
        AsyncProcessor<String> ap = new AsyncProcessor<String>()
        {

            @Override
            public void consume(String item)
            {
                list.add(item);
            }
        };
        ap.add("foo");
        ap.add("bar");
        ap.add("baz");
        ap.add("qux");
        ap.terminate();
        assertEquals(list.size(), 4);
        assertEquals(list.get(0), "foo");
        assertEquals(list.get(1), "bar");
        assertEquals(list.get(2), "baz");
        assertEquals(list.get(3), "qux");
    }
}
