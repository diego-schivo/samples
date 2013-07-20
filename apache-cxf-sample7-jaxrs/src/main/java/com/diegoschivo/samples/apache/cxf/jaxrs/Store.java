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
