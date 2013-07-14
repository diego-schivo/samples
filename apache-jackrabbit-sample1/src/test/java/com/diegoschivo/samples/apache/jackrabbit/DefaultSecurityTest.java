package com.diegoschivo.samples.apache.jackrabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.AccessDeniedException;
import javax.jcr.GuestCredentials;
import javax.jcr.PathNotFoundException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;

import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.principal.PrincipalIterator;
import org.apache.jackrabbit.api.security.principal.PrincipalManager;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.Query;
import org.apache.jackrabbit.api.security.user.QueryBuilder;
import org.apache.jackrabbit.api.security.user.QueryBuilder.Direction;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DefaultSecurityTest
{

    private static File directory;

    private static JackrabbitRepository repository;

    private JackrabbitSession session;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        directory = new File("target", DefaultSecurityTest.class.getSimpleName());
        repository = RepositoryUtils.createRepository(
            directory,
            DefaultSecurityTest.class
                .getResource(DefaultSecurityTest.class.getSimpleName() + "-repository.xml")
                .openStream());
    }

    @Before
    public void before() throws Exception
    {
        session = (JackrabbitSession) repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        session.getRootNode().addNode("foo");
        session.save();
    }

    @Test
    public void testAddNode() throws Exception
    {
        UserManager userManager = session.getUserManager();
        User admin = (User) userManager.getAuthorizable("admin");
        assertTrue(admin.isAdmin());
        AccessControlManager acManager = session.getAccessControlManager();
        Set<Privilege> privileges = new HashSet<Privilege>();
        for (Privilege privilege : acManager.getPrivileges("/foo"))
        {
            privileges.add(privilege);
        }
        assertEquals(1, privileges.size());
        assertTrue(privileges.contains(PrivilegeUtils.getPrivilege(Privilege.JCR_ALL, session.getWorkspace())));
        session.getNode("/foo").addNode("bar");
        session.save();

        User anonymous = (User) userManager.getAuthorizable("anonymous");
        assertFalse(anonymous.isAdmin());
        Session s = repository.login(new GuestCredentials());
        acManager = s.getAccessControlManager();
        privileges.clear();
        for (Privilege privilege : acManager.getPrivileges("/foo"))
        {
            privileges.add(privilege);
        }
        assertEquals(1, privileges.size());
        assertTrue(privileges.contains(PrivilegeUtils.getPrivilege(Privilege.JCR_READ, session.getWorkspace())));
        s.getNode("/foo").addNode("baz");
        try
        {
            s.save();
            fail();
        }
        catch (AccessDeniedException e)
        {
        }
        finally
        {
            s.logout();
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
            Authorizable authorizable = session.getUserManager().getAuthorizable(principal);
            if (authorizable.isGroup() || !((User) authorizable).isDisabled())
            {
                principals.put(principal.getName(), principal);
            }
        }
        assertEquals(5, principals.size());
        assertTrue(principals.containsKey("admin"));
        assertTrue(principals.containsKey("administrators"));
        assertTrue(principals.containsKey("anonymous"));
        assertTrue(principals.containsKey("GroupAdmin"));
        assertTrue(principals.containsKey("UserAdmin"));
    }

    @Test
    public void testUsers() throws Exception
    {
        UserManager userManager = session.getUserManager();
        List<Authorizable> users = new ArrayList<Authorizable>();
        for (Iterator<Authorizable> it = userManager.findAuthorizables(new Query()
        {

            public <T> void build(QueryBuilder<T> builder)
            {
                builder.setSelector(User.class);
                builder.setSortOrder("@name", Direction.ASCENDING);
            }
        }); it.hasNext();)
        {
            User user = (User) it.next();
            assertFalse(user.isGroup());
            if (!user.isDisabled())
            {
                users.add(user);
            }
        }
        assertEquals(2, users.size());
        assertEquals("admin", users.get(0).getID());
        assertEquals("anonymous", users.get(1).getID());
    }

    @Test
    public void testGroups() throws Exception
    {
        UserManager userManager = session.getUserManager();
        Map<String, Authorizable> groups = new HashMap<String, Authorizable>();
        for (Iterator<Authorizable> it = userManager.findAuthorizables(new Query()
        {

            public <T> void build(QueryBuilder<T> builder)
            {
                builder.setSelector(Group.class);
            }
        }); it.hasNext();)
        {
            Authorizable group = it.next();
            assertTrue(group.isGroup());
            groups.put(group.getID(), group);
        }
        assertEquals(3, groups.size());
        assertTrue(groups.containsKey("administrators"));
        assertTrue(groups.containsKey("GroupAdmin"));
        assertTrue(groups.containsKey("UserAdmin"));
    }

    @Test
    public void testAllowWrite() throws Exception
    {
        session.getUserManager().createUser("user1", "secret");
        Principal principal = session.getPrincipalManager().getPrincipal("user1");
        if (AccessControlUtils.addAccessControlEntry(
            session,
            "/foo",
            principal,
            new String[]{Privilege.JCR_WRITE },
            true))
        {
            session.save();
        }

        Session s = repository.login(new SimpleCredentials("user1", "secret".toCharArray()));
        try
        {
            s.getNode("/foo").addNode("bar");
            s.save();
        }
        finally
        {
            s.logout();
        }
    }

    @Test
    public void testDenyRead() throws Exception
    {
        session.getUserManager().createUser("user2", "secret");
        Principal principal = session.getPrincipalManager().getPrincipal("user2");
        if (AccessControlUtils.addAccessControlEntry(
            session,
            "/foo",
            principal,
            new String[]{Privilege.JCR_READ },
            false))
        {
            session.save();
        }

        Session s = repository.login(new SimpleCredentials("user2", "secret".toCharArray()));
        try
        {
            s.getNode("/foo");
            fail();
        }
        catch (PathNotFoundException e)
        {
        }
        finally
        {
            s.logout();
        }
    }

    @After
    public void after() throws Exception
    {
        if (session != null)
        {
            int i = 0;
            while (true)
            {
                User user = (User) session.getUserManager().getAuthorizable("user" + (++i));
                if (user == null)
                {
                    break;
                }
                if (!user.isDisabled())
                {
                    user.disable("Removed");
                }
            }
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
