FROM anapsix/alpine-java:jre8
LABEL  srai.micro.service="swapi-proxy" srai.micro.project="true"
VOLUME /tmp
RUN mkdir /build
ADD build/libs/swapi-proxy-0.0.1-SNAPSHOT.jar /build/.
ENTRYPOINT ["java","-Xmx64m", "-Xss256k","-Djava.security.egd=file:/dev/./urandom","-jar","/build/swapi-proxy-0.0.1-SNAPSHOT.jar"]
