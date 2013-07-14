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

package com.diegoschivo.samples.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;


public class LookAheadIteratorTest
{

    @Test
    public void testNull()
    {
        LookAheadIterator<String> lai = new LookAheadIterator<String>(null);
        try
        {
            lai.lookAhead();
            fail();
        }
        catch (NullPointerException e)
        {
        }
    }

    @Test
    public void testEmpty()
    {
        Iterator<String> it = Collections.<String> emptyList().iterator();
        LookAheadIterator<String> lai = new LookAheadIterator<String>(it);
        try
        {
            lai.lookAhead();
            fail();
        }
        catch (NoSuchElementException e)
        {
        }
    }

    @Test
    public void testSingleton()
    {
        Iterator<String> it = Collections.singletonList("foo").iterator();
        LookAheadIterator<String> lai = new LookAheadIterator<String>(it);
        assertTrue(lai.hasNext());
        assertEquals("foo", lai.lookAhead());
        assertTrue(lai.hasNext());
        assertEquals("foo", lai.lookAhead());
        assertEquals("foo", lai.next());
        assertFalse(lai.hasNext());
        try
        {
            lai.lookAhead();
            fail();
        }
        catch (NoSuchElementException e)
        {
        }
    }

    @Test
    public void testTwoItems()
    {
        Iterator<String> it = Arrays.asList(new String[]{"foo", "bar" }).iterator();
        LookAheadIterator<String> lai = new LookAheadIterator<String>(it);
        assertEquals("foo", lai.lookAhead());
        assertEquals("foo", lai.next());
        assertEquals("bar", lai.lookAhead());
        assertTrue(lai.hasNext());
        assertEquals("bar", lai.next());
    }
}
