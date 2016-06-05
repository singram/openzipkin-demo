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
    echo "    ./${SCRIPT_NAME} redis_clear"
    echo "    ./${SCRIPT_NAME} redis_load_data"
    echo "    ./${SCRIPT_NAME} redis_monitor"
    echo "    ./${SCRIPT_NAME} portals"
    echo "    ./${SCRIPT_NAME} purge"
    echo "    ./${SCRIPT_NAME} purge_all"
    echo "    ./${SCRIPT_NAME} run"
    echo "    ./${SCRIPT_NAME} stats"
    echo
}

function build {
    cd microservice-common-java;                          ./gradlew clean build publishToMavenLocal; cd -
    cd microservices/core/person-service-sw;              ./gradlew clean build; cd -

    find . -name *SNAPSHOT.jar -exec du -h {} \;
    docker-compose build
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
    "help")
        help
        ;;
    "redis_clear")
        echo "Clear redis"
        docker exec -it openzipkindemo_person-service-redis_1 redis-cli FLUSHDB
        ;;
    "redis_load_data")
        echo "Load test data into redis"
        ./microservices/core/person-service-sw/load_test_data $PARAMS
        ;;
    "redis_monitor")
        echo "Monitoring redis"
        docker exec -it openzipkindemo_person-service-redis_1 redis-cli MONITOR
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
    "run")
        echo "Starting services..."
        exec docker-compose up
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
