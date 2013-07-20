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

package com.diegoschivo.samples.apache.cxf.jms.queue.javafirst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Closeable;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


// apache-cxf-2.7.5-src/distribution/src/main/release/samples/java_first_jms
public class AdderCxfTest
{

    private static final String JMS_ENDPOINT_URI = "jms:queue:test.cxf.jmstransport.queue?timeToLive=1000"
        + "&jndiConnectionFactoryName=ConnectionFactory"
        + "&jndiInitialContextFactory"
        + "=org.apache.activemq.jndi.ActiveMQInitialContextFactory";

    private Object broker;

    private Server server;

    private Adder adder;

    @Before
    public void before() throws Exception
    {
        Class< ? > brokerClass = getClass().getClassLoader().loadClass("org.apache.activemq.broker.BrokerService");
        assertNotNull(brokerClass);
        broker = brokerClass.newInstance();
        brokerClass.getMethod("addConnector", String.class).invoke(broker, "tcp://localhost:61616");
        brokerClass.getMethod("setDataDirectory", String.class).invoke(broker, "target/activemq-data");
        brokerClass.getMethod("start").invoke(broker);

        Object implementor = new AdderImpl();
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setServiceClass(Adder.class);
        svrFactory.setTransportId(JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID);
        svrFactory.setAddress(JMS_ENDPOINT_URI);
        svrFactory.setServiceBean(implementor);
        server = svrFactory.create();

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setTransportId(JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID);
        factory.setAddress(JMS_ENDPOINT_URI);
        adder = factory.create(Adder.class);
    }

    @Test
    public void testSum() throws Exception
    {
        int c = adder.sum(3, 2);
        assertEquals(5, c);
    }

    @After
    public void after() throws Exception
    {
        if (adder instanceof Closeable)
        {
            ((Closeable) adder).close();
        }
        if (server != null)
        {
            server.stop();
        }
        if (broker != null)
        {
            broker.getClass().getMethod("stop").invoke(broker);
        }
    }
}
