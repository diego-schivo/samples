<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="urn:jsptld:http://java.sun.com/jsp/jstl/core">
  <jsp:directive.page contentType="text/html; charset=UTF-8" session="false" isELIgnored="false" />
  <jsp:directive.page import="javax.xml.namespace.QName" />
  <jsp:directive.page import="javax.xml.ws.Service" />
  <jsp:directive.page import="javax.xml.ws.soap.SOAPBinding" />
  <jsp:directive.page import="com.diegoschivo.samples.apache.cxf.sum.Sum" />
  <html>
    <head>
      <title>Apache CXF Sample 1</title>
    </head>
    <body>
      <h1>Apache CXF Sample 1</h1>
      <form action="${pageContext.request.contextPath}/index.jsp" method="get">
        <input type="text" name="a" value="${param.a}" size="4" /><![CDATA[ + ]]>
        <input type="text" name="b" value="${param.b}" size="4" /><![CDATA[ = ]]>
        <c:if test="${!empty(param.sum)}">
          <jsp:scriptlet><![CDATA[
try
{
    QName serviceName = new QName("http://sum.cxf.apache.samples.diegoschivo.com/", "Sum");
    QName portName = new QName("http://sum.cxf.apache.samples.diegoschivo.com/", "SumPort");
    Service service = Service.create(serviceName);
    String endpointAddress = "http://localhost:8080/apache-cxf-sample1/services/sum";
    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);
    Sum sum = service.getPort(Sum.class);
    int a = Integer.parseInt(request.getParameter("a"));
    int b = Integer.parseInt(request.getParameter("b"));
    int c = sum.sum(a, b);
    out.print(Integer.toString(c));
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