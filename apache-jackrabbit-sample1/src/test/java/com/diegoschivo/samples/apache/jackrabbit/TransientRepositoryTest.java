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

package com.diegoschivo.samples.apache.jackrabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TransientRepositoryTest
{

    private static File tempDir;

    private static Repository repository;

    private Session session;

    @BeforeClass
    public static void createRepository() throws IOException
    {
        tempDir = File.createTempFile("repository", "");
        tempDir.delete();
        tempDir.mkdir();
        repository = new TransientRepository();// tempDir);
    }

    @Test
    public void testGetDescriptor() throws RepositoryException
    {
        assertEquals("2.0", repository.getDescriptor(Repository.SPEC_VERSION_DESC));
        assertEquals(
            "Content Repository API for Java(TM) Technology Specification",
            repository.getDescriptor(Repository.SPEC_NAME_DESC));
        assertEquals("Apache Software Foundation", repository.getDescriptor(Repository.REP_VENDOR_DESC));
        assertEquals("http://jackrabbit.apache.org/", repository.getDescriptor(Repository.REP_VENDOR_URL_DESC));
        assertEquals("Jackrabbit", repository.getDescriptor(Repository.REP_NAME_DESC));
        assertEquals("2.4.4", repository.getDescriptor(Repository.REP_VERSION_DESC));
    }

    @Test
    public void testLoginAnonymous() throws RepositoryException
    {
        session = repository.login();
        assertEquals("anonymous", session.getUserID());
    }

    @Test
    public void testLoginCredentials() throws RepositoryException
    {
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        assertEquals("admin", session.getUserID());
    }

    @Test
    public void testAddAndGetNode() throws RepositoryException
    {
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        session.getRootNode().addNode("foo");
        session.save();
        Node node = session.getNode("/foo");
        assertNotNull(node);
        assertEquals("foo", node.getName());
    }

    @Test
    public void testAddAndRemoveNode() throws RepositoryException
    {
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        session.getRootNode().addNode("foo");
        session.save();
        session.getNode("/foo").remove();
        session.save();
        try
        {
            System.out.println(session.getNode("/foo").getNodes());
            fail();
        }
        catch (PathNotFoundException e)
        {
        }
    }

    @Test
    public void testSetAndGetProperty() throws RepositoryException
    {
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        session.getRootNode().setProperty("foo", "bar");
        session.save();
        Property property = session.getRootNode().getProperty("foo");
        assertNotNull(property);
        assertEquals("bar", property.getString());
    }

    @Test
    public void testImportXML() throws RepositoryException, IOException
    {
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        InputStream in = null;
        try
        {
            in = new FileInputStream("src/test/resources/foo.xml");
            session.importXML("/", in, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
            session.save();
            Node node = session.getNode("/foo/baz/qux");
            assertEquals("Hello World!", node.getProperty("message").getString());
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
        }
    }

    @Test
    public void testFoo() throws RepositoryException
    {
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        System.out.println(session.getWorkspace().getName());
    }

    @After
    public void removeAndLogout() throws RepositoryException
    {
        if (session != null)
        {
            if (session.getRootNode().hasNode("foo"))
            {
                session.getNode("/foo").remove();
            }
            session.save();
            session.logout();
        }
    }

    @AfterClass
    public static void destroyRepository()
    {
        tempDir.delete();
    }
}
