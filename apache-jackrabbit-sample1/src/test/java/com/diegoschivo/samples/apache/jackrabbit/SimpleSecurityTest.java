package com.diegoschivo.samples.apache.jackrabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.GuestCredentials;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.principal.PrincipalIterator;
import org.apache.jackrabbit.api.security.principal.PrincipalManager;
import org.apache.jackrabbit.core.security.AnonymousPrincipal;
import org.apache.jackrabbit.core.security.principal.AdminPrincipal;
import org.apache.jackrabbit.core.security.principal.EveryonePrincipal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class SimpleSecurityTest
{

    private static File directory;

    private static JackrabbitRepository repository;

    private JackrabbitSession session;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        directory = new File("target", SimpleSecurityTest.class.getSimpleName());
        repository = RepositoryUtils.createRepository(
            directory,
            SimpleSecurityTest.class
                .getResource(SimpleSecurityTest.class.getSimpleName() + "-repository.xml")
                .openStream());
    }

    @Before
    public void before() throws Exception
    {
        session = (JackrabbitSession) repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        Node foo = session.getRootNode().addNode("foo");
        foo.setProperty("message", "Hello World!");
        session.save();
    }

    @Test
    public void testAdmin() throws Exception
    {
        assertEquals("Hello World!", session.getNode("/foo").getProperty("message").getString());
    }

    @Test
    public void testAnonymous() throws Exception
    {
        Session s = repository.login(new GuestCredentials());
        Node foo = s.getNode("/foo");
        assertEquals("Hello World!", foo.getProperty("message").getString());
        foo.addNode("bar");
        try
        {
            s.save();
            fail();
        }
        catch (AccessDeniedException e)
        {
        }
    }

    @Test
    public void testPrincipals() throws Exception
    {
        PrincipalManager principalManager = session.getPrincipalManager();
        Map<String, Principal> principals = new HashMap<String, Principal>();
        for (PrincipalIterator it = principalManager.getPrincipals(PrincipalManager.SEARCH_TYPE_ALL); it.hasNext();)
        {
            Principal principal = it.nextPrincipal();
            principals.put(principal.getName(), principal);
        }
        assertEquals(3, principals.size());
        assertTrue(principals.get("admin") instanceof AdminPrincipal);
        assertTrue(principals.get("anonymous") instanceof AnonymousPrincipal);
        assertTrue(principals.get("everyone") instanceof EveryonePrincipal);
    }

    @Test
    public void testUserManager() throws Exception
    {
        try
        {
            session.getUserManager();
            fail();
        }
        catch (UnsupportedRepositoryOperationException e)
        {
        }
    }

    @After
    public void after() throws Exception
    {
        if (session != null)
        {
            if (session.nodeExists("/foo"))
            {
                session.removeItem("/foo");
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
