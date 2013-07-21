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

package com.diegoschivo.samples.apache.cxf.jaxrs.ssl.clientproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.ws.rs.core.Response;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.diegoschivo.samples.apache.cxf.jaxrs.Product;
import com.diegoschivo.samples.apache.cxf.jaxrs.Store;
import com.diegoschivo.samples.apache.cxf.jaxrs.StoreImpl;


public class StoreTest
{

    private static final String BASE_URL = "https://localhost:9000";

    private static final String SERVER_CONFIG_FILE = "com/diegoschivo/samples/apache/cxf/jaxrs/ssl/ServerConfig.xml";

    private static final String CLIENT_CONFIG_FILE = "com/diegoschivo/samples/apache/cxf/jaxrs/ssl/ClientConfig.xml";

    private static Bus bus;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        SpringBusFactory factory = new SpringBusFactory();
        bus = factory.createBus(SERVER_CONFIG_FILE);
        BusFactory.setDefaultBus(bus);
    }

    private Server storeServer;

    private Store storeClient;

    @Before
    public void before() throws Exception
    {
        Store store = new StoreImpl();
        store.addProduct(new Product("FOO", "Lorem ipsum"));

        JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
        serverFactory.setResourceClasses(Store.class);
        serverFactory.setResourceProvider(Store.class, new SingletonResourceProvider(store));
        serverFactory.setAddress(BASE_URL);
        storeServer = serverFactory.create();

        storeClient = JAXRSClientFactory.create(BASE_URL, Store.class, CLIENT_CONFIG_FILE);
    }

    @Test
    public void testGet() throws Exception
    {
        Product product = storeClient.getProduct(1L);
        assertEquals("FOO", product.getCode());
    }

    @Test
    public void testGetUnexisting() throws Exception
    {
        Product product = storeClient.getProduct(2L);
        assertNull(product);
    }

    @Test
    public void testAddProduct() throws Exception
    {
        Product product = new Product("BAR", "Dolor sit amet");
        Response response = storeClient.addProduct(product);
        try
        {
            assertEquals(HttpStatus.SC_OK, response.getStatus());
            assertEquals(Long.valueOf(2), response.readEntity(Product.class).getId());
        }
        finally
        {
            response.close();
        }
    }

    @Test
    public void testUpdateProduct() throws Exception
    {
        Product product = new Product(1L, "BAR", "Dolor sit amet");
        Response response = storeClient.updateProduct(product);
        try
        {
            assertEquals(HttpStatus.SC_OK, response.getStatus());
        }
        finally
        {
            response.close();
        }
    }

    @Test
    public void testUpdateUnexistingProduct() throws Exception
    {
        Product product = new Product(2L, "BAR", "Dolor sit amet");
        Response response = storeClient.updateProduct(product);
        try
        {
            assertEquals(HttpStatus.SC_NOT_MODIFIED, response.getStatus());
        }
        finally
        {
            response.close();
        }
    }

    @Test
    public void testDeleteProduct() throws Exception
    {
        Response response = storeClient.deleteProduct(1L);
        try
        {
            assertEquals(HttpStatus.SC_OK, response.getStatus());
        }
        finally
        {
            response.close();
        }
    }

    @Test
    public void testDeleteUnexistingProduct() throws Exception
    {
        Response response = storeClient.deleteProduct(2L);
        try
        {
            assertEquals(HttpStatus.SC_NOT_MODIFIED, response.getStatus());
        }
        finally
        {
            response.close();
        }
    }

    @After
    public void after() throws Exception
    {
        if (storeServer != null)
        {
            storeServer.stop();
        }
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        bus.shutdown(true);
    }
}
