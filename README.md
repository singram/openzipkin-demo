# openzipkin-demo
Experiments with the open zipkin framework

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

### To run

Start the ELK service which receives all docker logs

    ./ms run_elk

Execute the following to run the services.

    ./ms build
    ./ms run

### Gradle daemon

To accelerate local development, it is recommended to run gradle daemonized.  This is as simple as running the following

    echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties

## TODO
- Rename service to swapi-proxy
- Add Hystrix support for timeouts or disable.

## References

https://spring.io/blog/2016/02/15/distributed-tracing-with-spring-cloud-sleuth-and-spring-cloud-zipkin
http://cloud.spring.io/spring-cloud-sleuth/spring-cloud-sleuth.html#_features
https://programmaticponderings.wordpress.com/2016/02/15/diving-deeper-into-getting-started-with-spring-cloud/
https://git-scm.com/book/en/v2/Git-Tools-Submodules
https://github.com/openzipkin/docker-zipkin
https://github.com/openzipkin/zipkin-tracer
https://github.com/spring-cloud/spring-cloud-sleuth