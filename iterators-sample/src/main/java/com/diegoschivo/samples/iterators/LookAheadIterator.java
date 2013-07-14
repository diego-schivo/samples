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

import java.util.Iterator;


public class LookAheadIterator<T> extends WrapperIterator<T>
{

    private T item;

    private boolean itemAvailable = false;

    public LookAheadIterator(Iterator<T> iterator)
    {
        super(iterator);
    }

    @Override
    public boolean hasNext()
    {
        return itemAvailable || super.hasNext();
    }

    @Override
    public T next()
    {
        if (itemAvailable)
        {
            itemAvailable = false;
            return item;
        }
        return super.next();
    }

    public T lookAhead()
    {
        if (!itemAvailable)
        {
            item = super.next();
            itemAvailable = true;
        }
        return item;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
