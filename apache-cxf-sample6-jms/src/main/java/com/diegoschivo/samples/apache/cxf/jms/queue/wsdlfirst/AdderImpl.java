package com.diegoschivo.samples.apache.cxf.jms.queue.wsdlfirst;

@javax.jws.WebService(portName = "AdderPort", serviceName = "AdderService", targetNamespace = "http://samples.diegoschivo.com/apache/cxf/jms/queue/wsdlfirst", endpointInterface = "com.diegoschivo.samples.apache.cxf.jms.queue.wsdlfirst.AdderPortType", wsdlLocation = "com/diegoschivo/samples/apache/cxf/jms/queue/wsdlfirst/adder.wsdl")
public class AdderImpl implements AdderPortType
{

    public int sum(int a, int b)
    {
        return a + b;
    }
}