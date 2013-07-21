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

package com.diegoschivo.samples.eclipse.jetty.springmvc;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.servlet.DispatcherServlet;


public class AdderControllerTest
{

    private static Server server;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        server = new Server(8080);
        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        wac.setBaseResource(Resource.newClassPathResource("META-INF/webapp"));
        ServletHolder dispatcher = new ServletHolder("dispatcher", DispatcherServlet.class);
        wac.addServlet(dispatcher, "/*");
        server.setHandler(wac);
        server.start();
    }

    @Test
    public void testSum() throws Exception
    {
        HttpGet request = new HttpGet("http://localhost:8080/sum?a=3&b=2");
        request.addHeader("Accept", "text/plain");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpResponse response = httpClient.execute(request);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            assertEquals("5", EntityUtils.toString(response.getEntity()));
        }
        finally
        {
            request.releaseConnection();
        }
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        server.stop();
    }
}
