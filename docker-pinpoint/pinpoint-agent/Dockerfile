# DESCRIPTION       Pinpoint APM Agent Volume
# TO_BUILD          docker build -t pinpoint-agent .
# TO_RUN            docker run --name=pinpoint-agent pinpoint-agent

# FROM alpine:3.3
FROM anapsix/alpine-java:jre8

ENV PINPOINT_VERSION=1.5.2

ADD configure-agent.sh /usr/local/bin/
ADD install-agent.sh /usr/local/bin/

RUN chmod a+x /usr/local/bin/install-agent.sh
RUN /usr/local/bin/install-agent.sh

VOLUME /assets/pinpoint-agent

ENTRYPOINT ["/usr/local/bin/configure-agent.sh"]
