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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;


@Path("/store/")
public class Store
{

    private long currentId;

    private Map<Long, Product> products = new HashMap<Long, Product>();

    @GET
    @Path("/products/{id}/")
    public Product getProduct(@PathParam("id") String id)
    {
        return products.get(Long.parseLong(id));
    }

    @POST
    @Path("/products/")
    public Response addProduct(Product product)
    {
        product.setId(++currentId);
        products.put(product.getId(), product);
        return Response.ok(product).build();
    }

    @PUT
    @Path("/products/")
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

    @DELETE
    @Path("/products/{id}")
    public Response deleteProduct(@PathParam("id") String id)
    {
        Product existing = products.get(Long.parseLong(id));
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
