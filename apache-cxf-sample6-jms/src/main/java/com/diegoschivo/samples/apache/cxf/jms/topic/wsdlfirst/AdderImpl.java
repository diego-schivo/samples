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