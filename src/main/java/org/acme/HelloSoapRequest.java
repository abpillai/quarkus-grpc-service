package org.acme;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HelloSoap", namespace = "http://www.flydubai.com/HelloSoap")
public class HelloSoapRequest {
    private String clientName;

    public HelloSoapRequest() {}

    public HelloSoapRequest(String clientName) {
        this.clientName = clientName;
    }

    @XmlElement(name = "ClientName")
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
