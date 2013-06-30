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

package com.diegoschivo.samples.spring.ws.sum;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


@Endpoint
public class SumEndpoint
{

    public static final String NAMESPACE_URI = "http://www.diegoschivo.com/samples/spring-ws/sum";

    public static final String SUM_REQUEST_LOCAL_NAME = "sumRequest";

    public static final String SUM_RESPONSE_LOCAL_NAME = "sumResponse";

    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @PayloadRoot(localPart = SUM_REQUEST_LOCAL_NAME, namespace = NAMESPACE_URI)
    @Namespace(prefix = "n", uri = NAMESPACE_URI)
    @ResponsePayload
    public Element handleSumRequest(@XPathParam("//n:a") int a, @XPathParam("//n:b") int b)
        throws ParserConfigurationException
    {
        int c = a + b;
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element response = document.createElementNS(NAMESPACE_URI, SUM_RESPONSE_LOCAL_NAME);
        response.setTextContent(Integer.toString(c));
        return response;
    }
}
