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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;


public class TreeIteratorTest
{

    @Test
    public void testNull()
    {
        TreeIterator<String> it = new TreeIterator<String>((String) null)
        {

            @Override
            protected Iterator<String> children(String node)
            {
                return null;
            }
        };
        assertFalse(it.hasNext());
        try
        {
            it.next();
            fail();
        }
        catch (NoSuchElementException e)
        {
        }
    }

    @Test
    public void testSingleton()
    {
        TreeIterator<String> it = new TreeIterator<String>("foo")
        {

            @Override
            protected Iterator<String> children(String node)
            {
                return null;
            }
        };
        assertTrue(it.hasNext());
        assertEquals(it.next(), "foo");
        assertFalse(it.hasNext());
        try
        {
            it.next();
            fail();
        }
        catch (NoSuchElementException e)
        {
        }
    }

    @Test
    public void testOneChild()
    {
        TreeIterator<String> it = new TreeIterator<String>("foo")
        {

            @Override
            protected Iterator<String> children(String node)
            {
                return StringUtils.equals(node, "foo") ? Collections.singleton("bar").iterator() : null;
            }
        };
        assertTrue(it.hasNext());
        assertEquals(it.next(), "foo");
        assertTrue(it.hasNext());
        assertEquals(it.next(), "bar");
        assertFalse(it.hasNext());
    }

    @Test
    public void testTwoChildren()
    {
        TreeIterator<String> it = new TreeIterator<String>("foo")
        {

            @Override
            protected Iterator<String> children(String node)
            {
                return StringUtils.equals(node, "foo") ? Arrays.asList(new String[]{"bar", "baz" }).iterator() : null;
            }
        };
        assertTrue(it.hasNext());
        assertEquals(it.next(), "foo");
        assertTrue(it.hasNext());
        assertEquals(it.next(), "bar");
        assertTrue(it.hasNext());
        assertEquals(it.next(), "baz");
        assertFalse(it.hasNext());
    }

    @Test
    public void testNullNode()
    {
        TreeIterator<String> it = new TreeIterator<String>("foo")
        {

            @Override
            protected Iterator<String> children(String node)
            {
                if (StringUtils.equals(node, "foo"))
                {
                    return Arrays.asList(new String[]{"bar", null, "baz" }).iterator();
                }
                else if (node == null)
                {
                    return Collections.singleton("qux").iterator();
                }
                else
                {
                    return null;
                }
            }
        };
        assertTrue(it.hasNext());
        assertEquals(it.next(), "foo");
        assertTrue(it.hasNext());
        assertEquals(it.next(), "bar");
        assertTrue(it.hasNext());
        assertEquals(it.next(), null);
        assertTrue(it.hasNext());
        assertEquals(it.next(), "qux");
        assertTrue(it.hasNext());
        assertEquals(it.next(), "baz");
        assertFalse(it.hasNext());
    }

    @Test
    public void testDepthThree()
    {
        TreeIterator<String> it = new TreeIterator<String>("a")
        {

            @Override
            protected Iterator<String> children(String node)
            {
                String[] children = null;
                if (StringUtils.equals(node, "a"))
                {
                    children = new String[]{"b", "c" };
                }
                else if (StringUtils.equals(node, "b"))
                {
                    children = new String[]{"d" };
                }
                else if (StringUtils.equals(node, "c"))
                {
                    children = new String[]{"e", "f" };
                }
                return children != null ? Arrays.asList(children).iterator() : null;
            }
        };
        assertEquals(it.next(), "a");
        assertEquals(it.next(), "b");
        assertEquals(it.next(), "d");
        assertEquals(it.next(), "c");
        assertEquals(it.next(), "e");
        assertEquals(it.next(), "f");
    }
}
