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

package com.diegoschivo.samples.apache.cxf.jaxrs.security;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.security.access.annotation.Secured;

import com.diegoschivo.samples.apache.cxf.jaxrs.Product;


@Path("/store/")
public interface SecuredStore
{

    @Secured({"ROLE_USER", "ROLE_ADMIN" })
    @GET
    @Path("/products/{id}")
    public Product getProduct(@PathParam("id") Long id);

    @Secured("ROLE_ADMIN")
    @POST
    @Path("/products/")
    public Response addProduct(Product product);

    @Secured("ROLE_ADMIN")
    @PUT
    @Path("/products/")
    public Response updateProduct(Product product);

    @Secured("ROLE_ADMIN")
    @DELETE
    @Path("/products/{id}")
    public Response deleteProduct(@PathParam("id") Long id);
}
