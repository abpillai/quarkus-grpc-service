# Quarkus GRPC Service

GRPC service running on Quarkus


# HOW TO BUILD:

use java version 21

mvn clean install

mvn quarkus:dev


# REQUEST:

grpcurl -plaintext -d '{"clientName": "Test Client"}' localhost:9000 org.acme.HelloService/SayHello

# RESPONSE:

{
  "message": "Hello Test Client"
}
