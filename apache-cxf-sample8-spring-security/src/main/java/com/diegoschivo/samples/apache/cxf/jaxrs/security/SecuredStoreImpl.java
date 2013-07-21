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

import java.util.Map;

import javax.ws.rs.core.Response;

import com.diegoschivo.samples.apache.cxf.jaxrs.Product;
import com.diegoschivo.samples.apache.cxf.jaxrs.StoreImpl;


public class SecuredStoreImpl implements SecuredStore
{

    private StoreImpl impl = new StoreImpl();

    public void setProducts(Map<Long, Product> products)
    {
        impl.setProducts(products);
    }

    public Product getProduct(Long id)
    {
        return impl.getProduct(id);
    }

    public Response addProduct(Product product)
    {
        return impl.addProduct(product);
    }

    public Response updateProduct(Product product)
    {
        return impl.updateProduct(product);
    }

    public Response deleteProduct(Long id)
    {
        return impl.deleteProduct(id);
    }
}
