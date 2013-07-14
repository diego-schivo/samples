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
