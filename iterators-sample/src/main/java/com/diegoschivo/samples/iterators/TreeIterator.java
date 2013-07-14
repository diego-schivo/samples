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

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public abstract class TreeIterator<T> implements Iterator<T>
{

    private Boolean hasNext;

    private T item;

    private boolean read;

    private final Deque<Iterator<T>> stack = new LinkedList<Iterator<T>>();

    public TreeIterator(T node)
    {
        this(node != null ? Collections.singleton(node).iterator() : null);
    }

    public TreeIterator(Iterator<T> nodes)
    {
        if (nodes != null)
        {
            stack.add(nodes);
        }
    }

    public boolean hasNext()
    {
        if (hasNext == null)
        {
            if (!read)
            {
                hasNext = !stack.isEmpty() && stack.peek().hasNext();
            }
            else
            {
                Iterator<T> iter = children(item);
                read = false;
                if (iter != null && iter.hasNext())
                {
                    stack.push(iter);
                    hasNext = true;
                }
                else
                {
                    while (!stack.isEmpty() && !(iter = stack.peek()).hasNext())
                    {
                        stack.pop();
                    }
                    hasNext = iter.hasNext();
                }
            }
        }
        return hasNext;
    }

    public T next()
    {
        if (!hasNext())
        {
            throw new NoSuchElementException();
        }
        if (!read)
        {
            item = stack.peek().next();
            read = true;
            hasNext = null;
        }
        return item;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    protected abstract Iterator<T> children(T node);
}
