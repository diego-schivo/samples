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

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class GroupingIterator<T> implements Iterator<Iterator<T>>
{

    private LookAheadIterator<T> lai;

    private Comparator<T> comparator;

    private boolean groupRead;

    private Iterator<T> group;

    private boolean exausted;

    public GroupingIterator(Iterator<T> iterator, Comparator<T> comparator)
    {
        lai = new LookAheadIterator<T>(iterator);
        this.comparator = comparator;
    }

    public boolean hasNext()
    {
        if (!exausted && !groupRead)
        {
            if (group != null)
            {
                while (group.hasNext())
                {
                    group.next();
                }
            }
            exausted = !lai.hasNext();
            if (!exausted)
            {
                group = new Iterator<T>()
                {

                    private boolean started;

                    private boolean itemRead;

                    private T item;

                    private T previousItem;

                    private boolean finished;

                    public boolean hasNext()
                    {
                        if (!finished && !itemRead)
                        {
                            finished = !lai.hasNext();
                            if (!finished && (!started || comparator.compare(lai.lookAhead(), previousItem) == 0))
                            {
                                item = lai.next();
                                itemRead = true;
                            }
                            else
                            {
                                finished = true;
                            }
                            started = true;
                        }
                        return !finished;
                    }

                    public T next()
                    {
                        if (hasNext())
                        {
                            itemRead = false;
                            return (previousItem = item);
                        }
                        else
                        {
                            throw new NoSuchElementException();
                        }
                    }

                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
                groupRead = true;
            }
        }
        return !exausted;
    }

    public Iterator<T> next()
    {
        if (hasNext())
        {
            groupRead = false;
            return group;
        }
        else
        {
            throw new NoSuchElementException();
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
