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

import org.apache.commons.lang.StringUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


@Endpoint
public class SumEndpoint
{

    public static final String NAMESPACE_URI = "http://www.diegoschivo.com/samples/spring-ws/sum";

    public static final String SUM_REQUEST_LOCAL_NAME = "sumRequest";

    public static final String SUM_RESPONSE_LOCAL_NAME = "sumResponse";

    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @PayloadRoot(localPart = SUM_REQUEST_LOCAL_NAME, namespace = NAMESPACE_URI)
    @ResponsePayload
    public Element handleSumRequest(@RequestPayload Element requestElement) throws ParserConfigurationException
    {
        NodeList children = requestElement.getChildNodes();
        Text aText = null;
        Text bText = null;
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                NodeList children2 = child.getChildNodes();
                for (int j = 0; j < children2.getLength(); j++)
                {
                    Node child2 = children2.item(j);
                    if (child2.getNodeType() == Node.TEXT_NODE)
                    {
                        if (StringUtils.equals(child.getLocalName(), "a"))
                        {
                            aText = (Text) child2;
                        }
                        else if (StringUtils.equals(child.getLocalName(), "b"))
                        {
                            bText = (Text) child2;
                        }
                    }
                }
            }
        }
        if (aText == null || bText == null)
        {
            throw new IllegalArgumentException("Could not find request addends");
        }
        int a = Integer.parseInt(aText.getNodeValue());
        int b = Integer.parseInt(bText.getNodeValue());
        int c = a + b;
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element response = document.createElementNS(NAMESPACE_URI, SUM_RESPONSE_LOCAL_NAME);
        response.setTextContent(Integer.toString(c));
        return response;
    }
}
