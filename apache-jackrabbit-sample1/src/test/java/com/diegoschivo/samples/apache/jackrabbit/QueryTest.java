package com.diegoschivo.samples.apache.jackrabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyType;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class QueryTest
{

    private static File directory;

    private static JackrabbitRepository repository;

    private JackrabbitSession session;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        directory = new File("target", QueryTest.class.getSimpleName());
        repository = RepositoryUtils.createRepository(
            directory,
            QueryTest.class.getResource(QueryTest.class.getSimpleName() + "-repository.xml").openStream());
    }

    @Before
    public void before() throws Exception
    {
        session = (JackrabbitSession) repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        Node foo = session.getRootNode().addNode("foo");
        foo.setProperty("message", "the quick brown fox");
        foo.setProperty("reference", new String[]{}, PropertyType.REFERENCE);
        Node bar = foo.addNode("bar");
        bar.setProperty("message", "jumps over");
        bar.setProperty("reference", new String[]{}, PropertyType.REFERENCE);
        Node baz = foo.addNode("baz");
        baz.setProperty("message", "the lazy dog");
        baz.setProperty("reference", new String[]{bar.getIdentifier() }, PropertyType.REFERENCE);
        session.save();
    }

    @Test
    public void testContains() throws Exception
    {
        Query query = session
            .getWorkspace()
            .getQueryManager()
            .createQuery(
                "SELECT u.* FROM [nt:unstructured] AS u" + " WHERE ISDESCENDANTNODE([/]) AND CONTAINS(u.*, $search)",
                Query.JCR_SQL2);
        query.bindValue("search", session.getValueFactory().createValue("the"));
        QueryResult result = query.execute();
        Set<String> paths = new HashSet<String>();
        for (NodeIterator it = result.getNodes(); it.hasNext();)
        {
            paths.add(it.nextNode().getPath());
        }
        assertEquals(2, paths.size());
        assertTrue(paths.contains("/foo"));
        assertTrue(paths.contains("/foo/baz"));
    }

    // @Test
    public void testJoin() throws Exception
    {
        Query query = session
            .getWorkspace()
            .getQueryManager()
            .createQuery(
                "SELECT u.* FROM [nt:unstructured] AS u"
                    + " INNER JOIN [nt:unstructured] AS v ON u.reference = v.[jcr:uuid]",
                Query.JCR_SQL2);
        QueryResult result = query.execute();
        Set<String> paths = new HashSet<String>();
        for (RowIterator it = result.getRows(); it.hasNext();)
        {
            Row row = it.nextRow();
            paths.add(row.getNode("u").getPath());
        }
        assertEquals(1, paths.size());
        assertTrue(paths.contains("/foo/bar"));
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
