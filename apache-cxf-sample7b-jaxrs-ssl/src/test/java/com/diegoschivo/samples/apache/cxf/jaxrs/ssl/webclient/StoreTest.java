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

package com.diegoschivo.samples.apache.cxf.jaxrs.ssl.webclient;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
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

    private static final String BASE_SERVICE_URL = BASE_URL + "/store/products";

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

    private Server server;

    @Before
    public void before() throws Exception
    {
        Store store = new StoreImpl();
        store.addProduct(new Product("FOO", "Lorem ipsum"));

        JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
        serverFactory.setResourceClasses(Store.class);
        serverFactory.setResourceProvider(Store.class, new SingletonResourceProvider(store));
        serverFactory.setAddress(BASE_URL);
        server = serverFactory.create();
    }

    @Test
    public void testGet() throws Exception
    {
        WebClient webClient = WebClient.create(BASE_SERVICE_URL + "/1", CLIENT_CONFIG_FILE);
        Response response = webClient.get();
        try
        {
            assertEquals(HttpStatus.SC_OK, response.getStatus());
            assertEquals("FOO", response.readEntity(Product.class).getCode());
        }
        finally
        {
            response.close();
        }
    }

    @Test
    public void testGetUnexisting() throws Exception
    {
        WebClient webClient = WebClient.create(BASE_SERVICE_URL + "/2", CLIENT_CONFIG_FILE);
        Response response = webClient.get();
        try
        {
            assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatus());
        }
        finally
        {
            response.close();
        }
    }

    @Test
    public void testAddProduct() throws Exception
    {
        WebClient webClient = WebClient.create(BASE_SERVICE_URL, CLIENT_CONFIG_FILE);
        Product product = new Product("BAR", "Dolor sit amet");
        Response response = webClient.post(product);
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
        WebClient webClient = WebClient.create(BASE_SERVICE_URL, CLIENT_CONFIG_FILE);
        Product product = new Product(1L, "BAR", "Dolor sit amet");
        Response response = webClient.put(product);
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
        WebClient webClient = WebClient.create(BASE_SERVICE_URL, CLIENT_CONFIG_FILE);
        Product product = new Product(2L, "BAR", "Dolor sit amet");
        Response response = webClient.put(product);
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
        WebClient webClient = WebClient.create(BASE_SERVICE_URL + "/1", CLIENT_CONFIG_FILE);
        Response response = webClient.delete();
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
        WebClient webClient = WebClient.create(BASE_SERVICE_URL + "/2", CLIENT_CONFIG_FILE);
        Response response = webClient.delete();
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
        if (server != null)
        {
            server.stop();
        }
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        bus.shutdown(true);
    }
}
