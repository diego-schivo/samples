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

package com.diegoschivo.samples.apache.cxf.jms.topic.wsdlfirst;

import static org.junit.Assert.assertEquals;

import java.io.Closeable;
import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Holder;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


// apache-cxf-2.7.5-src/distribution/src/main/release/samples/jms_pubsub
public class AdderTest
{

    private static final QName SERVICE_NAME = new QName(
        "http://samples.diegoschivo.com/apache/cxf/jms/topic/wsdlfirst",
        "AdderService");

    private static final QName PORT_NAME = new QName(
        "http://samples.diegoschivo.com/apache/cxf/jms/topic/wsdlfirst",
        "AdderPort");

    private BrokerService broker;

    private Endpoint ep;

    private AdderPortType adder;

    private Holder<Integer> c = new Holder<Integer>();

    @Before
    public void before() throws Exception
    {
        broker = new BrokerService();
        broker.setPersistenceAdapter(new MemoryPersistenceAdapter());
        broker.setDataDirectory("target/activemq-data");
        broker.addConnector("tcp://localhost:61616");
        broker.start();

        Object implementor = new AdderImpl(c);
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

        File wsdl = new File("src/main/resources/com/diegoschivo/samples/apache/cxf/jms/topic/wsdlfirst/adder.wsdl");
        AdderService service = new AdderService(wsdl.toURI().toURL(), SERVICE_NAME);
        adder = service.getPort(PORT_NAME, AdderPortType.class);
    }

    @Test
    public void testSum() throws Exception
    {
        adder.sumOneWay(3, 2);
        Thread.sleep(1000);
        assertEquals(5, c.value.intValue());
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
}
