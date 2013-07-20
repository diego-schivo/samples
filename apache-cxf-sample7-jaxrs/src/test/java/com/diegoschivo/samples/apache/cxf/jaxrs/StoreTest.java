package com.diegoschivo.samples.apache.cxf.jaxrs;

import static org.junit.Assert.assertEquals;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.junit.Before;
import org.junit.Test;


public class StoreTest
{

    @Before
    public void before() throws Exception
    {
        Store store = new Store();
        store.addProduct(new Product("FOO", "Lorem ipsum"));
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(Store.class);
        sf.setResourceProvider(Store.class, new SingletonResourceProvider(store));
        sf.setAddress("http://localhost:9000/");
        sf.create();
    }

    @Test
    public void testGet() throws Exception
    {
        GetMethod method = new GetMethod("http://localhost:9000/store/products/1");
        method.addRequestHeader("Accept", "text/xml");
        HttpClient httpclient = new HttpClient();
        try
        {
            assertEquals(HttpStatus.SC_OK, httpclient.executeMethod(method));
            assertEquals("FOO", StringUtils.substringBetween(method.getResponseBodyAsString(), "<code>", "</code>"));
        }
        finally
        {
            method.releaseConnection();
        }
    }

    @Test
    public void testGetUnexisting() throws Exception
    {
        GetMethod method = new GetMethod("http://localhost:9000/store/products/2");
        method.addRequestHeader("Accept", "text/xml");
        HttpClient httpclient = new HttpClient();
        try
        {
            assertEquals(HttpStatus.SC_NO_CONTENT, httpclient.executeMethod(method));
        }
        finally
        {
            method.releaseConnection();
        }
    }

    @Test
    public void testAddProduct() throws Exception
    {
        PostMethod method = new PostMethod("http://localhost:9000/store/products");
        method.addRequestHeader("Accept", "text/xml");
        method.setRequestEntity(new StringRequestEntity(
            "<Product><code>BAR</code><name>Dolor sit amet</name></Product>",
            "text/xml",
            "ISO-8859-1"));
        HttpClient httpclient = new HttpClient();
        try
        {
            assertEquals(HttpStatus.SC_OK, httpclient.executeMethod(method));
            assertEquals("2", StringUtils.substringBetween(method.getResponseBodyAsString(), "<id>", "</id>"));
        }
        finally
        {
            method.releaseConnection();
        }
    }

    @Test
    public void testUpdateProduct() throws Exception
    {
        PutMethod put = new PutMethod("http://localhost:9000/store/products");
        put.setRequestEntity(new StringRequestEntity(
            "<Product><id>1</id><code>BAR</code><name>Dolor sit amet</name></Product>",
            "text/xml",
            "ISO-8859-1"));
        HttpClient httpclient = new HttpClient();
        try
        {
            assertEquals(HttpStatus.SC_OK, httpclient.executeMethod(put));
        }
        finally
        {
            put.releaseConnection();
        }
    }

    @Test
    public void testUpdateUnexistingProduct() throws Exception
    {
        PutMethod method = new PutMethod("http://localhost:9000/store/products");
        method.setRequestEntity(new StringRequestEntity(
            "<Product><id>2</id><code>BAR</code><name>Dolor sit amet</name></Product>",
            "text/xml",
            "ISO-8859-1"));
        HttpClient httpclient = new HttpClient();
        try
        {
            assertEquals(HttpStatus.SC_NOT_MODIFIED, httpclient.executeMethod(method));
        }
        finally
        {
            method.releaseConnection();
        }
    }

    @Test
    public void testDeleteProduct() throws Exception
    {
        DeleteMethod method = new DeleteMethod("http://localhost:9000/store/products/1");
        HttpClient httpclient = new HttpClient();
        try
        {
            assertEquals(HttpStatus.SC_OK, httpclient.executeMethod(method));
        }
        finally
        {
            method.releaseConnection();
        }
    }

    @Test
    public void testDeleteUnexistingProduct() throws Exception
    {
        DeleteMethod method = new DeleteMethod("http://localhost:9000/store/products/2");
        HttpClient httpclient = new HttpClient();
        try
        {
            assertEquals(HttpStatus.SC_NOT_MODIFIED, httpclient.executeMethod(method));
        }
        finally
        {
            method.releaseConnection();
        }
    }
}
