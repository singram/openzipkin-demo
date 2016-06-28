#!/bin/bash

/usr/local/bin/configure-agent.sh
java -Xmx64m -Xss256k \
  -javaagent:/assets/pinpoint-agent/pinpoint-bootstrap-1.5.2.jar \
  -Dpinpoint.agentId=mashup-service-agent \
  -Dpinpoint.applicationName=mashup-service \
  -Djava.security.egd=file:/dev/./urandom -jar \
  /build/mashup-service-0.0.1-SNAPSHOT.jar
