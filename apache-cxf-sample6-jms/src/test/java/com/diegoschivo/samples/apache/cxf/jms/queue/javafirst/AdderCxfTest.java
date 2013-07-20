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

import java.lang.reflect.Method;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.junit.Before;
import org.junit.Test;

import com.diegoschivo.samples.apache.cxf.jms.queue.javafirst.Adder;
import com.diegoschivo.samples.apache.cxf.jms.queue.javafirst.AdderImpl;


// apache-cxf-2.7.5-src/distribution/src/main/release/samples/java_first_jms
public class AdderCxfTest
{

    private static final String JMS_ENDPOINT_URI = "jms:queue:test.cxf.jmstransport.queue?timeToLive=1000"
        + "&jndiConnectionFactoryName=ConnectionFactory"
        + "&jndiInitialContextFactory"
        + "=org.apache.activemq.jndi.ActiveMQInitialContextFactory";

    private Adder adder;

    @Before
    public void before() throws Exception
    {
        Class< ? > brokerClass = getClass().getClassLoader().loadClass("org.apache.activemq.broker.BrokerService");
        assertNotNull(brokerClass);
        Object broker = brokerClass.newInstance();
        Method addConnectorMethod = brokerClass.getMethod("addConnector", String.class);
        addConnectorMethod.invoke(broker, "tcp://localhost:61616");
        Method setDataDirectory = brokerClass.getMethod("setDataDirectory", String.class);
        setDataDirectory.invoke(broker, "target/activemq-data");
        Method startMethod = brokerClass.getMethod("start");
        startMethod.invoke(broker);

        Object implementor = new AdderImpl();
        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setServiceClass(Adder.class);
        svrFactory.setTransportId(JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID);
        svrFactory.setAddress(JMS_ENDPOINT_URI);
        svrFactory.setServiceBean(implementor);
        svrFactory.create();

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
}
