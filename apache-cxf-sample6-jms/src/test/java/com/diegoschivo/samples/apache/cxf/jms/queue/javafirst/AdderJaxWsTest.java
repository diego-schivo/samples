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

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.junit.Before;
import org.junit.Test;

import com.diegoschivo.samples.apache.cxf.jms.queue.javafirst.Adder;


// apache-cxf-2.7.5-src/distribution/src/main/release/samples/java_first_jms
public class AdderJaxWsTest
{

    private static final String JMS_ENDPOINT_URI = "jms:queue:test.cxf.jmstransport.queue?timeToLive=1000"
        + "&jndiConnectionFactoryName=ConnectionFactory"
        + "&jndiInitialContextFactory"
        + "=org.apache.activemq.jndi.ActiveMQInitialContextFactory";

    private static final QName SERVICE_QNAME = new QName("http://impl.service.demo/", "HelloWorldImplService");

    private static final QName PORT_QNAME = new QName("http://service.demo/", "HelloWorldPort");

    private Adder adder;

    @Before
    public void before() throws Exception
    {
//        Endpoint.publish(JMS_ENDPOINT_URI, new AdderImpl());
//
//        Service service = Service.create(SERVICE_QNAME);
//        service.addPort(PORT_QNAME, JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID, JMS_ENDPOINT_URI);
//        adder = service.getPort(Adder.class);
    }

    @Test
    public void testSum() throws Exception
    {
//        int c = adder.sum(3, 2);
//        assertEquals(5, c);
    }
}
