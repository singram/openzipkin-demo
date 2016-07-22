# openzipkin-demo
Experiments with the open zipkin framework & pinpoint.

## Goals
- Explore opensource trace offerings applicable to micro services
- Develop framework service the combines both internal and external services and apply both openzipkin and pinpoint to it.

## Clone project

    git clone git@github.com:singram/openzipkin-demo.git
    git submodule init
    git submodule update


## Setup

Pre-requisites
- Java 1.8
- Docker
- Docker-compose

### Installation (debian base)

#### Install Docker

    apt-get install apparmor lxc cgroup-lite
    wget -qO- https://get.docker.com/ | sh
    sudo usermod -aG docker YourUserNameHere
    sudo service docker restart

#### Install Docker-compose  (1.6+)

*MAKE SURE YOU HAVE AN UP TO DATE VERSION OF DOCKER COMPOSE*

To check the version:

    docker-compose --version

To install the 1.6.0:

    sudo su
    curl -L https://github.com/docker/compose/releases/download/1.6.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    exit

Install supporting tools

    sudo apt-get install jq

### To build

Execute the following to run the services.

    ./ms build

#### Gradle daemon

To accelerate local development, it is recommended to run gradle daemonized.  This is as simple as running the following

    echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties

### To run

#### With open-zipkin

    ./ms run_zipkin

#### With pinpoint

    ./ms run_zipkin

### To access management portals

    ./ms portals

Note.  While this will open all relevant webpages not all will be populated depending on whether zipkin or pinpoint instrumentation is running.

## Overview

There are 3 key services
- swapi-proxy
-- A simple Spring Boot Java proxy service to the StarWars API which will cache returned data in Redis.
--  e.g. curl localhost:8083/character/2 | jq .
- quote-service
-- A simple Sinatra Ruby application which randomly returns a quote loaded from a local file.  For each call it also makes a call to www.google.com as an equivalent external dependancy to the swapi-proxy service
--  e.g. curl localhost:4567/ | jq .
- mashup-service
-- A SpringBoot Java application which calls both swapi-proxy and quote-services blending the data together in a payload.
--  e.g. curl localhost:8083/person/2 | jq .

To generate trace data execute

    curl localhost:8083/person/2 | jq .

In the docker compose files the sample rate for both zipkin and pinpoint is set to 100%.


## Debuging

### Mysql

    mysql -h 127.0.0.1 -u zipkin -pzipkin -D zipkin
    SELECT HEX(trace_id), HEX(id), HEX(parent_id), name, debug, duration, start_ts FROM zipkin_spans where trace_id = <trace id of interest> order by start_ts;

## References

### Quote source
- http://www.textfiles.com/humor/TAGLINES/

### Open Zipkin
- https://spring.io/blog/2016/02/15/distributed-tracing-with-spring-cloud-sleuth-and-spring-cloud-zipkin
- http://cloud.spring.io/spring-cloud-sleuth/spring-cloud-sleuth.html#_features
- https://programmaticponderings.wordpress.com/2016/02/15/diving-deeper-into-getting-started-with-spring-cloud/
- https://git-scm.com/book/en/v2/Git-Tools-Submodules
- https://github.com/openzipkin/docker-zipkin
- https://github.com/openzipkin/zipkin-tracer
- https://github.com/spring-cloud/spring-cloud-sleuth
- https://godoc.org/github.com/micro/go-platform/trace/zipkin/thrift/gen-go/zipkincore
- https://github.com/opentracing/opentracing-java/blob/master/opentracing-api/src/main/java/io/opentracing/tag/Tags.java

### Pinpoint
- https://github.com/naver/pinpoint
- https://github.com/naver/pinpoint/releases/tag/1.5.2
- https://github.com/naver/pinpoint/blob/master/quickstart/README.md

### Feign
- https://github.com/spring-cloud/spring-cloud-netflix/commit/a6b94a38fe5778970a122cf21c94d7159c1ce1dc
- https://blog.de-swaef.eu/the-netflix-stack-using-spring-boot-part-3-feign/
- https://github.com/Netflix/feign

### Other
- https://blog.buoyant.io/2016/05/17/distributed-tracing-for-polyglot-microservices/
- https://github.com/varnish/zipnish/
- https://github.com/inspectIT/inspectIT
- https://github.com/apache/incubator-htrace
- http://events.linuxfoundation.org/sites/events/files/slides/2015-03-05_apachecon2015__introducing_apache_htrace.pdf

### Java Annotations
- http://www.yegor256.com/2014/06/01/aop-aspectj-java-method-logging.html
- http://veerasundar.com/blog/2010/01/spring-aop-example-profiling-method-execution-time-tutorial/
- https://egkatzioura.wordpress.com/2016/05/29/aspect-oriented-programming-with-spring-boot/