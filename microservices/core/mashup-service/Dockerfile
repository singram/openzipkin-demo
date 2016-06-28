# FROM anapsix/alpine-java:jre8
FROM pinpoint-agent
LABEL  srai.micro.service="mashup-service" srai.micro.project="true"
VOLUME /tmp
RUN mkdir /build
ADD build/libs/mashup-service-0.0.1-SNAPSHOT.jar /build/.
ADD scripts/start_with_pinpoint.sh /.
RUN chmod +x /start_with_pinpoint.sh
ENTRYPOINT ["java","-Xmx64m", "-Xss256k","-Djava.security.egd=file:/dev/./urandom","-jar","/build/mashup-service-0.0.1-SNAPSHOT.jar"]
