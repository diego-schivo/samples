<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="urn:jsptld:http://java.sun.com/jsp/jstl/core">
  <jsp:directive.page contentType="text/html; charset=UTF-8" session="false" isELIgnored="false" />
  <jsp:directive.page import="java.io.*" />
  <jsp:directive.page import="javax.xml.transform.Source" />
  <jsp:directive.page import="javax.xml.transform.stream.StreamResult" />
  <jsp:directive.page import="org.springframework.core.io.*" />
  <jsp:directive.page import="org.springframework.ws.client.core.support.WebServiceGatewaySupport" />
  <jsp:directive.page import="org.springframework.xml.transform.ResourceSource" />
  <html>
    <head>
      <title>Sample: Spring WS</title>
    </head>
    <body>
      <h1>Sample: Spring WS</h1>
      <form action="${pageContext.request.contextPath}/index.jsp" method="get">
        <input type="text" name="a" value="${param.a}" size="4" /><![CDATA[ + ]]>
        <input type="text" name="b" value="${param.b}" size="4" /><![CDATA[ = ]]>
        <c:if test="${!empty(param.sum)}">
          <c:set var="sumRequest"><![CDATA[<?xml version="1.0" encoding="UTF-8"?>
<sumRequest xmlns="http://www.diegoschivo.com/samples/spring-ws/sum">
  <a>${param.a}</a>
  <b>${param.b}</b>
</sumRequest>]]>
          </c:set>
          <jsp:scriptlet><![CDATA[
try
{
    InputStream is = new ByteArrayInputStream(((String) pageContext.getAttribute("sumRequest")).getBytes("UTF-8"));
    Resource r = new InputStreamResource(is);
    Source s = new ResourceSource(r);
    WebServiceGatewaySupport sumClient = new WebServiceGatewaySupport()
    {

        {
            setDefaultUri("http://localhost:8080/spring-ws-sample2/services");
        }
    };
    sumClient.getWebServiceTemplate().sendSourceAndReceiveToResult(s, new StreamResult(out));
}
catch (Throwable e)
{
    out.print(e.getMessage());
}]]>
          </jsp:scriptlet>
        </c:if>
        <input type="submit" name="sum" value="OK" />
      </form>
    </body>
  </html>
</jsp:root>