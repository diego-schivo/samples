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

import java.io.File;

import javax.jcr.SimpleCredentials;

import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class InMemBundlePersistenceManagerTest
{

    private static File directory;

    private static JackrabbitRepository repository;

    private JackrabbitSession session;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        directory = new File("target", InMemBundlePersistenceManagerTest.class.getSimpleName());
        repository = RepositoryUtils.createRepository(
            directory,
            InMemBundlePersistenceManagerTest.class.getResource(
                InMemBundlePersistenceManagerTest.class.getSimpleName() + "-repository.xml").openStream());
    }

    @Before
    public void before() throws Exception
    {
        session = (JackrabbitSession) repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        session.getRootNode().addNode("foo");
        session.save();
    }

    @Test
    public void testGetNode() throws Exception
    {
        session.getNode("/foo");
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
