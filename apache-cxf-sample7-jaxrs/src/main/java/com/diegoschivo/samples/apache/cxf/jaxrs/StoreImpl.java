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

package com.diegoschivo.samples.apache.cxf.jaxrs;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;


public class StoreImpl implements Store
{

    private long currentId;

    private Map<Long, Product> products = new HashMap<Long, Product>();

    public Product getProduct(Long id)
    {
        return products.get(id);
    }

    public Response addProduct(Product product)
    {
        product.setId(++currentId);
        products.put(product.getId(), product);
        return Response.ok(product).build();
    }

    public Response updateProduct(Product product)
    {
        Product existing = products.get(product.getId());
        if (existing != null)
        {
            products.put(product.getId(), product);
            return Response.ok().build();
        }
        else
        {
            return Response.notModified().build();
        }
    }

    public Response deleteProduct(Long id)
    {
        Product existing = products.get(id);
        if (existing != null)
        {
            products.remove(id);
            return Response.ok().build();
        }
        else
        {
            return Response.notModified().build();
        }
    }
}
