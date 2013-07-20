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

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class StoreTest
{

    private static final String BASE_URL = "http://localhost:9000";

    private static final String BASE_SERVICE_URL = BASE_URL + "/store/products";

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
        HttpGet request = new HttpGet(BASE_SERVICE_URL + "/1");
        request.addHeader("Accept", "text/xml");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            assertEquals(
                "FOO",
                StringUtils.substringBetween(EntityUtils.toString(response.getEntity()), "<code>", "</code>"));
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testGetUnexisting() throws Exception
    {
        HttpGet request = new HttpGet(BASE_SERVICE_URL + "/2");
        request.addHeader("Accept", "text/xml");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testAddProduct() throws Exception
    {
        HttpPost request = new HttpPost(BASE_SERVICE_URL);
        request.addHeader("Accept", "text/xml");
        request.setEntity(new StringEntity(
            "<Product><code>BAR</code><name>Dolor sit amet</name></Product>",
            ContentType.TEXT_XML));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            assertEquals("2", StringUtils.substringBetween(EntityUtils.toString(response.getEntity()), "<id>", "</id>"));
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testUpdateProduct() throws Exception
    {
        HttpPut request = new HttpPut(BASE_SERVICE_URL);
        request.setEntity(new StringEntity(
            "<Product><id>1</id><code>BAR</code><name>Dolor sit amet</name></Product>",
            ContentType.TEXT_XML));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testUpdateUnexistingProduct() throws Exception
    {
        HttpPut request = new HttpPut(BASE_SERVICE_URL);
        request.setEntity(new StringEntity(
            "<Product><id>2</id><code>BAR</code><name>Dolor sit amet</name></Product>",
            ContentType.TEXT_XML));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_NOT_MODIFIED, response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testDeleteProduct() throws Exception
    {
        HttpDelete request = new HttpDelete(BASE_SERVICE_URL + "/1");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testDeleteUnexistingProduct() throws Exception
    {
        HttpDelete request = new HttpDelete(BASE_SERVICE_URL + "/2");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_NOT_MODIFIED, response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
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
}
