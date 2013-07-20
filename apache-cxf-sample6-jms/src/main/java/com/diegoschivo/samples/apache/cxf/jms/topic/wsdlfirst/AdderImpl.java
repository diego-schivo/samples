package com.diegoschivo.samples.apache.cxf.jms.topic.wsdlfirst;

import javax.xml.ws.Holder;


@javax.jws.WebService(portName = "AdderPort", serviceName = "AdderService", targetNamespace = "http://samples.diegoschivo.com/apache/cxf/jms/topic/wsdlfirst", endpointInterface = "com.diegoschivo.samples.apache.cxf.jms.topic.wsdlfirst.AdderPortType", wsdlLocation = "com/diegoschivo/samples/apache/cxf/jms/topic/wsdlfirst/adder.wsdl")
public class AdderImpl implements AdderPortType
{

    private Holder<Integer> c;

    public AdderImpl(Holder<Integer> c)
    {
        this.c = c;
    }

    public void sumOneWay(int a, int b)
    {
        c.value = a + b;
    }

}