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

package com.diegoschivo.samples.apache.cxf.sum;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.diegoschivo.samples.apache.cxf.sum package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SumResponse_QNAME = new QName("http://sum.cxf.apache.samples.diegoschivo.com/", "sumResponse");
    private final static QName _Sum_QNAME = new QName("http://sum.cxf.apache.samples.diegoschivo.com/", "sum");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.diegoschivo.samples.apache.cxf.sum
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SumResponse }
     * 
     */
    public SumResponse createSumResponse() {
        return new SumResponse();
    }

    /**
     * Create an instance of {@link Sum_Type }
     * 
     */
    public Sum_Type createSum_Type() {
        return new Sum_Type();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sum.cxf.apache.samples.diegoschivo.com/", name = "sumResponse")
    public JAXBElement<SumResponse> createSumResponse(SumResponse value) {
        return new JAXBElement<SumResponse>(_SumResponse_QNAME, SumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sum_Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sum.cxf.apache.samples.diegoschivo.com/", name = "sum")
    public JAXBElement<Sum_Type> createSum(Sum_Type value) {
        return new JAXBElement<Sum_Type>(_Sum_QNAME, Sum_Type.class, null, value);
    }

}
