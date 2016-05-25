FROM anapsix/alpine-java:jre8
LABEL  srai.micro.service="person-service-sw" srai.micro.project="true"
VOLUME /tmp
RUN mkdir /build
ADD build/libs/person-service-sw-0.0.1-SNAPSHOT.jar /build/person-service-sw-0.0.1-SNAPSHOT.jar
RUN ls -ali /build/
ENTRYPOINT ["java","-Xmx42m", "-Xss256k","-Djava.security.egd=file:/dev/./urandom","-jar","/build/person-service-sw-0.0.1-SNAPSHOT.jar"]
