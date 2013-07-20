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

package com.diegoschivo.samples.apache.cxf.jms_adder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.apache.cxf.transport.jms.JMSMessageHeadersType;
import org.apache.cxf.transport.jms.JMSPropertyType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


// apache-cxf-2.7.5-src/distribution/src/main/release/samples/jms_queue
public class JMSAdderTest
{

    private static final QName SERVICE_NAME = new QName(
        "http://samples.diegoschivo.com/apache/cxf/jms_adder",
        "JMSAdderService");

    private static final QName PORT_NAME = new QName("http://samples.diegoschivo.com/apache/cxf/jms_adder", "AdderPort");

    private BrokerService broker;

    private Endpoint ep;

    private JMSAdderPortType adder;

    @Before
    public void before() throws Exception
    {
        broker = new BrokerService();
        broker.setPersistenceAdapter(new MemoryPersistenceAdapter());
        broker.setDataDirectory("target/activemq-data");
        broker.addConnector("tcp://localhost:61616");
        broker.start();

        Object implementor = new JMSAdderImpl();
        String address = "http://cxf.apache.org/transports/jms";
        ep = Endpoint.publish(address, implementor);
        Runtime.getRuntime().addShutdownHook(new Thread()
        {

            @Override
            public void run()
            {
                if (ep != null)
                {
                    ep.stop();
                    ep = null;
                }
            }
        });

        File wsdl = new File("src/main/resources/jms_adder.wsdl");
        JMSAdderService service = new JMSAdderService(wsdl.toURI().toURL(), SERVICE_NAME);
        adder = service.getPort(PORT_NAME, JMSAdderPortType.class);
    }

    @Test
    public void testSum() throws Exception
    {
        int c = adder.sum(3, 2);
        assertEquals(5, c);
    }

    @Test
    public void testResponseContext() throws Exception
    {
        InvocationHandler handler = Proxy.getInvocationHandler(adder);
        assertTrue(handler instanceof BindingProvider);
        BindingProvider bp = (BindingProvider) handler;
        Map<String, Object> requestContext = bp.getRequestContext();
        JMSMessageHeadersType requestHeader = new JMSMessageHeadersType();
        requestHeader.setJMSCorrelationID("JMS_QUEUE_SAMPLE_CORRELATION_ID");
        requestHeader.setJMSExpiration(3600000L);
        JMSPropertyType propType = new JMSPropertyType();
        propType.setName("Test.Prop");
        propType.setValue("mustReturn");
        requestHeader.getProperty().add(propType);
        requestContext.put("org.apache.cxf.jms.client.request.headers", requestHeader);
        requestContext.put("org.apache.cxf.jms.client.timeout", 1000L);
        assertEquals(7, adder.sum(4, 3));
        Map<String, Object> responseContext = bp.getResponseContext();
        JMSMessageHeadersType responseHdr = (JMSMessageHeadersType) responseContext
            .get("org.apache.cxf.jms.client.response.headers");
        assertNotNull(responseHdr);
        assertEquals("JMS_QUEUE_SAMPLE_CORRELATION_ID", responseHdr.getJMSCorrelationID());
        assertNotNull(responseHdr.getProperty());
    }

    @After
    public void after() throws Exception
    {
        if (adder instanceof Closeable)
        {
            ((Closeable) adder).close();
        }
        if (ep != null)
        {
            ep.stop();
            ep = null;
        }
        if (broker != null)
        {
            broker.stop();
        }
    }

    @javax.jws.WebService(portName = "AdderPort", serviceName = "JMSAdderService", targetNamespace = "http://samples.diegoschivo.com/apache/cxf/jms_adder", endpointInterface = "com.diegoschivo.samples.apache.cxf.jms_adder.JMSAdderPortType", wsdlLocation = "jms_adder.wsdl")
    public class JMSAdderImpl implements JMSAdderPortType
    {

        public int sum(int a, int b)
        {
            return a + b;
        }
    }
}
