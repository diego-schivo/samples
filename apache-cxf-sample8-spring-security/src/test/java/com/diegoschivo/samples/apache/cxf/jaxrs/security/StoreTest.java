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

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class StoreTest
{

    private static final String BASE_URL = "http://localhost:8080";

    private static final String BASE_SERVICE_URL = BASE_URL + "/store";

    private static Server jettyServer;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        jettyServer = new Server(8080);
        WebAppContext wac = new WebAppContext();
        jettyServer.setHandler(wac);
        wac.setContextPath("/");
        wac.setBaseResource(Resource.newResource("src/main/webapp"));
        jettyServer.start();
    }

// private Store storeClient;
//
// @Before
// public void before() throws Exception
// {
// storeClient = JAXRSClientFactory.create(BASE_URL, Store.class);
// }

    @Test
    public void testGetProduct() throws Exception
    {
        HttpGet request = new HttpGet(BASE_SERVICE_URL + "/products/1");
        request.addHeader("Accept", "text/xml");
        request.addHeader("Authorization", "Basic " + Base64Utility.encode("user:user".getBytes()));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());
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
    public void testGetProductNoCredentials() throws Exception
    {
        HttpGet request = new HttpGet(BASE_SERVICE_URL + "/products/1");
        request.addHeader("Accept", "text/xml");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testGetProductWrongPassword() throws Exception
    {
        HttpGet request = new HttpGet(BASE_SERVICE_URL + "/products/1");
        request.addHeader("Accept", "text/xml");
        request.addHeader("Authorization", "Basic " + Base64Utility.encode("user:wrong".getBytes()));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testAddProduct() throws Exception
    {
        HttpPost request = new HttpPost(BASE_SERVICE_URL + "/products");
        request.addHeader("Accept", "text/xml");
        request.addHeader("Authorization", "Basic " + Base64Utility.encode("admin:admin".getBytes()));
        request.setEntity(new StringEntity(
            "<Product><code>BAR</code><name>Dolor sit amet</name></Product>",
            ContentType.TEXT_XML));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());
            assertEquals("2", StringUtils.substringBetween(EntityUtils.toString(response.getEntity()), "<id>", "</id>"));
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @Test
    public void testAddProductNonAdmin() throws Exception
    {
        HttpPost request = new HttpPost(BASE_SERVICE_URL + "/products");
        request.addHeader("Accept", "text/xml");
        request.addHeader("Authorization", "Basic " + Base64Utility.encode("user:user".getBytes()));
        request.setEntity(new StringEntity(
            "<Product><code>BAR</code><name>Dolor sit amet</name></Product>",
            ContentType.TEXT_XML));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatusLine().getStatusCode());
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        jettyServer.stop();
    }
}
