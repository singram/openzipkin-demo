#!/bin/bash

CMD=$1
shift
PARAMS=$*
CURRENT_BRANCH_NAME=`git rev-parse --abbrev-ref HEAD`
SCRIPT_NAME=ms
export DOCKER_HOST_IP=`ifconfig docker0 | grep "inet addr" | awk -F'[: ]+' '{ print $4 }'`

function help {
    echo "Usage:"
    echo "    ./${SCRIPT_NAME} build"
    echo "    ./${SCRIPT_NAME} clean"
    echo "    ./${SCRIPT_NAME} docker_host_ip"
    echo "    ./${SCRIPT_NAME} docker_ips"
    echo "    ./${SCRIPT_NAME} redis_clear"
    echo "    ./${SCRIPT_NAME} redis_load_data"
    echo "    ./${SCRIPT_NAME} redis_monitor"
    echo "    ./${SCRIPT_NAME} portals"
    echo "    ./${SCRIPT_NAME} purge"
    echo "    ./${SCRIPT_NAME} purge_all"
    echo "    ./${SCRIPT_NAME} run_probes"
    echo "    ./${SCRIPT_NAME} run_pinpoint"
    echo "    ./${SCRIPT_NAME} run_zipkin"
    echo "    ./${SCRIPT_NAME} stats"
    echo
}

function build {
    cd microservice-common-java;                    ./gradlew clean build publishToMavenLocal; cd -
    cd microservices/core/swapi-proxy;              ./gradlew clean build; cd -
    cd microservices/core/mashup-service;           ./gradlew clean build; cd -

    find . -name *SNAPSHOT.jar -exec du -h {} \;
    docker build -t pinpoint-agent ./docker-pinpoint/pinpoint-agent/.
    docker-compose -f docker-compose-open-zipkin.yml -f docker-compose-pinpoint.yml -f docker-compose-probes.yml -f docker-compose-services.yml rm -fv
    docker-compose -f docker-compose-open-zipkin.yml -f docker-compose-pinpoint.yml -f docker-compose-probes.yml -f docker-compose-services.yml build
}

case $CMD in
    "build")
        build
        ;;
    "clean")
        echo "Removing unneeded containers and images..."
        docker rm `docker ps --no-trunc -aq`
        docker images -q --filter "dangling=true" | xargs docker rmi
        ;;
    "docker_host_ip")
        echo $DOCKER_HOST_IP
        ;;
    "docker_ips")
        docker ps -q | xargs -n 1 docker inspect --format '{{ .NetworkSettings.IPAddress }} {{ .Name }}' | sed 's/ \// /'
        ;;
    "help")
        help
        ;;
    "redis_clear")
        echo "Clear redis"
        docker exec -it openzipkindemo_redis_1 redis-cli FLUSHDB
        ;;
    "redis_load_data")
        echo "Load test data into redis"
        ./microservices/core/swapi-proxy/load_test_data $PARAMS
        ;;
    "redis_monitor")
        echo "Monitoring redis"
        docker exec -it openzipkindemo_redis_1 redis-cli MONITOR
        ;;
    "portals")
        echo "Viewing management portals..."
        firefox http://localhost:8081 \
                http://localhost:9990 \
                http://localhost:8082 \
                http://localhost:4040/ &
        ;;
    "purge")
        echo "Removing api containers and images..."
        docker rm $(docker ps --no-trunc -aq --filter="name=springcloudmicroservices")
        docker rmi -f $(docker images -q --filter="label=srai.micro.project=true")
        ;;
    "purge_all")
        echo "Removing all docker containers and images..."
        docker rm -f $(docker ps -a -q)
        docker rmi -f $(docker images -q)
        ;;
    "run_probes")
        echo "Starting services for docker probes..."
        exec docker-compose -f docker-compose-probes.yml up
        ;;
    "run_pinpoint")
        echo "Starting services for pinpoint demo..."
        exec docker-compose -f docker-compose-services.yml -f docker-compose-pinpoint.yml up
        ;;
    "run_zipkin")
        echo "Starting services for zipkin demo..."
        exec docker-compose -f docker-compose-services.yml -f docker-compose-open-zipkin.yml up
        ;;
    "stats")
        echo "Displaying docker stats..."
        docker stats $(docker ps  | sed 1d | awk '{print $NF}')
        ;;
    *)
        echo "I don't know what you want me to do"
        help
        ;;
esac
