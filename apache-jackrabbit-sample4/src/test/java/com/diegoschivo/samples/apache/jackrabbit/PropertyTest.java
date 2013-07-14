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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.SimpleCredentials;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class PropertyTest
{

    private static File directory;

    private static JackrabbitRepository repository;

    private JackrabbitSession session;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        directory = new File("target", PropertyTest.class.getSimpleName());
        repository = RepositoryUtils.createRepository(
            directory,
            PropertyTest.class.getResource(PropertyTest.class.getSimpleName() + "-repository.xml").openStream());
    }

    @Before
    public void before() throws Exception
    {
        session = (JackrabbitSession) repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        session.getRootNode().addNode("foo");
        session.save();
    }

    @Test
    public void testBinary() throws Exception
    {
        Node file = session.getNode("/foo").addNode("bar", "nt:file");
        Node resource = file.addNode("jcr:content", "nt:resource");
        resource.setProperty("jcr:lastModified", Calendar.getInstance());
        resource.setProperty("jcr:encoding", "UTF-8");
        resource.setProperty("jcr:mimeType", "text/plain");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        writer.write("Hello World!");
        writer.flush();
        resource.setProperty(
            "jcr:data",
            session.getValueFactory().createBinary(new ByteArrayInputStream(out.toByteArray())));
        session.save();
        InputStream in = session.getNode("/foo/bar/jcr:content").getProperty("jcr:data").getBinary().getStream();
        assertEquals("Hello World!", IOUtils.readLines(in, "UTF-8").get(0));
    }

    @After
    public void after() throws Exception
    {
        if (session != null)
        {
            if (session.nodeExists("/foo"))
            {
                session.removeItem("/foo");
                session.save();
            }
            session.logout();
        }
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        repository.shutdown();
        FileUtils.deleteQuietly(directory);
    }
}
