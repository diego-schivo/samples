package com.diegoschivo.samples.apache.cxf.jms_adder;

import static org.junit.Assert.assertEquals;

import java.io.Closeable;
import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.junit.After;
import org.junit.Test;


public class JMSAdderTest
{

    private static final QName SERVICE_NAME = new QName(
        "http://samples.diegoschivo.com/apache/cxf/jms_adder",
        "JMSAdderService");

    private static final QName PORT_NAME = new QName("http://samples.diegoschivo.com/apache/cxf/jms_adder", "AdderPort");

    private BrokerService broker;

    private Endpoint ep;

    private int c;

    @Test
    public void testSumOneWay() throws Exception
    {
        broker = new BrokerService();
        broker.setPersistenceAdapter(new MemoryPersistenceAdapter());
        broker.setDataDirectory("target/activemq_data");
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
// System.out.println(wsdl.getAbsolutePath());
        JMSAdderService service = new JMSAdderService(wsdl.toURI().toURL(), SERVICE_NAME);
        JMSAdderPortType adder = service.getPort(PORT_NAME, JMSAdderPortType.class);
        adder.sumOneWay(3, 2);
        Thread.sleep(1000);
        assertEquals(5, c);

        if (adder instanceof Closeable)
        {
            ((Closeable) adder).close();
        }
    }

    @After
    public void after() throws Exception
    {
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

        public void sumOneWay(int a, int b)
        {
            c = a + b;
        }
    }
}
