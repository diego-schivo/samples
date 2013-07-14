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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.core.config.ConfigurationEntityResolver;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class DefaultRepositoryConfigurationFileTest
{

    private static File directory;

    private static RepositoryConfig config;

    @BeforeClass
    public static void installRepository() throws Exception
    {
        directory = new File("target", DefaultRepositoryConfigurationFileTest.class.getSimpleName());
        config = RepositoryConfig.install(directory);
    }

    @Test
    public void testFileSystemConfig() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(ConfigurationEntityResolver.INSTANCE);
        Document document = builder.parse(new File(directory, "repository.xml"));
        Assert
            .assertEquals(config.getFileSystem().getClass().getName(), getAttribute(document, "/FileSystem", "class"));
        Assert.assertEquals(config.getDataStore().getClass().getName(), getAttribute(document, "/DataStore", "class"));
        Assert.assertEquals(
            config.getSecurityConfig().getSecurityManagerConfig().getClassName(),
            getAttribute(document, "/Security/SecurityManager", "class"));
        Assert.assertEquals(
            config.getSecurityConfig().getAccessManagerConfig().getClassName(),
            getAttribute(document, "/Security/AccessManager", "class"));
        Assert.assertEquals(
            config.getSecurityConfig().getLoginModuleConfig().getClassName(),
            getAttribute(document, "/Security/LoginModule", "class"));
        Assert.assertEquals(
            config.getWorkspaceConfig("default").getFileSystem().getClass().getName(),
            getAttribute(document, "/Workspace/FileSystem", "class"));
        Assert.assertEquals(
            config.getWorkspaceConfig("default").getPersistenceManagerConfig().getClassName(),
            getAttribute(document, "/Workspace/PersistenceManager", "class"));
        Assert.assertEquals(
            "org.apache.jackrabbit.core.query.lucene.SearchIndex",
            getAttribute(document, "/Workspace/SearchIndex", "class"));
        Assert.assertEquals(
            config.getVersioningConfig().getFileSystem().getClass().getName(),
            getAttribute(document, "/Versioning/FileSystem", "class"));
        Assert.assertEquals(
            config.getVersioningConfig().getPersistenceManagerConfig().getClassName(),
            getAttribute(document, "/Versioning/PersistenceManager", "class"));
    }

    private static String getAttribute(Document document, String path, String name)
    {
        Element element = document.getDocumentElement();
        for (String token : StringUtils.split(StringUtils.removeStart(StringUtils.removeEnd(path, "/"), "/"), '/'))
        {
            element = (Element) element.getElementsByTagName(token).item(0);
        }
        return element.getAttributes().getNamedItem(name).getNodeValue();
    }

    @AfterClass
    public static void deleteRepository() throws Exception
    {
// FileUtils.deleteQuietly(directory);
    }
}
