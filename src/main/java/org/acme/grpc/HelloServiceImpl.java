package org.acme.grpc;

import org.acme.HelloRequest;
import org.acme.HelloResponse;
import org.acme.HelloServiceGrpc;
import org.acme.HelloServiceGrpc.HelloServiceImplBase;
import org.acme.client.SoapClient;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;







@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
   

    @Inject
    SoapClient soapClient;

    @Blocking
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        try {
            String soapResponse = soapClient.callSoapService(request.getClientName());
            
            System.out.println("soapResponse:"+soapResponse);
            
            HelloResponse response = HelloResponse.newBuilder()
                                                  .setMessage(soapResponse.replace("Welcome ", "Hello ")).build();
           
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
        	e.printStackTrace();
        	System.err.println("Error calling SOAP service: " + e.getMessage());
        	responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("SOAP Service Error: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}