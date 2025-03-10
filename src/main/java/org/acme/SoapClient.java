package org.acme;


import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;


@ApplicationScoped
public class SoapClient {
    private static final String SOAP_URL = "http://localhost:9090/HelloSoapService";

    public String callSoapService(String clientName) throws Exception {
        
        HelloSoapRequest request = new HelloSoapRequest(clientName);

        JAXBContext jaxbContext = JAXBContext.newInstance(HelloSoapRequest.class, HelloSoapResponse.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter xmlWriter = new StringWriter();
        marshaller.marshal(request, xmlWriter);
       
        
        String soapXml="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"+ "xmlns:hel=\"http://www.flydubai.com/HelloSoap\"><soapenv:Header/><soapenv:Body><hel:HelloSoap><ClientName>Test Client</ClientName></hel:HelloSoap></soapenv:Body></soapenv:Envelope>";
        
       
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SOAP_URL);
        Invocation.Builder invocationBuilder = target.request("text/xml") 
                                                     .header("SOAPAction", "") 
                                                     .header("Accept", "text/xml");

        String responseXml = invocationBuilder.post(Entity.entity(soapXml, "text/xml"), String.class);
        String extractedXml = extractSoapBody(responseXml);
        return extractedXml;
       
    }
    
   
    private String extractSoapBody(String responseXml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(responseXml.getBytes()));

        NodeList nodes = document.getElementsByTagNameNS("http://www.flydubai.com/HelloSoap", "HelloSoapResponse");

        if (nodes.getLength() == 0) {
            throw new Exception("SOAP Response does not contain HelloSoapResponse");
        }

        return nodes.item(0).getTextContent().trim(); 
    }
    
   
}
