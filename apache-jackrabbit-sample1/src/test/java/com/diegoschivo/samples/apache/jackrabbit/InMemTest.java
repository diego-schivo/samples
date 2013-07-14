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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.SessionImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.apache.jackrabbit.core.security.user.UserImpl;
import org.apache.jackrabbit.core.security.user.UserManagerImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class InMemTest
{

    private static File repoDir;

    private static JackrabbitRepository repo;

    private Session session;

    @BeforeClass
    public static void installRepository() throws Exception
    {
        repoDir = new File("target", InMemTest.class.getSimpleName());
        if (!repoDir.exists())
        {
            repoDir.mkdirs();
        }
        File repoXml = new File(repoDir, "repository.xml");
        if (!repoXml.exists())
        {
            InputStream input = null;
            OutputStream output = null;
            try
            {
                input = InMemTest.class.getResource("inmem-repository.xml").openStream();
                output = new FileOutputStream(repoXml);
                IOUtils.copy(input, output);
            }
            finally
            {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
        }
        repo = RepositoryImpl.create(RepositoryConfig.install(repoDir));
    }

    @Test
    public void testUsersPath() throws Exception
    {
        session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
        UserManager userManager = ((SessionImpl) session).getUserManager();
        String usersPath = ((UserManagerImpl) userManager).getUsersPath();
        assertEquals("/rep:security/rep:authorizables/rep:users", usersPath);
    }

    @Test
    public void testAdmin() throws Exception
    {
        session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
        UserManager userManager = ((SessionImpl) session).getUserManager();
        Authorizable admin = userManager.getAuthorizable("admin");
        assertEquals("admin", admin.getPrincipal().getName());
        assertTrue(((UserImpl) admin).isAdmin());
    }

    @After
    public void closeSession() throws Exception
    {
        if (session != null)
        {
            session.logout();
        }
    }

    @AfterClass
    public static void deleteRepository() throws Exception
    {
        repo.shutdown();
        FileUtils.deleteQuietly(repoDir);
    }
}
