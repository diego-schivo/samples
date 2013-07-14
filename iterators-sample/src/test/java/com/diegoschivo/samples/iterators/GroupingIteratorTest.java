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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;


public class GroupingIteratorTest
{

    private static final Comparator<String> INITIALS_COMPARATOR = new Comparator<String>()
    {

        public int compare(String str1, String str2)
        {
            String in1 = StringUtils.substring(str1, 0, 1);
            String in2 = StringUtils.substring(str2, 0, 1);
            if (in1 != null)
            {
                return in2 != null ? in1.compareTo(in2) : 1;
            }
            else
            {
                return in2 != null ? -1 : 0;
            }
        }
    };

    @Test
    public void testThreeGroups()
    {
        Iterator<String> it = Arrays.asList(new String[]{"foo", "bar", "baz", "qux" }).iterator();
        GroupingIterator<String> gi = new GroupingIterator<String>(it, INITIALS_COMPARATOR);

        Iterator<String> it1 = gi.next();
        assertEquals("foo", it1.next());
        assertFalse(it1.hasNext());

        Iterator<String> it2 = gi.next();
        assertEquals("bar", it2.next());
        assertTrue(it2.hasNext());
        assertEquals("baz", it2.next());
        assertFalse(it2.hasNext());

        Iterator<String> it3 = gi.next();
        assertEquals("qux", it3.next());
        assertFalse(it3.hasNext());
    }

    @Test
    public void testThreeGroupsSkipItem()
    {
        Iterator<String> it = Arrays.asList(new String[]{"foo", "bar", "baz", "qux" }).iterator();
        GroupingIterator<String> gi = new GroupingIterator<String>(it, INITIALS_COMPARATOR);

        Iterator<String> it1 = gi.next();
        assertEquals("foo", it1.next());
        assertFalse(it1.hasNext());

        Iterator<String> it2 = gi.next();
        assertEquals("bar", it2.next());
        assertTrue(it2.hasNext());

        Iterator<String> it3 = gi.next();
        assertEquals("qux", it3.next());
        assertFalse(it3.hasNext());
    }

    @Test
    public void testThreeGroupsSkipGroup()
    {
        Iterator<String> it = Arrays.asList(new String[]{"foo", "bar", "baz", "qux" }).iterator();
        GroupingIterator<String> gi = new GroupingIterator<String>(it, INITIALS_COMPARATOR);

        Iterator<String> it1 = gi.next();
        assertEquals("foo", it1.next());
        assertFalse(it1.hasNext());

        gi.next();

        Iterator<String> it3 = gi.next();
        assertEquals("qux", it3.next());
        assertFalse(it3.hasNext());
    }

    @Test
    public void testNullGroup()
    {
        Iterator<String> it = Arrays.asList(new String[]{"foo", null, null, "bar" }).iterator();
        GroupingIterator<String> gi = new GroupingIterator<String>(it, INITIALS_COMPARATOR);

        Iterator<String> it1 = gi.next();
        assertEquals("foo", it1.next());
        assertFalse(it1.hasNext());

        Iterator<String> it2 = gi.next();
        assertNull(it2.next());
        assertTrue(it2.hasNext());
        assertNull(it2.next());
        assertFalse(it2.hasNext());

        Iterator<String> it3 = gi.next();
        assertEquals("bar", it3.next());
        assertFalse(it3.hasNext());
    }
}
