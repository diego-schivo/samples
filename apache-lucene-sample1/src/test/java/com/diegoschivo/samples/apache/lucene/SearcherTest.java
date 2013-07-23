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

package com.diegoschivo.samples.apache.lucene;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class SearcherTest
{

    private static final String[] DOCUMENTS = {
        // 0
        "Lorem FOO ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        // 1
        "Ut enim BAR ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        // 2
        "Duis aute irure BAZ dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
        // 3
        "Excepteur sint occaecat cupidatat FOO non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        // 4
        "Lorem ipsum dolor sit amet, FOO consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        // 5
        "Ut enim ad minim veniam, quis FOO nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        // 6
        "Duis aute irure dolor in reprehenderit in FOO voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
        // 7
        "Excepteur sint occaecat cupidatat non proident, sunt in FOO culpa qui officia deserunt mollit anim id est laborum.",
        // 8
        "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed FOO do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        // 9
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris FOO nisi ut aliquip ex ea commodo consequat." };

    private static FSDirectory directory;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        directory = FSDirectory.open(new File("target", SearcherTest.class.getSimpleName()));
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, new StandardAnalyzer(Version.LUCENE_31));
        iwc.setOpenMode(OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(directory, iwc);
        try
        {
            for (String text : DOCUMENTS)
            {
                Document doc = new Document();
                // doc.add(new TextField("contents", text, Field.Store.NO));
                doc.add(new Field("contents", new StringReader(text)));
                writer.addDocument(doc);
            }
        }
        finally
        {
            writer.close();
        }
    }

    private IndexReader reader;

    private IndexSearcher searcher;

    private QueryParser parser;

    @Before
    public void before() throws Exception
    {
        // reader = DirectoryReader.open(directory);
        reader = IndexReader.open(directory);
        searcher = new IndexSearcher(reader);
        parser = new QueryParser(Version.LUCENE_31, "contents", new StandardAnalyzer(Version.LUCENE_31));
    }

    @Test
    public void testSearchFoo() throws Exception
    {
        Query query = parser.parse("foo");
        searcher.search(query, null, 100);
        TopDocs results;
        ScoreDoc[] hits;

        results = searcher.search(query, 10);
        assertEquals(8, results.totalHits);
        hits = results.scoreDocs;
        assertEquals(8, hits.length);

        results = searcher.search(query, 5);
        assertEquals(8, results.totalHits);
        hits = results.scoreDocs;
        assertEquals(5, hits.length);
    }

    @Test
    public void testSearchBar() throws Exception
    {
        Query query = parser.parse("bar");
        searcher.search(query, null, 100);

        TopDocs results = searcher.search(query, 10);
        assertEquals(1, results.totalHits);
        ScoreDoc[] hits = results.scoreDocs;
        assertEquals(1, hits.length);
        assertEquals(1, hits[0].doc);
    }

    @After
    public void after() throws Exception
    {
        reader.close();
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        FileUtils.deleteQuietly(directory.getDirectory());
    }
}
